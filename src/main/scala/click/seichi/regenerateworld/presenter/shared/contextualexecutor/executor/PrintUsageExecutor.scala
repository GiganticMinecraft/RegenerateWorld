package click.seichi.regenerateworld.presenter.shared.contextualexecutor.executor

import click.seichi.regenerateworld.presenter.shared.contextualexecutor.{CommandContext, ContextualExecutor, Result}

case object PrintUsageExecutor extends ContextualExecutor {
  override def executionWith(context: CommandContext): Result[Unit] =
    Right(context.sender.sendMessage(context.command.getUsage))
}