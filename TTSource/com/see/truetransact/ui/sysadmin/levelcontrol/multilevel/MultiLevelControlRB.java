/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MultiLevelControlRB.java
 * 
 * Created on Mon Sep 13 11:14:18 GMT+05:30 2004
 */

package com.see.truetransact.ui.sysadmin.levelcontrol.multilevel;

import java.util.ListResourceBundle;

public class MultiLevelControlRB extends ListResourceBundle {
    public MultiLevelControlRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"chkClearingCredit", "Clearing Credit"},
        {"btnRejection", ""},
        {"btnClose", ""},
        {"btnOR", "OR"},
        {"chkCashDebit", "Cash Debit"},
        {"btnAdd", ""},
        {"panMainLevel", "Level"},
        {"btnAuthorize", ""},
        {"lblMsg", ""},
        {"btnException", ""},
        {"btnReport", ""},
        {"chkClearingDebit", "Clearing Debit"},
        {"lblCondition", "Condition"},
        {"chkTransferCredit", "Transfer Credit"},
        {"btnSave", ""},
        {"panMultiLevel", ""},
        {"lblExpression", "Expression"},
        {"lblStatus", "Status :"},
        {"lblTransaction", "Transaction"},
        {"btnAND", "AND"},
        {"lblLevelNameValue", ""},
        {"chkTransferDebit", "Transfer Debit"},
        {"lblAmount", "Amount"},
        {"btnDelete", "Delete"},
        {"lblNoOfPersons", "No of Persons"},
        {"chkCashCredit", "Cash Credit"},
        {"btnEdit", ""},
        {"btnLevelID", ""},
        {"lblStatusValue", ""},
        {"lblLevelName", "Level Name"},
        {"btnCancel", ""},
        {"btnDone", "Done"},
        {"btnDelete1", ""},
        {"lblLevelID", "Level ID"},
        {"lblSep3", ""},
        {"WARNING_INVALID_EXP","ImproperExpression!!!"},
        {"WARNING_MINIMUM_ENTRY","Minimum one level is required!!!"},
        {"WARNING_ATLEAST_TRANSACTION","Atleast one option should be selected for transaction!!!"},
        {"WARNING_DUPLICATE_ENTRY","Duplicate Entry!!!"}
   };
}
