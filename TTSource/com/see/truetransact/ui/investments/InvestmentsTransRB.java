/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * ShareProductRB.java
 *
 * Created on Mon Apr 11 12:08:48 IST 2005
 */

package com.see.truetransact.ui.investments;

import java.util.ListResourceBundle;

public class InvestmentsTransRB extends ListResourceBundle {
    public InvestmentsTransRB(){
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
              
        {"lblInvestmentID", "Investment ID "},
        {"lblInvestmentName", "Investment Name "},
        {"lblInvestmentBehaves", "Investment Type "},
        {"lblIssueDate", "Issue Date "},
        {"lblFaceValue", "Face Value"},
        {"lblDebitChequeNo", "Cheque No"},
        {"lblLastIntPaidDate", "Last Interest Paid Date"},
        {"lblInterestPaymentFrequency", "Interest Payment Frequency : "},
        {"lblPeriod", "Investment Period "},
        {"lblCoupenRate", "Coupon Rate "},
        {"lblMaturityDate", "Maturity Date :"},
        
        {"lblTotalInvestmentAmount", "Total Investment Amount"},
        {"lblTotalPremiumPaid", "Total Premium Paid"},
        {"lblTotalPremiumCollected", "Total Premium Collected"} ,
        {"lblTotalInterestPaid", "Total Interest Paid"},
        {"lblTotalInterestCollected", "Total Interest Collected"},
        {"lblPurchaseDt", "Purchase Date"} ,     
        {"rdoPrimary", "Primary"} ,
        {"rdoSecondary", "Secondary"} ,
        {"rdoPurchase", "Purchase "} ,
        {"rdoSale", "Sale"} ,
        {"rdoMarket", "Market"} ,
        {"rdoOffMarket", "Off Market"} ,
        {"lblBrokerName", "Broker Name"} ,
        {"lblPurchaseRate", "Purchase Rate"} ,
        {"lblNoOfUnits", "No Of Units"} ,
        {"lblInvestmentAmount", "Investment Amount"} ,
        {"lblNarration", "Narration"} ,
        {"lblIPrematureROI", "Premature ROI"} ,
        {"lblPrematureIntAmt", "Interest Amount"} ,
        {"lblPremiumAmount", "Premium Amount"} ,
        {"lblTransTotalInvestmentAmount", "Trans Total Investment Amount"} ,
        {"lblBrokenPeriodIntAmount", "Broken Period Int Amount"} ,
        {"lblBrokerCommission", "Broker Commission"} ,
        {"lblTotalAmount", "Total Amount Rs."} 
        
    };
    
}
