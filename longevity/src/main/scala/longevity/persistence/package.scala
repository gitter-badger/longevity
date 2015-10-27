package longevity

import com.mongodb.casbah.Imports._
import com.typesafe.config.Config
import emblem.imports._
import emblem.TypeBoundPair
import longevity.context._
import longevity.subdomain._

/** manages entity persistence operations */
package object persistence {

  /** a `TypeKeyMap` of [[longevity.subdomain.RootEntity RootEntity]] to [[Repo]] */
  type RepoPool = TypeKeyMap[RootEntity, Repo]

  private[longevity] def buildRepoPool(
    subdomain: Subdomain,
    persistenceStrategy: PersistenceStrategy,
    config: Config)
  : RepoPool =
    persistenceStrategy match {
      case InMem => inMemRepoPool(subdomain)
      case Mongo =>
        val mongoClient = MongoClient(config.getString("mongodb.uri"))
        val mongoDb = mongoClient.getDB(config.getString("mongodb.db"))

        import com.mongodb.casbah.commons.conversions.scala._
        RegisterJodaTimeConversionHelpers()

        mongoRepoPool(subdomain, mongoDb)
    }

  private def inMemRepoPool(subdomain: Subdomain): RepoPool = {
    object repoFactory extends StockRepoFactory {
      def build[E <: RootEntity](entityType: RootEntityType[E], entityKey: TypeKey[E]): Repo[E] =
        new InMemRepo(entityType, subdomain)(entityKey)
    }
    buildRepoPool(subdomain, repoFactory)
  }

  private def mongoRepoPool(subdomain: Subdomain, mongoDB: MongoDB): RepoPool = {
    object repoFactory extends StockRepoFactory {
      def build[E <: RootEntity](entityType: RootEntityType[E], entityKey: TypeKey[E]): Repo[E] =
        new MongoRepo(entityType, subdomain, mongoDB)(entityKey)
    }
    buildRepoPool(subdomain, repoFactory)
  }

  private trait StockRepoFactory {
    def build[E <: RootEntity](entityType: RootEntityType[E], entityKey: TypeKey[E]): Repo[E]
  }

  private def buildRepoPool(
    subdomain: Subdomain,
    stockRepoFactory: StockRepoFactory)
  : RepoPool = {
    var repoPool = emptyRepoPool
    type Pair[RE <: RootEntity] = TypeBoundPair[RootEntity, TypeKey, RootEntityType, RE]
    def createRepoFromPair[RE <: RootEntity](pair: Pair[RE]): Unit = {
      val entityKey = pair._1
      val entityType = pair._2
      val repo = stockRepoFactory.build(entityType, entityKey)
      repoPool += (entityKey -> repo)
    }
    subdomain.rootEntityTypePool.iterator.foreach { pair => createRepoFromPair(pair) }
    finishRepoInitialization(repoPool)
    repoPool
  }

  private val emptyRepoPool = TypeKeyMap[RootEntity, Repo]

  private def finishRepoInitialization(repoPool: RepoPool): Unit = {
    repoPool.values.foreach { repo => repo._repoPoolOption = Some(repoPool) }
  }

}