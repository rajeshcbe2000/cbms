/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * ShareProductRB.java
 *
 * Created on Mon Apr 11 12:08:48 IST 2005
 */

package com.see.truetransact.ui.investments;

import java.util.ListResourceBundle;

public class CallMoneyRB extends ListResourceBundle {
    public CallMoneyRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"btnClose", ""},
        {"btnTabSave", "Save"},
        {"btnTabDelete", "Delete"},
        {"btnAuthorize", ""},
        {"lblMsg", ""},
        {"lblSpace4", "     "},
        {"lblSpace2", "     "},
        {"lblSpace3", "     "},
        {"lblSpace1", " Status :"},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"null", "Account Details"},
        {"btnTabNew", "New"},
        {"btnPrint", ""},
        {"btnException", ""},
        {"btnSave", ""},
        {"lblStatus", "                      "},
        {"btnDelete", ""},
        {"btnNew", ""},
        {"btnCancel", ""},
        {"lblCallMoneyInstituation", "CallMoney Institution"},
        {"lblCallMoneyDate", "Call Money Date"},
        {"lblCommunication", "Communication Mode"},
        {"lblNoOfDays", "No Of Days"},
        {"lblInterestRate", "Interest Rate"},
        {"rdoBorrowing", "Borrowing"},
        {"rdoLending", "Lending"},
        {"rdoNew", "Lending Reconcile"},
        {"rdoReconciled", "Borrowing Reconcile "},
        {"lblCallMoneyAmount", "Call Money Amount"},
        {"lblParticulars", "Particulars"},
        {"lblNoOfDaysExtension", "No Of Days Extension"},
        {"lblExtensionInterestRate", "Extension Interest Rate"},
        {"lblInterestAmount", "Interest Amount"}
        
    };
    
}
