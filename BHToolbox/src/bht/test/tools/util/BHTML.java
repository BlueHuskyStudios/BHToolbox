/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bht.test.tools.util;

import bht.tools.util.save.Saveable;

/**
 *
 * @author Supuhstar of Blue Husky Studios
 */
public class BHTML
{
  public static final String EXT = "BHTML";

  public static class Tag
  {
    private Type type;
    private String typeStr;
    
    public Tag(String type)
    {
      this.type = Type.getType(type);
      typeStr = this.type.ordinal() == Type.CUSTOM.ordinal() ? type : this.type.name();
    }
    
    public Type getType()
    {
      return type;
    }

    private boolean classMatchesTag(Saveable s, Tag compTag)
    {
      return s.getClass().getSimpleName().equals(compTag.getClassName());
    }

    private String getTypeString()
    {
      return typeStr;
    }

    public String getClassName()
    {
      return getClassName(this);
    }
    public static String getClassName(Tag tag)
    {
      if (tag.getType().ordinal() == Type.CUSTOM.ordinal())
        return tag.getTypeString();
      return Type.getClassName(tag.getType());
    }
    /**
     * An {@code enum} containing type tags of all the types of components BHTML is built to save
     */
    public static enum Type
    {
      CUSTOM,
      AWT_Button, AWT_Canvas, AWT_Checkbox, AWT_Choice, AWT_Container, AWT_Dialog, AWT_FileDialog, AWT_Frame, AWT_Label, AWT_List,
          AWT_Panel, AWT_ScrollPane, AWT_Scrollbar, AWT_TextArea, AWT_TextField, AWT_Window,
      JApplet, JButton, JCheckBox, JCheckBoxMenuItem, JColorChooser, JComboBox, JDesktopPane, JDialog, JEditorPane, JFileChooser,
          JFormattedTextField, JFrame, JInternalFrame, JLabel, JLayeredPane, JList, JMenu, JMenuBar, JMenuItem, JOptionPane,
          JPanel, JPasswordField, JPopupMenu, JProgressBar, JRadioButton, JRadioButtonMenuItem, JRootPane, JScrollBar,
          JScrollPane, JSeparator, JSlider, JSpinner, JSplitPane, JTabbedPane, JTable, JTextArea, JTextField, JTextPane,
          JToggleButton, JToolBar, JToolTip, JTree, JViewport, JWindow,
      BHFrame, BHInternalFrame, BHLabel, BHLinkLabel, BHMessagePanel, BHNotifierDialog, BHPanel, BHPasswordField, BHSpinner,
          BHTextArea, BHTextField, BHCustColorsEditPanel, BHBoardPanel, BHBoardSquare;
//      java.awt.Component c = new bht.tools.comps.gameboard.;



    public static String getClassName(Type type)
    {
      switch (type)
      {
        //<editor-fold defaultstate="collapsed" desc="AWT">
        case AWT_Button:
          return java.awt.Button.class.getSimpleName();
        case AWT_Canvas:
          return java.awt.Canvas.class.getSimpleName();
        case AWT_Checkbox:
          return java.awt.Checkbox.class.getSimpleName();
        case AWT_Choice:
          return java.awt.Choice.class.getSimpleName();
        case AWT_Container:
          return java.awt.Container.class.getSimpleName();
        case AWT_Dialog:
          return java.awt.Dialog.class.getSimpleName();
        case AWT_FileDialog:
          return java.awt.FileDialog.class.getSimpleName();
        case AWT_Frame:
          return java.awt.Frame.class.getSimpleName();
        case AWT_Label:
          return java.awt.Label.class.getSimpleName();
        case AWT_List:
          return java.awt.List.class.getSimpleName();
        case AWT_Panel:
          return java.awt.Panel.class.getSimpleName();
        case AWT_ScrollPane:
          return java.awt.ScrollPane.class.getSimpleName();
        case AWT_Scrollbar:
          return java.awt.Scrollbar.class.getSimpleName();
        case AWT_TextArea:
          return java.awt.TextArea.class.getSimpleName();
        case AWT_TextField:
          return java.awt.TextField.class.getSimpleName();
        case AWT_Window:
          return java.awt.Window.class.getSimpleName();
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Swing">
        case JApplet:
          return javax.swing.JApplet.class.getSimpleName();
        case JButton:
          return javax.swing.JButton.class.getSimpleName();
        case JCheckBox:
          return javax.swing.JCheckBox.class.getSimpleName();
        case JCheckBoxMenuItem:
          return javax.swing.JCheckBoxMenuItem.class.getSimpleName();
        case JColorChooser:
          return javax.swing.JColorChooser.class.getSimpleName();
        case JComboBox:
          return javax.swing.JComboBox.class.getSimpleName();
        case JDesktopPane:
          return javax.swing.JDesktopPane.class.getSimpleName();
        case JDialog:
          return javax.swing.JDialog.class.getSimpleName();
        case JEditorPane:
          return javax.swing.JEditorPane.class.getSimpleName();
        case JFileChooser:
          return javax.swing.JFileChooser.class.getSimpleName();
        case JFormattedTextField:
          return javax.swing.JFormattedTextField.class.getSimpleName();
        case JFrame:
          return javax.swing.JFrame.class.getSimpleName();
        case JInternalFrame:
          return javax.swing.JInternalFrame.class.getSimpleName();
        case JLabel:
          return javax.swing.JLabel.class.getSimpleName();
        case JLayeredPane:
          return javax.swing.JLayeredPane.class.getSimpleName();
        case JList:
          return javax.swing.JList.class.getSimpleName();
        case JMenu:
          return javax.swing.JMenu.class.getSimpleName();
        case JMenuBar:
          return javax.swing.JMenuBar.class.getSimpleName();
        case JMenuItem:
          return javax.swing.JMenuItem.class.getSimpleName();
        case JOptionPane:
          return javax.swing.JOptionPane.class.getSimpleName();
        case JPanel:
          return javax.swing.JPanel.class.getSimpleName();
        case JPasswordField:
          return javax.swing.JPasswordField.class.getSimpleName();
        case JPopupMenu:
          return javax.swing.JPopupMenu.class.getSimpleName();
        case JProgressBar:
          return javax.swing.JProgressBar.class.getSimpleName();
        case JRadioButton:
          return javax.swing.JRadioButton.class.getSimpleName();
        case JRadioButtonMenuItem:
          return javax.swing.JRadioButtonMenuItem.class.getSimpleName();
        case JRootPane:
          return javax.swing.JRootPane.class.getSimpleName();
        case JScrollBar:
          return javax.swing.JScrollBar.class.getSimpleName();
        case JScrollPane:
          return javax.swing.JScrollPane.class.getSimpleName();
        case JSeparator:
          return javax.swing.JSeparator.class.getSimpleName();
        case JSlider:
          return javax.swing.JSlider.class.getSimpleName();
        case JSpinner:
          return javax.swing.JSpinner.class.getSimpleName();
        case JSplitPane:
          return javax.swing.JSplitPane.class.getSimpleName();
        case JTabbedPane:
          return javax.swing.JTabbedPane.class.getSimpleName();
        case JTable:
          return javax.swing.JTable.class.getSimpleName();
        case JTextArea:
          return javax.swing.JTextArea.class.getSimpleName();
        case JTextField:
          return javax.swing.JTextField.class.getSimpleName();
        case JTextPane:
          return javax.swing.JTextPane.class.getSimpleName();
        case JToggleButton:
          return javax.swing.JToggleButton.class.getSimpleName();
        case JToolBar:
          return javax.swing.JToolBar.class.getSimpleName();
        case JToolTip:
          return javax.swing.JToolTip.class.getSimpleName();
        case JTree:
          return javax.swing.JTree.class.getSimpleName();
        case JViewport:
          return javax.swing.JViewport.class.getSimpleName();
        case JWindow:
          return javax.swing.JWindow.class.getSimpleName();
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="BH">
        case BHFrame:
          return bht.tools.comps.BHFrame.class.getSimpleName();
        case BHInternalFrame:
          return bht.tools.comps.BHInternalFrame.class.getSimpleName();
        case BHLabel:
          return bht.test.tools.comps.BHLabel.class.getSimpleName();
        case BHLinkLabel:
          return bht.tools.comps.BHLinkLabel.class.getSimpleName();
        case BHMessagePanel:
          return bht.tools.comps.BHMessagePanel.class.getSimpleName();
        case BHNotifierDialog:
          return bht.test.tools.comps._Legacy_BHNotifierDialog.class.getSimpleName();
        case BHPanel:
          return bht.tools.comps.BHContainer.class.getSimpleName();
        case BHPasswordField:
          return bht.tools.comps.BHPasswordField.class.getSimpleName();
        case BHSpinner:
          return bht.tools.comps.BHSpinner.class.getSimpleName();
        case BHTextArea:
          return bht.tools.comps.BHTextArea.class.getSimpleName();
        case BHTextField:
          return bht.tools.comps.BHTextField.class.getSimpleName();
        case BHCustColorsEditPanel:
          return bht.tools.comps.BHCustColorsEditPanel.class.getSimpleName();
        case BHBoardPanel:
          return bht.tools.comps.gameboard.BHBoardPanel.class.getSimpleName();
        case BHBoardSquare:
          return bht.tools.comps.gameboard.BHBoardSquare.class.getSimpleName();
        //</editor-fold>
        case CUSTOM:
          return null;
      }
      throw new EnumConstantNotPresentException(Type.class, type.name());
    }
      public static Type getType(String type)
      {
        for (Type t : values())
          if (type.equalsIgnoreCase(t.name()))
            return t;
        return CUSTOM;
      }
    }
  }
}
