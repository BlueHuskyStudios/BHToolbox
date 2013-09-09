package bht.tools.util;

import bht.tools.util.math.Numbers;

/**
 * Stores exactly 32 bits of information, while providing an interface to view and set individual bits. The point of this is to
 * be able to store numbers and boolean values while only using 32 bits of memory.
 * @author Supuhstar of Blue Husky Programming
 * @since 1.7.0
 * @version 1.1.0
 */
public class BitByte
{
  private volatile byte bi = 0;
  
  /**
   * Creates a new BitInt based on the values in the given array, where <tt>initVals[0]</tt> represents the bit on the far right
   * and <tt>initVals[31]</tt> is the bit on the far left.
   * @param initVals the array of <tt>boolean</tt>s representing the values in the new <tt>BitInt</tt>. Can be any length, but
   * if longer than <tt>Integer.SIZE</tt> values long, only the first <tt>Integer.SIZE</tt> values will be used. If less than
   * <tt>Integer.SIZE</tt>, all values will be used, and the remaining bits will be off.
   */
  public BitByte(boolean[] initVals)
  {
    String s = "";
    for (byte b=0; b < Math.min(initVals.length, Integer.SIZE); b++)
      s = (initVals[b] ? "1" : "0") + s;
    bi = Byte.parseByte(s, 2);
  }
  
  /**
   * Creates a new <tt>BitInt</tt> using the bit data contained in the provided <tt>int</tt>
   * @param initVals the <tt>int</tt> representing the bits to be stored in this <tt>BitInt</tt>
   */
  public BitByte(byte initVals)
  {
    bi = initVals;
  }
  
  /**
   * Sets the value of the bit at index <tt>index</tt> to be on or off, depending on the value of <tt>newVal</tt>
   * @param index the index of the bit to be changed, from the right
   * @param newVal the new value of the bit
   * @return the resulting <tt>BitInt</tt>
   */
  public BitByte setValueAt(byte index, boolean newVal)
  {
    bi = (byte)(newVal ? (1 << index) | bi : ~(1 << index) & bi);
    return this;
  }
  
  /**
   * Reads and returns the bits from index <tt>beginIndex</tt> to index <tt>endIndex</tt> as an <tt>int</tt>
   * @param beginIndex the index of the first bit to be read, from the right.
   * @param endIndex the index of the last bit to be read, from the right.
   * @return an <tt>int</tt> representation of all bits between <tt>beginIndex</tt> and <tt>endIndex</tt>, inclusive
   */
  public byte getValuesFrom(byte beginIndex, byte endIndex)
  {
    if (beginIndex > endIndex)
      throw new IllegalArgumentException("beginIndex must come before endIndex");
    return Byte.parseByte(toString().substring(bitIndex(endIndex), bitIndex(beginIndex)), 2);
  }
  
  /**
   * Sets the bits from index <tt>beginIndex</tt> to index <tt>endIndex</tt> to have the same values as those in <tt>newVal</tt>
   * @param beginIndex the index of the first bit to be changed, from the right.
   * @param endIndex the index of the last bit to be changed, from the right.
   * @param newVal the <tt>int</tt> representing the values of the new 
   * @return the resulting <tt>BitInt</tt>
   */
  public BitByte setValuesFrom(byte beginIndex, byte endIndex, byte newVal)
  {
    if (beginIndex > endIndex)
      throw new IllegalArgumentException("beginIndex must come before endIndex (" + beginIndex + " > " + endIndex + ")");
    for (byte c=beginIndex; c < endIndex; c++)
      setValueAt(c, (newVal | (1 << c)) != newVal);
    return this;
  }

  /**
   * Returns the state of the bit at index <tt>index</tt>
   * @param index the index of the bit to be observed, from the right
   * @return the state of the bit at index <tt>index</tt>, as a <tt>boolean</tt>
   */
  public boolean getValueAt(byte index)
  {
    return (bi | (1 << bitIndex(index))) != bi;
    /*
     * getValueAt(3)
     * 
     *bi = 0101 0001   bi = 0101 1001
     *   | 0000 1000      | 0000 1000 
     * ==============   ==============
     *     0101 1001        0101 1001
     *  == 0101 0001     == 0101 1001
     * ==============   ==============
     *     false            true
     */
  }
  
  /**
   * Returns a <tt>String</tt> representation of this <tt>BitInt</tt>, where each character is either a <tt>1</tt> or a
   * <tt>0</tt>, depending on its state.
   * @return a <tt>String</tt> representation of this <tt>BitInt</tt>
   */
  @Override
  public String toString()
  {
    return toString(2);
  }
  
  /**
   * Returns a <tt>String</tt> representation of this <tt>BitInt</tt>, represented in the given radix. This works the same as 
   * <tt>Integer.toString(int i, int radix)</tt>
   * @param radix the radix of the returned <tt>String</tt>. for instance, a radix of <tt>2</tt> returns a binary
   * representation, where a radix of <tt>16</tt> returns a hexadecimal representation
   * @return a <tt>String</tt> representation of this <tt>BitInt</tt>, represented in the given radix.
   */
  public String toString(int radix)
  {
    return Numbers.lenFmt(Integer.toString(bi, radix), Byte.SIZE);
  }
  
  /**
   * Calculates and returns the bitwise version of the given array-style index. For instance, 0, in arrays, points to the
   * leftmost value, while in bitwise functions, it points to the rightmost.
   * @param arrayIndex the array-style index of a value
   * @return the bitwise-style index of a value
   */
  public static byte bitIndex(byte arrayIndex)
  {
    return (byte)(Byte.SIZE - arrayIndex - 1);
  }
}
