package org.bh.tools.evt;



/**
 * ListenerListener, made for BHToolbox 2, is copyright Blue Husky Programming ©2015 BH-1-PS <hr>
 *
 * Listens for listener events.
 *
 * @author Kyli of Blue Husky Programming
 * @version 1.0.0
 * - 2015-10-10 (1.0.0) - Kyli created ListenerListener
 * @since 2015-10-10
 */
public interface ListenerListener {
	/**
	 * Called when a listener has started listening
	 *
	 * @param evt Any meta data about the event
	 */
	public void didStartListening(ListenerEvent evt);

	/**
	 * Called when a listener has stopped listening
	 *
	 * @param evt Any meta data about the event
	 */
	public void didStopListening(ListenerEvent evt);
}
