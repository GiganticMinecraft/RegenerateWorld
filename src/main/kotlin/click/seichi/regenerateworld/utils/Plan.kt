package click.seichi.regenerateworld.utils

import click.seichi.regenerateworld.SeedType
import java.time.ZonedDateTime

data class Plan(
    val id: String,
    val taskId: Int,
    val date: ZonedDateTime,
    val interval: Long,
    val seedType: SeedType,
    val seed: String?,
    val worlds: List<String>
)
