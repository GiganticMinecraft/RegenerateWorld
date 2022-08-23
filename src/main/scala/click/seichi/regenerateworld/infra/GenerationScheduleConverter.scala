package click.seichi.regenerateworld.infra

import click.seichi.regenerateworld.domain.GenerationSchedule
import org.bukkit.configuration.MemorySection

import java.util.UUID
import scala.jdk.CollectionConverters._

private object ConfigKeys {
  val NextDate = "next-date"
  val Interval = "interval"
  val SeedPattern = "seed-pattern"
  val Worlds = "worlds"
}

private object GenerationScheduleConverter {
  implicit class Converter(val section: MemorySection) {
    def read(id: UUID): Option[GenerationSchedule] = {
      for {
        nextDate <- Option(section.getString(ConfigKeys.NextDate))
        interval <- Option(section.getLong(ConfigKeys.Interval))
        seedPattern <- Option(section.getString(ConfigKeys.SeedPattern))
        worlds = section.getStringList(ConfigKeys.Worlds).asScala.toSet.filterNot(_ == null)
        schedule <- GenerationSchedule.fromRepository(id, nextDate, interval, seedPattern, worlds)
      } yield schedule
    }

    def write(schedule: GenerationSchedule): Unit = {
      Map(
        ConfigKeys.NextDate -> schedule.nextDate.toString,
        ConfigKeys.Interval -> schedule.interval,
        ConfigKeys.SeedPattern -> schedule.seedPattern.toString,
        ConfigKeys.Worlds -> schedule.worlds.toList.asJava
      ).foreach { case (key, value) => section.set(key, value)  }
    }
  }
}