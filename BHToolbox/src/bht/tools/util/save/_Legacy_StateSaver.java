package bht.tools.util.save;


//<editor-fold defaultstate="collapsed" desc="AWT Components">
import bht.tools.util.StringPP;
import bht.tools.util.save.Saveable;
import java.awt.Component;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.List;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuComponent;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.ScrollPane;
import java.awt.Scrollbar;
import java.awt.SplashScreen;
import java.awt.TextArea;
import java.awt.TextComponent;
import java.awt.TextField;
import java.awt.Window;
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Swing Components">
import javax.swing.AbstractButton;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
//import javax.swing.JLayer; //For 1.7
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JRootPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JToolTip;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.JWindow;
        
//</editor-fold>

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Programmer
 */
class _Legacy_StateSaver
{
  private boolean stateIsPrepared = false;
  private File dest;
  private StringPP spacer = new StringPP(), stateStr;
  
  /** The <tt>String</tt> representing the beginning of a part's state information */
  public static final String PART_BEGIN = "(";
  /** The <tt>String</tt> representing the end of a part's state information */
  public static final String PART_END = ")\n\r";
  /** The <tt>String</tt> representing the beginning of information about a state of a component */
  private static final String STATE_BEGIN = ":";
  /** The <tt>String</tt> representing the fact that the name of a component has ended and the states are beginning */
  private static final String NAME_END = "-";
  /** The <tt>String</tt> spacing the info from the left margin in the file */
  private static final String DEF_SPACER = "  ";
  
  //<editor-fold defaultstate="collapsed" desc="AWT Components">
  /** The <tt>String</tt> representing a <tt>java.awt.Button</tt> */
  public static final String PART_BUTTON = "Button" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>java.awt.Checkbox</tt> */
  public static final String PART_CHECK = "Check" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>java.awt.Container</tt> */
  public static final String PART_CNTNR = "Container" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>java.awt.Dialog</tt> */
  public static final String PART_DIA = "Dialog" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>java.awt.FileDialog</tt> */
  public static final String PART_FDIA = "FileDia" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>java.awt.Frame</tt> */
  public static final String PART_FRAME = "Frame" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>java.awt.Label</tt> */
  public static final String PART_LABEL = "Label" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>java.awt.List</tt> */
  public static final String PART_LIST = "List" + PART_BEGIN;
  //  /** The <tt>String</tt> representing a <tt>java.awt.Menu</tt> */
  //  public static final String PART_MENU = "Menu" + PART_BEGIN;
  //  /** The <tt>String</tt> representing a <tt>java.awt.MenuBar</tt> */
  //  public static final String PART_MNUBR = "MenuBar" + PART_BEGIN;
  //  /** The <tt>String</tt> representing a <tt>java.awt.</tt> */
  //  public static final String PART_MNUCMP = "MenuComp" + PART_BEGIN;
  //  /** The <tt>String</tt> representing a <tt>java.awt.</tt> */
  //  public static final String PART_MNUITM = "MenuItem" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>java.awt.Panel</tt> */
  public static final String PART_PANEL = "Panel" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>java.awt.ScrollPane</tt> */
  public static final String PART_SCRLPN = "ScrollPane" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>java.awt.Scrollbar</tt> */
  public static final String PART_SCRLBR = "Scrollbar" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>java.awt.SplashScreen</tt> */
  public static final String PART_SPLASH = "SplashScreen" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>java.awt.TextArea</tt> */
  public static final String PART_TXTARA = "TextArea" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>java.awt.TextComponent</tt> */
  public static final String PART_TXTCMPNT = "TextComponent" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>java.awt.</tt> */
  public static final String PART_TXTFLD = "TextField" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>java.awt.Window</tt> */
  public static final String PART_WNDW = "Window" + PART_BEGIN;
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Swing Components">
  /** The <tt>String</tt> representing a <tt>javax.swing.JApplet</tt> */
  public static final String PART_JAPLT = "JApplet" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JButton</tt> */
  public static final String PART_JBTN = "JButton" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JCheckBox</tt> */
  public static final String PART_JCHKBX = "JCheckBox" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JCheckBoxMenuItem</tt> */
  public static final String PART_JCHKBX_MNUITM = "JCheckBoxMenuItem" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JColorChooser</tt> */
  public static final String PART_JCLRCHSR = "JColorChooser" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JComboBox</tt> */
  public static final String PART_JCMBBX = "JComboBox" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JComponent</tt> */
  public static final String PART_JCMPNT = "JComponent" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JDesktopPane</tt> */
  public static final String PART_JDSKTP = "JDesktopPane" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JDialog</tt> */
  public static final String PART_JDIA = "JDialog" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JEditorPane</tt> */
  public static final String PART_JEDTRPN = "JEditorPane" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JFileChooser</tt> */
  public static final String PART_JFILECHSR = "JFileChooser" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JFormattedTextField</tt> */
  public static final String PART_JFRMTTXT = "JFormattedTextField" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JFrame</tt> */
  public static final String PART_JFRAME = "JFrame" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JInternalFrame</tt> */
  public static final String PART_JINTFRAME = "JInternalFrame" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JLabel</tt> */
  public static final String PART_JLABEL = "JLabel" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JLayer</tt> */
  public static final String PART_JLAYER = "JLayer" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JLayeredPane</tt> */
  public static final String PART_JLYRPANE = "JLayeredPane" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JList</tt> */
  public static final String PART_JLIST = "JList" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JMenu</tt> */
  public static final String PART_JMENU = "JMenu" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JMenuBar</tt> */
  public static final String PART_JMNUBR = "JMenuBar" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JMenuItem</tt> */
  public static final String PART_JMNUITM = "JMenuItem" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JOptionPane</tt> */
  public static final String PART_JOPTNPN = "JOptionPane" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JPanel</tt> */
  public static final String PART_JPANEL = "JPanel" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JPasswordField</tt> */
  public static final String PART_JPSWRDFLD = "JPasswordField" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JPopupMenu</tt> */
  public static final String PART_JPOPMNU = "JPopupMenu" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JProgressBar</tt> */
  public static final String PART_JPRGRSBR = "JProgressBar" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JRadioButton</tt> */
  public static final String PART_JRADBTN = "JRadioButton" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JRadioButtonMenuItem</tt> */
  public static final String PART_JRADBTNMNUITM = "JRadioButtonMenuItem" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JRootPane</tt> */
  public static final String PART_JROOTPN = "JRootPane" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JScrollBar</tt> */
  public static final String PART_JSCRLBR = "JScrollBar" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JScrollPane</tt> */
  public static final String PART_JSCRLPN = "JScrollPane" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JSeparator</tt> */
  public static final String PART_JSEP = "JSeparator" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JSlider</tt> */
  public static final String PART_JSLIDER = "JSlider" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JSpinner</tt> */
  public static final String PART_JSPNR = "JSpinner" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JSplitPane</tt> */
  public static final String PART_JSPLTPN = "JSplitPane" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JTabbedPane</tt> */
  public static final String PART_JTABPANE = "JTabbedPane" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JTable</tt> */
  public static final String PART_JTBL = "JTable" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JTextArea</tt> */
  public static final String PART_JTXTAREA = "JTextArea" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JTextField</tt> */
  public static final String PART_JTXTFIELD = "JTextField" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JTextPane</tt> */
  public static final String PART_JTXTPN = "JTextPane" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JToggleButton</tt> */
  public static final String PART_JTGLBTN = "JToggleButton" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.J</tt> */
  public static final String PART_JTOOLBAR = "JToolBar" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JToolTip</tt> */
  public static final String PART_JTOOLTIP = "JToolTip" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JTree</tt> */
  public static final String PART_JTREE = "JTree" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JViewport</tt> */
  public static final String PART_JVIEWPORT = "JViewport" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>javax.swing.JWindow</tt> */
  public static final String PART_JWINDOW = "JWindow" + PART_BEGIN;
  //</editor-fold>

  /** The <tt>String</tt> representing the end of a state */
  public static final String STATE_END = ";";
  /** The <tt>String</tt> representing the text in a component */
  public static final String STATE_TEXT = "txt" + STATE_BEGIN;
  /** The <tt>String</tt> representing a <tt>java.awt.Label</tt> */
  public static final String STATE_FORE = "fore" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>java.awt.Label</tt> */
  public static final String STATE_BACK = "back" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>java.awt.Label</tt> */
  public static final String STATE_OPAQ = "opaq" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>java.awt.Label</tt> */
  public static final String STATE_STATE = "state" + PART_BEGIN;
  
  public _Legacy_StateSaver()
  {
    this(new File(System.getProperty("user.dir") + File.separator + "state - " + java.util.Calendar.getInstance().getTime().toString()));
  }
  
  public _Legacy_StateSaver(File saveDest)
  {
    dest = saveDest;
  }
  
  public void saveStateFor(Component target) throws IOException
  {
    String state = queueStateFor(target);
    
    dest.mkdirs();
    dest.setWritable(true);
    dest.setReadable(true);
    System.out.println("Writing state for \"" + target + "\" to \"" + dest + "\"");
    java.io.PrintWriter print = new java.io.PrintWriter(dest);
    print.println(state);
    stateIsPrepared = false;
  }
  
  public void loadStateTo(Component target) throws FileNotFoundException
  {
    if (!stateIsPrepared)
    {
      prepareState();
    }
    
    // <editor-fold defaultstate="collapsed" desc="Format Checking">
    if (target.getAccessibleContext() == null || target.getAccessibleContext().getAccessibleName() == null || target.
            getAccessibleContext().getAccessibleName().isEmpty())
      throw new IllegalArgumentException("Target component has not been prepared for saving or loading its state (No Accessible"
                                         + " Name), so I couldn't possibly find which state is its.");
    
    StringPP partName = new StringPP(target.getAccessibleContext().getAccessibleName());
    
    if (!stateStr.contains(partName))
      throw new IllegalArgumentException("State of this component has not been saved, so I can't use it.");
    
//    if (stateStr.getOccurrencesOf(partName) > 1)
//      throw new IllegalArgumentException("State was saved multiple times in the same file, or multiple components were saved"
//                                         + " with the same Accessible Name. I can't discern which one to use.");
    // </editor-fold>
    StringPP compState;
//    for (int i=0, o=stateStr.getOccurrencesOf(partName); i < o; i++)
//    {
//      if (partNameMatchesPartType(compState.substring(compState.indexOf(NAME_END) + NAME_END.length(),
//                               compState.indexOf(PART_BEGIN)), target))
      compState = new StringPP(stateStr.substring(stateStr.indexOf(partName) + partName.length(),
                                                         stateStr.indexOf(PART_END, stateStr.indexOf(partName))));
//    }
    
    System.out.println(compState);
    //<editor-fold defaultstate="collapsed" desc="Component recognition">
    if (target instanceof Saveable)
    {
      ((Saveable)target).loadFromSavedString(compState);
    }
    else if (target instanceof Button)
    {
      StringPP state = compState.substring(compState.indexOf(NAME_END) + NAME_END.length(),
                               compState.indexOf(PART_BEGIN));
      if (!state.equalsIgnoreCase(PART_BUTTON))
        throw new IllegalArgumentException("The name of the component lies! " + target.getAccessibleContext().getAccessibleName());
      loadStateToPredefinedComponent(PART_BUTTON, target, compState);
    }
    //</editor-fold>
  }

  private String queueStateFor(Component target)
  {
    String state = target.getAccessibleContext().getAccessibleName() + NAME_END;
    if (target instanceof Button)
    {
      state += (PART_BUTTON +
                STATE_TEXT + ((Button)target).getLabel() + STATE_END +
                STATE_FORE + ((Button)target).getForeground().getRGB() + STATE_END +
                STATE_BACK + ((Button)target).getBackground().getRGB() + STATE_END +
                STATE_OPAQ + ((Button)target).isOpaque() + STATE_END +
                PART_END);
    }
    else if (target instanceof Checkbox)
    {
      state += (PART_BUTTON +
                STATE_TEXT  + ((Checkbox)target).getLabel() + STATE_END +
                STATE_FORE  + ((Checkbox)target).getForeground().getRGB() + STATE_END +
                STATE_BACK  + ((Checkbox)target).getBackground().getRGB() + STATE_END +
                STATE_OPAQ  + ((Checkbox)target).isOpaque() + STATE_END +
                STATE_STATE + ((Checkbox)target).getState() + STATE_END +
                PART_END);
    }
    else if (target instanceof Label)
    {
      state += (PART_LABEL +
                STATE_TEXT + ((Label)target).getText() + STATE_END +
                STATE_FORE + ((Label)target).getForeground().getRGB() + STATE_END +
                STATE_BACK + ((Label)target).getBackground().getRGB() + STATE_END +
                STATE_OPAQ + ((Label)target).isOpaque() + STATE_END +
                PART_END);
    }
    
    if (target instanceof Container)
    {
      spacer.append(DEF_SPACER);
      for (int i=0; i < ((Container)target).getComponentCount(); i++)
        state += queueStateFor(((Container)target).getComponent(i));
    }
    return state;
  }

  private void prepareState() throws FileNotFoundException
  {
    stateStr = new StringPP("");
    
    Scanner input = new Scanner(dest);
    for (int i=0; input.hasNextLine(); i++)
    {
      stateStr.appendln(input.nextLine());
    }
    
    stateIsPrepared = true;
  }

  private void loadStateToPredefinedComponent(final CharSequence PART_NAME, Component target, CharSequence partState)
  {
    target.setForeground(new Color(Integer.parseInt(extractFeatureFromStateString(STATE_FORE, partState).toString())));
    target.setBackground(new Color(Integer.parseInt(extractFeatureFromStateString(STATE_BACK, partState).toString())));
    
    if (PART_NAME.equals(PART_BUTTON))
    {
      ((Button)target).setLabel(extractFeatureFromStateString(STATE_TEXT, partState).toString());
    }
    else if (PART_NAME.equals(PART_LABEL))
    {
      ((Label)target).setText(extractFeatureFromStateString(STATE_TEXT, partState).toString());
    }
  }

  private StringPP extractFeatureFromStateString(final CharSequence STATE_NAME, CharSequence partState)
  {
    StringPP s = new StringPP(partState);
    return s.substring(s.indexOf(STATE_NAME) + s.length(), s.indexOf(STATE_END, s.indexOf(STATE_NAME)));
  }
}
