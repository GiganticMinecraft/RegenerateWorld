package click.seichi.regenerateworld.domain.model

import enumeratum.{Enum, EnumEntry}

sealed trait SeedPattern extends EnumEntry

object SeedPattern extends Enum[SeedPattern] {
  override val values: IndexedSeq[SeedPattern] = findValues
  def fromString(str: String): Option[SeedPattern] = values.find(_.entryName == str)

  /**
   * 現在のSeed値をそのまま利用する。
   */
  case object CURRENT_SEED extends SeedPattern

  /**
   * Seed値を新しく生成し、その値は自分で指定したものを用いる。
   */
  case object NEW_SEED extends SeedPattern

  /**
   * Seed値を新しく生成し、その値はランダムで生成する。
   */
  case object RANDOM_NEW_SEED extends SeedPattern
}
