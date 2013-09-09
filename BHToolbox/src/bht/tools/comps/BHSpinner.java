/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bht.tools.comps;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * <strong>Copyright Blue Husky Programming, © 2011</strong><br/>
 * A class that extends the capabilities of a JSpinner, so that it can be controlled by the mouse wheel. Also provided are
 * convenience methods for return values.
 * @param <T> the type of value that this spinner is handling. Only used for <tt>getParsedValue()</tt>.
 * @author Supuhstar of Blue Husky Programming
 * @version 1.0.0
 */
public class BHSpinner<T> extends JSpinner
{
  private static final long serialVersionUID = 1L;
  private final String DEF_TOOL_TIP = "Use your mouse wheel to scroll through values.";
  private String tipText = "";
  
  public BHSpinner()
  {
    this(new SpinnerNumberModel());
  }

  /**
   * Constructs a complete spinner with pair of next/previous buttons and an editor for the <code>SpinnerModel</code>.
   * @param model the <tt>javax.swing.SpinnerModel</tt> for this spinner
   */
  public BHSpinner(SpinnerModel model)
  {
    super(model);//sexy. :U
    addMouseWheelListener(new MouseWheelListener()
    {
      public void mouseWheelMoved(MouseWheelEvent e)
      {
        try
        {
          setValue(e.getWheelRotation() < 0 ? getNextValue() : getPreviousValue());
        }
        catch (IllegalArgumentException ex)
        {
          //They went too high or low.
        }
        catch (ClassCastException ex)
        {
          System.err.println("I don't even...");
          ex.printStackTrace();
        }
      }
    });
    super.setToolTipText(DEF_TOOL_TIP);
  }

  /**
   * Parses and returns the current value of the spinner as an <tt>int</tt>.
   * @return the current value of this spinner, as an <tt>int</tt>.
   */
  public int parseValueToInt()
  {
    return (int)parseValueToDouble();
  }

  /**
   * Parses and returns the current value of the spinner as a <tt>double</tt>.
   * @return the current value of this spinner, as a <tt>double</tt>.
   */
  public double parseValueToDouble()
  {
    return Double.parseDouble(parseValueToCharSequence().toString());
  }

  /**
   * Parses and returns the current value of the spinner as a <tt>long</tt>.
   * @return the current value of this spinner, as a <tt>long</tt>.
   */
  public long parseValueToLong()
  {
    return (long)parseValueToDouble();
  }

  /**
   * Parses and returns the current value of the spinner as a <tt>byte</tt>.
   * @return the current value of this spinner, as a <tt>byte</tt>.
   */
  public byte parseValueToByte()
  {
    return (byte)parseValueToDouble();
  }

  /**
   * Parses and returns the current value of the spinner as a <tt>float</tt>.
   * @return the current value of this spinner, as a <tt>float</tt>.
   */
  public float parseValueToFloat()
  {
    return (float)parseValueToDouble();
  }

  /**
   * Parses and returns the current value of the spinner as a <tt>short</tt>.
   * @return the current value of this spinner, as a <tt>short</tt>.
   */
  public short parseValueToShort()
  {
    return (short)parseValueToDouble();
  }

  /**
   * Returns a <tt>char</tt> representation of the return value of <tt>parseValueToInt()</tt>
   * @return the current value of the spinner as a <tt>char</tt>
   * @see #parseValueToInt()
   */
  public char parseValueToIntChar()
  {
    return (char)parseValueToInt();
  }

  /**
   * Returns the first <tt>char</tt> of the return value of <tt>parseValueToCharSequence()</tt>
   * @return the current value of the spinner as a <tt>char</tt>
   * @see #parseValueToCharSequence()
   */
  public char parseValueToStringChar()
  {
    return parseValueToCharSequence().charAt(0);
  }

  /**
   * Parses and returns the current value of the spinner as a <tt>CharSequence</tt>.
   * @return the current value of this spinner, as a <tt>CharSequence</tt>.
   */
  public CharSequence parseValueToCharSequence()
  {
    return getValue().toString();
  }

  /**
   * Returns a parsed version of the current value. The class to which this is parsed is based on the parameter <tt>T</tt> given
   * at the creation of this spinner.
   * @return a parsed version of the currently displayed value.
   */
//  @SuppressWarnings("unchecked")
  @SuppressWarnings("unchecked")
  public T parseValue()
  {
    return (T)(getValue());
  }

  @Override
  public void setToolTipText(String tipText)
  {
    this.tipText = tipText;
    super.setToolTipText(tipText == null ? DEF_TOOL_TIP : ((tipText.startsWith("<html>") ? tipText : "<html>" +
            (tipText.endsWith("</html>") ? tipText.substring(0, tipText.length() - "</html>".length()) : tipText)) +
            (tipText.length() == 0 ? "" : "<br/>") + DEF_TOOL_TIP));
  }
}
