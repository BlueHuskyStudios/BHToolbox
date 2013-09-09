package bht.tools.comps.gameboard;

import bht.tools.comps.BHContainer;
import bht.tools.util.ArrayPP;
import bht.tools.util.ArrayTable;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

/**
 * BHBoardPanel, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012. Licence is default.<hr/>
 * @author Supuhstar of Blue Husky Programming
 * @since Jan 16, 2012
 * @version 1.0.2
 */
public class BHBoardPanel extends BHContainer //NOTE: Must be compiled in UTF-8
{
  protected ArrayTable<BHBoardSquare> board;
  public static final byte TOP = BHBoardSquare.STATE_TOP_OPEN,
                           BOTTOM = BHBoardSquare.STATE_BOTTOM_OPEN,
                           LEFT = BHBoardSquare.STATE_LEFT_OPEN,
                           RIGHT = BHBoardSquare.STATE_RIGHT_OPEN;
  public static final byte SHAPE_CIRCLE = BHBoardSquare.SHAPE_CIRCLE,
                           SHAPE_ELIPSE = BHBoardSquare.SHAPE_ELIPSE,
                           SHAPE_SQUARE = BHBoardSquare.SHAPE_SQUARE,
                           SHAPE_RECT = BHBoardSquare.SHAPE_RECT,
                           SHAPE_DEFAULT = BHBoardSquare.SHAPE_DEFAULT;
  
  /**
   * A dummy constructor for use by child classes for custom creation
   */
  protected BHBoardPanel()
  {
    
  }
  
  public BHBoardPanel(int width, int height)
  {
    board = new ArrayTable<>(width, height);
    
    setLayout(new GridLayout(height, width));
    for(int y = 0, x, yl = height, xl = width; y < yl; y++)
    {
      for (x = 0; x < xl; x++)
      {
        add(board.setCell(y, x, new BHBoardSquare()).getCell(x, y));
      }
    }
  }
  
  public BHBoardPanel addActionListenerToSquare(int x, int y, ActionListener al)
  {
    board.getCell(x, y).addActionListener(al);
    return this;
  }
  
  public BHBoardPanel removeActionListenerToSquare(int x, int y, ActionListener al)
  {
    board.getCell(x, y).removeActionListener(al);
    return this;
  }

  public BHBoardPanel setBoardSize(int width, int height)
  {
    while (board.getNumCols() > width)
      board.removeCol(width);
    while (board.getNumRows() > height)
      board.removeRow(height);
    
    while (board.getNumCols() < width)
    {
      board.addCol(new ArrayPP<BHBoardSquare>(height));
      for (int i = 0, l = board.getNumRows(), c = board.getNumCols() - 1; i < l; i++)
      {
        board.setCell(c, i, new BHBoardSquare());
      }
    }
    while (board.getNumRows() < height)
    {
      board.addRow(new ArrayPP<BHBoardSquare>(width));
      for (int i = 0, l = board.getNumCols(), r = board.getNumRows() - 1; i < l; i++)
      {
        board.setCell(r, i, new BHBoardSquare());
      }
    }
    return this;
  }

  public BHBoardPanel openSquare(int x, int y, byte newSquareState)
  {
    board.getCell(x, y).openSides(newSquareState);
    
    if ((newSquareState & TOP) != 0 && y > 0)
      board.getCell(x, y - 1).openSides(BOTTOM);
    
    if ((newSquareState & RIGHT) != 0 && x < board.getNumCols())
      board.getCell(x + 1, y).openSides(LEFT);
    
    if ((newSquareState & BOTTOM) != 0 && y < board.getNumRows())
      board.getCell(x, y + 1).openSides(TOP);
    
    if ((newSquareState & LEFT) != 0 && x > 0)
      board.getCell(x - 1, y).openSides(RIGHT);
    
    return this;
  }
  
  public int getBoardWidth()
  {
    return board.getNumCols();
  }
  
  public int getBoardHeight()
  {
    return board.getNumRows();
  }

  public BHBoardSquare getSquareAt(int x, int y)
  {
    return board.getCell(x, y);
  }

  public void setPieceShape(byte shapeMask)
  {
    for (ArrayPP<BHBoardSquare> a : board)
    {
      for (BHBoardSquare bhbs : a)
      {
        bhbs.setShape(shapeMask);
      }
    }
  }
  
  public void setSquareAt(int x, int y, BHBoardSquare newSquare)
  {
    board.setCell(y, x, newSquare);
  }
}
