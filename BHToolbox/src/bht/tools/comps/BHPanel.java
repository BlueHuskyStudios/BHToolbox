/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bht.tools.comps;

import bht.tools.fx.Colors;
import bht.tools.fx.CompAction;
import bht.tools.util.save.Saveable;
import java.awt.Container;
import java.awt.Window;

/**
 * BHPanel, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012 BH-1-PS.<hr>
 * @author ben_sims2 of Blue Husky Programming
 * @since Jan 31, 2012
 * @version 1.0.0
 */
public class BHPanel extends Container implements BHComponent//NOTE: Must be compiled in UTF-8
{
  private CharSequence saveName;
  private Colors c;
  private CompAction ca;
  
  public BHPanel()
  {
    c = BHCompUtilities.getColorsFor(getParent());
    if (c == null)
      c = new Colors();
    ca = BHCompUtilities.getCompActionFor(getParent());
  }
  
  public Window getParentWindow()
  {
    Container c = this;
    while (!(c instanceof Window))
    {
      if ((c = c.getParent()) == null)
        return null;
    }
    return (Window)c;
  }

  @Override
  public CompAction getCompAction()
  {
    return ca;
  }

  @Override
  public Colors getColors()
  {
    return c;
  }

  @Override
  public BHPanel setSaveName(CharSequence newName)
  {
    saveName = newName;
    return this;
  }

  @Override
  public CharSequence getSaveName()
  {
    return saveName;
  }

  @Override
  public CharSequence getStringToSave()
  {
    throw new UnsupportedOperationException("Not implemnted in base container BHPanel.");
  }

  @Override
  public Saveable loadFromSavedString(CharSequence savedString)
  {
    throw new UnsupportedOperationException("Not implemnted in base container BHPanel.");
  }
}
