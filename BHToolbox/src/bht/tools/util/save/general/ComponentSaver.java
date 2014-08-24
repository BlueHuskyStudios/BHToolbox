package bht.tools.util.save.general;

import bht.tools.util.save.general.ObjectSaver;
import java.awt.Container;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * ComponentSaver, made for BHToolbox, is copyright Blue Husky Programming ©2014 CC 3.0 BY-SA<HR/>
 * 
 * @deprecated doesn't do anything different than {@link ObjectSaver}
 * 
 * @author Kyli of Blue Husky Programming
 * @version 1.0.0
 * @since 2014-08-23
 * @deprecated doesn't do anything different than {@link ObjectSaver}
 */
public class ComponentSaver extends ObjectSaver // TODO: Put components in folders
{
	protected ComponentSaver(){}
	
	public static <T extends Container> void save(T objectToSave, CharSequence objectName, CharSequence programName) throws IOException
	{
		ObjectOutputStream out = inventOutputStreamFor(programName, objectName);
		out.writeObject(objectToSave);
	}
	
	public static <T extends Container> T load(CharSequence objectName, CharSequence programName) throws IOException, ClassNotFoundException
	{
		ObjectInputStream in = inventInputStreamFor(programName, objectName);
		
		Object read = in.readObject();
		try
		{
			return (T)read;
		}
		catch (ClassCastException ex)
		{
			throw new IllegalStateException("Didn't expect to read " + (read == null ? "null" : read.getClass()), ex);
		}
	}
	
	public static boolean isSaved(CharSequence objectName, CharSequence programName) throws IOException, ClassNotFoundException
	{
		return SaveConstants.inventSaveFileFor(programName, objectName).exists();
	}
}
