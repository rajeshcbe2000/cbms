/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 **
 *
 * TableModel.java
 *
 * Created on June 23, 2003, 4:20 PM
 */

package com.see.truetransact.clientutil;

import java.util.ArrayList;

/**
 * Table Model for the JTable
 *
 * @author: Balachandar
 */
public class TableModel extends EnhancedTableModel {
    boolean celledit = false;
    int editColoumnNo = -1;

    public int getEditColoumnNo() {
        return editColoumnNo;
    }

    public void setEditColoumnNo(int editColoumnNo) {
        this.editColoumnNo = editColoumnNo;
    }
    
    /**
     * UNCTableModel constructor comment.
     */
    public TableModel() {
        super();
    }
    /**
     * UNCTableModel constructor comment.
     */
    public TableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }
    /**
     * Insert the method's description here.
     * Creation date: (3/19/2002 5:55:40 PM)
     * @param rows int
     * @param cols int
     */
    public TableModel(int rows, int cols) {
        super(rows, cols);
    }
    /**
     * Insert the method's description here.
     * Creation date: (4/24/2002 11:55:38 AM)
     * @param data java.util.ArrayList
     * @param identifies java.util.ArrayList
     */
    public TableModel(ArrayList data, ArrayList identifies) {
        super(data, identifies);
    }
    /**
     * Insert the method's description here.
     * Creation date: (1/14/2002 12:06:48 PM)
     * @return java.lang.Class
     * @param col int
     */
    public Class getColumnClass(int col) {
        Object obj = getValueAt(0, col);
        
        return obj == null ? Object.class : obj.getClass();
    }
    /**
     * Insert the method's description here.
     * Creation date: (4/24/2002 10:03:16 AM)
     * @return java.util.ArrayList
     */
    public ArrayList getIdentifiers() {
        return columnIdentifiers;
    }
    public ArrayList getRow(int row) {
        ArrayList rowVec = new ArrayList();
        for (int i=0, j=getColumnCount(); i < j; i++) {
            rowVec.add(getValueAt(row, i));
        }
        return rowVec;
    }
    public boolean isCellEditable(int row, int col) {
        //commented by Bala
        //return celledit;
//        System.out.println("########editColoumnNo : "+editColoumnNo);
//        System.out.println("########col : "+col);
        if (editColoumnNo==col) {
            return true;
        }
        return false;
    }
    public void setCellEditable(boolean editable) {
        celledit = editable;
    }
    /**
     * Set the model's data
     * Creation date: (11/22/2001 10:46:11 AM)
     * @param data java.lang.Object[][]
     */
    public void setData(Object[][] data) {
        this.dataArrayList = this.convertToArrayList(data);
    }
    /**
     * Set the model's data
     * Creation date: (11/22/2001 10:46:11 AM)
     * @param data java.lang.ArrayList
     */
    public void setData(ArrayList data) {
        this.dataArrayList = data;
    }
    /**
     * Set the model's data
     * Creation date: (11/22/2001 10:46:11 AM)
     * @param data java.lang.Object[][]
     */
    public void setHeading(Object[] columnNames) {
        this.columnIdentifiers = this.convertToArrayList(columnNames);
    }
    
    public void setHeading(ArrayList heading) {
        this.columnIdentifiers = heading;
    }
}
