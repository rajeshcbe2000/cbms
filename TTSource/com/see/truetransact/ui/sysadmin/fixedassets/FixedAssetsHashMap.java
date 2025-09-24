/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * FixedAssetsHashMap.java
 */
package com.see.truetransact.ui.sysadmin.fixedassets;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class FixedAssetsHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public FixedAssetsHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtAssetType", new Boolean(true));
        mandatoryMap.put("txtAssetDesc", new Boolean(true));
        mandatoryMap.put("txtProvision", new Boolean(true));
        mandatoryMap.put("cboTransBran", new Boolean(true));
        mandatoryMap.put("cboBranchId", new Boolean(true));
        mandatoryMap.put("txtpurchaseDebit", new Boolean(true));
        mandatoryMap.put("txtProvDebit", new Boolean(true));
        mandatoryMap.put("txtSellAcHdID", new Boolean(true));
        mandatoryMap.put("txtNullifyingDebit", new Boolean(true));
        mandatoryMap.put("txtNullifyingCredit", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}
