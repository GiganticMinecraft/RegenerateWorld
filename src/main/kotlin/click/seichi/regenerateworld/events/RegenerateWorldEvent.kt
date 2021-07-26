package click.seichi.regenerateworld.events

import org.bukkit.event.HandlerList
import org.bukkit.event.Event

class RegenerateWorldEvent(val worldName: String) : Event() {
    companion object {
        private val handlerList = HandlerList()

        @JvmStatic
        fun getHandlerList() = handlerList
    }

    override fun getHandlers() = handlerList
}