package click.seichi.regenerateworld.presenter.command.run

import click.seichi.regenerateworld.domain.model.SeedPattern
import click.seichi.regenerateworld.presenter.shared.WorldRegenerator
import click.seichi.regenerateworld.presenter.shared.contextualexecutor.executor.EchoExecutor
import click.seichi.regenerateworld.presenter.shared.contextualexecutor.{
  CommandContext,
  ContextualExecutor,
  Result
}
import click.seichi.regenerateworld.presenter.shared.contextualexecutor._
import click.seichi.regenerateworld.presenter.shared.exception.{
  CommandException,
  WorldRegenerationException
}
import org.bukkit.Bukkit

import scala.util.Try

case object New extends ContextualExecutor {
  val help: EchoExecutor = EchoExecutor(
    List("/rw run new <ワールド名> <シード値の設定> <新しいシード値>", "    指定されたワールドを指定された設定で再生成します。")
  )

  override def executionWith(context: CommandContext): Result[Unit] = {
    for {
      worldName <- Try(context.args(1)).toOption.toRight(CommandException.ArgIsInsufficient)
      world <- Option(Bukkit.getWorld(worldName))
        .toRight(WorldRegenerationException.WorldIsNotFound(worldName))
      seedPatternStr <- Try(context.args(2))
        .toOption
        .toRight(CommandException.ArgIsInsufficient)
      seedPattern <- SeedPattern
        .fromString(seedPatternStr)
        .toRight(WorldRegenerationException.SeedPatternIsNotFound(seedPatternStr))
      newSeed = Try(context.args(3)).toOption
    } yield {
      regenStartMessages(worldName).foreach(context.sender.sendMessage)

      WorldRegenerator.regenFromWorld(world, seedPattern, newSeed).onSuccess { _ =>
        context.sender.sendMessage(regenSuccessfulMessage(worldName))
      }
    }
  }
}
