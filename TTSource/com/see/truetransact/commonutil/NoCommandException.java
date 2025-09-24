/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * NoCommandException.java
 *
 * Created on October 10, 2003, 12:56 PM
 */

package com.see.truetransact.commonutil;

/** TTException is the super class for all Exception classes
 * in TT Package. So, all Exception classes must extend this
 * class for implementation.
 *
 * Usage:
 * For the empty Constructor,
 *      throw new NoCommandException();
 *
 * For the parameterized Constructor,
 *      throw new NoCommandException(java.lang.String message);
 * @author Balachandar
 */
public class NoCommandException extends TTException{
    
    /** Creates a new instance of NoCommandException */
    public NoCommandException() {
        super();
    }
    
    /** Creates a new instance of NoCommandException with a message
     * @param message Custom message can be given here
     */    
    public NoCommandException(String message) {
        super(message);
    }
    
    /** Creates a new instance of NoCommandException with the Exception
     *  This is useful to re-throw an exception
     * @param exception 
     */    
    public NoCommandException(Exception exception) {
        super(exception);
    }  
}
