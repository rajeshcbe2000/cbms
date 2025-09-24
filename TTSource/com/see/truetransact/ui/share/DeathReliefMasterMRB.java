/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DeathReliefMasterMRB.java
 * 
 * Created on Fri Aug 05 13:53:36 GMT+05:30 2011
 */

package com.see.truetransact.ui.share;

import java.util.ListResourceBundle;

    
    
public class DeathReliefMasterMRB extends ListResourceBundle {
    public DeathReliefMasterMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtProductId", "Product ID should not be empty!!!"},
        {"txtPaymentAmount", "Payment Amount should not be empty!!!"},
        {"txtPaymentHeadName", "Payment Head should not be empty!!!"},
        {"txtRecoveryHead", "Recovery Head should not be empty!!!"},
        {"txtDrfName", "Name should not be empty!!!"},
        {"txtActHeadName", "Account Head should not be empty!!!"},
        {"txtDrfAmount", "Amount should not be empty!!!"},
        {"tdtDrfFromDt", "Installment Date should not be empty!!!"},
        {"tdtDrfToDt", "Effective Till date should not be empty!!!"},
        {"cboCalculationFrequency"," Interest Calculation Frequency cannot be empty!!" },
        {"cboCalclulationCriteria","Calculation Criteria cannot be empty!!" },
        {"cboProductFrequency","Product Frequency cannot be empty!!" },
        {"txtDebitHead","Interest Debit A/C Head cannot be empty!!" },
        {"lblAmountRecovery","Recovery Amount cannot be empty!!" },
        {"lblRecoverAmount","Recovery Amount cannot be empty!!" },
         {"txtInterestRate","Interest Rate cannot be empty!!" },
         {"tdtFromDt","From Date  cannot be empty!!" },
          {"tdtCalculatedDt","Last Interest Calculated Date cannot be empty!!" }
      
         
         

   };

}
