package bht.tools.util.dynamics;

/**
 * DynInt, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012 BH-1-PS.<hr/>
 * @author Supuhstar of Blue Husky Programming
 * @since Nov 8, 2012
 * @version 1.0.0
 */
public abstract class DynInt extends DynObject<Integer> //NOTE: Must be compiled in UTF-8
{
  @Override
  public abstract Integer getValue();
  
  public int getPureInt(){ return getValue(); }
}
