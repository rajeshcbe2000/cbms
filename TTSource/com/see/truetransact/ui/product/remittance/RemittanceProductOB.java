/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * RemittanceProductOB.java
 *
* Created on January 7, 2004, 5:14 PM
 */

package com.see.truetransact.ui.product.remittance;

/**
 *
 * @author Hemant
 * Modification Lohith R.
 */

import java.util.Observable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;

import com.see.truetransact.transferobject.product.remittance.RemittanceProductTO;
import com.see.truetransact.transferobject.product.remittance.RemittanceProductBranchTO;
import com.see.truetransact.transferobject.product.remittance.RemittanceProductChargeTO;
import com.see.truetransact.ui.product.remittance.RemittanceProductRB;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyParameters;

public class RemittanceProductOB extends CObservable {
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
//    private static RemittanceProductRB objRemittanceProductRB = new RemittanceProductRB();
    
    java.util.ResourceBundle objRemittanceProductRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.product.remittance.RemittanceProductRB", ProxyParameters.LANGUAGE);
    
    private RemittanceProductBranchTO objRemittanceProductBranchTO ;
    private RemittanceProductChargeTO objRemittanceProductChargeTO ;
    private ComboBoxModel cbmRateType,cbmProdTypeCr;
    private int optionRemitProdBrch;
    private int optionRemitProdChrg;
    private int optionRemitProdBrchDelete;
    private int optionRemitProdChrgDelete;
    private int optionUniqueRemitProdID;
    private int optionRowPresent;
    private HashMap hash;
    private HashMap operationMap;
    private ProxyFactory proxy;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private int actionType;
    private int result;
    private String txtRateVal = "";
    private String txtForEvery = "";
    private String txtPercent = "";
    private String txtServiceTax = "";
    public boolean remitProdBrnchTabSelected = false;
    public int remitProdBrnchSelectedRow = 0;
    
    public boolean remitProdChrgTabSelected = false;
    public int remitProdChrgSelectedRow = 0;
    
    private ArrayList remittanceProductBranchTabRow = new ArrayList();
    private ArrayList remitProdBrchTabNew = new ArrayList();
    private ArrayList arrayListRemitProdBrchTabUpdate = new ArrayList();
    private HashMap tableHashData;
    private String cboRateType,cboProdTypeCr = "";
    private EnhancedTableModel tblRemittanceProductBranch;
    private EnhancedTableModel tblAliasRemittanceProductBranch;
    private final ArrayList remittanceProductBranchTabTitle = new ArrayList();
    private ArrayList remittanceProductChargeTabRow = new ArrayList();
    private EnhancedTableModel tblRemittanceProductCharge;
    private final ArrayList remittanceProductChargeTabTitle = new ArrayList();
    private ArrayList descList=new ArrayList();
    /**************************/
    static private ArrayList remitProductBrchRowData = new ArrayList();         //row data in the table
    //    static private ArrayList remitProductChrgRowData = new ArrayList();         //row data in the table
    
    private ArrayList remittProductBrchNewData = new ArrayList();              //new data when Button New is perssed
    //    private ArrayList remittProductChrgNewData = new ArrayList();              //new data when Button New is perssed
    
    
    /**************************/
    
    /**************************/
    
    
    static private ArrayList remitProductBrchColumnElement = new ArrayList();   //column data in the table
    //    static private ArrayList remitProductChrgColumnElement = new ArrayList();   //column data in the table
    
    private ArrayList remitProductBrchExistingData = new ArrayList();          //existing data when Button Yes is perssed to Update
    //    private ArrayList remitProductChrgExistingData = new ArrayList();          //existing data when Button Yes is perssed to Update
    
    private ArrayList remittProductBrchDeleteRow = new ArrayList();            //deleted data when Button Delete is perssed
    //    private ArrayList remittProductChrgDeleteRow = new ArrayList();            //deleted data when Button Delete is perssed
    
    /**************************/
    
    
    
    
    
    
    private ArrayList remittProductBrchInsertData;                             //data that has to be inserted
    //    private ArrayList remittProductChrgInsertData;                             //data that has to be inserted
    private ArrayList remittProductBrchUpdateData;                             //data that has to be updated
    //    private ArrayList remittProductChrgUpdateData;                             //data that has to be updated
    private ArrayList remittProductBrchDeleteData;                             //data that has to be deleted
    //    private ArrayList remittProductChrgDeleteData;                             //data that has to be deleted
    
    private HashMap hashChargesTableValues;//Contains All Charges Table Data
    
    private ArrayList remitProdBrchTOData;
    //    private ArrayList remitProdChrgTOData;
    private ArrayList remitProdBrchTO;
    //    private ArrayList remitProdChrgTO;
    
    
    private String txtIssueHeadGR = "";
    private String txtExchangeHeadGR = "";
    private String txtPayableHeadGR = "";
    private String txtRCHeadGR = "";
    private String txtTCHeadGR = "";
    private String txtOCHeadGR = "";
    private String txtCCHeadGR = "";
    private String txtDCHeadGR = "";
    private String txtRTGSSuspenseHead ="";
    private String txtPostageHeadGR = "";
    private String txtProductIdGR = "";
    private String txtProductDescGR = "";
    private String txtLapsedPeriodGR = "";
    private boolean rdoIsLapsedGR_Yes = false;
    private boolean rdoIsLapsedGR_No = false;
    private String txtLapsedHeadGR = "";
    private boolean rdoEFTProductGR_Yes = false;
    private boolean rdoEFTProductGR_No = false;
    private boolean rdoPrintServicesGR_Yes = false;
    private boolean rdoPrintServicesGR_No = false;
    private boolean rdoSeriesGR_Yes = false;
    private boolean rdoSeriesGR_No = false;
    private String txtCashLimitGR = "";
    private String txtMaximumAmount = "";
    private String txtMinimumAmount = "";
    private String txtRemarksGR = "";
    private String txtPerfix = "";
    private String txtSuffix = "";
    private String bankName = "";
    private String txtBankCode = "";
    private String txtBranchName = "";
    private String txtBranchCodeBR = "";
    private String lblRemitProdIDBranch = "";
    private String lblRemitProdDescBranch = "";
    private String lblRemitProdIDCharge = "";
    private String lblChargesBankCode = "";
    private String lblChargesBranchCode = "";
    private String lblRemitProdDescCharge = "";
    private String txtRttLimitBR = "";
    private String txtIVNoRR = "";
    private String txtOVNoRR = "";
    private String txtMinAmtRR = "";
    private String txtMaxAmtRR = "";
    
    private String cboPayableBranchGR = "";
    private String cboRttTypeBR = "";
    
    private String cboLapsedPeriodGR = "";
    private String cboProdCurrency = "";
    
    private String cboBehavesLike = "";
    
    public String productID = "";
    private String productDesc = "";
    private ComboBoxModel cbmPayableBranchGR;
    private ComboBoxModel cbmBehavesLike;
    private ComboBoxModel cbmLapsedPeriodGR;
    private ComboBoxModel cbmRttTypeBR;
    private ComboBoxModel cbmProdCurrency ;
    // Variables required for remit prod charges
    private String txtAmtRangeFrom = "";
    private String txtAmtRangeTo = "";
    private String txtCharges = "";
    private String cboChargeType = "";
    private String cboCategory = "";
    private String cboAmtRangeFrom = "";
    private String cboAmtRangeTo = "";
    private ComboBoxModel cbmChargeType;
    private ComboBoxModel cbmCategory;
    private ComboBoxModel cbmAmtRangeFrom;
    private ComboBoxModel cbmAmtRangeTo;
    private String chargeStatus = "";
    private LinkedHashMap hashCharges = null;
    private LinkedHashMap deletedCharges = null;
    private ArrayList chargesRowData = null;
    private static int deletedChargesCount = 1;
    private String txtMinAmt="";
    private String txtMaxAmt="";
    //Added by kannan
    private boolean chkNewProcedure = false;   
    private String tdtNewProcStartDt = "";   
    private String txtNewProcIssueAcHd = "";
    private String txtrtgsNeftProductId = "";
    private String txtrtgsNeftAcctNo = "";
    private String rtgsNeftGLType = "";

    public String getRtgsNeftGLType() {
        return rtgsNeftGLType;
    }

    public void setRtgsNeftGLType(String rtgsNeftGLType) {
        this.rtgsNeftGLType = rtgsNeftGLType;
    }
    public String getTxtRtgsNeftProductId() {
        return txtrtgsNeftProductId;
    }

    public void setTxtRtgsNeftProductId(String txtrtgsNeftProductId) {
        this.txtrtgsNeftProductId = txtrtgsNeftProductId;
    }

    public String getTxtRtgsNeftAcctNo() {
        return txtrtgsNeftAcctNo;
    }

    public void setTxtRtgsNeftAcctNo(String txtrtgsNeftAcctNo) {
        this.txtrtgsNeftAcctNo = txtrtgsNeftAcctNo;
    }

    private static RemittanceProductOB remittanceProductOB;
    static {
        try {
            remittanceProductOB = new RemittanceProductOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /* Sets the HashMap required to set JNDI,Home and Remote*/
    public RemittanceProductOB()throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "RemittanceProductJNDI");
        operationMap.put(CommonConstants.HOME, "product.remittance.RemittanceProductHome");
        operationMap.put(CommonConstants.REMOTE, "product.remittance.RemittanceProduct");
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        setRemittanceProductBranchTabTitle();
        tblRemittanceProductBranch = new EnhancedTableModel(null, remittanceProductBranchTabTitle);
        tblAliasRemittanceProductBranch = new EnhancedTableModel(null, remittanceProductBranchTabTitle);
        setRemittanceProductChargeTabTitle();
        tblRemittanceProductCharge = new EnhancedTableModel(null, remittanceProductChargeTabTitle);
        notifyObservers();
        fillDropdown();
    }
    
    public static RemittanceProductOB getInstance() {
        return remittanceProductOB;
    }
    
    /* Sets the Remittance Product Branch CTable Tittle to ArrayList*/
    private void setRemittanceProductBranchTabTitle() throws Exception{
        remittanceProductBranchTabTitle.add(objRemittanceProductRB.getString("bankName"));
        remittanceProductBranchTabTitle.add(objRemittanceProductRB.getString("branchCode"));
        remittanceProductBranchTabTitle.add(objRemittanceProductRB.getString("branchName"));
        remittanceProductBranchTabTitle.add(objRemittanceProductRB.getString("inwardVarNum"));
        remittanceProductBranchTabTitle.add(objRemittanceProductRB.getString("outwardVarNum"));
        remittanceProductBranchTabTitle.add(objRemittanceProductRB.getString("minAmt"));
        remittanceProductBranchTabTitle.add(objRemittanceProductRB.getString("maxAmt"));
    }
    
    /* Sets the Remittance Product Charges CTable Tittle to ArrayList*/
    private void setRemittanceProductChargeTabTitle() throws Exception{
        remittanceProductChargeTabTitle.add(objRemittanceProductRB.getString("lblChargeType"));
        remittanceProductChargeTabTitle.add(objRemittanceProductRB.getString("lblCategory"));
        remittanceProductChargeTabTitle.add(objRemittanceProductRB.getString("lblAmtRangeFrom"));
        remittanceProductChargeTabTitle.add(objRemittanceProductRB.getString("lblAmtRangeTo"));
        remittanceProductChargeTabTitle.add(objRemittanceProductRB.getString("lblCharges"));
    }
    
    
    public ComboBoxModel getCbmProdTypeCr() {
        return cbmProdTypeCr;
    }

    public void setCbmProdTypeCr(ComboBoxModel cbmProdTypeCr) {
        this.cbmProdTypeCr = cbmProdTypeCr;
    }

    public String getCboProdTypeCr() {
        return cboProdTypeCr;
    }

    public void setCboProdTypeCr(String cboProdTypeCr) {
        this.cboProdTypeCr = cboProdTypeCr;
    }
    
    /** Setter method for lblRemitProdIDBranch */
    void setLblRemitProdIDBranch(String lblRemitProdIDBranch){
        this.lblRemitProdIDBranch = lblRemitProdIDBranch;
        setChanged();
    }
    /** Getter method for lblRemitProdIDBranch */
    String getLblRemitProdIDBranch(){
        return this.lblRemitProdIDBranch;
    }
    
    /** Setter method for lblRemitProdIDBranch */
    void setLblRemitProdIDCharge(String lblRemitProdIDCharge){
        this.lblRemitProdIDCharge = lblRemitProdIDCharge;
        setChanged();
    }
    /** Getter method for lblRemitProdIDCharge */
    String getLblRemitProdIDCharge(){
        return this.lblRemitProdIDCharge;
    }
    /** Setter method for lblChargesBankCode */
    void setLblChargesBankCode(String lblChargesBankCode){
        this.lblChargesBankCode = lblChargesBankCode;
        setChanged();
    }
    /** Getter method for lblChargesBankCode */
    String getLblChargesBankCode(){
        return this.lblChargesBankCode;
    }
    /** Setter method for chargeStatus */
    void setChargeStatus(String chargeStatus){
        this.chargeStatus = chargeStatus;
        setChanged();
    }
    /** Getter method for chargeStatus */
    String getChargeStatus(){
        return this.chargeStatus;
    }
    /** Setter method for lblChargesBranchCode */
    void setLblChargesBranchCode(String lblChargesBranchCode){
        this.lblChargesBranchCode = lblChargesBranchCode;
        setChanged();
    }
    /** Getter method for lblChargesBranchCode */
    String getLblChargesBranchCode(){
        return this.lblChargesBranchCode;
    }
    
    /** Setter method for lblRemitProdDescBranch */
    void setLblRemitProdDescBranch(String lblRemitProdDescBranch){
        this.lblRemitProdDescBranch = lblRemitProdDescBranch;
        setChanged();
    }
    // Getter method for lblRemitProdDescBranch
    String getLblRemitProdDescBranch(){
        return this.lblRemitProdDescBranch;
    }
    
    /** Setter method for lblRemitProdDescCharge */
    void setLblRemitProdDescCharge(String lblRemitProdDescCharge){
        this.lblRemitProdDescCharge = lblRemitProdDescCharge;
        setChanged();
    }
    /** Getter method for lblRemitProdDescCharge */
    String getLblRemitProdDescCharge(){
        return this.lblRemitProdDescCharge;
    }
    
    /** Setter method for txtIssueHeadGR */
    void setTxtIssueHeadGR(String txtIssueHeadGR){
        this.txtIssueHeadGR = txtIssueHeadGR;
        setChanged();
    }
    /** Getter method for txtIssueHeadGR */
    String getTxtIssueHeadGR(){
        return this.txtIssueHeadGR;
    }
    
    /**Setter method for bankName */
    void setBankName(String bankName){
        this.bankName = bankName;
        setChanged();
    }
    /** Getter method for bankName */
    String getBankName(){
        return this.bankName;
    }
    
    /** Setter method for txtExchangeHeadGR  */
    void setTxtExchangeHeadGR(String txtExchangeHeadGR){
        this.txtExchangeHeadGR = txtExchangeHeadGR;
        setChanged();
    }
    /** Getter method for txtExchangeHeadGR */
    String getTxtExchangeHeadGR(){
        return this.txtExchangeHeadGR;
    }
    
    /** Setter method for txtPayableHeadGR */
    void setTxtPayableHeadGR(String txtPayableHeadGR){
        this.txtPayableHeadGR = txtPayableHeadGR;
        setChanged();
    }
    /** Getter method for txtPayableHeadGR */
    String getTxtPayableHeadGR(){
        return this.txtPayableHeadGR;
    }
    
    /** Setter method for txtRCHeadGR */
    void setTxtRCHeadGR(String txtRCHeadGR){
        this.txtRCHeadGR = txtRCHeadGR;
        setChanged();
    }
    /** Getter method for txtRCHeadGR */
    String getTxtRCHeadGR(){
        return this.txtRCHeadGR;
    }
    
    /** Setter method for txtTCHeadGR */
    void setTxtTCHeadGR(String txtTCHeadGR){
        this.txtTCHeadGR = txtTCHeadGR;
        setChanged();
    }
    /** Getter method for txtTCHeadGR */
    String getTxtTCHeadGR(){
        return this.txtTCHeadGR;
    }
    
    /** Setter method for txtOCHeadGR */
    void setTxtOCHeadGR(String txtOCHeadGR){
        this.txtOCHeadGR = txtOCHeadGR;
        setChanged();
    }
    /** Getter method for txtOCHeadGR */
    String getTxtOCHeadGR(){
        return this.txtOCHeadGR;
    }
    
    /** Setter method for txtCCHeadGR */
    void setTxtCCHeadGR(String txtCCHeadGR){
        this.txtCCHeadGR = txtCCHeadGR;
        setChanged();
    }
    /** Getter method for txtCCHeadGR */
    String getTxtCCHeadGR(){
        return this.txtCCHeadGR;
    }
    
    /** Setter method for txtDCHeadGR */
    void setTxtDCHeadGR(String txtDCHeadGR){
        this.txtDCHeadGR = txtDCHeadGR;
        setChanged();
    }
    /** Getter method for txtDCHeadGR */
    String getTxtDCHeadGR(){
        return this.txtDCHeadGR;
    }
    
    /** Setter method for txtPostageHeadGR */
    void setTxtPostageHeadGR(String txtPostageHeadGR){
        this.txtPostageHeadGR = txtPostageHeadGR;
        setChanged();
    }
    /** Getter method for txtPostageHeadGR */
    String getTxtPostageHeadGR(){
        return this.txtPostageHeadGR;
    }
    
    //    /** Setter method for cboProdCurrency */
    //    void setCboProdCurrency(String cboProdCurrency){
    //        this.cboProdCurrency = cboProdCurrency;
    //        setChanged();
    //    }
    //    /** Getter method for cboProdCurrency */
    //    String getCboProdCurrency(){
    //        return this.cboProdCurrency;
    //    }
    
    /** Setter method for txtProductIdGR */
    void setTxtProductIdGR(String txtProductIdGR){
        this.txtProductIdGR = txtProductIdGR;
        setChanged();
    }
    /** Getter method for txtProductIdGR */
    String getTxtProductIdGR(){
        return this.txtProductIdGR;
    }
    
    /** Setter method for txtProductDescGR */
    void setTxtProductDescGR(String txtProductDescGR){
        this.txtProductDescGR = txtProductDescGR;
        setChanged();
    }
    /** Getter method for txtProductDescGR */
    String getTxtProductDescGR(){
        return this.txtProductDescGR;
    }
    
    /** Setter method for cboLapsedPeriodGR */
    void setCboLapsedPeriodGR(String cboLapsedPeriodGR){
        this.cboLapsedPeriodGR = cboLapsedPeriodGR;
        setChanged();
    }
    /** Getter method for cboLapsedPeriodGR */
    String getCboLapsedPeriodGR(){
        return this.cboLapsedPeriodGR;
    }
    
    /** Setter method for txtLapsedPeriodGR */
    void setTxtLapsedPeriodGR(String txtLapsedPeriodGR){
        this.txtLapsedPeriodGR = txtLapsedPeriodGR;
        setChanged();
    }
    /** Getter method for txtLapsedPeriodGR */
    String getTxtLapsedPeriodGR(){
        return this.txtLapsedPeriodGR;
    }
    
    /** Setter method for rdoIsLapsedGR_Yes */
    void setRdoIsLapsedGR_Yes(boolean rdoIsLapsedGR_Yes){
        this.rdoIsLapsedGR_Yes = rdoIsLapsedGR_Yes;
        setChanged();
    }
    /** Getter method for rdoIsLapsedGR_Yes */
    boolean getRdoIsLapsedGR_Yes(){
        return this.rdoIsLapsedGR_Yes;
    }
    
    /** Setter method for rdoIsLapsedGR_No */
    void setRdoIsLapsedGR_No(boolean rdoIsLapsedGR_No){
        this.rdoIsLapsedGR_No = rdoIsLapsedGR_No;
        setChanged();
    }
    /** Getter method for rdoIsLapsedGR_No */
    boolean getRdoIsLapsedGR_No(){
        return this.rdoIsLapsedGR_No;
    }
    
    /** Setter method for txtLapsedHeadGR */
    void setTxtLapsedHeadGR(String txtLapsedHeadGR){
        this.txtLapsedHeadGR = txtLapsedHeadGR;
        setChanged();
    }
    /** Getter method for txtLapsedHeadGR */
    String getTxtLapsedHeadGR(){
        return this.txtLapsedHeadGR;
    }
    
    /** Setter method for rdoEFTProductGR_Yes */
    void setRdoEFTProductGR_Yes(boolean rdoEFTProductGR_Yes){
        this.rdoEFTProductGR_Yes = rdoEFTProductGR_Yes;
        setChanged();
    }
    /** ter method for rdoEFTProductGR_Yes */
    boolean getRdoEFTProductGR_Yes(){
        return this.rdoEFTProductGR_Yes;
    }
    
    /** Setter method for rdoEFTProductGR_No */
    void setRdoEFTProductGR_No(boolean rdoEFTProductGR_No){
        this.rdoEFTProductGR_No = rdoEFTProductGR_No;
        setChanged();
    }
    /** Getter method for rdoEFTProductGR_No */
    boolean getRdoEFTProductGR_No(){
        return this.rdoEFTProductGR_No;
    }
    
    /** Setter method for rdoPrintServicesGR_Yes */
    void setRdoPrintServicesGR_Yes(boolean rdoPrintServicesGR_Yes){
        this.rdoPrintServicesGR_Yes = rdoPrintServicesGR_Yes;
        setChanged();
    }
    /** Getter method for rdoPrintServicesGR_Yes */
    boolean getRdoPrintServicesGR_Yes(){
        return this.rdoPrintServicesGR_Yes;
    }
    
    /** Setter method for rdoPrintServicesGR_No */
    void setRdoPrintServicesGR_No(boolean rdoPrintServicesGR_No){
        this.rdoPrintServicesGR_No = rdoPrintServicesGR_No;
        setChanged();
    }
    /** Getter method for rdoPrintServicesGR_No */
    boolean getRdoPrintServicesGR_No(){
        return this.rdoPrintServicesGR_No;
    }
    
    /** Setter method for rdoSeriesGR_Yes */
    void setRdoSeriesGR_Yes(boolean rdoSeriesGR_Yes){
        this.rdoSeriesGR_Yes = rdoSeriesGR_Yes;
        setChanged();
    }
    /** Getter method for rdoSeriesGR_Yes */
    boolean getRdoSeriesGR_Yes(){
        return this.rdoSeriesGR_Yes;
    }
    
    /** Setter method for rdoSeriesGR_No */
    void setRdoSeriesGR_No(boolean rdoSeriesGR_No){
        this.rdoSeriesGR_No = rdoSeriesGR_No;
        setChanged();
    }
    /** Getter method for rdoSeriesGR_No */
    boolean getRdoSeriesGR_No(){
        return this.rdoSeriesGR_No;
    }
    
    /** Setter method for txtCashLimitGR */
    void setTxtCashLimitGR(String txtCashLimitGR){
        this.txtCashLimitGR = txtCashLimitGR;
        setChanged();
    }
    /** Getter method for txtCashLimitGR */
    String getTxtCashLimitGR(){
        return this.txtCashLimitGR;
    }
    
    /** Setter method for txtRemarksGR */
    void setTxtRemarksGR(String txtRemarksGR){
        this.txtRemarksGR = txtRemarksGR;
        setChanged();
    }
    /** Getter method for txtRemarksGR */
    String getTxtRemarksGR(){
        return this.txtRemarksGR;
    }
    
    /** Setter method for txtPerfix */
    void setTxtPerfix(String txtPerfix){
        this.txtPerfix = txtPerfix;
        setChanged();
    }
    /** Getter method for txtPerfix */
    String getTxtPerfix(){
        return this.txtPerfix;
    }
    
    /** Setter method for txtSuffix */
    void setTxtSuffix(String txtSuffix){
        this.txtSuffix = txtSuffix;
        setChanged();
    }
    /** Getter method for txtSuffix */
    String getTxtSuffix(){
        return this.txtSuffix;
    }
    
    /** Setter method for cboPayableBranchGR */
    void setCboPayableBranchGR(String cboPayableBranchGR){
        this.cboPayableBranchGR = cboPayableBranchGR;
        setChanged();
    }
    /** Getter method for cboPayableBranchGR */
    String getCboPayableBranchGR(){
        return this.cboPayableBranchGR;
    }
    
    /** Setter method for txtBankCode */
    void setTxtBankCode(String txtBankCode){
        this.txtBankCode = txtBankCode;
        setChanged();
    }
    /** Getter method for txtBankCode */
    String getTxtBankCode(){
        return this.txtBankCode;
    }
    
    /** Setter method for txtBranchName */
    void setTxtBranchName(String txtBranchName){
        this.txtBranchName = txtBranchName;
        setChanged();
    }
    /** Getter method for txtBranchName */
    String getTxtBranchName(){
        return this.txtBranchName;
    }
    
    /** Setter method for txtBranchCodeBR */
    void setTxtBranchCodeBR(String txtBranchCodeBR){
        this.txtBranchCodeBR = txtBranchCodeBR;
        setChanged();
    }
    /** Getter method for txtBranchCodeBR */
    String getTxtBranchCodeBR(){
        return this.txtBranchCodeBR;
    }
    
    /** Setter method for cboRttTypeBR */
    void setCboRttTypeBR(String cboRttTypeBR){
        this.cboRttTypeBR = cboRttTypeBR;
        setChanged();
    }
    /** Getter method for cboRttTypeBR */
    String getCboRttTypeBR(){
        return this.cboRttTypeBR;
    }
    
    /** Setter method for txtRttLimitBR */
    void setTxtRttLimitBR(String txtRttLimitBR){
        this.txtRttLimitBR = txtRttLimitBR;
        setChanged();
    }
    /** Getter method for txtRttLimitBR */
    String getTxtRttLimitBR(){
        return this.txtRttLimitBR;
    }
    
    /** Setter method for txtIVNoRR */
    void setTxtIVNoRR(String txtIVNoRR){
        this.txtIVNoRR = txtIVNoRR;
        setChanged();
    }
    /** Getter method for txtIVNoRR */
    String getTxtIVNoRR(){
        return this.txtIVNoRR;
    }
    
    /** Setter method for txtOVNoRR */
    void setTxtOVNoRR(String txtOVNoRR){
        this.txtOVNoRR = txtOVNoRR;
        setChanged();
    }
    /** Getter method for txtOVNoRR */
    String getTxtOVNoRR(){
        return this.txtOVNoRR;
    }
    
    /** Setter method for txtMinAmtRR */
    void setTxtMinAmtRR(String txtMinAmtRR){
        this.txtMinAmtRR = txtMinAmtRR;
        setChanged();
    }
    /** Getter method for txtMinAmtRR */
    String getTxtMinAmtRR(){
        return this.txtMinAmtRR;
    }
    
    /** Setter method for txtMaxAmtRR */
    void setTxtMaxAmtRR(String txtMaxAmtRR){
        this.txtMaxAmtRR = txtMaxAmtRR;
        setChanged();
    }
    /** Getter method for txtMaxAmtRR */
    String getTxtMaxAmtRR(){
        return this.txtMaxAmtRR;
    }
    
    /** Setter method for cboChargeType */
    void setCboChargeType(String cboChargeType){
        this.cboChargeType = cboChargeType;
        setChanged();
    }
    /** Getter method for cboChargeType */
    String getCboChargeType(){
        return this.cboChargeType;
    }
    
    /** Setter method for cboCategory */
    void setCboCategory(String cboCategory){
        this.cboCategory = cboCategory;
        setChanged();
    }
    /** Getter method for cboCategory */
    String getCboCategory(){
        return this.cboCategory;
    }
    
    /** Setter method for cboAmtRangeFrom */
    void setCboAmtRangeFrom(String cboAmtRangeFrom){
        this.cboAmtRangeFrom = cboAmtRangeFrom;
        setChanged();
    }
    /** Getter method for cboAmtRangeFrom */
    String getCboAmtRangeFrom(){
        return this.cboAmtRangeFrom;
    }
    
    /** Setter method for cboAmtRangeTo */
    void setCboAmtRangeTo(String cboAmtRangeTo){
        this.cboAmtRangeTo = cboAmtRangeTo;
        setChanged();
    }
    /** Getter method for cboAmtRangeTo */
    String getCboAmtRangeTo(){
        return this.cboAmtRangeTo;
    }
    
    /** Setter method for cboBehavesLike */
    void setCboBehavesLike(String cboBehavesLike){
        this.cboBehavesLike = cboBehavesLike;
        setChanged();
    }
    /** Getter method for cboBehavesLike */
    String getCboBehavesLike(){
        return this.cboBehavesLike;
    }
    
    /** Setter method for txtCharges */
    void setTxtCharges(String txtCharges){
        this.txtCharges = txtCharges;
        setChanged();
    }
    /** Getter method for txtCharges */
    String getTxtCharges(){
        return this.txtCharges;
    }
    
    /** Setter method for tblRemittanceProductBranch */
    void setTblRemittanceProductBranch(EnhancedTableModel tblRemittanceProductBranch){
        this.tblRemittanceProductBranch = tblRemittanceProductBranch;
        setChanged();
    }
    /** Getter method for tblRemittanceProductBranch */
    EnhancedTableModel getTblBrchRemittNumber(){
        return this.tblRemittanceProductBranch;
    }
    /** Setter method for tblAliasRemittanceProductBranch */
    void setTblAliasRemittanceProductBranch(){
        this.tblAliasRemittanceProductBranch = getTblBrchRemittNumber();
        setChanged();
    }
    /** Getter method for tblRemittanceProductBranch */
    EnhancedTableModel getTblAliasRemittanceProductBranch(){
        return this.tblAliasRemittanceProductBranch;
    }
    
    
    /** Setter method for cbmPayableBranchGR */
    public void setCbmPayableBranchGR(ComboBoxModel cbmPayableBranchGR) {
        this.cbmPayableBranchGR = cbmPayableBranchGR;
    }
    /** Getter method for cbmPayableBranchGR */
    public ComboBoxModel getCbmPayableBranchGR() {
        return cbmPayableBranchGR;
    }
    
    //    /** Setter method for cbmProdCurrency */
    //    public void setCbmProdCurrency(ComboBoxModel cbmProdCurrency) {
    //        this.cbmProdCurrency = cbmProdCurrency;
    //    }
    //    /** Getter method for cbmProdCurrency */
    //    public ComboBoxModel getCbmProdCurrency() {
    //        return cbmProdCurrency;
    //    }
    
    /** Setter method for setCbmChargeType */
    public void setCbmChargeType(ComboBoxModel cbmChargeType) {
        this.cbmChargeType = cbmChargeType;
    }
    /** Getter method for getCbmChargeType */
    public ComboBoxModel getCbmChargeType() {
        return cbmChargeType;
    }
    
    /** Setter method for setCbmCategory */
    public void setCbmCategory(ComboBoxModel cbmCategory) {
        this.cbmCategory = cbmCategory;
    }
    /** Getter method for getCbmCategory */
    public ComboBoxModel getCbmCategory() {
        return cbmCategory;
    }
    
    /** Setter method for setCbmAmtRangeFrom */
    public void setCbmAmtRangeFrom(ComboBoxModel cbmAmtRangeFrom) {
        this.cbmAmtRangeFrom = cbmAmtRangeFrom;
    }
    /** Getter method for getCbmAmtRangeFrom */
    public ComboBoxModel getCbmAmtRangeFrom() {
        return cbmAmtRangeFrom;
    }
    
    /** Setter method for setCbmAmtRangeTo */
    public void setCbmAmtRangeTo(ComboBoxModel cbmAmtRangeTo) {
        this.cbmAmtRangeTo = cbmAmtRangeTo;
    }
    /** Getter method for getCbmAmtRangeTo */
    public ComboBoxModel getCbmAmtRangeTo() {
        return cbmAmtRangeTo;
    }
    
    /** Setter method for cbmLapsedPeriodGR */
    public void setCbmLapsedPeriodGR(ComboBoxModel cbmLapsedPeriodGR) {
        this.cbmLapsedPeriodGR = cbmLapsedPeriodGR;
    }
    /** Getter method for cbmLapsedPeriodGR */
    public ComboBoxModel getCbmLapsedPeriodGR() {
        return cbmLapsedPeriodGR;
    }
    
    
    /** Setter method for cbmBehavesLike */
    public void setCbmBehavesLike(ComboBoxModel cbmBehavesLike) {
        this.cbmBehavesLike = cbmBehavesLike;
    }
    /** Getter method for cbmBehavesLike */
    public ComboBoxModel getCbmBehavesLike() {
        return cbmBehavesLike;
    }
    
    /** Setter method for cbmRttTypeBR */
    public void setCbmRttTypeBR(ComboBoxModel cbmRttTypeBR) {
        this.cbmRttTypeBR = cbmRttTypeBR;
    }
    
    /** Getter method for cbmRttTypeBR */
    public ComboBoxModel getCbmRttTypeBR() {
        return cbmRttTypeBR;
    }
    
    /** Setter method for tblRemittanceProductCharge */
    void setTblRemittanceProductCharge(EnhancedTableModel tblRemittanceProductCharge){
        this.tblRemittanceProductCharge = tblRemittanceProductCharge;
        setChanged();
    }
    /** Getter method for tblRemittanceProductCharge */
    EnhancedTableModel getTblRemittanceProductCharge(){
        return this.tblRemittanceProductCharge;
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
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    
    public void setActionType(int action) {
        actionType = action;
        setChanged();
    }
    public int getActionType(){
        return actionType;
    }
    
    /** A method to set the combo box values */
    private void fillDropdown() throws Exception{
        try{
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME,null);
            final ArrayList lookup_keys = new ArrayList();
            
            //            lookup_keys.add("FOREX.CURRENCY");
            lookup_keys.add("PERIOD");
            lookup_keys.add("REMIT_PROD_PAYBRAN");
            lookup_keys.add("REMITTANCE_PAYMENT.INSTRUMENT_TYPE");
            lookup_keys.add("REMITTANCE_PROD.CHARGE");
            lookup_keys.add("CATEGORY");
            lookup_keys.add("DEPOSIT.AMT_RANGE_FROM");
            lookup_keys.add("DEPOSIT.AMT_RANGE");
            lookup_keys.add("REMITTANCE.BEHAVES");
            lookup_keys.add("FOREX.RATE_TYPE");
            lookup_keys.add("PRODUCTTYPE");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookUpHash);
            lookUpHash = null;
            
            //            getKeyValue((HashMap)keyValue.get("FOREX.CURRENCY"));
            //            cbmProdCurrency = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("PERIOD"));
            cbmLapsedPeriodGR = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("REMIT_PROD_PAYBRAN"));
            cbmPayableBranchGR = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("REMITTANCE_PAYMENT.INSTRUMENT_TYPE"));
            cbmRttTypeBR = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("REMITTANCE_PROD.CHARGE"));
            cbmChargeType = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("CATEGORY"));
            cbmCategory = new ComboBoxModel(key,value);
            
//            getKeyValue((HashMap)keyValue.get("DEPOSIT.AMT_RANGE_FROM"));
//            cbmAmtRangeFrom = new ComboBoxModel(key,value);
//            getKeyValue((HashMap)keyValue.get("DEPOSIT.AMT_RANGE"));
//            cbmAmtRangeTo = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("REMITTANCE.BEHAVES"));
            cbmBehavesLike = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("FOREX.RATE_TYPE")); // For Amount/Percentage dropdown
            cbmRateType = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap) keyValue.get("PRODUCTTYPE"));
            cbmProdTypeCr = new ComboBoxModel(key, value);
            
            keyValue = null;
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    /** this method get the existing data from the Table **/
    public void existingData(){
        int remittanceProductBranchRowCount = tblRemittanceProductBranch.getRowCount();
        int remittanceProductBranchColumnCount = tblRemittanceProductBranch.getColumnCount();
        // Gets data from Remittance Product Branch CTable
        for(int i=0;i<remittanceProductBranchRowCount;i++){
            remitProductBrchColumnElement = new ArrayList();
            for(int j=0;j<remittanceProductBranchColumnCount;j++){
                remitProductBrchColumnElement.add(tblRemittanceProductBranch.getValueAt(i,j));
            }
            remitProductBrchRowData.add(remitProductBrchColumnElement);
            remitProductBrchColumnElement = null;
        }
    }
    
    /** Method to check Cbo AmtRangeFrom is always lesser than Cbo AmtRangeTo */
    /*public boolean getAmountRange(){
        boolean flag = false;
        int amountRangeFrom = 0;
        int amountRangeTo = 0;
        if(getCboAmtRangeFrom().length() > 0){
            amountRangeFrom = Integer.parseInt(getCbmAmtRangeFrom().getKeyForSelected().toString());
        }
        if(getCboAmtRangeTo().length() > 0){
            amountRangeTo = Integer.parseInt(getCbmAmtRangeTo().getKeyForSelected().toString());
        }
        if(amountRangeTo < amountRangeFrom){
            // Checks the Amount From and To Ranges
            String[] options = {objRemittanceProductRB.getString("cDialogOk")};
            COptionPane.showOptionDialog(null, objRemittanceProductRB.getString("amountMinMaxWarning"), CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
        } else { // This is for checking the range already exist or not
            int cnt = tblRemittanceProductCharge.getRowCount();
            double amtTableRangeFrom = 0;
            double amtTableRangeTo = 0;
            
            String chrgTableType = "";
            String categoryTable = "";
            
            for (int i=0; i < cnt; i++) {
                chrgTableType = CommonUtil.convertObjToStr(tblRemittanceProductCharge.getValueAt(i, 0));
                categoryTable = CommonUtil.convertObjToStr(tblRemittanceProductCharge.getValueAt(i, 1));
                
                if (CommonUtil.convertObjToStr(cbmChargeType.getSelectedItem()).equals(chrgTableType) && 
                CommonUtil.convertObjToStr(cbmCategory.getSelectedItem()).equals(categoryTable)) {
                    amtTableRangeFrom = CommonUtil.convertObjToDouble(tblRemittanceProductCharge.getValueAt(i, 2)).doubleValue();
                    amtTableRangeTo = CommonUtil.convertObjToDouble(tblRemittanceProductCharge.getValueAt(i, 3)).doubleValue();
                    
                    //__ Check for the Duplication of Amount Range
                    if(((Double.parseDouble((String)cbmAmtRangeFrom.getKeyForSelected()) < amtTableRangeFrom)
                    && (Double.parseDouble((String)cbmAmtRangeTo.getKeyForSelected()) < amtTableRangeFrom))
                    ||((Double.parseDouble((String)cbmAmtRangeFrom.getKeyForSelected()) > amtTableRangeTo)
                    && (Double.parseDouble((String)cbmAmtRangeTo.getKeyForSelected()) > amtTableRangeTo))){
                        // Checks the Amount From and To Ranges
                    } else {
                        String[] options = {objRemittanceProductRB.getString("cDialogOk")};
                        COptionPane.showOptionDialog(null, objRemittanceProductRB.getString("existanceWarning"), CommonConstants.WARNINGTITLE,
                            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                        break;
                    }
                }
            }
        }
        return flag;
    } */   
    
    
    /** Method to set lable of Remittance Product ID in Remit Prod Brch and Remit Prod Chrg */
    public void setLableProductID(){
        setLblRemitProdIDBranch(getTxtProductIdGR());
        setLblRemitProdIDCharge(getTxtProductIdGR());
        notifyObservers();
    }
    
    /** Method to set lable of Remittance Product DESC in Remit Prod Brch and Remit Prod Chrg */
    public void setLableProductDesc(){
        setLblRemitProdDescBranch(getTxtProductDescGR());
        setLblRemitProdDescCharge(getTxtProductDescGR());
        notifyObservers();
    }
    
    /** This method gets necessary fields and accordingly this data is Inserted,
     * Updated or Deleted
     */
    public int addRemittanceProductBranchTab(){
        ArrayList comp = new ArrayList();
        optionRemitProdBrch = -1;
        String bankNameColumnData = new String();
        String branchCodeColumnData = new String();
//        bankNameColumnData = getBankName();
        bankNameColumnData = getTxtBankCode();
        branchCodeColumnData = getTxtBranchCodeBR();
        try{
            remittanceProductBranchTabRow = new ArrayList();
            remitProdBrchTabNew = new ArrayList();
            
            // remitProdBrchTabNew (ArrayList) to be added in the CTable of Remit Prod Branch
//            remitProdBrchTabNew.add(getBankName());
            remitProdBrchTabNew.add(getTxtBankCode());
            remitProdBrchTabNew.add(getTxtBranchCodeBR());
            remitProdBrchTabNew.add(getTxtBranchName());
            remitProdBrchTabNew.add(getTxtIVNoRR());
            remitProdBrchTabNew.add(getTxtOVNoRR());
            remitProdBrchTabNew.add(getTxtMinAmtRR());
            remitProdBrchTabNew.add(getTxtMaxAmtRR());
            
            // remitProdBrchTabNew (ArrayList) to be added in the HashMap
//            remittanceProductBranchTabRow.add(getBankName());
            remittanceProductBranchTabRow.add(getTxtBankCode());
            remittanceProductBranchTabRow.add(getTxtBranchCodeBR());
            remittanceProductBranchTabRow.add(getTxtBranchName());
            remittanceProductBranchTabRow.add(getCboRttTypeBR());
            remittanceProductBranchTabRow.add(getTxtRttLimitBR());
            remittanceProductBranchTabRow.add(getTxtIVNoRR());
            remittanceProductBranchTabRow.add(getTxtOVNoRR());
            remittanceProductBranchTabRow.add(getTxtMinAmtRR());
            remittanceProductBranchTabRow.add(getTxtMaxAmtRR());
            
            ArrayList data = tblRemittanceProductBranch.getDataArrayList();
            final int dataSize = data.size();
            boolean exist = false;
            boolean rowExists = false;
            
            // If Remittance Product Branch Table is selected and the Row exists
            if(remitProdBrnchTabSelected == true){
                exist = true;
                rowExists = checkRemitProdBrchRow(bankNameColumnData, branchCodeColumnData);
                if(!rowExists){
                    updateRemittanceProductBranchTab(remitProdBrnchSelectedRow);
                    doremittProductBrchUpdateData(remitProdBrnchSelectedRow);
                    resetRemitProdBrchFields();
                }else{
                    resetRemitProdBrchFields();
                }
            }
            if(remitProdBrnchTabSelected != true){
                for (int i=0;i<dataSize;i++){
                    if ((((ArrayList)data.get(i)).get(0)).equals(bankNameColumnData) && (((ArrayList)data.get(i)).get(1)).equals(branchCodeColumnData)){
                        // Checking whether existing BRANCH_CODE is equal new BRANCH_CODE
                        exist = true;
                        String[] options = {objRemittanceProductRB.getString("cDialogYes"),objRemittanceProductRB.getString("cDialogNo"),objRemittanceProductRB.getString("cDialogCancel")};
                        optionRemitProdBrch = COptionPane.showOptionDialog(null, objRemittanceProductRB.getString("remitNoDuplicate"), CommonConstants.WARNINGTITLE,
                        COptionPane.YES_NO_CANCEL_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                        if (optionRemitProdBrch == 0){
                            // option selected is Yes
                            updateRemittanceProductBranchTab(i);
                            doremittProductBrchUpdateData(i);
                            resetRemitProdBrchFields();
                        }else if (optionRemitProdBrch == 1){
                            // option selected is No
                            resetRemitProdBrchFields();
                        }
                        break;
                    }
                }
            }
            if (!exist){
                /** If Remittance Product Branch is not selected and
                 * Entered data is not in the table.  */
                doRemittProductBrchNewData();
                insertRemittProductBrchTab();
            }
            setChanged();
            notifyObservers();
            remittanceProductBranchTabRow = null;
            remitProdBrchTabNew = null;
            remitProdBrnchTabSelected = false;
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return optionRemitProdBrch;
    }
    
    // Updates the CTable....
    private void updateRemittanceProductBranchTab(int row) throws Exception{
//        tblRemittanceProductBranch.setValueAt(getBankName(), row, 0);
        tblRemittanceProductBranch.setValueAt(getTxtBankCode(), row, 0);
        tblRemittanceProductBranch.setValueAt(getTxtBranchCodeBR(), row, 1);
        tblRemittanceProductBranch.setValueAt(getTxtBranchName(), row, 2);
        tblRemittanceProductBranch.setValueAt(getTxtIVNoRR(), row, 3);
        tblRemittanceProductBranch.setValueAt(getTxtOVNoRR(), row, 4);
        tblRemittanceProductBranch.setValueAt(getTxtMinAmtRR(), row, 5);
        tblRemittanceProductBranch.setValueAt(getTxtMaxAmtRR(), row, 6);
        this.tblAliasRemittanceProductBranch = this.tblRemittanceProductBranch;
        setChanged();
        notifyObservers();
    }
    
    /** This method get the update data entered (with option YES) **/
    private void doremittProductBrchUpdateData(int row){
        hashUpdate(row);
    }
    
    /** Method to delete the row from the ArrayList and update the new datas in to the ArrayList */
    private void hashUpdate(int row){
        ArrayList tempArrayList = new ArrayList();
        HashMap tempHash = new HashMap();
        int rowSize = arrayListRemitProdBrchTabUpdate.size();
        HashMap hashRemitProdBrchTabUpdate;
        for (int j = 0;j<rowSize;j++){
            hashRemitProdBrchTabUpdate = new HashMap();
            hashRemitProdBrchTabUpdate = (HashMap)arrayListRemitProdBrchTabUpdate.get(j);
            if(CommonUtil.convertObjToStr(hashRemitProdBrchTabUpdate.get("BANK_NAME")).equals(getBankName()) && CommonUtil.convertObjToStr(hashRemitProdBrchTabUpdate.get("BRANCH_NAME")).equals(getTxtBranchName())){
                // Deletes the row element from the ArrayList
                arrayListRemitProdBrchTabUpdate.remove(j);
                break;
            }
            hashRemitProdBrchTabUpdate = null;
        }
        // Updates elements to the ArrayList
//        tempHash.put("BANK_NAME",getBankName());
        tempHash.put("BANK_CODE",getTxtBankCode());
        tempHash.put("BRANCH_CODE", getTxtBranchCodeBR());
        tempHash.put("BRANCH_NAME", getTxtBranchName());
        tempHash.put("REMITTANCE_TYPE", getCboRttTypeBR());
        tempHash.put("REMITTANCE_LIMIT", getTxtRttLimitBR());
        tempHash.put("INWARD_VARIABLE_NO", getTxtIVNoRR());
        tempHash.put("OUTWARD_VARIABLE_NO", getTxtOVNoRR());
        tempHash.put("MIN_AMT", getTxtMinAmtRR());
        tempHash.put("MAX_AMT", getTxtMaxAmtRR());
        arrayListRemitProdBrchTabUpdate.add(tempHash);
        
        // Adds the element that has to be updated to the remitProductBrchExistingData (UPDATE DATA)
//        tempArrayList.add(getBankName());
        tempArrayList.add(getTxtBankCode());
        tempArrayList.add(getTxtBranchCodeBR());
        tempArrayList.add(getTxtBranchName());
        tempArrayList.add(getCboRttTypeBR());
        tempArrayList.add(getTxtRttLimitBR());
        tempArrayList.add(getTxtIVNoRR());
        tempArrayList.add(getTxtOVNoRR());
        tempArrayList.add(getTxtMinAmtRR());
        tempArrayList.add(getTxtMaxAmtRR());
        remitProductBrchExistingData.add(tempArrayList);
    }
    
    /** This method get the new data entered **/
    private void doRemittProductBrchNewData(){
        tableHashData = new HashMap();
//        tableHashData.put("BANK_NAME",getBankName() );
        tableHashData.put("BANK_CODE",getTxtBankCode() );
        tableHashData.put("BRANCH_CODE", getTxtBranchCodeBR());
        tableHashData.put("BRANCH_NAME", getTxtBranchName());
        tableHashData.put("REMITTANCE_TYPE", getCboRttTypeBR());
        tableHashData.put("REMITTANCE_LIMIT", getTxtRttLimitBR());
        tableHashData.put("INWARD_VARIABLE_NO", getTxtIVNoRR());
        tableHashData.put("OUTWARD_VARIABLE_NO", getTxtOVNoRR());
        tableHashData.put("MIN_AMT", getTxtMinAmtRR());
        tableHashData.put("MAX_AMT", getTxtMaxAmtRR());
        arrayListRemitProdBrchTabUpdate.add(tableHashData);
        remittProductBrchNewData.add(remittanceProductBranchTabRow);
        tableHashData = null;
    }
    
    // Insert into the Table in Remittance Product Branch ...
    private void insertRemittProductBrchTab() throws Exception{
        int row = tblRemittanceProductBranch.getRowCount();
        tblRemittanceProductBranch.insertRow(row,remitProdBrchTabNew);
        this.tblAliasRemittanceProductBranch = this.tblRemittanceProductBranch;
        resetRemitProdBrchFields();
    }
    
    /** Checks if the selected row is present in the Remittance Product Branch CTable */
    private boolean checkRemitProdBrchRow(String bankNameColumnData, String branchCodeColumnData){
        boolean rowExists = false;
        int rowSize = arrayListRemitProdBrchTabUpdate.size();
        ArrayList tempArrayList = new ArrayList();
        HashMap hashRemitProdBrchTabUpdate;
        for(int i=0;i<rowSize;i++){
            hashRemitProdBrchTabUpdate = new HashMap();
            hashRemitProdBrchTabUpdate = (HashMap)arrayListRemitProdBrchTabUpdate.get(i);
//            if((CommonUtil.convertObjToStr(hashRemitProdBrchTabUpdate.get("BANK_NAME")).equals(bankNameColumnData)) && (CommonUtil.convertObjToStr(hashRemitProdBrchTabUpdate.get("BRANCH_NAME")).equals(branchCodeColumnData))){
//                tempArrayList.add(CommonUtil.convertObjToStr(hashRemitProdBrchTabUpdate.get("BANK_NAME")));
            if((CommonUtil.convertObjToStr(hashRemitProdBrchTabUpdate.get("BANK_CODE")).equals(bankNameColumnData)) && (CommonUtil.convertObjToStr(hashRemitProdBrchTabUpdate.get("BRANCH_CODE")).equals(branchCodeColumnData))){    
            tempArrayList.add(CommonUtil.convertObjToStr(hashRemitProdBrchTabUpdate.get("BANK_CODE")));
//                tempArrayList.add(CommonUtil.convertObjToStr(hashRemitProdBrchTabUpdate.get("BRANCH_NAME")));
                tempArrayList.add(CommonUtil.convertObjToStr(hashRemitProdBrchTabUpdate.get("BRANCH_CODE")));
                tempArrayList.add(CommonUtil.convertObjToStr(hashRemitProdBrchTabUpdate.get("REMITTANCE_TYPE")));
                tempArrayList.add(CommonUtil.convertObjToStr(hashRemitProdBrchTabUpdate.get("REMITTANCE_LIMIT")));
                tempArrayList.add(CommonUtil.convertObjToStr(hashRemitProdBrchTabUpdate.get("INWARD_VARIABLE_NO")));
                tempArrayList.add(CommonUtil.convertObjToStr(hashRemitProdBrchTabUpdate.get("OUTWARD_VARIABLE_NO")));
                tempArrayList.add(CommonUtil.convertObjToStr(hashRemitProdBrchTabUpdate.get("MIN_AMT")));
                tempArrayList.add(CommonUtil.convertObjToStr(hashRemitProdBrchTabUpdate.get("MAX_AMT")));
                break;
            }
            hashRemitProdBrchTabUpdate = null;
        }
        if(tempArrayList.equals(remittanceProductBranchTabRow)){
            rowExists = true;
        }
        tempArrayList = null;
        return rowExists;
    }
    
    
    /** Checks if the selected row is present in the Remittance Product Charge CTable */
    private boolean checkRemitProdChrgRow(int remitProdBrnchSelectedRow){
        boolean rowExists = false;
        ArrayList tempArrayList = new ArrayList();
        tempArrayList.add(tblRemittanceProductCharge.getValueAt(remitProdBrnchSelectedRow, 0).toString());
        tempArrayList.add(tblRemittanceProductCharge.getValueAt(remitProdBrnchSelectedRow, 1).toString());
        tempArrayList.add(tblRemittanceProductCharge.getValueAt(remitProdBrnchSelectedRow, 2).toString());
        tempArrayList.add(tblRemittanceProductCharge.getValueAt(remitProdBrnchSelectedRow, 3).toString());
        tempArrayList.add(tblRemittanceProductCharge.getValueAt(remitProdBrnchSelectedRow, 4).toString());
        if(tempArrayList.equals(remittanceProductChargeTabRow)){
            rowExists = true;
        }
        tempArrayList = null;
        return rowExists;
    }
    
    /** Checks whether the enteried Product ID and Descrption are unique */
    public boolean uniqueProduct(){
        boolean dataExist = false;
        HashMap whereMap = new HashMap();
        HashMap resultMap = new HashMap();
        int countUniqueProductID = 0;
        int countUniqueProductDesc = 0;
        if(getActionType() == ClientConstants.ACTIONTYPE_NEW || getActionType() == ClientConstants.ACTIONTYPE_COPY){
            whereMap.put("PROD_ID",  getTxtProductIdGR());
            whereMap.put("PROD_DESC",getTxtProductDescGR());
            //System.out.println("@@@@@ getTxtProductIdGR()"+ getTxtProductIdGR());
        }else if(getActionType() == ClientConstants.ACTIONTYPE_EDIT ){
            whereMap.put("PROD_ID",  "");
            whereMap.put("PROD_DESC",getTxtProductDescGR());
            //System.out.println("@@@@@ whereMap()"+ getTxtProductIdGR());
        }
        List resultList = ClientUtil.executeQuery("getProductUnique", whereMap);
        
        if (resultList.size() > 0) {
            String strProductID = CommonUtil.convertObjToStr(((HashMap)resultList.get(0)).get("PROD_ID"));
            String strProductDesc = CommonUtil.convertObjToStr(((HashMap)resultList.get(0)).get("PROD_DESC"));
            countUniqueProductID = Integer.parseInt(strProductID);
            countUniqueProductDesc = Integer.parseInt(strProductDesc);
        }
        
        String yy =new String();
        if(getActionType() != ClientConstants.ACTIONTYPE_EDIT ){
            if(countUniqueProductID != 0 && countUniqueProductDesc == 0){
                dataExist = true;
                displayCDialogue("uniqueProductIDWarning");
            }else if(countUniqueProductID == 0 && countUniqueProductDesc != 0){
                dataExist = true;
                displayCDialogue("uniqueProductDESCWarning");
            }else if(countUniqueProductID != 0 && countUniqueProductDesc != 0){
                dataExist = true;
                displayCDialogue("uniqueProductIDandDESCWarning");
            }
        }else if (getActionType() == ClientConstants.ACTIONTYPE_EDIT && (!productDesc.equalsIgnoreCase(getTxtProductDescGR()))){
            if(countUniqueProductID == 0 && countUniqueProductDesc != 0){
                dataExist = true;
                displayCDialogue("uniqueProductDESCWarning");
            }
        }
        return dataExist;
    }
    
    public void displayCDialogue(String warningMessage){
        String[] options = {objRemittanceProductRB.getString("cDialogOk")};
        optionUniqueRemitProdID = COptionPane.showOptionDialog(null, objRemittanceProductRB.getString(warningMessage), CommonConstants.WARNINGTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
    }
    
    public void doSave(){
        initialise();
        if(getActionType() != ClientConstants.ACTIONTYPE_DELETE ){
            updateRemitProdBrchData();
            //            updateRemitProdChrgData();
            deleteRemitProdBrchData();
            //            deleteRemitProdChrgData();
            insertRemitProdBrchData();
            //            insertRemitProdChrgData();
        }
        doAction();
        deinitialise();
    }
    
    /** This method gets the data that has to be inserted into the database
     *  data the data that is already existing
     *  remittProductBrchNewData the data that is newly added
     *  remittProductBrchInsertData the data that is has to be inserted into the database
     */
    public void insertRemitProdBrchData(){        
        ArrayList remitProdBrchdata = new ArrayList();        
        remitProdBrchdata = tblRemittanceProductBranch.getDataArrayList();        
        int rowData = remitProdBrchdata.size();
        int rowRemittProductBrchNewData = remittProductBrchNewData.size();
        for(int i=0;i<rowData;i++){
            for(int j=0;j<rowRemittProductBrchNewData;j++){
                if(((ArrayList)remittProductBrchNewData.get(j)).get(0).equals(((ArrayList)remitProdBrchdata.get(i)).get(0))
                && ((ArrayList)remittProductBrchNewData.get(j)).get(1).equals(((ArrayList)remitProdBrchdata.get(i)).get(1))){
                    remittProductBrchInsertData.add(remittProductBrchNewData.get(j));
                }
            }
        }
        remitProdBrchdata = null;
        remittProductBrchNewData.clear();
        if(remittProductBrchInsertData.size() > 0){
            setRemitProdBrchHash(remittProductBrchInsertData, CommonConstants.STATUS_CREATED, CommonConstants.TOSTATUS_INSERT);
        }
        remittProductBrchInsertData.clear();
    }
    
    
    /** This method gets the data that has to be updated into the database
     *  remitProductBrchRowData the data that is already existing
     *  remitProductBrchExistingData the data that is newly added
     *  remittProductBrchUpdateData the data that is has to be updated
     */
    public void updateRemitProdBrchData(){
        int row = remitProductBrchRowData.size();
        int rowremitProductBrchExistingData = remitProductBrchExistingData.size();
        for(int i=0;i<row;i++){
            for(int j=0;j<rowremitProductBrchExistingData;j++){
                if(((ArrayList)remitProductBrchExistingData.get(j)).get(0).equals(CommonUtil.convertObjToStr(((ArrayList)remitProductBrchRowData.get(i)).get(0))) && ((ArrayList)remitProductBrchExistingData.get(j)).get(1).equals(CommonUtil.convertObjToStr(((ArrayList)remitProductBrchRowData.get(i)).get(1)))){
                    remittProductBrchUpdateData.add(remitProductBrchExistingData.get(j));
                }
            }
        }
        remitProductBrchExistingData.clear();
        if(remittProductBrchUpdateData.size() > 0){
            setRemitProdBrchHash(remittProductBrchUpdateData, CommonConstants.STATUS_MODIFIED, CommonConstants.TOSTATUS_UPDATE);
        }
        remittProductBrchUpdateData.clear();
    }
    
    
    /** This method gets the data that has to be deleted from the database
     *  remitProductBrchRowData the data that is already existing
     *  remittProductBrchDeleteRow the data that is newly added
     *  remittProductBrchDeleteData the data that is has to be deleted
     */
    public void deleteRemitProdBrchData(){
        int row = remitProductBrchRowData.size();
        int rowDelete = remittProductBrchDeleteRow.size();
        for(int i=0;i<row;i++){
            for(int j=0;j<rowDelete;j++){
                if(((ArrayList)remittProductBrchDeleteRow.get(j)).get(0).equals(CommonUtil.convertObjToStr(((ArrayList)remitProductBrchRowData.get(i)).get(0))) && ((ArrayList)remittProductBrchDeleteRow.get(j)).get(1).equals(CommonUtil.convertObjToStr(((ArrayList)remitProductBrchRowData.get(i)).get(1)))){
                    remittProductBrchDeleteData.add(remittProductBrchDeleteRow.get(j));
                }
            }
        }
        remittProductBrchDeleteRow.clear();
        if(remittProductBrchDeleteData.size() > 0){
            setRemitProdBrchHash(remittProductBrchDeleteData, CommonConstants.STATUS_DELETED, CommonConstants.TOSTATUS_DELETE);
        }
        remittProductBrchDeleteData.clear();
    }
    
    
    
    private void setRemitProdBrchHash(ArrayList dataArrayList, String status, String command ){
        ArrayList data;
        int arrayListSize = dataArrayList.size();
        for(int i=0;i<dataArrayList.size();i++){
            hash = new HashMap();
            data = new ArrayList();
            data =(ArrayList)dataArrayList.get(i);
            hash.put("BANK_CODE", data.get(0));
            hash.put("BRANCH_CODE", data.get(1));
            hash.put("BRANCH_NAME", data.get(2));
            hash.put("REMITTANCE_TYPE", data.get(3));
            hash.put("REMITTANCE_LIMIT", data.get(4));
            hash.put("INWARD_VARIABLE_NO", data.get(5));
            hash.put("OUTWARD_VARIABLE_NO", data.get(6));
            hash.put("MIN_AMT", data.get(7));
            hash.put("MAX_AMT", data.get(8));
            hash.put("STATUS", status);
            hash.put("COMMAND", command);
            remitProdBrchTO.add(hash);
            data = null;
            hash = null;
        }
    }
    
    
    
    /** To perform the appropriate operation */
    public void doAction() {
        try {
            doActionPerform();
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
        }
    }
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        boolean deleteRemitProdBrchDataExists = false;
        boolean deleteRemitProdChrgDataExists = false;
        final HashMap data = new HashMap();
        ArrayList remitProdBrch = new ArrayList();
        ArrayList arrayRemitProdBrchTabTO = new ArrayList();
        
        RemittanceProductTO objRemittanceProductTO;
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        data.put("COMMAND",getCommand());
        if(getActionType() != ClientConstants.ACTIONTYPE_DELETE ){
            objRemittanceProductTO = setRemittanceProductTO();
            objRemittanceProductTO.setCommand(getCommand());
            data.put("RemittanceProductTO",objRemittanceProductTO);
            if(remitProdBrchTO.size() >0){
                arrayRemitProdBrchTabTO = setRemittanceProductBranchTO( remitProdBrchTO );
                remitProdBrchTOData = null;
                data.put("RemittanceProductBranchTO",arrayRemitProdBrchTabTO);
            }
            
            if (deletedCharges != null) {
                data.put("DELETED_CHARGETO", deletedCharges);
            }
            
            data.put("RemittanceProductChargeTO",hashCharges);
//            System.out.println("hash just before:"+hashCharges);
//            System.out.println("proxy:data:" + data + " : " + operationMap);
            HashMap proxyResultMap = proxy.execute(data, operationMap);
            setProxyReturnMap(proxyResultMap);
            remitProdBrchTO.clear();
            arrayRemitProdBrchTabTO.clear();
            
        }else if(getActionType() == ClientConstants.ACTIONTYPE_DELETE ){
            deleteRemitProdBrchDataExists = remittanceProductBranchDelete();
            deleteRemitProdChrgDataExists = remittanceProductChargeDelete();
            if(deleteRemitProdBrchDataExists || deleteRemitProdChrgDataExists){
                displayCDialogue("deleteWarning");
                setResult(0);
            }
            
            if((!deleteRemitProdBrchDataExists) &&(!deleteRemitProdChrgDataExists)){
                objRemittanceProductTO = new RemittanceProductTO();
                objRemittanceProductTO.setProdId(getTxtProductIdGR());
                objRemittanceProductTO.setCommand(getCommand());
                
                getDeleteData(remitProdBrch);
                
                setRemitProdBrchHash(remitProdBrch, CommonConstants.STATUS_DELETED, CommonConstants.TOSTATUS_DELETE);
                
                arrayRemitProdBrchTabTO = setRemittanceProductBranchTO( remitProdBrchTO );
                
                remitProdBrchTOData = null;
                
                data.put("RemittanceProductTO",objRemittanceProductTO);
                data.put("RemittanceProductBranchTO",arrayRemitProdBrchTabTO);
                
                if (deletedCharges != null) {
                    data.put("DELETED_CHARGETO", deletedCharges);
                }
                
                data.put("RemittanceProductChargeTO",hashCharges);
                HashMap proxyResultMap = proxy.execute(data, operationMap);
                
                setResult(getActionType());
            }
        }
    }
    
    /** Method gets the datas to be deleted */
    private void  getDeleteData(ArrayList remitProdBrch){
        int arrayListRemitProdBrchTabUpdateSize = arrayListRemitProdBrchTabUpdate.size();
        ArrayList tempArrayList ;
        HashMap hashArrayListRemitProdBrchTabUpdate;
        for(int i=0;i<arrayListRemitProdBrchTabUpdateSize;i++){
            tempArrayList = new ArrayList();
            hashArrayListRemitProdBrchTabUpdate = new HashMap();
            hashArrayListRemitProdBrchTabUpdate = (HashMap)arrayListRemitProdBrchTabUpdate.get(i);
//            tempArrayList.add(CommonUtil.convertObjToStr(hashArrayListRemitProdBrchTabUpdate.get("BANK_NAME")));
            tempArrayList.add(CommonUtil.convertObjToStr(hashArrayListRemitProdBrchTabUpdate.get("BANK_CODE")));
            tempArrayList.add(CommonUtil.convertObjToStr(hashArrayListRemitProdBrchTabUpdate.get("BRANCH_CODE")));
            tempArrayList.add(CommonUtil.convertObjToStr(hashArrayListRemitProdBrchTabUpdate.get("BRANCH_NAME")));
            tempArrayList.add(CommonUtil.convertObjToStr(hashArrayListRemitProdBrchTabUpdate.get("REMITTANCE_TYPE")));
            tempArrayList.add(CommonUtil.convertObjToStr(hashArrayListRemitProdBrchTabUpdate.get("REMITTANCE_LIMIT")));
            tempArrayList.add(CommonUtil.convertObjToStr(hashArrayListRemitProdBrchTabUpdate.get("INWARD_VARIABLE_NO")));
            tempArrayList.add(CommonUtil.convertObjToStr(hashArrayListRemitProdBrchTabUpdate.get("OUTWARD_VARIABLE_NO")));
            tempArrayList.add(CommonUtil.convertObjToStr(hashArrayListRemitProdBrchTabUpdate.get("MIN_AMT")));
            tempArrayList.add(CommonUtil.convertObjToStr(hashArrayListRemitProdBrchTabUpdate.get("MAX_AMT")));
            remitProdBrch.add(tempArrayList);
            tempArrayList = null;
            hashArrayListRemitProdBrchTabUpdate = null;
        }
    }
    
    /** Gets the command issued Insert , Upadate or Delete **/
    private String getCommand() throws Exception{
        String command = null;
        switch (actionType) {
            case ClientConstants.ACTIONTYPE_NEW:
                command = CommonConstants.TOSTATUS_INSERT;
                break;
            case ClientConstants.ACTIONTYPE_COPY:
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
    
    /**  Gets the datas from the Fields and sets to Remittance TO */
    public RemittanceProductTO setRemittanceProductTO() {
        final RemittanceProductTO objRemittanceProductTO = new RemittanceProductTO();
        try{
            objRemittanceProductTO.setProdId(getTxtProductIdGR());
            objRemittanceProductTO.setProdDesc(getTxtProductDescGR());
            objRemittanceProductTO.setIssueHd(getTxtIssueHeadGR());
            objRemittanceProductTO.setExchangeHd(getTxtExchangeHeadGR());
            objRemittanceProductTO.setTelegramChrgHd(getTxtTCHeadGR());
            objRemittanceProductTO.setRevalChrgHd(getTxtRCHeadGR());
            objRemittanceProductTO.setOtherChrgHd(getTxtOCHeadGR());
            objRemittanceProductTO.setLapsedHd(getTxtLapsedHeadGR());
            //            objRemittanceProductTO.setBaseCurrency(CommonUtil.convertObjToStr(cbmProdCurrency.getKeyForSelected()));
            objRemittanceProductTO.setPayIssueBranch(CommonUtil.convertObjToStr(cbmPayableBranchGR.getKeyForSelected()));
            objRemittanceProductTO.setCashLimit(CommonUtil.convertObjToDouble(getTxtCashLimitGR()));
            objRemittanceProductTO.setMaximumAmount(CommonUtil.convertObjToDouble(getTxtMaximumAmount()));
            objRemittanceProductTO.setMinimumAmount(CommonUtil.convertObjToDouble(getTxtMinimumAmount()));
            objRemittanceProductTO.setPayHd(getTxtPayableHeadGR());
            objRemittanceProductTO.setPostageHd(getTxtPostageHeadGR());
            objRemittanceProductTO.setDuplChrgHd(getTxtDCHeadGR());
            objRemittanceProductTO.setCancellChrgHd(getTxtCCHeadGR());
            objRemittanceProductTO.setRemarks(getTxtRemarksGR());
            objRemittanceProductTO.setNumberPattern(getTxtPerfix());
            objRemittanceProductTO.setNumberPatternSuffix(getTxtSuffix());
            objRemittanceProductTO.setRtgsSuspenseAchd(getTxtRTGSSuspenseHead());
//            objRemittanceProductTO.setStatusBy(TrueTransactMain.USER_ID);
//            objRemittanceProductTO.setCreatedBy(TrueTransactMain.USER_ID);
            objRemittanceProductTO.setBehavesLike(CommonUtil.convertObjToStr(cbmBehavesLike.getKeyForSelected()));
            if(getRdoEFTProductGR_Yes()){
                objRemittanceProductTO.setEftProduct("Y");
            }else{
                objRemittanceProductTO.setEftProduct("N");
            }
            if(getRdoPrintServicesGR_Yes()){
                objRemittanceProductTO.setPrintServices("Y");
            }else{
                objRemittanceProductTO.setPrintServices("N");
            }
            if(getRdoIsLapsedGR_Yes()){
                objRemittanceProductTO.setLapseAppl("Y");
            }else{
                objRemittanceProductTO.setLapseAppl("N");
            }
//            if(getRdoSeriesGR_Yes()){
                objRemittanceProductTO.setSeriesMaintained("Y");
//            }else{
//                objRemittanceProductTO.setSeriesMaintained("N");
//            }
            
            objRemittanceProductTO.setAuthorizeStatus("");
        }catch(Exception e){
            parseException.logException(e,true);
        }
        
        int time = Integer.parseInt(getTxtLapsedPeriodGR());
        String timeUnit = getCboLapsedPeriodGR();
        if(timeUnit.equalsIgnoreCase("Years")){
            objRemittanceProductTO.setLapsePeriod(new Double(time * 365));
        }else if(timeUnit.equalsIgnoreCase("Months")){
            objRemittanceProductTO.setLapsePeriod(new Double(time * 30));
        }else{
            objRemittanceProductTO.setLapsePeriod(new Double(time * 1));
        }
        //for new procedure by kannan        
        if (isChkNewProcedure()) {
            objRemittanceProductTO.setNewProcedure("Y");
        } else {
            objRemittanceProductTO.setNewProcedure("N");
        }
        objRemittanceProductTO.setNewProcStartDt(com.see.truetransact.commonutil.DateUtil.getDateMMDDYYYY(getTdtNewProcStartDt()));
        objRemittanceProductTO.setNewProcIssueAcHd(CommonUtil.convertObjToStr(getTxtNewProcIssueAcHd()));
        if (getRtgsNeftGLType() != null && getRtgsNeftGLType().equals("N")) {
            objRemittanceProductTO.setRtgsNeftGLType("N");
            objRemittanceProductTO.setRtgsNeftProductType(getCboProdTypeCr());
            objRemittanceProductTO.setRtgsNeftProdId(getTxtRtgsNeftProductId());
            objRemittanceProductTO.setRtgsNeftActNum(getTxtRtgsNeftAcctNo());
        } else {
            objRemittanceProductTO.setRtgsNeftGLType("Y");
            objRemittanceProductTO.setRtgsNeftProductType("");
            objRemittanceProductTO.setRtgsNeftProdId("");
            objRemittanceProductTO.setRtgsNeftActNum("");
        }
        return objRemittanceProductTO;
    }
    
    /** Gets the datas from the Fields and sets to Remittance Product Branch TO */
    private ArrayList setRemittanceProductBranchTO(ArrayList remittProdBrchTO){
        int remittProdBrchTOSize = remittProdBrchTO.size();
        HashMap hashRemittProdBrchTO;
        remitProdBrchTOData =  new ArrayList();
        for(int i=0;i<remittProdBrchTOSize;i++){
            objRemittanceProductBranchTO = new RemittanceProductBranchTO();
            hashRemittProdBrchTO = new HashMap();
            hashRemittProdBrchTO = (HashMap)remittProdBrchTO.get(i);
            objRemittanceProductBranchTO.setProdId(getTxtProductIdGR());
            objRemittanceProductBranchTO.setBankCode(CommonUtil.convertObjToStr(hashRemittProdBrchTO.get("BANK_CODE")));
            objRemittanceProductBranchTO.setBranchCode(CommonUtil.convertObjToStr(hashRemittProdBrchTO.get("BRANCH_CODE")));
            objRemittanceProductBranchTO.setBranchName(CommonUtil.convertObjToStr(hashRemittProdBrchTO.get("BRANCH_NAME")));
            objRemittanceProductBranchTO.setRemittanceType(CommonUtil.convertObjToStr(hashRemittProdBrchTO.get("REMITTANCE_TYPE")));
            objRemittanceProductBranchTO.setRemittanceLimit(CommonUtil.convertObjToDouble(hashRemittProdBrchTO.get("REMITTANCE_LIMIT")));
            objRemittanceProductBranchTO.setInwardVariableNo(CommonUtil.convertObjToStr(hashRemittProdBrchTO.get("INWARD_VARIABLE_NO")));
            objRemittanceProductBranchTO.setOutwardVariableNo(CommonUtil.convertObjToStr(hashRemittProdBrchTO.get("OUTWARD_VARIABLE_NO")));
            objRemittanceProductBranchTO.setMinAmt(CommonUtil.convertObjToDouble(hashRemittProdBrchTO.get("MIN_AMT")));
            objRemittanceProductBranchTO.setMaxAmt(CommonUtil.convertObjToDouble(hashRemittProdBrchTO.get("MAX_AMT")));
            objRemittanceProductBranchTO.setCommand(CommonUtil.convertObjToStr(hashRemittProdBrchTO.get("COMMAND")));
            remitProdBrchTOData.add(objRemittanceProductBranchTO);
            hashRemittProdBrchTO = null;
            objRemittanceProductBranchTO =  null;
        }
        return remitProdBrchTOData;
    }
    
    /** Checks whether the data is already issued before deleteing Remittance Product Branch */
    public boolean remittanceProductBranchDelete(){
        ArrayList remitProdBrchTODelete = new ArrayList();
        boolean dataExist = false;
        HashMap whereMap = new HashMap();
        HashMap resultMap = new HashMap();
        if(getActionType() == ClientConstants.ACTIONTYPE_DELETE ){
            int tabRow = tblRemittanceProductBranch.getRowCount();
            if(tabRow > 0){
                for( int i=0;i<tabRow ;i++){
                    remitProdBrchTODelete.add(CommonUtil.convertObjToStr(((HashMap)arrayListRemitProdBrchTabUpdate.get(i)).get("BRANCH_CODE")));
                }
            }
            whereMap.put("PROD_ID",  getTxtProductIdGR());
            whereMap.put("DRAWEE_BRANCH_CODE",remitProdBrchTODelete);
        }else {
            remitProdBrchTODelete.add(getTxtBranchCodeBR());
            whereMap.put("PROD_ID",  getTxtProductIdGR());
            whereMap.put("DRAWEE_BRANCH_CODE",remitProdBrchTODelete);
        }
        final List resultList = ClientUtil.executeQuery("deleteremittissue", whereMap);
        if(!resultList.isEmpty()){
            dataExist = true;
        }else{
            setResult(getActionType());
            dataExist = false;
        }
        remitProdBrchTODelete = null;
        return dataExist;
    }
    
    /** Checks whether the data is already issued before deleteing Remittance Product Charges */
    public boolean remittanceProductChargeDelete(){
        ArrayList remitProdChrgTODelete = new ArrayList();
        boolean dataExist = false;
        HashMap whereMap = new HashMap();
        HashMap resultMap = new HashMap();
        if(getActionType() == ClientConstants.ACTIONTYPE_DELETE ){
            int tabRow = tblRemittanceProductCharge.getRowCount();
            if(tabRow > 0){
                for( int i=0;i<tabRow ;i++){
                    remitProdChrgTODelete.add(tblRemittanceProductCharge.getValueAt(i, 1));
                }
            }
            whereMap.put("PROD_ID",  getTxtProductIdGR());
            whereMap.put("CATEGORY",remitProdChrgTODelete);
        }else {
            remitProdChrgTODelete.add(getCbmCategory().getKeyForSelected());
            whereMap.put("PROD_ID",  getTxtProductIdGR());
            whereMap.put("CATEGORY",remitProdChrgTODelete);
        }
        final List resultList = ClientUtil.executeQuery("deleteRemitProdChrg", whereMap);
        if(!resultList.isEmpty()){
            dataExist = true;
        }else{
            setResult(getActionType());
            dataExist = false;
        }
        remitProdChrgTODelete = null;
        return dataExist;
    }
    
    public boolean populateData(HashMap whereMap) {
        boolean aliasBranchTableFlag = false;
        HashMap mapData = new HashMap() ;
        try {
            mapData = proxy.executeQuery(whereMap, operationMap);
            aliasBranchTableFlag = populateOB(mapData);
        } catch( Exception e ) {
            parseException.logException(e,true);
        }
        return aliasBranchTableFlag;
    }
    
    private boolean populateOB(HashMap mapData) {
        boolean aliasBranchTableFlag = false;
        ArrayList remitProdBrchTOArrayList  = new ArrayList();
        RemittanceProductTO remittanceProductTO;
        remittanceProductTO = (RemittanceProductTO) ((List) mapData.get("RemittanceProductTO")).get(0);
        getRemittanceProductTO(remittanceProductTO);
        remitProdBrchTOArrayList  = (ArrayList) mapData.get("RemittanceProductBranchTO");
        getRemittanceProductBranchTO(remitProdBrchTOArrayList);
        List chargeList  = (List) mapData.get("RemittanceProductChargeTO");
        ArrayList descLst=(ArrayList)mapData.get("RemittanceDesc");
        //System.out.println("descList###"+descLst.size());
        if (descLst != null && descLst.size() >0) {
            setDescList(descLst);
        }
        if (remittanceProductTO.getPayIssueBranch().equals("ISSU_BRANCH")) {
            getRemitProdChargeTO(chargeList,false);
            aliasBranchTableFlag = false;
        } else {
//        if (getRdoSeriesGR_Yes() == true) {
            getRemitProdChargeTO(chargeList,true);
            aliasBranchTableFlag = true;
//        } else if (getRdoSeriesGR_No() == true) {
//            getRemitProdChargeTO(chargeList,false);
//            aliasBranchTableFlag = false;
//        }
        }
        notifyObservers();
        return aliasBranchTableFlag;
    }
    
    
    /** Gets datas from Remittance and sets to Fields */
    public void getRemittanceProductTO(RemittanceProductTO objRemittanceProductTO){
        int value = CommonUtil.convertObjToInt(objRemittanceProductTO.getLapsePeriod());
        productID = objRemittanceProductTO.getProdId();
        productDesc = objRemittanceProductTO.getProdDesc();
        setTxtProductIdGR(objRemittanceProductTO.getProdId());
        setTxtProductDescGR(objRemittanceProductTO.getProdDesc());
        setTxtIssueHeadGR(objRemittanceProductTO.getIssueHd());
        setTxtExchangeHeadGR(objRemittanceProductTO.getExchangeHd());
        setTxtTCHeadGR(objRemittanceProductTO.getTelegramChrgHd());
        setTxtRCHeadGR(objRemittanceProductTO.getRevalChrgHd());
        setTxtOCHeadGR(objRemittanceProductTO.getOtherChrgHd());
        setTxtLapsedHeadGR(objRemittanceProductTO.getLapsedHd());
        //        setCboProdCurrency(CommonUtil.convertObjToStr(getCbmProdCurrency().getDataForKey(objRemittanceProductTO.getBaseCurrency())));
        setCboBehavesLike(CommonUtil.convertObjToStr(getCbmBehavesLike().getDataForKey(objRemittanceProductTO.getBehavesLike())));
        setTxtCashLimitGR(CommonUtil.convertObjToStr(objRemittanceProductTO.getCashLimit()));
        setTxtMaximumAmount(CommonUtil.convertObjToStr(objRemittanceProductTO.getMaximumAmount()));
        setTxtMinimumAmount(CommonUtil.convertObjToStr(objRemittanceProductTO.getMinimumAmount()));
        setTxtPayableHeadGR(objRemittanceProductTO.getPayHd());
        setTxtPostageHeadGR(objRemittanceProductTO.getPostageHd());
        setTxtDCHeadGR(objRemittanceProductTO.getDuplChrgHd());
        setTxtCCHeadGR(objRemittanceProductTO.getCancellChrgHd());
        setTxtRTGSSuspenseHead(objRemittanceProductTO.getRtgsSuspenseAchd());
        setTxtRemarksGR(objRemittanceProductTO.getRemarks());
        setTxtPerfix(objRemittanceProductTO.getNumberPattern());
        setTxtSuffix(objRemittanceProductTO.getNumberPatternSuffix());
        setStatusBy(objRemittanceProductTO.getStatusBy());
        setAuthorizeStatus(objRemittanceProductTO.getAuthorizeStatus());
        setCboPayableBranchGR(CommonUtil.convertObjToStr(getCbmPayableBranchGR().getDataForKey(objRemittanceProductTO.getPayIssueBranch())));
        if((value/365 > 0 ) && (value%365 == 0)){
            setTxtLapsedPeriodGR(new Integer(value/365).toString());
            setCboLapsedPeriodGR("Years");
        }else if((value/30 > 0 ) && (value%30 == 0)){
            setTxtLapsedPeriodGR(new Integer(value/30).toString());
            setCboLapsedPeriodGR("Months");
        }else{
            setTxtLapsedPeriodGR(new Integer(value).toString());
            setCboLapsedPeriodGR("Days");
        }
        if(objRemittanceProductTO.getSeriesMaintained().equals("Y")){
            setRdoSeriesGR_Yes(true);
        }else{
            setRdoSeriesGR_No(true);
        }
        if(objRemittanceProductTO.getEftProduct().equals("Y")){
            setRdoEFTProductGR_Yes(true);
        }else{
            setRdoEFTProductGR_No(true);
        }
        if(objRemittanceProductTO.getPrintServices().equals("Y")){
            setRdoPrintServicesGR_Yes(true);
        }else{
            setRdoPrintServicesGR_No(true);
        }
        if(objRemittanceProductTO.getLapseAppl().equals("Y")){
            setRdoIsLapsedGR_Yes(true);
        }else{
            setRdoIsLapsedGR_No(true);
        }
        //for new procedure by kannan
        if(CommonUtil.convertObjToStr(objRemittanceProductTO.getNewProcedure()).equals("Y")){
            setChkNewProcedure(true);
        }else{
            setChkNewProcedure(false);
        }
        setTdtNewProcStartDt(CommonUtil.convertObjToStr(objRemittanceProductTO.getNewProcStartDt()));
        setTxtNewProcIssueAcHd(CommonUtil.convertObjToStr(objRemittanceProductTO.getNewProcIssueAcHd()));        
        if (objRemittanceProductTO.getRtgsNeftGLType() != null && objRemittanceProductTO.getRtgsNeftGLType().equals("N")) {
            getCbmProdTypeCr().setKeyForSelected(CommonUtil.convertObjToStr(objRemittanceProductTO.getRtgsNeftProductType()));
            setTxtRtgsNeftProductId(objRemittanceProductTO.getRtgsNeftProdId());
            setTxtRtgsNeftAcctNo(objRemittanceProductTO.getRtgsNeftActNum());
            setRtgsNeftGLType("N");
        } else {
            setCboProdTypeCr("");
            setTxtRtgsNeftProductId("");
            setTxtRtgsNeftAcctNo("");
            setRtgsNeftGLType("Y");
        }
    }
    
    /** Gets datas from Remittance Product Branch TO and sets to ArrayList of CTable */
    public void getRemittanceProductBranchTO(ArrayList remitProdBrchTOArrayList ){
        int remitProdBrchTOArrayListSize = remitProdBrchTOArrayList.size();
        HashMap hashRemitProdBrchTOArrayList;
        arrayListRemitProdBrchTabUpdate = new ArrayList();
        for(int i=0;i<remitProdBrchTOArrayListSize;i++){
            tableHashData = new HashMap();
            hashRemitProdBrchTOArrayList = new HashMap();
            hashRemitProdBrchTOArrayList = (HashMap)remitProdBrchTOArrayList.get(i);
//            tableHashData.put("BANK_NAME", hashRemitProdBrchTOArrayList.get("BANK_NAME"));
            tableHashData.put("BANK_CODE", hashRemitProdBrchTOArrayList.get("BANK_CODE"));
            tableHashData.put("BRANCH_CODE", hashRemitProdBrchTOArrayList.get("BRANCH_CODE"));
            tableHashData.put("BRANCH_NAME", hashRemitProdBrchTOArrayList.get("BRANCH_NAME"));
            tableHashData.put("REMITTANCE_TYPE", hashRemitProdBrchTOArrayList.get("REMITTANCE_TYPE"));
            tableHashData.put("REMITTANCE_LIMIT", hashRemitProdBrchTOArrayList.get("REMITTANCE_LIMIT"));
            tableHashData.put("INWARD_VARIABLE_NO", hashRemitProdBrchTOArrayList.get("INWARD_VARIABLE_NO"));
            tableHashData.put("OUTWARD_VARIABLE_NO", hashRemitProdBrchTOArrayList.get("OUTWARD_VARIABLE_NO"));
            tableHashData.put("MIN_AMT", hashRemitProdBrchTOArrayList.get("MIN_AMT"));
            tableHashData.put("MAX_AMT", hashRemitProdBrchTOArrayList.get("MAX_AMT"));
            arrayListRemitProdBrchTabUpdate.add(tableHashData);
            
            tableHashData = null;
            
            ArrayList remitProdBrchTORow = new ArrayList();
//            remitProdBrchTORow.add(hashRemitProdBrchTOArrayList.get("BANK_NAME"));
            remitProdBrchTORow.add(hashRemitProdBrchTOArrayList.get("BANK_CODE"));
            remitProdBrchTORow.add(hashRemitProdBrchTOArrayList.get("BRANCH_CODE"));
            remitProdBrchTORow.add(hashRemitProdBrchTOArrayList.get("BRANCH_NAME"));
            remitProdBrchTORow.add(hashRemitProdBrchTOArrayList.get("INWARD_VARIABLE_NO"));
            remitProdBrchTORow.add(hashRemitProdBrchTOArrayList.get("OUTWARD_VARIABLE_NO"));
            remitProdBrchTORow.add(hashRemitProdBrchTOArrayList.get("MIN_AMT"));
            remitProdBrchTORow.add(hashRemitProdBrchTOArrayList.get("MAX_AMT"));
            tblRemittanceProductBranch.insertRow(0,remitProdBrchTORow);
            this.tblAliasRemittanceProductBranch = this.tblRemittanceProductBranch;
            hashRemitProdBrchTOArrayList = null;
            remitProdBrchTORow = null;
        }
    }
    
    
    // To get the Date from the Table into the Appropriate Fields...
    public void populateBrchRemittNumberTab(int row){
        int arrayListRemitProdBrchTabUpdateSize = arrayListRemitProdBrchTabUpdate.size();
        HashMap hashArrayListRemitProdBrchTabUpdate;
        remitProdBrnchTabSelected = true ;
        ArrayList data = (ArrayList)tblRemittanceProductBranch.getDataArrayList().get(row);
        for(int i=0;i<arrayListRemitProdBrchTabUpdateSize;i++){
            hashArrayListRemitProdBrchTabUpdate = new HashMap();
            hashArrayListRemitProdBrchTabUpdate = (HashMap)arrayListRemitProdBrchTabUpdate.get(i);
//            if((CommonUtil.convertObjToStr(hashArrayListRemitProdBrchTabUpdate.get("BANK_NAME")).equals(CommonUtil.convertObjToStr(data.get(0)))) && (CommonUtil.convertObjToStr(hashArrayListRemitProdBrchTabUpdate.get("BRANCH_NAME")).equals(CommonUtil.convertObjToStr(data.get(1))))){
//                setBankName(CommonUtil.convertObjToStr(hashArrayListRemitProdBrchTabUpdate.get("BANK_NAME")));
            if((CommonUtil.convertObjToStr(hashArrayListRemitProdBrchTabUpdate.get("BANK_CODE")).equals(CommonUtil.convertObjToStr(data.get(0)))) && (CommonUtil.convertObjToStr(hashArrayListRemitProdBrchTabUpdate.get("BRANCH_CODE")).equals(CommonUtil.convertObjToStr(data.get(1))))){
                
                setTxtBankCode(CommonUtil.convertObjToStr(hashArrayListRemitProdBrchTabUpdate.get("BANK_CODE")));
                setTxtBranchCodeBR(CommonUtil.convertObjToStr(hashArrayListRemitProdBrchTabUpdate.get("BRANCH_CODE")));
                setTxtBranchName(CommonUtil.convertObjToStr(hashArrayListRemitProdBrchTabUpdate.get("BRANCH_NAME")));
                setCboRttTypeBR(CommonUtil.convertObjToStr(hashArrayListRemitProdBrchTabUpdate.get("REMITTANCE_TYPE")));
                setTxtRttLimitBR(CommonUtil.convertObjToStr(hashArrayListRemitProdBrchTabUpdate.get("REMITTANCE_LIMIT")));
                setTxtIVNoRR(CommonUtil.convertObjToStr(hashArrayListRemitProdBrchTabUpdate.get("INWARD_VARIABLE_NO")));
                setTxtOVNoRR(CommonUtil.convertObjToStr(hashArrayListRemitProdBrchTabUpdate.get("OUTWARD_VARIABLE_NO")));
                setTxtMinAmtRR(CommonUtil.convertObjToStr(hashArrayListRemitProdBrchTabUpdate.get("MIN_AMT")));
                setTxtMaxAmtRR(CommonUtil.convertObjToStr(hashArrayListRemitProdBrchTabUpdate.get("MAX_AMT")));
                hashArrayListRemitProdBrchTabUpdate = null;
                break;
            }
        }
        setChanged();
        notifyObservers();
    }
    
    // Delete from CTable ....
    public void deleteRemittanceProductBranchTab(){
        try{
            final ArrayList data = tblRemittanceProductBranch.getDataArrayList();
            final int dataSize = data.size();
            ArrayList arrayListData;
            for (int i=0;i<dataSize;i++){
                arrayListData = new ArrayList();
                arrayListData = (ArrayList)data.get(i);
                if ((CommonUtil.convertObjToStr(arrayListData.get(0))).equals(CommonUtil.convertObjToStr(getTxtBankCode())) && (CommonUtil.convertObjToStr(arrayListData.get(1))).equals(CommonUtil.convertObjToStr(getTxtBranchCodeBR()))){
                    deleteRow();
                    tblRemittanceProductBranch.removeRow(i);
                    arrayListData = null;
                    break;
                }
                arrayListData = null;
            }
            this.tblAliasRemittanceProductBranch = this.tblRemittanceProductBranch;
            setChanged();
            notifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /** Method to delete the row from the ArrayList */
    private void deleteRow(){
        ArrayList deleteArrayList = new ArrayList();
        int arrayListRemitProdBrchTabUpdateSize = arrayListRemitProdBrchTabUpdate.size();
        HashMap hashArrayListRemitProdBrchTabUpdate;
        
        // Adds the element that has to be updated to the remittProductBrchDeleteRow (DELETE DATA)
//        deleteArrayList.add(getBankName());
        deleteArrayList.add(getTxtBankCode());
        deleteArrayList.add(getTxtBranchCodeBR());
        deleteArrayList.add(getTxtBranchName());
        deleteArrayList.add(getCboRttTypeBR());
        deleteArrayList.add(getTxtRttLimitBR());
        deleteArrayList.add(getTxtIVNoRR());
        deleteArrayList.add(getTxtIVNoRR());
        deleteArrayList.add(getTxtMinAmtRR());
        deleteArrayList.add(getTxtMaxAmtRR());
        remittProductBrchDeleteRow.add(deleteArrayList);
        deleteArrayList = null;    
        for (int j = 0;j<arrayListRemitProdBrchTabUpdateSize;j++){
            hashArrayListRemitProdBrchTabUpdate = new HashMap();
            hashArrayListRemitProdBrchTabUpdate = (HashMap)arrayListRemitProdBrchTabUpdate.get(j);
            if(CommonUtil.convertObjToStr(hashArrayListRemitProdBrchTabUpdate.get("BANK_CODE")).equals(CommonUtil.convertObjToStr(getTxtBankCode())) && (CommonUtil.convertObjToStr(hashArrayListRemitProdBrchTabUpdate.get("BRANCH_CODE")).equals(CommonUtil.convertObjToStr(getTxtBranchCodeBR())))){
                // Deletes the row element from the ArrayList
                arrayListRemitProdBrchTabUpdate.remove(j);
                break;
            }
        }
    }
    
    
    private void initialise(){
        remittProductBrchInsertData = new ArrayList();
        remittProductBrchUpdateData = new ArrayList();
        remittProductBrchDeleteData = new ArrayList();
        remitProdBrchTO = new ArrayList();
    }
    private void deinitialise(){
        remittProductBrchInsertData = null;
        remittProductBrchUpdateData =  null;
        remittProductBrchDeleteData =  null;
        remitProdBrchTO = null;
        hashChargesTableValues = null;
        
        chargesRowData = null;
        hashCharges = null;
        deletedCharges = null;
        deletedChargesCount = 1;
    }
    
    /** This deletes existing row from the Table */
    public void removeRemittanceProductBranchRow(){
        int row = tblRemittanceProductBranch.getRowCount();
        for(int i=0;i<row;i++) {
            tblRemittanceProductBranch.removeRow(0);
        }
        tblRemittanceProductBranch = new EnhancedTableModel(null, remittanceProductBranchTabTitle);
        setTblRemittanceProductBranch(tblRemittanceProductBranch);
        setTblAliasRemittanceProductBranch();
    }
    
    /** This deletes existing row from the Table */
    public void removeRemittanceProductChargeRow(){
        int row = tblRemittanceProductCharge.getRowCount();
        for(int i=0;i<row;i++) {
            tblRemittanceProductCharge.removeRow(0);
        }
        tblRemittanceProductCharge = new EnhancedTableModel(null, remittanceProductChargeTabTitle);
        setTblRemittanceProductCharge(tblRemittanceProductCharge);
    }
    
    public void clearData(){
        remittProductBrchNewData.clear();
        remitProductBrchExistingData.clear();
        remittProductBrchDeleteRow.clear();
    }
    
    /** Resets the General Remittance Fields to Null  */
    public void resetOBFields(){
        setTxtProductIdGR("");
        setTxtProductDescGR("");
        setTxtIssueHeadGR("");
        setTxtExchangeHeadGR("");
        setTxtTCHeadGR("");
        setTxtRCHeadGR("");
        setTxtOCHeadGR("");
        setTxtLapsedHeadGR("");
        setCboPayableBranchGR("");
        setTxtCashLimitGR("");
        setTxtMaximumAmount("");
        setTxtMinimumAmount("");
        setTxtPayableHeadGR("");
        setTxtPostageHeadGR("");
        setTxtDCHeadGR("");
        setTxtCCHeadGR("");
        setTxtRTGSSuspenseHead("");
        setTxtLapsedPeriodGR("");
        setCboLapsedPeriodGR("");
        setTxtRemarksGR("");
        //        setCboProdCurrency("");
        setTxtPerfix("");
        setTxtSuffix("");
        setLblRemitProdIDBranch("");
        setLblRemitProdIDCharge("");
        setLblRemitProdDescBranch("(");
        setLblRemitProdDescCharge("");
        setLblChargesBankCode("");
        setLblChargesBranchCode("");
        setCboBehavesLike("");
        setTxtMaxAmt("");
        setTxtMinAmt("");
        setTxtNewProcIssueAcHd("");
        setTdtNewProcStartDt("");
        setChkNewProcedure(false);
        deinitialise();
        setDescList(null);
        resetRadioButton();
        setTxtRtgsNeftAcctNo("");
        setTxtRtgsNeftProductId("");
    }
    
    /** Resets the Remittance Product Branch Fields to Null  */
    public void resetRemitProdBrchFields(){
        setTxtBankCode("");
        setTxtBranchName("");
        setTxtBranchCodeBR("");
        setCboRttTypeBR("");
        setTxtRttLimitBR("");
        setTxtIVNoRR("");
        setTxtOVNoRR("");
        setTxtMinAmtRR("");
        setTxtMaxAmtRR("");
        notifyObservers();
    }
    
    /** Resets the Remittance Product Charges Fields to Null  */
    public void resetRemitProdChrgFields(){
        setCboChargeType("");
        setCboCategory("");
        setTxtAmtRangeFrom("");
        setTxtAmtRangeTo("");
        setTxtCharges("");
        setTxtPercent("");
        setTxtServiceTax("");
        setCboRateType("");
        setTxtRateVal("");
        setTxtForEvery("");
        setChargeStatus("");
        setTxtMinAmt("");
        setTxtMaxAmt("");
        //setTxtNewProcIssueAcHd("");
        //setTdtNewProcStartDt("");
        //setChkNewProcedure(false);
        notifyObservers();
    }
    /** Resets the Remittance General Radio Buttons to Null  */
    public void resetRadioButton(){
        setRdoIsLapsedGR_Yes(false);
        setRdoIsLapsedGR_No(false);
        setRdoEFTProductGR_Yes(false);
        setRdoEFTProductGR_No(false);
        setRdoSeriesGR_Yes(false);
        setRdoSeriesGR_No(false);
        resetPrintRadioButton();
    }
    
    
    /** Resets Print Radio Button to Null  */
    public void resetPrintRadioButton(){
        setRdoPrintServicesGR_Yes(false);
        setRdoPrintServicesGR_No(false);
        notifyObservers();
    }
    /***************Modifications for Remit Product Charges tab Starts*************/
    public void setChargesBankBranchCodes() {
        setLblChargesBankCode(getTxtBankCode());
        setLblChargesBranchCode(getTxtBranchCodeBR());

        notifyObservers();        
    }
    // Setter method for txtPercent
    void setTxtPercent(String txtPercent){
        this.txtPercent = txtPercent;
        setChanged();
    }
    // Getter method for txtPercent
    String getTxtPercent(){
        return this.txtPercent;
    }
    // Setter method for txtForEvery
    void setTxtForEvery(String txtForEvery){
        this.txtForEvery = txtForEvery;
        setChanged();
    }
    // Getter method for txtForEvery
    String getTxtForEvery(){
        return this.txtForEvery;
    }
    // Setter method for txtRateVal
    void setTxtRateVal(String txtRateVal){
        this.txtRateVal = txtRateVal;
        setChanged();
    }
    // Getter method for txtRateVal
    String getTxtRateVal(){
        return this.txtRateVal;
    }
    /**
     * Insert TOs remit prod charges values into hashCharges when retrived from database
     */
    private void getRemitProdChargeTO(List list,boolean flag) {
        if (list != null) {
            if (hashCharges == null)
                hashCharges = new LinkedHashMap();
            
            for (int i=0,j=list.size();i<j;i++) {
                objRemittanceProductChargeTO = (RemittanceProductChargeTO)list.get(i);              
                
                if ((LinkedHashMap)hashCharges.get(returnKeyFromRemitProdChargeTO(objRemittanceProductChargeTO)) == null) {
                    LinkedHashMap hashSubCharges = new LinkedHashMap();
                    hashSubCharges.put(returnSubKeyFromRemitProdChargeTO(objRemittanceProductChargeTO), objRemittanceProductChargeTO);
                    hashCharges.put(returnKeyFromRemitProdChargeTO(objRemittanceProductChargeTO),hashSubCharges);
                    hashSubCharges = null;
                } else {
                    LinkedHashMap hashSubCharges = (LinkedHashMap)hashCharges.get(returnKeyFromRemitProdChargeTO(objRemittanceProductChargeTO));
                    hashSubCharges.put(returnSubKeyFromRemitProdChargeTO(objRemittanceProductChargeTO), objRemittanceProductChargeTO);
                    hashCharges.put(returnKeyFromRemitProdChargeTO(objRemittanceProductChargeTO),hashSubCharges);
                    hashSubCharges = null;
                }
                if (flag == false) {
                    if (chargesRowData == null)
                        chargesRowData = new ArrayList();
                    chargesRowData.add(setChargeColumnValues(objRemittanceProductChargeTO));
                    tblRemittanceProductCharge.setDataArrayList(chargesRowData,remittanceProductChargeTabTitle);
                }
                objRemittanceProductChargeTO = null;
            }
        }
    }
    
    
    /**
     * To set the data in Remittance Product Charges TO
     */
    public RemittanceProductChargeTO setRemittanceProductChargesTO() {
            final RemittanceProductChargeTO objRemittanceProductChargeTO = new RemittanceProductChargeTO();        
        try{
            objRemittanceProductChargeTO.setProdId(getTxtProductIdGR());
            objRemittanceProductChargeTO.setBankCode(CommonUtil.convertObjToStr(getLblChargesBankCode()));
            objRemittanceProductChargeTO.setBranchCode(CommonUtil.convertObjToStr(getLblChargesBranchCode()));
            objRemittanceProductChargeTO.setChargeType(CommonUtil.convertObjToStr(cbmChargeType.getKeyForSelected()));
            objRemittanceProductChargeTO.setCategory(CommonUtil.convertObjToStr(cbmCategory.getKeyForSelected()));
            objRemittanceProductChargeTO.setAmtRangeFrom(CommonUtil.convertObjToDouble(getTxtAmtRangeFrom()));
            objRemittanceProductChargeTO.setAmtRangeTo(CommonUtil.convertObjToDouble(getTxtAmtRangeTo()));
            objRemittanceProductChargeTO.setCharge(CommonUtil.convertObjToDouble(getTxtCharges()));
            objRemittanceProductChargeTO.setStatus(CommonUtil.convertObjToStr(getChargeStatus()));
            
            objRemittanceProductChargeTO.setForEveryAmt(CommonUtil.convertObjToDouble(getTxtForEvery()));
            objRemittanceProductChargeTO.setForEveryRate(CommonUtil.convertObjToDouble(getTxtRateVal()));
            objRemittanceProductChargeTO.setPercentage(CommonUtil.convertObjToDouble(getTxtPercent()));
            objRemittanceProductChargeTO.setServiceTax(CommonUtil.convertObjToDouble(getTxtServiceTax()));
            objRemittanceProductChargeTO.setMinAmt(CommonUtil.convertObjToDouble(getTxtMinAmt()));
            objRemittanceProductChargeTO.setMaxAmt(CommonUtil.convertObjToDouble(getTxtMaxAmt()));
            if(getCbmRateType().getKeyForSelected()!=null)
                objRemittanceProductChargeTO.setForEveryType((String)getCbmRateType().getKeyForSelected());
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return objRemittanceProductChargeTO;
    }
    // Setter method for cboRateType
    void setCboRateType(String cboRateType){
        this.cboRateType = cboRateType;
        setChanged();
    }
    // Getter method for cboRateType
    String getCboRateType(){
        return this.cboRateType;
    }
    /**
     * Getter for property cbmRateType.
     * @return Value of property cbmRateType.
     */
    public ComboBoxModel getCbmRateType() {
        return cbmRateType;
    }
    
    /**
     * Setter for property cbmRateType.
     * @param cbmRateType New value of property cbmRateType.
     */
    public void setCbmRateType(ComboBoxModel cbmRateType) {
        this.cbmRateType = cbmRateType;
    }
    /**
     * Set Remittance Product Charges fields in the OB
     */
    private void setRemittanceProductChargesOB(RemittanceProductChargeTO objRemittanceProductChargeTO) throws Exception {
        setLblRemitProdIDCharge(CommonUtil.convertObjToStr(objRemittanceProductChargeTO.getProdId()));
        setLblChargesBankCode(CommonUtil.convertObjToStr(objRemittanceProductChargeTO.getBankCode()));
        setLblChargesBranchCode(CommonUtil.convertObjToStr(objRemittanceProductChargeTO.getBranchCode()));
        setChargeStatus(CommonUtil.convertObjToStr(objRemittanceProductChargeTO.getStatus()));
        setTxtCharges(CommonUtil.convertObjToStr(objRemittanceProductChargeTO.getCharge()));
        setCboChargeType(CommonUtil.convertObjToStr(getCbmChargeType().getDataForKey(CommonUtil.convertObjToStr(objRemittanceProductChargeTO.getChargeType()))));
        setCboCategory(CommonUtil.convertObjToStr(getCbmCategory().getDataForKey(CommonUtil.convertObjToStr(objRemittanceProductChargeTO.getCategory()))));
        setTxtAmtRangeFrom(CommonUtil.convertObjToStr(objRemittanceProductChargeTO.getAmtRangeFrom()));
        setTxtAmtRangeTo(CommonUtil.convertObjToStr(objRemittanceProductChargeTO.getAmtRangeTo()));
        setCboRateType(CommonUtil.convertObjToStr(getCbmRateType().getDataForKey(CommonUtil.convertObjToStr(objRemittanceProductChargeTO.getForEveryType()))));
        
        setTxtPercent(CommonUtil.convertObjToStr(objRemittanceProductChargeTO.getPercentage()));
        setTxtServiceTax(CommonUtil.convertObjToStr(objRemittanceProductChargeTO.getServiceTax()));
        setTxtForEvery(CommonUtil.convertObjToStr(objRemittanceProductChargeTO.getForEveryAmt()));
        setTxtRateVal(CommonUtil.convertObjToStr(objRemittanceProductChargeTO.getForEveryRate()));
        setTxtMinAmt(CommonUtil.convertObjToStr(objRemittanceProductChargeTO.getMinAmt()));
        setTxtMaxAmt(CommonUtil.convertObjToStr(objRemittanceProductChargeTO.getMaxAmt()));
        notifyObservers();
    }
    /**
     * Action to be performed when Save Button in Remittance Product Charge Screen is pressed
     */
    public int saveRemittanceProductCharges(boolean isTableRowClicked,int rowIndex) {
        int mode = -1;
        try{
            objRemittanceProductChargeTO = setRemittanceProductChargesTO();
//            System.out.println("objRemittanceProductChargeTO:"+objRemittanceProductChargeTO);
            
            String chargeKey = returnChargeKey();
            String subChargeKey = returnSubChargeKey();
            
            boolean exist = false;
            
            if (hashCharges == null)
                hashCharges = new LinkedHashMap();
            
            if (chargesRowData == null)
                chargesRowData =  new ArrayList();     
            
            if (isTableRowClicked) {
//                chargesRowData.set(rowIndex, setChargeColumnValues(objRemittanceProductChargeTO));
                if ((LinkedHashMap)hashCharges.get(chargeKey) == null) {
                    LinkedHashMap hashSubCharges = new LinkedHashMap();
                    hashSubCharges.put(subChargeKey, objRemittanceProductChargeTO);
                    hashCharges.put(chargeKey, hashSubCharges);
                    hashSubCharges = null;
                    setRemittanceProductChargesOB(objRemittanceProductChargeTO);
                } else {
                    if (hashCharges != null && hashCharges.size() > 0) {
                        if (subChargeKey != null)
                           exist = duplicationCheckForCharges(chargeKey,subChargeKey,objRemittanceProductChargeTO,rowIndex);
                    }
                    if (exist) {
                        COptionPane.showMessageDialog(null, 
                        objRemittanceProductRB.getString("existanceWarning"));
                    }
                    else{
                        chargesRowData.set(rowIndex, setChargeColumnValues(objRemittanceProductChargeTO));
                        LinkedHashMap hashSubCharges = (LinkedHashMap) hashCharges.get(chargeKey);
                        hashSubCharges.put(subChargeKey, objRemittanceProductChargeTO);
                        hashCharges.put(chargeKey, hashSubCharges);
                        hashSubCharges = null;
                        tblRemittanceProductCharge.setDataArrayList(chargesRowData,remittanceProductChargeTabTitle);
                        setRemittanceProductChargesOB(objRemittanceProductChargeTO);
                    }
                }
                
//                setRemittanceProductChargesOB(objRemittanceProductChargeTO);
                resetRemitProdChrgFields();
            } else {
                if (hashCharges != null && hashCharges.size() > 0) {
                    if (subChargeKey != null)
                        exist = duplicationCheckForCharges(chargeKey,subChargeKey,objRemittanceProductChargeTO,-1);
                }
                if (exist) {
                    COptionPane.showMessageDialog(null, 
                    objRemittanceProductRB.getString("existanceWarning"));
                    resetRemitProdChrgFields();
//                    if (mode == 0){
//                        // option selected is Yes
//                        int index = returnMatchedRow(subChargeKey);
//                        if (index != -1) {
//                            chargesRowData.set(index, setChargeColumnValues(objRemittanceProductChargeTO));
//                        }
//                        
//                        
//                        if ((LinkedHashMap)hashCharges.get(chargeKey) == null) {
//                            LinkedHashMap hashSubCharges = new LinkedHashMap();
//                            hashSubCharges.put(subChargeKey, objRemittanceProductChargeTO);
//                            hashCharges.put(chargeKey, hashSubCharges);
//                            hashSubCharges = null;
//                        } else {
//                            LinkedHashMap hashSubCharges = (LinkedHashMap) hashCharges.get(chargeKey);
//                            hashSubCharges.put(subChargeKey, objRemittanceProductChargeTO);
//                            hashCharges.put(chargeKey, hashSubCharges);
//                            hashSubCharges = null;
//                        }
//                        resetRemitProdChrgFields();
//                    } else if( mode == 1) {
//                        // option selected is No
//                        resetRemitProdChrgFields();
//                    }
                } 
                else {
                    chargesRowData.add(setChargeColumnValues(objRemittanceProductChargeTO));                    
                    if ((LinkedHashMap)hashCharges.get(chargeKey) == null) {
                        LinkedHashMap hashSubCharges = new LinkedHashMap();
                        hashSubCharges.put(subChargeKey, objRemittanceProductChargeTO);
                        hashCharges.put(chargeKey, hashSubCharges);
                        hashSubCharges = null;
                    } else {
                        LinkedHashMap hashSubCharges = (LinkedHashMap) hashCharges.get(chargeKey);
                        hashSubCharges.put(subChargeKey, objRemittanceProductChargeTO);
                        hashCharges.put(chargeKey, hashSubCharges);
                        hashSubCharges = null;
                    }
                    resetRemitProdChrgFields();
                    tblRemittanceProductCharge.setDataArrayList(chargesRowData,remittanceProductChargeTabTitle);
                }
                
            }
            chargeKey = null;
            subChargeKey = null;
            objRemittanceProductChargeTO = null;
//            tblRemittanceProductCharge.setDataArrayList(chargesRowData,remittanceProductChargeTabTitle);
            notifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return mode;
    }
    private int returnMatchedRow(String subChargeKey) throws Exception {
        int returnRow = -1;
        ArrayList tableValues = (ArrayList)tblRemittanceProductCharge.getDataArrayList();
        for (int i=0,j=tableValues.size();i<j;i++) {
            ArrayList rowValues = (ArrayList)tableValues.get(i);
            setCboChargeType((String)rowValues.get(0));
            setCboCategory((String)rowValues.get(1));
            StringBuffer strBuf = new StringBuffer();
            strBuf.append(CommonUtil.convertObjToStr(cbmChargeType.getKeyForSelected()));
            strBuf.append(CommonUtil.convertObjToStr(cbmCategory.getKeyForSelected()));
            strBuf.append(CommonUtil.convertObjToStr(getTxtAmtRangeFrom()));
            
            if (CommonUtil.convertObjToStr(strBuf).equals(subChargeKey)) {
                strBuf = null;
                returnRow = i;
                break;
            }
            strBuf = null;
        }
        return returnRow;
    }
    /**
     * Action Performed when Delete is pressed in OtherBankBranch
     */
    public void deleteRemittanceProductCharges(int index) {
        
        try {
            if (hashCharges != null) {
                LinkedHashMap hashSubCharges = (LinkedHashMap)hashCharges.get(returnChargeKey());
                
                objRemittanceProductChargeTO = (RemittanceProductChargeTO) hashSubCharges.remove(returnSubChargeKey());
                
                if( ( objRemittanceProductChargeTO.getStatus().length()>0 ) && ( objRemittanceProductChargeTO.getStatus() != null ) && !(objRemittanceProductChargeTO.getStatus().equals(""))) {
                    if (deletedCharges == null)
                        deletedCharges = new LinkedHashMap();
                    deletedCharges.put(String.valueOf(deletedChargesCount++), objRemittanceProductChargeTO);
                }
                
                hashCharges.put(returnChargeKey(), hashSubCharges);
                hashSubCharges = null;
                objRemittanceProductChargeTO = null;
                chargesRowData.remove(index);
                tblRemittanceProductCharge.setDataArrayList(chargesRowData,remittanceProductChargeTabTitle);
            }
        } catch (Exception  e){
            parseException.logException(e,true);
        }
    }
    /**
     * populate Remit Prod Charges Details when Charge table is pressed
     */
    public void populateRemitProdCharge() {
        try {
            LinkedHashMap hashSubCharges = (LinkedHashMap)hashCharges.get(returnChargeKey());
            //System.out.println("@@@@@@@@hashSubCharges"+hashSubCharges);
            objRemittanceProductChargeTO = (RemittanceProductChargeTO) hashSubCharges.get(returnSubChargeKey());
            
            setRemittanceProductChargesOB(objRemittanceProductChargeTO);
            hashSubCharges = null;
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }    

    /**
     * Checking whether Remittance Product Charges Charge Type and Category are duplicated or not
     */
    private boolean duplicationCheckForCharges(String chargeKey,String subChargeKey,RemittanceProductChargeTO objRemittanceProductChargeTO,int row) throws Exception {
        //        System.out.println("chargeKey :" + chargeKey );
        //        System.out.println( " : subChbargeKey : " + subChargeKey );
        //        System.out.println(" : HC : " + hashCharges);
//        StringBuffer subCharge = new StringBuffer();
//        subCharge.append(CommonUtil.convertObjToStr((String)cbmChargeType.getKeyForSelected()));
//        subCharge.append(CommonUtil.convertObjToStr((String)cbmCategory.getKeyForSelected()));
//        String newSubCharge =  CommonUtil.convertObjToStr(subCharge);
        
        String newChargeType = CommonUtil.convertObjToStr((String)cbmChargeType.getKeyForSelected());
        String newCategory = CommonUtil.convertObjToStr((String)cbmCategory.getKeyForSelected());
        boolean chTypeAndCategory = false;
        boolean amountRange = false;
        if (hashCharges != null ) {
            if ((LinkedHashMap) hashCharges.get(chargeKey) != null) {
                LinkedHashMap hashSubCharges = (LinkedHashMap) hashCharges.get(chargeKey);
//                if (hashSubCharges.containsKey(subChargeKey) == true) {
//                    chTypeAndCategory = true;
//                } else {
//                    chTypeAndCategory = false;
//                }
                RemittanceProductChargeTO tmpRemittanceProductChargeTO= null;
                final Object[] keys = hashSubCharges.keySet().toArray();
                final int keysLength = keys.length;
                for (int i=0;i<keysLength;i++){
                    if (row==i) { continue;}
                    tmpRemittanceProductChargeTO = (RemittanceProductChargeTO)hashSubCharges.get(keys[i]);
                    if((((CommonUtil.convertObjToDouble(objRemittanceProductChargeTO.getAmtRangeFrom()).doubleValue() >= CommonUtil.convertObjToDouble(tmpRemittanceProductChargeTO.getAmtRangeFrom()).doubleValue())
                    && (CommonUtil.convertObjToDouble(objRemittanceProductChargeTO.getAmtRangeFrom()).doubleValue() <= CommonUtil.convertObjToDouble(tmpRemittanceProductChargeTO.getAmtRangeTo()).doubleValue()))
                    ||((CommonUtil.convertObjToDouble(objRemittanceProductChargeTO.getAmtRangeTo()).doubleValue() >= CommonUtil.convertObjToDouble(tmpRemittanceProductChargeTO.getAmtRangeFrom()).doubleValue())
                    && (CommonUtil.convertObjToDouble(objRemittanceProductChargeTO.getAmtRangeTo()).doubleValue() <= CommonUtil.convertObjToDouble(tmpRemittanceProductChargeTO.getAmtRangeTo()).doubleValue())))){
                        
                        if(((CommonUtil.convertObjToStr(tmpRemittanceProductChargeTO.getChargeType()).equalsIgnoreCase(newChargeType)) && 
                          (CommonUtil.convertObjToStr(tmpRemittanceProductChargeTO.getCategory()).equalsIgnoreCase(newCategory)))){
                                amountRange = true;
                                break;
                        }
                    }else{
                        amountRange = false;
//                        break;
                    }
                }
                hashSubCharges = null;
            }
        }
        
        //        return chTypeAndCategory && amountRange;
//        if(chTypeAndCategory && amountRange){
//            return true;
//        }else
            return amountRange;
    }
    
    /**
     * If Table Branch And Remittance Number is Selected in the Remittance Product Charges Screen
     */
    public void tableBranchRemittanceNumberIsSelected() {
        try {
            if (hashCharges != null) {
                LinkedHashMap hashSubCharges = (LinkedHashMap) hashCharges.get(returnChargeKey());
//                System.out.println("hashSubCharges:" + hashSubCharges);
                getKeySet(hashSubCharges);
                hashSubCharges = null;
            }
        } catch(Exception e){
            parseException.logException(e,true);
        }
    }
    /**
     * To get the Key Set
     */
    private void getKeySet(LinkedHashMap hash) {
        if (chargesRowData == null)
            chargesRowData = new ArrayList();
        chargesRowData.clear();
        if (hash != null) {
            Object obj[] = hash.keySet().toArray();
            
            for(int i=0,j=hash.size();i<j;i++) {
                String strKey = CommonUtil.convertObjToStr(obj[i]);
                objRemittanceProductChargeTO = (RemittanceProductChargeTO)hash.get(strKey);
                
                strKey = null;
                chargesRowData.add(setChargeColumnValues(objRemittanceProductChargeTO));
                objRemittanceProductChargeTO = null;
            }
            tblRemittanceProductCharge.setDataArrayList(chargesRowData,remittanceProductChargeTabTitle);
        } else {
            tblRemittanceProductCharge.setDataArrayList(chargesRowData,remittanceProductChargeTabTitle);
        }
        notifyObservers();
    }
    public void getKeyFromTable(int index){
        ArrayList tableData = (ArrayList)tblRemittanceProductCharge.getDataArrayList();
        ArrayList rowValues = (ArrayList)tableData.get(index);
        setCboChargeType((String)rowValues.get(0));
        setCboCategory((String)rowValues.get(1));
        setTxtAmtRangeFrom((String)rowValues.get(2));
//        setTxtAmtRangeTo((String)rowValues.get(3));
        tableData = null;
        rowValues = null;
    }
    /**
     * To return Remittance Product Charge Key
     */
    private String returnChargeKey() throws Exception {
        StringBuffer chargeKey = new StringBuffer();
//        if (getRdoSeriesGR_Yes() == true) {
            chargeKey.append(CommonUtil.convertObjToStr(getLblChargesBankCode()));
            chargeKey.append(CommonUtil.convertObjToStr(getLblChargesBranchCode()));
            
            if (chargeKey.toString().equals("")) {
                chargeKey.append(CommonUtil.convertObjToStr(getTxtProductIdGR()));
            }
//        } else if (getRdoSeriesGR_No() == true) {
//            chargeKey.append(CommonUtil.convertObjToStr(getTxtProductIdGR()));
//        }
        return CommonUtil.convertObjToStr(chargeKey);
    }
    
    private String returnSubChargeKey() throws Exception {
        StringBuffer chargeKey = new StringBuffer();
        chargeKey.append(CommonUtil.convertObjToStr((String)cbmChargeType.getKeyForSelected()));
        chargeKey.append(CommonUtil.convertObjToStr((String)cbmCategory.getKeyForSelected()));
        chargeKey.append(CommonUtil.convertObjToDouble(getTxtAmtRangeFrom()).doubleValue());
        return CommonUtil.convertObjToStr(chargeKey);
    }
    private String returnKeyFromRemitProdChargeTO(RemittanceProductChargeTO objChargeTO) {
        StringBuffer chargeKey = new StringBuffer();
        if (objChargeTO != null) {
//            if (getRdoSeriesGR_Yes() == true) {
                chargeKey.append(CommonUtil.convertObjToStr(objChargeTO.getBankCode()));
                chargeKey.append(CommonUtil.convertObjToStr(objChargeTO.getBranchCode()));

                if (chargeKey.toString().equals("")) {
                    chargeKey.append(CommonUtil.convertObjToStr(objChargeTO.getProdId()));
                }                
//            } else if (getRdoSeriesGR_No() == true) {
//                chargeKey.append(CommonUtil.convertObjToStr(objChargeTO.getProdId()));
//            }
        }
        return CommonUtil.convertObjToStr(chargeKey);
    }
    private String returnSubKeyFromRemitProdChargeTO(RemittanceProductChargeTO objChargeTO) {
        StringBuffer chargeKey = new StringBuffer();
        if (objChargeTO != null) {
            chargeKey.append(CommonUtil.convertObjToStr(objChargeTO.getChargeType()));
            chargeKey.append(CommonUtil.convertObjToStr(objChargeTO.getCategory()));
            chargeKey.append(CommonUtil.convertObjToDouble(objChargeTO.getAmtRangeFrom()).doubleValue());
        }
        return CommonUtil.convertObjToStr(chargeKey);
    }
    /**
     * Set Column values in the Remittance Product Charges details table
     */
    private ArrayList setChargeColumnValues(RemittanceProductChargeTO objRemittanceProductChargeTO) {
        ArrayList row = new ArrayList();
        row.add((String) getCbmChargeType().getDataForKey(CommonUtil.convertObjToStr(objRemittanceProductChargeTO.getChargeType())));
        row.add((String) getCbmCategory().getDataForKey(CommonUtil.convertObjToStr(objRemittanceProductChargeTO.getCategory())));
        row.add(CommonUtil.convertObjToStr(objRemittanceProductChargeTO.getAmtRangeFrom()));
        row.add(CommonUtil.convertObjToStr(objRemittanceProductChargeTO.getAmtRangeTo()));
        row.add(CommonUtil.convertObjToStr(objRemittanceProductChargeTO.getCharge()));
        return row;
    }
    
    /**
     * Getter for property txtAmtRangeFrom.
     * @return Value of property txtAmtRangeFrom.
     */
    public java.lang.String getTxtAmtRangeFrom() {
        return txtAmtRangeFrom;
    }
    
    /**
     * Setter for property txtAmtRangeFrom.
     * @param txtAmtRangeFrom New value of property txtAmtRangeFrom.
     */
    public void setTxtAmtRangeFrom(java.lang.String txtAmtRangeFrom) {
        this.txtAmtRangeFrom = txtAmtRangeFrom;
    }
    
    /**
     * Getter for property txtAmtRangeTo.
     * @return Value of property txtAmtRangeTo.
     */
    public java.lang.String getTxtAmtRangeTo() {
        return txtAmtRangeTo;
    }
    
    /**
     * Setter for property txtAmtRangeTo.
     * @param txtAmtRangeTo New value of property txtAmtRangeTo.
     */
    public void setTxtAmtRangeTo(java.lang.String txtAmtRangeTo) {
        this.txtAmtRangeTo = txtAmtRangeTo;
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

    public String getTxtMaxAmt() {
        return txtMaxAmt;
    }

    public void setTxtMaxAmt(String txtMaxAmt) {
        this.txtMaxAmt = txtMaxAmt;
    }

    public String getTxtMinAmt() {
        return txtMinAmt;
    }

    public void setTxtMinAmt(String txtMinAmt) {
        this.txtMinAmt = txtMinAmt;
    }

    public void setDescList(ArrayList descList) {
        this.descList = descList;
    }
     public ArrayList getDescList() {
       return descList;
    }

   public boolean isChkNewProcedure() {
        return chkNewProcedure;
    }

    public void setChkNewProcedure(boolean chkNewProcedure) {
        this.chkNewProcedure = chkNewProcedure;
    }

    public String getTdtNewProcStartDt() {
        return tdtNewProcStartDt;
    }

    public void setTdtNewProcStartDt(String tdtNewProcStartDt) {
        this.tdtNewProcStartDt = tdtNewProcStartDt;
    }
    public String getTxtNewProcIssueAcHd() {
        return txtNewProcIssueAcHd;
    }

    public void setTxtNewProcIssueAcHd(String txtNewProcIssueAcHd) {
        this.txtNewProcIssueAcHd = txtNewProcIssueAcHd;
    }

    public String getTxtMaximumAmount() {
        return txtMaximumAmount;
    }

    public void setTxtMaximumAmount(String txtMaximumAmount) {
        this.txtMaximumAmount = txtMaximumAmount;
    }

    public String getTxtMinimumAmount() {
        return txtMinimumAmount;
    }

    public void setTxtMinimumAmount(String txtMinimumAmount) {
        this.txtMinimumAmount = txtMinimumAmount;
    }
     
    public String getTxtRTGSSuspenseHead() {
        return txtRTGSSuspenseHead;
    }

    public void setTxtRTGSSuspenseHead(String txtRTGSSuspenseHead) {
        this.txtRTGSSuspenseHead = txtRTGSSuspenseHead;
    }
    
    /***************Modifications for Remit Product Charges tab Ends*************/
   
}