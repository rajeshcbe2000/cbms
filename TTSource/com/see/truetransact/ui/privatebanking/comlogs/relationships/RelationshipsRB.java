/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RelationshipsRB.java
 * 
 * Created on Thu Jul 15 16:30:35 GMT+05:30 2004
 */

package com.see.truetransact.ui.privatebanking.comlogs.relationships;

import java.util.ListResourceBundle;

public class RelationshipsRB extends ListResourceBundle {
    public RelationshipsRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"btnClose", ""},
        {"btnAuthorize", ""},
        {"lblMsg", ""},
        {"btnException", ""},
        {"lblSpace4", "     "},
        {"lblContactDate", "Contact Date"},
        {"lblRelationship", "Relationship"},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"lblSpace3", "     "},
        {"lblStatus", "                      "},
        {"lblSpace1", " Status :"},
        {"btnMember", ""},
        {"btnDelete", ""},
        {"lblSourceReference", "Source Reference"},
        {"lblLeadRSO", "Lead RSO"},
        {"lblType", "Type"},
        {"lblMemberValue", ""},
        {"lblMember", "Member"},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"lblRelationshipId", "RelationshipId"},
        {"lblRelationshipIdValue", ""},
        {"lblRelationshipValue", ""},
        {"btnNew", ""},
        {"lblInitiatedBy", "Initiated By"},
        {"btnCancel", ""},
        {"lblSource", "Source"},
        {"lblSubType", "Sub- Type"},
        {"lblContactDescription", "Contact Description"},
        {"btnPrint", ""},
        {"lblBankerName", "Banker Name"} 

   };

}
