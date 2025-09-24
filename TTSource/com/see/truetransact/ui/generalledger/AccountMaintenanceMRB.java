/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * AccountMaintenanceMRB.java
 */

package com.see.truetransact.ui.generalledger;
import java.util.ListResourceBundle;
public class AccountMaintenanceMRB extends ListResourceBundle {
    public AccountMaintenanceMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtAccountHead","Account Head should not be empty!!!"},
        {"chkCreditClearing", "CreditClearing should be selected!!!"},
        {"chkDebitClearing", "DebitClearing should be selected!!!"},
        {"cboPostingMode", "PostingMode should be a proper value!!!"},
        {"cboTransactionPosting", "TransactionPosting should be a proper value!!!"},
        {"cboContraHead", "ContraHead should be a proper value!!!"},
        {"txtCallingCode", "CallingCode should not be empty!!!"},
        {"chkReconcilliationAllowed", "ReconcilliationAllowed should be selected!!!"},
        {"txtBalanceInGL", "BalanceInGL should not be empty!!!"},
        {"chkDebitCash", "DebitCash should be selected!!!"},
        {"chkCreditTransfer", "CreditTransfer should be selected!!!"},
        {"cboGLBalanceType", "GLBalanceType should be a proper value!!!"},
        {"chkCreditCash", "CreditCash should be selected!!!"},
        {"rdoStatus_Implemented", "Status should be selected!!!"},
        {"chkDebitTransfer", "DebitTransfer should be selected!!!"},
        {"rdoFloatAccount_Yes", "FloatAccount should be selected!!!"} ,
        {"rdoNegValue_Yes", "NegValue should be selected!!!"},
        {"chkHdOfficeAc", "Head Office Account should be selected"},
        {"txtReconcillationAcHd", "Reconcillation Account Head should be a proper value!!!"},
        {"chkBalCheck", "Day End Zero Balance Check should be Selected!!!"}

   };

}
