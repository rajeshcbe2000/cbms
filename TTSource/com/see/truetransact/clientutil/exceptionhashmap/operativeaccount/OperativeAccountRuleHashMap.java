/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * operativeaccountRuleHashMap.java
 *
 * Created on May 19, 2004, 12:28 PM
 */

package com.see.truetransact.clientutil.exceptionhashmap.operativeaccount;
import java.util.HashMap;

import com.see.truetransact.clientutil.exceptionhashmap.ExceptionHashMap;
import com.see.truetransact.commonutil.exceptionconstants.operativeaccount.OperativeAccountConstants;

/**
 *
 * @author  rahul
 */
public class OperativeAccountRuleHashMap implements ExceptionHashMap {
    private HashMap exceptionMap;
    
    /** Creates a new instance of operativeaccountRuleHashMap */
    public OperativeAccountRuleHashMap() {
        exceptionMap = new HashMap();
        exceptionMap.put(OperativeAccountConstants.CHEQUENOTISSUED, "This Cheque(s) is not Issued.");
        exceptionMap.put(OperativeAccountConstants.CHEQUECLEARED, "This Cheque(s) is Already Cleared.");
        exceptionMap.put(OperativeAccountConstants.CHEQUECLEARED_NA, "This Cheque(s) is Sent for Clearing" + "\n" + 
                         "Pending for Authorization");
        exceptionMap.put(OperativeAccountConstants.CHEQUESTOPPED, "This Cheque(s) is Already Stopped.");
        exceptionMap.put(OperativeAccountConstants.CHEQUELOOSELEAF, "This Leaf No. is Already Issued.");
        exceptionMap.put(OperativeAccountConstants.CHEQUEREVOK_NA, "This Leaf No. is Revoked." + "\n" + 
                         "Pending for Authorization");
        exceptionMap.put(OperativeAccountConstants.FREEZE, " Clear Balance Is Less Than Input Amount");
        
        exceptionMap.put(OperativeAccountConstants.CHEQUESTOPPED_NA, "This Cheque(s) submitted to be Stopped." + "\n" + 
                         "Pending for Authorization");
        

         // Cheque Books Danger Level uses this
        exceptionMap.put(OperativeAccountConstants.BOOKS_DANGER_LEVEL, "Available Cheque Books are less than the Danger Level");
        
        // Cheque Books Available Level uses this
        exceptionMap.put(OperativeAccountConstants.BOOKS_AVAIL_LEVEL, "Available Cheque Books are less than the No of Books Ordered");
        
        exceptionMap.put(OperativeAccountConstants.EXCEPTION2, "Exception2 occurred");
        exceptionMap.put(OperativeAccountConstants.EXCEPTION3, "Exception3 occurred");
    }

    public HashMap getExceptionHashMap(){
        return this.exceptionMap;
    }    
}
