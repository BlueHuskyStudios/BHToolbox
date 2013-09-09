package bht.tools.util.save;

import bht.tools.util.ArrayPP;
import bht.tools.util.StringPP;
import java.io.File;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Set;

/**
 * A class that manages saving and loading of states to any object which extends {@link Saveable}. This class is {@code final}
 * only because extending this fairly basic class leaves too many potentially large and powerful objects untouchable by the child class.
 * @author Supuhstar of Blue Husky Programming
 * @see Saveable
 * @since 1.6_29
 * @version 1.1.2
 */
public final class StateSaver
{
  //<editor-fold defaultstate="collapsed" desc="Objects">
  private CharSequence title;
  private ArrayPP<Saveable> array;
  private SaveableBoolean autoSL;
  private ArrayPP<SaveListener> saveListeners;
  public static final String AUTO_SL_SAVE_NAME = "autoSL";
  private java.io.File saveFile;
  private java.util.Properties p = new java.util.Properties();
  //</editor-fold>
  
  /**
   * Creates a new {@code StateSaver} with the given title, using the default save file and and auto-save-load enabled.
   * <strong>Equivalent of {@code new StateSaver(progTitle, "save")}</strong>
   * @param progTitle The title of the program for which this {@code StateSaver} is saving
   */
  public StateSaver(CharSequence progTitle)
  {
    this(progTitle, "save");
  }
  
  /**
   * Creates a new {@code StateSaver} with the given title and auto-save-load preference, using the default save file.
   * <strong>Equivalent of {@code new StateSaver(progTitle, "save", autoSaveLoad}</strong>
   * @param progTitle The title of the program for which this {@code StateSaver} is saving
   * @param autoSaveLoad If {@code true}, the program will load the state upon starting and adding a saveable, and save upon
   * program exit.
   * @param useXML if {@code true}, the save file will be saved as an {@code .xml} file rather than a {@code .properties} file 
   */
  public StateSaver(CharSequence progTitle, boolean autoSaveLoad, boolean useXML)
  {
    this(progTitle, "save", autoSaveLoad, useXML);
  }
  
  /**
   * Creates a new {@code StateSaver} with the given title and auto-save-load preference, using the default save file.
   * <strong>Equivalent of {@code new StateSaver(progTitle, "save", autoSaveLoad}</strong>
   * @param progTitle The title of the program for which this {@code StateSaver} is saving
   * @param autoSaveLoad If {@code true}, the program will load the state upon starting and adding a saveable, and save upon
   * program exit.
   */
  public StateSaver(CharSequence progTitle, boolean autoSaveLoad)
  {
    this(progTitle, autoSaveLoad, false);
  }
  
  /**
   * Creates a new {@code StateSaver} with the given title and save file, with auto-save-load enabled.
   * <strong>Equivalent of {@code new StateSaver(progTitle, saveFileName, true}</strong>
   * @param progTitle The title of the program for which this {@code StateSaver} is saving
   * @param saveFileName The name of the file to which the state will be saved
   */
  public StateSaver(CharSequence progTitle, CharSequence saveFileName)
  {
    this(progTitle, saveFileName, true);
  }
  
  /**
   * Creates a new {@code StateSaver} with the given title and save file and auto-save-load preference.
   * To make the save file, this first detects whether the user is running Windows. If so, it uses the AppData directory. Else,
   * it uses the default temp directory. Then, it creates a file in that directory plus "\Blue Husky\{@code progTitle}\
   * {@code saveFileName}.xml"
   * @param progTitle The title of the program for which this {@code StateSaver} is saving
   * @param saveFileName The name of the file to which the state will be saved
   * @param autoSaveLoad If {@code true}, the program will load the state upon starting and adding a saveable, and save upon
   * program exit.
   */
  public StateSaver(CharSequence progTitle, CharSequence saveFileName, boolean autoSaveLoad)
  {
    this(progTitle, saveFileName, autoSaveLoad, false);
  }
  
  /**
   * Creates a new {@code StateSaver} with the given title and save file and auto-save-load preference.
   * To make the save file, this first detects whether the user is running Windows. If so, it uses the AppData directory. Else,
   * it uses the default temp directory. Then, it creates a file in that directory plus "\Blue Husky\{@code progTitle}\
   * {@code saveFileName}.xml"
   * @param progTitle The title of the program for which this {@code StateSaver} is saving
   * @param saveFileName The name of the file to which the state will be saved
   * @param autoSaveLoad If {@code true}, the program will load the state upon starting and adding a saveable, and save upon
   * program exit.
   * @param useXML if {@code true}, the save file will be saved as an {@code .xml} file rather than a {@code .properties} file 
   */
  public StateSaver(CharSequence progTitle, CharSequence saveFileName, boolean autoSaveLoad, boolean useXML)
  {
    this(progTitle, new java.io.File((new StringPP(System.getProperty("os.name")).containsIgnoreCase("windows") ?
                                      System.getenv("APPDATA") :
                                      System.getProperty("java.io.tmpdir")) + "\\Blue Husky\\" + progTitle + "\\" + saveFileName
                                          + "." + (useXML ? "xml" : "properties")), autoSaveLoad);
  }
  
  /**
   * Creates a new {@code StateSaver} with the given title and save file, with auto-save-load enabled.
   * <strong>Equivalent of {@code new StateSaver(progTitle, saveFile, true}</strong>
   * @param progTitle The title of the program for which this {@code StateSaver} is saving
   * @param saveFile The {@code java.io.File} to which the state will be saved
   */
  public StateSaver(CharSequence progTitle, java.io.File saveFile)
  {
    this(progTitle, saveFile, true);
  }
  
  /**
   * This is the full constructor. Creates a new {@code StateSaver} with the given {@code progTitle}, {@code saveFile}, and
   * {@code autoSaveLoad}
   * @param progTitle The title of the program for which this {@code StateSaver} is saving
   * @param saveFile The {@code java.io.File} to which the state will be saved
   * @param autoSaveLoad If {@code true}, the program will load the state upon starting and adding a saveable, and save upon
   * program exit.
   */
  public StateSaver(CharSequence progTitle, java.io.File saveFile, boolean autoSaveLoad)
  {
    title = progTitle;
    this.saveFile = saveFile;
    array = new ArrayPP<>();
    this.autoSL = new SaveableBoolean(autoSaveLoad, AUTO_SL_SAVE_NAME);
    
    saveListeners = new ArrayPP<>();
    
    Runtime.getRuntime().addShutdownHook(
    new Thread(new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          if (shouldAutoSaveLoad())
            saveState();
        }
        catch (java.io.FileNotFoundException ex)
        {
          ex.printStackTrace();
        }
        catch (java.io.IOException ex)
        {
          ex.printStackTrace();
        }
      }
    }));
    
    try
    {
      if (autoSaveLoad && !saveFile.exists())
      {
        saveFile.getParentFile().mkdirs();
        saveFile.createNewFile();
      }
      addSaveable(autoSL);
    }
    catch (Throwable t)
    {
      new IOException("Error when trying to add and load the saveable for the auto-save-load feature", t).printStackTrace();
    }
    
    try
    {
      if (autoSaveLoad)
        loadState();
    }
    catch (Throwable t)
    {
      new IOException("Error when trying to automatically load the state", t).printStackTrace();
    }
  }
  
  /**
   * Adds the {@link Saveable} {@code s} to the array of things to save and load. If auto-save-load is enabled, the state of this
   * {@code Saveable} is immediately loaded.
   * @param s the {@code Saveable} to be added to the array of {@code Saveable}s
   * @return {@code this}
   * @throws IOException If autosave fails. Will not be thrown if auto-save-load is disabled.
   */
  public StateSaver addSaveable(Saveable s) throws IOException
  {
    array.addWithoutDuplicates(s);
    p.setProperty(s.getSaveName().toString(), s.getStringToSave().toString());
    if (shouldAutoSaveLoad())
    {
      try
      {
        loadState();
        if (p.containsKey(s.getSaveName()))
          putState(s);
      }
      catch (Throwable t)
      {
        System.err.println("State could not be auto-loaded due to a " + t.getClass().getName() + ".");
      }
    }
    return this;
  }
  
  /**
   * If the given {@link Saveable} has already been added, it is removed
   * @param s the {@code Saveable} to be searched for and, if found, subsequently removed
   * @return {@code this}
   */
  public StateSaver removeSaveable(Saveable s)
  {
    array.remove(s, true);
    p.remove(s.getSaveName());
    return this;
  }
  
  public void saveState() throws java.io.IOException
  {
    SaveEvent evt = new SaveEvent(this);
    for (SaveListener sl : saveListeners)
      sl.stateSaving(evt);
    
    for (Saveable s : array)
      p.setProperty(s.getSaveName().toString(), s.getStringToSave().toString());
    boolean b = !saveFile.getParentFile().exists() ? saveFile.getParentFile().mkdirs() : true;
    if (!b)
    {
//      Logger.getLogger(getClass().getName()).log(Level.INFO, "Could not create directory: {0}", saveFile.getParent());
    }
    b = saveFile.createNewFile();
    if (!b)
    {
//      Logger.getLogger(getClass().getName()).log(Level.INFO, "Could not create new file: {0}", saveFile.getParent());
    }
    if (saveFile.getName().endsWith(".xml"))
      p.storeToXML(new java.io.FileOutputStream(saveFile), "Savestate for " + title + ", made on " + new java.util.Date().toString(), "UTF-16");
    else
      p.store(new java.io.FileOutputStream(saveFile), "Savestate for " + title + ", made on " + new java.util.Date().toString());
    
    for (SaveListener sl : saveListeners)
      sl.stateSaved(evt);
  }

  public void loadState() throws IOException
  {
    try
    {
      if (saveFile.getName().endsWith(".xml"))
        p.loadFromXML(new java.io.FileInputStream(saveFile));
      else
        p.load(new java.io.FileInputStream(saveFile));
    }
    catch (InvalidPropertiesFormatException ex)
    {
      saveState();
      if (saveFile.getName().endsWith(".xml"))
        p.loadFromXML(new java.io.FileInputStream(saveFile));
      else
        p.load(new java.io.FileInputStream(saveFile));
    }
    catch (IOException ex)
    {
      System.err.println("Save or file path cannot be found. Attempting to create a new one and save to that...");
      saveFile.getParentFile().mkdirs();
      saveFile.createNewFile();
      if (saveFile.getName().endsWith(".xml"))
        p.loadFromXML(new java.io.FileInputStream(saveFile));
      else
        p.load(new java.io.FileInputStream(saveFile));
    }
  }
  
  /**
   * Reads the loaded state that correlates to the given {@link Saveable}'s {@link Saveable#getSaveName()} and sets that as the
   * given {@code Saveable}'s new state via its {@link Saveable#loadFromSavedString(java.lang.CharSequence)}
   * @param s the {@link Saveable} to be changed, if its state has already been loaded
   * @return {@code this}
   * @throws IllegalArgumentException If the given Saveable cannot be found
   */
  public StateSaver putState(Saveable s)
  {
    String prop = p.getProperty(s.getSaveName().toString());
    if (prop == null)
      throw new java.lang.IllegalArgumentException("Could not find Saveable's state: " + s);
    s.loadFromSavedString(prop);
    return this;
  }
  
  /**
   * Attempts to put the states of all registered {@link Saveable}s. If any one can't be put for any reason, it is skipped and a
   * short {@link System#err} output is printed
   * @return {@code this}
   */
  public StateSaver putAllStates()
  {
    for (int i=0, l = array.length(); i < l; i++)
      putState(array.get(i));
    return this;
  }

  /**
   * If there is a saved object with the given key, the value of that object is returned
   * @param key The name of the object whose value is to be fetched
   * @return The value of the object with the given name, or {@code null} if none is found
   */
  public CharSequence getState(CharSequence key)
  {
    return p.getProperty(String.valueOf(key));
  }
  
  public StateSaver setState(Saveable s)
  {
    p.setProperty(s.getSaveName().toString(), s.getStringToSave().toString());
    return this;
  }
  
  public StateSaver setAllStates()//Adde Feb 25, 2012 (1.1.2) for Marian
  {
    for (Saveable s : array)
    {
      p.setProperty(s.getSaveName().toString(), s.getStringToSave().toString());
    }
    return this;
  }
  
  public boolean shouldAutoSaveLoad()
  {
    return autoSL.getState();
  }
  
  public StateSaver setShouldAutoSaveLoad(boolean shouldAutoSaveLoad)
  {
    autoSL.setState(shouldAutoSaveLoad);
    return this;
  }
  
  public String[] getAllSaveablesNames()
  {
    String[] names = p.stringPropertyNames().toArray(new String[0]);
    String[] ret = new String[Math.max(0, names.length - 1)];
    
    boolean b = false;
    for (int i=0, l=names.length; i < l; i++)
      if (names[i].equals(AUTO_SL_SAVE_NAME))//We don't want the end user to be able to access the autosave save-state directly
        b = true;
      else
        ret[b ? i - 1 : i] = names[i].toString();
    
    return ret;
  }
  
  public StateSaver addSaveListener(SaveListener sl)
  {
    saveListeners.add(sl);
    return this;
  }
  
  public StateSaver removeSaveListener(SaveListener sl)
  {
    saveListeners.remove(sl, true);
    return this;
  }
  
  /**
   * Makes this class use the given {@link File} to save the state
   * @param newSaveFile the new save file
   * @return the resulting {@code this}
   */
  public StateSaver setSaveFile(File newSaveFile)
  {
    saveFile = newSaveFile;
    return this;
  }
  
  /**
   * Returns the {@link File} that this class will use to save the state
   * @return the {@link File} that this class will use to save the state
   */
  public File getSaveFile()
  {
    return saveFile;
  }
  
  public int getNumSaveables()
  {
    return array.length() - 1;//" - 1" added Feb 25, 2012 (1.1.2) for Marian, because we don't wnat the user perceiving autoSL
  }
  
  public Saveable[] getAllSaveables()
  {
    Saveable[] ret = new Saveable[array.length() - 1];
    for (int i=0, j=0, l = ret.length; j < l; i++, j++)
    {
      ret[j] = array.get(i);
      if (ret[j].equals(autoSL))
        j--;
    }
    return ret;
  }
}