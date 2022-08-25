package click.seichi.regenerateworld.presenter.event

import click.seichi.regenerateworld.presenter.event.RegenWorldEvent.handlerList
import org.bukkit.event.{Event, HandlerList}

import scala.annotation.unused

case class RegenWorldEvent(worldName: String) extends Event {
  override def getHandlers: HandlerList = handlerList
}

object RegenWorldEvent {
  private val handlerList = new HandlerList()

  @unused
  def getHandlerList: HandlerList = handlerList

}
