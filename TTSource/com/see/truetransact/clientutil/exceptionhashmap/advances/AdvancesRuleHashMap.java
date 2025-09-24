/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AdvancesRuleHashMap.java
 *
 * Created on May 5, 2005, 1:00 PM
 */

package com.see.truetransact.clientutil.exceptionhashmap.advances;

import java.util.HashMap;

import com.see.truetransact.clientutil.exceptionhashmap.ExceptionHashMap;
import com.see.truetransact.commonutil.exceptionconstants.advances.AdvancesConstants;
/**
 *
 * @author  152713
 */
public class AdvancesRuleHashMap implements ExceptionHashMap{
    private HashMap exceptionMap;
    
    /** Creates a new instance of AdvancesRuleHashMap */
    public AdvancesRuleHashMap() {
        exceptionMap = new HashMap();
        
        // Limit Checking uses this
        exceptionMap.put(AdvancesConstants.ADVANCES_EXCEED_LIMIT, "Available Balance is less than the input Amount");
        exceptionMap.put(AdvancesConstants.ADVANCES_EXPIRY_DATE, "Advances Date has Expired");
    }
    
    public HashMap getExceptionHashMap() {
        return this.exceptionMap;
    }
    
}
