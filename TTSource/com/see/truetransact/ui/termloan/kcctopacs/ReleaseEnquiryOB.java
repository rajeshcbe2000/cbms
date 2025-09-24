/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ReleaseEnquiryOB.java
 *
 * Created on December 17, 2003, 11:41 AM
 */
package com.see.truetransact.ui.termloan.kcctopacs;

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
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.CObservable;
import java.util.*;
import java.util.Observer;
import java.util.Observable;

/**
 * @author bala
 */
public class ReleaseEnquiryOB extends CObservable {

    private boolean _isAvailable = true;
    private TableModel _tableModel;
    private CTable _tblData;
    private ArrayList _heading;
    private HashMap dataHash;
    private ArrayList data;
    private int dataSize;
    public final int MAXDATA = 1000;
    private ComboBoxModel cbmKccProdId;
    private String cboKCCProdId = "";
    private String txtAccNo;
    private String lblAccNameValue;
    private Date tdtFromDate;
    private Date tdtToDate;
    private ArrayList key;
    private ArrayList value;
    private HashMap linkMap = new HashMap();
    private ProxyFactory proxy;
    public boolean btnRDTransPressed = false;
    public boolean btnOtherDetPressed = false;
    private String txtRelNo;
    private HashMap lookUpHash;
    private HashMap keyValue;
    public boolean btnTransPressed = false;
    private static ReleaseEnquiryOB enquiryOB;

    static {
        try {
            enquiryOB = new ReleaseEnquiryOB();
        } catch (Exception e) {
        }
    }

    public static ReleaseEnquiryOB getInstance() {
        return enquiryOB;
    }

    /**
     * Creates a new instance of ViewAllOB
     */
    public ReleaseEnquiryOB() throws Exception {
        initiate();
    }

    private void initiate() throws Exception {
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            System.err.println("Exception " + e + "Caught");
            e.printStackTrace();
        }
        fillDropDown();
    }

    private void fillDropDown() throws Exception {

        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME, null);

        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME, "Cash.getAccProductAD");
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get("DATA"));
        cbmKccProdId = new ComboBoxModel(key, value);
    }

    private void getKeyValue(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    public void setAccountName() {
        try {
            final HashMap accountNameMap = new HashMap();
            accountNameMap.put("ACC_NUM", getTxtAccNo());
            accountNameMap.put("CLOSECHECK", getTxtAccNo());
            final java.util.List resultList = ClientUtil.executeQuery("getAccountNumberName" + "AD", accountNameMap);
            if (resultList != null && resultList.size() > 0) {
                final HashMap resultMap = (HashMap) resultList.get(0);
                setLblAccNameValue(resultMap.get("CUSTOMER_NAME").toString());
            } else {
                setLblAccNameValue("");
            }
        } catch (Exception e) {
        }
    }

    private void setAccName(String prodType, String actNum) {
        if (CommonUtil.convertObjToStr(prodType).length() > 0) {
            final HashMap accountNameMap = new HashMap();
            accountNameMap.put("ACC_NUM", actNum);
            final List resultList = ClientUtil.executeQuery("getAccountNumberName" + prodType, accountNameMap);
            if (resultList.size() >= 1) {
                final HashMap resultMap = (HashMap) resultList.get(0);
                this.lblAccNameValue = CommonUtil.convertObjToStr(resultMap.get("CUSTOMER_NAME").toString());
            }
        }
        setChanged();
    }

    /**
     * To get data for comboboxes
     */
    private HashMap populateDataLocal(HashMap obj, HashMap lookupMap) throws Exception {
        HashMap keyValue = proxy.executeQuery(obj, lookupMap);
        return keyValue;
    }
    ///as an when customer comes

    public HashMap loanInterestCalculationAsAndWhen(HashMap whereMap) {
        HashMap mapData = new HashMap();
        try {
            java.util.List mapDataList = ClientUtil.executeQuery("", whereMap); //, frame);
            mapData = (HashMap) mapDataList.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("#### MapData :" + mapData);
        return mapData;
    }

    private void fillData(HashMap keyValue) throws Exception {
        key = new ArrayList();
        value = new ArrayList();
        key.add("");
        value.add("");
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    public ArrayList populateData(HashMap mapID, CTable tblData) {
        _tblData = tblData;

        // Adding Where Condition
        HashMap whereMap = null;
        if (mapID.containsKey(CommonConstants.MAP_WHERE)) {
            if (mapID.get(CommonConstants.MAP_WHERE) instanceof HashMap) {
                whereMap = (HashMap) mapID.get(CommonConstants.MAP_WHERE);
            } else {
                System.out.println("Convert other data type to HashMap:" + mapID);
            }
        } else {
            whereMap = new HashMap();
        }

        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
            whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        }
        if (!whereMap.containsKey(CommonConstants.USER_ID)) {
            whereMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        }

        mapID.put(CommonConstants.MAP_WHERE, whereMap);

        System.out.println("Screen   : " + getClass());
        System.out.println("Map Name : " + mapID.get(CommonConstants.MAP_NAME));
        System.out.println("Map      : " + mapID);

        dataHash = ClientUtil.executeTableQuery(mapID);
        System.out.println("$$$$$$$$$dataHash " + dataHash);
        _heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);

        data = (ArrayList) dataHash.get(CommonConstants.TABLEDATA);

        if (btnTransPressed == true || btnOtherDetPressed == true) {
            setProperTransDate();
        }
        System.out.println("%%%%%%%%%data" + data);
        populateTable();
        whereMap = null;
        return _heading;
    }

    private void setProperTransDate() {
        ArrayList arrList;
        for (int i = 0; i < data.size(); i++) {
            arrList = (ArrayList) data.get(i);
            String transDt = CommonUtil.convertObjToStr(arrList.get(0));
            if (transDt.length() > 0) {
                Date transDtWithoutTimeStamp = DateUtil.getDateMMDDYYYY(transDt);
                transDtWithoutTimeStamp = new Date(transDtWithoutTimeStamp.getYear(),
                        transDtWithoutTimeStamp.getMonth(),
                        transDtWithoutTimeStamp.getDate());
                arrList.set(0, DateUtil.getStringDate(transDtWithoutTimeStamp));
            }
            data.set(i, arrList);
        }
        arrList = null;
    }

    private void setSizeTableData() {
        _tblData.getColumnModel().getColumn(0).setPreferredWidth(100);
        _tblData.getColumnModel().getColumn(1).setPreferredWidth(110);
        _tblData.getColumnModel().getColumn(9).setPreferredWidth(55);
        _tblData.getColumnModel().getColumn(10).setPreferredWidth(65);
    }

    public void populateTable() {
        boolean dataExist;
        if (_heading != null) {
            _isAvailable = true;
            dataExist = true;
            TableModel tableModel = new TableModel();
            tableModel.setHeading(_heading);
            tableModel.setData(data);
            tableModel.fireTableDataChanged();
            _tblData.setAutoResizeMode(5);
            _tblData.doLayout();
            _tblData.setModel(tableModel);
            _tblData.revalidate();
            if (btnOtherDetPressed == true) {
                setSizeTableData();
            }
        } else {
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
            if (rowdata != null) {
                obj = rowdata.get(i);
            }

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
        if (_tblData != null && _tableModel != null) {
            _tblData.setModel(_tableModel);
            _tblData.revalidate();
        }

    }

    public void resetForm() {
        setCboKCCProdId("");
        setTxtAccNo("");
        setTxtRelNo("");
        setTdtFromDate(null);
        setTdtToDate(null);
        _tableModel = new TableModel();
        refreshTable();
        ttNotifyObservers();
    }

    public void resetProdId() {
        _tableModel = new TableModel();
        refreshTable();
    }

    public void resetAccno() {
        setTxtRelNo("");
        setTdtFromDate(null);
        setTdtToDate(null);
        _tableModel = new TableModel();
        refreshTable();
    }

    public void resetRelNo() {
        _tableModel = new TableModel();
        refreshTable();
        ttNotifyObservers();
    }

    public void ttNotifyObservers() {
        notifyObservers();
    }

    /**
     * Getter for property dataSize.
     *
     * @return Value of property dataSize.
     */
    public int getDataSize() {
        return dataSize;
    }

    /**
     * Setter for property dataSize.
     *
     * @param dataSize New value of property dataSize.
     */
    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    /**
     * Getter for property cbmProdId.
     *
     * @return Value of property cbmProdId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmKccProdId() {
        return cbmKccProdId;
    }

    /**
     * Setter for property cbmProdId.
     *
     * @param cbmKccProdId New value of property cbmProdId.
     */
    public void setCbmKccProdId(com.see.truetransact.clientutil.ComboBoxModel cbmKccProdId) {
        this.cbmKccProdId = cbmKccProdId;
    }

    /**
     * Getter for property txtAccountNo.
     *
     * @return Value of property txtAccountNo.
     */
    public java.lang.String getTxtAccNo() {
        return txtAccNo;
    }

    /**
     * Setter for property txtAccountNo.
     *
     * @param txtAccountNo New value of property txtAccountNo.
     */
    public void setTxtAccNo(java.lang.String txtAccNo) {
        this.txtAccNo = txtAccNo;
    }

    /**
     * Getter for property tdtFromDate.
     *
     * @return Value of property tdtFromDate.
     */
    public java.util.Date getTdtFromDate() {
        return tdtFromDate;
    }

    /**
     * Setter for property tdtFromDate.
     *
     * @param tdtFromDate New value of property tdtFromDate.
     */
    public void setTdtFromDate(java.util.Date tdtFromDate) {
        this.tdtFromDate = tdtFromDate;
    }

    /**
     * Getter for property tdtToDate.
     *
     * @return Value of property tdtToDate.
     */
    public java.util.Date getTdtToDate() {
        return tdtToDate;
    }

    /**
     * Setter for property tdtToDate.
     *
     * @param tdtToDate New value of property tdtToDate.
     */
    public void setTdtToDate(java.util.Date tdtToDate) {
        this.tdtToDate = tdtToDate;
    }

    /**
     * Getter for property lblAccNameValue.
     *
     * @return Value of property lblAccNameValue.
     */
    public java.lang.String getLblAccNameValue() {
        return lblAccNameValue;
    }

    /**
     * Setter for property lblAccNameValue.
     *
     * @param lblAccNameValue New value of property lblAccNameValue.
     */
    public void setLblAccNameValue(java.lang.String lblAccNameValue) {
        this.lblAccNameValue = lblAccNameValue;
    }

    /**
     * Getter for property linkMap.
     *
     * @return Value of property linkMap.
     */
    public java.util.HashMap getLinkMap() {
        return linkMap;
    }

    /**
     * Setter for property linkMap.
     *
     * @param linkMap New value of property linkMap.
     */
    public void setLinkMap(java.util.HashMap linkMap) {
        this.linkMap = linkMap;
    }

    public String getTxtRelNo() {
        return txtRelNo;
    }

    public void setTxtRelNo(String txtRelNo) {
        this.txtRelNo = txtRelNo;
    }

    // Setter method for cboKCCProdId
    void setCboKCCProdId(String cboKCCProdId) {
        this.cboKCCProdId = cboKCCProdId;
        setChanged();
    }

    // Getter method for cboKCCProdId
    String getCboKCCProdId() {
        return this.cboKCCProdId;
    }
}
