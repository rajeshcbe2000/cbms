/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ViewSearchOB.java
 *
 * Created on December 17, 2003, 11:41 AM
 */

package com.see.truetransact.ui.common.viewsearch;

import java.util.Observable;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.regex.Pattern;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.uicomponent.CTable;

/**
 * @author  bala
 */
public class ViewSearchOB extends Observable {
    private boolean _isAvailable = true;
    private TableModel _tableModel;
    private CTable _tblData;
    private ArrayList _heading;
    
    /** Creates a new instance of ViewAllOB */
    public ViewSearchOB() {
    }
    
    public ArrayList populateData(HashMap mapID, CTable tblData) {
        _tblData = tblData;
        _isAvailable = ClientUtil.setTableModel(mapID, tblData);
        
        if (tblData.getModel() instanceof TableSorter) {
            _tableModel = ((TableSorter) tblData.getModel()).getModel();
        } else {
            _tableModel = (TableModel) tblData.getModel();
        }
        
        JTableHeader tblHeader = tblData.getTableHeader();
        TableColumnModel tcm = tblHeader.getColumnModel();
        Enumeration enum1 = tcm.getColumns();

        String str;
        _heading = new ArrayList();
        while (enum1.hasMoreElements()) {
            str = (String) ((TableColumn) enum1.nextElement()).getHeaderValue();
            _heading.add(str);
        }
        return _heading;
    }
    
    public void setTable(CTable tbl) {
        _tblData = tbl;
    }
    
    
    public boolean isAvailable() {
        return _isAvailable;
    }
    
    public HashMap fillData(int rowIndexSelected) {
        _tableModel = (TableModel) _tblData.getModel();
        ArrayList rowdata = _tableModel.getRow(rowIndexSelected);

        String data = "";
        HashMap hashdata = new HashMap();
        String strColName = null;
        Object obj = null;

        for (int i = 0, j = _tableModel.getColumnCount(); i < j; i++) {
            obj = rowdata.get(i);
            if (obj != null) {
                data = obj.toString();
            } else {
                data = "";
            }

            strColName = _tableModel.getColumnName(i).toUpperCase().trim();
            hashdata.put(strColName, data);
        }
        return hashdata;
    }
    
    public void refreshTable() {
        TableSorter tableSorter = new TableSorter();
        tableSorter.addMouseListenerToHeaderInTable(_tblData);

        tableSorter.setModel(_tableModel);
        tableSorter.fireTableDataChanged();

        _tblData.setModel(tableSorter);
        _tblData.revalidate();
        
    }
    
    public void searchData(String searchTxt, int selCol, int selColCri, boolean chkCase) {
        if (searchTxt.length() > 0) {
            ArrayList arrFilterRow = new ArrayList();
            ArrayList arrOriRow;
            String strArrData;

            for (int i=0, j=_tblData.getRowCount(); i < j; i++) {
                arrOriRow = _tableModel.getRow(i);
                strArrData = arrOriRow.get(selCol).toString();
                if (strArrData != null) {
                    strArrData = strArrData.trim();
                    if (!chkCase) strArrData = strArrData.toUpperCase();
                    
                    if ((selColCri==2 && strArrData.equals(searchTxt)) || 
                        (selColCri==0 && strArrData.startsWith(searchTxt)) ||
                        (selColCri==1 && strArrData.endsWith(searchTxt))) {
                        arrFilterRow.add(arrOriRow);
                    } else if (selColCri==3) {
                        if (Pattern.matches(searchTxt + "\\w*", strArrData)) 
                            arrFilterRow.add(arrOriRow);
                    }
                }
            }

            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(_tblData);
            
            TableModel tmlNew = new TableModel();
            tmlNew.setHeading(_heading);
            tmlNew.setData(arrFilterRow);
            tmlNew.fireTableDataChanged();
            tableSorter.setModel(tmlNew);
            tableSorter.fireTableDataChanged();
            
            _tblData.setModel(tableSorter);
            _tblData.revalidate();
        }        
    }
}