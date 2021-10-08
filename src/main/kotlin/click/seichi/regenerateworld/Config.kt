package click.seichi.regenerateworld

import click.seichi.regenerateworld.utils.*
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import java.time.ZonedDateTime
import java.util.*

const val PLANS_SECTION_NAME = "regeneration"

object Config {
    private val config: FileConfiguration by lazy {
        RegenerateWorld.plugin.let {
            it.saveDefaultConfig()
            it.reloadConfig()
            it.config
        }
    }
    private val plansSection: ConfigurationSection? by lazy {
        config.getConfigurationSection(PLANS_SECTION_NAME)
    }

    /**
     * 各項目に対して、ConfigPaths.existsを実行して、すべてtrueではない（存在しないか型が合わない）PlanのListを返す
     */
    private fun filterBadPlanConfigs(plans: ConfigurationSection, ids: List<UUID>) = ids.map { id ->
        id to ConfigPaths.values()
            .map { path -> path to plans.get(path.shortPath(id)) }
            .map { (path, value) -> path to path.isProperType(value) }
            .filterNot { it.second }
    }.filter { it.second.isNotEmpty() }.map { it.first to it.second.map { (paths, _) -> paths } }

    fun loadPlans() = plansSection?.let { plans ->
        val validUuids =
            plans.getKeys(false).mapNotNull { runCatching { UUID.fromString(it) }.getOrNull() }
        val invalidPlanConfigs = filterBadPlanConfigs(plans, validUuids)
        val validPlanIds = validUuids.filterNot {
            invalidPlanConfigs.map { (invalidConfigId, _) -> invalidConfigId }.contains(it)
        }

        if (invalidPlanConfigs.isEmpty() || validPlanIds.isEmpty())
            Logger.infoWithPrefix("すべての再生成スケジュールを読み込みました。")
        else invalidPlanConfigs.forEach { (id, pathsList) ->
            ConfigError.PATH_IS_NOT_FOUND.withServerLog("id: $id, paths: $pathsList")
        }

        validPlanIds.map { id ->
            val taskId = plans.getInt(ConfigPaths.TASK_ID.shortPath(id))
            val date = ZonedDateTime.parse(plans.getString(ConfigPaths.DATE.shortPath(id)))
            val interval = plans.getLong(ConfigPaths.INTERVAL.shortPath(id))
            val seedPattern =
                SeedPatterns.valueOf(plans.getString(ConfigPaths.SEED_TYPE.shortPath(id)))
            val seed =
                if (seedPattern.isSeedNecessary()) plans.getString(ConfigPaths.SEED.shortPath(id)) else null
            // Worldsは、実際に再生成を行う時にならないとMultiverseのロード状況が不明のため、Stringのまま
            val worlds =
                plans.getList(ConfigPaths.WORLDS.shortPath(id)).toList()
                    .filterIsInstance<String>()

            Plan(id, taskId, date, interval, seedPattern, seed, worlds)
        }
    } ?: run {
        ConfigError.PATH_IS_NOT_FOUND.withServerLog("id: $PLANS_SECTION_NAME")
        listOf()
    }

    fun setData(configPath: ConfigPaths, id: UUID, value: Any) =
        if (configPath.isProperType(value)) {
            config.set(configPath.fullPath(id), value)
            RegenerateWorld.plugin.saveConfig()
            Ok(value)
        } else Err(
            ConfigError.ARG_IS_INVALID_TYPE.withServerLog(
                "Config($configPath)に以下の値を設定しようとしました。: id:$id -> value:$value"
            )
        )
}

enum class ConfigError(private val reason: String) : IError {
    ARG_IS_INVALID_TYPE("指定された引数の型は不適切です。"),
    PATH_IS_NOT_FOUND("指定されたパスはConfigに存在しません。");

    override fun errorName() = this.name
    override fun reason() = this.reason
}
