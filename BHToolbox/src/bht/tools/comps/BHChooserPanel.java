package bht.tools.comps;

import bht.resources.Icons;
import bht.tools.util.ArrayPP;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.ListModel;

/**
 * BHChooserPanel, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012. License is default.<hr/>
 * A panel that lets user and programmer work together to build a textual list
 * @author Supuhstar of Blue Husky Programming
 * @since Jan 31, 2012
 * @version 1.0.0
 */
public class BHChooserPanel extends BHPanel //NOTE: Must be compiled in UTF-8
{
  private ArrayPP<CharSequence> listArray;
  private JButton addItemButton, clearSelectionButton, clearListButton;
  private JList<CharSequence> list;
  private BHTextField inputTextField;
  
  public BHChooserPanel()
  {
    listArray = new ArrayPP<>();
    initGUI();
  }

  private void initGUI()
  {
    GridBagConstraints gbc = new GridBagConstraints();
    setLayout(new GridBagLayout());
    
    inputTextField = new BHTextField("Type input here");
    inputTextField.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        addItemButton.doClick(1);
      }
    });
    gbc.fill = GridBagConstraints.BOTH;
    gbc.gridx = gbc.gridy = 0;
    add(inputTextField, gbc);
    
    addItemButton = new JButton(Icons.getIcon(Icons.ADD_24));
    addItemButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e)
      {
        String text = inputTextField.getText();
        if (text != null && !text.isEmpty())
        {
          inputTextField.setText(null);
          list.setValueIsAdjusting(true);
          listArray.add(text);
          list.setModel(makeListModel());
          list.setValueIsAdjusting(false);
        }
      }
    });
    gbc.gridx++;
    add(addItemButton, gbc);
    
    clearSelectionButton = new JButton(Icons.getIcon(Icons.REMOVE_24));
    clearSelectionButton.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        list.setValueIsAdjusting(true);
        listArray.remove(list.getSelectedIndices());
        list.setModel(makeListModel());
        list.setValueIsAdjusting(false);
      }
    });
    gbc.gridy++;
    add(clearSelectionButton, gbc);
    
    clearListButton = new JButton(Icons.getIcon(Icons.NO_24));
    clearListButton.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        list.setValueIsAdjusting(true);
        listArray.clear();
        list.setModel(makeListModel());
        list.setValueIsAdjusting(false);
      }
    });
    gbc.gridy++;
    add(clearListButton, gbc);
    
    list = new JList<>();
    list.setModel(makeListModel());
    gbc.gridheight=2;
    gbc.gridx--;
    gbc.gridy--;
    gbc.weightx = gbc.weighty = 1;
    add(list, gbc);
  }

  public CharSequence[] getListContents()
  {
    CharSequence[] ret = new CharSequence[listArray.length()];
    for (int i=0; i < ret.length; i++)
      ret[i] = listArray.get(i);
    return ret;
  }

  private ListModel<CharSequence> makeListModel()
  {
    return listArray.toListModel();
  }
}
