/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ParameterOB.java
 *
 * Created on March 15, 2004, 10:34 AM
 */
package com.see.truetransact.ui.clearing.banklevel;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.clearing.banklevel.BankClearingParameterTO;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uicomponent.CObservable;

import com.see.truetransact.ui.TrueTransactMain;
/**
 *
 * @author  Ashok Vijayakumar
 */
public class BankClearingParameterOB extends CObservable {
    
    // Variables declaration - do not modify
    private static BankClearingParameterOB objParameterOB; // singleton object
    final BankClearingParameterRB objBankClearingParameterRB = new BankClearingParameterRB();
    private ProxyFactory proxy;
    
    private final static Logger log = Logger.getLogger(BankClearingParameterOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final String YES = "Y";
    private final String NO = "N";
    private final String BRANCHID = TrueTransactMain.BRANCH_ID;
    
    
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private String txtClearingType = "";
    private String txtOCReturnCharges = "";
    private String txtICReturnCharges = "";
    private String txtOCInstumentCharges = "";
    private String txtClearingHD = "";
    private String txtClearingSuspenseHD = "";
    private String txtOCReturnChargesHD = "";
    private String txtICReturnChargesHD = "";
    private String txtOCInstrumentChargesHD="";
    private String txtShortClaimAcHead = "";
    private String txtExcessClaimAcHead = "";
    private boolean rdoCompleteDay_Yes = false;
    private boolean rdoCompleteDay_No = false;
    private boolean chkInstrumentCharges=false;
    private ComboBoxModel cbmClearingType;
    
    private HashMap lookUpHash;
    private HashMap keyValue;
    private HashMap operationMap;
    
    private ArrayList key;
    private ArrayList value;
    
    private int actionType;
    private int result;
    // End of variables declaration
    
    /** Creates a new instance of ParameterOB */
    public BankClearingParameterOB() {
        try {
            proxy = ProxyFactory.createProxy();
            setOperationMap();
            //            fillDropdown();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    static {
        try {
            log.info("Creating ParameterOB...");
            objParameterOB = new BankClearingParameterOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "BankClearingParameterJNDI");
        operationMap.put(CommonConstants.HOME, "clearing.banklevel.BankClearingParameterHome");
        operationMap.put(CommonConstants.REMOTE, "clearing.banklevel.BankClearingParameter");
    }
    
    public static BankClearingParameterOB getInstance() {
        return objParameterOB;
    }
    // Setter method for txtOCReturnCharges
    void setTxtOCReturnCharges(String txtOCReturnCharges){
        this.txtOCReturnCharges = txtOCReturnCharges;
        setChanged();
    }
    // Getter method for txtOCReturnCharges
    String getTxtOCReturnCharges(){
        return this.txtOCReturnCharges;
    }
    
    // Setter method for txtICReturnCharges
    void setTxtICReturnCharges(String txtICReturnCharges){
        this.txtICReturnCharges = txtICReturnCharges;
        setChanged();
    }
    // Getter method for txtICReturnCharges
    String getTxtICReturnCharges(){
        return this.txtICReturnCharges;
    }
    
    // Setter method for txtClearingType
    void setTxtClearingType(String txtClearingType){
        this.txtClearingType = txtClearingType;
        setChanged();
    }
    // Getter method for txtClearingType
    String getTxtClearingType(){
        return this.txtClearingType;
    }
    
    // Setter method for txtClearingHD
    void setTxtClearingHD(String txtClearingHD){
        this.txtClearingHD = txtClearingHD;
        setChanged();
    }
    // Getter method for txtClearingHD
    String getTxtClearingHD(){
        return this.txtClearingHD;
    }
    
    // Setter method for txtClearingSuspenseHD
    void setTxtClearingSuspenseHD(String txtClearingSuspenseHD){
        this.txtClearingSuspenseHD = txtClearingSuspenseHD;
        setChanged();
    }
    // Getter method for txtClearingSuspenseHD
    String getTxtClearingSuspenseHD(){
        return this.txtClearingSuspenseHD;
    }
    
    // Setter method for txtOCReturnChargesHD
    void setTxtOCReturnChargesHD(String txtOCReturnChargesHD){
        this.txtOCReturnChargesHD = txtOCReturnChargesHD;
        setChanged();
    }
    // Getter method for txtOCReturnChargesHD
    String getTxtOCReturnChargesHD(){
        return this.txtOCReturnChargesHD;
    }
    
    // Setter method for txtICReturnChargesHD
    void setTxtICReturnChargesHD(String txtICReturnChargesHD){
        this.txtICReturnChargesHD = txtICReturnChargesHD;
        setChanged();
    }
    // Getter method for txtICReturnChargesHD
    String getTxtICReturnChargesHD(){
        return this.txtICReturnChargesHD;
    }

    public String getTxtOCInstrumentChargesHD() {
        return txtOCInstrumentChargesHD;
    }

    public void setTxtOCInstrumentChargesHD(String txtOCInstrumentChargesHD) {
        this.txtOCInstrumentChargesHD = txtOCInstrumentChargesHD;
    }

    public String getTxtOCInstumentCharges() {
        return txtOCInstumentCharges;
    }

    public void setTxtOCInstumentCharges(String txtOCInstumentCharges) {
        this.txtOCInstumentCharges = txtOCInstumentCharges;
    }

    public boolean isChkInstrumentCharges() {
        return chkInstrumentCharges;
    }

    public void setChkInstrumentCharges(boolean chkInstrumentCharges) {
        this.chkInstrumentCharges = chkInstrumentCharges;
    }
    
    // Getter method for cbmClearingType
    //    public void setCbmClearingType(ComboBoxModel cbmClearingType){
    //        this.cbmClearingType = cbmClearingType;
    //        setChanged();
    //    }
    //    // Setter method for cbmClearingType
    //    ComboBoxModel getCbmClearingType(){
    //        return cbmClearingType;
    //    }
    // Setter method for actionType
    
    /**
     * Getter for property txtShortClaimAcHead.
     * @return Value of property txtShortClaimAcHead.
     */
    public java.lang.String getTxtShortClaimAcHead() {
        return txtShortClaimAcHead;
    }
    
    /**
     * Setter for property txtShortClaimAcHead.
     * @param txtShortClaimAcHead New value of property txtShortClaimAcHead.
     */
    public void setTxtShortClaimAcHead(java.lang.String txtShortClaimAcHead) {
        this.txtShortClaimAcHead = txtShortClaimAcHead;
    }
    
    /**
     * Getter for property txtExcessClaimAcHead.
     * @return Value of property txtExcessClaimAcHead.
     */
    public java.lang.String getTxtExcessClaimAcHead() {
        return txtExcessClaimAcHead;
    }
    
    /**
     * Setter for property txtExcessClaimAcHead.
     * @param txtExcessClaimAcHead New value of property txtExcessClaimAcHead.
     */
    public void setTxtExcessClaimAcHead(java.lang.String txtExcessClaimAcHead) {
        this.txtExcessClaimAcHead = txtExcessClaimAcHead;
    }
   
    public void setRdoCompleteDay_Yes(boolean rdoCompleteDay_Yes){
        this.rdoCompleteDay_Yes = rdoCompleteDay_Yes;
        setChanged();
    }
      // Getter method for rdoCompleteDay_Yes
    boolean getRdoCompleteDay_Yes(){
        return this.rdoCompleteDay_Yes;
    }
    
    // Setter method for rdoCompleteDay_No
    void setRdoCompleteDay_No(boolean rdoCompleteDay_No){
        this.rdoCompleteDay_No = rdoCompleteDay_No;
        setChanged();
    }
    // Getter method for rdoCompleteDay_No
    boolean getRdoCompleteDay_No(){
        return this.rdoCompleteDay_No;
    }
       
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }
    // Getter method for actionType
    public int getActionType(){
        return this.actionType;
    }
    
    
    // internally calls update in UI
    public void ttNotifyObservers(){
        notifyObservers();
    }
    // Setter method for result
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    // Getter method for result
    public int getResult(){
        return this.result;
    }
    // Setter method for lblStatus
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    // Getter method for lblStatus
    public String getLblStatus(){
        return this.lblStatus;
    }
    // To set the status based on ActionType, either New, Edit, etc.,
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }
    // To reset the status
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }
    /* To populate the screen */
    public void populateData(HashMap whereMap) {
        log.info("Inside populateData()");
        try {
            HashMap mapData = proxy.executeQuery(whereMap, operationMap);
            log.info("mapData:"+mapData);
            populateOB(mapData);
        } catch( Exception e ) {
            parseException.logException(e,true);
            e.printStackTrace();
        }
    }
    // To populate  the data in UI retrived from the database
    private void populateOB(HashMap mapData) throws Exception{
        log.info("In PopulateOB method:");
        BankClearingParameterTO parameterTO = (BankClearingParameterTO)((List) mapData.get("BankClearingParameterTO")).get(0);
        setParameterTO(parameterTO);
        ttNotifyObservers();
    }
    // To set the data in the UI
    private void setParameterTO(BankClearingParameterTO objparameterTO) throws Exception {
        setTxtClearingType(CommonUtil.convertObjToStr(objparameterTO.getClearingType()));
        setTxtOCReturnCharges(CommonUtil.convertObjToStr(objparameterTO.getOutwardReturnChrg()));
        setTxtICReturnCharges(CommonUtil.convertObjToStr(objparameterTO.getInwardReturnChrg()));
        setTxtOCInstumentCharges(CommonUtil.convertObjToStr(objparameterTO.getOcInstumentCharges()));       
        setTxtClearingHD(CommonUtil.convertObjToStr(objparameterTO.getClearingHd()));
        setTxtClearingSuspenseHD(CommonUtil.convertObjToStr(objparameterTO.getClearingSuspenseHd()));
        setTxtOCReturnChargesHD(CommonUtil.convertObjToStr(objparameterTO.getOutwardReturnHd()));
        setTxtICReturnChargesHD(CommonUtil.convertObjToStr(objparameterTO.getInwardReturnHd()));
        setTxtOCInstrumentChargesHD(CommonUtil.convertObjToStr(objparameterTO.getOutwardChargesHd()));
        if(objparameterTO.getInstrumentChargesCheck()!=null && objparameterTO.getInstrumentChargesCheck().equals("Y")){
            setChkInstrumentCharges(true);
        }
        setTxtShortClaimAcHead(objparameterTO.getShortClaimHd());
        setTxtExcessClaimAcHead(objparameterTO.getExcessClaimHd());
        if(objparameterTO.getDayendWithoutCo().equals(YES)){
            setRdoCompleteDay_Yes(true);
            setRdoCompleteDay_No(false);
        }else if(objparameterTO.getDayendWithoutCo().equals(NO)){
            setRdoCompleteDay_Yes(false);
            setRdoCompleteDay_No(true);
        }
        
    }
    /* To set data in the Transfer Object*/
    public BankClearingParameterTO setParameterData() {
        log.info("Inside setRemittanceIssueData()");
        final BankClearingParameterTO objParameterTO = new BankClearingParameterTO();
        try{
            objParameterTO.setClearingType(CommonUtil.convertObjToStr(getTxtClearingType()));
            objParameterTO.setClearingHd(CommonUtil.convertObjToStr(getTxtClearingHD()));
            objParameterTO.setOutwardReturnChrg(CommonUtil.convertObjToDouble(getTxtOCReturnCharges()));
            objParameterTO.setInwardReturnChrg(CommonUtil.convertObjToDouble(getTxtICReturnCharges()));
            objParameterTO.setOcInstumentCharges(CommonUtil.convertObjToDouble(getTxtOCInstumentCharges()));
            objParameterTO.setClearingSuspenseHd(CommonUtil.convertObjToStr(getTxtClearingSuspenseHD()));
            objParameterTO.setOutwardReturnHd(CommonUtil.convertObjToStr(getTxtOCReturnChargesHD()));
            objParameterTO.setInwardReturnHd(CommonUtil.convertObjToStr(getTxtICReturnChargesHD()));
            objParameterTO.setOutwardChargesHd(CommonUtil.convertObjToStr(getTxtOCInstrumentChargesHD()));
            if(isChkInstrumentCharges()==true){
                objParameterTO.setInstrumentChargesCheck("Y");   
            }else{
                 objParameterTO.setInstrumentChargesCheck("N");   
            }
            objParameterTO.setCommand(CommonUtil.convertObjToStr(getCommand()));
            objParameterTO.setShortClaimHd(getTxtShortClaimAcHead());
            objParameterTO.setExcessClaimHd(getTxtExcessClaimAcHead());
            if(getRdoCompleteDay_Yes()==true){
                objParameterTO.setDayendWithoutCo(YES);
            }else if(getRdoCompleteDay_Yes()==false){
                objParameterTO.setDayendWithoutCo(NO);
            }
            objParameterTO.setStatusBy(TrueTransactMain.USER_ID);
            objParameterTO.setStatusDt(ClientUtil.getCurrentDate());
            
        }catch(Exception e){
            e.printStackTrace();
            parseException.logException(e,true);
        }
        return objParameterTO;
    }
    
    /* To get the type of command */
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
    
    // To reset the fields present in the UI
    public void resetForm(){
        setTxtOCReturnCharges("");
        setTxtICReturnCharges("");
        setTxtOCInstumentCharges("");
        setTxtClearingType("");
        setTxtClearingHD("");
        setTxtClearingSuspenseHD("");
        setTxtOCReturnChargesHD("");
        setTxtICReturnChargesHD("");
        setTxtOCInstrumentChargesHD("");
        setChkInstrumentCharges(false);
        setTxtShortClaimAcHead("");
        setTxtExcessClaimAcHead("");
        setRdoCompleteDay_Yes(false);
        setRdoCompleteDay_No(false);
        ttNotifyObservers();
    }
    
    /** To perform the appropriate operation */
    public void doAction() {
        try {
            log.info("Inside doAction()");
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null ){
                    doActionPerform();
                }
                else{
                    final BankClearingParameterRB parameterRB = new BankClearingParameterRB();
                    throw new TTException(parameterRB.getString("TOCommandError"));
                }
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        log.info("Inside doActionPerform()");
        final BankClearingParameterTO objParameterTO = setParameterData();
        final HashMap data = new HashMap();
        data.put("BankClearingParameterTO",objParameterTO);
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        data.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
        data.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        HashMap proxyResultMap = proxy.execute(data, operationMap);
        setResult(actionType);
        actionType = ClientConstants.ACTIONTYPE_CANCEL;
        resetForm();
    }
    
    /** This checks for the duplication of ClearingType */
    public int checkDuplication(String clearingType) {
        int COUNT = 0,RESULT = 1;
        final int ZERO = 0;
        try {
            /* If the selected value in the combobox is not null proceed */
            if ( !(clearingType.equals("") ) ) {
                HashMap where = new HashMap();
                where.put("CLEARING_TYPE",clearingType);
                List clearingTypeCount = ClientUtil.executeQuery("getcountClearingType", where);
                where = null;
                COUNT = Integer.parseInt(((HashMap)clearingTypeCount.get(0)).get("COUNT").toString());
                /* If count is greater than zero then there is a duplication of clearingtype */
                if( COUNT > ZERO ) {
                    RESULT = showAlertWindow();
                }
            }
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        return RESULT;
    }
    
    /* If there is duplication Alert Message */
    private int showAlertWindow() throws Exception {
        int option = 1;
        String[] options = {objBankClearingParameterRB.getString("cDialogOK")};
        option = COptionPane.showOptionDialog(null, objBankClearingParameterRB.getString("WarningMessage"), CommonConstants.WARNINGTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        return option;
    }
    
    
    
}


