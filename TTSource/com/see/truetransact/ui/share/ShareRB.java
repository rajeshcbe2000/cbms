/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareRB.java
 * 
 * Created on Fri Dec 24 17:36:44 IST 2004
 */

package com.see.truetransact.ui.share;

import java.util.ListResourceBundle;

public class ShareRB extends ListResourceBundle {
    public ShareRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"btnClose", ""},
        {"panShareAcctDet", "Share Account Details"},
        {"btnAuthorize", ""},
        {"lblShareDetShareNoFrom", "Total Membership Fee"},
        {"lblConstitution", "Constitution"},
        {"lblMsg", ""},
        {"lblPin", "Pin"},
        {"lblSpace2", "     "},
        {"lblValDateOfBirth", ""},
        {"lblValStreet", ""},
        {"lblSpace3", "     "},
        {"lblValCity", ""},
        {"panCustomerName", "Customer Details"},
        {"lblSpace1", " Status :"},
        {"chkNotEligibleStatus", ""},
        {"lblCustId", "Customer Id"},
        {"lblShareDetShareAcctNo", "No Of Share ."},
        {"lblWelFund", "Welfare Fund paid Details"},
        {"lblCountry", "Country"},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"lblValCustomerName", ""},
        {"btnPrint", ""},
        {"lblDtNotEligiblePeriod", "Loan Upto"},//Not Eligible For Loan Upto
        {"lblShareDetNoOfShares", "Total Application Fee"},
        {"lblRelativeDetails", "Relative Mem Details"},
        {"lblDateOfBirth", "Date of Birth"},
        {"lblStreet", "Street"},
        {"lblCity", "City"},
        {"lblValArea", ""},
        {"lblMemFeeFixed", "Membership Fee"},
        {"lblMemFeePercent", "Member Fee %"},
        {"btnException", ""},
        {"lblDupIdCard", "Duplicate Id card details"},
        {"lblValCountry", ""},
        {"lblApplFee", "Application Fee"},
        {"btnShareAcctDetDel", "Delete"},
        {"btnSave", ""},
        {"lblShareFee", "Share Fee"},
        {"lblAcctStatus", "Account Status"},
        {"lblValPin", ""},
        {"btnShareAcctDetNew", "New"},
        {"lblStatus", "                      "},
        {"lblShareAcctNo", "Share Acct No."},
        {"lblIDCardNo", "ID Card No"},
        {"lblDirRelDet", "Director Relative Details"},
        {"btnCustomerIdFileOpen", ""},
        {"lblValState", ""},
        {"lblPropertyDetails", "Property Details"},
        {"lblArea", "Area"},
        {"lblSpace5", "     "},
        {"lblConnGrpDet", "Connected Group Details"},
        {"btnDelete", ""},
        {"lblCustomerName", "Customer Name"},
        {"lblResolutionNo", "Resolution No."},
        {"lblDtIssId", "Date of issue of Id"},
        {"lblShareAmt", "Share Face Value"},
        {"btnShareAcctDetSave", "Save"},
        {"btnNew", ""},
        {"lblNotEligibleStatus", "Not Eligible for loan"},
        {"lblDtShareDetIssShareCert", "Cert., Issue Date"},
        {"btnCancel", ""},
        {"lblState", "State"},
        {"lblShareDetShareNoTo", "Total Share Fee"},
        {"lblCommAddrType", "Communication Addr.Type"},//Communication
        {"lblShareType", "Share Type"},
        {"txtRemarks", "Remarks"},
        {"tblJntAccntColumn1", "Name"},
        {"tblJntAccntColumn2", "Cust.Id"},
        {"tblJntAccntColumn3", "Type"},
        {"tblJntAccntColumn4", "Main / Joint"},
        {"tblJntAccntColumn5", "Caste"},
        {"tblShrAccntDetColumn1", "Serial No."},
        {"tblShrAccntDetColumn2", "Issue Date "},
        {"tblShrAccntDetColumn3", "No.of shares"},
        {"tblShrAccntDetColumn4", "Share Value"},
        {"tblShrAccntDetColumn6", "Status"},
        {"tblShrAccntDetColumn5", "Add/Withdrawl"},
        {"tblShareAccLoanColum1","Cust Id"},
        {"tblShareAccLoanColum2","Cust Name"},
        {"tblShareAccLoanColum3","Loan No"},
        {"tblShareAccLoanColum4","Loan Limit Amt"},
        {"tblShareAccLoanColum5","Loan Open Dt"},
        {"tblShareAccLoanColum6","Present Out Standing"},
        {"tblShareAccLoanColum7","Loan_Status"},
        {"lblMemDivAcNo","Dividend Account No"},
        {"lblMemDivProdId","Dividend Prod Id"},
        {"lblMemDivProdType","Dividend Prod Type"},
        {"ShareNoFromLesserShareNoTo", "ShareNo.From should be lesser than ShareNo.To"},
        {"NonEligiblePeriodEmpty", "Non-Eligible-Period should not be empty"},
        {"ShareAccountDetailsEmpty", "Share Account Details should not be empty"},
        {"NoOfShareExceed", "Total No.of shares should not exceed "},
        {"NoRecords", "There are no transaction records."},
        {"cDialogOK", "OK"},
        {"lblResolutionNo1", "Resolution No1."},
        {"MoreThanIssuedCapital", "The Share account should not be greater than the Issued Capital "},
        {"saveInTxDetailsTable","Save The Current Transaction Details in the Table!!!"},
        {"selectTheConstitution","Please select the Constitution!!!"},
        {"selectTheCustomer", "Please select the Customer!!!"},
        {"selectTheShareType","Please select the Share Type!!!"},
        {"selectTheShare Dividend pay Mode","Please select the  Dividend pay Mode!!!"},
        {"shareAcctExists","Share account exists for this Customer:"},
        {"authExistShareAcct","Authorize existing share details for this Customer:"},
        {"saveAcctDet", "Save the Share Account Details before saving the Share Account!!!"},
        {"lblEmpRefNoNew","Employer Ref No.(New)"},
         {"lblEmpRefNoOld","Employer Ref No.(Old)"},
         {"lblImbp","IMBP"}
        
        
  };

}
