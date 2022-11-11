package click.seichi.regenerateworld.presenter.shared

import click.seichi.regenerateworld.domain.model.SeedPattern
import click.seichi.regenerateworld.presenter.shared.external.Multiverse._
import click.seichi.regenerateworld.presenter.event.{PreRegenWorldEvent, RegenWorldEvent}
import click.seichi.regenerateworld.presenter.shared.exception.WorldRegenerationException
import click.seichi.regenerateworld.presenter.shared.external.Multiverse
import com.onarandombox.MultiverseCore.api.MultiverseWorld
import org.bukkit.{Bukkit, World}

case object WorldRegenerator {
  def regen(
    world: MultiverseWorld,
    seedPattern: SeedPattern,
    newSeed: Option[String]
  ): Either[WorldRegenerationException, Unit] = {
    for {
      _ <-
        if (seedPattern.seedValueIsRequiredExplicitly && newSeed.forall(_.isEmpty))
          Left(WorldRegenerationException.SeedIsRequired)
        else Right(())
      preEvent = PreRegenWorldEvent(world.getName)
      _ = Bukkit.getPluginManager.callEvent(preEvent)
      _ <-
        if (preEvent.isCancelled) Left(WorldRegenerationException.RegenWorldEventIsCancelled)
        else Right(())
      _ = Bukkit.getPluginManager.callEvent(RegenWorldEvent(world.getName))
      _ <- Multiverse.regenWorld(
        world,
        seedPattern.isNewSeed,
        seedPattern.isRandomSeed,
        newSeed
      )
    } yield ()
  }

  def regenBukkitWorld(
    bukkitWorld: Option[World],
    seedPattern: SeedPattern,
    newSeed: Option[String]
  ): Either[WorldRegenerationException, Unit] =
    bukkitWorld match {
      case Some(world) =>
        world
          .asMVWorld()
          .toRight(WorldRegenerationException.BukkitWorldIsNotMVWorld(world.getName))
          .flatMap(regen(_, seedPattern, newSeed))
      case None => Left(WorldRegenerationException.WorldIsNotFound(""))
    }
}
