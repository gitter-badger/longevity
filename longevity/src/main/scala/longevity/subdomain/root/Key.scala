package longevity.subdomain.root

import emblem.basicTypes.isBasicType
import emblem.imports._
import longevity.exceptions.subdomain.PropValTypeException
import longevity.exceptions.subdomain.KeyHasNoSuchPropException
import longevity.exceptions.subdomain.UnsetPropException
import longevity.subdomain._

/** a natural key for this root entity type. a set of properties for which, given specific
 * property values for each of the properties, will match no more than one root instance.
 * 
 * @tparam R the root entity type
 * @param props the set of properties that make up this key
 */
case class Key[R <: RootEntity] private [subdomain] (
  val props: Seq[Prop[R, _]])(
  private implicit val shorthandPool: ShorthandPool) {

  private lazy val propPathToProp: Map[String, Prop[R, _]] = props.map(p => p.path -> p).toMap

  /** returns a builder for key vals */
  def builder = new ValBuilder

  /** returns the key val for the supplied root entity
   * @param e the root entity
   */
  def keyVal(root: R): Val = {
    val b = builder
    props.foreach { prop => b.setPropRaw(prop, prop.propVal(root)) }
    b.build
  }

  /** a value of this key */
  case class Val private[Key] (val propVals: Map[Prop[R, _], Any]) {

    /** gets the value of the key val for the specified prop.
     * 
     * throws java.util.NoSuchElementException if the prop is not part of the key
     * @param the prop to look up a value for
     */
    def apply(prop: Prop[R, _]): Any = propVals(prop)

    /** gets the value of the key val for the specified prop path.
     * 
     * throws java.util.NoSuchElementException if the prop indicated by the prop path is not part of the key
     * @param the prop to look up a value for
     */
    def apply(propPath: String): Any = propVals(propPathToProp(propPath))

    /** gets the shorthanded value of the key val for the specified prop. if there is a shorthand in
     * the shorthand pool that applies, it is applied to the raw value before it is returned.
     * 
     * throws java.util.NoSuchElementException if the prop is not part of the key
     * @param the prop to look up a value for
     */
    def shorthand(prop: Prop[R, _]): Any = {
      val raw = propVals(prop)
      if (shorthandPool.contains(prop.typeKey)) {
        def abbreviate[PV : TypeKey] = shorthandPool[PV].abbreviate(raw.asInstanceOf[PV])
        abbreviate(prop.typeKey)
      } else {
        raw
      }
    }

  }

  /** a builder of values for this key */
  class ValBuilder {

    private var propVals = Map[Prop[R, _], Any]()

    /** sets the property to the value */
    def setProp[A : TypeKey](propPath: String, propVal: A): ValBuilder =
      setProp(propPathToProp(propPath), propVal)

    /** sets the property to the value */
    def setProp[A : TypeKey](prop: Prop[R, _], propVal: A): ValBuilder = {
      if (!props.contains(prop)) throw new KeyHasNoSuchPropException(Key.this, prop)
      if (! (typeKey[A] <:< prop.typeKey)) throw new PropValTypeException(prop, propVal)
      propVals += prop -> propVal
      this
    }

    private[Key] def setPropRaw(prop: Prop[R, _], propVal: Any): Unit = {
      propVals += prop -> propVal
    }

    /** builds the key value
     * @throws longevity.exceptions.subdomain.UnsetPropException if any of the properties of the key
     * were not set in this builder
     */
    def build: Val = {
      if (propVals.size < props.size) {
        throw new UnsetPropException(Key.this, props diff propVals.keys.toSeq)
      }
      Val(propVals)
    }
  }

}
