/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bht.tools.fx;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.text.ParseException;
import javax.swing.JComponent;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;

/**
 *
 * @author Supuhstar
 */                                                                   //For JDK 1.7
class TestLAF extends com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel//javax.swing.plaf.nimbus.NimbusLookAndFeel
{
  byte style;
  private Colors colors = new Colors();
  final Font DEFAULT_FONT = new Font(Colors.DEFAULT_FONT, Font.PLAIN, (int)Math.round(Colors.DEFAULT_SIZE));
  
  public TestLAF(final byte style)
  {
    this.style = style;
    
    try
    {
      load(new java.net.URL("jar:http://s.supuhstar.operaunite.com/webserver/content/BHLAF.jar!/laf.xml"));
    }
    catch (ParseException ex)
    {
      System.err.println("Could not parse styles from SupuhComputer");
    }
    catch (IOException ex)
    {
      System.err.println("Could not load styles from SupuhComputer");
    }
    setStyleFactory(new javax.swing.plaf.synth.SynthStyleFactory() 
    {
      @Override
      public SynthStyle getStyle(JComponent c, Region id)
      {
        if (c instanceof javax.swing.JButton)
          return new javax.swing.plaf.synth.SynthStyle()
          {
            @Override
            protected Color getColorForState(SynthContext context, ColorType type)
            {
              return colors.getColor(style, context.getRegion().getName().equals(Region.TOOL_BAR.getName()) ? Colors.BACK : Colors.BOX);
            }

            @Override
            protected Font getFontForState(SynthContext context)
            {
              return DEFAULT_FONT;
            }
          };
        else
          return new SynthStyle() 
          {
            @Override
            protected Color getColorForState(SynthContext context, ColorType type)
            {
              return colors.getColor(style, Colors.BACK);
            }

            @Override
            protected Font getFontForState(SynthContext context)
            {
              return DEFAULT_FONT;
            }
          };
      }
    });
  }
}
