/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LeaveSanctionOB.java
 *
 * Created on Fri Jan 07 12:01:51 IST 2005
 */

package com.see.truetransact.ui.sysadmin.leavemanagement;

import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.transferobject.sysadmin.leavemanagement.LeaveSanctionTO;

import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.CommonUtil;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.ResourceBundle;

/**
 *
 * @author Swaroop
 */

public class LeaveSanctionOB extends CObservable{
    
    Date curDate = ClientUtil.getCurrentDate();
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger _log = Logger.getLogger(LeaveSanctionOB.class);
    private ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.sysadmin.leavemanagement.LeaveSanctionRB", ProxyParameters.LANGUAGE);
    private ProxyFactory proxy = null;
    private HashMap map,lookUpHash,keyValue;
    private int _result,_actionType;
    private ArrayList key,value;
    private static LeaveSanctionOB objLeaveSanctionOB;//Singleton Object
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    //    private Date lstRunDate=null;
    private ArrayList headings;
    
    final String YES = "Y";
    final String NO = "N";
    
    final String SUB_BLOCK = "SUB_BLOCK";
    final String BLOCK = "BLOCK";
    
    private String cboProcessType="";
    private ComboBoxModel cbmProcessType;
    private boolean rdoApp_Yes = false;
    private boolean rdoSan_Yes = false;
    private String appl_dt ="";
    private String req_from ="";
    private String req_to = "";
    private String noOfdays = "";
    private String leavePurpose = "";
    private String sanNo = "";
    private String sanDate = "";
    private String leaveID="";
    private String applSan="";
    private String empID="";
    private String tblLeaveType="";
    private String tabNoOfDays="";
    final ArrayList tableTitle = new ArrayList();
    private EnhancedTableModel tblLeaveDetails;
     private EnhancedTableModel tblLeaveDetails1;
    private ArrayList IncVal = new ArrayList();
    private boolean newData = false;
    private LinkedHashMap incParMap;
    private String cbotblLeaveType="";
    private ComboBoxModel cbmtblLeaveType;
    private String txtSlNo="";
    private LinkedHashMap tableMap;
    private HashMap lookupMap;
    private String cboLeaveTypeProcess="";
    private ComboBoxModel cbmLeaveTypeProcess;
    private HashMap _authorizeMap;
    private String oldSanNo="";
    private String tblreq_from ="";
    private String tblreq_to ="";
    private boolean withLtc= false;
    private boolean rdoTwoYr = false;
    private boolean rdoFourYr = false;
    private boolean withLeaveEncashment= false;
    private String leaveEncashmentDays= "";
    private ComboBoxModel cbmLeaveTypeEnquiry;
    private ComboBoxModel cbmEncashmentData;
    private String cboEncashmentData="";
    private String cboPayType="";
    private ComboBoxModel cbmPayType;
    private String encashLeaveType="";
    private String sanStatus="";
    private boolean leaveCancel=false;
    private String remarks= "";
    private String sanAuth= "";
    private String cboSanAuth="";
    private ComboBoxModel cbmSanAuth;
    
    // for leave application while authorized button pressed
       private String  cboProcessType1="";
       private ComboBoxModel cbmProcessType1;
       private String oldSanNo1="";
       private String  EmpID1="";
       private String appl_dt1 ="";
       private String req_from1 ="";
       private String req_to1 = "";
       private String noOfdays1 = "";
       private String leavePurpose1 = "";
       private String sanNo1 = "";
       private String sanDate1 = "";
       private boolean withLtc1= false;
       private boolean withLeaveEncashment1= false; 
       private boolean rdoTwoYr1 = false;
       private boolean rdoFourYr1 = false;  
       private String leaveEncashmentDays1= "";
       private String cbotblLeaveType1="";
       private ComboBoxModel cbmtblLeaveType1;
       private String cboPayType1="";
       private ComboBoxModel cbmPayType1;
       private String tabNoOfDays1="";
       private String tblreq_from1 ="";
       private String tblreq_to1 ="";
       private String txtSlNo1="";
       private LinkedHashMap incParMapAppl;
       private ComboBoxModel cbmEncashmentData1;
       private String cboEncashmentData1="";
       private boolean leaveCancel1=false;
       private String remarks1= "";
       private String sanAuth1= "";
       private String cboSanAuth1="";
       private ComboBoxModel cbmSanAuth1;
    
       
       private LinkedHashMap deletedTableMap;
    
    
    /** Creates a new instance of LeaveSanctionOB */
    private LeaveSanctionOB() {
        map = new HashMap();
        map.put(CommonConstants.JNDI, "LeaveSanctionJNDI");
        map.put(CommonConstants.HOME, "serverside.product.share.ShareProductHome");
        map.put(CommonConstants.REMOTE, "serverside.proudct.share.ShareProduct");
        try {
            proxy = ProxyFactory.createProxy();
            setTableTile();
            tblLeaveDetails = new EnhancedTableModel(null, tableTitle);
            tblLeaveDetails1 = new EnhancedTableModel(null, tableTitle);
            fillDropdown();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    static {
        try {
            _log.info("Creating LeaveSanctionOB...");
            objLeaveSanctionOB= new LeaveSanctionOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    private void setTableTile() throws Exception{
        tableTitle.add(resourceBundle.getString("tblColumn1"));
        tableTitle.add(resourceBundle.getString("tblColumn2"));
        tableTitle.add(resourceBundle.getString("tblColumn3"));
        tableTitle.add(resourceBundle.getString("tblColumn4"));
        tableTitle.add(resourceBundle.getString("tblColumn5"));
        tableTitle.add(resourceBundle.getString("tblColumn6"));
        IncVal = new ArrayList();
    }
    
    /* Filling up the the ComboBoxModel with key, value */
    private void fillDropdown() throws Exception{
        try{
            lookupMap = new HashMap();
            lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
            lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
            lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
            HashMap param = new HashMap();
            param.put(CommonConstants.MAP_NAME,null);
            final ArrayList lookupKey = new ArrayList();
            lookupKey.add("LOAN_SANCTION_SAN/EXT");
            lookupKey.add("LEAVE_PAY_TYPE");
            lookupKey.add("TERM_LOAN.SANCTIONING_AUTHORITY");
            param.put(CommonConstants.PARAMFORQUERY, lookupKey);
            final HashMap lookupValues = populateFill(param);
            
            fillData((HashMap)lookupValues.get("LOAN_SANCTION_SAN/EXT"));
            cbmProcessType = new ComboBoxModel(key,value);
            cbmProcessType1 = new ComboBoxModel(key,value);
            
            fillData((HashMap)lookupValues.get("LEAVE_PAY_TYPE"));
            cbmPayType = new ComboBoxModel(key,value);
            cbmPayType1 = new ComboBoxModel(key,value);
            
            fillData((HashMap)lookupValues.get("TERM_LOAN.SANCTIONING_AUTHORITY"));
            cbmSanAuth = new ComboBoxModel(key,value);
            cbmSanAuth1 = new ComboBoxModel(key,value);
            
            param.put(CommonConstants.MAP_NAME,"getLeaveType");
            HashMap where = new HashMap();
            where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            param.put(CommonConstants.PARAMFORQUERY, where);
            where = null;
            keyValue = ClientUtil.populateLookupData(param);
            
            fillData((HashMap)keyValue.get(CommonConstants.DATA));
            cbmLeaveTypeProcess = new ComboBoxModel(key,value);
            cbmLeaveTypeEnquiry = new ComboBoxModel(key,value);
            cbmEncashmentData = new ComboBoxModel(key,value);
            cbmEncashmentData1 = new ComboBoxModel(key,value);
            fillData((HashMap)keyValue.get(CommonConstants.DATA));
            ArrayList keyList = new ArrayList();
            keyList.addAll(key);
            ArrayList valueList = new ArrayList();
            valueList.addAll(value);
            cbmtblLeaveType1 = new ComboBoxModel(keyList,valueList);
            cbmtblLeaveType = new ComboBoxModel(keyList,valueList);
            cbmtblLeaveType.addKeyAndElement("LOSS OF PAY", "LOSS OF PAY");
            key = null;
            value = null;
            
        }catch(NullPointerException e){
            parseException.logException(e,true);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void fillData(HashMap keyValue)  throws Exception{
        key = new ArrayList();
        value = new ArrayList();
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
        
        
    }
    /** To get data for comboboxes */
    private HashMap populateFill(HashMap obj)  throws Exception{
        keyValue = proxy.executeQuery(obj,lookupMap);
        return keyValue;
    }
    
    
    
    /**
     * Returns an instance of LeaveSanctionOB.
     * @return  LeaveSanctionOB
     */
    
    public static LeaveSanctionOB getInstance()throws Exception{
        return objLeaveSanctionOB;
    }
    
    public void setResult(int result) {
        _result = result;
        setResultStatus();
        setChanged();
    }
    
    public int getActionType(){
        return _actionType;
    }
    
    public void setActionType(int actionType) {
        _actionType = actionType;
        setStatus();
        setChanged();
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }
    
    /** Getter for property lblStatus.
     * @return Value of property lblStatus.
     *
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }
    
    /** Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     *
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    public int getResult(){
        return _result;
    }
    
    /** Returns an Instance of LeaveSanctionTO */
    public LeaveSanctionTO getLeaveSanctionTO(String command){
        LeaveSanctionTO objLeaveSanctionTO = new LeaveSanctionTO();
        objLeaveSanctionTO.setProcessType(CommonUtil.convertObjToStr(getCbmProcessType().getKeyForSelected()));
        objLeaveSanctionTO.setAppl_dt(DateUtil.getDateMMDDYYYY(appl_dt));
        objLeaveSanctionTO.setReq_from(DateUtil.getDateMMDDYYYY(getReq_from()));
        objLeaveSanctionTO.setReq_to(DateUtil.getDateMMDDYYYY(getReq_to()));
        objLeaveSanctionTO.setNoOfdays(getNoOfdays());
        objLeaveSanctionTO.setLeavePurpose(getLeavePurpose());
        objLeaveSanctionTO.setSanNo(getSanNo());
        objLeaveSanctionTO.setSanDate(DateUtil.getDateMMDDYYYY(getSanDate()));
        objLeaveSanctionTO.setApplSan(getApplSan());
        objLeaveSanctionTO.setCommand(command);
        objLeaveSanctionTO.setStatusBy(TrueTransactMain.USER_ID);
        objLeaveSanctionTO.setStatusDt(curDate);
        objLeaveSanctionTO.setBranch(TrueTransactMain.BRANCH_ID);
        objLeaveSanctionTO.setEmpID(getEmpID());
        objLeaveSanctionTO.setOldSanNo(getOldSanNo());
        if (isRdoApp_Yes() == true) {
            objLeaveSanctionTO.setApplSan("APPLICATION");
        }
        else {
            objLeaveSanctionTO.setApplSan("SANCTION");
            objLeaveSanctionTO.setleaveApplID(getLeaveID());
            objLeaveSanctionTO.setSanCreatedBy(TrueTransactMain.USER_ID);
            objLeaveSanctionTO.setSanStatusDt(curDate);
            objLeaveSanctionTO.setSanStatusBy(TrueTransactMain.USER_ID);
            objLeaveSanctionTO.setSanCreatedDt(curDate);
        }
        if(command.equalsIgnoreCase("INSERT")){
            objLeaveSanctionTO.setCreatedBy(TrueTransactMain.USER_ID);
            objLeaveSanctionTO.setCreatedDt(curDate);
        }
        if(command.equalsIgnoreCase("UPDATE")||command.equalsIgnoreCase("DELETE")){
            objLeaveSanctionTO.setleaveApplID(getLeaveID());
        }
         if(isWithLtc()){
            objLeaveSanctionTO.setWithLtc(YES);
            if(isRdoTwoYr()==true) objLeaveSanctionTO.setBlockType(SUB_BLOCK);
            else  objLeaveSanctionTO.setBlockType(BLOCK);
        }else{
           objLeaveSanctionTO.setWithLtc(NO);
        }
         if(isWithLeaveEncashment()){
            objLeaveSanctionTO.setWithLeaveEncashment(YES);
            objLeaveSanctionTO.setLeaveEncashmentDays(getLeaveEncashmentDays());
            objLeaveSanctionTO.setCboEncashmentData(CommonUtil.convertObjToStr(getCbmEncashmentData().getKeyForSelected()));
        }else{
            objLeaveSanctionTO.setWithLeaveEncashment(NO);
        }
        if(isLeaveCancel()){
            objLeaveSanctionTO.setLeaveCancel(YES);
        }else{
             objLeaveSanctionTO.setLeaveCancel(NO);
        }
        objLeaveSanctionTO.setRemarks(getRemarks());
        objLeaveSanctionTO.setSanAuthority(CommonUtil.convertObjToStr(getCbmSanAuth().getKeyForSelected()));
        return objLeaveSanctionTO;
        
    }
    
    /** Sets all the LeaveSanction values to the OB varibles  there by populatin the UI fields */
    private void setLeaveSanctionTO(LeaveSanctionTO objLeaveSanctionTO){
        getCbmProcessType().setKeyForSelected(CommonUtil.convertObjToStr(objLeaveSanctionTO.getProcessType()));
        setAppl_dt(CommonUtil.convertObjToStr(objLeaveSanctionTO.getAppl_dt()));
        setReq_from(CommonUtil.convertObjToStr(objLeaveSanctionTO.getReq_from()));
        setReq_to(CommonUtil.convertObjToStr(objLeaveSanctionTO.getReq_to()));
        setNoOfdays(objLeaveSanctionTO.getNoOfdays());
        setLeavePurpose(objLeaveSanctionTO.getLeavePurpose());
        setSanNo(objLeaveSanctionTO.getSanNo());
        setSanDate(CommonUtil.convertObjToStr(objLeaveSanctionTO.getSanDate()));
        setStatusBy(objLeaveSanctionTO.getStatusBy());
        setApplSan(objLeaveSanctionTO.getApplSan());
        setEmpID(objLeaveSanctionTO.getEmpID());
        setLeaveID(objLeaveSanctionTO.getleaveApplID());
        setOldSanNo(objLeaveSanctionTO.getOldSanNo());
        if (objLeaveSanctionTO.getApplSan() != null) {
            if (objLeaveSanctionTO.getApplSan().equals("APPLICATION")) setRdoApp_Yes(true);
            else setRdoSan_Yes(true);
        }
        if(objLeaveSanctionTO.getWithLtc()!= null && objLeaveSanctionTO.getWithLtc().trim().equalsIgnoreCase(YES)){
            setWithLtc(true);
            if (objLeaveSanctionTO.getBlockType()!=null){
                if (objLeaveSanctionTO.getBlockType().equalsIgnoreCase(SUB_BLOCK)) setRdoTwoYr(true);
                else setRdoFourYr(true);
            }
        }else{
            setWithLtc(false);
        }
        
        if(objLeaveSanctionTO.getWithLeaveEncashment()!= null && objLeaveSanctionTO.getWithLeaveEncashment().trim().equalsIgnoreCase(YES)){
            setWithLeaveEncashment(true);
            setLeaveEncashmentDays(objLeaveSanctionTO.getLeaveEncashmentDays());
            getCbmEncashmentData().setKeyForSelected(CommonUtil.convertObjToStr(objLeaveSanctionTO.getCboEncashmentData()));
        }else{
            setWithLeaveEncashment(false);
        }
        setSanStatus(objLeaveSanctionTO.getSanStatus());
        
        if(objLeaveSanctionTO.getLeaveCancel()!= null && objLeaveSanctionTO.getLeaveCancel().trim().equalsIgnoreCase(YES)){
            setLeaveCancel(true);
        }
        else{
             setLeaveCancel(false);
        }
        setRemarks(objLeaveSanctionTO.getRemarks());
        getCbmSanAuth().setKeyForSelected(CommonUtil.convertObjToStr(objLeaveSanctionTO.getSanAuthority()));
        setChanged();
        notifyObservers();
    }
    
    
    private void setLeaveSanctionApplicationTO(LeaveSanctionTO objLeaveSanctionApplicationTO){
        getCbmProcessType1().setKeyForSelected(CommonUtil.convertObjToStr(objLeaveSanctionApplicationTO.getProcessType()));
        setAppl_dt1(CommonUtil.convertObjToStr(objLeaveSanctionApplicationTO.getAppl_dt()));
        setReq_from1(CommonUtil.convertObjToStr(objLeaveSanctionApplicationTO.getReq_from()));
        setReq_to1(CommonUtil.convertObjToStr(objLeaveSanctionApplicationTO.getReq_to()));
        setNoOfdays1(objLeaveSanctionApplicationTO.getNoOfdays());
        setLeavePurpose1(objLeaveSanctionApplicationTO.getLeavePurpose());
        setSanNo1(objLeaveSanctionApplicationTO.getSanNo());
        setSanDate1(CommonUtil.convertObjToStr(objLeaveSanctionApplicationTO.getSanDate()));
        setEmpID1(objLeaveSanctionApplicationTO.getEmpID());
        setLeaveID(objLeaveSanctionApplicationTO.getleaveApplID());
        setOldSanNo1(objLeaveSanctionApplicationTO.getOldSanNo());
        if(objLeaveSanctionApplicationTO.getWithLtc()!= null && objLeaveSanctionApplicationTO.getWithLtc().trim().equalsIgnoreCase(YES)){
            setWithLtc1(true);
            if (objLeaveSanctionApplicationTO.getBlockType()!=null){
                if (objLeaveSanctionApplicationTO.getBlockType().equalsIgnoreCase(SUB_BLOCK)) setRdoTwoYr1(true);
                else setRdoFourYr1(true);
            }
        }else{
            setWithLtc1(false);
        }
        
        if(objLeaveSanctionApplicationTO.getWithLeaveEncashment()!= null && objLeaveSanctionApplicationTO.getWithLeaveEncashment().trim().equalsIgnoreCase(YES)){
            setWithLeaveEncashment1(true);
            setLeaveEncashmentDays1(objLeaveSanctionApplicationTO.getLeaveEncashmentDays());
            getCbmEncashmentData1().setKeyForSelected(CommonUtil.convertObjToStr(objLeaveSanctionApplicationTO.getCboEncashmentData()));
        }else{
            setWithLeaveEncashment1(false);
        }
        
        if(objLeaveSanctionApplicationTO.getLeaveCancel()!= null && objLeaveSanctionApplicationTO.getLeaveCancel().trim().equalsIgnoreCase(YES)){
            setLeaveCancel1(true);
        }
        else{
             setLeaveCancel1(false);
        }
         setRemarks1(objLeaveSanctionApplicationTO.getRemarks());
        getCbmSanAuth1().setKeyForSelected(CommonUtil.convertObjToStr(objLeaveSanctionApplicationTO.getSanAuthority()));
        setChanged();
        notifyObservers();
    }
    
    /* Executes Query using the TO object */
    public void execute(String command , String process) {
        try {
            HashMap term = new HashMap();
            term.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            if(get_authorizeMap() == null && process.equalsIgnoreCase("SAVE")){
                term.put("LeaveSanctionTO", getLeaveSanctionTO(command));
                term.put("TableDetails",incParMap);
                term.put("deletedTableDetails",deletedTableMap);
            }
            else if(((!process.equalsIgnoreCase("SAVE")) || (!process.equalsIgnoreCase("AUTHORIZE"))) && get_authorizeMap() == null){
                term.put("LEAVE_TYPE",command);
                 term.put("EMP_ID",process);
                term.put("PROCESS_EXECUTION","PROCESS_EXECUTION");
            }
            else{
                term.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
                term.put("EMP_ID",getEmpID());
                term.put("PROCESS_TYPE",CommonUtil.convertObjToStr(getCbmProcessType().getKeyForSelected()));
                term.put("TableDetails",incParMap);
                if(isWithLeaveEncashment()){
                    HashMap EncashmentMap = new HashMap();
                    EncashmentMap.put("NO_OF_DAYS_ADDITION", getLeaveEncashmentDays());
                    EncashmentMap.put("ENCASHMENT_LEAVE_TYPE",getEncashLeaveType());
                    term.put("ENCASHMENT_DETAILS",EncashmentMap);
                }
            }
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            HashMap proxyResultMap=null;
            System.out.println("@#$@#$term:"+term);
            proxyResultMap = proxy.execute(term, map);
            System.out.println("proxyResultMap"+proxyResultMap);
            setProxyReturnMap(proxyResultMap);
            if (proxyResultMap != null  && command=="INSERT" && isRdoApp_Yes() == true){
                ClientUtil.showMessageWindow("Transaction No. : " + CommonUtil.convertObjToStr(proxyResultMap.get("LEAVE_ID")));
            }
            setResult(getActionType());
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    
    /** Resetting all tbe Fields of UI */
    public  void resetForm(){
        setRdoApp_Yes(false);
        setRdoSan_Yes(false);
        setCboProcessType("");
        setAppl_dt("");
        setReq_from("");
        setReq_to("");
        setNoOfdays("");
        setSanNo("");
        setSanDate("");
        setLeavePurpose("");
        setTabNoOfDays("");
        tblLeaveDetails.setDataArrayList(null,tableTitle);
        tblLeaveDetails1.setDataArrayList(null,tableTitle);
        setEmpID("");
        setWithLeaveEncashment(false);
        setWithLtc(false);
        setRdoTwoYr(true);
        setRdoFourYr(false);
        incParMap=null;
        setLeaveEncashmentDays("");
        setTblreq_from("");
        setTblreq_to("");
        setCbotblLeaveType("");
        setCboPayType("");
        setCboPayType1("");
        setTabNoOfDays("");
        incParMapAppl=null;
        setCboEncashmentData("");
        setCboEncashmentData1("");
        resetPanSanAppl();
        setReq_from1("");
        setNoOfdays1("");
        setReq_to1("");
        setLeavePurpose1("");
        setEmpID1("");
        setAppl_dt1("");
        setSanStatus("");
        setLeaveID("");
        setOldSanNo("");
        setLeaveCancel(false);
        deletedTableMap=null;
        setCboSanAuth("");
        setCboSanAuth1("");
        setRemarks("");
        resetPanSan();
        setChanged();
        notifyObservers();
    }
    public void resetPanSan() {
        setCbotblLeaveType("");
        setCboPayType("");
        setTabNoOfDays("");
        setTblreq_from("");
        setTblreq_to("");
        setChanged();
    }
    public void resetTableValues(){
        tblLeaveDetails.setDataArrayList(null,tableTitle);
    }
    
     public void resetPanSanAppl() {
        setCbotblLeaveType1("");
        setCboPayType1("");
        setTabNoOfDays1("");
        setTblreq_from1("");
        setTblreq_to1("");
        setChanged();
        notifyObservers();
    }
    
    
    /* Populates the TO object by executing a Query */
     public void populateData(HashMap whereMap) {
         HashMap mapData=null;
         try {
             mapData = proxy.executeQuery(whereMap, map);
             if((getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE) || (getActionType()==ClientConstants.ACTIONTYPE_REJECT) 
             ||(getActionType()==ClientConstants.ACTIONTYPE_VIEW) ||(getActionType()==ClientConstants.ACTIONTYPE_DELETE)){
                 if(mapData.containsKey("LeaveSanctionApplicationTO")){
                     LeaveSanctionTO objLeaveSanctionApplicationTO =  (LeaveSanctionTO) ((List) mapData.get("LeaveSanctionApplicationTO")).get(0);
                     setLeaveSanctionApplicationTO(objLeaveSanctionApplicationTO);
                     if(mapData.containsKey("APP_TABLE")){
                         incParMapAppl = (LinkedHashMap)mapData.get("APP_TABLE");
                         ArrayList addList =new ArrayList(incParMapAppl.keySet());
                         for(int i=0;i<addList.size();i++){
                             objLeaveSanctionApplicationTO = (LeaveSanctionTO)  incParMapAppl.get(addList.get(i));
                             ArrayList incTabRow = new ArrayList();
                             incTabRow.add(CommonUtil.convertObjToStr(objLeaveSanctionApplicationTO.getSlNo()));
                             incTabRow.add(CommonUtil.convertObjToStr(objLeaveSanctionApplicationTO.getTabLeaveType()));
                             incTabRow.add(CommonUtil.convertObjToStr(objLeaveSanctionApplicationTO.getTableNoOfdays()));
                             incTabRow.add(CommonUtil.convertObjToStr(objLeaveSanctionApplicationTO.getTblReqFrom()));
                             incTabRow.add(CommonUtil.convertObjToStr(objLeaveSanctionApplicationTO.getTblReqTo()));
                             incTabRow.add(CommonUtil.convertObjToStr(objLeaveSanctionApplicationTO.getPaymentType()));
                             tblLeaveDetails1.addRow(incTabRow);
                         }
                     }
                 }
             }
             
             if((getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE) || (getActionType()==ClientConstants.ACTIONTYPE_REJECT)||
             (getActionType()==ClientConstants.ACTIONTYPE_VIEW) ||(getActionType()==ClientConstants.ACTIONTYPE_DELETE)){
                 if(mapData.containsKey("LeaveSanctionTO")){
                     LeaveSanctionTO objLeaveSanctionTO =  (LeaveSanctionTO) ((List) mapData.get("LeaveSanctionTO")).get(0);
                     setLeaveSanctionTO(objLeaveSanctionTO);
                 }
                 if(mapData.containsKey("SAN_TABLE")){
                     incParMap = (LinkedHashMap)mapData.get("SAN_TABLE");
                 }
             }else if(whereMap.containsKey("SANCTION") && getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(mapData.containsKey("LeaveSanctionTO")){
                     LeaveSanctionTO objLeaveSanctionTO =  (LeaveSanctionTO) ((List) mapData.get("LeaveSanctionTO")).get(0);
                     setLeaveSanctionTO(objLeaveSanctionTO);
                 }
                 if(mapData.containsKey("SAN_TABLE")){
                     incParMap = (LinkedHashMap)mapData.get("SAN_TABLE");
                 }
             }
              else{
                 if(mapData.containsKey("LeaveSanctionApplicationTO")){
                     LeaveSanctionTO objLeaveSanctionTO =  (LeaveSanctionTO) ((List) mapData.get("LeaveSanctionApplicationTO")).get(0);
                     setLeaveSanctionTO(objLeaveSanctionTO);
                 }
                 if(mapData.containsKey("APP_TABLE")){
                     incParMap = (LinkedHashMap)mapData.get("APP_TABLE");
                 }
                 if(whereMap.containsKey("MODIFICATION_EDIT")){
                   if(mapData.containsKey("SAN_TABLE")){
                     incParMap = (LinkedHashMap)mapData.get("SAN_TABLE");
                 }  
                 }
             }
             if(incParMap!=null){
                 ArrayList addList =new ArrayList(incParMap.keySet());
                 for(int i=0;i<addList.size();i++){
                     LeaveSanctionTO    objLeaveSanctionTO = (LeaveSanctionTO)  incParMap.get(addList.get(i));
                     ArrayList incTabRow = new ArrayList();
                     incTabRow.add(CommonUtil.convertObjToStr(objLeaveSanctionTO.getSlNo()));
                     incTabRow.add(CommonUtil.convertObjToStr(objLeaveSanctionTO.getTabLeaveType()));
                     incTabRow.add(CommonUtil.convertObjToStr(objLeaveSanctionTO.getTableNoOfdays()));
                     incTabRow.add(CommonUtil.convertObjToStr(objLeaveSanctionTO.getTblReqFrom()));
                     incTabRow.add(CommonUtil.convertObjToStr(objLeaveSanctionTO.getTblReqTo()));
                     incTabRow.add(CommonUtil.convertObjToStr(objLeaveSanctionTO.getPaymentType()));
                     tblLeaveDetails.addRow(incTabRow);
                 }
             }
         } catch( Exception e ) {
             setResult(ClientConstants.ACTIONTYPE_FAILED);
             e.printStackTrace();
             parseException.logException(e,true);
             
         }
     }
    
    /**
     * Getter for property cboProcessType.
     * @return Value of property cboProcessType.
     */
    public java.lang.String getCboProcessType() {
        return cboProcessType;
    }
    
    /**
     * Setter for property cboProcessType.
     * @param cboProcessType New value of property cboProcessType.
     */
    public void setCboProcessType(java.lang.String cboProcessType) {
        this.cboProcessType = cboProcessType;
    }
    
    /**
     * Getter for property cbmProcessType.
     * @return Value of property cbmProcessType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProcessType() {
        return cbmProcessType;
    }
    
    /**
     * Setter for property cbmProcessType.
     * @param cbmProcessType New value of property cbmProcessType.
     */
    public void setCbmProcessType(com.see.truetransact.clientutil.ComboBoxModel cbmProcessType) {
        this.cbmProcessType = cbmProcessType;
    }
    
    /**
     * Getter for property rdoApp_Yes.
     * @return Value of property rdoApp_Yes.
     */
    public boolean isRdoApp_Yes() {
        return rdoApp_Yes;
    }
    
    /**
     * Setter for property rdoApp_Yes.
     * @param rdoApp_Yes New value of property rdoApp_Yes.
     */
    public void setRdoApp_Yes(boolean rdoApp_Yes) {
        this.rdoApp_Yes = rdoApp_Yes;
    }
    
    /**
     * Getter for property rdoSan_Yes.
     * @return Value of property rdoSan_Yes.
     */
    public boolean isRdoSan_Yes() {
        return rdoSan_Yes;
    }
    
    /**
     * Setter for property rdoSan_Yes.
     * @param rdoSan_Yes New value of property rdoSan_Yes.
     */
    public void setRdoSan_Yes(boolean rdoSan_Yes) {
        this.rdoSan_Yes = rdoSan_Yes;
    }
    
    /**
     * Getter for property appl_dt.
     * @return Value of property appl_dt.
     */
    public java.lang.String getAppl_dt() {
        return appl_dt;
    }
    
    /**
     * Setter for property appl_dt.
     * @param appl_dt New value of property appl_dt.
     */
    public void setAppl_dt(java.lang.String appl_dt) {
        this.appl_dt = appl_dt;
    }
    
    /**
     * Getter for property req_from.
     * @return Value of property req_from.
     */
    public java.lang.String getReq_from() {
        return req_from;
    }
    
    /**
     * Setter for property req_from.
     * @param req_from New value of property req_from.
     */
    public void setReq_from(java.lang.String req_from) {
        this.req_from = req_from;
    }
    
    /**
     * Getter for property req_to.
     * @return Value of property req_to.
     */
    public java.lang.String getReq_to() {
        return req_to;
    }
    
    /**
     * Setter for property req_to.
     * @param req_to New value of property req_to.
     */
    public void setReq_to(java.lang.String req_to) {
        this.req_to = req_to;
    }
    
    /**
     * Getter for property noOfdays.
     * @return Value of property noOfdays.
     */
    public java.lang.String getNoOfdays() {
        return noOfdays;
    }
    
    /**
     * Setter for property noOfdays.
     * @param noOfdays New value of property noOfdays.
     */
    public void setNoOfdays(java.lang.String noOfdays) {
        this.noOfdays = noOfdays;
    }
    
    /**
     * Getter for property leavePurpose.
     * @return Value of property leavePurpose.
     */
    public java.lang.String getLeavePurpose() {
        return leavePurpose;
    }
    
    /**
     * Setter for property leavePurpose.
     * @param leavePurpose New value of property leavePurpose.
     */
    public void setLeavePurpose(java.lang.String leavePurpose) {
        this.leavePurpose = leavePurpose;
    }
    
    /**
     * Getter for property sanNo.
     * @return Value of property sanNo.
     */
    public java.lang.String getSanNo() {
        return sanNo;
    }
    
    /**
     * Setter for property sanNo.
     * @param sanNo New value of property sanNo.
     */
    public void setSanNo(java.lang.String sanNo) {
        this.sanNo = sanNo;
    }
    
    /**
     * Getter for property sanDate.
     * @return Value of property sanDate.
     */
    public java.lang.String getSanDate() {
        return sanDate;
    }
    
    /**
     * Setter for property sanDate.
     * @param sanDate New value of property sanDate.
     */
    public void setSanDate(java.lang.String sanDate) {
        this.sanDate = sanDate;
    }
    
    /**
     * Getter for property leaveID.
     * @return Value of property leaveID.
     */
    public java.lang.String getLeaveID() {
        return leaveID;
    }
    
    /**
     * Setter for property leaveID.
     * @param leaveID New value of property leaveID.
     */
    public void setLeaveID(java.lang.String leaveID) {
        this.leaveID = leaveID;
    }
    
    
    
    /**
     * Getter for property applSan.
     * @return Value of property applSan.
     */
    public java.lang.String getApplSan() {
        return applSan;
    }
    
    /**
     * Setter for property applSan.
     * @param applSan New value of property applSan.
     */
    public void setApplSan(java.lang.String applSan) {
        this.applSan = applSan;
    }
    
    /**
     * Getter for property empID.
     * @return Value of property empID.
     */
    public java.lang.String getEmpID() {
        return empID;
    }
    
    /**
     * Setter for property empID.
     * @param empID New value of property empID.
     */
    public void setEmpID(java.lang.String empID) {
        this.empID = empID;
    }
    
    public void addToTable(int rowSelected, boolean updateMode){
        try{
            int rowSel=rowSelected;
            final LeaveSanctionTO objLeaveSanctionTO = new LeaveSanctionTO();
            if( incParMap == null ){
                incParMap = new LinkedHashMap();
            }
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isNewData()){
                    if(isRdoApp_Yes()==true){
                        objLeaveSanctionTO.setApplCreatedBy(TrueTransactMain.USER_ID);
                        objLeaveSanctionTO.setApplStatusDt(curDate);
                        objLeaveSanctionTO.setApplStatusBy(TrueTransactMain.USER_ID);
                        objLeaveSanctionTO.setApplCreatedDt(curDate);
                        objLeaveSanctionTO.setAppTblStatus(CommonConstants.STATUS_CREATED);
                    }
                    else{
                        objLeaveSanctionTO.setSanCreatedBy(TrueTransactMain.USER_ID);
                        objLeaveSanctionTO.setSanStatusDt(curDate);
                        objLeaveSanctionTO.setSanStatusBy(TrueTransactMain.USER_ID);
                        objLeaveSanctionTO.setSanCreatedDt(curDate);
                        objLeaveSanctionTO.setSanTblStatus(CommonConstants.STATUS_CREATED);
                    }
                }else{
                    if(isRdoApp_Yes()==true){
                        objLeaveSanctionTO.setApplStatusDt(curDate);
                        objLeaveSanctionTO.setApplStatusBy(TrueTransactMain.USER_ID);
                        objLeaveSanctionTO.setAppTblStatus(CommonConstants.STATUS_MODIFIED);
                    }
                    else{
                        objLeaveSanctionTO.setSanStatusDt(curDate);
                        objLeaveSanctionTO.setSanStatusBy(TrueTransactMain.USER_ID);
                        objLeaveSanctionTO.setSanTblStatus(CommonConstants.STATUS_MODIFIED);
                    }
                }
            }else{
                if(isRdoApp_Yes()==true){
                    objLeaveSanctionTO.setApplCreatedBy(TrueTransactMain.USER_ID);
                    objLeaveSanctionTO.setApplStatusDt(curDate);
                    objLeaveSanctionTO.setApplStatusBy(TrueTransactMain.USER_ID);
                    objLeaveSanctionTO.setApplCreatedDt(curDate);
                    objLeaveSanctionTO.setAppTblStatus(CommonConstants.STATUS_CREATED);
                }
                else{
                    objLeaveSanctionTO.setSanCreatedBy(TrueTransactMain.BRANCH_ID);
                    objLeaveSanctionTO.setSanStatusDt(curDate);
                    objLeaveSanctionTO.setSanStatusBy(TrueTransactMain.BRANCH_ID);
                    objLeaveSanctionTO.setSanCreatedDt(curDate);
                    objLeaveSanctionTO.setSanTblStatus(CommonConstants.STATUS_CREATED);
                }
            }
            int  slno=0;
            int nums[]= new int[50];
            int max=nums[0];
            if(!updateMode){
                ArrayList data = tblLeaveDetails.getDataArrayList();
                slno=serialNo(data);
            }
            else{
                if(isNewData()){
                    ArrayList data = tblLeaveDetails.getDataArrayList();
                    slno=serialNo(data);
                }
                else{
                    int b=CommonUtil.convertObjToInt(tblLeaveDetails.getValueAt(rowSelected,0));
                    slno=b;
                }
            }
            
            objLeaveSanctionTO.setSlNo(String.valueOf(slno));
            objLeaveSanctionTO.setTabLeaveType(CommonUtil.convertObjToStr(getCbmtblLeaveType().getKeyForSelected()));
            objLeaveSanctionTO.setTableNoOfdays(getTabNoOfDays());
            objLeaveSanctionTO.setTblReqFrom(DateUtil.getDateMMDDYYYY(getTblreq_from()));
            objLeaveSanctionTO.setTblReqTo(DateUtil.getDateMMDDYYYY(getTblreq_to()));
            objLeaveSanctionTO.setPaymentType(CommonUtil.convertObjToStr(getCbmPayType().getKeyForSelected()));
            incParMap.put(objLeaveSanctionTO.getSlNo(),objLeaveSanctionTO);
            String sno=String.valueOf(slno);
            updateTblLeave(rowSel,sno,objLeaveSanctionTO);
            notifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    private void updateTblLeave(int rowSel, String sno, LeaveSanctionTO objLeaveSanctionTO)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for(int i = tblLeaveDetails.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblLeaveDetails.getDataArrayList().get(j)).get(0);
            if(sno.equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
                ArrayList IncParRow = new ArrayList();
                ArrayList data = tblLeaveDetails.getDataArrayList();
                data.remove(rowSel);
                IncParRow.add(sno);
                IncParRow.add(getCbotblLeaveType());
                IncParRow.add(getTabNoOfDays());
                IncParRow.add(getTblreq_from());
                IncParRow.add(getTblreq_to());
                IncParRow.add(getCboPayType());
                tblLeaveDetails.insertRow(rowSel,IncParRow);
                IncParRow = null;
            }
        }
        if(!rowExists){
            ArrayList IncParRow = new ArrayList();
            IncParRow.add(sno);
            IncParRow.add(getCbotblLeaveType());
            IncParRow.add(getTabNoOfDays());
            IncParRow.add(getTblreq_from());
            IncParRow.add(getTblreq_to());
            IncParRow.add(getCboPayType());
            tblLeaveDetails.insertRow(tblLeaveDetails.getRowCount(),IncParRow);
            IncParRow = null;
        }
    }
    
    /**
     * Getter for property newData.
     * @return Value of property newData.
     */
    public boolean isNewData() {
        return newData;
    }
    
    /**
     * Setter for property newData.
     * @param newData New value of property newData.
     */
    public void setNewData(boolean newData) {
        this.newData = newData;
    }
    public int serialNo(ArrayList data){
        final int dataSize = data.size();
        int nums[]= new int[50];
        int max=nums[0];
        int slno=0;
        int a=0;
        slno=dataSize+1;
        for(int i=0;i<data.size();i++){
            a=CommonUtil.convertObjToInt(tblLeaveDetails.getValueAt(i,0));
            nums[i]=a;
            if(nums[i]>max)
                max=nums[i];
            slno=max+1;
        }
        return slno;
    }
    
    public void populateLeaveDetails(String row ,String table){
        try{
            if(table.equalsIgnoreCase("APPL_TAB")){
                resetPanSanAppl();
                final LeaveSanctionTO objLeaveSanctionApplTO = (LeaveSanctionTO)incParMapAppl.get(row);
                populateDataAppl(objLeaveSanctionApplTO);
            }
            else{
                resetPanSan();
                final LeaveSanctionTO objLeaveSanctionTO = (LeaveSanctionTO)incParMap.get(row);
                populateData(objLeaveSanctionTO);
            }
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    
    private void populateData(LeaveSanctionTO objLeaveSanctionTO)  throws Exception{
        getCbmtblLeaveType().setKeyForSelected(CommonUtil.convertObjToStr(objLeaveSanctionTO.getTabLeaveType()));
        setTabNoOfDays(CommonUtil.convertObjToStr(objLeaveSanctionTO.getTableNoOfdays()));
        setTxtSlNo(objLeaveSanctionTO.getSlNo());
        setTblreq_from(CommonUtil.convertObjToStr(objLeaveSanctionTO.getTblReqFrom()));
        setTblreq_to(CommonUtil.convertObjToStr(objLeaveSanctionTO.getTblReqTo()));
        getCbmPayType().setKeyForSelected(CommonUtil.convertObjToStr(objLeaveSanctionTO.getPaymentType()));
        setChanged();
        notifyObservers();
    }
    
    private void populateDataAppl(LeaveSanctionTO objLeaveSanctionApplTO)  throws Exception{
        getCbmtblLeaveType1().setKeyForSelected(CommonUtil.convertObjToStr(objLeaveSanctionApplTO.getTabLeaveType()));
        setTabNoOfDays1(CommonUtil.convertObjToStr(objLeaveSanctionApplTO.getTableNoOfdays()));
        setTxtSlNo1(objLeaveSanctionApplTO.getSlNo());
        setTblreq_from1(CommonUtil.convertObjToStr(objLeaveSanctionApplTO.getTblReqFrom()));
        setTblreq_to1(CommonUtil.convertObjToStr(objLeaveSanctionApplTO.getTblReqTo()));
        getCbmPayType1().setKeyForSelected(CommonUtil.convertObjToStr(objLeaveSanctionApplTO.getPaymentType()));
        setChanged();
        notifyObservers();
    }
    
     public void deleteTableData(String val, int row){
        if(deletedTableMap == null){
            deletedTableMap = new LinkedHashMap();
        }
        LeaveSanctionTO objLeaveSanctionTO = (LeaveSanctionTO) incParMap.get(val);
        if(isRdoApp_Yes()==true){
        objLeaveSanctionTO.setAppTblStatus(CommonConstants.STATUS_DELETED);
        }
        else{
            objLeaveSanctionTO.setSanTblStatus(CommonConstants.STATUS_DELETED);
        }
        deletedTableMap.put(CommonUtil.convertObjToStr(tblLeaveDetails.getValueAt(row,0)),incParMap.get(val));
        Object obj;
        obj=val;
        incParMap.remove(val);
        resetTableValues();
        try{
             populateTable();
        }
        catch(Exception e){
            parseException.logException(e,true);
        }
    }
     
       private void populateTable()  throws Exception{
          ArrayList incDataList = new ArrayList();
         incDataList = new ArrayList(incParMap.keySet());
         ArrayList addList =new ArrayList(incParMap.keySet());
         int length = incDataList.size();
          for(int i=0; i<length; i++){
                ArrayList incTabRow = new ArrayList();
                LeaveSanctionTO objLeaveSanctionTO = (LeaveSanctionTO) incParMap.get(addList.get(i));
                
                IncVal.add(objLeaveSanctionTO);
                if( isRdoApp_Yes()==true && !objLeaveSanctionTO.getAppTblStatus().equals("DELETED")){
                incTabRow.add(CommonUtil.convertObjToStr(objLeaveSanctionTO.getSlNo()));
                     incTabRow.add(CommonUtil.convertObjToStr(objLeaveSanctionTO.getTabLeaveType()));
                     incTabRow.add(CommonUtil.convertObjToStr(objLeaveSanctionTO.getTableNoOfdays()));
                     incTabRow.add(CommonUtil.convertObjToStr(objLeaveSanctionTO.getTblReqFrom()));
                     incTabRow.add(CommonUtil.convertObjToStr(objLeaveSanctionTO.getTblReqTo()));
                     incTabRow.add(CommonUtil.convertObjToStr(objLeaveSanctionTO.getPaymentType()));
                incTabRow.add("");
                tblLeaveDetails.addRow(incTabRow);
                }
                else if( isRdoSan_Yes()==true && !objLeaveSanctionTO.getSanTblStatus().equals("DELETED")){
                    incTabRow.add(CommonUtil.convertObjToStr(objLeaveSanctionTO.getSlNo()));
                     incTabRow.add(CommonUtil.convertObjToStr(objLeaveSanctionTO.getTabLeaveType()));
                     incTabRow.add(CommonUtil.convertObjToStr(objLeaveSanctionTO.getTableNoOfdays()));
                     incTabRow.add(CommonUtil.convertObjToStr(objLeaveSanctionTO.getTblReqFrom()));
                     incTabRow.add(CommonUtil.convertObjToStr(objLeaveSanctionTO.getTblReqTo()));
                     incTabRow.add(CommonUtil.convertObjToStr(objLeaveSanctionTO.getPaymentType()));
                incTabRow.add("");
                tblLeaveDetails.addRow(incTabRow);
                }
            }
            notifyObservers();
        
    }
     
    /**
     * Getter for property tabNoOfDays.
     * @return Value of property tabNoOfDays.
     */
    public java.lang.String getTabNoOfDays() {
        return tabNoOfDays;
    }
    
    /**
     * Setter for property tabNoOfDays.
     * @param tabNoOfDays New value of property tabNoOfDays.
     */
    public void setTabNoOfDays(java.lang.String tabNoOfDays) {
        this.tabNoOfDays = tabNoOfDays;
    }
    
    /**
     * Getter for property cbotblLeaveType.
     * @return Value of property cbotblLeaveType.
     */
    public java.lang.String getCbotblLeaveType() {
        return cbotblLeaveType;
    }
    
    /**
     * Setter for property cbotblLeaveType.
     * @param cbotblLeaveType New value of property cbotblLeaveType.
     */
    public void setCbotblLeaveType(java.lang.String cbotblLeaveType) {
        this.cbotblLeaveType = cbotblLeaveType;
    }
    
    /**
     * Getter for property cbmtblLeaveType.
     * @return Value of property cbmtblLeaveType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmtblLeaveType() {
        return cbmtblLeaveType;
    }
    
    /**
     * Setter for property cbmtblLeaveType.
     * @param cbmtblLeaveType New value of property cbmtblLeaveType.
     */
    public void setCbmtblLeaveType(com.see.truetransact.clientutil.ComboBoxModel cbmtblLeaveType) {
        this.cbmtblLeaveType = cbmtblLeaveType;
    }
    
    /**
     * Getter for property tblLeaveDetails.
     * @return Value of property tblLeaveDetails.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblLeaveDetails() {
        return tblLeaveDetails;
    }
    
    /**
     * Setter for property tblLeaveDetails.
     * @param tblLeaveDetails New value of property tblLeaveDetails.
     */
    public void setTblLeaveDetails(com.see.truetransact.clientutil.EnhancedTableModel tblLeaveDetails) {
        this.tblLeaveDetails = tblLeaveDetails;
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
    
    /**
     * Getter for property cboLeaveTypeProcess.
     * @return Value of property cboLeaveTypeProcess.
     */
    public java.lang.String getCboLeaveTypeProcess() {
        return cboLeaveTypeProcess;
    }
    
    /**
     * Setter for property cboLeaveTypeProcess.
     * @param cboLeaveTypeProcess New value of property cboLeaveTypeProcess.
     */
    public void setCboLeaveTypeProcess(java.lang.String cboLeaveTypeProcess) {
        this.cboLeaveTypeProcess = cboLeaveTypeProcess;
    }
    
    /**
     * Getter for property cbmLeaveTypeProcess.
     * @return Value of property cbmLeaveTypeProcess.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmLeaveTypeProcess() {
        return cbmLeaveTypeProcess;
    }
    
    /**
     * Setter for property cbmLeaveTypeProcess.
     * @param cbmLeaveTypeProcess New value of property cbmLeaveTypeProcess.
     */
    public void setCbmLeaveTypeProcess(com.see.truetransact.clientutil.ComboBoxModel cbmLeaveTypeProcess) {
        this.cbmLeaveTypeProcess = cbmLeaveTypeProcess;
    }
    
    /**
     * Getter for property _authorizeMap.
     * @return Value of property _authorizeMap.
     */
    public java.util.HashMap get_authorizeMap() {
        return _authorizeMap;
    }
    
    /**
     * Setter for property _authorizeMap.
     * @param _authorizeMap New value of property _authorizeMap.
     */
    public void set_authorizeMap(java.util.HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }
    
    /**
     * Getter for property oldSanNo.
     * @return Value of property oldSanNo.
     */
    public java.lang.String getOldSanNo() {
        return oldSanNo;
    }
    
    /**
     * Setter for property oldSanNo.
     * @param oldSanNo New value of property oldSanNo.
     */
    public void setOldSanNo(java.lang.String oldSanNo) {
        this.oldSanNo = oldSanNo;
    }
    
    /**
     * Getter for property tblreq_from.
     * @return Value of property tblreq_from.
     */
    public java.lang.String getTblreq_from() {
        return tblreq_from;
    }
    
    /**
     * Setter for property tblreq_from.
     * @param tblreq_from New value of property tblreq_from.
     */
    public void setTblreq_from(java.lang.String tblreq_from) {
        this.tblreq_from = tblreq_from;
    }
    
    /**
     * Getter for property tblreq_to.
     * @return Value of property tblreq_to.
     */
    public java.lang.String getTblreq_to() {
        return tblreq_to;
    }
    
    /**
     * Setter for property tblreq_to.
     * @param tblreq_to New value of property tblreq_to.
     */
    public void setTblreq_to(java.lang.String tblreq_to) {
        this.tblreq_to = tblreq_to;
    }
    
    
    
  
    
    
    /**
     * Getter for property leaveEncashmentDays.
     * @return Value of property leaveEncashmentDays.
     */
    public java.lang.String getLeaveEncashmentDays() {
        return leaveEncashmentDays;
    }
    
    /**
     * Setter for property leaveEncashmentDays.
     * @param leaveEncashmentDays New value of property leaveEncashmentDays.
     */
    public void setLeaveEncashmentDays(java.lang.String leaveEncashmentDays) {
        this.leaveEncashmentDays = leaveEncashmentDays;
    }
    
    /**
     * Getter for property withLtc.
     * @return Value of property withLtc.
     */
    public boolean isWithLtc() {
        return withLtc;
    }
    
    /**
     * Setter for property withLtc.
     * @param withLtc New value of property withLtc.
     */
    public void setWithLtc(boolean withLtc) {
        this.withLtc = withLtc;
    }
    
    /**
     * Getter for property withLeaveEncashment.
     * @return Value of property withLeaveEncashment.
     */
    public boolean isWithLeaveEncashment() {
        return withLeaveEncashment;
    }
    
    /**
     * Setter for property withLeaveEncashment.
     * @param withLeaveEncashment New value of property withLeaveEncashment.
     */
    public void setWithLeaveEncashment(boolean withLeaveEncashment) {
        this.withLeaveEncashment = withLeaveEncashment;
    }
    
    /**
     * Getter for property rdoTwoYr.
     * @return Value of property rdoTwoYr.
     */
    public boolean isRdoTwoYr() {
        return rdoTwoYr;
    }
    
    /**
     * Setter for property rdoTwoYr.
     * @param rdoTwoYr New value of property rdoTwoYr.
     */
    public void setRdoTwoYr(boolean rdoTwoYr) {
        this.rdoTwoYr = rdoTwoYr;
    }
    
    /**
     * Getter for property rdoFourYr.
     * @return Value of property rdoFourYr.
     */
    public boolean isRdoFourYr() {
        return rdoFourYr;
    }
    
    /**
     * Setter for property rdoFourYr.
     * @param rdoFourYr New value of property rdoFourYr.
     */
    public void setRdoFourYr(boolean rdoFourYr) {
        this.rdoFourYr = rdoFourYr;
    }
    
    /**
     * Getter for property cbmLeaveTypeEnquiry.
     * @return Value of property cbmLeaveTypeEnquiry.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmLeaveTypeEnquiry() {
        return cbmLeaveTypeEnquiry;
    }
    
    /**
     * Setter for property cbmLeaveTypeEnquiry.
     * @param cbmLeaveTypeEnquiry New value of property cbmLeaveTypeEnquiry.
     */
    public void setCbmLeaveTypeEnquiry(com.see.truetransact.clientutil.ComboBoxModel cbmLeaveTypeEnquiry) {
        this.cbmLeaveTypeEnquiry = cbmLeaveTypeEnquiry;
    }
    
    /**
     * Getter for property cboPayType.
     * @return Value of property cboPayType.
     */
    public java.lang.String getCboPayType() {
        return cboPayType;
    }
    
    /**
     * Setter for property cboPayType.
     * @param cboPayType New value of property cboPayType.
     */
    public void setCboPayType(java.lang.String cboPayType) {
        this.cboPayType = cboPayType;
    }
    
    /**
     * Getter for property cbmPayType.
     * @return Value of property cbmPayType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPayType() {
        return cbmPayType;
    }
    
    /**
     * Setter for property cbmPayType.
     * @param cbmPayType New value of property cbmPayType.
     */
    public void setCbmPayType(com.see.truetransact.clientutil.ComboBoxModel cbmPayType) {
        this.cbmPayType = cbmPayType;
    }
    
    /**
     * Getter for property cboProcessType1.
     * @return Value of property cboProcessType1.
     */
    public java.lang.String getCboProcessType1() {
        return cboProcessType1;
    }
    
    /**
     * Setter for property cboProcessType1.
     * @param cboProcessType1 New value of property cboProcessType1.
     */
    public void setCboProcessType1(java.lang.String cboProcessType1) {
        this.cboProcessType1 = cboProcessType1;
    }
    
    /**
     * Getter for property cbmProcessType1.
     * @return Value of property cbmProcessType1.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProcessType1() {
        return cbmProcessType1;
    }
    
    /**
     * Setter for property cbmProcessType1.
     * @param cbmProcessType1 New value of property cbmProcessType1.
     */
    public void setCbmProcessType1(com.see.truetransact.clientutil.ComboBoxModel cbmProcessType1) {
        this.cbmProcessType1 = cbmProcessType1;
    }
    
    /**
     * Getter for property oldSanNo1.
     * @return Value of property oldSanNo1.
     */
    public java.lang.String getOldSanNo1() {
        return oldSanNo1;
    }
    
    /**
     * Setter for property oldSanNo1.
     * @param oldSanNo1 New value of property oldSanNo1.
     */
    public void setOldSanNo1(java.lang.String oldSanNo1) {
        this.oldSanNo1 = oldSanNo1;
    }
    
    /**
     * Getter for property EmpID1.
     * @return Value of property EmpID1.
     */
    public java.lang.String getEmpID1() {
        return EmpID1;
    }
    
    /**
     * Setter for property EmpID1.
     * @param EmpID1 New value of property EmpID1.
     */
    public void setEmpID1(java.lang.String EmpID1) {
        this.EmpID1 = EmpID1;
    }
    
    /**
     * Getter for property appl_dt1.
     * @return Value of property appl_dt1.
     */
    public java.lang.String getAppl_dt1() {
        return appl_dt1;
    }
    
    /**
     * Setter for property appl_dt1.
     * @param appl_dt1 New value of property appl_dt1.
     */
    public void setAppl_dt1(java.lang.String appl_dt1) {
        this.appl_dt1 = appl_dt1;
    }
    
    /**
     * Getter for property req_from1.
     * @return Value of property req_from1.
     */
    public java.lang.String getReq_from1() {
        return req_from1;
    }
    
    /**
     * Setter for property req_from1.
     * @param req_from1 New value of property req_from1.
     */
    public void setReq_from1(java.lang.String req_from1) {
        this.req_from1 = req_from1;
    }
    
    /**
     * Getter for property req_to1.
     * @return Value of property req_to1.
     */
    public java.lang.String getReq_to1() {
        return req_to1;
    }
    
    /**
     * Setter for property req_to1.
     * @param req_to1 New value of property req_to1.
     */
    public void setReq_to1(java.lang.String req_to1) {
        this.req_to1 = req_to1;
    }
    
    /**
     * Getter for property noOfdays1.
     * @return Value of property noOfdays1.
     */
    public java.lang.String getNoOfdays1() {
        return noOfdays1;
    }
    
    /**
     * Setter for property noOfdays1.
     * @param noOfdays1 New value of property noOfdays1.
     */
    public void setNoOfdays1(java.lang.String noOfdays1) {
        this.noOfdays1 = noOfdays1;
    }
    
    /**
     * Getter for property leavePurpose1.
     * @return Value of property leavePurpose1.
     */
    public java.lang.String getLeavePurpose1() {
        return leavePurpose1;
    }
    
    /**
     * Setter for property leavePurpose1.
     * @param leavePurpose1 New value of property leavePurpose1.
     */
    public void setLeavePurpose1(java.lang.String leavePurpose1) {
        this.leavePurpose1 = leavePurpose1;
    }
    
    /**
     * Getter for property sanNo1.
     * @return Value of property sanNo1.
     */
    public java.lang.String getSanNo1() {
        return sanNo1;
    }
    
    /**
     * Setter for property sanNo1.
     * @param sanNo1 New value of property sanNo1.
     */
    public void setSanNo1(java.lang.String sanNo1) {
        this.sanNo1 = sanNo1;
    }
    
    /**
     * Getter for property sanDate1.
     * @return Value of property sanDate1.
     */
    public java.lang.String getSanDate1() {
        return sanDate1;
    }
    
    /**
     * Setter for property sanDate1.
     * @param sanDate1 New value of property sanDate1.
     */
    public void setSanDate1(java.lang.String sanDate1) {
        this.sanDate1 = sanDate1;
    }
    
    /**
     * Getter for property withLtc1.
     * @return Value of property withLtc1.
     */
    public boolean isWithLtc1() {
        return withLtc1;
    }
    
    /**
     * Setter for property withLtc1.
     * @param withLtc1 New value of property withLtc1.
     */
    public void setWithLtc1(boolean withLtc1) {
        this.withLtc1 = withLtc1;
    }
    
    /**
     * Getter for property withLeaveEncashment1.
     * @return Value of property withLeaveEncashment1.
     */
    public boolean isWithLeaveEncashment1() {
        return withLeaveEncashment1;
    }
    
    /**
     * Setter for property withLeaveEncashment1.
     * @param withLeaveEncashment1 New value of property withLeaveEncashment1.
     */
    public void setWithLeaveEncashment1(boolean withLeaveEncashment1) {
        this.withLeaveEncashment1 = withLeaveEncashment1;
    }
    
    /**
     * Getter for property rdoTwoYr1.
     * @return Value of property rdoTwoYr1.
     */
    public boolean isRdoTwoYr1() {
        return rdoTwoYr1;
    }
    
    /**
     * Setter for property rdoTwoYr1.
     * @param rdoTwoYr1 New value of property rdoTwoYr1.
     */
    public void setRdoTwoYr1(boolean rdoTwoYr1) {
        this.rdoTwoYr1 = rdoTwoYr1;
    }
    
    /**
     * Getter for property rdoFourYr1.
     * @return Value of property rdoFourYr1.
     */
    public boolean isRdoFourYr1() {
        return rdoFourYr1;
    }
    
    /**
     * Setter for property rdoFourYr1.
     * @param rdoFourYr1 New value of property rdoFourYr1.
     */
    public void setRdoFourYr1(boolean rdoFourYr1) {
        this.rdoFourYr1 = rdoFourYr1;
    }
    
    /**
     * Getter for property leaveEncashmentDays1.
     * @return Value of property leaveEncashmentDays1.
     */
    public java.lang.String getLeaveEncashmentDays1() {
        return leaveEncashmentDays1;
    }
    
    /**
     * Setter for property leaveEncashmentDays1.
     * @param leaveEncashmentDays1 New value of property leaveEncashmentDays1.
     */
    public void setLeaveEncashmentDays1(java.lang.String leaveEncashmentDays1) {
        this.leaveEncashmentDays1 = leaveEncashmentDays1;
    }
    
    /**
     * Getter for property cbotblLeaveType1.
     * @return Value of property cbotblLeaveType1.
     */
    public java.lang.String getCbotblLeaveType1() {
        return cbotblLeaveType1;
    }
    
    /**
     * Setter for property cbotblLeaveType1.
     * @param cbotblLeaveType1 New value of property cbotblLeaveType1.
     */
    public void setCbotblLeaveType1(java.lang.String cbotblLeaveType1) {
        this.cbotblLeaveType1 = cbotblLeaveType1;
    }
    
    /**
     * Getter for property cbmtblLeaveType1.
     * @return Value of property cbmtblLeaveType1.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmtblLeaveType1() {
        return cbmtblLeaveType1;
    }
    
    /**
     * Setter for property cbmtblLeaveType1.
     * @param cbmtblLeaveType1 New value of property cbmtblLeaveType1.
     */
    public void setCbmtblLeaveType1(com.see.truetransact.clientutil.ComboBoxModel cbmtblLeaveType1) {
        this.cbmtblLeaveType1 = cbmtblLeaveType1;
    }
    
    /**
     * Getter for property cboPayType1.
     * @return Value of property cboPayType1.
     */
    public java.lang.String getCboPayType1() {
        return cboPayType1;
    }
    
    /**
     * Setter for property cboPayType1.
     * @param cboPayType1 New value of property cboPayType1.
     */
    public void setCboPayType1(java.lang.String cboPayType1) {
        this.cboPayType1 = cboPayType1;
    }
    
    /**
     * Getter for property cbmPayType1.
     * @return Value of property cbmPayType1.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPayType1() {
        return cbmPayType1;
    }
    
    /**
     * Setter for property cbmPayType1.
     * @param cbmPayType1 New value of property cbmPayType1.
     */
    public void setCbmPayType1(com.see.truetransact.clientutil.ComboBoxModel cbmPayType1) {
        this.cbmPayType1 = cbmPayType1;
    }
    
    /**
     * Getter for property tabNoOfDays1.
     * @return Value of property tabNoOfDays1.
     */
    public java.lang.String getTabNoOfDays1() {
        return tabNoOfDays1;
    }
    
    /**
     * Setter for property tabNoOfDays1.
     * @param tabNoOfDays1 New value of property tabNoOfDays1.
     */
    public void setTabNoOfDays1(java.lang.String tabNoOfDays1) {
        this.tabNoOfDays1 = tabNoOfDays1;
    }
    
    /**
     * Getter for property tblreq_from1.
     * @return Value of property tblreq_from1.
     */
    public java.lang.String getTblreq_from1() {
        return tblreq_from1;
    }
    
    /**
     * Setter for property tblreq_from1.
     * @param tblreq_from1 New value of property tblreq_from1.
     */
    public void setTblreq_from1(java.lang.String tblreq_from1) {
        this.tblreq_from1 = tblreq_from1;
    }
    
    /**
     * Getter for property tblreq_to1.
     * @return Value of property tblreq_to1.
     */
    public java.lang.String getTblreq_to1() {
        return tblreq_to1;
    }
    
    /**
     * Setter for property tblreq_to1.
     * @param tblreq_to1 New value of property tblreq_to1.
     */
    public void setTblreq_to1(java.lang.String tblreq_to1) {
        this.tblreq_to1 = tblreq_to1;
    }
    
    /**
     * Getter for property txtSlNo1.
     * @return Value of property txtSlNo1.
     */
    public java.lang.String getTxtSlNo1() {
        return txtSlNo1;
    }
    
    /**
     * Setter for property txtSlNo1.
     * @param txtSlNo1 New value of property txtSlNo1.
     */
    public void setTxtSlNo1(java.lang.String txtSlNo1) {
        this.txtSlNo1 = txtSlNo1;
    }
    
    /**
     * Getter for property tblLeaveDetails1.
     * @return Value of property tblLeaveDetails1.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblLeaveDetails1() {
        return tblLeaveDetails1;
    }
    
    /**
     * Setter for property tblLeaveDetails1.
     * @param tblLeaveDetails1 New value of property tblLeaveDetails1.
     */
    public void setTblLeaveDetails1(com.see.truetransact.clientutil.EnhancedTableModel tblLeaveDetails1) {
        this.tblLeaveDetails1 = tblLeaveDetails1;
    }
    
    /**
     * Getter for property cbmEncashmentData.
     * @return Value of property cbmEncashmentData.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmEncashmentData() {
        return cbmEncashmentData;
    }
    
    /**
     * Setter for property cbmEncashmentData.
     * @param cbmEncashmentData New value of property cbmEncashmentData.
     */
    public void setCbmEncashmentData(com.see.truetransact.clientutil.ComboBoxModel cbmEncashmentData) {
        this.cbmEncashmentData = cbmEncashmentData;
    }
    
    /**
     * Getter for property cboEncashmentData.
     * @return Value of property cboEncashmentData.
     */
    public java.lang.String getCboEncashmentData() {
        return cboEncashmentData;
    }
    
    /**
     * Setter for property cboEncashmentData.
     * @param cboEncashmentData New value of property cboEncashmentData.
     */
    public void setCboEncashmentData(java.lang.String cboEncashmentData) {
        this.cboEncashmentData = cboEncashmentData;
    }
    
    /**
     * Getter for property cbmEncashmentData1.
     * @return Value of property cbmEncashmentData1.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmEncashmentData1() {
        return cbmEncashmentData1;
    }
    
    /**
     * Setter for property cbmEncashmentData1.
     * @param cbmEncashmentData1 New value of property cbmEncashmentData1.
     */
    public void setCbmEncashmentData1(com.see.truetransact.clientutil.ComboBoxModel cbmEncashmentData1) {
        this.cbmEncashmentData1 = cbmEncashmentData1;
    }
    
    /**
     * Getter for property cboEncashmentData1.
     * @return Value of property cboEncashmentData1.
     */
    private java.lang.String getCboEncashmentData1() {
        return cboEncashmentData1;
    }
    
    /**
     * Setter for property cboEncashmentData1.
     * @param cboEncashmentData1 New value of property cboEncashmentData1.
     */
    public void setCboEncashmentData1(java.lang.String cboEncashmentData1) {
        this.cboEncashmentData1 = cboEncashmentData1;
    }
    
    /**
     * Getter for property encashLeaveType.
     * @return Value of property encashLeaveType.
     */
    public java.lang.String getEncashLeaveType() {
        return encashLeaveType;
    }
    
    /**
     * Setter for property encashLeaveType.
     * @param encashLeaveType New value of property encashLeaveType.
     */
    public void setEncashLeaveType(java.lang.String encashLeaveType) {
        this.encashLeaveType = encashLeaveType;
    }
    
    /**
     * Getter for property sanStatus.
     * @return Value of property sanStatus.
     */
    public java.lang.String getSanStatus() {
        return sanStatus;
    }
    
    /**
     * Setter for property sanStatus.
     * @param sanStatus New value of property sanStatus.
     */
    public void setSanStatus(java.lang.String sanStatus) {
        this.sanStatus = sanStatus;
    }
    
    /**
     * Getter for property leaveCancel.
     * @return Value of property leaveCancel.
     */
    public boolean isLeaveCancel() {
        return leaveCancel;
    }    
  
    /**
     * Setter for property leaveCancel.
     * @param leaveCancel New value of property leaveCancel.
     */
    public void setLeaveCancel(boolean leaveCancel) {
        this.leaveCancel = leaveCancel;
    }    
    
    /**
     * Getter for property leaveCancel1.
     * @return Value of property leaveCancel1.
     */
    public boolean isLeaveCancel1() {
        return leaveCancel1;
    }
    
    /**
     * Setter for property leaveCancel1.
     * @param leaveCancel1 New value of property leaveCancel1.
     */
    public void setLeaveCancel1(boolean leaveCancel1) {
        this.leaveCancel1 = leaveCancel1;
    }
    
    /**
     * Getter for property remarks.
     * @return Value of property remarks.
     */
    public java.lang.String getRemarks() {
        return remarks;
    }
    
    /**
     * Setter for property remarks.
     * @param remarks New value of property remarks.
     */
    public void setRemarks(java.lang.String remarks) {
        this.remarks = remarks;
    }
    
    /**
     * Getter for property sanAuth.
     * @return Value of property sanAuth.
     */
    public java.lang.String getSanAuth() {
        return sanAuth;
    }
    
    /**
     * Setter for property sanAuth.
     * @param sanAuth New value of property sanAuth.
     */
    public void setSanAuth(java.lang.String sanAuth) {
        this.sanAuth = sanAuth;
    }
    
    /**
     * Getter for property remarks1.
     * @return Value of property remarks1.
     */
    public java.lang.String getRemarks1() {
        return remarks1;
    }
    
    /**
     * Setter for property remarks1.
     * @param remarks1 New value of property remarks1.
     */
    public void setRemarks1(java.lang.String remarks1) {
        this.remarks1 = remarks1;
    }
    
    /**
     * Getter for property sanAuth1.
     * @return Value of property sanAuth1.
     */
    public java.lang.String getSanAuth1() {
        return sanAuth1;
    }
    
    /**
     * Setter for property sanAuth1.
     * @param sanAuth1 New value of property sanAuth1.
     */
    public void setSanAuth1(java.lang.String sanAuth1) {
        this.sanAuth1 = sanAuth1;
    }
    
    /**
     * Getter for property cboSanAuth.
     * @return Value of property cboSanAuth.
     */
    public java.lang.String getCboSanAuth() {
        return cboSanAuth;
    }
    
    /**
     * Setter for property cboSanAuth.
     * @param cboSanAuth New value of property cboSanAuth.
     */
    public void setCboSanAuth(java.lang.String cboSanAuth) {
        this.cboSanAuth = cboSanAuth;
    }
    
    /**
     * Getter for property cbmSanAuth.
     * @return Value of property cbmSanAuth.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmSanAuth() {
        return cbmSanAuth;
    }
    
    /**
     * Setter for property cbmSanAuth.
     * @param cbmSanAuth New value of property cbmSanAuth.
     */
    public void setCbmSanAuth(com.see.truetransact.clientutil.ComboBoxModel cbmSanAuth) {
        this.cbmSanAuth = cbmSanAuth;
    }
    
    /**
     * Getter for property cboSanAuth1.
     * @return Value of property cboSanAuth1.
     */
    public java.lang.String getCboSanAuth1() {
        return cboSanAuth1;
    }
    
    /**
     * Setter for property cboSanAuth1.
     * @param cboSanAuth1 New value of property cboSanAuth1.
     */
    public void setCboSanAuth1(java.lang.String cboSanAuth1) {
        this.cboSanAuth1 = cboSanAuth1;
    }
    
    /**
     * Getter for property cbmSanAuth1.
     * @return Value of property cbmSanAuth1.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmSanAuth1() {
        return cbmSanAuth1;
    }
    
    /**
     * Setter for property cbmSanAuth1.
     * @param cbmSanAuth1 New value of property cbmSanAuth1.
     */
    public void setCbmSanAuth1(com.see.truetransact.clientutil.ComboBoxModel cbmSanAuth1) {
        this.cbmSanAuth1 = cbmSanAuth1;
    }
    
}