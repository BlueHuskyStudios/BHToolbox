package bht.test.tools.fx.ani;

import java.util.EventListener;


/**
 * AnimationListener, made for BHToolbox, is copyright Blue Husky Programming ©2013
 *
 * @author Supuhstar of Blue Husky Programming
 * @version 1.0.0
 * @since 2013-05-17
 */
public interface AnimationListener extends EventListener
{
	/**
	 * Alerts that the animation is about to start
	 * @param e the even describing the animation
	 */
	public void animationStarting(AnimationEvent e);
	/**
	 * Alerts that the animation is complete
	 * @param e the even describing the animation
	 */
	public void animationCompleted(AnimationEvent e);
}