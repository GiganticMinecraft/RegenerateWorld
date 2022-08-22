package click.seichi.regenerateworld.domain

import java.util.UUID

trait GenerationScheduleRepository {
  def list(): Set[GenerationSchedule]

  def find(uuid: UUID): Option[GenerationSchedule]

  def upsert(schedule: GenerationSchedule): Unit

  def remove(uuid: UUID): Boolean
}

trait UseGenerateScheduleRepository {
  val generateScheduleRepository: GenerationScheduleRepository
}
