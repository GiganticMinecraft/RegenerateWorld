package click.seichi.regenerateworld.domain.model

import enumeratum.{Enum, EnumEntry}

import java.time.temporal.ChronoUnit

sealed abstract class DateTimeUnit(val suffix: String, val chronoUnit: ChronoUnit) extends EnumEntry

object DateTimeUnit extends Enum[DateTimeUnit] {
  override val values: IndexedSeq[DateTimeUnit] = findValues

  def fromString(str: String): Option[DateTimeUnit] = values.find(_.entryName == str)

  case object YEAR extends DateTimeUnit("y", ChronoUnit.YEARS)

  case object MONTH extends DateTimeUnit("mo", ChronoUnit.MONTHS)

  case object WEEK extends DateTimeUnit("w", ChronoUnit.WEEKS)

  case object DAY_OF_MONTH extends DateTimeUnit("d", ChronoUnit.DAYS)

  case object HOUR extends DateTimeUnit("h", ChronoUnit.HOURS)

  case object MINUTE extends DateTimeUnit("m", ChronoUnit.MINUTES)
}