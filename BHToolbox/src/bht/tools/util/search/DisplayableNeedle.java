package bht.tools.util.search;


/**
 * An interface that defines an object that can be searched for and displayed.
 * 
 * @param <T> The type of display that will be used
 * 
 * @author Supuhstar of Blue Husky Programming
 * @version 2.0.0
 *		- 2.0.0 (2015-02-04) - Kyli separated Searchable into Needle and DisplayableNeedle
 */
public interface DisplayableNeedle<T> extends Needle
{
  
  /**
   * Returns the object that will work as this Needle's display.
   * @return the object that will work as this Needle's display
   * @see #setSearchDisplay(Object display)
   */
  public T getSearchDisplay();
  
  /**
   * Sets the object that will work as this Needle's display.
   * @param display the object that this Needle will use to display itself to the user. Intended use is as a
 <tt>JComponent</tt>, but who am I to tell you how to display this?
   * @return this Needle, for further modifications
   */
  public DisplayableNeedle setSearchDisplay(T display);
}
