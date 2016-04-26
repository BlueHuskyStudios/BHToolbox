package org.bh.tools.util.search;

/**
 * SearchListener, made for BHToolbox 2, is copyright Blue Husky Programming ©2015 BH-1-PS <hr/>
 *
 * @author Kyli of Blue Husky Programming
 * @version 1.0.0 - 2015-04-14 (1.0.0) - Kyli created SearchListener
 * @since 2015-04-14
 */
public interface SearchListener<ArgType, ResultType> {

    /**
     * Indicates that a search has begun with the given arguments. You may return {@code false} if you think the search
     * should not be performed, and the search engine can opt to stop due to that.
     *
     * @param args The arguments that will be used. Modifications of this array may be ignored. This is guaranteed to
     *             never be {@code null}.
     * @return {@code false} iff the listener thinks the search should NOT continue.
     */
    public boolean searchStarted(ArgType[] args);

    /**
     * Indicates that a search has completed, and these are the results
     *
     * @param results the results of the search. This is guaranteed to never be {@code null}.
     */
    public void searchComplete(ResultType[] results);

    /**
     * Indicates that the search was cancelled before it completed. This is often not a bad thing, and may be expected,
     * so implementing listeners may not want to notify the user that the search was cancelled.
     */
    public void searchCancelled();

    /**
     * Indicates that the search could not be completed as desired.
     *
     * @param anyResults    All the results gathered up until the search failed, if any.
     * @param failureReason The reason the search failed.
     */
    public void searchFailed(ResultType[] anyResults, SearchFailureReason failureReason);
}
