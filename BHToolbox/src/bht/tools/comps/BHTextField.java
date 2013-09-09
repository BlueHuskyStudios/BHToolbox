/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bht.tools.comps;

import bht.tools.util.Copier;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;

/**
 * Merely an extension of the <tt>javax.swing.JTextField</tt> class that adds a special "Tip Text" to the field that only
 * appears (in a different color than normal text) when the text field is both out of focus and empty (empty meaning the text
 * contained within is either <tt>null</tt> or returns <tt>true</tt> when the <tt>.isEmpty()</tt> method is called on it).
 * @author Supuhstar of Blue Husky Programming, © 2011
 * @version 1.0.0
 * @since 1.6_23
 * @see javax.swing.JTextField
 */
public class BHTextField extends JTextField
{
  private boolean losingFocus = false, gainingFocus = false, starting = true;
  private java.awt.Color tipColor = java.awt.SystemColor.textInactiveText, color;
  public static final String DEF_TIP_TEXT = "Type Here";
  private String tipText = DEF_TIP_TEXT;
  private javax.swing.JMenuItem cutMI = new javax.swing.JMenuItem("Cut"),
                                copyMI = new javax.swing.JMenuItem("Copy"),
                                pasteMI = new javax.swing.JMenuItem("Paste");
    
  public BHTextField()
  {
    this(null, DEF_TIP_TEXT, 0);
  }

  public BHTextField(String tipText)
  {
    this(null, tipText, 0);
  }

  public BHTextField(int columns)
  {
    this(null, DEF_TIP_TEXT, columns);
  }

  public BHTextField(String tipText, int columns)
  {
    this(null, tipText, columns);
  }

  public BHTextField(javax.swing.text.Document doc, String tipText, int columns)
  {
    super(doc, tipText, columns);
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
    addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyTyped(KeyEvent e)
      {
        Component c;
        if ((c = getParent()) != null)
          c.doLayout();
      }
    });
    starting = false;
    
//    add(new javax.swing.JPopupMenu());
    setComponentPopupMenu(new javax.swing.JPopupMenu());
    cutMI.addActionListener(new java.awt.event.ActionListener() {

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
    cutMI.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_DOWN_MASK));
    getComponentPopupMenu().add(cutMI);
    
    copyMI.addActionListener(new java.awt.event.ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e)
      {
        if (getSelectedText() == null)
          Copier.copy(getText());
        else
          Copier.searchToCopyIn(getThis());
      }
    });
    copyMI.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_DOWN_MASK));
    getComponentPopupMenu().add(copyMI);
    
    pasteMI.addActionListener(new java.awt.event.ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e)
      {
//        if (getSelectedText() == null)
//          setText(getText().substring(0, getCaretPosition()) + Copier.getClipboardString() + getText().substring(getCaretPosition()));
//        else
          setText(getText().substring(0, getSelectionStart()) + Copier.getClipboardString() + getText().substring(getSelectionEnd()));
      }
    });
    pasteMI.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_DOWN_MASK));
    getComponentPopupMenu().add(pasteMI);
  }
  
  private BHTextField getThis()
  {
    return this;
  }

  public String getTipText()
  {
    return tipText == null ? "" : tipText;
  }
  public void setTipText(String tipText)
  {
    String s = getText();
    this.tipText = tipText;
    setText(s);
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
    super.setText(isTip() ? getTipText() : super.getText());
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
    return isTip() ? "" : super.getText();
  }
  
  @Override
  public void setText(String text)
  {
    super.setText(text);
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
      return super.getText().equals(tipText) || super.getText().isEmpty();
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
  public void setEditable(final boolean b)
  {
    super.setEditable(b);
    new Thread(new Runnable()
    {
      @Override
      public void run()
      {
        for (short s=0; cutMI == null; s++) if (s == Short.MAX_VALUE) return;
        cutMI.setEnabled(b);
        for (short s=0; pasteMI == null; s++) if (s == Short.MAX_VALUE) return;
        pasteMI.setEnabled(b);
      }
    },"Waiter").start();
  }
}
