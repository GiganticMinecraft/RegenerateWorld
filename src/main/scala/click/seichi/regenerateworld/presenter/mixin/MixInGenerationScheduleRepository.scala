package click.seichi.regenerateworld.presenter.mixin

import click.seichi.regenerateworld.RegenerateWorld.INSTANCE
import click.seichi.regenerateworld.domain.GenerationScheduleRepository
import click.seichi.regenerateworld.infra.GenerationScheduleRepositoryImpl
import click.seichi.regenerateworld.usecase.usetraits.{Clock, UseClock, UseGenerationScheduleRepository}

import java.time.ZonedDateTime

trait MixInGenerationScheduleRepository extends UseGenerationScheduleRepository {
  override def generationScheduleRepository: GenerationScheduleRepository = new GenerationScheduleRepositoryImpl(
    INSTANCE.getConfig, INSTANCE.saveConfig, INSTANCE.reloadConfig
  )
}