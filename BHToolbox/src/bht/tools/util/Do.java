package bht.tools.util;

import java.io.PrintStream;

/**
 * Do, made for J. Jonah JSON, is copyright Blue Husky Programming ©2014 GPLv3<HR/>
 * 
 * A class full of tiny-named static methods that act as generalizers or shorten the names of common practices
 * 
 * @author Kyli of Blue Husky Programming
 * @since 2014-08-18
 * @version 1.3.0
 *		- 2015-02-05 (1.4.0) - Kyli Rouge added {@link #p(Object...)}
 *		- 2014-11-29 (1.3.0) - Kyli Rouge added {@link #S(Object...)} and {@link #S2(Object...)}
 *		- 2014-11-29 (1.2.0) - Kyli Rouge added {@link #a(Object...)} and {@link #A(Object...)}
 *		- 2014-08-20 (1.1.0) - Kyli Rouge moved Do into BHToolbox
 *		- 2014-08-18 (1.0.0) - Kyli Rouge created Do
 */
public class Do
{
	/**
	 * Made because of methods like {@link Long#equals(java.lang.Object)} returning {@code false} for {@code 12L == 12D}. Works
	 * best along with a static import.
	 * <br/>
	 * <br/>
	 * <ul>
	 *	<li>if {@code a == b}, returns true</li>
	 *	<li>if {@code a == null || b == null}, returns false, since null is only ever equal to itself</li>
	 *	<li>if both {@code a} and {@code b} are {@link Number}s, their {@link Number#doubleValue()} returns are compared</li>
	 *	<li>if both {@code a} and {@code b} are {@link Boolean}s, their {@link Boolean#booleanValue()} returns are compared</li>
	 *	<li>if all else fails, {@code a.equals(b)} is returned</li>
	 * </ul>
	 * 
	 * @param a the first object
	 * @param b the second object
	 * @return whether or not they're equal
	 */
	@SuppressWarnings("UnnecessaryUnboxing")
	public static boolean eq(Object a, Object b)
	{
		if (a == b)
			return true;
		if (a == null || b == null)
			return false;
		
		if (a instanceof Number && b instanceof Number)
			return ((Number)a).doubleValue() == ((Number)b).doubleValue();
		
		if (a instanceof Boolean && b instanceof Boolean)
			return ((Boolean)a).booleanValue() == ((Boolean)b).booleanValue();
		
		return a.equals(b);
	}
	
	/**
	 * A way to safely convert the given object to a string with a tiny method name. Works best along with a static import.
	 * 
	 *	<UL>
	 *		<LI>If the value is an array, all elements are recursively stringified, separated by commas ({@code ,}), and
	 *			surrounded by square brackets ({@code []})</LI>
	 *		<LI>Else, the value is passed through {@link String#valueOf(Object)}</LI>
	 *	</UL>
	 * @param o the object to be stringified
	 * @return the {@code String} version of the given object
	 */
	public static String s(Object o)
	{
		if (o instanceof Object[])
		{
			StringBuilder sb = new StringBuilder("[");
			for(int i = 0, l = ((Object[])o).length; i < l; i++)
			{
				sb.append(s(((Object[])o)[i]));
				if (i != l - 1)
					sb.append(',');
			}
			return sb.append(']').toString();
		}
		return String.valueOf(o);
	}
	
	/**
	 * A way to safely convert the given object to a string with a tiny method name, while putting character sequences in
	 * quotes. Works best along with a static import.
	 * 
	 *	<UL>
	 *		<LI>If the value is a CharSequence, the value is surrounded by quotes ({@code ""})</LI>
	 *		<LI>Else, the value is passed through {@link #s(Object)}</LI>
	 *	</UL>
	 * @param o the object to be stringified
	 * @return the {@code String} version of the given object
	 */
	public static String s2(Object o)
	{
		if (o instanceof CharSequence)
			return "\"" + o + '\"';
		return s(o);
	}
	
	/**
	 * A way to safely convert the given object to a string++ with a tiny method name. Works best along with a static import.
	 * 
	 *	<UL>
	 *		<LI>If the value is an array, all elements are recursively stringified, separated by commas ({@code ,}), and
	 *			surrounded by square brackets ({@code []})</LI>
	 *		<LI>Else, the value is passed through {@link StringPP#valueOf(Object)}</LI>
	 *	</UL>
	 * @param o the object to be stringified
	 * @return the {@code StringPP} version of the given object
	 */
	public static StringPP S(Object o)
	{
		if (o instanceof Object[])
		{
			StringBuilder sb = new StringBuilder("[");
			for(int i = 0, l = ((Object[])o).length; i < l; i++)
			{
				sb.append(s(((Object[])o)[i]));
				if (i != l - 1)
					sb.append(',');
			}
			return new StringPP(sb.append(']'));
		}
		return StringPP.valueOf(o);
	}
	
	/**
	 * A way to safely convert the given object to a string++ with a tiny method name, while putting character sequences in
	 * quotes. Works best along with a static import.
	 * 
	 *	<UL>
	 *		<LI>If the value is a CharSequence, the value is surrounded by quotes ({@code ""})</LI>
	 *		<LI>Else, the value is passed through {@link #S(Object)}</LI>
	 *	</UL>
	 * @param o the object to be stringified
	 * @return the {@code StringPP} version of the given object
	 */
	public static StringPP S2(Object o)
	{
		if (o instanceof CharSequence)
			return S("\"" + o + '\"');
		return S(o);
	}
	
	/**
	 * Creates an array out of the given parameter(s)
	 * @param <T> the type of object to make an array out of
	 * @param t the objects to return in an array
	 * @return an array of the given objects
	 */
	public static <T> T[] a(T... t)
	{
		return t;
	}
	
	/**
	 * Creates an array++ out of the given parameter(s)
	 * @param <T> the type of object to make an array++ out of
	 * @param t the objects to return in an array++
	 * @return an array++ of the given objects
	 */
	public static <T> ArrayPP<T> A(T... t)
	{
		return new ArrayPP<>(t);
	}
	
	/**
	 * Prints the given object(s) to the standard output stream, {@link System#out}. If given nothing, a blank line is printed.
	 * If given one or more objects, each is printed on its own line using {@code System.out.println} after passing each object
	 * through {@link #s(Object)}.
	 * 
	 * @param o the object(s) to print.
	 * 
	 * @see System#out
	 * @see PrintStream#println()
	 * @see PrintStream#println(Object)
	 * @see #s
	 * 
	 * @since 1.4.0
	 * @author Kyli Rouge
	 * @version 1.0.0
	 */
	@SuppressWarnings("UseOfSystemOutOrSystemErr")
	public static void p(Object... o)
	{
		if (o == null || o.length == 0)
			System.out.println();
		else for (Object object : o)
			System.out.println(s(object));
	}
}
