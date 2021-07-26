package click.seichi.regenerateworld

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
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}