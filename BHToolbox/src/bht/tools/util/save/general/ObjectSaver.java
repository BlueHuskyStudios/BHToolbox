package bht.tools.util.save.general;

import bht.tools.comps.BHTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 * ObjectSaver, made for Saveable Components, is copyright Blue Husky Programming ©2014 CC 3.0 BY-SA<HR/>
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
