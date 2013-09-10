package bht.tools.util.save;

/**
 * CompressedSaveableLong, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012. License is CC-BY-SA.<hr/>
 * A version of {@link SaveableLong} that compresses the long into exactly four characters before saving it, and decompresses
 * it back into a long before loading it.
 * @author Supuhstar
 */
public class CompressedSaveableLong extends AbstractSaveable<Long>
{
  public CompressedSaveableLong(long initState, CharSequence initName)
  {
    super(initState, initName);
  }

  //<editor-fold defaultstate="collapsed" desc="Saveable overrides">
  @Override
  public CharSequence getStringToSave()
  {
    /*
     * Separate the 64-bit number into two 32-bit segments, because the char datatype is only 16 bits and combine these into a
     * character sequence and return that sequence
     */
    return (char)((s & 0xFFFF000000000000L) >> 0x30) + "" +
           (char)((s & 0x0000FFFF00000000L) >> 0x20) + "" +
           (char)((s & 0x00000000FFFF0000L) >> 0x10) + "" +
           (char) (s & 0x000000000000FFFFL);//no need to right-shift zero positions
  }

  /**
   * Decodes the given saved string into a long. Note that this only works if the given string is properly formatted (exactly
   * four 16-bit Unicode characters)
   * @param savedString the string to which the  long has been saved. Must be exactly four characters long. If it is any other
   * length or {@code null}, then {@code this} is immediately returned
   * @return {@code this}
   */
  @Override
  public CompressedSaveableLong loadFromSavedString(CharSequence savedString)
  {
    if (savedString == null || savedString.length() != 4)
      return this;
    
    s |= ((long)savedString.charAt(0)) << 0x30;
    s |= ((long)savedString.charAt(1)) << 0x20;
    s |= ((long)savedString.charAt(2)) << 0x10;
    s |= ((long)savedString.charAt(3));//no need to left-shift zero positions
    
    return this;
  }
  //</editor-fold>
  
  /**
   * This is only for testing purposes. It creates a new {@code CompressedSaveableLong} with a random state assigned to it and
   * prints that. It then creates a {@link CharSequence} from this {@code CompressedSaveableLong}'s {@link #getStringToSave()}
   * method and prints that. It then sets the original {@code CompressedSaveableLong}'s state to {@code 0} to ensure the next
   * step works. Next, it loads the aforementioned {@link CharSequence} into the {@code CompressedSaveableLong}'s {@link
   * #loadFromSavedString(CharSequence)} and prints the state one last time.
   * <h5>Example output:</h5>
   * {@code Encoding "4461515785206973440"...}<br/>
   * {@code Encoded form is "㷪糷䐀". Decoding...}<br/>
   * {@code Decoded as "4461515785206973440".}
   * @param args unused
   */
  public static void main(String args[])
  {
    CompressedSaveableLong rand = new CompressedSaveableLong((long)(Math.random() * Long.MAX_VALUE), "test");
    
    System.out.println("Encoding \"" + rand.getState() + "\"...");
    
    CharSequence encodedRand = rand.getStringToSave();
    
    System.out.println("Encoded form is \"" + encodedRand + "\". Decoding...");
    
    ((CompressedSaveableLong)rand.setState(0L)).loadFromSavedString(encodedRand).getState();
    
    System.out.println("Decoded as \"" + rand.getState() + "\".");
  }
}