package bht.tools.misc;

import static bht.tools.util.Do.s;
import java.awt.Component;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import javax.swing.JOptionPane;

/**
 * OptionPaneHandler, made for BHToolbox, is copyright Blue Husky Programming ©2015 GPLv3 <hr/>
 * 
 * @author Kyli of Blue Husky Programming
 * @version 1.0.0
 *		- 2015-02-19 (1.0.0) - Kyli created OptionPaneHandler
 * @since 2015-02-19
 */
public class OptionPaneHandler extends Handler
{
	private Component parent;
	
	@Override
	public void publish(LogRecord record)
	{
		Level l = record.getLevel();
		
		if (l.intValue() < getLevel().intValue())
			return;
		
		String title = l.getName();
		
		int type = JOptionPane.PLAIN_MESSAGE;
		if (l.intValue() >= Level.SEVERE.intValue())
			type = JOptionPane.ERROR_MESSAGE;
		else if (l.intValue() >= Level.WARNING.intValue())
			type = JOptionPane.WARNING_MESSAGE;
		else if (l.intValue() >= Level.INFO.intValue())
			type = JOptionPane.INFORMATION_MESSAGE;
		
		JOptionPane.showMessageDialog(parent, record, title, type);
	}

	/**
	 * @deprecated does nothing.
	 */
	@Override
	public void flush(){}

	/**
	 * @deprecated does nothing.
	 */
	@Override
	public void close() throws SecurityException{}
	
}
