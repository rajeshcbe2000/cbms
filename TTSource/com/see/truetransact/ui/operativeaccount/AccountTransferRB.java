/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * AccountTransferRB.java
 */

package com.see.truetransact.ui.operativeaccount;
import java.util.ListResourceBundle;
public class AccountTransferRB extends ListResourceBundle {
    public AccountTransferRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"tblColumn1", "Select"},
        {"tblColumn2", "Account Number"},
        {"tblColumn3", "Account Holder's Name"},
        {"tblColumn4", "Last Transaction Date"},
        {"tblColumn5", "Balance"},
        {"tblColumn6", "status"},
        
        {"lblAccountHeadDisplay", ""},
        {"lblProductId", "Product ID"},
        {"lblAccountHead", "Account Head"},
        {"lblAccountHeadDescriptionDisplay", ""},
        {"noSelectionErrMsg", "Atleast one row should be selected!!!"},
   };

}
