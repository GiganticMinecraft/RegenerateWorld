package click.seichi.regenerateworld.presenter.command.run

import click.seichi.regenerateworld.presenter.GenerationScheduleUseCase
import click.seichi.regenerateworld.presenter.shared.WorldRegenerator
import click.seichi.regenerateworld.presenter.shared.contextualexecutor._
import click.seichi.regenerateworld.presenter.shared.contextualexecutor.executor.EchoExecutor
import click.seichi.regenerateworld.presenter.shared.contextualexecutor.{
  CommandContext,
  ContextualExecutor,
  Result
}
import click.seichi.regenerateworld.presenter.shared.exception.WorldRegenerationException
import org.bukkit.Bukkit

import java.util.UUID

case object Schedule extends ContextualExecutor {
  val help: EchoExecutor = EchoExecutor(
    List(
      "/rw run schedule <スケジュールID> <新しいシード値>",
      "    指定されたスケジュールの再生成を即時に行います。(シード値の設定がNewになっている場合は新しいシード値を指定する必要があります)"
    )
  )

  override def executionWith(context: CommandContext): Result[Unit] = {
    for {
      args <- parseArguments(List(Parsers.uuid))(context)
      uuid = args.parsed.head.asInstanceOf[UUID]
      schedule <- GenerationScheduleUseCase
        .findById(uuid)
        .toRight(WorldRegenerationException.ScheduleIsNotFound)
      newSeed = args.yetToBeParsed.headOption
    } yield for {
      worldName <- schedule.worlds
      world = Option(Bukkit.getWorld(worldName))
    } yield world match {
      case Some(world) =>
        regenStartMessages(worldName).foreach(context.sender.sendMessage)

        WorldRegenerator
          .regenBukkitWorld(Some(world), schedule.seedPattern, newSeed)
          .onSuccess { _ =>
            GenerationScheduleUseCase.finish(schedule.id)
            context.sender.sendMessage(regenSuccessfulMessage(worldName))
          }
      case _ => Left(WorldRegenerationException.WorldIsNotFound(worldName))
    }
  }
}
