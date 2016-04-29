package bht.tools.util.search;

import bht.tools.util.ArrayPP;
import bht.tools.util.StringPP;
import bht.tools.util.search.Needle.Keyword;

/**
 *
 * Searcher, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012 BH-1-PS.<hr>
 * @author Supuhstar of Blue Husky Programming
 * @since ‎Dec ‎20, ‎2011
 * @author Supuhstar
 * @version 1.1.0
 */
public class Searcher<T>
{
  /**
   * Performs a binary search for the given object on the given array of objects of the same type<br/>
   * <b>Equivalent to <tt>binarySearch(a, target, 0, a.length())</tt></b>
   * @param target the object to be searched for
   * @param a the array of objects to be searched
   * @return the index of the target, if it is found, or <tt>(-(insertion point) - 1)</tt> if it is not
   */
  public int binarySearch(ArrayPP<T> a, T target)
  {
    return binarySearch(a, target, 0, a.length());
  }

  /**
   * Performs a binary search for the given object on the given array of objects of the same type
   * @param target the object to be searched for
   * @param a the array of objects to be searched
   * @param low the lower index from which to start searching
   * @param high the upper index from which to start searching
   * @return the index of the target, if it is found, or <tt>(-(insertion point) - 1)</tt> if it is not
   */
  public int binarySearch(ArrayPP<T> a, T target, int low, int high)
  {
//    return java.util.Arrays.binarySearch(a.toArray(), target);
    if (high < low)
       return -(low + 1); // not found
    int mid = low + ((high - low) / 2);
    if (firstIsGreater(a.get(mid), target))
       return binarySearch(a, target, low, mid-1);
    else if (secondIsGreater(a.get(mid), target))
       return binarySearch(a, target, mid+1, high);
    else
       return mid; // found
  }

  private boolean firstIsGreater(T first, T second)
  {
    String one = first instanceof Needle ? ((Needle)first).getSearchTitle() + ((Needle)first).getKeywords().toString()
            : first.toString(),
           two = second instanceof Needle ? ((Needle)second).getSearchTitle() + ((Needle)second).getKeywords().toString()
            : second.toString();
    for (int i=0; i < Math.min(one.length(), two.length()); i++)
    {
      if (one.charAt(i) != two.charAt(i))
        return one.charAt(i) > two.charAt(i);
    }
    return false;
  }

  private boolean secondIsGreater(T first, T second)
  {
    return firstIsGreater(second, first);
  }
  
  /**
   * Returns an ArrayPP of all matches, sorted from best to worst match.
   * @param a the ArrayPP to be searched
   * @param target the item to be searched for
   * @return an ArrayPP of all matches, sorted from best to worst match.
   */
  public ArrayPP<T> smartSearch(ArrayPP<T> a, T target)
  {
    return smartSearch(a, target, 0.5);
  }
  
  /**
   * Performs a linear search of the provided array and returns a list of all encountered matches, sorted from best to worst match.
   * @param a the ArrayPP to be searched
   * @param target the item to be searched for
   * @param cutoff the match strength a search must maintain to be included in the results. Input -1 to include all, 0 to
   * exclude items that don't match at all
   * @return an array of items matching the <tt>Needle</tt> version of <tt>target</tt>, sorted from best to worst match.
   */
  public ArrayPP<T> smartSearch(ArrayPP<T> a, T target, double cutoff)//Efficiencies added Mar 11, 2012 (1.0.2) for marian
  {
    ArrayPP<T> result;// = new ArrayPP<>();
    ArrayPP<Double> order = new ArrayPP<>();
    Needle sTarg = target instanceof Needle ? (Needle)target : makeSearchable(target);
    
    T t;
    for(int i=0; i < a.length(); i++)
      order.add(((t = a.get(i)) instanceof Needle ? (Needle)t : makeSearchable(t)).getMatchStrength(sTarg));
    
    result = new ArrayPP(a).bubbleSort(order);
    order.bubbleSort();
    for (int i=0; i < result.length(); i++)
      if (order.get(i) < cutoff)
      {
        result.remove(i);
        order.remove(i);//How did I miss this before? Mar 11, 2012 (1.0.2) for Marian
        i--;
      }
    
    return result;
  }

  /**
   * Transforms any object into a {@code Needle}. Special cases:
   * <ul>
   * <li><b>{@code Needle}</b>: Returns the provided object</li>
   * <li><b>{@link CharSequence} (Such as {@link String} or {@link StringPP})</b>: Extracts all the words and adds each to
   * the list of keywords, and sets the title to the first word</li>
   * <!--li><b>{@link ArrayPP}</b>: Uses the {@link ArrayPP#toSearchable()} method</li-->
   * </ul>
   * @param anObject The object to transform into a {@code Needle}
   * @return the resulting {@code Needle}
   * @version 1.1.0
   */
  public static Needle makeSearchable(Object anObject)
  {
    if (anObject instanceof Needle)
      return (Needle)anObject;
    
    Needle s = new DefDisplayableNeedle();
    
    if (anObject instanceof CharSequence)
    {
      StringPP spp = anObject instanceof StringPP ? (StringPP)anObject : new StringPP((CharSequence)anObject);
      for (int i=0; i < spp.getWordCount(); i++)
        s.getKeywords().add(new Keyword(spp.getWord(i).toString(), 1));
      s.setSearchTitle(spp.getWord(0).toString());
    }
    /*else if (anObject instanceof ArrayPP) commented out 2013-07-18 (1.1.0) due to portability changes in ArrayPP
    { 
      s = ((ArrayPP)anObject).toSearchable();
    }*/
    else
    {
      StringPP spp = StringPP.makeStringPP(String.valueOf(anObject)); // changed from new StringPP(anObject.toString()) 2013-07-18 (1.1.0) for better results
      for (int i=0; i < spp.getWordCount(); i++)
        s.getKeywords().add(new Keyword(spp.getWord(i).toString(), 1));
    }
    
    return s;
  }
}
