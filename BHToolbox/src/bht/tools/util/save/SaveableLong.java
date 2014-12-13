package bht.tools.util.save;

/**
 * SaveableLong, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012. License is CC-BY-SA.<hr/>
 * Allows for the saving of a long data type as a named hexadecimal integer
 * @author Supuhstar
 */
public class SaveableLong extends AbstractSaveable<Long>
{
  public SaveableLong(long initState, CharSequence initName)
  {
    super(initState, initName);
  }

  //<editor-fold defaultstate="collapsed" desc="Saveable overrides">
  @Override
  public CharSequence getStringToSave()
  {
    return Long.toString(s, 16);
  }

  @Override
  public SaveableLong loadFromSavedString(CharSequence savedString)
  {
    if (savedString == null)
      return this;
    return (SaveableLong)setState(Long.valueOf(savedString.toString(), 16));
  }
  //</editor-fold>
}