package click.seichi.regenerateworld.presenter.event

import click.seichi.regenerateworld.presenter.event.PreRegenWorldEvent.handlerList
import org.bukkit.event.{Cancellable, Event, HandlerList}

import scala.annotation.unused

case class PreRegenWorldEvent(worldName: String) extends Event with Cancellable {
  // 本当は`isCancelled`という名前にすべきだが、isCancelledを関数でoverrideしなければいけない
  private var cancelStatus = false

  override def getHandlers: HandlerList = handlerList

  override def isCancelled: Boolean = cancelStatus

  override def setCancelled(newStatus: Boolean): Unit = cancelStatus = newStatus
}

object PreRegenWorldEvent {
  private val handlerList = new HandlerList()

  @unused
  def getHandlerList: HandlerList = handlerList
}
