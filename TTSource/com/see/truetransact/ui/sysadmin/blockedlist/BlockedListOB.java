/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BlockedListOB.java
 *
 * Created on Wed Feb 09 14:56:35 IST 2005
 */

package com.see.truetransact.ui.sysadmin.blockedlist;

import java.util.Observable;
import java.util.HashMap;
import java.util.Observable;
import org.apache.log4j.Logger;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.transferobject.sysadmin.blockedlist.BlockedListTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientutil.ComboBoxModel;
import java.util.Date;
/**
 *
 * @author Ashok Vijayakumar
 */

public class BlockedListOB extends CObservable{
    
    private String txtBlockedName = "";
    private String txaBusinessAddress = "";
    private String cboCustomerType = "";
    private String cboFraudStatus = "";
    private String txaRemarks = "";
    private String txtFraudClassifcation = "";
    private String cboFraudClassification = "";
    private String txtBlockedListId = "";
    private HashMap map;//HashMap for setting key,values for Jndi,home,remote
    private HashMap lookUpHash;//HashMap used for puting a mapname with a key which is to be used for filling ComboBoxModel
    private static BlockedListOB objBlockedListOB;//Singleton Object Reference
    private final static Logger log = Logger.getLogger(BlockedListOB.class);//Creating Instace of Log
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int _actionType;//This vairable is used to type of action 1-New 2-Edit 3-Delete
    private int _result;
    private ProxyFactory proxy;
    private final String YES = "Y";
    private final String NO = "N";
    private HashMap keyValue;
    private ArrayList key,value;
    private ComboBoxModel cbmCustomerType,cbmFraudStatus,cbmFraudClassification;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private Date currDt = null;
    /** Consturctor Declaration  for  TDSExemptionOB */
    private BlockedListOB() {
        try {
            currDt = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            setOperationMap();
            initComboBoxModel();
            fillDropdown();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    static {
        try {
            log.info("Creating ParameterOB...");
            objBlockedListOB = new BlockedListOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /**
     * Returns an instance of BlockedListOB.
     * @return  BlockedListOB
     */
    
    public static BlockedListOB getInstance()throws Exception{
        return objBlockedListOB;
    }
    
    // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception{
        map = new HashMap();
        map.put(CommonConstants.JNDI, "BlockedListJNDI");
        map.put(CommonConstants.HOME, "sysadmin.blockedlist.BlockedListHome");
        map.put(CommonConstants.REMOTE, "sysadmin.blockedlist.BlockedList");
    }
    
    /** creates Instances of all the ComboBoxModel **/
    private void initComboBoxModel(){
        cbmCustomerType = new ComboBoxModel();
        cbmFraudStatus = new ComboBoxModel();
        cbmFraudClassification = new ComboBoxModel();
    }
    
    private void fillDropdown() throws Exception{
        try{
            //log.info("Inside FillDropDown");
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME,null);
            final ArrayList lookup_keys = new ArrayList();
            
            lookup_keys.add("FRAUDSTATUS");
            lookup_keys.add("FRAUD.CLASSIFICATION");
            lookup_keys.add("CUSTOMER.TYPE");
            
            
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookUpHash);
            
            getKeyValue((HashMap)keyValue.get("FRAUDSTATUS"));
            cbmFraudStatus = new ComboBoxModel(key,value);
            makeNull();
            
            getKeyValue((HashMap)keyValue.get("FRAUD.CLASSIFICATION"));
            cbmFraudClassification = new ComboBoxModel(key,value);
            makeNull();
            
            getKeyValue((HashMap)keyValue.get("CUSTOMER.TYPE"));
            cbmCustomerType = new ComboBoxModel(key,value);
            makeNull();
            
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
    
    private void makeNull(){
        key=null;
        value=null;
    }
    // Setter method for txtBlockedName
    void setTxtBlockedName(String txtBlockedName){
        this.txtBlockedName = txtBlockedName;
        setChanged();
    }
    // Getter method for txtBlockedName
    String getTxtBlockedName(){
        return this.txtBlockedName;
    }
    
    // Setter method for txaBusinessAddress
    void setTxaBusinessAddress(String txaBusinessAddress){
        this.txaBusinessAddress = txaBusinessAddress;
        setChanged();
    }
    // Getter method for txaBusinessAddress
    String getTxaBusinessAddress(){
        return this.txaBusinessAddress;
    }
    
    // Setter method for cboCustomerType
    void setCboCustomerType(String cboCustomerType){
        this.cboCustomerType = cboCustomerType;
        setChanged();
    }
    // Getter method for cboCustomerType
    String getCboCustomerType(){
        return this.cboCustomerType;
    }
    
    /**
     * Getter for property cbmCustomerType.
     * @return Value of property cbmCustomerType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCustomerType() {
        return cbmCustomerType;
    }
    
    /**
     * Setter for property cbmCustomerType.
     * @param cbmCustomerType New value of property cbmCustomerType.
     */
    public void setCbmCustomerType(com.see.truetransact.clientutil.ComboBoxModel cbmCustomerType) {
        this.cbmCustomerType = cbmCustomerType;
        setChanged();
    }
    
    // Setter method for cboFraudStatus
    void setCboFraudStatus(String cboFraudStatus){
        this.cboFraudStatus = cboFraudStatus;
        setChanged();
    }
    // Getter method for cboFraudStatus
    String getCboFraudStatus(){
        return this.cboFraudStatus;
    }
    
    /**
     * Getter for property cbmFraudStatus.
     * @return Value of property cbmFraudStatus.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmFraudStatus() {
        return cbmFraudStatus;
    }
    
    /**
     * Setter for property cbmFraudStatus.
     * @param cbmFraudStatus New value of property cbmFraudStatus.
     */
    public void setCbmFraudStatus(com.see.truetransact.clientutil.ComboBoxModel cbmFraudStatus) {
        this.cbmFraudStatus = cbmFraudStatus;
        setChanged();
    }
    
    // Setter method for txaRemarks
    void setTxaRemarks(String txaRemarks){
        this.txaRemarks = txaRemarks;
        setChanged();
    }
    // Getter method for txaRemarks
    String getTxaRemarks(){
        return this.txaRemarks;
    }
    
    // Setter method for txtFraudClassifcation
    void setTxtFraudClassifcation(String txtFraudClassifcation){
        this.txtFraudClassifcation = txtFraudClassifcation;
        setChanged();
    }
    // Getter method for txtFraudClassifcation
    String getTxtFraudClassifcation(){
        return this.txtFraudClassifcation;
    }
    
    // Setter method for cboFraudClassification
    void setCboFraudClassification(String cboFraudClassification){
        this.cboFraudClassification = cboFraudClassification;
        setChanged();
    }
    // Getter method for cboFraudClassification
    String getCboFraudClassification(){
        return this.cboFraudClassification;
    }
    
    /**
     * Getter for property cbmFraudClassification.
     * @return Value of property cbmFraudClassification.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmFraudClassification() {
        return cbmFraudClassification;
    }
    
    /**
     * Setter for property cbmFraudClassification.
     * @param cbmFraudClassification New value of property cbmFraudClassification.
     */
    public void setCbmFraudClassification(com.see.truetransact.clientutil.ComboBoxModel cbmFraudClassification) {
        this.cbmFraudClassification = cbmFraudClassification;
        setChanged();
    }
    // Setter method for txtBlockedListId
    void setTxtBlockedListId(String txtBlockedListId){
        this.txtBlockedListId = txtBlockedListId;
        setChanged();
    }
    // Getter method for txtBlockedListId
    String getTxtBlockedListId(){
        return this.txtBlockedListId;
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
    }
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    public int getResult(){
        return _result;
    }
    
    /** Returns an Instance of BlockedListTO **/
    private BlockedListTO getBlockedListTO(String command){
        BlockedListTO objBlockedListTO = new BlockedListTO();
        objBlockedListTO.setCommand(command);
        if(command.equals(CommonConstants.TOSTATUS_INSERT)){
            objBlockedListTO.setStatus(CommonConstants.STATUS_CREATED);
        }else  if(command.equals(CommonConstants.TOSTATUS_UPDATE)){
            objBlockedListTO.setStatus(CommonConstants.STATUS_MODIFIED);
        }else  if(command.equals(CommonConstants.TOSTATUS_DELETE)){
            objBlockedListTO.setStatus(CommonConstants.STATUS_DELETED);
        }
        objBlockedListTO.setBlockId(getTxtBlockedListId());
        objBlockedListTO.setBlockedName(getTxtBlockedName());
        objBlockedListTO.setBusinessAddr(getTxaBusinessAddress());
        objBlockedListTO.setCustomerType(CommonUtil.convertObjToStr(getCbmCustomerType().getKeyForSelected()));
        objBlockedListTO.setFraudStatus(CommonUtil.convertObjToStr(getCbmFraudStatus().getKeyForSelected()));
        objBlockedListTO.setFraudClassification(CommonUtil.convertObjToStr(getCbmFraudClassification().getKeyForSelected()));
        if(cbmFraudClassification.getKeyForSelected().equals("OTHERS")){
            objBlockedListTO.setFraudClassification(getTxtFraudClassifcation());
        }else{
            objBlockedListTO.setFraudClassification(CommonUtil.convertObjToStr(getCbmFraudClassification().getKeyForSelected()));
        }
        objBlockedListTO.setRemarks(getTxaRemarks());
        objBlockedListTO.setStatusBy(TrueTransactMain.USER_ID);
        objBlockedListTO.setStatusDt(currDt);
        return objBlockedListTO;
    }
    
    /** Sets the tovaraibles values to the OB Variables there by  setting to the UI **/
    private void setBlockedListTO(BlockedListTO objBlockedListTO){
        setTxtBlockedListId(objBlockedListTO.getBlockId());
        setTxtBlockedName(objBlockedListTO.getBlockedName());
        setTxaBusinessAddress(objBlockedListTO.getBusinessAddr());
        setCboCustomerType(CommonUtil.convertObjToStr(getCbmCustomerType().getDataForKey(objBlockedListTO.getCustomerType())));
        setCboFraudStatus(CommonUtil.convertObjToStr(getCbmFraudStatus().getDataForKey(objBlockedListTO.getFraudStatus())));
        setCboFraudClassification(CommonUtil.convertObjToStr(getCbmFraudClassification().getDataForKey(objBlockedListTO.getFraudClassification())));
        if(getCboFraudClassification().equals("")){
            setCboFraudClassification(CommonUtil.convertObjToStr(cbmFraudClassification.getDataForKey("OTHERS")));
            setTxtFraudClassifcation(objBlockedListTO.getFraudClassification());
        }
        setTxaRemarks(objBlockedListTO.getRemarks());
        setStatusBy(objBlockedListTO.getStatusBy());
        setAuthorizeStatus(objBlockedListTO.getAuthorizeStatus());
        notifyObservers();
    }
    
    /** Resets all the fields to be empty **/
    public void resetForm(){
        setTxtBlockedListId("");
        setTxtBlockedName("");
        setTxaBusinessAddress("");
        setCboCustomerType("");
        setCboFraudStatus("");
        setCboFraudClassification("");
        setTxaRemarks("");
        notifyObservers();
    }
    
    /* Populates the TO object by executing a Query */
    public void populateData(HashMap whereMap) {
        HashMap mapData=null;
        try {
            mapData = proxy.executeQuery(whereMap, map);
            BlockedListTO objBlockedListTO =
            (BlockedListTO) ((List) mapData.get("BlockedListTO")).get(0);
            setBlockedListTO(objBlockedListTO);
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            
        }
    }
    
    /* Executes Query using the TO object */
    public void execute(String command) {
        try {
            HashMap term = new HashMap();
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            term.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            term.put("BlockedListTO", getBlockedListTO(command));
            HashMap proxyResultMap = proxy.execute(term, map);
            setProxyReturnMap(proxyResultMap);
            setResult(getActionType());
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
}