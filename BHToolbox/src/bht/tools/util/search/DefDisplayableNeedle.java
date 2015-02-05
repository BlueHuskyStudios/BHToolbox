package bht.tools.util.search;

import bht.tools.util.ArrayPP;
import bht.tools.util.StringPP;

/**
 * A default {@link Needle}, with all methods and constructors already implemented
 * @author Supuhstar of Blue Husky Programming
 * @version 1.0.1
		- 2.0.0 (2015-02-04) - Kyli renamed DefSearchable to DefDisplayableNeedle
 * @since 1.6_23
 */
public class DefDisplayableNeedle implements Needle
{
  private ArrayPP<Keyword> keys = new ArrayPP<>();
  private StringPP title;
  private Object display;

  public DefDisplayableNeedle()
  {
    this (null, null, null);
  }

  public DefDisplayableNeedle(ArrayPP<Keyword> keywords)
  {
    this(null, null, keywords);
  }

  public DefDisplayableNeedle(CharSequence title)
  {
    this(title, title);
  }

  public DefDisplayableNeedle(CharSequence title, ArrayPP<Keyword> keywords)
  {
    this(title, title, keywords);
  }

  public DefDisplayableNeedle(Object display)
  {
    this(display.toString(), display);
  }

  public DefDisplayableNeedle(Object display, ArrayPP<Keyword> keywords)
  {
    this(display.toString(), display, keywords);
  }

  public DefDisplayableNeedle(CharSequence title, Object display)
  {
    this(title, display, new ArrayPP<>(new Keyword(title, 1.0)));
  }
  
  public DefDisplayableNeedle(CharSequence title, Object display, ArrayPP<Keyword> keywords)
  {
    this.title = new StringPP(title);
    this.display = display;
    this.keys = keywords;
  }
  
  @Override
  public ArrayPP<Keyword> getKeywords()
  {
    return keys;
  }
  
  /**
   * Calculates and returns a match strength between the two <t>DefDisplayableNeedle</tt>s depending on whether...
   * <ol>
   * <li>They are equal (tested with <tt>==</tt> and <tt>.equals</tt>)</li>
   * <li>They have any or all matching keywords</li>
   * <li>They have equal or similar titles</li>
   * <li>They have equal displays</li>
   * </ol>
   * @param otherSearchable the other <tt>DefDisplayableNeedle</tt> to which this one will be compared
   * @return the <tt>double</tt> representing the strength of the match between the two items
   */
  @Override
  public double getMatchStrength(Needle otherSearchable)
  {
    if (otherSearchable == this || otherSearchable.equals(this))
      return 1;
    
    double strength = 0.5;
    
    if (title.equalsIgnoreCase(otherSearchable.getSearchTitle()) ||
        new StringPP(otherSearchable.getSearchTitle()).containsIgnoreCase(title) ||
        title.containsIgnoreCase(otherSearchable.getSearchTitle()))
      strength = Math.pow(strength, 0.25);
    
    if (otherSearchable.getKeywords().containsAll(getKeywords().toArray()))
      strength = Math.pow(strength, 0.05);
    else if (otherSearchable.getKeywords().containsAny(getKeywords().toArray()))
    {
      for (Keyword k : getKeywords())
        if (otherSearchable.getKeywords().contains(k))
          strength = Math.pow(strength, 0.25 * k.getRelevance());
    }
    else
      strength = Math.pow(strength, 2);
    
    if (display == otherSearchable.getSearchDisplay())
      strength = Math.pow(strength, 0.05);
    else if (display.equals(otherSearchable.getSearchDisplay()))
      strength = Math.pow(strength, 0.15);
    
    return strength;
  }
  
  @Override
  public String toString()
  {
    return title.toString();
  }
  
  @Override
  public DefDisplayableNeedle setSearchTitle(CharSequence newTitle)
  {
    title = new StringPP(newTitle);
    return this;
  }
  
  @Override
  public CharSequence getSearchTitle()
  {
    return title.toString();
  }
  
  @Override
  public DefDisplayableNeedle setSearchDisplay(Object newDisplay)
  {
    display = newDisplay;
    return this;
  }
  
  @Override
  public Object getSearchDisplay()
  {
    return display;
  }

  /**
   * The same as <tt>(int)(getMatchStrength(otherSearchable) * 100)</tt>
   * @param otherSearchable the other <tt>Needle</tt> that  
   * @return <tt>(int)(getMatchStrength(otherSearchable) * 100)</tt>
   */
  @Override
  public long compareTo64(Needle otherSearchable)
  {
    return (long)(getMatchStrength(otherSearchable) * 100);
  }

	@Override
	public int compareTo(Needle o)
	{
		return (int)compareTo64(o);
	}
}
