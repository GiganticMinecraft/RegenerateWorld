package click.seichi.regenerateworld.utils

import org.bukkit.Bukkit

object Logger {
    private val serverLogger = Bukkit.getServer().logger
    private const val MESSAGE_PREFIX = "[RegenerateWorld]"

    fun infoWithoutPrefix(msg: String) = serverLogger.info(msg)
    fun info(msg: String) = serverLogger.info("$MESSAGE_PREFIX $msg")

    fun severeWithoutPrefix(msg: String) = serverLogger.severe(msg)
    fun severe(msg: String) = serverLogger.severe("$MESSAGE_PREFIX $msg")
}