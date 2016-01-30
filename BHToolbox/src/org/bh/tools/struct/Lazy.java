package org.bh.tools.struct;

import java.util.function.Supplier;



/**
 * Lazy, made for BHToolbox 2, is copyright Blue Husky Programming ©2015 BH-1-PS <hr/>
 *
 * @author Kyli of Blue Husky Programming
 * @version 1.0.0
 * - 2015-07-05 (1.0.0) - Kyli created Lazy
 * @param <T> The type of object being supplied
 *
 * @since 2015-07-05
 */
public class Lazy<T> implements Supplier<T> {
    private T value;
    private Supplier<T> supplier;

    /**
     * Creates a new {@link Lazy} with the given {@link Supplier} of the same type {@code T}.
     *
     * @param supplier the supplier which will supply the value in {@link #get()}
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Lazy(Supplier<T> supplier) {
        setSupplier(supplier);
    }

    public void setSupplier(Supplier<T> supplier) {
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier cannot be null");
        }
        this.supplier = supplier;
    }

    /**
     * Returns the value from the supplier's {@link Supplier#get()}.
     *
     * @return the value from the supplier's {@link Supplier#get()}. If that returns {@code null}, not only will
     *         {@code null} be returned, but the supplier's {@link Supplier#get()} will be called again every time this
     *         method is, until a non-{@code null} value is gotten.
     */
    @Override
    public T get() {
        if (value == null) {
            value = supplier.get();
        }
        return value;
    }

}
