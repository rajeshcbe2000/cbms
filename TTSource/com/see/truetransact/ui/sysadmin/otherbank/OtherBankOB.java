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

package com.see.truetransact.ui.sysadmin.otherbank;

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
import com.see.truetransact.transferobject.sysadmin.otherbank.OtherBankBranchTO;
import com.see.truetransact.transferobject.sysadmin.otherbank.OtherBankTO;
import com.see.truetransact.uicomponent.CObservable;

/**
 *
 * @author  152715
 */

public class OtherBankOB extends CObservable{
    
    private String txtOtherBranchCode = "";
    private String txtOtherBranchShortName = "";
    private String txtBranchName = "";
    private String txtAddress = "";
    private String txtMICR = "";
    private String txtAccountHeadValue = "";
    private String txtPincode = "";
    private String cboCountry = "";
    private String cboState = "";
    private String cboCity = "";
    private String txtBankName = "";
    private String txtBankCode = "";
    private String txtBankShortName = "";
    private String authorizeStatus1 = "";
    private String authorizeBy = "";
    private String authorizeDt = "";
    private String otherBankBranchStatus = "";
    private ComboBoxModel cbmProdType;
    private ComboBoxModel cbmProdID;
    private String cboProdID = "";
    private String cboProductType = "";
    private String PhoneNo="";
    private boolean cRadio_HVC_Yes=false;
    private boolean cRadio_HVC_No=false;
    private boolean cRadio_DB_Yes=false;
    private boolean cRadio_DB_No=false;
    
    private ComboBoxModel cbmCountry;
    private ComboBoxModel cbmCity;
    private ComboBoxModel cbmState;
    
    private HashMap lookUpHash;
    private HashMap keyValue;
    
    private ArrayList key;
    private ArrayList value;
    private ArrayList rowData;
    private static int serialNo = 1;// To maintain serial No in Other Bank Branch Details Table
    private static int count = 1;// To maintain No of Other Bank Branch Details deleted
    
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int actionType;
    private int result;
    HashMap operationMap;
    final ArrayList tableTitle = new ArrayList();
    ProxyFactory proxy;
    private EnhancedTableModel tblOtherBankBranch;
    private LinkedHashMap otherBankBranchTO = null;// Contains Other Bank Branch Details which the Status is not DELETED
    private LinkedHashMap deletedOtherBankBranchTO = null;// Contains Other Bank Branch Details which the Status is DELETED
    private LinkedHashMap totalOtherBankBranchTO = null;// Contains Both Other Bank Details and Other Bank Branch Details
    private final String TO_DELETED_AT_UPDATE_MODE = "TO_DELETED_AT_UPDATE_MODE";
    private final String TO_NOT_DELETED_AT_UPDATE_MODE = "TO_NOT_DELETED_AT_UPDATE_MODE";
    
    
    
    final OtherBankRB objOtherBankRB = new OtherBankRB();
    
    
    private static OtherBankOB objOtherBankOB; // singleton object
    
    private final static Logger log = Logger.getLogger(OtherBankOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    static {
        try {
            log.info("Creating OtherBankOB...");
            objOtherBankOB = new OtherBankOB();
        } catch(Exception e) {
            //_log.error(e);
            parseException.logException(e,true);
        }
    }
    
    /**
     * Returns an instance of OtherBankOB.
     * @return  OtherBankOB
     */
    public static OtherBankOB getInstance() {
        return objOtherBankOB;
    }
    /** Creates a new instance of CashManagementOB */
    private OtherBankOB() throws Exception{
        proxy = ProxyFactory.createProxy();
        setOperationMap();
        fillDropdown();
        setTableTile();
        tblOtherBankBranch = new EnhancedTableModel(null, tableTitle);
        serialNo = 1;
        count =1;
    }
    
    /* Sets _subHeadTitle with table column headers */
    private void setTableTile() throws Exception{
        tableTitle.add(objOtherBankRB.getString("tblColumn1"));
        tableTitle.add(objOtherBankRB.getString("tblColumn2"));
        tableTitle.add(objOtherBankRB.getString("tblColumn3"));
        tableTitle.add(objOtherBankRB.getString("tblColumn4"));
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
    
    void setTblOtherBankBranch(EnhancedTableModel tblOtherBankBranch){
        this.tblOtherBankBranch = tblOtherBankBranch;
        setChanged();
    }
    EnhancedTableModel getTblOtherBankBranch(){
        return this.tblOtherBankBranch;
    }
    
    /* Sets the HashMap required to set JNDI,Home and Remote*/
    private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "OtherBankJNDI");
        operationMap.put(CommonConstants.HOME, "com.see.truetransact.serverside.sysadmin.otherbank.OtherBankHome");
        operationMap.put(CommonConstants.REMOTE, "com.see.truetransact.serverside.sysadmin.otherbank.OtherBank");
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    public void fillDropdown() throws Exception{
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
        lookup_keys.add("CUSTOMER.CITY");
        lookup_keys.add("CUSTOMER.STATE");
        lookup_keys.add("CUSTOMER.COUNTRY");
        
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("CUSTOMER.CITY"));
        cbmCity = new ComboBoxModel(key,value);
        
//        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("CUSTOMER.STATE"));
        cbmState = new ComboBoxModel(key,value);
        
//        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("CUSTOMER.COUNTRY"));
        cbmCountry = new ComboBoxModel(key,value);
        
        //Added By Suresh
        key = new ArrayList();
        value = new ArrayList();
        key.add("");
        value.add("");
        key.add("GL");
        value.add("General Ledger");
        key.add("INV");
        value.add("Investment");
        key.add("AB");
        value.add("Other Bank");
        cbmProdType = new ComboBoxModel(key,value);
        
        cbmProdID = new ComboBoxModel();
        makeComboBoxKeyValuesNull();
    }
    
    private void makeComboBoxKeyValuesNull(){
        key = null;
        value = null;
        lookUpHash = null;
        keyValue = null;
    }
    
    //Added By Suresh
    public void setCbmProdType(String prodType) {
        try {
            HashMap lookUpHash = new HashMap();
            if(prodType.equals("INV")){
                lookUpHash.put(CommonConstants.MAP_NAME,"Cash.getAccProduct" + prodType);
                lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
            }
            if(prodType.equals("AB")){
                lookUpHash.put(CommonConstants.MAP_NAME,"Cash.getAccProduct" + prodType);
                lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
            }else{
                key = new ArrayList();
                value = new ArrayList();
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        cbmProdID= new ComboBoxModel(key,value);
        this.cbmProdID = cbmProdID;
        setChanged();
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
    
    // Setter method for txtBankName
    void setTxtBankName(String txtBankName){
        this.txtBankName = txtBankName;
        setChanged();
    }
    // Getter method for txtBankName
    String getTxtBankName(){
        return this.txtBankName;
    }
    
    // Setter method for txtBankCode
    void setTxtBankCode(String txtBankCode){
        this.txtBankCode = txtBankCode;
        setChanged();
    }
    // Getter method for txtBankCode
    String getTxtBankCode(){
        return this.txtBankCode;
    }
    
    // Setter method for txtBankShortName
    void setTxtBankShortName(String txtBankShortName){
        this.txtBankShortName = txtBankShortName;
        setChanged();
    }
    // Getter method for txtBankShortName
    String getTxtBankShortName(){
        return this.txtBankShortName;
    }
    
    // Other Bank Branch
    
    void setCbmCity(ComboBoxModel cbmCity){
        this.cbmCity = cbmCity;
        setChanged();
    }
    
    ComboBoxModel getCbmCity(){
        return this.cbmCity;
    }
    
    void setCbmState(ComboBoxModel cbmState){
        this.cbmState = cbmState;
        setChanged();
    }
    
    ComboBoxModel getCbmState(){
        return this.cbmState;
    }
    
    void setCbmCountry(ComboBoxModel cbmCountry){
        this.cbmCountry = cbmCountry;
        setChanged();
    }
    
    ComboBoxModel getCbmCountry(){
        return this.cbmCountry;
    }
    
    
    // Setter method for txtOtherBranchCode
    void setTxtOtherBranchCode(String txtOtherBranchCode){
        this.txtOtherBranchCode = txtOtherBranchCode;
        setChanged();
    }
    // Getter method for txtOtherBranchCode
    String getTxtOtherBranchCode(){
        return this.txtOtherBranchCode;
    }
    
    // Setter method for txtOtherBranchShortName
    void setTxtOtherBranchShortName(String txtOtherBranchShortName){
        this.txtOtherBranchShortName = txtOtherBranchShortName;
        setChanged();
    }
    // Getter method for txtOtherBranchShortName
    String getTxtOtherBranchShortName(){
        return this.txtOtherBranchShortName;
    }
    
    // Setter method for txtBranchName
    void setTxtBranchName(String txtBranchName){
        this.txtBranchName = txtBranchName;
        setChanged();
    }
    // Getter method for txtBranchName
    String getTxtBranchName(){
        return this.txtBranchName;
    }
    
    // Setter method for txtAddress
    void setTxtAddress(String txtAddress){
        this.txtAddress = txtAddress;
        setChanged();
    }
    // Getter method for txtAddress
    String getTxtAddress(){
        return this.txtAddress;
    }
    
    // Setter method for otherBankBranchStatus
    void setOtherBankBranchStatus(String otherBankBranchStatus){
        this.otherBankBranchStatus = otherBankBranchStatus;
        setChanged();
    }
    // Getter method for otherBankBranchStatus
    String getOtherBankBranchStatus(){
        return this.otherBankBranchStatus;
    }
    
    // Setter method for txtPincode
    void setTxtPincode(String txtPincode){
        this.txtPincode = txtPincode;
        setChanged();
    }
    // Getter method for txtPincode
    String getTxtPincode(){
        return this.txtPincode;
    }
    
    // Setter method for cboCountry
    void setCboCountry(String cboCountry){
        this.cboCountry = cboCountry;
        setChanged();
    }
    // Getter method for cboCountry
    String getCboCountry(){
        return this.cboCountry;
    }
    
    // Setter method for cboState
    void setCboState(String cboState){
        this.cboState = cboState;
        setChanged();
    }
    // Getter method for cboState
    String getCboState(){
        return this.cboState;
    }
    
    // Setter method for cboCity
    void setCboCity(String cboCity){
        this.cboCity = cboCity;
        setChanged();
    }
    // Getter method for cboCity
    String getCboCity(){
        return this.cboCity;
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
    private void setOtherBankOB(OtherBankTO objOtherBankTO) throws Exception {
        log.info("Inside setOtherBankOB()");
        setTxtBankCode(CommonUtil.convertObjToStr(objOtherBankTO.getBankCode()));
        setTxtBankName(CommonUtil.convertObjToStr(objOtherBankTO.getBankName()));
        setTxtBankShortName(CommonUtil.convertObjToStr(objOtherBankTO.getBankShortName()));
        setAuthorizeDt(DateUtil.getStringDate(objOtherBankTO.getAuthorizeDt()));
        setAuthorizeBy(CommonUtil.convertObjToStr(objOtherBankTO.getAuthorizeBy()));
        setAuthorizeStatus1(CommonUtil.convertObjToStr(objOtherBankTO.getAuthorizeStatus()));
        //        ttNotifyObservers();
    }
    /**
     * To set the data in OtherBankTO TO
     */
    public OtherBankTO setOtherBankTO() {
        log.info("In setOtherBankTO...");
        
        final OtherBankTO objOtherBankTO = new OtherBankTO();
        try{
            objOtherBankTO.setBankCode(CommonUtil.convertObjToStr(getTxtBankCode()));
            objOtherBankTO.setBankName(CommonUtil.convertObjToStr(getTxtBankName()));
            objOtherBankTO.setBankShortName(CommonUtil.convertObjToStr(getTxtBankShortName()));
            objOtherBankTO.setStatus(CommonUtil.convertObjToStr(getCommand()));
//            objOtherBankTO.setAuthorizeDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getAuthorizeDt())));
            Date dtDate = (Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getAuthorizeDt()));
            if(dtDate != null){
            Date Dt = ClientUtil.getCurrentDate();
            Dt.setDate(dtDate.getDate());
            Dt.setMonth(dtDate.getMonth());
            Dt.setYear(dtDate.getYear());
            objOtherBankTO.setAuthorizeDt(Dt);
            }else{
               objOtherBankTO.setAuthorizeDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getAuthorizeDt())));
            }
            objOtherBankTO.setAuthorizeStatus(CommonUtil.convertObjToStr(getAuthorizeStatus1()));
            objOtherBankTO.setAuthorizeBy(CommonUtil.convertObjToStr(getAuthorizeBy()));
            
        }catch(Exception e){
            log.info("Error in setOtherBankTO()");
            parseException.logException(e,true);
        }
        return objOtherBankTO;
    }
    /**
     * Set Other Bank Branch fields in the OB
     */
    private void setOtherBankBranchOB(OtherBankBranchTO objOtherBankBranchTO) throws Exception {
        log.info("Inside setOtherBankOB()");
        setTxtBankCode(CommonUtil.convertObjToStr(objOtherBankBranchTO.getBankCode()));
        setTxtOtherBranchCode(CommonUtil.convertObjToStr(objOtherBankBranchTO.getBranchCode()));
        setTxtBranchName(CommonUtil.convertObjToStr(objOtherBankBranchTO.getBranchName()));
        setTxtOtherBranchShortName(CommonUtil.convertObjToStr(objOtherBankBranchTO.getBranchShortName()));
        setTxtAddress(CommonUtil.convertObjToStr(objOtherBankBranchTO.getAddress()));
        setTxtMICR(CommonUtil.convertObjToStr(objOtherBankBranchTO.getMicr()));
        setTxtAccountHeadValue(CommonUtil.convertObjToStr(objOtherBankBranchTO.getAccountHead()));
        setTxtPincode(CommonUtil.convertObjToStr(objOtherBankBranchTO.getPincode()));
        setCboCity((String) getCbmCity().getDataForKey(CommonUtil.convertObjToStr(objOtherBankBranchTO.getCity())));
        setCboState((String) getCbmState().getDataForKey(CommonUtil.convertObjToStr(objOtherBankBranchTO.getState())));
        setCboCountry((String) getCbmCountry().getDataForKey(CommonUtil.convertObjToStr(objOtherBankBranchTO.getCountry())));
        setOtherBankBranchStatus(CommonUtil.convertObjToStr(objOtherBankBranchTO.getStatus()));
        setPhoneNo(CommonUtil.convertObjToStr(objOtherBankBranchTO.getPhoneNo()));
        setCboProductType(CommonUtil.convertObjToStr(getCbmProdType().getDataForKey(CommonUtil.convertObjToStr(objOtherBankBranchTO.getProdType()))));
        setCboProdID(CommonUtil.convertObjToStr(getCbmProdID().getDataForKey(CommonUtil.convertObjToStr(objOtherBankBranchTO.getProdId()))));
        
        if(CommonUtil.convertObjToStr(objOtherBankBranchTO.getCRadio_HVC_Yes()).equals("Y")){
            setCRadio_HVC_Yes(true);
            setCRadio_HVC_No(false);
        }
        else{
            setCRadio_HVC_No(true);
            setCRadio_HVC_Yes(false);
        }
        if(CommonUtil.convertObjToStr(objOtherBankBranchTO.getCRadio_DB_Yes()).equals("Y")){
            setCRadio_DB_Yes(true);
            setCRadio_DB_No(false);
        
        }
        else{
            setCRadio_DB_No(true);
            setCRadio_DB_Yes(false);
        }
        
        //        ttNotifyObservers();
    }
    /**
     * To set the data in OtherBankTO TO
     */
    public OtherBankBranchTO setOtherBankBranchTO() {
        log.info("In setOtherBankBranchTO...");
        
        final OtherBankBranchTO objOtherBankBranchTO = new OtherBankBranchTO();
        try{
            objOtherBankBranchTO.setBankCode(CommonUtil.convertObjToStr(getTxtBankCode()));
            objOtherBankBranchTO.setBranchCode(CommonUtil.convertObjToStr(getTxtOtherBranchCode()));
            objOtherBankBranchTO.setBranchName(CommonUtil.convertObjToStr(getTxtBranchName()));
            objOtherBankBranchTO.setBranchShortName(CommonUtil.convertObjToStr(getTxtOtherBranchShortName()));
            objOtherBankBranchTO.setAddress(CommonUtil.convertObjToStr(getTxtAddress()));
            objOtherBankBranchTO.setMicr(CommonUtil.convertObjToStr(getTxtMICR()));
            objOtherBankBranchTO.setAccountHead(CommonUtil.convertObjToStr(getTxtAccountHeadValue()));
            objOtherBankBranchTO.setPincode(CommonUtil.convertObjToStr(getTxtPincode()));
            objOtherBankBranchTO.setCity(CommonUtil.convertObjToStr((String)cbmCity.getKeyForSelected()));
            objOtherBankBranchTO.setState(CommonUtil.convertObjToStr((String)cbmState.getKeyForSelected()));
            objOtherBankBranchTO.setCountry(CommonUtil.convertObjToStr((String)cbmCountry.getKeyForSelected()));
            objOtherBankBranchTO.setStatus(CommonUtil.convertObjToStr(getOtherBankBranchStatus()));
            objOtherBankBranchTO.setProdType(CommonUtil.convertObjToStr(cbmProdType.getKeyForSelected()));
            objOtherBankBranchTO.setProdId(CommonUtil.convertObjToStr(cbmProdID.getKeyForSelected()));
            objOtherBankBranchTO.setPhoneNo(CommonUtil.convertObjToStr(getPhoneNo()));
            if(isCRadio_HVC_No())
                objOtherBankBranchTO.setCRadio_HVC_Yes(objOtherBankRB.getString("radio_hvc_no"));
            else
                objOtherBankBranchTO.setCRadio_HVC_Yes(objOtherBankRB.getString("radio_hvc_yes"));
            if(isCRadio_DB_No())
                objOtherBankBranchTO.setCRadio_DB_Yes(objOtherBankRB.getString("radio_db_no"));
            else
                objOtherBankBranchTO.setCRadio_DB_Yes(objOtherBankRB.getString("radio_db_yes"));
            //            objOtherBankBranchTO.setCRadio_HVC_Yes(isCRadio_HVC_Yes());
        }catch(Exception e){
            log.info("Error in setOtherBankBranchTO()");
            parseException.logException(e,true);
        }
        return objOtherBankBranchTO;
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
                    throw new TTException(objOtherBankRB.getString("TOCommandError"));
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
        final OtherBankTO objOtherBankTO = setOtherBankTO();
        objOtherBankTO.setCommand(getCommand());
        
        if (totalOtherBankBranchTO == null)
            totalOtherBankBranchTO = new LinkedHashMap();
        if(getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            if (deletedOtherBankBranchTO != null) {
                totalOtherBankBranchTO.put(TO_DELETED_AT_UPDATE_MODE, deletedOtherBankBranchTO);
            }
        }
        totalOtherBankBranchTO.put(TO_NOT_DELETED_AT_UPDATE_MODE, otherBankBranchTO);
        
        final HashMap data = new HashMap();
        data.put("OtherBankTO",objOtherBankTO);
        data.put("OtherBankBranchTO",totalOtherBankBranchTO);
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
        resetOtherBank();
        resetOtherBankBranch();
        serialNo = 1;
        count =1;
        rowData = null;
        otherBankBranchTO = null;
        deletedOtherBankBranchTO = null;
        totalOtherBankBranchTO = null;
        tblOtherBankBranch.setDataArrayList(null,tableTitle);
        ttNotifyObservers();
    }
    /**
     * To Reset Other Bank details
     */
    public void resetOtherBank() {
        setTxtBankCode("");
        setTxtBankName("");
        setTxtBankShortName("");
    }
    public void resetOtherBankBranch() {
        setTxtOtherBranchCode("");
        setTxtBranchName("");
        setTxtOtherBranchShortName("");
        setTxtAddress("");
        setTxtMICR("");
        setTxtAccountHeadValue("");
        setTxtPincode("");
        setCboCity("");
        setCboState("");
        setCboCountry("");
        setCboProductType("");
        setCboProdID("");
        setOtherBankBranchStatus("");
        setPhoneNo("");
        setCRadio_DB_Yes(false);
        setCRadio_DB_No(false);
        setCRadio_HVC_Yes(false);
        setCRadio_HVC_No(false);
//        setTxtBankType("");
    }
    /**
     * Set Column values in the Other Bank Branch details table
     */
    private ArrayList setColumnValues(int rowClicked,OtherBankBranchTO objOtherBankBranchTO) {
        ArrayList row = new ArrayList();
        row.add(String.valueOf(rowClicked));
        row.add(CommonUtil.convertObjToStr(objOtherBankBranchTO.getProdType()));
        row.add(CommonUtil.convertObjToStr(objOtherBankBranchTO.getBranchCode()));
        row.add(CommonUtil.convertObjToStr(objOtherBankBranchTO.getBranchShortName()));
        return row;
        
    }
    
    /**
     * Action Performed when Delete is pressed in OtherBankBranch
     */
    public void deleteOtherBankBranch(int index) {
        log.info("deleteOtherBankBranch Invoked...");
        try {
            if (otherBankBranchTO != null) {
                OtherBankBranchTO objOtherBankBranchTO = (OtherBankBranchTO) otherBankBranchTO.remove(String.valueOf(index+1));
                if( ( objOtherBankBranchTO.getStatus().length()>0 ) && ( objOtherBankBranchTO.getStatus() != null ) && !(objOtherBankBranchTO.getStatus().equals(""))) {
                    if (deletedOtherBankBranchTO == null)
                        deletedOtherBankBranchTO = new LinkedHashMap();
                    deletedOtherBankBranchTO.put(String.valueOf(count++), objOtherBankBranchTO);
                } else if (otherBankBranchTO != null) {
                    for(int i = index+1,j=otherBankBranchTO.size();i<=j;i++) {
                        otherBankBranchTO.put(String.valueOf(i),(OtherBankBranchTO)otherBankBranchTO.remove(String.valueOf((i+1))));
                    }
                    
                }
                objOtherBankBranchTO = null;
                serialNo--;
                // Reset table data
                rowData.remove(index);
                 /* Orders the serial no in the arraylist (tableData) after the removal
               of selected Row in the table */
                for(int i=0,j = rowData.size();i<j;i++){
                    ( (ArrayList) rowData.get(i)).set(0,String.valueOf(i+1));
                }
                tblOtherBankBranch.setDataArrayList(rowData,tableTitle);
                
            }
        } catch (Exception  e){
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
        OtherBankTO objOtherBankTO = null;
        objOtherBankTO = (OtherBankTO) ((List) mapData.get("OtherBankTO")).get(0);
        setOtherBankOB(objOtherBankTO);
        List list = (List) mapData.get("OtherBankBranchTO");
        placeValuesInOtherBankBranchTable(list);
        ttNotifyObservers();
    }
    /**
     * To Populate OtherBankBranch Details
     */
    private void placeValuesInOtherBankBranchTable(List list) throws Exception {
        log.info("In populateOBDocuments...");
        if (list != null) {
            if (otherBankBranchTO == null)
                otherBankBranchTO = new LinkedHashMap();
            if (rowData == null)
                rowData = new ArrayList();
            for (int i=0,j=list.size();i<j;i++) {
                OtherBankBranchTO objOtherBankBranchTO = (OtherBankBranchTO) list.get(i);
                rowData.add(setColumnValues(serialNo, objOtherBankBranchTO));
                otherBankBranchTO.put(String.valueOf(serialNo),objOtherBankBranchTO);
                serialNo++;
                objOtherBankBranchTO = null;
            }
            tblOtherBankBranch.setDataArrayList(rowData,tableTitle);
        }
        
    }
    /**
     * If all the fields contains value then enable New button
     */
    public boolean enableNew(String bankCode, String bankShortName) {
        boolean flag = false;
        try {
            if (bankCode != null && bankCode.length() > 0 && bankShortName != null && bankShortName.length() > 0) {
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
    public boolean populateOtherBankBranch(int row) {
        boolean flag = false;
        log.info("populateOtherBankBranch Invoked...");
        try {
            OtherBankBranchTO objOtherBankBranchTO = (OtherBankBranchTO) otherBankBranchTO.get(String.valueOf(row+1));
            if ((objOtherBankBranchTO.getStatus() != null) && (objOtherBankBranchTO.getStatus().length() > 0) && !(objOtherBankBranchTO.getStatus().equals(""))) {
                flag = false;
            } else {
                flag = true;
            }
            setOtherBankBranchOB(objOtherBankBranchTO);
        } catch(Exception e) {
            parseException.logException(e,true);
        }
        return flag;
    }
    /**
     * To check whether Bank Code is duplicated or not
     */
    public boolean isBankCodeAlreadyExist(String bankCode){
        boolean exist = false;
        try{
            if (bankCode != null && bankCode.length() >0 && !bankCode.equals("")) {
                HashMap where = new HashMap();
                where.put("BANK_CODE", bankCode );
                List bankCodeCount = (List) ClientUtil.executeQuery("countBankCode", where);
                where = null;
                if ( bankCodeCount.size() > 0 && bankCodeCount != null) {
                    String count = CommonUtil.convertObjToStr(((LinkedHashMap) bankCodeCount.get(0)).get("COUNT"));
                    if ( Integer.parseInt(count) > 0 ) {
                        exist = true;
                    } else {
                        exist = false;
                    }
                    count = null;
                }
                bankCodeCount = null;
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
    public void saveOtherBankBranch(boolean tableClick,int rowClicked) {
        log.info("saveOtherBankBranch Invoked...");
        try {
            OtherBankBranchTO objOtherBankBranchTO = setOtherBankBranchTO();
            if (otherBankBranchTO == null)
                otherBankBranchTO = new LinkedHashMap();
            if (rowData == null)
                rowData =  new ArrayList();
            if (tableClick) {
                rowData.set(rowClicked, setColumnValues(rowClicked+1, objOtherBankBranchTO));
                otherBankBranchTO.put(String.valueOf(rowClicked+1), objOtherBankBranchTO);
                setOtherBankBranchOB(objOtherBankBranchTO);
            } else {
                rowData.add(setColumnValues(serialNo, objOtherBankBranchTO));
                otherBankBranchTO.put(String.valueOf(serialNo),objOtherBankBranchTO);
                serialNo++;
            }
            objOtherBankBranchTO = null;
            tblOtherBankBranch.setDataArrayList(rowData,tableTitle);
            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
//    /**
//     * Getter for property txtBankType.
//     * @return Value of property txtBankType.
//     */
//    public java.lang.String getTxtBankType() {
//        return txtBankType;
//    }
//    
//    /**
//     * Setter for property txtBankType.
//     * @param txtBankType New value of property txtBankType.
//     */
//    public void setTxtBankType(java.lang.String txtBankType) {
//        this.txtBankType = txtBankType;
//    }
    
    /**
     * Getter for property PhoneNo.
     * @return Value of property PhoneNo.
     */
    public java.lang.String getPhoneNo() {
        return PhoneNo;
    }
    
    /**
     * Setter for property PhoneNo.
     * @param PhoneNo New value of property PhoneNo.
     */
    public void setPhoneNo(java.lang.String PhoneNo) {
        this.PhoneNo = PhoneNo;
    }
    
    /**
     * Getter for property cRadio_HVC_Yes.
     * @return Value of property cRadio_HVC_Yes.
     */
    public boolean isCRadio_HVC_Yes() {
        return cRadio_HVC_Yes;
    }
    
    /**
     * Setter for property cRadio_HVC_Yes.
     * @param cRadio_HVC_Yes New value of property cRadio_HVC_Yes.
     */
    public void setCRadio_HVC_Yes(boolean cRadio_HVC_Yes) {
        this.cRadio_HVC_Yes = cRadio_HVC_Yes;
    }
    
    /**
     * Getter for property cRadio_HVC_No.
     * @return Value of property cRadio_HVC_No.
     */
    public boolean isCRadio_HVC_No() {
        return cRadio_HVC_No;
    }
    
    /**
     * Setter for property cRadio_HVC_No.
     * @param cRadio_HVC_No New value of property cRadio_HVC_No.
     */
    public void setCRadio_HVC_No(boolean cRadio_HVC_No) {
        this.cRadio_HVC_No = cRadio_HVC_No;
    }
    
    /**
     * Getter for property cRadio_DB_Yes.
     * @return Value of property cRadio_DB_Yes.
     */
    public boolean isCRadio_DB_Yes() {
        return cRadio_DB_Yes;
    }
    
    /**
     * Setter for property cRadio_DB_Yes.
     * @param cRadio_DB_Yes New value of property cRadio_DB_Yes.
     */
    public void setCRadio_DB_Yes(boolean cRadio_DB_Yes) {
        this.cRadio_DB_Yes = cRadio_DB_Yes;
    }
    
    /**
     * Getter for property cRadio_DB_No.
     * @return Value of property cRadio_DB_No.
     */
    public boolean isCRadio_DB_No() {
        return cRadio_DB_No;
    }
    
    /**
     * Setter for property cRadio_DB_No.
     * @param cRadio_DB_No New value of property cRadio_DB_No.
     */
    public void setCRadio_DB_No(boolean cRadio_DB_No) {
        this.cRadio_DB_No = cRadio_DB_No;
    }
    
    /**
     * Getter for property txtMICR.
     * @return Value of property txtMICR.
     */
    public java.lang.String getTxtMICR() {
        return txtMICR;
    }
    
    /**
     * Setter for property txtMICR.
     * @param txtMICR New value of property txtMICR.
     */
    public void setTxtMICR(java.lang.String txtMICR) {
        this.txtMICR = txtMICR;
    }
    
    /**
     * Getter for property txtAccountHeadValue.
     * @return Value of property txtAccountHeadValue.
     */
    public java.lang.String getTxtAccountHeadValue() {
        return txtAccountHeadValue;
    }
    
    /**
     * Setter for property txtAccountHeadValue.
     * @param txtAccountHeadValue New value of property txtAccountHeadValue.
     */
    public void setTxtAccountHeadValue(java.lang.String txtAccountHeadValue) {
        this.txtAccountHeadValue = txtAccountHeadValue;
    }
    
    /**
     * Getter for property cbmProdType.
     * @return Value of property cbmProdType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdType() {
        return cbmProdType;
    }
    
    /**
     * Setter for property cbmProdType.
     * @param cbmProdType New value of property cbmProdType.
     */
    public void setCbmProdType(com.see.truetransact.clientutil.ComboBoxModel cbmProdType) {
        this.cbmProdType = cbmProdType;
    }
    
    /**
     * Getter for property cbmProdID.
     * @return Value of property cbmProdID.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdID() {
        return cbmProdID;
    }
    
    /**
     * Setter for property cbmProdID.
     * @param cbmProdID New value of property cbmProdID.
     */
    public void setCbmProdID(com.see.truetransact.clientutil.ComboBoxModel cbmProdID) {
        this.cbmProdID = cbmProdID;
    }
    
    /**
     * Getter for property cboProdID.
     * @return Value of property cboProdID.
     */
    public java.lang.String getCboProdID() {
        return cboProdID;
    }
    
    /**
     * Setter for property cboProdID.
     * @param cboProdID New value of property cboProdID.
     */
    public void setCboProdID(java.lang.String cboProdID) {
        this.cboProdID = cboProdID;
    }
    
    /**
     * Getter for property cboProductType.
     * @return Value of property cboProductType.
     */
    public java.lang.String getCboProductType() {
        return cboProductType;
    }
    
    /**
     * Setter for property cboProductType.
     * @param cboProductType New value of property cboProductType.
     */
    public void setCboProductType(java.lang.String cboProductType) {
        this.cboProductType = cboProductType;
    }
    
}