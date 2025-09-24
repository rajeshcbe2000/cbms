/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PaddyPurchaseMasterOB.java
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
import com.see.truetransact.transferobject.paddyprocurement.PaddyPurchaseMasterTO;
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

public class PaddyPurchaseMasterOB extends CObservable{
    
    private TransactionOB transactionOB;
    private LinkedHashMap transactionDetailsTO ;
    private LinkedHashMap deletedTransactionDetailsTO;
    private LinkedHashMap allowedTransactionDetailsTO ;
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    HashMap data = new HashMap();
    private static PaddyPurchaseMasterOB objPaddyPurchaseMasterOB;
    private PaddyPurchaseMasterRB resourceBundle = new PaddyPurchaseMasterRB();
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger _log = Logger.getLogger(PaddyPurchaseMasterOB.class);
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
    private String purchaseSlNo = "";
    private String txtCnDNo = "";
    private String purchaseId = "";
    private String txtName = "";
    private String txtLocalityCode = "";
    private String txtLocalityName = "";
    private String txtProductCode = "";
    private String txtRatePerKg = "";
    private String txtKiloGram = "";
    private String txtAmount = "";
    private String txtlAcreage = "";
    private String txtBags = "";
    private String tdtBillDate = "";
    private String txtAddress = "";
    private String tdtPurchaseDate = "";
    private String txtTotalAmount = "";
    private String txtProductDesc = "";
    private boolean newPurchase = false;
    private LinkedHashMap purchaseMap = new LinkedHashMap();
    private LinkedHashMap deletedPurchase = new LinkedHashMap();
    private HashMap _authorizeMap;
    private EnhancedTableModel tblPurchaseList;
    private final String PRIMARY = "Primary";
    private final String EMPTY_STRING = "";
    private final int ADDRTYPE_COLNO = 0;
    private final int INCPAR_COLNO = 0;
    private final int STATUS_COLNO = 1;
    
    /* Creates a new instance of BillsOB */
    public PaddyPurchaseMasterOB() {
        currDt = ClientUtil.getCurrentDate();
        map = new HashMap();
        map.put(CommonConstants.JNDI, "PaddyPurchaseMasterJNDI");
        map.put(CommonConstants.HOME, "serverside.paddyprocurement.PaddyPurchaseMasterHome");
        map.put(CommonConstants.REMOTE, "serverside.paddyprocurement.PaddyPurchaseMaster");
        try {
            proxy = ProxyFactory.createProxy();
            fillDropdown();
            createPurchaseTable();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    static {
        try {
            _log.info("Creating BillsOB...");
            objPaddyPurchaseMasterOB = new PaddyPurchaseMasterOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /**
     * Returns an instance of PaddyPurchaseMasterOB.
     * @return  PaddyPurchaseMasterOB
     */
    
    public static PaddyPurchaseMasterOB getInstance()throws Exception{
        return objPaddyPurchaseMasterOB;
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
    
    public void populatePurchase(int row){
        try{
            purchaseTypeChanged(CommonUtil.convertObjToStr(tblPurchaseList.getValueAt(row,ADDRTYPE_COLNO)));
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void purchaseTypeChanged(String selectedItem){
        try{
            System.out.println("@#$@#$@#$purchaseTypeChanged");
            final PaddyPurchaseMasterTO objPaddyPurchaseMasterTO = (PaddyPurchaseMasterTO)purchaseMap.get(selectedItem);
            populatePurchaseData(objPaddyPurchaseMasterTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populatePurchaseData(PaddyPurchaseMasterTO objPaddyPurchaseMasterTO)  throws Exception{
        try{
            if(objPaddyPurchaseMasterTO != null){
                setPurchaseSlNo(objPaddyPurchaseMasterTO.getPurchaseSlNo());
                setTxtCnDNo(objPaddyPurchaseMasterTO.getTxtCnDNo());
                setTxtAddress(objPaddyPurchaseMasterTO.getTxtAddress());
                setTxtName(objPaddyPurchaseMasterTO.getTxtName());
                setTxtLocalityCode(objPaddyPurchaseMasterTO.getTxtLocalityCode());
                setTxtLocalityName(objPaddyPurchaseMasterTO.getTxtLocalityName());
                setTxtProductCode(objPaddyPurchaseMasterTO.getTxtProductCode());
                setTxtProductDesc(objPaddyPurchaseMasterTO.getTxtProductDesc());
                setTxtRatePerKg(objPaddyPurchaseMasterTO.getTxtRatePerKg());
                setTxtKiloGram(objPaddyPurchaseMasterTO.getTxtKiloGram());
                setTxtAmount(objPaddyPurchaseMasterTO.getTxtAmount());
                setTxtlAcreage(objPaddyPurchaseMasterTO.getTxtlAcreage());
                setTxtBags(objPaddyPurchaseMasterTO.getTxtBags());
                setTxtTotalAmount(objPaddyPurchaseMasterTO.getTxtTotalAmount());
                setTdtBillDate(CommonUtil.convertObjToStr(objPaddyPurchaseMasterTO.getTdtBillDate()));
                setTdtPurchaseDate(CommonUtil.convertObjToStr(objPaddyPurchaseMasterTO.getTdtPurchaseDate()));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void resetPurchase(){
        setPurchaseSlNo("");
        setTxtCnDNo("");
        setTxtName("");
        setTxtLocalityCode("");
        setTxtLocalityName("");
        setTxtProductCode("");
        setTxtProductDesc("");
        setTxtRatePerKg("");
        setTxtKiloGram("");
        setTxtAmount("");
        setTxtlAcreage("");
        setTxtBags("");
        setTdtBillDate("");
        setTxtAddress("");
        setTdtPurchaseDate("");
        setTxtTotalAmount("");
        setTxtProductDesc("");
    }
    public void resetWhilePurchaseSave(){
        setTxtProductCode("");
        setTxtProductDesc("");
        setTxtRatePerKg("");
        setTxtKiloGram("");
        setTxtAmount("");
        setTxtlAcreage("");
        setTxtBags("");
    }
    
    protected void makeToNull(){
        objPaddyPurchaseMasterOB= null;
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
            //            mapNameED = "getSelectPromotionEditTO";
            List list = ClientUtil.executeQuery(mapNameDT,whereMap);
            for (int i = 0;i<list.size();i++){
                ArrayList technicalTabRow = new ArrayList();
                drfTransTableMap = (HashMap)list.get(i);
                technicalTabRow = new ArrayList();
                technicalTabRow.add(CommonUtil.convertObjToStr(drfTransTableMap.get("PADDY_PURCHASE_SLNO")));
                technicalTabRow.add(CommonUtil.convertObjToStr(drfTransTableMap.get("PRODUCT_CODE")));
                technicalTabRow.add(CommonUtil.convertObjToStr(drfTransTableMap.get("KILO_GRAMS")));
                technicalTabRow.add(CommonUtil.convertObjToStr(drfTransTableMap.get("TOTAL_AMOUNT")));
                tblPurchaseList.insertRow(tblPurchaseList.getRowCount(),technicalTabRow);
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
            if(mapData.containsKey("PURCHASE")){
                purchaseMap = (LinkedHashMap)mapData.get("PURCHASE");
                ArrayList addList =new ArrayList(purchaseMap.keySet());
                for(int i=0;i<addList.size();i++){
                    PaddyPurchaseMasterTO objPaddyPurchaseMasterTO = (PaddyPurchaseMasterTO) purchaseMap.get(addList.get(i));
                    objPaddyPurchaseMasterTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objPaddyPurchaseMasterTO.setStatusBy(TrueTransactMain.USER_ID);
                    objPaddyPurchaseMasterTO.setStatusDt(currDt);
                    purchaseMap.put(objPaddyPurchaseMasterTO.getPurchaseSlNo(), objPaddyPurchaseMasterTO); 
                }
                populatePurchaseTable();
            }
        } catch( Exception e ) {
            System.out.println("Error In populateData()");
            parseException.logException(e,true);
        }
    }
    public void  purchase(int row,boolean familyDetailsFlag){
        try{
            final PaddyPurchaseMasterTO objPaddyPurchaseMasterTO=new PaddyPurchaseMasterTO();
            if( purchaseMap == null ){
                purchaseMap = new LinkedHashMap();
            }
            
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isNewPurchase()){
                    objPaddyPurchaseMasterTO.setStatus(CommonConstants.STATUS_CREATED);
                    objPaddyPurchaseMasterTO.setStatusBy(TrueTransactMain.USER_ID);
                    objPaddyPurchaseMasterTO.setStatusDt(currDt);
                }else{
                    objPaddyPurchaseMasterTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objPaddyPurchaseMasterTO.setStatusBy(TrueTransactMain.USER_ID);
                    objPaddyPurchaseMasterTO.setStatusDt(currDt);
                }
            }else{
                objPaddyPurchaseMasterTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            if( actionType == ClientConstants.ACTIONTYPE_EDIT ){
            }
            
            int slno;
            slno=0;
            
            if(!familyDetailsFlag){
                
                ArrayList data = tblPurchaseList.getDataArrayList();
                slno=serialNo(data,tblPurchaseList);
            }
            else if(isNewPurchase()){
                int b=CommonUtil.convertObjToInt(tblPurchaseList.getValueAt(row,0));
                slno= b + tblPurchaseList.getRowCount();
            }
            else{
                slno = CommonUtil.convertObjToInt(tblPurchaseList.getValueAt(row,0));
            }
            
            
            objPaddyPurchaseMasterTO.setPurchaseSlNo(String.valueOf(slno));
            
            objPaddyPurchaseMasterTO.setTxtCnDNo(getTxtCnDNo());
            objPaddyPurchaseMasterTO.setTxtName(getTxtName());
            objPaddyPurchaseMasterTO.setTxtLocalityCode(getTxtLocalityCode());
            objPaddyPurchaseMasterTO.setTxtLocalityName(getTxtLocalityName());
            objPaddyPurchaseMasterTO.setTxtProductCode(getTxtProductCode());
            objPaddyPurchaseMasterTO.setTxtProductDesc(getTxtProductDesc());
            objPaddyPurchaseMasterTO.setTxtRatePerKg(getTxtRatePerKg());
            objPaddyPurchaseMasterTO.setTxtKiloGram(getTxtKiloGram());
            objPaddyPurchaseMasterTO.setTxtAmount(getTxtAmount());
            objPaddyPurchaseMasterTO.setTxtlAcreage(getTxtlAcreage());
            objPaddyPurchaseMasterTO.setTxtBags(getTxtBags());
            objPaddyPurchaseMasterTO.setTdtBillDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtBillDate())));
            objPaddyPurchaseMasterTO.setTxtAddress(getTxtAddress());
            objPaddyPurchaseMasterTO.setTdtPurchaseDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtPurchaseDate())));
            Date billDate = DateUtil.getDateMMDDYYYY(getTdtBillDate());
            if(billDate != null){
                Date dJoin = (Date)currDt.clone();
                dJoin.setDate(billDate.getDate());
                dJoin.setMonth(billDate.getMonth());
                dJoin.setYear(billDate.getYear());
                objPaddyPurchaseMasterTO.setTdtBillDate(dJoin);
            }else{
                objPaddyPurchaseMasterTO.setTdtBillDate(DateUtil.getDateMMDDYYYY(getTdtBillDate()));
            }
            
            Date purchaseDate = DateUtil.getDateMMDDYYYY(getTdtPurchaseDate());
            if(purchaseDate != null){
                Date dJoin = (Date)currDt.clone();
                dJoin.setDate(purchaseDate.getDate());
                dJoin.setMonth(purchaseDate.getMonth());
                dJoin.setYear(purchaseDate.getYear());
                objPaddyPurchaseMasterTO.setTdtPurchaseDate(dJoin);
            }else{
                objPaddyPurchaseMasterTO.setTdtPurchaseDate(DateUtil.getDateMMDDYYYY(getTdtPurchaseDate()));
            }
            objPaddyPurchaseMasterTO.setStatusBy(TrueTransactMain.USER_ID);
            purchaseMap.put(CommonUtil.convertObjToStr(String.valueOf(slno)), objPaddyPurchaseMasterTO);
            updateTblPurchaseList(row,objPaddyPurchaseMasterTO);
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
    
    
    private void createPurchaseTable() throws Exception{
        final ArrayList purchaseColumn = new ArrayList();
        purchaseColumn.add("Sl No.");
        purchaseColumn.add("Product Code");
        purchaseColumn.add("Quantity");
        purchaseColumn.add("Amount");
        tblPurchaseList = new EnhancedTableModel(null, purchaseColumn);
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
    
    
    
    public void deletePurchase(int row){
        if(deletedPurchase == null){
            deletedPurchase = new LinkedHashMap();
        }
        PaddyPurchaseMasterTO objPaddyPurchaseMasterTO= (PaddyPurchaseMasterTO)purchaseMap.get(CommonUtil.convertObjToStr(tblPurchaseList.getValueAt(row,ADDRTYPE_COLNO)));
        objPaddyPurchaseMasterTO.setStatus(CommonConstants.STATUS_DELETED);
        objPaddyPurchaseMasterTO.setStatusDt(currDt);
        objPaddyPurchaseMasterTO.setStatusBy(TrueTransactMain.USER_ID);
        deletedPurchase.put(CommonUtil.convertObjToStr(tblPurchaseList.getValueAt(row,ADDRTYPE_COLNO)),purchaseMap.get(CommonUtil.convertObjToStr(tblPurchaseList.getValueAt(row,ADDRTYPE_COLNO))));
        purchaseMap.remove(tblPurchaseList.getValueAt(row,ADDRTYPE_COLNO));
        resetDeletePurchase();
    }
    
    public void resetDeletePurchase(){
        try{
            
            resetPurchase();
            resetPurchaseListTable();
            populatePurchaseTable();
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populatePurchaseTable()  throws Exception{
        ArrayList dependentDataLst = new ArrayList();
        dependentDataLst = new ArrayList(purchaseMap.keySet());
        ArrayList addList =new ArrayList(purchaseMap.keySet());
        int length = dependentDataLst.size();
        for(int i=0; i<length; i++){
            ArrayList dependentTabRow = new ArrayList();
            PaddyPurchaseMasterTO objPaddyPurchaseMasterTO = (PaddyPurchaseMasterTO) purchaseMap.get(addList.get(i));
            dependentTabRow = new ArrayList();
            dependentTabRow.add(CommonUtil.convertObjToStr(objPaddyPurchaseMasterTO.getPurchaseSlNo()));
            dependentTabRow.add(CommonUtil.convertObjToStr(objPaddyPurchaseMasterTO.getTxtProductCode()));
            dependentTabRow.add(CommonUtil.convertObjToStr(objPaddyPurchaseMasterTO.getTxtKiloGram()));
            dependentTabRow.add(CommonUtil.convertObjToStr(objPaddyPurchaseMasterTO.getTxtAmount()));
            tblPurchaseList.insertRow(tblPurchaseList.getRowCount(),dependentTabRow);
            dependentTabRow= null;
        }
    }
    
    public void resetPurchaseListTable(){
        for(int i = tblPurchaseList.getRowCount(); i > 0; i--){
            tblPurchaseList.removeRow(0);
        }
    }
    
    
    private void updateTblPurchaseList(int row,PaddyPurchaseMasterTO objPaddyPurchaseMasterTO)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        for(int i = tblPurchaseList.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblPurchaseList.getDataArrayList().get(j)).get(0);
            if(CommonUtil.convertObjToStr(getPurchaseSlNo()).equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
            }
        }
        
        ArrayList addressRow = new ArrayList();
        if(row == -1){
            addressRow.add(CommonUtil.convertObjToStr(objPaddyPurchaseMasterTO.getPurchaseSlNo()));
            addressRow.add(CommonUtil.convertObjToStr(objPaddyPurchaseMasterTO.getTxtProductCode()));
            addressRow.add(CommonUtil.convertObjToStr(objPaddyPurchaseMasterTO.getTxtKiloGram()));
            addressRow.add(CommonUtil.convertObjToStr(objPaddyPurchaseMasterTO.getTxtAmount()));
            tblPurchaseList.insertRow(tblPurchaseList.getRowCount(),addressRow);
            addressRow = null;
        }else{
            tblPurchaseList.removeRow(row);
            addressRow.add(CommonUtil.convertObjToStr(objPaddyPurchaseMasterTO.getPurchaseSlNo()));
            addressRow.add(CommonUtil.convertObjToStr(objPaddyPurchaseMasterTO.getTxtProductCode()));
            addressRow.add(CommonUtil.convertObjToStr(objPaddyPurchaseMasterTO.getTxtKiloGram()));
            addressRow.add(CommonUtil.convertObjToStr(objPaddyPurchaseMasterTO.getTxtAmount()));
            tblPurchaseList.insertRow(row,addressRow);
            addressRow = null;
        }
    }
    
    private void insertData() throws Exception{
        setPurchaseTransData();
    }
    private void updateData() throws Exception{
        setPurchaseTransData();
    }
    private void deleteData() throws Exception{
        setPurchaseTransData();
        data.put("PURCHASEID",getPurchaseId());
    }
    
    private void setPurchaseTransData(){
        if(purchaseMap!=null && purchaseMap.size() > 0){
            data.put("PURCHASE",purchaseMap);
        }
        if(deletedPurchase != null && deletedPurchase.size() > 0){
            data.put("DELETEDPURCHASE",deletedPurchase);
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
    public void authorizeLocationMaster(HashMap singleAuthorizeMap) {
        try{
            singleAuthorizeMap.put("AUTH_DATA","AUTH_DATA");
            proxy.executeQuery(singleAuthorizeMap,map);
        }
        catch(Exception e){
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
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
        setTxtCnDNo("");
        setTxtKiloGram("");
        setTxtLocalityCode("");
        setTxtLocalityName("");
        setTxtName("");
        setTxtProductCode("");
        setTxtProductDesc("");
        setTxtRatePerKg("");
        setTxtTotalAmount("");
        setTxtlAcreage("");
        setTdtBillDate("");
        setTdtPurchaseDate("");
        resetPurchaseListTable();
//        ttNotifyObservers();
    }
    
    
    
    /**
     * Getter for property purchaseSlNo.
     * @return Value of property purchaseSlNo.
     */
    public java.lang.String getPurchaseSlNo() {
        return purchaseSlNo;
    }
    
    /**
     * Setter for property purchaseSlNo.
     * @param purchaseSlNo New value of property purchaseSlNo.
     */
    public void setPurchaseSlNo(java.lang.String purchaseSlNo) {
        this.purchaseSlNo = purchaseSlNo;
    }
    
    /**
     * Getter for property txtCnDNo.
     * @return Value of property txtCnDNo.
     */
    public java.lang.String getTxtCnDNo() {
        return txtCnDNo;
    }
    
    /**
     * Setter for property txtCnDNo.
     * @param txtCnDNo New value of property txtCnDNo.
     */
    public void setTxtCnDNo(java.lang.String txtCnDNo) {
        this.txtCnDNo = txtCnDNo;
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
     * Getter for property txtLocalityCode.
     * @return Value of property txtLocalityCode.
     */
    public java.lang.String getTxtLocalityCode() {
        return txtLocalityCode;
    }
    
    /**
     * Setter for property txtLocalityCode.
     * @param txtLocalityCode New value of property txtLocalityCode.
     */
    public void setTxtLocalityCode(java.lang.String txtLocalityCode) {
        this.txtLocalityCode = txtLocalityCode;
    }
    
    /**
     * Getter for property txtLocalityName.
     * @return Value of property txtLocalityName.
     */
    public java.lang.String getTxtLocalityName() {
        return txtLocalityName;
    }
    
    /**
     * Setter for property txtLocalityName.
     * @param txtLocalityName New value of property txtLocalityName.
     */
    public void setTxtLocalityName(java.lang.String txtLocalityName) {
        this.txtLocalityName = txtLocalityName;
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
     * Getter for property tdtPurchaseDate.
     * @return Value of property tdtPurchaseDate.
     */
    public java.lang.String getTdtPurchaseDate() {
        return tdtPurchaseDate;
    }
    
    /**
     * Setter for property tdtPurchaseDate.
     * @param tdtPurchaseDate New value of property tdtPurchaseDate.
     */
    public void setTdtPurchaseDate(java.lang.String tdtPurchaseDate) {
        this.tdtPurchaseDate = tdtPurchaseDate;
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
     * Getter for property newPurchase.
     * @return Value of property newPurchase.
     */
    public boolean isNewPurchase() {
        return newPurchase;
    }
    
    /**
     * Setter for property newPurchase.
     * @param newPurchase New value of property newPurchase.
     */
    public void setNewPurchase(boolean newPurchase) {
        this.newPurchase = newPurchase;
    }
    
    /**
     * Getter for property tblPurchaseList.
     * @return Value of property tblPurchaseList.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblPurchaseList() {
        return tblPurchaseList;
    }
    
    /**
     * Setter for property tblPurchaseList.
     * @param tblPurchaseList New value of property tblPurchaseList.
     */
    public void setTblPurchaseList(com.see.truetransact.clientutil.EnhancedTableModel tblPurchaseList) {
        this.tblPurchaseList = tblPurchaseList;
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
     * Getter for property purchaseId.
     * @return Value of property purchaseId.
     */
    public java.lang.String getPurchaseId() {
        return purchaseId;
    }
    
    /**
     * Setter for property purchaseId.
     * @param purchaseId New value of property purchaseId.
     */
    public void setPurchaseId(java.lang.String purchaseId) {
        this.purchaseId = purchaseId;
    }
    
}

