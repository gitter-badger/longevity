package longevity.exceptions.subdomain.ptype

import emblem.TypeKey
import longevity.subdomain.persistent.Persistent

/** thrown on attempt to create a property with a type that longevity doesn't
 * support yet. these include and properties that are not exactly-one valued -
 * no options, sets or lists anywhere in the path. these also include paths that
 * end on an entity, and not a basic type, shorthand or assoc.
 */
class UnsupportedPropTypeException[P <: Persistent : TypeKey, U : TypeKey](val path: String)
extends PropException(
  s"longevity doesn't currently support properties with type `${implicitly[TypeKey[U]].name}`, such as " +
  s"`$path` in `${implicitly[TypeKey[P]].name}`")

