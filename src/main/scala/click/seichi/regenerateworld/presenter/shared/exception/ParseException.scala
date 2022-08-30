package click.seichi.regenerateworld.presenter.shared.exception

import enumeratum.{Enum, EnumEntry}

sealed abstract class ParseException(override val description: String)
    extends EnumEntry
    with OriginalException

object ParseException extends Enum[ParseException] {
  override val values: IndexedSeq[ParseException] = findValues

  case object IsNotUuid extends ParseException("It is not UUID")

  case object MustBeLong extends ParseException("It must be Long")

  case object MustBeNaturalNumber extends ParseException("It must be natural number")

  case object MustBeDateTimeUnit extends ParseException("It must be DateTimeUnit")

  case object MustBeInterval extends ParseException("It must be Interval")

  case class MustBeIncludedIn[*](keys: Iterable[*])
      extends ParseException(s"It must be included in ${keys.mkString(", ")}")
}
