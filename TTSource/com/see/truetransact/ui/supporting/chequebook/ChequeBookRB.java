/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * ChequeBookRB.java
 */
package com.see.truetransact.ui.supporting.chequebook;
import java.util.ListResourceBundle;
public class ChequeBookRB extends ListResourceBundle {
    public ChequeBookRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblAccounNumber", "Account No."},
        {"btnClose", ""},
        {"lblUnpaidChequesValue", ""},
        {"lblAvailableBalance", "Available Balance"},
        {"lblSeriesNoTo", "Series No.To"},
        {"lblChequeIssued", "Cheques Issued in the Financial Year"},
        {"lblCustName", "Account Name"},
        {"lblMsg", ""},
        {"rdoLeaf_Bulk", "Bulk"},
        {"lblTotalBalance", "Total Balance"},
        {"lblSpace2", "     "},
        {"lblAccHeadId", ""},
        {"lblSpace3", "     "},
        {"lblUnpaidCheques", "Unpaid Cheques Out of Last 3 Books"},
        {"lblChequeDate", "Cheque Date"},
        {"lblSpace1", " Status :"},
        {"lblCustomerID", "Customer Id"},
        {"lblNoOfLeaves", "No. of Leaves"},
        {"lblCustId", "Customer Id"},
        {"btnPaymentRevoke", "STOP PAYMENT INSTRUCTION REVOKE"},
        {"lblTotalBalanceValue", ""},
        {"lblCustomName", "Account Name"},
        {"lblAccountHeadDesc", ""},
        {"lblChequeIssuedValue", ""},
        {"btnEdit", ""},
        {"lblSpaces", ""},
        {"lblCustomNameValue", ""},
        {"lblStartChequeNo", "Starting Cheque No."},
        {"lblProductID", "Product Id"},
        {"lblRemark", "Remarks"},
        {"lblAccHdDesc", ""},
        {"lblAccHdId", ""},
        {"lblCustomerIdDesc", ""},
        {"lblStartingCheque", "Starting Cheque No."},
        {"lblAccHd", "Account Head"},
        {"lblSeriesNoFrom", "Series No.From"},
        {"lblAccNo", "Account No."},
        {"btnPrint", ""},
        {"lblChqStopDtVal", ""},
        {"lblChqRevokeDtVal", ""},
        {"lblChqStopDt", "Chq Stop Dt"},
        {"lblChqRevokeDt", "Chq Revoke Dt"},
        {"lblMethodOfDelivery", "Method of Delivery"},
        {"panPaymentIssues", ""},
        {"lblAccHead", "Account Head"},
        {"lblStopPaymentCharges", "Stop Payment Charges"},
        {"lblAccountNo", "Account No."},
        {"btnAccountNo", ""},
        {"lblAccountHead", "Account Head"},
        {"lblAccHeadDesc", ""},
        {"lblCustomerNameValue", ""},
        {"lblEndingCheque", "Ending Cheque No."},
        {"rdoLeaf_single", "Single"},
        {"lblStatus1", "                      "},
        {"lblCustIdDesc", ""},
        {"btnAccountNumber", ""},
        {"panChequeDetails", "Cheque Book"},
        {"lblPayeeName", "Payee Name"},
        {"lblProductId", "Product Id"},
        {"btnSave", ""},
        {"lblCustNameValue", ""},
        {"lblChargesCollected", "Charges Collected"},
        {"lblAvailableBalanceValue", ""},
        {"btnAccNo", ""},
        {"lblChequesReturnedValue", ""},
        {"lblRemarks", "Remarks"},
        {"btnDelete", ""},
        {"lblCustomerName", "Customer Name"},
        {"lblChequesReturned", "Cheques Returned in the Financial Year"},
        {"lblCustomerIDValue", ""},
        {"lblNamesOfAccount", "Customer Name"},
        {"lblEndChequeNo", "Ending Cheque No."},
        {"lblNoOfChequeBooks", "No. of Cheque Books"},
        {"btnNew", ""},
        {"lblReasonStopPayment", "Reason for Stop Payment"},
        {"lblProdId", "Product Id"},
        {"lblLeaf", "Cheque Leaf"},
        {"lblLeafNo", "Cheque Leaf No."},
        {"lblChequeAmt", "Cheque Amount"},
        {"btnCancel", ""},
        {"lblAccountHeadId", ""},
        {"lblCustomerId", "Customer Id"}, 
        
        {"lblUsage", "Usage"},
        
        {"lblProdType", "Product Type"},
        {"lblProductType", "Product Type"},
        {"lblProduct_Type", "Product Type"},
        
        {"tblColumn1", "Issued Date"},
        {"tblColumn2", "Series From"},
        {"tblColumn3", "Series To"},
        {"tblColumn4", "Starting Che No."},
        {"tblColumn5", "Ending Che No."},
        
        {"OK", "OK"},
        {"MESSAGEHEADING", "The Cheque Book releted details are:"},
        {"MESSAGETITLE", "Issued Cheque Book(s) Details"},
        
        
        {"chequeBookIssue", "List for Cheque Book Issue"},
        {"EcsStopPayment", "List for Ecs Stop Payment"},
        {"chequeStopPayment", "List for Cheque Stop Payment"},
        {"chequeLooseLeaf", "List for Cheque Loose Leaf"},
        
        {"chequeBookProdId", "List for Cheque Book Issue Where Product Id: "},
        {"chequeStopProdId", "List for Cheque Stop Payment Where Product Id: "},
        {"chequeLooseProdId", "List for Cheque Loose Leaf Where Product Id: "},
        {"EcsLooseProdId", "List for Cheque Loose Leaf Where Product Id: "},
        {"lblEcsProductType","Product Type"},
        {"lblEcsProductId","Product ID"},
        {"lblEcsAccNo","Account No"},
        {"lblEcsCustId","Cust ID"},
        {"lblEcsCustName","Cust Name"},
        {"lblEcsNo","Ecs Number"},
        {"lblEcsDate","Ecs Date"},
        {"lblEcsAmt","Ecs Amount"},
        {"lblEcsNo","Ecs Number"},
        {"lblEcsNo","Ecs Number"},
        
        
        {"WARNING", "Starting And Ending Cheque No. Values are not Consistent"},
        {"CHEQUENOWARNING", "Starting And Ending Cheque No. Should Have Proper Values."},
        {"CHEQUEBOOKWARNING", "No. of Available Cheque Books in this Series are Less than the Ordered No of Books."},
        {"AVAILBOOKS", "No. of Available Cheque Books in this Series are: "},
        {"ORDEREDBOOKS", "No. of Cheque Books Ordered: "},
        {"REORDERBOOKS", "Please enter the No. Of Cheque Books again."},
        {"ISSUEBOOKWARNING", "The No. of Books that can Be issued Are: "},
        
        {"CHEQUE_BOOK_NOWARNING", "The Number of Cheque Books Entered Should be more than Zero(0)."}
   };

}
