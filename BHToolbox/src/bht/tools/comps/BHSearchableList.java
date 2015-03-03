package bht.tools.comps;

import bht.resources.Icons;
import bht.tools.util.ArrayPP;
import bht.tools.util.StringPP;
import bht.tools.util.search.DefDisplayableNeedle;
import bht.tools.util.search.Needle;
import bht.tools.util.search.Needle.Keyword;
import bht.tools.util.search.Searcher;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 * BHSearchableList, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012. License is default.<hr/>
 * @author Supuhstar of Blue Husky Programming
 * @since Feb 28, 2012
 * @version 1.0.0
 */
public class BHSearchableList extends JPanel //NOTE: Must be compiled in UTF-8
{
  private ArrayPP<Needle> fullArray, dispArray;
  private ArrayPP<ActionListener> als;
  private BHTextField searchField;
  private BHImageComp searchIcon;
  private JList<String> list;
  private Needle previousSearch;
  
  public BHSearchableList(Needle... initSearchables)
  {
    als = new ArrayPP<>();
    
    fullArray = new ArrayPP<>(initSearchables);
    dispArray = fullArray.clone();
    initGUI();
    
    clearSearch();
    fix();
  }

  private void initGUI()
  {
    setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH;
    
    list = new JList<>();
    list.addMouseListener(new MouseAdapter()
    {
      @Override
      public void mouseClicked(MouseEvent e)
      {
        System.out.println("Clicked " + e.getClickCount() + " times(s)!");
        if (e.getClickCount() >= 2)
        {
          ActionEvent evt = new ActionEvent(e.getSource(), e.getID(), null, e.getWhen(), e.getModifiersEx());
          doActionPerformed(evt);
        }
      }
    });
    list.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyReleased(KeyEvent e)
      {int c = e.getExtendedKeyCode();
        if (c == KeyEvent.VK_ENTER ||
            c == KeyEvent.VK_UP || c == KeyEvent.VK_DOWN || c == KeyEvent.VK_LEFT || c == KeyEvent.VK_RIGHT ||
            c == KeyEvent.VK_HOME || c == KeyEvent.VK_END)
        {
          ActionEvent evt = new ActionEvent(e.getSource(), e.getID(), null, e.getWhen(), e.getModifiersEx());
          doActionPerformed(evt);
        }
      }
    });
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.gridx = gbc.gridy = 0;
    gbc.weightx = gbc.weighty = 1;
    add(list, gbc);
    
    searchField = new BHTextField("Search...");
    searchField.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyReleased(KeyEvent e)
      {
        searchFor(searchField.getText());
      }
    });
    gbc.gridwidth = 1;
    gbc.gridx++;
    gbc.gridy++;
    gbc.weighty = 0;
    add(searchField, gbc);
    
    searchIcon = new BHImageComp(Icons.getIcon(Icons.FIND_16), BHImageComp.BEHAVIOR_DEFAULT);
    searchIcon.setMinimumSize(new Dimension(16, 16));
    searchIcon.addMouseListener(new MouseAdapter()
    {
      @Override
      public void mouseClicked(MouseEvent e)
      {
        searchField.grabFocus();
        searchFor(searchField.getText());
      }
    });
    gbc.gridx--;
    gbc.weightx = 0;
    add(searchIcon, gbc);
  }
  
  public BHSearchableList add(Needle... items)
  {
    fullArray.add(items);
    searchFor(previousSearch);
//    fix();
    return this;
  }
  
  public BHSearchableList remove(Needle... items)
  {
    fullArray.remove(items, true);
    searchFor(previousSearch);
//    fix();
    return this;
  }
  
  public Needle get(int index)
  {
    return fullArray.get(index);
  }
  
  public void searchFor(CharSequence cs)
  {
    System.out.println("Searching for \"" + cs + "\"...");
    ArrayPP<Keyword> ks = new ArrayPP<>();
    for (StringPP s : new StringPP(cs).getWords())
      ks.add(new Keyword(s));
    Needle s = new DefDisplayableNeedle(cs, ks);
    searchFor(s);
  }
  
  public void searchFor(Needle s)
  {
    previousSearch = s;
    
    if (s == null || s.getKeywords() == null || s.getKeywords().isEmpty() ||
        s.getSearchTitle() == null || s.getSearchTitle().length() == 0)
    {
      clearSearch();
      fix();
      return;
    }
    Searcher searcher = new Searcher();
    dispArray = searcher.smartSearch(fullArray, s);
    fix();
  }
  
  public void clearSearch()
  {
    dispArray = fullArray.clone();
  }

  public void fix()
  {
    list.setModel(new AbstractListModel<String>()
    {
      @Override
      public int getSize()
      {
        return dispArray.length();
      }

      @Override
      public String getElementAt(int index)
      {
        return dispArray.get(index).getSearchTitle().toString();
      }
    });
  }
  
  /**
   * Adds the given action listener to the list that fires when the user performs an action on the list (not search area)
   * @param al the {@link ActionListener} to add
   */
  public void addActionListener(ActionListener al)
  {
    als.add(al);
  }
  
  /**
   * Removes the first encountered instance of the given action listener from the list that fires when the user performs an
   * action on the list (not search area)
   * @param al the {@link ActionListener} to remove
   */
  public void removeActionListener(ActionListener al)
  {
    als.remove(al, true);
  }
  
  public void doActionPerformed(ActionEvent e)
  {
    for (ActionListener actionListener : als)
    {
      if (actionListener != null)
        actionListener.actionPerformed(e);
    }
  }

  public Needle getSelectedItem()
  {
    return dispArray.get(list.getSelectedIndex());
  }
}
