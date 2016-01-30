package bht.tools.misc;

import static bht.tools.util.Do.s;
import static bht.tools.util.Do.S;
import bht.tools.util.StringPP;



/**
 * Argument, made for BHToolbox, is copyright Blue Husky Programming ©2013 BH-1-PS<HR/>
 *
 * @author Supuhstar of Blue Husky Programming
 * @version 1.1.1
 *		- 1.1.1 (2014-11-29) - Kyli Rouge folded and formatted code
 *		- 1.1.0 (2014-11-29) - Kyli Rouge added trigger character, documentation, and more boolean values
 * @since 2013-08-08
 */
public abstract class Argument
{
	private char triggerChar;
	private CharSequence triggerWord;
	private Parameter[] parameters;

	//<editor-fold defaultstate="collapsed" desc="init">
	public Argument(char initTriggerChar)
	{
		this(initTriggerChar, null);
	}
	
	public Argument(CharSequence initTriggerWord)
	{
		this(initTriggerWord, null);
	}
	
	public Argument(CharSequence initTriggerWord, Parameter[] initParameters)
	{
		this(initTriggerWord.charAt(0), initTriggerWord, initParameters);
	}
	
	public Argument(char initTriggerChar, Parameter[] initParameters)
	{
		this(initTriggerChar, null, initParameters);
	}
	
	public Argument(char initTriggerChar, CharSequence initTriggerWord, Parameter[] initParameters)
	{
		triggerChar = initTriggerChar;
		triggerWord = initTriggerWord;
		parameters = initParameters;
	}
	//</editor-fold>

	//<editor-fold defaultstate="collapsed" desc="core functionality">
	/**
	 * Alerts this class that the argument has been provided in the command line
	 *
	 * @param withParameters the parameters this argument has been provided with
	 *
	 * @return {@code this}
	 */
	public abstract Argument provide(Parameter... withParameters);
	
	/**
	 * Reports whether the argument has been provided
	 *
	 * @return {@code true} iff this argument has been provided already
	 */
	public abstract boolean isProvided();
//</editor-fold>

	//<editor-fold defaultstate="collapsed" desc="set/get">
	/**
	 * Returns the parameters
	 * @return the parameters
	 */
	public Parameter[] getParameters()
	{
		return parameters;
	}
	
	/**
	 * Sets the parameters
	 * @param parameters the new parameters
	 */
	public void setParameters(Parameter[] parameters)
	{
		this.parameters = parameters;
	}
	
	/**
	 * Returns the trigger word
	 * @return the trigger word
	 */
	public CharSequence getTriggerWord()
	{
		return triggerWord;
	}
	
	/**
	 * Sets the trigger word
	 * @param triggerWord the new trigger word
	 */
	public void setTriggerWord(CharSequence triggerWord)
	{
		this.triggerWord = triggerWord;
	}
	
	/**
	 * Returns the trigger character
	 * @return the trigger character
	 */
	public char getTriggerChar()
	{
		return triggerChar;
	}
	
	/**
	 * Sets the new trigger character. If this is 0, then it is considered to not exist.
	 * @param triggerChar the new trigger character
	 */
	public void setTriggerChar(char triggerChar)
	{
		this.triggerChar = triggerChar;
	}
	//</editor-fold>

	//<editor-fold defaultstate="collapsed" desc="overrides">
	@Override
	public String toString()
	{
		return
				(triggerChar == 0
			 ? ""
			 : '-' + s(triggerChar)
			) +
			(triggerWord == null || triggerWord.length() == 0
			 ? (triggerChar == 0
				   ? "[no arg]"
				   : "")
			 : "--" + s(triggerWord)
			);
	}
	//</editor-fold>



	//<editor-fold defaultstate="collapsed" desc="inner classes">
	/**
	 * A set of default parameters
	 */
	public static enum DefaultArg
	{
		/** Matches both {@link #POSITIVE} and {@link #NEGATIVE} */
		BOOLEAN,
		/** Matches {@code "1"}, {@code "y"}, {@code "yes"}, {@code "true"}, {@code "on"}, {@code "positive"}, and {@code "affirmative"} */
		POSITIVE,
		/** Matches {@code "0"}, {@code "n"}, {@code "no"}, {@code "false"}, {@code "off"}, and {@code "negative"} */
		NEGATIVE;
		
		public boolean matches(CharSequence cs)
		{
			if (cs == null)
				return false;
			StringPP s = S(cs);
			
			if (s.equalsAnyIgnoreCase("1", "y", "yes", "true",  "on",  "positive", "affirmative"))
				return this == POSITIVE || this == BOOLEAN;
			if (s.equalsAnyIgnoreCase("0", "n", "no",  "false", "off", "negative"))
				return this == NEGATIVE || this == BOOLEAN;
			return false;
		}
	}
	//</editor-fold>
}
