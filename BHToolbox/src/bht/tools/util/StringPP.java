package bht.tools.util;

import bht.tools.misc.ToPrimitives;
import bht.tools.util.math.Numbers;
import bht.tools.util.save.Saveable;
import java.util.Iterator;

/**
 * <strong>Copyright Blue Husky Programming, © 2011</strong><br/> {@code StringPP} is to {@code String} as {@code ArrayPP} is to
 * arrays. This class was made to give enhanced usability to character strings, with several convenience methods and several
 * interface implementations. For instance, {@code java.lang.String} gives several convenience methods, such as
 * {@code equalsIgnoreCase}; however, that method only takes in another {@code java.lang.String}
 *
 * @author Supuhstar
 * @since 1.6_23
 * @version 1.4.0
 */
public class StringPP implements java.io.Serializable, Comparable<CharSequence>, CharSequence, /*Iterable<StringPP>, */
								 Iterable<Character>, ToPrimitives, Saveable
{
	private ArrayPP<Character> chars;

//  Covered by public StringPP(char... c)
//  /**
//   * Creates a new {@code String++} containing no characters
//   */
//  public StringPP()
//  {
//    this("");
//  }
	/**
	 * Creates a new {@code String++} with the same characters as those in the provided {@code CharSequence}
	 *
	 * @param cs the characters to be put into the new {@code String++}, as contained in a {@code CharSequence}
	 */
	public StringPP(CharSequence cs)
	{
		this.chars = new ArrayPP<>();
		if (cs != null)
			for (int i = 0; i < cs.length(); i++)
				this.chars.add(cs.charAt(i));
	}

	/**
	 * Creates a new {@code String++} with the same characters as those in the provided {@code ArrayPP} of {@code Character}s
	 *
	 * @param array the characters to be put into the new {@code String++}, as contained in an {@code ArrayPP} of
	 * {@code Character}s
	 */
	public StringPP(ArrayPP<Character> array)
	{
		this.chars = array.clone();
	}

	/**
	 * Creates a new {@code String++} with the characters provided. This is also the catch-all constructor that supports the
	 * empty parameter list ({@code new StringPP()})
	 *
	 * @param c the characters to be added to the new {@code String++}
	 */
	public StringPP(char... c)
	{
		Character[] cs = new Character[c.length];
		for (int i = 0; i < cs.length; i++)
			cs[i]/*YYYYYYYYYYYYYYYEEEEEEEEEEEEEEEEEEEEEEEEEAAAAAAAAAAAAAAAAAAAAAHHHHHHHHHHHHHH!!!!!!!!!!!!*/ = c[i];
		this.chars = new ArrayPP<>(cs);
	}

	/**
	 * Creates a new {@code String++} from the given {@code Object}.
	 * <UL>
	 * <LI>If {@code o} is {@code null}, then this {@code String++} is empty</LI>
	 * <LI>If {@code o} is an {@link Iterable} or an array, then this {@code String++} is comprised of all items in {@code o}
	 * concatenated together with no delimeter</LI>
	 * <LI>Else, this {@code String++} is {@code o}'s {@code toString()} method</LI>
	 * </UL>
	 *
	 * @param o the object to make into a {@code String++}
	 * @version 1.0.0
	 * @since 2013-07-18 (1.4.0)
	 */
	public StringPP(Object o)
	{
		this();
		if (o == null)
			return;
		if (o instanceof Iterable)
			chars.addAll((Iterable) o);
		else if (o.getClass().isArray())
			for (Object item : (Object[]) o)
				if (item != null)
					append(String.valueOf(item));
				else
					append(String.valueOf(o));

		//((ArrayPP) o).t[0].getClass().isInstance(Character)
	}

	/**
	 * If the given {@link CharSequence} is already a {@link StringPP}, then it is returned. Else, a new {@link StringPP} is
	 * created out of it
	 *
	 * @param o the {@link CharSequence} to be turned into a {@link StringPP}
	 * @return a {@link StringPP} made out of the given {@link CharSequence}
	 * @since 2012/11/12 (1.3.3) for BHIM try 2
	 */
//	 * @deprecated use {@link #StringPP(java.lang.Object)} instead
	public static StringPP makeStringPP(Object o)
	{
		if (o instanceof StringPP)
			return (StringPP) o;
		return new StringPP(o);
	}

	/**
	 * Returns an array of {@code char}s, each one representing a character in this {@code String++}
	 *
	 * @return an array of {@code char}s, each one representing a character in this {@code String++}
	 */
	public char[] toCharArray()
	{
		char[] c = new char[length()];
		for (int i = 0; i < c.length; i++)
			c[i] = charAt(i);
		return c;
	}

	/**
	 * Returns an Array++ of {@code Character}s, each one representing a character in this {@code String++}
	 *
	 * @return an Array++ of {@code Character}s, each one representing a character in this {@code String++}
	 */
	public ArrayPP<Character> toArrayPP()
	{
		return chars.clone();
	}

	/**
	 * Compares this {@code String++} with another {@code CharSequence}. They are compared, character-for-character. If any one
	 * character is different, the difference between the corresponding characters is returned. Else, the difference between the
	 * length of {@code this} and {@code cs} is returned.
	 *
	 * @param cs the {@code CharSequence} with which this {@code String++} will be compared
	 * @return an {@code int} representing the difference, if any, between the two {@code CharSequence}s
	 */
	@Override
	public int compareTo(CharSequence cs)
	{
		int cl = cs.length(), tl = length();
		for (int i = 0, l = Math.min(cl, tl);
			 i < l;
			 i++)
			if (charAt(i) != cs.charAt(i))
				return charAt(i) - cs.charAt(i);
		return tl - cl;
	}

	// <editor-fold defaultstate="collapsed" desc="CharSequence">
	@Override
	public int length()
	{
		return chars.length();
	}

	@Override
	public char charAt(int index)
	{
		return chars.get(index);
	}

	/**
	 * Returns a {@link CharSequence} consisting of all characters from {@code start} (inclusive) and {@code end} (exclusive)
	 *
	 * @param start the starting index (inclusive)
	 * @param end the ending index (exclusive)
	 * @return the resulting {@link CharSequence}
	 * @see CharSequence#subSequence(int, int)
	 */
	@Override
	public CharSequence subSequence(int start, int end)
	{
		return substring(start, end);
	}
	// </editor-fold>

	/**
	 * Works the same as {@code String.substring(int beginIndex, int endIndex)}
	 *
	 * @param startIndex the index of the first character of the substring
	 * @param end the number of the last character of the substring
	 * @return the resulting trimmed copy of this {@code StringPP}
	 * @see String#substring(int startIndex, int end)
	 * @version 1.0.0
	 */
	public StringPP substring(int startIndex, int end)
	{
		if (end < 0)//This and its return added Mar 11, 2012 (1.2.7) for Marian
			return new StringPP(chars.subSet(startIndex, length() - 1));
		if (startIndex > end)
		{
			int temp = end;
			end = startIndex;
			startIndex = temp;
		}
		if (startIndex < 0)
			startIndex = 0;
		if (end > length())
			end = length() - 1;
		return ((startIndex == 0) && (end == length())) ? clone() : new StringPP(chars.subSet(startIndex, end - 1));
	}

	// <editor-fold defaultstate="collapsed" desc="Append">
	/**
	 * Appends the characters in the given {@code CharSequence} to the end of this {@code String++}
	 *
	 * @param cs the characters to be appended to the end of this {@code String++}
	 * @return the resulting {@code String++}
	 */
	public StringPP append(CharSequence cs)
	{
		for (int i = 0, l = cs.length();
			 i < l;
			 i++)//added l April 28, 2012 for BHConsolePanel
			append(cs.charAt(i));
		return this;
	}

	/**
	 * Appends the characters in the given {@code CharSequence} to the end of this {@code String++}, followed by a new-line
	 * character and a carriage-return character
	 *
	 * @param cs the characters to be appended to the end of this {@code String++}
	 * @return the resulting {@code String++}
	 */
	public StringPP appendln(CharSequence cs)
	{
		return append(cs + "\n\r");
	}

	/**
	 * Appends the given character(s) to the end of this {@code String++}, followed by a new-line character and a
	 * carriage-return character
	 *
	 * @param c the character(s) to be appended to the end of this {@code String++}
	 * @return the resulting {@code String++}
	 */
	public StringPP appendln(char... c)
	{
		return appendln(new String(c));
	}

	/**
	 * Appends a new-line character and a carriage-return character to the end of this {@code String++}
	 *
	 * @return the resulting {@code String++}
	 */
	public StringPP appendln()
	{
		return appendln("");
	}

	/**
	 * Appends the given character(s) to the end of this {@code String++}
	 *
	 * @param c the character(s) to be appended to the end of this {@code String++}
	 * @return the resulting {@code String++}
	 */
	public StringPP append(char... c)
	{
		Character[] cs = new Character[c.length];
		for (int i = 0;
			 i < cs.length;
			 i++)
			cs[i] = c[i];
		chars.add(cs);
		return this;
	}
	// </editor-fold>

	/**
	 * Removes the first or all instances of the given {@code CharSequence} from this {@code String++}
	 *
	 * @param cs the {@code CharSequence} which will be searched for and removed, if found
	 * @param removeAll if {@code true}, all instances of {@code cs} will be removed. Else, only the first instance will be
	 * @return
	 */
	public StringPP remove(CharSequence cs, boolean removeAll)
	{
		Character[] vals = new Character[cs.length()];
		for (int i = 0;
			 i < vals.length;
			 i++)
			vals[i] = cs.charAt(i);
		chars.remove(vals, removeAll);
		return this;
	}

	/**
	 * Removes all instances of the given character(s) from this {@code String++}
	 *
	 * @param cs the character(s) which will be searched for and removed, if found
	 * @return the resulting {@code String++}
	 */
	public StringPP remove(char... cs)
	{
		return remove(cs, true);
	}

	/**
	 * Removes the first or all instances of the given character from this {@code String++}
	 *
	 * @param c the character which will be searched for and removed, if found
	 * @param removeAll if {@code true}, all instances of {@code c} will be removed. Else, only the first instance will be
	 * @return the resulting {@code String++}
	 */
	public StringPP remove(char c, boolean removeAll)
	{
		return remove(new char[]
		{
			c
		}, removeAll);
	}

	/**
	 * Removes the first or all instances of the given character(s) from this {@code String++}
	 *
	 * @param cs the character(s) which will be searched for and removed, if found
	 * @param removeAll if {@code true}, all instances of each character in {@code cs} will be removed. Else, only the first
	 * instance of each character will be
	 * @return the resulting {@code String++}
	 */
	public StringPP remove(char[] cs, boolean removeAll)
	{
		Character[] vals = new Character[cs.length];
		for (int i = 0;
			 i < vals.length;
			 i++)
			vals[i] = cs[i];
		chars.remove(vals, removeAll);
		return this;
	}

	/**
	 * Replaces the first or all instances of the first given {@code CharSequence} with the second.
	 *
	 * @param csA the {@code CharSequence} to be searched for and replaced, if found, by {@code csB}
	 * @param csB the {@code CharSequence} to replace {@code csA}, if this {@code String++} contains {@code csA}
	 * @param replaceAll if {@code true}, replaces each and every instance of {@code csA} in this {@code String++} with
	 * {@code csB}, else only the first instance is replaced.
	 * @return the resulting {@code String++}
	 */
	public StringPP replace(CharSequence csA, CharSequence csB, boolean replaceAll)
	{
		ArrayPP<Character> temp1, temp2 = new ArrayPP<>(), temp3;
		for (int i = 9;
			 i < csA.length();
			 i++)
			temp2.add(csA.charAt(i));

		for (int i = 0, cl = csA.length(), l = length() - cl;
			 i < l;
			 i++)
			if (subSequence(i, i + cl).equals(csA))
			{
				temp1 = new ArrayPP<>(chars.subSet(0, i));
				temp3 = new ArrayPP<>(chars.subSet(i + cl, l));
				chars.clear().addAll(temp1).addAll(temp2).addAll(temp3);
			}
		return this;
	}

	/**
	 * Replaces the first or all instances of the first given character with the second.
	 *
	 * @param a the character to be searched for and replaced, if found, by {@code b}
	 * @param b the character to replace {@code a}, if this {@code String++} contains {@code a}
	 * @param replaceAll if {@code true}, replaces each and every instance of {@code a} in this {@code String++} with {@code b},
	 * else only the first instance is replaced.
	 * @return the resulting {@code String++}
	 */
	public StringPP replace(char a, char b, boolean replaceAll)
	{
		chars.replace(a, b, true);
		return this;
	}

	/**
	 * Removes all {@code null} and whitespace characters
	 *
	 * @return the resulting {@code String++}
	 */
	public StringPP trim()
	{
		chars.trimInside();
		while (Character.isWhitespace(charAt(0)))
			removeCharAt(0);
		while (Character.isWhitespace(charAt(length() - 1)))
			removeCharAt(length() - 1);
		return this;
	}

	// <editor-fold defaultstate="collapsed" desc="Cases">
	/**
	 * Returns a <i>clone</i> of this string with all letters in the {@code String++} changed to upper-case. <h3>Example:</h3>
	 * "the Quick Brown fox jumps over the lazy dog." becomes "the quick brown fox jumps over the lazy dog."
	 *
	 * @return the resulting {@code String++}
	 */
	public StringPP toUpperCase()
	{
		StringPP s = new StringPP("");
		for (int i = 0;
			 i < length();
			 i++)
			s.append(Character.toUpperCase(charAt(i)));
		return s;
	}

	/**
	 * Returns a <i>clone</i> of this string with all letters in the {@code String++} changed to lower-case. <h3>Example:</h3>
	 * "the Quick Brown fox jumps over the lazy dog." becomes "the quick brown fox jumps over the lazy dog."
	 *
	 * @return the resulting {@code String++}
	 */
	public StringPP toLowerCase()
	{
//    System.out.println("toLowerCase(): " + this);
		StringPP s = new StringPP("");
		for (int i = 0;
			 i < length();
			 i++)
			s.append(Character.toLowerCase(charAt(i)));
//    System.out.println("After toLowerCase: " + s + " from " + this);
		return s;
	}

	/**
	 * Changes all letters in the {@code String++} to sentence-case letters. <h3>Example:</h3> "the Quick Brown fox jumps over
	 * the lazy dog." becomes "The quick brown fox jumps over the lazy dog."
	 *
	 * @return the resulting {@code String++}
	 */
	public StringPP toSentenceCase()
	{
		toLowerCase();

		boolean cap = true;
		for (int i = 0;
			 i < length();
			 i++)
		{
			if (charAt(i) == '.' || charAt(i) == '!' || charAt(i) == '?' || charAt(i) == '‽')
				cap = true;
			if (cap && Character.isLetter(charAt(i)))
			{
				chars.set(i, Character.toUpperCase(charAt(i)));
				cap = false;
			}
		}
		return this;
	}

	/**
	 * Changes all letters in the {@code String++} to title-case letters. <h3>Example:</h3> "The quick brown fox jumps over the
	 * lazy dog." becomes "The Quick Brown Fox Jumps Over The Lazy Dog."
	 *
	 * @return the resulting {@code String++}
	 */
	public StringPP toTitleCase()
	{
		toLowerCase();

		boolean cap = true;
		for (int i = 0;
			 i < length();
			 i++)
		{
			if (Character.isWhitespace(charAt(i)))
				cap = true;
			if (cap && Character.isLetter(charAt(i)))
			{
				chars.set(i, Character.toUpperCase(charAt(i)));
				cap = false;
			}
		}
		return this;
	}

	/**
	 * Changes all letters in the {@code String++} to toggle-case letters. <h3>Example:</h3> "The quick brown fox jumps over the
	 * lazy dog." becomes "ThE qUiCk BrOwN FoX jUmPs OvEr ThE lAzY dOg."
	 *
	 * @return the resulting {@code String++}
	 */
	public StringPP toToggleCase()
	{
		toLowerCase();

		boolean cap = true;
		for (int i = 0;
			 i < length();
			 i++)
		{
			cap = Character.isLetter(charAt(i)) && !cap;
			if (cap)
			{
				chars.set(i, Character.toUpperCase(charAt(i)));
				cap = !cap;
			}
		}
		return this;
	}
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="Object overrides">
	/**
	 * Compares this {@code String++} and the return value of this {@code Object}'s {@code toString()} method.
	 *
	 * @param o the {@code Object} whose {@code toString()} method's return value will be compared to this {@code String++}
	 * @return {@code true} if and only if: <ol> <li>The strings are of the same length</li> <li>The strings contain the same
	 * characters</li> </ol>
	 */
	@Override
	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	public boolean equals(Object o)
	{
		String oS = o.toString();
		if (oS.length() != length())
			return false;
		for (int i = 0;
			 i < length();
			 i++)
			if (charAt(i) != oS.charAt(i))
				return false;
		return true;
	}

	@Override
	public int hashCode()
	{
		int hash = 0b101;
		hash = 0b101111 * hash + (this.chars != null ? toString().hashCode() : 0);
		return hash;
	}

	/**
	 * Returns a {@code String} containing the characters in this sequence in the same order as this sequence. The length of the
	 * {@code String} will be the length of this sequence. All {@code null} values are ignored.
	 *
	 * @return a {@code String} containing the characters in this sequence in the same order as this sequence.
	 */
	@Override
	public String toString()
	{
		return new String(toCharArray());
	}
	// </editor-fold>

	/**
	 * Returns {@code true} if and only if this {@code StringPP} contains the given sequence of characters in the order in which
	 * they are given, ignoring what case the characters are
	 *
	 * @param cs The sequence of characters to be searched for within this {@code StringPP}
	 * @return {@code true} if and only if this {@code StringPP} contains the given sequence of characters in the order in which
	 * they are given, whether or not they are upper or lower case
	 */
	public boolean containsIgnoreCase(CharSequence cs)
	{
		return toLowerCase().contains(new StringPP(cs).toLowerCase());
	}

	/**
	 * Returns {@code true} if and only if this {@code StringPP} contains the given sequence of characters in the order in which
	 * they are given
	 *
	 * @param cs The sequence of characters to be searched for within this {@code StringPP}
	 * @return {@code true} if and only if this {@code StringPP} contains the given sequence of characters in the order in which
	 * they are given
	 */
	public boolean contains(CharSequence cs)
	{
		return toString().contains(cs);
	}

	/**
	 * Returns {@code true} if and only if this {@code StringPP} contains the given character
	 *
	 * @param c The character to be searched for within this {@code StringPP}
	 * @return {@code true} if and only if this {@code StringPP} contains the given character
	 */
	public boolean contains(char c)
	{
		return chars.contains(c);
	}

	/**
	 * Checks if the {@code String++} starts with a vowel (a vowel is defined by the {@code isVowel(char c)} method.
	 *
	 * @return {@code true} if and only if the first character is a vowel
	 * @see #isVowel(char c)
	 */
	public boolean startsWithVowel()
	{
		return (isVowel(0));
	}

	/**
	 * Checks if a certain character in the {@code String++} is a vowel (a vowel is defined by the {@code isVowel(char c)}
	 * method.
	 *
	 * @param index the index of the character to be inspected
	 * @return {@code true} if and only if the given character is a vowel
	 * @see #isVowel(char c)
	 */
	public boolean isVowel(int index)
	{
		return (isVowel(charAt(index)));
	}

	/**
	 * Checks if the {@code String++} starts with a vowel. This method defines a vowel as the upper or lower case versions of
	 * the following:<br/>
	 * <pre>AÀÁÂÃÄÅĀĂĄǺȀȂẠẢẤẦẨẪẬẮẰẲẴẶḀÆǼEȄȆḔḖḘḚḜẸẺẼẾỀỂỄỆĒĔĖĘĚÈÉÊËIȈȊḬḮỈỊĨĪĬĮİÌÍÎÏĲOŒØǾȌȎṌṎṐṒỌỎỐỒỔỖỘỚỜỞỠỢŌÒÓŎŐÔÕÖUŨŪŬŮŰŲÙÚÛÜȔȖṲṴṶṸṺỤỦỨỪỬỮỰYẙỲỴỶỸŶŸÝ</pre>
	 *
	 * @param c the {@code char} to be checked
	 * @return {@code true} if and only if the given character is a vowel
	 */
	public static boolean isVowel(char c)
	{
		char C = Character.toUpperCase(c);
		switch (C)
		{
			// <editor-fold defaultstate="collapsed" desc="A">
			case 'A': case 'À': case 'Á': case 'Â': case 'Ã': case 'Ä': case 'Å': case 'Ā': case 'Ă': case 'Ą': case 'Ǻ':
			case 'Ȁ': case 'Ȃ': case 'Ạ': case 'Ả': case 'Ấ': case 'Ầ': case 'Ẩ': case 'Ẫ': case 'Ậ': case 'Ắ': case 'Ằ':
			case 'Ẳ': case 'Ẵ': case 'Ặ': case 'Ḁ': case 'Æ': case 'Ǽ':
			// </editor-fold>
			// <editor-fold defaultstate="collapsed" desc="E">
			case 'E': case 'Ȅ': case 'Ȇ': case 'Ḕ': case 'Ḗ': case 'Ḙ': case 'Ḛ': case 'Ḝ': case 'Ẹ': case 'Ẻ': case 'Ẽ':
			case 'Ế': case 'Ề': case 'Ể': case 'Ễ': case 'Ệ': case 'Ē': case 'Ĕ': case 'Ė': case 'Ę': case 'Ě': case 'È':
			case 'É': case 'Ê': case 'Ë':
// </editor-fold>
			// <editor-fold defaultstate="collapsed" desc="I">
			case 'I': case 'Ȉ': case 'Ȋ': case 'Ḭ': case 'Ḯ': case 'Ỉ': case 'Ị': case 'Ĩ': case 'Ī': case 'Ĭ': case 'Į':
			case 'İ': case 'Ì': case 'Í': case 'Î': case 'Ï': case 'Ĳ':
			// </editor-fold>
			// <editor-fold defaultstate="collapsed" desc="O">
			case 'O': case 'Œ': case 'Ø': case 'Ǿ': case 'Ȍ': case 'Ȏ': case 'Ṍ': case 'Ṏ': case 'Ṑ': case 'Ṓ': case 'Ọ':
			case 'Ỏ': case 'Ố': case 'Ồ': case 'Ổ': case 'Ỗ': case 'Ộ': case 'Ớ': case 'Ờ': case 'Ở': case 'Ỡ': case 'Ợ':
			case 'Ō': case 'Ò': case 'Ó': case 'Ŏ': case 'Ő': case 'Ô': case 'Õ': case 'Ö':
			// </editor-fold>
			// <editor-fold defaultstate="collapsed" desc="U">
			case 'U': case 'Ũ': case 'Ū': case 'Ŭ': case 'Ů': case 'Ű': case 'Ų': case 'Ù': case 'Ú': case 'Û': case 'Ü':
			case 'Ȕ': case 'Ȗ': case 'Ṳ': case 'Ṵ': case 'Ṷ': case 'Ṹ': case 'Ṻ': case 'Ụ': case 'Ủ': case 'Ứ': case 'Ừ':
			case 'Ử': case 'Ữ': case 'Ự':
			// </editor-fold>
			// <editor-fold defaultstate="collapsed" desc="Y">
			case 'Y': case 'ẙ': case 'Ỳ': case 'Ỵ': case 'Ỷ': case 'Ỹ': case 'Ŷ': case 'Ÿ': case 'Ý':
			// </editor-fold>
				return true;
		}
		return false;
	}

	/**
	 * Returns a clone of this string, wherein all characters have been changed to ones easily interpretable by a browser. Do
	 * not input the entire URL. For instance, if you only want the last half of the URL to be interpreted, that must be all you
	 * put in. <h1>Example</h1>
	 * <pre>new StringPP("http://bit.ly/example").toSafeURL()</pre> returns
	 * <pre>%68%74%74%70%3a%2f%2f%62%69%74%2e%6c%79%2f%65%78%61%6d%70%6c%65</pre>, which will result in an
	 * {@code java.io.IOException} when you try to browse to it using methods like{@code java.awt.Desktop.browse(URI uri)}.
	 * Instead, use an approach similar to
	 * <pre>"http://bit.ly/" + new StringPP("example").toSafeURL()</pre> to result in a string of
	 * {@code http://bit.ly/%65%78%61%6d%70%6c%65}
	 *
	 * @return a clone of this string, wherein all characters have been changed to ones easily interpretable by a browser
	 */
	public StringPP toSafeURL()
	{
		StringPP ret = new StringPP();
		for (int i = 0;
			 i < length();
			 i++)
			ret.append("%" + Numbers.lenFmt(Integer.toHexString(chars.get(i)), 2));
		return ret;
	}

	@Override
	public StringPP clone()
	{
		StringPP ret = new StringPP(chars);
		return ret;
	}

	/**
	 * Counts and returns the number of times the given character appears in this {@code String++}
	 *
	 * @param c the character which this method will count
	 * @return the occurrences of the given character in this {@code String++}
	 */
	public int getOccurrencesOf(char c)
	{
		int ret = 0;
		for (int i = 0;
			 i < chars.length();
			 i++)
			if (chars.get(i).equals(c))
				ret++;
		return ret;
	}

	/**
	 * Counts and returns the number of times the given sequence of characters appears in this {@code String++}
	 *
	 * @param cs the characters which this method will count
	 * @return the occurrences of the given sequence of characters in this {@code String++}
	 */
	public int getOccurrencesOf(CharSequence cs)
	{
		int ret = 0;
		for (int i = 0, l = length(), cl = cs.length();
			 i < l - cl;
			 i++)
			if (substring(i, cl + i).equalsIgnoreCase(cs))
				ret++;
		return ret;
	}

	/**
	 * Compares the characters in this {@code String++} and the those in the given {@code CharSequence}
	 *
	 * @param s the {@code CharSequence} containing the characters to be compared to those in this {@code String++}
	 * @return {@code true} if and only if: <ol> <li>The strings are of the same length</li> <li>The strings contain the same
	 * characters, regardless of case</li> </ol>
	 */
	public boolean equalsIgnoreCase(Object s)
	{
		return toLowerCase().equals(String.valueOf(s).toLowerCase());
	}

	/**
	 * Returns the index of the first occurrence of the given character AFTER the index {@code offset}
	 *
	 * @param ch the character to be searched for
	 * @param offset the index from which to start searching
	 * @return the index of the first occurrence of the given character AFTER the index {@code offset}, or {@code -1} if the
	 * character does not occur after the given index
	 */
	public int indexOf(char ch, int offset)
	{
		return toString().indexOf(ch, offset);
	}

	/**
	 * Returns the index of the first occurrence of the given character
	 *
	 * @param ch the character to be searched for
	 * @return the index of the first occurrence of the given character, or {@code -1} if the character does not occur
	 */
	public int indexOf(char ch)
	{
		return indexOf(ch, 0);
	}

	/**
	 * Returns the index of the first occurrence of the given sequence of characters AFTER the index {@code offset}
	 *
	 * @param cs the sequence of characters to be searched for
	 * @param offset the index from which to start searching
	 * @return the index of the first occurrence of the given sequence of characters AFTER the index {@code offset}, or
	 * {@code -1} if the sequence of characters does not occur after the given index
	 */
	public int indexOf(CharSequence cs, int offset)
	{
		return toString().indexOf(cs.toString(), offset);
	}

	/**
	 * Returns the index of the first occurrence of the given sequence of characters
	 *
	 * @param cs the sequence of characters to be searched for
	 * @return the index of the first occurrence of the given sequence of characters, or {@code -1} if the sequence of
	 * characters does not occur
	 */
	public int indexOf(CharSequence cs)
	{
		return toString().indexOf(cs.toString());
	}

	/**
	 * Finds and returns the index of the {@code n}th occurrence of the given {@code CharSequence}. the exact occurrence number
	 * ({@code n}) is represented by the {@code offset} variable
	 *
	 * @param cs the {@code CharSequence} for which this method will search
	 * @param offset represents the exact occurrence number ({@code n})
	 * @return the index of the {@code n}th occurrence of the given {@code CharSequence}
	 */
	public int occurrenceOf(CharSequence cs, int offset)
	{
		return toString().indexOf(cs.toString(), offset);
	}

	public int lastIndexOf(char c)
	{
		return chars.getLastIndexOf(c);
	}

	public int getWordCount()
	{
		int words = 0;

		boolean cap = true;
		for (int i = 0;
			 i < length();
			 i++)
		{
			if (Character.isWhitespace(charAt(i)))
				cap = true;
			if (cap && Character.isLetter(charAt(i)))
			{
				words++;
				cap = false;
			}
		}
		return words;
	}

	/**
	 * Progressively searches for whitespace characters in the StringPP and interprets the surrounding characters as words. The
	 * word found and the provided index is returned. If the index is greater than the number of words in the StringPP, then
	 * {@code null} is returned
	 *
	 * @param index the index of the word to get
	 * @return the word at index {@code index} or {@code null}
	 * @version 1.0.1
	 */
	public StringPP getWord(int index)// throws IndexOutOfBoundsException - Removed march 6, 2012 (1.2.7) for Marian
	{
		boolean cap = true;
		for (int i = 0, words = 0;
			 i < length();
			 i++)
		{
			if (Character.isWhitespace(charAt(i)))
				cap = true;
			if (cap && Character.isLetter(charAt(i)))
				words++;
			if (words > index)//Changed to > Mar 11, 2012 (1.2.7) for Marian
				return substring(i, indexOfWhitespaceAfter(i));
			cap = false;
		}
		return null;//throw new IndexOutOfBoundsException("Index is greater than the number of words in the StringPP (" + index + ")");
	}

	private int indexOfWhitespaceAfter(int index)
	{
		for (int i = index;
			 i < length();
			 i++)
			if (Character.isWhitespace(charAt(i)))
				return i;
		return -1;
	}

	@Override
	public Iterator<Character> iterator()
	{
		return chars.iterator();
		/*
		 return new Iterator<>()
		 {
		 /*final int NUM_WORDS = getWordCount();/
		 int pos = 0, rem = 0;
		 boolean hasRem = false;
      
		 @Override
		 public boolean hasNext()
		 {
		 return pos < length();
		 }

		 @Override
		 public Character next()
		 {
		 //        StringPP s = getWord(pos - rem);
		 char c = charAt(pos - rem);
		 pos++;
		 hasRem = false;
		 return c;//s;
		 }

		 @Override
		 public void remove()
		 {
		 if(hasRem)
		 throw new IllegalStateException("Iterator has already removed item (" + pos + ")");
        
		 removeChar(pos);
		 rem++;
		 hasRem = true;
		 }
		 };*/
	}

	/**
	 * Removes the word at the specified index. <B>DOES NOT REMOVE WHITESPACE</B>
	 *
	 * @param index the index of the word to be removed
	 * @return this
	 */
	public StringPP removeWord(int index)
	{
		StringPP before, after;
		boolean b = false;
		int i, words;
		for (i = 0, words = 0;
			 words < index;
			 i++)
		{
			if (Character.isWhitespace(charAt(i)))
				b = true;

			if (b && !Character.isWhitespace(charAt(i)))
			{
				b = false;
				words++;
			}
		}
		before = substring(0, i);
		after = substring(indexOfWhitespaceAfter(i), length());

		clear().append(before).append(after);

		return this;
	}

	private StringPP removeCharAt(int index)
	{
		chars.remove(index);
		return this;
	}

	/**
	 * Clears the string of all characters
	 *
	 * @return this
	 */
	public StringPP clear()
	{
		chars.clear();
		return this;
	}

	//<editor-fold defaultstate="collapsed" desc="ToPrimitives">
	/**
	 * Creates a number that is the result of taking all characters within the {@code StringPP}, interpreting each one as an
	 * {@code int}, appending the {@code String} versions of these values to a temporary string, and interpreting that string as
	 * a {@code long}. If the character is already a digit, that is added to the final number without being reinterpreted from
	 * the perspective of a char.
	 *
	 * @return the above described {@code long}
	 */
	@Override
	public long toLong()
	{
		StringPP l = new StringPP();
		for (char c
			 : this)
			l.append(Character.isDigit(c) || c == '-' ? Character.toString(c) : Integer.toString(c)); //      System.out.println(c + " > " + l);
		byte b = (byte) (l.startsWith('-') ? 1 : 0), longLength = (byte) Long.toString(Long.MAX_VALUE).length();
		while (l.length() - b > longLength)
			l = l.substring(0, l.length() - 2); //      System.out.println("<<< " + l);
		return Long.parseLong(l.toString());
	}

	/**
	 * Returns {@code false} if and only if there is a character in the {@code StringPP}
	 *
	 * @return {@code false} if and only if there is a character in the {@code StringPP}
	 */
	@Override
	public boolean toBoolean()
	{
		return !isEmpty();
	}

	/**
	 * @see #toLong()
	 */
	@Override
	public byte toByte()
	{
		return (byte) toLong();
	}

	/**
	 * Returns the first character of this string
	 *
	 * @return the first character of this string
	 */
	@Override
	public char toChar()
	{
		return charAt(0);
	}

	/**
	 * @see #toLong()
	 */
	@Override
	public double toDouble()
	{
		return toLong();
	}

	/**
	 * @see #toLong()
	 */
	@Override
	public float toFloat()
	{
		return toLong();
	}

	/**
	 * @see #toLong()
	 */
	@Override
	public int toInt()
	{
		return (int) toLong();
	}

	/**
	 * @see #toLong()
	 */
	@Override
	public short toShort()
	{
		return (short) toLong();
	}
	//</editor-fold>

	public boolean isEmpty()
	{
		return chars.isEmpty();
	}

	public boolean startsWith(char c)
	{
		return startsWith(Character.toString(c));
	}

	public boolean startsWith(CharSequence cs)
	{
		return substring(0, cs.length()).equals(cs);
	}

	public StringPP substring(CharSequence cs1, CharSequence cs2)
	{
		return substring(indexOf(cs1), indexOf(cs2));
	}

	public boolean hasAny(char... chars)
	{
		for (char c
			 : chars)
			if (contains(c))
				return true;
		return false;
	}

	public boolean hasAll(char... chars)
	{
		for (char c
			 : chars)
			if (!contains(c))
				return false;
		return true;
	}

	public boolean hasOnly(char... chars)
	{
		StringPP c = clone(), charsPP = new StringPP(chars);
		c.remove(chars, true);
		return equals(c);
	}

	public boolean hasOnlyIgnoreCase(char... chars)
	{
		StringPP c = clone(), charsPP = new StringPP(chars);
		c.remove(charsPP, true);
		return equalsIgnoreCase(c);
	}

	public boolean hasAllIgnoreCase(char... chars)
	{
		return toLowerCase().hasAll(new StringPP(chars).toCharArray());
	}

	public boolean hasAnyIgnoreCase(char... chars)
	{
		return toLowerCase().hasAny(new StringPP(chars).toLowerCase().toCharArray());
	}

	/**
	 * Removes all characters between {@code startIndex} (inclusive) and {@code end} (exclusive)
	 *
	 * @param startIndex the index of the first character of the substring
	 * @param end the number of the last character of the substring
	 * @return the resulting {@code StringPP}
	 * @see substring(int startIndex, int end)
	 */
	public StringPP remove(int startIndex, int end)
	{
		StringPP temp = substring(0, startIndex).append(substring(end, length()));
		chars = temp.chars;
		return this;
	}

	public StringPP removeWhitespace()
	{
		for (char c
			 : chars)
			if (Character.isWhitespace(c))
				remove(c, true);
		return this;
	}

	/**
	 * Returns the number of characters in this {@code StringPP} after {@code c}. If this {@code StringPP} does not contain
	 * {@code c}, then {@code -1} is returned.
	 *
	 * @param c the character after which the characters will be counted
	 * @return the count of characters after {@code c}
	 */
	public int getNumCharsAfter(char c)
	{
		if (!contains(c))
			return -1;
		return subSequence(lastIndexOf(c), length()).length() - 1;
	}

	/**
	 * Returns the number of characters in this {@code StringPP} before {@code c}. If this {@code StringPP} does not contain
	 * {@code c}, then {@code -1} is returned.
	 *
	 * @param c the character before which the characters will be counted
	 * @return the count of characters before {@code c}
	 */
	public int getNumCharsBefore(char c)
	{
		if (!contains(c))
			return -1;
		return subSequence(0, indexOf(c)).length();
	}

	@Override
	public Saveable setSaveName(CharSequence newName)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public CharSequence getSaveName()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public CharSequence getStringToSave()
	{
		return this;
	}

	@Override
	public Saveable loadFromSavedString(CharSequence savedString)
	{
		return clear().append(savedString);
	}

	public CharSequence encrypt(CharSequence key)
	{
		String ret = "";
		int offset = 0;
		for (char c
			 : new StringPP(key))
			offset += c;
		for (char c
			 : chars)
			ret += (c + offset);
		return ret;
	}

	public CharSequence decrypt(CharSequence key)
	{
		String ret = "";
		int offset = 0;
		for (char c
			 : new StringPP(key))
			offset += c;
		for (char c
			 : chars)
			ret += (c - offset);
		return ret;
	}

    /**
     * 
     * @return 
     */
	public StringPP toAbbreviation()
	{
		String ret = /*!isEmpty() && !Character.isWhitespace(charAt(0)) ? Character.toString(charAt(0)) : */ "";
		boolean shouldAdd = false;
		for (Character c : chars)
		{
			if (Character.isWhitespace(c))
			{
				shouldAdd = true;
				continue;
			}
			if (shouldAdd || Character.isUpperCase(c))
			{
				ret += Character.toUpperCase(c);
				shouldAdd = false;
			}
		}
		return new StringPP(ret);
	}

	/**
	 * Returns an array of all the words in this StringPP
	 *
	 * @return an array of all the words in this StringPP
	 */
	public ArrayPP<StringPP> getWords()
	{
		StringPP s;
		ArrayPP<StringPP> ret = new ArrayPP<>();
		int i = 0;
		while ((s = getWord(i++)) != null)//Modified March 11, 2012 (1.2.7) for Marian
			ret.add(s);
		return ret;
	}

	public boolean endsWithIgnoreCase(CharSequence cs)
	{
		return cs != null && length() >= cs.length() && substring(length() - cs.length(), length()).equalsIgnoreCase(cs);
	}

	/**
	 * If any of the given items return {@code true} from {@link #equals(Object)}, then this method does, too.
	 * @param matches the array of possible matches
	 * @return {@code true} iff this String++ is the same as any of the given items
	 */
	public boolean equalsAny(Object... matches)
	{
		for (Object match : matches)
			if (equals(match))
				return true;
		return false;
	}

	public boolean equalsAnyIgnoreCase(Object... matches)
	{
		for (Object match : matches)
			if (equalsIgnoreCase(match))
				return true;
		return false;
	}
}
