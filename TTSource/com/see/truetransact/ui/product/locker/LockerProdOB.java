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

package com.see.truetransact.ui.product.locker;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.transferobject.product.locker.LockerProdTO;
import com.see.truetransact.transferobject.product.locker.LockerProdChargesTO;
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
import javax.swing.table.TableModel;
import java.util.Date;
/**
 *
 * @author
 */

public class LockerProdOB extends CObservable {
    
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
    private String txtPostageAccountHead = "";
    private String txtContractAccountHead = "";
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
    private String cboMetric = "";
    
    private String tdtFromDt = "";
    private String tdtToDt = "";
    private String txtAmt = "";
    private String txtLength = "";
    private String txtBreth = "";
    private String txtHeight = "";
    //    private String cboMetric = "";
    private String txtPenalIntrstAcHd="";
    private String txtLockerRentAcHd = "";
    private String txtLockerRentAdvAcHd = "";
    private String txtLockerSusAcHd = "";
    private String txtLockerMiscAcHd = "";
    private String txtLockerBrkAcHd = "";
    private String txtLockerServAcHd = "";
    
    private String cboCustCategory = "";
    private String cboRateType = "";
    private String txtFromSlab = "";
    private String txtToSlab = "";
    private String txtForEvery = "";
    private String txtRateVal = "";
    private String txtFixRate = "";
    private String tdtStartDt = "";
    private String tdtEndDt = "";
    private boolean rdoRefundYes=false;
    private boolean rdoRefundNo=false;
    
    private String txtCommision = "";
    private String txtServiceTax = "";
    private boolean cRadio_ICC_Yes=false;
    private boolean cRadio_ICC_No=false;
    
    private boolean rdoPenalYes=false;
    private boolean rdoPenalNo=false;
    private String txtPenalRateInterest="";
    //private String txtPenalIntrstAcHd="";
    
    private EnhancedTableModel tblBillsCharges;
    private LinkedHashMap BillsDataTO=null;//contain not deleted records
    public ArrayList entireBillsDataRow = null;
    final ArrayList billsChargeTabTitle=new ArrayList();
    java.util.ResourceBundle objLockerProdRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.product.locker.LockerProdRB", ProxyParameters.LANGUAGE);
    private LinkedHashMap deletedChargesTO = null;
    private static int deleted_Charge=1;
    private LinkedHashMap deletedCharges=null;
    public boolean BillsChrgTabSelected = false;
    
    
    private  HashMap map = null;
    private static LockerProdOB objLockerProdOB; // singleton object
    private ProxyFactory proxy = null;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger _log = Logger.getLogger(LockerProdOB.class);
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
    private ComboBoxModel cbmMetric;
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
    
    LockerProdChargesTO objBillsChargesTabTO;
    LockerProdChargesTO objDelBillsChargesTabTO;
    LockerProdChargesTO objBillsChargesTabSelect;
    private static Date currDt = null;
    /* Creates a new instance of BillsOB */
    private LockerProdOB() {
        map = new HashMap();
        map.put(CommonConstants.JNDI, "LockerProdJNDI");
        map.put(CommonConstants.HOME, "serverside.product.locker.LockerProdHome");
        map.put(CommonConstants.REMOTE, "serverside.product.locker.LockerProd");
        try {
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
            currDt = ClientUtil.getCurrentDate();
            objLockerProdOB = new LockerProdOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /* Initialising the Combobox Model*/
    public void initUIComboBoxModel(){
        //        cbmTransitPeriod = new ComboBoxModel();
        //        cbmIntDays = new ComboBoxModel();
        //        cbmOperatesLike = new ComboBoxModel();
        //        cbmInstrumentType = new ComboBoxModel();
        cbmChargeType = new ComboBoxModel();
        cbmMetric = new ComboBoxModel();
        //        cbmCustCategory = new ComboBoxModel();
        //        cbmRateType = new ComboBoxModel();
        //        cbmSubRegType = new ComboBoxModel();
        //        cbmRegType = new ComboBoxModel();
        //        cbmBaseCurrency = new ComboBoxModel();
    }
    private void setBillsChargesTab()throws Exception{
        
        billsChargeTabTitle.add(objLockerProdRB.getString("tblColumn3"));
        billsChargeTabTitle.add(objLockerProdRB.getString("tblColumn5"));
        billsChargeTabTitle.add(objLockerProdRB.getString("tblColumn6"));
        billsChargeTabTitle.add(objLockerProdRB.getString("tblColumn9"));
        billsChargeTabTitle.add(objLockerProdRB.getString("tblColumn4"));
        billsChargeTabTitle.add(objLockerProdRB.getString("tblColumn14"));
        
        
    }
    /* Filling up the the ComboBox in the UI*/
    private void fillDropdown() throws Exception{
        try{
            _log.info("Inside FillDropDown");
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();
            //            lookup_keys.add("PERIOD");
            //            lookup_keys.add("BILLS_OPERATES_LIKE");
            //            lookup_keys.add("BILLS_CHARGE_TYPE");
            lookup_keys.add("LOCKER.CHARGES");
            lookup_keys.add("LOCKER.DIMENSIONS");
            //            lookup_keys.add("BILLS_CUST_CATEGORY");
            //            lookup_keys.add("BILLS_RATE_TYPE");
            //            lookup_keys.add("LODGEMENT.INSTRUCTIONS");
            //            lookup_keys.add("CATEGORY");
            //            lookup_keys.add("FOREX.RATE_TYPE");
            //            lookup_keys.add("BILLS_REG_TYPE");
            //            lookup_keys.add("BILLS_REG_SUB_TYPE");
            //            lookup_keys.add("FOREX.CURRENCY");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            //            keyValue = ClientUtil.populateLookupData(lookUpHash);
            //            getKeyValue((HashMap)keyValue.get("PERIOD"));
            //            cbmTransitPeriod = new ComboBoxModel(key,value);
            //
            //            keyValue = ClientUtil.populateLookupData(lookUpHash);
            //            getKeyValue((HashMap)keyValue.get("BILLS_OPERATES_LIKE"));
            //            cbmOperatesLike = new ComboBoxModel(key,value);
            //
            //            keyValue = ClientUtil.populateLookupData(lookUpHash);
            //            getKeyValue((HashMap)keyValue.get("BILLS_OPERATES_LIKE"));
            //            cbmInstrumentType = new ComboBoxModel(key,value);
            
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            //            getKeyValue((HashMap)keyValue.get("BILLS_CHARGE_TYPE"));
            getKeyValue((HashMap)keyValue.get("LOCKER.CHARGES"));
            cbmChargeType = new ComboBoxModel(key,value);
            
//            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get("LOCKER.DIMENSIONS"));
            cbmMetric = new ComboBoxModel(key,value);
            //
            //            keyValue = ClientUtil.populateLookupData(lookUpHash);
            //            getKeyValue((HashMap)keyValue.get("FOREX.RATE_TYPE"));
            //            cbmRateType = new ComboBoxModel(key,value);
            //
            //            keyValue = ClientUtil.populateLookupData(lookUpHash);
            //            getKeyValue((HashMap)keyValue.get("BILLS_REG_TYPE"));
            //            cbmRegType = new ComboBoxModel(key,value);
            //
            //            keyValue = ClientUtil.populateLookupData(lookUpHash);
            //            getKeyValue((HashMap)keyValue.get("PERIOD"));
            //            cbmIntDays = new ComboBoxModel(key,value);
            
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
    
    public static LockerProdOB getInstance()throws Exception{
        return objLockerProdOB;
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
    
    // Setter method for txtContractAccountHead
    void setTxtContractAccountHead(String txtContractAccountHead){
        this.txtContractAccountHead = txtContractAccountHead;
        setChanged();
    }
    // Getter method for txtContractAccountHead
    String getTxtContractAccountHead(){
        return this.txtContractAccountHead;
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
    // Setter method for 4444
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
        setTxtLength("");
        setTxtBreth("");
        setTxtHeight("");
        setTxtLockerBrkAcHd("");
        setTxtLockerMiscAcHd("");
        setTxtLockerRentAcHd("");
        setTxtLockerSusAcHd("");
        setTxtLockerServAcHd("");
        setTxtLockerRentAdvAcHd("");
        setRdoRefundYes(false);
        setRdoRefundNo(false);
        
        setCboChargeType("");
        setCboMetric("");
        //        setCboCustCategory("");
        //        setCboRateType("");
        //        setTxtFromSlab("");
        //        setTxtToSlab("");
        //        setTxtFixRate("");
        //        setTxtForEvery("");
        //        setTxtRateVal("");
        setTxtAmt("");
        setTxtServiceTax("");
        setTdtFromDt("");
        setTdtToDt("");
        
        setRdoPenalYes(false);
        setRdoPenalNo(false);
        setTxtPenalRateInterest("");
        setTxtPenalIntrstAcHd("");
        //        setCboIntDays("");
        ttNotifyObservers();
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    /* Setting up the OB fields by using TO objects */
    private void setLockerProdTO(LockerProdTO objLockerProdTO){
        setTxtProductId(CommonUtil.convertObjToStr(objLockerProdTO.getProdId()));
        setCboMetric((String) getCbmMetric().getDataForKey(CommonUtil.convertObjToStr(objLockerProdTO.getDimMet())));
        setTxtLength(CommonUtil.convertObjToStr(objLockerProdTO.getDimLen()));
        setTxtBreth(CommonUtil.convertObjToStr(objLockerProdTO.getDimBre()));
        setTxtHeight(CommonUtil.convertObjToStr(objLockerProdTO.getDimHei()));
        
        
        //        setCboRegType((String) getCbmRegType().getDataForKey(CommonUtil.convertObjToStr(objBillsTO.getRegType())));
        ////        setCboSubRegType((String)getCbmSubRegType().getDataForKey(CommonUtil.convertObjToStr(objBillsTO.getRegSubType())));
        //        if (!CommonUtil.convertObjToStr(objBillsTO.getRegSubType()).equals("")) {
        //            setSubRegType(objBillsTO.getRegType());
        //            setCboSubRegType((String) getCbmSubRegType().getDataForKey(CommonUtil.convertObjToStr(objBillsTO.getRegSubType())));
        //        }
        setTxtLockerSusAcHd(CommonUtil.convertObjToStr(objLockerProdTO.getGlAcHd()));
        //        setTxtInterestAccountHead(CommonUtil.convertObjToStr(objLockerProdTO.getIntAcHd()));
        setTxtLockerBrkAcHd(CommonUtil.convertObjToStr(objLockerProdTO.getChrgAcHd()));
        setTxtLockerRentAdvAcHd(CommonUtil.convertObjToStr(objLockerProdTO.getRentAdvAcHd()));
        
        //        if(objBillsTO.getContraAcHdYn().equals("Y") ){
        //            setRdoContraAccountHead_Yes(true);
        //        }
        //        else{
        //            setRdoContraAccountHead_No(true);
        //        }
        //
        //        if(objBillsTO.getIntIcc().equals("Y") ){
        //            setCRadio_ICC_Yes(true);
        //        }
        //        else{
        //            setCRadio_ICC_No(true);
        //        }
        //        setTxtDDAccountHead(CommonUtil.convertObjToStr(objLockerProdTO.getDdAcHd()));
        
        //        int value = CommonUtil.convertObjToInt(objLockerProdTO.getTransPeriod());
        //        if((value/365 > 0 ) && (value%365 == 0)){
        //            setTxtTransitPeriod(CommonUtil.convertObjToStr(new Integer(value/365)));
        //            setCboTransitPeriod("Years");
        //        }else if((value/30 > 0 ) && (value%30 == 0)){
        //            setTxtTransitPeriod(CommonUtil.convertObjToStr(new Integer(value/30)));
        //            setCboTransitPeriod("Months");
        //        }else{
        //            setTxtTransitPeriod(CommonUtil.convertObjToStr(new Integer(value)));
        //            setCboTransitPeriod("Days");
        //        }
        //
        //        int value1 = CommonUtil.convertObjToInt(objLockerProdTO.getNoOfIntDays());
        //        if((value1/365 > 0 ) && (value1%365 == 0)){
        //            setTxtIntDays(CommonUtil.convertObjToStr(new Integer(value1/365)));
        //            setCboIntDays("Years");
        //        }else if((value1/30 > 0 ) && (value1%30 == 0)){
        //            setTxtIntDays(CommonUtil.convertObjToStr(new Integer(value1/30)));
        //            setCboIntDays("Months");
        //        }else{
        //            setTxtIntDays(CommonUtil.convertObjToStr(new Integer(value1)));
        //            setCboIntDays("Days");
        //        }
        //
        //        if(objLockerProdTO.getPostDtChqYn().equals("Y")){
        //            setRdoPostDtdCheqAllowed_Yes(true);
        //        }
        //        else{
        //            setRdoPostDtdCheqAllowed_No(true);
        //        }
        
        //        setCboBaseCurrency(objBillsTO.getBaseCurrency());
        //        setTxtMarginAccountHead(CommonUtil.convertObjToStr(objLockerProdTO.getMarginAcHd()));
        setTxtLockerRentAcHd(CommonUtil.convertObjToStr(objLockerProdTO.getCommAcHd()));
        setTxtLockerMiscAcHd(CommonUtil.convertObjToStr(objLockerProdTO.getOtherHd()));
        //        setTxtPostageAccountHead(CommonUtil.convertObjToStr(objLockerProdTO.getPostageAcHd()));
        //        setTxtContractAccountHead(CommonUtil.convertObjToStr(objLockerProdTO.getContraAcHd()));
        //        setTxtIBRAccountHead(CommonUtil.convertObjToStr(objLockerProdTO.getIbrAcHd()));
        setTxtLockerServAcHd(CommonUtil.convertObjToStr(objLockerProdTO.getServTaxAcHd()));
        //        setTxtAtParLimit(CommonUtil.convertObjToStr(objLockerProdTO.getAtParLimit()));
        
        
        //        setTxtBillsRealisedHead(CommonUtil.convertObjToStr(objLockerProdTO.getBillsRealisedHd()));
        setAuthorizeDt(DateUtil.getStringDate(objLockerProdTO.getAuthorizeDt()));
        setAuthorizeRemarks( CommonUtil.convertObjToStr(objLockerProdTO.getAuthorizeRemark()));
        //        setAuthorizeStatus1(CommonUtil.convertObjToStr(objBillsTO.getAuthorizeStatus()));
        setAuthorizeUser(CommonUtil.convertObjToStr(objLockerProdTO.getAuthorizeUser()));
        setCreatedBy(CommonUtil.convertObjToStr(objLockerProdTO.getCreatedBy()));
        setCreatedDt(DateUtil.getStringDate(objLockerProdTO.getCreatedDt()));
        //
        //        setTxtDDAccountHead(CommonUtil.convertObjToStr(objBillsTO.getDdAcHd()));
        //        setTxtDiscountRateBills(CommonUtil.convertObjToStr(objBillsTO.getDiscountRateBd()));
        //        setTxtInterestAccountHead(CommonUtil.convertObjToStr(objBillsTO.getIntAcHd()));
        //        setTxtRateForCBP(CommonUtil.convertObjToStr(objBillsTO.getInterestRateCbp()));
        //        setTxtDefaultPostage(CommonUtil.convertObjToStr(objBillsTO.getPostageRate()));
        //        setTxtRateForDelay(CommonUtil.convertObjToStr(objBillsTO.getRateForDelay()));
        setTxtProdDesc(CommonUtil.convertObjToStr(objLockerProdTO.getProdDesc()));
        //        setTxtOthersHead(CommonUtil.convertObjToStr(objBillsTO.getOtherHd()));
        //        setTxtOverdueRateBills(CommonUtil.convertObjToStr(objBillsTO.getOverdueRateBd()));
        //        setTxtCleanBills(CommonUtil.convertObjToStr(objBillsTO.getOverdueRateCbp()));
        //        setBillStatus(CommonUtil.convertObjToStr(objBillsTO.getStatus()));
        setStatusBy1(CommonUtil.convertObjToStr(objLockerProdTO.getStatusBy()));
        setStatusDt(CommonUtil.convertObjToStr(DateUtil.getStringDate(objLockerProdTO.getStatusDt())));
        setTxtPenalRateInterest(CommonUtil.convertObjToStr(objLockerProdTO.getPenalRate()));
        setTxtPenalIntrstAcHd(CommonUtil.convertObjToStr(objLockerProdTO.getPenalAcHd()));
        //        setTxtTelChargesHead(CommonUtil.convertObjToStr(objBillsTO.getTelephoneChrgHd()));
        if(objLockerProdTO.getRdoRefund()!=null){
            if(objLockerProdTO.getRdoRefund().equalsIgnoreCase("Y")){
                setRdoRefundYes(true);
                setRdoRefundNo(false);
            } else{
                setRdoRefundYes(false);
                setRdoRefundNo(true);
            }
        }
        if(objLockerProdTO.getRdoPenal()!=null){
            if(objLockerProdTO.getRdoPenal().equalsIgnoreCase("Y")){
                setRdoPenalYes(true);
                setRdoPenalNo(false);
            } else
            {
                setRdoPenalYes(false);
                setRdoPenalNo(true);
                
            }
        }
        ttNotifyObservers();
        
    }
    
    /* Returns a TO object */
    public LockerProdTO getLockerProdTO(String command){
        LockerProdTO objLockerProdTO = new LockerProdTO();
        //        final String yes="Y";
        //        final String no="N";
        
        objLockerProdTO.setCommand(command);
        objLockerProdTO.setDimLen(CommonUtil.convertObjToInt(getTxtLength()));
        objLockerProdTO.setDimBre(CommonUtil.convertObjToInt(getTxtBreth()));
        objLockerProdTO.setDimHei(CommonUtil.convertObjToInt(getTxtHeight()));
        objLockerProdTO.setProdId(CommonUtil.convertObjToStr(getTxtProductId()));
        
        objLockerProdTO.setDimMet(CommonUtil.convertObjToStr((String)cbmMetric.getKeyForSelected()));
        //        objLockerProdTO.setRegType(CommonUtil.convertObjToStr((String)cbmRegType.getKeyForSelected()));
        //        objLockerProdTO.setRegSubType(CommonUtil.convertObjToStr((String)cbmSubRegType.getKeyForSelected()));
        objLockerProdTO.setGlAcHd(CommonUtil.convertObjToStr(getTxtLockerSusAcHd()));
        objLockerProdTO.setRentAdvAcHd(CommonUtil.convertObjToStr(getTxtLockerRentAdvAcHd()));
        //        objLockerProdTO.setIntAcHd(CommonUtil.convertObjToStr(getTxtInterestAccountHead()));
        objLockerProdTO.setChrgAcHd(CommonUtil.convertObjToStr(getTxtLockerBrkAcHd()));
        if(isRdoRefundYes()==true) {
            objLockerProdTO.setRdoRefund("Y");
        } else{
            objLockerProdTO.setRdoRefund("N");
         }
        
        //        if(getRdoContraAccountHead_Yes())
        //            objLockerProdTO.setContraAcHdYn(yes);
        //        else
        //            objLockerProdTO.setContraAcHdYn(no);
        //
        //        if(isCRadio_ICC_Yes())
        //            objLockerProdTO.setIntIcc(yes);
        //        else
        //            objLockerProdTO.setIntIcc(no);
        
        //        objLockerProdTO.setDdAcHd(CommonUtil.convertObjToStr(getTxtDDAccountHead()));
        
        //Getting the Transit period
        //        int time = CommonUtil.convertObjToInt(CommonUtil.convertObjToStr(getTxtTransitPeriod()));
        //        String timeUnit = getCboTransitPeriod();
        //        if(timeUnit.equals("Years")){
        //            //If Years is selected in the CboTransitPeriod in the UI
        //            objLockerProdTO.setTransPeriod(new Double(time * 365));
        //        }else if(timeUnit.equals("Months")){
        //            //If Months is selected in the cboTransitPeriod in the UI
        //            objLockerProdTO.setTransPeriod(new Double(time * 30));
        //        }else{
        //            //If Days is selected in the cboTransitPeriod in the UI
        //            objLockerProdTO.setTransPeriod(new Double(time * 1));
        //        }
        
        //         int time1 = CommonUtil.convertObjToInt(CommonUtil.convertObjToStr(getTxtIntDays()));
        //        String timeUnit1 = getCboIntDays();
        //        if(timeUnit1.equals("Years")){
        //            //If Years is selected in the CboTransitPeriod in the UI
        //            objLockerProdTO.setNoOfIntDays(new Double(time1 * 365));
        //        }else if(timeUnit1.equals("Months")){
        //            //If Months is selected in the cboTransitPeriod in the UI
        //            objLockerProdTO.setNoOfIntDays(new Double(time1 * 30));
        //        }else{
        //            //If Days is selected in the cboTransitPeriod in the UI
        //            objLockerProdTO.setNoOfIntDays(new Double(time1 * 1));
        //        }
        
        //        if( getRdoPostDtdCheqAllowed_Yes())
        //            objLockerProdTO.setPostDtChqYn(yes);
        //        else
        //            objLockerProdTO.setPostDtChqYn(no);
        
        //        objBillsTO.setBaseCurrency(CommonUtil.convertObjToStr(getCboBaseCurrency()));
        //        objLockerProdTO.setMarginAcHd(CommonUtil.convertObjToStr(getTxtMarginAccountHead()));
        objLockerProdTO.setCommAcHd(CommonUtil.convertObjToStr(getTxtLockerRentAcHd()));
        //        objLockerProdTO.setPostageAcHd(CommonUtil.convertObjToStr(getTxtPostageAccountHead()));
        //        objLockerProdTO.setContraAcHd(CommonUtil.convertObjToStr(getTxtContractAccountHead()));
        //        objLockerProdTO.setIbrAcHd(CommonUtil.convertObjToStr(getTxtIBRAccountHead()));
        objLockerProdTO.setServTaxAcHd(CommonUtil.convertObjToStr(getTxtLockerServAcHd()));
        //        objLockerProdTO.setAtParLimit(CommonUtil.convertObjToStr(getTxtAtParLimit()));
        
        //        objLockerProdTO.setBillsRealisedHd(CommonUtil.convertObjToStr(getTxtBillsRealisedHead()));
        objLockerProdTO.setCreatedBy(CommonUtil.convertObjToStr(getCreatedBy()));
        objLockerProdTO.setCreatedDt(currDt);
        //        objBillsTO.setDdAcHd(CommonUtil.convertObjToStr(getTxtDDAccountHead()));
        //        objBillsTO.setDiscountRateBd(CommonUtil.convertObjToDouble(getTxtDiscountRateBills()));
        //        objBillsTO.setIntAcHd(CommonUtil.convertObjToStr(getTxtInterestAccountHead()));
        //        objBillsTO.setInterestRateCbp(CommonUtil.convertObjToDouble(getTxtRateForCBP()));
        //        objBillsTO.setPostageRate(CommonUtil.convertObjToDouble(getTxtDefaultPostage()));
        //        objBillsTO.setRateForDelay(CommonUtil.convertObjToDouble(getTxtRateForDelay()));
        objLockerProdTO.setProdDesc(CommonUtil.convertObjToStr(getTxtProdDesc()));
        objLockerProdTO.setOtherHd(CommonUtil.convertObjToStr(getTxtLockerMiscAcHd()));
        //        objBillsTO.setOverdueRateBd(CommonUtil.convertObjToDouble(getTxtOverdueRateBills()));
        //        objBillsTO.setOverdueRateCbp(CommonUtil.convertObjToDouble(getTxtCleanBills()));
        objLockerProdTO.setStatus(CommonUtil.convertObjToStr(getBillStatus()));
        objLockerProdTO.setStatusBy(CommonUtil.convertObjToStr(getStatusBy()));
        objLockerProdTO.setStatusDt(currDt);
        //        objBillsTO.setTelephoneChrgHd(CommonUtil.convertObjToStr(getTxtTelChargesHead()));
        objLockerProdTO.setStatus(CommonConstants.STATUS_CREATED);
        objLockerProdTO.setPenalAcHd(CommonUtil.convertObjToStr(getTxtPenalIntrstAcHd()));
        objLockerProdTO.setPenalRate(CommonUtil.convertObjToDouble(getTxtPenalRateInterest()));
        if(isRdoPenalYes()==true) {
            objLockerProdTO.setRdoPenal("Y");
        } 
        else
        {
            objLockerProdTO.setRdoPenal("N");
         }
        
        return objLockerProdTO;
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
            System.out.println("mapdata=========="+mapData);
            LockerProdTO objLockerProdTO = (LockerProdTO) ((List) mapData.get("LockerProdTO")).get(0);
            //            BillsChargesTO objChargeTO=(BillsChargesTO)((List)mapData.get("BillsChargesTO")).get(0);
            
            setLockerProdTO(objLockerProdTO);
            populateOB(mapData);
            
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            
        }
    }
    private void populateOB(HashMap mapData) throws Exception{
        LockerProdChargesTO objBillsChargesTO = null;
        ArrayList arrayBillsChargesTabTO = null;
        System.out.print("mapdata$$$$#####"+mapData);
        List lstData = (List)mapData.get("LockerProdChargesTO");
        //        for(int i=0; i < lstData.size(); i++){
        //            objBillsChargesTO = (LockerProdChargesTO) ((List) mapData.get("LockerProdChargesTO")).get(i);
        //        }
        //        setBillsChargesTO(objBillsChargesTO);
        
        arrayBillsChargesTabTO =  (ArrayList) (mapData.get("LockerProdChargesTO"));
        setBillsChargesTabTO(arrayBillsChargesTabTO);
        System.out.print("arrayBillsChargesTabTO$$$$#####"+arrayBillsChargesTabTO);
        
        //        ttNotifyObservers();
    }
    
    private void setBillsChargesTabTO(ArrayList arrayBillsChargesTO) throws Exception{
        //log.info("In setBillsChargesTO...");
        System.out.println("setBillsCharges###"+arrayBillsChargesTO);
        LockerProdChargesTO objBillsChargesTO= new LockerProdChargesTO();
        BillsDataTO=new LinkedHashMap();
        entireBillsDataRow=new ArrayList();
        for (int i=0, j= arrayBillsChargesTO.size();i<j;i++){
            chargeTabRow = new ArrayList();
            objBillsChargesTO = (LockerProdChargesTO)arrayBillsChargesTO.get(i);
            BillsDataTO.put(String.valueOf(bills_Charge_No),objBillsChargesTO);
            entireBillsDataRow.add(setColumnValueBillsCharge(bills_Charge_No, objBillsChargesTO));
            bills_Charge_No++;
            
            
            objBillsChargesTO = (LockerProdChargesTO)arrayBillsChargesTO.get(i);
            //            chargeTabRow.add(CommonUtil.convertObjToStr(objBillsChargesTO.getInstrumentType()));
            chargeTabRow.add(CommonUtil.convertObjToStr(objBillsChargesTO.getChargeType()));
            //            chargeTabRow.add(CommonUtil.convertObjToStr(objBillsChargesTO.getCustCategory()));
            chargeTabRow.add(CommonUtil.convertObjToStr(objBillsChargesTO.getStartDate()));
            chargeTabRow.add(CommonUtil.convertObjToStr(objBillsChargesTO.getEndDate()));
            //            chargeTabRow.add(CommonUtil.convertObjToDouble(objBillsChargesTO.getFromSlab()));
            //            chargeTabRow.add(CommonUtil.convertObjToDouble(objBillsChargesTO.getToSlab()));
            chargeTabRow.add(CommonUtil.convertObjToDouble(objBillsChargesTO.getCommision()));
            chargeTabRow.add(CommonUtil.convertObjToDouble(objBillsChargesTO.getServiceTax()));
            //            chargeTabRow.add(CommonUtil.convertObjToDouble(objBillsChargesTO.getFixedRate()));
            //            chargeTabRow.add(CommonUtil.convertObjToDouble(objBillsChargesTO.getForEveryAmt()));
            //            chargeTabRow.add(CommonUtil.convertObjToStr(objBillsChargesTO.getRateType()));
            //            chargeTabRow.add(CommonUtil.convertObjToDouble(objBillsChargesTO.getForEveryRate()));
            chargeTabRow.add(CommonUtil.convertObjToStr(objBillsChargesTO.getAuthorizeStatus()));
            
            objBillsChargesTO = null;
            //entireBillsDataRow.add(objBillsChargesTO);
            tblBillsCharges.insertRow(i,chargeTabRow);
            //
        }
        bills_Charge_No=1;
    }
    private void setBillsChargesTO(LockerProdChargesTO objBillsChargesTO) throws Exception{
        //log.info("In setLoanProductChargesTO...");
        //        setCboInstrumentType((String) getCbmInstrumentType().getDataForKey(CommonUtil.convertObjToStr(objBillsChargesTO.getInstrumentType())));
        setCboChargeType((String) getCbmChargeType().getDataForKey(CommonUtil.convertObjToStr(objBillsChargesTO.getChargeType())));
        //        setCboCustCategory((String) getCbmCustCategory().getDataForKey(CommonUtil.convertObjToStr(objBillsChargesTO.getCustCategory())));
        setTdtFromDt(CommonUtil.convertObjToStr(objBillsChargesTO.getStartDate()));
        setTdtToDt(CommonUtil.convertObjToStr(objBillsChargesTO.getEndDate()));
        //        setTxtFromSlab(CommonUtil.convertObjToStr(objBillsChargesTO.getFromSlab()));
        //        setTxtToSlab(CommonUtil.convertObjToStr(objBillsChargesTO.getToSlab()));
        setTxtAmt(CommonUtil.convertObjToStr(objBillsChargesTO.getCommision()));
        setTxtServiceTax(CommonUtil.convertObjToStr(objBillsChargesTO.getServiceTax()));
        //        setTxtFixRate(CommonUtil.convertObjToStr(objBillsChargesTO.getFixedRate()));
        //        setTxtForEvery(CommonUtil.convertObjToStr(objBillsChargesTO.getForEveryAmt()));
        //        setCboRateType((String) getCbmRateType().getDataForKey(CommonUtil.convertObjToStr(objBillsChargesTO.getRateType())));
        //        setTxtRateVal(CommonUtil.convertObjToStr(objBillsChargesTO.getForEveryRate()));
        setAuthorizeStatus(CommonUtil.convertObjToStr(objBillsChargesTO.getAuthorizeStatus()));
        setAuthorizeDt(DateUtil.getStringDate(objBillsChargesTO.getAuthorizeDt()));
        
        setAuthorizeUser(CommonUtil.convertObjToStr(objBillsChargesTO.getAuthorizeUser()));
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
        //        setCboInstrumentType(CommonUtil.convertObjToStr(getCbmInstrumentType().getDataForKey(BillsCharge.get(0))));
        //        String Char = (String)BillsCharge.get(1).toString().toUpperCase();
        //        setCboChargeType(CommonUtil.convertObjToStr(BillsCharge.get(1)));
        setCboChargeType(CommonUtil.convertObjToStr(getCbmChargeType().getDataForKey(BillsCharge.get(0))));
        //        String Cust = (String)BillsCharge.get(2).toString().toUpperCase();
        //        setCboCustCategory(CommonUtil.convertObjToStr(getCbmCustCategory().getDataForKey(BillsCharge.get(2))));
        //        setCboCustCategory(CommonUtil.convertObjToStr(BillsCharge.get(2)));
        setTdtFromDt(CommonUtil.convertObjToStr(BillsCharge.get(1)));
        setTdtToDt(CommonUtil.convertObjToStr(BillsCharge.get(2)));
        setTxtAmt(CommonUtil.convertObjToStr(BillsCharge.get(3)));
        //        setTxtToSlab(CommonUtil.convertObjToStr(BillsCharge.get(6)));
        //        setTxtCommision(CommonUtil.convertObjToStr(BillsCharge.get(7)));
        setTxtServiceTax(CommonUtil.convertObjToStr(BillsCharge.get(4)));
        setAuthorizeStatus(CommonUtil.convertObjToStr(BillsCharge.get(5)));
        //        setTxtFixRate(CommonUtil.convertObjToStr(BillsCharge.get(9)));
        //        setTxtForEvery(CommonUtil.convertObjToStr(BillsCharge.get(10)));
        //        setCboRateType(CommonUtil.convertObjToStr(BillsCharge.get(11)));
        //        setTxtRateVal(CommonUtil.convertObjToStr(BillsCharge.get(12)));
        //        setStatus(CommonUtil.convertObjToStr(BillsCharge.get(6)));
        setChanged();
        ttNotifyObservers();
    }
    public boolean isProductIdAvailable(String productId) {
        boolean exist = false;
        try {
            if (productId != null && productId.length()>0) {
                HashMap where = new HashMap();
                where.put("PROD_ID", productId);
                List list = (List) ClientUtil.executeQuery("countLockerProd", where);
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
            final LockerProdChargesTO objLockerProdChargesTO;
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
            term.put("LockerProdTO", getLockerProdTO(command));
            //            term.put("BillsChargesTO", objBillsChargesTO);
            term.put("LockerProdChargesTO", billsCharge);
            if(BillsChrgTabSelected){
                //                final ArrayList billsChargeSelect = setBillsChargeSelect();
                final ArrayList billsChargeSelect = setBillsChargeTab();
                term.put("LockerProdChargesTabTO", billsChargeSelect);
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
                
                objBillsChargesTabTO = new LockerProdChargesTO();
                
                charges = (ArrayList)data.get(i);
                
                //                String InstrumentType = (String)charges.get(0).toString().toUpperCase();
                objBillsChargesTabTO.setChargeType(CommonUtil.convertObjToStr(charges.get(0)));
                
                //                String ChargeType = (String)charges.get(1).toString().toUpperCase();
                //                objBillsChargesTabTO.setChargeType(CommonUtil.convertObjToStr(charges.get(1)));
                
                //                String CustCategory = (String)charges.get(2).toString().toUpperCase();
                //                objBillsChargesTabTO.setCustCategory(CommonUtil.convertObjToStr(charges.get(2)));
                
                String dt=CommonUtil.convertObjToStr(charges.get(1));
                System.out.println("Date####"+dt);
                // objBillsChargesTabTO.setStartDate();
                objBillsChargesTabTO.setStartDate(DateUtil.getDateMMDDYYYY(dt));
                dt=CommonUtil.convertObjToStr(charges.get(2));
                objBillsChargesTabTO.setEndDate(DateUtil.getDateMMDDYYYY(dt));
                //                objBillsChargesTabTO.setFromSlab(CommonUtil.convertObjToDouble(charges.get(5)));
                //                objBillsChargesTabTO.setToSlab(CommonUtil.convertObjToDouble(charges.get(6)));
                objBillsChargesTabTO.setCommision(CommonUtil.convertObjToDouble(charges.get(3)));
                objBillsChargesTabTO.setServiceTax(CommonUtil.convertObjToDouble(charges.get(4)));
                //                objBillsChargesTabTO.setFixedRate(CommonUtil.convertObjToDouble(charges.get(9)));
                //                objBillsChargesTabTO.setForEveryAmt(CommonUtil.convertObjToDouble(charges.get(10)));
                //                objBillsChargesTabTO.setRateType(CommonUtil.convertObjToStr(charges.get(11)));
                //                objBillsChargesTabTO.setForEveryRate(CommonUtil.convertObjToDouble(charges.get(12)));
                objBillsChargesTabTO.setAuthorizeStatus(CommonUtil.convertObjToStr(charges.get(5)));
                
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
                
                objDelBillsChargesTabTO = new LockerProdChargesTO();
                
                objDelBillsChargesTabTO = (LockerProdChargesTO)data.get(String.valueOf(i));
                
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
            objBillsChargesTabSelect = new LockerProdChargesTO();
            
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
            LockerProdChargesTO objBillsChargesTO = setBillsChargesTO();
            if(BillsDataTO==null)
                BillsDataTO=new LinkedHashMap();
            if(entireBillsDataRow==null)
                entireBillsDataRow=new ArrayList();
            
            System.out.println(entireBillsDataRow+": *************   :"+BillsDataTO);
            if(tableClicked) {
                
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
    public ArrayList setColumnValueBillsCharge(int rowClicked,LockerProdChargesTO objBillsChargesTO){
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
            //            row.add(CommonUtil.convertObjToStr(objBillsChargesTO.getInstrumentType()));
            row.add(CommonUtil.convertObjToStr(objBillsChargesTO.getChargeType()));
            //            row.add(CommonUtil.convertObjToStr(objBillsChargesTO.getCustCategory()));
        }else{
            //         row.add(CommonUtil.convertObjToStr(getCbmInstrumentType().getKeyForSelected()));
            row.add(CommonUtil.convertObjToStr(getCbmChargeType().getKeyForSelected()));
            //         row.add(CommonUtil.convertObjToStr(getCbmCustCategory().getKeyForSelected()));
        }
        row.add(CommonUtil.convertObjToStr(objBillsChargesTO.getStartDate()));
        row.add(CommonUtil.convertObjToStr(objBillsChargesTO.getEndDate()));
        //        row.add(CommonUtil.convertObjToStr(objBillsChargesTO.getFromSlab()));
        //        row.add(CommonUtil.convertObjToStr(objBillsChargesTO.getToSlab()));
        row.add(CommonUtil.convertObjToStr(objBillsChargesTO.getCommision()));
        row.add(CommonUtil.convertObjToStr(objBillsChargesTO.getServiceTax()));
        //        row.add(CommonUtil.convertObjToStr(objBillsChargesTO.getFixedRate()));
        //        row.add(CommonUtil.convertObjToStr(objBillsChargesTO.getForEveryAmt()));
        //        row.add((String) getCbmRateType().getDataForKey(objBillsChargesTO.getRateType()));
        //        row.add(CommonUtil.convertObjToStr(objBillsChargesTO.getForEveryRate()));
        row.add(CommonUtil.convertObjToStr(objBillsChargesTO.getAuthorizeStatus()));
        
        System.out.println(":  row%%%%%%%%%%%%%%  :"+row);
        return row;
    }
    public LockerProdChargesTO setBillsChargesTO(){
        
        LockerProdChargesTO objBillsChargesTO=new  LockerProdChargesTO();
        //        objBillsChargesTO.setInstrumentType(CommonUtil.convertObjToStr(getCbmInstrumentType().getKeyForSelected()));
        objBillsChargesTO.setChargeType(CommonUtil.convertObjToStr(getCbmChargeType().getKeyForSelected()));
        //        objBillsChargesTO.setCustCategory(CommonUtil.convertObjToStr(getCbmCustCategory().getKeyForSelected()));
        objBillsChargesTO.setStartDate(DateUtil.getDateMMDDYYYY(getTdtFromDt()));
        objBillsChargesTO.setEndDate(DateUtil.getDateMMDDYYYY(getTdtToDt()));
        //        objBillsChargesTO.setFromSlab(new Double(Double.parseDouble(getTxtFromSlab())));
        //        objBillsChargesTO.setToSlab(new Double(Double.parseDouble(getTxtToSlab())));
        if(!getTxtAmt().equals(""))
            objBillsChargesTO.setCommision(new Double(Double.parseDouble(getTxtAmt())));
        if(!getTxtServiceTax().equals(""))
            objBillsChargesTO.setServiceTax(new Double(Double.parseDouble(getTxtServiceTax())));
        //        if(!getTxtFixRate().equals(""))
        //            objBillsChargesTO.setFixedRate(new Double(Double.parseDouble(getTxtFixRate())));
        //        if(!getTxtForEvery().equals(""))
        //            objBillsChargesTO.setForEveryAmt(new Double(Double.parseDouble(getTxtForEvery())));
        //        if(!getCbmRateType().getKeyForSelected().equals(""))
        //            objBillsChargesTO.setRateType(CommonUtil.convertObjToStr(getCbmRateType().getKeyForSelected()));
        //        if(!getTxtRateVal().equals(""))
        //            objBillsChargesTO.setForEveryRate(new Double(Double.parseDouble(getTxtRateVal())));
        
        
        setAuthorizeUser(CommonUtil.convertObjToStr(objBillsChargesTO.getAuthorizeUser()));
        //objBillsChargesTO.setAuthorizeDt(getAuthorizeDt());
        
        return objBillsChargesTO;
    }
    
    public void setBillsChargeOB(LockerProdChargesTO objBillsChargesTO){
        //        setCboInstrumentType((String) getCbmInstrumentType().getDataForKey(CommonUtil.convertObjToStr(objBillsChargesTO.getInstrumentType())));
        setCboChargeType((String) getCbmChargeType().getDataForKey(CommonUtil.convertObjToStr(objBillsChargesTO.getChargeType())));
        //        setCboCustCategory((String) getCbmCustCategory().getDataForKey(CommonUtil.convertObjToStr(objBillsChargesTO.getCustCategory())));
        setTdtFromDt(CommonUtil.convertObjToStr(objBillsChargesTO.getStartDate()));
        setTdtToDt(CommonUtil.convertObjToStr(objBillsChargesTO.getEndDate()));
        //        setTxtFromSlab(CommonUtil.convertObjToStr(objBillsChargesTO.getFromSlab()));
        //        setTxtToSlab(CommonUtil.convertObjToStr(objBillsChargesTO.getToSlab()));
        setTxtAmt(CommonUtil.convertObjToStr(objBillsChargesTO.getCommision()));
        setTxtServiceTax(CommonUtil.convertObjToStr(objBillsChargesTO.getServiceTax()));
        //        setTxtFixRate(CommonUtil.convertObjToStr(objBillsChargesTO.getFixedRate()));
        //        setTxtForEvery(CommonUtil.convertObjToStr(objBillsChargesTO.getForEveryAmt()));
        //        setCboRateType((String) getCbmRateType().getDataForKey(CommonUtil.convertObjToStr(objBillsChargesTO.getRateType())));
        //        setTxtRateVal(CommonUtil.convertObjToStr(objBillsChargesTO.getForEveryRate()));
        
    }
    
    public void deleteBillsCharge(int index) {
        //log.info("deleteDocuments Invoked...");
        try {
            if (BillsDataTO != null) {
                //                BillsChargesTO objBillsChargesTO = (BillsChargesTO) BillsDataTO;
                //                BillsChargesTO objBillsChargesTO = (BillsChargesTO) BillsDataTO.remove(String.valueOf(index));
                LockerProdChargesTO objBillsChargesTO = (LockerProdChargesTO) BillsDataTO.get(String.valueOf(index+1));
                if (deletedChargesTO == null)
                    deletedCharges = new LinkedHashMap();
                //                        deletedCharges.toArray(String.valueOf(index), objBillsChargesTO);
                if (objBillsChargesTO!=null) {
                    deletedCharges.put(String.valueOf(deletedCharges.size()), objBillsChargesTO);
                }
                if (BillsDataTO != null) {
                    for(int i = index+1,j=BillsDataTO.size();i<=j;i++) {
                        BillsDataTO.put(String.valueOf(i),(LockerProdChargesTO)BillsDataTO.remove(String.valueOf((i+1))));
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
        
        //         setCboInstrumentType("");
        setCboChargeType("");
        //         setCboCustCategory("");
        setTdtFromDt("");
        setTdtToDt("");
        //         setTxtFromSlab("");
        //         setTxtToSlab("");
        setTxtAmt("");
        setTxtServiceTax("");
        //         setTxtFixRate("");
        //         setTxtForEvery("");
        //         setCboRateType("");
        //         setTxtRateVal("");
        setAuthorizeStatus("");
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
            // log.info("Error in resetTable():");
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
    public boolean isRdoRefundYes() {
        return rdoRefundYes;
    }
    
    /**
     * Setter for property cboIntDays.
     * @param cboIntDays New value of property cboIntDays.
     */
    public void setRdoRefundYes(boolean rdoRefundYes) {
        this.rdoRefundYes = rdoRefundYes;
    }
    public boolean isRdoRefundNo() {
        return rdoRefundNo;
    }
    
    /**
     * Setter for property cboIntDays.
     * @param cboIntDays New value of property cboIntDays.
     */
    public void setRdoRefundNo(boolean rdoRefundNo) {
        this.rdoRefundNo = rdoRefundNo;
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
     * Getter for property cboMetric.
     * @return Value of property cboMetric.
     */
    public java.lang.String getCboMetric() {
        return cboMetric;
    }
    
    /**
     * Setter for property cboMetric.
     * @param cboMetric New value of property cboMetric.
     */
    public void setCboMetric(java.lang.String cboMetric) {
        this.cboMetric = cboMetric;
    }
    
    /**
     * Getter for property cbmMetric.
     * @return Value of property cbmMetric.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmMetric() {
        return cbmMetric;
    }
    
    /**
     * Setter for property cbmMetric.
     * @param cbmMetric New value of property cbmMetric.
     */
    public void setCbmMetric(com.see.truetransact.clientutil.ComboBoxModel cbmMetric) {
        this.cbmMetric = cbmMetric;
    }
    
    /**
     * Getter for property tdtFromDt.
     * @return Value of property tdtFromDt.
     */
    public java.lang.String getTdtFromDt() {
        return tdtFromDt;
    }
    
    /**
     * Setter for property tdtFromDt.
     * @param tdtFromDt New value of property tdtFromDt.
     */
    public void setTdtFromDt(java.lang.String tdtFromDt) {
        this.tdtFromDt = tdtFromDt;
    }
    
    /**
     * Getter for property tdtToDt.
     * @return Value of property tdtToDt.
     */
    public java.lang.String getTdtToDt() {
        return tdtToDt;
    }
    
    /**
     * Setter for property tdtToDt.
     * @param tdtToDt New value of property tdtToDt.
     */
    public void setTdtToDt(java.lang.String tdtToDt) {
        this.tdtToDt = tdtToDt;
    }
    
    /**
     * Getter for property txtAmt.
     * @return Value of property txtAmt.
     */
    public java.lang.String getTxtAmt() {
        return txtAmt;
    }
    
    /**
     * Setter for property txtAmt.
     * @param txtAmt New value of property txtAmt.
     */
    public void setTxtAmt(java.lang.String txtAmt) {
        this.txtAmt = txtAmt;
    }
    
    /**
     * Getter for property txtLength.
     * @return Value of property txtLength.
     */
    public java.lang.String getTxtLength() {
        return txtLength;
    }
    
    /**
     * Setter for property txtLength.
     * @param txtLength New value of property txtLength.
     */
    public void setTxtLength(java.lang.String txtLength) {
        this.txtLength = txtLength;
    }
    
    /**
     * Getter for property txtBreth.
     * @return Value of property txtBreth.
     */
    public java.lang.String getTxtBreth() {
        return txtBreth;
    }
    
    /**
     * Setter for property txtBreth.
     * @param txtBreth New value of property txtBreth.
     */
    public void setTxtBreth(java.lang.String txtBreth) {
        this.txtBreth = txtBreth;
    }
    
    
    
    
    /**
     * Getter for property txtHeight.
     * @return Value of property txtHeight.
     */
    public java.lang.String getTxtHeight() {
        return txtHeight;
    }
    
    /**
     * Setter for property txtHeight.
     * @param txtHeight New value of property txtHeight.
     */
    public void setTxtHeight(java.lang.String txtHeight) {
        this.txtHeight = txtHeight;
    }
    
    /**
     * Getter for property txtLockerRentAcHd.
     * @return Value of property txtLockerRentAcHd.
     */
    public java.lang.String getTxtLockerRentAcHd() {
        return txtLockerRentAcHd;
    }
    
    /**
     * Setter for property txtLockerRentAcHd.
     * @param txtLockerRentAcHd New value of property txtLockerRentAcHd.
     */
    public void setTxtLockerRentAcHd(java.lang.String txtLockerRentAcHd) {
        this.txtLockerRentAcHd = txtLockerRentAcHd;
    }
    
    /**
     * Getter for property txtLockerSusAcHd.
     * @return Value of property txtLockerSusAcHd.
     */
    public java.lang.String getTxtLockerSusAcHd() {
        return txtLockerSusAcHd;
    }
    
    /**
     * Setter for property txtLockerSusAcHd.
     * @param txtLockerSusAcHd New value of property txtLockerSusAcHd.
     */
    public void setTxtLockerSusAcHd(java.lang.String txtLockerSusAcHd) {
        this.txtLockerSusAcHd = txtLockerSusAcHd;
    }
    
    /**
     * Getter for property txtLockerMiscAcHd.
     * @return Value of property txtLockerMiscAcHd.
     */
    public java.lang.String getTxtLockerMiscAcHd() {
        return txtLockerMiscAcHd;
    }
    
    /**
     * Setter for property txtLockerMiscAcHd.
     * @param txtLockerMiscAcHd New value of property txtLockerMiscAcHd.
     */
    public void setTxtLockerMiscAcHd(java.lang.String txtLockerMiscAcHd) {
        this.txtLockerMiscAcHd = txtLockerMiscAcHd;
    }
    
    /**
     * Getter for property txtLockerBrkAcHd.
     * @return Value of property txtLockerBrkAcHd.
     */
    public java.lang.String getTxtLockerBrkAcHd() {
        return txtLockerBrkAcHd;
    }
    
    /**
     * Setter for property txtLockerBrkAcHd.
     * @param txtLockerBrkAcHd New value of property txtLockerBrkAcHd.
     */
    public void setTxtLockerBrkAcHd(java.lang.String txtLockerBrkAcHd) {
        this.txtLockerBrkAcHd = txtLockerBrkAcHd;
    }
    
    /**
     * Getter for property txtLockerServAcHd.
     * @return Value of property txtLockerServAcHd.
     */
    public java.lang.String getTxtLockerServAcHd() {
        return txtLockerServAcHd;
    }
    
    /**
     * Setter for property txtLockerServAcHd.
     * @param txtLockerServAcHd New value of property txtLockerServAcHd.
     */
    public void setTxtLockerServAcHd(java.lang.String txtLockerServAcHd) {
        this.txtLockerServAcHd = txtLockerServAcHd;
    }
    
    /**
     * Getter for property txtLockerRentAdvAcHd.
     * @return Value of property txtLockerRentAdvAcHd.
     */
    public java.lang.String getTxtLockerRentAdvAcHd() {
        return txtLockerRentAdvAcHd;
    }
    
    /**
     * Setter for property txtLockerRentAdvAcHd.
     * @param txtLockerRentAdvAcHd New value of property txtLockerRentAdvAcHd.
     */
    public void setTxtLockerRentAdvAcHd(java.lang.String txtLockerRentAdvAcHd) {
        this.txtLockerRentAdvAcHd = txtLockerRentAdvAcHd;
    }
    
    /**
     * Getter for property txtPenalIntrstAcHd.
     * @return Value of property txtPenalIntrstAcHd.
     */
    public String getTxtPenalIntrstAcHd() {
        return txtPenalIntrstAcHd;
    }
    
    /**
     * Setter for property txtPenalIntrstAcHd.
     * @param txtPenalIntrstAcHd New value of property txtPenalIntrstAcHd.
     */
    public void setTxtPenalIntrstAcHd(String txtPenalIntrstAcHd) {
        this.txtPenalIntrstAcHd = txtPenalIntrstAcHd;
    }
    
    /**
     * Getter for property rdoPenalYes.
     * @return Value of property rdoPenalYes.
     */
    public boolean isRdoPenalYes() {
        return rdoPenalYes;
    }
    
    /**
     * Setter for property rdoPenalYes.
     * @param rdoPenalYes New value of property rdoPenalYes.
     */
    public void setRdoPenalYes(boolean rdoPenalYes) {
        this.rdoPenalYes = rdoPenalYes;
    }
    
    /**
     * Getter for property rdoPenalNo.
     * @return Value of property rdoPenalNo.
     */
    public boolean isRdoPenalNo() {
        return rdoPenalNo;
    }
    
    /**
     * Setter for property rdoPenalNo.
     * @param rdoPenalNo New value of property rdoPenalNo.
     */
    public void setRdoPenalNo(boolean rdoPenalNo) {
        this.rdoPenalNo = rdoPenalNo;
    }
    
    /**
     * Getter for property txtPenalRateInterest.
     * @return Value of property txtPenalRateInterest.
     */
    public String getTxtPenalRateInterest() {
        return txtPenalRateInterest;
    }
    
    /**
     * Setter for property txtPenalRateInterest.
     * @param txtPenalRateInterest New value of property txtPenalRateInterest.
     */
    public void setTxtPenalRateInterest(String txtPenalRateInterest) {
        this.txtPenalRateInterest = txtPenalRateInterest;
    }
    
}