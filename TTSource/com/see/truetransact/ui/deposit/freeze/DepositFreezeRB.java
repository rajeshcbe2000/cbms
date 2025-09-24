/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositFreezeRB.java
 * 
 * Created on Fri Jun 04 11:40:52 GMT+05:30 2004
 */

package com.see.truetransact.ui.deposit.freeze;

import java.util.ListResourceBundle;

public class DepositFreezeRB extends ListResourceBundle {
    public DepositFreezeRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"btnClose", ""},
        {"lblFreezeDate", "Freeze Date"},
        {"btnSave", ""},
        {"btnCancel", ""},
        {"lblSubDepositNo", "Deposit Sub Number"},
        {"lblProductID", "Product ID"},
        {"lblFreezeSumValue", ""},
        {"btnPrint", ""},
        {"btnFreezeSave", ""},
        {"btnAuthorize", ""},
        {"lblMsg", ""},
        {"lblDepositNo", "Deposit Number"},
        {"btnDelete", ""},
        {"lblClearBalance", "Available Balance"},
        {"lblFreezeAmount", "Amount"},
        {"btnNew", ""},
        {"lblCustomerIDValue", ""},
        {"lblCustomerName", "Customer Name"},
        {"lblFreezeSum", "Sum of Authorized Freezes"},
        {"lblAccountHDValue", ""},
        {"btnFreezeDelete", ""},
        {"btnReject", ""},
        {"lblRemark", "Remarks"},
        {"btnException", ""},
        {"lblAccountHD", "Account Head"},
        {"lblCustomerNameValue", ""},
        {"lblCustomerID", "Customer ID"},
        {"btnFreezeNew", ""},
        {"lblClearBalanceValue", ""},
        {"btnUnFreeze", "UnFreeze"},
        {"lblStatus", "                      "},
        {"lblFreezeType", "Type"},
        {"btnEdit", ""},
        {"lblSpace4", "     "},
        {"btnDepositNo", ""},
        {"lblSpace3", "     "},
        {"lblSpace2", "     "},
        {"lblSpace1", " Status :"},
        {"FREEZESUM_WARNING","Freeze Amount is more than available balance"},
        {"AUTHORIZE_WARNING","Insufficient fund to fulfill your request"},
        {"ZEROAMOUNT_WARNING","Not a valid amount"},
        
        {"lblDepositAmt","Deposit Amount"},
        {"lblDepositAmtValue",""},
        {"lblLienSum","Sum of Authorized Liens"},
        {"lblLienSumValue",""},
        {"lblShadowFreeze","Shadow Freeze"},
        {"lblShadowFreezeValue",""},
         {"lblDepositFreezeNo", "Sl No."},
         {"lblDepositFreezeNoDesc", ""},
        {"UNFREEZE_REMARK","UnFreeze Remark"},
        {"PARTWARNING","If u Want To Freeze Full Amount Choose COMPLETE"} ,
        {"COMPWARNING","Choose the PARTIAL Option If u Dont Want To Freeze Complete Amount"} 
   };

}
