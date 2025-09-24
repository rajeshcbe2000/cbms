/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 **
 *
 * TableSorter.java
 *
 * Created on June 23, 2003, 4:20 PM
 */

package com.see.truetransact.clientutil;

/**
 * A sorter class for JTable
 *
 * @author: Balachandar
 */

import java.util.ArrayList;
import java.util.Date;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.event.TableModelEvent;

public class TableSorter extends TableMap {
    private int indexes[] = new int[0];
    private ArrayList sortingColumns = new ArrayList();
    private boolean ascending = true;
    private int compares= 0;
    /**
     * UNCTableSorter constructor without parameters
     * @param title java.lang.String
     */
    public TableSorter() {
        super();
    }
    /**
     * Insert the method's description here.
     * Creation date: (2/12/2002 9:52:12 AM)
     * @param data java.lang.Object[][]
     * @param columnNames java.lang.Object[]
     */
    public TableSorter(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }
    /**
     * UNCTableSorter constructor with a UNCTableModel as parameter
     * @param title java.lang.String
     */
    public TableSorter(TableModel model) {
        super();
        setModel(model);
    }
    /**
     * Insert the method's description here.
     * Creation date: (3/19/2002 5:55:40 PM)
     * @param rows int
     * @param cols int
     */
    public TableSorter(int rows, int cols) {
        super(rows, cols);
    }
    /**
     * Insert the method's description here.
     * Creation date: (4/24/2002 11:55:38 AM)
     * @param data java.util.ArrayList
     * @param identifies java.util.ArrayList
     */
    public TableSorter(ArrayList data, ArrayList identifies) {
        super(data, identifies);
    }
    /**
     *  Add a mouse listener to the Table to trigger a table sort
     *  when a column heading is clicked in the JTable.
     * Creation date: (12/6/2001 12:04:12 PM)
     * @param: JTable which the actionlistener being added on.
     * @return:  void
     */
    public void addMouseListenerToHeaderInTable(JTable table) {
        final TableSorter sorter = this;
        final JTable tableView = (JTable)table;
        tableView.setColumnSelectionAllowed(false);
        MouseAdapter listMouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                TableColumnModel columnModel = tableView.getColumnModel();
                int viewColumn = columnModel.getColumnIndexAtX(e.getX());
                int column = tableView.convertColumnIndexToModel(viewColumn);
                if (e.getClickCount() == 1 && column != -1) {
                    //System.out.println("Sorting ...");
                    //int shiftPressed = e.getModifiers() & InputEvent.SHIFT_MASK;
                    ascending = !ascending; //(shiftPressed == 0);
                    sorter.sortByColumn(column, ascending);
                    //sorter.sortByColumn(column, shiftPressed == 0);
                }
            }
        };
        JTableHeader th = tableView.getTableHeader();
        th.addMouseListener(listMouseListener);
    }
    /**
     * Check the table model for validate
     * Creation date: (12/6/2001 12:05:31 PM)
     * @return: void
     */
    public void checkModel() {
        if (indexes.length != model.getRowCount()) {
            System.err.println("Sorter not informed of a change in model.");
        }
    }
    /**
     *Compare two rows return result as: -1, 0, 1
     * Creation date: (12/6/2001 12:06:13 PM)
     * @param: int row1, int row2
     * @return: int
     */
    public int compare(int row1, int row2) {
        compares++;
        int re = 0;
        int sortingColNum = sortingColumns.size();
        for (int level = 0; level < sortingColNum; level++) {
            Integer column = (Integer) sortingColumns.get(level);
            int result = compareRowsByColumn(row1, row2, column.intValue());
            
            re = result;
            if (result != 0) {
                re = ascending ? result : -result;
            }
        }
        return re;
    }
    /**
     * Compare two rows according the specific column
     * Creation date: (12/6/2001 12:07:12 PM)
     * @param: int row1, int row2, int cloumn
     * @return: int
     */
    public int compareRowsByColumn(int row1, int row2, int column) {
        Class type = model.getColumnClass(column);
        TableModel data = model;
        
        int result = 0;
        
        // Check for nulls.
        Object o1 = data.getValueAt(row1, column);
        Object o2 = data.getValueAt(row2, column);
        
        // If both values are null, return 0.
        if (o1 == null && o2 == null) {
            result = 0;
        } else if (o1 == null) { // Define null less than everything.
            result = -1;
        } else if (o2 == null) {
            result = 1;
        } else if (type.getSuperclass() == java.lang.Number.class) {
            result = sortNumber((Number) o1, (Number) o2);
        } else if (type == java.util.Date.class) {
            result = sortDate((Date) o1, (Date) o2);
        } else if (type == String.class) {
            result = sortString((String) o1, (String) o2);
        } else if (type == Boolean.class) {
            boolean b1 = ((Boolean) o1).booleanValue();
            boolean b2 = ((Boolean) o2).booleanValue();
            result = sortBoolean(b1, b2);
        } else {
            result = sortString(o1.toString(), o2.toString());
        }
        return result;
    }
    /**
     * Get the actual selected index
     * Creation date: (12/4/2001 9:55:55 AM)
     * @return int
     * @param row int
     */
    public int getIndex(int row) {
        return indexes[row];
    }
    /**
     * Get the value at specific position
     * Creation date: (12/6/2001 12:08:20 PM)
     * @param: int aRow, int aColumn
     * @return: Object
     */
    public Object getValueAt(int aRow, int aColumn) {
        checkModel();
        return model.getValueAt(indexes[aRow], aColumn);
    }
    /**
     * Sort the array using n2sorting alogrithm.
     * Creation date: (12/6/2001 12:09:40 PM)
     * @param:
     * @return: void
     */
    public void n2sort() {
        int rowCount = getRowCount();
        for (int i = 0; i < rowCount; i++) {
            for (int j = i + 1; j < rowCount; j++) {
                if (compare(indexes[i], indexes[j]) == -1) {
                    swap(i, j);
                }
            }
        }
    }
    /**
     * RellocateIndex for the index array.
     * Creation date: (12/6/2001 12:10:35 PM)
     * @param:
     * @return: void
     */
    public void reallocateIndexes() {
        int rowCount = model.getRowCount();
        
        // Set up a new array of indexes with the right number of elements
        // for the new data model.
        indexes = new int[rowCount];
        
        // Initialise with the identity mapping.
        for (int row = 0; row < rowCount; row++) {
            indexes[row] = row;
        }
    }
    /**
     * Set a UNCTableModel to this sorter.
     * Creation date: (12/6/2001 12:11:25 PM)
     * @param: UNCTableModel model
     * @return: void
     */
    public void setModel(TableModel model) {
        super.setModel(model);
        reallocateIndexes();
    }
    /**
     * Set the value at specific position.
     * Creation date: (12/6/2001 12:12:03 PM)
     * @param: Object aValue, int aRow, int aColumn
     * @return: void
     */
    public void setValueAt(Object aValue, int aRow, int aColumn) {
        checkModel();
        model.setValueAt(aValue, indexes[aRow], aColumn);
    }
    /**
     * Sort the rows by using the shuttlersort mehtod
     * It may perform poorly in some circumstances. It
     * requires twice the space of an in-place algorithm and makes
     * NlogN assigments shuttling the values between the two
     * arrays. The number of compares appears to vary between N-1 and
     * NlogN depending on the initial order but the main reason for
     * using it here is that, unlike qsort, it is stable.
     * Creation date: (12/6/2001 12:13:25 PM)
     * @param: int from[], int to[], int loe, int high
     * @return: void
     */
    public void shuttlesort(int from[], int to[], int low, int high) {
        if (high - low < 2) {
            return;
        }
        int middle = (low + high) / 2;
        shuttlesort(to, from, low, middle);
        shuttlesort(to, from, middle, high);
        
        int p = low;
        int q = middle;
        
    /* This is an optional short-cut; at each recursive call,
    check to see if the elements in this subset are already
    ordered.  If so, no further comparisons are needed; the
    sub-array can just be copied.  The array must be copied rather
    than assigned otherwise sister calls in the recursion might
    get out of sinc.  When the number of elements is three they
    are partitioned so that the first set, [low, mid), has one
    element and and the second, [mid, high), has two. We skip the
    optimisation when the number of elements is three or less as
    the first compare in the normal merge will produce the same
    sequence of steps. This optimisation seems to be worthwhile
    for partially ordered lists but some analysis is needed to
    find out how the performance drops to Nlog(N) as the initial
    order diminishes - it may drop very quickly.  */
        
        if (high - low >= 4 && compare(from[middle - 1], from[middle]) <= 0) {
            for (int i = low; i < high; i++) {
                to[i] = from[i];
            }
            return;
        }
        
        // A normal merge.
        for (int i = low; i < high; i++) {
            if (q >= high || (p < middle && compare(from[p], from[q]) <= 0)) {
                to[i] = from[p++];
            } else {
                to[i] = from[q++];
            }
        }
    }
    /**
     * Sort the rows by calling shuttlesort mehtod.
     * Creation date: (12/6/2001 12:16:16 PM)
     * @param: Object sender
     * @return: void
     */
    public void sort(Object sender) {
        checkModel();
        compares = 0;
        shuttlesort((int[]) indexes.clone(), indexes, 0, indexes.length);
        //System.out.println("Compares: "+compares);
        
        /* Create a new model for sorted */
        int len = indexes.length;
        ArrayList data = new ArrayList(len);
        ArrayList oldData = getModel().getDataArrayList();
        ArrayList header = getModel().getIdentifiers();
        for (int i = 0; i < len; i++) {
            data.add(oldData.get(indexes[i]));
        }
        
        setModel(new TableModel(data, header));
    }
    /**
     * Compare two booleans and return result: -1, 0 ,1; assume true > false
     * Creation date: (12/6/2001 11:33:48 AM)
     * @return int
     * @param val1 boolean
     * @param val2 boolean
     */
    private int sortBoolean(boolean val1, boolean val2) {
        int result = 0;
        if (val1 == val2) {
            result = 0;
        } else if (val1) { // Define false < true
            result = 1;
        } else {
            result = -1;
        }
        return result;
    }
    /**
     * Sort the rows by specific column and ascending.
     * Creation date: (12/6/2001 12:17:52 PM)
     * @param: int column
     * @return: void
     */
    public void sortByColumn(int column) {
        sortByColumn(column, true);
    }
    /**
     * Sort the rows by specific column and direction
     * Creation date: (12/6/2001 12:17:52 PM)
     * @param: int column, boolean ascending
     * @return: void
     */
    public void sortByColumn(int column, boolean ascending) {
        this.ascending = ascending;
        sortingColumns.clear();
        sortingColumns.add(new Integer(column));
        sort(this);
        super.tableChanged(new TableModelEvent(this));
    }
    /**
     * Compare two Date and return result: -1, 0 1
     * Creation date: (12/6/2001 11:48:53 AM)
     * @return int
     * @param val1 java.util.Date
     * @param val2 java.util.Date
     */
    private int sortDate(Date val1, Date val2) {
        long n1 = val1.getTime();
        long n2 = val2.getTime();
        
        return sortLong(n1, n2);
    }
    /**
     * Compare two double, return result: -1, 0,1
     * Creation date: (12/6/2001 11:38:15 AM)
     * @return int
     * @param val1 double
     * @param val2 double
     */
    private int sortDouble(double val1, double val2) {
        int result = 0;
        if (val1 < val2) {
            result = -1;
        } else if (val1 > val2) {
            result = 1;
        } else {
            result = 0;
        }
        return result;
    }
    /**
     * Compare to long, return result: -1, 0, 1
     * Creation date: (12/6/2001 11:34:19 AM)
     * @return int
     * @param val1 long
     * @param val2 long
     */
    private int sortLong(long val1, long val2) {
        int result = 0;
        if (val1 < val2) {
            result = -1;
        } else if (val1 > val2) {
            result = 1;
        } else {
            result = 0;
        }
        return result;
    }
    /**
     * Return the compare result of two Numbers
     * Creation date: (12/6/2001 11:16:13 AM)
     * @return boolean
     * @param val1 java.lang.Number
     * @param val2 java.lang.Number
     */
    private int sortNumber(Number val1, Number val2) {
        double d1 = val1.doubleValue();
        double d2 = val2.doubleValue();
        
        return sortDouble(d1, d2);
    }
    /**
     * Compare two Strings and return result: -1, 0, 1
     * Creation date: (12/6/2001 11:43:50 AM)
     * @return int
     * @param val1 java.lang.String
     * @param val2 java.lang.String
     */
    private int sortString(String val1, String val2) {
        int result = 0;
        int re = val1.compareTo(val2);
        
        if (re < 0) {
            result = -1;
        } else if (re > 0) {
            result = 1;
        } else {
            result = 0;
        }
        return result;
    }
    /**
     * Swap two values in an array.
     * Creation date: (12/6/2001 12:19:53 PM)
     * @param: int i, int  j
     * @return: void
     */
    public void swap(int i, int j) {
        int tmp = indexes[i];
        indexes[i] = indexes[j];
        indexes[j] = tmp;
    }
    /**
     * Called when the table changed.
     * Creation date: (12/6/2001 12:20:32 PM)
     * @param:
     * @return:
     */
    public void tableChanged(TableModelEvent e) {
        //System.out.println("Sorter: tableChanged");
        reallocateIndexes();
        super.tableChanged(e);
    }
}
