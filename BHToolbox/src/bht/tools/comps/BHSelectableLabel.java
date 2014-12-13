/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bht.tools.comps;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.Border;

/**
 *
 * @author Supuhstar
 */
public class BHSelectableLabel extends BHTextField
{
  private static final Border OLD_BORDER = new BHTextField().getBorder();
  
  public BHSelectableLabel()
  {
    this(null, null, LEADING);
  }
  
  public BHSelectableLabel(String text)
  {
    this(null, text, LEADING);
  }
  
  public BHSelectableLabel(int horizAlignment)
  {
    this(null, null, horizAlignment);
  }
  
  public BHSelectableLabel(String text, int horizAlignment)
  {
    this(null, text, horizAlignment);
  }
  
  public BHSelectableLabel(javax.swing.text.Document doc, String text, int horizAlignment)
  {
    super(doc, null, 0);
    
    setDragEnabled(true);
    setEditable(false);
    setOpaque(false);
    setText(text);
    setHorizontalAlignment(horizAlignment);
    setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.TEXT_CURSOR));
    
    addMouseListener(new MouseAdapter()
    {
      @Override
      public void mousePressed(MouseEvent e)
      {
        FontMetrics fm = getFontMetrics(getFont());
        int pos = 0, length = 0, x = e.getX();
        CharSequence cs = getText();
        for (int i = 0, l = cs.length(); i < l; i++)
        {
          pos += fm.charWidth(cs.charAt(i));
          if (pos > x)
            break;
          length++;
        }
        setCaretPosition(length);
      }
    });
  }

  @Override
  public void setText(String text)
  {
    super.setText(text);
    setMinimumSize(getPreferredSize());
  }

  @Override
  public void setEditable(boolean b)
  {
    setOpaque(!b);
    setBackground(b ? SystemColor.text : /*SystemColor.control*/new Color(SystemColor.control.getRGB() | 0xFF000000, true));
    super.setEditable(b);
    setBorder(b ? OLD_BORDER : null);
  }
}
