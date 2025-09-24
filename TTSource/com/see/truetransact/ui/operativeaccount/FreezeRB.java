/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 */
package com.see.truetransact.ui.operativeaccount;
import java.util.ListResourceBundle;
public class FreezeRB extends ListResourceBundle {
    public FreezeRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblClearBalanceDisplay", ""},
        {"lblClearBalanceDisplay1", ""},
        {"lblCustomerName", "Customer Name"},
        {"lblExistingFreezesSumDisplay", ""},
        {"lblExistingLienSumDisplay", ""},
        {"btnFreezeNew", ""},
        {"btnFreezeSave", ""},
        {"lblAccountNumber", "Account Number"},
        {"rdoCreditDebit_Debit", "Debit"},
        {"lblAccountHead", "Account Head"},
        {"lblAccountHeadCode", ""},
        {"lblAccountHeadDesc", ""},
        {"lblType", "Type"},
        {"lblExistingLienSum", "Sum of Existing Lien"},
        {"lblExistingFreezesSum", "Sum of Existing Freezes"},
        {"lblClearBalance", "Available Balance"},
        {"lblClearBalance1", "Clear Balance"},
        {"lblAmount", "Amount"},
        {"btnAccountNumber", ""},
        {"lblCreditDebit", "Credit / Debit"},
        {"btnFreezeDelete", ""},
        {"btnUnFreeze", "UnFreeze"},
        {"lblProductID", "Product Id"},
        {"rdoCreditDebit_Credit", "Credit"},
        {"lblFreezeDate", "Freeze Date"},
        {"lblCustomerNameDisplay", ""},
        {"lblRemarks", "Remarks"}, 
        {"tblColumn1", "Sl No."}, 
        {"tblColumn2", "Type"}, 
        {"tblColumn3", "FSl No."}, 
        {"tblColumn4", "Amount"}, 
        {"tblColumn5", "Freeze Date"}, 
        {"tblColumn6", "Debit/Credit"}, 
        {"tblColumn7", "Status"}, 
        {"tblColumn8", "FreezeStatus"},
        {"lblFreezeSlNo", "Sl No."},
        {"lblFreezeSlNoDesc", ""},
        {"lblDate", "Date"},
        
        {"accNumberErrMsg", "Account Number should not be empty!!!"},
        {"optionLblUnFreeze", "UnFreeze Remarks"} ,
        
        {"cDialogYes", "Yes"},
        {"cDialogNo", "No"},
        {"cDialogCancel", "Cancel"},
        {"FREEZEDATEWARNING", "Selected Freeze date should be after Account Opening Date."},
        {"AMTWARNING", "Freeze exceeds the Available Balance..."},
        {"NOROWWARNING", "Please Enter the Atleast One Record to be marked as Freezed..."},
        {"existanceWarning", "This Freeze type for this Account No. Already exist. Do you want to modify the Amount?"},
        {"completeWarning", "This Account is Completely Freezed. No More Freze Can be Accepted."},
        {"ACCOUNTWARNING", "Do you really want to Change the Selected Account No?"},
        {"PARTWARNING", "If u Want To Freeze Full Amount Choose COMPLETE"},
        {"COMPWARNING", "Choose the PARTIAL Option If u Dont Want To Freeze Complete Amount"}
            
   };

}
