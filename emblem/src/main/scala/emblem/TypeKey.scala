package emblem

import scala.reflect.runtime.universe._
import emblem.reflectionUtil.makeTypeTag
import emblem.stringUtil.typeFullname
import emblem.stringUtil.typeName

/** behaves much like a `scala.reflect.runtime.universe.TypeTag`, except that it can also be safely used
 * as a key in a hash or a set. Two type keys will be equal if and only if their underlying types are equivalent
 * according to method `=:=` in `scala.reflect.api.Types.Type`. The [[hashCode]] method does its best to
 * produce unique hash values, and always produces values compatible with [[equals]].
 *
 * type keys are provided by an implicit method in [[emblem package emblem]], so you can get one implicitly
 * like so:
 *
 * {{{
 * def foo[A : TypeKey]() = {
 *   val key = implicitly[TypeKey[A]]
 * }
 * }}}
 *
 * or you can get one explicitly like so:
 *
 * {{{
 * val key = emblem.typeKey[List[String]]
 * }}}
 *
 * or if you already have a `TypeTag` at hand:
 *
 * {{{
 * val tag: TypeTag[A] = ???
 * val key = TypeKey(tag)
 * }}}
 *
 * @tparam A the type that we are keying one
 * @param tag the scala-reflect `TypeTag` for type `A`
 */
case class TypeKey[A](val tag: TypeTag[A]) {

  /** the scala-reflect `Type` for type `A` */
  def tpe: Type = tag.tpe

  /** shorthand for `this.tpe.<:<` */
  def <:<(that: Type) = this.tpe <:< that

  /** shorthand for `this.tpe.<:<` */
  def <:<(that: TypeKey[_]) = this.tpe <:< that.tpe

  /** shorthand for `this.tpe.=:=` */
  def =:=(that: Type) = this.tpe =:= that

  /** shorthand for `this.tpe.=:=` */
  def =:=(that: TypeKey[_]) = this.tpe =:= that.tpe

  /** the full type name for the type represented by this key */
  def fullname = typeFullname(tpe)

  /** the simple type name for the type represented by this key */
  def name = typeName(tpe)

  /** a list of type keys for the type arguments of this type */
  lazy val typeArgs: List[TypeKey[_]] = tpe.typeArgs map { tpe => TypeKey(makeTypeTag(tpe)) }

  /** if `B` is a lower bound to type `A`, then return `this` cast to `TypeKey[_ >: B]`, wrapped in an option.
   * this method is provided for Scala grammer only, so that you can reuse this type to safely please the
   * compiler into matching types. however, the return value will remain `==` to `this`, so it can be reused as
   * a key into a `TypeKeyMap`. but it will probably not be equal to a `TypeKey[_ >: B]` generated by other
   * means. in short, the results of this method are for short term use, as keys with the desired type.
   *
   * Here's an example how this works:
   *
   * {{{
   * val tki = typeKey[Int]
   * val tkli = typeKey[List[Int]]
   * val tkla = typeKey[List[_]]
   * val tksi = typeKey[Set[Int]]
   * val keys: List[TypeKey[_]] = List(tki, tkli, tkla, tksi)
   * 
   * // this is how i type:
   * 
   * val lotks: List[Option[TypeKey[_ >: String]]] = keys.map(_.castToLowerBound[String])
   * val lotki: List[Option[TypeKey[_ >: Int]]] = keys.map(_.castToLowerBound[Int])
   * 
   * val lotkli: List[Option[TypeKey[_ >: List[Int]]]] = keys.map(_.castToLowerBound[List[Int]])
   * val lotkla: List[Option[TypeKey[_ >: List[_]]]] = keys.map(_.castToLowerBound[List[_]])
   * 
   * // this is how i equal:
   * 
   * keys.map(_.castToLowerBound[String]) should equal (List(None, None, None, None))
   * keys.map(_.castToLowerBound[Int]) should equal (List(Some(tki), None, None, None))
   * 
   * keys.map(_.castToLowerBound[List[Int]]) should equal (List(None, Some(tkli), Some(tkla), None))
   * keys.map(_.castToLowerBound[List[_]]) should equal (List(None, None, Some(tkla), None))
   * }}}
   */
  def castToLowerBound[B : TypeKey]: Option[TypeKey[_ >: B]] =
    if (typeKey[B].tpe <:< this.tpe) Some(this.asInstanceOf[TypeKey[_ >: B]]) else None

  /** if `B` is an upper bound to type `A`, then return `this` cast to `TypeKey[_ <: B]`, wrapped in an option.
   * this method is provided for Scala grammer only, so that you can reuse this type to safely please the
   * compiler into matching types. however, the return value will remain `==` to `this`, so it can be reused as
   * a key into a `TypeKeyMap`. but it will probably not be equal to a `TypeKey[_ <: B]` generated by other
   * means. in short, the results of this method are for short term use, as keys with the desired type.
   *
   * Here's an example how this works:
   *
   * {{{
   * val tki = typeKey[Int]
   * val tkli = typeKey[List[Int]]
   * val tkla = typeKey[List[_]]
   * val tksi = typeKey[Set[Int]]
   * val keys: List[TypeKey[_]] = List(tki, tkli, tkla, tksi)
   * 
   * // this is how i type:
   * 
   * val lotks: List[Option[TypeKey[_ <: String]]] = keys.map(_.castToUpperBound[String])
   * val lotki: List[Option[TypeKey[_ <: Int]]] = keys.map(_.castToUpperBound[Int])
   * 
   * val lotkli: List[Option[TypeKey[_ <: List[Int]]]] = keys.map(_.castToUpperBound[List[Int]])
   * val lotkla: List[Option[TypeKey[_ <: List[_]]]] = keys.map(_.castToUpperBound[List[_]])
   * 
   * // this is how i equal:
   * 
   * keys.map(_.castToUpperBound[String]) should equal (List(None, None, None, None))
   * keys.map(_.castToUpperBound[Int]) should equal (List(Some(tki), None, None, None))
   * 
   * keys.map(_.castToUpperBound[List[Int]]) should equal (List(None, Some(tkli), None, None))
   * keys.map(_.castToUpperBound[List[_]]) should equal (List(None, Some(tkli), Some(tkla), None))
   * }}}
   */
  def castToUpperBound[B : TypeKey]: Option[TypeKey[_ <: B]] =
    if (this.tpe <:< typeKey[B].tpe) Some(this.asInstanceOf[TypeKey[_ <: B]]) else None

  override def equals(that: Any): Boolean = that.isInstanceOf[AnyRef] && {
    (this eq that.asInstanceOf[AnyRef]) || {
      that.isInstanceOf[TypeKey[_]] && {
        val thatTypeKey = that.asInstanceOf[TypeKey[_]]
        if (this.tag eq thatTypeKey.tag) true

        // this line is both a tentative performance optimization and a sanity check for me. if it turns out
        // hashCode is reporting different values for what should be the same type, then this will show up as
        // the type keys being unequal, rather than as erratic set/map behavior.
        else if (this.hashCode != that.hashCode) false

        else this.tpe =:= thatTypeKey.tpe
      }
    }
  }

  // TODO pt-86950618: this will map types such as List[Int] and List[String] to the same hash value. include
  // considerations for the type arguments to account for this
  override lazy val hashCode = {
    def symbolToString(s: Symbol):String = {
      val fullName = s.fullName
      fullName.substring(fullName.lastIndexWhere(c => c == '.' || c == '$') + 1)
    }
    val declNames = tag.tpe.decls.map(symbolToString _).toSet
    declNames.hashCode
  }

  override def toString = s"TypeKey[$tpe]"

}
