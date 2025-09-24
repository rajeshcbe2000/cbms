/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * GeneralLedgerRuleHashMap.java
 *
 * Created on July 2, 2004, 12:51 PM
 */

package com.see.truetransact.clientutil.exceptionhashmap.generalledger;
import java.util.HashMap;

import com.see.truetransact.clientutil.exceptionhashmap.ExceptionHashMap;
import com.see.truetransact.commonutil.exceptionconstants.generalledger.GeneralLedgerConstants;

/**
 *
 * @author  rahul
 */
public class GeneralLedgerRuleHashMap implements ExceptionHashMap {
    private HashMap exceptionMap;
    
    /** Creates a new instance of GeneralLedgerRuleHashMap */
    public GeneralLedgerRuleHashMap() {
        exceptionMap = new HashMap();
        exceptionMap.put(GeneralLedgerConstants.ACCOUNTHEAD, "This Account Head Does Not Exist");
        exceptionMap.put(GeneralLedgerConstants.ACCOUNTHEADPARAM, "The Parameters For This Account Head Does Not Exist");
    }
    
    public HashMap getExceptionHashMap(){
        return this.exceptionMap;
    }
    
}
