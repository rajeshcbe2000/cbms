/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * CurrencyExchangeMRB.java
 */

package com.see.truetransact.ui.forex;
import java.util.ListResourceBundle;
public class CurrencyExchangeMRB extends ListResourceBundle {
    public CurrencyExchangeMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtTransAmount", "TransAmount should not be empty!!!"},
        {"txtTouristName", "TouristName should not be empty!!!"},
        {"txtCustRemarks", "CustRemarks should not be empty!!!"},
        {"txtAcctNo", "AcctNo should not be empty!!!"},
        {"txtDiaCrossCcyRate", "DiaCrossCcyRate should not be empty!!!"},
        {"txtBranchCode", "BranchCode should not be empty!!!"},
        {"txtTouristBankDetails", "TouristBankDetails should not be empty!!!"},
        {"txtExchangeRate", "ExchangeRate should not be empty!!!"},
        {"txtTouristPassportNo", "TouristPassportNo should not be empty!!!"},
        {"rdoTransType_Deposit", "TransType should be selected!!!"},
        {"cboTransCurrency", "TransCurrency should be a proper value!!!"},
        {"txtCustGroup", "CustGroup should not be empty!!!"},
        {"txtTouristRemarks", "TouristRemarks should not be empty!!!"},
        {"txtTotalAmount", "TotalAmount should not be empty!!!"},
        {"txtRemarks", "Remarks should not be empty!!!"},
        {"txtTouristInstrumentNo", "TouristInstrumentNo should not be empty!!!"},
        {"txtDiaTotAmt", "DiaTotAmt should not be empty!!!"},
        {"txtDiaEquiAmt", "DiaEquiAmt should not be empty!!!"},
        {"txtType", "Type should not be empty!!!"},
        {"txtName", "Name should not be empty!!!"},
        {"txtDiaComm", "DiaComm should not be empty!!!"},
        {"cboConvCurrency", "ConvCurrency should be a proper value!!!"},
        {"txtCommission", "Commission should not be empty!!!"},
        {"txtDiaTransAmt", "DiaTransAmt should not be empty!!!"},
        {"txtValueDate", "ValueDate should not be empty!!!"},
        {"cboCustType", "CustType should be a proper value!!!"},
        {"txtTransId", "TransId should not be empty!!!"},
        {"tdtTouristInstrumentDt", "TouristInstrumentDt should not be empty!!!"},
        {"txtFromBranch", "Importing data"},
        {"txtImpTrID", "Importing data"}

   };

}
