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
import click.seichi.regenerateworld.presenter.shared.exception.WorldRegenerationException
import org.bukkit.{Bukkit, World}

case object New extends ContextualExecutor {
  val help: EchoExecutor = EchoExecutor(
    List("/rw run new <ワールド名> <シード値の設定> <新しいシード値>", "    指定されたワールドを指定された設定で再生成します。")
  )

  override def executionWith(context: CommandContext): Result[Unit] = {
    def parseWorld: SingleArgumentParser = arg =>
      Option(Bukkit.getWorld(arg)).toRight(WorldRegenerationException.WorldIsNotFound(arg))
    def parseSeedPattern: SingleArgumentParser = arg =>
      SeedPattern.fromString(arg).toRight(WorldRegenerationException.SeedPatternIsNotFound(arg))

    for {
      args <- parseArguments(List(parseWorld, parseSeedPattern))(context)
      world = args.parsed.head.asInstanceOf[World]
      worldName = world.getName
      seedPattern = args.parsed[1].asInstanceOf[SeedPattern]
      newSeed = args.yetToBeParsed.headOption
    } yield {
      regenStartMessages(worldName).foreach(context.sender.sendMessage)

      WorldRegenerator.regenFromWorld(world, seedPattern, newSeed).onSuccess { _ =>
        context.sender.sendMessage(regenSuccessfulMessage(worldName))
      }
    }
  }
}
