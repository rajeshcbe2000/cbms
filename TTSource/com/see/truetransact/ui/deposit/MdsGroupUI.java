/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * MdsGroupUI.java
 */

package com.see.truetransact.ui.deposit;

import com.see.truetransact.ui.termloan.*;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.viewall.ViewAll;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Date;
import java.util.HashMap;

public class MdsGroupUI extends javax.swing.JDialog{

    final int TRANS_PROD = 1, MDS_GROUP=2;
    public String branchID;
    Date currDt=null;
    
    public MdsGroupUI(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        currDt = ClientUtil.getCurrentDate();
        initComponents();
    }

    public MdsGroupUI(TermDepositUI termDepositUi) {
        initForm();
    }
    
    public MdsGroupUI(HashMap map) {
        initForm();
        if(map.containsKey("MDS_GROUP")){
            txtMdsGroup.setText(CommonUtil.convertObjToStr(map.get("MDS_GROUP")));
        }
        if(map.containsKey("MDS_REMARKS")){
            txaMdsRemarks.setText(CommonUtil.convertObjToStr(map.get("MDS_REMARKS")));
        }
    }

    public MdsGroupUI(String screen) {
        currDt = ClientUtil.getCurrentDate();
    }

    private void initForm() {
        initComponents();
        setFieldNames();
        branchID = TrueTransactMain.BRANCH_ID;
        currDt = ClientUtil.getCurrentDate();
    }

    private void setFieldNames() {
        lblMdsGroup.setName("lblMdsGroup");
        txtMdsGroup.setName("txtMdsGroup");
        btnMdsGroup.setName("btnMdsGroup");
    }
    
    public void resetform() {
        txtMdsGroup.setText("");
        TermDepositOB.setMdsGroup("");
        TermDepositOB.setMdsRemarks("");
    }

    public void show() {
        setTitle("Search MDS Group " + "[" + branchID + "]");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        setModal(true);
        super.show();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblMdsGroup = new com.see.truetransact.uicomponent.CLabel();
        txtMdsGroup = new com.see.truetransact.uicomponent.CTextField();
        btnMdsGroup = new com.see.truetransact.uicomponent.CButton();
        btnSubmit = new com.see.truetransact.uicomponent.CButton();
        btnRefresh = new com.see.truetransact.uicomponent.CButton();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txaMdsRemarks = new com.see.truetransact.uicomponent.CTextArea();
        btnCancel = new com.see.truetransact.uicomponent.CButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(150, 150));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        lblMdsGroup.setText("MDS Group");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(88, 65, 0, 0);
        getContentPane().add(lblMdsGroup, gridBagConstraints);

        txtMdsGroup.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMdsGroup.setName("txtAccountNo"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 67;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(85, 8, 0, 0);
        getContentPane().add(txtMdsGroup, gridBagConstraints);

        btnMdsGroup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnMdsGroup.setMaximumSize(new java.awt.Dimension(25, 25));
        btnMdsGroup.setMinimumSize(new java.awt.Dimension(25, 25));
        btnMdsGroup.setName("btnMdsGroup"); // NOI18N
        btnMdsGroup.setPreferredSize(new java.awt.Dimension(25, 25));
        btnMdsGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMdsGroupActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(85, 2, 0, 65);
        getContentPane().add(btnMdsGroup, gridBagConstraints);

        btnSubmit.setText("Submit");
        btnSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 0, 0);
        getContentPane().add(btnSubmit, gridBagConstraints);

        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnRefresh.setMaximumSize(new java.awt.Dimension(25, 25));
        btnRefresh.setMinimumSize(new java.awt.Dimension(25, 25));
        btnRefresh.setPreferredSize(new java.awt.Dimension(25, 25));
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 65);
        getContentPane().add(btnRefresh, gridBagConstraints);

        cLabel1.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(29, 77, 0, 0);
        getContentPane().add(cLabel1, gridBagConstraints);

        txaMdsRemarks.setColumns(20);
        txaMdsRemarks.setRows(5);
        jScrollPane1.setViewportView(txaMdsRemarks);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 142;
        gridBagConstraints.ipady = 52;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        getContentPane().add(jScrollPane1, gridBagConstraints);

        btnCancel.setText("Canel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 82, 0);
        getContentPane().add(btnCancel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMdsGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMdsGroupActionPerformed
        // TODO add your handling code here:
        resetform();
        popUp(MDS_GROUP);
    }//GEN-LAST:event_btnMdsGroupActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
        resetform();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
        // TODO add your handling code here:
        if(txtMdsGroup.getText() == null || txtMdsGroup.getText().equals("")){
            ClientUtil.showAlertWindow("Please select MDS Group before submitting...");
        } else {
            //TermDepositOB.setMdsGroup(CommonUtil.convertObjToStr(txtMdsGroup.getText()));
            TermDepositOB.setMdsGroup(TermDepositOB.getMdsGroup());    // Added by nithya on 05-08-2016        
            TermDepositOB.setMdsRemarks(CommonUtil.convertObjToStr(txaMdsRemarks.getText()));
            TermDepositUI.mdsGroupFilled = true;
            this.dispose();
        }
    }//GEN-LAST:event_btnSubmitActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        TermDepositOB.setMdsGroup("");
        TermDepositOB.setMdsRemarks("");
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void popUp(int field) {

        final HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        if (field == MDS_GROUP) {
            whereMap.put("MDS.GROUP", "MDS.GROUP");
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "getMdsGroup");
        }
        viewMap.put("MDS_GROUP", "MDS_GROUP");
        new ViewAll(this, viewMap).show();
    }

    public void fillData(Object param) {
        HashMap map = (HashMap) param;
        System.out.println("map..." + map);
        if (map.containsKey("DESCRIPTION") && map.containsKey("COMPANYID")) {
            txtMdsGroup.setText(CommonUtil.convertObjToStr(map.get("DESCRIPTION")));
            System.out.println("setting here");
          TermDepositOB.setMdsGroup(CommonUtil.convertObjToStr(map.get("COMPANYID")));
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MdsGroupUI dialog = new MdsGroupUI(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnMdsGroup;
    private com.see.truetransact.uicomponent.CButton btnRefresh;
    private com.see.truetransact.uicomponent.CButton btnSubmit;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private com.see.truetransact.uicomponent.CLabel lblMdsGroup;
    private com.see.truetransact.uicomponent.CTextArea txaMdsRemarks;
    public static com.see.truetransact.uicomponent.CTextField txtMdsGroup;
    // End of variables declaration//GEN-END:variables
}
