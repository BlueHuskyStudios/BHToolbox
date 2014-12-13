package bht.tools.util.save;

import bht.test.tools.util.BHTML;
import bht.test.tools.util.BHTML.Tag;
import bht.tools.util.ArrayPP;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Supuhstar of Blue Husky Studios
 */
public class _Legacy_StateSaver3
{
  ArrayPP<Saveable> array;

  public _Legacy_StateSaver3()
  {
    array = new ArrayPP<Saveable>();
  }

  public _Legacy_StateSaver3 add(Saveable... comps)
  {
    array.add(comps);
    return this;
  }

  /**
   * 
   * @param dest
   * @return
   * @throws IOException If the file exists but is a directory rather than a regular file (and this method cannot make it into a
   * new file), does not exist but cannot be created, or cannot be opened for any other reason
   */
  public _Legacy_StateSaver3 saveStateTo(java.io.File dest) throws IOException
  {
    if (!dest.isDirectory())
      dest = new java.io.File(dest.getPath() + "/state-" + System.currentTimeMillis() + "." + BHTML.EXT);
    String saveStr = "";
    for (Saveable s : array)
      saveStr += s.getStringToSave() + "\n\r";
    java.io.FileWriter w = new java.io.FileWriter(dest);
    w.write(saveStr);
    w.close();
    return this;
  }
  /**
   * Reads in a <tt>BHTML</tt> file and uses its information to load a state into the components added to this <tt>StateSaver</tt>
   * @param dest the <tt>BHTML</tt> file from which the saved state will be read
   * @return <tt>this</tt>
   * @throws FileNotFoundException if <tt>dest</tt> is not found
   */
  public _Legacy_StateSaver3 loadStateFrom(java.io.File dest) throws FileNotFoundException
  {
    String cntnts = "";
    java.util.Scanner s = new java.util.Scanner(dest);  
    while (s.hasNextLine())
      cntnts += s.nextLine();
    
    return this;
  }
  public boolean componentIsSaved(BHTML.Tag compTag)
  {
    for (Saveable s : array)
      if (classMatchesTag(s, compTag))
        return true;
    return false;
  }

  private boolean classMatchesTag(Saveable s, Tag compTag)
  {
    throw new UnsupportedOperationException("Not yet implemented");
  }

}
