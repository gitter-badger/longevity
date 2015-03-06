package emblem

// TODO pt 86950360: put in more of the map api

/** an abstract base class for shared functionality of [[TypeKeyMap]] and [[TypeBoundMap]] */
private[emblem] abstract class BaseTypeBoundMap[
  TypeBound,
  Key[_ <: TypeBound],
  Val[_ <: TypeBound]] (
  protected val underlying: Map[Any, Any]) {

  /** TODO scaladoc, specs */
  def contains(key: Key[_ <: TypeBound]) = underlying.contains(key)

  /** TODO scaladoc, specs */
  def foreach(f: (TypeBoundPair[TypeBound, Key, Val]) => Unit): Unit = iterator.foreach(f)

  override def hashCode = underlying.hashCode

  /** TODO scaladoc, specs */
  def iterator: Iterator[TypeBoundPair[TypeBound, Key, Val]] = {
    underlying.iterator.map { pair =>
      TypeBoundPair[TypeBound, Key, Val](
        pair._1.asInstanceOf[Key[TypeBound]],
        pair._2.asInstanceOf[Val[TypeBound]])
    }
  }

  /** Tests whether the map is empty.
   * @return `true` if the map does not contain any key/value binding, `false` otherwise.
   */
  def isEmpty = underlying.isEmpty

  /** Collects all keys of this map in an iterable collection.
   * @return the keys of this map as an iterable.
   */
  def keys: Iterable[Key[_ <: TypeBound]] = underlying.keys.asInstanceOf[Iterable[Key[_ <: TypeBound]]]

  /** The number of key/value bindings in this map */
  def size = underlying.size

  /** Collects all values of this map in an iterable collection.
   * @return the values of this map as an iterable. */
  def values: collection.Iterable[Val[_ <: TypeBound]] =
    underlying.values.asInstanceOf[Iterable[Val[_ <: TypeBound]]]

  protected def mapValuesUnderlying[
    TypeBound2 >: TypeBound,
    Val2[_ <: TypeBound2]](
    f: WideningTypeBoundFunction[TypeBound, TypeBound2, Val, Val2])
  : Map[Any, Any] = {
    def mapValue[TypeParam <: TypeBound](value1: Val[TypeParam]): Val2[TypeParam] = {
      f.apply[TypeParam](value1)
    }
    underlying.mapValues { value1 =>
      mapValue(value1.asInstanceOf[Val[_ <: TypeBound]])
    }
  }

}
