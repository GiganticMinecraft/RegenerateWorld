package click.seichi.regenerateworld

import click.seichi.regenerateworld.presenter.mixin.{MixInClock, MixInGenerationScheduleRepository}
import click.seichi.regenerateworld.usecase.GenerationScheduleUseCase

package object presenter {
  object GenerationScheduleUseCase extends GenerationScheduleUseCase with MixInGenerationScheduleRepository with MixInClock
}