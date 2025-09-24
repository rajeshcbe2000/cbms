/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TradingAcHeadRB.java
 *
 * Created on Mon Mar 09 16:05:07 IST 2015
 */

package com.see.truetransact.ui.trading.tradingachead;

import java.util.ListResourceBundle;

public class TradingAcHeadRB extends ListResourceBundle {
    public TradingAcHeadRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblHeadType", "Head Type"},
        {"lblAccountHead", "AC head"},
        {"lblAccountHeadDesc", "AC head desc"},
        {"lblMsg", ""},

        {"btnSave", ""},
        {"lblSpace3", "     "},
        {"lbSpace2", "     "},
        {"lblStatus", "                      "},
        {"lblSpace1", " Status :"},
        {"lblSpace5", "     "},
        {"btnDelete", ""},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"btnNew", ""},
        {"btnCancel", ""},
        {"btnPrint", ""} ,
        {"btnClose",""},
        {"btnAuthorize",""},
        {"btnException", ""},
   };
}
