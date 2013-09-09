/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bht.tools.comps;

import bht.tools.fx.Colors;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.swing.JLabel;

/**
 * A convenience class that extends the capabilities of a {@link JLabel} in order to very easily make a link to a URI or URL
 * @author Supuhstar of Blue Husky Programming
 * @version 1.0.2
 * @since 1.6_22
 */
public class BHLinkLabel extends JLabel
{
  private byte behavior = DEF_BEHAVIOR;
  public static final byte ALWAYS_UNDERLINE = 0x0,
                           UNDERLINE_ON_HOVER = 0x1,
                           UNDERLINE_OFF_HOVER = 0x2,
                           NEVER_UNDERLINE = 0x4,
                           DEF_BEHAVIOR = UNDERLINE_ON_HOVER;
  private CharSequence text;
  private static final String UNDERLINE_TEXT = "<html><span style=\"text-decoration: underline;\">";
  private java.net.URI URI;
  private static final java.net.URI NULL_URI = null;
  
  public BHLinkLabel()
  {
    this(null, NULL_URI, DEF_BEHAVIOR);
  }
  
  public BHLinkLabel(CharSequence defText, String uriToBrowse)
  {
    this(defText, uriToBrowse, DEF_BEHAVIOR);
  }
  
  public BHLinkLabel(CharSequence defText, String uriToBrowse, byte underlineBehavior)
  {
    setText(defText);
    setURI(uriToBrowse);
    setForeground(java.awt.Color.BLUE);
    setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
    setToolTipText(uriToBrowse.toString());
    addMouseListener(new java.awt.event.MouseListener() {
      @Override
      public void mouseClicked(MouseEvent e)
      {
        try
        {
          java.awt.Desktop.getDesktop().browse(URI);
        }
        catch (IOException ex)
        {
          ex.printStackTrace();
        }
      }

      @Override public void mousePressed(MouseEvent e){}
      @Override public void mouseReleased(MouseEvent e){}

      @Override
      public void mouseEntered(MouseEvent e)
      {
        fixTextForFocus();
      }

      @Override
      public void mouseExited(MouseEvent e)
      {
        fixTextForDefocus();
      }
    });
    getAccessibleContext().setAccessibleDescription(getAccessibleContext().getAccessibleDescription() +
            Colors.FORE_OVERRIDE + Colors.getHexString(getForeground()) + Colors.COMMAND_SEP);
  }
  
  public BHLinkLabel(CharSequence defText, URI uriToBrowse, byte underlineBehavior)
  {
    this (defText, String.valueOf(uriToBrowse), underlineBehavior);//Changed from .toString to String.valueOf() on March 3, 2012 (1.0.2) for testing/creation of BHNotifier
  }

  public BHLinkLabel(CharSequence defText, URL urlToBrowse, byte underlineBehavior)
  {
    this(defText, String.valueOf(urlToBrowse), underlineBehavior);//Changed from .toString to String.valueOf() on March 3, 2012 (1.0.2) for testing/creation of BHNotifier
  }
  
  public void setText(CharSequence newText)
  {
    setText(String.valueOf(newText));
  }
  
  @Override
  public void setText(String newText)
  {
    text = newText;
    fixTextForDefocus();
  }

  private void fixTextForDefocus()
  {
    String mod;
    
    switch (behavior)
    {
      default:
      case ALWAYS_UNDERLINE:
      case UNDERLINE_OFF_HOVER:
        mod = UNDERLINE_TEXT;
        break;
      case NEVER_UNDERLINE:
      case UNDERLINE_ON_HOVER:
        mod = "";
        break;
    }
    super.setText(mod + text);
  }

  private void fixTextForFocus()
  {
    
    String mod;
    
    switch (behavior)
    {
      default:
      case NEVER_UNDERLINE:
      case UNDERLINE_OFF_HOVER:
        mod = "";
        break;
      case ALWAYS_UNDERLINE:
      case UNDERLINE_ON_HOVER:
        mod = UNDERLINE_TEXT;
        break;
    }
    super.setText(mod + text);
  }

  public void setURI(java.net.URI newUriToBrowse)
  {
    URI = newUriToBrowse;
  }

  private void setURI(String uriToBrowse)
  {
    try
    {
      setURI(new URI(uriToBrowse));
    }
    catch (URISyntaxException ex)
    {
      setToolTipText("Could not parse URI: " + uriToBrowse);
      setURI((URI)null);
      setEnabled(false);
    }
  }
}
