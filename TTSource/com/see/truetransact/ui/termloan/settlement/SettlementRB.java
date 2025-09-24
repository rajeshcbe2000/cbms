/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * SettlementRB.java
 *
 * Created on December 23, 2004, 4:54 PM
 */

package com.see.truetransact.ui.termloan.settlement;

import java.util.ListResourceBundle;
/**
 *
 * @author  152713
 */
public class SettlementRB extends ListResourceBundle {
    
    /** Creates a new instance of PowerOfAttorneyRB */
    public SettlementRB() {
    }
    
    protected Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"lblBankName", "Bank Name"},
        {"lblBranchName", "Branch Name"},
        {"lblAccType", "Acc Type"},
        {"lblActNo", "ActNo"},
        {"lblSetMode", "Set Mode"},
        {"lblFromChqNo", "FromChqNo"},
        {"lblToChqNo", "ToChqNo"},
        {"lblQty", "Qty"},
        {"lblChqDate", "ChqDate"},
        {"lblChqAmt", "ChqAmt"},
        {"lblClearingDt", "ClearingDt"},
        {"lblChqBounce", "ChqBounce"},
        {"lblBounReason", "BounReason"},
        {"lblRemarks", "Remarks"},
        {"tblColumnPoA1", "Sl No"},
        {"tblColumnPoA2", "Chq No"},
        {"tblColumnPoA3", "Chq Date"},
        {"tblColumnPoA4", "Chq Amt"},
        {"tblColumnPoA5", "Status"},
        {"cDialogYes", "Yes"},
        {"cDialogNo", "No"},
        {"cDialogCancel", "Cancel"},
        {"cDialogOk", "Ok"},
        
        {"onBehalfOfSameCustWarn", "Same customer cannot give Power of Attorney"},
        {"onBehalfOfCombinationExistWarn", " already gave Power of Attorney to "},
        {"existanceCustomerWarningPoA", "This Customer has entry in Power of Attorney."}
        
    };
}
