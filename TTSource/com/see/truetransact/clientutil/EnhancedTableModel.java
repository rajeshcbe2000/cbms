/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 **
 *
 * EnhancedTableModel.java
 *
 * Created on July 7, 2003, 4:20 PM
 */

package com.see.truetransact.clientutil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.event.TableModelEvent;

import javax.swing.table.AbstractTableModel;

/**
 * This is an implementation of <code>TableModel</code> that
 * uses a <code>ArrayList</code> of <code>ArrayLists</code> to store the
 * cell value objects.
 * <p>
 * <strong>Warning:</strong>
 * Serialized objects of this class will not be compatible with
 * future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running
 * the same version of Swing.  As of 1.4, support for long term storage
 * of all JavaBeans<sup><font size="-2">TM</font></sup>
 * has been added to the <code>java.beans</code> package.
 * Please see {@link java.beans.XMLEncoder}.
 *
 * @version 
 * @author Balachandar
 *
 * @see TableModel
 * @see #getDataArrayList
 */
public class EnhancedTableModel extends AbstractTableModel implements Serializable {

//
// Instance Variables
//

  /**
   * The <code>ArrayList</code> of <code>ArrayLists</code> of 
   * <code>Object</code> values.
   */
    protected ArrayList    dataArrayList;

  /** The <code>ArrayList</code> of column identifiers. */
    protected ArrayList    columnIdentifiers;

//
// Constructors
//

    /**
     *  Constructs a default <code>EnhancedTableModel</code> 
     *  which is a table of zero columns and zero rows.
     */
    public EnhancedTableModel() {
        this(0, 0);
    }

    private static ArrayList newArrayList(int size) { 
	ArrayList v = new ArrayList(size); 
        v.ensureCapacity(size); 
	return v;
    }

    /**
     *  Constructs a <code>EnhancedTableModel</code> with
     *  <code>rowCount</code> and <code>columnCount</code> of
     *  <code>null</code> object values.
     *
     * @param rowCount           the number of rows the table holds
     * @param columnCount        the number of columns the table holds
     *
     * @see #setValueAt
     */
    public EnhancedTableModel(int rowCount, int columnCount) {
        this(newArrayList(columnCount), rowCount); 
    }

    /**
     *  Constructs a <code>EnhancedTableModel</code> with as many columns
     *  as there are elements in <code>columnNames</code>
     *  and <code>rowCount</code> of <code>null</code>
     *  object values.  Each column's name will be taken from
     *  the <code>columnNames</code> ArrayList.
     *
     * @param columnNames       <code>ArrayList</code> containing the names
     *				of the new columns; if this is 
     *                          <code>null</code> then the model has no columns
     * @param rowCount           the number of rows the table holds
     * @see #setDataArrayList
     * @see #setValueAt
     */
    public EnhancedTableModel(ArrayList columnNames, int rowCount) {
        setDataArrayList(newArrayList(rowCount), columnNames);
    }

    /**
     *  Constructs a <code>EnhancedTableModel</code> with as many
     *  columns as there are elements in <code>columnNames</code>
     *  and <code>rowCount</code> of <code>null</code>
     *  object values.  Each column's name will be taken from
     *  the <code>columnNames</code> array.
     *
     * @param columnNames       <code>array</code> containing the names
     *				of the new columns; if this is
     *                          <code>null</code> then the model has no columns
     * @param rowCount           the number of rows the table holds
     * @see #setDataArrayList
     * @see #setValueAt
     */
    public EnhancedTableModel(Object[] columnNames, int rowCount) {
        this(convertToArrayList(columnNames), rowCount);
    }

    /**
     *  Constructs a <code>EnhancedTableModel</code> and initializes the table
     *  by passing <code>data</code> and <code>columnNames</code>
     *  to the <code>setDataArrayList</code> method.
     *
     * @param data              the data of the table
     * @param columnNames       <code>ArrayList</code> containing the names
     *				of the new columns
     * @see #getDataArrayList
     * @see #setDataArrayList
     */
    public EnhancedTableModel(ArrayList data, ArrayList columnNames) {
        setDataArrayList(data, columnNames);
    }

    /**
     *  Constructs a <code>EnhancedTableModel</code> and initializes the table
     *  by passing <code>data</code> and <code>columnNames</code>
     *  to the <code>setDataArrayList</code>
     *  method. The first index in the <code>Object[][]</code> array is
     *  the row index and the second is the column index.
     *
     * @param data              the data of the table
     * @param columnNames       the names of the columns
     * @see #getDataArrayList
     * @see #setDataArrayList
     */
    public EnhancedTableModel(Object[][] data, Object[] columnNames) {
        setDataArrayList(data, columnNames);
    }

    /**
     *  Returns the <code>ArrayList</code> of <code>ArrayLists</code>
     *  that contains the table's
     *  data values.  The ArrayLists contained in the outer ArrayList are
     *  each a single row of values.  In other words, to get to the cell
     *  at row 1, column 5: <p>
     *
     *  <code>((ArrayList)getDataArrayList().elementAt(1)).elementAt(5);</code><p>
     *
     * @return  the ArrayList of ArrayLists containing the tables data values
     *
     * @see #newDataAvailable
     * @see #newRowsAdded
     * @see #setDataArrayList
     */
    public ArrayList getDataArrayList() {
        return dataArrayList;
    }

    private static ArrayList nonNullArrayList(ArrayList v) { 
	return (v != null) ? v : new ArrayList(); 
    } 

    /**
     *  Replaces the current <code>dataArrayList</code> instance variable 
     *  with the new ArrayList of rows, <code>dataArrayList</code>.
     *  <code>columnIdentifiers</code> are the names of the new 
     *  columns.  The first name in <code>columnIdentifiers</code> is
     *  mapped to column 0 in <code>dataArrayList</code>. Each row in
     *  <code>dataArrayList</code> is adjusted to match the number of 
     *  columns in <code>columnIdentifiers</code>
     *  either by truncating the <code>ArrayList</code> if it is too long,
     *  or adding <code>null</code> values if it is too short.
     *  <p>Note that passing in a <code>null</code> value for
     *  <code>dataArrayList</code> results in unspecified behavior,
     *  an possibly an exception.
     *
     * @param   dataArrayList         the new data ArrayList
     * @param   columnIdentifiers     the names of the columns
     * @see #getDataArrayList
     */
    public void setDataArrayList(ArrayList dataArrayList, ArrayList columnIdentifiers) {
        this.dataArrayList = nonNullArrayList(dataArrayList);
        this.columnIdentifiers = nonNullArrayList(columnIdentifiers); 
	justifyRows(0, getRowCount()); 
        fireTableStructureChanged();
    }

    /**
     *  Replaces the value in the <code>dataArrayList</code> instance 
     *  variable with the values in the array <code>dataArrayList</code>.
     *  The first index in the <code>Object[][]</code>
     *  array is the row index and the second is the column index.
     *  <code>columnIdentifiers</code> are the names of the new columns.
     *
     * @param dataArrayList		the new data ArrayList
     * @param columnIdentifiers	the names of the columns
     * @see #setDataArrayList(ArrayList, ArrayList)
     */
    public void setDataArrayList(Object[][] dataArrayList, Object[] columnIdentifiers) {
        setDataArrayList(convertToArrayList(dataArrayList), convertToArrayList(columnIdentifiers));
    }

    /**
     *  Equivalent to <code>fireTableChanged</code>.
     *
     * @param event  the change event 
     *
     */
    public void newDataAvailable(TableModelEvent event) {
        fireTableChanged(event);
    }

//
// Manipulating rows
// 

    private void justifyRows(int from, int to) { 
	// Sometimes the EnhancedTableModel is subclassed 
	// instead of the AbstractTableModel by mistake. 
	// Set the number of rows for the case when getRowCount 
	// is overridden. 
	dataArrayList.ensureCapacity(getRowCount()); 

        for (int i = from; i < to; i++) { 
	    if (dataArrayList.get(i) == null) { 
		dataArrayList.set(i, new ArrayList()); 
	    }
	    ((ArrayList)dataArrayList.get(i)).ensureCapacity(getColumnCount());
	}
    }

    /**
     *  Ensures that the new rows have the correct number of columns.
     *  This is accomplished by  using the <code>setSize</code> method in
     *  <code>ArrayList</code> which truncates ArrayLists
     *  which are too long, and appends <code>null</code>s if they
     *  are too short.
     *  This method also sends out a <code>tableChanged</code>
     *  notification message to all the listeners.
     *
     * @param                    this <code>TableModelEvent</code> describes 
     *                           where the rows were added. 
     *				 If <code>null</code> it assumes
     *                           all the rows were newly added
     * @see #getDataArrayList
     */
    public void newRowsAdded(TableModelEvent e) {
        justifyRows(e.getFirstRow(), e.getLastRow() + 1); 
        fireTableChanged(e);
    }

    /**
     *  Equivalent to <code>fireTableChanged</code>.
     *
     *  @param event the change event
     *
     */
    public void rowsRemoved(TableModelEvent event) {
        fireTableChanged(event);
    }

    /**
     * Obsolete as of Java 2 platform v1.3.  Please use <code>setRowCount</code> instead.
     */
    /*
     *  Sets the number of rows in the model.  If the new size is greater
     *  than the current size, new rows are added to the end of the model
     *  If the new size is less than the current size, all
     *  rows at index <code>rowCount</code> and greater are discarded. <p>
     *
     * @param   rowCount   the new number of rows
     * @see #setRowCount
     */
    public void setNumRows(int rowCount) { 
        int old = getRowCount();
	if (old == rowCount) { 
	    return; 
	}
	dataArrayList.ensureCapacity(rowCount);
        if (rowCount <= old) {
            fireTableRowsDeleted(rowCount, old-1);
        }
        else {
	    justifyRows(old, rowCount); 
            fireTableRowsInserted(old, rowCount-1);
        }
    }

    /**
     *  Sets the number of rows in the model.  If the new size is greater
     *  than the current size, new rows are added to the end of the model
     *  If the new size is less than the current size, all
     *  rows at index <code>rowCount</code> and greater are discarded. <p>
     *
     *  @see #setColumnCount
     */
    public void setRowCount(int rowCount) { 
	setNumRows(rowCount); 
    } 

    /**
     *  Adds a row to the end of the model.  The new row will contain
     *  <code>null</code> values unless <code>rowData</code> is specified.
     *  Notification of the row being added will be generated.
     *
     * @param   rowData          optional data of the row being added
     */
    public void addRow(ArrayList rowData) {
        insertRow(getRowCount(), rowData);
    }

    /**
     *  Adds a row to the end of the model.  The new row will contain
     *  <code>null</code> values unless <code>rowData</code> is specified.
     *  Notification of the row being added will be generated.
     *
     * @param   rowData          optional data of the row being added
     */
    public void addRow(Object[] rowData) {
        addRow(convertToArrayList(rowData));
    }

    /**
     *  Inserts a row at <code>row</code> in the model.  The new row
     *  will contain <code>null</code> values unless <code>rowData</code>
     *  is specified.  Notification of the row being added will be generated.
     *
     * @param   row             the row index of the row to be inserted
     * @param   rowData         optional data of the row being added
     * @exception  ArrayIndexOutOfBoundsException  if the row was invalid
     */
    public void insertRow(int row, ArrayList rowData) {
	dataArrayList.add(row, rowData);
	justifyRows(row, row+1);
        fireTableRowsInserted(row, row);
    }

    /**
     *  Inserts a row at <code>row</code> in the model.  The new row
     *  will contain <code>null</code> values unless <code>rowData</code>
     *  is specified.  Notification of the row being added will be generated.
     *
     * @param   row      the row index of the row to be inserted
     * @param   rowData          optional data of the row being added
     * @exception  ArrayIndexOutOfBoundsException  if the row was invalid
     */
    public void insertRow(int row, Object[] rowData) {
        insertRow(row, convertToArrayList(rowData));
    }

    private static int gcd(int i, int j) {
	return (j == 0) ? i : gcd(j, i%j); 
    }

    private static void rotate(ArrayList v, int a, int b, int shift) {
	int size = b - a; 
	int r = size - shift;
	int g = gcd(size, r); 
	for(int i = 0; i < g; i++) {
	    int to = i; 
	    Object tmp = v.get(a + to); 
	    for(int from = (to + r) % size; from != i; from = (to + r) % size) {
		v.set(a + to, v.get(a + from)); 
		to = from; 
	    }
	    v.set(a + to, tmp); 
	}
    }

    /**
     *  Moves one or more rows from the inlcusive range <code>start</code> to 
     *  <code>end</code> to the <code>to</code> position in the model. 
     *  After the move, the row that was at index <code>start</code> 
     *  will be at index <code>to</code>. 
     *  This method will send a <code>tableChanged</code> notification
     *  message to all the listeners. <p>
     *
     *  <pre>
     *  Examples of moves:
     *  <p>
     *  1. moveRow(1,3,5);
     *          a|B|C|D|e|f|g|h|i|j|k   - before
     *          a|e|f|g|h|B|C|D|i|j|k   - after
     *  <p>
     *  2. moveRow(6,7,1);
     *          a|b|c|d|e|f|G|H|i|j|k   - before
     *          a|G|H|b|c|d|e|f|i|j|k   - after
     *  <p> 
     *  </pre>
     *
     * @param   start       the starting row index to be moved
     * @param   end         the ending row index to be moved
     * @param   to          the destination of the rows to be moved
     * @exception  ArrayIndexOutOfBoundsException  if any of the elements 
     * would be moved out of the table's range 
     * 
     */
    public void moveRow(int start, int end, int to) { 
	int shift = to - start; 
	int first, last; 
	if (shift < 0) { 
	    first = to; 
	    last = end; 
	}
	else { 
	    first = start; 
	    last = to + end - start;  
	}
        rotate(dataArrayList, first, last + 1, shift); 

        fireTableRowsUpdated(first, last);
    }

    /**
     *  Removes the row at <code>row</code> from the model.  Notification
     *  of the row being removed will be sent to all the listeners.
     *
     * @param   row      the row index of the row to be removed
     * @exception  ArrayIndexOutOfBoundsException  if the row was invalid
     */
    public void removeRow(int row) {
        dataArrayList.remove(row);
        fireTableRowsDeleted(row, row);
    }

//
// Manipulating columns
// 

    /**
     * Replaces the column identifiers in the model.  If the number of
     * <code>newIdentifier</code>s is greater than the current number
     * of columns, new columns are added to the end of each row in the model.
     * If the number of <code>newIdentifier</code>s is less than the current
     * number of columns, all the extra columns at the end of a row are
     * discarded. <p>
     *
     * @param   newIdentifiers  ArrayList of column identifiers.  If
     *				<code>null</code>, set the model
     *                          to zero columns
     * @see #setNumRows
     */
    public void setColumnIdentifiers(ArrayList columnIdentifiers) {
	setDataArrayList(dataArrayList, columnIdentifiers); 
    }

    /**
     * Replaces the column identifiers in the model.  If the number of
     * <code>newIdentifier</code>s is greater than the current number
     * of columns, new columns are added to the end of each row in the model.
     * If the number of <code>newIdentifier</code>s is less than the current
     * number of columns, all the extra columns at the end of a row are
     * discarded. <p>
     *
     * @param   newIdentifiers  array of column identifiers. 
     *				If <code>null</code>, set
     *                          the model to zero columns
     * @see #setNumRows
     */
    public void setColumnIdentifiers(Object[] newIdentifiers) {
        setColumnIdentifiers(convertToArrayList(newIdentifiers));
    }

    /**
     *  Sets the number of columns in the model.  If the new size is greater
     *  than the current size, new columns are added to the end of the model 
     *  with <code>null</code> cell values.
     *  If the new size is less than the current size, all columns at index
     *  <code>columnCount</code> and greater are discarded. 
     *
     *  @param columnCount  the new number of columns in the model
     *
     *  @see #setColumnCount
     */
    public void setColumnCount(int columnCount) { 
	columnIdentifiers.ensureCapacity(columnCount); 
	justifyRows(0, getRowCount()); 
	fireTableStructureChanged();
    } 

    /**
     *  Adds a column to the model.  The new column will have the
     *  identifier <code>columnName</code>, which may be null.  This method
     *  will send a
     *  <code>tableChanged</code> notification message to all the listeners.
     *  This method is a cover for <code>addColumn(Object, ArrayList)</code> which
     *  uses <code>null</code> as the data ArrayList.
     *
     * @param   columnName the identifier of the column being added
     */
    public void addColumn(Object columnName) {
        addColumn(columnName, (ArrayList)null);
    }

    /**
     *  Adds a column to the model.  The new column will have the
     *  identifier <code>columnName</code>, which may be null.
     *  <code>columnData</code> is the
     *  optional ArrayList of data for the column.  If it is <code>null</code>
     *  the column is filled with <code>null</code> values.  Otherwise,
     *  the new data will be added to model starting with the first
     *  element going to row 0, etc.  This method will send a
     *  <code>tableChanged</code> notification message to all the listeners.
     *
     * @param   columnName the identifier of the column being added
     * @param   columnData       optional data of the column being added
     */
    public void addColumn(Object columnName, ArrayList columnData) {
        columnIdentifiers.add(columnName); 
	if (columnData != null) { 
            int columnSize = columnData.size(); 
            if (columnSize > getRowCount()) { 
	        dataArrayList.ensureCapacity(columnSize);
            }
	    justifyRows(0, getRowCount()); 
	    int newColumn = getColumnCount() - 1; 
	    for(int i = 0; i < columnSize; i++) { 
		  ArrayList row = (ArrayList)dataArrayList.get(i);
		  row.set(newColumn, columnData.get(i)); 
	    }
	} 
        else { 
	    justifyRows(0, getRowCount()); 
        }

        fireTableStructureChanged();
    }

    /**
     *  Adds a column to the model.  The new column will have the
     *  identifier <code>columnName</code>.  <code>columnData</code> is the
     *  optional array of data for the column.  If it is <code>null</code>
     *  the column is filled with <code>null</code> values.  Otherwise,
     *  the new data will be added to model starting with the first
     *  element going to row 0, etc.  This method will send a
     *  <code>tableChanged</code> notification message to all the listeners.
     *
     * @see #addColumn(Object, ArrayList)
     */
    public void addColumn(Object columnName, Object[] columnData) {
        addColumn(columnName, convertToArrayList(columnData));
    }

//
// Implementing the TableModel interface
//

    /**
     * Returns the number of rows in this data table.
     * @return the number of rows in the model
     */
    public int getRowCount() {
        return dataArrayList.size();
    }

    /**
     * Returns the number of columns in this data table.
     * @return the number of columns in the model
     */
    public int getColumnCount() {
        return columnIdentifiers.size();
    }

    /**
     * Returns the column name.
     *
     * @return a name for this column using the string value of the
     * appropriate member in <code>columnIdentifiers</code>.
     * If <code>columnIdentifiers</code> does not have an entry 
     * for this index, returns the default
     * name provided by the superclass
     */
    public String getColumnName(int column) {
        Object id = null; 
	// This test is to cover the case when 
	// getColumnCount has been subclassed by mistake ... 
	if (column < columnIdentifiers.size()) {  
	    id = columnIdentifiers.get(column); 
	}
        return (id == null) ? super.getColumnName(column) 
                            : id.toString();
    }

    /**
     * Returns true regardless of parameter values.
     *
     * @param   row             the row whose value is to be queried
     * @param   column          the column whose value is to be queried
     * @return                  true
     * @see #setValueAt
     */
    public boolean isCellEditable(int row, int column) {
        // commented by Bala
        //return true;
        return false;
    }

    /**
     * Returns an attribute value for the cell at <code>row</code>
     * and <code>column</code>.
     *
     * @param   row             the row whose value is to be queried
     * @param   column          the column whose value is to be queried
     * @return                  the value Object at the specified cell
     * @exception  ArrayIndexOutOfBoundsException  if an invalid row or
     *               column was given
     */
    public Object getValueAt(int row, int column) {
        ArrayList rowArrayList = (ArrayList) dataArrayList.get(row);
        return rowArrayList.get(column);
    }

    /**
     * Sets the object value for the cell at <code>column</code> and
     * <code>row</code>.  <code>aValue</code> is the new value.  This method
     * will generate a <code>tableChanged</code> notification.
     *
     * @param   aValue          the new value; this can be null
     * @param   row             the row whose value is to be changed
     * @param   column          the column whose value is to be changed
     * @exception  ArrayIndexOutOfBoundsException  if an invalid row or
     *               column was given
     */
    public void setValueAt(Object aValue, int row, int column) {
        ArrayList rowArrayList = (ArrayList)dataArrayList.get(row);
        rowArrayList.set(column, aValue);
        fireTableCellUpdated(row, column);
    }

//
// Protected Methods
//

    /** 
     * Returns a ArrayList that contains the same objects as the array.
     * @param anArray  the array to be converted
     * @return  the new ArrayList; if <code>anArray</code> is <code>null</code>,
     *				returns <code>null</code>
     */
    protected static ArrayList convertToArrayList(Object[] anArray) {
        if (anArray == null) { 
            return null;
	}
        ArrayList v = new ArrayList(anArray.length);
        for (int i=0; i < anArray.length; i++) {
            v.add(anArray[i]);
        }
        return v;
    }

    /** 
     * Returns a ArrayList of ArrayLists that contains the same objects as the array.
     * @param anArray  the double array to be converted
     * @return the new ArrayList of ArrayLists; if <code>anArray</code> is
     *				<code>null</code>, returns <code>null</code>
     */
    protected static ArrayList convertToArrayList(Object[][] anArray) {
        if (anArray == null) {
            return null;
	}
        ArrayList v = new ArrayList(anArray.length);
        for (int i=0; i < anArray.length; i++) {
            v.add(convertToArrayList(anArray[i]));
        }
        return v;
    }
    
    private Class[] types;
    
    public void setColumnClasses(Class[] classTypes) {
        types = classTypes;
    }

    /**
     * Addition by: Pranav
     * Added this method to include the classchecking machanism.
     * This will be useful when we are required to show checkbox instead of
     * true/false string
     */
    public Class getColumnClass(int c) {
        if (types!=null) {
            return types[c];
        } else {
			try {
				return getValueAt(0, c).getClass();
			}
			catch (NullPointerException exc)  {
				return String.class;
			}
        }
    }

} // End of class EnhancedTableModel
