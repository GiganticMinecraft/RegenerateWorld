package click.seichi.regenerateworld.presenter.shared.exception

import enumeratum.{Enum, EnumEntry}

sealed abstract class CommandException(override val description: String)
    extends EnumEntry
    with OriginalException

object CommandException extends Enum[CommandException] {
  override val values: IndexedSeq[CommandException] = findValues

  case object OnlyPlayerCanExecute
      extends CommandException("The command can be executed only by in-game players")
}
