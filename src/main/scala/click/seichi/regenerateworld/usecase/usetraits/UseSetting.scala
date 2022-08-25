package click.seichi.regenerateworld.usecase.usetraits

import click.seichi.regenerateworld.domain.repository.SettingRepository

trait UseSetting {
  def settingRepository: SettingRepository
}
