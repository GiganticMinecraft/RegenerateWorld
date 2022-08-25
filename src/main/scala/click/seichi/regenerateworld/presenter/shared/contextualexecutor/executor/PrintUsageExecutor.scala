package click.seichi.regenerateworld.presenter.shared.contextualexecutor.executor

import click.seichi.regenerateworld.presenter.shared.contextualexecutor.{CommandContext, ContextualExecutor}

case object PrintUsageExecutor extends ContextualExecutor {
  override def executionWith(context: CommandContext): Either[Throwable, Unit] =
    Right(context.sender.sendMessage(context.command.getUsage))
}