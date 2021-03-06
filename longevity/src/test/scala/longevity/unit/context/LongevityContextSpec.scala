package longevity.unit.context

import longevity.TestLongevityConfigs.cassandraConfig
import longevity.TestLongevityConfigs.mongoConfig
import longevity.context.Cassandra
import longevity.context.LongevityContext
import longevity.context.Mongo
import longevity.subdomain.KeyVal
import longevity.subdomain.Subdomain
import longevity.subdomain.PTypePool
import longevity.subdomain.PType
import org.json4s.JsonAST.JObject
import org.json4s.JsonAST.JString
import org.scalatest.FlatSpec
import org.scalatest.GivenWhenThen
import org.scalatest.Matchers
import scala.concurrent.ExecutionContext.Implicits.global

/** provides a sample [[LongevityContext]] to use in testing */
object LongevityContextSpec {

  object sample {

    case class AId(id: String) extends KeyVal[A]

    case class A(id: AId)
    object A extends PType[A] {
      object props {
        val id = prop[AId]("id")
      }
      val keySet = Set(key(props.id))
    }

    val subdomain = Subdomain("subtypePropType", PTypePool(A))
    val mongoContext = new LongevityContext(subdomain, mongoConfig)
    val cassandraContext = new LongevityContext(subdomain, cassandraConfig)
  }

}

/** unit tests for the proper [[LongevityContext]] construction */
class LongevityContextSpec extends FlatSpec with GivenWhenThen with Matchers {

  val contextBackEndPairs = Seq(
    (LongevityContextSpec.sample.mongoContext, Mongo),
    (LongevityContextSpec.sample.cassandraContext, Cassandra))

  for ((context, backEnd) <- contextBackEndPairs) {

    behavior of s"LongevityContext creation for ${context.config.backEnd}"

    it should "produce a context with the right subdomain" in {
      context.subdomain should equal (LongevityContextSpec.sample.subdomain)
    }

    it should "produce a context with the right back end" in {
      context.config.backEnd should equal (backEnd)
    }

    it should "produce repo pools of the right size" in {
      context.repoPool.values.size should equal (1)
      context.testRepoPool.values.size should equal (1)
      context.inMemTestRepoPool.values.size should equal (1)
    }

    it should "provide RepoCrudSpecs against both test repo pools" in {
      context.repoCrudSpec should not be (null)
      context.inMemRepoCrudSpec should not be (null)
    }

    it should "provide a working JSON marshaller" in {
      import LongevityContextSpec.sample.AId
      val marshaller = context.jsonMarshaller
      val jvalue = marshaller.marshall(AId("foo"))
      jvalue shouldBe a [JObject]
      val jobject = jvalue.asInstanceOf[JObject]
      jobject.values.size should equal (1)
      jobject.values.contains("id") should be (true)
      jobject.values("id") should equal ("foo")
    }

    it should "provide a working JSON unmarshaller" in {
      import LongevityContextSpec.sample.AId
      val unmarshaller = context.jsonUnmarshaller
      val aid = unmarshaller.unmarshall[AId](JObject(("id", JString("foo")) :: Nil))
      aid should equal (AId("foo"))
    }

  }

}
