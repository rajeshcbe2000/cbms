/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemittanceProductMRB.java
 *
 * Created on Thu Jun 24 18:03:43 GMT+05:30 2004
 */

package com.see.truetransact.ui.product.mds;

import java.util.ListResourceBundle;

public class MDSProductMRB extends ListResourceBundle {
    public MDSProductMRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"txtProductId",  "Product Id should not be empty!!!"},
        {"lblProductDescVal","Product Description should not be empty!!!"},
        {"txtAuctionMaxAmt", "Maximum Auction Amount should not be empty!!!"},
        {"txtAuctionMinAmt", "Minimum Auction Amount should not be empty!!!"},
        {"cboholidayInt",  "HolidayInt should not be empty!!!"},
        {"cboCommisionRate",  "Commision Rate should not be empty!!!"},
        {"cboDiscountRate",  "Discount Rate should not be empty!!!"},
        {"cboPenalIntRate",  "Penal Int Rate should not be empty!!!"},
        {"cboPenalPrizedRate",  "Penal Prized Rate should not be empty!!!"},
        {"cboLoanIntRate",  "Loan Int Rate should not be empty!!!"},
        {"txtCommisionRate", "Commision Rate should be a proper value!!!"},
        {"txtDiscountRate", "Discounted Rate should not be empty!!!"},
        {"txtPenalIntRate", "Penal Rate should not be empty!!!"},
        {"txtPenalPrizedRate", "Penal Prized Rate should not be empty!!!"},
        {"txtLoanIntRate", "Loan Rate should not be empty!!!"},
        {"txtBonusGracePeriod", "Bonus Grace Period should not be empty!!!"},
        {"txtBonusPrizedPeriod", "BonusPrizedPeriod should not be empty!!!"},
        {"txtPenalPeriod", "Penal Grace period should not be empty!!!"},
        {"txtPenalPrizedPeriod", "Penal Prized Grace Period should not be empty!!!"},
        {"cboPenalPeriod",  "Penal Period should not be empty!!!"},
        {"cboPenalPrizedPeriod",  "Penal Prized Period should not be empty!!!"},
        {"cboPenalCalc",  "Penal Calculation based on should not be empty!!!"},
        {"txtDiscountPeriodEnd", "DiscountPeriodEnd should not be empty!!!"},
        {"txtDiscountPrizedPeriodEnd", "DiscountPrizedPeriodEnd should not be empty!!!"},
        {"txtMargin", "Margin should be a proper value!!!"},
        {"txtMinLoanAmt",  "Minimum Loan Amount should not be empty!!!"},
        {"txtMaxLoanAmt",  "Maximum Loan Amount should not be empty!!!"},
//        {"txtReceiptHead",  "ReceiptHead should not be empty!!!"},
//        {"txtPaymentHead",  "PaymentHead should not be empty!!!"},
//        {"txtSuspenseHead",  "SuspenseHead should not be empty!!!"},
//        {"txtThalayalReceiptsHead",  "ThalayalReceiptsHead should not be empty!!!"},
//        {"txtMunnalReceiptsHead",  "MunnalReceiptsHead should not be empty!!!"},
//        {"txtCaseExpensesHead",  "CaseExpense Head should not be empty!!!"},
//        {"txtNoticeChargesHead",  "NoticeCharges Head should not be empty!!!"},
//        {"txtMiscellaneousHead",  "Miscellaneous Head should not be empty!!!"},
    };
}