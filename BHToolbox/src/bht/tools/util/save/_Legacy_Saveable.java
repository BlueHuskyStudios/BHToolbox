/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bht.tools.util.save;

/**
 *
 * @author Supuhstar
 */
public interface _Legacy_Saveable
{
  /**
   * Returns a <tt>String</tt> that contains sufficient information so that, if given to the class'
   * <tt>loadStringFromSave(String savedString)</tt> method, will be able to return all components back to their state at the
   * time when this method was called.
   * @return a <tt>String</tt> that holds the save information for this object
   */
  public abstract String getStringToSave();
  
  /**
   * Uses the information contained in <tt>savedString</tt> to restore this object to the state it was in when its
   * <tt>getStringToSave()</tt> method was called.
   * @param savedString a <tt>String</tt> with all the information needed to restore this component to a certain state
   * @return the resulting <tt>_Legacy_Saveable</tt> <tt>this</tt> 
   */
  public abstract _Legacy_Saveable loadFromSavedString(CharSequence savedString);
}
