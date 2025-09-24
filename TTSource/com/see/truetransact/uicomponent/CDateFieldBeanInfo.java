/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CDateFieldBeanInfo.java
 *
 * Created on July 18, 2003, 6:30 PM
 */

package com.see.truetransact.uicomponent;

import java.awt.Image;
import java.beans.SimpleBeanInfo;

/**
 *
 * @author  karthik
 */
public class CDateFieldBeanInfo extends SimpleBeanInfo{
     private Image icon;
     /** Creates a new instance of CDateFieldBeanInfo & load the Icon to display */
     public CDateFieldBeanInfo() {
        icon = loadImage("/com/see/truetransact/uicomponent/images/calendar.jpg");
    }
    
    /** To return the Icon */    
    public Image getIcon(int type)
    {
        return icon;
    }

}
