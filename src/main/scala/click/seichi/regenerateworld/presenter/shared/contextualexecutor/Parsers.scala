package click.seichi.regenerateworld.presenter.shared.contextualexecutor

import click.seichi.regenerateworld.domain.model.{DateTimeUnit, Interval, SeedPattern}
import click.seichi.regenerateworld.presenter.shared.exception.{
  ParseException,
  WorldRegenerationException
}
import org.bukkit.Bukkit

import java.util.UUID
import scala.util.Try
import scala.jdk.CollectionConverters._

// TODO: add tests except for #bukkitWorld
object Parsers {
  def uuid: SingleArgumentParser = arg =>
    Try(UUID.fromString(arg)).toOption.toRight(ParseException.IsNotUuid)

  def bukkitWorld: SingleArgumentParser = arg =>
    Bukkit
      .getWorlds
      .asScala
      .find(_.getName.toLowerCase == arg.toLowerCase)
      .toRight(WorldRegenerationException.WorldIsNotFound(arg))

  def seedPattern: SingleArgumentParser = arg =>
    SeedPattern.fromString(arg).toRight(WorldRegenerationException.SeedPatternIsNotFound(arg))

  def long: SingleArgumentParser = arg => arg.toLongOption.toRight(ParseException.MustBeLong)

  def naturalLongValue: SingleArgumentParser = arg =>
    long(arg).flatMap(parsed =>
      if (parsed.asInstanceOf[Long] > 0) Right(parsed)
      else Left(ParseException.MustBeNaturalNumber)
    )

  def dateTimeUnit: SingleArgumentParser = arg =>
    DateTimeUnit
      .fromString(arg)
      .orElse(DateTimeUnit.fromAliasString(arg))
      .toRight(ParseException.MustBeDateTimeUnit)

  def interval: SingleArgumentParser = arg => {
    val regex = """(\d*)([a-zA-z]{1,10})""".r

    val interval = for {
      matchResult <- regex.findFirstMatchIn(arg)
      value <- naturalLongValue(matchResult.group(1)).toOption
      unit <- dateTimeUnit(matchResult.group(2)).toOption
    } yield Interval(unit.asInstanceOf[DateTimeUnit], value.asInstanceOf[Long])

    interval.toRight(ParseException.MustBeInterval)
  }
}
