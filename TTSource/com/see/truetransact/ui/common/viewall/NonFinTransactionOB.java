/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * NonFinTransactionOB.java
 *
 * Created on December 17, 2003, 11:41 AM
 */

package com.see.truetransact.ui.common.viewall;

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

/**
 * @author  bala
 */
public class NonFinTransactionOB extends CObservable {
    private boolean _isAvailable = true;
    private TableModel _tableModel;
    private CTable _tblData;
    private ArrayList _heading;
    private HashMap dataHash;
    private ArrayList data;
    private int dataSize;
    public final int MAXDATA = 1000;
    private ComboBoxModel cbmProdType;
    private String prodType;
    private String txtAccNo;
    private String lblAccNameValue;
    private Date tdtFromDate;
    private Date tdtToDate;
    private ArrayList key;
    private ArrayList value;
    private HashMap linkMap=new HashMap();
    private ProxyFactory proxy;
    public boolean btnRDTransPressed = false;
    private ComboBoxModel cbmBranCode;
    private static Date currDt = null;
    private static NonFinTransactionOB nonFinTransactionOB;
    static {
        try {
            currDt = ClientUtil.getCurrentDate();
            nonFinTransactionOB = new NonFinTransactionOB();
        } catch(Exception e) {
        }
    }
    
    public static NonFinTransactionOB getInstance() {
        return nonFinTransactionOB;
    }
    
    /** Creates a new instance of ViewAllOB */
    public NonFinTransactionOB() throws Exception {
        initiate();
    }
    
    private void initiate() throws Exception {
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }
        fillDropDown();
    }
    
    private void fillDropDown() throws Exception{
        HashMap lookupMap = new HashMap();
        lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
        lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
        lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
        
        final HashMap param = new HashMap();
        
        final ArrayList lookupKey = new ArrayList();
        lookupKey.add("ENQ_PRODUCTTYPE");
        
        param.put(CommonConstants.MAP_NAME, null);
        param.put(CommonConstants.PARAMFORQUERY, lookupKey);
        
        HashMap lookupValues = ClientUtil.populateLookupData(param);
        
        fillData((HashMap)lookupValues.get("ENQ_PRODUCTTYPE"));
        cbmProdType=new ComboBoxModel(key,value);
        
        param.put(CommonConstants.MAP_NAME,"getBranchCode");
        HashMap where= new HashMap();
        param.put(CommonConstants.PARAMFORQUERY,where);
        lookupValues = ClientUtil.populateLookupData(param);
        fillData((HashMap)lookupValues.get(CommonConstants.DATA));
        cbmBranCode= new ComboBoxModel(key,value);
        
        
    }
    
    public void setAccountName(){
        try {
            final HashMap accountNameMap = new HashMap();
            accountNameMap.put("ACC_NUM",getTxtAccNo());
            accountNameMap.put("CLOSECHECK",getTxtAccNo());
            final java.util.List resultList = ClientUtil.executeQuery("getAccountNumberName"+this.getProdType(),accountNameMap);
            if(resultList != null && resultList.size()>0){
                final HashMap resultMap = (HashMap)resultList.get(0);
                setLblAccNameValue(resultMap.get("CUSTOMER_NAME").toString());
            } else {
                setLblAccNameValue("");
            }
        }catch(Exception e){
            
        }
    }
    
    /** To get data for comboboxes */
    private HashMap populateDataLocal(HashMap obj, HashMap lookupMap)  throws Exception{
        HashMap keyValue = proxy.executeQuery(obj,lookupMap);
        return keyValue;
    }
    ///as an when customer comes
    public HashMap loanInterestCalculationAsAndWhen(HashMap whereMap){
        HashMap mapData=new HashMap();
        try{
            java.util.List mapDataList = ClientUtil.executeQuery("", whereMap); //, frame);
            mapData=(HashMap)mapDataList.get(0);
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("#### MapData :"+mapData);
        return mapData;
    }
    private void fillData(HashMap keyValue)  throws Exception{
        key = new ArrayList();
        value = new ArrayList();
        key.add("");
        value.add("");
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    public HashMap asAnWhenCustomerComesYesNO(String acct_no,String batch_id){
        HashMap map=new HashMap();
        if(batch_id==null)
            map.put("ACT_NUM",acct_no);
        else
            map.put("BATCH_ID",batch_id);
        
        map.put("TRANS_DT", currDt.clone());
        map.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
        java.util.List lst=ClientUtil.executeQuery("IntCalculationDetail", map);
        if(lst==null || lst.isEmpty())
            lst=ClientUtil.executeQuery("IntCalculationDetailAD", map);
        if(lst !=null && lst.size()>0){
            map=(HashMap)lst.get(0);
            setLinkMap(map);
        }
        return map;
    }
    public ArrayList populateData(HashMap mapID, CTable tblData) {
        _tblData = tblData;
        System.out.println("Screen   : " + getClass());
        System.out.println("Map Name : " + mapID.get(CommonConstants.MAP_NAME));
        System.out.println("Map      : " + mapID);
        
        dataHash = ClientUtil.executeTableQuery(mapID);
        _heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        data = (ArrayList) dataHash.get(CommonConstants.TABLEDATA);
        populateTable();
        return _heading;
    }
    
    private void setProperDataType() {
        int size = data.size();
        if (size>0) {
            ArrayList arrList;
            arrList = (ArrayList)data.get(0);
            for (int i=0; i<size; i++) {
                arrList = (ArrayList)data.get(i);
                if ((getProdType().equals("ATL")|| getProdType().equals("TL")|| getProdType().equals("AD") || getProdType().equals("AAD")) && arrList.size()>8) {
                    arrList.set(3, ClientUtil.convertObjToCurrency(arrList.get(3)));
                    arrList.set(4, ClientUtil.convertObjToCurrency(arrList.get(4)));
                    arrList.set(5, ClientUtil.convertObjToCurrency(arrList.get(5)));
                    arrList.set(6, ClientUtil.convertObjToCurrency(arrList.get(6)));
                    arrList.set(7, ClientUtil.convertObjToCurrency(arrList.get(7)));
                    arrList.set(8, ClientUtil.convertObjToCurrency(arrList.get(8)));
                    arrList.set(9, ClientUtil.convertObjToCurrency(arrList.get(9)));
                    arrList.set(10, ClientUtil.convertObjToCurrency(arrList.get(10)));
                    if(arrList.size()>11){
                        arrList.set(11, ClientUtil.convertObjToCurrency(arrList.get(11)));
                        arrList.set(12, ClientUtil.convertObjToCurrency(arrList.get(12)));
                    }
                } else {
                    if (arrList.size()<=6) {
                        arrList.set(2, ClientUtil.convertObjToCurrency(arrList.get(2)));
                        arrList.set(3, ClientUtil.convertObjToCurrency(arrList.get(3)));
                        arrList.set(4, ClientUtil.convertObjToCurrency(arrList.get(4)));
                        arrList.set(5, ClientUtil.convertObjToCurrency(arrList.get(5)));
                    } else {
                        String transDt = CommonUtil.convertObjToStr(arrList.get(4));
                        if (transDt.length()>0) {
                            Date transDtWithoutTimeStamp = DateUtil.getDateMMDDYYYY(transDt);
                            transDtWithoutTimeStamp = new Date(transDtWithoutTimeStamp.getYear(),
                            transDtWithoutTimeStamp.getMonth(),
                            transDtWithoutTimeStamp.getDate());
                            arrList.set(4, DateUtil.getStringDate(transDtWithoutTimeStamp));
                        }
                        arrList.set(5, ClientUtil.convertObjToCurrency(arrList.get(5)));
                        arrList.set(6, ClientUtil.convertObjToCurrency(arrList.get(6)));
                        arrList.set(7, ClientUtil.convertObjToCurrency(arrList.get(7)));
                    }
                }
                data.set(i, arrList);
            }
        }
    }
    
    private void calcBalances() {
        if (data.size()>0) {
            int size = data.size()+1;
            ArrayList firstRow = (ArrayList)data.get(0);
            int colSize = firstRow.size();
            double balAmt = 0;
            if (getProdType().equals("GL") && CommonUtil.convertObjToDouble(firstRow.get(7)).doubleValue()!=0)
                balAmt = CommonUtil.convertObjToDouble(firstRow.get(7)).doubleValue();
            else if (getProdType().equals("TD") && colSize<=6)
                if(btnRDTransPressed == false){
                    balAmt = CommonUtil.convertObjToDouble(firstRow.get(4)).doubleValue()-
                    CommonUtil.convertObjToDouble(firstRow.get(3)).doubleValue()+
                    CommonUtil.convertObjToDouble(firstRow.get(2)).doubleValue();
                }
            else if (getProdType().equals("TD") && colSize>6)
                balAmt = 0;   //Deposit dayend balance not teken.  after taking that, this condition should be removed.
            else if (btnRDTransPressed == false && CommonUtil.convertObjToDouble(firstRow.get(7)).doubleValue()!=0)
                balAmt = CommonUtil.convertObjToDouble(firstRow.get(7)).doubleValue()-
                CommonUtil.convertObjToDouble(firstRow.get(6)).doubleValue()+
                CommonUtil.convertObjToDouble(firstRow.get(5)).doubleValue();
            ArrayList arrList = new ArrayList();
            if (colSize<=8) {
                //                ArrayList arrList = new ArrayList();
                arrList.add("");
                arrList.add("By B/F");
                arrList.add("");
                arrList.add("");
                if (colSize>6) {
                    arrList.add("");
                    arrList.add("");
                    arrList.add("");
                }
                arrList.add(CommonUtil.convertObjToStr(new Double(balAmt)));
                if (getProdType().equals("TD") && colSize<=6) {
                    arrList.add(new Double(0));
                }
                data.add(0, arrList);
                if (getProdType().equals("GL") || (getProdType().equals("TD") && colSize>6) || getProdType().equals("TL") || getProdType().equals("ATL"))
                    for (int i=1; i<size; i++) {
                        arrList = (ArrayList)data.get(i);
                        double drAmt = CommonUtil.convertObjToDouble(arrList.get(5)).doubleValue();
                        double crAmt = CommonUtil.convertObjToDouble(arrList.get(6)).doubleValue();
                        balAmt = balAmt+crAmt-drAmt;
                        //                arrList.remove(7);
                        arrList.set(7,new Double(balAmt));
                        //                data.remove(i);
                        data.set(i, arrList);
                    }
            }
            arrList = null;
            //            }
        }
    }
    private void removeTimeStampsInRecurringData() {
        int size = data.size();
        ArrayList arrList = new ArrayList();
        for (int i=0; i<size; i++) {
            arrList = (ArrayList)data.get(i);
            if (arrList!=null && arrList.size()>0) {
                String dueDt = CommonUtil.convertObjToStr(arrList.get(1));
                String transDt = CommonUtil.convertObjToStr(arrList.get(2));
                int count = CommonUtil.convertObjToInt(arrList.get(3));
                if (!transDt.equals("") && !transDt.equals("By B/F") && transDt.length()>0) {
                    Date transDtWithoutTimeStamp = DateUtil.getDateMMDDYYYY(dueDt);
                    transDtWithoutTimeStamp = new Date(transDtWithoutTimeStamp.getYear(),
                    transDtWithoutTimeStamp.getMonth(),
                    transDtWithoutTimeStamp.getDate());
                    arrList.set(1, DateUtil.getStringDate(transDtWithoutTimeStamp));
                    data.set(i, arrList);

                    transDtWithoutTimeStamp = DateUtil.getDateMMDDYYYY(transDt);
                    transDtWithoutTimeStamp = new Date(transDtWithoutTimeStamp.getYear(),
                    transDtWithoutTimeStamp.getMonth(),
                    transDtWithoutTimeStamp.getDate());
                    arrList.set(2, DateUtil.getStringDate(transDtWithoutTimeStamp));
                    data.set(i, arrList);
                    
                    Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(arrList.get(1)));
                    Date tranDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(arrList.get(2)));
                    int transMonth = tranDt.getMonth()+1;
                    int dueMonth = dueDate.getMonth()+1;
                    int dueYear = dueDate.getYear()+1900;
                    int transYear = tranDt.getYear()+1900;
                    int delayeInstallment = transMonth - dueMonth;
                    if(dueYear == transYear)
                        delayeInstallment = transMonth - dueMonth;
                    else{
                        int diffYear = transYear - dueYear;
                        delayeInstallment = (diffYear * 12 - dueMonth) + transMonth;
                    }
                    if(delayeInstallment<0)
                        delayeInstallment = 0;
                    arrList.set(3, new Integer(delayeInstallment));
                    data.set(i, arrList);          
                }
            }
        }
        arrList = null;
    }    
    private void removeTimeStampsInData() {
        int size = data.size();
        ArrayList arrList = new ArrayList();
        for (int i=0; i<size; i++) {
            arrList = (ArrayList)data.get(i);
            if (arrList!=null && arrList.size()>0) {
                String transDt = CommonUtil.convertObjToStr(arrList.get(0));
                if (transDt.length()>0) {
                    Date transDtWithoutTimeStamp = DateUtil.getDateMMDDYYYY(transDt);
                    transDtWithoutTimeStamp = new Date(transDtWithoutTimeStamp.getYear(),
                    transDtWithoutTimeStamp.getMonth(),
                    transDtWithoutTimeStamp.getDate());
                    //                    System.out.println("#$#$#$ transDtWithoutTimeStamp : "+transDtWithoutTimeStamp);
                    //                    System.out.println("#$#$#$ DateUtil.getStringDate(transDtWithoutTimeStamp) : "+DateUtil.getStringDate(transDtWithoutTimeStamp));
                    arrList.set(0, DateUtil.getStringDate(transDtWithoutTimeStamp));
                    //                    arrList.set(0, transDtWithoutTimeStamp);
                    data.set(i, arrList);
                }
            }
        }
        arrList = null;
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
            _tblData.setAutoResizeMode(5);
            _tblData.doLayout();
            _tblData.setModel(tableModel);
            _tblData.revalidate();
            if (getProdType().equals("TD") && ((ArrayList)data.get(0)).size()<=6) {
                if(btnRDTransPressed == true)
                    setRightAlignment(2);
                else{
                    setRightAlignment(2);
                    setRightAlignment(3);
                    setRightAlignment(4);
                    setRightAlignment(5);
                }
            } else if (getProdType().equals("ATL")|| getProdType().equals("TL") && ((ArrayList)data.get(0)).size()>8) {
                setRightAlignment(3);
                setRightAlignment(4);
                setRightAlignment(5);
                setRightAlignment(6);
                setRightAlignment(7);
                setRightAlignment(8);
                setRightAlignment(9);
                setRightAlignment(10);
                if(((ArrayList)data.get(0)).size()>11){
                    setRightAlignment(11);
                    setRightAlignment(12);
                }
            } else {
                setRightAlignment(5);
                setRightAlignment(6);
                setRightAlignment(7);
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
    
    private void setRightAlignment(int col) {
        javax.swing.table.DefaultTableCellRenderer r = new javax.swing.table.DefaultTableCellRenderer();
        r.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        _tblData.getColumnModel().getColumn(col).setCellRenderer(r);
        _tblData.getColumnModel().getColumn(col).sizeWidthToFit();
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
        //        TableSorter tableSorter = new TableSorter();
        //        tableSorter.addMouseListenerToHeaderInTable(_tblData);
        //
        //        tableSorter.setModel(_tableModel);
        //        tableSorter.fireTableDataChanged();
        if (_tblData!=null && _tableModel!=null) {
            _tblData.setModel(_tableModel);
            _tblData.revalidate();
        }
        
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
                System.out.println("### Data Row : "+arrOriRow);
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
                System.out.println("### Data Row : "+arrOriRow);
                strArrData = arrOriRow.get(selCol)!=null ? arrOriRow.get(selCol).toString() : "";
                if (strArrData != null) {
                    strArrData = strArrData.trim();
                    if (strArrData.length() == 0) {
                        arrFilterRow.add(arrOriRow);
                    }
                }
            }
        }
        System.out.println("### Selected Rows : "+arrFilterRow);
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
    
    public void resetForm() {
        setProdType("");
        setTxtAccNo("");
        setTdtFromDate(null);
        setTdtToDate(null);
        _tableModel = new TableModel();
        refreshTable();
        ttNotifyObservers();
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
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
     * Getter for property cbmProdType.
     * @return Value of property cbmProdType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdType() {
        return cbmProdType;
    }
    
    /**
     * Setter for property cbmProdType.
     * @param cbmProdType New value of property cbmProdType.
     */
    public void setCbmProdType(com.see.truetransact.clientutil.ComboBoxModel cbmProdType) {
        this.cbmProdType = cbmProdType;
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
     * Getter for property txtAccountNo.
     * @return Value of property txtAccountNo.
     */
    public java.lang.String getTxtAccNo() {
        return txtAccNo;
    }
    
    /**
     * Setter for property txtAccountNo.
     * @param txtAccountNo New value of property txtAccountNo.
     */
    public void setTxtAccNo(java.lang.String txtAccNo) {
        this.txtAccNo = txtAccNo;
    }
    
    /**
     * Getter for property tdtFromDate.
     * @return Value of property tdtFromDate.
     */
    public java.util.Date getTdtFromDate() {
        return tdtFromDate;
    }
    
    /**
     * Setter for property tdtFromDate.
     * @param tdtFromDate New value of property tdtFromDate.
     */
    public void setTdtFromDate(java.util.Date tdtFromDate) {
        this.tdtFromDate = tdtFromDate;
    }
    
    /**
     * Getter for property tdtToDate.
     * @return Value of property tdtToDate.
     */
    public java.util.Date getTdtToDate() {
        return tdtToDate;
    }
    
    /**
     * Setter for property tdtToDate.
     * @param tdtToDate New value of property tdtToDate.
     */
    public void setTdtToDate(java.util.Date tdtToDate) {
        this.tdtToDate = tdtToDate;
    }
    
    /**
     * Getter for property lblAccNameValue.
     * @return Value of property lblAccNameValue.
     */
    public java.lang.String getLblAccNameValue() {
        return lblAccNameValue;
    }
    
    /**
     * Setter for property lblAccNameValue.
     * @param lblAccNameValue New value of property lblAccNameValue.
     */
    public void setLblAccNameValue(java.lang.String lblAccNameValue) {
        this.lblAccNameValue = lblAccNameValue;
    }
    
    /**
     * Getter for property linkMap.
     * @return Value of property linkMap.
     */
    public java.util.HashMap getLinkMap() {
        return linkMap;
    }
    
    /**
     * Setter for property linkMap.
     * @param linkMap New value of property linkMap.
     */
    public void setLinkMap(java.util.HashMap linkMap) {
        this.linkMap = linkMap;
    }
    
    /**
     * Getter for property cbmBranCode.
     * @return Value of property cbmBranCode.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmBranCode() {
        return cbmBranCode;
    }
    
    /**
     * Setter for property cbmBranCode.
     * @param cbmBranCode New value of property cbmBranCode.
     */
    public void setCbmBranCode(com.see.truetransact.clientutil.ComboBoxModel cbmBranCode) {
        this.cbmBranCode = cbmBranCode;
    }
    
}