package bht.tools.comps;

import bht.tools.util.math.Numbers;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * 
 * @author Supuhstar of Blue Husky Programming
 * @since Dec 28, 2011
 * @version 1.0.0
 */
public class BHImageComp extends JPanel
{
  //<editor-fold defaultstate="collapsed" desc="Behavior Flags">
  /**
   * Defines that this image should automatically resize itself when the component is resized. Equal to {@value}.<hr/>
   * Behaviors can be combined with the {@code |} (OR) operator
   */
  public static final byte BEHAVIOR_AUTO_RESIZE = 0b0001;
  /**
   * Defines that this image should smooth itself out (use anti-aliasing) after resizing. Equal to {@value}.<hr/>
   * Behaviors can be combined with the {@code |} (OR) operator
   */
  public static final byte BEHAVIOR_SMOOTH_RESIZE = 0b0010;
  /**
   * Defines that this image should fit within its container upon resizing (if the container is taller or wider than the image,
   * then the image will stay the same scale so that the complete image is still visible). Equal to {@value}.<hr/>
   * Behaviors can be combined with the {@code |} (OR) operator
   */
  public static final byte BEHAVIOR_SCALE_FIT = Numbers.DIM_BEHAVIOR_SCALE_FIT;
  /**
   * Defines that this image should fit within its container upon resizing (if the container is taller or wider than the image,
   * then the image will be scaled and trimmed so that the entire container is filled). Equal to {@value}.<hr/>
   * Behaviors can be combined with the {@code |} (OR) operator
   */
  public static final byte BEHAVIOR_SCALE_FILL = Numbers.DIM_BEHAVIOR_SCALE_FILL;
  /**
   * Default image behavior. Equal to {@value}; the following combination:
   * {@link #BEHAVIOR_AUTO_RESIZE}{@code |}{@link #BEHAVIOR_SMOOTH_RESIZE}{@code |}{@link #BEHAVIOR_SCALE_FIT}
   * ({@value #BEHAVIOR_AUTO_RESIZE}{@code |}{@value #BEHAVIOR_SMOOTH_RESIZE}{@code |}{@value #BEHAVIOR_SCALE_FIT})
   * @see #BEHAVIOR_AUTO_RESIZE
   * @see #BEHAVIOR_SMOOTH_RESIZE
   * @see #BEHAVIOR_SCALE_FIT
   */
  public static final byte BEHAVIOR_DEFAULT = BEHAVIOR_AUTO_RESIZE | BEHAVIOR_SMOOTH_RESIZE | BEHAVIOR_SCALE_FIT;
  //</editor-fold>
  
  private byte behavior;
  private double ratio;
  private Image nakedImage;
  private JLabel wrapper;
  private Thread resizeThread;
  
  public BHImageComp(Image image, final byte BEHAVIOR)
  {
    this(convertImageToIcon(image), BEHAVIOR);
  }
  
  public BHImageComp(final Icon image, final byte BEHAVIOR)
  {
//    System.out.println("BHImageComp: Selected Behavior: " + BEHAVIOR);
    behavior = BEHAVIOR;
    setLayout(new BorderLayout());
    
    nakedImage = convertIconToImage(image);
    ratio = image == null ? 1 : (double)image.getIconWidth() / (double)image.getIconHeight();
    
    wrapper = new JLabel(image);
    wrapper.setHorizontalAlignment(SwingConstants.CENTER);
    wrapper.setVerticalAlignment(SwingConstants.CENTER);
    add(wrapper, BorderLayout.CENTER);
    addComponentListener(new ComponentAdapter()
    {
      @Override
      public void componentResized(ComponentEvent e)
      {
        if (resizeThread != null)
          resizeThread.stop();
        (resizeThread = new Thread(new Runnable()
        {
          @Override
          public void run()
          {
            if (nakedImage == null)
              return;
            try
            {
//              System.out.println("BHImageComp#componentResized(): Selected Behavior: " + b);
              if ((behavior & BEHAVIOR_AUTO_RESIZE) != 0)
              {
                Dimension d = Numbers.getScaledDim(new Dimension(nakedImage.getWidth(wrapper), nakedImage.getHeight(wrapper)),
                                           wrapper.getSize(), behavior);
                wrapper.setIcon(new ImageIcon(nakedImage.getScaledInstance(d.width, d.height, Image.SCALE_FAST)));
                if ((behavior & BEHAVIOR_SMOOTH_RESIZE) != 0)
                  wrapper.setIcon(new ImageIcon(nakedImage.getScaledInstance(d.width, d.height, Image.SCALE_SMOOTH)));
              }
            }
            catch (ThreadDeath td){}//This is expected when dealing with the smooth-resizing of the object
          }
        }, "resizeThread")).start();
      }
    });
  }
  
  public static Image convertIconToImage(Icon icon)
  {
    if (icon == null)
      return null;
    if (icon instanceof ImageIcon)
    {
      return ((ImageIcon) icon).getImage();
    }
    else
    {
      BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
      icon.paintIcon(null, image.getGraphics(), 0, 0);
      return image;
    }
  }
  
  public static Icon convertImageToIcon(Image image)
  {
    return new ImageIcon(image);
  }
  
  public Image getImageAsImage()
  {
    return nakedImage;
  }
  
  public Icon getImageAsIcon()
  {
    return convertImageToIcon(nakedImage);
  }
  
  public BHImageComp setImage(Image newImage)
  {
    nakedImage = newImage;
    return this;
  }
  
  public BHImageComp setImage(Icon image)
  {
    return setImage(convertIconToImage(image));
  }

  public void setBehavior(byte behavior)
  {
    this.behavior = behavior;
  }

  public byte getBehavior()
  {
    return behavior;
  }
}
