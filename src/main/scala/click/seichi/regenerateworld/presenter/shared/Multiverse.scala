package click.seichi.regenerateworld.presenter.shared

import click.seichi.regenerateworld.presenter.RegenerateWorld.INSTANCE
import com.onarandombox.MultiverseCore.MultiverseCore
import com.onarandombox.MultiverseCore.api.MultiverseWorld
import org.bukkit.World

case object Multiverse {
  private val instance =
    INSTANCE
      .getServer
      .getPluginManager
      .getPlugin("Multiverse-Core")
      .asInstanceOf[MultiverseCore]

  def fromBukkitWorld(world: World): Option[MultiverseWorld] =
    Option(instance.getMVWorldManager.getMVWorld(world))

  def regenWorld(
    world: MultiverseWorld,
    isNewSeed: Boolean,
    isRandomSeed: Boolean,
    newSeed: Option[String]
  ): Boolean =
    instance
      .getMVWorldManager
      .regenWorld(world.getName, isNewSeed, isRandomSeed, newSeed.orNull)

  implicit class BukkitWorldOps(val world: World) {
    def asMVWorld(): Option[MultiverseWorld] = fromBukkitWorld(world)
  }
}
