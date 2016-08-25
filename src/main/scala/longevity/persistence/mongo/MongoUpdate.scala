package longevity.persistence.mongo

import com.mongodb.DuplicateKeyException
import com.mongodb.casbah.commons.MongoDBObjectBuilder
import longevity.exceptions.persistence.WriteConflictException
import longevity.persistence.PState
import longevity.subdomain.persistent.Persistent
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.blocking

/** implementation of MongoRepo.create */
private[mongo] trait MongoUpdate[P <: Persistent] {
  repo: MongoRepo[P] =>

  def update(state: PState[P])(implicit context: ExecutionContext) = Future {
    val query = buildQuery(state)
    val modifiedDate = persistenceConfig.modifiedDate
    val casbah = casbahForP(state.get, mongoId(state), modifiedDate)
    val writeResult = try {
      blocking {
        mongoCollection.update(query, casbah)
      }
    } catch {
      case e: DuplicateKeyException => throwDuplicateKeyValException(state.get, e)
    }
    if (persistenceConfig.optimisticLocking && writeResult.getN == 0) {
      throw new WriteConflictException(state)
    }
    PState[P](state.id, modifiedDate, state.get)
  }

  private def buildQuery(state: PState[P]) = {
    val builder = new MongoDBObjectBuilder()
    builder += "_id" -> mongoId(state)
    if (persistenceConfig.optimisticLocking) {
      builder += "_modifiedDate" -> state.modifiedDate
    }
    builder.result()
  }

}