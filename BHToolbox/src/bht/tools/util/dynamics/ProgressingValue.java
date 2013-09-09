package bht.tools.util.dynamics;

/**
 * ProgressingValue, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012. License is default.<hr/>
 *
 * @author Supuhstar of Blue Husky Programming
 * @since Nov 8, 2012
 * @version 1.0.0
 */
public abstract class ProgressingValue //NOTE: Must be compiled in UTF-8
{
	/**
	 * Returns the value represented by this {@code ProgressingValue} when the observer is looking at the given position
	 *
	 * @param position the position to observe
	 * @return the value a the observed position
	 */
	public abstract double getValueAtPosition(double position);
	
//	/**
//	 * Returns a reverse version of this {@code ProgressingValue}
//	 * 
//	 * @return a reverse version of this {@code ProgressingValue}
//	 */
//	public abstract ProgressingValue getReverse();
}
