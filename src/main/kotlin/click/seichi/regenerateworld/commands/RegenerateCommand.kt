package click.seichi.regenerateworld.commands

import click.seichi.regenerateworld.Config
import click.seichi.regenerateworld.Multiverse
import click.seichi.regenerateworld.utils.IError
import click.seichi.regenerateworld.utils.SeedPatterns
import com.github.michaelbull.result.*
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import kotlin.onFailure
import kotlin.runCatching

object RegenerateCommand : TabExecutor {
    override fun onTabComplete(
        sender: CommandSender?,
        command: Command?,
        alias: String?,
        args: Array<out String>?
    ): MutableList<String> =
        when (args?.size) {
            0 -> CommandType.values().map { it.name.lowercase() }
            1 -> CommandType.safeValueOf(args[0])?.takeIf { it == CommandType.SCHEDULE }?.let {
                ScheduleCommandSubType.values().map { it.name.lowercase() }
            }
            else -> listOf()
        }?.toMutableList() ?: mutableListOf()

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (args.isEmpty()) {
            RegenerateCommandError.ARGS_ARE_SUFFICIENT.withLog(sender)
            return true
        }

        val commandType = CommandType.safeValueOf(args[0])
            .toResultOr { RegenerateCommandError.OPERATOR_IS_INCORRECT.withLog(sender) }
            .getOrElse { return true }

        if (args.size < commandType.argsSize) {
            RegenerateCommandError.ARGS_ARE_SUFFICIENT.withLog(sender)
            return true
        }

        when (commandType) {
            CommandType.HELP -> {
                sender.sendMessage("${ChatColor.WHITE}-RegenerateWorld Commands-")
                CommandType.values().forEach {
                    sender.sendMessage("${ChatColor.GOLD}${it.usage}${ChatColor.WHITE}: ${it.description}")
                }
            }
            CommandType.REGEN -> {
                val world = Bukkit.getWorld(args[1])
                    .toResultOr { RegenerateCommandError.WORLD_IS_NOT_FOUND.withLog(sender) }
                    .getOrElse { return true }
                val seedPattern = SeedPatterns.safeValueOf(args[2]).getOrElse {
                    it.withLog(sender)
                    return false
                }
                val newSeed = runCatching { args[3] }.onFailure {
                    if (seedPattern.isSeedNecessary()) {
                        RegenerateCommandError.ARGS_ARE_SUFFICIENT.withLog(sender)
                        return true
                    }
                }.getOrNull()

                Multiverse.findMvWorld(world).andThen { mvWorld ->
                    setOf("ワールドの再生成を開始します。", "この処理には時間がかかる可能性があります。")
                        .map { msg -> "${ChatColor.GREEN}$msg" }
                        .forEach { msg -> sender.sendMessage(msg) }

                    Multiverse.regenWorld(mvWorld, seedPattern, newSeed).onSuccess {
                        sender.sendMessage("${ChatColor.GREEN}ワールドの再生成が終了しました。")
                    }
                }.onFailure { it.withLog(sender) }
            }
            CommandType.SCHEDULE -> {
                // TODO:
                sender.sendMessage("schedule")
                executeScheduleSubCommand(args.drop(1), sender)
            }
            CommandType.LIST -> {
                val commands = Config.loadPlans()
                    .map { "${it.id}: ${it.worlds} | ${it.interval} | ${it.seedPatterns.name} | ${it.seed ?: "---"}" }
                val message =
                    if (commands.isEmpty()) "再生成予定は存在しません。"
                    else setOf(
                        "-RegenerateWorld ScheduleLists-",
                        "UUID: ワールド | 再生成間隔 | シード値の設定 | 指定したシード値"
                    ).map { "${ChatColor.WHITE}$it" }.plus(commands).joinToString("\n")
                sender.sendMessage(message)
            }
        }

        return true
    }
}

private fun executeScheduleSubCommand(args: List<String>, sender: CommandSender) {
    val subCommandType =
        ScheduleCommandSubType.values().find { it.name.lowercase() == args[0].lowercase() }
            .toResultOr { RegenerateCommandError.OPERATOR_IS_INCORRECT.withLog(sender) }
            .getOrElse { return }

    if (args.size < subCommandType.argsSize) {
        RegenerateCommandError.ARGS_ARE_SUFFICIENT.withLog(sender)
        return
    }

    when (subCommandType) {
        ScheduleCommandSubType.HELP -> {
            sender.sendMessage("${ChatColor.WHITE}-RegenerateWorld ScheduleSubCommands-")
            ScheduleCommandSubType.values().forEach {
                sender.sendMessage("${ChatColor.GOLD}${it.usage}${ChatColor.WHITE}: ${it.description}")
            }
        }
        // TODO: 実装する
        else -> sender.sendMessage("Unimplemented")
    }
}

private enum class RegenerateCommandError(private val reason: String) : IError {
    ARGS_ARE_SUFFICIENT("引数が不足しています。詳細については「/rw help」を参照してください。"),
    OPERATOR_IS_INCORRECT("操作識別子が間違っています。詳細については「/rw help」を参照してください。"),
    WORLD_IS_NOT_FOUND("指定されたBukkitワールドは見つかりませんでした。");

    override fun errorName() = this.name
    override fun reason() = this.reason
}

private enum class CommandType(val usage: String, val description: String, val argsSize: Int) {
    HELP("/rw help", "RegenerateWorldのコマンドの一覧を表示します。", 0),
    REGEN("/rw regen <ワールド名> <シード値パターン> <新しいシード値>", "指定されたワールドの再生成を行います。", 2),
    SCHEDULE("/rw schedule <add/edit/remove>", "再生成スケジュールを追加・変更・削除します。", 3),
    LIST("/rw list", "有効な再生成予定の一覧を表示します。", 0);

    companion object {
        fun safeValueOf(str: String) = values().find { it.name.lowercase() == str.lowercase() }
    }
}

private enum class ScheduleCommandSubType(
    val usage: String,
    val description: String,
    val argsSize: Int
) {
    HELP("/rw schedule help", "RegenerateWorldのコマンドのうち、/rw scheduleのサブコマンドの一覧を表示します。", 0),
    ADD(
        "/rw schedule add [w:world_SW,world_SW_2 i:1m s:random_new n:newseed]",
        "再生成のスケジュールを追加します。",
        3
    ),
    EDIT(
        "/rw schedule edit <UUID> [w:world_SW,world_SW_2 i:1m s:random_new n:newseed]",
        "指定された再生成のスケジュールを編集します。",
        4
    ),
    REMOVE("/rw schedule remove <UUID、コンマ区切り>", "指定された再生成のスケジュールを削除します。", 3)
}