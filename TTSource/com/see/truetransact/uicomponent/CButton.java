/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * CButton.java
 *
 * Created on July 28, 2003, 10:37 AM
 */

package com.see.truetransact.uicomponent;

import com.see.truetransact.clientutil.ClientConstants;
/**
 *
 * @author  annamalai_t1
 */
public class CButton extends javax.swing.JButton {
    private String helpMessage;
    private javax.swing.JLabel lblDisplay;
    private int mode;
    
    /** Creates a new instance of CButton */
    public CButton() {
        //this.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        helpFocus();
        setFont(new java.awt.Font("MS Sans Serif",java.awt.Font.PLAIN,13));
    }
    
    private void helpFocus() {
        addFocusListener(new HelpFocusAdapter());
//        addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                btnActionPerformed(evt);
//            }
//        });
    }
    
//    private void btnActionPerformed(java.awt.event.ActionEvent evt) {
//        System.out.println("getName : " + getName());
//        if (getName().equals("btnCancel")) {
//            int yesNo = COptionPane.showConfirmDialog(null, "Do you want to clear?");
//            
//            if (yesNo != COptionPane.YES_OPTION) {
//                return;
//            }
//        }
//    }

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
    
    /**
     * Getter for property mode.
     * @return Value of property mode.
     */
    public int getMode() {
        return mode;
    }
    
    /**
     * Setter for property mode.
     * @param mode New value of property mode.
     */
    public void setMode(int mode) {
        this.mode = mode;
    }
    
    public void setEnabled(boolean isEnable) {
        if (getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE) {
            super.setEnabled(false);
        } else {
            super.setEnabled(isEnable);
        }
    }
    
}
