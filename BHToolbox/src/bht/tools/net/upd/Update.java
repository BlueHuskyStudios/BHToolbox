package bht.tools.net.upd;

import java.net.URI;

/**
 * Update, made for BHToolbox, is copyright Blue Husky Programming ©2014 GPLv3<HR/>
 * 
 * Represents an update/
 * 
 * @author Kyli of Blue Husky Programming
 * @version 1.0.0
 * @since 2014-09-22
 */
public class Update
{
	private final Version newVersion;
	private final URI newPackage;
	private final Changelog changes;

	public Update(Version initNewVersion, URI initNewPackage)
	{
		newVersion = initNewVersion;
		newPackage = initNewPackage;
		changes = null;
	}

	public Update(Version initNewVersion, URI initNewPackage, Changelog initChanges)
	{
		newVersion = initNewVersion;
		newPackage = initNewPackage;
		changes = initChanges;
	}

	public Version getNewVersion()
	{
		return newVersion;
	}

	public URI getNewExecutable()
	{
		return newPackage;
	}

	public Changelog getChanges()
	{
		return changes;
	}

	@Override
	public String toString()
	{
		return "Update{" + "newVersion=" + newVersion + ", newPackage=" + newPackage + ", changes=" + changes + '}';
	}
}
