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
}
