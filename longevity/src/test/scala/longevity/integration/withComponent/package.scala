package longevity.integration

import longevity.context._
import longevity.shorthands._
import longevity.subdomain._

/** covers a root entity with a single association to another root entity */
package object withComponent {

  val entityTypes = EntityTypePool() + WithComponent + Component

  val subdomain = Subdomain("With Component", entityTypes)

  val longevityContext = LongevityContext(subdomain, ShorthandPool.empty, Mongo)

}