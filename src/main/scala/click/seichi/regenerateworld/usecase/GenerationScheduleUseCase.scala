package click.seichi.regenerateworld.usecase

import click.seichi.regenerateworld.domain.model.{GenerationSchedule, Interval, SeedPattern}
import click.seichi.regenerateworld.usecase.usetraits.{
  UseClock,
  UseGenerationScheduleRepository
}

import java.util.UUID

trait GenerationScheduleUseCase extends UseGenerationScheduleRepository with UseClock {
  def list(): Set[GenerationSchedule] = generationScheduleRepository.list()

  def filteredList(predicate: GenerationSchedule => Boolean): Set[GenerationSchedule] =
    list().filter(predicate)

  def findById(id: UUID): Option[GenerationSchedule] = generationScheduleRepository.find(id)

  def remove(id: UUID): Boolean = generationScheduleRepository.remove(id)

  def addWorld(id: UUID, worldName: String): Unit = {
    for {
      schedule <- findById(id)
      worlds = schedule.worlds + worldName if !schedule.worlds.contains(worldName)
      newSchedule = schedule.copy(worlds = worlds)
    } yield generationScheduleRepository.save(newSchedule)
  }

  def removeWorld(id: UUID, worldName: String): Unit = {
    for {
      schedule <- findById(id)
      worlds = schedule.worlds.filterNot(_ == worldName) if schedule.worlds.contains(worldName)
      newSchedule = schedule.copy(worlds = worlds)
    } yield generationScheduleRepository.save(newSchedule)
  }

  def finish(id: UUID): Unit = {
    for {
      schedule <- findById(id)
      newSchedule = schedule.finish(clock.now)
    } yield generationScheduleRepository.save(newSchedule)
  }

  def changeInterval(id: UUID, interval: Interval): Unit = {
    for {
      schedule <- findById(id)
      newSchedule = schedule.copy(interval = interval) if schedule.interval != interval
    } yield generationScheduleRepository.save(newSchedule)
  }

  def changeSeedPattern(id: UUID, pattern: SeedPattern): Unit = {
    for {
      schedule <- findById(id)
      newSchedule = schedule.copy(seedPattern = pattern) if schedule.seedPattern != pattern
    } yield generationScheduleRepository.save(newSchedule)
  }
}
