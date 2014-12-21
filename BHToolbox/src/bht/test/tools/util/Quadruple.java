package bht.test.tools.util;

import bht.tools.misc.CompleteObject;
import bht.tools.misc.ToPrimitives;

/**
 * The {@code Quadruple} class mimics a value of the nonexistent primitive type {@code quadruple}, which is a 128-bit
 * floating-point value. An object of type {@code Quadruple} contains two fields whose type are {@code long}, simply to store
 * the bits.<br/>
 * <br/>
 * In addition, this class provides several methods for converting a {@code double} to a {@code String} and
 * a {@code String} to a {@code double}, as well as other constants and methods useful when dealing with a {@code double}.<br/>
 * <br/>
 * This class attempts to completely comply with IEEE 754:
 * <ul>
 *  <li><b>arithmetic formats</b>: sets of binary and decimal floating-point data, which consist of finite numbers (including signed zeros and subnormal numbers), infinities, and special "not a number" values (NaNs)</li>
 *  <li><b>interchange formats</b>: encodings (bit strings) that may be used to exchange floating-point data in an efficient and compact form</li>
 *  <li><b>rounding rules</b>: properties to be satisfied when rounding numbers during arithmetic and conversions</li>
 *  <li><b>operations</b>: arithmetic and other operations on arithmetic formats</li>
 *  <li><b>exception handling</b>: indications of exceptional conditions (such as division by zero, overflow, etc.)</li>
 * </ul>
 *
 * @author Supuhstar of Blue Husky Studios
 * @version 1.0.1
 *		- 2014-08-19 (1.0.1) 0 Kyli Rouge added documentation, some TODOs, and fixed a compile error
 * @since Jul 20, 2012
 * 
 * @deprecated Not yet fully implemented
 */
public class Quadruple extends Number implements CompleteObject, ToPrimitives
{
  /**
   * A constant holding the positive infinity of type {@code Quadruple}. It is equal to the value returned by
   * {@link Quadruple#longBitsToQuadruple(0x7FF0000000000000L, 0L)}.
   * 
   * @since July 20, 2012 (1.0.0)
   */
  public static final Quadruple POSITIVE_INFINITY = new Quadruple(0x7FF0000000000000L, 0L);
  /**
   * A constant holding the negative infinity of type {@code Quadruple}. It is equal to the value returned by
   * {@link Quadruple#longBitsToQuadruple(0xFFF0000000000000L, 0L)}.
   * 
   * @since July 20, 2012 (1.0.0)
   */
  public static final Quadruple NEGATIVE_INFINITY = new Quadruple(0xFFF0000000000000L, 0L);
  /**
   * A constant holding a Not-a-Number (NaN) value of type
   * {@code Quadruple}. It is equivalent to the value returned by
   * {@link Quadruple#longBitsToQuadruple(0x7FF8000000000000L, 0L)}.
   * 
   * @since July 20, 2012 (1.0.0)
   */
  public static final Quadruple NaN = new Quadruple(0x7FF8000000000000L, 0L);
  /**
   * A constant holding the largest positive finite value of type
   * {@code Quadruple}, (2-2<sup>-52</sup>)&middot;2<sup>1023</sup>. It is equal to the hexadecimal floating-point literal
   * {@code 0x1.fffffffffffffP+1023} and also equal to
   * {@code Double.longBitsToDouble(0x7fefffffffffffffL)}.
   * 
   * @since July 20, 2012 (1.0.0)
   */
  public static final Quadruple MAX_VALUE = new Quadruple(0x7FFEFFFFFFFFFFFFL, 0xFFFFFFFFFFFFFFFFL); // ≈ 1.189731495357231765085759326628007 × 10^4932
  /**
   * A constant holding the smallest positive normal value of type
   * {@code double}, 2<sup>-1022</sup>. It is equal to the hexadecimal floating-point literal {@code 0x1.0p-1022} and also equal
   * to {@code Double.longBitsToDouble(0x0010000000000000L)}.
   *
   * @since July 20, 2012 (1.0.0)
   */
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> public static final Quadruple MIN_NORMAL = new Quadruple(0x7FFF000000000000L, 0x0000000000000001L); // 2.2250738585072014E-308
  /**
   * A constant holding the smallest positive nonzero value of type
   * {@code double}, 2<sup>-1074</sup>. It is equal to the hexadecimal floating-point literal
   * {@code 0x0.0000000000001P-1022} and also equal to
   * {@code Double.longBitsToDouble(0x1L)}.
   * 
   * @since July 20, 2012 (1.0.0)
   */
  public static final Quadruple MIN_VALUE = new Quadruple(0xFFFEFFFFFFFFFFFFL, 0xFFFFFFFFFFFFFFFFL); // ≈ -1.189731495357231765085759326628007 × 10^4932
  /**
   * Maximum exponent a finite {@code double} variable may have. It is equal to the value returned by
   * {@code Math.getExponent(Double.MAX_VALUE)}.
   *
   * @since July 20, 2012 (1.0.0)
   */
  public static final int MAX_EXPONENT = 0x3ff;
  /**
   * Minimum exponent a normalized {@code double} variable may have. It is equal to the value returned by
   * {@code Math.getExponent(Double.MIN_NORMAL)}.
   *
   * @since July 20, 2012 (1.0.0)
   */
  public static final int MIN_EXPONENT = 0xfffffc02;
  /**
   * The number of bits used to represent a {@code double} value.
   *
   * @since July 20, 2012 (1.0.0)
   */
  public static final int SIZE = 0x80;
  
  
  
  long l = 0, r = 0;

  public Quadruple(long initValueLeft, long initValueRight)
  {
	  l = initValueLeft;
	  r = initValueRight;
  }

  public Quadruple(double initValue)
  {
	  // TODO: convert from double
	  
  }

  /**
   * Creates a new {@code Quadruple} with the value of the given {@link Number}'s {@link Number#doubleValue()} value.
   * @param initValue the value with which to initialize this {@code Quadruple}
   */
  public Quadruple(Number initValue)
  {
	  this(initValue.doubleValue());
  }

  /**
   * Creates a new {@code Quadruple} with the value of the given {@link Number}'s {@link Number#doubleValue()} value.
   * @param initValue the value with which to initialize this {@code Quadruple}
   */
  public Quadruple(Quadruple initValue)
  {
	  this(initValue.l, initValue.r);
  }

  @Override
  public int intValue()
  {
	  // TODO: Account for mantissa, exponent of l and r
	  return (int) ((Long.signum(l) == -1 ? 1<<31 : 0) | (r & Long.MAX_VALUE));
  }
  
  public static void main(String[] args)
	{
		System.out.println(Integer.toBinaryString(new Quadruple(-1, 0).intValue()));
	}

  @Override
  public long longValue()
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public float floatValue()
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public double doubleValue()
  {
    long d = 0;
    d |= (l & 0x8000000000000000L);//sign
    // TODO: exponent
    // TODO: fraction
    return Double.longBitsToDouble(d);
  }

  /**
   * {@inheritDoc} 
   * @return {@code false} if the value is {@code 0}, {@code true} otherwise.
   */
  @Override
  public boolean toBoolean()
  {
    return (l|r) != 0;
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
  public Quadruple clone()
  {
    return new Quadruple(this);
  }
}
