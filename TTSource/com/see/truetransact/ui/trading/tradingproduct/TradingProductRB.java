/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TDSConfigRB.java
 * 
 * Created on Thu Feb 10 12:53:41 IST 2005
 */

package com.see.truetransact.ui.trading.tradingproduct;

import java.util.ListResourceBundle;

public class TradingProductRB extends ListResourceBundle {
    public TradingProductRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"rdoCutOff_No", "No"},
        {"btnClose", ""},
        {"btnAuthorize", ""},
        {"btnException", ""},
        {"lblMsg", ""},
        {"lblCutOff", "Include Cut off"},

        {"lblCutOff", "Tax"},
        {"lblTdsId", "Product ID"},
        {"lblEndDate", "Prod Desc"},
        {"btnSave", ""},
        {"lblSpace3", "     "},
        {"lbSpace2", "     "},
        {"lblStatus", "                      "},
        {"lblSpace1", " Status :"},
        {"lblStartDate", "UnitType*"},
        {"lblSpace5", "     "},
        {"btnDelete", ""},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"btnNew", ""},
        {"lblCutOfAmount", "Group Name*"},
        {"lblPercentage", "Item AcHd"},
        {"lblScope", "Tax%"},
        {"btnCancel", ""},
        {"rdoCutOff_Yes", "Yes"},
        {"lblTdsCreditAchdId", "Date"},
        {"btnPrint", ""} 

   };

}
