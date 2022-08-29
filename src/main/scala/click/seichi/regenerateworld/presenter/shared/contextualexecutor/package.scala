package click.seichi.regenerateworld.presenter.shared

import click.seichi.regenerateworld.presenter.shared.exception.OriginalException

package object contextualexecutor {
  type Result[T] = Either[OriginalException, T]

  type SingleArgumentParser = String => Result[Any]

  case class PartiallyParsedArgs(parsed: List[Any], yetToBeParsed: List[String])

  type CommandArgumentsParser = CommandContext => Result[PartiallyParsedArgs]

  implicit class Extensions[T](val result: Result[T]) {
    def onSuccess(fun: T => Unit): Result[T] = {
      result match {
        case Right(v) => fun(v)
        case _        =>
      }

      result
    }

    def onFailure(fun: OriginalException => Unit): Result[T] = {
      result match {
        case Left(v) => fun(v)
        case _       =>
      }

      result
    }
  }
}
