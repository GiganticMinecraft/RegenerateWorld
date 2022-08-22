package click.seichi.regenerateworld.infra

import click.seichi.regenerateworld.domain.{GenerationSchedule, GenerationScheduleRepository}
import org.bukkit.configuration.file.FileConfiguration
import GenerationScheduleConverter._
import org.bukkit.configuration.MemorySection

import java.util.UUID
import scala.jdk.CollectionConverters._
import scala.util.Try

class GenerationScheduleRepositoryImpl(getConfig: () => FileConfiguration, saveConfig: () => Unit) extends GenerationScheduleRepository {
  private def config = getConfig().getConfigurationSection("schedules")

  override def list(): Set[GenerationSchedule] = {
    val set = for {
      id <- config.getKeys(false).asScala
      uuid <- Try(UUID.fromString(id)).toOption
      section = config.get(id).asInstanceOf[MemorySection]
      schedule <- section.read(uuid)
    } yield schedule

    set.toSet
  }

  override def find(uuid: UUID): Option[GenerationSchedule] = {
    for {
      id <- config.getKeys(false).asScala.find(_ == uuid.toString)
      uuid <- Try(UUID.fromString(id)).toOption
      section = config.get(id).asInstanceOf[MemorySection]
      schedule <- section.read(uuid)
    } yield schedule
  }

  override def upsert(schedule: GenerationSchedule): Unit = {
    config.get(schedule.id.toString).asInstanceOf[MemorySection].write(schedule)
    saveConfig()
  }

  override def remove(uuid: UUID): Boolean = {
    config.set(uuid.toString, null)
    saveConfig()

    !config.isSet(uuid.toString)
  }
}