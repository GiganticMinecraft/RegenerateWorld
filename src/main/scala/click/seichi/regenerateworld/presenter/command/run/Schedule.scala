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
import click.seichi.regenerateworld.presenter.shared.exception.{
  CommandException,
  WorldRegenerationException
}
import org.bukkit.Bukkit

import java.util.UUID
import scala.util.Try

case object Schedule extends ContextualExecutor {
  val help: EchoExecutor = EchoExecutor(
    List("/rw run schedule <スケジュールID> <新しいシード値>", "    指定されたスケジュールの再生成を即時に行います。")
  )

  override def executionWith(context: CommandContext): Result[Unit] = {
    for {
      uuidStr <- Try(context.args(1)).toOption.toRight(CommandException.ArgIsInsufficient)
      schedule <- Try(UUID.fromString(uuidStr))
        .toOption
        .flatMap(GenerationScheduleUseCase.findById)
        .toRight(WorldRegenerationException.ScheduleIsNotFound)
      newSeed = Try(context.args(2)).toOption
    } yield for {
      worldName <- schedule.worlds
      world = Option(Bukkit.getWorld(worldName))
    } yield world match {
      case Some(w) =>
        regenStartMessages(worldName).foreach(context.sender.sendMessage)

        WorldRegenerator.regenFromWorld(w, schedule.seedPattern, newSeed).onSuccess { _ =>
          context.sender.sendMessage(regenSuccessfulMessage(worldName))
        }
      case _ => Left(WorldRegenerationException.WorldIsNotFound(worldName))
    }
  }
}
