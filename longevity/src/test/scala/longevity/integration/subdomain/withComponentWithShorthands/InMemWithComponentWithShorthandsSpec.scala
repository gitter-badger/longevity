package longevity.integration.subdomain.withComponentWithShorthands

import org.scalatest.Suites

class InMemWithComponentWithShorthandsSpec extends Suites(context.mongoContext.inMemRepoPoolSpec)
