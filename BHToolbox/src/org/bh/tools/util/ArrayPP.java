package org.bh.tools.util;

import bht.tools.misc.CompleteObject;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.bh.tools.util.ImmutableArrayPP.ArrayPosition.END;
import static org.bh.tools.util.ImmutableArrayPP.ArrayPosition.START;
import static org.bh.tools.util.ImmutableArrayPP.SearchBehavior.ALL;
import static org.bh.tools.util.ImmutableArrayPP.SearchBehavior.SOLELY;



/**
 * Would be called {@code Array++} if {@code +} was a legal character for a class name. This is a mutable array with
 * many enhancements and conveniences.
 *
 * @param <T> The type of item to store
 * 
 * @see ImmutableArrayPP
 *
 * @author Kyli of Blue Husky Programming
 * @version 2.1.0
 * <pre>
 *		- 2015-08-30 (2.1.0)
 *			! Kyli simplified {@link #emptyArrayOfLength(int)}
 *			~ Kyli changed the log message when an object couldn't be destroyed with {@link #clear(boolean) clear(true)}.
 *			+ Kyli added {@link #remove(SearchBehavior,Object,ArrayPosition)},
 *				{@link #remove(int)}, and {@link #removeChunk(int,int)}.
 *		- 2015-03-03 (2.0.0) . Kyli created this completely new ArrayPP, as a rewrite of {@link bht.tools.util.ArrayPP}.
 * </pre>
 *
 * @since 2015-03-03
 */
public class ArrayPP<T> extends ImmutableArrayPP<T> {
	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	public static final long serialVersionUID = 02_001_0000L;

	@SafeVarargs
	public ArrayPP(T... basis) {
		super(basis);
	}

	/**
	 * Creates a new Array++ with the items from the given {@link Iterable}.
	 *
	 * @param basis the {@link Iterable} holding the items to put in this Array++
	 */
	@SuppressWarnings({"unchecked", "OverridableMethodCallInConstructor"})
	public ArrayPP(Iterable<T> basis) {
		basis.forEach((i) -> append(i));
	}

	/**
	 * Creates a new, empty array++ of the given size.
	 *
	 * @param initSize    The size of the new, empty array
	 * @param emptySample An empty array to use to guarantee the new, empty array++ is of the right type. If this is
	 *                    not empty, its contents will be at the beginning of the new array++.
	 */
	public ArrayPP(int initSize, T[] emptySample) {
		super(initSize, emptySample);
	}

	@SuppressWarnings("unchecked")
	public ArrayPP<T> add(T... newVals) {
		return add(END, newVals);
	}

	@SuppressWarnings("unchecked")
	public ArrayPP<T> add(ArrayPosition position, T... newVals) {
		switch (position) {
			case END:
				return append(newVals);
			case START:
				return prepend(newVals);
		}
		return this;
	}

	/**
	 * Removes all objects from the array, with an option to destroy them as they are removed. Destruction is calling
	 * their {@link #finalize()} method.
	 *
	 * @param destructive
	 *
	 * @return
	 */
	public ArrayPP<T> clear(boolean destructive) {
		if (destructive) {
			for (int i = 0; i < array.length; i++) {
				T t = array[i];
				if (t instanceof CompleteObject) {
					try {
						((CompleteObject) t).finalize();
					}
					catch (Throwable ex) {
						Logger.getLogger(ArrayPP.class.getName()).log(Level.WARNING, "Could not destroy item " + i
						                                                             + ": " + t, ex);
					}
				}
			}
		}
		array = emptyArrayOfLength(0);
		return this;
	}

	/**
	 * Removes all objects from the array.
	 *
	 * @return this
	 */
	public ArrayPP<T> clear() {
		return clear(false);
	}

	public ArrayPP<T> increaseSize(int thisManyMoreSlots) {
		System.arraycopy(array, 0,
				array, 0,
				array.length + thisManyMoreSlots);
		return this;
	}

	/**
	 * Sets the value at slot {@code index} to be {@code newVal}
	 *
	 * @deprecated untested!
	 * @param index  The index of the slot to change
	 * @param newVal The new value to put into the slot
	 *
	 * @return {@code this}
	 */
	public ArrayPP<T> set(int index, T newVal) {
		if (index > this.length()) {
			increaseSize(this.length() - index);
		}
		array[index] = newVal;
		return this;
	}

	/**
	 * Sets the values at slots {@code fromIndex} through {@code newVals.length - fromIndex} to the values in
	 * {@code newVals}
	 *
	 * @deprecated untested!
	 * @param fromIndex The index of the slot to change
	 * @param newVals   The new values to put into the slot
	 *
	 * @return {@code this}
	 */
	public ArrayPP<T> setAll(int fromIndex, T[] newVals) {
		System.arraycopy(
				newVals, 0,
				array, fromIndex,
				this.length() - fromIndex);
		return this;
	}

	/**
	 * Appends the given values to the end of this array.
	 *
	 * @param newVals the new values to append
	 *
	 * @return {@code this}
	 */
	@SuppressWarnings("unchecked")
	public ArrayPP<T> prepend(T... newVals) {
		int length = length(),
				newLength = length + newVals.length,
				currentIndex = 0;
		T[] copy = emptyArrayOfLength(newLength);
		System.arraycopy(array, 0, copy, newVals.length, length);
		array = copy;
		for (; currentIndex < newVals.length; currentIndex++) {
			array[currentIndex] = newVals[currentIndex];
		}

		return this;
	}

	@SuppressWarnings("UseOfSystemOutOrSystemErr")
	public static void main(String[] args) {
		ArrayPP<String> test = new ArrayPP<>("won", "too", "tree");
		test.prepend("fore", "jive");
		test.append("sicks", "sever");
		System.out.println(test);
	}

	/**
	 * Remove all instances of the given value from this array++.
	 *
	 * @param val The value to search for and remove
	 *
	 * @return {@code this}
	 */
	public ArrayPP<T> removeAll(T val) {
		return remove(ALL, val, START);
	}

	/**
	 * Removes the given item from the array, using the given behaviors.
	 *
	 * @param behavior The behavior by which to search
	 * <ul>
	 * <li>If {@link ImmutableArrayPP.SearchBehavior#ANY ANY}, removes the first found matching object at an index
	 * close to {@code near}. If none is found, nothing is changed.</li>
	 * <li>If {@link ImmutableArrayPP.SearchBehavior#ALL ALL}, removes each and every found matching object. If none is
	 * found, nothing is changed.</li>
	 * <li>If {@link ImmutableArrayPP.SearchBehavior#SOLELY SOLELY}, first determines if the array consists solely of
	 * matching objects. If it does, {@link #clear()} is called.</li>
	 * </ul>
	 * @param val      The value to remove
	 * @param near     Used if {@code behavior} is {@link ImmutableArrayPP.SearchBehavior#ANY ANY}.
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayPP<T> remove(SearchBehavior behavior, T val, ArrayPosition near) {
		switch (behavior) {
			case ANY:
				int index = indexOf(near, val);
				if (index >= 0) {
					remove(index);
				}
				break;

			case ALL:
				for (int i = 0; i < array.length; i++) {
					if (Objects.equals(val, array[i])) {
						remove(i);
					}
				}
				break;

			case SOLELY:
				if (contains(SOLELY, val)) {
					clear();
				}
				break;
		}
		return this;
	}

	/**
	 * Removes the item at index {@code index}.
	 *
	 * @param index The index of the item to remove
	 *
	 * @return this
	 */
	public ArrayPP<T> remove(int index) {
		return removeChunk(index, index);
	}

	/**
	 * Removes all items from {@code startIndex} to {@code endIndex}, inclusive.
	 *
	 * @param startIndex The first index whose item is to be removed
	 * @param endIndex   The last index whose item is to be removed
	 *
	 * @return this
	 */
	@SuppressWarnings("AssignmentToMethodParameter")
	public ArrayPP<T> removeChunk(int startIndex, int endIndex) {
		if (startIndex > endIndex) {
			int oldStart = startIndex;
			startIndex = endIndex;
			endIndex = oldStart;
		}
		System.arraycopy(array, startIndex, // start writing at startIndex
				array, endIndex + 1, // start writing with the object after endIndex
				array.length - (endIndex - startIndex + 1)); // shorten the array appropriately
		return this;
	}

	/**
	 * Returns a new, empty array of type {@code T} with the given size.
	 *
	 * @param newLength the length of the new array
	 *
	 * @return a new, empty array of type {@code T} and size {@code newLength}.
	 */
	@SuppressWarnings("unchecked")
	private T[] emptyArrayOfLength(int newLength) {
		return (T[]) Array.newInstance(array.getClass().getComponentType(), newLength);
	}

	/**
	 * Appends the given values to the end of this array.
	 *
	 * @param newVals the new values to append
	 *
	 * @return {@code this}
	 */
	@SuppressWarnings("unchecked")
	public ArrayPP<T> append(T... newVals) {
		int length = length(), insertPoint = length, currentIndex = 0;
		array = java.util.Arrays.copyOf(array, length += newVals.length);

		for (; insertPoint < length; insertPoint++, currentIndex++) {
			array[insertPoint] = newVals[currentIndex];
		}

		return this;
	}

	/**
	 * Used only for value counting in {@link #addAll(Iterable)}
	 */
	private volatile long _addAll_valuesCount;

	/**
	 * Appends the values in the given Iterable to the end of this array. <strong>The given {@link Iterable}
	 * <em>must</em> be
	 * finite!</strong>
	 *
	 * @param vals the values to be appended, as represented by an {@code Iterable}
	 *
	 * @return the resulting array.
	 */
	public ArrayPP<T> addAll(Iterable<T> vals) {
		ArrayPP<T> app = new ArrayPP<>(vals);
		final int INIT_LENGTH = length();
		Spliterator<T> split = vals.spliterator();

		_addAll_valuesCount = split.getExactSizeIfKnown();
		if (_addAll_valuesCount < 0) { // if exact size isn't known
			_addAll_valuesCount = split.estimateSize();
		}
		if (_addAll_valuesCount < 0 || _addAll_valuesCount == Long.MAX_VALUE) { // if size is still unknown
			split.forEachRemaining((item) -> _addAll_valuesCount++);
		}

		array = Arrays.copyOf(array, INIT_LENGTH + (int) _addAll_valuesCount);

		split.forEachRemaining(new Consumer<T>() {
			private int insertPoint = INIT_LENGTH;

			@Override
			public void accept(T currentItem) {
				array[insertPoint++] = currentItem;
			}
		});
		return this;
	}

	public ArrayPP<T> addAll(T[] newVals) {
		int length = length(), insertPoint = length, currentIndex = 0;
		array = java.util.Arrays.copyOf(array, length += newVals.length);

		for (; insertPoint < length; insertPoint++, currentIndex++) {
			array[insertPoint] = newVals[currentIndex];
		}

		return this;
	}

	//<editor-fold defaultstate="collapsed" desc="Wrapping & Unwrapping">
	public static Byte[] wrap(byte[] unwrapped) {
		if (unwrapped == null) {
			return null;
		}
		if (unwrapped.length == 0) {
			return new Byte[0];
		}

		Byte[] ret = new Byte[unwrapped.length];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = unwrapped[i];
		}
		return ret;
	}

	public static byte[] unwrap(Byte[] wrapped) {
		if (wrapped == null) {
			return null;
		}
		if (wrapped.length == 0) {
			return new byte[0];
		}

		byte[] ret = new byte[wrapped.length];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = wrapped[i];
		}
		return ret;
	}

	public static Integer[] wrap(int[] unwrapped) {
		if (unwrapped == null) {
			return null;
		}
		if (unwrapped.length == 0) {
			return new Integer[0];
		}

		Integer[] ret = new Integer[unwrapped.length];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = unwrapped[i];
		}
		return ret;
	}

	public static int[] unwrap(Integer[] wrapped) {
		if (wrapped == null) {
			return null;
		}
		if (wrapped.length == 0) {
			return new int[0];
		}

		int[] ret = new int[wrapped.length];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = wrapped[i];
		}
		return ret;
	}
	//</editor-fold>
}
