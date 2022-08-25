package click.seichi.regenerateworld.presenter.shared

import click.seichi.regenerateworld.domain.model.SeedPattern
import Multiverse._
import com.onarandombox.MultiverseCore.api.MultiverseWorld
import enumeratum.{Enum, EnumEntry}
import org.bukkit.World

case object WorldRegenerator {
  def regen(world: MultiverseWorld, seedPattern: SeedPattern, newSeed: Option[String]): Either[WorldRegeneratorError, Unit] = {
    if (seedPattern == SeedPattern.NewSeed && newSeed.isEmpty) return Left(WorldRegeneratorError.SeedIsRequired)

    val isSuccessful = Multiverse.regenWorld(world, seedPattern.isNewSeed, seedPattern.isRandomSeed, newSeed)
    if (isSuccessful) Right() else Left(WorldRegeneratorError.MultiverseError)
  }

  def regenFromWorld(bukkitWorld: World, seedPattern: SeedPattern, newSeed: Option[String]): Either[WorldRegeneratorError, Unit] = {
    for {
      mvWorld <- bukkitWorld.asMVWorld().toRight(WorldRegeneratorError.BukkitWorldIsNotMVWorld)
      result <- regen(mvWorld, seedPattern, newSeed)
    } yield result
  }
}

sealed abstract class WorldRegeneratorError(val description: String) extends EnumEntry

object WorldRegeneratorError extends Enum[WorldRegeneratorError] {
  override val values: IndexedSeq[WorldRegeneratorError] = findValues

  case object SeedIsRequired extends WorldRegeneratorError("Seed is required with the SeedPattern")

  case object MultiverseError extends WorldRegeneratorError("Multiverse occured an error while regeneration")

  case object BukkitWorldIsNotMVWorld extends WorldRegeneratorError("The Bukkit world is not Multiverse World")
}