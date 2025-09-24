/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PaddySaleMasterOB.java
 *
 * Created on Fri Jun 10 15:40:57 IST 2011
 */

package com.see.truetransact.ui.paddyprocurement;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.LinkedHashMap;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientproxy.*;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.TableUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.transferobject.paddyprocurement.PaddySaleMasterTO;
import com.see.truetransact.ui.common.transaction.TransactionOB;

import org.apache.log4j.Logger;

import java.io.OutputStream;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


/**
 *
 * @author
 */

public class PaddySaleMasterOB extends CObservable{
    
    private TransactionOB transactionOB;
    private LinkedHashMap transactionDetailsTO ;
    private LinkedHashMap deletedTransactionDetailsTO;
    private LinkedHashMap allowedTransactionDetailsTO ;
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    HashMap data = new HashMap();
    private static PaddySaleMasterOB objPaddySaleMasterOB;
    private PaddySaleMasterRB resourceBundle = new PaddySaleMasterRB();
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger _log = Logger.getLogger(PaddySaleMasterOB.class);
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private HashMap map;
    private ProxyFactory proxy = null;
    private Date currDt = null;
    private int actionType;
    private int result;
    private HashMap operationMap;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private String txtBillNo = "";
    private String saleSlNo = "";
    private String saleID = "";
    private String txtName = "";
    private String txtProductCode = "";
    private String txtRatePerKg = "";
    private String txtKiloGram = "";
    private String txtAmount = "";
    private String txtlAcreage = "";
    private String txtBags = "";
    private String tdtBillDate = "";
    private String txtAddress = "";
    private String tdtSaleDate = "";
    private String txtTotalAmount = "";
    private String txtProductDesc = "";
    private boolean newSale = false;
    private LinkedHashMap saleMap = new LinkedHashMap();
    private LinkedHashMap deletedSale = new LinkedHashMap();
    private HashMap _authorizeMap;
    private EnhancedTableModel tblSaleList;
    private final String PRIMARY = "Primary";
    private final String EMPTY_STRING = "";
    private final int ADDRTYPE_COLNO = 0;
    private final int INCPAR_COLNO = 0;
    private final int STATUS_COLNO = 1;
    
    /* Creates a new instance of BillsOB */
    public PaddySaleMasterOB() {
        currDt = ClientUtil.getCurrentDate();
        map = new HashMap();
        map.put(CommonConstants.JNDI, "PaddySaleMasterJNDI");
        map.put(CommonConstants.HOME, "serverside.paddyprocurement.PaddySaleMasterHome");
        map.put(CommonConstants.REMOTE, "serverside.paddyprocurement.PaddySaleMaster");
        try {
            proxy = ProxyFactory.createProxy();
            fillDropdown();
            createSaleTable();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    static {
        try {
            _log.info("Creating BillsOB...");
            objPaddySaleMasterOB = new PaddySaleMasterOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /**
     * Returns an instance of PaddySaleMasterOB.
     * @return  PaddySaleMasterOB
     */
    
    public static PaddySaleMasterOB getInstance()throws Exception{
        return objPaddySaleMasterOB;
    }
    
    /* Filling up the the ComboBox in the UI*/
    private void fillDropdown() throws Exception{
        try{
            _log.info("Inside FillDropDown");
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("CUSTOMER.CITY");
            lookup_keys.add("CUSTOMER.STATE");
            lookup_keys.add("CUSTOMER.COUNTRY");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get("CUSTOMER.CITY"));
        }catch(NullPointerException e){
            parseException.logException(e,true);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    public void populateSale(int row){
        try{
            saleTypeChanged(CommonUtil.convertObjToStr(tblSaleList.getValueAt(row,ADDRTYPE_COLNO)));
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void saleTypeChanged(String selectedItem){
        try{
            System.out.println("@#$@#$@#$saleTypeChanged");
            final PaddySaleMasterTO objPaddySaleMasterTO = (PaddySaleMasterTO)saleMap.get(selectedItem);
            populateSaleData(objPaddySaleMasterTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populateSaleData(PaddySaleMasterTO objPaddySaleMasterTO)  throws Exception{
        try{
            if(objPaddySaleMasterTO != null){
                setSaleSlNo(objPaddySaleMasterTO.getSaleSlNo());
                setTxtBillNo(objPaddySaleMasterTO.getTxtBillNo());
                setTxtAddress(objPaddySaleMasterTO.getTxtAddress());
                setTxtName(objPaddySaleMasterTO.getTxtName());
                setTxtProductCode(objPaddySaleMasterTO.getTxtProductCode());
                setTxtProductDesc(objPaddySaleMasterTO.getTxtProductDesc());
                setTxtRatePerKg(objPaddySaleMasterTO.getTxtRatePerKg());
                setTxtKiloGram(objPaddySaleMasterTO.getTxtKiloGram());
                setTxtAmount(objPaddySaleMasterTO.getTxtAmount());
                setTxtlAcreage(objPaddySaleMasterTO.getTxtlAcreage());
                setTxtBags(objPaddySaleMasterTO.getTxtBags());
                setTxtTotalAmount(objPaddySaleMasterTO.getTxtTotalAmount());
                setTdtBillDate(CommonUtil.convertObjToStr(objPaddySaleMasterTO.getTdtBillDate()));
                setTdtSaleDate(CommonUtil.convertObjToStr(objPaddySaleMasterTO.getTdtSaleDate()));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void resetSale(){
        setSaleSlNo("");
        setTxtBillNo("");
        setTxtName("");
        setTxtProductCode("");
        setTxtProductDesc("");
        setTxtRatePerKg("");
        setTxtKiloGram("");
        setTxtAmount("");
        setTxtlAcreage("");
        setTxtBags("");
        setTdtBillDate("");
        setTxtTotalAmount("");
        setTxtProductDesc("");
    }
    public void resetWhileSaleSave(){
        setTxtProductCode("");
        setTxtProductDesc("");
        setTxtRatePerKg("");
        setTxtKiloGram("");
        setTxtAmount("");
        setTxtlAcreage("");
        setTxtBags("");
    }
    
    protected void makeToNull(){
        objPaddySaleMasterOB= null;
    }
    public void populatePaddyPurchTransData(String drfTransID){
        HashMap whereMap = new HashMap();
        LinkedHashMap dataMap = null;
        List rowList = new ArrayList();
        List depreciationList = new ArrayList();
        String mapNameDT = "";
        String mapNameED = "";
        String mapNameGA = "";
        
        whereMap.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
        whereMap.put("DRF_TRANS_ID",drfTransID);
        
        HashMap drfTransTableMap = new HashMap();
        if(getActionType() != ClientConstants.ACTIONTYPE_NEW){
            mapNameDT = "getSelectPaddyPurchaseTransAuthList";
            List list = ClientUtil.executeQuery(mapNameDT,whereMap);
            for (int i = 0;i<list.size();i++){
                ArrayList technicalTabRow = new ArrayList();
                drfTransTableMap = (HashMap)list.get(i);
                technicalTabRow = new ArrayList();
                technicalTabRow.add(CommonUtil.convertObjToStr(drfTransTableMap.get("PADDY_PURCHASE_SLNO")));
                technicalTabRow.add(CommonUtil.convertObjToStr(drfTransTableMap.get("PRODUCT_CODE")));
                technicalTabRow.add(CommonUtil.convertObjToStr(drfTransTableMap.get("KILO_GRAMS")));
                technicalTabRow.add(CommonUtil.convertObjToStr(drfTransTableMap.get("TOTAL_AMOUNT")));
                tblSaleList.insertRow(tblSaleList.getRowCount(),technicalTabRow);
            }
        }
        whereMap = null;
    }
    public void populateData(HashMap whereMap) {
        _log.info("In populateData()");
        final HashMap mapData;
        try {
            mapData = proxy.executeQuery(whereMap,map);
            System.out.println("@#$@#$@#$mapData:"+mapData);
            if(mapData.containsKey("TRANSACTION_LIST")){
                List list = (List) mapData.get("TRANSACTION_LIST");
                if (!list.isEmpty()) {
                    transactionOB.setDetails(list);
                }
            }
            if(mapData.containsKey("SALE")){
                saleMap = (LinkedHashMap)mapData.get("SALE");
                ArrayList addList =new ArrayList(saleMap.keySet());
                for(int i=0;i<addList.size();i++){
                    PaddySaleMasterTO objPaddySaleMasterTO = (PaddySaleMasterTO) saleMap.get(addList.get(i));
                    objPaddySaleMasterTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objPaddySaleMasterTO.setStatusBy(TrueTransactMain.USER_ID);
                    objPaddySaleMasterTO.setStatusDt(currDt);
                    saleMap.put(objPaddySaleMasterTO.getSaleSlNo(), objPaddySaleMasterTO);
                }
                populateSaleTable();
            }
            
        } catch( Exception e ) {
            System.out.println("Error In populateData()");
            parseException.logException(e,true);
        }
    }
    public void  sale(int row,boolean familyDetailsFlag){
        try{
            final PaddySaleMasterTO objPaddySaleMasterTO=new PaddySaleMasterTO();
            if( saleMap == null ){
                saleMap = new LinkedHashMap();
            }
            
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isNewSale()){
                    objPaddySaleMasterTO.setStatus(CommonConstants.STATUS_CREATED);
                    objPaddySaleMasterTO.setStatusBy(TrueTransactMain.USER_ID);
                    objPaddySaleMasterTO.setStatusDt(currDt);
                }else{
                    objPaddySaleMasterTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objPaddySaleMasterTO.setStatusBy(TrueTransactMain.USER_ID);
                    objPaddySaleMasterTO.setStatusDt(currDt);
                }
            }else{
                objPaddySaleMasterTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            int slno;
            slno=0;
            if(!familyDetailsFlag){
                ArrayList data = tblSaleList.getDataArrayList();
                slno=serialNo(data,tblSaleList);
            }
            else if(isNewSale()){
                int b=CommonUtil.convertObjToInt(tblSaleList.getValueAt(row,0));
                slno= b + tblSaleList.getRowCount();
            }
            else{
                slno = CommonUtil.convertObjToInt(tblSaleList.getValueAt(row,0));
            }
            objPaddySaleMasterTO.setSaleSlNo(String.valueOf(slno));
            objPaddySaleMasterTO.setTxtBillNo(getTxtBillNo());
            objPaddySaleMasterTO.setTxtName(getTxtName());
            objPaddySaleMasterTO.setTxtProductCode(getTxtProductCode());
            objPaddySaleMasterTO.setTxtProductDesc(getTxtProductDesc());
            objPaddySaleMasterTO.setTxtRatePerKg(getTxtRatePerKg());
            objPaddySaleMasterTO.setTxtKiloGram(getTxtKiloGram());
            objPaddySaleMasterTO.setTxtAmount(getTxtAmount());
            objPaddySaleMasterTO.setTxtlAcreage(getTxtlAcreage());
            objPaddySaleMasterTO.setTxtBags(getTxtBags());
            objPaddySaleMasterTO.setTdtBillDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtBillDate())));
            objPaddySaleMasterTO.setTxtAddress(getTxtAddress());
            objPaddySaleMasterTO.setTdtSaleDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtSaleDate())));
            Date billDate = DateUtil.getDateMMDDYYYY(getTdtBillDate());
            if(billDate != null){
                Date dJoin = (Date)currDt.clone();
                dJoin.setDate(billDate.getDate());
                dJoin.setMonth(billDate.getMonth());
                dJoin.setYear(billDate.getYear());
                objPaddySaleMasterTO.setTdtBillDate(dJoin);
            }else{
                objPaddySaleMasterTO.setTdtBillDate(DateUtil.getDateMMDDYYYY(getTdtBillDate()));
            }
            Date saleDate = DateUtil.getDateMMDDYYYY(getTdtSaleDate());
            if(saleDate != null){
                Date dJoin = (Date)currDt.clone();
                dJoin.setDate(saleDate.getDate());
                dJoin.setMonth(saleDate.getMonth());
                dJoin.setYear(saleDate.getYear());
                objPaddySaleMasterTO.setTdtSaleDate(dJoin);
            }else{
                objPaddySaleMasterTO.setTdtSaleDate(DateUtil.getDateMMDDYYYY(getTdtSaleDate()));
            }
            objPaddySaleMasterTO.setStatusBy(TrueTransactMain.USER_ID);
            saleMap.put(CommonUtil.convertObjToStr(String.valueOf(slno)), objPaddySaleMasterTO);
            updatetblSaleList(row,objPaddySaleMasterTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
        
    }
    
    public int serialNo(ArrayList data, EnhancedTableModel table_name){
        final int dataSize = data.size();
        int nums[]= new int[150];
        int max=nums[0];
        int slno=0;
        int a=0;
        slno=dataSize+1;
        for(int i=0;i<data.size();i++){
            a=CommonUtil.convertObjToInt(table_name.getValueAt(i,0));
            nums[i]=a;
            if(nums[i]>max)
                max=nums[i];
            slno=max+1;
        }
        return slno;
    }
    
    
    private void createSaleTable() throws Exception{
        final ArrayList saleColumn = new ArrayList();
        saleColumn.add("Sl No.");
        saleColumn.add("Product Code");
        saleColumn.add("Quantity");
        saleColumn.add("Amount");
        tblSaleList= new EnhancedTableModel(null, saleColumn);
    }
    
    public int showAlertWindow(String amtLimit) {
        int option = 1;
        try{
            String[] options = {resourceBundle.getString("cDialogYes"), resourceBundle.getString("CDialogNo")};
            option = COptionPane.showOptionDialog(null,amtLimit, CommonConstants.WARNINGTITLE,
            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
            null, options, options[0]);
        }catch (Exception e){
            parseException.logException(e,true);
        }
        return option;
    }
    
    
    
    public void deleteSale(int row){
        if(deletedSale == null){
            deletedSale = new LinkedHashMap();
        }
        deletedSale.put(CommonUtil.convertObjToStr(tblSaleList.getValueAt(row,ADDRTYPE_COLNO)),saleMap.get(CommonUtil.convertObjToStr(tblSaleList.getValueAt(row,ADDRTYPE_COLNO))));
        saleMap.remove(tblSaleList.getValueAt(row,ADDRTYPE_COLNO));
        resetDeleteSale();
    }
    
    public void resetDeleteSale(){
        try{
            
            resetSale();
            resetSaleListTable();
            populateSaleTable();
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populateSaleTable()  throws Exception{
        ArrayList dependentDataLst = new ArrayList();
        dependentDataLst = new ArrayList(saleMap.keySet());
        ArrayList addList =new ArrayList(saleMap.keySet());
        int length = dependentDataLst.size();
        for(int i=0; i<length; i++){
            ArrayList dependentTabRow = new ArrayList();
            PaddySaleMasterTO objPaddySaleMasterTO = (PaddySaleMasterTO) saleMap.get(addList.get(i));
            dependentTabRow = new ArrayList();
            dependentTabRow.add(CommonUtil.convertObjToStr(objPaddySaleMasterTO.getSaleSlNo()));
            dependentTabRow.add(CommonUtil.convertObjToStr(objPaddySaleMasterTO.getTxtProductCode()));
            dependentTabRow.add(CommonUtil.convertObjToStr(objPaddySaleMasterTO.getTxtKiloGram()));
            dependentTabRow.add(CommonUtil.convertObjToStr(objPaddySaleMasterTO.getTxtAmount()));
            tblSaleList.insertRow(tblSaleList.getRowCount(),dependentTabRow);
            dependentTabRow= null;
        }
    }
    
    public void resetSaleListTable(){
        for(int i = tblSaleList.getRowCount(); i > 0; i--){
            tblSaleList.removeRow(0);
        }
    }
    
    
    private void updatetblSaleList(int row,PaddySaleMasterTO objPaddySaleMasterTO)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        for(int i = tblSaleList.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblSaleList.getDataArrayList().get(j)).get(0);
            if(CommonUtil.convertObjToStr(getSaleSlNo()).equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
            }
        }
        
        ArrayList addressRow = new ArrayList();
        if(row == -1){
            addressRow.add(CommonUtil.convertObjToStr(objPaddySaleMasterTO.getSaleSlNo()));
            addressRow.add(CommonUtil.convertObjToStr(objPaddySaleMasterTO.getTxtProductCode()));
            addressRow.add(CommonUtil.convertObjToStr(objPaddySaleMasterTO.getTxtKiloGram()));
            addressRow.add(CommonUtil.convertObjToStr(objPaddySaleMasterTO.getTxtAmount()));
            tblSaleList.insertRow(tblSaleList.getRowCount(),addressRow);
            addressRow = null;
        }else{
            tblSaleList.removeRow(row);
            addressRow.add(CommonUtil.convertObjToStr(objPaddySaleMasterTO.getSaleSlNo()));
            addressRow.add(CommonUtil.convertObjToStr(objPaddySaleMasterTO.getTxtProductCode()));
            addressRow.add(CommonUtil.convertObjToStr(objPaddySaleMasterTO.getTxtKiloGram()));
            addressRow.add(CommonUtil.convertObjToStr(objPaddySaleMasterTO.getTxtAmount()));
            tblSaleList.insertRow(row,addressRow);
            addressRow = null;
        }
    }
    
    private void insertData() throws Exception{
        setSaleTransData();
    }
    private void updateData() throws Exception{
        setSaleTransData();
    }
    private void deleteData() throws Exception{
        setSaleTransData();
        data.put("SALEID",getSaleID());
    }
    
    private void setSaleTransData(){
        if(saleMap!=null && saleMap.size() > 0){
            data.put("SALE",saleMap);
        }
        if(deletedSale != null && deletedSale.size() > 0){
            data.put("DELETEDSALE",deletedSale);
        }
        if(CommonUtil.convertObjToStr(getTxtTotalAmount()).length() > 0){
            data.put("TOTALAMOUNT",getTxtTotalAmount());
        }
        if(allowedTransactionDetailsTO!=null && allowedTransactionDetailsTO.size()>0){
            if (transactionDetailsTO == null){
                transactionDetailsTO = new LinkedHashMap();
            }
            transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
            data.put("TransactionTO",transactionDetailsTO);
            allowedTransactionDetailsTO = null;
        }
    }
    /* Executes Query using the TO object */
    public HashMap doAction(){
        HashMap proxyResultMap = new HashMap();
        try{
            if(data ==null){
                data = new HashMap();
            }
            
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                switch(actionType) {
                    case ClientConstants.ACTIONTYPE_NEW:
                        insertData();
                        break;
                    case ClientConstants.ACTIONTYPE_EDIT:
                        updateData();
                        break;
                    case ClientConstants.ACTIONTYPE_DELETE:
                        deleteData();
                        break;
                    default:
                        // throw new ActionNotFoundException();
                }
                
                if( actionType != ClientConstants.ACTIONTYPE_CANCEL || get_authorizeMap() != null){
                    if( get_authorizeMap() != null){
                        data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
                    }
                    if(allowedTransactionDetailsTO!=null && allowedTransactionDetailsTO.size()>0){
                        if (transactionDetailsTO == null){
                            transactionDetailsTO = new LinkedHashMap();
                        }
                        transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
                        data.put("TransactionTO",transactionDetailsTO);
                        allowedTransactionDetailsTO = null;
                    }
                    _authorizeMap=null;
                }
                
                data.put(CommonConstants.MODULE, getModule());
                data.put(CommonConstants.SCREEN, getScreen());
                data.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
                data.put("COMMAND", getCommand());
                data.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                data.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                data.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
                System.out.println("!@#@@@####data"+data);
                proxyResultMap = proxy.execute(data, map);
                setProxyReturnMap(proxyResultMap);
                System.out.println("######## proxyResultMap :"+proxyResultMap);
                data = null;
                setResult(actionType);
                actionType = ClientConstants.ACTIONTYPE_CANCEL;
            }
        }catch(Exception e){
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
        return proxyResultMap;
    }
    
    /* To get the type of command */
    private String getCommand() throws Exception{
        String command = null;
        switch (getActionType()) {
            case ClientConstants.ACTIONTYPE_NEW:
                command = CommonConstants.TOSTATUS_INSERT;
                break;
            case ClientConstants.ACTIONTYPE_EDIT:
                command = CommonConstants.TOSTATUS_UPDATE;
                break;
                
            case ClientConstants.ACTIONTYPE_DELETE:
                command = CommonConstants.TOSTATUS_DELETE;
                break;
            default:
        }
        return command;
    }
    
    void setActionType(int actionType){
        this.actionType = actionType;
        setChanged();
    }
    int getActionType(){
        return this.actionType;
    }
    
    public int getResult(){
        return this.result;
    }
    
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    
    public String getLblStatus(){
        return this.lblStatus;
    }
    
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    public void ttNotifyObservers(){
        notifyObservers();
        //        setChanged();
    }
    
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
        setChanged();
        
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }
    
    
    /* Resetting all the Fields in the UI */
    public void resetForm(){
        
        setTxtAddress("");
        setTxtAmount("");
        setTxtBags("");
        setTxtBillNo("");
        setTxtKiloGram("");
        setTxtName("");
        setTxtProductCode("");
        setTxtProductDesc("");
        setTxtRatePerKg("");
        setTxtTotalAmount("");
        setTxtlAcreage("");
        setTdtBillDate("");
        setTdtSaleDate("");
        resetSaleListTable();
        ttNotifyObservers();
    }
    
    
    
    /**
     * Getter for property txtName.
     * @return Value of property txtName.
     */
    public java.lang.String getTxtName() {
        return txtName;
    }
    
    /**
     * Setter for property txtName.
     * @param txtName New value of property txtName.
     */
    public void setTxtName(java.lang.String txtName) {
        this.txtName = txtName;
    }
    
    /**
     * Getter for property txtProductCode.
     * @return Value of property txtProductCode.
     */
    public java.lang.String getTxtProductCode() {
        return txtProductCode;
    }
    
    /**
     * Setter for property txtProductCode.
     * @param txtProductCode New value of property txtProductCode.
     */
    public void setTxtProductCode(java.lang.String txtProductCode) {
        this.txtProductCode = txtProductCode;
    }
    
    /**
     * Getter for property txtRatePerKg.
     * @return Value of property txtRatePerKg.
     */
    public java.lang.String getTxtRatePerKg() {
        return txtRatePerKg;
    }
    
    /**
     * Setter for property txtRatePerKg.
     * @param txtRatePerKg New value of property txtRatePerKg.
     */
    public void setTxtRatePerKg(java.lang.String txtRatePerKg) {
        this.txtRatePerKg = txtRatePerKg;
    }
    
    /**
     * Getter for property txtKiloGram.
     * @return Value of property txtKiloGram.
     */
    public java.lang.String getTxtKiloGram() {
        return txtKiloGram;
    }
    
    /**
     * Setter for property txtKiloGram.
     * @param txtKiloGram New value of property txtKiloGram.
     */
    public void setTxtKiloGram(java.lang.String txtKiloGram) {
        this.txtKiloGram = txtKiloGram;
    }
    
    /**
     * Getter for property txtAmount.
     * @return Value of property txtAmount.
     */
    public java.lang.String getTxtAmount() {
        return txtAmount;
    }
    
    /**
     * Setter for property txtAmount.
     * @param txtAmount New value of property txtAmount.
     */
    public void setTxtAmount(java.lang.String txtAmount) {
        this.txtAmount = txtAmount;
    }
    
    /**
     * Getter for property txtlAcreage.
     * @return Value of property txtlAcreage.
     */
    public java.lang.String getTxtlAcreage() {
        return txtlAcreage;
    }
    
    /**
     * Setter for property txtlAcreage.
     * @param txtlAcreage New value of property txtlAcreage.
     */
    public void setTxtlAcreage(java.lang.String txtlAcreage) {
        this.txtlAcreage = txtlAcreage;
    }
    
    /**
     * Getter for property txtBags.
     * @return Value of property txtBags.
     */
    public java.lang.String getTxtBags() {
        return txtBags;
    }
    
    /**
     * Setter for property txtBags.
     * @param txtBags New value of property txtBags.
     */
    public void setTxtBags(java.lang.String txtBags) {
        this.txtBags = txtBags;
    }
    
    /**
     * Getter for property tdtBillDate.
     * @return Value of property tdtBillDate.
     */
    public java.lang.String getTdtBillDate() {
        return tdtBillDate;
    }
    
    /**
     * Setter for property tdtBillDate.
     * @param tdtBillDate New value of property tdtBillDate.
     */
    public void setTdtBillDate(java.lang.String tdtBillDate) {
        this.tdtBillDate = tdtBillDate;
    }
    
    /**
     * Getter for property txtAddress.
     * @return Value of property txtAddress.
     */
    public java.lang.String getTxtAddress() {
        return txtAddress;
    }
    
    /**
     * Setter for property txtAddress.
     * @param txtAddress New value of property txtAddress.
     */
    public void setTxtAddress(java.lang.String txtAddress) {
        this.txtAddress = txtAddress;
    }
    
    
    /**
     * Getter for property txtTotalAmount.
     * @return Value of property txtTotalAmount.
     */
    public java.lang.String getTxtTotalAmount() {
        return txtTotalAmount;
    }
    
    /**
     * Setter for property txtTotalAmount.
     * @param txtTotalAmount New value of property txtTotalAmount.
     */
    public void setTxtTotalAmount(java.lang.String txtTotalAmount) {
        this.txtTotalAmount = txtTotalAmount;
    }
    
    /**
     * Getter for property txtProductDesc.
     * @return Value of property txtProductDesc.
     */
    public java.lang.String getTxtProductDesc() {
        return txtProductDesc;
    }
    
    /**
     * Setter for property txtProductDesc.
     * @param txtProductDesc New value of property txtProductDesc.
     */
    public void setTxtProductDesc(java.lang.String txtProductDesc) {
        this.txtProductDesc = txtProductDesc;
    }
    
    /**
     * Getter for property tblSaleList.
     * @return Value of property tblSaleList.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel gettblSaleList() {
        return tblSaleList;
    }
    
    /**
     * Setter for property tblSaleList.
     * @param tblSaleList New value of property tblSaleList.
     */
    public void settblSaleList(com.see.truetransact.clientutil.EnhancedTableModel tblSaleList) {
        this.tblSaleList = tblSaleList;
    }
    
    /**
     * Getter for property transactionOB.
     * @return Value of property transactionOB.
     */
    public com.see.truetransact.ui.common.transaction.TransactionOB getTransactionOB() {
        return transactionOB;
    }
    
    /**
     * Setter for property transactionOB.
     * @param transactionOB New value of property transactionOB.
     */
    public void setTransactionOB(com.see.truetransact.ui.common.transaction.TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }
    
    /**
     * Getter for property allowedTransactionDetailsTO.
     * @return Value of property allowedTransactionDetailsTO.
     */
    public java.util.LinkedHashMap getAllowedTransactionDetailsTO() {
        return allowedTransactionDetailsTO;
    }
    
    /**
     * Setter for property allowedTransactionDetailsTO.
     * @param allowedTransactionDetailsTO New value of property allowedTransactionDetailsTO.
     */
    public void setAllowedTransactionDetailsTO(java.util.LinkedHashMap allowedTransactionDetailsTO) {
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }
    
    /**
     * Getter for property _authorizeMap.
     * @return Value of property _authorizeMap.
     */
    public java.util.HashMap get_authorizeMap() {
        return _authorizeMap;
    }
    
    /**
     * Setter for property _authorizeMap.
     * @param _authorizeMap New value of property _authorizeMap.
     */
    public void set_authorizeMap(java.util.HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }
    
    /**
     * Getter for property txtBillNo.
     * @return Value of property txtBillNo.
     */
    public java.lang.String getTxtBillNo() {
        return txtBillNo;
    }
    
    /**
     * Setter for property txtBillNo.
     * @param txtBillNo New value of property txtBillNo.
     */
    public void setTxtBillNo(java.lang.String txtBillNo) {
        this.txtBillNo = txtBillNo;
    }
    
    /**
     * Getter for property tblSaleList.
     * @return Value of property tblSaleList.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblSaleList() {
        return tblSaleList;
    }
    
    /**
     * Setter for property tblSaleList.
     * @param tblSaleList New value of property tblSaleList.
     */
    public void setTblSaleList(com.see.truetransact.clientutil.EnhancedTableModel tblSaleList) {
        this.tblSaleList = tblSaleList;
    }
    
    /**
     * Getter for property tdtSaleDate.
     * @return Value of property tdtSaleDate.
     */
    public java.lang.String getTdtSaleDate() {
        return tdtSaleDate;
    }
    
    /**
     * Setter for property tdtSaleDate.
     * @param tdtSaleDate New value of property tdtSaleDate.
     */
    public void setTdtSaleDate(java.lang.String tdtSaleDate) {
        this.tdtSaleDate = tdtSaleDate;
    }
    
    /**
     * Getter for property saleSlNo.
     * @return Value of property saleSlNo.
     */
    public java.lang.String getSaleSlNo() {
        return saleSlNo;
    }
    
    /**
     * Setter for property saleSlNo.
     * @param saleSlNo New value of property saleSlNo.
     */
    public void setSaleSlNo(java.lang.String saleSlNo) {
        this.saleSlNo = saleSlNo;
    }
    
    /**
     * Getter for property saleID.
     * @return Value of property saleID.
     */
    public java.lang.String getSaleID() {
        return saleID;
    }
    
    /**
     * Setter for property saleID.
     * @param saleID New value of property saleID.
     */
    public void setSaleID(java.lang.String saleID) {
        this.saleID = saleID;
    }
    
    /**
     * Getter for property newSale.
     * @return Value of property newSale.
     */
    public boolean isNewSale() {
        return newSale;
    }
    
    /**
     * Setter for property newSale.
     * @param newSale New value of property newSale.
     */
    public void setNewSale(boolean newSale) {
        this.newSale = newSale;
    }
    
}

