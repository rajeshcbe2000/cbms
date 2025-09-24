/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * LevelControlMRB.java
 */
package com.see.truetransact.ui.sysadmin.levelcontrol;
import java.util.ListResourceBundle;
public class LevelControlMRB extends ListResourceBundle {
    public LevelControlMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtDescription", "Description should not be empty!!!"},
        {"txtName", "Name should not be empty!!!"},
        {"txtLevelID", "LevelID should not be empty!!!"},
        {"txtClearingDebit", "ClearingDebit should not be empty!!!"},
        {"txtClearingCredit", "ClearingCash should not be empty!!!"},
        {"txtCashDebit", "CashDebit should not be empty!!!"},
        {"txtTransferDebit", "TransferDebit should not be empty!!!"},
        {"txtCashCredit", "CashCredit should not be empty!!!"},
        {"txtTransferCredit", "TransferCash should not be empty!!!"} ,
        {"chkSingleWindow", "SingleWindow should be selected!!!"} ,
        {"cboName", "Name should not be empty!!!"}
   };

}
