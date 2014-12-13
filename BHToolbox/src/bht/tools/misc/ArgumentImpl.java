package bht.tools.misc;

import bht.tools.util.ArrayPP;
import bht.tools.util.StringPP;



/**
 * Argument, made for BHToolbox, is copyright Blue Husky Programming ©2013 GPLv3<HR/>
 *
 * @author Supuhstar of Blue Husky Programming
 * @version 1.0.0
 * @since 2013-08-08
 * 
 * @deprecated Never quite got it to work
 */
public abstract class ArgumentImpl extends Argument
{
	private CharSequence triggerWord;
	private Parameter[] parameters;

	public ArgumentImpl(CharSequence initTriggerWord, Parameter[] initParameters)
	{
		super(initTriggerWord, initParameters);
		triggerWord = initTriggerWord;
		parameters = initParameters;
	}

	public abstract Argument argumentCalled();

	public boolean matches(CharSequence rawInput)
	{
		StringPP rawInputPP = StringPP.makeStringPP(rawInput);
		ArrayPP inputWords = rawInputPP.getWords();

		return matches(inputWords);
	}

	public boolean matches(Iterable<CharSequence> inputWords)
	{
		ArrayPP<CharSequence> inputWordsPP = new ArrayPP<>(inputWords);
		return matches(inputWordsPP.toArray());
	}

	public boolean matches(CharSequence... inputWords)
	{
		if (inputWords == null // If no input words are specified
				|| inputWords.length == 0 // If the array of input words is empty
				|| !String.valueOf(inputWords[0]).equalsIgnoreCase(String.valueOf(triggerWord))) // If the array does not start with the trigger word
			return false;
		for (int i = 0; i < inputWords.length; i++)
		{
			CharSequence charSequence = inputWords[i];
			if (StringPP.makeStringPP(triggerWord).equalsIgnoreCase(inputWords[i]))
			{
				if (parameters == null)
					return true;
				ArrayPP paramsPP = new ArrayPP(parameters);
				/* for (Object object : paramsPP)
				 * {
				 * if (object instanceof Object[])
				 * {
				 *
				 * }
				 * if (object instanceof DefaultArg)
				 *
				 * } */
				if (paramsPP.contains((Object)null))
					return true;
				// TODO: Finish this
			}
		}
		return false;
	}

	public ArgumentImpl.ArgumentParser getNewParser()
	{
		return new ArgumentImpl.ArgumentParser(this);
	}

	private int getMinRequiredParameters()
	{
		int mrp = 0;
		for (Object param : parameters)
		{
			if (recursiveNullCheck(param))
				break;
			mrp++;
		}
		return mrp;
	}

	private boolean recursiveNullCheck(Object hasNull)
	{
		if (hasNull == null)
			return true;
		if (hasNull instanceof Object[])
			for (Object o : (Object[])hasNull)
				if (recursiveNullCheck(hasNull))
					return true;
		return false;
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



	public class ArgumentParser
	{
		private ArgumentImpl basis;
		private int numMatchedArgs = 0;

		protected ArgumentParser(ArgumentImpl initBasis)
		{
			basis = initBasis;
		}

		public boolean needsMore()
		{
			return basis.getMinRequiredParameters() > numMatchedArgs;
		}

		public boolean takes(CharSequence cs)
		{
			return (cs == null && basis.parameters[numMatchedArgs] == null)
					|| recursiveEqualityCheck(basis.parameters[numMatchedArgs], cs);
		}

		private boolean recursiveEqualityCheck(Object o1, Object o2)
		{
			if (o1 == o2)
				return true;
			if (o1 instanceof Object[])
				if (o2 instanceof Object[])
					for (Object o1o : (Object[])o1)
						for (Object o2o : (Object[])o2)
							return recursiveEqualityCheck(o1o, o2o);
				else
					for (Object o1o : (Object[])o1)
						return recursiveEqualityCheck(o1o, o2);
			if (o2 instanceof Object[])
				for (Object o2o : (Object[])o2)
					return recursiveEqualityCheck(o1, o2o);
			return false;
		}

		public void parseWord(CharSequence charSequence)
		{
			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
		}
	}
}
