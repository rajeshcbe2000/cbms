/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * CashStockTransferUI.java
 *
 * Created on January 13, 2004, 4:28 PM
 */

package com.see.truetransact.ui.forex;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.uicomponent.CLabel;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.Date;
/**
 * @author  bala
 */
public class CashStockTransferUI extends com.see.truetransact.uicomponent.CInternalFrame {
    private double _amount=0.0;
    private Float _fltDenomination[];
    private String _currencyType;
    private int viewType;
    private final int VIEWUSERTYPE1=1;
    private final int VIEWUSERTYPE2=2;
    private final int VIEWBRANCHTYPE=3;
    private final String BRANCHCODE = "BRANCH CODE";
    private final String USERID = "USER ID";

    private CTextField txtCount[];
    private CLabel lblDenomination[];
    private CLabel lblMulti[];
    private CLabel lblEqui[];
    private CTextField txtTotal[];
    private CLabel lblGrandTotal;
    private CTextField txtGrandTotal;
    private String userID, branchCode;
    private Date currDt = null;
    /** Creates new form DenominationUI */
    public CashStockTransferUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        setupComboData();
    }

    /* Setup initial combo box values */
    private void setupComboData() {
        try {
            userID = TrueTransactMain.USERINFO.get("USER_ID").toString();
            branchCode = TrueTransactMain.BRANCHINFO.get("BRANCH_CODE").toString();
            txtBranch.setText(branchCode);
            
            if (userID != null)
                txtUserID.setText(userID);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        HashMap param = new HashMap();
        param.put(CommonConstants.MAP_NAME,null);

        ArrayList lookupKey = new ArrayList();
        lookupKey.add("FOREX.CURRENCY");

        param.put(CommonConstants.PARAMFORQUERY, lookupKey);
        HashMap lookupValues = ClientUtil.populateLookupData(param);

        HashMap keyValue = (HashMap)lookupValues.get("FOREX.CURRENCY");
        ArrayList key = (ArrayList) keyValue.get(CommonConstants.KEY);
        ArrayList value = (ArrayList) keyValue.get(CommonConstants.VALUE);
        ComboBoxModel cbmCurrency = new ComboBoxModel(key,value);
        
        cboCurrency.setModel(cbmCurrency);
    }
    
    /* Generating the Denomination UI based on the selected combobox value */
    private void initDenominationScreen () {
        panDenomination.removeAll();

        _currencyType = ((ComboBoxModel) cboCurrency.getModel()).getKeyForSelected().toString() ;
        
        HashMap whereMap = new HashMap();
        whereMap.put ("CURRENCY", _currencyType);
        List data = (List) ClientUtil.executeQuery("getDenominations", whereMap);

        final int lstSize = data.size();

        java.awt.GridBagConstraints gridBagConstraints;

        lblDenomination = new CLabel[lstSize];
        lblMulti = new CLabel[lstSize];
        txtCount = new CTextField[lstSize];
        lblEqui = new CLabel[lstSize];
        txtTotal = new CTextField[lstSize];
        _fltDenomination = new Float[lstSize];
        
        lblGrandTotal = new CLabel();
        txtGrandTotal = new CTextField();

        HashMap mapData;
        
        for (int i=0; i < lstSize; i++) {
            lblDenomination[i] = new CLabel();
            lblMulti[i] = new CLabel();
            txtCount[i] = new CTextField();
            lblEqui[i] = new CLabel();
            txtTotal[i] = new CTextField();
            
            mapData = (HashMap) data.get(i);
            _fltDenomination[i] = new Float(mapData.get("DENOMINATION_VALUE").toString());

            lblDenomination[i].setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            lblDenomination[i].setText(mapData.get("DENOMINATION_NAME").toString());
            lblDenomination[i].setPreferredSize(new java.awt.Dimension(50, 21));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = i+1;
            gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
            panDenomination.add(lblDenomination[i], gridBagConstraints);

            lblMulti[i].setText("x");
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = i+1;
            gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
            panDenomination.add(lblMulti[i], gridBagConstraints);

            txtCount[i].setHorizontalAlignment(javax.swing.JTextField.RIGHT);
            txtCount[i].setPreferredSize(new java.awt.Dimension(50, 21));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 2;
            gridBagConstraints.gridy = i+1;
            gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
            panDenomination.add(txtCount[i], gridBagConstraints);
            
            lblEqui[i].setText("=");
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 3;
            gridBagConstraints.gridy = i+1;
            gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
            panDenomination.add(lblEqui[i], gridBagConstraints);

            txtTotal[i].setEditable(false);
            txtTotal[i].setHorizontalAlignment(javax.swing.JTextField.RIGHT);
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 4;
            gridBagConstraints.gridy = i+1;
            gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
            panDenomination.add(txtTotal[i], gridBagConstraints);
            
            final int j = i;
            txtCount[i].addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusLost(java.awt.event.FocusEvent evt) {
                    txtTotal[j].setText(
                        String.valueOf(
                            _fltDenomination[j].doubleValue() * 
                            new Float(txtCount[j].getText().equals("") ? "0.0" :  txtCount[j].getText()).doubleValue()));

                    double grandTotal=0.0, dblValue=0.0;
                    for (int cnt=0; cnt < lstSize; cnt++) {
                        grandTotal += new Float(txtTotal[cnt].getText().equals("") ? "0.0" :  txtTotal[cnt].getText()).doubleValue();
                    }
                    txtGrandTotal.setText(String.valueOf(grandTotal));
                }
            });            
        }
        
        lblGrandTotal.setText("Total");
        lblGrandTotal.setHorizontalAlignment(javax.swing.JLabel.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = gridBagConstraints.EAST;
        gridBagConstraints.gridy = lstSize + 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDenomination.add(lblGrandTotal, gridBagConstraints);

        txtGrandTotal.setEditable(false);
        txtGrandTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = lstSize + 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDenomination.add(txtGrandTotal, gridBagConstraints);
        panDenomination.repaint();
        pack();
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        rdgTransType = new com.see.truetransact.uicomponent.CButtonGroup();
        panDenomination = new com.see.truetransact.uicomponent.CPanel();
        panTop = new com.see.truetransact.uicomponent.CPanel();
        lblCurrencyType = new com.see.truetransact.uicomponent.CLabel();
        cboCurrency = new com.see.truetransact.uicomponent.CComboBox();
        lblBranch = new com.see.truetransact.uicomponent.CLabel();
        txtBranch = new com.see.truetransact.uicomponent.CTextField();
        lblUserID = new com.see.truetransact.uicomponent.CLabel();
        txtUserID = new com.see.truetransact.uicomponent.CTextField();
        btnToBranchCode = new com.see.truetransact.uicomponent.CButton();
        btnToUserID = new com.see.truetransact.uicomponent.CButton();
        btnUserID = new com.see.truetransact.uicomponent.CButton();
        lblToBranch = new com.see.truetransact.uicomponent.CLabel();
        lblToUserID = new com.see.truetransact.uicomponent.CLabel();
        txtToBranchCode = new com.see.truetransact.uicomponent.CTextField();
        txtToUserID = new com.see.truetransact.uicomponent.CTextField();
        rdoTeller = new com.see.truetransact.uicomponent.CRadioButton();
        rdoBranch = new com.see.truetransact.uicomponent.CRadioButton();
        panBottom = new com.see.truetransact.uicomponent.CPanel();
        btnClose = new com.see.truetransact.uicomponent.CButton();

        getContentPane().setLayout(new java.awt.BorderLayout(5, 5));

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Transfer Cash");
        panDenomination.setLayout(new java.awt.GridBagLayout());

        panDenomination.setBorder(new javax.swing.border.TitledBorder(" Denominationwise Count "));
        panDenomination.setMinimumSize(new java.awt.Dimension(350, 350));
        panDenomination.setPreferredSize(new java.awt.Dimension(350, 350));
        getContentPane().add(panDenomination, java.awt.BorderLayout.CENTER);

        panTop.setLayout(new java.awt.GridBagLayout());

        panTop.setBorder(new javax.swing.border.TitledBorder(" Transfer Cash to other Teller/Branch "));
        lblCurrencyType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCurrencyType.setText("Currency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTop.add(lblCurrencyType, gridBagConstraints);

        cboCurrency.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCurrencyActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTop.add(cboCurrency, gridBagConstraints);

        lblBranch.setText("Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTop.add(lblBranch, gridBagConstraints);

        txtBranch.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTop.add(txtBranch, gridBagConstraints);

        lblUserID.setText("User ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTop.add(lblUserID, gridBagConstraints);

        txtUserID.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTop.add(txtUserID, gridBagConstraints);

        btnToBranchCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnToBranchCode.setPreferredSize(new java.awt.Dimension(21, 21));
        btnToBranchCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToBranchCodeActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 4, 4);
        panTop.add(btnToBranchCode, gridBagConstraints);

        btnToUserID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnToUserID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnToUserID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToUserIDActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 4, 4);
        panTop.add(btnToUserID, gridBagConstraints);

        btnUserID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnUserID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnUserID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUserIDActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 4, 4);
        panTop.add(btnUserID, gridBagConstraints);

        lblToBranch.setText("To Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTop.add(lblToBranch, gridBagConstraints);

        lblToUserID.setText("To User ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTop.add(lblToUserID, gridBagConstraints);

        txtToBranchCode.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTop.add(txtToBranchCode, gridBagConstraints);

        txtToUserID.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTop.add(txtToUserID, gridBagConstraints);

        rdoTeller.setText("Teller to Teller");
        rdgTransType.add(rdoTeller);
        rdoTeller.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTellerActionPerformed(evt);
            }
        });

        panTop.add(rdoTeller, new java.awt.GridBagConstraints());

        rdoBranch.setText("Branch to Branch");
        rdgTransType.add(rdoBranch);
        rdoBranch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoBranchActionPerformed(evt);
            }
        });

        panTop.add(rdoBranch, new java.awt.GridBagConstraints());

        getContentPane().add(panTop, java.awt.BorderLayout.NORTH);

        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        panBottom.add(btnClose);

        getContentPane().add(panBottom, java.awt.BorderLayout.SOUTH);

        pack();
    }//GEN-END:initComponents
    private void transType() {
        if (rdoBranch.isSelected()) {
            btnToBranchCode.setEnabled(true);

            txtToUserID.setText("");
            txtToBranchCode.setText("");

            btnUserID.setEnabled(false);
            btnToUserID.setEnabled(false);
        } else {
            txtToBranchCode.setText(txtBranch.getText());
            btnToBranchCode.setEnabled(false);

            btnUserID.setEnabled(true);
            btnToUserID.setEnabled(true);
        }
    }
    private void rdoBranchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoBranchActionPerformed
        // Add your handling code here:
        transType();
    }//GEN-LAST:event_rdoBranchActionPerformed
    private void rdoTellerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTellerActionPerformed
        // Add your handling code here:
        transType();
    }//GEN-LAST:event_rdoTellerActionPerformed
    private void popup(int currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        if (viewType == VIEWBRANCHTYPE)
            viewMap.put(CommonConstants.MAP_NAME, "getSelectBranchList");
        else 
            viewMap.put(CommonConstants.MAP_NAME, "getSelectUserList");

        new ViewAll(this, viewMap).show();
    }
    
    public void fillData(Object obj) {
        HashMap hash = (HashMap) obj;
        if (viewType != 0) {
            if (viewType == VIEWBRANCHTYPE) {
                txtToBranchCode.setText((String) hash.get(BRANCHCODE));
            } else if (viewType == VIEWUSERTYPE1) {
                txtUserID.setText((String) hash.get(USERID));
            } else if (viewType == VIEWUSERTYPE2) {
                txtToUserID.setText((String) hash.get(USERID));
            }
        }
    }
    private void btnToUserIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToUserIDActionPerformed
        // Add your handling code here:
        popup(VIEWUSERTYPE2);
    }//GEN-LAST:event_btnToUserIDActionPerformed
    private void btnToBranchCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToBranchCodeActionPerformed
        // Add your handling code here:
        popup(VIEWBRANCHTYPE);
    }//GEN-LAST:event_btnToBranchCodeActionPerformed
    private void btnUserIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUserIDActionPerformed
        // Add your handling code here:
        popup(VIEWUSERTYPE1);
    }//GEN-LAST:event_btnUserIDActionPerformed
    private void cboCurrencyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCurrencyActionPerformed
        // Add your handling code here:
        if (txtCount != null)
            saveData();
        initDenominationScreen();
    }//GEN-LAST:event_cboCurrencyActionPerformed
    private void saveData() {
        if (_fltDenomination != null) {
            HashMap whereMap;
            String strCnt = "";
            for (int i=0; i < _fltDenomination.length; i++) {            
                strCnt = txtCount[i].getText().trim();
                if (strCnt != null && !strCnt.equals("0.0") && !strCnt.equals("")) {
                    whereMap = new HashMap();
                    whereMap.put ("USER_ID", txtUserID.getText());
                    whereMap.put ("BRANCH_CODE", txtBranch.getText());
                    whereMap.put ("TO_BRANCH_CODE", txtToBranchCode.getText());
                    whereMap.put ("TO_USER_ID", txtToUserID.getText());
                    whereMap.put ("CREATED_BY", userID);
                    whereMap.put ("STATUS", CommonConstants.STATUS_CREATED);
                    whereMap.put ("CURRENCY", _currencyType);
                    whereMap.put ("DENOMINATION_COUNT", new Double(txtCount[i].getText()));
                    whereMap.put ("DENOMINATION_VALUE", _fltDenomination[i]);
                    whereMap.put("TODAY_DT", currDt.clone());
                    System.out.println (whereMap);
                    ClientUtil.execute("insertBranchCashTrans", whereMap);
                }
            }
        }
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        saveData();
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        //System.exit(0);
        this.dispose();
    }//GEN-LAST:event_exitForm
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        CashStockTransferUI dui = new CashStockTransferUI();
        dui.show();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnToBranchCode;
    private com.see.truetransact.uicomponent.CButton btnToUserID;
    private com.see.truetransact.uicomponent.CButton btnUserID;
    private com.see.truetransact.uicomponent.CComboBox cboCurrency;
    private com.see.truetransact.uicomponent.CLabel lblBranch;
    private com.see.truetransact.uicomponent.CLabel lblCurrencyType;
    private com.see.truetransact.uicomponent.CLabel lblToBranch;
    private com.see.truetransact.uicomponent.CLabel lblToUserID;
    private com.see.truetransact.uicomponent.CLabel lblUserID;
    private com.see.truetransact.uicomponent.CPanel panBottom;
    private com.see.truetransact.uicomponent.CPanel panDenomination;
    private com.see.truetransact.uicomponent.CPanel panTop;
    private com.see.truetransact.uicomponent.CButtonGroup rdgTransType;
    private com.see.truetransact.uicomponent.CRadioButton rdoBranch;
    private com.see.truetransact.uicomponent.CRadioButton rdoTeller;
    private com.see.truetransact.uicomponent.CTextField txtBranch;
    private com.see.truetransact.uicomponent.CTextField txtToBranchCode;
    private com.see.truetransact.uicomponent.CTextField txtToUserID;
    private com.see.truetransact.uicomponent.CTextField txtUserID;
    // End of variables declaration//GEN-END:variables
}