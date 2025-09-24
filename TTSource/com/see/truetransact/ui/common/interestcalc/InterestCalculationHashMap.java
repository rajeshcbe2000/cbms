/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * InterestCalculationHashMap.java
 */

package com.see.truetransact.ui.common.interestcalc;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class InterestCalculationHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public InterestCalculationHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtPrincipal", new Boolean(true));
        mandatoryMap.put("tdtFromDate", new Boolean(false));
        mandatoryMap.put("cboCompounded", new Boolean(false));        
        //added Deposits
        mandatoryMap.put("cboDepositsCompounded", new Boolean(false));        
        
        mandatoryMap.put("cboAccountType", new Boolean(false));
        mandatoryMap.put("rdoInterestOption_Simple", new Boolean(true));
        mandatoryMap.put("txtPenalRate", new Boolean(true));
        mandatoryMap.put("txtInterestCreditHead", new Boolean(true));
        mandatoryMap.put("txtInterestDebitHead", new Boolean(true));
        mandatoryMap.put("cboFloatPrecision", new Boolean(true));
        mandatoryMap.put("txtRateofInterest", new Boolean(true));
        mandatoryMap.put("cboMonth", new Boolean(false));
        mandatoryMap.put("cboRoundingInterest", new Boolean(true));
        mandatoryMap.put("tdtToDate", new Boolean(false));
        mandatoryMap.put("cboYear", new Boolean(false));
        mandatoryMap.put("cboRoundingPrincipal", new Boolean(true));
        mandatoryMap.put("cboGracePeriod", new Boolean(true));
        mandatoryMap.put("txtGracePeriod", new Boolean(true));
        mandatoryMap.put("txtDays", new Boolean(false));
        mandatoryMap.put("txtYears", new Boolean(false));
        mandatoryMap.put("txtMonths", new Boolean(false));
        mandatoryMap.put("rdoPeriodOption_Duration", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}
