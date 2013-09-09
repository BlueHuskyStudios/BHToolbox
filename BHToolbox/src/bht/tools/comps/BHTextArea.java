package bht.tools.comps;

import bht.tools.util.ArrayPP;
import bht.tools.util.Copier;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

/**
 * Merely an extension of the <tt>javax.swing.JTextField</tt> class that adds a special "Tip Text" to the field that only
 * appears (in a different color than normal text) when the text field is both out of focus and empty (empty meaning the text
 * contained within is either <tt>null</tt> or returns <tt>true</tt> when the <tt>.isEmpty()</tt> method is called on it).
 *
 * @author Supuhstar of Blue Husky Programming, © 2011
 * @version 1.1.1
 * @since 1.6_23
 * @see javax.swing.JTextField
 */
public class BHTextArea extends JTextArea
{
	private ArrayPP<ActionListener> actionListeners = new ArrayPP<>();
	private int actionModifiers;
	private boolean losingFocus = false, gainingFocus = false, starting = true;
	private java.awt.Color tipColor = java.awt.SystemColor.textInactiveText, color;
	public static final String DEF_TIP_TEXT = "Type Here";
	private String tipText = DEF_TIP_TEXT, virtualText = null;
	private JMenuItem cutMenuItem, copyMenuItem, pasteMenuItem;

	public BHTextArea()
	{
		this(null, DEF_TIP_TEXT, 0, 0);
	}

	public BHTextArea(String tipText)
	{
		this(null, tipText, 0, 0);
	}

	public BHTextArea(javax.swing.text.Document doc)
	{
		this(doc, DEF_TIP_TEXT, 0, 0);
	}

	public BHTextArea(int rows, int columns)
	{
		this(null, DEF_TIP_TEXT, rows, columns);
	}

	public BHTextArea(String tipText, int rows, int columns)
	{
		this(null, tipText, rows, columns);
	}

	public BHTextArea(javax.swing.text.Document doc, int rows, int columns)
	{
		this(doc, DEF_TIP_TEXT, rows, columns);
	}

	public BHTextArea(javax.swing.text.Document doc, String tipText, int rows, int columns)
	{
		super(doc, tipText, rows, columns);
		this.tipText = tipText;
//    tipColor = java.awt.SystemColor.textInactiveText/*.brighter()*/;
		color = java.awt.SystemColor.textText;
		super.setForeground(tipColor);
		addFocusListener(new java.awt.event.FocusAdapter()
		{
			@Override
			public void focusGained(java.awt.event.FocusEvent evt)
			{
				doFocusGained();
			}

			@Override
			public void focusLost(java.awt.event.FocusEvent evt)
			{
				doFocusLost();
			}
		});
		starting = false;

//    add(new javax.swing.JPopupMenu());
		setComponentPopupMenu(new javax.swing.JPopupMenu());
		cutMenuItem = new javax.swing.JMenuItem("Cut");
		cutMenuItem.addActionListener(new java.awt.event.ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (getSelectedText() == null)
				{
					Copier.copy(getText());
					setText(null);
				}
				else
				{
					Copier.copy(getSelectedText());
					setText(getText().substring(0, getSelectionStart()) + getText().substring(getSelectionEnd()));
				}
			}
		});
		cutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_DOWN_MASK));
		getComponentPopupMenu().add(cutMenuItem);

		copyMenuItem = new javax.swing.JMenuItem("Copy");
		copyMenuItem.addActionListener(new java.awt.event.ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (getSelectedText() == null)
					Copier.copy(getText());
				else
					Copier.searchToCopyIn(getThis());
			}
		});
		copyMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_DOWN_MASK));
		getComponentPopupMenu().add(copyMenuItem);

		pasteMenuItem = new javax.swing.JMenuItem("Paste");
		pasteMenuItem.addActionListener(new java.awt.event.ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
//        if (getSelectedText() == null)
//          setText(getText().substring(0, getCaretPosition()) + Copier.getClipboardString() + getText().substring(getCaretPosition()));
//        else
				setText(getText().substring(0, getSelectionStart()) + Copier.getClipboardString() + getText().substring(getSelectionEnd()));
			}
		});
		pasteMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_DOWN_MASK));
		getComponentPopupMenu().add(pasteMenuItem);



		addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				ActionEvent evt = new ActionEvent(e, ActionEvent.ACTION_PERFORMED, getText(), e.getModifiers());
				if (e.getExtendedKeyCode() == KeyEvent.VK_ENTER)
				{
					if ((actionModifiers & KeyEvent.ALT_DOWN_MASK) != 0 && !e.isAltDown())
						return;
					if ((actionModifiers & KeyEvent.CTRL_DOWN_MASK) != 0 && !e.isControlDown())
						return;
					if ((actionModifiers & KeyEvent.SHIFT_DOWN_MASK) != 0 && !e.isMetaDown())
						return;
					if ((actionModifiers & KeyEvent.SHIFT_DOWN_MASK) != 0 && !e.isShiftDown())
						return;
					for (ActionListener actionListener
						 : actionListeners)
					{
						actionListener.actionPerformed(evt);
					}
				}
			}
		});
	}

	private BHTextArea getThis()
	{
		return this;
	}

	public String getTipText()
	{
		return tipText == null ? "" : tipText;
	}

	public void setTipText(String tipText)
	{
		this.tipText = tipText;
		doFocusLost();
	}

	public void doFocusLost()
	{
		if (hasFocus())
		{
			doFocusGained();
			return;
		}
		losingFocus = true;
		resetTipColor();
		if (super.getDocument() == null)
			setDocument(createDefaultModel());
		super.setText(isTip() ? getTipText() : "<html>" + virtualText + "</html>");
		super.setForeground(isTip() ? tipColor : color);
		losingFocus = false;
	}

	public void doFocusGained()
	{
		if (!hasFocus())
		{
			doFocusLost();
			return;
		}
		gainingFocus = true;
		super.setForeground(color);
		super.setText(isTip() ? "" : super.getText());
		gainingFocus = false;
	}

	public void setTipColor(java.awt.Color c)
	{
		tipColor = c;
		doFocusLost();
	}

	public java.awt.Color getTipColor()
	{
		return tipColor;
	}

	@Override
	public String getText()
	{
		return isTip() ? null : super.getText();
	}

	@Override
	public void setText(String text)
	{
		super.setText(virtualText = text);
		doFocusLost();
	}

	public void resetTipColor()
	{
		tipColor = java.awt.SystemColor.textInactiveText/*.brighter()*/;
	}

	@Override
	public void setForeground(java.awt.Color c)
	{
		color = c;
		doFocusLost();
	}

//  @Override
//  public java.awt.Color getForeground()
//  {
//    return color;
//  }
	private boolean isTip()
	{
		try
		{
			return (virtualText == null || virtualText.isEmpty()) && (super.getText().isEmpty());
		}
		catch (NullPointerException ex)
		{
			return true;
		}
	}

	public void clear()
	{
		setText(null);
	}

	public boolean isEmpty()
	{
		return getText().isEmpty();
	}

	@Override
	public void setEditable(boolean b)
	{
		super.setEditable(b);
		if (cutMenuItem != null)
			cutMenuItem.setEnabled(b);
		if (pasteMenuItem != null)
			pasteMenuItem.setEnabled(b);
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		if (cutMenuItem != null)
			cutMenuItem.setEnabled(enabled);
		if (pasteMenuItem != null)
			pasteMenuItem.setEnabled(enabled);
	}

	/**
	 * Adds an {@link ActionListener} to the list of {@link ActionListener}s that are called when the user performs an action.
	 * The exact gesture that triggers this action is defined by {@link #setActionModifiers}
	 *
	 * @param newActionListener
	 * @since 2013-07-03 (1.2.0)
	 * @version 1.0.0
	 */
	public void addActionListener(ActionListener newActionListener)
	{
		actionListeners.add(newActionListener);
	}

	public void removeActionListener(ActionListener newActionListener, boolean removeAll)
	{
		actionListeners.remove(newActionListener, removeAll);
	}

	/**
	 * Sets the new action modifiers. This is based on the following set of constants provided in {@link KeyEvent}:
	 * <UL>
	 * <LI>{@link KeyEvent#ALT_DOWN_MASK}</LI>
	 * <LI>{@link KeyEvent#CTRL_DOWN_MASK}</LI>
	 * <LI>{@link KeyEvent#META_DOWN_MASK}</LI>
	 * <LI>{@link KeyEvent#SHIFT_DOWN_MASK}</LI>
	 * </UL>
	 * To combine them, use the bitwise OR
	 *
	 * @param newModifiers the {@code int} representing the combination of keys that must be pressed in conjunction with the
	 * Enter or Return key to represent an action
	 * @return {@code this}
	 * @since 2013-07-03 (1.2.0)
	 */
	public BHTextArea setActionModifiers(int newModifiers)
	{
		actionModifiers = newModifiers;
		return this;
	}

	public BHTextArea setActionModifiers(boolean alt, boolean ctrl, boolean meta, boolean shift)
	{
		int mod = 0;
		if (alt)
			mod |= KeyEvent.ALT_DOWN_MASK;
		if (ctrl)
			mod |= KeyEvent.CTRL_DOWN_MASK;
		if (meta)
			mod |= KeyEvent.META_DOWN_MASK;
		if (shift)
			mod |= KeyEvent.SHIFT_DOWN_MASK;
		return setActionModifiers((alt ? KeyEvent.ALT_DOWN_MASK : 0)
								  | (ctrl ? KeyEvent.CTRL_DOWN_MASK : 0)
								  | (meta ? KeyEvent.META_DOWN_MASK : 0)
								  | (shift ? KeyEvent.SHIFT_DOWN_MASK : 0));
	}
}
