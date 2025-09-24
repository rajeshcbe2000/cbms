/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductOB.java
 *
 * Created on Fri Jan 07 12:01:51 IST 2005
 */

package com.see.truetransact.ui.product.share;

import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.transferobject.product.share.ShareProductTO;
import com.see.truetransact.transferobject.product.share.ShareProductLoanTO;

import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.CommonUtil;
import java.util.*;

import org.apache.log4j.Logger;

import javax.swing.DefaultListModel;

/**
 *
 * @author Ashok Vijayakumar
 */

public class ShareProductOB extends CObservable{
    
    Date curDate = ClientUtil.getCurrentDate();
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger _log = Logger.getLogger(ShareProductOB.class);
    private ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.product.share.ShareProductRB", ProxyParameters.LANGUAGE);
    private ProxyFactory proxy = null;
    private ComboBoxModel  cbmDivCalcFrequency,cbmDivAppFrequency,cbmUnclaimedDivPeriod,cbmShareType,cbmNomineePeriod,cbmRefundPeriod,cbmAdditionalShareRefund,cbmLoanType;
//    added by nikhil
    private ComboBoxModel  cbmDivCalcFrequencyNew,cbmDivCalcType,cbmDividendRounding,cbmPensionProducts;
    private HashMap map,lookUpHash,keyValue;
    private int _result,_actionType;
    private ArrayList key,value;
    private static ShareProductOB objShareProductOB;//Singleton Object
    private String txtAdmissionFee = "";
    private String txtIncomeAccountHead="";
    private String txtAdmissionFeeMin = "";
    private String txtAdmissionFeeMax = "";
    private String rdoAdmissionFeeType = "";
    private String txtApplicationFee = "";
    private String txtFaceValue = "";
    private String txtWithDrawRestriction = "";
    private String txtMaxShareHolding = "";
    private String txtSubscribedCapital = "";
    private String txtPaidupCapital = "";
    private String txtShareFee = "";
    private String txtIssuedCapital = "";
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private String cboShareType = "";
    private boolean chkReqActHolder = false;
    private String chkConsiderSalaryRecovery = "";
    private List itemsSubmittingList = new ArrayList();
    private List itemsSubmittingAddrList = new ArrayList();
    
//    added by nikhil
    private String chkSubsidyForSCST = "";
    private String txtCustomerShare = "";
    private String txtGovernmentShare = "";
    private String txtGovtSubsidyAccountHead = "";

    private boolean rdoRatificationYes = false;
    private boolean rdoRatificationNo = false;
    private String txtNomineeaAllowed = "";
    private String cboNomineePeriod = "";
    private String txtNomineePeriod = "";
    private String txtShareSuspAccount = "";
    private String txtShareAccount = "";
    private String txtMemFeeAcct = "";
    private String txtApplFeeAcct = "";
    private String txtShareFeeAcct = "";
    private String txtMinIntialShares = "";
    private String txtRefundinaYear = "";
    private String txtRefundPeriod = "";
    private String cboRefundPeriod = "";
    private String cboAdditionalShareRefund = "";
    private String cboLoanType = "";
    private String txtLoanAvailingLimit = "";
    private String txtAdditionalShareRefund = "";
    private String txtUnsecuredAdvances = "";
//    private String txtSurityLimit = "";
    private String txtMaxLoanLimit = "";
    private String txtSecuredAdvances = "";
    private String tdtCalculatedDate = "";
    private String tdtDueDate = "";
    private String txtDividentPercentage = "";
    private String cboDivCalcFrequency = "";
    
//    added by nikhil
    private String cboDivCalcFrequencyNew= "";
    private String txtMinDividendAmount= "";
    private String cboDivCalcType= "";
    private String cboDividendRounding= "";
    public String Data;
    public String addrData;
    
    private String cboDivAppFrequency = "";
    private String cboUnclaimedDivPeriod = "";
    private String txtUnclaimedDivPeriod = "";
    private String txtDivPayableAcct="";
    private String txtDivPaidAcct="";
    private Date lstRunDate=null;
    private Date unclaimedRunDate=null;
    private Date unclaimedTransferDate=null;
    private String unClaimedDivTransferAcHd="";
    private ArrayList headings;
    private EnhancedTableModel tbmLoanType;
    private HashMap loanDataMap;
    private HashMap loanDataMapForEdit;
    private HashMap deletedMap;
    private String prefix = "";
    private String suffix = "";
    private String txtMaxLoanAmt = "";
    private String chkOutstandingRequired = "";
    private boolean rdoFullClosureYes = false;
    private boolean rdoFullClosureNo = false;
    private String pensionProductType = "";
    private String pensionProductId = "";
    private String pensionAcNo = "";
    private double minPension = 0.0;
    private double pensionAge = 0.0;
    private double sharePeriod = 0.0;
    private String shareCertificte = "";
    private String chkDrfAllowed = "";
    
    /** Creates a new instance of ShareProductOB */
    public ShareProductOB() {
        map = new HashMap();
        map.put(CommonConstants.JNDI, "ShareProductJNDI");
        map.put(CommonConstants.HOME, "serverside.product.share.ShareProductHome");
        map.put(CommonConstants.REMOTE, "serverside.proudct.share.ShareProduct");
        try {
            proxy = ProxyFactory.createProxy();
            initUIComboBoxModel();
            fillDropdown();
            createTblHeadings();
            createTbmLoanType();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    static {
        try {
            _log.info("Creating ShareProductOB...");
            objShareProductOB= new ShareProductOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** Creating Instances of ComboBoxModel */
    private void initUIComboBoxModel(){
        
        cbmDivCalcFrequency = new  ComboBoxModel();
        cbmDivAppFrequency = new  ComboBoxModel();
        cbmUnclaimedDivPeriod = new  ComboBoxModel();
        cbmShareType = new  ComboBoxModel();
        cbmNomineePeriod = new  ComboBoxModel();
        cbmRefundPeriod = new  ComboBoxModel();
        cbmAdditionalShareRefund = new  ComboBoxModel();
        cbmLoanType = new  ComboBoxModel();
        
//        added by nikhil
        cbmDivCalcFrequencyNew = new ComboBoxModel();
        cbmDividendRounding = new ComboBoxModel();
        cbmDivCalcType = new ComboBoxModel();
        cbmPensionProducts = new ComboBoxModel();
    }
    
    /* Filling up the the ComboBoxModel with key, value */
    private void fillDropdown() throws Exception{
        try{
            _log.info("Inside FillDropDown");
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("PERIOD");
            lookup_keys.add("FREQUENCY");
            lookup_keys.add("SHARE_TYPE");
            lookup_keys.add("LOANPRODUCT.CATEGORY");
//            added by nikhil
            lookup_keys.add("DIVIDEND_CALC_TYPE");
            lookup_keys.add("DIVIDEND_ROUND_OFF");
            lookup_keys.add("SHARE_PENSION_PRODUCTTYPE");
                      
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get("PERIOD"));
            cbmUnclaimedDivPeriod  = new ComboBoxModel(key,value);
            cbmNomineePeriod  = new ComboBoxModel(key,value);
            cbmRefundPeriod  = new ComboBoxModel(key,value);
            cbmAdditionalShareRefund  = new ComboBoxModel(key,value);
            getKeyValue((HashMap) keyValue.get("FREQUENCY"));
            cbmDivCalcFrequency = new ComboBoxModel(key,value);
            cbmDivAppFrequency= new ComboBoxModel(key,value);
            getKeyValue((HashMap) keyValue.get("SHARE_TYPE"));
            cbmShareType= new ComboBoxModel(key,value);
            
            
            getKeyValue((HashMap) keyValue.get("LOANPRODUCT.CATEGORY"));
            cbmLoanType= new ComboBoxModel(key,value);
            cbmLoanType.removeKeyAndElement("DAILY_LOAN");
            cbmLoanType.removeKeyAndElement("MDS_LOAN");
            cbmLoanType.removeKeyAndElement("PADDY_LOAN");
 
            
//          added by nikhil
            getKeyValue((HashMap) keyValue.get("FREQUENCY"));
            System.out.println("@#@#$@#$@$key:"+key+ "  :value :"+value);
            if(key.contains("1") || key.contains("7")){
                key.remove(key.indexOf("1"));
                key.remove(key.indexOf("7"));
            }
            if(value.contains("Daily") || value.contains("Weekly")){
                value.remove(value.indexOf("Daily"));
                value.remove(value.indexOf("Weekly"));
            }
            cbmDivCalcFrequencyNew = new ComboBoxModel(key,value);
            getKeyValue((HashMap) keyValue.get("DIVIDEND_ROUND_OFF"));
            cbmDividendRounding = new ComboBoxModel(key,value);
            getKeyValue((HashMap) keyValue.get("DIVIDEND_CALC_TYPE"));
            cbmDivCalcType = new ComboBoxModel(key, value);
            getKeyValue((HashMap) keyValue.get("SHARE_PENSION_PRODUCTTYPE"));
            cbmPensionProducts = new ComboBoxModel(key, value);

            }catch(NullPointerException e){
            parseException.logException(e,true);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /* Return the key,value(Array List) to be used up in ComboBoxModel */
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    /** Create the ArrayList headings and addup all the title to the columns for the
     *tablemodeel tbmLoanType */
    private void createTblHeadings(){
        headings = new ArrayList();
        headings.add(resourceBundle.getString("tblHeading1"));
        headings.add(resourceBundle.getString("tblHeading2"));
        headings.add(resourceBundle.getString("tblHeading3"));
        headings.add(resourceBundle.getString("tblHeading5"));
    }
    public String getChkOutstandingRequired() {
        return chkOutstandingRequired;
    }
    public void setChkOutstandingRequired(String chkOutstandingRequired) {
        this.chkOutstandingRequired = chkOutstandingRequired;
    }
    
    /** Creating the instance of tablemodel tbmLoanType **/
    private void createTbmLoanType(){
        tbmLoanType = new EnhancedTableModel(null,headings);
    }
    
    /**
     * Returns an instance of ShareProductOB.
     * @return  ShareProductOB
     */
    
    public static ShareProductOB getInstance()throws Exception{
        return objShareProductOB;
    }
    
    
    // Setter method for cboShareType
    void setCboShareType(String cboShareType){
        this.cboShareType = cboShareType;
        setChanged();
    }
    // Getter method for cboShareType
    String getCboShareType(){
        return this.cboShareType;
    }
    
    /**
     * Getter for property cbmShareType.
     * @return Value of property cbmShareType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmShareType() {
        return cbmShareType;
    }
    
    /**
     * Setter for property cbmShareType.
     * @param cbmShareType New value of property cbmShareType.
     */
    public void setCbmShareType(com.see.truetransact.clientutil.ComboBoxModel cbmShareType) {
        this.cbmShareType = cbmShareType;
    }
    
    // Setter method for txtFaceValue
    void setTxtFaceValue(String txtFaceValue){
        this.txtFaceValue = txtFaceValue;
        setChanged();
    }
    // Getter method for txtFaceValue
    String getTxtFaceValue(){
        return this.txtFaceValue;
    }
    
    // Setter method for txtIssuedCapital
    void setTxtIssuedCapital(String txtIssuedCapital){
        this.txtIssuedCapital = txtIssuedCapital;
        setChanged();
    }
    // Getter method for txtIssuedCapital
    String getTxtIssuedCapital(){
        return this.txtIssuedCapital;
    }
    
    // Setter method for txtSubscribedCapital
    void setTxtSubscribedCapital(String txtSubscribedCapital){
        this.txtSubscribedCapital = txtSubscribedCapital;
        setChanged();
    }
    // Getter method for txtSubscribedCapital
    String getTxtSubscribedCapital(){
        return this.txtSubscribedCapital;
    }
    
    // Setter method for txtPaidupCapital
    void setTxtPaidupCapital(String txtPaidupCapital){
        this.txtPaidupCapital = txtPaidupCapital;
        setChanged();
    }
    // Getter method for txtPaidupCapital
    String getTxtPaidupCapital(){
        return this.txtPaidupCapital;
    }
    
    // Setter method for txtMaxShareHolding
    void setTxtMaxShareHolding(String txtMaxShareHolding){
        this.txtMaxShareHolding = txtMaxShareHolding;
        setChanged();
    }
    // Getter method for txtMaxShareHolding
    String getTxtMaxShareHolding(){
        return this.txtMaxShareHolding;
    }
    
    // Setter method for txtAdmissionFee
    void setTxtAdmissionFee(String txtAdmissionFee){
        this.txtAdmissionFee = txtAdmissionFee;
        setChanged();
    }
    // Getter method for txtAdmissionFee
    String getTxtAdmissionFee(){
        return this.txtAdmissionFee;
    }
    
    // Setter method for txtApplicationFee
    void setTxtApplicationFee(String txtApplicationFee){
        this.txtApplicationFee = txtApplicationFee;
        setChanged();
    }
    // Getter method for txtApplicationFee
    String getTxtApplicationFee(){
        return this.txtApplicationFee;
    }
    
    // Setter method for txtShareFee
    void setTxtShareFee(String txtShareFee){
        this.txtShareFee = txtShareFee;
        setChanged();
    }
    // Getter method for txtShareFee
    String getTxtShareFee(){
        return this.txtShareFee;
    }
    
    // Setter method for chkReqActHolder
    void setChkReqActHolder(boolean chkReqActHolder){
        this.chkReqActHolder = chkReqActHolder;
        setChanged();
    }
    // Getter method for chkReqActHolder
    boolean getChkReqActHolder(){
        return this.chkReqActHolder;
    }
    
    // Setter method for txtNomineeaAllowed
    void setTxtNomineeaAllowed(String txtNomineeaAllowed){
        this.txtNomineeaAllowed = txtNomineeaAllowed;
        setChanged();
    }
    // Getter method for txtNomineeaAllowed
    String getTxtNomineeaAllowed(){
        return this.txtNomineeaAllowed;
    }
    
    // Setter method for cboNomineePeriod
    void setCboNomineePeriod(String cboNomineePeriod){
        this.cboNomineePeriod = cboNomineePeriod;
        setChanged();
    }
    // Getter method for cboNomineePeriod
    String getCboNomineePeriod(){
        return this.cboNomineePeriod;
    }
    
    /**
     * Getter for property cbmNomineePeriod.
     * @return Value of property cbmNomineePeriod.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmNomineePeriod() {
        return cbmNomineePeriod;
    }
    
    /**
     * Setter for property cbmNomineePeriod.
     * @param cbmNomineePeriod New value of property cbmNomineePeriod.
     */
    public void setCbmNomineePeriod(com.see.truetransact.clientutil.ComboBoxModel cbmNomineePeriod) {
        this.cbmNomineePeriod = cbmNomineePeriod;
    }
    
    
    // Setter method for txtNomineePeriod
    void setTxtNomineePeriod(String txtNomineePeriod){
        this.txtNomineePeriod = txtNomineePeriod;
        setChanged();
    }
    // Getter method for txtNomineePeriod
    String getTxtNomineePeriod(){
        return this.txtNomineePeriod;
    }
    
    // Setter method for txtShareSuspAccount
    void setTxtShareSuspAccount(String txtShareSuspAccount){
        this.txtShareSuspAccount = txtShareSuspAccount;
        setChanged();
    }
    // Getter method for txtShareSuspAccount
    String getTxtShareSuspAccount(){
        return this.txtShareSuspAccount;
    }
    
    // Setter method for txtRefundinaYear
    void setTxtRefundinaYear(String txtRefundinaYear){
        this.txtRefundinaYear = txtRefundinaYear;
        setChanged();
    }
    // Getter method for txtRefundinaYear
    String getTxtRefundinaYear(){
        return this.txtRefundinaYear;
    }
    
    // Setter method for txtRefundPeriod
    void setTxtRefundPeriod(String txtRefundPeriod){
        this.txtRefundPeriod = txtRefundPeriod;
        setChanged();
    }
    // Getter method for txtRefundPeriod
    String getTxtRefundPeriod(){
        return this.txtRefundPeriod;
    }
    
    // Setter method for cboRefundPeriod
    void setCboRefundPeriod(String cboRefundPeriod){
        this.cboRefundPeriod = cboRefundPeriod;
        setChanged();
    }
    // Getter method for cboRefundPeriod
    String getCboRefundPeriod(){
        return this.cboRefundPeriod;
    }
    
    /**
     * Setter for property cbmRefundPeriod.
     * @param cbmRefundPeriod New value of property cbmRefundPeriod.
     */
    public void setCbmRefundPeriod(com.see.truetransact.clientutil.ComboBoxModel cbmRefundPeriod) {
        this.cbmRefundPeriod = cbmRefundPeriod;
    }
    
    /**
     * Getter for property cbmAdditionalShareRefund.
     * @return Value of property cbmAdditionalShareRefund.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmAdditionalShareRefund() {
        return cbmAdditionalShareRefund;
    }
    
    // Setter method for cboAdditionalShareRefund
    void setCboAdditionalShareRefund(String cboAdditionalShareRefund){
        this.cboAdditionalShareRefund = cboAdditionalShareRefund;
        setChanged();
    }
    // Getter method for cboAdditionalShareRefund
    String getCboAdditionalShareRefund(){
        return this.cboAdditionalShareRefund;
    }
    
    /**
     * Getter for property cbmRefundPeriod.
     * @return Value of property cbmRefundPeriod.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRefundPeriod() {
        return cbmRefundPeriod;
    }
    
    
    
    /**
     * Setter for property cbmAdditionalShareRefund.
     * @param cbmAdditionalShareRefund New value of property cbmAdditionalShareRefund.
     */
    public void setCbmAdditionalShareRefund(com.see.truetransact.clientutil.ComboBoxModel cbmAdditionalShareRefund) {
        this.cbmAdditionalShareRefund = cbmAdditionalShareRefund;
    }
    
    // Setter method for txtAdditionalShareRefund
    void setTxtAdditionalShareRefund(String txtAdditionalShareRefund){
        this.txtAdditionalShareRefund = txtAdditionalShareRefund;
        setChanged();
    }
    // Getter method for txtAdditionalShareRefund
    String getTxtAdditionalShareRefund(){
        return this.txtAdditionalShareRefund;
    }
    
    // Setter method for txtUnsecuredAdvances
    void setTxtUnsecuredAdvances(String txtUnsecuredAdvances){
        this.txtUnsecuredAdvances = txtUnsecuredAdvances;
        setChanged();
    }
    // Getter method for txtUnsecuredAdvances
    String getTxtUnsecuredAdvances(){
        return this.txtUnsecuredAdvances;
    }
    
//    // Setter method for txtSurityLimit
//    void setTxtSurityLimit(String txtSurityLimit){
//        this.txtSurityLimit = txtSurityLimit;
//        setChanged();
//    }
//    // Getter method for txtSurityLimit
//    String getTxtSurityLimit(){
//        return this.txtSurityLimit;
//    }
    
    // Setter method for txtMaxLoanLimit
    void setTxtMaxLoanLimit(String txtMaxLoanLimit){
        this.txtMaxLoanLimit = txtMaxLoanLimit;
        setChanged();
    }
    // Getter method for txtMaxLoanLimit
    String getTxtMaxLoanLimit(){
        return this.txtMaxLoanLimit;
    }
    
    // Setter method for txtSecuredAdvances
    void setTxtSecuredAdvances(String txtSecuredAdvances){
        this.txtSecuredAdvances = txtSecuredAdvances;
        setChanged();
    }
    // Getter method for txtSecuredAdvances
    String getTxtSecuredAdvances(){
        return this.txtSecuredAdvances;
    }
    
    // Setter method for tdtCalculatedDate
    void setTdtCalculatedDate(String tdtCalculatedDate){
        this.tdtCalculatedDate = tdtCalculatedDate;
        setChanged();
    }
    // Getter method for tdtCalculatedDate
    String getTdtCalculatedDate(){
        return this.tdtCalculatedDate;
    }
    
    // Setter method for tdtDueDate
    void setTdtDueDate(String tdtDueDate){
        this.tdtDueDate = tdtDueDate;
        setChanged();
    }
    // Getter method for tdtDueDate
    String getTdtDueDate(){
        return this.tdtDueDate;
    }
    
    // Setter method for txtDividentPercentage
    void setTxtDividentPercentage(String txtDividentPercentage){
        this.txtDividentPercentage = txtDividentPercentage;
        setChanged();
    }
    // Getter method for txtDividentPercentage
    String getTxtDividentPercentage(){
        return this.txtDividentPercentage;
    }
    
    // Setter method for cboDivCalcFrequency
    void setCboDivCalcFrequency(String cboDivCalcFrequency){
        this.cboDivCalcFrequency = cboDivCalcFrequency;
        setChanged();
    }
    
    
    // Getter method for cboDivCalcFrequency
    String getCboDivCalcFrequency(){
        return this.cboDivCalcFrequency;
    }
    
    // Setter method for cboDivAppFrequency
    void setCboDivAppFrequency(String cboDivAppFrequency){
        this.cboDivAppFrequency = cboDivAppFrequency;
        setChanged();
    }
    // Getter method for cboDivAppFrequency
    String getCboDivAppFrequency(){
        return this.cboDivAppFrequency;
    }
    
    /**
     * Getter for property cbmDivCalcFrequency.
     * @return Value of property cbmDivCalcFrequency.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDivCalcFrequency() {
        return cbmDivCalcFrequency;
    }
    
    /**
     * Setter for property cbmDivCalcFrequency.
     * @param cbmDivCalcFrequency New value of property cbmDivCalcFrequency.
     */
    public void setCbmDivCalcFrequency(com.see.truetransact.clientutil.ComboBoxModel cbmDivCalcFrequency) {
        this.cbmDivCalcFrequency = cbmDivCalcFrequency;
    }
    /**
     * Getter for property cbmDivAppFrequency.
     * @return Value of property cbmDivAppFrequency.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDivAppFrequency() {
        return cbmDivAppFrequency;
    }
    
    /**
     * Setter for property cbmDivAppFrequency.
     * @param cbmDivAppFrequency New value of property cbmDivAppFrequency.
     */
    public void setCbmDivAppFrequency(com.see.truetransact.clientutil.ComboBoxModel cbmDivAppFrequency) {
        this.cbmDivAppFrequency = cbmDivAppFrequency;
    }
    
    
    // Setter method for cboUnclaimedDivPeriod
    void setCboUnclaimedDivPeriod(String cboUnclaimedDivPeriod){
        this.cboUnclaimedDivPeriod = cboUnclaimedDivPeriod;
        setChanged();
    }
    // Getter method for cboUnclaimedDivPeriod
    String getCboUnclaimedDivPeriod(){
        return this.cboUnclaimedDivPeriod;
    }
    
    /**
     * Getter for property cbmUnclaimedDivPeriod.
     * @return Value of property cbmUnclaimedDivPeriod.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmUnclaimedDivPeriod() {
        return cbmUnclaimedDivPeriod;
    }
    
    /**
     * Setter for property cbmUnclaimedDivPeriod.
     * @param cbmUnclaimedDivPeriod New value of property cbmUnclaimedDivPeriod.
     */
    public void setCbmUnclaimedDivPeriod(com.see.truetransact.clientutil.ComboBoxModel cbmUnclaimedDivPeriod) {
        this.cbmUnclaimedDivPeriod = cbmUnclaimedDivPeriod;
    }
    
    // Setter method for txtUnclaimedDivPeriod
    void setTxtUnclaimedDivPeriod(String txtUnclaimedDivPeriod){
        this.txtUnclaimedDivPeriod = txtUnclaimedDivPeriod;
        setChanged();
    }
    // Getter method for txtUnclaimedDivPeriod
    String getTxtUnclaimedDivPeriod(){
        return this.txtUnclaimedDivPeriod;
    }
    
    /**
     * Getter for property cbmLoanType.
     * @return Value of property cbmLoanType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmLoanType() {
        return cbmLoanType;
    }
    
    /**
     * Setter for property cbmLoanType.
     * @param cbmLoanType New value of property cbmLoanType.
     */
    public void setCbmLoanType(com.see.truetransact.clientutil.ComboBoxModel cbmLoanType) {
        this.cbmLoanType = cbmLoanType;
    }
    
    /**
     * Getter for property cboLoanType.
     * @return Value of property cboLoanType.
     */
    public java.lang.String getCboLoanType() {
        return cboLoanType;
    }
    
    /**
     * Setter for property cboLoanType.
     * @param cboLoanType New value of property cboLoanType.
     */
    public void setCboLoanType(java.lang.String cboLoanType) {
        this.cboLoanType = cboLoanType;
    }
    
    /**
     * Getter for property txtLoanAvailingLimit.
     * @return Value of property txtLoanAvailingLimit.
     */
    public java.lang.String getTxtLoanAvailingLimit() {
        return txtLoanAvailingLimit;
    }
    
    /**
     * Setter for property txtLoanAvailingLimit.
     * @param txtLoanAvailingLimit New value of property txtLoanAvailingLimit.
     */
    public void setTxtLoanAvailingLimit(java.lang.String txtLoanAvailingLimit) {
        this.txtLoanAvailingLimit = txtLoanAvailingLimit;
    }
    
    /**
     * Getter for property tbmLoanType.
     * @return Value of property tbmLoanType.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTbmLoanType() {
        return tbmLoanType;
    }
    
    /**
     * Setter for property tbmLoanType.
     * @param tbmLoanType New value of property tbmLoanType.
     */
    public void setTbmLoanType(com.see.truetransact.clientutil.EnhancedTableModel tbmLoanType) {
        this.tbmLoanType = tbmLoanType;
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
        setChanged();
    }
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    public int getResult(){
        return _result;
    }
       
      
    
    /** Returns an Instance of ShareProductTO */
    public ShareProductTO getShareProductTO(String command){
        StringBuffer manData= new StringBuffer();
        StringBuffer manAddrData= new StringBuffer();
        for(int i=0;i<getItemsSubmittingList().size();i++){
             manData.append(CommonUtil.convertObjToStr(getItemsSubmittingList().get(i)));
             manData.append(",");
        }
         for(int i=0;i<getItemsSubmittingAddrList().size();i++){
             manAddrData.append(CommonUtil.convertObjToStr(getItemsSubmittingAddrList().get(i)));
             manAddrData.append(",");
        }
        String Data=CommonUtil.convertObjToStr(manData);
        String addrData=CommonUtil.convertObjToStr(manAddrData);
        ShareProductTO objShareProductTO = new ShareProductTO();
        final String yes="Y";
        final String no="N";
        objShareProductTO.setCommand(command);
        if(objShareProductTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_INSERT)){
            objShareProductTO.setStatus(CommonConstants.STATUS_CREATED);
        }else  if(objShareProductTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_UPDATE)){
            objShareProductTO.setStatus(CommonConstants.STATUS_MODIFIED);
        }else if(objShareProductTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_DELETE)){
            objShareProductTO.setStatus(CommonConstants.STATUS_DELETED);
        }
        objShareProductTO.setBranchId(TrueTransactMain.BRANCH_ID);
        objShareProductTO.setStatusBy(TrueTransactMain.USER_ID);
        objShareProductTO.setStatusDt(curDate);
        objShareProductTO.setCommand(command);
        
        objShareProductTO.setShareType(CommonUtil.convertObjToStr(getCbmShareType().getKeyForSelected()));
        objShareProductTO.setMandatoryData(Data);
        objShareProductTO.setMandatoryAddrData(addrData);
        objShareProductTO.setFaceValue(CommonUtil.convertObjToDouble(getTxtFaceValue()));
        objShareProductTO.setIssuedCapital(CommonUtil.convertObjToDouble(getTxtIssuedCapital()));
        objShareProductTO.setSubscribedCapital(CommonUtil.convertObjToDouble(getTxtSubscribedCapital()));
        objShareProductTO.setPaidupCapital(CommonUtil.convertObjToDouble(getTxtPaidupCapital()));
        objShareProductTO.setWithdrawalRestriction(CommonUtil.convertObjToDouble(getTxtRefundinaYear()));
        objShareProductTO.setMaximumShare(CommonUtil.convertObjToDouble(getTxtMaxShareHolding()));
        objShareProductTO.setAdmissionFee(CommonUtil.convertObjToDouble(getTxtAdmissionFee()));
        objShareProductTO.setTxtAdmissionFeeMax(CommonUtil.convertObjToDouble(getTxtAdmissionFeeMax()));
        objShareProductTO.setTxtAdmissionFeeMin(CommonUtil.convertObjToDouble(getTxtAdmissionFeeMin()));
        objShareProductTO.setRdoAdmissionFeeType(CommonUtil.convertObjToStr(getRdoAdmissionFeeType()));
        objShareProductTO.setApplicationFee(CommonUtil.convertObjToDouble(getTxtApplicationFee()));
        objShareProductTO.setShareFee(CommonUtil.convertObjToDouble(getTxtShareFee()));
        objShareProductTO.setTxtDivPaidAcct(CommonUtil.convertObjToStr(getTxtDivPaidAcct()));
        objShareProductTO.setTxtDivPayableAcct(CommonUtil.convertObjToStr(getTxtDivPayableAcct()));
        objShareProductTO.setLstRunDate(getLstRunDate());
        objShareProductTO.setUnClaimedDivTransferAcHd(getUnClaimedDivTransferAcHd());
        objShareProductTO.setUnclaimedRunDate(getUnclaimedRunDate());
        objShareProductTO.setUnclaimedTransferDate(getUnclaimedTransferDate());
        
        if(getChkReqActHolder())
            objShareProductTO.setRequiredAccountHolder(yes);
        else
            objShareProductTO.setRequiredAccountHolder(no);
        if(getRdoRatificationYes()==true )
           objShareProductTO. setRdoRatification("Y");  
        else if(getRdoRatificationNo()==true)
         objShareProductTO. setRdoRatification("N");  
         if (getRdoFullClosureYes() == true) {
            objShareProductTO.setRdoFullClosure("Y");
        } else if (getRdoFullClosureNo() == true) {
            objShareProductTO.setRdoFullClosure("N");
        }
        objShareProductTO.setConsiderRecovery(CommonUtil.convertObjToStr(getChkConsiderSalaryRecovery()));
//        added by nikhil fo Share Subsidy
        objShareProductTO.setChkSubsidyForSCST(CommonUtil.convertObjToStr(getChkSubsidyForSCST())); 
        objShareProductTO.setTxtCustomerShare(CommonUtil.convertObjToStr(getTxtCustomerShare()));
        objShareProductTO.setTxtGovernmentShare(CommonUtil.convertObjToStr(getTxtGovernmentShare()));
        objShareProductTO.setTxtGovtSubsidyAccountHead(CommonUtil.convertObjToStr(getTxtGovtSubsidyAccountHead()));
        
        objShareProductTO.setDividendCalcFrequency(CommonUtil.convertObjToStr(getCbmDivCalcFrequency().getKeyForSelected()));
//        added by nikhil
        objShareProductTO.setCboDivCalcFrequencyNew(CommonUtil.convertObjToStr(getCbmDivCalcFrequencyNew().getKeyForSelected()));
        objShareProductTO.setCboDivCalcType(CommonUtil.convertObjToStr(getCbmDivCalcType().getKeyForSelected()));
        objShareProductTO.setCboDividendRounding(CommonUtil.convertObjToStr(getCbmDividendRounding().getKeyForSelected()));
        objShareProductTO.setTxtMinDividendAmount(CommonUtil.convertObjToStr(getTxtMinDividendAmount()));
        objShareProductTO.setTxtIncomeAccountHead(CommonUtil.convertObjToStr(getTxtIncomeAccountHead()));
        objShareProductTO.setDividendApplFrequency(CommonUtil.convertObjToStr(getCbmDivAppFrequency().getKeyForSelected()));
        int time = CommonUtil.convertObjToInt(getTxtUnclaimedDivPeriod());
        String timeUnit = getCboUnclaimedDivPeriod();
        objShareProductTO.setUnclaimedDividendPeriod(new Double(time));
//        Modified By Nikhil for Unclaimed Dividend payment Limit
        if(timeUnit.equalsIgnoreCase("Years")){
            //If Years is selected in the CboTransitPeriod in the UI
//            objShareProductTO.setUnclaimedDividendPeriod(new Double(time * 365));
            objShareProductTO.setUnclaimedDividendPeriodType("Y");
        }else if(timeUnit.equalsIgnoreCase("Months")){
            //If Months is selected in the cboTransitPeriod in the UI
//            objShareProductTO.setUnclaimedDividendPeriod(new Double(time * 30));
            objShareProductTO.setUnclaimedDividendPeriodType("M");
        }else{
            //If Days is selected in the cboTransitPeriod in the UI
//            objShareProductTO.setUnclaimedDividendPeriod(new Double(time * 1));
            objShareProductTO.setUnclaimedDividendPeriodType("D");
        }
        time = CommonUtil.convertObjToInt(getTxtRefundPeriod());
        timeUnit = getCboRefundPeriod();
        if(timeUnit.equalsIgnoreCase("Years")){
            //If Years is selected in the CboTransitPeriod in the UI
            objShareProductTO.setLockupRefund(new Double(time * 365));
        }else if(timeUnit.equalsIgnoreCase("Months")){
            //If Months is selected in the cboTransitPeriod in the UI
            objShareProductTO.setLockupRefund(new Double(time * 30));
        }else{
            //If Days is selected in the cboTransitPeriod in the UI
            objShareProductTO.setLockupRefund(new Double(time * 1));
        }
        time = CommonUtil.convertObjToInt(getTxtAdditionalShareRefund());
        timeUnit = getCboAdditionalShareRefund();
        if(timeUnit.equalsIgnoreCase("Years")){
            //If Years is selected in the CboTransitPeriod in the UI
            objShareProductTO.setLockupAdditionalShare(new Double(time * 365));
        }else if(timeUnit.equalsIgnoreCase("Months")){
            //If Months is selected in the cboTransitPeriod in the UI
            objShareProductTO.setLockupAdditionalShare(new Double(time * 30));
        }else{
            //If Days is selected in the cboTransitPeriod in the UI
            objShareProductTO.setLockupAdditionalShare(new Double(time * 1));
        }
        objShareProductTO.setAllowedNominee(CommonUtil.convertObjToDouble(getTxtNomineeaAllowed()));
        //        objShareProductTO.setLastDividendCalc(DateUtil.getDateMMDDYYYY(getTdtCalculatedDate()));
        //        objShareProductTO.setNextDueDate(DateUtil.getDateMMDDYYYY(getTdtDueDate()));
        Date CalDt = DateUtil.getDateMMDDYYYY(getTdtCalculatedDate());
        if(CalDt != null){
            Date calDate = (Date)curDate.clone();
            calDate.setDate(CalDt.getDate());
            calDate.setMonth(CalDt.getMonth());
            calDate.setYear(CalDt.getYear());
            objShareProductTO.setLastDividendCalc(calDate);
        }else{
            objShareProductTO.setLastDividendCalc(DateUtil.getDateMMDDYYYY(getTdtCalculatedDate()));
        }
        
        Date DueDt = DateUtil.getDateMMDDYYYY(getTdtDueDate());
        if(DueDt != null){
            Date dueDate = (Date)curDate.clone();
            dueDate.setDate(DueDt.getDate());
            dueDate.setMonth(DueDt.getMonth());
            dueDate.setYear(DueDt.getYear());
            objShareProductTO.setNextDueDate(dueDate);
        }else{
            objShareProductTO.setNextDueDate(DateUtil.getDateMMDDYYYY(getTdtDueDate()));
        }
        objShareProductTO.setPercentageDividend(CommonUtil.convertObjToDouble(getTxtDividentPercentage()));
        time = CommonUtil.convertObjToInt(getTxtNomineePeriod());
        timeUnit = getCboNomineePeriod();
        if(timeUnit.equalsIgnoreCase("Years")){
            //If Years is selected in the CboTransitPeriod in the UI
            objShareProductTO.setNominalMemPeriod(new Double(time * 365));
        }else if(timeUnit.equalsIgnoreCase("Months")){
            //If Months is selected in the cboTransitPeriod in the UI
            objShareProductTO.setNominalMemPeriod(new Double(time * 30));
        }else{
            //If Days is selected in the cboTransitPeriod in the UI
            objShareProductTO.setNominalMemPeriod(new Double(time * 1));
        }
        objShareProductTO.setShareSuspenseAchd(getTxtShareSuspAccount());
        objShareProductTO.setShareAchd(getTxtShareAccount());
        objShareProductTO.setMembershipFeeAchd(getTxtMemFeeAcct());
        objShareProductTO.setApplicationFeeAchd(getTxtApplFeeAcct());
        objShareProductTO.setMinIntialShares(getTxtMinIntialshares());
        objShareProductTO.setShareFeeAchd(getTxtShareFeeAcct());
        objShareProductTO.setNumPatternPrefix(getNumPatternPrefix());
        objShareProductTO.setNumPatternSuffix(getNumPatternSuffix());
        objShareProductTO.setIsOutstandingReq(getChkOutstandingRequired());
        if(getPensionAge()>0){
            objShareProductTO.setPensionAge(getPensionAge());
            objShareProductTO.setShareRunPeriod(getSharePeriod());
            objShareProductTO.setMinPension(getMinPension());
            objShareProductTO.setPensionDebitProdType(getPensionProductType());
            if(!getPensionProductType().equals("GL")){
                objShareProductTO.setPensionDebitProdId(getPensionProductId());
            }
            objShareProductTO.setPensionDebitAccount(getPensionAcNo());
        }
        objShareProductTO.setShareCertificate(getShareCertificte());
        objShareProductTO.setDrfAllowed(getChkDrfAllowed());
        System.out.println("getChkOutstandingRequired()"+getChkOutstandingRequired());
        return objShareProductTO;
        
    }
    
    /** Sets all the ShareProduct values to the OB varibles  there by populatin the UI fields */
    private void setShareProductTO(ShareProductTO objShareProductTO){
        setCboShareType(CommonUtil.convertObjToStr(getCbmShareType().getDataForKey(objShareProductTO.getShareType())));
        setTxtFaceValue(CommonUtil.convertObjToStr(objShareProductTO.getFaceValue()));
        setTxtIssuedCapital(CommonUtil.convertObjToStr(objShareProductTO.getIssuedCapital()));
        setTxtSubscribedCapital(CommonUtil.convertObjToStr(objShareProductTO.getSubscribedCapital()));
        setTxtPaidupCapital(CommonUtil.convertObjToStr(objShareProductTO.getPaidupCapital()));
        setTxtRefundinaYear(CommonUtil.convertObjToStr(objShareProductTO.getWithdrawalRestriction()));
        setTxtMaxShareHolding(CommonUtil.convertObjToStr(objShareProductTO.getMaximumShare()));
        setTxtAdmissionFee(CommonUtil.convertObjToStr(objShareProductTO.getAdmissionFee()));
        Data=(CommonUtil.convertObjToStr(objShareProductTO.getMandatoryData()));
        addrData=(CommonUtil.convertObjToStr(objShareProductTO.getMandatoryAddrData()));
        setTxtAdmissionFeeMax(CommonUtil.convertObjToStr(objShareProductTO.getTxtAdmissionFeeMax()));
        setTxtAdmissionFeeMin(CommonUtil.convertObjToStr(objShareProductTO.getTxtAdmissionFeeMin()));
        setRdoAdmissionFeeType(CommonUtil.convertObjToStr(objShareProductTO.getRdoAdmissionFeeType()));
        setTxtApplicationFee(CommonUtil.convertObjToStr(objShareProductTO.getApplicationFee()));
        setTxtShareFee(CommonUtil.convertObjToStr(objShareProductTO.getShareFee()));
        setTxtDivPaidAcct(CommonUtil.convertObjToStr(objShareProductTO.getTxtDivPaidAcct()));
        setTxtDivPayableAcct(CommonUtil.convertObjToStr(objShareProductTO.getTxtDivPayableAcct()));
        if(objShareProductTO.getRequiredAccountHolder().equalsIgnoreCase("Y"))
            setChkReqActHolder(true);
        else
            setChkReqActHolder(false);
        setChkConsiderSalaryRecovery(CommonUtil.convertObjToStr(objShareProductTO.getConsiderRecovery()));
        
//        added by Nikhil For Share Subsidy
        setChkSubsidyForSCST(CommonUtil.convertObjToStr(objShareProductTO.getChkSubsidyForSCST())); 
        setTxtCustomerShare(CommonUtil.convertObjToStr(objShareProductTO.getTxtCustomerShare()));
        setTxtGovernmentShare(CommonUtil.convertObjToStr(objShareProductTO.getTxtGovernmentShare()));
        setTxtGovtSubsidyAccountHead(CommonUtil.convertObjToStr(objShareProductTO.getTxtGovtSubsidyAccountHead()));
        
        if(objShareProductTO.isRdoRatification()!=null){
          if(objShareProductTO.isRdoRatification().equalsIgnoreCase("Y")){
              setRdoRatificationYes(true);
        setRdoRatificationNo(false);
          }
          else
          {
              setRdoRatificationNo(true);
              setRdoRatificationYes(false);
          }
        }
          if (objShareProductTO.isRdoFullClosure() != null) {
            if (objShareProductTO.isRdoFullClosure().equalsIgnoreCase("Y")) {
                setRdoFullClosureYes(true);
                setRdoFullClosureNo(false);
            } else {
                setRdoFullClosureNo(true);
                setRdoFullClosureYes(false);
            }
        }
        setCboDivCalcFrequency(CommonUtil.convertObjToStr((getCbmDivCalcFrequency().getDataForKey(objShareProductTO.getDividendCalcFrequency()))));
//        changed by nikhil
        setCboDivCalcFrequencyNew(CommonUtil.convertObjToStr((getCbmDivCalcFrequencyNew().getDataForKey(objShareProductTO.getCboDivCalcFrequencyNew()))));
        setCboDivCalcType(CommonUtil.convertObjToStr((getCbmDivCalcType().getDataForKey(objShareProductTO.getCboDivCalcType()))));
        setCboDividendRounding(CommonUtil.convertObjToStr((getCbmDividendRounding().getDataForKey(objShareProductTO.getCboDividendRounding()))));
        setTxtMinDividendAmount(CommonUtil.convertObjToStr(objShareProductTO.getTxtMinDividendAmount()));
       setTxtIncomeAccountHead(CommonUtil.convertObjToStr(objShareProductTO.getTxtIncomeAccountHead()));
        
        setCboDivAppFrequency(CommonUtil.convertObjToStr((getCbmDivAppFrequency().getDataForKey(objShareProductTO.getDividendApplFrequency()))));
        int value = CommonUtil.convertObjToInt(objShareProductTO.getUnclaimedDividendPeriod());
        String period = CommonUtil.convertObjToStr(objShareProductTO.getUnclaimedDividendPeriodType());
        setTxtUnclaimedDivPeriod(CommonUtil.convertObjToStr(new Integer(value)));
//        Changed by Nikhil
        if(period.equals("Y")){
//            setTxtUnclaimedDivPeriod(CommonUtil.convertObjToStr(new Integer(value/365)));
            setCboUnclaimedDivPeriod("Years");
        }else if(period.equals("M")){
//            setTxtUnclaimedDivPeriod(CommonUtil.convertObjToStr(new Integer(value/30)));
            setCboUnclaimedDivPeriod("Months");
        }else{
//            setTxtUnclaimedDivPeriod(CommonUtil.convertObjToStr(new Integer(value)));
            setCboUnclaimedDivPeriod("Days");
        }
        
        value = CommonUtil.convertObjToInt(objShareProductTO.getLockupRefund());
        if((value/365 > 0 ) && (value%365 == 0)){
            setTxtRefundPeriod(CommonUtil.convertObjToStr(new Integer(value/365)));
            setCboRefundPeriod("Years");
        }else if((value/30 > 0 ) && (value%30 == 0)){
            setTxtRefundPeriod(CommonUtil.convertObjToStr(new Integer(value/30)));
            setCboRefundPeriod("Months");
        }else{
            setTxtRefundPeriod(CommonUtil.convertObjToStr(new Integer(value)));
            setCboRefundPeriod("Days");
        }
        
        value = CommonUtil.convertObjToInt(objShareProductTO.getLockupRefund());
        if((value/365 > 0 ) && (value%365 == 0)){
            setTxtAdditionalShareRefund(CommonUtil.convertObjToStr(new Integer(value/365)));
            setCboAdditionalShareRefund("Years");
        }else if((value/30 > 0 ) && (value%30 == 0)){
            setTxtAdditionalShareRefund(CommonUtil.convertObjToStr(new Integer(value/30)));
            setCboAdditionalShareRefund("Months");
        }else{
            setTxtAdditionalShareRefund(CommonUtil.convertObjToStr(new Integer(value)));
            setCboAdditionalShareRefund("Days");
        }
        setTxtNomineeaAllowed(CommonUtil.convertObjToStr(objShareProductTO.getAllowedNominee()));
        setTdtCalculatedDate(DateUtil.getStringDate(objShareProductTO.getLastDividendCalc()));
        setTdtDueDate(DateUtil.getStringDate(objShareProductTO.getNextDueDate()));
        setTxtDividentPercentage(CommonUtil.convertObjToStr(objShareProductTO.getPercentageDividend()));
        value = CommonUtil.convertObjToInt(objShareProductTO.getNominalMemPeriod());
        if((value/365 > 0 ) && (value%365 == 0)){
            setTxtNomineePeriod(CommonUtil.convertObjToStr(new Integer(value/365)));
            setCboNomineePeriod("Years");
        }else if((value/30 > 0 ) && (value%30 == 0)){
            setTxtNomineePeriod(CommonUtil.convertObjToStr(new Integer(value/30)));
            setCboNomineePeriod("Months");
        }else{
            setTxtNomineePeriod(CommonUtil.convertObjToStr(new Integer(value)));
            setCboNomineePeriod("Days");
        }
        setTxtShareSuspAccount(objShareProductTO.getShareSuspenseAchd());
        setTxtShareAccount(objShareProductTO.getShareAchd());
        setTxtMemFeeAcct(objShareProductTO.getMembershipFeeAchd());
        setTxtApplFeeAcct(objShareProductTO.getApplicationFeeAchd());
         setTxtMinIntialShares(objShareProductTO.getMinIntialShares());
        setTxtShareFeeAcct(objShareProductTO.getShareFeeAchd());
        setLstRunDate(objShareProductTO.getLstRunDate());
        setUnclaimedRunDate(objShareProductTO.getUnclaimedRunDate());
        setUnclaimedTransferDate(objShareProductTO.getUnclaimedTransferDate());
        setUnClaimedDivTransferAcHd(objShareProductTO.getUnClaimedDivTransferAcHd());
        setNumPatternPrefix(objShareProductTO.getNumPatternPrefix());
        setNumPatternSuffix(objShareProductTO.getNumPatternSuffix());
        setPensionAge(CommonUtil.convertObjToDouble(objShareProductTO.getPensionAge()));
        setSharePeriod(CommonUtil.convertObjToDouble(objShareProductTO.getShareRunPeriod()));
        setMinPension(CommonUtil.convertObjToDouble(objShareProductTO.getMinPension()));
        setPensionProductType(objShareProductTO.getPensionDebitProdType());
        if(objShareProductTO.getPensionDebitProdType()!=null && !objShareProductTO.getPensionDebitProdType().equals("GL")){
            setPensionProductId(objShareProductTO.getPensionDebitProdId());    
        }
        System.out.println("objShareProductTO.getShareCertificate()#%#%#%"+objShareProductTO.getShareCertificate());
        setShareCertificte(objShareProductTO.getShareCertificate());
        setPensionAcNo(objShareProductTO.getPensionDebitAccount());
        System.out.println("setPensionAge#%#%#%"+getPensionAge());
        setChkDrfAllowed(objShareProductTO.getDrfAllowed());
        notifyObservers();
    }
    
    /*** Sets the values to the fields related to loandetials using the to ShareProductLoanTO **/
    private void setShareProductLoanTO(ShareProductLoanTO objShareProductLoanTO){
        setCboLoanType(CommonUtil.convertObjToStr(getCbmLoanType().getDataForKey(objShareProductLoanTO.getLoanType())));
        setTxtMaxLoanLimit(CommonUtil.convertObjToStr(objShareProductLoanTO.getMaxLoanLimit()));
        setTxtLoanAvailingLimit(CommonUtil.convertObjToStr(objShareProductLoanTO.getShareHoldingMultiples()));
        setTxtMaxLoanAmt(CommonUtil.convertObjToStr(objShareProductLoanTO.getMaxLoanAmt()));
//        setTxtSurityLimit(CommonUtil.convertObjToStr(objShareProductLoanTO.getSurityLimit()));
        notifyObservers();
    }
    
    /** Retruns an instance of ShareProductLoanTO **/
    private ShareProductLoanTO getShareProductLoanTO(){
        ShareProductLoanTO objShareProductLoanTO = new ShareProductLoanTO();
        objShareProductLoanTO.setShareType(CommonUtil.convertObjToStr(getCbmShareType().getKeyForSelected()));
        objShareProductLoanTO.setLoanType(CommonUtil.convertObjToStr(getCbmLoanType().getKeyForSelected()));
        objShareProductLoanTO.setMaxLoanLimit(CommonUtil.convertObjToDouble(getTxtMaxLoanLimit()));
        objShareProductLoanTO.setShareHoldingMultiples(CommonUtil.convertObjToDouble(getTxtLoanAvailingLimit()));
        objShareProductLoanTO.setMaxLoanAmt(CommonUtil.convertObjToDouble(getTxtMaxLoanAmt()));
//        objShareProductLoanTO.setSurityLimit(CommonUtil.convertObjToDouble(getTxtSurityLimit()));
        return objShareProductLoanTO;
    }
    
    /* Executes Query using the TO object */
    public void execute(String command) {
        try {
            HashMap term = new HashMap();
            term.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            term.put("ShareProductTO", getShareProductTO(command));
            if(loanDataMap!=null){
                term.put("ShareProductLoanTO", loanDataMap);
            }
            if(deletedMap != null){
                term.put("DeletedShareProductLoanTO", deletedMap);
            }
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            System.out.println("LoanDataMap "+ loanDataMap);
            System.out.println("DeletedMap "+ deletedMap);
            int countRec=0;
            if(getActionType() ==3){
                HashMap hash=new HashMap();
                hash.put("SHARE_TYPE",CommonUtil.convertObjToStr(getCbmShareType().getKeyForSelected()));
                List lst =ClientUtil.executeQuery("ShareProductCount", hash);
                hash=(HashMap)lst.get(0);
                countRec=CommonUtil.convertObjToInt(hash.get("COUNTS"));
                
            }
            HashMap proxyResultMap=null;
            if(countRec==0)
                proxyResultMap = proxy.execute(term, map);
            else
                ClientUtil.displayAlert("THIS PRODUCT HAVING ACCOUNT");
            setResult(getActionType());
            freeHashMaps();
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    
    /** Resetting all tbe Fields of UI */
    public  void resetForm(){
        setCboShareType("");
        setTxtFaceValue("");
        setTxtIssuedCapital("");
        setTxtSubscribedCapital("");
        setTxtPaidupCapital("");
        setTxtRefundinaYear("");
        setTxtMaxShareHolding("");
        setTxtAdmissionFee("");
        setTxtAdmissionFeeMax("");
        setTxtAdmissionFeeMin("");
        setRdoAdmissionFeeType("");
        setTxtApplicationFee("");
        setTxtShareFee("");
        setChkReqActHolder(false);
        setChkConsiderSalaryRecovery("");
//        added by nikhil for Share Subsidy
        setChkSubsidyForSCST("");
        setTxtCustomerShare("");
        setTxtGovernmentShare("");
        setTxtGovtSubsidyAccountHead("");
        setCboDivCalcFrequency("");
//        changed by nikhil
        setCboDividendRounding("");
        setCboDivCalcFrequencyNew("");
        setCboDivCalcType("");
        setTxtMinDividendAmount("");
        setTxtIncomeAccountHead("");
        setCboDivAppFrequency("");
        setTxtUnclaimedDivPeriod("");
        setCboUnclaimedDivPeriod("");
        setTxtRefundPeriod("");
        setCboRefundPeriod("");
        setTxtAdditionalShareRefund("");
        setCboAdditionalShareRefund("");
        setTxtNomineeaAllowed("");
        setTdtCalculatedDate("");
        setTdtDueDate("");
        setTxtDividentPercentage("");
        setTxtUnsecuredAdvances("");
        setTxtSecuredAdvances("");
        setTxtNomineePeriod("");
        setCboNomineePeriod("");
        setTxtShareSuspAccount("");
        setTxtShareAccount("");
        setTxtMinIntialShares("");
        setTxtMemFeeAcct("");
        setTxtApplFeeAcct("");
         setRdoRatificationYes(false);
       setRdoRatificationNo(false);
        setTxtShareFeeAcct("");
        setTxtDivPaidAcct("");
        setTxtDivPayableAcct("");
        resetTblLoanType();
        resetLoanDetails();
        setNumPatternPrefix("");
        setNumPatternSuffix("");
        setRdoFullClosureNo(false);
        setRdoFullClosureYes(false);
        setShareCertificte("");      
        setChkDrfAllowed("");
         notifyObservers();      
    }
    
    /** Resets the LoanDetails related fields **/
    public void resetLoanDetails(){
        setCboLoanType("");
        resetLoanDetailsExceptLoanType();
    }
    
    /** Resets the loanrelated details execept the loantype **/
    public void resetLoanDetailsExceptLoanType(){
        setTxtLoanAvailingLimit("");
        setTxtMaxLoanLimit("");
        setTxtMaxLoanAmt("");
//        setTxtSurityLimit("");
        notifyObservers();
    }
    
    /** This checks whether user entered sharetype already exists if it exists
     * it returns true otherwise false */
    public boolean isShareTypeExists(String cboShareTypeSelected){
        boolean exists = false;
        HashMap where = new HashMap();
        where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        ArrayList resultList =(ArrayList) ClientUtil.executeQuery("getSelectShareProductMap",where);
        where = null;
        if(resultList != null){
            for(int i=0; i<resultList.size(); i++){
                HashMap resultMap = (HashMap)resultList.get(i);
                String shareType =CommonUtil.convertObjToStr(resultMap.get("SHARE_TYPE"));
                if(shareType.equalsIgnoreCase(cboShareTypeSelected)){
                    exists = true;
                    break;
                }
            }
        }
        return exists;
    }
    
    /* Populates the TO object by executing a Query */
    public void populateData(HashMap whereMap) {
        HashMap mapData=null;
        try {
            freeHashMaps();
            mapData = proxy.executeQuery(whereMap, map);
            populateTblLoanType((HashMap)mapData.get("ShareProductLoanTO"));
            ShareProductTO objShareProductTO =
            (ShareProductTO) ((List)((HashMap) mapData.get("ShareProductTO")).get("ShareProductTO")).get(0);
            setShareProductTO(objShareProductTO);

        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
            parseException.logException(e,true);
            
        }
    }
    
    /** Create the instance of loanDataMap which contains all tbe loanrealated
     *data with key as the loantype and the value as the TO **/
    public void createLoanDataMap(boolean dataExists,int rowCount){
        ShareProductLoanTO objShareProductLoanTO = getShareProductLoanTO();
        ShareProductLoanTO   objShareLoanTO=new ShareProductLoanTO();
        if(loanDataMap == null){
            loanDataMap = new HashMap();
        }
        if(loanDataMapForEdit == null){
            loanDataMapForEdit = new HashMap();
        }
        ArrayList addList =new ArrayList(loanDataMapForEdit.keySet());
        int length = addList.size();
        if(length>0){
            for(int i=0; i<length; i++){
                
                objShareLoanTO = (ShareProductLoanTO) loanDataMapForEdit.get(addList.get(i));
                
                if(objShareLoanTO.getLoanType().equals(objShareProductLoanTO.getLoanType()) && getActionType()==ClientConstants.ACTIONTYPE_EDIT){
            objShareProductLoanTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    i=length;
        }else{
                    objShareProductLoanTO.setStatus(CommonConstants.STATUS_CREATED);
                }
            }
        }else{
            objShareProductLoanTO.setStatus(CommonConstants.STATUS_CREATED);
        }
        objShareProductLoanTO.setStatusBy(TrueTransactMain.USER_ID);
        objShareProductLoanTO.setStatusDt(curDate);
        loanDataMap.put(objShareProductLoanTO.getLoanType(), objShareProductLoanTO);
        insertRow(dataExists, rowCount);
    }
    
    /** Inserts the row in to the tblLoanType by setting the values in tbmLoanType **/
    public void insertRow(boolean dataExists, int rowCount){
        if (dataExists){
            tbmLoanType.setValueAt(CommonUtil.convertObjToStr(getCbmLoanType().getKeyForSelected()), rowCount, 0);
            tbmLoanType.setValueAt(CommonUtil.convertObjToStr(getTxtLoanAvailingLimit()), rowCount, 1);
            tbmLoanType.setValueAt(CommonUtil.convertObjToStr(getTxtMaxLoanLimit()), rowCount, 2);
            tbmLoanType.setValueAt(CommonUtil.convertObjToStr(getTxtMaxLoanAmt()), rowCount, 3);
//            tbmLoanType.setValueAt(CommonUtil.convertObjToStr(getTxtSurityLimit()), rowCount, 3);
        }else{
            final ArrayList rowData = new ArrayList();
            rowData.add(CommonUtil.convertObjToStr(getCbmLoanType().getKeyForSelected()));
            rowData.add(CommonUtil.convertObjToStr(getTxtLoanAvailingLimit()));
            rowData.add(CommonUtil.convertObjToStr(getTxtMaxLoanLimit()));
            rowData.add(CommonUtil.convertObjToStr(getTxtMaxLoanAmt()));
//            rowData.add(CommonUtil.convertObjToStr(getTxtSurityLimit()));
            tbmLoanType.insertRow(tbmLoanType.getRowCount(),rowData);
        }
        resetLoanDetails();
    }
    
    /** This method is used to populate the fields in the panLoanType in the UI
     *according to the row slected in the tblLoanType in the UI **/
    public void populateSelectedRow(int rowIndex){
        String primaryKey = CommonUtil.convertObjToStr(tbmLoanType.getValueAt(rowIndex,0));
        setShareProductLoanTO((ShareProductLoanTO)loanDataMap.get(primaryKey));
    }
    
    /** This method is used to remove the selected row when btnTabDelete button is clicked**/
    public void removeSelectedRow(int rowIndex){
        if(deletedMap == null){
            deletedMap = new HashMap();
        }
        ShareProductLoanTO objShareProductLoanTO = (ShareProductLoanTO) loanDataMap.get(tbmLoanType.getValueAt(rowIndex,0));
        objShareProductLoanTO.setStatus(CommonConstants.STATUS_DELETED);
        objShareProductLoanTO.setStatusBy(TrueTransactMain.USER_ID);
        objShareProductLoanTO.setStatusDt(curDate);
        if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
            deletedMap.put(tbmLoanType.getValueAt(rowIndex,0), objShareProductLoanTO);
        }
        loanDataMap.remove(tbmLoanType.getValueAt(rowIndex,0));
        tbmLoanType.removeRow(rowIndex);
        resetLoanDetails();
    }
    
    /** This method is used to populate the tblLoantType when btnedit button is clicked
     *in the ui, to populate the table with already existing data */
    private void populateTblLoanType(HashMap dataMap){
        try{
            ArrayList dataList = new ArrayList(dataMap.keySet());
            if(dataList.size() != 0){
                for(int i=0;i<dataList.size();i++){
                    ArrayList rowData = new ArrayList();
                    ShareProductLoanTO objLoanTO = (ShareProductLoanTO)dataMap.get(CommonUtil.convertObjToStr(dataList.get(i)));
                    objLoanTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objLoanTO.setStatusBy(TrueTransactMain.USER_ID);
                    objLoanTO.setStatusDt(curDate);
                    rowData.add(CommonUtil.convertObjToStr(objLoanTO.getLoanType()));
                    rowData.add(CommonUtil.convertObjToStr(objLoanTO.getShareHoldingMultiples()));
                    rowData.add(CommonUtil.convertObjToStr(objLoanTO.getMaxLoanLimit()));
                    rowData.add(CommonUtil.convertObjToStr(objLoanTO.getMaxLoanAmt()));
                    
//                    rowData.add(CommonUtil.convertObjToStr(objLoanTO.getSurityLimit()));
                    if(loanDataMap == null){
                        loanDataMap = new HashMap();
                    }
                    if(loanDataMapForEdit==null){
                        loanDataMapForEdit=new HashMap();
                    }
                    loanDataMap.put(objLoanTO.getLoanType(),objLoanTO);
                    loanDataMapForEdit.put(objLoanTO.getLoanType(),objLoanTO);
                    tbmLoanType.insertRow(0,rowData);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /** This is used to remove the rows in the tblLoanTyep **/
    public void resetTblLoanType(){
        for(int i = tbmLoanType.getRowCount(); i > 0; i--){
            tbmLoanType.removeRow(0);
        }
    }
    
    /** This creates the map named deletedMap which will containt the data
     *deleted in the tblLoanType */
    public void createDeletedMap(){
        if(tbmLoanType.getRowCount() != 0){
            for(int i=0; i<tbmLoanType.getRowCount();i++){
                if(deletedMap == null){
                    deletedMap = new HashMap();
                }
                ShareProductLoanTO objLoanTO = (ShareProductLoanTO)loanDataMap.get(tbmLoanType.getValueAt(i,0));
                objLoanTO.setStatus(CommonConstants.STATUS_DELETED);
                objLoanTO.setStatusBy(TrueTransactMain.USER_ID);
                objLoanTO.setStatusDt(curDate);
                deletedMap.put(tbmLoanType.getValueAt(i,0),objLoanTO);
                loanDataMap.remove(tbmLoanType.getValueAt(i,0));
            }
            loanDataMap = null;
        }
    }
    
    /** This is used to freeup the memeory used up by the maps, loanDataMap, deletedMap **/
    public void freeHashMaps(){
        loanDataMap = null;
        deletedMap = null;
        loanDataMapForEdit=null;
    }
    
    /**
     * Getter for property txtShareAccount.
     * @return Value of property txtShareAccount.
     */
    public java.lang.String getTxtShareAccount() {
        return txtShareAccount;
    }
    
    /**
     * Setter for property txtShareAccount.
     * @param txtShareAccount New value of property txtShareAccount.
     */
    public void setTxtShareAccount(java.lang.String txtShareAccount) {
        this.txtShareAccount = txtShareAccount;
    }
    
    /**
     * Getter for property txtMemFeeAcct.
     * @return Value of property txtMemFeeAcct.
     */
    public java.lang.String getTxtMemFeeAcct() {
        return txtMemFeeAcct;
    }
    
    /**
     * Setter for property txtMemFeeAcct.
     * @param txtMemFeeAcct New value of property txtMemFeeAcct.
     */
    public void setTxtMemFeeAcct(java.lang.String txtMemFeeAcct) {
        this.txtMemFeeAcct = txtMemFeeAcct;
    }
    
    /**
     * Getter for property txtApplFeeAcct.
     * @return Value of property txtApplFeeAcct.
     */
    public java.lang.String getTxtApplFeeAcct() {
        return txtApplFeeAcct;
    }
    
    /**
     * Setter for property txtApplFeeAcct.
     * @param txtApplFeeAcct New value of property txtApplFeeAcct.
     */
    public void setTxtApplFeeAcct(java.lang.String txtApplFeeAcct) {
        this.txtApplFeeAcct = txtApplFeeAcct;
    }
    
    /**
     * Getter for property txtShareFeeAcct.
     * @return Value of property txtShareFeeAcct.
     */
    public java.lang.String getTxtShareFeeAcct() {
        return txtShareFeeAcct;
    }
    
    /**
     * Setter for property txtShareFeeAcct.
     * @param txtShareFeeAcct New value of property txtShareFeeAcct.
     */
    public void setTxtShareFeeAcct(java.lang.String txtShareFeeAcct) {
        this.txtShareFeeAcct = txtShareFeeAcct;
    }
    
     public java.lang.String getTxtMinIntialshares() {
        return txtMinIntialShares;
    }
    
  
    public void setTxtMinIntialShares(java.lang.String txtMinIntialShares) {
        this.txtMinIntialShares = txtMinIntialShares;
    }
    
    /**
     * Getter for property txtDivPayableAcct.
     * @return Value of property txtDivPayableAcct.
     */
    public java.lang.String getTxtDivPayableAcct() {
        return txtDivPayableAcct;
    }
    
    /**
     * Setter for property txtDivPayableAcct.
     * @param txtDivPayableAcct New value of property txtDivPayableAcct.
     */
    public void setTxtDivPayableAcct(java.lang.String txtDivPayableAcct) {
        this.txtDivPayableAcct = txtDivPayableAcct;
    }
    
    /**
     * Getter for property txtDivPaidAcct.
     * @return Value of property txtDivPaidAcct.
     */
    public java.lang.String getTxtDivPaidAcct() {
        return txtDivPaidAcct;
    }
    
    /**
     * Setter for property txtDivPaidAcct.
     * @param txtDivPaidAcct New value of property txtDivPaidAcct.
     */
    public void setTxtDivPaidAcct(java.lang.String txtDivPaidAcct) {
        this.txtDivPaidAcct = txtDivPaidAcct;
    }
    
    /**
     * Getter for property lstRunDate.
     * @return Value of property lstRunDate.
     */
    public java.util.Date getLstRunDate() {
        return lstRunDate;
    }
    
    
     public java.lang.String getTxtIncomeAccountHead() {
            return txtIncomeAccountHead;
        }
        
        /**
         * Setter for property rdoAdmissionFeeType.
         * @param rdoAdmissionFeeType New value of property rdoAdmissionFeeType.
         */
        public void setTxtIncomeAccountHead(java.lang.String txtIncomeAccountHead) {
            this.txtIncomeAccountHead = txtIncomeAccountHead;
        }
    
    /**
     * Setter for property lstRunDate.
     * @param lstRunDate New value of property lstRunDate.
     */
    public void setLstRunDate(java.util.Date lstRunDate) {
        this.lstRunDate = lstRunDate;
    }
    
    /**
     * Getter for property unclaimedRunDate.
     * @return Value of property unclaimedRunDate.
     */
    public java.util.Date getUnclaimedRunDate() {
        return unclaimedRunDate;
    }
    
    /**
     * Setter for property unclaimedRunDate.
     * @param unclaimedRunDate New value of property unclaimedRunDate.
     */
    public void setUnclaimedRunDate(java.util.Date unclaimedRunDate) {
        this.unclaimedRunDate = unclaimedRunDate;
    }
    
    /**
     * Getter for property unclaimedTransferDate.
     * @return Value of property unclaimedTransferDate.
     */
    public java.util.Date getUnclaimedTransferDate() {
        return unclaimedTransferDate;
    }
    
    /**
     * Setter for property unclaimedTransferDate.
     * @param unclaimedTransferDate New value of property unclaimedTransferDate.
     */
    public void setUnclaimedTransferDate(java.util.Date unclaimedTransferDate) {
        this.unclaimedTransferDate = unclaimedTransferDate;
    }
    
    /**
     * Getter for property unClaimedDivTransferAcHd.
     * @return Value of property unClaimedDivTransferAcHd.
     */
    public java.lang.String getUnClaimedDivTransferAcHd() {
        return unClaimedDivTransferAcHd;
    }
    
    /**
     * Setter for property unClaimedDivTransferAcHd.
     * @param unClaimedDivTransferAcHd New value of property unClaimedDivTransferAcHd.
     */
    public void setUnClaimedDivTransferAcHd(java.lang.String unClaimedDivTransferAcHd) {
        this.unClaimedDivTransferAcHd = unClaimedDivTransferAcHd;
    }
    
    /**
     * Getter for property txtAdmissionFeeMin.
     * @return Value of property txtAdmissionFeeMin.
     */
    public java.lang.String getTxtAdmissionFeeMin() {
        return txtAdmissionFeeMin;
    }
    
    /**
     * Setter for property txtAdmissionFeeMin.
     * @param txtAdmissionFeeMin New value of property txtAdmissionFeeMin.
     */
    public void setTxtAdmissionFeeMin(java.lang.String txtAdmissionFeeMin) {
        this.txtAdmissionFeeMin = txtAdmissionFeeMin;
    }
    
    /**
     * Getter for property txtAdmissionFeeMax.
     * @return Value of property txtAdmissionFeeMax.
     */
    public java.lang.String getTxtAdmissionFeeMax() {
        return txtAdmissionFeeMax;
    }
    
    /**
     * Setter for property txtAdmissionFeeMax.
     * @param txtAdmissionFeeMax New value of property txtAdmissionFeeMax.
     */
    public void setTxtAdmissionFeeMax(java.lang.String txtAdmissionFeeMax) {
        this.txtAdmissionFeeMax = txtAdmissionFeeMax;
    }
    
    /**
     * Getter for property rdoAdmissionFeeType.
     * @return Value of property rdoAdmissionFeeType.
     */
    public java.lang.String getRdoAdmissionFeeType() {
        return rdoAdmissionFeeType;
    }
    
    /**
     * Setter for property rdoAdmissionFeeType.
     * @param rdoAdmissionFeeType New value of property rdoAdmissionFeeType.
     */
    public void setRdoAdmissionFeeType(java.lang.String rdoAdmissionFeeType) {
        this.rdoAdmissionFeeType = rdoAdmissionFeeType;
    }
    
    /**
     * Getter for property cboDivCalcFrequencyNew.
     * @return Value of property cboDivCalcFrequencyNew.
     */
    public java.lang.String getCboDivCalcFrequencyNew() {
        return cboDivCalcFrequencyNew;
    }
    
      public void setRdoRatificationYes(boolean rdoRatificationYes) {
        this.rdoRatificationYes = rdoRatificationYes;
    }
    
    /**
     * Getter for property cboDivCalcFrequencyNew.
     * @return Value of property cboDivCalcFrequencyNew.
     */
    public boolean getRdoRatificationYes() {
        return rdoRatificationYes;
    }
    
      public void setRdoRatificationNo(boolean rdoRatificationNo) {
        this.rdoRatificationNo = rdoRatificationNo;
    }
    
    /**
     * Getter for property cboDivCalcFrequencyNew.
     * @return Value of property cboDivCalcFrequencyNew.
     */
    public boolean getRdoRatificationNo() {
        return rdoRatificationNo;
    }
    
    
    /**
     * Setter for property cboDivCalcFrequencyNew.
     * @param cboDivCalcFrequencyNew New value of property cboDivCalcFrequencyNew.
     */
    public void setCboDivCalcFrequencyNew(java.lang.String cboDivCalcFrequencyNew) {
        this.cboDivCalcFrequencyNew = cboDivCalcFrequencyNew;
    }
    
    /**
     * Getter for property txtMinDividendAmount.
     * @return Value of property txtMinDividendAmount.
     */
    public java.lang.String getTxtMinDividendAmount() {
        return txtMinDividendAmount;
    }
    
    /**
     * Setter for property txtMinDividendAmount.
     * @param txtMinDividendAmount New value of property txtMinDividendAmount.
     */
    public void setTxtMinDividendAmount(java.lang.String txtMinDividendAmount) {
        this.txtMinDividendAmount = txtMinDividendAmount;
    }
    
    /**
     * Getter for property cboDivCalcType.
     * @return Value of property cboDivCalcType.
     */
    public java.lang.String getCboDivCalcType() {
        return cboDivCalcType;
    }
    
    /**
     * Setter for property cboDivCalcType.
     * @param cboDivCalcType New value of property cboDivCalcType.
     */
    public void setCboDivCalcType(java.lang.String cboDivCalcType) {
        this.cboDivCalcType = cboDivCalcType;
    }
    
    /**
     * Getter for property cboDividendRounding.
     * @return Value of property cboDividendRounding.
     */
    public java.lang.String getCboDividendRounding() {
        return cboDividendRounding;
    }
    
    /**
     * Setter for property cboDividendRounding.
     * @param cboDividendRounding New value of property cboDividendRounding.
     */
    public void setCboDividendRounding(java.lang.String cboDividendRounding) {
        this.cboDividendRounding = cboDividendRounding;
    }
    
    /**
     * Getter for property cbmDivCalcFrequencyNew.
     * @return Value of property cbmDivCalcFrequencyNew.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDivCalcFrequencyNew() {
        return cbmDivCalcFrequencyNew;
    }
    
    /**
     * Setter for property cbmDivCalcFrequencyNew.
     * @param cbmDivCalcFrequencyNew New value of property cbmDivCalcFrequencyNew.
     */
    public void setCbmDivCalcFrequencyNew(com.see.truetransact.clientutil.ComboBoxModel cbmDivCalcFrequencyNew) {
        this.cbmDivCalcFrequencyNew = cbmDivCalcFrequencyNew;
    }
    
    /**
     * Getter for property cbmDivCalcType.
     * @return Value of property cbmDivCalcType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDivCalcType() {
        return cbmDivCalcType;
    }
    
    /**
     * Setter for property cbmDivCalcType.
     * @param cbmDivCalcType New value of property cbmDivCalcType.
     */
    public void setCbmDivCalcType(com.see.truetransact.clientutil.ComboBoxModel cbmDivCalcType) {
        this.cbmDivCalcType = cbmDivCalcType;
    }
    
    /**
     * Getter for property cbmDividendRounding.
     * @return Value of property cbmDividendRounding.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDividendRounding() {
        return cbmDividendRounding;
    }
    
    /**
     * Setter for property cbmDividendRounding.
     * @param cbmDividendRounding New value of property cbmDividendRounding.
     */
    public void setCbmDividendRounding(com.see.truetransact.clientutil.ComboBoxModel cbmDividendRounding) {
        this.cbmDividendRounding = cbmDividendRounding;
    }

    public ComboBoxModel getCbmPensionProducts() {
        return cbmPensionProducts;
    }

    public void setCbmPensionProducts(ComboBoxModel cbmPensionProducts) {
        this.cbmPensionProducts = cbmPensionProducts;
    }
    
    public void setNumPatternPrefix(String prefix){
        this.prefix=prefix;
    }
    
    public String getNumPatternPrefix(){
        return prefix;
    }
    
    public void setNumPatternSuffix(String suffix){
        this.suffix=suffix;
    }
    
    public String getNumPatternSuffix(){
        return suffix;
    }
    	 
    /**
     * Getter for property chkConsiderSalaryRecovery.
     * @return Value of property chkConsiderSalaryRecovery.
     */
    public java.lang.String getChkConsiderSalaryRecovery() {
        return chkConsiderSalaryRecovery;
    }
    
    /**
     * Setter for property chkConsiderSalaryRecovery.
     * @param chkConsiderSalaryRecovery New value of property chkConsiderSalaryRecovery.
     */
    public void setChkConsiderSalaryRecovery(java.lang.String chkConsiderSalaryRecovery) {
        this.chkConsiderSalaryRecovery = chkConsiderSalaryRecovery;
    }

    /**
     * Getter for property chkSubsidyForSCST.
     * @return Value of property chkSubsidyForSCST.
     */
    public java.lang.String getChkSubsidyForSCST() {
        return chkSubsidyForSCST;
    }
    
    /**
     * Setter for property chkSubsidyForSCST.
     * @param chkSubsidyForSCST New value of property chkSubsidyForSCST.
     */
    public void setChkSubsidyForSCST(java.lang.String chkSubsidyForSCST) {
        this.chkSubsidyForSCST = chkSubsidyForSCST;
    }
    
    /**
     * Getter for property txtCustomerShare.
     * @return Value of property txtCustomerShare.
     */
    public java.lang.String getTxtCustomerShare() {
        return txtCustomerShare;
    }
    
    /**
     * Setter for property txtCustomerShare.
     * @param txtCustomerShare New value of property txtCustomerShare.
     */
    public void setTxtCustomerShare(java.lang.String txtCustomerShare) {
        this.txtCustomerShare = txtCustomerShare;
    }
    
    /**
     * Getter for property txtGovernmentShare.
     * @return Value of property txtGovernmentShare.
     */
    public java.lang.String getTxtGovernmentShare() {
        return txtGovernmentShare;
    }
    
    /**
     * Setter for property txtGovernmentShare.
     * @param txtGovernmentShare New value of property txtGovernmentShare.
     */
    public void setTxtGovernmentShare(java.lang.String txtGovernmentShare) {
        this.txtGovernmentShare = txtGovernmentShare;
    }
    
    /**
     * Getter for property txtGovtSubsidyAccountHead.
     * @return Value of property txtGovtSubsidyAccountHead.
     */
    public java.lang.String getTxtGovtSubsidyAccountHead() {
        return txtGovtSubsidyAccountHead;
    }
    
    /**
     * Setter for property txtGovtSubsidyAccountHead.
     * @param txtGovtSubsidyAccountHead New value of property txtGovtSubsidyAccountHead.
     */
    public void setTxtGovtSubsidyAccountHead(java.lang.String txtGovtSubsidyAccountHead) {
        this.txtGovtSubsidyAccountHead = txtGovtSubsidyAccountHead;
    }

    /**
     * Getter for property txtMaxLoanAmt.
     * @return Value of property txtMaxLoanAmt.
     */
    public java.lang.String getTxtMaxLoanAmt() {
        return txtMaxLoanAmt;
    }
    
    /**
     * Setter for property txtMaxLoanAmt.
     * @param txtMaxLoanAmt New value of property txtMaxLoanAmt.
     */
    public void setTxtMaxLoanAmt(java.lang.String txtMaxLoanAmt) {
        this.txtMaxLoanAmt = txtMaxLoanAmt;
    }

    public List getItemsSubmittingList() {
        return itemsSubmittingList;
    }

    public void setItemsSubmittingList(List itemsSubmittingList) {
        this.itemsSubmittingList = itemsSubmittingList;
    }

    public List getItemsSubmittingAddrList() {
        return itemsSubmittingAddrList;
    }

    public void setItemsSubmittingAddrList(List itemsSubmittingAddrList) {
        this.itemsSubmittingAddrList = itemsSubmittingAddrList;
    }
     public boolean getRdoFullClosureNo() {
        return rdoFullClosureNo;
    }

    public void setRdoFullClosureNo(boolean rdoFullClosureNo) {
        this.rdoFullClosureNo = rdoFullClosureNo;
    }

    public boolean getRdoFullClosureYes() {
        return rdoFullClosureYes;
    }

    public void setRdoFullClosureYes(boolean rdoFullClosureYes) {
        this.rdoFullClosureYes = rdoFullClosureYes;
    }

    public double getMinPension() {
        return minPension;
    }

    public void setMinPension(double minPension) {
        this.minPension = minPension;
    }

    public String getPensionAcNo() {
        return pensionAcNo;
    }

    public void setPensionAcNo(String pensionAcNo) {
        this.pensionAcNo = pensionAcNo;
    }

    public double getPensionAge() {
        return pensionAge;
    }

    public void setPensionAge(double pensionAge) {
        this.pensionAge = pensionAge;
    }

    public String getPensionProductId() {
        return pensionProductId;
    }

    public void setPensionProductId(String pensionProductId) {
        this.pensionProductId = pensionProductId;
    }

    public String getPensionProductType() {
        return pensionProductType;
    }

    public void setPensionProductType(String pensionProductType) {
        this.pensionProductType = pensionProductType;
    }

    public double getSharePeriod() {
        return sharePeriod;
    }

    public void setSharePeriod(double sharePeriod) {
        this.sharePeriod = sharePeriod;
    }

    public String getShareCertificte() {
        return shareCertificte;
    }

    public void setShareCertificte(String shareCertificte) {
        this.shareCertificte = shareCertificte;
    }
    
    public String getChkDrfAllowed() {
        return chkDrfAllowed;
    }

    public void setChkDrfAllowed(String chkDrfAllowed) {
        this.chkDrfAllowed = chkDrfAllowed;
    }
    

}