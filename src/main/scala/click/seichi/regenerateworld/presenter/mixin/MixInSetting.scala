package click.seichi.regenerateworld.presenter.mixin

import click.seichi.regenerateworld.RegenerateWorld.INSTANCE
import click.seichi.regenerateworld.domain.repository.SettingRepository
import click.seichi.regenerateworld.infra.SettingRepositoryImpl
import click.seichi.regenerateworld.usecase.usetraits.UseSetting

trait MixInSetting extends UseSetting {
  override def settingRepository: SettingRepository = SettingRepositoryImpl(INSTANCE.getConfig)
}