package org.bh.tools.math;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * Range, made for English Learner, is copyright BHStudios ©2016 BH-1-PS <hr>
 * I wanted a range, and this seems like a good way to do that.
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
     * A mutable version of {@link Range}
     */
    public static interface MutableRange<T extends Number> extends Range<T> {

        /**
         * Changes the lowest value of this range. If {@code newLow} is higher than {@link #getHigh() the old high},
         * then this acts like {@link #setLow(Number) setLow(newLow)}{@link #setHigh(Number) .setHigh(newLow)}. Imagine
         * this:
         * <pre>
         * MutableRange r = new MutableRange(3, 6);
         * 0  1  2 [3  4  5  6] 7  8  9
         *          ^--------^
         *
         * r.setLow(4);
         * 0  1  2  3 [4  5  6] 7  8  9
         *             ^-----^
         *
         * r.setLow(8);
         * 0  1  2  3  4  5  6  7 [8] 9
         *                         ^
         * </pre>
         *
         * @param newLow The new lowest value in this range. {@code null} is interpreted as {@code 0}.
         * @return {@code this}
         */
        public MutableRange<T> setLow(T newLow);

        /**
         * Changes the highest value of this range. If {@code newHigh} is lower than {@link #getLow() the old low}, then
         * this acts like {@link #setHigh(Number) setHigh(newHigh)}{@link #setLow(Number) .setLow(newHigh)}. Imagine
         * this:
         * <pre>
         * MutableRange r = new MutableRange(3, 6);
         * 0  1  2 [3  4  5  6] 7  8  9
         *          ^--------^
         *
         * r.setHigh(5);
         * 0  1  2 [3  4  5] 6  7  8  9
         *          ^-----^
         *
         * r.setHigh(1);
         * 0 [1] 2  3  4  5  6  7  8  9
         *    ^
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

        protected Long low, high;

        /**
         * Creates a new IntegerRange with the given values
         *
         * @param initLow  The initial low value of the range
         * @param initHigh The initial high value of the range
         */
        public IntegerRange(Long initLow, Long initHigh) {
            low = initLow;
            high = initHigh;
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
            public MutableIntegerRange(Long initLow, Long initHigh) {
                super(initLow, initHigh);
            }

            @Override
            public MutableIntegerRange setLow(Long newLow) {
                if (null == newLow) {
                    newLow = 0l;
                }
                low = newLow;
                if (newLow > getHigh()) {
                    setHigh(newLow);
                }
                return this;
            }

            @Override
            public MutableIntegerRange setHigh(Long newHigh) {
                if (null == newHigh) {
                    newHigh = 0l;
                }
                high = newHigh;
                if (newHigh < getLow()) {
                    setLow(newHigh);
                }
                return this;
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
                            return MutableIntegerRange.this.contains(((Number) o).longValue());
                        }
                        return false;
                    }
                };
            }

            /**
             * @return A set of all integers between {@link #getLow() low} and {@link #getHigh() high}, inclusive, as
             *         32-bit {@code int}s. Keep in mind that this may cause over- and under-flows.
             */
            public Set<Integer> toInt32Set() {
                return new AbstractSet<Integer>() {
                    @Override
                    public Iterator<Integer> iterator() {
                        return new Iterator<Integer>() {
                            int latest = 0;

                            @Override
                            public boolean hasNext() {
                                return latest < getLength();
                            }

                            @Override
                            public Integer next() {
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
                            return MutableIntegerRange.this.contains(((Number) o).longValue());
                        }
                        return false;
                    }
                };
            }
        }
    }

    /**
     * An implementation of {@link Range} which uses floating-point numbers.
     */
    public static class FloatingPointRange implements Range<Double> {

        protected Double low, high;

        /**
         * Creates a new FloatingPointRange with the given values
         *
         * @param initLow  The initial low value of the range
         * @param initHigh The initial high value of the range
         */
        public FloatingPointRange(Double initLow, Double initHigh) {
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
         * A mutable version of {@link FloatingPointRange}.
         */
        public static class MutableFloatingPointRange extends FloatingPointRange implements MutableRange<Double> {

            /**
             * Creates a new MutableFloatingPointRange with the given values
             *
             * @param initLow  The initial low value of the range
             * @param initHigh The initial high value of the range
             */
            public MutableFloatingPointRange(Double initLow, Double initHigh) {
                super(initLow, initHigh);
            }

            @Override
            public MutableFloatingPointRange setLow(Double newLow) {
                if (null == newLow) {
                    newLow = 0d;
                }
                low = newLow;
                if (newLow > getHigh()) {
                    setHigh(newLow);
                }
                return this;
            }

            @Override
            public MutableFloatingPointRange setHigh(Double newHigh) {
                if (null == newHigh) {
                    newHigh = 0d;
                }
                high = newHigh;
                if (newHigh < getLow()) {
                    setLow(newHigh);
                }
                return this;
            }
        }
    }
}
