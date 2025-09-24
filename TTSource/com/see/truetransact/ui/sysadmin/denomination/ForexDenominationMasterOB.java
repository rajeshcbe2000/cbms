/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ForexDenominationMasterOB.java
 *
 * Created on January 27, 2005, 10:32 AM
 */

package com.see.truetransact.ui.sysadmin.denomination;

/**
 *
 * @author  152715
 */

import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import org.apache.log4j.Logger;
import com.see.truetransact.transferobject.sysadmin.denomination.ForexDenominationMasterTO;
import com.see.truetransact.uicomponent.CObservable;
import java.util.Date;
public class ForexDenominationMasterOB extends CObservable {
    
    //Varaibles Declarations
    private String cboCurrency = "";
    private String txtDenominationName = "";
    private String txtDenominationValue = "";
    private String cboDenominationType = "";
    private String createdBy = "";
    private String createdDt = "";
    private String statusTO = "";
    private String statusBy = "";
    private String statusDt = "";
    private String authorizeStatus = "";
    private String authorizeBy = "";
    private String authorizeDt = "";
    
    // ComboBoxModels
    private ComboBoxModel cbmCurrency;
    private ComboBoxModel cbmDenominationType;
    
    private HashMap lookUpHash;
    private HashMap keyValue;
    
    private ArrayList key;
    private ArrayList value;
    
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int actionType;
    private int result;
    private HashMap operationMap;
    private ProxyFactory proxy;
    
    private final ForexDenominationMasterRB objForexDenominationMasterRB = new ForexDenominationMasterRB();
    
    
    private static ForexDenominationMasterOB objForexDenominationMasterOB; // singleton object
    
    private final static Logger log = Logger.getLogger(ForexDenominationMasterOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private static Date currDt = null;
    // Implementation of Singleton pattern
    static {
        try {
            currDt = ClientUtil.getCurrentDate();
            log.info("Creating ForexDenominationMasterOB...");
            objForexDenominationMasterOB = new ForexDenominationMasterOB();
        } catch(Exception e) {
            //_log.error(e);
            parseException.logException(e,true);
        }
    }
    
    /**
     * Returns an instance of OtherBankOB.
     * @return  OtherBankOB
     */
    public static ForexDenominationMasterOB getInstance() {
        return objForexDenominationMasterOB;
    }
    
    /** Creates a new instance of ForexDenominationMasterOB */
    public ForexDenominationMasterOB() throws Exception {
        proxy = ProxyFactory.createProxy();
        setOperationMap();
        fillDropdown();
        
    }
    // Setter method for txtDenominationName
    void setTxtDenominationName(String txtDenominationName){
        this.txtDenominationName = txtDenominationName;
        setChanged();
    }
    // Getter method for txtDenominationName
    String getTxtDenominationName(){
        return this.txtDenominationName;
    }
    // Setter method for txtDenominationValue
    void setTxtDenominationValue(String txtDenominationValue){
        this.txtDenominationValue = txtDenominationValue;
        setChanged();
    }
    // Getter method for txtDenominationValue
    String getTxtDenominationValue(){
        return this.txtDenominationValue;
    }
    
    // Setter method for cboCurrency
    void setCboCurrency(String cboCurrency){
        this.cboCurrency = cboCurrency;
        setChanged();
    }
    // Getter method for cboCurrency
    String getCboCurrency(){
        return this.cboCurrency;
    }
    // Setter method for cboDenominationType
    void setCboDenominationType(String cboDenominationType){
        this.cboDenominationType = cboDenominationType;
        setChanged();
    }
    // Getter method for cboDenominationType
    String getCboDenominationType(){
        return this.cboDenominationType;
    }
    // Setter method for createdBy
    void setCreatedBy(String createdBy){
        this.createdBy = createdBy;
        setChanged();
    }
    // Getter method for createdBy
    String getCreatedBy(){
        return this.createdBy;
    }
    // Setter method for createdDt
    void setCreatedDt(String createdDt){
        this.createdDt = createdDt;
        setChanged();
    }
    // Getter method for createdDt
    String getCreatedDt(){
        return this.createdDt;
    }
    // Setter method for statusDt
    void setStatusDt(String statusDt){
        this.statusDt = statusDt;
        setChanged();
    }
    // Getter method for statusDt
    String getStatusDt(){
        return this.statusDt;
    }
    // Setter method for statusBy
    public void setStatusBy(String statusBy){
        this.statusBy = statusBy;
        setChanged();
    }
    // Getter method for statusBy
    public java.lang.String getStatusBy(){
        return this.statusBy;
    }
    // Setter method for statusTO
    void setStatusTO(String statusTO){
        this.statusTO = statusTO;
        setChanged();
    }
    // Getter method for statusTO
    String getStatusTO(){
        return this.statusTO;
    }
    void setCbmCurrency(ComboBoxModel cbmCurrency){
        this.cbmCurrency = cbmCurrency;
        setChanged();
    }
    
    ComboBoxModel getCbmCurrency(){
        return this.cbmCurrency;
    }
    void setCbmDenominationType(ComboBoxModel cbmDenominationType){
        this.cbmDenominationType = cbmDenominationType;
        setChanged();
    }
    
    ComboBoxModel getCbmDenominationType(){
        return this.cbmDenominationType;
    }
    // Setter method for authorizeDt
    void setAuthorizeDt(String authorizeDt){
        this.authorizeDt = authorizeDt;
        setChanged();
    }
    // Getter method for authorizeDt
    String getAuthorizeDt(){
        return this.authorizeDt;
    }
    // Setter method for authorizeBy
    void setAuthorizeBy(String authorizeBy){
        this.authorizeBy = authorizeBy;
        setChanged();
    }
    // Getter method for authorizeBy
    String getAuthorizeBy(){
        return this.authorizeBy;
    }
    // Setter method for authorizeStatus
    public void setAuthorizeStatus(String authorizeStatus){
        this.authorizeStatus = authorizeStatus;
        setChanged();
    }
    // Getter method for authorizeStatus
    public java.lang.String getAuthorizeStatus(){
        return this.authorizeStatus;
    }
    
    /**
     * Set Forex Denomination Master Details in the OB
     */
    private void setForexDenominationMasterOB(ForexDenominationMasterTO objForexDenominationMasterTO) throws Exception {
        log.info("Inside setForexDenominationMasterOB()");
        if (objForexDenominationMasterTO != null) {
            setCboCurrency((String) getCbmCurrency().getDataForKey(CommonUtil.convertObjToStr(objForexDenominationMasterTO.getCurrency())));
            setTxtDenominationName(CommonUtil.convertObjToStr(objForexDenominationMasterTO.getDenominationName()));
            setTxtDenominationValue(CommonUtil.convertObjToStr(objForexDenominationMasterTO.getDenominationValue()));
            setCboDenominationType((String) getCbmDenominationType().getDataForKey(CommonUtil.convertObjToStr(objForexDenominationMasterTO.getDenominationType())));
            setAuthorizeDt(DateUtil.getStringDate(objForexDenominationMasterTO.getAuthorizedDt()));
            setAuthorizeBy(CommonUtil.convertObjToStr(objForexDenominationMasterTO.getAuthorizedBy()));
            setAuthorizeStatus(CommonUtil.convertObjToStr(objForexDenominationMasterTO.getAuthorizeStatus()));
            setCreatedBy(CommonUtil.convertObjToStr(objForexDenominationMasterTO.getCreatedBy()));
            setCreatedDt(DateUtil.getStringDate(objForexDenominationMasterTO.getCreatedDt()));
            setStatusBy(CommonUtil.convertObjToStr(objForexDenominationMasterTO.getStatusBy()));
            setStatusDt(DateUtil.getStringDate(objForexDenominationMasterTO.getStatusDt()));
            setStatusTO(CommonUtil.convertObjToStr(objForexDenominationMasterTO.getStatus()));
        }
    }
    /**
     * Set Forex Denomination Master Details in the TO
     */
    private ForexDenominationMasterTO setForexDenominationMasterTO() {
        final ForexDenominationMasterTO objForexDenominationMasterTO = new ForexDenominationMasterTO();
        log.info("Inside setForexDenominationMasterOB()");
        try {
            objForexDenominationMasterTO.setCurrency(CommonUtil.convertObjToStr((String)cbmCurrency.getKeyForSelected()));
            objForexDenominationMasterTO.setDenominationType(CommonUtil.convertObjToStr((String)cbmDenominationType.getKeyForSelected()));
//            objForexDenominationMasterTO.setAuthorizedDt(currDt);            
            objForexDenominationMasterTO.setDenominationName(CommonUtil.convertObjToStr(getTxtDenominationName()));
            objForexDenominationMasterTO.setDenominationValue(CommonUtil.convertObjToDouble(getTxtDenominationValue()));
            objForexDenominationMasterTO.setAuthorizedBy(CommonUtil.convertObjToStr(getAuthorizeBy()));
            objForexDenominationMasterTO.setAuthorizeStatus(CommonUtil.convertObjToStr(getAuthorizeStatus()));
            objForexDenominationMasterTO.setCreatedBy(CommonUtil.convertObjToStr(getCreatedBy()));            
            objForexDenominationMasterTO.setCreatedDt(currDt);
            objForexDenominationMasterTO.setStatusBy(CommonUtil.convertObjToStr(getStatusBy()));            
            objForexDenominationMasterTO.setStatusDt(currDt);
            objForexDenominationMasterTO.setStatus(CommonUtil.convertObjToStr(getStatusTO()));
            objForexDenominationMasterTO.setCommand(CommonUtil.convertObjToStr(getCommand()));
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        return objForexDenominationMasterTO;
    }
    // To reset all fields
    public void resetForm() {
        setTxtDenominationName("");
        setTxtDenominationValue("");
        setCboCurrency("");
        setCboDenominationType("");
        setAuthorizeBy("");
        setAuthorizeDt("");
        setAuthorizeStatus("");
        setCreatedBy("");
        setCreatedDt("");
        setStatusTO("");
        setStatusBy("");
        setStatusDt("");
        ttNotifyObservers();
    }
    public String getLblStatus(){
        return this.lblStatus;
    }
    
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    
    public int getActionType(){
        return this.actionType;
    }
    
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }
    
    public int getResult(){
        return this.result;
    }
    
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
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
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }
    public void ttNotifyObservers(){
        notifyObservers();
    }
    // To fill the combobox with values
    public void fillDropdown() throws Exception{
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
        lookup_keys.add("FOREX.CURRENCY");
        lookup_keys.add("FOREX.TYPE");
        
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("FOREX.CURRENCY"));
        cbmCurrency = new ComboBoxModel(key,value);        
        
//        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("FOREX.TYPE"));
        cbmDenominationType = new ComboBoxModel(key,value);
        
        makeComboBoxKeyValuesNull();
    }
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    private void makeComboBoxKeyValuesNull(){
        key = null;
        value = null;
        lookUpHash = null;
        keyValue = null;
    }
    /* Sets the HashMap required to set JNDI,Home and Remote*/
    private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "ForexDenominationMasterJNDI");
        operationMap.put(CommonConstants.HOME, "com.see.truetransact.serverside.sysadmin.denomination.ForexDenominationMasterHome");
        operationMap.put(CommonConstants.REMOTE, "com.see.truetransact.serverside.sysadmin.denomination.ForexDenominationMaster");
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
                    throw new TTException(objForexDenominationMasterRB.getString("TOCommandError"));
                }
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        log.info("In doActionPerform...");
        final ForexDenominationMasterTO objForexDenominationMasterTO = setForexDenominationMasterTO();
        
        final HashMap data = new HashMap();
        data.put("ForexDenominationMasterTO",objForexDenominationMasterTO);
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        HashMap proxyResultMap = proxy.execute(data,operationMap);
        setResult(actionType);
        actionType = ClientConstants.ACTIONTYPE_CANCEL;
        resetForm();
    }
    /**
     * To Avoid Duplication
     */
    public boolean checkDuplicate() {
        boolean exist = false;
        HashMap where = new HashMap();
        String count = "0";
        where.put("CURRENCY", (String)cbmCurrency.getKeyForSelected());
        where.put("NAME", getTxtDenominationName());
        where.put("VALUE", CommonUtil.convertObjToDouble(getTxtDenominationValue()));
        where.put("TYPE", (String)cbmDenominationType.getKeyForSelected());
        List list = (List) ClientUtil.executeQuery("checkDuplicate",where);
        if (list != null) {
            if(list.size()>0 && list != null){
                count = CommonUtil.convertObjToStr(((LinkedHashMap)list.get(0)).get("COUNT"));
            } else {
                count = "0";
            }
            if (Integer.parseInt(count)>0) {
                exist = true;
            } else {
                exist = false;
            }
        }
        where = null;
        list = null;
        return exist;
    }
    //Do display the Data from the Database, in UI
    public void populateData(HashMap whereMap) {
        log.info("In populateData...");
        final HashMap mapData;
        try {
            mapData = proxy.executeQuery(whereMap, operationMap);
            log.info("proxy.executeQuery is working fine");
            populateOB(mapData);
        } catch( Exception e ) {
            log.info("The error in populateData");
            parseException.logException(e,true);
        }
    }
    /**
     * populate Other Bank & Other Bank Branch Details
     */
    private void populateOB(HashMap mapData) throws Exception{
        log.info("In populateOB...");
        ForexDenominationMasterTO objForexDenominationMasterTO = (ForexDenominationMasterTO) ((List) mapData.get("ForexDenominationMasterTO")).get(0);
        setForexDenominationMasterOB(objForexDenominationMasterTO);
        objForexDenominationMasterTO = null;
        ttNotifyObservers();
    }
    
}
