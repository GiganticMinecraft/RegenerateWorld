package click.seichi.regenerateworld.presenter.shared

package object contextualexecutor {
 type Result[T] = Either[Throwable, T]
}