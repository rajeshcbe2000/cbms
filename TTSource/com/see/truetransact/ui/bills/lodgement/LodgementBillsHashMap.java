/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LodgementBillsHashMap.java
 *
 * Created on Mon Feb 07 12:19:44 IST 2005
 */

package com.see.truetransact.ui.bills.lodgement;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class LodgementBillsHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    
    public LodgementBillsHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtBillTenor", new Boolean(false));
        mandatoryMap.put("txtInvoiceNumber", new Boolean(false));
        mandatoryMap.put("cboProductId", new Boolean(false));
        mandatoryMap.put("txtDraweeNo", new Boolean(false));
        mandatoryMap.put("txtDraweeBranchCode", new Boolean(false));
        mandatoryMap.put("cboBillTenor", new Boolean(false));
        mandatoryMap.put("tdtRRLRDate", new Boolean(false));
        mandatoryMap.put("txtBranchCode", new Boolean(false));
        mandatoryMap.put("txtAccountHeadValue", new Boolean(false));
        mandatoryMap.put("txtDraweeBankCode", new Boolean(false));
        mandatoryMap.put("rdoDraweeHundi_Yes", new Boolean(false));
        mandatoryMap.put("tdtInstrumentDate", new Boolean(false));
        mandatoryMap.put("rdoBillAcceptance_Yes", new Boolean(false));
        mandatoryMap.put("cboBillsType", new Boolean(true));
        mandatoryMap.put("txtHundiAmount", new Boolean(false));
        mandatoryMap.put("txtGoodsAssigned", new Boolean(false));
        mandatoryMap.put("txaRemarks", new Boolean(false));
        mandatoryMap.put("tdtInvoiceDate", new Boolean(false));
        mandatoryMap.put("cboReceivedFrom", new Boolean(true));
        mandatoryMap.put("cboInstrumentType", new Boolean(true));
        mandatoryMap.put("txtHundiNo", new Boolean(false));
        mandatoryMap.put("txtGoodsValue", new Boolean(false));
        mandatoryMap.put("cboDraweeCountry", new Boolean(false));
        mandatoryMap.put("txtRRLRNumber", new Boolean(false));
        mandatoryMap.put("txtTransportCompany", new Boolean(false));
        mandatoryMap.put("txtInstrumentDate", new Boolean(false));
        mandatoryMap.put("txtDraweeBankName", new Boolean(false));
        mandatoryMap.put("txtDraweeBranchName", new Boolean(false));
        mandatoryMap.put("cboOtherCountry", new Boolean(false));
        mandatoryMap.put("txtHundiRemarks", new Boolean(false));
        mandatoryMap.put("txtAccountNo", new Boolean(false));
        mandatoryMap.put("txtOtherName", new Boolean(false));
        mandatoryMap.put("cboDraweeCity", new Boolean(false));
        mandatoryMap.put("txtReference", new Boolean(false));
        mandatoryMap.put("txtRateForDelay", new Boolean(false));
        mandatoryMap.put("txtInstrumentAmount", new Boolean(true));
        mandatoryMap.put("txtCreatDt", new Boolean(true));
        mandatoryMap.put("txtDraweeAddress", new Boolean(false));
        mandatoryMap.put("txtOtherPinCode", new Boolean(false));
        mandatoryMap.put("txtBankCode", new Boolean(false));
        mandatoryMap.put("txtStdInstruction", new Boolean(false));
        mandatoryMap.put("tdtDueDate", new Boolean(false));
        mandatoryMap.put("txtDraweePinCode", new Boolean(false));
        mandatoryMap.put("txtInvoiceAmount", new Boolean(false));
        mandatoryMap.put("txtPayeeName", new Boolean(false));
        mandatoryMap.put("cboOtherState", new Boolean(false));
        mandatoryMap.put("txtMICR", new Boolean(false));
        mandatoryMap.put("cboProductType", new Boolean(false));
        mandatoryMap.put("txtSendingTo", new Boolean(false));
        mandatoryMap.put("txtOtherBranchCode", new Boolean(false));
        mandatoryMap.put("tdtHundiDate", new Boolean(false));
        mandatoryMap.put("txtPayable", new Boolean(false));
        mandatoryMap.put("cboDraweeState", new Boolean(false));
        mandatoryMap.put("cboOtherCity", new Boolean(false));
        mandatoryMap.put("tdtAcceptanceDate", new Boolean(false));
        mandatoryMap.put("txtOtherAddress", new Boolean(false));
        mandatoryMap.put("txtDraweeName", new Boolean(false));
        mandatoryMap.put("cboStdInstruction", new Boolean(false));
        mandatoryMap.put("txtLodgementId", new Boolean(false));
        mandatoryMap.put("txtInstrumentNo", new Boolean(false));
        mandatoryMap.put("cboActivities", new Boolean(true));
        mandatoryMap.put("cboCustCategory", new Boolean(true));
        mandatoryMap.put("cboTranstype", new Boolean(false));
        mandatoryMap.put("txtAmount", new Boolean(false));
        mandatoryMap.put("cboRemitProdID", new Boolean(false));
        mandatoryMap.put("cboRemitCity", new Boolean(false));
        mandatoryMap.put("cboRemitDraweeBank", new Boolean(false));
        mandatoryMap.put("cboRemitBranchCode", new Boolean(false));
        mandatoryMap.put("txtInstAmt", new Boolean(false));
        mandatoryMap.put("txtRemitFavour", new Boolean(false));
        mandatoryMap.put("txtRemitFavour1", new Boolean(false));
        mandatoryMap.put("tdtRemitInstDate", new Boolean(false));
        mandatoryMap.put("txtInst1", new Boolean(false));
        mandatoryMap.put("txtInst2", new Boolean(false));
    }
    
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}
