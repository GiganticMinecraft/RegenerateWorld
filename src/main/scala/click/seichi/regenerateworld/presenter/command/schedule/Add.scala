package click.seichi.regenerateworld.presenter.command.schedule

import click.seichi.regenerateworld.domain.model.{Interval, SeedPattern}
import click.seichi.regenerateworld.presenter.GenerationScheduleUseCase
import click.seichi.regenerateworld.presenter.runnable.RegenerationTask
import click.seichi.regenerateworld.presenter.shared.contextualexecutor.executor.EchoExecutor
import click.seichi.regenerateworld.presenter.shared.contextualexecutor.{
  CommandContext,
  ContextualExecutor,
  Parsers,
  Result
}
import click.seichi.regenerateworld.presenter.shared.exception.WorldRegenerationException
import org.bukkit.ChatColor

case object Add extends ContextualExecutor {
  val help: EchoExecutor = EchoExecutor(
    List("/rw schedule add <再生成間隔> <シード値の設定> <新しいシード値> <ワールド名（半角スペース区切り）>", "    スケジュールを追加します。")
  )

  override def executionWith(context: CommandContext): Result[Unit] = {
    import click.seichi.regenerateworld.presenter.RegenerateWorld._

    for {
      args <- parseArguments(List(Parsers.interval, Parsers.seedPattern))(context)
      interval = args.parsed.head.asInstanceOf[Interval]
      seedPattern = args.parsed(1).asInstanceOf[SeedPattern]
      seedValue = args.yetToBeParsed.headOption
      _ <-
        if (seedPattern.seedValueIsRequiredExplicitly && seedValue.isEmpty) {
          Left(WorldRegenerationException.SeedIsRequired)
        } else {
          Right(())
        }
      worlds = if (seedValue.isDefined) args.yetToBeParsed.tail else args.yetToBeParsed
      uuid = GenerationScheduleUseCase.add(interval, seedPattern, worlds.toSet)
      newSchedule <- GenerationScheduleUseCase
        .findById(uuid)
        .toRight(WorldRegenerationException.ScheduleIsNotFound)
      task = RegenerationTask.runAtNextDate(newSchedule)
      _ = regenerationTasks.put(newSchedule.id, task)
    } yield context
      .sender
      .sendMessage(s"${ChatColor.GREEN}スケジュールの追加に成功しました(id: ${uuid.toString})")
  }
}
