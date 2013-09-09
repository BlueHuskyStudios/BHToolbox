/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bht.test.tools.fx.ani;

import bht.test.tools.fx.CompAction4;
import bht.tools.fx.Animation;
import bht.tools.util.dynamics.ProgressingValue;
import java.awt.Rectangle;

/**
 * An extension of {@link Animation} specifically for {@link CompAction4}. CompActionAnimation, made for BHToolbox, is made by
 * and copyrighted to Blue Husky Programming, ©2013 CC BY-SA 3.0.<HR/>
 *
 * @author Kyli of Blue Husky Programming
 * @since 2013-03-11
 * @version 1.0.0
 */
public interface CompActionAnimation extends Animation
{
	/**
	 * Change the movement curve of the animation.
	 *
	 * @param newPosition The new movement curve of the animation
	 * @return {@code this} object
	 */
	public CompActionAnimation setMovementCurve(ProgressingValue newMovementCurve);

	/**
	 * Returns the movement curve of the animation.
	 *
	 * @return The movement curve of the animation
	 */
	public ProgressingValue getMovementCurve();

	/**
	 * Returns the location of the component along the given curve at the given location
	 *
	 * @param movementCurve the curve along which the component should move
	 * @return the location at which the component should be along the given curve
	 */
	public Rectangle findLocation();

	/**
	 * Returns the opacity of the component along the given curve at the given location. If {@link #changesOpacity()} returns {@code false}, this method's return is meaningless
	 *
	 * @param movementCurve the curve along which the component should move
	 * @return the location at which the component should be along the given curve
	 */
	public double findOpacity();

	public CompActionAnimation setChangesLoation(boolean newChangesLocation);
	public boolean changesLoation();
	public CompActionAnimation setChangesOpacity(boolean newChangesOpacity);
	public boolean changesOpacity();
}