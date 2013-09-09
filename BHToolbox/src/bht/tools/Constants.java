package bht.tools;

/**
 * Constants used throughout the toolbox.
 * @author Supuhstar of Blue Husky Programming
 * @version 1.0.1
 */
public enum Constants
{
  DEF;
  /** Represents an error message. Equivalent to {@link javax.swing.JOptionPane#ERROR_MESSAGE} */
  final public static byte ERROR_MES = javax.swing.JOptionPane.ERROR_MESSAGE;
  /** Represents an info message. Equivalent to {@link javax.swing.JOptionPane#INFORMATION_MESSAGE} */
  final public static byte INFO_MES = javax.swing.JOptionPane.INFORMATION_MESSAGE;
  /** Represents a plain message. Equivalent to {@link javax.swing.JOptionPane#PLAIN_MESSAGE} */
  final public static byte PLAIN_MES = javax.swing.JOptionPane.PLAIN_MESSAGE;
  /** Represents a question message. Equivalent to {@link javax.swing.JOptionPane#QUESTION_MESSAGE} */
  final public static byte QUESTION_MES = javax.swing.JOptionPane.QUESTION_MESSAGE;
  /** Represents a warning message. Equivalent to {@link javax.swing.JOptionPane#WARNING_MESSAGE} */
  final public static byte WARNING_MES = javax.swing.JOptionPane.WARNING_MESSAGE;
  /** Represents a the default message. Currently equivalent to {@link #QUESTION_MES} */
  final public static byte DEF_MES = QUESTION_MES;
  /** The default font size. Currently equivalent to {@code 11} */
  public static final double DEF_FONT_SIZE = 11;
  /** The name of the default font. Currently equivalent to {@code Segoe UI} */
  public static final String DEF_FONT_NAME = "Segoe UI";
}
