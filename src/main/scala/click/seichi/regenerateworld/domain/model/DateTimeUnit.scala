package click.seichi.regenerateworld.domain.model

import enumeratum.{Enum, EnumEntry}

import java.time.temporal.ChronoUnit

sealed abstract class DateTimeUnit(val suffix: String, val chronoUnit: ChronoUnit) extends EnumEntry

object DateTimeUnit extends Enum[DateTimeUnit] {
  override val values: IndexedSeq[DateTimeUnit] = findValues

  def fromString(str: String): Option[DateTimeUnit] = values.find(_.entryName == str)

  case object Year extends DateTimeUnit("y", ChronoUnit.YEARS)

  case object Month extends DateTimeUnit("mo", ChronoUnit.MONTHS)

  case object Week extends DateTimeUnit("w", ChronoUnit.WEEKS)

  case object DayOfMonth extends DateTimeUnit("d", ChronoUnit.DAYS)

  case object Hour extends DateTimeUnit("h", ChronoUnit.HOURS)

  case object Minute extends DateTimeUnit("m", ChronoUnit.MINUTES)
}