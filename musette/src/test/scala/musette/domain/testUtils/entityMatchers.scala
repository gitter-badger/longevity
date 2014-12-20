package musette.domain
package testUtils

import org.scalatest._

// TODO: find some way to do this kind of stuff generically
package object entityMatchers extends Matchers {

  def persistedShouldMatchUnpersisted(persisted: Site, unpersisted: Site): Unit = {
    persisted.uri should equal (unpersisted.uri)
  }

  def persistedShouldMatchUnpersisted(persisted: User, unpersisted: User): Unit = {
    persisted.uri should equal (unpersisted.uri)
    persistedShouldMatchUnpersisted(persisted.site.retrieve, unpersisted.site.unpersisted)
    persisted.email should equal (unpersisted.email)
    persisted.handle should equal (unpersisted.handle)
    persisted.slug should equal (unpersisted.slug)
  }

}
