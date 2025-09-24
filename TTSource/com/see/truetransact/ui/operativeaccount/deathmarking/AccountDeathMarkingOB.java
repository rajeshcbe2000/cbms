/*
 *Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 * DeathMarkingOB.java
 *
 * Created on June 3, 2004, 12:59 PM
 */

package com.see.truetransact.ui.operativeaccount.deathmarking;

/**
 *
 * @author  Ashok
 */

import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.transferobject.operativeaccount.deathmarking.AccountDeathMarkingTO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;


public class AccountDeathMarkingOB extends CObservable {
    private String cboProductId = "";
    private String txtAccountNumber = "";
    private String tdtDtOfDeath = "";
    private String tdtReportedOn = "";
    private String txtReportedBy = "";
    private String cboRelationship = "";
    private String txtReferenceNo = "";
    private String txtRemarks = "";
    private HashMap map,lookupMap,lookupValues;
    private ProxyFactory proxy;
    private ArrayList key,value;
    private int _result;
    private int _actionType;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private static AccountDeathMarkingOB accountDeathMarkingOB;//singleton object
    private ComboBoxModel cbmProductId,cbmRelationship;
    private HashMap _authorizeMap;
    Date curDate = null;
    
    
    /** Creates a new instance of DeathMarkingOB */
    private AccountDeathMarkingOB() throws Exception {
        curDate = ClientUtil.getCurrentDate();
        map = new HashMap();
        map.put(CommonConstants.JNDI, "AccountDeathMarkingJNDI");
        map.put(CommonConstants.HOME, "operativeaccount.deathmarking.AccountDeathMarkingHome");
        map.put(CommonConstants.REMOTE,"operativeaccount.deathmarking.AccountDeathMarking");
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        fillDropDown();
    }
    
    static {
        try {
            accountDeathMarkingOB = new AccountDeathMarkingOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /**Retuns an instance of ob */
    public static AccountDeathMarkingOB getInstance(){
        return accountDeathMarkingOB;
    }
    
    // Setter method for cboProductId
    void setCboProductId(String cboProductId){
        this.cboProductId = cboProductId;
        setChanged();
    }
    // Getter method for cboProductId
    String getCboProductId(){
        return this.cboProductId;
    }
    
    /** Getter for property cbmProductId.
     * @return Value of property cbmProductId.
     *
     */
    public ComboBoxModel getCbmProductId() {
        return cbmProductId;
    }
    
    /** Setter for property cbmProductId.
     * @param cbmProductId New value of property cbmProductId.
     *
     */
    public void setCbmProductId(ComboBoxModel cbmProductId) {
        this.cbmProductId = cbmProductId;
    }
    
    // Setter method for txtDepositNumber
    void setTxtAccountNumber(String txtAccountNumber){
        this.txtAccountNumber = txtAccountNumber;
        setChanged();
    }
    // Getter method for txtDepositNumber
    String getTxtAccountNumber(){
        return this.txtAccountNumber;
    }
    
    // Setter method for tdtDtOfDeath
    void setTdtDtOfDeath(String tdtDtOfDeath){
        this.tdtDtOfDeath = tdtDtOfDeath;
        setChanged();
    }
    // Getter method for tdtDtOfDeath
    String getTdtDtOfDeath(){
        return this.tdtDtOfDeath;
    }
    
    // Setter method for tdtReportedOn
    void setTdtReportedOn(String tdtReportedOn){
        this.tdtReportedOn = tdtReportedOn;
        setChanged();
    }
    // Getter method for tdtReportedOn
    String getTdtReportedOn(){
        return this.tdtReportedOn;
    }
    
    // Setter method for txtReportedBy
    void setTxtReportedBy(String txtReportedBy){
        this.txtReportedBy = txtReportedBy;
        setChanged();
    }
    // Getter method for txtReportedBy
    String getTxtReportedBy(){
        return this.txtReportedBy;
    }
    
    // Setter method for cboRelationship
    void setCboRelationship(String cboRelationship){
        this.cboRelationship = cboRelationship;
        setChanged();
    }
    // Getter method for cboRelationship
    String getCboRelationship(){
        return this.cboRelationship;
    }
    
    /** Getter for property cbmRelationship.
     * @return Value of property cbmRelationship.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRelationship() {
        return cbmRelationship;
    }
    
    /** Setter for property cbmRelationship.
     * @param cbmRelationship New value of property cbmRelationship.
     *
     */
    public void setCbmRelationship(com.see.truetransact.clientutil.ComboBoxModel cbmRelationship) {
        this.cbmRelationship = cbmRelationship;
    }
    
    // Setter method for txtReferenceNo
    void setTxtReferenceNo(String txtReferenceNo){
        this.txtReferenceNo = txtReferenceNo;
        setChanged();
    }
    // Getter method for txtReferenceNo
    String getTxtReferenceNo(){
        return this.txtReferenceNo;
    }
    
    // Setter method for txtRemarks
    void setTxtRemarks(String txtRemarks){
        this.txtRemarks = txtRemarks;
        setChanged();
    }
    // Getter method for txtRemarks
    String getTxtRemarks(){
        return this.txtRemarks;
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
    
    // Setter for Authorization.
    public void setAuthorizeMap(HashMap authorizeMap) {
        _authorizeMap = authorizeMap;
    }
    
    // Getter for Authorization.
    public HashMap getAuthorizeMap() {
        return _authorizeMap;
    }
    
    /** Sets the ob fields thereby setting the ui fields */
    public  void setAccountDeathMarkingTO(AccountDeathMarkingTO objAccountDeathMarkingTO){
        setTxtAccountNumber(objAccountDeathMarkingTO.getActNum());
        setCboProductId(CommonUtil.convertObjToStr(getCbmProductId().getDataForKey(CommonUtil.convertObjToStr(objAccountDeathMarkingTO.getProductId()))));
        setTdtDtOfDeath(DateUtil.getStringDate(objAccountDeathMarkingTO.getDeathDt()));
        setTdtReportedOn(DateUtil.getStringDate(objAccountDeathMarkingTO.getReportedOn()));
        setTxtReportedBy(objAccountDeathMarkingTO.getReportedBy());
        setCboRelationship(objAccountDeathMarkingTO.getRelationship());
        setTxtReferenceNo(objAccountDeathMarkingTO.getReferenceNo());
        setTxtRemarks(objAccountDeathMarkingTO.getRemarks());
        notifyObservers();
    }
    
    /* Returns an instance of TO object */
    public AccountDeathMarkingTO getDeathMarkingTO(){
        AccountDeathMarkingTO objAccountDeathMarkingTO = new AccountDeathMarkingTO();
        objAccountDeathMarkingTO.setActNum(getTxtAccountNumber());
        objAccountDeathMarkingTO.setProductId(CommonUtil.convertObjToStr(cbmProductId.getKeyForSelected()));
        Date TdDt = DateUtil.getDateMMDDYYYY(getTdtDtOfDeath());
        if(TdDt != null){
        Date tdDate = (Date)curDate.clone();
        tdDate.setDate(TdDt.getDate());
        tdDate.setMonth(TdDt.getMonth());
        tdDate.setYear(TdDt.getYear());
//        objAccountDeathMarkingTO.setDeathDt(DateUtil.getDateMMDDYYYY(getTdtDtOfDeath()));
        objAccountDeathMarkingTO.setDeathDt(tdDate);
        }else{
             objAccountDeathMarkingTO.setDeathDt(DateUtil.getDateMMDDYYYY(getTdtDtOfDeath()));
        }
        
        Date TdRepDt = DateUtil.getDateMMDDYYYY(getTdtDtOfDeath());
        if(TdRepDt != null){
        Date tdrepDate = (Date)curDate.clone();
        tdrepDate.setDate(TdRepDt.getDate());
        tdrepDate.setMonth(TdRepDt.getMonth());
        tdrepDate.setYear(TdRepDt.getYear());
//        objAccountDeathMarkingTO.setReportedOn(DateUtil.getDateMMDDYYYY(getTdtReportedOn()));
        objAccountDeathMarkingTO.setReportedOn(tdrepDate);
        }else{
            objAccountDeathMarkingTO.setReportedOn(DateUtil.getDateMMDDYYYY(getTdtReportedOn()));
        }
        objAccountDeathMarkingTO.setReportedBy(getTxtReportedBy());
        objAccountDeathMarkingTO.setRelationship(getCboRelationship());
        objAccountDeathMarkingTO.setReferenceNo(getTxtReferenceNo());
        objAccountDeathMarkingTO.setRemarks(getTxtRemarks());
        return objAccountDeathMarkingTO;
    }
    
    /** Resets all the uifields */
    public void resetForm(){
        setTxtAccountNumber("");
        setCboProductId("");
        setTdtDtOfDeath("");
        setTdtReportedOn("");
        setTxtReportedBy("");
        setCboRelationship("");
        setTxtReferenceNo("");
        setTxtRemarks("");
        notifyObservers();
    }
    
    /** Fills the Dropdown by calling up a query */
    private void fillDropDown(){
        try{
            lookupMap = new HashMap();
            lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
            lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
            lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
            
            final HashMap param = new HashMap();
            param.put(CommonConstants.MAP_NAME, "getOpAccProductLookUp");
            param.put(CommonConstants.PARAMFORQUERY, null);
            lookupValues = ClientUtil.populateLookupData(param);
            fillData((HashMap)lookupValues.get(CommonConstants.DATA));
            cbmProductId = new ComboBoxModel(key,value);
            
            param.put(CommonConstants.MAP_NAME,null);
            final ArrayList lookupKey = new ArrayList();
            lookupKey.add("RELATIONSHIP");
            param.put(CommonConstants.PARAMFORQUERY, lookupKey);
            lookupValues = ClientUtil.populateLookupData(param);
            fillData((HashMap)lookupValues.get("RELATIONSHIP"));
            cbmRelationship = new ComboBoxModel(key,value);
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /** Fills the combobox with a model */
    private void fillData(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    /** To get data for comboboxes */
    private HashMap populateDataLocal(HashMap obj)  throws Exception{
        HashMap keyValue = proxy.executeQuery(obj,lookupMap);
        return keyValue;
    }
    
    /** To retrive Account Head details based on Product Id */
    public HashMap getAcctHeadForProd()throws Exception {
        HashMap resultMap = null;
        try {
            HashMap accountHeadMap = new HashMap();
            accountHeadMap.put("PROD_ID", getCboProductId());
            List resultList = ClientUtil.executeQuery("getAccountHead", accountHeadMap);
            resultMap = (HashMap)resultList.get(0);
        }catch(Exception e){
        }
        return resultMap;
    }
    
    /** To retrive Account Master details based on accountnumber to fill up the labels */
    public HashMap getActInfo(String accountNo)throws Exception {
        HashMap resultMap = null;
        try {
            HashMap actMap = new HashMap();
            actMap.put("ACT_NUM",accountNo);
            List resultList = ClientUtil.executeQuery("getAccountInfo", actMap);
            if(resultList.size() > 0){
                resultMap = (HashMap)resultList.get(0);
            }
        }catch(Exception e){
        }
        return resultMap;
    }
    
    /* Populates the TO object by executing a Query */
    public void populateData(HashMap whereMap) {
        HashMap mapData=null;
        try {
            mapData = proxy.executeQuery(whereMap, map);
            AccountDeathMarkingTO objAccountDeathMarkingTO =
            (AccountDeathMarkingTO) ((List) mapData.get("AccountDeathMarkingTO")).get(0);
            setAccountDeathMarkingTO(objAccountDeathMarkingTO);
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            
        }
    }
    /** To perform the appropriate operation */
    public void doAction() {
        try {
            if( _actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null || getAuthorizeMap() != null){
                    doActionPerform();
                }
                else{
                    final AccountDeathMarkingRB objAccountDeathMarkingRB = new AccountDeathMarkingRB();
                    throw new TTException(objAccountDeathMarkingRB.getString("TOCommandError"));
                }
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    /** To get the value of action performed */
    private String getCommand() throws Exception{
        String command = null;
        switch (_actionType) {
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
        final HashMap data = new HashMap();
        if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE){
            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
            data.put(CommonConstants.USER_ID, _authorizeMap.get(CommonConstants.USER_ID));
        }else {
            final AccountDeathMarkingTO objAccountDeathMarkingTO = getDeathMarkingTO();
            objAccountDeathMarkingTO.setCommand(getCommand());
            if(getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
                objAccountDeathMarkingTO.setCreatedBy(TrueTransactMain.USER_ID);
                objAccountDeathMarkingTO.setStatus("CREATED");
            }else {
                objAccountDeathMarkingTO.setStatusBy(TrueTransactMain.USER_ID);
            }
            
            data.put("AccountDeathMarkingTO",objAccountDeathMarkingTO);
            data.put(CommonConstants.MODULE, getModule());
            data.put(CommonConstants.SCREEN, getScreen());
        }
        HashMap proxyResultMap = proxy.execute(data, map);
        setResult(_actionType);
        _actionType = ClientConstants.ACTIONTYPE_NEW;
        resetForm();
    }
   
}
