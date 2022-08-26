package click.seichi.regenerateworld.presenter.shared.exception

import enumeratum.{Enum, EnumEntry}

sealed abstract class CommandException(override val description: String)
    extends EnumEntry
    with OriginalException

object CommandException extends Enum[CommandException] {
  override val values: IndexedSeq[CommandException] = findValues

  case object ArgIsInsufficient extends CommandException("Arg is insufficient. See /rw help")

  case object CommandExecutionFailed extends CommandException("Execution has failed")
}
