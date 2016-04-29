package bht.tools.fx;

import java.awt.Color;
import java.awt.Graphics;

/**
 * BHGraphicsSurrogate, made for BH Checkers 2, is made by and copyrighted to Blue Husky Programming, ©2012. License is
 * default.<hr> This is a class that allows for the drawing of an anti-aliased line.
 * @deprecated Doesn't work
 * @author Supuhstar of Blue Husky Programming
 * @since Jan 18, 2012
 * @version 1.0.0
 */
public class BHGraphicsSurrogate //NOTE: Must be compiled in UTF-8
{
  private Graphics g;

  public BHGraphicsSurrogate(Graphics graphics)
  {
    g = graphics;
  }
  
  //<editor-fold defaultstate="collapsed" desc="Wikipedia's line algorithm">
  //This block of code was given as "pseudo-code" and interpreted into working Java.

  /**
   * Plots a possibly translucent pixel onto the current graphics environment.
   *
   * @param x the x-coordinate of the pixel to be drawn
   * @param y the y-coordinate of the pixel to be drawn
   * @param alpha the opacity of the pixel to be drawn (0.0 is completely transparent, and 1.0 is completely opaque)
   */
  public void plot(double x, double y, double alpha)//plot the pixel at (x, y) with brightness c (where 0 <= c <= 1)
  {
//      if ((int)alpha != 0)
//        System.out.println(alpha);
    Color col = g.getColor();
    g.setColor(new Color(col.getRed(), col.getGreen(), col.getBlue(), ((int) ((alpha % 1) * 255))));
    g.drawLine((int) x, (int) y, (int) x, (int) y);
  }

  private int ipart(double x)
  {
    return (int) x;
  }

  private int round(double x)
  {
    return ipart(x + 0.5);
  }

  private double fpart(double x)
  {
    return x % 1;
  }

  private double rfpart(double x)
  {
    return 1 - fpart(x);
  }

  public void drawLine(double x1, double y1, double x2, double y2)
  {
    double dx = x2 - x1;
    double dy = y2 - y1;
    double dummy; //used for switching only

    if (Math.abs(dx) < Math.abs(dy))
    {
      //<editor-fold defaultstate="collapsed" desc="swap x1, y1">
      dummy = x1;
      x1 = y1;
      y1 = dummy;
      //</editor-fold>

      //<editor-fold defaultstate="collapsed" desc="swap x2, y2">
      dummy = x2;
      x2 = y2;
      y2 = dummy;
      //</editor-fold>

      //<editor-fold defaultstate="collapsed" desc="swap dx, dy">
      dummy = dx;
      dx = dy;
      dy = dummy;
      //</editor-fold>
    }

    if (x2 < x1)
    {
      //<editor-fold defaultstate="collapsed" desc="swap x1, x2">
      dummy = x1;
      x1 = x2;
      x2 = dummy;
      //</editor-fold>

      //<editor-fold defaultstate="collapsed" desc="swap y1, y2">
      dummy = y1;
      y1 = y2;
      y2 = dummy;
      //</editor-fold>
    }
    double gradient = dy / dx;

    // handle first endpoint
    double xend = round(x1);
    double yend = y1 + gradient * (xend - x1);
    double xgap = rfpart(x1 + 0.5);
    double xpxl1 = xend;  // this will be used in the main loop
    double ypxl1 = ipart(yend);
    plot(xpxl1, ypxl1, rfpart(yend) * xgap);
    plot(xpxl1, ypxl1 + 1, fpart(yend) * xgap);
    double intery = yend + gradient; // first y-intersection for the main loop

    // handle second endpoint
    xend = round(x2);
    yend = y2 + gradient * (xend - x2);
    xgap = fpart(x2 + 0.5);
    double xpxl2 = xend;  // this will be used in the main loop
    double ypxl2 = ipart(yend);
    plot(xpxl2, ypxl2, rfpart(yend) * xgap);
    plot(xpxl2, ypxl2 + 1, fpart(yend) * xgap);

    // main loop
    for (double x = xpxl1 + 1; x < xpxl2 - 1; x++)
    {
      plot(x, ipart(intery), rfpart(intery));
      plot(x, ipart(intery) + 1, fpart(intery));
      intery = intery + gradient;
    }
  }
  //</editor-fold>
}
