/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LevelControlOB.java
 *
 * Created on March 2, 2004, 12:35 PM
 */

package com.see.truetransact.ui.sysadmin.levelcontrol;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ComboBoxModel;

import com.see.truetransact.transferobject.sysadmin.levelcontrol.LevelControlTO;

import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

import org.apache.log4j.Logger;
import java.util.Date;
/**
 *
 * @author  rahul
 */
public class LevelControlOB extends Observable{
    private HashMap lookUpHash;
    private HashMap keyValue;
    private HashMap operationMap;
    private ArrayList key;
    private ArrayList value;
    
    private ProxyFactory proxy = null;
    
    final String YES = "Y";
    final String NO = "N";
    
    private int actionType;
    private int result;
    
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.sysadmin.levelcontrol.LevelControlRB", ProxyParameters.LANGUAGE);
    
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private final static Logger log = Logger.getLogger(LevelControlUI.class);
    private static Date currDt = null;
    private static LevelControlOB levelControlOB;
    static {
        try {
            currDt = ClientUtil.getCurrentDate();
            log.info("In LevelControlOB Declaration");
            levelControlOB = new LevelControlOB();
        } catch(Exception e) {
            log.info("Error in LevelControlOB Declaration");
        }
    }
    
    public static LevelControlOB getInstance() {
        return levelControlOB;
    }
    /** Creates a new instance of LevelControlOB */
    public LevelControlOB() throws Exception {
        initianSetup();
    }
    
    private void initianSetup() throws Exception{
        log.info("In initianSetup()");
        setOperationMap();
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            log.info(" Error In initianSetup()");
            e.printStackTrace();
        }
        fillDropdown();
    }
    
    // Set the value of JNDI and the Session Bean...
    private void setOperationMap() throws Exception{
        log.info("In setOperationMap()");
        
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "LevelControlJNDI");
        operationMap.put(CommonConstants.HOME, "sysadmin.levelcontrol.LevelControlHome");
        operationMap.put(CommonConstants.REMOTE, "sysadmin.levelcontrol.LevelControl");
    }
     private void getKeyValue(HashMap keyValue)  throws Exception{
        log.info("In getKeyValue()");
        
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
      public void fillDropdown() throws Exception{
        log.info("fillDropdown"); 
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,"getDesignation");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmName = new ComboBoxModel(key,value);
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
            e.printStackTrace();
        }
    }
    
    private void populateOB(HashMap mapData) throws Exception{
        log.info("In populateOB()");
        
        LevelControlTO objLevelControlTO = null;
        //Taking the Value of Transaction Id from each Table...
        // Here the first Row is selected...
        
        System.out.println("Returned Map data: " + mapData);
        
        System.out.println("Returned List data: " + mapData.get("LevelControlTO"));
        
        objLevelControlTO = (LevelControlTO) ((List) mapData.get("LevelControlTO")).get(0);
        setLevelControlTO(objLevelControlTO);
        //        ttNotifyObservers();
    }
    
    // To Enter the values in the UI fields, from the database...
    private void setLevelControlTO(LevelControlTO objLevelControlTO) throws Exception{
        log.info("In setLevelControlTO()");
        
        setTxtLevelID(objLevelControlTO.getLevelId());
        setTxtDescription(objLevelControlTO.getLevelDesc());
        setTxtCashCredit(CommonUtil.convertObjToStr(objLevelControlTO.getCashCredit()));
        setTxtCashDebit(CommonUtil.convertObjToStr(objLevelControlTO.getCashDebit()));
        setTxtTransferCredit(CommonUtil.convertObjToStr(objLevelControlTO.getTransCredit()));
        setTxtTransferDebit(CommonUtil.convertObjToStr(objLevelControlTO.getTransDebit()));
        setTxtClearingCredit(CommonUtil.convertObjToStr(objLevelControlTO.getClearingCredit()));
        setTxtClearingDebit(CommonUtil.convertObjToStr(objLevelControlTO.getClearingDebit()));
        setCboName(CommonUtil.convertObjToStr(objLevelControlTO.getLevelName()));
        
        if (CommonUtil.convertObjToStr(objLevelControlTO.getSingleWindow()).equalsIgnoreCase(YES)) {
            setChkSingleWindow(true);
        } else {
            setChkSingleWindow(false);
        }
    }
    
    // To enter the Data into the Database...Called from doActionPerform()...
    public LevelControlTO setLevelControl() {
        log.info("In setLevelControl()");
        
        final LevelControlTO objLevelControlTO = new LevelControlTO();
        try{
            objLevelControlTO.setLevelId(txtLevelID);
            objLevelControlTO.setLevelDesc(txtDescription);
             if (!CommonUtil.convertObjToStr(getCboName()).equals(""))
                objLevelControlTO.setLevelName(CommonUtil.convertObjToStr(getCbmName().getSelectedItem()));
            objLevelControlTO.setCashCredit(CommonUtil.convertObjToDouble(txtCashCredit));
            objLevelControlTO.setCashDebit(CommonUtil.convertObjToDouble(txtCashDebit));
            objLevelControlTO.setTransCredit(CommonUtil.convertObjToDouble(txtTransferCredit));
            objLevelControlTO.setTransDebit(CommonUtil.convertObjToDouble(txtTransferDebit));
            objLevelControlTO.setClearingCredit(CommonUtil.convertObjToDouble(txtClearingCredit));
            objLevelControlTO.setClearingDebit(CommonUtil.convertObjToDouble(txtClearingDebit));
            
            if (getChkSingleWindow()) {
                objLevelControlTO.setSingleWindow(YES);
            }else{
                objLevelControlTO.setSingleWindow(NO);
            }
            
            objLevelControlTO.setAuthorizeStatus("");
            objLevelControlTO.setBranchCode(TrueTransactMain.BRANCH_ID);
        }catch(Exception e){
            log.info("Error In setLevelControl()");
            //e.printStackTrace();
        }
        return objLevelControlTO;
    }
    
    
    // To perform Appropriate operation... Insert, Update, Delete...
    public void doAction() {
        log.info("In doAction()");
        try {
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null ){
                    doActionPerform();
                }
            }
            else
                log.info("Action Type Not Defined In setCashTransactionTO()");
        } catch (Exception e) {
            log.info("Error In doAction()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
            parseException.logException(e,true);
        }
    }
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        log.info("In doActionPerform()");
        
        final LevelControlTO objLevelControlTO = setLevelControl();
        objLevelControlTO.setCommand(getCommand());
        if(!getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
            objLevelControlTO.setStatusBy(TrueTransactMain.USER_ID);
            objLevelControlTO.setStatusDt(currDt);
        }
        final HashMap data = new HashMap();
        data.put("LevelControlTO",objLevelControlTO);
        
        HashMap proxyResultMap = proxy.execute(data,operationMap);
        
        if (proxyResultMap != null && proxyResultMap.containsKey(CommonConstants.TRANS_ID)) {
            ClientUtil.showMessageWindow(resourceBundle.getString("lblLevelID") + ": " + CommonUtil.convertObjToStr(proxyResultMap.get(CommonConstants.TRANS_ID)));
        }
        
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
    
    private String txtLevelID = "";
    private String txtName = "";
    private String txtDescription = "";
    private String txtCashCredit = "";
    private String txtCashDebit = "";
    private String txtTransferCredit = "";
    private String txtTransferDebit = "";
    private String txtClearingCredit = "";
    private String txtClearingDebit = "";
    private boolean chkSingleWindow = false;
     private String cboName="";
    private ComboBoxModel cbmName;
    
    // Returns the Current Value of Action type...
    public int getActionType(){
        return actionType;
    }
    
    // Sets the value of the Action Type to the poperation we want to execute...
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }
    
    public void resetForm(){
        log.info("In resetForm()");
        setTxtLevelID("");
        cboName="";
        setTxtDescription("");
        setTxtCashCredit("");
        setTxtCashDebit("");
        setTxtTransferCredit("");
        setTxtTransferDebit("");
        setTxtClearingCredit("");
        setTxtClearingDebit("");
        setChkSingleWindow(false);
        
        ttNotifyObservers();
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
        notifyObservers();
    }
    
    void setTxtLevelID(String txtLevelID){
        this.txtLevelID = txtLevelID;
        setChanged();
    }
    String getTxtLevelID(){
        return this.txtLevelID;
    }
    
    void setTxtName(String txtName){
        this.txtName = txtName;
        setChanged();
    }
    String getTxtName(){
        return this.txtName;
    }
    
    void setTxtDescription(String txtDescription){
        this.txtDescription = txtDescription;
        setChanged();
    }
    String getTxtDescription(){
        return this.txtDescription;
    }
    
    void setTxtCashCredit(String txtCashCredit){
        this.txtCashCredit = txtCashCredit;
        setChanged();
    }
    String getTxtCashCredit(){
        return this.txtCashCredit;
    }
    
    void setTxtCashDebit(String txtCashDebit){
        this.txtCashDebit = txtCashDebit;
        setChanged();
    }
    String getTxtCashDebit(){
        return this.txtCashDebit;
    }
    
    void setTxtTransferCredit(String txtTransferCredit){
        this.txtTransferCredit = txtTransferCredit;
        setChanged();
    }
    String getTxtTransferCredit(){
        return this.txtTransferCredit;
    }
    
    void setTxtTransferDebit(String txtTransferDebit){
        this.txtTransferDebit = txtTransferDebit;
        setChanged();
    }
    String getTxtTransferDebit(){
        return this.txtTransferDebit;
    }
    
    void setTxtClearingCredit(String txtClearingCredit){
        this.txtClearingCredit = txtClearingCredit;
        setChanged();
    }
    String getTxtClearingCredit(){
        return this.txtClearingCredit;
    }
    
    void setTxtClearingDebit(String txtClearingDebit){
        this.txtClearingDebit = txtClearingDebit;
        setChanged();
    }
    String getTxtClearingDebit(){
        return this.txtClearingDebit;
    }
    
    // Setter method for chkSingleWindow
    void setChkSingleWindow(boolean chkSingleWindow){
        this.chkSingleWindow = chkSingleWindow;
        setChanged();
    }
    // Getter method for chkSingleWindow
    boolean getChkSingleWindow(){
        return this.chkSingleWindow;
    }
    
    /** Checking if the amount entered in txtCashCredtit, txtCashDebit, txtTransferDebit, txtTransferCredit,txtClearingCredit,txtClearingDebit
     *already exists, if so setting a boolean variable exists as true otherwise false */
    public HashMap dataExists(){
        HashMap resultMap = new HashMap();
        try{
            HashMap whereMap = new HashMap();
            whereMap.put("CASH_CREDIT", CommonUtil.convertObjToDouble(getTxtCashCredit()));
            whereMap.put("CASH_DEBIT", CommonUtil.convertObjToDouble(getTxtCashDebit()));
            whereMap.put("CLEARING_CREDIT", CommonUtil.convertObjToDouble(getTxtClearingCredit()));
            whereMap.put("CLEARING_DEBIT", CommonUtil.convertObjToDouble(getTxtClearingDebit()));
            whereMap.put("TRANS_CREDIT", CommonUtil.convertObjToDouble(getTxtTransferCredit()));
            whereMap.put("TRANS_DEBIT", CommonUtil.convertObjToDouble(getTxtTransferDebit()));
            whereMap.put("LEVEL_ID", getTxtLevelID().trim().equals("") ? null : getTxtLevelID());
            
            ArrayList list = (ArrayList) ClientUtil.executeQuery("getSelectLevelControl", whereMap);
            if(list.size() > 0){
                resultMap = (HashMap) list.get(0);
                //                if( CommonUtil.convertObjToDouble(resultMap.get("COUNT")).doubleValue() > 0 ){
                //                    exists = true;
                //                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return resultMap;
    }
    
    /**
     * Getter for property cboName.
     * @return Value of property cboName.
     */
    public java.lang.String getCboName() {
        return cboName;
    }
    
    /**
     * Setter for property cboName.
     * @param cboName New value of property cboName.
     */
    public void setCboName(java.lang.String cboName) {
        this.cboName = cboName;
    }
    
    /**
     * Getter for property cbmName.
     * @return Value of property cbmName.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmName() {
        return cbmName;
    }
    
    /**
     * Setter for property cbmName.
     * @param cbmName New value of property cbmName.
     */
    public void setCbmName(com.see.truetransact.clientutil.ComboBoxModel cbmName) {
        this.cbmName = cbmName;
    }
    
}
