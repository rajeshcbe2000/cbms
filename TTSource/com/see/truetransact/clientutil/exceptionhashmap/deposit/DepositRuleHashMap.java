/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DepositsRuleHashMap.java
 *
 * Created on August 10, 2004, 11:57 AM
 */

package com.see.truetransact.clientutil.exceptionhashmap.deposit;
import java.util.HashMap;

import com.see.truetransact.clientutil.exceptionhashmap.ExceptionHashMap;
import com.see.truetransact.commonutil.exceptionconstants.deposit.DepositConstants;

/**
 *
 * @author  rahul
 */
public class DepositRuleHashMap implements ExceptionHashMap {
    private HashMap exceptionMap;
    
    /** Creates a new instance of DepositsRuleHashMap */
    public DepositRuleHashMap() {
        exceptionMap = new HashMap();
        exceptionMap.put(DepositConstants.DUPLICATEGROUP, "These Group(s) are Already Defined: ");
        
        //__ Used in RecurringAmtChageRule...
        exceptionMap.put(DepositConstants.FREQ_NOT_ATTAINED, "Time to Change the Installment Amount not reached");
        exceptionMap.put(DepositConstants.DECREASE_DEPOSIT_AMT, "Deposit Amount can only be Decreased.");
        exceptionMap.put(DepositConstants.INCREASE_DEPOSIT_AMT, "Deposit Amount can only be Increased.");
    }
    
    public HashMap getExceptionHashMap(){
        return this.exceptionMap;
    }
    
}
