package bht.test.tools.fx;

import bht.test.tools.fx.ani.ExpandAnimation;
import bht.tools.util.BHTimer;
import bht.tools.util.dynamics.Bézier2D;
import bht.tools.util.dynamics.ProgressingValue;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CompAction4, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012 BH-1-PS.<hr/>
 * A re-creation of the {@link bht.tools.fx.CompAction} class. Instead of calculating where the component should be every time
 * the animation timer ticks, this version reads the position from a pre-rendered data value, represented by a
 * {@link ProgressingValue}
 *
 * @author Kyli of Blue Husky Studios
 * @version 4.0.0
 * @see bht.tools.fx.CompAction4
 * @see ProgressingValue
 */
public class CompAction4
{
	//<editor-fold defaultstate="collapsed" desc="Object Declarations">
	//<editor-fold defaultstate="collapsed" desc="Static objects">
	public static final ActionListener ACTION_EXIT;
	public static final double DEFAULT_FPS = 60, DEFAULT_DURATION = 1;
	public static final ProgressingValue CURVE_BEZIER, CURVE_LINEAR, CURVE_DEFAULT;
	public static final byte LOCATION_CENTER = 0,
			LOCATION_TOP_LEFT = 1, LOCATION_TOP_RIGHT = 2, LOCATION_BOTTOM_LEFT = 3, LOCATION_BOTTOM_RIGHT = 4,
			LOCATION_TOP = 5, LOCATION_RIGHT = 6, LOCATION_BOTTOM = 7, LOCATION_LEFT = 8;

	static
	{
		CURVE_BEZIER = new Bézier2D();
		CURVE_LINEAR = new ProgressingValue()
		{
			@Override public double getValueAtPosition(double position)
			{
				return position;
			}
		};
		CURVE_DEFAULT = CURVE_BEZIER;

		ACTION_EXIT = new ActionListener()
		{
			@Override public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		};
	}
	//</editor-fold>
	private BHTimer timer;
	//</editor-fold>

	//<editor-fold defaultstate="collapsed" desc="Morph">
	/**
	 * Moves and resizes the given component from its current location to the given location, following the given movement curve
	 *
	 * @param targetComponent The component to be morphed
	 * @param targetLocation The location to morph the component to
	 * @param movementCurve the curve that the animation will follow
	 * @param duration The amount of time, in seconds, that the animation will take up.
	 * @param FPS the number of frames to be rendered per second
	 * @param animationEndListener The action to be performed when the animation ends
	 */
	public void morphTo(final Component targetComponent, final Rectangle targetLocation,
						final ProgressingValue movementCurve,
						final double duration, final double FPS,
						final ActionListener animationEndListener)
	{
		final Dimension O_MIN = targetComponent.getMinimumSize(), // So the animation can go smoothly, and still retain the
				O_MAX = targetComponent.getMaximumSize(); // programmer's intended UX
		targetComponent.setMinimumSize(null);
		targetComponent.setMaximumSize(null);

		timer = new BHTimer(new ActionListener()
		{
			long numTicks = (long) (FPS * duration), tick = 0;
			private int compX = targetComponent.getX(),
					compY = targetComponent.getY(),
					compWidth = targetComponent.getWidth(),
					compHeight = targetComponent.getHeight();

			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (tick == numTicks) // End condition
				{
					if (animationEndListener != null)
						animationEndListener.actionPerformed(e);
					timer.reset();
					targetComponent.setMinimumSize(O_MIN); // Reset to programmer's intended UX
					targetComponent.setMaximumSize(O_MAX);
				}
				double position = movementCurve.getValueAtPosition((double) tick++ / numTicks);
				targetComponent.setBounds((int) (compX + ((targetLocation.x - compX) * position)),
										  (int) (compY + ((targetLocation.y - compY) * position)),
										  (int) (compWidth + ((targetLocation.width - compWidth) * position)),
										  (int) (compHeight + ((targetLocation.height - compHeight) * position)));
			}
		}, (long) (1000 / FPS));
		timer.start();
	}

	public void morphTo(Component targetComponent, Rectangle targetRectangle,
						ProgressingValue movementCurve,
						double duration,
						ActionListener endAction)
	{
		morphTo(targetComponent, targetRectangle, movementCurve, duration, DEFAULT_FPS, null);
	}

	public void morphTo(Component targetComponent, Rectangle targetRectangle,
						ProgressingValue movementCurve,
						double duration)
	{
		morphTo(targetComponent, targetRectangle, movementCurve, duration, DEFAULT_FPS, null);
	}

	public void morphTo(Component targetComponent, Rectangle targetRectangle,
						double duration)
	{
		morphTo(targetComponent, targetRectangle, CURVE_DEFAULT, duration);
	}

	public void morphTo(Component targetComponent, Rectangle targetRectangle)
	{
		morphTo(targetComponent, targetRectangle, 1);
	}
	//</editor-fold>

	//<editor-fold defaultstate="collapsed" desc="ExpandAnimation">
	//<editor-fold defaultstate="collapsed" desc="ExpandAnimation Closed To...">
	public void slideClosedTo(final Component targetComponent, byte locationFlag,
							  ProgressingValue movementCurve,
							  double duration,
							  final ActionListener endAction)
	{
		Rectangle targetLoc = ExpandAnimation.getTargetLocationFor(targetComponent.getBounds(), locationFlag);
		if (targetLoc == null)
		{
			Logger.getGlobal().log(Level.SEVERE, "Sliding closed does not support location {0}", locationFlag);
			return;
		}
		
		
		morphTo(targetComponent, targetLoc, movementCurve, duration, DEFAULT_FPS, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				targetComponent.setVisible(false);
				if (endAction != null)
					endAction.actionPerformed(e);
			}
		});
	}

	public void slideClosedTo(Component targetComponent, byte locationFlag)
	{
		slideClosedTo(targetComponent, locationFlag, CURVE_DEFAULT, DEFAULT_FPS, null);
	}

	public void slideOpenFrom(final Component targetComponent, byte locationFlag,
							  ProgressingValue movementCurve,
							  double duration,
							  final ActionListener endAction)
	{
		Rectangle fromLoc = ExpandAnimation.getTargetLocationFor(targetComponent.getBounds(), locationFlag),
				targetLoc = targetComponent.getBounds();
		if (fromLoc == null)
		{
			Logger.getGlobal().log(Level.SEVERE, "Sliding open does not support location {0}", locationFlag);
			return;
		}
		targetComponent.setBounds(fromLoc);
		targetComponent.setVisible(true);
		morphTo(targetComponent, targetLoc, movementCurve, duration, DEFAULT_FPS, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (endAction != null)
					endAction.actionPerformed(e);
			}
		});
	}

	public void slideOpenFrom(Component targetComponent, byte locationFlag)
	{
		slideOpenFrom(targetComponent, locationFlag, CURVE_DEFAULT, DEFAULT_DURATION, null);
	}
	//</editor-fold>
	//</editor-fold>
}
