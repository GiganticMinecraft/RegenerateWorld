package click.seichi.regenerateworld.domain

import java.time.ZonedDateTime
import java.util.UUID
import scala.util.Try

//TODO: rename interval to minutesInterval
case class GenerationSchedule(id: UUID, nextDateTime: ZonedDateTime, interval: Long, seedPattern: SeedPattern, worlds: Set[String]) {
  // region Init

  if (interval <= 0) throw new IllegalArgumentException("The value of Interval must be more than 0")

  // endregion

  def finish(now: ZonedDateTime): GenerationSchedule = {
    val maybeNextDateTime = nextDateTime.plusMinutes(interval)
    val newNextDateTime = if (maybeNextDateTime.isBefore(now)) now.plusMinutes(interval) else maybeNextDateTime

    GenerationSchedule(id, newNextDateTime, interval, seedPattern, worlds)
  }
}

object GenerationSchedule {
  def fromRepository(id: UUID, nextDateTime: String, interval: Long, seedPattern: String, worlds: Set[String]): Option[GenerationSchedule] = {
    for {
      date <- Try(ZonedDateTime.parse(nextDateTime)).toOption
      pattern <- SeedPattern.fromString(seedPattern)
    } yield this (id, date, interval, pattern, worlds)
  }
}