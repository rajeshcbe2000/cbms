/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * GoldConfigurationOB.java
 *
 * Created on Wed Feb 02 12:57:50 IST 2005
 */

package com.see.truetransact.ui.termloan.goldLoanConfiguration;

import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.transferobject.termloan.goldLoanConfiguration.GoldConfigurationTO;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.ComboBoxModel;

import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientproxy.ProxyParameters;

import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * @author Sathiya
 */

public class GoldConfigurationOB extends CObservable{
    private HashMap operationMap;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private ArrayList overAllList = new ArrayList(); 
    private ArrayList deleteAllList;
    private ComboBoxModel cbmPurityOfGold;
    private EnhancedTableModel tblGoldTab;
    
    private long referenceNo = 0;
    private long setNo = 0;
    private String cboPurityOfGold;
    private String tdtFromDate = "";
    private String tdtToDate = "";
    private String txtPerGramRate = "";
    private String authStatus = "";
    //__ ArrayLists for the Gold Table...
    ArrayList goldTabTitle = new ArrayList();
    private ArrayList goldTabRow;
    
    private int actionType;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger log = Logger.getLogger(GoldConfigurationUI.class); 
    private ProxyFactory proxy = null;
    private Date curDate = null;
    private String defaultItem = "";
    //    private final String BRANCH = TrueTransactMain.BRANCH_ID;
    
    // To get the Value of Column Title and Dialogue Box...
    //    final GoldRB goldConfigurationOB = new GoldRB();
    java.util.ResourceBundle objGoldConfigurationRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.goldLoanConfiguration.GoldConfigurationRB", ProxyParameters.LANGUAGE);
    
    //    private static GoldConfigurationOB goldConfigurationOB;
    //    static {
    //        try {
    //            log.info("In goldConfigurationOB Declaration");
    //            goldConfigurationOB = new GoldConfigurationOB();
    //        } catch(Exception e) {
    //            parseException.logException(e,true);
    //        }
    //    }
    
    public static GoldConfigurationOB getInstance() throws Exception{
//        try {
            GoldConfigurationOB goldConfigurationOB = new GoldConfigurationOB();
            return goldConfigurationOB;
//        } catch(Exception e) {
//            parseException.logException(e,true);
//        }
    }
    
    /** Creates a new instance of InwardClearingOB */
    public GoldConfigurationOB() throws Exception {
        curDate = ClientUtil.getCurrentDate();
        cbmPurityOfGold = new ComboBoxModel();
        initianSetup();
        fillDropdown();
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
        
        setGoldConfigurationTabTitle();   //__ To set the Title of Table in 
        tblGoldTab = new EnhancedTableModel(null, goldTabTitle);
        //        overAllList = new ArrayList();
        deleteAllList = new ArrayList();
    }
    
    // To set the Column title in Table...
    private void setGoldConfigurationTabTitle() throws Exception{
        log.info("In setGoldTabTitle...");
        
        goldTabTitle.add(objGoldConfigurationRB.getString("tblColumn1"));
        goldTabTitle.add(objGoldConfigurationRB.getString("tblColumn2"));
        goldTabTitle.add(objGoldConfigurationRB.getString("tblColumn3"));
        goldTabTitle.add(objGoldConfigurationRB.getString("tblColumn4"));
        goldTabTitle.add(objGoldConfigurationRB.getString("tblColumn5"));
        goldTabTitle.add(objGoldConfigurationRB.getString("tblColumn6"));
    }
    
    // Set the value of JNDI and the Session Bean...
    private void setOperationMap() throws Exception{
        log.info("In setOperationMap()");
        
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "GoldConfigurationJNDI");
        operationMap.put(CommonConstants.HOME, "termloan.goldLoanConfiguration.GoldConfigurationHome");
        operationMap.put(CommonConstants.REMOTE, "termloan.goldLoanConfiguration.GoldConfiguration");
    }
    
    //Do display the Data from the Database, in UI
    public void populateData(HashMap whereMap) {
        log.info("In populateData()");
        final HashMap mapData;
        try {
            whereMap.put("BRANCHID",TrueTransactMain.BRANCH_ID);
            mapData = proxy.executeQuery(whereMap, operationMap);
            mapData.put("PURITY_OF_GOLD",whereMap.get("PURITY_OF_GOLD"));
            mapData.put("SET_NO",whereMap.get("SET_NO"));
            populateOB(mapData);
        } catch( Exception e ) {
            System.out.println("Error In populateData()");
            parseException.logException(e,true);
        }
    }
    
    private void populateOB(HashMap mapData) throws Exception{
        GoldConfigurationTO objGoldConfigurationTO = null;
        overAllList = new ArrayList();        //Taking the Value of Prod_Id from each Table...
        overAllList = ((ArrayList) mapData.get("GoldConfigurationTO"));
        setGoldTableData(overAllList);
        if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || getActionType() == ClientConstants.ACTIONTYPE_REJECT){
            if(mapData.containsKey("PURITY_OF_GOLD") && mapData.containsKey("SET_NO")){
                int row = -1;
                int setRow = new Integer(mapData.get("SET_NO").toString()).intValue();
                if(mapData.get("PURITY_OF_GOLD").equals("18CT")){
                    row = (setRow * 3) - 3;
                }else if(mapData.get("PURITY_OF_GOLD").equals("20CT")){
                    row = (setRow * 3) - 2;
                }else if(mapData.get("PURITY_OF_GOLD").equals("22CT")){
                    row = (setRow * 3) - 1;
                }
                setSetNo(setRow);
                populateallValues(row);                
            }
        }
        ttNotifyObservers();
    }
    
    // To Enter the values in the UI fields, from the database...
    private void setGoldConfigurationTO(GoldConfigurationTO objGoldConfigurationTO) throws Exception{
        log.info("In setGoldTO()");
        objGoldConfigurationTO.setBranchId(getSelectedBranchID());
        
        setTdtFromDate(CommonUtil.convertObjToStr(objGoldConfigurationTO.getFromDate()));
        setCboPurityOfGold(objGoldConfigurationTO.getPurityOfGold());
        setTxtPerGramRate(CommonUtil.convertObjToStr(objGoldConfigurationTO.getPerGramRate()));
    }
    
    public void setGoldTableData(List lst){
        for (int i=0 ; i < lst.size() ; i++){
            System.out.println("Gold Data Exists...");
            goldTabRow = new ArrayList();
            GoldConfigurationTO objGoldConfigurationTO = (GoldConfigurationTO)lst.get(i);
            objGoldConfigurationTO.setCommand("UPDATE");
            long setNo = objGoldConfigurationTO.getSetNo().longValue();
            if(i == 0){
                setSetNo(setNo);
            }
            goldTabRow.add(new Long(setNo));
            goldTabRow.add(CommonUtil.convertObjToStr(objGoldConfigurationTO.getFromDate()));
            goldTabRow.add(CommonUtil.convertObjToStr(objGoldConfigurationTO.getToDate()));
            goldTabRow.add(CommonUtil.convertObjToStr(objGoldConfigurationTO.getPurityOfGold()));
            goldTabRow.add(CommonUtil.convertObjToStr(objGoldConfigurationTO.getPerGramRate()));
            goldTabRow.add(CommonUtil.convertObjToStr(objGoldConfigurationTO.getAuthorizedStatus()));
            tblGoldTab.addRow(goldTabRow);
            goldTabRow = null;
        }
    }
    
    private GoldConfigurationTO setGoldConfigurationTO() {
        log.info("In setGoldDetails()");
        
        final GoldConfigurationTO objGoldConfigurationTO = new GoldConfigurationTO();
        try{
            if(getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                objGoldConfigurationTO.setCommand("UPDATE");
            }else if(getActionType() == ClientConstants.ACTIONTYPE_NEW){
                objGoldConfigurationTO.setCommand("INSERT");
            }
            objGoldConfigurationTO.setReferenceNo(new Long(getReferenceNo()));
            objGoldConfigurationTO.setSetNo(new Long(getSetNo()));
            objGoldConfigurationTO.setPurityOfGold(getCboPurityOfGold());
            objGoldConfigurationTO.setPerGramRate(new Double(getTxtPerGramRate()));          
            objGoldConfigurationTO.setStatus(CommonConstants.STATUS_CREATED);
            objGoldConfigurationTO.setStatusBy(TrueTransactMain.BRANCH_ID);
            objGoldConfigurationTO.setStatusDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(curDate.clone())));
            Date AppDt = DateUtil.getDateMMDDYYYY(getTdtFromDate());
            if(AppDt != null){
                Date appDate = (Date) curDate.clone();
                appDate.setDate(AppDt.getDate());
                appDate.setMonth(AppDt.getMonth());
                appDate.setYear(AppDt.getYear());
                objGoldConfigurationTO.setFromDate(appDate);
            }else{
                objGoldConfigurationTO.setFromDate(DateUtil.getDateMMDDYYYY(getTdtFromDate()));            
            }
            objGoldConfigurationTO.setToDate(DateUtil.getDateMMDDYYYY(getTdtToDate()));
            objGoldConfigurationTO.setBranchId(getSelectedBranchID());
            objGoldConfigurationTO.setDefaultItem(getDefaultItem());
            AppDt = null;
        }catch(Exception e){
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objGoldConfigurationTO;
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
        final GoldConfigurationTO objGoldConfigurationTO = setGoldConfigurationTO();
        data.put("COMMAND",getCommand());
        data.put("REFERENCE_NO",new Long(getReferenceNo()));
        data.put("CURR_DT",curDate.clone());
        data.put("SET_NO",new Long(getSetNo()));
        if(overAllList!=null && overAllList.size()>0){
            data.put("GoldConfigurationTO",overAllList);
        }
        if(deleteAllList != null && deleteAllList.size()>0){
            data.put("deleteGoldConfigurationTO",deleteAllList);
        }
        HashMap proxyResultMap = proxy.execute(data,operationMap);
        overAllList = new ArrayList();
        setResult(actionType);
        setProxyReturnMap(proxyResultMap);
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
        setCboPurityOfGold("");
        setTdtFromDate("");
        setTdtToDate("");
        setTxtPerGramRate("");
//        overAllList = new ArrayList();
    }
    
    public void removeDeleteValues(int rowCount){
        try{
            log.info("In setGoldTO()");
            GoldConfigurationTO objGoldConfigurationTO = (GoldConfigurationTO)overAllList.get(rowCount);
            objGoldConfigurationTO.setBranchId(getSelectedBranchID());
            objGoldConfigurationTO.setStatus(CommonConstants.STATUS_DELETED);
            deleteAllList.add(objGoldConfigurationTO);
            overAllList.remove(rowCount);
            tblGoldTab.removeRow(rowCount);
            tblGoldTab.fireTableDataChanged();
        }catch(Exception e){
            System.out.println(""+e);
        }
    }
    
    public void populateallValues(int rowCount){
        try{
            log.info("In setGoldTO()");
            GoldConfigurationTO objGoldConfigurationTO = (GoldConfigurationTO)overAllList.get(rowCount);
            objGoldConfigurationTO.setBranchId(getSelectedBranchID());
            setTdtFromDate(CommonUtil.convertObjToStr(objGoldConfigurationTO.getFromDate()));
            setTdtToDate(CommonUtil.convertObjToStr(objGoldConfigurationTO.getToDate()));
            setCboPurityOfGold(objGoldConfigurationTO.getPurityOfGold());
            setTxtPerGramRate(CommonUtil.convertObjToStr(objGoldConfigurationTO.getPerGramRate()));
            setAuthStatus(CommonUtil.convertObjToStr(objGoldConfigurationTO.getAuthorizedStatus()));
            setDefaultItem(CommonUtil.convertObjToStr(objGoldConfigurationTO.getDefaultItem()));
        }catch(Exception e){
            System.out.println(""+e);
        }
    }
    
    public void setGoldTabData(int selectedRec){
        System.out.println("Gold Data Exists...");
        goldTabRow = new ArrayList();
        ArrayList selectedList = new ArrayList();
        if(selectedRec == -1){
            if(getActionType() == ClientConstants.ACTIONTYPE_NEW && tblGoldTab.getRowCount() == 0){
                overAllList = new ArrayList();
            }
            GoldConfigurationTO goldList = setGoldConfigurationTO();
            selectedList.add("");
            selectedList.add(CommonUtil.convertObjToStr(getTdtFromDate()));
            selectedList.add(CommonUtil.convertObjToStr(getTdtToDate()));
            selectedList.add(CommonUtil.convertObjToStr(getCboPurityOfGold()));
            selectedList.add(CommonUtil.convertObjToStr(getTxtPerGramRate()));
            selectedList.add("");
            overAllList.add(goldList);
            tblGoldTab.addRow(selectedList);
        }else{
            GoldConfigurationTO goldList = setGoldConfigurationTO();
            selectedList.add(new Long(getSetNo()));
            selectedList.add(CommonUtil.convertObjToStr(getTdtFromDate()));
            selectedList.add(CommonUtil.convertObjToStr(getTdtToDate()));
            selectedList.add(CommonUtil.convertObjToStr(getCboPurityOfGold()));
            selectedList.add(CommonUtil.convertObjToStr(getTxtPerGramRate()));
            selectedList.add("");
            overAllList.set(selectedRec, goldList);
            tblGoldTab.removeRow(selectedRec);
            tblGoldTab.insertRow(selectedRec,selectedList);
        }
        goldTabRow = null;
    }
    
    /* Set and GET METHODS FOR THE tABLE...*/
    void setTblGold(EnhancedTableModel tblGoldTab){
        this.tblGoldTab = tblGoldTab;
        setChanged();
    }
    
    EnhancedTableModel getTblGold(){
        return this.tblGoldTab;
    }
    
    /** TO RESET THE TABLE...*/
    public void resetTable(){
        try{
            ArrayList data = tblGoldTab.getDataArrayList();
            for(int i=data.size(); i>0; i--)
                tblGoldTab.removeRow(i-1);
            tblGoldTab.setDataArrayList(null,goldTabTitle);
        }catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            log.info("Error in resetTable():");
        }
    }
    
    public void fillDropdown(){
        try{
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME,null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("GOLD_CONFIGURATION");
            
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get("GOLD_CONFIGURATION"));
            this.cbmPurityOfGold = new ComboBoxModel(key,value);
            
        }catch(NullPointerException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    /**
     * Getter for property cbmPurityOfGold.
     * @return Value of property cbmPurityOfGold.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPurityOfGold() {
        return cbmPurityOfGold;
    }
    
    /**
     * Setter for property cbmPurityOfGold.
     * @param cbmPurityOfGold New value of property cbmPurityOfGold.
     */
    public void setCbmPurityOfGold(com.see.truetransact.clientutil.ComboBoxModel cbmPurityOfGold) {
        this.cbmPurityOfGold = cbmPurityOfGold;
    }
    
    /**
     * Getter for property cboPurityOfGold.
     * @return Value of property cboPurityOfGold.
     */
    public java.lang.String getCboPurityOfGold() {
        return cboPurityOfGold;
    }
    
    /**
     * Setter for property cboPurityOfGold.
     * @param cboPurityOfGold New value of property cboPurityOfGold.
     */
    public void setCboPurityOfGold(java.lang.String cboPurityOfGold) {
        this.cboPurityOfGold = cboPurityOfGold;
    }
    
    /**
     * Getter for property txtPerGramRate.
     * @return Value of property txtPerGramRate.
     */
    public java.lang.String getTxtPerGramRate() {
        return txtPerGramRate;
    }
    
    /**
     * Setter for property txtPerGramRate.
     * @param txtPerGramRate New value of property txtPerGramRate.
     */
    public void setTxtPerGramRate(java.lang.String txtPerGramRate) {
        this.txtPerGramRate = txtPerGramRate;
    }
       
    /**
     * Getter for property tdtFromDate.
     * @return Value of property tdtFromDate.
     */
    public java.lang.String getTdtFromDate() {
        return tdtFromDate;
    }
    
    /**
     * Setter for property tdtFromDate.
     * @param tdtFromDate New value of property tdtFromDate.
     */
    public void setTdtFromDate(java.lang.String tdtFromDate) {
        this.tdtFromDate = tdtFromDate;
    }
    
    /**
     * Getter for property tdtToDate.
     * @return Value of property tdtToDate.
     */
    public java.lang.String getTdtToDate() {
        return tdtToDate;
    }
    
    /**
     * Setter for property tdtToDate.
     * @param tdtToDate New value of property tdtToDate.
     */
    public void setTdtToDate(java.lang.String tdtToDate) {
        this.tdtToDate = tdtToDate;
    }
    
    /**
     * Getter for property authStatus.
     * @return Value of property authStatus.
     */
    public java.lang.String getAuthStatus() {
        return authStatus;
    }
    
    /**
     * Setter for property authStatus.
     * @param authStatus New value of property authStatus.
     */
    public void setAuthStatus(java.lang.String authStatus) {
        this.authStatus = authStatus;
    }
    
    /**
     * Getter for property referenceNo.
     * @return Value of property referenceNo.
     */
    public long getReferenceNo() {
        return referenceNo;
    }
    
    /**
     * Setter for property referenceNo.
     * @param referenceNo New value of property referenceNo.
     */
    public void setReferenceNo(long referenceNo) {
        this.referenceNo = referenceNo;
    }
    
    /**
     * Getter for property setNo.
     * @return Value of property setNo.
     */
    public long getSetNo() {
        return setNo;
    }
    
    /**
     * Setter for property setNo.
     * @param setNo New value of property setNo.
     */
    public void setSetNo(long setNo) {
        this.setNo = setNo;
    }

    public String getDefaultItem() {
        return defaultItem;
    }

    public void setDefaultItem(String defaultItem) {
        this.defaultItem = defaultItem;
    }
    
}