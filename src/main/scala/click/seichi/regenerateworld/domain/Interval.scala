package click.seichi.regenerateworld.domain

case class Interval(unit: DateTimeUnit, value: Long) {
  // region Init

  if (value <= 0) throw new IllegalArgumentException("The value of Interval must be more than 0")

  // endregion
}