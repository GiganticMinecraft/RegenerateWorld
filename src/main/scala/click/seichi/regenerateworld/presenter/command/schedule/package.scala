package click.seichi.regenerateworld.presenter.command

import click.seichi.regenerateworld.presenter.shared.contextualexecutor.executor.{
  BranchedExecutor,
  EchoExecutor
}

package object schedule {
  val help: EchoExecutor = EchoExecutor(
    List("/rw schedule", "    再生成スケジュールを追加・変更・削除します。詳細は「/rw schedule help」を実行してください。")
  )

  val executor: BranchedExecutor =
    BranchedExecutor(
      Map("remove" -> Remove, "add" -> Add, "edit" -> Edit, "help" -> Help),
      Some(Help),
      Some(Help)
    )
}
