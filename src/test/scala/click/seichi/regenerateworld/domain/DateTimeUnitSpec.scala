package click.seichi.regenerateworld.domain

import click.seichi.regenerateworld.domain.model.DateTimeUnit
import org.scalatest.Inspectors.forAll
import org.scalatest.diagrams.Diagrams
import org.scalatest.flatspec.AnyFlatSpec

class DateTimeUnitSpec extends AnyFlatSpec with Diagrams {
  "DateTimeUnit#fromString" should "parse variant names" in {
    forAll(DateTimeUnit.namesToValuesMap) {
      case (entryName, unit) => assert(DateTimeUnit.fromString(entryName).contains(unit))
    }
  }

  it should "parse variant names successfully when upper string cases" in {
    forAll(DateTimeUnit.upperCaseNameValuesToMap) {
      case (entryName, unit) => assert(DateTimeUnit.fromString(entryName).contains(unit))
    }
  }

  it should "parse variant names successfully when lower string cases" in {
    forAll(DateTimeUnit.lowerCaseNamesToValuesMap) {
      case (entryName, unit) => assert(DateTimeUnit.fromString(entryName).contains(unit))
    }
  }

  it should "parse variant names unsuccessfully when different string cases" in {
    forAll(Set("day_of_month", "day-of-month")) { str =>
      assert(DateTimeUnit.fromString(str).isEmpty)
    }
  }

  it should "parse variant names unsuccessfully when different words" in {
    forAll(Set("hello", "myName", "is-Lucky!!!")) { str =>
      assert(DateTimeUnit.fromString(str).isEmpty)
    }
  }

  "DateTimeUnit#fromAliasString" should "parse variant names successfully when upper string cases" in {
    val upperCaseAliasValuesToMap =
      DateTimeUnit.values.map(unit => unit.alias.toUpperCase -> unit)

    forAll(upperCaseAliasValuesToMap) {
      case (alias, unit) => assert(DateTimeUnit.fromAliasString(alias).contains(unit))
    }
  }

  it should "parse variant names successfully when lower string cases" in {
    val lowerCaseAliasValuesToMap =
      DateTimeUnit.values.map(unit => unit.alias.toLowerCase -> unit)

    forAll(lowerCaseAliasValuesToMap) {
      case (alias, unit) => assert(DateTimeUnit.fromAliasString(alias).contains(unit))
    }
  }

  it should "parse variant names unsuccessfully when different words" in {
    forAll(Set("hello", "myName", "is-Lucky!!!")) { str =>
      assert(DateTimeUnit.fromAliasString(str).isEmpty)
    }
  }

}
