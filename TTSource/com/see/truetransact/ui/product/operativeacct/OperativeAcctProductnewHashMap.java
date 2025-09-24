/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OperativeAcctProductnewHashMap.java
 * 
 * Created on Wed Jul 21 10:47:58 IST 2004
 */

package com.see.truetransact.ui.product.operativeacct;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class OperativeAcctProductnewHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public OperativeAcctProductnewHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtAcClosingChrg", new Boolean(false));
        mandatoryMap.put("cboCreditIntRoundOff", new Boolean(false));
        mandatoryMap.put("txtMaxAmtWDSlip", new Boolean(false));
        mandatoryMap.put("txtMinBalAmt", new Boolean(false));
        mandatoryMap.put("txtMinBal1FlexiDep", new Boolean(false));
        mandatoryMap.put("txtStatChrg", new Boolean(false));
        mandatoryMap.put("txtPenalIntChrgStart", new Boolean(false));
        mandatoryMap.put("rdoDebitCompReq_Yes", new Boolean(false));
        mandatoryMap.put("txtMinBalIVRS", new Boolean(false));
        mandatoryMap.put("cboCreditProductRoundOff", new Boolean(false));
        mandatoryMap.put("txtAcctHd", new Boolean(true));
        mandatoryMap.put("txtTaxGL", new Boolean(false));
        mandatoryMap.put("cboStartProdCalc", new Boolean(false));
        mandatoryMap.put("txtFreeChkPD", new Boolean(false));
        mandatoryMap.put("txtApplDebitIntRate", new Boolean(false));
        mandatoryMap.put("rdoFolioToChargeOn_Credit", new Boolean(false));
        mandatoryMap.put("rdoTaxIntApplNRO_Yes", new Boolean(false));
        mandatoryMap.put("txtMaxCrIntRate", new Boolean(false));
        mandatoryMap.put("cboEndDayProdCalcSBCrInt", new Boolean(false));
        mandatoryMap.put("rdoCrIntGiven_Yes", new Boolean(false));
        mandatoryMap.put("rdoATMIssued_Yes", new Boolean(false));
        mandatoryMap.put("rdoChkAllowed_Yes", new Boolean(false));
        mandatoryMap.put("cboInOpAcChrgPd", new Boolean(false));
        mandatoryMap.put("rdoStopPaymentChrg_Yes", new Boolean(false));
        mandatoryMap.put("txtPenalIntDebitBalAcct", new Boolean(false));
        mandatoryMap.put("rdoIntUnClearBal_Yes", new Boolean(false));
        mandatoryMap.put("txtInOpChrg", new Boolean(false));
        mandatoryMap.put("txtOutwardChkRetChrg", new Boolean(false));
        mandatoryMap.put("cboFolioChrgApplFreq", new Boolean(false));
        mandatoryMap.put("txtMinBalDebitCards", new Boolean(false));
        mandatoryMap.put("rdoNonMainMinBalChrg_Yes", new Boolean(false));
        mandatoryMap.put("cboCalcCriteria", new Boolean(false));
        mandatoryMap.put("cboStatChargesChr", new Boolean(false));
        mandatoryMap.put("rdoNomineeReq_Yes", new Boolean(false));
        mandatoryMap.put("cboBehaves", new Boolean(false));
        mandatoryMap.put("rdoIntroReq_Yes", new Boolean(false));
        mandatoryMap.put("txtNoEntryPerFolio", new Boolean(false));
        mandatoryMap.put("txtExcessFreeWDChrgPT", new Boolean(false));
        mandatoryMap.put("txtNoFreeChkLeaves", new Boolean(false));
        mandatoryMap.put("cboMinTreatInOp", new Boolean(false));
        mandatoryMap.put("rdoStatCharges_Yes", new Boolean(false));
        mandatoryMap.put("cboDebitCompIntCalcFreq", new Boolean(false));
        mandatoryMap.put("cboMinTreatasDormant", new Boolean(false));
        mandatoryMap.put("cboStDayProdCalcSBCrInt", new Boolean(false));
        mandatoryMap.put("txtMinTreatasInOp", new Boolean(false));
        mandatoryMap.put("tdtLastIntApplDateCr", new Boolean(false));
        mandatoryMap.put("txtChkBkIssueChrgPL", new Boolean(false));
        mandatoryMap.put("rdoFolioChargeAppl_Yes", new Boolean(false));
        mandatoryMap.put("txtChrgMiscServChrg", new Boolean(false));
        mandatoryMap.put("rdoCreditCdIssued_Yes", new Boolean(false));
        mandatoryMap.put("cboStatFreq", new Boolean(false));
        mandatoryMap.put("txtStMonIntCalc", new Boolean(false));
        mandatoryMap.put("txtAcctCrInt", new Boolean(false));
        mandatoryMap.put("txtStopPayChrg", new Boolean(false));
        mandatoryMap.put("txtEndProdCalc", new Boolean(false));
        mandatoryMap.put("txtMinCrIntRate", new Boolean(false));
        mandatoryMap.put("tdtLastIntApplDate", new Boolean(false));
        mandatoryMap.put("cboCrIntCalcFreq", new Boolean(false));
        mandatoryMap.put("txtAcctOpenChrg", new Boolean(false));
        mandatoryMap.put("rdoTempODAllow_Yes", new Boolean(false));
        mandatoryMap.put("tdtFreeWDStFrom", new Boolean(false));
        mandatoryMap.put("txtExcessFreeWDChrg", new Boolean(false));
        mandatoryMap.put("cboIntCalcEndMon", new Boolean(false));
        mandatoryMap.put("rdoIVRSProvided_Yes", new Boolean(false));
        mandatoryMap.put("cboFreeChkLeaveStFrom", new Boolean(false));
        mandatoryMap.put("txtApplCrIntRate", new Boolean(false));
        mandatoryMap.put("txtMinDebitIntAmt", new Boolean(false));
        mandatoryMap.put("txtMinBal2FlexiDep", new Boolean(false));
        mandatoryMap.put("cboIncompFolioROffFreq", new Boolean(false));
        mandatoryMap.put("cboMinTreatNewClosure", new Boolean(false));
        mandatoryMap.put("txtFolioChrg", new Boolean(false));
        mandatoryMap.put("cboFreeWDStFrom", new Boolean(false));
        mandatoryMap.put("rdoFlexiHappen_SB", new Boolean(false));
        mandatoryMap.put("cboFreeWDPd", new Boolean(false));
        mandatoryMap.put("rdoCreditComp_Yes", new Boolean(false));
        mandatoryMap.put("rdoIssueToken_Yes", new Boolean(false));
        mandatoryMap.put("txtNoNominees", new Boolean(false));
        mandatoryMap.put("txtStatChargesChr", new Boolean(false));
        mandatoryMap.put("cboCreditCompIntCalcFreq", new Boolean(false));
        mandatoryMap.put("txtChrgPreClosure", new Boolean(false));
        mandatoryMap.put("rdoCollectInt_Yes", new Boolean(false));
        mandatoryMap.put("txtMinATMBal", new Boolean(false));
        mandatoryMap.put("txtStDayProdCalcSBCrInt", new Boolean(false));
        mandatoryMap.put("txtAcctDebitInt", new Boolean(false));
        mandatoryMap.put("txtAcctClosingChrg", new Boolean(false));
        mandatoryMap.put("txtDesc", new Boolean(false));
        mandatoryMap.put("cboProdCurrency", new Boolean(false));
        mandatoryMap.put("txtFreeWDChrg", new Boolean(false));
        mandatoryMap.put("txtMiscServChrg", new Boolean(false));
        mandatoryMap.put("rdoLimitDefAllow_Yes", new Boolean(false));
        mandatoryMap.put("txtMainTreatNewAcctClosure", new Boolean(false));
        mandatoryMap.put("txtAcctOpenCharges", new Boolean(false));
        mandatoryMap.put("rdoChkIssuedChrgCh_Yes", new Boolean(false));
        mandatoryMap.put("txtPrematureClosureChrg", new Boolean(false));
        mandatoryMap.put("txtInwardChkRetChrg", new Boolean(false));
        mandatoryMap.put("rdoMobBankClient_Yes", new Boolean(false));
        mandatoryMap.put("txtInOperative", new Boolean(false));
        mandatoryMap.put("cboToCollectFolioChrg", new Boolean(false));
        mandatoryMap.put("rdoStaffAcct_Yes", new Boolean(false));
        mandatoryMap.put("cboDebitIntApplFreq", new Boolean(false));
        mandatoryMap.put("cboMinBalAmt", new Boolean(false));
        mandatoryMap.put("cboStMonIntCalc", new Boolean(false));
        mandatoryMap.put("txtEndInterCalc", new Boolean(false));
        mandatoryMap.put("cboStartInterCalc", new Boolean(false));
        mandatoryMap.put("rdoABBAllowed_Yes", new Boolean(false));
        mandatoryMap.put("cboDebitIntCalcFreq", new Boolean(false));
        mandatoryMap.put("cboEndProdCalc", new Boolean(false));
        mandatoryMap.put("txtlInOpAcChrg", new Boolean(false));
        mandatoryMap.put("tdtLastIntCalcDate", new Boolean(false));
        mandatoryMap.put("tdtFreeChkLeaveStFrom", new Boolean(false));
        mandatoryMap.put("rdoAllowWD_Yes", new Boolean(false));
        mandatoryMap.put("txtProductID", new Boolean(true));
        mandatoryMap.put("txtMinBalCreditCd", new Boolean(false));
        mandatoryMap.put("txtMaxDebitIntRate", new Boolean(false));
        mandatoryMap.put("txtNoFreeWD", new Boolean(false));
        mandatoryMap.put("tdtLastIntCalcDateCR", new Boolean(false));
        mandatoryMap.put("rdoToChargeOnApplFreq_Manual", new Boolean(false));
        mandatoryMap.put("rdoDebitIntChrg_Yes2", new Boolean(false));
        mandatoryMap.put("txtStartProdCalc", new Boolean(false));
        mandatoryMap.put("rdoAcctOpenAppr_Yes", new Boolean(false));
        mandatoryMap.put("txtStopPaymentChrg", new Boolean(false));
        mandatoryMap.put("cboProdFreq", new Boolean(false));
        mandatoryMap.put("txtNonMainMinBalChrg", new Boolean(false));
        mandatoryMap.put("txtMaxCrIntAmt", new Boolean(false));
        mandatoryMap.put("txtMinCrIntAmt", new Boolean(false));
        mandatoryMap.put("txtIntCalcEndMon", new Boolean(false));
        mandatoryMap.put("cboDebitProductRoundOff", new Boolean(false));
        mandatoryMap.put("txtMinBalABB", new Boolean(false));
        mandatoryMap.put("cboCrIntApplFreq", new Boolean(false));
        mandatoryMap.put("txtStartInterCalc", new Boolean(false));
        mandatoryMap.put("cboProdFreqCr", new Boolean(false));
        mandatoryMap.put("rdoDebitCdIssued_Yes", new Boolean(false));
        mandatoryMap.put("txtRatePerFolio", new Boolean(false));
        mandatoryMap.put("cboFlexiTD", new Boolean(false));
        mandatoryMap.put("txtMinBalwchk", new Boolean(false));
        mandatoryMap.put("txtMinTreatasDormant", new Boolean(false));
        mandatoryMap.put("txtChkRetChrgIn", new Boolean(false));
        mandatoryMap.put("cboFreeChkPd", new Boolean(false));
        mandatoryMap.put("cboDebitIntRoundOff", new Boolean(false));
        mandatoryMap.put("txtEndDayProdCalcSBCrInt", new Boolean(false));
        mandatoryMap.put("txtMinDebitIntRate", new Boolean(false));
        mandatoryMap.put("rdoLinkFlexiAcct_Yes", new Boolean(false));
        mandatoryMap.put("txtChkRetChrOutward", new Boolean(false));
        mandatoryMap.put("txtClearingIntAcctHd", new Boolean(false));
        mandatoryMap.put("txtMinTreatasNew", new Boolean(false));
        mandatoryMap.put("cboEndInterCalc", new Boolean(false));
        mandatoryMap.put("txtRateTaxNRO", new Boolean(false));
        mandatoryMap.put("rdoIntClearing_Yes", new Boolean(false));
        mandatoryMap.put("cboMinTreatasNew", new Boolean(false));
        mandatoryMap.put("txtMinMobBank", new Boolean(false));
        mandatoryMap.put("txtChkBkIssueChrg", new Boolean(false));
        mandatoryMap.put("txtMinBalChkbk", new Boolean(false));
        mandatoryMap.put("txtMaxDebitIntAmt", new Boolean(false));
        mandatoryMap.put("txtFreeWDPd", new Boolean(false));
        mandatoryMap.put("txtNumPatternFollowedPrefix", new Boolean(true));
        mandatoryMap.put("txtNumPatternFollowedPSuffix", new Boolean(true));
         mandatoryMap.put("rdoAcc_Reg", new Boolean(true));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}
