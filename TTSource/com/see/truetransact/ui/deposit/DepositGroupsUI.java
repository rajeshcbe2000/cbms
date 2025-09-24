
/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * DepositGroupsUI.java
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

public class DepositGroupsUI extends javax.swing.JDialog{

    final int TRANS_PROD = 1, DEPOSIT_GROUP=2;
    public String branchID;
    Date currDt=null;
    private String depositProdId = "";
    
    public DepositGroupsUI(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        currDt = ClientUtil.getCurrentDate();
        initComponents();
    }

    public DepositGroupsUI(TermDepositUI termDepositUi) {
        initForm();
    }
    
    public DepositGroupsUI(HashMap map) {
        initForm();
        if(map.containsKey("GROUP_NO")){
            txtDepositGroup.setText(CommonUtil.convertObjToStr(map.get("MDS_GROUP")));
        }
       // if(map.containsKey("MDS_REMARKS")){
        //    txaMdsRemarks.setText(CommonUtil.convertObjToStr(map.get("MDS_REMARKS")));
       // }
        if(map.containsKey("PROD_ID")){ // Passing product id 
            depositProdId = CommonUtil.convertObjToStr(map.get("PROD_ID"));
        }
    }

    public DepositGroupsUI(String screen) {
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
        txtDepositGroup.setName("txtMdsGroup");
        btnDepositGroup.setName("btnMdsGroup");
    }
    
    public void resetform() {
        txtDepositGroup.setText("");
        TermDepositOB.setDepositGroup("");
        TermDepositOB.setDepositGroupRemarks("");
    }

    public void show() {
        setTitle("Search Deposit Group " + "[" + branchID + "]");
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
        txtDepositGroup = new com.see.truetransact.uicomponent.CTextField();
        btnDepositGroup = new com.see.truetransact.uicomponent.CButton();
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

        lblMdsGroup.setText("Deposit Group");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(88, 65, 0, 0);
        getContentPane().add(lblMdsGroup, gridBagConstraints);

        txtDepositGroup.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDepositGroup.setName("txtAccountNo"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 67;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(85, 8, 0, 0);
        getContentPane().add(txtDepositGroup, gridBagConstraints);

        btnDepositGroup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDepositGroup.setMaximumSize(new java.awt.Dimension(25, 25));
        btnDepositGroup.setMinimumSize(new java.awt.Dimension(25, 25));
        btnDepositGroup.setName("btnDepositGroup"); // NOI18N
        btnDepositGroup.setPreferredSize(new java.awt.Dimension(25, 25));
        btnDepositGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepositGroupActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(85, 2, 0, 65);
        getContentPane().add(btnDepositGroup, gridBagConstraints);

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

        btnCancel.setText("Cancel");
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

    private void btnDepositGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDepositGroupActionPerformed
        // TODO add your handling code here:
        resetform();
        popUp(DEPOSIT_GROUP);
    }//GEN-LAST:event_btnDepositGroupActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
        resetform();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
        // TODO add your handling code here:
        if(txtDepositGroup.getText() == null || txtDepositGroup.getText().equals("")){
            ClientUtil.showAlertWindow("Please select Deposit group before submitting...");
        } else {
            //TermDepositOB.setMdsGroup(CommonUtil.convertObjToStr(txtMdsGroup.getText()));
            TermDepositOB.setDepositGroup(TermDepositOB.getDepositGroup());    // Added by nithya on 05-08-2016        
            TermDepositOB.setDepositGroupRemarks(CommonUtil.convertObjToStr(txaMdsRemarks.getText()));
            TermDepositUI.depositGroupFilled = true;
            this.dispose();
        }
    }//GEN-LAST:event_btnSubmitActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        TermDepositOB.setDepositGroup("");
        TermDepositOB.setDepositGroupRemarks("");
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void popUp(int field) {

        final HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        if (field == DEPOSIT_GROUP) {
           // whereMap.put("MDS.GROUP", "MDS.GROUP");
            whereMap.put("PROD_ID",depositProdId);
            whereMap.put("BRANCH_ID",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "getDepositGroup");
        }
        viewMap.put("DEPOSIT_GROUP", "DEPOSIT_GROUP");
        new ViewAll(this, viewMap).show();
    }

    public void fillData(Object param) {
        HashMap map = (HashMap) param;
        System.out.println("map..." + map);       
        if (map.containsKey("GROUP_NAME") && map.containsKey("GROUP_NO")) {
            txtDepositGroup.setText(CommonUtil.convertObjToStr(map.get("GROUP_NAME")));
            System.out.println("setting here");
          TermDepositOB.setDepositGroup(CommonUtil.convertObjToStr(map.get("GROUP_NO")));
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DepositGroupsUI dialog = new DepositGroupsUI(new javax.swing.JFrame(), true);
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
    private com.see.truetransact.uicomponent.CButton btnDepositGroup;
    private com.see.truetransact.uicomponent.CButton btnRefresh;
    private com.see.truetransact.uicomponent.CButton btnSubmit;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private com.see.truetransact.uicomponent.CLabel lblMdsGroup;
    private com.see.truetransact.uicomponent.CTextArea txaMdsRemarks;
    public static com.see.truetransact.uicomponent.CTextField txtDepositGroup;
    // End of variables declaration//GEN-END:variables
}
