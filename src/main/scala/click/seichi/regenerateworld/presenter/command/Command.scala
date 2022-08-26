package click.seichi.regenerateworld.presenter.command

import click.seichi.regenerateworld.presenter.shared.contextualexecutor.executor.BranchedExecutor
import org.bukkit.command.TabExecutor

case object Command {
  val executor: TabExecutor =
    BranchedExecutor(
      Map(
        "list" -> ListSchedules,
        "run" -> run.executor,
        "schedule" -> schedule.executor,
        "help" -> Help
      ),
      Some(Help),
      Some(Help)
    ).asTabExecutor
}
