package click.seichi.regenerateworld

import click.seichi.regenerateworld.infra.model.GenerationScheduleConfig
import click.seichi.regenerateworld.presenter.Command
import org.bukkit.configuration.serialization.ConfigurationSerialization
import org.bukkit.plugin.java.JavaPlugin

class RegenerateWorld extends JavaPlugin {
  override def onEnable(): Unit = {
    RegenerateWorld.INSTANCE = this

    ConfigurationSerialization.registerClass(classOf[GenerationScheduleConfig])

    getCommand("regenworld").setExecutor(Command)
  }
}

object RegenerateWorld {
  var INSTANCE: RegenerateWorld = _
}