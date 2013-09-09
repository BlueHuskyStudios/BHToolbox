package bht.tools.comps.gameboard;

import bht.tools.comps.event.StateChangeEvent;
import bht.tools.comps.event.StateChangeListener;
import bht.tools.util.ArrayPP;

/**
 * BHBSSelectionManager, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012. License is default.<hr/>
 * @author Supuhstar of Blue Husky Programming
 * @since Jan 20, 2012
 * @version 1.0.1
 * @deprecated doesn't work?
 */
public class BHBSSelectionManager //NOTE: Must be compiled in UTF-8
{
  private ArrayPP<BHBoardSquare> squares;
  private ArrayPP<Integer> selOrder;
  private boolean deselecting = false;
  private int maxSelect;

//  public BHBSSelectionManager()
//  {
//    this(null);
//  }
  
  /**
   * Creates a new selection manager that ensures that only one of the given squares are selected at any given time.
   * @param squaresToManage the squares that will be managed as described above
   */
  public BHBSSelectionManager(BHBoardSquare... squaresToManage)
  {
    this (1, squaresToManage);
  }

  /**
   * Creates a new selection manager that ensures that only {@code maxSquaresToBeSelected} of the given squares are selected at
   * any given time.
   * @param maxSquaresToBeSelected the maximum number of squares to be selected
   * @param squaresToManage the squares that will be managed as described above
   */
  public BHBSSelectionManager(int maxSquaresToBeSelected, BHBoardSquare... squaresToManage)
  {
    maxSelect = maxSquaresToBeSelected;
    selOrder = new ArrayPP<>();
    addSquares(squaresToManage);
  }

  /**
   * Adds the given squares to the list of squares to manage, then adds a special selection listener to each one that ensures
   * that no more than {@link #maxSelect} squares are selected at a time
   * @param squaresToManage
   * @return {@code this} 
   */
  public BHBSSelectionManager addSquares(BHBoardSquare... squaresToManage)
  {
    if (squaresToManage == null)
      return this;
    if (squares == null)
      squares = new ArrayPP<>(squaresToManage);
    else
      squares.add(squaresToManage);
    
    for (int l=squares.length(), i = l - squaresToManage.length; i < l; i++)
    {
      final int I = i;
      squares.get(i).addStateChangeListener(new StateChangeListener()
      {
        @Override
        public void stateChanged(StateChangeEvent evt)
        {
          if (!deselecting && evt.getNewState())
          {
            selOrder.add(I);
            checkSelected();
          }
        }
      });
    }
    return this;
  }
  
  public BHBSSelectionManager checkSelected()
  {
    deselecting = true;
    while (selOrder.length() > maxSelect)
    {
      squares.get(selOrder.getFirstItem()).setSelected(false);
      squares.get(selOrder.getFirstItem()).repaint();
      selOrder.removeFirstItem();
    }
    deselecting = false;
    return this;
  }
}
