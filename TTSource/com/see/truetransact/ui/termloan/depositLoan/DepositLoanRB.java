/*Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 **/
package com.see.truetransact.ui.termloan.depositLoan;

import java.util.ListResourceBundle;
/*
 *@author shanmugavel
 */
public class DepositLoanRB extends ListResourceBundle {
    public DepositLoanRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
       
        {"lblProductId", "Loan Product"},
        {"lblDepositNo", "Deposit No"},
        {"lblAccountHeadId", "AccountHead"},
        {"lblConsititution", "Consititution"},
        {"lblCategory", "Category"},
        {"lblCustomerName", "Customer Name"},
        {"lblCustomerId", "Customer Id"},
        {"lblMemberId", "Member Id"},
        {"lblAcctNo_Sanction", "Loan Acc No"},
        {"lblSanctionRef", "Sanction Ref"},
        {"lblSanctionBy", "Sanction By"},
        {"lblTotalNoOfShare", "Total No Of Share"},
        {"lblTotalShareAmount", "Total Share Amount"},
        {"lblLoanAmt", "Loan Amt"},
        {"lblLoanAmt", "Loan Amt"},
        {"lblAccOpenDt", "Account Open Dt"},
        {"lblAccStatus", "Account Status"},
        {"lblRepaymentDt", "Repayment Dt"},
        {"lblInter", "Interest"},
        {"lblAdditionalLoanfacility","AdditionalLoanfacility"},
        
        {"tblColumnSanction_Main1","Deposit No"},
        {"tblColumnSanction_Main2","Deposit Amt"},
        {"tblColumnSanction_Main3","Deposit Int"},
        {"tblColumnSanction_Main4","Maturity Amt"},
        {"tblColumnSanction_Main5","Int Paid Amt"},
        {"tblColumnSanction_Main6","Maturity Dt"},
        {"tblColumnSanction_Main7","Status"},
        
        {"repayDeleteAfterrepayStarted","Disbursment Already Over Can not Delete Repayment Schedule "+"\n"+" make it as  " +
         "Inactive"+"\n"+" Then Create New Repayment Schedule"},
        {"REMARK_CASH_TRANS","Cash Transaction Token No"},
        {"REMARK_TRANSFER_TRANS","Transfer Transaction Operative Account No"}
    };
    
}
