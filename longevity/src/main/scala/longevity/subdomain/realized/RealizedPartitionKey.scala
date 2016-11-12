package longevity.subdomain.realized

import emblem.TypeKey
import emblem.emblematic.EmblematicPropPath
import longevity.subdomain.KeyVal
import longevity.subdomain.ptype.PartitionKey

private[longevity] case class RealizedPartitionKey[
  P : TypeKey,
  V <: KeyVal[P, V] : TypeKey] private [subdomain](
  override val key: PartitionKey[P, V],
  prop: RealizedProp[P, V],
  val partitionProps: Seq[RealizedProp[P, _]],
  val postPartitionProps: Seq[RealizedProp[P, _]],
  val emblematicPropPaths: Seq[EmblematicPropPath[V, _]])
extends RealizedKey[P, V](key, prop) {

  def hashed = key.hashed
  def partition = key.partition
  def fullyPartitioned = key.fullyPartitioned
  def props = partitionProps ++ postPartitionProps

  case class QueryInfo[B](inlinedPath: String, get: (V) => B, typeKey: TypeKey[B])

  lazy val queryInfos = props.zip(emblematicPropPaths).map {
    case (prop, epp) =>
      def qi[B](epp: EmblematicPropPath[V, B]) = QueryInfo(prop.inlinedPath, epp.get, epp.typeKey)
      qi(epp)
  }

}
