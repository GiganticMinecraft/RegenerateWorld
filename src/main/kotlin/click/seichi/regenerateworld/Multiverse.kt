package click.seichi.regenerateworld

import click.seichi.regenerateworld.events.PreRegenerateWorldEvent
import click.seichi.regenerateworld.events.RegenerateWorldEvent
import com.github.michaelbull.result.*
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

    private fun regenWorld(
        world: MultiverseWorld,
        isNewSeed: Boolean,
        isRandomSeed: Boolean,
        seed: String?
    ) {
        val preEvent = PreRegenerateWorldEvent(world.name)
        Bukkit.getPluginManager().callEvent(preEvent)
        if (preEvent.isCancelled) return

        instance.mvWorldManager.regenWorld(world.name, isNewSeed, isRandomSeed, seed)

        Bukkit.getPluginManager().callEvent(RegenerateWorldEvent(world.name))
    }

    fun regenWorld(
        world: MultiverseWorld,
        seedType: SeedType,
        seed: String? = null
    ): Result<Unit, MultiverseError> =
        if (seedType == SeedType.NEW_SEED && seed == null) Err(MultiverseError.SEED_IS_NON_NULL)
        else Ok(regenWorld(world, seedType.isNewSeed, seedType.isRandomSeed, seed))

    fun getSpawnWorld(): MultiverseWorld = instance.mvWorldManager.spawnWorld

    fun setSpawnLocation(world: MultiverseWorld, location: Location) {
        world.spawnLocation = location
    }
}

enum class SeedType(val isNewSeed: Boolean, val isRandomSeed: Boolean) {
    CURRENT_SEED(false, false),
    NEW_SEED(true, false),
    RANDOM_NEW_SEED(true, true)
}

private enum class MultiverseError(private val reason: String) : IError {
    SEED_IS_NON_NULL("Seed値が指定されていません。");

    override fun errorName() = this.name
    override fun reason() = this.reason
}
