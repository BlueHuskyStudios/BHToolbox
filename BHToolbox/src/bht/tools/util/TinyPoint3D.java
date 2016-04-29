package bht.tools.util;

/**
 * TinyPoint32, made for BH Redstone Sim, is made by and copyrighted to Blue Husky Programming, ©2012 BH-1-PS.<hr>
 * Similar to {@link TinyPoint}, but supports 3 dimensions (X, Y, and Z). Each coordinate has 21 bits to store its information,
 * including the sign. This means that each has a range of ±1,048,575. If this is too small for you,
 * consider using three {@code int}s or {@code long}s.<br/>
 * <br/>
 * There is one unused bit. This may be taken advantage of in the future.
 * @author Supuhstar of Blue Husky Programming
 * @since Jan 24, 2012
 * @version 1.0.0
 */
public class TinyPoint3D extends TinyPoint //NOTE: Must be compiled in UTF-8
{
  /**
   * Creates a new {@link TinyPoint} with the given coordinates
   * @param x the initial x-coordinate of the point
   * @param y the initial y-coordinate of the point
   * @param z the initial z-coordinate of the point
   * @see #setX(int)
   * @see #setY(int)
   * @see #setZ(int)
   */
  public TinyPoint3D(int x, int y, int z)
  {
    point |= x;
    point |= (long)y << 21;
    point |= (long)z << 42;
  }

  /**
   * Sets both the x and y coordinates of this point
   * @param x the new x-coordinate of the point
   * @param y the new y-coordinate of the point
   * @param z the new z-coordinate of the point
   */
  public void setLocation(int x, int y, int z)
  {
    setX(x);
    setY(y);
    setZ(z);
  }
  
  /**
   * Returns the x-coordinate of this point
   * @return the x-coordinate of this point
   */
  @Override
  public int getX()
  {
    return ((point & 0x0000000000100000L) != 0 ? -1 : 1) *  //If bit 21 is negative, negate the result
      (int) (point & 0x00000000000FFFFFL);//Get only the right 20 bits and return the result as a 32-bit integer
  }
  
  /**
   * Returns the y-coordinate of this point
   * @return the y-coordinate of this point
   */
  @Override
  public int getY()
  {
    return ((point & 0x0000020000000000L) != 0 ? -1 : 1) *  //If bit 42 is negative, negate the result
      (int)((point & 0x000001FFFFE00000L) >> 21);//Get only the middle 20 bits and then shift them over to the right edge and return the result as a 32-bit integer
  }
  
  /**
   * Returns the z-coordinate of this point
   * @return the z-coordinate of this point
   */
  public int getZ()
  {
    return ((point & 0x4000000000000000L) != 0 ? -1 : 1) *  //If bit 42 is negative, negate the result
      (int)((point & 0x3FFFFC0000000000L) >> 42);//Get only the left 20 after the leftmost bit and then shift them over to the right edge and return the result as a 32-bit integer
  }

  /**
   * Sets the x-coordinate of this point. This stores the given 21-bit integer in the right third of a 64-bit integer.
   * If the given number is larger than ±2<sup>20</sup>, then it is rounded to ±2<sup>20</sup>.
   * @param x the new x-coordinate of this point
   */
  @Override
  public void setX(int x)
  {
    if (x > 0xFFFFF)
      x = 0xFFFFF;
    else if (x < -0xFFFFF)
      x = -0xFFFFF;
    point |= x;
  }

  /**
   * Sets the y-coordinate of this point. This stores the given 21-bit integer in the center third of a 64-bit integer.
   * If the given number is larger than ±2<sup>20</sup>, then it is rounded to ±2<sup>20</sup>.
   * @param y the new y-coordinate of this point
   */
  @Override
  public void setY(int y)
  {
    if (y > 0xFFFFF)
      y = 0xFFFFF;
    else if (y < -0xFFFFF)
      y = -0xFFFFF;
    point |= (long)y << 21;
  }

  /**
   * Sets the z-coordinate of this point. This stores the given 21-bit integer in the left third of a 64-bit integer.
   * If the given number is larger than ±2<sup>20</sup>, then it is rounded to ±2<sup>20</sup>.
   * @param z the new z-coordinate of this point
   */
  public void setZ(int z)
  {
    if (z > 0xFFFFF)
      z = 0xFFFFF;
    else if (z < -0xFFFFF)
      z = -0xFFFFF;
    point |= (long)z << 42;
  }

  /**
   * Creates and returns a string representation of this object. Equivalent to {@code "(" + getX() + ", " + getY() + ")"}.<br/>
   * <strong>Examples:</strong>
   * <ul>
   *  <li>{@code System.out.println(new TinyPoint(0,0)} prints {@code (0, 0)}</li>
   *  <li>{@code System.out.println(new TinyPoint(15,64)} prints {@code (15, 64)}</li>
   *  <li>{@code System.out.println(new TinyPoint(-35486,864132456)} prints {@code (-35486, 864132456)}</li>
   *  <li>et cetera</li>
   * </ul>
   * @return a string representation of this object. Equivalent to {@code "(" + getX() + ", " + getY() + ")"}
   */
  @Override
  public String toString()
  {
    return "(" + getX() + ", " + getY() + ", " + getZ() + ")";
  }
}
