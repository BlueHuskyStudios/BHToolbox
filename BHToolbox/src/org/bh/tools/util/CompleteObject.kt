package org.bh.tools.util

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for BH Tic Tac Toe IntelliJ Project.
 *
 * A complete object is one which implements every single [Object] method
 *
 * @author Ben Leggiero
 * @since 2016-10-09
 */
interface CompleteObject : Cloneable, Equatable, Finalizable, Hashable, Stringifiable

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for BH Tic Tac Toe IntelliJ Project.
 *
 * An object which can be equated to another one
 *
 * @author Ben Leggiero
 * @since 2016-10-09
 */
interface Equatable {
    override fun equals(other: Any?): Boolean
}

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for BH Tic Tac Toe IntelliJ Project.
 *
 * An object which wants to explicitly have its [Object.finalize] function called at some point
 *
 * @author Ben Leggiero
 * @since 2016-10-09
 */
interface Finalizable {
    @Throws(Throwable::class) fun finalize()
}

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for BH Tic Tac Toe IntelliJ Project.
 *
 * An object which can provide a hash code
 *
 * @author Ben Leggiero
 * @since 2016-10-09
 */
interface Hashable {
    override fun hashCode(): Int
}

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for BH Tic Tac Toe IntelliJ Project.
 *
 * An object which can be turned into a [String]
 *
 * @author Ben Leggiero
 * @since 2016-10-09
 */
interface Stringifiable {
    override fun toString(): String
}