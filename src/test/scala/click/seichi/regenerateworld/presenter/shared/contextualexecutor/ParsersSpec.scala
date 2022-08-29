package click.seichi.regenerateworld.presenter.shared.contextualexecutor

import click.seichi.regenerateworld.domain.model.DateTimeUnit
import click.seichi.regenerateworld.presenter.shared.contextualexecutor.Parsers
import org.scalatest.diagrams.Diagrams
import org.scalatest.flatspec.AnyFlatSpec

import java.util.UUID
import java.util.concurrent.ThreadLocalRandom

class ParsersSpec extends AnyFlatSpec with Diagrams {
  "uuid" should "parse string value successfully" in {
    for {
      uuid <- (1 to 3).map(_ => UUID.randomUUID())
      res = Parsers.uuid(uuid.toString)
    } yield assert(res.map(_.asInstanceOf[UUID]).contains(uuid))
  }

  it should "parse string value unsuccessfully" in {
    for {
      someWord <- Set("hello", "myName", "is-Lucky!!!")
      res = Parsers.uuid(someWord)
    } yield assert(res.isLeft)
  }

  "long" should "parse string value successfully" in {
    for {
      long <- (1 to 3).map(_ =>
        ThreadLocalRandom.current().nextLong(Long.MinValue, Long.MaxValue)
      )
      res = Parsers.long(long.toString)
    } yield assert(res.map(_.asInstanceOf[Long]).contains(long))
  }

  private def longValues(least: Long, upper: Long) =
    (1 to 3).map(_ => ThreadLocalRandom.current().nextLong(least, upper))
  private def positiveLongValues = longValues(1L, Long.MaxValue)
  private val zeroLongValue = 0L
  private def negativeLongValues = longValues(Long.MinValue, -1L)

  "naturalLongValue" should "parse string value successfully" in {
    for {
      long <- positiveLongValues
      res = Parsers.naturalLongValue(long.toString)
    } yield assert(res.map(_.asInstanceOf[Long]).contains(long))
  }

  it should "parse string value unsuccessfully when 0 and negative value" in {
    assert(Parsers.naturalLongValue(zeroLongValue.toString).isLeft)

    for {
      long <- negativeLongValues
      res = Parsers.naturalLongValue(long.toString)
    } yield assert(res.isLeft)
  }

  private val dateTimeUnitAliasValuesToMap = {
    val lowerCaseAliasValuesToMap =
      DateTimeUnit.values.map(unit => unit.alias.toLowerCase -> unit)
    val upperCaseAliasValuesToMap =
      DateTimeUnit.values.map(unit => unit.alias.toUpperCase -> unit)

    lowerCaseAliasValuesToMap ++ upperCaseAliasValuesToMap
  }
  private val dateTimeUnitNamesToValuesMap =
    DateTimeUnit.namesToValuesMap ++ DateTimeUnit.lowerCaseNamesToValuesMap ++ DateTimeUnit.upperCaseNameValuesToMap

  "dateTimeUnit" should "parse string value successfully from entryNames" in {
    for {
      (entryName, unit) <- dateTimeUnitNamesToValuesMap
      parsed = Parsers.dateTimeUnit(entryName)
    } yield assert(parsed.map(_.asInstanceOf[DateTimeUnit]).contains(unit))
  }

  it should "parse string value successfully from aliasNames" in {
    for {
      (entryName, unit) <- dateTimeUnitAliasValuesToMap
      parsed = Parsers.dateTimeUnit(entryName)
    } yield assert(parsed.map(_.asInstanceOf[DateTimeUnit]).contains(unit))
  }
}
