/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved..
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * CEditorPane.java
 *
 * Created on January 3, 2005, 10:37 AM
 */

package com.see.truetransact.uicomponent;

/**
 *
 * @author  152699
 */
public class CEditorPane extends javax.swing.JEditorPane {
    private String helpMessage;
    private javax.swing.JLabel lblDisplay;
    
    /** Creates a new instance of CButton */
    public CEditorPane() {
        helpFocus();
    }
    
    private void helpFocus() {
        addFocusListener(new HelpFocusAdapter());
    }
    
    private class HelpFocusAdapter extends java.awt.event.FocusAdapter {
        public void focusGained(java.awt.event.FocusEvent evt) {
            if (lblDisplay != null) lblDisplay.setText(helpMessage);
        }
        public void focusLost(java.awt.event.FocusEvent evt) {
            if (lblDisplay != null) lblDisplay.setText("");
        }
    }
    
    public void setHelpMessage(javax.swing.JLabel lblDisplay, String helpMessage) {
        this.helpMessage = helpMessage;
        this.lblDisplay = lblDisplay;
    }
    public String getHelpMessage() {
        return this.helpMessage;
    }    
}
