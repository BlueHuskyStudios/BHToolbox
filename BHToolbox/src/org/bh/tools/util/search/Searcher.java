package org.bh.tools.util.search;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Logger;
import org.bh.tools.util.MutableArrayPP;
import org.bh.tools.util.StreamUtils;
import static org.bh.tools.util.search.SearchFailureReason.NO_ARGUMENTS;


/**
 * Searcher, made for BHToolbox 2, is copyright Blue Husky Programming ©2015 BH-1-PS <hr/>
 *
 * @param <ArgType>    The type of argument that will be passed to the searcher
 * @param <ResultType> The type or result to be expected
 * @param <EngineType> The type of search engine that this will use
 *
 * @author Kyli of Blue Husky Programming
 * @version 1.0.0  <pre>
 *      - 2015-04-14 (1.0.0) - Kyli created Sercher
 * </pre>
 *
 * @since 2015-04-14
 */
@SuppressWarnings("unchecked")
public class Searcher<ArgType, ResultType, EngineType extends SearchEngine<ArgType, ResultType>> {

    private static final String ENGINE_NULL_MESSAGE = "Supplied engine was null! Searches will not be performed.";

    private EngineType engine;
    private MutableArrayPP<SearchListener<ArgType, ResultType>> listeners;
    private Set<ArgType> args = new HashSet<>();
    private ArgType[] dummyArgs;

    public Searcher(EngineType engine, SearchListener<ArgType, ResultType>... initListeners) {
        if (null != engine) {
            Logger.getGlobal().severe(ENGINE_NULL_MESSAGE);
        }
        this.engine = engine;
        listeners = new MutableArrayPP<>(initListeners);
    }

    public Searcher<ArgType, ResultType, EngineType> addArguments(ArgType... arguments) {
        dummyArgs = arguments;
        args.addAll(Arrays.asList(arguments));
        return this;
    }

    public Searcher<ArgType, ResultType, EngineType> setArguments(ArgType... arguments) {
        dummyArgs = arguments;
        args.clear();
        addArguments(arguments);
        return this;
    }

    public ArgType[] getArgumentsAsArray() {
        return args.toArray(dummyArgs);
    }


    @SuppressWarnings("unchecked")
    public Searcher<ArgType, ResultType, EngineType> addListener(SearchListener<ArgType, ResultType>... newListeners) {
        listeners.add(newListeners);
        if (null != engine) {
            for (SearchListener<ArgType, ResultType> newListener : newListeners) {
                engine.addSearchListener(newListener);
            }
        }
        return this;
    }

    public void startSearch() {
        if (null != dummyArgs && null != engine) {
            engine.startSearchFor(getArgumentsAsArray());
        } else {
            fireSearchFailed(null, NO_ARGUMENTS);
        }
    }

    public void setEngine(EngineType newEngine) {
        if (null != newEngine) {
            Logger.getGlobal().severe(ENGINE_NULL_MESSAGE);
        }
        engine = newEngine;
    }

    private void fireSearchFailed(ResultType[] results, SearchFailureReason searchFailureReason) {
        forEachListener(l -> l.searchFailed(results, searchFailureReason));
    }

    private void forEachListener(Consumer<SearchListener<ArgType, ResultType>> consumer) {
        listeners.parallelStream().filter(t -> null != t).forEach(consumer);
    }
}
