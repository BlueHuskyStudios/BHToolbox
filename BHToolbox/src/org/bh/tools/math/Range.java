package org.bh.tools.math;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * Range, made for English Learner, is copyright BHStudios ©2016 BH-1-PS <hr/>
 * A range of numeric values.
 *
 * @author Kyli Rouge of BHStudios
 * @since 2016-04-18
 */
public interface Range<T extends Number> {

    /**
     * @return The lowest value in this range. This is a number (not null!) less than {@link #getHigh() getHigh()}.
     */
    public T getLow();

    /**
     * @return The highest value in this range This is a number (not null!) greater than {@link #getLow() getLow()}.
     */
    public T getHigh();

    /**
     * @return The distance between the low and high points. This is never negative.
     */
    public T getLength();

    /**
     * Determines whether the given value is within this range, inclusively.
     *
     * @param value The value to test.
     * @return {@code true} iff the given value is within this range.
     */
    public boolean contains(final T value);

    /**
     * Feel free to use this in a {@link Object#toString() toString()}!
     *
     * @return {@link #getLow() getLow() }{@code + " to " + }{@link #getHigh() getHigh()}
     */
    public default String stringValue() {
        return getLow() + " to " + getHigh();
    }

    /**
     * A mutable version of {@link IntRange}
     */
    public static interface MutableRange<T extends Number> extends Range<T> {

        /**
         * Changes the lowest value of this range. If {@code newLow} is higher than {@link #getHigh() the old high},
         * then this acts like {@link #setLow(long) setLow(oldHigh);} {@link #setHigh(long) setHigh(newLow);}. Imagine
         * this:
         * <pre>
         * IntRange r = new IntRange(3, 6);
         * 0  1  2 [3  4  5  6] 7  8  9
         *          ^--------^
         *
         * r.setLow(4);
         * 0  1  2  3 [4  5  6] 7  8  9
         *             ^-----^
         *
         * r.setLow(8);
         * 0  1  2  3  4  5 [6  7  8] 9
         *                   ^-----^
         * </pre>
         *
         * @param newLow The new lowest value in this range. {@code null} is interpreted as {@code 0}.
         * @return {@code this}
         */
        public MutableRange<T> setLow(T newLow);

        /**
         * Changes the highest value of this range. If {@code newHigh} is lower than {@link #getLow() the old low}, then
         * this acts like {@link #setHigh(long) setHigh(oldLow);} {@link #setLow(long) setLow(newHigh);}. Imagine this:
         * <pre>
         * IntRange r = new IntRange(3, 6);
         * 0  1  2 [3  4  5  6] 7  8  9
         *          ^--------^
         *
         * r.setHigh(5);
         * 0  1  2 [3  4  5] 6  7  8  9
         *          ^-----^
         *
         * r.setHigh(1);
         * 0 [1  2  3  4  5] 6  7  8  9
         *    ^-----------^
         * </pre>
         *
         * @param newLow The new highest value in this range. {@code null} is interpreted as {@code 0}.
         * @return {@code this}
         */
        public MutableRange<T> setHigh(T newHigh);
    }

    /**
     * An implementation of {@link Range} which uses integer numbers.
     */
    public static class IntegerRange implements Range<Long> {

        protected long low, high;

        /**
         * Creates a new IntegerRange with the given values
         *
         * @param initLow  The initial low value of the range
         * @param initHigh The initial high value of the range
         */
        public IntegerRange(long initLow, long initHigh) {
            low = initLow;
            high = initHigh;
        }

        /**
         * @return A set of all integers between {@link #getLow() low} and {@link #getHigh() high}, inclusive.
         */
        public Set<Long> toSet() {
            return new AbstractSet<Long>() {
                @Override
                public Iterator<Long> iterator() {
                    return new Iterator<Long>() {
                        long latest = 0;

                        @Override
                        public boolean hasNext() {
                            return latest < getLength();
                        }

                        @Override
                        public Long next() {
                            return latest++;
                        }
                    };
                }

                @Override
                public int size() {
                    return getLength().intValue();
                }

                @Override
                public boolean isEmpty() {
                    return Objects.equals(getLow(), getHigh());
                }

                @Override
                public boolean contains(Object o) {
                    if (o instanceof Number) {
                        return IntegerRange.this.contains(((Number) o).longValue());
                    }
                    return false;
                }
            };
        }

        @Override
        public Long getLow() {
            return low;
        }

        @Override
        public Long getHigh() {
            return high;
        }

        @Override
        public Long getLength() {
            return high - low;
        }

        @Override
        public boolean contains(final Long value) {
            return getLow() < value && value < getHigh();
        }

        /**
         * A mutable version of {@link IntegerRange}.
         */
        public static class MutableIntegerRange extends IntegerRange implements MutableRange<Long> {

            /**
             * Creates a new MutableIntegerRange with the given values
             *
             * @param initLow  The initial low value of the range
             * @param initHigh The initial high value of the range
             */
            public MutableIntegerRange(long initLow, long initHigh) {
                super(initLow, initHigh);
            }

            @Override
            public MutableIntegerRange setLow(Long newLow) {
                if (null == newLow) {
                    newLow = 0l;
                }
                if (newLow <= getLow()) {
                    low = newLow;
                } else {
                    low = getHigh();
                    setHigh(newLow);
                }
                return this;
            }

            @Override
            public MutableIntegerRange setHigh(Long newHigh) {
                if (null == newHigh) {
                    newHigh = 0l;
                }
                if (newHigh >= getHigh()) {
                    high = newHigh;
                } else {
                    high = getLow();
                    setLow(newHigh);
                }
                return this;
            }
        }
    }

    /**
     * An implementation of {@link Range} which uses real numbers.
     */
    public static class RealRange implements Range<Double> {

        protected double low, high;

        /**
         * Creates a new RealRange with the given values
         *
         * @param initLow  The initial low value of the range
         * @param initHigh The initial high value of the range
         */
        public RealRange(double initLow, double initHigh) {
            low = initLow;
            high = initHigh;
        }

        @Override
        public Double getLow() {
            return low;
        }

        @Override
        public Double getHigh() {
            return high;
        }

        @Override
        public Double getLength() {
            return high - low;
        }

        @Override
        public boolean contains(final Double value) {
            return getLow() < value && value < getHigh();
        }

        /**
         * A mutable version of {@link IntegerRange}.
         */
        public static class MutableRealRange extends RealRange implements MutableRange<Double> {

            /**
             * Creates a new MutableRealRange with the given values
             *
             * @param initLow  The initial low value of the range
             * @param initHigh The initial high value of the range
             */
            public MutableRealRange(long initLow, long initHigh) {
                super(initLow, initHigh);
            }

            @Override
            public MutableRealRange setLow(Double newLow) {
                if (null == newLow) {
                    newLow = 0d;
                }
                if (newLow <= getLow()) {
                    low = newLow;
                } else {
                    low = getHigh();
                    setHigh(newLow);
                }
                return this;
            }

            @Override
            public MutableRealRange setHigh(Double newHigh) {
                if (null == newHigh) {
                    newHigh = 0d;
                }
                if (newHigh >= getHigh()) {
                    high = newHigh;
                } else {
                    high = getLow();
                    setLow(newHigh);
                }
                return this;
            }
        }
    }
}
