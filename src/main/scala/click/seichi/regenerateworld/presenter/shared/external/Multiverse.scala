package click.seichi.regenerateworld.presenter.shared.external

import click.seichi.regenerateworld.presenter.RegenerateWorld.instance
import com.onarandombox.MultiverseCore.MultiverseCore
import com.onarandombox.MultiverseCore.api.MultiverseWorld
import org.bukkit.{Bukkit, Location, World}
import org.bukkit.entity.Player

object Multiverse {
  private val multiverse =
    instance
      .getServer
      .getPluginManager
      .getPlugin("Multiverse-Core")
      .asInstanceOf[MultiverseCore]

  def fromBukkitWorld(world: World): Option[MultiverseWorld] =
    Option(multiverse.getMVWorldManager.getMVWorld(world))

  def teleportPlayer(player: Player, location: Location): Unit =
    multiverse.teleportPlayer(Bukkit.getConsoleSender, player, location)

  private[shared] def regenWorld(
    world: MultiverseWorld,
    isNewSeed: Boolean,
    isRandomSeed: Boolean,
    newSeed: Option[String]
  ): Boolean =
    multiverse
      .getMVWorldManager
      .regenWorld(world.getName, isNewSeed, isRandomSeed, newSeed.orNull)

  implicit class BukkitWorldOps(val world: World) {
    def asMVWorld(): Option[MultiverseWorld] = fromBukkitWorld(world)
  }
}
