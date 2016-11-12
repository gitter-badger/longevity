package longevity.integration.subdomain.shorthandLists

import longevity.subdomain.PType

case class ShorthandLists(
  id: ShorthandListsId,
  boolean: List[BooleanShorthand],
  char: List[CharShorthand],
  double: List[DoubleShorthand],
  float: List[FloatShorthand],
  int: List[IntShorthand],
  long: List[LongShorthand],
  string: List[StringShorthand],
  dateTime: List[DateTimeShorthand])

object ShorthandLists extends PType[ShorthandLists] {
  object props {
    val id = prop[ShorthandListsId]("id")
  }
  object keys {
    val id = key[ShorthandListsId](props.id)
  }
}
