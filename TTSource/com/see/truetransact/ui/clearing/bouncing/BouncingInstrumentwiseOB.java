/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BouncingInstrumentwiseOB.java
 *
 * Created on April 7, 2004, 12:41 PM
 */

package com.see.truetransact.ui.clearing.bouncing;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.ui.clearing.bouncing.BouncingInstrumentwiseRB;
import com.see.truetransact.transferobject.clearing.bouncing.BouncingInstrumentwiseTO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CObservable;

import com.see.truetransact.ui.TrueTransactMain;

import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 *
 * @author  Lohith R.
 */
public class BouncingInstrumentwiseOB extends CObservable {
    private static BouncingInstrumentwiseOB objBouncingInstrumentwiseOB; // singleton object
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    private ComboBoxModel cbmBouncingType;
    //    private ComboBoxModel cbmReasonforBouncing;
    private ComboBoxModel cbmClearingType;
    
    private String cboBouncingType = "";
    private String txtClearingSerialNo = "";
    private String txtInwardScheduleNo = "";
    //    private String cboReasonforBouncing = "";
    private String txtReasonforBouncing = "";
    private boolean chkPresentAgain = false;
    private String cboClearingType = "";
    private String dateClearingDate = "";
    private String bouncingId = "";
    
    private String lblAccountHead = "";
    private String lblAccountNumber = "";
    private String lblAmount = "";
    private String lblBankCode = "";
    private String lblBranchCode = "";
    private String lblClearingDate = "";
    private String lblClearingType = "";
    private String lblInstrumentDate = "";
    private String lblInstrumentNumber = "";
    private String lblInstrumentType = "";
    private String lblName = "";
    
    private int actionType;
    private int result;
    private ArrayList key;
    private ArrayList value;
    private ProxyFactory proxy;
    private HashMap operationMap;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private static Date currDt = null;
    private HashMap authorizeMap;
    
    static {
        try {
            currDt = ClientUtil.getCurrentDate();
            objBouncingInstrumentwiseOB = new BouncingInstrumentwiseOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** Creates a new instance of BouncingInstrumentwiseOB */
    public BouncingInstrumentwiseOB() throws Exception {
        proxy = ProxyFactory.createProxy();
        setOperationMap();
        fillDropdown();
    }
    
    /** Sets the HashMap to required JNDI, Home and Remote */
    private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "BouncingInstrumentwiseJNDI");
        operationMap.put(CommonConstants.HOME, "clearing.bouncing.BouncingInstrumentwiseHome");
        operationMap.put(CommonConstants.REMOTE, "clearing.bouncing.BouncingInstrumentwise");
    }
    
    /** Method to get the combo box values */
    private void fillDropdown() throws Exception{
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
        lookup_keys.add("INWARD.BOUNCING_TYPE");
        //        lookup_keys.add("INWARD.BOUNCING_REASON");
        //        lookup_keys.add("INWARD.CLEARING_TYPE");
        
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        
        getKeyValue((HashMap)keyValue.get("INWARD.BOUNCING_TYPE"));
        cbmBouncingType = new ComboBoxModel(key,value);
        
        //        getKeyValue((HashMap)keyValue.get("INWARD.BOUNCTING_REASON"));
        //        cbmReasonforBouncing = new ComboBoxModel(key,value);
        
        
        //__ Data for the ClearingType Combo-Box...
        lookUpHash.put(CommonConstants.MAP_NAME,"InwardClearing.getInwardClearingType");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, TrueTransactMain.BRANCH_ID);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmClearingType = new ComboBoxModel(key,value);
        
        //        getKeyValue((HashMap)keyValue.get("INWARD.CLEARING_TYPE"));
        //        cbmClearingType = new ComboBoxModel(key,value);
        makeNull();
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    private void makeNull(){
        key = null;
        value = null;
    }
    
    /** Creates a new instance of BouncingInstrumentwiseOB */
    public static BouncingInstrumentwiseOB getInstance() {
        return objBouncingInstrumentwiseOB;
    }
    
    // Setter method for bouncingId
    void setbouncingId(String bouncingId){
        this.bouncingId = bouncingId;
        setChanged();
    }
    // Getter method for bouncingId
    String getbouncingId(){
        return this.bouncingId;
    }
    
    // Setter method for cboBouncingType
    void setCboBouncingType(String cboBouncingType){
        this.cboBouncingType = cboBouncingType;
        setChanged();
    }
    // Getter method for cboBouncingType
    String getCboBouncingType(){
        return this.cboBouncingType;
    }
    // Setter method for setCbmBouncingType
    void setCbmBouncingType(ComboBoxModel cbmBouncingType){
        this.cbmBouncingType = cbmBouncingType;
        setChanged();
    }
    // Getter method for getCbmBouncingType
    ComboBoxModel getCbmBouncingType(){
        return cbmBouncingType;
    }
    
    // Setter method for txtClearingSerialNo
    void setTxtClearingSerialNo(String txtClearingSerialNo){
        this.txtClearingSerialNo = txtClearingSerialNo;
        setChanged();
    }
    // Getter method for txtClearingSerialNo
    String getTxtClearingSerialNo(){
        return this.txtClearingSerialNo;
    }
    
    // Setter method for txtInwardScheduleNo
    void setTxtInwardScheduleNo(String txtInwardScheduleNo){
        this.txtInwardScheduleNo = txtInwardScheduleNo;
        setChanged();
    }
    // Getter method for txtInwardScheduleNo
    String getTxtInwardScheduleNo(){
        return this.txtInwardScheduleNo;
    }
    
    // Setter method for txtInwardScheduleNo
    void setTxtReasonforBouncing(String txtReasonforBouncing){
        this.txtReasonforBouncing = txtReasonforBouncing;
        setChanged();
    }
    // Getter method for txtInwardScheduleNo
    String getTxtReasonforBouncing(){
        return this.txtReasonforBouncing;
    }
    
    
    //    // Setter method for cboReasonforBouncing
    //    void setCboReasonforBouncing(String cboReasonforBouncing){
    //        this.cboReasonforBouncing = cboReasonforBouncing;
    //        setChanged();
    //    }
    //    // Getter method for cboReasonforBouncing
    //    String getCboReasonforBouncing(){
    //        return this.cboReasonforBouncing;
    //    }
    // Setter method for setCbmReasonforBouncing
    //    void setCbmReasonforBouncing(ComboBoxModel cbmReasonforBouncing){
    //        this.cbmReasonforBouncing = cbmReasonforBouncing;
    //        setChanged();
    //    }// Getter method for getCbmReasonforBouncing
    //    ComboBoxModel getCbmReasonforBouncing(){
    //        return cbmReasonforBouncing;
    //    }
    
    // Setter method for chkPresentAgain
    void setChkPresentAgain(boolean chkPresentAgain){
        this.chkPresentAgain = chkPresentAgain;
        setChanged();
    }
    // Getter method for chkPresentAgain
    boolean getChkPresentAgain(){
        return this.chkPresentAgain;
    }
    
    // Setter method for cboClearingType
    void setCboClearingType(String cboClearingType){
        this.cboClearingType = cboClearingType;
        setChanged();
    }
    // Getter method for cboClearingType
    String getCboClearingType(){
        return this.cboClearingType;
    }
    // Setter method for setCbmClearingType
    void setCbmClearingType(ComboBoxModel cbmClearingType){
        this.cbmClearingType = cbmClearingType;
        setChanged();
    }
    // Getter method for getCbmClearingType
    ComboBoxModel getCbmClearingType(){
        return cbmClearingType;
    }
    
    // Setter method for dateClearingDate
    void setdateClearingDate(String dateClearingDate){
        this.dateClearingDate = dateClearingDate;
        setChanged();
    }
    // Getter method for dateClearingDate
    String getdateClearingDate(){
        return this.dateClearingDate;
    }
    
    // Setter method for lblAccountHead
    void setLblAccountHead(String lblAccountHead){
        this.lblAccountHead = lblAccountHead;
        setChanged();
    }
    // Getter method for lblAccountHead
    String getLblAccountHead(){
        return this.lblAccountHead;
    }
    
    // Setter method for lblAccountNumber
    void setLblAccountNumber(String lblAccountNumber){
        this.lblAccountNumber = lblAccountNumber;
        setChanged();
    }
    // Getter method for lblAccountNumber
    String getLblAccountNumber(){
        return this.lblAccountNumber;
    }
    
    // Setter method for lblAmount
    void setLblAmount(String lblAmount){
        this.lblAmount = lblAmount;
        setChanged();
    }
    // Getter method for lblAmount
    String getLblAmount(){
        return this.lblAmount;
    }
    
    // Setter method for lblBankCode
    void setLblBankCode(String lblBankCode){
        this.lblBankCode = lblBankCode;
        setChanged();
    }
    // Getter method for lblBankCode
    String getLblBankCode(){
        return this.lblBankCode;
    }
    
    // Setter method for lblBranchCode
    void setLblBranchCode(String lblBranchCode){
        this.lblBranchCode = lblBranchCode;
        setChanged();
    }
    // Getter method for lblBranchCode
    String getLblBranchCode(){
        return this.lblBranchCode;
    }
    
    // Setter method for lblClearingDate
    void setLblClearingDate(String lblClearingDate){
        this.lblClearingDate = lblClearingDate;
        setChanged();
    }
    // Getter method for lblAccountHead
    String getLblClearingDate(){
        return this.lblClearingDate;
    }
    
    // Setter method for lblClearingType
    void setLblClearingType(String lblClearingType){
        this.lblClearingType = lblClearingType;
        setChanged();
    }
    // Getter method for lblClearingType
    String getLblClearingType(){
        return this.lblClearingType;
    }
    
    // Setter method for lblInstrumentDate
    void setLblInstrumentDate(String lblInstrumentDate){
        this.lblInstrumentDate = lblInstrumentDate;
        setChanged();
    }
    // Getter method for lblInstrumentDate
    String getLblInstrumentDate(){
        return this.lblInstrumentDate;
    }
    
    // Setter method for lblInstrumentNumber
    void setLblInstrumentNumber(String lblInstrumentNumber){
        this.lblInstrumentNumber = lblInstrumentNumber;
        setChanged();
    }
    // Getter method for lblInstrumentNumber
    String getLblInstrumentNumber(){
        return this.lblInstrumentNumber;
    }
    
    // Setter method for lblInstrumentType
    void setLblInstrumentType(String lblInstrumentType){
        this.lblInstrumentType = lblInstrumentType;
        setChanged();
    }
    // Getter method for lblInstrumentType
    String getLblInstrumentType(){
        return this.lblInstrumentType;
    }
    
    // Setter method for lblName
    void setLblName(String lblName){
        this.lblName = lblName;
        setChanged();
    }
    // Getter method for lblName
    String getLblName(){
        return this.lblName;
    }
    
    /** Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     *
     */
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    public String getLblStatus(){
        return lblStatus;
    }
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        notifyObservers();
    }
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        notifyObservers();
    }
    public int getResult(){
        return result;
    }
    public void setActionType(int action) {
        actionType = action;
        setChanged();
    }
    
    public int getActionType(){
        return actionType;
    }
    
    /** Sets the labels according to the Serial Number */
    public void setLabels(){
        final String spaceString = " ";
        StringBuffer instrumentNumberStringBuff = new StringBuffer();
        StringBuffer accountHeadStringBuff = new StringBuffer();
        StringBuffer accountNumberStringBuff = new StringBuffer();
        
        final HashMap clearingSerialNoMap = getQuery(getTxtClearingSerialNo(), "Bouncing_Instrumentwise.getSerialNumber");
        final HashMap accountHd = getQuery(CommonUtil.convertObjToStr(clearingSerialNoMap.get("PROD_ID")), "Bouncing_Instrumentwise.ViewAccountHead");
        final HashMap accountNum = getQuery(CommonUtil.convertObjToStr(clearingSerialNoMap.get("ACCT_NO")), "Bouncing_Instrumentwise.ViewAccountNumber");
        final String instrumentDateString = DateUtil.getStringDate((Date)clearingSerialNoMap.get("INSTRUMENT_DT"));
        final String clearingDateString = DateUtil.getStringDate((Date)clearingSerialNoMap.get("CLEARING_DT"));
        
        instrumentNumberStringBuff.append(clearingSerialNoMap.get("INSTRUMENT_NO1"));
        instrumentNumberStringBuff.append(spaceString);
        instrumentNumberStringBuff.append(clearingSerialNoMap.get("INSTRUMENT_NO2"));
        
        accountHeadStringBuff.append(accountHd.get("AC_HD_ID"));
        accountHeadStringBuff.append(spaceString);
        accountHeadStringBuff.append(accountHd.get("AC_HD_DESC"));
        
        accountNumberStringBuff.append(accountNum.get("ACCT_NO"));
        accountNumberStringBuff.append(spaceString);
        accountNumberStringBuff.append(spaceString);
        accountNumberStringBuff.append(accountNum.get("FNAME"));
        accountNumberStringBuff.append(spaceString);
        accountNumberStringBuff.append(accountNum.get("LNAME"));
        accountNumberStringBuff.append(spaceString);
        accountNumberStringBuff.append(accountNum.get("MNAME"));
        
        setLblClearingType(CommonUtil.convertObjToStr(clearingSerialNoMap.get("CLEARING_TYPE")));
        setLblInstrumentDate(instrumentDateString);
        setLblClearingDate(clearingDateString);
        setLblInstrumentType(CommonUtil.convertObjToStr(clearingSerialNoMap.get("INSTRUMENT_TYPE")));
        setLblAmount(CommonUtil.convertObjToStr(clearingSerialNoMap.get("AMOUNT")));
        setLblName(CommonUtil.convertObjToStr(clearingSerialNoMap.get("PAYEE_NAME")));
        setLblBankCode(CommonUtil.convertObjToStr(clearingSerialNoMap.get("BANK_CODE")));
        setLblBranchCode(CommonUtil.convertObjToStr(clearingSerialNoMap.get("BRANCH_CODE")));
        setLblInstrumentNumber(CommonUtil.convertObjToStr(instrumentNumberStringBuff));
        setLblAccountHead(CommonUtil.convertObjToStr(accountHeadStringBuff));
        setLblAccountNumber(CommonUtil.convertObjToStr(accountNumberStringBuff));
        notifyObservers();
    }
    
    /** Executes the query using Where Codition and Map Name
     * @param whereString Where Codition is passed as string
     * @param mapNameString Map Name is passed as string
     * @return returns Hash Map
     */
    private HashMap getQuery(String whereString, String mapNameString){
        final HashMap whereMap = new HashMap();
        HashMap mapID = new HashMap();
        whereMap.put("WHERE",whereString);
        whereMap.put("INITIATED_BRANCH",TrueTransactMain.BRANCH_ID);
        whereMap.put("TRANS_DT",currDt.clone());
        mapID.put(CommonConstants.MAP_WHERE, whereMap);
        System.out.println("@@@@@@@@@@@@"+whereMap);
        final List resultList = ClientUtil.executeQuery(mapNameString, mapID);
        final HashMap resultMap = (HashMap)resultList.get(0);
        return resultMap;
    }
    
    /** To perform the appropriate operation */
    public void doAction() {
        try {
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null || getAuthorizeMap() != null){
                    doActionPerform();
                }
                else{
                    final BouncingInstrumentwiseRB objBouncingInstrumentwiseRB = new BouncingInstrumentwiseRB();
                    throw new TTException(objBouncingInstrumentwiseRB.getString("TOCommandError"));
                }
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
        }
    }
    
    /** To get the value of action performed */
    private String getCommand() throws Exception{
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
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        final HashMap data = new HashMap();
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        
        if(getAuthorizeMap() == null){
            final BouncingInstrumentwiseTO objBouncingInstrumentwiseTO = setBouncingInstrumentwiseData();
            objBouncingInstrumentwiseTO.setCommand(getCommand());
            data.put("BouncingInstrumentwiseTO",objBouncingInstrumentwiseTO);
        }
        data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
        
        HashMap proxyResultMap = proxy.execute(data, operationMap);
        setResult(actionType);
        actionType = ClientConstants.ACTIONTYPE_NEW;
    }
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    
    /* To set common data in the Transfer Object*/
    public BouncingInstrumentwiseTO setBouncingInstrumentwiseData() {
        final BouncingInstrumentwiseTO objBouncingInstrumentwiseTO = new BouncingInstrumentwiseTO();
        try{
            objBouncingInstrumentwiseTO.setBouncingId(bouncingId);
            objBouncingInstrumentwiseTO.setBouncingType((String)cbmBouncingType.getKeyForSelected());
            objBouncingInstrumentwiseTO.setInwardId(txtClearingSerialNo);
            objBouncingInstrumentwiseTO.setInwardScheduleNo(txtInwardScheduleNo);
            //            objBouncingInstrumentwiseTO.setBouncingReason((String)cbmReasonforBouncing.getKeyForSelected());
            objBouncingInstrumentwiseTO.setBouncingReason(txtReasonforBouncing);
            
            objBouncingInstrumentwiseTO.setClearingType((String)cbmClearingType.getKeyForSelected());
            Date Dt = DateUtil.getDateMMDDYYYY(dateClearingDate);
            if(Dt != null){
            Date dtDate = (Date) currDt.clone();
            dtDate.setDate(Dt.getDate());
            dtDate.setMonth(Dt.getMonth());
            dtDate.setYear(Dt.getYear());
            objBouncingInstrumentwiseTO.setClearingDate(dtDate);
            }else{
                objBouncingInstrumentwiseTO.setClearingDate(DateUtil.getDateMMDDYYYY(dateClearingDate));
            }
            
            //            objBouncingInstrumentwiseTO.setBranchId (getTxtBranchId());
            //            objBouncingInstrumentwiseTO.setCreatedBy (getTxtCreatedBy());
            //            objBouncingInstrumentwiseTO.setStatusBy (getTxtStatusBy());
            //            objBouncingInstrumentwiseTO.setStatusDt (DateUtil.getDateMMDDYYYY (getTxtStatusDt()));
            //            objBouncingInstrumentwiseTO.setAuthorizeStatus (getTxtAuthorizeStatus());
            //            objBouncingInstrumentwiseTO.setAuthorizeBy (getTxtAuthorizeBy());
            //            objBouncingInstrumentwiseTO.setAuthorizeDt (DateUtil.getDateMMDDYYYY (getTxtAuthorizeDt()));
            
            /** sets status (TRUE / FALSE) of Present Again Check Box to TO Object */
            if (getChkPresentAgain() == true) {
                objBouncingInstrumentwiseTO.setPresentAgain("Y");
            }else{
                objBouncingInstrumentwiseTO.setPresentAgain("N");
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return objBouncingInstrumentwiseTO;
    }
    
    /** This method helps in getting CommonConstants.MAP_WHERE codition and executing the query */
    public void populateData(HashMap whereMap) {
        try {
            HashMap mapData = proxy.executeQuery(whereMap, operationMap);
            populateOB(mapData);
        } catch( Exception e ) {
            parseException.logException(e,true);
        }
        
    }
    
    
    /** This method helps in populating the data from the View All table to the respective Fields */
    private void populateOB(HashMap mapData) throws Exception{
        BouncingInstrumentwiseTO objBouncingInstrumentwiseTO;
        objBouncingInstrumentwiseTO = (BouncingInstrumentwiseTO) ((List) mapData.get("BouncingInstrumentwiseTO")).get(0);
        
        setCboBouncingType(CommonUtil.convertObjToStr(getCbmBouncingType().getDataForKey(objBouncingInstrumentwiseTO.getBouncingType())));
        //        setCboReasonforBouncing(CommonUtil.convertObjToStr(getCbmReasonforBouncing().getDataForKey(objBouncingInstrumentwiseTO.getBouncingReason())));
        setTxtReasonforBouncing(CommonUtil.convertObjToStr(objBouncingInstrumentwiseTO.getBouncingReason()));
        
        setCboClearingType(CommonUtil.convertObjToStr(getCbmClearingType().getDataForKey(objBouncingInstrumentwiseTO.getClearingType())));
        
        setTxtClearingSerialNo(objBouncingInstrumentwiseTO.getInwardId());
        setTxtInwardScheduleNo(objBouncingInstrumentwiseTO.getInwardScheduleNo());
        setdateClearingDate(DateUtil.getStringDate(objBouncingInstrumentwiseTO.getClearingDate()));
        /** gets status (TRUE / FALSE) of Present Again Check Box frm DataBase */
        if ((objBouncingInstrumentwiseTO.getPresentAgain()).equals("Y")){
            setChkPresentAgain(true);
        }else{
            setChkPresentAgain(false);
        }
        notifyObservers();
    }
    
    /** To reset the txt fileds to null */
    public void resetForm(){
        setCboBouncingType("");
        setCboClearingType("");
        //        setCboReasonforBouncing("");
        setTxtReasonforBouncing("");
        setdateClearingDate("");
        setChkPresentAgain(false);
        setTxtClearingSerialNo("");
        setTxtInwardScheduleNo("");
        resetLabels();
        notifyObservers();
    }
    
    /** Resets the labels */
    public void resetLabels(){
        setLblClearingType("");
        setLblInstrumentDate("");
        setLblClearingDate("");
        setLblInstrumentType("");
        setLblAmount("");
        setLblName("");
        setLblBankCode("");
        setLblBranchCode("");
        setLblInstrumentNumber("");
        setLblAccountHead("");
        setLblAccountNumber("");
    }
    
    
    public HashMap getAuthorizeMap() {
        return authorizeMap;
    }
    public void setAuthorizeMap(HashMap authorizeMap) {
        this.authorizeMap = authorizeMap;
    }
    
}