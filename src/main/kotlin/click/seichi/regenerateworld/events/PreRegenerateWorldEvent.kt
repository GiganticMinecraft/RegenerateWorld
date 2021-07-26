package click.seichi.regenerateworld.events

import org.bukkit.event.HandlerList
import org.bukkit.event.Cancellable
import org.bukkit.event.Event

class PreRegenerateWorldEvent(private val worldName: String) : Event(), Cancellable {
    private var isCancelled = false

    companion object {
        val handlerList = HandlerList()
    }

    override fun getHandlers(): HandlerList {
        return handlerList
    }

    override fun isCancelled(): Boolean {
        return isCancelled
    }

    override fun setCancelled(isCancelled: Boolean) {
        this.isCancelled = isCancelled
    }
}