package click.seichi.regenerateworld.presenter.shared.contextualexecutor

import org.bukkit.command.{Command, CommandSender}

case class CommandContext(sender: CommandSender, command: Command, args: List[String])
