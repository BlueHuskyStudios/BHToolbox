package bht.tools.misc;

import bht.tools.util.ArrayPP;
import bht.tools.util.StringPP;
import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;



/**
 * Argument, made for BHToolbox, is copyright Blue Husky Programming ©2013 GPLv3<HR/>
 *
 * @author Supuhstar of Blue Husky Programming
 * @version 1.0.0
 * @since 2013-08-08
 */
public abstract class Argument
{
	private CharSequence triggerWord;
	private Parameter[] parameters;

	public Argument(CharSequence initTriggerWord, Parameter[] initParameters)
	{
		triggerWord = initTriggerWord;
		parameters = initParameters;
	}

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

	public Parameter[] getParameters()
	{
		return parameters;
	}

	public void setParameters(Parameter[] parameters)
	{
		this.parameters = parameters;
	}

	public CharSequence getTriggerWord()
	{
		return triggerWord;
	}

	public void setTriggerWord(CharSequence triggerWord)
	{
		this.triggerWord = triggerWord;
	}

	@Override
	public String toString()
	{
		return String.valueOf(triggerWord);
	}



	public static enum DefaultArg
	{
		/** Matches both {@link #POSITIVE} and {@link #NEGATIVE} */
		BOOLEAN,
		/** Matches {@code "yes"}, {@code "true"}, {@code "on"}, {@code "positive"}, and {@code "affirmative"} */
		POSITIVE,
		/** Matches {@code "no"}, {@code "false"}, {@code "off"}, and {@code "negative"} */
		NEGATIVE;

		public boolean matches(CharSequence cs)
		{
			if (cs == null)
				return false;
			StringPP s = new StringPP(cs);

			if (s.equalsAnyIgnoreCase("no", "false", "off", "negative"))
				return this == NEGATIVE || this == BOOLEAN;
			if (s.equalsAnyIgnoreCase("yes", "true", "on", "positive", "affirmative"))
				return this == POSITIVE || this == BOOLEAN;
			return false;
		}
	}
}
