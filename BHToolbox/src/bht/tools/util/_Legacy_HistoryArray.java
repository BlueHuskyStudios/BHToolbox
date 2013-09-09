package bht.tools.util;


/**
 * This class was made for back/forward buttons, and can be easily used for such. Other possible uses include undo/redo.
 * @param <T> The type of objects with which this class will be dealing.
 * @author Supuhstar
 */
class _Legacy_HistoryArray<T>
{
  T array[];
  int current = 0;
  public static final int DEF_SIZE = 4;

  /**
   * Creates a new <tt>_Legacy_HistoryArray</tt> with <tt>DEF_SIZE</tt> reserved spots.<br/>
   * <b>Same as <tt>new _Legacy_HistoryArray(DEF_SIZE)</tt>.</b>
   */
  public _Legacy_HistoryArray()
  {
    this(DEF_SIZE);
  }

  /**
   * Creates a new <tt>_Legacy_HistoryArray</tt> with <tt>initSize</tt> reserved spots.<br/>
   * @param initSize The number of spots to reserve from the start. <b>Must be larger than 0.</b>
   */
  @SuppressWarnings("unchecked")
  public _Legacy_HistoryArray(int initSize)
  {
    array = (T[])new Object[initSize == 0 ? 1 : initSize];
  }

  public void add(T hist)
  {
    trimTo(current);
    int last = -1;
    for (int i = 0; i < array.length; i++)
    {
      if (array[i] == null)
      {
        last = i;
        break;
      }
    }
    if (last == -1)
    {
      @SuppressWarnings("unchecked")
      T[] temp = (T[])new Object[array.length * 2];
      System.arraycopy(array, 0, temp, 0, array.length);
      temp[array.length] = hist;
      array = temp;
      current = array.length;
    }
    else
    {
      array[last] = hist;
      current = last;
    }
  }

  public void trimTo(int index)
  {
    @SuppressWarnings("unchecked")
    T temp[] = (T[])new Object[index + 1];
    System.arraycopy(array, 0, temp, 0, index + 1);
    array = temp;
    current = index;
  }

  public T get(int index)
  {
    if (array[index] != null)
    return array[index];
    throw new IndexOutOfBoundsException("Index " + index + " is out of the range 0-" + getLastIndex());
  }

  public T getCurrent()
  {
    return get(current);
  }

  public int getCurrentIndex()
  {
    return current;
  }

  public T[] getBackHist()
  {
    return java.util.Arrays.copyOfRange(array, 0, current);
  }

  public T[] getNextHist()
  {
    return java.util.Arrays.copyOfRange(array, current + 1, array.length);
  }

  public int goTo(int index)
  {
    if (canGoTo(index))
      return current = index;
    throw new IndexOutOfBoundsException("Cannot go to " + index);
  }

  public int goBack()
  {
    if (canGoBack())
      return current--;
    throw new IndexOutOfBoundsException("Cannot go back any further");
  }

  public int goNext()
  {
    if (canGoNext())
      return current++;
    throw new IndexOutOfBoundsException("Cannot go forward any further");
  }

  public boolean canGoTo(int index)
  {
    return index >= 0 && index <= getLastIndex();
  }

  public boolean canGoBack()
  {
    return current > 0;
  }

  public boolean canGoNext()
  {
    return current < getLastIndex();
  }

  @SuppressWarnings("empty-statement")
  public int getLastIndex()
  {
    if (array[array.length - 1] != null)
      return array.length - 1;
    int i;
    for (i = 0; array[i] != null && i < array.length; i++);
    return i - 1;
  }
  
  @Override
  public String toString()
  {
    String ret = "";
    for (int i=0, j=0; i < array.length; i++)
    {
      if (array[i] != null)
      {
        j++;
        ret += (i == current ? "  >" : j + ": ") + array[i] + "\n";
      }
    }
    return ret;
  }

  int length()
  {
    return getLastIndex() + 1;
  }
}
