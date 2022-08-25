package click.seichi.regenerateworld.presenter.shared

package object exception {
  trait OriginalException {
    val description: String
  }

  case class LangException(throwable: Throwable) extends OriginalException {
    override val description: String = throwable.toString
  }

  implicit class Converter(val throwable: Throwable) {
    def toOriginalException: LangException = LangException(throwable)
  }
}
