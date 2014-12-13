/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bht.tools.comps;

import bht.tools.fx.Colors;
import bht.tools.fx.CompAction;
import bht.tools.util.save.Saveable;

/**
 * The definition of what all major GUI components should have.
 * @author Supuhstar of Blue Husky Studios
 */
public interface BHComponent extends Saveable
{
  public abstract CompAction getCompAction();

  public abstract Colors getColors();
}
