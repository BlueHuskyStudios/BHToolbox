package org.bh.tools.util.search;

import org.bh.tools.util.MutableArrayPP;



/**
 * Sercher, made for BHToolbox 2, is copyright Blue Husky Programming ©2015 BH-1-PS <hr/>
 *
 * @param <ArgType>    The type of argument that will be passed to the searcher
 * @param <ResultType> The type or result to be expected
 * @param <EngineType> The type of search engine that this will use
 *
 * @author Kyli of Blue Husky Programming
 * @version 1.0.0
 * <pre>
 *      - 2015-04-14 (1.0.0) - Kyli created Sercher
 * </pre>
 * @since 2015-04-14
 */
@SuppressWarnings("unchecked")
public class Searcher<ArgType, ResultType, EngineType extends SearchEngine<ArgType, ResultType>> {
    private EngineType engine;
    private MutableArrayPP<SearchListener<ArgType, ResultType>> listeners;
    private MutableArrayPP<ArgType> args;

    public Searcher(EngineType engine, SearchListener<ArgType, ResultType>... initListeners) {
        this.engine = engine;
        listeners = new MutableArrayPP<>(initListeners);
    }

    public Searcher<ArgType, ResultType, EngineType> addArgument(ArgType argument) {
        args.add(argument);
        return this;
    }
    
    public Searcher<ArgType, ResultType, EngineType> setArguments(ArgType[] arguments) {
        args.clear().add(arguments);
        return this;
    }
    
    public ArgType[] getArguments() {
        return args.toArray();
    }
    
    @SuppressWarnings("unchecked")
    public Searcher<ArgType, ResultType, EngineType> addListener(SearchListener<ArgType, ResultType>... newListener) {
        listeners.add(newListener);
        return this;
    }

    public void startSearch() {
        engine.startSearchFor(args.toArray());
    }
}
