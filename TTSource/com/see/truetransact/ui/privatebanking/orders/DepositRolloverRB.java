/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositRolloverRB.java
 * 
 * Created on Wed Jun 16 15:18:19 GMT+05:30 2004
 */

package com.see.truetransact.ui.privatebanking.orders;

import java.util.ListResourceBundle;

public class DepositRolloverRB extends ListResourceBundle {
    public DepositRolloverRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"btnClose", ""},
        {"lblContactTimeHours", "hrs"},
        {"lblContactMode", "Contact Mode"},
        {"lblInstructionFrom", "Instruction From"},
        {"lblAuthSrcDoc", "Auth Source Doc"},
        {"btnAuthorize", ""},
        {"lblSrcDocDate", "Source Doc Date"},
        {"btnException", ""},
        {"lblMsg", ""},
        {"lblSpace4", "     "},
        {"lblContactDate", "Contact Date"},
        {"lblRelationship", "Relationship"},
        {"panMemberDetails", ""},
        {"lblSpace2", "     "},
        {"panContactDetails", ""},
        {"btnSave", ""},
        {"lblClientContact", "Client Contact"},
        {"lblSpace3", " Status :"},
        {"lblStatus", "                      "},
        {"lblDescription", "Description"},
        {"lblSpace1", "     "},
        {"lblPhoneExtnum", "Phone Extension No."},
        {"lblOrderType", "Order Type"},
        {"lblSpace5", "     "},
        {"btnDelete", ""},
        {"lblMember", "Member"},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"lblContactTime", "Contact Time"},
        {"lblSrcDocDetails", "Source Doc Details"},
        {"btnNew", ""},
        {"lblSolicited", "Solicited"},
        {"btnCancel", ""},
        {"btnPrint", ""},
        {"lblContactTimeMinutes", "min"},
        {"lblViewInfoDoc", "View Visual Info Doc"},
        
        {"cDialogOk", "Ok"},
        {"memberRelationshipDeleteWarning", "The Member you are trying to delete, is relation of others... You can't delete this Member."},
        
        {"TOCommandError", "TO Status Command is null"}
   };
}
