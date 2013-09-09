package bht.tools.fx;

//import bht.tools.utilities.Numbers;
import bht.tools.util.StringPP;
import java.awt.Color;

/**
 * A class made to help assist with color management and regulating official Blue Husky Colors
 * @author Supuhstar of Blue Husky Programming
 * @version 1.0.4
 * @since 1.6.0_16
 */
public class Colors
{
  protected static final long serialVersionUID = 0L;
  private byte style, part;
  private static  byte spacer = 2;
  public static final byte DEFAULT = -1;
  public static final byte MOCHA = 0;
  public static final byte HUSKY = 1;
  public static final byte ASTRO = 2;
  public final byte CUSTOM = 3;
  private static byte last = DEFAULT;
  public static final byte BORDER = 10;
  public static final byte BOX = 11;
  public static final byte BACK = 12;
  public static final byte TEXT = 13;
  public static final byte TEXT_AREA = 14;
  private static final Color stdColors[] =
                          {Color.BLACK, Color.BLUE, Color.CYAN, Color.DARK_GRAY, Color.GRAY, Color.GREEN,
                           Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.RED, Color.WHITE, Color.YELLOW};
  private static final String STD_HEX[] =
                          {"000000", "0000FF", "00FFFF", "404040", "808080", "00FF00",
                           "C0C0C0", "FF00FF", "FFC800", "FFAFAF", "FF0000", "FFFFFF", "FFFF00"};
  private Color custBox = getColor(MOCHA, BOX), custBack = getColor(MOCHA, BACK), custText = getColor(MOCHA, TEXT),
          custTextArea = getColor(MOCHA, TEXT_AREA), custBord = getColor(MOCHA, BORDER);
  /** The default font size for the target component in the {@code fixStyleIn()} methods */
  public static final double DEFAULT_SIZE = 11;//Try to get this to sync with system preferences
  /** The name of the default font used in the {@code fixStyleIn()} methods */
  public static final String DEFAULT_FONT = "Segoe UI";
  /** The name of the default font used in the {@code fixStyleIn()} methods 
   *  @since Feb 22, 2012 (1.0.4) for Marian */
  public static final String TITLE_FONT = "Trebuchet MS";
  /** A {@code String} that ends an override command */
  public static final String COMMAND_SEP = java.io.File.pathSeparator;
  /** A {@code String} override command that signifies that you want the size offset of the font in this component to be different */
  public static final String SIZE_OVERRIDE = "FONT_SIZE:";
  /** A {@code String} override command that signifies that you want the exact size of the font in this component to be different */
  public static final String SIZE_ONLY_OVERRIDE = "FONT_SIZE_MUST_BE:";
  /** A {@code String} override command that signifies that you want the icon in this component to be different
   * @deprecated Not yet implemented*/
  public static final String ICON_OVERRIDE = "ICON:";
  /** A {@code String} override command that signifies that you want the background color in this component to be different */
  public static final String BACK_OVERRIDE = "BOX_COLOR:";
  /** A {@code String} override command that signifies that you want the text (foreground) color in this component to be different */
  public static final String FORE_OVERRIDE = "TEXT_COLOR:";
  /** A {@code String} override command that signifies that you want the font style (bold, italic, etc.) in this component to
   * be different.<br/><b>Use the constants in {@code java.awt.Font}</b> */
  public static final String FONT_STYLE_OVERRIDE = "FONT_STYLE:";
  /** A {@code String} override command that signifies that you want the font in this component to be different.<br/>
   * <b>Use the name of the font only</b> */
  public static final String FONT_OVERRIDE = "FONT:";
  /** A {@code String} override command that signifies that you want the {@code style} of this component to be different
   * (use {@code Colors.MOCHA}, {@code Colors.HUSKY}, etc.) */
  public static final String COLOR_STYLE_OVERRIDE = "COLOR_STYLE:";
  /** A {@code String} override command that signifies that you want this component to be treated as if it is a different
   * {@code part} (use {@code Colors.BOX}, {@code Colors.BACK}, etc.) */
  public static final String COLOR_PART_OVERRIDE = "COLOR_PART:";

  /**
   * Creates a new {@code Colors} object given a certain style and part. See documentation for the {@code getColor(byte style,
   * byte part)} method for further detail.
   * @param style the {@code byte} representing the visual style of the target color<br/>
   * <b>Example:</b><blockquote>{@code Colors.MOCHA}</blockquote>
   * @param part the {@code byte} representing the part for which the target color will be used<br/>
   * <b>Example:</b><blockquote>{@code Colors.BORDER}</blockquote>
   */
  public Colors(byte style, byte part)
  {
    this.style = style;
    this.part = part;
  }
  /**
   * Creates a new {@code Colors} object given a certain style and part. See documentation for the {@code getColor(byte style,
   * byte part)} method for further detail.
   * @param style the {@code byte} representing the visual style of the target color<br/>
   * <b>Example:</b><blockquote>{@code Colors.MOCHA}</blockquote>
   */
  public Colors(byte style)
  {
    this(style, Colors.BACK);
  }

  /**
   * Creates a new {@code Colors} object given a certain style and part. See documentation for the {@code getColor(byte style,
   * byte part)} method for further detail.
   * <b>Example:</b><blockquote>{@code Colors.BORDER}</blockquote>
   */
  public Colors()
  {
    this(Colors.MOCHA, Colors.BACK);
  }

  /**
   * Creates a new {@link Color} given a certain style and part. For example, if you wish to properly color a box (such
   * as a {@link javax.swing.JButton}) as it would be in the primary SupuhWiki theme, you would
   * set its border's color to be {@code Colors.getColor(}{@link Colors#MOCHA}{@code , }{@link Colors#BORDER}{@code )}, meaning
   * that you want it to be a border colored in the official way that SupuhWiki's borders are. For the filling, you would set
   * the background to {@code Colors.getColor(}{@link Colors#MOCHA}{@code , }{@link Colors#BOX}{@code )}. Note that this does
   * NOT use {@link Colors#BACK}. That is for the farthest back backgrounds. For the text, you would use
   * {@code Colors.getColor(}{@link Colors#MOCHA}{@code , }{@link Colors#TEXT}{@code )}.<br/>
   * <br/>
   * For custom colors, it will return the color set by you and/or the user in the {@link setCustomColor(Color, byte)}
   * @param part the {@code byte} representing the part for which the target color will be used.<br/>
   * <b>Example:</b><blockquote>{@link Colors#BORDER}</blockquote>
   * @return the official BH Color corresponding to the input style and part
   * @see Colors#BACK
   * @see Colors#BORDER
   * @see Colors#BOX
   * @see Colors#TEXT
   * @see Colors#TEXT_AREA
   */
  public Color getColor(byte part)
  {
    return getColor(style, part);
  }

  /**
   * Returns the {@link java.awt.Color} representation of these HSL values
   * @param hue the hue (from {@code 0} to {@code 360})
   * @param saturation the percentage of saturation (from {@code 0} to {@code 100})
   * @param lumination the percentage of lumination (from {@code 0} to {@code 100})
   * @return the {@link java.awt.Color} representation of these HSL values
   */
  public static Color getColor(short hue, byte saturation, byte lumination)
  {
    return getColor(hue, saturation, lumination, (byte)100);
  }

  /**
   * Returns the {@link java.awt.Color} representation of these HSL values
   * @param hue the hue (from {@code 0} to {@code 360})
   * @param saturation the percentage of saturation (from {@code 0} to {@code 100})
   * @param lumination the percentage of lumination (from {@code 0} to {@code 100})
   * @param alpha the percentage of alpha transparency (from {@code 0}(transparent) to {@code 100}(opaque))
   * @return the {@link java.awt.Color} representation of these HSL values
   */
  public static Color getColor(short hue, byte saturation, byte lumination, byte alpha)
  {
    return getColor((float)((double)hue / 360d), (float)((double)saturation / 100d), (float)((double)lumination / 100d));
  }

  /**
   * Returns the {@link java.awt.Color} representation of these HSL values
   * @param hue the percentage of hue (from {@code 0.0d} to {@code 1.0d})
   * @param saturation the percentage of saturation (from {@code 0.0d} to {@code 1.0d})
   * @param lumination the percentage of lumination (from {@code 0.0d} to {@code 1.0d})
   * @return the {@link java.awt.Color} representation of these HSL values
   */
  public static Color getColor(double hue, double saturation, double lumination)
  {
    return getColor(hue, saturation, lumination, 1);
  }

  /**
   * Returns the {@link java.awt.Color} representation of these HSL values
   * @param hue the percentage of hue (from {@code 0.0d} to {@code 1.0d})
   * @param saturation the percentage of saturation (from {@code 0.0d} to {@code 1.0d})
   * @param lumination the percentage of lumination (from {@code 0.0d} to {@code 1.0d})
   * @param alpha <B>!!CURRENTLY UNSUPPORTED!!</B> the percentage of alpha transparency (from {@code 0.0d}(transparent) to {@code 1.0d}(opaque))
   * @return the {@link java.awt.Color} representation of these HSL values
   */
  public static Color getColor(double hue, double saturation, double lumination, double alpha)
  {
//    System.out.println("(H:" + hue + ";S:" + saturation + ";L:" + lumination + ")");
    Color c = Color.getHSBColor((float)(Double.isNaN(hue) || Double.isInfinite(hue) ? 1 : (hue == 1.0 ? 1 : hue % 1)),
                                (float)(Double.isNaN(saturation) || Double.isInfinite(saturation) ? 1 : (saturation == 1.0 ? 1 : saturation % 1)),
                                (float)(Double.isNaN(lumination) || Double.isInfinite(lumination) ? 1 : (lumination == 1.0 ? 1 : lumination % 1)));
//    System.out.println(c);
    return c;
  }

  /**
   * Creates a new {@code Color} given a certain style and part. For example, if you wish to properly color a box (such
   * as a {@code javax.swing.JButton}) as it would be in the primary SupuhWiki theme, you would
   * set its border's color to be {@code Colors.getColor(Colors.MOCHA, Colors.BORDER)}, meaning that you want it to be a border
   * colored in the official way that SupuhWiki's borders are. For the filling, you would set the background to
   * {@code Colors.getColor(Colors.MOCHA, COLORS.BOX)}. Note that this does NOT use {@code Colors.BACK}. That is for the
   * farthest back backgrounds. For the text, you would use {@code Colors.getColor(Colors.MOCHA, Colors.TEXT)}.<br/>
   * <br/>
   * For custom colors, it will return the color set by you and/or the user in the {@code setCustomColor(Color color, byte part)}
   * @param style the {@code byte} representing the visual style for which the target color will be used<br/>
   * <b>Example:</b><blockquote>{@code Colors.MOCHA}</blockquote>
   * @param part the {@code byte} representing the part of the target color<br/>
   * <b>Example:</b><blockquote>{@code Colors.BORDER}</blockquote>
   * @return the official BH Color corresponding to the input style and part
   */
  public Color getColor(byte style, byte part)
  {
    Color c = null;
    switch (part)
    {
      case BORDER:
        switch (style)
        {
          case MOCHA:
            c = getColor("#78411A");
            break;
          case HUSKY:
            c = getColor("#D8FF2E");
            break;
          case ASTRO:
            c = new java.awt.Color(.5F, 0F, 0F);
            break;
          case CUSTOM:
            c = custBord;
            break;
          case DEFAULT:
            c = java.awt.SystemColor.controlDkShadow;
            break;
        }
        break;
      case TEXT:
        switch (style)
        {
          case MOCHA:
          case HUSKY:
          case ASTRO:
            c = getColor(style, Colors.BORDER);
            break;
          case DEFAULT:
            c = java.awt.SystemColor.textText;
            break;
          case CUSTOM:
            c = custText;
            break;
        }
        break;
      case TEXT_AREA:
        switch (style)
        {
          case MOCHA:
            c = new java.awt.Color(1F, 1F, 1F);
            break;
          case HUSKY:
            c = new java.awt.Color(0F, 0F, 0F);
            break;
          case ASTRO:
            c = new java.awt.Color(.1F, .1F, .1F);
            break;
          case CUSTOM:
            c = custTextArea;
            break;
          case DEFAULT:
            c = java.awt.SystemColor.text;
            break;
        }
        break;
      case BOX:
        switch (style)
        {
          case MOCHA:
            c = getColor("#BD9F68");
            break;
          case HUSKY:
            c = getColor("#0E1721");
            break;
          case ASTRO:
            c = new java.awt.Color(.05F, 0F, 0F);
            break;
          case CUSTOM:
            c = custBox;
            break;
          case DEFAULT:
//            c = Color.getColor(java.awt.SystemColor.control.toString());
            break;
        }
        break;
      case BACK:
        switch (style)
        {
          case MOCHA:
            c = getColor("#EDE4BF");
            break;
          case HUSKY:
            c = getColor("#2B2B2B");
            break;
          case ASTRO:
            c = new java.awt.Color(0);
            break;
          case CUSTOM:
            c = custBack;
            break;
          case DEFAULT:
            c = java.awt.SystemColor.window;
            break;
        }
        break;
    }
    return c;
  }

  /**
   * Gets the color you input when creating this object or wen you called {@code setStyle(byte style)}, {@code setPart(byte part)},
   * or {@code setColor(byte style, byte part)}.
   * @return the {@code java.awt.Color} representing the color you want to get.
   */
  public Color getColor()
  {
    return getColor(style, part);
  }

  public static boolean detectAlpha(CharSequence hexString)
  {
    if (hexString.length() < 8)
      return false;
    hexString = hexString.subSequence(hexString.length() - 8, hexString.length());
    if (!new StringPP(hexString).hasAnyIgnoreCase('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'))
      return false;
    return true;
  }

  /**
   * Uses a hex color {@code String} (such as {@code #BD9F68}, {@code 0x78411A}, or just {@code EDE4BF}) to create a {@code java.awt.Color}
   * @param hexString a {@code String} which represents the target color in hexadecimal, wherein the first two digits
   * (or first 16 bits) represent the amount of red in the color ({@code 00} is no red, {@code FF} is pure red),
   * the second two digits represent the amount of green, and the last two represent the amount of blue.
   * @return a {@code java.awt.Color} representation of the hex string.<br />
   * <b>Examples:</b><br />
   * <blockquote>
   * an input of {@code #FF0000} returns the equivalent of {@code java.awt.Color.RED}<br />
   * an input of {@code #00FF00} returns the equivalent of {@code java.awt.Color.GREEN}<br />
   * an input of {@code #0000FF} returns the equivalent of {@code java.awt.Color.BLUE}<br />
   * an input of {@code #000000} returns the equivalent of {@code java.awt.Color.BLACK}<br />
   * an input of {@code #FFFFFF} returns the equivalent of {@code java.awt.Color.WHITE}<br />
   * </blockquote>
   * @throws IllegalArgumentException if {@code hexString} is not a proper hexadecimal string
   */
  public static Color getColor(String hexString)
  {
    return getColor(hexString, detectAlpha(hexString));
  }

  /**
   * Uses a hex color {@code String} (such as {@code #BD9F68}, {@code 0x78411A}, or just {@code EDE4BF}) to create a {@code java.awt.Color}
   * @param hexString a {@code String} which represents the target color in hexadecimal, wherein the first two digits
   * (or first 16 bits) represent the amount of red in the color ({@code 00} is no red, {@code FF} is pure red),
   * the second two digits represent the amount of green, and the last two represent the amount of blue.
   * @param usesAlpha if true, this method will assume there is an alpha value
   * @return a {@code java.awt.Color} representation of the hex string.<br />
   * <b>Examples:</b><br />
   * <blockquote>
   * an input of {@code FF0000} returns the equivalent of {@code java.awt.Color.RED}<br />
   * an input of {@code 00FF00} returns the equivalent of {@code java.awt.Color.GREEN}<br />
   * an input of {@code 0000FF} returns the equivalent of {@code java.awt.Color.BLUE}<br />
   * an input of {@code 000000} returns the equivalent of {@code java.awt.Color.BLACK}<br />
   * an input of {@code FFFFFF} returns the equivalent of {@code java.awt.Color.WHITE}<br />
   * </blockquote>
   * @throws IllegalArgumentException if {@code hexString} is not a proper hexadecimal string
   */
  public static Color getColor(String hexString, boolean usesAlpha)
  {
    try
    {
      hexString = hexString.substring(hexString.length() - (usesAlpha ? 8 : 6)).toUpperCase();

      if (hexString.length() < (usesAlpha ? 8 : 6) || !new StringPP(hexString).hasAnyIgnoreCase('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'))
        throw new IllegalArgumentException("Illegal hex string: " + hexString);
      for (int i = 0; i < stdColors.length; i++)//Checks for basic colors
        if (hexString.equals(STD_HEX[i]))
          return stdColors[i];

      String hexRed = hexString.substring(0, 2),
              hexGreen = hexString.substring(2, 4),
              hexBlue = hexString.substring(4, 6),
              hexAlpha = usesAlpha ? hexString.substring(6) : "FF";
      return new java.awt.Color(Integer.parseInt(hexRed, 16), Integer.parseInt(hexGreen, 16), Integer.parseInt(hexBlue, 16), Integer.parseInt(hexAlpha, 16));
    }
    catch (StringIndexOutOfBoundsException ex)
    {
      throw new IllegalArgumentException("Illegal Hex String: " + hexString, ex);
    }
//    catch (Throwable t)
//    {
//      throw t;
//    }
  }

  /**
   * Calculates and returns a {@code String} representation of the specified Official BHColor in hexadecimal format
   * @param style the {@code byte} representing the visual style of the target color<br /><b>Example:</b><blockquote>{@code Colors.MOCHA}</blockquote>
   * @param part the {@code byte} representing the part of the target color<br /><b>Example:</b><blockquote>{@code Colors.BORDER}</blockquote>
   * @return {@code String} representation of the specified official BH Color in hexadecimal format
   */
  public String getHexString(byte style, byte part)
  {
    return getHexString(getColor(style, part));
  }

  /**
   * Calculates and returns a {@code String} representation of the specified Official BHColor in hexadecimal format
   * @return {@code String} representation of the Official BHColor (which you gave this {@code Colors} object) in hexadecimal format
   */
  public String getHexString()
  {
    return getHexString(style, part);
  }

  /**
   * Calculates and returns a {@code String} representation of the specified Official BHColor in hexadecimal format
   * @param c
   * @return {@code String} representation of the specified official BH Color in hexadecimal format
   */
  public static String getHexString(Color c)
  {
    return bht.tools.util.math.Numbers.lenFmt(Integer.toHexString(c.getRed()),2) +
           bht.tools.util.math.Numbers.lenFmt(Integer.toHexString(c.getGreen()),2) +
           bht.tools.util.math.Numbers.lenFmt(Integer.toHexString(c.getBlue()),2);
  }

  /**
   * Calculates and returns a {@code String} representation of the specified Official BHColor in hexadecimal format
   * @param c the {@code java.awt.Color}
   * @param includeAlpha if {@code true}, another two characters representing the Alpha value of this color will be appended to the return value
   * @return {@code String} representation of the specified official BH Color in hexadecimal format
   */
  public static String getHexString(Color c, boolean includeAlpha)
  {
    return bht.tools.util.math.Numbers.lenFmt(Integer.toHexString(c.getRed()),2) +
           bht.tools.util.math.Numbers.lenFmt(Integer.toHexString(c.getGreen()),2) +
           bht.tools.util.math.Numbers.lenFmt(Integer.toHexString(c.getBlue()),2) + (includeAlpha ?
           bht.tools.util.math.Numbers.lenFmt(Integer.toHexString(c.getAlpha()),2) : "");
  }

  public void fixStyleIn(java.awt.Component comp, java.awt.Font f)
  {
    fixStyleIn(comp, style, f.getName(), f.getStyle(), f.getSize() - DEFAULT_SIZE, false);
  }

  public void fixStyleIn(java.awt.Component comp)
  {
    fixStyleIn(comp, style, DEFAULT_FONT, java.awt.Font.PLAIN, 0, false);
  }

  public void fixStyleIn(java.awt.Component comp, byte style, java.awt.Font f)
  {
    fixStyleIn(comp, style, f.getName(), f.getStyle(), f.getSize() - DEFAULT_SIZE, false);
  }

  public void fixStyleIn(java.awt.Component comp, byte style)
  {
    fixStyleIn(comp, style, DEFAULT_FONT, java.awt.Font.PLAIN, 0, false);
  }

  public void fixStyleIn(java.awt.Component comp, String fontName, int fontStyle, double fontMod)
  {
    fixStyleIn(comp, style, fontName, fontStyle, fontMod, false);
  }

  public final String SPACER = "  ";
  @SuppressWarnings("UseOfSystemOutOrSystemErr")
  public void fixStyleIn(java.awt.Component comp, byte style, String fontName, int fontStyle, double fontMod, boolean print)
  {
    //<editor-fold defaultstate="collapsed" desc="Preface">
    @SuppressWarnings("LocalVariableHidesMemberVariable")
    byte part = (comp instanceof javax.swing.AbstractButton || comp instanceof javax.swing.JSeparator) &&
        !(comp instanceof javax.swing.JMenuItem) ? Colors.BOX :
            (comp instanceof javax.swing.CellRendererPane ||
                (comp instanceof javax.swing.text.JTextComponent && ((javax.swing.text.JTextComponent)comp).isEditable()) ||
                comp instanceof javax.swing.JList ? Colors.TEXT_AREA : Colors.BACK);
    boolean useBorders = !(LookAndFeelChanger.getLookAndFeel() instanceof javax.swing.plaf.nimbus.NimbusLookAndFeel ||
                           LookAndFeelChanger.getLookAndFeel() instanceof com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel);
    String desc;
    
    if (comp == null)
      throw new java.lang.NullPointerException("Cannot fix the UI of a null component");
    
    if (print)
    {
      for (int i = 0; i < spacer; i++)
      {
        System.out.print(SPACER);
      }
      desc = (comp.getName() != null && !comp.getName().isEmpty() ? "Name: \"" + comp.getName() + "\" " : "") + (comp.getAccessibleContext() != null ?
                                                                                                                  (comp.getAccessibleContext().getAccessibleName() != null && !comp.getAccessibleContext().getAccessibleName().isEmpty() ?
          "Accessible Name: \"" + comp.getAccessibleContext().getAccessibleName() + "\" " : "") +
        (comp.getAccessibleContext().getAccessibleDescription() != null && !comp.getAccessibleContext().getAccessibleDescription().isEmpty() ?
            "Accessible Description: \"" + comp.getAccessibleContext().getAccessibleDescription() + "\" " : "") : "");

        System.out.println(comp.getClass().getName() + ": " + desc);
    }
    String accDesc = "";
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Setup">
    try
    {
      accDesc = comp.getAccessibleContext().getAccessibleDescription();
      if (accDesc != null && accDesc.contains(COLOR_STYLE_OVERRIDE))
      {
        try
        {
          style = Byte.parseByte(accDesc.substring(accDesc.indexOf(COLOR_STYLE_OVERRIDE) + COLOR_STYLE_OVERRIDE.length(),
                                                   accDesc.indexOf(COMMAND_SEP, accDesc.indexOf(COLOR_STYLE_OVERRIDE) + COLOR_STYLE_OVERRIDE.length())));
          for (int i = 0; print && i < spacer; i++)
          {
            System.out.print(SPACER);
          }
          if (print)
            System.out.println("Style overridden to " + style);
        }
        catch (Throwable t)
        {
          for (int i = 0; print && i < spacer; i++)
          {
            System.out.print(SPACER);
          }
          if (print)
            System.out.println(t + " caught while overriding the style");
        }
      }
      if (accDesc != null && accDesc.contains(COLOR_PART_OVERRIDE))
      {
        try
        {
          part = Byte.parseByte(accDesc.substring(accDesc.indexOf(COLOR_PART_OVERRIDE) + COLOR_PART_OVERRIDE.length(),
                                                  accDesc.indexOf(COMMAND_SEP, accDesc.indexOf(COLOR_PART_OVERRIDE) + COLOR_PART_OVERRIDE.length())));
          for (int i = 0; print && i < spacer; i++)
          {
            System.out.print(SPACER);
          }
          if (print)
            System.out.println("Part overridden to " + style);
        }
        catch (Throwable t)
        {
          for (int i = 0; print && i < spacer; i++)
          {
            System.out.print(SPACER);
          }
          if (print)
            System.out.println(t + " caught while overriding the style");
        }
      }
      if (accDesc != null && accDesc.contains(SIZE_OVERRIDE))
      {
        try
        {
          fontMod += Integer.parseInt(accDesc.substring(accDesc.indexOf(SIZE_OVERRIDE) + SIZE_OVERRIDE.length(), accDesc.indexOf(COMMAND_SEP, accDesc.indexOf(
                                                                                                                                              SIZE_OVERRIDE) + SIZE_OVERRIDE.length())));
          for (int i = 0; print && i < spacer; i++)
          {
            System.out.print(SPACER);
          }
          if (print)
            System.out.println("Font size overridden to " + (fontMod + DEFAULT_SIZE));
        }
        catch (Throwable t)
        {
          for (int i = 0; print && i < spacer; i++)
          {
            System.out.print(SPACER);
          }
          if (print)
            System.out.println(t + " caught while overriding the size");
        }
      }
      if (accDesc != null && accDesc.contains(SIZE_ONLY_OVERRIDE))
      {
        try
        {
          fontMod = Integer.parseInt(accDesc.substring(accDesc.indexOf(SIZE_ONLY_OVERRIDE) + SIZE_ONLY_OVERRIDE.length(), accDesc.indexOf(COMMAND_SEP, accDesc.indexOf(
                                                                                                                                                       SIZE_ONLY_OVERRIDE) + SIZE_ONLY_OVERRIDE.length()))) - DEFAULT_SIZE;
          for (int i = 0; print && i < spacer; i++)
          {
            System.out.print(SPACER);
          }
          if (print)
            System.out.println("Font size overridden to " + (fontMod + DEFAULT_SIZE));
        }
        catch (Throwable t)
        {
          for (int i = 0; print && i < spacer; i++)
          {
            System.out.print(SPACER);
          }
          if (print)
            System.out.println(t + " caught while overriding the size");
        }
      }
      if (accDesc != null && accDesc.contains(FONT_STYLE_OVERRIDE))
      {
        try
        {
          fontStyle = Integer.parseInt(accDesc.substring(accDesc.indexOf(FONT_STYLE_OVERRIDE) + FONT_STYLE_OVERRIDE.length(), accDesc.indexOf(COMMAND_SEP, accDesc.indexOf(
                                                                                                                                                           FONT_STYLE_OVERRIDE) + FONT_STYLE_OVERRIDE.length())));
          for (int i = 0; print && i < spacer; i++)
          {
            System.out.print(SPACER);
          }
          if (print)
            System.out.println("Font style overridden to " + fontStyle);
        }
        catch (Throwable t)
        {
          for (int i = 0; print && i < spacer; i++)
          {
            System.out.print(SPACER);
          }
          if (print)
            System.out.println(t + " caught while overriding the font style");
        }
      }
      if (accDesc != null && accDesc.contains(FONT_OVERRIDE))
      {
        try
        {
          fontName = accDesc.substring(accDesc.indexOf(FONT_OVERRIDE) + FONT_OVERRIDE.length(), accDesc.indexOf(COMMAND_SEP, accDesc.indexOf(
                                                                                                                             FONT_OVERRIDE) + FONT_OVERRIDE.length()));
          for (int i = 0; print && i < spacer; i++)
          {
            System.out.print(SPACER);
          }
          if (print)
            System.out.println("Font overridden to " + fontName);
        }
        catch (Throwable t)
        {
          for (int i = 0; print && i < spacer; i++)
          {
            System.out.print(SPACER);
          }
          if (print)
            System.out.println(t + " caught while overriding the font");
        }
      }
    }
    catch (NullPointerException ex)
    {
      //      ex.printStackTrace(System.out);
      //      System.out.println("null");
      //      for (int i = 0; print && i < spacer; i++)
      //      {
      //        System.out.print(SPACER);
      //      }
      //      if (print)
      //        System.out.println("No More Components Found Herein");
    }
    catch (Throwable t)
    {
      if (print)
        t.printStackTrace(System.out);
    }
    //</editor-fold>

    spacer++;

    //<editor-fold defaultstate="collapsed" desc="Changing">
    try
    {
      //<editor-fold defaultstate="collapsed" desc="BasicArrowButton (Up/Down buttons of a JSpinner)">
      if (comp instanceof javax.swing.plaf.basic.BasicArrowButton)
      {
        int direction = ((javax.swing.plaf.basic.BasicArrowButton)comp).getDirection();
        
        ((javax.swing.plaf.basic.BasicArrowButton)comp).setBackground(style == DEFAULT ? new javax.swing.plaf.basic.BasicArrowButton(direction).
                                                                      getBackground() : getColor(style, part));
        ((javax.swing.plaf.basic.BasicArrowButton)comp).setForeground(style == DEFAULT ? new javax.swing.plaf.basic.BasicArrowButton(direction).
                                                                      getForeground() : getColor(style, TEXT));
        if (((javax.swing.plaf.basic.BasicArrowButton)comp) != null && ((javax.swing.plaf.basic.BasicArrowButton)comp).getName() != null)
          ((javax.swing.plaf.basic.BasicArrowButton)comp).setToolTipText((direction == javax.swing.plaf.basic.BasicArrowButton.NORTH
                                                                          ? "Increase this value (or scroll up" : "Decrease this value (or scroll down")
                                                                         + " with your mouse wheel)");
        ((javax.swing.plaf.basic.BasicArrowButton)comp).setBorder(style == DEFAULT || !useBorders ?
                                                                  new javax.swing.plaf.basic.BasicArrowButton(direction).getBorder() : new javax.swing.border.LineBorder(
                                                                  getColor(style, BORDER), 1));
      }
      //</editor-fold>
      //<editor-fold defaultstate="collapsed" desc="JCheckBox">
      else if (comp instanceof javax.swing.JCheckBox)
      {
        //        String type = (style == HUSKY ? "Husky" : (style == ASTRO ? "Astro" : "Mocha"));
        //        System.out.println("/resources/" + type + "CheckBox ___.png");
        //        ((javax.swing.JCheckBox)comp).setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/MochaCheckBox deselected.png")));
        //        ((javax.swing.JCheckBox)comp).setSelectedIcon(style == DEFAULT ? null : new javax.swing.ImageIcon(Main.class.
        //        getResource("/resources/" + type + "CheckBox selected.png")));
        //        ((javax.swing.JCheckBox)comp).setDisabledIcon(style == DEFAULT ? null : new javax.swing.ImageIcon(Main.class.
        //        getResource("/resources/" + type + "CheckBox disabled, deselected.png")));
        //        ((javax.swing.JCheckBox)comp).setDisabledSelectedIcon(style == DEFAULT ? null : new javax.swing.ImageIcon(Main.class.
        //        getResource("/resources/" + type + "CheckBox disabled, selected.png")));
        //        ((javax.swing.JCheckBox)comp).setPressedIcon(style == DEFAULT ? null : new javax.swing.ImageIcon(Main.class.
        //        getResource("/resources/" + type + "CheckBox deselected-hover.png")));
        //        ((javax.swing.JCheckBox)comp).setRolloverIcon(style == DEFAULT ? null : new javax.swing.ImageIcon(Main.class.
        //        getResource("/resources/" + type + "CheckBox deselected-hover.png")));
        //        ((javax.swing.JCheckBox)comp).setSelectedIcon(style == DEFAULT ? null : new javax.swing.ImageIcon(Main.class.
        //        getResource("/resources/" + type + "CheckBox selected.png")));
        
        ((javax.swing.JCheckBox)comp).setBackground(style == DEFAULT ? new javax.swing.JCheckBox().getBackground() : getColor(
                                                    style, part));
        ((javax.swing.JCheckBox)comp).setForeground(style == DEFAULT ? new javax.swing.JCheckBox().getForeground() : getColor(
                                                    style, TEXT));
      }
      //</editor-fold>
      //<editor-fold defaultstate="collapsed" desc="JRadioButton">
      else if (comp instanceof javax.swing.JRadioButton)
      {
        ((javax.swing.JRadioButton)comp).setBackground(style == DEFAULT ? new javax.swing.JRadioButton().getBackground() :
                                                       getColor(style, part));
        ((javax.swing.JRadioButton)comp).setForeground(style == DEFAULT ? new javax.swing.JRadioButton().getForeground() :
                                                       getColor(style, TEXT));
      }
      //</editor-fold>
      //<editor-fold defaultstate="collapsed" desc="JTabbedPane">
      else if (comp instanceof javax.swing.JTabbedPane)
      {
        ((javax.swing.JTabbedPane)comp).setBackground(style == DEFAULT ? new javax.swing.JTabbedPane().getBackground() : getColor(
                                                      style, part));
        ((javax.swing.JTabbedPane)comp).setForeground(style == DEFAULT ? new javax.swing.JTabbedPane().getForeground() : getColor(
                                                      style, TEXT));
      }
      //</editor-fold>
      //<editor-fold defaultstate="collapsed" desc="JSpinner">
      else if (comp instanceof javax.swing.JSpinner)
      {
        ((javax.swing.JSpinner)comp).setBorder(style == DEFAULT || !useBorders ? new javax.swing.JSpinner().getBorder() :
                                               (((javax.swing.JSpinner)comp).getBorder() == null ? null : new javax.swing.border.LineBorder(getColor(style, BORDER), 1)));
      }
      //</editor-fold>
      //<editor-fold defaultstate="collapsed" desc="JFormattedTextField">
      else if (comp instanceof javax.swing.JFormattedTextField)
      {
        ((javax.swing.JFormattedTextField)comp).setBackground(style == DEFAULT ? new javax.swing.JFormattedTextField().getBackground() : getColor(
                                                              style, part));
        ((javax.swing.JFormattedTextField)comp).setForeground(style == DEFAULT ? new javax.swing.JFormattedTextField().getForeground() : getColor(
                                                              style, TEXT));
        //        ((javax.swing.JFormattedTextField)comp).getAccessibleContext().setAccessibleDescription(SIZE_OVERRIDE + "48" + COMMAND_SEP);
      }
      //</editor-fold>
      //<editor-fold defaultstate="collapsed" desc="JButton">
      else if (comp instanceof javax.swing.JButton)
      {
        ((javax.swing.JButton)comp).setBackground(getColor(style, part));
        ((javax.swing.JButton)comp).setForeground(getColor(style, TEXT));
        ((javax.swing.JButton)comp).setBorder(style == DEFAULT || !useBorders ? new javax.swing.JButton().getBorder() :
                                              new javax.swing.border.LineBorder(getColor(style, BORDER), 1));
      }
      //</editor-fold>
      //<editor-fold defaultstate="collapsed" desc="JToggleButton">
      else if (comp instanceof javax.swing.JToggleButton)
      {
        ((javax.swing.JToggleButton)comp).setBackground(getColor(style, part));
        ((javax.swing.JToggleButton)comp).setForeground(getColor(style, TEXT));
        ((javax.swing.JToggleButton)comp).setBorder(style == DEFAULT || !useBorders ? new javax.swing.JToggleButton().getBorder() :
                                                    new javax.swing.border.LineBorder(getColor(style, BORDER), 1));
      }
      //</editor-fold>
      //<editor-fold defaultstate="collapsed" desc="JProgressBar">
      else if (comp instanceof javax.swing.JProgressBar)
      {
        ((javax.swing.JProgressBar)comp).setBackground(style == DEFAULT ? new javax.swing.JProgressBar().getBackground() :
                                                       getColor(style, part));
        ((javax.swing.JProgressBar)comp).setForeground(style == DEFAULT ? new javax.swing.JProgressBar().getForeground() :
                                                       getColor(style, TEXT));
        ((javax.swing.JProgressBar)comp).setBorder(style == DEFAULT || !useBorders ? new javax.swing.JProgressBar().getBorder() :
                                                   new javax.swing.border.LineBorder(getColor(style, TEXT), 2));
      }
      //</editor-fold>
      //<editor-fold defaultstate="collapsed" desc="JPopupMenu">
      else if (comp instanceof javax.swing.JPopupMenu)
      {
        ((javax.swing.JPopupMenu)comp).setBackground(getColor(style, part));
        ((javax.swing.JPopupMenu)comp).setForeground(getColor(style, TEXT));
        for (int i = 0; print && i < spacer; i++)
        {
          System.out.print(SPACER);
        }
        //        if (print)
        //          System.out.println("Searching for sub-components...");
        //        for (int i = 0; i < ((javax.swing.JPopupMenu)comp).getComponentCount(); i++)
        //        {
        //          fixStyleIn(((javax.swing.JPopupMenu)comp).getComponent(i), style, fontName, fontStyle, fontMod, false);
        //        }
      }
      //</editor-fold>
      //<editor-fold defaultstate="collapsed" desc="JMenu">
      else if (comp instanceof javax.swing.JMenu)
      {
        ((javax.swing.JMenuItem)comp).setBackground(getColor(style, part));
        ((javax.swing.JMenuItem)comp).setForeground(getColor(style, TEXT));
        for (int i = 0; print && i < spacer; i++)
        {
          System.out.print(SPACER);
        }
        if (print)
          System.out.println("Searching for sub-components...");
        //        for (int i = 0; i < ((javax.swing.JMenu)comp).getMenuComponentCount(); i++)
        //        {
        fixStyleIn(((javax.swing.JMenu)comp).getPopupMenu(), style, fontName, fontStyle, fontMod, print);
        //        }
        fontMod += 1;
      }
      //</editor-fold>
      //<editor-fold defaultstate="collapsed" desc="JMenuItem">
      else if (comp instanceof javax.swing.JMenuItem)
      {
        fontMod += 1;
        ((javax.swing.JMenuItem)comp).setBackground(getColor(style, part));
        ((javax.swing.JMenuItem)comp).setForeground(getColor(style, TEXT));
      }
      //</editor-fold>
      //<editor-fold defaultstate="collapsed" desc="JPanel">
      else if (comp instanceof javax.swing.JPanel)
      {
        ((javax.swing.JPanel)comp).setBackground(style == DEFAULT ? new javax.swing.JPanel().getBackground() :
                                                 getColor(style, part));
        ((javax.swing.JPanel)comp).setForeground(style == DEFAULT ? new javax.swing.JPanel().getForeground() :
                                                 getColor(style, TEXT));
        //        if (((javax.swing.JPanel)comp).getBorder() != null)
        //        {
        //          ((javax.swing.JPanel)comp).setBorder(new javax.swing.border.LineBorder(getColor(style, BORDER), 2));
        //        }
      }
      //</editor-fold>
      //<editor-fold defaultstate="collapsed" desc="JMenuBar">
      else if (comp instanceof javax.swing.JMenuBar)
      {
        ((javax.swing.JMenuBar)comp).setBackground(style == DEFAULT ? new javax.swing.JMenuBar().getBackground() :
                                                   getColor(style, part));
        ((javax.swing.JMenuBar)comp).setForeground(style == DEFAULT ? new javax.swing.JMenuBar().getForeground() :
                                                   getColor(style, TEXT));
        ((javax.swing.JMenuBar)comp).setBorder(style == DEFAULT || !useBorders ? new javax.swing.JMenuBar().getBorder() :
                                               javax.swing.BorderFactory.createLineBorder(getColor(style, part), 1));
        //        java.awt.Graphics g = comp.getGraphics();
        //        g.setColor(getColor(style, BORDER));
        //        for (int i=0; i < comp.getHeight(); i++)
        //          g.drawLine(0, i, comp.getWidth(), i);
        ////        ((javax.swing.JMenuBar)comp).update(g);
        //        ((javax.swing.JMenuBar)comp).paint(g);
      }
      //</editor-fold>
      //<editor-fold defaultstate="collapsed" desc="JSeparator">
      else if (comp instanceof javax.swing.JSeparator)
      {
        ((javax.swing.JSeparator)comp).setBackground(style == DEFAULT ? new javax.swing.JSeparator().getBackground() :
                                                     getColor(style, part));
        ((javax.swing.JSeparator)comp).setForeground(style == DEFAULT ? new javax.swing.JSeparator().getForeground() :
                                                     getColor(style, TEXT));
      }
      //</editor-fold>
      //<editor-fold defaultstate="collapsed" desc="CellRendererPane">
      else if (comp instanceof javax.swing.CellRendererPane)
      {
        ((javax.swing.CellRendererPane)comp).setBackground(style == DEFAULT ? java.awt.SystemColor.text : getColor(style, TEXT_AREA));
        ((javax.swing.CellRendererPane)comp).setForeground(style == DEFAULT ? java.awt.SystemColor.textText : getColor(style, TEXT));
        //        if (((javax.swing.JComponent)comp).getBorder() != null)
        //        {
        //          ((javax.swing.JComponent)comp).setBorder(style == DEFAULT ? new javax.swing.JTextField().getBorder() :
        //            new javax.swing.border.LineBorder(getColor(style, BORDER), 2));
        //        }
      }
      //</editor-fold>
      //<editor-fold defaultstate="collapsed" desc="JTextComponent">
      else if (comp instanceof javax.swing.text.JTextComponent)
      {
        comp.setBackground(style == DEFAULT ? java.awt.SystemColor.text : getColor(style, part));
        comp.setForeground(style == DEFAULT ? java.awt.SystemColor.textText : getColor(style, TEXT));
        if (((javax.swing.JComponent)comp).getBorder() != null)
        {
          ((javax.swing.text.JTextComponent)comp).setBorder(style == DEFAULT || !useBorders ? new javax.swing.JTextField().getBorder() :
                                                            new javax.swing.border.LineBorder(getColor(style, BORDER), ((javax.swing.text.JTextComponent)comp).isEditable() ? 2 : 1));
        }
      }
      //</editor-fold>
      //<editor-fold defaultstate="collapsed" desc="JList">
      else if (comp instanceof javax.swing.JList)
      {
        comp.setBackground(style == DEFAULT ? java.awt.SystemColor.text : getColor(style, part));
        comp.setForeground(style == DEFAULT ? java.awt.SystemColor.textText : getColor(style, TEXT));
        //        if (((javax.swing.JComponent)comp).getBorder() != null)
        //        {
        //          ((javax.swing.JComponent)comp).setBorder(new javax.swing.border.LineBorder(getColor(style, BORDER), 2));
        //        }
      }
      //</editor-fold>
      //<editor-fold defaultstate="collapsed" desc="JComponent">
      else if (comp instanceof javax.swing.JComponent)
      {
        comp.setBackground(style == DEFAULT ? new javax.swing.JComponent(){}.getBackground() : getColor(style, part));
        comp.setForeground(style == DEFAULT ? new javax.swing.JComponent(){}.getForeground() : getColor(style, TEXT));
        //        if (((javax.swing.JComponent)comp).getBorder() != null)
        //        {
        //          ((javax.swing.JComponent)comp).setBorder(new javax.swing.border.LineBorder(getColor(style, BORDER), 2));
        //        }
      }
      //</editor-fold>
      else
      {
        comp.setBackground(style == DEFAULT ? new java.awt.Component(){}.getBackground() : getColor(style, part));
        comp.setForeground(style == DEFAULT ? new java.awt.Component(){}.getForeground() : getColor(style, TEXT));
      }
    }
    catch (Throwable t)
    {
      if (print)
        t.printStackTrace(System.out);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Followup">
    try
    {
      if (((java.awt.Container)comp).getComponentCount() > 0)
      {
        for (int i = 0; print && i < spacer; i++)
        {
          System.out.print(SPACER);
        }
        if (print)
          System.out.println("Searching for sub-components...");
        for (int i = 0; i < ((java.awt.Container)comp).getComponentCount(); i++)
        {
          fixStyleIn(((java.awt.Container)comp).getComponent(i), style, fontName, fontStyle, fontMod, print);
        }
        
        for (int i = 0; print && i < spacer; i++)
        {
          System.out.print(SPACER);
        }
        if (print)
          System.out.println("No more components found herein...");
      }
    }
    catch (ClassCastException ex)
    {
      for (int i = 0; print && i < spacer; i++)
      {
        System.out.print(SPACER);
      }
      if (print)
        System.out.println("Component is not a container. Could not search therein.");
    }
    
    
    try
    {
      if (accDesc != null && accDesc.contains(FORE_OVERRIDE))
      {
        try
        {
          String hexString = accDesc.substring(accDesc.indexOf(FORE_OVERRIDE) + FORE_OVERRIDE.length(),
                                               accDesc.indexOf(COMMAND_SEP, accDesc.indexOf(FORE_OVERRIDE) + FORE_OVERRIDE.length()));
          Color c;
          comp.setForeground(c = getColor(hexString));
          
          if (comp instanceof javax.swing.JComponent && ((javax.swing.JComponent)comp).getBorder() instanceof javax.swing.border.TitledBorder)//Added Feb 18, 2012 (1.0.4) for Marian
            ((javax.swing.border.TitledBorder)((javax.swing.JComponent)comp).getBorder()).setTitleColor(c);
          
          System.out.println(comp.getForeground());
          for (int i = 0; print && i < spacer; i++)
          {
            System.out.print(SPACER);
          }
          if (print)
            System.out.println("Foreground overridden to " + hexString);
        }
        catch (Throwable t)
        {
          for (int i = 0; print && i < spacer; i++)
          {
            System.out.print(SPACER);
          }
          if (print)
            System.out.println(t + " caught while overriding the foreground");
        }
      }
      if (accDesc != null && accDesc.contains(BACK_OVERRIDE))
      {
        try
        {
          String hexString = accDesc.substring(accDesc.indexOf(BACK_OVERRIDE) + BACK_OVERRIDE.length(),
                                               accDesc.indexOf(COMMAND_SEP, accDesc.indexOf(BACK_OVERRIDE) + BACK_OVERRIDE.length()));
          comp.setBackground(getColor(hexString));
          for (int i = 0; print && i < spacer; i++)
          {
            System.out.print(SPACER);
          }
          if (print)
            System.out.println("Background overridden to " + hexString);
        }
        catch (Throwable t)
        {
          for (int i = 0; print && i < spacer; i++)
          {
            System.out.print(SPACER);
          }
          if (print)
            System.out.println(t + " caught while overriding the background");
        }
      }
      for (int i = 0; print && i < spacer; i++)
      {
        System.out.print(SPACER);
      }
      if (print)
        System.out.println("setting font to (" + fontName + ", " + fontStyle + ", " + (fontMod + DEFAULT_SIZE) + ")");
      comp.setFont(new java.awt.Font(fontName, fontStyle, (int)Math.round(fontMod + DEFAULT_SIZE)));
      if (comp instanceof javax.swing.JComponent)//Added Feb. 18, 2012 (1.0.4) for Marian
      {
        if (((javax.swing.JComponent)comp).getBorder() instanceof javax.swing.border.TitledBorder)
        {
          ((javax.swing.border.TitledBorder)((javax.swing.JComponent)comp).getBorder()).setTitleFont(new java.awt.Font(fontName, fontStyle, (int)Math.round(fontMod + DEFAULT_SIZE)));
          ((javax.swing.border.TitledBorder)((javax.swing.JComponent)comp).getBorder()).setTitleColor(style == DEFAULT ?
              new javax.swing.border.TitledBorder("").getTitleColor() : getColor(style, TEXT));
        }
      }
    }
    catch (Throwable t)
    {
      for (int i = 0; print && i < spacer; i++)
      {
        System.err.print(SPACER);
      }
      if (print)
        t.printStackTrace();
    }
    //</editor-fold>
    
    spacer--;
  }

  public void setCustomColor(Color color, byte part)
  {
    switch (part)
    {
      case BORDER:
        custBord = color;
        break;
      case BOX:
        custBox = color;
        break;
      case BACK:
        custBack = color;
        break;
      case TEXT:
        custText = color;
        break;
      case TEXT_AREA:
        custTextArea = color;
        break;
        default:
          throw new IllegalArgumentException("I don't know the part number " + part);
    }
  }

  public Color getCustomColor(byte part)
  {
    switch (part)
    {
      case BORDER:
        return custBord;
      case BOX:
        return custBox;
      case BACK:
        return custBack;
      case TEXT:
        return custText;
    }
    throw new IllegalArgumentException("I don't know the part number " + part);
  }

  public byte getStyle()
  {
    return style;
  }

  /**
   * Changes the style of this {@code Colors} object
   * @param newStyle the new style
   * @return {@code getLastStyle()}
   */
  public byte setStyle(byte newStyle)
  {
    this.last = this.style;
    this.style = newStyle;
    return getLastStyle();
  }

  public void fixStyleInAllWindows()
  {
    fixStyleInAllWindows(style);
  }

  public void fixStyleInAllWindows(byte style)
  {
    fixStyleInAllWindows(style, DEFAULT_FONT);
  }

  public void fixStyleInAllWindows(byte style, java.awt.Font font)
  {
    fixStyleInAllWindows(style, font.getName(), font.getStyle(), DEFAULT_SIZE - font.getSize());
  }

  public void fixStyleInAllWindows(byte style, String fontName)
  {
    fixStyleInAllWindows(style, fontName, java.awt.Font.PLAIN);
  }

  public void fixStyleInAllWindows(byte style, String fontName, int fontStyle)
  {
    fixStyleInAllWindows(style, fontName, fontStyle, 0);
  }

  public void fixStyleInAllWindows(byte style, String fontName, int fontStyle, double fontMod)
  {
    fixStyleInAllWindows(style, fontName, fontStyle, fontMod, false);
  }

  public void fixStyleInAllWindows(byte style, String fontName, int fontStyle, double fontMod, boolean print)
  {
    for (java.awt.Window w : java.awt.Window.getWindows())
      fixStyleIn(w, style, fontName, fontStyle, fontMod, print);
  }

  @Override
  public Colors clone()
  {
    Colors c = new Colors(style, part);
    c.custBack = custBack;
    c.custBord = custBord;
    c.custBox = custBox;
    c.custText = custText;
    c.custTextArea = custTextArea;
    return c;
  }

  public byte getLastStyle()
  {
    return last;
  }
}
