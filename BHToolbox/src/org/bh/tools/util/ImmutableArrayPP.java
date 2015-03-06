package org.bh.tools.util;

import static bht.tools.util.Do.i;
import java.io.Serializable;
import java.util.Iterator;
import java.util.logging.Logger;
import org.bh.tools.math.Averager;



/**
 * ImmutableArrayPP, made for BHToolbox, is copyright Blue Husky Programming ©2015 GPLv3 <hr/>
 * Would have been called "ImmutableArray++" if "+" were an acceptable character. This class is made to give enhanced features
 * to arrays, such as basic search functions and comparability, along with the standard accessing methods. The internal
 * structure is an array, so as to make the interactions as memory-efficient as possible.<br/>
 * <br/>
 * Many array-management methods return {@code this}, so as to allow daisy-chaining of commands, like you can with
 * {@code String}s.<br/>
 * <h3>Examples:</h3>
 * {@code String} array: {@code final String[] S = new String[]{"Won", "too", "tree!"};}<br/>
 * {@code Array++} of {@code String}s:
 * {@code final ImmutableArrayPP<String> S = new ImmutableArrayPP<String>("Won", "too", "tree!");}
 *
 * @param <T> The type of array with which this object will be dealing.
 * <h3>Examples:</h3>
 * <ul>
 * <li>{@code ImmutableArrayPP<String> strings = new ImmutableArrayPP<>();}
 * <ul><li>A new ImmutableArray++ of {@code String}s. Analogous to {@code final String[] strings;}</li></ul>
 * </li>
 * <li>{@code ImmutableArrayPP<String> strings = new ImmutableArrayPP<String>(args);}
 * <ul><li>A clone of an array of {@code String}s. Analogous to {@code final String[] strings = args;}</li></ul>
 * </li>
 * <li>{@code String[] newArgs = new ImmutableArrayPP<String>(args).toArray();}
 * <ul>
 * <li>A clone of an array of {@code String}s. Analogous to
 * {@code String[] temp; final String[] strings = System.arraycopy(args, 0, temp, 0, args.length);}.</li>
 * </ul>
 * </li>
 * </ul>
 *
 * @author Kyli of Blue Husky Programming
 * @version 2.0.0
 * <pre>
 *		- 2015-03-03 (2.0.0) - Kyli created this completely new ArrayPP, as an immutable rewrite of the old version.
 * </pre>
 *
 * @since 2015-03-03
 */
public class ImmutableArrayPP<T>
		implements Comparable<ImmutableArrayPP<?>>,
				   Iterable<T>,
				   Serializable {
	private T[] array;

	@Override
	@SuppressWarnings("unchecked")
	public int compareTo(ImmutableArrayPP<?> other) {
		if (array instanceof Comparable[]
					&& other.toArray() instanceof Comparable[]) {
			Averager avg = new Averager();
			if (other.length() > array.length) {
				for (int i = 0; i < Math.min(array.length, other.length()); i++) {
					avg.addToAverage(((Comparable) other.get(i)).compareTo(array[i]));
				}
			}
			return i(avg);
		}
		return array.length - other.length();
	}

	public T get(int index) {
		return array[index];
	}

	@Override
	public Iterator<T> iterator() {
		return new java.util.Iterator<T>() {
			private int pos = 0;

			@Override
			public boolean hasNext() {
				return pos < length();
			}

			@Override
			public T next() throws ArrayIndexOutOfBoundsException {
				return get(++pos - 1);
			}

			@Override
			public void remove() {
				Logger.getLogger(getClass().getName()).severe("remove() called on immutable array");
			}
		};
	}

	public int length() {
		return array.length;
	}

	/**
	 * Returns a <strong>copy</strong> of the array at the core of this class
	 *
	 * @return a <strong>copy</strong> of the array at the core of this class
	 */
	@SuppressWarnings("unchecked")
	public T[] toArray() {
		try {
			T[] ret = (T[]) new Object[length()];
			for (int i = 0, l = ret.length; i < l; i++) {
				ret[i] = get(i);
			}
			return ret;
		}
		catch (Exception e) {
			return array;
		}
	}
}
