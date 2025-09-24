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

package com.see.truetransact.ui.remittance.remitstoppayment;

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
import com.see.truetransact.transferobject.remittance.remitstoppayment.RemitStopPaymentTO;
import com.see.truetransact.uicomponent.CObservable;
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

public class RemitStopPaymentOB extends CObservable{
    private HashMap lookUpHash;
    private HashMap keyValue;
    private HashMap operationMap;
    private ArrayList key;
    private ArrayList value;
    
    private ComboBoxModel cbmProdId;
    
    //__ Lables...
    private String lblStopIdValue = "";
    private String lblDDDateValue = "";
    private String lblPayeeNameValue = "";
    private String lblAmountValue = "";
    private String lblDDStopDateValue = "";
    private String lblRevokeDateVal = "";
    private String authStatus = "";
    
    private String stopStatus = "";
    
    
    private int actionType;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger log = Logger.getLogger(RemitStopPaymentUI.class);
    
    private ProxyFactory proxy = null;
    
    private final String SINGLE = "S";
    private final String BULK = "B";
    
    private static RemitStopPaymentOB remitStopPaymentOB;
    static {
        try {
            log.info("In RemitStopPaymentOB Declaration");
            remitStopPaymentOB = new RemitStopPaymentOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    public static RemitStopPaymentOB getInstance() {
        return remitStopPaymentOB;
    }
    
    /** Creates a new instance of InwardClearingOB */
    public RemitStopPaymentOB() throws Exception {
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
        operationMap.put(CommonConstants.JNDI, "RemitStopPaymentJNDI");
        operationMap.put(CommonConstants.HOME, "remittance.remitstoppayment.RemitStopPaymentHome");
        operationMap.put(CommonConstants.REMOTE, "remittance.remitstoppayment.RemitStopPayment");
    }
    
    // To Fill the Combo boxes in the UI
    public void fillDropdown() throws Exception{
        System.out.println("In fillDropdown()");
        
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,"RemitStop.getProdId");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmProdId = new ComboBoxModel(key,value);
    }
    
    // Get the value from the Hash Map depending on the Value of Key...
    private void getKeyValue(HashMap keyValue)  throws Exception{
        log.info("In getKeyValue()");
        
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    
    public RemitStopPaymentTO setRemitStopPayment() {
        log.info("In setRemitStopPayment()");
        
        final RemitStopPaymentTO objRemitStopPaymentTO = new RemitStopPaymentTO();
        try{
            objRemitStopPaymentTO.setStopPaymentId(getLblStopIdValue());
            
            objRemitStopPaymentTO.setProdId((String)cbmProdId.getKeyForSelected());
            if (getRdoDDLeaf_Single() == true) {
                objRemitStopPaymentTO.setDdLeafType(SINGLE);
            } else if (getRdoDDLeaf_Bulk() == true) {
                objRemitStopPaymentTO.setDdLeafType(BULK);
            }
            
            objRemitStopPaymentTO.setStartDdNo1(getTxtStartDDNo1());
            objRemitStopPaymentTO.setStartDdNo2(getTxtStartDDNo2());
            objRemitStopPaymentTO.setEndDdNo1(getTxtEndDDNo1());
            objRemitStopPaymentTO.setEndDdNo2(getTxtEndDDNo2());
            objRemitStopPaymentTO.setStartVariableNo(getTxtStartVariableNo());
            objRemitStopPaymentTO.setEndVariableNo(getTxtEndVariableNo());
            objRemitStopPaymentTO.setRemarks(getTxtReason());
            objRemitStopPaymentTO.setRevokeRemark(getTxtRevokeRemark());
            objRemitStopPaymentTO.setStopStatus(CommonConstants.STOPPED);
            objRemitStopPaymentTO.setInitiatedBranch(ProxyParameters.BRANCH_ID);
            objRemitStopPaymentTO.setBranchId(getSelectedBranchID());
        }catch(Exception e){
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objRemitStopPaymentTO;
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
        RemitStopPaymentTO objRemitStopPaymentTO = null;
        //Taking the Value of Prod_Id from each Table...
        objRemitStopPaymentTO = (RemitStopPaymentTO) ((List) mapData.get("RemitStopPaymentTO")).get(0);
        setRemitStopPaymentTO(objRemitStopPaymentTO);
        //__ To get the Data for the Lables...
        setDDStopDetails();
        
        ttNotifyObservers();
    }
    
    // To Enter the values in the UI fields, from the database...
    private void setRemitStopPaymentTO(RemitStopPaymentTO objRemitStopPaymentTO) throws Exception{
        log.info("In setRemitStopPaymentTO()");
        
        setLblStopIdValue(CommonUtil.convertObjToStr(objRemitStopPaymentTO.getStopPaymentId()));
        setCboProdId((String) getCbmProdId().getDataForKey(CommonUtil.convertObjToStr(objRemitStopPaymentTO.getProdId())));
        
        if (CommonUtil.convertObjToStr(objRemitStopPaymentTO.getDdLeafType()).equals(SINGLE)) setRdoDDLeaf_Single(true);
        else setRdoDDLeaf_Bulk(true);
        
        setTxtStartDDNo1(CommonUtil.convertObjToStr(objRemitStopPaymentTO.getStartDdNo1()));
        setTxtStartDDNo2(CommonUtil.convertObjToStr(objRemitStopPaymentTO.getStartDdNo2()));
        setTxtEndDDNo1(CommonUtil.convertObjToStr(objRemitStopPaymentTO.getEndDdNo1()));
        setTxtEndDDNo2(CommonUtil.convertObjToStr(objRemitStopPaymentTO.getEndDdNo2()));
        setTxtStartVariableNo(CommonUtil.convertObjToStr(objRemitStopPaymentTO.getStartVariableNo()));
        setTxtEndVariableNo(CommonUtil.convertObjToStr(objRemitStopPaymentTO.getEndVariableNo()));
        setTxtReason(CommonUtil.convertObjToStr(objRemitStopPaymentTO.getRemarks()));
        setTxtRevokeRemark(CommonUtil.convertObjToStr(objRemitStopPaymentTO.getRevokeRemark()));
        setLblDDStopDateValue(DateUtil.getStringDate(objRemitStopPaymentTO.getStopStatusDt()));
        setLblRevokeDateVal(DateUtil.getStringDate(objRemitStopPaymentTO.getRevokeStatusDt()));
        setStatusBy(objRemitStopPaymentTO.getStatusBy());
        setAuthorizeStatus(objRemitStopPaymentTO.getAuthorizeStatus());
        //__ To set the Authrization Status...
        setAuthStatus(CommonUtil.convertObjToStr(objRemitStopPaymentTO.getAuthorizeStatus()));
        
        setStopStatus(CommonUtil.convertObjToStr(objRemitStopPaymentTO.getStopStatus()));
        
        //        setTxtCreatedBy(objRemitStopPaymentTO.getCreatedBy());
        //        setTxtCreatedDt(DateUtil.getStringDate(objRemitStopPaymentTO.getCreatedDt()));
        //        setTxtStatus(objRemitStopPaymentTO.getStatus());
        //        setTxtStatusBy(objRemitStopPaymentTO.getStatusBy());
        //        setTxtStatusDt(DateUtil.getStringDate(objRemitStopPaymentTO.getStatusDt()));
        //        setTxtAuthorizeStatus(objRemitStopPaymentTO.getAuthorizeStatus());
        //        setTxtAuthorizeDt(DateUtil.getStringDate(objRemitStopPaymentTO.getAuthorizeDt()));
        //        setTxtAuthorizeBy(objRemitStopPaymentTO.getAuthorizeBy());
        //        setTxtBranchId(objRemitStopPaymentTO.getBranchId());
        //       setTxtStopStatus(objRemitStopPaymentTO.getStopStatus());
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
        final RemitStopPaymentTO objRemitStopPaymentTO = setRemitStopPayment();
        objRemitStopPaymentTO.setCommand(getCommand());
        data.put("RemitStopPaymentTO",objRemitStopPaymentTO);
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
        setTxtStartDDNo1("");
        setTxtStartDDNo2("");
        setTxtEndDDNo1("");
        setTxtEndDDNo2("");
        setTxtStartVariableNo("");
        setTxtEndVariableNo("");
        setTxtReason("");
        setTxtRevokeRemark("");
        //__ Lables...
        setLblStopIdValue("");
        setLblDDDateValue("");
        setLblAmountValue("");
        setLblPayeeNameValue("");
        setLblDDStopDateValue("");
        setLblRevokeDateVal("");
    }
    
    private String cboProdId = "";
    private boolean rdoDDLeaf_Single = false;
    private boolean rdoDDLeaf_Bulk = false;
    private String txtStartDDNo1 = "";
    private String txtStartDDNo2 = "";
    private String txtEndDDNo1 = "";
    private String txtEndDDNo2 = "";
    private String txtStartVariableNo = "";
    private String txtEndVariableNo = "";
    private String txtReason = "";
    private String txtRevokeRemark = "";
    
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
    
    // Setter method for txtStartDDNo1
    void setTxtStartDDNo1(String txtStartDDNo1){
        this.txtStartDDNo1 = txtStartDDNo1;
        setChanged();
    }
    // Getter method for txtStartDDNo1
    String getTxtStartDDNo1(){
        return this.txtStartDDNo1;
    }
    
    // Setter method for txtStartDDNo2
    void setTxtStartDDNo2(String txtStartDDNo2){
        this.txtStartDDNo2 = txtStartDDNo2;
        setChanged();
    }
    // Getter method for txtStartDDNo2
    String getTxtStartDDNo2(){
        return this.txtStartDDNo2;
    }
    
    // Setter method for txtEndDDNo1
    void setTxtEndDDNo1(String txtEndDDNo1){
        this.txtEndDDNo1 = txtEndDDNo1;
        setChanged();
    }
    // Getter method for txtEndDDNo1
    String getTxtEndDDNo1(){
        return this.txtEndDDNo1;
    }
    
    // Setter method for txtEndDDNo2
    void setTxtEndDDNo2(String txtEndDDNo2){
        this.txtEndDDNo2 = txtEndDDNo2;
        setChanged();
    }
    // Getter method for txtEndDDNo2
    String getTxtEndDDNo2(){
        return this.txtEndDDNo2;
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
    
    // Setter method for lblStopIdValue
    void setLblStopIdValue(String lblStopIdValue){
        this.lblStopIdValue = lblStopIdValue;
        setChanged();
    }
    // Getter method for lblStopIdValue
    String getLblStopIdValue(){
        return this.lblStopIdValue;
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
    
    // Setter method for lblDDStopDateValue
    void setLblDDStopDateValue(String lblDDStopDateValue){
        this.lblDDStopDateValue = lblDDStopDateValue;
        setChanged();
    }
    // Getter method for lblDDStopDateValue
    String getLblDDStopDateValue(){
        return this.lblDDStopDateValue;
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
    
   
    
    public void setDDStopDetails(){
        try {
            final HashMap stopDDMap = new HashMap();
            stopDDMap.put("INSTRUMENTNO1",getTxtStartDDNo1());
            stopDDMap.put("INSTRUMENTNO2",getTxtStartDDNo2());
            stopDDMap.put("VARIABLENO",getTxtStartVariableNo());
            System.out.println("stopDDMap: " + stopDDMap);
            
            final List resultList = ClientUtil.executeQuery("getStopDDDetails", stopDDMap);
            if(resultList.size() > 0){
                
                final HashMap resultMap = (HashMap)resultList.get(0);
                System.out.println("resultMap: " + resultMap);
                setLblDDDateValue(DateUtil.getStringDate((Date)resultMap.get("BATCH_DT")));
                setLblPayeeNameValue(CommonUtil.convertObjToStr(resultMap.get("FAVOURING")));
                setLblAmountValue(CommonUtil.convertObjToStr(resultMap.get("AMOUNT")));
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void paymentRevoke(String remark, Date curDate){
        HashMap resultMap = new HashMap();
        try {
            final HashMap stopPayment = new HashMap();
            stopPayment.put("STOPPAYMENTID",getLblStopIdValue());
            stopPayment.put("STOPSTATUS",CommonConstants.REVOKED);
            stopPayment.put("REVOKE_REMARK", remark);
            stopPayment.put("REVOKE_STATUS_DT", curDate);
            System.out.println("stopPayment: " +stopPayment);
            ClientUtil.execute("DDStop.setPaymentRevoke", stopPayment);
        }catch(Exception e){
        }
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
     * Getter for property txtRevokeRemark.
     * @return Value of property txtRevokeRemark.
     */
    public java.lang.String getTxtRevokeRemark() {
        return txtRevokeRemark;
    }
    
    /**
     * Setter for property txtRevokeRemark.
     * @param txtRevokeRemark New value of property txtRevokeRemark.
     */
    public void setTxtRevokeRemark(java.lang.String txtRevokeRemark) {
        this.txtRevokeRemark = txtRevokeRemark;
    }
    
    /**
     * Getter for property lblRevokeDateVal.
     * @return Value of property lblRevokeDateVal.
     */
    public java.lang.String getLblRevokeDateVal() {
        return lblRevokeDateVal;
    }
    
    /**
     * Setter for property lblRevokeDateVal.
     * @param lblRevokeDateVal New value of property lblRevokeDateVal.
     */
    public void setLblRevokeDateVal(java.lang.String lblRevokeDateVal) {
        this.lblRevokeDateVal = lblRevokeDateVal;
    }
    
}