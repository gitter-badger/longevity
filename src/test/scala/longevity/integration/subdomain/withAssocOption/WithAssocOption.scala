package longevity.integration.subdomain.withAssocOption

import longevity.subdomain._

case class WithAssocOption(
  uri: String,
  associated: Option[Assoc[Associated]])
extends Root

object WithAssocOption extends RootType[WithAssocOption] {
  key("uri")
}
