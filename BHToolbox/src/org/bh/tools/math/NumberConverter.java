package org.bh.tools.math;

import java.math.BigDecimal;
import static org.bh.tools.math.BasicFunctions.clamp;

/**
 * NumberConverter, made for BHToolbox, is copyright Blue Husky Programming ©2016 BH-1-PS<hr>
 * Converts numbers. This takes on the philosophy wherein data is kept raw first and foremost, then converted to the
 * highest available native precision as needed, then converted to the necessary precision. Nulls are treated as zeroes.
 *
 * @author Kyli of Blue Husky Programming
 * @since 2016-04-24
 */
public class NumberConverter {

    /**
     * Returns the 32-bit integer form of the given number. The returned value is clamped between
     * {@link Integer#MIN_VALUE} and {@link Integer#MAX_VALUE}.
     *
     * @param int64 The 64-bit integer to convert.
     * @return The given integer in 32 bits.
     */
    public static int to32Bit(Long int64) {
        return toInt32(int64);
    }

    /**
     * Returns the 32-bit floating-point form of the given number. Only native conversion is performed.
     *
     * @param float64 The 64-bit floating-point number to convert.
     * @return The given floating-point number in 32 bits.
     */
    public static float to32Bit(Double float64) {
        return toFloatingPoint32(float64);
    }

    /**
     * Returns the 32-bit integer form of the given number. The returned value is clamped between
     * {@link Integer#MIN_VALUE} and {@link Integer#MAX_VALUE}.
     *
     * @param int64 The 64-bit integer to convert.
     * @return The given integer in 32 bits.
     */
    public static int toInt32(Number n) {
        if (null == n) {
            return 0;
        }
        if (n instanceof Integer) {
            return (Integer) n;
        }
        return clamp(Integer.MIN_VALUE, n, Integer.MAX_VALUE).intValue();
    }

    /**
     * Returns the 32-bit floating-point form of the given number. Only native conversion is performed.
     *
     * @param float64 The 64-bit floating-point number to convert.
     * @return The given floating-point number in 32 bits.
     */
    public static float toFloatingPoint32(Number n) {
        if (null == n) {
            return 0f;
        }
        if (n instanceof Float) {
            return (Float) n;
        }
        return n.floatValue();
    }

    /**
     * Returns the floating-point form of the given number. This is done in the highest precision primitively supported
     * by Java SE. If the given number is {@code null}, {@code 0} is returned.
     *
     * @param n The number to convert.
     * @return The given number, as a floating-point primitive.
     */
    public static double toFloatingPoint(Number n) {
        if (null == n) {
            return 0;
        }
        if (n instanceof Double) {
            return (Double) n;
        }
        return n.doubleValue();
    }

    /**
     * Returns the integer form of the given number. This is done in the highest precision primitively supported by Java
     * SE. If the given number is {@code null}, {@code 0} is returned.
     *
     * @param n The number to convert.
     * @return The given number, as an integer primitive.
     */
    public static long toInteger(Number n) {
        if (null == n) {
            return 0L;
        }
        if (n instanceof Long) {
            return (Long) n;
        }
        return n.longValue();
    }

    /**
     * Returns the form of the given number which is a <a href="https://en.wikipedia.org/wiki/Real_number">real
     * number</a>. If the given number is {@code null}, {@link BigDecimal#ZERO 0} is returned.
     *
     * @param n The number to convert into a real number.
     * @return The real number form of the given number.
     * @see https://en.wikipedia.org/wiki/Real_number
     */
    public static BigDecimal toReal(Number n) {
        if (null == n) {
            return BigDecimal.ZERO;
        }
        if (n instanceof BigDecimal) {
            return (BigDecimal) n;
        }
        if (n instanceof Integer || n instanceof Long) {
            int nInt64 = toInt32(n);
            switch (nInt64) {
                case 0:
                    return BigDecimal.ZERO;

                case 1:
                    return BigDecimal.ONE;

                case 10:
                    return BigDecimal.TEN;

                default:
                    break;
            }
        }
        return new BigDecimal(toString(n));
    }

    /**
     * Returns the string form of the given number. If the given number is {@code null}, {@code "0"} is returned.
     *
     * @param n The number to convert.
     * @return The given number as a string.
     */
    public static String toString(Number n) {
        if (null == n) {
            return "0";
        }
        return n.toString();
    }
}
