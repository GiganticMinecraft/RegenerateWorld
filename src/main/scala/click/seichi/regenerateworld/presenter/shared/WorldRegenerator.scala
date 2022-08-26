package click.seichi.regenerateworld.presenter.shared

import click.seichi.regenerateworld.domain.model.SeedPattern
import Multiverse._
import click.seichi.regenerateworld.presenter.event.{PreRegenWorldEvent, RegenWorldEvent}
import click.seichi.regenerateworld.presenter.shared.exception.WorldRegenerationException
import com.onarandombox.MultiverseCore.api.MultiverseWorld
import org.bukkit.{Bukkit, World}

case object WorldRegenerator {
  def regen(
    world: MultiverseWorld,
    seedPattern: SeedPattern,
    newSeed: Option[String]
  ): Either[WorldRegenerationException, Unit] = {
    if (seedPattern == SeedPattern.NewSeed && newSeed.isEmpty)
      return Left(WorldRegenerationException.SeedIsRequired)

    val preEvent = PreRegenWorldEvent(world.getName)
    Bukkit.getPluginManager.callEvent(preEvent)
    if (preEvent.isCancelled) return Left(WorldRegenerationException.RegenWorldEventIsCancelled)

    val isSuccessful =
      Multiverse.regenWorld(world, seedPattern.isNewSeed, seedPattern.isRandomSeed, newSeed)
    Bukkit.getPluginManager.callEvent(RegenWorldEvent(world.getName))
    if (isSuccessful) Right(()) else Left(WorldRegenerationException.MultiverseException)
  }

  def regenFromWorld(
    bukkitWorld: World,
    seedPattern: SeedPattern,
    newSeed: Option[String]
  ): Either[WorldRegenerationException, Unit] = {
    for {
      mvWorld <- bukkitWorld
        .asMVWorld()
        .toRight(WorldRegenerationException.BukkitWorldIsNotMVWorld(bukkitWorld.getName))
      result <- regen(mvWorld, seedPattern, newSeed)
    } yield result
  }
}
