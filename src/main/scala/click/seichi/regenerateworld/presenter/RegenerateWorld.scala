package click.seichi.regenerateworld.presenter

import click.seichi.regenerateworld.presenter.RegenerateWorld.regenerationTasks
import click.seichi.regenerateworld.presenter.command.Command
import click.seichi.regenerateworld.presenter.listener.RegenWorldListener
import click.seichi.regenerateworld.presenter.runnable.RegenerationTask
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

import java.time.ZonedDateTime
import java.util.UUID
import scala.collection.mutable

class RegenerateWorld extends JavaPlugin {
  override def onEnable(): Unit = {
    RegenerateWorld.instance = this

    saveDefaultConfig()

    getCommand("regenworld").setExecutor(Command.executor)
    getServer.getPluginManager.registerEvents(RegenWorldListener, this)

    Bukkit.getLogger.info("RegenerateWorld is enabled!")

    runRegeneration()
  }

  override def onDisable(): Unit = {
    regenerationTasks.values.foreach(_.cancel())

    Bukkit.getLogger.info("RegenerateWorld is disabled!")
  }

  private def runRegeneration(): Unit = {
    implicit val instance: RegenerateWorld = this

    GenerationScheduleUseCase
      .filteredList(schedule => {
        val nextDateTime = schedule.nextDateTime
        val now = ZonedDateTime.now()

        nextDateTime.isBefore(now) || nextDateTime.isEqual(now)
      })
      .foreach(RegenerationTask.runInstantly)

    // TODO: Add・Edit・Deleteされた際にBukkitTaskを登録し直す
    GenerationScheduleUseCase
      .list()
      .map(schedule => (schedule.id, RegenerationTask.runAtNextDate(schedule).getTaskId))
      .foreach { case (scheduleId, taskId) => regenerationTasks.put(scheduleId, taskId) }
  }
}

object RegenerateWorld {
  var instance: RegenerateWorld = _
  val regenerationTasks: mutable.Map[UUID, Int] = mutable.Map()
}
