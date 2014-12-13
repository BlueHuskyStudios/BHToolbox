package bht.test.tools.comps;

import bht.tools.fx.CompAction;
import bht.tools.fx.CompAction.ScreenLoc;
import bht.tools.util.ArrayPP;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 
 * @author Programmer
 */
public class _Legacy_BHNotifier
{
  private ArrayPP<_Legacy_BHNotifierDialog> dialogs;
  private ArrayPP<Integer> closeingDialogs;
  private boolean alwaysOnTop = true;
  private ScreenLoc corner = ScreenLoc.BOTTOM_RIGHT;
  private int decay;
  public static final int DEF_DECAY = 5000;

  public _Legacy_BHNotifier()
  {
    this(DEF_DECAY);
  }

  public _Legacy_BHNotifier(int decay)
  {
    this.decay = decay;
    dialogs = new ArrayPP<>();
    closeingDialogs = new ArrayPP<>();
  }
  
  public void notifyOf(String message, String title, byte buttons, String yesButtonText, String okButtonText, String noButtonText, String cancelButtonText)
  {
    dialogs.add(new _Legacy_BHNotifierDialog(message, title, buttons, decay, yesButtonText, okButtonText, noButtonText, cancelButtonText, alwaysOnTop));
    final int I = dialogs.length() - 1;
    dialogs.get(I).addWindowListener(new WindowAdapter() {

      public void windowClosing(WindowEvent e)
      {
//        CompAction.corner(dialogs.getFirstItem(), corner);
//        for (int i=1; i < dialogs.length() - 1; i++)
//          dialogs.get(i).getCompAction().goToLocation(dialogs.get, i, i);
//        dialogs.remove(I);
        closeingDialogs.add(I);
      }

      public void windowClosed(WindowEvent e)
      {
        dialogs.get(I).dispose();
        boolean b = false;
        for (int i=1; i < dialogs.length() - 1; i++)
        {
          if (!closeingDialogs.contains(i))
            if (b)
            {
//              int j;
//              for (j=0; j < dialogs.length(); j++)
//                if (!closeingDialogs.containsAll(j))
//                {
                  dialogs.get(i).getCompAction().goToLocation(dialogs.get(i), getComputedLocationFor(i));
//                  break;
//                }
            }
            else
            {
              dialogs.get(i).getCompAction().goToCorner(dialogs.get(i), corner);
              b = true;
            }
        }
        dialogs.remove(I);
        closeingDialogs.remove(I);
      }
    });
    
    if (dialogs.length() > 1)
    {
      dialogs.get(I).setLocation(dialogs.get(dialogs.length() - 2).getX(), dialogs.get(dialogs.length() - 2).getY() +
              (corner.equals(CompAction.ScreenLoc.BOTTOM_RIGHT) || corner.equals(CompAction.ScreenLoc.BOTTOM_LEFT) ?
                -dialogs.get(dialogs.length() - 2).getHeight() :
                dialogs.get(dialogs.length() - 2).getHeight()));
    }
    else
    {
      CompAction.snapToCorner(dialogs.getLastItem(), corner);
    }
    dialogs.get(I).setVisible(true);
  }

  public void setDecay(int newDecay)
  {
    decay = newDecay;
  }

  public int getDecay()
  {
    return decay;
  }

  public void setAlwaysOnTop(boolean top)
  {
    alwaysOnTop = top;
  }

  public boolean isAlwaysOnTop()
  {
    return alwaysOnTop;
  }

  public java.awt.Point getComputedLocationFor(int dialogIndex)
  {
    if (closeingDialogs.contains(dialogIndex))
      throw new IllegalArgumentException("Dialog " + dialogIndex + " is in the process of closing, and is therefore disregarded"
              + " when calculating dialog positions.");
    int trueIndex = getTrueIndex(dialogIndex);
    java.awt.Point poi = new java.awt.Point();
    return poi;
  }

  private int getTrueIndex(int dialogIndex)
  {
    int index = 0;
    for (int i=0; i <= dialogIndex; i++)
      if (!closeingDialogs.contains(i))
        index++;
    return index;
  }
}
