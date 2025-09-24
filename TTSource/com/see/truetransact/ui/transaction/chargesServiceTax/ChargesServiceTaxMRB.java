/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 */
package com.see.truetransact.ui.transaction.chargesServiceTax;
import java.util.ListResourceBundle;
public class ChargesServiceTaxMRB extends ListResourceBundle {
    public ChargesServiceTaxMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtAccountClosingCharges", "AccountClosingCharges should not be empty!!!"},
        {"txtNoOfUnusedChequeLeafs", "NoOfUnusedChequeLeafs should not be empty!!!"},
        {"txtChargeDetails", "ChargeDetails should not be empty!!!"},
        {"txtPayableBalance", "PayableBalance should not be empty!!!"},
        {"cboProductID", "ProductID should be a proper value!!!"},
        {"txtAccountNumber", "AccountNumber should not be empty!!!"},
        {"txtInterestPayable", "InterestPayable should not be empty!!!"},
        {"txtAccountHead","Account Head Should not be empty..."},
        {"txtAmount","Amount Should not be empty..."},
        {"txtChargeDetails","Particulars Should not be empty..."}
   };
}
