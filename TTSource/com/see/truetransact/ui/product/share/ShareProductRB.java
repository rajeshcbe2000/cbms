/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductRB.java
 *
 * Created on Mon Apr 11 12:08:48 IST 2005
 */

package com.see.truetransact.ui.product.share;

import java.util.ListResourceBundle;

public class ShareProductRB extends ListResourceBundle {
    public ShareProductRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"btnClose", ""},
        {"btnTabSave", "Save"},
        {"btnTabDelete", "Delete"},
        {"lblNommineeAllowed", "No. Of Nominees Allowed"},
        {"btnAuthorize", ""},
        {"lblMsg", ""},
        {"lblSpace4", "     "},
        {"lblSubscribedCapital", "Subscribed Capital"},
        {"lblPaidCapital", "Paidup Capital"},
        {"lblCalculatedDate", "Last Div Up To Financial Year "},
        {"lblUnClaimedDivPeriod", "Unclaimed Dividend Period"},
        {"lblSpace2", "     "},
        {"lblSpace3", "     "},
        {"lblSpace1", " Status :"},
        {"panLoan", "Share Link To Borrowing"},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"null", "Account Details"},
        {"btnTabNew", "New"},
        {"chkReqActHolder", ""},
        {"lblDueDate", "Next Due date"},
        {"lblMaxLoanLimit", "Maximum Loan Limit"},
        {"lblShareSuspAccount", "Share Suspense Account"},
        {"btnPrint", ""},
        {"lblLoanAvailingLimit", "Loan Availing Limit(Limit * ShareAmount)"},
        {"lblFaceValue", "Face Value"},
        {"lblConsiderSalaryRecovery", "Do not Consider For Salary Recovery"},
        {"lblDivAppFrequency", "Dividend Application Frequency"},
        {"lblMaxShareHolding", "Maximum No.of Shares"},
        {"lblNomineePeriod", "Nominal Member Period"},
        {"lblLoanType", "Loan Category Type"},
        {"lblApplicationFee", "Application Fee"},
        {"lblDividentPercentage", "Percentage of Dividend"},
        {"lblRefundinaYear", "Refund in a Year"},
        {"btnException", ""},
        {"lblSurityLimit", "Surety Limit"},
        {"lblShareType", "Share Type"},
        {"btnSave", ""},
        {"lblShareFee", "Share Fee"},
        {"btnShareAccount", ""},
        {"lblStatus", "                      "},
        {"lblAdmissionFeeFixed", "Admission Fee Fixed"},
        {"lblAdmissionFeePercent", "Admission Fee percent"},
        {"btnDelete", ""},
        {"lblDivCalcFrequency", "Dividend Calculation Frequency"},
        {"lblRefundPeriod", "Lock up Period for Refund of Shares"},
        {"lblAdditionalShareRefund", "Lock up Period for Additional Shares"},
        {"btnNew", ""},
        {"btnCancel", ""},
        {"lblAuthorizedCapital", "Authorized Capital"},
        {"lblReqActHolder", "Whether Should Be An A/c Holder"},
        {"panDivident", "Share Link To Dividend"},
        {"panAdvancesLimit", "Share Link To Borrowing"},
        {"lblUnsecuredAdvances", "Unsecured Advances"},
        {"lblSecuredAdvances", "Secured Advances"},
        {"lblPercentage", "%"},
        {"lblSecuredPercentage", "%"},
        {"cDialogOK", "OK"},
        {"existingMsg", "ShareType Selected Aleready Exists"},
        {"panLoanOperations", ""},
        {"tblHeading1", "Loan Type"},
        {"tblHeading2", "Borrower Share %"},
        {"tblHeading3", "Surity Share %"},
        {"tblHeading4", "Surety Limit"},
        {"tblHeading5", "Max Loan Amount"},
        {"unclaimedmsg", "Invalid Uncliamed Divident Period"},
        {"nomineeMsg", "Invalid Nominee Member Period"},
        {"shareMsg", "Invalid Lockup Period for Additional Shares"},
        {"refundMsg", "Invalid Lockup Period for Refund of Shares"},
        {"SubsidyMsg", "Kindly Enter the Subsidy details"},
        {"lblMinIntialShares","Minimum No.of Initial Shares"},
        {"pensionAge","Enter Pension Age(Not zero)!"},
        {"sharePeriod","Enter share Run Period(should Not be zero)!"},
        {"minPension","Enter Mininum Pension Amount(should Not be zero)!"},
        {"pensionProductType","Enter Pension Debit Account Type"},
        {"pensionProductId","Enter Pension Debit Product Id"},
        {"pensionAccount","Enter Pension Debit Account"}
        
    };
    
}
