package bht.tools.util.dynamics;

import java.awt.geom.Point2D;

/**
 * Bézier2D, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012. License is default.<hr/>
 * Utilizes the {@link ProgressingValue} class to represent a continuous Bézier curve on a 2D coordinate plane.
 *
 * @author Supuhstar of Blue Husky Programming
 * @since Nov 8, 2012
 * @version 1.0.0
 */
public class Bézier2D extends ProgressingValue //NOTE: Must be compiled in UTF-8
{
	public static final Bézier2D DEFAULT = new Bézier2D();
	private Point2D.Double[] points = new Point2D.Double[4];

	public Bézier2D()
	{
		this(0, new Point2D.Double(0.5, 0), new Point2D.Double(0.5, 1), 1);
	}

	public Bézier2D(double y0, Point2D.Double p1, Point2D.Double p2, double y3)
	{
		points[0] = new java.awt.geom.Point2D.Double(0, y0);
		points[1] = p1;
		points[2] = p2;
		points[3] = new java.awt.geom.Point2D.Double(1, y3);
	}

	/**
	 * Returns the predefined position along this Bézier curve, from {@code 0.0} to {@code 1.0}, inclusive. Input must also be
	 * in this range.
	 *
	 * @param position the horizontal position of the observer, from {@code 0.0} (far left) to {@code 1.0} (far right),
	 * inclusive
	 * @return the vertical position of the Bézier curve at the given horizontal position
	 */
	@Override
	public double getValueAtPosition(double position)
	{
		if (position > 1 || position < 0)
			position = position - (int) position;//Only use the value after the decimal place
		Point2D.Double p01, p12, p23,
				p01_12, p12_23;
		p01 = findProgressPoint(points[0], points[1], position);
		p12 = findProgressPoint(points[1], points[2], position);
		p23 = findProgressPoint(points[2], points[3], position);

		p01_12 = findProgressPoint(p01, p12, position);
		p12_23 = findProgressPoint(p12, p23, position);

		return findProgressPoint(p01_12, p12_23, position).y;
	}

	/*public static void main(String[] args)
	 {
	 Bézier2D bd = new Bézier2D(2, new java.awt.geom.Point2D.Double(0.5, 2), new java.awt.geom.Point2D.Double(0.5, 4), 4);
	 double max = 100;
	 for (int i = 0; i <= max; i++)
	 {
	 System.out.println(i + ": " + (bd.getValueAtPosition(i / max) * max));
	 }
	 }*/
	private Point2D.Double findProgressPoint(Point2D.Double a, Point2D.Double b, double position)
	{
		/*if (position == 0)
		 return a;
		 if (position == 1)
		 return b;*/
		return new Point2D.Double(a.x + (b.x - a.x) * position, a.y + (b.y - a.y) * position);
	}

	public static Bézier2D getReverse(Bézier2D b2d)
	{
		return new Bézier2D(b2d.points[3].y, b2d.points[2], b2d.points[1], b2d.points[0].y);
	}
}
