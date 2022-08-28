package click.seichi.regenerateworld.domain

import click.seichi.regenerateworld.domain.model.SeedPattern
import org.scalatest.diagrams.Diagrams
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.Inspectors.forAll

class SeedPatternSpec extends AnyFlatSpec with Diagrams {
  "SeedPattern#fromString" should "parse variant names" in {
    forAll(SeedPattern.namesToValuesMap) {
      case (entryName, pattern) => assert(SeedPattern.fromString(entryName).contains(pattern))
    }
  }

  it should "parse variant names when upper string cases" in {
    forAll(SeedPattern.upperCaseNameValuesToMap) {
      case (entryName, pattern) => assert(SeedPattern.fromString(entryName).contains(pattern))
    }
  }

  it should "parse variant names when lower string cases" in {
    forAll(SeedPattern.lowerCaseNamesToValuesMap) {
      case (entryName, pattern) => assert(SeedPattern.fromString(entryName).contains(pattern))
    }
  }

  it should "parse variant names unsuccessfully when different string cases" in {
    forAll(Set("CURRENT_SEED", "current-seed")) { str =>
      assert(SeedPattern.fromString(str).isEmpty)
    }
  }

  it should "parse variant names unsuccessfully when different words" in {
    forAll(Set("hello", "myName", "is-Lucky!!!")) { str =>
      assert(SeedPattern.fromString(str).isEmpty)
    }
  }
}
