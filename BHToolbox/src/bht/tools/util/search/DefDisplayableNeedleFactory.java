package bht.tools.util.search;

import bht.tools.util.ArrayPP;
import bht.tools.util.search.Needle.Keyword;



public class DefDisplayableNeedleFactory
        extends DisplayableNeedleFactory<DefNeedle, DefDisplayableNeedle<DefNeedle>> {

    @Override
    public DefDisplayableNeedle<DefNeedle> makeFromFactory(DefNeedle basis) {
        return new DefDisplayableNeedle<>(basis);
    }

    @Override
    public DefDisplayableNeedle<DefNeedle> makeFromFactory() {
        return new DefDisplayableNeedle<>();
    }

    @Override
    public DefDisplayableNeedle<DefNeedle> makeFromFactory(CharSequence cs, Iterable<Keyword> ks) {
        return makeFromFactory(new DefNeedle(cs, ArrayPP.valueOf(ks)));
    }
}
