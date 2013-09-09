/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bht.tools.util;

/**
 * TinyPoint, made for BH Checkers 2, is made by and copyrighted to Blue Husky Programming, ©2012. License is default.<hr/>
 * Similar to {@link java.awt.Point}, but only uses a single 64-bit integer to store its data.
 * @author Supuhstar of Blue Husky Programming
 * @since Jan 24, 2012
 * @version 1.1.0
 */
public class TinyPoint //NOTE: Must be compiled in UTF-8
{
  protected long point;
  
  /**
   * Creates a new {@link TinyPoint} with the given coordinates
   * @param x the initial x-coordinate of the point
   * @param y the initial x-coordinate of the point
   */
  public TinyPoint(int x, int y)
  {
    point |= x;
    point |= (long)y << 32;
  }
  
  protected TinyPoint(){}//Added 2012/05/24 for Redstone Sim

  /**
   * Sets both the x and y coordinates of this point
   * @param x the new x-coordinate of the point
   * @param y 
   */
  public void setLocation(int x, int y)
  {
    setX(x);
    setY(y);
  }
  
  /**
   * Returns the x-coordinate of this point
   * @return the x-coordinate of this point
   */
  public int getX()
  {
    return (int) (point & 0x00000000FFFFFFFFL);//Get only the right 32 bits and return the result as a 32-bit integer
  }
  
  /**
   * Returns the y-coordinate of this point
   * @return the y-coordinate of this point
   */
  public int getY()
  {
    return (int)((point & 0xFFFFFFFF00000000L) >> 32);//Get only the left 32 bits and then shift them over to the right edge and return the result as a 32-bit integer
  }

  /**
   * Sets the x-coordinate of this point. This stores the given 32-bit integer in the right half of a 64-bit integer
   * @param x the new x-coordinate of this point
   */
  public void setX(int x)
  {
    point |= x;
  }

  /**
   * Sets the y-coordinate of this point. This stores the given 32-bit integer in the left half of a 64-bit integer
   * @param y the new y-coordinate of this point
   */
  public void setY(int y)
  {
    point |= (long)y << 32;
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
    return "(" + getX() + ", " + getY() + ")";
  }
}
