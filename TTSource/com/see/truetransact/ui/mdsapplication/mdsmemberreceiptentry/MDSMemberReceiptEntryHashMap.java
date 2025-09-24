/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * MDSMemberReceiptEntryHashMap.java
 */
package com.see.truetransact.ui.mdsapplication.mdsmemberreceiptentry;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class MDSMemberReceiptEntryHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public MDSMemberReceiptEntryHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtAribitrationAmt", new Boolean(false));
        mandatoryMap.put("txtTotBonusAmt", new Boolean(false));
        mandatoryMap.put("txtTotPenalAmt", new Boolean(false));
        mandatoryMap.put("txtTotInstAmt", new Boolean(false));
        mandatoryMap.put("txtAmtPaid", new Boolean(false));
        mandatoryMap.put("txtNoticeAmt", new Boolean(false));
        mandatoryMap.put("txtTotInterest", new Boolean(false));
        mandatoryMap.put("txtTotDiscountAmt", new Boolean(false));
        mandatoryMap.put("txtMembershipNo", new Boolean(true));
        mandatoryMap.put("txtApplicantsName", new Boolean(false));
        mandatoryMap.put("cboTransType", new Boolean(false));
        mandatoryMap.put("txtTransactionAmt", new Boolean(false));
        mandatoryMap.put("txtTransProductId", new Boolean(false));
        mandatoryMap.put("cboProductType", new Boolean(false));
        mandatoryMap.put("txtDebitAccNo", new Boolean(false));
        mandatoryMap.put("tdtChequeDate", new Boolean(false));
        mandatoryMap.put("txtChequeNo", new Boolean(false));
        mandatoryMap.put("txtChequeNo2", new Boolean(false));
        mandatoryMap.put("cboInstrumentType", new Boolean(false));
        mandatoryMap.put("txtTokenNo", new Boolean(false));
        mandatoryMap.put("txtEarlierMemNo", new Boolean(false));
        mandatoryMap.put("txtChangedInstNo", new Boolean(false));
        mandatoryMap.put("txtEarlierMemName", new Boolean(false));
        mandatoryMap.put("chkThalayalMember", new Boolean(false));
        mandatoryMap.put("chkMunnalMember", new Boolean(false));
        mandatoryMap.put("chkChangedMember", new Boolean(false));
        mandatoryMap.put("tdtChangedDt", new Boolean(false));
        mandatoryMap.put("txtPaidAmount", new Boolean(false));
        mandatoryMap.put("txtPaidInst", new Boolean(false));
        mandatoryMap.put("tdtPaidDt", new Boolean(false));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}
