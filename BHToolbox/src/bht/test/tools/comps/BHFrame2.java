package bht.test.tools.comps;

import bht.test.tools.fx.Colors2;
import bht.test.tools.fx.CompAction4;
import bht.test.tools.fx.CompAction5;
import bht.test.tools.fx.ani.AnimationEvent;
import bht.test.tools.fx.ani.AnimationListener;
import bht.test.tools.fx.ani.CompActionAnimation;
import bht.test.tools.fx.ani.ExpandAnimation;
import bht.tools.comps.BHCompUtilities;
import bht.tools.util.dynamics.Bézier2D;
import bht.tools.util.dynamics.ProgressingValue;
import bht.tools.util.save.SaveableDouble;
import bht.tools.util.save.StateSaver;
import java.awt.HeadlessException;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * BHFrame2, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012. License is default.<hr/>
 * A desperately-needed recreation of {@link bht.tools.comps.BHFrame}
 *
 * @author Kyli of Blue Husky Programming
 * @version 2.0.0
 * @since 2012/12/18
 */
public class BHFrame2 extends JFrame implements Serializable
{
	//<editor-fold defaultstate="collapsed" desc="Non-GUI Objects">
	private byte animationCenter = CompAction4.LOCATION_CENTER;
	private CharSequence appName = "Blue Husky App", title;
	private CompAction5 compAction = new CompAction5();
	private CompActionAnimation closingAnimation, openingAnimation;
	private Colors2 colors = new Colors2();
	private int defaultCloseOperation = getDefaultCloseOperation();
	private Logger log;
	private ProgressingValue animationCurve, reverseAnimationCurve;
	private StateSaver animationDurationSaver;
	private SaveableDouble closingAnimationDuration, animationFPS;
	//</editor-fold>

	//<editor-fold defaultstate="collapsed" desc="Meta">
	public BHFrame2(CharSequence initAppName) throws HeadlessException
	{
		this(initAppName, Logger.getLogger(BHFrame2.class.getName()));
	}
	
	public BHFrame2(CharSequence initAppName, Logger initLogger) throws HeadlessException
	{
		super();
		
		log = initLogger;
		
		appName = initAppName;

		//<editor-fold defaultstate="collapsed" desc="Animations">
		animationCurve = new Bézier2D();

		closingAnimation = new ExpandAnimation(animationCenter, getThis(), animationCurve);
		openingAnimation = new ExpandAnimation(animationCenter, getThis(), animationCurve, true);

		animationDurationSaver = new StateSaver(appName, true);
		try
		{
			closingAnimationDuration = new SaveableDouble(2, "closeDuration");
			animationDurationSaver.addSaveable(closingAnimationDuration);
			animationFPS = new SaveableDouble(2, "fps");
			animationDurationSaver.addSaveable(animationFPS);
		}
		catch (IOException e)
		{
			Logger.getGlobal().warning("Could not load animation save state");
		}
		//</editor-fold>

		initGUI();
	}

	private BHFrame2 getThis()
	{
		return this;
	}
	//</editor-fold>
	//<editor-fold defaultstate="collapsed" desc="GUI Creation">
	//<editor-fold defaultstate="collapsed" desc="GUI Objects">
	private JMenuBar menuBar;
	private JMenu appMenu, fileMenu, editMenu, viewMenu, toolsMenu, helpMenu;
	private static JMenu lookAndFeelMenu;
	//</editor-fold>

	private void initGUI()
	{
		BHCompUtilities.setUsesOSMenuBar(true, getTitle()); // allow the underlying platform to set its own menu bar (OS X, Ubuntu's Unity, etc.)
//    LookAndFeelChanger.setLookAndFeel(LookAndFeelChanger.SYSTEM);

		if ((menuBar = getJMenuBar()) == null) // check if the underlying system has already implemented its own menu bar
			menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		if ((appMenu = menuBar.getMenu(0)) == null) // check if the underlying platform has implemented its own app menu
			appMenu = new JMenu(appName.toString());
		appMenu.add(new JMenuItem("Custom menu item"));
	}
	//</editor-fold>

	public CompAction5 getCompAction()
	{
		return compAction;
	}

	public Colors2 getColors()
	{
		return colors;
	}
	private boolean animationLock = false;

	@Override
	public void setVisible(final boolean newVisibility)
	{
		if (!animationLock) // Ensure no recursion
		{
			animationLock = true;
			compAction.animate(getThis(), newVisibility ? closingAnimation : openingAnimation, closingAnimationDuration.getState(), animationFPS.getState(), new AnimationListener()
			{
				@Override
				public void animationStarting(AnimationEvent e)
				{
					if (newVisibility)
						setVisible(newVisibility);
				}

				@Override
				public void animationCompleted(AnimationEvent e)
				{
					if (newVisibility)
						setVisible(true);
					else
					{
						switch(getDefaultCloseOperation())
						{
							case DISPOSE_ON_CLOSE:
								dispose();
								setVisible(false);
								break;
							case DO_NOTHING_ON_CLOSE:
								break;
							case EXIT_ON_CLOSE:
								System.exit(0);
								setVisible(false); // Just in case, y'know?
								break;
							case HIDE_ON_CLOSE:
								setVisible(false);
								break;
							default:
								log.log(Level.SEVERE, "Invalid close operation specified: {0}", getDefaultCloseOperation());
						}
					}
					animationLock = false;
				}
			});
			return;
		}
		super.setVisible(newVisibility);
	}

	public BHFrame2 setClosingAnimation(CompActionAnimation newClosingAnimation)
	{
		closingAnimation = newClosingAnimation;
		return this;
	}

	public BHFrame2 setOpeningAnimation(CompActionAnimation newOpeningAnimation)
	{
		openingAnimation = newOpeningAnimation;
		return this;
	}

	@Override
	public void setTitle(String newTitle)
	{
		title = newTitle;
		refreshTitle();
	}

	@Override
	public String getTitle()
	{
		return String.valueOf(title);
	}
	
	public BHFrame2 setAppName(CharSequence newAppName)
	{
		appName = newAppName;
		return refreshTitle();
	}
	
	public BHFrame2 refreshTitle()
	{
		super.setTitle((title == null ? "" : title + " - ") + appName);
		return this;
	}
	
	public BHFrame2 setLogger(Logger newLogger)
	{
		log = newLogger;
		return this;
	}
}
