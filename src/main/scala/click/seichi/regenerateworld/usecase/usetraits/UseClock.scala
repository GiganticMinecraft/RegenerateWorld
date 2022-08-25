package click.seichi.regenerateworld.usecase.usetraits

import java.time.ZonedDateTime

trait Clock {
  def now: ZonedDateTime
}

trait UseClock {
  def clock: Clock
}
