/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DenominationUI.java
 *
 * Created on January 13, 2004, 4:28 PM
 */

package com.see.truetransact.ui.common.denomination;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CLabel;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.NumericValidation;

import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.transferobject.transaction.cashmanagement.CashMovementDetailsTO;
import java.util.Date;
/**
 * @author  bala
 */
public class DenominationUI extends com.see.truetransact.uicomponent.CDialog {
    private String _currencyType;
    private String _transType;
    private double _amount=0.0;
    private Double _fltDenomination[];
    private String _strDenominationType[];
    
    private CTextField txtCount[];
    private CLabel lblDenomination[];
    private CLabel lblMulti[];
    private CLabel lblEqui[];
    private CTextField txtTotal[];
    
    private CLabel lblAvailDenomination[];  //created by Rajesh
    
    private CLabel lblGrandTotal;
    private CTextField txtGrandTotal;
    private Date currDt = null;
    /** Creates new form DenominationUI */
    public DenominationUI(HashMap param) {
        currDt = ClientUtil.getCurrentDate();
        setupInit(param);
    }

    private void setupInit(HashMap param) {
        _currencyType = (String) param.get("CURRENCY_TYPE");
        _transType = (String) param.get("TRANS_TYPE");
        
        initComponents();
        initDenominationScreen ();
        
        String amtWD = param.containsKey("AMOUNT") ? (String) param.get("AMOUNT") : "";
        
        setTitle(_transType + " : [" + _currencyType + "] " + amtWD);
        
        if (!amtWD.equals("") && amtWD != null)
            _amount = CommonUtil.convertObjToDouble(amtWD).doubleValue();

        if (param.get("DENOMINATION_LIST") != null) {
            initScreenAmount((ArrayList)param.get("DENOMINATION_LIST"));
        }
//        if (_transType.equals("Withdrawal") || _transType.equals("Debit")) {
//            initWithdrawAmount();
//        }
        pack();        
    }
    
    private void initScreenAmount(ArrayList denominationList) {
        if (denominationList.size()== 0) return;
        double denoAmt = 0.0, totalAmount=0.0;
        int count = 0;
        HashMap denoMap = null;
        double denomination;
        for (int i=0, j=0; i < _fltDenomination.length; i++) {
            denoMap = (HashMap) denominationList.get(j);
            denoAmt =  CommonUtil.convertObjToDouble(denoMap.get("DENOMINATION")).doubleValue();
            denomination = _fltDenomination[i].doubleValue();
            if (denomination == denoAmt && !_strDenominationType[i].equals("COIN")) {
                j++;
                count = CommonUtil.convertObjToInt(denoMap.get("COUNT"));
                txtCount[i].setText(String.valueOf(count));
                
                txtTotal[i].setText(
                    CommonUtil.convertObjToStr(
                        new Double(count * denomination)));
                totalAmount += count * denomination;
                
                if (j == denominationList.size()) break;
            } 
            // If the denomination is paise then divide the denomination by 100
            if (denomination == denoAmt && _strDenominationType[i].equals("COIN") && CommonUtil.convertObjToStr(lblDenomination[i].getText()).startsWith("Ps") && denomination != 1){
                denomination /= 100;
                j++;
                count = CommonUtil.convertObjToInt(denoMap.get("COUNT"));
                txtCount[i].setText(String.valueOf(count));
                
                txtTotal[i].setText(
                    CommonUtil.convertObjToStr(
                        new Double(count * denomination)));
                totalAmount += count * denomination;
                
                if (j == denominationList.size()) break;
            }
        }
        
        txtGrandTotal.setText(CommonUtil.convertObjToStr(new Double(totalAmount)));
    }
    
    
    private void initWithdrawAmount() {
        double count = 0.0, amount = _amount, totalAmount=0.0;
        
        for (int i=0; i < _fltDenomination.length; i++) {
            count = Math.floor(amount / _fltDenomination[i].doubleValue());
            txtCount[i].setText(CommonUtil.convertObjToStr(new Double(count)));
            txtTotal[i].setText(CommonUtil.convertObjToStr(new Double(count * _fltDenomination[i].doubleValue())));
            amount -= (count * _fltDenomination[i].doubleValue());
            totalAmount += count * _fltDenomination[i].doubleValue();
        }
        
        txtGrandTotal.setText(CommonUtil.convertObjToStr(new Double(totalAmount)));
    }

    private void initDenominationScreen () {
        txtCurrencyType.setText(_currencyType);
        txtTransType.setText(_transType);

        HashMap whereMap = new HashMap();
        whereMap.put ("CURRENCY", _currencyType);
        List data = (List) ClientUtil.executeQuery("getDenominations", whereMap);

        final int lstSize = data.size();

        /* The following block added for checking the 
         * entered denomination count with available
         * denomination of current user (Cashier)
         * (Developed by Rajesh)
         * Starts here----------
         */
        whereMap = new HashMap();
        whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
        whereMap.put("CURR_DATE", currDt.clone());
        whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
	final List lst = (List) ClientUtil.executeQuery("getAvailableDenominations", whereMap);
        CashMovementDetailsTO cmdTO = null;
        /* The lst object contains Available Denominations */
        
        java.awt.GridBagConstraints gridBagConstraints;

        lblDenomination = new CLabel[lstSize];
        lblMulti = new CLabel[lstSize];
        txtCount = new CTextField[lstSize];
        lblEqui = new CLabel[lstSize];
        txtTotal = new CTextField[lstSize];
        lblAvailDenomination = new CLabel[lstSize];
        _fltDenomination = new Double[lstSize];
        _strDenominationType = new String[lstSize];
        
        lblGrandTotal = new CLabel();
        txtGrandTotal = new CTextField();

        HashMap mapData;
        
        for (int i=0; i < lstSize; i++) {
            lblDenomination[i] = new CLabel();
            lblMulti[i] = new CLabel();
            txtCount[i] = new CTextField();
            txtCount[i].setValidation(new NumericValidation(8, 0));
            lblEqui[i] = new CLabel();
            txtTotal[i] = new CTextField();
            txtTotal[i].setValidation(new CurrencyValidation(14,2));
            lblAvailDenomination[i] = new CLabel();
            
            mapData = (HashMap) data.get(i);
            _fltDenomination[i] = new Double(CommonUtil.convertObjToStr(mapData.get("DENOMINATION_VALUE")));
            _strDenominationType[i] = CommonUtil.convertObjToStr(mapData.get("DENOMINATION_TYPE"));
            lblDenomination[i].setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            lblDenomination[i].setText(CommonUtil.convertObjToStr(mapData.get("DENOMINATION_NAME")));
            lblDenomination[i].setPreferredSize(new java.awt.Dimension(80, 21));
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

            cmdTO = (CashMovementDetailsTO) lst.get(i);
            String availDenom = CommonUtil.convertObjToStr(cmdTO.getDenominationCount());
            availDenom = availDenom.length()<1 ? "0" : availDenom;
            lblAvailDenomination[i].setText(availDenom);
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 5;
            gridBagConstraints.gridy = i+1;
            gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
            panDenomination.add(lblAvailDenomination[i], gridBagConstraints);
            
            final int j = i;
            txtCount[i].addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusLost(java.awt.event.FocusEvent evt) {
                    double denomination = _fltDenomination[j].doubleValue();
                    // If the denomination count exceeds the available denominations
                    // checking the lst object 
                    CashMovementDetailsTO cm = (CashMovementDetailsTO) lst.get(j);
                    System.out.println("#### CashMovementDetailsTO : "+cm);
                    double cntAvail = CommonUtil.convertObjToDouble(cm.getDenominationCount()).doubleValue();
                    double cntTyped = CommonUtil.convertObjToDouble(txtCount[j].getText()).doubleValue();
                    if (!_transType.equals("Deposit"))
                        if (cntTyped!=0)
                            if (cntTyped > cntAvail) {
                                ClientUtil.displayAlert("Available "+lblDenomination[j].getText()+" Count is "+(int)cntAvail);
                                txtCount[j].requestFocus();
                            }
                    // If the denomination is paise then divide the denomination by 100
                    if (_strDenominationType[j].equals("COIN") && CommonUtil.convertObjToStr(lblDenomination[j].getText()).startsWith("Ps") && denomination != 1){
                        denomination /= 100;
                    }
                    txtTotal[j].setText(
                            CommonUtil.convertObjToStr(new Double(
                            denomination * 
                            new Double(txtCount[j].getText().equals("") ? "0.0" :  txtCount[j].getText()).doubleValue())));

                    double grandTotal=0.0, dblValue=0.0;
                    for (int cnt=0; cnt < lstSize; cnt++) {
                        grandTotal += new Double(txtTotal[cnt].getText().equals("") ? "0.0" :  txtTotal[cnt].getText()).doubleValue();
                    }
                    txtGrandTotal.setText(CommonUtil.convertObjToStr(new Double(grandTotal)));
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
        txtGrandTotal.setValidation(new CurrencyValidation(14,2));
        txtGrandTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = lstSize + 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDenomination.add(txtGrandTotal, gridBagConstraints);
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
        txtCurrencyType = new com.see.truetransact.uicomponent.CTextField();
        lblTransType = new com.see.truetransact.uicomponent.CLabel();
        txtTransType = new com.see.truetransact.uicomponent.CTextField();
        sptLine = new com.see.truetransact.uicomponent.CSeparator();
        panBottom = new com.see.truetransact.uicomponent.CPanel();
        btnOk = new com.see.truetransact.uicomponent.CButton();
        btnCancel = new com.see.truetransact.uicomponent.CButton();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        panDenomination.setLayout(new java.awt.GridBagLayout());

        getContentPane().add(panDenomination, java.awt.BorderLayout.CENTER);

        panTop.setLayout(new java.awt.GridBagLayout());

        lblCurrencyType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCurrencyType.setText("Currency Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTop.add(lblCurrencyType, gridBagConstraints);

        txtCurrencyType.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTop.add(txtCurrencyType, gridBagConstraints);

        lblTransType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTransType.setText("Transaction Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTop.add(lblTransType, gridBagConstraints);

        txtTransType.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTop.add(txtTransType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panTop.add(sptLine, gridBagConstraints);

        getContentPane().add(panTop, java.awt.BorderLayout.NORTH);

        panBottom.setMinimumSize(new java.awt.Dimension(200, 35));
        panBottom.setPreferredSize(new java.awt.Dimension(200, 35));
        btnOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif")));
        btnOk.setText("Ok");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        panBottom.add(btnOk);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif")));
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        panBottom.add(btnCancel);

        getContentPane().add(panBottom, java.awt.BorderLayout.SOUTH);

        pack();
    }//GEN-END:initComponents

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        // Add your handling code here:
        //updateDenominationCount
//        String symbol = "";
//        if (_transType.equalsIgnoreCase("Withdrawal") || _transType.equalsIgnoreCase("Debit")) {
//            symbol = "-";
//        }
        
        if (CommonUtil.convertObjToDouble(txtGrandTotal.getText()).doubleValue() != _amount) {
            COptionPane.showMessageDialog(this, "Input and Denomination Amount are not tallied.");
        } else {
//            HashMap whereMap;
//            String strCnt = "";
//            for (int i=0; i < _fltDenomination.length; i++) {            
//                strCnt = txtCount[i].getText().trim();
//                if (strCnt != null && !strCnt.equals("0.0") && !strCnt.equals("")) {
//                    whereMap = new HashMap();
//                    whereMap.put ("CURRENCY", _currencyType);
//                    whereMap.put ("COUNT", new Double(txtCount[i].getText()));
//                    whereMap.put ("DENOMINATION", _fltDenomination[i]);
//                }
//            }
            this.dispose();
        }        
    }//GEN-LAST:event_btnOkActionPerformed
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
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
        HashMap map = new HashMap();
        map.put("CURRENCY_TYPE", "INR");
        map.put("TRANS_TYPE", "Deposit");
        map.put("AMOUNT", "745");

        DenominationUI dui = new DenominationUI(map);
        dui.show();
    }
    
    /**
     * Getter for property _fltDenomination.
     * @return Value of property _fltDenomination.
     */
    public java.lang.Double[] getFltDenomination() {
        return this._fltDenomination;
    }
    
    /**
     * Setter for property _fltDenomination.
     * @param _fltDenomination New value of property _fltDenomination.
     */
    public void setFltDenomination(java.lang.Double[] _fltDenomination) {
        this._fltDenomination = _fltDenomination;
    }
    
    /**
     * Getter for property txtCount.
     * @return Value of property txtCount.
     */
    public com.see.truetransact.uicomponent.CTextField[] getTxtCount() {
        return this.txtCount;
    }
    
    /**
     * Setter for property txtCount.
     * @param txtCount New value of property txtCount.
     */
    public void setTxtCount(com.see.truetransact.uicomponent.CTextField[] txtCount) {
        this.txtCount = txtCount;
    }
    
    /**
     * Getter for property _strDenominationType.
     * @return Value of property _strDenominationType.
     */
    public java.lang.String[] getDenominationType() {
        return this._strDenominationType;
    }
    
    /**
     * Setter for property _strDenominationType.
     * @param _strDenominationType New value of property _strDenominationType.
     */
    public void setDenominationType(java.lang.String[] _strDenominationType) {
        this._strDenominationType = _strDenominationType;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnOk;
    private com.see.truetransact.uicomponent.CLabel lblCurrencyType;
    private com.see.truetransact.uicomponent.CLabel lblTransType;
    private com.see.truetransact.uicomponent.CPanel panBottom;
    private com.see.truetransact.uicomponent.CPanel panDenomination;
    private com.see.truetransact.uicomponent.CPanel panTop;
    private com.see.truetransact.uicomponent.CSeparator sptLine;
    private com.see.truetransact.uicomponent.CTextField txtCurrencyType;
    private com.see.truetransact.uicomponent.CTextField txtTransType;
    // End of variables declaration//GEN-END:variables
}