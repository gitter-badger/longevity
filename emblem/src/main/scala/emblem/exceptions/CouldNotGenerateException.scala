package emblem.exceptions

import emblem.TypeKey

/** an exception thrown when
 * [[emblem.emblematic.traversors.sync.TestDataGenerator TestDataGenerator]]
 * cannot generate requested data due to encountering an unsupported type
 */
class CouldNotGenerateException(val typeKey: TypeKey[_], cause: CouldNotTraverseException)
extends TraversorException(s"don't know how to generate test data for type ${typeKey.tpe}", cause)
