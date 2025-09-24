/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * PartialWithdrawalRuleHashMap.java
 *
 * Created on June 29, 2004, 11:25 AM
 */

package com.see.truetransact.clientutil.exceptionhashmap.deposit.closing;

import java.util.HashMap;

import com.see.truetransact.clientutil.exceptionhashmap.ExceptionHashMap;
import com.see.truetransact.commonutil.exceptionconstants.deposit.closing.DepositClosingConstants;

/**
 *
 * @author  Pinky
 */

public class PartialWithdrawalRuleHashMap implements ExceptionHashMap{
  private HashMap exceptionMap;
    
    /** Creates a new instance of operativeaccountRuleHashMap */
    public PartialWithdrawalRuleHashMap() {
        exceptionMap = new HashMap();
        exceptionMap.put(DepositClosingConstants.NOPWVALUE,"No more Partial Withdrawal allowed");
        exceptionMap.put(DepositClosingConstants.MAXPWAMOUNT,"WithDrawal Amount reached MaxAllowed");
        exceptionMap.put(DepositClosingConstants.YEARPWAMOUNT,"WihtDrawal Amount reached MaxAllowed for this Year");       
    }
    
    public HashMap getExceptionHashMap(){
        return this.exceptionMap;
    }
    
    
}
