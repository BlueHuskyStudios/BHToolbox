package bht.tools.util.math;

import java.awt.geom.Point2D;

/**
 * Vector2D, made for BLISS, is copyright Blue Husky Programming ©2013 BH-1-PS<HR/>
 * Allows for easy vector math
 *
 * @author Supuhstar of Blue Husky Programming
 * @version 1.0.0
 * @since 2013-08-01
 */
public class Vector2D
{
	private double x1, y1, x2, y2;

	public Vector2D()
	{
	}

	public double getMagnitude()
	{
		Point2D end = getEnd();
		return Math.sqrt(Math.pow(x1 - end.getX(), 2) + Math.pow(y1 - end.getY(), 2));// Distance between two points 
	}

//	public Vector2D setMagnitude(double newMagnitude)
//	{
//		Math.pow(magnitude, 2) = Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2);// Distance between two points 
//	}

	public Point2D getRelativeEnd()
	{
		return new java.awt.geom.Point2D.Double(x2, y2);
	}

	public Vector2D setRelativeEnd(Point2D newEnd)
	{
		x2 = newEnd.getX();
		y2 = newEnd.getY();
		return this;
	}
	
	public Point2D getAnchor()
	{
		return new java.awt.geom.Point2D.Double(x1, y1);
	}
	
	public Vector2D setAnchor(Point2D newAnchor)
	{
		x1 = newAnchor.getX();
		y1 = newAnchor.getY();
		return this;
	}
	
	public Point2D getEnd()
	{
		return new java.awt.geom.Point2D.Double(x1 + x2, y1 + y2);
	}
	
	public Vector2D setEnd(Point2D newEnd)
	{
		x2 = newEnd.getX() - x1;
		y2 = newEnd.getY() - y1;
		return this;
	}

	@Override
	public Vector2D clone()
	{
		return new Vector2D().setAnchor(getAnchor()).setRelativeEnd(getRelativeEnd());
	}

	public static Vector2D add(Vector2D a, Vector2D b)
	{
		Vector2D ret = new Vector2D();
		Point2D are = a.getRelativeEnd(), bre = b.getRelativeEnd();
		ret.setRelativeEnd(new java.awt.geom.Point2D.Double(are.getX() + bre.getX(), are.getY() + bre.getY()));
		ret.setAnchor(b.getEnd());
		return ret;
	}

	@Override
	public String toString()
	{
		Point2D re = getRelativeEnd();
		return "\u27E8" + re.getX() + "," + re.getY() + "\u27E9";//U+27E8 and U+27E9 are chevrons, AKA angle brackets
	}
}
