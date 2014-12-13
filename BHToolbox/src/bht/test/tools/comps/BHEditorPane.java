package bht.test.tools.comps;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import javax.swing.JEditorPane;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;

/**
 * BHEditorPane, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012. License is default.<hr/>
 * @author Supuhstar of Blue Husky Programming
 * @since Apr 30, 2012
 * @version 1.0.0
 */
public class BHEditorPane extends JEditorPane//NOTE: Must be compiled in UTF-8
{
  private String text;

  public BHEditorPane(String type, String text)
  {
    super(type, text);
  }

  public BHEditorPane(URL initialPage) throws IOException
  {
    super(initialPage);
  }

  public BHEditorPane(String url) throws IOException
  {
    super(url);
  }

  public BHEditorPane()
  {
  }

  @Override
  public void setText(String t)
  {
    text = t;
  }

  @Override
  public String getText()
  {
    return text;
  }
  
  
}
