/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BackDatedTransactionMRB.java
 * 
 * Created on March 12, 2014, 3:43 PM 2014  
 */
package com.see.truetransact.ui.batchprocess.BackDatedTransaction;

import java.util.ListResourceBundle;

public class BackDatedTransactionMRB extends ListResourceBundle {

    public BackDatedTransactionMRB() {
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtAccountNo", "AccountNo should not be empty!!!"},
        {"txtAccountHeadValue", "Account Head should not be empty!!!"},
        {"txtParticulars", "Particulars should not be empty!!!"},
        {"txtNarration", "Member Name/Narration should not be empty!!!"},
        {"txtInstrumentNo2", "InstrumentNo2 should not be empty!!!"},
        //{"txtTokenNo", "TokenNo should not be empty!!!"},
        {"cboProductType", "ProductType should be a proper value!!!"},
        {"cboProductID", "ProductID should be a proper value!!!"},
        {"txtInstrumentNo1", "InstrumentNo1 should not be empty!!!"},
        {"tdtInstrumentDate", "InstrumentDate should not be empty!!!"},
        {"txtAmount", "Amount should not be zero or empty!!!"},
        {"cboInstrumentType", "InstrumentType should be a proper value!!!"},
        {"cboCurrency", "Currency should be a proper value!!!"}
    };

}
