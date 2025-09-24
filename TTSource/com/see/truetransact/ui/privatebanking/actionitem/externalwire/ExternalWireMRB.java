/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ExternalWireMRB.java
 * 
 * Created on Fri Jul 02 13:58:23 GMT+05:30 2004
 */

package com.see.truetransact.ui.privatebanking.actionitem.externalwire;

import java.util.ListResourceBundle;

public class ExternalWireMRB extends ListResourceBundle {
    public ExternalWireMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtChargesAmount", "ChargesAmount should not be empty!!!"},
        {"txtTraderDealerInst", "TraderDealerInst should not be empty!!!"},
        {"cboChargesPaidBy", "ChargesPaidBy should be a proper value!!!"},
        {"txtDebitAmount", "DebitAmount should not be empty!!!"},
        {"txtByOrderOf", "ByOrderOf should not be empty!!!"},
        {"tdtSettlementDate", "SettlementDate should not be empty!!!"},
        {"txtDebitEntitlementGroup", "DebitEntitlementGroup should not be empty!!!"},
        {"txtBenificiaryName", "BenificiaryName should not be empty!!!"},
        {"cboBenBankCity", "BenificiaryBankCity should be a proper value!!!"},
        {"txtSwiftCode", "SwiftCode should not be empty!!!"},
        {"cboCurrency", "Currency should be a proper value!!!"},
        {"cboBenBankState", "BenificiaryBankState should be a proper value!!!"},
        {"tdtExecutionDate", "ExecutionDate should not be empty!!!"},
        {"txtPaymentDetails", "PaymentDetails should not be empty!!!"},
        {"txtBankOfficeInstruction", "BankOfficeInstruction should not be empty!!!"},
        {"cboCorBankCountry", "CorrespondentBankCountry should be a proper value!!!"},
        {"rdoStandardCharges_Yes", "StandardCharges should be selected!!!"},
        {"txtMember", "Member should not be empty!!!"},
        {"txtBenificiartAcNo", "BenificiartAccountNo should not be empty!!!"},
        {"txtCreditAmount", "CreditAmount should not be empty!!!"},
        {"cboChargesCcy", "ChargesCcy should be a proper value!!!"},
        {"txtCreditNotes", "CreditNotes should not be empty!!!"},
        {"txtDebitAccount", "DebitAccount should not be empty!!!"},
        {"txtDebitAssetSubClass", "DebitAssetSubClass should not be empty!!!"},
        {"txtBenificiaryBank", "BenificiaryBank should not be empty!!!"},
        {"txtClientAdvices", "ClientAdvices should not be empty!!!"},
        {"cboCorBankCity", "CorrespondentBankCity should be a proper value!!!"},
        {"txtCorrespondentBank", "CorrespondentBank should not be empty!!!"},
        {"txtCorPin", "CorrrespondentBankPin should not be empty!!!"},
        {"txtDebitPortfolioLocation", "DebitPortfolioLocation should not be empty!!!"},
        {"txtBenPin", "BenificiaryBankPin should not be empty!!!"},
        {"cboBenBankCountry", "BenificiaryBankCountry should be a proper value!!!"},
        {"txtRoutingCode", "RoutingCode should not be empty!!!"},
        {"cboCorBankState", "CorrespondentBankState should be a proper value!!!"} 

   };

}
