/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PaddyLocalityMasterOB.java
 *
 * Created on Fri Jun 10 15:40:57 IST 2011
 */

package com.see.truetransact.ui.paddyprocurement;

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
import com.see.truetransact.transferobject.paddyprocurement.PaddyLocalityMasterTO;

import org.apache.log4j.Logger;

/**
 *
 * @author
 */

public class PaddyLocalityMasterOB extends CObservable{
    
    private String txtLocalityCode = "";
    private String txtLocalityName = "";
    private String txtStreet = "";
    private String txtArea = "";
    private String cboCity = "";
    private String cboState = "";
    private String txtPincode = "";
    private String cboCountry = "";
    HashMap data = new HashMap();
    private ComboBoxModel cbmCity;
    private ComboBoxModel cbmState;
    private ComboBoxModel cbmCountry;
    private int actionType=0;
    private int result=0;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private static PaddyLocalityMasterOB objPaddyLocalityMasterOB;
    
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger _log = Logger.getLogger(PaddyLocalityMasterOB.class);
    private HashMap operationMap;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;

    private HashMap map;
    private ProxyFactory proxy = null;
    
    private Date currDt = null;
    
    /* Creates a new instance of BillsOB */
    private PaddyLocalityMasterOB() {
        currDt = ClientUtil.getCurrentDate();
        map = new HashMap();
        map.put(CommonConstants.JNDI, "PaddyLocalityMasterJNDI");
        map.put(CommonConstants.HOME, "serverside.paddyprocurement.PaddyLocalityMasterHome");
        map.put(CommonConstants.REMOTE, "serverside.paddyprocurement.PaddyLocalityMaster");
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
            objPaddyLocalityMasterOB = new PaddyLocalityMasterOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /**
     * Returns an instance of PaddyLocalityMasterOB.
     * @return  PaddyLocalityMasterOB
     */
    
    public static PaddyLocalityMasterOB getInstance()throws Exception{
        return objPaddyLocalityMasterOB;
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
            dataMap.put("PaddyLocalityMasterTO", getPaddyLocalityMasterTO(command));
            System.out.println("@#$@#$dataMap:"+dataMap);
            HashMap proxyResultMap = proxy.execute(dataMap, map);
            dataMap = null;
            setResult(actionType);
            actionType = ClientConstants.ACTIONTYPE_CANCEL;
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
    public void authorizeLocationMaster(HashMap singleAuthorizeMap) {
        try{
            singleAuthorizeMap.put("AUTH_DATA","AUTH_DATA");
            proxy.executeQuery(singleAuthorizeMap,map);
        }
        catch(Exception e){
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    private PaddyLocalityMasterTO getPaddyLocalityMasterTO (String command) {
        PaddyLocalityMasterTO objPaddyLocalityMasterTO = new PaddyLocalityMasterTO();
        objPaddyLocalityMasterTO.setCommand (command);
        objPaddyLocalityMasterTO.setTxtLocalityCode(getTxtLocalityCode());
        objPaddyLocalityMasterTO.setTxtLocalityName(getTxtLocalityName());
        objPaddyLocalityMasterTO.setTxtStreet(getTxtStreet());
        objPaddyLocalityMasterTO.setTxtArea(getTxtArea());
        objPaddyLocalityMasterTO.setCboCity(CommonUtil.convertObjToStr(getCbmCity().getKeyForSelected()));
        objPaddyLocalityMasterTO.setCboState(CommonUtil.convertObjToStr(getCbmState().getKeyForSelected()));
        objPaddyLocalityMasterTO.setTxtPincode(getTxtPincode());
        objPaddyLocalityMasterTO.setCboCountry(CommonUtil.convertObjToStr(getCbmCountry().getKeyForSelected()));
        objPaddyLocalityMasterTO.setStatusBy(TrueTransactMain.USER_ID);
        objPaddyLocalityMasterTO.setStatusDt(currDt);
        objPaddyLocalityMasterTO.setStatusBy(TrueTransactMain.USER_ID);
        objPaddyLocalityMasterTO.setBranchCode (ProxyParameters.BRANCH_ID);
        System.out.println("@#$@#$@#objPaddyLocalityMasterTO"+objPaddyLocalityMasterTO);
        return objPaddyLocalityMasterTO;
    }
    
    private void setPaddyLocalityMasterTO(PaddyLocalityMasterTO objPaddyLocalityMasterTO) {
        setTxtLocalityCode(objPaddyLocalityMasterTO.getTxtLocalityCode());
        setTxtLocalityName(objPaddyLocalityMasterTO.getTxtLocalityName());
        setTxtStreet(objPaddyLocalityMasterTO.getTxtStreet());
        setTxtArea(objPaddyLocalityMasterTO.getTxtArea());
        setCboCity(CommonUtil.convertObjToStr(getCbmCity().getDataForKey(objPaddyLocalityMasterTO.getCboCity())));
        setTxtPincode (objPaddyLocalityMasterTO.getTxtPincode());
        setCboState(CommonUtil.convertObjToStr(getCbmState().getDataForKey(objPaddyLocalityMasterTO.getCboState())));
        setCboCountry(CommonUtil.convertObjToStr(getCbmCountry().getDataForKey(objPaddyLocalityMasterTO.getCboCountry())));
        
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
   
    public void  getData(HashMap whereMap){
        try{
            data = (HashMap)proxy.executeQuery(whereMap,map);
            System.out.println("@#$@#$@#$@#data : "+data+ " : "+whereMap+ " : "+map);
            if(data.containsKey("PaddyLocalityMasterTO")){
                PaddyLocalityMasterTO objPaddyLocalityMasterTO = (PaddyLocalityMasterTO)data.get("PaddyLocalityMasterTO");
                System.out.println("@#$@#PaddyLocalityMasterTO:"+objPaddyLocalityMasterTO);
                setPaddyLocalityMasterTO(objPaddyLocalityMasterTO);
                ttNotifyObservers();
            }
        }
        catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
  
    /* Resetting all the Fields in the UI */
    public void resetForm(){
        setTxtLocalityCode("");
        setTxtLocalityName("");
        setTxtStreet("");
        setTxtArea("");
        setCboCity("");
        setCboState("");
        setTxtPincode("");
        setCboCountry("");
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
    
    /**
     * Getter for property txtLocalityCode.
     * @return Value of property txtLocalityCode.
     */
    public java.lang.String getTxtLocalityCode() {
        return txtLocalityCode;
    }
    
    /**
     * Setter for property txtLocalityCode.
     * @param txtLocalityCode New value of property txtLocalityCode.
     */
    public void setTxtLocalityCode(java.lang.String txtLocalityCode) {
        this.txtLocalityCode = txtLocalityCode;
    }
    
    /**
     * Getter for property txtLocalityName.
     * @return Value of property txtLocalityName.
     */
    public java.lang.String getTxtLocalityName() {
        return txtLocalityName;
    }
    
    /**
     * Setter for property txtLocalityName.
     * @param txtLocalityName New value of property txtLocalityName.
     */
    public void setTxtLocalityName(java.lang.String txtLocalityName) {
        this.txtLocalityName = txtLocalityName;
    }
    
    /**
     * Getter for property actionType.
     * @return Value of property actionType.
     */
    public int getActionType() {
        return actionType;
    }
    
    /**
     * Setter for property actionType.
     * @param actionType New value of property actionType.
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }
    
    /**
     * Getter for property lblStatus.
     * @return Value of property lblStatus.
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }
    
    /**
     * Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }
     public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
        setChanged();
        
    }
     
     /**
      * Getter for property result.
      * @return Value of property result.
      */
     public int getResult() {
         return result;
     }
     
     /**
      * Setter for property result.
      * @param result New value of property result.
      */
     public void setResult(int result) {
         this.result = result;
     }
         /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }
     
}