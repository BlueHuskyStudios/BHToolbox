package bht.tools.comps;

import bht.resources.Icons;
import bht.tools.Constants;
import bht.tools.fx.Colors;
import bht.tools.fx.CompAction;
import bht.tools.fx.LookAndFeelChanger;
import bht.tools.fx.LookAndFeelChanger.LAFChangeEvent;
import bht.tools.fx.LookAndFeelChanger.LAFChangeListener;
import bht.tools.misc.YesNoBox;
import bht.tools.util.ArrayPP;
import bht.tools.util.Copier;
import bht.tools.util.math.Numbers;
import bht.tools.util.ProgLog;
import bht.tools.util.Searchable;
import bht.tools.util.StringPP;
import bht.tools.util.save.Saveable;
import bht.tools.util.save.SaveableBoolean;
import bht.tools.util.save.SaveableDouble;
import bht.tools.util.save.SaveableLong;
import bht.tools.util.save.SaveableString;
import bht.tools.util.save.StateSaver;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 * This frame comes with a {@link JMenuBar} loaded with {@link JMenus} titled "File", "Actions", "Edit", "View", "Tools", and
 * "Help" and a {@link BHMessagePanel} at the bottom. In the "Actions" menu is a dynamic "Exit" {@link JMenuItem} that always
 * stays at the bottom of the menu. In the "Edit" menu, wherein there is a "Copy" {@link JMenuItem}, which must first be told
 * what to clone (There is a method provided for this) using the {@link Copier} class. In the "View" menu, there are two more
 * menus, "Visual Style" and "Look and Feel". Within these two, there are menu items that let you change the color scheme of the
 * window, using the methods from the {@link Colors} class to set the styles and from the {@link LookAndFeelChanger} class to
 * change the Look-And-Feel. The "Help" menu has an "About" item that, much like the "Copy" item, must be told what to display.
 * <br/><br/>
 * On the bottom of the frame, there is a {@link BHMessagePanel} that defaults to saying "Welcome!" and not displaying an icon.
 * This can be changed via the frame's alertOf methods before showing the frame.
 * <br/><br/>
 * This frame also comes preloaded with several state managers, including an automatic state saver, a settings dialog, an online
 * updater (only works with officially registered Blue Husky Applications), and {@link CompAction} and {@link Colors} objects
 * <h1>WARNING:</h1><!--
 * <h2>Adding</h2>-->
 * To add components to this, you <b>MUST NOT</b> set the layout to be a {@link java.awt.BorderLayout}. If you do, the components
 * added will not be shown!
 * @author Supuhstar
 * @since Mar 10, 2011
 * @version 1.2.15
 */
@SuppressWarnings({"StaticNonFinalUsedInInitialization", "UseOfSystemOutOrSystemErr"})
public class BHFrame extends javax.swing.JFrame implements Saveable, Searchable, BHComponent
{
  private static final long serialVersionUID = 1L;
  private java.awt.event.ActionListener copyListener, updDLActionListener, updRestartActionListener, updManualActionListener;
  private ArrayPP<String> fontAPP;
  private ArrayPP<java.awt.event.ActionListener> exitListeners;
  private static boolean starting = true, fontDialogHasBeenLoaded = false;
  private boolean fixingExit = false, shouldExit = true, prints = false, autoCopy = true;
  protected boolean useTransparency = true;
  private BHCustColorsEditPanel custColorsEditPanel;
  private BHMessagePanel bhmp = new BHMessagePanel("Welcome!", BHMessagePanel.PLAIN_MES);
  public static final byte ACTIONS_MENU = 0, FILE_MENU = 1, EDIT_MENU = 2, VIEW_MENU = 3, TOOLS_MENU = 4, HELP_MENU = 5;
  private CharSequence saveName = "Base BHFrame", searchTitle;
  private static CharSequence TITLE, SHORT_TITLE, VERSION;
  public static final CharSequence TRUE_STR = "1", FALSE_STR="0", SETTING_ANIM="useAnims";
  private Colors mC = new Colors(Colors.MOCHA, Colors.BACK), testColors = mC.clone();
  private CompAction mCA = new CompAction();
//  private double lastSelectedFontMod = Colors.DEFAULT_SIZE;
//  private int lastSelectedFontStyle = java.awt.Font.PLAIN;
//  javax.swing.LookAndFeel
//          metal = new javax.swing.plaf.metal.MetalLookAndFeel(),
//          nimbus = new com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel(),
//          motif = new com.sun.java.swing.plaf.motif.MotifLookAndFeel(),
//          win = new com.sun.java.swing.plaf.windows.WindowsLookAndFeel(),
//          winClass = new com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel();
  private ProgLog log;
  private java.util.Properties settings;
  private static SaveableBoolean saveableUseAnims, saveablePlusDefaultFontSize;
  private static SaveableDouble saveableFontMod;
  private static SaveableLong saveableFontStyle;
  private static SaveableString saveableFontName;
  private static SaveableString saveableLAFSelection;
  private static StateSaver settingsSaver;
  private Thread fontSearchThread;
  public java.net.URL latestVerURL, latestUpdURL;

  static
  {
    System.out.println("BHFrame#static:  Creatng state saver...");
    try
    {
      settingsSaver = new StateSaver("Universal Settings");
      settingsSaver.addSaveable(saveableUseAnims = new SaveableBoolean(true, SETTING_ANIM));
      settingsSaver.addSaveable(saveableFontMod = new SaveableDouble(Colors.DEFAULT_SIZE, "fontMod"));
      settingsSaver.addSaveable(saveableFontName = new SaveableString(Colors.DEFAULT_FONT, "fontName"));
      settingsSaver.addSaveable(saveableFontStyle = new SaveableLong(java.awt.Font.PLAIN, "fontStyle"));
      settingsSaver.addSaveable(saveablePlusDefaultFontSize = new SaveableBoolean(false, "plusDefaultFontSize"));
      settingsSaver.addSaveable(saveableLAFSelection = new SaveableString(LookAndFeelChanger.getLookAndFeel().getClass().getName(), "lafSelection"));
    }
    catch (Throwable t)
    {
      t.printStackTrace();
    }
    System.out.println("BHFrame#static:  Loading saved settings...");
    try
    {
      settingsSaver.loadState();
      settingsSaver.putAllStates();
    }
    catch (Throwable t)
    {
      System.out.println("BHFrame#static:  ... nevermind. Turns out I couldn't load the settings.");
      t.printStackTrace();
    }
    
    System.out.println("BHFrame#static:  Initializing LAF Menu...");
    initLAFMenu();
    
    LookAndFeelChanger.addLAFChangeListener(new LAFChangeListener()
    {
      @Override
      public void lafChanged(LAFChangeEvent e)
      {
        for (JRadioButtonMenuItem jrbmi : lafRadioButtonMenuItems)
        {
          if (jrbmi.getText().startsWith(e.getNewLookAndFeel().getName()))
          {
            jrbmi.setSelected(true);
            return;
          }
        }
      }
    });
  }
  
  public BHFrame()
  {
    this("Generic Blue Husky Application", "Unknown");
  }
  
  public BHFrame(String title, String version)
  {
    this(title, version, Colors.MOCHA, LookAndFeelChanger.DEFAULT, false);
  }
  
  /**
   * Creates a new BHFrame
   * @param title the title of the application (e.g. {@code "Blue Husky Application"})
   * @param version the version of the application (e.g. {@code "1.0.0"})
   * @param initStyle the initial color style of the frame. See {@link bht.tools.fx.Colors}
   * @param initLAF the initial Look-And-Feel of the frame. See {@link bht.tools.fx.LookAndFeelChanger}
   * @param force whether to force the look and feel application. See {@link bht.tools.fx.LookAndFeelChanger#setLookAndFeel(byte, boolean)}
   * @see bht.tools.fx.Colors
   * @see bht.tools.fx.LookAndFeelChanger
   * @see bht.tools.fx.LookAndFeelChanger#setLookAndFeel(byte, boolean)
   */
  @SuppressWarnings({"UseOfSystemOutOrSystemErr", "OverridableMethodCallInConstructor"})
  public BHFrame(CharSequence title, CharSequence version, byte initStyle, byte initLAF, boolean force)
  {
    this(title, (title instanceof StringPP ? (StringPP)title : new StringPP(title)).toAbbreviation(), version, initStyle, initLAF, force);
  }
  
  /**
   * Creates a new BHFrame
   * @param title the title of the application (e.g. {@code "Blue Husky Application"})
   * @param shortTitle a shortened title for the application (e.g. {@code "BHApp"})
   * @param version the version of the application (e.g. {@code "1.0.0"})
   * @param initStyle the initial color style of the frame. See {@link bht.tools.fx.Colors}
   * @param initLAF the initial Look-And-Feel of the frame. See {@link bht.tools.fx.LookAndFeelChanger}
   * @param force whether to force the look and feel application. See {@link bht.tools.fx.LookAndFeelChanger#setLookAndFeel(byte, boolean)}
   * @see bht.tools.fx.Colors
   * @see bht.tools.fx.LookAndFeelChanger
   * @see bht.tools.fx.LookAndFeelChanger#setLookAndFeel(byte, boolean)
   */
  @SuppressWarnings({"UseOfSystemOutOrSystemErr", "OverridableMethodCallInConstructor"})
  public BHFrame(CharSequence title, CharSequence shortTitle, CharSequence version, byte initStyle, byte initLAF, boolean force)
  {
    System.out.println("BHFrame:  Initializing Variables...");
    /* At */ long last = System.nanoTime(), start = last;
    VERSION = version;
    TITLE = title;
    SHORT_TITLE = shortTitle == null || shortTitle.length() == 0 ? "Actions" : shortTitle;
    setSaveName(getSaveName() + " for " + TITLE);
    testTransparency();
    custColorsEditPanel = new BHCustColorsEditPanel(mC);
    try
    {
      latestVerURL = new java.net.URL("http://s.supuhstar.operaunite.com/s/content/BH/" + new StringPP(TITLE).toSafeURL() + "/latest.version");
      latestUpdURL = new java.net.URL("http://s.supuhstar.operaunite.com/s/content/BH/" + new StringPP(TITLE).toSafeURL() + "/latest.jar");
    }
    catch (MalformedURLException ex)
    {
      System.err.println("Could not parse URL for latest version");
      ex.printStackTrace();
    }
    System.out.println("BHFrame:  Initializing settings... (" + bht.tools.util.math.Numbers.groupDigits(System.nanoTime() - last) + ")");
    last = System.nanoTime();
    java.util.Properties p = new java.util.Properties();
    p.setProperty(SETTING_ANIM.toString(), TRUE_STR.toString());
    settings = new java.util.Properties(p);
    
    System.out.println("BHFrame:  Using OS Menu Bar... (" + bht.tools.util.math.Numbers.groupDigits(System.nanoTime() - last) + ")");
    last = System.nanoTime();
    BHCompUtilities.setUsesOSMenuBar(true, TITLE);
    
    System.out.println("BHFrame:  Initializing Components... (" + bht.tools.util.math.Numbers.groupDigits(System.nanoTime() - last) + ")");
    last = System.nanoTime();
    initComponents();
    
    System.out.println("BHFrame:  Positioning and fixing UI... (" + bht.tools.util.math.Numbers.groupDigits(System.nanoTime() - last) + ")");
//    last = System.nanoTime();
    
    setSize(getPreferredSize());
    CompAction.snapToCenter(getThis());
    CompAction.snapToCenter(custColorsEditDialog);
    CompAction.snapToCenter(setFontDialog);
    CompAction.snapToCenter(settingsDialog);
    CompAction.snapToCenter(updateDialog);
    CompAction.snapToCenter(aboutDialog);
    
    useTransparency = CompAction.testTranslucencySupport();
    
    if (initStyle != Colors.DEFAULT)
      mC.setStyle(initStyle);
    if (saveableLAFSelection.getState() == null)
    {
      saveableLAFSelection.setState(LookAndFeelChanger.getLookAndFeel(initLAF).getClass().getName());
      LookAndFeelChanger.setLookAndFeel(initLAF, force);
    }
    else
      addWindowListener(new WindowAdapter()//Added on Feb 19, 2012 (1.2.14) for Marian
      {
        @Override
        public void windowOpened(WindowEvent e)
        {
          try
          {
            LookAndFeelChanger.setLookAndFeel(saveableLAFSelection.getState(), true);
          }
          catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException ex)
          {
            System.err.println("BHFrame: Look-And-Feel could not be set.");
          }
          removeWindowListener(this);
        }
      });
    getRootPane().setDoubleBuffered(true);
    
    starting = false;
    System.out.println("BHFrame creation complete (" + bht.tools.util.math.Numbers.groupDigits(System.nanoTime() - start) + ")");
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING_MES: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings({"unchecked", "UseOfSystemOutOrSystemErr"})
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        /* At */ long last = System.nanoTime(), start = last;
        System.out.println("BHFrame.initComponents()");
        System.out.println("BHFrame.initComponents():  Initializing custColorsEditDialog...");
        custColorsEditDialog = new javax.swing.JDialog();
        setFontDialog = new javax.swing.JDialog();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        fontSearchTextField = new BHTextField("Type to Search...");
        fontListScrollPane = new javax.swing.JScrollPane();
        fontList = new javax.swing.JList<>();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        boldStyleCheckBox = new javax.swing.JCheckBox();
        italicStyleCheckBox = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        fontSizeSpinner = new BHSpinner<>();
        fontSizePlusDefaultCheckBox = new javax.swing.JCheckBox();
        fontConfirmationPanel = new javax.swing.JPanel();
        setFontDialogCancelButton = new javax.swing.JButton();
        setFontDialogOKButton = new javax.swing.JButton();
        exampleFontLabel = new javax.swing.JLabel();
        setFontDialogApplyButton = new javax.swing.JButton();
        settingsDialog = new javax.swing.JDialog();
        settingsTabbedPane = new javax.swing.JTabbedPane();
        basicSettingsPanel = new javax.swing.JPanel();
        useAnimationsCheckBox = new javax.swing.JCheckBox();
        updateDialog = new javax.swing.JDialog();
        updateDialogTextLabel = new javax.swing.JLabel();
        updateDialogProgressBar = new javax.swing.JProgressBar();
        updateDialogButton = new javax.swing.JButton();
        aboutDialog = new javax.swing.JDialog();
        aboutLogoImage = new BHImageComp(Icons.getIcon(Icons.WARNING_32), BHImageComp.BEHAVIOR_DEFAULT);
        aboutNameLabel = new javax.swing.JLabel();
        aboutVersionLabel = new javax.swing.JLabel();
        aboutCopyrightLabel = new javax.swing.JLabel();
        System.out.println("BHFrame.initComponents():  Initializing mainPanel... (" + Numbers.groupDigits(System.nanoTime() - last) + ")");
        last = System.nanoTime();
        mainPanel = new java.awt.Container();
        System.out.println("BHFrame.initComponents():  Initializing jMenuBar... (" + Numbers.groupDigits(System.nanoTime() - last) + ")");
        last = System.nanoTime();
        jMenuBar = getJMenuBar() == null ? new javax.swing.JMenuBar() : getJMenuBar();
        actionsMenu = new javax.swing.JMenu();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        exitMenuItem = new javax.swing.JMenuItem();
        fileMenu = new javax.swing.JMenu();
        editMenu = new javax.swing.JMenu();
        copyMenuItem = new javax.swing.JMenuItem();
        viewMenu = new javax.swing.JMenu();
        visualStylesMenu = new javax.swing.JMenu();
        mochaVisualStyleMenuItem = new javax.swing.JMenuItem();
        huskyVisualStyleMenuItem = new javax.swing.JMenuItem();
        astroVisualStyleMenuItem = new javax.swing.JMenuItem();
        custColorsMenu = new javax.swing.JMenu();
        customVisualStyleMenuItem = new javax.swing.JMenuItem();
        editCustomVisualStyleMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        defaultVisualStyleMenuItem = new javax.swing.JMenuItem();
        /*
        lookAndFeelMenu = //;
        */
        fontMenu = new javax.swing.JMenu();
        selectFontMenuItem = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();
        settingsMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        checkForUpdatesMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();

        //new Thread(new Runnable()
            //{
                //  public void run()
                //  {
                    start = System.nanoTime();
                    System.out.println("BHFrame.initComponents():  Setting up behavior of custColorsEditDialog (setting default close operation)... (" + Numbers.groupDigits(System.nanoTime() - last) + ")");
                    last = System.nanoTime();
                    custColorsEditDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
                    System.out.println("BHFrame.initComponents():  Setting up behavior of custColorsEditDialog (setting minimum size)... (" + Numbers.groupDigits(System.nanoTime() - last) + ")");
                    last = System.nanoTime();
                    custColorsEditDialog.setTitle("Custom Colors Editor - " + TITLE);
                    custColorsEditDialog.setMinimumSize(custColorsEditPanel.getPreferredSize());
                    System.out.println("BHFrame.initComponents():  Setting up behavior of custColorsEditDialog (adding window listener)... (" + Numbers.groupDigits(System.nanoTime() - last) + ")");
                    last = System.nanoTime();
                    custColorsEditDialog.addWindowListener(new java.awt.event.WindowAdapter() {
                        public void windowClosing(java.awt.event.WindowEvent evt) {
                            custColorsEditDialogWindowClosing(evt);
                        }
                    });
                    System.out.println("BHFrame.initComponents():  Adding sub-components of custColorsEditDialog... (" + Numbers.groupDigits(System.nanoTime() - last) + ")");
                    last = System.nanoTime();
                    custColorsEditDialog.add(custColorsEditPanel);

                    //}
                //}, "BHFrame.initComponents():  Setting up custColorsEditDialog").start();

        setFontDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setFontDialog.setTitle("Fonts - " + TITLE);
        setFontDialog.setMinimumSize(getPreferredSize());
        setFontDialog.setModal(true);
        setFontDialog.setModalityType(java.awt.Dialog.ModalityType.DOCUMENT_MODAL);
        setFontDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                setFontDialogWindowClosing(evt);
            }
        });
        setFontDialog.getContentPane().setLayout(new java.awt.GridBagLayout());

        jSplitPane1.setContinuousLayout(true);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        fontSearchTextField.setEnabled(false);
        fontSearchTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fontSearchTextFieldActionPerformed(evt);
            }
        });
        fontSearchTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                fontSearchTextFieldKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel1.add(fontSearchTextField, gridBagConstraints);

        fontListScrollPane.setMinimumSize(new java.awt.Dimension(64, 64));

        fontList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                fontListValueChanged(evt);
            }
        });
        fontListScrollPane.setViewportView(fontList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(fontListScrollPane, gridBagConstraints);

        jButton1.setText("Refresh font list");
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel1.add(jButton1, gridBagConstraints);

        jLabel1.setText("Fonts");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        jPanel1.add(jLabel1, gridBagConstraints);
        jLabel1.getAccessibleContext().setAccessibleDescription("Colors.SIZE_OVERRIDE + 8 + Colors.COMMAND_SEP");

        jSplitPane1.setLeftComponent(jPanel1);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel2.setText("Modifiers");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        jPanel2.add(jLabel2, gridBagConstraints);
        jLabel2.getAccessibleContext().setAccessibleDescription("Colors.SIZE_OVERRIDE + 8 + Colors.COMMAND_SEP");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Style"));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        boldStyleCheckBox.setSelected((saveableFontStyle.getState() & java.awt.Font.BOLD) != 0);
        boldStyleCheckBox.setText("Bold");
        boldStyleCheckBox.setEnabled(false);
        boldStyleCheckBox.setOpaque(false);
        boldStyleCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boldStyleCheckBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(boldStyleCheckBox, gridBagConstraints);

        italicStyleCheckBox.setSelected((saveableFontStyle.getState() & java.awt.Font.ITALIC) != 0);
        italicStyleCheckBox.setText("Italic");
        italicStyleCheckBox.setEnabled(false);
        italicStyleCheckBox.setOpaque(false);
        italicStyleCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                italicStyleCheckBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(italicStyleCheckBox, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel2.add(jPanel3, gridBagConstraints);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Size"));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        fontSizeSpinner.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(saveableFontMod.getState()), Double.valueOf(0.0d), null, Double.valueOf(1.0d)));
        fontSizeSpinner.setEnabled(false);
        fontSizeSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                fontSizeSpinnerStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel4.add(fontSizeSpinner, gridBagConstraints);

        fontSizePlusDefaultCheckBox.setSelected(saveablePlusDefaultFontSize.getState());
        fontSizePlusDefaultCheckBox.setText("+ default font size");
        fontSizePlusDefaultCheckBox.setEnabled(false);
        fontSizePlusDefaultCheckBox.setOpaque(false);
        fontSizePlusDefaultCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fontSizePlusDefaultCheckBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPanel4.add(fontSizePlusDefaultCheckBox, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel2.add(jPanel4, gridBagConstraints);

        jSplitPane1.setRightComponent(jPanel2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        setFontDialog.getContentPane().add(jSplitPane1, gridBagConstraints);

        fontConfirmationPanel.setLayout(new java.awt.GridBagLayout());

        setFontDialogCancelButton.setText("Cancel");
        setFontDialogCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setFontDialogCancelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        fontConfirmationPanel.add(setFontDialogCancelButton, gridBagConstraints);

        setFontDialogOKButton.setText("OK");
        setFontDialogOKButton.setEnabled(false);
        setFontDialogOKButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setFontDialogOKButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        fontConfirmationPanel.add(setFontDialogOKButton, gridBagConstraints);

        exampleFontLabel.setText("This is an example.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        fontConfirmationPanel.add(exampleFontLabel, gridBagConstraints);

        setFontDialogApplyButton.setText("Apply");
        setFontDialogApplyButton.setEnabled(false);
        setFontDialogApplyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setFontDialogApplyButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        fontConfirmationPanel.add(setFontDialogApplyButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        setFontDialog.getContentPane().add(fontConfirmationPanel, gridBagConstraints);

        settingsDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        settingsDialog.setTitle("Settings - " + TITLE);
        settingsDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                settingsDialogWindowClosing(evt);
            }
        });
        settingsDialog.getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        basicSettingsPanel.setLayout(new java.awt.GridBagLayout());

        useAnimationsCheckBox.setSelected(true);
        useAnimationsCheckBox.setText("Use animations");
        useAnimationsCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                useAnimationsCheckBoxStateChanged(evt);
            }
        });
        basicSettingsPanel.add(useAnimationsCheckBox, new java.awt.GridBagConstraints());

        settingsTabbedPane.addTab("Basic Settings", basicSettingsPanel);

        settingsDialog.getContentPane().add(settingsTabbedPane);

        updateDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        updateDialog.setTitle("Update Check - " + TITLE);
        updateDialog.setMinimumSize(new java.awt.Dimension(256, 128));
        updateDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                updateDialogWindowClosing(evt);
            }
        });
        updateDialog.getContentPane().setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        updateDialog.getContentPane().add(updateDialogTextLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        updateDialog.getContentPane().add(updateDialogProgressBar, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        updateDialog.getContentPane().add(updateDialogButton, gridBagConstraints);
        updateDialogButton.setVisible(false);

        updateDialog.pack();
        updateDialog.setMinimumSize(updateDialog.getPreferredSize());

        aboutDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        aboutDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                aboutDialogWindowClosing(evt);
            }
        });
        aboutDialog.getContentPane().setLayout(new java.awt.GridBagLayout());

        javax.swing.GroupLayout aboutLogoImageLayout = new javax.swing.GroupLayout(aboutLogoImage);
        aboutLogoImage.setLayout(aboutLogoImageLayout);
        aboutLogoImageLayout.setHorizontalGroup(
            aboutLogoImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 434, Short.MAX_VALUE)
        );
        aboutLogoImageLayout.setVerticalGroup(
            aboutLogoImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 254, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        aboutDialog.getContentPane().add(aboutLogoImage, gridBagConstraints);

        aboutNameLabel.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        aboutNameLabel.setText(TITLE.toString());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        aboutDialog.getContentPane().add(aboutNameLabel, gridBagConstraints);
        aboutNameLabel.getAccessibleContext().setAccessibleDescription(Colors.FONT_OVERRIDE + "Trebuchet MS" + Colors.COMMAND_SEP + Colors.FONT_STYLE_OVERRIDE + Font.BOLD + Colors.COMMAND_SEP + Colors.SIZE_OVERRIDE + 4 + Colors.COMMAND_SEP);

        aboutVersionLabel.setText(VERSION.toString());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        aboutDialog.getContentPane().add(aboutVersionLabel, gridBagConstraints);

        {
            int y;
            aboutCopyrightLabel.setText("Copyright Blue Husky Programming \u00A9" + ((y = Calendar.getInstance().get(Calendar.YEAR)) > 2012 ? "2012-" + y : y) + "");
        }
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        aboutDialog.getContentPane().add(aboutCopyrightLabel, gridBagConstraints);

        aboutDialog.pack();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(TITLE.toString());
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                formComponentAdded(evt);
            }
        });

        System.out.println("BHFrame.initComponents():  Adding sub-components of mainPanel... (" + Numbers.groupDigits(System.nanoTime() - last) + ")");
        last = System.nanoTime();

        System.out.println("BHFrame.initComponents():  Laying out sub-components of mainPanel... (" + Numbers.groupDigits(System.nanoTime() - last) + ")");
        last = System.nanoTime();

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 279, Short.MAX_VALUE)
        );

        System.out.println("BHFrame.initComponents():  Adding mainPanel to content pane... (" + Numbers.groupDigits(System.nanoTime() - last) + ")");
        last = System.nanoTime();

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);
        getContentPane().add(bhmp, java.awt.BorderLayout.SOUTH);

        System.out.println("BHFrame.initComponents():  Adding sub-components of jMenuBar... (" + Numbers.groupDigits(System.nanoTime() - last) + ")");
        last = System.nanoTime();

        actionsMenu.setMnemonic('A');
        actionsMenu.setText(SHORT_TITLE.toString());
        actionsMenu.getPopupMenu().addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                actionsMenuComponentAdded(evt);
            }
        });
        actionsMenu.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                actionsMenuComponentAdded(evt);
            }
        });
        actionsMenu.add(jSeparator1);

        exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        exitMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bht/resources/application-exit 16.png"))); // NOI18N
        exitMenuItem.setText("Exit");
        exitMenuItem.setToolTipText("Exit the program");
        exitMenuItem.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentRemoved(java.awt.event.ContainerEvent evt) {
                exitMenuItemComponentRemoved(evt);
            }
        });
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        actionsMenu.add(exitMenuItem);

        jMenuBar.add(actionsMenu);

        fileMenu.setText("File");
        jMenuBar.add(fileMenu);

        editMenu.setMnemonic('E');
        editMenu.setText("Edit");

        copyMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        copyMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bht/resources/edit-copy 16.png"))); // NOI18N
        copyMenuItem.setText("Copy");
        copyMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(copyMenuItem);

        jMenuBar.add(editMenu);

        viewMenu.setMnemonic('V');
        viewMenu.setText("View");

        visualStylesMenu.setText("Color Styles");

        mochaVisualStyleMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK));
        mochaVisualStyleMenuItem.setText("Mocha");
        mochaVisualStyleMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mochaVisualStyleMenuItemActionPerformed(evt);
            }
        });
        visualStylesMenu.add(mochaVisualStyleMenuItem);

        huskyVisualStyleMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_2, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK));
        huskyVisualStyleMenuItem.setText("Husky");
        huskyVisualStyleMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                huskyVisualStyleMenuItemActionPerformed(evt);
            }
        });
        visualStylesMenu.add(huskyVisualStyleMenuItem);

        astroVisualStyleMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_3, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK));
        astroVisualStyleMenuItem.setText("Astro");
        astroVisualStyleMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                astroVisualStyleMenuItemActionPerformed(evt);
            }
        });
        visualStylesMenu.add(astroVisualStyleMenuItem);

        custColorsMenu.setText("Custom");

        customVisualStyleMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_4, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK));
        customVisualStyleMenuItem.setText("Apply Custom Colors");
        customVisualStyleMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customVisualStyleMenuItemActionPerformed(evt);
            }
        });
        custColorsMenu.add(customVisualStyleMenuItem);

        editCustomVisualStyleMenuItem.setText("Edit Custom Colors");
        editCustomVisualStyleMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editCustomVisualStyleMenuItemActionPerformed(evt);
            }
        });
        custColorsMenu.add(editCustomVisualStyleMenuItem);

        visualStylesMenu.add(custColorsMenu);
        visualStylesMenu.add(jSeparator2);

        defaultVisualStyleMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_0, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK));
        defaultVisualStyleMenuItem.setText("Default");
        defaultVisualStyleMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                defaultVisualStyleMenuItemActionPerformed(evt);
            }
        });
        visualStylesMenu.add(defaultVisualStyleMenuItem);

        viewMenu.add(visualStylesMenu);

        lookAndFeelMenu.setText("Look-And-Feels");
        viewMenu.add(lookAndFeelMenu);

        fontMenu.setText("Font");

        selectFontMenuItem.setText("Select Font");
        selectFontMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectFontMenuItemActionPerformed(evt);
            }
        });
        fontMenu.add(selectFontMenuItem);

        viewMenu.add(fontMenu);

        jMenuBar.add(viewMenu);

        toolsMenu.setMnemonic('T');
        toolsMenu.setText("Tools");

        settingsMenuItem.setIcon(Icons.getIcon(Icons.SETTINGS_16));
        settingsMenuItem.setText("Settings");
        settingsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsMenuItemActionPerformed(evt);
            }
        });
        toolsMenu.add(settingsMenuItem);

        jMenuBar.add(toolsMenu);

        helpMenu.setMnemonic('H');
        helpMenu.setText("Help");

        checkForUpdatesMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bht/resources/network 16.png"))); // NOI18N
        checkForUpdatesMenuItem.setText("Check for Updates");
        checkForUpdatesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkForUpdatesMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(checkForUpdatesMenuItem);

        aboutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, java.awt.event.InputEvent.ALT_MASK));
        aboutMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bht/resources/dialog-information 16.png"))); // NOI18N
        aboutMenuItem.setText("About...");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        jMenuBar.add(helpMenu);

        System.out.println("BHFrame.initComponents():  Setting jMenuBar as default JMenuBar... (" + Numbers.groupDigits(System.nanoTime() - last) + ")");

        setJMenuBar(jMenuBar);
    }// </editor-fold>//GEN-END:initComponents

  private void settingsMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_settingsMenuItemActionPerformed
  {//GEN-HEADEREND:event_settingsMenuItemActionPerformed
    openWindow(settingsDialog);
  }//GEN-LAST:event_settingsMenuItemActionPerformed

  private void useAnimationsCheckBoxStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_useAnimationsCheckBoxStateChanged
  {//GEN-HEADEREND:event_useAnimationsCheckBoxStateChanged
    boolean b = useAnimationsCheckBox.isSelected();
    settings.setProperty(SETTING_ANIM.toString(), (b ? TRUE_STR : FALSE_STR).toString());
    saveableUseAnims.setState(b);
  }//GEN-LAST:event_useAnimationsCheckBoxStateChanged

  private void settingsDialogWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_settingsDialogWindowClosing
  {//GEN-HEADEREND:event_settingsDialogWindowClosing
    closeWindow(settingsDialog);
  }//GEN-LAST:event_settingsDialogWindowClosing

  private void setFontDialogWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_setFontDialogWindowClosing
  {//GEN-HEADEREND:event_setFontDialogWindowClosing
    closeWindow(setFontDialog);
  }//GEN-LAST:event_setFontDialogWindowClosing

  private void checkForUpdatesMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_checkForUpdatesMenuItemActionPerformed
  {//GEN-HEADEREND:event_checkForUpdatesMenuItemActionPerformed
    openWindow(updateDialog);
    checkForUpdates();
  }//GEN-LAST:event_checkForUpdatesMenuItemActionPerformed

  private void updateDialogWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_updateDialogWindowClosing
  {//GEN-HEADEREND:event_updateDialogWindowClosing
    closeWindow(updateDialog);
  }//GEN-LAST:event_updateDialogWindowClosing

  private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_aboutMenuItemActionPerformed
  {//GEN-HEADEREND:event_aboutMenuItemActionPerformed
    openWindow(aboutDialog);
  }//GEN-LAST:event_aboutMenuItemActionPerformed

  private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
  {//GEN-HEADEREND:event_formWindowClosing
    if (exitListeners == null || exitListeners.isEmpty())
      exit(0);
  }//GEN-LAST:event_formWindowClosing

  private void aboutDialogWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_aboutDialogWindowClosing
  {//GEN-HEADEREND:event_aboutDialogWindowClosing
    closeWindow(aboutDialog);
  }//GEN-LAST:event_aboutDialogWindowClosing

  private void actionsMenuComponentAdded(java.awt.event.ContainerEvent evt)                                           
  {                                               
    try
    {
      if (!starting && !fixingExit && exitMenuItem.getParent().equals(actionsMenu.getPopupMenu()))
      {
//        System.out.println("Component added to actions menu");

        fixingExit = true;
        actionsMenu.add(jSeparator1);
        actionsMenu.add(exitMenuItem);
        fixingExit = false;
      }
    }
    catch (Throwable t)
    {
      alertOf(t);
    }
  }                                          

  private void mochaVisualStyleMenuItemActionPerformed(java.awt.event.ActionEvent evt)                                                         
  {                                                             
    mC.setStyle(Colors.MOCHA);
    fixStyleInAllWindows();
  }                                                        

  private void huskyVisualStyleMenuItemActionPerformed(java.awt.event.ActionEvent evt)                                                         
  {                                                             
    mC.setStyle(Colors.HUSKY);
    fixStyleInAllWindows();
  }                                                        

  private void astroVisualStyleMenuItemActionPerformed(java.awt.event.ActionEvent evt)                                                         
  {                                                             
    mC.setStyle(Colors.ASTRO);
    fixStyleInAllWindows();
  }                                                        

  private void customVisualStyleMenuItemActionPerformed(java.awt.event.ActionEvent evt)                                                          
  {                                                              
    mC.setStyle(mC.CUSTOM);
    fixStyleInAllWindows();
  }                                                         

  private void editCustomVisualStyleMenuItemActionPerformed(java.awt.event.ActionEvent evt)                                                              
  {                                                                  
    alertOf("Opening Edit Colors dialog....", Constants.PLAIN_MES);
    openWindow(custColorsEditDialog);
    alertOf("Edit Colors Dialog Open", BHMessagePanel.PLAIN_MES);
  }                                                             
  
  @SuppressWarnings("deprecation")
  private void defaultVisualStyleMenuItemActionPerformed(java.awt.event.ActionEvent evt)
  {
    try
    {
      mC.setStyle(Colors.DEFAULT);
      mC.fixStyleInAllWindows();
      LookAndFeelChanger.setLookAndFeel(LookAndFeelChanger.getLookAndFeel(), true);
      if (!YesNoBox.bool("Default color style is prone to bugs. Keep Default color style?", "Default color style applied", YesNoBox.WARNING_MES))
      {
        mC.setStyle(mC.getLastStyle() == Colors.DEFAULT ? Colors.MOCHA : mC.getLastStyle());
        mC.fixStyleInAllWindows();
      }
    }
    catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException t)
    {
      alertOf(t);
    }
    catch (Throwable t)
    {
      alertOf(t);
    }
  }

  private void copyMenuItemActionPerformed(java.awt.event.ActionEvent evt)                                             
  {                                                 
    try
    {
//    for (int i=0; i < copyListeners.length; i++)
//    {
      boolean a = false, b = copyListener != null;
      if (b)
        copyListener/*s[i]*/.actionPerformed(evt);
//    }
      if (autoCopy)
      {
        a = Copier.searchToCopyIn(this);
      }
      alertOf(autoCopy ? (a ? "Selected text has been copied to clipboard! " : "Could not copy selected text to clipboard. ")
              : (b ? "Copy successful. " : "Nothing to copy. "), (autoCopy ? !a : !b) ? BHMessagePanel.WARNING_MES : BHMessagePanel.INFO_MES);
    }
    catch (Throwable t)
    {
      alertOf(t);
    }
  }                                            

  private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt)                                             
  {                                                 
    exit(0);
  }                                            

  private void custColorsEditDialogWindowClosing(java.awt.event.WindowEvent evt)                                                   
  {                                                       
    closeWindow(custColorsEditDialog);
    mC.fixStyleIn(this);
  }                                                  

  private void exitMenuItemComponentRemoved(java.awt.event.ContainerEvent evt)                                              
  {                                                  
    actionsMenu.remove(jSeparator1);
  }                                             

  private void formComponentAdded(java.awt.event.ContainerEvent evt)                                    
  {                                        
    mC.fixStyleIn(evt.getContainer());
  }                                   

  private void selectFontMenuItemActionPerformed(java.awt.event.ActionEvent evt)                                                   
  {                                                       
    if(!fontDialogHasBeenLoaded)
    {
      reloadFonts();
      setFontDialog.setMinimumSize(setFontDialog.getPreferredSize());
      CompAction.snapToCenter(setFontDialog);
    }
    setFontDialog.setVisible(true);
  }                                                  

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)                                         
  {                                             
    reloadFonts();
  }                                        

  private void italicStyleCheckBoxActionPerformed(java.awt.event.ActionEvent evt)                                                    
  {                                                        
    refreshExampleFont();
  }                                                   

  private void boldStyleCheckBoxActionPerformed(java.awt.event.ActionEvent evt)                                                  
  {                                                      
    refreshExampleFont();
  }                                                 

  private void fontListValueChanged(javax.swing.event.ListSelectionEvent evt)                                    
  {                                        
    refreshExampleFont();
  }                                   

  private void fontSizePlusDefaultCheckBoxActionPerformed(java.awt.event.ActionEvent evt)                                                            
  {                                                                
    refreshExampleFont();
  }                                                           

  private void fontSizeSpinnerStateChanged(javax.swing.event.ChangeEvent evt)                                             
  {                                                 
    refreshExampleFont();
  }                                            

  private void setFontDialogOKButtonActionPerformed(java.awt.event.ActionEvent evt)                                         
  {                                             
    applyFontChanges();
    setFontDialog.setVisible(false);
  }                                        

  private void setFontDialogCancelButtonActionPerformed(java.awt.event.ActionEvent evt)                                         
  {                                             
    cancelFontChanges();
    setFontDialog.setVisible(false);
  }                                        

  private void setFontDialogApplyButtonActionPerformed(java.awt.event.ActionEvent evt)                                         
  {                                             
    applyFontChanges();
  }                                        

  private void fontSearchTextFieldActionPerformed(java.awt.event.ActionEvent evt)                                                    
  {                                                        
    searchFontList();
  }                                                   

  private void fontSearchTextFieldKeyReleased(java.awt.event.KeyEvent evt)                                                
  {                                                    
    searchFontList();
  }                                               

  public javax.swing.JMenu getMenu(byte mask)
  {
    switch(mask)
    {
      default:
        throw new IllegalArgumentException("Unrecognized mask: " + mask);
      case ACTIONS_MENU:
        return actionsMenu;
      case FILE_MENU:
        return fileMenu;
      case EDIT_MENU:
        return editMenu;
      case VIEW_MENU:
        return viewMenu;
      case TOOLS_MENU:
        return toolsMenu;
      case HELP_MENU:
        return helpMenu;
    }
  }

  @Override
  public Colors getColors()
  {
    return mC;
  }
  
  /**
   * Returns the {@link javax.swing.JDialog} that containsAll the {@link BHCustColorsEditPanel} that will manage this program's 
   * colors
   * @return the dialog that holds this program's {@link BHCustColorsEditPanel}
   */
  public javax.swing.JDialog getCustColorsEditDialog()
  {
    return custColorsEditDialog;
  }
  
  /**
   * Returns the instance of CompAction used by this frame
   * @return the instance of CompAction used by this frame
   */
  @Override
  public CompAction getCompAction()
  {
    return mCA;
  }

  /**
   * Sets whether the frame should automatically search for selected text to clone when the Copy menu item is selected (AutoCopier)
   * @param shouldAutoCopy {@code true} if the frame should use the above described AutoCopier technique
   */
  public void setAutoCopies(boolean shouldAutoCopy)
  {
    autoCopy = shouldAutoCopy;
  }
  
  /**
   * returns whether the frame uses the AutoCopier technique
   * @return true if the frame uses the AutoCopier technique
   * @see #setAutoCopies(boolean shouldAutoCopy)
   */
  public boolean isAutoCopies()
  {
    return autoCopy;
  }

  @Override
  public java.awt.Container getContentPane()
  {
    return starting ? super.getContentPane() : mainPanel;
  }

  @Override
  public void setContentPane(Container contentPane)
  {
    if (starting)
      super.setContentPane(contentPane);
    else
      mainPanel = contentPane;
  }

  public void setPrints(boolean shouldPrint)
  {
    prints = shouldPrint;
    bhmp.setPrints(shouldPrint);
  }
  
  public boolean isPrints()
  {
    return prints;
  }

  public BHMessagePanel getMessagePanel()
  {
    return bhmp;
  }

  /**
   * Alerts the user of a message, via the built-in {@link BHMessagePanel}. The currently displayed icon is reused.
   * @param message the message which the user will see
   * @return a {@link String} version of the alert.
   * @see bht.tools.comps.BHMessagePanel
   */
  public String alertOf(String message)
  {
    String ret = bhmp.alertOf(message);
    if (log != null)
      log.logAction(ret);
    return ret;
  }

  /**
   * Alerts the user of a message, via the built-in {@link BHMessagePanel}. The {@code byte} mask tells the panel which icon
   * to display
   * @param message the message which the user will see
   * @param type the type of message. Use the provided {@code byte} constant masks.
   * @return a {@link String} version of the alert.
   * @see bht.tools.comps.BHMessagePanel
   */
  public String alertOf(String message, byte type)
  {
    String ret = bhmp.alertOf(message, type);
    if (log != null)
      log.logAction(ret);
    return ret;
  }

  /**
   * Alerts the user that a {@link Throwable} has been thrown, via the built-in {@link BHMessagePanel}. Similar to using
   * {@link alertOf(String message, byte type)} where the {@code type} is {@link bht.tools.Constants#ERROR_MES}, but with stack printing and
   * conditional message filling
   * @param t the {@link Throwable} of which to alert the user
   * @return a {@link String} version of the alert.
   * @see bht.tools.comps.BHMessagePanel
   */
  public String alertOf(Throwable t)
  {
    String ret = bhmp.alertOf(t);
    if (log != null)
      log.logThrowable(t);
    return ret;
  }

  /**
   * calls the {@link clearAlert()} method of the built-in {@link BHMessagePanel}
   * @see bht.tools.comps.BHMessagePanel
   */
  public void clearAlert()
  {
    bhmp.clear();
  }
  
  /**
   * Tries to save the state and performs all exitListener functions. If none have been specified, all windows are closed and
   * {@code System.exit(status)} is called
   * @param status the exit status of the program
   */
  @SuppressWarnings("UseOfSystemOutOrSystemErr")
  public void exit(int status)
  {
    alertOf("Exiting " + TITLE + "...", Constants.INFO_MES);
    try
    {
      if (settingsSaver != null)
        settingsSaver.saveState();
    }
    catch (Throwable t)
    {
      System.err.println("Couldn't save the program state:");
      t.printStackTrace();
    }
    if (exitListeners == null || exitListeners.isEmpty())
    {
      int s = 0;
      if (useTransparency)
      {
        for (java.awt.Window w : getWindows())
          if (w != null && w.isVisible())
            (w instanceof BHComponent ? ((BHComponent) w).getCompAction() : new CompAction()).ghostOut(w, true, s);
      }
      else if (shouldUseAnimations())
      {
        for (java.awt.Window w : getWindows())
          if (w != null && w.isVisible())
            (w instanceof BHComponent ? ((BHComponent) w).getCompAction() : new CompAction()).slideClosed(w, true, s);
      }
      else
        System.exit(s);
    }
    else
      for (java.awt.event.ActionListener al : exitListeners)
        al.actionPerformed(new java.awt.event.ActionEvent(this, 0, "" + status));
  }

  /**
   * Adds an {@link java.awt.event.ActionListener} to the array of actions to be performed when the exit menu item is selected
   * <h4>WARNING</h4>
   * If any exit listeners are added, the {@code BHFrame} surrenders exit control to them. This means that ONLY what is
   * specified in the various exit listeners will be carried out.
   * @param al the {@link java.awt.event.ActionListener} to be performed when the exit menu item is selected
   */
  public void addExitListener(java.awt.event.ActionListener al)
  {
    (exitListeners == null ? exitListeners = new ArrayPP<>() : exitListeners).add(al);
  }

  /**
   * Adds an {@link java.awt.event.ActionListener} to the array of actions to be performed when copying an object
   * @param al the action to be performed in the event that the user uses the Copy menu item
   */
  public void addCopyListener(java.awt.event.ActionListener al)
  {
//    java.awt.event.ActionListener als[] = new java.awt.event.ActionListener[copyListeners.length + 1];
//    System.arraycopy(copyListeners, 0, als, 0, copyListeners.length);
//    als[als.length - 1] = al;
//    copyListeners = als;
    copyListener = java.awt.AWTEventMulticaster.add(al, copyListener);
    fixCopyMenuItem();
  }
  
  /**
   * Adds an {@link java.awt.event.ActionListener} to the array of actions to be performed when copying an object
   * @param al the action to be performed in the event that the user uses the Copy menu item
   */
  public void removeCopyListener(java.awt.event.ActionListener al)
  {
    copyListener = java.awt.AWTEventMulticaster.remove(al, copyListener);
    fixCopyMenuItem();
  }
  
  private void fixCopyMenuItem()
  {
    copyMenuItem.setEnabled(autoCopy || copyListener != null);
  }
  
  @Override
  public java.awt.Component add(java.awt.Component c)
  {
    if (starting)
      return super.add(c);
    mainPanel.add(c);
    return c;
  }
  
  @Override
  public void add(java.awt.Component c, Object constraints)
  {
//    if (starting)
      super.add(c, constraints);
//    else
//      mainPanel.add(c, constraints);
  }
  
  @Override
  public java.awt.Component add(java.awt.Component c, int index)
  {
    if (starting)
      return super.add(c, index);
    mainPanel.add(c, index);
    return c;
  }
  
  @Override
  public java.awt.Component add(String name, java.awt.Component c)
  {
    if (starting)
      return super.add(name, c);
    mainPanel.add(name, c);
    return c;
  }
  
  @Override
  public void add(java.awt.Component c, Object constraints, int index)
  {
    if (starting)
      super.add(c, constraints, index);
    else
      mainPanel.add(c, constraints, index);
  }

  @Override
  public void setLayout(java.awt.LayoutManager manager)
  {
    if (starting)
      super.setLayout(manager);
    else
      mainPanel.setLayout(manager);
  }


  private static void lafMenuSetEnabled(boolean b)
  {
    for (int i=0; i < lookAndFeelMenu.getComponentCount(); i++)
    {
      lookAndFeelMenu.getComponent(i).setEnabled(b);
    }
  }

  private static ArrayPP<JRadioButtonMenuItem> lafRadioButtonMenuItems;
  private static javax.swing.ButtonGroup lafButtonGroup;
  private static void initLAFMenu()
  {
    javax.swing.LookAndFeel laf;
    lafRadioButtonMenuItems = new ArrayPP<>();
    lafButtonGroup = new ButtonGroup();
    lookAndFeelMenu = new JMenu();
    creation: for (final javax.swing.UIManager.LookAndFeelInfo lafi : javax.swing.UIManager.getInstalledLookAndFeels())
    {
      try
      {
        laf = LookAndFeelChanger.getLookAndFeel(lafi);
        for (int i=0, l=lafRadioButtonMenuItems.length(); i < l; i++)
          if (lafRadioButtonMenuItems.get(i).getText().equals(laf.getName()))
            continue creation;
        lafRadioButtonMenuItems.add(new javax.swing.JRadioButtonMenuItem(new javax.swing.AbstractAction("LAF switcher")
        {
          private static final long serialVersionUID = 1L;
          @Override
          @SuppressWarnings("deprecation")
          public void actionPerformed(java.awt.event.ActionEvent e)
          {
            try
            {
              lafMenuSetEnabled(false);
              LookAndFeelChanger.setLookAndFeel(lafi, true);
//              setDefaultLookAndFeelDecorated(true);
//              getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
              saveableLAFSelection.setState(lafi.getClassName());
            }
            catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException t)
            {
              t.printStackTrace();
              if (YesNoBox.bool("There was an unexpected error when switching Look-And-Feel. Quit program for safety?",
                                "Error when switching LAF - " + TITLE, YesNoBox.ERROR_MES)) System.exit(0);
            }
            catch (Throwable t)
            {
              t.printStackTrace();
              if (YesNoBox.bool("There was a particularly unexpected error when switching Look-And-Feel. Quit program for safety?",
                                "Error when switching LAF - " + TITLE, YesNoBox.ERROR_MES)) System.exit(0);
            }
            lafMenuSetEnabled(true);
          }
        }));
        lafButtonGroup.add(lafRadioButtonMenuItems.getLastItem());
        lafRadioButtonMenuItems.getLastItem().setSelected(saveableLAFSelection.getState().equals(lafi.getClassName()) ||
                      (lafButtonGroup.getSelection() == null &&
                       LookAndFeelChanger.getLookAndFeel(LookAndFeelChanger.DEFAULT).getName().equals(laf.getName())));
        boolean b;
        lafRadioButtonMenuItems.getLastItem().setText(laf.getName() + " (" + laf.getDescription() + ")");
        if (laf.isNativeLookAndFeel())
          lafRadioButtonMenuItems.getLastItem().setIcon(Icons.getIcon(Icons.SPECIAL_16));
        lafRadioButtonMenuItems.getLastItem().setEnabled(b = laf.isSupportedLookAndFeel());
        lafRadioButtonMenuItems.getLastItem().setToolTipText(laf.isSupportedLookAndFeel() ?
            "You might not be able to apply the \"" + laf.getName() + "\" because it is not " + "supported on this system." :
            (laf.isNativeLookAndFeel() ? "[NATIVE] - " : "") + (b ? "Set the Look-And-Feel of the application to \"" +
            laf.getName() + "\"" : laf.getName() + " is not supported."));
        lookAndFeelMenu.add(lafRadioButtonMenuItems.getLastItem());
      }
      catch (ClassNotFoundException | InstantiationException | IllegalAccessException t)
      {
        t.printStackTrace();
      }
      catch (Throwable t)
      {
        t.printStackTrace();
      }
    }
  }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel aboutCopyrightLabel;
    private javax.swing.JDialog aboutDialog;
    /*
    private javax.swing.JPanel aboutLogoImage;
    */private BHImageComp aboutLogoImage;
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JLabel aboutNameLabel;
    private javax.swing.JLabel aboutVersionLabel;
    private javax.swing.JMenu actionsMenu;
    private javax.swing.JMenuItem astroVisualStyleMenuItem;
    private javax.swing.JPanel basicSettingsPanel;
    private static javax.swing.JCheckBox boldStyleCheckBox;
    private javax.swing.JMenuItem checkForUpdatesMenuItem;
    private javax.swing.JMenuItem copyMenuItem;
    private static volatile javax.swing.JDialog custColorsEditDialog;
    private javax.swing.JMenu custColorsMenu;
    private javax.swing.JMenuItem customVisualStyleMenuItem;
    private javax.swing.JMenuItem defaultVisualStyleMenuItem;
    private javax.swing.JMenuItem editCustomVisualStyleMenuItem;
    private javax.swing.JMenu editMenu;
    private static javax.swing.JLabel exampleFontLabel;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private static javax.swing.JPanel fontConfirmationPanel;
    /*
    private static javax.swing.JList fontList;
    */private javax.swing.JList<String> fontList;
    private static javax.swing.JScrollPane fontListScrollPane;
    private javax.swing.JMenu fontMenu;
    /*
    private static javax.swing.JTextField fontSearchTextField;
    */private BHTextField fontSearchTextField;
    private static javax.swing.JCheckBox fontSizePlusDefaultCheckBox;
    /*
    private static javax.swing.JSpinner fontSizeSpinner;
    */private BHSpinner<Integer> fontSizeSpinner;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem huskyVisualStyleMenuItem;
    private static javax.swing.JCheckBox italicStyleCheckBox;
    private static javax.swing.JButton jButton1;
    private static javax.swing.JLabel jLabel1;
    private static javax.swing.JLabel jLabel2;
    private javax.swing.JMenuBar jMenuBar;
    private static javax.swing.JPanel jPanel1;
    private static javax.swing.JPanel jPanel2;
    private static javax.swing.JPanel jPanel3;
    private static javax.swing.JPanel jPanel4;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private static javax.swing.JSplitPane jSplitPane1;
    private static javax.swing.JMenu lookAndFeelMenu;
    /*
    private javax.swing.JPanel mainPanel;
    */private java.awt.Container mainPanel;
    private javax.swing.JMenuItem mochaVisualStyleMenuItem;
    private javax.swing.JMenuItem selectFontMenuItem;
    private static volatile javax.swing.JDialog setFontDialog;
    private static javax.swing.JButton setFontDialogApplyButton;
    private static javax.swing.JButton setFontDialogCancelButton;
    private static javax.swing.JButton setFontDialogOKButton;
    private static volatile javax.swing.JDialog settingsDialog;
    private javax.swing.JMenuItem settingsMenuItem;
    protected static javax.swing.JTabbedPane settingsTabbedPane;
    private javax.swing.JMenu toolsMenu;
    private static volatile javax.swing.JDialog updateDialog;
    private javax.swing.JButton updateDialogButton;
    private javax.swing.JProgressBar updateDialogProgressBar;
    private javax.swing.JLabel updateDialogTextLabel;
    private javax.swing.JCheckBox useAnimationsCheckBox;
    private javax.swing.JMenu viewMenu;
    private javax.swing.JMenu visualStylesMenu;
    // End of variables declaration//GEN-END:variables
  
  public void fixStyleInAllWindows()
  {
    try
    {
      mC.fixStyleInAllWindows(mC.getStyle(), getSelectedFontName(), getSelectedFontStyle(), getSelectedFontMod(), prints);
      jPanel1.setMinimumSize(jPanel1.getPreferredSize());
      for (java.awt.Window w : java.awt.Window.getWindows())
        w.setMinimumSize(w.getPreferredSize());
    }
    catch (Throwable t)
    {
      alertOf(t);
    }
    
    repaint();
  }
  
  /**
   * Sets whether the application closes when the window is closed. Does not affect the behavior of the Exit menu item. Setting
   * the default close operation to {@code HIDE_ON_CLOSE} overrides the input of this method.<br/>
   * <br/>
   * defaults to {@code true}
   * @param shouldExit {@code true} if you want the application to exit upon closing this window.
   */
  public void setAlwaysExitsOnClose(boolean shouldExit)
  {
    this.shouldExit = shouldExit;
  }
  
  /**
   * Returns whether the application closes when the window is closed. Setting the default close operation to
   * {@code HIDE_ON_CLOSE} overrides the result of this method.
   * @return {@code true} if you the application is to exit upon closing this window.
   */
  public boolean getAlwaysExitsOnClose()
  {
    return shouldExit;
  }

  private BHFrame getThis()
  {
    return this;
  }

  private void reloadFonts()
  {
    new Thread(new Runnable() 
    {
      @Override
      public void run()
      {
        alertOf("Loading font list...", Constants.INFO_MES);
        
        fontSearchTextField.setEnabled(false);
        jButton1.setEnabled(false);
        boldStyleCheckBox.setEnabled(false);
        italicStyleCheckBox.setEnabled(false);
        fontSizeSpinner.setEnabled(false);
        fontSizePlusDefaultCheckBox.setEnabled(false);
        setFontDialogOKButton.setEnabled(false);
        setFontDialogApplyButton.setEnabled(false);
        
        fontAPP = new ArrayPP<>(java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        
        fontList.setModel(new javax.swing.AbstractListModel<String>()
        {
          private static final long serialVersionUID = 1L;
          @Override
          public int getSize()
          {
            return fontAPP.length();
          }

          @Override
          public String getElementAt(int index)
          {
            return fontAPP.get(index);
          }
        });
        
        fontList.setSelectedValue(Colors.DEFAULT_FONT, true);
        
        fontSearchTextField.setEnabled(true);
        jButton1.setEnabled(true);
        boldStyleCheckBox.setEnabled(true);
        italicStyleCheckBox.setEnabled(true);
        fontSizeSpinner.setEnabled(true);
        fontSizePlusDefaultCheckBox.setEnabled(true);
        setFontDialogOKButton.setEnabled(true);
        setFontDialogApplyButton.setEnabled(true);
        
        alertOf("Font list loaded!", Constants.INFO_MES);
      }
    }, "Font list loader").start();
  }

  private void refreshExampleFont()
  {
    testColors.fixStyleIn(exampleFontLabel, testColors.getStyle(), getSelectedFontName(), getSelectedFontStyle(),
        getSelectedFontMod(), prints || true);
  }
  
  private String getSelectedFontName()
  {
    return fontList.getSelectedValue() == null ? Constants.DEF_FONT_NAME : fontList.getSelectedValue().toString();
  }
  
  private int getSelectedFontStyle()
  {
    return (boldStyleCheckBox.isSelected() ? java.awt.Font.BOLD : 0) +
        (italicStyleCheckBox.isSelected() ? java.awt.Font.ITALIC : 0);
  }
  
  public double getSelectedFontMod()
  {
    return fontSizeSpinner.parseValueToDouble() - (fontSizePlusDefaultCheckBox.isSelected() ? 0 : Colors.DEFAULT_SIZE);
  }

  private void applyFontChanges()
  {
    saveableFontName.setState(getSelectedFontName());
    saveableFontMod.setState(getSelectedFontMod());
    saveableFontStyle.setState((long)getSelectedFontStyle());
    saveablePlusDefaultFontSize.setState(fontSizePlusDefaultCheckBox.isSelected());
    fixStyleInAllWindows();
  }

  private void cancelFontChanges()
  {
    fontList.setSelectedValue(saveableFontName.getState(), true);
    fontSizeSpinner.setValue(saveableFontMod.getState());
    boldStyleCheckBox.setSelected((saveableFontStyle.getState() & java.awt.Font.BOLD) != 0);
    italicStyleCheckBox.setSelected((saveableFontStyle.getState() & java.awt.Font.ITALIC) != 0);
    fontSizePlusDefaultCheckBox.setSelected(saveablePlusDefaultFontSize.getState());
  }

  @SuppressWarnings("deprecation")
  private void searchFontList()
  {
    if (fontSearchThread != null)
      fontSearchThread.stop();
        
    (fontSearchThread = new Thread(new Runnable() 
    {
      @Override
      public void run()
      {
        alertOf("Loading font list...", Constants.INFO_MES);
        
        boolean b = fontSearchTextField.getText().isEmpty();
        
        final ArrayPP<String> iso = b ? fontAPP : fontAPP.isolate(fontSearchTextField.getText());
        
        fontList.setModel(new javax.swing.AbstractListModel<String>()
        {
          private static final long serialVersionUID = 1L;
          @Override
          public int getSize()
          {
            return iso.length();
          }

          @Override
          public String getElementAt(int index)
          {
            return iso.get(index);
          }
        });
        
        int matches = iso.length();
        alertOf(b ? "Font list restored" : "Found " + matches + " match" + (matches == 1 ? "" : "es") + " for \"" +
            fontSearchTextField.getText() + "\" in font list!", Constants.INFO_MES);
      }
    }, "bht.tools.comps.BHFrame-FontListSearcher")).start();
  }

  @Override
  public ArrayPP<Keyword> getKeywords()
  {
    return new ArrayPP<>(new Searchable.Keyword(TITLE), new Searchable.Keyword(searchTitle), new Searchable.Keyword(VERSION));
  }

  @Override
  public double getMatchStrength(Searchable otherSearchable)
  {
    if (otherSearchable == this)
      return 1;
    
    double strength = 0.5;
    
    if (otherSearchable.getSearchTitle().toString().equalsIgnoreCase(getTitle()) ||
        new StringPP(otherSearchable.getSearchTitle()).containsIgnoreCase(getTitle()) ||
        new StringPP(getTitle()).containsIgnoreCase(otherSearchable.getSearchTitle()))
      strength = Math.pow(strength, 0.25);
    
    if (otherSearchable.getKeywords().containsAll(getKeywords().toArray()))
      strength = Math.pow(strength, 0.25);
    else if (otherSearchable.getKeywords().containsAny(getKeywords().toArray()))
    {
      for (int i=0; i < getKeywords().length(); i++)
        if (otherSearchable.getKeywords().contains(getKeywords().get(i)))
          strength = Math.pow(strength, 0.05);
    }
    else
      strength = Math.pow(strength, 2);
    
    if (otherSearchable instanceof JFrame)
    {
      for (int i = 0; i < getComponentCount(); i++)
        if (new ArrayPP</*java.awt.Component*/>(((JFrame) otherSearchable).getComponents()).contains(getComponents()[i]))
          strength = Math.pow(strength, 0.05);

      for (int i = 0; i < ((JFrame) otherSearchable).getComponentCount(); i++)
        if (new ArrayPP</*java.awt.Component*/>(getComponents()).contains(((JFrame) otherSearchable).getComponents()[i]))
          strength = Math.pow(strength, 0.05);
    }

    if (getSearchDisplay() == otherSearchable.getSearchDisplay())
      strength = Math.pow(strength, 0.05);
    else if (getSearchDisplay().equals(otherSearchable.getSearchDisplay()))
      strength = Math.pow(strength, 0.15);
    
    return strength;
  }

  @Override
  public Object getSearchDisplay()
  {
    return this;
  }

  @Override
  public int compareTo(Searchable otherSearchable)
  {
    return (int)compareTo64(otherSearchable);
  }

  @Override
  public long compareTo64(Searchable otherSearchable)
  {
    return (long)(getMatchStrength(otherSearchable) * 200) - 100;
  }

  @Override
  public Searchable setSearchDisplay(Object display)
  {
    throw new UnsupportedOperationException("The frame IS the display");
  }

  @Override
  public Searchable setSearchTitle(CharSequence title)
  {
    searchTitle = title;
    if (super.getTitle() == null || super.getTitle().isEmpty())
      super.setTitle(title.toString());
    return this;
  }

  @Override
  public CharSequence getSearchTitle()
  {
    return searchTitle == null || searchTitle.length()==0 ? super.getTitle() : searchTitle;
  }
  
  @Override
  public void setTitle(String title)
  {
    TITLE = title;
    
    custColorsEditDialog.setTitle("Custom Colors Editor - " + title);
    settingsDialog.setTitle("Settings - " + title);
    setFontDialog.setTitle("Font - " + title);
    
    if (super.getTitle() == null || super.getTitle().isEmpty())
      setSearchTitle(title);//Why did I do this? There musta been a reason...
    super.setTitle(title);
  }
  
  @Override
  public String getTitle()
  {
    return super.getTitle() == null || super.getTitle().isEmpty() ? getSearchTitle().toString() : super.getTitle();
  }

  public void setUsesMenu(byte menuMask, boolean b)
  {
    getMenu(menuMask).setVisible(b);
  }

  public boolean usesMenu(byte menuMask)
  {
    return getMenu(menuMask).isVisible();
  }

  @Override
  public String getStringToSave()
  {
    String s = Character.toString(SEP), r = getTitle() + s;
    r += Numbers.between(getX(), 0, CompAction.SCREEN_BOUNDS.width) + s;
    r += Numbers.between(getY(), 0, CompAction.SCREEN_BOUNDS.height) + s;
    r += Numbers.between(getWidth(), 0, CompAction.SCREEN_BOUNDS.width) + s;
    r += Numbers.between(getHeight(), 0, CompAction.SCREEN_BOUNDS.height) + s;
    r += settings.getProperty(SETTING_ANIM.toString()) + s;
    r += getState() + s;
    return r;
  }

  @Override
  public Saveable loadFromSavedString(CharSequence savedString)
  {
    if (savedString == null)
      return this;
    String s = savedString.toString(),c;
    int b=0,e=s.indexOf(SEP), x,y,w,h;
    if (e!=-1)setTitle(s.substring(b, e));
    b=e+1; e=s.indexOf(SEP, b);
    
    if (e!=-1){x = Integer.parseInt(s.substring(b, e));
    b=e+1; e=s.indexOf(SEP, b);
    if (e!=-1){y = Integer.parseInt(s.substring(b, e));
    b=e+1; e=s.indexOf(SEP, b);
    if (e!=-1){w = Integer.parseInt(s.substring(b, e));
    b=e+1; e=s.indexOf(SEP, b);
    if (e!=-1){h = Integer.parseInt(s.substring(b, e));
    setBounds(x, y, w, h);}}}}
    
    b=e+1; e=s.indexOf(SEP, b);
    if (e!=-1)setShouldAnimate(s.substring(b, e).equals(TRUE_STR));
    b=e+1; e=s.indexOf(SEP, b);
    if (e!=-1)setState(Integer.parseInt(s.substring(b, e)));
    return this;
  }
  
  @Override
  public CharSequence getSaveName()
  {
    return saveName;
  }
  
  @Override
  public BHFrame setSaveName(CharSequence newSaveName)
  {
    saveName = newSaveName;
    return this;
  }

  /**
   * Returns whether this {@link BHFrame} is set to use animations
   * @return a {@code boolean} signifying whether this {@link BHFrame} is set to use animations
   */
  public boolean shouldUseAnimations()
  {
    return saveableUseAnims.getState();
  }
  
  /**
   * Tests whether the setting for using animations is set, and switches the internal {@code boolean}variable accordingly
   * @return this {@link BHFrame}
   */
  public BHFrame testAnimations()
  {
    CharSequence s;
    saveableUseAnims.setState((s = getSetting(SETTING_ANIM)) != null && s.equals(TRUE_STR));
    return this;
  }

  /**
   * Returns whether anything is set as this setting.
   * 
   * @param SETTING the {@link CharSequence}to search for in the settings
   * @return {@code true} if this setting has any value whatsoever 
   */
  public boolean settingIsSet(final CharSequence SETTING)
  {String s;
    return (s = settings.getProperty(SETTING.toString())) != null && !s.isEmpty();
  }

  /**
   * Displays the given {@link Window}, adhering to the system- and user-specified transparency and animation rules
   * @param w the window to be displayed
   */
  public void openWindow(java.awt.Window w)
  {
    openWindow(w, null);
  }

  /**
   * Displays the given {@link Window}, adhering to the system- and user-specified transparency and animation rules
   * @param w the window to be displayed
   * @param endAction the action to be performed at the end of the animation.
   */
  public void openWindow(java.awt.Window w, java.awt.event.ActionListener endAction)
  {
    if (shouldUseAnimations())
    {
      if (useTransparency)
        (w instanceof BHComponent ? ((BHComponent)w).getCompAction() : new CompAction()).ghostIn(w, endAction);
      else
        (w instanceof BHComponent ? ((BHComponent)w).getCompAction() : new CompAction()).slideOpen(w, w.getSize(),
          CompAction.DEF_BRAKE, CompAction.DEF_FPS, null, endAction);
    } 
    else if (useTransparency)
      (w instanceof BHComponent ? ((BHComponent)w).getCompAction() : new CompAction()).fadeIn(w, endAction);
    else
    {
      w.setVisible(true);
      if (endAction != null)
        endAction.actionPerformed(new java.awt.event.ActionEvent(this, 0, null, System.currentTimeMillis(), 0));
    }
  }

  /**
   * Hides the given {@link Window}, adhering to the system- and user-specified transparency and animation rules
   * @param w the window to be hidden
   */
  public void closeWindow(java.awt.Window w)
  {
    closeWindow(w, false);
  }

  /**
   * Hides the given {@link Window}, adhering to the system- and user-specified transparency and animation rules
   * @param w the window to be hidden
   * @param exit if {@code true}, the program will exit after the given window has closed
   */
  public void closeWindow(java.awt.Window w, boolean exit)
  {
    try
    {
      if (useTransparency)
        (w instanceof BHComponent ? ((BHComponent)w).getCompAction() : new CompAction()).ghostOut(w, exit, 0);
      else if (shouldUseAnimations())
        (w instanceof BHComponent ? ((BHComponent)w).getCompAction() : new CompAction()).slideClosed(w, exit, 0);
      else
      {
        w.setVisible(false);
        if (exit)
          System.exit(0);
      }
    }
    catch (Exception ex)
    {
      alertOf(ex);
      if (YesNoBox.bool("A minor error has occurred when closing the window.\nWould you like to exit for safety?",
                        ex.getClass().getSimpleName() + " - " + TITLE, YesNoBox.WARNING_MES))
        System.exit(ex.hashCode());
      w.setVisible(false);
    }
    catch (Error er)
    {
      alertOf(er);
      javax.swing.JOptionPane.showMessageDialog(null, "A major error has occurred when closing the window.\nNow exiting for safety reasons",
                        er.getClass().getSimpleName() + " - " + TITLE, YesNoBox.WARNING_MES, Icons.getIcon(Icons.ERROR_32));
        System.exit(er.hashCode());
    }
  }
  
  /**
   * Replaces the currently used {@link StateSaver} with the given one
   * @param newStateSaver the new {@link StateSaver} to use with this {@link BHFrame}
   * @return {@code this} {@code BHFrame}
   */
  public BHFrame setStateSaver(StateSaver newStateSaver)
  {
    settingsSaver = newStateSaver;
    try
    {
//      settingsSaver.addSaveable(this);
      settingsSaver.addSaveable(saveableFontMod);
      settingsSaver.addSaveable(saveableFontName);
      settingsSaver.addSaveable(saveableFontStyle);
      settingsSaver.addSaveable(saveableLAFSelection);
      settingsSaver.addSaveable(saveablePlusDefaultFontSize);
      settingsSaver.addSaveable(saveableUseAnims);
      settingsSaver.loadState();
      settingsSaver.putAllStates();
    }
    catch (IOException ex)
    {
      alertOf(ex);
    }
    return this;
  }

  /**
   * Returns the set value of the given {@code SETTING}, or {@code null} if no such setting exists
   * @param SETTING the setting whose set value is to be gotten
   * @return the set value of {@code SETTING}, or {@code null} if no such setting exists
   * @throws NullPointerException if no {@link StateSaver} has been specified or if creation was unsuccessful.
   * @see #setStateSaver
   */
  public CharSequence getSetting(final CharSequence SETTING) throws NullPointerException
  {
    return settingsSaver.getState(SETTING);
  }

  /**
   * Tells this {@link BHFrame} whether it should use component animations
   * @param shouldAnimate a {@code boolean} representing whether this {@code BHFrame} should use component animations
   * @return this {@code BHFrame}
   */
  public BHFrame setShouldAnimate(boolean shouldAnimate)
  {
     useAnimationsCheckBox.setSelected(shouldAnimate);
     return this;
  }
  
  /**
   * Adds a panel to the settings dialog
   * @param settingsPanel the panel to be added
   * @param settingsTitle the title of the panel
   * @return this {@link BHFrame}
   */
  public BHFrame addSettingsPanel(java.awt.Container settingsPanel, CharSequence settingsTitle)
  {
    settingsTabbedPane.add(settingsTitle.toString(), settingsPanel);
    settingsDialog.pack();
    settingsDialog.setMinimumSize(Numbers.min(settingsDialog.getPreferredSize(), CompAction.SCREEN_BOUNDS.getSize(),
        (byte)(Numbers.BEHAVIOR_HEIGHT + Numbers.BEHAVIOR_WIDTH)));
    return this;
  }

  /**
   * Refreshes the variable that the application uses to see if Transparency is supported. Usually doesn't change it.
   * @return this {@link BHFrame}
   */
  public BHFrame testTransparency()
  {
    return setShouldUseTransparency(CompAction.testTranslucencySupport());
  }

  public boolean shouldUseTransparency()
  {
    return useTransparency;
  }

  public BHFrame setShouldUseTransparency(boolean b)
  {
    useTransparency = b;
    return this;
  }
  
  public StateSaver getStateSaver()
  {
    return settingsSaver;
  }

  private void checkForUpdates()
  {
    new Thread(new Runnable()
    {
      @Override
      public void run()
      {
        checkForUpdatesMenuItem.setEnabled(false);
        updateDialogButton.setVisible(false);
        java.io.InputStream is = null;
        java.net.URLConnection urlc;
        try
        {
          String s;
          final StringPP ver = new StringPP();
          updateDialogTextLabel.setText(s = "Checking for updates...");
          alertOf(s, Constants.INFO_MES);
          updateDialogProgressBar.setValue(0);
          updateDialogProgressBar.setVisible(true);
          updateDialogProgressBar.setStringPainted(false);
          updateDialogProgressBar.setIndeterminate(true);
          urlc = latestVerURL.openConnection();
          urlc.connect();
          is = urlc.getInputStream();
          
          updateDialogProgressBar.setMaximum(Math.max(urlc.getContentLength(), is.available()));
          updateDialogProgressBar.setStringPainted(true);
          updateDialogProgressBar.setIndeterminate(false);
          for (int i, prog=0;(i = is.read()) != -1; prog++)
          {
            ver.append((char) i);
            updateDialogProgressBar.setValue(prog);
            updateDialogProgressBar.setString((byte)(updateDialogProgressBar.getPercentComplete() * 100) + "%");
          }
          updateDialogProgressBar.setValue(updateDialogProgressBar.getMaximum());
          updateDialogProgressBar.setString("100%");
          
//          System.out.println("Fetched/This : " + ver + "/" + VERSION);
          String n = "You have the latest version.";
          if (ver.equals(VERSION))
          {
            updateDialogProgressBar.setVisible(false);
            updateDialogTextLabel.setText(n);
            
            try
            {
              new CompAction().morphTo(updateDialog,
                  new java.awt.Rectangle(updateDialog.getX(), updateDialog.getY(),
                    Math.max(updateDialog.getPreferredSize().width, updateDialog.getWidth()),
                    Math.max(updateDialog.getPreferredSize().height, updateDialog.getHeight())),
                  0, CompAction.DEF_BRAKE, CompAction.DEF_FPS, false, false, null, 
                  new java.awt.event.ActionListener()
                      {
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {
                          updateDialog.setMinimumSize(updateDialog.getPreferredSize());
                        }
                      }, false, 0);
              is.close();
            }
            catch (Throwable t)
            {
              alertOf(t);
            }
            return;
          }
          boolean x = false;
          String V = VERSION.toString();
          for (int i1 = ver.indexOf('.'), i2 = V.indexOf('.'),
               l1 = ver.length(), l2 = V.length(),
               p1 = 0, p2 = 0, c;
               i1 < l1 && i2 < l2;
               p1=++i1, p2=++i2,
               i1 = ver.indexOf('.', i1), i2 = V.indexOf('.', i2),
               x = i1 == -1 || i2 == -1)
          {
            if (i1==-1)
              i1=l1;
            if (i2==-1)
              i2=l2;
            if ((c = ver.substring(p1, i1).compareTo(V.substring(p2, i2))) < 0)//Fetched version is higher
            {
              updateDialogTextLabel.setText(s = "An update is available");
              alertOf(s, Constants.WARNING_MES);
              updateDialogButton.removeActionListener(updRestartActionListener);
              updateDialogButton.addActionListener(updDLActionListener = new java.awt.event.ActionListener()
              {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                  applyUpdate(ver);
                }
              });
              updateDialogButton.setText("Click here to update from " + V + " to " + ver);
              updateDialogButton.setVisible(true);
              break;
            }
            if (c > 0)//This version is higher
            {
              updateDialogTextLabel.setText(s = "You are using the latest prerelease");
              alertOf(s, Constants.INFO_MES);
              break;
            }
            if (x)//There is no more string to evaluate
            {
              updateDialogTextLabel.setText(n);
              alertOf(n, Constants.INFO_MES);
              break;
            }
          }
        }
        catch (java.io.IOException ex)
        {
          updateDialogTextLabel.setText("Could not connect to update server. \r\nTry manually downloading the update.");
          updateDialogProgressBar.setVisible(false);
          updateDialogButton.removeActionListener(updDLActionListener);
          updateDialogButton.setText("Click here to try to manually download the update.");
          updateDialogButton.addActionListener(updManualActionListener = new java.awt.event.ActionListener() 
          {
            @Override
            public void actionPerformed(ActionEvent e)
            {
              try
              {
                java.awt.Desktop.getDesktop().browse(latestUpdURL.toURI());
              }
              catch (URISyntaxException | IOException t)
              {
                t.printStackTrace();
                StringPP c = new StringPP(t.getClass().getSimpleName()), m = new StringPP(t.getMessage());
                javax.swing.JOptionPane.showMessageDialog(updateDialog, "Encountered a" + (c.startsWithVowel() ? "n " : " ")
                    + c + " occurred when trying to browse to the URL.\n\n" + m, c + " - " + TITLE, Constants.ERROR_MES,
                    Icons.getIcon(Icons.ERROR_32));
                closeWindow(updateDialog);
              }
              catch (Throwable t)
              {
                t.printStackTrace();
                StringPP c = new StringPP(t.getClass().getSimpleName()), m = new StringPP(t.getMessage());
                javax.swing.JOptionPane.showMessageDialog(updateDialog, "Unexpectedly encountered a" +
                    (c.startsWithVowel() ? "n " : " ") + c + " occurred when trying to browse to the URL.\n\n" + m, c + " - "
                    + TITLE, Constants.ERROR_MES, Icons.getIcon(Icons.ERROR_32));
                closeWindow(updateDialog);
              }
            }
          });
          updateDialogButton.setVisible(true);
          new CompAction().morphTo(updateDialog,
              new java.awt.Rectangle(updateDialog.getX(), updateDialog.getY(),
                Math.max(updateDialog.getPreferredSize().width, updateDialog.getWidth()),
                Math.max(updateDialog.getPreferredSize().height, updateDialog.getHeight())),
              0, CompAction.DEF_BRAKE, CompAction.DEF_FPS, false, false, null,
              new java.awt.event.ActionListener()
                  {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                      updateDialog.setMinimumSize(updateDialog.getPreferredSize());
                    }
                  }, false, 0);
        }
        catch (Throwable t)
        {
          t.printStackTrace();
          StringPP c = new StringPP(t.getClass().getSimpleName()), m = new StringPP(t.getMessage());
          javax.swing.JOptionPane.showMessageDialog(updateDialog, "A" + (c.startsWithVowel() ? "n " : " ") + c + " occurred when trying to check for updates:\n\n" + m, c + " - " + TITLE, Constants.ERROR_MES, Icons.getIcon(Icons.ERROR_32));
          closeWindow(updateDialog);
        }
        
        try
        {
          if (is != null)//Added Mar 11, 2012 (1.2.15) for Marian
            is.close();
        }
        catch (Throwable t)
        {
          alertOf(t);
        }
        checkForUpdatesMenuItem.setEnabled(true);
      }
    }, "UpdateCheck").start();
  }

  private void applyUpdate(final CharSequence ver)
  {
    new Thread(new Runnable()
    {
      @Override
      public void run()
      {
        checkForUpdatesMenuItem.setEnabled(false);
        
        java.io.InputStream is;
        java.io.OutputStream os;
        java.net.URLConnection urlc;
        try
        {
          String s;
          updateDialogTextLabel.setText(s = "Downloading updates...");
          alertOf(s, Constants.INFO_MES);
          updateDialogProgressBar.setValue(0);
          updateDialogProgressBar.setVisible(true);
          updateDialogProgressBar.setIndeterminate(true);
          urlc = latestUpdURL.openConnection();
          urlc.connect();
          is = urlc.getInputStream();
          updateDialogProgressBar.setMaximum(Math.max(urlc.getContentLength(), is.available()));
          updateDialogProgressBar.setStringPainted(true);
          updateDialogProgressBar.setIndeterminate(false);
          final java.io.File f = new java.io.File(System.getProperty("user.dir") + java.io.File.separatorChar + TITLE + " " + ver + ".jar");
          os = new java.io.FileOutputStream(f, false);
          for (int i, prog=0;(i = is.read()) != -1; prog++)
          {
            os.write(i);
            updateDialogProgressBar.setValue(prog);
            updateDialogProgressBar.setString((byte)(updateDialogProgressBar.getPercentComplete() * 100) + "%");
          }
          updateDialogProgressBar.setValue(updateDialogProgressBar.getMaximum());
          updateDialogProgressBar.setString("100%");
          os.flush();
          os.close();
          is.close();
          updateDialogProgressBar.setStringPainted(false);
          updateDialogTextLabel.setText("Update successfully downloaded!");
          updateDialogButton.removeActionListener(updDLActionListener);
          updateDialogButton.addActionListener(updRestartActionListener = new java.awt.event.ActionListener()
              {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                  try
                  {
                    java.awt.Desktop.getDesktop().browse(f.toURI());
                  }
                  catch (Throwable t)
                  {
                    t.printStackTrace();
                    StringPP c = new StringPP(t.getClass().getSimpleName());
                    javax.swing.JOptionPane.showMessageDialog(updateDialog,
                        "Could not open the new version due to a" + (c.startsWithVowel() ? "n " : " ") + c + ":\n\n" +
                            t.getMessage() + "\n\nPlease open the JAR manually from your file browser.",
                        c + " - " + TITLE, Constants.ERROR_MES, Icons.getIcon(Icons.ERROR_32));
                  }
                  exit(0);
                }
              });
          updateDialogButton.setText("Close this version and open the new one");
          updateDialogButton.setVisible(true);
          new CompAction().morphTo(updateDialog,
              new java.awt.Rectangle(updateDialog.getX(), updateDialog.getY(),
                Math.max(updateDialog.getPreferredSize().width, updateDialog.getWidth()),
                Math.max(updateDialog.getPreferredSize().height, updateDialog.getHeight())),
              0, CompAction.DEF_BRAKE, CompAction.DEF_FPS, false, false, null,
              new java.awt.event.ActionListener()
                  {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                      updateDialog.setMinimumSize(updateDialog.getPreferredSize());
                    }
                  }, false, 0);
        }
        catch (Throwable t)
        {
          t.printStackTrace();
          StringPP c = new StringPP(t.getClass().getSimpleName()), m = new StringPP(t.getMessage());
          javax.swing.JOptionPane.showMessageDialog(updateDialog, "A" + (c.startsWithVowel() ? "n " : " ") + c + " occurred "
              + "when trying download the update:\n\n" + m, c + " - " + TITLE, Constants.ERROR_MES, Icons.getIcon(Icons.ERROR_32));
          closeWindow(updateDialog);
        }
        
        checkForUpdatesMenuItem.setEnabled(true);
      }
    }, "Updater").start();
  }
  
  public BHFrame setLogo(Image newLogo)
  {
    aboutLogoImage.setImage(newLogo);
    return this;
  }
  
  public BHFrame setLogo(Icon newLogo)
  {
    aboutLogoImage.setImage(newLogo);
    return this;
  }
  
  /**
   * Sets the logger
   * @param LOG the new logger
   * @since Feb 28, 2012 (1.2.14) for Marian
   */
  public void setLog(ProgLog LOG)
  {
    log = LOG;
  }
  
  
  /**
   * if {@code b} is {@code true}, then this program will allow the user to check SupuhServer for updates
   * @since Mar 11, 2012 (1.2.15) for Marian
   * @param b 
   */
  public void setCanUpdate(boolean b)
  {
    checkForUpdatesMenuItem.setVisible(b);
  }
}
