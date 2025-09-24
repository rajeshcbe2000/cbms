/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * OperativeAcctProductOB.java
 *
 * Created on August IDAYS3, 2003, IDAYS2:37 PM
 */

package com.see.truetransact.ui.product.operativeacct;

import java.util.Observable;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.LocaleConstants;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.transferobject.product.operativeacct.*;

import com.see.truetransact.clientexception.ClientParseException;

import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyParameters;
/**
 * @author  balachandar
 *
 * @modified Pinky
 * @modified Rahul
 * @modified Jayakrishnan.
 */
public class OperativeAcctProductOB extends CObservable {
    Date curDate = null;
    private HashMap map = null;
    private HashMap mapIntRate = new HashMap();
    private ArrayList arrInsert = new ArrayList();
    private ArrayList arrEdit = new ArrayList();
    private ArrayList arrDelete = new ArrayList();
    
    private ProxyFactory proxy = null;
    private int _actionType;
    private int _result;
    
    private final String MANUAL = "MANUAL";
    private final String SYSTEM = "SYSTEM";
    private final String BOTH = "BOTH";
    private final String CREDIT = "CREDIT";
    private final String DEBIT = "DEBIT";
    
    private final String REGULAR = "REGULAR";
    private final String NRO = "NRO";
    private final String NRE = "NRE";
    
    


    private final String DAYS = "DAYS";
    private final String MONTHS = "MONTHS";
    private final String YEARS = "YEARS";
    private final String YES = "Y";
    private final String NO = "N";
    
    private final int IDAYS = 1;
    private final int IMONTHS = 30;
    private final int IYEARS = 365;
    
    private ComboBoxModel cbmBehaves;
    private ComboBoxModel cbmPeriodNew;
    private ComboBoxModel cbmPeriodDormant;
    private ComboBoxModel cbmPeriodInop;
    private ComboBoxModel cbmPeriodCloser;
    private ComboBoxModel cbmStatFreq;
    private ComboBoxModel cbmWDPeriod;
    private ComboBoxModel cbmChqPeriod;
    private ComboBoxModel cbmDrCalcFreq;
    private ComboBoxModel cbmDrApplFreq;
    private ComboBoxModel cbmDrCompFreq;
    private ComboBoxModel cbmDrProdRound;
    private ComboBoxModel cbmDrIntRound;
    private ComboBoxModel cbmCrCalcFreq;
    private ComboBoxModel cbmCrApplFreq;
    private ComboBoxModel cbmCrCompFreq;
    private ComboBoxModel cbmCrProdRound;
    private ComboBoxModel cbmCrIntRound;
    private ComboBoxModel cbmCalcCrit;
    private ComboBoxModel cbmCrProdFreq;
    private ComboBoxModel cbmNonMain;
    private ComboBoxModel cbmAmtStatChrg;
    private ComboBoxModel cbmFolioChrgApplFreq;
    private ComboBoxModel cbmCollFolioChrg;
    private ComboBoxModel cbmIncFolioROffFreq;
    private ComboBoxModel cbmIntCalcEndMon;
    private ComboBoxModel cbmStMonIntCalc;
    private ComboBoxModel cbmProdFreq;
    private ComboBoxModel cbmInOpAcChrgPd;
    private ComboBoxModel cbmFlexiTD;
//    private ComboBoxModel cbmIntCategory;
    
    private ComboBoxModel cbmProdCurrency;
    
    private ComboBoxModel cbmStartInterCalc;
    private ComboBoxModel cbmEndInterCalc;
    private ComboBoxModel cbmStartProdCalc;
    private ComboBoxModel cbmEndProdCalc;
    
    
    private ComboBoxModel cbmFreeWDStFrom;
    private ComboBoxModel cbmFreeChkLeaveStFrom;
    private ComboBoxModel cbmStDayProdCalcSBCrInt;
    private ComboBoxModel cbmEndDayProdCalcSBCrInt;
    private ComboBoxModel cbmFolioChargedbefore;
    
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
//    private TableModel tmlIntRate;
//    private ArrayList operativeAcctIntTOs;
//    private OperativeAcctProductnewRB objOperativeAcctProductnewRB = new OperativeAcctProductnewRB();
    java.util.ResourceBundle objOperativeAcctProductnewRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.product.operativeacct.OperativeAcctProductnewRB", ProxyParameters.LANGUAGE);
    ArrayList tableHeader = new ArrayList();
    
    //Table Columns
    private final int accHD = 0;
    private final int categoryID = 1;
    private final int date = 2;
    private final int rate = 3;
        
    private String txtProductID = "";
    private String txtDesc = "";
    private String cboBehaves = "";
    private String txtMinBalChkbk = "";
    private String txtMinBalwchk = "";
    private boolean rdoChkAllowedYes = false;
    private boolean rdoChkAllowedNo = false;
    private String txtNoNominees = "";
    private boolean rdoAcctOpenApprYes = false;
    private boolean rdoAcctOpenApprNo = false;
    private boolean rdoNomineeReqYes = false;
    private boolean rdoNomineeReqNo = false;
    private boolean rdoIntroReqYes = false;
    private boolean rdoIntroReqNo = false;
    private String txtMainTreatNewAcctClosure = "";
    private String txtMinTreatasDormant = "";
    private String txtMinTreatasNew = "";
    private String txtMinTreatasInOp = "";
    private String cboMinTreatasDormant = "";
    private String cboMinTreatInOp = "";
    private String cboMinTreatasNew = "";
    private String cboMinTreatNewClosure = "";
    private String cboStatFreq = "";
    private String txtAcctHd = "";
    private String txtNoFreeChkLeaves = "";
    private String txtRateTaxNRO = "";
    private String txtMaxAmtWDSlip = "";
    private boolean rdoAllowWDYes = false;
    private boolean rdoAllowWDNo = false;
    private boolean rdoIntClearing_Yes = false;
    private boolean rdoIntClearing_No = false;
    private boolean rdoStaffAcctYes = false;
    private boolean rdoStaffAcctNo = false;
    private boolean rdoCollectIntYes = false;
    private boolean rdoCollectIntNo = false;
    private boolean rdoLimitDefAllowYes = false;
    private boolean rdoLimitDefAllowNo = false;
    private String txtNoFreeWD = "";
    private boolean rdoIntUnClearBalYes = false;
    private boolean rdoIntUnClearBalNo = false;
    private boolean rdoTempODAllowYes = false;
    private boolean rdoTempODAllowNo = false;
    //    private boolean rdoExtraIntApplYes = false;
    //    private boolean rdoExtraIntApplNo = false;
    private boolean rdoIssueTokenYes = false;
    private boolean rdoIssueTokenNo = false;
    private boolean rdoTaxIntApplNROYes = false;
    private boolean rdoTaxIntApplNRONo = false;
    private String tdtFreeWDStFrom = "";
    private String tdtFreeChkLeaveStFrom = "";
    private String txtFreeWDPd = "";
    private String cboFreeWDPd = "";
    private String txtFreeChkPD = "";
    private String cboFreeChkPd = "";
    private boolean rdoDebitCompReqYes = false;
    private boolean rdoDebitCompReqNo = false;
    private String txtMinDebitIntRate = "";
    private String txtMaxDebitIntRate = "";
    private String txtApplDebitIntRate = "";
    private String txtMinDebitIntAmt = "";
    private String txtMaxDebitIntAmt = "";
    private String cboDebitIntCalcFreq = "";
    private String cboDebitIntApplFreq = "";
    private String cboDebitIntRoundOff = "";
    private String cboDebitCompIntCalcFreq = "";
    private String cboDebitProductRoundOff = "";
    private String cboProdCurrency = "";
    private String cboProdFreq = "";
    private String tdtLastIntCalcDate = "";
    private String tdtLastIntApplDate = "";
    private boolean rdoDebitIntChrgYes2 = false;
    private boolean rdoDebitIntChrgNo2 = false;
    private String txtPenalIntDebitBalAcct = "";
    private String txtPenalIntChrgStart = "";
    private String txtMinCrIntRate = "";
    private String txtMaxCrIntRate = "";
    private String txtApplCrIntRate = "";
    private String txtMinCrIntAmt = "";
    private String txtMaxCrIntAmt = "";
    private String txtStDayProdCalcSBCrInt = "";
    private String txtEndDayProdCalcSBCrInt = "";
    private String cboCrIntCalcFreq = "";
    private String cboCrIntApplFreq = "";
    private String cboStMonIntCalc = "";
    private String cboIntCalcEndMon = "";
    private String tdtLastIntCalcDateCR = "";
    private String tdtLastIntApplDateCr = "";
    private String tdtLastFolioChargeDt="";
    private String tdtNextFolioDueDate="";
    private String minBalForIntCalc="";
    
    private String txtStartInterCalc = "";
    private String cboStartInterCalc = "";
    private String txtEndInterCalc = "";
    private String cboEndInterCalc = "";
    
    private String txtStartProdCalc = "";
    private String cboStartProdCalc = "";
    private String txtEndProdCalc = "";
    private String cboEndProdCalc = "";
    
    private boolean rdoCreditCompYes = false;
    private boolean rdoCreditCompNo = false;
    private String cboCreditCompIntCalcFreq = "";
    private String cboCreditProductRoundOff = "";
    private String cboCreditIntRoundOff = "";
    private String cboCalcCriteria = "";
    private String cboProdFreqCr = "";
    private boolean rdoCrIntGivenYes = false;
    private boolean rdoCrIntGivenNo = false;
    //    private String txtAddIntStaff = "";
    private String cboStDayProdCalcSBCrInt = "";
    private String cboEndDayProdCalcSBCrInt = "";
    private String txtIntCalcEndMon = "";
    private String txtStMonIntCalc = "";
    
    private String cboInOpAcChrgPd = "";
    private String txtlInOpAcChrg = "";
    private String txtChrgPreClosure = "";
    private String txtAcClosingChrg = "";
    private String txtChrgMiscServChrg = "";
    private boolean rdoNonMainMinBalChrg_Yes = false;
    private boolean rdoNonMainMinBalChrg_No = false;
    private boolean rdoChkIssuedChrgCh_Yes = false;
    private boolean rdoChkIssuedChrgCh_No = false;
    private boolean rdoFolioChargeAppl_Yes = false;
    private boolean rdoFolioChargeAppl_No = false;
    private boolean rdoFolioToChargeOn_Credit = false;
    private boolean rdoFolioToChargeOn_Debit = false;
    private boolean rdoFolioToChargeOn_Both = false;
    private boolean rdoStopPaymentChrg_Yes = false;
    private boolean rdoStopPaymentChrg_No = false;
    private boolean rdoStatCharges_Yes = false;
    private boolean rdoStatCharges_No = false;
    private String txtChkBkIssueChrgPL = "";
    private String txtStopPayChrg = "";
    private String txtChkRetChrOutward = "";
    private String txtChkRetChrgIn = "";
    private String txtAcctOpenCharges = "";
    private String txtNoEntryPerFolio = "";
    private String txtRatePerFolio = "";
    private String txtExcessFreeWDChrgPT = "";
    private String cboFolioChrgApplFreq = "";
    private String cboToCollectFolioChrg = "";
    private String cboIncompFolioROffFreq = "";
    private String txtMinBalAmt = "";
    private String cboMinBalAmt = "";
    private String txtStatChargesChr = "";
    private String cboStatChargesChr = "";
    private boolean rdoToChargeOnApplFreq_Manual = false;
    private boolean rdoToChargeOnApplFreq_System = false;
    private boolean rdoToChargeOnApplFreq_Both = false;
    private boolean rdoFlexiHappen_SB = false;
    private boolean rdoFlexiHappen_TD = false;
    private boolean rdoLinkFlexiAcct_Yes = false;
    private boolean rdoLinkFlexiAcct_No = false;
    private String txtMinBal1FlexiDep = "";
    private String txtMinBal2FlexiDep = "";
    private boolean rdoATMIssuedYes = false;
    private boolean rdoATMIssuedNo = false;
    private boolean rdoABBAllowed_Yes = false;
    private boolean rdoABBAllowed_No = false;
    private boolean rdoMobBankClient_Yes = false;
    private boolean rdoMobBankClient_No = false;
    private boolean rdoIVRSProvided_Yes = false;
    private boolean rdoIVRSProvided_No = false;
    private boolean rdoDebitCdIssued_Yes = false;
    private boolean rdoDebitCdIssued_No = false;
    private boolean rdoCreditCdIssued_Yes = false;
    private boolean rdoCreditCdIssued_No = false;
    private String txtMinATMBal = "";
    private String txtMinBalCreditCd = "";
    private String txtMinBalDebitCards = "";
    private String txtMinBalIVRS = "";
    private String txtMinMobBank = "";
    private String txtMinBalABB = "";
    private String txtIMPSLimit = "";
    private String txtInOpChrg = "";
    private String txtPrematureClosureChrg = "";
    private String txtAcctClosingChrg = "";
    private String txtMiscServChrg = "";
    private String txtStatChrg = "";
    private String txtFreeWDChrg = "";
    private String txtAcctDebitInt = "";
    private String txtAcctCrInt = "";
    private String txtChkBkIssueChrg = "";
    private String txtClearingIntAcctHd = "";
    private String txtStopPaymentChrg = "";
    private String txtOutwardChkRetChrg = "";
    private String txtInwardChkRetChrg = "";
    private String txtAcctOpenChrg = "";
    private String txtExcessFreeWDChrg = "";
    private String txtTaxGL = "";
    private String txtNonMainMinBalChrg = "";
    private String txtInOperative = "";
    private String txtFolioChrg = "";
    private String txtATMGL = "";
    private String cboFlexiTD = "";
    
    private String cboFreeWDStFrom = "";
    private String cboFreeChkLeaveStFrom = "";
    private String txtNumPatternFollowedPrefix = "";
    private String txtNumPatternFollowedSuffix = "";
    private String txtLastAccNum = "";
    
    private boolean rdoAcc_Reg = false;
    private boolean rdoAcc_Nro = false;
    private boolean rdoAcc_Nre = false;
    private String  txtFolioChargeType="";
    
    // Added by nithya on 16-03-2016 for 0004021
    // Changes in charges tab
    private String chkDebitWithdrawalCharge = "";
    private String txtDebitWithdrawalChargePeriod = "";
    private String cboDebitChargeType = "";
    private String txtDebitChargeTypeRate = "";
    // Chages in account head tab
    private String txtDebitWithdrawalChargeAcctHead = "";
    private String txtDebitWithdrawalChargeAcctHeadDesc = "";
    private String txtFolioRestrictionPriod="";
    private String cboFolioChargeRestritionFreq = "";
    
    /** Creates a new instance of AccountHead */
    public OperativeAcctProductOB() {
        curDate = ClientUtil.getCurrentDate();
        map = new HashMap();
        map.put(CommonConstants.JNDI, "OperativeAcctProductJNDI");
        map.put(CommonConstants.HOME, "product.operativeacct.OperativeAcctProductHome");
        map.put(CommonConstants.REMOTE, "product.operativeacct.OperativeAcctProduct");
        
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }
        populateCombo();
//        setCoulmnHeader();
//        tmlIntRate = new TableModel(new ArrayList(),tableHeader);
//        operativeAcctIntTOs = new ArrayList();
    }
    
    private void populateCombo() {
        HashMap param = new HashMap();
        ArrayList lookupKey = new ArrayList();
        HashMap lookupValues;
        HashMap keyValue;
        ArrayList key;
        ArrayList value;
        
        //for multiple lookup
        param.put(CommonConstants.MAP_NAME,null);
        
        lookupKey.add("OPERATIVEACCTPRODUCT.BEHAVES");
        lookupKey.add("PERIOD");
        lookupKey.add("FREQUENCY");
        lookupKey.add("OPERATIVEACCTPRODUCT.CALCCRITERIA");
        lookupKey.add("OPERATIVEACCTPRODUCT.FOLIOCHRG");
        lookupKey.add("OPERATIVEACCTPRODUCT.INTROUNDOFF");
        lookupKey.add("OPERATIVEACCTPRODUCT.NONMINBAL");
        lookupKey.add("OPERATIVEACCTPRODUCT.ROUNDOFF");
        lookupKey.add("OPERATIVEACCTPRODUCT.STATCHRG");
        lookupKey.add("MONTH");
        lookupKey.add("OPERATIVEACCTPRODUCT.PRODFREQ");
        lookupKey.add("FOREX.CURRENCY");
        lookupKey.add("OP_STARTS_FROM");
        lookupKey.add("OPERATIVE_ACCT.CHARGETYPE"); // Added by nithya on 17-03-2016 for 0004021
        lookupKey.add("FOLIO_PERIOD");
        param.put(CommonConstants.PARAMFORQUERY, lookupKey);
        lookupValues = ClientUtil.populateLookupData(param);
        
        keyValue = (HashMap)lookupValues.get("OPERATIVEACCTPRODUCT.BEHAVES");
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
        cbmBehaves = new ComboBoxModel(key,value);
        
        keyValue = (HashMap)lookupValues.get("PERIOD");
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
        cbmChqPeriod = new ComboBoxModel(key,value);
        cbmPeriodCloser = new ComboBoxModel(key,value);
        cbmPeriodDormant = new ComboBoxModel(key,value);
        cbmPeriodInop = new ComboBoxModel(key,value);
        cbmPeriodNew = new ComboBoxModel(key,value);
        cbmWDPeriod = new ComboBoxModel(key,value);
        
        keyValue = (HashMap)lookupValues.get("FREQUENCY");
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
        cbmCrApplFreq = new ComboBoxModel(key,value);
        cbmCrCalcFreq = new ComboBoxModel(key,value);
        cbmCrCompFreq = new ComboBoxModel(key,value);
        cbmCrProdFreq = new ComboBoxModel(key,value);
        cbmDrApplFreq = new ComboBoxModel(key,value);
        cbmDrCalcFreq = new ComboBoxModel(key,value);
        cbmDrCompFreq = new ComboBoxModel(key,value);
        cbmStatFreq = new ComboBoxModel(key,value);
        cbmFolioChrgApplFreq = new ComboBoxModel(key,value);
        cbmInOpAcChrgPd = new ComboBoxModel(key,value);
        cbmProdFreq = new ComboBoxModel(key,value);
        
        keyValue = (HashMap)lookupValues.get("OPERATIVEACCTPRODUCT.CALCCRITERIA");
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
        cbmCalcCrit = new ComboBoxModel(key,value);
        
        keyValue = (HashMap)lookupValues.get("OPERATIVEACCTPRODUCT.FOLIOCHRG");
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
        cbmCollFolioChrg = new ComboBoxModel(key,value);
        
        keyValue = (HashMap)lookupValues.get("OPERATIVEACCTPRODUCT.INTROUNDOFF");
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
        cbmCrIntRound = new ComboBoxModel(key,value);
        cbmDrIntRound = new ComboBoxModel(key,value);
        
        keyValue = (HashMap)lookupValues.get("OPERATIVEACCTPRODUCT.NONMINBAL");
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
        cbmNonMain = new ComboBoxModel(key,value);
        
        keyValue = (HashMap)lookupValues.get("OPERATIVEACCTPRODUCT.ROUNDOFF");
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
        cbmCrProdRound = new ComboBoxModel(key,value);
        cbmDrProdRound = new ComboBoxModel(key,value);
        
        cbmIncFolioROffFreq = new ComboBoxModel(key,value);
        
        keyValue = (HashMap)lookupValues.get("OPERATIVEACCTPRODUCT.STATCHRG");
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
        cbmAmtStatChrg = new ComboBoxModel(key,value);
        
        keyValue = (HashMap)lookupValues.get("MONTH");
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
        cbmIntCalcEndMon = new ComboBoxModel(key,value);
        cbmStMonIntCalc = new ComboBoxModel(key,value);
        cbmStDayProdCalcSBCrInt = new ComboBoxModel(key,value);
        cbmEndDayProdCalcSBCrInt = new ComboBoxModel(key,value);
        
        cbmStartInterCalc = new ComboBoxModel(key,value);
        cbmEndInterCalc = new ComboBoxModel(key,value);
        cbmStartProdCalc = new ComboBoxModel(key,value);
        cbmEndProdCalc = new ComboBoxModel(key,value);
        
        keyValue = (HashMap)lookupValues.get("OPERATIVEACCTPRODUCT.PRODFREQ");
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
        //        cbmProdFreq = new ComboBoxModel(key,value);
        
        keyValue = (HashMap)lookupValues.get("FOREX.CURRENCY");
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
        cbmProdCurrency = new ComboBoxModel(key,value);
        
        keyValue = (HashMap)lookupValues.get("OP_STARTS_FROM");
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
        cbmFreeWDStFrom = new ComboBoxModel(key,value);
        cbmFreeChkLeaveStFrom = new ComboBoxModel(key,value);
        
        keyValue = (HashMap)lookupValues.get("FOLIO_PERIOD");
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
        cbmFolioChargedbefore = new ComboBoxModel(key,value);
        
        // Loading Term Deposit Data
        param = new HashMap();
        param.put(CommonConstants.MAP_NAME,"getFlexiList");
        param.put(CommonConstants.PARAMFORQUERY, "");
        keyValue = (HashMap) ClientUtil.populateLookupData(param).get(CommonConstants.DATA);
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
        cbmFlexiTD = new ComboBoxModel(key,value);
        
        // Added by nithya on 17-03-2016 for 0004021
        keyValue = (HashMap)lookupValues.get("OPERATIVE_ACCT.CHARGETYPE");
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
        cbmDebitChargeType = new ComboBoxModel(key,value);
        // End
        
        // Loading Term Deposit Data
//        param = new HashMap();
//        param.put(CommonConstants.MAP_NAME,"getIntCategoryList");
//        param.put(CommonConstants.PARAMFORQUERY, "");
//        keyValue = (HashMap) ClientUtil.populateLookupData(param).get(CommonConstants.DATA);
//        key = (ArrayList)keyValue.get(CommonConstants.KEY);
//        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
//        cbmIntCategory = new ComboBoxModel(key,value);
    }
    
    public int getActionType(){
        return _actionType;
    }
    
    public void setActionType(int actionType) {
        _actionType = actionType;
        setStatus();
        setChanged();
    }
    
    private String getCommand(){
        String command = null;
        switch (_actionType) {
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
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    public int getResult(){
        return _result;
    }
    
    public void setResult(int result) {
        _result = result;
        setChanged();
    }
    
    /** To set the status based on ActionType, New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }
    
//    public TableModel getTmlIntRate(){
//        return this.tmlIntRate;
//    }
//    public void setTmlIntRate(TableModel tml){
//        this.tmlIntRate = tml;
//    }
    
    public ComboBoxModel getCbmProdCurrency(){
        return this.cbmProdCurrency;
    }
    public ComboBoxModel getCbmBehaves(){
        return this.cbmBehaves;
    }
    public ComboBoxModel getCbmPeriodNew(){
        return this.cbmPeriodNew;
    }
    public ComboBoxModel getCbmPeriodDormant(){
        return this.cbmPeriodDormant;
    }
    public ComboBoxModel getCbmPeriodInop(){
        return this.cbmPeriodInop;
    }
    public ComboBoxModel getCbmPeriodCloser(){
        return this.cbmPeriodCloser;
    }
    public ComboBoxModel getCbmStatFreq(){
        return this.cbmStatFreq;
    }
    public ComboBoxModel getCbmWDPeriod(){
        return this.cbmWDPeriod;
    }
    public ComboBoxModel getCbmChqPeriod(){
        return this.cbmChqPeriod;
    }
    public ComboBoxModel getCbmDrCalcFreq(){
        return this.cbmDrCalcFreq;
    }
    public ComboBoxModel getCbmDrApplFreq(){
        return this.cbmDrApplFreq;
    }
    public ComboBoxModel getCbmDrCompFreq(){
        return this.cbmDrCompFreq;
    }
    public ComboBoxModel getCbmDrProdRound(){
        return this.cbmDrProdRound;
    }
    public ComboBoxModel getCbmDrIntRound(){
        return this.cbmDrIntRound;
    }
    public ComboBoxModel getCbmCrCalcFreq(){
        return this.cbmCrCalcFreq;
    }
    public ComboBoxModel getCbmCrApplFreq(){
        return this.cbmCrApplFreq;
    }
    public ComboBoxModel getCbmCrCompFreq(){
        return this.cbmCrCompFreq;
    }
    public ComboBoxModel getCbmCrProdRound(){
        return this.cbmCrProdRound;
    }
    public ComboBoxModel getCbmCrIntRound(){
        return this.cbmCrIntRound;
    }
    public ComboBoxModel getCbmCalcCrit(){
        return this.cbmCalcCrit;
    }
    public ComboBoxModel getCbmCrProdFreq(){
        return this.cbmCrProdFreq;
    }
    public ComboBoxModel getCbmNonMain(){
        return this.cbmNonMain;
    }
    public ComboBoxModel getCbmAmtStatChrg(){
        return this.cbmAmtStatChrg;
    }
    public ComboBoxModel getCbmFolioChrgApplFreq(){
        return this.cbmFolioChrgApplFreq;
    }
    public ComboBoxModel getCbmCollFolioChrg(){
        return this.cbmCollFolioChrg;
    }
    public ComboBoxModel getCbmIncFolioROffFreq(){
        return this.cbmIncFolioROffFreq;
    }
    public ComboBoxModel getCbmIntCalcEndMon(){
        return this.cbmIntCalcEndMon;
    }
    public ComboBoxModel getCbmStMonIntCalc(){
        return this.cbmStMonIntCalc;
    }
    public ComboBoxModel getCbmProdFreq(){
        return this.cbmProdFreq;
    }
    public ComboBoxModel getCbmInOpAcChrgPd(){
        return this.cbmInOpAcChrgPd;
    }
    public ComboBoxModel getCbmFlexiTD(){
        return this.cbmFlexiTD;
    }
//    public ComboBoxModel getCbmIntCategory(){
//        return this.cbmIntCategory;
//    }
    
    public ComboBoxModel getCbmFreeWDStFrom(){
        return this.cbmFreeWDStFrom;
    }
    public ComboBoxModel getCbmFreeChkLeaveStFrom(){
        return this.cbmFreeChkLeaveStFrom;
    }
    public ComboBoxModel getCbmStDayProdCalcSBCrInt(){
        return this.cbmStDayProdCalcSBCrInt;
    }
    public ComboBoxModel getCbmEndDayProdCalcSBCrInt(){
        return this.cbmEndDayProdCalcSBCrInt;
    }
    public ComboBoxModel getCbmStartInterCalc(){
        return this.cbmStartInterCalc;
    }
    public ComboBoxModel getCbmEndInterCalc(){
        return this.cbmEndInterCalc;
    }
    public ComboBoxModel getCbmStartProdCalc(){
        return this.cbmStartProdCalc;
    }
    public ComboBoxModel getCbmEndProdCalc(){
        return this.cbmEndProdCalc;
    }

    public ComboBoxModel getCbmFolioChargedbefore() {
        return cbmFolioChargedbefore;
    }

    public void setCbmFolioChargedbefore(ComboBoxModel cbmFolioChargedbefore) {
        this.cbmFolioChargedbefore = cbmFolioChargedbefore;
    }

    public String getTxtFolioChargeType() {
        return txtFolioChargeType;
    }

    public void setTxtFolioChargeType(String txtFolioChargeType) {
        this.txtFolioChargeType = txtFolioChargeType;
    }
    
    
    public void populateData(HashMap whereMap) {
        HashMap mapData = null;
        try {
            mapData = proxy.executeQuery(whereMap, map);
            populateOB(mapData);
        } catch( Exception e ) {
            setResult(4);
            System.err.println( "Exception " + e.toString() + "Caught" );
            e.printStackTrace();
        }
    }
    
    public int insertData() {
        int state = 0;
        try {
            HashMap proxyResultMap = proxy.execute(populateBean(CommonConstants.TOSTATUS_INSERT), map);
            state = IDAYS;
            setResult(getActionType());
        } catch (Exception e) {
            setResult(4);
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }
        setResultStatus();
        return state;
    }
    
    public int updateData() {
        int state = 0;
        try {
            HashMap proxyResultMap = proxy.execute(populateBean(CommonConstants.TOSTATUS_UPDATE), map);
            state = IDAYS;
            setResult(getActionType());
        } catch (Exception e) {
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }
        setResultStatus();
        return state;
    }
    
    public int deleteData() {
        int state = 0;
        try {
            HashMap proxyResultMap = proxy.execute(populateBean(CommonConstants.TOSTATUS_DELETE), map);
            state = IDAYS;
            setResult(getActionType());
        } catch (Exception e) {
            setResult(4);
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }
        setResultStatus();
        return state;
    }
    
    private void setOperativeAcctProductTO(OperativeAcctProductTO objOperativeAcctProductTO) {
        setTxtAcctHd(CommonUtil.convertObjToStr(objOperativeAcctProductTO.getAcHdId()));
        setTxtProductID(CommonUtil.convertObjToStr(objOperativeAcctProductTO.getProdId()));
        setTxtDesc(CommonUtil.convertObjToStr(objOperativeAcctProductTO.getProdDesc()));
        setCboBehaves(CommonUtil.convertObjToStr(objOperativeAcctProductTO.getBehavior()));
        setCboProdCurrency(CommonUtil.convertObjToStr(objOperativeAcctProductTO.getBaseCurrency()));
        //setCboProdCurrency(LocaleConstants.DEFAULT_CURRENCY);
        if (objOperativeAcctProductTO.getSRemarks() != null) {
            if (objOperativeAcctProductTO.getSRemarks().equals("REGULAR")) setRdoAcc_Reg(true);
            else if (objOperativeAcctProductTO.getSRemarks().equals("NRO"))  setRdoAcc_Nro(true);
            else if (objOperativeAcctProductTO.getSRemarks().equals("NRE"))  setRdoAcc_Nre(true);
        }
        // Added by nithya on 17-03-2016 for 0004021
        if(objOperativeAcctProductTO.getIsdebitWithdrawalCharge() != null){
          setChkDebitWithdrawalCharge(objOperativeAcctProductTO.getIsdebitWithdrawalCharge());
        }
        if(objOperativeAcctProductTO.getChargePeriod() != null){
         setTxtDebitWithdrawalChargePeriod(CommonUtil.convertObjToStr(objOperativeAcctProductTO.getChargePeriod()));
        } 
    }
    
    private void setOperativeAcctParamTO(OperativeAcctParamTO objOperativeAcctParamTO) {
        int multiply = 0;
        
        if (objOperativeAcctParamTO.getChkAllowed() != null) {
            if (objOperativeAcctParamTO.getChkAllowed().equals(YES)) setRdoChkAllowedYes(true);
            else setRdoChkAllowedNo(true);
        }
        
        setTxtMinBalwchk(CommonUtil.convertObjToStr(objOperativeAcctParamTO.getMinBalWtChk()));
        setTxtMinBalChkbk(CommonUtil.convertObjToStr(objOperativeAcctParamTO.getMinBalWChk()));
        
        if (objOperativeAcctParamTO.getIntReq() != null) {
            if (objOperativeAcctParamTO.getIntReq().equals(YES)) setRdoIntroReqYes(true);
            else setRdoIntroReqNo(true);
        }
        
        if (objOperativeAcctParamTO.getNomineeReq() != null) {
            if (objOperativeAcctParamTO.getNomineeReq().equals(YES)) setRdoNomineeReqYes(true);
            else setRdoNomineeReqNo(true);
        }
        
        setTxtNoNominees(CommonUtil.convertObjToStr(objOperativeAcctParamTO.getNoOfNominee()));
        
        if (objOperativeAcctParamTO.getAcOpReq() != null) {
            if (objOperativeAcctParamTO.getAcOpReq().equals(YES)) setRdoAcctOpenApprYes(true);
            else setRdoAcctOpenApprNo(true);
        }
        
        multiply = CommonUtil.convertObjToInt(objOperativeAcctParamTO.getMinPdNew());
//        if (multiply >= IYEARS) {
//            setCboMinTreatasNew(YEARS);
//            multiply = multiply/IYEARS;
//        } else if (multiply >= IMONTHS) {
//            setCboMinTreatasNew(MONTHS);
//            multiply = multiply/IMONTHS;
//        } else if (multiply >= IDAYS) {
//            setCboMinTreatasNew(DAYS);
//            multiply = multiply;
//        } else {
//            setCboMinTreatasNew("");
//            multiply = 0;
//        }
        
        if (multiply >= IYEARS && (multiply%IYEARS == 0)) {
            setCboMinTreatasNew(YEARS);
            multiply = multiply/IYEARS;
        } else if (multiply >= IMONTHS && (multiply%IMONTHS == 0)) {
            setCboMinTreatasNew(MONTHS);
            multiply = multiply/IMONTHS;
        } else if (multiply >= IDAYS) {
            setCboMinTreatasNew(DAYS);
            multiply = multiply;
        } else {
            setCboMinTreatasNew("");
            multiply = 0;
        }
        setTxtMinTreatasNew(String.valueOf(multiply));
        
        multiply = CommonUtil.convertObjToInt(objOperativeAcctParamTO.getMinPdDormant());
        if (multiply >= IYEARS && (multiply%IYEARS == 0)) {
            setCboMinTreatasDormant(YEARS);
            multiply = multiply/IYEARS;
        } else if (multiply >= IMONTHS && (multiply%IMONTHS == 0)) {
            setCboMinTreatasDormant(MONTHS);
            multiply = multiply/IMONTHS;
        } else if (multiply >= IDAYS) {
            setCboMinTreatasDormant(DAYS);
            multiply = multiply;
        } else {
            setCboMinTreatasDormant("");
            multiply = 0;
        }
        setTxtMinTreatasDormant(String.valueOf(multiply));
        
        multiply = CommonUtil.convertObjToInt(objOperativeAcctParamTO.getMinPdInop());
        if (multiply >= IYEARS && (multiply%IYEARS == 0)) {
            setCboMinTreatInOp(YEARS);
            multiply = multiply/IYEARS;
        } else if (multiply >= IMONTHS && (multiply%IMONTHS == 0)) {
            setCboMinTreatInOp(MONTHS);
            multiply = multiply/IMONTHS;
        } else if (multiply >= IDAYS) {
            setCboMinTreatInOp(DAYS);
            multiply = multiply;
        } else {
            setCboMinTreatInOp("");
            multiply = 0;
        }
        setTxtMinTreatasInOp(String.valueOf(multiply));
        
        multiply = CommonUtil.convertObjToInt(objOperativeAcctParamTO.getMinPdCls());
        if (multiply >= IYEARS && (multiply%IYEARS == 0)) {
            setCboMinTreatNewClosure(YEARS);
            multiply = multiply/IYEARS;
        } else if (multiply >= IMONTHS && (multiply%IMONTHS == 0)) {
            setCboMinTreatNewClosure(MONTHS);
            multiply = multiply/IMONTHS;
        } else if (multiply >= IDAYS) {
            setCboMinTreatNewClosure(DAYS);
            multiply = multiply;
        } else {
            setCboMinTreatNewClosure("");
            multiply = 0;
        }
        setTxtMainTreatNewAcctClosure(String.valueOf(multiply));
        
        setCboStatFreq(CommonUtil.convertObjToStr(objOperativeAcctParamTO.getStatFrequency()));
        setTxtNoFreeWD(CommonUtil.convertObjToStr(objOperativeAcctParamTO.getFreeWithdrawals()));
        
        multiply = CommonUtil.convertObjToInt(objOperativeAcctParamTO.getFreeWithdrawalsPd());
        if (multiply >= IYEARS && (multiply%IYEARS == 0)) {
            setCboFreeWDPd(YEARS);
            multiply = multiply/IYEARS;
        } else if (multiply >= IMONTHS && (multiply%IMONTHS == 0)) {
            setCboFreeWDPd(MONTHS);
            multiply = multiply/IMONTHS;
        } else if (multiply >= IDAYS) {
            setCboFreeWDPd(DAYS);
            multiply = multiply;
        } else {
            setCboFreeWDPd("");
            multiply = 0;
        }
        setTxtFreeWDPd(String.valueOf(multiply));
        
        setTdtFreeWDStFrom(DateUtil.getStringDate(objOperativeAcctParamTO.getFreeWithdrawalFrom()));
        
        setTxtNoFreeChkLeaves(CommonUtil.convertObjToStr(objOperativeAcctParamTO.getNoFreeChkLeaves()));
        
        multiply = CommonUtil.convertObjToInt(objOperativeAcctParamTO.getFreeChkLeavesPd());
        if (multiply >= IYEARS && (multiply%IYEARS == 0)) {
            setCboFreeChkPd(YEARS);
            multiply = multiply/IYEARS;
        } else if (multiply >= IMONTHS && (multiply%IMONTHS == 0)) {
            setCboFreeChkPd(MONTHS);
            multiply = multiply/IMONTHS;
        } else if (multiply >= IDAYS) {
            setCboFreeChkPd(DAYS);
            multiply = multiply;
        } else {
            setCboFreeChkPd("");
            multiply = 0;
        }
        setTxtFreeChkPD(String.valueOf(multiply));
        
        setTdtFreeChkLeaveStFrom(DateUtil.getStringDate(objOperativeAcctParamTO.getFreeChkLeavesFrom()));
        
        if (objOperativeAcctParamTO.getTaxIntApplicable() != null) {
            if (objOperativeAcctParamTO.getTaxIntApplicable().equals(YES)) setRdoTaxIntApplNROYes(true);
            else setRdoTaxIntApplNRONo(true);
        }
        
        setTxtRateTaxNRO(CommonUtil.convertObjToStr(objOperativeAcctParamTO.getRateOfInt()));
        
        if (objOperativeAcctParamTO.getLmtDefinitionAllowed() != null) {
            if (objOperativeAcctParamTO.getLmtDefinitionAllowed().equals(YES)) setRdoLimitDefAllowYes(true);
            else setRdoLimitDefAllowNo(true);
        }
        
        if (objOperativeAcctParamTO.getTempOdAllowed() != null) {
            if (objOperativeAcctParamTO.getTempOdAllowed().equals(YES)) setRdoTempODAllowYes(true);
            else setRdoTempODAllowNo(true);
        }
        
        if (objOperativeAcctParamTO.getStaffAcctOpened() != null) {
            if (objOperativeAcctParamTO.getStaffAcctOpened().equals(YES)) setRdoStaffAcctYes(true);
            else setRdoStaffAcctNo(true);
        }
        
        //        if (objOperativeAcctParamTO.getExtraIntApplicable() != null) {
        //            if (objOperativeAcctParamTO.getExtraIntApplicable().equals(YES)) setRdoExtraIntApplYes(true);
        //            else setRdoExtraIntApplNo(true);
        //        }
        
        if (objOperativeAcctParamTO.getCollectIntCredit() != null) {
            if (objOperativeAcctParamTO.getCollectIntCredit().equals(YES)) setRdoCollectIntYes(true);
            else setRdoCollectIntNo(true);
        }
        
        if (objOperativeAcctParamTO.getDebitIntClearing() != null) {
            if (objOperativeAcctParamTO.getDebitIntClearing().equals(YES)) setRdoIntClearing_Yes(true);
            else setRdoIntClearing_No(true);
        }
        
        if (objOperativeAcctParamTO.getCreditIntUnclear() != null) {
            if (objOperativeAcctParamTO.getCreditIntUnclear().equals(YES)) setRdoIntUnClearBalYes(true);
            else setRdoIntUnClearBalNo(true);
        }
        
        if (objOperativeAcctParamTO.getIssueToken() != null) {
            if (objOperativeAcctParamTO.getIssueToken().equals(YES)) setRdoIssueTokenYes(true);
            else setRdoIssueTokenNo(true);
        }
        
        if (objOperativeAcctParamTO.getAllowWithdrawalSlip() != null) {
            if (objOperativeAcctParamTO.getAllowWithdrawalSlip().equals(YES)) setRdoAllowWDYes(true);
            else setRdoAllowWDNo(true);
        }
        
        setTxtMaxAmtWDSlip(CommonUtil.convertObjToStr(objOperativeAcctParamTO.getMaxAllowedWdSlip()));
        
        setCboFreeWDStFrom(CommonUtil.convertObjToStr(objOperativeAcctParamTO.getFreeWithdrawalType()));
        setCboFreeChkLeaveStFrom(CommonUtil.convertObjToStr(objOperativeAcctParamTO.getFreeChkLeavesType()));
        
        setTxtNumPatternFollowedPrefix(objOperativeAcctParamTO.getNumPatternFollowedPrefix());
        setTxtNumPatternFollowedSuffix(CommonUtil.convertObjToStr(objOperativeAcctParamTO.getNumPatternFollowedSuffix()));
        setTxtLastAccNum(CommonUtil.convertObjToStr(objOperativeAcctParamTO.getLastAccNum()));
    }
    
    private void setOperativeAcctIntRecvParamTO(OperativeAcctIntRecvParamTO objOperativeAcctIntRecvParamTO) {
        // Fillup OperativeAcctProductTO Object ----------------------------------------------------
        if (objOperativeAcctIntRecvParamTO.getDebitIntCharged() != null) {
            if (objOperativeAcctIntRecvParamTO.getDebitIntCharged().equals(YES)) setRdoDebitIntChrgYes2(true);
            else setRdoDebitIntChrgNo2(true);
        }
        
        setTxtMinDebitIntRate(CommonUtil.convertObjToStr(objOperativeAcctIntRecvParamTO.getMinDebitIntRate()));
        setTxtMaxDebitIntRate(CommonUtil.convertObjToStr(objOperativeAcctIntRecvParamTO.getMaxDebitIntRate()));
        setTxtApplDebitIntRate(CommonUtil.convertObjToStr(objOperativeAcctIntRecvParamTO.getApplDebitIntRate()));
        setTxtMinDebitIntAmt(CommonUtil.convertObjToStr(objOperativeAcctIntRecvParamTO.getMinDebitIntAmt()));
        setTxtMaxDebitIntAmt(CommonUtil.convertObjToStr(objOperativeAcctIntRecvParamTO.getMaxDebitIntAmt()));
        
        setCboDebitIntCalcFreq(CommonUtil.convertObjToStr(objOperativeAcctIntRecvParamTO.getDebitIntCalcFreq()));
        setCboDebitIntApplFreq(CommonUtil.convertObjToStr(objOperativeAcctIntRecvParamTO.getDebitIntApplFreq()));
        
        if (objOperativeAcctIntRecvParamTO.getDebitCompound() != null) {
            if (objOperativeAcctIntRecvParamTO.getDebitCompound().equals(YES)) setRdoDebitCompReqYes(true);
            else setRdoDebitCompReqNo(true);
        }
        
        setCboDebitCompIntCalcFreq(CommonUtil.convertObjToStr(objOperativeAcctIntRecvParamTO.getDebitCompintCalcFreq()));
        
        setCboProdFreq(CommonUtil.convertObjToStr(objOperativeAcctIntRecvParamTO.getProductFreq()));
        
        setCboDebitProductRoundOff(CommonUtil.convertObjToStr(objOperativeAcctIntRecvParamTO.getDebitProductRoundoff()));
        setCboDebitIntRoundOff(CommonUtil.convertObjToStr(objOperativeAcctIntRecvParamTO.getDebitIntRoundoff()));
        setTdtLastIntCalcDate(DateUtil.getStringDate(objOperativeAcctIntRecvParamTO.getLastIntCalcdtDebit()));
        setTdtLastIntApplDate(DateUtil.getStringDate(objOperativeAcctIntRecvParamTO.getLastIntAppldtDebit()));
        
        setTxtPenalIntDebitBalAcct(CommonUtil.convertObjToStr(objOperativeAcctIntRecvParamTO.getPenalIntDebitBalacct()));
        setTxtPenalIntChrgStart(CommonUtil.convertObjToStr(objOperativeAcctIntRecvParamTO.getPenalIntChargeStday()));
        
        /*txtStartInterCalc.setName("txtStartInterCalc");
    cboStartInterCalc.setName("cboStartInterCalc");
    txtEndInterCalc.setName("txtEndInterCalc");
    cboEndInterCalc.setName("cboEndInterCalc");
         
    txtStartProdCalc.setName("txtStartProdCalc");
    cboStartProdCalc.setName("cboStartProdCalc");
    txtEndProdCalc.setName("txtEndProdCalc");
    cboEndProdCalc.setName("cboEndProdCalc");*/
        
        setTxtStartInterCalc(CommonUtil.convertObjToStr(objOperativeAcctIntRecvParamTO.getStartdayIntCalc()));
        setCboStartInterCalc(CommonUtil.convertObjToStr(objOperativeAcctIntRecvParamTO.getStartmonIntCalc()));
        setTxtEndInterCalc(CommonUtil.convertObjToStr(objOperativeAcctIntRecvParamTO.getEnddayIntCalc()));
        setCboEndInterCalc(CommonUtil.convertObjToStr(objOperativeAcctIntRecvParamTO.getEndmonIntCalc()));
        
        setTxtStartProdCalc(CommonUtil.convertObjToStr(objOperativeAcctIntRecvParamTO.getStartdayProdCalc()));
        setCboStartProdCalc(CommonUtil.convertObjToStr(objOperativeAcctIntRecvParamTO.getStartmonProdCalc()));
        setTxtEndProdCalc(CommonUtil.convertObjToStr(objOperativeAcctIntRecvParamTO.getEnddayProdCalc()));
        setCboEndProdCalc(CommonUtil.convertObjToStr(objOperativeAcctIntRecvParamTO.getEndmonProdCalc()));
    }
    
    private void setOperativeAcctIntPayParamTO(OperativeAcctIntPayParamTO  objOperativeAcctIntPayParamTO ) {
        // Fillup OperativeAcctIntPayParamTO Object ----------------------------------------------------
        setTxtMinCrIntRate(CommonUtil.convertObjToStr(objOperativeAcctIntPayParamTO.getMinCrIntRate()));
        setTxtMaxCrIntRate(CommonUtil.convertObjToStr(objOperativeAcctIntPayParamTO.getMaxCrIntRate()));
        setTxtApplCrIntRate(CommonUtil.convertObjToStr(objOperativeAcctIntPayParamTO.getApplCrIntRate()));
        setTxtMinCrIntAmt(CommonUtil.convertObjToStr(objOperativeAcctIntPayParamTO.getMinCrIntAmt()));
        setTxtMaxCrIntAmt(CommonUtil.convertObjToStr(objOperativeAcctIntPayParamTO.getMaxCrIntAmt()));
        
        setCboCrIntCalcFreq(CommonUtil.convertObjToStr(objOperativeAcctIntPayParamTO.getCrIntCalcFreq()));
        setCboCrIntApplFreq(CommonUtil.convertObjToStr(objOperativeAcctIntPayParamTO.getCrIntApplFreq()));
        
        setCboStMonIntCalc(CommonUtil.convertObjToStr(objOperativeAcctIntPayParamTO.getStartmonIntCalc()));
        setTxtStMonIntCalc(CommonUtil.convertObjToStr(objOperativeAcctIntPayParamTO.getStartdayIntCalc()));
        
        setCboIntCalcEndMon(CommonUtil.convertObjToStr(objOperativeAcctIntPayParamTO.getEndmonIntCalc()));
        setTxtIntCalcEndMon(CommonUtil.convertObjToStr(objOperativeAcctIntPayParamTO.getEnddayIntCalc()));
        
        setTdtLastIntCalcDateCR(DateUtil.getStringDate(objOperativeAcctIntPayParamTO.getLastIntCalcdtCr()));
        setTdtLastIntApplDateCr(DateUtil.getStringDate(objOperativeAcctIntPayParamTO.getLastIntAppldtCr()));
        setMinBalForIntCalc(CommonUtil.convertObjToStr(objOperativeAcctIntPayParamTO.getMinBalforIntCalc()));
        
        if (objOperativeAcctIntPayParamTO.getCrCompound() != null) {
            if (objOperativeAcctIntPayParamTO.getCrCompound().equals(YES)) setRdoCreditCompYes(true);
            else setRdoCreditCompNo(true);
        }
        
        setCboCreditCompIntCalcFreq(CommonUtil.convertObjToStr(objOperativeAcctIntPayParamTO.getCrCompoundCalcFreq()));
        
        setCboCreditProductRoundOff(CommonUtil.convertObjToStr(objOperativeAcctIntPayParamTO.getCrProductRoundoff()));
        setCboCreditIntRoundOff(CommonUtil.convertObjToStr(objOperativeAcctIntPayParamTO.getCrIntRoundoff()));
        
        setTxtStDayProdCalcSBCrInt(CommonUtil.convertObjToStr(objOperativeAcctIntPayParamTO.getStartdayProdCalc()));
        setCboStDayProdCalcSBCrInt(CommonUtil.convertObjToStr(objOperativeAcctIntPayParamTO.getStartmonProdCalc()));
        
        setTxtEndDayProdCalcSBCrInt(CommonUtil.convertObjToStr(objOperativeAcctIntPayParamTO.getEnddayProdCalc()));
        setCboEndDayProdCalcSBCrInt(CommonUtil.convertObjToStr(objOperativeAcctIntPayParamTO.getEndmonProdCalc()));
        
        setCboCalcCriteria(CommonUtil.convertObjToStr(objOperativeAcctIntPayParamTO.getCalcCriteria()));
        //        setCboProdFreqCr(String.valueOf(CommonUtil.convertObjToInt(objOperativeAcctIntPayParamTO.getProductFreqIntPay())));
        setCboProdFreqCr(CommonUtil.convertObjToStr(objOperativeAcctIntPayParamTO.getProductFreqIntPay()));
        
        if (objOperativeAcctIntPayParamTO.getCreditIntGiven() != null) {
            if (objOperativeAcctIntPayParamTO.getCreditIntGiven().equals(YES)) setRdoCrIntGivenYes(true);
            else setRdoCrIntGivenNo(true);
        }
        
        //        setTxtAddIntStaff(CommonUtil.convertObjToStr(objOperativeAcctIntPayParamTO.getAdditionalIntStaff()));
    }
    
    private void setOperativeAcctChargesParamTO(OperativeAcctChargesParamTO objOperativeAcctChargesParamTO ) {
        // Fillup OperativeAcctChargesParamTO Object ----------------------------------------------------
        setTxtlInOpAcChrg(CommonUtil.convertObjToStr(objOperativeAcctChargesParamTO.getInoperativeAcCharges()));
        setCboInOpAcChrgPd(CommonUtil.convertObjToStr(objOperativeAcctChargesParamTO.getInoperativeAcChargePd()));
        setTxtChrgPreClosure(CommonUtil.convertObjToStr(objOperativeAcctChargesParamTO.getChgPrematureClosure()));
        setTxtAcClosingChrg(CommonUtil.convertObjToStr(objOperativeAcctChargesParamTO.getAcctClosingChg()));
        setTxtChrgMiscServChrg(CommonUtil.convertObjToStr(objOperativeAcctChargesParamTO.getMiscServiceChg()));
        
        if (objOperativeAcctChargesParamTO.getNonmainMinBalChg() != null) {
            if (objOperativeAcctChargesParamTO.getNonmainMinBalChg().equals(YES)) setRdoNonMainMinBalChrg_Yes(true);
            else setRdoNonMainMinBalChrg_No(true);
        }
        
        setTxtMinBalAmt(CommonUtil.convertObjToStr(objOperativeAcctChargesParamTO.getAmtNonmainMinbal()));
        setCboMinBalAmt(CommonUtil.convertObjToStr(objOperativeAcctChargesParamTO.getAmtNonmainMinbalPd()));
        
        if (objOperativeAcctChargesParamTO.getStatCharge() != null) {
            if (objOperativeAcctChargesParamTO.getStatCharge().equals(YES)) setRdoStatCharges_Yes(true);
            else setRdoStatCharges_No(true);
        }
        
        setTxtStatChargesChr(CommonUtil.convertObjToStr(objOperativeAcctChargesParamTO.getAmtChargeStat()));
        setCboStatChargesChr(CommonUtil.convertObjToStr(objOperativeAcctChargesParamTO.getAmtChargeStatType()));
        
        if (objOperativeAcctChargesParamTO.getChkIssueChg() != null) {
            if (objOperativeAcctChargesParamTO.getChkIssueChg().equals(YES)) setRdoChkIssuedChrgCh_Yes(true);
            else setRdoChkIssuedChrgCh_No(true);
        }
        
        setTxtChkBkIssueChrgPL(CommonUtil.convertObjToStr(objOperativeAcctChargesParamTO.getChkIssueChgperleaf()));
        
        if (objOperativeAcctChargesParamTO.getStopPaymentChg() != null) {
            if (objOperativeAcctChargesParamTO.getStopPaymentChg().equals(YES)) setRdoStopPaymentChrg_Yes(true);
            else setRdoStopPaymentChrg_No(true);
        }
        
        setTxtStopPayChrg(CommonUtil.convertObjToStr(objOperativeAcctChargesParamTO.getStopPaymentAmtchg()));
        setTxtChkRetChrOutward(CommonUtil.convertObjToStr(objOperativeAcctChargesParamTO.getChkReturnChgOutward()));
        setTxtChkRetChrgIn(CommonUtil.convertObjToStr(objOperativeAcctChargesParamTO.getChkReturnChgInward()));
        setTxtAcctOpenCharges(CommonUtil.convertObjToStr(objOperativeAcctChargesParamTO.getAcctOpeningChg()));
        
        if (objOperativeAcctChargesParamTO.getFolioChgApplicable() != null) {
            if (objOperativeAcctChargesParamTO.getFolioChgApplicable().equals(YES)) setRdoFolioChargeAppl_Yes(true);
            else setRdoFolioChargeAppl_No(true);
        }
        
        setTxtNoEntryPerFolio(CommonUtil.convertObjToStr(objOperativeAcctChargesParamTO.getNoEntriesperFolio()));
        setTxtRatePerFolio(CommonUtil.convertObjToStr(objOperativeAcctChargesParamTO.getRatePerFolio()));
        
        if (objOperativeAcctChargesParamTO.getToChargeOn() != null) {
            if (objOperativeAcctChargesParamTO.getToChargeOn().equals("MANUAL")) setRdoToChargeOnApplFreq_Manual(true);
            else if (objOperativeAcctChargesParamTO.getToChargeOn().equals("SYSTEM")) setRdoToChargeOnApplFreq_System(true);
            else if (objOperativeAcctChargesParamTO.getToChargeOn().equals("BOTH")) setRdoToChargeOnApplFreq_Both(true);
        }
        
        setCboFolioChrgApplFreq(CommonUtil.convertObjToStr(objOperativeAcctChargesParamTO.getFolioChgApplFreq()));
        
        setCboToCollectFolioChrg(CommonUtil.convertObjToStr(objOperativeAcctChargesParamTO.getToCollectFolioChg()));
        //folio date
        setTdtLastFolioChargeDt(CommonUtil.convertObjToStr(objOperativeAcctChargesParamTO.getLastFolioChargedt()));
        setTdtNextFolioDueDate(CommonUtil.convertObjToStr(objOperativeAcctChargesParamTO.getNextFolioChargedt()));
        
        if (objOperativeAcctChargesParamTO.getToChargeOnType() != null) {
            if (objOperativeAcctChargesParamTO.getToChargeOnType().equals("CREDIT")) setRdoFolioToChargeOn_Credit(true);
            else if (objOperativeAcctChargesParamTO.getToChargeOnType().equals("DEBIT")) setRdoFolioToChargeOn_Debit(true);
            else if (objOperativeAcctChargesParamTO.getToChargeOnType().equals("BOTH")) setRdoFolioToChargeOn_Both(true);
        }
        
        setCboIncompFolioROffFreq(CommonUtil.convertObjToStr(objOperativeAcctChargesParamTO.getIncompleteFolioRoundFreq()));
        
        setTxtExcessFreeWDChrgPT(CommonUtil.convertObjToStr(objOperativeAcctChargesParamTO.getChgExcessfreewdPertrans()));
        
        // Added by nithya on 17-03-2016 for 0004021
        setCboDebitChargeType(objOperativeAcctChargesParamTO.getDebitWithdrawalChargeType());
        setTxtDebitChargeTypeRate(CommonUtil.convertObjToStr(objOperativeAcctChargesParamTO.getDebitWithdrawalChargeRate()));
        // End
        setCboFolioChargeRestritionFreq(objOperativeAcctChargesParamTO.getCboFolioChargeRestrictionFrq());
        setTxtFolioRestrictionPriod(CommonUtil.convertObjToStr(objOperativeAcctChargesParamTO.getTxtFolioChargeRestrictionPeriod()));
        setTxtFolioChargeType(objOperativeAcctChargesParamTO.getTxtFolioChargeType());
        //////////////////////
        //objOperativeAcctChargesParamTO.setOutstChkCharges ( );
    }
    
    private void setOperativeAcctSpclitemParamTO(OperativeAcctSpclitemParamTO objOperativeAcctSpclitemParamTO ) {
        // Fillup OperativeAcctSpclitemParamTO Object ----------------------------------------------------
        
        if (objOperativeAcctSpclitemParamTO.getLinkedFlexiAcct() != null) {
            if (objOperativeAcctSpclitemParamTO.getLinkedFlexiAcct().equals(YES)) setRdoLinkFlexiAcct_Yes(true);
            else setRdoLinkFlexiAcct_No(true);
        }
        
        setTxtMinBal1FlexiDep(CommonUtil.convertObjToStr(objOperativeAcctSpclitemParamTO.getMinBal1Flexideposit()));
        setTxtMinBal2FlexiDep(CommonUtil.convertObjToStr(objOperativeAcctSpclitemParamTO.getMinBal2Flexideposit()));
        
        if (objOperativeAcctSpclitemParamTO.getFlexiHappen() != null) {
            if (objOperativeAcctSpclitemParamTO.getFlexiHappen().equals("SB")) setRdoFlexiHappen_SB(true);
            else if (objOperativeAcctSpclitemParamTO.getFlexiHappen().equals("TD")) setRdoFlexiHappen_TD(true);
        }
        
        if (objOperativeAcctSpclitemParamTO.getAtmCardIssued() != null) {
            if (objOperativeAcctSpclitemParamTO.getAtmCardIssued().equals(YES)) setRdoATMIssuedYes(true);
            else setRdoATMIssuedNo(true);
        }
        
        setTxtMinATMBal(CommonUtil.convertObjToStr(objOperativeAcctSpclitemParamTO.getMinBalAtm()));
        
        if (objOperativeAcctSpclitemParamTO.getCrCardIssued() != null) {
            if (objOperativeAcctSpclitemParamTO.getCrCardIssued().equals(YES)) setRdoCreditCdIssued_Yes(true);
            else setRdoCreditCdIssued_No(true);
        }
        
        setTxtMinBalCreditCd(CommonUtil.convertObjToStr(objOperativeAcctSpclitemParamTO.getMinBalCrCard()));
        
        if (objOperativeAcctSpclitemParamTO.getDrCardIssued() != null) {
            if (objOperativeAcctSpclitemParamTO.getDrCardIssued().equals(YES)) setRdoDebitCdIssued_Yes(true);
            else setRdoDebitCdIssued_No(true);
        }
        
        setTxtMinBalDebitCards(CommonUtil.convertObjToStr(objOperativeAcctSpclitemParamTO.getMinBalDrCard()));
        
        if (objOperativeAcctSpclitemParamTO.getIvrsProvided() != null) {
            if (objOperativeAcctSpclitemParamTO.getIvrsProvided().equals(YES)) setRdoIVRSProvided_Yes(true);
            else setRdoIVRSProvided_No(true);
        }
        
        setTxtMinBalIVRS(CommonUtil.convertObjToStr(objOperativeAcctSpclitemParamTO.getMinBalIvrs()));
        
        if (objOperativeAcctSpclitemParamTO.getMobileBanking() != null) {
            if (objOperativeAcctSpclitemParamTO.getMobileBanking().equals(YES)) setRdoMobBankClient_Yes(true);
            else setRdoMobBankClient_No(true);
        }
        
        setTxtMinMobBank(CommonUtil.convertObjToStr(objOperativeAcctSpclitemParamTO.getMinBalMobile()));
        
        if (objOperativeAcctSpclitemParamTO.getAnyBranchBanking() != null) {
            if (objOperativeAcctSpclitemParamTO.getAnyBranchBanking().equals(YES)) setRdoABBAllowed_Yes(true);
            else setRdoABBAllowed_No(true);
        }
        
        setTxtMinBalABB(CommonUtil.convertObjToStr(objOperativeAcctSpclitemParamTO.getMinBalAbb()));
        setTxtIMPSLimit(CommonUtil.convertObjToStr(objOperativeAcctSpclitemParamTO.getImpsLimit()));
        setCboFlexiTD(CommonUtil.convertObjToStr(objOperativeAcctSpclitemParamTO.getFlexiProdId()));
    }
    
    private void setOperativeAcctHeadParamTO(OperativeAcctHeadParamTO objOperativeAcctHeadParamTO) {
        // Fillup OperativeAcctHeadParamTO Object ---------------------------------------------------
       // String InopAcChrg=CommonUtil.convertObjToStr(objOperativeAcctHeadParamTO.getInopAcChrg());
        
        setTxtInOpChrg(CommonUtil.convertObjToStr(objOperativeAcctHeadParamTO.getInopAcChrg()));
        setTxtPrematureClosureChrg(CommonUtil.convertObjToStr(objOperativeAcctHeadParamTO.getPrematcloseChrg()));
        setTxtAcctClosingChrg(CommonUtil.convertObjToStr(objOperativeAcctHeadParamTO.getAccloseChrg()));
        setTxtMiscServChrg(CommonUtil.convertObjToStr(objOperativeAcctHeadParamTO.getMisserChrg()));
        setTxtStatChrg(CommonUtil.convertObjToStr(objOperativeAcctHeadParamTO.getStatChrg()));
        setTxtFreeWDChrg(CommonUtil.convertObjToStr(objOperativeAcctHeadParamTO.getFreewithdChrg()));
        setTxtAcctDebitInt(CommonUtil.convertObjToStr(objOperativeAcctHeadParamTO.getDebitInt()));
        //String InopAcChrg=CommonUtil.convertObjToStr(objOperativeAcctHeadParamTO.getCreditInt());
       // System.out.println("InopAcChrgInopAcChrgInopAcChrgInopAcChrg"+InopAcChrg);
        setTxtAcctCrInt(CommonUtil.convertObjToStr(objOperativeAcctHeadParamTO.getCreditInt()));
        setTxtClearingIntAcctHd(CommonUtil.convertObjToStr(objOperativeAcctHeadParamTO.getClrInt()));
        setTxtChkBkIssueChrg(CommonUtil.convertObjToStr(objOperativeAcctHeadParamTO.getChqIssueChrg()));
        setTxtStopPaymentChrg(CommonUtil.convertObjToStr(objOperativeAcctHeadParamTO.getStopPmtChrg()));
        setTxtOutwardChkRetChrg(CommonUtil.convertObjToStr(objOperativeAcctHeadParamTO.getChqRetOut()) );
        setTxtInwardChkRetChrg(CommonUtil.convertObjToStr(objOperativeAcctHeadParamTO.getChqRetIn()));
        setTxtAcctOpenChrg(CommonUtil.convertObjToStr(objOperativeAcctHeadParamTO.getActOpChrg()));
        setTxtExcessFreeWDChrg(CommonUtil.convertObjToStr(objOperativeAcctHeadParamTO.getExcessFreeWithd()));
        setTxtTaxGL(CommonUtil.convertObjToStr(objOperativeAcctHeadParamTO.getTax()));
        setTxtNonMainMinBalChrg(CommonUtil.convertObjToStr(objOperativeAcctHeadParamTO.getNonmntMinChrg()));
        setTxtInOperative(CommonUtil.convertObjToStr(objOperativeAcctHeadParamTO.getInopt()));
        setTxtFolioChrg(CommonUtil.convertObjToStr(objOperativeAcctHeadParamTO.getFolioChrg()));
        setTxtATMGL(CommonUtil.convertObjToStr(objOperativeAcctHeadParamTO.getAtmGL()));

        // Added by nithya on 17-03-2016 for 0004021
        setTxtDebitWithdrawalChargeAcctHead(objOperativeAcctHeadParamTO.getDebitWithdrawalCharge());
        // End
    }
    
//    private void setOperativeAcctIntRateParamTO(ArrayList objTO) {
//        tmlIntRate.setData(new ArrayList());
//        operativeAcctIntTOs.clear();
//        HashMap map= new HashMap();
//        
//        int j=objTO.size();
//        for ( int i=0; i<j;i++) {
//            map = (HashMap)objTO.get(i);
//            ArrayList arr= new ArrayList();
//            arr.add(map.get("acHdId"));
//            arr.add(map.get("intCatId"));
//            arr.add(map.get("intDate"));
//            arr.add(CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr((java.math.BigDecimal)map.get("intRate"))));
//            
//            /*insertIRData(arr);*/
//            operativeAcctIntTOs.add(getOperativeAcctIntRateTO(arr));
//            arr.set(1,map.get("intCatDesc"));
//            tmlIntRate.insertRow(tmlIntRate.getRowCount(),arr);
//            tmlIntRate.fireTableDataChanged();
//        }
//    }
    
//    public ArrayList populateIntRate(int rowNum) {
//        return tmlIntRate.getRow(rowNum);
//    }
//    
//    public void deleteIRData(int rowNum) {
//        tmlIntRate.removeRow(rowNum);
//        operativeAcctIntTOs.remove(rowNum);
//    }
    
//    public int insertIRData(ArrayList irRow) {
//        String categoryDesc = (String)cbmIntCategory.getDataForKey(irRow.get(1));
//        ArrayList arr = tmlIntRate.getDataArrayList();
//        int j = arr.size();
//        for (int i=0;i<j;i++) {
//            ArrayList arr1 = (ArrayList)arr.get(i);
//            if ( arr1.get(0).equals(irRow.get(0)) &&
//            arr1.get(1).equals(categoryDesc) &&
//            arr1.get(2).equals(irRow.get(2)) )
//                return -1;
//        }
//        operativeAcctIntTOs.add(getOperativeAcctIntRateTO(irRow));
//        irRow.set(1,categoryDesc);
//        tmlIntRate.insertRow(tmlIntRate.getRowCount(), irRow);
//        tmlIntRate.fireTableDataChanged();
//        return 0;
//    }
    
    private void populateOB(HashMap mapData) {
        //resetOBFields();
        
        OperativeAcctProductTO objOperativeAcctProductTO = null;
        OperativeAcctParamTO objOperativeAcctParamTO = null;
        OperativeAcctIntRecvParamTO objOperativeAcctIntRecvParamTO = null;
        OperativeAcctIntPayParamTO objOperativeAcctIntPayParamTO = null;
        OperativeAcctChargesParamTO objOperativeAcctChargesParamTO = null;
        OperativeAcctSpclitemParamTO objOperativeAcctSpclitemParamTO = null;
        OperativeAcctHeadParamTO objOperativeAcctHeadParamTO = null;
        
        if (((List) mapData.get("OperativeAcctProductTO")).size() > 0){
            objOperativeAcctProductTO = (OperativeAcctProductTO) ((List) mapData.get("OperativeAcctProductTO")).get(0);
            setOperativeAcctProductTO(objOperativeAcctProductTO);
        }
        
        if (((List) mapData.get("OperativeAcctParamTO")).size() > 0){
            objOperativeAcctParamTO = (OperativeAcctParamTO) ((List) mapData.get("OperativeAcctParamTO")).get(0);
            setOperativeAcctParamTO(objOperativeAcctParamTO);
        }
        
        if (((List) mapData.get("OperativeAcctIntRecvParamTO")).size() > 0){
            objOperativeAcctIntRecvParamTO = (OperativeAcctIntRecvParamTO) ((List) mapData.get("OperativeAcctIntRecvParamTO")).get(0);
            setOperativeAcctIntRecvParamTO(objOperativeAcctIntRecvParamTO);
        }
        
        if (((List) mapData.get("OperativeAcctIntPayParamTO")).size() > 0){
            objOperativeAcctIntPayParamTO = (OperativeAcctIntPayParamTO) ((List) mapData.get("OperativeAcctIntPayParamTO")).get(0);
            setOperativeAcctIntPayParamTO(objOperativeAcctIntPayParamTO);
        }
        
        if (((List) mapData.get("OperativeAcctChargesParamTO")).size() > 0){
            objOperativeAcctChargesParamTO = (OperativeAcctChargesParamTO) ((List) mapData.get("OperativeAcctChargesParamTO")).get(0);
            setOperativeAcctChargesParamTO(objOperativeAcctChargesParamTO);
        }
        
        if (((List) mapData.get("OperativeAcctSpclitemParamTO")).size() > 0){
            objOperativeAcctSpclitemParamTO = (OperativeAcctSpclitemParamTO) ((List) mapData.get("OperativeAcctSpclitemParamTO")).get(0);
            setOperativeAcctSpclitemParamTO(objOperativeAcctSpclitemParamTO);
        }
        
        if (((List) mapData.get("OperativeAcctHeadParamTO")).size() > 0){
            objOperativeAcctHeadParamTO = (OperativeAcctHeadParamTO) ((List) mapData.get("OperativeAcctHeadParamTO")).get(0);
            setOperativeAcctHeadParamTO(objOperativeAcctHeadParamTO);
        }
        
//        setOperativeAcctIntRateParamTO((ArrayList) mapData.get("OperativeAcctIntRateParamTO"));
        
        //notifyObservers();
    }
    
    /**
     * To know whether the Prod Id/Desc Already exist or not...
     * @return
     * @param strProdId Product Id To be verified
     * @param strProdDesc The description of the Product
     */
    public boolean isProdIdExist(String strProdId, String strProdDesc){
        boolean exist = false;
        if ((getLblStatus().equals(ClientConstants.ACTION_STATUS[1])) || (getLblStatus().equals(ClientConstants.ACTION_STATUS[20]))){
            int option = -1;
            final HashMap transactionMap = new HashMap();
            HashMap retrieve = new HashMap();
            transactionMap.put("prodId", strProdId);
            transactionMap.put("prodDesc", strProdDesc);
            final List resultList = ClientUtil.executeQuery("getProdIdCount", transactionMap);
            if (Integer.parseInt(((HashMap) resultList.get(0)).get("NO_ID").toString()) > 0){
                String[] options = {objOperativeAcctProductnewRB.getString("strOK")};
                option = COptionPane.showOptionDialog(null, objOperativeAcctProductnewRB.getString("PRODIDWARNING"), CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                exist = true;
            }
        }
        return exist;
    }
    
    private OperativeAcctProductTO getOperativeAcctProductTO(String command) {
        // Fillup OperativeAcctProductTO Object ----------------------------------------------------
        OperativeAcctProductTO objOperativeAcctProductTO = new OperativeAcctProductTO();
        objOperativeAcctProductTO.setCommand(command);
        objOperativeAcctProductTO.setProdId(getTxtProductID());
        objOperativeAcctProductTO.setAcHdId(getTxtAcctHd());
        objOperativeAcctProductTO.setProdDesc(getTxtDesc());
        objOperativeAcctProductTO.setBehavior(getCboBehaves());
        objOperativeAcctProductTO.setBaseCurrency(getCboProdCurrency());
         if (isRdoAcc_Reg() == true) objOperativeAcctProductTO.setSRemarks(REGULAR);
        else if (isRdoAcc_Nro() == true) objOperativeAcctProductTO.setSRemarks(NRO);
        else if (isRdoAcc_Nre() == true) objOperativeAcctProductTO.setSRemarks(NRE);
         // Added by nithya on 17-03-2016 for 0004021
        objOperativeAcctProductTO.setIsdebitWithdrawalCharge(getChkDebitWithdrawalCharge());
        objOperativeAcctProductTO.setChargePeriod(CommonUtil.convertObjToInt(getTxtDebitWithdrawalChargePeriod()));
        
        return objOperativeAcctProductTO;
    }
    
    private OperativeAcctParamTO getOperativeAcctParamTO() {
        int multiply = 0;
        
        // Fillup OperativeAcctProductTO Object ----------------------------------------------------
        OperativeAcctParamTO objOperativeAcctParamTO = new OperativeAcctParamTO();
        //objOperativeAcctParamTO.setCommand(command);
        objOperativeAcctParamTO.setProdId(getTxtProductID());
        
        if (getRdoChkAllowedYes() == true) objOperativeAcctParamTO.setChkAllowed(YES);
        else objOperativeAcctParamTO.setChkAllowed(NO);
        
        objOperativeAcctParamTO.setMinBalWChk(CommonUtil.convertObjToDouble(getTxtMinBalChkbk()));
        objOperativeAcctParamTO.setMinBalWtChk(CommonUtil.convertObjToDouble(getTxtMinBalwchk()));
        
        if (getRdoIntroReqYes() == true) objOperativeAcctParamTO.setIntReq(YES);
        else objOperativeAcctParamTO.setIntReq(NO);
        
        if (getRdoNomineeReqYes() == true) objOperativeAcctParamTO.setNomineeReq(YES);
        else objOperativeAcctParamTO.setNomineeReq(NO);
        
        objOperativeAcctParamTO.setNoOfNominee(CommonUtil.convertObjToDouble(getTxtNoNominees()));
        
        if (getRdoAcctOpenApprYes() == true) objOperativeAcctParamTO.setAcOpReq(YES);
        else objOperativeAcctParamTO.setAcOpReq(NO);
        
        if (getCboMinTreatasNew().equalsIgnoreCase(DAYS)) {
            multiply = IDAYS;
        } else if (getCboMinTreatasNew().equalsIgnoreCase(MONTHS)) {
            multiply = IMONTHS;
        } else if (getCboMinTreatasNew().equalsIgnoreCase(YEARS)) {
            multiply = IYEARS;
        } else {
            multiply = 0;
        }
        objOperativeAcctParamTO.setMinPdNew(new Double(CommonUtil.convertObjToInt(getTxtMinTreatasNew()) * multiply));
        
        if (getCboMinTreatasDormant().equalsIgnoreCase(DAYS)) {
            multiply = IDAYS;
        } else if (getCboMinTreatasDormant().equalsIgnoreCase(MONTHS)) {
            multiply = IMONTHS;
        } else if (getCboMinTreatasDormant().equalsIgnoreCase(YEARS)) {
            multiply = IYEARS;
        } else {
            multiply = 0;
        }
        objOperativeAcctParamTO.setMinPdDormant(new Double(CommonUtil.convertObjToInt(getTxtMinTreatasDormant()) * multiply));
        
        if (getCboMinTreatInOp().equalsIgnoreCase(DAYS)) {
            multiply = IDAYS;
        } else if (getCboMinTreatInOp().equalsIgnoreCase(MONTHS)) {
            multiply = IMONTHS;
        } else if (getCboMinTreatInOp().equalsIgnoreCase(YEARS)) {
            multiply = IYEARS;
        } else {
            multiply = 0;
        }
        objOperativeAcctParamTO.setMinPdInop(new Double(CommonUtil.convertObjToInt(getTxtMinTreatasInOp()) * multiply));
        
        if (getCboMinTreatNewClosure().equalsIgnoreCase(DAYS)) {
            multiply = IDAYS;
        } else if (getCboMinTreatNewClosure().equalsIgnoreCase(MONTHS)) {
            multiply = IMONTHS;
        } else if (getCboMinTreatNewClosure().equalsIgnoreCase(YEARS)) {
            multiply = IYEARS;
        } else {
            multiply = 0;
        }
        objOperativeAcctParamTO.setMinPdCls(new Double(CommonUtil.convertObjToInt(getTxtMainTreatNewAcctClosure()) * multiply));
        
        objOperativeAcctParamTO.setStatFrequency(CommonUtil.convertObjToDouble(getCboStatFreq()));
        
        objOperativeAcctParamTO.setFreeWithdrawals(CommonUtil.convertObjToDouble(getTxtNoFreeWD()));
        if (getCboFreeWDPd().equalsIgnoreCase(DAYS)) {
            multiply = IDAYS;
        } else if (getCboFreeWDPd().equalsIgnoreCase(MONTHS)) {
            multiply = IMONTHS;
        } else if (getCboFreeWDPd().equalsIgnoreCase(YEARS)) {
            multiply = IYEARS;
        } else {
            multiply = 0;
        }
        objOperativeAcctParamTO.setFreeWithdrawalsPd(new Double(CommonUtil.convertObjToInt(getTxtFreeWDPd()) * multiply));
        objOperativeAcctParamTO.setFreeWithdrawalType(getCboFreeWDStFrom());
        
        Date FreeDt = DateUtil.getDateMMDDYYYY(getTdtFreeWDStFrom());
        if(FreeDt != null){
        Date freeDate = (Date)curDate.clone();
        freeDate.setDate(FreeDt.getDate());
        freeDate.setMonth(FreeDt.getMonth());
        freeDate.setYear(FreeDt.getYear());
//        objOperativeAcctParamTO.setFreeWithdrawalFrom(DateUtil.getDateMMDDYYYY(getTdtFreeWDStFrom()));
        objOperativeAcctParamTO.setFreeWithdrawalFrom(freeDate);
        }else{
           objOperativeAcctParamTO.setFreeWithdrawalFrom(DateUtil.getDateMMDDYYYY(getTdtFreeWDStFrom())); 
        }
        
        objOperativeAcctParamTO.setNoFreeChkLeaves(CommonUtil.convertObjToDouble(getTxtNoFreeChkLeaves()));
        if (getCboFreeChkPd().equalsIgnoreCase(DAYS)) {
            multiply = IDAYS;
        } else if (getCboFreeChkPd().equalsIgnoreCase(MONTHS)) {
            multiply = IMONTHS;
        } else if (getCboFreeChkPd().equalsIgnoreCase(YEARS)) {
            multiply = IYEARS;
        } else {
            multiply = 0;
        }
        objOperativeAcctParamTO.setFreeChkLeavesPd(new Double(CommonUtil.convertObjToInt(getTxtFreeChkPD()) * multiply));
        objOperativeAcctParamTO.setFreeChkLeavesType(getCboFreeChkLeaveStFrom());
        
        Date FreChkDt = DateUtil.getDateMMDDYYYY(getTdtFreeChkLeaveStFrom());
        if(FreChkDt != null){
        Date frechkDate = (Date)curDate.clone();
        frechkDate.setDate(FreChkDt.getDate());
        frechkDate.setMonth(FreChkDt.getMonth());
        frechkDate.setYear(FreChkDt.getYear());
//        objOperativeAcctParamTO.setFreeChkLeavesFrom(DateUtil.getDateMMDDYYYY(getTdtFreeChkLeaveStFrom()));
        objOperativeAcctParamTO.setFreeChkLeavesFrom(frechkDate);
        }else{
            objOperativeAcctParamTO.setFreeChkLeavesFrom(DateUtil.getDateMMDDYYYY(getTdtFreeChkLeaveStFrom()));
        }
        
        if (getRdoTaxIntApplNROYes() == true) objOperativeAcctParamTO.setTaxIntApplicable(YES);
        else objOperativeAcctParamTO.setTaxIntApplicable(NO);
        
        objOperativeAcctParamTO.setRateOfInt(CommonUtil.convertObjToDouble(getTxtRateTaxNRO()));
        
        if (getRdoLimitDefAllowYes() == true) objOperativeAcctParamTO.setLmtDefinitionAllowed(YES);
        else objOperativeAcctParamTO.setLmtDefinitionAllowed(NO);
        
        if (getRdoTempODAllowYes() == true) objOperativeAcctParamTO.setTempOdAllowed(YES);
        else objOperativeAcctParamTO.setTempOdAllowed(NO);
        
        if (getRdoStaffAcctYes() == true) objOperativeAcctParamTO.setStaffAcctOpened(YES);
        else objOperativeAcctParamTO.setStaffAcctOpened(NO);
        
        //        if (getRdoExtraIntApplYes() == true) objOperativeAcctParamTO.setExtraIntApplicable (YES);
        //        else objOperativeAcctParamTO.setExtraIntApplicable (NO);
        
        if (getRdoCollectIntYes() == true) objOperativeAcctParamTO.setCollectIntCredit(YES);
        else objOperativeAcctParamTO.setCollectIntCredit(NO);
        
        if (getRdoIntClearing_Yes() == true) objOperativeAcctParamTO.setDebitIntClearing(YES);
        else objOperativeAcctParamTO.setDebitIntClearing(NO);
        
        if (getRdoIntUnClearBalYes() == true) objOperativeAcctParamTO.setCreditIntUnclear(YES);
        else objOperativeAcctParamTO.setCreditIntUnclear(NO);
        
        if (getRdoIssueTokenYes() == true) objOperativeAcctParamTO.setIssueToken(YES);
        else objOperativeAcctParamTO.setIssueToken(NO);
        
        if (getRdoAllowWDYes() == true) objOperativeAcctParamTO.setAllowWithdrawalSlip(YES);
        else objOperativeAcctParamTO.setAllowWithdrawalSlip(NO);
        
        objOperativeAcctParamTO.setMaxAllowedWdSlip(CommonUtil.convertObjToDouble(getTxtMaxAmtWDSlip()));
        
        objOperativeAcctParamTO.setNumPatternFollowedPrefix(getTxtNumPatternFollowedPrefix());
        objOperativeAcctParamTO.setNumPatternFollowedSuffix(new Long(getTxtNumPatternFollowedSuffix()));
        if(getTxtLastAccNum().length()>0)
        objOperativeAcctParamTO.setLastAccNum(new Long (getTxtLastAccNum()));
       
        
//        if(getTxtNumPatternFollowedSuffix().equals("1"))
//             objOperativeAcctParamTO.setLastAccNum(new Long (0));   
//        else{
//           long s= CommonUtil.convertObjToLong(getTxtNumPatternFollowedSuffix());
//		s=s-1;
//        objOperativeAcctParamTO.setLastAccNum(new Long(s));  
//        }
        return objOperativeAcctParamTO;
    }
    
    private OperativeAcctIntRecvParamTO getOperativeAcctIntRecvParamTO() {
        // Fillup OperativeAcctProductTO Object ----------------------------------------------------
        OperativeAcctIntRecvParamTO objOperativeAcctIntRecvParamTO = new OperativeAcctIntRecvParamTO();
        //objOperativeAcctIntRecvParamTO.setCommand(command);
        objOperativeAcctIntRecvParamTO.setProdId(getTxtProductID());
        
        if (getRdoDebitIntChrgYes2() == true) objOperativeAcctIntRecvParamTO.setDebitIntCharged(YES);
        else objOperativeAcctIntRecvParamTO.setDebitIntCharged(NO);
        
        objOperativeAcctIntRecvParamTO.setMinDebitIntRate(CommonUtil.convertObjToDouble(getTxtMinDebitIntRate()));
        objOperativeAcctIntRecvParamTO.setMaxDebitIntRate(CommonUtil.convertObjToDouble(getTxtMaxDebitIntRate()));
        objOperativeAcctIntRecvParamTO.setApplDebitIntRate(CommonUtil.convertObjToDouble(getTxtApplDebitIntRate()));
        objOperativeAcctIntRecvParamTO.setMinDebitIntAmt(CommonUtil.convertObjToDouble(getTxtMinDebitIntAmt()));
        objOperativeAcctIntRecvParamTO.setMaxDebitIntAmt(CommonUtil.convertObjToDouble(getTxtMaxDebitIntAmt()));
        
        objOperativeAcctIntRecvParamTO.setDebitIntCalcFreq(CommonUtil.convertObjToDouble(getCboDebitIntCalcFreq()));
        objOperativeAcctIntRecvParamTO.setDebitIntApplFreq(CommonUtil.convertObjToDouble(getCboDebitIntApplFreq()));
        
        objOperativeAcctIntRecvParamTO.setProductFreq(CommonUtil.convertObjToDouble(getCboProdFreq()));
        
        if (getRdoDebitCompReqYes() == true) objOperativeAcctIntRecvParamTO.setDebitCompound(YES);
        else objOperativeAcctIntRecvParamTO.setDebitCompound(NO);
        
        objOperativeAcctIntRecvParamTO.setDebitCompintCalcFreq(CommonUtil.convertObjToDouble(getCboDebitCompIntCalcFreq()));
        
        objOperativeAcctIntRecvParamTO.setDebitProductRoundoff(getCboDebitProductRoundOff());
        objOperativeAcctIntRecvParamTO.setDebitIntRoundoff(getCboDebitIntRoundOff());
        
        Date LstIntDt = DateUtil.getDateMMDDYYYY(getTdtLastIntCalcDate());
        if(LstIntDt != null){
        Date lstintDate = (Date)curDate.clone();
        lstintDate.setDate(LstIntDt.getDate());
        lstintDate.setMonth(LstIntDt.getMonth());
        lstintDate.setYear(LstIntDt.getYear());
        objOperativeAcctIntRecvParamTO.setLastIntCalcdtDebit(lstintDate);
        }else{
            objOperativeAcctIntRecvParamTO.setLastIntCalcdtDebit(DateUtil.getDateMMDDYYYY(getTdtLastIntCalcDate()));
        }
//        objOperativeAcctIntRecvParamTO.setLastIntCalcdtDebit(DateUtil.getDateMMDDYYYY(getTdtLastIntCalcDate()));
        
        Date LstApDt = DateUtil.getDateMMDDYYYY(getTdtLastIntApplDate());
        if(LstApDt != null){
        Date lstApDate = (Date)curDate.clone();
        lstApDate.setDate(LstApDt.getDate());
        lstApDate.setMonth(LstApDt.getMonth());
        lstApDate.setYear(LstApDt.getYear());
        objOperativeAcctIntRecvParamTO.setLastIntAppldtDebit(lstApDate);
        }else{
            objOperativeAcctIntRecvParamTO.setLastIntAppldtDebit(DateUtil.getDateMMDDYYYY(getTdtLastIntApplDate()));
        }
//        objOperativeAcctIntRecvParamTO.setLastIntAppldtDebit(DateUtil.getDateMMDDYYYY(getTdtLastIntApplDate()));
        
        objOperativeAcctIntRecvParamTO.setPenalIntDebitBalacct(CommonUtil.convertObjToDouble(getTxtPenalIntDebitBalAcct()));
        objOperativeAcctIntRecvParamTO.setPenalIntChargeStday(CommonUtil.convertObjToDouble(getTxtPenalIntChrgStart()));
        
        objOperativeAcctIntRecvParamTO.setStartdayIntCalc(CommonUtil.convertObjToDouble(getTxtStartInterCalc()));
        objOperativeAcctIntRecvParamTO.setStartmonIntCalc(getCboStartInterCalc());
        objOperativeAcctIntRecvParamTO.setEnddayIntCalc(CommonUtil.convertObjToDouble(getTxtEndInterCalc()));
        objOperativeAcctIntRecvParamTO.setEndmonIntCalc(getCboEndInterCalc());
        
        objOperativeAcctIntRecvParamTO.setStartdayProdCalc(CommonUtil.convertObjToDouble(getTxtStartProdCalc()));
        objOperativeAcctIntRecvParamTO.setStartmonProdCalc(getCboStartProdCalc());
        objOperativeAcctIntRecvParamTO.setEnddayProdCalc(CommonUtil.convertObjToDouble(getTxtEndProdCalc()));
        objOperativeAcctIntRecvParamTO.setEndmonProdCalc(getCboEndProdCalc());
        
        return objOperativeAcctIntRecvParamTO;
    }
    
    private OperativeAcctIntPayParamTO getOperativeAcctIntPayParamTO() {
        // Fillup OperativeAcctIntPayParamTO Object ----------------------------------------------------
        OperativeAcctIntPayParamTO objOperativeAcctIntPayParamTO = new OperativeAcctIntPayParamTO();
        //objOperativeAcctIntPayParamTO.setCommand(command);
        objOperativeAcctIntPayParamTO.setProdId(getTxtProductID());
        objOperativeAcctIntPayParamTO.setMinCrIntRate(CommonUtil.convertObjToDouble(getTxtMinCrIntRate()));
        objOperativeAcctIntPayParamTO.setMaxCrIntRate(CommonUtil.convertObjToDouble(getTxtMaxCrIntRate()));
        objOperativeAcctIntPayParamTO.setApplCrIntRate(CommonUtil.convertObjToDouble(getTxtApplCrIntRate()));
        objOperativeAcctIntPayParamTO.setMinCrIntAmt(CommonUtil.convertObjToDouble(getTxtMinCrIntAmt()) );
        objOperativeAcctIntPayParamTO.setMaxCrIntAmt(CommonUtil.convertObjToDouble(getTxtMaxCrIntAmt()));
        objOperativeAcctIntPayParamTO.setMinBalforIntCalc(CommonUtil.convertObjToDouble(getMinBalForIntCalc()));
        
        objOperativeAcctIntPayParamTO.setCrIntCalcFreq(CommonUtil.convertObjToDouble(getCboCrIntCalcFreq()));
        objOperativeAcctIntPayParamTO.setCrIntApplFreq(CommonUtil.convertObjToDouble(getCboCrIntApplFreq()));
        
        objOperativeAcctIntPayParamTO.setStartmonIntCalc(getCboStMonIntCalc());
        objOperativeAcctIntPayParamTO.setStartdayIntCalc(CommonUtil.convertObjToDouble(getTxtStMonIntCalc()));
        
        objOperativeAcctIntPayParamTO.setEndmonIntCalc(getCboIntCalcEndMon());
        objOperativeAcctIntPayParamTO.setEnddayIntCalc(CommonUtil.convertObjToDouble(getTxtIntCalcEndMon()));
        
        Date LsCalDt = DateUtil.getDateMMDDYYYY(getTdtLastIntCalcDateCR());
        if(LsCalDt != null){
            Date lsCalDate = (Date)curDate.clone();
            lsCalDate.setDate(LsCalDt.getDate());
            lsCalDate.setMonth(LsCalDt.getMonth());
            lsCalDate.setYear(LsCalDt.getYear());
//        objOperativeAcctIntPayParamTO.setLastIntCalcdtCr(DateUtil.getDateMMDDYYYY(getTdtLastIntCalcDateCR()));
        objOperativeAcctIntPayParamTO.setLastIntCalcdtCr(lsCalDate);
        }else{
            objOperativeAcctIntPayParamTO.setLastIntCalcdtCr(DateUtil.getDateMMDDYYYY(getTdtLastIntCalcDateCR()));
        }
        
        Date LsInAppDt = DateUtil.getDateMMDDYYYY(getTdtLastIntApplDateCr());
        if(LsInAppDt != null){
            Date lsinAppDate = (Date)curDate.clone();
            lsinAppDate.setDate(LsInAppDt.getDate());
            lsinAppDate.setMonth(LsInAppDt.getMonth());
            lsinAppDate.setYear(LsInAppDt.getYear());
//        objOperativeAcctIntPayParamTO.setLastIntAppldtCr(DateUtil.getDateMMDDYYYY(getTdtLastIntApplDateCr()));
        objOperativeAcctIntPayParamTO.setLastIntAppldtCr(lsinAppDate);
        }else{
            objOperativeAcctIntPayParamTO.setLastIntAppldtCr(DateUtil.getDateMMDDYYYY(getTdtLastIntApplDateCr()));
        }
        
        if (getRdoCreditCompYes() == true) objOperativeAcctIntPayParamTO.setCrCompound(YES);
        else objOperativeAcctIntPayParamTO.setCrCompound(NO);
        
        objOperativeAcctIntPayParamTO.setCrCompoundCalcFreq(CommonUtil.convertObjToDouble(getCboCreditCompIntCalcFreq()));
        
        objOperativeAcctIntPayParamTO.setCrProductRoundoff(getCboCreditProductRoundOff());
        objOperativeAcctIntPayParamTO.setCrIntRoundoff(getCboCreditIntRoundOff());
        
        objOperativeAcctIntPayParamTO.setStartdayProdCalc(CommonUtil.convertObjToDouble(getTxtStDayProdCalcSBCrInt()));
        objOperativeAcctIntPayParamTO.setStartmonProdCalc(getCboStDayProdCalcSBCrInt());
        
        objOperativeAcctIntPayParamTO.setEnddayProdCalc(CommonUtil.convertObjToDouble(getTxtEndDayProdCalcSBCrInt()));
        objOperativeAcctIntPayParamTO.setEndmonProdCalc(getCboEndDayProdCalcSBCrInt());
        
        objOperativeAcctIntPayParamTO.setCalcCriteria(getCboCalcCriteria());
        objOperativeAcctIntPayParamTO.setProductFreqIntPay(getCboProdFreqCr());
        
        if (getRdoCrIntGivenYes() == true) objOperativeAcctIntPayParamTO.setCreditIntGiven(YES);
        else objOperativeAcctIntPayParamTO.setCreditIntGiven(NO);
        //        objOperativeAcctIntPayParamTO.setAdditionalIntStaff (CommonUtil.convertObjToDouble(getTxtAddIntStaff()));
        
        return objOperativeAcctIntPayParamTO;
    }
    
    private OperativeAcctChargesParamTO getOperativeAcctChargesParamTO() {
        // Fillup OperativeAcctChargesParamTO Object ----------------------------------------------------
        OperativeAcctChargesParamTO objOperativeAcctChargesParamTO = new OperativeAcctChargesParamTO();
        //objOperativeAcctChargesParamTO.setCommand(command);
        objOperativeAcctChargesParamTO.setProdId(getTxtProductID());
        objOperativeAcctChargesParamTO.setInoperativeAcCharges(CommonUtil.convertObjToDouble(getTxtlInOpAcChrg()));
        objOperativeAcctChargesParamTO.setInoperativeAcChargePd(CommonUtil.convertObjToDouble(getCboInOpAcChrgPd()));
        objOperativeAcctChargesParamTO.setChgPrematureClosure(CommonUtil.convertObjToDouble(getTxtChrgPreClosure()));
        objOperativeAcctChargesParamTO.setAcctClosingChg(CommonUtil.convertObjToDouble(getTxtAcClosingChrg()));
        objOperativeAcctChargesParamTO.setMiscServiceChg(CommonUtil.convertObjToDouble(getTxtChrgMiscServChrg()));
        
        if (getRdoNonMainMinBalChrg_Yes() == true) objOperativeAcctChargesParamTO.setNonmainMinBalChg(YES);
        else objOperativeAcctChargesParamTO.setNonmainMinBalChg(NO);
        
        objOperativeAcctChargesParamTO.setAmtNonmainMinbal(CommonUtil.convertObjToDouble(getTxtMinBalAmt()));
        objOperativeAcctChargesParamTO.setAmtNonmainMinbalPd(getCboMinBalAmt());
        
        if (getRdoStatCharges_Yes() == true) objOperativeAcctChargesParamTO.setStatCharge(YES);
        else objOperativeAcctChargesParamTO.setStatCharge(NO);
        
        objOperativeAcctChargesParamTO.setAmtChargeStat(CommonUtil.convertObjToDouble(getTxtStatChargesChr()));
        objOperativeAcctChargesParamTO.setAmtChargeStatType(getCboStatChargesChr());
        
        if (getRdoChkIssuedChrgCh_Yes() == true) objOperativeAcctChargesParamTO.setChkIssueChg(YES);
        else objOperativeAcctChargesParamTO.setChkIssueChg(NO);
        
        objOperativeAcctChargesParamTO.setChkIssueChgperleaf(CommonUtil.convertObjToDouble(getTxtChkBkIssueChrgPL()));
        
        if (getRdoStopPaymentChrg_Yes() == true) objOperativeAcctChargesParamTO.setStopPaymentChg(YES);
        else objOperativeAcctChargesParamTO.setStopPaymentChg(NO);
        
        objOperativeAcctChargesParamTO.setStopPaymentAmtchg(CommonUtil.convertObjToDouble(getTxtStopPayChrg()));
        objOperativeAcctChargesParamTO.setChkReturnChgOutward(CommonUtil.convertObjToDouble(getTxtChkRetChrOutward()));
        objOperativeAcctChargesParamTO.setChkReturnChgInward(CommonUtil.convertObjToDouble(getTxtChkRetChrgIn()));
        objOperativeAcctChargesParamTO.setAcctOpeningChg(CommonUtil.convertObjToDouble(getTxtAcctOpenCharges()));
        
        if (getRdoFolioChargeAppl_Yes() == true) objOperativeAcctChargesParamTO.setFolioChgApplicable(YES);
        else objOperativeAcctChargesParamTO.setFolioChgApplicable(NO);
        
        objOperativeAcctChargesParamTO.setNoEntriesperFolio(CommonUtil.convertObjToDouble(getTxtNoEntryPerFolio()));
        objOperativeAcctChargesParamTO.setRatePerFolio(CommonUtil.convertObjToDouble(getTxtRatePerFolio()));
        
        Date LsFolDt = DateUtil.getDateMMDDYYYY(getTdtLastFolioChargeDt());
        if(LsFolDt != null){
            Date lsfolDate = (Date)curDate.clone();
            lsfolDate.setDate(LsFolDt.getDate());
            lsfolDate.setMonth(LsFolDt.getMonth());
            lsfolDate.setYear(LsFolDt.getYear());
        objOperativeAcctChargesParamTO.setLastFolioChargedt(lsfolDate);
        }else{
            objOperativeAcctChargesParamTO.setLastFolioChargedt(DateUtil.getDateMMDDYYYY(getTdtLastFolioChargeDt()));
        }
//        objOperativeAcctChargesParamTO.setLastFolioChargedt(DateUtil.getDateMMDDYYYY(getTdtLastFolioChargeDt()));
        
        Date NxFolDt = DateUtil.getDateMMDDYYYY(getTdtNextFolioDueDate());
        if(NxFolDt != null){
            Date nxfolDate = (Date)curDate.clone();
            nxfolDate.setDate(NxFolDt.getDate());
            nxfolDate.setMonth(NxFolDt.getMonth());
            nxfolDate.setYear(NxFolDt.getYear());
        objOperativeAcctChargesParamTO.setNextFolioChargedt(nxfolDate);
        }else{
            objOperativeAcctChargesParamTO.setNextFolioChargedt(DateUtil.getDateMMDDYYYY(getTdtNextFolioDueDate()));
        }
//        objOperativeAcctChargesParamTO.setNextFolioChargedt(DateUtil.getDateMMDDYYYY(getTdtNextFolioDueDate()));
        
        if (getRdoToChargeOnApplFreq_Manual() == true) objOperativeAcctChargesParamTO.setToChargeOn(MANUAL);
        else if (getRdoToChargeOnApplFreq_System() == true) objOperativeAcctChargesParamTO.setToChargeOn(SYSTEM);
        else if (getRdoToChargeOnApplFreq_Both() == true) objOperativeAcctChargesParamTO.setToChargeOn(BOTH);
        
        objOperativeAcctChargesParamTO.setFolioChgApplFreq(CommonUtil.convertObjToDouble(getCboFolioChrgApplFreq()));
        
        objOperativeAcctChargesParamTO.setToCollectFolioChg(getCboToCollectFolioChrg());
        
        if (getRdoFolioToChargeOn_Credit() == true) objOperativeAcctChargesParamTO.setToChargeOnType(CREDIT);
        else if (getRdoFolioToChargeOn_Debit() == true) objOperativeAcctChargesParamTO.setToChargeOnType(DEBIT);
        else if (getRdoFolioToChargeOn_Both() == true) objOperativeAcctChargesParamTO.setToChargeOnType(BOTH);
        
        objOperativeAcctChargesParamTO.setIncompleteFolioRoundFreq(getCboIncompFolioROffFreq());
        
        objOperativeAcctChargesParamTO.setChgExcessfreewdPertrans(CommonUtil.convertObjToDouble(getTxtExcessFreeWDChrgPT()));
        //////////////////////
        //objOperativeAcctChargesParamTO.setOutstChkCharges ( );
        
        // Added by nithya on 17-03-2016 for 0004021        
        objOperativeAcctChargesParamTO.setDebitWithdrawalChargeType(getCboDebitChargeType());
        objOperativeAcctChargesParamTO.setDebitWithdrawalChargeRate(CommonUtil.convertObjToDouble(getTxtDebitChargeTypeRate()));
        // End
        objOperativeAcctChargesParamTO.setCboFolioChargeRestrictionFrq(getCboFolioChargeRestritionFreq());
        objOperativeAcctChargesParamTO.setTxtFolioChargeRestrictionPeriod(CommonUtil.convertObjToInt(getTxtFolioRestrictionPriod()));
        objOperativeAcctChargesParamTO.setTxtFolioChargeType(getTxtFolioChargeType());
        return objOperativeAcctChargesParamTO;
    }
    
    private OperativeAcctSpclitemParamTO getOperativeAcctSpclitemParamTO() {
        // Fillup OperativeAcctSpclitemParamTO Object ----------------------------------------------------
        OperativeAcctSpclitemParamTO objOperativeAcctSpclitemParamTO = new OperativeAcctSpclitemParamTO();
        //objOperativeAcctSpclitemParamTO.setCommand(command);
        objOperativeAcctSpclitemParamTO.setProdId(getTxtProductID());
        
        if (getRdoLinkFlexiAcct_Yes() == true) objOperativeAcctSpclitemParamTO.setLinkedFlexiAcct(YES);
        else objOperativeAcctSpclitemParamTO.setLinkedFlexiAcct(NO);
        
        objOperativeAcctSpclitemParamTO.setMinBal1Flexideposit(CommonUtil.convertObjToDouble(getTxtMinBal1FlexiDep()));
        objOperativeAcctSpclitemParamTO.setMinBal2Flexideposit(CommonUtil.convertObjToDouble(getTxtMinBal2FlexiDep()));
        
        if (getRdoFlexiHappen_SB() == true) objOperativeAcctSpclitemParamTO.setFlexiHappen("SB");
        else objOperativeAcctSpclitemParamTO.setFlexiHappen("TD");
        
        if (getRdoATMIssuedYes() == true) objOperativeAcctSpclitemParamTO.setAtmCardIssued(YES);
        else objOperativeAcctSpclitemParamTO.setAtmCardIssued(NO);
        
        objOperativeAcctSpclitemParamTO.setMinBalAtm(CommonUtil.convertObjToDouble(getTxtMinATMBal()));
        
        if (getRdoCreditCdIssued_Yes() == true) objOperativeAcctSpclitemParamTO.setCrCardIssued(YES);
        else objOperativeAcctSpclitemParamTO.setCrCardIssued(NO);
        
        objOperativeAcctSpclitemParamTO.setMinBalCrCard(CommonUtil.convertObjToDouble(getTxtMinBalCreditCd()));
        
        if (getRdoDebitCdIssued_Yes() == true) objOperativeAcctSpclitemParamTO.setDrCardIssued(YES);
        else objOperativeAcctSpclitemParamTO.setDrCardIssued(NO);
        
        objOperativeAcctSpclitemParamTO.setMinBalDrCard(CommonUtil.convertObjToDouble(getTxtMinBalDebitCards()));
        
        if (getRdoIVRSProvided_Yes() == true) objOperativeAcctSpclitemParamTO.setIvrsProvided(YES);
        else objOperativeAcctSpclitemParamTO.setIvrsProvided(NO);
        
        objOperativeAcctSpclitemParamTO.setMinBalIvrs(CommonUtil.convertObjToDouble(getTxtMinBalIVRS()));
        
        if (getRdoMobBankClient_Yes() == true) objOperativeAcctSpclitemParamTO.setMobileBanking(YES);
        else objOperativeAcctSpclitemParamTO.setMobileBanking(NO);
        
        objOperativeAcctSpclitemParamTO.setMinBalMobile(CommonUtil.convertObjToDouble(getTxtMinMobBank()));
        
        if (getRdoABBAllowed_Yes() == true) objOperativeAcctSpclitemParamTO.setAnyBranchBanking(YES);
        else objOperativeAcctSpclitemParamTO.setAnyBranchBanking(NO);
        
        objOperativeAcctSpclitemParamTO.setMinBalAbb(CommonUtil.convertObjToDouble(getTxtMinBalABB()));
        objOperativeAcctSpclitemParamTO.setImpsLimit(CommonUtil.convertObjToDouble(getTxtIMPSLimit()));
        objOperativeAcctSpclitemParamTO.setFlexiProdId(getCboFlexiTD());
        
        return objOperativeAcctSpclitemParamTO;
    }
    
    private OperativeAcctHeadParamTO getOperativeAcctHeadParamTO() {
        // Fillup OperativeAcctHeadParamTO Object ----------------------------------------------------
        OperativeAcctHeadParamTO objOperativeAcctHeadParamTO = new OperativeAcctHeadParamTO();
        //objOperativeAcctHeadParamTO.setCommand(command);
        objOperativeAcctHeadParamTO.setProdId(getTxtProductID());
        objOperativeAcctHeadParamTO.setInopAcChrg(getTxtInOpChrg());
        objOperativeAcctHeadParamTO.setPrematcloseChrg(getTxtPrematureClosureChrg());
        objOperativeAcctHeadParamTO.setAccloseChrg(getTxtAcctClosingChrg());
        objOperativeAcctHeadParamTO.setMisserChrg(getTxtMiscServChrg());
        objOperativeAcctHeadParamTO.setStatChrg(getTxtStatChrg());
        objOperativeAcctHeadParamTO.setFreewithdChrg(getTxtFreeWDChrg());
        objOperativeAcctHeadParamTO.setDebitInt(getTxtAcctDebitInt());
        objOperativeAcctHeadParamTO.setCreditInt(getTxtAcctCrInt());
        objOperativeAcctHeadParamTO.setClrInt(getTxtClearingIntAcctHd());
        objOperativeAcctHeadParamTO.setChqIssueChrg(getTxtChkBkIssueChrg());
        objOperativeAcctHeadParamTO.setStopPmtChrg(getTxtStopPaymentChrg());
        objOperativeAcctHeadParamTO.setChqRetOut(getTxtOutwardChkRetChrg() );
        objOperativeAcctHeadParamTO.setChqRetIn(getTxtInwardChkRetChrg());
        objOperativeAcctHeadParamTO.setActOpChrg(getTxtAcctOpenChrg());
        objOperativeAcctHeadParamTO.setExcessFreeWithd(getTxtExcessFreeWDChrg());
        objOperativeAcctHeadParamTO.setTax(getTxtTaxGL());
        objOperativeAcctHeadParamTO.setNonmntMinChrg(getTxtNonMainMinBalChrg());
        objOperativeAcctHeadParamTO.setInopt(getTxtInOperative());
        objOperativeAcctHeadParamTO.setFolioChrg(getTxtFolioChrg());
		objOperativeAcctHeadParamTO.setAtmGL(getTxtATMGL());
        objOperativeAcctHeadParamTO.setDebitWithdrawalCharge(getTxtDebitWithdrawalChargeAcctHead()); // Added by nithya on 17-03-2016 for 0004021
        return objOperativeAcctHeadParamTO;
    }
    
//    private ArrayList getOperativeAcctIntRateParamTO() {
//        return operativeAcctIntTOs;
//    }
    
    private HashMap populateBean(String command) {
        HashMap opAcctProd = new HashMap();
        
        opAcctProd.put("OperativeAcctProductTO", getOperativeAcctProductTO(command));
        opAcctProd.put("OperativeAcctParamTO", getOperativeAcctParamTO());
        opAcctProd.put("OperativeAcctIntRecvParamTO", getOperativeAcctIntRecvParamTO());
        opAcctProd.put("OperativeAcctIntPayParamTO", getOperativeAcctIntPayParamTO());
        opAcctProd.put("OperativeAcctChargesParamTO", getOperativeAcctChargesParamTO());
        opAcctProd.put("OperativeAcctSpclitemParamTO", getOperativeAcctSpclitemParamTO());
        opAcctProd.put("OperativeAcctHeadParamTO", getOperativeAcctHeadParamTO());
//        opAcctProd.put("OperativeAcctIntRateParamTO", getOperativeAcctIntRateParamTO());
        
        opAcctProd.put(CommonConstants.MODULE, getModule());
        opAcctProd.put(CommonConstants.SCREEN, getScreen());
        return opAcctProd;
    }

    
    private ComboBoxModel cbmDebitChargeType;

    public ComboBoxModel getCbmDebitChargeType() {
        return cbmDebitChargeType;
    }

    public String getCboDebitChargeType() {
        return cboDebitChargeType;
    }

    public String getChkDebitWithdrawalCharge() {
        return chkDebitWithdrawalCharge;
    }

    public String getTxtDebitChargeTypeRate() {
        return txtDebitChargeTypeRate;
    }

    public String getTxtDebitWithdrawalChargeAcctHead() {
        return txtDebitWithdrawalChargeAcctHead;
    }

    public String getTxtDebitWithdrawalChargeAcctHeadDesc() {
        return txtDebitWithdrawalChargeAcctHeadDesc;
    }

    public String getTxtDebitWithdrawalChargePeriod() {
        return txtDebitWithdrawalChargePeriod;
    }

    public void setCbmDebitChargeType(ComboBoxModel cbmDebitChargeType) {
        this.cbmDebitChargeType = cbmDebitChargeType;
    }

    public void setCboDebitChargeType(String cboDebitChargeType) {
        this.cboDebitChargeType = cboDebitChargeType;
    }

    public void setChkDebitWithdrawalCharge(String chkDebitWithdrawalCharge) {
        this.chkDebitWithdrawalCharge = chkDebitWithdrawalCharge;
    }

    public void setTxtDebitChargeTypeRate(String txtDebitChargeTypeRate) {
        this.txtDebitChargeTypeRate = txtDebitChargeTypeRate;
    }

    public void setTxtDebitWithdrawalChargeAcctHead(String txtDebitWithdrawalChargeAcctHead) {
        this.txtDebitWithdrawalChargeAcctHead = txtDebitWithdrawalChargeAcctHead;
    }

    public void setTxtDebitWithdrawalChargeAcctHeadDesc(String txtDebitWithdrawalChargeAcctHeadDesc) {
        this.txtDebitWithdrawalChargeAcctHeadDesc = txtDebitWithdrawalChargeAcctHeadDesc;
    }

    public void setTxtDebitWithdrawalChargePeriod(String txtDebitWithdrawalChargePeriod) {
        this.txtDebitWithdrawalChargePeriod = txtDebitWithdrawalChargePeriod;
    }

    public String getCboFolioChargeRestritionFreq() {
        return cboFolioChargeRestritionFreq;
    }

    public void setCboFolioChargeRestritionFreq(String cboFolioChargeRestritionFreq) {
        this.cboFolioChargeRestritionFreq = cboFolioChargeRestritionFreq;
    }



    
 
    // End
    
    
    public void resetOBFields() {
        txtProductID = "";
        txtDesc = "";
        cboBehaves = "";
        txtMinBalChkbk = "";
        txtMinBalwchk = "";
        rdoChkAllowedYes = false;
        rdoChkAllowedNo = false;
        txtNoNominees = "";
        rdoAcctOpenApprYes = false;
        rdoAcctOpenApprNo = false;
        rdoNomineeReqYes = false;
        rdoNomineeReqNo = false;
        rdoIntroReqYes = false;
        rdoIntroReqNo = false;
        txtMainTreatNewAcctClosure = "";
        txtMinTreatasDormant = "";
        txtMinTreatasNew = "";
        txtMinTreatasInOp = "";
        cboMinTreatasDormant = "";
        cboMinTreatInOp = "";
        cboMinTreatasNew = "";
        cboMinTreatNewClosure = "";
        cboStatFreq = "";
        cboProdFreq = "";
        cboProdCurrency = "";
        txtAcctHd = "";
        txtNoFreeChkLeaves = "";
        
        cboFreeWDStFrom = "";
        cboFreeChkLeaveStFrom = "";
        
        txtRateTaxNRO = "";
        txtMaxAmtWDSlip = "";
        rdoAllowWDYes = false;
        rdoAllowWDNo = false;
        rdoIntClearing_Yes = false;
        rdoIntClearing_No = false;
        rdoStaffAcctYes = false;
        rdoStaffAcctNo = false;
        rdoCollectIntYes = false;
        rdoCollectIntNo = false;
        rdoLimitDefAllowYes = false;
        rdoLimitDefAllowNo = false;
        txtNoFreeWD = "";
        rdoIntUnClearBalYes = false;
        rdoIntUnClearBalNo = false;
        rdoTempODAllowYes = false;
        rdoTempODAllowNo = false;
        //        rdoExtraIntApplYes = false;
        //        rdoExtraIntApplNo = false;
        rdoIssueTokenYes = false;
        rdoIssueTokenNo = false;
        rdoTaxIntApplNROYes = false;
        rdoTaxIntApplNRONo = false;
        tdtFreeWDStFrom = "";
        tdtFreeChkLeaveStFrom = "";
        txtFreeWDPd = "";
        cboFreeWDPd = "";
        txtFreeChkPD = "";
        cboFreeChkPd = "";
        rdoDebitCompReqYes = false;
        rdoDebitCompReqNo = false;
        txtMinDebitIntRate = "";
        txtMaxDebitIntRate = "";
        txtApplDebitIntRate = "";
        txtMinDebitIntAmt = "";
        txtMaxDebitIntAmt = "";
        cboDebitIntCalcFreq = "";
        cboDebitIntApplFreq = "";
        cboDebitIntRoundOff = "";
        cboDebitCompIntCalcFreq = "";
        cboDebitProductRoundOff = "";
        tdtLastIntCalcDate = "";
        tdtLastIntApplDate = "";
        rdoDebitIntChrgYes2 = false;
        rdoDebitIntChrgNo2 = false;
        txtPenalIntDebitBalAcct = "";
        txtPenalIntChrgStart = "";
        txtMinCrIntRate = "";
        txtMaxCrIntRate = "";
        txtApplCrIntRate = "";
        txtMinCrIntAmt = "";
        txtMaxCrIntAmt = "";
        minBalForIntCalc="";
        txtStDayProdCalcSBCrInt = "";
        txtEndDayProdCalcSBCrInt = "";
        cboCrIntCalcFreq = "";
        cboCrIntApplFreq = "";
        cboStMonIntCalc = "";
        cboIntCalcEndMon = "";
        tdtLastIntCalcDateCR = "";
        tdtLastIntApplDateCr = "";
        rdoCreditCompYes = false;
        rdoCreditCompNo = false;
        cboCreditCompIntCalcFreq = "";
        cboCreditProductRoundOff = "";
        cboCreditIntRoundOff = "";
        cboCalcCriteria = "";
        cboProdFreqCr = "";
        
        txtStartInterCalc = "";
        cboStartInterCalc = "";
        txtEndInterCalc = "";
        cboEndInterCalc = "";
        txtStartProdCalc = "";
        cboStartProdCalc = "";
        txtEndProdCalc = "";
        cboEndProdCalc = "";
        
        rdoCrIntGivenYes = false;
        rdoCrIntGivenNo = false;
        //        txtAddIntStaff = "";
        cboInOpAcChrgPd = "";
        txtlInOpAcChrg = "";
        txtChrgPreClosure = "";
        txtAcClosingChrg = "";
        txtChrgMiscServChrg = "";
        rdoNonMainMinBalChrg_Yes = false;
        rdoNonMainMinBalChrg_No = false;
        rdoChkIssuedChrgCh_Yes = false;
        rdoChkIssuedChrgCh_No = false;
        rdoFolioChargeAppl_Yes = false;
        rdoFolioChargeAppl_No = false;
        rdoFolioToChargeOn_Credit = false;
        rdoFolioToChargeOn_Debit = false;
        rdoFolioToChargeOn_Both = false;
        rdoStopPaymentChrg_Yes = false;
        rdoStopPaymentChrg_No = false;
        rdoStatCharges_Yes = false;
        rdoStatCharges_No = false;
        txtChkBkIssueChrgPL = "";
        txtStopPayChrg = "";
        txtChkRetChrOutward = "";
        txtChkRetChrgIn = "";
        txtAcctOpenCharges = "";
        txtNoEntryPerFolio = "";
        txtRatePerFolio = "";
        txtExcessFreeWDChrgPT = "";
        cboFolioChrgApplFreq = "";
        cboToCollectFolioChrg = "";
        cboIncompFolioROffFreq = "";
        txtMinBalAmt = "";
        cboMinBalAmt = "";
        txtStatChargesChr = "";
        cboStatChargesChr = "";
        rdoToChargeOnApplFreq_Manual = false;
        rdoToChargeOnApplFreq_System = false;
        rdoToChargeOnApplFreq_Both = false;
        rdoFlexiHappen_SB = false;
        rdoFlexiHappen_TD = false;
        rdoLinkFlexiAcct_Yes = false;
        rdoLinkFlexiAcct_No = false;
        txtMinBal1FlexiDep = "";
        txtMinBal2FlexiDep = "";
        rdoATMIssuedYes = false;
        rdoATMIssuedNo = false;
        rdoABBAllowed_Yes = false;
        rdoABBAllowed_No = false;
        rdoMobBankClient_Yes = false;
        rdoMobBankClient_No = false;
        rdoIVRSProvided_Yes = false;
        rdoIVRSProvided_No = false;
        rdoDebitCdIssued_Yes = false;
        rdoDebitCdIssued_No = false;
        rdoCreditCdIssued_Yes = false;
        rdoCreditCdIssued_No = false;
        txtMinATMBal = "";
        txtMinBalCreditCd = "";
        txtMinBalDebitCards = "";
        txtMinBalIVRS = "";
        txtMinMobBank = "";
        txtMinBalABB = "";
        txtIMPSLimit = "";
        txtInOpChrg = "";
        txtPrematureClosureChrg = "";
        txtAcctClosingChrg = "";
        txtMiscServChrg = "";
        txtStatChrg = "";
        txtFreeWDChrg = "";
        txtAcctDebitInt = "";
        txtAcctCrInt = "";
        txtChkBkIssueChrg = "";
        txtClearingIntAcctHd = "";
        txtStopPaymentChrg = "";
        txtOutwardChkRetChrg = "";
        txtInwardChkRetChrg = "";
        txtAcctOpenChrg = "";
        txtExcessFreeWDChrg = "";
        txtTaxGL = "";
        txtNonMainMinBalChrg = "";
        txtInOperative = "";
        txtFolioChrg = "";
        cboFlexiTD = "";
        
        txtNumPatternFollowedPrefix = "";
        txtNumPatternFollowedSuffix = "";
        txtLastAccNum ="";
        rdoAcc_Reg = false;
        rdoAcc_Nro = false;
        rdoAcc_Nre = false;
        
        
        // Addedd by nithya
        chkDebitWithdrawalCharge = "";
        txtDebitWithdrawalChargePeriod = "";
        cboDebitChargeType = "";
        txtDebitChargeTypeRate = "";     
        txtDebitWithdrawalChargeAcctHead = "";
        txtDebitWithdrawalChargeAcctHeadDesc = "";
        // End
        
        //setTxtNRF("");
        //setChanged();
//        tmlIntRate.setData(new ArrayList());
//        tmlIntRate.fireTableDataChanged();
//        operativeAcctIntTOs.clear();
        notifyObservers();
    }
    
    void setTxtProductID(String txtProductID){
        this.txtProductID = txtProductID;
        setChanged();
        
    }
    String getTxtProductID(){
        return this.txtProductID;
    }
    
    void setTxtDesc(String txtDesc){
        this.txtDesc = txtDesc;
        setChanged();
        
    }
    String getTxtDesc(){
        return this.txtDesc;
    }
    
    void setCboBehaves(String cboBehaves){
        this.cboBehaves = cboBehaves;
        setChanged();
        
    }
    String getCboBehaves(){
        return this.cboBehaves;
    }
    
    void setCboProdFreq(String cboProdFreq){
        this.cboProdFreq = cboProdFreq;
        setChanged();
        
    }
    String getCboProdFreq(){
        return this.cboProdFreq;
    }
    
    void setTxtMinBalChkbk(String txtMinBalChkbk){
        this.txtMinBalChkbk = txtMinBalChkbk;
        setChanged();
        
    }
    String getTxtMinBalChkbk(){
        return this.txtMinBalChkbk;
    }
    
    void setTxtMinBalwchk(String txtMinBalwchk){
        this.txtMinBalwchk = txtMinBalwchk;
        setChanged();
        
    }
    String getTxtMinBalwchk(){
        return this.txtMinBalwchk;
    }
    
    void setRdoChkAllowedYes(boolean rdoChkAllowedYes){
        this.rdoChkAllowedYes = rdoChkAllowedYes;
        setChanged();
        
    }
    boolean getRdoChkAllowedYes(){
        return this.rdoChkAllowedYes;
    }
    
    void setRdoChkAllowedNo(boolean rdoChkAllowedNo){
        this.rdoChkAllowedNo = rdoChkAllowedNo;
        setChanged();
        
    }
    boolean getRdoChkAllowedNo(){
        return this.rdoChkAllowedNo;
    }
    
    void setTxtNoNominees(String txtNoNominees){
        this.txtNoNominees = txtNoNominees;
        setChanged();
        
    }
    String getTxtNoNominees(){
        return this.txtNoNominees;
    }
    
    void setRdoAcctOpenApprYes(boolean rdoAcctOpenApprYes){
        this.rdoAcctOpenApprYes = rdoAcctOpenApprYes;
        setChanged();
        
    }
    boolean getRdoAcctOpenApprYes(){
        return this.rdoAcctOpenApprYes;
    }
    
    void setRdoAcctOpenApprNo(boolean rdoAcctOpenApprNo){
        this.rdoAcctOpenApprNo = rdoAcctOpenApprNo;
        setChanged();
        
    }
    boolean getRdoAcctOpenApprNo(){
        return this.rdoAcctOpenApprNo;
    }
    
    void setRdoNomineeReqYes(boolean rdoNomineeReqYes){
        this.rdoNomineeReqYes = rdoNomineeReqYes;
        setChanged();
        
    }
    boolean getRdoNomineeReqYes(){
        return this.rdoNomineeReqYes;
    }
    
    void setRdoNomineeReqNo(boolean rdoNomineeReqNo){
        this.rdoNomineeReqNo = rdoNomineeReqNo;
        setChanged();
        
    }
    boolean getRdoNomineeReqNo(){
        return this.rdoNomineeReqNo;
    }
    
    void setRdoIntroReqYes(boolean rdoIntroReqYes){
        this.rdoIntroReqYes = rdoIntroReqYes;
        setChanged();
        
    }
    boolean getRdoIntroReqYes(){
        return this.rdoIntroReqYes;
    }
    
    void setRdoIntroReqNo(boolean rdoIntroReqNo){
        this.rdoIntroReqNo = rdoIntroReqNo;
        setChanged();
        
    }
    boolean getRdoIntroReqNo(){
        return this.rdoIntroReqNo;
    }
    
    void setTxtMainTreatNewAcctClosure(String txtMainTreatNewAcctClosure){
        this.txtMainTreatNewAcctClosure = txtMainTreatNewAcctClosure;
        setChanged();
        
    }
    String getTxtMainTreatNewAcctClosure(){
        return this.txtMainTreatNewAcctClosure;
    }
    
    void setTxtMinTreatasDormant(String txtMinTreatasDormant){
        this.txtMinTreatasDormant = txtMinTreatasDormant;
        setChanged();
        
    }
    String getTxtMinTreatasDormant(){
        return this.txtMinTreatasDormant;
    }
    
    void setTxtMinTreatasNew(String txtMinTreatasNew){
        this.txtMinTreatasNew = txtMinTreatasNew;
        setChanged();
        
    }
    String getTxtMinTreatasNew(){
        return this.txtMinTreatasNew;
    }
    
    void setTxtMinTreatasInOp(String txtMinTreatasInOp){
        this.txtMinTreatasInOp = txtMinTreatasInOp;
        setChanged();
        
    }
    String getTxtMinTreatasInOp(){
        return this.txtMinTreatasInOp;
    }
    
    void setCboMinTreatasDormant(String cboMinTreatasDormant){
        this.cboMinTreatasDormant = cboMinTreatasDormant;
        setChanged();
        
    }
    String getCboMinTreatasDormant(){
        return this.cboMinTreatasDormant;
    }
    
    void setCboMinTreatInOp(String cboMinTreatInOp){
        this.cboMinTreatInOp = cboMinTreatInOp;
        setChanged();
        
    }
    String getCboMinTreatInOp(){
        return this.cboMinTreatInOp;
    }
    
    void setCboMinTreatasNew(String cboMinTreatasNew){
        this.cboMinTreatasNew = cboMinTreatasNew;
        setChanged();
        
    }
    String getCboMinTreatasNew(){
        return this.cboMinTreatasNew;
    }
    
    void setCboProdCurrency(String cboProdCurrency){
        this.cboProdCurrency = cboProdCurrency;
        setChanged();
        
    }
    String getCboProdCurrency(){
        return this.cboProdCurrency;
    }
    
    void setCboMinTreatNewClosure(String cboMinTreatNewClosure){
        this.cboMinTreatNewClosure = cboMinTreatNewClosure;
        setChanged();
        
    }
    String getCboMinTreatNewClosure(){
        return this.cboMinTreatNewClosure;
    }
    
    void setCboStatFreq(String cboStatFreq){
        this.cboStatFreq = cboStatFreq;
        setChanged();
        
    }
    String getCboStatFreq(){
        return this.cboStatFreq;
    }
    
    void setTxtAcctHd(String txtAcctHd){
        this.txtAcctHd = txtAcctHd;
        setChanged();
        
    }
    String getTxtAcctHd(){
        return this.txtAcctHd;
    }
    
    void setTxtNoFreeChkLeaves(String txtNoFreeChkLeaves){
        this.txtNoFreeChkLeaves = txtNoFreeChkLeaves;
        setChanged();
        
    }
    String getTxtNoFreeChkLeaves(){
        return this.txtNoFreeChkLeaves;
    }
    
    void setTxtRateTaxNRO(String txtRateTaxNRO){
        this.txtRateTaxNRO = txtRateTaxNRO;
        setChanged();
        
    }
    String getTxtRateTaxNRO(){
        return this.txtRateTaxNRO;
    }
    
    void setTxtMaxAmtWDSlip(String txtMaxAmtWDSlip){
        this.txtMaxAmtWDSlip = txtMaxAmtWDSlip;
        setChanged();
        
    }
    String getTxtMaxAmtWDSlip(){
        return this.txtMaxAmtWDSlip;
    }
    
    void setRdoAllowWDYes(boolean rdoAllowWDYes){
        this.rdoAllowWDYes = rdoAllowWDYes;
        setChanged();
        
    }
    boolean getRdoAllowWDYes(){
        return this.rdoAllowWDYes;
    }
    
    void setRdoAllowWDNo(boolean rdoAllowWDNo){
        this.rdoAllowWDNo = rdoAllowWDNo;
        setChanged();
        
    }
    boolean getRdoAllowWDNo(){
        return this.rdoAllowWDNo;
    }
    
    void setRdoIntClearing_Yes(boolean rdoIntClearing_Yes){
        this.rdoIntClearing_Yes = rdoIntClearing_Yes;
        setChanged();
        
    }
    boolean getRdoIntClearing_Yes(){
        return this.rdoIntClearing_Yes;
    }
    
    void setRdoIntClearing_No(boolean rdoIntClearing_No){
        this.rdoIntClearing_No = rdoIntClearing_No;
        setChanged();
        
    }
    boolean getRdoIntClearing_No(){
        return this.rdoIntClearing_No;
    }
    
    void setRdoStaffAcctYes(boolean rdoStaffAcctYes){
        this.rdoStaffAcctYes = rdoStaffAcctYes;
        setChanged();
        
    }
    boolean getRdoStaffAcctYes(){
        return this.rdoStaffAcctYes;
    }
    
    void setRdoStaffAcctNo(boolean rdoStaffAcctNo){
        this.rdoStaffAcctNo = rdoStaffAcctNo;
        setChanged();
        
    }
    boolean getRdoStaffAcctNo(){
        return this.rdoStaffAcctNo;
    }
    
    void setRdoCollectIntYes(boolean rdoCollectIntYes){
        this.rdoCollectIntYes = rdoCollectIntYes;
        setChanged();
        
    }
    boolean getRdoCollectIntYes(){
        return this.rdoCollectIntYes;
    }
    
    void setRdoCollectIntNo(boolean rdoCollectIntNo){
        this.rdoCollectIntNo = rdoCollectIntNo;
        setChanged();
        
    }
    boolean getRdoCollectIntNo(){
        return this.rdoCollectIntNo;
    }
    
    void setRdoLimitDefAllowYes(boolean rdoLimitDefAllowYes){
        this.rdoLimitDefAllowYes = rdoLimitDefAllowYes;
        setChanged();
        
    }
    boolean getRdoLimitDefAllowYes(){
        return this.rdoLimitDefAllowYes;
    }
    
    void setRdoLimitDefAllowNo(boolean rdoLimitDefAllowNo){
        this.rdoLimitDefAllowNo = rdoLimitDefAllowNo;
        setChanged();
        
    }
    boolean getRdoLimitDefAllowNo(){
        return this.rdoLimitDefAllowNo;
    }
    
    void setTxtNoFreeWD(String txtNoFreeWD){
        this.txtNoFreeWD = txtNoFreeWD;
        setChanged();
        
    }
    String getTxtNoFreeWD(){
        return this.txtNoFreeWD;
    }
    
    void setRdoIntUnClearBalYes(boolean rdoIntUnClearBalYes){
        this.rdoIntUnClearBalYes = rdoIntUnClearBalYes;
        setChanged();
        
    }
    boolean getRdoIntUnClearBalYes(){
        return this.rdoIntUnClearBalYes;
    }
    
    void setRdoIntUnClearBalNo(boolean rdoIntUnClearBalNo){
        this.rdoIntUnClearBalNo = rdoIntUnClearBalNo;
        setChanged();
        
    }
    boolean getRdoIntUnClearBalNo(){
        return this.rdoIntUnClearBalNo;
    }
    
    void setRdoTempODAllowYes(boolean rdoTempODAllowYes){
        this.rdoTempODAllowYes = rdoTempODAllowYes;
        setChanged();
        
    }
    boolean getRdoTempODAllowYes(){
        return this.rdoTempODAllowYes;
    }
    
    void setRdoTempODAllowNo(boolean rdoTempODAllowNo){
        this.rdoTempODAllowNo = rdoTempODAllowNo;
        setChanged();
        
    }
    boolean getRdoTempODAllowNo(){
        return this.rdoTempODAllowNo;
    }
    
    //    void setRdoExtraIntApplYes(boolean rdoExtraIntApplYes){
    //        this.rdoExtraIntApplYes = rdoExtraIntApplYes;
    //        setChanged();
    //
    //    }
    //    boolean getRdoExtraIntApplYes(){
    //        return this.rdoExtraIntApplYes;
    //    }
    //
    //    void setRdoExtraIntApplNo(boolean rdoExtraIntApplNo){
    //        this.rdoExtraIntApplNo = rdoExtraIntApplNo;
    //        setChanged();
    //
    //    }
    //    boolean getRdoExtraIntApplNo(){
    //        return this.rdoExtraIntApplNo;
    //    }
    
    void setRdoIssueTokenYes(boolean rdoIssueTokenYes){
        this.rdoIssueTokenYes = rdoIssueTokenYes;
        setChanged();
        
    }
    boolean getRdoIssueTokenYes(){
        return this.rdoIssueTokenYes;
    }
    
    void setRdoIssueTokenNo(boolean rdoIssueTokenNo){
        this.rdoIssueTokenNo = rdoIssueTokenNo;
        setChanged();
        
    }
    boolean getRdoIssueTokenNo(){
        return this.rdoIssueTokenNo;
    }
    
    void setRdoTaxIntApplNROYes(boolean rdoTaxIntApplNROYes){
        this.rdoTaxIntApplNROYes = rdoTaxIntApplNROYes;
        setChanged();
        
    }
    boolean getRdoTaxIntApplNROYes(){
        return this.rdoTaxIntApplNROYes;
    }
    
    void setRdoTaxIntApplNRONo(boolean rdoTaxIntApplNRONo){
        this.rdoTaxIntApplNRONo = rdoTaxIntApplNRONo;
        setChanged();
        
    }
    boolean getRdoTaxIntApplNRONo(){
        return this.rdoTaxIntApplNRONo;
    }
    
    void setTdtFreeWDStFrom(String tdtFreeWDStFrom){
        this.tdtFreeWDStFrom = tdtFreeWDStFrom;
        setChanged();
        
    }
    String getTdtFreeWDStFrom(){
        return this.tdtFreeWDStFrom;
    }
    
    void setTdtFreeChkLeaveStFrom(String tdtFreeChkLeaveStFrom){
        this.tdtFreeChkLeaveStFrom = tdtFreeChkLeaveStFrom;
        setChanged();
        
    }
    String getTdtFreeChkLeaveStFrom(){
        return this.tdtFreeChkLeaveStFrom;
    }
    
    void setTxtFreeWDPd(String txtFreeWDPd){
        this.txtFreeWDPd = txtFreeWDPd;
        setChanged();
        
    }
    String getTxtFreeWDPd(){
        return this.txtFreeWDPd;
    }
    
    void setCboFreeWDPd(String cboFreeWDPd){
        this.cboFreeWDPd = cboFreeWDPd;
        setChanged();
        
    }
    String getCboFreeWDPd(){
        return this.cboFreeWDPd;
    }
    
    void setTxtFreeChkPD(String txtFreeChkPD){
        this.txtFreeChkPD = txtFreeChkPD;
        setChanged();
        
    }
    String getTxtFreeChkPD(){
        return this.txtFreeChkPD;
    }
    
    void setCboFreeChkPd(String cboFreeChkPd){
        this.cboFreeChkPd = cboFreeChkPd;
        setChanged();
        
    }
    String getCboFreeChkPd(){
        return this.cboFreeChkPd;
    }
    
    void setRdoDebitCompReqYes(boolean rdoDebitCompReqYes){
        this.rdoDebitCompReqYes = rdoDebitCompReqYes;
        setChanged();
        
    }
    boolean getRdoDebitCompReqYes(){
        return this.rdoDebitCompReqYes;
    }
    
    void setRdoDebitCompReqNo(boolean rdoDebitCompReqNo){
        this.rdoDebitCompReqNo = rdoDebitCompReqNo;
        setChanged();
        
    }
    boolean getRdoDebitCompReqNo(){
        return this.rdoDebitCompReqNo;
    }
    
    void setTxtMinDebitIntRate(String txtMinDebitIntRate){
        this.txtMinDebitIntRate = txtMinDebitIntRate;
        setChanged();
        
    }
    String getTxtMinDebitIntRate(){
        return this.txtMinDebitIntRate;
    }
    
    void setTxtMaxDebitIntRate(String txtMaxDebitIntRate){
        this.txtMaxDebitIntRate = txtMaxDebitIntRate;
        setChanged();
        
    }
    String getTxtMaxDebitIntRate(){
        return this.txtMaxDebitIntRate;
    }
    
    void setTxtApplDebitIntRate(String txtApplDebitIntRate){
        this.txtApplDebitIntRate = txtApplDebitIntRate;
        setChanged();
        
    }
    String getTxtApplDebitIntRate(){
        return this.txtApplDebitIntRate;
    }
    
    void setTxtMinDebitIntAmt(String txtMinDebitIntAmt){
        this.txtMinDebitIntAmt = txtMinDebitIntAmt;
        setChanged();
        
    }
    String getTxtMinDebitIntAmt(){
        return this.txtMinDebitIntAmt;
    }
    
    void setTxtMaxDebitIntAmt(String txtMaxDebitIntAmt){
        this.txtMaxDebitIntAmt = txtMaxDebitIntAmt;
        setChanged();
        
    }
    String getTxtMaxDebitIntAmt(){
        return this.txtMaxDebitIntAmt;
    }
    
    void setCboDebitIntCalcFreq(String cboDebitIntCalcFreq){
        this.cboDebitIntCalcFreq = cboDebitIntCalcFreq;
        setChanged();
        
    }
    String getCboDebitIntCalcFreq(){
        return this.cboDebitIntCalcFreq;
    }
    
    void setCboDebitIntApplFreq(String cboDebitIntApplFreq){
        this.cboDebitIntApplFreq = cboDebitIntApplFreq;
        setChanged();
        
    }
    String getCboDebitIntApplFreq(){
        return this.cboDebitIntApplFreq;
    }
    
    void setCboDebitIntRoundOff(String cboDebitIntRoundOff){
        this.cboDebitIntRoundOff = cboDebitIntRoundOff;
        setChanged();
        
    }
    String getCboDebitIntRoundOff(){
        return this.cboDebitIntRoundOff;
    }
    
    void setCboDebitCompIntCalcFreq(String cboDebitCompIntCalcFreq){
        this.cboDebitCompIntCalcFreq = cboDebitCompIntCalcFreq;
        setChanged();
        
    }
    String getCboDebitCompIntCalcFreq(){
        return this.cboDebitCompIntCalcFreq;
    }
    
    void setCboDebitProductRoundOff(String cboDebitProductRoundOff){
        this.cboDebitProductRoundOff = cboDebitProductRoundOff;
        setChanged();
        
    }
    String getCboDebitProductRoundOff(){
        return this.cboDebitProductRoundOff;
    }
    
    void setTdtLastIntCalcDate(String tdtLastIntCalcDate){
        this.tdtLastIntCalcDate = tdtLastIntCalcDate;
        setChanged();
        
    }
    String getTdtLastIntCalcDate(){
        return this.tdtLastIntCalcDate;
    }
    
    void setTdtLastIntApplDate(String tdtLastIntApplDate){
        this.tdtLastIntApplDate = tdtLastIntApplDate;
        setChanged();
        
    }
    String getTdtLastIntApplDate(){
        return this.tdtLastIntApplDate;
    }
    
    void setRdoDebitIntChrgYes2(boolean rdoDebitIntChrgYes2){
        this.rdoDebitIntChrgYes2 = rdoDebitIntChrgYes2;
        setChanged();
        
    }
    boolean getRdoDebitIntChrgYes2(){
        return this.rdoDebitIntChrgYes2;
    }
    
    void setRdoDebitIntChrgNo2(boolean rdoDebitIntChrgNo2){
        this.rdoDebitIntChrgNo2 = rdoDebitIntChrgNo2;
        setChanged();
        
    }
    boolean getRdoDebitIntChrgNo2(){
        return this.rdoDebitIntChrgNo2;
    }
    
    void setTxtPenalIntDebitBalAcct(String txtPenalIntDebitBalAcct){
        this.txtPenalIntDebitBalAcct = txtPenalIntDebitBalAcct;
        setChanged();
        
    }
    String getTxtPenalIntDebitBalAcct(){
        return this.txtPenalIntDebitBalAcct;
    }
    
    void setTxtPenalIntChrgStart(String txtPenalIntChrgStart){
        this.txtPenalIntChrgStart = txtPenalIntChrgStart;
        setChanged();
        
    }
    String getTxtPenalIntChrgStart(){
        return this.txtPenalIntChrgStart;
    }
    
    void setTxtMinCrIntRate(String txtMinCrIntRate){
        this.txtMinCrIntRate = txtMinCrIntRate;
        setChanged();
        
    }
    String getTxtMinCrIntRate(){
        return this.txtMinCrIntRate;
    }
    
    void setTxtMaxCrIntRate(String txtMaxCrIntRate){
        this.txtMaxCrIntRate = txtMaxCrIntRate;
        setChanged();
        
    }
    String getTxtMaxCrIntRate(){
        return this.txtMaxCrIntRate;
    }
    
    void setTxtApplCrIntRate(String txtApplCrIntRate){
        this.txtApplCrIntRate = txtApplCrIntRate;
        setChanged();
        
    }
    String getTxtApplCrIntRate(){
        return this.txtApplCrIntRate;
    }
    
    void setTxtMinCrIntAmt(String txtMinCrIntAmt){
        this.txtMinCrIntAmt = txtMinCrIntAmt;
        setChanged();
        
    }
    String getTxtMinCrIntAmt(){
        return this.txtMinCrIntAmt;
    }
    
    void setTxtMaxCrIntAmt(String txtMaxCrIntAmt){
        this.txtMaxCrIntAmt = txtMaxCrIntAmt;
        setChanged();
        
    }
    String getTxtMaxCrIntAmt(){
        return this.txtMaxCrIntAmt;
    }
    
    void setTxtStDayProdCalcSBCrInt(String txtStDayProdCalcSBCrInt){
        this.txtStDayProdCalcSBCrInt = txtStDayProdCalcSBCrInt;
        setChanged();
        
    }
    String getTxtStDayProdCalcSBCrInt(){
        return this.txtStDayProdCalcSBCrInt;
    }
    
    void setTxtEndDayProdCalcSBCrInt(String txtEndDayProdCalcSBCrInt){
        this.txtEndDayProdCalcSBCrInt = txtEndDayProdCalcSBCrInt;
        setChanged();
        
    }
    String getTxtEndDayProdCalcSBCrInt(){
        return this.txtEndDayProdCalcSBCrInt;
    }
    
    void setCboCrIntCalcFreq(String cboCrIntCalcFreq){
        this.cboCrIntCalcFreq = cboCrIntCalcFreq;
        setChanged();
        
    }
    String getCboCrIntCalcFreq(){
        return this.cboCrIntCalcFreq;
    }
    
    void setCboCrIntApplFreq(String cboCrIntApplFreq){
        this.cboCrIntApplFreq = cboCrIntApplFreq;
        setChanged();
        
    }
    String getCboCrIntApplFreq(){
        return this.cboCrIntApplFreq;
    }
    
    void setCboStMonIntCalc(String cboStMonIntCalc){
        this.cboStMonIntCalc = cboStMonIntCalc;
        setChanged();
        
    }
    String getCboStMonIntCalc(){
        return this.cboStMonIntCalc;
    }
    
    void setCboIntCalcEndMon(String cboIntCalcEndMon){
        this.cboIntCalcEndMon = cboIntCalcEndMon;
        setChanged();
        
    }
    String getCboIntCalcEndMon(){
        return this.cboIntCalcEndMon;
    }
    
    void setTdtLastIntCalcDateCR(String tdtLastIntCalcDateCR){
        this.tdtLastIntCalcDateCR = tdtLastIntCalcDateCR;
        setChanged();
        
    }
    String getTdtLastIntCalcDateCR(){
        return this.tdtLastIntCalcDateCR;
    }
    
    void setTdtLastIntApplDateCr(String tdtLastIntApplDateCr){
        this.tdtLastIntApplDateCr = tdtLastIntApplDateCr;
        setChanged();
        
    }
    String getTdtLastIntApplDateCr(){
        return this.tdtLastIntApplDateCr;
    }
    
    void setRdoCreditCompYes(boolean rdoCreditCompYes){
        this.rdoCreditCompYes = rdoCreditCompYes;
        setChanged();
        
    }
    boolean getRdoCreditCompYes(){
        return this.rdoCreditCompYes;
    }
    
    void setRdoCreditCompNo(boolean rdoCreditCompNo){
        this.rdoCreditCompNo = rdoCreditCompNo;
        setChanged();
        
    }
    boolean getRdoCreditCompNo(){
        return this.rdoCreditCompNo;
    }
    
    void setCboCreditCompIntCalcFreq(String cboCreditCompIntCalcFreq){
        this.cboCreditCompIntCalcFreq = cboCreditCompIntCalcFreq;
        setChanged();
        
    }
    String getCboCreditCompIntCalcFreq(){
        return this.cboCreditCompIntCalcFreq;
    }
    
    void setCboCreditProductRoundOff(String cboCreditProductRoundOff){
        this.cboCreditProductRoundOff = cboCreditProductRoundOff;
        setChanged();
        
    }
    String getCboCreditProductRoundOff(){
        return this.cboCreditProductRoundOff;
    }
    
    void setCboCreditIntRoundOff(String cboCreditIntRoundOff){
        this.cboCreditIntRoundOff = cboCreditIntRoundOff;
        setChanged();
        
    }
    String getCboCreditIntRoundOff(){
        return this.cboCreditIntRoundOff;
    }
    
    void setCboCalcCriteria(String cboCalcCriteria){
        this.cboCalcCriteria = cboCalcCriteria;
        setChanged();
        
    }
    String getCboCalcCriteria(){
        return this.cboCalcCriteria;
    }
    
    void setCboProdFreqCr(String cboProdFreqCr){
        this.cboProdFreqCr = cboProdFreqCr;
        setChanged();
        
    }
    String getCboProdFreqCr(){
        return this.cboProdFreqCr;
    }
    
    void setRdoCrIntGivenYes(boolean rdoCrIntGivenYes){
        this.rdoCrIntGivenYes = rdoCrIntGivenYes;
        setChanged();
        
    }
    boolean getRdoCrIntGivenYes(){
        return this.rdoCrIntGivenYes;
    }
    
    void setRdoCrIntGivenNo(boolean rdoCrIntGivenNo){
        this.rdoCrIntGivenNo = rdoCrIntGivenNo;
        setChanged();
        
    }
    boolean getRdoCrIntGivenNo(){
        return this.rdoCrIntGivenNo;
    }
    
    //    void setTxtAddIntStaff(String txtAddIntStaff){
    //        this.txtAddIntStaff = txtAddIntStaff;
    //        setChanged();
    //
    //    }
    //    String getTxtAddIntStaff(){
    //        return this.txtAddIntStaff;
    //    }
    
    void setCboInOpAcChrgPd(String cboInOpAcChrgPd){
        this.cboInOpAcChrgPd = cboInOpAcChrgPd;
        setChanged();
        
    }
    String getCboInOpAcChrgPd(){
        return this.cboInOpAcChrgPd;
    }
    
    void setTxtlInOpAcChrg(String txtlInOpAcChrg){
        this.txtlInOpAcChrg = txtlInOpAcChrg;
        setChanged();
        
    }
    String getTxtlInOpAcChrg(){
        return this.txtlInOpAcChrg;
    }
    
    void setTxtChrgPreClosure(String txtChrgPreClosure){
        this.txtChrgPreClosure = txtChrgPreClosure;
        setChanged();
        
    }
    String getTxtChrgPreClosure(){
        return this.txtChrgPreClosure;
    }
    
    void setTxtAcClosingChrg(String txtAcClosingChrg){
        this.txtAcClosingChrg = txtAcClosingChrg;
        setChanged();
        
    }
    String getTxtAcClosingChrg(){
        return this.txtAcClosingChrg;
    }
    
    void setTxtChrgMiscServChrg(String txtChrgMiscServChrg){
        this.txtChrgMiscServChrg = txtChrgMiscServChrg;
        setChanged();
        
    }
    String getTxtChrgMiscServChrg(){
        return this.txtChrgMiscServChrg;
    }
    
    void setRdoNonMainMinBalChrg_Yes(boolean rdoNonMainMinBalChrg_Yes){
        this.rdoNonMainMinBalChrg_Yes = rdoNonMainMinBalChrg_Yes;
        setChanged();
        
    }
    boolean getRdoNonMainMinBalChrg_Yes(){
        return this.rdoNonMainMinBalChrg_Yes;
    }
    
    void setRdoNonMainMinBalChrg_No(boolean rdoNonMainMinBalChrg_No){
        this.rdoNonMainMinBalChrg_No = rdoNonMainMinBalChrg_No;
        setChanged();
        
    }
    boolean getRdoNonMainMinBalChrg_No(){
        return this.rdoNonMainMinBalChrg_No;
    }
    
    void setRdoChkIssuedChrgCh_Yes(boolean rdoChkIssuedChrgCh_Yes){
        this.rdoChkIssuedChrgCh_Yes = rdoChkIssuedChrgCh_Yes;
        setChanged();
        
    }
    boolean getRdoChkIssuedChrgCh_Yes(){
        return this.rdoChkIssuedChrgCh_Yes;
    }
    
    void setRdoChkIssuedChrgCh_No(boolean rdoChkIssuedChrgCh_No){
        this.rdoChkIssuedChrgCh_No = rdoChkIssuedChrgCh_No;
        setChanged();
        
    }
    boolean getRdoChkIssuedChrgCh_No(){
        return this.rdoChkIssuedChrgCh_No;
    }
    
    void setRdoFolioChargeAppl_Yes(boolean rdoFolioChargeAppl_Yes){
        this.rdoFolioChargeAppl_Yes = rdoFolioChargeAppl_Yes;
        setChanged();
        
    }
    boolean getRdoFolioChargeAppl_Yes(){
        return this.rdoFolioChargeAppl_Yes;
    }
    
    void setRdoFolioChargeAppl_No(boolean rdoFolioChargeAppl_No){
        this.rdoFolioChargeAppl_No = rdoFolioChargeAppl_No;
        setChanged();
        
    }
    boolean getRdoFolioChargeAppl_No(){
        return this.rdoFolioChargeAppl_No;
    }
    
    void setRdoFolioToChargeOn_Credit(boolean rdoFolioToChargeOn_Credit){
        this.rdoFolioToChargeOn_Credit = rdoFolioToChargeOn_Credit;
        setChanged();
        
    }
    boolean getRdoFolioToChargeOn_Credit(){
        return this.rdoFolioToChargeOn_Credit;
    }
    
    void setRdoFolioToChargeOn_Debit(boolean rdoFolioToChargeOn_Debit){
        this.rdoFolioToChargeOn_Debit = rdoFolioToChargeOn_Debit;
        setChanged();
        
    }
    boolean getRdoFolioToChargeOn_Debit(){
        return this.rdoFolioToChargeOn_Debit;
    }
    
    void setRdoFolioToChargeOn_Both(boolean rdoFolioToChargeOn_Both){
        this.rdoFolioToChargeOn_Both = rdoFolioToChargeOn_Both;
        setChanged();
        
    }
    boolean getRdoFolioToChargeOn_Both(){
        return this.rdoFolioToChargeOn_Both;
    }
    
    void setRdoStopPaymentChrg_Yes(boolean rdoStopPaymentChrg_Yes){
        this.rdoStopPaymentChrg_Yes = rdoStopPaymentChrg_Yes;
        setChanged();
        
    }
    boolean getRdoStopPaymentChrg_Yes(){
        return this.rdoStopPaymentChrg_Yes;
    }
    
    void setRdoStopPaymentChrg_No(boolean rdoStopPaymentChrg_No){
        this.rdoStopPaymentChrg_No = rdoStopPaymentChrg_No;
        setChanged();
        
    }
    boolean getRdoStopPaymentChrg_No(){
        return this.rdoStopPaymentChrg_No;
    }
    
    void setRdoStatCharges_Yes(boolean rdoStatCharges_Yes){
        this.rdoStatCharges_Yes = rdoStatCharges_Yes;
        setChanged();
        
    }
    boolean getRdoStatCharges_Yes(){
        return this.rdoStatCharges_Yes;
    }
    
    void setRdoStatCharges_No(boolean rdoStatCharges_No){
        this.rdoStatCharges_No = rdoStatCharges_No;
        setChanged();
        
    }
    boolean getRdoStatCharges_No(){
        return this.rdoStatCharges_No;
    }
    
    void setTxtChkBkIssueChrgPL(String txtChkBkIssueChrgPL){
        this.txtChkBkIssueChrgPL = txtChkBkIssueChrgPL;
        setChanged();
        
    }
    String getTxtChkBkIssueChrgPL(){
        return this.txtChkBkIssueChrgPL;
    }
    
    void setTxtStopPayChrg(String txtStopPayChrg){
        this.txtStopPayChrg = txtStopPayChrg;
        setChanged();
        
    }
    String getTxtStopPayChrg(){
        return this.txtStopPayChrg;
    }
    
    void setTxtChkRetChrOutward(String txtChkRetChrOutward){
        this.txtChkRetChrOutward = txtChkRetChrOutward;
        setChanged();
        
    }
    String getTxtChkRetChrOutward(){
        return this.txtChkRetChrOutward;
    }
    
    void setTxtChkRetChrgIn(String txtChkRetChrgIn){
        this.txtChkRetChrgIn = txtChkRetChrgIn;
        setChanged();
        
    }
    String getTxtChkRetChrgIn(){
        return this.txtChkRetChrgIn;
    }
    
    void setTxtAcctOpenCharges(String txtAcctOpenCharges){
        this.txtAcctOpenCharges = txtAcctOpenCharges;
        setChanged();
        
    }
    String getTxtAcctOpenCharges(){
        return this.txtAcctOpenCharges;
    }
    
    void setTxtNoEntryPerFolio(String txtNoEntryPerFolio){
        this.txtNoEntryPerFolio = txtNoEntryPerFolio;
        setChanged();
        
    }
    String getTxtNoEntryPerFolio(){
        return this.txtNoEntryPerFolio;
    }
    
    void setTxtRatePerFolio(String txtRatePerFolio){
        this.txtRatePerFolio = txtRatePerFolio;
        setChanged();
        
    }
    String getTxtRatePerFolio(){
        return this.txtRatePerFolio;
    }
    
    void setTxtExcessFreeWDChrgPT(String txtExcessFreeWDChrgPT){
        this.txtExcessFreeWDChrgPT = txtExcessFreeWDChrgPT;
        setChanged();
        
    }
    String getTxtExcessFreeWDChrgPT(){
        return this.txtExcessFreeWDChrgPT;
    }
    
    void setCboFolioChrgApplFreq(String cboFolioChrgApplFreq){
        this.cboFolioChrgApplFreq = cboFolioChrgApplFreq;
        setChanged();
        
    }
    String getCboFolioChrgApplFreq(){
        return this.cboFolioChrgApplFreq;
    }
    
    void setCboToCollectFolioChrg(String cboToCollectFolioChrg){
        this.cboToCollectFolioChrg = cboToCollectFolioChrg;
        setChanged();
        
    }

    String getCboToCollectFolioChrg(){
        return this.cboToCollectFolioChrg;
    }
    
    void setCboIncompFolioROffFreq(String cboIncompFolioROffFreq){
        this.cboIncompFolioROffFreq = cboIncompFolioROffFreq;
        setChanged();
        
    }
    String getCboIncompFolioROffFreq(){
        return this.cboIncompFolioROffFreq;
    }
    
    void setTxtMinBalAmt(String txtMinBalAmt){
        this.txtMinBalAmt = txtMinBalAmt;
        setChanged();
        
    }
    String getTxtMinBalAmt(){
        return this.txtMinBalAmt;
    }
    
    void setCboMinBalAmt(String cboMinBalAmt){
        this.cboMinBalAmt = cboMinBalAmt;
        setChanged();
        
    }
    String getCboMinBalAmt(){
        return this.cboMinBalAmt;
    }
    
    void setTxtStatChargesChr(String txtStatChargesChr){
        this.txtStatChargesChr = txtStatChargesChr;
        setChanged();
        
    }
    String getTxtStatChargesChr(){
        return this.txtStatChargesChr;
    }
    
    void setCboStatChargesChr(String cboStatChargesChr){
        this.cboStatChargesChr = cboStatChargesChr;
        setChanged();
        
    }
    String getCboStatChargesChr(){
        return this.cboStatChargesChr;
    }
    
    void setRdoToChargeOnApplFreq_Manual(boolean rdoToChargeOnApplFreq_Manual){
        this.rdoToChargeOnApplFreq_Manual = rdoToChargeOnApplFreq_Manual;
        setChanged();
    }
    boolean getRdoToChargeOnApplFreq_Manual(){
        return this.rdoToChargeOnApplFreq_Manual;
    }
    
    void setRdoToChargeOnApplFreq_System(boolean rdoToChargeOnApplFreq_System){
        this.rdoToChargeOnApplFreq_System = rdoToChargeOnApplFreq_System;
        setChanged();
        
    }
    boolean getRdoToChargeOnApplFreq_System(){
        return this.rdoToChargeOnApplFreq_System;
    }
    
    void setRdoToChargeOnApplFreq_Both(boolean rdoToChargeOnApplFreq_Both){
        this.rdoToChargeOnApplFreq_Both = rdoToChargeOnApplFreq_Both;
        setChanged();
        
    }
    boolean getRdoToChargeOnApplFreq_Both(){
        return this.rdoToChargeOnApplFreq_Both;
    }
    
    void setRdoFlexiHappen_SB(boolean rdoFlexiHappen_SB){
        this.rdoFlexiHappen_SB = rdoFlexiHappen_SB;
        setChanged();
        
    }
    boolean getRdoFlexiHappen_SB(){
        return this.rdoFlexiHappen_SB;
    }
    
    void setRdoFlexiHappen_TD(boolean rdoFlexiHappen_TD){
        this.rdoFlexiHappen_TD = rdoFlexiHappen_TD;
        setChanged();
        
    }
    boolean getRdoFlexiHappen_TD(){
        return this.rdoFlexiHappen_TD;
    }
    
    void setRdoLinkFlexiAcct_Yes(boolean rdoLinkFlexiAcct_Yes){
        this.rdoLinkFlexiAcct_Yes = rdoLinkFlexiAcct_Yes;
        setChanged();
        
    }
    boolean getRdoLinkFlexiAcct_Yes(){
        return this.rdoLinkFlexiAcct_Yes;
    }
    
    void setRdoLinkFlexiAcct_No(boolean rdoLinkFlexiAcct_No){
        this.rdoLinkFlexiAcct_No = rdoLinkFlexiAcct_No;
        setChanged();
        
    }
    boolean getRdoLinkFlexiAcct_No(){
        return this.rdoLinkFlexiAcct_No;
    }
    
    void setTxtMinBal1FlexiDep(String txtMinBal1FlexiDep){
        this.txtMinBal1FlexiDep = txtMinBal1FlexiDep;
        setChanged();
        
    }
    String getTxtMinBal1FlexiDep(){
        return this.txtMinBal1FlexiDep;
    }
    
    void setTxtMinBal2FlexiDep(String txtMinBal2FlexiDep){
        this.txtMinBal2FlexiDep = txtMinBal2FlexiDep;
        setChanged();
        
    }
    String getTxtMinBal2FlexiDep(){
        return this.txtMinBal2FlexiDep;
    }
    
    void setRdoATMIssuedYes(boolean rdoATMIssuedYes){
        this.rdoATMIssuedYes = rdoATMIssuedYes;
        setChanged();
        
    }
    boolean getRdoATMIssuedYes(){
        return this.rdoATMIssuedYes;
    }
    
    void setRdoATMIssuedNo(boolean rdoATMIssuedNo){
        this.rdoATMIssuedNo = rdoATMIssuedNo;
        setChanged();
        
    }
    boolean getRdoATMIssuedNo(){
        return this.rdoATMIssuedNo;
    }
    
    void setRdoABBAllowed_Yes(boolean rdoABBAllowed_Yes){
        this.rdoABBAllowed_Yes = rdoABBAllowed_Yes;
        setChanged();
        
    }
    boolean getRdoABBAllowed_Yes(){
        return this.rdoABBAllowed_Yes;
    }
    
    void setRdoABBAllowed_No(boolean rdoABBAllowed_No){
        this.rdoABBAllowed_No = rdoABBAllowed_No;
        setChanged();
        
    }
    boolean getRdoABBAllowed_No(){
        return this.rdoABBAllowed_No;
    }
    
    void setRdoMobBankClient_Yes(boolean rdoMobBankClient_Yes){
        this.rdoMobBankClient_Yes = rdoMobBankClient_Yes;
        setChanged();
        
    }
    boolean getRdoMobBankClient_Yes(){
        return this.rdoMobBankClient_Yes;
    }
    
    void setRdoMobBankClient_No(boolean rdoMobBankClient_No){
        this.rdoMobBankClient_No = rdoMobBankClient_No;
        setChanged();
        
    }
    boolean getRdoMobBankClient_No(){
        return this.rdoMobBankClient_No;
    }
    
    void setRdoIVRSProvided_Yes(boolean rdoIVRSProvided_Yes){
        this.rdoIVRSProvided_Yes = rdoIVRSProvided_Yes;
        setChanged();
        
    }
    boolean getRdoIVRSProvided_Yes(){
        return this.rdoIVRSProvided_Yes;
    }
    
    void setRdoIVRSProvided_No(boolean rdoIVRSProvided_No){
        this.rdoIVRSProvided_No = rdoIVRSProvided_No;
        setChanged();
        
    }
    boolean getRdoIVRSProvided_No(){
        return this.rdoIVRSProvided_No;
    }
    
    void setRdoDebitCdIssued_Yes(boolean rdoDebitCdIssued_Yes){
        this.rdoDebitCdIssued_Yes = rdoDebitCdIssued_Yes;
        setChanged();
        
    }
    boolean getRdoDebitCdIssued_Yes(){
        return this.rdoDebitCdIssued_Yes;
    }
    
    void setRdoDebitCdIssued_No(boolean rdoDebitCdIssued_No){
        this.rdoDebitCdIssued_No = rdoDebitCdIssued_No;
        setChanged();
        
    }
    boolean getRdoDebitCdIssued_No(){
        return this.rdoDebitCdIssued_No;
    }
    
    void setRdoCreditCdIssued_Yes(boolean rdoCreditCdIssued_Yes){
        this.rdoCreditCdIssued_Yes = rdoCreditCdIssued_Yes;
        setChanged();
        
    }
    boolean getRdoCreditCdIssued_Yes(){
        return this.rdoCreditCdIssued_Yes;
    }
    
    void setRdoCreditCdIssued_No(boolean rdoCreditCdIssued_No){
        this.rdoCreditCdIssued_No = rdoCreditCdIssued_No;
        setChanged();
        
    }
    boolean getRdoCreditCdIssued_No(){
        return this.rdoCreditCdIssued_No;
    }
    
    void setTxtMinATMBal(String txtMinATMBal){
        this.txtMinATMBal = txtMinATMBal;
        setChanged();
        
    }
    String getTxtMinATMBal(){
        return this.txtMinATMBal;
    }
    
    void setTxtMinBalCreditCd(String txtMinBalCreditCd){
        this.txtMinBalCreditCd = txtMinBalCreditCd;
        setChanged();
        
    }
    String getTxtMinBalCreditCd(){
        return this.txtMinBalCreditCd;
    }
    
    void setTxtMinBalDebitCards(String txtMinBalDebitCards){
        this.txtMinBalDebitCards = txtMinBalDebitCards;
        setChanged();
        
    }
    String getTxtMinBalDebitCards(){
        return this.txtMinBalDebitCards;
    }
    
    void setTxtMinBalIVRS(String txtMinBalIVRS){
        this.txtMinBalIVRS = txtMinBalIVRS;
        setChanged();
        
    }
    String getTxtMinBalIVRS(){
        return this.txtMinBalIVRS;
    }
    
    void setTxtMinMobBank(String txtMinMobBank){
        this.txtMinMobBank = txtMinMobBank;
        setChanged();
        
    }
    String getTxtMinMobBank(){
        return this.txtMinMobBank;
    }
    
    void setTxtMinBalABB(String txtMinBalABB){
        this.txtMinBalABB = txtMinBalABB;
        setChanged();
        
    }
    String getTxtMinBalABB(){
        return this.txtMinBalABB;
    }

    public String getTxtIMPSLimit() {
        return txtIMPSLimit;
    }

    public void setTxtIMPSLimit(String txtIMPSLimit) {
        this.txtIMPSLimit = txtIMPSLimit;
    }
    
    
    
    void setTxtInOpChrg(String txtInOpChrg){
        this.txtInOpChrg = txtInOpChrg;
        setChanged();
        
    }
    String getTxtInOpChrg(){
        return this.txtInOpChrg;
    }
    
    void setTxtPrematureClosureChrg(String txtPrematureClosureChrg){
        this.txtPrematureClosureChrg = txtPrematureClosureChrg;
        setChanged();
        
    }
    String getTxtPrematureClosureChrg(){
        return this.txtPrematureClosureChrg;
    }
    
    void setTxtAcctClosingChrg(String txtAcctClosingChrg){
        this.txtAcctClosingChrg = txtAcctClosingChrg;
        setChanged();
        
    }
    String getTxtAcctClosingChrg(){
        return this.txtAcctClosingChrg;
    }
    
    void setTxtMiscServChrg(String txtMiscServChrg){
        this.txtMiscServChrg = txtMiscServChrg;
        setChanged();
        
    }
    String getTxtMiscServChrg(){
        return this.txtMiscServChrg;
    }
    
    void setTxtStatChrg(String txtStatChrg){
        this.txtStatChrg = txtStatChrg;
        setChanged();
        
    }
    String getTxtStatChrg(){
        return this.txtStatChrg;
    }
    
    void setTxtFreeWDChrg(String txtFreeWDChrg){
        this.txtFreeWDChrg = txtFreeWDChrg;
        setChanged();
        
    }
    String getTxtFreeWDChrg(){
        return this.txtFreeWDChrg;
    }
    
    void setTxtAcctDebitInt(String txtAcctDebitInt){
        this.txtAcctDebitInt = txtAcctDebitInt;
        setChanged();
        
    }
    String getTxtAcctDebitInt(){
        return this.txtAcctDebitInt;
    }
    
    void setTxtAcctCrInt(String txtAcctCrInt){
        this.txtAcctCrInt = txtAcctCrInt;
        setChanged();
        
    }
    String getTxtAcctCrInt(){
        return this.txtAcctCrInt;
    }
    
    void setTxtChkBkIssueChrg(String txtChkBkIssueChrg){
        this.txtChkBkIssueChrg = txtChkBkIssueChrg;
        setChanged();
        
    }
    String getTxtChkBkIssueChrg(){
        return this.txtChkBkIssueChrg;
    }
    
    void setTxtClearingIntAcctHd(String txtClearingIntAcctHd){
        this.txtClearingIntAcctHd = txtClearingIntAcctHd;
        setChanged();
        
    }
    String getTxtClearingIntAcctHd(){
        return this.txtClearingIntAcctHd;
    }
    
    void setTxtStopPaymentChrg(String txtStopPaymentChrg){
        this.txtStopPaymentChrg = txtStopPaymentChrg;
        setChanged();
        
    }
    String getTxtStopPaymentChrg(){
        return this.txtStopPaymentChrg;
    }
    
    void setTxtOutwardChkRetChrg(String txtOutwardChkRetChrg){
        this.txtOutwardChkRetChrg = txtOutwardChkRetChrg;
        setChanged();
        
    }
    String getTxtOutwardChkRetChrg(){
        return this.txtOutwardChkRetChrg;
    }
    
    void setTxtInwardChkRetChrg(String txtInwardChkRetChrg){
        this.txtInwardChkRetChrg = txtInwardChkRetChrg;
        setChanged();
        
    }
    String getTxtInwardChkRetChrg(){
        return this.txtInwardChkRetChrg;
    }
    
    void setTxtAcctOpenChrg(String txtAcctOpenChrg){
        this.txtAcctOpenChrg = txtAcctOpenChrg;
        setChanged();
        
    }
    String getTxtAcctOpenChrg(){
        return this.txtAcctOpenChrg;
    }
    
    void setTxtExcessFreeWDChrg(String txtExcessFreeWDChrg){
        this.txtExcessFreeWDChrg = txtExcessFreeWDChrg;
        setChanged();
        
    }
    String getTxtExcessFreeWDChrg(){
        return this.txtExcessFreeWDChrg;
    }
    
    void setTxtTaxGL(String txtTaxGL){
        this.txtTaxGL = txtTaxGL;
        setChanged();
        
    }
    String getTxtTaxGL(){
        return this.txtTaxGL;
    }
    
    void setTxtNonMainMinBalChrg(String txtNonMainMinBalChrg){
        this.txtNonMainMinBalChrg = txtNonMainMinBalChrg;
        setChanged();
        
    }
    String getTxtNonMainMinBalChrg(){
        return this.txtNonMainMinBalChrg;
    }
    
    void setTxtInOperative(String txtInOperative){
        this.txtInOperative = txtInOperative;
        setChanged();
        
    }
    String getTxtInOperative(){
        return this.txtInOperative;
    }
    
    void setTxtFolioChrg(String txtFolioChrg){
        this.txtFolioChrg = txtFolioChrg;
        setChanged();
        
    }
    String getTxtFolioChrg(){
        return this.txtFolioChrg;
    }
    
    void setCboFlexiTD(String cboFlexiTD){
        this.cboFlexiTD = cboFlexiTD;
        setChanged();
        
    }
    String getCboFlexiTD(){
        return this.cboFlexiTD;
    }
    
    /*void setCboIntCategory(String cboIntCategory){
        this.cboIntCategory = cboIntCategory;
        setChanged();
     
    }
    String getCboIntCategory(){
        return this.cboIntCategory;
    } */
    
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

    public String getTxtFolioRestrictionPriod() {
        return txtFolioRestrictionPriod;
    }

    public void setTxtFolioRestrictionPriod(String txtFolioRestrictionPriod) {
        this.txtFolioRestrictionPriod = txtFolioRestrictionPriod;
    }
    
    
    
//    public OperativeAcctIntRateParamTO getOperativeAcctIntRateTO(ArrayList arr) {
//        
//        OperativeAcctIntRateParamTO objOperativeAcctIntRateParamTO = new OperativeAcctIntRateParamTO();
//        objOperativeAcctIntRateParamTO.setAcHdId((String)arr.get(0));
//        objOperativeAcctIntRateParamTO.setIntCatId((String)arr.get(1));
//        objOperativeAcctIntRateParamTO.setIntDate((Date)arr.get(2));
//        objOperativeAcctIntRateParamTO.setIntRate((Double)arr.get(3));
//        return objOperativeAcctIntRateParamTO;
//    }
    public void doAction() {
        if ( (getActionType() == ClientConstants.ACTIONTYPE_NEW) || (getActionType() == ClientConstants.ACTIONTYPE_COPY)) {
            insertData();
        }else if ( getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            updateData();
        }else if ( getActionType() == ClientConstants.ACTIONTYPE_DELETE){
            HashMap hash=new HashMap();
            int count=0;
            hash.put("PROD_ID",getTxtProductID());
            List lst=ClientUtil.executeQuery("getProdCountActRec",hash);
            if(lst !=null && lst.size()>0){
                hash=(HashMap)lst.get(0);
                count=CommonUtil.convertObjToInt(hash.get("COUNTS"));
            }
            if(count==0)
                deleteData();
            
            else
                ClientUtil.displayAlert("THIS PRODUCT HAVING ACCOUNT");
        }
    }
//    private void setCoulmnHeader() {
//        tableHeader.add("AccountHead");
//        tableHeader.add("Category");
//        tableHeader.add("Date");
//        tableHeader.add("Rate");
//    }
    
    
    public void verifyAcctHead(com.see.truetransact.uicomponent.CTextField accountHead, String mapName) {
        try{
            final HashMap data = new HashMap();
            data.put("ACCT_HD",accountHead.getText());
            data.put(CommonConstants.MAP_NAME , mapName);
            HashMap proxyResultMap = proxy.execute(data,map);
        }catch(Exception e){
            System.out.println("Error in verifyAcctHead");
//            accountHead.setText("");
            parseException.logException(e,true);
        }
    }
    void setCboFreeWDStFrom(String cboFreeWDStFrom){
        this.cboFreeWDStFrom = cboFreeWDStFrom;
        setChanged();
        
    }
    String getCboFreeWDStFrom(){
        return this.cboFreeWDStFrom;
    }
    
    void setCboFreeChkLeaveStFrom(String cboFreeChkLeaveStFrom){
        this.cboFreeChkLeaveStFrom = cboFreeChkLeaveStFrom;
        setChanged();
        
    }
    String getCboFreeChkLeaveStFrom(){
        return this.cboFreeChkLeaveStFrom;
    }
    
    
    void setCboStDayProdCalcSBCrInt(String cboStDayProdCalcSBCrInt){
        this.cboStDayProdCalcSBCrInt = cboStDayProdCalcSBCrInt;
        setChanged();
        
    }
    String getCboStDayProdCalcSBCrInt(){
        return this.cboStDayProdCalcSBCrInt;
    }
    
    void setCboEndDayProdCalcSBCrInt(String cboEndDayProdCalcSBCrInt){
        this.cboEndDayProdCalcSBCrInt = cboEndDayProdCalcSBCrInt;
        setChanged();
        
    }
    String getCboEndDayProdCalcSBCrInt(){
        return this.cboEndDayProdCalcSBCrInt;
    }//txtIntCalcEndMon, txtStMonIntCalc
    
    void setTxtIntCalcEndMon(String txtIntCalcEndMon){
        this.txtIntCalcEndMon = txtIntCalcEndMon;
        setChanged();
        
    }
    String getTxtIntCalcEndMon(){
        return this.txtIntCalcEndMon;
    }
    
    void setTxtStMonIntCalc(String txtStMonIntCalc){
        this.txtStMonIntCalc = txtStMonIntCalc;
        setChanged();
        
    }
    String getTxtStMonIntCalc(){
        return this.txtStMonIntCalc;
    }
    
    
    void setTxtStartInterCalc(String txtStartInterCalc){
        this.txtStartInterCalc = txtStartInterCalc;
        setChanged();
        
    }
    String getTxtStartInterCalc(){
        return this.txtStartInterCalc;
    }
    
    void setCboStartInterCalc(String cboStartInterCalc){
        this.cboStartInterCalc = cboStartInterCalc;
        setChanged();
        
    }
    String getCboStartInterCalc(){
        return this.cboStartInterCalc;
    }
    
    void setTxtEndInterCalc(String txtEndInterCalc){
        this.txtEndInterCalc = txtEndInterCalc;
        setChanged();
        
    }
    String getTxtEndInterCalc(){
        return this.txtEndInterCalc;
    }
    
    void setCboEndInterCalc(String cboEndInterCalc){
        this.cboEndInterCalc = cboEndInterCalc;
        setChanged();
        
    }
    String getCboEndInterCalc(){
        return this.cboEndInterCalc;
    }
    
    void setTxtStartProdCalc(String txtStartProdCalc){
        this.txtStartProdCalc = txtStartProdCalc;
        setChanged();
        
    }
    String getTxtStartProdCalc(){
        return this.txtStartProdCalc;
    }
    
    void setCboStartProdCalc(String cboStartProdCalc){
        this.cboStartProdCalc = cboStartProdCalc;
        setChanged();
        
    }
    String getCboStartProdCalc(){
        return this.cboStartProdCalc;
    }
    
    void setTxtEndProdCalc(String txtEndProdCalc){
        this.txtEndProdCalc = txtEndProdCalc;
        setChanged();
        
    }
    String getTxtEndProdCalc(){
        return this.txtEndProdCalc;
    }
    
    void setCboEndProdCalc(String cboEndProdCalc){
        this.cboEndProdCalc = cboEndProdCalc;
        setChanged();
        
    }
    String getCboEndProdCalc(){
        return this.cboEndProdCalc;
    }
    
    /**
     * Getter for property tdtLastFolioChargeDt.
     * @return Value of property tdtLastFolioChargeDt.
     */
    public java.lang.String getTdtLastFolioChargeDt() {
        return tdtLastFolioChargeDt;
    }
    
    /**
     * Setter for property tdtLastFolioChargeDt.
     * @param tdtLastFolioChargeDt New value of property tdtLastFolioChargeDt.
     */
    public void setTdtLastFolioChargeDt(java.lang.String tdtLastFolioChargeDt) {
        this.tdtLastFolioChargeDt = tdtLastFolioChargeDt;
    }
    
    /**
     * Getter for property tdtNextFolioDueDate.
     * @return Value of property tdtNextFolioDueDate.
     */
    public java.lang.String getTdtNextFolioDueDate() {
        return tdtNextFolioDueDate;
    }
    
    /**
     * Setter for property tdtNextFolioDueDate.
     * @param tdtNextFolioDueDate New value of property tdtNextFolioDueDate.
     */
    public void setTdtNextFolioDueDate(java.lang.String tdtNextFolioDueDate) {
        this.tdtNextFolioDueDate = tdtNextFolioDueDate;
    }
    
    /**
     * Getter for property txtNumPatternFollowedPrefix.
     * @return Value of property txtNumPatternFollowedPrefix.
     */
    public java.lang.String getTxtNumPatternFollowedPrefix() {
        return txtNumPatternFollowedPrefix;
    }
    
    /**
     * Setter for property txtNumPatternFollowedPrefix.
     * @param txtNumPatternFollowedPrefix New value of property txtNumPatternFollowedPrefix.
     */
    public void setTxtNumPatternFollowedPrefix(java.lang.String txtNumPatternFollowedPrefix) {
        this.txtNumPatternFollowedPrefix = txtNumPatternFollowedPrefix;
    }
    
    /**
     * Getter for property txtNumPatternFollowedSuffix.
     * @return Value of property txtNumPatternFollowedSuffix.
     */
    public java.lang.String getTxtNumPatternFollowedSuffix() {
        return txtNumPatternFollowedSuffix;
    }
    
    /**
     * Setter for property txtNumPatternFollowedSuffix.
     * @param txtNumPatternFollowedSuffix New value of property txtNumPatternFollowedSuffix.
     */
    public void setTxtNumPatternFollowedSuffix(java.lang.String txtNumPatternFollowedSuffix) {
        this.txtNumPatternFollowedSuffix = txtNumPatternFollowedSuffix;
    }
    
    /**
     * Getter for property txtLastAccNum.
     * @return Value of property txtLastAccNum.
     */
    public java.lang.String getTxtLastAccNum() {
        return txtLastAccNum;
    }
    
    /**
     * Setter for property txtLastAccNum.
     * @param txtLastAccNum New value of property txtLastAccNum.
     */
    public void setTxtLastAccNum(java.lang.String txtLastAccNum) {
        this.txtLastAccNum = txtLastAccNum;
    }
    
    /**
     * Getter for property rdoToChargeOnApplFreq_Manual.
     * @return Value of property rdoToChargeOnApplFreq_Manual.
     */
    public boolean isRdoToChargeOnApplFreq_Manual() {
        return rdoToChargeOnApplFreq_Manual;
    }
    
    /**
     * Getter for property rdoAcc_Nro.
     * @return Value of property rdoAcc_Nro.
     */
    public boolean isRdoAcc_Nro() {
        return rdoAcc_Nro;
    }
    
    /**
     * Setter for property rdoAcc_Nro.
     * @param rdoAcc_Nro New value of property rdoAcc_Nro.
     */
    public void setRdoAcc_Nro(boolean rdoAcc_Nro) {
        this.rdoAcc_Nro = rdoAcc_Nro;
    }
    
    /**
     * Getter for property rdoAcc_Nre.
     * @return Value of property rdoAcc_Nre.
     */
    public boolean isRdoAcc_Nre() {
        return rdoAcc_Nre;
    }
    
    /**
     * Setter for property rdoAcc_Nre.
     * @param rdoAcc_Nre New value of property rdoAcc_Nre.
     */
    public void setRdoAcc_Nre(boolean rdoAcc_Nre) {
        this.rdoAcc_Nre = rdoAcc_Nre;
    }
    
    /**
     * Getter for property rdoAcc_Reg.
     * @return Value of property rdoAcc_Reg.
     */
    public boolean isRdoAcc_Reg() {
        return rdoAcc_Reg;
    }
    
    /**
     * Setter for property rdoAcc_Reg.
     * @param rdoAcc_Reg New value of property rdoAcc_Reg.
     */
    public void setRdoAcc_Reg(boolean rdoAcc_Reg) {
        this.rdoAcc_Reg = rdoAcc_Reg;
    }
    
    /**
     * Getter for property minBalForIntCalc.
     * @return Value of property minBalForIntCalc.
     */
    public java.lang.String getMinBalForIntCalc() {
        return minBalForIntCalc;
    }
    
    /**
     * Setter for property minBalForIntCalc.
     * @param minBalForIntCalc New value of property minBalForIntCalc.
     */
    public void setMinBalForIntCalc(java.lang.String minBalForIntCalc) {
        this.minBalForIntCalc = minBalForIntCalc;
    }
    
    public String getTxtATMGL() {
        return txtATMGL;
    }

    public void setTxtATMGL(String txtATMGL) {
        this.txtATMGL = txtATMGL;
    }

    
}