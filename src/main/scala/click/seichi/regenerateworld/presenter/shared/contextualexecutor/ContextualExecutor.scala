package click.seichi.regenerateworld.presenter.shared.contextualexecutor

import org.bukkit.command.{Command, CommandSender, TabExecutor}

import scala.jdk.CollectionConverters._
import java.util

trait ContextualExecutor {
  def executionWith(context: CommandContext): Either[Throwable, Unit]
  def tabCandidatesFor(context: CommandContext): List[String] = Nil
}

object ContextualExecutor {
  implicit class ContextualTabExecutor(val contextualExecutor: ContextualExecutor) {
    def asTabExecutor(): TabExecutor = new TabExecutor {
      override def onCommand(commandSender: CommandSender, command: Command, alias: String, args: Array[String]): Boolean = {
        true
      }

      override def onTabComplete(commandSender: CommandSender, command: Command, alias: String, args: Array[String]): util.List[String] = {
        val context = CommandContext(commandSender, command, args.toList)

        contextualExecutor.tabCandidatesFor(context)
      }.asJava
    }
  }
}