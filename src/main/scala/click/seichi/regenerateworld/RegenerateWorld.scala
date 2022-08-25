package click.seichi.regenerateworld

import click.seichi.regenerateworld.presenter.command.Command
import click.seichi.regenerateworld.presenter.listener.RegenWorldListener
import org.bukkit.plugin.java.JavaPlugin

class RegenerateWorld extends JavaPlugin {
  override def onEnable(): Unit = {
    RegenerateWorld.INSTANCE = this
    if (!getServer.getPluginManager.isPluginEnabled("Multiverse-Core")) {
      getServer.getLogger.severe("Since Multiverse-Core is not enabled, RegenerateWorld will be disabled.")
      getServer.getPluginManager.disablePlugin(this)
    }

    saveDefaultConfig()

    getCommand("regenworld").setExecutor(Command)
    getServer.getPluginManager.registerEvents(RegenWorldListener, this)
  }
}

object RegenerateWorld {
  var INSTANCE: RegenerateWorld = _
}