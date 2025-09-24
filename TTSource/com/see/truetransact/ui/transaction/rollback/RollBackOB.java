/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * RollBackOB.java
 *
 * Created on December 17, 2003, 11:41 AM
 */

package com.see.truetransact.ui.transaction.rollback;

import java.util.Observable;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.regex.Pattern;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uicomponent.CTable;

import org.apache.log4j.Logger;

/**
 * @author  bala
 */
public class RollBackOB extends Observable {
    private boolean _isAvailable = true;
    private TableModel _tableModel;
    private CTable _tblData;
    private ArrayList _heading;
    private HashMap dataHash;
    private ArrayList data;
    private int dataSize;
    public final int MAXDATA = 1000;
    
    private ProxyFactory proxy = null;
    private Date currDt;
    private HashMap operationMap;
    private int result;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private final static Logger log = Logger.getLogger(RollBackUI.class);
    HashMap proxyReturnMap = null;
    
    /** Creates a new instance of ViewAllOB */
    public RollBackOB() throws Exception {
        setOperationMap();
        currDt = ClientUtil.getCurrentDate();
        proxy = ProxyFactory.createProxy();
    }
    
    private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "RollBackJNDI");
        operationMap.put(CommonConstants.HOME, "rollback.RollBackHome");
        operationMap.put(CommonConstants.REMOTE, "rollback.RollBack");
    }
    
    public void doActionPerform(HashMap dataMap) throws Exception{
        TTException exception = null;
            HashMap proxyResultMap = new HashMap();
            proxyResultMap = proxy.execute(dataMap,operationMap);
        if (proxyResultMap!=null && (proxyResultMap.containsKey("ROLL_BACK_ID") || proxyResultMap.containsKey("INSUFFICIENT_BALANCE"))) {
                setProxyReturnMap(proxyResultMap);
            }
        System.out.println("proxyReturnMap data :"+proxyReturnMap);
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
//        System.out.println("### Data : "+data);
        if (getDataSize()<=MAXDATA)
            populateTable();
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
            _tblData.setModel(tableSorter);
            _tblData.revalidate();
            if(_heading.get(2) != null && CommonUtil.convertObjToStr(_heading.get(2)).equals("Account No")){
                _tblData.getColumnModel().getColumn(2).setPreferredWidth(110);
            }
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
            if (_tblData.getModel() instanceof TableSorter) {
            _tableModel = ((TableSorter) _tblData.getModel()).getModel();
        } else {
            _tableModel = (TableModel) _tblData.getModel();
        }
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
    
    public void clearTable(CTable tblData) {
            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(tblData);
            TableModel tableModel = new TableModel();
            tableModel.setHeading(new ArrayList());
            tableModel.setData(new ArrayList());
            tableModel.fireTableDataChanged();
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged(); 
            tblData.setModel(tableSorter);
            tblData.revalidate();
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
            ArrayList arrOriRow = new ArrayList();
            String strArrData=null;
            int j = data.size();
//            if (j<=MAXDATA) {
//                arrFilterRow = data;
//                j=0;
//            }
            for (int i=0; i < j; i++) {
                arrOriRow = (ArrayList)data.get(i);
//                System.out.println("### Data Row : "+arrOriRow);
                if (arrOriRow.get(selCol)!=null) 
                    strArrData = arrOriRow.get(selCol).toString();
                if (strArrData != null && strArrData.length()>0) {
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
                strArrData = null;
            }
//            System.out.println("### Selected Rows : "+arrFilterRow);
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
     * Getter for property result.
     * @return Value of property result.
     */
    public int getResult() {
        return result;
    }
    
    /**
     * Setter for property result.
     * @param result New value of property result.
     */
    public void setResult(int result) {
        this.result = result;
    }
    
    /**
     * Getter for property proxyReturnMap.
     * @return Value of property proxyReturnMap.
     */
    public java.util.HashMap getProxyReturnMap() {
        return proxyReturnMap;
    }
    
    /**
     * Setter for property proxyReturnMap.
     * @param proxyReturnMap New value of property proxyReturnMap.
     */
    public void setProxyReturnMap(java.util.HashMap proxyReturnMap) {
        this.proxyReturnMap = proxyReturnMap;
    }
    
}
