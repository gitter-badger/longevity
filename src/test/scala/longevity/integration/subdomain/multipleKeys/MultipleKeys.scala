package longevity.integration.subdomain.multipleKeys

import longevity.subdomain._

case class MultipleKeys(
  uri: String,
  username: String)
extends Root

object MultipleKeys extends RootType[MultipleKeys] {
  key("uri")
  key("username")
}
