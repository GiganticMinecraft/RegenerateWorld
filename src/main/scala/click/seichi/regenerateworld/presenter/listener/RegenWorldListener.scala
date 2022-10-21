package click.seichi.regenerateworld.presenter.listener

import click.seichi.regenerateworld.presenter.Setting
import click.seichi.regenerateworld.presenter.event.{PreRegenWorldEvent, RegenWorldEvent}
import click.seichi.regenerateworld.presenter.shared.external.Multiverse
import org.bukkit.Bukkit
import org.bukkit.event.{EventHandler, Listener}

import java.util.Collections.emptyList

case object RegenWorldListener extends Listener {
  private def replaceWithWorldName(s: String, worldName: String) =
    s.replaceAll("%world%", worldName)

  @EventHandler
  def onPreRegenWorld(event: PreRegenWorldEvent): Unit = {
    val setting = Setting.get()

    setting
      .map(_.beforeCommand)
      .getOrElse(Set())
      .map(replaceWithWorldName(_, event.worldName))
      .foreach(Bukkit.dispatchCommand(Bukkit.getConsoleSender, _))

    val players =
      Option(Bukkit.getWorld(event.worldName)).map(_.getPlayers).getOrElse(emptyList)
    setting
      .map(_.teleportWorldName)
      .flatMap(name => Option(Bukkit.getWorld(name)))
      .filter(_.getName.toLowerCase != event.worldName.toLowerCase) match {
      case Some(world) =>
        players.forEach(Multiverse.teleportPlayer(_, world.getSpawnLocation))
      case _ =>
        players.forEach(
          _.kickPlayer(s"Regenerating the world (${event.worldName}) you were in")
        )
    }
  }

  @EventHandler
  def onRegenWorld(event: RegenWorldEvent): Unit = {
    Setting
      .get()
      .map(_.afterCommand)
      .getOrElse(Set())
      .map(replaceWithWorldName(_, event.worldName))
      .foreach(Bukkit.dispatchCommand(Bukkit.getConsoleSender, _))
  }
}
