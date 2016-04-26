package bht.tools.util.dynamics;

/**
 * DynObject, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012 BH-1-PS.<hr/>
 * @param <T> The type of object that this dynamically-changing object represents.
 * @author Supuhstar of Blue Husky Programming
 * @since Nov 8, 2012
 * @version 1.0.0
 */
public abstract class DynObject<T>
{
  public abstract T getValue();
  
  public T setValue(T newVal) {return getValue();}
}
