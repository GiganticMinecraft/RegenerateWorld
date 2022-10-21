package click.seichi.regenerateworld

import click.seichi.regenerateworld.presenter.mixin.{
  MixInClock,
  MixInGenerationScheduleRepository,
  MixInSetting
}
import click.seichi.regenerateworld.usecase.{GenerationScheduleUseCase, SettingUseCase}

package object presenter {
  object GenerationScheduleUseCase
      extends GenerationScheduleUseCase
      with MixInGenerationScheduleRepository
      with MixInClock

  object Setting extends SettingUseCase with MixInSetting
}
