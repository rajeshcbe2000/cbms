/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * EmpSalaryStructureRB.java
 * 
 * Created on Sat Feb 26 14:00:49 GMT+05:30 2011
 */

package com.see.truetransact.ui.common;

import java.util.ListResourceBundle;

public class EmpSalaryStructureRB extends ListResourceBundle {
    public EmpSalaryStructureRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"rdoPercentOrFixed_Fixed", "FIXED"},
        {"btnClose", ""},
        {"cDialogYes", "Yes"},
        {"CDialogNo", "No"},
        {"lblSalFromDate", "From Date"},
        {"lblAllowanceID", "Allowance ID"},
        {"btnTabSave", "Save"},
        {"lblPercentOrFixed", "Allowance In"},
        {"btnAuthorize", ""},
        {"btnException", ""},
        {"lblMsg", ""},
        {"lblSpace4", "     "},
        {"rdoBasedOnBasic_Yes", "Yes"},
        {"lblFromAmount", "From Amount"},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"panSalaryMasterDetails", "Allowance/Deduction  Details"},
        {"lblSpace3", "     "},
        {"lblStatus", "                      "},
        {"lblSpace1", " Status :"},
        {"rdoEarnOrDed_Earning", "Earn"},
        {"btnView", ""},
        {"rdoBasedOnBasic_No", "No"},
        {"lblBasedOnBasicYesNo", "Using Basic"},
        {"panSalaryDetails", "SalaryDetails"},
        {"lblSpace5", "     "},
        {"btnDelete", ""},
        {"lblEarnOrDeduction", "Earn/Ded"},
        {"btnTabDelete", "Delete"},
        {"panGroupData", "Bank/Branch Details"},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"lblAllowanceType", "AllowanceType"},
        {"btnTabNew", "New"},
        {"lblMaxAmount", "Max Amount"},
        {"btnNew", ""},
        {"lblToAmount", " To Amount"},
        {"rdoPercentOrFixed_Percent", "PERCENT"},
        {"btnCancel", ""},
        {"rdoEarnOrDed_Deduction", "Ded"},
        {"lbSalToDate", "To Date"},
        {"btnPrint", ""},
        {"tblZonal1", "Select"},
        {"tblZonal2", "Zonal ID"},
        {"tblBranch1", "Select"},
        {"tblBranch2", "Branch ID"},
        {"tblGrade1", "Select"},
        {"tblGrade2", "Grade Type"},
        {"tblPopulation1", "Select"},
        {"tblPopulation2", "PopuLation Type"},
        {"tblSalStruct1", "SL NO"},
        {"tblSalStruct2", "From Date"},
        {"tblSalStruct3", "To Date"},
        {"tblSalStruct4", "From Amount"},
        {"tblSalStruct5", "To Ammount"},
        {"lblAllowanceAmount1", "Amount"},
        {"lblAllowanceAmount2", "Percent"}
        

   };

}
