package bht.resources;

import bht.tools.misc.YesNoBox;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.DefaultButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.UIManager;

/**
 * Manages the icons stored within this package. All are static for greatest usability.
 * @author Supuhstar
 * @since 1.6.0_23
 * @version 1.1.2
 */
public class Icons
{
//  public static ImageIcon
//          warningIcon32 = getIcon("dialog-warning 32.png"),
//          errorIcon32 = getIcon("dialog-error 32.png"),
//          infoIcon32 = getIcon("dialog-information 32.png"),
//          questionIcon32 = getIcon("dialog-question 32.png"),
//          warningIcon16 = getIcon("dialog-warning 16.png"),
//          errorIcon16 = getIcon("dialog-error 16.png"),
//          infoIcon16 = getIcon("dialog-information 16.png"),
//          questionIcon16 = getIcon("dialog-question 16.png"),
//          blank =  getIcon("blank.png");
  public static final byte BLANK = YesNoBox.PLAIN_MES;
  public static final byte APP_EXIT = 0x50;
  public static final byte SPECIAL_16 = 0x51;
  public static final byte SETTINGS_16 = 0x52;
  public static final byte ADD_24 = 0x53;
  public static final byte REMOVE_24 = 0x54;
  public static final byte NO_24 = 0x55;
  public static final byte CLOSE_12 = 0x56;
  public static final byte FIND_16 = 0x57;
  public static final byte INFO_32 = YesNoBox.INFO_MES;
  public static final byte INFO_16 = (byte)(INFO_32 + 0x10);
  public static final byte QUESTION_32 = YesNoBox.QUESTION_MES;
  public static final byte QUESTION_16 = (byte)(QUESTION_32 + 0x10);
  public static final byte WARNING_32 = YesNoBox.WARNING_MES;
  public static final byte WARNING_16 = (byte)(WARNING_32 + 0x10);
  public static final byte ERROR_32 = YesNoBox.ERROR_MES;
  public static final byte ERROR_16 = (byte)(ERROR_32 + 0x10);
  
  private static ImageIcon getIcon(String name)
  {
    return new javax.swing.ImageIcon(Icons.class.getResource("/bht/resources/" + name));
  }
  
  public static ImageIcon getImageIconFromIcon(Icon fromIcon)
  {
    return new ImageIcon(getImageFromIcon(fromIcon));
  }
  
  public static BufferedImage getImageFromIcon(Icon fromIcon)
  {
    if (fromIcon instanceof BufferedImage)
      return (BufferedImage)fromIcon;
    fromIcon = new SafeIcon(fromIcon);
    BufferedImage image = new BufferedImage(fromIcon.getIconWidth(), fromIcon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
    fromIcon.paintIcon(new javax.swing.JPanel(), image.getGraphics(), 0, 0);
    return image;
  }
  
  /**
   * Gets the icon represented by the given <tt>byte</tt> mask. You may use a <tt>byte</tt> mask from another class in the
   * toolbox; it will result in the 32x32-pixel version of the respective icon.
   * @param mask the <tt>byte</tt> representing the icon you want.
   * @return the icon you want, as an <tt>javax.swing.ImageIcon</tt>
   * @throws IllegalArgumentException if the mask you provide is not recognized
   */
  public static ImageIcon getIcon(byte mask)
  {
    switch(mask)
    {
      case BLANK:
        return getIcon("blank.png");
      case APP_EXIT:
        return getIcon("application-exit 16.png");
      case SPECIAL_16:
        return getIcon("native 16.png");
      case SETTINGS_16:
        return getIcon("pref 16.png");
      case ADD_24:
        return getIcon("list-add 24.png");
      case REMOVE_24:
        return getIcon("list-remove 24.png");
      case NO_24:
        return getIcon("edit-delete 24.png");
      case CLOSE_12://Added March 4, 2012 for BHNotifier
        ImageIcon ret/* = null*/;
        /*try
        {
          ret = new javax.swing.ImageIcon(new URL("http://s.supuhstar.operaunite.com/s/content/icons/Nimbus/12x12/actions/list-remove.png"));
        }
        catch (MalformedURLException ex)
        {
        }
        if (ret == null)*/
          ret = new ImageIcon(getIcon("list-remove 24.png").getImage().getScaledInstance(12, 12, Image.SCALE_SMOOTH));
        return ret;
      case FIND_16:
        return getIcon("edit-find 16.png");

      case INFO_16:
        return getIcon("dialog-information 16.png");
//        return makeImageIcon("OptionPane.informationIcon", 16);
      case INFO_32:
        return getIcon("dialog-information 32.png");
//        return makeImageIcon("OptionPane.informationIcon", 32);
      case QUESTION_16:
        return getIcon("dialog-question 16.png");
//        return makeImageIcon("OptionPane.questionIcon", 16);
      case QUESTION_32:
        return getIcon("dialog-question 32.png");
//        return makeImageIcon("OptionPane.questionIcon", 32);
      case WARNING_16:
        return getIcon("dialog-warning 16.png");
//        return makeImageIcon("OptionPane.warningIcon", 16);
      case WARNING_32:
        return getIcon("dialog-warning 32.png");
//        return makeImageIcon("OptionPane.warningIcon", 32);
      case ERROR_16:
        return getIcon("dialog-error 16.png");
//        return makeImageIcon("OptionPane.errorIcon", 16);
      case ERROR_32:
        return getIcon("dialog-error 32.png");
//        return makeImageIcon("OptionPane.errorIcon", 32);
      default:
        throw new IllegalArgumentException("Icon mask not recognized: " + mask);
    }
  }
  
  /**
   * Convenience method that returns a scaled instance of the provided {@link BufferedImage}.
   *
   * @param imageToScale the original image to be scaled
   * @param targetWidth the desired width of the scaled instance, in pixels
   * @param targetHeight the desired height of the scaled instance, in pixels
   * @param hint one of the rendering hints that corresponds to
   *    {@link RenderingHints#KEY_INTERPOLATION} (e.g.
   *    {@link RenderingHints#VALUE_INTERPOLATION_NEAREST_NEIGHBOR},
   *    {@link RenderingHints#VALUE_INTERPOLATION_BILINEAR},
   *    {@link RenderingHints#VALUE_INTERPOLATION_BICUBIC})
   * @param higherQuality if true, this method will use a multi-step scaling technique that provides higher quality than the
   * usual one-step technique (only useful in downscaling cases, where
   *    {@code targetWidth} or {@code targetHeight} is smaller than the original dimensions, and generally only when the {@code BILINEAR}
   * hint is specified)
   * @return a scaled version of the original {@link BufferedImage}
   */
  public static BufferedImage getScaledInstance(BufferedImage imageToScale, int targetWidth, int targetHeight, Object hint,
                                                boolean higherQuality)
  {
    int type = //(imageToScale.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB :
               BufferedImage.TYPE_INT_ARGB;
    BufferedImage ret = (BufferedImage) imageToScale;
    int w, h;
    if (higherQuality)
    {
      // Use multi-step technique: start with original size, then
      // scale down in multiple passes with drawImage()
      // until the target size is reached
      w = imageToScale.getWidth();
      h = imageToScale.getHeight();
    }
    
    else
    {
      // Use one-step technique: scale directly from original
      // size to target size with a single drawImage() call
      w = targetWidth;
      h = targetHeight;
    }

    do
    {
      if (higherQuality && w > targetWidth)
      {
        w /= 2;
        if (w < targetWidth)
        {
          w = targetWidth;
        }
      }

      if (higherQuality && h > targetHeight)
      {
        h /= 2;
        if (h < targetHeight)
        {
          h = targetHeight;
        }
      }

      BufferedImage tmp = new BufferedImage(w, h, type);
      Graphics2D g2 = tmp.createGraphics();
      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
      g2.drawImage(ret, 0, 0, w, h, null);
      g2.dispose();

      ret = tmp;
    } while (w != targetWidth || h != targetHeight);

    return ret;
  }

  private static ImageIcon makeImageIcon(String uiManagerKey, int widthHeight)
  {
    return new ImageIcon(getScaledInstance(getImageFromIcon((Icon) UIManager.get(uiManagerKey)), widthHeight, widthHeight,
                                           RenderingHints.VALUE_INTERPOLATION_BICUBIC, true));
  }
  
  /**
 * Some ui-icons misbehave in that they unconditionally class-cast to the component type they are mostly painted on.
   * Consequently they blow up if we are trying to paint them anywhere else (f.i. in a renderer).
   *
   * This Icon is an adaption of a cool trick by Darryl Burke/Rob Camick found at
   * http://tips4java.wordpress.com/2008/12/18/icon-table-cell-renderer/#comment-120
   *
   * The base idea is to instantiate a component of the type expected by the icon, let it paint into the graphics of a
   * bufferedImage and create an ImageIcon from it. In subsequent calls the ImageIcon is used.
   *
   */
  private static class SafeIcon implements Icon
  {
    private Icon wrappee;
    private Icon standIn;
    
    public SafeIcon(Icon wrappee)
    {
      this.wrappee = wrappee;
    }
    
    @Override
    public int getIconHeight()
    {
      return wrappee.getIconHeight();
    }
    
    @Override
    public int getIconWidth()
    {
      return wrappee.getIconWidth();
    }
    
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y)
    {
      if (standIn == this)
      {
        paintFallback(c, g, x, y);
      }
      else if (standIn != null)
      {
        standIn.paintIcon(c, g, x, y);
      }
      else
      {
        try
        {
          wrappee.paintIcon(c, g, x, y);          
        }
        catch (ClassCastException e)
        {
          createStandIn(e, x, y);
          standIn.paintIcon(c, g, x, y);
        }
      }
    }

    /**
     * @param e
     */
    private void createStandIn(ClassCastException e, int x, int y)
    {
      try
      {
        Class<?> clazz = getClass(e);
        JComponent standInComponent = getSubstitute(clazz);
        standIn = createImageIcon(standInComponent, x, y);
      }
      catch (Exception e1)
      {
        // something went wrong - fallback to this painting
        standIn = this;
      }      
    }
    
    private Icon createImageIcon(JComponent standInComponent, int x, int y)
    {
      BufferedImage image = new BufferedImage(getIconWidth(),
                                              getIconHeight(), BufferedImage.TYPE_INT_ARGB);
      Graphics g = image.createGraphics();
      try
      {
        wrappee.paintIcon(standInComponent, g, 0, 0);
        return new ImageIcon(image);
      }
      finally
      {
        g.dispose();
      }
    }

    /**
     * @param clazz
     * @throws IllegalAccessException
     */
    private JComponent getSubstitute(Class<?> clazz) throws IllegalAccessException
    {
      JComponent standInComponent;
      try
      {
        standInComponent = (JComponent) clazz.newInstance();
      }
      catch (InstantiationException e)
      {
        standInComponent = new AbstractButton()
        {
          
        };
        ((AbstractButton) standInComponent).setModel(new DefaultButtonModel());
      }      
      return standInComponent;
    }
    
    private Class<?> getClass(ClassCastException e) throws ClassNotFoundException
    {
      String className = e.getMessage();
      className = className.substring(className.lastIndexOf(" ") + 1);
      return Class.forName(className);
      
    }
    
    private void paintFallback(Component c, Graphics g, int x, int y)
    {
      g.drawRect(x, y, getIconWidth(), getIconHeight());
      g.drawLine(x, y, x + getIconWidth(), y + getIconHeight());
      g.drawLine(x + getIconWidth(), y, x, y + getIconHeight());
    }
  }
  
}
