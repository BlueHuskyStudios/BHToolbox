package bht.tools.util;

import bht.tools.util.math.Numbers;
import java.util.EventListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



/**
 * BHTimer, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012. License is default.<hr/>
 * Acts like a lightweight version of {@link javax.swing.Timer}. This class performs a specified action until told to
 * stop, with
 * a specified delay between repetitions. This class takes advantage of separate threads, so make use of synchronized
 * methods
 * and volatile variables.
 *
 * @author Supuhstar of Blue Husky Programming
 * @since Dec 20, 2011
 * @version 1.2.1
 */
public class BHTimer {
	public static final byte PRIORITY_VERY_HIGH = Thread.MAX_PRIORITY,
			PRIORITY_NORMAL = Thread.NORM_PRIORITY,
			PRIORITY_VERY_LOW = Thread.MIN_PRIORITY,
			PRIORITY_HIGH = (byte) Numbers.mean(PRIORITY_VERY_HIGH, PRIORITY_VERY_HIGH, PRIORITY_NORMAL),
			PRIORITY_MEDIUM_HIGH = (byte) Numbers.mean(PRIORITY_VERY_HIGH, PRIORITY_NORMAL, PRIORITY_NORMAL),
			PRIORITY_MEDIUM_LOW = (byte) Numbers.mean(PRIORITY_VERY_LOW, PRIORITY_NORMAL, PRIORITY_NORMAL),
			PRIORITY_LOW = (byte) Numbers.mean(PRIORITY_VERY_LOW, PRIORITY_VERY_LOW, PRIORITY_NORMAL);
	private volatile ActionListener action;
	private volatile boolean running = false, rep = true;//rep added March 4, 2012 (1.1.0) for BHNotifier
	private volatile byte chosenPriority = PRIORITY_NORMAL;
	private volatile long delay;
	private final Thread timer;

	/**
	 * Creates a new BHTimer that will perform the given action repeatedly approximately the given number of times per
	 * second
	 *
	 * @param initRunnable The action that will be performed
	 * @param initDelay    The delay between performances
	 *
	 * @since 1.2.0 (2013-05-14) for BHIM Try 2
	 * @version 1.0.0
	 */
	public BHTimer(final Runnable initRunnable, long initDelay) {
		this(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				initRunnable.run();
			}
		}, initDelay);
	}

	/**
	 * Creates a new BHTimer that will perform the given action repeatedly approximately the given number of times per
	 * second
	 *
	 * @param initAction The action that will be performed
	 * @param initFPS    Then approximate number of times the given action will be performed every second
	 *
	 * @since 1.2.0 (2013-05-14) for BHIM Try 2
	 * @version 1.0.0
	 */
	public BHTimer(final Runnable initRunnable, double initFPS) {
		this(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				initRunnable.run();
			}
		}, initFPS);
	}

	/**
	 * Creates a new BHTimer that will perform the given action repeatedly approximately the given number of times per
	 * second
	 *
	 * @param initAction The action that will be performed
	 * @param initFPS    Then approximate number of times the given action will be performed every second
	 *
	 * @since 1.2.0 (2013-05-14) for BHIM Try 2
	 * @version 1.0.1
	 * - Changed "1000.0" to "1000d"
	 */
	public BHTimer(ActionListener initAction, double initFPS) {
		this(initAction, (long) (1000d / initFPS));
	}

	/**
	 * Creates a new BHTimer that will perform the given action repeatedly with the given delay
	 *
	 * @param initAction The action that will be performed
	 * @param initDelay  The delay between performances
	 *
	 * @since 1.0.0 (2011-12-20)
	 * @version 1.0.0
	 */
	public BHTimer(ActionListener initAction, long initDelay)//Reminds me of the Atlanta airport
	{
		action = initAction;
		delay = initDelay;
		(timer = new Thread(new Runnable() {
			@Override
			public synchronized void run() {
//        synchronized (timer)
//        {
				ActionEvent evt = new ActionEvent(getThis(), 0, null);//Added May 6, 2012 (1.1.2) for BHMI
				do {
					try {
						if (delay != 0) {
							wait(delay);
						}
						if (running) {
							action.actionPerformed(evt);
						}
//						 else
//							 wait();
					}
					catch (InterruptedException ex) {
						Logger.getGlobal().log(Level.SEVERE, "Thread was awoken", ex);
					}
				} while (rep);//changed to do...while(rep) March 4, 2012 (1.1.0) for BHNotifier
//        }
			}
		}, "BHTimer_" + this)).start();
	}

	@SuppressWarnings("FinalPrivateMethod")
	private final BHTimer getThis() {
		return this;
	}

	/**
	 * Starts or resumes the timer with the priority chosen by {@link #setPriority(byte)}
	 */
	public synchronized void start() {
		timer.setPriority(chosenPriority);
		running = true;
//    timer.notify();
		notifyAll();
	}

	/**
	 * Pauses the timer just before the next scheduled repetition and sets the priority to "very low"
	 */
	public void stop() {
		running = false;
		timer.setPriority(PRIORITY_VERY_LOW);
	}

	/**
	 * Sets the delay in milliseconds between action firings. This value must be higher than or equal to 0. If it is
	 * less than
	 * 0, the delay will be set to 0.
	 *
	 * @param millis the new delay between firings, in milliseconds.
	 */
	public void setDelay(long millis) {
		delay = Math.max(millis, 0);
	}

	/**
	 * Returns the delay of the timer
	 *
	 * @return the target number of milliseconds between cycles
	 */
	public long getDelay() {
		return delay;
	}

	public void setAction(ActionListener newAction) {
		action = newAction;
	}

	/**
	 * Returns the action performed every repetition
	 *
	 * @return the action performed every repetition
	 *
	 * @version 1.0.0
	 */
	public EventListener getAction() {
		return action;
	}

	/**
	 * Sets the priority of the timer thread. Any value between {@link #PRIORITY_VERY_HIGH} and
	 * {@link #PRIORITY_VERY_LOW},
	 * inclusive can be used.
	 *
	 * @param priority the new thread priority
	 *
	 * @see #PRIORITY_VERY_HIGH
	 * @see #PRIORITY_HIGH
	 * @see #PRIORITY_MEDIUM_HIGH
	 * @see #PRIORITY_NORMAL
	 * @see #PRIORITY_MEDIUM_LOW
	 * @see #PRIORITY_LOW
	 * @see #PRIORITY_VERY_LOW
	 *
	 * @version 1.0.0
	 */
	public void setPriority(byte priority) {
		System.out.println("Setting priority: " + priority);
		if (priority < PRIORITY_VERY_LOW || priority > PRIORITY_VERY_HIGH) {
			throw new IllegalArgumentException(
					"priority (" + priority + ") must be between PRIORITY_VERY_LOW ("
							+ PRIORITY_VERY_LOW + ") and PRIORITY_VERY_HIGH (" + PRIORITY_VERY_HIGH
							+ "), inclusive"
			);
		}
		timer.setPriority(chosenPriority = priority);
	}

	/**
	 * Asynchronously halts the timer
	 *
	 * @since March 4, 2012 (1.1.0) for BHNotifier
	 * @version 1.0.0
	 */
	public void reset() {
		timer.stop();
	}

	/**
	 * Asynchronously sets whether the timer repeats
	 *
	 * @param shouldRepeat if {@code true}, the timer repeats. If false, will perform at most one more action before
	 *                     pausing the timer.
	 *
	 * @since March 4, 2012 (1.1.0) for BHNotifier
	 * @version 1.0.0
	 */
	public void setRepeats(boolean shouldRepeat) {
		rep = shouldRepeat;
	}

	/**
	 * Set the repetition interval of the timer, in repetitions per second. This is a target interval and is
	 * <EM>not</EM> guaranteed to be met
	 *
	 * @param framesPerSecond the number of updates per second
	 *
	 * @since 2013-09-11 (1.2.1) for BLISS
	 * @version 1.0.0
	 */
	public void setDelay(double framesPerSecond) {
		setDelay((long) (1000d / framesPerSecond));
	}
}
