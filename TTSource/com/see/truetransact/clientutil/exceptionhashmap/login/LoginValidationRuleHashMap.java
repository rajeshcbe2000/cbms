/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LoginValidationRuleHashMap.java
 *
 * Created on March 14, 2005, 10:33 AM
 */

package com.see.truetransact.clientutil.exceptionhashmap.login;

import java.util.HashMap;

import com.see.truetransact.clientutil.exceptionhashmap.ExceptionHashMap;
import com.see.truetransact.commonutil.exceptionconstants.login.LoginConstants;

/**
 *
 * @author  JK
 */
public class LoginValidationRuleHashMap implements ExceptionHashMap {
    private HashMap exceptionMap;
    
    /** Creates a new instance of GeneralLedgerRuleHashMap */
    public LoginValidationRuleHashMap() {
        exceptionMap = new HashMap();
        exceptionMap.put(LoginConstants.INVALID_USER, "Not a valid User.");
        exceptionMap.put(LoginConstants.BLOCKED_USER, "This User is blocked.");
        exceptionMap.put(LoginConstants.EXPIRED_USER, "User Account is expired.");
        exceptionMap.put(LoginConstants.UNAUTHORIZED_USER, "Not an Authorized User.");
        exceptionMap.put(LoginConstants.SUSPENDED_USER, "User is Temporarily Suspended.");
        exceptionMap.put(LoginConstants.INVALID_TERMINAL_USER, "Not a valid Terminal User.");
        exceptionMap.put(LoginConstants.CONTINUE_AFTER_DAY_BEGIN, "Continue after Day Begin.");
    }

    public HashMap getExceptionHashMap(){
        return this.exceptionMap;
    }
}
