package click.seichi.regenerateworld.utils

import click.seichi.regenerateworld.PLANS_SECTION_NAME

/**
 * Configの階層をたどるのに度々書いていると間違えるので、列挙クラス化。詳細の項目については、[[Plan]]を参照。
 */
enum class ConfigPaths {
    BUKKIT_TASK_ID, DATE, INTERVAL, SEED_TYPE, SEED, WORLDS;

    fun shortPath(id: String) = "$id.${this.name.lowercase()}"
    fun fullPath(id: String) = "$PLANS_SECTION_NAME.${shortPath(id)}"
}