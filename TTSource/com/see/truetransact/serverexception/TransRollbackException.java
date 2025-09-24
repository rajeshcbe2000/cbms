/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * TransRollbackException.java
 *
 * Created on October 10, 2003, 12:56 PM
 */

package com.see.truetransact.serverexception;

/** TransRollbackException is the super class for all Exception classes
 * in TT Package. So, all Exception classes must extend this
 * class for implementation.
 *
 * Usage:
 * For the empty Constructor,
 *      throw new TransRollbackException();
 *
 * For the parameterized Constructor,
 *      throw new TransRollbackException(java.lang.String message);
 * @author Balachandar
 */
import com.see.truetransact.commonutil.TTException;

public class TransRollbackException extends TTException{
    
    /** Creates a new instance of TransRollbackException */
    public TransRollbackException() {
        super();
    }
    
    /** Creates a new instance of TransRollbackException with a message
     * @param message Custom message can be given here
     */    
    public TransRollbackException(String message) {
        super(message);
    }
    
    /** Creates a new instance of TransRollbackException with a exception
     * @param exception 
     */    
    public TransRollbackException(Exception exception) {
        super(exception);
        exception.printStackTrace();
    }
}
