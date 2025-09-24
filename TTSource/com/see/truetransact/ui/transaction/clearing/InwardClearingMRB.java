/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InwardClearingMRB.java
 * 
 * Created on Tue May 04 16:17:02 IST 2004
 */

package com.see.truetransact.ui.transaction.clearing;

import java.util.ListResourceBundle;

public class InwardClearingMRB extends ListResourceBundle {
    public InwardClearingMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtAmount", "Amount should not be empty!!!"},
        {"txtInstrumentNo2", "InstrumentNo2 should not be empty!!!"},
        {"cboCurrency", "Currency should be a proper value!!!"},
        {"tdtInstrumentDate", "InstrumentDate should not be empty!!!"},
//        {"cboBranchCode", "BranchCode should be a proper value!!!"},
          {"txtBranchCodeID", "BranchCode should be a proper value!!!"},
        {"cboInstrumentTypeID", "InstrumentTypeID should be a proper value!!!"},
        {"txtPayeeName", "PayeeName should not be empty!!!"},
        {"txtInstrumentNo1", "InstrumentNo1 should not be empty!!!"},
//        {"cboBankCode", "BankCode should be a proper value!!!"},
         {"txtBankCodeID", "BankCode should be a proper value!!!"},
        {"txtAccountNumber", "AccountNumber should not be empty!!!"},
        {"cboProdId", "ProdId should be a proper value!!!"},
        {"txtCovtAmount", "CovtAmount should not be empty!!!"},
        {"txtScheduleNumber", "ScheduleNumber should not be empty!!!"},
        {"cboClearingType", "ClearingType should be a proper value!!!"}, 
        {"cboProductType", "ProductType should be a proper value!!!"} ,
        {"cboScheduleNo", "ScheduleNo should be a proper value!!!"}

   };

}
