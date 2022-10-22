package click.seichi.regenerateworld.presenter.command.schedule

import click.seichi.regenerateworld.domain.model.{Interval, SeedPattern}
import click.seichi.regenerateworld.presenter.GenerationScheduleUseCase
import click.seichi.regenerateworld.presenter.RegenerateWorld._
import click.seichi.regenerateworld.presenter.runnable.RegenerationTask
import click.seichi.regenerateworld.presenter.shared.contextualexecutor.executor.EchoExecutor
import click.seichi.regenerateworld.presenter.shared.contextualexecutor.{
  CommandContext,
  ContextualExecutor,
  Parsers,
  Result
}
import click.seichi.regenerateworld.presenter.shared.exception.{
  CommandException,
  ParseException,
  WorldRegenerationException
}
import enumeratum.{Enum, EnumEntry}
import org.bukkit.ChatColor

import java.util.UUID
import scala.util.Try

sealed abstract class EditKey() extends EnumEntry

private object EditKey extends Enum[EditKey] {
  override val values: IndexedSeq[EditKey] = findValues
  def fromString(str: String): Option[EditKey] =
    values.find(_.entryName.toLowerCase == str.toLowerCase)

  case object Interval extends EditKey

  case object SeedPattern extends EditKey

  case object Worlds extends EditKey
}

case object Edit extends ContextualExecutor {
  val help: EchoExecutor = EchoExecutor(
    List("/rw schedule edit <スケジュールID> <Key> <Value>", "    スケジュールを編集します。")
  )

  override def executionWith(context: CommandContext): Result[Unit] = {
    for {
      id <- context.args.headOption.toRight(CommandException.ArgIsInsufficient)
      id <- Parsers.uuid(id).map(_.asInstanceOf[UUID])
      _ <- GenerationScheduleUseCase
        .findById(id)
        .toRight(WorldRegenerationException.ScheduleIsNotFound)
      key <- Try(context.args(1)).toOption.toRight(CommandException.ArgIsInsufficient)
      key <- EditKey
        .fromString(key)
        .toRight(ParseException.MustBeIncludedIn(EditKey.values.map(_.toString)))
      value <- Try(context.args(2)).toOption.toRight(CommandException.ArgIsInsufficient)
    } yield {
      key match {
        case EditKey.SeedPattern =>
          val pattern = Parsers.seedPattern(value).map(_.asInstanceOf[SeedPattern]) match {
            case Right(pattern) => pattern
            case Left(e)        => return Left(e)
          }

          GenerationScheduleUseCase.changeSeedPattern(id, pattern)
        case EditKey.Interval =>
          val interval = Parsers.interval(value).map(_.asInstanceOf[Interval]) match {
            case Right(interval) => interval
            case Left(e)         => return Left(e)
          }

          GenerationScheduleUseCase.changeInterval(id, interval)
        case EditKey.Worlds =>
          val worlds = context.args.splitAt(1)._2

          GenerationScheduleUseCase.setWorlds(id, worlds.toSet)
      }

      regenerationTasks(id).cancel()
      val newSchedule = GenerationScheduleUseCase.findById(id).get
      val newTask = RegenerationTask.runAtNextDate(newSchedule)
      regenerationTasks.put(id, newTask)

      context.sender.sendMessage(s"${ChatColor.GREEN}スケジュールの編集に成功しました")
    }
  }
}
