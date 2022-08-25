package click.seichi.regenerateworld.presenter.command.regen

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
import org.bukkit.{Bukkit, ChatColor}

import scala.util.Try

case object New extends ContextualExecutor {
  val help: EchoExecutor = EchoExecutor(
    List("/rw regen new <ワールド名> <シード値の設定> <新しいシード値>", "    指定されたワールドを指定された設定で再生成します。")
  )

  override def executionWith(context: CommandContext): Result[Unit] = {
    for {
      worldName <- Try(context.args(1)).toOption.toRight(CommandException.ArgIsInsufficient)
      world <- Option(Bukkit.getWorld(worldName))
        .toRight(WorldRegenerationException.WorldIsNotFound)
      seedPatternStr <- Try(context.args(2))
        .toOption
        .toRight(CommandException.ArgIsInsufficient)
      seedPattern <- SeedPattern
        .fromString(seedPatternStr)
        .toRight(WorldRegenerationException.SeedPatternIsNotFound)
      newSeed = Try(context.args(3)).toOption
    } yield {
      Set("ワールドの再生成を開始します。", "この処理には時間がかかる可能性があります。")
        .map { msg => s"${ChatColor.GREEN}$msg" }
        .foreach(context.sender.sendMessage)

      WorldRegenerator.regenFromWorld(world, seedPattern, newSeed).onSuccess { _ =>
        context.sender.sendMessage(s"${ChatColor.GREEN}ワールドの再生成が終了しました。")
      }
    }
  }
}
