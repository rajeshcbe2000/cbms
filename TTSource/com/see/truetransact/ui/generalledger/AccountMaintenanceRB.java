/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * AccountMaintenanceRB.java
 */

package com.see.truetransact.ui.generalledger;
import java.util.ListResourceBundle;
public class AccountMaintenanceRB extends ListResourceBundle {
    public AccountMaintenanceRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"chkCreditClearing", ""},
        {"lblAccountType", "Account Type"},
        {"lblCredit", "Credit"},
        {"lblSubHeadDescription", ""},
        {"lblAccountHead", "Account Head"},
        {"lblAccountClosedOnDisplay", ""},
        {"rdoFloatAccount_No", "No"},
        {"chkCreditTransfer", ""},
        {"chkDebitCash", ""},
        {"lblSubHead", "Sub Head"},
        {"lblLastTransactionDateDisplay", ""},
        {"lblAccountOpenedOn", "Account Opened On"},
        {"chkDebitClearing", ""},
        {"lblAccountClosedOn", "Account Closed On"},
        {"lblSubHeadDisplay", ""},
        {"lblAccountOpenedOnDisplay", ""},
        {"rdoStatus_NonImplemented", "Non-Implemented"},
        {"lblBalanceInGL", "Balance in GL"},
        {"chkCreditCash", ""},
        {"lblFirstTransactionDateDisplay", ""},
        {"lblBalanceGLType", "Type of Balance in GL"},
        {"rdoFloatAccount_Yes", "Yes"},
        {"lblCash", "Cash"},
        {"lblClearing", "Clearing"},
        {"lblAccountTypeDisplay", ""},
        {"lblAccountHeadCodeDisplay", ""},
        {"lblFirstTransactionDate", "First Transaction Date"},
        {"lblPostingMode", "Posting Mode"},
        {"rdoStatus_Implemented", "Implemented"},
        {"lblFloatAccount", "Float Account"},
        {"lblTransactionPosting", "Transaction Posting"},
        {"lblDebit", "Debit"},
        {"lblContraHead", "Contra Account Head"},
        {"lblTransfer", "Transfer"},
        {"lblMajorHead", "Major Head"},
        {"chkReconcilliationAllowed", ""},
        {"lblReconcilliationAllowed", "Reconcilliation Allowed"},
        {"chkDebitTransfer", ""},
        {"lblLastTransactionDate", "Last Transaction Date"},
        {"lblMajorHeadDisplay", ""},
        {"TOCommandError", "TO Status Command is null"},
        
        {"cboContraHead" , "Contra Head should be a proper value!!!"},
        {"txtReconcillationAcHd", "Reconcillation Account Head should be a proper value"},
        
        {"rdoNegValue_No", "No"},
        {"rdoNegValue_Yes", "Yes"},
        
        // UNGENERATED CODE
        {"acHdInvalid" , "Account head is invalid"},
        {"acHdConfigured" , "Account head is already configured"},
        {"lblHdOfficeAc", "Head Office Account"},
        {"lblReconcillationAcHd", "Reconciliation Account Head"},
        {"lblBalCheck", "Day End Zero Balance Check"},
        {"btnDelete", ""},
        {"lblAccountType", "Account Type"},
        {"btnClose", ""},
        {"lblAccountHeadCode", "Account Head Code"},
        {"lblAccountHead", "Account Head"},
        {"lblAccountHeadDesc", "Description"},
        {"lblReceiveDayBookDetail", "Receipt Detail in Day Book"},
        {"lblPayDayBookDetail", "Payment Detail in Day Book"},
        {"lblAccountHeadOrder", "Account Head Order"},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"btnAuthorize", ""},
        {"lblSubHead", "Sub Head"},
        {"lblMsg", ""},
        {"btnException", ""},
        {"lblSpace4", "     "},
        {"btnNew", ""},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"btnCancel", ""},
        {"lblMajorHead", "Major Head"},
        {"lblSpace3", "     "},
        {"lblStatus", "                      "},
        {"lblSpace1", " Status :"},
        {"btnPrint", ""},
        {"lblSpace5", "     "},
        {"lblSerTaxApl","Service Tax Applicable"},
        
        /** UNGENERATED CODE  ***/
        
        {"TOCommandError", "TO Status Command is null"},
        {"treeHeading","Account Head"},
        {"acHdExistance","Active Account(s) exist for this Account Head!!!"},
        
   };

}
