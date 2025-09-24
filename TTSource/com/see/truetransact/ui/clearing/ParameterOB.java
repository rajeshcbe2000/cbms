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
package com.see.truetransact.ui.clearing;

import org.apache.log4j.Logger;

import java.util.Observable;
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
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.clearing.ParameterTO;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.COptionPane;
import java.util.Date;
import com.see.truetransact.ui.TrueTransactMain;
/**
 *
 * @author  Prasath.T
 */
public class ParameterOB  extends CObservable{
    
    // Variables declaration - do not modify
    private static ParameterOB objParameterOB; // singleton object
    final ParameterRB objParameterRB = new ParameterRB();
    private ProxyFactory proxy;
    
    private final static Logger log = Logger.getLogger(ParameterOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final String YES = "Y";
    private final String NO = "N";
    private final String BRANCHID = TrueTransactMain.BRANCH_ID;
    private final int IDAYS = 1;
    private final int IMONTHS = 30;
    private final int IYEARS = 365;
    
    private final String DAYS = "DAYS";
    private final String MONTHS = "MONTHS";
    private final String YEARS = "YEARS";
    
    private boolean rdoHighValue_Yes = false;
    private boolean rdoHighValue_No = false;
    
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private String cboClearingType = "";
    private String cboClearingFreq = "";
    private String txtClearingFreq = "";
    private String txtLotSize = "";
    private String txtServiceBranchCode = "";
    private String txtValueofHighValueCheque = "";
    
    private ComboBoxModel cbmClearingType;
    private ComboBoxModel cbmClearingFreq;
    
    private HashMap lookUpHash;
    private HashMap lookupMap;
    private HashMap keyValue;
    private HashMap operationMap;
    
    private ArrayList key;
    private ArrayList value;
    private Date currDt = null;
    private int actionType;
    private int result;
    // End of variables declaration
    
    /** Creates a new instance of ParameterOB */
    public ParameterOB() {
        try {
            currDt = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            setOperationMap();
            initUIComboBoxModel();
            fillDropdown();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    static {
        try {
            log.info("Creating ParameterOB...");
            objParameterOB = new ParameterOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }

    /** Creating Instance for ComboBoxModel */
    private void initUIComboBoxModel(){
        cbmClearingType = new ComboBoxModel();
    }
    
    /** Filling up the DropDown */
    public void fillDropdown() throws Exception{
        lookupMap = new HashMap();
        lookupMap.put(CommonConstants.MAP_NAME,"getSelectBankClearingType");
        lookupMap.put(CommonConstants.PARAMFORQUERY, null);
        keyValue = ClientUtil.populateLookupData(lookupMap);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmClearingType = new ComboBoxModel(key,value);
        
        //for multiple lookup
        lookupMap = new HashMap();
        lookupMap.put(CommonConstants.MAP_NAME, null);
        ArrayList lookupKey = new ArrayList();
	lookupKey.add("PERIOD");
        lookupMap.put(CommonConstants.PARAMFORQUERY, lookupKey);
        lookupMap = ClientUtil.populateLookupData(lookupMap);
        
        lookupMap = (HashMap)lookupMap.get("PERIOD");
        key = (ArrayList)lookupMap.get(CommonConstants.KEY);
        value = (ArrayList)lookupMap.get(CommonConstants.VALUE);
        cbmClearingFreq = new ComboBoxModel(key,value);
        
        makeComboBoxKeyValuesNull();
    }
    
    /** Return the key,value to be populated for the comboboxmodel */
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    /**
     * To make ComboBox key values Null
     */
    private void makeComboBoxKeyValuesNull(){
        key = null;
        value = null;
        lookupMap = null;
        keyValue = null;
    }
    
    // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "ParameterJNDI");
        operationMap.put(CommonConstants.HOME, "clearing.ParameterHome");
        operationMap.put(CommonConstants.REMOTE, "clearing.Parameter");
    }
    
    /* Return an instance of ParameterOB */
    public static ParameterOB getInstance() {
        return objParameterOB;
    }
    
    // Setter method for txtLotSize
    void setTxtLotSize(String txtLotSize){
        this.txtLotSize = txtLotSize;
        setChanged();
    }
    // Getter method for txtLotSize
    String getTxtLotSize(){
        return this.txtLotSize;
    }
    
    // Setter method for rdoHighValue_Yes
    void setRdoHighValue_Yes(boolean rdoHighValue_Yes){
        this.rdoHighValue_Yes = rdoHighValue_Yes;
        setChanged();
    }
    // Getter method for rdoHighValue_Yes
    boolean getRdoHighValue_Yes(){
        return this.rdoHighValue_Yes;
    }
    
    // Setter method for rdoHighValue_No
    void setRdoHighValue_No(boolean rdoHighValue_No){
        this.rdoHighValue_No = rdoHighValue_No;
        setChanged();
    }
    // Getter method for rdoHighValue_No
    boolean getRdoHighValue_No(){
        return this.rdoHighValue_No;
    }
    
     void setCboClearingType(String cboClearingType){
        this.cboClearingType = cboClearingType;
    }
    // Getter method for txtClearingType
    String getCboClearingType(){
        return this.cboClearingType;
    }
    
    // Setter method for txtServiceBranchCode
    void setTxtServiceBranchCode(String txtServiceBranchCode){
        this.txtServiceBranchCode = txtServiceBranchCode;
        setChanged();
    }
    // Getter method for txtServiceBranchCode
    String getTxtServiceBranchCode(){
        return this.txtServiceBranchCode;
    }
    
    // Setter method for txtValueofHighValueCheque
    void setTxtValueofHighValueCheque(String txtValueofHighValueCheque){
        this.txtValueofHighValueCheque = txtValueofHighValueCheque;
        setChanged();
    }
    // Getter method for txtValueofHighValueCheque
    String getTxtValueofHighValueCheque(){
        return this.txtValueofHighValueCheque;
    }
    
    // Getter method for cbmClearingType
    public void setCbmClearingType(ComboBoxModel cbmClearingType){
        this.cbmClearingType = cbmClearingType;
        setChanged();
    }
    //    // Setter method for cbmClearingType
    ComboBoxModel getCbmClearingType(){
        return cbmClearingType;
    }
    // Setter method for actionType
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
        ParameterTO parameterTO = (ParameterTO)((List) mapData.get("ParameterTO")).get(0);
        setParameterTO(parameterTO);
        ttNotifyObservers();
    }
    // To set the data in the UI
    private void setParameterTO(ParameterTO objparameterTO) throws Exception {
        setCboClearingType(CommonUtil.convertObjToStr(objparameterTO.getClearingType()));

        int multiply = CommonUtil.convertObjToInt(objparameterTO.getClearingPeriod());
        if (multiply >= IYEARS && (multiply%IYEARS == 0)) {
            setCboClearingFreq(CommonUtil.convertObjToStr(getCbmClearingFreq().getDataForKey(YEARS)));
            multiply = multiply/IYEARS;
        } else if (multiply >= IMONTHS && (multiply%IMONTHS == 0)) {
            setCboClearingFreq(CommonUtil.convertObjToStr(getCbmClearingFreq().getDataForKey(MONTHS)));
            multiply = multiply/IMONTHS;
        } else if (multiply >= IDAYS) {
            setCboClearingFreq(CommonUtil.convertObjToStr(getCbmClearingFreq().getDataForKey(DAYS)));
            multiply = multiply;
        } else {
            setCboClearingFreq("");
            multiply = 0;
        }
        System.out.println("Cbo:" + getCboClearingFreq());
        System.out.println("Txt:" + multiply);
        setTxtClearingFreq(String.valueOf(multiply));

        setTxtLotSize(CommonUtil.convertObjToStr(objparameterTO.getLotsizeMicrClearing()));
        if (objparameterTO.getHighValAppl().equals(YES)) {
            setRdoHighValue_Yes(true);
            setRdoHighValue_No(false);
        } else if (objparameterTO.getHighValAppl().equals(NO)) {
            setRdoHighValue_Yes(false);
            setRdoHighValue_No(true);
        }
        setTxtServiceBranchCode(CommonUtil.convertObjToStr(objparameterTO.getServiceBranchCode()));
        setTxtValueofHighValueCheque(CommonUtil.convertObjToStr(objparameterTO.getHighValCheque()));
    }
    /* To set data in the Transfer Object*/
    public ParameterTO setParameterData() {
        log.info("Inside setRemittanceIssueData()");
        final ParameterTO objParameterTO = new ParameterTO();
        try{
            objParameterTO.setClearingType(CommonUtil.convertObjToStr(getCboClearingType()));
            objParameterTO.setLotsizeMicrClearing(CommonUtil.convertObjToDouble(getTxtLotSize()));
            if (getRdoHighValue_Yes()==true) {
                objParameterTO.setHighValAppl(CommonUtil.convertObjToStr(YES));
            } else if (getRdoHighValue_No()==true) {
                objParameterTO.setHighValAppl(CommonUtil.convertObjToStr(NO));
            }
            objParameterTO.setServiceBranchCode(CommonUtil.convertObjToStr(getTxtServiceBranchCode()));
            objParameterTO.setHighValCheque(CommonUtil.convertObjToDouble(getTxtValueofHighValueCheque()));
            if(!getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
                objParameterTO.setStatusBy(TrueTransactMain.USER_ID);
            }
            objParameterTO.setCommand(CommonUtil.convertObjToStr(getCommand()));
            objParameterTO.setBranchId(BRANCHID);
            
            int multiply = 0;
            if (getCboClearingFreq().equalsIgnoreCase(DAYS)) {
                multiply = IDAYS;
            } else if (getCboClearingFreq().equalsIgnoreCase(MONTHS)) {
                multiply = IMONTHS;
            } else if (getCboClearingFreq().equalsIgnoreCase(YEARS)) {
                multiply = IYEARS;
            } else {
                multiply = 0;
            }
            objParameterTO.setClearingPeriod(new Double(CommonUtil.convertObjToInt(getTxtClearingFreq()) * multiply));
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
        setTxtLotSize("");
        setRdoHighValue_Yes(false);
        setRdoHighValue_No(false);
        setCboClearingType("");
        setTxtServiceBranchCode("");
        setTxtValueofHighValueCheque("");
        setCboClearingFreq("");
        setTxtClearingFreq("");
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
                    final ParameterRB parameterRB = new ParameterRB();
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
        final ParameterTO objParameterTO = setParameterData();
        final HashMap data = new HashMap();
        data.put("ParameterTO",objParameterTO);
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        data.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        HashMap proxyResultMap = proxy.execute(data, operationMap);
        setResult(actionType);
        actionType = ClientConstants.ACTIONTYPE_CANCEL;
        resetForm();
    }
    
    /* Used to check the duplication of Clearing Type when new record is going to be inserted */
    public int checkDuplication(String clearingType) {
        int COUNT = 0,RESULT = 1;
        final int ZERO = 0;
        try {
            /* If the selected value in the combobox is not null proceed */
            if ( !(clearingType.equals("") ) ) {
                HashMap where = new HashMap();
                where.put("CLEARING_TYPE",clearingType);
                where.put("BRANCH_ID", BRANCHID);
                List clearingTypeCount = ClientUtil.executeQuery("countClearingType", where);
                where = null;
                COUNT = Integer.parseInt(((HashMap)clearingTypeCount.get(0)).get("COUNT").toString());
                /* If count is greater than zero then there is a duplication of clearingtype */
                if( COUNT > ZERO ) {
                    RESULT = showAlertWindow();
                    setCboClearingType("");
                    notifyObservers();
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
        String[] options = {objParameterRB.getString("cDialogOK")};
        option = COptionPane.showOptionDialog(null, objParameterRB.getString("WarningMessage"), CommonConstants.WARNINGTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        return option;
    }
    
    /* Returns a resultmap by executing a Query */
    public HashMap getResultMap(String clearingType){
        HashMap resultMap = null;
        HashMap where = new HashMap();
        where.put("CLEARING_TYPE", clearingType);
        ArrayList resultList =(ArrayList) ClientUtil.executeQuery("getSelectBankParams", where);
        where = null;
        if(resultList.size() > 0){
            resultMap = (HashMap) resultList.get(0);
        }
        return resultMap;
    }
    
    /**
     * Getter for property cbmClearingFreq.
     * @return Value of property cbmClearingFreq.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmClearingFreq() {
        return cbmClearingFreq;
    }    
    
    /**
     * Setter for property cbmClearingFreq.
     * @param cbmClearingFreq New value of property cbmClearingFreq.
     */
    public void setCbmClearingFreq(com.see.truetransact.clientutil.ComboBoxModel cbmClearingFreq) {
        this.cbmClearingFreq = cbmClearingFreq;
    }
    
    /**
     * Getter for property cboClearingFreq.
     * @return Value of property cboClearingFreq.
     */
    public java.lang.String getCboClearingFreq() {
        return cboClearingFreq;
    }
    
    /**
     * Setter for property cboClearingFreq.
     * @param cboClearingFreq New value of property cboClearingFreq.
     */
    public void setCboClearingFreq(java.lang.String cboClearingFreq) {
        this.cboClearingFreq = cboClearingFreq;
        setChanged();        
    }
    
    /**
     * Getter for property txtClearingFreq.
     * @return Value of property txtClearingFreq.
     */
    public java.lang.String getTxtClearingFreq() {
        return txtClearingFreq;
    }
    
    /**
     * Setter for property txtClearingFreq.
     * @param txtClearingFreq New value of property txtClearingFreq.
     */
    public void setTxtClearingFreq(java.lang.String txtClearingFreq) {
        this.txtClearingFreq = txtClearingFreq;
        setChanged();
    }
}