package click.seichi.regenerateworld.domain

import click.seichi.regenerateworld.domain.model.DateTimeUnit
import org.scalatest.Inspectors.forAll
import org.scalatest.diagrams.Diagrams
import org.scalatest.flatspec.AnyFlatSpec

class DateTimeUnitSpec extends AnyFlatSpec with Diagrams {
  "DateTimeUnit#fromString" should "parse variant names" in {
    val legalDateTimeUnitStringsMap: Map[String, DateTimeUnit] =
      Map(
        "Year" -> DateTimeUnit.Year,
        "Month" -> DateTimeUnit.Month,
        "Week" -> DateTimeUnit.Week,
        "DayOfMonth" -> DateTimeUnit.DayOfMonth,
        "Hour" -> DateTimeUnit.Hour,
        "Minute" -> DateTimeUnit.Minute
      )

    forAll(legalDateTimeUnitStringsMap) {
      case (str, unit) => assert(DateTimeUnit.fromString(str).contains(unit))
    }
  }

  it should "parse variant names unsuccessfully when different string cases" in {
    forAll(Set("year", "YEAR", "day-of-month")) { str =>
      assert(DateTimeUnit.fromString(str).isEmpty)
    }
  }

  it should "parse variant names unsuccessfully when different words" in {
    forAll(Set("hello", "myName", "is-Lucky!!!")) { str =>
      assert(DateTimeUnit.fromString(str).isEmpty)
    }
  }

}
