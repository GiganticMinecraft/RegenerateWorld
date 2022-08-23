package click.seichi.regenerateworld.domain

import java.time.ZonedDateTime
import java.util.UUID
import scala.util.Try

// TODO: rename interval to minutesInterval
// TODO: rename nextDate to nextDateTime
case class GenerationSchedule(id: UUID, nextDate: ZonedDateTime, interval: Int, seedPattern: SeedPattern, worlds: Set[String]) {
  // region Init

  if (interval <= 0) throw new IllegalArgumentException("The interval value must be more than 0")

  // endregion

  def finish(now: ZonedDateTime): GenerationSchedule = {
    val maybeNextDate = nextDate.plusMinutes(interval)
    val newNextDate = if (maybeNextDate.isBefore(now)) now.plusMinutes(interval) else maybeNextDate

    GenerationSchedule(id, newNextDate, interval, seedPattern, worlds)
  }
}

object GenerationSchedule {
  def fromRepository(id: UUID, nextDate: String, interval: Long, seedPattern: String, worlds: Set[String]): Option[GenerationSchedule] = {
    for {
      date <- Try(ZonedDateTime.parse(nextDate)).toOption
      pattern <- SeedPattern.fromString(seedPattern)
    } yield this (id, date, interval, pattern, worlds)
  }
}