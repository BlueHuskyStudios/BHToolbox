package bht.tools.util;

/**
 * Stores exactly 32 bits of information, while providing an interface to view and set individual bits. The point of this is to
 * be able to store numbers and boolean values while only using 32 bits of memory.
 * @author Supuhstar of Blue Husky Studios
 * @since 2012/02/15
 * @version 1.1.1
 */
public class BitInt
{
  private volatile int bi = 0;
  
  /**
   * Creates a new BitInt based on the values in the given array, where <tt>initVals[0]</tt> represents the bit on the far right
   * and <tt>initVals[31]</tt> is the bit on the far left.
   * @param initVals the array of <tt>boolean</tt>s representing the values in the new <tt>BitInt</tt>. Can be any length, but
   * if longer than <tt>Integer.SIZE</tt> values long, only the first <tt>Integer.SIZE</tt> values will be used. If less than
   * <tt>Integer.SIZE</tt>, all values will be used, and the remaining bits will be off.
   */
  public BitInt(boolean[] initVals)
  {
    /* Vitrified 2012/09/24
     * 
     * String s = "";
     * for (int c=0, l = Math.min(initVals.length, Integer.SIZE); c < l; c++)
     *   s = (initVals[c] ? "1" : "0") + s;
     * bi = Integer.parseInt(s, 2);
     */

    for (int c = 0, l = Math.min(initVals.length, Integer.SIZE); c < l; c++)
      if (initVals[c])
        bi |= 1 << c;
  }
  
  /**
   * Creates a new <tt>BitInt</tt> using the bit data contained in the provided <tt>int</tt>
   * @param initVals the <tt>int</tt> representing the bits to be stored in this <tt>BitInt</tt>
   */
  public BitInt(int initVals)
  {
    bi = initVals;
  }
  
  /**
   * Sets the value of the bit at index <tt>index</tt> to be on or off, depending on the value of <tt>newVal</tt>
   * @param index the index of the bit to be changed, from the right
   * @param newVal the new value of the bit
   * @return the resulting <tt>BitInt</tt>
   */
  public BitInt setValueAt(byte index, boolean newVal)
  {
    if (index < 0 || index > Integer.SIZE)//Added 2012/09/24 for BitInt
      throw new IndexOutOfBoundsException("index must be between 0 and " + Integer.SIZE + ", inclusive");
    bi = newVal ? (1 << index) | bi : ~(1 << index) & bi;
    return this;
  }
  
  /**
   * Reads and returns the bits from index <tt>beginIndex</tt> to index <tt>endIndex</tt> as an <tt>int</tt>
   * @param beginIndex the index of the first bit to be read, from the right.
   * @param endIndex the index of the last bit to be read, from the right.
   * @return an <tt>int</tt> representation of all bits between <tt>beginIndex</tt> and <tt>endIndex</tt>, inclusive
   */
  public int getValuesFrom(byte beginIndex, byte endIndex)
  {
    if (beginIndex == 0 && endIndex == Integer.SIZE - 1)//Added 2012/10/01 for Proofs of Concepts: perfectElevator
      return bi;
    if (beginIndex > endIndex)
      throw new IllegalArgumentException("beginIndex must not be larger than endIndex (" + beginIndex + " > " + endIndex + ")");
    return Integer.parseInt(toString().substring(bitIndex(endIndex), bitIndex(beginIndex)), 2);
  }
  
  /**
   * Sets the bits from index <tt>beginIndex</tt> to index <tt>endIndex</tt> to have the same values as those in {@code newVal}.
   * Values in slots higher than the specified range are ignored.
   * @param beginIndex the index of the first bit to be changed, from the right.
   * @param endIndex the index of the last bit to be changed, from the right.
   * @param newVal the {@code int} representing the values to be set
   * @return this
   */
  public BitInt setValuesFrom(byte beginIndex, byte endIndex, int newVal)
  {
    if (beginIndex == 0 && endIndex == Integer.SIZE - 1)//Added 2012/10/01 for Proofs of Concepts: perfectElevator
      bi = newVal;
    if (beginIndex > endIndex)
      throw new IllegalArgumentException("beginIndex must not be larger than endIndex (" + beginIndex + " > " + endIndex + ")");
     
    for (byte c=beginIndex; c < endIndex; c++)//To vitrify: Probably can be done with pure bitwise operations
      setValueAt(c, (newVal | (1 << c)) != newVal);
    return this;
  }
  
  /**
   * Sets the bits from index <tt>beginIndex</tt> to index <tt>endIndex</tt> to have the same values as those in {@code newVal}.
   * Values in slots higher than the specified range are ignored.
   * @param beginIndex the index of the first bit to be changed, from the right.
   * @param endIndex the index of the last bit to be changed, from the right.
   * @param newVal the {@link BitInt} representing the values to be set
   * @return this
   */
  public BitInt setValuesFrom(byte beginIndex, byte endIndex, BitInt newVal)
  {
    if (beginIndex == 0 && endIndex == Integer.SIZE - 1)
      bi = newVal.bi;
    if (beginIndex > endIndex)
      throw new IllegalArgumentException("beginIndex must not be larger than endIndex (" + beginIndex + " > " + endIndex + ")");
     
    for (byte c=beginIndex, c1 = 0; c < endIndex; c++, c1++)//To vitrify: Probably can be done with pure bitwise operations
      setValueAt(c, newVal.getValueAt(c1));
    return this;
  }

  /**
   * Returns the state of the bit at index <tt>index</tt>
   * @param index the index of the bit to be observed, from the right
   * @return the state of the bit at index <tt>index</tt>, as a <tt>boolean</tt>
   */
  public boolean getValueAt(byte index)
  {
    if (index < 0 || index > Integer.SIZE)//Added 2012/09/24 for BitInt
      throw new IndexOutOfBoundsException("index must be between 0 and " + Integer.SIZE + ", inclusive");
    return (bi | (1 << index)) != bi;
    /*
     *     0101 0001        0101 1001
     *   | 0000 1000      | 0000 1000 
     * ==============   ==============
     *     0101 1001        0101 1001
     *  != 0101 0001     != 0101 1001
     * ==============   ==============
     *     true             false
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
    return Integer.toString(bi, radix);
  }
  
  /**
   * Calculates and returns the bitwise version of the given array-style index. For instance, 0, in arrays, points to the
   * leftmost value, while in bitwise functions, it points to the rightmost.
   * @param arrayIndex the array-style index of a value
   * @return the bitwise-style index of a value
   */
  public static int bitIndex(int arrayIndex)
  {
    return Integer.SIZE - arrayIndex - 1;
  }

  /**
   * Returns {@code false} if and only if every bit is off. Else, returns true.
   * @return {@code true} if any one of the values is {@code true}.
   * @since 2012/10/01 (1.1.1)
   */
  public boolean isAnyValueOn()
  {
    return bi != 0;
  }

  /**
   * Returns {@code false} if and only if every bit is on. Else, returns true.
   * @return {@code true} if any one of the values is {@code false}.
   * @since 2012/10/01 (1.1.1)
   */
  public boolean isAnyValueOff()
  {
    return bi != 0xFFFF_FFFF;
  }
}
