/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositClosingRB.java
 * 
 * Created on Thu May 20 12:39:02 GMT+05:30 2004
 */

package com.see.truetransact.ui.deposit.closing;

import java.util.ListResourceBundle;

public class DepositClosingRB extends ListResourceBundle {
    public DepositClosingRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblCategoryValue", ""},
        {"lblSettlementMode", "Settlement Mode"},
        {"btnClose", ""},
        {"lblInterestPaymentFrequencyValue", ""},
        {"btnSave", ""},
        {"lblInterestCredited", "Interest Credited"},
        {"lblBalanceDeposit", "Balance Deposit"},
        {"lblRateOfInterest", "Rate Of Interest"},
        {"btnCancel", ""},
        {"lblTDSCollectedValue", ""},
        {"lblPayRecValue", ""},
        {"lblDeathClaim","Death Claim Applied"},
        //2lines added 26.03.2007
        {"lblRateApplicableValue",""},
        {"lblPenaltyPenalRateValue",""},
        {"lblMVvalue", ""},
        {"btnAuthorize", ""},
        {"lblMsg", ""},
        {"lblDepositNo", "Deposit No."},
        {"lblAgentCommisionRecovered","Commision Recovered Amount"},
        {"lblWithDrawnAmount", "WithDrawn Amount"},
        {"lblClTDSCollected", "TDS Collected"},
        {"lblInterestDrValue", ""},
        {"lblMV", "Maturity Value"},
        {"lblTDSCollected", "TDS Collected"},
        {"lblInterestCreditedValue", ""},
        {"lblBalanceDepositValue", ""},
        {"lblDepositDate", "Date of Deposit"},
        {"lblProductId", "Product ID"},
        {"lblCategory", "Category"},
        {"btnException", ""},
        {"lblInterestRateValue", ""},
        {"lblConstitution", "Constitution"},
        {"lblBalanceValue", ""},
        {"lblInstPaidValue", ""},
        {"lblStatus", "                      "},
        {"lblBalance", "Balance"},
        {"lblSpace5", "     "},
        {"lblCustStreetValue", ""},
        {"lblPinCodeValue", ""},
        {"lblSpace3", "     "},
        {"lblSpace2", "     "},
        {"lblCustomerIdValue", ""},
        {"lblSpace1", " Status :"},
        {"lblCity", "City"},
        {"panDepositDetails", "Deposits Detail"},
        {"lblMaturityDateValue", ""},
        {"lblDepositDateValue", ""},
        {"lblRateOfInterestValue", ""},
        {"lblPrincipalValue", ""},
        {"lblDelayedAmount","Delayed Amount"},
        {"lblDelayedInstallments","Delayed Installments"},
        {"lblInstDue", "Total Number of Installments"},
        {"lblMaturityDate", "Maturity Date"},
        {"btnPrint", ""},
        {"lblDepositAccountName", "Account Name"},
        {"lblCityValue", ""},
        {"lblInterestDr", "Interest Payable"},
        {"lblLienFreezeAmount", "Lien/Freeze Amount"},
        {"lblWithDrawnAmountValue", ""},
        {"panNormalClosure", ""},
        {"btnDelete", ""},
        {"btnNew", ""},
        {"lblInterestCrValue", ""},
        {"lblLastInterestApplDateValue", ""},
        {"panClosingPosition", "Closing Position"},
        {"lblDepositAccountNameValue", ""},
        {"lblConstitutionValue", ""},
        {"lblPayRec", "Payable/Receivable"},
        {"btnReject", ""},
        {"panCustomer", "Customer Information"},
        {"lblPeriodValue", ""},
        {"lblAccountHeadValue", "[]"},
        {"lblInstDueValue", ""},
        {"lblPrincipal", "Principal"},
        {"lblAccountHead", "Account Head"},
        {"lblPeriod", "Period"},
        {"lblInterestPaymentFrequency", "Interest Payment Frequency"},
        {"lblInterestCr", "Interest Paid"},
        {"lblActualPeriodRun","Actual Period Run"},
        {"lblActualPeriodRunValue",""},
        {"lblLastInterestApplDate", "Last Interest Applied Date"},
        {"lblClDisbursal", "Closing Disbursal"},
        {"lblClTDSCollectedValue", ""},
        {"lblInterestDrawn", "Interest Drawn"},
        {"panSubDepositInfo", "Sub Deposit Information"},
        {"lblInstPaid", "Number of Installments Paid"},
        {"lblInterestDrawnValue", ""},
        {"lblLienFreezeAmountValue", ""},
        {"btnEdit", ""},
        {"lblClDisbursalValue", ""},
        {"btnDepositNo", ""},   
        {"lblTransferOut","Closure on Transfer Out"},
        {"lblTransferringBranch","TransferOut Branch Code"},
        {"lblBranch","TransferOut Branch Name"},        
        {"shadowCrDr", "Deposit cannot be closed as Shadow Debit or Credit is not zero."},
        {"freezeAmount", "Deposit cannot be closed as Freeze exists."},
        {"TransferOut", "Transfer Out Branch Code Should Not be Empty!!!"},
        {"lienAmount", "Lien Exists For This Deposit..."},
        {"WARNING_WITHDRAWALAMT_GREATER", "Withdrawal Amount greater than Available!!!"},
        {"WARNING_WITHDRAWALAMT_ZERO", "Not a valid Amount!!!"},
        {"WARNING_WITHDRAWALAMT_NULL", "Withdrawal Amount cannnot be null!!!"},
        {"WARNING_WITHDRAWALAMT_INVALIDMUL", "Withdrawal Amount should be in unit multiple"},
        {"WARNING_CLOSURETYPE_NULL", "Please select the closure type"},
          //Added by Chithra on 21-04-14
        {"lblAdditionalIntrstHed","Ad Int Det."},
        {"lblPeriodOfMaturity","Pd Run Frm Dt of Mtrty."},
        {"lblMaturityPeriod",""},
        {"lblAddIntrstRate","Ad Int Rate"},
        {"lblAddIntrstRteVal",""},
        {"lblAddIntRteAmt","Ad Int Amt"},
        {"lblAddIntRtAmtVal",""}
   };
}
