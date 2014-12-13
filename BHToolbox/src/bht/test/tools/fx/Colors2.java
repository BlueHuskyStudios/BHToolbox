package bht.test.tools.fx;

import bht.tools.fx.Colors;
import java.awt.Color;
import java.awt.Component;

/**
 * Colors2, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012. License is default.<hr/>
 * A complete remake of the outdated and, admittedly, messy {@link Colors}.
 * @author Supuhstar of Blue Husky Programming
 * @since Feb 22, 2012 (Java version 1.7_02)
 * @version 2.0.0
 */
public class Colors2 //NOTE: Must be compiled in UTF-8
{
  public static final byte PART_BORDER    = 0b001;
  public static final byte PART_BOX       = 0b010;
  public static final byte PART_BACK      = 0b011;
  public static final byte PART_TEXT      = 0b100;
  public static final byte PART_TEXT_AREA = 0b101;
  
  public static Color getColor(ColorSet style, byte part)
  {
    switch (part)
    {
      case PART_BACK:
        return style.getBackgroundColor();
      case PART_BORDER:
        return style.getBorderColor();
      case PART_BOX:
        return style.getBoxColor();
      case PART_TEXT:
        return style.getBoxColor();
      case PART_TEXT_AREA:
        return style.getBoxColor();
      default:
        throw new AssertionError(part);
    }
  }
  
  public static void fixGUIIn(Component compToFix)
  {
    
  }
  
  public interface ColorSet//After naming this off instinct, I remembered I'd thought it out 6 months earlier. I went to look at my notes to name it the same thing, and it turns out that I named it BHColorSet. That made me smile.
  {
    public abstract Color getBorderColor();
    public abstract Color getBoxColor();
    public abstract Color getBackgroundColor();
    public abstract Color getTextColor();
    public abstract Color getTextAreaColor();
  }
}