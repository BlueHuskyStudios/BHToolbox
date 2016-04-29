package bht.test.tools.util;

import bht.resources.Icons;
import bht.tools.comps.BHComponent;
import bht.tools.fx.Colors;
import bht.tools.fx.CompAction;
import bht.tools.fx.CompAction.ScreenLoc;
import bht.tools.fx.LookAndFeelChanger;
import bht.tools.util.ArrayPP;
import bht.tools.util.BHTimer;
import bht.tools.util.save.Saveable;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

/**
 * BHNotifier, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012 BH-1-PS.<hr>
 *
 * @author Supuhstar of Blue Husky Programming
 * @since Mar 3, 2012
 * @version 1.0.0
 */
@SuppressWarnings("UseOfSystemOutOrSystemErr")
public class BHNotifier //NOTE: Must be compiled in UTF-8
{
  private static ArrayPP<BHNotifierDialog> dialogs;
  private static final Font DEF_TITLE_FONT = new Font("Segoe UI Light", Font.PLAIN, 14),
          DEF_MESSAGE_FONT = new Font("Segoe UI", Font.PLAIN, 11);
  private static int padding = 8;
  private static final Rectangle SCREEN_BOUNDS = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

  static//public BHNotifier()
  {
    dialogs = new ArrayPP<>();
  }

  public static void main(String args[])
  {
    System.out.println("Starting");
    LookAndFeelChanger.setLookAndFeel(LookAndFeelChanger.NIMBUS);
    BHNotifier.notifyOf("I'm a message!", "I'm a title!");
    BHNotifier.notifyOf("I'm another message!", "I'm another title!");
    BHNotifier.notifyOf("What a twist!!", "I'm a different title!");
    
    JFrame jf = new JFrame("Notifier test");
    JButton b = new JButton("Notify!");
    b.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e)
      {
        BHNotifier.notifyOf("This is a notification with a rather long message! It was shown at " + System.currentTimeMillis(), "Longness!!");
      }
    });
    jf.add(b, BorderLayout.CENTER);
    jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    jf.pack();
    jf.setVisible(true);
  }

  public static void notifyOf(CharSequence message, CharSequence title)
  {
    System.out.println("BHNotifier#notifyOf(\"" + message + "\", \"" + title + "\")");
    final BHNotifierDialog bhnd = new BHNotifierDialog(message, title);
    dialogs.add(bhnd);
    bhnd.addWindowListener(new WindowAdapter()
    {
      @Override
      public void windowClosing(WindowEvent e)
      {
        System.out.println("BHNotifier$WindowAdapter#windowClosing(" + e + ")");
        dialogs.remove(bhnd, true);
        showAllNotifications();
      }
    });
    bhnd.setLocation(getPreferredLocation());
    showAllNotifications();
  }

  private static void showAllNotifications()
  {
    System.out.println("BHNotifier#showAllNotifications()");
    int x = SCREEN_BOUNDS.width, y = SCREEN_BOUNDS.height, w = 0;
//    Rectangle locs[] = new Rectangle[dialogs.trimInside().length()];
    if (dialogs.isEmpty())//If there are no notifications to show, we need not fix anything
      return;
    for (BHNotifierDialog d : dialogs)
    {
      w = Math.max((int) Math.round(d.getPreferredSize().getWidth()), w);
    }

//    w += padding;
    x -= w + padding;
    w = Math.min(w, dialogs.getFirstItem().getMaximumSize().width);
//    h = Math.min(h, dialogs.getFirstItem().getMaximumSize().height);

    for (int i = 0, l = dialogs.length(); i < l; i++)
    {
      if (!dialogs.has(i))//Due to threading shenanegans, the size of the array might change during this method
        continue;
//      dialogs.get(i).pack();
      dialogs.get(i).setVisible(true);
      dialogs.get(i).getCompAction().morphTo(dialogs.get(i),
          new Rectangle(x, y -= dialogs.get(i).getHeight() + padding, w, dialogs.get(i).getHeight()));
    }
  }

  private static ScreenLoc getPreferredEdge()
  {
    return CompAction.ScreenLoc.RIGHT;
  }

  private static Point getPreferredLocation()
  {
    return new Point(SCREEN_BOUNDS.width, SCREEN_BOUNDS.height);
  }

  private static class BHNotifierDialog extends JDialog implements BHComponent
  {
    private JLabel titleLabel, closeButton;
    private JTextArea messageLabel;
    
    private ArrayPP<WindowListener> windowListeners;
    private ArrayPP<ActionListener> actionListeners = new ArrayPP<>();
    private BHTimer decayTimer;
    private CompAction ca;
    private Colors c;
    private long decay;
    public static final long DEF_DECAY = 10000;

    public BHNotifierDialog(CharSequence message, CharSequence title)
    {
      this(message, title, DEF_DECAY);
    }
    
    public BHNotifierDialog(CharSequence message, CharSequence title, long decayInMillis)
    {
      System.out.println("BHNotifierDialog(\"" + message + "\", \"" + title + "\", " + decayInMillis + ")");
      ca = new CompAction();
      c = new Colors();
      decay = decayInMillis;
      decayTimer = new BHTimer(new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          System.out.println("BHNotifier$ActionListener#actionPerformed(" + e + ")");
          close(new WindowEvent(getThis(), e.getID()));
        }
      }, decay);
      decayTimer.setRepeats(false);

      initGUI();
      setMessage(message);
      setTitle(title);

      setTitle(title.toString());
      setAlwaysOnTop(true);
    }

    private BHNotifierDialog getThis()
    {
      return this;
    }

    //<editor-fold defaultstate="collapsed" desc="BHComponent Overrides">
    @Override
    public CompAction getCompAction()
    {
      return ca;
    }

    @Override
    public Colors getColors()
    {
      return c;
    }

    @Override
    public Saveable setSaveName(CharSequence newName)
    {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CharSequence getSaveName()
    {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CharSequence getStringToSave()
    {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Saveable loadFromSavedString(CharSequence savedString)
    {
      throw new UnsupportedOperationException("Not supported yet.");
    }
    //</editor-fold>

    private void initGUI()
    {
      System.out.println("BHNotifierDialog#initGUI()");
      //<editor-fold defaultstate="collapsed" desc="this">
      System.out.println("BHNotifierDialog#initGUI(): Adding listeners");
      addMouseListener(new MouseAdapter()
      {
        @Override
        public void mouseClicked(MouseEvent e)
        {
          //          System.out.println("Button " + e.getButton() + " pressed");
          if (e.getButton() == MouseEvent.BUTTON3)
            close(new WindowEvent(getThis(), e.getID()));
        }
      });
      /*addComponentListener(new ComponentAdapter()
       * {
       * @Override
       * public void componentResized(ComponentEvent e)
       * {
       * fixShape();
       * }
       * 
       * @Override
       * public void componentMoved(ComponentEvent e)
       * {
       * fixShape();
       * }
       * });*/
      super.addWindowListener(new WindowAdapter()
      {
        @Override
        public void windowOpened(WindowEvent e)
        {
          System.out.println("BHNotifierDialog$WindowAdapter#windowOpened(" + e + ")");
          decayTimer.start();
        }

        @Override
        public void windowClosed(WindowEvent e)
        {
          System.out.println("BHNotifierDialog$WindowAdapter#windowClosed(" + e + ")");
          dispose();
        }
      });
      System.out.println("BHNotifierDialog#initGUI(): Setting initial frame settings...");
      getRootPane().setBorder(new LineBorder(c.getColor(Colors.BORDER), 2));
      setLayout(new GridBagLayout());
      setMaximumSize(new Dimension(SCREEN_BOUNDS.width / 6, SCREEN_BOUNDS.height / 6));
      setResizable(false);
      setUndecorated(true);
      GridBagConstraints gbc = new GridBagConstraints();
      //</editor-fold>

      //<editor-fold defaultstate="collapsed" desc="titleLabel">
      System.out.println("BHNotifierDialog#initGUI(): Initializing and adding titleLabel...");
      titleLabel = new JLabel("Alert", SwingConstants.RIGHT);
      titleLabel.setFont(DEF_TITLE_FONT);
      titleLabel.getAccessibleContext().setAccessibleDescription(Colors.FONT_OVERRIDE + DEF_TITLE_FONT.getName() + Colors.COMMAND_SEP +
                                                                 Colors.SIZE_ONLY_OVERRIDE + DEF_TITLE_FONT.getSize() + Colors.COMMAND_SEP);
      gbc.fill = GridBagConstraints.BOTH;
      gbc.gridx = gbc.gridy = 0;
      gbc.ipadx = gbc.ipady = 2;
      gbc.weightx = 1;
      add(titleLabel, gbc);
      //</editor-fold>
      
      //<editor-fold defaultstate="collapsed" desc="closeButton">
      System.out.println("BHNotifierDialog#initGUI(): Initializing and adding closeButton...");
      closeButton = new JLabel(Icons.getIcon(Icons.CLOSE_12));
      closeButton.addMouseListener(new MouseAdapter()
      {
        @Override
        public void mouseClicked(MouseEvent e)
        {
          close(new WindowEvent(getThis(), e.getID()));
        }
      });
      closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      gbc.gridx++;
      gbc.weightx = 0;
      add(closeButton, gbc);
      //</editor-fold>

      //<editor-fold defaultstate="collapsed" desc="messageLabel">
      System.out.println("BHNotifierDialog#initGUI(): Initializing and adding messageLabel...");
      messageLabel = new JTextArea("Something happened", 0, 0);
      messageLabel.setLineWrap(true);
      messageLabel.setWrapStyleWord(true);
      messageLabel.setEditable(false);
      messageLabel.setBorder(new JButton().getBorder());
      messageLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      messageLabel.setFont(DEF_MESSAGE_FONT);
      messageLabel.getAccessibleContext().setAccessibleDescription(Colors.FONT_OVERRIDE + DEF_MESSAGE_FONT.getName() + Colors.COMMAND_SEP +
                                                                   Colors.SIZE_ONLY_OVERRIDE + DEF_MESSAGE_FONT.getSize() + Colors.COMMAND_SEP);
      messageLabel.addMouseListener(new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e)
        {
          if (e.getButton() == MouseEvent.BUTTON1)
            doActionPerformed(new ActionEvent(messageLabel, e.getID(), null));
          else if (e.getButton() == MouseEvent.BUTTON3)
            close(new WindowEvent(getThis(), e.getID()));
            
        }
      });
      gbc.gridwidth = GridBagConstraints.REMAINDER;
      gbc.gridx = 0;
      gbc.gridy++;
      gbc.weighty = 1;
      add(messageLabel, gbc);
      //</editor-fold>
      
      System.out.println("BHNotifierDialog#initGUI(): Setting final frame settings...");
      pack();
      c.fixStyleIn(this);
    }

    private void fixShape()
    {
      Shape s = new Shape()
      {
        double x = getThis().getX(), y = getThis().getY(), w = getThis().getWidth(), h = getThis().getHeight(), aw = 4, ah = 4;

        @Override
        public Rectangle2D getBounds2D()
        {
          return getThis().getBounds();
        }

        @Override
        public Rectangle getBounds()
        {
          return getThis().getBounds();
        }

        @Override
        public boolean contains(double x, double y)
        {
          return getThis().getBounds().contains(x, y);
        }

        @Override
        public boolean contains(Point2D p)
        {
          return getThis().getBounds().contains(p);
        }

        @Override
        public boolean intersects(double x, double y, double w, double h)
        {
          return getThis().getBounds().intersects(x, y, w, h);
        }

        @Override
        public boolean intersects(Rectangle2D r)
        {
          return getThis().getBounds().intersects(r);
        }

        @Override
        public boolean contains(double x, double y, double w, double h)
        {
          return getThis().getBounds().contains(x, y, w, h);
        }

        @Override
        public boolean contains(Rectangle2D r)
        {
          return getThis().getBounds().contains(r);
        }

        @Override
        public PathIterator getPathIterator(AffineTransform at)
        {
          return getThis().getBounds().getPathIterator(at);
        }

        @Override
        public PathIterator getPathIterator(AffineTransform at, double flatness)
        {
          return getThis().getBounds().getPathIterator(at, flatness);
        }
      };
      setShape(s);
      revalidate();
      doLayout();
    }
    
    public void setMessage(CharSequence message)
    {System.out.println("setMessage(\"" + message + "\")");
      messageLabel.setText(message.toString());
      pack();
    }

    public void setTitle(CharSequence title)
    {System.out.println("setTitle(\"" + title + "\")");
      titleLabel.setText(title.toString());
      pack();
    }

    @Override
    protected void finalize() throws Throwable
    {
      super.finalize();
    }

    @Override
    public synchronized void addWindowListener(WindowListener l)
    {
      System.out.println("BHNotifierDialog#addWindowListener(" + l + ")");
      
      if (windowListeners == null)
        windowListeners = new ArrayPP<>();
      windowListeners.add(l);
    }

    @Override protected void processWindowEvent(WindowEvent e)
    {
      System.out.println("BHNotifierDialog#processWindowEvent(" + e + ")");
      super.processWindowEvent(e);
      for (WindowListener listener : windowListeners)
        if (listener != null)
        {
          switch (e.getID())
          {
            case WindowEvent.WINDOW_OPENED:
              listener.windowOpened(e);
              break;
            case WindowEvent.WINDOW_CLOSING:
              listener.windowClosing(e);
              break;
            case WindowEvent.WINDOW_CLOSED:
              listener.windowClosed(e);
              break;
            case WindowEvent.WINDOW_ICONIFIED:
              listener.windowIconified(e);
              break;
            case WindowEvent.WINDOW_DEICONIFIED:
              listener.windowDeiconified(e);
              break;
            case WindowEvent.WINDOW_ACTIVATED:
              listener.windowActivated(e);
              break;
            case WindowEvent.WINDOW_DEACTIVATED:
              listener.windowDeactivated(e);
              break;
            default:
              break;
          }
        }
    }

    public void close(WindowEvent e)
    {
      System.out.println("BHNotifierDialog#close(" + e + ")");
      for (WindowListener wl : windowListeners)
        wl.windowClosing(e);

      ca.slideOut(getThis(), getPreferredEdge(), CompAction.DEF_BRAKE * 4);
      
      for (WindowListener wl : windowListeners)
        wl.windowClosed(e);
    }
    
    public void addActionListener(ActionListener al)
    {
      actionListeners.add(al);
    }
    
    public void removeActionListener(ActionListener al)
    {
      actionListeners.remove(al, true);
    }
    
    public void doActionPerformed(ActionEvent evt)
    {
      for (ActionListener actionListener : actionListeners)
      {
        actionListener.actionPerformed(evt);
      }
    }
  }
}