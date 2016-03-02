package org.bh.tools.util;

import java.util.ArrayList;
import java.util.Collection;

import static org.bh.tools.util.Do.A.a;

/**
 * A collection of tiny methods with tiny names that {@link Do} tiny things
 *
 * @author Kyli Rouge of Blue Husky Studios
 */
public class Do {

    private Do() {
    }

    /**
     * An organizational subclass grouping string-related operations
     */
    public static class S {

        private S() {
        }

        /**
         * Returns the String value of the given Object, with quotes if it's a {@link CharSequence}
         *
         * @param o the Object to convert to a String with conditional quotes
         *
         * @return the String value of {@code o}, with quotes if it's a {@link CharSequence}
         */
        public static String qs(Object o) {
            if (o instanceof CharSequence) {
                return "\"" + o + '\"';
            }
            return s(o);
        }

        /**
         * Returns the String value of the given Object
         *
         * @param o the Object to convert to a String
         *
         * @return the String value of {@code o}
         */
        public static String s(Object o) {
            return String.valueOf(o);
        }

        /**
         * Returns the String value of the given Object, with special parsing if it's an array. In such a case, the
         * String returned is in the following format: {@code [value1, value2, value3]}
         *
         * @param o the Object to convert to a String with conditional formatting
         *
         * @return the String value of {@code o}, with special parsing if it's an array
         */
        public static String as(Object o) {
            if (o != null && (o.getClass().isArray() || o instanceof Iterable)) {
                Object[] a = a(o);
                StringBuilder ret = new StringBuilder("[");
                if (a.length > 0) {
                    ret.append(qs(a[0]));
                    for (int i = 1; i < a.length; i++) {
                        ret.append(',').append(qs(a[i]));
                    }
                }
                return ret.append(']').toString();
            }
            return s(o);
        }
    }

    /**
     * An organizational subclass grouping array-related operations
     */
    public static class A {

        /**
         * Returns an array version of the given object.
         * <ul>
         * <li>If {@code o} is {@code null}, {@code null} is returned.
         * <li>Else, if {@code o} is an array, it is returned.
         * <li>Else, if {@code o} is a {@link Collection}, its {@link Collection#toArray() toArray()} result is returned.
         * <li>Else, if {@code o} is an {@link Iterable}, its contents are put into an array, and that is returned.
         * <li>Else, an array is returned whose single value is {@code o}.
         * </ul>
         *
         * @param o the object to turn into an array
         *
         * @return {@code o}, as an array.
         */
        public static Object[] a(Object o) {
            if (o == null) {
                return null;
            } else if (o.getClass().isArray()) {
                return (Object[]) o;
            } else if (o instanceof Collection) {
                Collection c = (Collection)o;
                return c.toArray();
            } else if (o instanceof Iterable) {
                Iterable i = (Iterable) o;
                ArrayList<Object> ret = new ArrayList<>();
                for (Object object : i) {
                    ret.add(object);
                }
                return ret.toArray();
            } else {
                return new Object[]{o};
            }
        }
    }

}
