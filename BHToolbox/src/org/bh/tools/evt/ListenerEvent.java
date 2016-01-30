package org.bh.tools.evt;

import java.util.EventObject;



/**
 * ListenerEvent, made for BHToolbox 2, is copyright Blue Husky Programming ©2015 BH-1-PS <hr/>
 *
 * @author Kyli of Blue Husky Programming
 * @version 1.0.0
 * - 2015-10-10 (1.0.0) - Kyli created ListenerEvent
 * @since 2015-10-10
 */
public class ListenerEvent extends EventObject {
	private static final long serialVersionUID = 1_000_0000L;

	public ListenerEvent(Object source) {
		super(source);
	}

}
