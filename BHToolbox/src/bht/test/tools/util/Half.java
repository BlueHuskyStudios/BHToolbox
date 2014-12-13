package bht.test.tools.util;

/**
 * 
 * @author Kyli of Blue Husky Studios
 * @version 1.0.0
 * @since Jul 21, 2012
 * 
 * @deprecated not yet complete (or compilable)
 */
public class Half extends Number implements CompleteObject, ToPrimitives
{
	public static final byte MAX_EXPONENT = 0b11111; // since we only have a 5-bit exponent, this is our max
	
	private short h = (short)0x0000;
	
	public Half(short rawBits)
	{
		h = rawBits;
	}
	public half(float initValue)
	{// thanks to http://javastack.tumblr.com/post/13740168684/extracting-sign-exponent-and-mantissa for the algorithm
		int fbits = Float.floatToIntBits(-0.005f);
		h |= fbits >>> 31 << 15; // set the sign
		h |= Math.max((fbits >>> 23 & ((1 << 8) - 1)) - ((1 << 7) - 1), MAX_EXPONENT) << 10; // set the exponent
		h |= Math.max(fbits & ((1 << 23) - 1), MAX_MANTISSA); // set the mantissa
	}
}
