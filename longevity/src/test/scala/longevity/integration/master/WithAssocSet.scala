package longevity.integration.master

import longevity.subdomain._

case class WithAssocSet(
  uri: String,
  associated: Set[Assoc[Associated]])
extends RootEntity

object WithAssocSet extends RootEntityType[WithAssocSet]
