package bht.tools.util;

import bht.tools.misc.YesNoBox;
import bht.tools.util.math.Numbers;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple class that creates an logging file for your program. To use this, you must either create a {@code ProgLog}
 * object and call {@code objectName.logger}, call {@code new ProgLog().logger} each time (recommend only using for
 * logging one error), or create a new PrintStream object (e.g. {@code java.io.PrintStream log = new ProgLog().logger}).
 * <br /><br /><i><b>Note:</b> Formerly <tt>ErrorPrinter</tt>, this class does everything its predecessor did and more. Name was
 * changed to reflect the fact that this does much more, now, than print errors.
 * @author Blue Husky Programming
 * @version 1.4.0
 * @author Supuhstar
 * @since 1.6_20
 */
public class ProgLog
{
  //<editor-fold defaultstate="collapsed" desc="Objects">
  /** A {@code PrintStream} object which prints everything to a logging file in the package */
  private PrintStream ps;
  private FileOutputStream loggerFOS;
  private CharSequence title, version, fileName, ext;
  private File logFile;
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Initializers">
  /**
   * Creates a new {@code ProgLog} object and a new logging file in the package.
   * @param title a {@code String} containing the title of the program for which you are logging happenings. <br />Example:
   * {@code "Blue Husky's Program"}
   * @param version a {@code String} containing the version number of the program for which you are making this
   * logging file. <br />Example: {@code "1.2.10"}
   * @param fileName a {@code String} that represents the name of the logging file. <br />Example: {@code "Program log"}
   * @param extension a {@code String} representing the extension of the logging file. <br />Example: {@code "log"}
   */
  @SuppressWarnings("OverridableMethodCallInConstructor")
  public ProgLog(CharSequence title, CharSequence version, CharSequence fileName, CharSequence extension)
  {
    this.title = title;
    this.version = version;
    this.fileName = fileName;
    this.ext = extension.subSequence(extension.toString().indexOf('.') + 1, extension.length());
    initLogFile();
    Runtime.getRuntime().addShutdownHook(
            new Thread(new Runnable()
            {
              @Override
              public void run()
              {
                closeFile(0);
              }
            }));
  }
  
  /**
   * Creates a new {@code ProgLog} object and a new error-logging file in the package. The same as calling
   * {@code ProgLog(title, version, fileName, "log");}
   * @param title a {@code String} containing the title of the program for which you are logging happenings.
   * <br />Example: {@code "Blue Husky's Program"}
   * @param version a {@code String} containing the version number of the program for which you are making this logging
   * file. <br />Example: {@code "1.2.10"}
   * @param fileName a {@code String} that represents the name of the logging file. <br />Example: {@code "Program
   * log"}
   */
  public ProgLog(CharSequence title, CharSequence version, CharSequence fileName)
  {
    this(title, version, fileName, "log");
  }
  
  /**
   * Creates a new {@code ProgLog} object and a new logging file in the package. The same as calling
   * {@code ProgLog(title, version, title + "-log(" + System.currentTimeMillis() + ")");}
   * @param title a {@code String} containing the title of the program for which you are logging happenings.
   * <br />Example: {@code "Blue Husky's Program"}
   * @param version a {@code String} containing the version number of the program for which you are making this logging
   * file. <br />Example: {@code "1.2.10"}
   */
  public ProgLog(CharSequence title, CharSequence version)
  {
    this(title, version, title + "-log");
  }
  
  /**
   * Creates a new {@code ProgLog} object and a new logging file in the package. The same as calling
   * {@code ProgLog("a program", "unknown");}
   */
  public ProgLog()
  {
    this("App", "unknown");
  }
  //</editor-fold>
  
  /**
   * Creates a log file and fills in information about the user's computer. This is automatically called upon creation of a
   * ProgLog object.
   */
  public void initLogFile()
  {
    logFile = new File((new StringPP(System.getProperty("os.name")).containsIgnoreCase("windows") ?
      System.getProperty("user.home") + "\\AppData\\Roaming\\Blue Husky\\" + /*new StringPP(*/title/*).replace(' ', '_', true)*/ :
      System.getProperty("java.io.tmpdir")) + "\\" + fileName + "." + ext);
    try
    {
      logFile.setReadable(true);
      logFile.setWritable(true);
      
      boolean b = true;
      if (!logFile.getParentFile().exists())
        b = logFile.getParentFile().mkdirs();
      if (!b)
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Could not create directory: {0}", logFile.getParent());/*
      if (!logFile.exists())
      {
        b = logFile.createNewFile();
        if (!b)
          Logger.getLogger(getClass().getName()).log(Level.INFO, "Could not create file: {0}", logFile.getParent());
      }*///Unnecessary due to thefollowing line
//      logger = new java.io.PrintStream(logFile);
      loggerFOS = new FileOutputStream(logFile, true);
      ps = new java.io.PrintStream(loggerFOS, true);
    }
    catch (IOException ex)
    {
      Logger.getLogger(getClass().getName()).log(Level.WARNING, null, ex);
      FileWriter writeToFile;
      try
      {
        writeToFile = new FileWriter(logFile);
        writeToFile.write("");
        writeToFile.close();
      }
      catch (IOException ex1)
      {
        Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex1);
        javax.swing.JOptionPane.showMessageDialog(null, "Could not create the error-logging file. This normally happens when \n"
                + "the program does not have permission to write to the directory. Please ensure that you have given Java \n"
                + "permission to write to \n\"" + logFile + "\".",
                "Error when making program log - " + title, YesNoBox.ERROR_MES);
        System.exit(1);
      }
      try
      {
//        logger = new java.io.PrintStream(logFile);
        loggerFOS = new FileOutputStream(logFile, true);
      }
      catch (FileNotFoundException ex1)
      {
        Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex1);
        javax.swing.JOptionPane.showMessageDialog(null, "Could not access the recently error logging file \"" +
                logFile.getName() + "\".\nDid you remove the drive or something?", "Error when making program log - " + title,
                YesNoBox.ERROR_MES);
        System.exit(1);
      }
    }

    writeLine("«««««This is the error-reporting file for " + title + " (version " + version + ")»»»»»");
    writeLine();
    writeLine();
    writeLine("              time of file init: " + java.util.Calendar.getInstance().getTime());
    writeLine("               program run from: " + System.getProperty("user.dir"));
    writeLine("program used to run the program: " + System.getProperty("java.class.path"));
    writeLine("                        OS name: " + System.getProperty("os.name") + " (version " + System.getProperty("os.version") + ")");
    writeLine("                OS Architecture: " + System.getProperty("os.arch"));
    writeLine("           Available Processors: " + Runtime.getRuntime().availableProcessors());
    writeLine("                Starting Memory: " + Numbers.toPrettyBytes(Runtime.getRuntime().freeMemory()) + " Free / " +
                                                    Numbers.toPrettyBytes(Runtime.getRuntime().totalMemory()) + " Total / " +
                                                    Numbers.toPrettyBytes(Runtime.getRuntime().maxMemory()) + " Max");//added specifying text Feb 28, 2012 (1.3.1) for Marian
    writeLine("  Java Virtural Machine version: " + System.getProperty("java.vm.version"));
    writeLine("           Java runtime version: " + System.getProperty("java.runtime.version"));
    writeLine("                        Country: " + System.getProperty("user.country"));
    writeLine("                      Time Zone: " + System.getProperty("user.timezone"));
    writeLine("                       Language: " + System.getProperty("user.language"));
    writeLine("                       Username: " + System.getProperty("user.name"));
    for (int i=0; i < 0x8F; i++)
      write("-");
    writeLine();
  }
  
  //<editor-fold defaultstate="collapsed" desc="Settings">
  /**
   * Sets the title of the program, shown in error dialogs and the log file.
   * @param title a {@code String} containing the title of the program for which you are logging happenings.
   * <br />Example: {@code "Blue Husky's Program"}
   */
  public void setTitle(String title)
  {
    this.title = title;
  }
  
  /**
   * Sets the version of the program, shown in error dialogs and the log file.
   * @param version a {@code String} containing the version number of the program for which you are making this logging
   * file. <br />Example: {@code "1.2.10"}
   */
  public void setVersion(String version)
  {
    this.version = version;
  }
  
  /**
   * Sets the extension of the log file. Default is {@code .log}
   * @param extension a {@code String} representing the extension of the logging file. <br />Example: {@code .log}
   */
  public void setExtension(String extension)
  {
    ext = extension;
  }
  
  /**
   * Sets the name of the log file. Default is the title of the program followed by {@code " log"}
   * @param fileName a {@code String} representing the name of the logging file. <br />Example: {@code "Program log"}
   */
  public void setFileName(String fileName)
  {
    this.fileName = fileName;
  }
  
  /**
   * Returns the title of the program, shown in error dialogs and the log file.
   * @return a {@code String} containing the title of the program for which you are logging happenings.
   * <br />Example: {@code "Blue Husky's Program"}
   */
  public CharSequence getTitle()
  {
    return title;
  }
  
  /**
   * Returns the version of the program, shown in error dialogs and the log file.
   * @return a {@code String} containing the version number of the program for which you are making this log file.
   * <br />Example: {@code "1.2.10"}
   */
  public CharSequence getVersion()
  {
    return version;
  }
  
  /**
   * Returns the extension of the log file.
   * @return a {@code String} representing the extension of the log file. <br />Example: {@code ".log"}
   */
  public CharSequence getExtension()
  {
    return ext;
  }
  
  /**
   * Returns the name of the log file.
   * @return a {@code String} representing the name of the log file. <br />Example: {@code "Program log"}
   */
  public CharSequence getFileName()
  {
    return fileName;
  }
  
  /**
   * Returns the name of the log file.
   * @return a {@code String} representing the name of the log file. <br />Example: {@code "Program log"}
   */
  public java.io.File getFile()
  {
    return logFile;
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="logThrowable methods">
  /**
   * Logs a throwable in the selected {@code java.io.PrintStream}
   * @param t the {@code Throwable} that represents the error, exception, etc. that happened
   * @param method The {@code String} containing the name of the method in which this is called. Leaving this empty will
   * simply leave less information to be reviewed in the file
   */
  @SuppressWarnings("UseOfSystemOutOrSystemErr")
  public void logThrowable(Throwable t, String method)
  {
    writeLine();
    writeLine("<<<<< " + t + (method == null || method.isEmpty() ? "" : " found in " + method) + " on " +
              java.util.Calendar.getInstance().getTime());
    //    t.printStackTrace(stream);
    StackTraceElement ste[] = t.getStackTrace();
    for (int i=0; i < ste.length; i++)
      writeLine(ste[i].toString());
    writeLine();
    writeLine(">>>>> end log of " + t);
    System.err.println(t + " logged in error file");
  }
  
  /**
   * Logs a throwable in the selected {@code java.io.PrintStream}
   * @param t the {@code Throwable} that represents the error, exception, etc. that happened
   * <strong>Please note that this is meant to be the {@code logger} object. Please utilize that in here.</strong>
   */
  public void logThrowable(Throwable t)
  {
    logThrowable(t, t.getStackTrace()[0].getMethodName());
  }
  //</editor-fold>


  //<editor-fold defaultstate="collapsed" desc="logAction">
  /**
   * Logs an event in the logging file
   * @param evt The event to be logged (such as an <tt>java.awt.event.ActionEvent</tt>)
   * @param info information about the event <br /><b>Example:</b> <tt>"Button 2 pressed"</tt>
   * @param method the method used to invoke the event
   * <br /><b>Example:</b> <tt>"jButtonActionPerformed(java.awt.event.ActionEvent evt)"</tt>
   */
  public void logAction(java.util.EventObject evt, String info, String method)
  {
    writeLine();
    writeLine("<@>" + (info != null && !info.isEmpty() ? info : "") + (method != null && !method.isEmpty() ?
                                                                       (info != null && !info.isEmpty() ? " inside " + method : method + " invoked ") : "") + " => " + (evt != null ?
                                                                                                       evt.getClass().getName() + " caused by " + evt.getSource() + ". Other info: " + evt : " NO GIVEN EVENT!"));
  }
  
  /**
   * Logs an event in the logging file<br />
   * <i>Same as calling <tt>logAction(evt, null, null);</tt></i>
   * @param evt The event to be logged (such as an <tt>java.awt.event.ActionEvent</tt>)
   */
  public void logAction(java.util.EventObject evt)
  {
    logAction(evt, null, null);
  }
  
  /**
   * Logs an event in the logging file<br />
   * <i>Same as calling <tt>logAction(null, info, method);</tt></i>
   * @param info information about the event <br /><b>Example:</b> <tt>"Button 2 pressed"</tt>
   * @param method the method used to invoke the event
   * <br /><b>Example:</b> <tt>"button2ActionPerformed(java.awt.event.ActionEvent evt)</tt>
   */
  public void logAction(String info, String method)
  {
    logAction(null, info, method);
  }
  
  /**
   * Logs an event in the logging file<br />
   * <i>Same as calling <tt>logAction(evt, info, null);</tt></i>
   * @param evt The event to be logged (such as an <tt>java.awt.event.ActionEvent</tt>)
   * @param info information about the event <br /><b>Example:</b> <tt>"Button 2 pressed"</tt>
   */
  public void logAction(java.util.EventObject evt, String info)
  {
    logAction(evt, info, null);
  }
  
  /**
   * Logs an event in the logging file<br />
   * <i>Same as calling <tt>logAction(null, info, null);</tt></i>
   * @param info information about the event <br /><b>Example:</b> <tt>"Button 2 pressed"</tt>
   */
  public void logAction(String info)
  {
    logAction(null, info, null);
  }
  
  /**
   * <i>Same as calling <tt>logAction(null, null, null);</tt></i><br />
   * Results in the following being logged:
   * <blockquote><tt>&lt;@&gt; =&gt; NO GIVEN EVENT!</tt></blockquote>
   * @deprecated PLEASE USE ANY <tt>logAction</tt> METHOD BUT THIS
   */
  public void logAction()
  {
    logAction(null, null, null);
  }
  //</editor-fold>
  
  /*public FileOutputStream getLogger()
  {
    return loggerFOS;
  }*/

  //<editor-fold defaultstate="collapsed" desc="Interaction with Writer">
  /**
   * Finishes and closes {@code logger} and sets the file it wrote to to no longer be writable
   * @param status the status of the closing of the file and/or program.<br />
   * By convention, a nonzero status code indicates abnormal termination.
   */
  @SuppressWarnings("UseOfSystemOutOrSystemErr")
  public void closeFile(int status)
  {
    writeLine();
    writeLine();
    write("<<<<<Program exited ");
    if (status == 0)
      write("properly");
    else
      write("with error code " + status);
    writeLine(" on " + java.util.Calendar.getInstance().getTime() + ">>>>>");
    writeLine("Ending Memory: " + Numbers.toPrettyBytes(Runtime.getRuntime().freeMemory()) + " Free / " +
              Numbers.toPrettyBytes(Runtime.getRuntime().totalMemory()) + " Total / " +
              Numbers.toPrettyBytes(Runtime.getRuntime().maxMemory()) + " Max");
    
    writeLine();writeLine();writeLine();writeLine();

    byte good = 0xf;

    try
    {
      if (!logFile.setWritable(false))
        good ^= 0b0001;//These were changed from "good = (byte)(good ^ 0bXXXX)" to "good ^= 0bXXXX" on Feb 28, 2012 (1.3.1) for Marian
    }
    catch (Throwable t)
    {
      good ^= 0b0001;
    }

    try
    {
      if (!logFile.setExecutable(false))
        good ^= 0b0010;
    }
    catch (Throwable t)
    {
      good ^= 0b0010;
    }

    try
    {
      if (!logFile.setReadOnly())
        good ^= 0b0100;
    }
    catch (Throwable t)
    {
      good ^= 0b0100;
    }

    try
    {
      if (!logFile.setReadable(true))
        good ^= 0b1000;
    }
    catch (Throwable t)
    {
      good ^= 0b1000;
    }

    if (good != 0b1111)//changed from 0xf Feb 28, 2012 (1.3.1) for Marian
    {
      System.err.println("Error file improperly closed: (error code " + Integer.toHexString(good) + ")");
      writeLine("NOTE: Error file improperly closed: (error code " + Integer.toHexString(good) + ")");
    }
    try
    {
      loggerFOS.close();
    }
    catch (IOException ex)
    {
      System.err.println("Could not close output stream:");
      ex.printStackTrace();
    }
  }

  public void wipeLog()
  {
    closeFile(0);
    logFile.delete();
  }
  
  private void write(CharSequence cs)
  {/*
   * try
   * {
   * for (int i=0; i < cs.length(); i++)
   * stream.write(cs.charAt(i));
   * }
   * catch(IOException ex)
   * {
   * System.err.println("Could not write to file:");
   * ex.printStackTrace();
   * }*/
    ps.print(cs);
  }
  
  private void writeLine(CharSequence cs)
  {
    ps.println(cs);
  }
  
  private void writeLine()
  {
    ps.println();
  }
  
  public java.io.PrintStream getPrintStream()
  {
    return ps;
  }
  
  public FileOutputStream getOutputStream()
  {
    return loggerFOS;
  }
  //</editor-fold>
  
  public enum ProgLoggingLevel
  {
    SEVERE, HIGH, MODERATE, LOW, NO_CONCERN
  }
}
