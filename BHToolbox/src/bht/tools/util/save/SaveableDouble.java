package bht.tools.util.save;

/**
 * An implementation of {@link Saveable} that allows for easy saving of a double-precision floating-point value
 * @author Supuhstar of Blue Husky Studios
 * @version 1.1.0
 */
public class SaveableDouble extends AbstractSaveable<Double>
{
  public SaveableDouble(double initState, CharSequence initName)
  {
    super(initState, initName);
  }

  //<editor-fold defaultstate="collapsed" desc="Saveable overrides">

  @Override
  public CharSequence getStringToSave()
  {
    return Double.toString(s);
  }

  @Override
  public Saveable loadFromSavedString(CharSequence savedString)
  {
    return setState(Double.valueOf(savedString.toString()));
  }
  //</editor-fold>
}