/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * ValidationRuleException.java
 *
 * Created on June 23, 2003, 12:23 PM
 */

package com.see.truetransact.businessrule.validateexception;

import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.CommonConstants;
import java.util.HashMap;

/**
 * @author  Balachandar
 *
 * This class implements an exception which can wrapped a lower-level exception.
 */

public class ValidationRuleException extends TTException {
    private Exception exception;
    
    /**
     * Creates a new ValidationRuleException wrapping another exception, and with a detail message.
     * @param message the detail message.
     * @param exception the wrapped exception.
    public ValidationRuleException(String message, Exception exception) {
        super(message);
        this.exception = exception;
        return;
    }
     */
    
    /**
     * Creates a ValidationRuleException with the specified detail message.
     * @param message the detail message.
     */
    public ValidationRuleException(String message) {
        super(message);
        //, null);
        return;
    }
    
    /**
     * Creates a ValidationRuleException with the specified detail message.
     * @param message the detail message.
     */
    public ValidationRuleException(String message, String data) {
        super();
        HashMap map = new HashMap();
        map.put(CommonConstants.ERR_MESSAGE, message);
        map.put(CommonConstants.ERR_DATA, data);
        setExceptionHashMap(map);
        return;
    }
    
    /**
     * Creates a new ValidationRuleException wrapping another exception, and with no detail message.
     * @param exception the wrapped exception.
     */
    public ValidationRuleException(Exception exception) {
        super(exception);
        return;
    }
    
    /**
     * Gets the wrapped exception.
     *
     * @return the wrapped exception.
     */
    public Exception getException() {
        return exception;
    }
    
    /**
     * Retrieves (recursively) the root cause exception.
     *
     * @return the root cause exception.
     */
    public Exception getRootCause() {
        if (exception instanceof ValidationRuleException) {
            return ((ValidationRuleException) exception).getRootCause();
        }
        return exception == null ? this : exception;
    }
    
    public String toString() {
        if (exception instanceof ValidationRuleException) {
            return ((ValidationRuleException) exception).toString();
        }
        return exception == null ? super.toString() : exception.toString();
    }
}
