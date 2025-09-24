/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OtherBankOB.java
 *
 * Created on Thu Dec 30 16:06:04 IST 2004
 */

package com.see.truetransact.ui.sysadmin.stateTalukMaster;

import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
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
import java.util.Date;
import java.util.Observable;
import org.apache.log4j.Logger;
import com.see.truetransact.transferobject.sysadmin.stateTalukMaster.StateTalukTO;
import com.see.truetransact.uicomponent.CObservable;
import java.lang.Object;

/**
 *
 * @author  152715
 */

public class StateTalukOB extends CObservable{
   
    private String authorizeStatus1 = "";
    private String authorizeBy = "";
    private String authorizeDt = "";
    
    private String txtTalukName = "";
    private String txtTalukCode = "";
    private String txtDistrictName = "";
    private String txtDistrictCode = "";
     private String txtStateName = "";
    private String txtStateCode = "";
    
    private HashMap lookUpHash;
    private HashMap keyValue;
    
    private ArrayList key;
    private ArrayList value;
    private ArrayList rowData;
    private static int serialNo = 1;// To maintain serial No in Other Bank Branch Details Table
    private static int count = 1;// To maintain No of Other Bank Branch Details deleted
    
    
    private StateTalukTO objStateTalukTO;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int actionType;
    private int result;
    HashMap operationMap;
    final ArrayList tableTitle = new ArrayList();
    final ArrayList tableTitleState = new ArrayList();
    ProxyFactory proxy;
    private EnhancedTableModel tblDistrict;
    private EnhancedTableModel tblStateDis;
    private LinkedHashMap otherBankBranchTO = null;// Contains Other Bank Branch Details which the Status is not DELETED
    private LinkedHashMap deletedOtherBankBranchTO = null;// Contains Other Bank Branch Details which the Status is DELETED
    private LinkedHashMap totalOtherBankBranchTO = null;// Contains Both Other Bank Details and Other Bank Branch Details
    private final String TO_DELETED_AT_UPDATE_MODE = "TO_DELETED_AT_UPDATE_MODE";
    private final String TO_NOT_DELETED_AT_UPDATE_MODE = "TO_NOT_DELETED_AT_UPDATE_MODE";
    private LinkedHashMap incParMap;
    private boolean newIncomeDet = false;
    private String txtSlNo="";
    private LinkedHashMap deletedIncomeparMap;
    private ArrayList IncVal = new ArrayList();
    private ArrayList IncValState = new ArrayList();
    private LinkedHashMap StateMap;
    private String txtStateSlNo="";
    private LinkedHashMap deletedStateMap;
    
    
    
    final StateTalukRB objStateTalukRB = new StateTalukRB();
    
    
    private static StateTalukOB objStateTalukOB; // singleton object
    
    private final static Logger log = Logger.getLogger(StateTalukOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    static {
        try {
            log.info("Creating StateTalukOB...");
            objStateTalukOB = new StateTalukOB();
        } catch(Exception e) {
            //_log.error(e);
            parseException.logException(e,true);
        }
    }
    
    /**
     * Returns an instance of OtherBankOB.
     * @return  OtherBankOB
     */
    public static StateTalukOB getInstance() {
        return objStateTalukOB;
    }
    /** Creates a new instance of CashManagementOB */
    private StateTalukOB() throws Exception{
        proxy = ProxyFactory.createProxy();
        setOperationMap();
        setTableTile();
        setTableTileState();
        tblDistrict = new EnhancedTableModel(null, tableTitle);
        tblStateDis = new EnhancedTableModel(null, tableTitleState);
        serialNo = 1;
        count =1;
    }
    
    /* Sets _subHeadTitle with table column headers */
    private void setTableTile() throws Exception{
        tableTitle.add(objStateTalukRB.getString("tblColumn1"));
        tableTitle.add(objStateTalukRB.getString("tblColumn2"));
        tableTitle.add(objStateTalukRB.getString("tblColumn3"));
        tableTitle.add(objStateTalukRB.getString("tblColumn4"));
        tableTitle.add(objStateTalukRB.getString("tblColumn5"));
        tableTitle.add(objStateTalukRB.getString("tblColumn10"));
        tableTitle.add(objStateTalukRB.getString("tblColumn12"));
        tableTitle.add(objStateTalukRB.getString("tblColumn13"));
        IncVal = new ArrayList();
    }
     private void setTableTileState() throws Exception{
        tableTitleState.add(objStateTalukRB.getString("tblColumn6"));
        tableTitleState.add(objStateTalukRB.getString("tblColumn7"));
        tableTitleState.add(objStateTalukRB.getString("tblColumn8"));
        tableTitleState.add(objStateTalukRB.getString("tblColumn9"));
        tableTitleState.add(objStateTalukRB.getString("tblColumn11"));  
        tableTitleState.add(objStateTalukRB.getString("tblColumn14"));
        tableTitleState.add(objStateTalukRB.getString("tblColumn15"));
        IncValState = new ArrayList();
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
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    void setTblDistrict(EnhancedTableModel tblDistrict){
        this.tblDistrict = tblDistrict;
        setChanged();
    }
    EnhancedTableModel getTblDistrict(){
        return this.tblDistrict;
    }
    
    /* Sets the HashMap required to set JNDI,Home and Remote*/
    private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "StateTalukJNDI");
        operationMap.put(CommonConstants.HOME, "com.see.truetransact.serverside.sysadmin.stateTalukMaster.StateTalukHome");
        operationMap.put(CommonConstants.REMOTE, "com.see.truetransact.serverside.sysadmin.stateTalukMaster.StateTaluk");
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
    // Setter method for authorizeStatus
    void setAuthorizeStatus1(String authorizeStatus){
        this.authorizeStatus1 = authorizeStatus;
        setChanged();
    }
    // Getter method for authorizeStatus
    String getAuthorizeStatus1(){
        return this.authorizeStatus1;
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
    // Setter method for authorizeDt
    void setAuthorizeDt(String authorizeDt){
        this.authorizeDt = authorizeDt;
        setChanged();
    }
    // Getter method for authorizeDt
    String getAuthorizeDt(){
        return this.authorizeDt;
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
    
    /**
     * Set Other Bank fields in the OB
     */
    private void setStateTalukOB(StateTalukTO objStateTalukTO) throws Exception {
        log.info("Inside setStateTalukOB()");
    }
    /**
     * To set the data in OtherBankTO TO
     */
    public StateTalukTO setStateTalukTO() {
        log.info("In setStateTalukTO...");
        
        final StateTalukTO objStateTalukTO = new StateTalukTO();
        try{ 
            objStateTalukTO.setStatus(CommonUtil.convertObjToStr(getCommand()));
            objStateTalukTO.setStateCode(CommonUtil.convertObjToInt(getTxtStateCode()));
            objStateTalukTO.setStateName(CommonUtil.convertObjToInt(getTxtStateName()));
            objStateTalukTO.setDisCode(CommonUtil.convertObjToStr(getTxtDistrictCode()));
            objStateTalukTO.setDisName(CommonUtil.convertObjToStr(getTxtDistrictName()));
            objStateTalukTO.setTalukCode(CommonUtil.convertObjToStr(getTxtTalukCode()));
            objStateTalukTO.setTalukName(CommonUtil.convertObjToStr(getTxtTalukName())); 
            objStateTalukTO.setTalukStatus(CommonUtil.convertObjToStr(getCommand()));
            objStateTalukTO.setDisStatus(CommonUtil.convertObjToStr(getCommand()));
            objStateTalukTO.setSateStatusBy(TrueTransactMain.USER_ID);
            objStateTalukTO.setDisStatusBy(TrueTransactMain.USER_ID);
            objStateTalukTO.setTalStatusBy(TrueTransactMain.USER_ID);
            objStateTalukTO.setStateCreatedBy(TrueTransactMain.USER_ID);
            objStateTalukTO.setDisCreatedBy(TrueTransactMain.USER_ID);
            objStateTalukTO.setTalCreatedBy(TrueTransactMain.USER_ID);
            
        }catch(Exception e){
            log.info("Error in setStateTalukTO()");
            parseException.logException(e,true);
        }
        return objStateTalukTO;
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
//                    throw new TTException(objstateTalukOB.getString("TOCommandError"));
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
        final StateTalukTO objStateTalukTO = setStateTalukTO();
        objStateTalukTO.setCommand(getCommand());
       
        if(getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            if (deletedOtherBankBranchTO != null) {
                totalOtherBankBranchTO.put(TO_DELETED_AT_UPDATE_MODE, deletedOtherBankBranchTO);
            }
        }
//        totalOtherBankBranchTO.put(TO_NOT_DELETED_AT_UPDATE_MODE, otherBankBranchTO);
        
        final HashMap data = new HashMap();
        objStateTalukTO.setStateStatusDt(ClientUtil.getCurrentDate());
        objStateTalukTO.setBranCode(TrueTransactMain.BRANCH_ID);
        if(isNewIncomeDet()){
            objStateTalukTO.setTalukStatus(CommonConstants.STATUS_CREATED);
        }
        else{ 
          objStateTalukTO.setTalukStatus(CommonConstants.STATUS_MODIFIED);
        }
        data.put("StateTalukTO",objStateTalukTO);
        data.put("TalukDetailsTO",incParMap);
        data.put("DistrictDetailsTO",StateMap);
        data.put("deletedTalukDetails",deletedIncomeparMap);
        data.put("deletedDistrictDetails",deletedStateMap);
        data.put("MODE",getCommand());// To Maintain the Status CREATED, MODIFIED, DELETED
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        System.out.println("doactionperform######"+data);
        HashMap proxyResultMap = proxy.execute(data,operationMap);
        setResult(actionType);
        actionType = ClientConstants.ACTIONTYPE_CANCEL;
        resetForm();
    }
  
    /**
     * To reset all fields
     */
    public void resetForm() {
        resetState();
        resetTaluk();
        resetDistrict();
        tblDistrict.setDataArrayList(null,tableTitle);
        tblStateDis.setDataArrayList(null,tableTitleState);
        incParMap=null;
        StateMap=null;
        deletedIncomeparMap=null;
        deletedStateMap=null;
        ttNotifyObservers();
    }
    /**
     * To Reset Other Bank details
     */
    public void resetState() {
        setTxtStateCode("");
        setTxtStateName("");
        ttNotifyObservers();
    }
    public void resetDistrict() {
       setTxtDistrictCode("");
       setTxtDistrictName("");
       ttNotifyObservers();
    }
     public void resetTaluk() {
       setTxtTalukCode("");
       setTxtTalukName("");
       ttNotifyObservers();
    }
   
    
  
    //Do display the Data from the Database, in UI
    public void populateData(HashMap whereMap) {
        log.info("In populateData...");
        whereMap.put("AUTHSTATUS",String.valueOf(getActionType()));
        System.out.println("@@@whereMap"+whereMap);
        final HashMap mapData;
        try {
            mapData = (HashMap)proxy.executeQuery(whereMap, operationMap);
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
        StateTalukTO objStateTalukTO = null;
//        StateTalukTO objStateTO = null;
//        objStateTO = (StateTalukTO) mapData.get("StateDisTO");
//        setStateData(objStateTO);
        if(mapData.containsKey("StateDisTO"))
        {
           StateMap = (LinkedHashMap)mapData.get("StateDisTO");
                ArrayList addList =new ArrayList(StateMap.keySet());
                for(int i=0;i<addList.size();i++){
                   objStateTalukTO = (StateTalukTO)  StateMap.get(addList.get(i));
                    objStateTalukTO.setStatus(CommonConstants.STATUS_MODIFIED);
                     ArrayList incTabRow = new ArrayList();
                 incTabRow.add(CommonUtil.convertObjToStr(objStateTalukTO.getStateSlno()));
                incTabRow.add(CommonUtil.convertObjToStr(objStateTalukTO.getStateCode()));
                incTabRow.add(CommonUtil.convertObjToStr(objStateTalukTO.getDisCode()));
                incTabRow.add(CommonUtil.convertObjToStr(objStateTalukTO.getDisName()));
                incTabRow.add(CommonUtil.convertObjToStr(objStateTalukTO.getDisStatus()));
                incTabRow.add(CommonUtil.convertObjToStr(objStateTalukTO.getDisAuthorizeStatus()));
                 if (getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE||getActionType()==ClientConstants.ACTIONTYPE_REJECT)
                  incTabRow.add("NO");
                else
                 incTabRow.add("");
                tblStateDis.addRow(incTabRow); 
                }
        }
        
        if(mapData.containsKey("TalukTO"))
        {
              incParMap = (LinkedHashMap)mapData.get("TalukTO");
                ArrayList addList =new ArrayList(incParMap.keySet());
//            for(int i=0;i<addList.size();i++)
//            {
//                objStateTalukTO = (StateTalukTO) incParMap.get(addList.get(i));
//                ArrayList insTable = new ArrayList();
//                insTable.add(CommonUtil.convertObjToStr(objStateTalukTO.getSlno()));
//                insTable.add(CommonUtil.convertObjToStr(objStateTalukTO.getDisCode()));
//                insTable.add(CommonUtil.convertObjToStr(objStateTalukTO.getDisName()));
//                insTable.add(CommonUtil.convertObjToStr(objStateTalukTO.getTalukCode()));
//                insTable.add(CommonUtil.convertObjToStr(objStateTalukTO.getTalukName()));
//                insTable.add(CommonUtil.convertObjToStr(objStateTalukTO.getTalukStatus()));
//                insTable.add(CommonUtil.convertObjToStr(objStateTalukTO.getTalAuthorizeStatus()));
//                if (getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE||getActionType()==ClientConstants.ACTIONTYPE_REJECT)
//                  insTable.add("NO");
//                else
//                 insTable.add("");   
//                tblDistrict.addRow(insTable);
//            }
//             objStateTalukTO=null;
        }
        
        ttNotifyObservers();
    }
    
    public void  setStateData(StateTalukTO objStateTalukTO){
       setTxtStateCode(CommonUtil.convertObjToStr(objStateTalukTO.getStateCode()));
       setTxtStateName(CommonUtil.convertObjToStr(objStateTalukTO.getStateName())); 
    }
    /**
     * To Populate OtherBankBranch Details
     */
    
    /**
     * If all the fields contains value then enable New button
     */
    public boolean enableNew(String stateCode) {
        boolean flag = false;
        try {
            if (stateCode != null && stateCode.length() > 0) {
                flag = true;
            } else {
                flag = false;
            }
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        return flag;
    }
    /**
     * populate Other Bank Branch Details when table is pressed
     */
//    public boolean populateOtherBankBranch(int row) {
//        boolean flag = false;
//        log.info("populateOtherBankBranch Invoked...");
//        try {
//            OtherBankBranchTO objOtherBankBranchTO = (OtherBankBranchTO) otherBankBranchTO.get(String.valueOf(row+1));
//            if ((objOtherBankBranchTO.getStatus() != null) && (objOtherBankBranchTO.getStatus().length() > 0) && !(objOtherBankBranchTO.getStatus().equals(""))) {
//                flag = false;
//            } else {
//                flag = true;
//            }
//            setOtherBankBranchOB(objOtherBankBranchTO);
//        } catch(Exception e) {
//            parseException.logException(e,true);
//        }
//        return flag;
//    }
    /**
     * To check whether Bank Code is duplicated or not
     */
    public boolean isStateCodeAlreadyExist(String stateCode){
        boolean exist = false;
        try{
            if (stateCode != null && stateCode.length() >0 && !stateCode.equals("")) {
                HashMap where = new HashMap();
                where.put("STATE_CODE", stateCode );
                List stateCodeCount = (List) ClientUtil.executeQuery("countStateCodeName", where);
                where = null;
                if ( stateCodeCount.size() > 0 && stateCodeCount != null) {
                    int a=CommonUtil.convertObjToInt(stateCodeCount.get(0));
                    if ( a > 0 ) {
                        exist = true;
                    } else {
                        exist = false;
                    }
                    a = 0;
                }
                stateCodeCount = null;
            }
        }
        catch ( Exception e ) {
            parseException.logException(e,true);
        }
        return exist;
    }
    /**
     * Action to be performed when Save Button in Other Bank Branch Screen is pressed
     */

    /**
     * Getter for property txtTalukName.
     * @return Value of property txtTalukName.
     */
    public java.lang.String getTxtTalukName() {
        return txtTalukName;
    }
    
    /**
     * Setter for property txtTalukName.
     * @param txtTalukName New value of property txtTalukName.
     */
    public void setTxtTalukName(java.lang.String txtTalukName) {
        this.txtTalukName = txtTalukName;
    }
    
    /**
     * Getter for property txtTalukCode.
     * @return Value of property txtTalukCode.
     */
    public java.lang.String getTxtTalukCode() {
        return txtTalukCode;
    }    

    /**
     * Setter for property txtTalukCode.
     * @param txtTalukCode New value of property txtTalukCode.
     */
    public void setTxtTalukCode(java.lang.String txtTalukCode) {
        this.txtTalukCode = txtTalukCode;
    }    
    
    /**
     * Getter for property txtDistrictName.
     * @return Value of property txtDistrictName.
     */
    public java.lang.String getTxtDistrictName() {
        return txtDistrictName;
    }
    
    /**
     * Setter for property txtDistrictName.
     * @param txtDistrictName New value of property txtDistrictName.
     */
    public void setTxtDistrictName(java.lang.String txtDistrictName) {
        this.txtDistrictName = txtDistrictName;
    }
    
    /**
     * Getter for property txtDistrictCode.
     * @return Value of property txtDistrictCode.
     */
    public java.lang.String getTxtDistrictCode() {
        return txtDistrictCode;
    }
    
    /**
     * Setter for property txtDistrictCode.
     * @param txtDistrictCode New value of property txtDistrictCode.
     */
    public void setTxtDistrictCode(java.lang.String txtDistrictCode) {
        this.txtDistrictCode = txtDistrictCode;
    }
    
    /**
     * Getter for property txtStateName.
     * @return Value of property txtStateName.
     */
    public java.lang.String getTxtStateName() {
        return txtStateName;
    }
    
    /**
     * Setter for property txtStateName.
     * @param txtStateName New value of property txtStateName.
     */
    public void setTxtStateName(java.lang.String txtStateName) {
        this.txtStateName = txtStateName;
    }
    
    /**
     * Getter for property txtStateCode.
     * @return Value of property txtStateCode.
     */
    public java.lang.String getTxtStateCode() {
        return txtStateCode;
    }
    
    /**
     * Setter for property txtStateCode.
     * @param txtStateCode New value of property txtStateCode.
     */
    public void setTxtStateCode(java.lang.String txtStateCode) {
        this.txtStateCode = txtStateCode;
    }
     public void addToTable(int rowSelected, boolean updateMode){
        try{
            int rowSel=rowSelected;
            String inc="INCOME";
            final StateTalukTO objStateTalukTO = new StateTalukTO();
            if( incParMap == null ){
                incParMap = new LinkedHashMap();
            }
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isNewIncomeDet()){
                    objStateTalukTO.setTalukStatus(CommonConstants.STATUS_CREATED);
                }else{
                    objStateTalukTO.setTalukStatus(CommonConstants.STATUS_MODIFIED);
                }
            }else{
                objStateTalukTO.setStatus(CommonConstants.STATUS_CREATED);
                objStateTalukTO.setTalukStatus(CommonConstants.STATUS_CREATED);
            }
            int  slno=0;
            int nums[]= new int[50];
            int max=nums[0];
           if(!updateMode){
                ArrayList data = tblDistrict.getDataArrayList();
            slno=serialNo(data,inc);
           }
           else{
               if(isNewIncomeDet()){
                ArrayList data = tblDistrict.getDataArrayList();
               slno=serialNo(data,inc); 
           }
               else{
                int b=CommonUtil.convertObjToInt(tblDistrict.getValueAt(rowSelected,0));
            slno=b;
               }
           }
          
            objStateTalukTO.setSlno(String.valueOf(slno));
            objStateTalukTO.setStateCode(CommonUtil.convertObjToInt(txtStateCode));
            objStateTalukTO.setDisName(txtDistrictName);
            objStateTalukTO.setDisCode(txtDistrictCode);
            objStateTalukTO.setTalukCode(txtTalukCode);
            objStateTalukTO.setTalukName(txtTalukName);    
            objStateTalukTO.setTalStatusBy(TrueTransactMain.USER_ID);
            objStateTalukTO.setDisStatusBy(TrueTransactMain.USER_ID);
            objStateTalukTO.setTalCreatedBy(TrueTransactMain.USER_ID);
            objStateTalukTO.setDisCreatedBy(TrueTransactMain.USER_ID);
            incParMap.put(objStateTalukTO.getSlno()+"."+""+txtStateCode+"."+""+txtDistrictCode+"."+""+txtTalukCode,objStateTalukTO);
            String sno=String.valueOf(slno);
            updateTblDistrictList(rowSel,sno,objStateTalukTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
       private void updateTblDistrictList(int rowSel, String sno, StateTalukTO objStateTalukTO)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for(int i = tblDistrict.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblDistrict.getDataArrayList().get(j)).get(0);
             if(sno.equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
            ArrayList IncParRow = new ArrayList();
            ArrayList data = tblDistrict.getDataArrayList();
            data.remove(rowSel);
            IncParRow.add(sno);
            IncParRow.add(txtDistrictCode);
            IncParRow.add(txtDistrictName);
            IncParRow.add(txtTalukCode);
            IncParRow.add(txtTalukName);
            IncParRow.add(objStateTalukTO.getTalukStatus());
            IncParRow.add("");
            IncParRow.add("");
            tblDistrict.insertRow(rowSel,IncParRow);
            IncParRow = null;
            }
        }
        if(!rowExists){
            ArrayList IncParRow = new ArrayList();
            IncParRow.add(String.valueOf(sno));
            IncParRow.add(txtDistrictCode);
            IncParRow.add(txtDistrictName);
            IncParRow.add(txtTalukCode);
            IncParRow.add(txtTalukName);
            IncParRow.add(objStateTalukTO.getTalukStatus());
            IncParRow.add("");
            IncParRow.add("");
            tblDistrict.insertRow(tblDistrict.getRowCount(),IncParRow);
            IncParRow = null;
        }
    }
         public int serialNo(ArrayList data, String str){
            final int dataSize = data.size();
             int nums[]= new int[50];
            int max=nums[0];
            int slno=0;
            int a=0;
              slno=dataSize+1;
            for(int i=0;i<data.size();i++){
                if(str.equals("STATE"))
                 a=CommonUtil.convertObjToInt(tblStateDis.getValueAt(i,0));
                else
                a=CommonUtil.convertObjToInt(tblDistrict.getValueAt(i,0));
               nums[i]=a;
               if(nums[i]>max)
                   max=nums[i];
               slno=max+1;
            }
         return slno;
      }
         
         /**
          * Getter for property newIncomeDet.
          * @return Value of property newIncomeDet.
          */
         public boolean isNewIncomeDet() {
             return newIncomeDet;
         }
         
         /**
          * Setter for property newIncomeDet.
          * @param newIncomeDet New value of property newIncomeDet.
          */
         public void setNewIncomeDet(boolean newIncomeDet) {
             this.newIncomeDet = newIncomeDet;
         }
         
         public void populateDetails(String row){
       try{
          IncomeChanged(row);
       }catch(Exception e){
            parseException.logException(e,true);
        }
   }
         public void IncomeChanged(String selectedItem){
        try{
           final StateTalukTO objStateTalukTO = (StateTalukTO)incParMap.get(selectedItem);
           resetTaluk();
           resetDistrict();
           if (getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE||getActionType()==ClientConstants.ACTIONTYPE_REJECT){
               objStateTalukTO.setVerification("YES");
               incParMap.put(selectedItem,objStateTalukTO);
           }
           populateData(objStateTalukTO); 
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
         
          public void populateDistrictDetails(int row){
       try{
          IncomeChangedDistrict(CommonUtil.convertObjToStr(tblStateDis.getValueAt(row,0)));
       }catch(Exception e){
            parseException.logException(e,true);
        }
   }
           public void IncomeChangedDistrict(String selectedItem){
        try{
           final StateTalukTO objStateTalukTO = (StateTalukTO)StateMap.get(selectedItem);
           resetDistrict();
           populateDistrictData(objStateTalukTO); 
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
         private void populateData(StateTalukTO objStateTalukTO)  throws Exception{
         setTxtDistrictCode(objStateTalukTO.getDisCode());
         setTxtDistrictName(objStateTalukTO.getDisName());
         setTxtTalukCode(objStateTalukTO.getTalukCode());
         setTxtTalukName(objStateTalukTO.getTalukName());
         setTxtSlNo(objStateTalukTO.getSlno());
         setTxtStateSlNo(objStateTalukTO.getStateSlno());
         setChanged();
    }
           private void populateDistrictData(StateTalukTO objStateTalukTO)  throws Exception{
            setTxtDistrictCode(objStateTalukTO.getDisCode());
            setTxtDistrictName(objStateTalukTO.getDisName());
         
         setChanged();
    }
         
         /**
          * Getter for property txtSlNo.
          * @return Value of property txtSlNo.
          */
         public java.lang.String getTxtSlNo() {
             return txtSlNo;
         }
         
         /**
          * Setter for property txtSlNo.
          * @param txtSlNo New value of property txtSlNo.
          */
         public void setTxtSlNo(java.lang.String txtSlNo) {
             this.txtSlNo = txtSlNo;
         }
          public void deleteTableData(String val, int row){
        if(deletedIncomeparMap == null){
            deletedIncomeparMap = new LinkedHashMap();
        }
        StateTalukTO objStateTalukTO = (StateTalukTO) incParMap.get(val);
        objStateTalukTO.setTalukStatus(CommonConstants.STATUS_DELETED);
        deletedIncomeparMap.put(CommonUtil.convertObjToStr(tblDistrict.getValueAt(row,0)),incParMap.get(val));
        Object obj;
        obj=val;
        incParMap.remove(val);
        resetDistrictTable();
        try{
             populateincParTable();
        }
        catch(Exception e){
            parseException.logException(e,true);
        }
//        resetNewIncomeParticulars();
    }
           public void resetDistrictTable(){
        for(int i = tblDistrict.getRowCount(); i > 0; i--){
            tblDistrict.removeRow(0);
        }
    }
            public void resetStateTable(){
        for(int i = tblStateDis.getRowCount(); i > 0; i--){
            tblStateDis.removeRow(0);
        }
    }
           private void populateincParTable()  throws Exception{
          ArrayList incDataList = new ArrayList();
         incDataList = new ArrayList(incParMap.keySet());
         ArrayList addList =new ArrayList(incParMap.keySet());
         int length = incDataList.size();
          for(int i=0; i<length; i++){
                ArrayList incTabRow = new ArrayList();
                StateTalukTO objStateTalukTO = (StateTalukTO) incParMap.get(addList.get(i));
                
                IncVal.add(objStateTalukTO);
                if(!objStateTalukTO.getTalukStatus().equals("DELETED"))
                if(txtDistrictCode.equalsIgnoreCase(CommonUtil.convertObjToStr(objStateTalukTO.getDisCode()))){
                incTabRow.add(CommonUtil.convertObjToStr(objStateTalukTO.getSlno()));
                incTabRow.add(CommonUtil.convertObjToStr(objStateTalukTO.getDisCode()));
                incTabRow.add(CommonUtil.convertObjToStr(objStateTalukTO.getDisName()));
                incTabRow.add(CommonUtil.convertObjToStr(objStateTalukTO.getTalukCode()));
                incTabRow.add(CommonUtil.convertObjToStr(objStateTalukTO.getTalukName()));
                incTabRow.add(CommonUtil.convertObjToStr(objStateTalukTO.getTalukStatus()));
                incTabRow.add(CommonUtil.convertObjToStr(objStateTalukTO.getTalAuthorizeStatus()));
                incTabRow.add("");
                tblDistrict.addRow(incTabRow);
                }
            }
            ttNotifyObservers();
        
    }
             private void populateStateTable()  throws Exception{
          ArrayList incDataList = new ArrayList();
         incDataList = new ArrayList(StateMap.keySet());
         ArrayList addList =new ArrayList(StateMap.keySet());
         int length = incDataList.size();
          for(int i=0; i<length; i++){
                ArrayList incTabRow = new ArrayList();
                StateTalukTO objStateTalukTO = (StateTalukTO) StateMap.get(addList.get(i));
                
                IncVal.add(objStateTalukTO);
                
                incTabRow.add(CommonUtil.convertObjToStr(objStateTalukTO.getStateSlno()));
                incTabRow.add(CommonUtil.convertObjToStr(objStateTalukTO.getStateCode()));
                incTabRow.add(CommonUtil.convertObjToStr(objStateTalukTO.getDisCode()));
                incTabRow.add(CommonUtil.convertObjToStr(objStateTalukTO.getDisName()));
                incTabRow.add(CommonUtil.convertObjToStr(objStateTalukTO.getDisStatus()));
                 incTabRow.add(CommonUtil.convertObjToStr(objStateTalukTO.getDisAuthorizeStatus()));
                incTabRow.add("");
                tblStateDis.addRow(incTabRow);
            }
            ttNotifyObservers();
        
    }
           
           /**
            * Getter for property tblStateDis.
            * @return Value of property tblStateDis.
            */
           public com.see.truetransact.clientutil.EnhancedTableModel getTblStateDis() {
               return tblStateDis;
           }
           
           /**
            * Setter for property tblStateDis.
            * @param tblStateDis New value of property tblStateDis.
            */
           public void setTblStateDis(com.see.truetransact.clientutil.EnhancedTableModel tblStateDis) {
               this.tblStateDis = tblStateDis;
           }
           
            public void addToTableState(){
                int cnt=0;
                int disCode=CommonUtil.convertObjToInt(getTxtDistrictCode());
                int dataSize=tblStateDis.getRowCount();
                if(dataSize>0){
                    for(int i=0;i<dataSize;i++){
                        int a= CommonUtil.convertObjToInt(tblStateDis.getValueAt(i,2)); 
                        if(a==disCode){
                            cnt++;
                StateTalukTO objStateTalukTO = (StateTalukTO) StateMap.get(CommonUtil.convertObjToStr(tblStateDis.getValueAt(i,0)));
                       objStateTalukTO.setDisCode(txtDistrictCode);
                       objStateTalukTO.setDisName(txtDistrictName);
                       tblStateDis.setValueAt(txtDistrictCode, i,2); 
                       tblStateDis.setValueAt(txtDistrictName, i,3); 
                        }
                    }
                    if(cnt==0){
                         insertIntoStatetable();
                    }
                }
                else{
                    insertIntoStatetable();
                }
            }
            public void insertIntoStatetable(){
                try{
                final StateTalukTO objStateTalukTO = new StateTalukTO();
            if( StateMap == null ){
                StateMap = new LinkedHashMap();
            }
                String state="STATE";
                int slno=0;
                int dataSize=tblStateDis.getRowCount();
                if(dataSize<=0)
                slno=1;
                else{
                    ArrayList data = tblStateDis.getDataArrayList();
                    slno=serialNo(data,state);
                }
                 if(isNewIncomeDet())
                objStateTalukTO.setDisStatus(CommonConstants.STATUS_CREATED);    
                 else objStateTalukTO.setDisStatus(CommonConstants.STATUS_CREATED);
             objStateTalukTO.setStateSlno(String.valueOf(slno));
            objStateTalukTO.setStateCode(CommonUtil.convertObjToInt(txtStateCode));
            objStateTalukTO.setDisCode(txtDistrictCode);
            objStateTalukTO.setDisName(txtDistrictName);
            objStateTalukTO.setSateStatusBy(TrueTransactMain.USER_ID);
            objStateTalukTO.setStateCreatedBy(TrueTransactMain.USER_ID);
            objStateTalukTO.setDisCreatedBy(TrueTransactMain.USER_ID);
            StateMap.put(objStateTalukTO.getStateSlno(),objStateTalukTO);
            objStateTalukTO.setDisStatusBy(TrueTransactMain.USER_ID);
            String sno=String.valueOf(slno);
            updateStateTable(sno,objStateTalukTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
            }
            
            public void updateStateTable(String sno, StateTalukTO objStateTalukTO){
               ArrayList StateRow = new ArrayList();
            StateRow.add(String.valueOf(sno));
            StateRow.add(txtStateCode);
            StateRow.add(txtDistrictCode);
            StateRow.add(txtDistrictName);
            StateRow.add(objStateTalukTO.getDisStatus());
            StateRow.add("");
            StateRow.add("");
            tblStateDis.insertRow(tblStateDis.getRowCount(),StateRow);
            StateRow = null;
            }
            
            /**
             * Getter for property txtStateSlNo.
             * @return Value of property txtStateSlNo.
             */
            public java.lang.String getTxtStateSlNo() {
                return txtStateSlNo;
            }
            
            /**
             * Setter for property txtStateSlNo.
             * @param txtStateSlNo New value of property txtStateSlNo.
             */
            public void setTxtStateSlNo(java.lang.String txtStateSlNo) {
                this.txtStateSlNo = txtStateSlNo;
            }
            public void deleteFromTable(int row){
                if(deletedStateMap == null){
            deletedStateMap = new LinkedHashMap();
        }
        StateTalukTO objStateTalukTO = (StateTalukTO) StateMap.get(CommonUtil.convertObjToStr(tblStateDis.getValueAt(row,0)));
        objStateTalukTO.setDisStatus(CommonConstants.STATUS_DELETED);
        deletedStateMap.put(CommonUtil.convertObjToStr(tblStateDis.getValueAt(row,0)),StateMap.get(CommonUtil.convertObjToStr(tblStateDis.getValueAt(row,0))));
        StateMap.remove(tblStateDis.getValueAt(row,0));
        resetStateTable();
        try{
             populateStateTable();
        }
        catch(Exception e){
            parseException.logException(e,true);
        }
                
            }
            
            public void getTalukDetailsBasedonrowSelected(int state_code, int district_code){
                String disCode=String.valueOf(district_code);
                HashMap where = new HashMap();
                LinkedHashMap ParMap = new LinkedHashMap();
                HashMap returnMap = new HashMap();
                if(incParMap!=null){
                ArrayList addList =new ArrayList(incParMap.keySet());
                for(int i=0;i<addList.size();i++) {
                    objStateTalukTO = (StateTalukTO) incParMap.get(addList.get(i));
                    if(objStateTalukTO.getDisCode().equals(disCode)){
                        ArrayList insTable = new ArrayList();
                        insTable.add(CommonUtil.convertObjToStr(objStateTalukTO.getSlno()));
                        insTable.add(CommonUtil.convertObjToStr(objStateTalukTO.getDisCode()));
                        insTable.add(CommonUtil.convertObjToStr(objStateTalukTO.getDisName()));
                        insTable.add(CommonUtil.convertObjToStr(objStateTalukTO.getTalukCode()));
                        insTable.add(CommonUtil.convertObjToStr(objStateTalukTO.getTalukName()));
                        insTable.add(CommonUtil.convertObjToStr(objStateTalukTO.getTalukStatus()));
                        insTable.add(CommonUtil.convertObjToStr(objStateTalukTO.getTalAuthorizeStatus()));
                        if (getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE||getActionType()==ClientConstants.ACTIONTYPE_REJECT){
                            if(objStateTalukTO.getVerification().equalsIgnoreCase("YES"))
                            insTable.add("YES");
                            else
                                insTable.add("NO"); 
                        } else{
                            insTable.add("");
                        }
                        if((getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE||getActionType()==ClientConstants.ACTIONTYPE_REJECT) && objStateTalukTO.getTalAuthorizeStatus()== null)
                            tblDistrict.addRow(insTable);
                        else if(getActionType()==ClientConstants.ACTIONTYPE_NEW||getActionType()==ClientConstants.ACTIONTYPE_EDIT||getActionType()==ClientConstants.ACTIONTYPE_VIEW)
                            tblDistrict.addRow(insTable);
                        objStateTalukTO=null;
                    }
                }
                }
            }
       
            /**
             * Getter for property incParMap.
             * @return Value of property incParMap.
             */
            public java.util.LinkedHashMap getIncParMap() {
                return incParMap;
            }            
            
            /**
             * Setter for property incParMap.
             * @param incParMap New value of property incParMap.
             */
            public void setIncParMap(java.util.LinkedHashMap incParMap) {
                this.incParMap = incParMap;
            }
            
}