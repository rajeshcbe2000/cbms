/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermDepositHashMap.java
 * 
 * Created on Wed Aug 18 15:23:27 GMT+05:30 2004
 */
package com.see.truetransact.ui.deposit;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class TermDepositHashMap implements UIMandatoryHashMap {

    private HashMap mandatoryMap;

    public TermDepositHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtOtherPhone", new Boolean(true));
        mandatoryMap.put("txtNomDetStreet", new Boolean(true));
        mandatoryMap.put("txtMaturityAmount", new Boolean(false));
        mandatoryMap.put("txtNomDetShare", new Boolean(true));
        mandatoryMap.put("cboNominatedBy", new Boolean(true));
        mandatoryMap.put("tdtPaymentDate", new Boolean(false));
        mandatoryMap.put("txtInterBranchTransferNo", new Boolean(true));
        mandatoryMap.put("tdtExpDt", new Boolean(true));
        mandatoryMap.put("txtAuthSignStreet", new Boolean(true));
        mandatoryMap.put("txtTransferingBranchCode", new Boolean(true));
        mandatoryMap.put("rdoOpeningMode_Normal", new Boolean(true));
        mandatoryMap.put("chk15hDeclarations", new Boolean(false));
        mandatoryMap.put("txtInterestProvidedAmount", new Boolean(true));
        mandatoryMap.put("cboNomDetRelationship", new Boolean(true));
        mandatoryMap.put("cboNomDetStatus", new Boolean(true));
        mandatoryMap.put("txtAuthSignName", new Boolean(true));
        mandatoryMap.put("rdoIntroReqd_Yes", new Boolean(true));
        mandatoryMap.put("txtLastTdsCollected", new Boolean(true));
        mandatoryMap.put("cboNomDetMinCountry", new Boolean(true));
        mandatoryMap.put("tdtMaturityDate", new Boolean(false));
        mandatoryMap.put("txtOtherArea", new Boolean(true));
        mandatoryMap.put("cboNomDetState", new Boolean(true));
        mandatoryMap.put("cboCredAccDetProductId", new Boolean(true));
        mandatoryMap.put("txtDepositAmount", new Boolean(true));
        mandatoryMap.put("txtPeriodOfDeposit_Years", new Boolean(false));
        mandatoryMap.put("txtAccNo", new Boolean(true));
        mandatoryMap.put("txtPrintedNoOfTheFdr", new Boolean(false));
        mandatoryMap.put("cboPoaCustomerName", new Boolean(true));
        mandatoryMap.put("txtAuthSignDesignation", new Boolean(true));
        mandatoryMap.put("chkPowerOfAttorney", new Boolean(false));
        mandatoryMap.put("txtName", new Boolean(true));
        mandatoryMap.put("txtNomDetArea", new Boolean(true));
        mandatoryMap.put("txtAuthSignPager", new Boolean(true));
        mandatoryMap.put("txtAuthSignBusinessPhone", new Boolean(true));
        mandatoryMap.put("cboNomDetMinAddType", new Boolean(true));
        mandatoryMap.put("txtPowOfAttPoASlNo", new Boolean(true));
        mandatoryMap.put("txtPowOfAttPhone", new Boolean(true));
        mandatoryMap.put("txtPanNumber", new Boolean(false));
        mandatoryMap.put("txtCredAccDetAmt", new Boolean(true));
        mandatoryMap.put("cboSettlementMode", new Boolean(true));
        mandatoryMap.put("cboNomDetMinGdType", new Boolean(true));
        mandatoryMap.put("txtCredAccDetParticulars", new Boolean(true));
        mandatoryMap.put("cboPowOfAttState", new Boolean(true));
        mandatoryMap.put("txtNomDetNomSlNo", new Boolean(true));
        mandatoryMap.put("txtIssBy", new Boolean(true));
        mandatoryMap.put("cboPowOfAttCity", new Boolean(true));
        mandatoryMap.put("txtAuthSignMobile", new Boolean(true));
        mandatoryMap.put("txtIssAuth", new Boolean(true));
        mandatoryMap.put("txtPoAAreaCode", new Boolean(true));
        mandatoryMap.put("cboOnBehalfOf", new Boolean(true));
        mandatoryMap.put("tdtLastInstallmentReceivedDate", new Boolean(false));
        mandatoryMap.put("cboCategory", new Boolean(true));
        mandatoryMap.put("txtPowOfAttPoAHoldName", new Boolean(true));
        mandatoryMap.put("cboNomDetCity", new Boolean(true));
        mandatoryMap.put("cboNomDetCountry", new Boolean(true));
        mandatoryMap.put("txtPeriodicInterestAmount", new Boolean(false));
        mandatoryMap.put("cboIdType", new Boolean(true));
        mandatoryMap.put("txtNomDetMinGdName", new Boolean(true));
        mandatoryMap.put("txtPowOfAttArea", new Boolean(true));
        mandatoryMap.put("txtNomDetNomName", new Boolean(true));
        mandatoryMap.put("cboInterestPaymentFrequency", new Boolean(false));
        mandatoryMap.put("txtAuthSignHomePhone", new Boolean(true));
        mandatoryMap.put("chkNomineeDetails", new Boolean(false));
        mandatoryMap.put("txtOtherPin", new Boolean(true));
        mandatoryMap.put("txtPowOfAttStreet", new Boolean(true));
        mandatoryMap.put("txtDocNo", new Boolean(true));
        mandatoryMap.put("txtAuthSignNoOfAuthSign", new Boolean(false));
        mandatoryMap.put("txtPowOfAttPincode", new Boolean(true));
        mandatoryMap.put("txtNomDetMinPhone", new Boolean(true));
        mandatoryMap.put("cboOtherCity", new Boolean(true));
        mandatoryMap.put("cboNomDetMinState", new Boolean(true));
        mandatoryMap.put("txtOtherAreaCode", new Boolean(true));
        mandatoryMap.put("txtNomDetPhone", new Boolean(true));
        mandatoryMap.put("txtOtherStreet", new Boolean(true));
        mandatoryMap.put("cboPaymentType", new Boolean(false));
        mandatoryMap.put("txtPeriodOfDeposit_Months", new Boolean(false));
        mandatoryMap.put("txtAuthSignArea", new Boolean(true));
        mandatoryMap.put("cboNomDetAddType", new Boolean(true));
        mandatoryMap.put("chkAuthorizedSignatory", new Boolean(false));
        mandatoryMap.put("txtCustomerId", new Boolean(true));
        mandatoryMap.put("txtTransferingAmount", new Boolean(true));
        mandatoryMap.put("cboInterestPaymentMode", new Boolean(false));
        mandatoryMap.put("cboProductId", new Boolean(true));
        mandatoryMap.put("cboAuthSignCity", new Boolean(true));
        mandatoryMap.put("txtAuthSignPincode", new Boolean(true));
        mandatoryMap.put("chkTaxDeductions", new Boolean(false));
        mandatoryMap.put("tdtDateOfDeposit", new Boolean(true));
        mandatoryMap.put("txtInterestPaid", new Boolean(true));
        mandatoryMap.put("txtAuthSignBusinessFax", new Boolean(true));
        mandatoryMap.put("cboNomDetMinCity", new Boolean(true));
        mandatoryMap.put("txtIntroName", new Boolean(true));
        mandatoryMap.put("cboAuthSignState", new Boolean(true));
        mandatoryMap.put("tdtPowOfAttPeriodTo", new Boolean(true));
        mandatoryMap.put("cboPowOfAttAddType", new Boolean(true));
        mandatoryMap.put("txtCredAccDetDepositNo", new Boolean(true));
        mandatoryMap.put("txtNomDetMinAreaCode", new Boolean(true));
        mandatoryMap.put("cboAuthSignAddForComm", new Boolean(true));
        mandatoryMap.put("txtRemarks", new Boolean(false));
        mandatoryMap.put("txtNomDetMinStreet", new Boolean(true));
        mandatoryMap.put("tdtDateOfTransfer", new Boolean(true));
        mandatoryMap.put("tdtLastInterestCalculatedDate", new Boolean(true));
        mandatoryMap.put("txtPeriodOfDeposit_Days", new Boolean(false));
        mandatoryMap.put("txtAuthSignLimits", new Boolean(true));
        mandatoryMap.put("cboAuthSignCountry", new Boolean(true));
        mandatoryMap.put("txtDesig", new Boolean(true));
        mandatoryMap.put("cboInstallmentAmount", new Boolean(false));
        mandatoryMap.put("tdtOriginalDateOfDeposit", new Boolean(true));
        mandatoryMap.put("cboIntroducerType", new Boolean(false));
        mandatoryMap.put("txtBankName", new Boolean(true));
        mandatoryMap.put("txtTotalNumberOfInstallments", new Boolean(false));
        mandatoryMap.put("cboOtherCountry", new Boolean(true));
        mandatoryMap.put("tdtPowOfAttPeriodFrom", new Boolean(true));
        mandatoryMap.put("txtTotalInstallmentReceived", new Boolean(false));
        mandatoryMap.put("txtNo", new Boolean(true));
        mandatoryMap.put("cboPowOfAttCountry", new Boolean(true));
        mandatoryMap.put("cboConstitution", new Boolean(true));
        mandatoryMap.put("cboAddressType", new Boolean(true));
        mandatoryMap.put("txtNomDetPincode", new Boolean(true));
        mandatoryMap.put("txtAuthSignHomeAreaCode", new Boolean(true));
        mandatoryMap.put("txtDebAccDetParticulars", new Boolean(true));
        mandatoryMap.put("txtTotalInterestAmount", new Boolean(false));
        mandatoryMap.put("txtIntroducerDetailsAccountNumber", new Boolean(true));
        mandatoryMap.put("cboOtherState", new Boolean(true));
        mandatoryMap.put("txtOriginalAccountNumber", new Boolean(true));
        mandatoryMap.put("txtNomDetMinPincode", new Boolean(true));
        mandatoryMap.put("txtCredAccDetAccNo", new Boolean(true));
        mandatoryMap.put("txtAuthSignHomeFax", new Boolean(true));
        mandatoryMap.put("txtNomDetMinArea", new Boolean(true));
        mandatoryMap.put("txtBranchName", new Boolean(true));
        mandatoryMap.put("txtRateOfInterest", new Boolean(true));
        mandatoryMap.put("txtNomDetAreaCode", new Boolean(true));
        mandatoryMap.put("txtPowOfAttRemarks", new Boolean(true));
        mandatoryMap.put("tdtNomDetMinDOB", new Boolean(true));
        mandatoryMap.put("txtAuthSignEmailId", new Boolean(true));
        mandatoryMap.put("txtDebAccDetAmt", new Boolean(true));
        mandatoryMap.put("chkStandingInstructions", new Boolean(false));
        mandatoryMap.put("cboDocType", new Boolean(true));
        mandatoryMap.put("txtDepsoitNo", new Boolean(false));
        mandatoryMap.put("tdtIssDt", new Boolean(true));
        mandatoryMap.put("tdtTdsCollectedUpto", new Boolean(true));
        mandatoryMap.put("txtAgentId", new Boolean(false));
        mandatoryMap.put("tdtCalenderFreqDate", new Boolean(false));
    }

    public HashMap getMandatoryHashMap() {
        return this.mandatoryMap;
    }
}
