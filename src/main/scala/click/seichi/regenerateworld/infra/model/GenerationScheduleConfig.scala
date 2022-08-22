package click.seichi.regenerateworld.infra.model

import click.seichi.regenerateworld.domain.{GenerationSchedule, SeedPattern}
import org.bukkit.configuration.serialization.ConfigurationSerializable

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util
import java.util.UUID
import scala.jdk.CollectionConverters._

private object ConfigKeys {
  val NextDate = "next-date"
  val Interval = "interval"
  val SeedPattern = "seed-pattern"
  val Worlds = "worlds"
}

class GenerationScheduleConfig(
  private val nextDate: LocalDateTime,
  private val interval: Int,
  private val seedPattern: SeedPattern,
  private val worlds: Set[String]
) extends ConfigurationSerializable {
  private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

  override def serialize(): util.Map[String, Any] = {
    Map(
      ConfigKeys.NextDate -> nextDate.format(formatter),
      ConfigKeys.Interval -> interval,
      ConfigKeys.SeedPattern -> seedPattern,
      ConfigKeys.Worlds -> worlds
    ).asJava
  }

  def deserialize(map: util.Map[String, AnyRef]): GenerationScheduleConfig = {
    val nextDate = LocalDateTime.parse(map.get(ConfigKeys.NextDate).asInstanceOf[String], formatter)
    val interval = map.get(ConfigKeys.Interval).asInstanceOf[Int]
    val seedPattern = SeedPattern.fromString(map.get(ConfigKeys.SeedPattern).asInstanceOf[String]).get
    val worlds = map.get(ConfigKeys.Worlds).asInstanceOf[Set[String]]

    new GenerationScheduleConfig(nextDate, interval, seedPattern, worlds)
  }

  def toGenerationSchedule(uuid: UUID): GenerationSchedule =
    GenerationSchedule(uuid, nextDate, interval, seedPattern, worlds)
}

object GenerationScheduleConfig {
  def fromGenerationSchedule(schedule: GenerationSchedule): GenerationScheduleConfig =
    new GenerationScheduleConfig(schedule.nextDate, schedule.interval, schedule.seedPattern, schedule.worlds)
}
