package click.seichi.regenerateworld.presenter.command

import click.seichi.regenerateworld.presenter.shared.contextualexecutor.executor.{
  BranchedExecutor,
  EchoExecutor
}

package object regen {
  val help: EchoExecutor = EchoExecutor(
    List("/rw regen", "    ワールドの再生成を行います。詳細は「/rw regen help」を実行してください。")
  )

  val executor: BranchedExecutor = BranchedExecutor(
    Map("new" -> New, "schedule" -> Schedule, "help" -> Help),
    Some(Help),
    Some(Help)
  )
}
