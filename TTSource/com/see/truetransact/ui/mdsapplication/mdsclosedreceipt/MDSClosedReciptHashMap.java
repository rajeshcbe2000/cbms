/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * MDSClosedReciptHashMap.java
 */

package com.see.truetransact.ui.mdsapplication.mdsclosedreceipt;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class MDSClosedReciptHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public MDSClosedReciptHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtNetAmtPaid", new Boolean(false));
        mandatoryMap.put("txtInterest", new Boolean(false));
        mandatoryMap.put("txtDiscountAmt", new Boolean(false));
        mandatoryMap.put("txtBonusAmt", new Boolean(false));
        mandatoryMap.put("txtPenalAmtPayable", new Boolean(false));
        mandatoryMap.put("txtInstPayable", new Boolean(false));
        mandatoryMap.put("txtNoOfInstToPaay", new Boolean(false));
        mandatoryMap.put("txtTotalInstAmt", new Boolean(false));
        mandatoryMap.put("txtPendingInst", new Boolean(false));
        mandatoryMap.put("txtInstAmt", new Boolean(false));
        mandatoryMap.put("txtCurrentInstNo", new Boolean(false));
        mandatoryMap.put("txtNoOfInst", new Boolean(false));
        mandatoryMap.put("tdtChitStartDt", new Boolean(false));
        mandatoryMap.put("rdoPrizedMember_Yes", new Boolean(false));
        mandatoryMap.put("txtBonusAmtAvail", new Boolean(false));
        mandatoryMap.put("txtDivisionNo", new Boolean(false));
        mandatoryMap.put("txtSchemeName", new Boolean(true));
        mandatoryMap.put("txtChittalNo", new Boolean(true));
        mandatoryMap.put("txtNoticeAmt", new Boolean(false));
        mandatoryMap.put("txtAribitrationAmt", new Boolean(false));
        mandatoryMap.put("txtEarlierMember", new Boolean(false));
        mandatoryMap.put("txtChangedInst", new Boolean(false));
        mandatoryMap.put("txtEarlierMemberName", new Boolean(false));
        mandatoryMap.put("chkThalayal", new Boolean(false));
        mandatoryMap.put("chkMunnal", new Boolean(false));
        mandatoryMap.put("chkMemberChanged", new Boolean(false));
        mandatoryMap.put("tdtChangedDate", new Boolean(false));
        mandatoryMap.put("txtPaidAmt", new Boolean(false));
        mandatoryMap.put("txtPaidInst", new Boolean(false));
        mandatoryMap.put("tdtPaidDate", new Boolean(false));
        
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}
