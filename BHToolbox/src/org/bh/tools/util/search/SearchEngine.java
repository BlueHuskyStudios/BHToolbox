package org.bh.tools.util.search;

/**
 * SearchEngine, made for BHToolbox 2, is copyright Blue Husky Programming ©2015 BH-1-PS <hr/>
 * 
 * @param <ArgType> The type of argument being passed to the search engine
 * @param <ResultType> The type of result expected from the search engine
 * 
 * @author Kyli of Blue Husky Programming
 * @version 1.0.0
 * <pre>
 *		- 2015-04-14 (1.0.0) - Kyli created SearchEngine
 * </pre>
 * @since 2015-04-14
 */
public interface SearchEngine<ArgType, ResultType>
{
    public void startSearchFor(ArgType... arguments);
    public void addSearchListener(SearchListener<ArgType, ResultType> listener);
}
