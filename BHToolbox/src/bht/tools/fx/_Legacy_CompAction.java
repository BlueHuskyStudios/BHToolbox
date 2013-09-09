/*
 * Plans for version 3:
 *  ! Streamline animations to skip frames if they go too slowly
 */

package bht.tools.fx;

import bht.tools.util.math.Numbers;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;

/**
 * A utility that performs actions (such as animations) upon a component. <br /><br />
 * <b>ATTENTION:</b> Formerly the <tt>FrameAction</tt> class, now works more than just windows. Most methods still work on
 * windows, but will now also perform the same actions on any component.
 * <b>A note to former developers:</b> Many internal things have changed since the previous version. You can still use any of
 * the non-moving methods in a static context, but you must make a new <tt>CompAction</tt> object for such methods as
 * <tt>slideOpen</tt> and <tt>goToLocation</tt>. This doubles as both a safety precaution (to avoid problems and glitches such
 * as twitchy or flashing components) and so that you can combine effects. For instance, you can tell a component to both go to
 * a certain location and fade away (like in the new <tt>ghostIn</tt> and <tt>ghostOut</tt> methods), and you will not get any
 * strange or unpredicted effects.
 * @version 2.0.11
 * @author Blue Husky Programming, © 2011
 */
public class _Legacy_CompAction
{
  /**
   * <tt>byte</tt> mask to be used with the <tt>corner(Component comp, byte corner)</tt> and <tt>goToCorner(final Component comp
   * , final byte CORNER)</tt> methods to place the component in the corresponding corner of its parent
   */
  public static final byte TOP_LEFT = 0, TOP_RIGHT = 1, BOTTOM_RIGHT = 2, BOTTOM_LEFT = 3;
  /**
   * <tt>byte</tt> mask to be used with the <tt>slideOut(Component comp, byte location)</tt> and <tt>slideIn(Component comp,
   * byte location)</tt> methods to slide the component to or from the corresponding side of its parent
   */
  public static final byte TOP = 4, RIGHT = 5, BOTTOM = 6, LEFT = 7;
  /**
   * The recommended value for methods that take a <tt>BRAKE</tt> parameter, such as <tt>fadeAway(Component comp, float BRAKE)</tt>
   */
  public static final float DEF_BRAKE = 4;
  public static final int DEF_FPS = 30;
  private static int screen = 0, offset;
  private static volatile int count = 0, snapDists[];
  private javax.swing.Timer mover, slider, fader;
  private static volatile boolean noOffset = false;
  private static volatile Component thisComponent, thisComponent2, snapComps[];
  private static Dialog flashingDialog;
  private static Dimension dim = new Dimension(), min;
  private static GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
  @SuppressWarnings("StaticNonFinalUsedInInitialization")
  private static Rectangle maxWinBounds = ge.getMaximumWindowBounds();
  @SuppressWarnings("StaticNonFinalUsedInInitialization")
  public static final Rectangle SCREEN_BOUNDS = new Rectangle(ge.getScreenDevices()[0].getDisplayMode().getWidth(), ge.
          getScreenDevices()[0].getDisplayMode().getHeight());
  private static Window flashingWindow;
  private java.awt.event.ComponentListener snapCompListener;

  public _Legacy_CompAction()
  {
    this(0);
  }

  public _Legacy_CompAction(int screen)
  {
    setScreen(screen);
  }
  
  /**
   * Centers a component in the selected monitor
   * @param comp
   * the component you wish to affect
   */
  public static void center(Component comp)
  {//Added " + 0.5" Feb 19, 2012 (2.0.10) for Marian
    if (comp instanceof Window)
    {
      screenOffset();
      setLocation(comp, new Rectangle((int) (((noOffset ? SCREEN_BOUNDS : maxWinBounds).getWidth() / 2.0) - (comp.getWidth() / 2.0) + 0.5) + offset,
                                      (int) (((noOffset ? SCREEN_BOUNDS : maxWinBounds).getHeight() / 2.0) - (comp.getHeight() / 2.0) + 0.5),
                                      (int) comp.getWidth(),
                                      (int) comp.getHeight()));
    }
    else
      setLocation(comp, new Rectangle((int) ((comp.getParent().getWidth() / 2.0) - (comp.getWidth() / 2.0) + 0.5) + offset,
                                      (int) ((comp.getParent().getHeight() / 2.0) - (comp.getHeight() / 2.0) + 0.5),
                                      (int) comp.getWidth(),
                                      (int) comp.getHeight()));
  }
  
  /**
   * Centers a component over the other given component
   * @param comp the component you wish to affect
   * @param context the context of the centering. This is how the method knows where to place {@code comp}
   */
  public static void center(Component comp, Component context)//Added Feb 19, 2012 (2.0.10) for Marian
  {
    if (context == null)
    {
      center(comp);
      return;
    }
    screenOffset();
    setLocation(comp, new Rectangle((int) (context.getX() + (context.getWidth() / 2.0) - (comp.getWidth() / 2.0) + 0.5) + offset,
                                    (int) (context.getY() + (context.getHeight() / 2.0) - (comp.getHeight() / 2.0) + 0.5),
                                    (int) comp.getWidth(),
                                    (int) comp.getHeight()));
  }

  /**
   * Places a component in the corner of the specified monitor
   * @param comp
   * the component you wish to place
   * @param CORNER
   * <p>byte</tt> indicating the corner of the display where you wish to place the component. Please use the provided constants
   * <tt>TOP_LEFT</tt>, <tt>TOP_RIGHT</tt>, <tt>BOTTOM_LEFT</tt>, and <tt>BOTTOM_RIGHT</tt> in this field.
   */
  public static void corner(Component comp, final byte CORNER)
  {
    screenOffset();
    Point DEST;
    if (comp instanceof Window)
      switch (CORNER)
      {
        default:
        case TOP_LEFT:
          DEST = new Point(SCREEN_BOUNDS.getLocation());
          break;
        case TOP_RIGHT:
          DEST = new Point(SCREEN_BOUNDS.width - comp.getWidth() + SCREEN_BOUNDS.x, SCREEN_BOUNDS.y);
          break;
        case BOTTOM_RIGHT:
          DEST = new Point(SCREEN_BOUNDS.width - comp.getWidth() + SCREEN_BOUNDS.x, SCREEN_BOUNDS.height - comp.getHeight() - SCREEN_BOUNDS.y);
          break;
        case BOTTOM_LEFT:
          DEST = new Point(SCREEN_BOUNDS.x, SCREEN_BOUNDS.height - comp.getHeight() - SCREEN_BOUNDS.y);
          break;
      }
    else
      switch (CORNER)
      {
        default:
        case TOP_LEFT:
          DEST = new Point(0,0);
          break;
        case TOP_RIGHT:
          DEST = new Point(comp.getParent().getWidth() - comp.getWidth(), 0);
          break;
        case BOTTOM_RIGHT:
          DEST = new Point(comp.getParent().getWidth() - comp.getWidth(), comp.getParent().getHeight() - comp.getHeight());
          break;
        case BOTTOM_LEFT:
          DEST = new Point(0, comp.getParent().getHeight() - comp.getHeight());
          break;
      }
    setLocation(comp, DEST);
  }

  /**
   * Moves a component to the corner of the specified monitor.
   * @param comp
   * the component you wish to move
   * @param CORNER
   * <tt>byte</tt> indicating the corner of the display where you wish to move the component. Please use the provided constants
   * <tt>TOP_LEFT</tt>, <tt>TOP_RIGHT</tt>, <tt>BOTTOM_LEFT</tt>, and <tt>BOTTOM_RIGHT</tt> in this field.
   */
  public void goToCorner(final Component comp, final byte CORNER)
  {
    screenOffset();
    final Point DEST;
    if (comp instanceof Window)
      switch (CORNER)
      {
        default:
        case TOP_LEFT:
          DEST = new Point(SCREEN_BOUNDS.getLocation());
          break;
        case TOP_RIGHT:
          DEST = new Point(SCREEN_BOUNDS.width - comp.getWidth() + SCREEN_BOUNDS.x, SCREEN_BOUNDS.y);
          break;
        case BOTTOM_RIGHT:
          DEST = new Point(SCREEN_BOUNDS.width - comp.getWidth() + SCREEN_BOUNDS.x, SCREEN_BOUNDS.height - comp.getHeight() - SCREEN_BOUNDS.y);
          break;
        case BOTTOM_LEFT:
          DEST = new Point(SCREEN_BOUNDS.x, SCREEN_BOUNDS.height - comp.getHeight() - SCREEN_BOUNDS.y);
          break;
      }
    else
      switch (CORNER)
      {
        default:
        case TOP_LEFT:
          DEST = new Point(0,0);
          break;
        case TOP_RIGHT:
          DEST = new Point(comp.getParent().getWidth() - comp.getWidth(), 0);
          break;
        case BOTTOM_RIGHT:
          DEST = new Point(comp.getParent().getWidth() - comp.getWidth(), comp.getParent().getHeight() - comp.getHeight());
          break;
        case BOTTOM_LEFT:
          DEST = new Point(0, comp.getParent().getHeight() - comp.getHeight());
          break;
      }
    goToLocation(comp, DEST);
  }

  /**
   * Slides open a component for effect
   * @param COMP the component you wish to slide open
   * @param WIDTH The final width of the component after sliding it open
   * @param HEIGHT The final height of the component after sliding it open
   * @param BRAKE the amount of a damper to put on the sliding. 0.0 means that it will snap straight to open, and any number
   * higher means it will take longer. Recommended value is 2.0
   * @param FPS The target speed of the animation, in frames per second
   * @param endAction The action to perform after the animation is complete
   * @param ret The size to which the component will be set after the animation is complete
   * @param retState The state to which the component will be set after the animation is complete
   */
  public void slideOpen(final Component COMP, final int WIDTH, final int HEIGHT, final float BRAKE, final int FPS,
                         final Dimension ret, final int retState, final java.awt.event.ActionListener endAction)
  {
    final Dimension oMin = COMP.getMinimumSize();
    COMP.setMinimumSize(new Dimension(0,0));
    COMP.setSize(0, 0);
    COMP.setVisible(true);
    morphTo(COMP, new Rectangle(COMP.getX(), COMP.getY(), WIDTH, HEIGHT), BRAKE, FPS,
        new Rectangle(COMP.getX(), COMP.getY(), ret == null ? COMP.getWidth() : ret.width, ret == null ? COMP.getHeight() :
        ret.height), retState, false,
        new java.awt.event.ActionListener()
        {
          @Override
          public void actionPerformed(ActionEvent e)
          {
            COMP.setMinimumSize(oMin);
            if (endAction != null)
              endAction.actionPerformed(e);
          }
        });

    /*final int FPS = 30;
    if (BRAKE < 0)
      throw new IllegalArgumentException("BRAKE must be >= 0 (was set to " + BRAKE + ")");
    final Rectangle BEGIN = COMP.getBounds(); BEGIN.height=0; BEGIN.width=0;
    final Dimension oMin = COMP.getMinimumSize();
    final int oState = COMP instanceof Frame ? ((Frame)COMP).getState() : 0;
    final int wDiff = WIDTH - BEGIN.width, hDiff = HEIGHT - BEGIN.height;
    stopSliding();
    thisComponent = COMP;
    COMP.setMinimumSize(new Dimension(0,0));
    COMP.setSize(0,0);
    COMP.setVisible(true);
    BEGIN.height = COMP.getHeight();
    BEGIN.width = COMP.getWidth();
    slider = new javax.swing.Timer(FPS / 1000, new java.awt.event.ActionListener()
    {
      private float p = 0;
      private Rectangle expDim = new Rectangle(BEGIN);
      @Override
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        if (!COMP.getBounds().equals(expDim))
          stopSliding();
        float f = (p + 0) / 100;
        expDim.width = (int)(BEGIN.width + (wDiff * f));
        expDim.height = (int)(BEGIN.height + (hDiff * f));
        COMP.setBounds(BEGIN.x, BEGIN.y, expDim.width, expDim.height);

        p = p + ((100 - p) / (BRAKE + 1));

        if (p >= 99.999 || (expDim.width == WIDTH && expDim.height == HEIGHT))//If the animation is very close to being completed
        {
          stopSliding();
          COMP.setMinimumSize(oMin);
          if (COMP instanceof Frame)
            ((Frame)COMP).setState(oState);
          if (endAction != null)
            endAction.actionPerformed(evt);
        }
      }
    });
    slider.start();*/
  }

  /**
   * Slides open a component for effect, uses the preferred size as the target size.
   * @param comp the component you wish to slide open
   * @param BRAKE the amount of a damper to put on the sliding. 0.0 means that it will snap straight to open, and any number
   * higher means it will take longer. Recommended value is 2.0
   */
  public void slideOpen(final Component comp, final float BRAKE)
  {
    slideOpen(comp, comp.getWidth(), comp.getHeight(), BRAKE, DEF_FPS, comp.getSize(), 0, null);
  }

  /**
   * Slides open a component for effect, uses the preferred size as the target size.
   * @param comp the component you wish to slide open
   * @param endAction The action to perform after the animation is complete
   */
  public void slideOpen(final Component comp, final java.awt.event.ActionListener endAction)
  {
    slideOpen(comp, comp.getWidth(), comp.getHeight(), DEF_BRAKE, DEF_FPS, comp.getSize(), 0, endAction);
  }

  /**
   * Slides open a component for effect, uses the preferred size as the target size.
   * @param comp
   * the component you wish to slide open
   */
  public void slideOpen(final Component comp)
  {
    slideOpen(comp, comp.getWidth(), comp.getHeight(), DEF_BRAKE, DEF_FPS, comp.getSize(), 0, null);
  }

  /**
   * Sets the screen on which to display the target window when using the <tt>center(Component window)</tt>, <tt>corner(Component window, final int CORNER)</tt>, <tt>goToCorner(final Component window, final int CORNER)</tt>, <tt>slideOpenFromCenter(final Component window, final int WIDTH, final int HEIGHT)</tt>, <tt>slideOpenFromCenter(final Component window)</tt>, and <tt>slideClosedToCenter(final Component window, final boolean exit)</tt> methods.
   * @param preferredScreen
   * the number of the preferred screen
   */
  public final void setScreen(int preferredScreen)
  {
    screen = preferredScreen - 1;
    int w = ge.getScreenDevices()[0].getDisplayMode().getWidth(), h = ge.getScreenDevices()[0].getDisplayMode().getHeight();
//    System.out.println("Selected screen: " + preferredScreen + " [" + ge.getScreenDevices()[screen].getDisplayMode().getWidth() + ", " + ge.getScreenDevices()[screen].getDisplayMode().getHeight() + "]");
    for (int i = 1; i <= screen; i++)
    {
      w = ge.getScreenDevices()[i].getDisplayMode().getWidth();
      h = ge.getScreenDevices()[i].getDisplayMode().getHeight();
    }
//    System.out.println("w: " + w + "\th: " + h);
    SCREEN_BOUNDS.setSize(w, h);
    maxWinBounds.setSize(w, (int) ge.getMaximumWindowBounds().getHeight());
//    System.out.println(maxWinBounds);
  }

  /**
   * Gets the Screen on which to display the target window when using the <tt>center(Component window)</tt>,
   * <tt>corner(Component window, final int CORNER)</tt>, <tt>goToCorner(final Component window, final int CORNER)</tt>,
   * <tt>slideOpenFromCenter(final Component window, final int WIDTH, final int HEIGHT)</tt>,
   * <tt>slideOpenFromCenter(final Component window)</tt>, and
   * <tt>slideClosedToCenter(final Component window, final boolean exit)</tt> methods.
   * @return the currently used screen
   */
  public static int getScreen()
  {
    return screen + 1;
  }

  /**
   * Stops the movement of all windows and other components currently being affected by CompAction
   * @deprecated use other <tt>stop</tt> methods to stop specific actions
   */
  public void stop()
  {
    stopSliding();
    stopMoving();
    stopFading();
  }

  /**
   * Stops the movement of the window or other component currently being slid open or shut (<tt>slide</tt> methods)
   */
  public void stopSliding()
  {
    if (slider != null)
    {
      slider.restart();
      slider.stop();
    }
    if (thisComponent != null)
      thisComponent.setEnabled(true);
    if (thisComponent2 != null)
      thisComponent2.setEnabled(true);
    count = 0;
  }

  /**
   * Stops the movement of the window or other component currently being moved (<tt>goTo</tt> methods)
   */
  public void stopMoving()
  {
    if (mover != null)
    {
      mover.restart();
      mover.stop();
    }
    if (thisComponent != null)
    {
      thisComponent.setEnabled(true);
      thisComponent.repaint();
      thisComponent.doLayout();
    }
    if (thisComponent2 != null)
    {
      thisComponent2.setEnabled(true);
      thisComponent2.repaint();
      thisComponent2.doLayout();
    }
    count = 0;
  }

  /**
   * Stops the movement of the window currently being faded (<tt>fade</fade> methods)
   */
  public void stopFading()
  {
    if (fader != null)
    {
      fader.restart();
      fader.stop();
    }
    if (thisComponent != null)
      thisComponent.setEnabled(true);
    if (thisComponent2 != null)
      thisComponent2.setEnabled(true);
    count = 0;
  }

  public boolean isSliding()
  {
    return slider != null && slider.isRunning();
  }

  public boolean isMoving()
  {
    return mover != null && mover.isRunning();
  }

  public boolean isFading()
  {
    return fader != null && fader.isRunning();
  }

  /**
   * sets whether the component is offset from the corner of the screen due to a taskbar, dock, or similar object when using the
   * <tt>corner(Component comp, final int CORNER)</tt> and <tt>goToCorner(final Component comp, final int CORNER)</tt> methods.
   * @param flag
   * the boolean flag as to whether there is an offset
   */
  public void setOffset(boolean flag)
  {
    noOffset = flag;
  }

  /**
   * @return a boolean that represents whether the component is offset from the corner of the screen due to a taskbar, dock, or
   * similar object when using the <tt>corner(Component comp, final int CORNER)</tt> and <tt>goToCorner(final Component comp, final int CORNER)</tt> methods.
   */
  public static boolean isOffset()
  {
    return noOffset;
  }

  /**
   * Slides open a component for effect, keeping it in the center of the selected display
   * @param comp
   * the component you wish to slide open
   * @param WIDTH
   * The final width of the component after sliding it open
   * @param HEIGHT
   * The final height of the component after sliding it open
   */
  public void slideOpenFromCenter(final Component comp, final int WIDTH, final int HEIGHT)
  {
    stopSliding();
    thisComponent = comp;
    min = new Dimension(comp.getMinimumSize());
    comp.setMinimumSize(new Dimension(0, 0));
    comp.setSize(new Dimension(0, 0));
    dim = comp.getSize();
    slider = new javax.swing.Timer(50, new java.awt.event.ActionListener()
    {
      int w = WIDTH, h = HEIGHT;

      @Override
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        center(comp);
        if (dim.getWidth() < WIDTH || dim.getHeight() < HEIGHT)
        {
          dim = new Dimension(WIDTH - w, HEIGHT - h);

          if (dim.getWidth() < WIDTH)
          {
            w = (int) (w / 1.25);
          }
          else
          {
            h = (int) (h / 1.25);
          }
          comp.setSize(dim);
          comp.repaint();
        }
        else
        {
          stopSliding();
          comp.setMinimumSize(new Dimension(min.width < WIDTH ? min.width : WIDTH, min.height < HEIGHT ? min.height : HEIGHT));
        }
//        System.out.println("WIDTH: " + WIDTH + ", HEIGHT: " + HEIGHT + ", w: " + w + ", h: " + h + ", window.getSize().toString(): " + window.getSize().toString() + ", dim:" + dim + ", timer2.isRunning(): " + timer2.isRunning());
      }
    });
    center(comp);
    comp.setVisible(true);
    slider.start();
  }


  /**
   * Slides open a component for effect, keeping it in the center of the selected display
   * @param comp
   * the component you wish to slide open
   * @param d
   * The final dimensions of the component after sliding it open
   */
  public void slideOpenFromCenter(final Component comp, final Dimension d)
  {
    slideOpenFromCenter(comp, d.width, d.height);
  }

  /**
   * Slides open a component for effect, keeping it in the center of the selected display, uses the minimum size as the target size.<br />
   * Equivalent of calling <tt>slideOpenFromCenter(component, component.getMinimumSize());</tt>
   * @param comp
   * the component you wish to slide open
   */
  public void slideOpenFromCenter(final Component comp)
  {
    slideOpenFromCenter(comp, comp.getMinimumSize());
  }

  /**
   * Slides a component closed for effect<br />
   * Equivalent of calling <tt>slideClosed(component, false);</tt>
   * @param COMP
   * the component you wish to slide closed
   */
  public void slideClosed(final Component COMP)
  {
    slideClosed(COMP, DEF_BRAKE, null, null, DEF_FPS, 0, false, 0);
  }
  
  
  /**
   * Slides a component closed for effect<br />
   * Equivalent of calling <tt>slideClosed(component, false);</tt>
   * @param COMP the component to be slid closed
   * @param BRAKE the amount of a damper to put on the sliding. 0.0 means that it will snap straight to open, and any number
   * higher means it will take longer. Recommended value is 2.0
   * the component you wish to slide closed
   */
  public void slideClosed(final Component COMP, final float BRAKE)
  {
    slideClosed(COMP, BRAKE, null, null, DEF_FPS, 0, false, 0);
  }
  
  /**
   * Slides a component closed for effect
   * @param COMP the component you wish to slide closed
   * @param exit quits the program at the end if true
   * @param status the exit status of the program
   */
  public void slideClosed(final Component COMP, final boolean exit, final int status)
  {
    slideClosed(COMP, DEF_BRAKE, null, null, DEF_FPS, 0, exit, status);
  }
  
  /**
   * Slides a component closed for effect
   * @param COMP the component you wish to slide closed
   * @param exit quits the program at the end if true
   * @param status the exit status of the program
   * @see #morphTo(java.awt.Component, java.awt.Rectangle, float, int, java.awt.Rectangle, int, boolean, java.awt.event.ActionListener) 
   */
  public void slideClosed(final Component COMP, final float BRAKE, final boolean exit, final int status)
  {
    slideClosed(COMP, BRAKE, null, null, DEF_FPS, 0, exit, status);
  }

  /**
   * Slides a component closed for effect
   * @param COMP the component to be slid closed
   * @param BRAKE the amount of a damper to put on the sliding. 0.0 means that it will snap straight to open, and any number
   * higher means it will take longer. Recommended value is 2.0
   * @param exit quits the program at the end if true
   * @param status the exit status of the program
   * @see #morphTo(java.awt.Component, java.awt.Rectangle, float, int, java.awt.Rectangle, int, boolean, java.awt.event.ActionListener) 
   */
  public void slideClosed(final Component COMP, final float BRAKE, final java.awt.event.ActionListener endAction,
                           final Rectangle ret, final int FPS, final int retState, final boolean exit, final int status)
  {
    final Dimension oMin = COMP.getMinimumSize();
    COMP.setMinimumSize(new Dimension(0,0));
    morphTo(COMP, new Rectangle(COMP.getX(), COMP.getY(), 0, 0), BRAKE, FPS, new Rectangle(COMP.getX(), COMP.getY(), 0, 0),
                                 retState, true, new java.awt.event.ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        try
        {
          COMP.setBounds(ret == null ? new Rectangle(COMP.getX(), COMP.getY(), oMin.width, oMin.height) : ret);
          COMP.setMinimumSize(oMin);
        }
        catch (Throwable t)
        {
          t.printStackTrace();
        }
        if (endAction != null)
          endAction.actionPerformed(e);
        if (exit)
          System.exit(status);
      }
    });
  }

  /**
   * Slides a component closed for effect, keeping it in the center of the selected display<br />
   * Equivalent of calling <tt>slideClosedToCenter(component, DO_NOT_EXIT);</tt>
   * @param comp
   * the component you wish to slide closed
   */
  public void slideClosedToCenter(final Component comp)
  {
    slideClosedToCenter(comp, false, 0);
  }

  /**
   * Slides a component closed for effect, keeping it in the center of the selected display
   * @param comp
   * the component you wish to slide closed
   * @param exit quits the program at the end if <tt>true</tt>.
   * @param status the exit status of the program
   */
  public void slideClosedToCenter(final Component comp, final boolean exit, final int status)
  {
//    window.setResizable(true);
    stopSliding();
    thisComponent = comp;
    min = new Dimension(comp.getMinimumSize());
    comp.setMinimumSize(new Dimension(0, 0));
    comp.setEnabled(false);
    dim = new Dimension(comp.getWidth(), comp.getHeight());
    slider = new javax.swing.Timer(50, new java.awt.event.ActionListener()
    {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        center(comp);
        if (dim.getWidth() > 1)
        {
          dim = new Dimension((int) (dim.getWidth() / 1.25) - 1, (int) dim.getHeight());
        }
        else
        {
          dim = new Dimension((int) dim.getWidth(), (int) (dim.getHeight() / 1.25) - 1);
        }
        comp.setSize(dim);
        comp.repaint();

//          System.out.println(window.getSize() + "\p" + dim.getSize());
        if (dim.getHeight() <= 1)
        {
          comp.setVisible(false);
          stopSliding();
          if (exit)
          {
            System.exit(status);
          }
          comp.setEnabled(true);
          comp.setMinimumSize(min);
        }
      }
    });
    slider.start();
  }

  /**
   * Slides the component in from the specified edge of the component's parent to its currently set location
   * @param comp the <tt>java.awt.Component</tt> to emerge
   * @param location a <tt>byte</tt> mask representing the edge from which the component will emerge. Use <tt>TOP</tt>,
   * <tt>RIGHT</tt>, <tt>BOTTOM</tt>, or <tt>LEFT</tt>
   */
  public void slideIn(Component comp, byte location)
  {
    Point from = new Point(comp.getX(), comp.getY());
    setLocation(comp, location == TOP || location == BOTTOM ? comp.getX() : -comp.getWidth(),
                   location == LEFT || location == RIGHT ? comp.getY() : -comp.getHeight());
    comp.setVisible(true);
    goToLocation(comp, from);
  }

  /**
   * Slides the component out to the specified edge of the component's parent to its current location
   * @param comp the <tt>java.awt.Component</tt> to hide
   * @param location a <tt>byte</tt> mask representing the edge to which the component will hide. Use <tt>TOP</tt>,
   * <tt>RIGHT</tt>, <tt>BOTTOM</tt>, or <tt>LEFT</tt>
   */
  public void slideOut(Component comp, byte location)
  {
    slideOut(comp, location, DEF_BRAKE);
  }

  /**
   * Slides the component out to the specified edge of the component's parent to its current location
   * @param comp the <tt>java.awt.Component</tt> to hide
   * @param location a <tt>byte</tt> mask representing the edge to which the component will hide. Use <tt>TOP</tt>,
   * <tt>RIGHT</tt>, <tt>BOTTOM</tt>, or <tt>LEFT</tt>
   * @param brake the amount of a damper to put on the sliding. 0.0 means that it will snap straight to the edge, and any number
   * higher means it will take longer. Recommended value is 2.0
   */
  public void slideOut(Component comp, byte location, final float brake)
  {
    slideOut(comp, location, brake, false, 0);
  }

  /**
   * Slides the component out to the specified edge of the component's parent to its current location
   * @param comp the <tt>java.awt.Component</tt> to hide
   * @param location the edge to which the component will hide
   * @param brake the amount of a damper to put on the sliding. 0.0 means that it will snap straight to the edge, and any number
   * higher means it will take longer. Recommended value is 2.0
   * @param exit
   * @param status
   */
  public void slideOut(Component comp, byte location, final float brake, final boolean exit, final int status)
  {
    Point from = new Point(comp.getX(), comp.getY());
    goToLocation(comp, new Point(location == TOP || location == BOTTOM ? comp.getX() : (location == LEFT ? -comp.getWidth() : (comp instanceof Window ? SCREEN_BOUNDS.width : comp.getParent().getWidth()) + comp.getWidth()),
                   location == LEFT || location == RIGHT ? comp.getY() : (location == TOP ? -comp.getHeight() : (comp instanceof Window ? SCREEN_BOUNDS.height : comp.getParent().getHeight()) + comp.getHeight())),
                 brake, DEF_FPS, from, comp instanceof Frame ? ((Frame)comp).getState() : 0, true,
                 new java.awt.event.ActionListener()
                 {
                   @Override
                   public void actionPerformed(ActionEvent e)
                   {
                     if (exit)
                       System.exit(status);
                   }
                 });
  }

  /**
   * Moves the specified component to the specified location
   * @param comp
   * the component you wish to move
   * @param location
   * java.awt.Rectangle indicating the location where you wish to move the component.
   */
  public void goToLocation(final Component comp, final Point location)
  {
    goToLocation(comp, location, location, false);
  }

  /**
   * Moves the specified component to the specified location
   * @param comp
   * the component you wish to move
   * @param X
   * <tt>int</tt> indicating the X location to where you wish to move the component.
   * @param Y
   * <tt>int</tt> indicating the Y location to where you wish to move the component.
   * @param ret a <tt>java.awt.Rectangle</tt>
   */
  public void goToLocation(final Component comp, final int X, final int Y, final Point ret)
  {
    goToLocation(comp, new Point(X, Y), ret, false);
  }

  /**
   * Moves the specified component to the specified location
   * @param comp
   * the component you wish to move
   * @param location
   * <tt>java.awt.Rectangle</tt> indicating the location where you wish to move the component.
   * @param ret
   * <tt>java.awt.Rectangle</tt> indicating the location where you wish to place the component after moving.
   * @param hide true if you want the component's <tt>setVisible(boolean b)</tt> to false at the end
   */
  public void goToLocation(final Component comp, final Point location, final Point ret, final boolean hide)
  {
    goToLocation(comp, location, DEF_BRAKE, DEF_FPS, ret,
                  ret == null ? 0 : (comp instanceof Frame ? ((Frame)comp).getState() : 0), hide, null);
  }

  /**
   * Moves the specified component to the specified location
   * @param comp
   * the component you wish to move
   * @param location
   * <tt>java.awt.Rectangle</tt> indicating the location where you wish to move the component.
   * @param ret
   * <tt>java.awt.Rectangle</tt> indicating the location where you wish to place the component after moving.
   * @param hide true if you want the component's <tt>setVisible(boolean b)</tt> to false at the end
   */
  public void goToLocation(final Component comp, final Point location, final float BRAKE, final Point ret, final boolean hide)
  {
    goToLocation(comp, location, BRAKE, DEF_FPS, ret,
                  ret == null ? 0 : (comp instanceof Frame ? ((Frame)comp).getState() : 0), hide, null);
  }


  /**
   * Moves the specified component to the specified location
   * @param COMP
   * the component you wish to move
   * @param location
   * <tt>java.awt.Rectangle</tt> indicating the location where you wish to move the component.
   * @param BRAKE the amount of a damper to put on the moving. 0.0 means that it will snap straight to the location, and any
   * number higher means it will take longer. Recommended value is 2.0
   * @param FPS The maximum speed of the animation, in frames per second
   * @param ret <tt>java.awt.Rectangle</tt> indicating the location where you wish to place the component after moving.
   * @param retState the state at which to set the component at the end of the animation.
   * <b>ONLY WORKS IF THE COMPONENT IS A <TT>java.awt.Frame</TT>. ELSE IS IGNORED.</b>
   * @param hide true if you want the component's <tt>setVisible(boolean b)</tt> to false at the end
   * @param endAction The action to perform after the animation is complete
   */
  public void goToLocation(final Component COMP, final Point location, final float BRAKE, final int FPS, final Point ret, final int retState,
                           final boolean hide, final java.awt.event.ActionListener endAction)
  {
    morphTo(COMP, new Rectangle(location.x, location.y, COMP.getWidth(), COMP.getHeight()), BRAKE, FPS,
             null, retState, hide, endAction);
  }

  /**
   * Moves the specified component in the specified location.
   * Equivalent to calling <tt>goToocation(comp, new java.awt.Rectangle(x, y, comp.getWidth(), comp.getHeight()));</tt>
   * @param comp the <tt>java.awt.Component</tt> you wish to move
   * @param x <tt>int</tt> indicating the horizontal location where you wish to place the component.
   * @param y <tt>int</tt> indicating the vertical location where you wish to place the component.
   */
  public void goToLocation(Component comp, int x, int y)
  {
    goToLocation(comp, new Point(x, y));
  }

  /**
   * Places the specified component in the specified location.
   * Equivalent of calling <tt>location(comp, new java.awt.Rectangle(x, y, comp.getWidth(), comp.getHeight()));</tt>
   * @param comp the <tt>java.awt.Component</tt> you wish to place
   * @param x <tt>int</tt> indicating the horizontal location where you wish to place the component.
   * @param y <tt>int</tt> indicating the vertical location where you wish to place the component.
   */
  public static void setLocation(Component comp, int x, int y)
  {
    setLocation(comp, new Rectangle(x, y, comp.getWidth(), comp.getHeight()));
  }

  /**
   * Places the specified component in the specified location.<br/>
   * <i>Equivalent to calling <tt>setLocation(comp, new java.awt.Rectangle(location.width, location.height, comp.getWidth(),
   * comp.getHeight()));</tt></i>
   * @param comp the <tt>java.awt.Component</tt> you wish to place
   * @param location <tt>java.awt.Dimension</tt> indicating the location where you wish to place the component.
   */
  public static void setLocation(Component comp, Dimension location)
  {
    setLocation(comp, new Rectangle(location.width, location.height, comp.getWidth(), comp.getHeight()));
  }

  /**
   * Places the specified component in the specified location.<br/>
   * <i>Equivalent to calling <tt>setLocation(comp, new java.awt.Rectangle(location.width, location.height, comp.getWidth(),
   * comp.getHeight()));</tt></i>
   * @param comp the <tt>java.awt.Component</tt> you wish to place
   * @param location <tt>java.awt.Point</tt> indicating the location where you wish to place the component.
   */
  public static void setLocation(Component comp, Point location)
  {
    setLocation(comp, new Rectangle(location.x, location.y, comp.getWidth(), comp.getHeight()));
  }

  /**
   * Places the specified component in the specified location
   * @param comp the <tt>java.awt.Component</tt> you wish to place
   * @param location <tt>java.awt.Rectangle</tt> indicating the location where you wish to place the component.
   */
  public static void setLocation(Component comp, Rectangle location)
  {
//    System.out.println("setLocation(" + window + ", " + location + ")");
    comp.setBounds(location.x, location.y, location.width, location.height);
  }

  /**
   * calculates the offset from the original position based on the screen selection
   */
  private static void screenOffset()
  {
    offset = 0;
    for (int i = 0; !noOffset && i < screen; i++)
    {
      offset += ge.getScreenDevices()[i].getDisplayMode().getWidth();
    }
//    System.out.println("offset set to " + offset);
  }

  /**
   * sets the transparency of a component
   * @param comp The component to try to make transparent
   * @param t the transparency of the component (0.0F - 1.0F), where 1.0 is fully opaque, and 0.0 is invisible
   */
  @SuppressWarnings("UseOfSystemOutOrSystemErr")
  public static void setTransparency(Component comp, float t)
  {
    try
    {
      if (comp instanceof Window)
      {
        try
        {
//For JDK 1.7
          ((Window) comp).setOpacity(t);
        }
        catch (Throwable th)
        {
//          System.err.println("JRE may be less than 1.7!");
          if (!th.getClass().isInstance(new NoSuchMethodError()))//If it's a NoSuchMethodError, we know simply that the JVM hasn't caught up with Java 7 and the next attempt should work.
            th.printStackTrace();
          try
          {
//For JDK 1.6
            com.sun.awt.AWTUtilities.setWindowOpacity((Window) comp, t);
          }
          catch (Throwable th1)
          {
            throw new UnsupportedOperationException("It seems that transparency is not supported", th1);
//            System.out.println("Transparency derp");
          }
        }
      }
      else
      {
        if (comp instanceof JComponent)
          ((JComponent)comp).setOpaque(false);
        Graphics2D g2 = (Graphics2D) comp.getGraphics().create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, t));
        comp.paint(g2);
        g2.dispose();
      }
    }
    catch (Throwable th)
    {
      throw new UnsupportedOperationException("Transparency might not be supported", th);
    }
  }

  public static float getTransparency(Component comp)
  {
    if (comp instanceof Window)
      return com.sun.awt.AWTUtilities.getWindowOpacity((Window)comp);
    else
    {
      return 1;
    }
  }
  
  /**
   * Returns whether translucency is theoretically supported by the OS
   * @return 
   *//*
  public static boolean isTranslucencySupported()
  {java.awt.GraphicsDevice d = ge.getDefaultScreenDevice();
    return d.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT) ||
            d.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSLUCENT);
  }*/
  
  /**
   * Tests whether translucency is supported (practically, not theoretically), and returns the result.
   * @return <tt>true</tt> if and only if translucency is supported in practice.
   */
  public static boolean testTranslucencySupport()
  {
    javax.swing.JFrame f = new javax.swing.JFrame();
    f.setBounds(-1000, -1000, 0, 0);
    f.setVisible(true);
    try
    {
      setTransparency(f, 0.5F);
    }
    catch (Throwable t)
    {
      f.setVisible(false);
      f.dispose();
      f = null;
      return false;
    }
    f.setVisible(false);
    f.dispose();
    f = null;
    return true;//isTranslucencySupported();
  }

  /**
   * Fades the given component in from invisible to fully visible at a given speed
   * @param comp the component to be affected
   */
  public void fadeIn(final Component comp)
  {
    fadeIn(comp, DEF_BRAKE);
  }

  /**
   * Fades the given component in from invisible to fully visible at a given speed
   * @param comp the component to be affected
   */
  public void fadeIn(final Component comp, float BRAKE)
  {
    fadeIn(comp, BRAKE, null);
  }

  /**
   * Fades the given component in from invisible to fully visible at a given speed
   * @param comp the component to be affected
   */
  public void fadeIn(final Component comp, ActionListener endAction)
  {
    fadeIn(comp, DEF_BRAKE, endAction);
  }

  /**
   * Fades the given component in from invisible to fully visible at a given speed
   * @param comp the component to be affected
   * @param BRAKE the amount of a damper to put on the fading. 0.0 means that it will snap straight to opaque, and any number
   * higher means it will take longer. Recommended value is 2.0
   * @param endAction the action to be performed after the animation has completed 
   */
  public void fadeIn(final Component comp, final float BRAKE, final ActionListener endAction)
  {
    if (BRAKE < 0)
      throw new IllegalArgumentException("BRAKE must be >= 0 (was set to " + BRAKE + ")");

    stopFading();
    thisComponent = comp;
    setTransparency(comp, 0);
    comp.setVisible(true);
    fader = new javax.swing.Timer(100, new java.awt.event.ActionListener()
    {
      float t = 0;
      @Override
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        setTransparency(comp, Numbers.between((t + 1) / 100, 1, 0));

        t = t + ((100 - t) / (BRAKE + 1));

        if (t >= 99.999)
        {
          stopFading();
          comp.setVisible(true);
          setTransparency(comp, 1);
          if (endAction != null)
            endAction.actionPerformed(evt);
        }
      }
    });
    fader.start();
  }

  public void fadeAway(final Component comp)
  {
    fadeAway(comp, DEF_BRAKE);
  }

  public void fadeAway(final Component comp, final float BRAKE)
  {
    fadeAway(comp, BRAKE, false, 0, null);
  }

  public void fadeAway(final Component comp, final boolean exit, final int STATUS)
  {
    fadeAway(comp, DEF_BRAKE, exit, STATUS, null);
  }


  /**
   * Fades the given component in from invisible to fully visible at a given speed
   * @param comp the component to be affected
   * @param BRAKE the amount of a damper to put on the fading. 0.0 means that it will snap straight to opaque, and any number
   * higher means it will take longer. Recommended value is 2.0
   * @param exit whether the application should exit after the component fades away
   * @param STATUS the exit status to use if <tt>exit == true</tt>
   * @param endAction The action to perform at the end of the animation 
   */
  public void fadeAway(final Component comp, final float BRAKE, final boolean exit, final int STATUS, final java.awt.event.ActionListener endAction)
  {
    if (BRAKE < 0)
      throw new IllegalArgumentException("brake must be greater than 0 (" + BRAKE + ")");

    stopFading();
    thisComponent = comp;
    setTransparency(comp, 1);
    fader = new javax.swing.Timer(100, new java.awt.event.ActionListener()
    {
      float t = 0;
      @Override
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        setTransparency(comp, Numbers.between(1 - ((t + 1) / 100), 1, 0));

        t = t + ((100 - t) / (BRAKE + 1));

        if (t >= 99.999F)
        {
          stopFading();
          comp.setVisible(false);
          setTransparency(comp, 0);
          if (endAction != null)
            endAction.actionPerformed(evt);
          if (exit)
            System.exit(STATUS);
        }
      }
    });
    fader.start();
  }

  /**
   * Utilizes all three types of window effects (moving, resizing, and fading) to morph one component into another
   * @param fromComp the original component, to be morphed away
   * @param toComp the component to which the original is being being morphed
   * @param BRAKE the amount of a damper to put on the morph. 0.0 means that the first component will snap straight to the
   * second, and any number higher means it will take longer.
   * @param endAction the action to be performed when the animation is completed
   */
  @SuppressWarnings("UseOfSystemOutOrSystemErr")
  public void morphInto(final Component fromComp, final Component toComp, final float BRAKE, final java.awt.event.ActionListener endAction)
  {
    if (BRAKE < 0)
      throw new IllegalArgumentException("BRAKE must be >= 0 (was set to " + BRAKE + ")");
    final Rectangle BEGIN = fromComp.getBounds(), here = new Rectangle(BEGIN), END = toComp.getBounds();
    final int xDiff = toComp.getX() - BEGIN.x, yDiff = toComp.getY() - BEGIN.y,
            wDiff = toComp.getWidth() - BEGIN.width, hDiff = toComp.getHeight() - BEGIN.height;
    stopMoving();
    stopFading();
    thisComponent = fromComp;
    thisComponent2 = toComp;
    mover = new javax.swing.Timer(100, new java.awt.event.ActionListener()
    {
      private float p = 0;
      @Override
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        float f = (p + 0) / 100;
        here.x = (int)(BEGIN.x + (xDiff * f));
        here.y = (int)(BEGIN.y + (yDiff * f));
        here.width = (int)(BEGIN.width + (wDiff * f));
        here.height = (int)(BEGIN.height + (hDiff * f));
        setLocation(fromComp, here);
        setTransparency(fromComp, f);

        setLocation(toComp, here);
        setTransparency(toComp, 1 - f);

        p = p + ((100 - p) / (BRAKE + 1));

        if (p >= 99.999)
        {
          stopMoving();
          setLocation(toComp, END);
          setTransparency(toComp, 0);
          if (endAction != null)
            endAction.actionPerformed(evt);
        }
      }
    });
    mover.start();

    // <editor-fold defaultstate="collapsed" desc="OLD CODE, kept for reference only">
    /*
    stop();
    thisComponent = fromComp;
    thisComponent2 = toComp;
    final Rectangle FROM = fromComp.getBounds(), DEST = toComp.getBounds();
    final boolean GOING_UP = DEST.getY() < FROM.getY(),
    GOING_RIGHT = DEST.getX() > FROM.getX(),
    EXPANDING_H = DEST.getWidth() > FROM.getWidth(),
    EXPANDING_V = DEST.getHeight() > FROM.getHeight(),
    fromResize = (fromComp instanceof Frame && ((Frame)fromComp).isResizable()) ||
    (fromComp instanceof Dialog && ((Dialog)fromComp).isResizable()),
    toResize = (toComp instanceof Frame && ((Frame)toComp).isResizable()) ||
    (toComp instanceof Dialog && ((Dialog)toComp).isResizable());

    int speed = 100;

    fromComp.setBounds(FROM);
    toComp.setBounds(FROM);
    mover = new javax.swing.Timer(speed, new java.awt.event.ActionListener()
    {
    double d = 0;
    int count = 0;
    Rectangle here = FROM;

    @Override
    public void actionPerformed(java.awt.event.ActionEvent evt)
    {
    //        System.out.println("d: " + d);
    //        System.out.println("FROM: " + FROM);
    //        System.out.println("here: " + here);
    //        System.out.println("DEST: " + DEST);

    here = new Rectangle((int) ((FROM.getX() * (1 - d)) + (DEST.getX() * d)), (int) ((FROM.getY() * (1 - d)) + (DEST.getY()
     * d)),
    (int) ((FROM.getWidth() * (1 - d)) + (DEST.getWidth() * d)), (int) ((FROM.getHeight() * (1 - d))
    + (DEST.getHeight() * d)));
    fromComp.setBounds(here);
    toComp.setBounds(here);
    fromComp.setSize((int) here.getWidth(), (int) here.getHeight());
    toComp.setSize((int) here.getWidth(), (int) here.getHeight());

    //        System.out.println("((" + GOING_UP + " && " + here.getY() + " <= " + DEST.getY() + ") || (" + !GOING_UP + " && " + here.
    //                getY() + " >= " + DEST.getY() + ")) &&\n" + "((" + GOING_RIGHT + " && " + here.getX() + " >= " + DEST.getX()
    //                           + ") || (" + !GOING_RIGHT + " && " + here.getX() + " <= " + DEST.getX() + ")) &&\n" + "(("
    //                           + EXPANDING_V + " && " + here.getHeight() + " >= " + DEST.getHeight() + ") || (" + !EXPANDING_V
    //                           + " && " + here.getHeight() + " <= " + DEST.getHeight() + ")) &&\n" + "((" + EXPANDING_H + " && " + here.
    //                getWidth() + " >= " + DEST.getWidth() + ") || (" + !EXPANDING_H + " && " + here.getWidth() + " <= " + DEST.
    //                getWidth() + "))");

    if (d >= 1 ||
    (((GOING_UP && here.getY() <= DEST.getY()) || (!GOING_UP && here.getY() >= DEST.getY())) &&
    ((GOING_RIGHT && here.getX() >= DEST.getX()) || (!GOING_RIGHT && here.getX() <= DEST.getX())) &&
    ((EXPANDING_V && here.getHeight() >= DEST.getHeight()) || (!EXPANDING_V && here.getHeight() <= DEST.getHeight())) &&
    ((EXPANDING_H && here.getWidth() >= DEST.getWidth()) || (!EXPANDING_H && here.getWidth() <= DEST.getWidth()))))
    {
    //          System.out.println("Done moving!");
    fromComp.setVisible(false);
    toComp.setVisible(true);
    fromComp.setBounds(FROM);
    toComp.setBounds(DEST);
    fromComp.setSize((int) FROM.getWidth(), (int) FROM.getHeight());
    toComp.setSize((int) DEST.getWidth(), (int) DEST.getHeight());

    //          System.out.println("FROM: " + FROM);
    //          System.out.println("DEST: " + DEST);

    if (fromComp instanceof Frame && !fromResize)
    {
    ((Frame) fromComp).setResizable(false);
    }
    else if (fromComp instanceof Dialog && !fromResize)
    {
    ((Dialog) fromComp).setResizable(false);
    }

    if (toComp instanceof Frame && !toResize)
    {
    ((Frame) toComp).setResizable(false);
    }
    else if (toComp instanceof Dialog && !toResize)
    {
    ((Dialog) toComp).setResizable(false);
    }
    stopMoving();
    }
    count++;
    d = (double) count / 10;
    }
    });
    try
    {
    if (com.sun.awt.AWTUtilities.isTranslucencySupported(Translucency.TRANSLUCENT))
    {
    setTransparency(toComp, 0F);
    toComp.setVisible(true);

    fader = new javax.swing.Timer(speed, new java.awt.event.ActionListener()
    {
    float t = 1;
    byte count = 0;

    @Override
    public void actionPerformed(java.awt.event.ActionEvent evt)
    {
    t -= .1F;
    count++;

    if (t <= 0.01 || count >= 10)
    {
    //              System.out.println("Done fading!");
    stopFading();
    fromComp.setVisible(false);
    setTransparency(fromComp, 1);
    setTransparency(toComp, 1);
    }
    else
    {
    //              System.out.println("from: " + p + "\tto:" + (1 - p));
    setTransparency(fromComp, t);
    setTransparency(toComp, 1 - t);
    }
    }
    });
    fader.start();
    }
    else
    {
    System.err.println("ERROR: transleucency not supported");
    fromComp.setVisible(false);
    }
    }
    catch (Throwable t)
    {
    }
    mover.start();
    toComp.setVisible(true);*/// </editor-fold>
  }

  /**
   * Alerts the OS that a window wants attention, usually resulting in the corresponding taskbar icon "flashing"
   * @param window the <tt>Window</tt> that wants attention
   */
  public static void flash(Window window)
  {
    flash(window, false);
  }

  /**
   * Alerts the OS that a window wants attention, usually resulting in the corresponding taskbar icon "flashing". An optional alert sound can be played, as well
   * @param window the <tt>Window</tt> that wants attention
   * @param ding a <tt>boolean</tt> which signifies whether you want the window to make an alert sound along with the flash
   */
  public static void flash(Window window, final boolean ding)
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
    if (ding)
      Toolkit.getDefaultToolkit().beep();
  }

  /**
   * Snaps a component to the edges of its parent when dragged near them. Only guaranteed to work on the primary monitor.<br />
   * <i><b>Note:</b> a glitch causes the window to snap too far from the respective lower or right edge when a taskbar, dock,
   * etc. is reserving the space on top or left. No glitches found when the item reserving the edge is on the bottom or right.<br/>
   * <br/>
   * <b>Note</b> A Java disability causes the window to be unable to maximize. Only use this for windows which you don'p plan
   * to be maximized.</i>
   * @param comp the component that is to snap to its parent's edges
   * @param dist the distance that defines "near", as an <tt>int</tt>. A distance of zero deactivates the ability.
   */
  public void setSnapsToEdges(final Component comp, final int dist)
  {
    if (snapCompListener == null)
      snapCompListener = new java.awt.event.ComponentAdapter()
      {
        @Override
        public void componentMoved(java.awt.event.ComponentEvent evt)
        {
          Rectangle b = comp.getBounds(), p = (comp.getParent() == null ? ge.getMaximumWindowBounds() : comp.getParent().getBounds());
          int bX = (int) b.getX(), bY = (int) b.getY(), bW = (int) b.getWidth(), bH = (int) b.getHeight(),
                  pX = (int) p.getX(), pY = (int) p.getY(), pW = (int) p.getWidth(), pH = (int) p.getHeight();
//          System.out.println(b + "\n" + p);
          comp.setBounds((bX <= dist + pX && bX >= pX - dist) ? pX : (bX + bW >= pW - dist && bX + bW <= pW + dist ? pW - bW : bX),
                         (bY <= dist + pY && bY >= pY - dist) ? pY : (bY + bH >= pH - dist && bY + bH <= pH + dist ? pH - bH : bY),
                         bW, bH);
        }
      };
    if (dist != 0)
    {
      comp.addComponentListener(snapCompListener);
    }
    else
    {
      comp.removeComponentListener(snapCompListener);
    }
  }

  /**
   * The component will fade in whilst moving upwards, giving a ghostly effect
   * @param comp the component to ghost in
   */
  public void ghostIn(Component comp)
  {
    ghostIn(comp, null);
  }

  /**
   * The component will fade in whilst moving upwards, giving a ghostly effect
   * @param comp the component to ghost in
   * @param endAction The action to perform at the end of the animation 
   */
  public void ghostIn(Component comp, java.awt.event.ActionListener endAction)
  {
    Point b = new Point(comp.getX(), comp.getY());
    int s = comp instanceof Frame ? ((Frame)comp).getState() : 0;
    setLocation(comp, new Rectangle((int)b.getX(), (int)b.getY() + 64, comp.getWidth(), comp.getHeight()));
    fadeIn(comp, DEF_BRAKE);
    
    goToLocation(comp, b, DEF_BRAKE, DEF_FPS, b, s, false, endAction);
  }

  /**
   * The component will fade away whilst moving upwards, giving a ghostly effect
   * @param comp the component to ghost away
   */
  public void ghostOut(Component comp)
  {
    ghostOut(comp, DEF_BRAKE);
  }

  /**
   * The component will fade away whilst moving upwards, giving a ghostly effect
   * @param comp the component to ghost away
   */
  public void ghostOut(Component comp, final float BRAKE)
  {
    ghostOut(comp, BRAKE, false);
  }

  /**
   * The component will fade away whilst moving upwards, giving a ghostly effect
   * @param comp the component to ghost away
   * @param exit if <tt>true</tt>, a <tt>System.exit(0);</tt> command will be given at the end of the animation.
   */
  public void ghostOut(Component comp, boolean exit)
  {
    ghostOut(comp, DEF_BRAKE, exit);
  }

  /**
   * The component will fade away whilst moving upwards, giving a ghostly effect
   * @param comp the component to ghost away
   * @param exit if <tt>true</tt>, a <tt>System.exit(0);</tt> command will be given at the end of the animation.
   * @since March 5, 2012 (2.0.11) For Marian
   */
  public void ghostOut(Component comp, final float BRAKE, boolean exit)
  {
    ghostOut(comp, BRAKE, exit, 0);
  }

  /**
   * The component will fade away whilst moving upwards, giving a ghostly effect
   * @param comp the component to ghost away
   * @param exit if <tt>true</tt>, a <tt>System.exit(status);</tt> command will be given at the end of the animation.
   * @param status the exit status to be used in <tt>exit</tt> is <tt>true</tt>
   */
  public void ghostOut(Component comp, boolean exit, int status)
  {
    ghostOut(comp, DEF_BRAKE, exit, status);
  }

  /**
   * The component will fade away whilst moving upwards, giving a ghostly effect
   * @param comp the component to ghost away
   * @param exit if <tt>true</tt>, a <tt>System.exit(status);</tt> command will be given at the end of the animation.
   * @param status the exit status to be used in <tt>exit</tt> is <tt>true</tt>
   * @since March 5, 2012 (2.0.11) For Marian
   */
  public void ghostOut(Component comp, final float BRAKE, boolean exit, int status)
  {
    ghostOut(comp, BRAKE, exit, status, null);
  }

  /**
   * The component will fade away whilst moving upwards, giving a ghostly effect
   * @param comp the component to ghost away
   * @param exit if <tt>true</tt>, a <tt>System.exit(status);</tt> command will be given at the end of the animation.
   * @param status the exit status to be used in <tt>exit</tt> is <tt>true</tt>
   * @param endAction The action to perform at the end of the animation
   * @param BRAKE the amount of a damper to put on the animation. 0.0 means that the first component will instantly disappear,
   * and any number higher means it will take longer.
   */
  public void ghostOut(Component comp, final float BRAKE, boolean exit, int status, java.awt.event.ActionListener endAction)
  {
    Point b = new Point(comp.getX(), comp.getY());
    fadeAway(comp, BRAKE, exit, status, endAction);
    goToLocation(comp, new Point((int)b.getX(), (int)b.getY() - 64), BRAKE, b, true);
  }


  /**
   * Utilizes all three types of window effects (moving, resizing, and fading) to morph a component to a different location andor size
   * @param comp the original component, to be morphed
   * @param to the location to which the component is being being morphed
   * second, and any number higher means it will take longer.
   */
  public void morphTo(final Component comp, Rectangle to)
  {
    morphTo(comp, to, DEF_BRAKE, null);
  }

  /**
   * Utilizes all three types of window effects (moving, resizing, and fading) to morph a component to a different location andor size
   * @param comp the original component, to be morphed
   * @param to the location to which the component is being being morphed
   * @param BRAKE the amount of a damper to put on the morph. 0.0 means that the first component will snap straight to the
   * second, and any number higher means it will take longer.
   * @param endAction the action to be performed when the animation is completed
   */
  public void morphTo(final Component comp, final Rectangle to, final float BRAKE, final java.awt.event.ActionListener endAction)
  {
    morphTo(comp, to, BRAKE, DEF_FPS, null, 0, false, endAction);
  }

  /**
   * Utilizes all three types of window effects (moving, resizing, and fading) to morph a component to a different location andor size
   * @param comp the original component, to be morphed
   * @param to the location to which the component is being being morphed
   * @param BRAKE the amount of a damper to put on the morph. 0.0 means that the first component will snap straight to the
   * second, and any number higher means it will take longer.
   * @param FPS The maximum speed of the animation, in frames per second
   * @param ret the location to which the component will return at the end of the animation. If <tt>null</tt>, it will stay
   * where it ends
   * @param retState the state at which to set the component at the end of the animation.
   * <b>ONLY WORKS IF THE COMPONENT IS A <TT>java.awt.Frame</TT>. ELSE IS IGNORED.</b>
   * @param hide if <tt>true</tt>, the component will be passed a <tt>.setVisible(false)</tt> command at the conclusion of the
   * animation
   * @param endAction the action to be performed when the animation is completed
   */
  public void morphTo(final Component comp, final Rectangle to, final float BRAKE, int FPS,
                      Rectangle ret, int retState, final boolean hide, final java.awt.event.ActionListener endAction)
  {
    if (BRAKE < 0)
      throw new IllegalArgumentException("BRAKE must be >= 0 (was set to " + BRAKE + ")");
    final Rectangle BEGIN = comp.getBounds(), here = new Rectangle(BEGIN);
    final int xDiff = to.x - comp.getX(), yDiff = to.y - comp.getY(),
            wDiff = to.width - comp.getWidth(), hDiff = to.height - comp.getHeight(),
            MAX_ITER = 64;//Added March 5, 2012 (2.0.11) for Marian
    stopMoving();
    thisComponent = comp;
    count = 0;//Added March 5, 2012 (2.0.11) for Marian
    mover = new javax.swing.Timer(FPS / 1000, new java.awt.event.ActionListener()
    {
      private float p = 0;
      @Override
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        float f = (p + 0) / 100;
        here.x = (int)(BEGIN.x + (xDiff * f));
        here.y = (int)(BEGIN.y + (yDiff * f));
        here.width = (int)(BEGIN.width + (wDiff * f)) + 1;
        here.height = (int)(BEGIN.height + (hDiff * f)) + 1;
        setLocation(comp, here);

        p = p + ((100 - p) / (BRAKE + 1));

        if (count++ > MAX_ITER ||//Added March 5, 2012 (2.0.11) for Marian
                p >= 99 ||
                (here.x == to.x && here.y == to.y && 
                here.width == to.width && here.height == to.height))
        {
          stopMoving();
          if (hide)
            comp.setVisible(false);
          setLocation(comp, to);
          if (endAction != null)
            endAction.actionPerformed(evt);
        }
      }
    });
    mover.start();
  }

  public void goToCenter(Component comp)
  {
    if (comp instanceof Window)
    {
      screenOffset();
      goToLocation(comp, new Point((int) (((noOffset ? SCREEN_BOUNDS : maxWinBounds).getWidth() / 2) - (comp.getSize().getWidth() / 2)) + offset,
                                             (int) (((noOffset ? SCREEN_BOUNDS : maxWinBounds).getHeight() / 2) - (comp.getSize().getHeight() / 2))));
    }
    else
      goToLocation(comp, new Point((int) ((comp.getParent().getWidth() / 2) - (comp.getSize().getWidth() / 2)) + offset,
                                             (int) ((comp.getParent().getHeight() / 2) - (comp.getSize().getHeight() / 2))));
  }
}
