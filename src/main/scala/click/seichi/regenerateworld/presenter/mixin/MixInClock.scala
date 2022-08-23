package click.seichi.regenerateworld.presenter.mixin

import click.seichi.regenerateworld.usecase.usetraits.{Clock, UseClock}

import java.time.ZonedDateTime

trait MixInClock extends UseClock {
  object Clock extends Clock {
    override def now: ZonedDateTime = ZonedDateTime.now()
  }

  override def clock: Clock = Clock
}