package bht.tools.util.save.general;

import bht.tools.comps.BHTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.WriteAbortedException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 * ObjectSaver, made for Saveable Components, is copyright Blue Husky Programming ©2014 BH-1-PS<hr>
 * A way to very easily save and load objects
 * 
 * @author Kyli of Blue Husky Programming
 * @version 1.0.0
 * @since 2014-08-22
 */
public class ObjectSaver
{
	protected ObjectSaver(){}
	
	public static <T extends Serializable> void save(T objectToSave, CharSequence objectName, CharSequence programName) throws IOException
	{
		ObjectOutputStream out = inventOutputStreamFor(programName, objectName);
		out.writeObject(objectToSave);
	}
	
	public static <T extends Serializable> T load(CharSequence objectName, CharSequence programName) throws IOException, ClassNotFoundException
	{
		return load(objectName, programName, true);
	}
	
	public static <T extends Serializable> T load(CharSequence objectName, CharSequence programName, boolean deleteIfInvalid) throws IOException, ClassNotFoundException
	{
		ObjectInputStream in = inventInputStreamFor(programName, objectName);
		
		Object read = null;
		try
		{
			read = in.readObject();
		}
		catch (InvalidClassException | NotSerializableException | WriteAbortedException e) // if the cache is invalid
		{
			if (deleteIfInvalid)
				delete(objectName, programName);
			return null;
		}
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

	private static void delete(CharSequence objectName, CharSequence programName)
	{
		SaveConstants.inventSaveFileFor(programName, objectName).delete();
	}
	
	
	
	

	protected static ObjectOutputStream inventOutputStreamFor(CharSequence programName, CharSequence fileName) throws IOException
	{
		return new ObjectOutputStream(SaveConstants.inventOutputStreamFor(programName, fileName));
	}

	protected static ObjectInputStream inventInputStreamFor(CharSequence programName, CharSequence fileName) throws IOException
	{
		return new ObjectInputStream(SaveConstants.inventInputStreamFor(programName, fileName));
	}
	
	
	
	
	private static JFrame jf;
	private static BHTextField titleNamer;
	public static void main(String[] args) throws Exception
	{
		if (!ObjectSaver.isSaved("JFrame", ".test"))
		{
			jf = new JFrame();
			titleNamer = new BHTextField("Set window title");
			jf.add(titleNamer);
		}
		else
		{
			jf = ObjectSaver.load("JFrame", ".test");
			titleNamer = (BHTextField)jf.getContentPane().getComponent(0);
		}
		
		titleNamer.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				jf.setTitle(titleNamer.getText());
				try
				{
					ObjectSaver.save(jf, "JFrame", ".test");
				}
				catch (IOException ex)
				{
					Logger.getLogger(ObjectSaver.class.getName()).log(Level.SEVERE, "Could not save JFrame", ex);
				}
			}
		});
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
	}
}
