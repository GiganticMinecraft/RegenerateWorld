package click.seichi.regenerateworld.utils

import click.seichi.regenerateworld.PLANS_SECTION_NAME

/**
 * Configの階層をたどるのに度々書いていると間違えるので、列挙クラス化。詳細の項目については、[[Plan]]を参照。
 */
enum class ConfigPaths {
    TASK_ID, DATE, INTERVAL, SEED_TYPE, SEED, WORLDS;

    /**
     * サブ階層のみへのパス文字列を返す。
     * @param id UniqueID。
     */
    fun shortPath(id: String) = "$id.${this.name.lowercase()}"

    /**
     * すべての階層を含むパス文字列を返す。ルートからの絶対パスのようなもの。
     * @param id UniqueID。
     */
    fun fullPath(id: String) = "$PLANS_SECTION_NAME.${shortPath(id)}"
}