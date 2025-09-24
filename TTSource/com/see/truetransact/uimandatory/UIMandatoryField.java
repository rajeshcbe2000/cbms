/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * UIMandatoryField.java
 *
 * Created on July 26, 2003, 1:13 PM
 */

package com.see.truetransact.uimandatory;
import java.util.HashMap;
/** This interface should be implemented in all the screens involving components to
 * ensure the Mandatory behaviour of components
 * @author karthik
 */
public interface UIMandatoryField {
    
    /** To create the HashMap object created in the Screens
     * @return
     */    
    public void setMandatoryHashMap();
    
    /** To return the HashMap object created in the Screens
     * @return
     */    
    public HashMap getMandatoryHashMap();
}
