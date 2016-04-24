package bht.tools.util.search;

import bht.tools.util.ArrayPP;
import bht.tools.util.StringPP;



/**
 * A default {@link Needle}, with all methods and constructors already implemented
 *
 * @author Supuhstar of Blue Husky Programming
 * @version 2.0.0
 * - 2.0.0 (2015-02-04) - Kyli renamed DefSearchable to DefDisplayableNeedle
 * @since 1.6_23
 */
public class DefNeedle implements Needle {
    private ArrayPP<Keyword> keys = new ArrayPP<>();
    private StringPP title;

    public DefNeedle() {
        this("", new ArrayPP<>());
    }

    public DefNeedle(ArrayPP<Keyword> keywords) {
        this("", keywords);
    }

    public DefNeedle(CharSequence title) {
        this(title, new ArrayPP<>(new Keyword(title, 1.0)));
    }

    public DefNeedle(CharSequence title, ArrayPP<Keyword> keywords) {
        this.title = new StringPP(title);
        this.keys = keywords;
    }

    @Override
    public ArrayPP<Keyword> getKeywords() {
        return keys;
    }

    /**
     * Calculates and returns a match strength between the two <t>DefDisplayableNeedle</tt>s depending on whether...
     * <ol>
     * <li>They are equal (tested with <tt>==</tt> and <tt>.equals</tt>)</li>
     * <li>They have any or all matching keywords</li>
     * <li>They have equal or similar titles</li>
     * <li>They have equal displays</li>
     * </ol>
     *
     * @param otherSearchable the other <tt>DefDisplayableNeedle</tt> to which this one will be compared
     *
     * @return the <tt>double</tt> representing the strength of the match between the two items
     */
    @Override
    public double getMatchStrength(Needle otherSearchable) {
        if (otherSearchable == this || otherSearchable.equals(this)) {
            return 1;
        }

        double strength = 0.5;

        if (title.equalsIgnoreCase(otherSearchable.getSearchTitle()) || new StringPP(otherSearchable.getSearchTitle())
                .containsIgnoreCase(title) || title.containsIgnoreCase(otherSearchable.getSearchTitle())) {
            strength = Math.pow(strength, 0.25);
        }

        if (otherSearchable.getKeywords().containsAll(getKeywords().toArray())) {
            strength = Math.pow(strength, 0.05);
        }
        else if (otherSearchable.getKeywords().containsAny(getKeywords().toArray())) {
            for (Keyword k : getKeywords()) {
                if (otherSearchable.getKeywords().contains(k)) {
                    strength = Math.pow(strength, 0.25 * k.getRelevance());
                }
            }
        }
        else {
            strength = Math.pow(strength, 2);
        }

        return strength;
    }

    @Override
    public String toString() {
        return title.toString();
    }

    @Override
    public DefNeedle setSearchTitle(CharSequence newTitle) {
        title = new StringPP(newTitle);
        return this;
    }

    @Override
    public CharSequence getSearchTitle() {
        return title.toString();
    }

    /**
     * The same as {@code (int)(getMatchStrength(otherSearchable) * 100)}
     *
     * @param otherNeedle the other {@link Needle} to be compared
     *
     * @return {@code (int)(getMatchStrength(otherSearchable) * 100)}
     */
    @Override
    public long compareTo64(Needle otherNeedle) {
        return (long) (getMatchStrength(otherNeedle) * 100);
    }

    @Override
    public int compareTo(Needle o) {
        return (int) compareTo64(o);
    }
}
