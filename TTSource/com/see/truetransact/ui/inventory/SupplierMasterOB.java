/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SupplierMasterOB.java
 *
 * Created on Fri Jun 10 15:40:57 IST 2011
 */

package com.see.truetransact.ui.inventory;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.transferobject.inventory.SupplierMasterTO;

import org.apache.log4j.Logger;

/**
 *
 * @author
 */

public class SupplierMasterOB extends CObservable{
    
    private String txtSupplierName = "";
    private String txtSupplierID = "";
    private String txtStreet = "";
    private String txtArea = "";
    private String cboCity = "";
    private String cboState = "";
    private String txtPincode = "";
    private String cboCountry = "";
    private String txtTinNo = "";
    private String txtCST = "";
    private String txtAddrRemarks = "";
    
    private ComboBoxModel cbmCity;
    private ComboBoxModel cbmState;
    private ComboBoxModel cbmCountry;
    
    private static SupplierMasterOB objSupplierMasterOB;
    
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger _log = Logger.getLogger(SupplierMasterOB.class);
    
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int _result;
    private int _actionType;
    private int result;
    
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;

    private HashMap map;
    private ProxyFactory proxy = null;
    
    private Date currDt = null;
    
    /* Creates a new instance of BillsOB */
    private SupplierMasterOB() {
        currDt = ClientUtil.getCurrentDate();
        map = new HashMap();
        map.put(CommonConstants.JNDI, "SupplierMasterJNDI");
        map.put(CommonConstants.HOME, "serverside.inventory.SupplierMasterHome");
        map.put(CommonConstants.REMOTE, "serverside.inventory.SupplierMaster");
        try {
            proxy = ProxyFactory.createProxy();
            fillDropdown();
//            setBillsChargesTab();
//            tblBillsCharges=new EnhancedTableModel(null,billsChargeTabTitle);
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    static {
        try {
            _log.info("Creating BillsOB...");
            objSupplierMasterOB = new SupplierMasterOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /**
     * Returns an instance of SupplierMasterOB.
     * @return  SupplierMasterOB
     */
    
    public static SupplierMasterOB getInstance()throws Exception{
        return objSupplierMasterOB;
    }
    
    /* Filling up the the ComboBox in the UI*/
    private void fillDropdown() throws Exception{
        try{
            _log.info("Inside FillDropDown");
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("CUSTOMER.CITY");
            lookup_keys.add("CUSTOMER.STATE");
            lookup_keys.add("CUSTOMER.COUNTRY");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            
            getKeyValue((HashMap)keyValue.get("CUSTOMER.CITY"));
            cbmCity = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("CUSTOMER.STATE"));
            cbmState = new ComboBoxModel(key,value);

            getKeyValue((HashMap)keyValue.get("CUSTOMER.COUNTRY"));
            cbmCountry = new ComboBoxModel(key,value);

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
    
    /* Executes Query using the TO object */
    public void doAction() {
        try {
            HashMap dataMap = new HashMap();
            dataMap.put(CommonConstants.MODULE, getModule());
            dataMap.put(CommonConstants.SCREEN, getScreen());
            dataMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            dataMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            String command = getCommand();
            dataMap.put("SupplierMasterTO", getSupplierMasterTO(command));
            HashMap proxyResultMap = proxy.execute(dataMap, map);
            setResult(getActionType());
            dataMap = null;
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    /* To get the type of command */
    private String getCommand() throws Exception{
        String command = null;
        switch (getActionType()) {
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
    
    private SupplierMasterTO getSupplierMasterTO (String command) {
        SupplierMasterTO objSupplierMasterTO = new SupplierMasterTO();
        objSupplierMasterTO.setCommand (command);
        objSupplierMasterTO.setSupplierid (getTxtSupplierID());
        objSupplierMasterTO.setSuppliername (getTxtSupplierName());
        objSupplierMasterTO.setStreet (getTxtStreet());
        objSupplierMasterTO.setArea (getTxtArea());
        objSupplierMasterTO.setCity (CommonUtil.convertObjToStr(getCbmCity().getKeyForSelected()));
        objSupplierMasterTO.setState (CommonUtil.convertObjToStr(getCbmState().getKeyForSelected()));
        objSupplierMasterTO.setPincode (getTxtPincode());
        objSupplierMasterTO.setCountry (CommonUtil.convertObjToStr(getCbmCountry().getKeyForSelected()));
        objSupplierMasterTO.setTinno (getTxtTinNo());
        objSupplierMasterTO.setCst (getTxtCST());
        objSupplierMasterTO.setAddrremarks (getTxtAddrRemarks());
        objSupplierMasterTO.setBranchCode (ProxyParameters.BRANCH_ID);
        return objSupplierMasterTO;
    }
    
    private void setSupplierMasterTO(SupplierMasterTO objSupplierMasterTO) {
        setTxtSupplierID(objSupplierMasterTO.getSupplierid ());
        setTxtSupplierName (objSupplierMasterTO.getSuppliername ());
        setTxtStreet (objSupplierMasterTO.getStreet ());
        setTxtArea (objSupplierMasterTO.getArea ());
        setCboCity (CommonUtil.convertObjToStr(getCbmCountry().getDataForKey(objSupplierMasterTO.getCity ())));
        setCboState (objSupplierMasterTO.getState ());
        setTxtPincode (CommonUtil.convertObjToStr(getCbmCountry().getDataForKey(objSupplierMasterTO.getPincode ())));
        setCboCountry (CommonUtil.convertObjToStr(getCbmCountry().getDataForKey(objSupplierMasterTO.getCountry ())));
        setTxtTinNo (objSupplierMasterTO.getTinno ());
        setTxtCST (objSupplierMasterTO.getCst ());
        setTxtAddrRemarks (objSupplierMasterTO.getAddrremarks ());
    }
    
    // Setter method for txtSupplierName
    void setTxtSupplierName(String txtSupplierName){
        this.txtSupplierName = txtSupplierName;
        setChanged();
    }
    // Getter method for txtSupplierName
    String getTxtSupplierName(){
        return this.txtSupplierName;
    }
    
    // Setter method for txtSupplierID
    void setTxtSupplierID(String txtSupplierID){
        this.txtSupplierID = txtSupplierID;
        setChanged();
    }
    // Getter method for txtSupplierID
    String getTxtSupplierID(){
        return this.txtSupplierID;
    }
    
    // Setter method for txtStreet
    void setTxtStreet(String txtStreet){
        this.txtStreet = txtStreet;
        setChanged();
    }
    // Getter method for txtStreet
    String getTxtStreet(){
        return this.txtStreet;
    }
    
    // Setter method for txtArea
    void setTxtArea(String txtArea){
        this.txtArea = txtArea;
        setChanged();
    }
    // Getter method for txtArea
    String getTxtArea(){
        return this.txtArea;
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
    
    // Setter method for cboState
    void setCboState(String cboState){
        this.cboState = cboState;
        setChanged();
    }
    // Getter method for cboState
    String getCboState(){
        return this.cboState;
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
    
    // Setter method for txtTinNo
    void setTxtTinNo(String txtTinNo){
        this.txtTinNo = txtTinNo;
        setChanged();
    }
    // Getter method for txtTinNo
    String getTxtTinNo(){
        return this.txtTinNo;
    }
    
    // Setter method for txtCST
    void setTxtCST(String txtCST){
        this.txtCST = txtCST;
        setChanged();
    }
    // Getter method for txtCST
    String getTxtCST(){
        return this.txtCST;
    }
    
    // Setter method for txtAddrRemarks
    void setTxtAddrRemarks(String txtAddrRemarks){
        this.txtAddrRemarks = txtAddrRemarks;
        setChanged();
    }
    // Getter method for txtAddrRemarks
    String getTxtAddrRemarks(){
        return this.txtAddrRemarks;
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
    
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    
    /* Resetting all the Fields in the UI */
    public void resetForm(){
        setTxtSupplierID("");
        setTxtSupplierName("");
        setTxtStreet("");
        setTxtArea("");
        setCboCity("");
        setCboState("");
        setTxtPincode("");
        setCboCountry("");
        setTxtTinNo("");
        setTxtCST("");
        setTxtAddrRemarks("");
        ttNotifyObservers();
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    /**
     * Getter for property cbmCity.
     * @return Value of property cbmCity.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCity() {
        return cbmCity;
    }    
    
    /**
     * Setter for property cbmCity.
     * @param cbmCity New value of property cbmCity.
     */
    public void setCbmCity(com.see.truetransact.clientutil.ComboBoxModel cbmCity) {
        this.cbmCity = cbmCity;
    }
    
    /**
     * Getter for property cbmState.
     * @return Value of property cbmState.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmState() {
        return cbmState;
    }
    
    /**
     * Setter for property cbmState.
     * @param cbmState New value of property cbmState.
     */
    public void setCbmState(com.see.truetransact.clientutil.ComboBoxModel cbmState) {
        this.cbmState = cbmState;
    }
    
    /**
     * Getter for property cbmCountry.
     * @return Value of property cbmCountry.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCountry() {
        return cbmCountry;
    }
    
    /**
     * Setter for property cbmCountry.
     * @param cbmCountry New value of property cbmCountry.
     */
    public void setCbmCountry(com.see.truetransact.clientutil.ComboBoxModel cbmCountry) {
        this.cbmCountry = cbmCountry;
    }
    
}