package click.seichi.regenerateworld.utils

import click.seichi.regenerateworld.PLANS_SECTION_NAME

/**
 * Configの階層をたどるのに度々書いていると間違えるので、列挙クラス化。詳細の項目については、[[Plan]]を参照。
 */
enum class ConfigPaths {
    TASK_ID {
        override fun exists(value: Any?) = value is Int
    },
    DATE {
        override fun exists(value: Any?) = value is String
    },
    INTERVAL {
        override fun exists(value: Any?) = value is Int
    },
    SEED_TYPE {
        override fun exists(value: Any?) =
            value is String && SeedPatterns.safeValueOf(value) != null
    },
    SEED {
        override fun exists(value: Any?) = value is String?
    },
    WORLDS {
        override fun exists(value: Any?) = value is List<*> && value.all { it is String }
    };

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

    abstract fun exists(value: Any?): Boolean
}