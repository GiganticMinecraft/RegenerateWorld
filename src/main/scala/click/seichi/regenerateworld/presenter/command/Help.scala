package click.seichi.regenerateworld.presenter.command

import click.seichi.regenerateworld.presenter.shared.contextualexecutor.executor.EchoExecutor
import click.seichi.regenerateworld.presenter.shared.contextualexecutor.{
  CommandContext,
  ContextualExecutor,
  Result
}

case object Help extends ContextualExecutor {
  val help: EchoExecutor = EchoExecutor(List("/rw help", "    ヘルプメッセージを表示します。"))

  override def executionWith(context: CommandContext): Result[Unit] = {
    List(ListSchedules.help, help, run.help).foreach(_.executionWith(context))

    Right(())
  }
}
