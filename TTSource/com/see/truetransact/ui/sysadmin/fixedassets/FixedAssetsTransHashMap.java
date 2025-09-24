/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * FixedAssetsTransHashMap.java
 * 
 * Created on Tue Jan 18 16:22:31 IST 2011
 */

package com.see.truetransact.ui.sysadmin.fixedassets;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class FixedAssetsTransHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public FixedAssetsTransHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboBranchIdMove", new Boolean(true));
        mandatoryMap.put("cboDepartMove", new Boolean(true));
        mandatoryMap.put("txtFloorMove", new Boolean(true));
        mandatoryMap.put("txtBreakageRegion", new Boolean(true));
        mandatoryMap.put("txtSaleAmount", new Boolean(true));
        mandatoryMap.put("tdtSaleDate", new Boolean(true));
        mandatoryMap.put("tdtMovementDate", new Boolean(true));
        mandatoryMap.put("tdtBreakageDate", new Boolean(true));
        mandatoryMap.put("txtFromAssetId", new Boolean(false));
        mandatoryMap.put("txtFaceVal", new Boolean(false));
        mandatoryMap.put("txtAssetIdMove", new Boolean(false));
        mandatoryMap.put("cboSubType", new Boolean(false));
        mandatoryMap.put("cboProductTypeSale", new Boolean(false));
        mandatoryMap.put("txtDepart", new Boolean(false));
        mandatoryMap.put("txtCurrValue", new Boolean(false));
        mandatoryMap.put("txtFaceValueSale", new Boolean(false));
        mandatoryMap.put("txtCurrentValueSale", new Boolean(false));
        mandatoryMap.put("txtSlNo", new Boolean(false));
        mandatoryMap.put("txtFloorBreak", new Boolean(false));
        mandatoryMap.put("txtBranchIdBreak", new Boolean(false));
        mandatoryMap.put("txtDepartBreak", new Boolean(false));
        mandatoryMap.put("cboProductType", new Boolean(false));
        mandatoryMap.put("txtSlNoBreak", new Boolean(false));
        mandatoryMap.put("txtAssetIdSale", new Boolean(false));
        mandatoryMap.put("txtCurrValueBreak", new Boolean(false));
        mandatoryMap.put("txtBranchId", new Boolean(false));
        mandatoryMap.put("txtPurchasedDate", new Boolean(false));
        mandatoryMap.put("txtAssetIdBreak", new Boolean(false));
        mandatoryMap.put("txtFaceValBreak", new Boolean(false));
        mandatoryMap.put("cboSubTypeSale", new Boolean(false));
        mandatoryMap.put("tdtDepDate", new Boolean(false));
        mandatoryMap.put("txtFloor", new Boolean(false));
        mandatoryMap.put("txtToAssetId", new Boolean(false));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}
