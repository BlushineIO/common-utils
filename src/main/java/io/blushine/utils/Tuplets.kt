/**
 * Original source code by Jetbrains s.r.o. in file Tuplets.kt. This file simply changes
 * [Pair] and [Tuplets] to mutable versions
 */

package io.blushine.utils

import java.io.Serializable

/**
 * Represents a generic pair of two values.
 *
 * There is no meaning attached to values in this class, it can be used for any purpose.
 * MutablePair exhibits value semantics, i.e. two pairs are equal if both components are equal.
 *
 * An example of decomposing it into values:
 * @sample samples.misc.Tuples.pairDestructuring
 *
 * @param A type of the first value.
 * @param B type of the second value.
 * @property first First value.
 * @property second Second value.
 * @constructor Creates a new instance of MutablePair.
 */
data class MutablePair<A, B>(
		var first: A,
		var second: B
) : Serializable {

	/**
	 * Returns string representation of the [MutablePair] including its [first] and [second] values.
	 */
	override fun toString(): String = "($first, $second)"
}

/**
 * Creates a tuple of type [MutablePair] from this and [that].
 *
 * This can be useful for creating [Map] literals with less noise, for example:
 * @sample samples.collections.Maps.Instantiation.mapFromPairs
 */
infix fun <A, B> A.to(that: B): MutablePair<A, B> = MutablePair(this, that)

/**
 * Converts this pair into a list.
 */
fun <T> MutablePair<T, T>.toList(): List<T> = listOf(first, second)

/**
 * Represents a triad of values
 *
 * There is no meaning attached to values in this class, it can be used for any purpose.
 * MutableTriple exhibits value semantics, i.e. two triples are equal if all three components are equal.
 * An example of decomposing it into values:
 * @sample samples.misc.Tuples.tripleDestructuring
 *
 * @param A type of the first value.
 * @param B type of the second value.
 * @param C type of the third value.
 * @property first First value.
 * @property second Second value.
 * @property third Third value.
 */
data class MutableTriple<A, B, C>(
		var first: A,
		var second: B,
		var third: C
) : Serializable {

	/**
	 * Returns string representation of the [MutableTriple] including its [first], [second] and [third] values.
	 */
	override fun toString(): String = "($first, $second, $third)"
}

/**
 * Converts this triple into a list.
 */
fun <T> MutableTriple<T, T, T>.toList(): List<T> = listOf(first, second, third)
