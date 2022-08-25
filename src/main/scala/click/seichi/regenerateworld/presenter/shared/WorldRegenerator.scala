package click.seichi.regenerateworld.presenter.shared

import click.seichi.regenerateworld.domain.model.SeedPattern
import Multiverse._
import com.onarandombox.MultiverseCore.api.MultiverseWorld
import enumeratum.{Enum, EnumEntry}
import org.bukkit.World

case object WorldRegenerator {
  def regen(world: MultiverseWorld, seedPattern: SeedPattern, newSeed: Option[String]): Either[WorldRegeneratorError, Unit] = {
    if (seedPattern == SeedPattern.NewSeed && newSeed.isEmpty) return Left(WorldRegeneratorError.SeedIsNeeded)

    val isSuccessful = Multiverse.regenWorld(world, seedPattern.isNewSeed, seedPattern.isRandomSeed, newSeed)
    if (isSuccessful) Right() else Left(WorldRegeneratorError.MultiverseError)
  }

  def regenFromWorld(world: World, seedPattern: SeedPattern, newSeed: Option[String]): Either[WorldRegeneratorError, Unit] = {
    for {
      mvWorld <- world.asMVWorld().toRight(WorldRegeneratorError.WorldIsNotMVWorld)
      result <- regen(mvWorld, seedPattern, newSeed)
    } yield result
  }
}

sealed abstract class WorldRegeneratorError(val description: String) extends EnumEntry

object WorldRegeneratorError extends Enum[WorldRegeneratorError] {
  override val values: IndexedSeq[WorldRegeneratorError] = findValues

  case object SeedIsNeeded extends WorldRegeneratorError("Seed is needed with the SeedPattern")

  case object MultiverseError extends WorldRegeneratorError("Multiverse occured an error while regeneration")

  case object WorldIsNotMVWorld extends WorldRegeneratorError("The world is not Multiverse World")
}