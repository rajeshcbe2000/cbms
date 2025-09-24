/*Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemittanceIssueOB.java
 *
 * Created on January 6, 2004, 12:10 PM
 */

package com.see.truetransact.ui.remittance;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.transferobject.remittance.RemittanceIssueTO;
import com.see.truetransact.transferobject.remittance.RemittanceIssueTransactionTO;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientproxy.ProxyFactory;
import java.util.Observable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import org.apache.log4j.Logger;
import com.see.truetransact.ui.common.transaction.TransactionOB;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.ui.TrueTransactMain ;

/**
 *
 * @author  Prasath.T
 */

public class RemittanceIssueOB extends com.see.truetransact.uicomponent.CObservable {
    
    Date curDate = null;
    private String txtTotalIssueAmt = "";
    private String txtTotalInstruments = "";
    private String cboProductId = "";
    private String cboPayeeProductId = "";
    private String txtProductId = "" ;
    private String cboProductType = "";
    private String cboPayeeProductType = "";
    private String txtFavouring = "";
    private String cboTransmissionType = "";
    private String txtPANGIRNo = "";
    private String cboCategory = "";
    private String cboCity = "";
    private String cboDraweeBank = "";
    private String cboBranchCode = "";
    private String txtAmt = "";
    private String txtExchange = "";
    private String oldExg = "";
    private String oldPos = "";
    private String oldSt = "";
    private String txtExchange1 = "";
    private String txtTotalAmt = "";
    private String txtPayeeAccHead = "";
    private String txtOtherCharges = "";
    private String txtPayeeAccNo = "";
    private String txtPostage = "";
    private String txtInstrumentNo1 = "";
    private String txtInstrumentNo2 = "";
    private String txtVariableNo = "";
    private String txtRemarks = "";
    private String cboCrossing = "";
    private ComboBoxModel cbmTransProductId;
    private TransactionOB transactionOB;
    private HashMap _authorizeMap ;
    private HashMap oldTransDetMap = null;
    
    private String operationMode = null ;
    public HashMap dupDetMap;
    
    // Variables declaration for Transaction Details
    // Variables declaration for Issue Details
    
    private String cboRevalidateTransType = "";
    private String cboDuplicateTransType = "";
    private String cummulativeIssueTotal = "";
    private String batchDt ="";
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private String lblAccountHeadDisplay = "";
    private String lblAccHeadBalDisplay = "";
    private String lblBatchId = "";
    private String txtIssueId = "";
    private String variableNo = "";
    private String transactionId = "";
    private String alertForAmount = "";
    private String warningMessage = "";
    
    private String txtRevalidationCharge = "";
    private String tdtDOExpiring = "";
    private String txtRevalidateRemarks = "";
    private String lblLapsePeriod = "";
    
    private String txtDuplicateRemarks = "";
    private String txtDuplicationCharge = "";
    private String txtServiceTax = "";
    private String txtDupServTax = "";
    private String txtRevServTax = "";
    private String selectRow = "";
    private String setTOStatus = "";
    
    
    
    private String lblDraweeBank = "";
    private String lblBranchCode = "";
    private String lblVariableNo = "";
    
    private String lblRevalidationDate = "";
    private String lblDuplicationDate = "";
    private String strExchange = "";
    private String authorizeStatus = "";
    private String authorizeDate = "";
    private String authorizeUser = "";
    private String paidStatus = "";
    
    private String statusBy = "";
    private String statusDt = "";
    private String branchIdFromTrueTransactMain = "";
    
    private String payableAt = "" ;
    
    private boolean dupInEdit = false;
    private boolean dupInDelete = false;
    
    private boolean revInEdit = false;
    private boolean revInDelete = false;
    private String revTotAmt = "";
    private int tranCnt = 0;
    
    private String tempProdId = "";
    
    private String totAmt = "";
    private String dupTotAmt = "";
    private String varNum = "";
    private String authStatus = "";
    private String authRemarks = "";
    private String remitForFlag="";
    private final String DELETED_ISSUE_TOs = "DELETED_ISSUE_TOs";
    private final String NOT_DELETED_ISSUE_TOs = "NOT_DELETED_ISSUE_TOs";
    
    private final String DELETED_TRANS_TOs = "DELETED_TRANS_TOs";
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    
    
    private HashMap lookUpHash;
    private HashMap keyValue;
    private HashMap operationMap;
    
    
    private LinkedHashMap issueDetailsTO = null;
    private LinkedHashMap deletedIssueTO = null;
    public LinkedHashMap allowedIssueTO = null;
    private HashMap loanActMap=new HashMap();
    private LinkedHashMap transactionDetailsTO = null;
    private LinkedHashMap deletedTransactionDetailsTO = null;
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private HashMap procChargeMap=new HashMap();
    private ArrayList key;
    private ArrayList value;
    private ArrayList tableData = null;
    private ArrayList rowDataForTransDetails = null;//Row values for Trans Details table
    final   ArrayList issueDetailsTitle = new ArrayList(); // Issue Details table Column names
    
    private ComboBoxModel cbmProductType ;
    private ComboBoxModel cbmPayeeProductType ;
    private ComboBoxModel cbmPayeeProductId ;
    private ComboBoxModel cbmCategory;
    private ComboBoxModel cbmTransactionType;// Transaction Type in Trans Details
    private ComboBoxModel cbmCrossing;
    private ComboBoxModel cbmTransmissionType;
    private ComboBoxModel cbmProductId;
    private ComboBoxModel cbmCity;
    private ComboBoxModel cbmDraweeBank;
    private ComboBoxModel cbmBranchCode;
    private ComboBoxModel cbmRevalidateTransType;
    private ComboBoxModel cbmDuplicateTransType;
    
    private int result;
    private int actionType;
    private static int dup = 0;
    private static int issueSerialNo = 1;
    private static int noOfDeletedIssueTOs = 1;
    final int UPDATE = 2,DELETE = 3,NEW =1,CANCELLATION = 0,REVALIDATION =1,DUPLICATION = 2;
    
    private ProxyFactory proxy;
    private EnhancedTableModel tblIssueDetails;// Issue Details table model
    private EnhancedTableModel tblTransactionDetails;// Transaction Details table model
    final RemittanceIssueRB objRemitIssueRB = new RemittanceIssueRB();
    private static RemittanceIssueTO updateTOWithDuplicateDetails = new RemittanceIssueTO();
    private static RemittanceIssueTO updateTOWithRevalidateDetails = new RemittanceIssueTO();
    
    private final static Logger log = Logger.getLogger(RemittanceIssueOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    public boolean depositFlag = false;
    // End of variables declaration
    
    
    
    /** Creates a new instance of RemittanceIssueOB */
    public RemittanceIssueOB() throws Exception{
        curDate = ClientUtil.getCurrentDate();
        proxy = ProxyFactory.createProxy();
        setOperationMode(CommonConstants.REMIT_ISSUE);
        //Default to issue. If R or D changes from that screen
        setOperationMap();
        
        cbmCity = new ComboBoxModel();
        cbmDraweeBank = new ComboBoxModel();
        cbmBranchCode = new ComboBoxModel();
        cbmCategory = new ComboBoxModel();
        cbmProductType =  new ComboBoxModel();
        cbmPayeeProductType =  new ComboBoxModel();
        cbmPayeeProductId =  new ComboBoxModel();
        fillDropdown();
        setIssueDetailsTitle();
        tblIssueDetails = new EnhancedTableModel(null, issueDetailsTitle);
        tblTransactionDetails = new EnhancedTableModel(null, issueDetailsTitle);
        makeComboBoxKeyValuesNull();
    }
    
    /* Sets the HashMap required to set JNDI,Home and Remote*/
    private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "RemittanceIssueJNDI");
        operationMap.put(CommonConstants.HOME, "remittance.RemittanceIssueHome");
        operationMap.put(CommonConstants.REMOTE, "remittance.RemittanceIssue");
    }
    
    /* Sets IssueDetails  with table column headers */
    private void setIssueDetailsTitle() throws Exception{
        issueDetailsTitle.add(objRemitIssueRB.getString("tblIssueColumn1"));
        issueDetailsTitle.add(objRemitIssueRB.getString("tblIssueColumn2"));
        issueDetailsTitle.add(objRemitIssueRB.getString("tblIssueColumn3"));
        issueDetailsTitle.add(objRemitIssueRB.getString("tblIssueColumn4"));
    }
    
    public void fillDropdown() throws Exception{
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
        lookup_keys.add("PRODUCTTYPE");
        lookup_keys.add("REMITTANCE_ISSUE.TRANSACTION_TYPE");
        lookup_keys.add("REMITTANCE_ISSUE.CROSSING");
        
        lookup_keys.add("REMITTANCE_ISSUE.TRANSMISSION_TYPE");
        lookup_keys.add("PROD_ID");
        
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("REMITTANCE_ISSUE.TRANSACTION_TYPE"));
        cbmTransactionType = new ComboBoxModel(key,value);
        
        //        keyValue = ClientUtil.populateLookupData(lookUpHash);
        //        getKeyValue((HashMap)keyValue.get("REMITTANCE_ISSUE.TRANSACTION_TYPE"));
        cbmRevalidateTransType = new ComboBoxModel(key,value);
        
        //        keyValue = ClientUtil.populateLookupData(lookUpHash);
        //        getKeyValue((HashMap)keyValue.get("REMITTANCE_ISSUE.TRANSACTION_TYPE"));
        cbmDuplicateTransType = new ComboBoxModel(key,value);
        
//        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
        // TMPRemove GL entry from ProductType
        key.remove("GL");
        value.remove("General Ledger");
        cbmProductType = new ComboBoxModel(key,value);
        cbmPayeeProductType = new ComboBoxModel(key,value);
        
//        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("REMITTANCE_ISSUE.CROSSING"));
        cbmCrossing = new ComboBoxModel(key,value);
        
//        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("REMITTANCE_ISSUE.TRANSMISSION_TYPE"));
        cbmTransmissionType = new ComboBoxModel(key,value);
        
        
        
        /* ProdId is taken from Remittance_product */
        lookUpHash.put(CommonConstants.MAP_NAME,"RemitIssuegetProdId");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmProductId = new ComboBoxModel(key,value);
        
        makeComboBoxKeyValuesNull();
    }
    
    //        public void getProductIdByType() throws Exception{//String productType)
    //        /** The data to be show in Combo Box other than LOOKUP_MASTER table
    //         * Show Product Id */
    //        System.out.println("PRODUCT TYPE = " + getCboProductType());
    //        HashMap lookUpHash = new HashMap();
    //        lookUpHash.put(CommonConstants.MAP_NAME,"Charges.getProductData" + (String) getCbmProductType().getKeyForSelected());
    //        lookUpHash.put(CommonConstants.PARAMFORQUERY,null);
    //        keyValue = ClientUtil.populateLookupData(lookUpHash);
    //        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
    //        cbmTransProductId = new ComboBoxModel(key,value);
    //    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    //***********************
    
    // Setter method for statusDt
    void setStatusDate(String statusDt){
        this.statusDt = statusDt;
        setChanged();
    }
    // Getter method for statusDt
    String getStatusDate(){
        return this.statusDt;
    }
    // Setter method for statusBy
    void setStatusBy1(String statusBy){
        this.statusBy = statusBy;
        //        setChanged();
    }
    // Getter method for statusBy
    String getStatusBy1(){
        return this.statusBy;
    }
    
    
    // Setter method for cboRevalidateTransType
    public  void setCboRevalidateTransType(String cboRevalidateTransType){
        this.cboRevalidateTransType = cboRevalidateTransType;
        setChanged();
    }
    // Getter method for cboRevalidateTransType
    public String getCboRevalidateTransType(){
        return this.cboRevalidateTransType;
    }
    // Setter method for cboDuplicateTransType
    public  void setCboDuplicateTransType(String cboDuplicateTransType){
        this.cboDuplicateTransType = cboDuplicateTransType;
        setChanged();
    }
    // Getter method for cboDuplicateTransType
    public String getCboDuplicateTransType(){
        return this.cboDuplicateTransType;
    }
    
    // Setter method for setTOStatus
    void setTOStatus(String setTOStatus){
        this.setTOStatus = setTOStatus;
    }
    // Getter method for setTOStatus
    String getTOStatus(){
        return this.setTOStatus;
    }
    // Setter method for strExchange
    void setTOExchange(String strExchange){
        this.strExchange = strExchange;
    }
    // Getter method for strExchange
    String getTOExchange(){
        return this.strExchange;
    }
    // To reset the fields present in the RemitIssueRevalidationUI
    public void resetRevalidation(){
        setLblDraweeBank("");
        setLblBranchCode("");
        setLblVariableNo("");
        setLblLapsePeriod("");
        setLblRevalidationDate("");
        setLblDuplicationDate("");
        setTxtRevalidationCharge("");
        setTdtDOExpiring("");
        setTxtRevalidateRemarks("");
        setCboRevalidateTransType("");
        ttNotifyObservers();
    }
    
    /**
     * methods corresponds to RemitIssueRevalidateUI
     */
    
    // Setter method for txtRevalidationCharge
    public void setTxtRevalidationCharge( String txtRevalidationCharge){
        this.txtRevalidationCharge = txtRevalidationCharge;
        setChanged();
    }
    // Getter method for txtRevalidationCharge
    public String getTxtRevalidationCharge(){
        return this.txtRevalidationCharge;
    }
    // Setter method for tdtDOExpiring
    public void setTdtDOExpiring( String tdtDOExpiring){
        this.tdtDOExpiring = tdtDOExpiring;
        setChanged();
    }
    // Getter method for tdtDOExpiring
    public String getTdtDOExpiring(){
        return this.tdtDOExpiring;
    }
    // Setter method for branchIdFromTrueTransactMain
    public void setBranchIdFromTrueTransactMain( String branchIdFromTrueTransactMain){
        this.branchIdFromTrueTransactMain = branchIdFromTrueTransactMain;
    }
    // Getter method for branchIdFromTrueTransactMain
    public String getBranchIdFromTrueTransactMain(){
        return this.branchIdFromTrueTransactMain;
    }
    // Setter method for txtRevalidateRemarks
    public void setTxtRevalidateRemarks( String txtRevalidateRemarks){
        this.txtRevalidateRemarks = txtRevalidateRemarks;
        setChanged();
    }
    // Getter method for txtRevalidateRemarks
    public String getTxtRevalidateRemarks(){
        return this.txtRevalidateRemarks;
    }
    
    // To reset the fields present in the RemitIssueDuplicationUI
    public void resetDuplication(){
        setCboDuplicateTransType("");
        setTxtDuplicateRemarks("");
        setTxtDuplicationCharge("");
        setTxtDupServTax("");
    }
    public void populateLabelFields(){
        try{
            log.info("In populateLabelFields method");
            //            RemittanceIssueUI remit = new RemittanceIssueUI();
            //            remit.updateOBFields();
            setLblDraweeBank(CommonUtil.convertObjToStr(getCboDraweeBank()));
            setLblBranchCode(CommonUtil.convertObjToStr(getCboBranchCode()));
            setLblVariableNo(CommonUtil.convertObjToStr(getTxtVariableNo()));
            setLblLapsePeriod(CommonUtil.convertObjToStr(getLblLapsePeriod()));
        }catch (Exception e){
            parseException.logException(e,true);
        }
    }
    public void setRevalidDate(){
        try{
            setLblRevalidationDate(CommonUtil.convertObjToStr( DateUtil.getStringDate( curDate ) ) );
            findLapsedPeriod();
        }catch (Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void setDuplicateDate(){
        try{
            setLblDuplicationDate(CommonUtil.convertObjToStr( DateUtil.getStringDate( curDate ) ) );
        }catch (Exception e){
            parseException.logException(e,true);
        }
    }
    
    
    // Setter method for txtRevalidationCharge
    public void setTxtDuplicateRemarks( String txtDuplicateRemarks){
        this.txtDuplicateRemarks = txtDuplicateRemarks;
        setChanged();
    }
    // Getter method for txtRevalidationCharge
    public String getTxtDuplicateRemarks(){
        return this.txtDuplicateRemarks;
    }
    // Setter method for tdtDOExpiring
    public void setTxtDuplicationCharge( String txtDuplicationCharge){
        this.txtDuplicationCharge = txtDuplicationCharge;
        setChanged();
    }
    // Getter method for tdtDOExpiring
    public String getTxtDuplicationCharge(){
        return this.txtDuplicationCharge;
    }
    
    
    
    
    //**********************************************
    public void setCbmCategory(ComboBoxModel cbmCategory){
        this.cbmCategory = cbmCategory;
        setChanged();
    }
    
    ComboBoxModel getCbmCategory(){
        return cbmCategory;
    }
    public void setCbmCity(ComboBoxModel cbmCity){
        this.cbmCity = cbmCity;
        setChanged();
        System.out.println("cbmCity : "+cbmCity.getKeys());
    }
    
    ComboBoxModel getCbmCity(){
        return cbmCity;
    }
    public void setCbmDraweeBank(ComboBoxModel cbmDraweeBank){
        this.cbmDraweeBank = cbmDraweeBank;
        setChanged();
    }
    
    ComboBoxModel getCbmDraweeBank(){
        return cbmDraweeBank;
    }
    public void setCbmBranchCode(ComboBoxModel cbmBranchCode){
        this.cbmBranchCode = cbmBranchCode;
        setChanged();
    }
    
    ComboBoxModel getCbmBranchCode(){
        return cbmBranchCode;
    }
    
    public void setCbmTransactionType(ComboBoxModel cbmTransactionType){
        this.cbmTransactionType = cbmTransactionType;
        setChanged();
    }
    
    public ComboBoxModel getCbmTransactionType(){
        return cbmTransactionType;
    }
    
    public void setCbmCrossing(ComboBoxModel cbmCrossing){
        this.cbmCrossing = cbmCrossing;
        setChanged();
    }
    
    ComboBoxModel getCbmCrossing(){
        return cbmCrossing;
    }
    
    public void setCbmTransmissionType(ComboBoxModel cbmTransmissionType){
        this.cbmTransmissionType = cbmTransmissionType;
        setChanged();
    }
    
    ComboBoxModel getCbmTransmissionType(){
        return cbmTransmissionType;
    }
    public void setCbmTransactionTypeForRevalidation(ComboBoxModel cbmRevalidateTransType){
        this.cbmRevalidateTransType = cbmRevalidateTransType;
        setChanged();
    }
    
    public ComboBoxModel getCbmTransactionTypeForRevalidation(){
        return cbmRevalidateTransType;
    }
    public void setCbmTransactionTypeForDuplication(ComboBoxModel cbmDuplicateTransType){
        this.cbmDuplicateTransType = cbmDuplicateTransType;
        setChanged();
    }
    
    public ComboBoxModel getCbmTransactionTypeForDuplication(){
        return cbmDuplicateTransType;
    }
    
    public void setCbmProductId(ComboBoxModel cbmProductId){
        this.cbmProductId = cbmProductId;
        setChanged();
    }
    
    ComboBoxModel getCbmProductId(){
        return cbmProductId;
    }
    
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    
    public String getLblStatus(){
        return this.lblStatus;
    }
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }
    
    public int getActionType(){
        return this.actionType;
    }
    
    
    public int getResult(){
        return this.result;
    }
    
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    
    public void setBatchId(String lblBatchId){
        this.lblBatchId = lblBatchId;
        setChanged();
    }
    
    public String getBatchId(){
        return this.lblBatchId;
    }
    
    public void setIssueId(String txtIssueId){
        this.txtIssueId = txtIssueId;
        setChanged();
    }
    
    /**
     *
     * @return the Issue Id for Issue Details
     */
    public String getIssueId(){
        return this.txtIssueId;
    }
    // Setter for transactionId Automatic generation
    /**
     *
     * @param transactionId is used to store the Transaction Id for Transaction Details
     */
    public void setTransactionId(String transactionId){
        this.transactionId = transactionId;
        setChanged();
    }
    // Getter for transactionId Automatic generation
    /**
     *
     * @return the Transaction Id
     */
    public String getTransactionId(){
        return this.transactionId;
    }
    
    /**
     *
     * @param batchDt is used to store in which date the Batch is created
     */
    public void setBatchDt(String batchDt){
        this.batchDt = batchDt;
        setChanged();
    }
    
    /**
     *
     * @return the created Batch Date
     */
    public String getBatchDt(){
        return this.batchDt;
    }
    /* To set the lblAccountHeadDisplay to display Issue_HD from REMITTANCE_PRODUCT */
    public void setLblAccountHeadDisplay(String lblAccountHeadDisplay){
        this.lblAccountHeadDisplay = lblAccountHeadDisplay;
        setChanged();
    }
    /* To get the value in lblAccountHeadDisplay */
    public String getLblAccountHeadDisplay(){
        return this.lblAccountHeadDisplay;
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }
    /* To set the model in table issue details */
    void setTblIssueDetails(EnhancedTableModel tblIssue){
        this.tblIssueDetails = tblIssue;
        setChanged();
    }
    
    EnhancedTableModel getTblIssueDetails(){
        return this.tblIssueDetails;
    }
    /* To set the model in table trans details */
    void setTblTransDetails(EnhancedTableModel tblTransactionDetails){
        this.tblTransactionDetails = tblTransactionDetails;
        setChanged();
    }
    
    EnhancedTableModel getTblTransDetails(){
        return this.tblTransactionDetails;
    }
    
    /**
     * set the Total Amt by adding exchange, postage, other charges, amount
     */
    public void addAllCharges(){
        
        double totalAmt =0;
        totalAmt =  CommonUtil.convertObjToDouble(getTxtExchange()).doubleValue()     +
        CommonUtil.convertObjToDouble(getTxtPostage()).doubleValue()                  +
        CommonUtil.convertObjToDouble(getTxtOtherCharges()).doubleValue()             +
        CommonUtil.convertObjToDouble(getTxtAmt()).doubleValue();
        
        if(getOperationMode().equals(CommonConstants.REMIT_REVALIDATE))
            totalAmt = CommonUtil.convertObjToDouble(getTxtRevalidationCharge()).doubleValue()   +
            CommonUtil.convertObjToDouble(getTxtAmt()).doubleValue();
        
        if(getOperationMode().equals(CommonConstants.REMIT_DUPLICATE))
            totalAmt = CommonUtil.convertObjToDouble(getTxtDuplicationCharge()).doubleValue()   +
            CommonUtil.convertObjToDouble(getTxtAmt()).doubleValue();
        
        setTxtAmt(String.valueOf(totalAmt));
        
    }
    private void makeComboBoxKeyValuesNull(){
        key = null;
        value = null;
        lookUpHash = null;
        keyValue = null;
    }
    /* To set common data in the Transfer Object*/
    public RemittanceIssueTO setRemittanceIssueTO() {
        log.info("Inside setRemittanceIssueTO()");
        final RemittanceIssueTO objRemittanceIssueTO = new RemittanceIssueTO();
        try{
            objRemittanceIssueTO.setCommand(CommonUtil.convertObjToStr(getCommand()));
            objRemittanceIssueTO.setBatchId(CommonUtil.convertObjToStr(getBatchId()));
            objRemittanceIssueTO.setIssueId(CommonUtil.convertObjToStr(getIssueId()));
            
            Date BtDt = DateUtil.getDateMMDDYYYY(getBatchDt());
            if(BtDt != null){
                Date btDate = (Date)curDate.clone();
                btDate.setDate(BtDt.getDate());
                btDate.setMonth(BtDt.getMonth());
                btDate.setYear(BtDt.getYear());
                objRemittanceIssueTO.setBatchDt(btDate);
            }else{
                objRemittanceIssueTO.setBatchDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getBatchDt())));
            }
            
            //            objRemittanceIssueTO.setBatchDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getBatchDt())));
            objRemittanceIssueTO.setProdId(CommonUtil.convertObjToStr((String)cbmProductId.getKeyForSelected()));
            objRemittanceIssueTO.setCity(CommonUtil.convertObjToStr((String)cbmCity.getKeyForSelected()));
            objRemittanceIssueTO.setDraweeBank(CommonUtil.convertObjToStr((String)cbmDraweeBank.getKeyForSelected()));
            objRemittanceIssueTO.setDraweeBranchCode(CommonUtil.convertObjToStr((String)cbmBranchCode.getKeyForSelected()));
            objRemittanceIssueTO.setFavouring(CommonUtil.convertObjToStr(getTxtFavouring()));
            objRemittanceIssueTO.setRemitType(CommonUtil.convertObjToStr((String)cbmTransmissionType.getKeyForSelected()));
            objRemittanceIssueTO.setAmount(CommonUtil.convertObjToDouble(getTxtAmt()));
            objRemittanceIssueTO.setPanGirNo(CommonUtil.convertObjToStr(getTxtPANGIRNo()));
            objRemittanceIssueTO.setCategory(CommonUtil.convertObjToStr((String)cbmCategory.getKeyForSelected()));
            objRemittanceIssueTO.setPayeeProdType(CommonUtil.convertObjToStr((String)cbmPayeeProductType.getKeyForSelected()));
            getCbmProductId().setSelectedItem(getCboProductId());
            objRemittanceIssueTO.setPayeeProdId(CommonUtil.convertObjToStr((String)cbmPayeeProductId.getKeyForSelected()));
            objRemittanceIssueTO.setExchange(CommonUtil.convertObjToDouble(getTxtExchange()));
            objRemittanceIssueTO.setExgCal(CommonUtil.convertObjToDouble(getTxtExchange1()));
            objRemittanceIssueTO.setPostage(CommonUtil.convertObjToDouble(getTxtPostage()));
            objRemittanceIssueTO.setOtherCharges(CommonUtil.convertObjToDouble(getTxtOtherCharges()));
            objRemittanceIssueTO.setTotalAmt(CommonUtil.convertObjToDouble(getTxtTotalAmt()));
            objRemittanceIssueTO.setRemarks(CommonUtil.convertObjToStr(getTxtRemarks()));
            objRemittanceIssueTO.setPayeeAcctHead(CommonUtil.convertObjToStr(getTxtPayeeAccHead()));
            objRemittanceIssueTO.setCrossing(CommonUtil.convertObjToStr((String)cbmCrossing.getKeyForSelected()));
            objRemittanceIssueTO.setPayeeAcctNo(CommonUtil.convertObjToStr(getTxtPayeeAccNo()));
            objRemittanceIssueTO.setInstrumentNo1(CommonUtil.convertObjToStr(getTxtInstrumentNo1()));
            objRemittanceIssueTO.setInstrumentNo2(CommonUtil.convertObjToStr(getTxtInstrumentNo2()));
            objRemittanceIssueTO.setVariableNo(CommonUtil.convertObjToStr(getTxtVariableNo()));
            objRemittanceIssueTO.setStatus(CommonUtil.convertObjToStr(getTOStatus()));
            objRemittanceIssueTO.setRemitForFlag(CommonUtil.convertObjToStr(getRemitForFlag()));
            
            Date AtDt = DateUtil.getDateMMDDYYYY(getAuthorizeDt());
            if(AtDt != null){
                Date atDate = (Date)curDate.clone();
                atDate.setDate(AtDt.getDate());
                atDate.setMonth(AtDt.getMonth());
                atDate.setYear(AtDt.getYear());
                objRemittanceIssueTO.setAuthorizeDt(atDate);
            }else{
                objRemittanceIssueTO.setAuthorizeDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getAuthorizeDt())));
            }
            //            objRemittanceIssueTO.setAuthorizeDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getAuthorizeDt())));
            objRemittanceIssueTO.setAuthorizeStatus(CommonUtil.convertObjToStr(getAuthorizeStatus()));
            objRemittanceIssueTO.setAuthorizeUser(CommonUtil.convertObjToStr(getAuthorizeUser()));
            objRemittanceIssueTO.setPaidStatus(CommonUtil.convertObjToStr(getPaidStatus()));
            objRemittanceIssueTO.setStatusBy(CommonUtil.convertObjToStr(getStatusBy1()));
            
            Date StDt = DateUtil.getDateMMDDYYYY(getStatusDate());
            if(StDt != null){
                Date stDate = (Date)curDate.clone();
                stDate.setDate(StDt.getDate());
                stDate.setMonth(StDt.getMonth());
                stDate.setYear(StDt.getYear());
                objRemittanceIssueTO.setStatusDt(stDate);
            }else{
                objRemittanceIssueTO.setStatusDt(DateUtil.getDateMMDDYYYY(getStatusDate()));
            }
            //            objRemittanceIssueTO.setStatusDt(DateUtil.getDateMMDDYYYY(getStatusDate()));
            
            objRemittanceIssueTO.setBranchId(CommonUtil.convertObjToStr(getBranchIdFromTrueTransactMain()));
            
            if(getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                // add Revalidation details to the TO Object at the time Revalidation
                objRemittanceIssueTO.setRevalidateTrans(CommonUtil.convertObjToStr((String)cbmRevalidateTransType.getKeyForSelected()));
                objRemittanceIssueTO.setRevalidateCharge( CommonUtil.convertObjToDouble( getTxtRevalidationCharge() ) );
                Date RevDt = DateUtil.getDateMMDDYYYY(getLblRevalidationDate());
                if(RevDt != null){
                    Date revDate = (Date)curDate.clone();
                    revDate.setDate(RevDt.getDate());
                    revDate.setMonth(RevDt.getMonth());
                    revDate.setYear(RevDt.getYear());
                    objRemittanceIssueTO.setRevalidateDt(revDate);
                }else{
                    objRemittanceIssueTO.setRevalidateDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getLblRevalidationDate())));
                }
                //                objRemittanceIssueTO.setRevalidateDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getLblRevalidationDate())));
                
                Date ExpDt = DateUtil.getDateMMDDYYYY(getTdtDOExpiring());
                if(ExpDt != null){
                    Date expDate = (Date)curDate.clone();
                    expDate.setDate(ExpDt.getDate());
                    expDate.setMonth(ExpDt.getMonth());
                    expDate.setYear(ExpDt.getYear());
                    objRemittanceIssueTO.setRevalidateExpiryDt(expDate);
                }else{
                    objRemittanceIssueTO.setRevalidateExpiryDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtDOExpiring())));
                }
                //                objRemittanceIssueTO.setRevalidateExpiryDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtDOExpiring())));
                objRemittanceIssueTO.setRevalidateRemarks(CommonUtil.convertObjToStr(getTxtRevalidateRemarks()));
                // add Duplication details to the TO Object at the time of Duplication
                objRemittanceIssueTO.setDuplicateTrans(CommonUtil.convertObjToStr((String)cbmDuplicateTransType.getKeyForSelected()));
                objRemittanceIssueTO.setDuplicateCharge(CommonUtil.convertObjToDouble( getTxtDuplicationCharge() ) );
                
                Date DupDt = DateUtil.getDateMMDDYYYY(getLblDuplicationDate());
                if(DupDt != null){
                    Date dupDate = (Date)curDate.clone();
                    dupDate.setDate(DupDt.getDate());
                    dupDate.setMonth(DupDt.getMonth());
                    dupDate.setYear(DupDt.getYear());
                    objRemittanceIssueTO.setDuplicateDt(dupDate);
                }else{
                    objRemittanceIssueTO.setDuplicateDt(DateUtil.getDateMMDDYYYY(getLblDuplicationDate()));
                }
                //                objRemittanceIssueTO.setDuplicateDt(DateUtil.getDateMMDDYYYY(getLblDuplicationDate()));
                objRemittanceIssueTO.setDuplicateRemarks(CommonUtil.convertObjToStr(getTxtDuplicateRemarks()));
                objRemittanceIssueTO.setDupServTax(CommonUtil.convertObjToDouble(getTxtDupServTax()));
                objRemittanceIssueTO.setRevServTax(CommonUtil.convertObjToDouble(getTxtRevServTax()));
                //objRemittanceIssueTO.setCancelCharge(CommonUtil.convertObjToDouble( getTxtCancellationCharge() ) );
                //objRemittanceIssueTO.setCancelDt(DateUtil.getDateMMDDYYYY(getLblCancellationDate()));
                //objRemittanceIssueTO.setCancelRemarks(CommonUtil.convertObjToStr(getTxtCancellationRemarks()));
                objRemittanceIssueTO.setAuthorizeRemark(CommonUtil.convertObjToStr(getAuthRemarks()));
                
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return objRemittanceIssueTO;
    }
    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
        setChanged();
    }
    
    public String getAuthorizeStatus(){
        return this.authorizeStatus;
    }
    public void setAuthorizeDt(String authorizeDate) {
        this.authorizeDate = authorizeDate;
        setChanged();
    }
    
    public String getAuthorizeDt(){
        return this.authorizeDate;
    }
    public void setAuthorizeUser(String authorizeUser) {
        this.authorizeUser = authorizeUser;
        setChanged();
    }
    
    public String getAuthorizeUser(){
        return this.authorizeUser;
    }
    public void setPaidStatus(String paidStatus) {
        this.paidStatus = paidStatus;
        setChanged();
    }
    
    public String getPaidStatus(){
        return this.paidStatus;
    }
    /** To perform the appropriate operation */
    public boolean doAction() {
        boolean result = true;
        try {
            log.info("Inside doAction()");
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                System.out.println("getCommand () : " + getCommand());
                // if( getCommand() != null ){
                doActionPerform();
                //}
                //                else{
                //                    final RemittanceIssueRB objremittanceIssueRB = new RemittanceIssueRB();
                //                    throw new TTException(objremittanceIssueRB.getString("TOCommandError"));
                //                }
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            result = false;
            parseException.logException(e,true);
        }
        return result;
    }
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        int count = 0;
        log.info("Inside doActionPerform()");
        final HashMap data = new HashMap();
        System.out.println("######################"+updateTOWithDuplicateDetails);
        System.out.println("#############%^%^%#########"+updateTOWithRevalidateDetails);
        data.put("MODE", getCommand());
        data.put("OPERATION_MODE", getOperationMode());
        data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
        if(getProcChargeMap().size()>0)
            data.put("PROCCHARGEHASH",procChargeMap);
        if(loanActMap.size()>0)
            data.put("LINK_BATCH_ID",loanActMap.get("LINK_BATCH_ID"));
        if (issueDetailsTO == null)
            issueDetailsTO = new LinkedHashMap();
        if (transactionDetailsTO == null)
            transactionDetailsTO = new LinkedHashMap();
        if(updateTOWithDuplicateDetails == null)
            updateTOWithDuplicateDetails = new RemittanceIssueTO();
        if(updateTOWithRevalidateDetails == null)
            updateTOWithRevalidateDetails = new RemittanceIssueTO();
        
        if(getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            if (deletedIssueTO != null) {
                issueDetailsTO.put(DELETED_ISSUE_TOs,deletedIssueTO);
            }
            
            if (deletedTransactionDetailsTO != null) {
                transactionDetailsTO.put(DELETED_TRANS_TOs,deletedTransactionDetailsTO);
            }
        }
        
        issueDetailsTO.put(NOT_DELETED_ISSUE_TOs,allowedIssueTO);
        transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
        
        System.out.println("issueDetailsTO : " + issueDetailsTO);
        if((updateTOWithDuplicateDetails.getDuplicateCharge() != null) && (getOperationMode().equals(CommonConstants.REMIT_DUPLICATE))){
            data.put("updateTOWithDuplicateDetails",updateTOWithDuplicateDetails);
            data.put("MODE", CommonConstants.TOSTATUS_INSERT);
            setActionType(ClientConstants.ACTIONTYPE_DUPLICATE);
        }
        if((updateTOWithDuplicateDetails.getDuplicateCharge() == null) && (getOperationMode().equals(CommonConstants.REMIT_DUPLICATE))){
            data.put("MODE", CommonConstants.TOSTATUS_INSERT);
            setActionType(ClientConstants.ACTIONTYPE_DUPLICATE);
        }
        if(isDupInEdit()){
            data.put("MODE", CommonConstants.TOSTATUS_UPDATE);
            setDupInEdit(false);
            setActionType(ClientConstants.ACTIONTYPE_EDIT);
        }
        if(isDupInDelete()){
            data.put("MODE", CommonConstants.TOSTATUS_DELETE);
            setDupInDelete(false);
            setActionType(ClientConstants.ACTIONTYPE_DELETE);
        }
        
        if((updateTOWithRevalidateDetails.getRevalidateCharge() != null) && (getOperationMode().equals(CommonConstants.REMIT_REVALIDATE))){
            data.put("updateTOWithRevalidateDetails",updateTOWithRevalidateDetails);
            data.put("MODE", CommonConstants.TOSTATUS_INSERT);
            setActionType(ClientConstants.ACTIONTYPE_REVALIDATE);
        }
        if((updateTOWithRevalidateDetails.getRevalidateCharge() == null) && (getOperationMode().equals(CommonConstants.REMIT_REVALIDATE))){
            data.put("MODE", CommonConstants.TOSTATUS_INSERT);
            setActionType(ClientConstants.ACTIONTYPE_REVALIDATE);
        }
        if(isRevInEdit()){
            data.put("MODE", CommonConstants.TOSTATUS_UPDATE);
            setRevInEdit(false);
            setActionType(ClientConstants.ACTIONTYPE_EDIT);
        }
        if(isRevInDelete()){
            data.put("MODE", CommonConstants.TOSTATUS_DELETE);
            setRevInDelete(false);
            setActionType(ClientConstants.ACTIONTYPE_DELETE);
        }
        data.put("RemittanceIssueTO",issueDetailsTO);
        data.put("TransactionTO",transactionDetailsTO);
        data.put(CommonConstants.MODULE,getModule());
        data.put(CommonConstants.SCREEN,getScreen());
        
        if (oldTransDetMap != null)
            if (oldTransDetMap.size() > 0) {
                
                if (oldTransDetMap.containsKey("TransactionDetails"))
                    data.put("TransactionDetails", oldTransDetMap.get("TransactionDetails"));
                //        if (oldTransDetMap.containsKey("EXH_CASH_TRANS_DETAILS"))
                //            data.put("EXH_CASH_TRANS_DETAILS", oldTransDetMap.get("EXH_CASH_TRANS_DETAILS"));
                //        if (oldTransDetMap.containsKey("OTHERCHRG_CASH_TRANS_DETAILS"))
                //            data.put("OTHERCHRG_CASH_TRANS_DETAILS", oldTransDetMap.get("OTHERCHRG_CASH_TRANS_DETAILS"));
                //        if (oldTransDetMap.containsKey("POSTAGE_CASH_TRANS_DETAILS"))
                //            data.put("POSTAGE_CASH_TRANS_DETAILS", oldTransDetMap.get("POSTAGE_CASH_TRANS_DETAILS"));
                //        if (oldTransDetMap.containsKey("AMT_TRANSFER_TRANS_DETAILS"))
                //            data.put("AMT_TRANSFER_TRANS_DETAILS", oldTransDetMap.get("AMT_TRANSFER_TRANS_DETAILS"));
                //        if (oldTransDetMap.containsKey("EXH_TRANSFER_TRANS_DETAILS"))
                //            data.put("EXH_TRANSFER_TRANS_DETAILS", oldTransDetMap.get("EXH_TRANSFER_TRANS_DETAILS"));
                //        if (oldTransDetMap.containsKey("OTHERCHRG_TRANSFER_TRANS_DETAILS"))
                //            data.put("OTHERCHRG_TRANSFER_TRANS_DETAILS", oldTransDetMap.get("OTHERCHRG_TRANSFER_TRANS_DETAILS"));
                //        if (oldTransDetMap.containsKey("POSTAGE_TRANSFER_TRANS_DETAILS"))
                //            data.put("POSTAGE_TRANSFER_TRANS_DETAILS", oldTransDetMap.get("POSTAGE_TRANSFER_TRANS_DETAILS"));
                //         if (oldTransDetMap.containsKey("TRANSFER_TRANS_DETAILS"))
                //            data.put("TRANSFER_TRANS_DETAILS", oldTransDetMap.get("TRANSFER_TRANS_DETAILS"));
            }
        
        
        oldTransDetMap = null;
        
        System.out.println("DATA###### : " + data);
        
        //System.out.println("transactionDetailsTO : " + transactionDetailsTO);
        
        
        HashMap proxyResultMap = proxy.execute(data, operationMap);
        setProxyReturnMap(proxyResultMap);
        setResult(actionType);
        if (proxyResultMap != null && proxyResultMap.containsKey("VARIABLE_NO") && proxyResultMap.containsKey("INST_NO1")
        && proxyResultMap.containsKey("INST_NO2")) {
            ClientUtil.showMessageWindow("Variable No. : " + CommonUtil.convertObjToStr(proxyResultMap.get("VARIABLE_NO"))+"\n"
            /* +"Instrument No. : "+CommonUtil.convertObjToStr(proxyResultMap.get("INST_NO1")) + "-" + CommonUtil.convertObjToStr(proxyResultMap.get("INST_NO2"))*/);
        }
        issueDetailsTO = null;
        transactionDetailsTO = null;
        deletedTransactionDetailsTO = null;
        deletedIssueTO = null;
        allowedIssueTO = null;
        allowedTransactionDetailsTO = null;
        updateTOWithDuplicateDetails = null;
        
        actionType = ClientConstants.ACTIONTYPE_CANCEL;
        //        resetForm();
    }
    
    // Setter for Authorization.
    public void setAuthorizeMap(HashMap authorizeMap) {
        _authorizeMap = authorizeMap;
    }
    
    // Getter for Authorization.
    public HashMap getAuthorizeMap() {
        return _authorizeMap;
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
    
    
    
    /* Reset the whole form remittance*/
    public void resetForm() {
        // Reset Issue Details
        setBatchId("");
        setTxtAmt("");
        setCboCity("");
        setCboBranchCode("");
        setCboDraweeBank("");
        setCboCategory("");
        setCboProductId("");
        setTxtFavouring("");
        setCboTransmissionType("");
        setTxtPANGIRNo("");
        setCboCrossing("");
        setTxtRemarks("");
        setTxtOtherCharges("");
        setTxtPostage("");
        setTxtExchange("");
        setTxtExchange1("");
        setTxtTotalAmt("");
        setTxtPayeeAccHead("");
        setTxtPayeeAccNo("");
        setTxtInstrumentNo1("");
        setTxtInstrumentNo2("");
        setTxtTotalInstruments("");
        setTxtVariableNo("");
        setTOStatus("");
        
        // reset Transaction Details
        setTransactionId("");
        setCboProductType("");
        setTxtTotalIssueAmt("");
        
        setTxtPayeeAccHead("");
        setCboPayeeProductId("");
        setCboPayeeProductType("");
        setTotAmt("");
        setAuthorizeStatus("");
        setAuthorizeUser("");
        setAuthorizeDt("");
        setAuthRemarks("");
        
        
        
        //        tblIssueDetails.setDataArrayList(null, issueDetailsTitle);
        tblIssueDetails= new EnhancedTableModel(null, issueDetailsTitle);
        issueSerialNo = 1;
        noOfDeletedIssueTOs = 1;
        tableData = null;
        rowDataForTransDetails = null;
        ttNotifyObservers();
    }
    
    /* Reset the fields present in the issue details */
    public void resetIssueDetails(){
        setTxtAmt("");
        setCboCity("");
        setCboBranchCode("");
        setCboDraweeBank("");
        setCboCategory("");
        setCboProductId("");
        setTxtFavouring("");
        setCboTransmissionType("");
        setTxtPANGIRNo("");
        setCboCrossing("");
        setTxtRemarks("");
        setTxtOtherCharges("");
        setTxtPostage("");
        setTxtExchange("");
        setTxtExchange1("");
        setTxtTotalAmt("");
        setTxtTotalIssueAmt("");
        setTxtPayeeAccHead("");
        setCboPayeeProductType("");
        setCboPayeeProductId("");
        setTxtPayeeAccHead("");
        setTxtPayeeAccNo("");
        setTxtInstrumentNo1("");
        setTxtInstrumentNo2("");
        setTxtVariableNo("");
        setTOStatus("");
        //reset Duplication and Revalidation Charges
        setTxtRevalidationCharge("");
        setTxtRevServTax("");
        setTxtDuplicationCharge("");
        setTxtDupServTax("");
        setLblAccHeadBalDisplay("");
        ttNotifyObservers();
    }
    
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }
    
    /* To populate the screen */
    public boolean populateData(HashMap whereMap) {
        boolean flag = false;
        log.info("Inside populateData()");
        try {
            HashMap mapData = proxy.executeQuery(whereMap, operationMap);
            System.out.println("####mapData"+mapData);
            log.info("mapData:"+mapData);
            flag =  populateOB(mapData);
        } catch( Exception e ) {
            parseException.logException(e,true);
        }
        return flag;
    }
    // To retrive the data from the database and populate it in the OB
    private boolean populateOB(HashMap mapData) throws Exception{
        //        boolean flag = false;
        //        log.info("In PopulateOB method:");
        //        List list = (List) mapData.get("RemittanceIssueTO");
        //        System.out.println("####populateOB"+list);
        //        createObjects();
        //
        //        oldTransDetMap = new HashMap();
        //
        //        if (mapData.containsKey("TransactionDetails"))
        //            oldTransDetMap.put("TransactionDetails", mapData.get("TransactionDetails"));
        //        System.out.println("oldTransDetMap&&&&&&"+oldTransDetMap);
        //
        //        if (!list.isEmpty())
        //            if(setDetails(list)) {
        //                // If it is Authorized
        //                flag =  true;
        //            } else {
        //                // If it is Not Authorized
        //                flag =  false;
        //            }
        //        list = null;
        //        list = (List) mapData.get("TransactionTO");
        //
        //        if (!list.isEmpty()) {
        //            transactionOB.setDetails(list);
        //            if(transactionOB.getLoneActNum()!=null)
        //                loanActMap.put("LINK_BATCH_ID",transactionOB.getLoneActNum()) ;
        //        }
        //        list = null;
        //        return flag;
        boolean flag = false;
        log.info("In PopulateOB method:");
        
        //        System.out.println("####populateOB"+list);
        createObjects();
        
        oldTransDetMap = new HashMap();
        
        if (mapData.containsKey("TransactionDetails"))
            oldTransDetMap.put("TransactionDetails", mapData.get("TransactionDetails"));
        System.out.println("oldTransDetMap&&&&&&"+oldTransDetMap);
        List  list = (List) mapData.get("TransactionTO");
        
        if (!list.isEmpty()) {
            transactionOB.setDetails(list);
            setTranCnt(list.size());
            if(transactionOB.getLoneActNum()!=null)
                loanActMap.put("LINK_BATCH_ID",transactionOB.getLoneActNum()) ;
        }
        list = null;
        
        list = (List) mapData.get("RemittanceIssueTO");
        if (!list.isEmpty())
            if(setDetails(list)) {
                // If it is Authorized
                flag =  true;
            } else {
                // If it is Not Authorized
                flag =  false;
            }
        list = null;
        
        return flag;
    }
    // To find the record size data size retrived from the database
    private int recordSize(List record) {
        return record.size();
    }
    
    // To set the data retrived from the database to  the tables of both details
    private boolean setDetails(List data) throws Exception {
        log.info("setDetails");
        boolean authorized = false;
        final int once = 0;
        int check = 0;
        System.out.println("Data : " + data);
        if (data.get(0) instanceof RemittanceIssueTO){
            for (int i=0, j= recordSize(data);i<j;i++) {
                RemittanceIssueTO objRemittanceIssueTO = (RemittanceIssueTO) data.get(i);
                setOldExg(CommonUtil.convertObjToStr(objRemittanceIssueTO.getExchange()));
                setOldPos(CommonUtil.convertObjToStr(objRemittanceIssueTO.getPostage()));
                setOldSt(CommonUtil.convertObjToStr(objRemittanceIssueTO.getOtherCharges()));
                setStatusBy(objRemittanceIssueTO.getStatusBy());
                setAuthorizeStatus(objRemittanceIssueTO.getAuthorizeStatus());
                if (getActionType()==UPDATE){
                    objRemittanceIssueTO.setCommand(CommonConstants.TOSTATUS_UPDATE);
                } else if (getActionType()==DELETE){
                    objRemittanceIssueTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                }
                allowedIssueTO.put(String.valueOf(issueSerialNo),objRemittanceIssueTO);
                tableData.add(setRowValuesInIssueDetails(issueSerialNo, objRemittanceIssueTO));
                issueSerialNo++;
                if ((check == once) && (objRemittanceIssueTO.getVariableNo().equals(getTxtVariableNo()))) {
                    // check this condition only once
                    check++;
                    //                    setRemittanceIssueOB(objRemittanceIssueTO);
                    if ((objRemittanceIssueTO.getAuthorizeStatus() != null) && (objRemittanceIssueTO.getAuthorizeStatus().equals("AUTHORIZED"))) {
                        authorized = true;
                    }
                }
                setLblDuplicationDate(CommonUtil.convertObjToStr(objRemittanceIssueTO.getDuplicateDt()));
                setLblRevalidationDate(CommonUtil.convertObjToStr(objRemittanceIssueTO.getRevalidateDt()));
                varNum = objRemittanceIssueTO.getVariableNo();
                authStatus = objRemittanceIssueTO.getAuthorizeStatus();
                setAuthRemarks(CommonUtil.convertObjToStr(objRemittanceIssueTO.getAuthorizeRemark()));
                objRemittanceIssueTO.getAuthorizeRemark();
                objRemittanceIssueTO = null;
            }
            
            setTxtTotalIssueAmt(calculateTotalAmount());
            if((!(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE)) &&
            (!(getActionType() == ClientConstants.ACTIONTYPE_REJECT)) &&
            ((getOperationMode() == CommonConstants.REMIT_DUPLICATE) || (getOperationMode() == CommonConstants.REMIT_REVALIDATE))){
                double transTotalAmt = CommonUtil.convertObjToDouble(transactionOB.getLblTotalTransactionAmtVal()).doubleValue();
                setTotAmt(String.valueOf(transTotalAmt));
            }
            //System.out.println("getTxtTotalIssueAmt in line 1249 : " + getTxtTotalIssueAmt());
            tblIssueDetails.setDataArrayList(tableData,issueDetailsTitle);
            ttNotifyObservers();
        } else {
            // moved
        }
        return authorized;
    }
    
    // creates objects if it is not null
    private void createObjects() {
        // Create instance of allowedIssueTO which is used to stored allowed Issue Transfer Objects
        if (allowedIssueTO == null)
            allowedIssueTO = new LinkedHashMap();
        // Create instance of tableData which is used to stored table row values of Issue Details
        if (tableData == null)
            tableData = new ArrayList();
        // Create instance of allowedTransactionDetailsTO which is used to stored allowed Transaction Details Transfer Objects
        if (allowedTransactionDetailsTO == null)
            allowedTransactionDetailsTO = new LinkedHashMap();
        // Create instance of rowDataForTransDetails which is used to stored table row values of Transaction Details
        if (rowDataForTransDetails == null)
            rowDataForTransDetails = new ArrayList();
    }
    
    
    /*   The method saveInIssueDetails is used to save the TO Objects in
     *   the HashMap allowedIssueTO
     */
    
    public int saveInIssueDetails(boolean tableIssueMousePressed,int selectedRow ) {
        log.info("Inside saveInIssueDetails()");
        final int NO_DUPLICATION = 2,YES = 0,NO = 1;
        int RESULT = 2;
        try{
            if (allowedIssueTO == null)
                allowedIssueTO =  new LinkedHashMap();
            if (tableData == null)
                tableData =  new ArrayList();
            RemittanceIssueTO  objRemittanceIssueTO = setRemittanceIssueTO();
            // System.out.println("objRemittanceIssueTO getTotalAmt ob 1064: " + objRemittanceIssueTO.getTotalAmt());
            /* Save button perform updation if the table is pressed */
            if(tableIssueMousePressed){
                /* Not to populate the alert message if the save button is pressed without modifications*/
                if(!compareRemittanceIssueTOs( (RemittanceIssueTO) allowedIssueTO.get( String.valueOf(selectedRow+1) ),objRemittanceIssueTO) ){
                    RESULT = checkingForDuplication(objRemittanceIssueTO);
                }
                /* If there is no duplication update in the HashMap allowedIssueTO */
                if(RESULT == NO_DUPLICATION){
                    tableData.set(selectedRow, setRowValuesInIssueDetails(selectedRow+1, objRemittanceIssueTO));
                    allowedIssueTO.put(String.valueOf(selectedRow+1), objRemittanceIssueTO);
                    /* If there is a duplication if the option chosen is YES delete the old record and add new record*/
                } else if(RESULT == YES){
                    deleteRecord(selectedRow);
                    /* If the option chosen is NO then it is in the EDIT mode */
                }else {
                    RESULT = NO;
                }
                /* Save button performs insertion if the table issue details is not pressed */
            } else {
                /* Checks is there any duplication or not */
                RESULT = checkingForDuplication(objRemittanceIssueTO);
                /* If there is no duplication insert the record in the hashmap allowedIssueTO */
                if(RESULT==NO_DUPLICATION){
                    tableData.add(setRowValuesInIssueDetails(issueSerialNo, objRemittanceIssueTO));
                    allowedIssueTO.put(String.valueOf(issueSerialNo), objRemittanceIssueTO);
                    issueSerialNo++;
                }
            }
            objRemittanceIssueTO = null;
            // System.out.println("Coming Here ... 1317");
            setTxtTotalIssueAmt(calculateTotalAmount());
            
            //System.out.println("Coming Here ... setTxtTotalIssueAmt : " + getTxtTotalIssueAmt());
            setTxtTotalInstruments(CommonUtil.convertObjToStr(String.valueOf( tableData.size()) ) );
            /* Set the records in the table Issue Details */
            tblIssueDetails.setDataArrayList( tableData,issueDetailsTitle);
            ttNotifyObservers();
            if((getOperationMode().equals(CommonConstants.REMIT_DUPLICATE)) || (getOperationMode().equals(CommonConstants.REMIT_REVALIDATE))){
                setTxtTotalIssueAmt(getTotAmt());
            }
        }catch( Exception e ) {
            parseException.logException(e,true);
        }
        return RESULT;
    }
    
    
     /*  This method deleteRecord is used to delete  the particular
        Record chosen from the Table IssueDetails in the UI */
    public int deleteRecord(int selectedRow){
        log.info("Inside deleteRecord()");
        int rowCount = -1;
        try{
            RemittanceIssueTO objRemit = (RemittanceIssueTO) allowedIssueTO.remove( String.valueOf( (selectedRow+1) ) );
            
            if( ( objRemit.getStatus().length()>0 ) && ( objRemit.getStatus() != null ) && !(objRemit.getStatus().equals(""))) {
                if (deletedIssueTO == null)
                    deletedIssueTO = new LinkedHashMap();
                deletedIssueTO.put(String.valueOf(noOfDeletedIssueTOs++), objRemit);
            }
            objRemit = null;
            /* Remove the selected TO Object from the HashMap(issueDetailsMap) */
            for(int i = selectedRow+1,j=allowedIssueTO.size();i<=j;i++){
                allowedIssueTO.put(String.valueOf(i),(RemittanceIssueTO)allowedIssueTO.get(String.valueOf((i+1))));
            }
            allowedIssueTO.get( String.valueOf( (selectedRow+1) ) );
            tableData.remove(selectedRow);
            /* Orders the serial no in the arraylist (tableData) after the removal
               of selected Row in the table */
            for(int i=0,j = tableData.size();i<j;i++){
                ( (ArrayList) tableData.get(i)).set(0,String.valueOf(i+1));
            }
            //System.out.println("Coming to deleetRecord .....");
            setTxtTotalIssueAmt(calculateTotalAmount());
            setTxtTotalInstruments(CommonUtil.convertObjToStr(String.valueOf( tableData.size()) ) );
            tblIssueDetails.setDataArrayList(tableData,issueDetailsTitle);
            issueSerialNo--;
        } catch( Exception e ) {
            parseException.logException(e,true);
        }
        return tblIssueDetails.getRowCount();
    }
    
    /* To set the Attributes values in the  Table IssueDetails */
    private ArrayList setRowValuesInIssueDetails(int serial_No_OR_selectedRow, RemittanceIssueTO objRemittIssueTO) throws Exception {
        ArrayList singleRow = new ArrayList();
        singleRow.add(String.valueOf(serial_No_OR_selectedRow));
        singleRow.add((String) getCbmProductId().getDataForKey(CommonUtil.convertObjToStr(objRemittIssueTO.getProdId())));
        singleRow.add(CommonUtil.convertObjToStr(objRemittIssueTO.getVariableNo()));
        singleRow.add(CommonUtil.convertObjToStr(getRecordTotal(objRemittIssueTO)));
        System.out.println("###@@@getrecord form table"+singleRow);
        return singleRow;
    }
    // To set Column value in the transaction details table
    private ArrayList setTableValuesForTransactionDetails(int serial_No_OR_selectedRow,TransactionTO objRemitTrans) {
        ArrayList singleRow = new ArrayList();
        singleRow.add(String.valueOf(serial_No_OR_selectedRow));
        singleRow.add(CommonUtil.convertObjToStr(objRemitTrans.getTransId()));
        singleRow.add((String) getCbmTransactionType().getDataForKey(CommonUtil.convertObjToStr(objRemitTrans.getTransType())));
        singleRow.add(CommonUtil.convertObjToStr(objRemitTrans.getTransAmt()));
        return singleRow;
        
    }
    
    
    /* Checking the whether the records are duplicated or not */
    private int checkingForDuplication(RemittanceIssueTO objRemittIssueTO){
        log.info("Inside checkingForDuplication()");
        int option = 2,YES = 0,NO = 1;
        try{
            String[] options = {objRemitIssueRB.getString("cDialogYes"),objRemitIssueRB.getString("cDialogNo")};
            /* Checking the entire records present in the HashMap allowedIssueTO for duplication */
            
            for(int i =1,j=allowedIssueTO.size();i<=j;i++){
                if (compareRemittanceIssueTOs((RemittanceIssueTO) allowedIssueTO.get( String.valueOf(i)),objRemittIssueTO)) {
                    option = COptionPane.showOptionDialog(null, objRemitIssueRB.getString("WarningMessage"), CommonConstants.WARNINGTITLE,
                    COptionPane.YES_NO_OPTION, COptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
                    if(option == YES){
                        option = YES;
                        break;
                    } else {
                        option = NO;
                    }
                }
            }
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        return option;
    }
    
    /* To Compare whether the TO Objects are Equal or Not */
    private boolean compareRemittanceIssueTOs(RemittanceIssueTO source, RemittanceIssueTO target) {
        log.info("Inside compareRemittanceIssueTOs()");
        String strSource = getTOasString(source);
        String strTarget = getTOasString(target);
        return strSource.equals(strTarget);
    }
    
    /* Converts TO Object into String to check for duplication */
    private String getTOasString(RemittanceIssueTO objRemittance) {
        log.info("Inside getTOasString()");
        StringBuffer strB = new StringBuffer();
        strB.append((String) getCbmCategory().getDataForKey(CommonUtil.convertObjToStr(objRemittance.getCategory())));
        strB.append((String) getCbmCrossing().getDataForKey(CommonUtil.convertObjToStr(objRemittance.getCrossing())));
        strB.append((String) getCbmProductId().getDataForKey(CommonUtil.convertObjToStr(objRemittance.getProdId())));
        strB.append((String) getCbmTransmissionType().getDataForKey(CommonUtil.convertObjToStr(objRemittance.getRemitType())));
        strB.append((String) getCbmCity().getDataForKey(CommonUtil.convertObjToStr(objRemittance.getCity())));
        strB.append((String) getCbmDraweeBank().getDataForKey(CommonUtil.convertObjToStr(objRemittance.getDraweeBank())));
        strB.append((String) getCbmBranchCode().getDataForKey(CommonUtil.convertObjToStr(objRemittance.getDraweeBranchCode())));
        strB.append(CommonUtil.convertObjToStr(objRemittance.getFavouring()));
        strB.append(CommonUtil.convertObjToStr(objRemittance.getPanGirNo()));
        strB.append(CommonUtil.convertObjToStr(objRemittance.getRemarks()));
        strB.append(CommonUtil.convertObjToStr(objRemittance.getPayeeAcctHead()));
        strB.append(CommonUtil.convertObjToStr(objRemittance.getPayeeAcctNo()));
        strB.append(CommonUtil.convertObjToStr(objRemittance.getAmount()));
        strB.append(CommonUtil.convertObjToStr(objRemittance.getExchange()));
        strB.append(CommonUtil.convertObjToStr(objRemittance.getPostage()));
        strB.append(CommonUtil.convertObjToStr(objRemittance.getOtherCharges()));
        strB.append(CommonUtil.convertObjToStr(objRemittance.getTotalAmt()));
        strB.append(CommonUtil.convertObjToStr(objRemittance.getInstrumentNo1()));
        strB.append(CommonUtil.convertObjToStr(objRemittance.getInstrumentNo2()));
        return CommonUtil.convertObjToStr(strB);
    }
    // To get the total amount and set in the table
    private String getRecordTotal(RemittanceIssueTO objRemitTO){
        log.info("Inside getRecordTotal()");
        String returnStr = new String();
        try{
            double cummulativeTotal = 0;
            double TotAmt = 0;
            cummulativeTotal = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(objRemitTO.getTotalAmt())).doubleValue() ;
            System.out.println("beforecummulativeTotal : " + cummulativeTotal);
            TotAmt = CommonUtil.convertObjToDouble(objRemitTO.getRevalidateCharge()).doubleValue()   +
            CommonUtil.convertObjToDouble(objRemitTO.getDuplicateCharge()).doubleValue() +
            CommonUtil.convertObjToDouble(objRemitTO.getDupServTax()).doubleValue() +
            cummulativeTotal +
            CommonUtil.convertObjToDouble(objRemitTO.getRevServTax()).doubleValue();
            System.out.println("cummulativeTotal : " + cummulativeTotal);
            cummulativeIssueTotal =  CommonUtil.convertObjToStr(new Double(cummulativeTotal) );
            returnStr = cummulativeIssueTotal;
            if((getOperationMode().equals(CommonConstants.REMIT_DUPLICATE)) || (getOperationMode().equals(CommonConstants.REMIT_REVALIDATE))){
                setTotAmt(CommonUtil.convertObjToStr(new Double(TotAmt)));
            }
        }
        catch ( Exception e ) {
            parseException.logException(e,true);
        }
        return returnStr;
    }
    
    
    /*  The method populateSelectedIssueDetails is used to populate the particular
        Record chosen from the Table IssueDetails in the UI */
    public boolean  populateSelectedIssueDetails(String row){
        log.info("Inside populateSelectedIssueDetails()");
        boolean isAuthorized = false;
        try{
            RemittanceIssueTO objRemittanceIssueTO = (RemittanceIssueTO)allowedIssueTO.get(row);
            System.out.println("objRemittanceIssueTO : " + objRemittanceIssueTO);
            setRemittanceIssueOB(objRemittanceIssueTO);
            isAuthorized = isAuthorized(objRemittanceIssueTO);
        }catch( Exception e ) {
            parseException.logException(e,true);
        }
        return isAuthorized;
    }
    
    /**
     * To check whether it is authorized or not
     */
    private boolean isAuthorized(RemittanceIssueTO objRemitTO){
        boolean authorized = false;
        try{
            HashMap where = new HashMap();
            String batchId = "", issueId = "";
            batchId = CommonUtil.convertObjToStr( objRemitTO.getBatchId() );
            issueId = CommonUtil.convertObjToStr( objRemitTO.getIssueId() );
            where.put("BATCH_ID", batchId );
            where.put("ISSUE_ID", issueId );
            List isAuthorized = executeQuery("IsAuthorized", where);
            where = null;
            if ( isAuthorized.size() > 0 && isAuthorized != null) {
                String count = CommonUtil.convertObjToStr(((HashMap) isAuthorized.get(0)).get("COUNT"));
                if ( Integer.parseInt(count) > 0 ) {
                    authorized = true;
                } else {
                    authorized = false;
                }
            }
        }
        catch ( Exception e ) {
            parseException.logException(e,true);
        }
        return authorized;
    }
    /* To set combo box  branch code
     * corresponding to the value selected in drawee bank
     */
    public void populateDraweeBrank(String productId,String city){
        try{
            if(productId.length()>0){
                if(getPayableAt().equals("ISSU_BRANCH")){
                    ArrayList key = new ArrayList();
                    key.add("");
                    key.add(TrueTransactMain.BANK_ID);
                    
                    ArrayList value = new ArrayList();
                    value.add("");
                    value.add(TrueTransactMain.BANK_ID) ;
                    
                    cbmDraweeBank = new ComboBoxModel(key,value);
                    setCbmDraweeBank(cbmDraweeBank);
                }
                else{
                    if(lookUpHash==null)
                        lookUpHash = new HashMap();
                    lookUpHash.put(CommonConstants.MAP_NAME,"getBank");
                    lookUpHash.put(CommonConstants.PARAMFORQUERY, getParametersForBank(productId,city));
                    keyValue = ClientUtil.populateLookupData(lookUpHash);
                    getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
                    cbmDraweeBank = new ComboBoxModel(key,value);
                    setCbmDraweeBank(cbmDraweeBank);
                    makeComboBoxKeyValuesNull();
                }
            }
            productId = null;
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    /**
     * To fill the Category field corresponding to the
     * productId chosen
     */
    public void populateCategory(String productId){
        try{
            if(productId.length()>0){
                if(lookUpHash==null)
                    lookUpHash = new HashMap();
                lookUpHash.put(CommonConstants.MAP_NAME,"getCategory");
                lookUpHash.put(CommonConstants.PARAMFORQUERY, productId);
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
                cbmCategory = new ComboBoxModel(key,value);
                setCbmCategory(cbmCategory);
                makeComboBoxKeyValuesNull();
            }
            productId = null;
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    // To get the parameters to populate the values in the drawee bank combobox
    public HashMap getParametersForBank(String productId,String city)throws Exception {
        final HashMap where = new HashMap();
        where.put("PROD_ID", productId);
        where.put("CITY",city);
        return where;
    }
    /**
     * To fill City with data
     */
    public void populateCity(){
        try{
            if(lookUpHash==null)
                lookUpHash = new HashMap();
            
            getPayableAtBranch();
            
            if(getPayableAt().equals("ISSU_BRANCH")){
                HashMap map = new HashMap();
                map = getBankData(map) ;
                lookUpHash.put(CommonConstants.PARAMFORQUERY, map);
                lookUpHash.put(CommonConstants.MAP_NAME,"getCityForPayable");
            }
            else{
                HashMap map = new HashMap();
                if(!tempProdId.equals(""))
                    map.put("PROD_ID", tempProdId);
                else
                    map.put("PROD_ID", getCbmProductId().getKeyForSelected());
                lookUpHash.put(CommonConstants.PARAMFORQUERY, map);
                lookUpHash.put(CommonConstants.MAP_NAME,"getCityRemittanceIssue");
            }
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
            cbmCity = new ComboBoxModel(key,value);
            setCbmCity(cbmCity);
            makeComboBoxKeyValuesNull();
            tempProdId = "";
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private HashMap getBankData(HashMap map){
        map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        return map ;
    }
    
    private void getPayableAtBranch(){
        String prodId = (String)cbmProductId.getKeyForSelected();
        if(!tempProdId.equals(""))
            prodId = tempProdId;
        String payableAt = "" ;
        HashMap map = new HashMap();
        map.put("PRODUCT_ID", prodId) ;
        List tempList = ClientUtil.executeQuery("getPayableAt", map);
        System.out.println("map = "+map);
        System.out.println("in getPayableAtBranch() tempList.size() : "+tempList.size());
        if(tempList.size() > 0){
            setPayableAt(CommonUtil.convertObjToStr(tempList.get(0))) ;
            System.out.println("tempList.get(0) : "+tempList.get(0));}
        //        tempProdId = "";
    }
    /**
     * populate Branches for corresponding productId and Bank Code
     */
    public void populateBranchCode(String productId, String bankCode){
        try{
            if(getPayableAt().equals("ISSU_BRANCH")){
                ArrayList key = new ArrayList();
                key.add("");
                key.add(TrueTransactMain.BRANCH_ID);
                
                ArrayList value = new ArrayList();
                value.add("");
                value.add(TrueTransactMain.BRANCH_ID) ;
                cbmBranchCode = new ComboBoxModel(key,value);
                setCbmBranchCode(cbmBranchCode);
            }
            else{
                if(productId.length()>0 && bankCode.length()>0){
                    final HashMap where = new HashMap();
                    where.put("PROD_ID",productId);
                    where.put("BANK_CODE",bankCode);
                    if(lookUpHash==null)
                        lookUpHash = new HashMap();
                    lookUpHash.put(CommonConstants.MAP_NAME,"getBranch");
                    lookUpHash.put(CommonConstants.PARAMFORQUERY, where);
                    keyValue = ClientUtil.populateLookupData(lookUpHash);
                    getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
                    cbmBranchCode = new ComboBoxModel(key,value);
                    setCbmBranchCode(cbmBranchCode);
                    makeComboBoxKeyValuesNull();
                }
            }
            productId = null;
            bankCode = null;
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    
    
    /**
     * To populate Exchange field with data for
     * the corresponding productId and category
     */
    public void populateExchange( String productId, String category, String amount, boolean btnNewIsPressed ) {
        
        try{
            if (btnNewIsPressed) {
                setTxtExchange(executeQueryForCharge(productId, category, amount, "EXCHANGE"));
                setTxtPostage(executeQueryForCharge(productId, category, amount, "POSTAL"));
                setTxtOtherCharges(calServiceTax(getTxtExchange(),productId,category,amount, "EXCHANGE", "", ""));
            } else if (allowedIssueTO != null) {
                RemittanceIssueTO objRemitIssue = (RemittanceIssueTO) allowedIssueTO.get(getSelectRow());
                if (objRemitIssue != null) {
                    String src = getAsString(CommonUtil.convertObjToStr(objRemitIssue.getProdId()),CommonUtil.convertObjToStr(objRemitIssue.getCategory()),CommonUtil.convertObjToStr(objRemitIssue.getAmount()));
                    String tar = getAsString(productId,category,amount);
                    if ( src.equals(tar) ) {
                        if ( getTxtExchange().equals("") ) {
                            setTxtExchange(executeQueryForCharge(productId, category, amount, "EXCHANGE"));
                        } else {
                            setTxtExchange(getTxtExchange());
                        }
                        if ( getTxtPostage().equals("") ) {
                            setTxtPostage(executeQueryForCharge(productId, category, amount, "POSTAL"));
                        } else {
                            setTxtPostage(getTxtPostage());
                        }
                    } else {
                        setTxtExchange(executeQueryForCharge(productId, category, amount, "EXCHANGE"));
                        setTxtPostage(executeQueryForCharge(productId, category, amount, "POSTAL"));
                    }
                }
            }
        }catch (Exception e){
            parseException.logException(e,true);
        }
        
    }
    
    public String executeQueryForCharge(String productId, String category, String amount, String chargeType ){
        double inputAmt = CommonUtil.convertObjToDouble(amount).doubleValue() ;
        double calculatedCharge = 0.0;
        HashMap where = new HashMap();
        where.put("PROD_ID", productId);
        where.put("CATEGORY", category);
        where.put("AMOUNT", amount);
        where.put("CHARGE_TYPE", chargeType);
        where.put("PAYABLE", getPayableAt());
        where.put("BANK_CODE", getCbmDraweeBank().getKeyForSelected());
        where.put("BRANCH_CODE", getCbmBranchCode().getKeyForSelected());
        List outList = executeQuery("getExchange", where);
        System.out.println("where:" + where);
        System.out.println("outList:" + outList);
        where = null ;
        if(outList.size() > 0){ //If a charge is configured. else return default zero
            //            for(int i = 0;i < outList.size(); i++){
            HashMap outputMap  = (HashMap)outList.get(0);
            
            if(outputMap != null){
                double toAmt = CommonUtil.convertObjToDouble(outputMap.get("TO_AMT")).doubleValue() ;
                double fixedRate = CommonUtil.convertObjToDouble(outputMap.get("CHARGE")).doubleValue() ;
                double percentage = CommonUtil.convertObjToDouble(outputMap.get("PERCENTAGE")).doubleValue() ;
                
                double forEveryAmt = CommonUtil.convertObjToDouble(outputMap.get("FOR_EVERY_AMT")).doubleValue() ;
                double forEveryRate = CommonUtil.convertObjToDouble(outputMap.get("FOR_EVERY_RATE")).doubleValue() ;
                String forEveryType = CommonUtil.convertObjToStr(outputMap.get("FOR_EVERY_TYPE")) ;
                outputMap = null ;
                if(percentage != 0)
                    calculatedCharge += (inputAmt * percentage) / 100;
                if(fixedRate != 0)
                    calculatedCharge += fixedRate ;
                
                if (inputAmt > toAmt) {
                    if(forEveryAmt != 0){
                        double remainder = inputAmt - toAmt ;
                        if(forEveryType.toUpperCase().equals("AMOUNT")) //Value from Lookup Table
                            calculatedCharge += (remainder / forEveryAmt) * forEveryRate ;
                        else if(forEveryType.toUpperCase().equals("PERCENTAGE"))//Value from Lookup Table
                            calculatedCharge += ((remainder / forEveryAmt) * percentage)/100 ;
                    }
                }
            }
            //        }
        }
        System.out.println("===========>>>>>>>>calculatedCharge : " + calculatedCharge);
        calculatedCharge = (double)getNearest((long)(calculatedCharge *100),100)/100;
        return String.valueOf(calculatedCharge) ;
        
    }
    public String calServiceTax(String exchange,String productId,String category,String amount, String chargeType, String DraweeBank, String branchCode){
        double inputAmt = CommonUtil.convertObjToDouble(amount).doubleValue() ;
        double exchangeAmt = CommonUtil.convertObjToDouble(exchange).doubleValue() ;
        double calculatedCharge = 0.0;
        HashMap where = new HashMap();
        where.put("PROD_ID", productId);
        where.put("CATEGORY", category);
        where.put("AMOUNT", amount);
        where.put("CHARGE_TYPE", chargeType);
        where.put("PAYABLE", getPayableAt());
        if(DraweeBank.equals(""))
            where.put("BANK_CODE", getCbmDraweeBank().getKeyForSelected());
        else
            where.put("BANK_CODE", DraweeBank);
        if(branchCode.equals(""))
            where.put("BRANCH_CODE", getCbmBranchCode().getKeyForSelected());
        else
            where.put("BRANCH_CODE", branchCode);
        List outList = executeQuery("getServiceTax", where);
        System.out.println("where:" + where);
        System.out.println("outList:" + outList);
        where = null ;
        if(outList.size() > 0){ //If a charge is configured. else return default zero
            HashMap outputMap  = (HashMap)outList.get(0);
            
            if(outputMap != null){
                double serviceTax = CommonUtil.convertObjToDouble(outputMap.get("SERVICE_TAX")).doubleValue() ;
                if(serviceTax != 0)
                    calculatedCharge = (exchangeAmt * serviceTax) / 100;
            }
        }
        System.out.println("===========>>>>>>>>calculatedCharge : " + calculatedCharge);
        calculatedCharge = (double)getNearest((long)(calculatedCharge *100),100)/100;
        return String.valueOf(calculatedCharge) ;
    }
    
    public long getNearest(long number,long roundingFactor)  {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor%2) != 0)
            roundingFactorOdd +=1;
        long mod = number%roundingFactor;
        if ((mod < (roundingFactor/2)) || (mod < (roundingFactorOdd/2)))
            return lower(number,roundingFactor);
        else
            return higher(number,roundingFactor);
    }
    public long lower(long number,long roundingFactor) {
        long mod = number%roundingFactor;
        return number-mod;
    }
    
    public long higher(long number,long roundingFactor) {
        long mod = number%roundingFactor;
        if ( mod == 0)
            return number;
        return (number-mod) + roundingFactor ;
    }
    //Below method changed as above
    //    private String executeQueryForExchange( String productId, String category, String amount ) {
    //        String exchangeValue = "";
    //        if( (productId != null && !(productId.equals(""))) && (category != null && !(category.equals(""))) && (amount != null && !(amount.equals(""))) ){
    //            HashMap where = new HashMap();
    //            where.put("PROD_ID", productId);
    //            where.put("CATEGORY", category);
    //            where.put("AMOUNT", amount);
    //            List charge = executeQuery("getExchange", where);
    //            where = null;
    //            if(charge.size()>0 && charge != null){
    //                exchangeValue = CommonUtil.convertObjToStr(((HashMap)charge.get(0)).get("CHARGE"));
    //            } else {
    //                exchangeValue = "";
    //            }
    //        }
    //        return CommonUtil.convertObjToStr(exchangeValue);
    //    }
    /**
     * If PRINT_SERVICES is No in the TABLE REMITTANCE_PRODUCT
     * for the selected productId then disable crossing field
     */
    public String getPrintServices( String productId ) {
        String printService = "Y";
        try {
            HashMap where = new HashMap();
            where.put("PROD_ID", productId);
            List printServices = executeQuery("getPrintServices", getBehavesLikeBasedOnProdId(where));
            where = null;
            if(printServices.size()>0 && printServices != null){
                printService = CommonUtil.convertObjToStr(((HashMap)printServices.get(0)).get("PRINT_SERVICES"));
            } else {
                printService = "Y";
            }
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        return printService;
    }
    /**
     * @return productId, Category, Amount as single String
     */
    private String getAsString( String productId, String category, String amount )throws Exception {
        StringBuffer strb = new StringBuffer();
        strb.append(CommonUtil.convertObjToStr(productId));
        strb.append(CommonUtil.convertObjToStr(category));
        strb.append(CommonUtil.convertObjToStr(amount));
        return CommonUtil.convertObjToStr(strb);
    }
    // Setter the warning Message for Amount
    public void setWarningMessageForAmount(String alertForAmount){
        this.alertForAmount = alertForAmount;
    }
    // Getter the warning Message for Amount
    public String getWarningMessageForAmount(){
        return this.alertForAmount;
    }
    // Setter for warningMessage for branch validation
    public void setWarningMessage(String warningMessage){
        this.warningMessage = warningMessage;
    }
    // Getter for warningMessage for branch validation
    public String getWarningMessage(){
        return this.warningMessage;
    }
    // To show alert message
    public int showAlertWindow(String amtLimit) {
        int option = 1;
        try{
            String[] options = {objRemitIssueRB.getString("cDialogOK")};
            option = COptionPane.showOptionDialog(null,amtLimit, CommonConstants.WARNINGTITLE,
            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
            null, options, options[0]);
        }catch (Exception e){
            parseException.logException(e,true);
        }
        return option;
    }
    
    
    
    
    // Setter for lblDraweeBank
    public void setLblDraweeBank(String lblDraweeBank){
        this.lblDraweeBank = lblDraweeBank;
        setChanged();
    }
    // Getter for lblDraweeBank
    public String getLblDraweeBank(){
        return this.lblDraweeBank;
    }
    // Setter for lblBranchCode
    public void setLblBranchCode(String lblBranchCode){
        this.lblBranchCode = lblBranchCode;
        setChanged();
    }
    // Getter for lblBranchCode
    public String getLblBranchCode(){
        return this.lblBranchCode;
    }
    // Setter for lblVariableNo
    public void setLblVariableNo(String lblVariableNo){
        this.lblVariableNo = lblVariableNo;
        setChanged();
    }
    // Getter for lblVariableNo
    public String getLblVariableNo(){
        return this.lblVariableNo;
    }
    // Setter for lblLapsePeriod
    public void setLblLapsePeriod(String lblLapsePeriod){
        this.lblLapsePeriod = lblLapsePeriod;
        setChanged();
    }
    // Getter for lblLapsePeriod
    public String getLblLapsePeriod(){
        return this.lblLapsePeriod;
    }
    
    // Setter for lblRevalidationDate
    public void setLblRevalidationDate(String lblRevalidationDate){
        this.lblRevalidationDate = lblRevalidationDate;
        setChanged();
    }
    // Getter for lblRevalidationDate
    public String getLblRevalidationDate(){
        return this.lblRevalidationDate;
    }
    // Setter for lblDuplicationDate
    public void setLblDuplicationDate(String lblDuplicationDate){
        this.lblDuplicationDate = lblDuplicationDate;
        setChanged();
    }
    // Getter for lblDuplicationDate
    public String getLblDuplicationDate(){
        return this.lblDuplicationDate;
    }
    
    /**
     * Retrives the IssueId for the corresponding ProductId
     * to display in the lblAccHead
     */
    public void getAccountHeadForProduct(String productId) {
        log.info("Inside getAccountHeadForProduct()");
        try {
            final HashMap remitissueMap = new HashMap();
            remitissueMap.put("PROD_ID",productId);
            final List resultList = executeQuery("getAccountHeadForProductId", remitissueMap);
            if( resultList.size() >0 && resultList != null){
                final HashMap resultMap = (HashMap)resultList.get(0);
                setLblAccountHeadDisplay(CommonUtil.convertObjToStr(resultMap.get("ISSUE_HD")));
                HashMap dataMap = new HashMap();
                dataMap.put("AC_HD_ID", CommonUtil.convertObjToStr(resultMap.get("ISSUE_HD")));
                dataMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                final List lst = executeQuery("getAccountHeadBalForProdId", dataMap);
                if(lst != null && lst.size() > 0){
                    dataMap = (HashMap)lst.get(0);
                    setLblAccHeadBalDisplay(CommonUtil.convertObjToStr(dataMap.get("CUR_BAL")));
                }else
                    setLblAccHeadBalDisplay("");
                
            } else {
                setLblAccountHeadDisplay("");
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    // Sets the selected row in the issue or trans table
    public void setSelectRow(int selectedRow) {
        this.selectRow = String.valueOf(selectedRow+1);
    }
    // Get the selected row in the issue or trans table
    public String getSelectRow(){
        return this.selectRow;
    }
    
    /**
     * To find whether remittance issued get lapsed or not
     */
    public void findLapsedPeriod() {
        int day = 0;
        try {
            String days = "0";
            String period = "0";
            RemittanceIssueTO objRemitIssue = (RemittanceIssueTO) allowedIssueTO.get(getSelectRow());
            if ( objRemitIssue != null && ! ( objRemitIssue.equals("") )  ) {
                HashMap where = new HashMap();
                where.put("VARIABLE_NO",CommonUtil.convertObjToStr(objRemitIssue.getVariableNo()));
                where.put("TODAY_DT", curDate);
                List noOfDays = executeQuery("getNoOfDays", where);
                where = null;
                if(noOfDays.size()>0 && noOfDays != null){
                    days = CommonUtil.convertObjToStr(((HashMap) noOfDays.get(0)).get("NUM_OF_DAYS"));
                }
                where = new HashMap();
                where.put("PROD_ID",CommonUtil.convertObjToStr(objRemitIssue.getProdId()));
                List lapsePeriod = executeQuery("getLapsePeriod", getBehavesLikeBasedOnProdId(where));
                where = null;
                if(lapsePeriod.size()>0 && lapsePeriod != null){
                    period = CommonUtil.convertObjToStr(((HashMap) lapsePeriod.get(0)).get("LAPSE_PERIOD"));
                }
                if( (Integer.parseInt(days))  > (Integer.parseInt(period)) ){
                    convertDaysToYearMonthDay(Integer.parseInt(days)-Integer.parseInt(period));
                } else {
                    convertDaysToYearMonthDay(0);
                }
            }
        } catch (Exception e){
            parseException.logException(e,true);
        }
    }
    /**
     * Executes the query
     */
    private List executeQuery(String mapName, HashMap where){
        List returnList = null;
        try{
            returnList = (List) ClientUtil.executeQuery(mapName, where);
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return returnList;
    }
    /**
     * Converts Days into Years, Months, Days
     */
    public void convertDaysToYearMonthDay(int days){
        int month = 0,year = 0;
        StringBuffer strBuf = new StringBuffer();
        try{
            if(days > 0){
                month = days/30;
                days = days%30;
                if(month >=12){
                    year = month/12;
                    month = month%12;
                }else {
                    year = 0;
                }
            }else{
                days = 0;
                month = 0;
                year = 0;
            }
            strBuf.append(year);
            strBuf.append(" years ");
            strBuf.append(month);
            strBuf.append(" months ");
            strBuf.append(days);
            strBuf.append(" days ");
            setLblLapsePeriod(CommonUtil.convertObjToStr(strBuf));
            strBuf = null;
        }catch (Exception e){
            parseException.logException(e,true);
        }
    }
    // To check the branch according to the instrument type
    public boolean validateBranchId(String branchId, String draweeBank, String draweeBranch,String productId ){
        boolean isAvailable = false;
        try{
            String instrumentType = "";
            // select the bank_code from BANK TABLE
            List bank = executeQuery("getBank_CodeFromBank", null);
            if (bank.size() > 0 && bank != null) {
                String bankName = CommonUtil.convertObjToStr(((HashMap)bank.get(0)).get("BANK_CODE"));
                StringBuffer strb = new StringBuffer();
                strb.append(bankName);
                strb.append(branchId);
                String bankBranch = CommonUtil.convertObjToStr(strb);
                strb = null;
                if (strb == null)
                    strb = new StringBuffer();
                strb.append(draweeBank);
                strb.append(draweeBranch);
                String draweeBankBranch = CommonUtil.convertObjToStr(strb);
                strb = null;
                HashMap where  = new HashMap();
                where.put("PROD_ID", CommonUtil.convertObjToStr(productId));
                instrumentType = (String)(getBehavesLikeBasedOnProdId(where)).get("BEHAVES_LIKE");
                where = null;
                if(instrumentType.length() > 0) {
                    if (instrumentType.equals("DD")) {
                        if (bankBranch.equals(draweeBankBranch)) {
                            setWarningMessage(objRemitIssueRB.getString("DD"));
                            isAvailable = true;
                            showAlertWindow(objRemitIssueRB.getString("DD"));
                            
                        }
                    } else if (instrumentType.equals("PO")) {
                        if (!bankBranch.equals(draweeBankBranch)) {
                            setWarningMessage(objRemitIssueRB.getString("payOrder"));
                            isAvailable = true;
                            showAlertWindow(objRemitIssueRB.getString("payOrder"));
                        }
                    }
                }
            }
        }catch (Exception e){
            parseException.logException(e,true);
        }
        return isAvailable;
    }
    
    /**
     * @return BEHAVES_LIKE from the table REMITTANCE_PRODUCT based on the PROD_ID
     */
    public HashMap getBehavesLikeBasedOnProdId(HashMap where) throws Exception {
        final HashMap behaves_Like = new HashMap();
        String instrumentType = "";
        List behavesLike = executeQuery("getBehavesLikeFromRemitProduct", where);
        if (behavesLike.size() > 0 && behavesLike != null) {
            instrumentType = CommonUtil.convertObjToStr(((HashMap)behavesLike.get(0)).get("BEHAVES_LIKE"));
        }
        if (instrumentType.length() > 0) {
            behaves_Like.put("BEHAVES_LIKE", instrumentType);
        } else {
            behaves_Like.put("BEHAVES_LIKE", instrumentType);
        }
        return behaves_Like;
    }
    
    /**
     * To update the TO Object with Revalidation Values
     * at the time of Revalidation
     */
    public void updateTOsWithRevalidation(String index) {
        //        RemittanceIssueTO updateTOWithRevalidateDetails = (RemittanceIssueTO) allowedIssueTO.get(index);
        //        updateTOWithRevalidateDetails.setRevalidateTrans(CommonUtil.convertObjToStr((String)cbmRevalidateTransType.getKeyForSelected()));
        //        updateTOWithRevalidateDetails.setRevalidateCharge( CommonUtil.convertObjToDouble( getTxtRevalidationCharge() ) );
        //        //        updateTOWithRevalidateDetails.setRevalidateDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getLblRevalidationDate())));
        //        //        updateTOWithRevalidateDetails.setRevalidateExpiryDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtDOExpiring())));
        //        Date LbRevDt = DateUtil.getDateMMDDYYYY(getLblRevalidationDate());
        //        if(LbRevDt != null){
        //            Date lbrevDate = (Date)curDate.clone();
        //            lbrevDate.setDate(LbRevDt.getDate());
        //            lbrevDate.setMonth(LbRevDt.getMonth());
        //            lbrevDate.setYear(LbRevDt.getYear());
        //            updateTOWithRevalidateDetails.setRevalidateDt(lbrevDate);
        //        }else{
        //            updateTOWithRevalidateDetails.setRevalidateDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getLblRevalidationDate())));
        //        }
        //
        //        Date DoExpDt = DateUtil.getDateMMDDYYYY(getTdtDOExpiring());
        //        if(DoExpDt != null){
        //            Date doexpDate = (Date)curDate.clone();
        //            doexpDate.setDate(DoExpDt.getDate());
        //            doexpDate.setMonth(DoExpDt.getMonth());
        //            doexpDate.setYear(DoExpDt.getYear());
        //            updateTOWithRevalidateDetails.setRevalidateExpiryDt(doexpDate);
        //        }else{
        //            updateTOWithRevalidateDetails.setRevalidateExpiryDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtDOExpiring())));
        //        }
        //
        //        updateTOWithRevalidateDetails.setRevalidateRemarks(CommonUtil.convertObjToStr(getTxtRevalidateRemarks()));
        //        allowedIssueTO.put(index, updateTOWithRevalidateDetails);
        if(updateTOWithRevalidateDetails == null)
            updateTOWithRevalidateDetails = new RemittanceIssueTO();
        updateTOWithRevalidateDetails.setRevalidateTrans(CommonUtil.convertObjToStr((String)cbmDuplicateTransType.getKeyForSelected()));
        updateTOWithRevalidateDetails.setRevalidateCharge(CommonUtil.convertObjToDouble(getTxtRevalidationCharge()));
        updateTOWithRevalidateDetails.setRevServTax(CommonUtil.convertObjToDouble(getTxtServiceTax()));
        Date DuDt = DateUtil.getDateMMDDYYYY(getLblRevalidationDate());
        if(DuDt != null){
            Date duDate = (Date)curDate.clone();
            duDate.setDate(DuDt.getDate());
            duDate.setMonth(DuDt.getMonth());
            duDate.setYear(DuDt.getYear());
            updateTOWithRevalidateDetails.setRevalidateDt(duDate);
        }else{
            updateTOWithRevalidateDetails.setRevalidateDt(DateUtil.getDateMMDDYYYY(getLblRevalidationDate()));
        }
        Date DoExpDt = DateUtil.getDateMMDDYYYY(getTdtDOExpiring());
        if(DoExpDt != null){
            Date doexpDate = (Date)curDate.clone();
            doexpDate.setDate(DoExpDt.getDate());
            doexpDate.setMonth(DoExpDt.getMonth());
            doexpDate.setYear(DoExpDt.getYear());
            updateTOWithRevalidateDetails.setRevalidateExpiryDt(doexpDate);
        }else{
            updateTOWithRevalidateDetails.setRevalidateExpiryDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtDOExpiring())));
        }
        updateTOWithRevalidateDetails.setRevalidateRemarks(CommonUtil.convertObjToStr(getTxtRevalidateRemarks()));
        //        dupDetMap.put("updateTOWithDuplicateDetailsTO", updateTOWithDuplicateDetails);
        
    }
    
    /**
     * To update the TO Object with Revalidation Values
     * at the time of Revalidation
     */
    public void updateTOsWithDuplication(String index) {
        //        RemittanceIssueTO updateTOWithDuplicateDetails = (RemittanceIssueTO) allowedIssueTO.get(index);
        //        updateTOWithDuplicateDetails.setDuplicateTrans(CommonUtil.convertObjToStr((String)cbmDuplicateTransType.getKeyForSelected()));
        //        updateTOWithDuplicateDetails.setDuplicateCharge(CommonUtil.convertObjToDouble( getTxtDuplicationCharge() ) );
        ////        updateTOWithDuplicateDetails.setDuplicateDt(DateUtil.getDateMMDDYYYY(getLblDuplicationDate()));
        //        Date DuDt = DateUtil.getDateMMDDYYYY(getLblDuplicationDate());
        //        if(DuDt != null){
        //         Date duDate = (Date)curDate.clone();
        //         duDate.setDate(DuDt.getDate());
        //         duDate.setMonth(DuDt.getMonth());
        //         duDate.setYear(DuDt.getYear());
        //        updateTOWithDuplicateDetails.setDuplicateDt(duDate);
        //        }else{
        //             updateTOWithDuplicateDetails.setDuplicateDt(DateUtil.getDateMMDDYYYY(getLblDuplicationDate()));
        //        }
        //        updateTOWithDuplicateDetails.setDuplicateRemarks(CommonUtil.convertObjToStr(getTxtDuplicateRemarks()));
        //        allowedIssueTO.put(index, updateTOWithDuplicateDetails);
        //        setOperationMode(CommonConstants.REMIT_DUPLICATE);
        //        RemittanceIssueTO updateTOWithDuplicateDetails;
        if(updateTOWithDuplicateDetails == null)
            updateTOWithDuplicateDetails = new RemittanceIssueTO();
        updateTOWithDuplicateDetails.setDuplicateTrans(CommonUtil.convertObjToStr((String)cbmDuplicateTransType.getKeyForSelected()));
        updateTOWithDuplicateDetails.setDuplicateCharge(CommonUtil.convertObjToDouble(getTxtDuplicationCharge()));
        updateTOWithDuplicateDetails.setDupServTax(CommonUtil.convertObjToDouble(getTxtServiceTax()));
        Date DuDt = DateUtil.getDateMMDDYYYY(getLblDuplicationDate());
        if(DuDt != null){
            Date duDate = (Date)curDate.clone();
            duDate.setDate(DuDt.getDate());
            duDate.setMonth(DuDt.getMonth());
            duDate.setYear(DuDt.getYear());
            updateTOWithDuplicateDetails.setDuplicateDt(duDate);
        }else{
            updateTOWithDuplicateDetails.setDuplicateDt(DateUtil.getDateMMDDYYYY(getLblDuplicationDate()));
        }
        updateTOWithDuplicateDetails.setDuplicateRemarks(CommonUtil.convertObjToStr(getTxtDuplicateRemarks()));
        //        dupDetMap.put("updateTOWithDuplicateDetailsTO", updateTOWithDuplicateDetails);
    }
    
    
    public void updateOBWithDuplication() {
        setTxtDuplicationCharge(CommonUtil.convertObjToStr(updateTOWithDuplicateDetails.getDuplicateCharge()));
        setTxtDuplicateRemarks(CommonUtil.convertObjToStr(updateTOWithDuplicateDetails.getDuplicateRemarks()));
        setTxtServiceTax(CommonUtil.convertObjToStr(updateTOWithDuplicateDetails.getDupServTax()));
        transactionOB.setLblTotalTransactionAmtVal("");
    }
    
    public void updateOBWithRevalidate() {
        setTxtRevalidationCharge(CommonUtil.convertObjToStr(updateTOWithRevalidateDetails.getRevalidateCharge()));
        setTxtRevalidateRemarks(CommonUtil.convertObjToStr(updateTOWithRevalidateDetails.getRevalidateRemarks()));
        setTxtServiceTax(CommonUtil.convertObjToStr(updateTOWithRevalidateDetails.getRevServTax()));
        transactionOB.setLblTotalTransactionAmtVal("");
    }
    
    public void updateDuplicateDetails() {
        updateTOWithDuplicateDetails = null;
    }
    
    /**
     * Set the Issue Details in the OB
     */
    private void setRemittanceIssueOB(RemittanceIssueTO objRemittanceIssueTO) throws Exception {
        log.info("Inside setRemittanceIssueOB()");
        setBatchId(CommonUtil.convertObjToStr(objRemittanceIssueTO.getBatchId()));
        setBatchDt(DateUtil.getStringDate(objRemittanceIssueTO.getBatchDt()));
        setIssueId(CommonUtil.convertObjToStr(objRemittanceIssueTO.getIssueId()));
        setCboCrossing((String) getCbmCrossing().getDataForKey(CommonUtil.convertObjToStr(objRemittanceIssueTO.getCrossing())));
        tempProdId = objRemittanceIssueTO.getProdId();
        setCboProductId((String) getCbmProductId().getDataForKey(CommonUtil.convertObjToStr(objRemittanceIssueTO.getProdId())));
        setCboTransmissionType((String) getCbmTransmissionType().getDataForKey(CommonUtil.convertObjToStr(objRemittanceIssueTO.getRemitType())));
        setTxtFavouring(CommonUtil.convertObjToStr(objRemittanceIssueTO.getFavouring()));
        setTxtPANGIRNo(CommonUtil.convertObjToStr(objRemittanceIssueTO.getPanGirNo()));
        setTxtRemarks(CommonUtil.convertObjToStr(objRemittanceIssueTO.getRemarks()));
        setCboPayeeProductType((String) getCbmPayeeProductType().getDataForKey(CommonUtil.convertObjToStr(objRemittanceIssueTO.getPayeeProdType())));
        setCboPayeeProductId((String) getCbmPayeeProductId().getDataForKey(CommonUtil.convertObjToStr(objRemittanceIssueTO.getPayeeProdId())));  //This line added by Rajesh
        setTxtPayeeAccNo(CommonUtil.convertObjToStr(objRemittanceIssueTO.getPayeeAcctNo()));
        setTxtAmt(CommonUtil.convertObjToStr(objRemittanceIssueTO.getAmount()));
        setTxtExchange(CommonUtil.convertObjToStr(objRemittanceIssueTO.getExchange()));
        setTxtExchange1(CommonUtil.convertObjToStr(objRemittanceIssueTO.getExgCal()));
        setTOExchange(CommonUtil.convertObjToStr(objRemittanceIssueTO.getExchange()));
        setTxtPostage(CommonUtil.convertObjToStr(objRemittanceIssueTO.getPostage()));
        setTxtOtherCharges(CommonUtil.convertObjToStr(objRemittanceIssueTO.getOtherCharges()));
        setTxtTotalAmt(CommonUtil.convertObjToStr(objRemittanceIssueTO.getTotalAmt()));
        setTxtTotalInstruments(CommonUtil.convertObjToStr(String.valueOf( tableData.size()) ) );
        setTxtTotalIssueAmt(CommonUtil.convertObjToStr(getTxtTotalIssueAmt()));
        setTxtInstrumentNo1(CommonUtil.convertObjToStr(objRemittanceIssueTO.getInstrumentNo1()));
        setTxtInstrumentNo2(CommonUtil.convertObjToStr(objRemittanceIssueTO.getInstrumentNo2()));
        setTxtVariableNo(CommonUtil.convertObjToStr(objRemittanceIssueTO.getVariableNo()));
        populateCity();
        setCboCity((String) getCbmCity().getDataForKey(CommonUtil.convertObjToStr(objRemittanceIssueTO.getCity())));
        
        populateDraweeBrank( CommonUtil.convertObjToStr(objRemittanceIssueTO.getProdId()), CommonUtil.convertObjToStr(objRemittanceIssueTO.getCity()) );
        setCboDraweeBank((String) getCbmDraweeBank().getDataForKey(CommonUtil.convertObjToStr(objRemittanceIssueTO.getDraweeBank())));
        
        
        populateBranchCode(CommonUtil.convertObjToStr(objRemittanceIssueTO.getProdId()),CommonUtil.convertObjToStr(objRemittanceIssueTO.getDraweeBank()));
        System.out.println("############## prodId + draweeBank : [" + objRemittanceIssueTO.getProdId()+" + "+objRemittanceIssueTO.getDraweeBank() +"]");
        setCboBranchCode((String) getCbmBranchCode().getDataForKey(CommonUtil.convertObjToStr(objRemittanceIssueTO.getDraweeBranchCode())));
        System.out.println("############## cbmBranchCode : [" + objRemittanceIssueTO.getDraweeBranchCode() +"]") ;
        
        populateCategory(CommonUtil.convertObjToStr(objRemittanceIssueTO.getProdId()));
        setCboCategory((String) getCbmCategory().getDataForKey(CommonUtil.convertObjToStr(objRemittanceIssueTO.getCategory())));
        
        setCboRevalidateTransType((String) getCbmTransactionTypeForRevalidation().getDataForKey(CommonUtil.convertObjToStr(objRemittanceIssueTO.getRevalidateTrans())));
        setTxtRevalidationCharge(CommonUtil.convertObjToStr(objRemittanceIssueTO.getRevalidateCharge()));
        setTdtDOExpiring(CommonUtil.convertObjToStr(DateUtil.getStringDate(objRemittanceIssueTO.getRevalidateExpiryDt())));
        setTxtRevalidateRemarks(CommonUtil.convertObjToStr(objRemittanceIssueTO.getRevalidateRemarks()));
        setCboDuplicateTransType((String) getCbmTransactionTypeForDuplication().getDataForKey(CommonUtil.convertObjToStr(objRemittanceIssueTO.getDuplicateTrans())));
        setTxtDuplicationCharge(CommonUtil.convertObjToStr(objRemittanceIssueTO.getDuplicateCharge()));
        setTxtDuplicateRemarks(CommonUtil.convertObjToStr(objRemittanceIssueTO.getDuplicateRemarks()));
        setTxtDupServTax(CommonUtil.convertObjToStr(objRemittanceIssueTO.getDupServTax()));
        setTxtRevServTax(CommonUtil.convertObjToStr(objRemittanceIssueTO.getRevServTax()));
        setRemitForFlag(CommonUtil.convertObjToStr(objRemittanceIssueTO.getRemitForFlag()));
        
        setAuthorizeUser(CommonUtil.convertObjToStr(objRemittanceIssueTO.getAuthorizeUser()));
        setAuthorizeStatus(CommonUtil.convertObjToStr(objRemittanceIssueTO.getAuthorizeStatus()));
        setAuthorizeDt(DateUtil.getStringDate(objRemittanceIssueTO.getAuthorizeDt()));
        setPaidStatus(CommonUtil.convertObjToStr(objRemittanceIssueTO.getPaidStatus()));
        
        setTOStatus(CommonUtil.convertObjToStr(objRemittanceIssueTO.getStatus()));
        
        if(getOperationMode().equals(CommonConstants.REMIT_REVALIDATE))
            setTxtTotalAmt( String.valueOf(
            CommonUtil.convertObjToDouble(getTxtTotalAmt()).doubleValue() +
            CommonUtil.convertObjToDouble(getTxtRevalidationCharge()).doubleValue()));
        
        if(getOperationMode().equals(CommonConstants.REMIT_DUPLICATE))
            setTxtTotalAmt( String.valueOf(
            CommonUtil.convertObjToDouble(getTxtTotalAmt()).doubleValue() +
            CommonUtil.convertObjToDouble(getTxtDuplicationCharge()).doubleValue()));
        
        //        if(objRemittanceIssueTO.getAuthorizeRemark().equals("DEPOSIT_PAY_ORDER"))
        //            depositFlag = true;
        ttNotifyObservers();
        
    }
    
    // to calculate the Total Amount for issue & Trans details
    public String calculateTotalAmount() {
        String returnTotalAmt = "";
        if(tableData != null && tableData.size() > 0) {
            final int column = 3;
            
            try {
                double totalAmount = 0.0;
                ArrayList rowData = new ArrayList();
                for (int i=0,j=tableData.size();i<j;i++) {
                    rowData = (ArrayList) tableData.get(i);
                    totalAmount += Double.parseDouble(CommonUtil.convertObjToStr(rowData.get(column)));
                    rowData = null;
                }
                returnTotalAmt =  CommonUtil.convertObjToStr(new Double(String.valueOf(totalAmount)));
                //                if(getOperationMode().equals(CommonConstants.REMIT_DUPLICATE)){
                //                    double totAmt = 0.0;
                //                    double taxAmt = 0.0;
                //                    taxAmt = CommonUtil.convertObjToDouble(getTxtDupServTax()).doubleValue();
                //                    totAmt = totalAmount + taxAmt;
                //                    returnTotalAmt = CommonUtil.convertObjToStr(new Double(String.valueOf(totAmt)));
                ////                    setTxtTotalAmt(returnTotalAmt);
                ////                    totAmt = ((CommonUtil.convertObjToDouble(totalAmount).doubleValue())
                ////                    +((CommonUtil.convertObjToDouble(getTxtDupServTax()).doubleValue())));
                //                }
            } catch (Exception e) {
                parseException.logException(e,true);
            }
        }
        return returnTotalAmt;
    }
    
    /**
     * To check whether the amount field (in REMIT_ISSUE) lies
     * between the range (min amt and max amt in REMITTANCE_PROD_BRANCH)
     */
    public int checkAmountLimit(String productId, String bankCode, String branchCode, String amount) {
        int returnValue = 1;
        try {
            HashMap where = new HashMap();
            where.put("PROD_ID",productId);
            where.put("BANK_CODE",bankCode);
            where.put("BRANCH_CODE",branchCode);
            where.put("AMOUNT",amount);
            List amtLimit = executeQuery("getMin_Max_Amt", where);
            where = null;
            if(amtLimit.size()>0 && amtLimit != null){
                final  String minimumAmount = CommonUtil.convertObjToStr(((HashMap)amtLimit.get(0)).get("MIN_AMT"));
                final  String maximumAmount = CommonUtil.convertObjToStr(((HashMap)amtLimit.get(0)).get("MAX_AMT"));
                StringBuffer message = new StringBuffer();
                message.append("The Amount Should be Between Minimum Amount ( ");
                message.append(minimumAmount);
                message.append(" ) ");
                message.append("And Maximum Amount ( ");
                message.append(maximumAmount);
                message.append(" ) ");
                setWarningMessageForAmount(CommonUtil.convertObjToStr(message));
                returnValue = showAlertWindow(CommonUtil.convertObjToStr(message));
                if (availabilityOfPanGirNo()) {
                    returnValue = 2;
                }
                message = null;
            } else {
                setWarningMessageForAmount("");
                returnValue = 1;
            }
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        return returnValue;
    }
    /**
     * PAN / GIR NO need or not
     */
    public boolean availabilityOfPanGirNo() {
        boolean needPanGirNo = false;
        int size = allowedTransactionDetailsTO.size();
        for (int i = 1;i<=size;i++) {
            RemittanceIssueTransactionTO transTO = (RemittanceIssueTransactionTO)allowedTransactionDetailsTO.get(String.valueOf(i));
            if (transTO.getTransType().equals("CASH")) {
                needPanGirNo = true;
            } else {
                needPanGirNo = false;
                break;
            }
            transTO = null;
        }
        return needPanGirNo;
    }
    
    /**
     * Getter for property cboProductType.
     * @return Value of property cboProductType.
     */
    public java.lang.String getCboProductType() {
        return cboProductType;
    }
    
    /**
     * Setter for property cboProductType.
     * @param cboProductType New value of property cboProductType.
     */
    public void setCboProductType(java.lang.String cboProductType) {
        this.cboProductType = cboProductType;
    }
    
    /**
     * Getter for property cbmProductType.
     * @return Value of property cbmProductType.
     */
    public ComboBoxModel getCbmProductType() {
        return cbmProductType;
    }
    
    /**
     * Setter for property cbmProductType.
     * @param cbmProductType New value of property cbmProductType.
     */
    public void setCbmProductType(ComboBoxModel cbmProductType) {
        this.cbmProductType = cbmProductType;
    }
    
    /**
     * Getter for property cbmTransProductId.
     * @return Value of property cbmTransProductId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmTransProductId() {
        return cbmTransProductId;
    }
    
    /**
     * Setter for property cbmTransProductId.
     * @param cbmTransProductId New value of property cbmTransProductId.
     */
    public void setCbmTransProductId(com.see.truetransact.clientutil.ComboBoxModel cbmTransProductId) {
        this.cbmTransProductId = cbmTransProductId;
    }
    
    /**
     * Getter for property allowedTransactionDetailsTO.
     * @return Value of property allowedTransactionDetailsTO.
     */
    public java.util.LinkedHashMap getAllowedTransactionDetailsTO() {
        return allowedTransactionDetailsTO;
    }
    
    /**
     * Setter for property allowedTransactionDetailsTO.
     * @param allowedTransactionDetailsTO New value of property allowedTransactionDetailsTO.
     */
    public void setAllowedTransactionDetailsTO(java.util.LinkedHashMap allowedTransactionDetailsTO) {
        // System.out.println("In OB of RemIssue : " + allowedTransactionDetailsTO);
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }
    public void setDupTransactionDetailsTO(java.util.LinkedHashMap allowedTransactionDetailsTO) {
        // System.out.println("In OB of RemIssue : " + allowedTransactionDetailsTO);
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }
    
    /**
     * Setter for property transactionOB.
     * @param transactionOB New value of property transactionOB.
     */
    public void setTransactionOB(TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }
    
    /**
     * Getter for property operationMode.
     * @return Value of property operationMode.
     */
    public java.lang.String getOperationMode() {
        return operationMode;
    }
    
    /**
     * Setter for property operationMode.
     * @param operationMode New value of property operationMode.
     */
    public void setOperationMode(java.lang.String operationMode) {
        this.operationMode = operationMode;
    }
    
    // Setter method for txtTotalIssueAmt
    void setTxtTotalIssueAmt(String txtTotalIssueAmt){
        // System.out.println("txtTotalIssueAmt in line 77 : " + txtTotalIssueAmt);
        this.txtTotalIssueAmt = txtTotalIssueAmt;
        setChanged();
    }
    // Getter method for txtTotalIssueAmt
    String getTxtTotalIssueAmt(){
        return this.txtTotalIssueAmt;
    }
    
    // Setter method for txtTotalInstruments
    void setTxtTotalInstruments(String txtTotalInstruments){
        this.txtTotalInstruments = txtTotalInstruments;
        setChanged();
    }
    // Getter method for txtTotalInstruments
    String getTxtTotalInstruments(){
        return this.txtTotalInstruments;
    }
    
    // Setter method for cboProductId
    void setCboProductId(String cboProductId){
        this.cboProductId = cboProductId;
        setChanged();
    }
    // Getter method for cboProductId
    String getCboProductId(){
        return this.cboProductId;
    }
    
    // Setter method for txtFavouring
    void setTxtFavouring(String txtFavouring){
        this.txtFavouring = txtFavouring;
        setChanged();
    }
    // Getter method for txtFavouring
    String getTxtFavouring(){
        return this.txtFavouring;
    }
    
    // Setter method for cboTransmissionType
    void setCboTransmissionType(String cboTransmissionType){
        this.cboTransmissionType = cboTransmissionType;
        setChanged();
    }
    // Getter method for cboTransmissionType
    String getCboTransmissionType(){
        return this.cboTransmissionType;
    }
    
    // Setter method for txtPANGIRNo
    void setTxtPANGIRNo(String txtPANGIRNo){
        this.txtPANGIRNo = txtPANGIRNo;
        setChanged();
    }
    // Getter method for txtPANGIRNo
    String getTxtPANGIRNo(){
        return this.txtPANGIRNo;
    }
    
    // Setter method for cboCategory
    void setCboCategory(String cboCategory){
        this.cboCategory = cboCategory;
        setChanged();
    }
    // Getter method for cboCategory
    String getCboCategory(){
        return this.cboCategory;
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
    
    // Setter method for cboDraweeBank
    void setCboDraweeBank(String cboDraweeBank){
        this.cboDraweeBank = cboDraweeBank;
        setChanged();
    }
    // Getter method for cboDraweeBank
    String getCboDraweeBank(){
        return this.cboDraweeBank;
    }
    
    // Setter method for cboBranchCode
    void setCboBranchCode(String cboBranchCode){
        this.cboBranchCode = cboBranchCode;
        setChanged();
    }
    // Getter method for cboBranchCode
    String getCboBranchCode(){
        return this.cboBranchCode;
    }
    
    // Setter method for txtAmt
    void setTxtAmt(String txtAmt){
        this.txtAmt = txtAmt;
        setChanged();
    }
    // Getter method for txtAmt
    String getTxtAmt(){
        return this.txtAmt;
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
    
    // Setter method for txtTotalAmt
    public void setTxtTotalAmt(String txtTotalAmt){
        this.txtTotalAmt = txtTotalAmt;
        setChanged();
    }
    // Getter method for txtTotalAmt
    public String getTxtTotalAmt(){
        return this.txtTotalAmt;
    }
    
    // Setter method for txtPayeeAccHead
    void setTxtPayeeAccHead(String txtPayeeAccHead){
        this.txtPayeeAccHead = txtPayeeAccHead;
        setChanged();
    }
    // Getter method for txtPayeeAccHead
    String getTxtPayeeAccHead(){
        return this.txtPayeeAccHead;
    }
    
    // Setter method for txtOtherCharges
    void setTxtOtherCharges(String txtOtherCharges){
        this.txtOtherCharges = txtOtherCharges;
        setChanged();
    }
    // Getter method for txtOtherCharges
    String getTxtOtherCharges(){
        return this.txtOtherCharges;
    }
    
    // Setter method for txtPayeeAccNo
    void setTxtPayeeAccNo(String txtPayeeAccNo){
        this.txtPayeeAccNo = txtPayeeAccNo;
        setChanged();
    }
    // Getter method for txtPayeeAccNo
    String getTxtPayeeAccNo(){
        return this.txtPayeeAccNo;
    }
    
    // Setter method for txtPostage
    void setTxtPostage(String txtPostage){
        this.txtPostage = txtPostage;
        setChanged();
    }
    // Getter method for txtPostage
    String getTxtPostage(){
        return this.txtPostage;
    }
    
    // Setter method for txtInstrumentNo1
    void setTxtInstrumentNo1(String txtInstrumentNo1){
        this.txtInstrumentNo1 = txtInstrumentNo1;
        setChanged();
    }
    // Getter method for txtInstrumentNo1
    String getTxtInstrumentNo1(){
        return this.txtInstrumentNo1;
    }
    
    // Setter method for txtInstrumentNo2
    void setTxtInstrumentNo2(String txtInstrumentNo2){
        this.txtInstrumentNo2 = txtInstrumentNo2;
        setChanged();
    }
    // Getter method for txtInstrumentNo2
    String getTxtInstrumentNo2(){
        return this.txtInstrumentNo2;
    }
    
    // Setter method for txtVariableNo
    void setTxtVariableNo(String txtVariableNo){
        this.txtVariableNo = txtVariableNo;
        setChanged();
    }
    // Getter method for txtVariableNo
    String getTxtVariableNo(){
        return this.txtVariableNo;
    }
    
    // Setter method for txtRemarks
    void setTxtRemarks(String txtRemarks){
        this.txtRemarks = txtRemarks;
        setChanged();
    }
    // Getter method for txtRemarks
    String getTxtRemarks(){
        return this.txtRemarks;
    }
    
    // Setter method for cboCrossing
    void setCboCrossing(String cboCrossing){
        this.cboCrossing = cboCrossing;
        setChanged();
    }
    // Getter method for cboCrossing
    String getCboCrossing(){
        return this.cboCrossing;
    }
    
    /**
     * Getter for property payableAt.
     * @return Value of property payableAt.
     */
    public java.lang.String getPayableAt() {
        return payableAt;
    }
    
    /**
     * Setter for property payableAt.
     * @param payableAt New value of property payableAt.
     */
    public void setPayableAt(java.lang.String payableAt) {
        this.payableAt = payableAt;
    }
    
    /**
     * Getter for property cbmPayeeProductType.
     * @return Value of property cbmPayeeProductType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPayeeProductType() {
        return cbmPayeeProductType;
    }
    
    /**
     * Setter for property cbmPayeeProductType.
     * @param cbmPayeeProductType New value of property cbmPayeeProductType.
     */
    public void setCbmPayeeProductType(com.see.truetransact.clientutil.ComboBoxModel cbmPayeeProductType) {
        this.cbmPayeeProductType = cbmPayeeProductType;
    }
    
    /**
     * Getter for property cbmPayeeProductId.
     * @return Value of property cbmPayeeProductId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPayeeProductId() {
        return cbmPayeeProductId;
    }
    
    
    /**
     * Getter for property cboPayeeProductId.
     * @return Value of property cboPayeeProductId.
     */
    public java.lang.String getCboPayeeProductId() {
        return cboPayeeProductId;
    }
    
    /**
     * Setter for property cboPayeeProductId.
     * @param cboPayeeProductId New value of property cboPayeeProductId.
     */
    public void setCboPayeeProductId(java.lang.String cboPayeeProductId) {
        this.cboPayeeProductId = cboPayeeProductId;
        setChanged();
    }
    
    public void setCbmPayeeProductId(String prodType) {
        try {
            if(prodType.length()>=1) {
                lookUpHash = new HashMap();
                lookUpHash.put(CommonConstants.MAP_NAME,"Cash.getAccProduct" + prodType);
                lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
                cbmPayeeProductId = new ComboBoxModel(key,value);
            }
            else
                cbmPayeeProductId = new ComboBoxModel();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.cbmPayeeProductId = cbmPayeeProductId;
        setChanged();
    }
    
    /**
     * Getter for property cboPayeeProductType.
     * @return Value of property cboPayeeProductType.
     */
    public java.lang.String getCboPayeeProductType() {
        return cboPayeeProductType;
    }
    
    /**
     * Setter for property cboPayeeProductType.
     * @param cboPayeeProductType New value of property cboPayeeProductType.
     */
    public void setCboPayeeProductType(java.lang.String cboPayeeProductType) {
        this.cboPayeeProductType = cboPayeeProductType;
        setChanged();
    }
    
    public void setAccountHead() {
        try {
            System.out.println("here for edit"+(String)cbmPayeeProductId.getKeyForSelected());
            final HashMap accountHeadMap = new HashMap();
            accountHeadMap.put("PROD_ID",(String)cbmPayeeProductId.getKeyForSelected());
            final List resultList = ClientUtil.executeQuery("getAccountHeadProd"+this.getCboPayeeProductType(), accountHeadMap);
            final HashMap resultMap = (HashMap)resultList.get(0);
            setTxtPayeeAccHead(resultMap.get("AC_HEAD").toString());
        }catch(Exception e){
        }
    }
    
    public void destroyAllowedIssue(){
        allowedIssueTO = null;
    }
    
    public void destroyDupIssue(){
        updateTOWithDuplicateDetails = null;
    }
    /**
     * Getter for property procChargeMap.
     * @return Value of property procChargeMap.
     */
    public java.util.HashMap getProcChargeMap() {
        return procChargeMap;
    }
    
    /**
     * Setter for property procChargeMap.
     * @param procChargeMap New value of property procChargeMap.
     */
    public void setProcChargeMap(java.util.HashMap procChargeMap) {
        this.procChargeMap = procChargeMap;
    }
    
    /**
     * Getter for property oldTransDetMap.
     * @return Value of property oldTransDetMap.
     */
    public java.util.HashMap getOldTransDetMap() {
        return oldTransDetMap;
    }
    
    /**
     * Setter for property oldTransDetMap.
     * @param oldTransDetMap New value of property oldTransDetMap.
     */
    public void setOldTransDetMap(java.util.HashMap oldTransDetMap) {
        this.oldTransDetMap = oldTransDetMap;
    }
    
    /**
     * Getter for property lblAccHeadBalDisplay.
     * @return Value of property lblAccHeadBalDisplay.
     */
    public java.lang.String getLblAccHeadBalDisplay() {
        return lblAccHeadBalDisplay;
    }
    
    /**
     * Setter for property lblAccHeadBalDisplay.
     * @param lblAccHeadBalDisplay New value of property lblAccHeadBalDisplay.
     */
    public void setLblAccHeadBalDisplay(java.lang.String lblAccHeadBalDisplay) {
        this.lblAccHeadBalDisplay = lblAccHeadBalDisplay;
    }
    /**
     * Getter for property dup.
     * @return Value of property dup.
     */
    public int getDup() {
        return dup;
    }
    
    /**
     * Setter for property dup.
     * @param dup New value of property dup.
     */
    public void setDup(int dup) {
        this.dup = dup;
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
     * Getter for property txtDupServTax.
     * @return Value of property txtDupServTax.
     */
    public java.lang.String getTxtDupServTax() {
        return txtDupServTax;
    }
    
    /**
     * Setter for property txtDupServTax.
     * @param txtDupServTax New value of property txtDupServTax.
     */
    public void setTxtDupServTax(java.lang.String txtDupServTax) {
        this.txtDupServTax = txtDupServTax;
    }
    
    /**
     * Getter for property txtRevServTax.
     * @return Value of property txtRevServTax.
     */
    public java.lang.String getTxtRevServTax() {
        return txtRevServTax;
    }
    
    /**
     * Setter for property txtRevServTax.
     * @param txtRevServTax New value of property txtRevServTax.
     */
    public void setTxtRevServTax(java.lang.String txtRevServTax) {
        this.txtRevServTax = txtRevServTax;
    }
    
    /**
     * Getter for property totAmt.
     * @return Value of property totAmt.
     */
    public java.lang.String getTotAmt() {
        return totAmt;
    }
    
    /**
     * Setter for property totAmt.
     * @param totAmt New value of property totAmt.
     */
    public void setTotAmt(java.lang.String totAmt) {
        this.totAmt = totAmt;
    }
    
    /**
     * Getter for property dupInEdit.
     * @return Value of property dupInEdit.
     */
    public boolean isDupInEdit() {
        return dupInEdit;
    }
    
    /**
     * Setter for property dupInEdit.
     * @param dupInEdit New value of property dupInEdit.
     */
    public void setDupInEdit(boolean dupInEdit) {
        this.dupInEdit = dupInEdit;
    }
    
    /**
     * Getter for property dupInDelete.
     * @return Value of property dupInDelete.
     */
    public boolean isDupInDelete() {
        return dupInDelete;
    }
    
    /**
     * Setter for property dupInDelete.
     * @param dupInDelete New value of property dupInDelete.
     */
    public void setDupInDelete(boolean dupInDelete) {
        this.dupInDelete = dupInDelete;
    }
    
    /**
     * Getter for property dupTotAmt.
     * @return Value of property dupTotAmt.
     */
    public java.lang.String getDupTotAmt() {
        return dupTotAmt;
    }
    
    /**
     * Setter for property dupTotAmt.
     * @param dupTotAmt New value of property dupTotAmt.
     */
    public void setDupTotAmt(java.lang.String dupTotAmt) {
        this.dupTotAmt = dupTotAmt;
    }
    
    /**
     * Getter for property revInEdit.
     * @return Value of property revInEdit.
     */
    public boolean isRevInEdit() {
        return revInEdit;
    }
    
    /**
     * Setter for property revInEdit.
     * @param revInEdit New value of property revInEdit.
     */
    public void setRevInEdit(boolean revInEdit) {
        this.revInEdit = revInEdit;
    }
    
    /**
     * Getter for property revInDelete.
     * @return Value of property revInDelete.
     */
    public boolean isRevInDelete() {
        return revInDelete;
    }
    
    /**
     * Setter for property revInDelete.
     * @param revInDelete New value of property revInDelete.
     */
    public void setRevInDelete(boolean revInDelete) {
        this.revInDelete = revInDelete;
    }
    
    /**
     * Getter for property revTotAmt.
     * @return Value of property revTotAmt.
     */
    public java.lang.String getRevTotAmt() {
        return revTotAmt;
    }
    
    /**
     * Setter for property revTotAmt.
     * @param revTotAmt New value of property revTotAmt.
     */
    public void setRevTotAmt(java.lang.String revTotAmt) {
        this.revTotAmt = revTotAmt;
    }
    
    /**
     * Getter for property authRemarks.
     * @return Value of property authRemarks.
     */
    public java.lang.String getAuthRemarks() {
        return authRemarks;
    }
    
    /**
     * Setter for property authRemarks.
     * @param authRemarks New value of property authRemarks.
     */
    public void setAuthRemarks(java.lang.String authRemarks) {
        this.authRemarks = authRemarks;
    }
    

    /**
     * Getter for property txtExchange1.
     * @return Value of property txtExchange1.
     */
    public java.lang.String getTxtExchange1() {
        return txtExchange1;
    }
    
    /**
     * Setter for property txtExchange1.
     * @param txtExchange1 New value of property txtExchange1.
     */
    public void setTxtExchange1(java.lang.String txtExchange1) {
        this.txtExchange1 = txtExchange1;
    }
    
    /**
     * Getter for property remitForFlag.
     * @return Value of property remitForFlag.
     */
    public java.lang.String getRemitForFlag() {
        return remitForFlag;
    }
    
    /**
     * Setter for property remitForFlag.
     * @param remitForFlag New value of property remitForFlag.
     */
    public void setRemitForFlag(java.lang.String remitForFlag) {
        this.remitForFlag = remitForFlag;
    }
    
    /**
     * Getter for property oldExg.
     * @return Value of property oldExg.
     */
    public java.lang.String getOldExg() {
        return oldExg;
    }
    
    /**
     * Setter for property oldExg.
     * @param oldExg New value of property oldExg.
     */
    public void setOldExg(java.lang.String oldExg) {
        this.oldExg = oldExg;
    }
    
    /**
     * Getter for property oldPos.
     * @return Value of property oldPos.
     */
    public java.lang.String getOldPos() {
        return oldPos;
    }
    
    /**
     * Setter for property oldPos.
     * @param oldPos New value of property oldPos.
     */
    public void setOldPos(java.lang.String oldPos) {
        this.oldPos = oldPos;
    }
    
    /**
     * Getter for property oldSt.
     * @return Value of property oldSt.
     */
    public java.lang.String getOldSt() {
        return oldSt;
    }
    
    /**
     * Setter for property oldSt.
     * @param oldSt New value of property oldSt.
     */
    public void setOldSt(java.lang.String oldSt) {
        this.oldSt = oldSt;
    }
    
    /**
     * Getter for property tranCnt.
     * @return Value of property tranCnt.
     */
    public int getTranCnt() {
        return tranCnt;
    }
    
    /**
     * Setter for property tranCnt.
     * @param tranCnt New value of property tranCnt.
     */
    public void setTranCnt(int tranCnt) {
        this.tranCnt = tranCnt;
    }
    
}


