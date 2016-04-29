package bht.tools.fx;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import javax.swing.JComponent;
import javax.swing.border.TitledBorder;

/**
 * RecursiveStyling, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012. License is CC-BY-SA.<hr>
 * A set of convenience methods for recursively styling GUI components
 * @author Kyli of Blue Husky Studios
 * @version 1.0.0
 * @since Aug 12, 2012
 */
public class RecursiveStyling
{
  /**
   * Sets the font of all the components contained within the given one to be the given font.
   * @param comp the comp containing more components, all of which will have their fonts changed
   * @param f the font to apply to the given component and all its children
   */
  public static void setFontsIn(Component comp, Font f)
  {
    comp.setFont(f);
    if (comp instanceof Container)
    {
      for (Component c : ((Container)comp).getComponents())
      {
        setFontsIn(c, f);
      }
    }
  }
  
  public static void setFontSizeIn(Component comp, float size)
  {
    comp.setFont(comp.getFont().deriveFont(size));
    
    if (comp instanceof Container)
    {
      for (Component c : ((Container)comp).getComponents())
      {
        setFontSizeIn(c, size);
      }
    }
  }
  
  public static void setFontStyleIn(Component comp, int style)
  {
    comp.setFont(comp.getFont().deriveFont(style));
    
    if (comp instanceof Container)
    {
      for (Component c : ((Container)comp).getComponents())
      {
        setFontStyleIn(c, style);
      }
    }
  }
  
  public static void setFontFamilyIn(Component comp, String familyName)
  {
    Font f1 = comp.getFont(), f2;
    if (f1 == null)
      f1 = new Font(familyName, Font.PLAIN, 11);
    else
      f1 = new Font(familyName, f1.getStyle(), f1.getSize());
    
    comp.setFont(f1);
    if (comp instanceof JComponent && ((JComponent)comp).getBorder() instanceof TitledBorder)
      ((TitledBorder) ((JComponent) comp).getBorder()).setTitleFont(
              f1.deriveFont((float) (f2 = ((TitledBorder) ((JComponent) comp).getBorder()).getTitleFont()).getSize())
               .deriveFont((int)   f2.getStyle()));

    if (comp instanceof Container)
    {
      for (Component c : ((Container)comp).getComponents())
      {
        setFontFamilyIn(c, familyName);
      }
    }
  }
}
