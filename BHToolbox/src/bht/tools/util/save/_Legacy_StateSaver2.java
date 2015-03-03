package bht.tools.util.save;

import bht.tools.util.StringPP;
import java.awt.Component;
import java.awt.Container;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Programmer
 */
public class _Legacy_StateSaver2
{
  private boolean stateIsPrepared = false;
  private File dest;
  private StringPP spacer = new StringPP(), stateStr;
  private IllegalArgumentException badTarget = new IllegalArgumentException("target is not a Saveable");
  
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

  /** The <tt>String</tt> representing the end of a state */
  public static final String STATE_END = ";";
  /** The <tt>String</tt> representing the text in a component */
  public static final String STATE_TEXT = "txt" + STATE_BEGIN;
  /** The <tt>String</tt> representing the text in a component's tool tip */
  public static final String STATE_TOOLTIP = "toolTxt" + STATE_BEGIN;
  /** The <tt>String</tt> representing a <tt>java.awt.Label</tt> */
  public static final String STATE_FORE = "fore" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>java.awt.Label</tt> */
  public static final String STATE_BACK = "back" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>java.awt.Label</tt> */
  public static final String STATE_BOUNDS = "bounds" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>java.awt.Label</tt> */
  public static final String STATE_OPAQ = "opaq" + PART_BEGIN;
  /** The <tt>String</tt> representing a <tt>java.awt.Label</tt> */
  public static final String STATE_STATE = "state" + PART_BEGIN;
  
  public _Legacy_StateSaver2()
  {
    this(new File(System.getProperty("user.dir") + File.separator + "state - " + java.util.Calendar.getInstance().getTime().toString()));
  }
  
  public _Legacy_StateSaver2(File saveDest)
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
    else
      throw badTarget;
    //</editor-fold>
  }

  private String queueStateFor(Component target)
  {
    String state = target.getAccessibleContext().getAccessibleName() + NAME_END;
    
    if (target instanceof Saveable)
      state += ((Saveable)target).getStringToSave();
    else
      throw badTarget;
    
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
}
