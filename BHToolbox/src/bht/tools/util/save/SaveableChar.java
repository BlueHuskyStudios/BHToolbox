package bht.tools.util.save;

/**
 * An implementation of {@link Saveable} that allows for easy saving of a character
 * @author Supuhstar
 * @since Jan 09, 2012
 * @version 1.0.0
 */
public class SaveableChar extends AbstractSaveable<Character>
{
  public SaveableChar(char initState, CharSequence saveName)
  {
    super(initState, saveName);
  }

  //<editor-fold defaultstate="collapsed" desc="Saveable overrides">

  @Override
  public CharSequence getStringToSave()
  {
    return Character.toString(s);
  }

  @Override
  public Saveable loadFromSavedString(CharSequence savedString)
  {
    return setState(savedString.charAt(0));
  }
  //</editor-fold>
}