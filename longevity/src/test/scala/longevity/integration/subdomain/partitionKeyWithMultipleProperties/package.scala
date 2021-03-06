package longevity.integration.subdomain

import longevity.TestLongevityConfigs
import longevity.subdomain.Subdomain
import longevity.subdomain.CTypePool
import longevity.subdomain.CType
import longevity.subdomain.PTypePool

/** covers a persistent with a partition key that contains multiple properties */
package object partitionKeyWithMultipleProperties {

  val subdomain = Subdomain(
    "Partition Key With Multiple Properties",
    PTypePool(PartitionKeyWithMultipleProperties),
    CTypePool(CType[Uri]))

  val contexts = TestLongevityConfigs.sparseContextMatrix(subdomain)

}
