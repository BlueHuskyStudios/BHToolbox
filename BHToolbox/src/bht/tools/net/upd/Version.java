package bht.tools.net.upd;

import bht.tools.util.ArrayPP;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Version, made for BHToolbox, is copyright Blue Husky Programming ©2014 BH-1-PS<HR/>
 * 
 * @author Kyli of Blue Husky Programming
 * @version 1.1.0
 *		- 1.1.1 (2014-11-29) - Kyli Rouge Changed version pattern from \d(\.\d)* to \d+(\.\d+)*
 *		- 1.1.0 (2014-11-29) - Kyli Rouge added support for channels
 * @since 2014-09-22
 */
public class Version implements Comparable<Version>
{
	/** Defines what a version looks like: {@code \d+(\.\d+)*} */
	public static final Pattern VERSION_PATTERN = Pattern.compile("\\d+(\\.\\d+)*");
	
	public final Integer[] STAGES;
	private String cache;
	private Channel channel = Channel.STABLE;

	/*public Version(int... stages)
	{
		STAGES = new Integer[stages.length];
		for (int i = 0; i < stages.length; i++)
			STAGES[i] = stages[i];
	}*/
	
	/**
	 * Creates a version number with the given stages.
	 * For instance, if it's version 1.2.3, you would call {@code new Version(1,2,3)}
	 * 
	 * @param stages the stages of the version number
	 */
	public Version(Integer... stages)
	{
		STAGES = stages;
	}

	/**
	 * Returns the channel that this version is on
	 * @return the channel that this version is on
	 */
	public Channel getChannel()
	{
		return channel;
	}

	/**
	 * Changes the channel that this version is on
	 * @param newChannel the new channel for this version to be on
	 * @return {@code this}
	 */
	public Version setChannel(Channel newChannel)
	{
		channel = newChannel;
		return this;
	}

	/**
	 * Returns a string representation of the version, like: {@code "1.2.3β"}
	 * @return a string representation of the version
	 */
	@Override
	public String toString()
	{
		if (cache != null)
			return cache;
		return cache = new ArrayPP<>(STAGES).toString("", ".", "").concat(Character.toString(channel.UNICODE));
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

	/**
	 * Evaluates whether these are equal.
	 * Evaluation happens in this order:
	 * <OL>
	 *	<LI>check with == (yes? true)</LI>
	 *	<LI>whether obj is null (yes? false)</LI>
	 *	<LI>whether they're the same class (no? false)</LI>
	 *	<LI>whether their channels are equal (no? false)</LI>
	 *	<LI>whether their stages are equal (no? false)</LI>
	 *	<LI>if all of the above are not tripped, return true</LI>
	 * </OL>
	 * @param obj the other object to test
	 * @return {@code true} iff both this and the other object are equal
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final Version other = (Version) obj;
		if (!this.channel.equals(other.channel)) return false;
		if (!Arrays.deepEquals(this.STAGES, other.STAGES)) return false;
		return true;
	}
	
	

	/**
	 * <ol>
	 *	<li>If the given object is this object, {@code 0} is returned</li>
	 *	<li>Else, if the given object's channel is different from the other Version's, the difference in ordinals is returned</li>
	 *	<li>Else, if any one of this Version's stages is different from the other Version's corresponding stage, the difference
	 *		is returned</li>
	 *	<li>Else, the difference between the number of stages is returned.</li>
	 * </ol>
	 * @param o the other object to compare this one against
	 * @return an integer, centered around 0, telling how much more or less this object is than the other.
	 */
	@Override
	public int compareTo(Version o)
	{
		if (equals(o))
			return 0;
		if (channel != o.channel)
			return channel.ordinal() - o.channel.ordinal();
		for(int i = 0, l = Math.min(STAGES.length, o.STAGES.length); i < l; i++)
			if (!STAGES[i].equals(o.STAGES[i]))
				return STAGES[i] - o.STAGES[i];
		return STAGES.length - o.STAGES.length;
	}
	
	/**
	 * Represents a channel (stable, beta, alpha, lambda)
	 */
	public static enum Channel
	{
		/**
		 * Signifies that this software is in very unstable or incomplete testing, and the next major release should be an
		 * unstable alpha test.
		 * 
		 * <DL>
		 *	<DT>ASCII</DT>
		 *		<DD>{@code l} (108)</DD>
		 *	<DT>Unicode</DT>
		 *		<DD>U+03BB Greek Small Letter Lambda</DD>
		 *	<DT>HTML</DT>
		 *		<DD>{@code "&lambda;"}</DD>
		 * </DL>
		 */
		LAMBDA('l', 'λ', "&lambda;"),
		/**
		 * Signifies that this software is in unstable alpha testing, and the next major release should be a little more stable
		 * (beta).
		 * 
		 * <DL>
		 *	<DT>ASCII</DT>
		 *		<DD>{@code a} (97)</DD>
		 *	<DT>Unicode</DT>
		 *		<DD>U+03B1 Greek Small Letter Alpha</DD>
		 *	<DT>HTML</DT>
		 *		<DD>{@code "&alpha;"}</DD>
		 * </DL>
		 */
		ALPHA('a', 'α', "&alpha;"),
		/**
		 * Signifies that this software is in beta testing, and the next major release should be stable.
		 * 
		 * <DL>
		 *	<DT>ASCII</DT>
		 *		<DD>{@code b} (98)</DD>
		 *	<DT>Unicode</DT>
		 *		<DD>U+03B2 Greek Small Letter Beta</DD>
		 *	<DT>HTML</DT>
		 *		<DD>{@code "&beta;"}</DD>
		 * </DL>
		 */
		BETA('b', 'β', "&beta;"),
		/**
		 * Signifies that this software is stable. This is the only channel without a symbol.
		 * 
		 * <DL>
		 *	<DT>ASCII</DT>
		 *		<DD>space (20)</DD>
		 *	<DT>Unicode</DT>
		 *		<DD>U+200C Zero-Width Non-Joiner</DD>
		 *	<DT>HTML</DT>
		 *		<DD>the empty string ({@code ""})</DD>
		 * </DL>
		 */
		STABLE;
		
		final char ASCII, UNICODE;
		final String HTML;
		private Channel()
		{
			          // U+200C ZERO-WIDTH NON-JOINER
			this(' ', (char)0x200C, "");
		}
		private Channel(char ascii, char unicode, String html)
		{
			ASCII = ascii;
			UNICODE = unicode;
			HTML = html;
		}
	}
}