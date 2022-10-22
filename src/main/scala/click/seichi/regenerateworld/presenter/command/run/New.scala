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
import org.bukkit.World

case object New extends ContextualExecutor {
  val help: EchoExecutor = EchoExecutor(
    List(
      "/rw run new <ワールド名> <シード値の設定> <新しいシード値>",
      "    指定されたワールドを指定された設定で再生成します。(シード値の設定がNewになっている場合は新しいシード値を指定する必要があります)"
    )
  )

  override def executionWith(context: CommandContext): Result[Unit] = {
    for {
      args <- parseArguments(List(Parsers.bukkitWorld, Parsers.seedPattern))(context)
      world = args.parsed.head.asInstanceOf[World]
      worldName = world.getName
      seedPattern = args.parsed(1).asInstanceOf[SeedPattern]
      newSeed = args.yetToBeParsed.headOption
    } yield {
      regenStartMessages(worldName).foreach(context.sender.sendMessage)

      WorldRegenerator.regenBukkitWorld(world, seedPattern, newSeed).onSuccess { _ =>
        context.sender.sendMessage(regenSuccessfulMessage(worldName))
      }
    }
  }
}
