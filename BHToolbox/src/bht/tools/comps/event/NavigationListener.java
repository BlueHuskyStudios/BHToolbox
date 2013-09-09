package bht.tools.comps.event;

/**
 * NavigationListener, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012. License is default.<hr/>
 * @author Supuhstar of Blue Husky Programming
 * @since Apr 11, 2012
 * @version 1.0.0
 */
public interface NavigationListener extends java.util.EventListener //NOTE: Must be compiled in UTF-8
{
  public void willNavigate(NavigationEvent evt);
  public void didNavigate(NavigationEvent evt);
}
