package click.seichi.regenerateworld.events

import org.bukkit.event.HandlerList
import org.bukkit.event.Cancellable
import org.bukkit.event.Event

class RegenerateWorldEvent(private val worldName: String) : Event() {
    companion object {
        val handlerList = HandlerList()
    }

    override fun getHandlers() = handlerList
}