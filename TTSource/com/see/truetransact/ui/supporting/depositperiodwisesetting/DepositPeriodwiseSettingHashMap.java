/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * DepositPeriodwiseSettingHashMap.java
 */
package com.see.truetransact.ui.supporting.depositperiodwisesetting;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class DepositPeriodwiseSettingHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public DepositPeriodwiseSettingHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboPeriodType", new Boolean(true));
        mandatoryMap.put("txtPeriodName", new Boolean(true));
        mandatoryMap.put("txtPeriodFrom", new Boolean(true));
        mandatoryMap.put("txtPeriodTo", new Boolean(true));
        mandatoryMap.put("txtPriority", new Boolean(true));
        
        mandatoryMap.put("txtamountrange", new Boolean(true));
        mandatoryMap.put("txtfromamount", new Boolean(true));
        mandatoryMap.put("txttoamount", new Boolean(true));
        mandatoryMap.put("txtpriority", new Boolean(true));
        //  mandatoryMap.put("txtPriority", new Boolean(true));
        
        mandatoryMap.put("txtamountrange1", new Boolean(true));
        mandatoryMap.put("txtfromamount1", new Boolean(true));
        mandatoryMap.put("txttoamount1", new Boolean(true));
        mandatoryMap.put("txtpriority1", new Boolean(true));
        
         mandatoryMap.put("txtPriority2", new Boolean(true));
        mandatoryMap.put("txtdesc", new Boolean(true));
        mandatoryMap.put("txtPeriodFrom2", new Boolean(true));
        mandatoryMap.put("txtPeriodTo2", new Boolean(true));
        
        
        mandatoryMap.put("txtdoubtfrom", new Boolean(true));
        mandatoryMap.put("txtdoubtto", new Boolean(true));
        mandatoryMap.put("txtbadfrom", new Boolean(true));
        mandatoryMap.put("txtbadto", new Boolean(true));
        mandatoryMap.put("txtdocdoubtfrom", new Boolean(true));
        mandatoryMap.put("txtdocdoubtto", new Boolean(true));
        mandatoryMap.put("txtdocbadfrom", new Boolean(true));
        mandatoryMap.put("txtdocbadto", new Boolean(true));
        mandatoryMap.put("txtdoubtnarra", new Boolean(true));
        mandatoryMap.put("txtbadnarra", new Boolean(true));
        mandatoryMap.put("txtdocdoubtnara", new Boolean(true));
        mandatoryMap.put("txtdocbadnara", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}
