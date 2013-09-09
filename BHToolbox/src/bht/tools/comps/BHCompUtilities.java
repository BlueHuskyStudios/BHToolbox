package bht.tools.comps;

import bht.tools.fx.Colors;
import bht.tools.fx.CompAction;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Window;
import javax.swing.JFrame;

/**
 *
 * @author Supuhstar
 * @version 1.2.0
 * @see BHComponent
 */
public class BHCompUtilities
{
	public static final Font DEFAULT_FONT;
	static
	{
		javax.swing.JFrame temp = new JFrame();
		Graphics g = temp.getGraphics();
		Font f = null;
		if (g != null)
			f = g.getFont();
		if (f == null)
			f = new Font(Font.SANS_SERIF, Font.PLAIN, 11);
		DEFAULT_FONT = f;
	}
	
	/**
	 * Returns the <tt>java.awt.Window</tt> that owns the provided <tt>java.awt.Component</tt>, or <tt>null</tt> if none does
	 *
	 * @param comp the component whose parent <tt>java.awt.Window</tt> is to be found
	 * @return the <tt>java.awt.Window</tt> that owns the provided <tt>java.awt.Component</tt>, or <tt>null</tt> if none does
	 */
	public static Window getParentWindowOf(java.awt.Component comp)
	{
		return comp == null ? null : (comp instanceof Window ? (Window) comp : getParentWindowOf(comp.getParent()));
	}

	/**
	 * If the provided component is in a <tt>BHFrame</tt>, returns the result of that frame's <tt>.getColors()</tt> method, else
	 * if it is a <tt>BHCompUtilities</tt>, <tt>BHFrame</tt>, returns it's private Colors object, else returns a new
	 * <tt>Colors</tt> object
	 *
	 * @param c The component whose <tt>Colors</tt> object will be gotten
	 * @return the <tt>Colors</tt> object as described above
	 */
	public static Colors getColorsFor(Component c)
	{
		if (c == null)
			return new Colors();
		if (c instanceof BHFrame)
			return ((BHFrame) c).getColors();
		if (c instanceof BHComponent)
			return ((BHComponent) c).getColors();

		Component comp = c.getParent();
		while (comp != null)
		{
			if (comp instanceof BHFrame)
				break;
			comp = c.getParent();
		}
		if (comp == null)
			return new Colors();
		return ((BHFrame) comp).getColors();
	}

	public static CompAction getCompActionFor(Component c)
	{
		if (c instanceof BHComponent)
			return ((BHComponent) c).getCompAction();
		if (c == null)
			return new CompAction();

		Component comp = c.getParent();
		while (comp != null)
		{
			if (comp instanceof BHFrame)
				break;
			comp = c.getParent();
		}
		if (comp == null)
			return new CompAction();
		return ((BHFrame) comp).getCompAction();
	}

	/**
	 * Sets whether the OSX menu bar is used. If the platform is not OS X, nothing happens.
	 * Future implementations may allow for the Ubuntu Unity menu bar to be used.
	 *
	 * @param uses if true, then this application uses the OS X menu bar.
	 * @param name The name of the application. If it is null, the name is not changed.
	 * @version 1.2.0
	 */
	public static void setUsesOSMenuBar(boolean uses, CharSequence name)
	{
		System.setProperty("apple.laf.useScreenMenuBar", Boolean.toString(uses));
		if (name != null)
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", name.toString());

		//TODO: Unity menu bar, too
	}
}
