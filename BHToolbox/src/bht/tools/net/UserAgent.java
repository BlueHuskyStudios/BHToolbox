package bht.tools.net;

import bht.tools.net.upd.Version;
import bht.tools.util.ArrayPP;
import static bht.tools.util.Do.A;
import static bht.tools.util.Do.s;

/**
 * UserAgent, made for BHToolbox, is copyright Blue Husky Programming ©2014 GPLv3 <hr/>
 * A data structure for representing a user agent string like
 * {@code Mozilla/[version] ([system and browser information]) [platform] ([platform details]) [extensions]}
 * 
 * @author Kyli of Blue Husky Programming
 * @version 1.0.0
		- 2014-11-29 (1.0.0) - Kyli created UserAgent
 * @since 2014-11-29
 */
public class UserAgent
{
	public static final Program defaultPlatform =
		new Program(
			"Java",
			Version.fromString(System.getProperty("java.version").replace('_', '.')),
			A(System.getProperty("java.vm.name"))
		);
	
	private Program mainProgram;
	private ArrayPP<Program> otherPrograms;
	private Program platform;

	public UserAgent(String initProgramName, Version initProgramVersion)
	{
		this(new Program(initProgramName, initProgramVersion));
	}
	
	public UserAgent(String initProgramName, Version initProgramVersion, String initProgramDetails)
	{
		this(new Program(initProgramName, initProgramVersion, initProgramDetails));
	}
	
	public UserAgent(Program initMainProgram)
	{
		this(initMainProgram, ArrayPP.EMPTY, defaultPlatform);
	}

	public UserAgent(Program initMainProgram, ArrayPP<Program> initOtherPrograms, Program initPlatform)
	{
		mainProgram = initMainProgram;
		otherPrograms = initOtherPrograms;
		platform = initPlatform;
	}

	public Program getMainProgram()
	{
		return mainProgram;
	}

	public ArrayPP<Program> getOtherPrograms()
	{
		return otherPrograms;
	}

	public Program getPlatform()
	{
		return platform;
	}

	@Override
	public String toString()
	{
		return
			s(mainProgram) +
			(platform == null ? "" : " " + platform) +
			(otherPrograms == null ? "" : ' ' + otherPrograms.toString("", " ", ""));
	}
	
	
	
	
	
	public static class Program
	{
		public final String NAME;
		public final Version VERSION;
		public final ArrayPP<String> DETAILS;

		public Program(String NAME, Version VERSION)
		{
			this(NAME, VERSION, ArrayPP.EMPTY);
		}
		
		public Program(String initName, Version initVersion, String initDetails)
		{
			this(initName, initVersion, A(initDetails));
		}
		
		public Program(String initName, Version initVersion, ArrayPP<String> initDetails)
		{
			NAME = initName;
			VERSION = initVersion;
			DETAILS = initDetails;
		}

		@Override
		public String toString()
		{
			return
				NAME + '/' +
				(VERSION == null ? "" : VERSION) +
				(DETAILS == null || DETAILS.isEmpty() ? "" : " " + DETAILS.toString("(", "; ", ")"));
		}
	}
}
