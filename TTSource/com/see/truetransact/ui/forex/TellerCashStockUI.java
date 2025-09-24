/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TellerCashStockUI.java
 *
 * Created on January 13, 2004, 4:28 PM
 */

package com.see.truetransact.ui.forex;

import java.util.HashMap;
import java.util.ArrayList;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.uicomponent.CLabel;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.ui.TrueTransactMain;
/**
 * @author  bala
 */
public class TellerCashStockUI extends com.see.truetransact.uicomponent.CInternalFrame {
    private double _amount=0.0;
    private Float _fltDenomination[];
    private String _currencyType;
    
    private CTextField txtCount[];
    private CLabel lblDenomination[];
    private CLabel lblDenominationType[];
    private CLabel lblMulti[];
    private CLabel lblEqui[];
    private CTextField txtTotal[];
    private CLabel lblGrandTotal;
    private CTextField txtGrandTotal;
    private String _dayMode;
    private String userID, branchCode;
    
    /** Creates new form DenominationUI */
    public TellerCashStockUI(String dayMode) {
        _dayMode = dayMode;
        initComponents();
        setupComboData();
        
        if (_dayMode.equalsIgnoreCase("DAYBEGIN"))
            setTitle("Day Begin Cash Stock");
        else 
            setTitle("Day End Cash Stock");
    }

    private void setupComboData() {
        try {
            userID= TrueTransactMain.USERINFO.get("USER_ID").toString();
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
    
    private void initDenominationScreen () {
        panDenomination.removeAll();

        _currencyType = ((ComboBoxModel) cboCurrency.getModel()).getKeyForSelected().toString() ;
        
        HashMap whereMap = new HashMap();
        whereMap.put ("CURRENCY", _currencyType);
        java.util.List data = (java.util.List) ClientUtil.executeQuery("getDenominations", whereMap);

        final int lstSize = data.size();

        java.awt.GridBagConstraints gridBagConstraints;
        lblDenominationType=new CLabel[lstSize];
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
            lblDenominationType[i]=new  CLabel();
            lblDenomination[i] = new CLabel();
            lblMulti[i] = new CLabel();
            txtCount[i] = new CTextField();
            lblEqui[i] = new CLabel();
            txtTotal[i] = new CTextField();
             txtCount[i].setAllowAll(true);
            mapData = (HashMap) data.get(i);
            _fltDenomination[i] = new Float(mapData.get("DENOMINATION_VALUE").toString());

            lblDenominationType[i].setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
//           lblDenominationType[i].setText("<HTML></b><font color=blue>"+CommonUtil.convertObjToStr(mapData.get("DENOMINATION_TYPE"))+ "</font></b></html>");
             lblDenominationType[i].setText(CommonUtil.convertObjToStr(mapData.get("DENOMINATION_TYPE")));
            lblDenominationType[i].setPreferredSize(new java.awt.Dimension(50, 21));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = i+1;
            gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
            panDenomination.add(lblDenominationType[i], gridBagConstraints);
            
            lblDenomination[i].setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            lblDenomination[i].setText(mapData.get("DENOMINATION_NAME").toString());
            lblDenomination[i].setPreferredSize(new java.awt.Dimension(140, 21));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = i+1;
            gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
            panDenomination.add(lblDenomination[i], gridBagConstraints);

            
            if(i==0){
                lblDenominationType[i].setVisible(true);
            }else{
                if(lblDenominationType[i].getText().equals(lblDenominationType[i-1].getText()))
                {       
                 lblDenominationType[i].setVisible(false);
                
                }else{
                lblDenominationType[i].setVisible(true);
                }
            }

            lblMulti[i].setText("x");
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 2;
            gridBagConstraints.gridy = i+1;
            gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
            panDenomination.add(lblMulti[i], gridBagConstraints);

            txtCount[i].setHorizontalAlignment(javax.swing.JTextField.RIGHT);
            txtCount[i].setPreferredSize(new java.awt.Dimension(50, 21));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 3;
            gridBagConstraints.gridy = i+1;
            gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
            panDenomination.add(txtCount[i], gridBagConstraints);
            
            lblEqui[i].setText("=");
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 4;
            gridBagConstraints.gridy = i+1;
            gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
            panDenomination.add(lblEqui[i], gridBagConstraints);

            txtTotal[i].setEditable(false);
            txtTotal[i].setHorizontalAlignment(javax.swing.JTextField.RIGHT);
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 5;
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

        panDenomination = new com.see.truetransact.uicomponent.CPanel();
        panTop = new com.see.truetransact.uicomponent.CPanel();
        lblCurrencyType = new com.see.truetransact.uicomponent.CLabel();
        cboCurrency = new com.see.truetransact.uicomponent.CComboBox();
        lblBranch = new com.see.truetransact.uicomponent.CLabel();
        txtBranch = new com.see.truetransact.uicomponent.CTextField();
        lblUserID = new com.see.truetransact.uicomponent.CLabel();
        txtUserID = new com.see.truetransact.uicomponent.CTextField();
        chkCashBox = new com.see.truetransact.uicomponent.CCheckBox();
        panBottom = new com.see.truetransact.uicomponent.CPanel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();

        getContentPane().setLayout(new java.awt.BorderLayout(5, 5));

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setPreferredSize(new java.awt.Dimension(600, 670));
        panDenomination.setLayout(new java.awt.GridBagLayout());

        panDenomination.setBorder(new javax.swing.border.TitledBorder(" Denominationwise Count "));
        panDenomination.setMinimumSize(new java.awt.Dimension(271, 450));
        panDenomination.setPreferredSize(new java.awt.Dimension(271, 450));
        getContentPane().add(panDenomination, java.awt.BorderLayout.CENTER);

        panTop.setLayout(new java.awt.GridBagLayout());

        panTop.setBorder(new javax.swing.border.TitledBorder(" Teller Cash Stock"));
        lblCurrencyType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCurrencyType.setText("Currency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
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
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTop.add(cboCurrency, gridBagConstraints);

        lblBranch.setText("Branch");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTop.add(lblBranch, gridBagConstraints);

        txtBranch.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTop.add(txtBranch, gridBagConstraints);

        lblUserID.setText("User ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTop.add(lblUserID, gridBagConstraints);

        txtUserID.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTop.add(txtUserID, gridBagConstraints);

        chkCashBox.setText("CashBox");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        panTop.add(chkCashBox, gridBagConstraints);

        getContentPane().add(panTop, java.awt.BorderLayout.NORTH);

        btnCancel.setText("Save");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        panBottom.add(btnCancel);

        getContentPane().add(panBottom, java.awt.BorderLayout.SOUTH);

        pack();
    }//GEN-END:initComponents

    private void cboCurrencyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCurrencyActionPerformed
        // Add your handling code here:
        if (txtCount != null)
            saveData();
        initDenominationScreen();
    }//GEN-LAST:event_cboCurrencyActionPerformed
    private void saveData() {
        // Add your handling code here:
        if (_fltDenomination != null) {
            HashMap whereMap;
            String strCnt = "";

            for (int i=0; i < _fltDenomination.length; i++) {            
                strCnt = txtCount[i].getText().trim();
                String dtype=lblDenominationType[i].getText();
               
                
                if (strCnt != null && !strCnt.equals("0.0") && !strCnt.equals("")) {
                    whereMap = new HashMap();
                 
                    whereMap.put ("DENOMINATION_TYPE", dtype);
                   
                    whereMap.put ("USERID", userID);
                    whereMap.put ("BRANCH", branchCode);
                    whereMap.put ("CURRENCY", _currencyType);
                    whereMap.put ("COUNT", CommonUtil.convertObjToStr(txtCount[i].getText()));
                    whereMap.put ("DENOMINATION", _fltDenomination[i]);
                     
                    whereMap.put ("DENOMINATION_NAME", lblDenomination[i].getText());
                    
                    whereMap.put("TODAY_DT", ClientUtil.getCurrentDate());    
                    
                    if(chkCashBox.isSelected()){
                        whereMap.put("SOURCE", "CASH BOX"); 
                    }else{
                        whereMap.put("SOURCE", "VAULTREC"); 
                    }
                    System.out.println (whereMap);

                    
                     
                    if (_dayMode.equalsIgnoreCase("DAYBEGIN")) {
                        java.util.List lis=ClientUtil.executeQuery("getDenominationName", whereMap);
                        if(lis!=null && lis.size()>0){
                            ClientUtil.displayAlert("Denomination count already created for " + lblDenomination[i].getText() + " rupee " + lblDenominationType[i].getText() );
//                            return;
                        }
                        else{
                        ClientUtil.execute("tellerCountDayBegin", whereMap);
                        }
                    } else {
                        ClientUtil.execute("tellerCountDayEnd", whereMap);
                    }
                }
            }
        }
    }    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        saveData();
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        //System.exit(0);
        this.dispose();
    }//GEN-LAST:event_exitForm
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        TellerCashStockUI dui = new TellerCashStockUI("DAYBEGIN");
        dui.show();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CComboBox cboCurrency;
    private com.see.truetransact.uicomponent.CCheckBox chkCashBox;
    private com.see.truetransact.uicomponent.CLabel lblBranch;
    private com.see.truetransact.uicomponent.CLabel lblCurrencyType;
    private com.see.truetransact.uicomponent.CLabel lblUserID;
    private com.see.truetransact.uicomponent.CPanel panBottom;
    private com.see.truetransact.uicomponent.CPanel panDenomination;
    private com.see.truetransact.uicomponent.CPanel panTop;
    private com.see.truetransact.uicomponent.CTextField txtBranch;
    private com.see.truetransact.uicomponent.CTextField txtUserID;
    // End of variables declaration//GEN-END:variables
}