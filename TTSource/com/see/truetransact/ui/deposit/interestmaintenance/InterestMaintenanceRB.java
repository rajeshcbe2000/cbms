/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InterestMaintenanceRB.java
 * 
 * Created on Mon Jan 17 17:30:09 IST 2005
 */

package com.see.truetransact.ui.deposit.interestmaintenance;

import java.util.ListResourceBundle;

public class InterestMaintenanceRB extends ListResourceBundle {
    public InterestMaintenanceRB(){
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
        {"lblLimitAmt", "Additional Interest"},
        {"lblInterExpiry_Per", "%"},
        {"lblStatementPenal", "Statement Default Penal"},
        {"lblFromAmount", "From Amount"},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"lblSpace3", "     "},
        {"lblStatement_Per", "%"},
        {"lblProdType", "Product Type"},
        {"lblPenalInterest", "Penal Interest"},
        {"lblStatus", "                      "},
        {"lblSpace1", " Status :"},
        {"lblODI_Per", "%"},
        {"btnDelete", ""},
        {"lblAgainstInterest", "Against Clearing Interest"},
        {"btnTabDelete", "Delete"},
        {"lblRateInterest", "Rate of Interest"},
        {"panGroupData", "Group Data"},
        {"lblGroupIdDesc", ""},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"lblInterExpiry", "Interest for Expiry of Limit"},
        {"lblDate", "Date"},
        {"lblSpaces", "     "},
        {"lblODI", "OD Interest"},
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
        {"tblInter8", "Active Status"},
        {"tblInter9", "Auth Status"},
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
        {"AMOUNTWARNING","Amount and Period Should Be Maximum"},
        {"INT_TYPE_WARNING", "Select Interest Type"}

   };

}
