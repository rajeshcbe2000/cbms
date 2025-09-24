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

package com.see.truetransact.ui.inventory;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class ProductMasterHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public ProductMasterHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtProductCode", new Boolean(true));
        mandatoryMap.put("txtSalesReturnAcHd", new Boolean(true));
        mandatoryMap.put("txtQty", new Boolean(true));
        mandatoryMap.put("txtTaxAcHd", new Boolean(true));
        mandatoryMap.put("txtSellingPrice", new Boolean(true));
        mandatoryMap.put("txtReorderLevel", new Boolean(true));
        mandatoryMap.put("txtSalesAcHd", new Boolean(true));
        mandatoryMap.put("txtPurchasePrice", new Boolean(true));
        mandatoryMap.put("txtPurchaseReturnAcHd", new Boolean(true));
        mandatoryMap.put("txtProductDesc", new Boolean(true));
        mandatoryMap.put("txtPurchaseAcHd", new Boolean(true));
        mandatoryMap.put("cboUnit", new Boolean(true));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}
