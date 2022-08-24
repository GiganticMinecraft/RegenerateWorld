package click.seichi.regenerateworld.domain.model.repository

import click.seichi.regenerateworld.domain.model.GenerationSchedule
import java.util.UUID

trait GenerationScheduleRepository {
  def list(): Set[GenerationSchedule]

  def find(uuid: UUID): Option[GenerationSchedule]

  def save(schedule: GenerationSchedule): Unit

  def remove(uuid: UUID): Boolean
}
