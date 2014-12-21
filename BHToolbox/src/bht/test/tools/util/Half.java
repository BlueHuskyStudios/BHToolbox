package bht.test.tools.util;

import bht.tools.misc.CompleteObject;
import bht.tools.misc.ToPrimitives;
import bht.tools.util.math.Numbers;

/**
 * 16 bits of unnecessary floating-point badness.
 * 
 * @see IEEE floating-point standard. This adheres to that.
 * 
 * @author Kyli of Blue Husky Studios
 * @version 1.0.0
 * @since Jul 21, 2012
 *
 * @deprecated full compatibility with 32-bit float not yet realized
 */
public class Half extends Number implements CompleteObject, ToPrimitives
{
	public static final byte EXPONENT_BITS = 0x1F;
	public static final short EXPONENT_MASK = EXPONENT_BITS << 8;
	public static final byte MAX_EXPONENT = 15;
	public static final byte MIN_EXPONENT = -15;
	public static final short MANTISSA_BITS = 0x3FF;
	public static final short MANTISSA_MASK = MANTISSA_BITS;
	public static final short MAX_MANTISSA = 0x3FF;
	public static final short MIN_MANTISSA = 0x000;
	public static final Half INFINITY          = new Half((short)0x7C00);
	public static final Half NEGATIVE_INFINITY = new Half((short)0xFC00);
	public static final Half MAX_VALUE         = new Half((short)0x7BFF);
	public static final Half MIN_VALUE         = new Half((short)0xFBFF);
	public static final Half NaN               = new Half((short)0xFFFF);

	private short h = 0;

	
	/**
	 * Creates a new half using the given 16 bits
	 * @param rawBits the initial bits
	 * 
	 * @author Kyli Rouge
	 * @since 2014-12-14
	 * @version 1.0.0
	 */
	public Half(short rawBits)
	{
		h = rawBits;
	}
	
	/**
	 * Creates a new half using the given preexisting value
	 * @param initValue the initial value
	 * 
	 * @author Kyli Rouge
	 * @since 2014-12-14
	 * @version 1.0.0
	 */
	@SuppressWarnings({"UseOfSystemOutOrSystemErr", "OverridableMethodCallInConstructor"})
	public Half(float initValue) // must refine
	{
//		System.out.println("new Half(float " + initValue + ") {");
		int fbits = Float.floatToIntBits(initValue);
//		System.out.println("\tfbits == 0x" + Integer.toHexString(fbits));
		setSign((fbits & 0x8000_0000) == 0x8000_0000);
		System.out.println("\tsign == 0x" + Integer.toHexString((fbits & 0x8000_0000) >>> 16));
//		h |= Math.min((fbits & 0x7F80_0000) >> 23, MAX_EXPONENT) << 10; // set the exponent
		setExponent((byte)((fbits & 0x7F80_0000) >> 23));
		System.out.println("\texp == Math.min((0x" + Integer.toHexString(fbits) + " & 0x7F80_0000) >> 23, 0x" + Integer.toHexString(MAX_EXPONENT) + ") << 10");
		System.out.println("\texp == Math.min((0x" + Integer.toHexString(fbits & 0x7F80_0000) + ") >> 23, 0x" + Integer.toHexString(MAX_EXPONENT) + ") << 10");
		System.out.println("\texp == Math.min(0x" + Integer.toHexString((fbits & 0x7F80_0000) >> 23) + ", 0x" + Integer.toHexString(MAX_EXPONENT) + ") << 10");
		System.out.println("\texp == 0x" + Integer.toHexString(Math.min(((fbits & 0x7F80_0000) >> 23), MAX_EXPONENT)) + " << 10");
		System.out.println("\texp == 0x" + Integer.toHexString(Math.min(((fbits & 0x7F80_0000) >> 23), MAX_EXPONENT) << 10));
		h |= Math.min(fbits & (0x7F_FFFF), MAX_MANTISSA); // set the mantissa
		System.out.println("\tmtsa == Math.min(0x" + Integer.toHexString(fbits) + " & (0x7F_FFFF), 0x" + Integer.toHexString(MAX_MANTISSA) + ")");
		System.out.println("\tmtsa == Math.min(0x" + Integer.toHexString(fbits & (0x7F_FFFF)) + ", 0x" + Integer.toHexString(MAX_MANTISSA) + ")");
		System.out.println("\tmtsa == 0x" + Integer.toHexString(Math.min(fbits & (0x7F_FFFF), MAX_MANTISSA)));
		System.out.println("}");
	}
	
	/**
	 * Creates a new half using the given preexisting value
	 * @param initValue the initial value
	 * 
	 * @author Kyli Rouge
	 * @since 2014-12-14
	 * @version 1.0.0
	 */
	public Half(Half initValue)
	{
		this(initValue.h);
	}
	
	/**
	 * Returns true iff this is negative. This checks to see if the 15th bit (most significant) is 1
	 * @return true iff this is negative.
	 * 
	 * @author Kyli Rouge
	 * @since 2014-12-16
	 * @version 1.0.0
	 */
	public boolean getSign()
	{
		return (h & 0x8000) == 0x8000;
	}
	/**
	 * Sets the new sign. Sign will be negative iff {@code newSign} is {@code true}
	 * @param newSign the new sign
	 * @return {@code this}
	 * 
	 * @author Kyli Rouge
	 * @since 2014-12-18
	 * @version 1.0.0
	 */
	public Half setSign(boolean newSign)
	{
		if (newSign)
			h |= 0x8000_0000;
		else
			h &= 0x7FFF_FFFF;
		return this;
	}
	
	/**
	 * Returns the bits of the exponent (bits 14 - 10)
	 * @return the bits of the exponent
	 * 
	 * @author Kyli Rouge
	 * @since 2014-12-16
	 * @version 1.0.0
	 */
	public byte getExponentBits()
	{
		return (byte)((h & 0x7C00) >> 10); // get only bits 14 - 10, and shift them over 10 so they're 5 - 0
	}
	/**
	 * Returns the value of the exponent (bits 14 - 10)
	 * @return the value of the exponent
	 */
	public byte getExponent()
	{
		byte exp = getExponentBits();
		if ((exp & 0x40) == 0x40) // if it's negative
			return (byte)-(exp & 0x3F); // that's converting a negative to a byte, not subtracting something from the byte type
		return (byte)(exp & 0x3F);
	}
	
	/**
	 * Uses the given bits as the new exponent. Only bits 6 - 0 are used.
	 * @param newBits the bits to use as the new exponent
	 * @return {@code this}
	 * 
	 * @author Kyli Rouge
	 * @since 2014-12-18
	 * @version 1.0.0
	 */
	public Half setExponentBits(byte newBits)
	{
		h &= (newBits & 0x7F) << 8;
		return this;
	}
	/**
	 * Uses the given value as the new exponent. This is clamped to {@link #MAX_EXPONENT} and {@link #MIN_EXPONENT}.
	 * @param newExponent the value to use as the new exponent
	 * @return {@code this}
	 * 
	 * @author Kyli Rouge
	 * @since 2014-12-18
	 * @version 1.0.0
	 */
	public Half setExponent(byte newExponent)
	{
		newExponent = Numbers.clamp(newExponent, MIN_EXPONENT, MAX_EXPONENT); // make sure the exponent is between the required positions
		if (newExponent < 0) // if it's negative
			newExponent |= 0x40; // put a sign in the 6th bit
		else
			newExponent &= 0x3F; // remove any sign in the 6th bit
		setExponentBits(newExponent);// now put this into the proper place
		return this;
	}
	
	/**
	 * Returns the value of the mantissa (the last 15 bits)
	 * @return the value of the mantissa
	 */
	public short getMantissa()
	{
		return (short)(h & 0x3FF);
	}

	@Override
	public int intValue()
	{
		return (int) floatValue();
	}

	@Override
	public long longValue()
	{
		return (long)doubleValue();
	}

	@Override
	public float floatValue()
	{
		return Float.intBitsToFloat(
			((h & 0x8000)    << 16) | // move sign to bit 31
			((getExponent()) << 13) | // move exponent to bits 27 - 23
			(getMantissa())
		);
	}

	@Override
	public double doubleValue()
	{
		return (double)floatValue();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return {@code false} if the value is {@code 0}, {@code true} otherwise.
	 */
	@Override
	public boolean toBoolean()
	{
		return h != 0;
	}

	@Override
	public byte toByte()
	{
		return byteValue();
	}

	@Override
	public char toChar()
	{
		return (char) intValue();
	}

	@Override
	public double toDouble()
	{
		return doubleValue();
	}

	@Override
	public float toFloat()
	{
		return floatValue();
	}

	@Override
	public int toInt()
	{
		return intValue();
	}

	@Override
	public long toLong()
	{
		return longValue();
	}

	@Override
	public short toShort()
	{
		return shortValue();
	}

	@Override
	@SuppressWarnings("FinalizeDeclaration")
	public void finalize() throws Throwable
	{
		super.finalize();
	}

	@Override
	@SuppressWarnings({"CloneDoesntCallSuperClone", "CloneDeclaresCloneNotSupported"})
	public Half clone()
	{
		return new Half(this);
	}
	
	public boolean isNaN()
	{
//		System.out.println("\tisNaN(){ (" + Integer.toHexString(h) + " & 0x7C00) == 0x7C00 && (" + Integer.toHexString(h) + " & 0x3FF) != 0");
//		System.out.println("\tisNaN(){ (" + Integer.toHexString(h & 0x7C00) + ") == 0x7C00 && (" + Integer.toHexString(h & 0x3FF) + ") != 0");
		return (h & 0x7C00) == 0x7C00 && (h & 0x3FF) != 0;
	}
	public boolean isInfinite()
	{
		return (h & 0x7FFF) == 0x7C00;
	}
	public boolean isPositive()
	{
		return (h & 0x8000) == 0;
	}

	@Override
	public String toString()
	{
		return
			isNaN()
				? "NaN"
				: isInfinite()
					? (isPositive() ? "" : '-') + "Infinity"
					: Float.toString(toFloat())
		;
	}
	
	@SuppressWarnings("UseOfSystemOutOrSystemErr")
	public static void main(String[] args)
	{                                               // expected output:
		System.out.println(Half.INFINITY);          // Infinity
		System.out.println(Half.MAX_VALUE);         // 
		System.out.println(Half.MIN_VALUE);         // 
		System.out.println(Half.NEGATIVE_INFINITY); // -Infinity
		System.out.println(Half.NaN);               // NaN
		System.out.println(new Half(1f));           // 1
		System.out.println(new Half(12f));          // 12
		System.out.println(new Half(123f));         // 123
		System.out.println(new Half(1234f));        // 1234
		System.out.println(new Half(-1234f));       // -1234
		System.out.println(new Half(12.34f));       // 12.34
		System.out.println(new Half(-12.34f));      // -12.34
	}
}
