/*
 * Copyright 2004 SeE Consulting (P) Ltd. All rights reserved.
 *
 * This software is the proprietary information of SeE Consulting (P) Ltd..  
 * Use is subject to license terms.
 *
 * AuthorizeOB.java
 *
 * Created on March 3, 2004, 1:46 PM
 */
package com.see.truetransact.ui.share.shareconversion;

//import java.util.Observable;
import com.see.truetransact.ui.termloan.arbitration.*;
import com.see.truetransact.ui.termloan.notices.*;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
//import java.util.Enumeration;
import java.util.regex.Pattern;
import java.util.Date;
//import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
//import javax.swing.table.TableColumnModel;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
//import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientutil.ComboBoxModel;
//import com.see.truetransact.transferobject.termloan.chargesTo.TermLoanChargesTO;

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.TrueTransactMain;

/**
 * Authorize Observable is the supporting class for Authorize UI
 *
 * @author bala
 */
public class ShareConversionOB extends CObservable {

    private boolean _isAvailable = true;
    private TableModel _tableModel;
    private CTable _tblData;
    private ArrayList _heading;
    private ArrayList sizeList = null;
    private TableModel searchTableModel;
    private ArrayList dataArrayList;
    private String relationalOperator = "";
    private ArrayList tempArrayList;
    private boolean isMultiSelect = false;
    private ComboBoxModel cbmProdId;
    private Map guarantorMap = null;
    private ArrayList guarantorList = null;   
    private ProxyFactory proxy;
    private HashMap map;
    private final static ClientParseException parseException = ClientParseException.getInstance();    
    private String result;    
    private String txtArbRate = "";
    private ArrayList key,value;
    private HashMap lookupMap;
     private ComboBoxModel cbmShareClass;
    private ComboBoxModel cbmShareConversionClass;
     private String cboShareClass = "";
    private String cboShareconversionClass = "";

    public String getTxtArbRate() {
        return txtArbRate;
    }

    public void setTxtArbRate(String txtArbRate) {
        this.txtArbRate = txtArbRate;
    }

    /**
     * Creates a new instance of AuthorizeOB
     */
    public ShareConversionOB() {
        try {
            proxy = ProxyFactory.createProxy();

            map = new HashMap();
            map.put(CommonConstants.JNDI, "ShareConversionJNDI");
            map.put(CommonConstants.HOME, "ShareConversionHome");
            map.put(CommonConstants.REMOTE, "ShareConversion");
            fillDropDown();

        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void fillDropDown() throws Exception {       
        HashMap param = new HashMap();
        param.put(CommonConstants.MAP_NAME, null);
        final ArrayList lookupKey = new ArrayList();
        lookupKey.add("SHARE_TYPE");
        param.put(CommonConstants.PARAMFORQUERY, lookupKey);
        HashMap lookupValues = ClientUtil.populateLookupData(param);
        key =  new ArrayList();
        value = new ArrayList();
        fillData((HashMap)lookupValues.get("SHARE_TYPE"));
        this.cbmShareClass = new ComboBoxModel(key,value);
        fillData((HashMap)lookupValues.get("SHARE_TYPE"));
        this.cbmShareConversionClass = new ComboBoxModel(key,value);
        param = null;
        lookupValues = null;
        key =  new ArrayList();
        value = new ArrayList();
        key = null;
        value = null;
    }
    
     private void fillData(HashMap keyValue)  throws Exception{
            key = (ArrayList)keyValue.get(CommonConstants.KEY);
            value = (ArrayList)keyValue.get(CommonConstants.VALUE);
     }

    public void printSMS(HashMap smsMap) {
        try {
            smsMap = proxy.execute(smsMap, map);
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
            setResult(ClientConstants.RESULT_STATUS[4]);
        }
    }

    /**
     * updateStatus method used to update the database field based on the UI
     * button pressed
     *
     * @param map HashMap from UI which is passed as a argument to Authorize UI
     * constructor
     * @param status Passed by UI. (Authorize, Reject, Exception - statuses)
     */
    public HashMap insertConvertedShares(HashMap whereMap) {
        HashMap obj = new HashMap();       
        obj.put("SHARE_CONVERSION", "SHARE_CONVERSION");
        if (whereMap.get("CONVERTED_SHARE_LIST") != null) {
            obj.put("CONVERTED_SHARE_LIST", whereMap.get("CONVERTED_SHARE_LIST"));
            obj.put("USER_ID", TrueTransactMain.USER_ID);
        }
        obj.put("SHARE_CONVERSION_CLASS", getCboShareconversionClass());      
        try {
            HashMap where = proxy.execute(obj, map);
            setResult(ClientConstants.RESULT_STATUS[2]);
            return where;
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
            setResult(ClientConstants.RESULT_STATUS[4]);
        }
        return null;
    }

   

    public void setSelectAll(CTable table, Boolean selected) {
        for (int i = 0, j = table.getRowCount(); i < j; i++) {
            table.setValueAt(selected, i, 0);
        }
    }

    /**
     * Retrives data and populates the CTable using TableModel
     *
     * @param mapID HashMap used to retrive data from DB
     * @param tblData CTable object used to update the table with TableModel
     * @return Returns ArrayList for populating Search Combobox
     */
    public ArrayList populateData(HashMap mapID, CTable tblData) {
        HashMap whereMap = null;
        //Added By Suresh
        _heading = null;
        _tblData = tblData;
        String editFlag = "N";
        HashMap tempMap = new HashMap();

        if (mapID.containsKey(CommonConstants.MAP_WHERE)) {
            whereMap = (HashMap) mapID.get(CommonConstants.MAP_WHERE);
        } else {
            whereMap = new HashMap();
        }

        whereMap.put(CommonConstants.HIERARCHY_ID, ProxyParameters.HIERARCHY_ID);
        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
            whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        }
        if (!whereMap.containsKey(CommonConstants.USER_ID)) {
            whereMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        }
        if (!whereMap.containsKey(CommonConstants.AUTHORIZESTATUS) && mapID.containsKey(CommonConstants.AUTHORIZESTATUS)) {
            whereMap.put(CommonConstants.AUTHORIZESTATUS, mapID.get(CommonConstants.AUTHORIZESTATUS));
        }
        if(whereMap!=null && whereMap.containsKey("EDIT")){
            editFlag = CommonUtil.convertObjToStr(whereMap.get("EDIT"));
        }
        javax.swing.table.DefaultTableModel tblModel = null;
        if (tblData.getModel() instanceof TableSorter) {
            _tableModel = ((TableSorter) tblData.getModel()).getModel();
        } else if (tblData.getModel() instanceof TableModel) {
            _tableModel = (TableModel) tblData.getModel();
        } else {
            tblModel = (javax.swing.table.DefaultTableModel) tblData.getModel();
        }
        while (_tableModel != null && tblData.getRowCount() > 0) {
            _tableModel.removeRow(0);
        }
        while (tblModel != null && tblData.getRowCount() > 0) {
            tblModel.removeRow(0);
        }
        guarantorMap = null;

        tempMap.putAll(whereMap);

        List list = ClientUtil.executeQuery((String) mapID.get(CommonConstants.MAP_NAME),
                whereMap);

        isMultiSelect = mapID.containsKey("MULTISELECT") ? true : false;
        isMultiSelect = true;
        _isAvailable = list.size() > 0 ? true : false;

        ArrayList data = new ArrayList();
        ArrayList colData = new ArrayList();

        HashMap map;
        Iterator iterator = null;

        if (_isAvailable) {
            map = (HashMap) list.get(0);
            iterator = map.keySet().iterator();
        } else {
            if (_heading != null) {
                setTblModel(tblData, null, _heading);
            }
            return null;
        }

        if (_heading == null) {
            _heading = new ArrayList();
            if (isMultiSelect) {
                _heading.add("Select");
            }
            sizeList = new ArrayList();
            if (isMultiSelect) {
                sizeList.add(new Integer(6));
            }
            String head = "";
            _heading.add("SHARE_ACCT_NO");
            _heading.add("CUST_ID");
            _heading.add("NAME");
            _heading.add("DOB");
            _heading.add("AGE");
            _heading.add("RETIRE_DT");
            _heading.add("CONVERTED_SHARE");           
        }
        String cellData = "", keyData = "";
        Object obj = null;
        int cellLen = 0;
        for (int i = 0; i < list.size(); i++) {
            ArrayList newList = new ArrayList();
            double amt = 0;
            double fee = 0;
            map = (HashMap) list.get(i);
            newList.add(new Boolean(false));
            newList.add(CommonUtil.convertObjToStr(map.get("SHARE_ACCT_NO")));
            newList.add(CommonUtil.convertObjToStr(map.get("CUST_ID")));
            newList.add(CommonUtil.convertObjToStr(map.get("NAME")));
            newList.add(CommonUtil.convertObjToStr(map.get("DOB")));
            newList.add(CommonUtil.convertObjToStr(map.get("AGE")));
            newList.add(CommonUtil.convertObjToStr(map.get("RETIRE_DT")));
            newList.add(CommonUtil.convertObjToStr(map.get("CONVERTED_SHARE")));           
            data.add(newList);
        }
        setTblModel(tblData, data, _heading);
        TableColumn col = null;
        tblData.revalidate();

        if (tblData.getModel() instanceof TableSorter) {
            _tableModel = ((TableSorter) tblData.getModel()).getModel();
        } else {
            _tableModel = (TableModel) tblData.getModel();
        }
        tempMap.clear();
        tempMap = null;

        return _heading;
    }


    public void removeRowsFromGuarantorTable(CTable tblData) {
        javax.swing.table.DefaultTableModel tblModel = null;
        if (tblData.getModel() instanceof TableSorter) {
            _tableModel = ((TableSorter) tblData.getModel()).getModel();
        } else if (tblData.getModel() instanceof TableModel) {
            _tableModel = (TableModel) tblData.getModel();
        } else {
            tblModel = (javax.swing.table.DefaultTableModel) tblData.getModel();
        }

        while (_tableModel != null && tblData.getRowCount() > 0) {
            _tableModel.removeRow(0);
        }
        while (tblModel != null && tblData.getRowCount() > 0) {
            tblModel.removeRow(0);
        }
        guarantorMap = null;
    }

  

    private void setTblModel(CTable tbl, ArrayList tblData, ArrayList head) {
        TableSorter tableSorter = new TableSorter();
        //tableSorter.addMouseListenerToHeaderInTable(tbl);
        TableModel tableModel = new TableModel(tblData, head) {
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                if (mColIndex == 0 || mColIndex == 9 || mColIndex == 10 || mColIndex == 11 || mColIndex == 12) {
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

    /**
     * Table Object Setter method
     *
     * @param tbl CTable Object
     */
    public void setTable(CTable tbl) {
        _tblData = tbl;
    }

    /**
     * Is Data Available or not checking Method
     *
     * @return Returns boolean
     */
    public boolean isAvailable() {
        return _isAvailable;
    }

    /**
     * fillData populates the UI based on the table row selected
     *
     * @param rowIndexSelected Selected Table Row index
     * @return Returns HashMap with Table Column & Row values for the selected
     * row.
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
        hashdata.put(CommonConstants.AUTHORIZEDT, ClientUtil.getCurrentDate());

        return hashdata;
    }

    /**
     * Getter method for TableModel
     *
     * @return Returns TableModel
     */
    public TableModel getTableModel() {
        return _tableModel;
    }

    /**
     * Search used to update the table model based on the search criteria given
     * by the user.
     *
     * @param searchTxt Search Text which is entered by the user
     * @param selCol Colunm selected from the combobox
     * @param selColCri Condition selected from the condition combobox
     * @param chkCase Match case checking
     */
   

    public void setDataArrayList() {
        dataArrayList = searchTableModel.getDataArrayList();
    }

    
    /**
     * Getter for property searchTableModel.
     *
     * @return Value of property searchTableModel.
     */
    public ArrayList getTableModel(CTable table) {
        TableModel tblModel = null;
        if (table.getModel() instanceof TableSorter) {
            tblModel = ((TableSorter) table.getModel()).getModel();
        } else if (table.getModel() instanceof TableModel) {
            tblModel = (TableModel) table.getModel();
        }
        System.out.println("@@## Data ArrayList : " + tblModel.getDataArrayList());
        return tblModel.getDataArrayList();
    }

    /**
     * Getter for property searchTableModel.
     *
     * @return Value of property searchTableModel.
     */
    public com.see.truetransact.clientutil.TableModel getSearchTableModel() {
        return searchTableModel;
    }

    /**
     * Setter for property searchTableModel.
     *
     * @param searchTableModel New value of property searchTableModel.
     */
    public void setSearchTableModel(com.see.truetransact.clientutil.TableModel searchTableModel) {
        this.searchTableModel = searchTableModel;
        setDataArrayList();
    }

    /**
     * Getter for property cbmProdId.
     *
     * @return Value of property cbmProdId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdId() {
        return cbmProdId;
    }

    /**
     * Setter for property cbmProdId.
     *
     * @param cbmProdId New value of property cbmProdId.
     */
    public void setCbmProdId(com.see.truetransact.clientutil.ComboBoxModel cbmProdId) {
        this.cbmProdId = cbmProdId;
    }

    /**
     * Getter for property guarantorMap.
     *
     * @return Value of property guarantorMap.
     */
    public java.util.Map getGuarantorMap() {
        return guarantorMap;
    }

    /**
     * Setter for property guarantorMap.
     *
     * @param guarantorMap New value of property guarantorMap.
     */
    public void setGuarantorMap(java.util.Map guarantorMap) {
        this.guarantorMap = guarantorMap;
    }

   
    /**
     * Getter for property result.
     *
     * @return Value of property result.
     */
    public java.lang.String getResult() {
        return result;
    }

    /**
     * Setter for property result.
     *
     * @param result New value of property result.
     */
    public void setResult(java.lang.String result) {
        this.result = result;
    }

    public ComboBoxModel getCbmShareClass() {
        return cbmShareClass;
    }

    public void setCbmShareClass(ComboBoxModel cbmShareClass) {
        this.cbmShareClass = cbmShareClass;
    }

    public ComboBoxModel getCbmShareConversionClass() {
        return cbmShareConversionClass;
    }

    public void setCbmShareConversionClass(ComboBoxModel cbmShareConversionClass) {
        this.cbmShareConversionClass = cbmShareConversionClass;
    }

    public String getCboShareClass() {
        return cboShareClass;
    }

    public void setCboShareClass(String cboShareClass) {
        this.cboShareClass = cboShareClass;
    }

    public String getCboShareconversionClass() {
        return cboShareconversionClass;
    }

    public void setCboShareconversionClass(String cboShareconversionClass) {
        this.cboShareconversionClass = cboShareconversionClass;
    }
    
}