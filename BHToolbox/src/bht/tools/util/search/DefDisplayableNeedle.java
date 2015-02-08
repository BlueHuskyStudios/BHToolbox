package bht.tools.util.search;

import bht.tools.util.ArrayPP;
import bht.tools.util.StringPP;

/**
 * A default {@link Needle}, with all methods and constructors already implemented
 *
 * @author Supuhstar of Blue Husky Programming
 * @version 2.0.0
 *		- 2.0.0 (2015-02-04) - Kyli renamed DefSearchable to DefDisplayableNeedle
 * @since 1.6_23
 */
public class DefDisplayableNeedle<T> extends DefNeedle implements DisplayableNeedle<T>
{
	private ArrayPP<Keyword> keys = new ArrayPP<>();
	private StringPP title;
	private T display;

	public DefDisplayableNeedle()
	{
		this(null, null, null);
	}

	public DefDisplayableNeedle(ArrayPP<Keyword> keywords)
	{
		this(null, null, keywords);
	}

	public DefDisplayableNeedle(CharSequence title)
	{
		this(title, (T)null);
	}

	public DefDisplayableNeedle(CharSequence title, ArrayPP<Keyword> keywords)
	{
		this(title, null, keywords);
	}

	public DefDisplayableNeedle(T display)
	{
		this(display.toString(), display);
	}

	public DefDisplayableNeedle(T display, ArrayPP<Keyword> keywords)
	{
		this(display.toString(), display, keywords);
	}

	public DefDisplayableNeedle(CharSequence title, T display)
	{
		this(title, display, new ArrayPP<>(new Keyword(title, 1.0)));
	}

	public DefDisplayableNeedle(CharSequence title, T display, ArrayPP<Keyword> keywords)
	{
		super(title, keywords);
		this.display = display;
	}

	/**
	 * Calculates and returns a match strength between the two <t>DefDisplayableNeedle</tt>s depending on whether...
	 * <ol>
	 * <li>They are equal (tested with <tt>==</tt> and <tt>.equals</tt>)</li>
	 * <li>They have any or all matching keywords</li>
	 * <li>They have equal or similar titles</li>
	 * <li>They have equal displays</li>
	 * </ol>
	 *
	 * @param otherNeedle the other <tt>DefDisplayableNeedle</tt> to which this one will be compared
	 * @return the <tt>double</tt> representing the strength of the match between the two items
	 */
	@Override
	public double getMatchStrength(Needle otherNeedle)
	{
		if (otherNeedle == this || otherNeedle.equals(this))
			return 1;

		double strength = super.getMatchStrength(otherNeedle);

		if (otherNeedle instanceof DisplayableNeedle)
		{
			DisplayableNeedle displayableNeedle = (DisplayableNeedle)otherNeedle;
			if (display == displayableNeedle.getSearchDisplay())
				strength = Math.pow(strength, 0.05);
			else if (display.equals(displayableNeedle.getSearchDisplay()))
				strength = Math.pow(strength, 0.15);
		}

		return strength;
	}
	@Override
	public DefDisplayableNeedle setSearchDisplay(T newDisplay)
	{
		display = newDisplay;
		return this;
	}

	@Override
	public T getSearchDisplay()
	{
		return display;
	}
}
