/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * TdsDeductionHashMap.java
 */

package com.see.truetransact.ui.deposit.tds;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class TdsDeductionHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public TdsDeductionHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtDepositNo", new Boolean(true));
        mandatoryMap.put("txtDepositAmount", new Boolean(true));
        mandatoryMap.put("dateMaturityDate", new Boolean(true));
        mandatoryMap.put("txtTdsAmount", new Boolean(true));
        mandatoryMap.put("dateTDSStartDate", new Boolean(false));
        mandatoryMap.put("cboProductID", new Boolean(true));
        mandatoryMap.put("cboDepositSubNo", new Boolean(true));
        mandatoryMap.put("txtInterestPayable", new Boolean(true));
        mandatoryMap.put("cboCollectionType", new Boolean(true));
        mandatoryMap.put("txtInterestPaid", new Boolean(true));
        mandatoryMap.put("txtDebitAccHead", new Boolean(true));
        mandatoryMap.put("dateTDSEndDate", new Boolean(false));
        mandatoryMap.put("txtRemarks", new Boolean(true));
        mandatoryMap.put("dateDepositDate", new Boolean(true));
        mandatoryMap.put("txtInterestAccrued", new Boolean(true));
        mandatoryMap.put("txtDebitAccNum", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}
