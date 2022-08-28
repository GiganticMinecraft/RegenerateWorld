package click.seichi.regenerateworld.presenter.shared.contextualexecutor

import click.seichi.regenerateworld.presenter.shared.exception.CommandException

import java.util.UUID
import scala.util.Try

object parser {
  def uuid: SingleArgumentParser = arg =>
    Try(UUID.fromString(arg)).toOption.toRight(CommandException.ArgIsNotUuid)
}
