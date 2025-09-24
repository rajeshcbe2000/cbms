/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * MDSClosedReciptOB.java
 *
 * Created on January 7, 2004, 5:14 PM
 *
 */

package com.see.truetransact.ui.mdsapplication.mdsclosedreceipt;

/**
 *
 * @author Sathiya
 *
 */

//import java.util.Observable;
import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.ui.common.transaction.TransactionOB;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.transferobject.mdsapplication.mdsclosedreceipt.MDSClosedReciptTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.servicetax.servicetaxdetails.ServiceTaxDetailsTO;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;

public class MDSClosedReciptOB extends CObservable {
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    java.util.ResourceBundle objMDSReceiptEntryRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.mdsapplication.mdsreceiptentry.MDSReceiptEntryRB", ProxyParameters.LANGUAGE);
    
    private HashMap hash;
    private HashMap operationMap;
    private ProxyFactory proxy;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private int actionType;
    private int result;
    private boolean closureFlag = false;
    
    private MDSClosedReciptTO mdsReceiptEntryTO = null;
    private MDSClosedReciptTO splitMDSClosedReceiptEntryTO = null;
    private HashMap MDSClosedTransListMap = new HashMap();
    private HashMap wholeSplitMapp = new HashMap();
    private HashMap oldTransDetMap = new HashMap();
    private TransactionOB transactionOB;
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private LinkedHashMap transactionDetailsTO = null;
    private LinkedHashMap deletedTransactionDetailsTO = null;
    private final String DELETED_TRANS_TOs = "DELETED_TRANS_TOs";
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    private String cbobranch;
    private String txtSchemeName = "";
    private String txtChittalNo = "";
    private String lblMemberNameVal = "";
    private String lblMemberNo = "";
    private String txtDivisionNo = "";
    private String tdtChitStartDt = "";
    private String tdtChitEndDt = "";
    private String tdtInsUptoPaid = "";
    private String txtNoOfInst = "";
    private String txtSubNo = "";
    private String txtCurrentInstNo = "";
    private String txtInstAmt = "";
    private String txtPendingInst = "";
    private String txtPaidInst = "";
    private String txtTotalInstAmt = "";
    private String txtBonusAmtAvail = "";
    private boolean rdoPrizedMember_Yes = false;
    private boolean rdoPrizedMember_No = false;
    private boolean rdoBankPay_Yes = false;
    private boolean rdoBankPay_No = false;
    private String txtNoticeAmt = "";
    private String txtAribitrationAmt = "";
    private String txtNoOfInstToPaay = "";
    private String txtInstPayable = "";
    private String txtPenalAmtPayable = "";
    private String txtBonusAmt = "";
    private String txtForfeitBonus = ""; //2093
    private String bankAdvAmt = "";
    private String txtDiscountAmt = "";
    private String txtInterest = "";
    private String txtNetAmtPaid = "";
    
    private String lblHouseStNo = "";
    private String lblArea = "";
    private String lblCity = "";
    private String lblState = "";
    private String lblpin = "";
    
    double instAmount = 0.0, penalAmount = 0.0, bonusAmount = 0.0, noticeAmount = 0.0,arbitrationAmount = 0.0,serviceTaxAmount = 0.0;;
    private String tdtPaidDate = "";
    private String txtPaidNoOfInst = "";
    private String txtPaidAmt = "";
    private boolean chkThalayal = false;
    private boolean chkMunnal = false;
    private boolean chkMemberChanged = false;
    private String txtEarlierMember = "";
    private String txtEarlierMemberName = "";
    private String txtChangedInst = "";
    private String tdtChangedDate = "";
    private String transId = "";
    private Date currDate = null;
    private EnhancedTableModel tblOtherDetails,tblLoanDetails,tblTransactionDetails;
    private ArrayList tblOtherDetailsList,tblLoanDetailsList,tblTransactionDetailsList;
    
    private HashMap productMap = new HashMap();
    private HashMap authorizeMap = new HashMap();
    private HashMap instMap = new HashMap();
    private HashMap chittalMap = new HashMap();
    
    private String narration = "";
    private String multipleMember = "";
    private HashMap installmentMap = null;
    private ComboBoxModel cbmbranch;
    private String isSplitMDSTransaction = "";
    List splitTransInstList = new ArrayList();
    List bonusAmountList = new ArrayList();
    List penalList = new ArrayList();
    List narrationList = new ArrayList();
    ArrayList splitMDSReceiptEntryLst = new ArrayList();
    private HashMap serviceTax_Map=null;
    private String lblServiceTaxval="";
    private String isWeeklyOrMonthlyScheme = "";
    private String instalmntFreq="";   
    private String isStandingInstructionSet = ""; // Added by nithya for 0003756 on 18.02.2016
    private double txtGlobalPenalAmt=0.0;
    private HashMap returnWaiveMap = null;
    private HashMap installGraceDate = null;

    public double getTxtGlobalPenalAmt() {
        return txtGlobalPenalAmt;
    }

    public void setTxtGlobalPenalAmt(double txtGlobalPenalAmt) {
        this.txtGlobalPenalAmt = txtGlobalPenalAmt;
    }
    
    
    
    public String getLblServiceTaxval() {
        return lblServiceTaxval;
    }

    public void setLblServiceTaxval(String lblServiceTaxval) {
        this.lblServiceTaxval = lblServiceTaxval;
    }

    public HashMap getServiceTax_Map() {
        return serviceTax_Map;
    }

    public void setServiceTax_Map(HashMap serviceTax_Map) {
        this.serviceTax_Map = serviceTax_Map;
    }
    
    
    public String getCbobranch() {
        return cbobranch;
    }

    public void setCbobranch(String cbobranch) {
        this.cbobranch = cbobranch;
    }

    public ComboBoxModel getCbmbranch() {
        return cbmbranch;
    }

    public void setCbmbranch(ComboBoxModel cbmbranch) {
        this.cbmbranch = cbmbranch;
    }

    // Added by nithya for 0003756 on 18.02.2016
    public String getIsStandingInstructionSet() {
        return isStandingInstructionSet;
    }

    public void setIsStandingInstructionSet(String isStandingInstructionSet) {
        this.isStandingInstructionSet = isStandingInstructionSet;
    }
    // End   
    
    private static MDSClosedReciptOB MDSReceiptEntryOB;
    static {
        try {
            MDSReceiptEntryOB = new MDSClosedReciptOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /* Sets the HashMap required to set JNDI,Home and Remote*/
    public MDSClosedReciptOB() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "MDSClosedReciptJNDI");
        operationMap.put(CommonConstants.HOME, "mdsapplication.mdsreceiptentry.MDSReceiptEntryHome");
        operationMap.put(CommonConstants.REMOTE, "mdsapplication.mdsreceiptentry.MDSReceiptEntry");
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        settblOtherDetailsList();
        settblLoanDetailsList();
        settblTransactionDetailsList();
        tblOtherDetails = new EnhancedTableModel(null, tblOtherDetailsList);
        tblLoanDetails = new EnhancedTableModel(null, tblLoanDetailsList);
        tblTransactionDetails = new EnhancedTableModel(null, tblTransactionDetailsList);
        notifyObservers();
        fillDropdown();
        currDate = ClientUtil.getCurrentDate();
    }
    
    
    private void settblOtherDetailsList() throws Exception{
        tblOtherDetailsList = new ArrayList();
        tblOtherDetailsList.add("lblChargeType");
        tblOtherDetailsList.add("lblCategory");
        tblOtherDetailsList.add("lblAmtRangeFrom");
        tblOtherDetailsList.add("lblAmtRangeTo");
        tblOtherDetailsList.add("lblCharges");
    }
    
    private void settblLoanDetailsList() throws Exception{
        tblLoanDetailsList = new ArrayList();
        tblLoanDetailsList.add("lblChargeType");
        tblLoanDetailsList.add("lblCategory");
        tblLoanDetailsList.add("lblAmtRangeFrom");
        tblLoanDetailsList.add("lblAmtRangeTo");
        tblLoanDetailsList.add("lblCharges");
    }
    
    private void settblTransactionDetailsList() throws Exception{
        tblTransactionDetailsList = new ArrayList();
        tblTransactionDetailsList.add("lblChargeType");
        tblTransactionDetailsList.add("lblCategory");
        tblTransactionDetailsList.add("lblAmtRangeFrom");
        tblTransactionDetailsList.add("lblAmtRangeTo");
        tblTransactionDetailsList.add("lblCharges");
    }
    
    public static MDSClosedReciptOB getInstance() {
        return MDSReceiptEntryOB;
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
    public void fillDropdown() throws Exception {
        ArrayList key = new ArrayList();
        ArrayList value = new ArrayList();
        HashMap mapShare = new HashMap();
        List keyValue = null;
        keyValue = ClientUtil.executeQuery("getbranches", mapShare);
        key.add("");
        value.add("");
        if (keyValue != null && keyValue.size() > 0) {
            for (int i = 0; i < keyValue.size(); i++) {
                mapShare = (HashMap) keyValue.get(i);
                key.add(mapShare.get("BRANCH_CODE"));
                value.add(mapShare.get("BRANCH_CODE"));
            }
        }
        cbmbranch = new ComboBoxModel(key, value);
        key = null;
        value = null;
        keyValue.clear();
        keyValue = null;
        mapShare.clear();
        mapShare = null;
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    public void doSave(){
        //        initialise();
        if(getActionType() != ClientConstants.ACTIONTYPE_DELETE ){
            //            updateRemitProdBrchData();
            //            updateRemitProdChrgData();
            //            deleteRemitProdBrchData();
            //            deleteRemitProdChrgData();
            //            insertRemitProdBrchData();
            //            insertRemitProdChrgData();
        }
        doAction();
        //        deinitialise();
    }
    
    /** To perform the appropriate operation */
    public void doAction() {
        try {
            doActionPerform();
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
        }
    }
    
    private MDSClosedReciptTO setMDSReceiptEntryTO(){
        MDSClosedReciptTO mdsReceiptEntryTO = new MDSClosedReciptTO();
        mdsReceiptEntryTO.setSchemeName(getTxtSchemeName());
        mdsReceiptEntryTO.setDivisionNo(CommonUtil.convertObjToDouble(getTxtDivisionNo()));
        mdsReceiptEntryTO.setChittalNo(CommonUtil.convertObjToStr(getTxtChittalNo()));
        mdsReceiptEntryTO.setChitStartDt(CommonUtil.getProperDate((Date)currDate.clone(),DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtChitStartDt()))));
        mdsReceiptEntryTO.setChitEndDt(CommonUtil.getProperDate((Date)currDate.clone(),DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtChitEndDt()))));
        mdsReceiptEntryTO.setNoOfInst(CommonUtil.convertObjToInt(getTxtNoOfInst()));
        mdsReceiptEntryTO.setSubNo(CommonUtil.convertObjToDouble(getTxtSubNo()));
        mdsReceiptEntryTO.setCurrInst(CommonUtil.convertObjToDouble(getTxtCurrentInstNo()));
        mdsReceiptEntryTO.setInstAmt(CommonUtil.convertObjToDouble(getTxtInstAmt()));
        mdsReceiptEntryTO.setPendingInst(CommonUtil.convertObjToDouble(getTxtPendingInst()));
        mdsReceiptEntryTO.setTotalInstDue(CommonUtil.convertObjToDouble(getTxtTotalInstAmt()));
        mdsReceiptEntryTO.setBonusAmtAvail(CommonUtil.convertObjToDouble(getTxtBonusAmtAvail()));
        mdsReceiptEntryTO.setMemberName(CommonUtil.convertObjToStr(getLblMemberNameVal()));
        if(getRdoPrizedMember_Yes() == true){
            mdsReceiptEntryTO.setPrizedMember("Y");
        }else{
            mdsReceiptEntryTO.setPrizedMember("N");
        }
        mdsReceiptEntryTO.setNoticeAmt(CommonUtil.convertObjToDouble(getTxtNoticeAmt()));
        mdsReceiptEntryTO.setArbitrationAmt(CommonUtil.convertObjToDouble(getTxtAribitrationAmt()));
        mdsReceiptEntryTO.setNoOfInstPay(CommonUtil.convertObjToInt(getTxtNoOfInstToPaay()));
        mdsReceiptEntryTO.setInstAmtPayable(CommonUtil.convertObjToDouble(getTxtInstPayable()));
        mdsReceiptEntryTO.setPenalAmtPayable(CommonUtil.convertObjToDouble(getTxtPenalAmtPayable()));
        mdsReceiptEntryTO.setBonusAmtPayable(CommonUtil.convertObjToDouble(getTxtBonusAmt()));
        mdsReceiptEntryTO.setDiscountAmt(CommonUtil.convertObjToDouble(getTxtDiscountAmt()));
        mdsReceiptEntryTO.setMdsInterset(CommonUtil.convertObjToDouble(getTxtInterest()));
        mdsReceiptEntryTO.setNetAmt(CommonUtil.convertObjToDouble(getTxtNetAmtPaid()));
        mdsReceiptEntryTO.setPaidDate(CommonUtil.getProperDate((Date)currDate.clone(),DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtPaidDate()))));
        mdsReceiptEntryTO.setPaidInst(CommonUtil.convertObjToDouble(getTxtPaidInst()));
        mdsReceiptEntryTO.setPaidAmt(CommonUtil.convertObjToDouble(getTxtPaidAmt()));
        if(getChkThalayal() == true){
            mdsReceiptEntryTO.setThalayal("Y");
        }else{
            mdsReceiptEntryTO.setThalayal("N");
        }
        if(getChkMunnal() == true){
            mdsReceiptEntryTO.setMunnal("Y");
        }else{
            mdsReceiptEntryTO.setMunnal("N");
        }
        if(getChkMemberChanged() == true){
            mdsReceiptEntryTO.setMemberChanged("Y");
        } else {
            mdsReceiptEntryTO.setMemberChanged("N");
        }
        //modified by rishad 29/aug/2019  (some case failing to insert data into mds transdetails)
        mdsReceiptEntryTO.setBankPay("N");
//        if (getRdoBankPay_No() == true) {
//            mdsReceiptEntryTO.setBankPay("N");
//        } else {
//            mdsReceiptEntryTO.setBankPay("Y");
//        }
        mdsReceiptEntryTO.setEarlierMemberNo(CommonUtil.convertObjToStr(getTxtEarlierMember()));
        mdsReceiptEntryTO.setEarlierMemberName(CommonUtil.convertObjToStr(getTxtEarlierMemberName()));
        mdsReceiptEntryTO.setChangedInstNo(CommonUtil.convertObjToDouble(getTxtChangedInst()));
        mdsReceiptEntryTO.setChangedDt(CommonUtil.getProperDate((Date)currDate.clone(),DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtChangedDate()))));
        //        mdsReceiptEntryTO.setStatus(ClientConstants.)
        mdsReceiptEntryTO.setStatusBy(TrueTransactMain.USER_ID);
        //        mdsReceiptEntryTO.setStatusDt
        mdsReceiptEntryTO.setBranchCode(getSelectedBranchID());//TrueTransactMain.BRANCH_ID
        mdsReceiptEntryTO.setNarration(getNarration());
        mdsReceiptEntryTO.setInitiatedBranch(ProxyParameters.BRANCH_ID);
        //        if(getTran)
        //        mdsReceiptEntryTO.setTransId
        mdsReceiptEntryTO.setServiceTaxAmt(CommonUtil.convertObjToDouble(getLblServiceTaxval()));
        mdsReceiptEntryTO.setForfeitBonusAmtPayable(CommonUtil.convertObjToDouble(getTxtForfeitBonus())); //16-07-2020
        mdsReceiptEntryTO.setBankAdvanceAmt(CommonUtil.convertObjToDouble(getBankAdvAmt()));
        return mdsReceiptEntryTO;
    }
    
    protected void getCustomerAddressDetails(String value){

    }
    
//    public void schemeDetails(HashMap chittalParamMap){
//        java.util.List lst = ClientUtil.executeQuery("getSelectChitDetails", chittalParamMap);
//        if(lst!=null && lst.size()>0){
//            chittalParamMap = (HashMap)lst.get(0);
//            //            setTxtSchemeName(mdsReceiptEntryTO.getSchemeName());
//            setTxtDivisionNo(CommonUtil.convertObjToStr(chittalParamMap.get("DIVISION_NO")));
//            setTdtChitStartDt(CommonUtil.convertObjToStr(chittalParamMap.get("CHIT_START_DT")));
//            //            setTdtChitEndDt(CommonUtil.convertObjToStr(mdsReceiptEntryTO.getChitEndDt()));
//            setTxtNoOfInst(CommonUtil.convertObjToStr(chittalParamMap.get("")));
//            setTxtCurrentInstNo(CommonUtil.convertObjToStr(chittalParamMap.get("")));
//            setTxtInstAmt(CommonUtil.convertObjToStr(chittalParamMap.get("INST_AMT")));
//            setTxtPendingInst(CommonUtil.convertObjToStr(chittalParamMap.get("")));
//            setTxtTotalInstAmt(CommonUtil.convertObjToStr(chittalParamMap.get("")));
//            
//        }
//    }
    
    protected void getCustomerAddressDetails(){
        HashMap custAddressMap = new HashMap();
        custAddressMap.put("SCHEME_NAME",getTxtSchemeName());
        custAddressMap.put("CHITTAL_NO",getTxtChittalNo());
        custAddressMap.put("SUB_NO",CommonUtil.convertObjToInt(getTxtSubNo()));
        List lst = ClientUtil.executeQuery("getCustomerAddressDetailsinAppln",custAddressMap);
        if(lst!=null && lst.size()>0){
            custAddressMap = (HashMap)lst.get(0);
            setLblHouseStNo(CommonUtil.convertObjToStr(custAddressMap.get("HOUSE_ST")));
            setLblArea(CommonUtil.convertObjToStr(custAddressMap.get("AREA")));
            setLblCity(CommonUtil.convertObjToStr(custAddressMap.get("CITY")));
            setLblState(CommonUtil.convertObjToStr(custAddressMap.get("STATE")));
            setLblpin(CommonUtil.convertObjToStr(custAddressMap.get("PIN")));
        }
    }
    
    public void setClosureDetails(boolean flag){
        closureFlag = flag;
    }
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        final HashMap data = new HashMap();
        if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || getActionType() == ClientConstants.ACTIONTYPE_REJECT){
            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
//            data.put("NO_OF_INST",getInstMap());
            if(TrueTransactMain.SERVICE_TAX_REQ.equals("Y")){
                 data.put("SERVICE_TAX_AUTH", "SERVICE_TAX_AUTH");
            }
            data.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
             if (checkForInterBranchTransExistance())
            data.put("INTER_BRANCH_TRANS", new Boolean(true));
        }
       if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE){
           data.put("WHOLE_SPLIT_MAP", wholeSplitMapp);
       }
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        data.put("COMMAND",getCommand());
        //        if(getActionType() != ClientConstants.ACTIONTYPE_DELETE ){
        mdsReceiptEntryTO = setMDSReceiptEntryTO();
        mdsReceiptEntryTO.setCommand(CommonUtil.convertObjToStr(data.get("COMMAND")));
        data.put("mdsReceiptEntryTO", mdsReceiptEntryTO);
        if (isSplitMDSTransaction != null && isSplitMDSTransaction.equals("Y")) {
            data.put("mdsClosedReceiptEntryTOList", splitMDSReceiptEntryLst);
            data.put("IS_SPLIT_MDS_TRANSACTION", isSplitMDSTransaction);
        }
        if (transactionDetailsTO == null)
            transactionDetailsTO = new LinkedHashMap();
        if (deletedTransactionDetailsTO != null) {
            transactionDetailsTO.put(DELETED_TRANS_TOs,deletedTransactionDetailsTO);
            deletedTransactionDetailsTO = null;
        }
        transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
        allowedTransactionDetailsTO = null;
        
        if (oldTransDetMap != null && oldTransDetMap.size() > 0){
            if(oldTransDetMap.containsKey("NET_TRANSACTION_TRANSFER")){
                data.put("NET_TRANSACTION_TRANSFER",oldTransDetMap.get("NET_TRANSACTION_TRANSFER"));
            }
            if(oldTransDetMap.containsKey("NET_TRANSACTION_CASH")){
                data.put("NET_TRANSACTION_CASH",oldTransDetMap.get("NET_TRANSACTION_CASH"));
            }
            if(oldTransDetMap.containsKey("PENAL_TRANSACTION_CASH")){
                data.put("PENAL_TRANSACTION_CASH",oldTransDetMap.get("PENAL_TRANSACTION_CASH"));
            }
            if(oldTransDetMap.containsKey("ARBITRATION_TRANSACTION_CASH")){
                data.put("ARBITRATION_TRANSACTION_CASH",oldTransDetMap.get("ARBITRATION_TRANSACTION_CASH"));
            }            
            if(oldTransDetMap.containsKey("NOTICE_TRANSACTION_CASH")){
                data.put("NOTICE_TRANSACTION_CASH",oldTransDetMap.get("NOTICE_TRANSACTION_CASH"));
            }
            if(oldTransDetMap.containsKey("BONUS_TRANSACTION_TRANSFER")){
                data.put("BONUS_TRANSACTION_TRANSFER",oldTransDetMap.get("BONUS_TRANSACTION_TRANSFER"));
            }            
            if(oldTransDetMap.containsKey("DISCOUNT_TRANSACTION_TRANSFER")){
                data.put("DISCOUNT_TRANSACTION_TRANSFER",oldTransDetMap.get("DISCOUNT_TRANSACTION_TRANSFER"));
            }            
            data.put("NET_TRANS_ID", getTransId());
        }
        data.put("SCHEME_NAME",getTxtSchemeName());
        data.put("CHITTAL_NO",getTxtChittalNo());
        data.put("TransactionTO",transactionDetailsTO);
        data.put("WAIVE_MAP",getReturnWaiveMap());
        if (getInstallmentMap()!=null && getInstallmentMap().size()>0) {
            data.put("INSTALLMENT_MAP", getInstallmentMap());
        }
        System.out.println("#### closureFlag : "+closureFlag);
        if(closureFlag){
            data.put("MDS_CLOSURE","MDS_CLOSURE");
        }
        System.out.println("#### INSTALLMENT_MAP : "+getInstallmentMap());
        if(getActionType() == ClientConstants.ACTIONTYPE_NEW && getLblServiceTaxval()!=null && CommonUtil.convertObjToDouble(getLblServiceTaxval())>0){
            data.put("serviceTaxDetails", getServiceTax_Map());
            data.put("serviceTaxDetailsTO", setServiceTaxDetails());
        }
        if (getInstallGraceDate() != null && !getInstallGraceDate().isEmpty()) {
            data.put("GRACE_MAP", getInstallGraceDate());
            data.put("PRODUCT_MAP", getProductMap());
        }
        HashMap proxyResultMap = proxy.execute(data, operationMap);
        setProxyReturnMap(proxyResultMap);
        setResult(getActionType());
        displayTransDetail(proxyResultMap);
    }
    
     // The following method added by Rajesh.
    // To check any interbranch transactions happened or not.
    public boolean checkForInterBranchTransExistance() {
        boolean isInterBranchTrans = false;
        if (allowedTransactionDetailsTO!=null && allowedTransactionDetailsTO.size()>0) {
            for (int i=1;i<=allowedTransactionDetailsTO.size();i++) {
                TransactionTO objTxTransferTO = (TransactionTO)allowedTransactionDetailsTO.get(String.valueOf(i));
                if (objTxTransferTO!=null && objTxTransferTO.getTransType().equals("TRANSFER")) {
                    if(objTxTransferTO  !=null && (!objTxTransferTO.getProductType().equals("GL")))
                    if(checkAcNoWithoutProdType(objTxTransferTO.getDebitAcctNo()).equals(objTxTransferTO.getBranchId()))
                     
                        isInterBranchTrans = true;
                        return true;
//                    if (!objTxTransferTO.getBranchId().equals(TrueTransactMain.BRANCH_ID))
//                        isInterBranchTrans = true;
                }/*else if(getTxtChittalNo()!=null && getTxtChittalNo().length()>0){
                    if(checkAcNoWithoutProdType(objTxTransferTO.getDebitAcctNo()).equals(ProxyParameters.BRANCH_ID)){
                        isInterBranchTrans = true;
                        return true;
                    }
                }*/
                objTxTransferTO = null;
            }
        }
        return isInterBranchTrans;
    }
    
      // Checks a/c no. existence without prod_type & prod_id
    public String checkAcNoWithoutProdType(String actNum) {
        
        HashMap mapData=new HashMap();
        boolean isExists = false;
        try{//dont delete chck selectalldao
             if(actNum.indexOf("_")>0)
                actNum =actNum.substring(0,actNum.indexOf("_"));
           
            System.out.println("actNum"+actNum);
            mapData.put("ACT_NUM", actNum);
            List mapDataList = ClientUtil.executeQuery("getActNumFromAllProducts", mapData); 
            System.out.println("#### mapDataList :"+mapDataList);
            if (mapDataList!=null && mapDataList.size()>0) {
                mapData=(HashMap)mapDataList.get(0);
                setSelectedBranchID(CommonUtil.convertObjToStr(mapData.get("BRANCH_ID")));
                return CommonUtil.convertObjToStr(mapData.get("BRANCH_ID"));
              
            
              
            } 
            mapDataList = null;
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return "";
    }
    private void displayTransDetail(HashMap proxyResultMap) {
        System.out.println("@#$@@$@@@$ proxyResultMap : " +proxyResultMap);
        if(getResult() != ClientConstants.ACTIONTYPE_FAILED && proxyResultMap.size()>0) {
            String cashDisplayStr = "Cash Transaction Details...\n";
            String transferDisplayStr = "Transfer Transaction Details...\n";
            String displayStr = "";
            String oldBatchId = "";
            String newBatchId = "";
            String transType = "";
            Object keys[] = proxyResultMap.keySet().toArray();
            int cashCount = 0;
            int transferCount = 0;
            List tempList = null;
            HashMap transMap = null;
            String actNum = "";
            for (int i=0; i<keys.length; i++) {
                tempList = (List)proxyResultMap.get(keys[i]);
                if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH")!=-1) {
                    for (int j=0; j<tempList.size(); j++) {
                        transMap = (HashMap) tempList.get(j);
                        cashDisplayStr += "Trans Id : "+transMap.get("TRANS_ID")+
                        "   Trans Type : "+transMap.get("TRANS_TYPE");
                        actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                        if(actNum != null && !actNum.equals("")){
                            cashDisplayStr +="   Account No : "+transMap.get("ACT_NUM")+
                            "   Amount : "+transMap.get("AMOUNT")+"\n";
                        }else{
                            cashDisplayStr += "   Ac Hd Desc : "+transMap.get("AC_HD_ID")+
                            "   Amount : "+transMap.get("AMOUNT")+"\n";
                        }
                    }
                    cashCount++;
                } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER")!=-1) {
                    for (int j=0; j<tempList.size(); j++) {
                        transMap = (HashMap) tempList.get(j);
                        transferDisplayStr += "Trans Id : "+transMap.get("TRANS_ID")+
                        "   Batch Id : "+transMap.get("BATCH_ID")+
                        "   Trans Type : "+transMap.get("TRANS_TYPE");
                        actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                        if(actNum != null && !actNum.equals("")){
                            transferDisplayStr +="   Account No : "+transMap.get("ACT_NUM")+
                            "   Amount : "+transMap.get("AMOUNT")+"\n";
                        }else{
                            transferDisplayStr += "   Ac Hd Desc : "+transMap.get("AC_HD_ID")+
                            "   Amount : "+transMap.get("AMOUNT")+"\n";
                        }
                    }
                    transferCount++;
                }
            }
            if(cashCount>0){
                displayStr+=cashDisplayStr;
            } 
            if(transferCount>0){
                displayStr+=transferDisplayStr;
            }
            ClientUtil.showMessageWindow(""+displayStr);
            
//            String actNum = CommonUtil.convertObjToStr(observable.getStrACNumber());
//            HashMap transMap = new HashMap();
//            transMap.put("LOAN_NO",observable.getStrACNumber());
//            transMap.put("CURR_DT", curr_dt);
//            List lst = ClientUtil.executeQuery("getTransferTransLoanAuthDetails", transMap);
//            if(lst !=null && lst.size()>0){
//                displayStr += "Transfer Transaction Details...\n";
//                for(int i = 0;i<lst.size();i++){
//                    transMap = (HashMap)lst.get(i);
//                    displayStr += "Trans Id : "+transMap.get("TRANS_ID")+
//                    "   Batch Id : "+transMap.get("BATCH_ID")+
//                    "   Trans Type : "+transMap.get("TRANS_TYPE");
//                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
//                    if(actNum != null && !actNum.equals("")){
//                        displayStr +="   Account No : "+transMap.get("ACT_NUM")+
//                        "   Deposit Amount : "+transMap.get("AMOUNT")+"\n";
//                    }else{
//                        displayStr += "   Account Head : "+transMap.get("AC_HD_ID")+
//                        "   Interest Amount : "+transMap.get("AMOUNT")+"\n";
//                    }
//                    System.out.println("#### :" +transMap);
//                    oldBatchId = newBatchId;
//                }
//            }
//            actNum = CommonUtil.convertObjToStr(observable.getStrACNumber());
//            transMap = new HashMap();
//            transMap.put("LOAN_NO",actNum);
//            transMap.put("CURR_DT", curr_dt);
//            lst = ClientUtil.executeQuery("getCashTransLoanAuthDetails", transMap);
//            if(lst !=null && lst.size()>0){
//                displayStr += "Cash Transaction Details...\n";
//                for(int i = 0;i<lst.size();i++){
//                    transMap = (HashMap)lst.get(i);
//                    displayStr +="Trans Id : "+transMap.get("TRANS_ID")+
//                    "   Trans Type : "+transMap.get("TRANS_TYPE");
//                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
//                    if(actNum != null && !actNum.equals("")){
//                        displayStr +="   Account No :  "+transMap.get("ACT_NUM")+
//                        "   Deposit Amount :  "+transMap.get("AMOUNT")+"\n";
//                    }else{
//                        displayStr +="   Account Head :  "+transMap.get("AC_HD_ID")+
//                        "   Interest Amount :  "+transMap.get("AMOUNT")+"\n";
//                    }
//                }
//            }
//            if(!displayStr.equals("")){
//                ClientUtil.showMessageWindow(""+displayStr);
//            }
        }
    }
    
    public void setProductMapDetails(){
        HashMap schemeMap = new HashMap();
        schemeMap.put("SCHEME_NAME",getTxtSchemeName());
        List lst = ClientUtil.executeQuery("getSelectSchemeAcctHead",schemeMap);
        if(lst!=null && lst.size()>0){
            productMap = (HashMap)lst.get(0);
        }
    }
    
    public void setReceiptDetails(HashMap map){
//        HashMap chittalMap = new HashMap();
        long count =0;
        chittalMap.put("CHITTAL_NO",getTxtChittalNo());
        chittalMap.put("SUB_NO",CommonUtil.convertObjToInt(getTxtSubNo()));
        List lst = ClientUtil.executeQuery("getSelctApplnReceiptDetails", chittalMap);
        if(lst!=null && lst.size()>0){
            chittalMap = (HashMap)lst.get(0);
            setTxtDivisionNo(CommonUtil.convertObjToStr(chittalMap.get("DIVISION_NO")));
            setTxtChittalNo(CommonUtil.convertObjToStr(chittalMap.get("CHITTAL_NO")));
            setTdtChitStartDt(CommonUtil.convertObjToStr(chittalMap.get("CHIT_START_DT")));
            setTxtNoOfInst(CommonUtil.convertObjToStr(chittalMap.get("NO_OF_INSTALLMENTS")));
            setTxtCurrentInstNo(CommonUtil.convertObjToStr(chittalMap.get("")));
            setTxtInstAmt(CommonUtil.convertObjToStr(chittalMap.get("INST_AMT")));
            setLblMemberNameVal(CommonUtil.convertObjToStr(chittalMap.get("MEMBER_NAME")));
            setLblMemberNo(CommonUtil.convertObjToStr(chittalMap.get("MEMBER_NO")));
            setTxtPendingInst(CommonUtil.convertObjToStr(chittalMap.get("")));
            setTxtTotalInstAmt(CommonUtil.convertObjToStr(chittalMap.get("")));
            Date endDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalMap.get("CHIT_END_DT")));
            setInstalmntFreq(CommonUtil.convertObjToStr(chittalMap.get("INSTALLMENT_FREQUENCY")));
            // Added by nithya for 0003756 on 18.02.2016
            if(chittalMap.containsKey("STANDING_INSTN")){
                setIsStandingInstructionSet(CommonUtil.convertObjToStr(chittalMap.get("STANDING_INSTN")));
            }   
            // End            
            if(chittalMap.get("MUNNAL") != null && CommonUtil.convertObjToStr(chittalMap.get("MUNNAL")).equals("Y")){
                setChkMunnal(true);
            }else if(chittalMap.get("THALAYAL") != null && CommonUtil.convertObjToStr(chittalMap.get("THALAYAL")).equals("Y")){
                setChkThalayal(true);
            }
            
//            int instDay = 0; //Added by Rajesh
            
            HashMap instMap = new HashMap();
            instMap.put("CHITTAL_NO",getTxtChittalNo());
            instMap.put("SUB_NO",CommonUtil.convertObjToInt(getTxtSubNo()));
            List instLst = ClientUtil.executeQuery("getNoOfInstalmentsPaid", instMap);
            if(instLst!=null && instLst.size()>0){
                instMap = (HashMap)instLst.get(0);
                //count = CommonUtil.convertObjToLong(instMap.get("NO_OF_INST"));
                count = CommonUtil.convertObjToInt(instMap.get("NO_OF_INST"));
                setTxtPaidNoOfInst(String.valueOf(count));
                // Added by Rajesh
//                setTdtInsUptoPaid(CommonUtil.convertObjToStr(instMap.get("TRANS_DT")));
            }else {
                setTxtPaidNoOfInst(String.valueOf("0"));
                // Following block added by Rajesh
//                Date startDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalMap.get("SCHEME_START_DT")));
//                instDay = CommonUtil.convertObjToInt(chittalMap.get("INSTALLMENT_DAY"));
//                startDate.setDate(instDay);
//                int stMonth = startDate.getMonth();
//                startDate.setMonth(stMonth+(int)count-1);
//                setTdtInsUptoPaid(CommonUtil.convertObjToStr(startDate));
            }
            HashMap prizedMap = new HashMap();
            prizedMap.put("SCHEME_NAME",getTxtSchemeName());
            prizedMap.put("DIVISION_NO",chittalMap.get("DIVISION_NO"));
            lst = ClientUtil.executeQuery("getSelectPrizedDetailsEntryRecords", prizedMap);
            if(lst!=null && lst.size()>0){
                prizedMap = (HashMap)lst.get(0);
                setTxtBonusAmtAvail(CommonUtil.convertObjToStr(prizedMap.get("PRIZED_AMOUNT")));
                setTdtChitEndDt(CommonUtil.convertObjToStr(prizedMap.get("NEXT_INSTALLMENT_DATE")));
            }
            prizedMap = new HashMap();
            prizedMap.put("SCHEME_NAME",getTxtSchemeName());
            prizedMap.put("DIVISION_NO",chittalMap.get("DIVISION_NO"));
            prizedMap.put("CHITTAL_NO",getTxtChittalNo());
            prizedMap.put("SUB_NO",CommonUtil.convertObjToInt(getTxtSubNo()));
            setProductMapDetails();
            System.out.println("productMap**"+productMap);
            if (productMap.containsKey("FROM_AUCTION_ENTRY") && productMap.get("FROM_AUCTION_ENTRY") != null && productMap.get("FROM_AUCTION_ENTRY").equals("Y")) {
                lst = ClientUtil.executeQuery("getSelectPrizedDetailsEntryRecords", prizedMap);
                System.out.println("lst in FROM_AUCTION_ENTRY" + lst);
                if (lst != null && lst.size() > 0) {
                    prizedMap = (HashMap) lst.get(0);
                    setRdoPrizedMember_Yes(true);
                } else {
                    setRdoPrizedMember_No(true);
                }
            } else if (productMap.containsKey("AFTER_CASH_PAYMENT") && productMap.get("AFTER_CASH_PAYMENT") != null && productMap.get("AFTER_CASH_PAYMENT").equals("Y")) {
                lst = ClientUtil.executeQuery("getSelectPrizedDetailsAfterCashPayment", prizedMap);
                System.out.println("lst in AFTER_CASH_PAYMENT" + lst);
                if (lst != null && lst.size() > 0) {
                    prizedMap = (HashMap) lst.get(0);
                    System.out.println("SIIIIII" + prizedMap.size());
                    if (prizedMap.size() >= 1) {
                        setRdoPrizedMember_Yes(true);
                    } else {
                        setRdoPrizedMember_No(true);
                    }
                } else {
                    setRdoPrizedMember_No(true);
                }
            } else {
                lst = ClientUtil.executeQuery("getSelectPrizedDetailsEntryRecords", prizedMap);
                System.out.println("lst in ELSE" + lst);
                if (lst != null && lst.size() > 0) {
                    prizedMap = (HashMap) lst.get(0);
                    if (prizedMap.get("DRAW") != null && !prizedMap.get("DRAW").equals("") && prizedMap.get("DRAW").equals("Y")) {
                        setRdoPrizedMember_Yes(true);
                    }
                    if (prizedMap.get("AUCTION") != null && !prizedMap.get("AUCTION").equals("") && prizedMap.get("AUCTION").equals("Y")) {
                        setRdoPrizedMember_Yes(true);
                    }
                } else {
                    setRdoPrizedMember_No(true);
                }
            }
            if(productMap.containsKey("INSTALLMENT_FREQUENCY") && productMap.get("INSTALLMENT_FREQUENCY") != null){
                if(CommonUtil.convertObjToInt(productMap.get("INSTALLMENT_FREQUENCY")) == 7){
                   isWeeklyOrMonthlyScheme = "W";   
                }else{
                   isWeeklyOrMonthlyScheme = "M";  
                }
            }
            int instDay = 0;
            HashMap whereMap = new HashMap();
            whereMap.put("SCHEME_NAME",getTxtSchemeName());
            whereMap.put("DIVISION_NO", CommonUtil.convertObjToInt(chittalMap.get("DIVISION_NO")));
	    whereMap.put("INSTALLMENT_NO",CommonUtil.convertObjToInt(String.valueOf(count)));
            if(isWeeklyOrMonthlyScheme != null && !isWeeklyOrMonthlyScheme.equals("")  && isWeeklyOrMonthlyScheme.equals("W")){
            	lst = ClientUtil.executeQuery("getSelectWeeklyInstUptoPaid", whereMap);    
            }else{
            	lst = ClientUtil.executeQuery("getSelectInstUptoPaid", whereMap);
            }
            if(lst!=null && lst.size()>0){
                whereMap = (HashMap)lst.get(0);
                setTdtInsUptoPaid(CommonUtil.convertObjToStr(whereMap.get("NEXT_INSTALLMENT_DATE")));
            }else{
                Date startDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalMap.get("SCHEME_START_DT")));
                instDay = CommonUtil.convertObjToInt(chittalMap.get("INSTALLMENT_DAY"));
                startDate.setDate(instDay);
                int stMonth = startDate.getMonth();
                startDate.setMonth(stMonth+(int)count-1);
                setTdtInsUptoPaid(CommonUtil.convertObjToStr(startDate));
            }
            
            Date InstalDt = null;
            HashMap insDateMap = new HashMap();
            insDateMap.put("DIVISION_NO",CommonUtil.convertObjToInt(getTxtDivisionNo()));
            insDateMap.put("SCHEME_NAME",getTxtSchemeName());
            insDateMap.put("CURR_DATE",currDate.clone());
            List insDateLst = ClientUtil.executeQuery("getMDSNextInsDate", insDateMap);
            if(insDateLst!=null && insDateLst.size()>0){
                insDateMap = (HashMap)insDateLst.get(0);
                // The following block changed by Rajesh
//                InstalDt = (Date) insDateMap.get("INST_DT");
//                int instalDay = InstalDt.getDate();
//                instDay = instalDay;
                if (insDateMap.get("INST_DT")!=null) {
                    InstalDt = (Date) insDateMap.get("INST_DT");
                    int instalDay = InstalDt.getDate();
                    instDay = instalDay;
                } else {
                    instDay = CommonUtil.convertObjToInt(chittalMap.get("INSTALLMENT_DAY"));
                }
                //End
            }else{
                instDay = CommonUtil.convertObjToInt(chittalMap.get("INSTALLMENT_DAY"));
            }
            System.out.println("@@#@#@#@#@#@# no of inst pay 1 ..."+getTxtNoOfInstToPaay());
            Date startDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalMap.get("SCHEME_START_DT")));
            int stYear = startDate.getYear()+1900;
            int currYear = currDate.getYear()+1900;
            int stMonth = startDate.getMonth();
            int currMonth = currDate.getMonth();
            int value = 0;
            int pending = 0;

            
//            long count = CommonUtil.convertObjToLong(chittalMap.get("INST_COUNT"))+1;
            int totInst = CommonUtil.convertObjToInt(chittalMap.get("NO_OF_INSTALLMENTS"));
            if(stYear == currYear && stMonth == currMonth){
                pending = 0;
                setTxtPendingInst(String.valueOf("0"));
                setTxtNoOfInstToPaay(String.valueOf("1"));
                value = currMonth - stMonth+1;
                if(totInst == value){
                    setTxtCurrentInstNo(String.valueOf(value));
                }else if(instDay<currDate.getDate()){
                    setTxtCurrentInstNo(String.valueOf(value+1));
                }else{
                    setTxtCurrentInstNo(String.valueOf(value));
                }
                System.out.println("@@#@#@#@#@#@# no of inst pay 2 ..."+getTxtNoOfInstToPaay());
            }else if(stYear == currYear && stMonth != currMonth){
                pending = 0;
                value = currMonth - stMonth+1;
                int diffMonth = currMonth - stMonth;
                int pendingVal = diffMonth - (int)count;
                int noOfInsPay = 0;
                if(instDay<currDate.getDate()){
                    pending = pendingVal+1;
                }
                else{
                    pending = pendingVal;
                }
                if(pending>0)
                    setTxtPendingInst(String.valueOf(pending));
                else
                    setTxtPendingInst(String.valueOf(0));
                noOfInsPay = diffMonth-(int)count+1;
                if(noOfInsPay>0){
                    setTxtNoOfInstToPaay(String.valueOf(noOfInsPay));
                }else{
                    setTxtNoOfInstToPaay("0");
                }
                System.out.println("@@#@#@#@#@#@# no of inst pay 2 ..."+getTxtNoOfInstToPaay());
                if(totInst == value){
                    setTxtCurrentInstNo(String.valueOf(value));
                }else if(instDay<currDate.getDate()){
                    // Changed by Rajesh
                    setTxtCurrentInstNo(String.valueOf(value));
//                    setTxtCurrentInstNo(String.valueOf(value+1));
                }else{
                    setTxtCurrentInstNo(String.valueOf(value));
                }
                int curInsNo = CommonUtil.convertObjToInt(getTxtCurrentInstNo());
                //                int countChk = curInsNo-(int)count;
                //                if(countChk>=2){
                    Date installDate = (Date) currDate.clone();
                    instDay = CommonUtil.convertObjToInt(chittalMap.get("INSTALLMENT_DAY"));
                    installDate.setMonth(stMonth+curInsNo-1);
                    installDate.setDate(instDay);
                    setTdtChitEndDt(CommonUtil.convertObjToStr(installDate));
                //                }
            }else{
                int year = currYear - stYear;
                value = (year * 12) + currMonth - stMonth;
                int pendingVal = value - (int)count;
                int noOfInsPay = 0;
                if(instDay<currDate.getDate()){
                    pending = pendingVal+1;
                }
                else{
                    pending = pendingVal;
                }
                if(pending>0)
                    setTxtPendingInst(String.valueOf(pending));
                else
                    setTxtPendingInst(String.valueOf(0));
                System.out.println("@#@#@#@#@#@#  instDay : "+instDay);
                System.out.println("@#@#@#@#@#@#  year : "+year);
                System.out.println("@#@#@#@#@#@#  val : "+value);
                System.out.println("@#@#@#@#@#@#  cnt : "+count);
                System.out.println("@#@#@#@#@#@#  no inst : "+noOfInsPay);
                noOfInsPay = value-(int)count+1;
                System.out.println("@#@#@#@#@#@#  no inst 1: "+noOfInsPay);
                if(noOfInsPay>0){
                    setTxtNoOfInstToPaay(String.valueOf(noOfInsPay));
                }else{
                    setTxtNoOfInstToPaay("0");
                }
                if(totInst == value){
                    setTxtCurrentInstNo(String.valueOf(value));
                }else if(instDay<currDate.getDate()){
                    setTxtCurrentInstNo(String.valueOf(value+1));
                }else{
                    setTxtCurrentInstNo(String.valueOf(value));
                }
                int curInsNo = CommonUtil.convertObjToInt(getTxtCurrentInstNo());
                int countChk = curInsNo-(int)count;
                Date installDate = (Date)startDate.clone();
                instDay = CommonUtil.convertObjToInt(chittalMap.get("INSTALLMENT_DAY"));
                installDate=DateUtil.addDays(installDate,curInsNo*30);
                installDate.setDate(instDay);
                setTdtChitEndDt(CommonUtil.convertObjToStr(installDate));
                System.out.println("@@#@#@#@#@#@# no of inst pay 3 ..."+getTxtNoOfInstToPaay());
            }
            double instAmt = CommonUtil.convertObjToDouble(chittalMap.get("INST_AMT")).doubleValue();
            HashMap MdsDetailsMap = new HashMap();
            MdsDetailsMap.put("CHITTAL_NO",getTxtChittalNo());
            MdsDetailsMap.put("SCHEME_NAME",getTxtSchemeName());
            MdsDetailsMap.put("SUB_NO",CommonUtil.convertObjToInt(getTxtSubNo()));
            lst = ClientUtil.executeQuery("getSelectRecordDetails", MdsDetailsMap);
            if(lst!=null && lst.size()>0){            
                MdsDetailsMap = (HashMap)lst.get(0);
                setTdtPaidDate(CommonUtil.convertObjToStr(MdsDetailsMap.get("PAYMENT_DATE")));
                setTxtPaidInst(CommonUtil.convertObjToStr(MdsDetailsMap.get("INSTALLMENT_NO")));
                setTxtPaidAmt(CommonUtil.convertObjToStr(MdsDetailsMap.get("PRIZED_AMOUNT")));
            }
            MdsDetailsMap = new HashMap();
            MdsDetailsMap.put("CHITTAL_NO",getTxtChittalNo());
            MdsDetailsMap.put("SCHEME_NAME",getTxtSchemeName());
            lst = ClientUtil.executeQuery("getSelectChangedRecordDetails", MdsDetailsMap);
            if(lst!=null && lst.size()>0){            
                MdsDetailsMap = (HashMap)lst.get(0);
                if(MdsDetailsMap.get("THALAYAL")!=null && MdsDetailsMap.get("THALAYAL").equals("Y")){
                    setChkThalayal(true);                    
                }else{
                    setChkThalayal(false);                    
                }
                if(MdsDetailsMap.get("MUNNAL")!=null && MdsDetailsMap.get("MUNNAL").equals("Y")){
                    setChkMunnal(true);
                }else{
                    setChkMunnal(false);
                }                
                setChkMemberChanged(true);
                setTxtEarlierMember(CommonUtil.convertObjToStr(MdsDetailsMap.get("OLD_MEMBER_NO")));
                setTxtEarlierMemberName(CommonUtil.convertObjToStr(MdsDetailsMap.get("OLD_MEMBER_NAME")));
                setTxtChangedInst(CommonUtil.convertObjToStr(MdsDetailsMap.get("INSTALLMENT_NO")));
                setTdtChangedDate(CommonUtil.convertObjToStr(MdsDetailsMap.get("CHANGE_EFFECTIVE_DATE")));
            }else{
                setChkMunnal(false);
                setChkThalayal(false); 
                setChkMemberChanged(false);
                setTxtEarlierMember("");
                setTxtEarlierMemberName("");
                setTxtChangedInst("");
                setTdtChangedDate("");
            }
            insDateMap = new HashMap();
            insDateMap.put("DIVISION_NO",CommonUtil.convertObjToInt(getTxtDivisionNo()));
            insDateMap.put("SCHEME_NAME",getTxtSchemeName());
            insDateMap.put("CURR_DATE",currDate.clone());
            insDateMap.put("ADD_MONTHS", CommonUtil.convertObjToInt("-1"));
            if(isWeeklyOrMonthlyScheme != null && !isWeeklyOrMonthlyScheme.equals("")  && isWeeklyOrMonthlyScheme.equals("W")){
            	insDateLst = ClientUtil.executeQuery("getWeeklyMDSCurrentInsDate", insDateMap);    
            }else{
            	insDateLst = ClientUtil.executeQuery("getMDSCurrentInsDate", insDateMap);
            }
            if(insDateLst!=null && insDateLst.size()>0){
                insDateMap = (HashMap)insDateLst.get(0);
                InstalDt = (Date) insDateMap.get("INSTALLMENT_DT");
                setTdtChitEndDt(CommonUtil.convertObjToStr(InstalDt));
                //Commented by Rajesh
                setTxtCurrentInstNo(CommonUtil.convertObjToStr(insDateMap.get("INSTALLMENT_NO")));
                //Changed By Suresh 31-jan-2013
                int pendingInst = 0;
                String advance_Collection ="";
                HashMap prodMap = new HashMap();
                prodMap.put("SCHEME_NAME", getTxtSchemeName());
                List prodLst = ClientUtil.executeQuery("getSelectSchemeAcctHead", prodMap);
                if (prodLst != null && prodLst.size() > 0) {
                    prodMap = (HashMap) prodLst.get(0);
                    advance_Collection = CommonUtil.convertObjToStr(prodMap.get("ADVANCE_COLLECTION"));
                }
                if (advance_Collection.equals("Y")) {// changed count - 1 to count for 	0007562: MDS Interest after closure by nithya on 02-01-2018
                    pendingInst = CommonUtil.convertObjToInt(insDateMap.get("INSTALLMENT_NO")) - (int) count ;
                } else {
                    pendingInst = CommonUtil.convertObjToInt(insDateMap.get("INSTALLMENT_NO")) - (int) count;
                }
                if(pendingInst>0){
                    setTxtPendingInst(String.valueOf(pendingInst));
                }else{
                    setTxtPendingInst("0");
                }
                System.out.println("@@#@#@#@#@#@# endDate ..."+DateUtil.dateDiff(endDate,currDate));
                System.out.println("@@#@#@#@#@#@# end date  ..."+endDate);
                System.out.println("@@#@#@#@#@#@# currdate ..."+currDate);
                if(DateUtil.dateDiff(endDate,currDate)>0){
                    setTxtNoOfInstToPaay(getTxtPendingInst());
                    System.out.println("@@#@#@#@#@#@# no of inst pay 6 ..."+getTxtNoOfInstToPaay());
                }
    			setTxtTotalInstAmt(String.valueOf(instAmt * CommonUtil.convertObjToDouble(getTxtPendingInst()).doubleValue()));                            
            }
            System.out.println("@@#@#@#@#@#@# no of inst pay 5 ..."+getTxtNoOfInstToPaay());
            insDateMap.clear();
            insDateMap = null;
            insDateLst = null;
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
    
    public boolean populateData(HashMap whereMap) {
        isSplitMDSTransaction = "";
        boolean aliasBranchTableFlag = false;
        HashMap mapData = new HashMap() ;
        try {
            if (whereMap != null && whereMap.containsKey("isSplitMDSTransaction") && whereMap.get("isSplitMDSTransaction") != null) {
                isSplitMDSTransaction = CommonUtil.convertObjToStr(whereMap.get("isSplitMDSTransaction"));
            }
            mapData = proxy.executeQuery(whereMap, operationMap);
            aliasBranchTableFlag = populateOB(mapData);
        } catch( Exception e ) {
            parseException.logException(e,true);
        }
        return aliasBranchTableFlag;
    }
    
    private boolean populateOB(HashMap mapData) {
        boolean aliasBranchTableFlag = false;
        try{
            mdsReceiptEntryTO = new MDSClosedReciptTO();
            if (isSplitMDSTransaction != null && isSplitMDSTransaction.equals("Y")) {
                if (mapData.containsKey("MDSReceiptEntryTO") && mapData.get("MDSReceiptEntryTO") != null) {
                    bonusAmount = 0.0;
                    penalAmount = 0.0;
                    instAmount = 0.0;
                    noticeAmount = 0.0;
                    arbitrationAmount = 0.0;
                    serviceTaxAmount = 0.0;
                    List lst = (List) mapData.get("MDSReceiptEntryTO");
                    if (lst != null && lst.size() > 0) {
                        wholeSplitMapp.put("MDSReceiptEntryTO", lst);
//                        mdsReceiptEntryTO = new MDSReceiptEntryTO();
                        for (int i = 0; i < lst.size(); i++) {
                            mdsReceiptEntryTO = (MDSClosedReciptTO) lst.get(i);
                            instAmount += mdsReceiptEntryTO.getInstAmtPayable();
                            penalAmount += mdsReceiptEntryTO.getPenalAmtPayable();
                            bonusAmount += mdsReceiptEntryTO.getBonusAmtPayable();
                            noticeAmount += mdsReceiptEntryTO.getNoticeAmt();
                            arbitrationAmount += mdsReceiptEntryTO.getArbitrationAmt();
                            serviceTaxAmount += mdsReceiptEntryTO.getServiceTaxAmt();
                        }
                        getMDSReceiptEntryTO(mdsReceiptEntryTO);
                    }
                }
            } else {
                mdsReceiptEntryTO = (MDSClosedReciptTO) ((List) mapData.get("MDSReceiptEntryTO")).get(0);
                getMDSReceiptEntryTO(mdsReceiptEntryTO);
            }
            List lst = (List)mapData.get("TransactionTO");
            transactionOB.setDetails(lst);
            oldTransDetMap = new HashMap();            
            if(mapData.containsKey("NET_TRANSACTION_TRANSFER")){
                oldTransDetMap.put("NET_TRANSACTION_TRANSFER",mapData.get("NET_TRANSACTION_TRANSFER"));
            }
            if(mapData.containsKey("NET_TRANSACTION_CASH")){
                oldTransDetMap.put("NET_TRANSACTION_CASH",mapData.get("NET_TRANSACTION_CASH"));
            }
            if(mapData.containsKey("PENAL_TRANSACTION_CASH")){
                oldTransDetMap.put("PENAL_TRANSACTION_CASH",mapData.get("PENAL_TRANSACTION_CASH"));
            }
            if(mapData.containsKey("ARBITRATION_TRANSACTION_CASH")){
                oldTransDetMap.put("ARBITRATION_TRANSACTION_CASH",mapData.get("ARBITRATION_TRANSACTION_CASH"));
            }            
            if(mapData.containsKey("NOTICE_TRANSACTION_CASH")){
                oldTransDetMap.put("NOTICE_TRANSACTION_CASH",mapData.get("NOTICE_TRANSACTION_CASH"));
            }
            if(mapData.containsKey("BONUS_TRANSACTION_TRANSFER")){
                oldTransDetMap.put("BONUS_TRANSACTION_TRANSFER",mapData.get("BONUS_TRANSACTION_TRANSFER"));
            }       
            if(mapData.containsKey("DISCOUNT_TRANSACTION_TRANSFER")){
                oldTransDetMap.put("DISCOUNT_TRANSACTION_TRANSFER",mapData.get("DISCOUNT_TRANSACTION_TRANSFER"));
            }  
            if(mapData.containsKey("VOUCHER_DATA")){
                HashMap whereMap = new HashMap();
                whereMap = (HashMap)mapData.get("VOUCHER_DATA");
                displayTransDetail(whereMap);
            }
            notifyObservers();
        }catch (Exception e){
            System.out.println("PrintStackTrace :" + e);
        }
        return aliasBranchTableFlag;
    }
    
    private MDSClosedReciptTO getMDSReceiptEntryTO(MDSClosedReciptTO mdsReceiptEntryTO){
        //        MDSReceiptEntryTO mdsReceiptEntryTO = new MDSReceiptEntryTO();
        setTxtSchemeName(mdsReceiptEntryTO.getSchemeName());
        setTxtChittalNo(mdsReceiptEntryTO.getChittalNo());
        setTxtDivisionNo(CommonUtil.convertObjToStr(mdsReceiptEntryTO.getDivisionNo()));
        setTdtChitStartDt(CommonUtil.convertObjToStr(mdsReceiptEntryTO.getChitStartDt()));
        setTdtChitEndDt(CommonUtil.convertObjToStr(mdsReceiptEntryTO.getChitEndDt()));
        setTxtNoOfInst(CommonUtil.convertObjToStr(mdsReceiptEntryTO.getNoOfInst()));
        setTxtSubNo(CommonUtil.convertObjToStr(mdsReceiptEntryTO.getSubNo()));
        setTxtCurrentInstNo(CommonUtil.convertObjToStr(mdsReceiptEntryTO.getCurrInst()));
        setTxtInstAmt(CommonUtil.convertObjToStr(mdsReceiptEntryTO.getInstAmt()));
        setTxtPendingInst(CommonUtil.convertObjToStr(mdsReceiptEntryTO.getPendingInst()));
        setTxtTotalInstAmt(CommonUtil.convertObjToStr(mdsReceiptEntryTO.getTotalInstDue()));
        setTxtBonusAmtAvail(CommonUtil.convertObjToStr(mdsReceiptEntryTO.getBonusAmtAvail()));
        setLblMemberNameVal(CommonUtil.convertObjToStr(mdsReceiptEntryTO.getMemberName()));
        if(mdsReceiptEntryTO.getPrizedMember().equals("Y")){
            setRdoPrizedMember_Yes(true);
        }else{
            setRdoPrizedMember_Yes(false);
        }
        if (isSplitMDSTransaction != null && isSplitMDSTransaction.equals("Y")) {
            setTxtNoticeAmt(CommonUtil.convertObjToStr(noticeAmount));
        } else {
            setTxtNoticeAmt(CommonUtil.convertObjToStr(mdsReceiptEntryTO.getNoticeAmt()));
        }
        if (isSplitMDSTransaction != null && isSplitMDSTransaction.equals("Y")) {
            setTxtAribitrationAmt(CommonUtil.convertObjToStr(arbitrationAmount));
            setLblServiceTaxval(CommonUtil.convertObjToStr(serviceTaxAmount));
        } else {
            setTxtAribitrationAmt(CommonUtil.convertObjToStr(mdsReceiptEntryTO.getArbitrationAmt()));
            setLblServiceTaxval(CommonUtil.convertObjToStr(mdsReceiptEntryTO.getServiceTaxAmt()));
        }
        setTxtNoOfInstToPaay(CommonUtil.convertObjToStr(mdsReceiptEntryTO.getNoOfInstPay()));
        System.out.println("@#@#@#@#@#@#@# no of inst pay : "+getTxtNoOfInstToPaay());
        if (isSplitMDSTransaction != null && isSplitMDSTransaction.equals("Y")) {
            setTxtInstPayable(CommonUtil.convertObjToStr(instAmount));
        } else {
            setTxtInstPayable(CommonUtil.convertObjToStr(mdsReceiptEntryTO.getInstAmtPayable()));
        }
        if (isSplitMDSTransaction != null && isSplitMDSTransaction.equals("Y")) {
            setTxtPenalAmtPayable(CommonUtil.convertObjToStr(penalAmount));
        } else {
            setTxtPenalAmtPayable(CommonUtil.convertObjToStr(mdsReceiptEntryTO.getPenalAmtPayable()));
        }
        if (isSplitMDSTransaction != null && isSplitMDSTransaction.equals("Y")) {
            setTxtBonusAmt(CommonUtil.convertObjToStr(bonusAmount));
        } else {
            setTxtBonusAmt(CommonUtil.convertObjToStr(mdsReceiptEntryTO.getBonusAmtPayable()));
        }
        setTxtDiscountAmt(CommonUtil.convertObjToStr(mdsReceiptEntryTO.getDiscountAmt()));
        setTxtInterest(CommonUtil.convertObjToStr(mdsReceiptEntryTO.getMdsInterset()));
        if (isSplitMDSTransaction != null && isSplitMDSTransaction.equals("Y")) {
            setTxtNetAmtPaid(CommonUtil.convertObjToDouble(instAmount + penalAmount + noticeAmount+arbitrationAmount+serviceTaxAmount).toString());
        } else {
            setTxtNetAmtPaid(CommonUtil.convertObjToStr(mdsReceiptEntryTO.getNetAmt() + mdsReceiptEntryTO.getNoticeAmt()));//9535
        }
        setTdtPaidDate(CommonUtil.convertObjToStr(mdsReceiptEntryTO.getPaidDate()));
        setTxtPaidInst(CommonUtil.convertObjToStr(mdsReceiptEntryTO.getPaidInst()));
        setTxtPaidAmt(CommonUtil.convertObjToStr(mdsReceiptEntryTO.getPaidAmt()));
        if(mdsReceiptEntryTO.getThalayal().equals("Y")){
            setChkThalayal(true);
        }else{
            setChkThalayal(true);
        }
        if(mdsReceiptEntryTO.getMunnal().equals("Y")){
            setChkMunnal(true);
        }else{
            setChkMunnal(false);
        }
        if(mdsReceiptEntryTO.getMemberChanged().equals("Y")){
            setChkMemberChanged(true);
        }else{
            setChkMemberChanged(false);
        }
        if(mdsReceiptEntryTO.getBankPay().equals("N")){
            setRdoBankPay_No(true);
        }else if(mdsReceiptEntryTO.getBankPay().equals("Y")){
            setRdoBankPay_Yes(true);
        }
        
        setTxtEarlierMember(CommonUtil.convertObjToStr(mdsReceiptEntryTO.getEarlierMemberNo()));
        setTxtEarlierMemberName(CommonUtil.convertObjToStr(mdsReceiptEntryTO.getEarlierMemberName()));
        setTxtChangedInst(CommonUtil.convertObjToStr(mdsReceiptEntryTO.getChangedInstNo()));
        setTdtChangedDate(CommonUtil.convertObjToStr(mdsReceiptEntryTO.getChangedDt()));
        setTransId(mdsReceiptEntryTO.getNetTransId());
        setTxtForfeitBonus(CommonUtil.convertObjToStr(mdsReceiptEntryTO.getForfeitBonusAmtPayable())); //2093
        setBankAdvAmt(CommonUtil.convertObjToStr(mdsReceiptEntryTO.getBankAdvanceAmt()));
        setNarration(CommonUtil.convertObjToStr(mdsReceiptEntryTO.getNarration()));//KD-4275 - Added by Nithya - Narration not updating in trans details
        setEditDetails();
        return mdsReceiptEntryTO;
    }
    
    public void setEditDetails() {
        long count = 0;
        HashMap instMap = new HashMap();
        instMap.put("CHITTAL_NO",getTxtChittalNo());
        instMap.put("SUB_NO",CommonUtil.convertObjToInt(getTxtSubNo()));
        List lst = ClientUtil.executeQuery("getNoOfInstalmentsPaid", instMap);
        if(lst!=null && lst.size()>0){
            instMap = (HashMap)lst.get(0);
            count = CommonUtil.convertObjToInt(instMap.get("NO_OF_INST"));
            setTxtPaidNoOfInst(String.valueOf(count));
        }else {
            setTxtPaidNoOfInst(String.valueOf("0"));
        }
        int instDay = 0;
        HashMap whereMap = new HashMap();
        whereMap.put("SCHEME_NAME",getTxtSchemeName());
        whereMap.put("DIVISION_NO", CommonUtil.convertObjToInt(getTxtDivisionNo()));
        whereMap.put("INSTALLMENT_NO",CommonUtil.convertObjToInt(String.valueOf(count)));
        lst = ClientUtil.executeQuery("getSelectInstUptoPaid", whereMap);
        if(lst!=null && lst.size()>0){
            whereMap = (HashMap)lst.get(0);
            setTdtInsUptoPaid(CommonUtil.convertObjToStr(whereMap.get("NEXT_INSTALLMENT_DATE")));
        }else{
            if(getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE ||getActionType()==ClientConstants.ACTIONTYPE_REJECT)
                setReceiptDetails(hash);
            Date startDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalMap.get("SCHEME_START_DT")));
            instDay = CommonUtil.convertObjToInt(chittalMap.get("INSTALLMENT_DAY"));
            startDate.setDate(instDay);
            int stMonth = startDate.getMonth();
            startDate.setMonth(stMonth+(int)count-1);
            setTdtInsUptoPaid(CommonUtil.convertObjToStr(startDate));
        }
        lst.clear();
        instMap.clear();
        whereMap.clear();
        lst = null;
        instMap = null;
        whereMap = null;
    }
        
    /** Resets the General Remittance Fields to Null  */
    public void resetOBFields(){
        setTxtSchemeName("");
        setTxtChittalNo("");
        setLblMemberNameVal("");
        setLblMemberNo("");
        setTxtDivisionNo("");
        setTdtChitStartDt("");
        setTxtNoOfInst("");
        setTxtSubNo("");
        setTxtCurrentInstNo("");
        setTdtInsUptoPaid("");
        setTxtInstAmt("");
        setTxtPendingInst("");
        setTxtTotalInstAmt("");
        setTxtBonusAmtAvail("");
        setRdoPrizedMember_Yes(false);
        setRdoPrizedMember_No(false);
        setTxtNoticeAmt("");
        setTxtAribitrationAmt("");
        setTxtNoOfInstToPaay("");
        setTxtInstPayable("");
        setTxtInstPayable("");
        setTxtBonusAmt("");
        setTxtForfeitBonus("");//2093
        setBankAdvAmt("");
        setTxtDiscountAmt("");
        setTxtInterest("");
        setTxtNetAmtPaid("");
        setTdtChitEndDt("");
        setTdtPaidDate("");
        setTxtPaidInst("");
        setTxtPaidNoOfInst("");
        setTxtPaidAmt("");
        setTxtPenalAmtPayable("");
        setChkThalayal(false);
        setChkMunnal(false);
        setChkMemberChanged(false);
        setTxtEarlierMember("");
        setTxtEarlierMemberName("");
        setTxtChangedInst("");
        setTdtChangedDate("");
        setLblServiceTaxval("");
        setServiceTax_Map(null);
        setReturnWaiveMap(null);
        setTxtGlobalPenalAmt(0.0);
    }
    
    /**
     * Getter for property txtSchemeName.
     * @return Value of property txtSchemeName.
     */
    public java.lang.String getTxtSchemeName() {
        return txtSchemeName;
    }
    
    /**
     * Setter for property txtSchemeName.
     * @param txtSchemeName New value of property txtSchemeName.
     */
    public void setTxtSchemeName(java.lang.String txtSchemeName) {
        this.txtSchemeName = txtSchemeName;
    }
    
    /**
     * Getter for property txtChittalNo.
     * @return Value of property txtChittalNo.
     */
    public java.lang.String getTxtChittalNo() {
        return txtChittalNo;
    }
    
    /**
     * Setter for property txtChittalNo.
     * @param txtChittalNo New value of property txtChittalNo.
     */
    public void setTxtChittalNo(java.lang.String txtChittalNo) {
        this.txtChittalNo = txtChittalNo;
    }
    
    /**
     * Getter for property lblMemberNameVal.
     * @return Value of property lblMemberNameVal.
     */
    public java.lang.String getLblMemberNameVal() {
        return lblMemberNameVal;
    }
    
    /**
     * Setter for property lblMemberNameVal.
     * @param lblMemberNameVal New value of property lblMemberNameVal.
     */
    public void setLblMemberNameVal(java.lang.String lblMemberNameVal) {
        this.lblMemberNameVal = lblMemberNameVal;
    }
    
    /**
     * Getter for property txtDivisionNo.
     * @return Value of property txtDivisionNo.
     */
    public java.lang.String getTxtDivisionNo() {
        return txtDivisionNo;
    }
    
    /**
     * Setter for property txtDivisionNo.
     * @param txtDivisionNo New value of property txtDivisionNo.
     */
    public void setTxtDivisionNo(java.lang.String txtDivisionNo) {
        this.txtDivisionNo = txtDivisionNo;
    }
    
    /**
     * Getter for property tdtChitStartDt.
     * @return Value of property tdtChitStartDt.
     */
    public java.lang.String getTdtChitStartDt() {
        return tdtChitStartDt;
    }
    
    /**
     * Setter for property tdtChitStartDt.
     * @param tdtChitStartDt New value of property tdtChitStartDt.
     */
    public void setTdtChitStartDt(java.lang.String tdtChitStartDt) {
        this.tdtChitStartDt = tdtChitStartDt;
    }
    
    /**
     * Getter for property txtNoOfInst.
     * @return Value of property txtNoOfInst.
     */
    public java.lang.String getTxtNoOfInst() {
        return txtNoOfInst;
    }
    
    /**
     * Setter for property txtNoOfInst.
     * @param txtNoOfInst New value of property txtNoOfInst.
     */
    public void setTxtNoOfInst(java.lang.String txtNoOfInst) {
        this.txtNoOfInst = txtNoOfInst;
    }
    
    /**
     * Getter for property txtCurrentInstNo.
     * @return Value of property txtCurrentInstNo.
     */
    public java.lang.String getTxtCurrentInstNo() {
        return txtCurrentInstNo;
    }
    
    /**
     * Setter for property txtCurrentInstNo.
     * @param txtCurrentInstNo New value of property txtCurrentInstNo.
     */
    public void setTxtCurrentInstNo(java.lang.String txtCurrentInstNo) {
        this.txtCurrentInstNo = txtCurrentInstNo;
    }
    
    /**
     * Getter for property txtInstAmt.
     * @return Value of property txtInstAmt.
     */
    public java.lang.String getTxtInstAmt() {
        return txtInstAmt;
    }
    
    /**
     * Setter for property txtInstAmt.
     * @param txtInstAmt New value of property txtInstAmt.
     */
    public void setTxtInstAmt(java.lang.String txtInstAmt) {
        this.txtInstAmt = txtInstAmt;
    }
    
    /**
     * Getter for property txtPendingInst.
     * @return Value of property txtPendingInst.
     */
    public java.lang.String getTxtPendingInst() {
        return txtPendingInst;
    }
    
    /**
     * Setter for property txtPendingInst.
     * @param txtPendingInst New value of property txtPendingInst.
     */
    public void setTxtPendingInst(java.lang.String txtPendingInst) {
        this.txtPendingInst = txtPendingInst;
    }
    
    /**
     * Getter for property txtTotalInstAmt.
     * @return Value of property txtTotalInstAmt.
     */
    public java.lang.String getTxtTotalInstAmt() {
        return txtTotalInstAmt;
    }
    
    /**
     * Setter for property txtTotalInstAmt.
     * @param txtTotalInstAmt New value of property txtTotalInstAmt.
     */
    public void setTxtTotalInstAmt(java.lang.String txtTotalInstAmt) {
        this.txtTotalInstAmt = txtTotalInstAmt;
    }
    
    /**
     * Getter for property txtBonusAmtAvail.
     * @return Value of property txtBonusAmtAvail.
     */
    public java.lang.String getTxtBonusAmtAvail() {
        return txtBonusAmtAvail;
    }
    
    /**
     * Setter for property txtBonusAmtAvail.
     * @param txtBonusAmtAvail New value of property txtBonusAmtAvail.
     */
    public void setTxtBonusAmtAvail(java.lang.String txtBonusAmtAvail) {
        this.txtBonusAmtAvail = txtBonusAmtAvail;
    }
    
    /**
     * Getter for property rdoPrizedMember_Yes.
     * @return Value of property rdoPrizedMember_Yes.
     */
    public boolean getRdoPrizedMember_Yes() {
        return rdoPrizedMember_Yes;
    }
    
    /**
     * Setter for property rdoPrizedMember_Yes.
     * @param rdoPrizedMember_Yes New value of property rdoPrizedMember_Yes.
     */
    public void setRdoPrizedMember_Yes(boolean rdoPrizedMember_Yes) {
        this.rdoPrizedMember_Yes = rdoPrizedMember_Yes;
    }
    
    /**
     * Getter for property rdoPrizedMember_No.
     * @return Value of property rdoPrizedMember_No.
     */
    public boolean getRdoPrizedMember_No() {
        return rdoPrizedMember_No;
    }
    
    /**
     * Setter for property rdoPrizedMember_No.
     * @param rdoPrizedMember_No New value of property rdoPrizedMember_No.
     */
    public void setRdoPrizedMember_No(boolean rdoPrizedMember_No) {
        this.rdoPrizedMember_No = rdoPrizedMember_No;
    }
    
    /**
     * Getter for property txtNoticeAmt.
     * @return Value of property txtNoticeAmt.
     */
    public java.lang.String getTxtNoticeAmt() {
        return txtNoticeAmt;
    }
    
    /**
     * Setter for property txtNoticeAmt.
     * @param txtNoticeAmt New value of property txtNoticeAmt.
     */
    public void setTxtNoticeAmt(java.lang.String txtNoticeAmt) {
        this.txtNoticeAmt = txtNoticeAmt;
    }
    
    /**
     * Getter for property txtAribitrationAmt.
     * @return Value of property txtAribitrationAmt.
     */
    public java.lang.String getTxtAribitrationAmt() {
        return txtAribitrationAmt;
    }
    
    /**
     * Setter for property txtAribitrationAmt.
     * @param txtAribitrationAmt New value of property txtAribitrationAmt.
     */
    public void setTxtAribitrationAmt(java.lang.String txtAribitrationAmt) {
        this.txtAribitrationAmt = txtAribitrationAmt;
    }
    
    /**
     * Getter for property txtNoOfInstToPaay.
     * @return Value of property txtNoOfInstToPaay.
     */
    public java.lang.String getTxtNoOfInstToPaay() {
        return txtNoOfInstToPaay;
    }
    
    /**
     * Setter for property txtNoOfInstToPaay.
     * @param txtNoOfInstToPaay New value of property txtNoOfInstToPaay.
     */
    public void setTxtNoOfInstToPaay(java.lang.String txtNoOfInstToPaay) {
        this.txtNoOfInstToPaay = txtNoOfInstToPaay;
    }
    
    /**
     * Getter for property txtInstPayable.
     * @return Value of property txtInstPayable.
     */
    public java.lang.String getTxtInstPayable() {
        return txtInstPayable;
    }
    
    /**
     * Setter for property txtInstPayable.
     * @param txtInstPayable New value of property txtInstPayable.
     */
    public void setTxtInstPayable(java.lang.String txtInstPayable) {
        this.txtInstPayable = txtInstPayable;
    }
    
    /**
     * Getter for property txtPenalAmtPayable.
     * @return Value of property txtPenalAmtPayable.
     */
    public java.lang.String getTxtPenalAmtPayable() {
        return txtPenalAmtPayable;
    }
    
    /**
     * Setter for property txtPenalAmtPayable.
     * @param txtPenalAmtPayable New value of property txtPenalAmtPayable.
     */
    public void setTxtPenalAmtPayable(java.lang.String txtPenalAmtPayable) {
        this.txtPenalAmtPayable = txtPenalAmtPayable;
    }
    
    /**
     * Getter for property txtBonusAmt.
     * @return Value of property txtBonusAmt.
     */
    public java.lang.String getTxtBonusAmt() {
        return txtBonusAmt;
    }
    
    /**
     * Setter for property txtBonusAmt.
     * @param txtBonusAmt New value of property txtBonusAmt.
     */
    public void setTxtBonusAmt(java.lang.String txtBonusAmt) {
        this.txtBonusAmt = txtBonusAmt;
    }
    
    /**
     * Getter for property txtDiscountAmt.
     * @return Value of property txtDiscountAmt.
     */
    public java.lang.String getTxtDiscountAmt() {
        return txtDiscountAmt;
    }
    
    /**
     * Setter for property txtDiscountAmt.
     * @param txtDiscountAmt New value of property txtDiscountAmt.
     */
    public void setTxtDiscountAmt(java.lang.String txtDiscountAmt) {
        this.txtDiscountAmt = txtDiscountAmt;
    }
    
    /**
     * Getter for property txtInterest.
     * @return Value of property txtInterest.
     */
    public java.lang.String getTxtInterest() {
        return txtInterest;
    }
    
    /**
     * Setter for property txtInterest.
     * @param txtInterest New value of property txtInterest.
     */
    public void setTxtInterest(java.lang.String txtInterest) {
        this.txtInterest = txtInterest;
    }
    
    /**
     * Getter for property txtNetAmtPaid.
     * @return Value of property txtNetAmtPaid.
     */
    public java.lang.String getTxtNetAmtPaid() {
        return txtNetAmtPaid;
    }
    
    /**
     * Setter for property txtNetAmtPaid.
     * @param txtNetAmtPaid New value of property txtNetAmtPaid.
     */
    public void setTxtNetAmtPaid(java.lang.String txtNetAmtPaid) {
        this.txtNetAmtPaid = txtNetAmtPaid;
    }
    
    /**
     * Getter for property tdtPaidDate.
     * @return Value of property tdtPaidDate.
     */
    public java.lang.String getTdtPaidDate() {
        return tdtPaidDate;
    }
    
    /**
     * Setter for property tdtPaidDate.
     * @param tdtPaidDate New value of property tdtPaidDate.
     */
    public void setTdtPaidDate(java.lang.String tdtPaidDate) {
        this.tdtPaidDate = tdtPaidDate;
    }
    
    /**
     * Getter for property txtPaidInst.
     * @return Value of property txtPaidInst.
     */
    public java.lang.String getTxtPaidInst() {
        return txtPaidInst;
    }
    
    /**
     * Setter for property txtPaidInst.
     * @param txtPaidInst New value of property txtPaidInst.
     */
    public void setTxtPaidInst(java.lang.String txtPaidInst) {
        this.txtPaidInst = txtPaidInst;
    }
    
    /**
     * Getter for property txtPaidAmt.
     * @return Value of property txtPaidAmt.
     */
    public java.lang.String getTxtPaidAmt() {
        return txtPaidAmt;
    }
    
    /**
     * Setter for property txtPaidAmt.
     * @param txtPaidAmt New value of property txtPaidAmt.
     */
    public void setTxtPaidAmt(java.lang.String txtPaidAmt) {
        this.txtPaidAmt = txtPaidAmt;
    }
    
    /**
     * Getter for property txtEarlierMember.
     * @return Value of property txtEarlierMember.
     */
    public java.lang.String getTxtEarlierMember() {
        return txtEarlierMember;
    }
    
    /**
     * Setter for property txtEarlierMember.
     * @param txtEarlierMember New value of property txtEarlierMember.
     */
    public void setTxtEarlierMember(java.lang.String txtEarlierMember) {
        this.txtEarlierMember = txtEarlierMember;
    }
    
    /**
     * Getter for property txtEarlierMemberName.
     * @return Value of property txtEarlierMemberName.
     */
    public java.lang.String getTxtEarlierMemberName() {
        return txtEarlierMemberName;
    }
    
    /**
     * Setter for property txtEarlierMemberName.
     * @param txtEarlierMemberName New value of property txtEarlierMemberName.
     */
    public void setTxtEarlierMemberName(java.lang.String txtEarlierMemberName) {
        this.txtEarlierMemberName = txtEarlierMemberName;
    }
    
    /**
     * Getter for property txtChangedInst.
     * @return Value of property txtChangedInst.
     */
    public java.lang.String getTxtChangedInst() {
        return txtChangedInst;
    }
    
    /**
     * Setter for property txtChangedInst.
     * @param txtChangedInst New value of property txtChangedInst.
     */
    public void setTxtChangedInst(java.lang.String txtChangedInst) {
        this.txtChangedInst = txtChangedInst;
    }
    
    /**
     * Getter for property tdtChangedDate.
     * @return Value of property tdtChangedDate.
     */
    public java.lang.String getTdtChangedDate() {
        return tdtChangedDate;
    }
    
    /**
     * Setter for property tdtChangedDate.
     * @param tdtChangedDate New value of property tdtChangedDate.
     */
    public void setTdtChangedDate(java.lang.String tdtChangedDate) {
        this.tdtChangedDate = tdtChangedDate;
    }
    
    /**
     * Getter for property chkThalayal.
     * @return Value of property chkThalayal.
     */
    public boolean getChkThalayal() {
        return chkThalayal;
    }
    
    /**
     * Setter for property chkThalayal.
     * @param chkThalayal New value of property chkThalayal.
     */
    public void setChkThalayal(boolean chkThalayal) {
        this.chkThalayal = chkThalayal;
    }
    
    /**
     * Getter for property chkMunnal.
     * @return Value of property chkMunnal.
     */
    public boolean getChkMunnal() {
        return chkMunnal;
    }
    
    /**
     * Setter for property chkMunnal.
     * @param chkMunnal New value of property chkMunnal.
     */
    public void setChkMunnal(boolean chkMunnal) {
        this.chkMunnal = chkMunnal;
    }
    
    /**
     * Getter for property chkMemberChanged.
     * @return Value of property chkMemberChanged.
     */
    public boolean getChkMemberChanged() {
        return chkMemberChanged;
    }
    
    /**
     * Setter for property chkMemberChanged.
     * @param chkMemberChanged New value of property chkMemberChanged.
     */
    public void setChkMemberChanged(boolean chkMemberChanged) {
        this.chkMemberChanged = chkMemberChanged;
    }
    
    /**
     * Getter for property tdtChitEndDt.
     * @return Value of property tdtChitEndDt.
     */
    public java.lang.String getTdtChitEndDt() {
        return tdtChitEndDt;
    }
    
    /**
     * Setter for property tdtChitEndDt.
     * @param tdtChitEndDt New value of property tdtChitEndDt.
     */
    public void setTdtChitEndDt(java.lang.String tdtChitEndDt) {
        this.tdtChitEndDt = tdtChitEndDt;
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
     * Getter for property transactionOB.
     * @return Value of property transactionOB.
     */
    public com.see.truetransact.ui.common.transaction.TransactionOB getTransactionOB() {
        return transactionOB;
    }
    
    /**
     * Setter for property transactionOB.
     * @param transactionOB New value of property transactionOB.
     */
    public void setTransactionOB(com.see.truetransact.ui.common.transaction.TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
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
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }
    
    /**
     * Getter for property transactionDetailsTO.
     * @return Value of property transactionDetailsTO.
     */
    public java.util.LinkedHashMap getTransactionDetailsTO() {
        return transactionDetailsTO;
    }
    
    /**
     * Setter for property transactionDetailsTO.
     * @param transactionDetailsTO New value of property transactionDetailsTO.
     */
    public void setTransactionDetailsTO(java.util.LinkedHashMap transactionDetailsTO) {
        this.transactionDetailsTO = transactionDetailsTO;
    }
    
    /**
     * Getter for property deletedTransactionDetailsTO.
     * @return Value of property deletedTransactionDetailsTO.
     */
    public java.util.LinkedHashMap getDeletedTransactionDetailsTO() {
        return deletedTransactionDetailsTO;
    }
    
    /**
     * Setter for property deletedTransactionDetailsTO.
     * @param deletedTransactionDetailsTO New value of property deletedTransactionDetailsTO.
     */
    public void setDeletedTransactionDetailsTO(java.util.LinkedHashMap deletedTransactionDetailsTO) {
        this.deletedTransactionDetailsTO = deletedTransactionDetailsTO;
    }
    
    /**
     * Getter for property productMap.
     * @return Value of property productMap.
     */
    public java.util.HashMap getProductMap() {
        return productMap;
    }
    
    /**
     * Setter for property productMap.
     * @param productMap New value of property productMap.
     */
    public void setProductMap(java.util.HashMap productMap) {
        this.productMap = productMap;
    }
    
    /**
     * Getter for property transId.
     * @return Value of property transId.
     */
    public java.lang.String getTransId() {
        return transId;
    }
    
    /**
     * Setter for property transId.
     * @param transId New value of property transId.
     */
    public void setTransId(java.lang.String transId) {
        this.transId = transId;
    }
    
    /**
     * Getter for property authorizeMap.
     * @return Value of property authorizeMap.
     */
    public java.util.HashMap getAuthorizeMap() {
        return authorizeMap;
    }
    
    /**
     * Setter for property authorizeMap.
     * @param authorizeMap New value of property authorizeMap.
     */
    public void setAuthorizeMap(java.util.HashMap authorizeMap) {
        this.authorizeMap = authorizeMap;
    }
    
    /**
     * Getter for property rdoBankPay_Yes.
     * @return Value of property rdoBankPay_Yes.
     */
    public boolean getRdoBankPay_Yes() {
        return rdoBankPay_Yes;
    }
    
    /**
     * Setter for property rdoBankPay_Yes.
     * @param rdoBankPay_Yes New value of property rdoBankPay_Yes.
     */
    public void setRdoBankPay_Yes(boolean rdoBankPay_Yes) {
        this.rdoBankPay_Yes = rdoBankPay_Yes;
    }
    
    /**
     * Getter for property rdoBankPay_No.
     * @return Value of property rdoBankPay_No.
     */
    public boolean getRdoBankPay_No() {
        return rdoBankPay_No;
    }
    
    /**
     * Setter for property rdoBankPay_No.
     * @param rdoBankPay_No New value of property rdoBankPay_No.
     */
    public void setRdoBankPay_No(boolean rdoBankPay_No) {
        this.rdoBankPay_No = rdoBankPay_No;
    }
    
    /**
     * Getter for property lblHouseStNo.
     * @return Value of property lblHouseStNo.
     */
    public java.lang.String getLblHouseStNo() {
        return lblHouseStNo;
    }
    
    /**
     * Setter for property lblHouseStNo.
     * @param lblHouseStNo New value of property lblHouseStNo.
     */
    public void setLblHouseStNo(java.lang.String lblHouseStNo) {
        this.lblHouseStNo = lblHouseStNo;
    }
    
    /**
     * Getter for property lblArea.
     * @return Value of property lblArea.
     */
    public java.lang.String getLblArea() {
        return lblArea;
    }
    
    /**
     * Setter for property lblArea.
     * @param lblArea New value of property lblArea.
     */
    public void setLblArea(java.lang.String lblArea) {
        this.lblArea = lblArea;
    }
    
    /**
     * Getter for property lblCity.
     * @return Value of property lblCity.
     */
    public java.lang.String getLblCity() {
        return lblCity;
    }
    
    /**
     * Setter for property lblCity.
     * @param lblCity New value of property lblCity.
     */
    public void setLblCity(java.lang.String lblCity) {
        this.lblCity = lblCity;
    }
    
    /**
     * Getter for property lblState.
     * @return Value of property lblState.
     */
    public java.lang.String getLblState() {
        return lblState;
    }
    
    /**
     * Setter for property lblState.
     * @param lblState New value of property lblState.
     */
    public void setLblState(java.lang.String lblState) {
        this.lblState = lblState;
    }
    
    /**
     * Getter for property lblpin.
     * @return Value of property lblpin.
     */
    public java.lang.String getLblpin() {
        return lblpin;
    }
    
    /**
     * Setter for property lblpin.
     * @param lblpin New value of property lblpin.
     */
    public void setLblpin(java.lang.String lblpin) {
        this.lblpin = lblpin;
    }
    
    /**
     * Getter for property instMap.
     * @return Value of property instMap.
     */
    public java.util.HashMap getInstMap() {
        return instMap;
    }
    
    /**
     * Setter for property instMap.
     * @param instMap New value of property instMap.
     */
    public void setInstMap(java.util.HashMap instMap) {
        this.instMap = instMap;
    }
    
    /**
     * Getter for property chittalMap.
     * @return Value of property chittalMap.
     */
    public java.util.HashMap getChittalMap() {
        return chittalMap;
    }
    
    /**
     * Setter for property chittalMap.
     * @param chittalMap New value of property chittalMap.
     */
    public void setChittalMap(java.util.HashMap chittalMap) {
        this.chittalMap = chittalMap;
    }
    
    /**
     * Getter for property txtPaidNoOfInst.
     * @return Value of property txtPaidNoOfInst.
     */
    public java.lang.String getTxtPaidNoOfInst() {
        return txtPaidNoOfInst;
    }
    
    /**
     * Setter for property txtPaidNoOfInst.
     * @param txtPaidNoOfInst New value of property txtPaidNoOfInst.
     */
    public void setTxtPaidNoOfInst(java.lang.String txtPaidNoOfInst) {
        this.txtPaidNoOfInst = txtPaidNoOfInst;
    }
    
    /**
     * Getter for property lblMemberNo.
     * @return Value of property lblMemberNo.
     */
    public java.lang.String getLblMemberNo() {
        return lblMemberNo;
    }
    
    /**
     * Setter for property lblMemberNo.
     * @param lblMemberNo New value of property lblMemberNo.
     */
    public void setLblMemberNo(java.lang.String lblMemberNo) {
        this.lblMemberNo = lblMemberNo;
    }
    
    /**
     * Getter for property tdtInsUptoPaid.
     * @return Value of property tdtInsUptoPaid.
     */
    public java.lang.String getTdtInsUptoPaid() {
        return tdtInsUptoPaid;
    }
    
    /**
     * Setter for property tdtInsUptoPaid.
     * @param tdtInsUptoPaid New value of property tdtInsUptoPaid.
     */
    public void setTdtInsUptoPaid(java.lang.String tdtInsUptoPaid) {
        this.tdtInsUptoPaid = tdtInsUptoPaid;
    }
    
    /**
     * Getter for property narration.
     * @return Value of property narration.
     */
    public java.lang.String getNarration() {
        return narration;
    }
    
    /**
     * Setter for property narration.
     * @param narration New value of property narration.
     */
    public void setNarration(java.lang.String narration) {
        this.narration = narration;
    }
    
    /**
     * Getter for property installmentMap.
     * @return Value of property installmentMap.
     */
    public java.util.HashMap getInstallmentMap() {
        return installmentMap;
    }
    
    /**
     * Setter for property installmentMap.
     * @param installmentMap New value of property installmentMap.
     */
    public void setInstallmentMap(java.util.HashMap installmentMap) {
        this.installmentMap = installmentMap;
    }
    
    /**
     * Getter for property txtSubNo.
     * @return Value of property txtSubNo.
     */
    public java.lang.String getTxtSubNo() {
        return txtSubNo;
    }
    
    /**
     * Setter for property txtSubNo.
     * @param txtSubNo New value of property txtSubNo.
     */
    public void setTxtSubNo(java.lang.String txtSubNo) {
        this.txtSubNo = txtSubNo;
    }
    
    /**
     * Getter for property multipleMember.
     * @return Value of property multipleMember.
     */
    public java.lang.String getMultipleMember() {
        return multipleMember;
    }
    
    /**
     * Setter for property multipleMember.
     * @param multipleMember New value of property multipleMember.
     */
    public void setMultipleMember(java.lang.String multipleMember) {
        this.multipleMember = multipleMember;
    }
	public ServiceTaxDetailsTO setServiceTaxDetails() {
        final ServiceTaxDetailsTO objservicetaxDetTo = new ServiceTaxDetailsTO();
        try {
            objservicetaxDetTo.setCommand(getCommand());
            objservicetaxDetTo.setStatus(CommonConstants.STATUS_CREATED);
            objservicetaxDetTo.setStatusBy(TrueTransactMain.USER_ID);
            objservicetaxDetTo.setAcct_Num(getTxtChittalNo());
            objservicetaxDetTo.setParticulars("Cash");
            objservicetaxDetTo.setBranchID(ProxyParameters.BRANCH_ID);
            objservicetaxDetTo.setTrans_type("C");
            if (serviceTax_Map != null && serviceTax_Map.containsKey("SERVICE_TAX")) {
                objservicetaxDetTo.setServiceTaxAmt(CommonUtil.convertObjToDouble(serviceTax_Map.get("SERVICE_TAX")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey("EDUCATION_CESS")) {
                objservicetaxDetTo.setEducationCess(CommonUtil.convertObjToDouble(serviceTax_Map.get("EDUCATION_CESS")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey("HIGHER_EDU_CESS")) {
                objservicetaxDetTo.setHigherCess(CommonUtil.convertObjToDouble(serviceTax_Map.get("HIGHER_EDU_CESS")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey("TOT_TAX_AMT")) {
                objservicetaxDetTo.setTotalTaxAmt(CommonUtil.convertObjToDouble(serviceTax_Map.get("TOT_TAX_AMT")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.SWACHH_CESS)) {
                objservicetaxDetTo.setSwachhCess(CommonUtil.convertObjToDouble(serviceTax_Map.get(ServiceTaxCalculation.SWACHH_CESS)));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS)) {
                objservicetaxDetTo.setKrishiKalyan(CommonUtil.convertObjToDouble(serviceTax_Map.get(ServiceTaxCalculation.KRISHIKALYAN_CESS)));
            }
            double roudVal = objservicetaxDetTo.getTotalTaxAmt() - (objservicetaxDetTo.getServiceTaxAmt() + objservicetaxDetTo.getEducationCess() + objservicetaxDetTo.getHigherCess()+objservicetaxDetTo.getSwachhCess() + objservicetaxDetTo.getKrishiKalyan());
            ServiceTaxCalculation serviceTaxObj= new ServiceTaxCalculation();
            objservicetaxDetTo.setRoundVal(CommonUtil.convertObjToStr(serviceTaxObj.roundOffAmtForRoundVal(roudVal)));
            objservicetaxDetTo.setStatusDt(currDate);

            if (getCommand().equalsIgnoreCase("INSERT")) {
                objservicetaxDetTo.setCreatedBy(TrueTransactMain.USER_ID);
                objservicetaxDetTo.setCreatedDt(currDate);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objservicetaxDetTo;

    }
private void doSplitMDSTransaction(){
    splitMDSReceiptEntryLst.clear();
    isSplitMDSTransaction = "";
    if (getMDSClosedTransListMap() != null && getMDSClosedTransListMap().size() > 0) {
        HashMap MDSsplitTrans = (HashMap) getMDSClosedTransListMap();
        if (MDSsplitTrans != null && MDSsplitTrans.containsKey("IS_SPLIT_MDS_TRANSACTION") && MDSsplitTrans.get("IS_SPLIT_MDS_TRANSACTION") != null) {
            isSplitMDSTransaction = CommonUtil.convertObjToStr(MDSsplitTrans.get("IS_SPLIT_MDS_TRANSACTION"));
        }
        if (isSplitMDSTransaction != null && isSplitMDSTransaction.equals("Y")) {
            if(MDSsplitTrans.containsKey("INST_AMT_LIST") && MDSsplitTrans.get("INST_AMT_LIST") != null){
            splitTransInstList = (List) MDSsplitTrans.get("INST_AMT_LIST");
            }
            if(MDSsplitTrans.containsKey("BONUS_AMT_LIST") && MDSsplitTrans.get("BONUS_AMT_LIST") != null){
            bonusAmountList = (List) MDSsplitTrans.get("BONUS_AMT_LIST");
            }
            if(MDSsplitTrans.containsKey("PENAL_AMT_LIST") && MDSsplitTrans.get("PENAL_AMT_LIST") != null){
            penalList = (List) MDSsplitTrans.get("PENAL_AMT_LIST");
            }
            if(MDSsplitTrans.containsKey("NARRATION_LIST") && MDSsplitTrans.get("NARRATION_LIST") != null){
            narrationList = (List) MDSsplitTrans.get("NARRATION_LIST");
            }
        }
        double noticeAmounts = CommonUtil.convertObjToDouble(getTxtNoticeAmt());
        double arbitrationAmounts = CommonUtil.convertObjToDouble(getTxtAribitrationAmt());
        double serviceTaxAmt = CommonUtil.convertObjToDouble(getLblServiceTaxval());
        if (splitTransInstList.size() > 0 && splitTransInstList != null
                && bonusAmountList.size() > 0 && bonusAmountList != null
                && penalList.size() > 0 && penalList != null && narrationList.size() > 0 && narrationList != null) {
            double curInst = CommonUtil.convertObjToDouble(getTxtPaidNoOfInst());
            for (int i = 0; i < splitTransInstList.size(); i++) {
                splitMDSClosedReceiptEntryTO = setMDSReceiptEntryTO();
                double instAmt = 0, bonusAmt = 0, penalAmt = 0;
                String splitNarration = "";
                instAmt = CommonUtil.convertObjToDouble(splitTransInstList.get(i));
                bonusAmt = CommonUtil.convertObjToDouble(bonusAmountList.get(i));
                penalAmt = CommonUtil.convertObjToDouble(penalList.get(i));
                splitNarration = CommonUtil.convertObjToStr(narrationList.get(i));
                splitMDSClosedReceiptEntryTO.setBonusAmtPayable(bonusAmt);
                splitMDSClosedReceiptEntryTO.setCurrInst(++curInst);
                splitMDSClosedReceiptEntryTO.setNarration(splitNarration);
                splitMDSClosedReceiptEntryTO.setPenalAmtPayable(penalAmt);
                splitMDSClosedReceiptEntryTO.setInstAmtPayable(instAmt);
                splitMDSClosedReceiptEntryTO.setNetAmt(instAmt + penalAmt);
                splitMDSReceiptEntryLst.add(splitMDSClosedReceiptEntryTO);
                if (noticeAmounts > 0) {
                    splitMDSClosedReceiptEntryTO.setNoticeAmt(noticeAmounts);
                } else {
                    splitMDSClosedReceiptEntryTO.setNoticeAmt(0.0);
                }
                noticeAmounts = 0.0;
                if (arbitrationAmounts > 0) {
                    splitMDSClosedReceiptEntryTO.setArbitrationAmt(arbitrationAmounts);
                } else {
                    splitMDSClosedReceiptEntryTO.setArbitrationAmt(0.0);
                }
                arbitrationAmounts = 0.0;
                if (serviceTaxAmt > 0) {
                    splitMDSClosedReceiptEntryTO.setServiceTaxAmt(serviceTaxAmt);
                } else {
                    splitMDSClosedReceiptEntryTO.setServiceTaxAmt(0.0);
                }
                serviceTaxAmt = 0.0;
            }
        }
    }

}
    public HashMap getMDSClosedTransListMap() {
        return MDSClosedTransListMap;
    }
    public void issplitTransaction() {
        if (MDSClosedTransListMap.containsKey("IS_SPLIT_MDS_TRANSACTION") && MDSClosedTransListMap.get("IS_SPLIT_MDS_TRANSACTION") != null) {
            if (MDSClosedTransListMap.get("IS_SPLIT_MDS_TRANSACTION").equals("Y")) {
                splitMDSClosedReceiptEntryTO = setMDSReceiptEntryTO();
                doSplitMDSTransaction();
            }
        }
    }
    public void setMDSClosedTransListMap(HashMap MDSClosedTransListMap) {
        this.MDSClosedTransListMap = MDSClosedTransListMap;
        issplitTransaction();
    }

    public String getInstalmntFreq() {
        return instalmntFreq;
    }

    public void setInstalmntFreq(String instalmntFreq) {
        this.instalmntFreq = instalmntFreq;
    }

    public String getTxtForfeitBonus() {
        return txtForfeitBonus;
    }

    public void setTxtForfeitBonus(String txtForfeitBonus) {
        this.txtForfeitBonus = txtForfeitBonus;
    }

    public String getBankAdvAmt() {
        return bankAdvAmt;
    }

    public void setBankAdvAmt(String bankAdvAmt) {
        this.bankAdvAmt = bankAdvAmt;
    }       

    public HashMap getReturnWaiveMap() {
        return returnWaiveMap;
    }

    public void setReturnWaiveMap(HashMap returnWaiveMap) {
        this.returnWaiveMap = returnWaiveMap;
    }

    public HashMap getInstallGraceDate() {
        return installGraceDate;
    }

    public void setInstallGraceDate(HashMap installGraceDate) {
        this.installGraceDate = installGraceDate;
    }
    
    
}