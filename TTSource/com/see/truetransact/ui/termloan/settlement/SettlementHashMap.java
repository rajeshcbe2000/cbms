/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * SettlementHashMap.java
 *
 * Created on December 23, 2004, 5:34 PM
 */

package com.see.truetransact.ui.termloan.settlement;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
/**
 *
 * @author  152713
 */
public class SettlementHashMap implements UIMandatoryHashMap{
    private HashMap mandatoryMap;
    /** Creates a new instance of PowerOfAttorneyHashMap */
    public SettlementHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboBankName", new Boolean(false));
        mandatoryMap.put("cboBranchName", new Boolean(false));
        mandatoryMap.put("txtActNo", new Boolean(true));
        mandatoryMap.put("txtFromChqNo", new Boolean(true));
        mandatoryMap.put("txtToChqNo", new Boolean(false));  // true changed as false by Rajesh.
        mandatoryMap.put("txtQty", new Boolean(true));
        mandatoryMap.put("tdtChqDate", new Boolean(false));
        mandatoryMap.put("txtChqAmt", new Boolean(false));
        mandatoryMap.put("tdtClearingDt", new Boolean(false));
        mandatoryMap.put("cboBounReason", new Boolean(false));
        mandatoryMap.put("txtRemarks", new Boolean(false));
//        mandatoryMap.put("txtRemark_PowerAttroney", new Boolean(false));
//        mandatoryMap.put("txtArea_PowerAttroney", new Boolean(false));
//        mandatoryMap.put("txtPhone_PowerAttroney", new Boolean(false));
//        mandatoryMap.put("txtCustID_PoA", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap() {
        return this.mandatoryMap;
    }
    
}
