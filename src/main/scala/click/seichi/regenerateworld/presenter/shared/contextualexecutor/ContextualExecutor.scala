package click.seichi.regenerateworld.presenter.shared.contextualexecutor

import click.seichi.regenerateworld.presenter.shared.exception.{CommandException, LangException}
import org.bukkit.ChatColor
import org.bukkit.command.{Command, CommandSender, TabExecutor}

import scala.jdk.CollectionConverters._
import java.util

trait ContextualExecutor {
  def executionWith(context: CommandContext): Result[Unit]
  def tabCandidatesFor(context: CommandContext): List[String] = Nil
  final def parseArguments(parsers: List[SingleArgumentParser]): CommandArgumentsParser = {
    context =>
      @scala.annotation.tailrec
      def parse(
        remainingParsers: List[SingleArgumentParser],
        remainingArgs: List[String],
        reverseAccumulator: List[Any] = List()
      ): Result[PartiallyParsedArgs] = {
        // parserがなければparseすべき引数はないものとしてreturn
        val (parserHead, parserTail) = remainingParsers match {
          case ::(head, next) => (head, next)
          case Nil =>
            return Right(PartiallyParsedArgs(reverseAccumulator.reverse, remainingArgs))
        }

        // parserは指定されているので、引数がないということは不足しているということ
        val (argHead, argTail) = remainingArgs match {
          case ::(head, next) => (head, next)
          case Nil            => return Left(CommandException.ArgIsInsufficient)
        }

        // parserがエラーを返してきた場合は次のparser・引数の処理を行わない
        parserHead(argHead) match {
          case Left(err)        => Left(err)
          case Right(parsedArg) => parse(parserTail, argTail, parsedArg :: reverseAccumulator)
        }
      }

      parse(parsers, context.args)
  }
}

object ContextualExecutor {
  implicit class ContextualTabExecutor(val contextualExecutor: ContextualExecutor) {
    def asTabExecutor: TabExecutor = new TabExecutor {
      private def addChatColor(str: String): String = s"${ChatColor.RED}s$str"

      override def onCommand(
        commandSender: CommandSender,
        command: Command,
        alias: String,
        args: Array[String]
      ): Boolean = {
        val context = CommandContext(commandSender, command, args.toList)
        val errMessage =
          s"The exception has thrown while executing ${command.getName} command in RegenerateWorld"

        contextualExecutor.executionWith(context) match {
          case Left(err: LangException) =>
            println(errMessage)
            err.throwable.printStackTrace()
            Seq(
              errMessage,
              "Please contact to the server administrators",
              s"details: ${err.description}"
            ).map(addChatColor).foreach(commandSender.sendMessage)
          case Left(err) =>
            Seq(errMessage, err.description)
              .map(addChatColor)
              .foreach(commandSender.sendMessage)
          case Right(_) =>
        }

        true
      }

      override def onTabComplete(
        commandSender: CommandSender,
        command: Command,
        alias: String,
        args: Array[String]
      ): util.List[String] = {
        val context = CommandContext(commandSender, command, args.toList)

        contextualExecutor.tabCandidatesFor(context)
      }.asJava
    }
  }
}
