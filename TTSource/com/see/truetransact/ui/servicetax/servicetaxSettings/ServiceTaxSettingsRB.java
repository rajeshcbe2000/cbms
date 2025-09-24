/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenConfigRB.java
 * 
 * Created on Thu Jan 20 15:41:51 IST 2005
 */

package com.see.truetransact.ui.servicetax.servicetaxSettings;

import java.util.ListResourceBundle;

public class ServiceTaxSettingsRB extends ListResourceBundle {
    public ServiceTaxSettingsRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblSpace5", "     "},
        {"btnDelete", ""},
        {"btnClose", ""},
        {"lblServiceTaxId", "Service Tax Id"},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"lblFromDate", "With Effect Date"},
        {"btnAuthorize", ""},
        {"lblMsg", ""},
        {"btnException", ""},
        {"lbltaxRate", "Tax Rate"},
        {"btnNew", ""},
        {"lblEdCess", "Education cess"},
        {"lbltaxHead", "Service Tax Head"},
        {"btnSave", ""},
        {"btnCancel", ""},
        {"lblSpace3", "     "},
        {"lblStatus", "                      "},
        {"lbSpace2", "     "},
        {"lblSpace1", " Status :"},
        {"btnPrint", ""} ,
        {"lblHigherCess", "Secondary and  higher education cess"},
        {"cDialogOK", "OK"},
       // {"existingMsg", "TokenType with this Series Already Exists"},
      //  {"tokenTypeExistingMsg", "TokenType Selected Already Exists"},
      //  {"tokenNoMsg", "Token Number Entered should be greater than Zero"}

   };

}
