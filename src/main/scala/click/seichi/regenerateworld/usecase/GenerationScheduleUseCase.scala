package click.seichi.regenerateworld.usecase

import click.seichi.regenerateworld.domain.{GenerationSchedule, Interval, SeedPattern}
import click.seichi.regenerateworld.usecase.usetraits.{UseClock, UseGenerationScheduleRepository}

import java.util.UUID

trait GenerationScheduleUseCase extends UseGenerationScheduleRepository with UseClock {
  def list(): Set[GenerationSchedule] = generationScheduleRepository.list()

  def filteredList(predicate: GenerationSchedule => Boolean): Set[GenerationSchedule] = list().filter(predicate)

  def findById(id: UUID): Option[GenerationSchedule] = generationScheduleRepository.find(id)

  def remove(id: UUID): Boolean = generationScheduleRepository.remove(id)

  def addWorld(id: UUID, worldName: String): Unit = {
    for {
      schedule <- findById(id)
      worlds = schedule.worlds + worldName
      newSchedule = GenerationSchedule(schedule.id, schedule.nextDateTime, schedule.interval, schedule.seedPattern, worlds)
    } yield generationScheduleRepository.save(newSchedule)
  }

  def removeWorld(id: UUID, worldName: String): Unit = {
    for {
      schedule <- findById(id)
      worlds = schedule.worlds.filterNot(_ == worldName)
      newSchedule = GenerationSchedule(schedule.id, schedule.nextDateTime, schedule.interval, schedule.seedPattern, worlds)
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
      newSchedule = GenerationSchedule(schedule.id, schedule.nextDateTime, interval, schedule.seedPattern, schedule.worlds)
    } yield generationScheduleRepository.save(newSchedule)
  }

  def changeSeedPattern(id: UUID, pattern: SeedPattern): Unit = {
    for {
      schedule <- findById(id)
      newSchedule = GenerationSchedule(schedule.id, schedule.nextDateTime, schedule.interval, pattern, schedule.worlds)
    } yield generationScheduleRepository.save(newSchedule)
  }
}