package bht.tools.comps;

import bht.tools.comps.event.NavigationEvent;
import bht.tools.comps.event.NavigationEvent.NavigationState;
import static bht.tools.comps.event.NavigationEvent.NavigationState.STATE_GOING_BACKWARD;
import static bht.tools.comps.event.NavigationEvent.NavigationState.STATE_GOING_FORWARD;
import static bht.tools.comps.event.NavigationEvent.NavigationState.STATE_SKIPPING_BACKWARD;
import static bht.tools.comps.event.NavigationEvent.NavigationState.STATE_SKIPPING_FORWARD;
import bht.tools.comps.event.NavigationListener;
import bht.tools.util.ArrayPP;
import bht.tools.util.HistoryArray;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/**
 * BHNavButtons, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012 BH-1-PS.<hr/>
 *
 * @param <T> The type of thing that these buttons browse
 * @author Supuhstar of Blue Husky Programming
 * @since Apr 11, 2012
 * @version 1.0.0
 */
public class BHNavButtons<T> extends JPanel //NOTE: Must be compiled in UTF-8
{
	private JButton backButton, forwardButton, histButton;
	private JPopupMenu histPopupMenu;
	private HistoryArray<T> history;
	private ArrayPP<JMenuItem> histCompArray;
	private ArrayPP<NavigationListener> navigationListeners;
	private static final Insets DEF_MARGIN = new Insets(2, 2, 2, 2), DEF_INSETS = new Insets(4, 4, 4, 4);

	public BHNavButtons()
	{
		this(new HistoryArray<T>());
	}

	public BHNavButtons(HistoryArray<T> initHist)
	{
		history = initHist;

		history.addNavigationListener(new NavigationListener()
		{
			@Override
			public void willNavigate(NavigationEvent evt)
			{
				for (NavigationListener navigationListener : navigationListeners)
					navigationListener.willNavigate(evt);

				backButton.setEnabled(false);
				forwardButton.setEnabled(false);
				histButton.setEnabled(false);
			}

			@Override
			public void didNavigate(NavigationEvent evt)
			{
				fixNavButtons();

				for (NavigationListener navigationListener : navigationListeners)
					navigationListener.didNavigate(evt);
			}
		});

		navigationListeners = new ArrayPP<>();
		initGUI();
	}

	private void initGUI()
	{
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		setOpaque(false);

		//<editor-fold defaultstate="collapsed" desc="backButton">
		backButton = new JButton("◄");
		backButton.setMargin(DEF_MARGIN);
		backButton.setMinimumSize(new Dimension(24, 24));
		backButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				NavigationEvent evt = new NavigationEvent(backButton, history.getCurrentIndex(), history.getCurrentIndex() + 1,
														  history.getCurrent(), history.getVirtualBack(), STATE_GOING_BACKWARD);
				for (NavigationListener nl : navigationListeners)
					nl.willNavigate(evt);

				history.goBack();

				for (NavigationListener nl : navigationListeners)
					nl.didNavigate(evt);
			}
		});
		backButton.addMouseListener(new MouseAdapter()
		{
			@Override public void mouseClicked(MouseEvent e)
			{
				if (e.isPopupTrigger())
					showPopup(e);
			}

			@Override public void mouseDragged(MouseEvent e)
			{
				showPopup(e);
			}

			private void showPopup(MouseEvent e)
			{
				histPopupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		});
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(DEF_INSETS.top, DEF_INSETS.left, DEF_INSETS.bottom, 0);
		add(backButton, gbc);
    //</editor-fold>

		//<editor-fold defaultstate="collapsed" desc="forwardButton">
		forwardButton = new JButton("►");
		forwardButton.setMargin(DEF_MARGIN);
		forwardButton.setMinimumSize(new Dimension(24, 24));
		forwardButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int temp;
				NavigationEvent evt = new NavigationEvent(forwardButton, temp = history.getCurrentIndex(), temp + 1,
														  history.getCurrent(), history.getVirtualNext(), STATE_GOING_FORWARD);
				for (NavigationListener nl : navigationListeners)
					nl.willNavigate(evt);

				history.goNext();

				for (NavigationListener nl : navigationListeners)
					nl.didNavigate(evt);
			}
		});
		forwardButton.addMouseListener(new MouseAdapter()
		{
			@Override public void mouseClicked(MouseEvent e)
			{
				if (e.isPopupTrigger())
					showPopup(e);
			}

			@Override public void mouseDragged(MouseEvent e)
			{
				showPopup(e);
			}

			private void showPopup(MouseEvent e)
			{
				histPopupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		});
		gbc.gridx++;
		gbc.insets.left = 0;
		add(forwardButton, gbc);
    //</editor-fold>

		//<editor-fold defaultstate="collapsed" desc="histPopupMenu and histCompArray">
		histPopupMenu = new JPopupMenu();
		histCompArray = new ArrayPP<>();
		fixHistPopupMenu();
    //</editor-fold>

		//<editor-fold defaultstate="collapsed" desc="histButton">
		histButton = new JButton("▼");
		histButton.setMargin(new Insets(0, 0, 0, 0));
		histButton.addMouseListener(new MouseAdapter()
		{
			@Override public void mouseClicked(MouseEvent e)
			{
				showPopup(e);
			}

			@Override public void mouseDragged(MouseEvent e)
			{
				showPopup(e);
			}

			private void showPopup(MouseEvent e)
			{
//        histPopupMenu.show(e.getComponent(), e.getX(), e.getY());

			}
		});
		histButton.setComponentPopupMenu(histPopupMenu);
		histButton.setInheritsPopupMenu(true);
		gbc.gridx++;
		gbc.insets.right = DEF_INSETS.right;
		add(histButton, gbc);
		//</editor-fold>
	}

	private int histPos, histLength;
	private T histTemp;

	private void fixHistPopupMenu()
	{
		histCompArray.destroy();
		histPopupMenu.removeAll();

		boolean isCurrent = false, hasHitCurrent = false;
		for (histPos = 0, histLength = history.length(); histPos < histLength; histPos++)
		{
			histTemp = history.get(histPos);
			JMenuItem jmi = new JMenuItem(toPrettyString(histTemp));

			//The following ensures that the given booleans are only calculated and set when they have to be.
			if (!hasHitCurrent)
			{
				if (hasHitCurrent = histPos >= history.getCurrentIndex())
				{
					isCurrent = true;
					jmi.setEnabled(false);
				}
			}
			else if (isCurrent)
				isCurrent = false;

			jmi.setToolTipText(
				isCurrent
					? "You're already here!"
					: (hasHitCurrent
						? "Go forward to"
						: "Go back to")
				+ toPrettyString(histTemp));

			jmi.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					int temp;
					NavigationEvent evt
						= new NavigationEvent(
							backButton,
							temp = history.getCurrentIndex(),
							histPos,
							history.getCurrent(),
							history.getVirtualBack(),
							histPos > temp
								? STATE_SKIPPING_FORWARD
								: STATE_SKIPPING_BACKWARD
						)
					;
					for (NavigationListener nl : navigationListeners)
						nl.willNavigate(evt);

					history.goTo(histPos);

					for (NavigationListener nl : navigationListeners)
						nl.didNavigate(evt);
				}
			});
			histCompArray.add(jmi);
		}

		for (JMenuItem jmi : histCompArray)
			histPopupMenu.add(jmi);
	}

	public void fixNavButtons()
	{
		boolean b = history.canGoBack();
		backButton.setEnabled(b);
		backButton.setToolTipText(b ? "Go back to " + history.get(history.getCurrentIndex() - 1) : "You can't go back");

		forwardButton.setEnabled(b = history.canGoNext());
		forwardButton.setToolTipText(b ? "Go forward to " + history.get(history.getCurrentIndex() + 1) : "You can't go forward");
	}

	/**
	 * Adds the given {@link NavigationListener} to the list of listeners that fire whenever a navigation is performed. This
	 * object is added to the list regardless of whether it is already in the list.
	 *
	 * @param navigationListener
	 */
	public void addNavigationListener(NavigationListener navigationListener)
	{
		navigationListeners.add(navigationListener);
	}

	/**
	 * Removes the given {@link NavigationListener} from the list of listeners that fire whenever a navigation is performed.
	 * Only the first encountered instance of the given object is removed. If multiple instances have been added, this method
	 * will
	 * <b><i>not</i></b> remove them all, just one. If the given object is not found, this method returns normally.
	 *
	 * @param navigationListener
	 */
	public void removeNavigationListener(NavigationListener navigationListener)
	{
		navigationListeners.remove(navigationListener, false);
	}

	/**
	 * Returns the history behind these buttons
	 *
	 * @return the history behind these buttons
	 */
	public HistoryArray<T> getHistory()
	{
		return history;
	}

	private String toPrettyString(T histTemp)
	{
		return histTemp instanceof File ? ((File) histTemp).getName() : histTemp.toString();
	}
}
