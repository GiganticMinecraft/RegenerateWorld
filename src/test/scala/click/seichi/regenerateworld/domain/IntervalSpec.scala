package click.seichi.regenerateworld.domain

import click.seichi.regenerateworld.domain.model.{DateTimeUnit, Interval}
import org.scalatest.diagrams.Diagrams
import org.scalatest.flatspec.AnyFlatSpec

class IntervalSpec extends AnyFlatSpec with Diagrams {
  private val dateTimeUnit = DateTimeUnit.Hour
  private val positiveNumber = 10
  private val negativeNumber = -10

  "Interval" should "be generated successfully with positive value" in {
    require(positiveNumber > 0)
    val interval = Interval(dateTimeUnit, positiveNumber)
    assert(interval.unit == dateTimeUnit)
    assert(interval.value == positiveNumber)
  }

  it should "not be generated successfully with negative value" in {
    assertThrows[IllegalArgumentException] {
      require(negativeNumber < 0)
      Interval(DateTimeUnit.Hour, -1)
    }
  }

  it should "not be generated successfully with 0 value" in {
    assertThrows[IllegalArgumentException] {
      Interval(DateTimeUnit.Hour, 0)
    }
  }
}
