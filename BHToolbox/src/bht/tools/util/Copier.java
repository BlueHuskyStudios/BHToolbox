package bht.tools.util;

import bht.resources.Icons;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;


/**
 * This class allows easy copying of information to the Operating System's clipboard
 * @author Blue Husky Programming, © 2010
 * @version 1.1.2
 */
public class Copier
{
  /**
  Places a <code>String</code> into the Operating System's clipboard
  @param input the <code>String</code> to be copied
   */
  public static void copy(String input)
  {
//    DataFlavor[] df = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().getAvailableDataFlavors();
//    for (int i = 0; i < df.length; i++)
//    {
//      System.out.println(df[i].getHumanPresentableName());
//    }
    try
    {
      java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new java.awt.datatransfer.StringSelection(input), null);
    }
    catch (Throwable ex)
    {
      javax.swing.JOptionPane.showMessageDialog(null, "Exception while trying to copy " + input + ":\n" + ex,
                                                ex.getClass().getSimpleName() + " while copying",
                                                javax.swing.JOptionPane.ERROR_MESSAGE, bht.resources.Icons.getIcon(Icons.ERROR_32));
      javax.swing.JOptionPane.showMessageDialog(null, "Information not copied", ex.getClass().getSimpleName() + " while copying",
                                                javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }
  }

  /**
  Places a <code>String</code> representation of an <code>int</code> into the Operating System's clipboard
  @param input the <code>int</code> to be copied
   */
  public static void copy(int input)
  {
    copy(Integer.toString(input));
  }

  /**
  Places a <code>String</code> representation of a <code>double</code> into the Operating System's clipboard
  @param input the <code>double</code> to be copied
   */
  public static void copy(double input)
  {
    copy(Double.toString(input));
  }

  /**
  Places a <code>String</code> representation of a <code>long</code> into the Operating System's clipboard
  @param input the <code>long</code> to be copied
   */
  public static void copy(long input)
  {
    copy(Long.toString(input));
  }

  /**
  Places a <code>String</code> representation of a <code>float</code> into the Operating System's clipboard
  @param input the <code>float</code> to be copied
   */
  public static void copy(float input)
  {
    copy(Float.toString(input));
  }

  /**
  Places a <code>String</code> representation of a <code>char</code> into the Operating System's clipboard
  @param input the <code>char</code> to be copied
   */
  public static void copy(char input)
  {
    copy(Character.toString(input));
  }

  /**
  Places a <code>String</code> representation of a <code>byte</code> into the Operating System's clipboard
  @param input the <code>byte</code> to be copied
   */
  public static void copy(byte input)
  {
    copy(Byte.toString(input));
  }

  /**
  Places the return value of an <code>Object</code>'s <code>toString()</code> method into the Operating System's clipboard
  @param input the <code>Object</code> to be copied
   */
  public static void copy(Object input)
  {
    copy(input.toString());
  }



  /**
   * Recurses through all components in this one and copies all selected text.
   * @param c the component to search
   * @return <tt>true</tt> if and only if the method has found text to copy.
   */
  public static boolean searchToCopyIn(java.awt.Component c)
  {
    boolean found = false;
    String copy = "";
    if (c instanceof javax.swing.text.JTextComponent && ((javax.swing.text.JTextComponent) c).getSelectedText() != null)
    {
      copy += (((javax.swing.text.JTextComponent)c).getSelectedText()) + "\n";
      found = true;
    }
    if (c instanceof java.awt.Container)
      for (int i = 0; i < ((java.awt.Container) c).getComponentCount(); i++)
        found = found || searchToCopyIn(((java.awt.Container) c).getComponent(i));
    try
    {
      copy(copy.substring(0, copy.length() - 1));
    }
    catch (StringIndexOutOfBoundsException ex)
    {
      return false;
    }
    return found;
  }

  public static String getClipboardString()
  {
    try
    {
      return java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null).getTransferData(DataFlavor.stringFlavor).toString();
    }
    catch (Throwable t)
    {
      t.printStackTrace();
      return "[ERROR FETCHING CLIPBOARD CONTENTS - " + t.getClass().getSimpleName() + "]";
    }
  }
}
