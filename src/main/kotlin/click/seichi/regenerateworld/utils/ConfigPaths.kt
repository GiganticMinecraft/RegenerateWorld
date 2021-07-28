package click.seichi.regenerateworld.utils

import click.seichi.regenerateworld.PLANS_SECTION_NAME

enum class ConfigPaths {
    TASK_ID, DATE, INTERVAL, SEED_TYPE, SEED, WORLDS;

    fun shortPath(id: String) = "$id.${this.name.lowercase()}"
    fun fullPath(id: String) = "$PLANS_SECTION_NAME.${shortPath(id)}"
}