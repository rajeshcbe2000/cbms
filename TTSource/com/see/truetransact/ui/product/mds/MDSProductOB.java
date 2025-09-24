/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * MDSProductOB.java
 *
 * Created on January 7, 2004, 5:14 PM
 */

package com.see.truetransact.ui.product.mds;

/**
 *
 * @author Sathiya
 *
 */

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.transferobject.product.mds.MDSProductTO;
import com.see.truetransact.transferobject.product.mds.MDSProductAcctHeadTO;
import com.see.truetransact.transferobject.product.mds.MDSProductOtherDetailsTO;
import java.util.Date;

public class MDSProductOB extends CObservable {
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    java.util.ResourceBundle objMDSProductRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.product.mds.MDSProductRB", ProxyParameters.LANGUAGE);
    
    
    private HashMap operationMap;
    private ProxyFactory proxy;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList value;
    private int actionType;
    private ArrayList key;
    private HashMap hash;
    private int result;
    
    
    private MDSProductOtherDetailsTO mdsProductOtherDetailsTO = null;
    private MDSProductAcctHeadTO mdsProductAcctHeadTO = null;
    private MDSProductTO mdsProductTO = null;
    
    private ComboBoxModel cbmholidayInt,cbmCommisionRate,cbmDiscountRate,cbmPenalIntRate,cbmPenalPrizedRate,cbmLoanIntRate,cbmBonusGracePeriod,cbmBonusPrizedPreriod,cbmPaymentTimeCharges,
    cbmPenalPeriod,cbmPenalPrizedPeriod,cbmPenalCalc,cbmDiscountPeriod,cbmDiscountPrizedPeriod,cbmRoundOffCriteria,cbmPenalIntRateFullInstAmt,cbmPenalPrizedIntRateFullInstAmt;
    
    
    
    private String cboholidayInt = "";
    private String txtProductId = "";
    private String lblProductDescVal = "";
    private String txtActHead = "";
    
    private boolean chkAcceptClassA = false;
    private boolean chkAcceptClassB = false;
    private boolean chkAcceptClassC = false;
    private boolean chkAcceptClassAll = false;
    private boolean chkFromAuctionEnrtry = false;
    private boolean chkAfterCashPayment = false;
    private boolean chkSplitMDSTransaction = false;
    
    private boolean chkAcceptableClassA = false;
    private boolean chkAcceptableClassB = false;
    private boolean chkAcceptableClassC = false;
    private boolean chkAcceptableClassAll = false;
    
    private Double txtAuctionMaxAmt = 0.0;  //AJITH
    private Double txtAuctionMinAmt = 0.0;  //AJITH
    
    private String txtReceiptHead = "";
    private String txtPaymentHead = "";
    private String txtSuspenseHead = "";
    private String txtMiscellaneousHead = "";
    private String txtCommisionHead = "";
    private String txtBonusPayableHead = "";
    private String txtBonusReceivableHead = "";
    private String txtPenalHead = "";
    private String txtThalayalReceiptsHead = "";
    private String txtThalayalBonusHead = "";
    private String txtMunnalBonusHead = "";
    private String txtMunnalReceiptsHead = "";
    private String txtBankingHead = "";
    private String txtNoticeChargesHead = "";
    private String txtCaseExpensesHead = "";
    
    private boolean rdoOnlyMembers_yes = false;
    private boolean rdoOnlyMembers_no = false;
    private boolean rdoSuretyRequired_yes = false;
    private boolean rdoSuretyRequired_no = false;
    private boolean rdoShortfall_yes = false;
    private boolean rdoShortfall_no = false;
    private boolean rdoChitDefaults_yes = false;
    private boolean rdoChitDefaults_no = false;
    private boolean rdoNonPrizedChit_yes = false;
    private boolean rdoNonPrizedChit_no = false;
    private boolean rdoBonusRecoveredExistingChittal_yes = false;
    private boolean rdoBonusRecoveredExistingChittal_no = false;
    private boolean rdoBonusAllowed_yes = false;
    private boolean rdoBonusAllowed_no = false;
    private boolean rdobonusPayableOwner_yes = false;
    private boolean rdobonusPayableOwner_no = false;
    private boolean rdoPrizedChitOwner_yes = false;
    private boolean rdoPrizedChitOwner_no = false;
    private boolean rdoPrizedChitOwnerAfter_yes = false;
    private boolean rdoPrizedChitOwnerAfter_no = false;
    private boolean rdoprizedDefaulters_yes = false;
    private boolean rdoprizedDefaulters_no = false;
    private boolean rdoBonusAmt_Yes = false;
    private boolean rdoBonusAmt_No = false;
    private boolean rdoBonusPayFirstIns_Yes = false;
    private boolean rdoBonusPayFirstIns_No = false;
    private boolean rdoAdvanceCollection_Yes = false;
    private boolean rdoAdvanceCollection_NO = false;
    private boolean rdoTransFirstIns_Yes = false;
    private boolean rdoTransFirstIns_No = false;
    private String rdoPending="";
    private String rdoMethod1="";
    private String rdoMethod2="";
    private Double txtpaymentTimeCharges = 0.0;    //AJITH
    private Date currDt = null;
    private String chkIsGDS = "";
    private String chkIsBonusTrans = "";

    public String getChkIsBonusTrans() {
        return chkIsBonusTrans;
    }

    public void setChkIsBonusTrans(String chkIsBonusTrans) {
        this.chkIsBonusTrans = chkIsBonusTrans;
    }

    public String getChkIsGDS() {
        return chkIsGDS;
    }

    public void setChkIsGDS(String chkIsGDS) {
        this.chkIsGDS = chkIsGDS;
    }
    
    
    public String getRdoMethod1() {
        return rdoMethod1;
    }

    public void setRdoMethod1(String rdoMethod1) {
        this.rdoMethod1 = rdoMethod1;
    }

    public String getRdoMethod2() {
        return rdoMethod2;
    }

    public void setRdoMethod2(String rdoMethod2) {
        this.rdoMethod2 = rdoMethod2;
    }

    public String getRdoPending() {
        return rdoPending;
    }

    public void setRdoPending(String rdoPending) {
        this.rdoPending = rdoPending;
    }

    public String getRdoPrized() {
        return rdoPrized;
    }

    public void setRdoPrized(String rdoPrized) {
        this.rdoPrized = rdoPrized;
    }

    public String getRdoWhichEverIsLess() {
        return rdoWhichEverIsLess;
    }

    public void setRdoWhichEverIsLess(String rdoWhichEverIsLess) {
        this.rdoWhichEverIsLess = rdoWhichEverIsLess;
    }
    
    private String rdoPrized="";
    private String rdoWhichEverIsLess = "";
    private String cboCommisionRate = "";
    private String cboDiscountRate = "";
    private String cboPenalIntRate = "";
    private String cboPenalPrizedRate = "";
    private String cboLoanIntRate = "";
    private String cboBonusGracePeriod = "";
    private String cboBonusPrizedPreriod = "";
    private String cboPenalPeriod = "";
    private String cboPenalPrizedPeriod = "";
    private String cboPenalCalc = "";
    private String cboDiscountPeriod = "";
    private String cboDiscountPrizedPeriod = "";
    private String cboRoundOffCriteria = "";
    private String cboPenalIntRateFullInstAmt = "";
    private String cboPenalPrizedIntRateFullInstAmt = "";
    
    private Double txtCommisionRate = 0.0; //AJITH Changed from String
    private Double txtDiscountRate = 0.0; //AJITH Changed from String
    private Double txtPenalIntRate = 0.0; //AJITH Changed from String
    private Double txtPenalPrizedRate = 0.0; //AJITH Changed from String
    private Double txtLoanIntRate = 0.0; //AJITH Changed from String
    private Double txtBonusGracePeriod = 0.0; //AJITH Changed from String
    private Double txtBonusPrizedPreriod = 0.0; //AJITH Changed from String
    private Double txtPenalPeriod = 0.0; //AJITH Changed from String
    private Double txtPenalPrizedPeriod = 0.0; //AJITH Changed from String
    private Double txtDiscountPeriod = 0.0; //AJITH Changed from String
    private Double txtDiscountPrizedPeriod = 0.0; //AJITH Changed from String
    private Double txtMargin = 0.0; //AJITH Changed from String
    private Double txtMinLoanAmt = 0.0; //AJITH Changed from String
    private Double txtMaxLoanAmt = 0.0; //AJITH Changed from String
    
    private boolean rdoBonusGraceDays = false;
    private boolean rdoBonusGraceMonth = false;
    private boolean rdoBonusGraceAfter = false;
    private boolean rdoBonusGraceEnd = false;

    private boolean rdoBonusPrizedDays = false;
    private boolean rdoBonusPrizedMonth = false;
    private boolean rdoBonusPrizedAfter = false;
    private boolean rdoBonusPrizedEnd = false;

    private boolean rdoDiscountPeriodDays = false;
    private boolean rdoDiscountPeriodMonth = false;
    private boolean rdoDiscountPeriodAfter = false;
    private boolean rdoDiscountPeriodEnd = false;

    private boolean rdoDiscountPrizedPeriodDays = false;
    private boolean rdoDiscountPrizedPeriodMonth = false;
    private boolean rdoDiscountPrizedPeriodAfter = false;
    private boolean rdoDiscountPrizedPeriodEnd = false;
    
    private boolean rdoDiscountAllowed_yes = false;
    private boolean rdoDiscountAllowed_no = false;
    private boolean rdoLoanCanbeGranted_yes = false;
    private boolean rdoLoanCanbeGranted_no = false;
    private String cboPaymentTimeCharges = "";
    
    private String cboMunnalTransCategory = "";
    private String cboThalayalTransCategory = "";
    private ComboBoxModel cbmMunnalTransCategory;
    private ComboBoxModel cbmThalayalTransCategory;
    
    
        
    private static MDSProductOB mdsProductOB;
    static {
        try {
            mdsProductOB = new MDSProductOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /* Sets the HashMap required to set JNDI,Home and Remote*/
    public MDSProductOB() throws Exception{
        currDt = ClientUtil.getCurrentDate();
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "MDSProductJNDI");
        operationMap.put(CommonConstants.HOME, "product.mds.MDSProdutHome");
        operationMap.put(CommonConstants.REMOTE, "product.mds.MDSProdut");
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        notifyObservers();
        fillDropdown();
    }
    
    public static MDSProductOB getInstance() {
        return mdsProductOB;
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
            lookup_keys.add("MDS_PERIOD");
            lookup_keys.add("ADVANCESPRODUCT.CHARGETYPE");
            lookup_keys.add("MDS_GRACE_PEROID");
            lookup_keys.add("REMITTANCE_PROD.CHARGE");
            lookup_keys.add("CATEGORY");
            lookup_keys.add("DEPOSIT.AMT_RANGE_FROM");
            lookup_keys.add("DEPOSIT.AMT_RANGE");
            lookup_keys.add("REMITTANCE.BEHAVES");
            lookup_keys.add("MDS_HOLIDAY_INST");
            lookup_keys.add("OPERATIVEACCTPRODUCT.INTROUNDOFF");
            lookup_keys.add("PENELINT_FULLAMT_INSTAMT");
            lookup_keys.add("MDSPRODUCT.CHARGETYPE");
            lookup_keys.add("BANK_AMT_TRANS_CATEGORY");
            
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookUpHash);
            lookUpHash = null;
            
            getKeyValue((HashMap)keyValue.get("MDS_HOLIDAY_INST"));
            cbmholidayInt = new ComboBoxModel(key,value);            
            
            getKeyValue((HashMap)keyValue.get("ADVANCESPRODUCT.CHARGETYPE"));
            cbmCommisionRate = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("ADVANCESPRODUCT.CHARGETYPE"));
            cbmDiscountRate = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("ADVANCESPRODUCT.CHARGETYPE"));
            cbmPenalIntRate = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("ADVANCESPRODUCT.CHARGETYPE"));
            cbmPenalPrizedRate = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("ADVANCESPRODUCT.CHARGETYPE"));
            cbmLoanIntRate = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("MDS_GRACE_PEROID"));
            cbmBonusGracePeriod = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("MDS_GRACE_PEROID"));
            cbmBonusPrizedPreriod = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("MDS_PERIOD"));
            cbmPenalPeriod = new ComboBoxModel(key,value);
            getKeyValue((HashMap) keyValue.get("PENELINT_FULLAMT_INSTAMT"));
            cbmPenalIntRateFullInstAmt = new ComboBoxModel(key, value);
            
            getKeyValue((HashMap) keyValue.get("PENELINT_FULLAMT_INSTAMT"));
            cbmPenalPrizedIntRateFullInstAmt = new ComboBoxModel(key, value);
            getKeyValue((HashMap)keyValue.get("MDS_PERIOD"));
            cbmPenalPrizedPeriod = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("MDS_PERIOD"));
            cbmPenalCalc = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("MDS_GRACE_PEROID"));
            cbmDiscountPeriod = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("MDS_GRACE_PEROID"));
            cbmDiscountPrizedPeriod = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("MDSPRODUCT.CHARGETYPE"));
            cbmPaymentTimeCharges = new ComboBoxModel(key,value);

            getKeyValue((HashMap)keyValue.get("OPERATIVEACCTPRODUCT.INTROUNDOFF"));
            cbmRoundOffCriteria = new ComboBoxModel(key,value);
            cbmRoundOffCriteria.removeKeyAndElement("NO_ROUND_OFF");
            cbmRoundOffCriteria.removeKeyAndElement("HIGHER_VALUE");
            
            getKeyValue((HashMap)keyValue.get("BANK_AMT_TRANS_CATEGORY"));
            cbmThalayalTransCategory = new ComboBoxModel(key,value);
            cbmMunnalTransCategory = new ComboBoxModel(key,value);
            
            keyValue = null;
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
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
        }
    }
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        boolean deleteRemitProdBrchDataExists = false;
        boolean deleteRemitProdChrgDataExists = false;
        final HashMap data = new HashMap();
        ArrayList remitProdBrch = new ArrayList();
        ArrayList arrayRemitProdBrchTabTO = new ArrayList();
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        data.put("COMMAND",getCommand());
        if(getActionType() != ClientConstants.ACTIONTYPE_DELETE ){
            mdsProductTO = setMDSProductTO();
            mdsProductAcctHeadTO = setMDSProductAcctHeadTO();
            mdsProductOtherDetailsTO = setMDSProductOtherDetailsTO();
            if(data.get("COMMAND").equals("INSERT")){
                mdsProductTO.setStatus(CommonConstants.STATUS_CREATED);
            }else if(data.get("COMMAND").equals("UPDATE")){
                mdsProductTO.setStatus(CommonConstants.STATUS_MODIFIED);
            }else{
                mdsProductTO.setStatus(CommonConstants.STATUS_DELETED);
            }
            mdsProductTO.setStatusBy(TrueTransactMain.USER_ID);
            mdsProductTO.setStatusDt(currDt);
            data.put("mdsProductTO",mdsProductTO);
            data.put("mdsProductAcctHeadTO",mdsProductAcctHeadTO);
            data.put("mdsProductOtherDetailsTO",mdsProductOtherDetailsTO);
            HashMap proxyResultMap = proxy.execute(data, operationMap);
            System.out.println("#$%#$#%$proxyMap"+proxyResultMap);
            setProxyReturnMap(proxyResultMap);
            arrayRemitProdBrchTabTO.clear();
            setResult(getActionType());
            
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
    
    public void resetForm(){
        setTxtProductId("");
        setLblProductDescVal(""); 
        setTxtActHead("");
        setTxtAuctionMaxAmt(0.0);   //AJITH
        setTxtAuctionMinAmt(0.0);   //AJITH
        setTxtReceiptHead("");
        setTxtPaymentHead("");
        setTxtSuspenseHead("");
        setTxtMiscellaneousHead("");
        setTxtCommisionHead("");
        setTxtBonusPayableHead("");
        setTxtBonusReceivableHead("");
        setTxtPenalHead("");
        setTxtThalayalReceiptsHead("");
        setTxtThalayalBonusHead("");
        setTxtMunnalBonusHead("");
        setTxtMunnalReceiptsHead("");
        setTxtBankingHead("");
        setTxtNoticeChargesHead("");
        setTxtCaseExpensesHead("");

        setRdoOnlyMembers_yes(false);
        setRdoOnlyMembers_no(false);
        setRdoSuretyRequired_yes(false);
        setRdoSuretyRequired_no(false);
        setRdoShortfall_yes(false);
        setRdoShortfall_no(false);
        setRdoChitDefaults_yes(false);
        setRdoChitDefaults_no(false);
        setRdoNonPrizedChit_yes(false);
        setRdoNonPrizedChit_no(false);
        setRdoBonusRecoveredExistingChittal_yes(false);
        setRdoBonusRecoveredExistingChittal_no(false);
        setRdoBonusAllowed_yes(false);
        setRdoBonusAllowed_no(false);
        setRdobonusPayableOwner_yes(false);
        setRdobonusPayableOwner_no(false);
        setRdoPrizedChitOwner_yes(false);
        setRdoPrizedChitOwner_no(false);
        setRdoPrizedChitOwnerAfter_yes(false);
        setRdoPrizedChitOwnerAfter_no(false);
        setRdoprizedDefaulters_yes(false);
        setRdoprizedDefaulters_no(false);
        setRdoBonusAmt_Yes(false);
        setRdoBonusAmt_No(false);
        setRdoBonusPayFirstIns_Yes(false);
        setRdoBonusPayFirstIns_No(false);
        setRdoAdvanceCollection_Yes(false);
        setRdoAdvanceCollection_NO(false);
        setCboholidayInt("");
        setCboCommisionRate("");
        setCboDiscountRate("");
        setCboPenalIntRate("");
        setCboPenalPrizedRate("");
        setCboLoanIntRate("");
        setCboBonusGracePeriod("");
        setCboBonusPrizedPreriod("");
        setCboPenalPeriod("");
        setCboPenalPrizedPeriod("");
        setCboPenalCalc("");
        setCboDiscountPeriod("");
        setCboDiscountPrizedPeriod("");
        setCboRoundOffCriteria("");

        setTxtCommisionRate(0.0);  //AJITH Changed from ""
        setTxtDiscountRate(0.0);  //AJITH Changed from ""
        setTxtpaymentTimeCharges(0.0);  //AJITH Changed from ""
        setCboPaymentTimeCharges("");
        setTxtPenalIntRate(0.0);  //AJITH Changed from ""
        setTxtPenalPrizedRate(0.0);  //AJITH Changed from ""
        setTxtLoanIntRate(0.0);  //AJITH Changed from ""
        setTxtBonusGracePeriod(0.0);  //AJITH Changed from ""
        setTxtBonusPrizedPreriod(0.0);  //AJITH Changed from ""
        setTxtPenalPeriod(0.0);  //AJITH Changed from ""
        setTxtPenalPrizedPeriod(0.0);  //AJITH Changed from ""
        setTxtDiscountPeriod(0.0);  //AJITH Changed from ""
        setTxtDiscountPrizedPeriod(0.0);  //AJITH Changed from ""
        setTxtMargin(0.0);  //AJITH Changed from ""
        setTxtMinLoanAmt(0.0);  //AJITH Changed from ""
        setTxtMaxLoanAmt(0.0);  //AJITH Changed from ""
        
        setChkAfterCashPayment(false);
        setChkFromAuctionEnrtry(false);
        setChkSplitMDSTransaction(false);
        setChkAcceptableClassA(false);
        setChkAcceptableClassB(false);
        setChkAcceptableClassC(false);
        setChkAcceptableClassAll(false);
        setRdoSuretyRequired_yes(false);
        setChkAcceptClassA(false);
        setChkAcceptClassB(false);
        setChkAcceptClassC(false);
        setChkAcceptClassAll(false);
        setRdoDiscountAllowed_yes(false);
        setRdoDiscountAllowed_no(false);
        setRdoLoanCanbeGranted_yes(false);
        setRdoLoanCanbeGranted_no(false);
        
        
        setRdoBonusGraceDays(false);
        setRdoBonusGraceMonth(false);
        setRdoBonusGraceAfter(false);
        setRdoBonusGraceEnd(false);

        setRdoBonusPrizedDays(false);
        setRdoBonusPrizedMonth(false);
        setRdoBonusPrizedAfter(false);
        setRdoBonusPrizedEnd(false);

        setRdoDiscountPrizedPeriodDays(false);
        setRdoDiscountPrizedPeriodMonth(false);
        setRdoDiscountPrizedPeriodAfter(false);
        setRdoDiscountPrizedPeriodEnd(false);

        setRdoDiscountPeriodDays(false);
        setRdoDiscountPeriodMonth(false);
        setRdoDiscountPeriodAfter(false);
        setRdoDiscountPeriodEnd(false);   
        
        setChkIsBonusTrans("");
        setChkIsGDS("");
        
        setCboThalayalTransCategory("");
        setCboMunnalTransCategory("");
    }
    
    private MDSProductTO setMDSProductTO(){
        mdsProductTO = new MDSProductTO();
        mdsProductTO.setProdId(getTxtProductId());
        mdsProductTO.setProdDesc(getLblProductDescVal());
//        mdsProductTO.setAcctHead(getTxtActHead());
	if(getRdoOnlyMembers_yes() == true){
            mdsProductTO.setOnlyMember("Y");
        }else{
            mdsProductTO.setOnlyMember("N");
        }        
        if(getChkAcceptableClassA() == true){
            mdsProductTO.setAcceptClassA("A");            
        }
        if(getChkAcceptableClassB() == true){
            mdsProductTO.setAcceptClassB("B");            
        }
        if(getChkAcceptableClassC() == true){
            mdsProductTO.setAcceptClassC("C");            
        }
        if(getChkAcceptableClassAll() == true){
            mdsProductTO.setAcceptClassAll("D");            
        }
        if(getRdoSuretyRequired_yes() == true){
            mdsProductTO.setSurety("Y");            
        }else{
            mdsProductTO.setSurety("N");            
        }
        if(getChkAcceptClassA()){
            mdsProductTO.setAcceptableClassA("A");            
        } 
        if(getChkAcceptClassB()){
            mdsProductTO.setAcceptableClassB("B");            
        } 
        if(getChkAcceptClassC()){
            mdsProductTO.setAcceptableClassC("C");            
        }
        if(getChkAcceptClassAll()){
            mdsProductTO.setAcceptableClassAll("D");            
        }
        if(getRdoShortfall_yes()){
            mdsProductTO.setShortFall("Y");
        }else{
            mdsProductTO.setShortFall("N");
        }
        if(getRdoChitDefaults_yes()){
            mdsProductTO.setDefaultChitOwners("Y");
        }else{
            mdsProductTO.setDefaultChitOwners("N");
        }
        if(getRdoNonPrizedChit_yes()){
            if(getRdoBonusRecoveredExistingChittal_yes()){
                mdsProductTO.setBonusExistingChit("Y");
            }else{
                mdsProductTO.setBonusExistingChit("N");
            }
            mdsProductTO.setNonPrizedChange("Y");
        }else{
            mdsProductTO.setNonPrizedChange("N");
            mdsProductTO.setBonusExistingChit("");
        }
        if(getRdoBonusAmt_Yes()){
            mdsProductTO.setBonusRounding("Y");
        }else{
            mdsProductTO.setBonusRounding("N");
        }
         if(getRdoBonusPayFirstIns_Yes()){
            mdsProductTO.setBonusPayForFirstIns("Y");
        }else{
            mdsProductTO.setBonusPayForFirstIns("N");
        }
         if(isRdoTransFirstIns_Yes())
         {
             mdsProductTO.setTransForFirstIns("Y");
         }
        if(isRdoTransFirstIns_No())
        {
            mdsProductTO.setTransForFirstIns("N");
        }
        if(getRdoAdvanceCollection_Yes()){
            mdsProductTO.setAdvanceCollection("Y");
        }else{
            mdsProductTO.setAdvanceCollection("N");
        }
        mdsProductTO.setAuctionMaxamt(getTxtAuctionMaxAmt());   //AJITH
        mdsProductTO.setAuctionMinamt(getTxtAuctionMinAmt());   //AJITH
        if(getRdoBonusAllowed_yes()){
            mdsProductTO.setBonusAllowed("Y");
        }else{
            mdsProductTO.setBonusAllowed("N");
        }
        if(getRdobonusPayableOwner_yes()){
            mdsProductTO.setPrizedOwnerBonus("Y");
        }else{
            mdsProductTO.setPrizedOwnerBonus("N");
        }
        if(getRdoPrizedChitOwner_yes()){
            mdsProductTO.setAfterAuctionEligible("Y");
        }else{
            mdsProductTO.setAfterAuctionEligible("N");
        }
        if(getRdoPrizedChitOwnerAfter_yes()){
            mdsProductTO.setAfterPaymentEligible("Y");
        }else{
            mdsProductTO.setAfterPaymentEligible("N");
        }
        if(getRdoprizedDefaulters_yes()){
            mdsProductTO.setPrizedDefaulters("Y");
        }else{
            mdsProductTO.setPrizedDefaulters("N");
        }
        System.out.println("getRdoPendinggetRdoPending"+getRdoPending()+"   "+getRdoPrized());
        if(getRdoPending().equals("Y"))
        {
           mdsProductTO.setRdoPending("Y");
          
        }
        else
        {
          
            mdsProductTO.setRdoPending("N"); 
        }
        if(getRdoPrized().equals("Y"))
        {
              mdsProductTO.setRdoPrized("Y");
        }
        else
        {
              mdsProductTO.setRdoPrized("N");
            
        }
        if(getRdoWhichEverIsLess().equals("Y"))
        {
              mdsProductTO.setRdoPrizdOrInstlAmtIsLess("Y");
        }
        else
        {
              mdsProductTO.setRdoPrizdOrInstlAmtIsLess("N");
            
        }
        
        if(getRdoMethod1().equals("Y"))
        {
            mdsProductTO.setRdoMethod1("Y");
        }
        else
        {
           mdsProductTO.setRdoMethod1("N");  
        }
        
        
        if(getRdoMethod2().equals("Y"))
        {
            mdsProductTO.setRdoMethod2("Y");
        }
        else
        {
           mdsProductTO.setRdoMethod2("N");  
        }
        if (isChkAfterCashPayment()) {
            mdsProductTO.setAfterCashPayment("Y");
        }else{
            mdsProductTO.setAfterCashPayment("N");
        }
        if (isChkFromAuctionEnrtry()) {
            mdsProductTO.setFromAuctionEnrtry("Y");
        } else {
            mdsProductTO.setFromAuctionEnrtry("N");
        }
        if (isChkSplitMDSTransaction()) {
            mdsProductTO.setSplitMDSTransaction("Y");
        } else {
            mdsProductTO.setSplitMDSTransaction("N");
        }        
        if(getChkIsGDS().equalsIgnoreCase("Y")){
           mdsProductTO.setIsGDS("Y"); 
        }else{
           mdsProductTO.setIsGDS("N"); 
        }
        
        if(chkIsBonusTrans.equalsIgnoreCase("Y")){
            mdsProductTO.setIsBonusTrans("Y");
        }else{
            mdsProductTO.setIsBonusTrans("N");
        }
        
        System.out.println("mdsProductTO.. in obbbbeeyyy"+mdsProductTO.getRdoPending()+"...."+mdsProductTO.getRdoPrized());
        mdsProductTO.setRoundoffCriteria((String)cbmRoundOffCriteria.getKeyForSelected());
        mdsProductTO.setHolidayInt(getCboholidayInt());
        return mdsProductTO;
    }
    
    private MDSProductAcctHeadTO setMDSProductAcctHeadTO(){
        mdsProductAcctHeadTO = new MDSProductAcctHeadTO();
        mdsProductAcctHeadTO.setProdId(getTxtProductId());
        mdsProductAcctHeadTO.setReceiptHead(getTxtReceiptHead());
        mdsProductAcctHeadTO.setPaymentHead(getTxtPaymentHead());
        mdsProductAcctHeadTO.setSuspenseHead(getTxtSuspenseHead());
        mdsProductAcctHeadTO.setMiscellaneousHead(getTxtMiscellaneousHead());
        mdsProductAcctHeadTO.setCommisionHead(getTxtCommisionHead());
        mdsProductAcctHeadTO.setBonusPayableHead(getTxtBonusPayableHead());
        mdsProductAcctHeadTO.setBonusReceivableHead(getTxtBonusReceivableHead());
        mdsProductAcctHeadTO.setPenalInterestHead(getTxtPenalHead());
        mdsProductAcctHeadTO.setThalayalRepPayHead(getTxtThalayalReceiptsHead());
        mdsProductAcctHeadTO.setThalayalBonusHead(getTxtThalayalBonusHead());
        mdsProductAcctHeadTO.setMunnalBonusHead(getTxtMunnalBonusHead());
        mdsProductAcctHeadTO.setMunnalRepPayHead(getTxtMunnalReceiptsHead());
        mdsProductAcctHeadTO.setBankingRepPayHead(getTxtBankingHead());
        mdsProductAcctHeadTO.setNoticeChargesHead(getTxtNoticeChargesHead());
        mdsProductAcctHeadTO.setCaseExpenseHead(getTxtCaseExpensesHead());
        return mdsProductAcctHeadTO;
    }
    
    private MDSProductOtherDetailsTO setMDSProductOtherDetailsTO(){
        mdsProductOtherDetailsTO = new MDSProductOtherDetailsTO();
        mdsProductOtherDetailsTO.setProdId(getTxtProductId());
        mdsProductOtherDetailsTO.setCommisionRateAmt(getTxtCommisionRate());    //AJITH
        mdsProductOtherDetailsTO.setCommisionRateType(getCboCommisionRate());
        mdsProductOtherDetailsTO.setDiscountRateAmt(getTxtDiscountRate());  //AJITH
        mdsProductOtherDetailsTO.setDiscountRateType(getCboDiscountRate());
        mdsProductOtherDetailsTO.setPenalIntAmt(getTxtPenalIntRate());  //AJITH
        mdsProductOtherDetailsTO.setPenalIntType(getCboPenalIntRate());
        mdsProductOtherDetailsTO.setPenalPrizedIntAmt(getTxtPenalPrizedRate());  //AJITH
        mdsProductOtherDetailsTO.setPenalPrizedIntType(getCboPenalPrizedRate());
        mdsProductOtherDetailsTO.setLoanIntAmt(getTxtLoanIntRate());    //AJITH
        mdsProductOtherDetailsTO.setLoanIntType(getCboLoanIntRate());
        mdsProductOtherDetailsTO.setBonusGracePeriod(getTxtBonusGracePeriod());  //AJITH
        mdsProductOtherDetailsTO.setBonusPrizedGracePeriod(getTxtBonusPrizedPreriod());  //AJITH
        mdsProductOtherDetailsTO.setPenalGracePeriod(getTxtPenalPeriod());  //AJITH
        mdsProductOtherDetailsTO.setPenalGracePeriodType(getCboPenalPeriod());
        mdsProductOtherDetailsTO.setPenalPrizedGracePeriod(getTxtPenalPrizedPeriod());  //AJITH
        mdsProductOtherDetailsTO.setPenalPrizedGracePeriodType(getCboPenalPrizedPeriod());
        mdsProductOtherDetailsTO.setPenalCalc(getCboPenalCalc());
        mdsProductOtherDetailsTO.setPenalIntRateFullInstAmt(getCboPenalIntRateFullInstAmt());
        mdsProductOtherDetailsTO.setPenalPrizedIntRateFullInstAmt(getCboPenalPrizedIntRateFullInstAmt());
        mdsProductOtherDetailsTO.setPriizedMoneyPaymentAmt(getTxtpaymentTimeCharges());  //AJITH
        mdsProductOtherDetailsTO.setPriizedMoneyPaymentType(getCboPaymentTimeCharges());
        if(getRdoBonusGraceDays() == true){
            mdsProductOtherDetailsTO.setBonusGracePeriodDays("D");            
        }else if(getRdoBonusGraceMonth() == true){
            mdsProductOtherDetailsTO.setBonusGracePeriodMonths("M");
        }else if(getRdoBonusGraceDays() == true){
            mdsProductOtherDetailsTO.setBonusGracePeriodAfter("D");
        }else if(getRdoBonusGraceEnd() == true){
            mdsProductOtherDetailsTO.setBonusGracePeriodEnd("E");
        }        
        if(getRdoBonusPrizedDays() == true){
            mdsProductOtherDetailsTO.setBonusPrizedGracePeriodDays("D");
        }else if(getRdoBonusPrizedMonth() == true){
            mdsProductOtherDetailsTO.setBonusPrizedGracePeriodMnth("M");
        }else if(getRdoBonusPrizedAfter() == true){
            mdsProductOtherDetailsTO.setBonusPrizedGracePeriodAft("A");
        }else if(getRdoBonusPrizedEnd() == true){
            mdsProductOtherDetailsTO.setBonusPrizedGracePeriodEnd("E");
        }
        if(getRdoDiscountAllowed_yes() == true){
            mdsProductOtherDetailsTO.setDiscountAllowed("Y");
        }else{
            mdsProductOtherDetailsTO.setDiscountAllowed("N");
        }
        if(getRdoDiscountPeriodEnd() == true){
            mdsProductOtherDetailsTO.setDisGracePeriodDays("E");
        }else if(getRdoDiscountPeriodAfter() == true){
            mdsProductOtherDetailsTO.setDisGracePeriodDays("A");
        }else if(getRdoDiscountPeriodMonth() == true){
            mdsProductOtherDetailsTO.setDisGracePeriodDays("M");
        }else if(getRdoDiscountPeriodDays() == true){
            mdsProductOtherDetailsTO.setDisGracePeriodDays("D");
        }
        mdsProductOtherDetailsTO.setDisGracePeriod(getTxtDiscountPeriod());  //AJITH
        if(getRdoDiscountPrizedPeriodDays() == true){
            mdsProductOtherDetailsTO.setDisPrizedGracePeriodDays("D");
        }else if(getRdoDiscountPrizedPeriodMonth() == true){
            mdsProductOtherDetailsTO.setDisPrizedGracePeriodMonths("M");
        }else if(getRdoDiscountPrizedPeriodAfter() == true){
            mdsProductOtherDetailsTO.setDisPrizedGracePeriodAfter("A");
        }else if(getRdoDiscountPrizedPeriodEnd() == true){
            mdsProductOtherDetailsTO.setDisPrizedGracePeriodEnd("E");
        }
        if(getRdoDiscountPrizedPeriodEnd() == true){
            mdsProductOtherDetailsTO.setDisPrizedGracePeriod(CommonUtil.convertObjToDouble(""));
        }else{
            mdsProductOtherDetailsTO.setDisPrizedGracePeriod(getTxtDiscountPrizedPeriod()); //AJITH
        }
        if(getRdoLoanCanbeGranted_yes() == true){
            mdsProductOtherDetailsTO.setLoanAllowed("Y");
            mdsProductOtherDetailsTO.setLoanMargin(getTxtMargin());  //AJITH
            mdsProductOtherDetailsTO.setMinLoanAmt(getTxtMinLoanAmt()); //AJITH
            mdsProductOtherDetailsTO.setMaxLoanAmt(getTxtMaxLoanAmt()); //AJITH
        }else{
            mdsProductOtherDetailsTO.setLoanAllowed("N");
            mdsProductOtherDetailsTO.setLoanMargin(CommonUtil.convertObjToDouble(null));
            mdsProductOtherDetailsTO.setMinLoanAmt(CommonUtil.convertObjToDouble(null));
            mdsProductOtherDetailsTO.setMaxLoanAmt(CommonUtil.convertObjToDouble(null));
        }
        mdsProductOtherDetailsTO.setThalayalBankTransferCategory(getCboThalayalTransCategory());
        mdsProductOtherDetailsTO.setMunnalBankTransferCategory(getCboMunnalTransCategory());
        return mdsProductOtherDetailsTO;
    }
    
    private boolean populateOB(HashMap mapData) {
        boolean aliasBranchTableFlag = false;
        ArrayList remitProdBrchTOArrayList  = new ArrayList();
        MDSProductTO mdsProductTO;
        MDSProductAcctHeadTO mdsProductAcctHeadTO;
        MDSProductOtherDetailsTO mdsProductOtherDetailsTO;
        mdsProductTO = (MDSProductTO) ((List) mapData.get("getMDSProductTO")).get(0);
        getMDSProductTO(mdsProductTO);
//        mdsProductAcctHeadTO = (MDSProductAcctHeadTO) ((List) mapData.get("getMDSProductAcctHeadTO")).get(0);
//        getMDSProductAcctHeadTO(mdsProductAcctHeadTO);
        mdsProductOtherDetailsTO = (MDSProductOtherDetailsTO) ((List) mapData.get("getMDSProductOtherDetailsTO")).get(0);
        getMDSProductOtherDetailsTO(mdsProductOtherDetailsTO);
        notifyObservers();
        return aliasBranchTableFlag;
    }  
    
    private void getMDSProductTO(MDSProductTO mdsProductTO){        
        setTxtProductId(mdsProductTO.getProdId());
        setLblProductDescVal(mdsProductTO.getProdDesc());
//        setTxtActHead(mdsProductTO.getAcctHead());
	if(mdsProductTO.getOnlyMember().equals("Y")){
            setRdoOnlyMembers_yes(true);
        }else{
            setRdoOnlyMembers_no(true);
        }        
        if(mdsProductTO.getAcceptClassA()!=null && !mdsProductTO.getAcceptClassA().equals("") && mdsProductTO.getAcceptClassA().equals("A")){
            setChkAcceptableClassA(true);   
        }
        if(mdsProductTO.getAcceptClassB()!=null && !mdsProductTO.getAcceptClassB().equals("") && mdsProductTO.getAcceptClassB().equals("B")){
            setChkAcceptableClassB(true);
        }
        if(mdsProductTO.getAcceptClassC()!=null && !mdsProductTO.getAcceptClassC().equals("") && mdsProductTO.getAcceptClassC().equals("C")){
            setChkAcceptableClassC(true);
        }
        if(mdsProductTO.getAcceptClassAll()!=null && !mdsProductTO.getAcceptClassAll().equals("") && mdsProductTO.getAcceptClassAll().equals("D")){
            setChkAcceptableClassAll(true);
        }
        if(mdsProductTO.getSurety()!=null && !mdsProductTO.getSurety().equals("") && mdsProductTO.getSurety().equals("Y")){
            setRdoSuretyRequired_yes(true);
        }else{
            setRdoSuretyRequired_no(true);
        }
        if(mdsProductTO.getAcceptableClassA()!=null && !mdsProductTO.getAcceptableClassA().equals("") && mdsProductTO.getAcceptableClassA().equals("A")){
            setChkAcceptClassA(true);            
        }
        if(mdsProductTO.getAcceptableClassB()!=null && !mdsProductTO.getAcceptableClassB().equals("") && mdsProductTO.getAcceptableClassB().equals("B")){
            setChkAcceptClassB(true);            
        }
        if(mdsProductTO.getAcceptableClassC()!=null && !mdsProductTO.getAcceptableClassC().equals("") && mdsProductTO.getAcceptableClassC().equals("C")){
            setChkAcceptClassC(true);            
        }
        if(mdsProductTO.getAcceptableClassAll()!=null && !mdsProductTO.getAcceptableClassAll().equals("") && mdsProductTO.getAcceptableClassAll().equals("D")){
            setChkAcceptClassAll(true);            
        }
        if(mdsProductTO.getShortFall()!=null && !mdsProductTO.getShortFall().equals("") && mdsProductTO.getShortFall().equals("Y")){
            setRdoShortfall_yes(true);
        }else{
            setRdoShortfall_no(true);
        }
        if(mdsProductTO.getDefaultChitOwners()!=null && !mdsProductTO.getDefaultChitOwners().equals("") && mdsProductTO.getDefaultChitOwners().equals("Y")){
            setRdoChitDefaults_yes(true);
        }else{
            setRdoChitDefaults_no(true);
        }
        if(mdsProductTO.getNonPrizedChange()!=null && !mdsProductTO.getNonPrizedChange().equals("") && mdsProductTO.getNonPrizedChange().equals("Y")){
            setRdoNonPrizedChit_yes(true);
            if(mdsProductTO.getBonusExistingChit()!=null && !mdsProductTO.getBonusExistingChit().equals("") && mdsProductTO.getBonusExistingChit().equals("Y")){
                setRdoBonusRecoveredExistingChittal_yes(true);
            }else{
                setRdoBonusRecoveredExistingChittal_no(true);
            }
        }else{
            setRdoBonusRecoveredExistingChittal_yes(false);
            setRdoBonusRecoveredExistingChittal_no(false);
            setRdoNonPrizedChit_no(true);
        }
        
        if(mdsProductTO.getBonusExistingChit()!=null && !mdsProductTO.getBonusExistingChit().equals("") && mdsProductTO.getBonusExistingChit().equals("Y")){
            setRdoBonusRecoveredExistingChittal_yes(true);
        }else{
            setRdoBonusRecoveredExistingChittal_yes(false);
        }
        
        if(mdsProductTO.getBonusRounding()!=null && !mdsProductTO.getBonusRounding().equals("") && mdsProductTO.getBonusRounding().equals("Y")){
            setRdoBonusAmt_Yes(true);
            setRdoBonusAmt_No(false);
        }else{
            setRdoBonusAmt_No(true);
            setRdoBonusAmt_Yes(false);
        }
         if(mdsProductTO.getBonusPayForFirstIns()!=null && !mdsProductTO.getBonusPayForFirstIns().equals("") && mdsProductTO.getBonusPayForFirstIns().equals("Y")){
            setRdoBonusPayFirstIns_Yes(true);
            setRdoBonusPayFirstIns_No(false);
        }else{
            setRdoBonusPayFirstIns_No(true);
            setRdoBonusPayFirstIns_Yes(false);
        }
        if(mdsProductTO.getAdvanceCollection()!=null && !mdsProductTO.getAdvanceCollection().equals("") && mdsProductTO.getAdvanceCollection().equals("Y")){
            setRdoAdvanceCollection_Yes(true);
            setRdoAdvanceCollection_NO(false);
        }else{
            setRdoAdvanceCollection_NO(true);
            setRdoAdvanceCollection_Yes(false);
        }
        setTxtAuctionMaxAmt(mdsProductTO.getAuctionMaxamt());   //AJITH
        setTxtAuctionMinAmt(mdsProductTO.getAuctionMinamt());   //AJITH
        if(mdsProductTO.getBonusAllowed()!=null && !mdsProductTO.getBonusAllowed().equals("") && mdsProductTO.getBonusAllowed().equals("Y")){
            setRdoBonusAllowed_yes(true);
        }else{
            setRdoBonusAllowed_no(true);
        }
        if(mdsProductTO.getPrizedOwnerBonus()!=null && !mdsProductTO.getPrizedOwnerBonus().equals("") && mdsProductTO.getPrizedOwnerBonus().equals("Y")){
            setRdobonusPayableOwner_yes(true);
        }else{
            setRdobonusPayableOwner_no(true);
        }
        if(mdsProductTO.getAfterAuctionEligible()!=null && !mdsProductTO.getAfterAuctionEligible().equals("") && mdsProductTO.getAfterAuctionEligible().equals("Y")){
            setRdoPrizedChitOwner_yes(true);
        }else{
            setRdoPrizedChitOwner_no(true);
        }
        if(mdsProductTO.getAfterPaymentEligible()!=null && !mdsProductTO.getAfterPaymentEligible().equals("") && mdsProductTO.getAfterPaymentEligible().equals("Y")){
            setRdoPrizedChitOwnerAfter_yes(true);
        }else{
            setRdoPrizedChitOwnerAfter_no(true);
        }
        if(mdsProductTO.getPrizedDefaulters()!=null && !mdsProductTO.getPrizedDefaulters().equals("") && mdsProductTO.getPrizedDefaulters().equals("Y")){
            setRdoprizedDefaulters_yes(true);
        }else{
            setRdoprizedDefaulters_no(true);
        }
        setCboRoundOffCriteria((String) getCbmRoundOffCriteria().getDataForKey(CommonUtil.convertObjToStr(mdsProductTO.getRoundoffCriteria())));
        setCboholidayInt(mdsProductTO.getHolidayInt());
        
        if(mdsProductTO.getRdoPending()!=null && !mdsProductTO.getRdoPending().equals("") && mdsProductTO.getRdoPending().equals("Y"))
        {
            setRdoPending("Y");
        }
        else
        {
            setRdoPending("N");
        }
        if(mdsProductTO.getRdoPrized()!=null && !mdsProductTO.getRdoPrized().equals("") && mdsProductTO.getRdoPrized().equals("Y"))
        {
            setRdoPrized("Y");
        }
        else
        {
            setRdoPrized("N");
        }
        if(mdsProductTO.getRdoPrizdOrInstlAmtIsLess()!=null && !mdsProductTO.getRdoPrizdOrInstlAmtIsLess().equals("") && mdsProductTO.getRdoPrizdOrInstlAmtIsLess().equals("Y"))
        {
            setRdoWhichEverIsLess("Y");
        }
        else
        {
            setRdoWhichEverIsLess("N");
        }
         if(mdsProductTO.getRdoMethod1()!=null && !mdsProductTO.getRdoMethod1().equals("") && mdsProductTO.getRdoMethod1().equals("Y"))
        {
            setRdoMethod1("Y");
        }
        else
        {
           setRdoMethod1("N");
        }
         
         if(mdsProductTO.getRdoMethod2()!=null && !mdsProductTO.getRdoMethod2().equals("") && mdsProductTO.getRdoMethod2().equals("Y"))
        {
            setRdoMethod2("Y");
        }
        else
        {
           setRdoMethod2("N");
        }
         
         
         
         
           if(mdsProductTO.getRdoMethod1()!=null && !mdsProductTO.getRdoMethod1().equals("") && mdsProductTO.getRdoMethod1().equals("Y"))
        {
            setRdoMethod1("Y");
        }
        else
        {
           setRdoMethod1("N");
        }
         
          
         if(mdsProductTO.getTransForFirstIns()!=null && !mdsProductTO.getTransForFirstIns().equals("") && mdsProductTO.getTransForFirstIns().equals("Y"))
        {
            setRdoTransFirstIns_Yes(true);
        }
        else
        {
         setRdoTransFirstIns_Yes(false);
        }
        if (mdsProductTO.getFromAuctionEnrtry() != null && !mdsProductTO.getFromAuctionEnrtry().equals("") && mdsProductTO.getFromAuctionEnrtry().equals("Y")) {
            setChkFromAuctionEnrtry(true);
        }
        if (mdsProductTO.getAfterCashPayment() != null && !mdsProductTO.getAfterCashPayment().equals("") && mdsProductTO.getAfterCashPayment().equals("Y")) {
            setChkAfterCashPayment(true);
        }
        if (mdsProductTO.getSplitMDSTransaction() != null && !mdsProductTO.getSplitMDSTransaction().equals("") && mdsProductTO.getSplitMDSTransaction().equals("Y")) {
            setChkSplitMDSTransaction(true);
        }
        
        if(mdsProductTO.getIsGDS().equalsIgnoreCase("Y")){
            setChkIsGDS("Y");
        }else{
            setChkIsGDS("N");
        }        
        if(mdsProductTO.getIsBonusTrans().equalsIgnoreCase("Y")){
            setChkIsBonusTrans("Y");
        }else{
            setChkIsBonusTrans("N");
        }
    }
    
    private void getMDSProductAcctHeadTO(MDSProductAcctHeadTO mdsProductAcctHeadTO){
        setTxtReceiptHead(mdsProductAcctHeadTO.getReceiptHead());
        setTxtPaymentHead(mdsProductAcctHeadTO.getPaymentHead());
        setTxtSuspenseHead(mdsProductAcctHeadTO.getSuspenseHead());
        setTxtMiscellaneousHead(mdsProductAcctHeadTO.getMiscellaneousHead());
        setTxtCommisionHead(mdsProductAcctHeadTO.getCommisionHead());
        setTxtBonusPayableHead(mdsProductAcctHeadTO.getBonusPayableHead());
        setTxtBonusReceivableHead(mdsProductAcctHeadTO.getBonusReceivableHead());
        setTxtPenalHead(mdsProductAcctHeadTO.getPenalInterestHead());
        setTxtThalayalReceiptsHead(mdsProductAcctHeadTO.getThalayalRepPayHead());
        setTxtThalayalBonusHead(mdsProductAcctHeadTO.getThalayalBonusHead());
        setTxtMunnalBonusHead(mdsProductAcctHeadTO.getMunnalBonusHead());
        setTxtMunnalReceiptsHead(mdsProductAcctHeadTO.getMunnalRepPayHead());
        setTxtBankingHead(mdsProductAcctHeadTO.getBankingRepPayHead());
        setTxtNoticeChargesHead(mdsProductAcctHeadTO.getNoticeChargesHead());
        setTxtCaseExpensesHead(mdsProductAcctHeadTO.getCaseExpenseHead());
    }
    
    private void getMDSProductOtherDetailsTO(MDSProductOtherDetailsTO mdsProductOtherDetailsTO){
        setTxtCommisionRate(mdsProductOtherDetailsTO.getCommisionRateAmt());    //AJITH
        setCboCommisionRate(mdsProductOtherDetailsTO.getCommisionRateType());
        setCboPaymentTimeCharges(mdsProductOtherDetailsTO.getPriizedMoneyPaymentType());
        setTxtpaymentTimeCharges(mdsProductOtherDetailsTO.getPriizedMoneyPaymentAmt());  //AJITH
        setTxtDiscountRate(mdsProductOtherDetailsTO.getDiscountRateAmt());  //AJITH
        setCboDiscountRate(mdsProductOtherDetailsTO.getDiscountRateType());
        setTxtPenalIntRate(mdsProductOtherDetailsTO.getPenalIntAmt());  //AJITH
        setCboPenalIntRate(mdsProductOtherDetailsTO.getPenalIntType());
        setTxtPenalPrizedRate(mdsProductOtherDetailsTO.getPenalPrizedIntAmt());  //AJITH
        setCboPenalPrizedRate(mdsProductOtherDetailsTO.getPenalPrizedIntType());
        setTxtLoanIntRate(mdsProductOtherDetailsTO.getLoanIntAmt());    //AJITH
        setCboLoanIntRate(mdsProductOtherDetailsTO.getLoanIntType());
        setTxtBonusPrizedPreriod(mdsProductOtherDetailsTO.getBonusPrizedGracePeriod());  //AJITH
        setTxtPenalPeriod(mdsProductOtherDetailsTO.getPenalGracePeriod());  //AJITH
        setCboPenalPeriod(mdsProductOtherDetailsTO.getPenalGracePeriodType());
        setTxtPenalPrizedPeriod(mdsProductOtherDetailsTO.getPenalPrizedGracePeriod());  //AJITH
        setCboPenalPrizedPeriod(mdsProductOtherDetailsTO.getPenalPrizedGracePeriodType());
        setCboPenalCalc(mdsProductOtherDetailsTO.getPenalCalc());
        setCboPenalIntRateFullInstAmt(mdsProductOtherDetailsTO.getPenalIntRateFullInstAmt());
        setCboPenalPrizedIntRateFullInstAmt(mdsProductOtherDetailsTO.getPenalPrizedIntRateFullInstAmt());
        System.out.println("NIIIIIIII"+getCboPenalIntRateFullInstAmt());
        if(mdsProductOtherDetailsTO.getBonusGracePeriodDays()!=null && !mdsProductOtherDetailsTO.getBonusGracePeriodDays().equals("") && 
        mdsProductOtherDetailsTO.getBonusGracePeriodDays().equals("D")){
            setRdoBonusGraceDays(true);
        }else if(mdsProductOtherDetailsTO.getBonusGracePeriodMonths()!=null && !mdsProductOtherDetailsTO.getBonusGracePeriodMonths().equals("") && 
        mdsProductOtherDetailsTO.getBonusGracePeriodMonths().equals("M")){
            setRdoBonusGraceMonth(true);
        }else if(mdsProductOtherDetailsTO.getBonusGracePeriodAfter()!=null && !mdsProductOtherDetailsTO.getBonusGracePeriodAfter().equals("") && 
        mdsProductOtherDetailsTO.getBonusGracePeriodAfter().equals("A")){
            setRdoBonusGraceAfter(true);
        }else if(mdsProductOtherDetailsTO.getBonusGracePeriodEnd()!=null && !mdsProductOtherDetailsTO.getBonusGracePeriodEnd().equals("") && 
        mdsProductOtherDetailsTO.getBonusGracePeriodEnd().equals("E")){
            setRdoBonusGraceEnd(true);
        }        
        setTxtBonusGracePeriod(mdsProductOtherDetailsTO.getBonusGracePeriod());  //AJITH
        if(mdsProductOtherDetailsTO.getBonusPrizedGracePeriodDays()!=null && !mdsProductOtherDetailsTO.getBonusPrizedGracePeriodDays().equals("") && 
        mdsProductOtherDetailsTO.getBonusPrizedGracePeriodDays().equals("D")){
            setRdoBonusPrizedDays(true);;
        }else if(mdsProductOtherDetailsTO.getBonusPrizedGracePeriodMnth()!=null && !mdsProductOtherDetailsTO.getBonusPrizedGracePeriodMnth().equals("") && 
        mdsProductOtherDetailsTO.getBonusPrizedGracePeriodMnth().equals("M")){
            setRdoBonusPrizedMonth(true);
        }else if(mdsProductOtherDetailsTO.getBonusPrizedGracePeriodAft()!=null && !mdsProductOtherDetailsTO.getBonusPrizedGracePeriodAft().equals("") && 
        mdsProductOtherDetailsTO.getBonusPrizedGracePeriodAft().equals("A")){
            setRdoBonusPrizedAfter(true);
        }else if(mdsProductOtherDetailsTO.getBonusPrizedGracePeriodEnd()!=null && !mdsProductOtherDetailsTO.getBonusPrizedGracePeriodEnd().equals("") && 
        mdsProductOtherDetailsTO.getBonusPrizedGracePeriodEnd().equals("E")){
            setRdoBonusPrizedEnd(true);
        }
        if(mdsProductOtherDetailsTO.getDisPrizedGracePeriodDays()!=null && !mdsProductOtherDetailsTO.getDisPrizedGracePeriodDays().equals("") && 
        mdsProductOtherDetailsTO.getDisPrizedGracePeriodDays().equals("D")){
            setRdoDiscountPrizedPeriodDays(true);
        }else if(mdsProductOtherDetailsTO.getDisPrizedGracePeriodMonths()!=null && !mdsProductOtherDetailsTO.getDisPrizedGracePeriodMonths().equals("") && 
        mdsProductOtherDetailsTO.getDisPrizedGracePeriodMonths().equals("M")){
            setRdoDiscountPrizedPeriodMonth(true);
        }else if(mdsProductOtherDetailsTO.getDisPrizedGracePeriodAfter()!=null && !mdsProductOtherDetailsTO.getDisPrizedGracePeriodAfter().equals("") && 
        mdsProductOtherDetailsTO.getDisPrizedGracePeriodAfter().equals("A")){
            setRdoDiscountPrizedPeriodAfter(true);
        }else if(mdsProductOtherDetailsTO.getDisPrizedGracePeriodEnd()!=null && !mdsProductOtherDetailsTO.getDisPrizedGracePeriodEnd().equals("") && 
        mdsProductOtherDetailsTO.getDisPrizedGracePeriodEnd().equals("E")){
            setRdoDiscountPrizedPeriodEnd(true);
        }
        if(mdsProductOtherDetailsTO.getDisGracePeriodDays()!= null && !mdsProductOtherDetailsTO.getDisGracePeriodDays().equals("") && 
        mdsProductOtherDetailsTO.getDisGracePeriodDays().equals("E")){
            setRdoDiscountPeriodEnd(true);
        }else if(mdsProductOtherDetailsTO.getDisGracePeriodDays()!= null && !mdsProductOtherDetailsTO.getDisGracePeriodDays().equals("") && 
        mdsProductOtherDetailsTO.getDisGracePeriodDays().equals("A")){
            setRdoDiscountPeriodAfter(true);
        }else if(mdsProductOtherDetailsTO.getDisGracePeriodDays()!=null && !mdsProductOtherDetailsTO.getDisGracePeriodDays().equals("") && 
        mdsProductOtherDetailsTO.getDisGracePeriodDays().equals("M")){
            setRdoDiscountPeriodMonth(true);
        }else if(mdsProductOtherDetailsTO.getDisGracePeriodDays()!=null && !mdsProductOtherDetailsTO.getDisGracePeriodDays().equals("") && 
        mdsProductOtherDetailsTO.getDisGracePeriodDays().equals("D")){
            setRdoDiscountPeriodDays(true);
        }
        if(mdsProductOtherDetailsTO.getDiscountAllowed()!=null && !mdsProductOtherDetailsTO.getDiscountAllowed().equals("") && 
        mdsProductOtherDetailsTO.getDiscountAllowed().equals("Y")){
            setRdoDiscountAllowed_yes(true);
        }else{
            setRdoDiscountAllowed_no(true);
        }
        setTxtDiscountPeriod(mdsProductOtherDetailsTO.getDisGracePeriod());  //AJITH
        setTxtDiscountPrizedPeriod(mdsProductOtherDetailsTO.getDisPrizedGracePeriod());  //AJITH
        if(mdsProductOtherDetailsTO.getLoanAllowed()!=null && !mdsProductOtherDetailsTO.getLoanAllowed().equals("") && mdsProductOtherDetailsTO.getLoanAllowed().equals("Y")){
            setRdoLoanCanbeGranted_yes(true);
        }else{
            setRdoLoanCanbeGranted_no(true);
        }
        setTxtMargin(mdsProductOtherDetailsTO.getLoanMargin()); //AJITH
        setTxtMinLoanAmt(mdsProductOtherDetailsTO.getMinLoanAmt());  //AJITH
        setTxtMaxLoanAmt(mdsProductOtherDetailsTO.getMaxLoanAmt());  //AJITH
        setCboThalayalTransCategory(CommonUtil.convertObjToStr(mdsProductOtherDetailsTO.getThalayalBankTransferCategory()));
        setCboMunnalTransCategory(CommonUtil.convertObjToStr(mdsProductOtherDetailsTO.getMunnalBankTransferCategory()));
    }
        
    public void verifyAcctHead(com.see.truetransact.uicomponent.CTextField accountHead, String mapName) {
        try{
            final HashMap data = new HashMap();
            data.put("ACCT_HD",accountHead.getText());
            data.put(CommonConstants.MAP_NAME , mapName);
            HashMap proxyResultMap = proxy.execute(data,operationMap);
        }catch(Exception e){
            System.out.println("Error in verifyAcctHead");
            accountHead.setText("");
            parseException.logException(e,true);
        }
    }
    
    /**
     * Getter for property cbmCommisionRate.
     * @return Value of property cbmCommisionRate.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCommisionRate() {
        return cbmCommisionRate;
    }
    
    /**
     * Setter for property cbmCommisionRate.
     * @param cbmCommisionRate New value of property cbmCommisionRate.
     */
    public void setCbmCommisionRate(com.see.truetransact.clientutil.ComboBoxModel cbmCommisionRate) {
        this.cbmCommisionRate = cbmCommisionRate;
    }
    
    /**
     * Getter for property cbmDiscountRate.
     * @return Value of property cbmDiscountRate.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDiscountRate() {
        return cbmDiscountRate;
    }
    
    /**
     * Setter for property cbmDiscountRate.
     * @param cbmDiscountRate New value of property cbmDiscountRate.
     */
    public void setCbmDiscountRate(com.see.truetransact.clientutil.ComboBoxModel cbmDiscountRate) {
        this.cbmDiscountRate = cbmDiscountRate;
    }
    
    /**
     * Getter for property cbmPenalIntRate.
     * @return Value of property cbmPenalIntRate.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPenalIntRate() {
        return cbmPenalIntRate;
    }
    
    /**
     * Setter for property cbmPenalIntRate.
     * @param cbmPenalIntRate New value of property cbmPenalIntRate.
     */
    public void setCbmPenalIntRate(com.see.truetransact.clientutil.ComboBoxModel cbmPenalIntRate) {
        this.cbmPenalIntRate = cbmPenalIntRate;
    }
    
    /**
     * Getter for property cbmPenalPrizedRate.
     * @return Value of property cbmPenalPrizedRate.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPenalPrizedRate() {
        return cbmPenalPrizedRate;
    }
    
    /**
     * Setter for property cbmPenalPrizedRate.
     * @param cbmPenalPrizedRate New value of property cbmPenalPrizedRate.
     */
    public void setCbmPenalPrizedRate(com.see.truetransact.clientutil.ComboBoxModel cbmPenalPrizedRate) {
        this.cbmPenalPrizedRate = cbmPenalPrizedRate;
    }
    
    /**
     * Getter for property cbmLoanIntRate.
     * @return Value of property cbmLoanIntRate.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmLoanIntRate() {
        return cbmLoanIntRate;
    }
    
    /**
     * Setter for property cbmLoanIntRate.
     * @param cbmLoanIntRate New value of property cbmLoanIntRate.
     */
    public void setCbmLoanIntRate(com.see.truetransact.clientutil.ComboBoxModel cbmLoanIntRate) {
        this.cbmLoanIntRate = cbmLoanIntRate;
    }
    
    /**
     * Getter for property cbmBonusGracePeriod.
     * @return Value of property cbmBonusGracePeriod.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmBonusGracePeriod() {
        return cbmBonusGracePeriod;
    }
    
    /**
     * Setter for property cbmBonusGracePeriod.
     * @param cbmBonusGracePeriod New value of property cbmBonusGracePeriod.
     */
    public void setCbmBonusGracePeriod(com.see.truetransact.clientutil.ComboBoxModel cbmBonusGracePeriod) {
        this.cbmBonusGracePeriod = cbmBonusGracePeriod;
    }
    
    /**
     * Getter for property cbmBonusPrizedPreriod.
     * @return Value of property cbmBonusPrizedPreriod.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmBonusPrizedPreriod() {
        return cbmBonusPrizedPreriod;
    }
    
    /**
     * Setter for property cbmBonusPrizedPreriod.
     * @param cbmBonusPrizedPreriod New value of property cbmBonusPrizedPreriod.
     */
    public void setCbmBonusPrizedPreriod(com.see.truetransact.clientutil.ComboBoxModel cbmBonusPrizedPreriod) {
        this.cbmBonusPrizedPreriod = cbmBonusPrizedPreriod;
    }
    
    /**
     * Getter for property cbmPenalPeriod.
     * @return Value of property cbmPenalPeriod.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPenalPeriod() {
        return cbmPenalPeriod;
    }
    
    /**
     * Setter for property cbmPenalPeriod.
     * @param cbmPenalPeriod New value of property cbmPenalPeriod.
     */
    public void setCbmPenalPeriod(com.see.truetransact.clientutil.ComboBoxModel cbmPenalPeriod) {
        this.cbmPenalPeriod = cbmPenalPeriod;
    }
    
    /**
     * Getter for property cbmPenalPrizedPeriod.
     * @return Value of property cbmPenalPrizedPeriod.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPenalPrizedPeriod() {
        return cbmPenalPrizedPeriod;
    }
    
    /**
     * Setter for property cbmPenalPrizedPeriod.
     * @param cbmPenalPrizedPeriod New value of property cbmPenalPrizedPeriod.
     */
    public void setCbmPenalPrizedPeriod(com.see.truetransact.clientutil.ComboBoxModel cbmPenalPrizedPeriod) {
        this.cbmPenalPrizedPeriod = cbmPenalPrizedPeriod;
    }
    
    /**
     * Getter for property cbmPenalCalc.
     * @return Value of property cbmPenalCalc.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPenalCalc() {
        return cbmPenalCalc;
    }
    
    /**
     * Setter for property cbmPenalCalc.
     * @param cbmPenalCalc New value of property cbmPenalCalc.
     */
    public void setCbmPenalCalc(com.see.truetransact.clientutil.ComboBoxModel cbmPenalCalc) {
        this.cbmPenalCalc = cbmPenalCalc;
    }
    
    /**
     * Getter for property cbmDiscountPeriod.
     * @return Value of property cbmDiscountPeriod.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDiscountPeriod() {
        return cbmDiscountPeriod;
    }
    
    /**
     * Setter for property cbmDiscountPeriod.
     * @param cbmDiscountPeriod New value of property cbmDiscountPeriod.
     */
    public void setCbmDiscountPeriod(com.see.truetransact.clientutil.ComboBoxModel cbmDiscountPeriod) {
        this.cbmDiscountPeriod = cbmDiscountPeriod;
    }
    
    /**
     * Getter for property cbmDiscountPrizedPeriod.
     * @return Value of property cbmDiscountPrizedPeriod.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDiscountPrizedPeriod() {
        return cbmDiscountPrizedPeriod;
    }
    
    /**
     * Setter for property cbmDiscountPrizedPeriod.
     * @param cbmDiscountPrizedPeriod New value of property cbmDiscountPrizedPeriod.
     */
    public void setCbmDiscountPrizedPeriod(com.see.truetransact.clientutil.ComboBoxModel cbmDiscountPrizedPeriod) {
        this.cbmDiscountPrizedPeriod = cbmDiscountPrizedPeriod;
    }
    
    /**
     * Getter for property txtProductId.
     * @return Value of property txtProductId.
     */
    public java.lang.String getTxtProductId() {
        return txtProductId;
    }
    
    /**
     * Setter for property txtProductId.
     * @param txtProductId New value of property txtProductId.
     */
    public void setTxtProductId(java.lang.String txtProductId) {
        this.txtProductId = txtProductId;
    }
    
    /**
     * Getter for property lblProductDescVal.
     * @return Value of property lblProductDescVal.
     */
    public java.lang.String getLblProductDescVal() {
        return lblProductDescVal;
    }
    
    /**
     * Setter for property lblProductDescVal.
     * @param lblProductDescVal New value of property lblProductDescVal.
     */
    public void setLblProductDescVal(java.lang.String lblProductDescVal) {
        this.lblProductDescVal = lblProductDescVal;
    }
    
    /**
     * Getter for property txtActHead.
     * @return Value of property txtActHead.
     */
    public java.lang.String getTxtActHead() {
        return txtActHead;
    }
    
    /**
     * Setter for property txtActHead.
     * @param txtActHead New value of property txtActHead.
     */
    public void setTxtActHead(java.lang.String txtActHead) {
        this.txtActHead = txtActHead;
    }

    public Double getTxtAuctionMaxAmt() {
        return txtAuctionMaxAmt;
    }

    public void setTxtAuctionMaxAmt(Double txtAuctionMaxAmt) {
        this.txtAuctionMaxAmt = txtAuctionMaxAmt;
    }

    public Double getTxtAuctionMinAmt() {
        return txtAuctionMinAmt;
    }

    public void setTxtAuctionMinAmt(Double txtAuctionMinAmt) {
        this.txtAuctionMinAmt = txtAuctionMinAmt;
    }

    /**
     * Getter for property txtReceiptHead.
     * @return Value of property txtReceiptHead.
     */
    public java.lang.String getTxtReceiptHead() {
        return txtReceiptHead;
    }
    
    /**
     * Setter for property txtReceiptHead.
     * @param txtReceiptHead New value of property txtReceiptHead.
     */
    public void setTxtReceiptHead(java.lang.String txtReceiptHead) {
        this.txtReceiptHead = txtReceiptHead;
    }
    
    /**
     * Getter for property txtPaymentHead.
     * @return Value of property txtPaymentHead.
     */
    public java.lang.String getTxtPaymentHead() {
        return txtPaymentHead;
    }
    
    /**
     * Setter for property txtPaymentHead.
     * @param txtPaymentHead New value of property txtPaymentHead.
     */
    public void setTxtPaymentHead(java.lang.String txtPaymentHead) {
        this.txtPaymentHead = txtPaymentHead;
    }
    
    /**
     * Getter for property txtSuspenseHead.
     * @return Value of property txtSuspenseHead.
     */
    public java.lang.String getTxtSuspenseHead() {
        return txtSuspenseHead;
    }
    
    /**
     * Setter for property txtSuspenseHead.
     * @param txtSuspenseHead New value of property txtSuspenseHead.
     */
    public void setTxtSuspenseHead(java.lang.String txtSuspenseHead) {
        this.txtSuspenseHead = txtSuspenseHead;
    }
    
    /**
     * Getter for property txtMiscellaneousHead.
     * @return Value of property txtMiscellaneousHead.
     */
    public java.lang.String getTxtMiscellaneousHead() {
        return txtMiscellaneousHead;
    }
    
    /**
     * Setter for property txtMiscellaneousHead.
     * @param txtMiscellaneousHead New value of property txtMiscellaneousHead.
     */
    public void setTxtMiscellaneousHead(java.lang.String txtMiscellaneousHead) {
        this.txtMiscellaneousHead = txtMiscellaneousHead;
    }
    
    /**
     * Getter for property txtCommisionHead.
     * @return Value of property txtCommisionHead.
     */
    public java.lang.String getTxtCommisionHead() {
        return txtCommisionHead;
    }
    
    /**
     * Setter for property txtCommisionHead.
     * @param txtCommisionHead New value of property txtCommisionHead.
     */
    public void setTxtCommisionHead(java.lang.String txtCommisionHead) {
        this.txtCommisionHead = txtCommisionHead;
    }
    
    /**
     * Getter for property txtBonusPayableHead.
     * @return Value of property txtBonusPayableHead.
     */
    public java.lang.String getTxtBonusPayableHead() {
        return txtBonusPayableHead;
    }
    
    /**
     * Setter for property txtBonusPayableHead.
     * @param txtBonusPayableHead New value of property txtBonusPayableHead.
     */
    public void setTxtBonusPayableHead(java.lang.String txtBonusPayableHead) {
        this.txtBonusPayableHead = txtBonusPayableHead;
    }
    
    /**
     * Getter for property txtBonusReceivableHead.
     * @return Value of property txtBonusReceivableHead.
     */
    public java.lang.String getTxtBonusReceivableHead() {
        return txtBonusReceivableHead;
    }
    
    /**
     * Setter for property txtBonusReceivableHead.
     * @param txtBonusReceivableHead New value of property txtBonusReceivableHead.
     */
    public void setTxtBonusReceivableHead(java.lang.String txtBonusReceivableHead) {
        this.txtBonusReceivableHead = txtBonusReceivableHead;
    }
    
    /**
     * Getter for property txtPenalHead.
     * @return Value of property txtPenalHead.
     */
    public java.lang.String getTxtPenalHead() {
        return txtPenalHead;
    }
    
    /**
     * Setter for property txtPenalHead.
     * @param txtPenalHead New value of property txtPenalHead.
     */
    public void setTxtPenalHead(java.lang.String txtPenalHead) {
        this.txtPenalHead = txtPenalHead;
    }
    
    /**
     * Getter for property txtThalayalReceiptsHead.
     * @return Value of property txtThalayalReceiptsHead.
     */
    public java.lang.String getTxtThalayalReceiptsHead() {
        return txtThalayalReceiptsHead;
    }
    
    /**
     * Setter for property txtThalayalReceiptsHead.
     * @param txtThalayalReceiptsHead New value of property txtThalayalReceiptsHead.
     */
    public void setTxtThalayalReceiptsHead(java.lang.String txtThalayalReceiptsHead) {
        this.txtThalayalReceiptsHead = txtThalayalReceiptsHead;
    }
    
    /**
     * Getter for property txtThalayalBonusHead.
     * @return Value of property txtThalayalBonusHead.
     */
    public java.lang.String getTxtThalayalBonusHead() {
        return txtThalayalBonusHead;
    }
    
    /**
     * Setter for property txtThalayalBonusHead.
     * @param txtThalayalBonusHead New value of property txtThalayalBonusHead.
     */
    public void setTxtThalayalBonusHead(java.lang.String txtThalayalBonusHead) {
        this.txtThalayalBonusHead = txtThalayalBonusHead;
    }
    
    /**
     * Getter for property txtMunnalReceiptsHead.
     * @return Value of property txtMunnalReceiptsHead.
     */
    public java.lang.String getTxtMunnalReceiptsHead() {
        return txtMunnalReceiptsHead;
    }
    
    /**
     * Setter for property txtMunnalReceiptsHead.
     * @param txtMunnalReceiptsHead New value of property txtMunnalReceiptsHead.
     */
    public void setTxtMunnalReceiptsHead(java.lang.String txtMunnalReceiptsHead) {
        this.txtMunnalReceiptsHead = txtMunnalReceiptsHead;
    }
    
    /**
     * Getter for property txtBankingHead.
     * @return Value of property txtBankingHead.
     */
    public java.lang.String getTxtBankingHead() {
        return txtBankingHead;
    }
    
    /**
     * Setter for property txtBankingHead.
     * @param txtBankingHead New value of property txtBankingHead.
     */
    public void setTxtBankingHead(java.lang.String txtBankingHead) {
        this.txtBankingHead = txtBankingHead;
    }
    
    /**
     * Getter for property txtNoticeChargesHead.
     * @return Value of property txtNoticeChargesHead.
     */
    public java.lang.String getTxtNoticeChargesHead() {
        return txtNoticeChargesHead;
    }
    
    /**
     * Setter for property txtNoticeChargesHead.
     * @param txtNoticeChargesHead New value of property txtNoticeChargesHead.
     */
    public void setTxtNoticeChargesHead(java.lang.String txtNoticeChargesHead) {
        this.txtNoticeChargesHead = txtNoticeChargesHead;
    }
    
    /**
     * Getter for property txtCaseExpensesHead.
     * @return Value of property txtCaseExpensesHead.
     */
    public java.lang.String getTxtCaseExpensesHead() {
        return txtCaseExpensesHead;
    }
    
    /**
     * Setter for property txtCaseExpensesHead.
     * @param txtCaseExpensesHead New value of property txtCaseExpensesHead.
     */
    public void setTxtCaseExpensesHead(java.lang.String txtCaseExpensesHead) {
        this.txtCaseExpensesHead = txtCaseExpensesHead;
    }
    
    /**
     * Getter for property rdoOnlyMembers_yes.
     * @return Value of property rdoOnlyMembers_yes.
     */
    public boolean getRdoOnlyMembers_yes() {
        return rdoOnlyMembers_yes;
    }
    
    /**
     * Setter for property rdoOnlyMembers_yes.
     * @param rdoOnlyMembers_yes New value of property rdoOnlyMembers_yes.
     */
    public void setRdoOnlyMembers_yes(boolean rdoOnlyMembers_yes) {
        this.rdoOnlyMembers_yes = rdoOnlyMembers_yes;
    }
    
    /**
     * Getter for property rdoOnlyMembers_no.
     * @return Value of property rdoOnlyMembers_no.
     */
    public boolean getRdoOnlyMembers_no() {
        return rdoOnlyMembers_no;
    }
    
    /**
     * Setter for property rdoOnlyMembers_no.
     * @param rdoOnlyMembers_no New value of property rdoOnlyMembers_no.
     */
    public void setRdoOnlyMembers_no(boolean rdoOnlyMembers_no) {
        this.rdoOnlyMembers_no = rdoOnlyMembers_no;
    }
 
    /**
     * Getter for property rdoSuretyRequired_yes.
     * @return Value of property rdoSuretyRequired_yes.
     */
    public boolean getRdoSuretyRequired_yes() {
        return rdoSuretyRequired_yes;
    }
    
    /**
     * Setter for property rdoSuretyRequired_yes.
     * @param rdoSuretyRequired_yes New value of property rdoSuretyRequired_yes.
     */
    public void setRdoSuretyRequired_yes(boolean rdoSuretyRequired_yes) {
        this.rdoSuretyRequired_yes = rdoSuretyRequired_yes;
    }
    
    /**
     * Getter for property rdoSuretyRequired_no.
     * @return Value of property rdoSuretyRequired_no.
     */
    public boolean getRdoSuretyRequired_no() {
        return rdoSuretyRequired_no;
    }
    
    /**
     * Setter for property rdoSuretyRequired_no.
     * @param rdoSuretyRequired_no New value of property rdoSuretyRequired_no.
     */
    public void setRdoSuretyRequired_no(boolean rdoSuretyRequired_no) {
        this.rdoSuretyRequired_no = rdoSuretyRequired_no;
    }
    
    /**
     * Getter for property rdoShortfall_yes.
     * @return Value of property rdoShortfall_yes.
     */
    public boolean getRdoShortfall_yes() {
        return rdoShortfall_yes;
    }
    
    /**
     * Setter for property rdoShortfall_yes.
     * @param rdoShortfall_yes New value of property rdoShortfall_yes.
     */
    public void setRdoShortfall_yes(boolean rdoShortfall_yes) {
        this.rdoShortfall_yes = rdoShortfall_yes;
    }
    
    /**
     * Getter for property rdoShortfall_no.
     * @return Value of property rdoShortfall_no.
     */
    public boolean getRdoShortfall_no() {
        return rdoShortfall_no;
    }
    
    /**
     * Setter for property rdoShortfall_no.
     * @param rdoShortfall_no New value of property rdoShortfall_no.
     */
    public void setRdoShortfall_no(boolean rdoShortfall_no) {
        this.rdoShortfall_no = rdoShortfall_no;
    }
    
    /**
     * Getter for property rdoChitDefaults_yes.
     * @return Value of property rdoChitDefaults_yes.
     */
    public boolean getRdoChitDefaults_yes() {
        return rdoChitDefaults_yes;
    }
    
    /**
     * Setter for property rdoChitDefaults_yes.
     * @param rdoChitDefaults_yes New value of property rdoChitDefaults_yes.
     */
    public void setRdoChitDefaults_yes(boolean rdoChitDefaults_yes) {
        this.rdoChitDefaults_yes = rdoChitDefaults_yes;
    }
    
    /**
     * Getter for property rdoChitDefaults_no.
     * @return Value of property rdoChitDefaults_no.
     */
    public boolean getRdoChitDefaults_no() {
        return rdoChitDefaults_no;
    }
    
    /**
     * Setter for property rdoChitDefaults_no.
     * @param rdoChitDefaults_no New value of property rdoChitDefaults_no.
     */
    public void setRdoChitDefaults_no(boolean rdoChitDefaults_no) {
        this.rdoChitDefaults_no = rdoChitDefaults_no;
    }
    
    /**
     * Getter for property rdoNonPrizedChit_yes.
     * @return Value of property rdoNonPrizedChit_yes.
     */
    public boolean getRdoNonPrizedChit_yes() {
        return rdoNonPrizedChit_yes;
    }
    
    /**
     * Setter for property rdoNonPrizedChit_yes.
     * @param rdoNonPrizedChit_yes New value of property rdoNonPrizedChit_yes.
     */
    public void setRdoNonPrizedChit_yes(boolean rdoNonPrizedChit_yes) {
        this.rdoNonPrizedChit_yes = rdoNonPrizedChit_yes;
    }
    
    /**
     * Getter for property rdoBonusRecoveredExistingChittal_yes.
     * @return Value of property rdoBonusRecoveredExistingChittal_yes.
     */
    public boolean getRdoBonusRecoveredExistingChittal_yes() {
        return rdoBonusRecoveredExistingChittal_yes;
    }
    
    /**
     * Setter for property rdoBonusRecoveredExistingChittal_yes.
     * @param rdoBonusRecoveredExistingChittal_yes New value of property rdoBonusRecoveredExistingChittal_yes.
     */
    public void setRdoBonusRecoveredExistingChittal_yes(boolean rdoBonusRecoveredExistingChittal_yes) {
        this.rdoBonusRecoveredExistingChittal_yes = rdoBonusRecoveredExistingChittal_yes;
    }
    
    /**
     * Getter for property rdoBonusRecoveredExistingChittal_no.
     * @return Value of property rdoBonusRecoveredExistingChittal_no.
     */
    public boolean getRdoBonusRecoveredExistingChittal_no() {
        return rdoBonusRecoveredExistingChittal_no;
    }
    
    /**
     * Setter for property rdoBonusRecoveredExistingChittal_no.
     * @param rdoBonusRecoveredExistingChittal_no New value of property rdoBonusRecoveredExistingChittal_no.
     */
    public void setRdoBonusRecoveredExistingChittal_no(boolean rdoBonusRecoveredExistingChittal_no) {
        this.rdoBonusRecoveredExistingChittal_no = rdoBonusRecoveredExistingChittal_no;
    }
    
    
    
    
    
    /**
     * Getter for property rdoNonPrizedChit_no.
     * @return Value of property rdoNonPrizedChit_no.
     */
    public boolean getRdoNonPrizedChit_no() {
        return rdoNonPrizedChit_no;
    }
    
    /**
     * Setter for property rdoNonPrizedChit_no.
     * @param rdoNonPrizedChit_no New value of property rdoNonPrizedChit_no.
     */
    public void setRdoNonPrizedChit_no(boolean rdoNonPrizedChit_no) {
        this.rdoNonPrizedChit_no = rdoNonPrizedChit_no;
    }
    
    /**
     * Getter for property rdoBonusAllowed_yes.
     * @return Value of property rdoBonusAllowed_yes.
     */
    public boolean getRdoBonusAllowed_yes() {
        return rdoBonusAllowed_yes;
    }
    
    /**
     * Setter for property rdoBonusAllowed_yes.
     * @param rdoBonusAllowed_yes New value of property rdoBonusAllowed_yes.
     */
    public void setRdoBonusAllowed_yes(boolean rdoBonusAllowed_yes) {
        this.rdoBonusAllowed_yes = rdoBonusAllowed_yes;
    }
    
    /**
     * Getter for property rdoBonusAllowed_no.
     * @return Value of property rdoBonusAllowed_no.
     */
    public boolean getRdoBonusAllowed_no() {
        return rdoBonusAllowed_no;
    }
    
    /**
     * Setter for property rdoBonusAllowed_no.
     * @param rdoBonusAllowed_no New value of property rdoBonusAllowed_no.
     */
    public void setRdoBonusAllowed_no(boolean rdoBonusAllowed_no) {
        this.rdoBonusAllowed_no = rdoBonusAllowed_no;
    }
    
    /**
     * Getter for property rdobonusPayableOwner_yes.
     * @return Value of property rdobonusPayableOwner_yes.
     */
    public boolean getRdobonusPayableOwner_yes() {
        return rdobonusPayableOwner_yes;
    }
    
    /**
     * Setter for property rdobonusPayableOwner_yes.
     * @param rdobonusPayableOwner_yes New value of property rdobonusPayableOwner_yes.
     */
    public void setRdobonusPayableOwner_yes(boolean rdobonusPayableOwner_yes) {
        this.rdobonusPayableOwner_yes = rdobonusPayableOwner_yes;
    }
    
    /**
     * Getter for property rdobonusPayableOwner_no.
     * @return Value of property rdobonusPayableOwner_no.
     */
    public boolean getRdobonusPayableOwner_no() {
        return rdobonusPayableOwner_no;
    }
    
    /**
     * Setter for property rdobonusPayableOwner_no.
     * @param rdobonusPayableOwner_no New value of property rdobonusPayableOwner_no.
     */
    public void setRdobonusPayableOwner_no(boolean rdobonusPayableOwner_no) {
        this.rdobonusPayableOwner_no = rdobonusPayableOwner_no;
    }
    
    /**
     * Getter for property rdoPrizedChitOwner_yes.
     * @return Value of property rdoPrizedChitOwner_yes.
     */
    public boolean getRdoPrizedChitOwner_yes() {
        return rdoPrizedChitOwner_yes;
    }
    
    /**
     * Setter for property rdoPrizedChitOwner_yes.
     * @param rdoPrizedChitOwner_yes New value of property rdoPrizedChitOwner_yes.
     */
    public void setRdoPrizedChitOwner_yes(boolean rdoPrizedChitOwner_yes) {
        this.rdoPrizedChitOwner_yes = rdoPrizedChitOwner_yes;
    }
    
    /**
     * Getter for property rdoPrizedChitOwner_no.
     * @return Value of property rdoPrizedChitOwner_no.
     */
    public boolean getRdoPrizedChitOwner_no() {
        return rdoPrizedChitOwner_no;
    }
    
    /**
     * Setter for property rdoPrizedChitOwner_no.
     * @param rdoPrizedChitOwner_no New value of property rdoPrizedChitOwner_no.
     */
    public void setRdoPrizedChitOwner_no(boolean rdoPrizedChitOwner_no) {
        this.rdoPrizedChitOwner_no = rdoPrizedChitOwner_no;
    }
    
    /**
     * Getter for property rdoPrizedChitOwnerAfter_yes.
     * @return Value of property rdoPrizedChitOwnerAfter_yes.
     */
    public boolean getRdoPrizedChitOwnerAfter_yes() {
        return rdoPrizedChitOwnerAfter_yes;
    }
    
    /**
     * Setter for property rdoPrizedChitOwnerAfter_yes.
     * @param rdoPrizedChitOwnerAfter_yes New value of property rdoPrizedChitOwnerAfter_yes.
     */
    public void setRdoPrizedChitOwnerAfter_yes(boolean rdoPrizedChitOwnerAfter_yes) {
        this.rdoPrizedChitOwnerAfter_yes = rdoPrizedChitOwnerAfter_yes;
    }
    
    /**
     * Getter for property rdoPrizedChitOwnerAfter_no.
     * @return Value of property rdoPrizedChitOwnerAfter_no.
     */
    public boolean getRdoPrizedChitOwnerAfter_no() {
        return rdoPrizedChitOwnerAfter_no;
    }
    
    /**
     * Setter for property rdoPrizedChitOwnerAfter_no.
     * @param rdoPrizedChitOwnerAfter_no New value of property rdoPrizedChitOwnerAfter_no.
     */
    public void setRdoPrizedChitOwnerAfter_no(boolean rdoPrizedChitOwnerAfter_no) {
        this.rdoPrizedChitOwnerAfter_no = rdoPrizedChitOwnerAfter_no;
    }
    
    /**
     * Getter for property rdoprizedDefaulters_yes.
     * @return Value of property rdoprizedDefaulters_yes.
     */
    public boolean getRdoprizedDefaulters_yes() {
        return rdoprizedDefaulters_yes;
    }
    
    /**
     * Setter for property rdoprizedDefaulters_yes.
     * @param rdoprizedDefaulters_yes New value of property rdoprizedDefaulters_yes.
     */
    public void setRdoprizedDefaulters_yes(boolean rdoprizedDefaulters_yes) {
        this.rdoprizedDefaulters_yes = rdoprizedDefaulters_yes;
    }
    
    /**
     * Getter for property rdoprizedDefaulters_no.
     * @return Value of property rdoprizedDefaulters_no.
     */
    public boolean getRdoprizedDefaulters_no() {
        return rdoprizedDefaulters_no;
    }
    
    /**
     * Setter for property rdoprizedDefaulters_no.
     * @param rdoprizedDefaulters_no New value of property rdoprizedDefaulters_no.
     */
    public void setRdoprizedDefaulters_no(boolean rdoprizedDefaulters_no) {
        this.rdoprizedDefaulters_no = rdoprizedDefaulters_no;
    }
    
    /**
     * Getter for property cboCommisionRate.
     * @return Value of property cboCommisionRate.
     */
    public java.lang.String getCboCommisionRate() {
        return cboCommisionRate;
    }
    
    /**
     * Setter for property cboCommisionRate.
     * @param cboCommisionRate New value of property cboCommisionRate.
     */
    public void setCboCommisionRate(java.lang.String cboCommisionRate) {
        this.cboCommisionRate = cboCommisionRate;
    }
    
    /**
     * Getter for property cboDiscountRate.
     * @return Value of property cboDiscountRate.
     */
    public java.lang.String getCboDiscountRate() {
        return cboDiscountRate;
    }
    
    /**
     * Setter for property cboDiscountRate.
     * @param cboDiscountRate New value of property cboDiscountRate.
     */
    public void setCboDiscountRate(java.lang.String cboDiscountRate) {
        this.cboDiscountRate = cboDiscountRate;
    }
    
    /**
     * Getter for property cboPenalIntRate.
     * @return Value of property cboPenalIntRate.
     */
    public java.lang.String getCboPenalIntRate() {
        return cboPenalIntRate;
    }
    
    /**
     * Setter for property cboPenalIntRate.
     * @param cboPenalIntRate New value of property cboPenalIntRate.
     */
    public void setCboPenalIntRate(java.lang.String cboPenalIntRate) {
        this.cboPenalIntRate = cboPenalIntRate;
    }
    
    /**
     * Getter for property cboPenalPrizedRate.
     * @return Value of property cboPenalPrizedRate.
     */
    public java.lang.String getCboPenalPrizedRate() {
        return cboPenalPrizedRate;
    }
    
    /**
     * Setter for property cboPenalPrizedRate.
     * @param cboPenalPrizedRate New value of property cboPenalPrizedRate.
     */
    public void setCboPenalPrizedRate(java.lang.String cboPenalPrizedRate) {
        this.cboPenalPrizedRate = cboPenalPrizedRate;
    }
    
    /**
     * Getter for property cboLoanIntRate.
     * @return Value of property cboLoanIntRate.
     */
    public java.lang.String getCboLoanIntRate() {
        return cboLoanIntRate;
    }
    
    /**
     * Setter for property cboLoanIntRate.
     * @param cboLoanIntRate New value of property cboLoanIntRate.
     */
    public void setCboLoanIntRate(java.lang.String cboLoanIntRate) {
        this.cboLoanIntRate = cboLoanIntRate;
    }
    
    /**
     * Getter for property cboBonusGracePeriod.
     * @return Value of property cboBonusGracePeriod.
     */
    public java.lang.String getCboBonusGracePeriod() {
        return cboBonusGracePeriod;
    }
    
    /**
     * Setter for property cboBonusGracePeriod.
     * @param cboBonusGracePeriod New value of property cboBonusGracePeriod.
     */
    public void setCboBonusGracePeriod(java.lang.String cboBonusGracePeriod) {
        this.cboBonusGracePeriod = cboBonusGracePeriod;
    }
    
    /**
     * Getter for property cboBonusPrizedPreriod.
     * @return Value of property cboBonusPrizedPreriod.
     */
    public java.lang.String getCboBonusPrizedPreriod() {
        return cboBonusPrizedPreriod;
    }
    
    /**
     * Setter for property cboBonusPrizedPreriod.
     * @param cboBonusPrizedPreriod New value of property cboBonusPrizedPreriod.
     */
    public void setCboBonusPrizedPreriod(java.lang.String cboBonusPrizedPreriod) {
        this.cboBonusPrizedPreriod = cboBonusPrizedPreriod;
    }
    
    /**
     * Getter for property cboPenalPeriod.
     * @return Value of property cboPenalPeriod.
     */
    public java.lang.String getCboPenalPeriod() {
        return cboPenalPeriod;
    }
    
    /**
     * Setter for property cboPenalPeriod.
     * @param cboPenalPeriod New value of property cboPenalPeriod.
     */
    public void setCboPenalPeriod(java.lang.String cboPenalPeriod) {
        this.cboPenalPeriod = cboPenalPeriod;
    }
    
    /**
     * Getter for property cboPenalPrizedPeriod.
     * @return Value of property cboPenalPrizedPeriod.
     */
    public java.lang.String getCboPenalPrizedPeriod() {
        return cboPenalPrizedPeriod;
    }
    
    /**
     * Setter for property cboPenalPrizedPeriod.
     * @param cboPenalPrizedPeriod New value of property cboPenalPrizedPeriod.
     */
    public void setCboPenalPrizedPeriod(java.lang.String cboPenalPrizedPeriod) {
        this.cboPenalPrizedPeriod = cboPenalPrizedPeriod;
    }
    
    /**
     * Getter for property cboPenalCalc.
     * @return Value of property cboPenalCalc.
     */
    public java.lang.String getCboPenalCalc() {
        return cboPenalCalc;
    }
    
    /**
     * Setter for property cboPenalCalc.
     * @param cboPenalCalc New value of property cboPenalCalc.
     */
    public void setCboPenalCalc(java.lang.String cboPenalCalc) {
        this.cboPenalCalc = cboPenalCalc;
    }
    
    /**
     * Getter for property cboDiscountPeriod.
     * @return Value of property cboDiscountPeriod.
     */
    public java.lang.String getCboDiscountPeriod() {
        return cboDiscountPeriod;
    }
    
    /**
     * Setter for property cboDiscountPeriod.
     * @param cboDiscountPeriod New value of property cboDiscountPeriod.
     */
    public void setCboDiscountPeriod(java.lang.String cboDiscountPeriod) {
        this.cboDiscountPeriod = cboDiscountPeriod;
    }
    
    /**
     * Getter for property cboDiscountPrizedPeriod.
     * @return Value of property cboDiscountPrizedPeriod.
     */
    public java.lang.String getCboDiscountPrizedPeriod() {
        return cboDiscountPrizedPeriod;
    }
    
    /**
     * Setter for property cboDiscountPrizedPeriod.
     * @param cboDiscountPrizedPeriod New value of property cboDiscountPrizedPeriod.
     */
    public void setCboDiscountPrizedPeriod(java.lang.String cboDiscountPrizedPeriod) {
        this.cboDiscountPrizedPeriod = cboDiscountPrizedPeriod;
    }

    public Double getTxtCommisionRate() {
        return txtCommisionRate;
    }

    public void setTxtCommisionRate(Double txtCommisionRate) {
        this.txtCommisionRate = txtCommisionRate;
    }

    public Double getTxtDiscountRate() {
        return txtDiscountRate;
    }

    public void setTxtDiscountRate(Double txtDiscountRate) {
        this.txtDiscountRate = txtDiscountRate;
    }

    public Double getTxtPenalIntRate() {
        return txtPenalIntRate;
    }

    public void setTxtPenalIntRate(Double txtPenalIntRate) {
        this.txtPenalIntRate = txtPenalIntRate;
    }

    public Double getTxtPenalPrizedRate() {
        return txtPenalPrizedRate;
    }

    public void setTxtPenalPrizedRate(Double txtPenalPrizedRate) {
        this.txtPenalPrizedRate = txtPenalPrizedRate;
    }

    public Double getTxtLoanIntRate() {
        return txtLoanIntRate;
    }

    public void setTxtLoanIntRate(Double txtLoanIntRate) {
        this.txtLoanIntRate = txtLoanIntRate;
    }

    public Double getTxtBonusGracePeriod() {
        return txtBonusGracePeriod;
    }

    public void setTxtBonusGracePeriod(Double txtBonusGracePeriod) {
        this.txtBonusGracePeriod = txtBonusGracePeriod;
    }

    public Double getTxtBonusPrizedPreriod() {
        return txtBonusPrizedPreriod;
    }

    public void setTxtBonusPrizedPreriod(Double txtBonusPrizedPreriod) {
        this.txtBonusPrizedPreriod = txtBonusPrizedPreriod;
    }

    public Double getTxtPenalPeriod() {
        return txtPenalPeriod;
    }

    public void setTxtPenalPeriod(Double txtPenalPeriod) {
        this.txtPenalPeriod = txtPenalPeriod;
    }

    public Double getTxtPenalPrizedPeriod() {
        return txtPenalPrizedPeriod;
    }

    public void setTxtPenalPrizedPeriod(Double txtPenalPrizedPeriod) {
        this.txtPenalPrizedPeriod = txtPenalPrizedPeriod;
    }

    public Double getTxtDiscountPeriod() {
        return txtDiscountPeriod;
    }

    public void setTxtDiscountPeriod(Double txtDiscountPeriod) {
        this.txtDiscountPeriod = txtDiscountPeriod;
    }

    public Double getTxtDiscountPrizedPeriod() {
        return txtDiscountPrizedPeriod;
    }

    public void setTxtDiscountPrizedPeriod(Double txtDiscountPrizedPeriod) {
        this.txtDiscountPrizedPeriod = txtDiscountPrizedPeriod;
    }

    public Double getTxtMargin() {
        return txtMargin;
    }

    public void setTxtMargin(Double txtMargin) {
        this.txtMargin = txtMargin;
    }

    public Double getTxtMinLoanAmt() {
        return txtMinLoanAmt;
    }

    public void setTxtMinLoanAmt(Double txtMinLoanAmt) {
        this.txtMinLoanAmt = txtMinLoanAmt;
    }

    public Double getTxtMaxLoanAmt() {
        return txtMaxLoanAmt;
    }

    public void setTxtMaxLoanAmt(Double txtMaxLoanAmt) {
        this.txtMaxLoanAmt = txtMaxLoanAmt;
    }

    /**
     * Getter for property rdoDiscountAllowed_yes.
     * @return Value of property rdoDiscountAllowed_yes.
     */
    public boolean getRdoDiscountAllowed_yes() {
        return rdoDiscountAllowed_yes;
    }
    
    /**
     * Setter for property rdoDiscountAllowed_yes.
     * @param rdoDiscountAllowed_yes New value of property rdoDiscountAllowed_yes.
     */
    public void setRdoDiscountAllowed_yes(boolean rdoDiscountAllowed_yes) {
        this.rdoDiscountAllowed_yes = rdoDiscountAllowed_yes;
    }
    
    /**
     * Getter for property rdoDiscountAllowed_no.
     * @return Value of property rdoDiscountAllowed_no.
     */
    public boolean getRdoDiscountAllowed_no() {
        return rdoDiscountAllowed_no;
    }
    
    /**
     * Setter for property rdoDiscountAllowed_no.
     * @param rdoDiscountAllowed_no New value of property rdoDiscountAllowed_no.
     */
    public void setRdoDiscountAllowed_no(boolean rdoDiscountAllowed_no) {
        this.rdoDiscountAllowed_no = rdoDiscountAllowed_no;
    }
    
    /**
     * Getter for property rdoLoanCanbeGranted_no.
     * @return Value of property rdoLoanCanbeGranted_no.
     */
    public boolean getRdoLoanCanbeGranted_no() {
        return rdoLoanCanbeGranted_no;
    }
    
    /**
     * Setter for property rdoLoanCanbeGranted_no.
     * @param rdoLoanCanbeGranted_no New value of property rdoLoanCanbeGranted_no.
     */
    public void setRdoLoanCanbeGranted_no(boolean rdoLoanCanbeGranted_no) {
        this.rdoLoanCanbeGranted_no = rdoLoanCanbeGranted_no;
    }
    
    /**
     * Getter for property rdoLoanCanbeGranted_yes.
     * @return Value of property rdoLoanCanbeGranted_yes.
     */
    public boolean getRdoLoanCanbeGranted_yes() {
        return rdoLoanCanbeGranted_yes;
    }
    
    /**
     * Setter for property rdoLoanCanbeGranted_yes.
     * @param rdoLoanCanbeGranted_yes New value of property rdoLoanCanbeGranted_yes.
     */
    public void setRdoLoanCanbeGranted_yes(boolean rdoLoanCanbeGranted_yes) {
        this.rdoLoanCanbeGranted_yes = rdoLoanCanbeGranted_yes;
    }
    
    /**
     * Getter for property txtMunnalBonusHead.
     * @return Value of property txtMunnalBonusHead.
     */
    public java.lang.String getTxtMunnalBonusHead() {
        return txtMunnalBonusHead;
    }
    
    /**
     * Setter for property txtMunnalBonusHead.
     * @param txtMunnalBonusHead New value of property txtMunnalBonusHead.
     */
    public void setTxtMunnalBonusHead(java.lang.String txtMunnalBonusHead) {
        this.txtMunnalBonusHead = txtMunnalBonusHead;
    }
    
    /**
     * Getter for property cbmholidayInt.
     * @return Value of property cbmholidayInt.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmholidayInt() {
        return cbmholidayInt;
    }
    
    /**
     * Setter for property cbmholidayInt.
     * @param cbmholidayInt New value of property cbmholidayInt.
     */
    public void setCbmholidayInt(com.see.truetransact.clientutil.ComboBoxModel cbmholidayInt) {
        this.cbmholidayInt = cbmholidayInt;
    }
    
    /**
     * Getter for property cboholidayInt.
     * @return Value of property cboholidayInt.
     */
    public java.lang.String getCboholidayInt() {
        return cboholidayInt;
    }
    
    /**
     * Setter for property cboholidayInt.
     * @param cboholidayInt New value of property cboholidayInt.
     */
    public void setCboholidayInt(java.lang.String cboholidayInt) {
        this.cboholidayInt = cboholidayInt;
    }
    
    /**
     * Getter for property rdoBonusGraceDays.
     * @return Value of property rdoBonusGraceDays.
     */
    public boolean getRdoBonusGraceDays() {
        return rdoBonusGraceDays;
    }
    
    /**
     * Setter for property rdoBonusGraceDays.
     * @param rdoBonusGraceDays New value of property rdoBonusGraceDays.
     */
    public void setRdoBonusGraceDays(boolean rdoBonusGraceDays) {
        this.rdoBonusGraceDays = rdoBonusGraceDays;
    }
    
    /**
     * Getter for property rdoBonusGraceMonth.
     * @return Value of property rdoBonusGraceMonth.
     */
    public boolean getRdoBonusGraceMonth() {
        return rdoBonusGraceMonth;
    }
    
    /**
     * Setter for property rdoBonusGraceMonth.
     * @param rdoBonusGraceMonth New value of property rdoBonusGraceMonth.
     */
    public void setRdoBonusGraceMonth(boolean rdoBonusGraceMonth) {
        this.rdoBonusGraceMonth = rdoBonusGraceMonth;
    }
    
    /**
     * Getter for property rdoBonusGraceAfter.
     * @return Value of property rdoBonusGraceAfter.
     */
    public boolean getRdoBonusGraceAfter() {
        return rdoBonusGraceAfter;
    }
    
    /**
     * Setter for property rdoBonusGraceAfter.
     * @param rdoBonusGraceAfter New value of property rdoBonusGraceAfter.
     */
    public void setRdoBonusGraceAfter(boolean rdoBonusGraceAfter) {
        this.rdoBonusGraceAfter = rdoBonusGraceAfter;
    }
    
    /**
     * Getter for property rdoBonusPrizedDays.
     * @return Value of property rdoBonusPrizedDays.
     */
    public boolean getRdoBonusPrizedDays() {
        return rdoBonusPrizedDays;
    }
    
    /**
     * Setter for property rdoBonusPrizedDays.
     * @param rdoBonusPrizedDays New value of property rdoBonusPrizedDays.
     */
    public void setRdoBonusPrizedDays(boolean rdoBonusPrizedDays) {
        this.rdoBonusPrizedDays = rdoBonusPrizedDays;
    }
    
    /**
     * Getter for property rdoBonusPrizedMonth.
     * @return Value of property rdoBonusPrizedMonth.
     */
    public boolean getRdoBonusPrizedMonth() {
        return rdoBonusPrizedMonth;
    }
    
    /**
     * Setter for property rdoBonusPrizedMonth.
     * @param rdoBonusPrizedMonth New value of property rdoBonusPrizedMonth.
     */
    public void setRdoBonusPrizedMonth(boolean rdoBonusPrizedMonth) {
        this.rdoBonusPrizedMonth = rdoBonusPrizedMonth;
    }
    
    /**
     * Getter for property rdoBonusPrizedAfter.
     * @return Value of property rdoBonusPrizedAfter.
     */
    public boolean getRdoBonusPrizedAfter() {
        return rdoBonusPrizedAfter;
    }
    
    /**
     * Setter for property rdoBonusPrizedAfter.
     * @param rdoBonusPrizedAfter New value of property rdoBonusPrizedAfter.
     */
    public void setRdoBonusPrizedAfter(boolean rdoBonusPrizedAfter) {
        this.rdoBonusPrizedAfter = rdoBonusPrizedAfter;
    }
    
    /**
     * Getter for property rdoBonusPrizedEnd.
     * @return Value of property rdoBonusPrizedEnd.
     */
    public boolean getRdoBonusPrizedEnd() {
        return rdoBonusPrizedEnd;
    }
    
    /**
     * Setter for property rdoBonusPrizedEnd.
     * @param rdoBonusPrizedEnd New value of property rdoBonusPrizedEnd.
     */
    public void setRdoBonusPrizedEnd(boolean rdoBonusPrizedEnd) {
        this.rdoBonusPrizedEnd = rdoBonusPrizedEnd;
    }
    
    /**
     * Getter for property rdoDiscountPeriodDays.
     * @return Value of property rdoDiscountPeriodDays.
     */
    public boolean getRdoDiscountPeriodDays() {
        return rdoDiscountPeriodDays;
    }
    
    /**
     * Setter for property rdoDiscountPeriodDays.
     * @param rdoDiscountPeriodDays New value of property rdoDiscountPeriodDays.
     */
    public void setRdoDiscountPeriodDays(boolean rdoDiscountPeriodDays) {
        this.rdoDiscountPeriodDays = rdoDiscountPeriodDays;
    }
    
    /**
     * Getter for property rdoDiscountPeriodMonth.
     * @return Value of property rdoDiscountPeriodMonth.
     */
    public boolean getRdoDiscountPeriodMonth() {
        return rdoDiscountPeriodMonth;
    }
    
    /**
     * Setter for property rdoDiscountPeriodMonth.
     * @param rdoDiscountPeriodMonth New value of property rdoDiscountPeriodMonth.
     */
    public void setRdoDiscountPeriodMonth(boolean rdoDiscountPeriodMonth) {
        this.rdoDiscountPeriodMonth = rdoDiscountPeriodMonth;
    }
    
    /**
     * Getter for property rdoDiscountPeriodAfter.
     * @return Value of property rdoDiscountPeriodAfter.
     */
    public boolean getRdoDiscountPeriodAfter() {
        return rdoDiscountPeriodAfter;
    }
    
    /**
     * Setter for property rdoDiscountPeriodAfter.
     * @param rdoDiscountPeriodAfter New value of property rdoDiscountPeriodAfter.
     */
    public void setRdoDiscountPeriodAfter(boolean rdoDiscountPeriodAfter) {
        this.rdoDiscountPeriodAfter = rdoDiscountPeriodAfter;
    }
    
    /**
     * Getter for property rdoDiscountPeriodEnd.
     * @return Value of property rdoDiscountPeriodEnd.
     */
    public boolean getRdoDiscountPeriodEnd() {
        return rdoDiscountPeriodEnd;
    }
    
    /**
     * Setter for property rdoDiscountPeriodEnd.
     * @param rdoDiscountPeriodEnd New value of property rdoDiscountPeriodEnd.
     */
    public void setRdoDiscountPeriodEnd(boolean rdoDiscountPeriodEnd) {
        this.rdoDiscountPeriodEnd = rdoDiscountPeriodEnd;
    }
    
    /**
     * Getter for property rdoDiscountPrizedPeriodDays.
     * @return Value of property rdoDiscountPrizedPeriodDays.
     */
    public boolean getRdoDiscountPrizedPeriodDays() {
        return rdoDiscountPrizedPeriodDays;
    }
    
    /**
     * Setter for property rdoDiscountPrizedPeriodDays.
     * @param rdoDiscountPrizedPeriodDays New value of property rdoDiscountPrizedPeriodDays.
     */
    public void setRdoDiscountPrizedPeriodDays(boolean rdoDiscountPrizedPeriodDays) {
        this.rdoDiscountPrizedPeriodDays = rdoDiscountPrizedPeriodDays;
    }
    
    /**
     * Getter for property rdoDiscountPrizedPeriodMonth.
     * @return Value of property rdoDiscountPrizedPeriodMonth.
     */
    public boolean getRdoDiscountPrizedPeriodMonth() {
        return rdoDiscountPrizedPeriodMonth;
    }
    
    /**
     * Setter for property rdoDiscountPrizedPeriodMonth.
     * @param rdoDiscountPrizedPeriodMonth New value of property rdoDiscountPrizedPeriodMonth.
     */
    public void setRdoDiscountPrizedPeriodMonth(boolean rdoDiscountPrizedPeriodMonth) {
        this.rdoDiscountPrizedPeriodMonth = rdoDiscountPrizedPeriodMonth;
    }
    
    /**
     * Getter for property rdoDiscountPrizedPeriodAfter.
     * @return Value of property rdoDiscountPrizedPeriodAfter.
     */
    public boolean getRdoDiscountPrizedPeriodAfter() {
        return rdoDiscountPrizedPeriodAfter;
    }
    
    /**
     * Setter for property rdoDiscountPrizedPeriodAfter.
     * @param rdoDiscountPrizedPeriodAfter New value of property rdoDiscountPrizedPeriodAfter.
     */
    public void setRdoDiscountPrizedPeriodAfter(boolean rdoDiscountPrizedPeriodAfter) {
        this.rdoDiscountPrizedPeriodAfter = rdoDiscountPrizedPeriodAfter;
    }
    
    /**
     * Getter for property rdoDiscountPrizedPeriodEnd.
     * @return Value of property rdoDiscountPrizedPeriodEnd.
     */
    public boolean getRdoDiscountPrizedPeriodEnd() {
        return rdoDiscountPrizedPeriodEnd;
    }
    
    /**
     * Setter for property rdoDiscountPrizedPeriodEnd.
     * @param rdoDiscountPrizedPeriodEnd New value of property rdoDiscountPrizedPeriodEnd.
     */
    public void setRdoDiscountPrizedPeriodEnd(boolean rdoDiscountPrizedPeriodEnd) {
        this.rdoDiscountPrizedPeriodEnd = rdoDiscountPrizedPeriodEnd;
    }
    
    /**
     * Getter for property rdoBonusGraceEnd.
     * @return Value of property rdoBonusGraceEnd.
     */
    public boolean getRdoBonusGraceEnd() {
        return rdoBonusGraceEnd;
    }
    
    /**
     * Setter for property rdoBonusGraceEnd.
     * @param rdoBonusGraceEnd New value of property rdoBonusGraceEnd.
     */
    public void setRdoBonusGraceEnd(boolean rdoBonusGraceEnd) {
        this.rdoBonusGraceEnd = rdoBonusGraceEnd;
    }
    
    /**
     * Getter for property chkAcceptClassA.
     * @return Value of property chkAcceptClassA.
     */
    public boolean getChkAcceptClassA() {
        return chkAcceptClassA;
    }
    
    /**
     * Setter for property chkAcceptClassA.
     * @param chkAcceptClassA New value of property chkAcceptClassA.
     */
    public void setChkAcceptClassA(boolean chkAcceptClassA) {
        this.chkAcceptClassA = chkAcceptClassA;
    }
    
    /**
     * Getter for property chkAcceptClassB.
     * @return Value of property chkAcceptClassB.
     */
    public boolean getChkAcceptClassB() {
        return chkAcceptClassB;
    }
    
    /**
     * Setter for property chkAcceptClassB.
     * @param chkAcceptClassB New value of property chkAcceptClassB.
     */
    public void setChkAcceptClassB(boolean chkAcceptClassB) {
        this.chkAcceptClassB = chkAcceptClassB;
    }
    
    /**
     * Getter for property chkAcceptClassC.
     * @return Value of property chkAcceptClassC.
     */
    public boolean getChkAcceptClassC() {
        return chkAcceptClassC;
    }
    
    /**
     * Setter for property chkAcceptClassC.
     * @param chkAcceptClassC New value of property chkAcceptClassC.
     */
    public void setChkAcceptClassC(boolean chkAcceptClassC) {
        this.chkAcceptClassC = chkAcceptClassC;
    }
    
    /**
     * Getter for property chkAcceptClassAll.
     * @return Value of property chkAcceptClassAll.
     */
    public boolean getChkAcceptClassAll() {
        return chkAcceptClassAll;
    }
    
    /**
     * Setter for property chkAcceptClassAll.
     * @param chkAcceptClassAll New value of property chkAcceptClassAll.
     */
    public void setChkAcceptClassAll(boolean chkAcceptClassAll) {
        this.chkAcceptClassAll = chkAcceptClassAll;
    }

    /**
     * Getter for property chkAcceptableClassA.
     * @return Value of property chkAcceptableClassA.
     */
    public boolean getChkAcceptableClassA() {
        return chkAcceptableClassA;
    }
    
    /**
     * Setter for property chkAcceptableClassA.
     * @param chkAcceptableClassA New value of property chkAcceptableClassA.
     */
    public void setChkAcceptableClassA(boolean chkAcceptableClassA) {
        this.chkAcceptableClassA = chkAcceptableClassA;
    }
    
    /**
     * Getter for property chkAcceptableClassB.
     * @return Value of property chkAcceptableClassB.
     */
    public boolean getChkAcceptableClassB() {
        return chkAcceptableClassB;
    }
    
    /**
     * Setter for property chkAcceptableClassB.
     * @param chkAcceptableClassB New value of property chkAcceptableClassB.
     */
    public void setChkAcceptableClassB(boolean chkAcceptableClassB) {
        this.chkAcceptableClassB = chkAcceptableClassB;
    }
    
    /**
     * Getter for property chkAcceptableClassC.
     * @return Value of property chkAcceptableClassC.
     */
    public boolean getChkAcceptableClassC() {
        return chkAcceptableClassC;
    }
    
    /**
     * Setter for property chkAcceptableClassC.
     * @param chkAcceptableClassC New value of property chkAcceptableClassC.
     */
    public void setChkAcceptableClassC(boolean chkAcceptableClassC) {
        this.chkAcceptableClassC = chkAcceptableClassC;
    }
    
    /**
     * Getter for property chkAcceptableClassAll.
     * @return Value of property chkAcceptableClassAll.
     */
    public boolean getChkAcceptableClassAll() {
        return chkAcceptableClassAll;
    }
    
    /**
     * Setter for property chkAcceptableClassAll.
     * @param chkAcceptableClassAll New value of property chkAcceptableClassAll.
     */
    public void setChkAcceptableClassAll(boolean chkAcceptableClassAll) {
        this.chkAcceptableClassAll = chkAcceptableClassAll;
    }
    
    /**
     * Getter for property rdoBonusAmt_Yes.
     * @return Value of property rdoBonusAmt_Yes.
     */
    public boolean getRdoBonusAmt_Yes() {
        return rdoBonusAmt_Yes;
    }
    
    /**
     * Setter for property rdoBonusAmt_Yes.
     * @param rdoBonusAmt_Yes New value of property rdoBonusAmt_Yes.
     */
    public void setRdoBonusAmt_Yes(boolean rdoBonusAmt_Yes) {
        this.rdoBonusAmt_Yes = rdoBonusAmt_Yes;
    }
    
    /**
     * Getter for property rdoBonusAmt_No.
     * @return Value of property rdoBonusAmt_No.
     */
    public boolean getRdoBonusAmt_No() {
        return rdoBonusAmt_No;
    }
    
    /**
     * Setter for property rdoBonusAmt_No.
     * @param rdoBonusAmt_No New value of property rdoBonusAmt_No.
     */
    public void setRdoBonusAmt_No(boolean rdoBonusAmt_No) {
        this.rdoBonusAmt_No = rdoBonusAmt_No;
    }
    
    /**
     * Getter for property rdoBonusPayFirstIns_Yes.
     * @return Value of property rdoBonusPayFirstIns_Yes.
     */
    public boolean getRdoBonusPayFirstIns_Yes() {
        return rdoBonusPayFirstIns_Yes;
    }
    
    /**
     * Setter for property rdoBonusPayFirstIns_Yes.
     * @param rdoBonusPayFirstIns_Yes New value of property rdoBonusPayFirstIns_Yes.
     */
    public void setRdoBonusPayFirstIns_Yes(boolean rdoBonusPayFirstIns_Yes) {
        this.rdoBonusPayFirstIns_Yes = rdoBonusPayFirstIns_Yes;
    }
    
    /**
     * Getter for property rdoBonusPayFirstIns_No.
     * @return Value of property rdoBonusPayFirstIns_No.
     */
    public boolean getRdoBonusPayFirstIns_No() {
        return rdoBonusPayFirstIns_No;
    }
    
    /**
     * Setter for property rdoBonusPayFirstIns_No.
     * @param rdoBonusPayFirstIns_No New value of property rdoBonusPayFirstIns_No.
     */
    public void setRdoBonusPayFirstIns_No(boolean rdoBonusPayFirstIns_No) {
        this.rdoBonusPayFirstIns_No = rdoBonusPayFirstIns_No;
    }
    
    /**
     * Getter for property rdoAdvanceCollection_Yes.
     * @return Value of property rdoAdvanceCollection_Yes.
     */
    public boolean getRdoAdvanceCollection_Yes() {
        return rdoAdvanceCollection_Yes;
    }
    
    /**
     * Setter for property rdoAdvanceCollection_Yes.
     * @param rdoAdvanceCollection_Yes New value of property rdoAdvanceCollection_Yes.
     */
    public void setRdoAdvanceCollection_Yes(boolean rdoAdvanceCollection_Yes) {
        this.rdoAdvanceCollection_Yes = rdoAdvanceCollection_Yes;
    }
    
    /**
     * Getter for property rdoAdvanceCollection_NO.
     * @return Value of property rdoAdvanceCollection_NO.
     */
    public boolean getRdoAdvanceCollection_NO() {
        return rdoAdvanceCollection_NO;
    }
    
    /**
     * Setter for property rdoAdvanceCollection_NO.
     * @param rdoAdvanceCollection_NO New value of property rdoAdvanceCollection_NO.
     */
    public void setRdoAdvanceCollection_NO(boolean rdoAdvanceCollection_NO) {
        this.rdoAdvanceCollection_NO = rdoAdvanceCollection_NO;
    }
    
    /**
     * Getter for property cbmRoundOffCriteria.
     * @return Value of property cbmRoundOffCriteria.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRoundOffCriteria() {
        return cbmRoundOffCriteria;
    }    
    
    /**
     * Setter for property cbmRoundOffCriteria.
     * @param cbmRoundOffCriteria New value of property cbmRoundOffCriteria.
     */
    public void setCbmRoundOffCriteria(com.see.truetransact.clientutil.ComboBoxModel cbmRoundOffCriteria) {
        this.cbmRoundOffCriteria = cbmRoundOffCriteria;
    }    
    
    /**
     * Getter for property cboRoundOffCriteria.
     * @return Value of property cboRoundOffCriteria.
     */
    public java.lang.String getCboRoundOffCriteria() {
        return cboRoundOffCriteria;
    }
    
    /**
     * Setter for property cboRoundOffCriteria.
     * @param cboRoundOffCriteria New value of property cboRoundOffCriteria.
     */
    public void setCboRoundOffCriteria(java.lang.String cboRoundOffCriteria) {
        this.cboRoundOffCriteria = cboRoundOffCriteria;
    }
    
    /**
     * Getter for property rdoTransFirstIns_Yes.
     * @return Value of property rdoTransFirstIns_Yes.
     */
    public boolean isRdoTransFirstIns_Yes() {
        return rdoTransFirstIns_Yes;
    }    

    /**
     * Setter for property rdoTransFirstIns_Yes.
     * @param rdoTransFirstIns_Yes New value of property rdoTransFirstIns_Yes.
     */
    public void setRdoTransFirstIns_Yes(boolean rdoTransFirstIns_Yes) {
        this.rdoTransFirstIns_Yes = rdoTransFirstIns_Yes;
    }   
      public boolean getRdoTransFirstIns_Yes() {
      return  rdoTransFirstIns_Yes ;
    }   
    
    /**
     * Getter for property rdoTransFirstIns_No.
     * @return Value of property rdoTransFirstIns_No.
     */
    public boolean isRdoTransFirstIns_No() {
        return rdoTransFirstIns_No;
    }
    
    /**
     * Setter for property rdoTransFirstIns_No.
     * @param rdoTransFirstIns_No New value of property rdoTransFirstIns_No.
     */
    public void setRdoTransFirstIns_No(boolean rdoTransFirstIns_No) {
        this.rdoTransFirstIns_No = rdoTransFirstIns_No;
    }
    
    public ComboBoxModel getCbmPenalIntRateFullInstAmt() {
        return cbmPenalIntRateFullInstAmt;
    }
    public void setCbmPenalIntRateFullInstAmt(ComboBoxModel cbmPenalIntRateFullInstAmt) {
        this.cbmPenalIntRateFullInstAmt = cbmPenalIntRateFullInstAmt;
    }
    public ComboBoxModel getCbmPenalPrizedIntRateFullInstAmt() {
        return cbmPenalPrizedIntRateFullInstAmt;
    }
    public void setCbmPenalPrizedIntRateFullInstAmt(ComboBoxModel cbmPenalPrizedIntRateFullInstAmt) {
        this.cbmPenalPrizedIntRateFullInstAmt = cbmPenalPrizedIntRateFullInstAmt;
    }
    public String getCboPenalIntRateFullInstAmt() {
        return cboPenalIntRateFullInstAmt;
    }
    public void setCboPenalIntRateFullInstAmt(String cboPenalIntRateFullInstAmt) {
        this.cboPenalIntRateFullInstAmt = cboPenalIntRateFullInstAmt;
    }
    public String getCboPenalPrizedIntRateFullInstAmt() {
        return cboPenalPrizedIntRateFullInstAmt;
    }
    public void setCboPenalPrizedIntRateFullInstAmt(String cboPenalPrizedIntRateFullInstAmt) {
        this.cboPenalPrizedIntRateFullInstAmt = cboPenalPrizedIntRateFullInstAmt;
    }

    public boolean isChkAfterCashPayment() {
        return chkAfterCashPayment;
    }

    public void setChkAfterCashPayment(boolean chkAfterCashPayment) {
        this.chkAfterCashPayment = chkAfterCashPayment;
    }

    public boolean isChkFromAuctionEnrtry() {
        return chkFromAuctionEnrtry;
    }

    public void setChkFromAuctionEnrtry(boolean chkFromAuctionEnrtry) {
        this.chkFromAuctionEnrtry = chkFromAuctionEnrtry;
    }

    public boolean isChkSplitMDSTransaction() {
        return chkSplitMDSTransaction;
    }

    public void setChkSplitMDSTransaction(boolean chkSplitMDSTransaction) {
        this.chkSplitMDSTransaction = chkSplitMDSTransaction;
    }

    public ComboBoxModel getCbmPaymentTimeCharges() {
        return cbmPaymentTimeCharges;
    }

    public void setCbmPaymentTimeCharges(ComboBoxModel cbmPaymentTimeCharges) {
        this.cbmPaymentTimeCharges = cbmPaymentTimeCharges;
    }

    public String getCboPaymentTimeCharges() {
        return cboPaymentTimeCharges;
    }

    public void setCboPaymentTimeCharges(String cboPaymentTimeCharges) {
        this.cboPaymentTimeCharges = cboPaymentTimeCharges;
    }

    public Double getTxtpaymentTimeCharges() {
        return txtpaymentTimeCharges;
    }

    public void setTxtpaymentTimeCharges(Double txtpaymentTimeCharges) {
        this.txtpaymentTimeCharges = txtpaymentTimeCharges;
    }

    public ComboBoxModel getCbmMunnalTransCategory() {
        return cbmMunnalTransCategory;
    }

    public void setCbmMunnalTransCategory(ComboBoxModel cbmMunnalTransCategory) {
        this.cbmMunnalTransCategory = cbmMunnalTransCategory;
    }

    public ComboBoxModel getCbmThalayalTransCategory() {
        return cbmThalayalTransCategory;
    }

    public void setCbmThalayalTransCategory(ComboBoxModel cbmThalayalTransCategory) {
        this.cbmThalayalTransCategory = cbmThalayalTransCategory;
    }

    public String getCboMunnalTransCategory() {
        return cboMunnalTransCategory;
    }

    public void setCboMunnalTransCategory(String cboMunnalTransCategory) {
        this.cboMunnalTransCategory = cboMunnalTransCategory;
    }

    public String getCboThalayalTransCategory() {
        return cboThalayalTransCategory;
    }

    public void setCboThalayalTransCategory(String cboThalayalTransCategory) {
        this.cboThalayalTransCategory = cboThalayalTransCategory;
    }   
    
    
}