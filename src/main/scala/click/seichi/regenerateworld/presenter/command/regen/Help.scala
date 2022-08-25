package click.seichi.regenerateworld.presenter.command.regen

import click.seichi.regenerateworld.presenter.shared.contextualexecutor.executor.EchoExecutor
import click.seichi.regenerateworld.presenter.shared.contextualexecutor.{
  CommandContext,
  ContextualExecutor,
  Result
}

case object Help extends ContextualExecutor {
  val help: EchoExecutor = EchoExecutor(List("/rw regen help", "    ヘルプメッセージを表示します。"))

  override def executionWith(context: CommandContext): Result[Unit] = {
    List(Schedule.help, New.help, help).foreach(_.executionWith(context))

    Right(())
  }
}
