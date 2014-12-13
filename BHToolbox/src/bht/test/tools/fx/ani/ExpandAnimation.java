package bht.test.tools.fx.ani;

import bht.tools.util.dynamics.Bézier2D;
import bht.tools.util.dynamics.ProgressingValue;
import java.awt.Component;
import java.awt.Rectangle;
import javax.swing.JRootPane;

/**
 * ExpandAnimation, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2013 CC BY-SA 3.0.<HR/>
 * 
 * @author Kyli of Blue Husky Programming
 * @since 2013-00-11
 * @version 1.0.0
 */
public class ExpandAnimation extends DefCompActionAnimation
{
	private byte targetLocation;
	private boolean contracts = false;
	
	public ExpandAnimation(Component initTargetComponent)
	{
		this(CENTER, initTargetComponent);
	}
	
	public ExpandAnimation(byte initTargetLocation, Component initTargetComponent)
	{
		this(initTargetLocation, initTargetComponent, Bézier2D.DEFAULT);
	}
	
	public ExpandAnimation(byte initTargetLocation, Component initTargetComponent, ProgressingValue initMovementCurve)
	{
		this(initTargetLocation, initTargetComponent, initMovementCurve, false);
	}
	
	@SuppressWarnings("OverridableMethodCallInConstructor")
	public ExpandAnimation(byte initTargetLocation, Component initTargetComponent, ProgressingValue initMovementCurve, boolean contracts)
	{
//		super(0, initMovementCurve, initTargetComponent.getBounds(), null);
		super(initMovementCurve, true, initTargetComponent.getBounds(), initTargetComponent.getBounds(), false, 0, 1);
		setTargetLocation(initTargetLocation);
		setContracts(contracts);
	}
	
	public ExpandAnimation setTargetLocation(byte locationFlag)
	{
		switch(locationFlag)
		{
			case CENTER:
			case TOP:
			case TOP_RIGHT:
			case RIGHT:
			case BOTTOM_RIGHT:
			case BOTTOM:
			case BOTTOM_LEFT:
			case LEFT:
			case TOP_LEFT:
				return (ExpandAnimation)setEndLocation(getTargetLocationFor(getStartLocation(), targetLocation = locationFlag));
			default:
				throw new IllegalArgumentException("Location flag not supported: " + locationFlag);
		}
	}
	
	
  public static Rectangle getTargetLocationFor(Rectangle startLocation, byte locationFlag)
  {
    Rectangle targetLoc;
    switch(locationFlag)
    {
      case CENTER:
        targetLoc = new Rectangle((startLocation.width / 2) + startLocation.x,
                                  (startLocation.height / 2) + startLocation.y, 0, 0);
        break;
      case TOP:
        targetLoc = new Rectangle((startLocation.width / 2) + startLocation.x,
                                  startLocation.y, 0, 0);
        break;
      case RIGHT:
        targetLoc = new Rectangle(startLocation.width + startLocation.x,
                                  (startLocation.height / 2) + startLocation.y, 0, 0);
        break;
      case BOTTOM:
        targetLoc = new Rectangle((startLocation.width / 2) + startLocation.x,
                                  startLocation.height + startLocation.y, 0, 0);
        break;
      case LEFT:
        targetLoc = new Rectangle(startLocation.x,
                                  (startLocation.height / 2) + startLocation.y, 0, 0);
        break;
      case TOP_LEFT:
        targetLoc = new Rectangle(startLocation.x, startLocation.y, 0, 0);
        break;
      case TOP_RIGHT:
        targetLoc = new Rectangle(startLocation.width + startLocation.x,
                                  startLocation.y, 0, 0);
        break;
      case BOTTOM_LEFT:
        targetLoc = new Rectangle(startLocation.x,
                                  startLocation.height + startLocation.y, 0, 0);
        break;
      case BOTTOM_RIGHT:
        targetLoc = new Rectangle(startLocation.width + startLocation.x,
                                  startLocation.height + startLocation.y, 0, 0);
        break;
      default:
        targetLoc = null;
    }
    return targetLoc;
  }

	private ExpandAnimation setContracts(boolean newContracts)
	{
		contracts = newContracts;
		return this;
	}

	public boolean getContracts()
	{
		return contracts;
	}

	@Override
	public DefCompActionAnimation setAnimationPosition(double newPosition)
	{
		return super.setAnimationPosition(contracts ? 1 - newPosition : newPosition);
	}
}
