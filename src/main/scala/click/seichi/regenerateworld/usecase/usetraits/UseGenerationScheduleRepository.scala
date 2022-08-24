package click.seichi.regenerateworld.usecase.usetraits

import click.seichi.regenerateworld.domain.repository.GenerationScheduleRepository

trait UseGenerationScheduleRepository {
 def generationScheduleRepository: GenerationScheduleRepository
}