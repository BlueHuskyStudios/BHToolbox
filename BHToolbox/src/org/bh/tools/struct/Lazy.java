/*
 * Lazy, made for BHToolbox, is copyright Blue Husky Programming ©2015 BH-1-PS
 */
package org.bh.tools.struct;

import java.util.function.Supplier;

/**
 * Lazy, made for BHToolbox 2, is copyright Blue Husky Programming ©2015 BH-1-PS <hr>
 * Lazily instantiates an object when and only when it's needed. Also allows the object to be temporarily nullified.
 *
 * @param <T> The type of object this will maintain.
 *
 * @author Kyli of Blue Husky Programming
 * @since 2015-08-07
 */
public class Lazy<T> implements Supplier<T> {

    private T value;
    private Supplier<T> supplier;

    /**
     * Creates a new lazily-instantiated object, whose value will be supplied by the given {@link Supplier} the first
     * time {@link #get() get()} is called. Creates a new {@link Lazy} with the given {@link Supplier} of the same type
     * {@code T}.
     *
     * @param supplier the non-null supplier which will provide the value when it's needed.
     *
     * @version 1.0.0
     * @since 2015-08-07 (1.0.0)
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Lazy(Supplier<T> supplier) {
        setSupplier(supplier);
    }

    /**
     * Sets the supplier which will supply the value when needed.
     *
     * @param supplier
     */
    public void setSupplier(Supplier<T> supplier) {
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier cannot be null");
        }
        this.supplier = supplier;
    }

    /**
     * Returns the value lazily. If the supplier has never had its {@link Supplier#get() get()} method called, it is
     * used exactly once to find the value and cache it. in subsequent calls, the cached value is returned.
     *
     * @return the value in the given supplier's {@link Supplier#get() get()}
     *
     * @version 1.0.0
     * @since 2015-08-07 (1.0.0)
     */
    @Override
    public T get() {
        if (value == null) {
            if (supplier == null) {
                return null;
            }
            value = supplier.get();
        }
        return value;
    }

    /**
     * Clears the value from memory. Next time {@link #get() get()} is called, a brand new value will be returned.
     *
     * @version 1.0.0
     * @since 2015-08-07 (1.0.0)
     */
    public void clear() {
        value = null;
    }

    /**
     * Determines whether the value is currently set. If this returns {@code false}, then a brand new value will be
     * returned next time {@link #get() get()} is called.
     *
     * @return {@code true} iff the value is currently set.
     *
     * @version 1.0.0
     * @since 2015-08-07 (1.0.0)
     */
    public boolean isSet() {
        return value != null;
    }
}
