/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * COptionPane.java
 *
 * Created on July 28, 2003, 10:37 AM
 */

package com.see.truetransact.uicomponent;

/**
 *
 * @author  annamalai_t1
 */
public class COptionPane extends javax.swing.JOptionPane {
    
    /** Creates a new instance of COptionPane */
    public COptionPane() {
    }
    
    public COptionPane(Object obj,int messageType,int optionType) {
        super(obj,messageType,optionType);
    }
    
}
