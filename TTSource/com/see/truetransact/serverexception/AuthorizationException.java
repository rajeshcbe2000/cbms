/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 */
package com.see.truetransact.serverexception;

/*
 * AuthorizationException.java
 *
 * Created on July 9, 2003, 3:38 PM
 */
import com.see.truetransact.commonutil.TTException;

/** AuthorizationException class is used to throw exception under the following
 * situations,
 * 1. When an unauthorized person tries to logon to the system
 *
 * Usage:
 * Generally, this class is used to throw exceptions as given below,
 * 1.Consider that a method named 'logon' is invoking a method named 'validate' for
 * authorization
 *
 * public void logon(String userName, String password) throws Exception
 * {
 *    if(!validate(userName, password))
 *        throw new AuthorizationException();
 * }
 *
 * private boolean validate(String userName, String password)
 * {
 *    boolean valid;
 *    Some code for checking...
 *
 *    return valid;
 * }
 *
 * The thrown exception will be caught in the top level class which initiates the
 * method invocation.
 *
 * 2. It can also be thrown with a message with the parameterized constructor,
 *
 *    public void logon(String userName, String password) throws Exception
 *    {
 *        if(!validate(userName, password))
 *            throw new AuthorizationException("Unauthorized person");
 *    }
 * @author karthik
 */
public class AuthorizationException extends TTException{
    /** Creates a new instance of AuthorizationException */
    public AuthorizationException(){
        super();
    }
    
    /** Creates a new instance of AuthorizationException with a message
     * @param message For accepting custom messages
     */    
    public AuthorizationException(String message){
        super(message);
    }
    
    /** Creates a new instance of AuthorizationException with the Exception
     *  This is useful to re-throw an exception
     * @param exception 
     */    
    public AuthorizationException(Exception exception) {
        super(exception);
    }  
}
