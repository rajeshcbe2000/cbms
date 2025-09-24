/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * MandatorySuper.java
 *
 * Created on July 25, 2003, 11:06 AM
 */

package com.see.truetransact.uimandatory;
import java.util.HashMap;

/** This interface should be implemented in all <..>HashMap classes for the screens
 * to ensure the Mandatory behaviour of components
 * @author karthik
 */
public interface UIMandatoryHashMap {
     public HashMap getMandatoryHashMap();
}
