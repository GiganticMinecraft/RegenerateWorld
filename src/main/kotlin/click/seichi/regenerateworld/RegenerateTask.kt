package click.seichi.regenerateworld

import click.seichi.regenerateworld.utils.ConfigPaths
import click.seichi.regenerateworld.utils.Plan
import click.seichi.regenerateworld.utils.Util
import com.github.michaelbull.result.get
import com.github.michaelbull.result.mapBoth
import com.github.michaelbull.result.mapError
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

class RegenerateTask(private val plan: Plan) : BukkitRunnable() {
    private val warningMinutes = setOf(1, 3, 5, 10)
    private var count: Int = 10

    override fun run() {
        val worldNames = plan.worlds.mapNotNull { Multiverse.findMvWorld(it).get() }.map { it.name }
        if (warningMinutes.contains(count)) {
            Bukkit.broadcastMessage("あと${count}分で${worldNames}の再生成が行われます。")
        } else if (count == 0) {
            object : BukkitRunnable() {
                override fun run() {
                    // 実際に再生成を行うまで時間があるので、この間にMultiverseからアンロードされている可能性を考えて、生成を行うときには再度MultiverseWorldを探す
                    plan.worlds.mapNotNull {
                        Multiverse.findMvWorld(it)
                            .mapError { err -> err.withServerLog("ワールド名: $it") }.get()
                    }.forEach { world ->
                        Multiverse.regenWorld(world, plan.seedPatterns, plan.seed).mapBoth(
                            success = { Bukkit.broadcastMessage("${world.name}の再生成を行いました。") },
                            failure = { err -> err.withServerLog("ID: ${plan.id}") }
                        )
                    }

                    val nextDate = plan.lastRegeneratedDate.plusMinutes(plan.interval)
                    Config.setData(ConfigPaths.DATE, plan.id, nextDate.toString())
                    Util.scheduleRegenerateTask(plan)
                }
            }.runTask(RegenerateWorld.plugin)
            this.cancel()
        }
        count -= 1
    }
}
