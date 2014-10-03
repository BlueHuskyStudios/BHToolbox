package bht.tools.fx;

import bht.tools.util.BHTimer;
import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.HashMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import static bht.tools.Constants.MAX_WIN_BOUNDS;

/**
 * CompAction, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012. License is default.<hr/>
 * An incarnation of {@code CompAction} wherein ALL animation methods simply use {@link #morphTo(java.awt.Component,
 * java.awt.Rectangle, double, double, double, boolean, java.awt.Rectangle, java.awt.event.ActionListener, boolean, int)} in
 * different ways.
 * <h1>Future Goals:</h1>
 * <ul>
 *  <li>Implement different transition functions (linear, ease in, ease out)</li>
 *  <li>Skip frames or decrease delay if lag is detected</li>
 * </ul>
 * @author Supuhstar of Blue Husky Programming
 * @since Mar 26, 2012
 * @version 3.0.2
 *		- 2014-08-23 (3.0.2) - Kyli Rouge made {@link #testTranslucencySupport()} more efficient
 *		- 2014-08-23 (3.0.1) - Kyli Rouge tweaked and formatted methods
 */
public class CompAction //NOTE: Must be compiled in UTF-8
{
  private BHTimer morpher;
  
  /**
   * Defines the "default" brake, used when none is specified. This tends to give the best results, not too quick to be
   * unnoticeable, but not so long that it is annoying. This value is {@value}.
   */
  public static final double DEF_BRAKE = 4;
  public static final double DEF_FPS = 30;
  
  
  
  public CompAction()
  {
    
  }

  
  
  //<editor-fold defaultstate="collapsed" desc="Basic Static Methods">
  public static void snapToCenter(final Component COMP_TO_CENTER)
  {
    Rectangle parentBounds;
    if (COMP_TO_CENTER instanceof Window)
      parentBounds = new Rectangle(MAX_WIN_BOUNDS);
    else
      parentBounds = COMP_TO_CENTER.getParent().getBounds();
    
    COMP_TO_CENTER.setBounds((int)((parentBounds.width / 2.0) - (COMP_TO_CENTER.getWidth() / 2.0)),
                             (int)((parentBounds.height / 2.0) - (COMP_TO_CENTER.getHeight() / 2.0)),
                             COMP_TO_CENTER.getWidth(),
                             COMP_TO_CENTER.getHeight());
  }

  public static void snapToCorner(final Component COMP_TO_CORNER, ScreenLoc corner)
  {
    Rectangle parent =
		COMP_TO_CORNER instanceof Window
			? new Rectangle(MAX_WIN_BOUNDS)
			: COMP_TO_CORNER.getParent().getBounds();
    int x = 0, y = 0;
    switch (corner)
    {
      case TOP_LEFT:
      case BOTTOM_LEFT:
        y = parent.height - COMP_TO_CORNER.getHeight();
        break;
      case TOP_RIGHT:
      case BOTTOM_RIGHT:
        x = parent.width - COMP_TO_CORNER.getWidth();
        break;
      default:
        throw new AssertionError(corner);
    }
    COMP_TO_CORNER.setLocation(x, y);
  }

  
  private static boolean translucencyResult, testedTranslucency = false;
  
  /**
   * Tests whether translucency is supported on the current platform. This caches the result, so the first call will be exactly
   * the same as subsequent ones. An application restart is required for a different result.
   * 
   * @return {@code true} if we've successfully tested the system to have supported translucency.
   * 
   * @version 1.1.0
   *		- 2014-08-23 (1.1.0) - Kyli Rouge let the result be cached
   */
  @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
  public static boolean testTranslucencySupport()
  {
	  if (testedTranslucency)
		  return translucencyResult;
    boolean b;
    JFrame jf = null;
    try
    {
      jf = new JFrame();
      jf.setLocation(Integer.MIN_VALUE, Integer.MIN_VALUE);
      jf.setVisible(true);
      com.sun.awt.AWTUtilities.setWindowOpacity(jf, 0.5F);
      b = true;
    }
    catch (Throwable t) // Old transparency is not supported
    {
      if (jf != null)
      {
        jf.dispose();
        jf = null;
      }
      try
      {
        jf = new JFrame();
        jf.setLocation(Integer.MIN_VALUE, Integer.MIN_VALUE);
        jf.setVisible(true);
        jf.setOpacity(0.5F);
        b = true;
      }
      catch (Throwable t2) // semitransparency of decorated frames is not supported
      {
        b = false;
      }
    }
    if (jf != null)
    {
      jf.dispose();
      jf = null;
    }
	testedTranslucency = true;
    return translucencyResult = b;
  }
  
  
  private static Window flashingWindow;
  private static Dialog flashingDialog;
  
  @SuppressWarnings("empty-statement")
  public static void flash(Window window, boolean shouldDing)
  {
    for(
		int i = 0,
			l = Integer.MAX_VALUE / 4;
		i < l
			&& flashingWindow != null;
		i++); // Wait reasonably long to ensure that this method isn't already running

    if (window != null)
    {
      flashingWindow = window;
      flashingDialog = new Dialog(flashingWindow);
      flashingDialog.setUndecorated(true);
      flashingDialog.setSize(0, 0);
      flashingDialog.setModal(false);
      flashingDialog.addWindowFocusListener(new java.awt.event.WindowAdapter()
      {
        @Override
        public void windowGainedFocus(java.awt.event.WindowEvent e)
        {
          flashingWindow.requestFocus();
          flashingDialog.setVisible(false);
          super.windowGainedFocus(e);
        }
      });
      flashingWindow.addWindowFocusListener(new java.awt.event.WindowAdapter()
      {
        @Override
        public void windowGainedFocus(java.awt.event.WindowEvent e)
        {
          flashingDialog.setVisible(false);
          super.windowGainedFocus(e);
        }
      });
      if (!flashingWindow.isFocused())
      {
        if (flashingDialog.isVisible())
        {
          flashingDialog.setVisible(false);
        }
        flashingDialog.setLocation(0, 0);
        flashingDialog.setLocationRelativeTo(flashingWindow);
        flashingDialog.setVisible(true);
      }
      flashingWindow.requestFocus();
      flashingWindow.requestFocusInWindow();
    }
    if (shouldDing)
      Toolkit.getDefaultToolkit().beep();
    
    flashingWindow = flashingDialog = null;
  }
  //</editor-fold>
  
  
  
  //<editor-fold defaultstate="collapsed" desc="Morph">
  /**
   * The heart of {@link CompAction}, which performs ALL animations. The heart of THIS method is a {@link BHTimer} object, which
   * performs all animation frames in a separate thread.
   * 
   * @param COMP_TO_MORPH the component to morph.
   * @param TARGET_LOC the location to which the component is moving
   * @param TARGET_TRANSPARENCY the transparency that the component is to have at the end of the animation
   * @param BRAKE the brake of the animation (higher values mean slower animation; 0.0 means that no animation will be
   *        performed. The recommended value for this is {@link #DEF_BRAKE}, or {@value #DEF_BRAKE})
   * @param FRAMES_PER_SECOND the target number of frames to be calculated and applied in any given second. The animation may go
   *        slower than this at different points, but will not go any faster.
   * @param SHOULD_SHOW_WHEN_STARTING if {@code true}, this method will passes {@code true} to the
   *        {@link Component#setVisible(boolean)} method of {@code COMP_TO_MORPH} immediately before starting the animation.
   * @param HIDE_WHEN_FINISHED if {@code true}, this method will passes {@code false} to the
   *        {@link Component#setVisible(boolean)} method of {@code COMP_TO_MORPH} immediately after completing the animation.
   * @param RETURN_LOC the location at to which the component will snap after all animation proceedures are complete.
   * @param END_ACTION the action to be performed after the animation is completed
   * @param SHOULD_EXIT_AFTER_MORPH if {@code true}, then {@code System.exit(EXIT_STATUS} will be called after all end-animation
   *        formalities have been performed
   * @param EXIT_STATUS the status of the exit, unneeded and unused if {@code SHOULD_EXIT_AFTER_MORPH} is {@code false}
   * 
   * @throws NullPointerException if {@code COMP_TO_MORPH} is {@code null}
   * @throws UnsupportedOperationException if translucency is not supported and the {@code TARGET_TRANSPARENCY} is different
   *         from the current transparency value of {@code COMP_TO_MORPH}.
   * 
   * @see BHTimer
   * @see Rectangle
   * @see #testTranslucencySupport()
   * @see #getTransparency(java.awt.Component)
   */
	public void morphTo(
		final Component COMP_TO_MORPH,
		final Rectangle TARGET_LOC,
		final double TARGET_TRANSPARENCY,
		final double BRAKE,
		final double FRAMES_PER_SECOND,
		final boolean SHOULD_SHOW_WHEN_STARTING,
		final boolean HIDE_WHEN_FINISHED,
		final Rectangle RETURN_LOC,
		final ActionListener END_ACTION,
		final boolean SHOULD_EXIT_AFTER_MORPH,
		final int EXIT_STATUS)
	{
		if (COMP_TO_MORPH == null)
			throw new NullPointerException("Cannot manipulate a null component (COMP_TO_MORPH is null)");
		{
			double trans = getTransparency(COMP_TO_MORPH);
			if (!testTranslucencySupport()
				&& trans != TARGET_TRANSPARENCY)
				throw new UnsupportedOperationException(
					"This system does not support decorated translucency, and the requested "
					+ "operation would change the translucency by "
					+ (trans - TARGET_TRANSPARENCY)
					+ " while the target component is decorated");
		}

		if (morpher != null)
		{
			morpher.stop();
			morpher.reset();
		}

		final Rectangle BEGIN = COMP_TO_MORPH.getBounds();
		morpher = new BHTimer(new ActionListener()
			{
				private int count = 0;
				private double l = 0, temp, t;
				private final double BEGIN_T = getTransparency(COMP_TO_MORPH), tDiff = TARGET_TRANSPARENCY - BEGIN_T;
				private Rectangle here = BEGIN.getBounds();
				private final int xDiff = TARGET_LOC.x - BEGIN.x, yDiff = TARGET_LOC.y - BEGIN.y,
						wDiff = TARGET_LOC.width - BEGIN.width, hDiff = TARGET_LOC.height - BEGIN.height,
						MAX_ITER = 128; // Added March 5, 2012 (2.0.11) for Marian
				private final boolean SHOULD_USE_TRANPARENCY = TARGET_TRANSPARENCY != getTransparency(COMP_TO_MORPH);

				@Override public void actionPerformed(ActionEvent actionEvent)
				{
					temp = (l + 0) / 100;
					here.x = (int) (BEGIN.x + (xDiff * temp));
					here.y = (int) (BEGIN.y + (yDiff * temp));
					here.width = (int) (BEGIN.width + (wDiff * temp)) + 1;
					here.height = (int) (BEGIN.height + (hDiff * temp)) + 1;
					COMP_TO_MORPH.setBounds(here); // Added March 26, 2012 (3.0.0) for rebuilding of CompAction

					temp = (t + 0) / 100;
					if (SHOULD_USE_TRANPARENCY)
						setTransparency(COMP_TO_MORPH, BEGIN_T + (tDiff * temp));

					l = l + ((100 - l) / (BRAKE + 1));
					t = t + ((100 - t) / (BRAKE + 1));

					if ( // if we're done
							count++ > MAX_ITER // Added March 5, 2012 (2.0.11) for Marian
									|| l >= 99.99
									|| (here.x == TARGET_LOC.x
										&& here.y == TARGET_LOC.y
										&& here.width == TARGET_LOC.width
										&& here.height == TARGET_LOC.height))
					{
						if (HIDE_WHEN_FINISHED)
							COMP_TO_MORPH.setVisible(false);
						COMP_TO_MORPH.setBounds(RETURN_LOC != null ? RETURN_LOC : TARGET_LOC);
						if (END_ACTION != null)
							END_ACTION.actionPerformed(actionEvent);
						if (SHOULD_EXIT_AFTER_MORPH)
							System.exit(EXIT_STATUS);
						morpher.reset();
					}
				}
			},
			Math.round(1000 / FRAMES_PER_SECOND)
		);

		if (SHOULD_SHOW_WHEN_STARTING)
			COMP_TO_MORPH.setVisible(true);

		morpher.start();
	}
  
	public void morphTo(final Component COMP_TO_MORPH, final Rectangle TARGET_LOC)
	{
		morphTo(COMP_TO_MORPH, TARGET_LOC, 0, DEF_BRAKE, DEF_FPS, false, false, null, null, false, 0);
	}
  
  /*TODO:
	public void morphInto(
		final Component START_COMP,
		final Component END_COMP,
		final double BRAKE,
		final double FRAMES_PER_SECOND,
		final ActionListener END_ACTION,
		final boolean SHOULD_EXIT_AFTER_MORPH,
		final int EXIT_STATUS)
  */
  //</editor-fold>
  
  
  
  //<editor-fold defaultstate="collapsed" desc="Transparency">
  /**
   * Sets the transparency of the given component, where 0.0 is opaque and 1.0 is invisible. 64-bit "{@code double}" precision
   * might not be respected (some methods require 32-bit "{@code float}" precision)
   * @param compToFade the component whose transparency will be changed
   * @param transparency the new transparency for the component
   */
  public static void setTransparency(Component compToFade, double transparency)
  {
    try
    {
      if (compToFade instanceof Window)
      {
        try
        {
          //For JDK 1.7
          ((Window) compToFade).setOpacity(1 - (float)transparency);
        }
        catch (Throwable th)
        {
          //          System.err.println("JRE may be less than 1.7!");
          if (!th.getClass().isInstance(new NoSuchMethodError()))//If it's a NoSuchMethodError, we know simply that the JVM hasn't caught up with Java 7 and the next attempt should work.
            th.printStackTrace();
          try
          {
            //For JDK 1.6
            com.sun.awt.AWTUtilities.setWindowOpacity((Window) compToFade, 1 - (float)transparency);
          }
          catch (Throwable th1)
          {
            throw new UnsupportedOperationException("It seems that transparency is not supported", th1);
          }
        }
      }
      else
      {
        if (compToFade instanceof JComponent)
          ((JComponent)compToFade).setOpaque(false);
        Graphics2D g2 = (Graphics2D) compToFade.getGraphics().create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)transparency));
        compToFade.paint(g2);
        g2.dispose();
      }
    }
    catch (Throwable th)
    {
      throw new UnsupportedOperationException("Transparency might not be supported", th);
    }
  }
  
  /**
   * Returns the transparency of the given component. 0.0 is opaque, 1.0 is invisible, intermediate values are intermediate.
   * If {@link #testTranslucencySupport()} has been called already and returned {@code false}, this will always return {@code 1}
   * 
   * @param comp the component to test
   * @return a {@code float} between 0.0 and 1.0, inclusive
   * 
   * @version 1.0.1
   *		- 2014-08-23 (1.0.1) - Kyli Rouge implemented a shortcut if we already know transparency isn't supported
   */
  public static float getTransparency(Component comp)
  {
	  if (testedTranslucency && !translucencyResult)
		  return 1;
	  
    if (comp instanceof Window)
      try
      {
        return 1 - ((Window) comp).getOpacity();
      }
      catch (Exception e)
      {
        return 1 - com.sun.awt.AWTUtilities.getWindowOpacity((Window) comp);
      }
    else
    {
      return 0;
    }
  }
  
  public void fade(Component COMP_TO_FADE, double NEW_TRANSPARENCY, ActionListener endAction)
  {
    morphTo(COMP_TO_FADE, COMP_TO_FADE.getBounds(), NEW_TRANSPARENCY, DEF_BRAKE, DEF_FPS, false, false, null, null, false, 0);
  }
  
  public void fadeIn(final Component COMP_TO_FADE_IN, ActionListener endAction)
  {
    setTransparency(COMP_TO_FADE_IN, 0);
    COMP_TO_FADE_IN.setVisible(true);
    fade(COMP_TO_FADE_IN, 0, endAction);
  }
  
  
  //<editor-fold defaultstate="collapsed" desc="Ghost">
  public void ghostIn(Component compToGhostIn, ActionListener endAction)
  {
    Rectangle target = compToGhostIn.getBounds();
    compToGhostIn.setBounds(target.x, target.y - MAX_WIN_BOUNDS.height / 16, target.width, target.height);
    setTransparency(compToGhostIn, 1);
    compToGhostIn.setVisible(true);
    morphTo(compToGhostIn, target, 0, DEF_BRAKE, DEF_FPS, true, false, null, endAction, false, 0);
  }

  public void ghostIn(Component compToGhostIn)
  {
    ghostIn(compToGhostIn, null);
  }
  
  public void ghostOut(final Component COMP_TO_GHOST_OUT, final boolean SHOULD_EXIT_AFTER_GHOST, final int EXIT_STATUS)
  {
    Rectangle target = COMP_TO_GHOST_OUT.getBounds();
    target = new Rectangle(target.x, target.y + MAX_WIN_BOUNDS.height / 16, target.width, target.height);
    COMP_TO_GHOST_OUT.setVisible(true);
    morphTo(COMP_TO_GHOST_OUT, target, 1, DEF_BRAKE, DEF_FPS, false, true, null, null, SHOULD_EXIT_AFTER_GHOST, EXIT_STATUS);
  }
  
  public void ghostOut(final Component COMP_TO_GHOST_OUT)
  {
    ghostOut(COMP_TO_GHOST_OUT, false, 0);
  }
  //</editor-fold>

  //</editor-fold>
  
  
  
  //<editor-fold defaultstate="collapsed" desc="Slide">
  public void slideClosed(final Component COMP_TO_SLIDE_CLOSED, final boolean SHOULD_EXIT_AFTER_SLIDING, final int EXIT_STATUS)
  {
    final Dimension MIN = COMP_TO_SLIDE_CLOSED.getMinimumSize();
    COMP_TO_SLIDE_CLOSED.setMinimumSize(new Dimension(0, 0));
    morphTo(COMP_TO_SLIDE_CLOSED, new Rectangle(COMP_TO_SLIDE_CLOSED.getX(), COMP_TO_SLIDE_CLOSED.getY(), 0, 0), 0, DEF_BRAKE,
                                                                                                                    DEF_FPS, false, true, null, new ActionListener()
                                                                                                                    {
                                                                                                                      @Override
                                                                                                                      public void actionPerformed(ActionEvent e)
                                                                                                                      {
                                                                                                                        COMP_TO_SLIDE_CLOSED.setMinimumSize(MIN);
                                                                                                                      }
                                                                                                                    }, SHOULD_EXIT_AFTER_SLIDING, EXIT_STATUS);
  }
  
  public void slideOpen(final Component COMP_TO_SLIDE_OPEN)
  {
    slideOpen(COMP_TO_SLIDE_OPEN, COMP_TO_SLIDE_OPEN.getSize(), DEF_BRAKE, DEF_FPS, null, false, null);
  }
  public void slideOpen(final Component COMP_TO_SLIDE_OPEN, final Dimension TARGET_DIM, final double BRAKE,
                        final double FRAMES_PER_SECOND, Rectangle RETURN_LOC, ActionListener endAction)
  {
    slideOpen(COMP_TO_SLIDE_OPEN, TARGET_DIM, BRAKE, FRAMES_PER_SECOND, RETURN_LOC, false, endAction);
  }
  
  public void slideOpen(final Component COMP_TO_SLIDE_OPEN, final Dimension TARGET_DIM, final double BRAKE,
                        final double FRAMES_PER_SECOND, Rectangle RETURN_LOC, boolean useTranparency, ActionListener endAction)
  {
    COMP_TO_SLIDE_OPEN.setSize(0, 0);
    morphTo(COMP_TO_SLIDE_OPEN,
            new Rectangle(COMP_TO_SLIDE_OPEN.getX(), COMP_TO_SLIDE_OPEN.getY(), TARGET_DIM.width, TARGET_DIM.height),
            0, BRAKE, FRAMES_PER_SECOND, true, false, RETURN_LOC, endAction, false, 0);
  }
  public void slideOut(final Component COMP_TO_SLIDE_OUT, ScreenLoc edge, final double BRAKE,
                       final double FRAMES_PER_SECOND, final ActionListener END_ACTION, final boolean SHOULD_EXIT_AFTER_SLIDING,
                       final int EXIT_STATUS)
  {
    Rectangle parent = COMP_TO_SLIDE_OUT instanceof Window ?
                       new Rectangle(MAX_WIN_BOUNDS) :
                       COMP_TO_SLIDE_OUT.getParent().getBounds();
    int x = 0, y = 0;
    switch (edge)
    {
      case BOTTOM:
      case LEFT:
      case RIGHT:
      case TOP:
        break;
      default:
        throw new AssertionError("\"" + edge + "\" is not an edge");
    }
    
    switch (edge){case BOTTOM: y = parent.height + 16;}
    switch (edge){case RIGHT: x = parent.width + 16;}
    
//    goToLocation(COMP_TO_SLIDE_OUT, new Rectangle(new Point(x, y), COMP_TO_SLIDE_OUT.getSize()), BRAKE, DEF_FPS, );
    goToLocation(COMP_TO_SLIDE_OUT, new Point(x, y), BRAKE, DEF_FPS, false, true, COMP_TO_SLIDE_OUT.getBounds(), END_ACTION,
                 SHOULD_EXIT_AFTER_SLIDING, EXIT_STATUS);
  }

  public void slideOut(final Component COMP_TO_SLIDE_OUT, ScreenLoc edge, final double BRAKE)
  {
    slideOut(COMP_TO_SLIDE_OUT, edge, BRAKE, DEF_FPS, null, false, 0);
  }

  public void slideOut(final Component COMP_TO_SLIDE_OUT, ScreenLoc edge)
  {
    slideOut(COMP_TO_SLIDE_OUT, edge, DEF_BRAKE);
  }

  public void slideIn(final Component COMP_TO_SLIDE_IN, ScreenLoc edge, final double BRAKE,
                      final double FRAMES_PER_SECOND, final ActionListener END_ACTION, final boolean SHOULD_EXIT_AFTER_SLIDING,
                      final int EXIT_STATUS)
  {
    Rectangle parent = COMP_TO_SLIDE_IN instanceof Window ?
                       new Rectangle(MAX_WIN_BOUNDS) :
                       COMP_TO_SLIDE_IN.getParent().getBounds();
    int x = 0, y = 0;
    switch (edge)
    {
      case BOTTOM:
      case BOTTOM_CENTER:
      case LEFT:
      case MIDDLE_LEFT:
      case RIGHT:
      case MIDDLE_RIGHT:
      case TOP:
      case TOP_CENTER:
        break;
      default:
        throw new AssertionError("\"" + edge + "\" is not an edge");
    }
    
    switch (edge)
    {
      case BOTTOM:
      case BOTTOM_CENTER:
        y = parent.height + 16;
    }
    switch (edge)
    {
      case RIGHT:
      case MIDDLE_RIGHT:
        x = parent.width + 16;
    }
    
    Rectangle origin = COMP_TO_SLIDE_IN.getBounds();
    COMP_TO_SLIDE_IN.setLocation(x, y);
    
    goToLocation(COMP_TO_SLIDE_IN, origin.getLocation(), BRAKE, DEF_FPS, false, true, origin.getBounds(), END_ACTION,
                 SHOULD_EXIT_AFTER_SLIDING, EXIT_STATUS);
  }
  
  public void slideIn(final Component COMP_TO_SLIDE_IN, ScreenLoc edge)
  {
    slideIn(COMP_TO_SLIDE_IN, edge, DEF_BRAKE, DEF_FPS, null, false, 0);
  }
  //</editor-fold>
  
  
  
  //<editor-fold defaultstate="collapsed" desc="goToX">
  public void goToCenter(final Component COMP_TO_CENTER)
  {
    boolean b = COMP_TO_CENTER instanceof Window;
    Rectangle parent = COMP_TO_CENTER instanceof Window ? new Rectangle(MAX_WIN_BOUNDS) : COMP_TO_CENTER.getParent().getBounds(),
    center = COMP_TO_CENTER.getBounds();
    center = new Rectangle((int)((parent.width / 2.0) - (center.width / 2.0)),
    (int)((parent.height / 2.0) - (center.height / 2.0)), center.width, center.height);
    goToLocation(COMP_TO_CENTER, center.getLocation());
  }
  
  //<editor-fold defaultstate="collapsed" desc="goToLocation">
  public void goToLocation(final Component COMP_TO_CENTER, Point location)
  {
    goToLocation(COMP_TO_CENTER, location, DEF_BRAKE, DEF_FPS, false);
  }
  public void goToLocation(final Component COMP_TO_CENTER, Point location, final double BRAKE, final double FRAMES_PER_SECOND,
                           final boolean SHOULD_HIDE_AFTER_GOING)
  {
    goToLocation(COMP_TO_CENTER, location, BRAKE, FRAMES_PER_SECOND, false, SHOULD_HIDE_AFTER_GOING, null, null, false, 0);
  }
  public void goToLocation(final Component COMP_TO_CENTER, Point location, final double BRAKE, final double FRAMES_PER_SECOND,
                           boolean shouldShowBeforeGoing, final boolean SHOULD_HIDE_AFTER_GOING, final Rectangle RETURN_LOC,
                           final ActionListener END_ACTION, final boolean SHOULD_EXIT_AFTER_GOING, final int EXIT_STATUS)
  {
    morphTo(COMP_TO_CENTER, new Rectangle(location, COMP_TO_CENTER.getSize()), 0, BRAKE, FRAMES_PER_SECOND,
            shouldShowBeforeGoing, SHOULD_HIDE_AFTER_GOING, RETURN_LOC, END_ACTION, SHOULD_EXIT_AFTER_GOING, EXIT_STATUS);
  }
  //</editor-fold>
  
  public void goToCorner(final Component COMP_TO_CORNER, ScreenLoc corner)
  {
    Rectangle parent = COMP_TO_CORNER instanceof Window ? new Rectangle(MAX_WIN_BOUNDS) : COMP_TO_CORNER.getParent().getBounds();
    int x = 0, y = 0;
    switch (corner)
    {
      case BOTTOM_LEFT:
      case BOTTOM_RIGHT:
      case TOP_LEFT:
      case TOP_RIGHT:
        break;
      default:
        throw new AssertionError("\"" + corner + "\" is not a corner");
    }
    
    if (corner.equals(ScreenLoc.BOTTOM_LEFT) || corner.equals(ScreenLoc.BOTTOM_RIGHT))
      y = parent.height - COMP_TO_CORNER.getHeight();
    if (corner.equals(ScreenLoc.TOP_RIGHT) || corner.equals(ScreenLoc.BOTTOM_RIGHT))
      x = parent.width - COMP_TO_CORNER.getWidth();
    
    morphTo(COMP_TO_CORNER, new Rectangle(x, y, COMP_TO_CORNER.getWidth(), COMP_TO_CORNER.getHeight()));
  }
  //</editor-fold>
  
  
  
  private static HashMap<Component, SnapListener> snapToEdgesDictionary;
  public static void setSnapsToEdges(Component c, int distanceToSnap)
  {
    if (snapToEdgesDictionary == null)
      snapToEdgesDictionary = new HashMap<>();
    if (snapToEdgesDictionary.containsKey(c))
    {
      if (distanceToSnap == 0)
      {
        c.removeComponentListener(snapToEdgesDictionary.get(c));
        return;
      }
	  else
		 snapToEdgesDictionary.get(c).setSnapDistance(distanceToSnap);
    }
    SnapListener sl;
    snapToEdgesDictionary.put(c, sl = new SnapListener(c, distanceToSnap));
    c.addComponentListener(sl);
  }
  
  
  
  //<editor-fold defaultstate="collapsed" desc="inner classes">
	public enum ScreenLoc
	{
		                              TOP,
		      TOP_LEFT,            TOP_CENTER,            TOP_RIGHT,
		LEFT, MIDDLE_LEFT,        MIDDLE_CENTER,       MIDDLE_RIGHT, RIGHT,
		      BOTTOM_LEFT,        BOTTOM_CENTER,       BOTTOM_RIGHT,
			                         BOTTOM
	}
  private static class SnapListener implements ComponentListener
  {
    int dist;
    Component c;
    
    public SnapListener(Component owner, int distanceToSnap)
    {
      c = owner;
      dist = distanceToSnap;
    }
    
    @Override
    public void componentResized(ComponentEvent e)
    {
      snapComponent();
    }
    
    @Override
    public void componentMoved(ComponentEvent e)
    {
      snapComponent();
    }
    
    @Override public void componentShown(ComponentEvent e){}
    
    @Override public void componentHidden(ComponentEvent e){}
    
    private void setSnapDistance(int distanceToSnap)
    {
      dist = distanceToSnap;
    }
    
    private void snapComponent()
    {
      Dimension parent = c instanceof Window ? MAX_WIN_BOUNDS : c.getParent().getSize();
      int x = c.getX(), y = c.getY();
      if (x <= dist && x >= -dist)
        x = 0;
      else if (x >= parent.width - c.getWidth() - dist && x <= parent.width - c.getWidth() + dist)
        x = parent.width - c.getWidth();
      if (y <= dist && y >= -dist)
        y = 0;
      else if (y >= parent.height - c.getHeight() - dist && y <= parent.height - c.getHeight() + dist)
        y = parent.height - c.getHeight();
      
      c.setBounds(x, y, c.getWidth(), c.getHeight());
    }
  }
  //</editor-fold>
}
