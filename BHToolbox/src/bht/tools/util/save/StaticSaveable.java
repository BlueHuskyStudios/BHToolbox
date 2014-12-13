/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bht.tools.util.save;

import bht.tools.util.save.Saveable;

/**
 *
 * @author Supuhstar of Blue Husky Studios
 */
public interface StaticSaveable<T> extends Saveable
{
  public T loadStaticallyFromSavedString(CharSequence savedString);

  /**
   * @deprecated use <tt>loadStaticallyFromSavedString(CharSequence savedString)</tt>, instead
   */
  @Override
  public StaticSaveable<T> loadFromSavedString(CharSequence savedString);
}
