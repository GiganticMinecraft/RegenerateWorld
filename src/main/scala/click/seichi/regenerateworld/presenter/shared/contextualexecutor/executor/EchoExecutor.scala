package click.seichi.regenerateworld.presenter.shared.contextualexecutor.executor

import click.seichi.regenerateworld.presenter.shared.contextualexecutor.{
  CommandContext,
  ContextualExecutor,
  Result
}

case class EchoExecutor(private val messages: List[String]) extends ContextualExecutor {
  override def executionWith(context: CommandContext): Result[Unit] =
    Right(messages.foreach(context.sender.sendMessage))
}
