/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ForexExchangeOB.java
 *
 * Created on May 4, 2004, 4:59 PM
 */

package com.see.truetransact.ui.forex;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;


import com.see.truetransact.transferobject.forex.ForexExchangeTO;

import org.apache.log4j.Logger;

import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 *
 * @author  rahul
 */
public class ForexExchangeOB extends Observable{
    
    private HashMap lookUpHash;
    private HashMap keyValue;
    private HashMap operationMap;
    private ArrayList key;
    private ArrayList value;
    final ArrayList forexTabTitle = new ArrayList();
    private ArrayList forexTabRow;
    
    private ProxyFactory proxy = null;
    private EnhancedTableModel tblForexTab;
    
    private ComboBoxModel cbmTransCurrency;
    private ComboBoxModel cbmHours;
    private ComboBoxModel cbmMinutes;
    
    private int actionType;
    private int result;
    
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private String lblBaseCurrency;
    private String lblExchangeId;
    
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private final static Logger log = Logger.getLogger(ForexExchangeUI.class);
    
    // To get the Value of Column Title and Dialogue Box...
    final ForexExchangeRB objForexExchangeRB = new ForexExchangeRB();
    
    private static Date currDt = null;
    private static ForexExchangeOB forexExchangeOB;
    static {
        try {
            currDt = ClientUtil.getCurrentDate();
            log.info("In ForexExchangeOB Declaration");
            forexExchangeOB = new ForexExchangeOB();
        } catch(Exception e) {
            log.info("Error in ChequeBookOB Declaration");
        }
    }
    
    public static ForexExchangeOB getInstance() {
        return forexExchangeOB;
    }
    
    /** Creates a new instance of ForexExchangeOB */
    public ForexExchangeOB() throws Exception {
        initianSetup();
    }
    
    private void initianSetup() throws Exception{
        log.info("initianSetup");
        setOperationMap();
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            log.info(" Error In initianSetup()");
            parseException.logException(e,true);
        }
        fillDropdown();              // To Fill all the Combo Boxes
        setForexTabTitle();          // To set the Title of Table in Charges Tab...
        tblForexTab = new EnhancedTableModel(null, forexTabTitle);
    }
    
    // To set the Column title in Table...
    private void setForexTabTitle() throws Exception{
        log.info("In setForexTabTitle...");
        
        forexTabTitle.add(objForexExchangeRB.getString("tblColumn1"));
        forexTabTitle.add(objForexExchangeRB.getString("tblColumn2"));
        forexTabTitle.add(objForexExchangeRB.getString("tblColumn3"));
        forexTabTitle.add(objForexExchangeRB.getString("tblColumn4"));
    }
    
    // Set the value of JNDI and the Session Bean...
    private void setOperationMap() throws Exception{
        log.info("In setOperationMap()");
        
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "ForexExchangeJNDI");
        operationMap.put(CommonConstants.HOME, "forex.ForexExchangeHome");
        operationMap.put(CommonConstants.REMOTE, "forex.ForexExchange");
    }
    
    public void fillDropdown() throws Exception{
        log.info("fillDropdown");
        
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
        /*lookup_keys.add("OP_AC_PRODUCT");*/
        lookup_keys.add("FOREX.CURRENCY");
        lookup_keys.add("HOURS");
        lookup_keys.add("MINUTES");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        
        getKeyValue((HashMap)keyValue.get("FOREX.CURRENCY"));
        cbmTransCurrency = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("HOURS"));
        cbmHours= new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("MINUTES"));
        cbmMinutes= new ComboBoxModel(key,value);
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        log.info("getKeyValue");
        
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
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
        }
    }
    
    private void populateOB(HashMap mapData) throws Exception{
        log.info("In populateOB()");
        ForexExchangeTO objForexExchangeTO = null;
        objForexExchangeTO = (ForexExchangeTO) ((List) mapData.get("ForexExchangeTO")).get(0);
        setForexExchangeTO(objForexExchangeTO);
        ttNotifyObservers();
    }
    
    // To Enter the values in the UI fields, from the database...
    private void setForexExchangeTO(ForexExchangeTO objForexExchangeTO) throws Exception{
        log.info("In setForexExchangeTO()");
        //        setTxtExchangeId(objForexExchangeTO.getExchangeId());
        setTdtValidDate(DateUtil.getStringDate(objForexExchangeTO.getValueDate()));
        setCboTransCurrency((String) getCbmTransCurrency().getDataForKey(CommonUtil.convertObjToStr(objForexExchangeTO.getTransCurrency())));
        setTxtMiddleRate(CommonUtil.convertObjToStr(objForexExchangeTO.getMiddleRate()));
        setLblBaseCurrency(CommonUtil.convertObjToStr(objForexExchangeTO.getBaseCurrency()));
        setCboHours((String) getCbmHours().getDataForKey(CommonUtil.convertObjToStr(objForexExchangeTO.getValueHrs())));
        setCboMinutes((String) getCbmMinutes().getDataForKey(CommonUtil.convertObjToStr(objForexExchangeTO.getValueMins())));
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
                if( getCommand() != null ){
                    doActionPerform();
                }
            }
            else
                log.info("Action Type Not Defined In setInwardClearingTO()");
        } catch (Exception e) {
            log.info("Error In doAction()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
        }
    }
    
    public ForexExchangeTO setForexExchangeTO() {
        log.info("In setChequeBookStopPayment()");
        
        final ForexExchangeTO objForexExchangeTO = new ForexExchangeTO();
        try{
            objForexExchangeTO.setExchangeId(lblExchangeId);
//            objForexExchangeTO.setValueDate(DateUtil.getDateMMDDYYYY(tdtValidDate));
            Date dtDate = DateUtil.getDateMMDDYYYY(tdtValidDate);
            if(dtDate != null){
            Date Dt = (Date) currDt.clone();
            Dt.setDate(dtDate.getDate());
            Dt.setMonth(dtDate.getMonth());
            Dt.setYear(dtDate.getYear());
            objForexExchangeTO.setValueDate(Dt);
            }else{
                objForexExchangeTO.setValueDate(DateUtil.getDateMMDDYYYY(tdtValidDate));
            }
            objForexExchangeTO.setTransCurrency((String)cbmTransCurrency.getKeyForSelected());
            objForexExchangeTO.setMiddleRate(CommonUtil.convertObjToDouble(txtMiddleRate));
            objForexExchangeTO.setBaseCurrency(lblBaseCurrency);
            objForexExchangeTO.setValueHrs((String)cbmHours.getKeyForSelected());
            objForexExchangeTO.setValueMins((String)cbmMinutes.getKeyForSelected());
        }catch(Exception e){
            log.info("Error In setChequeBookLooseLeaf()");
            e.printStackTrace();
        }
        return objForexExchangeTO;
    }
    
    //To fill the Table at the Time of insert, update and delete...
    public void fillForexTab(String VALUEDATE){
        log.info("In fillForexTab()");
        try{
            final HashMap forexDataMap = new HashMap();
            forexDataMap.put("VALUEDATE",VALUEDATE);
            final ArrayList resultList = (ArrayList)ClientUtil.executeQuery("getForexExchangeTab", forexDataMap);
            setForexTab(resultList);
        }catch(Exception e){
            log.info("Error in fillForexTab...");
        }
    }
    
    private void setForexTab(ArrayList forexTabFill) throws Exception{
        log.info("In setChequeTab()");
        ForexExchangeTO objForexExchangeTO;
        if (forexTabFill != null){
            log.info("In setForexTab() After if before For...");
            for (int i=0, j= forexTabFill.size();i<j;i++){
                forexTabRow = new ArrayList();
                final HashMap resultMap = (HashMap)forexTabFill.get(i);
                
                forexTabRow.add(CommonUtil.convertObjToStr(resultMap.get("VALUE_DATE")));
                forexTabRow.add(CommonUtil.convertObjToStr(resultMap.get("BASE_CURRENCY")));
                forexTabRow.add(CommonUtil.convertObjToStr(resultMap.get("TRANS_CURRENCY")));
                forexTabRow.add(CommonUtil.convertObjToStr(resultMap.get("MIDDLE_RATE")));
                
                tblForexTab.insertRow(0,forexTabRow);
            }
        }
        ttNotifyObservers();
    }
    
    //TO RESET THE TABLE...
    public void resetTable(){
        try{
            ArrayList data = tblForexTab.getDataArrayList();
            for(int i=data.size(); i>0; i--)
                tblForexTab.removeRow(i-1);
            setChanged();
            notifyObservers();
        }catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            log.info("Error in resetTable():");
        }
    }
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        log.info("In doActionPerform()");
        final HashMap data = new HashMap();
        final ForexExchangeTO objForexExchangeTO = setForexExchangeTO();
        objForexExchangeTO.setCommand(getCommand());
        data.put("ForexExchangeTO",objForexExchangeTO);
        
        HashMap proxyResultMap = proxy.execute(data,operationMap);
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
    
    public void resetForm(){
        setTdtValidDate("");
        setCboTransCurrency("");
        setTxtMiddleRate("");
        setCboHours("");
        setCboMinutes("");
        ttNotifyObservers();
    }
    
    // To reset the Lables in UI...
    public void resetLable(){
        this.setLblExchangeId("");
        this.setLblBaseCurrency("");
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
        notifyObservers();
    }
    
    
    
    private String tdtValidDate = "";
    private String cboTransCurrency = "";
    private String txtMiddleRate = "";
    private String cboHours = "";
    private String cboMinutes = "";
    
    // Setter method for tdtValidDate
    void setTdtValidDate(String tdtValidDate){
        this.tdtValidDate = tdtValidDate;
        setChanged();
    }
    // Getter method for tdtValidDate
    String getTdtValidDate(){
        return this.tdtValidDate;
    }
    
    // Setter method for cboTransCurrency
    void setCboTransCurrency(String cboTransCurrency){
        this.cboTransCurrency = cboTransCurrency;
        setChanged();
    }
    // Getter method for cboTransCurrency
    String getCboTransCurrency(){
        return this.cboTransCurrency;
    }
    
    // Set and the get methods for ComboBox models...
    public void setCbmTransCurrency(ComboBoxModel cbmTransCurrency){
        this.cbmTransCurrency = cbmTransCurrency;
        setChanged();
    }
    
    ComboBoxModel getCbmTransCurrency(){
        return cbmTransCurrency;
    }
    
    // Setter method for txtMiddleRate
    void setTxtMiddleRate(String txtMiddleRate){
        this.txtMiddleRate = txtMiddleRate;
        setChanged();
    }
    // Getter method for txtMiddleRate
    String getTxtMiddleRate(){
        return this.txtMiddleRate;
    }
    
    // Setter method for cboHours
    void setCboHours(String cboHours){
        this.cboHours = cboHours;
        setChanged();
    }
    // Getter method for cboHours
    String getCboHours(){
        return this.cboHours;
    }
    
    // Set and the get methods for ComboBox models...
    public void setCbmHours(ComboBoxModel cbmHours){
        this.cbmHours = cbmHours;
        setChanged();
    }
    
    ComboBoxModel getCbmHours(){
        return cbmHours;
    }
    
    // Setter method for cboMinutes
    void setCboMinutes(String cboMinutes){
        this.cboMinutes = cboMinutes;
        setChanged();
    }
    // Getter method for cboMinutes
    String getCboMinutes(){
        return this.cboMinutes;
    }
    
    // Set and the get methods for ComboBox models...
    public void setCbmMinutes(ComboBoxModel cbmMinutes){
        this.cbmMinutes = cbmMinutes;
        setChanged();
    }
    
    ComboBoxModel getCbmMinutes(){
        return cbmMinutes;
    }
    
    public void setLblBaseCurrency(String lblBaseCurrency){
        this.lblBaseCurrency = lblBaseCurrency;
        setChanged();
    }
    public String getLblBaseCurrency(){
        return this.lblBaseCurrency;
    }
    
    public void setLblExchangeId(String lblExchangeId){
        this.lblExchangeId = lblExchangeId;
        setChanged();
    }
    public String getLblExchangeId(){
        return this.lblExchangeId;
    }
    
    //Set and get for The Table...
    void setTblForex(EnhancedTableModel tblForexTab){
        this.tblForexTab = tblForexTab;
        setChanged();
    }
    
    EnhancedTableModel getTblForex(){
        return this.tblForexTab;
    }
    
    public void setBaseCurrency(){
        try {
            final HashMap currencyMap = new HashMap();
            final List resultList = ClientUtil.executeQuery("getBaseCurrency", null);
            final HashMap resultMap = (HashMap)resultList.get(0);
            
            final String PRODCURRENCY = CommonUtil.convertObjToStr(resultMap.get("BASE_CURRENCY"));
            currencyMap.put("PRODCURRENCY",PRODCURRENCY);
            final List currencyList = ClientUtil.executeQuery("getProdCurrency", currencyMap);
            final HashMap baseCurrencyMap = (HashMap)currencyList.get(0);
            
            setLblBaseCurrency(CommonUtil.convertObjToStr(baseCurrencyMap.get("LOOKUP_DESC")));
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    
}
