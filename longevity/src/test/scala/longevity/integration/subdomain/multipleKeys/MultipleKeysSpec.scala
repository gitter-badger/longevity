package longevity.integration.subdomain.multipleKeys

import org.scalatest.Suites
import scala.concurrent.ExecutionContext.Implicits.global

class MultipleKeysSpec extends Suites(contexts.map(_.repoCrudSpec): _*)

