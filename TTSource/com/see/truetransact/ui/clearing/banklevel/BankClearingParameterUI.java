/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ParameterUI.java
 *
 * Created on March 12, 2004, 5:49 PM
 */
package com.see.truetransact.ui.clearing.banklevel;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Observer;
import java.util.Observable;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.ResourceBundle;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;

/**
 *
 * @author  Ashok Vijayakumar
 */
public class BankClearingParameterUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField {
    
    // Variables declaration - do not modify
    private BankClearingParameterOB observable;
    private HashMap mandatoryMap;
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("com.see.truetransact.ui.clearing.banklevel.BankClearingParameterRB", ProxyParameters.LANGUAGE);
    final int EDIT=0, DELETE=1,CLEA_HD=2,CLEA_SUSP_HD=3,OCRC_HD=4,INCR_HD=5, SCAH=7, ECAH=8,AUTHORIZE=6, VIEW=10,OICC_HD=11;;
    int viewType=-1;
    
    
    // End of variables declaration
    
    /** Creates new form ParameterUI */
    public BankClearingParameterUI() {
        initComponents();
        initStartUp();
    }
    /* methods invoked at the time of new form */
    private void initStartUp() {
        setFieldNames();
        internationalize();
        setObservable();
        setMaximumLength();
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        setHelpButtonEnableDisable(false);
        setMandatoryHashMap();
        setHelpMessage();
        observable.resetForm();
        lblOCInstrumentChargesHD.setVisible(false);
        txtOCInstrumentCharges.setVisible(false);
        lblOCInstrumentCharges.setVisible(false);
        txtOCInstrumentChargesHD.setVisible(false);
        btnOCInstrumentChargesHD.setVisible(false);
    }
    private void setObservable(){
        /* Implementing Singleton pattern */
        observable = BankClearingParameterOB.getInstance();
        observable.addObserver(this);
    }
    
    /* To enable or disable the main New Save Delete buttons  */
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
    }
    
    // To set the maximun length of fields in the UI
    private void  setMaximumLength(){
        txtClearingHD.setMaxLength(32);
        txtOCReturnCharges.setMaxLength(16);
        txtOCReturnCharges.setValidation(new CurrencyValidation(14,2));
        txtICReturnCharges.setMaxLength(16);
        txtICReturnCharges.setValidation(new CurrencyValidation(14,2));
        txtClearingSuspenseHD.setMaxLength(32);
        txtOCReturnChargesHD.setMaxLength(32);
        txtICReturnChargesHD.setMaxLength(32);
        txtOCInstrumentChargesHD.setMaxLength(32);
        txtOCInstrumentCharges.setMaxLength(16);
        txtOCInstrumentCharges.setValidation(new CurrencyValidation(14,2));
        
    }
    
    /* To add the radio buttons */
    private void addRadioButtons(){
        rdoCompleteDay = new CButtonGroup();
        rdoCompleteDay.add(rdoCompleteDay_No);
        rdoCompleteDay.add(rdoCompleteDay_Yes);
        
    }
    
    /* To remove the radio buttons */
    private void removeRadioButtons(){
        rdoCompleteDay.remove(rdoCompleteDay_Yes);
        rdoCompleteDay.remove(rdoCompleteDay_No);
    }
    
    public static void main(String args[]){
        javax.swing.JFrame frame = new javax.swing.JFrame();
        BankClearingParameterUI ui = new BankClearingParameterUI();
        frame.getContentPane().add(ui);
        frame.setSize(500,500);
        frame.setVisible(true);
        ui.setVisible(true);
        frame.show();
        ui.show();
        
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoCompleteDay = new com.see.truetransact.uicomponent.CButtonGroup();
        panParameter = new com.see.truetransact.uicomponent.CPanel();
        lblClearingHD = new com.see.truetransact.uicomponent.CLabel();
        lblOCReturnCharges = new com.see.truetransact.uicomponent.CLabel();
        lblICReturnCharges = new com.see.truetransact.uicomponent.CLabel();
        txtOCReturnCharges = new com.see.truetransact.uicomponent.CTextField();
        txtICReturnCharges = new com.see.truetransact.uicomponent.CTextField();
        lblClearingType = new com.see.truetransact.uicomponent.CLabel();
        panClearingHD = new com.see.truetransact.uicomponent.CPanel();
        txtClearingHD = new com.see.truetransact.uicomponent.CTextField();
        btnClearingHD = new com.see.truetransact.uicomponent.CButton();
        lblClearingSuspenseHD = new com.see.truetransact.uicomponent.CLabel();
        lblOCReturnChargesHD = new com.see.truetransact.uicomponent.CLabel();
        lblICReturnChargesHD = new com.see.truetransact.uicomponent.CLabel();
        panClearingSuspenseHD = new com.see.truetransact.uicomponent.CPanel();
        txtClearingSuspenseHD = new com.see.truetransact.uicomponent.CTextField();
        btnClearingSuspenseHD = new com.see.truetransact.uicomponent.CButton();
        panOCReturnChargesHD = new com.see.truetransact.uicomponent.CPanel();
        txtOCReturnChargesHD = new com.see.truetransact.uicomponent.CTextField();
        btnOCReturnChargesHD = new com.see.truetransact.uicomponent.CButton();
        panICReturnChargesHD = new com.see.truetransact.uicomponent.CPanel();
        txtICReturnChargesHD = new com.see.truetransact.uicomponent.CTextField();
        btnICReturnChargesHD = new com.see.truetransact.uicomponent.CButton();
        txtClearingType = new com.see.truetransact.uicomponent.CTextField();
        lblShortClaimAcHead = new com.see.truetransact.uicomponent.CLabel();
        panShortClaim = new com.see.truetransact.uicomponent.CPanel();
        txtShortClaimAcHead = new com.see.truetransact.uicomponent.CTextField();
        btnShortClaimAcHead = new com.see.truetransact.uicomponent.CButton();
        lblExcessClaimAcHead = new com.see.truetransact.uicomponent.CLabel();
        cPanel2 = new com.see.truetransact.uicomponent.CPanel();
        txtExcessClaimAcHead = new com.see.truetransact.uicomponent.CTextField();
        btnExcessClaimAcHead = new com.see.truetransact.uicomponent.CButton();
        lblCompleteDayEWCOPosting = new com.see.truetransact.uicomponent.CLabel();
        panCompleteDayEWCOPosting = new com.see.truetransact.uicomponent.CPanel();
        rdoCompleteDay_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoCompleteDay_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblOCInstrumentCharges = new com.see.truetransact.uicomponent.CLabel();
        txtOCInstrumentCharges = new com.see.truetransact.uicomponent.CTextField();
        lblOCInstrumentChargesHD = new com.see.truetransact.uicomponent.CLabel();
        panICReturnChargesHD1 = new com.see.truetransact.uicomponent.CPanel();
        txtOCInstrumentChargesHD = new com.see.truetransact.uicomponent.CTextField();
        btnOCInstrumentChargesHD = new com.see.truetransact.uicomponent.CButton();
        chkInstrumentCharges = new com.see.truetransact.uicomponent.CCheckBox();
        tbrParameter = new com.see.truetransact.uicomponent.CToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lbSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace26 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace27 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lbSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace28 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace30 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace31 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrParameter = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptView = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Parameter");
        getContentPane().setLayout(new java.awt.BorderLayout(10, 10));

        panParameter.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panParameter.setLayout(new java.awt.GridBagLayout());

        lblClearingHD.setText("Clearing Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParameter.add(lblClearingHD, gridBagConstraints);

        lblOCReturnCharges.setText("Outward Clearing Return Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParameter.add(lblOCReturnCharges, gridBagConstraints);

        lblICReturnCharges.setText("Inward Clearing Return Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParameter.add(lblICReturnCharges, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParameter.add(txtOCReturnCharges, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParameter.add(txtICReturnCharges, gridBagConstraints);

        lblClearingType.setText("Clearing Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParameter.add(lblClearingType, gridBagConstraints);

        panClearingHD.setLayout(new java.awt.GridBagLayout());

        txtClearingHD.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClearingHD.add(txtClearingHD, gridBagConstraints);

        btnClearingHD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnClearingHD.setPreferredSize(new java.awt.Dimension(21, 21));
        btnClearingHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearingHDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panClearingHD.add(btnClearingHD, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        panParameter.add(panClearingHD, gridBagConstraints);

        lblClearingSuspenseHD.setText("Clearing Suspense Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParameter.add(lblClearingSuspenseHD, gridBagConstraints);

        lblOCReturnChargesHD.setText("Outward Clearing Return Charges Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParameter.add(lblOCReturnChargesHD, gridBagConstraints);

        lblICReturnChargesHD.setText("Inward Clearing Return Charges Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParameter.add(lblICReturnChargesHD, gridBagConstraints);

        panClearingSuspenseHD.setLayout(new java.awt.GridBagLayout());

        txtClearingSuspenseHD.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClearingSuspenseHD.add(txtClearingSuspenseHD, gridBagConstraints);

        btnClearingSuspenseHD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnClearingSuspenseHD.setPreferredSize(new java.awt.Dimension(21, 21));
        btnClearingSuspenseHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearingSuspenseHDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panClearingSuspenseHD.add(btnClearingSuspenseHD, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        panParameter.add(panClearingSuspenseHD, gridBagConstraints);

        panOCReturnChargesHD.setLayout(new java.awt.GridBagLayout());

        txtOCReturnChargesHD.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOCReturnChargesHD.add(txtOCReturnChargesHD, gridBagConstraints);

        btnOCReturnChargesHD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnOCReturnChargesHD.setPreferredSize(new java.awt.Dimension(21, 21));
        btnOCReturnChargesHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOCReturnChargesHDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panOCReturnChargesHD.add(btnOCReturnChargesHD, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        panParameter.add(panOCReturnChargesHD, gridBagConstraints);

        panICReturnChargesHD.setLayout(new java.awt.GridBagLayout());

        txtICReturnChargesHD.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panICReturnChargesHD.add(txtICReturnChargesHD, gridBagConstraints);

        btnICReturnChargesHD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnICReturnChargesHD.setPreferredSize(new java.awt.Dimension(21, 21));
        btnICReturnChargesHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnICReturnChargesHDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panICReturnChargesHD.add(btnICReturnChargesHD, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        panParameter.add(panICReturnChargesHD, gridBagConstraints);

        txtClearingType.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtClearingTypeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParameter.add(txtClearingType, gridBagConstraints);

        lblShortClaimAcHead.setText("Short Claim Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParameter.add(lblShortClaimAcHead, gridBagConstraints);

        panShortClaim.setLayout(new java.awt.GridBagLayout());

        txtShortClaimAcHead.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShortClaim.add(txtShortClaimAcHead, gridBagConstraints);

        btnShortClaimAcHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnShortClaimAcHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnShortClaimAcHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShortClaimAcHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panShortClaim.add(btnShortClaimAcHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panParameter.add(panShortClaim, gridBagConstraints);

        lblExcessClaimAcHead.setText("Excess Claim Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 30;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParameter.add(lblExcessClaimAcHead, gridBagConstraints);

        cPanel2.setLayout(new java.awt.GridBagLayout());

        txtExcessClaimAcHead.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        cPanel2.add(txtExcessClaimAcHead, gridBagConstraints);

        btnExcessClaimAcHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnExcessClaimAcHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnExcessClaimAcHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcessClaimAcHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        cPanel2.add(btnExcessClaimAcHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 30;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panParameter.add(cPanel2, gridBagConstraints);

        lblCompleteDayEWCOPosting.setText("Complete Day End Without CO Posting");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 31;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParameter.add(lblCompleteDayEWCOPosting, gridBagConstraints);

        panCompleteDayEWCOPosting.setLayout(new java.awt.GridBagLayout());

        rdoCompleteDay.add(rdoCompleteDay_Yes);
        rdoCompleteDay_Yes.setText("Yes");
        panCompleteDayEWCOPosting.add(rdoCompleteDay_Yes, new java.awt.GridBagConstraints());

        rdoCompleteDay.add(rdoCompleteDay_No);
        rdoCompleteDay_No.setText("No");
        panCompleteDayEWCOPosting.add(rdoCompleteDay_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 31;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panParameter.add(panCompleteDayEWCOPosting, gridBagConstraints);

        lblOCInstrumentCharges.setText("Outward Clearing Isntrument Collection Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 39;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParameter.add(lblOCInstrumentCharges, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 39;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParameter.add(txtOCInstrumentCharges, gridBagConstraints);

        lblOCInstrumentChargesHD.setText("Outward Clearing Instrument Collection Charges Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 38;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParameter.add(lblOCInstrumentChargesHD, gridBagConstraints);

        panICReturnChargesHD1.setLayout(new java.awt.GridBagLayout());

        txtOCInstrumentChargesHD.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panICReturnChargesHD1.add(txtOCInstrumentChargesHD, gridBagConstraints);

        btnOCInstrumentChargesHD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnOCInstrumentChargesHD.setPreferredSize(new java.awt.Dimension(21, 21));
        btnOCInstrumentChargesHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOCInstrumentChargesHDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panICReturnChargesHD1.add(btnOCInstrumentChargesHD, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 38;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        panParameter.add(panICReturnChargesHD1, gridBagConstraints);

        chkInstrumentCharges.setText("Collect Outward Clearing Instrument Charges");
        chkInstrumentCharges.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkInstrumentCharges.setMinimumSize(new java.awt.Dimension(270, 21));
        chkInstrumentCharges.setPreferredSize(new java.awt.Dimension(290, 21));
        chkInstrumentCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkInstrumentChargesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 37;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panParameter.add(chkInstrumentCharges, gridBagConstraints);

        getContentPane().add(panParameter, java.awt.BorderLayout.CENTER);

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrParameter.add(btnView);

        lbSpace3.setText("     ");
        tbrParameter.add(lbSpace3);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrParameter.add(btnNew);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrParameter.add(lblSpace26);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrParameter.add(btnEdit);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrParameter.add(lblSpace27);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrParameter.add(btnDelete);

        lbSpace2.setText("     ");
        tbrParameter.add(lbSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrParameter.add(btnSave);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrParameter.add(lblSpace28);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrParameter.add(btnCancel);

        lblSpace3.setText("     ");
        tbrParameter.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrParameter.add(btnAuthorize);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrParameter.add(lblSpace29);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrParameter.add(btnException);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrParameter.add(lblSpace30);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrParameter.add(btnReject);

        lblSpace5.setText("     ");
        tbrParameter.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrParameter.add(btnPrint);

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrParameter.add(lblSpace31);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrParameter.add(btnClose);

        getContentPane().add(tbrParameter, java.awt.BorderLayout.NORTH);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace1.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace1, gridBagConstraints);

        lblStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblStatus, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        mnuProcess.setText("Process");

        mitNew.setText("New");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptView);

        mitSave.setText("Save");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrParameter.add(mnuProcess);

        setJMenuBar(mbrParameter);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        popUp(VIEW);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed
    
    private void btnExcessClaimAcHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcessClaimAcHeadActionPerformed
        // TODO add your handling code here:
        popUp(ECAH);
    }//GEN-LAST:event_btnExcessClaimAcHeadActionPerformed
    
    private void btnShortClaimAcHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShortClaimAcHeadActionPerformed
        // TODO add your handling code here:
        popUp(SCAH);
    }//GEN-LAST:event_btnShortClaimAcHeadActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    /** Does the Authorization of the selected rows in a Popup Window AuthorizeStatusUI */
    public void authorizeStatus(String authorizeStatus) {
        if (viewType != AUTHORIZE){
            setModified(true);
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getBankClearingParamAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeBankClearingParameter");
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        } else if (viewType == AUTHORIZE){
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("USER_ID", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("CLEARING_TYPE", CommonUtil.convertObjToStr(txtClearingType.getText()));
            singleAuthorizeMap.put("AUTHORIZE_DT", ClientUtil.getCurrentDate());
            System.out.println("AuthorizeMap "+ singleAuthorizeMap);
            ClientUtil.execute("authorizeBankClearingParameter", singleAuthorizeMap);
            viewType = -1;
            btnCancelActionPerformed(null);
        }
    }
    private void txtClearingTypeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtClearingTypeFocusLost
        // TODO add your handling code here:
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW ){
            observable.checkDuplication(CommonUtil.convertObjToStr(txtClearingType.getText()));
        }
    }//GEN-LAST:event_txtClearingTypeFocusLost
    /* To disable the ValueofHighValueCheque if High Value is not Applicable */
    
    private void btnICReturnChargesHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnICReturnChargesHDActionPerformed
        // Add your handling code here:
        popUp(INCR_HD);
    }//GEN-LAST:event_btnICReturnChargesHDActionPerformed
    
    private void btnOCReturnChargesHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOCReturnChargesHDActionPerformed
        // Add your handling code here:
        popUp(OCRC_HD);
    }//GEN-LAST:event_btnOCReturnChargesHDActionPerformed
    
    private void btnClearingSuspenseHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearingSuspenseHDActionPerformed
        // Add your handling code here:
        popUp(CLEA_SUSP_HD);
    }//GEN-LAST:event_btnClearingSuspenseHDActionPerformed
    
    private void btnClearingHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearingHDActionPerformed
        // Add your handling code here:
        popUp(CLEA_HD);
    }//GEN-LAST:event_btnClearingHDActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        observable.resetForm();
        ClientUtil.enableDisable(this,false);
        setButtonEnableDisable();
        setHelpButtonEnableDisable(false);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        observable.setStatus();
        setModified(false);
        lblOCInstrumentChargesHD.setVisible(false);
        txtOCInstrumentCharges.setVisible(false);
        lblOCInstrumentCharges.setVisible(false);
        txtOCInstrumentChargesHD.setVisible(false);
        btnOCInstrumentChargesHD.setVisible(false);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panParameter);
        if(chkInstrumentCharges.isSelected()==true){
            if(txtOCInstrumentCharges.getText().length()==0 ){
                     ClientUtil.displayAlert("Enter Value for O/W Clg Instrument Collection Charges ");
                     return;
            }
             if(txtOCInstrumentChargesHD.getText().length()==0){
                ClientUtil.displayAlert("Enter Value for O/W Clg Instrument Collection Charges Head ");
                return;
            }
        }if (mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
        }
        else{
            savePerformed();
        }
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed
    /* To display an alert message if any of the mandatory fields is not inputed */
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        popUp(DELETE);
        ClientUtil.enableDisable(this,false);
        setHelpButtonEnableDisable(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        popUp(EDIT);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        ClientUtil.enableDisable(this,true);
        setHelpButtonEnableDisable(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        observable.setStatus();
        setModified(true);
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnOCInstrumentChargesHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOCInstrumentChargesHDActionPerformed
        // TODO add your handling code here:
         popUp(OICC_HD);
    }//GEN-LAST:event_btnOCInstrumentChargesHDActionPerformed

    private void chkInstrumentChargesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkInstrumentChargesActionPerformed
        // TODO add your handling code here:
        if (chkInstrumentCharges.isSelected()) {
          lblOCInstrumentChargesHD.setVisible(true);
          txtOCInstrumentCharges.setVisible(true);
          lblOCInstrumentCharges.setVisible(true);
          txtOCInstrumentChargesHD.setVisible(true);
          btnOCInstrumentChargesHD.setVisible(true);
        } else {
          lblOCInstrumentChargesHD.setVisible(false);
          txtOCInstrumentCharges.setVisible(false);
          lblOCInstrumentCharges.setVisible(false); 
          btnOCInstrumentChargesHD.setVisible(false);
          txtOCInstrumentCharges.setText("");
          txtOCInstrumentChargesHD.setText("");
          txtOCInstrumentCharges.setVisible(false);
          txtOCInstrumentChargesHD.setVisible(false);
        }
    }//GEN-LAST:event_chkInstrumentChargesActionPerformed
 private void btnCheck(){
         btnCancel.setEnabled(true);
         btnSave.setEnabled(false);
         btnNew.setEnabled(false);
         btnDelete.setEnabled(false);
         btnAuthorize.setEnabled(false);
         btnReject.setEnabled(false);
         btnException.setEnabled(false);
         btnEdit.setEnabled(false);
     }   
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        removeRadioButtons();
        txtOCReturnCharges.setText(observable.getTxtOCReturnCharges());
        txtICReturnCharges.setText(observable.getTxtICReturnCharges());
        txtOCInstrumentCharges.setText(observable.getTxtOCInstumentCharges());
        lblStatus.setText(observable.getLblStatus());
        txtClearingType.setText(observable.getTxtClearingType());
        txtClearingHD.setText(observable.getTxtClearingHD());
        
        txtClearingSuspenseHD.setText(observable.getTxtClearingSuspenseHD());
        txtOCReturnChargesHD.setText(observable.getTxtOCReturnChargesHD());
        txtICReturnChargesHD.setText(observable.getTxtICReturnChargesHD());
        txtOCInstrumentChargesHD.setText(observable.getTxtOCInstrumentChargesHD());
        if(observable.isChkInstrumentCharges()==true) {
             chkInstrumentCharges.setSelected(true);
             lblOCInstrumentChargesHD.setVisible(true);
             txtOCInstrumentCharges.setVisible(true);
             lblOCInstrumentCharges.setVisible(true);
             txtOCInstrumentChargesHD.setVisible(true);
             btnOCInstrumentChargesHD.setVisible(true);
        }else{
             chkInstrumentCharges.setSelected(false);
        }
        txtShortClaimAcHead.setText(observable.getTxtShortClaimAcHead());
        txtExcessClaimAcHead.setText(observable.getTxtExcessClaimAcHead());
        
        rdoCompleteDay_Yes.setSelected(observable.getRdoCompleteDay_Yes());
        rdoCompleteDay_No.setSelected(observable.getRdoCompleteDay_No());
        
        addRadioButtons();
    }
    
   /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        panClearingHD.setName("panClearingHD");
        panClearingSuspenseHD.setName("panClearingSuspenseHD");
        panICReturnChargesHD.setName("panICReturnChargesHD");
        panOCReturnChargesHD.setName("panOCReturnChargesHD");
        panCompleteDayEWCOPosting.setName("panCompleteDayEWCOPosting");
        panParameter.setName("panParameter");
        btnCancel.setName("btnCancel");
        btnClearingHD.setName("btnClearingHD");
        btnClearingSuspenseHD.setName("btnClearingSuspenseHD");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        
        btnAuthorize.setName("btnAuthorize");
        btnException.setName("btnException");
        btnReject.setName("btnReject");
        
        btnICReturnChargesHD.setName("btnICReturnChargesHD");
        btnNew.setName("btnNew");
        btnOCReturnChargesHD.setName("btnOCReturnChargesHD");
        btnPrint.setName("btnPrint");
        btnSave.setName("btnSave");
        txtClearingType.setName("txtClearingType");
        lbSpace2.setName("lbSpace2");
        lblClearingHD.setName("lblClearingHD");
        lblClearingSuspenseHD.setName("lblClearingSuspenseHD");
        lblClearingType.setName("lblClearingType");
        lblICReturnCharges.setName("lblICReturnCharges");
        lblICReturnChargesHD.setName("lblICReturnChargesHD");
        lblMsg.setName("lblMsg");
        lblOCReturnCharges.setName("lblOCReturnCharges");
        lblOCReturnChargesHD.setName("lblOCReturnChargesHD");
        lblCompleteDayEWCOPosting.setName("lblCompleteDayEWCOPosting");
        lblSpace1.setName("lblSpace1");
        lblSpace3.setName("lblSpace3");
        lblStatus.setName("lblStatus");
        mbrParameter.setName("mbrParameter");
        panStatus.setName("panStatus");
        txtClearingHD.setName("txtClearingHD");
        txtClearingSuspenseHD.setName("txtClearingSuspenseHD");
        txtICReturnCharges.setName("txtICReturnCharges");
         txtOCInstrumentCharges.setName("txtOCInstrumentCharges");
        txtICReturnChargesHD.setName("txtICReturnChargesHD");
        txtOCInstrumentChargesHD.setName("txtOCInstrumentChargesHD");
        txtOCReturnCharges.setName("txtOCReturnCharges");
        txtOCReturnChargesHD.setName("txtOCReturnChargesHD");
        txtShortClaimAcHead.setName("txtShortClaimAcHead");
        txtExcessClaimAcHead.setName("txtExcessClaimAcHead");
        rdoCompleteDay_No.setName("rdoCompleteDay_No");
        rdoCompleteDay_Yes.setName("rdoCompleteDay_Yes");
    }
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtOCReturnCharges(txtOCReturnCharges.getText());
        observable.setTxtICReturnCharges(txtICReturnCharges.getText());
        observable.setTxtOCInstumentCharges(txtOCInstrumentCharges.getText());
        observable.setTxtClearingType(txtClearingType.getText());
        observable.setTxtClearingHD(txtClearingHD.getText());
        observable.setTxtClearingSuspenseHD(txtClearingSuspenseHD.getText());
        observable.setTxtOCReturnChargesHD(txtOCReturnChargesHD.getText());
        observable.setTxtICReturnChargesHD(txtICReturnChargesHD.getText());
        observable.setTxtOCInstrumentChargesHD(txtOCInstrumentChargesHD.getText());
        observable.setChkInstrumentCharges(chkInstrumentCharges.isSelected());
        observable.setTxtExcessClaimAcHead(txtExcessClaimAcHead.getText());
        observable.setTxtShortClaimAcHead(txtShortClaimAcHead.getText());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
    }
    
        /* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        BankClearingParameterMRB objMandatoryRB = new BankClearingParameterMRB();
        txtOCReturnCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("txtOCReturnCharges"));
        txtOCInstrumentCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("txtOCInstrumentCharges"));
//        txtOCInstrumentChargesHD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtOCInstrumentChargesHD"));
        txtICReturnCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("txtICReturnCharges"));
        txtClearingType.setHelpMessage(lblMsg, objMandatoryRB.getString("txtClearingType"));
        txtClearingHD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtClearingHD"));
        txtClearingSuspenseHD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtClearingSuspenseHD"));
        txtOCReturnChargesHD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtOCReturnChargesHD"));
        txtICReturnChargesHD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtICReturnChargesHD"));
        txtShortClaimAcHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtShortClaimAcHead"));
        txtExcessClaimAcHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtExcessClaimAcHead"));
        rdoCompleteDay_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoCompleteDay_Yes"));
    }
    
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblClearingHD.setText(resourceBundle.getString("lblClearingHD"));
        btnICReturnChargesHD.setText(resourceBundle.getString("btnICReturnChargesHD"));
        lblOCReturnChargesHD.setText(resourceBundle.getString("lblOCReturnChargesHD"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblClearingType.setText(resourceBundle.getString("lblClearingType"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblClearingSuspenseHD.setText(resourceBundle.getString("lblClearingSuspenseHD"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lbSpace2.setText(resourceBundle.getString("lbSpace2"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        btnClearingSuspenseHD.setText(resourceBundle.getString("btnClearingSuspenseHD"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblOCReturnCharges.setText(resourceBundle.getString("lblOCReturnCharges"));
        lblICReturnCharges.setText(resourceBundle.getString("lblICReturnCharges"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnClearingHD.setText(resourceBundle.getString("btnClearingHD"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnOCReturnChargesHD.setText(resourceBundle.getString("btnOCReturnChargesHD"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblICReturnChargesHD.setText(resourceBundle.getString("lblICReturnChargesHD"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnException.setText(resourceBundle.getString("btnException"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        lblShortClaimAcHead.setText(resourceBundle.getString("lblShortClaimAcHead"));
        lblExcessClaimAcHead.setText(resourceBundle.getString("lblExcessClaimAcHead"));
        rdoCompleteDay_Yes.setText(resourceBundle.getString("rdoCompleteDay_Yes"));
        lblCompleteDayEWCOPosting.setText(resourceBundle.getString("lblCompleteDayEWCOPosting"));
        rdoCompleteDay_No.setText(resourceBundle.getString("rdoCompleteDay_No"));
    }
    
/* Auto Generated Method - setMandatoryHashMap()
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtOCReturnCharges", new Boolean(true));
        mandatoryMap.put("txtICReturnCharges", new Boolean(true));
        mandatoryMap.put("txtClearingType", new Boolean(true));
        mandatoryMap.put("txtClearingHD", new Boolean(true));
        mandatoryMap.put("txtClearingSuspenseHD", new Boolean(true));
        mandatoryMap.put("txtOCReturnChargesHD", new Boolean(true));
        mandatoryMap.put("txtICReturnChargesHD", new Boolean(true));
//        mandatoryMap.put("txtOCInstrumentChargesHD", new Boolean(true));
        mandatoryMap.put("txtShortClaimAcHead", new Boolean(true));
        mandatoryMap.put("txtExcessClaimAcHead", new Boolean(true));
        mandatoryMap.put("rdoCompleteDay_Yes", new Boolean(true));
        mandatoryMap.put("txtOCInstrumentCharges", new Boolean(true));
//        mandatoryMap.put("txtOCInstrumentChargesHD", new Boolean(true));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    /** Shows a PopUp ViewAll which is filled up with row according to the New,Edit,Delete, Help button clicked */
    private void popUp(int field) {
        if( field == CLEA_HD || field == CLEA_SUSP_HD || field == OCRC_HD || field == INCR_HD || field == SCAH || field == ECAH || field == OICC_HD) {
            lblStatus.setText(ClientConstants.ACTION_STATUS[observable.getActionType()]);
        }else {
            lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
        }
        viewType = field;
        final HashMap viewMap = new HashMap();
        if(field==EDIT || field==DELETE || field == VIEW){//Edit=0 and Delete=1
            viewMap.put(CommonConstants.MAP_NAME, "getSelectClearingParameter");
        }else {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectAcctHeadTOList");
        }
        new ViewAll(this, viewMap).show();
    }
    
    /** Called by the Popup window created thru popUp method
     * @param param
     */
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        final String accountHead=(String)hash.get("AC_HD_ID");
        if (viewType != -1) {
            /* In the Edit or Delete Mode */
            if (viewType==EDIT || viewType==DELETE || viewType==AUTHORIZE || viewType == VIEW) {
                hash.put(CommonConstants.MAP_WHERE, hash.get("CLEARING_TYPE"));
                hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                observable.populateData(hash);
                observable.setStatus();
                if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                    ClientUtil.enableDisable(this, true);
                    setHelpButtonEnableDisable(true);
                    setTxtClearingTypeEnableDisable(false);
                } else if(observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW){
                    ClientUtil.enableDisable(this, false);
                }
                if(viewType == AUTHORIZE){
                    ClientUtil.enableDisable(this,false);
                }
                setButtonEnableDisable();
            }else if (viewType==CLEA_HD) {
                txtClearingHD.setText(accountHead);
            }else if (viewType==CLEA_SUSP_HD) {
                txtClearingSuspenseHD.setText(accountHead);
            }else if (viewType==OCRC_HD) {
                txtOCReturnChargesHD.setText(accountHead);
            }else if (viewType==INCR_HD) {
                txtICReturnChargesHD.setText(accountHead);
            }else if (viewType==SCAH) {
                txtShortClaimAcHead.setText(accountHead);
            }else if (viewType==ECAH) {
                txtExcessClaimAcHead.setText(accountHead);
            }else if (viewType==OICC_HD) {
                txtOCInstrumentChargesHD.setText(accountHead);
            }
        }
        setModified(true);
    }
    
    // action performed when save button is pressed
    private void savePerformed() {
        int DUPLICATION = 2;
        final int NO = 1,NOCHANGE=2;
        updateOBFields();
        setModified(false);
        /* Checking for duplication of clearingType only at the time of Insertion */
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW ){
            DUPLICATION = observable.checkDuplication(CommonUtil.convertObjToStr(txtClearingType.getText()));
        }
        /* If there is no duplication perform */
        if (DUPLICATION == NO ||DUPLICATION == NOCHANGE){
            observable.doAction();
            observable.setResultStatus();
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
                ClientUtil.enableDisable(this, false);
                setButtonEnableDisable();
                setHelpButtonEnableDisable(false);
                observable.resetForm();
            }
            
        }
        
    }
    
    /* To Enable or Disable the button payeeAcc head */
    private void setHelpButtonEnableDisable(boolean flag){
        btnClearingHD.setEnabled(flag);
        btnClearingSuspenseHD.setEnabled(flag);
        btnICReturnChargesHD.setEnabled(flag);
        btnOCReturnChargesHD.setEnabled(flag);
        btnShortClaimAcHead.setEnabled(flag);
        btnExcessClaimAcHead.setEnabled(flag);
        
    }
    /* To set enable disable the CboClearingType */
    private void setTxtClearingTypeEnableDisable(boolean flag) {
        txtClearingType.setEnabled(flag);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClearingHD;
    private com.see.truetransact.uicomponent.CButton btnClearingSuspenseHD;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnExcessClaimAcHead;
    private com.see.truetransact.uicomponent.CButton btnICReturnChargesHD;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnOCInstrumentChargesHD;
    private com.see.truetransact.uicomponent.CButton btnOCReturnChargesHD;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnShortClaimAcHead;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CPanel cPanel2;
    private com.see.truetransact.uicomponent.CCheckBox chkInstrumentCharges;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lbSpace3;
    private com.see.truetransact.uicomponent.CLabel lblClearingHD;
    private com.see.truetransact.uicomponent.CLabel lblClearingSuspenseHD;
    private com.see.truetransact.uicomponent.CLabel lblClearingType;
    private com.see.truetransact.uicomponent.CLabel lblCompleteDayEWCOPosting;
    private com.see.truetransact.uicomponent.CLabel lblExcessClaimAcHead;
    private com.see.truetransact.uicomponent.CLabel lblICReturnCharges;
    private com.see.truetransact.uicomponent.CLabel lblICReturnChargesHD;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblOCInstrumentCharges;
    private com.see.truetransact.uicomponent.CLabel lblOCInstrumentChargesHD;
    private com.see.truetransact.uicomponent.CLabel lblOCReturnCharges;
    private com.see.truetransact.uicomponent.CLabel lblOCReturnChargesHD;
    private com.see.truetransact.uicomponent.CLabel lblShortClaimAcHead;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace26;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace28;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace31;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CMenuBar mbrParameter;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panClearingHD;
    private com.see.truetransact.uicomponent.CPanel panClearingSuspenseHD;
    private com.see.truetransact.uicomponent.CPanel panCompleteDayEWCOPosting;
    private com.see.truetransact.uicomponent.CPanel panICReturnChargesHD;
    private com.see.truetransact.uicomponent.CPanel panICReturnChargesHD1;
    private com.see.truetransact.uicomponent.CPanel panOCReturnChargesHD;
    private com.see.truetransact.uicomponent.CPanel panParameter;
    private com.see.truetransact.uicomponent.CPanel panShortClaim;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCompleteDay;
    private com.see.truetransact.uicomponent.CRadioButton rdoCompleteDay_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoCompleteDay_Yes;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CToolBar tbrParameter;
    private com.see.truetransact.uicomponent.CTextField txtClearingHD;
    private com.see.truetransact.uicomponent.CTextField txtClearingSuspenseHD;
    private com.see.truetransact.uicomponent.CTextField txtClearingType;
    private com.see.truetransact.uicomponent.CTextField txtExcessClaimAcHead;
    private com.see.truetransact.uicomponent.CTextField txtICReturnCharges;
    private com.see.truetransact.uicomponent.CTextField txtICReturnChargesHD;
    private com.see.truetransact.uicomponent.CTextField txtOCInstrumentCharges;
    private com.see.truetransact.uicomponent.CTextField txtOCInstrumentChargesHD;
    private com.see.truetransact.uicomponent.CTextField txtOCReturnCharges;
    private com.see.truetransact.uicomponent.CTextField txtOCReturnChargesHD;
    private com.see.truetransact.uicomponent.CTextField txtShortClaimAcHead;
    // End of variables declaration//GEN-END:variables
    
}
