package click.seichi.regenerateworld.commands

import click.seichi.regenerateworld.Multiverse
import click.seichi.regenerateworld.utils.IError
import click.seichi.regenerateworld.utils.SeedPatterns
import com.github.michaelbull.result.getOrElse
import com.github.michaelbull.result.mapBoth
import com.github.michaelbull.result.toResultOr
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

object RegenerateCommand : TabExecutor {
    override fun onTabComplete(
        sender: CommandSender?,
        command: Command?,
        alias: String?,
        args: Array<out String>?
    ): MutableList<String> {
        TODO("Not yet implemented")
    }

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

        val commandType =
            CommandType.values().find { it.name.lowercase() == args[0].lowercase() }
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

                Multiverse.findMvWorld(world).mapBoth(
                    success = {
                        setOf("ワールドの再生成を開始します。", "この処理には時間がかかる可能性があります。")
                            .map { msg -> "${ChatColor.GREEN}$msg" }
                            .forEach { msg -> sender.sendMessage(msg) }
                        // TODO: Seed変更に対応する
                        Multiverse.regenWorld(it, SeedPatterns.NEW_SEED).mapBoth(
                            success = { sender.sendMessage("${ChatColor.GREEN}ワールドの再生成が終了しました。") },
                            failure = { err -> err.withLog(sender) }
                        )
                    },
                    failure = { it.withLog(sender) }
                )
            }
            // TODO: 実装する
            CommandType.SCHEDULE -> {
                sender.sendMessage("schedule")
                executeScheduleSubCommand(args.drop(1), sender)
            }
            // TODO: 実装する
            CommandType.LIST -> sender.sendMessage("list")
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
        else -> TODO("Unimplemented")
    }
}

private enum class RegenerateCommandError(private val reason: String) : IError {
    ARGS_ARE_SUFFICIENT("引数が不足しています。詳細については「/rw help」を参照してください。"),
    OPERATOR_IS_INCORRECT("操作識別子が間違っています。詳細については「/rw help」を参照してください。"),
    WORLD_IS_NOT_FOUND("指定されたBukkitワールドは見つかりませんでした。");

    override fun errorName() = this.name
    override fun reason() = this.reason
}

// TODO 引数の数を適切に
// TODO: すべての再生成計画を表示するコマンド
private enum class CommandType(val usage: String, val description: String, val argsSize: Int) {
    HELP("/rw help", "RegenerateWorldのコマンドの一覧を表示します。", 0),
    REGEN("/rw regen <ワールド名、コンマ区切り>", "指定されたワールドの再生成を行います。", 1),
    SCHEDULE("/rw schedule <add/edit/remove>", "再生成スケジュールを追加・変更・削除します。", 1),
    LIST("/rw list", "有効な再生成予定の一覧を表示します。", 0)
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
        1
    ),
    EDIT(
        "/rw schedule edit <UUID> [w:world_SW,world_SW_2 i:1m s:random_new n:newseed]",
        "指定された再生成のスケジュールを編集します。",
        2
    ),
    REMOVE("/rw schedule remove <UUID、コンマ区切り>", "指定された再生成のスケジュールを削除します。", 1)
}