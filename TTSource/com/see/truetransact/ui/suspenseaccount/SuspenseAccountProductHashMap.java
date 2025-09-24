/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ProductMasterHashMap.java
 * 
 * Created on Mon Jun 20 16:50:12 GMT+05:30 2011
 */

package com.see.truetransact.ui.suspenseaccount;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class SuspenseAccountProductHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public SuspenseAccountProductHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtItemCode", new Boolean(true));
        mandatoryMap.put("txtSalesReturnAcHd", new Boolean(false));
        mandatoryMap.put("txtQty", new Boolean(false));
        mandatoryMap.put("txtTaxAcHd", new Boolean(false));
        mandatoryMap.put("txtSellingPrice", new Boolean(false));
        mandatoryMap.put("txtOrderLevel", new Boolean(false));
        mandatoryMap.put("txtSalesAcHd", new Boolean(false));
        mandatoryMap.put("txtPurchasePrice", new Boolean(false));
        mandatoryMap.put("txtPurchaseReturnAcHd", new Boolean(false));
        mandatoryMap.put("txtItemDesc", new Boolean(false));
        mandatoryMap.put("txtPurchaseAcHd", new Boolean(false));
        mandatoryMap.put("cboUnit", new Boolean(false));
        mandatoryMap.put("txtSuspenseProductHead", new Boolean(true));
        mandatoryMap.put("txtSuspenseProdID", new Boolean(true));
        mandatoryMap.put("txtSuspenseProdName", new Boolean(true));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}
