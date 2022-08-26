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

  case class BukkitWorldIsNotMVWorld(worldName: String)
      extends WorldRegenerationException(
        s"The Bukkit world ($worldName) is not a Multiverse World"
      )

  case class WorldIsNotFound(worldName: String)
      extends WorldRegenerationException(s"The world ($worldName) is not found")

  case class SeedPatternIsNotFound(seedPattern: String)
      extends WorldRegenerationException(s"The SeedPattern ($seedPattern) is not found")

  case object ScheduleIsNotFound
      extends WorldRegenerationException("The RegenerationSchedule is not found")

  case object RegenWorldEventIsCancelled
      extends WorldRegenerationException("The RegenWorldEvent was cancelled")
}
