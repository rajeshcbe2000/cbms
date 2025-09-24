/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PurchaseEquitiesOB.java
 *
 * Created on July 7, 2004, 5:15 PM
 */

package com.see.truetransact.ui.privatebanking.actionitem.purchaseequities;

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
import com.see.truetransact.transferobject.privatebanking.actionitem.purchaseequities.PurchaseEquitiesTO;
import com.see.truetransact.clientutil.ComboBoxModel;
import org.apache.log4j.Logger;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;

public class PurchaseEquitiesOB extends CObservable{
    Date curDate = null;
    private String txtMember = "";
    private String txtEntitlementGroup = "";
    private String txtPortfolioLocation = "";
    private String txtPortfolioAssetSubClass = "";
    private String txtPortfolioAccount = "";
    private String txtSettlementAssetSubClass = "";
    private String txtSettlementAccount = "";
    private String tdtExecutionDate = "";
    private String tdtSettlementDate = "";
    private boolean rdoEDTSEligible_Yes = false;
    private boolean rdoEDTSEligible_No = false;
    private String txtSMIInfo = "";
    private String cboOrderType = "";
    private String cboOrderSubType = "";
    private boolean rdoPhoneOrder_Yes = false;
    private boolean rdoPhoneOrder_No = false;
    private String cboCurrency = "";
    private String txtDealerName = "";
    private String txtLotSize = "";
    private String txtUnits = "";
    private String txtPrice = "";
    private String cboCommType = "";
    private String txtCommission = "";
    private String cboCommission = "";
    private String tdtTillDate = "";
    private boolean rdoProcessthruEdts_Yes = false;
    private boolean rdoProcessthruEdts_No = false;
    private String txtExchange = "";
    private String txtLodgementFee = "";
    private String cboLodgementFee = "";
    private String txtCommRate = "";
    private String txtApproxAmount = "";
    private String txtMinCommAmount = "";
    private String cboMinAmount = "";
    private String txtBankOfficeInstruction = "";
    private String txtTraderDealerInst = "";
    private String txtCreditNotes = "";
    private String txtClientAdvices = "";
    private String lblReferenceNumber = "";
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int _actionType, _result;
    private ComboBoxModel cbmOrderType,cbmOrderSubType,cbmCurrency,cbmCommType,cbmCommission,cbmLodgementFee,cbmMinAmount;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger _log = Logger.getLogger(PurchaseEquitiesOB.class);
    private ProxyFactory proxy;
    private HashMap lookUpHash;
    private HashMap keyValue,map;
    private ArrayList key;
    private ArrayList value;
    private static PurchaseEquitiesOB objPurchaseEquitiesOB;
    private final String YES = "Y",NO = "N";
    
    /** Creates a new instance of PurchaseEquitiesOB */
    public PurchaseEquitiesOB() {
        curDate = ClientUtil.getCurrentDate();
        map = new HashMap();
        map.put(CommonConstants.JNDI, "PurchaseEquitiesJNDI");
        map.put(CommonConstants.HOME, "serverside.privatebanking.actionitem.purchaseequities.PurchaseEquitiesHome");
        map.put(CommonConstants.REMOTE, "serverside.privatebanking.actionitem.purchaseequities.PurchaseEquities");
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
            objPurchaseEquitiesOB= new PurchaseEquitiesOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** This method Returns an instance of this class **/
    public static PurchaseEquitiesOB getInstance()throws Exception{
        return objPurchaseEquitiesOB;
    }
    
    /** Creates the instances for the comboboxmodels */
    private void initUIComboBoxModel(){
        cbmOrderType = new ComboBoxModel();
        cbmOrderSubType = new ComboBoxModel();
        cbmCurrency = new ComboBoxModel();
        cbmCommType = new ComboBoxModel();
        cbmCommission = new ComboBoxModel();
        cbmLodgementFee = new ComboBoxModel();
        cbmMinAmount = new ComboBoxModel();
    }
    
    /* Filling up the the ComboBox in the UI*/
    private void fillDropdown() throws Exception{
        try{
            _log.info("Inside FillDropDown");
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("PVT_PUR.ORDER_TYPE");
            lookup_keys.add("FOREX.CURRENCY");
            lookup_keys.add("PVT_PUR.ORDER_SUB_TYPE");
            lookup_keys.add("PVT_PUR.COMM_TYPE");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get("PVT_PUR.ORDER_TYPE"));
            cbmOrderType = new ComboBoxModel(key,value);
            getKeyValue((HashMap) keyValue.get("FOREX.CURRENCY"));
            cbmCurrency = new ComboBoxModel(key,value);
            cbmCommission = new ComboBoxModel(key,value);
            cbmLodgementFee = new ComboBoxModel(key,value);
            cbmMinAmount = new ComboBoxModel(key,value);
            getKeyValue((HashMap) keyValue.get("PVT_PUR.ORDER_SUB_TYPE"));
            cbmOrderSubType = new ComboBoxModel(key,value);
            getKeyValue((HashMap) keyValue.get("PVT_PUR.COMM_TYPE"));
            cbmCommType = new ComboBoxModel(key,value);
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
    
    // Setter method for txtEntitlementGroup
    void setTxtEntitlementGroup(String txtEntitlementGroup){
        this.txtEntitlementGroup = txtEntitlementGroup;
        setChanged();
    }
    // Getter method for txtEntitlementGroup
    String getTxtEntitlementGroup(){
        return this.txtEntitlementGroup;
    }
    
    // Setter method for txtPortfolioLocation
    void setTxtPortfolioLocation(String txtPortfolioLocation){
        this.txtPortfolioLocation = txtPortfolioLocation;
        setChanged();
    }
    // Getter method for txtPortfolioLocation
    String getTxtPortfolioLocation(){
        return this.txtPortfolioLocation;
    }
    
    // Setter method for txtPortfolioAssetSubClass
    void setTxtPortfolioAssetSubClass(String txtPortfolioAssetSubClass){
        this.txtPortfolioAssetSubClass = txtPortfolioAssetSubClass;
        setChanged();
    }
    // Getter method for txtPortfolioAssetSubClass
    String getTxtPortfolioAssetSubClass(){
        return this.txtPortfolioAssetSubClass;
    }
    
    // Setter method for txtPortfolioAccount
    void setTxtPortfolioAccount(String txtPortfolioAccount){
        this.txtPortfolioAccount = txtPortfolioAccount;
        setChanged();
    }
    // Getter method for txtPortfolioAccount
    String getTxtPortfolioAccount(){
        return this.txtPortfolioAccount;
    }
    
    // Setter method for txtSettlementAssetSubClass
    void setTxtSettlementAssetSubClass(String txtSettlementAssetSubClass){
        this.txtSettlementAssetSubClass = txtSettlementAssetSubClass;
        setChanged();
    }
    // Getter method for txtSettlementAssetSubClass
    String getTxtSettlementAssetSubClass(){
        return this.txtSettlementAssetSubClass;
    }
    
    // Setter method for txtSettlementAccount
    void setTxtSettlementAccount(String txtSettlementAccount){
        this.txtSettlementAccount = txtSettlementAccount;
        setChanged();
    }
    // Getter method for txtSettlementAccount
    String getTxtSettlementAccount(){
        return this.txtSettlementAccount;
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
    
    // Setter method for rdoEDTSEligible_Yes
    void setRdoEDTSEligible_Yes(boolean rdoEDTSEligible_Yes){
        this.rdoEDTSEligible_Yes = rdoEDTSEligible_Yes;
        setChanged();
    }
    // Getter method for rdoEDTSEligible_Yes
    boolean getRdoEDTSEligible_Yes(){
        return this.rdoEDTSEligible_Yes;
    }
    
    // Setter method for rdoEDTSEligible_No
    void setRdoEDTSEligible_No(boolean rdoEDTSEligible_No){
        this.rdoEDTSEligible_No = rdoEDTSEligible_No;
        setChanged();
    }
    // Getter method for rdoEDTSEligible_No
    boolean getRdoEDTSEligible_No(){
        return this.rdoEDTSEligible_No;
    }
    
    // Setter method for txtSMIInfo
    void setTxtSMIInfo(String txtSMIInfo){
        this.txtSMIInfo = txtSMIInfo;
        setChanged();
    }
    // Getter method for txtSMIInfo
    String getTxtSMIInfo(){
        return this.txtSMIInfo;
    }
    
    // Setter method for cboOrderType
    void setCboOrderType(String cboOrderType){
        this.cboOrderType = cboOrderType;
        setChanged();
    }
    // Getter method for cboOrderType
    String getCboOrderType(){
        return this.cboOrderType;
    }
    
    /**
     * Getter for property cbmOrderType.
     * @return Value of property cbmOrderType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmOrderType() {
        return cbmOrderType;
    }
    
    /**
     * Setter for property cbmOrderType.
     * @param cbmOrderType New value of property cbmOrderType.
     */
    public void setCbmOrderType(com.see.truetransact.clientutil.ComboBoxModel cbmOrderType) {
        this.cbmOrderType = cbmOrderType;
    }
    
    // Setter method for cboOrderSubType
    void setCboOrderSubType(String cboOrderSubType){
        this.cboOrderSubType = cboOrderSubType;
        setChanged();
    }
    // Getter method for cboOrderSubType
    String getCboOrderSubType(){
        return this.cboOrderSubType;
    }
    
    /**
     * Getter for property cbmOrderSubType.
     * @return Value of property cbmOrderSubType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmOrderSubType() {
        return cbmOrderSubType;
    }
    
    /**
     * Setter for property cbmOrderSubType.
     * @param cbmOrderSubType New value of property cbmOrderSubType.
     */
    public void setCbmOrderSubType(com.see.truetransact.clientutil.ComboBoxModel cbmOrderSubType) {
        this.cbmOrderSubType = cbmOrderSubType;
    }
    
    // Setter method for rdoPhoneOrder_Yes
    void setRdoPhoneOrder_Yes(boolean rdoPhoneOrder_Yes){
        this.rdoPhoneOrder_Yes = rdoPhoneOrder_Yes;
        setChanged();
    }
    // Getter method for rdoPhoneOrder_Yes
    boolean getRdoPhoneOrder_Yes(){
        return this.rdoPhoneOrder_Yes;
    }
    
    // Setter method for rdoPhoneOrder_No
    void setRdoPhoneOrder_No(boolean rdoPhoneOrder_No){
        this.rdoPhoneOrder_No = rdoPhoneOrder_No;
        setChanged();
    }
    // Getter method for rdoPhoneOrder_No
    boolean getRdoPhoneOrder_No(){
        return this.rdoPhoneOrder_No;
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
    
    
    // Setter method for txtDealerName
    void setTxtDealerName(String txtDealerName){
        this.txtDealerName = txtDealerName;
        setChanged();
    }
    // Getter method for txtDealerName
    String getTxtDealerName(){
        return this.txtDealerName;
    }
    
    // Setter method for txtLotSize
    void setTxtLotSize(String txtLotSize){
        this.txtLotSize = txtLotSize;
        setChanged();
    }
    // Getter method for txtLotSize
    String getTxtLotSize(){
        return this.txtLotSize;
    }
    
    // Setter method for txtUnits
    void setTxtUnits(String txtUnits){
        this.txtUnits = txtUnits;
        setChanged();
    }
    // Getter method for txtUnits
    String getTxtUnits(){
        return this.txtUnits;
    }
    
    // Setter method for txtPrice
    void setTxtPrice(String txtPrice){
        this.txtPrice = txtPrice;
        setChanged();
    }
    // Getter method for txtPrice
    String getTxtPrice(){
        return this.txtPrice;
    }
    
    // Setter method for txtCommType
    void setCboCommType(String cboCommType){
        this.cboCommType = cboCommType;
        setChanged();
    }
    // Getter method for txtCommType
    String getCboCommType(){
        return this.cboCommType;
    }
    
    /**
     * Getter for property cbmCommType.
     * @return Value of property cbmCommType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCommType() {
        return cbmCommType;
    }
    
    /**
     * Setter for property cbmCommType.
     * @param cbmCommType New value of property cbmCommType.
     */
    public void setCbmCommType(com.see.truetransact.clientutil.ComboBoxModel cbmCommType) {
        this.cbmCommType = cbmCommType;
    }
    
    
    // Setter method for txtCommission
    void setTxtCommission(String txtCommission){
        this.txtCommission = txtCommission;
        setChanged();
    }
    // Getter method for txtCommission
    String getTxtCommission(){
        return this.txtCommission;
    }
    
    // Setter method for cboCommission
    void setCboCommission(String cboCommission){
        this.cboCommission = cboCommission;
        setChanged();
    }
    // Getter method for cboCommission
    String getCboCommission(){
        return this.cboCommission;
    }
    
    /**
     * Getter for property cbmCommission.
     * @return Value of property cbmCommission.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCommission() {
        return cbmCommission;
    }
    
    /**
     * Setter for property cbmCommission.
     * @param cbmCommission New value of property cbmCommission.
     */
    public void setCbmCommission(com.see.truetransact.clientutil.ComboBoxModel cbmCommission) {
        this.cbmCommission = cbmCommission;
    }
    
    
    // Setter method for tdtTillDate
    void setTdtTillDate(String tdtTillDate){
        this.tdtTillDate = tdtTillDate;
        setChanged();
    }
    // Getter method for tdtTillDate
    String getTdtTillDate(){
        return this.tdtTillDate;
    }
    
    // Setter method for rdoProcessthruEdts_Yes
    void setRdoProcessthruEdts_Yes(boolean rdoProcessthruEdts_Yes){
        this.rdoProcessthruEdts_Yes = rdoProcessthruEdts_Yes;
        setChanged();
    }
    // Getter method for rdoProcessthruEdts_Yes
    boolean getRdoProcessthruEdts_Yes(){
        return this.rdoProcessthruEdts_Yes;
    }
    
    // Setter method for rdoProcessthruEdts
    void setRdoProcessthruEdts_No(boolean rdoProcessthruEdts_No){
        this.rdoProcessthruEdts_No = rdoProcessthruEdts_No;
        setChanged();
    }
    // Getter method for rdoProcessthruEdts
    boolean getRdoProcessthruEdts_No(){
        return this.rdoProcessthruEdts_No;
    }
    
    // Setter method for txtExchange
    void setTxtExchange(String txtExchange){
        this.txtExchange = txtExchange;
        setChanged();
    }
    // Getter method for txtExchange
    String getTxtExchange(){
        return this.txtExchange;
    }
    
    // Setter method for txtLodgementFee
    void setTxtLodgementFee(String txtLodgementFee){
        this.txtLodgementFee = txtLodgementFee;
        setChanged();
    }
    // Getter method for txtLodgementFee
    String getTxtLodgementFee(){
        return this.txtLodgementFee;
    }
    
    // Setter method for cboLodgementFee
    void setCboLodgementFee(String cboLodgementFee){
        this.cboLodgementFee = cboLodgementFee;
        setChanged();
    }
    // Getter method for cboLodgementFee
    String getCboLodgementFee(){
        return this.cboLodgementFee;
    }
    
    /**
     * Getter for property cbmLodgementFee.
     * @return Value of property cbmLodgementFee.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmLodgementFee() {
        return cbmLodgementFee;
    }
    
    /**
     * Setter for property cbmLodgementFee.
     * @param cbmLodgementFee New value of property cbmLodgementFee.
     */
    public void setCbmLodgementFee(com.see.truetransact.clientutil.ComboBoxModel cbmLodgementFee) {
        this.cbmLodgementFee = cbmLodgementFee;
    }
    
    // Setter method for txtCommRate
    void setTxtCommRate(String txtCommRate){
        this.txtCommRate = txtCommRate;
        setChanged();
    }
    // Getter method for txtCommRate
    String getTxtCommRate(){
        return this.txtCommRate;
    }
    
    // Setter method for txtApproxAmount
    void setTxtApproxAmount(String txtApproxAmount){
        this.txtApproxAmount = txtApproxAmount;
        setChanged();
    }
    // Getter method for txtApproxAmount
    String getTxtApproxAmount(){
        return this.txtApproxAmount;
    }
    
    // Setter method for txtMinCommAmount
    void setTxtMinCommAmount(String txtMinCommAmount){
        this.txtMinCommAmount = txtMinCommAmount;
        setChanged();
    }
    // Getter method for txtMinCommAmount
    String getTxtMinCommAmount(){
        return this.txtMinCommAmount;
    }
    
    // Setter method for cboMinAmount
    void setCboMinAmount(String cboMinAmount){
        this.cboMinAmount = cboMinAmount;
        setChanged();
    }
    // Getter method for cboMinAmount
    String getCboMinAmount(){
        return this.cboMinAmount;
    }
    
    /**
     * Getter for property cbmMinAmount.
     * @return Value of property cbmMinAmount.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmMinAmount() {
        return cbmMinAmount;
    }
    
    /**
     * Setter for property cbmMinAmount.
     * @param cbmMinAmount New value of property cbmMinAmount.
     */
    public void setCbmMinAmount(com.see.truetransact.clientutil.ComboBoxModel cbmMinAmount) {
        this.cbmMinAmount = cbmMinAmount;
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
    
    /** Resets the OB Fields */
    public void resetForm(){
        setTxtMember("");
        setTxtEntitlementGroup("");
        setTxtPortfolioLocation("");
        setTxtPortfolioAssetSubClass("");
        setTxtPortfolioAccount("");
        setTxtSettlementAssetSubClass("");
        setTxtSettlementAccount("");
        setTdtExecutionDate("");
        setTdtSettlementDate("");
        setRdoEDTSEligible_Yes(false);
        setRdoEDTSEligible_No(false);
        setTxtSMIInfo("");
        setCboOrderType("");
        setCboOrderSubType("");
        setRdoPhoneOrder_Yes(false);
        setRdoPhoneOrder_No(false);
        setCboCurrency("");
        setTxtDealerName("");
        setTxtLotSize("");
        setTxtUnits("");
        setTxtPrice("");
        setCboCommType("");
        setTxtCommission("");
        setCboCommission("");
        setTdtTillDate("");
        setRdoProcessthruEdts_Yes(false);
        setRdoProcessthruEdts_No(false);
        setTxtExchange("");
        setTxtLodgementFee("");
        setCboLodgementFee("");
        setTxtCommRate("");
        setTxtApproxAmount("");
        setTxtMinCommAmount("");
        setCboMinAmount("");
        setTxtBankOfficeInstruction("");
        setTxtTraderDealerInst("");
        setTxtCreditNotes("");
        setTxtClientAdvices("");
        notifyObservers();
    }
    
    /** Returns an instance of TO object */
    public PurchaseEquitiesTO getPurchaseEquitiesTO(String command){
        PurchaseEquitiesTO objPurchaseEquitiesTO = new PurchaseEquitiesTO();
        objPurchaseEquitiesTO.setCommand(command);
        if(objPurchaseEquitiesTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
            objPurchaseEquitiesTO.setStatus(CommonConstants.STATUS_CREATED);
            objPurchaseEquitiesTO.setCreatedBy(TrueTransactMain.USER_ID);
        }else{
            objPurchaseEquitiesTO.setStatusBy(TrueTransactMain.USER_ID);
        }
        objPurchaseEquitiesTO.setPurchaseId(getLblReferenceNumber());
        objPurchaseEquitiesTO.setMemberId(getTxtMember());
        objPurchaseEquitiesTO.setEntitleGrp(getTxtEntitlementGroup());
        objPurchaseEquitiesTO.setPortfolioLoc(getTxtPortfolioLocation());
        objPurchaseEquitiesTO.setPfAssetSubClass(getTxtPortfolioAssetSubClass());
        objPurchaseEquitiesTO.setPfAcct(getTxtPortfolioAccount());
        objPurchaseEquitiesTO.setSetAssetSubClass(getTxtSettlementAssetSubClass());
        objPurchaseEquitiesTO.setSetAcct(getTxtSettlementAccount());
//        objPurchaseEquitiesTO.setExecDt(DateUtil.getDateMMDDYYYY(getTdtExecutionDate()));
//        objPurchaseEquitiesTO.setSettlementDt(DateUtil.getDateMMDDYYYY(getTdtSettlementDate()));
        Date TdExDt = DateUtil.getDateMMDDYYYY(getTdtExecutionDate());
        if(TdExDt != null){
        Date tdexDate = (Date)curDate.clone();
        tdexDate.setDate(TdExDt.getDate());
        tdexDate.setMonth(TdExDt.getMonth());
        tdexDate.setYear(TdExDt.getYear());
        objPurchaseEquitiesTO.setExecDt(tdexDate);
        }else{
          objPurchaseEquitiesTO.setExecDt(DateUtil.getDateMMDDYYYY(getTdtExecutionDate()));  
        }
        
        Date TdSetDt = DateUtil.getDateMMDDYYYY(getTdtSettlementDate());
        if(TdSetDt != null){
        Date tdsetDate = (Date)curDate.clone();
        tdsetDate.setDate(TdSetDt.getDate());
        tdsetDate.setMonth(TdSetDt.getMonth());
        tdsetDate.setYear(TdSetDt.getYear());
        objPurchaseEquitiesTO.setSettlementDt(tdsetDate);
        }else{
            objPurchaseEquitiesTO.setSettlementDt(DateUtil.getDateMMDDYYYY(getTdtSettlementDate()));
        }
        
        if(getRdoEDTSEligible_Yes()){
            objPurchaseEquitiesTO.setEdtsEligible(YES);
        }else{
            objPurchaseEquitiesTO.setEdtsEligible(NO);
        }
        objPurchaseEquitiesTO.setSmiInfo(getTxtSMIInfo());
        objPurchaseEquitiesTO.setOrderType(getCboOrderType());
        objPurchaseEquitiesTO.setOrderSubType(getCboOrderSubType());
        if(getRdoPhoneOrder_Yes()){
            objPurchaseEquitiesTO.setPhoneOrder(YES);
        }else{
            objPurchaseEquitiesTO.setPhoneOrder(NO);
        }
        objPurchaseEquitiesTO.setCurrency(getCboCurrency());
        objPurchaseEquitiesTO.setDealerName(getTxtDealerName());
        objPurchaseEquitiesTO.setLotSize(CommonUtil.convertObjToDouble(getTxtLotSize()));
        objPurchaseEquitiesTO.setUnits(CommonUtil.convertObjToDouble(getTxtUnits()));
        objPurchaseEquitiesTO.setPrice(CommonUtil.convertObjToDouble(getTxtPrice()));
        objPurchaseEquitiesTO.setCommType(getCboCommType());
        objPurchaseEquitiesTO.setCommission(CommonUtil.convertObjToDouble(getTxtCommission()));
        objPurchaseEquitiesTO.setCommCurrency(getCboCommission());
//        objPurchaseEquitiesTO.setGoodTillDt(DateUtil.getDateMMDDYYYY(getTdtTillDate()));
        
        Date TdTilDt = DateUtil.getDateMMDDYYYY(getTdtTillDate());
        if(TdTilDt != null){
        Date tdtilDate = (Date)curDate.clone();
        tdtilDate.setDate(TdTilDt.getDate());
        tdtilDate.setMonth(TdTilDt.getMonth());
        tdtilDate.setYear(TdTilDt.getYear());
        objPurchaseEquitiesTO.setGoodTillDt(tdtilDate);
        }else{
            objPurchaseEquitiesTO.setGoodTillDt(DateUtil.getDateMMDDYYYY(getTdtTillDate()));
        }
        if(getRdoProcessthruEdts_Yes()){
            objPurchaseEquitiesTO.setProcessEdts(YES);
        }else{
            objPurchaseEquitiesTO.setProcessEdts(NO);
        }
        objPurchaseEquitiesTO.setExchange(getTxtExchange());
        objPurchaseEquitiesTO.setLodgementFee(CommonUtil.convertObjToDouble(getTxtLodgementFee()));
        objPurchaseEquitiesTO.setLodgementCurrency(getCboLodgementFee());
        objPurchaseEquitiesTO.setCommRate(CommonUtil.convertObjToDouble(getTxtCommRate()));
        objPurchaseEquitiesTO.setApproxAmount(CommonUtil.convertObjToDouble(getTxtApproxAmount()));
        objPurchaseEquitiesTO.setMinCommAmount(CommonUtil.convertObjToDouble(getTxtMinCommAmount()));
        objPurchaseEquitiesTO.setMinCommCurrency(getCboMinAmount());
        objPurchaseEquitiesTO.setBankInst(getTxtBankOfficeInstruction());
        objPurchaseEquitiesTO.setCreditNotes(getTxtCreditNotes());
        objPurchaseEquitiesTO.setTraderInst(getTxtTraderDealerInst());
        objPurchaseEquitiesTO.setClientAdvices(getTxtClientAdvices());
        
        return objPurchaseEquitiesTO;
    }
    
    /** Sets the OB Fields thru TO */
    public void setPurchaseEquitiesTO(PurchaseEquitiesTO objPurchaseEquitiesTO){
        setLblReferenceNumber(objPurchaseEquitiesTO.getPurchaseId());
        setTxtMember(objPurchaseEquitiesTO.getMemberId());
        setTxtEntitlementGroup(objPurchaseEquitiesTO.getEntitleGrp());
        setTxtPortfolioLocation(objPurchaseEquitiesTO.getPortfolioLoc());
        setTxtPortfolioAssetSubClass(objPurchaseEquitiesTO.getPfAssetSubClass());
        setTxtPortfolioAccount(objPurchaseEquitiesTO.getPfAcct());
        setTxtSettlementAssetSubClass(objPurchaseEquitiesTO.getSetAssetSubClass());
        setTxtSettlementAccount(objPurchaseEquitiesTO.getSetAcct());
        setTdtExecutionDate(DateUtil.getStringDate(objPurchaseEquitiesTO.getExecDt()));
        setTdtSettlementDate(DateUtil.getStringDate(objPurchaseEquitiesTO.getSettlementDt()));
        if(objPurchaseEquitiesTO.getEdtsEligible().equals(YES)){
            setRdoEDTSEligible_Yes(true);
        }else{
            setRdoEDTSEligible_No(true);
        }
        setTxtSMIInfo(objPurchaseEquitiesTO.getSmiInfo());
        setCboOrderType(objPurchaseEquitiesTO.getOrderType());
        setCboOrderSubType(objPurchaseEquitiesTO.getOrderSubType());
        if(objPurchaseEquitiesTO.getPhoneOrder().equals(YES)){
            setRdoPhoneOrder_Yes(true);
        }else{
            setRdoPhoneOrder_No(true);
        }
        setCboCurrency(objPurchaseEquitiesTO.getCurrency());
        setTxtDealerName(objPurchaseEquitiesTO.getDealerName());
        setTxtLotSize(CommonUtil.convertObjToStr(objPurchaseEquitiesTO.getLotSize()));
        setTxtUnits(CommonUtil.convertObjToStr(objPurchaseEquitiesTO.getUnits()));
        setTxtPrice(CommonUtil.convertObjToStr(objPurchaseEquitiesTO.getPrice()));
        setCboCommType(objPurchaseEquitiesTO.getCommType());
        setTxtCommission(CommonUtil.convertObjToStr(objPurchaseEquitiesTO.getCommission()));
        setCboCommission(objPurchaseEquitiesTO.getCommCurrency());
        setTdtTillDate(DateUtil.getStringDate(objPurchaseEquitiesTO.getGoodTillDt()));
        if(objPurchaseEquitiesTO.getProcessEdts().equals("Y")){
            setRdoProcessthruEdts_Yes(true);
        }else{
            setRdoProcessthruEdts_No(true);
        }
        setTxtExchange(objPurchaseEquitiesTO.getExchange());
        setTxtLodgementFee(CommonUtil.convertObjToStr(objPurchaseEquitiesTO.getLodgementFee()));
        setCboLodgementFee(objPurchaseEquitiesTO.getLodgementCurrency());
        setTxtCommRate(CommonUtil.convertObjToStr(objPurchaseEquitiesTO.getCommRate()));
        setTxtApproxAmount(CommonUtil.convertObjToStr(objPurchaseEquitiesTO.getApproxAmount()));
        setTxtMinCommAmount(CommonUtil.convertObjToStr(objPurchaseEquitiesTO.getMinCommAmount()));
        setCboMinAmount(objPurchaseEquitiesTO.getMinCommCurrency());
        setTxtBankOfficeInstruction(objPurchaseEquitiesTO.getBankInst());
        setTxtCreditNotes(objPurchaseEquitiesTO.getCreditNotes());
        setTxtTraderDealerInst(objPurchaseEquitiesTO.getTraderInst());
        setTxtClientAdvices(objPurchaseEquitiesTO.getClientAdvices());
        notifyObservers();
    }
    
    /** Returns a map by executing a  query which can be used to fill up the labels in the ui */
    public HashMap getLabelMap(String orderId)throws Exception {
        HashMap resultMap = null;
        try {
            HashMap whereMap = new HashMap();
            whereMap.put("ORD_ID", orderId);
            List resultList = ClientUtil.executeQuery("SelectPvtOrderMaster", whereMap);
            if(resultList.size() > 0){
                resultMap = (HashMap)resultList.get(0);
            }
            resultList = null;
        }catch(Exception e){
        }
        return resultMap;
    }
    
    /* Executes Query using the TO object */
    public void execute(String command) {
        try {
            HashMap term = new HashMap();
            term.put("PurchaseEquitiesTO", getPurchaseEquitiesTO(command));
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
    
    /* Populates the TO object by executing a Query */
    public void populateData(HashMap whereMap) {
        HashMap mapData=null;
        try {
            mapData = proxy.executeQuery(whereMap, map);
            PurchaseEquitiesTO objPurchaseEquitiesTO =
            (PurchaseEquitiesTO) ((List) mapData.get("PurchaseEquitiesTO")).get(0);
            setPurchaseEquitiesTO(objPurchaseEquitiesTO);
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            
        }
    }
}
