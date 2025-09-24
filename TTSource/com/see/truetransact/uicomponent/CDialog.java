/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CDialog.java
 *
 * Created on July 28, 2003, 10:37 AM
 */

package com.see.truetransact.uicomponent;

/**
 * @author  annamalai_t1
 * @author bala
 */
public class CDialog extends javax.swing.JDialog {
    /**
     * CDialog constructor comment.
     */
    public CDialog() {
        super();
    }
    /**
     * CDialog constructor comment.
     * @param owner java.awt.Dialog
     */
    public CDialog(java.awt.Dialog owner) {
        super(owner);
    }
    /**
     * CDialog constructor comment.
     * @param owner java.awt.Dialog
     * @param title java.lang.String
     */
    public CDialog(java.awt.Dialog owner, String title) {
        super(owner, title);
    }
    /**
     * CDialog constructor comment.
     * @param owner java.awt.Dialog
     * @param title java.lang.String
     * @param modal boolean
     */
    public CDialog(java.awt.Dialog owner, String title, boolean modal) {
        super(owner, title, modal);
    }
    /**
     * CDialog constructor comment.
     * @param owner java.awt.Dialog
     * @param modal boolean
     */
    public CDialog(java.awt.Dialog owner, boolean modal) {
        super(owner, modal);
    }
    /**
     * CDialog constructor comment.
     * @param owner java.awt.Frame
     */
    public CDialog(java.awt.Frame owner) {
        super(owner);
    }
    /**
     * CDialog constructor comment.
     * @param owner java.awt.Frame
     * @param title java.lang.String
     */
    public CDialog(java.awt.Frame owner, String title) {
        super(owner, title);
    }
    /**
     * CDialog constructor comment.
     * @param owner java.awt.Frame
     * @param title java.lang.String
     * @param modal boolean
     */
    public CDialog(java.awt.Frame owner, String title, boolean modal) {
        super(owner, title, modal);
    }
    /**
     * CDialog constructor comment.
     * @param owner java.awt.Frame
     * @param modal boolean
     */
    public CDialog(java.awt.Frame owner, boolean modal) {
        super(owner, modal);
    }
    /**
     * CDialog constructor comment.
     * @param owner java.awt.Frame
     * @param modal boolean
     */
    public CDialog(CInternalFrame owner, boolean modal) {
        super();
        setModal(modal);
    }
}
