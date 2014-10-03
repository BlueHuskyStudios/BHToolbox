package bht.tools;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;

/**
 * Constants used throughout the toolbox.
 * @author Supuhstar of Blue Husky Programming
 * @version 1.1.0
 *		- 1.1.0 (2014-10-02) - Kyli Rouge moved MAX_WIN_BOUNDS, SCREN_BOUNDS, and ALL_SCREEN_BOUNDS from CompAction
 *		- 1.0.1 (2014-03)
 *		- 1.0.0
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
  /** The maximum boundaries of a window (i.e. the size of a maximized window) */
  public static final Dimension MAX_WIN_BOUNDS;
  /** The boundaries of the default screen */
  public static final Dimension SCREEN_BOUNDS;
  /** The boundaries of all screens */
  public static final Dimension ALL_SCREEN_BOUNDS[];
  
  
  
  static
  {
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    DisplayMode dm = ge.getScreenDevices()[0].getDisplayMode();
    MAX_WIN_BOUNDS = new Dimension(dm.getWidth(), dm.getHeight());
    SCREEN_BOUNDS = new Dimension((dm = ge.getDefaultScreenDevice().getDisplayMode()).getWidth(), dm.getHeight());
    
    ALL_SCREEN_BOUNDS = new Dimension[ge.getScreenDevices().length];
    for (int i = 0; i < ALL_SCREEN_BOUNDS.length; i++)
      ALL_SCREEN_BOUNDS[i] = new Dimension((dm = ge.getScreenDevices()[i].getDisplayMode()).getWidth(), dm.getHeight());
  }
}
