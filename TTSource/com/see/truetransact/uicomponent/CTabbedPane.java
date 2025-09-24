/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * CTabbedPane.java
 *
 * Created on July 28, 2003, 10:37 AM
 */

package com.see.truetransact.uicomponent;

import java.util.HashMap;
/**
 *
 * @author  annamalai_t1
 * @author  Bala
 */
public class CTabbedPane extends javax.swing.JTabbedPane {
    HashMap tabMap = new HashMap();
    
    /** Creates a new instance of CTabbedPane */
    public CTabbedPane() {
        resetVisits();
        setFont(new java.awt.Font("MS Sans Serif",java.awt.Font.PLAIN,13));
        
        addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabbedPaneStateChanged(evt);
            }
        });   
    }

    private void tabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {
        tabMap.put(String.valueOf(getSelectedIndex()),new Boolean(true));
    }
    
    public String isAllTabsVisited() {
        StringBuffer strB = new StringBuffer();
        Boolean yes = new Boolean(true);
        for (int i=0, j=getTabCount(); i < j; i++) {
            if (!tabMap.get(String.valueOf(i)).equals(yes)) {
                strB.append (getTitleAt(i) + " is not visited.\n");
            }
        }
        return strB.toString();
    }

    // The following method added by Rajesh
    public String isAllTabsVisited(String excepTabName) {
        String exTab = excepTabName;
        StringBuffer strB = new StringBuffer();
        Boolean yes = new Boolean(true);
        for (int i=0, j=getTabCount(); i < j; i++) {
            if (!tabMap.get(String.valueOf(i)).equals(yes) && !getTitleAt(i).equals(exTab)) 
            {
                strB.append (getTitleAt(i) + " is not visited.\n");
            }
        }
        return strB.toString();
    }
    
    public void resetVisits() {
        tabMap = new HashMap();
        for (int i=0, j=getTabCount(); i < j; i++) {
            tabMap.put(String.valueOf(i), new Boolean(false));
        }
    }
}
