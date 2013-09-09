/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bht.tools.fx;

import bht.tools.misc.YesNoBox;
import bht.tools.util.ArrayPP;
import java.awt.Frame;
import java.awt.Window;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeListener;

/**
 * A convenience class for changing the Java Look-And-Feel. Place a <tt>byte</tt> constant in the
 * <tt>setLookAndFeel(byte laf)</tt> method and it will change the LAF for all open windows.
 * @author Supuhstar
 * @since 1.6_23
 * @version 1.1.1
 */
public class LookAndFeelChanger
{
  public static final byte SYSTEM = -0x2;
  public static final byte DEFAULT = -0x1;
  public static final byte METAL = 0x0;
  public static final byte NIMBUS = 0x1;
  public static final byte CDE = 0x2;
  public static final byte MOTIF = CDE;
  public static final byte WINDOWS = 0x3;
  public static final byte WINDOWS_CLASSIC = 0x4;
  public static final ArrayPP<Byte> LAFs = new ArrayPP<>(METAL, NIMBUS, MOTIF, WINDOWS, WINDOWS_CLASSIC);
  private static final LookAndFeel DEFAULT_LAF = UIManager.getLookAndFeel();
  
  private static final ArrayPP<LAFChangeListener> CLs = new ArrayPP<>();
//  public static final byte BH = 0x10;


  /**
   * Applies a certain <tt>javax.swing.LookAndFeel</tt> to all windows in the application based on a predefined <tt>byte</tt>
   * mask. (see <tt>byte</tt> constants provided)..
   * @param laf the <tt>byte</tt> mask for one of the 5 predefined <tt>javax.swing.LookAndFeel</tt>s to be applied to the
   * application.
   * @param force if <tt>true</tt>, will try to change the <tt>LookAndFeel</tt> even if the provided one is the currently used one.
   * @see #setLookAndFeel(javax.swing.LookAndFeel, boolean)
   */
  @SuppressWarnings({"UseSpecificCatch", "UseOfSystemOutOrSystemErr"})
  public static void setLookAndFeel(byte laf, boolean force)
  {
    try
    {
      setLookAndFeel(getLookAndFeel(laf), force);
    }
    catch (Throwable t)
    {
      System.err.println("Failed to set the look-and-feel. Please alert Blue Husky Studios in any way listed in http://supuh.wikia.com/wiki/Supuhstar");
      t.printStackTrace();
      try
      {
        UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
      }
      catch (Throwable t2)
      {
        if (YesNoBox.bool("Error when setting Look-And-Feel. Quit program for safety?", "Error when setting LAF", YesNoBox.ERROR_MES))
          System.exit(1);
      }
    }
  }

  /**
   * Applies a certain <tt>javax.swing.LookAndFeel</tt> to all windows in the application.
   * @param LAFClassName the <tt>String</tt> form of the fully-qualified class name of the <tt>javax.swing.LookAndFeel</tt> to
   * be applied to the application.
   * @param force if <tt>true</tt>, will try to change the <tt>LookAndFeel</tt> even if the provided one is the currently used one.
   * @throws UnsupportedLookAndFeelException if the automatic reversion back to the last successfully applied LAF is
   * unsuccessful or if <tt>lnf.isSupportedLookAndFeel()</tt> is false
   * @throws ClassNotFoundException if the <tt>LookAndFeel</tt> class could not be found
   * @throws InstantiationException if a new instance of the class couldn't be created
   * @throws IllegalAccessException if the class or initializer isn't accessible
   * @throws ClassCastException if <tt>LAFClassName</tt> does not identify a class that extends <tt>LookAndFeel</tt>
   * @deprecated for guaranteed results, use the version of this method that works on <tt>byte</tt> masks.
   * @see #setLookAndFeel(java.lang.CharSequence, boolean, java.lang.Object) 
   */
  public static void setLookAndFeel(CharSequence LAFClassName, boolean force) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    setLookAndFeel(LAFClassName, force, null);
  }

  /**
   * Applies a certain <tt>javax.swing.LookAndFeel</tt> to all windows in the application. Each window's {@link Window#pack()}
   * method is called to avoid unwanted distorting from differing component sizes determined by different Look-And-Feels. If the
   * window is a {@link Frame}, then its state before the change is remembered and set again after the change.
   * @param LAFClassName the <tt>String</tt> form of the fully-qualified class name of the <tt>javax.swing.LookAndFeel</tt> to
   * be applied to the application.
   * @param force if <tt>true</tt>, will try to change the <tt>LookAndFeel</tt> even if the provided one is the currently used
   * one.
   * @param invoker the object that called this method
   * @throws UnsupportedLookAndFeelException if the automatic reversion back to the last successfully applied LAF is
   * unsuccessful or if <tt>lnf.isSupportedLookAndFeel()</tt> is false
   * @throws ClassNotFoundException if the <tt>LookAndFeel</tt> class could not be found
   * @throws InstantiationException if a new instance of the class couldn't be created
   * @throws IllegalAccessException if the class or initializer isn't accessible
   * @throws ClassCastException if <tt>LAFClassName</tt> does not identify a class that extends <tt>LookAndFeel</tt>
   * @deprecated for guaranteed results, use the version of this method that works on <tt>byte</tt> masks.
   */
  public static void setLookAndFeel(CharSequence LAFClassName, boolean force, Object invoker) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException
  {
//    System.out.println(UIManager.getLookAndFeel().getClass().getName() + ".equals(" + LAFClassName + ")");
    if (!force && UIManager.getLookAndFeel().getClass().getName().equals(LAFClassName))
      return;
//    if (!YesNoBox.bool("Changing the Look-And-Feel of the program to " + LAFClassName + " changes how the program looks and somtimes how it works.\n"
//            + "This is not always a successbul or beneficiary change. Are you sure you want to change it?"))
//      return;
    LookAndFeel last = UIManager.getLookAndFeel();
    try
    {
      UIManager.setLookAndFeel(LAFClassName.toString());
      Window windows[] = Window.getWindows();
      boolean f;
      int s;
      for (int i=0; i < windows.length; i++)
      {
        java.awt.Rectangle r = new java.awt.Rectangle(windows[i].getBounds());//Changed to new rather than assignment {Feb 22, 2012 (1.1.1) for Marian}
        f = windows[i] instanceof java.awt.Frame;
        s = f ? ((java.awt.Frame)(windows[i])).getState() : java.awt.Frame.NORMAL;
//        windows[i].pack();//Commented out Feb 27, 2012 (1.1.1) for Marian - Though it makes the window look better in the end, it introduces unneeded lag and interfered with CompAction animations
        javax.swing.SwingUtilities.updateComponentTreeUI(windows[i]);
        if (f)
          ((java.awt.Frame)(windows[i])).setState(s);
        if (!f || s != java.awt.Frame.MAXIMIZED_BOTH)//"f &&" changed to "!f ||" {Feb 22, 2012 (1.1.1) for Marian}
          windows[i].setBounds(r);
        
      }
    }
    catch (UnsupportedLookAndFeelException ex)
    {
      UIManager.setLookAndFeel(last);
    }
    catch (Throwable t)
    {
      try
      {
        UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
      }
      catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException t2)
      {
        if (YesNoBox.bool("Error when setting Look-And-Feel. Quit program for safety?",
                          "Error when setting LAF",
                          YesNoBox.ERROR_MES))
          System.exit(1);
      }
      catch (Throwable t2)
      {
        if (YesNoBox.bool("Very unexpected error when setting Look-And-Feel. Quit program for safety?",
                          "Error when setting LAF", YesNoBox.ERROR_MES))
          System.exit(1);
      }
    }
    
    LAFChangeEvent evt = new LAFChangeEvent(invoker, last, getLookAndFeel(LAFClassName));
    for (LAFChangeListener cl : CLs)
      cl.lafChanged(evt);
  }
  
  /**
   * Applies a certain <tt>javax.swing.LookAndFeel</tt> to all windows in the application.
   * @param laf the <tt>javax.swing.LookAndFeel</tt> to be applied to the application.
   * @param force if <tt>true</tt>, will try to change the <tt>LookAndFeel</tt> even if the provided one is the currently used one.
   * @throws UnsupportedLookAndFeelException if the automatic reversion back to the last successfully applied LAF is
   * unsuccessful or if <code>lnf.isSupportedLookAndFeel()</code> is false
   * @throws ClassNotFoundException if the <code>LookAndFeel</code> class could not be found
   * @throws InstantiationException if a new instance of the class couldn't be created
   * @throws IllegalAccessException if the class or initializer isn't accessible
   * @throws ClassCastException if <tt>LAFClassName</tt> does not identify a class that extends <tt>LookAndFeel</tt>
   * @deprecated for guaranteed results, use the version of this method that works on <tt>byte</tt> masks.
   * @see #setLookAndFeel(java.lang.CharSequence, boolean) 
   */
  public static void setLookAndFeel(LookAndFeel laf, boolean force) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    setLookAndFeel(laf.getClass().getName(), force);
  }

  /**
   * Applies a certain <tt>javax.swing.LookAndFeel</tt> to all windows in the application.
   * @param lafi the <tt>javax.swing.UIManager.LookAndFeelInfo</tt> to be interpreted into a <tt>javax.swing.LookAndFeel</tt>
   * and applied to the application.
   * @param force if <tt>true</tt>, will try to change the <tt>LookAndFeel</tt> even if the provided one is the currently used one.
   * @throws UnsupportedLookAndFeelException if the automatic reversion back to the last successfully applied LAF is
   * unsuccessful or if <code>lnf.isSupportedLookAndFeel()</code> is false
   * @throws ClassNotFoundException if the <code>LookAndFeel</code> class could not be found
   * @throws InstantiationException if a new instance of the class couldn't be created
   * @throws IllegalAccessException if the class or initializer isn't accessible
   * @throws ClassCastException if <tt>LAFClassName</tt> does not identify a class that extends <tt>LookAndFeel</tt>
   * @deprecated for guaranteed results, use the version of this method that works on <tt>byte</tt> masks.
   */
  public static void setLookAndFeel(javax.swing.UIManager.LookAndFeelInfo lafi, boolean force) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    setLookAndFeel(lafi.getClassName(), force);
  }

  
  /**
   * Applies a certain <tt>javax.swing.LookAndFeel</tt> to all windows in the application based on a predefined <tt>byte</tt>
   * mask. (see <tt>byte</tt> constants provided)
   * @param laf the <tt>byte</tt> mask for one of the 5 predefined <tt>javax.swing.LookAndFeel</tt>s to be applied to the
   * application.
   */
  public static void setLookAndFeel(byte laf)
  {
    setLookAndFeel(laf, false);
  }

  /**
   * Applies a certain <tt>javax.swing.LookAndFeel</tt> to all windows in the application.
   * @param LAFClassName the <tt>String</tt> form of the fully-qualified class name of the <tt>javax.swing.LookAndFeel</tt> to
   * be applied to the application.
   * @throws UnsupportedLookAndFeelException if the automatic reversion back to the last successfully applied LAF is
   * unsuccessful or if <tt>lnf.isSupportedLookAndFeel()</tt> is false
   * @throws ClassNotFoundException if the <tt>LookAndFeel</tt> class could not be found
   * @throws InstantiationException if a new instance of the class couldn't be created
   * @throws IllegalAccessException if the class or initializer isn't accessible
   * @throws ClassCastException if <tt>LAFClassName</tt> does not identify a class that extends <tt>LookAndFeel</tt>
   */
  public static void setLookAndFeel(CharSequence LAFClassName) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    setLookAndFeel(LAFClassName, false);
  }
  
  /**
   * Applies a certain <tt>javax.swing.LookAndFeel</tt> to all windows in the application.
   * @param laf the <tt>javax.swing.LookAndFeel</tt> to be applied to the application.
   * @throws UnsupportedLookAndFeelException if the automatic reversion back to the last successfully applied LAF is
   * unsuccessful or if <code>lnf.isSupportedLookAndFeel()</code> is false
   * @throws ClassNotFoundException if the <code>LookAndFeel</code> class could not be found
   * @throws InstantiationException if a new instance of the class couldn't be created
   * @throws IllegalAccessException if the class or initializer isn't accessible
   * @throws ClassCastException if <tt>LAFClassName</tt> does not identify a class that extends <tt>LookAndFeel</tt>
   */
  public static void setLookAndFeel(LookAndFeel laf) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    setLookAndFeel(laf, false);
  }

  /**
   * Applies a certain <tt>javax.swing.LookAndFeel</tt> to all windows in the application.
   * @param lafi the <tt>javax.swing.UIManager.LookAndFeelInfo</tt> to be interpreted into a <tt>javax.swing.LookAndFeel</tt>
   * and applied to the application.
   * @throws UnsupportedLookAndFeelException if the automatic reversion back to the last successfully applied LAF is
   * unsuccessful or if <code>lnf.isSupportedLookAndFeel()</code> is false
   * @throws ClassNotFoundException if the <code>LookAndFeel</code> class could not be found
   * @throws InstantiationException if a new instance of the class couldn't be created
   * @throws IllegalAccessException if the class or initializer isn't accessible
   * @throws ClassCastException if <tt>LAFClassName</tt> does not identify a class that extends <tt>LookAndFeel</tt>
   */
  public static void setLookAndFeel(javax.swing.UIManager.LookAndFeelInfo lafi) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    setLookAndFeel(lafi, false);
  }
  
  /**
   * Finds and returns the currently used <tt>LookAndFeel</tt>
   * @return the currently used <tt>LookAndFeel</tt>
   */
  public static LookAndFeel getLookAndFeel()
  {
    return javax.swing.UIManager.getLookAndFeel();
  }

  @SuppressWarnings("UseOfSystemOutOrSystemErr")
  private static byte getLookAndFeelByte(LookAndFeel lookAndFeel)
  {
    if (lookAndFeel instanceof javax.swing.plaf.metal.MetalLookAndFeel)
      return METAL;
    try
    {
      if (lookAndFeel instanceof javax.swing.plaf.nimbus.NimbusLookAndFeel)
        return NIMBUS;
    }
    catch (Throwable t)
    {
      if (lookAndFeel instanceof com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel)
        return NIMBUS;
    }
    if (lookAndFeel instanceof com.sun.java.swing.plaf.motif.MotifLookAndFeel)
      return MOTIF;
    if (lookAndFeel instanceof com.sun.java.swing.plaf.windows.WindowsLookAndFeel)
      return WINDOWS;
    if (lookAndFeel instanceof com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel)
      return WINDOWS_CLASSIC;
    if (lookAndFeel.equals(DEFAULT_LAF))
      return DEFAULT;
    if (lookAndFeel.getClass().getName().equals(UIManager.getSystemLookAndFeelClassName()))
      return SYSTEM;
    System.err.println("Undefined byte (" + lookAndFeel + "). For custom Look-And-Feels, use setLookAndFeel(javax.swing."
            + "LookAndFeel laf, java.awt.Window w). Returning default LAF...");
    return DEFAULT;
  }
  
  public static byte getLookAndFeelByte(CharSequence lafClassName) throws ClassNotFoundException,
                                                                    InstantiationException,
                                                                    IllegalAccessException
  {
    return getLookAndFeelByte(getLookAndFeel(lafClassName));
  }
  
  public byte getLookAndFeelByte() throws ClassNotFoundException,
                                          InstantiationException,
                                          IllegalAccessException
  {
    return getLookAndFeelByte(getLookAndFeel());
  }
  
  /**
   * Returns the description of the currently applied <tt>LookAndFeel</tt>
   * @return the <tt>String</tt> description of the currently applied <tt>LookAndFeel</tt>
   */
  public String getLookAndFeelDescription()
  {
    return getLookAndFeelDescription(getLookAndFeel());
  }
  
  /**
   * Returns the <tt>LookAndFeel</tt> corresponding to a predefined mask. (see <tt>byte</tt> constants provided)
   * @param laf the <tt>byte</tt> mask representing the <tt>LookAndFeel</tt> to be returned
   * @return the <tt>LookAndFeel</tt> represented by the <tt>byte</tt> mask
   */
  @SuppressWarnings({"UseSpecificCatch", "UseOfSystemOutOrSystemErr"})
  public static LookAndFeel getLookAndFeel(byte laf)
  {
      switch (laf)
      {
        case DEFAULT:
          return DEFAULT_LAF;
        case SYSTEM:
          try
          {
            return getLookAndFeel(UIManager.getSystemLookAndFeelClassName());
          }
          catch (Throwable t)
          {
            t.printStackTrace();
            setLookAndFeel(DEFAULT);
          }
        case METAL:
          return new javax.swing.plaf.metal.MetalLookAndFeel();
        case NIMBUS:
          try
          {
            return new javax.swing.plaf.nimbus.NimbusLookAndFeel();
          }
          finally
          {
            return new com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel();
          }
        case MOTIF:
          return new com.sun.java.swing.plaf.motif.MotifLookAndFeel();
        case WINDOWS:
          return new com.sun.java.swing.plaf.windows.WindowsLookAndFeel();
        case WINDOWS_CLASSIC:
          return new com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel();
        default:
          System.err.println("Undefined byte (" + laf + "). For custom Look-And-Feels, use setLookAndFeel(javax.swing."
                  + "LookAndFeel laf, java.awt.Window w). Returning default LAF...");
          return getLookAndFeel(DEFAULT);
      }
  }
  
  /**
   * Returns the description of the <tt>LookAndFeel</tt> represented by a predefined mask.
   * @param laf the <tt>byte</tt> mask representing the <tt>LookAndFeel</tt> whose description is being fetched.
   * (see <tt>byte</tt> constants provided)
   * @return the <tt>String</tt> description of the <tt>LookAndFeel</tt> represented by the <tt>byte</tt> mask parameter
   */
  public static String getLookAndFeelDescription(byte laf)
  {
    return getLookAndFeel(laf).getDescription();
  }
  
  /**
   * Returns the description of the <tt>LookAndFeel</tt> provided
   * @param laf the <tt>LookAndFeel</tt> whose description is being fetched.
   * @return the <tt>String</tt> description of the <tt>LookAndFeel</tt> provided
   * @deprecated for guaranteed results, use the version of this method that works on <tt>byte</tt> masks.
   */
  public static String getLookAndFeelDescription(LookAndFeel laf)
  {
    return laf.getDescription();
  }
  
  

  /**
   * Attempts to create a <tt>javax.swing.LookAndFeel</tt> given the <tt>javax.swing.UIManager.LookAndFeelInfo</tt> describing
   * the <tt>javax.swing.LookAndFeel</tt>
   * @param lafi the <tt>javax.swing.UIManager.LookAndFeelInfo</tt> to be interpreted into a <tt>javax.swing.LookAndFeel</tt>
   * @return the interpreted <tt>javax.swing.LookAndFeel</tt>
   * @throws ClassNotFoundException if the provided <tt>String</tt> does not point to a real class
   * @throws  IllegalAccessException  if the class or its nullary
   *               constructor is not accessible.
   * @throws  InstantiationException
   *               if this <code>Class</code> represents an abstract class,
   *               an interface, an array class, a primitive type, or void;
   *               or if the class has no nullary constructor;
   *               or if the instantiation fails for some other reason.
   * @throws  ExceptionInInitializerError if the initialization
   *               provoked by this method fails.
   * @throws  SecurityException
   *             If a security manager, <i>s</i>, is present and any of the
   *             following conditions is met:
   *             <ul>
   *             <li> invocation of
   *             <tt>{@link SecurityManager#checkMemberAccess
   *             s.checkMemberAccess(this, Member.PUBLIC)}</tt> denies
   *             creation of new instances of this class
   *             <li> the caller's class loader is not the same as or an
   *             ancestor of the class loader for the current class and
   *             invocation of <tt>{@link SecurityManager#checkPackageAccess
   *             s.checkPackageAccess()}</tt> denies access to the package
   *             of this class
   *             </ul>
   */
  public static LookAndFeel getLookAndFeel(javax.swing.UIManager.LookAndFeelInfo lafi) throws ClassNotFoundException,
          InstantiationException, IllegalAccessException
  {
    return getLookAndFeel(lafi.getClassName());
  }

  /**
   * Attempts to create a <tt>javax.swing.LookAndFeel</tt> given the name of the class
   * @param lafClassName the <tt>String</tt> to be interpreted into a <tt>javax.swing.LookAndFeel</tt>\
   * @return the interpreted <tt>javax.swing.LookAndFeel</tt>
   * @throws ClassNotFoundException if the provided <tt>String</tt> does not point to a real class
   * @throws  IllegalAccessException  if the class or its nullary
   *               constructor is not accessible.
   * @throws  InstantiationException
   *               if this <code>Class</code> represents an abstract class,
   *               an interface, an array class, a primitive type, or void;
   *               or if the class has no nullary constructor;
   *               or if the instantiation fails for some other reason.
   * @throws  ExceptionInInitializerError if the initialization
   *               provoked by this method fails.
   * @throws  SecurityException
   *             If a security manager, <i>s</i>, is present and any of the
   *             following conditions is met:
   *             <ul>
   *             <li> invocation of
   *             <tt>{@link SecurityManager#checkMemberAccess
   *             s.checkMemberAccess(this, Member.PUBLIC)}</tt> denies
   *             creation of new instances of this class
   *             <li> the caller's class loader is not the same as or an
   *             ancestor of the class loader for the current class and
   *             invocation of <tt>{@link SecurityManager#checkPackageAccess
   *             s.checkPackageAccess()}</tt> denies access to the package
   *             of this class
   *             </ul>
   */
  public static LookAndFeel getLookAndFeel(CharSequence lafClassName) throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
//    System.out.println(lafClassName);
    Class lafClass = Class.forName(lafClassName.toString());
    return (LookAndFeel)(lafClass.newInstance());
  }
  
  /**
   * Adds the given {@link ChangeListener} to the array of change listeners that are triggered whenever the Look-And-Feel is
   * changed with the {@link LookAndFeelChanger}
   * @since 1.1.0 (Jan 16, 2012)
   * @param cl the {@code ChangeListener} to add to the array of change listeners
   */
  public static void addLAFChangeListener(LAFChangeListener cl)
  {
    CLs.add(cl);
  }
  
  /**
   * Removes all instances of the given {@link ChangeListener} from the array of change listeners that are triggered whenever
   * the Look-And-Feel is changed with the {@link LookAndFeelChanger}
   * @since 1.1.0 (Jan 16, 2012)
   * @param cl the {@code ChangeListener} to add to the array of change listeners
   */
  public static void removeLAFChangeListener(LAFChangeListener cl)
  {
    CLs.remove(cl, true);
  }

  public static interface LAFChangeListener
  {
    public abstract void lafChanged(LAFChangeEvent e);
  }

  public static class LAFChangeEvent
  {
    private Object i;
    private LookAndFeel o, n;
    public LAFChangeEvent(Object invoker, LookAndFeel oldLookAndFeel, LookAndFeel newLookAndFeel)
    {
      i = invoker;
      o = oldLookAndFeel;
      n = newLookAndFeel;
    }
    
    public Object getInvoker()
    {
      return i;
    }
    
    public LookAndFeel getNewLookAndFeel()
    {
      return n;
    }
    
    public LookAndFeel getOldLookAndFeel()
    {
      return o;
    }
  }
}
