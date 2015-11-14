package longevity.persistence

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import emblem.imports._
import longevity.exceptions.AssocIsUnpersistedException
import longevity.subdomain._
import longevity.context.LongevityContext

/** an in-memory repository for aggregate roots of type `E`
 * 
 * @param entityType the entity type for the aggregate roots this repository handles
 * @param subdomain the subdomain containing the root that this repo persists
 */
class InMemRepo[E <: RootEntity : TypeKey](
  entityType: RootEntityType[E],
  subdomain: Subdomain)
extends Repo[E](entityType, subdomain) {
  repo =>

  private case class IntId(i: Int) extends PersistedAssoc[E] {
    val associateeTypeKey = repo.entityTypeKey
    private[longevity] val _lock = 0
    def retrieve = repo.retrieve(this).map(_.get)
  }

  private var nextId = 0
  private var idToEntityMap = Map[PersistedAssoc[E], Persisted[E]]()
  
  case class NKV(val natKey: NatKey[E], val natKeyVal: NatKey[E]#Val)
  private var nkvToEntityMap = Map[NKV, Persisted[E]]()

  def create(unpersisted: Unpersisted[E]) = getSessionCreationOrElse(unpersisted, {
    patchUnpersistedAssocs(unpersisted.get).map { e =>
      val id = repo.synchronized {
        val id = IntId(nextId)
        nextId += 1
        id
      }
      persist(id, e)
    }
  })

  def retrieve(natKey: NatKey[E])(natKeyVal: natKey.Val): Future[Option[Persisted[E]]] = {
    natKey.props.foreach { prop =>
      if (prop.typeKey <:< typeKey[Assoc[_]]) {
        val assoc = natKeyVal(prop).asInstanceOf[Assoc[_ <: RootEntity]]
        if (!assoc.isPersisted) throw new AssocIsUnpersistedException(assoc)
      }
    }
    val optionE = nkvToEntityMap.get(NKV(natKey, natKeyVal))
    Promise.successful(optionE).future
  }

  def update(persisted: Persisted[E]) = {
    dumpNatKeys(persisted.orig)
    patchUnpersistedAssocs(persisted.get) map {
      persist(persisted.assoc, _)
    }
  }

  def delete(persisted: Persisted[E]) = {
    repo.synchronized { idToEntityMap -= persisted.assoc }
    dumpNatKeys(persisted.orig)
    val deleted = new Deleted(persisted)
    Promise.successful(deleted).future
  }

  private def retrieve(assoc: PersistedAssoc[E]) = {
    val optionE = idToEntityMap.get(assoc)
    Promise.successful(optionE).future
  }

  private def persist(assoc: PersistedAssoc[E], e: E): Persisted[E] = {
    val persisted = new Persisted[E](assoc, e)
    repo.synchronized {
      idToEntityMap += (assoc -> persisted)
      entityType.natKeys.foreach { natKey =>
        val natKeyVal = natKey.natKeyVal(e)
        nkvToEntityMap += (NKV(natKey, natKeyVal) -> persisted)
      }
    }
    persisted
  }

  private def dumpNatKeys(e: E) = repo.synchronized {
    entityType.natKeys.foreach { natKey =>
      val natKeyVal = natKey.natKeyVal(e)
      nkvToEntityMap -= NKV(natKey, natKeyVal)
    }
  }

}
