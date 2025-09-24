/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * OutwardClearingRB.java
 */
package com.see.truetransact.ui.transaction.clearing.outward;

import java.util.ListResourceBundle;
public class OutwardClearingRB extends ListResourceBundle {
    public OutwardClearingRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"btnEndPISD", "End Pay in Slip"},
        {"btnClose", ""},
        {"lblOptModePISD", "Operation Mode"},
        {"lblInstrumentNo1ID", "Instrument No  I"},
        {"lblInstrumentNo2ID", "Instrument No  II"},
        {"lblAccNoPISD", "Account Number"},
        {"lblMsg", ""},
        {"lblProdIdPISD", "Product Id"},
        {"lblSpace2", "     "},
        {"lblSpace3", "     "},
        {"lblRemarksID", "Remarks"},
        {"lblOpeningDateValuePISD", ""},
        {"lblRemarksPISD", "Remarks"},
        {"lblBranchCodeID", "Branch Code"},
        {"lblTotalAmountValueID", ""},
        {"btnNewPISD", "New"},
        {"btnView", "View"},
        {"lblAccRemarksValuePISD", ""},
        {"btnNewID", "New"},
        {"lblAmountPISD", "Amount"},
        {"btnEndID", "End Instruments"},
        {"btnEdit", ""},
        {"lblDrawerAccNoID", "Drawer Acct No"},
        {"lblCategoryPISD", "Category"},
        {"btnPrint", ""},
        {"lblOptModeValuePISD", ""},
        {"lblTotalAmountPISD", "Total Pay In Slip Amount"},
        {"lblTotalAmountValuePISD", ""},
        {"btnSavePISD", "Save"},
        {"panPayInSlipDetOC", "Pay In Slip Details"},
        {"btnDeletePISD", "Delete"},
        {"panInstrDetOC", "Instrument Details"},
        {"lblAccHolderNamePISD", "Acc Holder's Name"},
        {"lblTotalAmountID", "Total Instruments' Amount"},
        {"lblInstrumentDtID", "Instrument Date"},
        {"btnSave", ""},
        {"lblDrawerNameID", "Drawer Name"},
        {"lblStatus", "                      "},
        {"lblBatchIdOC", "Batch Id"},
        {"lblConstitutionPISD", "Constitution"},
        {"lblCategoryValuePISD", ""},
        {"lblAmountID", "Amount"},
        {"btnDelete", ""},
        {"btnAccNoPISD", ""},
        {"lblAccHeadPISD", "Account Head"},
        {"lblAccHeadValuePISD", ""},
        {"lblSpace", " Status :"},
        {"lblAccHolderNameValuePISD", ""},
        {"btnNew", ""},
        {"lblInstrumentTypeID", "Instrument Type"},
        {"lblInstrDetailsCurrency", "Currency"},
        {"lblAccRemarksPISD", "Remarks"},
        {"lblPayeeNameID", "Payee Name"},
        {"lblOpeningDatePISD", "Opening Date"},
        {"lblConstitutionValuePISD", ""},
        {"btnDeleteID", "Delete"},
        {"btnCancel", ""},
        {"panMainOC", ""},
        {"btnSaveID", "Save"},
        {"lblClearingTypeID", "Clearing Type"},
        {"lblBankCodeID", "Bank Code"}, 
        {"tblLblSN", "S.N."},
        {"tblLblClID", "ClearingID"},
        {"tblLblDAN", "InstrumentNo"},
        {"tblLblAmt", "Amount"},
        {"tblLblPISID", "PayInSlip ID"},
        {"tblLblPID", "Prod ID"},
        {"tblLblAN", "Account No"},
        {"tblLblTB", "Total Balance"},
        {"tblLblCB", "Clear Balance"},
        {"tblLblSD", "Shadow Debit"},
        {"tblLblSC", "Shadow Credit"},
        {"lblScheduleNo","Schedule No"},
        {"lblClearingDate","Clearing Date"},
        {"dialogForDiffAmt", "The Pay-InSlip Amount and Total Instrument Amount does not tally"},
        {"warningMessageOK", "Ok"},
        {"WARNING_INVALIDAMT","Invalid Amount"},
        {"WARNING_MINENTRY","There should be atleast one entry in batch"},
        {"WARNING_COUNTTALLY","Pay-in-slip & Outward Clearing entries are more than one entry. Put it in different Batch."},
        {"REMARK"," remark"},
        {"B_C_TYPE", "Bounce Clearing Type:"},
        {"BOUNCETYPE", "No Data Available for the Bounce Clearing Type."},
        {"LOCAL_DELETE","Locally Deleted entry for Batch"},
        {"BOUNCECLEARINGTYPE", "Enter the Clearing Type For the Bounce: "},
        {"lblBookedInstr","No Instrument Booked"},
        {"lblValNoInstrBooked"," "},
        {"lblBookedAmt"," BookedAmount"},
        {"lblValAmount"," "},
        {"lblBankNameID"," BankName"},
        {"lblBankNameIDValue"," "},
        {"lblBranchNameID","BranchName "},
        {"lblBranchNameIDValue"," "},
        
        {"WARNING_CREATE","Create a entry in Outward Tally for the selected Clearing Type"}
    };
}
