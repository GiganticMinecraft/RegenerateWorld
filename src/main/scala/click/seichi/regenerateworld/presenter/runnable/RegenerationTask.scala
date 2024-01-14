package click.seichi.regenerateworld.presenter.runnable

import click.seichi.regenerateworld.domain.model.{GenerationSchedule, SeedPattern}
import click.seichi.regenerateworld.presenter.GenerationScheduleUseCase
import click.seichi.regenerateworld.presenter.mixin.MixInClock
import click.seichi.regenerateworld.presenter.shared.WorldRegenerator
import click.seichi.regenerateworld.presenter.shared.exception.WorldRegenerationException
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.{BukkitRunnable, BukkitTask}
import org.bukkit.{Bukkit, ChatColor}

import java.time.temporal.ChronoUnit

object RegenerationTask extends MixInClock {
  def runAtNextDate(schedule: GenerationSchedule)(implicit instance: JavaPlugin): BukkitTask = {
    val difference = clock.now.until(schedule.nextDateTime, ChronoUnit.SECONDS)
    require(difference >= 0, "The schedule's NextDateTime must be in the future")

    new BukkitRunnable {
      override def run(): Unit = runInstantly(schedule)
    }.runTaskLaterAsynchronously(instance, difference * 20L)
  }

  def runInstantly(schedule: GenerationSchedule)(
    implicit instance: JavaPlugin
  ): Unit =
    new RegenerationTask(schedule).run()
}

private class RegenerationTask(private val schedule: GenerationSchedule)(
  implicit instance: JavaPlugin
) {
  private def run(): Unit =
    for (count <- 10 to 0 by -1) {
      count match {
        case 1 | 3 | 5 | 10 =>
          Bukkit.broadcastMessage(
            s"${ChatColor.RED}The regeneration of ${schedule.worlds.mkString(", ")} will be held in $count minute(s)!"
          )
        case 0 =>
          val regenResults = for {
            worldName <- schedule.worlds
          } yield for {
            world <- Option(Bukkit.getWorld(worldName))
              .toRight(WorldRegenerationException.WorldIsNotFound(worldName))
            seedPattern =
              if (schedule.seedPattern.seedValueIsRequiredExplicitly) SeedPattern.RandomNewSeed
              else schedule.seedPattern
            _ = new BukkitRunnable() {
              override def run(): Unit =
                WorldRegenerator.regenBukkitWorld(Some(world), seedPattern, None)
            }.runTask(instance)
          } yield worldName

          regenResults.foreach(res => {
            val logger = instance.getLogger

            res match {
              case Right(worldName) =>
                logger.info(
                  s"The world (name: $worldName) regeneration schedule (ID: ${schedule.id}) has completed"
                )
              case Left(e) =>
                logger.severe(
                  s"Error has occurred while regenerating a world with the schedule (ID: ${schedule.id}): ${e.description}"
                )
            }
          })

          // Scheduleの次回日時の設定は1回でいいので、for-yieldの外に置く
          new BukkitRunnable {
            override def run(): Unit = GenerationScheduleUseCase.finish(schedule.id)
          }.runTask(instance)
        case _ =>
      }

      // TODO: wait 60 secs
      Thread.sleep(1 * 1000)
    }
}
