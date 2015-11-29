package longevity.persistence.mongo

import com.mongodb.casbah.Imports._
import emblem.imports._
import emblem.stringUtil._
import longevity.exceptions.subdomain.AssocIsUnpersistedException
import longevity.persistence._
import longevity.subdomain._
import longevity.subdomain.root._
import org.bson.types.ObjectId
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Failure
import scala.util.Success

/** a MongoDB repository for aggregate roots of type `E`.
 *
 * @param entityType the entity type for the aggregate roots this repository handles
 * @param subdomain the subdomain containing the root that this repo persists
 * @param mongoDb the connection to the mongo database
 */
class MongoRepo[E <: RootEntity : TypeKey] protected[persistence] (
  entityType: RootEntityType[E],
  subdomain: Subdomain,
  mongoDb: MongoDB)
extends Repo[E](entityType, subdomain) {
  repo =>

  private[persistence] case class MongoId(objectId: ObjectId) extends PersistedAssoc[E] {
    val associateeTypeKey = repo.entityTypeKey
    private[longevity] val _lock = 0
    def retrieve = repo.retrieve(this).map(_.get)
  }

  private val collectionName = camelToUnderscore(typeName(entityTypeKey.tpe))
  private val mongoCollection = mongoDb(collectionName)

  private val emblemPool = subdomain.entityEmblemPool
  private val extractorPool = shorthandPoolToExtractorPool(subdomain.shorthandPool)
  private lazy val entityToCasbahTranslator = new EntityToCasbahTranslator(emblemPool, extractorPool, repoPool)
  private lazy val casbahToEntityTranslator = new CasbahToEntityTranslator(emblemPool, extractorPool, repoPool)

  def create(unpersisted: Unpersisted[E]) = getSessionCreationOrElse(unpersisted, {
    patchUnpersistedAssocs(unpersisted.get) map { patched =>
      val objectId = new ObjectId()
      val casbah = entityToCasbahTranslator.translate(patched) ++ MongoDBObject("_id" -> objectId)
      val writeResult = mongoCollection.insert(casbah)
      new Persisted[E](MongoId(objectId), patched)
    }
  })

  def retrieve(key: Key[E])(keyVal: key.Val): Future[Option[Persisted[E]]] = Future {
    val builder = MongoDBObject.newBuilder
    key.props.foreach { prop => builder += (prop.path -> resolvePropVal(prop, keyVal(prop))) }
    val query = builder.result
    val resultOption = mongoCollection.findOne(query)
    val idEntityOption = resultOption map { result =>
      val id = result.getAs[ObjectId]("_id").get
      id -> casbahToEntityTranslator.translate(result)
    }
    idEntityOption map { case (id, e) => new Persisted[E](MongoId(id), e) }
  }

  private def resolvePropVal(prop: Prop[E, _], raw: Any): Any = {
    if (subdomain.shorthandPool.contains(prop.typeKey)) {
      def abbreviate[PV : TypeKey] = subdomain.shorthandPool[PV].abbreviate(raw.asInstanceOf[PV])
      abbreviate(prop.typeKey)
    } else if (prop.typeKey <:< typeKey[Assoc[_]]) {
      val assoc = raw.asInstanceOf[Assoc[_ <: RootEntity]]
      if (!assoc.isPersisted) throw new AssocIsUnpersistedException(assoc)
      raw.asInstanceOf[MongoRepo[T]#MongoId forSome { type T <: RootEntity }].objectId
    } else {
      raw
    }
  }

  def update(persisted: Persisted[E]) = patchUnpersistedAssocs(persisted.get) map { patched =>
    val objectId = persisted.assoc.asInstanceOf[MongoId].objectId
    val query = MongoDBObject("_id" -> objectId)
    val casbah = entityToCasbahTranslator.translate(patched) ++ MongoDBObject("_id" -> objectId)
    val writeResult = mongoCollection.update(query, casbah)
    new Persisted[E](persisted.assoc, patched)
  }

  def delete(persisted: Persisted[E]) = Future {
    val objectId = persisted.assoc.asInstanceOf[MongoId].objectId
    val query = MongoDBObject("_id" -> objectId)
    val writeResult = mongoCollection.remove(query)
    new Deleted(persisted)
  }

  protected def retrieveByValidatedQuery(query: ValidatedQuery[E]): Future[Seq[Persisted[E]]] = {
    ???
  }

  private def retrieve(assoc: PersistedAssoc[E]) = Future {
    val objectId = assoc.asInstanceOf[MongoId].objectId
    val query = MongoDBObject("_id" -> objectId)
    val resultOption = mongoCollection.findOne(query)
    val entityOption = resultOption map { casbahToEntityTranslator.translate(_) }
    entityOption map { e => new Persisted[E](assoc, e) }
  }

}
