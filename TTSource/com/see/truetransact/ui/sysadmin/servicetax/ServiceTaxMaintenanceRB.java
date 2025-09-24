/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ServiceTaxMaintenanceRB.java
 * 
 * Created on Mon Jan 17 17:30:09 IST 2005
 */

package com.see.truetransact.ui.sysadmin.servicetax;

import java.util.ListResourceBundle;

public class ServiceTaxMaintenanceRB extends ListResourceBundle {
    public ServiceTaxMaintenanceRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblToPeriod", "To Period"},
        {"btnClose", ""},
        {"btnTabSave", "Save"},
        {"lblGroupId", "Interest Rate Group ID"},
        {"btnAuthorize", ""},
        {"lblMsg", ""},
        {"btnException", ""},
        {"lblSpace4", "     "},
        {"lblLimitAmt", "Limit Amount"},
        {"lblInterExpiry_Per", "%"},
        {"lblStatementPenal", "Total Tax"},
        {"lblFromAmount", "From Amount"},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"lblSpace3", "     "},
        {"lblStatement_Per", "%"},
        {"lblProdType", "Product Type"},
        {"lblPenalInterest", "Cess1"},
        {"lblStatus", "                      "},
        {"lblSpace1", " Status :"},
        {"lblODI_Per", "%"},
        {"btnDelete", ""},
        {"lblAgainstInterest", "Against Clearing Interest"},
        {"btnTabDelete", "Delete"},
        {"lblRateInterest", "Service Tax"},
        {"panGroupData", "Group Data"},
        {"lblGroupIdDesc", ""},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"lblInterExpiry", "Interest for Expiry of Limit"},
        {"lblDate", "From Date"},
        {"lblSpaces", "     "},
        {"lblODI", "Cess2"},
        {"btnTabNew", "New"},
        {"btnNew", ""},
        {"lblToDate", "To Date"},
        {"lblFromPeriod", "From Period"},
        {"lblToAmount", "To Amount"},
        {"lblPenalInterestPer", "%"},
        {"btnCancel", ""},
        {"lblGroupName", "Interest Rate Group Name"},
        {"lblAgainstInterest_Per", "%"},
        {"btnPrint", ""},
        {"lblRateInterestPer", "%"} ,
        
        {"tblProd1", "Select"},
        {"tblProd2", "Product ID"},
        {"tblProd3", "Product Description"},
        
        {"tblCategory1", "Select"},
        {"tblCategory2", "Category"},
        {"tblCategory3", "Category Description"},
        
        {"tblInter1", "From Date"},
        {"tblInter2", "To Date"},
        {"tblInter3", "From Amt"},
        {"tblInter4", "To Amt"},
        {"tblInter5", "From Period"},
        {"tblInter6", "To Period"},
        {"tblInter7", "Int Rate"},
         {"lblServiceTaxHd", "ServiceTaxAccountHead"},
          {"lblCess1Achd", "Cess1AccountHead"},
          {"lblCess2AcHd", "Cess2AccountHead"},
          {"panTaxAcHd", "Service Tax Account Head"},
          {"panInterestCalculation", "Tax Rate Maintenance"},
          {"panInterestGroup", "Service Tax For Charges"},
          
         
          
        
        {"cDialogOk", "OK"},
        {"cDialogNo", "No"},
        {"cDialogYes", "Yes"},
        {"cDialogCancel", "Cancel"},
        
        {"AMOUNT_WARNING", "To Amount Should Be greater than From Amount"},
        {"RANGE_WARNING", "To Period Should Be greater than From Period"},
        {"PENAL_WARNING", "Interest Rate Should be More than Penal Rate"},
        {"RATE_WARNING", "Interest Rate and Penal Rate Should Not be Greater Than or Equal to 100%"},
        {"SYNC_WARNING1", "The Period Range for "},
        {"SYNC_WARNING2", " Combination of Date Range and Amount Range is not Synchronised"},
        {"existanceWarning", "The Rate for this Range of Date and Amount already exist. Please Enter the Data Again"},
        {"NOROWWARNING", "Please Enter the Atleast One Record for Interest Calculation"},
        {"TODATE_WARNING", "Make previous schedule end date with currently selected Date."},
        {"PROD_WARNING", " Please select atleast one Product ID"},
        {"PROD_TYPE_WARNING", " Please select Product Type before Proceding further."},
        {"CATEGORY_WARNING", " Please select atleast one Category for the Selected Product ID"},
        {"DATA_WARNING", "Please Enter the Data for the Selected Combination of Product and Category ID"},
        {"AMOUNTWARNING","Amount and Period Should Be Maximum"}


   };

}
