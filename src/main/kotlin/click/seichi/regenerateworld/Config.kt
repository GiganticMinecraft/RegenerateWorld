package click.seichi.regenerateworld

import click.seichi.regenerateworld.utils.ConfigPaths
import click.seichi.regenerateworld.utils.Plan
import click.seichi.regenerateworld.utils.SeedType
import org.bukkit.configuration.file.FileConfiguration
import java.time.ZonedDateTime

const val PLANS_SECTION_NAME = "regeneration"

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
        return plans.getKeys(false).map { id ->
            // TODO 各項目の存在確認
            val taskId = plans.getInt(ConfigPaths.BUKKIT_TASK_ID.shortPath(id))
            val date = ZonedDateTime.parse(
                plans.getString(ConfigPaths.DATE.shortPath(id)).replace("=", "T")
            )
            val interval = plans.getLong(ConfigPaths.INTERVAL.shortPath(id))
            val seedType = SeedType.valueOf(plans.getString(ConfigPaths.SEED_TYPE.shortPath(id)))
            val seed =
                if (seedType.isSeedNecessary()) plans.getString(ConfigPaths.SEED.shortPath(id)) else null
            val worlds =
                plans.getList(ConfigPaths.WORLDS.shortPath(id)).toList().filterIsInstance<String>()

            Plan(id, taskId, date, interval, seedType, seed, worlds)
        }
    }

    // TODO valueの型を限定的にする
    @Deprecated("This method can make a destructive change.")
    fun setData(pathType: ConfigPaths, id: String, value: Any) {
        config.set(pathType.fullPath(id), value)
        RegenerateWorld.plugin.saveConfig()
    }
}
