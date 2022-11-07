package click.seichi.regenerateworld.presenter.runnable

import cats.syntax.all._
import cats.implicits._
import cats.effect.{Async, IO, Sync}
import click.seichi.regenerateworld.domain.model.{GenerationSchedule, SeedPattern}
import click.seichi.regenerateworld.presenter.GenerationScheduleUseCase
import click.seichi.regenerateworld.presenter.mixin.MixInClock
import click.seichi.regenerateworld.presenter.shared.WorldRegenerator
import click.seichi.regenerateworld.presenter.shared.exception.WorldRegenerationException
import org.bukkit.{Bukkit, ChatColor, World}
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.{BukkitRunnable, BukkitTask}

import java.time.temporal.ChronoUnit
import scala.concurrent.duration.DurationInt

object RegenerationTask extends MixInClock {
  def runAtNextDate(schedule: GenerationSchedule)(implicit instance: JavaPlugin): BukkitTask = {
    val difference = clock.now.until(schedule.nextDateTime, ChronoUnit.SECONDS)
    require(difference >= 0, "The schedule's NextDateTime must be in the future")

    new BukkitRunnable {
      override def run(): Unit = runInstantly(schedule)
    }.runTaskLaterAsynchronously(instance, difference * 20L)
  }

  def runInstantly(schedule: GenerationSchedule)(implicit instance: JavaPlugin) =
    new RegenerationTask(schedule).ooo[IO]()
}

private class RegenerationTask(private val schedule: GenerationSchedule)(
  implicit instance: JavaPlugin
) {

  private def aaaa[F[_]: Async](worldName: String) =
    for {
      world <- Async[F].delay(Option(Bukkit.getWorld(worldName)))
      world <- world match {
        case Some(world) => Async[F].pure(world)
        case None => Async[F].raiseError(WorldRegenerationException.WorldIsNotFound(worldName))
      }
      seedPattern =
        if (schedule.seedPattern.seedValueIsRequiredExplicitly) SeedPattern.RandomNewSeed
        else schedule.seedPattern
      _ <- Sync[F].blocking(WorldRegenerator.regenBukkitWorld(world, seedPattern, None))
    } yield worldName

  def ooo[F[_]: Async]() =
    for {
      count <- 10 to 0 by -1
    } yield for {
      _ <-
        Async[F]
          .delay {
            Bukkit.broadcastMessage(
              s"${ChatColor.RED}The regeneration of ${schedule.worlds.mkString(", ")} will be held in $count minute(s)!"
            )
          }
          .whenA(Set(1, 2, 3, 5, 10).contains(count))
      _ <- Async[F].delay(schedule.worlds.toList.traverse(aaaa)).whenA(count == 0)
      _ <- Async[F].sleep(1.seconds)
    } yield ()

  def run(): Unit =
    for (count <- 10 to 0 by -1) {
      if (count == 0) {
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
              WorldRegenerator.regenBukkitWorld(world, seedPattern, None)
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
      } else if (Set(1, 3, 5, 10).contains(count)) {
        Bukkit.broadcastMessage(
          s"${ChatColor.RED}The regeneration of ${schedule.worlds.mkString(", ")} will be held in $count minute(s)!"
        )
      }

      // TODO: wait 60 secs
      Thread.sleep(1 * 1000)
    }

  private def regenSync(world: World, seedPattern: SeedPattern) = {
    var res: Either[WorldRegenerationException, Unit] = Left(
      WorldRegenerationException.ScheduleIsNotFound
    )
    new BukkitRunnable() {
      override def run(): Unit = {
        res = WorldRegenerator.regenBukkitWorld(world, seedPattern, None)
      }
    }.runTask(instance)

    res
  }
}
