package bht.tools.util.save.general;

import bht.tools.util.StringPP;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class SaveConstants
{
	public static final String SANDBOX_DIR_STRING;
	public static final String COMPANY_SANDBOX_NAME = "Blue Husky";
	public static final File SANDBOX_DIR_FILE;
	
	static
	{
		SANDBOX_DIR_STRING =
			(new StringPP(System.getProperty("os.name"))
				.containsIgnoreCase("windows")
					? System.getenv("APPDATA")
					: System.getProperty("java.io.tmpdir")
			)
			+ "\\Blue Husky\\"
		;
		SANDBOX_DIR_FILE = new File(SANDBOX_DIR_STRING);
	}
	
	public static String inventSaveDirFor(CharSequence programName, CharSequence saveFileName)
	{
		return SANDBOX_DIR_STRING + programName + "\\" + saveFileName;
	}
	
	public static File inventSaveFileFor(CharSequence programName, CharSequence saveFileName)
	{
		return new File(SANDBOX_DIR_FILE, programName + "\\" + saveFileName);
	}

	static FileOutputStream inventOutputStreamFor(CharSequence programName, CharSequence saveFileName) throws IOException
	{
		File outputFile = inventSaveFileFor(programName, saveFileName);
		outputFile.getParentFile().mkdirs();
		outputFile.createNewFile();
		try
		{
			return new FileOutputStream(outputFile);
		}
		catch (FileNotFoundException ex)
		{
			Logger.getLogger(SaveConstants.class.getName()).log(Level.SEVERE, "Impossible FileNotFound exception", ex);
			throw new UnknownError("The file we just made wasn't found... Somehow.");
		}
	}

	static FileInputStream inventInputStreamFor(CharSequence programName, CharSequence fileName) throws FileNotFoundException
	{
		return new FileInputStream(inventSaveFileFor(programName, fileName));
	}
}
