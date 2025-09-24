/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * MultipleStandingHashMap.java
 */

package com.see.truetransact.ui.transaction.multipleStanding;
import com.see.truetransact.ui.payroll.payMaster.*;
import com.see.truetransact.ui.termloan.groupLoan.*;
import com.see.truetransact.ui.termloan.SHG.*;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class MultipleStandingHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public MultipleStandingHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtEmployeeID", new Boolean(true));
        mandatoryMap.put("cboPayCodes", new Boolean(true));
        mandatoryMap.put("txtAmount", new Boolean(true));
        mandatoryMap.put("tdtFromDt", new Boolean(true));
        mandatoryMap.put("txtRecoveryMonth", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}
