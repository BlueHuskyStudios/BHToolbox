package org.bh.tools.struct.coord

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for BH Tic Tac Toe IntelliJ Project.
 *
 * @author Ben Leggiero
 * @since 2016-09-29
 */
data class Size<NumberType>(val width: NumberType, val height: NumberType) where NumberType: Number

typealias Dimension<NumberType> = Size<NumberType>
