package click.seichi.regenerateworld.presenter.shared.contextualexecutor

import click.seichi.regenerateworld.presenter.shared.exception.LangException
import org.bukkit.command.{Command, CommandSender, TabExecutor}

import scala.jdk.CollectionConverters._
import java.util

trait ContextualExecutor {
  def executionWith(context: CommandContext): Result[Unit]
  def tabCandidatesFor(context: CommandContext): List[String] = Nil
}

object ContextualExecutor {
  implicit class ContextualTabExecutor(val contextualExecutor: ContextualExecutor) {
    def asTabExecutor: TabExecutor = new TabExecutor {
      override def onCommand(commandSender: CommandSender, command: Command, alias: String, args: Array[String]): Boolean = {
        val context = CommandContext(commandSender, command, args.toList)
        val errMessage = s"The exception has thrown while executing ${command.getName} command in RegenerateWorld"

        contextualExecutor.executionWith(context) match {
          case Left(err: LangException) =>
            println(errMessage)
            err.throwable.printStackTrace()
            Seq(errMessage, "Please contact to the server administrators", s"details: ${err.description}")
              .foreach(commandSender.sendMessage)
          case Left(err) =>
            Seq(errMessage, err.description).foreach(commandSender.sendMessage)
          case Right(_) =>
        }

        true
      }

      override def onTabComplete(commandSender: CommandSender, command: Command, alias: String, args: Array[String]): util.List[String] = {
        val context = CommandContext(commandSender, command, args.toList)

        contextualExecutor.tabCandidatesFor(context)
      }.asJava
    }
  }
}