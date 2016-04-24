package org.bh.tools.math;

import static org.bh.tools.math.NumberConverter.toFloatingPoint;

/**
 * BasicFunctions, made for BHToolbox, is copyright Blue Husky Programming ©2016 BH-1-PS<HR/>
 * Basic mathematical functions. This takes on the philosophy wherein data is kept raw first and foremost, then
 * converted to the highest available native precision as needed, then converted to the necessary precision.
 *
 * @author Kyli of Blue Husky Programming
 * @since 2016-04-24
 */
public class BasicFunctions {

    /**
     * Finds the first largest value in the given list. If there are two equal numbers whose values are higher than all
     * others, the first is returned. If the given array is {@code null} or empty, {@code null} is returned. This
     * comparison is done in the highest-available-precision floating-point form of the given values, but the returned
     * value is the raw, unconverted one from the given array.
     *
     *
     * @param numbers The numbers in which to find a maximum.
     * @return The highest of the given numbers.
     */
    public static final Number max(Number... numbers) {
        if (null == numbers || 0 == numbers.length) {
            return null;
        }
        if (1 == numbers.length) {
            return numbers[0];
        }
        if (2 == numbers.length) {
            if (toFloatingPoint(numbers[0]) > toFloatingPoint(numbers[1])) {
                return numbers[0];
            }
            return numbers[1];
        }
        Number largest = numbers[0];
        for (Number current : numbers) {
            if (toFloatingPoint(current) > toFloatingPoint(largest)) {
                largest = current;
            }
        }
        return largest;
    }

    /**
     * Finds the first lowest value in the given list. If there are two equal numbers whose values are lower than all
     * others, the first is returned. If the given array is {@code null} or empty, {@code null} is returned. This
     * comparison is done in the highest-available-precision floating-point form of the given values, but the returned
     * value is the raw, unconverted one from the given array.
     *
     *
     * @param numbers The numbers in which to find a minimum.
     * @return The lowest of the given numbers.
     */
    public static final Number min(Number... numbers) {
        if (null == numbers || 0 == numbers.length) {
            return null;
        }
        if (1 == numbers.length) {
            return numbers[0];
        }
        if (2 == numbers.length) {
            if (toFloatingPoint(numbers[0]) < toFloatingPoint(numbers[1])) {
                return numbers[0];
            }
            return numbers[1];
        }
        Number smallest = numbers[0];
        for (Number current : numbers) {
            if (toFloatingPoint(current) < toFloatingPoint(smallest)) {
                smallest = current;
            }
        }
        return smallest;
    }

    /**
     * Clamps the variable number between the other two ({@code lowest < variable < highest}). That is to say, if the
     * variable number's value is less than the lowest, the lowest is returned. Else, if it is greater than the highest,
     * the highest is returned. Else, the variable number is returned. This comparison is done in the
     * highest-available-precision floating-point form of the given values, but the returned value is the raw,
     * unconverted one provided.
     *
     * @param lowest   The lowest possible value to return. This is returned iff {@code variable} is less than it.
     * @param variable The number to clamp between the other two.
     * @param highest  The highest possible value to return. This is returned iff {@code variable} is greather than it.
     * @return {@code variable}, clamped between {@code lowest} and {@code highest}.
     */
    public static Number clamp(Number lowest, Number variable, Number highest) {
        if (toFloatingPoint(lowest) > toFloatingPoint(variable)) {
            return lowest;
        } else if (toFloatingPoint(variable) > toFloatingPoint(highest)) {
            return highest;
        }
        return lowest;
    }
}
