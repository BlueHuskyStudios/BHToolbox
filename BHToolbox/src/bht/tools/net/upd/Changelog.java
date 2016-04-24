package bht.tools.net.upd;

import bht.tools.util.ArrayPP;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.Set;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

/**
 * Changelog, made for BHToolbox, is copyright Blue Husky Programming ©2014 BH-1-PS<HR/>
 * 
 * @author Kyli of Blue Husky Programming
 * @version 1.0.0
 * @since 2014-09-23
 */
public class Changelog
{
	private Change[] changes;

	public Changelog(Change[] changes)
	{
		this.changes = changes;
	}

	public static Changelog fromSciptObjectMirror(ScriptObjectMirror json)
	{
		Set<Entry<String, Object>> changeSet = json.entrySet();
		
		Change[] changes = new Change[changeSet.size()];
		Entry[] entries = new Entry[changes.length];
		entries = changeSet.toArray(entries);
		
		
		
		try
		{
			for (int i = 0; i < changes.length; i++)
			{
				Object jsonChange = entries[i].getValue();
				if (!(jsonChange instanceof ScriptObjectMirror))
					throw new IllegalArgumentException("Given ScriptObjectMirror must be an array of other ScriptObjectMirrors");
				ScriptObjectMirror jsonChangeSOM = (ScriptObjectMirror) jsonChange;
				String type = jsonChangeSOM.keySet().toArray(new String[]{})[0];
				String desc = (String)jsonChangeSOM.get(type);
				changes[i] = new Change(ChangeType.fromString(type), desc);
			}
		}
		catch (ClassCastException e)
		{
			throw new IllegalArgumentException("Given ScriptObjectMirror not properly formatted", e);
		}
		return new Changelog(changes);
	}

	@Override
	public String toString()
	{
		return "Changelog{" + "changes=" + Arrays.toString(changes) + '}';
	}
	
	public static class Change
	{
		private ChangeType type;
		private String description;

		public Change(ChangeType type, String description)
		{
			this.type = type;
			this.description = description;
		}

		public ChangeType getType()
		{
			return type;
		}

		public void setType(ChangeType type)
		{
			this.type = type;
		}

		public String getDescription()
		{
			return description;
		}

		public void setDescription(String description)
		{
			this.description = description;
		}

		@Override
		public String toString()
		{
			return type + " " + description;
		}
	}
	
	public static enum ChangeType
	{
		/** Indicates something was added. Symbol is {@code +} */
		ADDITION    ('+'),
		/** Indicates something was removed. Symbol is {@code -} */
		REMOVAL     ('-'),
		/** Indicates something was improved and/or fixed. Symbol is {@code !} */
		IMPROVEMENT ('!'),
		/** Indicates something was changed in a way that can't be better described by other ChangeTypes. Symbol is {@code .} */
		BASIC_CHANGE('.'),
		/** Indicates a bug was found. Symbol is {@code x} */
		BUG_FOUND   ('x');
		
		public final char INDICATOR;
		private ChangeType(final char indicator)
		{
			INDICATOR = indicator;
		}

		@Override
		public String toString()
		{
			return Character.toString(INDICATOR);
		}
		
		/**
		 * Returns the ChangeType that corresponds with the given indicator
		 * 
		 * @param indicator one of these 5 indicators: {@code +-!.x}
		 * @return the ChangeType that corresponds with the given indicator
		 * @throws IllegalArgumentException if the given indicator doesn't start with one of the given indicators
		 */
		public static ChangeType fromString(String indicator)
		{
			char indChar = indicator.charAt(0);
			
			for (ChangeType changeType : values())
				if (changeType.INDICATOR == indChar)
					return changeType;
			
			/*switch (indChar) // may be faster, but is less dynamic
			{
				case '+': return ADDITION;
				case '-': return REMOVAL;
				case '!': return IMPROVEMENT;
				case '.': return BASIC_CHANGE;
				case 'x': return BUG_FOUND;
			}*/
			throw new IllegalArgumentException("Given string does not represent a change type: " + indicator);
		}
	}
}
