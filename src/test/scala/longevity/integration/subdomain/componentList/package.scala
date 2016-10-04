package longevity.integration.subdomain

import longevity.TestLongevityConfigs
import longevity.subdomain.embeddable.ETypePool
import longevity.subdomain.embeddable.EType
import longevity.subdomain.Subdomain
import longevity.subdomain.ptype.PTypePool

/** covers a root entity with a list of component entities */
package object componentList {

  val subdomain = Subdomain(
    "Component List",
    PTypePool(WithComponentList),
    ETypePool(EType[Component]))

  val contexts = TestLongevityConfigs.sparseContextMatrix(subdomain)

}
