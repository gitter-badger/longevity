package longevity.subdomain.ptype

import emblem.TypeKey
import emblem.emblematic.Emblem
import emblem.typeKey
import longevity.subdomain.KeyVal

/** a natural key for this persistent type. wraps a [[Prop property]] that,
 * given specific a property value, will match the corresponding member of no
 * more than one persistent object.
 * 
 * @tparam P the persistent type
 * @tparam V the key value type
 * @param keyValProp a property for the key
 */
class Key[P : TypeKey, V <: KeyVal[P] : TypeKey] private [subdomain] (
  val keyValProp: Prop[P, V]) {

  private[subdomain] val keyValEmblem = Emblem[V]
  private[longevity] def keyValTypeKey = keyValEmblem.typeKey

  override def toString = s"Key[${typeKey[P].name},${typeKey[V].name}]"

  override def hashCode = keyValProp.hashCode

  override def equals(that: Any) =
    that.isInstanceOf[Key[_, _]] && keyValProp == that.asInstanceOf[Key[_, _]].keyValProp

}
