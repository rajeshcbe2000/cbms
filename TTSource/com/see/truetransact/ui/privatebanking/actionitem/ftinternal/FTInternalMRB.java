/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ActionItemMRB.java
 * 
 * Created on Fri Jun 18 17:14:59 GMT+05:30 2004
 */

package com.see.truetransact.ui.privatebanking.actionitem.ftinternal;

import java.util.ListResourceBundle;

public class FTInternalMRB extends ListResourceBundle {
    public FTInternalMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtMember", "MemberId should not be empty!!!"},
        {"txtCreditAmount", "CreditAmount should not be empty!!!"},
        {"txtTraderDealerInst", "TraderDealerInst should not be empty!!!"},
        {"txtDebitAmount", "DebitAmount should not be empty!!!"},
        {"txtDebitPortfolioLocation", "DebitPortfolioLocation should not be empty!!!"},
        {"txtCreditNotes", "CreditNotes should not be empty!!!"},
        {"txtDebitAccount", "DebitAccount should not be empty!!!"},
        {"txtDebitAssetSubClass", "DebitAssetSubClass should not be empty!!!"},
        {"txtDebitEntitlementGroup", "DebitEntitlementGroup should not be empty!!!"},
        {"tdtExecutionDate", "ExecutionDate should not be empty!!!"},
        {"txtCreditPortfolioLocation", "CreditPortfolioLocation should not be empty!!!"},
        {"txtCreditAccount", "CreditAccount should not be empty!!!"},
        {"tdtValueDate", "ValueDate should not be empty!!!"},
        {"txtCreditEntitlementGroup", "CreditEntitlementGroup should not be empty!!!"},
        {"txtClientAdvices", "ClientAdvices should not be empty!!!"},
        {"txtBankOfficeInstruction", "BankOfficeInstruction should not be empty!!!"},
        {"txtCreditAssetSubClass", "CreditAssetSubClass should not be empty!!!"} 

   };

}
