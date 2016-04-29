package bht.tools.comps.gameboard;

import bht.tools.comps.event.StateChangeEvent;
import bht.tools.comps.event.StateChangeListener;
import bht.tools.util.ArrayPP;
import bht.tools.util.math.Numbers;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**
 * BHBoardSquare, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012.<hr>
 * @author Supuhstar of Blue Husky Programming
 * @version 1.0.0
 * @since Jan 16, 2012
 */
public class BHBoardSquare extends Component implements DragGestureListener, DropTargetListener, DragSourceListener, Transferable //NOTE: Must be compiled in UTF-8
{
  private ArrayPP<ActionListener> als;
  private ArrayPP<StateChangeListener> cls;
  private boolean selected = false, dragOver = false, opacity;
  private Border border;
  public static final byte STATE_LEFT_OPEN   = 0b0001,
                           STATE_TOP_OPEN    = 0b0010,
                           STATE_RIGHT_OPEN  = 0b0100,
                           STATE_BOTTOM_OPEN = 0b1000,
                           STATE_DEFAULT     = 0b0000;
  public static final byte SHAPE_CIRCLE  = 1,
                           SHAPE_ELIPSE  = 2,
                           SHAPE_SQUARE  = 3,
                           SHAPE_RECT    = 4,
                           SHAPE_DEFAULT = SHAPE_CIRCLE;
  private byte st, sh;
  private CharSequence t;
  private Color color;
  private short borderThickness = 2;
  
  
  public BHBoardSquare()
  {
    this(STATE_DEFAULT, SHAPE_DEFAULT);
  }
  
  public BHBoardSquare(byte state, byte shape)
  {
    st = state;
    sh = shape;
    als = new ArrayPP<>();
    cls = new ArrayPP<>();
    addMouseListener(new MouseAdapter()
    {
      @Override
      public void mouseClicked(MouseEvent e)
      {
        if (isEnabled())
        {
          ActionEvent evt = new ActionEvent(e.getSource(), e.getID(), null, e.getWhen(), e.getModifiersEx());
          setSelected(!selected);
          for (ActionListener al : als)
            al.actionPerformed(evt);
        }
        repaint();
      }
    });
    addKeyListener(new KeyAdapter() {

      @Override
      public void keyPressed(KeyEvent e)
      {
        if (e.isActionKey())
        {
//          System.out.println("ACTION!");
        }
        if (e.getExtendedKeyCode() == KeyEvent.VK_SPACE)
        {
//          System.out.println("SPAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACE!!!!!!");
        }
        else if (e.getExtendedKeyCode() == KeyEvent.VK_ENTER || e.getExtendedKeyCode() == KeyEvent.VK_ACCEPT)
        {
//          System.out.println("LET'S DO THIS!!!");
        }
      }
    });
    addFocusListener(new FocusListener()
    {
      @Override
      public void focusGained(FocusEvent fe)
      {
        repaint();
      }

      @Override
      public void focusLost(FocusEvent fe)
      {
        repaint();
      }
    });
    
    setDropTarget(new DropTarget(this, DnDConstants.ACTION_MOVE, this, true));
    setFocusable(true);
  }
  
  private BHBoardSquare getThis()
  {
    return this;
  }
  
//  private Thread paintThread;

  @Override
  @SuppressWarnings("UseOfSystemOutOrSystemErr")
  public void paint(final Graphics g)
  {
    if (g instanceof Graphics2D)
      ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
    /*
    if (paintThread != null)
      paintThread.stop();
    paintThread = new Thread(new Runnable()
    {
      @Override
      public void run()
      {*/
        //<editor-fold defaultstate="collapsed" desc="Constants">
        final int W = getWidth(), H = getHeight();
        final int diam = Math.min(H - 4, W - 4);
        final int bumper = (int)Math.round(diam / (borderThickness * 4.0)), bumper2 = bumper * 2;
        final double hW = W / 2.0, hH = H / 2.0;
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Draw the Background">
        if (isOpaque())
        {
          g.setColor(getBackground());
          g.fillRect(0, 0, W, H);
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Draw the Piece">
        if (color != null)
        {
          {
            int a;
            g.setColor(isEnabled() ? color : (color.getRed() >= 127 || color.getGreen() >= 127 || color.getBlue() >= 127 ?
                new Color((int)Numbers.mean(color.getRed(), 0),
                          (int)Numbers.mean(color.getGreen(), 0),
                          (int)Numbers.mean(color.getBlue(), 0)) :
                new Color((int)Numbers.mean(color.getRed(), 255),
                          (int)Numbers.mean(color.getGreen(), 255),
                          (int)Numbers.mean(color.getBlue(), 255))));//new Color((a = (color.getRed() + color.getGreen() + color.getBlue()) / 3) > 127 ? Math.max(a * 2, 255) : a / 2, a, a));
          }
          {
            double rad = diam / 2.0;
            Color invert = new Color(0xFFFFFF - color.getRGB());
            if (!isEnabled())
            {
              int a;
              invert = new Color(a = (a = (invert.getRed() + invert.getGreen() + invert.getBlue()) / 3) > 127 ?
                                     Math.min(a * 2, 255) : a / 2, a, a);
            }
            switch (sh)
            {
              default:
                System.err.println("Unknown shape mask: " + sh);
              case SHAPE_CIRCLE:
                g.fillOval((int) (hW - rad), (int) (hH - rad), diam, diam);
                if (selected)
                {
                  g.setColor(invert);
                  g.fillOval((int) (hW - rad) + bumper, (int) (hH - rad) + bumper, diam - bumper2, diam - bumper2);//Draw an  inversely colored circle within the piece
                }
                break;
              case SHAPE_ELIPSE:
                g.fillOval(bumper, bumper, getWidth() - bumper2, getHeight() - bumper2);
                if (selected)
                {
                  g.setColor(invert);
                  g.fillOval(bumper2, bumper2, getWidth() - bumper2 - bumper2, getHeight() - bumper2 - bumper2);
                }
                break;
              case SHAPE_SQUARE:
                g.fillRoundRect((int) (hW - rad), (int) (hH - rad), diam, diam, bumper2, bumper2);
                if (selected)
                {
                  g.setColor(invert);
                  g.fillRoundRect((int) (hW - rad) + bumper, (int) (hH - rad) + bumper, diam - bumper2, diam - bumper2, bumper,
                                  bumper);
                }
                break;
              case SHAPE_RECT:
                g.fillRoundRect(bumper, bumper, W - (bumper2), H - (bumper2), bumper2, bumper2);
                if (selected)
                {
                  g.setColor(invert);
                  g.fillRoundRect(bumper2, bumper2, W - bumper2 - bumper2, H - bumper2 - bumper2, bumper, bumper);
                }
                break;
            }
          }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Draw the Border">
        g.setColor(dragOver ? Color.BLUE : getForeground());

        if (border == null)
        {
          if (!topIsOpen())
          {
            g.fillRect(0, 0, W, borderThickness);
          }
          if (!leftIsOpen())
          {
            g.fillRect(0, 0, borderThickness, H);
          }
          if (!rightIsOpen())
          {
            g.fillRect(W - borderThickness, 0, W - borderThickness, H);
          }
          if (!bottomIsOpen())
          {
            g.fillRect(0, H - borderThickness, W, H - borderThickness);
          }
        }
        else
          border.paintBorder(getThis(), g, 0, 0, W, H);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Draw the Text">
        if (t != null)
        {
          g.setFont(getFont());
          int x = (int) ((W / 2.0) - ((t.length() * getFont().getSize()) / 2.0)), y = (int) ((H / 2.0) + (getFont().getSize()
                                                                                                          / 2.0));
          g.setColor(getBackground());

          String s = t.toString();
          //<editor-fold defaultstate="collapsed" desc="Border around text">
          g.drawString(s, x, y + 1);
          g.drawString(s, x, y - 1);
          g.drawString(s, x + 1, y);
          g.drawString(s, x - 1, y);
          g.drawString(s, x + 1, y + 1);
          g.drawString(s, x - 1, y - 1);
          g.drawString(s, x - 1, y + 1);
          g.drawString(s, x + 1, y - 1);
          g.drawString(s, x + 1, y - 1);
          g.drawString(s, x - 1, y + 1);
          //</editor-fold>

          g.setColor(getForeground());
          g.drawString(s, x, y);
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Draw the Selection Rectangle">
        if (hasFocus())
        {
          g.setColor(getForeground());
          int bumper3 = Math.max(bumper, 3), bumper4 = Math.max(bumper2 + 1, (bumper3 * 2) + 1);//These ensure there's at LEAST a one-pixel gap between the selection rectangle and the border
          g.drawRoundRect(bumper3, bumper3, W - bumper4, H - bumper4, bumper, bumper);
        }
        //</editor-fold>
      /*}
    }, "BHBoardSquare#paint");
    paintThread.start();*/
  }
  
  public void setBorder(Border newBorder)
  {
    border = newBorder;
  }

  @Override
  public void setForeground(Color c)
  {
    super.setForeground(c);
    
    if (border == null || border instanceof LineBorder)
      border = new LineBorder(getForeground(), 2);
  }

  public boolean topIsOpen()
  {
    return allSidesAreOpen(STATE_TOP_OPEN);
  }

  private boolean rightIsOpen()
  {
    return allSidesAreOpen(STATE_RIGHT_OPEN);
  }

  private boolean leftIsOpen()
  {
    return allSidesAreOpen(STATE_LEFT_OPEN);
  }

  private boolean bottomIsOpen()
  {
    return allSidesAreOpen(STATE_BOTTOM_OPEN);
  }

  /**
   * Adds the given {@link ActionListener} to the array of actions to be performed
   * @param l the {@code ActinoListener} to be added
   * @see ArrayPP#add(Object... l)
   */
  public synchronized void addActionListener(ActionListener l)
  {
    als.add(l);
  }

  /**
   * Removes ALL instances of the given {@link ActionListener} from the array of actions to be performed
   * @param l the {@code ActinoListener} to be removed
   * @see ArrayPP#remove(Object l, boolean true)
   */
  public synchronized void removeActionListener(ActionListener l)
  {
    als.remove(l, true);
  }

  public CharSequence getText()
  {
    return t;
  }

  /**
   * Sets the text of this square
   * @param text the new text of this square
   */
  public void setText(CharSequence text)
  {
    t = text;
    repaint();
  }
  
  /**
   * Opens the given side(s) of this square and then repaints.
   * @param sideMask the side(s) to open
   * @return {@code this}
   * @see #STATE_TOP_OPEN
   * @see #STATE_LEFT_OPEN
   * @see #STATE_RIGHT_OPEN
   * @see #STATE_BOTTOM_OPEN
   * @deprecated since this object is designed to work within and be managed by a {@link BHBoardPanel} and therefore should not
   * be called directly, you should use {@link BHBoardPanel#openSquare(int, int, byte)}, instead
   */
  public BHBoardSquare openSides(byte sideMask)
  {
    st |= sideMask;
    repaint();
    return this;
  }
  
  
  /**
   * Closes the given side(s) of this square and then repaints.
   * @param sideMask the side(s) to open
   * @return {@code this}
   * @see #STATE_TOP_OPEN
   * @see #STATE_LEFT_OPEN
   * @see #STATE_RIGHT_OPEN
   * @see #STATE_BOTTOM_OPEN
   * @deprecated since this object is designed to work within and be managed by a {@link BHBoardPanel}, you should use
   * {@link BHBoardPanel#closeSquare(int, int, byte)}, instead
   */
  public BHBoardSquare closeSides(byte sideMask)
  {
    st &= ~sideMask;
    repaint();
    return this;
  }

  /**
   * Return {@code true} if and only if any one side represented by the given {@code byte} mask are open
   * @param sideMask the mask of which sides should be tested. Can be a combination (using {@code +} or {@code |}) of {@link #STATE_BOTTOM_OPEN}, {@link #STATE_LEFT_OPEN}, {@link #STATE_RIGHT_OPEN}, or {@link #STATE_TOP_OPEN}
   * @return {@code true} if and only if any one side represented by the given {@code byte} mask are open
   */
  public boolean anySideIsOpen(byte sideMask)
  {
    return (sideMask & st) != 0;
  }

  /**
   * Return {@code true} if and only if all sides represented by the given {@code byte} mask are open
   * @param sideMask the mask of which sides should be tested. Can be a combination (using {@code +} or {@code |}) of {@link #STATE_BOTTOM_OPEN}, {@link #STATE_LEFT_OPEN}, {@link #STATE_RIGHT_OPEN}, or {@link #STATE_TOP_OPEN}
   * @return {@code true} if and only if all sides represented by the given {@code byte} mask are open
   */
  public boolean allSidesAreOpen(byte sideMask)
  {
    return (sideMask & st) == sideMask;
  }
  
  public BHBoardSquare setColor(Color newColor)
  {
    color = newColor;
    return this;
  }
  
  public Color getColor()
  {
    return color;
  }
  
  public BHBoardSquare setShape(byte shapeMask)
  {
    sh = shapeMask;
    return this;
  }
  
  public byte getShape()
  {
    return sh;
  }
  
  public short getBorderThickness()
  {
    return borderThickness;
  }
  
  public BHBoardSquare setBorderThickness(short newThickness)
  {
    borderThickness = newThickness;
    return this;
  }
  
  public boolean isSelected()
  {
    return selected;
  }
  
  public BHBoardSquare setSelected(boolean select)
  {
    setState(select);
    
    return this;
  }

  @Override
  public Dimension getPreferredSize()
  {
    return new Dimension(32, 32);
  }
  
  public BHBoardSquare addStateChangeListener(StateChangeListener scl)
  {
    cls.add(scl);
    return this;
  }
  
  public BHBoardSquare removeStateListener(StateChangeListener scl)
  {
    cls.remove(scl, true);
    return this;
  }
  
  public BHBoardSquare setState(boolean isSelected)
  {
    StateChangeEvent evt = new StateChangeEvent(this, selected, selected = isSelected);
    for (StateChangeListener scl : cls)
      scl.stateChanged(evt);
    return this;
  }

  //<editor-fold defaultstate="collapsed" desc="Drag and drop">
  @Override
  public void dragGestureRecognized(DragGestureEvent dge)
  {
    dge.startDrag(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR), this, this);
  }
  
  @Override
  public void dragEnter(DropTargetDragEvent dtde)
  {
    dragOver = true;
    repaint();
  }
  
  @Override
  public void dragOver(DropTargetDragEvent dtde)
  {
    dragOver = true;
    repaint();
  }
  
  @Override
  public void dropActionChanged(DropTargetDragEvent dtde)
  {
    System.out.println("Drop action changed to " + dtde.getDropAction());
  }
  
  @Override
  public void dragExit(DropTargetEvent dte)
  {
    dragOver = false;
    repaint();
  }
  
  @Override
  public void drop(DropTargetDropEvent dtde)
  {
    dragOver = false;
    repaint();
  }
  
  private DataFlavor df = new DataFlavor(getClass(), "Board Square");
  
  @Override
  public DataFlavor[] getTransferDataFlavors()
  {
    try
    {
      return new DataFlavor[]{df};
    }
    catch (Throwable t)
    {
      t.printStackTrace();
      return null;
    }
  }
  
  @Override
  public boolean isDataFlavorSupported(DataFlavor flavor)
  {
    return flavor.getMimeType().equals(df.getMimeType());
  }
  
  @Override
  public BHBoardSquare getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
  {
    if (!isDataFlavorSupported(flavor))
      throw new UnsupportedFlavorException(flavor);
    return this;
  }
  
  @Override
  public void dragEnter(DragSourceDragEvent dsde)
  {
    dragOver = true;
    repaint();
  }
  
  @Override
  public void dragOver(DragSourceDragEvent dsde)
  {
    dragOver = true;
    repaint();
  }
  
  @Override
  public void dropActionChanged(DragSourceDragEvent dsde)
  {
    System.out.println("Drop action changed to " + dsde.getDropAction());
  }
  
  @Override
  public void dragExit(DragSourceEvent dse)
  {
    dragOver = false;
    repaint();
  }
  
  @Override
  public void dragDropEnd(DragSourceDropEvent dsde)
  {
    dragOver = false;
    repaint();
  }
  //</editor-fold>
  
  public void setOpaque(boolean opaque)
  {
    opacity = opaque;
  }

  @Override
  public boolean isOpaque()
  {
    return opacity;
  }
}
