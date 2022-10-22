package click.seichi.regenerateworld.presenter

import click.seichi.regenerateworld.presenter.command.Command
import click.seichi.regenerateworld.presenter.listener.RegenWorldListener
import click.seichi.regenerateworld.presenter.runnable.RegenerationTask
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

import java.time.ZonedDateTime

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
    // TODO: cancel all tasks

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

    // TODO: Add・Edit・Deleteされた際に変更を反映させる必要があるので、TaskIdを持っておきたい
    GenerationScheduleUseCase.list().foreach(RegenerationTask.runAtNextDate)
  }
}

object RegenerateWorld {
  var instance: RegenerateWorld = _
}
