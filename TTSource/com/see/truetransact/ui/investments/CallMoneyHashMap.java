/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * ShareProductHashMap.java
 * 
 * Created on Mon Apr 11 12:14:43 IST 2005
 */

package com.see.truetransact.ui.investments;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class CallMoneyHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public CallMoneyHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtInvestmentID", new Boolean(true));
        mandatoryMap.put("txtInvestmentName", new Boolean(true));
        mandatoryMap.put("tdtlIssueDt", new Boolean(true));
        mandatoryMap.put("cboInvestmentBehaves", new Boolean(true));
        mandatoryMap.put("tdtMaturityDate", new Boolean(true));
        mandatoryMap.put("txtFaceValue", new Boolean(true));
        mandatoryMap.put("cboInterestPaymentFrequency", new Boolean(true));
        mandatoryMap.put("txtCouponRate", new Boolean(false));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}
