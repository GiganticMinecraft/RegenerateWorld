package click.seichi.regenerateworld.presenter

import org.bukkit.command.{Command, CommandSender, TabExecutor}

import java.util
import java.util.Collections.emptyList

object Command extends TabExecutor {
  override def onCommand(commandSender: CommandSender, command: Command, s: String, strings: Array[String]): Boolean = {
    val list = GenerationScheduleUseCase.list()
    println(list)

    true
  }

  override def onTabComplete(commandSender: CommandSender, command: Command, s: String, strings: Array[String]): util.List[String] = emptyList()
}
