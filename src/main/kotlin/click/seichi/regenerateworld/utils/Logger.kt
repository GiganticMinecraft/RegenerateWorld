package click.seichi.regenerateworld.utils

import org.bukkit.Bukkit

object Logger {
    private val serverLogger = Bukkit.getServer().logger
    private const val MESSAGE_PREFIX = "[RegenerateWorld]"

    fun info(msg: String) = serverLogger.info(msg)
    fun infoWithPrefix(msg: String) = info("$MESSAGE_PREFIX $msg")

    fun severe(msg: String) = serverLogger.severe(msg)
    fun severeWithPrefix(msg: String) = severe("$MESSAGE_PREFIX $msg")
}