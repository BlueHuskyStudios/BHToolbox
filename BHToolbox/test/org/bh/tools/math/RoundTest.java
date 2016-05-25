package org.bh.tools.math;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.bh.tools.math.NumberConverter.*;
import static org.junit.Assert.assertEquals;

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for BHToolbox
 *
 * @author Kyli Rouge
 * @since 2016-05-24
 */
public class RoundTest {
	private static final BigDecimal NICKEL  = toReal("0.05");
	private static final BigDecimal QUARTER = toReal("0.25");
	private static final BigDecimal TWENTY  = toReal("20.00");

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void round() throws Exception {
		// basic test; if this doesn't pass then something's seriously wrong:
		assertEquals(toReal(123.45), Round.round(123.454, (short) 2, RoundingMode.HALF_UP));
		assertEquals(toReal(123.46), Round.round(123.455, (short) 2, RoundingMode.HALF_UP));
		assertEquals(toReal(123.46), Round.round(123.456, (short) 2, RoundingMode.HALF_UP));

		// if a 5¢ coin is the smallest denomination (like in Canada):
		assertEquals(toReal("1.00"), Round.roundTier(1.02, NICKEL));
		assertEquals(toReal("1.05"), Round.roundTier(1.03, NICKEL));
		assertEquals(toReal("1.30"), Round.roundTier(1.29, NICKEL));
		assertEquals(toReal("1.30"), Round.roundTier(1.30, NICKEL));

		// if a 25¢ coin is the smallest denomination (like in bars):
		assertEquals(toReal("1.00"), Round.roundTier(1.12, QUARTER));
		assertEquals(toReal("1.25"), Round.roundTier(1.13, QUARTER));
		assertEquals(toReal("3.75"), Round.roundTier(3.65, QUARTER));
		assertEquals(toReal("3.75"), Round.roundTier(3.80, QUARTER));

		// Or if $20 is the cutoff:
		assertEquals(toReal("0.00"), Round.roundTier(9.99, TWENTY));
		assertEquals(toReal("20.00"), Round.roundTier(10.00, TWENTY));
		assertEquals(toReal("40.00"), Round.roundTier(39.99, TWENTY));
		assertEquals(toReal("40.00"), Round.roundTier(40.00, TWENTY));
	}


}