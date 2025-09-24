/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductRB.java
 *
 * Created on Mon Apr 11 12:08:48 IST 2005
 */

package com.see.truetransact.ui.termloan.guarantee;

import java.util.ListResourceBundle;

public class GuaranteeMasterRB extends ListResourceBundle {
    public GuaranteeMasterRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"btnClose", ""},
        {"btnTabSave", "Save"},
        {"btnTabDelete", "Delete"},        
        {"btnAuthorize", ""},
        {"lblMsg", ""},
        {"lblSpace4", "     "},        
        {"lblSpace2", "     "},
        {"lblSpace3", "     "},
        {"lblSpace1", " Status :"},       
        {"btnReject", ""},
        {"btnEdit", ""},
        {"null", "Account Details"},
        {"btnTabNew", "New"},      
        {"btnPrint", ""},       
        {"btnException", ""},
        {"btnSave", ""},
        {"lblStatus", "                      "},
        {"btnDelete", ""},
        {"btnNew", ""},
        {"btnCancel", ""},
              
        {"lblPli", "P.L.I"},
        {"lblPliBranch", "P.L.I Branch "},
        {"lblCustId", "Customer ID "},
        {"lblSanctionNo", "Sanction No "},
        {"lblSanctionDt", "Sanction Date "},
        {"lblLoanNo", "Loan No "},
        {"lblLoanDt", "Loan Date "},
        {"lblLoanAmount", "Loan Amount "},
        {"lblHolidayPeriod", "Holiday Period "},
        {"lblNoOfInstallments", "No Of Installments"},
        {"blRepaymentFrequency", "Repayment Frequency"},
        {"lblInterestType", "Interest Rate"},
        {"lblGuaranteeNo", " Guarantee No"} ,
        {"lblGuaranteeDt", "Guarantee Date"},
        {"lblGuaranteeSanctionedBy", "Guarantee Sanctioned By"},
        {"lblGuaranteeSanctionNo", "Guarantee Sanction No"}, 
        {"lblGuaranteeAmount", "Guarantee Amount "},
        {"lblGuaranteeFeePayBy", "Guarantee Fee Pay By "},
        {"lblGuaranteeFeePer", "Guarantee Fee %"},
        {"lblGuaranteeFee", "Guarantee Fee"},
         {"lblRepaymentFrequency", "Repayment Frequency"},
        {"rdoCustomer", "Customer "},
        {"rdoPLI", "P.L.I "},
        
    };
    
}
