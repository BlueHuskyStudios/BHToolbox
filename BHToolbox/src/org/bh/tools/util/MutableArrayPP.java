package org.bh.tools.util;

import bht.tools.misc.CompleteObject;
import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bh.tools.math.Range.IntegerRange.MutableIntegerRange;

import static org.bh.tools.util.ArrayPP.ArrayPosition.END;
import static org.bh.tools.util.ArrayPP.ArrayPosition.START;
import static org.bh.tools.util.ArrayPP.SearchBehavior.ALL;
import static org.bh.tools.util.ArrayPP.SearchBehavior.SOLELY;


/**
 * Would be called {@code MutableArray++} if {@code +} was a legal character for a class name. This is a mutable array
 * with many enhancements and conveniences.
 *
 * @param <T> The type of item to store
 *
 * @see ArrayPP
 *
 * @author Kyli of Blue Husky Programming
 * @version 2.2.0  <pre>
 *		- 2016-04-06 (2.2.0)
 *                      ~ Kyli renamed {@code Array++} to {@code MutableArray++}
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
public class MutableArrayPP<T> extends ArrayPP<T> {

    @SuppressWarnings("FieldNameHidesFieldInSuperclass")
    public static final long serialVersionUID = 02_001_0000L;

    @SafeVarargs
    public MutableArrayPP(T... basis) {
        super(basis);
    }

    /**
     * Creates a new Array++ with the items from the given {@link Iterable}.
     *
     * @param basis the {@link Iterable} holding the items to put in this Array++
     */
    @SuppressWarnings({"unchecked", "OverridableMethodCallInConstructor"})
    public MutableArrayPP(Iterable<T> basis) {
        basis.forEach((i) -> append(i));
    }

    /**
     * Creates a new, empty array++ of the given size.
     *
     * @param initSize    The size of the new, empty array
     * @param emptySample An empty array to use to guarantee the new, empty array++ is of the right type. If this is not
     *                    empty, its contents will be at the beginning of the new array++.
     */
    public MutableArrayPP(int initSize, T[] emptySample) {
        super(initSize, emptySample);
    }

    @SuppressWarnings("unchecked")
    public MutableArrayPP<T> add(T... newVals) {
        return add(END, newVals);
    }

    @SuppressWarnings("unchecked")
    public MutableArrayPP<T> add(ArrayPosition position, T... newVals) {
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
    public MutableArrayPP<T> clear(boolean destructive) {
        if (destructive) {
            for (int i = 0; i < array.length; i++) {
                T t = array[i];
                if (t instanceof CompleteObject) {
                    try {
                        ((CompleteObject) t).finalize();
                    } catch (Throwable ex) {
                        Logger.getLogger(MutableArrayPP.class.getName()).log(Level.WARNING, "Could not destroy item " + i
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
    public MutableArrayPP<T> clear() {
        return clear(false);
    }

    public MutableArrayPP<T> increaseSize(int thisManyMoreSlots) {
        System.arraycopy(array, 0,
                array, 0,
                array.length + thisManyMoreSlots);
        return this;
    }

    /**
     * Identical to {@link #set(int, Object, boolean) set(index, newVal, false)}.
     *
     * @param index  The index of the slot to change
     * @param newVal The new value to put into the slot
     *
     * @return {@code this}
     *
     * @throws ArrayIndexOutOfBoundsException if {@code index} is greater than {@link #length() length()}
     */
    public MutableArrayPP<T> set(int index, T newVal) {
        return set(index, newVal, false);
    }

    /**
     * Sets the value at slot {@code index} to be {@code newVal}
     *
     * @param index      The index of the slot to change
     * @param newVal     The new value to put into the slot
     * @param increaseOK Indicates whether it's OK to increase the size of the array to accommodate the new value. If
     *                   {@code false}, a {@link ArrayIndexOutOfBoundsException} may be thrown by passing an index that
     *                   is too high.
     *
     * @return {@code this}
     *
     * @throws ArrayIndexOutOfBoundsException if {@code increaseOK} is {@code false} and {@code index} is greater than
     *                                        {@link #length() length()}
     */
    public MutableArrayPP<T> set(int index, T newVal, boolean increaseOK) {
        if (increaseOK) {
            if (index > this.length()) {
                increaseSize((this.length() - index) + 1);
            }
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
    public MutableArrayPP<T> setAll(int fromIndex, T[] newVals) {
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
    public MutableArrayPP<T> prepend(T... newVals) {
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
        MutableArrayPP<String> test = new MutableArrayPP<>("won", "too", "tree");
        test.prepend("fore", "jive");
        test.append("sicks", "heaven");
        System.out.println(test);
    }

    /**
     * Remove all instances of the given value from this array++.
     *
     * @param val The value to search for and remove
     *
     * @return {@code this}
     */
    public MutableArrayPP<T> removeAll(T val) {
        return remove(ALL, val, START);
    }

    /**
     * Removes the given item from the array, using the given behaviors.
     *
     * @param behavior The behavior by which to search
     * <ul>
     * <li>If {@link ImmutableArrayPP.SearchBehavior#ANY ANY}, removes the first found matching object at an index close
     * to {@code near}. If none is found, nothing is changed.</li>
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
    public MutableArrayPP<T> remove(SearchBehavior behavior, T val, ArrayPosition near) {
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
    public MutableArrayPP<T> remove(int index) {
        return removeChunk(index, index);
    }

    /**
     * Removes all items from {@code startIndex}, inclusive, to {@code endIndex}, inclusive.
     *
     * @param startIndex The first index whose item is to be removed
     * @param endIndex   The last index whose item is to be removed
     *
     * @return this
     */
    @SuppressWarnings("AssignmentToMethodParameter")
    public MutableArrayPP<T> removeChunk(int startIndex, int endIndex) {
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
    public MutableArrayPP<T> append(T... newVals) {
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
     * <em>must</em> be finite!</strong>
     *
     * @param vals the values to be appended, as represented by an {@code Iterable}
     *
     * @return the resulting array.
     */
    public MutableArrayPP<T> addAll(Iterable<T> vals) {
        MutableArrayPP<T> app = new MutableArrayPP<>(vals);
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

    public MutableArrayPP<T> addAll(T[] newVals) {
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

    public Map<Integer, T> toMap() {
        return new Map<Integer, T>() {
            /**
             * @return the result of {@link #length()}
             */
            @Override
            public int size() {
                return length();
            }

            /**
             * If the given key is a {@link Number}, returns {@code true} iff it's between {@code 0} and
             * {@link #length()}. Else, {@code false} is returned.
             *
             * @param key The index of the object to find.
             * @return {@code true} iff the given key is a number between 0 and {@link length()}
             */
            @Override
            public boolean containsKey(Object key) {
                if (key instanceof Number) {
                    int intKey = ((Number) key).intValue();
                    return intKey > 0 && intKey < length();
                }
                return false;
            }

            /**
             * If {@code needle} is {@code null}, this returns the result of {@link #containsNull()}. Else, if
             * {@code needle} is of type {@link #T}, returns the result of {@link #contains(Object)}. Else, it returns
             * {@code false}.
             *
             * @param needle The object to find in the values of this array++.
             * @return {@code true} iff {@code needle} is in this array++.
             */
            @SuppressWarnings("unchecked") // type is transitively checked
            @Override
            public boolean containsValue(Object needle) {
                if (null == needle) {
                    return containsNull();
                } else if (type.isInstance(needle)) {
                    return contains((T) needle);
                } else {
                    return false;
                }
            }

            /**
             * If {@code key} is a non-{@code null} {@link Number}, this acts like {@link #get(int)} passed the result
             * of {@code key}'s {@link Number#intValue() intValue()} method. Else, returns {@code null}.
             *
             * @param key The index of the object to find.
             * @return The object at index {@code key}, or {@code null}.
             *
             * @throws ArrayIndexOutOfBoundsException If the given key is a number outside the array bounds.
             */
            @Override
            public T get(Object key) {
                if (null == key) {
                    return null;
                } else if (key instanceof Number) {
                    return MutableArrayPP.this.get(((Number) key).intValue());
                } else {
                    return null;
                }
            }

            @Override
            public T put(Integer key, T newVal) {
                T oldVal = get(key);
                set(key, newVal);
                return oldVal;
            }

            /**
             * Iff the given key is a number, the object at the index represented by its
             * {@link Number#intValue() intValue()} is removed and returned. Else, {@code null} is returned.
             * <hr/> {@inheritDoc}
             *
             * @param key The index of the object to fetch.
             * @return The object that was at index {@code key} (using {@link MutableArrayPP#get(int)}) before it was
             *         removed, or {@code null} if you don't give a valid index as the key.
             *
             * @throws ArrayIndexOutOfBoundsException If the given key is a number outside the array bounds.
             */
            @Override
            public T remove(Object key) {
                if (null == key) {
                    return null;
                } else if (key instanceof Number) {
                    int idx = ((Number) key).intValue();
                    T oldVal = this.get(idx);
                    MutableArrayPP.this.remove(idx);
                    return oldVal;
                }
                return null;
            }

            @Override
            public void putAll(Map<? extends Integer, ? extends T> m) {
                for (Entry<? extends Integer, ? extends T> entry : m.entrySet()) {
                    set(entry.getKey(), entry.getValue(), true);
                }
            }

            @Override
            public void clear() {
                MutableArrayPP.this.clear();
            }

            @Override
            public Set<Integer> keySet() {
                return new MutableIntegerRange(0, length() - 1).toInt32Set();
            }

            @Override
            public Collection<T> values() {
                return MutableArrayPP.this.toCollection();
            }

            @Override
            public Set<Entry<Integer, T>> entrySet() {
                // I wish there were a more efficient way to do this :/

                HashSet<Entry<Integer, T>> ret = new HashSet<>(length());
                for (int i = length() - 1; i > 0; i--) {
                    ret.add(new AbstractMap.SimpleEntry<>(i, get(i)));
                }
                return ret;
            }

            @Override
            public boolean isEmpty() {
                return length() == 0;
            }
        };
    }

    public Collection<T> toCollection() {
        return Arrays.asList(toArray());
    }
}
