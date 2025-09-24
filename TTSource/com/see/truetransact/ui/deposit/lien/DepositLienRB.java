/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositLienRB.java
 * 
 * Created on Wed Jun 02 10:35:02 GMT+05:30 2004
 */

package com.see.truetransact.ui.deposit.lien;

import java.util.ListResourceBundle;

public class DepositLienRB extends ListResourceBundle {
    public DepositLienRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"btnLienSave", ""},
        {"btnClose", ""},
        {"lblLienAccountNumber", "Lien Account Number"},
        {"btnSave", ""},
        {"btnCancel", ""},
        {"lblSubDepositNo", "Deposit Sub Number"},
        {"lblLienAmount", "Lien Amount"},
        {"lblProductID", "Product ID"},
        {"btnPrint", ""},
        {"lblLienSum", "Sum of Authorized Liens"},
        {"lblLienProductID", "Product ID"},
        {"btnLienDelete", ""},
        {"btnAuthorize", ""},
        {"lblMsg", ""},
        {"lblLienCustId", "Customer ID"},
        {"lblDepositNo", "Deposit Number"},
        {"lblLienCustIdValue", ""},
        {"btnDelete", ""},
        {"lblClearBalance", "Available Balance"},
        {"btnNew", ""},
        {"lblCustomerIDValue", ""},
        {"lblCustomerName", "Customer Name"},
        {"btnLienNew", ""},
        {"lblAccountHDValue", ""},
        {"btnUnLien", "UnLien"},
        {"btnReject", ""},
        {"lblCreditLienAccount", "Credit Lien Account"},
        {"btnLienActNum", ""},
        {"lblRemark", "Remarks"},
        {"lblAccountHD", "Account Head"},
        {"btnException", ""},
        {"lblCustomerNameValue", ""},
        {"lblLienAccountHead", "Lien Account Head"},
        {"lblLienAccHDValue", ""},
        {"lblCustomerID", "Customer ID"},
        {"lblClearBalanceValue", ""},
        {"lblLienDate", "Lien Date"},
        {"lblStatus", "                      "},
        {"btnEdit", ""},
        {"lblSpace4", "     "},
        {"lblSpace3", "     "},
        {"btnDepositNo", ""},
        {"lblLienSumValue", ""},
        {"lblSpace2", "     "},
        {"lblSpace1", " Status :"},
        {"LIENSUM_WARNING","Lien Amount is more than available balance"},
        {"LIENDATE_WARNING","Improper Lien Date. Lien Date cannot be before"},
        {"AUTHORIZE_WARNING","Insufficient fund to fulfill your request"},
        {"DEPOSIT_DATE","Deposit Date : "},
        {"AOD_DATE","Lien Account Opening Date : "},
        {"ZEROAMOUNT_WARNING","Not a valid amount"},
        
        {"lblDepositAmt","Deposit Amount"},
        {"lblDepositAmtValue",""},
        {"lblFreezeSum","Sum of Authorized Freezes"},
        {"lblFreezeSumValue",""},
        {"lblShadowLien","Shadow Lien"},
        {"lblShadowLienValue",""},
        {"lblDepositLienSLNO", "SL No"},
        {"lblDepositLienDesc", ""},
        {"UNLIEN_REMARK","UnLien Remark"},
        {"lblLoanOtherSocietyLoanType","LoanType"},
        {"lblLLoanOtherSocietyLienAcNo","Lien Ac Number"},
        {"lblLoanOtherSocietyLienCustName","Customer Name"},
        {"lblLoanOtherSocietyLienAmount","Lien Amount"},
        {"lblLoanOtherSocietyLienDate","Lien Date"},
        {"lblLoanOtherSocietyRemark","Remark"}

    };
    
}
