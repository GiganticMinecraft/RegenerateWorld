package click.seichi.regenerateworld.presenter.command

import click.seichi.regenerateworld.presenter.GenerationScheduleUseCase
import click.seichi.regenerateworld.presenter.shared.contextualexecutor.executor.EchoExecutor
import click.seichi.regenerateworld.presenter.shared.contextualexecutor.{
  CommandContext,
  ContextualExecutor,
  Result
}
import click.seichi.regenerateworld.presenter.shared.exception.CommandException
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object ListSchedules extends ContextualExecutor {
  val help: EchoExecutor = EchoExecutor(List("/rw list", "有効な再生成予定の一覧を表示します。"))

  def executionWith(context: CommandContext): Result[Unit] = {
    val sender = context.sender match {
      case p: Player => p
      case _         => return Left(CommandException.OnlyPlayerCanExecute)
    }

    val formattedSchedules = GenerationScheduleUseCase.list().map { schedule =>
      s"${schedule.id}: ${schedule.worlds.toString} | ${schedule.interval.toString} | ${schedule
          .seedPattern
          .entryName} | ${schedule.nextDateTime.toString}"
    }
    val message =
      if (formattedSchedules.isEmpty) s"${ChatColor.RED}再生成予定はありません。"
      else
        Set("-RegenerateWorld ScheduleLists-", "UUID: ワールド | 再生成間隔 | シード値の設定 | 次回再生成予定日時")
          .map { str => s"${ChatColor.WHITE}$str" }
          .concat(formattedSchedules)
          .mkString("¥n")

    sender.sendMessage(message)

    Right(())
  }
}
