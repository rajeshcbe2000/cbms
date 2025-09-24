/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SuspiciousConfigOB.java
 *
 * Created on Sat Jan 08 14:57:46 IST 2005
 */

package com.see.truetransact.ui.sysadmin.suspiciousconfig;


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
import com.see.truetransact.transferobject.sysadmin.suspiciousconfig.SuspiciousConfigTO;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyParameters;

/**
 *
 * @author
 */

public class SuspiciousConfigOB extends CObservable {
    
    private String cboConfigurationFor = "";
    private String cboProdType = "";
    private String cboProdId = "";
    private String txtAccNo = "";
    private String txtCustNo = "";
    private String txtCountExceeds = "";
    private String txtWorthExceeds = "";
//    private String cboTransactionType = "";
    private String txtPeriod = "";
    private String cboPeriod = "";
    
    private boolean chkCreditCash = false;
    private boolean chkCreditTransfer = false;
    private boolean chkDebitClearing = false;
    private boolean chkDebitTransfer = false;
    private boolean chkDebitCash = false;
    private boolean chkCreditClearing = false;
    
    private final String YES = "Y";
    private final String NO = "N";
    
    private double period = 0;
    private int periodTxtValue = 0;
    private static int SL_NO = 1;
    
    private String ConfigStatus = "";
    private String statusBy = "";
    private String statusDt = "";
    private String confKey = "";
    private String serialKey = "";
    
    private ComboBoxModel cbmProdType;
    private ComboBoxModel cbmProdId;
    private ComboBoxModel cbmConfigFor;
//    private ComboBoxModel cbmTransType;
    private ComboBoxModel cbmPeriod;
    
    private HashMap lookUpHash;
    private HashMap keyValue;
    
    private ArrayList key;
    private ArrayList value;
    
    private ArrayList rowData;
    private static int serialNo = 1;// To maintain serial No in Suspicious Config Details Table
    private static int count = 1;// To maintain No of Suspicious Config Details deleted
    
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int actionType;
    private int result;
    HashMap operationMap;
    final ArrayList tableTitle = new ArrayList();
    ProxyFactory proxy;
    private EnhancedTableModel tblSuspiciousConfig;
    private LinkedHashMap suspiciousConfigTO = null;// Contains Suspicious Config Details which the Status is not DELETED
    private LinkedHashMap deletedsuspiciousConfigTO = null;// Contains Suspicious Config Details which the Status is DELETED
    private LinkedHashMap totalsuspiciousConfigTO = null;// Contains Both Suspicious Config Details
    
    private final String TO_DELETED_AT_UPDATE_MODE = "TO_DELETED_AT_UPDATE_MODE";
    private final String TO_NOT_DELETED_AT_UPDATE_MODE = "TO_NOT_DELETED_AT_UPDATE_MODE";
    
//    final SuspiciousConfigRB resourceBundle = new SuspiciousConfigRB();
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.sysadmin.suspiciousconfig.SuspiciousConfigRB", ProxyParameters.LANGUAGE);
    
    
    private static SuspiciousConfigOB objSuspiciousConfigOB; // singleton object
    
    private final static Logger log = Logger.getLogger(SuspiciousConfigOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    static {
        try {
            log.info("Creating SuspiciousConfigOB...");
            objSuspiciousConfigOB = new SuspiciousConfigOB();
        } catch(Exception e) {
            //_log.error(e);
            parseException.logException(e,true);
        }
    }
    
    /**
     * Returns an instance of OtherBankOB.
     * @return  OtherBankOB
     */
    public static SuspiciousConfigOB getInstance() {
        return objSuspiciousConfigOB;
    }
    /** Creates a new instance of HeadOB */
    private SuspiciousConfigOB() throws Exception{
        proxy = ProxyFactory.createProxy();
        setOperationMap();
        fillDropdown();
        setTableTile();
        tblSuspiciousConfig = new EnhancedTableModel(null, tableTitle);
        serialNo = 1;
    }
    /* Sets _subHeadTitle with table column headers */
    private void setTableTile() throws Exception{
        tableTitle.add(resourceBundle.getString("tblColumn1"));
        tableTitle.add(resourceBundle.getString("tblColumn2"));
        tableTitle.add(resourceBundle.getString("tblColumn3"));
        tableTitle.add(resourceBundle.getString("tblColumn4"));
    }
    /* Sets the HashMap required to set JNDI,Home and Remote*/
    private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "SuspiciousConfigJNDI");
        operationMap.put(CommonConstants.HOME, "com.see.truetransact.serverside.sysadmin.otherbank.SuspiciousConfigHome");
        operationMap.put(CommonConstants.REMOTE, "com.see.truetransact.serverside.sysadmin.otherbank.SuspiciousConfig");
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    public void fillDropdown() throws Exception{
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();

        key=new ArrayList();                             //5 lines added by Rajesh
        value=new ArrayList();
        key.add("");
        value.add("");
        cbmProdId = new ComboBoxModel(key,value);
        
        lookup_keys.add("SUSPECIOUS.CONFIG");
        lookup_keys.add("PRODUCTTYPE");
//        lookup_keys.add("SUSPECIOUS.TRANSTYPE");
        lookup_keys.add("PERIOD");
        
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("SUSPECIOUS.CONFIG"));
        cbmConfigFor = new ComboBoxModel(key,value);
        
//        keyValue = ClientUtil.populateLookupData(lookUpHash);     //Commented by Rajesh
        getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
        cbmProdType = new ComboBoxModel(key,value);
        
//        keyValue = ClientUtil.populateLookupData(lookUpHash);
//        getKeyValue((HashMap)keyValue.get("SUSPECIOUS.TRANSTYPE"));
//        cbmTransType = new ComboBoxModel(key,value);
        
//        keyValue = ClientUtil.populateLookupData(lookUpHash);     //Commented by Rajesh
        getKeyValue((HashMap)keyValue.get("PERIOD"));
        cbmPeriod = new ComboBoxModel(key,value);
        
        makeComboBoxKeyValuesNull();
    }
    
    private void makeComboBoxKeyValuesNull(){
        key = null;
        value = null;
        lookUpHash = null;
        keyValue = null;
    }
    
    void setTblSuspiciousConfig(EnhancedTableModel tblSuspiciousConfig){
        this.tblSuspiciousConfig = tblSuspiciousConfig;
        setChanged();
    }
    EnhancedTableModel getTblSuspiciousConfig(){
        return this.tblSuspiciousConfig;
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
    
    // Setter method for confKey
    void setConfigKey(String confKey){
        this.confKey = confKey;
        setChanged();
    }
    // Getter method for confKey
    String getConfigKey(){
        return this.confKey;
    }
    // Setter method for serialKey
    void setSL_NO(String serialKey){
        this.serialKey = serialKey;
        setChanged();
    }
    // Getter method for SL_NO
    String getSL_NO(){
        return this.serialKey;
    }
    
    // Setter method for ConfigStatus
    void setConfigurationStatus(String ConfigStatus){
        this.ConfigStatus = ConfigStatus;
        setChanged();
    }
    // Getter method for ConfigStatus
    String getConfigurationStatus(){
        return this.ConfigStatus;
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
    // Setter method for statusDt
    void setStatusDt(String statusDt){
        this.statusDt = statusDt;
        setChanged();
    }
    // Getter method for statusDt
    String getStatusDt(){
        return this.statusDt;
    }
    
    // Setter method for cboConfigurationFor
    void setCboConfigurationFor(String cboConfigurationFor){
        this.cboConfigurationFor = cboConfigurationFor;
        setChanged();
    }
    // Getter method for cboConfigurationFor
    String getCboConfigurationFor(){
        return this.cboConfigurationFor;
    }
    
    // Setter method for cboProdType
    void setCboProdType(String cboProdType){
        this.cboProdType = cboProdType;
        setChanged();
    }
    // Getter method for cboProdType
    String getCboProdType(){
        return this.cboProdType;
    }
    
    // Setter method for cboProdId
    void setCboProdId(String cboProdId){
        this.cboProdId = cboProdId;
        setChanged();
    }
    // Getter method for cboProdId
    String getCboProdId(){
        return this.cboProdId;
    }
    
    // Setter method for txtAccNo
    void setTxtAccNo(String txtAccNo){
        this.txtAccNo = txtAccNo;
        setChanged();
    }
    // Getter method for txtAccNo
    String getTxtAccNo(){
        return this.txtAccNo;
    }
    
    // Setter method for txtCustNo
    void setTxtCustNo(String txtCustNo){
        this.txtCustNo = txtCustNo;
        setChanged();
    }
    // Getter method for txtCustNo
    String getTxtCustNo(){
        return this.txtCustNo;
    }
    
    // Setter method for txtCountExceeds
    void setTxtCountExceeds(String txtCountExceeds){
        this.txtCountExceeds = txtCountExceeds;
        setChanged();
    }
    // Getter method for txtCountExceeds
    String getTxtCountExceeds(){
        return this.txtCountExceeds;
    }
    
    // Setter method for txtWorthExceeds
    void setTxtWorthExceeds(String txtWorthExceeds){
        this.txtWorthExceeds = txtWorthExceeds;
        setChanged();
    }
    // Getter method for txtWorthExceeds
    String getTxtWorthExceeds(){
        return this.txtWorthExceeds;
    }
    
    // Setter method for cboTransactionType
//    void setCboTransactionType(String cboTransactionType){
//        this.cboTransactionType = cboTransactionType;
//        setChanged();
//    }
//    // Getter method for cboTransactionType
//    String getCboTransactionType(){
//        return this.cboTransactionType;
//    }
    
    // Setter method for txtPeriod
    void setTxtPeriod(String txtPeriod){
        this.txtPeriod = txtPeriod;
        setChanged();
    }
    // Getter method for txtPeriod
    String getTxtPeriod(){
        return this.txtPeriod;
    }
    
    // Setter method for cboPeriod
    void setCboPeriod(String cboPeriod){
        this.cboPeriod = cboPeriod;
        setChanged();
    }
    // Getter method for cboPeriod
    String getCboPeriod(){
        return this.cboPeriod;
    }
    // Other Bank Branch
    
//    void setCbmTransType(ComboBoxModel cbmTransType){
//        this.cbmTransType = cbmTransType;
//        setChanged();
//    }
//    
//    ComboBoxModel getCbmTransType(){
//        return this.cbmTransType;
//    }
    
    void setCbmProdType(ComboBoxModel cbmProdType){
        this.cbmProdType = cbmProdType;
        setChanged();
    }
    
    ComboBoxModel getCbmProdType(){
        return this.cbmProdType;
    }
    void setCbmConfigFor(ComboBoxModel cbmConfigFor){
        this.cbmConfigFor = cbmConfigFor;
        setChanged();
    }
    
    ComboBoxModel getCbmConfigFor(){
        return this.cbmConfigFor;
    }
    //    void setCbmProdId(ComboBoxModel cbmProdId){
    //        this.cbmProdId = cbmProdId;
    //        setChanged();
    //    }
    
    
    
    
    
    
    public void setCbmProdId(String prodType) {
        try {
            if (prodType != null && prodType.length()>0 && !prodType.equals("")) {
                lookUpHash = new HashMap();
                lookUpHash.put(CommonConstants.MAP_NAME,"Cash.getAccProduct" + prodType);
                lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
                cbmProdId = new ComboBoxModel(key,value);
                this.cbmProdId = cbmProdId;
                setChanged();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    ComboBoxModel getCbmProdId(){
        return this.cbmProdId;
    }
    void setCbmPeriod(ComboBoxModel cbmPeriod){
        this.cbmPeriod = cbmPeriod;
        setChanged();
    }
    
    ComboBoxModel getCbmPeriod(){
        return this.cbmPeriod;
    }
    /**
     * Set Suspicious ConfigOB fields in the OB
     */
    private void setSuspiciousConfigOB(SuspiciousConfigTO objSuspiciousConfigTO) throws Exception {
        log.info("Inside setSuspiciousConfigOB()");
        
        setConfigKey(objSuspiciousConfigTO.getConfKey());
        setSL_NO(objSuspiciousConfigTO.getSlNo());
        setConfigurationStatus(objSuspiciousConfigTO.getStatus());
        setStatusBy(objSuspiciousConfigTO.getStatusBy());
        setStatusDt(DateUtil.getStringDate(objSuspiciousConfigTO.getStatusDt()));
        setCboConfigurationFor((String) getCbmConfigFor().getDataForKey(objSuspiciousConfigTO.getConfFor()));
        setCboProdType((String) getCbmProdType().getDataForKey(objSuspiciousConfigTO.getProdType()));
//        setCboTransactionType((String) getCbmTransType().getDataForKey(CommonUtil.convertObjToStr(objSuspiciousConfigTO.getTransType())));
        
//        if (getCbmProdId()== null || getCbmProdId().equals("")) {
            setCbmProdId(objSuspiciousConfigTO.getProdType());
            setCboProdId((String) getCbmProdId().getDataForKey(objSuspiciousConfigTO.getProdId()));
//        }
        setCboPeriod(calPeriodOB(CommonUtil.convertObjToInt(objSuspiciousConfigTO.getPeriod())));
        setTxtPeriod(String.valueOf(periodTxtValue));
        
        setTxtAccNo(CommonUtil.convertObjToStr(objSuspiciousConfigTO.getAcctNo()));
        setTxtCustNo(CommonUtil.convertObjToStr(objSuspiciousConfigTO.getCustNo()));
        setTxtCountExceeds(CommonUtil.convertObjToStr(objSuspiciousConfigTO.getCountExceeds()));
        setTxtWorthExceeds(CommonUtil.convertObjToStr(objSuspiciousConfigTO.getWorthExceeds()));
        
	if(CommonUtil.convertObjToStr(objSuspiciousConfigTO.getCrCash()).equalsIgnoreCase(YES)){
            setChkCreditCash(true);
        }else{
            setChkCreditCash(false);
        }
        
        if(CommonUtil.convertObjToStr(objSuspiciousConfigTO.getCrTrans()).equalsIgnoreCase(YES)){
            setChkCreditTransfer(true);
        }else{
            setChkCreditTransfer(false);
        }
        
        if(CommonUtil.convertObjToStr(objSuspiciousConfigTO.getCrClr()).equalsIgnoreCase(YES)){
            setChkCreditClearing(true);
        }else{
            setChkCreditClearing(false);
        }
        
        if(CommonUtil.convertObjToStr(objSuspiciousConfigTO.getDrCash()).equalsIgnoreCase(YES)){
            setChkDebitCash(true);
        }else{
            setChkDebitCash(false);
        }
        
        if(CommonUtil.convertObjToStr(objSuspiciousConfigTO.getDrClr()).equalsIgnoreCase(YES)){
            setChkDebitClearing(true);
        }else{
            setChkDebitClearing(false);
        }
        
        if(CommonUtil.convertObjToStr(objSuspiciousConfigTO.getDrTrans()).equalsIgnoreCase(YES)){
            setChkDebitTransfer(true);
        }else{
            setChkDebitTransfer(false);
        }
        
//        ttNotifyObservers();
    }
    public String calPeriodOB(int value) {
        String periodValue;
        if ((value >= 365) && (value%365 == 0)) {
            periodValue = (String) getCbmPeriod().getDataForKey(CommonUtil.convertObjToStr("YEARS"));
            value = value/365;
        } else if ((value >= 30) && (value % 30 == 0)) {
            periodValue=(String) getCbmPeriod().getDataForKey(CommonUtil.convertObjToStr("MONTHS"));
            value = value/30;
        } else if ((value >= 1) && (value % 1 == 0)) {
            periodValue=(String) getCbmPeriod().getDataForKey(CommonUtil.convertObjToStr("DAYS"));
            value = value;
        } else {
            periodValue="";
            value = 0;
        }
        periodTxtValue = value;
        return periodValue;
    }
    /**
     * reset the form
     */
    public void resetForm() {
        resetFields();
        serialNo = 1;
        SL_NO = 1;
        count = 1;
        period = 0;
        periodTxtValue = 0;
        rowData = null;
        suspiciousConfigTO = null;
        deletedsuspiciousConfigTO = null;
        totalsuspiciousConfigTO = null;
        tblSuspiciousConfig.setDataArrayList(null,tableTitle);
        ttNotifyObservers();
    }
    /**
     * To set the data in OtherBankTO TO
     */
    public SuspiciousConfigTO setSuspiciousConfigTO() {
        log.info("In setSuspiciousConfigTO...");
        
        final SuspiciousConfigTO objSuspiciousConfigTO = new SuspiciousConfigTO();
        try{
            objSuspiciousConfigTO.setConfKey(CommonUtil.convertObjToStr(getConfigKey()));
            objSuspiciousConfigTO.setSlNo(CommonUtil.convertObjToStr(getSL_NO()));
            objSuspiciousConfigTO.setStatus(CommonUtil.convertObjToStr(getConfigurationStatus()));
//            objSuspiciousConfigTO.setStatusBy(CommonUtil.convertObjToStr(TrueTransactMain.USER_ID));
            objSuspiciousConfigTO.setConfFor(CommonUtil.convertObjToStr((String)cbmConfigFor.getKeyForSelected()));
            calculatePeriod(txtPeriod,CommonUtil.convertObjToStr((String)cbmPeriod.getKeyForSelected()));
            objSuspiciousConfigTO.setPeriod(CommonUtil.convertObjToDouble(String.valueOf(period)));
            
            if (cbmProdId!= null)
                objSuspiciousConfigTO.setProdId(CommonUtil.convertObjToStr((String)cbmProdId.getKeyForSelected()));
            
            objSuspiciousConfigTO.setProdType(CommonUtil.convertObjToStr((String)cbmProdType.getKeyForSelected()));
            
//            objSuspiciousConfigTO.setTransType(CommonUtil.convertObjToStr((String)cbmTransType.getKeyForSelected()));
            
            objSuspiciousConfigTO.setCountExceeds(CommonUtil.convertObjToDouble(getTxtCountExceeds()));
            objSuspiciousConfigTO.setWorthExceeds(CommonUtil.convertObjToDouble(getTxtWorthExceeds()));
            
            objSuspiciousConfigTO.setAcctNo(CommonUtil.convertObjToStr(getTxtAccNo()));
            objSuspiciousConfigTO.setCustNo(CommonUtil.convertObjToStr(getTxtCustNo()));
//            objSuspiciousConfigTO.setStatusDt(DateUtil.getDateMMDDYYYY(getStatusDt()));
            Date dtDate = DateUtil.getDateMMDDYYYY(getStatusDt());
            if(dtDate != null){
            Date Dt = ClientUtil.getCurrentDate();
            Dt.setDate(dtDate.getDate());
            Dt.setMonth(dtDate.getMonth());
            Dt.setYear(dtDate.getYear());
            objSuspiciousConfigTO.setStatusDt(Dt);
            }else{
                objSuspiciousConfigTO.setStatusDt(DateUtil.getDateMMDDYYYY(getStatusDt()));
            }
            if(getChkCreditCash()){
                objSuspiciousConfigTO.setCrCash(YES);
            }else{
                objSuspiciousConfigTO.setCrCash(NO);
            }
            
            if(getChkCreditTransfer()){
                objSuspiciousConfigTO.setCrTrans(YES);
            }else{
                objSuspiciousConfigTO.setCrTrans(NO);
            }
            
            if(getChkCreditClearing()){
                objSuspiciousConfigTO.setCrClr(YES);
            }else{
                objSuspiciousConfigTO.setCrClr(NO);
            }
            
            if(getChkDebitClearing()){
                objSuspiciousConfigTO.setDrClr(YES);
            }else{
                objSuspiciousConfigTO.setDrClr(NO);
            }
            
            if(getChkDebitTransfer()){
                objSuspiciousConfigTO.setDrTrans(YES);
            }else{
                objSuspiciousConfigTO.setDrTrans(NO);
            }
            
            if(getChkDebitCash()){
                objSuspiciousConfigTO.setDrCash(YES);
            }else{
                objSuspiciousConfigTO.setDrCash(NO);
            }
            
            
        }catch(Exception e){
            log.info("Error in setOtherBankBranchTO()");
            parseException.logException(e,true);
        }
        return objSuspiciousConfigTO;
    }
    /**
     * return value for cboPeriod
     */
    
    
    public void resetFields() {
//        setConfigKey("");
        setSL_NO("");
        setConfigurationStatus("");
        setStatusBy("");
        setStatusDt("");
        setCboConfigurationFor("");
        resetAccountLevel();
        resetCustLevel();
        resetTrans();
    }
    
    public void resetAccountLevel() {
        setCboProdType("");
        setCboProdId("");
        setTxtAccNo("");
    }
    public void resetCustLevel() {
        setTxtCustNo("");
    }
    public void resetTrans() {
        setTxtCountExceeds("");
        setTxtWorthExceeds("");
        setCboPeriod("");
        setTxtPeriod("");
        
        setChkCreditCash(false);
        setChkCreditTransfer(false);
        setChkCreditClearing(false);
        setChkDebitClearing(false);
        setChkDebitTransfer(false);
        setChkDebitCash(false);
        
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
                    throw new TTException(resourceBundle.getString("TOCommandError"));
                }
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        //        ttNotifyObservers();
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
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        log.info("In doActionPerform...");
        final SuspiciousConfigTO objSuspiciousConfigTO = setSuspiciousConfigTO();
        
        if (totalsuspiciousConfigTO == null)
            totalsuspiciousConfigTO = new LinkedHashMap();
        if(getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            if (deletedsuspiciousConfigTO != null) {
                totalsuspiciousConfigTO.put(TO_DELETED_AT_UPDATE_MODE, deletedsuspiciousConfigTO);
            }
        }
        
        if (getCommand() == CommonConstants.TOSTATUS_UPDATE) {
        SuspiciousConfigTO tempSuspiciousConfigTO;
        tempSuspiciousConfigTO = (SuspiciousConfigTO) suspiciousConfigTO.get("1");
        if (tempSuspiciousConfigTO.getProdType()==null || tempSuspiciousConfigTO.getProdType()=="")  
            tempSuspiciousConfigTO.setProdType(objSuspiciousConfigTO.getProdType());
        suspiciousConfigTO = new LinkedHashMap();
        suspiciousConfigTO.put("1",tempSuspiciousConfigTO);   
        }
        totalsuspiciousConfigTO.put(TO_NOT_DELETED_AT_UPDATE_MODE, suspiciousConfigTO);
        
        final HashMap data = new HashMap();
        data.put("SuspiciousConfigTO",totalsuspiciousConfigTO);
        totalsuspiciousConfigTO = null;
        data.put("MODE",getCommand());// To Maintain the Status CREATED, MODIFIED, DELETED
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        HashMap proxyResultMap = proxy.execute(data,operationMap);
        setResult(actionType);
        actionType = ClientConstants.ACTIONTYPE_CANCEL;
        resetForm();
    }
    /**
     * Action Performed when Delete is pressed in SuspiciousConfig
     */
    public void deleteSuspiciousConfig(int index) {
        log.info("deleteOtherBankBranch Invoked...");
        try {
            if (suspiciousConfigTO != null) {
                SuspiciousConfigTO objSuspiciousConfigTO = (SuspiciousConfigTO) suspiciousConfigTO.remove(String.valueOf(index+1));
                if( ( objSuspiciousConfigTO.getStatus().length()>0 ) && ( objSuspiciousConfigTO.getStatus() != null ) && !(objSuspiciousConfigTO.getStatus().equals(""))) {
                    if (deletedsuspiciousConfigTO == null)
                        deletedsuspiciousConfigTO = new LinkedHashMap();
                    deletedsuspiciousConfigTO.put(String.valueOf(count++), objSuspiciousConfigTO);
                } else if (suspiciousConfigTO != null) {
                    for(int i = index+1,j=suspiciousConfigTO.size();i<=j;i++) {
                        suspiciousConfigTO.put(String.valueOf(i),(SuspiciousConfigTO)suspiciousConfigTO.remove(String.valueOf((i+1))));
                    }
                    
                }
                objSuspiciousConfigTO = null;
                serialNo--;
                // Reset table data
                rowData.remove(index);
                 /* Orders the serial no in the arraylist (tableData) after the removal
               of selected Row in the table */
                for(int i=0,j = rowData.size();i<j;i++){
                    ( (ArrayList) rowData.get(i)).set(0,String.valueOf(i+1));
                }
                tblSuspiciousConfig.setDataArrayList(rowData,tableTitle);
                
            }
        } catch (Exception  e){
            parseException.logException(e,true);
        }
    }
    /**
     * Set Column values in the SuspiciousConfig details table
     */
    private ArrayList setColumnValues(int rowClicked,SuspiciousConfigTO objSuspiciousConfigTO) {
        ArrayList row = new ArrayList();
        row.add(String.valueOf(rowClicked));
        row.add(CommonUtil.convertObjToStr(objSuspiciousConfigTO.getConfFor()));
        row.add(CommonUtil.convertObjToDouble(objSuspiciousConfigTO.getCountExceeds()));
        row.add(CommonUtil.convertObjToDouble(objSuspiciousConfigTO.getWorthExceeds()));
//        row.add((String) getCbmTransType().getDataForKey(CommonUtil.convertObjToStr(objSuspiciousConfigTO.getTransType())));
        return row;
    }
    
    
    /**
     * Action to be performed when Save Button in SuspiciousConfig Screen is pressed
     */
    public void saveSuspiciousConfig(boolean tableClick,int rowClicked) {
        log.info("saveOtherBankBranch Invoked...");
        try {
            SuspiciousConfigTO objSuspiciousConfigTO = setSuspiciousConfigTO();
            if (suspiciousConfigTO == null)
                suspiciousConfigTO = new LinkedHashMap();
            if (rowData == null)
                rowData =  new ArrayList();
            if (tableClick) {
                rowData.set(rowClicked, setColumnValues(rowClicked+1, objSuspiciousConfigTO));
                suspiciousConfigTO.put(String.valueOf(rowClicked+1), objSuspiciousConfigTO);
                setSuspiciousConfigOB(objSuspiciousConfigTO);
            } else {
                rowData.add(setColumnValues(serialNo, objSuspiciousConfigTO));
                objSuspiciousConfigTO.setSlNo(String.valueOf(SL_NO++));
                suspiciousConfigTO.put(String.valueOf(serialNo),objSuspiciousConfigTO);
                serialNo++;
            }
            objSuspiciousConfigTO = null;
            tblSuspiciousConfig.setDataArrayList(rowData,tableTitle);
            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    //Do display the Data from the Database, in UI
    public void populateData(HashMap whereMap) {        
        log.info("In populateData...");
        final HashMap mapData;
        try {
            mapData = proxy.executeQuery(whereMap, operationMap);
            log.info("proxy.executeQuery is working fine");
            populateOB(mapData);
            SL_NO = executeQueryForSL_NO(whereMap)+1;            
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
        List list = (List) mapData.get("SuspiciousConfigTO");
        placeValuesInSuspiciousConfigTable(list);        
//        ttNotifyObservers();
    }
    private int executeQueryForSL_NO(HashMap map) {
        String count = "0";        
        try {
            if(map != null){
                HashMap where = new HashMap();
                where.put("CONF_KEY", (String)map.get("CONF_KEY"));
                List totalRecords = (List) ClientUtil.executeQuery("NoOfRecForParticularConfig", where);
                where = null;
                if(totalRecords.size()>0 && totalRecords != null){
                    count = CommonUtil.convertObjToStr(((LinkedHashMap)totalRecords.get(0)).get("COUNT"));
                } else {
                    count = "0";
                }
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return Integer.parseInt(count);
        
    }
    /**
     * To Populate SuspiciousConfig Details
     */
    private void placeValuesInSuspiciousConfigTable(List list) throws Exception {
        log.info("In placeValuesInSuspiciousConfigTable...");
        if (list != null) {
            if (suspiciousConfigTO == null)
                suspiciousConfigTO = new LinkedHashMap();
            if (rowData == null)
                rowData = new ArrayList();
            for (int i=0,j=list.size();i<j;i++) {
                SuspiciousConfigTO objSuspiciousConfigTO = (SuspiciousConfigTO) list.get(i);
                rowData.add(setColumnValues(serialNo, objSuspiciousConfigTO));
                suspiciousConfigTO.put(String.valueOf(serialNo),objSuspiciousConfigTO);
                serialNo++;
                objSuspiciousConfigTO = null;
            }
            SL_NO = serialNo;
            tblSuspiciousConfig.setDataArrayList(rowData,tableTitle);
        }        
        
    }
    /**
     * populate Other Bank Branch Details when table is pressed
     */
    public String populateSuspiciousConfig(int row) {
        String configFor = null;
        log.info("populateSuspiciousConfig Invoked...");
        try {
            SuspiciousConfigTO objSuspiciousConfigTO = (SuspiciousConfigTO) suspiciousConfigTO.get(String.valueOf(row+1));
            setSuspiciousConfigOB(objSuspiciousConfigTO);
            configFor = (String)objSuspiciousConfigTO.getConfFor();
            objSuspiciousConfigTO = null;
        } catch(Exception e) {
            parseException.logException(e,true);
        }
        return configFor;
    }
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }
    public int getLevel(String configFor) {
        int GENERAL=0,ACCOUNT=1,CUSTOMER=2,LEVEL =-1;
        log.info("getLevel Invoked...");
        try {
            if (configFor != null && configFor.length()>0 && !configFor.equals("")) {
                if (configFor.equals("GENERAL")) {
                    LEVEL = GENERAL;
                } else if (configFor.equals("ACCOUNT")) {
                    LEVEL = ACCOUNT;
                } else if (configFor.equals("CUSTOMER")) {
                    LEVEL = CUSTOMER;
                } else {
                    LEVEL = LEVEL;
                }
            } else {
                LEVEL = LEVEL;
            }
        } catch(Exception e) {
            parseException.logException(e,true);
        }
        return LEVEL;
    }
    
    public void calculatePeriod(String periodOB, String comboPeriod) {
        try {
            if (periodOB != null && periodOB.length()>0 && !periodOB.equals("") &&
            comboPeriod != null && comboPeriod.length()>0 && !comboPeriod.equals("") ) {
                period = Double.parseDouble(periodOB);
                if (comboPeriod.equals("DAYS")) {
                    period = period;
                } else if (comboPeriod.equals("MONTHS")) {
                    period = period * 30;
                } else if (comboPeriod.equals("YEARS")) {
                    period = period * 365;
                }
            }
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    
    public boolean getChkCreditCash() {
        return chkCreditCash;
    }
    public void setChkCreditCash(boolean chkCreditCash) {
        this.chkCreditCash = chkCreditCash;
    }
    
    public boolean getChkCreditTransfer() {
        return chkCreditTransfer;
    }
    public void setChkCreditTransfer(boolean chkCreditTransfer) {
        this.chkCreditTransfer = chkCreditTransfer;
    }
    
    public boolean getChkDebitClearing() {
        return chkDebitClearing;
    }
    public void setChkDebitClearing(boolean chkDebitClearing) {
        this.chkDebitClearing = chkDebitClearing;
    }
    
    public boolean getChkDebitTransfer() {
        return chkDebitTransfer;
    }
    public void setChkDebitTransfer(boolean chkDebitTransfer) {
        this.chkDebitTransfer = chkDebitTransfer;
    }
    
    public boolean getChkDebitCash() {
        return chkDebitCash;
    }
    public void setChkDebitCash(boolean chkDebitCash) {
        this.chkDebitCash = chkDebitCash;
    }
    
    public boolean getChkCreditClearing() {
        return chkCreditClearing;
    }
    public void setChkCreditClearing(boolean chkCreditClearing) {
        this.chkCreditClearing = chkCreditClearing;
    }
    
}