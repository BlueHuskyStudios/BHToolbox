package org.bh.tools.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.bh.tools.math.NumberConverter.*;

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for BHToolbox.
 * <p>
 * For rounding numbers
 *
 * @author Kyli Rouge
 * @since 2016-05-24
 */
@SuppressWarnings("WeakerAccess")
public class Round {

	private Round() {}

	/**
	 * Rounds the given number {@code n} to the given precision using the given rounding mode and tier.
	 *
	 * @param n    The number to round.
	 * @param tier The tier to which to round. For instance, if calculating cash needed when there are nickels but no
	 *             pennies, use {@code 0.05}.
	 */
	public static Number roundTier(Number n, Number tier) {
		return round(n, RoundingMode.HALF_UP, tier);
	}

	/**
	 * Rounds the given number {@code n} to the given precision using the given rounding mode and tier. Formula thanks
	 * to <a href="https://math.stackexchange.com/q/1798899/26772">Math StackExchange</a>.
	 *
	 * @param n    The number to round.
	 * @param mode The mode by which to round the number.
	 * @param tier The tier to which to round. For instance, if calculating cash needed when there are nickels but no
	 *             pennies, use {@code 0.05}.
	 *
	 * @see <a href="https://math.stackexchange.com/q/1798899/26772">What is the function for “round n to the nearest m”?</a>
	 */
	public static Number round(Number n, RoundingMode mode, Number tier) {
		BigDecimal nDecimal    = toReal(n);
		BigDecimal tierDecimal = toReal(tier);
		return nDecimal
				.divide(tierDecimal, 0, mode)
				.multiply(tierDecimal)
				.setScale(tierDecimal.scale(), mode);
	}

	/**
	 * Rounds the given number {@code n} to the given precision using the given rounding mode and tier.
	 *
	 * @param n         The number to round.
	 * @param precision How precise the result should be (where {@code 0} is whole numbers, {@code 2} is hundredths,
	 *                  {@code -2} is hundreds, etc.).
	 */
	public static Number round(Number n, short precision) {
		return round(n, precision, RoundingMode.HALF_UP);
	}

	/**
	 * Rounds the given number {@code n} to the given precision using the given rounding mode and tier.
	 *
	 * @param n         The number to round.
	 * @param precision How precise the result should be (where {@code 0} is whole numbers, {@code 2} is hundredths,
	 *                  {@code -2} is hundreds, etc.).
	 * @param mode      The mode by which to round the number.
	 */
	public static Number round(Number n, short precision, RoundingMode mode) {
		return toReal(n).setScale(precision, mode);
	}
}
