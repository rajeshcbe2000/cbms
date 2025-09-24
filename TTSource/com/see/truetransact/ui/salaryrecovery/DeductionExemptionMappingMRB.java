/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * DeductionExemptionMappingMRB.java
 */

package com.see.truetransact.ui.salaryrecovery;
import java.util.ListResourceBundle;
public class DeductionExemptionMappingMRB extends ListResourceBundle {
    public DeductionExemptionMappingMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
      
        {"cboProdId", "ProdId should be a proper value!!!\n"},
        {"cboProdType", "ProdType should be a proper value!!!\n"},
        {"txtAccNo", "AccNo should be a proper value!!!\n"},
        {"txtEmployerRefNo" , "EmployerRefNo should not be empty!!!\n"},
        {"cboExemptionMode" , "Exemption Mode should be a proper value !!!\n"},

   };

}
