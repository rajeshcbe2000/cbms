/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * BalanceOB.java
 *
 * Created on December 17, 2003, 11:41 AM
 */
package com.see.truetransact.ui.transaction.Balance;

import com.see.truetransact.ui.transaction.rollback.*;
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
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.uicomponent.CTable;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author bala
 */
public class BalanceOB extends Observable {

    final ArrayList tableTitle = new ArrayList();
    final ArrayList tableTitle1 = new ArrayList();
    private boolean _isAvailable = true;
    private TableModel _tableModel;
    private CTable _tblData;
    private ArrayList _heading;
    private HashMap dataHash;
    private List finalTableList = null;
    private ArrayList data;
    private int dataSize;
    public final int MAXDATA = 1000;
    private EnhancedTableModel tblProduct;
    private EnhancedTableModel tblData;
    private String selectedButton = "";
    private String cbobranch;
    private ComboBoxModel cbmbranch;

    public String getSelectedButton() {
        return selectedButton;
    }

    public void setSelectedButton(String selectedButton) {
        this.selectedButton = selectedButton;
    }

    public EnhancedTableModel getTblData() {
        return tblData;
    }

    public void setTblData(EnhancedTableModel tblData) {
        this.tblData = tblData;
    }

    public EnhancedTableModel getTblProduct() {
        return tblProduct;
    }

    public void setTblProduct(EnhancedTableModel tblProduct) {
        this.tblProduct = tblProduct;
    }
    public String getCbobranch() {
        return cbobranch;
    }
    public void setCbobranch(String cbobranch) {
        this.cbobranch = cbobranch;
    }
    public ComboBoxModel getCbmbranch() {
        return cbmbranch;
    }
    public void setCbmbranch(ComboBoxModel cbmbranch) {
        this.cbmbranch = cbmbranch;
    }
    private EnhancedTableModel tblCategory;
    private ArrayList rdList;
    private ProxyFactory proxy = null;
    private int actionType;
    private List finalList = null;
    private HashMap map;
    private Date currDt;
    private HashMap operationMap;
    private int result;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger log = Logger.getLogger(BalanceUI.class);
    HashMap proxyReturnMap = null;

    /**
     * Creates a new instance of ViewAllOB
     */
    public BalanceOB() throws Exception {
        setOperationMap();
        currDt = ClientUtil.getCurrentDate();
        proxy = ProxyFactory.createProxy();
        setTableTitle();
        setTableTitle1();
    }

    private void setTableTitle() {
        tableTitle.add("Select");
        tableTitle.add("Product Id");
        tableTitle.add("Product Description");

    }

    private void setTableTitle1() {
        tableTitle1.add("SI_NO");
        tableTitle1.add("DESCRIPTION");
        tableTitle1.add("OPENING_NO");
        tableTitle1.add("OPENING_AMT");
        tableTitle1.add("PAYMENT_NO");
        tableTitle1.add("PAYMENT_AMT");
        tableTitle1.add("RECEIPT_NO");
        tableTitle1.add("RECEIPT_AMT");
        tableTitle1.add("BALANCE_NO");
        tableTitle1.add("BALANCE_AMT");
        tableTitle1.add("OVRDUE_NO");
        tableTitle1.add("OVRDUE_AMT");
    }

    private void setOperationMap() throws Exception {
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "RollBackJNDI");
        operationMap.put(CommonConstants.HOME, "rollback.RollBackHome");
        operationMap.put(CommonConstants.REMOTE, "rollback.RollBack");
    }

    public void doActionPerform(HashMap dataMap) throws Exception {
        TTException exception = null;
        HashMap proxyResultMap = new HashMap();
        proxyResultMap = proxy.execute(dataMap, operationMap);
        if (proxyResultMap != null && (proxyResultMap.containsKey("ROLL_BACK_ID") || proxyResultMap.containsKey("INSUFFICIENT_BALANCE"))) {
            setProxyReturnMap(proxyResultMap);
        }
        System.out.println("proxyReturnMap data :" + proxyReturnMap);
    }
public void fillDropDown() {
        ArrayList key = new ArrayList();
        ArrayList value = new ArrayList();
        HashMap mapShare = new HashMap();
        List keyValue = null;
        keyValue = ClientUtil.executeQuery("getbranches", mapShare);
        System.out.println("keyValue=======" + keyValue);
        key.add("");
        value.add("");
        if (keyValue != null && keyValue.size() > 0) {
            for (int i = 0; i < keyValue.size(); i++) {
                mapShare = (HashMap) keyValue.get(i);
                key.add(mapShare.get("BRANCH_CODE"));
                value.add(mapShare.get("BRANCH_CODE"));
            }
        }
        System.out.println("key======" + key);
        System.out.println("value======" + value);
        cbmbranch = new ComboBoxModel(key,value);
        key = null;
        value = null;
        keyValue.clear();
        keyValue = null;
        mapShare.clear();
        mapShare = null;
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
        System.out.println("getCbmbranch()======"+getCbmbranch().getKeyForSelected());
        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
            whereMap.put("BRANCH_CODE",getCbmbranch().getKeyForSelected());
        }
        if (!whereMap.containsKey(CommonConstants.USER_ID)) {
            whereMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        }

        mapID.put(CommonConstants.MAP_WHERE, whereMap);

        System.out.println("Screen   : " + getClass());
        System.out.println("Map Name : " + mapID.get(CommonConstants.MAP_NAME));
        System.out.println("Map      : " + mapID);

//        _isAvailable = ClientUtil.setTableModel(mapID, tblData);
        dataHash = ClientUtil.executeTableQuery(mapID);
        _heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        System.out.println("_heading===" + _heading);
        data = (ArrayList) dataHash.get(CommonConstants.TABLEDATA);
        ArrayList tblDatanew = new ArrayList();
        for (int i = 0; i < data.size(); i++) {
            List tmpList = (List) data.get(i);
            ArrayList newList = new ArrayList();
            newList.add(new Boolean(false));
            newList.add(tmpList.get(0));
            newList.add(tmpList.get(1));
            tblDatanew.add(newList);
        }
        tblProduct = new EnhancedTableModel((ArrayList) tblDatanew, tableTitle);
        setDataSize(data.size());
        return _heading;
    }

    public ArrayList populateData1(HashMap mapID, CTable tblData1) {
        _tblData = tblData1;
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
        System.out.println("dataHash === : " + dataHash);
        _heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        System.out.println("_heading===" + _heading);
        data = (ArrayList) dataHash.get(CommonConstants.TABLEDATA);
        setDataSize(data.size());
        if (getDataSize() <= MAXDATA) {
            populateTable();
        }
        return _heading;
    }

    public void populateTable() {
        boolean dataExist;
        if (_heading != null) {
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
            if (getSelectedButton().equals("LOAN_BALANCE")) {
                _tblData.getColumnModel().getColumn(0).setPreferredWidth(80);
                _tblData.getColumnModel().getColumn(1).setPreferredWidth(80);
                _tblData.getColumnModel().getColumn(2).setPreferredWidth(200);
            }else if(getSelectedButton().equals("BORROWING_BALANCE") || getSelectedButton().equals("INVESTMENT_BALANCE")){ 
                _tblData.getColumnModel().getColumn(0).setPreferredWidth(200);
                _tblData.getColumnModel().getColumn(1).setPreferredWidth(80);
            } else {
                _tblData.getColumnModel().getColumn(0).setPreferredWidth(85);
                _tblData.getColumnModel().getColumn(1).setPreferredWidth(200);
                _tblData.getColumnModel().getColumn(2).setPreferredWidth(100);
            }
            _tblData.getColumnModel().getColumn(3).setPreferredWidth(100);
            _tblData.getColumnModel().getColumn(4).setPreferredWidth(100);
            _tblData.getColumnModel().getColumn(5).setPreferredWidth(100);
            _tblData.getColumnModel().getColumn(6).setPreferredWidth(100);
            if (getSelectedButton().equals("LOAN_BALANCE") || getSelectedButton().equals("BORROWING_BALANCE") || getSelectedButton().equals("DEPOSIT_BALANCE")) {
                _tblData.getColumnModel().getColumn(7).setPreferredWidth(100);
                _tblData.getColumnModel().getColumn(8).setPreferredWidth(100);
                if (getSelectedButton().equals("LOAN_BALANCE") || getSelectedButton().equals("DEPOSIT_BALANCE")){
                    _tblData.getColumnModel().getColumn(9).setPreferredWidth(100);
                    if (getSelectedButton().equals("LOAN_BALANCE")){
                        _tblData.getColumnModel().getColumn(10).setPreferredWidth(100);
                        _tblData.getColumnModel().getColumn(11).setPreferredWidth(100);
                        _tblData.getColumnModel().getColumn(12).setPreferredWidth(100);
                    }
                }
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
            _tblData.setAutoResizeMode(0);
            _tblData.doLayout();
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
    }

    public void setTable(CTable tbl) {
        _tblData = tbl;
    }

    public void setFinalList(java.util.List finalList) {
        this.finalList = finalList;
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

    public void insertTableData(HashMap whereMap) {
        try {
            ArrayList rowList = new ArrayList();
            ArrayList tableList = new ArrayList();
            HashMap dataMap = new HashMap();
            dataMap.putAll(whereMap);
            ArrayList list = new ArrayList();
            HashMap proxyResultMap = proxy.executeQuery(whereMap, map);
            if (proxyResultMap != null && proxyResultMap.size() > 0) {
                System.out.println("!@!@ INTEREST_DATA : " + proxyResultMap.get("INTEREST_DATA"));
                System.out.println("!@!@ RD_DATA : " + proxyResultMap.get("RD_DATA"));
                tableList = (ArrayList) proxyResultMap.get("INTEREST_DATA");
                list = (ArrayList) proxyResultMap.get("RD_DATA");
                if (dataMap.containsKey("EXCLUDE_LIEN")) {
                    for (int k = 0; k < tableList.size(); k++) {
                        ArrayList interestList = (ArrayList) tableList.get(k);
                        String depNo = CommonUtil.convertObjToStr(interestList.get(2));
                        for (int m = 0; m < list.size(); m++) {
                            HashMap lienMap = (HashMap) list.get(m);
                            if (lienMap.get("CHANGE_COLOR").toString().equals("TRUE")) {
                                if (depNo.equals(lienMap.get("ACT_NUM").toString())) {
                                    tableList.remove(k);
                                    list.remove(m);
                                    k = k - 1;
                                    m = m - 1;
                                }
                            }
                        }
                    }
                    setAccountsList(list);
                    setFinalList(tableList);
                } else {
                    setAccountsList(list);
                    setFinalList(tableList);
                }
            }
            System.out.println("#$# tableList:" + tableList);
            tblProduct = new EnhancedTableModel((ArrayList) tableList, tableTitle);
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
    }

    public void setResultStatus() {
        ttNotifyObservers();
    }

    public void ttNotifyObservers() {
        notifyObservers();
    }

    void setTblCategory(EnhancedTableModel tblCategory) {
        this.tblCategory = tblCategory;
        setChanged();
    }

    EnhancedTableModel getTblCategory() {
        return this.tblCategory;
    }

    public void updateInterestData() {
        tblProduct = new EnhancedTableModel((ArrayList) finalList, tableTitle);
    }

    public void setAccountsList(ArrayList rdList) {
        this.rdList = rdList;
    }

    public ArrayList getAccountsList() {
        return rdList;
    }

    /**
     * To perform the necessary operation
     */
    public void doAction(List finalTableList) {
        TTException exception = null;
        log.info("In doAction()");
        try {
            doActionPerform((HashMap) finalTableList);
        } catch (Exception e) {
            System.out.println("##$$$##$#$#$#$# Exception e : " + e);
            log.info("Error In doAction()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
            if (e instanceof TTException) {
                exception = (TTException) e;
            }
        }
        if (exception != null) {
            parseException.logException(exception, true);
            setResult(actionType);
        }
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
            String strArrData = null;
            int j = data.size();
            for (int i = 0; i < j; i++) {
                arrOriRow = (ArrayList) data.get(i);
                if (arrOriRow.get(selCol) != null) {
                    strArrData = arrOriRow.get(selCol).toString();
                }
                if (strArrData != null && strArrData.length() > 0) {
                    strArrData = strArrData.trim();
                    if (!chkCase) {
                        strArrData = strArrData.toUpperCase();
                    }
                    if ((selColCri == 2 && strArrData.equals(searchTxt))
                            || (selColCri == 0 && strArrData.startsWith(searchTxt))
                            || (selColCri == 1 && strArrData.endsWith(searchTxt))) {
                        arrFilterRow.add(arrOriRow);
                    } else if (selColCri == 3) {
                        if (Pattern.matches(searchTxt + "\\w*", strArrData)) {
                            arrFilterRow.add(arrOriRow);
                        }
                    }
                }
                strArrData = null;
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
     * Getter for property result.
     *
     * @return Value of property result.
     */
    public int getResult() {
        return result;
    }

    /**
     * Setter for property result.
     *
     * @param result New value of property result.
     */
    public void setResult(int result) {
        this.result = result;
    }

    /**
     * Getter for property proxyReturnMap.
     *
     * @return Value of property proxyReturnMap.
     */
    public java.util.HashMap getProxyReturnMap() {
        return proxyReturnMap;
    }

    /**
     * Setter for property proxyReturnMap.
     *
     * @param proxyReturnMap New value of property proxyReturnMap.
     */
    public void setProxyReturnMap(java.util.HashMap proxyReturnMap) {
        this.proxyReturnMap = proxyReturnMap;
    }
}
