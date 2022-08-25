package click.seichi.regenerateworld.presenter.shared

import click.seichi.regenerateworld.presenter.shared.exception.OriginalException

package object contextualexecutor {
  type Result[T] = Either[OriginalException, T]
}
