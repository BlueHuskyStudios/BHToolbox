package bht.tools.comps;

import bht.tools.fx.CompAction;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;

/**
 *
 * @author Supuhstar
 */
public class BHDWM implements javax.swing.DesktopManager
{
  JInternalFrame activeFrame;
  @Override public void openFrame(JInternalFrame f)
  {
    (f instanceof BHInternalFrame ? ((BHInternalFrame)f).getCompAction() : new CompAction()).ghostIn(f);
  }

  @Override public void closeFrame(JInternalFrame f)
  {
    (f instanceof BHInternalFrame ? ((BHInternalFrame)f).getCompAction() : new CompAction()).ghostOut(f);
  }

  @Override public void maximizeFrame(final JInternalFrame f)
  {
    if (f.isMaximum())
      return;
    
    final java.awt.Rectangle r = f.getNormalBounds();
    (f instanceof BHInternalFrame ? ((BHInternalFrame)f).getCompAction() : new CompAction())
            .morphTo(f, new java.awt.Rectangle(0, 0, f.getParent().getWidth(), f.getParent().getHeight()), 0,
            CompAction.DEF_BRAKE, CompAction.DEF_FPS, true, false, null,
            new java.awt.event.ActionListener()
            {
              @Override public void actionPerformed(ActionEvent e)
              {
                try
                {
                  f.setMaximum(true);
                }
                catch (PropertyVetoException ex)
                {
                  Logger.getLogger(BHDWM.class.getName()).log(Level.SEVERE, null, ex);
                }
                f.setNormalBounds(r);
              }
            },
            false,0);
  }

  @Override public void minimizeFrame(JInternalFrame f)
  {
    if (!f.isMaximum())
      return;
    (f instanceof BHInternalFrame ? ((BHInternalFrame)f).getCompAction() : new CompAction()).morphTo(f, f.getNormalBounds());
  }

  @Override public void iconifyFrame(final JInternalFrame f)
  {
    if (f.isIcon())
      return;
    
    final Rectangle r = f.getNormalBounds();
    
    (f instanceof BHInternalFrame ? ((BHInternalFrame)f).getCompAction() : new CompAction()).
          morphTo(f, new java.awt.Rectangle(
              (f.getParent().getWidth() / 2) - (f.getWidth() / 2),
              (f.getParent().getHeight()) + (f.getHeight()),
              f.getWidth() / 2,
              f.getHeight() / 2),
          1,
          CompAction.DEF_BRAKE,
          CompAction.DEF_FPS,
          false,
          true,
          null,
          new java.awt.event.ActionListener()
          {
            @Override
            public void actionPerformed(ActionEvent e)
            {
              try
              {
                f.setIcon(true);
              }
              catch (PropertyVetoException ex)
              {
                ex.printStackTrace();
              }
              f.setNormalBounds(r);
            }
          },
          false, 0);
//    (f instanceof BHInternalFrame ? ((BHInternalFrame)f).getCompAction() : new CompAction()).fadeAway(f); Replaced by a morphto method that accepts target transparency
  }

  @Override public void deiconifyFrame(final JInternalFrame f)
  {
    if (!f.isIcon())
      return;
    (f instanceof BHInternalFrame ? ((BHInternalFrame)f).getCompAction() : new CompAction()).
          morphTo(f,
          f.getNormalBounds(),
          0,
          CompAction.DEF_BRAKE,
          CompAction.DEF_FPS,
          true,
          false,
          null,
          new java.awt.event.ActionListener()
            {
              @Override
              public void actionPerformed(ActionEvent e)
              {
                try
                {
                  f.setIcon(true);
                }
                catch (PropertyVetoException ex)
                {
                  ex.printStackTrace();
                }
              }
            },
          false, 0);
//    (f instanceof BHInternalFrame ? ((BHInternalFrame)f).getCompAction() : new CompAction()).fadeIn(f);
  }

  @Override public void activateFrame(JInternalFrame f)
  {
    activeFrame = f;
  }

  @Override public void deactivateFrame(JInternalFrame f)
  {
    if (activeFrame == f)
      activeFrame = null;
  }

  @Override public void beginDraggingFrame(JComponent f)
  {
    
  }

  @Override public void dragFrame(JComponent f, int newX, int newY)
  {
    f.setLocation(newX, newY);
  }

  @Override public void endDraggingFrame(JComponent f)
  {
    
  }

  @Override public void beginResizingFrame(JComponent f, int direction)
  {
    
  }

  @Override public void resizeFrame(JComponent f, int newX, int newY, int newWidth, int newHeight)
  {
    f.setBounds(newX, newY, newWidth, newHeight);
  }

  @Override public void endResizingFrame(JComponent f)
  {
    
  }

  @Override public void setBoundsForFrame(JComponent f, int newX, int newY, int newWidth, int newHeight)
  {
    f.setBounds(newX, newY, newWidth, newHeight);
  }
  
}
