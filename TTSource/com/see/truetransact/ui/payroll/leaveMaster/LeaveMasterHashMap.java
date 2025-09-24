/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LeaveMasterHashMap.java
 *
 */
package com.see.truetransact.ui.payroll.leaveMaster;

/**
 *
 * @author anjuanand
 */
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
import java.util.HashMap;

public class LeaveMasterHashMap implements UIMandatoryHashMap {

    private HashMap mandatoryMap;

    public LeaveMasterHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtLeaveDedPerDay", new Boolean(true));
        mandatoryMap.put("cboLeaveDescription", new Boolean(true));
    }

    public HashMap getMandatoryHashMap() {
        return this.mandatoryMap;
    }
}
