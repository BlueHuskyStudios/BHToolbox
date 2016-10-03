package org.bh.tools.util;

import org.bh.tools.func.*;
import org.bh.tools.math.*;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static org.bh.tools.math.NumberConverter.*;
import static org.bh.tools.util.ArrayPP.ArrayPosition.*;
import static org.bh.tools.util.ArrayPP.SearchResults.*;
import static org.bh.tools.util.Do.S.*;


/**
 * ArrayPP, made for BHToolbox, is copyright Blue Husky Programming ©2015 BH-1-PS <hr>
 * Would have been called "{@code Array++}" if "{@code +}" were an acceptable character. This class is made to give
 * enhanced features to arrays, such as basic search functions and comparability, along with the standard accessing
 * methods. The internal structure is an array, so as to make the interactions as memory-efficient as possible.<br/>
 * <br/>
 * Many array-management methods return {@code this}, so as to allow daisy-chaining of commands, like you can with
 * {@code String}s.<br/>
 * <h3>Examples:</h3> {@code String} array: {@code final String[] S = new String[]{"Won", "too", "tree!"};}<br/>
 * {@code Array++} of {@code String}s:
 * {@code final ImmutableArrayPP<String> S = new ImmutableArrayPP<String>("Won", "too", "tree!");}
 *
 * @param <T> The type of object to store
 * <h3>Examples:</h3>
 * <ul>
 * <li>{@code ImmutableArrayPP<String> strings = new ImmutableArrayPP<>();}
 * <ul><li>A new ImmutableArray++ of {@code String}s. Analogous to {@code final String[] strings;}</li></ul>
 * </li>
 * <li>{@code ImmutableArrayPP<String> strings = new ImmutableArrayPP<>(args);}
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
 * @version 2.3.0  <pre>
 *		- 2016-10-01 (2.3.0)
 *                      ~ Ben added functional programming paradigms.
 *		- 2016-04-06 (2.2.0)
 *                      ~ Kyli renamed {@code ImmutableArray++} to {@code Array++}.
 *		~ 2015-08-30 (2.1.0) - Kyli renamed {@code ContainsBehavior} to {@code SearchBehavior}.
 *		+ 2015-03-03 (2.0.0) - Kyli created this completely new ArrayPP, as an immutable rewrite of {@link bht.tools.util.ArrayPP}.
 * </pre>
 *
 * @since 2015-03-03
 */
@SuppressWarnings({"unused", "WeakerAccess"}) public class ArrayPP<T>
        implements Comparable<ArrayPP<?>>,
        Iterable<T>,
        Serializable {

    public static final long serialVersionUID = 0x2_003_0000L;

    /**
     * The internal array that holds all the data for this Array++
     */
    @SuppressWarnings("ProtectedField")
    protected T[] array;
    protected final Class<T> type; //maybe passed into the method

    /**
     * Creates a new array++ with the given content type and contents.
     *
     * @param contents The contents of the array++.
     */
    @SuppressWarnings("unchecked") @SafeVarargs
    public ArrayPP(T... contents) {
        type = (Class<T>) (Class<?>)contents.getClass();
        array = Arrays.copyOf(contents, contents.length);
    }

    /**
     * Creates a new, empty, immutable array++ of the given size.
     *
     * @param initSize    The size of the new, empty, immutable array++
     * @param emptySample An non-null empty array to use to guarantee the new, empty, immutable array++ is of the right
     *                    type. If this is not empty, its contents will be at the beginning of the new, immutable
     *                    array++.
     */
    @SuppressWarnings("unchecked") @SafeVarargs
    public ArrayPP(int initSize, T... emptySample) {
        type = (Class<T>) (Class<?>)emptySample.getClass();
        array = Arrays.copyOf(emptySample, initSize);
    }

    /**
     * Creates a new, filled, immutable array++ of the given size.
     *
     * @param numberOfElements The size of the new, empty, immutable array++
     * @param emptySample      An non-null empty array to use to guarantee the new, empty, immutable array++ is of the
     *                         right type. If this is not empty, its contents will be at the beginning of the new,
     *                         immutable array++.
     * @param generator        The generator which will be used to fill this array with its contents
     */
    @SafeVarargs
    public ArrayPP(int numberOfElements, IndexedGenerator<T> generator, @NotNull T... emptySample) {
        this(numberOfElements, emptySample) ;
        for (int i = 0; i < numberOfElements; i++) {
            array[i] = generator.generate(i);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public int compareTo(@NotNull ArrayPP<?> other) {
        if (array instanceof Comparable[]
                && other.toArray() instanceof Comparable[]) {
            Averager avg = new Averager();
            if (other.length() > array.length) {
                for (int i = 0; i < Math.min(array.length, other.length()); i++) {
                    avg.average(((Comparable) other.get(i)).compareTo(array[i]));
                }
            }
            return toInt32(avg);
        }
        return array.length - other.length();
    }

    /**
     * Returns the item at the given position
     *
     * @param index the index of the item to get
     *
     * @return the item you wanted
     *
     * @throws ArrayIndexOutOfBoundsException if the given index is lower than 0 or greater than {@link #length()}
     */
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

    /**
     * Returns the length of the array
     *
     * @return the length of the array
     */
    public int length() {
        return array.length;
    }

    /**
     * Indicates whether this array has a length of {@code 0}
     *
     * @return {@code true} iff this array has a length of {@code 0}
     */
    public boolean isEmpty() {
        return array.length == 0;
    }

    /**
     * Indicates whether this array contains nothing but {@code null}s
     *
     * @return {@code true} iff this array contains nothing but {@code null}s
     */
    @SuppressWarnings("unchecked")
    public boolean isFlat() {
        return contains(SearchBehavior.SOLELY, (T) null);
    }

    /**
     * @return a <strong>copy</strong> of the array at the core of this class
     */
    @SuppressWarnings("unchecked")
    public T[] toArray() {
        T[] ret = (T[]) new Object[length()];
        System.arraycopy(array, 0, ret, 0, length());
        return ret;
    }

    /**
     * Gets the indices of all occurrences where {@code needle.equals(occurrence)}. If {@code needle} is {@code null},
     * {@link #indicesOfNulls()} is called.
     *
     * @param needle the item to search for
     *
     * @return the indices where {@code needle} occurs
     */
    public int[] indicesOf(T needle) {
        if (needle == null) {
            return indicesOfNulls();
        }
        LinkedList<Integer> indices = new LinkedList<>();
        for (int i = 0, l = length(); i < l; i++) {
            if (needle.equals(get(i))) {
                indices.add(i);
            }
        }
        Integer[] result = indices.toArray(new Integer[0]);
        int[] ret = new int[result.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = result[i];
        }
        return ret;
    }

    /**
     * Returns the indices of all {@code null} values in this array.
     *
     * @return the indices of all {@code null{ values in this array.
     */
    public int[] indicesOfNulls() {
        LinkedList<Integer> indices = new LinkedList<>();
        for (int i = 0, l = length(); i < l; i++) {
            if (get(i) == null) {
                indices.add(i);
            }
        }
        Integer[] result = indices.toArray(new Integer[0]);
        int[] ret = new int[result.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = result[i];
        }
        return ret;
    }

    /**
     * Returns the index of the given needle near the given position in the array. If the given needle can't be found at
     * all, {@link SearchResults#NOT_FOUND NOT_FOUND}.{@link SearchResults#INTVAL} is returned.
     *
     * @param near   the position to start searching from
     * @param needle the needle to search from
     *
     * @return the index of the given needle near the given position, or {@code -1} if none can be found.
     */
    public int indexOf(ArrayPosition near, T needle) {
        if (needle == null) {
            return indexOfNull(near);
        }
        switch (near) {
            case START:
                for (int i = 0, l = length(); i < l; i++) {
                    if (needle.equals(get(i))) {
                        return i;
                    }
                }
                break;
            case END:
                for (int i = length(); i > 0; i--) {
                    if (needle.equals(get(i))) {
                        return i;
                    }
                }
        }
        return NOT_FOUND.INTVAL;
    }


    /**
     * Returns the index of the first {@code null} value near the given position in the array. If no {@code null} can be
     * found at all, {@code -1} is returned
     *
     * @param near the position to start searching from
     *
     * @return the index of the first null near the given position, or {@code -1} if none can be found.
     */
    private int indexOfNull(ArrayPosition near) {
        switch (near) {
            case START:
                for (int i = 0, l = length(); i < l; i++) {
                    if (get(i) == null) {
                        return i;
                    }
                }
                break;
            case END:
                for (int i = length(); i > 0; i--) {
                    if (get(i) == null) {
                        return i;
                    }
                }
        }
        return NOT_FOUND.INTVAL;
    }

    /**
     * Determines if the given {@code needle} exists within this array
     *
     * @param needle the needle to search for
     *
     * @return {@code true} iff the given {@code needle} was found.
     */
    public boolean contains(T needle) {
        return indexOf(START, needle) >= 0;
    }

    /**
     * Determines if the given {@code needles} exist within this array, based on the {@code behavior} given. When using
     * ANY and SOLELY behavior, this is more efficient when {@code needles.length < this.length()}. The opposite is true
     * with ALL behavior.
     *
     * @param behavior the behavior of this search
     * @param needles  the needles to search for
     *
     * @return {@code true} iff the given needles are found with the given behavior
     */
    @SuppressWarnings("unchecked")
    public boolean contains(SearchBehavior behavior, T... needles) {
        switch (behavior) {
            case ANY:
                // TODO: If this array is smaller than the needle list, make the outer loop the needles
                for (int i = 0, l = length(); i < l; i++) { // let's hope this array has more items than are being searched for
                    T t = get(i);
                    for (T needle : needles) {
                        if (Objects.equals(needle, t)) {
                            return true;
                        }
                    }
                }
                break;
            case ALL:
                needleLooper:
                for (T needle : needles) { // for all needles...
                    for (int i = 0, l = length(); i < l; i++) { // compare against all items in this array
                        T t = get(i);
                        if (Objects.equals(needle, t)) { // if there is an item in the array that matches this needle
                            continue needleLooper; // start back up top
                        }
                    }
                    return false; // if that if statement was never entered, we never found it.
                }
                break;
            case SOLELY:
                for (int i = 0, l = length(); i < l; i++) {
                    T t = get(i);
                    for (T needle : needles) {
                        if (Objects.equals(needle, t)) {
                            return true;
                        }
                    }
                }
                break;
        }
        return false;
    }

    @Override
    public String toString() {
        return as(this);
    }

    /**
     * Returns this array, starting with the given {@code preceding} character sequence, ending with the given
     * {@code succeeding} one, and with the given {@code separator} between each element.
     *
     * @param preceding  the character sequence to start the String with
     * @param separator  the character sequence to end the String with
     * @param succeeding the character sequence to separate each item with
     *
     * @return this array, starting with, separated by, and ending with the given sequences
     *
     * @author Kyli Rouge
     * @since 2014-08-20
     * @version 1.0.0 - 2014-08-20 (1.8.0) - Kyli Rouge created this method for more powerful stringification
     */
    public String toString(CharSequence preceding, CharSequence separator, CharSequence succeeding) {
        if (isEmpty()) {
            return s(preceding) + succeeding;
        }
        StringBuilder sb = new StringBuilder(preceding);
        for (int i = 0, l = length() - 1; i < l; i++) {
            sb.append(get(i)).append(separator);
        }
        return sb.append(getLastItem()).append(succeeding).toString();
    }

    public boolean containsNull() {
        return stream().anyMatch(t -> null == t);
    }

    public Stream<T> stream() {
        return Arrays.stream(toArray());
    }

    public T getLastItem() {
        return get(length() - 1);
    }


    /**
     * Basic, universal positions in an array
     */
    public enum ArrayPosition {
        /**
         * Represents the beginning of the array (index {@code 0})
         */
        START,
        /**
         * Represents the end of the array (index {@code length})
         */
        END
    }


    /**
     * Indicates what kind of behavior to exhibit when searching.
     */
    public enum SearchBehavior {
        /**
         * Indicates that search methods should stop after the first find
         */
        ANY,
        /**
         * Indicates that search methods should guarantee that ALL given items are in the array
         */
        ALL,
        /**
         * Indicates that search methods should guarantee that the the array consists SOLELY of the given items
         */
        SOLELY
    }


    /**
     * Indicates a specific, yet non-typical search result, wherein a specific result cannot be returned.
     */
    public enum SearchResults {

        /**
         * Indicates that the value which was searched for was not found. This has an {@link #INTVAL integer value} of
         * {@code -1}
         */
        NOT_FOUND(-1);

        /**
         * The integer value of this search result.
         */
        public final int INTVAL;

        SearchResults(int intVal) {
            INTVAL = intVal;
        }
    }

}
