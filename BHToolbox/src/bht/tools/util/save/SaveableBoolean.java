package bht.tools.util.save;

/**
 * An implementation of {@link Saveable} that allows for easy saving of a boolean value
 * @author Supuhstar of Blue Husky Studios
 * @version 1.1.0
 */
public class SaveableBoolean extends AbstractSaveable<Boolean>
{
  public SaveableBoolean(boolean initState, CharSequence saveName)
  {
    super(initState, saveName);
  }

  //<editor-fold defaultstate="collapsed" desc="Saveable overrides">
  @Override
  public CharSequence getStringToSave()
  {
    return s ? "1" : "0";
  }

  @Override
  public Saveable loadFromSavedString(CharSequence savedString)
  {
    if (savedString == null)
      return this;
    return setState(savedString.charAt(0) == '1');
  }
  //</editor-fold>
}