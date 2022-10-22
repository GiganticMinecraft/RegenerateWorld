package click.seichi.regenerateworld.presenter.mixin

import click.seichi.regenerateworld.presenter.RegenerateWorld.instance
import click.seichi.regenerateworld.domain.repository.GenerationScheduleRepository
import click.seichi.regenerateworld.infra.GenerationScheduleRepositoryImpl
import click.seichi.regenerateworld.usecase.usetraits.UseGenerationScheduleRepository

trait MixInGenerationScheduleRepository extends UseGenerationScheduleRepository {
  override def generationScheduleRepository: GenerationScheduleRepository =
    GenerationScheduleRepositoryImpl(
      instance.getConfig,
      instance.saveConfig,
      instance.reloadConfig
    )
}
