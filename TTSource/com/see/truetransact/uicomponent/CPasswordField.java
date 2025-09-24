/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * CPasswordField.java
 *
 * Created on July 28, 2003, 10:37 AM
 */

package com.see.truetransact.uicomponent;

import java.awt.event.KeyEvent;
/**
 *
 * @author  annamalai_t1, Bala
 */
public class CPasswordField extends javax.swing.JPasswordField {
    private String helpMessage;
    private javax.swing.JLabel lblDisplay;
//    private StringBuffer pwd = null;
//    private char PWD_CHAR = '?';
    /** Creates a new instance of CPasswordField */
    public CPasswordField() {
//        pwd = new StringBuffer();
        helpFocus();
    }
    
    private void helpFocus() {
        setPreferredSize(new java.awt.Dimension(100, 21));
        addFocusListener(new HelpFocusAdapter());
//        addKeyListener(new java.awt.event.KeyAdapter() {
//            public void keyTyped(java.awt.event.KeyEvent evt) {
//                if (evt.getKeyChar() == '\n') {
//                    nextFocus();
//                } else {
//                    char keyChar = evt.getKeyChar();
//                    if (keyChar != KeyEvent.VK_BACK_SPACE && keyChar != KeyEvent.VK_DELETE){
//                        pwd.append(keyChar);
//                        evt.setKeyChar(PWD_CHAR);
//                    } else {
//                        setText("");
//                    }
//                }
//            }
//        });
    }
    
//    public char[] getPassword() {
//        return pwd.toString().toCharArray();
//    }
//    
//    public String getText() {
//        return pwd.toString().trim();
//    }
//
//    public void setText(String strPwd) {
//        pwd = new StringBuffer();
//        pwd.append(strPwd);
//        StringBuffer tmp = new StringBuffer();
//        for (int i=0, j=strPwd.length(); i < j; i++) {
//            tmp.append(PWD_CHAR);
//        }
//        super.setText(tmp.toString());
//    }
    private class HelpFocusAdapter extends java.awt.event.FocusAdapter {
        public void focusGained(java.awt.event.FocusEvent evt) {
            if (lblDisplay != null) lblDisplay.setText(helpMessage);
        }
        public void focusLost(java.awt.event.FocusEvent evt) {
            if (lblDisplay != null) lblDisplay.setText("");
//            System.out.println("pwd : " + getText());
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
