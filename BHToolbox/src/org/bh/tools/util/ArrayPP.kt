@file:Suppress("unused")

package org.bh.tools.util

import org.bh.tools.func.IndexedGenerator
import org.bh.tools.math.setOfInts
import org.bh.tools.util.ArrayPosition.END
import org.bh.tools.util.ArrayPosition.START
import org.bh.tools.util.Do.S.`as`
import org.bh.tools.util.Do.S.s
import org.bh.tools.util.SearchBehavior.*
import org.bh.tools.util.SearchResults.NOT_FOUND
import java.io.Serializable
import java.util.*
import java.util.stream.Stream
import kotlin.collections.Map.Entry


/**
 * ArrayPP, made for BHToolbox, is copyright Blue Husky Programming ©2015 BH-1-PS <hr>
 * Would have been called "{@code Array++}" if "{@code +}" were an acceptable character. This class is made to give
 * enhanced features to arrays, such as basic search functions and comparability, along with the standard accessing
 * methods. The internal structure is an array, so as to make the interactions as memory-efficient as possible.<br/>
 * <br/>
 * Many array-management methods return {@code this}, so as to allow daisy-chaining of commands, like you can with
 * {@code String}s.<br/>
 * <h3>Examples:</h3> {@code String} array: {@code final String[] S = new String[]{"Won", "too", "tree!"};}<br/>
 * {@code Array++} of {@code String}s:
 * {@code final ImmutableArrayPP<String> S = new ImmutableArrayPP<String>("Won", "too", "tree!");}
 *
 * @param <T> The type of object to store
 * <h3>Examples:</h3>
 * <ul>
 * <li>{@code ImmutableArrayPP<String> strings = new ImmutableArrayPP<>();}
 * <ul><li>A new ImmutableArray++ of {@code String}s. Analogous to {@code final String[] strings;}</li></ul>
 * </li>
 * <li>{@code ImmutableArrayPP<String> strings = new ImmutableArrayPP<>(args);}
 * <ul><li>A clone of an array of {@code String}s. Analogous to {@code final String[] strings = args;}</li></ul>
 * </li>
 * <li>{@code String[] newArgs = new ImmutableArrayPP<String>(args).toArray();}
 * <ul>
 * <li>A clone of an array of {@code String}s. Analogous to
 * {@code String[] temp; final String[] strings = System.arraycopy(args, 0, temp, 0, args.length);}.</li>
 * </ul>
 * </li>
 * </ul>
 *
 * @author Kyli of Blue Husky Programming
 * @version 2.3.0  <pre>
 *		- 2016-10-01 (2.3.0)
 *                      ~ Ben added functional programming paradigms.
 *		- 2016-04-06 (2.2.0)
 *                      ~ Kyli renamed {@code ImmutableArray++} to {@code Array++}.
 *		~ 2015-08-30 (2.1.0) - Kyli renamed {@code ContainsBehavior} to {@code SearchBehavior}.
 *		+ 2015-03-03 (2.0.0) - Kyli created this completely new ArrayPP, as an immutable rewrite of {@link bht.tools.util.ArrayPP}.
 * </pre>
 *
 * @since 2015-03-03
 */
open class ArrayPP<T> : Comparable<ArrayPP<T>>, Iterable<T?>, Serializable {
    companion object {
        //                       Mmmmrrrr
        val serialVersionUID = 0x30000000
    }

    /**
     * The internal array that holds all the data for this Array++
     */
    protected var array: Array<T?>
    protected val type: Class<T> // maybe passed into the method

    /**
     * Creates a new array++ with the given content type and contents.
     *
     * @param contents The contents of the array++.
     */
    @Suppress("UNCHECKED_CAST")
    constructor(vararg contents: T) {
        type = contents.javaClass as Class<T>
        array = Arrays.copyOf(contents, contents.size)
    }

    /**
     * Creates a new, empty, immutable array++ of the given size.
     *
     * @param initSize    The size of the new, empty, immutable array++
     *
     * @param emptySample An non-null empty array to use to guarantee the new, empty, immutable array++ is of the right
     *                    type. If this is not empty, its contents will be at the beginning of the new, immutable
     *                    array++.
     */
    @Suppress("UNCHECKED_CAST")
    constructor(initSize: Int, vararg emptySample: T) {
        type = emptySample.javaClass as Class<T>
        array = Arrays.copyOf(emptySample, initSize)
    }

    /**
     * Creates a new, filled, immutable array++ of the given size.
     *
     * @param numberOfElements The size of the new, empty, immutable array++
     *
     * @param emptySample      An non-null empty array to use to guarantee the new, empty, immutable array++ is of the
     *                         right type. If this is not empty, its contents will be at the beginning of the new,
     *                         immutable array++.
     *
     * @param generator        The generator which will be used to fill this array with its contents
     */
    @SafeVarargs
    constructor(numberOfElements: Int, generator: IndexedGenerator<T>, vararg emptySample: T)
    : this(numberOfElements, *emptySample) {
        (0..numberOfElements - 1).forEach { i -> array[i] = generator(i) }
    }

    /**
     * Compares this array to the other. If the lengths are different, their difference is returned. Else, if any one
     * item in the arrays are inequal, `-1` is returned. Else (if the arrays are the same size and all elements are
     * equal), `0` is returned.
     */
    override fun compareTo(other: ArrayPP<T>): Int {
        val lengthDiff = length() - other.length()
        if (lengthDiff != 0) {
            return lengthDiff
        }
        if (other.length() == array.size) {
            (0 until array.size).forEach { i ->
                if (array[i] != other[i]) {
                    return -1
                }
            }
        }
        return 0
    }

    /**
     * Returns the item at the given position

     * @param index the index of the item to get
     * *
     * *
     * @return the item you wanted
     * *
     * *
     * @throws ArrayIndexOutOfBoundsException if the given index is lower than 0 or greater than [.length]
     */
    operator fun get(index: Int): T? {
        return array[index]
    }

    override fun iterator(): kotlin.collections.Iterator<T?> {
        return object : kotlin.collections.Iterator<T?> {
            private var pos = 0

            override fun hasNext(): Boolean {
                return pos < length()
            }

            @Throws(ArrayIndexOutOfBoundsException::class)
            override fun next(): T? {
                return get(++pos - 1)
            }
        }
    }

    /**
     * @return the length of the array, which is the same as the number of items in it
     */
    fun length(): Int {
        return array.size
    }

    /** The same as [length()] */
    val length: Int get() = length() // sure!

    /** The same as [length()] */
    val size: Int get() = length() // okay!

    /** The same as [length()] */
    val count: Int get() = length() // why not? xD

    /**
     * Indicates whether this array has a length of `0`

     * @return `true` iff this array has a length of `0`
     */
    val isEmpty: Boolean
        get() = array.size == 0

    /**
     * Indicates whether this array contains nothing but `null`s

     * @return `true` iff this array contains nothing but `null`s
     */
    val isFlat: Boolean
        get() = contains(SearchBehavior.SOLELY, null as T)

    /**
     * @return A **copy** of the array at the core of this class
     */
    fun toArray(): Array<T?> {
        return array.clone()
    }

    /**
     * Gets the indices of all occurrences where `needle.equals(occurrence)`. If `needle` is `null`,
     * [.indicesOfNulls] is called.

     * @param needle the item to search for
     * *
     * *
     * @return the indices where `needle` occurs
     */
    fun indicesOf(needle: T?): IntArray {
        if (needle == null) {
            return indicesOfNulls()
        }
        val indices = LinkedList<Int>()
        run {
            var i = 0
            val l = length()
            while (i < l) {
                if (needle == get(i)) {
                    indices.add(i)
                }
                i++
            }
        }
        val result = indices.toTypedArray()
        val ret = IntArray(result.size)
        for (i in ret.indices) {
            ret[i] = result[i]
        }
        return ret
    }

    /**
     * Returns the indices of all `null` values in this array.

     * @return the indices of all `null{ values in this array.`
     */
    fun indicesOfNulls(): IntArray {
        val indices = LinkedList<Int>()
        run {
            var i = 0
            val l = length()
            while (i < l) {
                if (get(i) == null) {
                    indices.add(i)
                }
                i++
            }
        }
        val result = indices.toTypedArray()
        val ret = IntArray(result.size)
        for (i in ret.indices) {
            ret[i] = result[i]
        }
        return ret
    }

    /**
     * Returns the index of the given needle near the given position in the array. If the given needle can't be found at
     * all, [NOT_FOUND][SearchResults.NOT_FOUND].[INTVAL][SearchResults.INTVAL] is returned.

     * @param near   the position to start searching from
     * *
     * @param needle the needle to search from
     * *
     * *
     * @return the index of the given needle near the given position, or `-1` if none can be found.
     */
    fun indexOf(near: ArrayPosition, needle: T?): Int {
        if (needle == null) {
            return indexOfNull(near)
        }
        when (near) {
            START -> run {
                var i = 0
                val l = length()
                while (i < l) {
                    if (needle == get(i)) {
                        return i
                    }
                    i++
                }
            }
            END -> for (i in length() downTo 1) {
                if (needle == get(i)) {
                    return i
                }
            }
        }
        return SearchResults.NOT_FOUND.INTVAL
    }


    /**
     * Returns the index of the first `null` value near the given position in the array. If no `null` can be
     * found at all, `-1` is returned

     * @param near the position to start searching from
     * *
     * *
     * @return the index of the first null near the given position, or `-1` if none can be found.
     */
    private fun indexOfNull(near: ArrayPosition): Int {
        when (near) {
            START -> run {
                var i = 0
                val l = length()
                while (i < l) {
                    if (get(i) == null) {
                        return i
                    }
                    i++
                }
            }
            END -> for (i in length() downTo 1) {
                if (get(i) == null) {
                    return i
                }
            }
        }
        return NOT_FOUND.INTVAL
    }

    /**
     * Determines if the given `needle` exists within this array

     * @param needle the needle to search for
     * *
     * *
     * @return `true` iff the given `needle` was found.
     */
    operator fun contains(needle: T): Boolean {
        return indexOf(START, needle) >= 0
    }

    /**
     * Determines if the given `needles` exist within this array, based on the `behavior` given. When using
     * ANY and SOLELY behavior, this is more efficient when `needles.length &lt; this.length()`. The opposite is true
     * with ALL behavior.
     *
     * @param behavior the behavior of this search
     * @param needles  the needles to search for
     *
     * @return `true` iff the given needles are found with the given behavior
     */
    fun contains(behavior: SearchBehavior, vararg needles: T?): Boolean {
        when (behavior) {
            ANY ->
                // TODO: If this array is smaller than the needle list, make the outer loop the needles
                run {
                    var i = 0
                    val l = length()
                    while (i < l) { // let's hope this array has more items than are being searched for
                        val t = get(i)
                        for (needle in needles) {
                            if (needle == t) {
                                return true
                            }
                        }
                        i++
                    }
                }
            ALL -> needleLooper@ for (needle in needles) { // for all needles...
                var i = 0
                val l = length()
                while (i < l) { // compare against all items in this array
                    val t = get(i)
                    if (needle == t) { // if there is an item in the array that matches this needle
                        continue@needleLooper // start back up top
                    }
                    i++
                }
                return false // if that if statement was never entered, we never found it.
            }
            SOLELY -> {
                var i = 0
                val l = length()
                while (i < l) {
                    val t = get(i)
                    for (needle in needles) {
                        if (needle == t) {
                            return true
                        }
                    }
                    i++
                }
            }
        }
        return false
    }

    override fun toString(): String {
        return `as`(this)
    }

    /**
     * Returns this array, starting with the given `preceding` character sequence, ending with the given
     * `succeeding` one, and with the given `separator` between each element.
     *
     * @param preceding  the character sequence to start the String with
     * @param separator  the character sequence to end the String with
     * @param succeeding the character sequence to separate each item with
     *
     * @return this array, starting with, separated by, and ending with the given sequences
     *
     * @author Kyli Rouge
     * @since 2014-08-20
     * @version 1.0.0 - 2014-08-20 (1.8.0) - Kyli Rouge created this method for more powerful stringification
     */
    fun toString(preceding: CharSequence, separator: CharSequence, succeeding: CharSequence): String {
        if (isEmpty) {
            return s(preceding) + succeeding
        }
        val sb = StringBuilder(preceding)
        var i = 0
        val l = length() - 1
        while (i < l) {
            sb.append(get(i)).append(separator)
            i++
        }
        return sb.append(lastItem).append(succeeding).toString()
    }

    fun containsNull(): Boolean {
        return stream().anyMatch { t -> null == t }
    }

    fun stream(): Stream<T> {
        return Arrays.stream(toArray())
    }

    val lastItem: T?
        get() = get(length() - 1)

    /**
     * Indicates whether this contains any of, all of, or exactly the contents of the given collection
     */
    fun contains(behavior: SearchBehavior, collection: Collection<T?>): Boolean {
        return when (behavior) {
            ANY -> array.any { collection.contains(it) }
            ALL -> array.any { !collection.contains(it) }
            SOLELY -> length() == collection.size && contains(ALL, collection)
        }
    }

    /**
     * @return this array++ as a collection. This does not involve copying, and so will always reflect the state of
     * this array.
     */
    fun toCollection(): Collection<T?> {
        return object : Collection<T?> {
            override val size: Int
                get() = length()

            override fun contains(element: T?): Boolean {
                return this@ArrayPP.contains(element)
            }

            override fun containsAll(elements: Collection<T?>): Boolean {
                return this@ArrayPP.contains(ALL, elements)
            }

            override fun isEmpty(): Boolean {
                return this@ArrayPP.isEmpty
            }

            override fun iterator(): Iterator<T?> {
                return this@ArrayPP.iterator()
            }
        }
    }

    val orderedSetValue: Set<Entry<Int, T?>>
        get() = object : Set<Entry<Int, T?>> {
            override val size: Int
                get() = this@ArrayPP.length()

            override fun contains(element: Entry<Int, T?>): Boolean {
                throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun containsAll(elements: Collection<Entry<Int, T?>>): Boolean {
                throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun isEmpty(): Boolean {
                throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun iterator(): Iterator<Entry<Int, T?>> {
                throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }

    val mapValue: Map<Int, T?>
        get() = object : Map<Int, T?> {
            override val entries: Set<Entry<Int, T?>>
                get() = this@ArrayPP.orderedSetValue

            override val keys: Set<Int>
                get() = setOfInts(0, length())

            override val size: Int
                get() = this@ArrayPP.length()

            override val values: Collection<T?>
                get() = this@ArrayPP.toCollection()

            override fun containsKey(key: Int): Boolean {
                return key < this@ArrayPP.length()
            }

            override fun containsValue(value: T?): Boolean {
                return this@ArrayPP.contains(value)
            }

            override fun get(key: Int): T? {
                return this@ArrayPP[key]
            }

            override fun isEmpty(): Boolean {
                return length() == 0
            }
        }
}


/**
 * Basic, universal positions in an array
 */
enum class ArrayPosition {
    /**
     * Represents the beginning of the array (index `0`)
     */
    START,
    /**
     * Represents the end of the array (index `length`)
     */
    END
}


/**
 * Indicates what kind of behavior to exhibit when searching.
 */
enum class SearchBehavior {
    /**
     * Indicates that search methods should stop after the first find
     */
    ANY,
    /**
     * Indicates that search methods should guarantee that ALL given items are in the array
     */
    ALL,
    /**
     * Indicates that search methods should guarantee that the the array consists SOLELY of the given items
     */
    SOLELY
}


/**
 * Indicates a specific, yet non-typical search result, wherein a specific result cannot be returned.
 */
enum class SearchResults constructor(
        /**
         * The integer value of this search result.
         */
        val INTVAL: Int) {

    /**
     * Indicates that the value which was searched for was not found. This has an [integer value][.INTVAL] of
     * `-1`
     */
    NOT_FOUND(-1)
}
