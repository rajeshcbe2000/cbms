/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * OutwardClearingMRB.java
 */
package com.see.truetransact.ui.transaction.clearing.outward;
import java.util.ListResourceBundle;
public class OutwardClearingMRB extends ListResourceBundle {
    public OutwardClearingMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtAmountPISD", "AmountPISD should not be empty!!!"},
        {"txtConvAmt", "Converted Amount should not be empty!!!"},
        {"txtAmountID", "AmountID should not be empty!!!"},
        {"cboClearingTypeID", "ClearingTypeID should be a proper value!!!"},
        {"txtBranchCodeID", "BranchCodeID should not be empty!!!"},
        {"txtDrawerNameID", "DrawerNameID should not be empty!!!"},
        {"txtDrawerAccNoID", "DrawerAccNoID should not be empty!!!"},
        {"cboInstrumentTypeID", "InstrumentTypeID should be a proper value!!!"},
        {"cboInstrDetailsCurrency","Instrument Details Currency should be a proper value!!!"},
        {"cboPayInSlipCurrency","Pay In Slip Currency should be a proper value!!!"},
        {"cboProdIdPISD", "ProdIdPISD should be a proper value!!!"},
        {"txtBankCodeID", "BankCodeID should not be empty!!!"},
        {"dtdInstrumentDtID", "InstrumentDtID should not be empty!!!"},
        {"cboScheduleNo", "ScheduleNo should be a proper value!!!"},
        {"txtRemarksPISD", "RemarksPISD should not be empty!!!"},
        {"txtAccNoPISD", "AccNoPISD should not be empty!!!"},
        {"txtBatchIdOC", "BatchIdOC should not be empty!!!"},
        {"txtRemarksID", "RemarksID should not be empty!!!"},
        {"txtInstrumentNo1ID", "InstrumentNo1ID should not be empty!!!"},
        {"txtInstrumentNo2ID", "InstrumentNo2ID should not be empty!!!"},
        {"txtPayeeNameID", "PayeeNameID should not be empty!!!"} 

   };

}
