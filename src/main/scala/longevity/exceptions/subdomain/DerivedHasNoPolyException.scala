package longevity.exceptions.subdomain

/** thrown on attempt to construct a
 * [[longevity.subdomain.Subdomain subdomain]] with a
 * [[longevity.subdomain.embeddable.DerivedType derived type]] that does not
 * have a corresponding [[longevity.subdomain.embeddable.PolyType poly type]],
 * or a [[longevity.subdomain.ptype.DerivedPType derived persistent type]] that
 * does not have a corresponding [[longevity.subdomain.ptype.PolyPType
 * poly persistent type]]
 */
class DerivedHasNoPolyException(typeName: String, isPType: Boolean)
extends SubdomainException(
  if (isPType) "DerivedPType $typeName does not have a corresponding PolyPType"
  else "DerivedType $typeName does not have a corresponding PolyPype")