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
        if (seedPattern == SeedPattern.NewSeed && newSeed.forall(_.isEmpty))
          Left(WorldRegenerationException.SeedIsRequired)
        else Right(())
      preEvent = PreRegenWorldEvent(world.getName)
      _ = Bukkit.getPluginManager.callEvent(preEvent)
      _ <-
        if (preEvent.isCancelled) Left(WorldRegenerationException.RegenWorldEventIsCancelled)
        else Right(())
      _ = Bukkit.getPluginManager.callEvent(RegenWorldEvent(world.getName))
      isSuccessful = Multiverse.regenWorld(
        world,
        seedPattern.isNewSeed,
        seedPattern.isRandomSeed,
        newSeed
      )
      _ <- if (isSuccessful) Right(()) else Left(WorldRegenerationException.MultiverseException)
    } yield ()
  }

  def regenBukkitWorld(
    bukkitWorld: World,
    seedPattern: SeedPattern,
    newSeed: Option[String]
  ): Either[WorldRegenerationException, Unit] =
    bukkitWorld
      .asMVWorld()
      .toRight(WorldRegenerationException.BukkitWorldIsNotMVWorld(bukkitWorld.getName))
      .flatMap(regen(_, seedPattern, newSeed))
}
