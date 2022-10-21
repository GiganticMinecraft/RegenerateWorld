package click.seichi.regenerateworld.presenter.runnable

import click.seichi.regenerateworld.domain.model.{GenerationSchedule, SeedPattern}
import click.seichi.regenerateworld.presenter.GenerationScheduleUseCase
import click.seichi.regenerateworld.presenter.shared.WorldRegenerator
import click.seichi.regenerateworld.presenter.shared.exception.WorldRegenerationException
import org.bukkit.{Bukkit, ChatColor}
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

object RegenerateTask {
  def run(schedule: GenerationSchedule)(implicit instance: JavaPlugin): Unit = {
    new RegenerateTask(schedule).runTaskTimerAsynchronously(instance, 0L, 20L)
  }
}

private class RegenerateTask(private val schedule: GenerationSchedule)(
  implicit val instance: JavaPlugin
) extends BukkitRunnable {
  private var count = 10

  override def run(): Unit = {
    if (count == 0) {
      for {
        worldName <- schedule.worlds
        world = Option(Bukkit.getWorld(worldName))
          .toRight(WorldRegenerationException.WorldIsNotFound(worldName))
        seedPattern =
          if (schedule.seedPattern.seedValueIsRequiredExplicitly) SeedPattern.RandomNewSeed
          else schedule.seedPattern
        result = world.flatMap(w => WorldRegenerator.regenBukkitWorld(w, seedPattern, None))
        logger = instance.getLogger
      } yield result match {
        case Right(_) =>
          logger.info(
            s"The world ($worldName) regeneration schedule (ID: ${schedule.id}) has completed"
          )
        case Left(e) =>
          logger.severe(
            s"Error has occurred while regenerating a world ($worldName) with the schedule (ID: ${schedule.id}): ${e.description}"
          )
      }

      // RunnableのcancelとScheduleの次回日時の設定は1回でいいので、for-yieldの外に置く
      GenerationScheduleUseCase.finish(schedule.id)
      this.cancel()
    } else if (Set(1, 3, 5, 10).contains(count)) {
      Bukkit.broadcastMessage(
        s"${ChatColor.RED}The regeneration of ${schedule.worlds.mkString(", ")} will be held in $count minute(s)!"
      )
    }

    count -= 1
  }
}
