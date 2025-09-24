/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * CLabel.java
 *
 * Created on July 28, 2003, 10:37 AM
 */

package com.see.truetransact.uicomponent;

import javax.swing.Icon;
/**
 *
 * @author  annamalai_t1
 */
public class CLabel extends javax.swing.JLabel {
    
    /** Creates a new instance of CLabel */
    public CLabel() {
        setFont(new java.awt.Font("MS Sans Serif",java.awt.Font.PLAIN,13));
    }
    

    /**
     * Defines the icon this component will display.  If
     * the value of icon is null, nothing is displayed.
     * <p>
     * The default value of this property is null.
     * <p>
     * This is a JavaBeans bound property.  
     * 
     * @see #setVerticalTextPosition
     * @see #setHorizontalTextPosition
     * @see #getIcon
     * @beaninfo
     *    preferred: true
     *        bound: true
     *    attribute: visualUpdate true
     *  description: The icon this component will display.
     */
    public void setIcon(Icon icon) {
        super.setIcon(icon);
        firePropertyChange("icon", getIcon(), icon);
        revalidate();
        repaint();
    }
}
