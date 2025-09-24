/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * MDSChangeofMemberHashMap.java
 */
package com.see.truetransact.ui.mdsapplication.mdschangeofmember;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class MDSChangeofMemberHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public MDSChangeofMemberHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtInstallmentNo", new Boolean(false));
        mandatoryMap.put("txtRemarks", new Boolean(false));
        mandatoryMap.put("txtTotalAmount", new Boolean(false));
        mandatoryMap.put("tdtEffetiveDt", new Boolean(false));
        mandatoryMap.put("txtSchemeName", new Boolean(true));
        mandatoryMap.put("txtNewMemberNo", new Boolean(false));
        mandatoryMap.put("txtChittalNo", new Boolean(false));
        mandatoryMap.put("txtDivisionNo", new Boolean(false));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}
