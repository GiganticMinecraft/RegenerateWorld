package click.seichi.regenerateworld.domain.model

import java.time.ZonedDateTime
import java.util.UUID
import scala.util.Try

case class GenerationSchedule(
  id: UUID,
  nextDateTime: ZonedDateTime,
  interval: Interval,
  seedPattern: SeedPattern,
  worlds: Set[String]
) {
  def finish(now: ZonedDateTime): GenerationSchedule = {
    val maybeNextDateTime = nextDateTime.plus(interval.value, interval.unit.chronoUnit)
    val newNextDateTime =
      if (maybeNextDateTime.isBefore(now)) now.plus(interval.value, interval.unit.chronoUnit)
      else maybeNextDateTime

    GenerationSchedule(id, newNextDateTime, interval, seedPattern, worlds)
  }
}

object GenerationSchedule {
  def fromRepository(
    id: UUID,
    nextDateTime: String,
    intervalUnit: String,
    intervalValue: Long,
    seedPattern: String,
    worlds: Set[String]
  ): Option[GenerationSchedule] = {
    for {
      date <- Try(ZonedDateTime.parse(nextDateTime)).toOption
      pattern <- SeedPattern.fromString(seedPattern)
      unit <- DateTimeUnit.fromString(intervalUnit)
      interval = Interval(unit, intervalValue)
    } yield this(id, date, interval, pattern, worlds)
  }
}
