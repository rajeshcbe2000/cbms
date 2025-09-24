/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ViewLogImportanceOB.java
 *
 * Created on January 7, 2005, 2:50 PM
 */

package com.see.truetransact.ui.sysadmin.view;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.*;
import com.see.truetransact.commonutil.*;
import com.see.truetransact.transferobject.sysadmin.viewlogimportance.ViewLogImportanceTO;
import com.see.truetransact.ui.sysadmin.view.ViewLogRB;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uicomponent.CObservable;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Date;
/**
 *
 * @author  152713
 */
public class ViewLogImportanceOB extends CObservable {
    
    private static ViewLogImportanceOB objViewLogImportanceOB; // singleton object
    private static ViewLogImportanceRB objViewLogImportanceRB = new ViewLogImportanceRB();
    
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger log = Logger.getLogger(ViewLogImportanceOB.class);
    
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    private final   String  ACTIVITY = "ACTIVITY";
    private final   String  ALL_VALUES = "ALL_VALUES";
    private final   String  COMMAND = "COMMAND";
    private final   String  IMPORTANCE = "IMPORTANCE";
    private final   String  INSERT = "INSERT";
    private final   String  MODULE = "MODULE";
    private final   String  OPTION = "OPTION";
    private final   String  SCREEN = "SCREEN";
    private final   String  SLNO = "SLNO";
    private final   String  TABLE_VALUES = "TABLE_VALUES";
    private final   String  UPDATE = "UPDATE";
    
    private final   ArrayList viewLogImportanceTabTitle = new ArrayList();      //  Table Title of viewLogImportance
    private ArrayList viewLogImportanceTabValues = new ArrayList();
    private ArrayList viewLogImportanceEachTabRecord;
    
    private HashMap viewLogImportanceEachRecord;
    
    private LinkedHashMap viewLogImportanceAll = new LinkedHashMap();          // Both displayed and hidden values in the table
    
    private TableUtil tableUtilViewLogImportance = new TableUtil();
    
    private ComboBoxModel cbmModule;
    private ComboBoxModel cbmScreen;
    private ComboBoxModel cbmActivity;
    private ComboBoxModel cbmImportance;
    
    private EnhancedTableModel tblTable_Importance;
    
    private String cboModule = "";
    private String cboBranchID = "";
    private String cboScreen = "";
    private String cboActivity = "";
    private String cboImportance = "";
    
    private int actionType;
    private int resultStatus;
    private int result;
    private ArrayList key;
    private ArrayList value;
    private ProxyFactory proxy;
    private HashMap operationMap;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private Date currDt = null;
    /** Creates a new instance of ViewLogImportanceOB */
    public ViewLogImportanceOB(){
        try{
            currDt = ClientUtil.getCurrentDate();
            setOperationMap();
            proxy = ProxyFactory.createProxy();
            fillDropdown();
            setViewLogImportanceTabTitle();
            tableUtilViewLogImportance = new TableUtil();
            tableUtilViewLogImportance.setAttributeKey(SLNO);
            tblTable_Importance = new EnhancedTableModel(null, viewLogImportanceTabTitle);
        }catch(Exception e) {
            log.info("Exception in ViewLogImportanceOB(): "+e);
            parseException.logException(e,true);
        }
    }
    
    private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "ViewLogImportanceJNDI");
        operationMap.put(CommonConstants.HOME, "ViewLogImportanceHome");
        operationMap.put(CommonConstants.REMOTE, "ViewLogImportance");
    }
    
    static {
        try {
            objViewLogImportanceOB = new ViewLogImportanceOB();
        } catch(Exception e) {
            log.info("Exception in ViewLogImportanceOB() constructor: "+e);
            parseException.logException(e,true);
        }
    }
    
    /** Creates a new instance of ViewLogOB */
    public static ViewLogImportanceOB getInstance() {
        return objViewLogImportanceOB;
    }
    
    public void setViewLogImportanceTabTitle(){
        try{
            viewLogImportanceTabTitle.add(objViewLogImportanceRB.getString("tblColumn1"));
            viewLogImportanceTabTitle.add(objViewLogImportanceRB.getString("lblModule"));
            viewLogImportanceTabTitle.add(objViewLogImportanceRB.getString("lblScreen"));
            viewLogImportanceTabTitle.add(objViewLogImportanceRB.getString("lblActivity"));
            viewLogImportanceTabTitle.add(objViewLogImportanceRB.getString("lblImportance"));
        }catch(Exception e) {
            log.info("Exception in setViewLogImportanceTabTitle(): "+e);
            parseException.logException(e,true);
        }
    }
    
    /** Method to get the combo box values */
    private void fillDropdown() throws Exception{
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
        lookup_keys.add("LOGVIEW_IMPORTANCE");
        
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("LOGVIEW_IMPORTANCE"));
        cbmImportance = new ComboBoxModel(key,value);
        
        lookUpHash = null;
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,"ViewLog.Screen");
        lookUpHash.put(CommonConstants.PARAMFORQUERY,null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmScreen = new ComboBoxModel(key,value);
        
        lookUpHash = null;
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,"ViewLog.Module");
        lookUpHash.put(CommonConstants.PARAMFORQUERY,null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmModule = new ComboBoxModel(key,value);
        
        lookUpHash = null;
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,"ViewLog.Activity");
        lookUpHash.put(CommonConstants.PARAMFORQUERY,null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmActivity = new ComboBoxModel(key,value);
        
        lookUpHash = null;
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
    
    public void resetTable(){
        tblTable_Importance = new EnhancedTableModel(null, viewLogImportanceTabTitle);
        tableUtilViewLogImportance = new TableUtil();
        tableUtilViewLogImportance.setAttributeKey(SLNO);
        this.ttNotifyObservers();
    }
    
    public void resetForm(){
        resetFields();
        resetTable();
    }
    
    public void resetFields(){
        setCboActivity("");
        setCboImportance("");
        setCboModule("");
        setCboScreen("");
    }
    /**
     * To retrieve the data from database to populate in UI
     * @param whereMap is the HashMap having where conditions
     */
    public void populateData(HashMap whereMap) {
        log.info("In populateData...");
        HashMap mapData = null;
        try {
            mapData =  (HashMap) proxy.executeQuery(whereMap, operationMap);
            log.info("proxy.executeQuery is working fine");
            populateOB(mapData);
        } catch( Exception e ) {
            log.info("Exception caught in populateData"+e);
            parseException.logException(e,true);
        }
    }
    
    /**
     * To retrieve the data from database to populate in UI
     * @param mapData is the HashMap having the values retrieved from database
     */
    private void populateOB(HashMap mapData) {
        log.info("In populateOB...");
        setViewLogImportanceTO((ArrayList) (mapData.get("ViewLogImportanceTO")));
        ttNotifyObservers();
    }
    
    public void setViewLogImportanceTO(ArrayList viewLogImportanceList){
        try{
            ViewLogImportanceTO viewLogImportanceTO;
            HashMap viewLogImportanceRecordMap;
            ArrayList removedValues = new ArrayList();
            LinkedHashMap allViewLogImportanceRecords = new LinkedHashMap();
            ArrayList viewLogImportanceRecordList;
            ArrayList tabViewLogImportanceRecords = new ArrayList();
            
            // To retrieve the viewLogImportance Details from the Serverside
            for (int i = viewLogImportanceList.size() - 1,j = 0;i >= 0;--i,++j){
                viewLogImportanceTO = (ViewLogImportanceTO) viewLogImportanceList.get(j);
                viewLogImportanceRecordMap = new HashMap();
                viewLogImportanceRecordList = new ArrayList();
                
                viewLogImportanceRecordList.add(CommonUtil.convertObjToStr(viewLogImportanceTO.getImpId()));
                viewLogImportanceRecordList.add(CommonUtil.convertObjToStr(getCbmModule().getDataForKey(viewLogImportanceTO.getModule())));
                viewLogImportanceRecordList.add(CommonUtil.convertObjToStr(getCbmScreen().getDataForKey(viewLogImportanceTO.getScreen())));
                viewLogImportanceRecordList.add(CommonUtil.convertObjToStr(getCbmActivity().getDataForKey(viewLogImportanceTO.getActivity())));
                viewLogImportanceRecordList.add(CommonUtil.convertObjToStr(getCbmImportance().getDataForKey(viewLogImportanceTO.getImportance())));
                
                tabViewLogImportanceRecords.add(viewLogImportanceRecordList);
                
                viewLogImportanceRecordMap.put(SLNO, CommonUtil.convertObjToStr(viewLogImportanceTO.getImpId()));
                viewLogImportanceRecordMap.put(MODULE, CommonUtil.convertObjToStr(viewLogImportanceTO.getModule()));
                viewLogImportanceRecordMap.put(SCREEN, CommonUtil.convertObjToStr(viewLogImportanceTO.getScreen()));
                viewLogImportanceRecordMap.put(ACTIVITY, CommonUtil.convertObjToStr(viewLogImportanceTO.getActivity()));
                viewLogImportanceRecordMap.put(IMPORTANCE, CommonUtil.convertObjToStr(viewLogImportanceTO.getImportance()));
                
                viewLogImportanceRecordMap.put(COMMAND, UPDATE);
                
                allViewLogImportanceRecords.put(viewLogImportanceTO.getImpId(), viewLogImportanceRecordMap);
                
                viewLogImportanceRecordList = null;
                viewLogImportanceRecordMap = null;
            }
            viewLogImportanceAll.clear();
            viewLogImportanceTabValues.clear();
            
            viewLogImportanceAll = allViewLogImportanceRecords;
            viewLogImportanceTabValues = tabViewLogImportanceRecords;
            
            tblTable_Importance.setDataArrayList(viewLogImportanceTabValues, viewLogImportanceTabTitle);
            
            tableUtilViewLogImportance.setRemovedValues(removedValues);
            tableUtilViewLogImportance.setAllValues(viewLogImportanceAll);
            tableUtilViewLogImportance.setTableValues(viewLogImportanceTabValues);
            setMax_Del_ViewLogImportance_No();
            tabViewLogImportanceRecords = null;
            allViewLogImportanceRecords = null;
            removedValues = null;
        }catch(Exception e){
            log.info("Error in setViewLogImportanceTO()..."+e);
            parseException.logException(e,true);
        }
    }
    
    private void setMax_Del_ViewLogImportance_No(){
        try{
            HashMap whereMap = new HashMap();
            HashMap retrieve = new HashMap();
            List resultList = ClientUtil.executeQuery("getSelectViewLogImportanceMaxSLNO", whereMap);
            
            if (resultList.size() > 0){
                retrieve = (HashMap) resultList.get(0);
                tableUtilViewLogImportance.setStr_MAX_DEL_SL_NO(CommonUtil.convertObjToStr(retrieve.get("MAX_SL_NO")));
            }
            
            retrieve = null;
            resultList = null;
            whereMap = null;
        }catch(Exception e){
            log.info("Error In setMax_Del_ViewLogImportance_No: "+e);
            parseException.logException(e,true);
        }
    }
    
    public void doAction() {
        log.info("In doAction...");
        try {
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null ){
                    log.info("before doActionPerform...");
                    doActionPerform();
                }
                else{
                    log.info("In doAction()-->getCommand() is null:" );
                }
            }
            else
                log.info("In doAction()-->actionType is null:" );
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            log.info("Error in doAction():"+e);
            parseException.logException(e,true);
        }
    }
    
    
    private void doActionPerform() throws Exception{
        log.info("doActionPerform");
        HashMap objViewLogImportanceTOMap = setViewLogImportance();
        HashMap data = new HashMap();
        data.put("ViewLogImportanceTO",  objViewLogImportanceTOMap);
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        HashMap proxyResultMap = proxy.execute(data,operationMap);
        setResult(actionType);
        objViewLogImportanceTOMap = null;
        data = null;
    }
    
    public HashMap setViewLogImportance(){
        HashMap viewLogImportanceMap = new HashMap();
        try{
            ViewLogImportanceTO viewLogImportanceTO;
            java.util.Set keySet =  viewLogImportanceAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            HashMap oneRecord;
            
            // To set the values for ViewLogImportance Transfer Object
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) viewLogImportanceAll.get(objKeySet[j]);
                viewLogImportanceTO = new ViewLogImportanceTO();
                viewLogImportanceTO.setImpId(CommonUtil.convertObjToStr(oneRecord.get(SLNO)));
                viewLogImportanceTO.setModule(CommonUtil.convertObjToStr(oneRecord.get(MODULE)));
                viewLogImportanceTO.setScreen(CommonUtil.convertObjToStr(oneRecord.get(SCREEN)));
                viewLogImportanceTO.setActivity(CommonUtil.convertObjToStr(oneRecord.get(ACTIVITY)));
                viewLogImportanceTO.setImportance(CommonUtil.convertObjToStr(oneRecord.get(IMPORTANCE)));
                viewLogImportanceTO.setCommand(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
                if (oneRecord.get(COMMAND).equals(INSERT)){
                    viewLogImportanceTO.setStatus(CommonConstants.STATUS_CREATED);
                }else if (oneRecord.get(COMMAND).equals(UPDATE)){
                    viewLogImportanceTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
                viewLogImportanceTO.setStatusBy(TrueTransactMain.USER_ID);
                viewLogImportanceTO.setStatusDt(currDt);
                
                viewLogImportanceMap.put(String.valueOf(j+1), viewLogImportanceTO);
                
                oneRecord = null;
                viewLogImportanceTO = null;
            }
            
            // To set the values for ViewLogImportance Transfer Object
            // where as the existing records in Database are deleted in client side
            // useful for updating the status
            for (int i = tableUtilViewLogImportance.getRemovedValues().size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) tableUtilViewLogImportance.getRemovedValues().get(j);
                viewLogImportanceTO = new ViewLogImportanceTO();
                viewLogImportanceTO.setImpId(CommonUtil.convertObjToStr(oneRecord.get(SLNO)));
                viewLogImportanceTO.setModule(CommonUtil.convertObjToStr(oneRecord.get(MODULE)));
                viewLogImportanceTO.setScreen(CommonUtil.convertObjToStr(oneRecord.get(SCREEN)));
                viewLogImportanceTO.setActivity(CommonUtil.convertObjToStr(oneRecord.get(ACTIVITY)));
                viewLogImportanceTO.setImportance(CommonUtil.convertObjToStr(oneRecord.get(IMPORTANCE)));
                viewLogImportanceTO.setCommand(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
                viewLogImportanceTO.setStatus(CommonConstants.STATUS_DELETED);
                viewLogImportanceTO.setStatusBy(TrueTransactMain.USER_ID);
                viewLogImportanceTO.setStatusDt(currDt);
                
                viewLogImportanceMap.put(String.valueOf(viewLogImportanceMap.size()+1), viewLogImportanceTO);
                
                oneRecord = null;
                viewLogImportanceTO = null;
            }
            
        }catch(Exception e){
            log.info("Error In setViewLogImportance() "+e);
            parseException.logException(e,true);
        }
        return viewLogImportanceMap;
    }
    
    private String getCommand() throws Exception{
        log.info("getCommand");
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
    
    public void ttNotifyObservers(){
        this.notifyObservers();
    }
    
    // Setter method for cboModule
    void setCboModule(String cboModule){
        this.cboModule = cboModule;
        setChanged();
    }
    // Getter method for cboModule
    String getCboModule(){
        return this.cboModule;
    }
    
    // Setter method for setCbmModule
    void setCbmModule(ComboBoxModel cbmModule){
        this.cbmModule = cbmModule;
        setChanged();
    }
    // Getter method for getCbmModule
    ComboBoxModel getCbmModule(){
        return cbmModule;
    }
    
    // Setter method for cboScreen
    void setCboScreen(String cboScreen){
        this.cboScreen = cboScreen;
        setChanged();
    }
    // Getter method for cboScreen
    String getCboScreen(){
        return this.cboScreen;
    }
    
    // Setter method for setCbmScreen
    void setCbmScreen(ComboBoxModel cbmScreen){
        this.cbmScreen = cbmScreen;
        setChanged();
    }
    // Getter method for getCbmScreen
    ComboBoxModel getCbmScreen(){
        return cbmScreen;
    }
    
    // Setter method for cboActivity
    void setCboActivity(String cboActivity){
        this.cboActivity = cboActivity;
        setChanged();
    }
    // Getter method for cboActivity
    String getCboActivity(){
        return this.cboActivity;
    }
    
    // Setter method for setCbmActivity
    void setCbmActivity(ComboBoxModel cbmActivity){
        this.cbmActivity = cbmActivity;
        setChanged();
    }
    // Getter method for getCbmActivity
    ComboBoxModel getCbmActivity(){
        return cbmActivity;
    }
    
    // Setter method for cboImportance
    void setCboImportance(String cboImportance){
        this.cboImportance = cboImportance;
        setChanged();
    }
    // Getter method for cboImportance
    String getCboImportance(){
        return this.cboImportance;
    }
    
    // Setter method for setCbmImportance
    void setCbmImportance(ComboBoxModel cbmImportance){
        this.cbmImportance = cbmImportance;
        setChanged();
    }
    // Getter method for getCbmImportance
    ComboBoxModel getCbmImportance(){
        return cbmImportance;
    }
    
    void setTblViewLogImportance(EnhancedTableModel tblTable_Importance){
        this.tblTable_Importance = tblTable_Importance;
        setChanged();
    }
    EnhancedTableModel getTblViewLogImportance(){
        return this.tblTable_Importance;
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
    
    /**
     * It will set the result of the query executed
     * @param resultStatus is the integer value which gives whether the data are inserted or updated or
     * deleted or the execution is failed
     */
    public void setResult(int resultStatus) {
        log.info("In setResult...");
        this.resultStatus = resultStatus;
        setChanged();
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
    
    public int addViewLogImportanceDetails(int recordPosition, boolean update){
        int option = -1;
        try{
            log.info("Add ViewLogImportance Details...");
            viewLogImportanceEachTabRecord = new ArrayList();
            viewLogImportanceEachRecord = new HashMap();
            HashMap result = new HashMap();
            ArrayList data = tblTable_Importance.getDataArrayList();
            tblTable_Importance.setDataArrayList(data, viewLogImportanceTabTitle);
            final int dataSize = data.size();
            insertGuarantor(dataSize+1);
            if (!update) {
                // If the table is not in Edit Mode
                result = tableUtilViewLogImportance.insertTableValues(viewLogImportanceEachTabRecord, viewLogImportanceEachRecord);
                
                viewLogImportanceTabValues = (ArrayList) result.get(TABLE_VALUES);
                viewLogImportanceAll = (LinkedHashMap) result.get(ALL_VALUES);
                option = CommonUtil.convertObjToInt(result.get(OPTION));
                
                tblTable_Importance.setDataArrayList(viewLogImportanceTabValues, viewLogImportanceTabTitle);
            }else{
                option = updateViewLogImportanceTab(recordPosition);
            }
            
            setChanged();
            
            viewLogImportanceEachTabRecord = null;
            viewLogImportanceEachRecord = null;
            result = null;
            data = null;
        }catch(Exception e){
            log.info("Error in addViewLogImportanceDetails()..."+e);
            parseException.logException(e,true);
        }
        return option;
    }
    
    private void insertGuarantor(int recordPosition){
        viewLogImportanceEachTabRecord.add(String.valueOf(recordPosition));
        viewLogImportanceEachTabRecord.add(getCboModule());
        viewLogImportanceEachTabRecord.add(getCboScreen());
        viewLogImportanceEachTabRecord.add(getCboActivity());
        viewLogImportanceEachTabRecord.add(getCboImportance());
        
        viewLogImportanceEachRecord.put(SLNO, String.valueOf(recordPosition));
        viewLogImportanceEachRecord.put(MODULE, CommonUtil.convertObjToStr(getCbmModule().getKeyForSelected()));
        viewLogImportanceEachRecord.put(SCREEN, CommonUtil.convertObjToStr(getCbmScreen().getKeyForSelected()));
        viewLogImportanceEachRecord.put(ACTIVITY, CommonUtil.convertObjToStr(getCbmActivity().getKeyForSelected()));
        viewLogImportanceEachRecord.put(IMPORTANCE, CommonUtil.convertObjToStr(getCbmImportance().getKeyForSelected()));
        viewLogImportanceEachRecord.put(COMMAND, "");
    }
    
    private int updateViewLogImportanceTab(int recordPosition){
        int option = -1;
        HashMap result = new HashMap();
        try{
            result = tableUtilViewLogImportance.updateTableValues(viewLogImportanceEachTabRecord, viewLogImportanceEachRecord, recordPosition);
            
            viewLogImportanceTabValues = (ArrayList) result.get(TABLE_VALUES);
            viewLogImportanceAll = (LinkedHashMap) result.get(ALL_VALUES);
            option = CommonUtil.convertObjToInt(result.get(OPTION));
            
            tblTable_Importance.setDataArrayList(viewLogImportanceTabValues, viewLogImportanceTabTitle);
            
        }catch(Exception e){
            log.info("Error in updateViewLogImportanceTab()..."+e);
            parseException.logException(e,true);
        }
        result = null;
        return option;
    }
    
    public void populateViewLogImportanceDetails(int recordPosition){
        try{
            HashMap eachRecs;
            java.util.Set keySet =  viewLogImportanceAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            String strRecordKey = (String) ((ArrayList) (tblTable_Importance.getDataArrayList().get(recordPosition))).get(0);
            
            // To populate the corresponding record from the ViewLogImportance Details
            for (int i = keySet.size() - 1, j = 0;i >= 0;--i, ++j){
                if (((String) ((HashMap) viewLogImportanceAll.get(objKeySet[j])).get(SLNO)).equals(strRecordKey)){
                    // To populate the Corresponding record from CTable
                    eachRecs = (HashMap) viewLogImportanceAll.get(objKeySet[j]);
                    setCboActivity(CommonUtil.convertObjToStr(getCbmActivity().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(ACTIVITY)))));
                    setCboScreen(CommonUtil.convertObjToStr(getCbmScreen().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(SCREEN)))));
                    setCboModule(CommonUtil.convertObjToStr(getCbmModule().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(MODULE)))));
                    setCboImportance(CommonUtil.convertObjToStr(getCbmImportance().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(IMPORTANCE)))));
                    
                    break;
                }
                eachRecs = null;
            }
            keySet = null;
            
            objKeySet = null;
            setChanged();
            ttNotifyObservers();
        }catch(Exception e){
            log.info("Error in populateViewLogImportanceDetails()..."+e);
            parseException.logException(e,true);
        }
    }
    
    public void deleteViewLogImportanceTabRecord(int recordPosition){
        HashMap result = new HashMap();
        try{
            result = tableUtilViewLogImportance.deleteTableValues(recordPosition);
            
            viewLogImportanceTabValues = (ArrayList) result.get(TABLE_VALUES);
            viewLogImportanceAll = (LinkedHashMap) result.get(ALL_VALUES);
            
            tblTable_Importance.setDataArrayList(viewLogImportanceTabValues, viewLogImportanceTabTitle);
            
        }catch(Exception e){
            log.info("Error in deleteViewLogImportanceTabRecord()..."+e);
            parseException.logException(e,true);
        }
        result = null;
    }
    
    // To create objects
    public void createObject(){
        viewLogImportanceTabValues = new ArrayList();
        viewLogImportanceAll = new LinkedHashMap();
        tblTable_Importance.setDataArrayList(null, viewLogImportanceTabTitle);
        tableUtilViewLogImportance = new TableUtil();
        tableUtilViewLogImportance.setAttributeKey(SLNO);
    }
    
    // To destroy Objects
    public void destroyObjects(){
        viewLogImportanceTabValues = null;
        viewLogImportanceAll = null;
        tableUtilViewLogImportance = null;
    }
    
}
