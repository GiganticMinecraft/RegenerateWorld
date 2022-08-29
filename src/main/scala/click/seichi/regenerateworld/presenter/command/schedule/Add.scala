package click.seichi.regenerateworld.presenter.command.schedule

import click.seichi.regenerateworld.domain.model.{GenerationSchedule, Interval, SeedPattern}
import click.seichi.regenerateworld.presenter.GenerationScheduleUseCase
import click.seichi.regenerateworld.presenter.shared.contextualexecutor.executor.EchoExecutor
import click.seichi.regenerateworld.presenter.shared.contextualexecutor.{
  CommandContext,
  ContextualExecutor,
  Result,
  parser
}

import java.time.ZonedDateTime
import java.util.UUID

case object Add extends ContextualExecutor {
  val help: EchoExecutor = EchoExecutor(
    List("/rw schedule add <再生成間隔> <シード値の設定> <ワールド名（半角スペース区切り）>", "    スケジュールを追加します。")
  )

  override def executionWith(context: CommandContext): Result[Unit] = {
    // TODO: UseCaseに#addを実装する
    for {
      args <- parseArguments(List(parser.interval, parser.seedPattern))(context)
      interval = args.parsed.head.asInstanceOf[Interval]
      seedPattern = args.parsed(1).asInstanceOf[SeedPattern]
      worlds = args.yetToBeParsed.toSet
      schedule = GenerationSchedule(
        UUID.randomUUID(),
        ZonedDateTime.now(),
        interval,
        seedPattern,
        worlds
      )
    } yield GenerationScheduleUseCase.generationScheduleRepository.save(schedule)
  }
}
