package bht.test.tools.fx;

import bht.tools.fx.Animation;
import bht.tools.util.ArrayPP;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionListener;

/**
 * _legacy_CompAnim, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012. License is default.<hr/>
 * A separation of abilities from {@link bht.tools.fx.CompAction}. This class, unlike the original, makes use of the new {@link
 * Animation} interface to allow the animation to be paused, rewound, or skipped in any order. The original will still be kept
 * and used for things like its {@link bht.tools.fx.CompAction#flash(java.awt.Window, boolean)},
 * {@link bht.tools.fx.CompAction#snapToCenter(java.awt.Component)}, and
 * {@link bht.tools.fx.CompAction#snapToCorner(java.awt.Component, bht.tools.fx.CompAction.ScreenLoc)} methods.
 * @author Kyli of Blue Husky Studios
 * @deprecated No longer being developed
 */
public class _legacy_CompAnim
{
  /**
   * The target component to be animated.
   */
  private Component target;
  private ArrayPP<Stop> stops;
  
  

  /**
   * Creates a new {@link _legacy_CompAnim} which affects the given component.
   * @param target the component to be animated
   */
  public _legacy_CompAnim(Component target)
  {
    this.target = target;
    stops = new ArrayPP<>();
  }
  
  
  
  /**
   * Sets the location of the animation immediately, without a transition.
   * @param PERCENTAGE the location in the animation to which the component will snap.
   */
  public void snapTo(final double PERCENTAGE)
  {
    //TODO: Search for the two closest stops
    //TODO: IF one of these stops has the same percentage as the given one
      //TODO: Set the state to be exactly that defined by the stop
    //TODO: ELSE
      //TODO: Set the component's state to be a certain state between these stops
  }
  
  /**
   * Adds a stop to the animation "timeline". This defines a state of the animation and does not literally stop the animation.
   * @param stop The {@link Stop} to be added
   */
  public void addStop(Stop stop)
  {
    stops.add(stop);
  }
  
  /**
   * Defines the state of a component at certain locations along the animation "timeline"
   */
  public static class Stop
  {
    private final Rectangle L, D;
    private final double T, P;
    private final boolean S, E;
    private final ActionListener A;
    private final int X;
    
    /**
     * Defines the state of this stop
     * @param PERCENTAGE The location of this stop in the animation. {@code 0.0} is the beginning of the animation and {@code
     * 1.0} is the end.
     * @param LOC The location of the component at this stop. If {@code null}, the component will not move when approaching this
     *    stop from the left.
     * @param TRANSPARENCY The transparency of the component at this stop. {@code 0.0} is opaque and {@code 1.0} is invisible.
     * @param SHOULD_SHOW If true, the component should be visible at and after this stop. If false, the component should be
     *    hidden at and after this stop.
     * @param DEF_LOC The default location of the component at this stop. This is the location of the component when the stop is
     *    observed at or to the right of its percentage. If {@code null}, then {@code LOC} is used, instead.
     * @param ACTION The action to be performed when this stop is observed directly. If {@code null}, no action will be taken.
     * @param SHOULD_EXIT If {@code true}, the application will exit at or after this stop.
     * @param EXIT_STATUS If {@code SHOULD_EXIT} is {@code true}, this is the status passed to the JVM upon exiting.
     * @see ActionListener
     * @see System#exit(int)
     */
    public Stop(final double PERCENTAGE, final Rectangle LOC, final double TRANSPARENCY, boolean SHOULD_SHOW,
                final Rectangle DEF_LOC, final ActionListener ACTION, final boolean SHOULD_EXIT, final int EXIT_STATUS)
    {
      P = PERCENTAGE;
      L = LOC;
      T = TRANSPARENCY;
      S = SHOULD_SHOW;
      D = DEF_LOC;
      A = ACTION;
      E = SHOULD_EXIT;
      X = EXIT_STATUS;
    }
  }
}
