package org.bh.tools.struct.coord

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for BH Tic Tac Toe IntelliJ Project.
 *
 * @author Ben Leggiero
 * @since 2016-09-29
 */
data class Point<NumberType>(val x: NumberType, val y: NumberType) where NumberType: Number {
    companion object {
        val zero: Point<Number> get() = Point(0, 0)
    }
}

typealias Coordinate<NumberType> = Point<NumberType>

