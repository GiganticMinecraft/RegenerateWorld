package click.seichi.regenerateworld.presenter.injector

import click.seichi.regenerateworld.RegenerateWorld.INSTANCE
import click.seichi.regenerateworld.domain.{GenerationScheduleRepository, UseGenerateScheduleRepository}
import click.seichi.regenerateworld.infra.GenerationScheduleRepositoryImpl
import click.seichi.regenerateworld.usecase.GenerationScheduleUseCase

trait MixInGenerateScheduleRepository extends UseGenerateScheduleRepository {
  val generateScheduleRepository: GenerationScheduleRepository = new GenerationScheduleRepositoryImpl(
    INSTANCE.getConfig, INSTANCE.saveConfig, INSTANCE.reloadConfig
  )
}

object GenerationScheduleUseCase extends GenerationScheduleUseCase with MixInGenerateScheduleRepository