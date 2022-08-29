package click.seichi.regenerateworld.presenter.command.schedule

import click.seichi.regenerateworld.domain.model.{Interval, SeedPattern}
import click.seichi.regenerateworld.presenter.GenerationScheduleUseCase
import click.seichi.regenerateworld.presenter.shared.contextualexecutor.executor.EchoExecutor
import click.seichi.regenerateworld.presenter.shared.contextualexecutor.{
  CommandContext,
  ContextualExecutor,
  Parsers,
  Result
}
import org.bukkit.ChatColor

case object Add extends ContextualExecutor {
  val help: EchoExecutor = EchoExecutor(
    List("/rw schedule add <再生成間隔> <シード値の設定> <ワールド名（半角スペース区切り）>", "    スケジュールを追加します。")
  )

  override def executionWith(context: CommandContext): Result[Unit] = {
    for {
      args <- parseArguments(List(Parsers.interval, Parsers.seedPattern))(context)
      interval = args.parsed.head.asInstanceOf[Interval]
      seedPattern = args.parsed(1).asInstanceOf[SeedPattern]
      worlds = args.yetToBeParsed.toSet
      uuid = GenerationScheduleUseCase.add(interval, seedPattern, worlds)
    } yield context
      .sender
      .sendMessage(s"${ChatColor.GREEN}スケジュールの追加に成功しました(id: ${uuid.toString})")
  }
}
