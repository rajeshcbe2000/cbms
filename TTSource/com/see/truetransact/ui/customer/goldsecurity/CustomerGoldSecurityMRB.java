/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SecurityInsuranceMRB.java
 * 
 * Created on Wed Jan 12 18:34:35 IST 2005
 */

package com.see.truetransact.ui.customer.goldsecurity;

import com.see.truetransact.ui.customer.security.*;
import java.util.ListResourceBundle;

public class CustomerGoldSecurityMRB extends ListResourceBundle {
    public CustomerGoldSecurityMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cboInsuranceNo", "Enter the Insurance Number."},
        {"txtSecurityValue", "Enter the Security Value."},
        {"txtPolicyAmt", "Enter the Policy Amount."},
        {"tdtPolicyDate", "Enter the Policy Date."},
        {"txtTotalSecurity_Value", "Enter the Total Security Value"},
        {"txtRemark_Insurance", "Enter the Remark."},
        {"txtAvalSecVal", "Enter the Available Security Value."},
        {"txtParticulars", "Enter the Particulars."},
        {"txtPolicyNumber", "Enter the Policy Number."},
        {"txtPremiumAmt", "Enter the Premium Amount."},
        {"cboSecurityCate", "Enter the Security Category."},
        {"chkSelCommodityItem", "SelCommodityItem should be selected!!!"},
        {"tdtAson", "Enter the Ason date."},
        {"cboForMillIndus", "Enter the For MillIndus."},
        {"cboStockStateFreq", "Enter the Stock State Frequency."},
        {"tdtDateCharge", "Enter the Date of Charge."},
        {"txtSecurityNo", "Enter the Security No."},
        {"cboSecurityNo_Insurance", "Enter the Insurance's Security No."},
        {"rdoSecurityType_Primary", "Enter the Security Type."},
        {"txtInsureCompany", "Enter the Insurance Company."},
        {"tdtDateInspection", "Enter the Date of Inspection."},
        {"tdtExpityDate", "Enter the Expity Date."},
        {"cboNatureCharge", "Enter the Nature of Charge."},
        {"cboNatureRisk", "Enter the Risk Nature."},
        {"tdtFromDate", "Enter the From Date."},
        {"tdtToDate", "Enter the To Date."} ,
        {"txtNetWeight", "Enter the NetWeight of the ornaments."} ,
        {"txtGrossWeight", "Enter the GrossWeight of the ornaments."} 
        
   };

}
