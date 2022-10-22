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
      Interval(1, DateTimeUnit.Year),
      SeedPattern.CurrentSeed,
      Set()
    )

  "GenerationScheduleUseCase#add" should "add new schedule" in {
    val now = defaultSchedule.nextDateTime
    val interval = defaultSchedule.interval
    (() => mockClock.now).expects().returning(now).once()
    (mockRepo.save _)
      .expects(where { schedule: GenerationSchedule =>
        schedule.nextDateTime == now.plus(interval.value, interval.unit.chronoUnit)
      })
      .once()
    useCase.add(interval, defaultSchedule.seedPattern, defaultSchedule.worlds)
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

  "GenerationScheduleUseCase#setWorlds" should "add the worldName to worlds list" in {
    val newWorlds = Set("new_world")

    val schedule = defaultSchedule.copy(worlds = Set("world_1", "world_2", "world_3"))
    (mockRepo.find _).expects(*).returning(Some(schedule)).once()
    (mockRepo.save _).expects(schedule.copy(worlds = newWorlds)).once()
    useCase.setWorlds(schedule.id, newWorlds)
  }

  "GenerationScheduleUseCase#changeInterval" should "change the interval" in {
    val changedInterval = Interval(10, DateTimeUnit.Minute)

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
