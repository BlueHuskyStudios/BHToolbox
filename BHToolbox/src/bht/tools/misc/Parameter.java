package bht.tools.misc;

/**
 * Parameter, made for BHToolbox, is copyright Blue Husky Programming, ©2013 <HR/>
 * 
 * @author csu of Blue Husky Programming
 * @version 1.0.0
 * @since 2013-09-09
 */
public abstract class Parameter extends Argument
{

	public Parameter(CharSequence initTriggerWord, Parameter[] initParameters)
	{
		super(initTriggerWord, initParameters);
	}
	
	public abstract boolean isOptional();
}
