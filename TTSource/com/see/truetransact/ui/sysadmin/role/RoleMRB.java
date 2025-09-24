/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RoleMRB.java
 * 
 * Created on Wed May 11 14:42:08 IST 2005
 */

package com.see.truetransact.ui.sysadmin.role;

import java.util.ListResourceBundle;

public class RoleMRB extends ListResourceBundle {
    public RoleMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtGroupID", "GroupID should not be empty!!!"},
        {"txtLevelID", "LevelID should not be empty!!!"},
        {"txtClearingDebit", "ClearingDebit should not be empty!!!"},
        {"txtTransCredit", "TransCredit should not be empty!!!"},
        {"txtCashDebit", "CashDebit should not be empty!!!"},
        {"cboRoleId", "RoleID should not be empty!!!"},
        {"txtLevelName", "LevelName should not be empty!!!"},
        {"txtCashCredit", "CashCredit should not be empty!!!"},
        {"chkAccAllBran", "AccAllBran should be selected!!!"},
        {"txtGroupDesc", "GroupDesc should not be empty!!!"},
        {"txtLevelDesc", "LevelDesc should not be empty!!!"},
        {"txtRoleDesc", "RoleDesc should not be empty!!!"},
        {"txtTransDebit", "TransDebit should not be empty!!!"},
        {"txtClearingCredit", "ClearingCredit should not be empty!!!"} ,
        
        {"txtLevelIDForeign", "LevelIDForeign should not be empty!!!"} ,
        {"txtLevelNameForeign", "LevelNameForeign should not be empty!!!"} ,
        {"txtLevelDescForeign", "LevelDescForeign should not be empty!!!"} ,
        {"txtCashCreditForeign", "CashCreditForeign should not be empty!!!"} ,
        {"txtCashDebitForeign", "CashDebitForeign should not be empty!!!"} ,
        {"txtClearingCreditForeign", "ClearingCreditForeign should not be empty!!!"} ,
        {"txtClearingDebitForeign", "ClearingDebitForeign should not be empty!!!"} ,
        {"txtTransCreditForeign", "TransCreditForeign should not be empty!!!"} ,
        {"txtTransDebitForeign", "tTransDebitForeign should not be empty!!!"} ,
        {"cboRoleHierarchy", "Role Hierarchy should be selected!!!"},
        {"chkHierarchyAllowed", "Hierarchy Allowed should be Selected!!!"},
   };

}
