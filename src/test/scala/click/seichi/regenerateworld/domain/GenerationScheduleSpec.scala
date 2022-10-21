package click.seichi.regenerateworld.domain

import click.seichi.regenerateworld.domain.model.{
  DateTimeUnit,
  GenerationSchedule,
  Interval,
  SeedPattern
}
import org.scalatest.Inspectors.forAll
import org.scalatest.diagrams.Diagrams
import org.scalatest.flatspec.AnyFlatSpec

import java.time.{LocalDate, LocalTime, ZoneId, ZonedDateTime}
import java.util.UUID

class GenerationScheduleSpec extends AnyFlatSpec with Diagrams {
  // TODO: GenerationSchedule#fromRepository

  "GenerationSchedule#finish" should "calculate Interval plus now" in {
    val nowDateTime =
      ZonedDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.of(0, 0, 0), ZoneId.of("Asia/Tokyo"))
    val intervalsSet = DateTimeUnit.values.map { unit => Interval(unit, 1) }

    forAll(intervalsSet) { interval =>
      val schedule =
        GenerationSchedule(
          UUID.randomUUID(),
          nowDateTime,
          interval,
          SeedPattern.NewSeed,
          Set()
        )

      assert(
        schedule.finish(nowDateTime).nextDateTime ==
          nowDateTime.plus(interval.value, interval.unit.chronoUnit)
      )
    }
  }
}
