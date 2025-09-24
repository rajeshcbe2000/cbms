/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositsProductHashMap.java
 * 
 * Created on Thu Jul 08 14:12:15 IST 2004
 */

package com.see.truetransact.ui.product.deposits;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class DepositsProductHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public DepositsProductHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboNoOfDays", new Boolean(false));
        mandatoryMap.put("rdoCanReceiveExcessInstal_yes", new Boolean(false));
        mandatoryMap.put("rdoCanReceiveExcessInstal_no", new Boolean(false));
        mandatoryMap.put("cboIntType", new Boolean(false));
        mandatoryMap.put("cboProvisioningLevel", new Boolean(false));
        mandatoryMap.put("txtAcctNumberPattern", new Boolean(true));
        mandatoryMap.put("cboIntApplnFreq", new Boolean(false));
        mandatoryMap.put("rdoInstallmentInRecurringDepositAcct_Yes", new Boolean(false));
        mandatoryMap.put("txtWithdrawalsInMultiplesOf", new Boolean(false));
        mandatoryMap.put("cboMaxDepositPeriod", new Boolean(false));
        mandatoryMap.put("txtAcctHd", new Boolean(true));
        mandatoryMap.put("rdoCalcTDS_Yes", new Boolean(true));
        mandatoryMap.put("cboChangeValue", new Boolean(false));
        mandatoryMap.put("cboDepositPerUnit", new Boolean(false));
        mandatoryMap.put("rdoInterestAfterMaturity_Yes", new Boolean(true));
        mandatoryMap.put("txtIntPaybleGLHead", new Boolean(true));
        mandatoryMap.put("txtCommisionPaybleGLHead", new Boolean (false));
        mandatoryMap.put("rdoWithdrawalWithInterest_Yes", new Boolean(false));
        mandatoryMap.put("txtFixedDepositAcctHead", new Boolean(false));
        mandatoryMap.put("txtDepositPerUnit", new Boolean(false));
        mandatoryMap.put("rdoRenewalOfDepositAllowed_Yes", new Boolean(false));
        mandatoryMap.put("rdoSystemCalcValues_Yes", new Boolean(false));
        mandatoryMap.put("txtAlphaSuffix", new Boolean(false));
        mandatoryMap.put("rdoTermDeposit_Yes", new Boolean(false));
        mandatoryMap.put("rdoProvisionOfInterest_Yes", new Boolean(false));
        mandatoryMap.put("cboMinNoOfDays", new Boolean(false));
        mandatoryMap.put("txtIntPeriodForBackDatedRenewal", new Boolean(false));
        mandatoryMap.put("tdtLastInterestAppliedDate", new Boolean(false));
        mandatoryMap.put("cboIntProvisioningFreq", new Boolean(false));
        mandatoryMap.put("cboMaturityDateAfterLastInstalPaid", new Boolean(false));
        mandatoryMap.put("txtLimitForBulkDeposit", new Boolean(false));
        mandatoryMap.put("txtMinAmtOfPAN", new Boolean(false));
        mandatoryMap.put("rdoAutoAdjustment_Yes", new Boolean(false));
        mandatoryMap.put("rdoIntProvisioningApplicable_Yes", new Boolean(false));
        mandatoryMap.put("cboPeriodInMultiplesOf", new Boolean(false));
        mandatoryMap.put("cboMaturityInterestType", new Boolean(false));
        mandatoryMap.put("rdoFlexiFromSBCA_Yes", new Boolean(false));
        mandatoryMap.put("txtMaturityDateAfterLastInstalPaid", new Boolean(false));
//        mandatoryMap.put("rdoAmountRounded_Yes", new Boolean(true));
        mandatoryMap.put("cboInstallmentToBeCharged", new Boolean(false));
        mandatoryMap.put("txtMaxPeriodMDt", new Boolean(false));
        mandatoryMap.put("tdtSchemeIntroDate", new Boolean(true));
        mandatoryMap.put("tdtNextIntProvisionalDate", new Boolean(false));
        mandatoryMap.put("txtMinDepositPeriod", new Boolean(true));
        mandatoryMap.put("rdoCalcMaturityValue_Yes", new Boolean(false));
        mandatoryMap.put("txtIntOnMaturedDepositAcctHead", new Boolean(false));
        mandatoryMap.put("rdoTransferToMaturedDeposits_Yes", new Boolean(false));
        mandatoryMap.put("cboIntCalcMethod", new Boolean(false));
        mandatoryMap.put("cboIntPeriodForBackDatedRenewal", new Boolean(false));
        mandatoryMap.put("txtInterestOnMaturedDeposits", new Boolean(false));
        mandatoryMap.put("txtNoOfPartialWithdrawalAllowed", new Boolean(false));
        mandatoryMap.put("rdoIntroducerReqd_Yes", new Boolean(false));
        mandatoryMap.put("tdtNextInterestAppliedDate", new Boolean(false));
        mandatoryMap.put("txtAdvanceMaturityNoticeGenPeriod", new Boolean(false));
        mandatoryMap.put("txtPrematureWithdrawal", new Boolean(false));
        mandatoryMap.put("txtAmtInMultiplesOf", new Boolean(true));
        mandatoryMap.put("txtAfterHowManyDays", new Boolean(false));
        mandatoryMap.put("txtIntDebitPLHead", new Boolean(true));
        mandatoryMap.put("txtMaxAmtOfCashPayment", new Boolean(true));
        mandatoryMap.put("txtMinDepositAmt", new Boolean(true));
        mandatoryMap.put("cboMinDepositPeriod", new Boolean(false));
        mandatoryMap.put("txtDesc", new Boolean(false));
        mandatoryMap.put("cboProdCurrency", new Boolean(false));
        mandatoryMap.put("cboOperatesLike", new Boolean(true));
        mandatoryMap.put("cboIntCriteria", new Boolean(false));
        mandatoryMap.put("txtCutOffDayForPaymentOfInstal", new Boolean(false));
        mandatoryMap.put("txtAcctHeadForFloatAcct", new Boolean(false));
        mandatoryMap.put("cboRoundOffCriteria", new Boolean(false));
        mandatoryMap.put("txtIntProvisioningAcctHd", new Boolean(true));
        mandatoryMap.put("cboMaxPeriodMDt", new Boolean(false));
        mandatoryMap.put("txtMaxDepositAmt", new Boolean(true));
        mandatoryMap.put("txtAfterNoDays", new Boolean(false));
        mandatoryMap.put("rdoLastInstallmentAllowed_Yes", new Boolean(false));
        mandatoryMap.put("cboCutOffDayForPaymentOfInstal", new Boolean(false));
        mandatoryMap.put("tdtLastIntProvisionalDate", new Boolean(false));
        mandatoryMap.put("txtMinIntToBePaid", new Boolean(false));
        mandatoryMap.put("txtMaxNopfTimes", new Boolean(false));
        mandatoryMap.put("txtProductID", new Boolean(true));
        mandatoryMap.put("cboIntCompoundingFreq", new Boolean(false));
        mandatoryMap.put("rdoAdjustPrincipleToLoan_Yes", new Boolean(false));
        mandatoryMap.put("rdoAdjustIntOnDeposits_No", new Boolean(false));
        mandatoryMap.put("rdoAutoRenewalAllowed_Yes", new Boolean(false));
        mandatoryMap.put("txtMinAmtOfPartialWithdrawalAllowed", new Boolean(false));
        mandatoryMap.put("txtPeriodInMultiplesOf", new Boolean(false));
        mandatoryMap.put("rdoPartialWithdrawalAllowed_Yes", new Boolean(false));
        mandatoryMap.put("cboMaturityInterestRate", new Boolean(false));
        mandatoryMap.put("cboIntMaintainedAsPartOf", new Boolean(false));
        mandatoryMap.put("txttMaturityDepositAcctHead", new Boolean(false));
        mandatoryMap.put("txtMinNoOfDays", new Boolean(false));
        mandatoryMap.put("txtMaxNoOfPartialWithdrawalAllowed", new Boolean(false));
        mandatoryMap.put("txtMaxAmtOfPartialWithdrawalAllowed", new Boolean(false));
        mandatoryMap.put("txtMaxDepositPeriod", new Boolean(true));
        mandatoryMap.put("rdoIntPayableOnExcessInstal_Yes", new Boolean(false));
        mandatoryMap.put("rdoPenaltyOnLateInstallmentsChargeble_Yes", new Boolean(false));
        mandatoryMap.put("txtIntProvisionOfMaturedDeposit", new Boolean(false));
        mandatoryMap.put("rdoExtnOfDepositBeforeMaturity_Yes", new Boolean(false));
        mandatoryMap.put("cboIntRoundOff", new Boolean(false));
        mandatoryMap.put("cboInterestType", new Boolean(false));
        mandatoryMap.put("rdoPayInterestOnHoliday_Yes", new Boolean(true));
        mandatoryMap.put("rdoRecurringDepositToFixedDeposit_Yes", new Boolean(false));
        mandatoryMap.put("rdoRecalcOfMaturityValue_Yes", new Boolean(false));
        mandatoryMap.put("txtTDSGLAcctHd", new Boolean(false));
        mandatoryMap.put("txtLastAcctNumber", new Boolean(true));
        //added for daily deposits scheme
        mandatoryMap.put("cboDepositsFrequency",new Boolean(false));    
        mandatoryMap.put("cboFromAmount", new Boolean (false));
        mandatoryMap.put("cboToAmount", new Boolean (false));
        mandatoryMap.put("cboFromPeriod", new Boolean (false));
        mandatoryMap.put("cboToPeriod", new Boolean (false));
//        mandatoryMap.put("txtFromAmount", new Boolean (false));
//        mandatoryMap.put("txtToAmount", new Boolean (false));
        mandatoryMap.put("txtFromPeriod", new Boolean(false));
        mandatoryMap.put("txtToPeriod", new Boolean (false));
        mandatoryMap.put("txtCommisionRate", new Boolean (false));
        mandatoryMap.put("tdtDate", new Boolean(false));
        mandatoryMap.put("tdtToDate", new Boolean (false));
        mandatoryMap.put("rdoRegular", new Boolean(true));
        mandatoryMap.put("rdoStaffAccount_Yes", new Boolean(true));
        mandatoryMap.put("rdoIntroducerReqd_Yes", new Boolean(true));
        mandatoryMap.put("rdoMonthEnd", new Boolean(false));
        mandatoryMap.put("rdoInstallmentDay", new Boolean (false));
        
}

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}
