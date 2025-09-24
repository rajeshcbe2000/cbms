/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AuthorizeOB.java
 *
 * Created on March 3, 2004, 1:46 PM
 */

package com.see.truetransact.ui.transaction.common;

import java.util.Observable;
import java.util.List;
import java.util.Iterator;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.regex.Pattern;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.transferobject.transaction.reconciliation.ReconciliationTO;

import com.see.truetransact.clientproxy.ProxyParameters;

/** Authorize Observable is the supporting class for Authorize UI
 *
 * @author bala
 */
public class TransHOCommonOB extends Observable {
    private boolean _isAvailable = true;
    private TableModel _tableModel;
    private CTable _tblData;
    private ArrayList _heading;
    private ArrayList key = new ArrayList();
    private ArrayList value = new ArrayList();
    ArrayList data = new ArrayList();            
    private HashMap outPutMap = new HashMap();
    ReconciliationTO reconciliationTO = null;
    private String totalAmountValue = ""; 
    public String editTrans = "";
    public boolean exceedsAmt = false;
    public String cboBranchValue = "";
    public String cboCategoryVal = "";
    public ComboBoxModel cbmBranchValue,cbmCategoryVal;
    /** Creates a new instance of AuthorizeOB */
    public TransHOCommonOB() {
        try{
            List lst = (List)ClientUtil.executeQuery("getAllBranchesList", null);
            getMap(lst);
            HashMap lookUpHash = new HashMap();
            HashMap keyValue = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME,null);
            final ArrayList lookup_keys = new ArrayList();
            
            lookup_keys.add("HO_MODULE_CATEGORY");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookUpHash);
            
            getKeyValue((HashMap)keyValue.get("HO_MODULE_CATEGORY"));
            cbmCategoryVal = new ComboBoxModel(key,value);
            System.out.println("########ListForAgent : "+lst);

        }catch(NullPointerException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }    
    private void getMap(List list) {
        try{
            ArrayList key = new ArrayList();
            ArrayList value = new ArrayList();

            //The first values in the ArrayList key and value are empty String to display the
            //first row of all dropdowns to be empty String
            key.add("");
            value.add("");
            for (int i=0, j=list.size(); i < j; i++) {
                key.add(((HashMap)list.get(i)).get("KEY"));
                value.add(((HashMap)list.get(i)).get("VALUE"));
            }
            cbmBranchValue = new ComboBoxModel(key,value);
        }catch(Exception e){
            
        }
    }

    
    /** updateStatus method used to update the database field based on the UI button
     * pressed
     *
     * @param map       HashMap from UI which is passed as a argument to Authorize UI constructor
     * @param status    Passed by UI. (Authorize, Reject, Exception - statuses)
     */
    public void updateStatus(HashMap map) {
        /*ArrayList selectedList = getSelected();
        String strUpdateMap = (String) map.get(CommonConstants.UPDATE_MAP_NAME);
        HashMap dataMap;
        for (int i=0, j=selectedList.size(); i < j; i++) {
            dataMap = (HashMap) selectedList.get(i);
            dataMap.put ("STATUS", status);
            dataMap.put ("USER_ID", userID);
            ClientUtil.execute(strUpdateMap, dataMap);
        }*/
        populateData(map, _tblData);
    }
    public ArrayList getSelectedSingleClick(int selectedColumn,int row, double enteredAmt, CTable tblData) {
        ArrayList selectedList = new ArrayList();
        selectedList = (ArrayList)data.get(row);
        String value = CommonUtil.convertObjToStr(((ArrayList)data.get(row)).get(0));        
        double presentAmt = CommonUtil.convertObjToDouble(((ArrayList)data.get(row)).get(7)).doubleValue();
        double balance = CommonUtil.convertObjToDouble(((ArrayList)data.get(row)).get(6)).doubleValue();
        String presentTransId = "";
        presentTransId = CommonUtil.convertObjToStr(((ArrayList)data.get(row)).get(10));
        if(!presentTransId.equals("") && !editTrans.equals(presentTransId)){
//            if(value.equals("false"))
                selectedList.set(0,new Boolean(false));
//            if(value.equals("true"))
//                selectedList.set(0,new Boolean(true));
            data.set(row, selectedList);
            setTblModel(_tblData, data, _heading);                
            ClientUtil.showAlertWindow("Selection not allowed..."+"\n"+"Transaction already selected in : " +" "+
            presentTransId+"  and authorization is pending");
        }else{
            exceedsAmt = false;
            if((presentAmt+balance)>=enteredAmt){
                double actualAmt = balance + presentAmt;
                double reconcile = actualAmt - enteredAmt;
                if(selectedColumn == 0){
                    if(value.equals("true")){
                        selectedList.set(0, new Boolean(true));
                        enteredAmt = actualAmt;
                        reconcile = 0;
                    }else if(value.equals("false")){
                        selectedList.set(0, new Boolean(false));
                        enteredAmt = 0;
                        reconcile = actualAmt;
                    }
                    selectedList.set(7, new Double(enteredAmt));
                    selectedList.set(6, new Double(reconcile));
                    setTotalAmountValue(String.valueOf(enteredAmt+reconcile));
                    data.set(row, selectedList);
                    setTblModel(_tblData, data, _heading);     
                }
            }
        }
        return data;
    }
    
    public ArrayList getSelected(int row, double enteredAmt, CTable tblData) {
        ArrayList selectedList = new ArrayList();
        selectedList = (ArrayList)data.get(row);
        String value = CommonUtil.convertObjToStr(((ArrayList)data.get(row)).get(0));        
        double presentAmt = CommonUtil.convertObjToDouble(((ArrayList)data.get(row)).get(7)).doubleValue();
        double balance = CommonUtil.convertObjToDouble(((ArrayList)data.get(row)).get(6)).doubleValue();
        exceedsAmt = false;
        if(enteredAmt>0){
            if((presentAmt+balance)>=enteredAmt){
                double actualAmt = balance + presentAmt;
                double reconcile = actualAmt - enteredAmt;
                if(reconcile>=0){
                    if(enteredAmt>0)
                        selectedList.set(0, new Boolean(true));
                    else if(presentAmt > 0){
                        selectedList.set(0, new Boolean(false));
                        enteredAmt = presentAmt;
                    }else
                        selectedList.set(0, new Boolean(false));
                    selectedList.set(7, new Double(enteredAmt));
                    selectedList.set(6, new Double(reconcile));
                    setTotalAmountValue(String.valueOf(enteredAmt+reconcile));
                    data.set(row, selectedList);
                    setTblModel(_tblData, data, _heading);     
                }else{
                    exceedsAmt = true;
                    ClientUtil.showAlertWindow("Amount Exceeding the balance available");                
                }
            }else{
                exceedsAmt = true;
                ClientUtil.showAlertWindow("Amount Exceeding the balance available");
            }
        }
        return data;
    }
        
    /** Retrives data and populates the CTable using TableModel
     *
     * @param mapID     HashMap used to retrive data from DB
     * @param tblData   CTable object used to update the table with TableModel
     * @return          Returns ArrayList for populating Search Combobox
     */
    public ArrayList populateData(HashMap mapID, CTable tblData) {
        //return AuthorizeOB.populateData(mapID, tblData);
        HashMap whereMap = null;
        
        _tblData = tblData;
        
        if (mapID.containsKey(CommonConstants.MAP_WHERE))
            whereMap = (HashMap) mapID.get(CommonConstants.MAP_WHERE);
        else
            whereMap = new HashMap();

        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
            whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        }
        if (!whereMap.containsKey(CommonConstants.USER_ID)) {
            whereMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        }
        if (!whereMap.containsKey(CommonConstants.AUTHORIZESTATUS) && mapID.containsKey(CommonConstants.AUTHORIZESTATUS)) {
            whereMap.put(CommonConstants.AUTHORIZESTATUS, mapID.get(CommonConstants.AUTHORIZESTATUS));
        }

        System.out.println("Screen   : " + getClass());
        System.out.println("Map Name : " + mapID.get(CommonConstants.MAP_NAME));
        System.out.println("Map      : " + mapID + ":" + whereMap);

        outPutMap = ClientUtil.executeTableQuery(mapID);
        _heading = (ArrayList) outPutMap.get(CommonConstants.TABLEHEAD);
            
        ArrayList arrList = new ArrayList();            
        if(!mapID.containsKey("EDIT_MAP_LIST") && !mapID.containsKey("AUTHORIZE_MODE")){
            String editTransId = "";
            editTransId = CommonUtil.convertObjToStr(mapID.get("TRANS_ID"));
            data = (ArrayList) outPutMap.get(CommonConstants.TABLEDATA);
            if (_heading!=null && _heading.size()>0)
                _heading.add(0, "Select");
            if (data!=null && data.size()>0)
                for (int i=0; i<data.size();i++) {
                    String presentTransId = "";
                    arrList = (ArrayList)data.get(i);
                    double presentAmt = CommonUtil.convertObjToDouble(((ArrayList)data.get(i)).get(6)).doubleValue();
                    presentTransId = CommonUtil.convertObjToStr(((ArrayList)data.get(i)).get(9));
                    if(mapID.containsKey("EDIT_MODE")){
                        if(presentAmt>0 && editTransId.equals(presentTransId)){
                            double balance = CommonUtil.convertObjToDouble(((ArrayList)data.get(i)).get(5)).doubleValue();
                            double reconcile = balance - presentAmt;
                            arrList.add(0, new Boolean(true));
                            arrList.set(6, new Double(reconcile));
                        }else
                            arrList.add(0, new Boolean(false));
                    }else if(mapID.containsKey("NEW_MODE")){
                        if(presentAmt>0 && !presentTransId.equals("") && presentTransId.length()>0){
                            double balance = CommonUtil.convertObjToDouble(((ArrayList)data.get(i)).get(5)).doubleValue();
                            double reconcile = balance - presentAmt;
                            arrList.add(0, new Boolean(false));
                            arrList.set(6, new Double(reconcile));
                        }else
                            arrList.add(0, new Boolean(false));                        
                    }else
                        arrList.add(0, new Boolean(false));
                    data.set(i, arrList);
                }
        }
//------------------------------------upstair dont touch----------------------        
        if(mapID.containsKey("EDIT_MAP_LIST") && !mapID.containsKey("AUTHORIZE_MODE")){
            String editTransId = "";
            editTransId = CommonUtil.convertObjToStr(mapID.get("TRANS_ID"));
            data = (ArrayList) outPutMap.get(CommonConstants.TABLEDATA);
            if (_heading!=null && _heading.size()>0)
                _heading.add(0, "Select");
            if (data!=null && data.size()>0)
                for (int i=0; i<data.size();i++) {
                    String presentTransId = "";
                    arrList = (ArrayList)data.get(i);
                    double presentAmt = CommonUtil.convertObjToDouble(((ArrayList)data.get(i)).get(6)).doubleValue();
                    presentTransId = CommonUtil.convertObjToStr(((ArrayList)data.get(i)).get(9));
                    if(mapID.containsKey("EDIT_MODE")){
                        if(presentAmt>0 && editTransId.equals(presentTransId)){
                            double balance = CommonUtil.convertObjToDouble(((ArrayList)data.get(i)).get(5)).doubleValue();
                            double reconcile = balance - presentAmt;
                            arrList.add(0, new Boolean(true));
                            arrList.set(6, new Double(reconcile));
                        }else
                            arrList.add(0, new Boolean(false));
                    }else if(mapID.containsKey("NEW_MODE")){
                        if(presentAmt>0 && !presentTransId.equals("") && presentTransId.length()>0){
                            double balance = CommonUtil.convertObjToDouble(((ArrayList)data.get(i)).get(5)).doubleValue();
                            double reconcile = balance - presentAmt;
                            arrList.add(0, new Boolean(false));
                            arrList.set(6, new Double(reconcile));
                        }else
                            arrList.add(0, new Boolean(false));                        
                    }else
                        arrList.add(0, new Boolean(false));
                    data.set(i, arrList);
                }
        }
        if(mapID.containsKey("AUTHORIZE_MODE")){
            String editTransId = "";
            editTransId = CommonUtil.convertObjToStr(mapID.get("TRANS_ID"));
            data = (ArrayList) outPutMap.get(CommonConstants.TABLEDATA);
            if (_heading!=null && _heading.size()>0)
                _heading.add(0, "Select");
            if (data!=null && data.size()>0)
                for (int i=0; i<data.size();i++) {
                    String presentTransId = "";
                    arrList = (ArrayList)data.get(i);
                    double presentAmt = CommonUtil.convertObjToDouble(((ArrayList)data.get(i)).get(6)).doubleValue();
                    presentTransId = CommonUtil.convertObjToStr(((ArrayList)data.get(i)).get(9));
                    if(mapID.containsKey("AUTHORIZE_MODE")){
                        if(presentAmt>0 && editTransId.equals(presentTransId)){
                            double balance = CommonUtil.convertObjToDouble(((ArrayList)data.get(i)).get(5)).doubleValue();
                            double reconcile = balance - presentAmt;
                            arrList.add(0, new Boolean(true));
                            arrList.set(6, new Double(reconcile));
                        }else
                            arrList.add(0, new Boolean(false));
                    }else if(mapID.containsKey("NEW_MODE")){
                        if(presentAmt>0 && !presentTransId.equals("") && presentTransId.length()>0){
                            double balance = CommonUtil.convertObjToDouble(((ArrayList)data.get(i)).get(5)).doubleValue();
                            double reconcile = balance - presentAmt;
                            arrList.add(0, new Boolean(false));
                            arrList.set(6, new Double(reconcile));
                        }else
                            arrList.add(0, new Boolean(false));                        
                    }else
                        arrList.add(0, new Boolean(false));
                    data.set(i, arrList);
                }
        }        
        System.out.println("### Data : "+data);
        populateTable();
        whereMap = null;
        return _heading;
    }
    
    public void populateTable() {
        boolean dataExist;
        if (_heading != null){
            _isAvailable = true;
            dataExist = true;         
            setTblModel(_tblData, data, _heading); 
            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(_tblData);
            TableModel tableModel = new TableModel(data, _heading) {
                    public boolean isCellEditable(int rowIndex, int mColIndex) {
                        if (mColIndex == 0) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                };

            tableModel.fireTableDataChanged();
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();

            _tblData.setModel(tableSorter);
            _tblData.revalidate();

            if (_tblData.getModel() instanceof TableSorter) {
                _tableModel = ((TableSorter) _tblData.getModel()).getModel();
            } else {
                _tableModel = (TableModel) _tblData.getModel();
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
      
    }
       
    private void setTblModel(CTable tbl, ArrayList tblData, ArrayList head) {
        TableSorter tableSorter = new TableSorter();
        tableSorter.addMouseListenerToHeaderInTable(tbl);
        TableModel tableModel = new TableModel(tblData, head) {
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                if (mColIndex == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        
        tableModel.fireTableDataChanged();
        tableSorter.setModel(tableModel);
        tableSorter.fireTableDataChanged();
        
        tbl.setModel(tableSorter);
        tbl.revalidate();
    }
    /** Table Object Setter method
     *
     * @param tbl   CTable Object
     */
    public void setTable(CTable tbl) {
        _tblData = tbl;
    }
    
    
    /** Is Data Available or not checking Method
     *
     * @return  Returns boolean
     */
    public boolean isAvailable() {
        return _isAvailable;
    }
    
    /** fillData populates the UI based on the table row selected
     *
     * @param rowIndexSelected  Selected Table Row index
     * @return                  Returns HashMap with Table Column &
     * Row values for the selected row.
     */
    public HashMap fillData(int rowIndexSelected) {
        _tableModel = (TableModel) _tblData.getModel();
        ArrayList rowdata = _tableModel.getRow(rowIndexSelected);
        
        HashMap hashdata = new HashMap();
        String strColName = null;
        Object obj = null;
        
        for (int i = 0, j = _tableModel.getColumnCount(); i < j; i++) {
            obj = rowdata.get(i);
            
            strColName = _tableModel.getColumnName(i).toUpperCase().trim();
            //            hashdata.put(strColName, CommonUtil.convertObjToStr(obj));
            
            if (obj != null) {
                hashdata.put(strColName, obj);
            } else {
                hashdata.put(strColName, "");
            }
        }
        
        hashdata.put(CommonConstants.HIERARCHY_ID, ProxyParameters.HIERARCHY_ID);
        // Adding Authorization Date
        hashdata.put("AUTHORIZEDT", ClientUtil.getCurrentDate());
        
        return hashdata;
    }
    
    /** Getter method for TableModel
     * @return Returns TableModel
     */
    public TableModel getTableModel() {
        return _tableModel;
    }
    
    /** Search used to update the table model based on the search criteria given by the
     * user.
     *
     * @param searchTxt     Search Text which is entered by the user
     * @param selCol        Colunm selected from the combobox
     * @param selColCri     Condition selected from the condition combobox
     * @param chkCase       Match case checking
     */
    public void searchData(String searchTxt, int selCol, int selColCri) {
        if (searchTxt.length() > 0) {
            ArrayList arrFilterRow = new ArrayList();
            ArrayList arrOriRow;
            String strArrData;
            
            for (int i=0, j=_tblData.getRowCount(); i < j; i++) {
                arrOriRow = _tableModel.getRow(i);
                strArrData = arrOriRow.get(selCol).toString();
                if (strArrData != null) {
                    strArrData = strArrData.trim();
                    //                    if (!chkCase) strArrData = strArrData.toUpperCase();
                    
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
            
            TableModel tmlNew = new TableModel(arrFilterRow, _heading) {
                public boolean isCellEditable(int rowIndex, int mColIndex) {
                    if (mColIndex == 0) {
                        return true;
                    } else {
                        return false;
                    }
                }
            };
            
            tmlNew.fireTableDataChanged();
            tableSorter.setModel(tmlNew);
            tableSorter.fireTableDataChanged();
            
            _tblData.setModel(tableSorter);
            _tblData.revalidate();
        }
    }
    
    /**
     * Getter for property totalAmountValue.
     * @return Value of property totalAmountValue.
     */
    public java.lang.String getTotalAmountValue() {
        return totalAmountValue;
    }
    
    /**
     * Setter for property totalAmountValue.
     * @param totalAmountValue New value of property totalAmountValue.
     */
    public void setTotalAmountValue(java.lang.String totalAmountValue) {
        this.totalAmountValue = totalAmountValue;
    }
    
    /**
     * Getter for property cboBranchValue.
     * @return Value of property cboBranchValue.
     */
    public java.lang.String getCboBranchValue() {
        return cboBranchValue;
    }
    
    /**
     * Setter for property cboBranchValue.
     * @param cboBranchValue New value of property cboBranchValue.
     */
    public void setCboBranchValue(java.lang.String cboBranchValue) {
        this.cboBranchValue = cboBranchValue;
    }
    
    /**
     * Getter for property cbmBranchValue.
     * @return Value of property cbmBranchValue.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmBranchValue() {
        return cbmBranchValue;
    }
    
    /**
     * Setter for property cbmBranchValue.
     * @param cbmBranchValue New value of property cbmBranchValue.
     */
    public void setCbmBranchValue(com.see.truetransact.clientutil.ComboBoxModel cbmBranchValue) {
        this.cbmBranchValue = cbmBranchValue;
    }
    
    /**
     * Getter for property cbmCategoryVal.
     * @return Value of property cbmCategoryVal.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCategoryVal() {
        return cbmCategoryVal;
    }
    
    /**
     * Setter for property cbmCategoryVal.
     * @param cbmCategoryVal New value of property cbmCategoryVal.
     */
    public void setCbmCategoryVal(com.see.truetransact.clientutil.ComboBoxModel cbmCategoryVal) {
        this.cbmCategoryVal = cbmCategoryVal;
    }
    
    /**
     * Getter for property cboCategoryVal.
     * @return Value of property cboCategoryVal.
     */
    public java.lang.String getCboCategoryVal() {
        return cboCategoryVal;
    }
    
    /**
     * Setter for property cboCategoryVal.
     * @param cboCategoryVal New value of property cboCategoryVal.
     */
    public void setCboCategoryVal(java.lang.String cboCategoryVal) {
        this.cboCategoryVal = cboCategoryVal;
    }
    
}