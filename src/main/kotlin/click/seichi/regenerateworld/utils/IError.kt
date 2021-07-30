package click.seichi.regenerateworld.utils

import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

interface IError {
    fun errorName(): String
    fun reason(): String
    fun withLog(sender: CommandSender): IError = run {
        sender.sendMessage("${ChatColor.RED}${reason()}")
        return this
    }

    fun withServerLog(additionalMessage: String = "なし"): IError = run {
        setOf("処理中にエラーが発生しました。", "${errorName()}: ${reason()}(追加情報: $additionalMessage)")
            .forEach { Logger.severeWithPrefix(it) }
        return this
    }
}