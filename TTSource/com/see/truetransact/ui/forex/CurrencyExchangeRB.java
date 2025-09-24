/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * CurrencyExchangeRB.java
 */

package com.see.truetransact.ui.forex;
import java.util.ListResourceBundle;
public class CurrencyExchangeRB extends ListResourceBundle {
    public CurrencyExchangeRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblConvCurrency", "Conversion Currency"},
        {"btnClose", ""},
        {"lblTouristBankDetails", "Bank Details"},
        {"lblCustRemarlks", "Remarks"},
        {"lblTouristName", "Name"},
        {"lblCustGroup", "Customer Group"},
        {"lblBranchCode", "Branch Code"},
        {"lblTransCurrency", "Transaction Currency"},
        {"lblMsg", ""},
        {"lblDiaTotAmt", "Total Amount"},
        {"panTourist", " Tourist Info "},
        {"lblTransId", "Transaction ID"},
        {"lblDiaComm", "Commission"},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"lblValueDate", "Value Date"},
        {"lblExchangeRate", "Exchange Rate"},
        {"lblSpace3", "     "},
        {"panDiaCalc", " Calculation "},
        {"lblTouristInstrumentNo", "Instrument No"},
        {"lblDiaPaidAmt", "Transaction Amount"},
        {"lblStatus", "                      "},
        {"lblCustomerType", "Customer Type"},
        {"btnAcctNo", ""},
        {"rdoTransType_Deposit", "Deposit"},
        {"lblSpace1", " Status :"},
        {"btnDenomination", "Denomination"},
        {"lblRemarks", "Remarks"},
        {"btnDelete", ""},
        {"lblName", "Name"},
        {"lblDiaEquiAmt", "Equivalent Amount"},
        {"lblTouristRemarks", "Remarks"},
        {"lblTotalAmount", "Total Amount"},
        {"lblType", "Type"},
        {"btnEdit", ""},
        {"lblCommission", "Commission"},
        {"lblDiaCrossCcyRate", "Cross Ccy Rate"},
        {"rdoTransType_Withdrawal", "Withdrawal"},
        {"btnNew", ""},
        {"panAcct", " Customer Info "},
        {"btnCancel", ""},
        {"lblTouristInstrumentDt", "Instrument Date"},
        {"lblTouristPassportNo", "Passport No."},
        {"lblTransAmount", "Amount"},
        {"btnPrint", ""},
        {"lblTransType", "Transaction Type"},
        {"lblAcctNo", "Account No"},
        {"btnExport", "Export"},
        {"btnImport", "btnImport"},
        {"lblFromBranch", "Imported From Branch"},
        {"lblImpTrID", "Imported Transaction ID"}
        
   };

}
