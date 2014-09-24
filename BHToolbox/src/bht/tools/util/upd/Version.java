package bht.tools.util.upd;

import bht.tools.util.ArrayPP;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Version, made for BHToolbox, is copyright Blue Husky Programming ©2014 GPLv3<HR/>
 * 
 * @author Kyli of Blue Husky Programming
 * @version 1.0.0
 * @since 2014-09-22
 */
public class Version implements Comparable<Version>
{
	/** Defines what a version looks like: {@code \d(\.\d)*} */
	public static final Pattern VERSION_PATTERN = Pattern.compile("\\d(\\.\\d)*");
	
	public final Integer[] STAGES;
	private String cache;

	/*public Version(int... stages)
	{
		STAGES = new Integer[stages.length];
		for (int i = 0; i < stages.length; i++)
			STAGES[i] = stages[i];
	}*/
	
	public Version(Integer... stages)
	{
		STAGES = stages;
	}

	@Override
	public String toString()
	{
		if (cache != null)
			return cache;
		return cache = new ArrayPP<>(STAGES).toString("", ".", "");
	}
	
	
	
	/**
	 * Converts the given string into a Version. The string must be formatted to this regex: {@code \d(\.\d)*}
	 * 
	 * @param vStr A valid version string (e.g. {@code 12}, {@code 1.2}, {@code 1.23.4567}, etc.)
	 * @return A {@link Version} made from the given string
	 */
	public static Version fromString(String vStr)
	{
		if (!VERSION_PATTERN.matcher(vStr).matches())
			throw new IllegalArgumentException("Given string is not a valid version number: " + vStr);
		
		String[] numStrings = Pattern.compile("\\.").split(vStr);
		Integer[] nums = new Integer[numStrings.length];
		
		for (int i = 0; i < numStrings.length; i++)
			nums[i] = Integer.valueOf(numStrings[i]);
		return new Version(nums);
	}

	@Override
	public int hashCode()
	{
		int hash = 3;
		hash = 29 * hash + Arrays.deepHashCode(this.STAGES);
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final Version other = (Version) obj;
		if (!Arrays.deepEquals(this.STAGES, other.STAGES)) return false;
		return true;
	}
	
	

	/**
	 * <ol>
	 *	<li>If the given object is this object, {@code 0} is returned</li>
	 *	<li>Else, if any one of this Version's stages is different from the other Version's corresponding stage, the difference
	 *		is returned</li>
	 *	<li>Else, the difference between the number of stages is returned.</li>
	 * </ol>
	 * @param o
	 * @return 
	 */
	@Override
	public int compareTo(Version o)
	{
		if (this.equals(o))
			return 0;
		for(int i = 0, l = Math.min(STAGES.length, o.STAGES.length); i < l; i++)
			if (!STAGES[i].equals(o.STAGES[i]))
				return STAGES[i] - o.STAGES[i];
		return STAGES.length - o.STAGES.length;
	}
}