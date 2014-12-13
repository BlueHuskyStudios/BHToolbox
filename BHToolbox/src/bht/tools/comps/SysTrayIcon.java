package bht.tools.comps;

import bht.tools.util.ArrayPP;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A convenience class for creating and managing a System Tray icon
 * @author Supuhstar of Blue Husky Programming
 * @since sometime in 2010
 * @version 1.2.0
 */
public class SysTrayIcon
{
  private ArrayPP<ActionListener> als;
  private SystemTray st;
  private TrayIcon ti;
  
  public static final byte MSG_INFO = 0;
  public static final byte MSG_WARNING = 1;
  public static final byte MSG_ERROR = 2;
  public static final byte MSG_NONE = 3;
  
  /**
   * Creates a new <tt>SysTrayIcon</tt> and adds it to the system tray (AKA the "Taskbar Status Area" or "Notification Area").
   * @param icon the icon representing the application in the system tray
   * @param toolTip the tip that will be shown upon hovering over the icon
   * @param popupMenu the menu that will be shown upon right-clicking the icon
   * @throws AWTException if the system tray is missing
   */
  public SysTrayIcon(Image icon, String toolTip, java.awt.PopupMenu popupMenu) throws AWTException
  {
    st = SystemTray.getSystemTray();
    ti = new TrayIcon(icon, toolTip, popupMenu);
    try
    {
      st.add(ti);
    }
    catch (AWTException ex)
    {
      throw ex;//I used to do something with this... might still.
    }
    ti.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        for (ActionListener actionListener : als)
        {
          actionListener.actionPerformed(e);
        }
      }
    });
  }
  
  /**
   * Allows you to change the icon shown in the system tray to the specified image
   * @param newIcon the new icon
   * @return the resulting {@code this}
   */
  public SysTrayIcon setIcon(Image newIcon)
  {
    ti.setImage(newIcon);
    return this;
  }
  
  /**
   * Returns the icon shown in the system tray as an {@link Image}
   * @return  the icon shown in the system tray
   */
  public Image getIcon()
  {
    return ti.getImage();
  }
  
  /**
   * Allows you to change the tooltip for the icon to the specified text
   * @param newTip the new tooltip
   * @return the resulting {@code this}
   */
  public SysTrayIcon setToolTipText(CharSequence newTip)
  {
    ti.setToolTip(newTip.toString());
    return this;
  }
  
  /**
   * Returns the text shown for the icon
   * @return  the text shown for the icon
   */
  public CharSequence getToolTipText()
  {
    return ti.getToolTip();
  }
  
  /**
   * Allows you to change the popup (context/right-click) menu for the icon to the specified one
   * @param newPopupMenu the new popup (context/right-click) menu
   * @return the resulting {@code this}
   */
  public SysTrayIcon setPopupMenu(PopupMenu newPopupMenu)
  {
    ti.setPopupMenu(newPopupMenu);
    return this;
  }
  
  /**
   * Returns the popup (context/right-click) menu for the icon
   * @return popup (context/right-click) menu for the icon
   */
  public PopupMenu getPopupMenu()
  {
    return ti.getPopupMenu();
  }

  /**
   * Adds an {@link ActionListener} whose {@link ActionListener#actionPerformed(java.awt.event.ActionEvent)} method will be
   * called whenever the tray icon is clicked.
   * @param actionListener the {@link ActionListener} to add
   * @since March 26, 2012 (1.2.0) for Marian
   */
  public void addActionListener(ActionListener actionListener)
  {
    if (als == null)
    {
      als = new ArrayPP<ActionListener>(actionListener);
      return;
    }
    als.add(actionListener);
  }

  /**
   * Removes an {@link ActionListener}
   * @param actionListener the {@link ActionListener} to remove
   * @see #addActionListener(java.awt.event.ActionListener) 
   * @since March 26, 2012 (1.2.0) for Marian
   */
  public void removeActionListener(ActionListener actionListener)
  {
    if (als == null)
    {
      return;
    }
    als.remove(actionListener, false);
  }
  
  public void showBalloon(String title, String body, byte type)
  {
	  MessageType parsedType;
	  switch(type)
	  {
		  case MSG_INFO:          parsedType = MessageType.INFO;    break;
		  case MSG_WARNING:       parsedType = MessageType.WARNING; break;
		  case MSG_ERROR:         parsedType = MessageType.ERROR;   break;
		  default: case MSG_NONE: parsedType = MessageType.NONE;    break;
	  }
	  ti.displayMessage(title, body, parsedType);
  }
}
