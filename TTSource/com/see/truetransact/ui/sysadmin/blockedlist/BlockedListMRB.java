/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BlockedListMRB.java
 * 
 * Created on Wed Feb 09 14:54:16 IST 2005
 */

package com.see.truetransact.ui.sysadmin.blockedlist;

import java.util.ListResourceBundle;

public class BlockedListMRB extends ListResourceBundle {
    public BlockedListMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cboFraudStatus", "FraudStatus should be a proper value!!!"},
        {"txaRemarks", "Remarks should not be empty!!!"},
        {"txaBusinessAddress", "BusinessAddress should not be empty!!!"},
        {"txtFraudClassifcation", "FraudClassifcation should not be empty!!!"},
        {"txtBlockedName", "BlockedName should not be empty!!!"},
        {"cboFraudClassification", "FraudClassification should be a proper value!!!"},
        {"txtBlockedListId", "BlockedListId should not be empty!!!"},
        {"cboCustomerType", "CustomerType should be a proper value!!!"} 

   };

}
