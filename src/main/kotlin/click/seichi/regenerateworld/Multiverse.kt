package click.seichi.regenerateworld

import com.onarandombox.MultiverseCore.MultiverseCore
import com.onarandombox.MultiverseCore.api.MultiverseWorld
import org.bukkit.Location
import org.bukkit.World

object Multiverse {
    private lateinit var instance: MultiverseCore

    fun load() {
        val plugin = RegenerateWorld.plugin.server.pluginManager.getPlugin("Multiverse-Core")

        if (plugin != null && plugin is MultiverseCore) instance = plugin
    }

    fun findMvWorld(world: World): MultiverseWorld? = instance.mvWorldManager.getMVWorld(world)

    private fun regenWorld(world: MultiverseWorld, isNewSeed: Boolean, isRandomSeed: Boolean, seed: String?) =
        instance.mvWorldManager.regenWorld(world.name, isNewSeed, isRandomSeed, seed)

    fun regenWorldWithCurrentSeed(world: MultiverseWorld) =
        regenWorld(world, isNewSeed = false, isRandomSeed = false, seed = null)

    fun regenWorldWithNewSeed(world: MultiverseWorld, seed: String) =
        regenWorld(world, isNewSeed = true, isRandomSeed = false, seed)

    fun regenWorldWithRandomNewSeed(world: MultiverseWorld) =
        regenWorld(world, isNewSeed = true, isRandomSeed = true, seed = null)

    fun setSpawnLocation(world: MultiverseWorld, location: Location) { world.spawnLocation = location }
}