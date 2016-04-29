package org.bh.tools.util.search;

/**
 * IteratorSoruce, made for BHToolbox 2, is copyright Blue Husky Programming ©2015 BH-1-PS <hr>
 * 
 * @param <T> The type of object that will be iterated through by the source iterator
 * 
 * @author Kyli of Blue Husky Programming
 * @version 1.0.0
 *		- 2015-04-14 (1.0.0) - Kyli created IteratorSoruce
 * @since 2015-04-14
 */
public class IteratorSoruce<T> implements SourceSearchArgument<Iterable<T>>
{
    Iterable<T> source;

    public IteratorSoruce(Iterable<T> initSource) {
        source = initSource;
    }

    @Override
    public Iterable<T> getSource() {
        return source;
    }
}
