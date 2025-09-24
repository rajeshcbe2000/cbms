/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MultiLevelControlMRB.java
 * 
 * Created on Mon Sep 13 11:28:21 GMT+05:30 2004
 */

package com.see.truetransact.ui.sysadmin.levelcontrol.multilevel;

import java.util.ListResourceBundle;

public class MultiLevelControlMRB extends ListResourceBundle {
    public MultiLevelControlMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtLevelID", "LevelID should not be empty!!!"},
        {"txtNoOfPersons", "NoOfPersons should not be empty!!!"},
        {"txtAmount", "Amount should not be empty!!!"},
        {"chkClearingCredit", "ClearingCredit should be selected!!!"},
        {"chkTransferCredit", "TransferCredit should be selected!!!"},
        {"txtExpression", "Expression should not be empty!!!"},
        {"cboCondition", "Condition should be a proper value!!!"},
        {"chkCashDebit", "CashDebit should be selected!!!"},
        {"chkCashCredit", "CashCredit should be selected!!!"},
        {"chkClearingDebit", "ClearingDebit should be selected!!!"},
        {"chkTransferDebit", "TransferDebit should be selected!!!"} 

   };

}
