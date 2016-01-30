package bht.tools.util.search;

import bht.tools.misc.Factory;
import bht.tools.util.search.Needle.Keyword;

/**
 * NeedleFactory, made for BHToolbox 2, is copyright Blue Husky Programming ©2015 BH-1-PS <hr/>
 * 
 * Allows for the creation of {@link Needle}s by automated processes.
 * 
 * @param <N> The type of {@link Needle} to create
 * 
 * @author Kyli of Blue Husky Programming
 * @version 1.0.0
 *		- 2015-04-01 (1.0.0) - Kyli created NeedleFactory
 * @since 2015-04-01
 */
@SuppressWarnings("ClassMayBeInterface")
public abstract class NeedleFactory<N extends Needle> implements Factory<N>
{
    /**
     * Creates a {@link Needle} of type {@code N} with the given search terms
     * @param rawSearch the search as it was originally typed
     * @param tokens the keywords within the raw search
     * @return a new {@link Needle} of type {@code N}
     */
    public abstract N makeFromFactory(CharSequence rawSearch, Iterable<Keyword> tokens);
}
