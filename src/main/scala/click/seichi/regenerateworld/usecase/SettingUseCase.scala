package click.seichi.regenerateworld.usecase

import click.seichi.regenerateworld.domain.model.Setting
import click.seichi.regenerateworld.usecase.usetraits.UseSetting

trait SettingUseCase extends UseSetting {
  def get(): Option[Setting] = settingRepository.get()
}
