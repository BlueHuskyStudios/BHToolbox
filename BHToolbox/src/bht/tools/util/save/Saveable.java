/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bht.tools.util.save;

/**
 *
 * @author Supuhstar of Blue Husky Studios
 * @version 1.0.0
 */
public abstract interface Saveable
{
  /**
   * A unique separation character. Useful for states that have different parts. Equal to <tt>U+{@value}</tt>
   */
  public static final char SEP = '\u001F';
  /**
   * Sets the unique name of this object. See the Javadoc for <tt>getName</tt> for how unique this name must be.
   * @param newName The name of this object
   * @return the resulting <tt>Saveable</tt> <tt>this</tt>
   */
  public abstract Saveable setSaveName(CharSequence newName);
  /**
   * Returns the unique name of this object, so that calling this method will ALWAYS give the exact same unique name, regardless
   * of when it is called (including separate runtimes). However, its children must NOT return the same <tt>String</tt>, and two
   * objects of this exact same class must not return the same <tt>String</tt> as this one.
   * @return This object's unique name
   */
  public abstract CharSequence getSaveName();
  /**
   * Returns a <tt>String</tt> that contains sufficient information so that, if given to the object's
   * <tt>loadStringFromSave(String savedString)</tt> method, will be able to return all components back to their state at the
   * time when this method was called.
   * @return a <tt>String</tt> that holds the save information for this object
   */
  public abstract CharSequence getStringToSave();
  
  /**
   * Uses the information contained in <tt>savedString</tt> to restore this object to the state it was in when its
   * <tt>getStringToSave()</tt> method was called.
   * @param savedString a <tt>String</tt> with all the information needed to restore this component to a certain state
   * @return the resulting <tt>Saveable</tt> <tt>this</tt> 
   */
  public abstract Saveable loadFromSavedString(CharSequence savedString);
}