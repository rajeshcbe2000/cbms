/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RelationshipsOB.java
 *
 * Created on July 15, 2004, 4:23 PM
 */

package com.see.truetransact.ui.privatebanking.comlogs.relationships;

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
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.transferobject.privatebanking.comlogs.relationships.RelationshipsTO;
import com.see.truetransact.clientutil.ComboBoxModel;
import org.apache.log4j.Logger;
import com.see.truetransact.ui.TrueTransactMain;

public class RelationshipsOB extends Observable{
    
    private String lblRelationshipId = "";
    private String txtMember = "";
    private String txtBankerName = "";
    private String txtInitiatedBy = "";
    private String txtLeadRSO = "";
    private String txaContactDescription = "";
    private String cboType = "";
    private String cboSource = "";
    private String txtSourceReference = "";
    private String tdtContactDate = "";
    private String cboSubType = "";
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static Logger _log = Logger.getLogger(RelationshipsOB.class);
    private int _result,_actionType;
    private ProxyFactory proxy;
    private HashMap lookUpHash;
    private HashMap keyValue,map;
    private ArrayList key;
    private ArrayList value;
    private static RelationshipsOB objRelationshipsOB;
    private ComboBoxModel cbmSource,cbmType,cbmSubType;
    
    /** Creates a new instance of RelationshipsOB */
    private RelationshipsOB() {
        map= new HashMap();
        map.put(CommonConstants.JNDI, "RelationshipsJNDI");
        map.put(CommonConstants.HOME, "serverside.privatebanking.comlogs.relationships.RelationshipsHome");
        map.put(CommonConstants.REMOTE, "serverside.privatebanking.comlogs.relationships.Relationships");
        try {
            proxy = ProxyFactory.createProxy();
            initUIComboBoxModel();
            fillDropdown();
        } catch (Exception e) {
             parseException.logException(e,true);
        }
    }
    
    static {
        try {
            _log.info("Creating BillsOB...");
            objRelationshipsOB= new RelationshipsOB();
        } catch(Exception e) {
             parseException.logException(e,true);
        }
    }
    
    /** This method Returns an instance of this class **/
    public static RelationshipsOB getInstance()throws Exception{
           return objRelationshipsOB;
    }
    
    /**
     * Getter for property lblRelationshipId.
     * @return Value of property lblRelationshipId.
     */
    public java.lang.String getLblRelationshipId() {
        return lblRelationshipId;
    }
    
    /**
     * Setter for property lblRelationshipId.
     * @param lblRelationshipId New value of property lblRelationshipId.
     */
    public void setLblRelationshipId(java.lang.String lblRelationshipId) {
        this.lblRelationshipId = lblRelationshipId;
        setChanged();
    }
    
    /**
     * Getter for property txtMember.
     * @return Value of property txtMember.
     */
    public java.lang.String getTxtMember() {
        return txtMember;
    }
    
    /**
     * Setter for property txtMember.
     * @param txtMember New value of property txtMember.
     */
    public void setTxtMember(java.lang.String txtMember) {
        this.txtMember = txtMember;
    }
    
    // Setter method for txtBankerName
    void setTxtBankerName(String txtBankerName){
        this.txtBankerName = txtBankerName;
        setChanged();
    }
    // Getter method for txtBankerName
    String getTxtBankerName(){
        return this.txtBankerName;
    }
    
    // Setter method for cboInitiatedBy
    void setTxtInitiatedBy(String txtInitiatedBy){
        this.txtInitiatedBy = txtInitiatedBy;
        setChanged();
    }
    // Getter method for cboInitiatedBy
    String getTxtInitiatedBy(){
        return this.txtInitiatedBy;
    }
    
    // Setter method for txtLeadRSO
    void setTxtLeadRSO(String txtLeadRSO){
        this.txtLeadRSO = txtLeadRSO;
        setChanged();
    }
    // Getter method for txtLeadRSO
    String getTxtLeadRSO(){
        return this.txtLeadRSO;
    }
    
    // Setter method for txaContactDescription
    void setTxaContactDescription(String txaContactDescription){
        this.txaContactDescription = txaContactDescription;
        setChanged();
    }
    // Getter method for txaContactDescription
    String getTxaContactDescription(){
        return this.txaContactDescription;
    }
    
    // Setter method for cboType
    void setCboType(String cboType){
        this.cboType = cboType;
        setChanged();
    }
    // Getter method for cboType
    String getCboType(){
        return this.cboType;
    }
    
    /**
     * Getter for property cbmType.
     * @return Value of property cbmType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmType() {
        return cbmType;
    }
    
    /**
     * Setter for property cbmType.
     * @param cbmType New value of property cbmType.
     */
    public void setCbmType(com.see.truetransact.clientutil.ComboBoxModel cbmType) {
        this.cbmType = cbmType;
    }
    
    
    // Setter method for cboSource
    void setCboSource(String cboSource){
        this.cboSource = cboSource;
        setChanged();
    }
    // Getter method for cboSource
    String getCboSource(){
        return this.cboSource;
    }
    
    /**
     * Getter for property cbmSource.
     * @return Value of property cbmSource.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmSource() {
        return cbmSource;
    }
    
    /**
     * Setter for property cbmSource.
     * @param cbmSource New value of property cbmSource.
     */
    public void setCbmSource(com.see.truetransact.clientutil.ComboBoxModel cbmSource) {
        this.cbmSource = cbmSource;
    }
    
    // Setter method for txtSourceReference
    void setTxtSourceReference(String txtSourceReference){
        this.txtSourceReference = txtSourceReference;
        setChanged();
    }
    // Getter method for txtSourceReference
    String getTxtSourceReference(){
        return this.txtSourceReference;
    }
    
    // Setter method for tdtContactDate
    void setTdtContactDate(String tdtContactDate){
        this.tdtContactDate = tdtContactDate;
        setChanged();
    }
    // Getter method for tdtContactDate
    String getTdtContactDate(){
        return this.tdtContactDate;
    }
    
    // Setter method for cboSubType
    void setCboSubType(String cboSubType){
        this.cboSubType = cboSubType;
        setChanged();
    }
    // Getter method for cboSubType
    String getCboSubType(){
        return this.cboSubType;
    }
    
    /**
     * Getter for property cbmSubType.
     * @return Value of property cbmSubType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmSubType() {
        return cbmSubType;
    }
    
    /**
     * Setter for property cbmSubType.
     * @param cbmSubType New value of property cbmSubType.
     */
    public void setCbmSubType(com.see.truetransact.clientutil.ComboBoxModel cbmSubType) {
        this.cbmSubType = cbmSubType;
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
    
    /** Returns an instance of TO object */
    public RelationshipsTO getRelationshipsTO(String command){
        RelationshipsTO objRelationshipsTO = new RelationshipsTO();
        objRelationshipsTO.setCommand(command);
        if(objRelationshipsTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
            objRelationshipsTO.setStatus(CommonConstants.STATUS_CREATED);
        }else{
            objRelationshipsTO.setStatusBy(TrueTransactMain.USER_ID);
        }
        objRelationshipsTO.setRelateId(getLblRelationshipId());
        objRelationshipsTO.setMemberId(getTxtMember());
        objRelationshipsTO.setBankerName(getTxtBankerName());
        objRelationshipsTO.setInitiatedBy(getTxtInitiatedBy());
        objRelationshipsTO.setLeadRso(getTxtLeadRSO());
        objRelationshipsTO.setContactDesc(getTxaContactDescription());
        objRelationshipsTO.setRelateType(getCboType());
        objRelationshipsTO.setRelateSource(getCboSource());
        objRelationshipsTO.setSourceRef(getTxtSourceReference());
        Date IsDt = DateUtil.getDateMMDDYYYY(getTdtContactDate());
        if(IsDt != null){
        Date isDate = ClientUtil.getCurrentDate();
        isDate.setDate(IsDt.getDate());
        isDate.setMonth(IsDt.getMonth());
        isDate.setYear(IsDt.getYear());
        objRelationshipsTO.setContactDt(isDate);
        }else{
            objRelationshipsTO.setContactDt(DateUtil.getDateMMDDYYYY(getTdtContactDate()));
        }
//        objRelationshipsTO.setContactDt(DateUtil.getDateMMDDYYYY(getTdtContactDate()));
        objRelationshipsTO.setSubType(getCboSubType());
        return objRelationshipsTO;
    }
    
    /* sets the OB Fields thru TO objects */
    public void setRelationshipsTO(RelationshipsTO objRelationshipsTO){
        setLblRelationshipId(objRelationshipsTO.getRelateId());
        setTxtMember(objRelationshipsTO.getMemberId());
        setTxtBankerName(objRelationshipsTO.getBankerName());
        setTxtInitiatedBy(objRelationshipsTO.getInitiatedBy());
        setTxtLeadRSO(objRelationshipsTO.getLeadRso());
        setTxaContactDescription(objRelationshipsTO.getContactDesc());
        setCboType(objRelationshipsTO.getRelateType());
        setCboSource(objRelationshipsTO.getRelateSource());
        setTxtSourceReference(objRelationshipsTO.getSourceRef());
        setTdtContactDate(DateUtil.getStringDate(objRelationshipsTO.getContactDt()));
        setCboSubType(objRelationshipsTO.getSubType());
        notifyObservers();
    }
    
    /** Creates the instances for the comboboxmodels */
    private void initUIComboBoxModel(){
        cbmType = new ComboBoxModel();
        cbmSource = new ComboBoxModel();
        cbmSubType = new ComboBoxModel();
    }
    
    /* Filling up the the ComboBox in the UI*/
    private void fillDropdown() throws Exception{
        try{
            _log.info("Inside FillDropDown");
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("PVT_RELATE.TYPE");
            lookup_keys.add("PVT.CONTACT_MODE");
            lookup_keys.add("PVT_RELATE.SUB_TYPE");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get("PVT_RELATE.TYPE"));
            cbmType = new ComboBoxModel(key,value);
            getKeyValue((HashMap) keyValue.get("PVT_RELATE.SUB_TYPE"));
            cbmSubType = new ComboBoxModel(key,value);
            getKeyValue((HashMap) keyValue.get("PVT.CONTACT_MODE"));
            cbmSource = new ComboBoxModel(key,value);
        }catch(NullPointerException e){
              parseException.logException(e,true);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    /** Returns a map by executing a  query which can be used to fill up the labels in the ui */
    public HashMap getLabelMap(String orderId)throws Exception {
        HashMap resultMap = null;
        try {
            HashMap whereMap = new HashMap();
            whereMap.put("ORD_ID", orderId);
            List resultList = ClientUtil.executeQuery("SelectPvtOrderMaster", whereMap);
            resultMap = (HashMap)resultList.get(0);
            resultList = null;
        }catch(Exception e){
        }
        return resultMap;
    }
    
    /* Executes Query using the TO object */
    public void execute(String command) {
        try {
            HashMap term = new HashMap();
            term.put("RelationshipsTO", getRelationshipsTO(command));
            HashMap proxyResultMap = proxy.execute(term, map);
            term = null;
            setResult(getActionType());
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
     /* Populates the TO object by executing a Query */
    public void populateData(HashMap whereMap) {
        HashMap mapData=null;
        try {
            mapData = proxy.executeQuery(whereMap, map);
            RelationshipsTO objRelationshipsTO =
            (RelationshipsTO) ((List) mapData.get("RelationshipsTO")).get(0);
            setRelationshipsTO(objRelationshipsTO);
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            
        }
    }
    
    /** Resets the OBFields */
    public void resetForm(){
        setTxtMember("");
        setTxtBankerName("");
        setTxtInitiatedBy("");
        setTxtLeadRSO("");
        setTxaContactDescription("");
        setCboType("");
        setCboSource("");
        setTxtSourceReference("");
        setTdtContactDate("");
        setCboSubType("");
        notifyObservers();
    }
}
