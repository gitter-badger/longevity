package longevity.integration.subdomain.shorthandSets

import longevity.subdomain.PType

case class ShorthandSets(
  id: ShorthandSetsId,
  boolean: Set[BooleanShorthand],
  char: Set[CharShorthand],
  double: Set[DoubleShorthand],
  float: Set[FloatShorthand],
  int: Set[IntShorthand],
  long: Set[LongShorthand],
  string: Set[StringShorthand],
  dateTime: Set[DateTimeShorthand])

object ShorthandSets extends PType[ShorthandSets] {
  object props {
    val id = prop[ShorthandSetsId]("id")
  }
  object keys {
    val id = key[ShorthandSetsId](props.id)
  }
}
