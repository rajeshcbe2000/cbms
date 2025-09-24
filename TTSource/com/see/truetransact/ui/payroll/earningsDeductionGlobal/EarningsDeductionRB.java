/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EarningsDeductionRB.java
 * 
 /**
 *
 * @author anjuanand
 */
package com.see.truetransact.ui.payroll.earningsDeductionGlobal;

import java.util.ListResourceBundle;

public class EarningsDeductionRB extends ListResourceBundle {

    public EarningsDeductionRB() {
    }

    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"btnClose", ""},
        {"btnDelete", ""},
        {"btnSave", ""},
        {"btnEdit", ""},
        {"btnNew", ""},
        {"btnCancel", ""},
        {"btnClose", ""},
        {"btnView", ""},
        {"rdoEarnings", "Earnings"},
        {"rdoDeduction", "Deduction"},
        {"rdoContra", "Contribution"},
        {"lblStatus", "                      "},
        {"lblMsg", ""},
        {"lblSpace", "Status : "},
        {"lblModuleType", "Module Type"},
        {"lblDescription", "Description"},
        {"lblCalculationType", "Calculation Type"},
        {"lblAmount", "Amount"},
        {"lblActive", "Active"},
        {"lblExcludeFromTax", "Exclude From Tax"},
        {"lblPaymentVoucherRequired", "Payment Voucher Required"},
        {"lblIndividualRequired", "Whether required for all Employees"},
        {"lblAccountNo", "Account Head"},
        {"lblAccountType", "Account Type"},
        {"lblModType", "Module Type"},
        {"lblPercentage", "Percentage"},
        {"lblPercent", "%"},
        {"lblIncludePersonalPay", "Include Personal Pay"},
        {"lblMinAmt", "Min Amt"},
        {"lblMaxAmt", "Max Amt"},
        {"lblProductType", "Product Type"},
        {"lblOnlyForContra", "Only For Contra"},
        {"lblFromDate", "From Date"},
        {"lblGl", "GL"},
        {"lblProdType", "Product Type"}
    };
}
