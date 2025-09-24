/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * BackDatedEntryDeletionOB.java
 *
 * Created on December 17, 2003, 11:41 AM
 */

package com.see.truetransact.ui.batchprocess.backdatedentrydeletion;

import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.ui.common.viewall.*;
import java.util.Observable;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.regex.Pattern;
import java.util.Date;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.commonutil.DateUtil;

/**
 * @author  bala
 */
public class BackDatedEntryDeletionOB extends Observable {
    private boolean _isAvailable = true;
    private TableModel _tableModel;
    private CTable _tblData;
    private ArrayList _heading;
    private HashMap dataHash;
    private ArrayList data;
    private int dataSize;
    private HashMap lookupMap;
    private ComboBoxModel mainProductTypeModel;
    private ArrayList key;
    private ArrayList value;
    private HashMap keyValue;
    public final int MAXDATA = 1000;
    private ProxyFactory proxy = null;
    /** Creates a new instance of ViewAllOB */
    public BackDatedEntryDeletionOB() {
        try {
            // create the proxy
            proxy = ProxyFactory.createProxy();
            fillDropdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void fillDropdown() throws Exception {
        try {
            lookupMap = new HashMap();
            lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
            lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
            lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");

            final HashMap param = new HashMap();
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
            final ArrayList lookupKey = new ArrayList();
            lookupKey.add("PRODUCTTYPE");
            param.put(CommonConstants.MAP_NAME, null);
            param.put(CommonConstants.PARAMFORQUERY, lookupKey);
            HashMap lookupValues = ClientUtil.populateLookupData(param);

            fillData((HashMap) lookupValues.get("PRODUCTTYPE"));
            mainProductTypeModel = new ComboBoxModel(key, value);
            mainProductTypeModel.removeKeyAndElement("TL");
            mainProductTypeModel.removeKeyAndElement("TD");
            mainProductTypeModel.removeKeyAndElement("AD");
            mainProductTypeModel.removeKeyAndElement("MDS");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private HashMap populateDataLocal(HashMap obj) throws Exception {
        keyValue = proxy.executeQuery(obj, lookupMap);
        return keyValue;
    }
    
    private void fillData(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }
    
    public ArrayList populateData(HashMap mapID, CTable tblData) {
        _tblData = tblData;
        
        // Adding Where Condition
        HashMap whereMap = null;
        if (mapID.containsKey(CommonConstants.MAP_WHERE)) {
            if (mapID.get(CommonConstants.MAP_WHERE) instanceof HashMap)
                whereMap = (HashMap) mapID.get(CommonConstants.MAP_WHERE);
            else 
                System.out.println ("Convert other data type to HashMap:" + mapID);
        } else {
            whereMap = new HashMap();
        }
        
        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
            whereMap.put (CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        }
        if (!whereMap.containsKey(CommonConstants.USER_ID)) {
            whereMap.put (CommonConstants.USER_ID, ProxyParameters.USER_ID);
        }
        
        mapID.put (CommonConstants.MAP_WHERE, whereMap);
        
        System.out.println ("Screen   : " + getClass());
        System.out.println ("Map Name : " + mapID.get(CommonConstants.MAP_NAME));
        System.out.println ("Map      : " + mapID);
        
//        _isAvailable = ClientUtil.setTableModel(mapID, tblData);
        dataHash = ClientUtil.executeTableQuery(mapID);
        _heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        data = (ArrayList) dataHash.get(CommonConstants.TABLEDATA);
        setDataSize(data.size());
        System.out.println("### Data : "+data);
//        String s = new Date(((ArrayList)data.get(0)).get(6).toString()).toString();
//        System.out.println("### Data : "+DateUtil.getDateMMDDYYYY(s));
//        if (getDataSize()<=MAXDATA)
            populateTable();
//        else {
//            ClientUtil.displayAlert("Select some Criteria to view...");
//            TableSorter tableSorter = new TableSorter();
//            tableSorter.addMouseListenerToHeaderInTable(_tblData);
//            TableModel tableModel = new TableModel();
//            tableModel.setHeading(new ArrayList());
//            tableModel.setData(new ArrayList());
//            tableModel.fireTableDataChanged();
//            tableSorter.setModel(tableModel);
//            tableSorter.fireTableDataChanged();
//            
//            _tblData.setModel(tableSorter);
//            _tblData.revalidate();
//        }
        whereMap = null;
        return _heading;
    }

    public void populateTable() {
//        ArrayList heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        boolean dataExist;
        if (_heading != null){
            _isAvailable = true;
            dataExist = true;
            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(_tblData);
            TableModel tableModel = new TableModel();
            tableModel.setHeading(_heading);
            tableModel.setData(data);
            tableModel.fireTableDataChanged();
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();
            _tblData.setAutoResizeMode(0);
            _tblData.doLayout();
            _tblData.setModel(tableSorter);
            _tblData.revalidate();
            
        }else{
            _isAvailable = false;
            dataExist = false;
            
            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(_tblData);
            TableModel tableModel = new TableModel();
            tableModel.setHeading(new ArrayList());
            tableModel.setData(new ArrayList());
            tableModel.fireTableDataChanged();
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();
            
            _tblData.setModel(tableSorter);
            _tblData.revalidate();
            
            ClientUtil.noDataAlert();
        }
//            if (_tblData.getModel() instanceof TableSorter) {
//            _tableModel = ((TableSorter) _tblData.getModel()).getModel();
//        } else {
//            _tableModel = (TableModel) _tblData.getModel();
//        }
//        
//        JTableHeader tblHeader = _tblData.getTableHeader();
//        TableColumnModel tcm = tblHeader.getColumnModel();
//        Enumeration enum = tcm.getColumns();
//
//        String str;
//        _heading = new ArrayList();
//        while (enum.hasMoreElements()) {
//            str = (String) ((TableColumn) enum.nextElement()).getHeaderValue();
//            _heading.add(str);
//        }        
    }
    public void setTable(CTable tbl) {
        _tblData = tbl;
    }
    
    
    public boolean isAvailable() {
        return _isAvailable;
    }
    
    public HashMap fillData(int rowIndexSelected) {
        _tableModel = (TableModel) _tblData.getModel();
        ArrayList rowdata = null;
        
        if (rowIndexSelected > -1) {
            rowdata = _tableModel.getRow(rowIndexSelected);
        }
        
        HashMap hashdata = new HashMap();
        String strColName = null;
        Object obj = null;

        for (int i = 0, j = _tableModel.getColumnCount(); i < j; i++) {
            if (rowdata != null)
                obj = rowdata.get(i);
            
            strColName = _tableModel.getColumnName(i).toUpperCase().trim();
            if (obj != null) {
                hashdata.put(strColName, obj);
            } else {
                hashdata.put(strColName, "");
            }
        }
        rowdata = null;
        hashdata = null;
        obj = null;
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
        ArrayList arrFilterRow = new ArrayList();
        ArrayList arrOriRow = new ArrayList();
        String strArrData;
        int j = data.size();
        if (searchTxt.length() > 0) {
//            if (j<=MAXDATA) {
//                arrFilterRow = data;
//                j=0;
//            }
            for (int i=0; i < j; i++) {
                arrOriRow = (ArrayList)data.get(i);
//                System.out.println("### Data Row : "+arrOriRow);
                strArrData = arrOriRow.get(selCol)!=null ? arrOriRow.get(selCol).toString() : "";
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
        } else {
            for (int i=0; i < j; i++) {
                arrOriRow = (ArrayList)data.get(i);
//                System.out.println("### Data Row : "+arrOriRow);
                strArrData = arrOriRow.get(selCol)!=null ? arrOriRow.get(selCol).toString() : "";
                if (strArrData != null) {
                    strArrData = strArrData.trim();
                    if (strArrData.length() == 0) {
                        arrFilterRow.add(arrOriRow);
                    }
                }
            }
        }
//        System.out.println("### Selected Rows : "+arrFilterRow);
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
        arrFilterRow = null;
        arrOriRow = null;
    }
    
    /**
     * Getter for property dataSize.
     * @return Value of property dataSize.
     */
    public int getDataSize() {
        return dataSize;
    }
    
    /**
     * Setter for property dataSize.
     * @param dataSize New value of property dataSize.
     */
    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    /**
     * Getter for property mainProductTypeModel.
     *
     * @return Value of property mainProductTypeModel.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getMainProductTypeModel() {
        return mainProductTypeModel;
    }

    /**
     * Setter for property mainProductTypeModel.
     *
     * @param mainProductTypeModel New value of property mainProductTypeModel.
     *
     */
    public void setMainProductTypeModel(com.see.truetransact.clientutil.ComboBoxModel mainProductTypeModel) {
        this.mainProductTypeModel = mainProductTypeModel;
    }
    
}

