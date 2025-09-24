/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * DailyDepositTransMRB.java
 */
package com.see.truetransact.ui.transaction.dailyDepositTrans;
import java.util.ListResourceBundle;
public class DailyDepositTransMRB extends ListResourceBundle {
    public DailyDepositTransMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtAmount", "Amount should not be empty!!!"},
        {"rdoTransactionType_Credit", "TransactionType should be selected!!!"},
        {"txtInputAmt", "InputAmt should not be empty!!!"},
        {"cboProdId", "ProdId should be a proper value!!!"},
        {"cboProdType", "ProdType should be a proper value!!!"},
        {"txtAccNo", "AccNo should be a proper value!!!"},
        {"txtAccHdId", "AccHdId should be a proper value!!!"},
        {"AlreadyAdd", "Already This Account is Added in The list!!!"},
        {"txtAccNo", "Account no should not be empty !!!"},
        {"cboAgentType", "Agent no should not be empty !!!"},
        {"tdtInstrumentDate", "Collection Date  should not be empty !!!"},
        {"txtInitiatorChannel", "InitiatorChannel should be a proper value!!!"} 

   };

}
