package bht.tools.util.math;

import bht.tools.util.ArrayPP;
import bht.tools.util.StringPP;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bht.tools.util.Do.s;

import java.awt.Dimension;

/**
 * Numbers, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012 BH-1-PS.<hr> A
 * convenience class for dealing with numbers
 *
 * @author Supuhstar
 * @version 2.3.0  <pre>
 *		- 2.3.0 (2015-03-02)
 *			+ Kyli added:
 *				{@link #isIntegerOfSize(java.lang.CharSequence, int)}
 *				{@link #isByte(CharSequence)}
 *				{@link #isShort(CharSequence)}
 *				{@link #isShort(CharSequence)}
 *				{@link #isInt(CharSequence)}
 *				{@link #isLong(CharSequence)}
 * </pre>
 */
@SuppressWarnings({"ConfusingArrayVararg", "PrimitiveArrayArgumentToVariableArgMethod", "BroadCatchBlock",
    "TooBroadCatch"})
public class Numbers {

    //<editor-fold defaultstate="collapsed" desc="Behavior Flags">
    /**
     * Used for {@link #max(java.awt.Dimension, java.awt.Dimension, byte)} and {@link #min(java.awt.Dimension,
     * java.awt.Dimension, byte)} to signify that the returned dimension should have a larger/smaller overall perimeter
     * than the other.
     */
    public static final byte BEHAVIOR_PERIMETER = 0b0000;
    /**
     * Used for {@link #max(java.awt.Dimension, java.awt.Dimension, byte)} and {@link #min(java.awt.Dimension,
     * java.awt.Dimension, byte)} to signify that the returned dimension should have a larger/smaller overall area than
     * the other.
     */
    public static final byte BEHAVIOR_AREA = 0b0001;
    /**
     * Used for {@link #max(java.awt.Dimension, java.awt.Dimension, byte)} and {@link #min(java.awt.Dimension,
     * java.awt.Dimension, byte)} to signify that the returned dimension should be wider/thinner than the other.
     */
    public static final byte BEHAVIOR_WIDTH = 0b0010;
    /**
     * Used for {@link #max(java.awt.Dimension, java.awt.Dimension, byte)} and {@link #min(java.awt.Dimension,
     * java.awt.Dimension, byte)} to signify that the returned dimension should be taller/shorter than the other.
     */
    public static final byte BEHAVIOR_HEIGHT = 0b0100;
    /**
     * Used for {@link #max(java.awt.Dimension, java.awt.Dimension, byte)} and {@link #min(java.awt.Dimension,
     * java.awt.Dimension, byte)} to signify that the returned dimension should(n't) fit inside the other.
     */
    public static final byte BEHAVIOR_FIT = 0b1000;
    //</editor-fold>

    /**
     * Returns the larger dimension based on the given behavior.
     *
     * @param dim1     The first dimension to be compared
     * @param dim2     The second dimension to be compared
     * @param BEHAVIOR the behavior of the determination algorithm.<br/>
     * <h5>All Possible Uses:</h5>
     * <UL>
     * <LI>{@link #BEHAVIOR_AREA}</LI>
     * <LI>{@link #BEHAVIOR_PERIMETER}</LI>
     * <LI>{@link #BEHAVIOR_HEIGHT}</LI>
     * <LI>{@link #BEHAVIOR_WIDTH}</LI>
     * <LI>{@link #BEHAVIOR_FIT} (or {@code Numbers.BEHAVIOR_HEIGHT | Numbers.BEHAVIOR_WIDTH}) (returns the dimension
     * which is either taller or wider)</LI>
     * </UL>
     *
     * @return the larger dimension
     *
     * @version 1.1.0 - 1.1.0 - 2013-09-26 - Kyli Rouge - Added BEHAVIOR_FIT, changed + to |
     */
    public static Dimension max(Dimension dim1, Dimension dim2, final byte BEHAVIOR) {
        switch (BEHAVIOR) {
            default:
                throw new IllegalArgumentException("Undefined byte mask: " + BEHAVIOR);
            case BEHAVIOR_AREA:
                return dim1.height * dim1.width > dim2.height * dim2.width ? dim1 : dim2;
            case BEHAVIOR_PERIMETER:
                return (dim1.height * 2) + (dim1.width * 2) > (dim2.height * 2) + (dim2.width * 2) ? dim1 : dim2;
            case BEHAVIOR_HEIGHT:
                return dim1.height > dim2.height ? dim1 : dim2;
            case BEHAVIOR_WIDTH:
                return dim1.width > dim2.width ? dim1 : dim2;
            case BEHAVIOR_FIT:
            case BEHAVIOR_WIDTH | BEHAVIOR_HEIGHT:
                return dim1.height > dim2.height ? dim1 : (dim1.width > dim2.width ? dim1 : dim2);
        }
    }

    /**
     * Returns the smaller dimension based on the given behavior.
     *
     * @param dim1     The first dimension to be compared
     * @param dim2     The second dimension to be compared
     * @param BEHAVIOR the behavior of the determination algorithm.<br/>
     * <h5>All Possible Uses:</h5>
     * <UL>
     * <LI>{@link #BEHAVIOR_AREA}</LI>
     * <LI>{@link #BEHAVIOR_PERIMETER}</LI>
     * <LI>{@link #BEHAVIOR_HEIGHT}</LI>
     * <LI>{@link #BEHAVIOR_WIDTH}</LI>
     * <LI>{@link #BEHAVIOR_FIT} (or {@code Numbers.BEHAVIOR_HEIGHT | Numbers.BEHAVIOR_WIDTH}) (returns the dimension
     * which is either shorter or thinner)</LI>
     * </UL>
     *
     * @return the smaller dimension
     *
     * @version 1.1.0 - 1.1.0 - 2013-09-26 - Kyli Rouge - Added BEHAVIOR_FIT, changed + to |
     */
    public static Dimension min(Dimension dim1, Dimension dim2, final byte BEHAVIOR) {
        switch (BEHAVIOR) {
            default:
                throw new IllegalArgumentException("Undefined byte mask: " + BEHAVIOR);
            case BEHAVIOR_AREA:
                return dim1.height * dim1.width < dim2.height * dim2.width ? dim1 : dim2;
            case BEHAVIOR_PERIMETER:
                return (dim1.height * 2) + (dim1.width * 2) < (dim2.height * 2) + (dim2.width * 2) ? dim1 : dim2;
            case BEHAVIOR_HEIGHT:
                return dim1.height < dim2.height ? dim1 : dim2;
            case BEHAVIOR_WIDTH:
                return dim1.width < dim2.width ? dim1 : dim2;
            case BEHAVIOR_FIT:
            case BEHAVIOR_WIDTH | BEHAVIOR_HEIGHT:
                return dim1.height < dim2.height ? dim1 : (dim1.width < dim2.width ? dim1 : dim2);
        }
    }

    /**
     * Creates and returns the rank form of the given number <h4>Examples</h4> <UL> <LI>{@code toRank(1)} returns
     * {@code "1st"}</LI> <LI>{@code toRank(2)} returns {@code "2nd"}</LI> <LI>{@code toRank(3)} returns
     * {@code "3rd"}</LI>
     * <LI>{@code toRank(4)} returns {@code "4th"}</LI> <LI>{@code toRank(0)} returns {@code "0th"}</LI>
     * <LI>{@code toRank(21)} returns {@code "21st"}</LI> <LI>{@code toRank(42)} returns {@code "42nd"}</LI>
     * <LI>{@code toRank(11)} returns {@code "11st"} (it's not perfect, blame the English language for not calling it
     * "Tenty-first")</LI> </UL>
     *
     * @param number the number to become a rank
     *
     * @return
     */
    public static String toRank(double number) {
        String ret = toPrettyString(number);
        return (ret += ret.endsWith("1") ? "st" : (ret.endsWith("2") ? "nd" : (ret.endsWith("3") ? "rd" : "th")));
    }

    /**
     * Calculates the ratio between the two given numbers. This uses {@link #getGCDRec(double, double)}. If that fails,
     * it uses {@link #getGCDItr(double, double)}.
     *
     * @param d1 The first number. Can be any value.
     * @param d2 The second number. Can be any value.
     *
     * @return The ratio of the two numbers, as a {@link Ratio}
     *
     * @throws bht.tools.util.math.Numbers.IrrationalRatioException if no ratio can be found. This is highly unlikely.
     * @see Ratio
     * @see #getGCDRec(double, double)
     * @see #getGCDItr(double, double)
     */
    public static strictfp Ratio getRatio(double d1, double d2) throws IrrationalRatioException {
        if (d1 == 0) {
            return new Ratio((d2 == 0 ? 0 : Double.POSITIVE_INFINITY), 0);
        }
        if (d2 == 0) {
            return new Ratio(0, Double.POSITIVE_INFINITY);
        }
        while (max(d1, d2) < Long.MAX_VALUE && (!isCloseTo(d1, (long) d1) || !isCloseTo(d2, (long) d2)))//Get it without the decimal
        {
            d1 *= 10;
            d2 *= 10;
        }
        long l1 = (long) d1, l2 = (long) d2;
        try {
//      l1 = (long)teaseUp(d1); l2 = (long)teaseUp(d2); //my gut tells me to leave this uncommented, but it's causing this method to return zeroes.
            double gcd = getGCDRec(l1, l2);
            return new Ratio(((long) (d1 / gcd)), ((long) (d2 / gcd)));
        } catch (StackOverflowError er) {
            try {
                double gcd = getGCDItr(l1, l2);
                return new Ratio(((long) (d1 / gcd)), ((long) (d2 / gcd)));
            } catch (Throwable t) {
                throw new IrrationalRatioException("Irrational ratio: " + l1 + " to " + l2);
            }
        }
    }

    /**
     * Recursively attempts to discover the <b>G</b>reatest <b>C</b>ommon <b>D</b>enominator between the two given
     * numbers
     *
     * @param i1 the first number
     * @param i2 the second number
     *
     * @return the numbers' greatest common denominator, or {@code NaN} if a StackOverflowError occurs <!--@throws
     * StackOverflowError sometimes, if the process goes on too long.-->
     *
     * @see #getGCDItr(long, long)
     */
    public static double getGCDRec(double i1, double i2)// throws StackOverflowError
    {
        try {
            if (i1 == i2 || i2 == 0) {
                return i1;
            }
            if (i1 == 0) {
                return i2;
            }
            if (i1 > i2) {
                return getGCDRec(i1 - i2, i2);
            }
            return getGCDRec(i1, i2 - i1);
        } catch (StackOverflowError so) {
            return Double.NaN;
        }
    }

    /**
     * Iteratively attempts to discover the <b>G</b>reatest <b>C</b>ommon <b>D</b>enominator between the two given
     * numbers
     *
     * @param i1 the first number
     * @param i2 the second number
     *
     * @return the numbers' greatest common denominator
     *
     * @see #getGCDRec(long, long)
     */
    public static double getGCDItr(double i1, double i2) {
        for (short i = 0; i < Short.MAX_VALUE && i1 != i2; i++) // Using Short.MAX_VALUE to ensure no infinite looping
        {
            while (i1 > i2) {
                i1 = i1 - i2;
            }
            while (i2 > i1) {
                i2 = i2 - i1;
            }
        }
        return i1;
    }
    /**
     * The {@code char} representation of the default radix point (Unicode character {@value } - '{@code .}')
     */
    public static final char RAD_POI = '.';

    // <editor-fold defaultstate="collapsed" desc="public static boolean isInRangeOf(number num, number[] num)">
    public static boolean isInRangeOf(byte num, byte... nums) {
        double[] l = new double[nums.length];
        System.arraycopy(nums, 0, l, 0, nums.length);
        return isInRangeOf((double) num, l);
    }

    public static boolean isInRangeOf(short num, short... nums) {
        double[] l = new double[nums.length];
        System.arraycopy(nums, 0, l, 0, nums.length);
        return isInRangeOf((double) num, l);
    }

    public static boolean isInRangeOf(int num, int... nums) {
        double[] l = new double[nums.length];
        System.arraycopy(nums, 0, l, 0, nums.length);
        return isInRangeOf((double) num, l);
    }

    public static boolean isInRangeOf(long num, long... nums) {
        double[] l = new double[nums.length];
        System.arraycopy(nums, 0, l, 0, nums.length);
        return isInRangeOf((double) num, l);
    }

    public static boolean isInRangeOf(float num, float... nums) {
        double[] l = new double[nums.length];
        System.arraycopy(nums, 0, l, 0, nums.length);
        return isInRangeOf((double) num, l);
    }

    public static boolean isInRangeOf(double num, double... nums) {
        double low = Double.MAX_VALUE;
        double high = Double.MIN_VALUE;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] < low) {
                low = nums[i];
            } else if (nums[i] > high) {
                high = nums[i];
            }
        }
        return num > low && num < high;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="public static boolean contains(number num, number[] nums)">
    public static boolean contains(byte num, byte... nums) {
        double[] l = new double[nums.length];
        System.arraycopy(nums, 0, l, 0, nums.length);
        return contains((double) num, l);
    }

    public static boolean contains(short num, short... nums) {
        double[] l = new double[nums.length];
        System.arraycopy(nums, 0, l, 0, nums.length);
        return contains((double) num, l);
    }

    public static boolean contains(int num, int... nums) {
        double[] l = new double[nums.length];
        System.arraycopy(nums, 0, l, 0, nums.length);
        return contains((double) num, l);
    }

    public static boolean contains(long num, long... nums) {
        double[] l = new double[nums.length];
        System.arraycopy(nums, 0, l, 0, nums.length);
        return contains((double) num, l);
    }

    public static boolean contains(float num, float... nums) {
        double[] l = new double[nums.length];
        System.arraycopy(nums, 0, l, 0, nums.length);
        return contains((double) num, l);
    }

    public static boolean contains(double num, double... nums) {
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == num) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates and returns whether {@code d1} is close to {@code d2} <h4>Examples:</h4> <UL>
     * <LI>{@code d1 == 5}, {@code d2 == 5} <UL><LI>returns {@code true}</LI></UL></LI>
     * <LI>{@code d1 == 5.0001}, {@code d2 == 5}
     * <UL><LI>returns {@code true}</LI></UL></LI> <LI>{@code d1 == 5}, {@code d2 == 5.0001} <UL><LI>returns
     * {@code true}</LI></UL></LI> <LI>{@code d1 == 5.24999}, {@code d2 == 5.25} <UL><LI>returns
     * {@code true}</LI></UL></LI>
     * <LI>{@code d1 == 5.25}, {@code d2 == 5.24999} <UL><LI>returns {@code true}</LI></UL></LI>
     * <LI>{@code d1 == 5}, {@code d2 == 5.1} <UL><LI>returns {@code false}</LI></UL></LI> </UL>
     *
     * @param d1
     * @param d2
     *
     * @return
     */
    public static boolean isCloseTo(double d1, double d2) {
        if (d1 == d2) {
            return true;
        }
        double t;
        String ds = Double.toString(d1);
        if (//extractDouble(ds.substring(0, ds.length()-1)) == d2 || extractDouble((ds = Double.toString(d2)).substring(0, ds.length()-1)) == d1 ||
                (t = teaseUp(d1 - 1)) == d2 || (t = teaseUp(d2 - 1)) == d1) {
            return true;
        }
        return false;
    }
    // </editor-fold>

    /**
     * Works like Double.parseDouble, but ignores any extraneous characters. The first radix point ({@code .}) is the
     * only one treated as such.<br/> <h4>Examples:</h4> <UL> <LI>{@code extractDouble("123456.789", false)} returns the
     * double value of {@code 123456.789}</LI> <LI>{@code extractDouble("1qw2e3rty4uiop[5a'6.p7u8&9", false)} returns
     * the double value of {@code 123456.789}</LI> <LI>{@code extractDouble("123,456.7.8.9", false)} returns the double
     * value of {@code 123456.789}</LI> <LI>{@code extractDouble("I have $9,862.39 in the bank.", false)} returns the
     * double value of {@code 9862.39}</LI> <LI>{@code extractDouble("12e6", true)} returns the double value of
     * {@code 12000000}</LI>
     * <LI>{@code extractDouble("2.5e1.6", true)} returns the double value of
     * {@code 99.526792638374312692563076271938}</LI>
     * </UL>
     *
     * @param str    The {@link CharSequence} from which to extract a {@code double}.
     * @param sciNot if {@code true}, this method will acknowledge scientific notation. For example, it will parse
     *               {@code "1.25e4"} as {@code 12500} rather than as {@code 1.254}
     *
     * @return the {@code double} that has been found within the string, if any.
     *
     * @throws NumberFormatException if {@code str} does not contain a digit between 0 and 9, inclusive.
     * @throws NullPointerException  if {@code str} is {@code null}
     */
    public static double extractDouble(CharSequence str, boolean sciNot) throws NumberFormatException {
        try {
            return Double.parseDouble(str.toString());
        } catch (Throwable t) {
            boolean r = true, m = false;
            String d = "";
            int mult = 1;
            for (int i = 0; i < str.length(); i++) {
                if (sciNot && (str.charAt(i) == 'E' || str.charAt(i) == 'e')) {
                    r = m = true;
                } else if (Character.isDigit(str.charAt(i)) || (str.charAt(i) == RAD_POI && r)) {
                    if (str.charAt(i) == RAD_POI && r) {
                        r = false;
                    }
                    if (sciNot && m) {
                        mult += str.charAt(i);
                    } else {
                        d += str.charAt(i);
                    }
                }
            }
            try {
                return Double.parseDouble(d) * Math.pow(10, mult);
            } catch (NumberFormatException ex) {
                throw new NumberFormatException("The input string could not be parsed to a double: " + str);
            }
        }
    }

    /**
     * Works like Double.parseDouble, but ignores any extraneous characters. The first radix point ({@code .}) is the
     * only one treated as such.<br/> <h4>Examples:</h4> <UL> <LI>{@code extractDouble("123456.789")} returns the double
     * value of {@code 123456.789}</LI> <LI>{@code extractDouble("1qw2e3rty4uiop[5a'6.p7u8&9")} returns the double value
     * of {@code 123456.789}</LI> <LI>{@code extractDouble("123,456.7.8.9")} returns the double value of
     * {@code 123456.789}</LI>
     * <LI>{@code extractDouble("I have $9,862.39 in the bank.")} returns the double value of {@code 9862.39}</LI>
     * <LI>{@code extractDouble("12e6")} returns the double value of {@code 126}</LI> </UL>
     *
     * @param str The {@link CharSequence} from which to extract a {@code double}.
     *
     * @return the {@code double} that has been found within the string, if any.
     *
     * @throws NumberFormatException if {@code str} does not contain a digit between 0 and 9, inclusive.
     */
    public static double extractDouble(CharSequence str) throws NumberFormatException {
        return extractDouble(str, false);
    }

    // <editor-fold defaultstate="collapsed" desc="public static String groupDigits(long t)">
    public static String groupDigits(double l) {
        return groupDigits(l, (byte) 3, ",");
    }

    public static String groupDigits(double num, byte size) {
        return groupDigits(num, size, ",");
    }

    public static String groupDigits(double num, String grouper) {
        return groupDigits(num, (byte) 3, grouper);
    }

    public static String groupDigits(double num, byte size, String grouper) {
        if (num < Math.pow(10, size) && num > 0) {
            return toPrettyString(num);
        }

        String ret = "", numStr = toPrettyString(num);
        boolean b = numStr.contains(RAD_POI + "");
        if (b) {
            ret = numStr.substring(numStr.indexOf(RAD_POI));
        }
        for (int i = 0, j = b ? numStr.indexOf(RAD_POI) - 1 : numStr.length() - 1/* ,
                 * t = b ? numStr.length() : j + 1 */;
                j >= 0; i++, j--) {
            if (!Character.isDigit(numStr.charAt(j))) {
                if (numStr.length() > j) {
                    ret += numStr.substring(j + 1);
                }
                continue;
            }
            ret = numStr.charAt(j) + (i % size == 0 && i != 0 ? grouper : "") + ret;
        }
        return ret;
    }
    // </editor-fold>

    /**
     * Formats the given number so that it is a certain length <h4>Examples</h4> <UL> <LI>{@code lenFmt(12, 4)} returns
     * {@code "0012"}</LI> <LI>{@code lenFmt(1234, 4)} returns {@code "1234"}</LI> <LI>{@code lenFmt(123456, 4)} returns
     * {@code "3456"}</LI> </UL>
     *
     * @param num the number to be formatted
     * @param len the length of the returned string
     *
     * @return the length-formatted number, as a string
     */
    public static String lenFmt(CharSequence num, int len) {
        if (num.length() >= len) {
            return num.subSequence(num.length() - len, num.length()).toString();
        }

        String ret = num.toString();
        for (int i = 0, l = len - num.length(); i < l; i++) {
            ret = '0' + ret;
        }
        return ret;
    }

    /**
     * Formats the given number so that it is a certain length <h4>Examples</h4> <UL> <LI>{@code lenFmt(12, 4)} returns
     * {@code "0012"}</LI> <LI>{@code lenFmt(1234, 4)} returns {@code "1234"}</LI> <LI>{@code lenFmt(123456, 4)} returns
     * {@code "3456"}</LI> </UL>
     *
     * @param num the number to be formatted
     * @param len the length of the returned string
     *
     * @return the length-formatted number, as a string
     */
    public static String lenFmt(long num, int len) {
        return lenFmt(Long.toString(num), len);
    }

    /**
     * Returns the number represented by the given {@code double} {@code num}, formatted to {@code fmt} decimal places:
     * <ol>
     * <LI>{@code X.0, 3} <UL><LI>Returns {@code "X.000"}</LI></UL></LI> <LI>{@code X.00000000Y, 3} <UL><LI>Returns
     * {@code "X.000"}</LI></UL></LI> <LI>{@code X.YZ, 3} <UL><LI>Returns {@code "X.YZ0"}</LI></UL></LI>
     * <LI>{@code X.0YZ, 4}
     * <UL><LI>Returns {@code "X.0YZ0"}</LI></UL></LI> <LI>{@code X.0YZABC, 3} <UL><LI>Returns
     * {@code "X.0YZ"}</LI></UL></LI>
     * <LI>{@code X.0YZABC, 0} <UL><LI>Returns {@code "X"}</LI></UL></LI> <LI>{@code ABC.DEF, 1} <UL><LI>Returns
     * {@code "ABC.D"}</LI></UL></LI> <LI>{@link Double.POSITIVE_INFINITY} <UL><LI>Returns {@code "8"}</LI></UL></LI>
     * <LI>{@link Double.NEGATIVE_INFINITY} <UL><LI>Returns {@code "-8"}</LI></UL></LI> <LI>{@link Double.NaN}
     * <UL><LI>Returns {@code "?"}</LI></UL></LI> </ol>
     *
     * @param num the number to be formatted
     * @param fmt the number of decimal places to ensure
     *
     * @return the formatted number
     *
     * @see #toPrettyDate(double)
     */
    public static double radFmt(double num, int fmt) {
        StringPP ret = new StringPP(Double.toString(extractDouble(Double.toString(num))));
        if (!ret.contains(RAD_POI)) {
            ret.append(RAD_POI, '0');
        }
        return Double.parseDouble(ret.substring(0, ret.lastIndexOf(RAD_POI) + fmt + 1).toString());
    }

    /**
     * Returns {@code true} if and only if <b>ALL</b> bits that are on in the second value are also on in the first
     * value
     * <h4>Examples</h4> <UL> <LI>when {@code value1} is {@code -55} ({@code 11001001}) and {@code value2} is
     * {@code 9} ({@code 00001001}), where the 5th and 8th bits are on in both values, and are the ONLY ones on in
     * {@code value2}, this will return {@code true}</LI> <LI>when {@code value1} is {@code -55} ({@code 11001001}) and
     * {@code value2} is {@code 45} ({@code 00101101}), where the 5th and 8th bits are on in both values, this will
     * return {@code false}</LI>
     * <LI>when {@code value1} is {@code -55} ({@code 11001001}) and {@code value2} is {@code 45} ({@code 00100100}),
     * where there are no bits on in either of the values, this will return {@code false}</LI> </UL>
     *
     * @param value       the value which carries the information bit(s)
     * @param bitsToCheck the value which carries the checking bit(s)
     *
     * @return {@code true} if and only if <b>ALL</b> bits that are on in the second value are also on in the first
     *         value
     */
    public static boolean allGivenBitsAreOn(int value, int bitsToCheck) {
        return (value & bitsToCheck) == bitsToCheck;
    }

    /**
     * Returns {@code true} if and only if <b>ANY</b> bits that are on in the second value are also on in the first
     * value
     * <h4>Examples</h4> <UL> <LI>when {@code value1} is {@code -55} ({@code 11001001}) and {@code value2} is
     * {@code 9} ({@code 00001001}), where the 5th and 8th bits are on in both values, and are the ONLY ones on in
     * {@code value2}, this will return {@code true}</LI> <LI>when {@code value1} is {@code -55} ({@code 11001001}) and
     * {@code value2} is {@code 45} ({@code 00101101}), where the 5th and 8th bits are on in both values, this will
     * return {@code true}</LI>
     * <LI>when {@code value1} is {@code -55} ({@code 11001001}) and {@code value2} is {@code 45} ({@code 00100100}),
     * where there are no bits on in either of the values, this will return {@code false}</LI> </UL>
     *
     * @param value       the value which carries the information bit(s)
     * @param bitsToCheck the value which carries the checking bit(s)
     *
     * @return {@code true} if and only if <b>ANY</b> bits that are on in the second value are also on in the first
     *         value
     */
    public static boolean anyGivenBitsAreOn(int value, int bitsToCheck) {
        return (value & bitsToCheck) != 0;
    }

    //<editor-fold defaultstate="collapsed" desc="Between">
    /**
     * Returns {@code num} if, and only if, it lies between the given limits. If the limits are equal, returns the limit
     * value. It does not matter which limit is higher.
     *
     * @param num    the number to be evaluated, and possibly returned
     * @param limit1 the first limit
     * @param limit2 the second limit
     *
     * @return {@code num} if it is between the limits, else the closest limit.
     */
    public static double between(double num, double limit1, double limit2) {
        if (limit1 == limit2) {
            return limit1;
        }
        return (limit1 > limit2) ? Math.min(max(num, limit2), limit1) : Math.min(max(num, limit1), limit2);
    }

    /**
     * Returns {@code num} if, and only if, it lies between the given limits. If the limits are equal, returns the limit
     * value. It does not matter which limit is higher.
     *
     * @param num    the number to be evaluated, and possibly returned
     * @param limit1 the first limit
     * @param limit2 the second limit
     *
     * @return {@code num} if it is between the limits, else the closest limit.
     */
    public static float between(float num, float limit1, float limit2) {
        return (float) between((double) num, limit1, limit2);
    }

    /**
     * Returns {@code num} if, and only if, it lies between the given limits. If the limits are equal, returns the limit
     * value. It does not matter which limit is higher.
     *
     * @param num    the number to be evaluated, and possibly returned
     * @param limit1 the first limit
     * @param limit2 the second limit
     *
     * @return {@code num} if it is between the limits, else the closest limit.
     */
    public static int between(int num, int limit1, int limit2) {
        return (int) between((double) num, limit1, limit2);
    }

    /**
     * Returns {@code num} if, and only if, it lies between the given limits. If the limits are equal, returns the limit
     * value. It does not matter which limit is higher.
     *
     * @param num    the number to be evaluated, and possibly returned
     * @param limit1 the first limit
     * @param limit2 the second limit
     *
     * @return {@code num} if it is between the limits, else the closest limit.
     */
    public static long between(long num, long limit1, long limit2) {
        return (long) between((double) num, limit1, limit2);
    }

    /**
     * Returns {@code num} if, and only if, it lies between the given limits. If the limits are equal, returns the limit
     * value. It does not matter which limit is higher.
     *
     * @param num    the number to be evaluated, and possibly returned
     * @param limit1 the first limit
     * @param limit2 the second limit
     *
     * @return {@code num} if it is between the limits, else the closest limit.
     */
    public static short between(short num, short limit1, short limit2) {
        return (short) between((double) num, limit1, limit2);
    }

    /**
     * Returns {@code num} if, and only if, it lies between the given limits. If the limits are equal, returns the limit
     * value. It does not matter which limit is higher.
     *
     * @param num    the number to be evaluated, and possibly returned
     * @param limit1 the first limit
     * @param limit2 the second limit
     *
     * @return {@code num} if it is between the limits, else the closest limit.
     */
    public static byte between(byte num, byte limit1, byte limit2) {
        return (byte) between((double) num, limit1, limit2);
    }
    //</editor-fold>

    /**
     * Returns this {@code double}'s "toPrettyDate" format:
     * <ol>
     * <LI>{@code X.0} ({@code d} is an integer)
     * <UL><LI>Returns {@code "X"}</LI></UL></LI>
     * <LI>{@code X.00000000Y} ({@code d} is close to an integer)
     * <UL><LI>Returns {@code "X"}</LI></UL></LI>
     * <LI>{@code X.YZ} ({@code d} is definitely not an integer)
     * <UL><LI>Returns {@code "X.YZ"}</LI></UL></LI>
     * <LI>{@link Double.POSITIVE_INFINITY} ({@code d} is infinitely large)
     * <UL><LI>Returns {@code "\u221E"}</LI></UL></LI>
     * <LI>{@link Double.NEGATIVE_INFINITY} ({@code d} is infinitely small)
     * <UL><LI>Returns {@code "-\u221E"}</LI></UL></LI>
     * <LI>{@link Double.NaN} ({@code d} is not a number)
     * <UL><LI>Returns {@code "?"}</LI></UL></LI>
     * <LI>{@code X.YZeA.BC} ({@code d} is in scientific notation)
     * <UL><LI>Returns {@code X.YZ×10^A.BC}</LI></UL></LI>
     * </ol>
     *
     * @param d the number being evaluated and hopefully prettified
     *
     * @return the hopefully prettified version of {@code d}
     */
    public static String toPrettyString(double d) {
        if (d == (long) d) {
            return I_FMT.format((long) d);
        }
        if (d == Double.POSITIVE_INFINITY) {
            return "\u221E";
        }
        if (d == Double.NEGATIVE_INFINITY) {
            return "-\u221E";
        }
        if (d == Double.NaN) {
            return "?";
        }
        StringPP s = new StringPP(Double.toString(d));
        if (s.containsIgnoreCase('e')) {
            Matcher m = SCINOT_PATTERN.matcher(s);
            String left = m.group(1);
            String right = m.group(2);
            return FMT.format(extractDouble(left)) + "×10^" + FMT.format(extractDouble(right));
        }
        return FMT.format(d);
    }
    private static final NumberFormat FMT = NumberFormat.getNumberInstance();
    private static final NumberFormat I_FMT = NumberFormat.getIntegerInstance();
    private static final Pattern SCINOT_PATTERN = Pattern.compile("(-?\\d+(\\.\\d+)?)e(-?\\d+(\\.\\d+)?)",
            Pattern.CASE_INSENSITIVE);

    /**
     * continually increases the value of the last digit in {@code d1} until the length of the double changes
     *
     * @param d1
     *
     * @return
     */
    public static double teaseUp(double d1) {
        String s = Double.toString(d1), o = s(s);//In order to evaluate the digits, let's look at it digit-by-digit.
        byte b;
//    double teaser;
//
//    {//Only keep teaserS in memory when it's needed
//      String teaserS = "1";
//      while (teaserS.length() < o.length() - 2)
//      {
//        teaserS = "0" + teaserS;
//      }
//      teaserS = "0." + teaserS;
//
//      teaser = Double.parseDouble(teaserS);
//    }

        for (byte c = 0; Double.toString(extractDouble(s)).length() >= o.length() && c < 100; c++) {
            //s = s.substring(0, s.length() - 1) + ((b = Byte.parseByte(Character.toString(s.charAt(s.length() - 1)))) == 9 ? 0 : b+1);
            boolean is9 = false;
            while (s.charAt(s.length() - 1) == '9') {
                is9 = true;
                s = s.substring(0, s.length() - 2) + ((b = Byte.parseByte(Character.toString(s.charAt(s.length() - 2))))
                        == 9 ? "" : b
                                + 1);
            }
            if (!Character.isDigit(s.charAt(s.length() - 1)))//if it ends in something like '.'
            {
                s = s.substring(0, s.length() - 1);//trim it so it's only numbers, again.
            }
            s = s.substring(0, s.length() - 1) + Byte.toString((byte) (Byte.parseByte(Character.toString(s.charAt(s
                    .length() - 1)))
                    + 1));

        }
        return extractDouble(s);
    }

    /**
     * Calculates and returns the average of all given numbers
     *
     * @param nums the numbers to average
     *
     * @return the average of all given numbers
     */
    public static double mean(Number... nums) {
        double ret = 0;
        for (Number d : nums) {
            ret += d.doubleValue();
        }
        return ret / nums.length;
    }

    /**
     * Works similarly to the {@code switch} statement. This method returns the given choice out of the given list of
     * choices. For instance, {@code choose(2, "Hello", "World!", "Foo", "Bar")} would return {@code "Foo"}
     *
     * @param choice  The index of the choice to return
     * @param choices The choices, one of which will be returned
     *
     * @return the object in {@code choices} at index {@code choice}
     *
     * @throws ArrayIndexOutOfBoundsException if the given index {@code choice} is less than {@code 0} or greater than
     *                                        or equal to the number of given choices
     */
    public static Object choose(int choice, Object... choices) {
        return choices[choice];
    }

    /**
     * Returns a "pretty" version of the given date.<br/> <h4>Examples</h4> <UL>
     * <LI>{@code toPrettyDate(1000, Calendar.SECOND)} produces {@code "1 second"}</LI>
     * <LI>{@code toPrettyDate(1851, Calendar.SECOND)} produces {@code "1 second"}</LI>
     * <LI>{@code toPrettyDate(1851, Calendar.MILLISECOND)} produces {@code "1 second, 851 milliseconds"}</LI>
     * <LI>{@code toPrettyDate(1000000, Calendar.SECOND)} produces {@code "16 minutes, 40 seconds"}</LI>
     * <LI>{@code toPrettyDate(10000000, Calendar.SECOND)} produces {@code "16 minutes, 40 seconds"}</LI>
     * <LI>{@code toPrettyDate(100000000, Calendar.SECOND)} produces {@code "2 hours, 46 minutes, 40 seconds"}</LI>
     * <LI>{@code toPrettyDate(1000000000, Calendar.SECOND)} produces
     * {@code "11 days, 13 hours, 46 minutes, 40 seconds"}</LI>
     * <LI>{@code toPrettyDate(1000000000, Calendar.DAY_OF_YEAR)} produces {@code "11 days"}</LI>
     * <LI>{@code toPrettyDate(86400000, Calendar.DAY_OF_YEAR)} produces {@code "1 day"}</LI>
     * <LI>{@code toPrettyDate(86400000, Calendar.MILLISECOND)} produces {@code "1 day"}</LI>
     * <LI>{@code toPrettyDate(86400001, Calendar.MILLISECOND)} produces {@code "1 day, 1 millisecond"}</LI>
     * <LI>{@code toPrettyDate(86400001, Calendar.DAY_OF_YEAR)} produces {@code "1 day"}</LI>
     * <LI>{@code toPrettyDate(43200000006L, Calendar.MILLISECOND)} produces
     * {@code "1 year, 134 days, 6 milliseconds"}</LI>
     * </UL>
     *
     * @param timeInMillis The time, in milliseconds, to be formatted
     * @param limit        the maximum precision (the smallest unit) to use, as defined by the following variables: <UL>
     * <LI>{@link Calendar#MILLISECOND}</LI> <LI>{@link Calendar#SECOND}</LI> <LI>{@link Calendar#MINUTE}</LI>
     * <LI>{@link Calendar#HOUR_OF_DAY}</LI> <LI>{@link Calendar#DAY_OF_YEAR}</LI> <LI>{@link Calendar#YEAR}</LI> </UL>
     *
     * @return a {@link CharSequence} which is a "pretty" representation of the given time
     *
     * @since Feb 22, 2012 (2.1.11)
     * @see Calendar
     */
    public static CharSequence toPrettyDate(double timeInMillis, int limit) {
        byte secs = (byte) ((timeInMillis / 1000) % 60),
                mins = (byte) ((timeInMillis / 1000 / 60) % 60),
                hrs = (byte) ((timeInMillis / 1000 / 60 / 60) % 24);
        short mils = (short) (timeInMillis % 1000),
                days = (short) ((timeInMillis / 1000 / 60 / 60 / 24) % 365.24219);
        long yrs = (long) (timeInMillis / 1000 / 60 / 60 / 24 / 365.24219);

        //TODO: make these booleans into one byte
        boolean useMs = (limit == Calendar.MILLISECOND),
                useS = (useMs || limit == Calendar.SECOND),
                useM = (useS || limit == Calendar.MINUTE),
                useH = (useM || limit == Calendar.HOUR_OF_DAY || limit == Calendar.HOUR),
                useD = (useH || limit == Calendar.DAY_OF_YEAR || limit == Calendar.DAY_OF_MONTH || limit
                == Calendar.DAY_OF_WEEK
                || limit == Calendar.DAY_OF_WEEK_IN_MONTH),
                useY = (useD || limit == Calendar.YEAR);
        /* X matters and (previous shows or the user wants X to show) */

        return ((useY = useY && yrs != 0)
                ? yrs + " year" + (yrs == 1 ? "" : "s")
                : "")
                + ((useD = useD && days != 0)
                        ? (useY ? ", " : "") + days + " day" + (days == 1 ? "" : "s")
                        : "")
                + ((useH = useH && hrs != 0)
                        ? ((useD || useY) ? ", " : "") + hrs + " hour" + (hrs == 1 ? "" : "s")
                        : "")
                + ((useM = useM && mins != 0)
                        ? ((useH || useD || useY) ? ", " : "") + mins + " minute" + (mins == 1 ? "" : "s")
                        : "")
                + ((useS = useS && secs != 0)
                        ? ((useM || useH || useD || useY) ? ", " : "") + secs + " second" + (secs == 1 ? "" : "s")
                        : "")
                + ((useMs && mils != 0)
                        ? (useS || useM || useH || useD || useY ? ", " : "") + mils + " millisecond"
                        + (mils == 1 ? "" : "s")
                        : "");
    }
    //<editor-fold defaultstate="collapsed" desc="Dim Behavior Flags">
    /**
     * Defines that this image should fit within its container upon resizing (if the container is taller or wider than
     * the image, then the image will stay the same scale so that the complete image is still visible). Equal to
     * {@value}.<hr>
     * Behaviors can be combined with the {@code |} (OR) operator
     */
    public static final byte DIM_BEHAVIOR_SCALE_FIT = 0b0100;
    /**
     * Defines that this image should fit within its container upon resizing (if the container is taller or wider than
     * the image, then the image will be scaled and trimmed so that the entire container is filled). Equal to
     * {@value}.<hr>
     * Behaviors can be combined with the {@code |} (OR) operator
     */
    public static final byte DIM_BEHAVIOR_SCALE_FILL = 0b1000;
    /**
     * Default image behavior. Equal to {@link #DIM_BEHAVIOR_SCALE_FIT} ({@value})
     *
     * @see #DIM_BEHAVIOR_SCALE_FIT
     */
    public static final byte DIM_BEHAVIOR_DEFAULT = DIM_BEHAVIOR_SCALE_FIT;
    //</editor-fold>

    /**
     * Returns non-zero scaled dimensions based on the given information
     *
     * @param objDim        the dimensions of the object to be scaled, as represented by a {@link java.awt.Dimension}
     *                      object
     * @param contDim       the dimensions of the object containing the object to be scaled, as represented by a
     *                      {@link java.awt.Dimension} object
     * @param BEHAVIOR_MASK How the method should behave while scaling the object dimensions. Can be any one of the
     *                      {@code DIM_BEHAVIOR_*} masks
     *
     * @see #DIM_BEHAVIOR_SCALE_FILL
     * @see #DIM_BEHAVIOR_SCALE_FIT
     * @return scaled dimensions based on the given information
     */
    public static Dimension getScaledDim(Dimension objDim, Dimension contDim, final byte BEHAVIOR_MASK) {
        double imageRatio = (double) objDim.width / (double) objDim.height,
                contRatio = (double) contDim.width / (double) contDim.height;
//    System.out.println("BHImageComp#getScaledDim(): image ratio:" + imageRatio + "; container ratio: " + contRatio);
//    System.out.println("BHImageComp#getScaledDim(Dimension " + imageDim + ", Dimension " + container + ", final byte " + BEHAVIOR_MASK + ")");
        Dimension ret = objDim.getSize();
        if ((BEHAVIOR_MASK & DIM_BEHAVIOR_SCALE_FIT) == DIM_BEHAVIOR_SCALE_FIT) // If the "Scale Fit" bit is on
        {
//      System.out.println("  Fit behavior");
            ret = new Dimension();
            if (contRatio < imageRatio)//If the image is wider than the container
            {
//        System.out.println("  Image is wider");
                ret.width = contDim.width;
                ret.height = (int) (contDim.width * (1 / imageRatio));
            } else {
//        System.out.println("  Image is taller");
                ret.height = contDim.height;
                ret.width = (int) (contDim.height * imageRatio);
            }
        } else if ((BEHAVIOR_MASK & DIM_BEHAVIOR_SCALE_FILL) == DIM_BEHAVIOR_SCALE_FILL) // If the "Scale Fill" bit is on
        {
//      System.out.println("  Fill behavior");
            ret = new Dimension();
            if (contRatio > imageRatio)//If the image is taller than the container
            {
//        System.out.println("  Image is taller");
                ret.width = contDim.width;
                ret.height = (int) (contDim.width * imageRatio);
            } else {
//        System.out.println("  Image is wider");
                ret.height = contDim.height;
                ret.width = (int) (contDim.height * imageRatio);
            }
        }
        if (ret.height <= 0) {
            ret.height = 1;
        }
        if (ret.width <= 0) {
            ret.width = 1;
        }
        return ret;
    }

    /**
     * Returns the bits stored within the given {@code long} from {@code fromIndex} (inclusive) to {@code toIndex}
     * (inclusive)
     *
     * @param value
     * @param fromIndex
     * @param toIndex
     *
     * @return
     */
    public static long getBits(long value, byte fromIndex, byte toIndex) {
        int d = Math.min(fromIndex, toIndex);//d is created to prefer speed over memory usage
        return (value & (((long) Math.pow(2, Math.abs(fromIndex - toIndex) + 1) - 1) << d)) >> d;
        /*
         * getBits(10662, 6, 2): (should return all bits from index 6 to 2, which are 01001 in binary and 9 in decimal)
         * value == 10662 == 0010 1001 1010 0110
         * fromIndex == 6
         * toIndex == 2
         * d == Math.min(6, 2) == 2
         *
         * Math.pow(2, Math.abs(6 - 2) + 1) - 1 == Math.pow(2, 5) - 1 == 31 == 0000 0000 0001 1111
         * (Math.pow(2, Math.abs(6 - 2) + 1) - 1) << d == 31 << 2 == 0000 0000 0001 1111 << 2 == 0000 0000 0111 1100
         * value & ((Math.pow(2, Math.abs(6 - 2) + 1) - 1) << d) == 10662 & 0000 0000 0111 1100 == 0010 1001 1010 0110 &
         * 0000 0000 0111 1100 == 0000 0000 0010 0100
         * (value & ((Math.pow(2, Math.abs(6 - 2) + 1) - 1) << d)) >> d == 0000 0000 0010 0100 >> 2 == 0000 0000 0000
         * 1001 == 01001 == 9
         * return 9
         */
 /*
         * getBits(10662, 3, 8): (should return all bits from index 8 to 3, which are 110100 in binary and 100 in
         * decimal)
         * value == 10662 == 0010 1001 1010 0110
         * fromIndex == 3
         * toIndex == 8
         * d == Math.min(3, 8) == 3
         *
         * Math.pow(2, Math.abs(3 - 8) + 1) - 1 == Math.pow(3, 6) - 1 == 31 == 0000 0000 0001 1111
         * (Math.pow(2, Math.abs(3 - 8) + 1) - 1) << d == 31 << 3 == 0000 0000 0001 1111 << 3 == 0000 0000 1111 1000
         * value & ((Math.pow(2, Math.abs(3 - 8) + 1) - 1) << d) == 10662 & 0000 0000 1111 1000 == 0010 1001 1010 0110 &
         * 0000 0000 1111 1000 == 0000 0000 1010 0000
         * (value & ((Math.pow(2, Math.abs(3 - 8) + 1) - 1) << d)) >> d == 0000 0000 1010 0000 >> 3 == 0000 0000 0001
         * 0100 == 10100 == 20
         * return 20
         */
    }
    /**
     * {@value}
     */
    public static final byte X = 1;

    /**
     * Creates a human-readable string that represents the given number of bytes <H4>Examples:</H4> <UL>
     * <LI>{@code toBytes(5)} returns "{@code 5 B}"</LI> <LI>{@code toBytes(1024)} returns "{@code 1 KB}"</LI>
     * <LI>{@code toBytes(2048)} returns "{@code 2 KB}"</LI> <LI>{@code toBytes(18236)} returns
     * "{@code 17.80859375 KB}"</LI>
     * <LI>{@code toBytes(5      * 1024 * 1024)} returns "{@code 5 MB}"</LI> <LI>{@code toBytes(8      * 1024 * 1024 * 1024)}
     * returns "{@code 8 GB}"</LI>
     * <LI>{@code toBytes(116L   * 1024 * 1024 * 1024 * 1024)} returns "{@code 116 TB}"</LI>
     * <LI>{@code toBytes(1L     * 1024 * 1024 * 1024 * 1024 * 1024)} returns "{@code 1 PB}"</LI>
     * <LI>{@code toBytes(32L    * 1024 * 1024 * 1024 * 1024 * 1024 * 1024)} returns "{@code 32 EB}"</LI>
     * <LI>{@code toBytes(256.0  * 1024 * 1024 * 1024 * 1024 * 1024 * 1024 * 1024)} returns "{@code 256 ZB}"</LI>
     * <LI>{@code toBytes(2.0    * 1024 * 1024 * 1024 * 1024 * 1024 * 1024 * 1024)} returns "{@code 1 YB}"</LI>
     * <LI>{@code toBytes(8000.0 * 1024 * 1024 * 1024 * 1024 * 1024 * 1024 * 1024)} returns "{@code 8000 YB}"</LI> </UL>
     * Anything higher than 1024<sup>8</sup> will be shown in terms of Yottabytes
     *
     * @param bytes the number of bytes to be interpreted. This is only in {@code double} format so that very large
     *              numbers (larger than the limit of a {@code long} ({@code 9223372036854775807L},
     *              {@code 0x7FFFFFFFFFFFFFFFL}, or 8&times;1024<sup>6</sup>))
     *
     * @return a {@link CharSequence} that represents the given number of bytes.
     */
    public static CharSequence toPrettyBytes(double bytes) {
        //<editor-fold defaultstate="collapsed" desc="Legacy code, taken from the old bht.tools.util.ProgLog. Kept for reference">
        /* if ((bytes / 1024) >= 1)
         * {
         * if (((bytes / 1024) / 1024) >= 1)
         * {
         * if ((((bytes / 1024) / 1024) / 1024) >= 1)
         * {
         * return Double.toString((double)bytes / (1024 * 1024 * 1024)).substring(0, Double.toString((double)bytes /
         * (1024 * 1024 * 1024)).indexOf(".") + 2) + " GB";
         * }
         * return Double.toString((double)bytes / (1024 * 1024)).substring(0, Double.toString((double)bytes / (1024 *
         * 1024)).indexOf(".") + 2) + " MB";
         * }
         * return ((double)bytes / 1024) + " KB";
         * }
         * return bytes + " B"; */
        //</editor-fold>
        long l1 = 0x7fffffffffffffffL;
        //Changed ">" to ">=" 2012/10/21 (2.1.14) for PiStory
        //Removed " " before names 2012/10/21 (2.1.14) for PiStory
        if (bytes >= 0x400)//1024 (Kilobyte)
        {
            if (bytes >= 0x100000)//1024^2 (Megabyte)
            {
                if (bytes >= 0x40000000)//1024^3 (Gigabyte)
                {
                    if (bytes >= 0x10000000000L)//1024^4 (Terabyte - Too big for an int. A long will have to handle it.)
                    {
                        if (bytes >= 0x4000000000000L)//1024^5 (Petabyte)
                        {
                            if (bytes >= 0x1000000000000000L)//1024^6 (Exabyte)
                            {
                                if (bytes >= 1180591620717411303424D)//1024^7 (Zettabyte - Too big for a long. Hopefully floating-point shenanegans can comprehend it.)
                                {
                                    if (bytes >= 1208925819614629174706176D)//1024^8 (Yottabyte)
                                    {
                                        return toPrettyString(bytes / 1208925819614629174706176D) + "YiB";//Yottabytes
                                    } else {
                                        return toPrettyString(bytes / 1180591620717411303424D) + "ZiB";//Zettabytes
                                    }
                                } else {
                                    return toPrettyString(bytes / 1152921504606846976.0) + "EiB";//Exabytes
                                }
                            } else {
                                return toPrettyString(bytes / 1125899906842624.0) + "PiB";//Petabytes
                            }
                        } else {
                            return toPrettyString(bytes / 1099511627776.0) + "TiB";//Terabytes
                        }
                    } else {
                        return toPrettyString(bytes / 1073741824.0) + "GiB";//Gigabytes
                    }
                } else {
                    return toPrettyString(bytes / 1048576.0) + "MiB";//Megabytes
                }
            } else {
                return toPrettyString(bytes / 1024.0) + "KiB";//Kilobytes
            }
        }
        return toPrettyString(bytes) + "B";//Bytes    //Added toPrettyString 2012/10/21 (2.1.14) for PiStory
    }

    /**
     * Return the maximum value of the given list of doubles as quickly as possible.<BR/> <STRONG>Special
     * Cases:</STRONG> <UL>
     * <LI>If the given set of values is null or empty, an exception is thrown</LI> <LI>If the given array only has one
     * value in it ({@code vals.length == 1}, then its only value is returned</LI> <LI>If the given array only has two
     * values in it ({@code vals.length == 2}, then they are given to {@link Math#max(double,
     *      double)}, and that result is returned.</LI> </UL>
     *
     * @param vals the list of values to compare to find the maximum
     *
     * @return the maximum value in the given list
     *
     * @throws NullPointerException     if {@code vals} is {@code null}
     * @throws IllegalArgumentException if {@code vals} is empty (has length {@code 0})
     * @see Math#max(double,double)
     * @since 2012/10/10 (2.1.13)
     */
    public static double max(double... vals) {
        if (vals == null) {
            throw new NullPointerException("cannot find the max of a null list");
        }
        if (vals.length == 0) {
            throw new IllegalArgumentException("cannot find the max of an empty list");
        }
        if (vals.length == 1) // don't do math if we don't have to
        {
            return vals[0];
        }
        if (vals.length == 2) // don't loop if we don't have to
        {
            return Math.max(vals[0], vals[1]);
        }

        double max = vals[0];
        for (int c = 1; c < vals.length; c++) {
            if (max < vals[c]) {
                max = vals[c];
            }
        }
        return max;
    }

    /**
     * Return the minimum value of the given list of doubles as quickly as possible.
     * <BR/>
     * <STRONG>Special Cases:</STRONG>
     * <UL>
     * <LI>If the given set of values is null or empty, an exception is thrown</LI>
     * <LI>If the given array only has one value in it ({@code vals.length == 1}), then its only value is returned</LI>
     * <LI>If the given array only has two values in it ({@code vals.length == 2}), then they are given to
     * {@link Math#min(double,double)}, and that result is returned.</LI>
     * </UL>
     *
     * @param vals the list of values to compare to find the minimum
     *
     * @return the minimum value in the given list
     *
     * @throws NullPointerException     if {@code vals} is {@code null}
     * @throws IllegalArgumentException if {@code vals} is empty (has length {@code 0})
     * @see Math#min(double,double)
     * @since 2012/10/11 (2.1.13)
     */
    public static double min(double... vals) {
        if (vals == null) {
            throw new NullPointerException("cannot find the min of a null list");
        }
        if (vals.length == 0) {
            throw new IllegalArgumentException("cannot find the min of an empty list");
        }
        if (vals.length == 1) // don't do math if we don't have to
        {
            return vals[0];
        }
        if (vals.length == 2) // don't loop if we don't have to
        {
            return Math.min(vals[0], vals[1]);
        }

        double min = vals[0];
        for (int c = 1; c < vals.length; c++) {
            if (min > vals[c]) {
                min = vals[c];
            }
        }
        return min;
    }

    /**
     * Return the sum of the given list of doubles as quickly as possible.<BR/> <STRONG>Special Cases:</STRONG> <UL>
     * <LI>If the given set of values is null or empty, an exception is thrown</LI> <LI>If the given array only has one
     * value in it ({@code vals.length == 1}, then its only value is returned</LI> <LI>If the given array only has two
     * values in it ({@code vals.length == 2}, then the sum of the two is returned.</LI> </UL>
     *
     * @param vals the list of values to summize
     *
     * @return the sum value in the given list
     *
     * @throws NullPointerException     if {@code vals} is {@code null}
     * @throws IllegalArgumentException if {@code vals} is empty (has length {@code 0})
     * @since 2012/10/10 (2.1.13)
     */
    public static double sum(double... vals) {
        if (vals == null) {
            throw new NullPointerException("cannot find the sum of a null list");
        }
        if (vals.length == 0) {
            throw new IllegalArgumentException("cannot find the sum of an empty list");
        }
        if (vals.length == 1) // don't do math if we don't have to
        {
            return vals[0];
        }
        if (vals.length == 2) // don't loop if we don't have to
        {
            return vals[0] + vals[1];
        }

        double sum = 0;
        for (double d : vals) {
            sum += d;
        }
        return sum;
    }

    /**
     * Return the number of values in the given list of doubles that are repeated. If there are no repeats, an empty
     * array is returned. The array returned is <EM>guaranteed</EM> to not have repeated values in it. This means that
     * {@code Numbers.repeats(Numbers.repeats(list))} will <EM>always</EM> return an empty array.<BR/> <STRONG>Special
     * Cases:</STRONG> <UL> <LI>If the given set of values is null or empty, an exception is thrown</LI> <LI>If the
     * given array only has one value in it ({@code vals.length == 1}, then its only value is returned</LI> <LI>If the
     * given array only has two values in it ({@code vals.length == 2}, then the sum of the two is returned.</LI> </UL>
     *
     * @param vals the list of values to summize
     *
     * @return the sum value in the given list
     *
     * @throws NullPointerException     if {@code vals} is {@code null}
     * @throws IllegalArgumentException if {@code vals} is empty (has length {@code 0})
     * @since 2012/10/10 (2.1.13)
     * @version 1.0.1 - 1.0.1 (2014-12-18) - Kyli Rouge commented out unnecessary continue; statement
     */
    public static double[] getDuplicates(double... vals) {
        if (vals == null) {
            throw new NullPointerException("cannot evaluate a null list");
        }
        if (vals.length == 0) {
            throw new IllegalArgumentException("cannot evaluate an empty list");
        }
        if (vals.length == 1) // don't do math if we don't have to
        {
            return new double[0];
        }
        if (vals.length == 2 && vals[0] == vals[1]) // don't loop if we don't have to
        {
            return new double[]{vals[0]};
        }

        ArrayPP<Double> repeats = new ArrayPP<>();
        int k = 0;
        for (int j = 1; j <= vals.length - 1; j++) {
            if (vals[j] == vals[j - 1]) {
                k++;
                repeats.addWithoutDuplicates(vals[j]);
                /* Vitrified 2012/10/11: Made unnecessary by addWithoutDuplicates. Also, it only worked if the list was
                 * sorted.
                 * do//We don't want repeat values in repeats
                 * j++;
                 * while(j < vals.length && vals[j] == vals[j-1]);
                 */
//				continue;
            }
        }

        double[] ret = new double[repeats.length()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = repeats.get(i);
        }
        return ret;
    }

    /**
     * Returns the <strong>location</strong> of the last occurrence of an even integer in the given list, or -1 if none
     * is encountered
     *
     * @param list a list of integers
     *
     * @return the <strong>location</strong> of the last occurrence of an even integer in the given list
     */
    public static int lastEven(double... list) {
        for (int c = list.length - 1; c >= 0; c++) {
            if (list[c] % 2 == 0) {
                return c;
            }
        }
        return -1;
    }

    /**
     * Finds the location of the first occurrence of the largest integer
     *
     * @param list list of numbers
     *
     * @return
     */
    public static int firstLargest(double... list) {
        double max = list[0];
        int location = 0;
        for (int c = 1; c < list.length; c++) {
            if (list[c] > max) {
                max = list[location = c];
            }
        }
        return location;
    }
    public static final byte SORT_BUBBLE_SORT = 0b0000_0000;

    /**
     * Returns a sorted version of the given list using the specified sorting method
     *
     * @param behavior
     * @param list
     *
     * @return
     */
    public static double[] sort(byte behavior, double... list) {
        double[] result = new double[list.length];
        System.arraycopy(list, 0, result, 0, list.length);
        switch (behavior) {
            case SORT_BUBBLE_SORT:
                double temp;
                for (int c1 = 0, l = list.length - 1; c1 < l; c1++) {
                    for (int c2 = c1; c2 < list.length; c2++) {
                        if (result[c2] > result[c2 + 1]) {
                            temp = result[c2];
                            result[c2] = result[c2 + 1];
                            result[c2 + 1] = temp;
                        }
                    }
                }
                break;
            default:
                throw new AssertionError("Invalid behavior flag: " + behavior);
        }
        return result;
    }
    /**
     * Defines the behavior of the {@link #round(byte, double)} method so that it returns the given number rounded up to
     * an integer
     */
    public static final byte ROUND_UP = 0b00;
    public static final byte ROUND_DOWN = 0b01;
    public static final byte ROUND_NEAREST = 0b10;

    public static int round(byte behavior, double number) {
        switch (behavior) {
            case ROUND_UP:
                return (int) ((int) number == number ? number : number + 1);
            case ROUND_DOWN:
                return (int) number;
            case ROUND_NEAREST:
                return (int) (number + 0.5);
            default:
                throw new AssertionError("Invalid behavior flag: " + behavior);
        }
    }

    /**
     * Returns the given subset of items in the list. This uses the PHP-style bounding model
     *
     * @param start If {@code start} is non-negative, the returned range will start at the {@code start}'th position,
     *              counting from zero. For instance, in the array {@code char[]{'a', 'b', 'c', 'd', 'e', 'f'}}, the
     *              character at position {@code 0} is {@code 'a'}, the character at position {@code 2} is {@code 'c'},
     *              and so forth.<br/>
     * If {@code start} is negative, the returned range will start at the {@code start}'th position from the end.<BR/>
     * <B>Example</B> of Using a negative start:      <PRE>
     * 	asPHPRange(6, -1);    // returns (5, 5)
     * 	asPHPRange(6, -2);    // returns (4, 5)
     * 	asPHPRange(6, -3, 1); // returns (3, 3)
     * </PRE>
     *
     * @param size  the number of items in the list
     *
     * @return the {@link Range} from {@code low} to {@code high}, as bounded like PHP
     *
     * @throws IndexOutOfBoundsException If {@code size} is less than or equal to {@code start}
     *
     * @see Any PHP functions such as http://www.php.net/manual/en/function.array-slice.php
     *
     * @since 2013-12-22 (2.2.0)
     * @version 1.0.0
     * @author Kyli Rouge of Blue Husky Studios
     */
    public static Range asPHPRange(int size, int start) {
        return asPHPRange(size, start, size);
    }

    /**
     * Returns the given subset of items in the list. This uses the PHP-style bounding model
     *
     * @param start If {@code start} is non-negative, the returned range will start at the {@code start}'th position,
     *               counting from zero. For instance, in the array {@code char[]{'a', 'b', 'c', 'd', 'e', 'f'}}, the
     *               character at position {@code 0} is {@code 'a'}, the character at position {@code 2} is {@code 'c'},
     *               and so forth.<br/>
     * If {@code start} is negative, the returned range will start at the {@code start}'th position from the end.<BR/>
     * <B>Example</B> of Using a negative start:      <PRE>
     * 	asPHPRange(6, -1);    // returns (5, 6)
     * 	asPHPRange(6, -2);    // returns (4, 6)
     * 	asPHPRange(6, -3, 1); // returns (3, 4)
     * </PRE>
     *
     * @param length If {@code length} is given and is <STRONG>positive</STRONG>, the range returned will contain at
     *               most {@code length} values beginning from {@code start} (depending on {@code size}).<BR/>
     * If {@code length} is given and is <STRONG>negative</STRONG>, then that many values will be omitted from the end
     * (after the {@code start} position has been calculated when a {@code start} is negative). If {@code start} denotes
     * the position of this truncation or beyond, {@code null} will be returned.<BR/>
     * If {@code length} is given and is {@code 0}, {@code null} will be returned.<BR/>
     * If {@code length} is omitted, the substring starting from start until the end of the string will be
     * returned.<BR/>
     * <STRONG>Example</STRONG> Using a negative length:      <PRE>
     * asPHPRange(6, 0, -1);  // returns (0, 5)
     * asPHPRange(6, 2, -1);  // returns (2, 5)
     * asPHPRange(6, 4, -4);  // returns null
     * asPHPRange(6, -3, -1); // returns (3, 5)
     * </PRE>
     *
     * @param size   the number of items in the list
     *
     * @return the {@link Range} from {@code low} to {@code high}, as bounded like PHP
     *
     * @throws IndexOutOfBoundsException If {@code size} is less than or equal to {@code start}
     *
     * @see Any PHP functions such as http://www.php.net/manual/en/function.array-slice.php
     *
     * @since 2013-12-22 (2.2.0)
     * @version 1.0.0
     * @author Kyli Rouge of Blue Husky Studios
     */
    public static Range asPHPRange(int size, int start, int length) {
        if (size <= start) {
            throw new IndexOutOfBoundsException("start (" + start + ") must be less than size (" + size + ")");
        }
        if (start < 0) {
            start += size;
        }
        if (start < 0) {
            start = 0;
        }

        if (length < 0) {
            length += size - start;
        }
        if (length >= size) {
            length = size - 1;
        } else if (length < 0) {
            length = 0;
        }

        return new Range(start, Math.min(start + length, size));
    }

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public static void main(String[] args) {
        //<editor-fold defaultstate="collapsed" desc="toPrettyDate">
        System.out.println("==== toPrettyDate ====");
        System.out.println("\"1 second\": \"" + toPrettyDate(1000, Calendar.SECOND) + "\"");//
        System.out.println("\"1 second\": \"" + toPrettyDate(1851, Calendar.SECOND) + "\"");//
        System.out.println("\"1 second, 851 milliseconds\": \"" + toPrettyDate(1851, Calendar.MILLISECOND) + "\"");//
        System.out.println("\"16 minutes, 40 seconds\": \"" + toPrettyDate(1000000, Calendar.SECOND) + "\"");//
        System.out.println("\"2 hours, 46 minutes, 40 seconds\": \"" + toPrettyDate(10000000, Calendar.SECOND) + "\"");//
        System.out.println("\"1 day, 3 hours, 46 minutes, 40 seconds\": \"" + toPrettyDate(100000000, Calendar.SECOND)
                + "\"");//
        System.out.println("\"11 days, 13 hours, 46 minutes, 40 seconds\": \"" + toPrettyDate(1000000000,
                Calendar.SECOND) + "\"");//
        System.out.println("\"11 days\": \"" + toPrettyDate(1000000000, Calendar.DAY_OF_YEAR) + "\"");//
        System.out.println("\"1 day\": \"" + toPrettyDate(86400000, Calendar.DAY_OF_YEAR) + "\"");//
        System.out.println("\"1 day\": \"" + toPrettyDate(86400000, Calendar.MILLISECOND) + "\"");//
        System.out.println("\"1 day, 1 millisecond\": \"" + toPrettyDate(86400001, Calendar.MILLISECOND) + "\"");//
        System.out.println("\"1 day\": \"" + toPrettyDate(86400001, Calendar.DAY_OF_YEAR) + "\"");//
        System.out.println("\"1 year, 134 days, 6 milliseconds\": \"" + toPrettyDate(43200000006L, Calendar.MILLISECOND)
                + "\"");//
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="getGCD">
        System.out.println("==== getGCDItr, getGCDRec ====");
        double x, y, e, i, r;
        System.out.println("GCD of " + (x = 8) + " and " + (y = 4) + " is " + (e = 4) + ": " + (((i = getGCDItr(x, y))
                - e) + ((r
                = getGCDRec(
                        x, y))
                - e)
                == 0 ? "confirmed!"
                        : "discrepency! (itr = "
                        + i
                        + " and, rec = " + r
                        + ")"));

        System.out.println("GCD of " + (x = 875) + " and " + (y = 125) + " is " + (e = 125) + ": " + (((i = getGCDItr(x,
                y))
                - e)
                + ((r = getGCDRec(
                        x, y)) - e)
                == 0 ? "confirmed!"
                        : "discrepency! (itr = "
                        + i
                        + " and, rec = "
                        + r + ")"));

        System.out.println("GCD of " + (x = 300) + " and " + (y = 588) + " is " + (e = 12) + ": " + (((i = getGCDItr(x,
                y))
                - e)
                + ((r = getGCDRec(x,
                        y))
                - e)
                == 0 ? "confirmed!"
                        : "discrepency! (itr = "
                        + i
                        + " and, rec = "
                        + r + ")"));

        System.out.println("GCD of " + (x = 123) + " and " + (y = 456) + " is " + (e = 3) + ": "
                + (((i = getGCDItr(x, y)) - e)
                + ((r = getGCDRec(x, y)) - e)
                == 0 ? "confirmed!"
                        : "discrepency! (itr = " + i
                        + " and, rec = " + r + ")"));

        System.out.println("GCD of " + (x = 3141592654L) + " and " + (y = 2718281828L) + " is " + (e = 2) + ": "
                + (((i = getGCDItr(
                        x, y)) - e)
                + ((r
                = getGCDRec(x,
                        y))
                - e) == 0
                        ? "confirmed!"
                        : "discrepency! (itr = "
                        + i
                        + " and, rec = "
                        + r + ")"));

        System.out.println("GCD of " + (x = 123456789L) + " and " + (y = 987654321L) + " is " + (e = 9) + ": "
                + (((i = getGCDItr(x,
                        y))
                - e) + ((r
                = getGCDRec(
                        x, y))
                - e) == 0
                        ? "confirmed!"
                        : "discrepency! (itr = "
                        + i
                        + " and, rec = "
                        + r + ")"));
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="lists">
        final double[] vals = new double[]{-2, 8, 3, -7, 5, 0.0, -0.0, 10, 5, 1, 2};

        System.out.println("==== Evaluating lists ====");
        System.out.println("List which is being evaluated: " + Arrays.toString(vals));
        System.out.println("=== sum ===");
        System.out.println(sum(vals));
        System.out.println("=== max ===");
        System.out.println(max(vals));
        System.out.println("=== min ===");
        System.out.println(min(vals));
        System.out.println("=== getDuplicates ===");
        System.out.println(Arrays.toString(getDuplicates(vals)));
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="asPHPRange">
        {
            System.out.println("==== asPHPRange ====");
            String test = "abcdef";
            Range range;
            System.out.println("     f: " + test.substring((range = asPHPRange(test.length(), -1)).low, range.high)); // returns "f"
            System.out.println("    ef: " + test.substring((range = asPHPRange(test.length(), -2)).low, range.high)); // returns "ef"
            System.out.println("     d: " + test.substring((range = asPHPRange(test.length(), -3, 1)).low, range.high)); // returns "d"
            System.out.println(" abcde: " + test.substring((range = asPHPRange(test.length(), 0, -1)).low, range.high)); // returns "abcde"
            System.out.println("   cde: " + test.substring((range = asPHPRange(test.length(), 2, -1)).low, range.high)); // returns "cde"
            System.out.println("  null: " + test.substring((range = asPHPRange(test.length(), 4, -4)).low, range.high)); // returns null
            System.out.println("    de: " + test.substring((range = asPHPRange(test.length(), -3, -1)).low, range.high)); // returns "de"
        }
        //</editor-fold>
    }

    /**
     * Clamps the given value between the given minimum and maximum
     *
     * @param value the value to clamp
     * @param min   the minimum value
     * @param max   the maximum value
     *
     * @return {@code min} if {@code value} is lower than it, else {@code max} if {@code value} is higher than that,
     *         else {@code value}
     *
     * @since 2014-12-18 (2.2.1) for bht.test.tools.util.Half}
     * @author Kyli Rouge
     * @version 1.0.0
     */
    public static byte clamp(byte value, byte min, byte max) {
        return value < min
                ? min
                : value > max
                        ? max
                        : value;
    }

    /**
     * Clamps the given value between the given minimum and maximum
     *
     * @param value the value to clamp
     * @param min   the minimum value
     * @param max   the maximum value
     *
     * @return {@code min} if {@code value} is lower than it, else {@code max} if {@code value} is higher than that,
     *         else {@code value}
     *
     * @since 2014-12-18 (2.2.1) for bht.test.tools.util.Half
     * @author Kyli Rouge
     * @version 1.0.0
     */
    public static double clamp(double value, double min, double max) {
        return value < min
                ? min
                : value > max
                        ? max
                        : value;
    }

    public static boolean isIntegerOfSize(CharSequence text, int bitCount) {
        if (text == null) {
            return false;
        }
        String textString = text.toString();
        long longValue;
        try {
            longValue = Long.valueOf(textString);
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
        return (longValue & (1 << bitCount - 1)) == longValue;
    }

    public static boolean isByte(CharSequence text) {
        return isIntegerOfSize(text, Byte.SIZE);
    }

    public static boolean isShort(CharSequence text) {
        return isIntegerOfSize(text, Short.SIZE);
    }

    public static boolean isInt(CharSequence text) {
        return isIntegerOfSize(text, Integer.SIZE);
    }

    public static boolean isLong(CharSequence text) {
        return isIntegerOfSize(text, Long.SIZE);
    }

    /**
     * Returns a byte array version of {@code bytes}. The resulting array is {@link Long#BYTES} slots long
     * ({@value Long#BYTES}) and each slot represents the bits in the original {@code long}, in the proper order.
     *
     * @param bytes the {@code long} to convert into an {@value Long#BYTES}-{@code byte} array.
     *
     * @return an {@value Long#BYTES}-{@code byte} array version of {@code bytes}.
     */
    public static byte[] longToByteArray(long bytes) {
//        ByteBuffer.allocate(Long.SIZE).putLong(bytes).array();
        byte[] ret = new byte[Long.BYTES];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = (byte) (bytes >> (ret.length - i - 1 << 3));
        }
        return ret;
    }

    /**
     * Creates a {@code long} made up of the given 8 bytes.
     *
     * @param b7 the most-significant byte
     * @param b6 the 2<sup>nd</sup>-most, 7<sup>th</sup>-least significant byte
     * @param b5 the 3<sup>rd</sup>-most, 6<sup>th</sup>-least significant byte
     * @param b4 the 4<sup>th</sup>-most, 5<sup>th</sup>-least significant byte
     * @param b3 the 4<sup>th</sup>-least, 5<sup>th</sup>-most significant byte
     * @param b2 the 3<sup>rd</sup>-least, 6<sup>th</sup>-most significant byte
     * @param b1 the 2<sup>nd</sup>-least, 7<sup>th</sup>-most significant byte
     * @param b0 the least-significant byte
     *
     * @return a {@code long} made up of the given 8 bytes.
     */
    public static long longFromBytes(byte b7, byte b6, byte b5,
            byte b4, byte b3, byte b2,
            byte b1, byte b0) {
        // I've never used octal constants before :D
        return (((long) b7 << 070L)
                | ((b6 & 0xFFL) << 060)
                | ((b5 & 0xFFL) << 050)
                | ((b4 & 0xFFL) << 040)
                | ((b3 & 0xFFL) << 030)
                | ((b2 & 0xFFL) << 020)
                | ((b1 & 0xFFL) << 010)
                | ((b0 & 0xFFL)));
    }

    public static class IrrationalRatioException extends Exception {

        public IrrationalRatioException(Throwable cause) {
            super(cause);
        }

        public IrrationalRatioException(String message, Throwable cause) {
            super(message, cause);
        }

        public IrrationalRatioException(String message) {
            super(message);
        }

        public IrrationalRatioException() {
            super();
        }
    }

    /**
     * Represents a fraction.
     */
    public static class Fraction {

        private double t, b;
        private char s;

        public Fraction(double top, double bottom) {
            this(top, bottom, '/');
        }

        public Fraction(double top, double bottom, char separator) {
            t = top;
            b = bottom;
            s = separator;
        }

        //<editor-fold defaultstate="collapsed" desc="Getters">
        public double getTop() {
            return t;
        }

        public double getBottom() {
            return b;
        }

        public char getSeparator() {
            return s;
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Setters">
        public void setTop(double newTop) {
            t = newTop;
        }

        public void setBottom(double newBottom) {
            b = newBottom;
        }

        public void setSeparator(char separator) {
            s = separator;
        }
        //</editor-fold>

        /**
         * Returns the result of dividing the left side divided by the right side
         *
         * @return the result of dividing the left side divided by the right side
         */
        public double toDouble() {
            return t / b;
        }

        @Override
        public String toString() {
            return toPrettyString(t) + s + toPrettyString(b);
        }
    }

    public static class Ratio extends Fraction {

        public Ratio(double left, double right) {
            super(left, right, ':');
        }
    }

    public static class Range {

        int low, high;

        public Range(int initLow, int initHigh) {
            low = initLow;
            high = initHigh;
        }

        public int getLow() {
            return low;
        }

        public Range setLow(int newLow) {
            low = newLow;
            return this;
        }

        public int getHigh() {
            return high;
        }

        public Range setHigh(int newHigh) {
            high = newHigh;
            return this;
        }

        public int getLength() {
            return Math.abs(high - low);
        }

        @Override
        @SuppressWarnings("CloneDoesntCallSuperClone")
        protected Range clone() throws CloneNotSupportedException {
            return new Range(low, high);
        }

        @Override
        public String toString() {
            return low + " to " + high;
        }
    }

}
