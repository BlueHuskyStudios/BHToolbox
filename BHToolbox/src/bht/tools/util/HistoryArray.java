package bht.tools.util;

import bht.tools.comps.event.NavigationEvent;
import bht.tools.comps.event.NavigationEvent.NavigationState;
import static bht.tools.comps.event.NavigationEvent.NavigationState.STATE_GOING_BACKWARD;
import static bht.tools.comps.event.NavigationEvent.NavigationState.STATE_GOING_FORWARD;
import static bht.tools.comps.event.NavigationEvent.NavigationState.STATE_SKIPPING_FORWARD;
import bht.tools.comps.event.NavigationListener;

/**
 * This class was made for back/forward buttons, and can be easily used for such. Other possible uses include undo/redo. As you
 * may guess, at the heart of this class is the <tt>ArrayPP</tt> class, to make management as efficient as possible.
 *
 * @param <T> The type of objects with which this class will be dealing. For instance: for a file browser, you should use
 * <tt>HistoryArray&lt;java.io.File&gt; ha = new HistoryArray&lt;java.io.File&gt;();</tt>
 * @author Supuhstar of Blue Husky Programming
 * @version 1.0.1 (Copyright Blue Husky Studios ©2011)
 */
public class HistoryArray<T> extends ArrayPP<T>
{
	private ArrayPP<NavigationListener> navigationListeners = new ArrayPP<>();
	protected int here;

	/**
	 * Creates a new <tt>HistoryArray</tt> with a blank <tt>ArrayPP</tt> with the first item as the currently observed item.
	 */
	public HistoryArray()
	{
		this(new ArrayPP<T>(), 0);
	}

	/**
	 * Creates a new <tt>HistoryArray</tt> with <tt>initArray</tt> as the basis for the history array.
	 *
	 * @param initArray The array of objects to make into a navigable history array. May be empty or null.
	 */
	@SuppressWarnings("unchecked")
	public HistoryArray(T... initArray)
	{
		this(initArray, initArray.length - 1);
	}

	/**
	 * Creates a new <tt>HistoryArray</tt> with <tt>initArray</tt> as the basis for the history array.
	 *
	 * @param initArray The array of objects to make into a navigable history array. May be empty or null.
	 */
	@SuppressWarnings("unchecked")
	public HistoryArray(ArrayPP<T> initArray)
	{
		this(initArray.toArray());
	}

	/**
	 * Creates a new <tt>HistoryArray</tt> with <tt>initArray</tt> as the basis for the history array.
	 *
	 * @param initArray The array of objects to make into a navigable history array. May be empty or null.
	 * @param current The item that is used as the "Current" item
	 */
	@SuppressWarnings("unchecked")
	public HistoryArray(T[] initArray, int current)
	{
		this(new ArrayPP(initArray), current);
	}

	/**
	 * Creates a new <tt>HistoryArray</tt> with <tt>initArray</tt> as the basis for the history array.
	 *
	 * @param initArray The array of objects to make into a navigable history array. May be empty or null.
	 * @param current The item that is used as the "Current" item
	 */
	@SuppressWarnings("unchecked")
	public HistoryArray(ArrayPP<T> initArray, int current)
	{
		t = initArray.toArray();
		here = current;
	}

	/**
	 * Appends the given item to the end of the array, without affecting anything else.
	 *
	 * @param item the item to make the new end
	 * @return the resulting HistoryArray
	 */
	public HistoryArray<T> append(T item)
	{
		super.add(item);
		return this;
	}

	/**
	 * Trims the array to the current position, appends the given item to the end, and sets the current position to the end (in
	 * that order)
	 *
	 * @param items the item to make the new end
	 * @return the resulting HistoryArray
	 */
	@Override
	public HistoryArray<T> add(T... items)
	{
		NavigationEvent evt =
			new NavigationEvent(
				this,
				here,
				length() + items.length - 2,
				getCurrent(),
				items[items.length - 1],
				STATE_SKIPPING_FORWARD
			)
		;

		for (NavigationListener navigationListener
			 : navigationListeners)
			navigationListener.willNavigate(evt);

		trimTo(here);
		super.add(items);
		here = length() - 1;

		for (NavigationListener navigationListener
			 : navigationListeners)
			navigationListener.didNavigate(evt);
		return this;
	}

	public HistoryArray<T> goNext()
	{
		NavigationEvent evt = new NavigationEvent(this, here, here + 1, getCurrent(), getVirtualNext(), STATE_GOING_FORWARD);

		for (NavigationListener navigationListener
			 : navigationListeners)
			navigationListener.willNavigate(evt);

		if (canGoNext())
			here++;
		else
			throw new IllegalStateException("Already at end");

		for (NavigationListener navigationListener
			 : navigationListeners)
			navigationListener.didNavigate(evt);

		return this;
	}

	public HistoryArray<T> goBack()
	{
		NavigationEvent evt = new NavigationEvent(this, here, here - 1, getCurrent(), getVirtualBack(), STATE_GOING_BACKWARD);

		for (NavigationListener navigationListener
			 : navigationListeners)
			navigationListener.willNavigate(evt);

		if (canGoBack())
			here--;
		else
			throw new IllegalStateException("Already at end");

		for (NavigationListener navigationListener
			 : navigationListeners)
			navigationListener.didNavigate(evt);
		return this;
	}

	public boolean canGoNext()
	{
		return canGoTo(here + 1);
	}

	public boolean canGoBack()
	{
		return canGoTo(here - 1);
	}

	public boolean canGoTo(int index)
	{
		return index >= 0 && index < length();
	}

	public int getCurrentIndex()
	{
		return here;
	}

	public HistoryArray<T> goTo(int loc)
	{
		NavigationEvent evt =
			new NavigationEvent(
					this,
					here,
					loc,
					getCurrent(),
					get(loc),
					loc > here
						? NavigationState.STATE_SKIPPING_FORWARD
						: NavigationState.STATE_SKIPPING_BACKWARD
			);

		for (NavigationListener navigationListener
			 : navigationListeners)
			navigationListener.willNavigate(evt);

		this.here = loc;

		for (NavigationListener navigationListener
			 : navigationListeners)
			navigationListener.didNavigate(evt);
		return this;
	}

	/*@Override
	public T get(int index)
	{
		return super.get(index);
	}*/

	public T getCurrent()
	{
		return isEmpty() ? null : get(here);
	}

	public ArrayPP<T> getBackHist()
	{
		return super.subSet(0, here - 1);
	}

	public ArrayPP<T> getNextHist()
	{
		return super.subSet(here + 1, length() - 1);
	}

	public ArrayPP<T> getFullHist()
	{
		return this;
	}

	/**
	 * Removes everything after {@code index}. Does not destroy the removed items.
	 *
	 * @param index the index of the last item to keep
	 * @return the resulting {@code this}
	 */
	public HistoryArray<T> trimTo(int index)
	{
		return trimTo(index, false);
	}

	/**
	 * Removes everything after {@code index}, destroying them if specified
	 *
	 * @param index the index of the last item to keep
	 * @param destroy if {@code true}, the trimmed items will be destroyed (set to null) before removal
	 * @return the resulting {@code this}
	 */
	public HistoryArray<T> trimTo(int index, boolean destroy)
	{
		if (destroy)
			for (int i = index + 1;
				 i < t.length;
				 i++)
				t[i] = null;
		t = super.subSet(0, index).toArray();
		return this;
	}

	public HistoryArray<T> trimToCurrent()
	{
		return trimTo(here);
	}

	@Override
	public String toString()
	{
		StringBuilder ret = new StringBuilder();
		for (int i = 0,
				j = 0;
			 i < t.length;
			 i++)
			if (t[i] != null)
			{
				j++;
				ret.append(i == here ? "  >" : j + ": ").append(t[i]).append("\r\n");
			}
		return ret.toString();
	}

	public T getFromBackHist(int index)
	{
		return getBackHist().get(index);
	}

	public T getFromNextHist(int index)
	{
		return getNextHist().get(index);
	}

	/**
	 * Returns the value of the item which would be "Current" if the {@link #goBack()} method is called
	 *
	 * @return the value of the item which would be "Current" if the {@link #goBack()} method is called
	 * @since May 26, 2012 for File Browser
	 */
	public T getVirtualBack()
	{
		return get(Math.max(0, getCurrentIndex() - 1));
	}

	/**
	 * Returns the value of the item which would be "Current" if the {@link #goNext()} method is called
	 *
	 * @return the value of the item which would be "Current" if the {@link #goNext()} method is called
	 * @since May 26, 2012 for File Browser
	 */
	public T getVirtualNext()
	{
		return get(Math.min(length() - 1, getCurrentIndex() + 1));
	}

	public void addNavigationListener(NavigationListener navigationListener)
	{
		navigationListeners.add(navigationListener);
	}

	public void removeNavigationListener(NavigationListener navigationListener)
	{
		navigationListeners.remove(navigationListener, true);
	}
}
