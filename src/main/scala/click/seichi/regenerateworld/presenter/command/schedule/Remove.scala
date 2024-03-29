package click.seichi.regenerateworld.presenter.command.schedule

import click.seichi.regenerateworld.presenter.GenerationScheduleUseCase
import click.seichi.regenerateworld.presenter.shared.contextualexecutor.executor.EchoExecutor
import click.seichi.regenerateworld.presenter.shared.contextualexecutor.{
  CommandContext,
  ContextualExecutor,
  Parsers,
  Result
}
import click.seichi.regenerateworld.presenter.shared.exception.{
  CommandException,
  WorldRegenerationException
}
import org.bukkit.ChatColor

import java.util.UUID

case object Remove extends ContextualExecutor {
  val help: EchoExecutor = EchoExecutor(
    List("/rw schedule remove <スケジュールID>", "    指定されたIDのスケジュールを削除します。")
  )

  override def executionWith(context: CommandContext): Result[Unit] =
    for {
      args <- parseArguments(List(Parsers.uuid))(context)
      uuid = args.parsed.head.asInstanceOf[UUID]
      schedule <- GenerationScheduleUseCase
        .findById(uuid)
        .toRight(WorldRegenerationException.ScheduleIsNotFound)
      isSuccessful = GenerationScheduleUseCase.remove(schedule.id)
    } yield
      if (isSuccessful)
        Right(
          context
            .sender
            .sendMessage(s"${ChatColor.GREEN}指定されたスケジュール(id: ${uuid.toString})の削除に成功しました")
        )
      else Left(CommandException.CommandExecutionFailed)
}
