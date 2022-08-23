package click.seichi.regenerateworld.usecase.usetraits

import click.seichi.regenerateworld.domain.GenerationScheduleRepository

trait UseGenerationScheduleRepository {
 def generationScheduleRepository: GenerationScheduleRepository
}