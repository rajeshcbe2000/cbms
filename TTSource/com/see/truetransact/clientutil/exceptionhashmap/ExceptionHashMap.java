/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * MandatorySuper.java
 *
 * Created on December 12, 2003, 10:44 AM
 */

package com.see.truetransact.clientutil.exceptionhashmap;
import java.util.HashMap;

/** This interface should be implemented in all <..>HashMap classes for the screens
 * to get the exception message corresponding to the business rule 
 * @author karthik
 */
public interface ExceptionHashMap {
     public HashMap getExceptionHashMap();
}
