package click.seichi.regenerateworld.listener

import click.seichi.regenerateworld.Multiverse
import click.seichi.regenerateworld.events.PreRegenerateWorldEvent
import click.seichi.regenerateworld.events.RegenerateWorldEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object RegenerateWorldEventListener : Listener {
    @EventHandler
    fun onPreRegenerateWorld(event: PreRegenerateWorldEvent) {
        setOf("${event.worldName}の再生成を開始します。").map { "say $it" }.forEach {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), it)
        }

        Bukkit.getWorld(event.worldName).players.forEach {
            it.teleport(Multiverse.getSpawnWorld().spawnLocation)
        }
    }

    @EventHandler
    fun onRegenerateWorld(event: RegenerateWorldEvent) {
        setOf("${event.worldName}の再生成を終了しました。").map { "say $it" }.forEach {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), it)
        }
    }
}