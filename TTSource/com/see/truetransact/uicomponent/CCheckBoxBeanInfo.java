/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * CCheckBoxBeanInfo.java
 *
 * Created on July 28, 2003, 10:57 AM
 */

package com.see.truetransact.uicomponent;

import java.awt.Image;

/**
 * @author annamalai_t1
 */


public class CCheckBoxBeanInfo extends java.beans.SimpleBeanInfo {
     private Image icon;
     /** Creates a new instance of CCheckBoxBeanInfo & load the Icon to display */
     public CCheckBoxBeanInfo() {
        icon = loadImage("/com/see/truetransact/uicomponent/images/CCheckBoxColor16.gif");
    }
    
    /** To return the Icon */    
    public Image getIcon(int type)
    {
        return icon;
    } 
    
}
