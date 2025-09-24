/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemitStopPaymentOB.java
 *
 * Created on Tue Jan 25 09:29:03 IST 2005
 */

package com.see.truetransact.ui.supporting.InventoryMovement;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.transferobject.supporting.InventoryMovement.InventoryMovementTO;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 *
 * @author
 */

public class InventoryMovementOB extends CObservable{
    private HashMap lookUpHash;
    private HashMap keyValue;
    private HashMap operationMap;
    private ArrayList key;
    private ArrayList value;
    
    private ComboBoxModel cbmProdId;
    
    //__ Lables...
    private String lblMissingIdValue = "";
    private String lblDDDateValue = "";
    private String lblPayeeNameValue = "";
    private String lblAmountValue = "";
//    private String lblMissingDateValue = "";
    private String authStatus = "";
    
    private String stopStatus = "";
     private String cboReason;
    private ComboBoxModel cbmReason;
    private String tdtDate = "";
    private String remarks;
    private String statusDt = "";
    
    
    private int actionType;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger log = Logger.getLogger(InventoryMovementUI.class);
    
    private ProxyFactory proxy = null;
    
    private final String SINGLE = "S";
    private final String BULK = "B";
    Date curDate = null;
    
    private static InventoryMovementOB inventoryMovementOB;
    static {
        try {
            log.info("In InventoryMovementOB Declaration");
             inventoryMovementOB = new InventoryMovementOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    public static InventoryMovementOB getInstance() {
        return inventoryMovementOB;
    }
    
    /** Creates a new instance of InwardClearingOB */
    public InventoryMovementOB() throws Exception {
        curDate = ClientUtil.getCurrentDate();
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
        operationMap.put(CommonConstants.JNDI, "InventoryMovementJNDI");
        operationMap.put(CommonConstants.HOME, "supporting.InventoryMovement.InventoryMovementHome");
        operationMap.put(CommonConstants.REMOTE, "supporting.InventoryMovement.InventoryMovement");
    }
    
    // To Fill the Combo boxes in the UI
    public void fillDropdown() throws Exception{
        log.info("In fillDropdown()");
         lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("INVENTORY.USAGE");
        lookup_keys.add("INVENTORYMOVEMENT_REASON");
       lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
       getKeyValue((HashMap)keyValue.get("INVENTORY.USAGE"));
        cbmProdId = new ComboBoxModel(key,value);
        
//         keyValue = ClientUtil.populateLookupData(lookUpHash);
       getKeyValue((HashMap)keyValue.get("INVENTORYMOVEMENT_REASON"));
        cbmReason = new ComboBoxModel(key,value);
    }
    
    // Get the value from the Hash Map depending on the Value of Key...
    private void getKeyValue(HashMap keyValue)  throws Exception{
        log.info("In getKeyValue()");
        
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    
    public InventoryMovementTO setInventoryMovement() {
        log.info("In setInventoryMovement()");
        
        final InventoryMovementTO objInventoryMovementTO = new InventoryMovementTO();
        try{
            objInventoryMovementTO.setId(getLblMissingIdValue());
            
            objInventoryMovementTO.setProdId((String)cbmProdId.getKeyForSelected());
            if (getRdoDDLeaf_Single() == true) {
                objInventoryMovementTO.setDdLeafType(SINGLE);
            } else if (getRdoDDLeaf_Bulk() == true) {
                objInventoryMovementTO.setDdLeafType(BULK);
            }
            
            objInventoryMovementTO.setStartNo1(getTxtStartNo1());
            objInventoryMovementTO.setStartNo2(getTxtStartNo2());
            objInventoryMovementTO.setEndNo1(getTxtEndNo1());
            objInventoryMovementTO.setEndNo2(getTxtEndNo2());
            objInventoryMovementTO.setRemarks(getRemarks());
            objInventoryMovementTO.setInitiatedBranch(ProxyParameters.BRANCH_ID);
            objInventoryMovementTO.setBranchId(getSelectedBranchID());
            objInventoryMovementTO.setReason((String) cbmReason.getKeyForSelected());
//            objInventoryMovementTO.setMissingDt(DateUtil.getDateMMDDYYYY(tdtDate));
//            objInventoryMovementTO.setStatusDt(DateUtil.getDateMMDDYYYY(statusDt));
            Date tdDt = DateUtil.getDateMMDDYYYY(tdtDate);
            if(tdDt != null){
            Date tdDate = (Date)curDate.clone();
            tdDate.setDate(tdDt.getDate());
            tdDate.setMonth(tdDt.getMonth());
            tdDate.setYear(tdDt.getYear());
            objInventoryMovementTO.setMissingDt(tdDate);
            }else{
                objInventoryMovementTO.setMissingDt(DateUtil.getDateMMDDYYYY(tdtDate));
            }
            
            Date StDt = DateUtil.getDateMMDDYYYY(statusDt);
            if(StDt != null){
            Date stDate = (Date)curDate.clone();
            stDate.setDate(StDt.getDate());
            stDate.setMonth(StDt.getMonth());
            stDate.setYear(StDt.getYear());
            objInventoryMovementTO.setStatusDt(stDate);
            }else{
                objInventoryMovementTO.setStatusDt(DateUtil.getDateMMDDYYYY(statusDt));
            }
            objInventoryMovementTO.setStatusBy(TrueTransactMain.USER_ID);
        }catch(Exception e){
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objInventoryMovementTO;
    }
    
    //Do display the Data from the Database, in UI
    public void populateData(HashMap whereMap) {
        log.info("In populateData()");
        final HashMap mapData;
        try {
            mapData = proxy.executeQuery(whereMap, operationMap);
            populateOB(mapData);
        } catch( Exception e ) {
            System.out.println("Error In populateData()");
            parseException.logException(e,true);
        }
    }
    
    private void populateOB(HashMap mapData) throws Exception{
        InventoryMovementTO objInventoryMovementTO = null;
        //Taking the Value of Prod_Id from each Table...
        objInventoryMovementTO = (InventoryMovementTO) ((List) mapData.get("InventoryMovementTO")).get(0);
        setInventoryMovementTO(objInventoryMovementTO);
        //__ To get the Data for the Lables...
//        setDDStopDetails();
        
        ttNotifyObservers();
    }
    
    // To Enter the values in the UI fields, from the database...
    private void setInventoryMovementTO(InventoryMovementTO objInventoryMovementTO) throws Exception{
        log.info("In setRemitStopPaymentTO()");
        
        setLblMissingIdValue(CommonUtil.convertObjToStr(objInventoryMovementTO.getId()));
        setCboProdId((String) getCbmProdId().getDataForKey(CommonUtil.convertObjToStr(objInventoryMovementTO.getProdId())));
        
        if (CommonUtil.convertObjToStr(objInventoryMovementTO.getDdLeafType()).equals(SINGLE)) setRdoDDLeaf_Single(true);
        else setRdoDDLeaf_Bulk(true);
        setCboReason((String) getCbmReason().getDataForKey(CommonUtil.convertObjToStr(objInventoryMovementTO.getReason())));
        setTxtStartNo1(CommonUtil.convertObjToStr(objInventoryMovementTO.getStartNo1()));
        setTxtStartNo2(CommonUtil.convertObjToStr(objInventoryMovementTO.getStartNo2()));
        setTxtEndNo1(CommonUtil.convertObjToStr(objInventoryMovementTO.getEndNo1()));
        setTxtEndNo2(CommonUtil.convertObjToStr(objInventoryMovementTO.getEndNo2()));
        setRemarks(CommonUtil.convertObjToStr(objInventoryMovementTO.getRemarks()));
        setAuthorizeStatus(objInventoryMovementTO.getAuthorizeStatus());
        setStatusBy(objInventoryMovementTO.getStatusBy());
        setTdtDate(CommonUtil.convertObjToStr(objInventoryMovementTO.getMissingDt()));
        setStatusDt(DateUtil.getStringDate(objInventoryMovementTO.getStatusDt()));
        //__ To set the Authrization Status...
        setAuthStatus(CommonUtil.convertObjToStr(objInventoryMovementTO.getAuthorizeStatus()));
        
      
    }
    
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
                log.info("Action Type Not Defined In setChequeBookTO()");
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
        final InventoryMovementTO objInventoryMovementTO = setInventoryMovement();
        objInventoryMovementTO.setCommand(getCommand());
        data.put("InventoryMovementTO",objInventoryMovementTO);
        data.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
        data.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        HashMap proxyResultMap = proxy.execute(data,operationMap);
        setProxyReturnMap(proxyResultMap);
        setResult(actionType);
        resetForm();
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
    
    // Returns the Current Value of Action type...
    public int getActionType(){
        return actionType;
    }
    
    // Sets the value of the Action Type to the poperation we want to execute...
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
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
    
    public void resetForm(){
        setCboProdId("");
        setRdoDDLeaf_Single(false);
        setRdoDDLeaf_Bulk(false);
        setTxtStartNo1("");
        setTxtStartNo2("");
        setTxtEndNo1("");
        setTxtEndNo2("");
        setTxtStartVariableNo("");
        setTxtEndVariableNo("");
        setTxtReason("");
        //__ Lables...
        setLblMissingIdValue("");
        setLblDDDateValue("");
        setLblAmountValue("");
        setLblPayeeNameValue("");
        setCboReason("");
        setRemarks("");
        setTdtDate("");
        setLblStatus("");
    }
    
    private String cboProdId = "";
    private boolean rdoDDLeaf_Single = false;
    private boolean rdoDDLeaf_Bulk = false;
    private String txtStartNo1 = "";
    private String txtStartNo2 = "";
    private String txtEndNo1 = "";
    private String txtEndNo2 = "";
    private String txtStartVariableNo = "";
    private String txtEndVariableNo = "";
    private String txtReason = "";
    
    ComboBoxModel getCbmProdId(){
        return cbmProdId;
    }
    
    // Setter method for cboProdId
    void setCboProdId(String cboProdId){
        this.cboProdId = cboProdId;
        setChanged();
    }
    // Getter method for cboProdId
    String getCboProdId(){
        return this.cboProdId;
    }
    
    // Setter method for rdoDDLeaf_Single
    void setRdoDDLeaf_Single(boolean rdoDDLeaf_Single){
        this.rdoDDLeaf_Single = rdoDDLeaf_Single;
        setChanged();
    }
    // Getter method for rdoDDLeaf_Single
    boolean getRdoDDLeaf_Single(){
        return this.rdoDDLeaf_Single;
    }
    
    // Setter method for rdoDDLeaf_Bulk
    void setRdoDDLeaf_Bulk(boolean rdoDDLeaf_Bulk){
        this.rdoDDLeaf_Bulk = rdoDDLeaf_Bulk;
        setChanged();
    }
    // Getter method for rdoDDLeaf_Bulk
    boolean getRdoDDLeaf_Bulk(){
        return this.rdoDDLeaf_Bulk;
    }
    
  

    
    
    // Setter method for txtStartVariableNo
    void setTxtStartVariableNo(String txtStartVariableNo){
        this.txtStartVariableNo = txtStartVariableNo;
        setChanged();
    }
    // Getter method for txtStartVariableNo
    String getTxtStartVariableNo(){
        return this.txtStartVariableNo;
    }
    
    // Setter method for txtEndVariableNo
    void setTxtEndVariableNo(String txtEndVariableNo){
        this.txtEndVariableNo = txtEndVariableNo;
        setChanged();
    }
    // Getter method for txtEndVariableNo
    String getTxtEndVariableNo(){
        return this.txtEndVariableNo;
    }
    
    // Setter method for txtReason
    void setTxtReason(String txtReason){
        this.txtReason = txtReason;
        setChanged();
    }
    // Getter method for txtReason
    String getTxtReason(){
        return this.txtReason;
    }
    
  
    
    // Setter method for lblDDDateValue
    void setLblDDDateValue(String lblDDDateValue){
        this.lblDDDateValue = lblDDDateValue;
        setChanged();
    }
    // Getter method for lblDDDateValue
    String getLblDDDateValue(){
        return this.lblDDDateValue;
    }
    
    // Setter method for lblPayeeNameValue
    void setLblPayeeNameValue(String lblPayeeNameValue){
        this.lblPayeeNameValue = lblPayeeNameValue;
        setChanged();
    }
    // Getter method for lblPayeeNameValue
    String getLblPayeeNameValue(){
        return this.lblPayeeNameValue;
    }
    
   
    
    // Setter method for lblAmountValue
    void setLblAmountValue(String lblAmountValue){
        this.lblAmountValue = lblAmountValue;
        setChanged();
    }
    // Getter method for lblAmountValue
    String getLblAmountValue(){
        return this.lblAmountValue;
    }
    
    
    // Setter method for authStatus
    void setAuthStatus(String authStatus){
        this.authStatus = authStatus;
        setChanged();
    }
    // Getter method for authStatus
    String getAuthStatus(){
        return this.authStatus;
    }
    
    /**
     * Getter for property stopStatus.
     * @return Value of property stopStatus.
     */
    public java.lang.String getStopStatus() {
        return stopStatus;
    }    
    
    /**
     * Setter for property stopStatus.
     * @param stopStatus New value of property stopStatus.
     */
    public void setStopStatus(java.lang.String stopStatus) {
        this.stopStatus = stopStatus;
    }
    
    /**
     * Getter for property lblMissingIdValue.
     * @return Value of property lblMissingIdValue.
     */
    public java.lang.String getLblMissingIdValue() {
        return lblMissingIdValue;
    }
    
    /**
     * Setter for property lblMissingIdValue.
     * @param lblMissingIdValue New value of property lblMissingIdValue.
     */
    public void setLblMissingIdValue(java.lang.String lblMissingIdValue) {
        this.lblMissingIdValue = lblMissingIdValue;
    }
    
    /**
     * Getter for property txtStartNo1.
     * @return Value of property txtStartNo1.
     */
    public java.lang.String getTxtStartNo1() {
        return txtStartNo1;
    }
    
    /**
     * Setter for property txtStartNo1.
     * @param txtStartNo1 New value of property txtStartNo1.
     */
    public void setTxtStartNo1(java.lang.String txtStartNo1) {
        this.txtStartNo1 = txtStartNo1;
    }
    
    /**
     * Getter for property txtStartNo2.
     * @return Value of property txtStartNo2.
     */
    public java.lang.String getTxtStartNo2() {
        return txtStartNo2;
    }
    
    /**
     * Setter for property txtStartNo2.
     * @param txtStartNo2 New value of property txtStartNo2.
     */
    public void setTxtStartNo2(java.lang.String txtStartNo2) {
        this.txtStartNo2 = txtStartNo2;
    }
    
    /**
     * Getter for property txtEndNo1.
     * @return Value of property txtEndNo1.
     */
    public java.lang.String getTxtEndNo1() {
        return txtEndNo1;
    }
    
    /**
     * Setter for property txtEndNo1.
     * @param txtEndNo1 New value of property txtEndNo1.
     */
    public void setTxtEndNo1(java.lang.String txtEndNo1) {
        this.txtEndNo1 = txtEndNo1;
    }
    
    /**
     * Getter for property txtEndNo2.
     * @return Value of property txtEndNo2.
     */
    public java.lang.String getTxtEndNo2() {
        return txtEndNo2;
    }
    
    /**
     * Setter for property txtEndNo2.
     * @param txtEndNo2 New value of property txtEndNo2.
     */
    public void setTxtEndNo2(java.lang.String txtEndNo2) {
        this.txtEndNo2 = txtEndNo2;
    }
    
    /**
     * Getter for property cboReason.
     * @return Value of property cboReason.
     */
    public java.lang.String getCboReason() {
        return cboReason;
    }    
    
    /**
     * Setter for property cboReason.
     * @param cboReason New value of property cboReason.
     */
    public void setCboReason(java.lang.String cboReason) {
        this.cboReason = cboReason;
    }    
   
    /**
     * Getter for property cbmReason.
     * @return Value of property cbmReason.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmReason() {
        return cbmReason;
    }    
    
    /**
     * Setter for property cbmReason.
     * @param cbmReason New value of property cbmReason.
     */
    public void setCbmReason(com.see.truetransact.clientutil.ComboBoxModel cbmReason) {
        this.cbmReason = cbmReason;
    }
    
    /**
     * Getter for property tdtDate.
     * @return Value of property tdtDate.
     */
    public java.lang.String getTdtDate() {
        return tdtDate;
    }
    
    /**
     * Setter for property tdtDate.
     * @param tdtDate New value of property tdtDate.
     */
    public void setTdtDate(java.lang.String tdtDate) {
        this.tdtDate = tdtDate;
    }
    
    /**
     * Getter for property remarks.
     * @return Value of property remarks.
     */
    public java.lang.String getRemarks() {
        return remarks;
    }
    
    /**
     * Setter for property remarks.
     * @param remarks New value of property remarks.
     */
    public void setRemarks(java.lang.String remarks) {
        this.remarks = remarks;
    }
    
    /**
     * Getter for property statusDt.
     * @return Value of property statusDt.
     */
    public java.lang.String getStatusDt() {
        return statusDt;
    }
    
    /**
     * Setter for property statusDt.
     * @param statusDt New value of property statusDt.
     */
    public void setStatusDt(java.lang.String statusDt) {
        this.statusDt = statusDt;
    }
    
}