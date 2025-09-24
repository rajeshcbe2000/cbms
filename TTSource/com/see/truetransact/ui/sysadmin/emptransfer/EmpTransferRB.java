/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 */
package com.see.truetransact.ui.sysadmin.emptransfer;
import java.util.ListResourceBundle;
public class EmpTransferRB extends ListResourceBundle {
    public EmpTransferRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblEmpId", "Employee ID"},
        {"lblTranBran", "Transfer Branch"},
        {"lblLastDayCurrBran", "Last Working Day"},
        {"lblRoleCurr", "Role In Current Branch"},
        {"lblCurrBran", "Current Branch"},
        {"lblDoj", "Date Of Joining"},
        {"lblRoleTrans", "Role In Transferred Branch"},
        {"rdoReq_Yes","Request"},
        {"rdoOff_Yes","Official"}
    };
}
