/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * PingAllOB.java
 *
 * Created on December 17, 2003, 11:41 AM
 */

package com.see.truetransact.ui.sysadmin.tools;

import java.util.Observable;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Enumeration;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.ping.*;

/**
 * @author  bala
 */
public class PingAllOB extends Observable {
    private boolean _isAvailable = true;
    private TableModel _tableModel;
    private CTable _tblData;
    private ArrayList _heading;
    
    /** Creates a new instance of ViewAllOB */
    public PingAllOB() {
    }
    
    public ArrayList populateData(CTable tblData) {
        _tblData = tblData;
        
        List list = ClientUtil.executeQuery("getSelectBranchIPDetails", null);
        
        ArrayList hostList = new ArrayList();
        ArrayList portList = new ArrayList();
        for (int i=0, j=list.size(); i < j; i++) {
            hostList.add(CommonUtil.convertObjToStr(((HashMap) list.get(i)).get("IP_ADDR")));
            portList.add(CommonUtil.convertObjToStr(((HashMap) list.get(i)).get("PORT")));
        }
        
        HashMap pingMap = new Ping().pingAll(hostList.toArray(), portList.toArray());
//        System.out.println (pingMap);
        /*_isAvailable = list.size() > 0 ? true : false;*/
        
        ArrayList heading = new ArrayList();
        ArrayList data = new ArrayList();
        ArrayList colData = new ArrayList();

        HashMap map = (HashMap) list.get(0);
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            heading.add((String) iterator.next());
        }
        heading.add (PingTarget.PINGRESULT);
        
        String cellData="", keyData="";
        for (int i=0, j=list.size(); i < j; i++) {
            map = (HashMap) list.get(i);
            keyData = (String) map.get("IP_ADDR");
            colData = new ArrayList();
            iterator = map.values().iterator();
            while (iterator.hasNext()) {
                cellData = CommonUtil.convertObjToStr(iterator.next());
                colData.add(cellData);
            }
            if (pingMap.containsKey(keyData)) {
                cellData = (String) ((HashMap) pingMap.get(keyData)).get(PingTarget.PINGRESULT);
                if (cellData.equals(PingTarget.UNKNOWN) || 
                   cellData.equals(PingTarget.TIMEOUT)) {
                    cellData = "<html><font color=red>" + cellData + "</font></html>";
                }
            } else {
                cellData = "null";
            }
            colData.add(cellData);
            data.add(colData);
        }
        
        TableSorter tableSorter = new TableSorter();
        tableSorter.addMouseListenerToHeaderInTable(tblData);
        TableModel tableModel = new TableModel();
        tableModel.setHeading(heading);
        tableModel.setData(data);
        tableModel.fireTableDataChanged();
        tableSorter.setModel(tableModel);
        tableSorter.fireTableDataChanged();

        tblData.setModel(tableSorter);
        tblData.revalidate();
        
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
                data = CommonUtil.convertObjToStr(obj);
            } else {
                data = "";
            }

            strColName = _tableModel.getColumnName(i).toUpperCase().trim();
            hashdata.put(strColName, data);
        }
        return hashdata;
    }
    
    public TableModel getTableModel() {
        return _tableModel;
    }
    
    public void searchData(String searchTxt, int selCol, int selColCri, boolean chkCase) {
        if (searchTxt.length() > 0) {
            ArrayList arrFilterRow = new ArrayList();
            ArrayList arrOriRow;
            String strArrData;

            for (int i=0, j=_tblData.getRowCount(); i < j; i++) {
                arrOriRow = _tableModel.getRow(i);
                strArrData = CommonUtil.convertObjToStr(arrOriRow.get(selCol));
                if (strArrData != null) {
                    strArrData = strArrData.trim();
                    if (!chkCase) strArrData = strArrData.toUpperCase();
                    
                    if ((selColCri==0 && strArrData.equals(searchTxt)) || 
                        (selColCri==1 && strArrData.startsWith(searchTxt)) ||
                        (selColCri==2 && strArrData.endsWith(searchTxt))) {
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