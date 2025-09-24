/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BlockedListRB.java
 * 
 * Created on Wed Feb 09 14:39:43 IST 2005
 */

package com.see.truetransact.ui.sysadmin.blockedlist;

import java.util.ListResourceBundle;

public class BlockedListRB extends ListResourceBundle {
    public BlockedListRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblSpace5", "     "},
        {"btnDelete", ""},
        {"btnClose", ""},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"btnAuthorize", ""},
        {"lblMsg", ""},
        {"btnException", ""},
        {"btnNew", ""},
        {"lblBlockedListId", "Blocked List Id"},
        {"lblFraudStatus", "Fraud Status"},
        {"btnSave", ""},
        {"lblFraudClassifcation", "Fraud Classification"},
        {"btnCancel", ""},
        {"lblSpace3", "     "},
        {"lblBlockedName", "Blocked Name"},
        {"lblStatus", "                      "},
        {"lblCustomerType", "Customer Type"},
        {"lbSpace2", "     "},
        {"lblSpace1", " Status :"},
        {"lblBusinessAddress", "Business Addresss"},
        {"btnPrint", ""},
        {"lblRemarks", "Remarks"} 

   };

}
