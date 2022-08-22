package click.seichi.regenerateworld.domain

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import scala.util.Try

case class GenerationSchedule(id: UUID, nextDate: LocalDateTime, interval: Int, seedPattern: SeedPattern, worlds: Set[String])

object GenerationSchedule {
  private val formatter = DateTimeFormatter.ofPattern("")

  def fromRepository(id: String, nextDate: String, interval: Int, seedPattern: String, worlds: Set[String]): Option[GenerationSchedule] = {
    for {
      uuid <- Try(UUID.fromString(id)).toOption
      date <- Try(LocalDateTime.parse(nextDate, formatter)).toOption
      pattern <- SeedPattern.fromString(seedPattern)
    } yield this (uuid, date, interval, pattern, worlds)
  }

  // TODO: impl some methods to update
}
