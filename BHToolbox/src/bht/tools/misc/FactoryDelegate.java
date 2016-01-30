package bht.tools.misc;



/**
 * FactoryDelegate, made for BHToolbox, is copyright Blue Husky Programming ©2015 BH-1-PS <hr/>
 *
 * @author Kyli of Blue Husky Programming
 * @version 1.0.0
 * - 2015-03-01 (1.0.0) - Kyli created FactoryDelegate
 * @param <From> The type which can be used to make new objects of type {@code To}
 * @param <To>   The type of object this factory makes.
 *
 * @since 2015-03-01
 */
public interface FactoryDelegate<From, To> extends Factory<To> {
    /**
     * Returns a {@code To} version of {@code basis}
     *
     * @param basis the basis from which the returned object will be made
     *
     * @return a representation of {@code basis}, but of type {@code To}
     */
    public To makeFromFactory(From basis);

    /**
     * Returns {@link #makeFromFactory(Object) makeFromFactory(null)}.
     *
     * @return {@link #makeFromFactory(Object) makeFromFactory(null)}
     *
     * @deprecated Please use {@link #makeFromFactory(Object) makeFromFactort(From basis)} instead.
     */
    @Override
    @Deprecated
    public default To makeFromFactory() {
        return makeFromFactory(null);
    }
}
