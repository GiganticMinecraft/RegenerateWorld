package click.seichi.regenerateworld

import org.bukkit.configuration.file.FileConfiguration
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

const val PLANS_SECTION_NAME = "regeneration"

data class Plan(
    val id: String,
    val date: ZonedDateTime,
    val interval: Int,
    val worlds: List<String>
)

object Config {
    private lateinit var config: FileConfiguration

    fun load() {
        RegenerateWorld.plugin.let {
            it.saveDefaultConfig()
            it.reloadConfig()
            config = it.config
        }
    }

    fun loadPlans(): List<Plan> {
        val plans = config.getConfigurationSection(PLANS_SECTION_NAME)
        return plans.getKeys(false).map {
            val date = ZonedDateTime.parse(
                plans.getString(PathType.DATE.shortPath(it)).replace("=", "T"),
                DateTimeFormatter.ISO_OFFSET_DATE_TIME
            )
            val interval = plans.getInt(PathType.INTERVAL.shortPath(it))
            val worlds =
                plans.getList(PathType.WORLDS.shortPath(it)).toList().filterIsInstance<String>()

            Plan(it, date, interval, worlds)
        }
    }
}

enum class PathType {
    DATE, INTERVAL, WORLDS;

    fun shortPath(id: String) = "$id.${this.name.lowercase()}"
    fun fullPath(id: String) = "${PLANS_SECTION_NAME}.${shortPath(id)}"
}