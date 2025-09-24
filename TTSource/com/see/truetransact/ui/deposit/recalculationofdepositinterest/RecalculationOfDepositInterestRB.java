/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RecalculationOfDepositInterestRB.java
 * 
 * Created on Thu Sep 16 11:38:27 IST 2004
 */

package com.see.truetransact.ui.deposit.recalculationofdepositinterest;

import java.util.ListResourceBundle;

public class RecalculationOfDepositInterestRB extends ListResourceBundle {
    public RecalculationOfDepositInterestRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"chkExcessTransactionCharges", "Excess Transaction"},
        {"btnFromAccount", ""},
        {"btnClose", ""},
        {"lblFromDate", "Account Opened From Date"},
        {"lblToAccount", "To Account"},
        {"chkFolioCharges", "Folio"},
        {"chkChequeBookIssueCharges", "Cheque Book Issue"},
        {"chkMiscCharges", "Miscellaneous"},
        {"lblFromAccount", "From Account"},
        {"chkInoperativeCharges", "InOperative"},
        {"btnToAccount", ""},
        {"chkStopPaymentCharges", "Stop Payment"},
        {"chkStatementCharges", "Statement"},
        {"lblMsg", ""},
        {"lblSpace4", "     "},
        {"btnProcess", ""},
        {"lblToDate", "Account Opened To Date"},
        {"lblProductId", "Product ID"},
        {"lblProdType","Product Type"},
        {"panChargeType", "Charges"},
        {"lblStatus", "                      "},
        {"lblSpace1", " Status :"},
        {"btnPrint", ""},
        {"rdoInterestYes","YES"},
        {"tblColumn1","Deposit No"},
        {"tblColumn2","Customer Name"},
        {"tblColumn3","Category"},
        {"tblColumn4","DepositDate"},
        {"tblColumn5","Old ROI"},
        {"tblColumn6","New ROI"},
        {"tblColumn7","OldMaturityAmt"},
        {"tblColumn8","NewMaturityAmt"},
        {"rdoInterestNo","NO"},
        {"chkNonMaintenanceOFMinBalCharges", "Non-Maintenance of Minimum Balance"}
        
   };

}
