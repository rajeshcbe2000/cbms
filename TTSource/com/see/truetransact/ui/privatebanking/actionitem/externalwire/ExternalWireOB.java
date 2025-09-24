/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ExternalWireOB.java
 *
 * Created on July 2, 2004, 11:43 AM
 */

package com.see.truetransact.ui.privatebanking.actionitem.externalwire;

/**
 *
 * @author Ashok
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
import com.see.truetransact.transferobject.privatebanking.actionitem.externalwire.ExternalWireTO;
import com.see.truetransact.clientutil.ComboBoxModel;
import org.apache.log4j.Logger;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;

public class ExternalWireOB extends CObservable{
    
    private String lblReferenceNumber = "";
    private String txtMember = "";
    private String txtDebitEntitlementGroup = "";
    private String txtDebitPortfolioLocation = "";
    private String txtDebitAssetSubClass = "";
    private String txtDebitAccount = "";
    private String cboCurrency = "";
    private String tdtExecutionDate = "";
    private String tdtSettlementDate = "";
    private String txtDebitAmount = "";
    private String txtCreditAmount = "";
    private String cboChargesPaidBy = "";
    private boolean rdoStandardCharges_Yes = false;
    private boolean rdoStandardCharges_No = false;
    private String txtChargesAmount = "";
    private String cboChargesCcy = "";
    private String txtByOrderOf = "";
    private String txtSwiftCode = "";
    private String txtRoutingCode = "";
    private String txtBenificiaryName = "";
    private String txtBenificiartAcNo = "";
    private String txtBenificiaryBank = "";
    private String txtCorrespondentBank = "";
    private String txtBenPin = "";
    private String txtPaymentDetails = "";
    private String txtCorPin = "";
    private String cboBenBankCountry = "";
    private String cboBenBankCity = "";
    private String cboCorBankCity = "";
    private String cboBenBankState = "";
    private String cboCorBankState = "";
    private String cboCorBankCountry = "";
    private String txtBankOfficeInstruction = "";
    private String txtTraderDealerInst = "";
    private String txtCreditNotes = "";
    private String txtClientAdvices = "";
    private ProxyFactory proxy;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger _log = Logger.getLogger(ExternalWireOB.class);
    private static ExternalWireOB objExternalWireOB;
    private int _actionType, _result;
    private ComboBoxModel cbmCurrency,cbmChargesCcy,cbmBenBankCity,cbmBenBankState,cbmBenBankCountry;
    private ComboBoxModel cbmCorBankCity,cbmCorBankCountry,cbmCorBankState,cbmChargesPaidBy;
    private HashMap lookUpHash;
    private HashMap keyValue,map;
    private ArrayList key;
    private ArrayList value;
    final String YES = "Y";
    final String NO = "N";
    Date curDate = null;
    
    /** Creates a new instance of ExternalWireOB */
    private ExternalWireOB() {
        curDate = ClientUtil.getCurrentDate();
        map = new HashMap();
        map.put(CommonConstants.JNDI, "ExternalWireJNDI");
        map.put(CommonConstants.HOME, "serverside.privatebanking.actionitem.externalWire.ExternalWireHome");
        map.put(CommonConstants.REMOTE, "serverside.privatebanking.actionitem.externalWire.ExternalWire");
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
            objExternalWireOB= new ExternalWireOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /* Initialising the Combobox Model*/
    public void initUIComboBoxModel(){
        cbmChargesPaidBy = new ComboBoxModel();
        cbmCurrency = new ComboBoxModel();
        cbmChargesCcy = new ComboBoxModel();
        cbmBenBankCity = new ComboBoxModel();
        cbmBenBankCountry = new ComboBoxModel();
        cbmBenBankState = new ComboBoxModel();
        cbmCorBankCountry = new ComboBoxModel();
        cbmCorBankCity = new ComboBoxModel();
        cbmCorBankState = new ComboBoxModel();
    }
    
    /* Filling up the the ComboBox in the UI*/
    private void fillDropdown() throws Exception{
        try{
            _log.info("Inside FillDropDown");
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("PVT.CHRG_PAID_BY");
            lookup_keys.add("FOREX.CURRENCY");
            lookup_keys.add("CUSTOMER.CITY");
            lookup_keys.add("CUSTOMER.COUNTRY");
            lookup_keys.add("CUSTOMER.STATE");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get("PVT.CHRG_PAID_BY"));
            cbmChargesPaidBy = new ComboBoxModel(key,value);
            getKeyValue((HashMap) keyValue.get("FOREX.CURRENCY"));
            cbmCurrency = new ComboBoxModel(key,value);
            cbmChargesCcy = new ComboBoxModel(key,value);
            getKeyValue((HashMap) keyValue.get("CUSTOMER.CITY"));
            cbmBenBankCity = new ComboBoxModel(key,value);
            cbmCorBankCity = new ComboBoxModel(key,value);
            getKeyValue((HashMap) keyValue.get("CUSTOMER.COUNTRY"));
            cbmBenBankCountry = new ComboBoxModel(key,value);
            cbmCorBankCountry = new ComboBoxModel(key,value);
            getKeyValue((HashMap) keyValue.get("CUSTOMER.STATE"));
            cbmBenBankState = new ComboBoxModel(key,value);
            cbmCorBankState = new ComboBoxModel(key,value);
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
    
    /** This method Returns an instance of this class **/
    public static ExternalWireOB getInstance()throws Exception{
        return objExternalWireOB;
    }
    
    /**
     * Getter for property lblReferenceNumber.
     * @return Value of property lblReferenceNumber.
     */
    public java.lang.String getLblReferenceNumber() {
        return lblReferenceNumber;
    }
    
    /**
     * Setter for property lblReferenceNumber.
     * @param lblReferenceNumber New value of property lblReferenceNumber.
     */
    public void setLblReferenceNumber(java.lang.String lblReferenceNumber) {
        this.lblReferenceNumber = lblReferenceNumber;
        setChanged();
    }
    
    // Setter method for txtMember
    void setTxtMember(String txtMember){
        this.txtMember = txtMember;
        setChanged();
    }
    // Getter method for txtMember
    String getTxtMember(){
        return this.txtMember;
    }
    
    // Setter method for txtDebitEntitlementGroup
    void setTxtDebitEntitlementGroup(String txtDebitEntitlementGroup){
        this.txtDebitEntitlementGroup = txtDebitEntitlementGroup;
        setChanged();
    }
    // Getter method for txtDebitEntitlementGroup
    String getTxtDebitEntitlementGroup(){
        return this.txtDebitEntitlementGroup;
    }
    
    // Setter method for txtDebitPortfolioLocation
    void setTxtDebitPortfolioLocation(String txtDebitPortfolioLocation){
        this.txtDebitPortfolioLocation = txtDebitPortfolioLocation;
        setChanged();
    }
    // Getter method for txtDebitPortfolioLocation
    String getTxtDebitPortfolioLocation(){
        return this.txtDebitPortfolioLocation;
    }
    
    // Setter method for txtDebitAssetSubClass
    void setTxtDebitAssetSubClass(String txtDebitAssetSubClass){
        this.txtDebitAssetSubClass = txtDebitAssetSubClass;
        setChanged();
    }
    // Getter method for txtDebitAssetSubClass
    String getTxtDebitAssetSubClass(){
        return this.txtDebitAssetSubClass;
    }
    
    // Setter method for txtDebitAccount
    void setTxtDebitAccount(String txtDebitAccount){
        this.txtDebitAccount = txtDebitAccount;
        setChanged();
    }
    // Getter method for txtDebitAccount
    String getTxtDebitAccount(){
        return this.txtDebitAccount;
    }
    
    // Setter method for cboCurrency
    void setCboCurrency(String cboCurrency){
        this.cboCurrency = cboCurrency;
        setChanged();
    }
    // Getter method for cboCurrency
    String getCboCurrency(){
        return this.cboCurrency;
    }
    
    /**
     * Getter for property cbmCurrency.
     * @return Value of property cbmCurrency.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCurrency() {
        return cbmCurrency;
    }
    
    /**
     * Setter for property cbmCurrency.
     * @param cbmCurrency New value of property cbmCurrency.
     */
    public void setCbmCurrency(com.see.truetransact.clientutil.ComboBoxModel cbmCurrency) {
        this.cbmCurrency = cbmCurrency;
    }
    
    
    // Setter method for tdtExecutionDate
    void setTdtExecutionDate(String tdtExecutionDate){
        this.tdtExecutionDate = tdtExecutionDate;
        setChanged();
    }
    // Getter method for tdtExecutionDate
    String getTdtExecutionDate(){
        return this.tdtExecutionDate;
    }
    
    // Setter method for tdtSettlementDate
    void setTdtSettlementDate(String tdtSettlementDate){
        this.tdtSettlementDate = tdtSettlementDate;
        setChanged();
    }
    // Getter method for tdtSettlementDate
    String getTdtSettlementDate(){
        return this.tdtSettlementDate;
    }
    
    // Setter method for txtDebitAmount
    void setTxtDebitAmount(String txtDebitAmount){
        this.txtDebitAmount = txtDebitAmount;
        setChanged();
    }
    // Getter method for txtDebitAmount
    String getTxtDebitAmount(){
        return this.txtDebitAmount;
    }
    
    // Setter method for txtCreditAmount
    void setTxtCreditAmount(String txtCreditAmount){
        this.txtCreditAmount = txtCreditAmount;
        setChanged();
    }
    // Getter method for txtCreditAmount
    String getTxtCreditAmount(){
        return this.txtCreditAmount;
    }
    
    // Setter method for cboChargesPaidBy
    void setCboChargesPaidBy(String cboChargesPaidBy){
        this.cboChargesPaidBy = cboChargesPaidBy;
        setChanged();
    }
    // Getter method for cboChargesPaidBy
    String getCboChargesPaidBy(){
        return this.cboChargesPaidBy;
    }
    
    /**
     * Getter for property cbmChargesPaidBy.
     * @return Value of property cbmChargesPaidBy.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmChargesPaidBy() {
        return cbmChargesPaidBy;
    }
    
    /**
     * Setter for property cbmChargesPaidBy.
     * @param cbmChargesPaidBy New value of property cbmChargesPaidBy.
     */
    public void setCbmChargesPaidBy(com.see.truetransact.clientutil.ComboBoxModel cbmChargesPaidBy) {
        this.cbmChargesPaidBy = cbmChargesPaidBy;
    }
    
    
    // Setter method for rdoStandardCharges_Yes
    void setRdoStandardCharges_Yes(boolean rdoStandardCharges_Yes){
        this.rdoStandardCharges_Yes = rdoStandardCharges_Yes;
        setChanged();
    }
    // Getter method for rdoStandardCharges_Yes
    boolean getRdoStandardCharges_Yes(){
        return this.rdoStandardCharges_Yes;
    }
    
    // Setter method for rdoStandardCharges_No
    void setRdoStandardCharges_No(boolean rdoStandardCharges_No){
        this.rdoStandardCharges_No = rdoStandardCharges_No;
        setChanged();
    }
    // Getter method for rdoStandardCharges_No
    boolean getRdoStandardCharges_No(){
        return this.rdoStandardCharges_No;
    }
    
    // Setter method for txtChargesAmount
    void setTxtChargesAmount(String txtChargesAmount){
        this.txtChargesAmount = txtChargesAmount;
        setChanged();
    }
    // Getter method for txtChargesAmount
    String getTxtChargesAmount(){
        return this.txtChargesAmount;
    }
    
    // Setter method for cboChargesCcy
    void setCboChargesCcy(String cboChargesCcy){
        this.cboChargesCcy = cboChargesCcy;
        setChanged();
    }
    // Getter method for cboChargesCcy
    String getCboChargesCcy(){
        return this.cboChargesCcy;
    }
    
    /**
     * Getter for property cbmChargesCcy.
     * @return Value of property cbmChargesCcy.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmChargesCcy() {
        return cbmChargesCcy;
    }
    
    /**
     * Setter for property cbmChargesCcy.
     * @param cbmChargesCcy New value of property cbmChargesCcy.
     */
    public void setCbmChargesCcy(com.see.truetransact.clientutil.ComboBoxModel cbmChargesCcy) {
        this.cbmChargesCcy = cbmChargesCcy;
    }
    
    
    // Setter method for txtByOrderOf
    void setTxtByOrderOf(String txtByOrderOf){
        this.txtByOrderOf = txtByOrderOf;
        setChanged();
    }
    // Getter method for txtByOrderOf
    String getTxtByOrderOf(){
        return this.txtByOrderOf;
    }
    
    // Setter method for txtSwiftCode
    void setTxtSwiftCode(String txtSwiftCode){
        this.txtSwiftCode = txtSwiftCode;
        setChanged();
    }
    // Getter method for txtSwiftCode
    String getTxtSwiftCode(){
        return this.txtSwiftCode;
    }
    
    // Setter method for txtRoutingCode
    void setTxtRoutingCode(String txtRoutingCode){
        this.txtRoutingCode = txtRoutingCode;
        setChanged();
    }
    // Getter method for txtRoutingCode
    String getTxtRoutingCode(){
        return this.txtRoutingCode;
    }
    
    // Setter method for txtBenificiaryName
    void setTxtBenificiaryName(String txtBenificiaryName){
        this.txtBenificiaryName = txtBenificiaryName;
        setChanged();
    }
    // Getter method for txtBenificiaryName
    String getTxtBenificiaryName(){
        return this.txtBenificiaryName;
    }
    
    // Setter method for txtBenificiartAcNo
    void setTxtBenificiaryAcNo(String txtBenificiartAcNo){
        this.txtBenificiartAcNo = txtBenificiartAcNo;
        setChanged();
    }
    // Getter method for txtBenificiartAcNo
    String getTxtBenificiaryAcNo(){
        return this.txtBenificiartAcNo;
    }
    
    // Setter method for txtBenificiaryBank
    void setTxtBenificiaryBank(String txtBenificiaryBank){
        this.txtBenificiaryBank = txtBenificiaryBank;
        setChanged();
    }
    // Getter method for txtBenificiaryBank
    String getTxtBenificiaryBank(){
        return this.txtBenificiaryBank;
    }
    
    // Setter method for txtCorrespondentBank
    void setTxtCorrespondentBank(String txtCorrespondentBank){
        this.txtCorrespondentBank = txtCorrespondentBank;
        setChanged();
    }
    // Getter method for txtCorrespondentBank
    String getTxtCorrespondentBank(){
        return this.txtCorrespondentBank;
    }
    
    // Setter method for txtBenPin
    void setTxtBenPin(String txtBenPin){
        this.txtBenPin = txtBenPin;
        setChanged();
    }
    // Getter method for txtBenPin
    String getTxtBenPin(){
        return this.txtBenPin;
    }
    
    // Setter method for txtPaymentDetails
    void setTxtPaymentDetails(String txtPaymentDetails){
        this.txtPaymentDetails = txtPaymentDetails;
        setChanged();
    }
    // Getter method for txtPaymentDetails
    String getTxtPaymentDetails(){
        return this.txtPaymentDetails;
    }
    
    // Setter method for txtCorPin
    void setTxtCorPin(String txtCorPin){
        this.txtCorPin = txtCorPin;
        setChanged();
    }
    // Getter method for txtCorPin
    String getTxtCorPin(){
        return this.txtCorPin;
    }
    
    // Setter method for cboBenBankCountry
    void setCboBenBankCountry(String cboBenBankCountry){
        this.cboBenBankCountry = cboBenBankCountry;
        setChanged();
    }
    // Getter method for cboBenBankCountry
    String getCboBenBankCountry(){
        return this.cboBenBankCountry;
    }
    
    /**
     * Getter for property cbmBenBankCountry.
     * @return Value of property cbmBenBankCountry.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmBenBankCountry() {
        return cbmBenBankCountry;
    }
    
    /**
     * Setter for property cbmBenBankCountry.
     * @param cbmBenBankCountry New value of property cbmBenBankCountry.
     */
    public void setCbmBenBankCountry(com.see.truetransact.clientutil.ComboBoxModel cbmBenBankCountry) {
        this.cbmBenBankCountry = cbmBenBankCountry;
    }
    
    // Setter method for cboBenBankCity
    void setCboBenBankCity(String cboBenBankCity){
        this.cboBenBankCity = cboBenBankCity;
        setChanged();
    }
    // Getter method for cboBenBankCity
    String getCboBenBankCity(){
        return this.cboBenBankCity;
    }
    
    /**
     * Getter for property cbmBenBankCity.
     * @return Value of property cbmBenBankCity.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmBenBankCity() {
        return cbmBenBankCity;
    }
    
    /**
     * Setter for property cbmBenBankCity.
     * @param cbmBenBankCity New value of property cbmBenBankCity.
     */
    public void setCbmBenBankCity(com.see.truetransact.clientutil.ComboBoxModel cbmBenBankCity) {
        this.cbmBenBankCity = cbmBenBankCity;
    }
    
    
    // Setter method for cboCorBankCity
    void setCboCorBankCity(String cboCorBankCity){
        this.cboCorBankCity = cboCorBankCity;
        setChanged();
    }
    // Getter method for cboCorBankCity
    String getCboCorBankCity(){
        return this.cboCorBankCity;
    }
    
    /**
     * Getter for property cbmCorBankCity.
     * @return Value of property cbmCorBankCity.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCorBankCity() {
        return cbmCorBankCity;
    }
    
    /**
     * Setter for property cbmCorBankCity.
     * @param cbmCorBankCity New value of property cbmCorBankCity.
     */
    public void setCbmCorBankCity(com.see.truetransact.clientutil.ComboBoxModel cbmCorBankCity) {
        this.cbmCorBankCity = cbmCorBankCity;
    }
    
    
    // Setter method for cboBenBankState
    void setCboBenBankState(String cboBenBankState){
        this.cboBenBankState = cboBenBankState;
        setChanged();
    }
    // Getter method for cboBenBankState
    String getCboBenBankState(){
        return this.cboBenBankState;
    }
    
    /**
     * Getter for property cbmBenBankState.
     * @return Value of property cbmBenBankState.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmBenBankState() {
        return cbmBenBankState;
    }
    
    /**
     * Setter for property cbmBenBankState.
     * @param cbmBenBankState New value of property cbmBenBankState.
     */
    public void setCbmBenBankState(com.see.truetransact.clientutil.ComboBoxModel cbmBenBankState) {
        this.cbmBenBankState = cbmBenBankState;
    }
    
    
    // Setter method for cboCorBankState
    void setCboCorBankState(String cboCorBankState){
        this.cboCorBankState = cboCorBankState;
        setChanged();
    }
    // Getter method for cboCorBankState
    String getCboCorBankState(){
        return this.cboCorBankState;
    }
    
    /**
     * Getter for property cbmCorBankState.
     * @return Value of property cbmCorBankState.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCorBankState() {
        return cbmCorBankState;
    }
    
    /**
     * Setter for property cbmCorBankState.
     * @param cbmCorBankState New value of property cbmCorBankState.
     */
    public void setCbmCorBankState(com.see.truetransact.clientutil.ComboBoxModel cbmCorBankState) {
        this.cbmCorBankState = cbmCorBankState;
    }
    
    
    // Setter method for cboCorBankCountry
    void setCboCorBankCountry(String cboCorBankCountry){
        this.cboCorBankCountry = cboCorBankCountry;
        setChanged();
    }
    // Getter method for cboCorBankCountry
    String getCboCorBankCountry(){
        return this.cboCorBankCountry;
    }
    
    /**
     * Getter for property cbmCorBankCountry.
     * @return Value of property cbmCorBankCountry.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCorBankCountry() {
        return cbmCorBankCountry;
    }
    
    /**
     * Setter for property cbmCorBankCountry.
     * @param cbmCorBankCountry New value of property cbmCorBankCountry.
     */
    public void setCbmCorBankCountry(com.see.truetransact.clientutil.ComboBoxModel cbmCorBankCountry) {
        this.cbmCorBankCountry = cbmCorBankCountry;
    }
    
    
    // Setter method for txtBankOfficeInstruction
    void setTxtBankOfficeInstruction(String txtBankOfficeInstruction){
        this.txtBankOfficeInstruction = txtBankOfficeInstruction;
        setChanged();
    }
    // Getter method for txtBankOfficeInstruction
    String getTxtBankOfficeInstruction(){
        return this.txtBankOfficeInstruction;
    }
    
    // Setter method for txtTraderDealerInst
    void setTxtTraderDealerInst(String txtTraderDealerInst){
        this.txtTraderDealerInst = txtTraderDealerInst;
        setChanged();
    }
    // Getter method for txtTraderDealerInst
    String getTxtTraderDealerInst(){
        return this.txtTraderDealerInst;
    }
    
    // Setter method for txtCreditNotes
    void setTxtCreditNotes(String txtCreditNotes){
        this.txtCreditNotes = txtCreditNotes;
        setChanged();
    }
    // Getter method for txtCreditNotes
    String getTxtCreditNotes(){
        return this.txtCreditNotes;
    }
    
    // Setter method for txtClientAdvices
    void setTxtClientAdvices(String txtClientAdvices){
        this.txtClientAdvices = txtClientAdvices;
        setChanged();
    }
    // Getter method for txtClientAdvices
    String getTxtClientAdvices(){
        return this.txtClientAdvices;
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
    
    /** Resets the Fields in the UI to Blank */
    public void resetForm(){
        setTxtMember("");
        setTxtDebitEntitlementGroup("");
        setTxtDebitPortfolioLocation("");
        setTxtDebitAssetSubClass("");
        setTxtDebitAccount("");
        setCboCurrency("");
        setTdtExecutionDate("");
        setTdtSettlementDate("");
        setTxtDebitAmount("");
        setTxtCreditAmount("");
        setCboChargesPaidBy("");
        setRdoStandardCharges_Yes(false);
        setRdoStandardCharges_No(false);
        setTxtChargesAmount("");
        setCboChargesCcy("");
        setTxtByOrderOf("");
        setTxtSwiftCode("");
        setTxtRoutingCode("");
        setTxtBenificiaryName("");
        setTxtBenificiaryAcNo("");
        setTxtBenificiaryBank("");
        setTxtCorrespondentBank("");
        setTxtBenPin("");
        setTxtPaymentDetails("");
        setTxtCorPin("");
        setTxtBankOfficeInstruction("");
        setTxtTraderDealerInst("");
        setTxtCreditNotes("");
        setTxtClientAdvices("");
        setCboBenBankCountry("");
        setCboBenBankCity("");
        setCboBenBankState("");
        setCboCorBankCity("");
        setCboCorBankCountry("");
        setCboCorBankState("");
        notifyObservers();
        
    }
    
    /** Returns an Instance of ExternalWireTO */
    private ExternalWireTO getExternalWireTO(String command){
        
        ExternalWireTO objExternalWireTO = new ExternalWireTO();
        objExternalWireTO.setCommand(command);
        if(objExternalWireTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
            objExternalWireTO.setStatus(CommonConstants.STATUS_CREATED);
            objExternalWireTO.setCreatedBy(TrueTransactMain.USER_ID);
        }else{
            objExternalWireTO.setStatusBy(TrueTransactMain.USER_ID);
        }
        objExternalWireTO.setRefId(getLblReferenceNumber());
        objExternalWireTO.setMemberId(getTxtMember());
        objExternalWireTO.setEntitleGrp(getTxtDebitEntitlementGroup());
        objExternalWireTO.setPortfolioLoc(getTxtDebitPortfolioLocation());
        objExternalWireTO.setAssetSubClass(getTxtDebitAssetSubClass());
        objExternalWireTO.setDebitAcct(getTxtDebitAccount());
        objExternalWireTO.setWireTransCurrency(getCboCurrency());
        
        Date ExDt = DateUtil.getDateMMDDYYYY(getTdtExecutionDate());
        if(ExDt != null){
        Date exDate = (Date)curDate.clone();
        exDate.setDate(ExDt.getDate());
        exDate.setMonth(ExDt.getMonth());
        exDate.setYear(ExDt.getYear());
//        objExternalWireTO.setExecDt(DateUtil.getDateMMDDYYYY(getTdtExecutionDate()));
        objExternalWireTO.setExecDt(exDate);
        }else{
            objExternalWireTO.setExecDt(DateUtil.getDateMMDDYYYY(getTdtExecutionDate()));
        }
        
        Date StDt = DateUtil.getDateMMDDYYYY(getTdtSettlementDate());
        if(StDt != null){
        Date stDate = (Date)curDate.clone();
        stDate.setDate(StDt.getDate());
        stDate.setMonth(StDt.getMonth());
        stDate.setYear(StDt.getYear());
//        objExternalWireTO.setSettlementDt(DateUtil.getDateMMDDYYYY(getTdtSettlementDate()));
        objExternalWireTO.setSettlementDt(stDate);
        }else{
            objExternalWireTO.setSettlementDt(DateUtil.getDateMMDDYYYY(getTdtSettlementDate()));
        }
        
        objExternalWireTO.setDebitAmt(CommonUtil.convertObjToDouble(getTxtDebitAmount()));
        objExternalWireTO.setCreditAmt(CommonUtil.convertObjToDouble((getTxtCreditAmount())));
        objExternalWireTO.setChrgPaidBy(getCboChargesPaidBy());
        if(getRdoStandardCharges_Yes()){
            objExternalWireTO.setStdCharges(YES);
        }else{
            objExternalWireTO.setStdCharges(NO);
        }
        
        objExternalWireTO.setChrgAmt(CommonUtil.convertObjToDouble(getTxtChargesAmount()));
        objExternalWireTO.setChrgCcy(getCboChargesCcy());
        objExternalWireTO.setByOrderOf(getTxtByOrderOf());
        objExternalWireTO.setRoutingCode(getTxtRoutingCode());
        objExternalWireTO.setSwiftCode(getTxtSwiftCode());
        objExternalWireTO.setBenefitName(getTxtBenificiaryName());
        objExternalWireTO.setBenefitBank(getTxtBenificiaryBank());
        objExternalWireTO.setBenefitAcctNo(getTxtBenificiaryAcNo());
        objExternalWireTO.setBenefitBankCountry(getCboBenBankCountry());
        objExternalWireTO.setBenefitCity(getCboBenBankCity());
        objExternalWireTO.setBenefitState(getCboBenBankState());
        objExternalWireTO.setBenefitPin(getTxtBenPin());
        objExternalWireTO.setCorresBank(getTxtCorrespondentBank());
        objExternalWireTO.setCorresCity(getCboCorBankCity());
        objExternalWireTO.setCorresState(getCboCorBankState());
        objExternalWireTO.setCorresCountry(getCboCorBankCountry());
        objExternalWireTO.setCorresPin(getTxtCorPin());
        objExternalWireTO.setBankOffInstruct(getTxtBankOfficeInstruction());
        objExternalWireTO.setTraderInstruct(getTxtTraderDealerInst());
        objExternalWireTO.setCreditNotes(getTxtCreditNotes());
        objExternalWireTO.setClientAdvices(getTxtClientAdvices());
        objExternalWireTO.setPaymentDetails(getTxtPaymentDetails());
        return objExternalWireTO;
        
    }
    
    /** Sets the ExteranalOB Fields thru ExternalWireTO */
    private void setExternalWireTO(ExternalWireTO objExternalWireTO){
        setLblReferenceNumber(objExternalWireTO.getRefId());
        setTxtMember(objExternalWireTO.getMemberId());
        setTxtDebitEntitlementGroup(objExternalWireTO.getEntitleGrp());
        setTxtDebitPortfolioLocation(objExternalWireTO.getPortfolioLoc());
        setTxtDebitAssetSubClass(objExternalWireTO.getAssetSubClass());
        setTxtDebitAccount(objExternalWireTO.getDebitAcct());
        setCboCurrency(objExternalWireTO.getWireTransCurrency());
        setTdtExecutionDate(DateUtil.getStringDate(objExternalWireTO.getExecDt()));
        setTdtSettlementDate(DateUtil.getStringDate(objExternalWireTO.getSettlementDt()));
        setTxtDebitAmount(CommonUtil.convertObjToStr(objExternalWireTO.getDebitAmt()));
        setTxtCreditAmount(CommonUtil.convertObjToStr(objExternalWireTO.getCreditAmt()));
        setCboChargesPaidBy(objExternalWireTO.getChrgPaidBy());
        if(objExternalWireTO.getStdCharges().equals(YES)){
            setRdoStandardCharges_Yes(true);
        }else{
            setRdoStandardCharges_No(true);
        }
        setTxtChargesAmount(CommonUtil.convertObjToStr(objExternalWireTO.getChrgAmt()));
        setCboChargesCcy(objExternalWireTO.getChrgCcy());
        setTxtByOrderOf(objExternalWireTO.getByOrderOf());
        setTxtRoutingCode(objExternalWireTO.getRoutingCode());
        setTxtSwiftCode(objExternalWireTO.getSwiftCode());
        setTxtBenificiaryName(objExternalWireTO.getBenefitName());
        setTxtBenificiaryBank(objExternalWireTO.getBenefitBank());
        setTxtBenificiaryAcNo(objExternalWireTO.getBenefitAcctNo());
        setCboBenBankCountry(objExternalWireTO.getBenefitBankCountry());
        setCboBenBankCity(objExternalWireTO.getBenefitCity());
        setCboBenBankState(objExternalWireTO.getBenefitState());
        setTxtBenPin(objExternalWireTO.getBenefitPin());
        setTxtCorrespondentBank(objExternalWireTO.getCorresBank());
        setCboCorBankCity(objExternalWireTO.getCorresCity());
        setCboCorBankState(objExternalWireTO.getCorresState());
        setCboCorBankCountry(objExternalWireTO.getCorresCountry());
        setTxtCorPin(objExternalWireTO.getCorresPin());
        setTxtBankOfficeInstruction(objExternalWireTO.getBankOffInstruct());
        setTxtTraderDealerInst(objExternalWireTO.getTraderInstruct());
        setTxtCreditNotes(objExternalWireTO.getCreditNotes());
        setTxtClientAdvices(objExternalWireTO.getClientAdvices());
        setTxtPaymentDetails(objExternalWireTO.getPaymentDetails());
        notifyObservers();
        
    }
    
    
    /* Executes Query using the TO object */
    public void execute(String command) {
        try {
            HashMap term = new HashMap();
            term.put("ExternalWireTO", getExternalWireTO(command));
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            HashMap proxyResultMap = proxy.execute(term, map);
            term = null;
            setResult(getActionType());
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
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
    
    /* Populates the TO object by executing a Query */
    public void populateData(HashMap whereMap) {
        HashMap mapData=null;
        try {
            mapData = proxy.executeQuery(whereMap, map);
            ExternalWireTO objExternalWireTO =
            (ExternalWireTO) ((List) mapData.get("ExternalWireTO")).get(0);
            setExternalWireTO(objExternalWireTO);
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            
        }
    }
}
