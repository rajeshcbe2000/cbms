/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EditMigratedDataRB.java
 * 
 * Created on Wed Oct 31 13:19:32 IST 2012
 */

package com.see.truetransact.ui.transaction.editMigratedData;

import java.util.ListResourceBundle;

public class EditMigratedDataRB extends ListResourceBundle {
    public EditMigratedDataRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblCustomerIdCr", "Account No"},
        {"panDepositDetails", "Deposit Account Details"},
        {"lblTotDiscountAmt2", "Penal Amount"},
        {"lblTotInterest", "Rate"},
        {"btnClear", "Clear"},
        {"lblMsg", ""},
        {"panLoanDetails", "Loan Account Details"},
        {"lblAribitrationAmt6", "Net Amount"},
        {"btnReprintClose", "Close"},
        {"btnUpdate", "UPDATE"},
        {"btnCustomerIdFileOpenCr", ""},
        {"lblStatus", "                      "},
        {"lblAribitrationAmt", "Total Interest Drawn"},
        {"lblLoanIntCalcDt", "Last Interest Calc Date"},
        {"lblTotDiscountAmt", "Maturity Amount"},
        {"panMDSDetails", "MDS Chittal Details"},
        {"lblAribitrationAmt2", "Interest Frequency"},
        {"panAccountDetails", "Product Details"},
        {"lblSpace", " Status :"},
        {"lblNoticeAmt", "Total Interest Periodic Interest"},
        {"lblAribitrationAmt1", "Total Credit"},
        {"lblMemberName", ""},
        {"lblProdId", "Product Id"},
        {"lblTotInstAmt", "Last Interest Application Date"},
        {"lblTotInstAmt2", "Chit Start Date"},
        {"lblTotBonusAmt2", "Bonus Amount"},
        {"lblTotBonusAmt", "Next Interest Application Date"},
        {"lblProdType", "Product Type"} 

   };

}
