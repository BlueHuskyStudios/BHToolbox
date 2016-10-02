package org.bh.tools.func

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for BH Tic Tac Toe IntelliJ Project.
 *
 * Generates a new object of type `OutputType`
 *
 * @author Ben Leggiero
 * @since 2016-10-01
 */
interface Generator<OutputType> {
    fun generate(): OutputType
}

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for BH Tic Tac Toe IntelliJ Project.
 *
 * Generates a new object of type `OutputType`, based on an index.
 *
 * @author Ben Leggiero
 * @since 2016-10-01
 */
interface IndexedGenerator<OutputType> {
    fun generate(idx: Int): OutputType
}
