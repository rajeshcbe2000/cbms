/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SupplierMasterHashMap.java
 * 
 * Created on Fri Jun 10 15:39:15 IST 2011
 */

package com.see.truetransact.ui.paddyprocurement;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class PaddyPurchaseMasterHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public PaddyPurchaseMasterHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtCnDNo", new Boolean(true));
        mandatoryMap.put("txtName", new Boolean(true));
        mandatoryMap.put("txtLocalityCode", new Boolean(true));
        mandatoryMap.put("txtLocalityName", new Boolean(true));
        mandatoryMap.put("txtProductCode", new Boolean(false));
        mandatoryMap.put("txtRatePerKg", new Boolean(false));
        mandatoryMap.put("txtKiloGram", new Boolean(false));
        mandatoryMap.put("txtAmount", new Boolean(false));
        mandatoryMap.put("txtlAcreage",new Boolean(false));
        mandatoryMap.put("txtBags",new Boolean(false));
        mandatoryMap.put("tdtBillDate", new Boolean(false));
        mandatoryMap.put("txtAddress", new Boolean(false));
        mandatoryMap.put("tdtPurchaseDate", new Boolean(false));
        mandatoryMap.put("txtTotalAmount", new Boolean(false));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}
