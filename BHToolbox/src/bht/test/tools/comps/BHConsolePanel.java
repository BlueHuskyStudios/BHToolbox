package bht.test.tools.comps;

import bht.tools.util.ArrayPP;
import bht.tools.util.ArrayTable;
import bht.tools.util.StringPP;
import java.awt.Font;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * BHConsolePanel, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012. License is default.<hr/>
 * @author Supuhstar of Blue Husky Programming
 * @since Mar 12, 2012
 * @version 1.0.0
 */
public class BHConsolePanel //NOTE: Must be compiled in UTF-8
        extends BHEditorPane
        implements Closeable, Flushable, Appendable
{
  private ArrayTable<Character> buffer;
  private boolean isClosed = false;
  private PrintStream printSurrogate;
  private InputStream inSurrogate;

  public BHConsolePanel()
  {
    buffer = new ArrayTable<>();
    printSurrogate = new PrintStream(new OutputStream()
    {private long start, appendTime, flushTime;
      @Override
      public void write(int b) throws IOException
      {
        start = System.nanoTime();
        getThis().append((char)b);
        appendTime = System.nanoTime();
        getThis().flush();
        flushTime = System.nanoTime();
      }
    });
    setFont(new Font("Lucida Console", Font.PLAIN, 12));
  }
  
  @SuppressWarnings("FinalPrivateMethod")
  private final BHConsolePanel getThis()
  {
    return this;
  }

  @Override
  public void close() throws IOException
  {
    isClosed = true;
  }

  @Override
  public void flush()// throws IOException
  {
    if (isClosed)
      return;
    
    StringPP line, newText = new StringPP();
    for (ArrayPP<Character> lineArray : buffer)
    {
      line = new StringPP(lineArray);
      newText.append(line);
    }
    setText(newText.toString());
    System.gc();
  }

  @Override
  public BHConsolePanel append(CharSequence csq) throws IOException
  {
    StringPP str = new StringPP(csq);
    for (Character c : str)
    {
      append(c);
    }
    return this;
  }

  @Override
  public BHConsolePanel append(CharSequence csq, int start, int end) throws IOException
  {
    return append(csq.subSequence(start, end));
  }

  @Override
  public BHConsolePanel append(char c) throws IOException
  {
    buffer.setLastEmptyCell(c);
    if (c == '\n' || c == '\r')
      buffer.addRow(new ArrayPP<Character>());
    return this;
  }

  public PrintStream printStream()
  {
    return printSurrogate;
  }

  public InputStream inputStream()
  {
    return inSurrogate;
  }
  
  public BHConsolePanel clear()
  {
    buffer = new ArrayTable<>();
    flush();
    return this;
  }
}
