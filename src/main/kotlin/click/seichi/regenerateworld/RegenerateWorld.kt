package click.seichi.regenerateworld

import click.seichi.regenerateworld.commands.RegenerateCommand
import click.seichi.regenerateworld.listener.RegenerateWorldEventListener
import click.seichi.regenerateworld.utils.Util
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.time.ZonedDateTime

class RegenerateWorld : JavaPlugin() {
    companion object {
        lateinit var plugin: RegenerateWorld
            private set
    }

    override fun onEnable() {
        plugin = this
        Multiverse.load()
        Config.load()
        // TODO: debug
        println(Config.loadPlans())

        Config.loadPlans().forEach {
            val now = ZonedDateTime.now()
            if (it.lastRegeneratedDate.isBefore(now)) {
                Config.setData(PathType.DATE, it.id, now.toString())
                RegenerateTask(it).runTaskTimer(plugin, 0, 20L * 1) //TODO: 1secごとなので1minに
            } else Util.scheduleRegenerateTask(it)
        }

        getCommand("regenerateworld").executor = RegenerateCommand
        server.pluginManager.registerEvents(RegenerateWorldEventListener, this)
    }

    override fun onDisable() {
        Bukkit.getScheduler().cancelTasks(plugin)
    }
}