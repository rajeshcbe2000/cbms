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

package com.see.truetransact.ui.termloan.notices;

//import java.util.Observable;
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

/** Authorize Observable is the supporting class for Authorize UI
 *
 * @author bala
 */
public class LoanNoticeOB extends CObservable {
    private boolean _isAvailable = true;
    private TableModel _tableModel;
    private CTable _tblData;
    private ArrayList _heading;
    private ArrayList sizeList =null;
    private TableModel searchTableModel;
    private ArrayList dataArrayList;
    private String relationalOperator="";
    private ArrayList tempArrayList;
    private boolean isMultiSelect = false;
    private ComboBoxModel cbmProdId;
//    private ComboBoxModel cbmNoticeType;
    private Map guarantorMap = null;
    private ArrayList guarantorList = null;
    private String txtNoticeCharge = "";
    private String txtPostageCharge = "";
    private ProxyFactory proxy;
    private HashMap map;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String prodType="";
    private String result;
    private Date tdtAuctionDate=null;
    private String strSurityOnly="";
    private String batchId="";
    private int operation;

    public String getStrSurityOnly() {
        return strSurityOnly;
    }

    public void setStrSurityOnly(String strSurityOnly) {
        this.strSurityOnly = strSurityOnly;
    }
    
    int getOperation() {
        return operation;
    }
    void setOperation(int operation) {
        this.operation = operation;
    }
    /** Creates a new instance of AuthorizeOB */
    public LoanNoticeOB() {
        try {
            proxy = ProxyFactory.createProxy();
            
            map = new HashMap();
            map.put(CommonConstants.JNDI, "TermLoanChargesJNDI");
            map.put(CommonConstants.HOME, "termloan.charges.TermLoanChargesHome");
            map.put(CommonConstants.REMOTE, "termloan.charges.TermLoanCharges");
            
            
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    public void fillDropDown(String productType) {
        ArrayList key = new ArrayList();
        ArrayList value = new ArrayList();
        HashMap lookUpHash = new HashMap();
        lookUpHash.put("PROD_ID", productType);
        List keyValue=null;
        setProdType(productType);
        if(productType.equals("MDS")){
            keyValue = ClientUtil.executeQuery("getProductsForMDSNotice", lookUpHash);
        }else{
            keyValue = ClientUtil.executeQuery("getProductsForLoanNotice", lookUpHash);
        }
        key.add("");
        value.add("");
        LinkedHashMap prodMap = new LinkedHashMap();
        if (keyValue!=null && keyValue.size()>0) {
            for (int i=0; i<keyValue.size(); i++) {
                prodMap = (LinkedHashMap)keyValue.get(i);
                key.add(prodMap.get("PROD_ID"));
                value.add(prodMap.get("PROD_DESC"));
            }
        }
        cbmProdId = new ComboBoxModel(key,value);
        key = null;
        value = null;
        lookUpHash.clear();
        lookUpHash = null;
        keyValue.clear();
        keyValue = null;
        prodMap.clear();
        prodMap = null;
    }
    
    public void printSMS(HashMap smsMap) {
        try {
            smsMap = proxy.execute(smsMap, map) ;
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e,true);
            setResult(ClientConstants.RESULT_STATUS[4]);
        }
    }
    
    /** updateStatus method used to update the database field based on the UI button
     * pressed
     *
     * @param map       HashMap from UI which is passed as a argument to Authorize UI constructor
     * @param status    Passed by UI. (Authorize, Reject, Exception - statuses)
      */   
    public void insertCharges(HashMap whereMap, boolean onlyChargeDetails,String noticeType,HashMap tempMap) {
        HashMap obj = new HashMap();
        obj.put("NOTICE_CHARGES", whereMap);
        obj.put("AUCTIONDT",tempMap.get("AUCTION_DT"));
        obj.put("TYPE_OF_NOTICE",noticeType);
        obj.put("ONLY_CHARGE_DETAILS", new Boolean(onlyChargeDetails));
        obj.put(CommonConstants.MODULE, getModule());	
        obj.put(CommonConstants.SCREEN, getScreen());
        if(prodType.equals("MDS")){
            obj.put("PROD_TYPE",prodType);
        }
        if(getStrSurityOnly()!=null && getStrSurityOnly().equals("Y")){
             obj.put("SURITY_ONLY","Y");
        }
        try {
             HashMap proxyReturnMap = proxy.execute(obj, map) ;
             setProxyReturnMap(proxyReturnMap);
             setResult(ClientConstants.RESULT_STATUS[2]);
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e,true);
            setResult(ClientConstants.RESULT_STATUS[4]);
        }
    }
    
    public void insertAuthCharge(HashMap whereMap, HashMap AuthMap){
        HashMap obj = new HashMap();
        obj.put("NOTICE_CHARGES", whereMap);
        obj.put("AUTHORIZEMAP", AuthMap);
        obj.put("LOAN_NOTICE","LOAN_NOTICE");
        try {
             HashMap proxyReturnMap = proxy.execute(obj, map) ;
             setProxyReturnMap(proxyReturnMap);
             setResult(ClientConstants.RESULT_STATUS[2]);
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e,true);
            setResult(ClientConstants.RESULT_STATUS[4]);
        }
    }


    public String getSelected() {
        Boolean bln;
//        ArrayList arrRow;
//        HashMap selectedMap;
//        ArrayList selectedList = new ArrayList();
        String selected="";
        for (int i=0, j=_tableModel.getRowCount(); i < j; i++) {
            bln = (Boolean) _tableModel.getValueAt(i, 0);
            if (bln.booleanValue() == true) {
                if(prodType.equals("MDS")){
                    selected+="'"+_tableModel.getValueAt(i, 2);
                }else{
                    selected+="'"+_tableModel.getValueAt(i, 1);
                }
                selected+="',";
//                selectedList.add(_tableModel.getValueAt(i, 1));
            }
        }
        selected = selected.length()>0 ? selected.substring(0,selected.length()-1) : ""; 
        System.out.println("#$#$ selected : "+selected);
        return selected;
    }
    
    public void setSelectAll(CTable table, Boolean selected) {
        for (int i=0, j=table.getRowCount(); i < j; i++) {
            table.setValueAt(selected, i, 0);
        }
    }
    
    
    /** Retrives data and populates the CTable using TableModel
     *
     * @param mapID     HashMap used to retrive data from DB
     * @param tblData   CTable object used to update the table with TableModel
     * @return          Returns ArrayList for populating Search Combobox
     */    
    public ArrayList populateData(HashMap mapID, CTable tblData) {
        HashMap whereMap = null;
        //Added By Suresh
        _heading = null;    
        _tblData = tblData;
        HashMap tempMap = new HashMap();
        
        if (mapID.containsKey(CommonConstants.MAP_WHERE))
            whereMap = (HashMap) mapID.get(CommonConstants.MAP_WHERE);
        else 
            whereMap = new HashMap();

        whereMap.put (CommonConstants.HIERARCHY_ID, ProxyParameters.HIERARCHY_ID);
        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
            whereMap.put (CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        }
        if (!whereMap.containsKey(CommonConstants.USER_ID)) {
            whereMap.put (CommonConstants.USER_ID, ProxyParameters.USER_ID);
        }
        if (!whereMap.containsKey(CommonConstants.AUTHORIZESTATUS) && mapID.containsKey(CommonConstants.AUTHORIZESTATUS)) {
            whereMap.put (CommonConstants.AUTHORIZESTATUS, mapID.get(CommonConstants.AUTHORIZESTATUS));
        }

        System.out.println ("Screen   : LoanNoticeOB");
        System.out.println ("Map Name : " + mapID.get(CommonConstants.MAP_NAME));
        System.out.println ("Map      : " + mapID + ":" + whereMap);
        
        javax.swing.table.DefaultTableModel tblModel = null;
        if (tblData.getModel() instanceof TableSorter) {
            _tableModel = ((TableSorter) tblData.getModel()).getModel();
        } else if (tblData.getModel() instanceof TableModel) {
            _tableModel = (TableModel) tblData.getModel();
        } else {
            tblModel = (javax.swing.table.DefaultTableModel) tblData.getModel();
        }

        while (_tableModel!=null && tblData.getRowCount() > 0) {
            _tableModel.removeRow(0);
        }
         while (tblModel!=null && tblData.getRowCount() > 0) {
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
                setTblModel (tblData, null, _heading);
            }            
            return null;
        }
        
        if (_heading == null) {
            _heading = new ArrayList();
            if (isMultiSelect)
                _heading.add ("Select");
            sizeList = new ArrayList();
            if (isMultiSelect)
                sizeList.add (new Integer(6));
            String head = "";
            while (iterator.hasNext()) {
                head = (String) iterator.next();
                sizeList.add (new Integer(head.length()));
                _heading.add(head);
            }
        }
//        System.out.println("@@@$$$ sizeList : "+sizeList);
        
        seperateList(list);

        String cellData="", keyData="";
        Object obj = null;
        int cellLen = 0;
        for (int i=0, j=list.size(), k=1; i < j; i++, k=1) {
            map = (HashMap) list.get(i);
            colData = new ArrayList();
            iterator = map.values().iterator();
            if (isMultiSelect)
                colData.add(new Boolean(false));
            
            while (iterator.hasNext()) {
                obj = iterator.next();
                cellData = CommonUtil.convertObjToStr(obj);
                cellLen = cellData.length();
                if (cellLen > ((Integer) sizeList.get(k)).intValue()) {
                    sizeList.remove(k);
                    sizeList.add(k, new Integer(cellLen));
                }
                colData.add(cellData);
                k++;
            }
            data.add(colData);
        }
         

//        System.out.println("@@@$$$ sizeList : "+sizeList);
        
        setTblModel(tblData, data, _heading);
        
        TableColumn col = null;
        for (int i=0, j=sizeList.size(), k=0; i < j; i++) {
            k = ((Integer)sizeList.get(i)).intValue();
            if (k > 50) {
                k = 400;
            } else {
                k *= 8;
            }
            col = tblData.getColumn(_heading.get(i));
            col.setPreferredWidth(k);
            col.setMinWidth(k);
//            System.out.println("@@@$$$ col width : "+k);
            //col.setMaxWidth(k);
        }
        //Added By Suresh
        if(prodType.equals("MDS")){
            _tblData.getColumnModel().getColumn(3).setPreferredWidth(65);
            _tblData.getColumnModel().getColumn(6).setPreferredWidth(65);
        }
        tblData.revalidate();
        
        if (tblData.getModel() instanceof TableSorter) {
            _tableModel = ((TableSorter) tblData.getModel()).getModel();
        } else {
            _tableModel = (TableModel) tblData.getModel();
        }
        
//        System.out.println("@@@$$$ tempMap : "+tempMap);
        populateCharges(tempMap);
        tempMap.clear();
        tempMap = null;
        
        return _heading;
    }
    
    private void populateCharges(HashMap whereMap) {
        if (whereMap.containsKey("PROD_ID")) {
            List lst=null;
            if(prodType.equals("MDS")){
                lst = ClientUtil.executeQuery("getChargesForMDSNotices", whereMap);
            }else{
                lst = ClientUtil.executeQuery("getChargesForLoanNotices", whereMap);
            }
//            HashMap resultMap = null;
            HashMap resultMap = new HashMap();
            if (lst!=null && lst.size()>0) {
                resultMap = (HashMap) lst.get(0);
                txtNoticeCharge = CommonUtil.convertObjToStr(resultMap.get("NOTICE_CHARGE_AMT"));
                txtPostageCharge = CommonUtil.convertObjToStr(resultMap.get("POSTAGE_AMT"));
            }
            resultMap.clear();
            resultMap = null;
            lst.clear();
            lst = null;
        }
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
        
        while (_tableModel!=null && tblData.getRowCount() > 0) {
            _tableModel.removeRow(0);
        }
         while (tblModel!=null && tblData.getRowCount() > 0) {
            tblModel.removeRow(0);
        }
        guarantorMap = null;
    }
    
    public void populateGuarantorTable(String actNum, CTable tblData) {
        if (_heading!=null) {
            if (guarantorMap!=null && guarantorMap.containsKey(actNum)) {
                guarantorList = (ArrayList) guarantorMap.get(actNum);
                setTblModel(tblData, guarantorList, _heading);
                TableColumn col = null;
                for (int i=0, j=sizeList.size(), k=0; i < j; i++) {
                    k = ((Integer)sizeList.get(i)).intValue();
                    if (k > 50) {
                        k = 400;
                    } else {
                        k *= 8;
                    }
                    col = tblData.getColumn(_heading.get(i));
                    col.setPreferredWidth(k);
                    col.setMinWidth(k);
                }
                //Added By Suresh
                if(prodType.equals("MDS")){
                    _tblData.getColumnModel().getColumn(3).setPreferredWidth(65);
                    _tblData.getColumnModel().getColumn(6).setPreferredWidth(65);
                }
                tblData.revalidate();            
            } else {
                setTblModel(tblData, null, _heading);
            }
        }
    }
    
    private void seperateList(List list) {
        ArrayList tempList = new ArrayList();
//        System.out.println("@@@$$$ list : "+list);
        Map map = new HashMap();
        Iterator iterator = null;
        String cellData;
        for (int i=0; i<list.size(); i++) {
            map = (HashMap) list.get(i);
//            System.out.println("@@@$$$ map : "+map);
            if (String.valueOf(map.get("CUST_TYPE")).equals("GUARANTOR")) {
                iterator = map.values().iterator();
//                if (guarantorList==null) {
                guarantorList = new ArrayList();
//                }
                guarantorList.add(new Boolean(false));
                if (guarantorMap==null) {
                    guarantorMap = new HashMap();
                }
//                System.out.println("@@@$$$ guarantorMap : "+guarantorMap);
                while (iterator.hasNext()) {
                    cellData = CommonUtil.convertObjToStr(iterator.next());
                    guarantorList.add(cellData);
                }
//                System.out.println("@@@$$$ guarantorList : "+guarantorList);
                //Changed By Suresh
                String actNum="";
                if(prodType.equals("MDS")){
                    actNum = String.valueOf(map.get("CHITTAL_NO"));
                }else{
                    actNum = String.valueOf(map.get("ACT_NUM"));
                }
                if (guarantorMap.containsKey(actNum)) {
                    tempList = (ArrayList) guarantorMap.get(actNum);
                } else {
                    tempList = new ArrayList();
                }
                tempList.add(guarantorList);
                guarantorMap.put(actNum, tempList);
                list.remove(i--);
//                if (i<8) {
//                    System.out.println("@@@$$$ guarantorMap : "+guarantorMap);
//                }
            }
        }
//        System.out.println("@@@$$$ final list : "+list);
//        System.out.println("@@@$$$ final guarantorMap : "+guarantorMap);
    }
    
        
    private void setTblModel(CTable tbl, ArrayList tblData, ArrayList head) {
       TableSorter tableSorter = new TableSorter();
       // tableSorter.addMouseListenerToHeaderInTable(tbl);
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
                                Row values for the selected row.
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
        
        hashdata.put (CommonConstants.HIERARCHY_ID, ProxyParameters.HIERARCHY_ID);
        // Adding Authorization Date
        hashdata.put(CommonConstants.AUTHORIZEDT, ClientUtil.getCurrentDate());
        
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
    
    public void setDataArrayList() {
        dataArrayList = searchTableModel.getDataArrayList();
    }
    
    public void searchData(String searchTxt, int selCol, int selColCri, boolean chkCase, String operator) {
        if (searchTxt.length() > 0) {
            ArrayList arrFilterRow = new ArrayList();
            ArrayList arrOriRow;
            String strArrData;
            for (int i=0, j=dataArrayList.size(); i < j; i++) {
                arrOriRow = (ArrayList)dataArrayList.get(i);
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
            
            if (relationalOperator.equals("Or"))
                arrFilterRow.addAll(tempArrayList);
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
//            if (relationalOperator.equals("Or"))
            if (operator.equals("And"))
                dataArrayList = arrFilterRow;
            if (operator.equals("Or")) {
                if (tempArrayList==null) tempArrayList = new ArrayList();
                tempArrayList = arrFilterRow;
            }
            relationalOperator = operator;
        }        
    }
    
    /**
     * Getter for property searchTableModel.
     * @return Value of property searchTableModel.
     */
    public ArrayList getTableModel(CTable table) {
        TableModel tblModel = null;
        if (table.getModel() instanceof TableSorter) {
            tblModel = ((TableSorter) table.getModel()).getModel();
        } else if (table.getModel() instanceof TableModel) {
            tblModel = (TableModel) table.getModel();
        }
        System.out.println("@@## Data ArrayList : "+tblModel.getDataArrayList());
        return tblModel.getDataArrayList();
    }
    
    /**
     * Getter for property searchTableModel.
     * @return Value of property searchTableModel.
     */
    public com.see.truetransact.clientutil.TableModel getSearchTableModel() {
        return searchTableModel;
    }
    
    /**
     * Setter for property searchTableModel.
     * @param searchTableModel New value of property searchTableModel.
     */
    public void setSearchTableModel(com.see.truetransact.clientutil.TableModel searchTableModel) {
        this.searchTableModel = searchTableModel;
        setDataArrayList();
    }
    
    /**
     * Getter for property cbmProdId.
     * @return Value of property cbmProdId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdId() {
        return cbmProdId;
    }
    
    /**
     * Setter for property cbmProdId.
     * @param cbmProdId New value of property cbmProdId.
     */
    public void setCbmProdId(com.see.truetransact.clientutil.ComboBoxModel cbmProdId) {
        this.cbmProdId = cbmProdId;
    }
    
    /**
     * Getter for property guarantorMap.
     * @return Value of property guarantorMap.
     */
    public java.util.Map getGuarantorMap() {
        return guarantorMap;
    }
    
    /**
     * Setter for property guarantorMap.
     * @param guarantorMap New value of property guarantorMap.
     */
    public void setGuarantorMap(java.util.Map guarantorMap) {
        this.guarantorMap = guarantorMap;
    }
    
    /**
     * Getter for property txtNoticeCharge.
     * @return Value of property txtNoticeCharge.
     */
    public java.lang.String getTxtNoticeCharge() {
        return txtNoticeCharge;
    }
    
    /**
     * Setter for property txtNoticeCharge.
     * @param txtNoticeCharge New value of property txtNoticeCharge.
     */
    public void setTxtNoticeCharge(java.lang.String txtNoticeCharge) {
        this.txtNoticeCharge = txtNoticeCharge;
    }
    
    /**
     * Getter for property txtPostageCharge.
     * @return Value of property txtPostageCharge.
     */
    public java.lang.String getTxtPostageCharge() {
        return txtPostageCharge;
    }
    
    /**
     * Setter for property txtPostageCharge.
     * @param txtPostageCharge New value of property txtPostageCharge.
     */
    public void setTxtPostageCharge(java.lang.String txtPostageCharge) {
        this.txtPostageCharge = txtPostageCharge;
    }
    
    /**
     * Getter for property result.
     * @return Value of property result.
     */
    public java.lang.String getResult() {
        return result;
    }
    
    /**
     * Setter for property result.
     * @param result New value of property result.
     */
    public void setResult(java.lang.String result) {
        this.result = result;
    }
    
    /**
     * Getter for property prodType.
     * @return Value of property prodType.
     */
    public java.lang.String getProdType() {
        return prodType;
    }
    
    /**
     * Setter for property prodType.
     * @param prodType New value of property prodType.
     */
    public void setProdType(java.lang.String prodType) {
        this.prodType = prodType;
    }
    
    /**
     * Getter for property tdtAuctionDate.
     * @return Value of property tdtAuctionDate.
     */
    public Date getTdtAuctionDate() {
        return tdtAuctionDate;
    }
    
    /**
     * Setter for property tdtAuctionDate.
     * @param tdtAuctionDate New value of property tdtAuctionDate.
     */
    public void setTdtAuctionDate(Date tdtAuctionDate) {
        this.tdtAuctionDate = tdtAuctionDate;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }
    
    
    
}