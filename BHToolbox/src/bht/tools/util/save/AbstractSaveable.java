package bht.tools.util.save;

/**
 * AbstractSaveable, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012 BH-1-PS.<hr/>
 * @author Supuhstar of Blue Husky Programming
 * @since Feb 2, 2012
 * @version 1.0.0
 */
public abstract class AbstractSaveable<T> implements Saveable //NOTE: Must be compiled in UTF-8
{
  protected T s;
  protected CharSequence n;

  public AbstractSaveable(T initState, CharSequence initName)
  {
    s = initState;
    n = initName;
  }

  @Override
  public CharSequence getSaveName()
  {
    return n;
  }

  @Override
  public Saveable setSaveName(CharSequence newName)
  {
    n = newName;
    return this;
  }
  
  /**
   * Returns the current state of the saveable object
   * @return the current state of the saveable object
   */
  public T getState()
  {
    return s;
  }
  
  /**
   * Changes the current state of the saveable object.
   * @param newState the new state of the saveable object
   * @return {@code this} object
   */
  public Saveable setState(T newState)
  {
    s = newState;
    return this;
  }
  
  /**
   * Returns a {@code String} representation of this {@link Saveable}, which follows this format: {@code saveName + " => " +
   * state}. For instance, the {@link SaveableBoolean} used by {@link StateSaver} to save whether auto-save-load is enabled
   * could return "{@code autoSL => true}"
   * @return a {@code String} representation of this {@link Saveable}
   */
  @Override
  public String toString()
  {
    return n + " => " + s;
  }
}
