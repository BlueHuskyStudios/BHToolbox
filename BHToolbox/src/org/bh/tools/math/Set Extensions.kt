package org.bh.tools.math

/*
 * Copyright BHStudios ©2016 BH-1-PS. Made for BHToolbox.
 *
 * @author Ben Leggiero
 * @since 2016-10-13
 */

/**
 * Returns a set of `Int`s that starts at `from` and ends at `to`, inclusively.
 *
 * @param from the number at one end of the set
 * @param to   the number at the other end of the set
 */
fun setOfInts(from: Int, to: Int): Set<Int> {
    return object : Set<Int> {
        val lowest = Math.min(from, to)
        val highest = Math.max(from, to)
        val length = highest - lowest

        override val size: Int
            get() = length

        override fun contains(element: Int): Boolean {
            return element > lowest && element < highest
        }

        override fun containsAll(elements: Collection<Int>): Boolean {
            return elements.size >= length && !elements.any { !this.contains(it) }
        }

        override fun isEmpty(): Boolean {
            return length == 0
        }

        override fun iterator(): Iterator<Int> {
            return object : Iterator<Int> {
                var current = lowest

                override fun hasNext(): Boolean {
                    return current < highest
                }

                override fun next(): Int {
                    val old = current
                    current += 1
                    return old
                }
            }
        }
    }
}
