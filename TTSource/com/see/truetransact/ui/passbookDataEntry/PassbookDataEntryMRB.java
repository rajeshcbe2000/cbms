/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PassbookDataEntryMRB.java
 * 
 /**
 *
 * @author anjuanand
 */
package com.see.truetransact.ui.passbookDataEntry;

import java.util.ListResourceBundle;

public class PassbookDataEntryMRB extends ListResourceBundle {

    public PassbookDataEntryMRB() {
    }

    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"cboBankHead", "Please enter Bank Head!!!"},
        {"tdtDate", "Please select Date!!!"},
        {"rdoTransactionType_Debit", "Please select if Payment!!!"},
        {"rdoTransactionType_Credit", "Please select if Receipt!!!"},
        {"txtTransactionID", "Please enter Transaction ID!!!"},
        {"cboInstrumentType", "Please select Instrument Type!!!"},
        {"txtInstrumentNo1", "Please enter Instrument No!!!"},
        {"txtInstrumentNo2", "Please enter Instrument No!!!"},
        {"tdtInstrumentDate", "Please select Instrument Date!!!"},
        {"txaParticulars", "Please enter Particulars!!!"},
        {"txtAmount", "Please enter Amount!!!"}
    };
}
