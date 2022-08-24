package click.seichi.regenerateworld.presenter.mixin

import click.seichi.regenerateworld.RegenerateWorld.INSTANCE
import click.seichi.regenerateworld.domain.repository.GenerationScheduleRepository
import click.seichi.regenerateworld.infra.GenerationScheduleRepositoryImpl
import click.seichi.regenerateworld.usecase.usetraits.UseGenerationScheduleRepository

trait MixInGenerationScheduleRepository extends UseGenerationScheduleRepository {
  override def generationScheduleRepository: GenerationScheduleRepository = GenerationScheduleRepositoryImpl(
    INSTANCE.getConfig, INSTANCE.saveConfig, INSTANCE.reloadConfig
  )
}