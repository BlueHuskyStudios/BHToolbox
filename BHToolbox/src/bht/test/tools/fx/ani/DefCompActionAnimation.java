package bht.test.tools.fx.ani;

import bht.tools.util.dynamics.ProgressingValue;
import java.awt.Rectangle;

/**
 * DefCompActionAnimation, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2013 CC BY-SA 3.0.<hr>
 * a default implementation of {@link CompActionAnimation}
 * @author Kyli of Blue Husky Programming
 * @since 2013-00-11
 * @version 1.0.0
 */
public class DefCompActionAnimation implements CompActionAnimation
{
	private double p;
	public final static byte CENTER = 0, TOP = 1, TOP_RIGHT = 2, RIGHT = 3, BOTTOM_RIGHT = 4, BOTTOM = 5, BOTTOM_LEFT = 6,
	LEFT = 7, TOP_LEFT = 8;
	private Rectangle sLoc, eLoc;
	private ProgressingValue movementCurve;
	private boolean moves, fades;
	private double sOpac, eOpac;
	
	static
	{
	}

	/**
	 * Creates a new generic component animation. 
	 * @param initPosition The initial position of the animation, from {@code 0.0} to {@code 1.0}
	 * @param initStartLocation The initial location of the component
	 * @param initEndLocation The final location of the component
	 */
	public DefCompActionAnimation(
			ProgressingValue initMovementCurve,
			boolean initChangesLocation, Rectangle initStartLocation, Rectangle initEndLocation,
			boolean initChangesOpacity, double initStartOpacity, double initEndOpacity)
	{
		movementCurve = initMovementCurve;
		
		moves = initChangesLocation;
		sLoc = initStartLocation;
		eLoc = initEndLocation;
		
		fades = initChangesOpacity;
		sOpac = initStartOpacity;
		eOpac = initEndOpacity;
	}

	@Override
	public DefCompActionAnimation setAnimationPosition(double newPosition)
	{
		p = newPosition;
		return this;
	}

	@Override
	public double getAnimationPosition()
	{
		return p;
	}

	protected Rectangle getStartLocation()
	{
		return sLoc;
	}

	protected DefCompActionAnimation setStartLocation(Rectangle newStartLocation)
	{
		sLoc = newStartLocation;
		return this;
	}

	protected Rectangle getEndLocation()
	{
		return eLoc;
	}

	protected DefCompActionAnimation setEndLocation(Rectangle newEndLocation)
	{
		eLoc = newEndLocation;
		return this;
	}
	
	@Override
	public Rectangle findLocation()
	{
        double curveHeight = movementCurve.getValueAtPosition(p);
        return new Rectangle((int)(sLoc.x      + ((eLoc.x      - sLoc.x)      * curveHeight)),
                             (int)(sLoc.y      + ((eLoc.y      - sLoc.y)      * curveHeight)),
                             (int)(sLoc.width  + ((eLoc.width  - sLoc.width)  * curveHeight)),
                             (int)(sLoc.height + ((eLoc.height - sLoc.height) * curveHeight)));
	}

	@Override
	public CompActionAnimation setMovementCurve(ProgressingValue newMovementCurve)
	{
		movementCurve = newMovementCurve;
		return this;
	}

	@Override
	public ProgressingValue getMovementCurve()
	{
		return movementCurve;
	}

	@Override
	public double findOpacity()
	{
		double min = Math.min(sOpac, eOpac);
		return (
				movementCurve.getValueAtPosition(p) // Get the raw position of the movement curve
				* (
					Math.max(sOpac, eOpac) - min // Get the range of opacity values, anchored to a minimum of 0
				)// Multiply these to get the scaled position along the movement curve
			)
			+ min; // Add the minimum opacity to remove the anchor value
	}

	@Override
	public boolean changesLoation()
	{
		return moves;
	}

	@Override
	public DefCompActionAnimation setChangesLoation(boolean newChangesLocation)
	{
		moves = newChangesLocation;
		return this;
	}

	@Override
	public boolean changesOpacity()
	{
		return fades;
	}

	@Override
	public DefCompActionAnimation setChangesOpacity(boolean newChangesOpacity)
	{
		fades = newChangesOpacity;
		return this;
	}
}
