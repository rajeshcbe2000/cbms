/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * SuspenceAcctSearchUI.java
 */

package com.see.truetransact.ui.termloan;

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

public class SuspenceAcctSearchUI extends javax.swing.JDialog{

    final int TRANS_PROD = 1, ACC_NUM=2;
    public String branchID;
    Date currDt=null;
    private String PROD_ID = "PROD_ID";
    
    public SuspenceAcctSearchUI(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public SuspenceAcctSearchUI(TermLoanUI termLoanUi) {
        initForm();
    }

    public SuspenceAcctSearchUI(String screen) {
    }

    private void initForm() {
        initComponents();
        setFieldNames();
        branchID = TrueTransactMain.BRANCH_ID;
        currDt = ClientUtil.getCurrentDate();
    }

    private void setFieldNames() {
        lblAccountNo.setName("lblAccountNo");
        lblProductId.setName("lblProductId");
        txtAccountNo.setName("txtAccountNo");
        txtProductId.setName("txtProductId");
        btnAccountNo.setName("btnAccountNo");
        btnProductId.setName("btnProductId");
    }
    
    public void resetform() {
        txtAccountNo.setText("");
        txtProductId.setText("");
        TermLoanOB.setSuspenceAccountNo("");
        TermLoanOB.setSuspenceProductID("");
        TermLoanOB.setTableCheck(false);
    }

    public void show() {
        setTitle("Search Suspense Account " + "[" + branchID + "]");
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

        lblProductId = new com.see.truetransact.uicomponent.CLabel();
        lblAccountNo = new com.see.truetransact.uicomponent.CLabel();
        txtAccountNo = new com.see.truetransact.uicomponent.CTextField();
        txtProductId = new com.see.truetransact.uicomponent.CTextField();
        btnProductId = new com.see.truetransact.uicomponent.CButton();
        btnAccountNo = new com.see.truetransact.uicomponent.CButton();
        btnSubmit = new com.see.truetransact.uicomponent.CButton();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnRefresh = new com.see.truetransact.uicomponent.CButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(150, 150));
        setPreferredSize(new java.awt.Dimension(285, 124));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        lblProductId.setText("Prod Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 8);
        getContentPane().add(lblProductId, gridBagConstraints);

        lblAccountNo.setText("Account No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(lblAccountNo, gridBagConstraints);

        txtAccountNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccountNo.setName("txtAccountNo"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(txtAccountNo, gridBagConstraints);

        txtProductId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtProductId.setName("txtAccountNo"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(txtProductId, gridBagConstraints);

        btnProductId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnProductId.setMaximumSize(new java.awt.Dimension(25, 25));
        btnProductId.setMinimumSize(new java.awt.Dimension(25, 25));
        btnProductId.setName("btnAccountNo"); // NOI18N
        btnProductId.setPreferredSize(new java.awt.Dimension(25, 25));
        btnProductId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 1, 0);
        getContentPane().add(btnProductId, gridBagConstraints);

        btnAccountNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccountNo.setMaximumSize(new java.awt.Dimension(25, 25));
        btnAccountNo.setMinimumSize(new java.awt.Dimension(25, 25));
        btnAccountNo.setName("btnAccountNo"); // NOI18N
        btnAccountNo.setPreferredSize(new java.awt.Dimension(25, 25));
        btnAccountNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccountNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(btnAccountNo, gridBagConstraints);

        btnSubmit.setText("Submit");
        btnSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        getContentPane().add(btnSubmit, gridBagConstraints);

        btnClose.setText("Cancel");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        getContentPane().add(btnClose, gridBagConstraints);

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
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 1, 0);
        getContentPane().add(btnRefresh, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAccountNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountNoActionPerformed
        // TODO add your handling code here:
        resetform();
        popUp(TRANS_PROD);
    }//GEN-LAST:event_btnAccountNoActionPerformed

    private void btnProductIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductIdActionPerformed
        // TODO add your handling code here:
        popUp(ACC_NUM);
    }//GEN-LAST:event_btnProductIdActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        resetform();
        TermLoanOB.setTableCheck(true);
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
        resetform();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
        // TODO add your handling code here:
        if((txtAccountNo.getText()== null || txtAccountNo.getText().equals("")) || (txtProductId.getText() == null || txtProductId.getText().equals(""))){
            ClientUtil.showAlertWindow("Please select Prod Id and Accound No...");
        } else {
            TermLoanOB.setSuspenceAccountNo(CommonUtil.convertObjToStr(txtAccountNo.getText()));
            TermLoanOB.setSuspenceProductID(CommonUtil.convertObjToStr(txtProductId.getText()));
            this.dispose();
        }
    }//GEN-LAST:event_btnSubmitActionPerformed

    private void popUp(int field) {

        final HashMap viewMap = new HashMap();
        if (field == TRANS_PROD) {
            HashMap where_map = new HashMap();
            where_map.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_NAME, "InterMaintenance.getProductData" + "SA");
            viewMap.put(CommonConstants.MAP_WHERE, where_map);
        }
        if (field == ACC_NUM) {
            HashMap where_map = new HashMap();
            viewMap.put(CommonConstants.MAP_NAME, "Remittance.getAccountDataSA");
            where_map.put(CommonConstants.PRODUCT_ID, txtProductId.getText());
            where_map.put(PROD_ID, txtProductId.getText());
            where_map.put("SELECTED_BRANCH", ProxyParameters.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, where_map);
        }
        viewMap.put("SUSPENSE_SEARCH", "SUSPENSE_SEARCH");
        new ViewAll(this, viewMap).show();
    }

    public void fillData(Object param) {
        HashMap map = (HashMap) param;
        System.out.println("map..." + map);
        if (map.containsKey("PROD_ID")) {
            txtProductId.setText(CommonUtil.convertObjToStr(map.get("PROD_ID")));
        } else if(map.containsKey("ACT_NUM")) {
            txtAccountNo.setText(CommonUtil.convertObjToStr(map.get("ACT_NUM")));
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                SuspenceAcctSearchUI dialog = new SuspenceAcctSearchUI(new javax.swing.JFrame(), true);
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
    private com.see.truetransact.uicomponent.CButton btnAccountNo;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnProductId;
    private com.see.truetransact.uicomponent.CButton btnRefresh;
    private com.see.truetransact.uicomponent.CButton btnSubmit;
    private com.see.truetransact.uicomponent.CLabel lblAccountNo;
    private com.see.truetransact.uicomponent.CLabel lblProductId;
    public static com.see.truetransact.uicomponent.CTextField txtAccountNo;
    public static com.see.truetransact.uicomponent.CTextField txtProductId;
    // End of variables declaration//GEN-END:variables
}
