package click.seichi.regenerateworld.presenter

import scala.concurrent.ExecutionContext.Implicits._
import click.seichi.regenerateworld.presenter.RegenerateWorld.{instance, regenerationTasks}
import click.seichi.regenerateworld.presenter.command.Command
import click.seichi.regenerateworld.presenter.listener.RegenWorldListener
import click.seichi.regenerateworld.presenter.runnable.RegenerationTask
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask

import java.time.ZonedDateTime
import java.util.UUID
import scala.collection.mutable
import scala.concurrent.Future

class RegenerateWorld extends JavaPlugin {
  override def onEnable(): Unit = {
    RegenerateWorld.instance = this

    saveDefaultConfig()

    getCommand("regenworld").setExecutor(Command.executor)
    getServer.getPluginManager.registerEvents(RegenWorldListener, this)

    Bukkit.getLogger.info("RegenerateWorld is enabled!")

    runRegeneration()
    instance.getLogger.severe("on enable awaited?")
  }

  override def onDisable(): Unit = {
    regenerationTasks.values.foreach(_.cancel())

    Bukkit.getLogger.info("RegenerateWorld is disabled!")
  }

  private def runRegeneration(): Unit = Future {
    import RegenerateWorld._
    import cats.effect.unsafe.implicits._

    for {
      schedule <- GenerationScheduleUseCase.filteredList(schedule => {
        val nextDateTime = schedule.nextDateTime
        val now = ZonedDateTime.now()

        nextDateTime.isBefore(now) || nextDateTime.isEqual(now)
      })
    } yield RegenerationTask.runInstantly(schedule).unsafeRunAsync(_ => println())

//    val ft = GenerationScheduleUseCase
//      .filteredList(schedule => {
//        val nextDateTime = schedule.nextDateTime
//        val now = ZonedDateTime.now()
//
//        nextDateTime.isBefore(now) || nextDateTime.isEqual(now)
//      })
//      .map(schedule => RegenerationTask.runInstantly(schedule))

    // TODO: await fin
    instance.getLogger.severe("awaited?")
  }
//    GenerationScheduleUseCase
//      .list()
//      .tapEach(schedule => {
//        val nextDateTime = schedule.nextDateTime
//        val now = ZonedDateTime.now()
//
//        if (nextDateTime.isBefore(now) || nextDateTime.isEqual(now))
//          RegenerationTask.runInstantly(schedule)
//      })
//      .tapEach(_ => instance.getLogger.info("awaited?"))
//      .map(schedule => (schedule.id, RegenerationTask.runAtNextDate(schedule)))
//      .foreach { case (scheduleId, task) => regenerationTasks.put(scheduleId, task) }
}

object RegenerateWorld {
  implicit var instance: RegenerateWorld = _
  val regenerationTasks: mutable.Map[UUID, BukkitTask] = mutable.Map()
}
