/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * InventoryDetailsOB.java
 *
 * Created on August 24, 2004, 10:49 AM
 */

package com.see.truetransact.ui.supporting.inventory;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.transferobject.supporting.inventory.InventoryDetailsTO;

import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.see.truetransact.uicomponent.CObservable;
/**
 *
 * @author  rahul
 */
public class InventoryDetailsOB extends CObservable {
    private HashMap lookUpHash;
    private HashMap keyValue;
    private HashMap operationMap;
    private ArrayList key;
    private ArrayList value;
    
    private ProxyFactory proxy = null;
    
    private ComboBoxModel cbmTransType;
    
    private int actionType;
    private int result;
    
    private String lblTransID = "";
    private String lblTransDate = "";
    private String lblProdType = "";
    private String lblAcctNo = "";
    private String lblItemSubType = "";
    private String lblLeavesNO = "";
    private String lblAvailableBooksDesc = "";
    private String lblInstrumentPrefix = "";
    
   
    private HashMap authorizeMap;
    
    private static InventoryDetailsOB inventoryDetailsOB;
    private final static Logger log = Logger.getLogger(InventoryDetailsUI.class);
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    
    static {
        try {
            log.info("In InventoryDetailsOB Declaration");
            inventoryDetailsOB = new InventoryDetailsOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    public static InventoryDetailsOB getInstance() {
        return inventoryDetailsOB;
    }
    
    
    /** Creates a new instance of InventoryDetailsOB */
    public InventoryDetailsOB() throws Exception {
        initianSetup();
    }
    
    private void initianSetup() throws Exception{
        log.info("In initianSetup()");
        setOperationMap();
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        fillDropdown();// To Fill all the Combo Boxes
    }
    
    // Set the value of JNDI and the Session Bean...
    private void setOperationMap() throws Exception{
        log.info("In setOperationMap()");
        
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "InventoryDetailsJNDI");
        operationMap.put(CommonConstants.HOME, "supporting.inventory.InventoryDetailsHome");
        operationMap.put(CommonConstants.REMOTE, "supporting.inventory.InventoryDetails");
    }
    
    // To Fill the Combo boxes in the UI
    public void fillDropdown() throws Exception{
        log.info("In fillDropdown()");
        
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
        lookup_keys.add("INVENTORY.TRAN.TYPE");
        
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("INVENTORY.TRAN.TYPE"));
        cbmTransType = new ComboBoxModel(key,value);
    }
    
    // Get the value from the Hash Map depending on the Value of Key...
    private void getKeyValue(HashMap keyValue)  throws Exception{
        log.info("In getKeyValue()");
        
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    // Returns the Current Value of Action type...
    public int getActionType(){
        return actionType;
    }
    
    // Sets the value of the Action Type to the poperation we want to execute...
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }
    
    ComboBoxModel getCbmTransType(){
        return cbmTransType;
    }
    
    
    // To set and change the Status of the lable STATUS
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    public int getResult(){
        return this.result;
    }
    
    // To set the Value of the lblStatus...
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    public String getLblStatus(){
        return lblStatus;
    }
    
    //To reset the Value of lblStatus after each save action...
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }
    
    public void ttNotifyObservers(){
        setChanged();
        notifyObservers();
    }
    
    /*
     * Setter and Getter methods for the Authorization.
     */
    public void setAuthorizeMap(HashMap authMap) {
        authorizeMap = authMap;
    }
    
    public HashMap getAuthorizeMap() {
        return authorizeMap;
    }
    
    // To enter the Data into the Database...Called from doActionPerform()...
    public InventoryDetailsTO setInventoryDetails() {
        log.info("In setInventoryMaster()");
        
        final InventoryDetailsTO objInventoryDetailsTO = new InventoryDetailsTO();
        try{
            objInventoryDetailsTO.setTransId(lblTransID);
            objInventoryDetailsTO.setItemId(txtItemID);
            //            objInventoryDetailsTO.setTransDt(DateUtil.getDateMMDDYYYY(""));
            objInventoryDetailsTO.setTransType((String)cbmTransType.getKeyForSelected());
            objInventoryDetailsTO.setBookQuantity(CommonUtil.convertObjToDouble(txtBookQuantity));
            objInventoryDetailsTO.setBookSlnoFrom(CommonUtil.convertObjToDouble(txtBookFrom));
            objInventoryDetailsTO.setBookSlnoTo(CommonUtil.convertObjToDouble(txtBookTo));
            objInventoryDetailsTO.setInstPrefix(CommonUtil.convertObjToStr(txtInstrumentPrefix));
            objInventoryDetailsTO.setLeavesSlnoFrom(CommonUtil.convertObjToDouble(txtChequeFrom));
            objInventoryDetailsTO.setLeavesSlnoTo(CommonUtil.convertObjToDouble(txtChequeTo));
            objInventoryDetailsTO.setInitiatedBranch(ProxyParameters.BRANCH_ID);
            if(getActionType() == ClientConstants.ACTIONTYPE_NEW){
                objInventoryDetailsTO.setCreatedBy(TrueTransactMain.USER_ID);
            }
            if(getActionType() == ClientConstants.ACTIONTYPE_EDIT
            || getActionType() == ClientConstants.ACTIONTYPE_DELETE ){
                
                objInventoryDetailsTO.setStatusBy(TrueTransactMain.USER_ID);
            }
            //            objInventoryDetailsTO.setProdType("");
            //            objInventoryDetailsTO.setActNum("");
            //            objInventoryDetailsTO.setStatus("");
            //            objInventoryDetailsTO.setStatusDt(DateUtil.getDateMMDDYYYY(""));
            //            objInventoryDetailsTO.setAuthorizeStatus("");
            //            objInventoryDetailsTO.setAuthorizeBy("");
            //            objInventoryDetailsTO.setAuthorizeDt(DateUtil.getDateMMDDYYYY(""));
        }catch(Exception e){
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objInventoryDetailsTO;
    }
    
    //Do display the Data from the Database, in UI
    public void populateData(HashMap whereMap) {
        log.info("In populateData()");
        
        final HashMap mapData;
        try {
            mapData = proxy.executeQuery(whereMap, operationMap);
            populateOB(mapData);
        } catch( Exception e ) {
            log.info("Error In populateData()");
            parseException.logException(e,true);
        }
    }
    
    private void populateOB(HashMap mapData) throws Exception{
        log.info("In populateOB()");
        
        InventoryDetailsTO objInventoryDetailsTO = null;
        //Taking the Value of Prod_Id from each Table...
        // Here the first Row is selected...
        objInventoryDetailsTO = (InventoryDetailsTO) ((List) mapData.get("InventoryDetailsTO")).get(0);
        setInventoryDetailsTO(objInventoryDetailsTO);
        ttNotifyObservers();
    }
    
    
    // To Enter the values in the UI fields, from the database...
    private void setInventoryDetailsTO(InventoryDetailsTO objInventoryDetailsTO) throws Exception{
        log.info("In setInventoryMasterTO()");
        setLblTransID(CommonUtil.convertObjToStr(objInventoryDetailsTO.getTransId()));
        setTxtItemID(CommonUtil.convertObjToStr(objInventoryDetailsTO.getItemId()));
        setLblTransDate(DateUtil.getStringDate(objInventoryDetailsTO.getTransDt()));
        setCboTransType((String) getCbmTransType().getDataForKey(CommonUtil.convertObjToStr(objInventoryDetailsTO.getTransType())));
        setTxtBookQuantity(CommonUtil.convertObjToStr(objInventoryDetailsTO.getBookQuantity()));
        setTxtBookFrom(CommonUtil.convertObjToStr(objInventoryDetailsTO.getBookSlnoFrom()));
        setTxtBookTo(CommonUtil.convertObjToStr(objInventoryDetailsTO.getBookSlnoTo()));
        setTxtChequeFrom(CommonUtil.convertObjToStr(objInventoryDetailsTO.getLeavesSlnoFrom()));
        setTxtChequeTo(CommonUtil.convertObjToStr(objInventoryDetailsTO.getLeavesSlnoTo()));
        setLblProdType(CommonUtil.convertObjToStr(objInventoryDetailsTO.getProdType()));
        setLblAcctNo(CommonUtil.convertObjToStr(objInventoryDetailsTO.getActNum()));
        setTxtInstrumentPrefix(CommonUtil.convertObjToStr(objInventoryDetailsTO.getInstPrefix()));
        setStatusBy(CommonUtil.convertObjToStr(objInventoryDetailsTO.getStatusBy()));
        setAuthorizeStatus(objInventoryDetailsTO.getAuthorizeStatus());
        //        setTxtCreatedBy(objInventoryDetailsTO.getCreatedBy());
        //        setTxtCreatedDt(DateUtil.getStringDate(objInventoryDetailsTO.getCreatedDt()));
        //        setTxtStatus(objInventoryDetailsTO.getStatus());
        //        setTxtStatusBy(objInventoryDetailsTO.getStatusBy());
        //        setTxtStatusDt(DateUtil.getStringDate(objInventoryDetailsTO.getStatusDt()));
        //        setTxtAuthorizeStatus(objInventoryDetailsTO.getAuthorizeStatus());
        //        setTxtAuthorizeBy(objInventoryDetailsTO.getAuthorizeBy());
        //        setTxtAuthorizeDt(DateUtil.getStringDate(objInventoryDetailsTO.getAuthorizeDt()));
        
    }
    
    /**
     * To perform Appropriate operation... Insert, Update, Delete...
     */
    public void doAction() {
        log.info("In doAction()");
        try {
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null || getAuthorizeMap() != null){
                    doActionPerform();
                }
            }
            else
                log.info("Action Type Not Defined");
        } catch (Exception e) {
            log.info("Error In doAction()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        log.info("In doActionPerform()");
        final HashMap data = new HashMap();
        if(getAuthorizeMap() == null){
            final InventoryDetailsTO objInventoryDetailsTO = setInventoryDetails();
            objInventoryDetailsTO.setCommand(getCommand());
            data.put("InventoryDetailsTO",objInventoryDetailsTO);
        }
        data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
        data.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
        data.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        
        HashMap proxyResultMap = proxy.execute(data,operationMap);
        setProxyReturnMap(proxyResultMap);
        setResult(actionType);
//        resetForm();
    }
    
    // to decide which action Should be performed...
    private String getCommand() throws Exception{
        log.info("In getCommand()");
        
        String command = null;
        switch (actionType) {
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
    
    // To reset all the fields in the UI
    public void resetForm(){
        log.info("In resetForm()");
        setTxtItemID("");
        setCboTransType("");
        setTxtBookQuantity("");
        setTxtBookFrom("");
        setTxtBookTo("");
        setTxtChequeFrom("");
        setTxtChequeTo("");
        /*
         * To Reset the Lables...
         */
        setLblTransID("");
        setLblTransDate("");
        setLblProdType("");
        setLblAcctNo("");
        setLblAvailableBooksDesc("");
        setLblItemSubType("");
        setLblLeavesNO("");
        setTxtInstrumentPrefix("");
        this.setAuthorizeMap(null);
        
        ttNotifyObservers();
    }
    
    private String txtItemID = "";
    private String cboTransType = "";
    private String txtBookQuantity = "";
    private String txtInstrumentPrefix = "";
    private String txtBookFrom = "";
    private String txtBookTo = "";
    private String txtChequeFrom = "";
    private String txtChequeTo = "";
    
    // Setter method for txtItemID
    void setTxtItemID(String txtItemID){
        this.txtItemID = txtItemID;
        setChanged();
    }
    // Getter method for txtItemID
    String getTxtItemID(){
        return this.txtItemID;
    }
    
    // Setter method for cboTransType
    void setCboTransType(String cboTransType){
        this.cboTransType = cboTransType;
        setChanged();
    }
    // Getter method for cboTransType
    String getCboTransType(){
        return this.cboTransType;
    }
    
    // Setter method for txtBookQuantity
    void setTxtBookQuantity(String txtBookQuantity){
        this.txtBookQuantity = txtBookQuantity;
        setChanged();
    }
    // Getter method for txtBookQuantity
    String getTxtBookQuantity(){
        return this.txtBookQuantity;
    }
    
    // Setter method for txtBookFrom
    void setTxtBookFrom(String txtBookFrom){
        this.txtBookFrom = txtBookFrom;
        setChanged();
    }
    // Getter method for txtBookFrom
    String getTxtBookFrom(){
        return this.txtBookFrom;
    }
    
    // Setter method for txtBookTo
    void setTxtBookTo(String txtBookTo){
        this.txtBookTo = txtBookTo;
        setChanged();
    }
    // Getter method for txtBookTo
    String getTxtBookTo(){
        return this.txtBookTo;
    }
    
    // Setter method for txtChequeFrom
    void setTxtChequeFrom(String txtChequeFrom){
        this.txtChequeFrom = txtChequeFrom;
        setChanged();
    }
    // Getter method for txtChequeFrom
    String getTxtChequeFrom(){
        return this.txtChequeFrom;
    }
    
    // Setter method for txtChequeTo
    void setTxtChequeTo(String txtChequeTo){
        this.txtChequeTo = txtChequeTo;
        setChanged();
    }
    // Getter method for txtChequeTo
    String getTxtChequeTo(){
        return this.txtChequeTo;
    }
    
    // Setter method for lblTransID
    void setLblTransID(String lblTransID){
        this.lblTransID = lblTransID;
        setChanged();
    }
    // Getter method for lblTransID
    String getLblTransID(){
        return this.lblTransID;
    }
    
    // Setter method for lblTransDate
    void setLblTransDate(String lblTransDate){
        this.lblTransDate = lblTransDate;
        setChanged();
    }
    // Getter method for lblTransDate
    String getLblTransDate(){
        return this.lblTransDate;
    }
    
    // Setter method for lblProdType
    void setLblProdType(String lblProdType){
        this.lblProdType = lblProdType;
        setChanged();
    }
    // Getter method for lblProdType
    String getLblProdType(){
        return this.lblProdType;
    }
    
    // Setter method for lblAcctNo
    void setLblAcctNo(String lblAcctNo){
        this.lblAcctNo = lblAcctNo;
        setChanged();
    }
    // Getter method for lblAcctNo
    String getLblAcctNo(){
        return this.lblAcctNo;
    }
    
    public int getLeavesNo(String itemId){
        int leaves = 0;
        try {
            HashMap leavesMap = new HashMap();
            leavesMap.put("ITEM ID", itemId);
            final List resultList = ClientUtil.executeQuery("getInventoryMasterLeaveNo", leavesMap);
            if(resultList.size() > 0){
                final HashMap resultMap = (HashMap)resultList.get(0);
                leaves = CommonUtil.convertObjToInt(resultMap.get("LEAVES_PER_BOOK"));
            }
        }catch(Exception e){
            System.out.println("Error in setActData()");
        }
        return leaves;
    }
    
    //__ To validate the Instrument Nos...
    public boolean validateInstruments(){
        HashMap dataMap = new HashMap();
        boolean isIssued = false;
        try {
            HashMap leavesMap = new HashMap();
            dataMap.put("ITEM_ID", CommonUtil.convertObjToStr(txtItemID));
            dataMap.put("TRANS_TYPE", CommonUtil.convertObjToStr(cbmTransType.getKeyForSelected()));
            dataMap.put("SERIES_FROM", getTxtBookFrom());
            dataMap.put("SERIES_TO", getTxtBookTo());
            dataMap.put("INSTRU_FROM", CommonUtil.convertObjToInt(getTxtChequeFrom()));
            dataMap.put("INSTRU_TO", CommonUtil.convertObjToInt(getTxtChequeTo()));
            dataMap.put("INSTRUMENT_PREFIX", getTxtInstrumentPrefix());
            System.out.println("dataMap: " + dataMap);
            final List resultList = ClientUtil.executeQuery("vaidateInstrumentNo", dataMap);
            if(resultList.size() > 0){
                final HashMap resultMap = (HashMap)resultList.get(0);
                if(CommonUtil.convertObjToStr(resultMap.get("ITEM_ID")).length() > 0){
                    isIssued = true;
                }
            }
        }catch(Exception e){
            System.out.println("Error in getLeavesNo()");
        }
        return isIssued;
    }
     public boolean getAvailableBooks(){
        HashMap dataMap = new HashMap();
        boolean isAvailable = false;
        try {
            HashMap leavesMap = new HashMap();
            dataMap.put("ITEM_ID", CommonUtil.convertObjToStr(txtItemID));
            dataMap.put("BOOK_QUANTITY", getTxtBookQuantity());
            System.out.println("dataMap: " + dataMap);
            final List lstData = ClientUtil.executeQuery("getAvailableBooks", dataMap);
            if(lstData.size() > 0){
//                final HashMap mapData = (HashMap)lstData.get(0);
//                if(CommonUtil.convertObjToStr(mapData.get("ITEM_ID")).length() > 0){
                    isAvailable = false;
//                }
            }else{
                isAvailable = true;
            }
        }catch(Exception e){
            System.out.println("Error in getLeavesNo()");
        }
        return isAvailable;
     }
    
    /**
     * Getter for property lblItemSubType.
     * @return Value of property lblItemSubType.
     */
    public java.lang.String getLblItemSubType() {
        return lblItemSubType;
    }
    
    /**
     * Setter for property lblItemSubType.
     * @param lblItemSubType New value of property lblItemSubType.
     */
    public void setLblItemSubType(java.lang.String lblItemSubType) {
        this.lblItemSubType = lblItemSubType;
    }
    
    /**
     * Getter for property lblLeavesNO.
     * @return Value of property lblLeavesNO.
     */
    public java.lang.String getLblLeavesNO() {
        return lblLeavesNO;
    }
    
    /**
     * Setter for property lblLeavesNO.
     * @param lblLeavesNO New value of property lblLeavesNO.
     */
    public void setLblLeavesNO(java.lang.String lblLeavesNO) {
        this.lblLeavesNO = lblLeavesNO;
    }
    
    /**
     * Getter for property lblAvailableBooksDesc.
     * @return Value of property lblAvailableBooksDesc.
     */
    public java.lang.String getLblAvailableBooksDesc() {
        return lblAvailableBooksDesc;
    }
    
    /**
     * Setter for property lblAvailableBooksDesc.
     * @param lblAvailableBooksDesc New value of property lblAvailableBooksDesc.
     */
    public void setLblAvailableBooksDesc(java.lang.String lblAvailableBooksDesc) {
        this.lblAvailableBooksDesc = lblAvailableBooksDesc;
    }
    
    /**
     * Getter for property lblInstrumentPrefix.
     * @return Value of property lblInstrumentPrefix.
     */
    public java.lang.String getLblInstrumentPrefix() {
        return lblInstrumentPrefix;
    }
    
    /**
     * Setter for property lblInstrumentPrefix.
     * @param lblInstrumentPrefix New value of property lblInstrumentPrefix.
     */
    public void setLblInstrumentPrefix(java.lang.String lblInstrumentPrefix) {
        this.lblInstrumentPrefix = lblInstrumentPrefix;
    }
    
    /**
     * Getter for property txtInstrumentPrefix.
     * @return Value of property txtInstrumentPrefix.
     */
    public java.lang.String getTxtInstrumentPrefix() {
        return txtInstrumentPrefix;
    }
    
    /**
     * Setter for property txtInstrumentPrefix.
     * @param txtInstrumentPrefix New value of property txtInstrumentPrefix.
     */
    public void setTxtInstrumentPrefix(java.lang.String txtInstrumentPrefix) {
        this.txtInstrumentPrefix = txtInstrumentPrefix;
    }
    
}
