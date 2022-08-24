package click.seichi.regenerateworld.presenter.listener

import click.seichi.regenerateworld.presenter.Setting
import click.seichi.regenerateworld.presenter.event.{PreRegenWorldEvent, RegenWorldEvent}
import org.bukkit.Bukkit
import org.bukkit.event.{EventHandler, Listener}

case object RegenWorldListener extends Listener {
  private def replaceWithWorldName(s: String, worldName: String) = s.replaceAll("%world%", worldName)

  @EventHandler
  def onPreRegenWorld(event: PreRegenWorldEvent): Unit = {
    for {
      setting <- Setting.settingRepository.get()
      _ = setting.beforeCommand.map(replaceWithWorldName(_, event.worldName)).foreach(Bukkit.dispatchCommand(Bukkit.getConsoleSender, _))
      world <- Option(Bukkit.getWorld(event.worldName))
      worldName = setting.teleportWorldName
      teleportWorld <- Option(Bukkit.getWorld(worldName))
      _ = world.getPlayers.forEach { player => player.teleport(teleportWorld.getSpawnLocation) }
    } yield ()
  }

  @EventHandler
  def onRegenWorld(event: RegenWorldEvent): Unit = {
    for {
      setting <- Setting.settingRepository.get()
      _ = setting.afterCommand.map(replaceWithWorldName(_, event.worldName)).foreach(Bukkit.dispatchCommand(Bukkit.getConsoleSender, _))
    } yield ()
  }
}