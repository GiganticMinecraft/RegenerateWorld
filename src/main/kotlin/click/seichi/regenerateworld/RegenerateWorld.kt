package click.seichi.regenerateworld

import click.seichi.regenerateworld.commands.RegenerateCommand
import click.seichi.regenerateworld.listener.RegenerateWorldEventListener
import org.bukkit.plugin.java.JavaPlugin

class RegenerateWorld : JavaPlugin() {
    companion object {
        lateinit var plugin: RegenerateWorld
            private set
    }

    override fun onEnable() {
        plugin = this
        Multiverse.load()

        getCommand("regenerateworld").executor = RegenerateCommand
        server.pluginManager.registerEvents(RegenerateWorldEventListener, this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}