package bht;

import bht.tools.fx.LookAndFeelChanger;
import javax.swing.JOptionPane;

/**
 * DummyMain, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012. License is default.<hr/>
 * In case someone tries to run the toolbox, they get this.
 * @author Supuhstar of Blue Husky Programming
 * @since Mar 11, 2012
 * @version 1.0.0
 */
public class DummyMain //NOTE: Must be compiled in UTF-8
{
  static //Do this all without a main method to save an extra step of time
  {
    LookAndFeelChanger.setLookAndFeel(LookAndFeelChanger.SYSTEM);                     //Change the LAF to the system's native LAF so the user is more comfortable seeing it
    
    JOptionPane.showOptionDialog(null,                                                //Parent component (none, as this is a standalone dialog)
                                 "This isn't the JAR we're looking for. Move along.", //Body message (echoing title Force Suggestion)
                                 "This is not the JAR you're looking for.",           //Title message (Obi-Wan's Force Suggestion)
                                 JOptionPane.OK_OPTION,                               //Type of options to be presented (one, that closes the dialog)
                                 JOptionPane.WARNING_MESSAGE,                         //Type of message to be presented (warn the user they they've made a mistake)
                                 null,                                                //Custom icon (none, default to LAF warning icon)
                                 new String[]{"Move along."},                         //List of options (one, with the second Force Suggestion)
                                 "Move along.");                                      //Default option (the only one)
  }
  
  public static void main(String[]args){}
}
