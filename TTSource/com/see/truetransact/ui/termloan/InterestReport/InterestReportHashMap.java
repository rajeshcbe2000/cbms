/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TransferHashMap.java
 * 
 * Created on Wed May 12 18:34:23 GMT+05:30 2004
 */

package com.see.truetransact.ui.termloan.InterestReport;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class InterestReportHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public InterestReportHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboProductType",new Boolean(true));
        mandatoryMap.put("cboProductID",new Boolean(true));
        mandatoryMap.put("txtAccountNumber",new Boolean(true));
        mandatoryMap.put("cboChargesType",new Boolean(true));
        mandatoryMap.put("tdtChargesDate",new Boolean(true));
        mandatoryMap.put("txtChargesAmount",new Boolean(true));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}
