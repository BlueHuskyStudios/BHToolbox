package bht.tools.util.dynamics;

/**
 * ObjectFactory, made for BHToolbox, is copyright Blue Husky Programming ©2015 GPLv3 <hr/>
 * 
 * Allows for dynamic creation of objects on-the-fly, so instead of passing a set value, you can have that value created later
 * 
 * @param <T> The type of object that will be created
 * 
 * @author Kyli of Blue Husky Programming
 * @version 1.0.0
 *		- 2015-01-02 (1.0.0) - Kyli created ObjectFactory
 * @since 2015-01-02
 */
public interface ObjectFactory<T>
{
	public abstract T create();
}
