package click.seichi.regenerateworld

import click.seichi.regenerateworld.presenter.Command
import org.bukkit.plugin.java.JavaPlugin

class RegenerateWorld extends JavaPlugin {
  override def onEnable(): Unit = {
    RegenerateWorld.INSTANCE = this

    saveDefaultConfig()

    getCommand("regenworld").setExecutor(Command)
  }
}

object RegenerateWorld {
  var INSTANCE: RegenerateWorld = _
}