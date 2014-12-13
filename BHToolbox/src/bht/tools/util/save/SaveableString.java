package bht.tools.util.save;

import java.awt.image.BufferedImage;

/**
 * An implementation of {@link Saveable} that allows for easy saving of a character sequence
 * @author Supuhstar of Blue Husky Studios
 * @version 1.1.0
 * @since Jan 09, 2012
 */
public class SaveableString extends AbstractSaveable<CharSequence>
{
  public SaveableString(CharSequence initState, CharSequence saveName)
  {
    super(initState, saveName);
  }

  //<editor-fold defaultstate="collapsed" desc="Saveable overrides">

  @Override
  public CharSequence getStringToSave()
  {
    return String.valueOf(s);
  }

  @Override
  public Saveable loadFromSavedString(CharSequence savedString)
  {
    return setState(savedString);
  }
  //</editor-fold>
}