/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * Author   : Chithra
 * Location : Thrissur
 * Date of Completion : 15-06-2015
 */
package com.see.truetransact.ui.product.commission;

import java.util.ListResourceBundle;

public class DailyCommissionRB extends ListResourceBundle {
    public DailyCommissionRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"btnClose", ""},
        {"btnAuthorize", ""},
        {"btnException", ""},
        {"lblMsg", ""},
        {"lblCommId", "Commission ID"},
        {"lblProductType", "Product Type"},
        {"btnSave", ""},
        {"lblSpace3", "     "},
        {"lbSpace2", "     "},
        {"lblStatus", "                      "},
        {"lblSpace1", " Status :"},
        {"lblcommType", "Commission Type"},
        {"lblSpace5", "     "},
        {"btnDelete", ""},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"btnNew", ""},
        {"lblPeriod", "Period"},
        {"lblAmt", "Amount"},
        {"lblPenal", "Penal"},
        {"btnCancel", ""},
        {"btnPrint", ""} 

   };

}
