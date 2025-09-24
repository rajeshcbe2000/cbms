/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * LockRB.java
 */

package com.see.truetransact.ui.salaryrecovery;
import java.util.ListResourceBundle;
public class LockRB extends ListResourceBundle {
    public LockRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"btnClose", ""},
        {"lblEmployerRefNo","Employer Ref.No"},
        {"lblMemberName","Member Name"},
        {"lblMsg", ""},
        {"panTransaction", ""},
        {"btnEdit", ""},
        {"lblAccNo", "Account No."},
        {"btnSave", ""},
        {"lblStatus", "                      "},
        {"btnAccNo", ""},
        {"lblAmount", "Amount"},
        {"lblRemarks", "Remarks"},
        {"lblDeleteFlag","Delete"},
        {"btnNew", ""},
        {"lblProductId", "Product Id"},
        {"lblProdType", "Product Type"},
        {"btnCancel", ""},
        {"btnVer",""},
        {"rdoLock","LOCK"},
        {"rdoUnLock","UNLOCK"},
        {"rdoSpecificAccount","SPECIFIC ACCOUNT"},
        {"rdoAllAccounts","ALL ACCOUNTS"},
        {"btnProcess","PROCESS"},
        {"btnClear","Clear"}
    };
    
}
