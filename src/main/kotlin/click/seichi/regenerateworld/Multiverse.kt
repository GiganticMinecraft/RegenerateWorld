package click.seichi.regenerateworld

import click.seichi.regenerateworld.events.PreRegenerateWorldEvent
import click.seichi.regenerateworld.events.RegenerateWorldEvent
import com.onarandombox.MultiverseCore.MultiverseCore
import com.onarandombox.MultiverseCore.api.MultiverseWorld
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World

object Multiverse {
    private lateinit var instance: MultiverseCore

    fun load() {
        val plugin = RegenerateWorld.plugin.server.pluginManager.getPlugin("Multiverse-Core")

        if (plugin != null && plugin is MultiverseCore) instance = plugin
    }

    fun findMvWorld(world: World): MultiverseWorld? = instance.mvWorldManager.getMVWorld(world)

    private fun regenWorld(world: MultiverseWorld, isNewSeed: Boolean, isRandomSeed: Boolean, seed: String?) {
        val preEvent = PreRegenerateWorldEvent(world.name)
        Bukkit.getPluginManager().callEvent(preEvent)
        if (preEvent.isCancelled) return

        instance.mvWorldManager.regenWorld(world.name, isNewSeed, isRandomSeed, seed)

        Bukkit.getPluginManager().callEvent(RegenerateWorldEvent(world.name))
    }

    fun regenWorldWithCurrentSeed(world: MultiverseWorld) =
        regenWorld(world, isNewSeed = false, isRandomSeed = false, seed = null)

    fun regenWorldWithNewSeed(world: MultiverseWorld, seed: String) =
        regenWorld(world, isNewSeed = true, isRandomSeed = false, seed)

    fun regenWorldWithRandomNewSeed(world: MultiverseWorld) =
        regenWorld(world, isNewSeed = true, isRandomSeed = true, seed = null)

    fun getSpawnWorld() = instance.mvWorldManager.spawnWorld

    fun setSpawnLocation(world: MultiverseWorld, location: Location) { world.spawnLocation = location }
}