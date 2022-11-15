package click.seichi.regenerateworld.presenter.runnable

import cats.Monad
import cats.effect.std.Dispatcher
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

  def runInstantly(schedule: GenerationSchedule)(
    implicit instance: JavaPlugin
  ): IO[List[Unit]] =
    new RegenerationTask(schedule).ooo[IO]()
}

trait OnMinecraftServerThread[F[_]] {

  /**
   * マインクラフトサーバーが走るスレッド上でアクションを実行する。
   */
  def runAction[G[_]: Dispatcher: Sync, A](ga: G[A]): F[A]
}

object OnMinecraftServerThread {
  def apply[F[_]](implicit ev: OnMinecraftServerThread[F]): OnMinecraftServerThread[F] = ev
}

class OnBukkitServerThread[F[_]: Monad: Async](implicit plugin: JavaPlugin)
    extends OnMinecraftServerThread[F] {
  override def runAction[G[_]: Async, A](ga: G[A]): F[A] = {
    val checkMainThread = Sync[G].delay {
      plugin.getServer.isPrimaryThread
    }

    for {
      immediateResult <- checkMainThread.ifM[Option[A]](ga.map(Some.apply), Monad[G].pure(None))
      result <- immediateResult match {
        case Some(value) => Monad[F].pure(value)
        case None =>
          Async[F].async[A] { cb =>
            val runnable: Runnable = () => {
              val a =
                Dispatcher[G].use(dispatcher => Sync[G].delay(dispatcher.unsafeRunSync(ga)))

              cb(a.asRight)
            }

            val task = Bukkit.getScheduler.runTask(plugin, runnable)
            Async[F].delay(task.cancel())
          }
      }
    } yield result
  }
}

private class RegenerationTask(private val schedule: GenerationSchedule)(
  implicit instance: JavaPlugin
) {
  private def aaaa[F[_]: Async](worldName: String): F[Unit] = {
    val seedPattern =
      if (schedule.seedPattern.seedValueIsRequiredExplicitly) SeedPattern.RandomNewSeed
      else schedule.seedPattern

    for {
      world <- Async[F].delay(Option(Bukkit.getWorld(worldName)))
      result <- new OnBukkitServerThread[F]
        .runAction(Sync[F].delay(WorldRegenerator.regenBukkitWorld(world, seedPattern, None)))
//      result <- Sync[F].delay(WorldRegenerator.regenBukkitWorld(world, seedPattern, None))
    } yield {
      val logger = instance.getLogger

      result match {
        case Right(worldName) =>
          logger.info(
            s"The world (name: $worldName) regeneration schedule (ID: ${schedule.id}) has completed"
          )
        case Left(e) =>
          logger.severe(
            s"Error has occurred while regenerating a world with the schedule (ID: ${schedule.id}): ${e.description}"
          )
      }
    }
  }

  def ooo[F[_]: Async](): F[List[Unit]] = {
    val countDowns = Set(1, 2, 3, 5, 10)

    (10 to 0 by -1).toList.traverse { count =>
      for {
        _ <-
          Async[F]
            .delay {
              Bukkit.broadcastMessage(
                s"${ChatColor.RED}The regeneration of ${schedule.worlds.mkString(", ")} will be held in $count minute(s)!"
              )
            }
            .whenA(countDowns.contains(count))
        _ <- schedule.worlds.toList.traverse(aaaa(_)).whenA(count == 0)
        _ <- Async[F].sleep(1.seconds)
      } yield ()
    }
  }

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
      } else if (Set(1, 3, 5, 10).contains(count)) {
        Bukkit.broadcastMessage(
          s"${ChatColor.RED}The regeneration of ${schedule.worlds.mkString(", ")} will be held in $count minute(s)!"
        )
      }

      // TODO: wait 60 secs
      Thread.sleep(1 * 1000)
    }
}
