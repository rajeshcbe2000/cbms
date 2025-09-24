/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * CLabelBeanInfo.java
 *
 * Created on July 28, 2003, 10:57 AM
 */

package com.see.truetransact.uicomponent;

import java.awt.Image;

/**
 * @author annamalai_t1
 */


public class CLabelBeanInfo extends java.beans.SimpleBeanInfo {
     private Image icon;
     /** Creates a new instance of CLabelBeanInfo & load the Icon to display */
     public CLabelBeanInfo() {
        icon = loadImage("/com/see/truetransact/uicomponent/images/CLabelColor16.gif");
    }
    
    /** To return the Icon */    
    public Image getIcon(int type)
    {
        return icon;
    } 
    
}
