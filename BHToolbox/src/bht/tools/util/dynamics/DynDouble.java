package bht.tools.util.dynamics;

/**
 * DynDouble, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012. License is default.<hr/>
 * @author Supuhstar of Blue Husky Programming
 * @since Nov 8, 2012
 * @version 1.0.0
 */
public abstract class DynDouble extends DynObject<Double> //NOTE: Must be compiled in UTF-8
{
  @Override
  public abstract Double getValue();
  
  public double getPureInt(){ return getValue(); }
}
