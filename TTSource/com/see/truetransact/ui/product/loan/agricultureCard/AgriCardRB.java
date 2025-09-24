/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LoanProductRB.java
 * 
 * Created on Tue May 11 16:05:22 IST 2004
 */

package com.see.truetransact.ui.product.loan.agricultureCard;

import java.util.ListResourceBundle;

public class AgriCardRB extends ListResourceBundle {
    public AgriCardRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"btnReject", ""},
        {"panOtherBank", "Other Bank"},
        {"btnEdit", ""},
        {"lblBankShortName", "Bank Short Name"},
        {"btnNew", ""},
        {"lblBankCode", "Bank Code"},
        {"btnCancel", ""},
        {"lblAddress", "Address"},
        {"lblState", "State"},
        {"panOtherBankBranch", "Other Bank Branch"},
        {"btnPrint", ""},
        {"lblAgriCardType", "Card Type"},
        {"lblAgriCardValidity", "Card Validity"},
        {"lblProdType", "Product Type"},
        {"lblProdId", "Product ID"},
        {"lblSBInterest", "SB Interest Given"},
        {"tblColumn1", "Card Type"},
        {"tblColumn2", "Card Period"},
//        {"tblColumn3", "Card Period Freq"},
        {"tblColumn4", "ProdType"},
        {"tblColumn5", "ProdId"},
        {"cRadio_SB_Yes","Y"},
        {"cRadio_SB_No","N"},
        
        {"btnAgriNew", "New"},
        {"btnAgriSave", "Save"},
        {"btnAgriDelete", "Delete"}

   };

}
