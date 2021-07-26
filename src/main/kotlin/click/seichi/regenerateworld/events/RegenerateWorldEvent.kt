package click.seichi.regenerateworld.events

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class RegenerateWorldEvent(val worldName: String) : Event() {
    companion object {
        private val handlerList = HandlerList()

        @JvmStatic
        fun getHandlerList() = handlerList
    }

    override fun getHandlers() = handlerList
}