package click.seichi.regenerateworld.infra

import click.seichi.regenerateworld.domain.model.Setting
import click.seichi.regenerateworld.domain.repository.SettingRepository
import org.bukkit.configuration.file.FileConfiguration

import scala.jdk.CollectionConverters._

case class SettingRepositoryImpl(getConfig: () => FileConfiguration) extends SettingRepository {
  private def config = getConfig().getConfigurationSection("settings")

  override def get(): Option[Setting] = {
    for {
      beforeCommand <- Option(config.getStringList("command.before").asScala.toSet)
      afterCommand <- Option(config.getStringList("command.after").asScala.toSet)
      teleportWorldName <- Option(config.getString("teleport-world-name"))
    } yield Setting(beforeCommand, afterCommand, teleportWorldName)
  }
}