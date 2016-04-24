package bht.tools.util;

import java.io.PrintStream;



/**
 * Do, made for J. Jonah JSON, is copyright Blue Husky Programming ©2014 BH-1-PS<HR/>
 *
 * A class full of tiny-named static methods that act as generalizers or shorten the names of common practices
 *
 * @author Kyli of Blue Husky Programming
 * @since 2014-08-18
 * @version 1.5.0
 * <pre>
 *		- 2015-03-03 (1.6.0) for Too Many Fonts
 *			+ Kyli added {@link #r64(Number)}
 *			+ Kyli added {@link #r(Number)}
 *			+ Kyli added {@link #r32(Number)}
 *			! Kyli expanded scope of conversion methods to all objects
 *		- 2015-02-12 (1.5.0) for BH Font Manager
 *			+ Kyli added {@link #i32(Number)}
 *			+ Kyli added {@link #i(Number)}
 *			+ Kyli added {@link #i64(Number)}
 *			+ Kyli added {@link #l(Number)}
 *			+ Kyli added {@link #i16(Number)}
 *			+ Kyli added {@link #i8(Number)}
 *			+ Kyli added {@link #f64(Number)}
 *			+ Kyli added {@link #d(Number)}
 *			+ Kyli added {@link #f32(Number)}
 *			+ Kyli added {@link #f(Number)}
 *		- 2015-02-05 (1.4.0) - Kyli Rouge added {@link #p(Object...)}
 *		- 2014-11-29 (1.3.0) - Kyli Rouge added {@link #S(Object...)} and {@link #S2(Object...)}
 *		- 2014-11-29 (1.2.0) - Kyli Rouge added {@link #a(Object...)} and {@link #A(Object...)}
 *		- 2014-08-20 (1.1.0) - Kyli Rouge moved Do into BHToolbox
 *		- 2014-08-18 (1.0.0) - Kyli Rouge created Do
 * </pre>
 */
public class Do {
	/**
	 * Made because of methods like {@link Long#equals(java.lang.Object)} returning {@code false} for {@code 12L == 12D}. Works
	 * best along with a static import.
	 * <br/>
	 * <br/>
	 * <ul>
	 * <li>if {@code a == b}, returns true</li>
	 * <li>if {@code a == null || b == null}, returns false, since null is only ever equal to itself</li>
	 * <li>if both {@code a} and {@code b} are {@link Number}s, their {@link Number#doubleValue()} returns are compared</li>
	 * <li>if both {@code a} and {@code b} are {@link Boolean}s, their {@link Boolean#booleanValue()} returns are compared</li>
	 * <li>if all else fails, {@code a.equals(b)} is returned</li>
	 * </ul>
	 *
	 * @param a the first object
	 * @param b the second object
	 *
	 * @return whether or not they're equal
	 */
	@SuppressWarnings("UnnecessaryUnboxing")
	public static boolean eq(Object a, Object b) {
		if (a == b) {
			return true;
		}
		if (a == null || b == null) {
			return false;
		}

		if (a instanceof Number && b instanceof Number) {
			return ((Number) a).doubleValue() == ((Number) b).doubleValue();
		}

		if (a instanceof Boolean && b instanceof Boolean) {
			return ((Boolean) a).booleanValue() == ((Boolean) b).booleanValue();
		}

		return a.equals(b);
	}

	/**
	 * A way to safely convert the given object to a string with a tiny method name. Works best along with a static import.
	 *
	 * <UL>
	 * <LI>If the value is an array, all elements are recursively stringified, separated by commas ({@code ,}), and
	 * surrounded by square brackets ({@code []})</LI>
	 * <LI>Else, the value is passed through {@link String#valueOf(Object)}</LI>
	 * </UL>
	 *
	 * @param o the object to be stringified
	 *
	 * @return the {@code String} version of the given object
	 */
	public static String s(Object o) {
		if (o instanceof Object[]) {
			StringBuilder sb = new StringBuilder("[");
			for (int i = 0, l = ((Object[]) o).length; i < l; i++) {
				sb.append(s(((Object[]) o)[i]));
				if (i != l - 1) {
					sb.append(',');
				}
			}
			return sb.append(']').toString();
		}
		return String.valueOf(o);
	}

	/**
	 * A way to safely convert the given object to a string with a tiny method name, while putting character sequences in
	 * quotes. Works best along with a static import.
	 *
	 * <UL>
	 * <LI>If the value is a CharSequence, the value is surrounded by quotes ({@code ""})</LI>
	 * <LI>Else, the value is passed through {@link #s(Object)}</LI>
	 * </UL>
	 *
	 * @param o the object to be stringified
	 *
	 * @return the {@code String} version of the given object
	 */
	public static String s2(Object o) {
		if (o instanceof CharSequence) {
			return "\"" + o + '\"';
		}
		return s(o);
	}

	/**
	 * A way to safely convert the given object to a string++ with a tiny method name. Works best along with a static import.
	 *
	 * <UL>
	 * <LI>If the value is an array, all elements are recursively stringified, separated by commas ({@code ,}), and
	 * surrounded by square brackets ({@code []})</LI>
	 * <LI>Else, the value is passed through {@link StringPP#valueOf(Object)}</LI>
	 * </UL>
	 *
	 * @param o the object to be stringified
	 *
	 * @return the {@code StringPP} version of the given object
	 */
	public static StringPP S(Object o) {
		if (o instanceof Object[]) {
			StringBuilder sb = new StringBuilder("[");
			for (int i = 0, l = ((Object[]) o).length; i < l; i++) {
				sb.append(s(((Object[]) o)[i]));
				if (i != l - 1) {
					sb.append(',');
				}
			}
			return new StringPP(sb.append(']'));
		}
		return StringPP.valueOf(o);
	}

	/**
	 * A way to safely convert the given object to a string++ with a tiny method name, while putting character sequences in
	 * quotes. Works best along with a static import.
	 *
	 * <UL>
	 * <LI>If the value is a CharSequence, the value is surrounded by quotes ({@code ""})</LI>
	 * <LI>Else, the value is passed through {@link #S(Object)}</LI>
	 * </UL>
	 *
	 * @param o the object to be stringified
	 *
	 * @return the {@code StringPP} version of the given object
	 */
	public static StringPP S2(Object o) {
		if (o instanceof CharSequence) {
			return S("\"" + o + '\"');
		}
		return S(o);
	}

	/**
	 * Creates an array out of the given parameter(s)
	 *
	 * @param <T> the type of object to make an array out of
	 * @param t   the objects to return in an array
	 *
	 * @return an array of the given objects
	 */
	public static <T> T[] a(T... t) {
		return t;
	}

	/**
	 * Creates an array++ out of the given parameter(s)
	 *
	 * @param <T> the type of object to make an array++ out of
	 * @param t   the objects to return in an array++
	 *
	 * @return an array++ of the given objects
	 */
	public static <T> ArrayPP<T> A(T... t) {
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
	public static void p(Object... o) {
		if (o == null || o.length == 0) {
			System.out.println();
		}
		else {
			for (Object object : o) {
				System.out.println(s(object));
			}
		}
	}

	//<editor-fold defaultstate="collapsed" desc="number conversion">
	/**
	 * Converts the given number to a 32-bit integer
	 *
	 * @param n the object to convert
	 *
	 * @return {@code n} as a 32-bit integer
	 *
	 * @author Kyli Rouge
	 * @since 1.5.0
	 * @version 1.1.0
	 * - 1.1.0 (2015-02-17; 1.6.0) - Kyli expanded scope to all objects
	 */
	public static int i32(Object n) {
		if (n instanceof Integer) {
			return (Integer) n;
		}
		if (n instanceof Number) {
			return r32(((Number) n).floatValue());
		}
		return Integer.parseInt(s(n));
	}

	/**
	 * Shorthand for {@link #i32(Number)}. Converts the given number to a 32-bit integer
	 *
	 * @param n the object to convert
	 *
	 * @return {@code n} as a 32-bit integer
	 *
	 * @author Kyli Rouge
	 * @since 1.5.0
	 * @version 1.1.0
	 * - 1.1.0 (2015-02-17; 1.6.0) - Kyli expanded scope to all objects
	 */
	public static int i(Object n) {
		return i32(n);
	}

	/**
	 * Converts the given number to a 64-bit integer
	 *
	 * @param n the object to convert
	 *
	 * @return {@code n} as a 64-bit integer
	 *
	 * @author Kyli Rouge
	 * @since 1.5.0
	 * @version 1.1.0
	 * - 1.1.0 (2015-02-17; 1.6.0) - Kyli expanded scope to all objects
	 */
	public static long i64(Object n) {
		if (n instanceof Long) {
			return (Long) n;
		}
		if (n instanceof Number) {
			return r64(((Number) n).doubleValue());
		}
		return Long.parseLong(s(n));
	}

	/**
	 * Shorthand for {@link #i64(Number)}. Converts the given number to a 64-bit integer
	 *
	 * @param n the object to convert
	 *
	 * @return {@code n} as a 64-bit integer
	 *
	 * @author Kyli Rouge
	 * @since 1.5.0
	 * @version 1.1.0
	 * - 1.1.0 (2015-02-17; 1.6.0) - Kyli expanded scope to all objects
	 */
	public static long l(Object n) {
		return i64(n);
	}

	/**
	 * Converts the given number to a 16-bit integer
	 *
	 * @param n the number to convert
	 *
	 * @return {@code n} as a 16-bit integer
	 *
	 * @author Kyli Rouge
	 * @since 1.5.0
	 * @version 1.1.0
	 * - 1.1.0 (2015-02-17; 1.6.0) - Kyli expanded scope to all objects
	 */
	public static short i16(Object n) {
		if (n instanceof Short) {
			return (Short) n;
		}
		if (n instanceof Number) {
			return ((Number) n).shortValue();
		}
		return Short.parseShort(s(n));
	}

	/**
	 * Converts the given number to an 8-bit integer
	 *
	 * @param n the number to convert
	 *
	 * @return {@code n} as an 8-bit integer
	 *
	 * @author Kyli Rouge
	 * @since 1.5.0
	 * @version 1.1.0
	 * - 1.1.0 (2015-02-17; 1.6.0) - Kyli expanded scope to all objects
	 */
	public static byte i8(Object n) {
		if (n instanceof Byte) {
			return (Byte) n;
		}
		if (n instanceof Number) {
			return ((Number) n).byteValue();
		}
		return Byte.parseByte(s(n));
	}

	/**
	 * Rounds the given number to the nearest {@code long}
	 *
	 * @param n the number to round
	 *
	 * @return {@code n}, rounded to the nearest {@code long}
	 *
	 * @version 1.1.0
	 * - 1.1.0 (2015-03-03; 1.6.0) - Kyli expanded scope to all objects
	 */
	public static long r64(Object n) {
		// if it's already an integer, just return it.
		if (n instanceof Long
					|| n instanceof Integer
					|| n instanceof Short
					|| n instanceof Byte) {
			return ((Number) n).longValue();
		}
		return Math.round(Double.valueOf(s(n)));
	}

	/**
	 * Shorthand for {@link #r64}. Rounds the given number to the nearest {@code long}
	 *
	 * @param n the number to round
	 *
	 * @return {@code n}, rounded to the nearest {@code long}
	 *
	 * @version 1.1.0
	 * - 1.1.0 (2015-03-03; 1.6.0) - Kyli expanded scope to all objects
	 */
	public static long r(Object n) {
		return r64(n);
	}

	/**
	 * Rounds the given number to the nearest {@code int}
	 *
	 * @param n the number to round
	 *
	 * @return {@code n}, rounded to the nearest {@code int}
	 *
	 * @version 1.1.0
	 * - 1.1.0 (2015-03-03; 1.6.0) - Kyli expanded scope to all objects
	 */
	public static int r32(Object n) {
		return Math.round(Float.valueOf(s(n)));
	}

	/**
	 * Converts the given number to a 64-bit floating-point decimal
	 *
	 * @param n the number to convert
	 *
	 * @return {@code n} as a 64-bit floating-point decimal
	 *
	 * @author Kyli Rouge
	 * @since 1.5.0
	 * @version 1.1.0
	 * - 1.1.0 (2015-03-03; 1.6.0) - Kyli expanded scope to all objects
	 */
	public static double f64(Object n) {
		if (n instanceof Number) {
			return ((Number) n).doubleValue();
		}
		return Double.valueOf(s(n));
	}

	/**
	 * Shorthand for {@link #f64(Number)}. Converts the given number to a 64-bit floating-point decimal
	 *
	 * @param n the number to convert
	 *
	 * @return {@code n} as a 64-bit floating-point decimal
	 *
	 * @author Kyli Rouge
	 * @since 1.5.0
	 * @version 1.1.0
	 * - 1.1.0 (2015-03-03; 1.6.0) - Kyli expanded scope to all objects
	 */
	public static double d(Object n) {
		return f64(n);
	}

	/**
	 * Converts the given number to a 32-bit floating-point decimal
	 *
	 * @param n the number to convert
	 *
	 * @return {@code n} as a 32-bit floating-point decimal
	 *
	 * @author Kyli Rouge
	 * @since 1.5.0
	 * @version 1.1.0
	 * - 1.1.0 (2015-03-03; 1.6.0) - Kyli expanded scope to all objects
	 */
	public static float f32(Object n) {
		if (n instanceof Number) {
			return ((Number) n).floatValue();
		}
		return Float.valueOf(s(n));
	}

	/**
	 * Shorthand for {@link #f32(Number)}. Converts the given number to a 32-bit floating-point decimal
	 *
	 * @param n the number to convert
	 *
	 * @return {@code n} as a 32-bit floating-point decimal
	 *
	 * @author Kyli Rouge
	 * @since 1.5.0
	 * @version 1.1.0
	 * - 1.1.0 (2015-03-03; 1.6.0) - Kyli expanded scope to all objects
	 */
	public static float f(Object n) {
		return f32(n);
	}
	//</editor-fold>
}
