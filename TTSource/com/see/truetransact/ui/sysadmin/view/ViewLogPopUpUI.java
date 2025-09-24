/* Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ViewLogPopUpUI.java
 *
 * Created on April 28, 2004, 5:49 PM
 */

package com.see.truetransact.ui.sysadmin.view;

import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.ui.sysadmin.view.ViewLogRB;
import com.see.truetransact.commonutil.CommonUtil;

import java.util.HashMap ;
import java.util.ArrayList ;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 *
 * @author  Lohith R.
 */
public class ViewLogPopUpUI extends com.see.truetransact.uicomponent.CDialog {
    
    HashMap paramMap = new HashMap();
    CInternalFrame parent = null;
    
    /** Creates new form ViewLogPopUpUI */
    public ViewLogPopUpUI(CInternalFrame parent,HashMap paramMap) {
        this.parent = parent;
        this.paramMap = paramMap;
        setupInit();
        setupScreen();
    }
    
    private void setupInit() {
        initComponents();
        setFieldNames();
        internationalize();
        populateData(paramMap);
    }
    
    
    /** Auto Generated Method - setFieldNames()
     * This method assigns name for all the components.
     * Other functions are working based on this name. */
    private void setFieldNames() {
        btnOk.setName("btnOk");
        lblUserID.setName("lblUserID");
        lblUsrID.setName("lblUsrID");
        lblAcitivity.setName("lblAcitivity");
        lblAct.setName("lblAct");
        lblBranchID.setName("lblBranchID");
        lblBrnID.setName("lblBrnID");
        lblIPAddress.setName("lblIPAddress");
        lblIPAddr.setName("lblIPAddr");
        lblModule.setName("lblModule");
        lblMod.setName("lblMod");
        lblScreen.setName("lblScreen");
        lblScr.setName("lblScr");
        lblDate.setName("lblDate");
        lblDt.setName("lblDt");
        lblData.setName("lblData");
        txaData.setName("txaData");
        lblRemark.setName("lblData");
        txaRemark.setName("txaRemark");
        lblPrimaryKey.setName("lblPrimaryKey");
        lblPrimKey.setName("lblPrimKey");
        panMain.setName("panMain");
        panButton.setName("panMain");
        panFields.setName("panMain");
        panLeft.setName("panMain");
        panRight.setName("panMain");
    }
    
    
    /** Auto Generated Method - internationalize()
     * This method used to assign display texts from
     * the Resource Bundle File. */
    private void internationalize() {
        ViewLogRB resourceBundle = new ViewLogRB();
        btnOk.setText(resourceBundle.getString("btnOk"));
        lblUserID.setText(resourceBundle.getString("lblUserid"));
        lblUsrID.setText(resourceBundle.getString("lblUsrID"));
        lblAcitivity.setText(resourceBundle.getString("lblActivity"));
        lblAct.setText(resourceBundle.getString("lblAct"));
        lblBranchID.setText(resourceBundle.getString("lblBranchID"));
        lblBrnID.setText(resourceBundle.getString("lblBrnID"));
        lblIPAddress.setText(resourceBundle.getString("lblIPAddress"));
        lblIPAddr.setText(resourceBundle.getString("lblIPAddr"));
        lblModule.setText(resourceBundle.getString("lblModule"));
        lblMod.setText(resourceBundle.getString("lblMod"));
        lblScreen.setText(resourceBundle.getString("lblScreen"));
        lblScr.setText(resourceBundle.getString("lblScr"));
        lblDate.setText(resourceBundle.getString("lblDate"));
        lblDt.setText(resourceBundle.getString("lblDt"));
        lblData.setText(resourceBundle.getString("lblData"));
        lblRemark.setText(resourceBundle.getString("lblRemarks"));
        lblPrimaryKey.setText(resourceBundle.getString("lblPrimaryKey"));
        lblPrimKey.setText(resourceBundle.getString("lblPrimKey"));
    }
    
    private void setupScreen() {
        setModal(true);
        /* Calculate the screen size */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        /* Center frame on the screen */
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }
    
    
    /** Populates the Hash Map values to the appropriate lables
     * @param hash
     */    
    public void populateData(HashMap hash){
        lblAct.setText(CommonUtil.convertObjToStr(hash.get("STATUS")));
        lblBrnID.setText(CommonUtil.convertObjToStr(hash.get("BRANCH_ID")));
        lblDt.setText(CommonUtil.convertObjToStr(hash.get("TIME_STAMP")));
        lblIPAddr.setText(CommonUtil.convertObjToStr(hash.get("IP_ADDR")));
        lblMod.setText(CommonUtil.convertObjToStr(hash.get("MODULE")));
        lblPrimKey.setText(CommonUtil.convertObjToStr(hash.get("PRIMARY_KEY")));
        lblScr.setText(CommonUtil.convertObjToStr(hash.get("SCREEN")));
        lblUsrID.setText(CommonUtil.convertObjToStr(hash.get("USER_ID")));
        txaData.setText(CommonUtil.convertObjToStr(hash.get("DATA")));
        txaRemark.setText(CommonUtil.convertObjToStr(hash.get("REMARKS")));
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        panMain = new com.see.truetransact.uicomponent.CPanel();
        panButton = new com.see.truetransact.uicomponent.CPanel();
        btnOk = new com.see.truetransact.uicomponent.CButton();
        sprRemark = new com.see.truetransact.uicomponent.CScrollPane();
        txaRemark = new com.see.truetransact.uicomponent.CTextArea();
        lblRemark = new com.see.truetransact.uicomponent.CLabel();
        lblData = new com.see.truetransact.uicomponent.CLabel();
        sprData = new com.see.truetransact.uicomponent.CScrollPane();
        txaData = new com.see.truetransact.uicomponent.CTextArea();
        panFields = new com.see.truetransact.uicomponent.CPanel();
        panRight = new com.see.truetransact.uicomponent.CPanel();
        lblPrimKey = new com.see.truetransact.uicomponent.CLabel();
        lblAct = new com.see.truetransact.uicomponent.CLabel();
        lblUsrID = new com.see.truetransact.uicomponent.CLabel();
        lblBrnID = new com.see.truetransact.uicomponent.CLabel();
        lblBranchID = new com.see.truetransact.uicomponent.CLabel();
        lblUserID = new com.see.truetransact.uicomponent.CLabel();
        lblAcitivity = new com.see.truetransact.uicomponent.CLabel();
        lblPrimaryKey = new com.see.truetransact.uicomponent.CLabel();
        panLeft = new com.see.truetransact.uicomponent.CPanel();
        lblDate = new com.see.truetransact.uicomponent.CLabel();
        lblModule = new com.see.truetransact.uicomponent.CLabel();
        lblScreen = new com.see.truetransact.uicomponent.CLabel();
        lblIPAddress = new com.see.truetransact.uicomponent.CLabel();
        lblDt = new com.see.truetransact.uicomponent.CLabel();
        lblMod = new com.see.truetransact.uicomponent.CLabel();
        lblScr = new com.see.truetransact.uicomponent.CLabel();
        lblIPAddr = new com.see.truetransact.uicomponent.CLabel();
        sprFields = new com.see.truetransact.uicomponent.CSeparator();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        panMain.setLayout(new java.awt.GridBagLayout());

        panMain.setMinimumSize(new java.awt.Dimension(450, 400));
        panMain.setPreferredSize(new java.awt.Dimension(450, 400));
        panButton.setLayout(new java.awt.GridBagLayout());

        btnOk.setText("OK");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(btnOk, gridBagConstraints);

        sprRemark.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sprRemark.setMinimumSize(new java.awt.Dimension(150, 40));
        sprRemark.setPreferredSize(new java.awt.Dimension(150, 40));
        sprRemark.setAutoscrolls(true);
        txaRemark.setEditable(false);
        txaRemark.setLineWrap(true);
        txaRemark.setWrapStyleWord(true);
        txaRemark.setMinimumSize(new java.awt.Dimension(100, 60));
        txaRemark.setPreferredSize(new java.awt.Dimension(80, 400));
        sprRemark.setViewportView(txaRemark);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        panButton.add(sprRemark, gridBagConstraints);

        lblRemark.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        panButton.add(lblRemark, gridBagConstraints);

        lblData.setText("Data");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        panButton.add(lblData, gridBagConstraints);

        sprData.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sprData.setMinimumSize(new java.awt.Dimension(150, 40));
        sprData.setPreferredSize(new java.awt.Dimension(150, 40));
        sprData.setAutoscrolls(true);
        txaData.setEditable(false);
        txaData.setLineWrap(true);
        txaData.setWrapStyleWord(true);
        txaData.setMinimumSize(new java.awt.Dimension(100, 60));
        txaData.setPreferredSize(new java.awt.Dimension(80, 600));
        sprData.setViewportView(txaData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panButton.add(sprData, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panMain.add(panButton, gridBagConstraints);

        panFields.setLayout(new java.awt.GridBagLayout());

        panFields.setBorder(new javax.swing.border.TitledBorder("Log Details"));
        panRight.setLayout(new java.awt.GridBagLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRight.add(lblPrimKey, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRight.add(lblAct, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRight.add(lblUsrID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRight.add(lblBrnID, gridBagConstraints);

        lblBranchID.setText("Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRight.add(lblBranchID, gridBagConstraints);

        lblUserID.setText("User ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRight.add(lblUserID, gridBagConstraints);

        lblAcitivity.setText("Acitivity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRight.add(lblAcitivity, gridBagConstraints);

        lblPrimaryKey.setText("Primary Key");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRight.add(lblPrimaryKey, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.weightx = 1.0;
        panFields.add(panRight, gridBagConstraints);

        panLeft.setLayout(new java.awt.GridBagLayout());

        lblDate.setText("Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeft.add(lblDate, gridBagConstraints);

        lblModule.setText("Module");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeft.add(lblModule, gridBagConstraints);

        lblScreen.setText("Screen");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeft.add(lblScreen, gridBagConstraints);

        lblIPAddress.setText("IP Address");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeft.add(lblIPAddress, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeft.add(lblDt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeft.add(lblMod, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeft.add(lblScr, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeft.add(lblIPAddr, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.weightx = 1.0;
        panFields.add(panLeft, gridBagConstraints);

        sprFields.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFields.add(sprFields, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.weightx = 1.0;
        panMain.add(panFields, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(panMain, gridBagConstraints);

        pack();
    }//GEN-END:initComponents
    
    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        // Add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnOkActionPerformed
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog
    
    /**
     * @param args the command line arguments
     *
     * public static void main(String args[]) {
     * //        new ViewLogPopUpUI(new javax.swing.JFrame(), true).show();
     * }
     */
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnOk;
    private com.see.truetransact.uicomponent.CLabel lblAcitivity;
    private com.see.truetransact.uicomponent.CLabel lblAct;
    private com.see.truetransact.uicomponent.CLabel lblBranchID;
    private com.see.truetransact.uicomponent.CLabel lblBrnID;
    private com.see.truetransact.uicomponent.CLabel lblData;
    private com.see.truetransact.uicomponent.CLabel lblDate;
    private com.see.truetransact.uicomponent.CLabel lblDt;
    private com.see.truetransact.uicomponent.CLabel lblIPAddr;
    private com.see.truetransact.uicomponent.CLabel lblIPAddress;
    private com.see.truetransact.uicomponent.CLabel lblMod;
    private com.see.truetransact.uicomponent.CLabel lblModule;
    private com.see.truetransact.uicomponent.CLabel lblPrimKey;
    private com.see.truetransact.uicomponent.CLabel lblPrimaryKey;
    private com.see.truetransact.uicomponent.CLabel lblRemark;
    private com.see.truetransact.uicomponent.CLabel lblScr;
    private com.see.truetransact.uicomponent.CLabel lblScreen;
    private com.see.truetransact.uicomponent.CLabel lblUserID;
    private com.see.truetransact.uicomponent.CLabel lblUsrID;
    private com.see.truetransact.uicomponent.CPanel panButton;
    private com.see.truetransact.uicomponent.CPanel panFields;
    private com.see.truetransact.uicomponent.CPanel panLeft;
    private com.see.truetransact.uicomponent.CPanel panMain;
    private com.see.truetransact.uicomponent.CPanel panRight;
    private com.see.truetransact.uicomponent.CScrollPane sprData;
    private com.see.truetransact.uicomponent.CSeparator sprFields;
    private com.see.truetransact.uicomponent.CScrollPane sprRemark;
    private com.see.truetransact.uicomponent.CTextArea txaData;
    private com.see.truetransact.uicomponent.CTextArea txaRemark;
    // End of variables declaration//GEN-END:variables
    
}
