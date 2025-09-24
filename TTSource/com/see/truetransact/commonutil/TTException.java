/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TTException.java
 *
 * Created on July 10, 2003, 10:26 AM
 */

package com.see.truetransact.commonutil;

/** TTException is the super class for all Exception classes
 * in TT Package. So, all Exception classes must extend this
 * class for implementation.
 *
 * Usage:
 * For the empty Constructor,
 *      throw new TTException();
 *
 * For the parameterized Constructor,
 *      throw new TTException(java.lang.String message);
 * @author karthik
 */
import java.util.HashMap;
public class TTException extends Exception{
    private HashMap exceptionHashMap;
    /** Creates a new instance of TTException */
    public TTException() {
        super();
    }
    
    /** Creates a new instance of TTException with a message
     * @param message Custom message can be given here
     */    
    public TTException(java.lang.String message) {
        super(message);
    }  
    
    /** Creates a new instance of TTException with the Exception
     *  This is useful to re-throw an exception
     * @param exception 
     */    
    public TTException(Exception exception) {
        super(exception);
    } 
    
    public TTException(HashMap exceptionHashMap){
        super();
        this.exceptionHashMap = exceptionHashMap;
    }
 
    /** Getter for property exceptionHashMap.
     * @return Value of property exceptionHashMap.
     *
     */
    public java.util.HashMap getExceptionHashMap() {
        return exceptionHashMap;
    }
    
    /** Setter for property exceptionHashMap.
     * @param exceptionHashMap New value of property exceptionHashMap.
     *
     */
    public void setExceptionHashMap(java.util.HashMap exceptionHashMap) {
        this.exceptionHashMap = exceptionHashMap;
    }
    
}
