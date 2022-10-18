package click.seichi.regenerateworld.domain.model

import java.time.ZonedDateTime
import java.util.UUID
import scala.util.Try

case class GenerationSchedule(
  id: UUID,
  nextDateTime: ZonedDateTime,
  interval: Interval,
  seedPattern: SeedPattern,
  seedValue: String,
  worlds: Set[String]
) {
  def finish(now: ZonedDateTime): GenerationSchedule = {
    val newNextDateTime = now.plus(interval.value, interval.unit.chronoUnit)

    GenerationSchedule(id, newNextDateTime, interval, seedPattern, seedValue, worlds)
  }
}

object GenerationSchedule {
  def fromRepository(
    id: UUID,
    nextDateTime: String,
    intervalUnit: String,
    intervalValue: Long,
    seedPattern: String,
    seedValue: String,
    worlds: Set[String]
  ): Option[GenerationSchedule] = {
    for {
      date <- Try(ZonedDateTime.parse(nextDateTime)).toOption
      pattern <- SeedPattern.fromString(seedPattern)
      unit <- DateTimeUnit.fromString(intervalUnit)
      interval <- Try(Interval(unit, intervalValue)).toOption
      schedule = this(id, date, interval, pattern, seedValue, worlds)
    } yield schedule
  }
}
