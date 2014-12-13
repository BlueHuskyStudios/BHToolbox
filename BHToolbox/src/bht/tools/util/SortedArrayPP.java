package bht.tools.util;

/**
 * SortedArrayPP, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012.<hr/>
 * An extension of ArrayPP that automatically sorts itself when something is added to it.
 * @param <T> The type of objects in the array. MUST implement {@link Comparable}
 * @author Supuhstar of Blue Husky Programming
 * @since Jan 5, 2012
 * @version 1.1.0
 * @see ArrayPP
 */
public class SortedArrayPP<T extends Comparable> extends ArrayPP<T>
{

  /**
   * Inserts the given values into the array at sorted positions
   * @param vals the values to be inserted
   * @return the resulting array
   */
  @Override
  public ArrayPP<T> add(T... vals)
  {
    valLoop: for (int thisCount, valCount = 0, thisLength = length(), valLength = vals.length; valCount < valLength; valCount++)
    {
      for (thisCount=0; thisCount<thisLength; thisCount++)
      {
        if ((vals[valCount]).compareTo(get(thisCount)) < 0)
        {
          insert(vals[valCount], thisCount);
          continue valLoop;
        }
      }
    }
    
    return this;
  }

  /**
   * Inserts the given values into the array at sorted positions
   * @param vals the values to be inserted
   * @return the resulting array
   * @see #add(T[]) 
   */
  @Override
  public ArrayPP<T> addAll(Iterable<T> vals)
  {
    for (T t : vals)
    {
      add(t);
    }
    return this;
  }

  /**
   * 
   * @param strengthMarkers
   * @param vals
   * @see #bubbleSort(bht.tools.util.ArrayPP)
   * @return 
   */
  public ArrayPP<T> add(ArrayPP<Double> strengthMarkers, T... vals)
  {
    super.add(vals);
    return bubbleSort(strengthMarkers);
  }


  /**
   * 
   * @param strengthMarkers
   * @param vals
   * @see #bubbleSort(bht.tools.util.ArrayPP)
   * @return 
   */
  public ArrayPP<T> addAll(ArrayPP<Double> strengthMarkers, Iterable<T> vals)
  {
    super.addAll(vals);
    return bubbleSort(strengthMarkers);
  }
}
