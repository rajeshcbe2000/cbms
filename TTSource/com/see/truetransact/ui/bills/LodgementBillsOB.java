/* Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LodgementBillsOB.java
 *
 * Created on March 16, 2004, 4:03 PM
 */

package com.see.truetransact.ui.bills;

import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.transferobject.bills.LodgementBillsTO;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.uicomponent.CObservable;

import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author  Lohith R.
 */

public class LodgementBillsOB extends CObservable{
    
    private static LodgementBillsOB objLodgementBillsOB; // singleton object
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private ComboBoxModel cbmTypeOfBill;
    private ComboBoxModel cbmBankDetails;
    private ComboBoxModel cbmDraweeDetailsCity;
    private ComboBoxModel cbmDraweeDetailsState;
    private ComboBoxModel cbmDraweeDetailsCountry;
    private ComboBoxModel cbmDispatchDetailsCity;
    private ComboBoxModel cbmDispatchDetailsState;
    private ComboBoxModel cbmDispatchDetailsCountry;
    private ComboBoxModel cbmProductID;
    
    private int actionType;
    private int result;
    
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private ProxyFactory proxy;
    private HashMap operationMap;
    
    private String cboProductID = "";
    private String cboTypeOfBill = "";
    private String cboBankDetails = "";
    private String txtCustomerID = "";
    private String txtInstrumentDetails = "";
    private String txtDiscount = "";
    private String txtMargin = "";
    private String txtBorrowerNum = "";
    private String txtPSBROtherBanks = "";
    private String txtCommission = "";
    private String txtPostage = "";
    private String txtDraweeDetailsName = "";
    private String txtDraweeDetailsStreet = "";
    private String txtDraweeDetailsPincode = "";
    private String txtDraweeDetailsArea = "";
    private String cboDraweeDetailsCity = "";
    private String cboDraweeDetailsState = "";
    private String cboDraweeDetailsCountry = "";
    private String txtDraweeDetailsOthers = "";
    private String txtDispatchDetailsName = "";
    private String txtDispatchDetailsStreet = "";
    private String txtDispatchDetailsPincode = "";
    private String txtDispatchDetailsArea = "";
    private String cboDispatchDetailsCity = "";
    private String cboDispatchDetailsState = "";
    private String cboDispatchDetailsCountry = "";
    private String txtDispatchDetailsOthers = "";
    private String txtAmountOfBill = "";
    private String lodgementId = "";
    private String lblCustomerAccountNumber = "";
    private String lblAccountHead = "";
    private String lblCustomerName = "";
    
    static {
        try {
            objLodgementBillsOB = new LodgementBillsOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    
    /** Creates a new instance of LodgementBillsOB */
    public LodgementBillsOB()throws Exception {
        proxy = ProxyFactory.createProxy();
        setOperationMap();
        fillDropdown();
    }
    
    
    /* Sets the HashMap required to set JNDI,Home and Remote*/
    private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "LodgementBillsJNDI");
        operationMap.put(CommonConstants.HOME, "bills.LodgementBillsHome");
        operationMap.put(CommonConstants.REMOTE, "bills.LodgementBills");
    }
    
    /** Creates a new instance of RemittancePaymentOB */
    public static LodgementBillsOB getInstance() {
        return objLodgementBillsOB;
    }
    
    /** A method to get the combo box values */
    private void fillDropdown() throws Exception{
        lookUpHash = new HashMap();
        // The data to be show in Combo Box from LOOKUP_MASTER table
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("BILL.TYPE");
        lookup_keys.add("BILL.BANK_DETAILS");
        lookup_keys.add("CUSTOMER.CITY");
        lookup_keys.add("CUSTOMER.STATE");
        lookup_keys.add("CUSTOMER.COUNTRY");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        
        getKeyValue((HashMap)keyValue.get("BILL.TYPE"));
        cbmTypeOfBill = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("BILL.BANK_DETAILS"));
        cbmBankDetails = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("CUSTOMER.CITY"));
        cbmDraweeDetailsCity = new ComboBoxModel(key,value);
        cbmDispatchDetailsCity = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("CUSTOMER.STATE"));
        cbmDraweeDetailsState = new ComboBoxModel(key,value);
        cbmDispatchDetailsState = new ComboBoxModel(key,value);
        
        
        getKeyValue((HashMap)keyValue.get("CUSTOMER.COUNTRY"));
        cbmDraweeDetailsCountry = new ComboBoxModel(key,value);
        cbmDispatchDetailsCountry = new ComboBoxModel(key,value);
        
        /** The data to be show in Combo Box other than LOOKUP_MASTER table  */
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,"Bill_Lodgements.getProdId");
        lookUpHash.put(CommonConstants.PARAMFORQUERY,null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmProductID = new ComboBoxModel(key,value);
        makeNull();
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    private void makeNull(){
        key = null;
        value = null;
    }
    
    
    void setCboProductID(String cboProductID){
        this.cboProductID = cboProductID;
        setChanged();
    }
    String getCboProductID(){
        return this.cboProductID;
    }
    void setCbmProductID(ComboBoxModel cbmProductID){
        this.cbmProductID = cbmProductID;
        setChanged();
    }
    ComboBoxModel getCbmProductID(){
        return cbmProductID;
    }
    
    
    void setCboTypeOfBill(String cboTypeOfBill){
        this.cboTypeOfBill = cboTypeOfBill;
        setChanged();
    }
    String getCboTypeOfBill(){
        return this.cboTypeOfBill;
    }
    void setCbmTypeOfBill(ComboBoxModel cbmTypeOfBill){
        this.cbmTypeOfBill = cbmTypeOfBill;
        setChanged();
    }
    ComboBoxModel getCbmTypeOfBill(){
        return cbmTypeOfBill;
    }
    
    
    void setCboBankDetails(String cboBankDetails){
        this.cboBankDetails = cboBankDetails;
        setChanged();
    }
    String getCboBankDetails(){
        return this.cboBankDetails;
    }
    void setCbmBankDetails(ComboBoxModel cbmBankDetails){
        this.cbmBankDetails = cbmBankDetails;
        setChanged();
    }
    ComboBoxModel getCbmBankDetails(){
        return cbmBankDetails;
    }
    
    
    void setTxtCustomerID(String txtCustomerID){
        this.txtCustomerID = txtCustomerID;
        setChanged();
    }
    String getTxtCustomerID(){
        return this.txtCustomerID;
    }
    
    
    void setTxtInstrumentDetails(String txtInstrumentDetails){
        this.txtInstrumentDetails = txtInstrumentDetails;
        setChanged();
    }
    String getTxtInstrumentDetails(){
        return this.txtInstrumentDetails;
    }
    
    
    void setTxtAmountOfBill(String txtAmountOfBill){
        this.txtAmountOfBill = txtAmountOfBill;
        setChanged();
    }
    String getTxtAmountOfBill(){
        return this.txtAmountOfBill;
    }
    
    
    void setTxtDiscount(String txtDiscount){
        this.txtDiscount = txtDiscount;
        setChanged();
    }
    String getTxtDiscount(){
        return this.txtDiscount;
    }
    
    
    void setTxtMargin(String txtMargin){
        this.txtMargin = txtMargin;
        setChanged();
    }
    String getTxtMargin(){
        return this.txtMargin;
    }
    
    
    void setTxtBorrowerNum(String txtBorrowerNum){
        this.txtBorrowerNum = txtBorrowerNum;
        setChanged();
    }
    String getTxtBorrowerNum(){
        return this.txtBorrowerNum;
    }
    
    
    void setTxtPSBROtherBanks(String txtPSBROtherBanks){
        this.txtPSBROtherBanks = txtPSBROtherBanks;
        setChanged();
    }
    String getTxtPSBROtherBanks(){
        return this.txtPSBROtherBanks;
    }
    
    
    void setTxtCommission(String txtCommission){
        this.txtCommission = txtCommission;
        setChanged();
    }
    String getTxtCommission(){
        return this.txtCommission;
    }
    
    
    void setTxtPostage(String txtPostage){
        this.txtPostage = txtPostage;
        setChanged();
    }
    String getTxtPostage(){
        return this.txtPostage;
    }
    
    
    void setTxtDraweeDetailsName(String txtDraweeDetailsName){
        this.txtDraweeDetailsName = txtDraweeDetailsName;
        setChanged();
    }
    String getTxtDraweeDetailsName(){
        return this.txtDraweeDetailsName;
    }
    
    
    void setTxtDraweeDetailsStreet(String txtDraweeDetailsStreet){
        this.txtDraweeDetailsStreet = txtDraweeDetailsStreet;
        setChanged();
    }
    String getTxtDraweeDetailsStreet(){
        return this.txtDraweeDetailsStreet;
    }
    
    
    void setTxtDraweeDetailsPincode(String txtDraweeDetailsPincode){
        this.txtDraweeDetailsPincode = txtDraweeDetailsPincode;
        setChanged();
    }
    String getTxtDraweeDetailsPincode(){
        return this.txtDraweeDetailsPincode;
    }
    
    
    void setTxtDraweeDetailsArea(String txtDraweeDetailsArea){
        this.txtDraweeDetailsArea = txtDraweeDetailsArea;
        setChanged();
    }
    String getTxtDraweeDetailsArea(){
        return this.txtDraweeDetailsArea;
    }
    
    
    void setCboDraweeDetailsCity(String cboDraweeDetailsCity){
        this.cboDraweeDetailsCity = cboDraweeDetailsCity;
        setChanged();
    }
    String getCboDraweeDetailsCity(){
        return this.cboDraweeDetailsCity;
    }
    void setCbmDraweeDetailsCity(ComboBoxModel cbmDraweeDetailsCity){
        this.cbmDraweeDetailsCity = cbmDraweeDetailsCity;
        setChanged();
    }
    ComboBoxModel getCbmDraweeDetailsCity(){
        return cbmDraweeDetailsCity;
    }
    
    
    void setCboDraweeDetailsState(String cboDraweeDetailsState){
        this.cboDraweeDetailsState = cboDraweeDetailsState;
        setChanged();
    }
    String getCboDraweeDetailsState(){
        return this.cboDraweeDetailsState;
    }
    void setCbmDraweeDetailsState(ComboBoxModel cbmDraweeDetailsState){
        this.cbmDraweeDetailsState = cbmDraweeDetailsState;
        setChanged();
    }
    ComboBoxModel getCbmDraweeDetailsState(){
        return cbmDraweeDetailsState;
    }
    
    
    void setCboDraweeDetailsCountry(String cboDraweeDetailsCountry){
        this.cboDraweeDetailsCountry = cboDraweeDetailsCountry;
        setChanged();
    }
    String getCboDraweeDetailsCountry(){
        return this.cboDraweeDetailsCountry;
    }
    void setCbmDraweeDetailsCountry(ComboBoxModel cbmDraweeDetailsCountry){
        this.cbmDraweeDetailsCountry = cbmDraweeDetailsCountry;
        setChanged();
    }
    ComboBoxModel getCbmDraweeDetailsCountry(){
        return cbmDraweeDetailsCountry;
    }
    
    
    void setTxtDraweeDetailsOthers(String txtDraweeDetailsOthers){
        this.txtDraweeDetailsOthers = txtDraweeDetailsOthers;
        setChanged();
    }
    String getTxtDraweeDetailsOthers(){
        return this.txtDraweeDetailsOthers;
    }
    
    
    void setTxtDispatchDetailsName(String txtDispatchDetailsName){
        this.txtDispatchDetailsName = txtDispatchDetailsName;
        setChanged();
    }
    String getTxtDispatchDetailsName(){
        return this.txtDispatchDetailsName;
    }
    
    
    void setTxtDispatchDetailsStreet(String txtDispatchDetailsStreet){
        this.txtDispatchDetailsStreet = txtDispatchDetailsStreet;
        setChanged();
    }
    String getTxtDispatchDetailsStreet(){
        return this.txtDispatchDetailsStreet;
    }
    
    
    void setTxtDispatchDetailsPincode(String txtDispatchDetailsPincode){
        this.txtDispatchDetailsPincode = txtDispatchDetailsPincode;
        setChanged();
    }
    String getTxtDispatchDetailsPincode(){
        return this.txtDispatchDetailsPincode;
    }
    
    
    void setTxtDispatchDetailsArea(String txtDispatchDetailsArea){
        this.txtDispatchDetailsArea = txtDispatchDetailsArea;
        setChanged();
    }
    String getTxtDispatchDetailsArea(){
        return this.txtDispatchDetailsArea;
    }
    
    
    void setCboDispatchDetailsCity(String cboDispatchDetailsCity){
        this.cboDispatchDetailsCity = cboDispatchDetailsCity;
        setChanged();
    }
    String getCboDispatchDetailsCity(){
        return this.cboDispatchDetailsCity;
    }
    void setCbmDispatchDetailsCity(ComboBoxModel cbmDispatchDetailsCity){
        this.cbmDispatchDetailsCity = cbmDispatchDetailsCity;
        setChanged();
    }
    ComboBoxModel getCbmDispatchDetailsCity(){
        return cbmDispatchDetailsCity;
    }
    
    
    void setCboDispatchDetailsState(String cboDispatchDetailsState){
        this.cboDispatchDetailsState = cboDispatchDetailsState;
        setChanged();
    }
    String getCboDispatchDetailsState(){
        return this.cboDispatchDetailsState;
    }
    void setCbmDispatchDetailsState(ComboBoxModel cbmDispatchDetailsState){
        this.cbmDispatchDetailsState = cbmDispatchDetailsState;
        setChanged();
    }
    ComboBoxModel getCbmDispatchDetailsState(){
        return cbmDispatchDetailsState;
    }
    
    
    void setCboDispatchDetailsCountry(String cboDispatchDetailsCountry){
        this.cboDispatchDetailsCountry = cboDispatchDetailsCountry;
        setChanged();
    }
    String getCboDispatchDetailsCountry(){
        return this.cboDispatchDetailsCountry;
    }
    void setCbmDispatchDetailsCountry(ComboBoxModel cbmDispatchDetailsCountry){
        this.cbmDispatchDetailsCountry = cbmDispatchDetailsCountry;
        setChanged();
    }
    ComboBoxModel getCbmDispatchDetailsCountry(){
        return cbmDispatchDetailsCountry;
    }
    
    
    void setTxtDispatchDetailsOthers(String txtDispatchDetailsOthers){
        this.txtDispatchDetailsOthers = txtDispatchDetailsOthers;
        setChanged();
    }
    String getTxtDispatchDetailsOthers(){
        return this.txtDispatchDetailsOthers;
    }
    
    
    void setLodgementId(String lodgementId){
        this.lodgementId = lodgementId;
        setChanged();
    }
    String getLodgementId(){
        return this.lodgementId;
    }
    
    
    void setLblCustomerAccountNumber(String lblCustomerAccountNumber){
        this.lblCustomerAccountNumber = lblCustomerAccountNumber;
        setChanged();
    }
    String getLblCustomerAccountNumber(){
        return this.lblCustomerAccountNumber;
    }
    
    
    void setLblAccountHead(String lblAccountHead){
        this.lblAccountHead = lblAccountHead;
        setChanged();
    }
    String getLblAccountHead(){
        return this.lblAccountHead;
    }
    
    
    void setLblCustomerName(String lblCustomerName){
        this.lblCustomerName = lblCustomerName;
        setChanged();
    }
    String getLblCustomerName(){
        return this.lblCustomerName;
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
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        notifyObservers();
    }
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        notifyObservers();
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
    
    
    /** To perform the appropriate operation */
    public void doAction() {
        try {
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null ){
                    doActionPerform();
                }
                else{
                    final LodgementBillsRB objLodgementBillsRB = new LodgementBillsRB();
                    throw new TTException(objLodgementBillsRB.getString("TOCommandError"));
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
        final LodgementBillsTO objLodgementBillsTO = setLodgementBillsTOData();
        objLodgementBillsTO.setCommand(getCommand());
        final HashMap data = new HashMap();
        data.put("LodgementBillsTO",objLodgementBillsTO);
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());        
        HashMap proxyResultMap = proxy.execute(data, operationMap);
        setResult(actionType);
    }
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    
    /* To set common data in the Transfer Object*/
    public LodgementBillsTO setLodgementBillsTOData() {
        final LodgementBillsTO objLodgementBillsTO = new LodgementBillsTO();
        try{
            objLodgementBillsTO.setLodgementId(lodgementId);
            objLodgementBillsTO.setProdId((String)cbmProductID.getKeyForSelected());
            objLodgementBillsTO.setBillType((String)cbmTypeOfBill.getKeyForSelected());
            objLodgementBillsTO.setBankDetail((String)cbmBankDetails.getKeyForSelected());
            objLodgementBillsTO.setDdCity((String)cbmDraweeDetailsCity.getKeyForSelected());
            objLodgementBillsTO.setDdState((String)cbmDraweeDetailsState.getKeyForSelected());
            objLodgementBillsTO.setDdCountry((String)cbmDraweeDetailsCountry.getKeyForSelected());
            objLodgementBillsTO.setFdCity((String)cbmDispatchDetailsCity.getKeyForSelected());
            objLodgementBillsTO.setFdState((String)cbmDispatchDetailsState.getKeyForSelected());
            objLodgementBillsTO.setFdCountry((String)cbmDispatchDetailsCountry.getKeyForSelected());
            objLodgementBillsTO.setCustId(txtCustomerID);
            objLodgementBillsTO.setInstrumentDetails(txtInstrumentDetails);
            objLodgementBillsTO.setBillAmt(CommonUtil.convertObjToDouble(txtAmountOfBill));
            objLodgementBillsTO.setDiscount(CommonUtil.convertObjToDouble(txtDiscount));
            objLodgementBillsTO.setMargin(CommonUtil.convertObjToDouble(txtMargin));
            objLodgementBillsTO.setBorrowerNo(txtBorrowerNum);
            objLodgementBillsTO.setPSbrOther(txtPSBROtherBanks);
            objLodgementBillsTO.setCommission(CommonUtil.convertObjToDouble(txtCommission));
            objLodgementBillsTO.setPostage(CommonUtil.convertObjToDouble(txtPostage));
            objLodgementBillsTO.setDdDrawee(txtDraweeDetailsOthers);
            objLodgementBillsTO.setDdName(txtDraweeDetailsName);
            objLodgementBillsTO.setDdStreet(txtDraweeDetailsStreet);
            objLodgementBillsTO.setDdArea(txtDraweeDetailsArea);
            objLodgementBillsTO.setDdPincode(txtDraweeDetailsPincode);
            objLodgementBillsTO.setFdDrawee(txtDispatchDetailsOthers);
            objLodgementBillsTO.setFdName(txtDispatchDetailsName);
            objLodgementBillsTO.setFdStreet(txtDispatchDetailsStreet);
            objLodgementBillsTO.setFdArea(txtDispatchDetailsArea);
            objLodgementBillsTO.setFdPincode(txtDispatchDetailsPincode);
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return objLodgementBillsTO;
    }
    
    /* To populate the screen */
    public void populateData(HashMap whereMap) {
        try {
            HashMap mapData = proxy.executeQuery(whereMap, operationMap);
            populateOB(mapData);
        } catch( Exception e ) {
            parseException.logException(e,true);
        }
    }
    
    private void populateOB(HashMap mapData) throws Exception{
        LodgementBillsTO objLodgementBillsTO;
        objLodgementBillsTO = (LodgementBillsTO) ((List) mapData.get("LodgementBillsTO")).get(0);
        setCboProductID(CommonUtil.convertObjToStr(getCbmProductID().getDataForKey(objLodgementBillsTO.getProdId())));
        setCboTypeOfBill(CommonUtil.convertObjToStr(getCbmTypeOfBill().getDataForKey(objLodgementBillsTO.getBillType())));
        setCboBankDetails(CommonUtil.convertObjToStr(getCbmBankDetails().getDataForKey(objLodgementBillsTO.getBankDetail())));
        setCboDraweeDetailsCity(CommonUtil.convertObjToStr(getCbmDraweeDetailsCity().getDataForKey(objLodgementBillsTO.getDdCity())));
        setCboDraweeDetailsState(CommonUtil.convertObjToStr(getCbmDraweeDetailsState().getDataForKey(objLodgementBillsTO.getDdState())));
        setCboDraweeDetailsCountry(CommonUtil.convertObjToStr(getCbmDraweeDetailsCountry().getDataForKey(objLodgementBillsTO.getDdCountry())));
        setCboDispatchDetailsCity(CommonUtil.convertObjToStr(getCbmDispatchDetailsCity().getDataForKey(objLodgementBillsTO.getFdCity())));
        setCboDispatchDetailsState(CommonUtil.convertObjToStr(getCbmDispatchDetailsState().getDataForKey(objLodgementBillsTO.getFdState())));
        setCboDispatchDetailsCountry(CommonUtil.convertObjToStr(getCbmDispatchDetailsCountry().getDataForKey(objLodgementBillsTO.getFdCountry())));
        setTxtCustomerID(objLodgementBillsTO.getCustId());
        setTxtInstrumentDetails(objLodgementBillsTO.getInstrumentDetails());
        setTxtAmountOfBill(objLodgementBillsTO.getBillAmt().toString());
        setTxtDiscount(objLodgementBillsTO.getDiscount().toString());
        setTxtMargin(objLodgementBillsTO.getMargin().toString());
        setTxtBorrowerNum(objLodgementBillsTO.getBorrowerNo());
        setTxtPSBROtherBanks(objLodgementBillsTO.getPSbrOther());
        setTxtCommission(objLodgementBillsTO.getCommission().toString());
        setTxtPostage(objLodgementBillsTO.getPostage().toString());
        setTxtDraweeDetailsOthers(objLodgementBillsTO.getDdDrawee());
        setTxtDraweeDetailsName(objLodgementBillsTO.getDdName());
        setTxtDraweeDetailsStreet(objLodgementBillsTO.getDdStreet());
        setTxtDraweeDetailsArea(objLodgementBillsTO.getDdArea());
        setTxtDraweeDetailsPincode(objLodgementBillsTO.getDdPincode());
        setTxtDispatchDetailsOthers(objLodgementBillsTO.getFdDrawee());
        setTxtDispatchDetailsName(objLodgementBillsTO.getFdName());
        setTxtDispatchDetailsStreet(objLodgementBillsTO.getFdStreet());
        setTxtDispatchDetailsArea(objLodgementBillsTO.getFdArea());
        setTxtDispatchDetailsPincode(objLodgementBillsTO.getFdPincode());
        notifyObservers();
    }
    
    /** To reset the txt fileds to null */
    public void resetForm(){
        setCboProductID("");
        setCboTypeOfBill("");
        setCboBankDetails("");
        setCboDraweeDetailsCity("");
        setCboDraweeDetailsState("");
        setCboDraweeDetailsCountry("");
        setCboDispatchDetailsCity("");
        setCboDispatchDetailsState("");
        setCboDispatchDetailsCountry("");
        setTxtCustomerID("");
        setTxtInstrumentDetails("");
        setTxtAmountOfBill("");
        setTxtDiscount("");
        setTxtMargin("");
        setTxtBorrowerNum("");
        setTxtPSBROtherBanks("");
        setTxtCommission("");
        setTxtPostage("");
        setTxtDraweeDetailsOthers("");
        setTxtDraweeDetailsName("");
        setTxtDraweeDetailsStreet("");
        setTxtDraweeDetailsArea("");
        setTxtDraweeDetailsPincode("");
        setTxtDispatchDetailsOthers("");
        setTxtDispatchDetailsName("");
        setTxtDispatchDetailsStreet("");
        setTxtDispatchDetailsArea("");
        setTxtDispatchDetailsPincode("");
        notifyObservers();
    }
}