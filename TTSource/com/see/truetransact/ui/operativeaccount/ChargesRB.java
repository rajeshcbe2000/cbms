/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * ChargesRB.java
 * 
 * Created on Thu Sep 16 11:38:27 IST 2004
 */

package com.see.truetransact.ui.operativeaccount;

import java.util.ListResourceBundle;

public class ChargesRB extends ListResourceBundle {
    public ChargesRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"chkExcessTransactionCharges", "Excess Transaction"},
        {"btnFromAccount", ""},
        {"btnClose", ""},
        {"lblFromDate", "From Date"},
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
        {"lblToDate", "To Date"},
        {"lblProductId", "Product ID"},
        {"lblProdType","Product Type"},
        {"panChargeType", "Charges"},
        {"lblStatus", "                      "},
        {"lblSpace1", " Status :"},
        {"btnPrint", ""},
        {"rdoInterestYes","YES"},
        
        {"rdoInterestNo","NO"},
        {"chkNonMaintenanceOFMinBalCharges", "Non-Maintenance of Minimum Balance"}
        
   };

}
