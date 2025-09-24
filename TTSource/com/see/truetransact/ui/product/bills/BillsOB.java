/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BillsOB.java
 *
 * Created on Mon Feb 07 10:41:15 IST 2005
 */

package com.see.truetransact.ui.product.bills;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.transferobject.product.bills.BillsTO;
import com.see.truetransact.transferobject.product.bills.BillsChargesTO;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import org.apache.log4j.Logger;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.ui.TrueTransactMain;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Date;
/**
 *
 * @author
 */

public class BillsOB extends CObservable {
    
    private String txtProductId = "";
    //    private String cboBaseCurrency = "";
    private String cboSubRegType = "";
    private String cboRegType = "";
    private String cboOperatesLike = "";
    private String txtProdDesc = "";
    private String txtGLAccountHead = "";
    private String txtInterestAccountHead = "";
    private String txtChargesAccountHead = "";
    private boolean rdoContraAccountHead_Yes = false;
    private boolean rdoContraAccountHead_No = false;
    private boolean rdoPostDtdCheqAllowed_Yes = false;
    private boolean rdoPostDtdCheqAllowed_No = false;
    private String txtDDAccountHead = "";
    private String txtBillsRealisedHead = "";
    private String txtMarginAccountHead = "";
    private String txtCommissionAccountHead = "";
    private String txtDrBkChargeAccountHead="";
    private String txtMisBkChargeAccountHead="";

    public String getTxtDrBkChargeAccountHead() {
        return txtDrBkChargeAccountHead;
    }

    public void setTxtDrBkChargeAccountHead(String txtDrBkChargeAccountHead) {
        this.txtDrBkChargeAccountHead = txtDrBkChargeAccountHead;
    }

    public String getTxtMisBkChargeAccountHead() {
        return txtMisBkChargeAccountHead;
    }

    public void setTxtMisBkChargeAccountHead(String txtMisBkChargeAccountHead) {
        this.txtMisBkChargeAccountHead = txtMisBkChargeAccountHead;
    }
    private String txtPostageAccountHead = "";
    private String txtContraCrAccountHead = "";
    private String txtContraDrAccountHead = "";
    private String txtIBRAccountHead = "";
    private String txtServiceTaxHd = "";
    private String txtOthersHead = "";
    private String txtTelChargesHead = "";
    private String txtAtParLimit = "";
    private String txtDiscountRateBills = "";
    private String txtDefaultPostage = "";
    private String txtRateForDelay = "";
    private String txtOverdueRateBills = "";
    private String txtRateForCBP = "";
    private String txtCleanBills = "";
    private String txtTransitPeriod = "";
    private String txtIntDays = ""; 
    private String cboTransitPeriod = "";
    private String cboIntDays = "";
    private String chkCollectCommFrmCustomer = "";
    private String billStatus = "";
    private String status = "";
    private String statusBy = "";
    private String statusDt = "";
    private String createdBy = "";
    private String createdDt = "";
    private String authorizeStatus = "";
    private String authorizeRemarks = "";
    private String authorizeDt = "";
    private String authorizeUser = "";
    
    private String cboInstrumentType = "";
    private String cboChargeType = "";
    private String cboCustCategory = "";
    private String cboRateType = "";
    private String txtFromSlab = "";
    private String txtToSlab = "";
    private String txtForEvery = "";
    private String txtRateVal = "";
    private String txtFixRate = "";
    private String tdtStartDt = "";
    private String tdtEndDt = "";
    private String txtCommision = "";
    private String txtServiceTax = "";
    private boolean cRadio_ICC_Yes=false;
    private boolean cRadio_ICC_No=false;
    private String txtCollectOtherBankCommFrmCustomer="";
    private boolean rdoGlAcHd=false;
    private boolean rdoInvestmentAcHd=false;
    private String txtOBCCommAcHd="";
    private EnhancedTableModel tblBillsCharges;
    private LinkedHashMap BillsDataTO=null;//contain not deleted records 
    public ArrayList entireBillsDataRow = null;
    final ArrayList billsChargeTabTitle=new ArrayList();
    java.util.ResourceBundle objBillsRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.product.bills.BillsRB", ProxyParameters.LANGUAGE);
    private LinkedHashMap deletedChargesTO = null;
    private static int deleted_Charge=1;
    private LinkedHashMap deletedCharges=null;
    public boolean BillsChrgTabSelected = false;
    private String txtBankChargesAcHd = "",txtDebitBankChargesAcHd="";

    public String getTxtDebitBankChargesAcHd() {
        return txtDebitBankChargesAcHd;
    }

    public void setTxtDebitBankChargesAcHd(String txtDebitBankChargesAcHd) {
        this.txtDebitBankChargesAcHd = txtDebitBankChargesAcHd;
    }
    private  HashMap map = null;
    private static BillsOB objBillsOB; // singleton object
    private ProxyFactory proxy = null;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger _log = Logger.getLogger(BillsOB.class);
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int _result;
    private int _actionType;
    
    private ComboBoxModel cbmTransitPeriod;
    private ComboBoxModel cbmIntDays;
    private ComboBoxModel cbmOperatesLike;
    private ComboBoxModel cbmRegType;
    private ComboBoxModel cbmSubRegType;
    
     private ComboBoxModel cbmInstrumentType;
     private ComboBoxModel cbmChargeType;
     private ComboBoxModel cbmCustCategory;
     private ComboBoxModel cbmRateType;
     
     private int bills_Charge_No=1;
     
     
    //    private ComboBoxModel cbmBaseCurrency;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private ArrayList chargeTabRow;
    private ArrayList billsChargeSelect;
    
    BillsChargesTO objBillsChargesTabTO;
    BillsChargesTO objDelBillsChargesTabTO;
    BillsChargesTO objBillsChargesTabSelect;
    private Date currDt = null;
    /* Creates a new instance of BillsOB */
    private BillsOB() {
        map = new HashMap();
        map.put(CommonConstants.JNDI, "BillsJNDI");
        map.put(CommonConstants.HOME, "serverside.product.bills.BillsHome");
        map.put(CommonConstants.REMOTE, "serverside.proudct.bills.Bills");
        try {
            currDt = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            initUIComboBoxModel();
            fillDropdown();
            setBillsChargesTab();
            tblBillsCharges=new EnhancedTableModel(null,billsChargeTabTitle);
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    static {
        try {
            _log.info("Creating BillsOB...");
            objBillsOB= new BillsOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /* Initialising the Combobox Model*/
    public void initUIComboBoxModel(){
        cbmTransitPeriod = new ComboBoxModel();
        cbmIntDays = new ComboBoxModel();
        cbmOperatesLike = new ComboBoxModel();
        cbmInstrumentType = new ComboBoxModel();
        cbmChargeType = new ComboBoxModel();
        cbmCustCategory = new ComboBoxModel();
        cbmRateType = new ComboBoxModel();
        cbmSubRegType = new ComboBoxModel();
        cbmRegType = new ComboBoxModel();
        //        cbmBaseCurrency = new ComboBoxModel();
    }
    private void setBillsChargesTab()throws Exception{
       
        billsChargeTabTitle.add(objBillsRB.getString("tblColumn2"));
        billsChargeTabTitle.add(objBillsRB.getString("tblColumn3"));
        billsChargeTabTitle.add(objBillsRB.getString("tblColumn4"));
        billsChargeTabTitle.add(objBillsRB.getString("tblColumn5"));
        billsChargeTabTitle.add(objBillsRB.getString("tblColumn6"));
        
        
    }
    /* Filling up the the ComboBox in the UI*/
    private void fillDropdown() throws Exception{
        try{
            _log.info("Inside FillDropDown");
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("PERIOD");
            lookup_keys.add("BILLS_OPERATES_LIKE");
//            lookup_keys.add("BILLS_CHARGE_TYPE");
//            lookup_keys.add("LODGEMENT.INSTRUCTIONS");
            lookup_keys.add("BILLS_CUST_CATEGORY");
            lookup_keys.add("BILLS_RATE_TYPE");
            lookup_keys.add("LODGEMENT.CHARGES");
            lookup_keys.add("CATEGORY");
            lookup_keys.add("FOREX.RATE_TYPE");
            lookup_keys.add("BILLS_REG_TYPE");
//            lookup_keys.add("BILLS_REG_SUB_TYPE");
            //            lookup_keys.add("FOREX.CURRENCY");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get("PERIOD"));
            cbmTransitPeriod = new ComboBoxModel(key,value);
            
//            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get("BILLS_OPERATES_LIKE"));
            cbmOperatesLike = new ComboBoxModel(key,value);
            
//            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get("BILLS_OPERATES_LIKE"));
            cbmInstrumentType = new ComboBoxModel(key,value);
            
            keyValue = ClientUtil.populateLookupData(lookUpHash);
//            getKeyValue((HashMap)keyValue.get("BILLS_CHARGE_TYPE"));
            getKeyValue((HashMap)keyValue.get("LODGEMENT.CHARGES"));
            cbmChargeType = new ComboBoxModel(key,value);
            
//            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get("CATEGORY"));
            cbmCustCategory = new ComboBoxModel(key,value);
            
//            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get("FOREX.RATE_TYPE"));
            cbmRateType = new ComboBoxModel(key,value);
            
//            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get("BILLS_REG_TYPE"));
            cbmRegType = new ComboBoxModel(key,value);
            
//            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get("PERIOD"));
            cbmIntDays = new ComboBoxModel(key,value);
            
//            keyValue = ClientUtil.populateLookupData(lookUpHash);
//            getKeyValue((HashMap)keyValue.get("BILLS_REG_SUB_TYPE"));
//            cbmSubRegType = new ComboBoxModel(key,value);
            
            
            //            getKeyValue((HashMap) keyValue.get("FOREX.CURRENCY"));
            //            cbmBaseCurrency = new ComboBoxModel(key,value);
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
     * Returns an instance of BillsOB.
     * @return  BillsOB
     */
    
    public static BillsOB getInstance()throws Exception{
        return objBillsOB;
    }
    
    
    // Setter method for txtProductId
    void setTxtProductId(String txtProductId){
        this.txtProductId = txtProductId;
        setChanged();
    }
    // Getter method for txtProductId
    String getTxtProductId(){
        return this.txtProductId;
    }
    
    //    // Setter method for cboBaseCurrency
    //    void setCboBaseCurrency(String cboBaseCurrency){
    //        this.cboBaseCurrency = cboBaseCurrency;
    //        setChanged();
    //    }
    //    // Getter method for cboBaseCurrency
    //    String getCboBaseCurrency(){
    //        return this.cboBaseCurrency;
    //    }
    
    // Setter method for cboOperatesLike
    void setCboOperatesLike(String cboOperatesLike){
        this.cboOperatesLike = cboOperatesLike;
        setChanged();
    }
    // Getter method for cboOperatesLike
    String getCboOperatesLike(){
        return this.cboOperatesLike;
    }
    
    // Setter method for txtProdDesc
    void setTxtProdDesc(String txtProdDesc){
        this.txtProdDesc = txtProdDesc;
        setChanged();
    }
    // Getter method for txtProdDesc
    String getTxtProdDesc(){
        return this.txtProdDesc;
    }
    
    // Setter method for txtGLAccountHead
    void setTxtGLAccountHead(String txtGLAccountHead){
        this.txtGLAccountHead = txtGLAccountHead;
        setChanged();
    }
    // Getter method for txtGLAccountHead
    String getTxtGLAccountHead(){
        return this.txtGLAccountHead;
    }
    
    // Setter method for txtInterestAccountHead
    void setTxtInterestAccountHead(String txtInterestAccountHead){
        this.txtInterestAccountHead = txtInterestAccountHead;
        setChanged();
    }
    // Getter method for txtInterestAccountHead
    String getTxtInterestAccountHead(){
        return this.txtInterestAccountHead;
    }
    
    // Setter method for txtChargesAccountHead
    void setTxtChargesAccountHead(String txtChargesAccountHead){
        this.txtChargesAccountHead = txtChargesAccountHead;
        setChanged();
    }
    // Getter method for txtChargesAccountHead
    String getTxtChargesAccountHead(){
        return this.txtChargesAccountHead;
    }
    
    // Setter method for rdoContraAccountHead_Yes
    void setRdoContraAccountHead_Yes(boolean rdoContraAccountHead_Yes){
        this.rdoContraAccountHead_Yes = rdoContraAccountHead_Yes;
        setChanged();
    }
    // Getter method for rdoContraAccountHead_Yes
    boolean getRdoContraAccountHead_Yes(){
        return this.rdoContraAccountHead_Yes;
    }
    
    // Setter method for rdoContraAccountHead_No
    void setRdoContraAccountHead_No(boolean rdoContraAccountHead_No){
        this.rdoContraAccountHead_No = rdoContraAccountHead_No;
        setChanged();
    }
    // Getter method for rdoContraAccountHead_No
    boolean getRdoContraAccountHead_No(){
        return this.rdoContraAccountHead_No;
    }
    
    // Setter method for rdoPostDtdCheqAllowed_Yes
    void setRdoPostDtdCheqAllowed_Yes(boolean rdoPostDtdCheqAllowed_Yes){
        this.rdoPostDtdCheqAllowed_Yes = rdoPostDtdCheqAllowed_Yes;
        setChanged();
    }
    // Getter method for rdoPostDtdCheqAllowed_Yes
    boolean getRdoPostDtdCheqAllowed_Yes(){
        return this.rdoPostDtdCheqAllowed_Yes;
    }
    
    // Setter method for rdoPostDtdCheqAllowed_No
    void setRdoPostDtdCheqAllowed_No(boolean rdoPostDtdCheqAllowed_No){
        this.rdoPostDtdCheqAllowed_No = rdoPostDtdCheqAllowed_No;
        setChanged();
    }
    // Getter method for rdoPostDtdCheqAllowed_No
    boolean getRdoPostDtdCheqAllowed_No(){
        return this.rdoPostDtdCheqAllowed_No;
    }
    
    // Setter method for txtDDAccountHead
    void setTxtDDAccountHead(String txtDDAccountHead){
        this.txtDDAccountHead = txtDDAccountHead;
        setChanged();
    }
    // Getter method for txtDDAccountHead
    String getTxtDDAccountHead(){
        return this.txtDDAccountHead;
    }
    
    // Setter method for txtBillsRealisedHead
    void setTxtBillsRealisedHead(String txtBillsRealisedHead){
        this.txtBillsRealisedHead = txtBillsRealisedHead;
        setChanged();
    }
    // Getter method for txtBillsRealisedHead
    String getTxtBillsRealisedHead(){
        return this.txtBillsRealisedHead;
    }
    
    // Setter method for txtMarginAccountHead
    void setTxtMarginAccountHead(String txtMarginAccountHead){
        this.txtMarginAccountHead = txtMarginAccountHead;
        setChanged();
    }
    // Getter method for txtMarginAccountHead
    String getTxtMarginAccountHead(){
        return this.txtMarginAccountHead;
    }
    
    // Setter method for txtCommissionAccountHead
    void setTxtCommissionAccountHead(String txtCommissionAccountHead){
        this.txtCommissionAccountHead = txtCommissionAccountHead;
        setChanged();
    }
    // Getter method for txtCommissionAccountHead
    String getTxtCommissionAccountHead(){
        return this.txtCommissionAccountHead;
    }
    
    // Setter method for txtPostageAccountHead
    void setTxtPostageAccountHead(String txtPostageAccountHead){
        this.txtPostageAccountHead = txtPostageAccountHead;
        setChanged();
    }
    // Getter method for txtPostageAccountHead
    String getTxtPostageAccountHead(){
        return this.txtPostageAccountHead;
    }
    
    
    
    // Setter method for txtIBRAccountHead
    void setTxtIBRAccountHead(String txtIBRAccountHead){
        this.txtIBRAccountHead = txtIBRAccountHead;
        setChanged();
    }
    // Getter method for txtIBRAccountHead
    String getTxtIBRAccountHead(){
        return this.txtIBRAccountHead;
    }
    
    // Setter method for txtOthersHead
    void setTxtOthersHead(String txtOthersHead){
        this.txtOthersHead = txtOthersHead;
        setChanged();
    }
    // Getter method for txtOthersHead
    String getTxtOthersHead(){
        return this.txtOthersHead;
    }
    
    // Setter method for txtTelChargesHead
    void setTxtTelChargesHead(String txtTelChargesHead){
        this.txtTelChargesHead = txtTelChargesHead;
        setChanged();
    }
    // Getter method for txtTelChargesHead
    String getTxtTelChargesHead(){
        return this.txtTelChargesHead;
    }
    
    // Setter method for txtAtParLimit
    void setTxtAtParLimit(String txtAtParLimit){
        this.txtAtParLimit = txtAtParLimit;
        setChanged();
    }
    // Getter method for txtAtParLimit
    String getTxtAtParLimit(){
        return this.txtAtParLimit;
    }
    
    // Setter method for txtDiscountRateBills
    void setTxtDiscountRateBills(String txtDiscountRateBills){
        this.txtDiscountRateBills = txtDiscountRateBills;
        setChanged();
    }
    // Getter method for txtDiscountRateBills
    String getTxtDiscountRateBills(){
        return this.txtDiscountRateBills;
    }
    
    // Setter method for txtDefaultPostage
    void setTxtDefaultPostage(String txtDefaultPostage){
        this.txtDefaultPostage = txtDefaultPostage;
        setChanged();
    }
    // Getter method for txtDefaultPostage
    String getTxtDefaultPostage(){
        return this.txtDefaultPostage;
    }
    
    // Setter method for txtOverdueRateBills
    void setTxtOverdueRateBills(String txtOverdueRateBills){
        this.txtOverdueRateBills = txtOverdueRateBills;
        setChanged();
    }
    // Getter method for txtOverdueRateBills
    String getTxtOverdueRateBills(){
        return this.txtOverdueRateBills;
    }
    
    // Setter method for txtRateForCBP
    void setTxtRateForCBP(String txtRateForCBP){
        this.txtRateForCBP = txtRateForCBP;
        setChanged();
    }
    // Getter method for txtRateForCBP
    String getTxtRateForCBP(){
        return this.txtRateForCBP;
    }
    
    // Setter method for txtCleanBills
    void setTxtCleanBills(String txtCleanBills){
        this.txtCleanBills = txtCleanBills;
        setChanged();
    }
    // Getter method for txtCleanBills
    String getTxtCleanBills(){
        return this.txtCleanBills;
    }
    
    // Setter method for txtTransitPeriod
    void setTxtTransitPeriod(String txtTransitPeriod){
        this.txtTransitPeriod = txtTransitPeriod;
        setChanged();
    }
    // Getter method for txtTransitPeriod
    String getTxtTransitPeriod(){
        return this.txtTransitPeriod;
    }
    
    // Setter method for cboTransitPeriod
    void setCboTransitPeriod(String cboTransitPeriod){
        this.cboTransitPeriod = cboTransitPeriod;
        setChanged();
    }
    // Getter method for cboTransitPeriod
    String getCboTransitPeriod(){
        return this.cboTransitPeriod;
    }
    // Setter method for billStatus
    void setBillStatus(String billStatus){
        this.billStatus = billStatus;
        setChanged();
    }
    // Getter method for billStatus
    String getBillStatus(){
        return this.billStatus;
    }
    // Setter method for statusBy
    void setStatusBy1(String statusBy){
        this.statusBy = statusBy;
        setChanged();
    }
    // Getter method for statusBy
    String privategetStatusBy(){
        return this.statusBy;
    }
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
    void setAuthorizeStatus1(String authorizeStatus){
        this.authorizeStatus = authorizeStatus;
        setChanged();
    }
    // Getter method for authorizeStatus
    String getAuthorizeStatus1(){
        return this.authorizeStatus;
    }
    // Setter method for authorizeUser
    void setAuthorizeUser(String authorizeUser){
        this.authorizeUser = authorizeUser;
        setChanged();
    }
    // Getter method for authorizeUser
    String getAuthorizeUser(){
        return this.authorizeUser;
    }
    // Setter method for authorizeRemarks
    void setAuthorizeRemarks(String authorizeRemarks){
        this.authorizeRemarks = authorizeRemarks;
        setChanged();
    }
    // Getter method for authorizeRemarks
    String getAuthorizeRemarks(){
        return this.authorizeRemarks;
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
    // Setter method for createdBy
    void setCreatedBy(String createdBy){
        this.createdBy = createdBy;
        setChanged();
    }
    // Getter method for createdBy
    String getCreatedBy(){
        return this.createdBy;
    }
    // Setter method for createdDt
    void setCreatedDt(String createdDt){
        this.createdDt = createdDt;
        setChanged();
    }
    // Getter method for createdDt
    String getCreatedDt(){
        return this.createdDt;
    }
    
    /** Getter for property cbmTransitPeriod.
     * @return Value of property cbmTransitPeriod.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmTransitPeriod() {
        return cbmTransitPeriod;
    }
    
    /** Setter for property cbmTransitPeriod.
     * @param cbmTransitPeriod New value of property cbmTransitPeriod.
     *
     */
    public void setCbmTransitPeriod(com.see.truetransact.clientutil.ComboBoxModel cbmTransitPeriod) {
        this.cbmTransitPeriod = cbmTransitPeriod;
    }
    /** Getter for property cbmOperatesLike.
     * @return Value of property cbmOperatesLike.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmOperatesLike() {
        return cbmOperatesLike;
    }
    
    /** Setter for property cbmOperatesLike.
     * @param cbmOperatesLike New value of property cbmOperatesLike.
     *
     */
    public void setCbmOperatesLike(com.see.truetransact.clientutil.ComboBoxModel cbmOperatesLike) {
        this.cbmOperatesLike = cbmOperatesLike;
    }
    
    /** Getter for property CbmRegType.
     * @return Value of property CbmRegType.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRegType() {
        return cbmRegType;
    }
    
    /** Setter for property CbmRegType.
     * @param CbmRegType New value of property CbmRegType.
     *
     */
    public void setCbmRegType(com.see.truetransact.clientutil.ComboBoxModel cbmRegType) {
        this.cbmRegType = cbmRegType;
    }
    
//    /** Getter for property CbmRegType.
//     * @return Value of property CbmRegType.
//     *
//     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmSubRegType() {
        return cbmSubRegType;
    }
//    
//    /** Setter for property CbmRegType.
//     * @param CbmRegType New value of property CbmRegType.
//     *
//     */
    public void setCbmSubRegType(com.see.truetransact.clientutil.ComboBoxModel cbmSubRegType) {
        this.cbmSubRegType = cbmSubRegType;
    }
    
    /** Getter for property cbmBaseCurrency.
     * @return Value of property cbmBaseCurrency.
     *
     */
    //    public com.see.truetransact.clientutil.ComboBoxModel getCbmBaseCurrency() {
    //        return cbmBaseCurrency;
    //    }
    //
    //    /** Setter for property cbmBaseCurrency.
    //     * @param cbmBaseCurrency New value of property cbmBaseCurrency.
    //     *
    //     */
    //    public void setCbmBaseCurrency(com.see.truetransact.clientutil.ComboBoxModel cbmBaseCurrency) {
    //        this.cbmBaseCurrency = cbmBaseCurrency;
    //    }
    
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
    
    /* Resetting all the Fields in the UI */
    public void resetForm(){
        setTxtProductId("");
        setTxtProdDesc("");
        setCboOperatesLike("");
        setCboRegType("");
        setCboSubRegType("");
        setTxtBillsRealisedHead("");
        setAuthorizeDt("");
        setAuthorizeRemarks("");
        setAuthorizeStatus1("");
        setAuthorizeUser("");
        setCreatedBy("");
        setCreatedDt("");
        setChkCollectCommFrmCustomer("");
        setTxtDDAccountHead("");
        setTxtDiscountRateBills("");
        setTxtInterestAccountHead("");
        setTxtRateForCBP("");
        setTxtDefaultPostage("");
        setTxtRateForDelay("");
        setTxtProdDesc("");
        setTxtOthersHead("");
        setTxtOverdueRateBills("");
        setTxtCleanBills("");
        setBillStatus("");
        setStatusBy1("");
        setStatusDt("");
        setTxtTelChargesHead("");
        setTxtGLAccountHead("");
        setTxtInterestAccountHead("");
        setTxtChargesAccountHead("");
        setRdoContraAccountHead_Yes(false);
        setRdoContraAccountHead_No(false);
        setCRadio_ICC_No(false);
        setCRadio_ICC_Yes(false);
        setTxtDDAccountHead("");
        setTxtTransitPeriod("");
        setTxtIntDays("");
        setCboTransitPeriod("");
        setRdoPostDtdCheqAllowed_Yes(false);
        setRdoPostDtdCheqAllowed_No(false);
        setRdoGlAcHd(false);
        setRdoInvestmentAcHd(false);
        setTxtOBCCommAcHd("");
      
        setTxtMarginAccountHead("");
        setTxtCommissionAccountHead("");
        //added by rishad
        setTxtDrBkChargeAccountHead("");
        setTxtMisBkChargeAccountHead("");
        setTxtPostageAccountHead("");
        setTxtContraCrAccountHead("");
        setTxtContraDrAccountHead("");
        setTxtIBRAccountHead("");
        setTxtServiceTaxHd("");
        setTxtAtParLimit("");
        setCboInstrumentType("");
        setCboChargeType("");
        setCboCustCategory("");
        setCboRateType("");
        setTxtFromSlab("");
        setTxtToSlab("");
        setTxtFixRate("");
        setTxtForEvery("");
        setTxtRateVal("");
        setTxtCommision("");
        setTxtServiceTax("");
        setTdtStartDt("");
        setTdtEndDt("");
        setCboIntDays("");
        setTxtBankChargesAcHd("");
        setTxtDebitBankChargesAcHd("");
        ttNotifyObservers();
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    /* Setting up the OB fields by using TO objects */
    private void setBillsTO(BillsTO objBillsTO){
        setTxtProductId(CommonUtil.convertObjToStr(objBillsTO.getProdId()));        
        setCboOperatesLike((String) getCbmOperatesLike().getDataForKey(CommonUtil.convertObjToStr(objBillsTO.getOperatesLike())));
        setCboRegType((String) getCbmRegType().getDataForKey(CommonUtil.convertObjToStr(objBillsTO.getRegType())));
//        setCboSubRegType((String)getCbmSubRegType().getDataForKey(CommonUtil.convertObjToStr(objBillsTO.getRegSubType())));
        if (!CommonUtil.convertObjToStr(objBillsTO.getRegSubType()).equals("")) {
            setSubRegType(objBillsTO.getRegType());
            setCboSubRegType((String) getCbmSubRegType().getDataForKey(CommonUtil.convertObjToStr(objBillsTO.getRegSubType())));
        }
        setTxtGLAccountHead(CommonUtil.convertObjToStr(objBillsTO.getGlAcHd()));
        setTxtInterestAccountHead(CommonUtil.convertObjToStr(objBillsTO.getIntAcHd()));
        setTxtChargesAccountHead(CommonUtil.convertObjToStr(objBillsTO.getChrgAcHd()));
        
        if(objBillsTO.getContraAcHdYn().equals("Y") ){
            setRdoContraAccountHead_Yes(true);
        }
        else{
            setRdoContraAccountHead_No(true);
        }
        
        if(objBillsTO.getIntIcc().equals("Y") ){
            setCRadio_ICC_Yes(true);
        }
        else{
            setCRadio_ICC_No(true);
        }
        setTxtDDAccountHead(CommonUtil.convertObjToStr(objBillsTO.getDdAcHd()));
        setChkCollectCommFrmCustomer(CommonUtil.convertObjToStr(objBillsTO.getOtherBankCommFrmCust()));
        int value = CommonUtil.convertObjToInt(objBillsTO.getTransPeriod());
        if((value/365 > 0 ) && (value%365 == 0)){
            setTxtTransitPeriod(CommonUtil.convertObjToStr(new Integer(value/365)));
            setCboTransitPeriod("Years");
        }else if((value/30 > 0 ) && (value%30 == 0)){
            setTxtTransitPeriod(CommonUtil.convertObjToStr(new Integer(value/30)));
            setCboTransitPeriod("Months");
        }else{
            setTxtTransitPeriod(CommonUtil.convertObjToStr(new Integer(value)));
            setCboTransitPeriod("Days");
        }
        
        int value1 = CommonUtil.convertObjToInt(objBillsTO.getNoOfIntDays());
        if((value1/365 > 0 ) && (value1%365 == 0)){
            setTxtIntDays(CommonUtil.convertObjToStr(new Integer(value1/365)));
            setCboIntDays("Years");
        }else if((value1/30 > 0 ) && (value1%30 == 0)){
            setTxtIntDays(CommonUtil.convertObjToStr(new Integer(value1/30)));
            setCboIntDays("Months");
        }else{
            setTxtIntDays(CommonUtil.convertObjToStr(new Integer(value1)));
            setCboIntDays("Days");
        }
        
        if(objBillsTO.getPostDtChqYn().equals("Y")){
            setRdoPostDtdCheqAllowed_Yes(true);
        }
        else{
            setRdoPostDtdCheqAllowed_No(true);
        }
        if(objBillsTO.getCreditOtherBankTo()!=null)
        if(objBillsTO.getCreditOtherBankTo().equals("Gl A/c Hd")){
            setRdoGlAcHd(true);
            setRdoInvestmentAcHd(false);
        }else{
            setRdoInvestmentAcHd(true);
           setRdoGlAcHd(false);
        }
        setTxtOBCCommAcHd(objBillsTO.getTxtOBCCommAcHd());
        
        //        setCboBaseCurrency(objBillsTO.getBaseCurrency());
        setTxtMarginAccountHead(CommonUtil.convertObjToStr(objBillsTO.getMarginAcHd()));
        setTxtCommissionAccountHead(CommonUtil.convertObjToStr(objBillsTO.getCommAcHd()));
        setTxtDrBkChargeAccountHead(CommonUtil.convertObjToStr(objBillsTO.getBankChargesDrAcHd()));
        setTxtMisBkChargeAccountHead(CommonUtil.convertObjToStr(objBillsTO.getBankChargesMisAcHd()));
        setTxtPostageAccountHead(CommonUtil.convertObjToStr(objBillsTO.getPostageAcHd()));
        setTxtContraCrAccountHead(CommonUtil.convertObjToStr(objBillsTO.getContraAcHd()));
        setTxtContraDrAccountHead(CommonUtil.convertObjToStr(objBillsTO.getContraDrAcHd()));
        
        setTxtIBRAccountHead(CommonUtil.convertObjToStr(objBillsTO.getIbrAcHd()));
        setTxtServiceTaxHd(CommonUtil.convertObjToStr(objBillsTO.getServTaxAcHd()));
        setTxtAtParLimit(CommonUtil.convertObjToStr(objBillsTO.getAtParLimit()));
        
        
        setTxtBillsRealisedHead(CommonUtil.convertObjToStr(objBillsTO.getBillsRealisedHd()));
        setAuthorizeDt(DateUtil.getStringDate(objBillsTO.getAuthorizeDt()));
        setAuthorizeRemarks( CommonUtil.convertObjToStr(objBillsTO.getAuthorizeRemark()));
        setAuthorizeStatus1(CommonUtil.convertObjToStr(objBillsTO.getAuthorizeStatus()));
        setAuthorizeUser(CommonUtil.convertObjToStr(objBillsTO.getAuthorizeUser()));
        setCreatedBy(CommonUtil.convertObjToStr(objBillsTO.getCreatedBy()));
        setCreatedDt(DateUtil.getStringDate(objBillsTO.getCreatedDt()));
        
        setTxtDDAccountHead(CommonUtil.convertObjToStr(objBillsTO.getDdAcHd()));
        setTxtDiscountRateBills(CommonUtil.convertObjToStr(objBillsTO.getDiscountRateBd()));
        setTxtInterestAccountHead(CommonUtil.convertObjToStr(objBillsTO.getIntAcHd()));
        setTxtRateForCBP(CommonUtil.convertObjToStr(objBillsTO.getInterestRateCbp()));
        setTxtDefaultPostage(CommonUtil.convertObjToStr(objBillsTO.getPostageRate()));
        setTxtRateForDelay(CommonUtil.convertObjToStr(objBillsTO.getRateForDelay()));
        setTxtProdDesc(CommonUtil.convertObjToStr(objBillsTO.getProdDesc()));
        setTxtOthersHead(CommonUtil.convertObjToStr(objBillsTO.getOtherHd()));
        setTxtOverdueRateBills(CommonUtil.convertObjToStr(objBillsTO.getOverdueRateBd()));
        setTxtCleanBills(CommonUtil.convertObjToStr(objBillsTO.getOverdueRateCbp()));
        setBillStatus(CommonUtil.convertObjToStr(objBillsTO.getStatus()));
        setStatusBy1(CommonUtil.convertObjToStr(objBillsTO.getStatusBy()));
        setStatusDt(CommonUtil.convertObjToStr(DateUtil.getStringDate(objBillsTO.getStatusDt())));
        setTxtTelChargesHead(CommonUtil.convertObjToStr(objBillsTO.getTelephoneChrgHd()));
        setTxtBankChargesAcHd(CommonUtil.convertObjToStr(objBillsTO.getBankChargesAcHd()));
        setTxtDebitBankChargesAcHd(CommonUtil.convertObjToStr(objBillsTO.getDebitBankChargesAcHd()));
        ttNotifyObservers();
    }
    
    /* Returns a TO object */
    public BillsTO getBillsTO(String command){
        BillsTO objBillsTO = new BillsTO();
        final String yes="Y";
        final String no="N";
        
        objBillsTO.setCommand(command);
        objBillsTO.setProdId(CommonUtil.convertObjToStr(getTxtProductId()));
        objBillsTO.setOperatesLike(CommonUtil.convertObjToStr((String)cbmOperatesLike.getKeyForSelected()));
        objBillsTO.setRegType(CommonUtil.convertObjToStr((String)cbmRegType.getKeyForSelected()));
        objBillsTO.setRegSubType(CommonUtil.convertObjToStr((String)cbmSubRegType.getKeyForSelected()));
        objBillsTO.setGlAcHd(CommonUtil.convertObjToStr(getTxtGLAccountHead()));
        objBillsTO.setIntAcHd(CommonUtil.convertObjToStr(getTxtInterestAccountHead()));
        objBillsTO.setChrgAcHd(CommonUtil.convertObjToStr(getTxtChargesAccountHead()));
        
        if(getRdoContraAccountHead_Yes())
            objBillsTO.setContraAcHdYn(yes);
        else
            objBillsTO.setContraAcHdYn(no);
        
        if(isCRadio_ICC_Yes())
            objBillsTO.setIntIcc(yes);
        else
            objBillsTO.setIntIcc(no);
        objBillsTO.setDdAcHd(CommonUtil.convertObjToStr(getTxtDDAccountHead()));
        objBillsTO.setOtherBankCommFrmCust(CommonUtil.convertObjToStr(getChkCollectCommFrmCustomer()));
        //Getting the Transit period
        int time = CommonUtil.convertObjToInt(CommonUtil.convertObjToStr(getTxtTransitPeriod()));
        String timeUnit = getCboTransitPeriod();
        if(timeUnit.equals("Years")){
            //If Years is selected in the CboTransitPeriod in the UI
            objBillsTO.setTransPeriod(new Double(time * 365));
        }else if(timeUnit.equals("Months")){
            //If Months is selected in the cboTransitPeriod in the UI
            objBillsTO.setTransPeriod(new Double(time * 30));
        }else{
            //If Days is selected in the cboTransitPeriod in the UI
            objBillsTO.setTransPeriod(new Double(time * 1));
        }
        
         int time1 = CommonUtil.convertObjToInt(CommonUtil.convertObjToStr(getTxtIntDays()));
        String timeUnit1 = getCboIntDays();
        if(timeUnit1.equals("Years")){
            //If Years is selected in the CboTransitPeriod in the UI
            objBillsTO.setNoOfIntDays(new Double(time1 * 365));
        }else if(timeUnit1.equals("Months")){
            //If Months is selected in the cboTransitPeriod in the UI
            objBillsTO.setNoOfIntDays(new Double(time1 * 30));
        }else{
            //If Days is selected in the cboTransitPeriod in the UI
            objBillsTO.setNoOfIntDays(new Double(time1 * 1));
        }
        
        if( getRdoPostDtdCheqAllowed_Yes())
            objBillsTO.setPostDtChqYn(yes);
        else
            objBillsTO.setPostDtChqYn(no);
        if(isRdoGlAcHd()){
            objBillsTO.setCreditOtherBankTo("Gl A/c Hd");
        }else{
            objBillsTO.setCreditOtherBankTo("Investment A/c Hd");
        }
        objBillsTO.setTxtOBCCommAcHd(getTxtOBCCommAcHd());
        
        //        objBillsTO.setBaseCurrency(CommonUtil.convertObjToStr(getCboBaseCurrency()));
        objBillsTO.setMarginAcHd(CommonUtil.convertObjToStr(getTxtMarginAccountHead()));
        objBillsTO.setCommAcHd(CommonUtil.convertObjToStr(getTxtCommissionAccountHead()));
        objBillsTO.setBankChargesDrAcHd(CommonUtil.convertObjToStr(getTxtDrBkChargeAccountHead()));
        objBillsTO.setBankChargesMisAcHd(CommonUtil.convertObjToStr(getTxtMisBkChargeAccountHead()));
        objBillsTO.setPostageAcHd(CommonUtil.convertObjToStr(getTxtPostageAccountHead()));
        objBillsTO.setContraAcHd(CommonUtil.convertObjToStr(getTxtContraCrAccountHead()));
        objBillsTO.setContraDrAcHd(CommonUtil.convertObjToStr(getTxtContraDrAccountHead()));
        
        objBillsTO.setIbrAcHd(CommonUtil.convertObjToStr(getTxtIBRAccountHead()));
        objBillsTO.setServTaxAcHd(CommonUtil.convertObjToStr(getTxtServiceTaxHd()));
        objBillsTO.setAtParLimit(CommonUtil.convertObjToStr(getTxtAtParLimit()));
        
        objBillsTO.setBillsRealisedHd(CommonUtil.convertObjToStr(getTxtBillsRealisedHead()));
         objBillsTO.setCreatedBy(CommonUtil.convertObjToStr(getCreatedBy()));
        objBillsTO.setCreatedDt(currDt);
        objBillsTO.setDdAcHd(CommonUtil.convertObjToStr(getTxtDDAccountHead()));
        objBillsTO.setDiscountRateBd(CommonUtil.convertObjToDouble(getTxtDiscountRateBills()));
        objBillsTO.setIntAcHd(CommonUtil.convertObjToStr(getTxtInterestAccountHead()));
        objBillsTO.setInterestRateCbp(CommonUtil.convertObjToDouble(getTxtRateForCBP()));
        objBillsTO.setPostageRate(CommonUtil.convertObjToDouble(getTxtDefaultPostage()));
        objBillsTO.setRateForDelay(CommonUtil.convertObjToDouble(getTxtRateForDelay()));
        objBillsTO.setProdDesc(CommonUtil.convertObjToStr(getTxtProdDesc()));
        objBillsTO.setOtherHd(CommonUtil.convertObjToStr(getTxtOthersHead()));
        objBillsTO.setOverdueRateBd(CommonUtil.convertObjToDouble(getTxtOverdueRateBills()));
        objBillsTO.setOverdueRateCbp(CommonUtil.convertObjToDouble(getTxtCleanBills()));
        objBillsTO.setStatus(CommonUtil.convertObjToStr(getBillStatus()));
        objBillsTO.setStatusBy(CommonUtil.convertObjToStr(getStatusBy()));
        objBillsTO.setStatusDt(currDt);
        objBillsTO.setTelephoneChrgHd(CommonUtil.convertObjToStr(getTxtTelChargesHead()));
        objBillsTO.setStatus(CommonConstants.STATUS_CREATED);
        objBillsTO.setBankChargesAcHd(CommonUtil.convertObjToStr(getTxtBankChargesAcHd()));
        objBillsTO.setDebitBankChargesAcHd(CommonUtil.convertObjToStr(getTxtDebitBankChargesAcHd()));
        return objBillsTO;
    }
    public void setResult(int result) {
        _result = result;
        setResultStatus();
        setChanged();
    }
    /* Populates the TO object by executing a Query */
    public void populateData(HashMap whereMap) {
        HashMap mapData=null;
        try {
            mapData = proxy.executeQuery(whereMap, map);
            BillsTO objBillsTO = (BillsTO) ((List) mapData.get("BillsTO")).get(0);
//            BillsChargesTO objChargeTO=(BillsChargesTO)((List)mapData.get("BillsChargesTO")).get(0);
            
            setBillsTO(objBillsTO);
            populateOB(mapData);
           
        } catch( Exception e ) {
            e.printStackTrace();
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            
        }
    }
    private void populateOB(HashMap mapData) throws Exception{
        BillsChargesTO objBillsChargesTO = null;
        ArrayList arrayBillsChargesTabTO = null;
        System.out.print("mapdata$$$$#####"+mapData);
        List lstData = (List)mapData.get("BillsChargesTO");
        for(int i=0; i < lstData.size(); i++){
            objBillsChargesTO = (BillsChargesTO) ((List) mapData.get("BillsChargesTO")).get(i);
        }
        setBillsChargesTO(objBillsChargesTO);
        
        arrayBillsChargesTabTO =  (ArrayList) (mapData.get("BillsChargesTO"));
        setBillsChargesTabTO(arrayBillsChargesTabTO);
        System.out.print("arrayBillsChargesTabTO$$$$#####"+arrayBillsChargesTabTO);
        
//        ttNotifyObservers();
    }
    
    private void setBillsChargesTabTO(ArrayList arrayBillsChargesTO) throws Exception{
        //log.info("In setBillsChargesTO...");
        System.out.println("setBillsCharges###"+arrayBillsChargesTO);
        BillsChargesTO objBillsChargesTO= new BillsChargesTO();
        BillsDataTO=new LinkedHashMap();
        entireBillsDataRow=new ArrayList();
        for (int i=0, j= arrayBillsChargesTO.size();i<j;i++){
            chargeTabRow = new ArrayList();
            objBillsChargesTO = (BillsChargesTO)arrayBillsChargesTO.get(i);
            BillsDataTO.put(String.valueOf(bills_Charge_No),objBillsChargesTO);
            entireBillsDataRow.add(setColumnValueBillsCharge(bills_Charge_No, objBillsChargesTO));
            bills_Charge_No++;

            
            objBillsChargesTO = (BillsChargesTO)arrayBillsChargesTO.get(i);
            chargeTabRow.add(CommonUtil.convertObjToStr(objBillsChargesTO.getInstrumentType()));
            chargeTabRow.add(CommonUtil.convertObjToStr(objBillsChargesTO.getChargeType()));
            chargeTabRow.add(CommonUtil.convertObjToStr(objBillsChargesTO.getCustCategory()));
            chargeTabRow.add(CommonUtil.convertObjToStr(objBillsChargesTO.getStartDate()));
            chargeTabRow.add(CommonUtil.convertObjToStr(objBillsChargesTO.getEndDate()));
            chargeTabRow.add(CommonUtil.convertObjToDouble(objBillsChargesTO.getFromSlab()));
            chargeTabRow.add(CommonUtil.convertObjToDouble(objBillsChargesTO.getToSlab()));
            chargeTabRow.add(CommonUtil.convertObjToDouble(objBillsChargesTO.getCommision()));
            chargeTabRow.add(CommonUtil.convertObjToDouble(objBillsChargesTO.getServiceTax()));
            chargeTabRow.add(CommonUtil.convertObjToDouble(objBillsChargesTO.getFixedRate()));
            chargeTabRow.add(CommonUtil.convertObjToDouble(objBillsChargesTO.getForEveryAmt()));
            chargeTabRow.add(CommonUtil.convertObjToStr(objBillsChargesTO.getRateType()));
            chargeTabRow.add(CommonUtil.convertObjToDouble(objBillsChargesTO.getForEveryRate()));
            chargeTabRow.add(CommonUtil.convertObjToStr(objBillsChargesTO.getStatus()));
            objBillsChargesTO = null;
            //entireBillsDataRow.add(objBillsChargesTO);
            tblBillsCharges.insertRow(i,chargeTabRow);
//            
        }
       bills_Charge_No=1;
    }
    private void setBillsChargesTO(BillsChargesTO objBillsChargesTO) throws Exception{
        //log.info("In setLoanProductChargesTO...");
        setCboInstrumentType((String) getCbmInstrumentType().getDataForKey(CommonUtil.convertObjToStr(objBillsChargesTO.getInstrumentType())));
        setCboChargeType((String) getCbmChargeType().getDataForKey(CommonUtil.convertObjToStr(objBillsChargesTO.getChargeType())));
        setCboCustCategory((String) getCbmCustCategory().getDataForKey(CommonUtil.convertObjToStr(objBillsChargesTO.getCustCategory())));
        setTdtStartDt(CommonUtil.convertObjToStr(objBillsChargesTO.getStartDate()));
        setTdtEndDt(CommonUtil.convertObjToStr(objBillsChargesTO.getEndDate()));
        setTxtFromSlab(CommonUtil.convertObjToStr(objBillsChargesTO.getFromSlab()));
        setTxtToSlab(CommonUtil.convertObjToStr(objBillsChargesTO.getToSlab()));
        setTxtCommision(CommonUtil.convertObjToStr(objBillsChargesTO.getCommision()));
        setTxtServiceTax(CommonUtil.convertObjToStr(objBillsChargesTO.getServiceTax()));
        setTxtFixRate(CommonUtil.convertObjToStr(objBillsChargesTO.getFixedRate()));
        setTxtForEvery(CommonUtil.convertObjToStr(objBillsChargesTO.getForEveryAmt()));
        setCboRateType((String) getCbmRateType().getDataForKey(CommonUtil.convertObjToStr(objBillsChargesTO.getRateType())));
        setTxtRateVal(CommonUtil.convertObjToStr(objBillsChargesTO.getForEveryRate()));
        setStatus(CommonUtil.convertObjToStr(objBillsChargesTO.getStatus()));
        objBillsChargesTO = null;
        
        
    }
    
     //==========INSERT TABLE OF CHARGES...==========
    void setTblBillsCharges(EnhancedTableModel tblBillsCharges){
        //log.info("In setTblBillsCharges...");
        
        this.tblBillsCharges = tblBillsCharges;
        setChanged();
    }
    
    EnhancedTableModel getTblBillsCharges(){
        return this.tblBillsCharges;
    }
    public void populateBillsCharge(int row){
        ArrayList BillsCharge=(ArrayList)tblBillsCharges.getDataArrayList().get(row);
        System.out.println("populateNoticeCharge####"+BillsCharge);
//        String Inst = (String)BillsCharge.get(0).toString().toUpperCase();
//        setCboInstrumentType(CommonUtil.convertObjToStr(BillsCharge.get(0)));
        setCboInstrumentType(CommonUtil.convertObjToStr(getCbmInstrumentType().getDataForKey(BillsCharge.get(0))));
//        String Char = (String)BillsCharge.get(1).toString().toUpperCase();
//        setCboChargeType(CommonUtil.convertObjToStr(BillsCharge.get(1)));
        setCboChargeType(CommonUtil.convertObjToStr(getCbmChargeType().getDataForKey(BillsCharge.get(1))));
//        String Cust = (String)BillsCharge.get(2).toString().toUpperCase();
        setCboCustCategory(CommonUtil.convertObjToStr(getCbmCustCategory().getDataForKey(BillsCharge.get(2))));
//        setCboCustCategory(CommonUtil.convertObjToStr(BillsCharge.get(2)));
        setTdtStartDt(CommonUtil.convertObjToStr(BillsCharge.get(3)));
        setTdtEndDt(CommonUtil.convertObjToStr(BillsCharge.get(4)));
        setTxtFromSlab(CommonUtil.convertObjToStr(BillsCharge.get(5)));
        setTxtToSlab(CommonUtil.convertObjToStr(BillsCharge.get(6)));
        setTxtCommision(CommonUtil.convertObjToStr(BillsCharge.get(7)));
        setTxtServiceTax(CommonUtil.convertObjToStr(BillsCharge.get(8)));
        setTxtFixRate(CommonUtil.convertObjToStr(BillsCharge.get(9)));
        setTxtForEvery(CommonUtil.convertObjToStr(BillsCharge.get(10)));
        setCboRateType(CommonUtil.convertObjToStr(BillsCharge.get(11)));
        setTxtRateVal(CommonUtil.convertObjToStr(BillsCharge.get(12)));
        setStatus(CommonUtil.convertObjToStr(BillsCharge.get(13)));
        setChanged();
        ttNotifyObservers();
    }
    public boolean isProductIdAvailable(String productId) {
        boolean exist = false;
        try {
            if (productId != null && productId.length()>0) {
                HashMap where = new HashMap();
                where.put("PROD_ID", productId);
                List list = (List) ClientUtil.executeQuery("countProdId", where);
                if ( list.size() > 0 && list != null) {
                    String count = CommonUtil.convertObjToStr(((HashMap) list.get(0)).get("COUNT"));
                    if ( Integer.parseInt(count) > 0 ) {
                        exist = true;
                    } else {
                        exist = false;
                    }
                }
            }
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        return exist;
    }
    
    /* Executes Query using the TO object */
    public void execute(String command) {
        try {
            HashMap term = new HashMap();
            final BillsChargesTO objBillsChargesTO;
            final ArrayList billsCharge = setBillsChargeTab();
            ArrayList delBillsCharge = null;
            if(deletedCharges != null && deletedCharges.size() > 0){
                delBillsCharge = setDelBillsChargeTab();
                term.put("DeletedChargesTO", delBillsCharge);
            }
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            term.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            term.put("BillsTO", getBillsTO(command));
//            term.put("BillsChargesTO", objBillsChargesTO);
            term.put("BillsChargesTO", billsCharge);
            if(BillsChrgTabSelected){
//                final ArrayList billsChargeSelect = setBillsChargeSelect();
                final ArrayList billsChargeSelect = setBillsChargeTab();
                term.put("BillsChargesTabTO", billsChargeSelect);
            }
            HashMap proxyResultMap = proxy.execute(term, map);
            setResult(getActionType());
            delBillsCharge = null;
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    public ArrayList setBillsChargeTab() {
      // ArrayList data=new  ArrayList();
       ArrayList data=tblBillsCharges.getDataArrayList();
       //data=entireBillsDataRow;
       System.out.println("Data####"+data);
        ArrayList charges = new ArrayList();
        chargeTabRow = new ArrayList();
        final int dataSize = data.size();
        for (int i=0;i<dataSize;i++){
            try{
                
                objBillsChargesTabTO = new BillsChargesTO();
                
                charges = (ArrayList)data.get(i);
                
//                String InstrumentType = (String)charges.get(0).toString().toUpperCase();
                objBillsChargesTabTO.setInstrumentType(CommonUtil.convertObjToStr(charges.get(0)));
                
//                String ChargeType = (String)charges.get(1).toString().toUpperCase();
                objBillsChargesTabTO.setChargeType(CommonUtil.convertObjToStr(charges.get(1)));
                
//                String CustCategory = (String)charges.get(2).toString().toUpperCase();
                objBillsChargesTabTO.setCustCategory(CommonUtil.convertObjToStr(charges.get(2)));
                
                String dt=CommonUtil.convertObjToStr(charges.get(3));
                System.out.println("Date####"+dt);
              // objBillsChargesTabTO.setStartDate();
                objBillsChargesTabTO.setStartDate(DateUtil.getDateMMDDYYYY(dt));
                dt=CommonUtil.convertObjToStr(charges.get(4));
                objBillsChargesTabTO.setEndDate(DateUtil.getDateMMDDYYYY(dt));
                objBillsChargesTabTO.setFromSlab(CommonUtil.convertObjToDouble(charges.get(5)));
                objBillsChargesTabTO.setToSlab(CommonUtil.convertObjToDouble(charges.get(6)));
                objBillsChargesTabTO.setCommision(CommonUtil.convertObjToDouble(charges.get(7)));
                objBillsChargesTabTO.setServiceTax(CommonUtil.convertObjToDouble(charges.get(8)));
                objBillsChargesTabTO.setFixedRate(CommonUtil.convertObjToDouble(charges.get(9)));
                objBillsChargesTabTO.setForEveryAmt(CommonUtil.convertObjToDouble(charges.get(10)));
                objBillsChargesTabTO.setRateType(CommonUtil.convertObjToStr(charges.get(11)));
                objBillsChargesTabTO.setForEveryRate(CommonUtil.convertObjToDouble(charges.get(12)));
                objBillsChargesTabTO.setStatus(CommonUtil.convertObjToStr(charges.get(13)));
                System.out.println("objBillsChargesTabTO#########"+objBillsChargesTabTO);
                chargeTabRow.add(objBillsChargesTabTO);
               
            }catch(Exception e){
                
                parseException.logException(e,true);
               
            }
        }
        
        return chargeTabRow;
    }
    
     public ArrayList setDelBillsChargeTab() {
      // ArrayList data=new  ArrayList();
       LinkedHashMap data = deletedCharges;
       System.out.println("Data####"+data);
//       if(data.size() >0 && data != null){
        LinkedHashMap charges = new LinkedHashMap();
        chargeTabRow = new ArrayList();
        final int dataSize = data.size();
        for (int i=0;i<dataSize;i++){
            try{
                
                objDelBillsChargesTabTO = new BillsChargesTO();
                
                objDelBillsChargesTabTO = (BillsChargesTO)data.get(String.valueOf(i));
                
//                String InstrumentType = (String)charges.get(0).toString().toUpperCase();
//                objDelBillsChargesTabTO.setInstrumentType(CommonUtil.convertObjToStr(charges.get(0)));
//                
////                String ChargeType = (String)charges.get(1).toString().toUpperCase();
//                objDelBillsChargesTabTO.setChargeType(CommonUtil.convertObjToStr(charges.get(1)));
//                
////                String CustCategory = (String)charges.get(2).toString().toUpperCase();
//                objDelBillsChargesTabTO.setCustCategory(CommonUtil.convertObjToStr(charges.get(2)));
//                
//                String dt=CommonUtil.convertObjToStr(charges.get(3));
//                System.out.println("Date####"+dt);
//              // objBillsChargesTabTO.setStartDate();
//                objDelBillsChargesTabTO.setStartDate(DateUtil.getDateMMDDYYYY(dt));
//                dt=CommonUtil.convertObjToStr(charges.get(4));
//                objDelBillsChargesTabTO.setEndDate(DateUtil.getDateMMDDYYYY(dt));
//                objDelBillsChargesTabTO.setFromSlab(CommonUtil.convertObjToDouble(charges.get(5)));
//                objDelBillsChargesTabTO.setToSlab(CommonUtil.convertObjToDouble(charges.get(6)));
//                objDelBillsChargesTabTO.setCommision(CommonUtil.convertObjToDouble(charges.get(7)));
//                objDelBillsChargesTabTO.setFixedRate(CommonUtil.convertObjToDouble(charges.get(8)));
//                objDelBillsChargesTabTO.setForEveryAmt(CommonUtil.convertObjToDouble(charges.get(9)));
//                objDelBillsChargesTabTO.setRateType(CommonUtil.convertObjToStr(charges.get(10)));
//                objDelBillsChargesTabTO.setForEveryRate(CommonUtil.convertObjToDouble(charges.get(11)));
//                objDelBillsChargesTabTO.setStatus(CommonUtil.convertObjToStr(charges.get(12)));
//                System.out.println("objBillsChargesTabTO#########"+objBillsChargesTabTO);
                chargeTabRow.add(objDelBillsChargesTabTO);
            
               
            }catch(Exception e){
                
                parseException.logException(e,true);
               
            }
        }
//       }
        return chargeTabRow;
    }
    
    public ArrayList setBillsChargeSelect() {
            ArrayList chargeTab = new ArrayList();
         try{
                    objBillsChargesTabSelect = new BillsChargesTO();
                    
                    objBillsChargesTabSelect.setInstrumentType(CommonUtil.convertObjToStr(billsChargeSelect.get(0)));
                objBillsChargesTabSelect.setChargeType(CommonUtil.convertObjToStr(billsChargeSelect.get(1)));
                objBillsChargesTabSelect.setCustCategory(CommonUtil.convertObjToStr(billsChargeSelect.get(2)));
                String date=CommonUtil.convertObjToStr(billsChargeSelect.get(3));
                objBillsChargesTabSelect.setStartDate(DateUtil.getDateMMDDYYYY(date));
                date=CommonUtil.convertObjToStr(billsChargeSelect.get(4));
                objBillsChargesTabSelect.setEndDate(DateUtil.getDateMMDDYYYY(date));
                objBillsChargesTabSelect.setFromSlab(CommonUtil.convertObjToDouble(billsChargeSelect.get(5)));
                objBillsChargesTabSelect.setToSlab(CommonUtil.convertObjToDouble(billsChargeSelect.get(6)));
                objBillsChargesTabSelect.setCommision(CommonUtil.convertObjToDouble(billsChargeSelect.get(7)));
                objBillsChargesTabSelect.setFixedRate(CommonUtil.convertObjToDouble(billsChargeSelect.get(8)));
                objBillsChargesTabSelect.setForEveryAmt(CommonUtil.convertObjToDouble(billsChargeSelect.get(9)));
                objBillsChargesTabSelect.setRateType(CommonUtil.convertObjToStr(billsChargeSelect.get(10)));
                objBillsChargesTabSelect.setForEveryRate(CommonUtil.convertObjToDouble(billsChargeSelect.get(11)));
                objBillsChargesTabSelect.setProdID(CommonUtil.convertObjToStr(getTxtProductId()));
                System.out.println("objBillsChargesTabSelect#########"+objBillsChargesTabSelect);
                chargeTab.add(objBillsChargesTabSelect);
         }
         catch(Exception e){
                
                parseException.logException(e,true);
               
            }
                return chargeTab;
    }
    public void saveBillsCharge(boolean tableClicked,int rowClick){
        try{
        BillsChargesTO objBillsChargesTO = setBillsChargesTO();
        if(BillsDataTO==null)
            BillsDataTO=new LinkedHashMap();
        if(entireBillsDataRow==null)
            entireBillsDataRow=new ArrayList();
       
       System.out.println(entireBillsDataRow+": *************   :"+BillsDataTO);
        if(tableClicked)
        {   
            
            entireBillsDataRow.set(rowClick, setColumnValueBillsCharge((rowClick), objBillsChargesTO));
            billsChargeSelect = (ArrayList) entireBillsDataRow.get(0);
            System.out.println(":  entire#####  :"+entireBillsDataRow.get(0));
            System.out.println(":  entireBillsDataRow&&&&&&&7&&&&&&  :"+entireBillsDataRow);
            
            BillsDataTO.put(String.valueOf(rowClick+1), objBillsChargesTO);
            
            setBillsChargeOB(objBillsChargesTO);
//             ttNotifyObservers();
//            tblBillsCharges.setDataArrayList(entireBillsDataRow, billsChargeTabTitle);
            System.out.println(":  objbilss###$$$$  :"+BillsDataTO);
            BillsChrgTabSelected = true;
                
        }else{
            entireBillsDataRow.add(setColumnValueBillsCharge(bills_Charge_No,objBillsChargesTO));
            BillsDataTO.put(String.valueOf(bills_Charge_No),objBillsChargesTO);
            bills_Charge_No++;
            System.out.println(entireBillsDataRow+":  Charrge###ARRayList   :"+BillsDataTO);
        }
        
        objBillsChargesTO=null;
//        ttNotifyObservers();
        tblBillsCharges.setDataArrayList(entireBillsDataRow, billsChargeTabTitle);
        bills_Charge_No=1;
        System.out.println(":  entireBillsDataRow  :"+entireBillsDataRow);
        ttNotifyObservers();
        }catch(Exception e){
            e.printStackTrace();
        }
    
    }
    public ArrayList setColumnValueBillsCharge(int rowClicked,BillsChargesTO objBillsChargesTO){
        ArrayList row =new ArrayList();
        System.out.println("objBillsChargesTO.getChargeType()"+objBillsChargesTO.getChargeType());
//        row.add((String) getCbmInstrumentType().getDataForKey(objBillsChargesTO.getInstrumentType()));
//        row.add((String) getCbmChargeType().getDataForKey(objBillsChargesTO.getChargeType()));
//         CommonUtil.convertObjToStr(getCbmChargeType().getKeyForSelected());
//        row.add((String) getCbmCustCategory().getDataForKey(objBillsChargesTO.getCustCategory()));
//        row.add(objBillsChargesTO.getInstrumentType());
//        row.add(objBillsChargesTO.getChargeType());
//        row.add(objBillsChargesTO.getCustCategory());
        if(getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            row.add(CommonUtil.convertObjToStr(objBillsChargesTO.getInstrumentType()));
            row.add(CommonUtil.convertObjToStr(objBillsChargesTO.getChargeType()));
            row.add(CommonUtil.convertObjToStr(objBillsChargesTO.getCustCategory()));
        }else{
         row.add(CommonUtil.convertObjToStr(getCbmInstrumentType().getKeyForSelected()));
         row.add(CommonUtil.convertObjToStr(getCbmChargeType().getKeyForSelected()));
         row.add(CommonUtil.convertObjToStr(getCbmCustCategory().getKeyForSelected()));
        }
        row.add(CommonUtil.convertObjToStr(objBillsChargesTO.getStartDate()));
        row.add(CommonUtil.convertObjToStr(objBillsChargesTO.getEndDate()));
        row.add(CommonUtil.convertObjToStr(objBillsChargesTO.getFromSlab()));
        row.add(CommonUtil.convertObjToStr(objBillsChargesTO.getToSlab()));
        row.add(CommonUtil.convertObjToStr(objBillsChargesTO.getCommision()));
        row.add(CommonUtil.convertObjToStr(objBillsChargesTO.getServiceTax()));
        row.add(CommonUtil.convertObjToStr(objBillsChargesTO.getFixedRate()));
        row.add(CommonUtil.convertObjToStr(objBillsChargesTO.getForEveryAmt()));
        row.add((String) getCbmRateType().getDataForKey(objBillsChargesTO.getRateType()));
        row.add(CommonUtil.convertObjToStr(objBillsChargesTO.getForEveryRate()));
        row.add(CommonUtil.convertObjToStr(objBillsChargesTO.getStatus()));
        
        System.out.println(":  row%%%%%%%%%%%%%%  :"+row);
        return row;
    }
        public BillsChargesTO setBillsChargesTO(){
        
        BillsChargesTO objBillsChargesTO=new  BillsChargesTO();
        objBillsChargesTO.setInstrumentType(CommonUtil.convertObjToStr(getCbmInstrumentType().getKeyForSelected()));
        objBillsChargesTO.setChargeType(CommonUtil.convertObjToStr(getCbmChargeType().getKeyForSelected()));
        objBillsChargesTO.setCustCategory(CommonUtil.convertObjToStr(getCbmCustCategory().getKeyForSelected()));
        objBillsChargesTO.setStartDate(DateUtil.getDateMMDDYYYY(getTdtStartDt()));
        objBillsChargesTO.setEndDate(DateUtil.getDateMMDDYYYY(getTdtEndDt()));
        objBillsChargesTO.setFromSlab(new Double(Double.parseDouble(getTxtFromSlab())));
        objBillsChargesTO.setToSlab(new Double(Double.parseDouble(getTxtToSlab())));
        if(!getTxtCommision().equals(""))
            objBillsChargesTO.setCommision(new Double(Double.parseDouble(getTxtCommision())));
        if(!getTxtServiceTax().equals(""))
            objBillsChargesTO.setServiceTax(new Double(Double.parseDouble(getTxtServiceTax())));
        if(!getTxtFixRate().equals(""))
            objBillsChargesTO.setFixedRate(new Double(Double.parseDouble(getTxtFixRate())));
        if(!getTxtForEvery().equals(""))
            objBillsChargesTO.setForEveryAmt(new Double(Double.parseDouble(getTxtForEvery())));
        if(!getCbmRateType().getKeyForSelected().equals(""))
            objBillsChargesTO.setRateType(CommonUtil.convertObjToStr(getCbmRateType().getKeyForSelected()));
        if(!getTxtRateVal().equals(""))
            objBillsChargesTO.setForEveryRate(new Double(Double.parseDouble(getTxtRateVal())));
        objBillsChargesTO.setStatus(getStatus());
        return objBillsChargesTO;
    }
        
     public void setBillsChargeOB(BillsChargesTO objBillsChargesTO){
        setCboInstrumentType((String) getCbmInstrumentType().getDataForKey(CommonUtil.convertObjToStr(objBillsChargesTO.getInstrumentType())));
        setCboChargeType((String) getCbmChargeType().getDataForKey(CommonUtil.convertObjToStr(objBillsChargesTO.getChargeType())));
        setCboCustCategory((String) getCbmCustCategory().getDataForKey(CommonUtil.convertObjToStr(objBillsChargesTO.getCustCategory())));
        setTdtStartDt(CommonUtil.convertObjToStr(objBillsChargesTO.getStartDate()));
        setTdtEndDt(CommonUtil.convertObjToStr(objBillsChargesTO.getEndDate()));
        setTxtFromSlab(CommonUtil.convertObjToStr(objBillsChargesTO.getFromSlab()));
        setTxtToSlab(CommonUtil.convertObjToStr(objBillsChargesTO.getToSlab()));
        setTxtCommision(CommonUtil.convertObjToStr(objBillsChargesTO.getCommision()));
        setTxtServiceTax(CommonUtil.convertObjToStr(objBillsChargesTO.getServiceTax()));
        setTxtFixRate(CommonUtil.convertObjToStr(objBillsChargesTO.getFixedRate()));
        setTxtForEvery(CommonUtil.convertObjToStr(objBillsChargesTO.getForEveryAmt()));
        setCboRateType((String) getCbmRateType().getDataForKey(CommonUtil.convertObjToStr(objBillsChargesTO.getRateType())));
        setTxtRateVal(CommonUtil.convertObjToStr(objBillsChargesTO.getForEveryRate()));
        
    }
    
     public void deleteBillsCharge(int index) {
        //log.info("deleteDocuments Invoked...");
        try {
            if (BillsDataTO != null) {
//                BillsChargesTO objBillsChargesTO = (BillsChargesTO) BillsDataTO;
//                BillsChargesTO objBillsChargesTO = (BillsChargesTO) BillsDataTO.remove(String.valueOf(index));
                BillsChargesTO objBillsChargesTO = (BillsChargesTO) BillsDataTO.get(String.valueOf(index+1));
                    if (deletedChargesTO == null)
                        deletedCharges = new LinkedHashMap();
//                        deletedCharges.toArray(String.valueOf(index), objBillsChargesTO);
                        deletedCharges.put(String.valueOf(index), objBillsChargesTO);
                    if (BillsDataTO != null) {
                        for(int i = index+1,j=BillsDataTO.size();i<=j;i++) {
                            BillsDataTO.put(String.valueOf(i),(BillsChargesTO)BillsDataTO.remove(String.valueOf((i+1))));
                        }
                    
                    }
//                  objBillsChargesTO = null;
                    deleted_Charge--;
                    // Reset table data
                    entireBillsDataRow.remove(index);
                
                    System.out.println("DeleteCharge####"+BillsDataTO);
                   /* for(int i=0,j = entireBillsDataRow.size();i<j;i++){
                        ( (ArrayList) entireBillsDataRow.get(i)).set(0,String.valueOf(i+1));
                    }*/
                    tblBillsCharges.setDataArrayList(entireBillsDataRow,billsChargeTabTitle);
                
                }
            } catch (Exception  e){
                parseException.logException(e,true);
            }
         
        }
    
     public void setSubRegType(String RegType){
       
            try{
                if(RegType.equalsIgnoreCase("CHEQUE")){
                    lookUpHash = new HashMap();
                    lookUpHash.put(CommonConstants.MAP_NAME, null);
                    final ArrayList lookup_keys = new ArrayList();
                    lookup_keys.add("BILLS_SUB_REG_CHQ");
                    lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
                    keyValue = ClientUtil.populateLookupData(lookUpHash);
                    getKeyValue((HashMap)keyValue.get("BILLS_SUB_REG_CHQ"));
                    cbmSubRegType = new ComboBoxModel(key,value);
                    this.cbmSubRegType = cbmSubRegType;
                    setChanged();
                }else if(RegType.equalsIgnoreCase("BILL")){
                    lookUpHash = new HashMap();
                    lookUpHash.put(CommonConstants.MAP_NAME, null);
                    final ArrayList lookup_keys = new ArrayList();
                    lookup_keys.add("BILLS_SUB_REG_BILL");
                    lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
                    keyValue = ClientUtil.populateLookupData(lookUpHash);
                    getKeyValue((HashMap)keyValue.get("BILLS_SUB_REG_BILL"));
                    cbmSubRegType = new ComboBoxModel(key,value);
                    this.cbmSubRegType = cbmSubRegType;
                    setChanged();
                }
                
            } catch (Exception ex) {
                ex.printStackTrace();
            }
     }
     public void resetBillsChargeValues(){
        
         setCboInstrumentType("");
         setCboChargeType("");
         setCboCustCategory("");
         setTdtStartDt("");
         setTdtEndDt("");
         setTxtFromSlab("");
         setTxtToSlab("");
         setTxtCommision("");
         setTxtServiceTax("");
         setTxtFixRate("");
         setTxtForEvery("");
         setCboRateType("");
         setTxtRateVal("");
         setStatus("");
//         ttNotifyObservers();
    }
     public void resetTable(){
        try{
            
            ArrayList data = tblBillsCharges.getDataArrayList();
            for(int i=data.size(); i>0; i--)
                tblBillsCharges.removeRow(i-1);
            setChanged();
            ttNotifyObservers();
        }catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            //log.info("Error in resetTable():");
            parseException.logException(e,true);
        }
    }
    /**
     * Getter for property cboInstrumentType.
     * @return Value of property cboInstrumentType.
     */
    public java.lang.String getCboInstrumentType() {
        return cboInstrumentType;
    }    
    
    /**
     * Setter for property cboInstrumentType.
     * @param cboInstrumentType New value of property cboInstrumentType.
     */
    public void setCboInstrumentType(java.lang.String cboInstrumentType) {
        this.cboInstrumentType = cboInstrumentType;
    }
    
    /**
     * Getter for property cboChargeType.
     * @return Value of property cboChargeType.
     */
    public java.lang.String getCboChargeType() {
        return cboChargeType;
    }
    
    /**
     * Setter for property cboChargeType.
     * @param cboChargeType New value of property cboChargeType.
     */
    public void setCboChargeType(java.lang.String cboChargeType) {
        this.cboChargeType = cboChargeType;
    }
    
    /**
     * Getter for property cboCustCategory.
     * @return Value of property cboCustCategory.
     */
    public java.lang.String getCboCustCategory() {
        return cboCustCategory;
    }
    
    /**
     * Setter for property cboCustCategory.
     * @param cboCustCategory New value of property cboCustCategory.
     */
    public void setCboCustCategory(java.lang.String cboCustCategory) {
        this.cboCustCategory = cboCustCategory;
    }
    
    /**
     * Getter for property txtFromSlab.
     * @return Value of property txtFromSlab.
     */
    public java.lang.String getTxtFromSlab() {
        return txtFromSlab;
    }
    
    /**
     * Setter for property txtFromSlab.
     * @param txtFromSlab New value of property txtFromSlab.
     */
    public void setTxtFromSlab(java.lang.String txtFromSlab) {
        this.txtFromSlab = txtFromSlab;
    }
    
    /**
     * Getter for property txtToSlab.
     * @return Value of property txtToSlab.
     */
    public java.lang.String getTxtToSlab() {
        return txtToSlab;
    }
    
    /**
     * Setter for property txtToSlab.
     * @param txtToSlab New value of property txtToSlab.
     */
    public void setTxtToSlab(java.lang.String txtToSlab) {
        this.txtToSlab = txtToSlab;
    }
    
   
    /**
     * Getter for property txtCommision.
     * @return Value of property txtCommision.
     */
    public java.lang.String getTxtCommision() {
        return txtCommision;
    }
    
    /**
     * Setter for property txtCommision.
     * @param txtCommision New value of property txtCommision.
     */
    public void setTxtCommision(java.lang.String txtCommision) {
        this.txtCommision = txtCommision;
    }
    
    /**
     * Getter for property cbmInstrumentType.
     * @return Value of property cbmInstrumentType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmInstrumentType() {
        return cbmInstrumentType;
    }
    
    /**
     * Setter for property cbmInstrumentType.
     * @param cbmInstrumentType New value of property cbmInstrumentType.
     */
    public void setCbmInstrumentType(com.see.truetransact.clientutil.ComboBoxModel cbmInstrumentType) {
        this.cbmInstrumentType = cbmInstrumentType;
    }
    
    /**
     * Getter for property cbmChargeType.
     * @return Value of property cbmChargeType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmChargeType() {
        return cbmChargeType;
    }
    
    /**
     * Setter for property cbmChargeType.
     * @param cbmChargeType New value of property cbmChargeType.
     */
    public void setCbmChargeType(com.see.truetransact.clientutil.ComboBoxModel cbmChargeType) {
        this.cbmChargeType = cbmChargeType;
    }
    
    /**
     * Getter for property cbmCustCategory.
     * @return Value of property cbmCustCategory.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCustCategory() {
        return cbmCustCategory;
    }
    
    /**
     * Setter for property cbmCustCategory.
     * @param cbmCustCategory New value of property cbmCustCategory.
     */
    public void setCbmCustCategory(com.see.truetransact.clientutil.ComboBoxModel cbmCustCategory) {
        this.cbmCustCategory = cbmCustCategory;
    }
    
    /**
     * Getter for property cboRateType.
     * @return Value of property cboRateType.
     */
    public java.lang.String getCboRateType() {
        return cboRateType;
    }
    
    /**
     * Setter for property cboRateType.
     * @param cboRateType New value of property cboRateType.
     */
    public void setCboRateType(java.lang.String cboRateType) {
        this.cboRateType = cboRateType;
    }
    
    /**
     * Getter for property cbmRateType.
     * @return Value of property cbmRateType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRateType() {
        return cbmRateType;
    }
    
    /**
     * Setter for property cbmRateType.
     * @param cbmRateType New value of property cbmRateType.
     */
    public void setCbmRateType(com.see.truetransact.clientutil.ComboBoxModel cbmRateType) {
        this.cbmRateType = cbmRateType;
    }
    
    /**
     * Getter for property tdtEndDt.
     * @return Value of property tdtEndDt.
     */
    public java.lang.String getTdtEndDt() {
        return tdtEndDt;
    }
    
    /**
     * Setter for property tdtEndDt.
     * @param tdtEndDt New value of property tdtEndDt.
     */
    public void setTdtEndDt(java.lang.String tdtEndDt) {
        this.tdtEndDt = tdtEndDt;
    }
    
    /**
     * Getter for property tdtStartDt.
     * @return Value of property tdtStartDt.
     */
    public java.lang.String getTdtStartDt() {
        return tdtStartDt;
    }
    
    /**
     * Setter for property tdtStartDt.
     * @param tdtStartDt New value of property tdtStartDt.
     */
    public void setTdtStartDt(java.lang.String tdtStartDt) {
        this.tdtStartDt = tdtStartDt;
    }
    
    /**
     * Getter for property txtFixRate.
     * @return Value of property txtFixRate.
     */
    public java.lang.String getTxtFixRate() {
        return txtFixRate;
    }
    
    /**
     * Setter for property txtFixRate.
     * @param txtFixRate New value of property txtFixRate.
     */
    public void setTxtFixRate(java.lang.String txtFixRate) {
        this.txtFixRate = txtFixRate;
    }
    
    /**
     * Getter for property txtRateVal.
     * @return Value of property txtRateVal.
     */
    public java.lang.String getTxtRateVal() {
        return txtRateVal;
    }
    
    /**
     * Setter for property txtRateVal.
     * @param txtRateVal New value of property txtRateVal.
     */
    public void setTxtRateVal(java.lang.String txtRateVal) {
        this.txtRateVal = txtRateVal;
    }
    
    /**
     * Getter for property txtForEvery.
     * @return Value of property txtForEvery.
     */
    public java.lang.String getTxtForEvery() {
        return txtForEvery;
    }
    
    /**
     * Setter for property txtForEvery.
     * @param txtForEvery New value of property txtForEvery.
     */
    public void setTxtForEvery(java.lang.String txtForEvery) {
        this.txtForEvery = txtForEvery;
    }
    
    /**
     * Getter for property status.
     * @return Value of property status.
     */
    public java.lang.String getStatus() {
        return status;
    }    
    
    /**
     * Setter for property status.
     * @param status New value of property status.
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }
    
    /**
     * Getter for property cboRegType.
     * @return Value of property cboRegType.
     */
    public java.lang.String getCboRegType() {
        return cboRegType;
    }
    
    /**
     * Setter for property cboRegType.
     * @param cboRegType New value of property cboRegType.
     */
    public void setCboRegType(java.lang.String cboRegType) {
        this.cboRegType = cboRegType;
    }
    
     /**
     * Getter for property cboRegType.
     * @return Value of property cboRegType.
     */
    public java.lang.String getCboSubRegType() {
        return cboSubRegType;
    }
    
    /**
     * Setter for property cboRegType.
     * @param cboRegType New value of property cboRegType.
     */
    public void setCboSubRegType(java.lang.String cboSubRegType) {
        this.cboSubRegType = cboSubRegType;
    }
    
    /**
     * Getter for property txtServiceTax.
     * @return Value of property txtServiceTax.
     */
    public java.lang.String getTxtServiceTax() {
        return txtServiceTax;
    }
    
    /**
     * Setter for property txtServiceTax.
     * @param txtServiceTax New value of property txtServiceTax.
     */
    public void setTxtServiceTax(java.lang.String txtServiceTax) {
        this.txtServiceTax = txtServiceTax;
    }
    
    /**
     * Getter for property txtRateForDelay.
     * @return Value of property txtRateForDelay.
     */
    public java.lang.String getTxtRateForDelay() {
        return txtRateForDelay;
    }
    
    /**
     * Setter for property txtRateForDelay.
     * @param txtRateForDelay New value of property txtRateForDelay.
     */
    public void setTxtRateForDelay(java.lang.String txtRateForDelay) {
        this.txtRateForDelay = txtRateForDelay;
    }
    
    /**
     * Getter for property txtIntDays.
     * @return Value of property txtIntDays.
     */
    public java.lang.String getTxtIntDays() {
        return txtIntDays;
    }
    
    /**
     * Setter for property txtIntDays.
     * @param txtIntDays New value of property txtIntDays.
     */
    public void setTxtIntDays(java.lang.String txtIntDays) {
        this.txtIntDays = txtIntDays;
    }
    
    /**
     * Getter for property cboIntDays.
     * @return Value of property cboIntDays.
     */
    public java.lang.String getCboIntDays() {
        return cboIntDays;
    }
    
    /**
     * Setter for property cboIntDays.
     * @param cboIntDays New value of property cboIntDays.
     */
    public void setCboIntDays(java.lang.String cboIntDays) {
        this.cboIntDays = cboIntDays;
    }
    
    /**
     * Getter for property cbmIntDays.
     * @return Value of property cbmIntDays.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmIntDays() {
        return cbmIntDays;
    }
    
    /**
     * Setter for property cbmIntDays.
     * @param cbmIntDays New value of property cbmIntDays.
     */
    public void setCbmIntDays(com.see.truetransact.clientutil.ComboBoxModel cbmIntDays) {
        this.cbmIntDays = cbmIntDays;
    }
    
    /**
     * Getter for property txtServiceTaxHd.
     * @return Value of property txtServiceTaxHd.
     */
    public java.lang.String getTxtServiceTaxHd() {
        return txtServiceTaxHd;
    }
    
    /**
     * Setter for property txtServiceTaxHd.
     * @param txtServiceTaxHd New value of property txtServiceTaxHd.
     */
    public void setTxtServiceTaxHd(java.lang.String txtServiceTaxHd) {
        this.txtServiceTaxHd = txtServiceTaxHd;
    }
    
    /**
     * Getter for property cRadio_ICC_Yes.
     * @return Value of property cRadio_ICC_Yes.
     */
    public boolean isCRadio_ICC_Yes() {
        return cRadio_ICC_Yes;
    }
    
    /**
     * Setter for property cRadio_ICC_Yes.
     * @param cRadio_ICC_Yes New value of property cRadio_ICC_Yes.
     */
    public void setCRadio_ICC_Yes(boolean cRadio_ICC_Yes) {
        this.cRadio_ICC_Yes = cRadio_ICC_Yes;
    }
    
    /**
     * Getter for property cRadio_ICC_No.
     * @return Value of property cRadio_ICC_No.
     */
    public boolean isCRadio_ICC_No() {
        return cRadio_ICC_No;
    }
    
    /**
     * Setter for property cRadio_ICC_No.
     * @param cRadio_ICC_No New value of property cRadio_ICC_No.
     */
    public void setCRadio_ICC_No(boolean cRadio_ICC_No) {
        this.cRadio_ICC_No = cRadio_ICC_No;
    }
    
    /**
     * Getter for property txtContraCrAccountHead.
     * @return Value of property txtContraCrAccountHead.
     */
    public java.lang.String getTxtContraCrAccountHead() {
        return txtContraCrAccountHead;
    }
    
    /**
     * Setter for property txtContraCrAccountHead.
     * @param txtContraCrAccountHead New value of property txtContraCrAccountHead.
     */
    public void setTxtContraCrAccountHead(java.lang.String txtContraCrAccountHead) {
        this.txtContraCrAccountHead = txtContraCrAccountHead;
    }
    
    /**
     * Getter for property txtContraDrAccountHead.
     * @return Value of property txtContraDrAccountHead.
     */
    public java.lang.String getTxtContraDrAccountHead() {
        return txtContraDrAccountHead;
    }
    
    /**
     * Setter for property txtContraDrAccountHead.
     * @param txtContraDrAccountHead New value of property txtContraDrAccountHead.
     */
    public void setTxtContraDrAccountHead(java.lang.String txtContraDrAccountHead) {
        this.txtContraDrAccountHead = txtContraDrAccountHead;
    }
    
    /**
     * Getter for property chkCollectCommFrmCustomer.
     * @return Value of property chkCollectCommFrmCustomer.
     */
    public java.lang.String getChkCollectCommFrmCustomer() {
        return chkCollectCommFrmCustomer;
    }
    
    /**
     * Setter for property chkCollectCommFrmCustomer.
     * @param chkCollectCommFrmCustomer New value of property chkCollectCommFrmCustomer.
     */
    public void setChkCollectCommFrmCustomer(java.lang.String chkCollectCommFrmCustomer) {
        this.chkCollectCommFrmCustomer = chkCollectCommFrmCustomer;
    }
    
    /**
     * Getter for property txtCollectOtherBankCommFrmCustomer.
     * @return Value of property txtCollectOtherBankCommFrmCustomer.
     */
    
    
    /**
     * Getter for property rdoGlAcHd.
     * @return Value of property rdoGlAcHd.
     */
    public boolean isRdoGlAcHd() {
        return rdoGlAcHd;
    }
    
    /**
     * Setter for property rdoGlAcHd.
     * @param rdoGlAcHd New value of property rdoGlAcHd.
     */
    public void setRdoGlAcHd(boolean rdoGlAcHd) {
        this.rdoGlAcHd = rdoGlAcHd;
    }
    
    /**
     * Getter for property rdoInvestmentAcHd.
     * @return Value of property rdoInvestmentAcHd.
     */
    public boolean isRdoInvestmentAcHd() {
        return rdoInvestmentAcHd;
    }
    
    /**
     * Setter for property rdoInvestmentAcHd.
     * @param rdoInvestmentAcHd New value of property rdoInvestmentAcHd.
     */
    public void setRdoInvestmentAcHd(boolean rdoInvestmentAcHd) {
        this.rdoInvestmentAcHd = rdoInvestmentAcHd;
    }
    
    /**
     * Getter for property txtOBCCommAcHd.
     * @return Value of property txtOBCCommAcHd.
     */
    public java.lang.String getTxtOBCCommAcHd() {
        return txtOBCCommAcHd;
    }
    
    /**
     * Setter for property txtOBCCommAcHd.
     * @param txtOBCCommAcHd New value of property txtOBCCommAcHd.
     */
    public void setTxtOBCCommAcHd(java.lang.String txtOBCCommAcHd) {
        this.txtOBCCommAcHd = txtOBCCommAcHd;
    }

    public String getTxtBankChargesAcHd() {
        return txtBankChargesAcHd;
    }

    public void setTxtBankChargesAcHd(String txtBankChargesAcHd) {
        this.txtBankChargesAcHd = txtBankChargesAcHd;
    }
    
}