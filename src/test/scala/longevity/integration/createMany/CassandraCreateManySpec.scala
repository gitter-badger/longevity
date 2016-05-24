package longevity.integration.createMany

import longevity.integration.subdomain.withAssoc

/** unit tests for the [[RepoPool.createMany]] method against Cassandra back end */
class CassandraCreateManySpec extends BaseCreateManySpec(
  withAssoc.mongoContext,
  withAssoc.mongoContext.testRepoPool)