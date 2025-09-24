/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * RTGSOtherBankBranchHashMap.java
 */
package com.see.truetransact.ui.product.remittance;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class RTGSOtherBankBranchHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public RTGSOtherBankBranchHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtRepayScheduleMode", new Boolean(false));
        mandatoryMap.put("cboIntGetFrom", new Boolean(true));
        mandatoryMap.put("txtAcct_Name", new Boolean(false));
        mandatoryMap.put("tdtDisbursement_Dt", new Boolean(false));
        mandatoryMap.put("cboAddrType_PoA", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}
