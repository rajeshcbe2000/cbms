/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DepositRolloverOB.java
 *
 * Created on June 16, 2004, 3:24 PM
 */

package com.see.truetransact.ui.privatebanking.orders;

import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.transferobject.privatebanking.orders.DepositRolloverTO;
import com.see.truetransact.ui.privatebanking.orders.DepositRolloverRB;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uicomponent.CObservable;

import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Date;

/**
 *
 * @author  Lohith R.
 */

public class DepositRolloverOB extends CObservable {
    
    private static DepositRolloverOB objDepositRolloverOB; // singleton object
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private String orderID = "";
    private String txtMember = "";
    private String txtPhoneExtnum = "";
    private String txtDescription = "";
    private String dateContactDate = "";
    private String dateSrcDocDate = "";
    private String txtSrcDocDetails = "";
    private String cboViewInfoDoc = "";
    private String cboContactMode = "";
    private String cboOrderType = "";
    private String cboRelationship = "";
    private String cboAuthSrcDoc = "";
    private String cboSolicited = "";
    private String cboInstructionFrom = "";
    private String cboContactTimeMinutes = "";
    private String cboContactTimeHours = "";
    private String cboClientContact = "";
    
    private ComboBoxModel cbmViewInfoDoc;
    private ComboBoxModel cbmContactMode;
    private ComboBoxModel cbmOrderType;
    private ComboBoxModel cbmRelationship;
    private ComboBoxModel cbmAuthSrcDoc;
    private ComboBoxModel cbmSolicited;
    private ComboBoxModel cbmInstructionFrom;
    private ComboBoxModel cbmContactTimeMinutes;
    private ComboBoxModel cbmContactTimeHours;
    private ComboBoxModel cbmClientContact;
    
    private int actionType;
    private int result;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private ProxyFactory proxy;
    private HashMap operationMap;
    public String orderid = "";
    private int optionMemberRelation;
    Date curDate = null;
    
    static {
        try {
            objDepositRolloverOB = new DepositRolloverOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** Creates a new instance of DepositRolloverOB */
    public DepositRolloverOB()throws Exception {
        curDate = ClientUtil.getCurrentDate();
        proxy = ProxyFactory.createProxy();
        setOperationMap();
        fillDropdown();
        cbmRelationship = new ComboBoxModel();
    }
    
    /** Creates a new instance of RemittancePaymentOB */
    public static DepositRolloverOB getInstance() {
        return objDepositRolloverOB;
    }
    
    /** Sets the HashMap required to set JNDI,Home and Remote*/
    private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "DepositRolloverJNDI");
        operationMap.put(CommonConstants.HOME, "privatebanking.orders.DepositRolloverHome");
        operationMap.put(CommonConstants.REMOTE, "privatebanking.orders.DepositRollover");
    }
    
    /** A method to set the combo box values */
    private void fillDropdown() throws Exception{
        lookUpHash = new HashMap();
        // The data to be show in Combo Box from LOOKUP_MASTER table
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("PVT.CONTACT_MODE");
        lookup_keys.add("PVT.ORDER_TYPE");
        lookup_keys.add("PVT.INSTRUCT_FROM");
        lookup_keys.add("PVT.SOLICITED");
        lookup_keys.add("PVT.AUTH_SOUR");
        lookup_keys.add("PVT.VISUAL_INFO");
        lookup_keys.add("HOURS");
        lookup_keys.add("MINUTES");
        
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        
        getKeyValue((HashMap)keyValue.get("PVT.CONTACT_MODE"));
        cbmContactMode = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("PVT.ORDER_TYPE"));
        cbmOrderType = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("PVT.INSTRUCT_FROM"));
        cbmInstructionFrom = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("PVT.SOLICITED"));
        cbmSolicited = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("PVT.AUTH_SOUR"));
        cbmAuthSrcDoc = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("PVT.VISUAL_INFO"));
        cbmViewInfoDoc = new ComboBoxModel(key,value);
        
        
        getKeyValue((HashMap)keyValue.get("HOURS"));
        cbmContactTimeHours = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("MINUTES"));
        cbmContactTimeMinutes = new ComboBoxModel(key,value);
        
        
        /** The data to be show in Combo Box other than LOOKUP_MASTER table  */
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,"DepositRolloverTO.getClientContact");
        lookUpHash.put(CommonConstants.PARAMFORQUERY,null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmClientContact = new ComboBoxModel(key,value);
        
        makeNull();
    }
    
    /** A method to set Relationship combo box value */
    public void setComboRelationship(){
        try{
            int keySize = 0;
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME,"DepositRolloverTO.getRelationship");
            lookUpHash.put(CommonConstants.PARAMFORQUERY,null);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            orderid = getOrderID();
            getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
            if(getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                keySize = key.size();
                for(int i=0;i<keySize;i++){
                    if(key.get(i).equals(orderid)){
                        key.remove(i);
                        value.remove(i);
                        break;
                    }
                }
            }
            cbmRelationship = new ComboBoxModel(key,value);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    private void makeNull(){
        key = null;
        value = null;
    }
    
    /** Setter method for orderID */
    void setOrderID(String orderID){
        this.orderID = orderID;
        setChanged();
    }
    /** Getter method for orderID */
    String getOrderID(){
        return this.orderID;
    }
    
    /** Setter method for txtMember */
    void setTxtMember(String txtMember){
        this.txtMember = txtMember;
        setChanged();
    }
    /** Getter method for txtMember */
    String getTxtMember(){
        return this.txtMember;
    }
    
    /** Setter method for cboContactMode */
    void setCboContactMode(String cboContactMode){
        this.cboContactMode = cboContactMode;
        setChanged();
    }
    /** Getter method for cboContactMode */
    String getCboContactMode(){
        return this.cboContactMode;
    }
    
    /** Setter method for cbmContactMode */
    void setCbmContactMode(ComboBoxModel cbmContactMode){
        this.cbmContactMode = cbmContactMode;
        setChanged();
    }
    /** Getter method for cbmContactMode */
    ComboBoxModel getCbmContactMode(){
        return cbmContactMode;
    }
    
    /** Setter method for cComboBox3 */
    void setCboRelationship(String cboRelationship){
        this.cboRelationship = cboRelationship;
        setChanged();
    }
    /** Getter method for cboRelationship */
    String getCboRelationship(){
        return this.cboRelationship;
    }
    
    /** Setter method for cbmRelationship */
    void setCbmRelationship(ComboBoxModel cbmRelationship){
        this.cbmRelationship = cbmRelationship;
        setChanged();
    }
    /** Getter method for cbmRelationship */
    ComboBoxModel getCbmRelationship(){
        return cbmRelationship;
    }
    
    /** Setter method for cboOrderType */
    void setCboOrderType(String cboOrderType){
        this.cboOrderType = cboOrderType;
        setChanged();
    }
    /** Getter method for cboOrderType */
    String getCboOrderType(){
        return this.cboOrderType;
    }
    
    /** Setter method for cbmOrderType */
    void setCbmOrderType(ComboBoxModel cbmOrderType){
        this.cbmOrderType = cbmOrderType;
        setChanged();
    }
    /** Getter method for cbmOrderType */
    ComboBoxModel getCbmOrderType(){
        return cbmOrderType;
    }
    
    /** Setter method for cboAuthSrcDoc */
    void setCboAuthSrcDoc(String cboAuthSrcDoc){
        this.cboAuthSrcDoc = cboAuthSrcDoc;
        setChanged();
    }
    /** Getter method for cboAuthSrcDoc */
    String getCboAuthSrcDoc(){
        return this.cboAuthSrcDoc;
    }
    
    /** Setter method for cbmAuthSrcDoc */
    void setCbmAuthSrcDoc(ComboBoxModel cbmAuthSrcDoc){
        this.cbmAuthSrcDoc = cbmAuthSrcDoc;
        setChanged();
    }
    /** Getter method for cbmAuthSrcDoc */
    ComboBoxModel getCbmAuthSrcDoc(){
        return cbmAuthSrcDoc;
    }
    
    /** Setter method for txtSrcDocDetails */
    void setTxtSrcDocDetails(String txtSrcDocDetails){
        this.txtSrcDocDetails = txtSrcDocDetails;
        setChanged();
    }
    /** Getter method for txtSrcDocDetails */
    String getTxtSrcDocDetails(){
        return this.txtSrcDocDetails;
    }
    
    /** Setter method for cboSolicited */
    void setCboSolicited(String cboSolicited){
        this.cboSolicited = cboSolicited;
        setChanged();
    }
    /** Getter method for cboSolicited */
    String getCboSolicited(){
        return this.cboSolicited;
    }
    
    /** Setter method for cbmSolicited */
    void setCbmSolicited(ComboBoxModel cbmSolicited){
        this.cbmSolicited = cbmSolicited;
        setChanged();
    }
    /**Getter method for cbmSolicited */
    ComboBoxModel getCbmSolicited(){
        return cbmSolicited;
    }
    
    /** Setter method for cboInstructionFrom */
    void setCboInstructionFrom(String cboInstructionFrom){
        this.cboInstructionFrom = cboInstructionFrom;
        setChanged();
    }
    /** Getter method for cboInstructionFrom */
    String getCboInstructionFrom(){
        return this.cboInstructionFrom;
    }
    
    /** Setter method for cbmInstructionFrom */
    void setCbmInstructionFrom(ComboBoxModel cbmInstructionFrom){
        this.cbmInstructionFrom = cbmInstructionFrom;
        setChanged();
    }
    /** Getter method for cbmInstructionFrom */
    ComboBoxModel getCbmInstructionFrom(){
        return cbmInstructionFrom;
    }
    
    /** Setter method for txtPhoneExtnum */
    void setTxtPhoneExtnum(String txtPhoneExtnum){
        this.txtPhoneExtnum = txtPhoneExtnum;
        setChanged();
    }
    /** Getter method for txtPhoneExtnum */
    String getTxtPhoneExtnum(){
        return this.txtPhoneExtnum;
    }
    
    /** Setter method for dateContactDate */
    void setDateContactDate(String dateContactDate){
        this.dateContactDate = dateContactDate;
        setChanged();
    }
    /** Getter method for dateContactDate */
    String getDateContactDate(){
        return this.dateContactDate;
    }
    
    /** Setter method for cboContactTimeMinutes */
    void setCboContactTimeMinutes(String cboContactTimeMinutes){
        this.cboContactTimeMinutes = cboContactTimeMinutes;
        setChanged();
    }
    /** Getter method for cboContactTimeMinutes */
    String getCboContactTimeMinutes(){
        return this.cboContactTimeMinutes;
    }
    
    /** Setter method for cbmContactTimeMinutes */
    void setCbmContactTimeMinutes(ComboBoxModel cbmContactTimeMinutes){
        this.cbmContactTimeMinutes = cbmContactTimeMinutes;
        setChanged();
    }
    /** Getter method for cbmContactTimeMinutes */
    ComboBoxModel getCbmContactTimeMinutes(){
        return cbmContactTimeMinutes;
    }
    
    /** Setter method for cboViewInfoDoc */
    void setCboViewInfoDoc(String cboViewInfoDoc){
        this.cboViewInfoDoc = cboViewInfoDoc;
        setChanged();
    }
    /** Getter method for cboViewInfoDoc */
    String getCboViewInfoDoc(){
        return this.cboViewInfoDoc;
    }
    
    /** Setter method for cbmViewInfoDoc */
    void setCbmViewInfoDoc(ComboBoxModel cbmViewInfoDoc){
        this.cbmViewInfoDoc = cbmViewInfoDoc;
        setChanged();
    }
    /** Getter method for cbmViewInfoDoc */
    ComboBoxModel getCbmViewInfoDoc(){
        return cbmViewInfoDoc;
    }
    
    /** Setter method for cboContactTimeHours */
    void setCboContactTimeHours(String cboContactTimeHours){
        this.cboContactTimeHours = cboContactTimeHours;
        setChanged();
    }
    /** Getter method for cboContactTimeHours */
    String getCboContactTimeHours(){
        return this.cboContactTimeHours;
    }
    
    /** Setter method for cbmContactTimeHours */
    void setCbmContactTimeHours(ComboBoxModel cbmContactTimeHours){
        this.cbmContactTimeHours = cbmContactTimeHours;
        setChanged();
    }
    /**Getter method for cbmContactTimeHours */
    ComboBoxModel getCbmContactTimeHours(){
        return cbmContactTimeHours;
    }
    
    /** Setter method for cboClientContact */
    void setCboClientContact(String cboClientContact){
        this.cboClientContact = cboClientContact;
        setChanged();
    }
    /** Getter method for cboClientContact */
    String getCboClientContact(){
        return this.cboClientContact;
    }
    
    /** Setter method for cbmClientContact */
    void setCbmClientContact(ComboBoxModel cbmClientContact){
        this.cbmClientContact = cbmClientContact;
        setChanged();
    }
    /** Getter method for cbmClientContact */
    ComboBoxModel getCbmClientContact(){
        return cbmClientContact;
    }
    
    /** Setter method for dateSrcDocDate */
    void setDateSrcDocDate(String dateSrcDocDate){
        this.dateSrcDocDate = dateSrcDocDate;
        setChanged();
    }
    /** Getter method for dateSrcDocDate */
    String getDateSrcDocDate(){
        return this.dateSrcDocDate;
    }
    
    /** Setter method for txtDescription */
    void setTxtDescription(String txtDescription){
        this.txtDescription = txtDescription;
        setChanged();
    }
    /** Getter method for txtDescription*/
    String getTxtDescription(){
        return this.txtDescription;
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
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        notifyObservers();
    }
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        notifyObservers();
    }
    
    public void setResult(int result) {
        this.result = result;
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
    
    /** Method to check if the Member is related to other Members */
    public boolean memberDependency(){
        final DepositRolloverRB objDepositRolloverRB = new DepositRolloverRB();
        boolean memberRelationExists = false;
        final HashMap whereMap = new HashMap();
        String member = getOrderID();
        whereMap.put("MEMBER",member);
        final List resultList = ClientUtil.executeQuery("ViewAllMemberRelationship", whereMap);
        if(!resultList.isEmpty()){
            // The Member is related to other Members
            String[] options = {objDepositRolloverRB.getString("cDialogOk")};
            optionMemberRelation = COptionPane.showOptionDialog(null, objDepositRolloverRB.getString("memberRelationshipDeleteWarning"), CommonConstants.WARNINGTITLE,
            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
            memberRelationExists = true;
            setResult(0);
        }
        return memberRelationExists;
    }
    
    /** To perform the appropriate operation */
    public void doAction() {
        try {
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                if( getCommand() != null ){
                    doActionPerform();
                }
                else{
                    final DepositRolloverRB objDepositRolloverRB = new DepositRolloverRB();
                    throw new TTException(objDepositRolloverRB.getString("TOCommandError"));
                }
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
        }
    }
    
    /** To get the value of action performed */
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
        final DepositRolloverTO objDepositRolloverTO = setDepositRolloverTOData();
        objDepositRolloverTO.setCommand(getCommand());
        final HashMap data = new HashMap();
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        data.put("DepositRolloverTO",objDepositRolloverTO);
        HashMap proxyResultMap = proxy.execute(data, operationMap);
        setResult(actionType);
    }
    
    /** Gets the datas from the Fields and sets to DepositRollover TO */
    public DepositRolloverTO setDepositRolloverTOData() {
        final DepositRolloverTO objDepositRolloverTO = new DepositRolloverTO();
        try{
            objDepositRolloverTO.setOrdId(getOrderID());
            objDepositRolloverTO.setMember(getTxtMember());
            objDepositRolloverTO.setRelationship(CommonUtil.convertObjToStr(cbmRelationship.getKeyForSelected()));
            objDepositRolloverTO.setOrderType(CommonUtil.convertObjToStr(cbmOrderType.getKeyForSelected()));
            objDepositRolloverTO.setContactMode(CommonUtil.convertObjToStr(cbmContactMode.getKeyForSelected()));
            
            Date ConDt = DateUtil.getDateMMDDYYYY(dateContactDate);
            if(ConDt != null){
            Date conDate = (Date)curDate.clone();
            conDate.setDate(ConDt.getDate());
            conDate.setMonth(ConDt.getMonth());
            conDate.setYear(ConDt.getYear());
//            objDepositRolloverTO.setContactDt(DateUtil.getDateMMDDYYYY(dateContactDate));
            objDepositRolloverTO.setContactDt(conDate);
            }else{
                objDepositRolloverTO.setContactDt(DateUtil.getDateMMDDYYYY(dateContactDate));
            }
            
            objDepositRolloverTO.setContactHr(CommonUtil.convertObjToStr(cbmContactTimeHours.getKeyForSelected()));
            objDepositRolloverTO.setContactMins(CommonUtil.convertObjToStr(cbmContactTimeMinutes.getKeyForSelected()));
            objDepositRolloverTO.setClientContact(CommonUtil.convertObjToStr(cbmClientContact.getKeyForSelected()));
            objDepositRolloverTO.setPhoneExt(getTxtPhoneExtnum());
            objDepositRolloverTO.setInstructionFrom(CommonUtil.convertObjToStr(cbmInstructionFrom.getKeyForSelected()));
            objDepositRolloverTO.setSolicited(CommonUtil.convertObjToStr(cbmSolicited.getKeyForSelected()));
            
            Date ScrDt = DateUtil.getDateMMDDYYYY(dateContactDate);
            if(ScrDt != null){
            Date scrDate = (Date)curDate.clone();;
            scrDate.setDate(ScrDt.getDate());
            scrDate.setMonth(ScrDt.getMonth());
            scrDate.setYear(ScrDt.getYear());
//            objDepositRolloverTO.setSourDocDt(DateUtil.getDateMMDDYYYY(dateContactDate));
            objDepositRolloverTO.setSourDocDt(scrDate);
            }else{
               objDepositRolloverTO.setSourDocDt(DateUtil.getDateMMDDYYYY(dateContactDate));
            }
            
            objDepositRolloverTO.setDescription(getTxtDescription());
            objDepositRolloverTO.setAuthSourDoc(CommonUtil.convertObjToStr(cbmAuthSrcDoc.getKeyForSelected()));
            objDepositRolloverTO.setSourDocDetails(getTxtSrcDocDetails());
            objDepositRolloverTO.setViewVisual(CommonUtil.convertObjToStr(cbmViewInfoDoc.getKeyForSelected()));
            objDepositRolloverTO.setStatusBy(TrueTransactMain.USER_ID);
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return objDepositRolloverTO;
    }
    
    /** To populate to the screen */
    public void populateData(HashMap whereMap) {
        HashMap mapData = new HashMap() ;
        try {
            mapData = proxy.executeQuery(whereMap, operationMap);
            populateOB(mapData);
        } catch( Exception e ) {
            parseException.logException(e,true);
        }
    }
    
    private void populateOB(HashMap mapData) {
        DepositRolloverTO objDepositRolloverTO;
        objDepositRolloverTO = (DepositRolloverTO) ((List) mapData.get("DepositRolloverTO")).get(0);
        getDepositRolloverTO(objDepositRolloverTO);
        objDepositRolloverTO = null;
        notifyObservers();
    }
    
    /** Gets datas from DepositRollover TO and sets to Fields*/
    public void getDepositRolloverTO(DepositRolloverTO objDepositRolloverTO){
        setTxtMember(objDepositRolloverTO.getMember());
        setCboContactMode(CommonUtil.convertObjToStr(getCbmContactMode().getDataForKey(objDepositRolloverTO.getContactMode())));
        setCboOrderType(CommonUtil.convertObjToStr(getCbmOrderType().getDataForKey(objDepositRolloverTO.getOrderType())));
        setCboRelationship(CommonUtil.convertObjToStr(getCbmRelationship().getDataForKey(objDepositRolloverTO.getRelationship())));
        setTxtSrcDocDetails(objDepositRolloverTO.getSourDocDetails());
        setCboAuthSrcDoc(CommonUtil.convertObjToStr(getCbmAuthSrcDoc().getDataForKey(objDepositRolloverTO.getAuthSourDoc())));
        setCboSolicited(CommonUtil.convertObjToStr(getCbmSolicited().getDataForKey(objDepositRolloverTO.getSolicited())));
        setCboInstructionFrom(CommonUtil.convertObjToStr(getCbmInstructionFrom().getDataForKey(objDepositRolloverTO.getInstructionFrom())));
        setTxtPhoneExtnum(objDepositRolloverTO.getPhoneExt());
        setCboContactTimeMinutes(CommonUtil.convertObjToStr(getCbmContactTimeMinutes().getDataForKey(objDepositRolloverTO.getContactMins())));
        setCboContactTimeHours(CommonUtil.convertObjToStr(getCbmContactTimeHours().getDataForKey(objDepositRolloverTO.getContactHr())));
        setCboClientContact(CommonUtil.convertObjToStr(getCbmClientContact().getDataForKey(objDepositRolloverTO.getClientContact())));
        setTxtDescription(objDepositRolloverTO.getDescription());
        setCboViewInfoDoc(CommonUtil.convertObjToStr(getCbmViewInfoDoc().getDataForKey(objDepositRolloverTO.getViewVisual())));
        setDateContactDate(DateUtil.getStringDate(objDepositRolloverTO.getContactDt()));
        setDateSrcDocDate(DateUtil.getStringDate(objDepositRolloverTO.getSourDocDt()));
    }
    
    /** Resets all the Fields to Null  */
    public void resetFields(){
        setTxtMember("");
        setCboContactMode("");
        setCboOrderType("");
        setCboRelationship("");
        setTxtSrcDocDetails("");
        setCboAuthSrcDoc("");
        setCboSolicited("");
        setCboInstructionFrom("");
        setTxtPhoneExtnum("");
        setCboContactTimeMinutes("");
        setCboContactTimeHours("");
        setCboClientContact("");
        setTxtDescription("");
        setCboViewInfoDoc("");
        setDateContactDate("");
        setDateSrcDocDate("");
        notifyObservers();
    }
}