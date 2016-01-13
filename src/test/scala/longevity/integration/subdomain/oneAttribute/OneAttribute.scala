package longevity.integration.subdomain.oneAttribute

import longevity.subdomain._

case class OneAttribute(uri: String) extends Root

object OneAttribute extends RootType[OneAttribute] {
  key("uri")
}
