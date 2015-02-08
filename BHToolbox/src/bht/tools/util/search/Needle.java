package bht.tools.util.search;

import bht.tools.util.ArrayPP;
import bht.tools.util.Comparable64;
import java.util.Objects;


/**
 * An interface that defines an object that can be searched for.
 * 
 * @author Supuhstar of Blue Husky Programming
 * @version 2.0.1
 *		- 2.0.1 (2015-02-08) - Kyli made better documentation, var names, and default weight
 *		- 2.0.0 (2015-02-04) - Kyli separated Searchable into Needle and DisplayableNeedle
 */
public interface Needle extends Comparable64<Needle>
{
  /**
   * Returns the array of keywords for this Needle. For instance, a {@link javax.swing.JFrame} extension that implements
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
   *    <tr><td>{@code Saveable}</td></tr>
   *    <tr><td>{@code Message}</td></tr>
   *    <tr><td>{@code Time}</td></tr>
   *    <tr><td>{@code Clock}</td></tr>
   *  </tbody>
   * </table>
   * @return the array of keywords for this Needle
   */
  public ArrayPP<Keyword> getKeywords();
  
  /**
   * Calculates and returns a match strength between the two {@link Needle}s. It is recommended that the method does this
   * depending on whether...
   * <ol>
   * <li>They are equal (tested with <tt>==</tt> and <tt>.equals</tt>)</li>
   * <li>They have any or all matching keywords</li>
   * <li>They have equal or similar titles</li>
   * <li>They have equal displays</li>
   * </ol>
   * @param otherNeedle the other <tt>Needle</tt> to which this one will be compared
   * @return the <tt>double</tt> representing the strength of the match between the two items
   */
  public double getMatchStrength(Needle otherNeedle);
  
  /**
   * Returns the title of this Needle as a <tt>String</tt>
   * @return the title of this Needle as a <tt>String</tt>
   */
  public CharSequence getSearchTitle();

  @Override
  public long compareTo64(Needle otherNeedle);

  
  /**
   * Sets the String that will work as this Needle's title.
   * @param title the {@link CharSequence} that this Needle will use to describe itself to the user. For example, if this
 Needle is a <tt>JComponent</tt> inside a <tt>JTabbedPane</tt>, this might be the text on the tab.
   * @return this Needle, for further modifications
   */
  public Needle setSearchTitle(CharSequence title);
  
  /**
   * A keyword that can be used to increase search accuracy. This class wraps a {@link CharSequence} that represents the actual
   * keyword, and a {@code double} that the search engine can use to determine how relevant the keyword is to the object it
   * represents, from {@code 0} (not at all relevant) up.
   */
  public class Keyword
  {
    private CharSequence keyword;
    private double relevance;

	/**
	 * Creates a new keyword with the given text
	 * @param keyword the initial text of the keyword
	 */
    public Keyword(CharSequence keyword)
    {
      this(keyword, .5);
    }

	/**
	 * Creates a new keyword with the given text and relevance
	 * @param initKeyword the initial text of the keyword
	 * @param initRelevance the initial relevance of the keyword, from {@code 0} (not at all relevant) up.
	 */
    public Keyword(CharSequence initKeyword, double initRelevance)
    {
      this.keyword = initKeyword;
      this.relevance = initRelevance;
    }

	/**
	 * Returns how relevant this keyword is to the {@link Searchable}
	 * @return how relevant this keyword is to the {@link Searchable}, from {@code 0} (not at all relevant) up.
	 */
    public double getRelevance()
    {
      return relevance;
    }

    public CharSequence getKeyword()
    {
      return keyword;
    }

    public void setImportance(double importance)
    {
      this.relevance = importance;
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
      (int) (Double.doubleToLongBits(this.relevance) ^ (Double.doubleToLongBits(this.relevance) >>> 32));
      return hash;
    }*/
    
    @Override
    public int hashCode()//For JDK 7
    {
      int hash = 0b101;
      hash = 0b1101 * hash + Objects.hashCode(this.keyword);
      hash =
      0b1101 * hash +
      (int) (Double.doubleToLongBits(this.relevance) ^ (Double.doubleToLongBits(this.relevance) >>> 0b100000));
      return hash;
    }
    
    /**
     * Compares <tt>obj</tt> and this <tt>Keyword</tt> for equality, based on the following factors:
     * <ol>
     * <li><tt>obj</tt> is not <tt>null</tt></li>
     * <li><tt>obj</tt> is a member of <tt>Keyword</tt> or a subclass that extends it</li>
     * <li>The <tt>keyword</tt> <tt>CharSequence</tt> in <tt>obj</tt> is the same, character-for-character, as that of this <tt>Keyword</tt>
     * <li>The <tt>relevance</tt> value of <tt>obj</tt> is within one billionth of that of this <tt>Keyword</tt>
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
      if ((int)(this.relevance * 1000000000) != (int)(other.relevance * 1000000000)) return false;
      return true;
    }*/
    @Override
    public boolean equals(Object obj)//For JDK 7
    {
      if (obj == null) return false;
      if (getClass() != obj.getClass() && !(obj instanceof Keyword)) return false;
      final Keyword other = (Keyword) obj;
      if (!Objects.equals(this.keyword, other.keyword)) return false;
      if ((int)(this.relevance * 1000000000) != (int)(other.relevance * 1000000000)) return false;
      return true;
    }

    @Override
    public String toString()
    {
      return "\"" + keyword + "\" + " + relevance;
    }
  }
}
