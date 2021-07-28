package click.seichi.regenerateworld

import click.seichi.regenerateworld.events.PreRegenerateWorldEvent
import click.seichi.regenerateworld.events.RegenerateWorldEvent
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.toResultOr
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

    fun findMvWorld(worldName: String) =
        instance.mvWorldManager.getMVWorld(worldName)
            .toResultOr { MultiverseError.WORLD_IS_NOT_FOUND }

    fun findMvWorld(world: World) =
        instance.mvWorldManager.getMVWorld(world).toResultOr { MultiverseError.WORLD_IS_NOT_FOUND }

    private fun regenWorld(
        world: MultiverseWorld,
        isNewSeed: Boolean,
        isRandomSeed: Boolean,
        seed: String?
    ): Result<Boolean, MultiverseError> {
        val preEvent = PreRegenerateWorldEvent(world.name)
        Bukkit.getPluginManager().callEvent(preEvent)
        if (preEvent.isCancelled) return Err(MultiverseError.EVENT_IS_CANCELLED)

        val result = instance.mvWorldManager.regenWorld(world.name, isNewSeed, isRandomSeed, seed)
        Bukkit.getPluginManager().callEvent(RegenerateWorldEvent(world.name))

        return Ok(result)
    }

    fun regenWorld(
        world: MultiverseWorld,
        seedType: SeedType,
        seed: String? = null
    ): Result<Boolean, MultiverseError> =
        if (seedType == SeedType.NEW_SEED && seed == null) Err(MultiverseError.SEED_IS_NON_NULL)
        else regenWorld(world, seedType.isNewSeed, seedType.isRandomSeed, seed)

    fun getSpawnWorld(): MultiverseWorld = instance.mvWorldManager.spawnWorld

    fun setSpawnLocation(world: MultiverseWorld, location: Location) {
        world.spawnLocation = location
    }
}

enum class SeedType(val isNewSeed: Boolean, val isRandomSeed: Boolean) {
    CURRENT_SEED(false, false),
    NEW_SEED(true, false),
    RANDOM_NEW_SEED(true, true);

    fun isSeedNecessary() = this == NEW_SEED
}

enum class MultiverseError(private val reason: String) : IError {
    EVENT_IS_CANCELLED("イベントがキャンセルされています。"),
    SEED_IS_NON_NULL("シード値が指定されていません。"),
    WORLD_IS_NOT_FOUND("指定されたMultiverseワールドは存在しません。");

    override fun errorName() = this.name
    override fun reason() = this.reason
}
