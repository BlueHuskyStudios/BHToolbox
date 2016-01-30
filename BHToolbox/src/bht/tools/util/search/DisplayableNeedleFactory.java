package bht.tools.util.search;

import bht.tools.misc.FactoryDelegate;
import bht.tools.util.search.Needle.Keyword;


public abstract class DisplayableNeedleFactory
<N extends Needle, D extends DisplayableNeedle<N>>
implements FactoryDelegate<N, D>
{
    public abstract D makeFromFactory(CharSequence cs, Iterable<Keyword> ks);
}
