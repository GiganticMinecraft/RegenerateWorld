package click.seichi.regenerateworld.domain

import java.time.ZonedDateTime
import java.util.UUID
import scala.util.Try

case class GenerationSchedule(id: UUID, nextDate: ZonedDateTime, interval: Int, seedPattern: SeedPattern, worlds: Set[String])

object GenerationSchedule {
  def fromRepository(id: UUID, nextDate: String, interval: Int, seedPattern: String, worlds: Set[String]): Option[GenerationSchedule] = {
    for {
      date <- Try(ZonedDateTime.parse(nextDate)).toOption
      pattern <- SeedPattern.fromString(seedPattern)
    } yield this (id, date, interval, pattern, worlds)
  }

  // TODO: impl some methods to update
}
