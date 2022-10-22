package click.seichi.regenerateworld.domain.model

case class Interval(value: Long, unit: DateTimeUnit) {
  // region Init

  if (value <= 0)
    throw new IllegalArgumentException("The value of Interval must be more than 0")

  // endregion
}
