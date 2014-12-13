package bht.tools.comps;

import bht.tools.fx.Colors;
import bht.tools.fx.CompAction;
import bht.tools.util.save.Saveable;
import javax.swing.JInternalFrame;

/**
 *
 * @author Supuhstar
 */
public class BHInternalFrame extends JInternalFrame implements BHComponent
{
  CharSequence name;
  CompAction ca = new CompAction();
  
  public CompAction getCompAction()
  {
    return ca;
  }

  @Override
  public Saveable setSaveName(CharSequence newName)
  {
    name = newName;
    return this;
  }

  @Override
  public CharSequence getSaveName()
  {
    return name;
  }

  @Override
  public CharSequence getStringToSave()
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   * @deprecated Not supported yet.
   * @throws UnsupportedOperationException always
   */
  @Override
  public Saveable loadFromSavedString(CharSequence savedString) throws UnsupportedOperationException 
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Colors getColors()
  {
    return BHCompUtilities.getColorsFor(getParent());
  }
}
