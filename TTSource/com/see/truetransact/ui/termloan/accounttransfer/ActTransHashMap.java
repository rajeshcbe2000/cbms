/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ActTransHashMap.java
 *
 * Created on December 23, 2004, 5:34 PM
 */
package com.see.truetransact.ui.termloan.accounttransfer;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
/**
 *
 * @author  152713
 */
public class ActTransHashMap implements UIMandatoryHashMap{
    private HashMap mandatoryMap;
    /** Creates a new instance of PowerOfAttorneyHashMap */
    public ActTransHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboBankName", new Boolean(true));
        mandatoryMap.put("cboBranchName", new Boolean(true));
        mandatoryMap.put("txtRefNo", new Boolean(true));
        mandatoryMap.put("txtAmt", new Boolean(true));
        mandatoryMap.put("txtSecDepRec", new Boolean(false));  // true changed as false by Rajesh.
        mandatoryMap.put("txtPoDdNo", new Boolean(true));
        mandatoryMap.put("tdtPoDdDate", new Boolean(false));
        mandatoryMap.put("txtPoDdAmt", new Boolean(false));
        mandatoryMap.put("txtRemarks", new Boolean(false));
//        mandatoryMap.put("cboBounReason", new Boolean(false));
//        mandatoryMap.put("txtRemarks", new Boolean(false));
//        mandatoryMap.put("txtRemark_PowerAttroney", new Boolean(false));
//        mandatoryMap.put("txtArea_PowerAttroney", new Boolean(false));
//        mandatoryMap.put("txtPhone_PowerAttroney", new Boolean(false));
//        mandatoryMap.put("txtCustID_PoA", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap() {
        return this.mandatoryMap;
    }
    
}
