package click.seichi.regenerateworld

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

interface IError {
    fun errorName(): String
    fun reason(): String
    fun withLog(sender: CommandSender): IError = run {
        sender.sendMessage("${ChatColor.RED}${reason()}")
        return this
    }

    fun withServerLog(): IError = run {
        setOf("処理中にエラーが発生しました。", "${errorName()}: ${reason()}")
            .forEach { Bukkit.getServer().logger.severe(it) }
        return this
    }
}