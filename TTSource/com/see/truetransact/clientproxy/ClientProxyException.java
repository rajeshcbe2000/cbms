/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * ClientProxyException.java
 *
 * Created on June 23, 2003, 12:23 PM
 */

package com.see.truetransact.clientproxy;

import com.see.truetransact.commonutil.TTException;
/**
 * @author  Balachandar
 *
 * This class implements an exception which can wrapped a lower-level exception.
 */

public class ClientProxyException extends TTException {
    private Exception exception;
    
    /**
     * Creates a new ClientProxyException wrapping another exception, and with a detail message.
     * @param message the detail message.
     * @param exception the wrapped exception.
     */
    public ClientProxyException(String message, Exception exception) {
        super(message);
        this.exception = exception;
        return;
    }
    
    /**
     * Creates a ClientProxyException with the specified detail message.
     * @param message the detail message.
     */
    public ClientProxyException(String message) {
        this(message, null);
        return;
    }
    
    /**
     * Creates a new ClientProxyException wrapping another exception, and with no detail message.
     * @param exception the wrapped exception.
     */
    public ClientProxyException(Exception exception) {
        this(null, exception);
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
        if (exception instanceof ClientProxyException) {
            return ((ClientProxyException) exception).getRootCause();
        }
        return exception == null ? this : exception;
    }
    
    public String toString() {
        if (exception instanceof ClientProxyException) {
            return ((ClientProxyException) exception).toString();
        }
        return exception == null ? super.toString() : exception.toString();
    }
}
