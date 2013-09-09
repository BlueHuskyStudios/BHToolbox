/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bht.tools.comps;

import javax.swing.JPasswordField;

/**
 * Merely an extension of the <tt>javax.swing.JPasswordField</tt> class that displays a special "Tip Text" in the field that
 * only appears (in a different color than normal text) when the text field is both out of focus and empty (empty meaning the
 * text contained within is either <tt>null</tt> or returns <tt>true</tt> when the <tt>.isEmpty()</tt> method is called on it).
 * @author Supuhstar of Blue Husky Programming, © 2011
 * @version 1.0.0
 * @since 1.6_23
 * @see javax.swing.JPasswordField
 */
public class BHPasswordField extends JPasswordField
{
  private char echoChar = new javax.swing.JPasswordField().getEchoChar();
  private boolean losingFocus = false, gainingFocus = false;
  private java.awt.Color tipColor, color;
  private String tipText = "Password";
  public static final String DEF_TIP_TEXT = "Type Here";

  public BHPasswordField()
  {
    this(null, DEF_TIP_TEXT, 0);
  }

  public BHPasswordField(String tipText)
  {
    this(null, tipText, 0);
  }

  public BHPasswordField(int columns)
  {
    this(null, DEF_TIP_TEXT, columns);
  }

  public BHPasswordField(String tipText, int columns)
  {
    this(null, tipText, columns);
  }

  public BHPasswordField(javax.swing.text.Document doc, String tipText, int columns)
  {
    super(doc, tipText, columns);
    this.tipText = tipText;
    tipColor = java.awt.SystemColor.textInactiveText/*.brighter()*/;
    color = java.awt.SystemColor.textText;
    setForeground(tipColor);
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
    doFocusLost();
  }

  public String getTipText()
  {
    return tipText;
  }
  
  public void setTipText(String tipText)
  {
    this.tipText = tipText;
    setText("");
    doFocusLost();
  }

  public void doFocusLost()
  {
    losingFocus = true;
    resetTipColor();
    setForeground(getText() == null || getText().isEmpty() ? tipColor : color);
    super.setText(super.getText() == null || super.getText().isEmpty() || isTip() ? getTipText() : super.getText());
    super.setEchoChar(isTip() ? 0 : echoChar);
    losingFocus = false;
  }

  public void doFocusGained()
  {
    gainingFocus = true;
    super.setEchoChar(echoChar);
    setForeground(color);
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
  
  /**
   * @deprecated For security reasons, use <tt>getPassword()</tt>, instead
   */
  @Override
  public String getText()
  {
    return !isTip() ? super.getText() : "";
  }

  @Override
  public char[] getPassword()
  {
    return isTip() ? new char[]{} : super.getPassword();
  }
  
  @Override
  public void setText(String text)
  {
    super.setText(text);
    doFocusLost();
  }

  private void resetTipColor()
  {
    tipColor = java.awt.SystemColor.textInactiveText/*.brighter()*/;
  }

  @Override
  public char getEchoChar()
  {
    return isTip() ? echoChar : super.getEchoChar();
  }

  @Override
  public void setEchoChar(char c)
  {
    echoChar = c;
  }

  private boolean isTip()
  {
    return super.getText() != null && super.getText().equals(tipText);
  }
}
