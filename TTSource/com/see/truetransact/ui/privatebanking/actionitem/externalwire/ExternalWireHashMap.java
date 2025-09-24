/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * ExternalWireHashMap.java
 */
package com.see.truetransact.ui.privatebanking.actionitem.externalwire;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class ExternalWireHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public ExternalWireHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtChargesAmount", new Boolean(true));
        mandatoryMap.put("cboChargesPaidBy", new Boolean(true));
        mandatoryMap.put("txtDebitAmount", new Boolean(true));
        mandatoryMap.put("txtTraderDealerInst", new Boolean(true));
        mandatoryMap.put("txtByOrderOf", new Boolean(true));
        mandatoryMap.put("txtDebitEntitlementGroup", new Boolean(true));
        mandatoryMap.put("tdtSettlementDate", new Boolean(true));
        mandatoryMap.put("txtBenificiaryName", new Boolean(true));
        mandatoryMap.put("txtSwiftCode", new Boolean(true));
        mandatoryMap.put("cboCurrency", new Boolean(true));
        mandatoryMap.put("cboBenBankCity", new Boolean(true));
        mandatoryMap.put("cboBenBankState", new Boolean(true));
        mandatoryMap.put("tdtExecutionDate", new Boolean(true));
        mandatoryMap.put("txtPaymentDetails", new Boolean(true));
        mandatoryMap.put("txtBankOfficeInstruction", new Boolean(true));
        mandatoryMap.put("cboCorBankCountry", new Boolean(true));
        mandatoryMap.put("rdoStandardCharges_Yes", new Boolean(true));
        mandatoryMap.put("txtMember", new Boolean(true));
        mandatoryMap.put("txtBenificiartAcNo", new Boolean(true));
        mandatoryMap.put("txtCreditAmount", new Boolean(true));
        mandatoryMap.put("cboChargesCcy", new Boolean(true));
        mandatoryMap.put("txtCreditNotes", new Boolean(true));
        mandatoryMap.put("txtDebitAccount", new Boolean(true));
        mandatoryMap.put("txtDebitAssetSubClass", new Boolean(true));
        mandatoryMap.put("txtBenificiaryBank", new Boolean(true));
        mandatoryMap.put("txtClientAdvices", new Boolean(true));
        mandatoryMap.put("txtDebitPortfolioLocation", new Boolean(true));
        mandatoryMap.put("cboCorBankCity", new Boolean(true));
        mandatoryMap.put("txtCorrespondentBank", new Boolean(true));
        mandatoryMap.put("txtCorPin", new Boolean(true));
        mandatoryMap.put("txtBenPin", new Boolean(true));
        mandatoryMap.put("cboBenBankCountry", new Boolean(true));
        mandatoryMap.put("txtRoutingCode", new Boolean(true));
        mandatoryMap.put("cboCorBankState", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}
