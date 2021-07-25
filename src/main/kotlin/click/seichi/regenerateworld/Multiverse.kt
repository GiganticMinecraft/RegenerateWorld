package click.seichi.regenerateworld

import com.onarandombox.MultiverseCore.MultiverseCore

object Multiverse {
    private lateinit var instance: MultiverseCore

    fun load() {
        val plugin = RegenerateWorld.plugin.server.pluginManager.getPlugin("Multiverse-Core")

        if (plugin != null && plugin is MultiverseCore) instance = plugin
    }
}