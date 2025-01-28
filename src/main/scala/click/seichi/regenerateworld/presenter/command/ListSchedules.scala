package click.seichi.regenerateworld.presenter.command

import click.seichi.regenerateworld.presenter.GenerationScheduleUseCase
import click.seichi.regenerateworld.presenter.shared.contextualexecutor.executor.EchoExecutor
import click.seichi.regenerateworld.presenter.shared.contextualexecutor.{
  CommandContext,
  ContextualExecutor,
  Result
}
import org.bukkit.ChatColor

object ListSchedules extends ContextualExecutor {
  val help: EchoExecutor = EchoExecutor(List("/rw list", "    有効な再生成予定の一覧を表示します。"))

  override def executionWith(context: CommandContext): Result[Unit] = {
    val formattedSchedules = for {
      schedule <- GenerationScheduleUseCase.list()
      formattedInterval = s"${schedule.interval.value}${schedule.interval.unit.alias}"
      formattedSchedule =
        s"${schedule.id}: ${schedule.worlds.mkString(", ")} | $formattedInterval | ${schedule.seedPattern.entryName} | ${schedule.nextDateTime}"
    } yield formattedSchedule

    val messages =
      if (formattedSchedules.isEmpty) Seq(s"${ChatColor.RED}再生成の設定はありません。")
      else
        Seq("RegenerateWorld - ScheduleLists", "UUID: ワールド | 再生成間隔 | シード値の設定 | 次回再生成予定日時")
          .map { str => s"${ChatColor.WHITE}$str" }
          .concat(formattedSchedules)

    Right(messages.foreach(context.sender.sendMessage))
  }
}
