package click.seichi.regenerateworld.domain

import click.seichi.regenerateworld.domain.model.SeedPattern
import org.scalatest.diagrams.Diagrams
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.Inspectors.forAll

class SeedPatternSpec extends AnyFlatSpec with Diagrams {
  "SeedPattern#fromString" should "return Some" in {
    val legalSeedPatternStringsMap: Map[String, SeedPattern] =
      Map(
        "CurrentSeed" -> SeedPattern.CurrentSeed,
        "NewSeed" -> SeedPattern.NewSeed,
        "RandomNewSeed" -> SeedPattern.RandomNewSeed
      )

    forAll(legalSeedPatternStringsMap) {
      case (str, pattern) => assert(SeedPattern.fromString(str).contains(pattern))
    }
  }

  it should "return None when different string cases" in {
    forAll(Set("currentSeed", "CURRENT_SEED", "current-seed")) { str =>
      assert(SeedPattern.fromString(str).isEmpty)
    }
  }

  it should "return None when different words" in {
    forAll(Set("hello", "myName", "is-Lucky!!!")) { str =>
      assert(SeedPattern.fromString(str).isEmpty)
    }
  }
}
