/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ScreenTOComparator.java
 *
 * Created on Created on March 8, 2004,10:28 AM
 */

package com.see.truetransact.ui.sysadmin.group;

import com.see.truetransact.transferobject.sysadmin.group.ScreenMasterTO;
import java.util.Comparator;

/**
 * This class is used to compare two ScreenMasterTO objects lexicographically
 * by name.
 *
 * @author  Pinky
 */

public class ScreenTOComparator implements Comparator {
    
    /**
     * This method is required to implement the Comparator interface.
     * It compares two ScreenMasterTO objects by name.
     *
     * it will return  -1 if ScreenMasterTO1.ScreenName is lexicographically less than
     *          that of ScreenMasterTO2.ScreenName; 1 if it is greater;  0 if the
     *          names match.
     *
     */
    public int compare(Object obj1, Object obj2) {
        
        ScreenMasterTO screenObj1= (ScreenMasterTO) obj1;
        ScreenMasterTO screenObj2 = (ScreenMasterTO) obj2;
        
        return screenObj1.toString().compareTo(screenObj2.toString());
    }
}