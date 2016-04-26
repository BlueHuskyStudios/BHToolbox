package org.bh.tools.util;

import java.util.function.Predicate;

/**
 * StreamUtils, made for BHToolbox 2, is copyright Blue Husky Programming ©2016 BH-1-PS <hr/>
 *
 * @author Kyli of Blue Husky Programming
 * @since 2016-04-25
 */
public class StreamUtils {

    /**
     * A filter that won't let through any {@code null} values.
     */
    public static final Predicate NONNULL_FILTER = t -> null != t;
}
