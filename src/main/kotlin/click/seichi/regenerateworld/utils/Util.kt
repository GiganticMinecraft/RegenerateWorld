package click.seichi.regenerateworld.utils

import click.seichi.regenerateworld.Config
import click.seichi.regenerateworld.RegenerateTask
import click.seichi.regenerateworld.RegenerateWorld
import org.bukkit.Bukkit
import java.time.Duration
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object Util {
    fun scheduleRegenerateTask(plan: Plan) {
        Bukkit.getScheduler().cancelTask(plan.bukkitTaskId)
        val now = ZonedDateTime.now()
        val nextDate = now.plusMinutes(plan.interval)
        val taskId = RegenerateTask(plan).runTaskTimer(
            RegenerateWorld.plugin,
            Duration.between(now, nextDate).toMinutes() * 60 * 20,
            20L * 1
        ).taskId
        Logger.info(
            "次回再生成は「${nextDate.format(DateTimeFormatter.ISO_LOCAL_DATE)} ${
                nextDate.format(DateTimeFormatter.ISO_LOCAL_TIME)
            }」"
        )
        Config.setData(ConfigPaths.TASK_ID, plan.id, taskId)
        Config.setData(ConfigPaths.DATE, plan.id, nextDate.toString())
    }
}