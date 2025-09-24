/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CarrierOB.java
 *
 * Created on January 5, 2005, 11:47 AM
 */

package com.see.truetransact.ui.bills;

/**
 *
 * @author  152715
 */
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
import com.see.truetransact.transferobject.bills.CarrierTO;

public class CarrierOB extends CObservable {
    // Variables Declarations
    
    private String txtCarrierCode = "";
    private String txtCarrierName = "";
    private String cboCarrierType = "";
    private String txtAddress = "";
    private String txtPincode = "";
    private String cboCountry = "";
    private String cboState = "";
    private String cboCity = "";
    private boolean chkIsApproved = false;
    private String authorizeStatus = "";
    private String authorizeBy = "";
    private String authorizeDt = "";
    private String status = "";
    private String statusBy = "";
    private String statusDt = "";
    
    private final String YES = "Y";
    private final String NO = "N";
    
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int actionType;
    private int result;
    Date curDate = null;
    
    private ProxyFactory proxy;
    
    private HashMap operationMap = null;
    private HashMap lookUpHash = null;
    private HashMap keyValue;
    
    private ArrayList key;
    private ArrayList value;
    
    private ComboBoxModel cbmCarrierType;
    private ComboBoxModel cbmCountry;
    private ComboBoxModel cbmCity;
    private ComboBoxModel cbmState;
    
    private final static Logger log = Logger.getLogger(CarrierOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private static CarrierOB objCarrierOB; // singleton object
    final CarrierRB objCarrierRB = new CarrierRB();
    
    
    static {
        try {
            log.info("Creating CarrierOB...");
            objCarrierOB = new CarrierOB();
        } catch(Exception e) {
            //_log.error(e);
            parseException.logException(e,true);
        }
    }
    
    /**
     * Returns an instance of CarrierOB.
     * @return  CarrierOB
     */
    public static CarrierOB getInstance() {
        return objCarrierOB;
    }
    
    
    
    /** Creates a new instance of CarrierOB */
    public CarrierOB() throws Exception {
        curDate = ClientUtil.getCurrentDate();
        proxy = ProxyFactory.createProxy();
        setOperationMap();
        fillDropdown();
    }
    
    /* Sets the HashMap required to set JNDI,Home and Remote*/
    private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "CarrierJNDI");
        operationMap.put(CommonConstants.HOME, "com.see.truetransact.serverside.bills.CarrierHome");
        operationMap.put(CommonConstants.REMOTE, "com.see.truetransact.serverside.bills.Carrier");
    }
    /**
     * To Fill ComboBoxes with Values
     */
    public void fillDropdown() throws Exception{
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
        lookup_keys.add("BILLS.CARRIER_TYPE");
        lookup_keys.add("CUSTOMER.CITY");
        lookup_keys.add("CUSTOMER.STATE");
        lookup_keys.add("CUSTOMER.COUNTRY");
        
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("BILLS.CARRIER_TYPE"));
        cbmCarrierType = new ComboBoxModel(key,value);
        
//        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("CUSTOMER.CITY"));
        cbmCity = new ComboBoxModel(key,value);
        
//        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("CUSTOMER.STATE"));
        cbmState = new ComboBoxModel(key,value);
        
//        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("CUSTOMER.COUNTRY"));
        cbmCountry = new ComboBoxModel(key,value);
        
        makeComboBoxKeyValuesNull();
    }
    
    /**
     * @returns the key and value needed for combobox
     */
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
    
    void setCbmCarrierType(ComboBoxModel cbmCarrierType){
        this.cbmCarrierType = cbmCarrierType;
        setChanged();
    }
    
    ComboBoxModel getCbmCarrierType(){
        return this.cbmCarrierType;
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
    
    /**
     * To update the Status based on result performed by doAction() method
     */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
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
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    // Setter method for txtCarrierCode
    void setTxtCarrierCode(String txtCarrierCode){
        this.txtCarrierCode = txtCarrierCode;
        setChanged();
    }
    // Getter method for txtCarrierCode
    String getTxtCarrierCode(){
        return this.txtCarrierCode;
    }
    
    // Setter method for txtCarrierName
    void setTxtCarrierName(String txtCarrierName){
        this.txtCarrierName = txtCarrierName;
        setChanged();
    }
    // Getter method for txtCarrierName
    String getTxtCarrierName(){
        return this.txtCarrierName;
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
    // Setter method for txtPincode
    void setTxtPincode(String txtPincode){
        this.txtPincode = txtPincode;
        setChanged();
    }
    // Getter method for txtPincode
    String getTxtPincode(){
        return this.txtPincode;
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
    // Setter method for cboCarrierType
    void setCboCarrierType(String cboCarrierType){
        this.cboCarrierType = cboCarrierType;
        setChanged();
    }
    // Getter method for cboCarrierType
    String getCboCarrierType(){
        return this.cboCarrierType;
    }
    // Setter method for status
    void setCarrierStatus(String status){
        this.status = status;
        setChanged();
    }
    // Getter method for status
    String getCarrierStatus(){
        return this.status;
    }
    // Setter method for statusBy
//    void setStatusBy(String statusBy){
//        this.statusBy = statusBy;
//        setChanged();
//    }
//    // Getter method for statusBy
//    String getStatusBy(){
//        return this.statusBy;
//    }
    // Setter method for statusDt
    void setStatusDt(String statusDt){
        this.statusDt = statusDt;
        setChanged();
    }
    // Getter method for statusDt
    String getStatusDt(){
        return this.statusDt;
    }
    // Setter method for authorizeStatus
//    void setAuthorizeStatus(String authorizeStatus){
//        this.authorizeStatus = authorizeStatus;
//        setChanged();
//    }
//    // Getter method for authorizeStatus
//    String getAuthorizeStatus(){
//        return this.authorizeStatus;
//    }
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
    // Setter method for chkIsApproved
    void setChkIsApproved(boolean chkIsApproved){
        this.chkIsApproved = chkIsApproved;
        setChanged();
    }
    // Getter method for chkIsApproved
    boolean getChkIsApproved(){
        return this.chkIsApproved;
    }
    /**
     * reset the fields
     */
    public void resetForm() {
        setTxtCarrierCode("");
        setTxtCarrierName("");
        setTxtAddress("");
        setTxtPincode("");
        setCboCity("");
        setCboCountry("");
        setCboState("");
        setCboCarrierType("");
        setCarrierStatus("");
        setStatusBy("");
        setStatusDt("");
        setAuthorizeStatus("");
        setAuthorizeBy("");
        setAuthorizeDt("");
        setChkIsApproved(false);
        ttNotifyObservers();
    }
    /**
     * To set the data in OtherBankTO TO
     */
    public CarrierTO setCarrierTO() {
        log.info("In setCarrierTO...");
        final CarrierTO objCarrierTO = new CarrierTO();
        
        try{
            objCarrierTO.setCarrierCode(CommonUtil.convertObjToStr(getTxtCarrierCode()));
            objCarrierTO.setCarrierName(CommonUtil.convertObjToStr(getTxtCarrierName()));
            objCarrierTO.setAddress(CommonUtil.convertObjToStr(getTxtAddress()));
            objCarrierTO.setCarrierType(CommonUtil.convertObjToStr((String)cbmCarrierType.getKeyForSelected()));
            objCarrierTO.setAddress(CommonUtil.convertObjToStr(getTxtAddress()));
            objCarrierTO.setPincode(CommonUtil.convertObjToStr(getTxtPincode()));
            objCarrierTO.setCity(CommonUtil.convertObjToStr((String)cbmCity.getKeyForSelected()));
            objCarrierTO.setState(CommonUtil.convertObjToStr((String)cbmState.getKeyForSelected()));
            objCarrierTO.setCountry(CommonUtil.convertObjToStr((String)cbmCountry.getKeyForSelected()));
            objCarrierTO.setApproved(getChkIsApproved()?YES: NO);
            objCarrierTO.setStatus(CommonUtil.convertObjToStr(getCarrierStatus()));
            objCarrierTO.setStatusBy(CommonUtil.convertObjToStr(getStatusBy()));
            Date StDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getStatusDt()));
            Date stDate = (Date) curDate.clone();
            stDate.setDate(StDt.getDate());
            stDate.setMonth(StDt.getMonth());
            stDate.setYear(StDt.getYear());
//            objCarrierTO.setStatusDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getStatusDt())));
            objCarrierTO.setStatusDt(stDate);
            objCarrierTO.setAuthorizeBy(CommonUtil.convertObjToStr(getAuthorizeBy()));
            Date AtDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getAuthorizeDt()));
            Date atDate = (Date) curDate.clone();
            atDate.setDate(AtDt.getDate());
            atDate.setMonth(AtDt.getMonth());
            atDate.setYear(AtDt.getYear());
//            objCarrierTO.setAuthorizeDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getAuthorizeDt())));
            objCarrierTO.setAuthorizeDt(atDate);
            objCarrierTO.setAuthorizeStatus(CommonUtil.convertObjToStr(getAuthorizeStatus()));
        }catch(Exception e){
            log.info("Error in setOtherBankBranchTO()");
            parseException.logException(e,true);
        }
        
        return objCarrierTO;
    }
    /**
     * Set the values in the OB
     */
    private void setCarrierOB(CarrierTO objCarrierTO) throws Exception {
        log.info("Inside setCarrierOB()");
        setChkIsApproved(CommonUtil.convertObjToStr(objCarrierTO.getApproved()).equals(YES) ? true : false);
        setTxtCarrierCode(CommonUtil.convertObjToStr(objCarrierTO.getCarrierCode()));
        setTxtCarrierName(CommonUtil.convertObjToStr(objCarrierTO.getCarrierName()));
        setTxtAddress(CommonUtil.convertObjToStr(objCarrierTO.getAddress()));
        setTxtPincode(CommonUtil.convertObjToStr(objCarrierTO.getPincode()));
        setCboCarrierType((String) getCbmCarrierType().getDataForKey(CommonUtil.convertObjToStr(objCarrierTO.getCarrierType())));
        setCboCity((String) getCbmCity().getDataForKey(CommonUtil.convertObjToStr(objCarrierTO.getCity())));
        setCboState((String) getCbmState().getDataForKey(CommonUtil.convertObjToStr(objCarrierTO.getState())));
        setCboCountry((String) getCbmCountry().getDataForKey(CommonUtil.convertObjToStr(objCarrierTO.getCountry())));
        setCarrierStatus(CommonUtil.convertObjToStr(objCarrierTO.getStatus()));
        setStatusBy(CommonUtil.convertObjToStr(objCarrierTO.getStatusBy()));
        setStatusDt(DateUtil.getStringDate(objCarrierTO.getStatusDt()));
        setAuthorizeStatus(CommonUtil.convertObjToStr(objCarrierTO.getAuthorizeStatus()));
        setAuthorizeBy(CommonUtil.convertObjToStr(objCarrierTO.getAuthorizeBy()));
        setAuthorizeDt(DateUtil.getStringDate(objCarrierTO.getAuthorizeDt()));        
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
                    throw new TTException(objCarrierRB.getString("TOCommandError"));
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
        final CarrierTO objCarrierTO = setCarrierTO();
        final HashMap data = new HashMap();
        data.put("CarrierTO",objCarrierTO);
        data.put("MODE",getCommand());
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        HashMap proxyResultMap = proxy.execute(data,operationMap);
        setResult(actionType);
        actionType = ClientConstants.ACTIONTYPE_CANCEL;
        resetForm();
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
        final CarrierTO objCarrierTO = (CarrierTO) ((List) mapData.get("CarrierTO")).get(0);
        setCarrierOB(objCarrierTO);
        ttNotifyObservers();
    }
    /**
     * To check whether Carrier Code is duplicated or not
     */
    public boolean isCarrierCodeAlreadyExist(String carrierCode){
        boolean exist = false;
        try{
            if (carrierCode != null && carrierCode.length() >0 && !carrierCode.equals("")) {
                HashMap where = new HashMap();
                where.put("CARRIER_CODE", carrierCode );
                List carrierCodeCount = (List) ClientUtil.executeQuery("countCarrierCode", where);
                where = null;
                if ( carrierCodeCount.size() > 0 && carrierCodeCount != null) {
                    String count = CommonUtil.convertObjToStr(((LinkedHashMap) carrierCodeCount.get(0)).get("COUNT"));
                    if ( Integer.parseInt(count) > 0 ) {
                        exist = true;
                    } else {
                        exist = false;
                    }
                    count = null;
                }
                carrierCodeCount = null;
            }
        }
        catch ( Exception e ) {
            parseException.logException(e,true);
        }
        return exist;
    }
}