package click.seichi.regenerateworld.events

import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class PreRegenerateWorldEvent(val worldName: String) : Event(), Cancellable {
    private var isCancelled = false

    companion object {
        private val handlerList = HandlerList()

        @JvmStatic
        fun getHandlerList() = handlerList
    }

    override fun getHandlers() = handlerList

    override fun isCancelled() = isCancelled

    override fun setCancelled(isCancelled: Boolean) { this.isCancelled = isCancelled }
}