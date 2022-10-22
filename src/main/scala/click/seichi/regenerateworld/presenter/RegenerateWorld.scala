package click.seichi.regenerateworld.presenter

import click.seichi.regenerateworld.presenter.command.Command
import click.seichi.regenerateworld.presenter.listener.RegenWorldListener
import org.bukkit.plugin.java.JavaPlugin

class RegenerateWorld extends JavaPlugin {
  override def onEnable(): Unit = {
    RegenerateWorld.INSTANCE = this

    saveDefaultConfig()

    getCommand("regenworld").setExecutor(Command.executor)
    getServer.getPluginManager.registerEvents(RegenWorldListener, this)
    // TODO: register runnable per each schedule
  }
}

object RegenerateWorld {
  var instance: RegenerateWorld = _
}
