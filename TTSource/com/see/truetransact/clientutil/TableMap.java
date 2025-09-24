/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 **
 *
 * TableMap.java
 *
 * Created on June 23, 2003, 4:20 PM
 */

package com.see.truetransact.clientutil;

/**
 * Provide a table map for sorting functionality
 *
 * @author: Balachandar
 */

import java.util.ArrayList;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class TableMap extends TableModel implements TableModelListener {
    protected TableModel model;
    
    /**
     * Insert the method's description here.
     * Creation date: (2/12/2002 9:49:07 AM)
     */
    public TableMap() {
        super();
    }
    /**
     * Insert the method's description here.
     * Creation date: (2/12/2002 9:49:58 AM)
     * @param data java.lang.Object[][]
     * @param columnNames java.lang.Object[]
     */
    public TableMap(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }
    /**
     * Insert the method's description here.
     * Creation date: (3/19/2002 5:55:40 PM)
     * @param rows int
     * @param cols int
     */
    public TableMap(int rows, int cols) {
        super(rows, cols);
    }
    /**
     * Insert the method's description here.
     * Creation date: (4/24/2002 11:55:38 AM)
     * @param data java.util.ArrayList
     * @param identifies java.util.ArrayList
     */
    public TableMap(ArrayList data, ArrayList identifies) {
        super(data, identifies);
    }
    
    public Class getColumnClass(int aColumn) {
        return model.getColumnClass(aColumn);
    }
    public int getColumnCount() {
        return (model == null) ? 0 : model.getColumnCount();
    }
    public String getColumnName(int aColumn) {
        return model.getColumnName(aColumn);
    }
    public TableModel getModel() {
        return model;
    }
    public int getRowCount() {
        return (model == null) ? 0 : model.getRowCount();
    }
    // By default, implement TableModel by forwarding all messages
    // to the model.
    
    public Object getValueAt(int aRow, int aColumn) {
        return model.getValueAt(aRow, aColumn);
    }
    public boolean isCellEditable(int row, int column) {
        return model.isCellEditable(row, column);
    }
    public void setModel(TableModel model) {
        this.model = model;
        model.addTableModelListener(this);
    }
    public void setValueAt(Object aValue, int aRow, int aColumn) {
        model.setValueAt(aValue, aRow, aColumn);
    }
    /**
     * Implementation of the TableModelListener interface,
     * By default forward all events to all the listeners.
     * Creation date: (11/29/2001 11:21:55 AM)
     * @param:
     * @return:
     */
    public void tableChanged(TableModelEvent e) {
        fireTableChanged(e);
    }
}
