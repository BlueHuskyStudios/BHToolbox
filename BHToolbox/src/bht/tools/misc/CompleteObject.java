package bht.tools.misc;

/**
 * CompleteObject, made for BH Toolbox, is made by and copyrighted to Blue Husky Programming, ©2012 BH-1-PS.<hr>
 * Signifies that the object that implements this interface overrides ALL capabilities of a basic object. This can be used as
 * a way to guarantee that any object that implements this interface also has the {@link java.lang.Object#clone()} and
 * {@link java.lang.Object#finalize()} methods
 * @author Supuhstar of Blue Husky Programming
 * @since Jan 25, 2012
 * @version 1.0.0
 * @see Object
 */
@SuppressWarnings("FinalizeDeclaration")
public interface CompleteObject extends Cloneable
{
  public Object clone() throws CloneNotSupportedException;
  @Override public abstract boolean equals(Object anotherObject);
  abstract void finalize() throws Throwable;
  @Override public abstract int hashCode();
  @Override public abstract String toString();
  
}
