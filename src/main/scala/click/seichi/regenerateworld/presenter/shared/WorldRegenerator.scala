package click.seichi.regenerateworld.presenter.shared

import click.seichi.regenerateworld.domain.model.SeedPattern
import Multiverse._
import com.onarandombox.MultiverseCore.api.MultiverseWorld
import enumeratum.{Enum, EnumEntry}
import org.bukkit.World

case object WorldRegenerator {
  def regen(world: MultiverseWorld, seedPattern: SeedPattern, newSeed: Option[String]): Either[WorldRegenerationError, Unit] = {
    if (seedPattern == SeedPattern.NewSeed && newSeed.isEmpty) return Left(WorldRegenerationError.SeedIsRequired)

    val isSuccessful = Multiverse.regenWorld(world, seedPattern.isNewSeed, seedPattern.isRandomSeed, newSeed)
    if (isSuccessful) Right(()) else Left(WorldRegenerationError.MultiverseError)
  }

  def regenFromWorld(bukkitWorld: World, seedPattern: SeedPattern, newSeed: Option[String]): Either[WorldRegenerationError, Unit] = {
    for {
      mvWorld <- bukkitWorld.asMVWorld().toRight(WorldRegenerationError.BukkitWorldIsNotMVWorld)
      result <- regen(mvWorld, seedPattern, newSeed)
    } yield result
  }
}

sealed abstract class WorldRegenerationError(val description: String) extends EnumEntry

object WorldRegenerationError extends Enum[WorldRegenerationError] {
  override val values: IndexedSeq[WorldRegenerationError] = findValues

  case object SeedIsRequired extends WorldRegenerationError("Seed is required with the SeedPattern")

  case object MultiverseError extends WorldRegenerationError("Multiverse occured an error while regeneration")

  case object BukkitWorldIsNotMVWorld extends WorldRegenerationError("The Bukkit world is not Multiverse World")
}