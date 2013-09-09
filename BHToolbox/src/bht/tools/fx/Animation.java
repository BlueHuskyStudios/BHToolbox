package bht.tools.fx;

/**
 * Defines the accessibility and behavior of an animation
 *
 * @author Kyli
 */
public interface Animation
{
	/**
	 * This is designed so that animations can be smoothly scaled, rewinded, and skipped without bugs. When this method is
	 * called, the object in question should snap immediately to the part of its animation defined by the given percentage.
	 *
	 * For example, if the animation is a square moving from the left of the screen to the right, passing this method the value
	 * {@code 0.25} will place the box 25% of the way across the screen without transitioning, regardless of its previous
	 * position on the screen.
	 *
	 * @param percentage the position to which the animation should snap. This should be in the range {@code (0, 1)}. It's
	 * recommended that if the method is passed a {@code 0.0}, then the object in question is set to a state of no animation, as
	 * if it has not yet been affected, whereas if it is passed a {@code 1.0}, the object is set to a state of no animation, as
	 * if it has already been completely affected. Any state in between these should reflect a logical progression.
	 * @return {@code this} object
	 */
	public Animation setAnimationPosition(double percentage);

	/**
	 * Returns the animation's current position in the range {@code (0, 1)}. <STRONG>This does <EM>not</EM> imply any prediction
	 * about how long the animation will take, nor which direction the animation is going.</STRONG> For instance, if this
	 * returns {@code 0.98} on one call and {@code 0.99} on the next, it might return {@code 0.50}, {@code 1.00}, or any other
	 * value on the next. <STRONG>Do not use this method for prediction.</STRONG>
	 *
	 * @return a {@code double} within the range {@code (0, 1)}, indicating the current animation position
	 */
	public double getAnimationPosition();
}
