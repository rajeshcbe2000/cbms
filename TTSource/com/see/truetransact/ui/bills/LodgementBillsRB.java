/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * LodgementBillsRB.java
 */
package com.see.truetransact.ui.bills;
import java.util.ListResourceBundle;
public class LodgementBillsRB extends ListResourceBundle {
    public LodgementBillsRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"lblDispatchDetailsArea", "Area"},
        {"lblDispatchDetailsName", "Name"},
        {"btnClose", ""},
        {"btnSave", ""},
        {"btnCancel", ""},
        {"btnCustomerID", ""},
        {"lblDispatchDetailsStreet", "Street"},
        {"lblMsg", ""},
        {"lblAccHead", "Account Head"},
        {"lblDispatchDetailsCountry", "Country"},
        {"lblCustomerName", ""},
        {"lblDispatchDetailsState", "State"},
        {"lblDispatchDetailsCity", "City"},
        {"lblDispatchDetailsOthers", "Drawee Bank / Branch / Others"},
        {"lblDraweeDetailsArea", "Area"},
        {"lblDraweeDetailsName", "Name"},
        {"lblDispatchDetailsPincode", "Pincode"},
        {"lblAmtOfBill", "Amount of Bill"},
        {"lblBankDetails", "Bank Details"},
        {"lblCustomerID", "Customer ID"},
        {"lblCustomerAccNo", "Customer Acc. No."},
        {"lblDraweeDetailsCountry", "Country"},
        {"lblAmountOfBill", ""},
        {"lblDraweeDetailsStreet", "Street"},
        {"lblPostage", "Postage"},
        {"lblDraweeDetailsState", "State"},
        {"lblStatus", "                      "},
        {"lblInstrumentDetails", "Instrument / Bill Details"},
        {"lblSpace3", " Status :"},
        {"lblSpace2", "     "},
        {"lblSpace1", "     "},
        {"lblCommission", "Commission"},
        {"lblDraweeDetailsPincode", "Pincode"},
        {"lblProductID", "Product ID"},
        {"lblDraweeDetailsCity", "City"},
        {"btnPrint", ""},
        {"lblBorrowerNum", "Borrower No."},
        {"lblTypeOfBill", "Type of Bills"},
        {"panProductID", ""},
        {"lblDraweeDetailsOthers", "Drawee Bank / Branch / Others"},
        {"btnDelete", ""},
        {"btnNew", ""},
        {"panDraweeDeatils", "Drawee Details"},
        {"lblDiscount", "Discount"},
        {"lblAccountHead", ""},
        {"lblCustomerNm", "Customer Name"},
        {"lblPSRBOtherBanks", "P / SBR / Other Banks"},
        {"panDispatchDetails", "Forwarded / Dispatch Details"},
        {"btnEdit", ""},
        {"lblMargin", "Margin"},
        {"lblCustomerAccountNumber", ""},
        {"TOCommandError", "TO Status Command is null"}  
    };
    
}
