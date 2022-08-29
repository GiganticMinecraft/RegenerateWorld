package click.seichi.regenerateworld.usecase

import click.seichi.regenerateworld.domain.model.{
  DateTimeUnit,
  GenerationSchedule,
  Interval,
  SeedPattern
}
import click.seichi.regenerateworld.domain.repository.GenerationScheduleRepository
import click.seichi.regenerateworld.usecase.usetraits.Clock
import org.scalamock.scalatest.MockFactory
import org.scalatest.diagrams.Diagrams
import org.scalatest.flatspec.AnyFlatSpec

import java.time.ZonedDateTime
import java.util.UUID

class GenerationScheduleUseCaseSpec extends AnyFlatSpec with Diagrams with MockFactory {
  private val mockRepo = mock[GenerationScheduleRepository]
  private val mockClock = mock[Clock]
  private object useCase extends GenerationScheduleUseCase {
    override def clock: Clock = mockClock
    override def generationScheduleRepository: GenerationScheduleRepository = mockRepo
  }

  private def defaultSchedule =
    GenerationSchedule(
      UUID.randomUUID(),
      ZonedDateTime.now(),
      Interval(DateTimeUnit.Year, 1),
      SeedPattern.NewSeed,
      Set()
    )

  "GenerationScheduleUseCase#add" should "add new schedule" in {
    val now = defaultSchedule.nextDateTime
    val interval = defaultSchedule.interval
    val seedPattern = defaultSchedule.seedPattern
    val worlds = defaultSchedule.worlds
    (mockClock.now _).expects().returning(now).once()
    (mockRepo.save _)
      .expects(where { schedule: GenerationSchedule =>
        schedule.nextDateTime == now.plus(
          interval.value,
          interval.unit.chronoUnit
        ) && schedule.interval == interval && schedule.seedPattern == seedPattern && schedule.worlds == worlds
      })
      .once()
    useCase.add(interval, seedPattern, worlds)
  }

  "GenerationScheduleUseCase#findById" should "return Some(schedule) when given the existing uuid" in {
    val schedules = (1 to 3).map(_ => defaultSchedule)
    require(schedules.nonEmpty)
    (mockRepo.find _).expects(schedules.head.id).returning(Some(schedules.head))
    assert(useCase.findById(schedules.head.id).contains(schedules.head))
  }

  it should "return None when given the not existing uuid" in {
    (mockRepo.find _).expects(*).returning(None)
    assert(useCase.findById(UUID.randomUUID()).isEmpty)
  }

  "GenerationScheduleUseCase#addWorld" should "add the worldName to worlds list" in {
    val worlds = Set("world_1", "world_2", "world_3")
    val newWorldName = "newWorldName"

    val schedule = defaultSchedule.copy(worlds = worlds)
    (mockRepo.find _).expects(*).returning(Some(schedule)).once()
    (mockRepo.save _).expects(schedule.copy(worlds = worlds + newWorldName)).once()
    useCase.addWorld(schedule.id, newWorldName)
  }

  it should "add nothing when world names already have the same one" in {
    val worlds = Set("world_1", "world_2", "world_3")
    require(worlds.nonEmpty)
    val newWorldName = worlds.head

    val schedule = defaultSchedule.copy(worlds = worlds)
    (mockRepo.find _).expects(*).returning(Some(schedule)).once()
    (mockRepo.save _).expects(schedule).never()
    useCase.addWorld(schedule.id, newWorldName)
  }

  it should "add nothing if the schedule is not found" in {
    val id = UUID.randomUUID()
    (mockRepo.find _).expects(*).returning(None).once()
    (mockRepo.save _).expects(*).never()
    useCase.addWorld(id, "newWorldName")
  }

  "GenerationScheduleUseCase#removeWorld" should "remove the worldName from worlds list" in {
    val worlds = Set("world_1", "world_2", "world_3")
    require(worlds.nonEmpty)
    val removedWorldName = worlds.head

    val schedule = defaultSchedule.copy(worlds = worlds)
    (mockRepo.find _).expects(*).returning(Some(schedule)).once()
    (mockRepo.save _).expects(schedule.copy(worlds = worlds - removedWorldName)).once()
    useCase.removeWorld(schedule.id, removedWorldName)
  }

  it should "remove nothing when world names don't have the name" in {
    val worlds = Set("world_1", "world_2", "world_3")
    val removedWorldName = "sonzaisinaiyo"

    val schedule = defaultSchedule.copy(worlds = worlds)
    (mockRepo.find _).expects(*).returning(Some(schedule)).once()
    (mockRepo.save _).expects(schedule).never()
    useCase.removeWorld(schedule.id, removedWorldName)
  }

  it should "remove nothing if the schedule is not found" in {
    val id = UUID.randomUUID()
    (mockRepo.find _).expects(*).returning(None).once()
    (mockRepo.save _).expects(*).never()
    useCase.removeWorld(id, "removedWorldName")
  }

  "GenerationScheduleUseCase#changeInterval" should "change the interval" in {
    val changedInterval = Interval(DateTimeUnit.Minute, 10)

    val schedule = defaultSchedule
    (mockRepo.find _).expects(*).returning(Some(schedule)).once()
    (mockRepo.save _).expects(schedule.copy(interval = changedInterval)).once()
    useCase.changeInterval(schedule.id, changedInterval)
  }

  it should "change nothing when given the same interval" in {
    val schedule = defaultSchedule
    (mockRepo.find _).expects(*).returning(Some(schedule)).once()
    (mockRepo.save _).expects(schedule).never()
    useCase.changeInterval(schedule.id, schedule.interval)
  }

  it should "change nothing if the schedule is not found" in {
    val id = UUID.randomUUID()
    (mockRepo.find _).expects(*).returning(None).once()
    (mockRepo.save _).expects(*).never()
    useCase.changeInterval(id, defaultSchedule.interval)
  }
}
