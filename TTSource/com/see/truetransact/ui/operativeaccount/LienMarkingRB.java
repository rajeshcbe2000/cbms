/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * LienMarkingRB.java
 */

package com.see.truetransact.ui.operativeaccount;
import java.util.ListResourceBundle;
public class LienMarkingRB extends ListResourceBundle {
    public LienMarkingRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"btnUnLien", "UnLien"},
        {"btnClose", ""},
        {"lblAccountNumber", "Account Number"},
        {"lblLienAmount", "Lien Amount"},
        {"lblAccountHeadCode", ""},
        {"lblAccountHead", "Account Head"},
        {"lblClearBalance", "Available Balance"},
        {"lblClearBalance1", "Clear Balance"},
        {"btnLienNew", ""},
        {"lblMsg", ""},
        {"btnAccountNumber", ""},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"lblSpace3", "     "},
        {"lblStatus", "                      "},
        {"btnLienDelete", ""},
        {"lblSpace1", " Status :"},
        {"lblRemarks", "Remarks"},
        {"lblClearBalanceDisplay", ""},
        {"lblClearBalanceDisplay1", ""},
        {"lblCustomerName", "Customer Name"},
        {"btnDelete", ""},
        {"lblExistingLiensSum", "Sum of Existing Liens"},
        {"lblExistingFreezeSum", "Sum of Existing Freeze"},
        {"lblAccountHeadDesc", ""},
        {"btnEdit", ""},
        {"lblLienDate", "Lien Date"},
        {"btnLienSave", ""},
        {"lblLienAccountNumber", "Lien Account Number"},
        {"lblExistingLiensSumDisplay", ""},
        {"lblExistingFreezeSumDisplay", ""},
        {"btnNew", ""},
        {"lblProductID", "Product Id"},
        {"btnCancel", ""},
        {"btnPrint", ""},
        {"lblLienAccountHead", "Lien Account Head"},
        {"lblCustomerNameDisplay", ""},
        {"tblColumn1", "Sl No."}, 
        {"tblColumn2", "Lien Date"}, 
        {"tblColumn3", "LSl No."}, 
        {"tblColumn4", "Amount"}, 
        {"tblColumn5", "Lien A/C No."}, 
        {"tblColumn6", "Status"},
         {"tblColumn7", "LienStatus"},
        {"accNumberErrMsg", "Account Number should not be empty!!!"},
        {"lblBranchId", ""},
        
        {"lblLienProduct", "Lien Product"},
        {"lblLienCustName", ""},
        {"lblLienAccountHeadDesc", ""},
        {"btnLienAccountNumber", ""},
        
        {"lblLienSlNo", "LSl No."},
        {"lblLienSlNoDesc", ""},
        
        {"optionLblUnLien", "UnLien Remarks"},
        
        {"cDialogYes", "Yes"},
        {"cDialogNo", "No"},
        {"cDialogCancel", "Cancel"},
        {"LIENACTWARNING", "Please Enter the Lien Account No."},
        {"AMTWARNING", "Lien Amount exceeds the Available Balance..."},
        {"LIENDATEWARNING", "Selected Lien date should be after Account Opening Date."},
        {"NOROWWARNING", "Please Enter the Atleast One Record to be marked as Lien..."},
        {"existanceWarning", "The Amount for this Lien Account No. Already exist. Do you want to modify the Amount?"},
        {"ACCOUNTWARNING", "Do you really want to Change the Selected Account No?"},
        {"WARNING", "Date cannot be Post Date!!!"},
        {"ACCNTWARNING","lien Account Number Cannot be Empty"} 
 
   };

}
