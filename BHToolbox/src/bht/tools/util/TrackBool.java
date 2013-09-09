/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package bht.tools.util;

/**
 * Automatically keeps track of up to 68,719,476,736 booleans in an extremely memory-efficient way
 * @author Supuhstar
 */
public class TrackBool
{
  private final long POS;//gotta get rid of this, somehow
  private static final ArrayPP<Integer> LIST = new ArrayPP<>();
  
  public TrackBool(boolean initState)
  {
    addBoolToList(initState);
    POS = getListLength();
  }

  private static void addBoolToList(boolean initState)
  {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  private static long getListLength()
  {
    return LIST.length();
  }
  
  public boolean getState()
  {
    return getStateOfPosition(POS);
  }

  private static boolean getStateOfPosition(long POS)
  {
    int index=0;
    for (int i : LIST)
    {
      if (index * 32 >= POS)//If the loop has already passed the value to be observed
      {
        long l=1;
        for (long c=0, t = (POS + 32); c < t; c++)//Shift the value to the left until the only 1 in this long is underneath the value to be fetched
          l = l << 1;
        return (i & l) != 0;//This will return true if the target bit is 1
      }
      
      index++;
    }
    throw new UnknownError("The program shouldn't have failed, but it did.");
  }
  
  public void setState(boolean newState)
  {
    setStateOfPosition(POS, newState);
  }

  private static void setStateOfPosition(long POS, boolean newState)
  {
    for (int i=0, i32=0, len=LIST.length(); i < len; i++, i32 = i * 32)
    {
      if (i32 >= POS)//If the loop has already passed the value to be observed
      {
        int l=1;
        for (long c=0, t = POS - (i32); c < t; c++)//Shift the value to the left until the 1 in this long is underneath the value to be fetched
          l = l << 1;
        if (newState ? (LIST.get(i) & l) == 0 : (LIST.get(i) & l) != 0)//If the bit is not already set to that state
          LIST.set(i, LIST.get(i) ^ l);//Toggle the given bit
        return;
      }
    }
    throw new UnknownError("The program shouldn't have failed, but it did.");
  }
}
