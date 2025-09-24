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

package com.see.truetransact.ui.trading.tradingsuppliermaster;

import java.util.ListResourceBundle;

public class TradingSupplierMasterRB extends ListResourceBundle {
    public TradingSupplierMasterRB(){
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

        {"lblSupplierID", "Supplier ID"},
        {"lblName","Name"},
        {"lblAddress","Address"},
        {"lblTdsId", "SuplierId"},
        {"lblSundryCreditors","Sundry Creditors"},
        {"lblDate", "Date"},
        {"btnSave", ""},
        {"lblSpace3", "     "},
        {"lbSpace2", "     "},
        {"lblStatus", "                      "},
        {"lblSpace1", " Status :"},
        {"lblStartDate", "Financial Year Start Date"},
        {"lblSpace5", "     "},
        {"btnDelete", ""},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"btnNew", ""},
        {"lblCutOfAmount", "Cut off Amount"},
        {"lblphone", "Phone"},
        {"lblScope", "Scope"},
        {"btnCancel", ""},
        {"rdoCutOff_Yes", "Yes"},
        {"lblTdsCreditAchdId", "Tds Cr A/c Head Id"},
        {"btnPrint", ""},
        {"btnName",""},
        {"btnSundryCreditors",""},
        {"btnPurchase",""},
        {"lblphone","Phone"},
        {"lblCSTNO","CST NO"},
        {"lbKGSTNO","KGST NO"},
        {"lbTinNo","Tin No"}
        
            

   };

}
