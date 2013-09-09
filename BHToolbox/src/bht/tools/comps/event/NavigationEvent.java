package bht.tools.comps.event;

/**
 * NavigationEvent, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012. License is default.<hr/>
 * @author Supuhstar of Blue Husky Programming
 * @since Apr 11, 2012
 * @version 1.0.0
 */
public class NavigationEvent extends java.util.EventObject //NOTE: Must be compiled in UTF-8
{
  private final Number oldPosition, newPosition;
  private final Object oldObject, newObject;
  private final byte state;
  public static final byte STATE_GOING_BACKWARD = 0b00, STATE_GOING_FORWARD = 0b01, STATE_SKIPPING = 0b10;

  /**
   * Creates a new {@link NavigationEvent}, assigning all the required attributes to it.
   * @param source The object on which the Event initially occurred.
   * @param oldPosition The position before navigation occurred
   * @param newPosition The position after navigation occurred
   * @param oldObject The object that was being observed before navigation occurred
   * @param newObject The object that was being observed before navigation occurred
   * @param state The type of navigation. Valid inputs are {@link #STATE_GOING_BACKWARD} ({@value #STATE_GOING_BACKWARD}),
   * {@link #STATE_GOING_FORWARD} ({@value #STATE_GOING_FORWARD}), and {@link #STATE_SKIPPING} ({@value #STATE_SKIPPING})
   */
  public NavigationEvent(Object source, Number oldPosition, Number newPosition, Object oldObject, Object newObject, byte state)
  {
    super(source);
    
    if ((state | 0b11) != 0)
      throw new IllegalArgumentException("State must be STATE_GOING_BACKWARD (" + STATE_GOING_BACKWARD +
                                         "), STATE_GOING_FORWARD (" + STATE_GOING_FORWARD + "), or STATE_SKIPPING (" +
                                         STATE_SKIPPING + "), but was \"" + state + "\"");
    
    this.oldPosition = oldPosition;
    this.newPosition = newPosition;
    this.oldObject = oldObject;
    this.newObject = newObject;
    this.state = state;
  }

  public Number getNewPosition()
  {
    return newPosition;
  }

  public Number getOldPosition()
  {
    return oldPosition;
  }

  public Object getNewObject()
  {
    return newObject;
  }

  public Object getOldObject()
  {
    return oldObject;
  }

  public byte getState()
  {
    return state;
  }
}
