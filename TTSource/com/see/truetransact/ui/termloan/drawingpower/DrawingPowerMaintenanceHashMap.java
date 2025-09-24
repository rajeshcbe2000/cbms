/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * DrawingPowerMaintenanceHashMap.java
 */

package com.see.truetransact.ui.termloan.drawingpower;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class DrawingPowerMaintenanceHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public DrawingPowerMaintenanceHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboCurrentDPMonth", new Boolean(true));
        mandatoryMap.put("txtPurchase", new Boolean(true));
        mandatoryMap.put("txtMargin", new Boolean(true));
        mandatoryMap.put("txtParticularsofGoods", new Boolean(true));
        mandatoryMap.put("dateDateofInspection", new Boolean(true));
        mandatoryMap.put("cboPreviousDPMonth", new Boolean(true));
        mandatoryMap.put("cboStockStmtFreq", new Boolean(true));
        mandatoryMap.put("dateDueDate", new Boolean(true));
        mandatoryMap.put("txtValueofOpeningStock", new Boolean(true));
        mandatoryMap.put("txtCalculatedDrawingPower", new Boolean(true));
        mandatoryMap.put("txtPreviousDPValue", new Boolean(true));
        mandatoryMap.put("dateStockSubmittedDate", new Boolean(true));
        mandatoryMap.put("datePreviousDPValCalOn", new Boolean(true));
        mandatoryMap.put("txtValueofClosingStock", new Boolean(true));
        mandatoryMap.put("txtSales", new Boolean(true));
        mandatoryMap.put("txtPresentStockValue", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}
