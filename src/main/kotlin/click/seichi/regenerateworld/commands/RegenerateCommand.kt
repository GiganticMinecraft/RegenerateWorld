package click.seichi.regenerateworld.commands

import click.seichi.regenerateworld.Multiverse
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
            sender.sendMessage("${ChatColor.RED}引数が不足しています。詳細については「/rw help」を参照してください。")
            return true
        }
        val commandType =
            CommandType.values().find { it.name.lowercase() == args[0].lowercase() } ?: run {
                sender.sendMessage("${ChatColor.RED}操作識別子が間違っています。詳細については「/rw help」を参照してください。")
                return true
            }

        when(commandType) {
            CommandType.HELP -> CommandType.values().forEach {
                sender.sendMessage("${ChatColor.GOLD}${it.usage}${ChatColor.WHITE}: ${it.description}")
            }
            CommandType.REGEN -> {
                val world = Bukkit.getWorld(args[1]) ?: run {
                    sender.sendMessage("${ChatColor.RED}指定されたBukkitワールドは見つかりませんでした。")
                    return true
                }
                Multiverse.findMvWorld(world)?.let {
                    setOf("ワールドの再生成を開始します。", "この処理には時間がかかる可能性があります。")
                        .map { msg -> "${ChatColor.GREEN}$msg" }
                        .forEach { msg -> sender.sendMessage(msg) }
                    // TODO: seedを選べるように
                    Multiverse.regenWorldWithRandomNewSeed(it)
                    sender.sendMessage("${ChatColor.GREEN}ワールドの再生成が終了しました。")
                } ?: run {
                    sender.sendMessage("${ChatColor.RED}指定されたMultiverseワールドは見つかりませんでした。")
                }
            }
            // TODO: 実装する
            CommandType.SCHEDULE -> sender.sendMessage("schedule")
        }

        return true
    }
}

private enum class CommandType(val usage: String, val description: String) {
    HELP("/rw help", "RegenerateWorldのコマンドの一覧を表示します。"),
    REGEN("/rw regen", "指定されたワールドの再生成を行います。"),
    SCHEDULE("/rw schedule", "指定されたワールドの再生成をスケジュールします。")
}