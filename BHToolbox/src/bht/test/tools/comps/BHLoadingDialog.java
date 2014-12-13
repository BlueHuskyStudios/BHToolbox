package bht.test.tools.comps;

import bht.tools.fx.CompAction;
import java.awt.BorderLayout;
import java.lang.reflect.Method;
import javax.swing.JDialog;
import javax.swing.JProgressBar;

/**
 * BHLoadingDialog, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012. License is default.<hr/>
 * @author Supuhstar of Blue Husky Programming
 * @since Mar 12, 2012
 * @version 1.0.0
 */
public class BHLoadingDialog extends JDialog //NOTE: Must be compiled in UTF-8
{
  private JProgressBar progressBar;
  private CompAction ca;
  

  /**
   * Does nothing.
   */
  static
  {
  }
  
  /**
   * Creates a new {@link BHLoadingDialog}
   */
  public BHLoadingDialog()
  {
    initGUI();
    ca = new CompAction();
    fixUI();
  }

  private void initGUI()
  {
    setUndecorated(true);
    
    progressBar = new JProgressBar();
    progressBar.setIndeterminate(true);
    progressBar.setStringPainted(true);
    progressBar.setString("Loading...");
    add(progressBar, BorderLayout.CENTER);
  }
  
  public void setPercentComplete(double percent)
  {
    if (percent < 0)
      progressBar.setIndeterminate(true);
    else
      progressBar.setValue((int)(percent * (progressBar.getMaximum() - progressBar.getMinimum())));
  }
  
  /**
   * Makes the dialog display that the given method is running in the following format: "{@code Running methodName in ClassName…}"
   * @param m the method which will be displayed to the user as the currently running one
   */
  public void setCurrentMethod(Method m)
  {
    if (checkNull(m))
      return;
    progressBar.setString("Running " + m.getName() + " in " + m.getDeclaringClass().getSimpleName() + "…");
    fixUI();
  }
  
  /**
   * Makes the dialog display that the given class is being created in the following format: "{@code Creating ClassName…}"
   * @param c the class which will be displayed to the user as the currently running one
   */
  public void setCurrentClass(Class c)
  {
    if (checkNull(c))
      return;
    progressBar.setString("Creating " + c.getSimpleName() + "…");
    fixUI();
  }
  
  /**
   * If it is not appropriate to use {@link #setCurrentMethod(Method m)} or {@link #setCurrentClass(Class c)}, then this method
   * should be employed to alert the user of the current action
   * @param action the action which will be displayed to the user as the current one
   */
  public void setCurrentAction(CharSequence action)
  {
    if (checkNull(action))
      return;
    progressBar.setString(action.toString());
    fixUI();
  }

  private boolean checkNull(Object o)
  {
    if (o == null)
    {
      progressBar.setString("Loading…");
      return true;
    }
    return false;
  }

  private void fixUI()
  {
    pack();
    setSize(getPreferredSize());
    ca.goToCenter(this);
  }
}
