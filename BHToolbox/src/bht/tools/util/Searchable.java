package bht.tools.util;

import java.util.Objects;


/**
 * An interface that defines an object that can be searched for.
 * @author Supuhstar of Blue Husky Programming
 * @version 1.0.7
 */
public interface Searchable extends Comparable64<Searchable>
{
  /**
   * Returns the array of keywords for this Searchable. For instance, a {@link javax.swing.JFrame} extension that implements
   * {@code Saveable} and contains a single message which states the current time might return the following array:<br/>
   * <table border=1>
   *  <tbody>
   *    <tr><td>{@code Swing}</td></tr>
   *    <tr><td>{@code AWT}</td></tr>
   *    <tr><td>{@code JFrame}</td></tr>
   *    <tr><td>{@code Frame}</td></tr>
   *    <tr><td>{@code Window}</td></tr>
   *    <tr><td>{@code Closable}</td></tr>
   *    <tr><td>{@code Minimizable}</td></tr>
   *    <tr><td>{@code Maximizable}</td></tr>
   *    <tr><td>{@code Iconifiable}</td></tr>
   *    <tr><td>{@code Message}</td></tr>
   *    <tr><td>{@code Time}</td></tr>
   *    <tr><td>{@code Clock}</td></tr>
   *  </tbody>
   * </table>
   * @return the array of keywords for this Searchable
   */
  public ArrayPP<Keyword> getKeywords();
  
  /**
   * Calculates and returns a match strength between the two {@link Searchable}s. It is recommended that the method does this
   * depending on whether...
   * <ol>
   * <li>They are equal (tested with <tt>==</tt> and <tt>.equals</tt>)</li>
   * <li>They have any or all matching keywords</li>
   * <li>They have equal or similar titles</li>
   * <li>They have equal displays</li>
   * </ol>
   * @param otherSearchable the other <tt>Searchable</tt> to which this one will be compared
   * @return the <tt>double</tt> representing the strength of the match between the two items
   */
  public double getMatchStrength(Searchable otherSearchable);
  
  /**
   * Returns the title of this Searchable as a <tt>String</tt>
   * @return the title of this Searchable as a <tt>String</tt>
   */
  public CharSequence getSearchTitle();
  
  /**
   * Returns the object that will work as this Searchable's display.
   * @return the object that will work as this Searchable's display
   * @see #setSearchDisplay(Object display)
   */
  public Object getSearchDisplay();

  /**
   * The same as <tt>(long)(getMatchStrength(otherSearchable) * 100)</tt>
   * @param otherSearchable the other <tt>Searchable</tt> that  
   * @return <tt>(long)(getMatchStrength(otherSearchable) * 100)</tt>
   * @see Comparable64#compareTo(java.lang.Object) 
   */
  @Override
  public long compareTo64(Searchable otherSearchable);

  
  /**
   * Sets the String that will work as this Searchable's title.
   * @param title the {@link CharSequence} that this Searchable will use to describe itself to the user. For example, if this
   * Searchable is a <tt>JComponent</tt> inside a <tt>JTabbedPane</tt>, this might be the text on the tab.
   * @return this Searchable, for further modifications
   */
  public Searchable setSearchTitle(CharSequence title);
  
  /**
   * Sets the object that will work as this Searchable's display.
   * @param display the object that this Searchable will use to display itself to the user. Intended use is as a
   * <tt>JComponent</tt>, but who am I to tell you how to display this?
   * @return this Searchable, for further modifications
   */
  public Searchable setSearchDisplay(Object display);
  
  public class Keyword
  {
    private CharSequence keyword;
    private double importance;

    public Keyword(CharSequence keyword)
    {
      this(keyword, 1);
    }

    public Keyword(CharSequence keyword, double importance)
    {
      this.keyword = keyword;
      this.importance = importance;
    }

    public double getImportance()
    {
      return importance;
    }

    public CharSequence getKeyword()
    {
      return keyword;
    }

    public void setImportance(double importance)
    {
      this.importance = importance;
    }

    public void setKeyword(CharSequence keyword)
    {
      this.keyword = keyword;
    }

    /*
    @Override
    public int hashCode()//For JDK 6
    {
      int hash = 3;
      hash = 41 * hash + (this.keyword != null ? this.keyword.hashCode() : 0);
      hash =
      41 * hash +
      (int) (Double.doubleToLongBits(this.importance) ^ (Double.doubleToLongBits(this.importance) >>> 32));
      return hash;
    }*/
    
    @Override
    public int hashCode()//For JDK 7
    {
      int hash = 0b101;
      hash = 0b1101 * hash + Objects.hashCode(this.keyword);
      hash =
      0b1101 * hash +
      (int) (Double.doubleToLongBits(this.importance) ^ (Double.doubleToLongBits(this.importance) >>> 0b100000));
      return hash;
    }
    
    /**
     * Compares <tt>obj</tt> and this <tt>Keyword</tt> for equality, based on the following factors:
     * <ol>
     * <li><tt>obj</tt> is not <tt>null</tt></li>
     * <li><tt>obj</tt> is a member of <tt>Keyword</tt> or a subclass that extends it</li>
     * <li>The <tt>keyword</tt> <tt>CharSequence</tt> in <tt>obj</tt> is the same, character-for-character, as that of this <tt>Keyword</tt>
     * <li>The <tt>importance</tt> value of <tt>obj</tt> is within one billionth of that of this <tt>Keyword</tt>
     * </ol>
     * @param obj
     * @return 
     */
    
    /*@Override
    public boolean equals(Object obj)
    {
      if (obj == null) return false;
      if (getClass() != obj.getClass() && !(obj instanceof Keyword)) return false;
      final Keyword other = (Keyword) obj;
      if (this.keyword != other.keyword && (this.keyword == null || !this.keyword.equals(other.keyword))) return false;
      if ((int)(this.importance * 1000000000) != (int)(other.importance * 1000000000)) return false;
      return true;
    }*/
    @Override
    public boolean equals(Object obj)//For JDK 7
    {
      if (obj == null) return false;
      if (getClass() != obj.getClass() && !(obj instanceof Keyword)) return false;
      final Keyword other = (Keyword) obj;
      if (!Objects.equals(this.keyword, other.keyword)) return false;
      if ((int)(this.importance * 1000000000) != (int)(other.importance * 1000000000)) return false;
      return true;
    }

    @Override
    public String toString()
    {
      return "\"" + keyword + "\" + " + importance;
    }
  }
}
