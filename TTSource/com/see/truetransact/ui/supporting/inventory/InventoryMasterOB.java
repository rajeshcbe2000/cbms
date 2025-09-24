/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * InventoryMasterOB.java
 *
 * Created on August 20, 2004, 2:19 PM
 */

package com.see.truetransact.ui.supporting.inventory;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.transferobject.supporting.inventory.InventoryMasterTO;

import com.see.truetransact.uicomponent.CObservable ;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 *
 * @author  rahul
 * @modified Sunil
 *  Added Edit Locking
 *  Added Multi Branch support
 */
public class InventoryMasterOB extends CObservable{
    private HashMap lookUpHash;
    private HashMap keyValue;
    private HashMap operationMap;
    private ArrayList key;
    private ArrayList value;
    
    private ProxyFactory proxy = null;
    
    private ComboBoxModel cbmItemType;
    private ComboBoxModel cbmUsage;
    
    private int actionType;
    private int result;
    
    private String lblInventoryID = "";
    private String lblTotalBooks = "";
    private String lblTotalLeaves = "";
    
    private static InventoryMasterOB inventoryMasterOB;
    private final static Logger log = Logger.getLogger(InventoryMasterUI.class);
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    static {
        try {
            log.info("In InventoryMasterOB Declaration");
            inventoryMasterOB = new InventoryMasterOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    public static InventoryMasterOB getInstance() {
        return inventoryMasterOB;
    }
    /** Creates a new instance of InventoryMasterOB */
    public InventoryMasterOB() throws Exception {
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
        operationMap.put(CommonConstants.JNDI, "InventoryMasterJNDI");
        operationMap.put(CommonConstants.HOME, "supporting.inventory.InventoryMasterHome");
        operationMap.put(CommonConstants.REMOTE, "supporting.inventory.InventoryMaster");
    }
    
    // To Fill the Combo boxes in the UI
    public void fillDropdown() throws Exception{
        log.info("In fillDropdown()");
        
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
        lookup_keys.add("INVENTORY.TYPE");
        lookup_keys.add("INVENTORY.USAGE");
        
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("INVENTORY.TYPE"));
        cbmItemType = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("INVENTORY.USAGE"));
        cbmUsage = new ComboBoxModel(key,value);
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
    
    ComboBoxModel getCbmItemType(){
        return cbmItemType;
    }
    
    ComboBoxModel getCbmUsage(){
        return cbmUsage;
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
    
    
    // To enter the Data into the Database...Called from doActionPerform()...
    public InventoryMasterTO setInventoryMaster() {
        log.info("In setInventoryMaster()");
        
        final InventoryMasterTO objInventoryMasterTO = new InventoryMasterTO();
        try{
            objInventoryMasterTO.setItemId(lblInventoryID);
            objInventoryMasterTO.setItemType((String)cbmItemType.getKeyForSelected());
            objInventoryMasterTO.setItemSubType((String)cbmUsage.getKeyForSelected());
            objInventoryMasterTO.setLeavesPerBook(CommonUtil.convertObjToDouble(txtLeavesPerBook));
            objInventoryMasterTO.setBooksReorderLevel(CommonUtil.convertObjToDouble(txtReOrderLevel));
            objInventoryMasterTO.setBooksDangerLevel(CommonUtil.convertObjToDouble(txtDangerLevel));
            objInventoryMasterTO.setInstrumentPrefix(txtInstrumentPrefix);
            objInventoryMasterTO.setRemarks(txtRemarks);
            
            objInventoryMasterTO.setAvailableBooks(CommonUtil.convertObjToDouble(lblTotalBooks));
            if(getActionType() == ClientConstants.ACTIONTYPE_NEW){
                objInventoryMasterTO.setCreatedBy(TrueTransactMain.USER_ID);
            }
            if(getActionType() == ClientConstants.ACTIONTYPE_EDIT
            || getActionType() == ClientConstants.ACTIONTYPE_DELETE ){
                
                objInventoryMasterTO.setStatusBy(TrueTransactMain.USER_ID);
            }
            
            
            //            objInventoryMasterTO.setCreatedDt (DateUtil.getDateMMDDYYYY (""));
            //            objInventoryMasterTO.setStatus ("");
            //            objInventoryMasterTO.setStatusBy(TrueTransactMain.USER_ID);
            //            objInventoryMasterTO.setStatusDt (DateUtil.getDateMMDDYYYY (""));
            objInventoryMasterTO.setAuthorizeStatus ("");
            objInventoryMasterTO.setAuthorizeBy ("");
            //            objInventoryMasterTO.setAuthorizeDt (DateUtil.getDateMMDDYYYY (""));
            objInventoryMasterTO.setBranchId(getSelectedBranchID());
            objInventoryMasterTO.setInitiatedBranch(ProxyParameters.BRANCH_ID);
            
            
        }catch(Exception e){
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objInventoryMasterTO;
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
        
        InventoryMasterTO objInventoryMasterTO = null;
        //Taking the Value of Prod_Id from each Table...
        // Here the first Row is selected...
        objInventoryMasterTO = (InventoryMasterTO) ((List) mapData.get("InventoryMasterTO")).get(0);
        setInventoryMasterTO(objInventoryMasterTO);
        ttNotifyObservers();
    }
    
    
    // To Enter the values in the UI fields, from the database...
    private void setInventoryMasterTO(InventoryMasterTO objInventoryMasterTO) throws Exception{
        log.info("In setInventoryMasterTO()");
        setLblInventoryID(CommonUtil.convertObjToStr(objInventoryMasterTO.getItemId()));
        setCboItemType((String) getCbmItemType().getDataForKey(CommonUtil.convertObjToStr(objInventoryMasterTO.getItemType())));
        setCboUsage((String) getCbmUsage().getDataForKey(CommonUtil.convertObjToStr(objInventoryMasterTO.getItemSubType())));
        setTxtLeavesPerBook(CommonUtil.convertObjToStr(objInventoryMasterTO.getLeavesPerBook()));
        setTxtReOrderLevel(CommonUtil.convertObjToStr(objInventoryMasterTO.getBooksReorderLevel()));
        setTxtDangerLevel(CommonUtil.convertObjToStr(objInventoryMasterTO.getBooksDangerLevel()));
        setTxtRemarks(CommonUtil.convertObjToStr(objInventoryMasterTO.getRemarks()));
        
        setLblTotalBooks(CommonUtil.convertObjToStr(objInventoryMasterTO.getAvailableBooks()));
        
        int books = CommonUtil.convertObjToInt(objInventoryMasterTO.getAvailableBooks());
        int leaves = CommonUtil.convertObjToInt(objInventoryMasterTO.getLeavesPerBook());
        int totalLeaves = books * leaves;
        
        setLblTotalLeaves(String.valueOf(totalLeaves));
        
        //        setTxtCreatedBy(objInventoryMasterTO.getCreatedBy());
        //        setTxtCreatedDt(DateUtil.getStringDate(objInventoryMasterTO.getCreatedDt()));
        //        setTxtStatus(objInventoryMasterTO.getStatus());
        //        setTxtStatusBy(objInventoryMasterTO.getStatusBy());
        //        setTxtStatusDt(DateUtil.getStringDate(objInventoryMasterTO.getStatusDt()));
        //        setTxtAuthorizeStatus(objInventoryMasterTO.getAuthorizeStatus());
        //        setTxtAuthorizeBy(objInventoryMasterTO.getAuthorizeBy());
        //        setTxtAuthorizeDt(DateUtil.getStringDate(objInventoryMasterTO.getAuthorizeDt()));
        //        setTxtBranchId(objInventoryMasterTO.getBranchId());
        setTxtInstrumentPrefix(CommonUtil.convertObjToStr(objInventoryMasterTO.getInstrumentPrefix()));
        setStatusBy(CommonUtil.convertObjToStr(objInventoryMasterTO.getStatusBy()));
        setAuthorizeStatus(objInventoryMasterTO.getAuthorizeStatus());
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
                if( getCommand() != null){
                    doActionPerform();
                }
            }
            else
                log.info("Action Type Not Defined");
        } catch (Exception e) {
            System.out.println("Error In doAction()");
            parseException.logException(e,true);
            setResult(ClientConstants.ACTIONTYPE_FAILED);
        }
    }
    
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        log.info("In doActionPerform()");
        final HashMap data = new HashMap();
        data.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
        data.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
        final InventoryMasterTO objInventoryMasterTO = setInventoryMaster();
        objInventoryMasterTO.setCommand(getCommand());
        data.put("InventoryMasterTO",objInventoryMasterTO);
        HashMap proxyResultMap = proxy.execute(data,operationMap);
        setProxyReturnMap(proxyResultMap);
        setResult(actionType);
//        resetForm();
    }
    
    // To reset all the fields in the UI
    public void resetForm(){
        log.info("In resetForm()");
        setCboItemType("");
        setCboUsage("");
        setTxtLeavesPerBook("");
        setTxtReOrderLevel("");
        setTxtDangerLevel("");
        setTxtInstrumentPrefix("");
        setTxtRemarks("");
        
        /*
         * To Reset the Lables...
         */
        setLblInventoryID("");
        setLblTotalBooks("");
        setLblTotalLeaves("");
        
        ttNotifyObservers();
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
    
    
    private String cboItemType = "";
    private String cboUsage = "";
    private String txtLeavesPerBook = "";
    private String txtReOrderLevel = "";
    private String txtDangerLevel = "";
    private String txtInstrumentPrefix = "";
    private String txtRemarks = "";
    
    // Setter method for txtLeavesPerBook
    void setTxtRemarks(String txtRemarks){
        this.txtRemarks = txtRemarks;
        setChanged();
    }
    // Getter method for txtLeavesPerBook
    String getTxtRemarks(){
        return this.txtRemarks;
    }
    
    // Setter method for cboItemType
    void setCboItemType(String cboItemType){
        this.cboItemType = cboItemType;
        setChanged();
    }
    // Getter method for cboItemType
    String getCboItemType(){
        return this.cboItemType;
    }
    
    // Setter method for cboUsage
    void setCboUsage(String cboUsage){
        this.cboUsage = cboUsage;
        setChanged();
    }
    // Getter method for cboUsage
    String getCboUsage(){
        return this.cboUsage;
    }
    
    // Setter method for txtLeavesPerBook
    void setTxtLeavesPerBook(String txtLeavesPerBook){
        this.txtLeavesPerBook = txtLeavesPerBook;
        setChanged();
    }
    // Getter method for txtLeavesPerBook
    String getTxtLeavesPerBook(){
        return this.txtLeavesPerBook;
    }
    
    // Setter method for txtReOrderLevel
    void setTxtReOrderLevel(String txtReOrderLevel){
        this.txtReOrderLevel = txtReOrderLevel;
        setChanged();
    }
    // Getter method for txtReOrderLevel
    String getTxtReOrderLevel(){
        return this.txtReOrderLevel;
    }
    
    // Setter method for txtDangerLevel
    void setTxtDangerLevel(String txtDangerLevel){
        this.txtDangerLevel = txtDangerLevel;
        setChanged();
    }
    // Getter method for txtDangerLevel
    String getTxtDangerLevel(){
        return this.txtDangerLevel;
    }
    
    // Setter method for txtInstrumentPrefix
    void setTxtInstrumentPrefix(String txtInstrumentPrefix){
        this.txtInstrumentPrefix = txtInstrumentPrefix;
        setChanged();
    }
    // Getter method for txtInstrumentPrefix
    String getTxtInstrumentPrefix(){
        return this.txtInstrumentPrefix;
    }
    
    // Setter method for lblInventoryID
    void setLblInventoryID(String lblInventoryID){
        this.lblInventoryID = lblInventoryID;
        setChanged();
    }
    // Getter method for lblInventoryID
    String getLblInventoryID(){
        return this.lblInventoryID;
    }
    
    // Setter method for lblTotalBooks
    void setLblTotalBooks(String lblTotalBooks){
        this.lblTotalBooks = lblTotalBooks;
        setChanged();
    }
    // Getter method for lblTotalBooks
    String getLblTotalBooks(){
        return this.lblTotalBooks;
    }
    
    // Setter method for lblTotalLeaves
    void setLblTotalLeaves(String lblTotalLeaves){
        this.lblTotalLeaves = lblTotalLeaves;
        setChanged();
    }
    // Getter method for lblTotalLeaves
    String getLblTotalLeaves(){
        return this.lblTotalLeaves;
    }
    
    public HashMap getBookQuanIn(){
        HashMap result = new HashMap();
        try{
            final HashMap transDataMap = new HashMap();
            transDataMap.put("ITEM ID", getLblInventoryID());
            System.out.println("transDataMap :"+transDataMap);
            
            final List resultTrans = ClientUtil.executeQuery("getBookQuanTransIn", transDataMap);
            if(resultTrans!=null){
               result = (HashMap)resultTrans.get(0);
            }
        }catch(Exception e){
            e.printStackTrace();
            parseException.logException(e,true);
        }
        return result;
    }
    
    public HashMap getBookQuanOut(){
        HashMap result = new HashMap();
        try{
            final HashMap transDataMap = new HashMap();
            transDataMap.put("ITEM ID", getLblInventoryID());
            System.out.println("transDataMap :"+transDataMap);
            
            final List resultTrans = ClientUtil.executeQuery("getBookQuanTransOut", transDataMap);
            if(resultTrans!=null){
               result = (HashMap)resultTrans.get(0);
            }
        }catch(Exception e){
            e.printStackTrace();
            parseException.logException(e,true);
        }
        return result;
    }
    
    
}
