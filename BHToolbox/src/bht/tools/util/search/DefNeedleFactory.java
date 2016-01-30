package bht.tools.util.search;

import bht.tools.util.ArrayPP;
import bht.tools.util.search.Needle.Keyword;



public class DefNeedleFactory extends NeedleFactory<DefNeedle> {

    @Override
    public DefNeedle makeFromFactory(CharSequence cs, Iterable<Keyword> ks) {
        return new DefNeedle(cs, ArrayPP.valueOf(ks));
    }

    @Override
    public DefNeedle makeFromFactory() {
        return new DefNeedle();
    }

}
