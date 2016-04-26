package bht.tools.util;

/**
 * TinyPoint, made for Blues2D, is made by and copyrighted to Blue Husky Programming, ©2012 BH-1-PS.<hr/>
 * A small, efficient alternative to {@link java.awt.Dimension} which only uses a 64-bit integer to store its data.
 * @author Supuhstar of Blue Husky Programming
 * @version 1.0.0
 * @since June 17, 2012
 */
public class TinyDim
{
  protected long size;
  
  /**
   * Creates a new {@link TinyPoint} with the given coordinates
   * @param w the initial width of the point
   * @param h the initial height of the point
   */
  public TinyDim(int w, int h)
  {
    size |= w;
    size |= (long)h << 32;
  }
  
  protected TinyDim(){}
  
  /**
   * Sets both the x and y coordinates of this point
   * @param x the new x-coordinate of the point
   * @param y 
   */
  public void setSize(int w, int h)
  {
    setW(w);
    setH(h);
  }
  
  /**
   * Returns the width of this point
   * @return the width of this point
   */
  public int getW()
  {
    return (int) (size & 0x00000000FFFFFFFFL);//Get only the right 32 bits and return the result as a 32-bit integer
  }
  
  /**
   * Returns the height of this point
   * @return the height of this point
   */
  public int getH()
  {
    return (int)((size & 0xFFFFFFFF00000000L) >> 32);//Get only the left 32 bits and then shift them over to the right edge and return the result as a 32-bit integer
  }

  /**
   * Sets the width of this point. This stores the given 32-bit integer in the right half of a 64-bit integer
   * @param w the new width of this point
   */
  public void setW(int w)
  {
    size |= w;
  }

  /**
   * Sets the height of this point. This stores the given 32-bit integer in the left half of a 64-bit integer
   * @param h the new height of this point
   */
  public void setH(int h)
  {
    size |= (long)h << 32;
  }

  /**
   * Creates and returns a string representation of this object. Equivalent to {@code "[(" + getW() + ", " + getH() + "), (" +
   * getX() + ", " + getY() + ")]"}.<br/>
   * <strong>Examples:</strong>
   * <ul>
   *  <li>{@code System.out.println(new TinyPoint(0,0,0,0)} prints {@code [(0, 0), (0, 0)]}</li>
   *  <li>{@code System.out.println(new TinyPoint(15,64, 18, 57)} prints {@code [(15, 64), (18, 57)]}</li>
   *  <li>{@code System.out.println(new TinyPoint(-35486,864132456, -56039,23456789)} prints {@code [(-35486, 864132456) (-56039, 23456789)]}</li>
   *  <li>et cetera</li>
   * </ul>
   * @return a string representation of this object. Equivalent to {@code "[(" + getW() + ", " + getH() + "), (" + getX() +
   * ", " + getY() + ")]"}
   */
  @Override
  public String toString()
  {
    return "(" + getW() + " x " + getH() + ")";
  }
}
