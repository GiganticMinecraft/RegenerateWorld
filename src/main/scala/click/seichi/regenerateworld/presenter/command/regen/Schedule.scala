package click.seichi.regenerateworld.presenter.command.regen

import click.seichi.regenerateworld.presenter.shared.contextualexecutor.executor.EchoExecutor
import click.seichi.regenerateworld.presenter.shared.contextualexecutor.{
  CommandContext,
  ContextualExecutor,
  Result
}

case object Schedule extends ContextualExecutor {
  val help: EchoExecutor = EchoExecutor(
    List("/rw regen schedule <スケジュールID>", "    指定されたスケジュールの再生成を行います。")
  )

  // TODO: impl
  override def executionWith(context: CommandContext): Result[Unit] = ???
}
