package bht.tools.misc;

import bht.resources.Icons;
import javax.swing.JOptionPane;

/**
 * A utility that displays a dialog box with a yes-or-no question and answer buttons. Custom Nimbus icons are used if possible,
 * else the default are.
 * @version 1.3.1
 * @author Supuhstar of Blue Husky Programming, © 2010
 * @since 1.6_19
 */
public class YesNoBox
{
  /** Represents an error message. */
  final public static byte ERROR_MES = JOptionPane.ERROR_MESSAGE;
  /** Represents an info message. */
  final public static byte INFO_MES = JOptionPane.INFORMATION_MESSAGE;
  /** Represents a plain message. */
  final public static byte PLAIN_MES = JOptionPane.PLAIN_MESSAGE;
  /** Represents a question message. */
  final public static byte QUESTION_MES = JOptionPane.QUESTION_MESSAGE;
  /** Represents a warning message. */
  final public static byte WARNING_MES = JOptionPane.WARNING_MESSAGE;
  /** Represents a the default message. */
  final public static byte DEF_MES = QUESTION_MES;
  /** Return value from class method if the "Yes" option is chosen. */
  final public static int YES_RES = 0;
  /** Return value from class method if the "No" option is chosen. */
  final public static int NO_RES = 1;
  /** Return value from class method if user closes window without selecting anything.
   * More than likely this should be treated as a <code>NO_RES</code>. */
  final public static int CLOSED_RES = -1;
  private static String dir = java.io.File.separator + "bht" + java.io.File.separator + "resources" + java.io.File.separator;
//  private static ImageIcon warningIcon = Icons.warningIcon32, errorIcon = Icons.errorIcon32, infoIcon = Icons.infoIcon32, questionIcon = Icons.questionIcon32;

  /**
   *
   * Displays a dialog box with a yes/no answer. Answer buttons are automatically translated. The title and icon type are
   * automatically filled in.
   * @param message The <code>String</code> containing the text that comprises the message shown in the dialog box
   * @return <code>true</code> if user pressed the "Yes" button. Any other action returns <code>false</code>
   */
  public static boolean bool(String message)
  {
    return integer(message) == YES_RES;
  }

  /**
   * Displays a dialog box with a yes/no answer. Answer buttons are automatically translated.
   * @param message The <code>String</code> containing the text that comprises the message shown in the dialog box
   * @param title The <code>String</code> containing the text in the title bar
   * @param type The <code>int</code> representing the icon type. Recommended that you use the corresponding <code>int</code>s
   * provided in this class or those in the javax.swing.JOptionPane class
   * @return <code>true</code> represents that the user pressed the "Yes" button. Any other action returns <code>false</code>
   */
  public static boolean bool(String message, String title, byte type)
  {
    return integer(message, title, type) == YES_RES;
  }

  /**
   * Displays a dialog box with a yes/no answer. Answer buttons are automatically translated. The title and icon type are
   * automatically filled in.
   * @param message The <code>String</code> containing the text that comprises the message shown in the dialog box
   * @return an <code>int</code> representing whether the user pressed yes or nor simply closed the box.
   * <ul>
   * <li>{@link #YES_RES} if the user presses the "Yes" option
   * <li>{@link #NO_RES} if the user presses the "No" option
   * <li>{@link #CLOSED_RES} if the user closes the dialog
   * </ul>
   */
  public static int integer(String message)
  {
    return integer(message, "Yes or No?", QUESTION_MES);
  }

  /**
   * Displays a dialog box with a yes/no answer. Answer buttons are automatically translated.
   * @param message The <code>String</code> containing the text that comprises the message shown in the dialog box
   * @param title The <code>String</code> containing the text in the title bar
   * @param type The <code>byte</code> representing the icon type.
   * It is recommended that you use the corresponding <code>byte</code>s provided in this class or those in the
   * javax.swing.JOptionPane class
   * @return an <code>int</code> representing whether the user pressed yes or nor simply closed the box.
   * <ul>
   * <li>{@link #YES_RES} if the user presses the "Yes" option
   * <li>{@link #NO_RES} if the user presses the "No" option
   * <li>{@link #CLOSED_RES} if the user closes the dialog
   * </ul>
   */
  public static int integer(String message, String title, byte type)
  {
    try
    {
      return JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION, type, Icons.getIcon(type));
    }
    catch (Throwable t)
    {
      t.printStackTrace();
      return JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION, type);
    }
  }
}
