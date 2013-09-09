package bht.tools.misc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * BasicStaticActions, made for BHToolbox, is copyright Blue Husky Programming ©2013 <HR/>
 * A collection of basic {@code static} {@code final} {@link ActionListener}s.
 * 
 * @author Supuhstar of Blue Husky Programming
 * @version 1.0.0
 * @since 2013-05-15
 */
public class BasicStaticActions
{
	public static final ActionListener ACTION_EXIT = new ActionListener() {@Override public void actionPerformed(ActionEvent e){System.exit(0);}};
}
