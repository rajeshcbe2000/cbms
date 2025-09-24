/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositRolloverMRB.java
 * 
 * Created on Wed Jun 16 15:43:00 GMT+05:30 2004
 */

package com.see.truetransact.ui.privatebanking.orders;

import java.util.ListResourceBundle;

public class DepositRolloverMRB extends ListResourceBundle {
    public DepositRolloverMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtDescription", "Description should not be empty!!!"},
        {"dateContactDate", "Contact Date should not be empty!!!"},
        {"cboOrderType", "Order Type should be a proper value!!!"},
        {"cboInstructionFrom", "Instruction From should be a proper value!!!"},
        {"txtSrcDocDetails", "Source Doc Details should be a proper value!!!"},
        {"txtMember", "Member should not be empty!!!"},
        {"cboContactTimeHours", "Contact Time Hours should be a proper value!!!"},
        {"cboSolicited", "Solicited should be a proper value!!!"},
        {"cboContactMode", "Contact Mode should be a proper value!!!"},
        {"txtPhoneExtnum", "Phone Extnum should not be empty!!!"},
        {"cboAuthSrcDoc", "Auth Source Doc should not be empty!!!"},
        {"cboClientContact", "Client Contact should be a proper value!!!"},
        {"cboContactTimeMinutes", "Contact Time Minutes should be a proper value!!!"},
        {"dateSrcDocDate", "Source Doc Date should not be empty!!!"},
        {"cboRelationship", "Relationship should be a proper value!!!"},
        {"cboViewInfoDoc", "View Info Doc should be a proper value!!!"} 
   };
}
