/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MultiLevelControlHashMap.java
 * 
 * Created on Mon Sep 13 11:26:04 GMT+05:30 2004
 */

package com.see.truetransact.ui.sysadmin.levelcontrol.multilevel;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class MultiLevelControlHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public MultiLevelControlHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtAmount", new Boolean(true));
        mandatoryMap.put("txtNoOfPersons", new Boolean(true));
        mandatoryMap.put("txtLevelID", new Boolean(false));
        mandatoryMap.put("chkClearingCredit", new Boolean(false));
        mandatoryMap.put("chkTransferCredit", new Boolean(false));
        mandatoryMap.put("txtExpression", new Boolean(true));
        mandatoryMap.put("cboCondition", new Boolean(true));
        mandatoryMap.put("chkCashCredit", new Boolean(false));
        mandatoryMap.put("chkCashDebit", new Boolean(false));
        mandatoryMap.put("chkTransferDebit", new Boolean(false));
        mandatoryMap.put("chkClearingDebit", new Boolean(false));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}
