package click.seichi.regenerateworld.presenter.command

import click.seichi.regenerateworld.presenter.shared.contextualexecutor.executor.BranchedExecutor
import org.bukkit.command.TabExecutor

case object Command {
  val executor: TabExecutor =
    BranchedExecutor(
      Map("list" -> ListSchedules),
      Some(ListSchedules.help), // TODO: HelpのExecutorにする
      Some(ListSchedules.help)
    ).asTabExecutor
}
