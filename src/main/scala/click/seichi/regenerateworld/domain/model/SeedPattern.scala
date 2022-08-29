package click.seichi.regenerateworld.domain.model

import enumeratum.{Enum, EnumEntry}

sealed abstract class SeedPattern(val isNewSeed: Boolean, val isRandomSeed: Boolean)
    extends EnumEntry

object SeedPattern extends Enum[SeedPattern] {
  override val values: IndexedSeq[SeedPattern] = findValues
  def fromString(str: String): Option[SeedPattern] =
    values.find(_.entryName.toLowerCase == str.toLowerCase)

  /**
   * 現在のSeed値をそのまま利用する。
   */
  case object CurrentSeed extends SeedPattern(false, false)

  /**
   * Seed値を新しく生成し、その値は自分で指定したものを用いる。
   */
  case object NewSeed extends SeedPattern(true, false)

  /**
   * Seed値を新しく生成し、その値はランダムで生成する。
   */
  case object RandomNewSeed extends SeedPattern(true, true)
}
