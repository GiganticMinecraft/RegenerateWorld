package click.seichi.regenerateworld.presenter.command

import click.seichi.regenerateworld.presenter.shared.contextualexecutor.executor.{
  BranchedExecutor,
  EchoExecutor
}
import org.bukkit.ChatColor

package object regen {
  val help: EchoExecutor = EchoExecutor(
    List("/rw regen", "    ワールドの再生成を行います。詳細は「/rw regen help」を実行してください。")
  )

  val executor: BranchedExecutor = BranchedExecutor(
    Map("new" -> New, "schedule" -> Schedule, "help" -> Help),
    Some(Help),
    Some(Help)
  )

  private[regen] def regenStartMessages(worldName: String): Set[String] =
    Set(s"${worldName}の再生成を開始します。", "この処理には時間がかかる可能性があります。").map { msg =>
      s"${ChatColor.GREEN}$msg"
    }

  private[regen] def regenSuccessfulMessage(worldName: String) =
    s"${ChatColor.GREEN}${worldName}ワールドの再生成が終了しました。"
}
