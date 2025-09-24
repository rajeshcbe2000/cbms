/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LeaveDetailsHashMap.java
 *
 */
package com.see.truetransact.ui.payroll.leaveDetails;

/**
 *
 * @author anjuanand
 */
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
import java.util.HashMap;

public class LeaveDetailsHashMap implements UIMandatoryHashMap {

    private HashMap mandatoryMap;

    public LeaveDetailsHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtEmployeeId", new Boolean(true));
        mandatoryMap.put("cboLeaveDescription", new Boolean(true));
        mandatoryMap.put("tdtLeaveDate", new Boolean(true));
        mandatoryMap.put("txtLeaveRemarks", new Boolean(false));
    }

    public HashMap getMandatoryHashMap() {
        return this.mandatoryMap;
    }
}
