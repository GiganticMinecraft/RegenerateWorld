package click.seichi.regenerateworld.presenter.shared.contextualexecutor.executor

import click.seichi.regenerateworld.presenter.shared.contextualexecutor.{CommandContext, ContextualExecutor, Result}

case class BranchedExecutor(
  branches: Map[String, ContextualExecutor],
  whenArgInsufficient: Option[ContextualExecutor] = Some(PrintUsageExecutor),
  whenBranchNotFound: Option[ContextualExecutor] = Some(PrintUsageExecutor)
) extends ContextualExecutor {
  override def executionWith(context: CommandContext): Result[Unit] = {
    def executeOptionally(executor: Option[ContextualExecutor]): Result[Unit] =
      executor match {
        case Some(executor) => executor.executionWith(context)
        case None => Right(())
      }

    val (firstArg, remaining) = context.args match {
      case head :: tail => (head, tail)
      case Nil => return executeOptionally(whenArgInsufficient)
    }
    val branch = branches.getOrElse(firstArg, return executeOptionally(whenBranchNotFound))
    val nextContext = context.copy(args = remaining)

    branch.executionWith(nextContext)
  }

  override def tabCandidatesFor(context: CommandContext): List[String] = {
    context.args match {
      case head :: tail =>
        val childExecutor = branches.getOrElse(head, return Nil)

        childExecutor.tabCandidatesFor(context.copy(args = tail))
      case Nil => branches.keys.toArray.sorted.toList
    }
  }
}