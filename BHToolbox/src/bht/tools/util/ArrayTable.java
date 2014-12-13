/*
 * Major changes:
 * 2011/07/25 - Rewrote original methods to use more efficient structures and Java 7 improvements
 *              Added a new constructor that makes an ArrayTable out of a Map
 * 2013/07/30 - Added support for column names, row names
 */
package bht.tools.util;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Iterator;

/**
 * <strong>Copyright Blue Husky Programming, © 2011</strong><br/>
 * Like a 2D ArrayPP
 * @param <T> the type of value in the table
 * @author Supuhstar fo Blue Husky Programming
 * @version 1.6.0
 */
public class ArrayTable<T> implements Iterable<ArrayPP<T>>
{
  private ArrayPP<ArrayPP<T>> rows;
  private ArrayPP<Object> colKeys, rowKeys;

//  /**
//   * @deprecated Please use <tt>ArrayTable(ArrayPP<ArrayPP<T>> a)</tt>, instead.
//   */
  public ArrayTable()
  {
    this(new ArrayPP<ArrayPP<T>>());
  }

  /**
   * Makes a new <tt>ArrayTable</tt> of objects of type <tt>T</tt>, based on the given 2D <tt>ArrayPP</tt>
   * @param a the 2D ArrayPP base
   */
  public ArrayTable(ArrayPP<ArrayPP<T>> a)
  {
    rows = a.clone();
	colKeys = new ArrayPP<>(getNumCols());
	rowKeys = new ArrayPP<>(getNumRows());
  }

  @SuppressWarnings("OverridableMethodCallInConstructor")
  public ArrayTable(T[][] t)
  {
    rows = new ArrayPP<ArrayPP<T>>();
    colKeys = new ArrayPP<>();
    rowKeys = new ArrayPP<>();
    for (int y=0; y < t.length; y++)
      addRow(t[y]);
  }
  
  public ArrayTable(int width, int height)
  {
    colKeys = new ArrayPP<>();
    rowKeys = new ArrayPP<>();
    rows = new ArrayPP<>(height);
    for (int i=0; i < height; i++)
      rows.set(i, new ArrayPP<T>(width));
  }

  /**
   * Gets an item from the table
   * @param col an <tt>int</tt> representing the column in which the item is contained
   * @param row an <tt>int</tt> representing the row in which the item is contained
   * @return the desired item
   */
  public T getCell(int col, int row)
  {
    if (col < 0 || col > getNumCols() - 1 || row < 0 || row > getNumRows() - 1)
      throw new ArrayIndexOutOfBoundsException("Request out of bounds: (" + col + ", " + row + ")");
    return rows.get(row).get(col);
  }

  /**
   * Gets an item from the table
   * @param point a {@link TinyPoint} representing the row and column in which the item is contained
   * @return the desired item
   */
  public T getCell(TinyPoint point)
  {
    return getCell(point.getY(), point.getX());
  }

  /**
   * Sets an item in the table to be a certain value
   * @param row an <tt>int</tt> representing the row in which the item to be changed is contained
   * @param col an <tt>int</tt> representing the column in which the item to be changed is contained
   * @param newVal the new value of the item
   * @return this ArrayTable
   */
  public ArrayTable<T> setCell(int row, int col, T newVal)
  {
    rows.get(row).set(col, newVal);
    return this;
  }

  /**
   * Sets an item in the table to be a certain value
   * @param point a {@link TinyPoint} representing the column and row in which the item to be changed is contained
   * @param newVal the new value of the item
   * @return this ArrayTable
   */
  public ArrayTable<T> setCell(TinyPoint point, T newVal)
  {
    return setCell(point.getY(), point.getX(), newVal);
  }

  /**
   * Returns the row specified by the parameter <tt>row</tt>
   * @param row the index of the target row
   * @return the specified row
   */
  public ArrayPP<T> getRow(int row)
  {
    return rows.get(row);
  }

  /**
   * Returns the column specified by the parameter <tt>col</tt>
   * <h1>Warning!</h1>
   * <b>This is NOT meant for table modification, as it returns a COPY of the items</b>
   * @param col the index of the target column
   * @return the specified column
   */
  public ArrayPP<T> getCol(int col)
  {
    ArrayPP<T> ret = new ArrayPP<T>();
    for (int i=0; i < rows.length(); i++)
      ret.add(getCell(col, i));
    return ret;
  }

  /**
   * Creates and returns a <tt>String</tt> representation of this <tt>ArrayTable</tt>
   * @return a <tt>String</tt> representation of this <tt>ArrayTable</tt>
   * @see ArrayPP#toString()
   * @version 1.1.0
   */
  @Override
  public String toString()
  {
    String ret = ",";
	ret += colKeys.toString() + "\r\n";//Added 2013-07-30 (1.6.0) for BLISS
    for (int i=0; i < rows.length(); i++)
      ret += rowKeys.get(i) + "," + rows.get(i).toString() + "\r\n";
    return ret.length() == 0 ? ret : ret.substring(0, ret.length() - 1);
  }

  /**
   * Searches for the given items and returns an <tt>ArrayTable</tt> that contains all rows that have matches, without
   * duplicating a row due to multiple matches. The search algorithm works by taking each row individually as an
   * {@link ArrayPP} and adds the row to the new table if and only if the row's {@link ArrayPP#containsAll} method for the specified
   * item returns {@code true}.
   * @param item the item to be searched for
   * @return a new <tt>ArrayTable</tt> containing ONLY numRows that contain an item in the parameter(s)
   * @see ArrayPP#containsAll(T... items)
   */
  public ArrayTable<T> isolateRowsContainingAll(T... item)
  {
    ArrayTable<T> a = new ArrayTable<T>();

    for (ArrayPP<T> row : this)
    {
      for (int j = 0, l=row.length(); j < l - 1; j++)
        if (row.containsAll(item))
        {
          a.addRow(row);
          break;
        }
    }
    return a;
  }

  /**
   * Searches for the given items and returns an <tt>ArrayTable</tt> that contains any rows that have matches, without
   * duplicating a row due to multiple matches. The search algorithm works by taking each row individually as an
   * {@link ArrayPP} and adds the row to the new table if and only if the row's {@link ArrayPP#containsAny} method for the
   * specified item returns {@code true}.
   * @param item the item to be searched for
   * @return a new <tt>ArrayTable</tt> containing ONLY numRows that contain an item in the parameter(s)
   * @see ArrayPP#containsAll(T... items)
   */
  public ArrayTable<T> isolateRowsContainingAny(T... item)
  {
    ArrayTable<T> a = new ArrayTable<T>();

    for (ArrayPP<T> row : this)
    {
      if (row.containsAny(item))
      {
        a.addRow(row);
      }
    }
    return a;
  }

  /**
   * Searches for the given items and returns an {@code ArrayTable} that contains none of the rows that have matches, without
   * double-removing a row due to multiple matches. The search algorithm works by taking each row individually as an
   * {@link ArrayPP} and adds the row to the new table if and only if the row's {@link ArrayPP#containsAll} method for the specified
   * item returns {@code false}.
   * @param item the item to be searched for
   * @return a new <tt>ArrayTable</tt> containing ONLY numRows that contain an item in the parameter(s)
   * @see ArrayPP#containsNone(T... items)
   */
  public ArrayTable<T> isolateRowsWithoutAll(T... item)
  {
    ArrayTable<T> a = new ArrayTable<T>();

    for (ArrayPP<T> row : this)
    {
      for (int j = 0, l=row.length(); j < l - 1; j++)
        if (!row.containsAll(item))
        {
          a.addRow(row);
          break;
        }
    }
    return a;
  }

  /**
   * Searches for the given items and returns an {@code ArrayTable} that contains none of the rows that have matches, without
   * double-removing a row due to multiple matches. The search algorithm works by taking each row individually as an
   * {@link ArrayPP} and adds the row to the new table if and only if the row's {@link ArrayPP#containsAny} method for the
   * specified item returns {@code false}.
   * @param item the item to be searched for
   * @return a new <tt>ArrayTable</tt> containing ONLY numRows that contain an item in the parameter(s)
   * @see ArrayPP#containsNone(T... items)
   */
  public ArrayTable<T> isolateRowsWithoutAny(T... item)
  {
    ArrayTable<T> a = new ArrayTable<T>();

    for (ArrayPP<T> row : this)
    {
      for (int j = 0, l=row.length(); j < l - 1; j++)
        if (row.containsNone(item))
        {
          a.addRow(row);
          break;
        }
    }
    return a;
  }

  public ArrayTable<T> isolateColsContainingAny(T... item)
  {
    ArrayTable<T> a = new ArrayTable<T>();
    ArrayPP<T> col;
    for (int i=0, c = getNumCols(); i < c - 1; i++)
    {
      col = getCol(i);
      for (int j=0, l=col.length(); j < l - 1; j++)
        if (col.containsAny(item))
        {
          a.addCol(col);
          break;
        }
    }
    return a;
  }

  public ArrayTable<T> isolateColsContainingAll(T... item)
  {
    ArrayTable<T> a = new ArrayTable<T>();
    ArrayPP<T> col;
    for (int i=0, c = getNumCols(); i < c - 1; i++)
    {
      col = getCol(i);
      for (int j=0, l=col.length(); j < l - 1; j++)
        if (col.containsAll(item))
        {
          a.addCol(col);
          break;
        }
    }
    return a;
  }

  @Override
  public ArrayTable<T> clone()
  {
    return new ArrayTable<T>(rows);
  }

  public ArrayTable<T> addRow(T... items)
  {
    return addRow(new ArrayPP<T>(items));
  }

  public ArrayTable<T> addRow(ArrayPP<T> items)
  {
    rows.add(items);
    return this;
  }

  public ArrayTable<T> addCol(T... items)
  {
    return addCol(null, items);
  }
  
  /**
   * Adds a column with the given name
   * @param name the name of the new column
   * @param items the items in the new column
   * @return {@code this}
   * @since 2013-07-30 (
   */
  public ArrayTable<T> addCol(CharSequence name, T... items)
  {
	  colKeys.add(name);
    return addRow(new ArrayPP<T>(items));
  }

  public ArrayTable<T> addCol(ArrayPP<T> items)
  {
    int c = getNumCols();
    for (int i=0; i < Math.max(getNumRows(), items.length()); i++)
    {
      try
      {
        getRow(i).add(items.get(i));
      }
      catch (Exception ex)
      {
        ArrayPP<T> a = new ArrayPP<T>();
        for (int j=0; j < c; j++)
          a.add((T)null);
        a.add(items.get(i));
        addRow(a);
      }
    }
    return this;
  }

  /**
   * removes all values from the <tt>ArrayTable</tt>
   * @return the resulting, empty <tt>ArrayTable</tt>
   */
  public ArrayTable<T> clear()
  {
//    System.out.println("Clearing " + getNumRows() + " numRows...");
//    while(getNumRows() > 0)
//      removeRow(0);
//    System.out.println("\n\n\tCleared!\n\n{" + this + "}");
    rows.clear();
    return this;
  }

  public ArrayTable<T> removeRow(int row)
  {
//    System.out.println("Removing row " + row + "... {" + getRow(row) + "}");
    rows.remove(row);
//    System.out.println("Row removed! {" + (getNumRows() == 0 || row > getNumRows() - 1 || getRow(row) == null ? "" : getRow(row)) + "}");
    return this;
  }
  
  /**
   * Removes the given column and its name from this table
   * @param col the column to remove
   * @return {@code this}
   * @version 1.1.0
   */
  public ArrayTable<T> removeCol(int col)
  {
	  colKeys.remove(col);
    for (int i=0, r = getNumRows(); i < r; i++)
      getRow(i).remove(col);
    return this;
  }

  /**
   * Counts the number of numRows in the table and returns the final result.
   * @return the number of numRows in the table
   */
  public int getNumRows()
  {
    return rows.length();
  }

  /**
   * Calculates and returns the width of the widest row
   * @return the number of columns in the table
   */
  public int getNumCols()
  {
//    System.out.println("Calculating number of columns...");
    int ret = 0, r = getNumRows();
    for (int i=0,l=0; i < r; i++)
    {
      l = rows.get(i).length();
      if (l > ret)
//        System.out.println("  Line " + i + " has " + l + ". " + (l > ret ? "Increasing ret to " + l : "Continuing with ret as " + ret) + "...");
      ret = Math.max(l, ret);
    }
    return ret;
  }

  /**
   * Creates an <tt>ArrayTable</tt> of <tt>String</tt>s out of the text of a <tt>.CSV</tt> file.
   * @param csv the <tt>String</tt> that represents the contents of a <tt>.CSV</tt> file. <B>MUST BE FORMATTED EXACTLY AS A <TT>.CSV</TT> FILE IS</B>
   * @return a new <tt>ArrayTable</tt> of <tt>String</tt>s
   */
  public static ArrayTable<String> createArrayTableFromCSV(String csv)
  {
    final char LITERAL_BRACKET = '"', SEP = ',';
    ArrayTable<String> newTable = new ArrayTable();

    java.util.Scanner fullScanner = new java.util.Scanner(csv), lineScanner;
    StringPP line;
    String item;
    ArrayPP<String> row;
//    int c = 0, offset;
    boolean s;
    for (int i=0; fullScanner.hasNextLine(); i++)
    {
      s = false;
      item = "";
      line = new StringPP(fullScanner.nextLine());
      lineScanner = new java.util.Scanner(line.toString());
//      c = line.getOccurrencesOf(SEP);
      row = new ArrayPP<String>();
      char cPre = 0;
      for (char c1 : line)
      {
//        System.out.print(c1);
        if (c1 == LITERAL_BRACKET)
        {
          if (cPre == LITERAL_BRACKET)
            item += LITERAL_BRACKET;
          s = !s;
          cPre = c1;
          continue;
        }
        if (c1 == SEP && !s)
        {
          row.add(item);
          item = "";
          cPre = c1;
          continue;
        }
        item += c1;
        cPre = c1;
      }
      row.add(item);
//      System.out.println();
      newTable.addRow(row);
    }
    return newTable;
  }

  /**
   * Creates an <tt>ArrayTable&lt;String&gt;</tt> out of a properly formatted <tt>.CSV</tt> file
   * @param csv the <tt>java.io.File</tt> representing the properly formatted <tt>.CSV</tt> file to be read
   * @return an <tt>ArrayTable&lt;String&gt;</tt> representation of the table contained in the <tt>.CSV</tt> file
   * @throws FileNotFoundException if the given file does not exist, or can otherwise not be accessed
   */
  public static ArrayTable<String> createArrayTableFromCSV(java.io.File csv) throws FileNotFoundException
  {
    java.util.Scanner s = new java.util.Scanner(csv);
    String ret = "";
    for (int i=0; s.hasNextLine(); i++)
      ret += s.nextLine() + "\n";
    return createArrayTableFromCSV(ret.substring(0, ret.length() - 1));
  }

  /**
   * Creates and returns a <tt>String</tt> that can be used as the contents of a properly formatted <tt>.CSV</tt> file
   * @return a <tt>String</tt> that can be used as the contents of a properly formatted <tt>.CSV</tt> file
   */
  public String toCSV()
  {
    StringPP csv = new StringPP();
    for (int i = 0; i < rows.length(); i++)
    {
      for (T t : rows.get(i))
      {
        csv.append(t.toString() + (i < rows.length() ? "," : ""));
      }
      csv.appendln();
    }
    return csv.toString();
  }
  
  public java.awt.Dimension getSize()
  {
    return new java.awt.Dimension(getNumCols(), getNumRows());
  }

  /**
   * Generates and returns an iterator that, upon each call of <tt>next()</tt>, returns the next ROW of items in the table, from the top down
   * @return a new <tt>Iterator</tt> of the ROWS in this table
   */
  @Override
  public Iterator<ArrayPP<T>> iterator()
  {
    return new Iterator<ArrayPP<T>>()
    {
      int pos = 0;
      @Override
      public boolean hasNext()
      {
        return pos < getNumRows();
      }

      @Override
      public ArrayPP<T> next()
      {
        return getRow(pos++);
      }

      @Override
      public void remove()
      {
        removeRow(pos);
      }
    };
  }

  /**
   * Returns the class of the first non-null object in the array
   * @deprecated May not accurately represent the classes of every object in the array
   * @return the class of the first non-null object in the array
   * @throws NullPointerException if there is no non-null object in the array
   */
  public Class<T> getTypeClass()
  {
    for (ArrayPP<T> a : rows)
      for (T t : a)
        if (t != null)
          return (Class<T>) t.getClass();
    throw new NullPointerException("No non-null value in the array to measure");
  }

  public javax.swing.table.TableModel toTableModel()
  {
    return new javax.swing.table.TableModel()
    {
      T[][] array = toArray();
      int numCols = array[0].length, numRows = array.length - 1;
      Class<T> classType = getTypeClass();

      @Override
      public int getRowCount()
      {
        return numRows;
      }

      @Override
      public int getColumnCount()
      {
        return numCols;
      }

      @Override
      public Object getValueAt(int rowIndex, int columnIndex)
      {
        return array[rowIndex + 1][columnIndex];
      }

      @Override
      public String getColumnName(int columnIndex)
      {
        return String.valueOf(array[0][columnIndex]);
      }

      @Override
      public Class<?> getColumnClass(int columnIndex)
      {
        return classType;
      }

      @Override
      public boolean isCellEditable(int rowIndex, int columnIndex)
      {
        return false;
      }

      @Override
      public void setValueAt(Object aValue, int rowIndex, int columnIndex) throws ClassCastException
      {
        array[rowIndex][columnIndex] = (T)aValue;
      }

      @Override public void addTableModelListener(javax.swing.event.TableModelListener l){}
      @Override public void removeTableModelListener(javax.swing.event.TableModelListener l){}
    };
  }

  public T[][] toArray()
  {
    Object[][] array = new Object[getNumRows()][getNumCols()];
    int i=0;
    for (ArrayPP<T> a : rows)
    {
      array[i]=a.toArray();
      i++;
    }
    return (T[][])array;
  }

  /**
   * Returns <tt>true</tt> if and only if the table has no content (A table with only null values still has content)
   * @return whether or not the table is empty
   */
  public boolean isEmpty()
  {
    return getNumRows() == 0;
  }

  /**
   * Returns whether there exists, in this table, a row such that the row is the same length as the given array and the items in
   * the row are equal to those in the given array
   * @param row
   * @return
   */
  public boolean containsRow(T... row)
  {
    for (ArrayPP<T> a : rows)
      if (Arrays.equals(a.toArray(), row))
        return true;
    return false;
  }

  public void insertRow(ArrayPP<T> row, int i)
  {
    rows.insert(row, i);
  }

  /**
   * Searches for the last cell which is both empty ({@code null}) and preceded by a filled cell
   * @param newVal the new value of the cell
   * @return this table 
   */
  public ArrayTable<T> setLastEmptyCell(T newVal)
  {
    boolean prevCellFilled = false;
    TinyPoint candidate = new TinyPoint(0, 0);
    ArrayPP<T> row;
    for (int x, y = 0, w, h = getNumRows(); y < h; y++)
    {
      row = getRow(y);
      for (x = 0, w = row.length(); x <= w; x++)
      {
        if (row.has(x))
        {
          prevCellFilled = true;
          continue;
        }
        else if (prevCellFilled)
          candidate.setLocation(x, y);
        prevCellFilled = false;
      }
    }
    return ensureHasCell(candidate).setCell(candidate, newVal);
  }

  /**
   * Searches for the last cell which is both empty ({@code null}) and preceded by a filled cell
   * @return the value at the last 
   */
  public T getLastFilledCell()
  {
    TinyPoint candidate = new TinyPoint(0, 0);
    ArrayPP<T> row;
    for (int x, y = 0, w, h = getNumRows(); y < h; y++)
    {
      row = getRow(y);
      for (x = 0, w = getRow(y).length(); x < w; x++)
      {
        if (row.has(x))
        {
          candidate.setLocation(x, y);
          continue;
        }
      }
    }
    return getCell(candidate);
  }

  private ArrayTable<T> ensureHasCell(TinyPoint candidate)
  {
    return ensureHasCell(candidate.getY(), candidate.getX());
  }

  public ArrayTable<T> ensureHasCell(int row, int col)
  {
    while (row >= getNumRows())
      rows.add(new ArrayPP<T>());
    while (col >= rows.get(row).length())
      rows.get(row).add((T)null);
    
    return this;
  }

  /*public ArrayTable<T> setSize(Dimension size)
  {
    
    return this;
  }*/

	public void setColumnKey(int colIndex, Object newKey)
	{
		colKeys.set(colIndex, newKey);
	}
	
	public Object getColumnKey(int colIndex)
	{
		return colKeys.get(colIndex);
	}

	public ArrayPP<T> getCol(Object colKey)
	{
		return getCol(getColIndexForKey(colKey));
	}

	public void setRowKey(int rowIndex, Object newKey)
	{
		rowKeys.set(rowIndex, newKey);
	}
	
	public Object getRowKey(int rowIndex)
	{
		return rowKeys.get(rowIndex);
	}

	public ArrayPP<T> getRow(Object rowKey)
	{
		return getRow(getRowIndexForKey(rowKey));
	}

	public ArrayTable<T> removeCol(Object colKey)
	{
		return removeCol(getColIndexForKey(colKey));
	}

	public ArrayTable<T> removeRow(Object rowKey)
	{
		return removeRow(getRowIndexForKey(rowKey));
	}
	
	public int getRowIndexForKey(Object rowKey)
	{
		return rowKeys.getIndexOf(rowKey);
	}
	
	/**
	 * Returns the index of the column that uses the given key
	 * @param colKey the key to be searched for
	 * @return the index of the column using the given key
	 * @see ArrayPP#getIndexOf(java.lang.Object)
	 */
	public int getColIndexForKey(Object colKey)
	{
		return colKeys.getIndexOf(colKey);
	}

	public ArrayTable<T> insertColumn(ArrayPP<T> col, int colIndex)
	{
		for (int i=0, l=rows.length(); i < l; i++)
		{
			rows.get(i).add(col.get(i));
		}
		return this;
	}
	
	public T getCell(Object colKey, Object rowKey)
	{
		return getCell(getColIndexForKey(colKey), getRowIndexForKey(rowKey));
	}
	
	public ArrayTable<T> setCell(Object rowKey, Object colKey, T newVal)
	{
		return setCell(getRowIndexForKey(rowKey), getColIndexForKey(colKey), newVal);
	}
}