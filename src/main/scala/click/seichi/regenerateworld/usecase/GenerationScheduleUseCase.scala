package click.seichi.regenerateworld.usecase

import click.seichi.regenerateworld.domain.{GenerationSchedule, UseGenerateScheduleRepository}

trait GenerationScheduleUseCase extends UseGenerateScheduleRepository {
  def list(): Set[GenerationSchedule] = generateScheduleRepository.list()

  def filteredList(predicate: GenerationSchedule => Boolean): Set[GenerationSchedule] = list().filter(predicate)
}
