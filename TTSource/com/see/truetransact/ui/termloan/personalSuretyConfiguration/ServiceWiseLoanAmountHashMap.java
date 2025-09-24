/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * ServiceWiseLoanAmountHashMap.java
 */

package com.see.truetransact.ui.termloan.personalSuretyConfiguration;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class ServiceWiseLoanAmountHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public ServiceWiseLoanAmountHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtFromServicePeriod", new Boolean(true));
        mandatoryMap.put("txtToServicePeriod", new Boolean(true));
        mandatoryMap.put("txtMaximumLoanAmount", new Boolean(true));
        mandatoryMap.put("txtMinimumLoanAmount", new Boolean(true));
        mandatoryMap.put("tdtFromDate", new Boolean(true));
       
       // mandatoryMap.put("txaRemarks", new Boolean(false));

    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}
