@file:Suppress("unused")

package org.bh.tools.util

import org.bh.tools.func.IndexedGenerator
import org.bh.tools.util.ArrayPosition.END
import org.bh.tools.util.ArrayPosition.START
import org.bh.tools.util.SearchBehavior.*
import java.util.*
import java.util.function.Consumer
import java.util.logging.Logger
import java.util.stream.Stream


/**
 * Would be called {@code MutableArray++} if {@code +} was a legal character for a class name. This is a mutable array
 * with many enhancements and conveniences.
 *
 * @param <T> The type of item to store
 *
 * @see ArrayPP
 *
 * @author Kyli of Blue Husky Programming
 * @version 2.3.0  <pre>
 *		- 2016-10-01 (2.3.0)
 *                      ~ Ben added functional programming paradigms.
 *		- 2016-04-06 (2.2.0)
 *                      ~ Kyli renamed {@code Array++} to {@code MutableArray++}
 *		- 2015-08-30 (2.1.0)
 *			! Kyli simplified {@link #emptyArrayOfLength(int)}
 *			~ Kyli changed the log message when an object couldn't be destroyed with {@link #clear(boolean) clear(true)}.
 *			+ Kyli added {@link #remove(SearchBehavior,Object,ArrayPosition)},
 *				{@link #remove(int)}, and {@link #removeChunk(int,int)}.
 *		- 2015-03-03 (2.0.0) . Kyli created this completely new ArrayPP, as a rewrite of {@link org.bh.tools.util.ArrayPP}.
 * </pre>
 *
 * @since 2015-03-03
 */
class MutableArrayPP<T> : ArrayPP<T>, MutableIterable<T?> {

    constructor(vararg basis: T) : super(*basis)

    /**
     * Creates a new Array++ with the items from the given [Iterable].
     *
     * @param basis the [Iterable] holding the items to put in this Array++
     */
    constructor(basis: Iterable<T>) {
        basis.forEach(Consumer<T> { this.append(it) })
    }

    /**
     * Creates a new, empty array++ of the given size.
     *
     * @param initSize    The size of the new, empty array
     *
     * @param emptySample `optional` - An empty array to use to guarantee the new, empty array++ is of the right type.
     *                    If this is not empty, its contents will be at the beginning of the new array++.
     */
    constructor(initSize: Int, vararg emptySample: T) : super(initSize, *emptySample)

    /**
     * Creates a new, filled, mutable array++ of the given size.
     *
     * @param numberOfElements The size of the new, empty, immutable array++
     *
     * @param generator        The generator which will be used to fill this array with its contents
     *
     * @param emptySample      `optional` - An non-null empty array to use to guarantee the new, empty, immutable
     *                         array++ is of the right type. If this is not empty, its contents will be at the
     *                         beginning of the new, immutable array++.
     */
    constructor(numberOfElements: Int, generator: IndexedGenerator<T>, vararg emptySample: T)
    : super(numberOfElements, generator, *emptySample)

    /**
     * Adds all the given values to the end of this array++
     *
     * @param newValues The new values to be added
     *
     * @return `this`, with the added values
     */
    fun add(vararg newValues: T): MutableArrayPP<T> {
        return add(END, *newValues)
    }

    fun add(position: ArrayPosition, vararg newVals: T): MutableArrayPP<T> {
        when (position) {
            END -> return append(*newVals)
            START -> return prepend(*newVals)
        }
    }

    /**
     * Removes all objects from the array, with an option to destroy them as they are removed. Destruction is calling
     * their [.finalize] method.
     *
     * @param destructive If `true`, attempts to call [Object.finalize] on each object in this array.
     *                    If any one fails, a message is logged and that one is skipped. Either way, the array is
     *                    emptied at the end.
     *
     *
     * @return `this`
     */
    @Suppress("UNCHECKED_CAST") // for some reason this complains. Seems like a compiler bug.
    fun clear(destructive: Boolean): MutableArrayPP<T> {
        if (destructive) {
            (array.filter { it is Finalizable } as List<Finalizable>).forEach { it.finalize() }
        }
        array = Arrays.copyOf(array, 0)
        return this
    }

    /**
     * Removes all objects from the array.

     * @return this
     */
    fun clear(): MutableArrayPP<T> {
        return clear(false)
    }

    fun increaseSize(thisManyMoreSlots: Int): MutableArrayPP<T> {
        System.arraycopy(array, 0,
                array, 0,
                array.size + thisManyMoreSlots)
        return this
    }

    /**
     * Sets the value at slot `index` to be `newVal`

     * @param index      The index of the slot to change
     * *
     * @param newVal     The new value to put into the slot
     * *
     * @param increaseOK Indicates whether it's OK to increase the size of the array to accommodate the new value. If
     * *                   `false`, a [ArrayIndexOutOfBoundsException] may be thrown by passing an index that
     * *                   is too high.
     * *
     * *
     * @return `this`
     * *
     * *
     * @throws ArrayIndexOutOfBoundsException if `increaseOK` is `false` and `index` is greater than
     * *                                        [length()][.length]
     */
    @JvmOverloads fun set(index: Int, newVal: T, increaseOK: Boolean = false): MutableArrayPP<T> {
        if (increaseOK) {
            if (index > this.length()) {
                increaseSize(this.length() - index + 1)
            }
        }
        array[index] = newVal
        return this
    }

    /**
     * Sets the values at slots `fromIndex` through `newValues.length - fromIndex` to the values in
     * `newValues`
     *
     * @param fromIndex The index of the slot to change
     * @param newValues   The new values to put into the slot
     *
     * @return `this`
     */
    @Deprecated("untested!\n      ")
    fun setAll(fromIndex: Int, newValues: Array<T>): MutableArrayPP<T> {
        System.arraycopy(
                newValues, 0,
                array, fromIndex,
                this.length() - fromIndex)
        return this
    }

    /**
     * Appends the given values to the end of this array.
     *
     * @param newValues the new values to append
     *
     * @return `this`
     */
    @SuppressWarnings("unchecked")
    fun prepend(vararg newValues: T): MutableArrayPP<T> {
        var newArray: Array<T?> = newValues as Array<T?>
        newArray += array
        array = newArray
        return this
    }

    /**
     * Remove all instances of the given value from this array++.
     *
     * @param val The value to search for and remove
     *
     * @return `this`
     */
    fun removeAll(value: T?): MutableArrayPP<T> {
        return remove(ALL, value, START)
    }

    /**
     * Removes the given item from the array, using the given behaviors.
     *
     * @param behavior The behavior by which to search
     *
     *  * If [ANY][ArrayPP.SearchBehavior.ANY], removes the first found matching object at an index close
     *    to `near`. If none is found, nothing is changed.
     *  * If [ALL][ArrayPP.SearchBehavior.ALL], removes each and every found matching object. If none is
     *    found, nothing is changed.
     *  * If [SOLELY][ArrayPP.SearchBehavior.SOLELY], first determines if the array consists solely of
     *    matching objects. If it does, [.clear] is called.
     *
     * @param value      The value to remove
     * @param near     Used if `behavior` is [ANY][ArrayPP.SearchBehavior.ANY].
     *
     * @return `this`
     */
    fun remove(behavior: SearchBehavior, value: T?, near: ArrayPosition): MutableArrayPP<T> {
        when (behavior) {
            ANY -> {
                val index = indexOf(near, value)
                if (index >= 0) {
                    remove(index)
                }
            }

            ALL -> for (i in array.indices) {
                if (value === array[i] || value == array[i]) {
                    remove(i)
                }
            }

            SOLELY -> if (contains(SOLELY, value)) {
                clear()
            }
        }
        return this
    }

    /**
     * Removes the item at index `index`.
     *
     * @param index The index of the item to remove
     *
     * @return `this`
     */
    fun remove(index: Int): MutableArrayPP<T> {
        return removeChunk(index, index)
    }

    /**
     * Removes all items from `startIndex`, inclusive, to `endIndex`, inclusive.
     *
     * @param startIndex The first index whose item is to be removed
     * @param endIndex   The last index whose item is to be removed
     *
     * @return this
     */
    @SuppressWarnings("AssignmentToMethodParameter")
    fun removeChunk(startIndex: Int, endIndex: Int): MutableArrayPP<T> {
        var adjustedStartIndex = startIndex
        var adjustedEndIndex = endIndex
        if (adjustedStartIndex > adjustedEndIndex) {
            val oldStart = adjustedStartIndex
            adjustedStartIndex = adjustedEndIndex
            adjustedEndIndex = oldStart
        }
        System.arraycopy(array, adjustedStartIndex, // start writing at startIndex
                array, adjustedEndIndex + 1, // start writing with the object after endIndex
                array.size - (adjustedEndIndex - adjustedStartIndex + 1)) // shorten the array appropriately
        return this
    }

    /**
     * Remove all null values from this array++.
     *
     * @return `this`
     */
    fun removeNulls(): MutableArrayPP<T> {
        return removeAll(null)
    }

    /**
     * @return A stream of all values in this array, which can be traversed asynchronously
     */
    fun parallelStream(): Stream<T> {
        return stream().parallel()
    }

    /**
     * Appends the given values to the end of this array.
     *
     * @param newVals the new values to append
     *
     * @return `this`
     */
    @SuppressWarnings("unchecked")
    fun append(vararg newVals: T): MutableArrayPP<T> {
        var length = length()
        var insertPoint = length
        var currentIndex = 0
        length += newVals.size
        array = java.util.Arrays.copyOf(array, length)

        while (insertPoint < length) {
            array[insertPoint] = newVals[currentIndex]
            insertPoint++
            currentIndex++
        }

        return this
    }

    /**
     * Appends the values in the given Iterable to the end of this array. **The given [Iterable]
     * *must* be finite!**
     *
     * @param vals the values to be appended, as represented by an `Iterable`
     *
     * @return the resulting array.
     */
    fun addAll(vals: Iterable<T>): MutableArrayPP<T> {
        val app = MutableArrayPP(vals)
        val INIT_LENGTH = length()
        val split = vals.spliterator()

        var count = split.exactSizeIfKnown
        if (count < 0) { // if exact size isn't known
            count = split.estimateSize()
        }
        if (count < 0 || count == java.lang.Long.MAX_VALUE) { // if size is still unknown
            split.forEachRemaining { item -> count++ }
        }

        array = Arrays.copyOf(array, INIT_LENGTH + count.toInt())
        var insertPoint = INIT_LENGTH
        split.forEachRemaining { array[insertPoint++] = it }
        return this
    }

    fun addAll(newVals: Array<T>): MutableArrayPP<T> {
        var length = length()
        var insertPoint = length
        var currentIndex = 0
        length += newVals.size
        array = java.util.Arrays.copyOf(array, length)

        while (insertPoint < length) {
            array[insertPoint] = newVals[currentIndex]
            insertPoint++
            currentIndex++
        }

        return this
    }
    //</editor-fold>

    companion object {
        val serialVersionUID = 0x30000000L

        //<editor-fold defaultstate="collapsed" desc="Wrapping & Unwrapping">
        fun wrap(unwrapped: ByteArray?): ArrayPP<Byte>? {
            if (unwrapped == null) {
                return null
            }

            if (unwrapped.size == 0) {
                return ArrayPP()
            }

            return ArrayPP(unwrapped.size, { idx -> unwrapped[idx] })
        }

        fun unwrap(wrapped: ArrayPP<Byte>?): ByteArray? {
            if (wrapped == null) {
                return null
            }
            if (wrapped.size == 0) {
                return ByteArray(0)
            }

            val ret = ByteArray(wrapped.size)
            for (i in ret.indices) {
                ret[i] = wrapped[i]!! // We know all elements in this are non-optional
            }
            return ret
        }

        fun wrap(unwrapped: IntArray?): Array<Int?>? {
            if (unwrapped == null) {
                return null
            }
            if (unwrapped.size == 0) {
                return arrayOfNulls(0)
            }

            val ret = arrayOfNulls<Int>(unwrapped.size)
            for (i in ret.indices) {
                ret[i] = unwrapped[i]
            }
            return ret
        }

        fun unwrap(wrapped: Array<Int>?): IntArray? {
            if (wrapped == null) {
                return null
            }
            if (wrapped.size == 0) {
                return IntArray(0)
            }

            val ret = IntArray(wrapped.size)
            for (i in ret.indices) {
                ret[i] = wrapped[i]
            }
            return ret
        }
    }

    override fun iterator(): MutableIterator<T?> {
        return object : MutableIterator<T?> {
            private var pos = 0

            override fun hasNext(): Boolean {
                return pos < length()
            }

            @Throws(ArrayIndexOutOfBoundsException::class)
            override fun next(): T? {
                return get(++pos - 1)
            }

            override fun remove() {
                Logger.getLogger(javaClass.name).severe("remove() called on immutable array")
            }
        }
    }
}
