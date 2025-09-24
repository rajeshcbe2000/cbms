/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenConfigMRB.java
 * 
 * Created on Thu Jan 20 16:39:25 IST 2005
 */

package com.see.truetransact.ui.indend;

import java.util.ListResourceBundle;

public class DepoMRB extends ListResourceBundle {
    public DepoMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cboStoreNo", "Store name should not be empty!!!\n"},
        {"cboSalesmanId", "Sales man name should not be empty!!!\n"},
        {"txtName", "Depo name should not be empty!!!\n"},
        {"txtOpngStock", "Op. Stock should not be empty!!!\n"},
        {"tdtStockasonDate", "Stock as on date should not be empty!!!\n"},
        {"txtSalesgpHead", "Sales group head should not be empty!!!"},
        {"txtPurchasegpHead", "Purchase group head should not be empty!!!\n"},
        {"txtSalesReturngpHead", "Sales Return group head should not be empty!!!\n"},
        {"txtPurchaseReturngpHead", "Purchase Return group head should not be empty!!!\n"},
        {"txtDeficiategpHead", "Deficiate group head should not be empty!!!\n"},
        {"txtDamagegpHead", "Damage group head should not be empty!!!\n"},
        {"txtServiceTaxgpHead", "ServiceTax group head should not be empty!!!\n"},
        {"txtVatTaxgpHead", "VatTax group head should not be empty!!!\n"},
        {"txtComReciedgpHead", "Commission Recieved group head should not be empty!!!\n"},
        {"txtMisIncomegpHead", "MiscIncome group head should not be empty!!!\n"},
        {"txtPurVatTaxgpHead", "Purchase Vat Tax group head should not be empty!!!\n"},
        {"txtStockHd", "Stock head should not be empty!!!\n"},
        {"txtPurRetrnVatTaxgpHead", "Purchase Return Vat Tax group head should not be empty!!!\n"},
        {"txtSaleVatTaxgpHead", "Sale Vat Tax group head should not be empty!!!\n"},
        {"txtSaleRetrnVatTaxgpHead", "Sale Return Vat Tax group head should not be empty!!!\n"},
        {"txtDamageVatgpHead", "Damage Vat Tax group head should not be empty!!!\n"},
        {"txtDeficitVatgpHead", "Deficite Vat Tax group head should not be empty!!!\n"},
        {"txtOtherExpnsgpHead", "Other Expense Vat Tax group head should not be empty!!!\n"},
        {"txtOpngStockNo", "Enter number for opening stock!!!\n"}
    };

}
