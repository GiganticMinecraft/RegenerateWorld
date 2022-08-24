package click.seichi.regenerateworld.domain.repository

import click.seichi.regenerateworld.domain.model.Setting

trait SettingRepository {
  def get(): Option[Setting]
}
