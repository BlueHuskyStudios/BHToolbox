package bht.tools.net.upd;

import bht.tools.net.upd.Changelog.ChangeType;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

/**
 * UpdateChecker, made for BHToolbox, is copyright Blue Husky Programming ©2014 GPLv3<HR/>
 * 
 * @author Kyli of Blue Husky Programming
 * @version 1.0.0
 * @since 2014-09-23
 */
public class UpdateChecker
{
	/**
	 * Checks to see if the content at the given URL indicates an update is available. The content can be in one of two forms:
	 * 
	 * <ol>
	 *	<li>A plaintext file with the new version on the first line and a valid URI to the entire update package on the second line</li>
	 *	<li>A JSON file with the following fields:
	 *		<dl>
	 *			<dt>{@code version} (required)</dt>
	 *				<dd>The latest version, matching this regex: {@code /\d(\.\d)+/}</dd>
	 *			<dt>{@code changes} (optional)</dt>
	 *				<dd>An array of objects representing changes, each in the following format:<br/>
	 *					{@code {"<type>": "<description>"}}<br/>
	 *					where {@code <type>} is one of {@code +-!.x}, corresponding to the symbols of each {@link ChangeType},
	 *					and {@code <description>} is a short description of the change. Those changes that indicate a bug was
	 *					found should also cite a corresponding issue-tracker number.
	 *				</dd>
	 *			<dt>{@code package} (required)</dt>
	 *				<dd>A valid URI to the entire update package</dd>
	 *		</dl>
	 *	</li>
	 * </ol>
	 * 
	 * <strong>Examples:</strong>
	 * <dl>
	 *	<dt>Format 1:</dt>
	 *		<dd>
	 * <pre>
	 * 1.0.1
	 * http://prog.BHStudios.org/myApp/myApp_1.0.1.exe
	 * </pre>
	 *		</dd>
	 *	<dt>Format 2:</dt>
	 *		<dd>
	 * <pre>
	 * {
	 * 	"version": "1.0.1",
	 * 	"changes": [
	 * 		{"+": "Added an 'OK' button"},
	 * 		{"-": "Removed 'Oh no!' message"},
	 * 		{"!": "Fixed bug where affirmative button did nothing (bug 12345)"},
	 * 		{".": "Moved action buttons to the right"},
	 * 		{"x": "Found bug where 'OK' button opens a new dialog (bug 12346)"}
	 * 	],
	 * 	"package": "http://prog.BHStudios.org/myApp/myApp_1.0.1.exe"
	 * }
	 * </pre>
	 *		</dd>
	 * </dl>
	 * 
	 * @param updateLocation A valid URL pointing to a file with the previously described content
	 * @param currentVersion The current version, to compare against
	 * @return an {@link Update} if the given URI indicate a new version is available. Else, {@code null}
	 */
	public static Update checkForUpdates(URL updateLocation, Version currentVersion) throws MalformedURLException, IOException
	{
		URLConnection connection = updateLocation.openConnection();
		connection.setAllowUserInteraction(false);
		connection.setDoInput(true);
		connection.setDoOutput(false);
		connection.setRequestProperty("User-Agent",
			  "BHUpdateChecker/1.0.0 "
			+ "Java/" + System.getProperty("java.version") + "(" + System.getProperty("java.vm.name") + ")");
		connection.connect();
		BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
		
		int data;
		ArrayList<Integer> dataArrayList = new ArrayList<>(100);
		while ((data = in.read()) != -1)
			dataArrayList.add(data);
		
		
		char[] dataArray = new char[dataArrayList.size()];
		for (int i = 0; i < dataArray.length; i++)
			dataArray[i] = (char)(int)dataArrayList.get(i);
		
		String dataString = new String(dataArray);
		Scanner scan = new Scanner(dataString);
		if (scan.hasNextLine())
		{
			String read = scan.nextLine();
			try
			{
				Version newVersion = Version.fromString(read);
				if (newVersion.compareTo(currentVersion) <= 0) // if no update is available, return null as contracted
					return null;
				read = scan.nextLine();
				URI newPackage = new URI(read);
				return new Update(newVersion, newPackage);
			}
			catch (IllegalArgumentException | URISyntaxException | NullPointerException ex) // version or URL do not parse. Hoping it's JSON
			{
				try
				{
					ScriptEngine js = new ScriptEngineManager().getEngineByName("javascript");
					Object updateData = js.eval("u=" + dataString);
					if (updateData instanceof JSObject)
					{
						JSObject jso = (JSObject)updateData;

						Object member;
						Version newVersion;
						URI newPackage;
						Changelog changes = null;
						{
							member = jso.getMember("version");
							if (!(member instanceof String))
								throw new RuntimeException("No version number given");
							newVersion = Version.fromString((String)member);

							if (newVersion.compareTo(currentVersion) <= 0) // if no update is available, return null as contracted
								return null;
						}
						{
							member = jso.getMember("package");
							if (!(member instanceof String))
								throw new RuntimeException("No update package given");
							newPackage = new URI((String)member);
						}
						{
							member = jso.getMember("changes");
							if (member instanceof ScriptObjectMirror)
								changes = Changelog.fromSciptObjectMirror((ScriptObjectMirror)member);
						}
						return new Update(newVersion, newPackage, changes);
					}
				}
				catch (Throwable e)
				{
					Logger.getGlobal().log(Level.SEVERE, "Can't parse update. Here's the raw update information:\r\n" + dataString, e);
				}
			}
		}
		return null;
	}
	
	
	public static void main(String[] args) throws ScriptException, IOException, URISyntaxException
	{
		System.out.println("What version are you using?");
		Version v = Version.fromString(new Scanner(System.in).nextLine());
		Update u = checkForUpdates(new URL("http://prog.bhstudios.org/bhmi/update.txt"), v);
		System.out.println(u == null ? "No updates available" : u);
		/*
		ScriptEngine js = new ScriptEngineManager().getEngineByName("javascript");
		Object updateData = js.eval("u={\n" +
			"	\"version\": \"1.0.1\",\n" +
			"	\"changes\": [\n" +
			"		{\"+\": \"Added an 'OK' button\"},\n" +
			"		{\"-\": \"Removed 'Heck yeah!' button\"},\n" +
			"		{\"!\": \"Fixed bug where affirmative button did nothing\"},\n" +
			"		{\".\": \"Moved action buttons to the right\"},\n" +
			"		{\"x\": \"Found bug where 'OK' button opens a new dialog\"}\n" +
			"	],\n" +
			"	\"package\": \"http://prog.BHStudios.org/myApp/myApp_1.0.1.exe\"\n" +
			"}"
		);
		JSObject jso = (JSObject)updateData;
		Object version = jso.getMember("version");
		Object changes = jso.getMember("changes");
		Object pkg = jso.getMember("package");
		Object derp = jso.getMember("derp");
		Changelog.fromSciptObjectMirror((ScriptObjectMirror)changes);*/
	}
}
