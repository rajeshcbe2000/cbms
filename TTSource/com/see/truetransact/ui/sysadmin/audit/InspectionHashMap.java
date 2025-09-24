/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * InspectionHashMap.java
 */

package com.see.truetransact.ui.sysadmin.audit;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class InspectionHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public InspectionHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtStaffPosition", new Boolean(true));
        mandatoryMap.put("txtActualPosition", new Boolean(true));
        mandatoryMap.put("txtBranchCode", new Boolean(true));
        mandatoryMap.put("tdtInspectionConcludedOn", new Boolean(true));
        mandatoryMap.put("txaOtherInfo", new Boolean(true));
        mandatoryMap.put("txtNumberOfManDays", new Boolean(true));
        mandatoryMap.put("tdtInspectionCommencedOn", new Boolean(true));
        mandatoryMap.put("cboCategory", new Boolean(true));
        mandatoryMap.put("cboClassification", new Boolean(true));
        mandatoryMap.put("cboBranchRating", new Boolean(true));
        mandatoryMap.put("cboJobCategory", new Boolean(true));
        mandatoryMap.put("cboWeeklyHoliday", new Boolean(true));
        mandatoryMap.put("tdtPositionAsOn", new Boolean(true));
        mandatoryMap.put("txaInspectingOfficials", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}
