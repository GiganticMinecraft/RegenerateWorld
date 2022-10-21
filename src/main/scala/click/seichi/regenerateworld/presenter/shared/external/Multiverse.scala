package click.seichi.regenerateworld.presenter.shared.external

import click.seichi.regenerateworld.presenter.RegenerateWorld.INSTANCE
import com.onarandombox.MultiverseCore.MultiverseCore
import com.onarandombox.MultiverseCore.api.MultiverseWorld
import org.bukkit.{Bukkit, Location, World}
import org.bukkit.entity.Player

object Multiverse {
  private val instance =
    INSTANCE
      .getServer
      .getPluginManager
      .getPlugin("Multiverse-Core")
      .asInstanceOf[MultiverseCore]

  def fromBukkitWorld(world: World): Option[MultiverseWorld] =
    Option(instance.getMVWorldManager.getMVWorld(world))

  def teleportPlayer(player: Player, location: Location): Unit =
    instance.teleportPlayer(Bukkit.getConsoleSender, player, location)

  private[shared] def regenWorld(
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
