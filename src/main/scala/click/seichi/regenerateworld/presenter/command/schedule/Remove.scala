package click.seichi.regenerateworld.presenter.command.schedule

import click.seichi.regenerateworld.presenter.GenerationScheduleUseCase
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

import java.util.UUID
import scala.util.Try

case object Remove extends ContextualExecutor {
  val help: EchoExecutor = EchoExecutor(
    List("/rw schedule remove <スケジュールID>", "    指定されたIDのスケジュールを削除します。")
  )

  override def executionWith(context: CommandContext): Result[Unit] =
    for {
      uuidStr <- Try(context.args(1)).toOption.toRight(CommandException.ArgIsInsufficient)
      schedule <- Try(UUID.fromString(uuidStr))
        .toOption
        .flatMap(GenerationScheduleUseCase.findById)
        .toRight(WorldRegenerationException.ScheduleIsNotFound)
      isSuccessful = GenerationScheduleUseCase.remove(schedule.id)
    } yield if (isSuccessful) Right(()) else Left(CommandException.CommandExecutionFailed)
}
