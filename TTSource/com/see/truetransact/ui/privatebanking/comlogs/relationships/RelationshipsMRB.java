/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RelationshipsMRB.java
 * 
 * Created on Thu Jul 15 17:05:10 GMT+05:30 2004
 */

package com.see.truetransact.ui.privatebanking.comlogs.relationships;

import java.util.ListResourceBundle;

public class RelationshipsMRB extends ListResourceBundle {
    public RelationshipsMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtMember", "BankerName should not be empty!!!"},
        {"txtBankerName", "BankerName should not be empty!!!"},
        {"tdtContactDate", "ContactDate should not be empty!!!"},
        {"txtLeadRSO", "LeadRSO should not be empty!!!"},
        {"cboSource", "Source should be a proper value!!!"},
        {"cboSubType", "SubType should be a proper value!!!"},
        {"cboType", "Type should be a proper value!!!"},
        {"txtSourceReference", "SourceReference should not be empty!!!"},
        {"txaContactDescription", "ContactDescription should not be empty!!!"},
        {"txtInitiatedBy", "InitiatedBy should not be empty!!!"} 

   };

}
