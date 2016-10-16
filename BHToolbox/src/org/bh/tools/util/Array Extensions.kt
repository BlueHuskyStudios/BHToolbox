package org.bh.tools.util

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for BHToolbox.
 *
 * @author Ben Leggiero
 * @since 2016-10-16
 */

infix operator fun <T> Array<T>.plus(rhs: Array<T>): Array<T> {
    var lhs = this
    lhs += rhs
    return lhs
}
