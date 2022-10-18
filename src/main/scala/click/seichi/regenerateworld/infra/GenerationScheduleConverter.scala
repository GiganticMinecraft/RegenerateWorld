package click.seichi.regenerateworld.infra

import click.seichi.regenerateworld.domain.model.GenerationSchedule
import org.bukkit.configuration.MemorySection

import java.util.UUID
import scala.jdk.CollectionConverters._

private object ConfigKeys {
  val NextDateTime = "next-date-time"
  val Interval = "interval"
  val IntervalUnit = s"$Interval.unit"
  val IntervalValue = s"$Interval.value"
  val SeedPattern = "seed-pattern"
  val SeedValue = "seed-value"
  val Worlds = "worlds"
}

private object GenerationScheduleConverter {
  implicit class Converter(val section: MemorySection) {
    def read(id: UUID): Option[GenerationSchedule] = {
      for {
        nextDateTime <- Option(section.getString(ConfigKeys.NextDateTime))
        intervalUnit <- Option(section.getString(ConfigKeys.IntervalUnit))
        intervalValue <- Option(section.getLong(ConfigKeys.IntervalValue))
        seedPattern <- Option(section.getString(ConfigKeys.SeedPattern))
        seedValue <- Option(section.getString(ConfigKeys.SeedValue))
        worlds = section.getStringList(ConfigKeys.Worlds).asScala.toSet.filterNot(_ == null)
        schedule <- GenerationSchedule.fromRepository(
          id,
          nextDateTime,
          intervalUnit,
          intervalValue,
          seedPattern,
          seedValue,
          worlds
        )
      } yield schedule
    }

    def write(schedule: GenerationSchedule): Unit = {
      Map(
        ConfigKeys.NextDateTime -> schedule.nextDateTime.toString,
        ConfigKeys.IntervalUnit -> schedule.interval.unit.toString,
        ConfigKeys.IntervalValue -> schedule.interval.value,
        ConfigKeys.SeedPattern -> schedule.seedPattern.toString,
        ConfigKeys.SeedValue -> schedule.seedValue,
        ConfigKeys.Worlds -> schedule.worlds.toList.asJava
      ).foreach { case (key, value) => section.set(key, value) }
    }
  }
}
