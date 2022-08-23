package click.seichi.regenerateworld.infra

import click.seichi.regenerateworld.domain.GenerationSchedule
import org.bukkit.configuration.MemorySection

import java.util.UUID
import scala.jdk.CollectionConverters._

private object ConfigKeys {
  val NextDateTime = "next-date-time"
  val Interval = "interval"
  val SeedPattern = "seed-pattern"
  val Worlds = "worlds"
}

private object GenerationScheduleConverter {
  implicit class Converter(val section: MemorySection) {
    def read(id: UUID): Option[GenerationSchedule] = {
      for {
        nextDateTime <- Option(section.getString(ConfigKeys.NextDateTime))
        interval <- Option(section.getLong(ConfigKeys.Interval))
        seedPattern <- Option(section.getString(ConfigKeys.SeedPattern))
        worlds = section.getStringList(ConfigKeys.Worlds).asScala.toSet.filterNot(_ == null)
        schedule <- GenerationSchedule.fromRepository(id, nextDateTime, interval, seedPattern, worlds)
      } yield schedule
    }

    def write(schedule: GenerationSchedule): Unit = {
      Map(
        ConfigKeys.NextDateTime -> schedule.nextDateTime.toString,
        ConfigKeys.Interval -> schedule.interval,
        ConfigKeys.SeedPattern -> schedule.seedPattern.toString,
        ConfigKeys.Worlds -> schedule.worlds.toList.asJava
      ).foreach { case (key, value) => section.set(key, value)  }
    }
  }
}