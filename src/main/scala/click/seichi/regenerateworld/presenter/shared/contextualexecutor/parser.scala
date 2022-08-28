package click.seichi.regenerateworld.presenter.shared.contextualexecutor

import click.seichi.regenerateworld.domain.model.SeedPattern
import click.seichi.regenerateworld.presenter.shared.exception.{
  CommandException,
  WorldRegenerationException
}
import org.bukkit.Bukkit

import java.util.UUID
import scala.util.Try
import scala.jdk.CollectionConverters._

object parser {
  def uuid: SingleArgumentParser = arg =>
    Try(UUID.fromString(arg)).toOption.toRight(CommandException.ArgIsNotUuid)

  def bukkitWorld: SingleArgumentParser = arg =>
    Bukkit
      .getWorlds
      .asScala
      .find(_.getName.toLowerCase == arg.toLowerCase)
      .toRight(WorldRegenerationException.WorldIsNotFound(arg))

  def seedPattern: SingleArgumentParser = arg =>
    SeedPattern.fromString(arg).toRight(WorldRegenerationException.SeedPatternIsNotFound(arg))
}
