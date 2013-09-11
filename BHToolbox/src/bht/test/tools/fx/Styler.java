package bht.test.tools.fx;

import java.awt.Color;
import java.awt.Font;



/**
 * Colors3, made for Error on line 6, column 24 in Templates/Classes/Class.java
 * Expecting a string, date or number here, Expression project is instead a freemarker.template.SimpleHash, is copyright Blue
 * Husky Programming, ©2013 <HR/>
 *
 * @author csu of Blue Husky Programming
 * @version 1.0.0
 * @since 2013-09-11
 */
public class Styler
{
	public static interface Stylable
	{
		public Style[] getStyles();
	}
	
	public static abstract class Style
	{
		private Color color;

		public void setColor(Color newColor)
		{
			color = newColor;
		}
		public Color getColor()
		{
			return color;
		}
		
		public static class TextStyle extends Style
		{
			private Font font;
		}
	}
}
