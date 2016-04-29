package bht.tools.comps.event;

/**
 * StateChangeEvent, made for BH Checkers 2, is made by and copyrighted to Blue Husky Programming, ©2012 BH-1-PS.<hr>
 * @author Supuhstar of Blue Husky Programming
 * @since Jan 19, 2012
 * @version 1.0.0
 */
public class StateChangeEvent //NOTE: Must be compiled in UTF-8
{
  private Object source;
  private byte state;
  
  public StateChangeEvent(Object source, boolean oldState, boolean newState)
  {
    this.source = source;
    if (oldState)
      state /*|*/= 0b01;//Since this is the first change of the state variable, we initialize it instead of modify it. The | operator is commented out as a reminder that, if this becomes a later-than-first change, it should be used
    if (newState)
      state |= 0b10;
  }
  
  public boolean getOldState()
  {
    return (state & 0b01) != 0;
  }
  
  public boolean getNewState()
  {
    return (state & 0b10) != 0;
  }
}
