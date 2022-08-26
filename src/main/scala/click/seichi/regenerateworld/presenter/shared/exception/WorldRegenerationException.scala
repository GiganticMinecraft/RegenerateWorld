package click.seichi.regenerateworld.presenter.shared.exception

import enumeratum.{Enum, EnumEntry}

sealed abstract class WorldRegenerationException(override val description: String)
    extends EnumEntry
    with OriginalException

object WorldRegenerationException extends Enum[WorldRegenerationException] {
  override val values: IndexedSeq[WorldRegenerationException] = findValues

  case object SeedIsRequired
      extends WorldRegenerationException("Seed is required with the SeedPattern")

  case object MultiverseException
      extends WorldRegenerationException(
        "Multiverse occurred an error while regenerating the world"
      )

  case object BukkitWorldIsNotMVWorld
      extends WorldRegenerationException("The Bukkit world is not a Multiverse World")

  case object WorldIsNotFound extends WorldRegenerationException("The world is not found")

  case object SeedPatternIsNotFound
      extends WorldRegenerationException("The SeedPattern is not found")

  case object ScheduleIsNotFound
      extends WorldRegenerationException("The RegenerationSchedule is not found")

  case object RegenWorldEventIsCancelled
      extends WorldRegenerationException("The RegenWorldEvent is cancelled")
}
