/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * CashManagementUI.java
 *
 * Created on January 28, 2005, 1:02 PM
 */

package com.see.truetransact.ui.transaction.cashmanagement;

/**
 *
 * @author  152715
 */


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;
import java.util.List;


import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.PincodeValidation_IN;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.uicomponent.CLabel;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.transferobject.transaction.cashmanagement.CashMovementDetailsTO;
import java.util.Date;
public class CashManagementUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer , UIMandatoryField  {
    
    // variable declarations
    private CashManagementOB observable;
    final CashManagementRB  resourceBundle = new CashManagementRB();
    private HashMap mandatoryMap;
    private int viewType = -1;
    private final int NEW=0, EDIT=1,DELETE=2,AUTHORIZE=3,RECEIVING_ID=4,ISSUE_ID=5, VIEW = 6;
    boolean isFilled = false;
    boolean transTypeSelected = false;
    // variable declaration for DenominationUI
    //    private String _currencyType;
    // CurrencyType hardcoded INR
    private String _transType;
    private double _amount=0.0;
    private Double _fltDenomination[];
    private String _strDenominationType[];
    private CTextField txtTransTotal[];//This is Array of CTextField to display the total of each type of Denomination
    private CLabel lblGrandTotal;//This is used to give  a display of GrandTotal
    private CTextField txtTransGrandTotal;//This is used to display the GrandTotal ie., the consolidated total of all type of Denominations related to TransactionDenominations
    private CTextField txtTransCount[];//An Array of CTextField that enable the user to enter the number of Counts related panTransDenomination
    //of Particular Denomination related to Transaction Denominations
    private CLabel lblTransDenominationType[];
    private CLabel lblTransDenomination[];//This is Array of CLabel that used to display the particular type of Denomination(either '1000' or '100' etc.,) related to TransactionDenominations
    private CLabel lblMulti[];//This is Array of CLabel to display the "*" for all the Denominations Type
    private CLabel lblEqui[];//This is Array of CLabel to display the "=" for all the Denominations Type
    private CLabel lblCashDenominationType[];
    private CLabel lblCashDenomination[];//This is Array of CLabel that used to display the particular type of Denomination(either '1000' or '100' etc.,) related to CashDenomination
    private CTextField txtCashCount[];//An Array of CTextField that enable the user to enter the number of Counts related panTransDenomination
    //of Particular Denomination related to Transaction Denomination
    private CTextField txtCashTotal[];//This is Array of CTextField to display the total of each type of Denomination related to CashDenomination
    private CTextField txtCashGrandTotal;//This is used to display the GrandTotal ie., the consolidated total of all type of Denominations related to panCashDenomination
    private String cashMovementID = "";
    private LinkedHashMap transDetailsHash = null;
    private LinkedHashMap cashDetailsHash = null;
    private ArrayList denomDetailsList = new ArrayList();//This is used to store up the to's while retriving data during Edit operation
    private final String CASHBOXDENOMINATION = "CASH BOX";//this is used to store the type of Denomination used in the Screeen as CashBoxDenomination
    private String TRANSACTIONDENOMINATION = "";//this is used to store type of Denomination as transactionDenomination
    private HashMap transDenomNameMap,cashDenomNameMap; // This is used to trace out the DenominationName's which has been modified while doing changes
    //with denomination count of particular denominatio name in Edit mode to avoid duplication while updation
    public HashMap mapData;
    public HashMap where = new HashMap();
    public HashMap dataMap = new HashMap();
    public final HashMap dataHash = dataMap;
    private Date currDt = null;
    /*cashmanage*/
    /** Creates new form CashManagementUI */
    public CashManagementUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initStartUp();
    }
    private void initStartUp() {
        setFieldNames();
        setObservable();
        internationalize();
        setMandatoryHashMap();
        setMaximumLength();
        setHelpMessage();
        setButtonEnableDisable();
        initTransDenominationScreen();
        initCashDenominationScreen();
        setTransDenominationMaximumLength();
        setCashDenominationMaximumLength();
         new MandatoryCheck().putMandatoryMarks(getClass().getName(),panDetails);
        enableDisablePanCashMovement(false);
        enableDisablePanDenomination(false);
        enableDisableHelpButtons(false);
        disableCashierIDs(false);
        observable.resetForm();
        observable.resetStatus();
    }
    private void disableCashierIDs(boolean flag) {
        txtReceivingCashierID.setEditable(flag);
        txtIssueCashierID.setEditable(flag);
    }
    // To set The Value of the Buttons Depending on the Value or Condition...
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
    
    private void initTransDenominationScreen() {
         
        HashMap whereMap = new HashMap();
        whereMap.put("CURRENCY", "INR");// currency is hardcoded as INR
        java.util.List data = (java.util.List) ClientUtil.executeQuery("getDenominations", whereMap);
        
        final int lstSize = data.size();
        
        java.awt.GridBagConstraints gridBagConstraints;
        lblTransDenominationType = new CLabel[lstSize];
        lblTransDenomination = new CLabel[lstSize];
        lblMulti = new CLabel[lstSize];
        txtTransCount = new CTextField[lstSize];
        lblEqui = new CLabel[lstSize];
        txtTransTotal = new CTextField[lstSize];
        _fltDenomination = new Double[lstSize];
        _strDenominationType = new String[lstSize];
        
        lblGrandTotal = new CLabel();
        txtTransGrandTotal = new CTextField();
      
        for (int i=0; i < lstSize; i++) {
            lblTransDenomination[i] = new CLabel();
            lblMulti[i] = new CLabel();
            txtTransCount[i] = new CTextField();
            lblEqui[i] = new CLabel();
            txtTransTotal[i] = new CTextField();
            lblTransDenominationType[i]=new CLabel();
            mapData = (HashMap) data.get(i);
            _fltDenomination[i] = new Double(mapData.get("DENOMINATION_VALUE").toString());
            _strDenominationType[i] = CommonUtil.convertObjToStr(mapData.get("DENOMINATION_TYPE"));
            
            lblTransDenominationType[i].setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            lblTransDenominationType[i].setText(CommonUtil.convertObjToStr(mapData.get("DENOMINATION_TYPE")));
            lblTransDenominationType[i].setPreferredSize(new java.awt.Dimension(50, 21));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = i+1;
            gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
            panTransDenomination.add(lblTransDenominationType[i], gridBagConstraints);
            
            if(i==0){
                lblTransDenominationType[i].setVisible(true);
            }else{
                if(lblTransDenominationType[i].getText().equals(lblTransDenominationType[i-1].getText())) {
                    lblTransDenominationType[i].setVisible(false);
                    
                }else{
                    lblTransDenominationType[i].setVisible(true);
                }
            }
            
            lblTransDenomination[i].setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            lblTransDenomination[i].setText(CommonUtil.convertObjToStr(mapData.get("DENOMINATION_NAME")));
            lblTransDenomination[i].setPreferredSize(new java.awt.Dimension(140, 21));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = i+1;
            gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
            panTransDenomination.add(lblTransDenomination[i], gridBagConstraints);
            
            lblMulti[i].setText("x");
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 2;
            gridBagConstraints.gridy = i+1;
            gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
            panTransDenomination.add(lblMulti[i], gridBagConstraints);
            
            txtTransCount[i].setHorizontalAlignment(javax.swing.JTextField.RIGHT);
            txtTransCount[i].setPreferredSize(new java.awt.Dimension(50, 21));
            txtTransCount[i].setAllowNumber(true);
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 3;
            gridBagConstraints.gridy = i+1;
            gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
            panTransDenomination.add(txtTransCount[i], gridBagConstraints);
            
            lblEqui[i].setText("=");
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 4;
            gridBagConstraints.gridy = i+1;
            gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
            panTransDenomination.add(lblEqui[i], gridBagConstraints);
            
            txtTransTotal[i].setEditable(false);
            txtTransTotal[i].setHorizontalAlignment(javax.swing.JTextField.RIGHT);
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 5;
            gridBagConstraints.gridy = i+1;
            gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
            panTransDenomination.add(txtTransTotal[i], gridBagConstraints);
            
            final int j = i;
            txtTransCount[i].addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusLost(java.awt.event.FocusEvent evt) {
                    HashMap denomHashmap = new HashMap();
                    HashMap hash = new HashMap();
                   // int k = 0;
                    double denomination = _fltDenomination[j].doubleValue();
                    
                    
                if(rdoTranscationType_Receipt.isSelected())
                   if(!( txtTransCount[j].getText().equals(null)))
                      if(!( txtTransCount[j].getText().equals("")))
                                if(chkVaultCash.isSelected()) {
                        HashMap mapData;
                        HashMap where = new HashMap();
                        where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                                    where.put("SOURCE","VAULTREC" );
                                    where.put("DENOMINATION_TYPE",lblTransDenominationType[j].getText());
                                    where.put("DENOMINATION_NAME",lblTransDenomination[j].getText());
                        java.util.List lst = ClientUtil.executeQuery("getAvailableForexDeno",where);
                        where = null;
                                    if(lst!=null && lst.size()>0){
                                        where=(HashMap)lst.get(0);
                                        double opCount=CommonUtil.convertObjToDouble(txtTransCount[j].getText()).doubleValue();
                                        double count = CommonUtil.convertObjToDouble(where.get("OPENING_COUNT")).doubleValue();
                                        if(opCount>count){
                            displayAlert("Available "+lblTransDenomination[j].getText()+" Count is : "+count);
                            txtTransCount[j].setText("");
                                            return;
                                        }
                                    }else{
                                        displayAlert("Available "+lblTransDenomination[j].getText()+" Count is : 0");
                                        txtTransCount[j].setText("");
                                        return;
                                    }
                                    if(txtReceivingCashierID.getText().equals("")){
                                        ClientUtil.displayAlert("Please select Reciever Cashier Id");
                                        txtTransCount[j].setText("");
                                        return;
                                    }
                                }else{
                                    ClientUtil.displayAlert("Please select vaultCash");
                                     txtTransCount[j].setText("");
                                    return;
                                }
//                    if(!chkVaultCash.isSelected())
//                        if(!txtReceivingCashierID.getText().equals("")) {
//                            if(!txtIssueCashierID.getText().equals("")){
//                                if(!( txtCashCount[j].getText().equals(null)))
//                                    if(!( txtCashCount[j].getText().equals(""))){
//                                        ClientUtil.displayAlert("CashBox Denomination not allowed");
//                                        txtTransCount[j].setText("");
//                                        return;
//                                    }
//                            }else{
//                                ClientUtil.displayAlert("Please select Issuer Id");
//                                txtTransCount[j].setText("");
//                                return;
//                            }
//                        }else{
//                            ClientUtil.displayAlert("Please select Received Id");
//                            txtTransCount[j].setText("");
//                            return;
//                        }
                    
                    if(chkVaultCash.isSelected()) {
                        if( !rdoTranscationType_Receipt.isSelected() && !rdoTranscationType_Payment.isSelected()){
                            ClientUtil.displayAlert("Please selectTransaction Type");
                            txtTransCount[j].setText("");
                            return;
                        }
                    }
                    
                    
                     if(rdoTranscationType_Payment.isSelected())
                     if(!( txtTransCount[j].getText().equals(null)))
                        if(!( txtTransCount[j].getText().equals("")))
                                if(chkVaultCash.isSelected()) {
                                     if(txtIssueCashierID.getText().equals("")){
                                        ClientUtil.displayAlert("Please select Issuer Cashier Id");
                            txtTransCount[j].setText("");
                                        return;
                                    }
                                }else{
                                     ClientUtil.displayAlert("Please select vaultCash");
                                     txtTransCount[j].setText("");
                                    return; 
                                }
                    
                    
                    //                    if(!chkVaultCash.isSelected())
                    //                        if(!( txtTransCount[j].getText().equals(null)))
                    //                            if(!( txtTransCount[j].getText().equals("")))
                    //                                if(rdoTranscationType_Payment.isSelected()){
                    //                                    HashMap denoMap = new HashMap();
                    //                                    HashMap whereMap = new HashMap();
                    //                                    HashMap denoDataMap = new HashMap();
                    //                                    whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                    //                                    whereMap.put("CURR_DATE", currDt);
                    //                                    whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                    //                                    whereMap.put("INITIATED_BRANCH",TrueTransactMain.BRANCH_ID);
                    //                                    where.put("SOURCE","VAULTREC" );
                    //                                    java.util.List lstData = ClientUtil.executeQuery("getAvailableDenominations", whereMap);
                    //                                    System.out.println("@@@@2@@lst:lst"+lstData);
                    //                                    whereMap = null;
                    //                                    CashMovementDetailsTO objCashMovementDetailsTO;
                    //                                    if (lstData.size()>0) {
                    //                                        for (int b=0; b<lstData.size(); b++) {
                    //                                            objCashMovementDetailsTO = (CashMovementDetailsTO) lstData.get(b);
                    //                                            denoDataMap.put(objCashMovementDetailsTO.getDenominationName(), objCashMovementDetailsTO.getDenominationCount());
                    //                                        }
                    //                                        HashMap hashData = denoDataMap;
                    //                                        hash.put("DENOMINATION_COUNT", txtTransCount[j].getText());
                    //                                        String opCount = (String) hash.get("DENOMINATION_COUNT");
                    //                                        double opCounts = Double.parseDouble(opCount);
                    //                                        if (hashData.size()>0) {
                    //                                            double count = CommonUtil.convertObjToDouble(hashData.get(lblTransDenomination[j].getText())).doubleValue();
                    //                                            if(opCounts > count){
                    //                                                displayAlert("Available "+lblTransDenomination[j].getText()+" Count is : "+count);
                    //                                                txtTransCount[j].setText("");
                    //                                            }
                    //                                        }
                    //                                    }
                    //
                    //                                }
                    // If the denomination is paise then divide the denomination by 100
                    if (_strDenominationType[j].equals("COIN") && CommonUtil.convertObjToStr(lblTransDenomination[j].getText()).startsWith("Ps") && denomination != 1){
                        denomination /= 100;
                    }
                    txtTransTotal[j].setText(
                    CommonUtil.convertObjToStr(new Double(
                    denomination *
                    new Double(txtTransCount[j].getText().equals("") ? "0.0" :  txtTransCount[j].getText()).doubleValue())));
                    
                    double grandTotal=0.0, dblValue=0.0;
                    for (int cnt=0; cnt < lstSize; cnt++) {
                        grandTotal += new Double(txtTransTotal[cnt].getText().equals("") ? "0.0" :  txtTransTotal[cnt].getText()).doubleValue();
                    }
                    txtTransGrandTotal.setText(CommonUtil.convertObjToStr(new Double(grandTotal)));
                    txtTransBalance.setText(CommonUtil.convertObjToStr(new Double(grandTotal)));
                    
                
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
        panTransDenomination.add(lblGrandTotal, gridBagConstraints);
        
        txtTransGrandTotal.setEditable(false);
        txtTransGrandTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = lstSize + 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransDenomination.add(txtTransGrandTotal, gridBagConstraints);
        
    }
    
    private void initCashDenominationScreen() {
        
        HashMap whereMap = new HashMap();
        whereMap.put("CURRENCY", "INR");// currency is hardcoded as INR
        java.util.List data = (java.util.List) ClientUtil.executeQuery("getDenominations", whereMap);
        
        final int lstSize = data.size();
        
        java.awt.GridBagConstraints gridBagConstraints;
        lblCashDenominationType=new CLabel[lstSize];
        lblCashDenomination = new CLabel[lstSize];
        lblMulti = new CLabel[lstSize];
        txtCashCount = new CTextField[lstSize];
        lblEqui = new CLabel[lstSize];
        txtCashTotal = new CTextField[lstSize];
        _fltDenomination = new Double[lstSize];
        _strDenominationType = new String[lstSize];
        
        lblGrandTotal = new CLabel();
        txtCashGrandTotal = new CTextField();
        
        HashMap mapData;
         
        for (int i=0; i < lstSize; i++) {
            lblCashDenomination[i] = new CLabel();
            lblMulti[i] = new CLabel();
            txtCashCount[i] = new CTextField();
            lblEqui[i] = new CLabel();
            txtCashTotal[i] = new CTextField();
            lblCashDenominationType[i]=new CLabel();
            mapData = (HashMap) data.get(i);
            _fltDenomination[i] = new Double(mapData.get("DENOMINATION_VALUE").toString());
            _strDenominationType[i] = CommonUtil.convertObjToStr(mapData.get("DENOMINATION_TYPE"));
            
            lblCashDenominationType[i].setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            lblCashDenominationType[i].setText(CommonUtil.convertObjToStr(mapData.get("DENOMINATION_TYPE")) );
            lblCashDenominationType[i].setPreferredSize(new java.awt.Dimension(50, 21));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = i+1;
            gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
            panCashDenomination.add(lblCashDenominationType[i], gridBagConstraints);
            
            if(i==0){
                lblCashDenominationType[i].setVisible(true);
            }else{
                if(lblCashDenominationType[i].getText().equals(lblCashDenominationType[i-1].getText())) {
                    lblCashDenominationType[i].setVisible(false);
                    
                }else{
                    lblCashDenominationType[i].setVisible(true);
                }
            }
            
            lblCashDenomination[i].setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            lblCashDenomination[i].setText(CommonUtil.convertObjToStr(mapData.get("DENOMINATION_NAME")));
            lblCashDenomination[i].setPreferredSize(new java.awt.Dimension(140, 21));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = i+1;
            gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
            panCashDenomination.add(lblCashDenomination[i], gridBagConstraints);
            
            lblMulti[i].setText("x");
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 2;
            gridBagConstraints.gridy = i+1;
            gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
            panCashDenomination.add(lblMulti[i], gridBagConstraints);
            
            txtCashCount[i].setHorizontalAlignment(javax.swing.JTextField.RIGHT);
            txtCashCount[i].setPreferredSize(new java.awt.Dimension(50, 21));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 3;
            gridBagConstraints.gridy = i+1;
            gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
            panCashDenomination.add(txtCashCount[i], gridBagConstraints);
            
            lblEqui[i].setText("=");
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 4;
            gridBagConstraints.gridy = i+1;
            gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
            panCashDenomination.add(lblEqui[i], gridBagConstraints);
            
            txtCashTotal[i].setEditable(false);
            txtCashTotal[i].setHorizontalAlignment(javax.swing.JTextField.RIGHT);
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 5;
            gridBagConstraints.gridy = i+1;
            gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
            panCashDenomination.add(txtCashTotal[i], gridBagConstraints);
            final int j = i;
            txtCashCount[i].addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusLost(java.awt.event.FocusEvent evt) {
                    double denomination = _fltDenomination[j].doubleValue();
                    
                    HashMap denomHashmap = new HashMap();
                    HashMap hash = new HashMap();
                    
                    if(rdoTranscationType_Receipt.isSelected())
                        if(!( txtCashCount[j].getText().equals(null)))
                            if(!( txtCashCount[j].getText().equals("")))
                                if(chkVaultCash.isSelected()) {
                                    HashMap mapData;
                                    HashMap where = new HashMap();
                                    where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                                    where.put("SOURCE","CASH BOX" );
                                    where.put("DENOMINATION_TYPE",lblCashDenominationType[j].getText());
                                    where.put("DENOMINATION_NAME",lblCashDenomination[j].getText());
                                    java.util.List lst = ClientUtil.executeQuery("getAvailableForexDeno",where);
                                    
                                    where = null;
                                    if(lst!=null && lst.size()>0){
                                        where=(HashMap)lst.get(0);
                                        double opCount=CommonUtil.convertObjToDouble(txtCashCount[j].getText()).doubleValue();
                                        double count = CommonUtil.convertObjToDouble(where.get("OPENING_COUNT")).doubleValue();
                                        if(opCount>count){
                                            displayAlert("Available "+lblCashDenomination[j].getText()+" Count is :"+count);
                                            txtCashCount[j].setText("");
                                            return;
                                        }
                                    }else{
                                        displayAlert("Available "+lblCashDenomination[j].getText()+" Count is : 0");
                                        txtCashCount[j].setText("");
                                        return;
                                    }
                                    if(txtReceivingCashierID.getText().equals("")){
                                        ClientUtil.displayAlert("Please select Reciever Cashier Id");
                                        txtCashCount[j].setText("");
                                        return;
                                    }
                                }else{
                                    ClientUtil.displayAlert("Please select vaultCash");
                                     txtCashCount[j].setText("");
                                    return;
                                }
                    
                    
                    if(!chkVaultCash.isSelected())
                        if(!txtReceivingCashierID.getText().equals("")) {
                            if(!txtIssueCashierID.getText().equals("")){
                                if(!( txtCashCount[j].getText().equals(null)))
                                    if(!( txtCashCount[j].getText().equals(""))){
                                        ClientUtil.displayAlert("CashBox Denomination not allowed");
                                        txtCashCount[j].setText("");
                                        return;
                                    }
                            }else{
                                ClientUtil.displayAlert("Please select Issuer Id");
                                txtCashCount[j].setText("");
                                return;
                            }
                        }else{
                            ClientUtil.displayAlert("Please select Received Id");
                            txtCashCount[j].setText("");
                            return;
                        }
                    if(chkVaultCash.isSelected()) {
                        if( !rdoTranscationType_Receipt.isSelected() && !rdoTranscationType_Payment.isSelected()){
                            ClientUtil.displayAlert("Please selectTransaction Type");
                            txtCashCount[j].setText("");
                            return;
                        }
                    }
                    
                    
                     if(rdoTranscationType_Payment.isSelected())
                        if(!( txtCashCount[j].getText().equals(null)))
                            if(!( txtCashCount[j].getText().equals("")))
                                if(chkVaultCash.isSelected()) {
                                     if(txtIssueCashierID.getText().equals("")){
                                        ClientUtil.displayAlert("Please select Issuer Cashier Id");
                                        txtCashCount[j].setText("");
                                        return;
                                    }
                                }else{
                                     ClientUtil.displayAlert("Please select vaultCash");
                                      txtCashCount[j].setText("");
                                    return; 
                                }
                    
                    
                    
                    //                                if(rdoTranscationType_Payment.isSelected()){
                    //                                    HashMap denoMap = new HashMap();
                    //                                    HashMap whereMap = new HashMap();
                    //                                    HashMap denoDataMap = new HashMap();
                    //                                    whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                    //                                    whereMap.put("CURR_DATE", currDt);
                    //                                    whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                    //                                    whereMap.put("INITIATED_BRANCH",TrueTransactMain.BRANCH_ID);
                    //                                    where.put("SOURCE","CASH BOX" );
                    //                                    java.util.List lstData = ClientUtil.executeQuery("getAvailableDenominations", whereMap);
                    //                                    System.out.println("@@@@2@@lst:lst"+lstData);
                    //                                    whereMap = null;
                    //                                    CashMovementDetailsTO objCashMovementDetailsTO;
                    //                                    if (lstData.size()>0) {
                    //                                        for (int b=0; b<lstData.size(); b++) {
                    //                                            objCashMovementDetailsTO = (CashMovementDetailsTO) lstData.get(b);
                    //                                            denoDataMap.put(objCashMovementDetailsTO.getDenominationName(), objCashMovementDetailsTO.getDenominationCount());
                    //                                        }
                    //                                        HashMap hashData = denoDataMap;
                    //                                        hash.put("DENOMINATION_COUNT", txtCashCount[j].getText());
                    //                                        String opCount = (String) hash.get("DENOMINATION_COUNT");
                    //                                        double opCounts = Double.parseDouble(opCount);
                    //                                        if (hashData.size()>0) {
                    //                                            double count = CommonUtil.convertObjToDouble(hashData.get(lblTransDenomination[j].getText())).doubleValue();
                    //                                            if(opCounts > count){
                    //                                                displayAlert("Available "+lblTransDenomination[j].getText()+" Count is : "+count);
                    //                                                txtCashCount[j].setText("");
                    //                                            }
                    //                                        }
                    //                                    }
                    //
                    //                                }
                    
                    
                    
                    
                    // If the denomination is paise then divide the denomination by 100
                    if (_strDenominationType[j].equals("COIN") && CommonUtil.convertObjToStr(lblCashDenomination[j].getText()).startsWith("Ps") && denomination != 1){
                        denomination /= 100;
                    }
                 
                    txtCashTotal[j].setText(
                    CommonUtil.convertObjToStr(new Double(
                    denomination *
                    new Double(txtCashCount[j].getText().equals("") ? "0.0" :  txtCashCount[j].getText()).doubleValue())));
                    
                    double grandTotal=0.0, dblValue=0.0;
                    for (int cnt=0; cnt < lstSize; cnt++) {
                        grandTotal += new Double(txtCashTotal[cnt].getText().equals("") ? "0.0" :  txtCashTotal[cnt].getText()).doubleValue();
                    }
                    txtCashGrandTotal.setText(CommonUtil.convertObjToStr(new Double(grandTotal)));
                    txtCashBoxBalance.setText(CommonUtil.convertObjToStr(new Double(grandTotal)));
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
        panCashDenomination.add(lblGrandTotal, gridBagConstraints);
        
        txtCashGrandTotal.setEditable(false);
        txtCashGrandTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = lstSize + 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCashDenomination.add(txtCashGrandTotal, gridBagConstraints);
     
   
}
    
    /**
     * check whether TransDenomination values is entered or not
     */
    private boolean checkMandatoryForTransDenomination() {
        String strCnt = "";boolean isNull = true;
        
        for (int i=0; i < _fltDenomination.length; i++) {
            strCnt = txtTransCount[i].getText().trim();
           
            if (strCnt != null && !strCnt.equals("0.0") && !strCnt.equals("")) {
                isNull = false;
            }
        }
//        if (isNull) {
//            displayAlert((String)resourceBundle.getString("warningDenomination"));
//        }
        return isNull;
    }
    
    /**
     * check whether CashBox denomination values is entered or not
     */
    private boolean checkMandatoryForCashDenomination() {
        String strCnt = "";boolean isNull = true;
         
        for (int i=0; i < _fltDenomination.length; i++) {
            strCnt = txtCashCount[i].getText().trim();
            if (strCnt != null && !strCnt.equals("0.0") && !strCnt.equals("")) {
                isNull = false;
            }
        }
        if (isNull) {
            displayAlert((String)resourceBundle.getString("warningDenomination"));
        }
        return isNull;
    }
    
    /**
     * To store the denominations
     */
    private LinkedHashMap getTransDenominationValues() {
        if (transDetailsHash == null)
            transDetailsHash = new LinkedHashMap();
        String strCnt = "";
        if(txtTransBalance.getText().length()>0)
        for (int i=0; i < _fltDenomination.length; i++) {
            strCnt = txtTransCount[i].getText().trim();
            if (strCnt != null && !strCnt.equals("0.0") && !strCnt.equals("")) {
                ArrayList denominations = new ArrayList();
                    denominations.add(lblTransDenominationType[i].getText());
                denominations.add(lblTransDenomination[i].getText());
                denominations.add(txtTransCount[i].getText());
                denominations.add(txtTransTotal[i].getText());
                if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){
                    denominations.add(CommonConstants.STATUS_CREATED);
                }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                        if (transDenomNameMap.containsKey(lblTransDenomination[i].getText()+lblTransDenominationType[i].getText())) {
                        denominations.add(CommonConstants.STATUS_MODIFIED);
                    } else {
                        denominations.add(CommonConstants.STATUS_CREATED);
                    }
                    }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE){
                        denominations.add(CommonConstants.STATUS_DELETED);
                }
                //                if (transDetailsHash.containsKey(lblTransDenomination[i].getText())) {
                //                    denominations.add(CommonConstants.STATUS_MODIFIED);
                //                } else {
                //                    denominations.add(CommonConstants.STATUS_CREATED);
                //                }
                denominations.add(TRANSACTIONDENOMINATION);
                    String lbli=CommonUtil.convertObjToStr(new Integer(i));
                    transDetailsHash.put(lblTransDenomination[i].getText()+lblTransDenominationType[i].getText(), denominations);
                    //                    transDetailsHash.put(lbli, denominations);
                denominations = null;
            } else {
                if(transDenomNameMap!=null){
                        if (transDenomNameMap.containsKey(lblTransDenomination[i].getText()+lblTransDenominationType[i].getText())) {
                        ArrayList denominations = new ArrayList();
                            denominations.add(lblTransDenominationType[i].getText());
                        denominations.add(lblTransDenomination[i].getText());
                        denominations.add(txtTransCount[i].getText());
                        denominations.add(txtTransTotal[i].getText());
                        denominations.add(CommonConstants.STATUS_DELETED);
                        denominations.add(TRANSACTIONDENOMINATION);
                            transDetailsHash.put(lblTransDenomination[i].getText()+lblTransDenominationType[i].getText(), denominations);
                        denominations = null;
                    }
                }
            }
        }
        return transDetailsHash;
    }
    
    /**
     * To store the denominations
     */
    private LinkedHashMap getCashDenominationValues() {
        if (cashDetailsHash == null)
            cashDetailsHash = new LinkedHashMap();
      
        String strCnt = "";
        for (int i=0; i < _fltDenomination.length; i++) {
            strCnt = txtCashCount[i].getText().trim();
            if (strCnt != null && !strCnt.equals("0.0") && !strCnt.equals("")) {
                ArrayList denominations = new ArrayList();
                denominations.add(lblCashDenominationType[i].getText());
                denominations.add(lblCashDenomination[i].getText());
                denominations.add(txtCashCount[i].getText());
                denominations.add(txtCashTotal[i].getText());
                if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){
                    denominations.add(CommonConstants.STATUS_CREATED);
                }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                    if (cashDenomNameMap.containsKey(lblCashDenomination[i].getText()+lblCashDenominationType[i].getText())) {
                        denominations.add(CommonConstants.STATUS_MODIFIED);
                    } else {
                        denominations.add(CommonConstants.STATUS_CREATED);
                    }
                }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE){
                    denominations.add(CommonConstants.STATUS_DELETED);
                }
                //                if (cashDetailsHash.containsKey(lblCashDenomination[i].getText())) {
                //                    denominations.add(CommonConstants.STATUS_MODIFIED);
                //                } else {
                //                    denominations.add(CommonConstants.STATUS_CREATED);
                //                }
                denominations.add(CASHBOXDENOMINATION);
                //                String lbli=CommonUtil.convertObjToStr(new Integer(i));
                cashDetailsHash.put(lblCashDenomination[i].getText()+lblCashDenominationType[i].getText(), denominations);
                //                cashDetailsHash.put(lbli, denominations);
                denominations = null;
            } else {
                if(cashDenomNameMap!=null){
                    if (cashDenomNameMap.containsKey(lblCashDenomination[i].getText()+lblCashDenominationType[i].getText())) {
                        ArrayList denominations = new ArrayList();
                        denominations.add(lblCashDenominationType[i].getText());
                        denominations.add(lblCashDenomination[i].getText());
                        denominations.add(txtCashCount[i].getText());
                        denominations.add(txtCashTotal[i].getText());
                        denominations.add(CommonConstants.STATUS_DELETED);
                        denominations.add(CASHBOXDENOMINATION);
                        cashDetailsHash.put(lblCashDenomination[i].getText()+lblCashDenominationType[i].getText(), denominations);
                        denominations = null;
                    }
                }
            }
        }
             
        return cashDetailsHash;
    }
    
    /* To set Maximum length and for validation */
    private void setMaximumLength() {
        txtReceivingCashierID.setMaxLength(32);
        txtIssueCashierID.setMaxLength(32);
        txtCashBoxBalance.setMaxLength(16);
        txtCashBoxBalance.setValidation(new CurrencyValidation());
        txtTransBalance.setMaxLength(16);
        txtTransBalance.setValidation(new CurrencyValidation());
    }
    /* To set Maximum length and for validation */
    private void setTransDenominationMaximumLength() {
        for (int i=0; i < _fltDenomination.length; i++) {
            txtTransCount[i].setValidation(new NumericValidation(8, 0));
            txtTransTotal[i].setValidation(new CurrencyValidation(14,2));
        }
        txtTransGrandTotal.setValidation(new CurrencyValidation(14,2));
    }
    
    /* To set Maximum length and for validation */
    private void setCashDenominationMaximumLength() {
        for (int i=0; i < _fltDenomination.length; i++) {
            txtCashCount[i].setValidation(new NumericValidation(8, 0));
            txtCashTotal[i].setValidation(new CurrencyValidation(14,2));
        }
        txtCashGrandTotal.setValidation(new CurrencyValidation(14,2));
    }
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnIssueCashierID.setName("btnIssueCashierID");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReceivingCashierID.setName("btnReceivingCashierID");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        lblCashierName.setName("lblCashierName");
        lblCashBoxBalance.setName("lblCashBoxBalance");
        lblDate.setName("lblDate");
        lblDisplayCashierName.setName("lblDisplayCashierName");
        lblDisplayDate.setName("lblDisplayDate");
        lblDisplayIssueCashierName.setName("lblDisplayIssueCashierName");
        lblIssueCashierID.setName("lblIssueCashierID");
        lblIssueCashierName.setName("lblIssueCashierName");
        lblMsg.setName("lblMsg");
        lblReceivingCashierId.setName("lblReceivingCashierId");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace5.setName("lblSpace5");
        lblStatus.setName("lblStatus");
        lblTransactionType.setName("lblTransactionType");
        //        lblVaultCash.setName("lblVaultCash");
        mbrCashManagement.setName("mbrCashManagement");
        panCashManagement.setName("panCashManagement");
        panCashierID.setName("panCashierID");
        panTransDenomination.setName("panTransDenomination");
        panCashDenomination.setName("panCashDenomination");
        panDetails.setName("panDetails");
        panIssueCashierID.setName("panIssueCashierID");
        panStatus.setName("panStatus");
        panTransType.setName("panTransType");
        //        panVaultCash.setName("panVaultCash");
        rdoTranscationType_Payment.setName("rdoTranscationType_Payment");
        rdoTranscationType_Receipt.setName("rdoTranscationType_Receipt");
        //        rdoVaultCash_No.setName("rdoVaultCash_No");
        //        rdoVaultCash_Yes.setName("rdoVaultCash_Yes");
        txtIssueCashierID.setName("txtIssueCashierID");
        txtReceivingCashierID.setName("txtReceivingCashierID");
        txtCashBoxBalance.setName("txtCashBoxBalance");
        txtTransBalance.setName("txtTransBalance");
        lblTransBalance.setName("lblTransBalance");
    }
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        lblReceivingCashierId.setText(resourceBundle.getString("lblReceivingCashierId"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblTransactionType.setText(resourceBundle.getString("lblTransactionType"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        //        rdoVaultCash_No.setText(resourceBundle.getString("rdoVaultCash_No"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblCashierName.setText(resourceBundle.getString("lblCashierName"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblDisplayIssueCashierName.setText(resourceBundle.getString("lblDisplayIssueCashierName"));
        btnIssueCashierID.setText(resourceBundle.getString("btnIssueCashierID"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        rdoTranscationType_Receipt.setText(resourceBundle.getString("rdoTranscationType_Receipt"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        //        lblVaultCash.setText(resourceBundle.getString("lblVaultCash"));
        lblDate.setText(resourceBundle.getString("lblDate"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblDisplayDate.setText(resourceBundle.getString("lblDisplayDate"));
        lblIssueCashierID.setText(resourceBundle.getString("lblIssueCashierID"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblDisplayCashierName.setText(resourceBundle.getString("lblDisplayCashierName"));
        rdoTranscationType_Payment.setText(resourceBundle.getString("rdoTranscationType_Payment"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        btnReceivingCashierID.setText(resourceBundle.getString("btnReceivingCashierID"));
        lblIssueCashierName.setText(resourceBundle.getString("lblIssueCashierName"));
        lblCashBoxBalance.setText(resourceBundle.getString("lblCashBoxBalance"));
        //        rdoVaultCash_Yes.setText(resourceBundle.getString("rdoVaultCash_Yes"));
        lblTransBalance.setText(resourceBundle.getString("lblTransBalance"));
    }
    
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        removeRadioButtons();
        chkVaultCash.setSelected(observable.getRdoVaultCash_Yes());
        //        rdoVaultCash_No.setSelected(observable.getRdoVaultCash_No());
        txtReceivingCashierID.setText(observable.getTxtReceivingCashierID());
        txtIssueCashierID.setText(observable.getTxtIssueCashierID());
        txtCashBoxBalance.setText(observable.getTxtCashBoxBalance());
        if(observable.getRdoVaultCash_Yes() && observable.getRdoTranscationType_Receipt()){
            rdoTranscationType_Receipt.setVisible(true);
            rdoTranscationType_Payment.setVisible(true);
            txtIssueCashierID.setVisible(false);
            panIssueCashierID.setVisible(false);
            lblDisplayIssueCashierName.setVisible(false);
            lblIssueCashierName.setVisible(false);
            lblIssueCashierID.setVisible(false);
            
        }if(observable.getRdoVaultCash_Yes() && observable.getRdoTranscationType_Payment()){
            rdoTranscationType_Receipt.setVisible(true);
            rdoTranscationType_Payment.setVisible(true);
            txtReceivingCashierID.setVisible(false);
            panCashierID.setVisible(false);
            lblDisplayCashierName.setVisible(false);
            lblReceivingCashierId.setVisible(false);
            lblCashierName.setVisible(false);
        }
        rdoTranscationType_Receipt.setSelected(observable.getRdoTranscationType_Receipt());
        rdoTranscationType_Payment.setSelected(observable.getRdoTranscationType_Payment());
        lblStatus.setText(observable.getLblStatus());
        lblDisplayCashierName.setText(observable.getLblDisplayCashierName());
        lblDisplayIssueCashierName.setText(observable.getLblIssueCashierName());
        lblDisplayDate.setText(observable.getCashDt());
        addRadioButtons();
    }
    /* To remove the radio buttons */
    private void removeRadioButtons(){
        //        rdgVaultCash.remove(rdoVaultCash_Yes);
        //        rdgVaultCash.remove(rdoVaultCash_No);
        
        rdgTransactionType.remove(rdoTranscationType_Payment);
        rdgTransactionType.remove(rdoTranscationType_Receipt);
    }
    
    /* To add the radio buttons */
    private void addRadioButtons(){
        rdgVaultCash = new CButtonGroup();
        //        rdgVaultCash.add(rdoVaultCash_Yes);
        //        rdgVaultCash.add(rdoVaultCash_No);
        
        rdgTransactionType = new CButtonGroup();
        rdgTransactionType.add(rdoTranscationType_Payment);
        rdgTransactionType.add(rdoTranscationType_Receipt);
    }
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        if(chkVaultCash.isSelected()){
            observable.setRdoVaultCash_Yes(true);
        }
        else{
            observable.setRdoVaultCash_No(true);
        }
        //
        observable.setTxtReceivingCashierID(txtReceivingCashierID.getText());
        observable.setTxtIssueCashierID(txtIssueCashierID.getText());
        observable.setRdoTranscationType_Receipt(rdoTranscationType_Receipt.isSelected());
        observable.setRdoTranscationType_Payment(rdoTranscationType_Payment.isSelected());
        observable.setTxtCashBoxBalance(txtCashBoxBalance.getText());
        observable.setLblDisplayCashierName(lblDisplayCashierName.getText());
        observable.setCashDt(lblDisplayDate.getText());
        observable.setLblIssueCashierName(lblDisplayIssueCashierName.getText());
       
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("rdoVaultCash_Yes", new Boolean(true));
        mandatoryMap.put("txtReceivingCashierID", new Boolean(true));
        mandatoryMap.put("txtIssueCashierID", new Boolean(true));
        mandatoryMap.put("txtCashBoxBalance", new Boolean(true));
        mandatoryMap.put("txtTransBalance", new Boolean(true));
        mandatoryMap.put("rdoTranscationType_Receipt", new Boolean(true));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    
    
/* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        final CashManagementMRB objMandatoryRB = new CashManagementMRB();
        //        rdoVaultCash_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoVaultCash_Yes"));
        txtReceivingCashierID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtReceivingCashierID"));
        txtIssueCashierID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtIssueCashierID"));
        txtCashBoxBalance.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCashBoxBalance"));
        txtTransBalance.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTransBalance"));
        rdoTranscationType_Receipt.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoTranscationType_Receipt"));
    }
    
    
    public void authorizeStatus(String authorizeStatus) {
        try{
            if (!isFilled){
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "viewAuthorizeCashMovement");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeCashMovement");
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
                
            observable.setStatus();
            authorizeUI.show();
            btnSave.setEnabled(false);
            isFilled = true;
        } else if (viewType == AUTHORIZE && isFilled){
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("USER_ID", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("CASH_MOVEMENT_ID", cashMovementID);
            singleAuthorizeMap.put("AUTHORIZEDT",  currDt);
                if(observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE && observable.getRdoVaultCash_Yes())
                    observable.doActionPerformAuthorize();
            ClientUtil.execute("authorizeCashMovement", singleAuthorizeMap);
                ClientUtil.execute("AuthorizeCashMovementDetails", singleAuthorizeMap);
            viewType = -1;
             super.setOpenForEditBy(observable.getStatusBy());
             super.removeEditLock(cashMovementID);
             btnCancelActionPerformed(null);
            observable.setResult(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setResultStatus();
//            observable.resetStatus();
            isFilled = false;
        }
        lblStatus.setText(observable.getLblStatus());
//        resetCashDenominations();
//        resetTransDenominations();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /** To display a popUp window for viewing existing data */
    private void popUp() {
        final HashMap viewMap = new HashMap();
        if ( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||  observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE ||  observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW){
            ArrayList lst = new ArrayList();
            lst.add("CASH_MOVEMENT_ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW){
                viewMap.put(CommonConstants.MAP_NAME, "viewCashMovementDetails");
            }else{
            viewMap.put(CommonConstants.MAP_NAME, "viewCashMovement");
            }
        } else if (viewType == RECEIVING_ID) {
            viewMap.put(CommonConstants.MAP_NAME, "getEmployeeName");
        } else if (viewType == ISSUE_ID) {
            viewMap.put(CommonConstants.MAP_NAME, "getEmployeeName");
        }
        new ViewAll(this, viewMap).show();
    }
    private void resetTransDenominations() {
        for (int i=0; i < _fltDenomination.length; i++) {
            txtTransCount[i].setText("");
            txtTransTotal[i].setText("");
        }
        txtTransGrandTotal.setText("");
        transDetailsHash = null;
        
    }
    private void resetCashDenominations() {
        for (int i=0; i < _fltDenomination.length; i++) {
            txtCashCount[i].setText("");
            txtCashTotal[i].setText("");
        }
        txtCashGrandTotal.setText("");
        cashDetailsHash = null;
    }
    // Called Automatically when viewAll() is Called...
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||  observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
            cashMovementID = (String)hash.get("CASH_MOVEMENT_ID");
            hash.put("CASH_MOVEMENT_ID", cashMovementID);
            hash.put("DENOMINATION_TYPE", CASHBOXDENOMINATION);
            HashMap condition = new HashMap();
            condition.put(CommonConstants.MAP_WHERE, hash);
            observable.populateData(condition);
            
            // to display issue cashier name and receiving cashier name at the time of edit/delete
            observable.setLblDisplayCashierName(observable.getCashierName(observable.getTxtReceivingCashierID()));
            lblDisplayCashierName.setText(observable.getLblDisplayCashierName());
            
            observable.setLblIssueCashierName(observable.getCashierName(observable.getTxtIssueCashierID()));
            lblDisplayIssueCashierName.setText(observable.getLblIssueCashierName());
            populateCashDenominations();
            denomDetailsList = (ArrayList)observable.populateTransDenominations();
            if(denomDetailsList!=null && denomDetailsList.size()>0)
                transDenomNameMap = (HashMap)denomDetailsList.get(0);
            if (denomDetailsList != null && denomDetailsList.size()>0) {
                double grandTotal=0.0;
                for (int i=0; i < _fltDenomination.length; i++) {
                    int index = observable.matchDenomination(i,lblTransDenomination[i].getText(),lblTransDenominationType[i].getText(),denomDetailsList);
                    if (index != -1) {
                        if(observable.getDenominationType().equals(CASHBOXDENOMINATION)){
                            txtCashCount[index].setText(observable.getDenominationCount());
                            txtCashTotal[index].setText(observable.getDenominationTotal());
                        }else{
                            
                            txtTransCount[index].setText(observable.getDenominationCount());
                            txtTransTotal[index].setText(observable.getDenominationTotal());
                        }
                    }
                    
                    if(observable.getDenominationType().equals(CASHBOXDENOMINATION)){
                        grandTotal += new Double(txtCashTotal[i].getText().equals("") ? "0.0" :  txtCashTotal[i].getText()).doubleValue();
                        txtCashGrandTotal.setText(CommonUtil.convertObjToStr(new Double(grandTotal)));
                    }else{
                        grandTotal += new Double(txtTransTotal[i].getText().equals("") ? "0.0" :  txtTransTotal[i].getText()).doubleValue();
                        txtTransGrandTotal.setText(CommonUtil.convertObjToStr(new Double(grandTotal)));
                    }
                    
                }
            }
            txtTransBalance.setText(txtTransGrandTotal.getText());
            denomDetailsList = null;
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                enableDisablePanCashMovement(true);
                enableDisablePanDenomination(true);
                enableDisableHelpButtons(false);
            } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||  observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                enableDisablePanCashMovement(false);
                enableDisablePanDenomination(false);
                enableDisableHelpButtons(false);
            }
        }else if (viewType == RECEIVING_ID) {
            
            observable.setTxtReceivingCashierID((String)hash.get("USER ID"));
            observable.setLblDisplayCashierName((String)hash.get("EMP NAME"));
//            populateCashDenominations();
            if(rdoTranscationType_Receipt.isSelected()){
                HashMap hmap=new HashMap();
                hmap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
                
                
                List list1=ClientUtil.executeQuery("getPendingAuthList", hmap);
                if(list1!=null && list1.size()>0){
                    ClientUtil.displayAlert("Already a record is pending for authorization.Authorize the same and go for a new record ");
                    txtReceivingCashierID.setText("");
                    lblDisplayCashierName.setText("");
                    return;
                }else{
            txtReceivingCashierID.setText(observable.getTxtReceivingCashierID());
            lblDisplayCashierName.setText(observable.getLblDisplayCashierName());
            disableCashierIDs(false);
                }
            }else{
                txtReceivingCashierID.setText(observable.getTxtReceivingCashierID());
                lblDisplayCashierName.setText(observable.getLblDisplayCashierName());
                disableCashierIDs(false);
            }
            
            if(!txtIssueCashierID.getText().equals("") && !txtReceivingCashierID.getText().equals("") && !chkVaultCash.isSelected()){
                rdoTranscationType_Receipt.setEnabled(false);
                rdoTranscationType_Payment.setEnabled(false);
            }
            
        }else if (viewType == ISSUE_ID) {
            observable.setTxtIssueCashierID((String)hash.get("USER ID"));
            observable.setLblIssueCashierName((String)hash.get("EMP NAME"));
//            populateCashDenominations();
            txtIssueCashierID.setText(observable.getTxtIssueCashierID());
            lblDisplayIssueCashierName.setText(observable.getLblIssueCashierName());
            if(!txtIssueCashierID.getText().equals("") && !txtReceivingCashierID.getText().equals("") && !chkVaultCash.isSelected()){
                rdoTranscationType_Receipt.setEnabled(false);
                rdoTranscationType_Payment.setEnabled(false);
            }
            disableCashierIDs(false);
        }
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            observable.setStatus();
            disableCashierIDs(false);
            setButtonEnableDisable();
        }
         if(viewType==AUTHORIZE) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                }
    }
    
    private void populateCashDenominations() {
//        if (viewType == RECEIVING_ID || viewType == ISSUE_ID) {
//        String userID = viewType == RECEIVING_ID ? observable.getTxtReceivingCashierID() : observable.getTxtIssueCashierID();

//        }
            
            denomDetailsList = (ArrayList)observable.populateCashDenominations();
        cashDenomNameMap = (HashMap) denomDetailsList.get(0);
            if (denomDetailsList != null && denomDetailsList.size()>0) {
                double grandTotal=0.0;
                for (int i=0; i < _fltDenomination.length; i++) {
                    
                int index = observable.matchDenomination(i,lblCashDenomination[i].getText(),lblCashDenominationType[i].getText(),denomDetailsList);
                    if (index != -1) {
                        if(observable.getDenominationType().equals(CASHBOXDENOMINATION)){
                            txtCashCount[index].setText(observable.getDenominationCount());
                            txtCashTotal[index].setText(observable.getDenominationTotal());
                        }else{
                            txtTransCount[index].setText(observable.getDenominationCount());
                            txtTransTotal[index].setText(observable.getDenominationTotal());
                        }
                    }
                    
                    if(observable.getDenominationType().equals(CASHBOXDENOMINATION)){
                        grandTotal += new Double(txtCashTotal[i].getText().equals("") ? "0.0" :  txtCashTotal[i].getText()).doubleValue();
                        txtCashGrandTotal.setText(CommonUtil.convertObjToStr(new Double(grandTotal)));
                        txtCashBoxBalance.setText(CommonUtil.convertObjToStr(new Double(grandTotal)));
                    }else{
                        grandTotal += new Double(txtTransTotal[i].getText().equals("") ? "0.0" :  txtTransTotal[i].getText()).doubleValue();
                        txtTransGrandTotal.setText(CommonUtil.convertObjToStr(new Double(grandTotal)));
                    }
                    
                }
            }
            tabDenomination.setSelectedIndex(1);
           
            denomDetailsList = null;
    }
    
    private void setObservable() {
        /* Implementing Singleton pattern */
        observable = CashManagementOB.getInstance();
        observable.addObserver(this);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgVaultCash = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgTransactionType = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrCashManagement = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace26 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace27 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
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
        panCashManagement = new com.see.truetransact.uicomponent.CPanel();
        panDetails = new com.see.truetransact.uicomponent.CPanel();
        lblDate = new com.see.truetransact.uicomponent.CLabel();
        lblDisplayDate = new com.see.truetransact.uicomponent.CLabel();
        lblReceivingCashierId = new com.see.truetransact.uicomponent.CLabel();
        lblCashierName = new com.see.truetransact.uicomponent.CLabel();
        lblDisplayCashierName = new com.see.truetransact.uicomponent.CLabel();
        panCashierID = new com.see.truetransact.uicomponent.CPanel();
        txtReceivingCashierID = new com.see.truetransact.uicomponent.CTextField();
        btnReceivingCashierID = new com.see.truetransact.uicomponent.CButton();
        panIssueCashierID = new com.see.truetransact.uicomponent.CPanel();
        txtIssueCashierID = new com.see.truetransact.uicomponent.CTextField();
        btnIssueCashierID = new com.see.truetransact.uicomponent.CButton();
        lblIssueCashierID = new com.see.truetransact.uicomponent.CLabel();
        lblDisplayIssueCashierName = new com.see.truetransact.uicomponent.CLabel();
        lblIssueCashierName = new com.see.truetransact.uicomponent.CLabel();
        lblTransactionType = new com.see.truetransact.uicomponent.CLabel();
        panTransType = new com.see.truetransact.uicomponent.CPanel();
        rdoTranscationType_Receipt = new com.see.truetransact.uicomponent.CRadioButton();
        rdoTranscationType_Payment = new com.see.truetransact.uicomponent.CRadioButton();
        lblCashBoxBalance = new com.see.truetransact.uicomponent.CLabel();
        txtCashBoxBalance = new com.see.truetransact.uicomponent.CTextField();
        lblTransBalance = new com.see.truetransact.uicomponent.CLabel();
        txtTransBalance = new com.see.truetransact.uicomponent.CTextField();
        chkVaultCash = new com.see.truetransact.uicomponent.CCheckBox();
        tabDenomination = new com.see.truetransact.uicomponent.CTabbedPane();
        panTransScroll = new com.see.truetransact.uicomponent.CPanel();
        scrTransDenomination = new com.see.truetransact.uicomponent.CScrollPane();
        panTransDenomination = new com.see.truetransact.uicomponent.CPanel();
        panCashScroll = new com.see.truetransact.uicomponent.CPanel();
        scrCashDenomination = new com.see.truetransact.uicomponent.CScrollPane();
        panCashDenomination = new com.see.truetransact.uicomponent.CPanel();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrCashManagement = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptDelete = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Cash Management");
        setMinimumSize(new java.awt.Dimension(125, 45));
        setPreferredSize(new java.awt.Dimension(675, 630));

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.setEnabled(false);
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrCashManagement.add(btnView);

        lblSpace4.setText("     ");
        tbrCashManagement.add(lblSpace4);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrCashManagement.add(btnNew);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrCashManagement.add(lblSpace26);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrCashManagement.add(btnEdit);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrCashManagement.add(lblSpace27);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrCashManagement.add(btnDelete);

        lblSpace2.setText("     ");
        tbrCashManagement.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrCashManagement.add(btnSave);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrCashManagement.add(lblSpace28);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrCashManagement.add(btnCancel);

        lblSpace3.setText("     ");
        tbrCashManagement.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrCashManagement.add(btnAuthorize);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrCashManagement.add(lblSpace29);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrCashManagement.add(btnException);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrCashManagement.add(lblSpace30);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.setMaximumSize(new java.awt.Dimension(29, 27));
        btnReject.setMinimumSize(new java.awt.Dimension(29, 27));
        btnReject.setPreferredSize(new java.awt.Dimension(29, 27));
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrCashManagement.add(btnReject);

        lblSpace5.setText("     ");
        tbrCashManagement.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrCashManagement.add(btnPrint);

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrCashManagement.add(lblSpace31);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrCashManagement.add(btnClose);

        getContentPane().add(tbrCashManagement, java.awt.BorderLayout.NORTH);

        panCashManagement.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panCashManagement.setLayout(new java.awt.GridBagLayout());

        panDetails.setLayout(new java.awt.GridBagLayout());

        lblDate.setText("Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDetails.add(lblDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDetails.add(lblDisplayDate, gridBagConstraints);

        lblReceivingCashierId.setText("Receiving Cashier ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDetails.add(lblReceivingCashierId, gridBagConstraints);

        lblCashierName.setText("Cashier Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDetails.add(lblCashierName, gridBagConstraints);

        lblDisplayCashierName.setForeground(new java.awt.Color(0, 51, 204));
        lblDisplayCashierName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDetails.add(lblDisplayCashierName, gridBagConstraints);

        panCashierID.setLayout(new java.awt.GridBagLayout());

        txtReceivingCashierID.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panCashierID.add(txtReceivingCashierID, gridBagConstraints);

        btnReceivingCashierID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnReceivingCashierID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnReceivingCashierID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnReceivingCashierID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReceivingCashierIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panCashierID.add(btnReceivingCashierID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDetails.add(panCashierID, gridBagConstraints);

        panIssueCashierID.setLayout(new java.awt.GridBagLayout());

        txtIssueCashierID.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panIssueCashierID.add(txtIssueCashierID, gridBagConstraints);

        btnIssueCashierID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnIssueCashierID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnIssueCashierID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnIssueCashierID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIssueCashierIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panIssueCashierID.add(btnIssueCashierID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDetails.add(panIssueCashierID, gridBagConstraints);

        lblIssueCashierID.setText("Issue Cashier ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDetails.add(lblIssueCashierID, gridBagConstraints);

        lblDisplayIssueCashierName.setForeground(new java.awt.Color(0, 51, 204));
        lblDisplayIssueCashierName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDetails.add(lblDisplayIssueCashierName, gridBagConstraints);

        lblIssueCashierName.setText("Issue Cashier Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDetails.add(lblIssueCashierName, gridBagConstraints);

        lblTransactionType.setText("Transaction Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDetails.add(lblTransactionType, gridBagConstraints);

        panTransType.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                panTransTypeFocusLost(evt);
            }
        });
        panTransType.setLayout(new java.awt.GridBagLayout());

        rdgTransactionType.add(rdoTranscationType_Receipt);
        rdoTranscationType_Receipt.setText("Receipt");
        rdoTranscationType_Receipt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTranscationType_ReceiptActionPerformed(evt);
            }
        });
        panTransType.add(rdoTranscationType_Receipt, new java.awt.GridBagConstraints());

        rdgTransactionType.add(rdoTranscationType_Payment);
        rdoTranscationType_Payment.setText("Payment");
        rdoTranscationType_Payment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTranscationType_PaymentActionPerformed(evt);
            }
        });
        panTransType.add(rdoTranscationType_Payment, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDetails.add(panTransType, gridBagConstraints);

        lblCashBoxBalance.setText("Cash Box Balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDetails.add(lblCashBoxBalance, gridBagConstraints);

        txtCashBoxBalance.setEditable(false);
        txtCashBoxBalance.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDetails.add(txtCashBoxBalance, gridBagConstraints);

        lblTransBalance.setText("Transaction Balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDetails.add(lblTransBalance, gridBagConstraints);

        txtTransBalance.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDetails.add(txtTransBalance, gridBagConstraints);

        chkVaultCash.setText("Vault Cash");
        chkVaultCash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkVaultCashActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panDetails.add(chkVaultCash, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panCashManagement.add(panDetails, gridBagConstraints);

        tabDenomination.setMinimumSize(new java.awt.Dimension(320, 380));
        tabDenomination.setPreferredSize(new java.awt.Dimension(320, 380));

        panTransScroll.setLayout(new java.awt.GridBagLayout());

        scrTransDenomination.setMinimumSize(new java.awt.Dimension(275, 304));
        scrTransDenomination.setPreferredSize(new java.awt.Dimension(275, 304));

        panTransDenomination.setMinimumSize(new java.awt.Dimension(271, 450));
        panTransDenomination.setPreferredSize(new java.awt.Dimension(271, 450));
        panTransDenomination.setLayout(new java.awt.GridBagLayout());
        scrTransDenomination.setViewportView(panTransDenomination);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTransScroll.add(scrTransDenomination, gridBagConstraints);

        tabDenomination.addTab("Trans Denomination", panTransScroll);

        panCashScroll.setLayout(new java.awt.GridBagLayout());

        scrCashDenomination.setMinimumSize(new java.awt.Dimension(275, 304));
        scrCashDenomination.setPreferredSize(new java.awt.Dimension(275, 304));

        panCashDenomination.setMinimumSize(new java.awt.Dimension(290, 450));
        panCashDenomination.setPreferredSize(new java.awt.Dimension(290, 450));
        panCashDenomination.setLayout(new java.awt.GridBagLayout());
        scrCashDenomination.setViewportView(panCashDenomination);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panCashScroll.add(scrCashDenomination, gridBagConstraints);

        tabDenomination.addTab("Cash Box Denomination\n", panCashScroll);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        panCashManagement.add(tabDenomination, gridBagConstraints);
        tabDenomination.getAccessibleContext().setAccessibleName("Trans Denomination");

        getContentPane().add(panCashManagement, java.awt.BorderLayout.CENTER);

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
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptDelete);

        mitSave.setText("Save");
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitClose.setText("Close");
        mnuProcess.add(mitClose);

        mbrCashManagement.add(mnuProcess);

        setJMenuBar(mbrCashManagement);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void chkVaultCashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkVaultCashActionPerformed
        // TODO add your handling code here:
        //        if(chkVaultCash.isSelected()){
        //            lblIssueCashierID.setVisible(false);
        //            panIssueCashierID.setVisible(false);
        //            lblIssueCashierName.setVisible(false);
        //            lblTransactionType.setVisible(true);
        //            panTransType.setVisible(true);
        //            lblReceivingCashierId.setVisible(true);
        //            panCashierID.setVisible(true);
        //        }else{
        //            lblIssueCashierID.setVisible(true);
        //            panIssueCashierID.setVisible(true);
        //            lblIssueCashierName.setVisible(true);
        //            lblTransactionType.setVisible(false);
        //            panTransType.setVisible(false);
        //            lblReceivingCashierId.setVisible(true);
        //            panCashierID.setVisible(true);
        //        }
    }//GEN-LAST:event_chkVaultCashActionPerformed
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
         observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        popUp();
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed
    
    private void rdoTranscationType_PaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTranscationType_PaymentActionPerformed
        // TODO add your handling code here:
        
        transTypeSelected = true;
        lblTransBalance.setText(resourceBundle.getString("lblPaymentBalance"));
        tabDenomination.setTitleAt(0, resourceBundle.getString("lblPaymentDenomination"));
        lblReceivingCashierId.setVisible(false);
        panCashierID.setVisible(false);
        lblCashierName.setVisible(false);
        lblDisplayCashierName.setVisible(false);
        lblIssueCashierID.setVisible(true);
        panIssueCashierID.setVisible(true);
        lblIssueCashierName.setVisible(true);
        lblDisplayIssueCashierName.setVisible(true);
        //        lblReceivingCashierId.setText("Issue Cashier ID");
        //        lblCashierName.setText("Issuing Cashier Name");
        //        lblIssueCashierID.setText("Receiving Cashier ID");
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            String userID = txtReceivingCashierID.getText();
            observable.setRdoTranscationType_Payment(rdoTranscationType_Payment.isSelected());
            observable.setRdoVaultCash_Yes(chkVaultCash.isSelected());
            //            observable.setRdoVaultCash_No(rdoVaultCash_No.isSelected());
            //            if (userID.length() != 0) {
            ////                displayAlert("Select the Receiving Cashier ID");
            ////                observable.setRdoTranscationType_Payment(false);
            ////                rdoTranscationType_Payment.setSelected(false);
            ////                return;
            ////            } else {
            //                HashMap hashForDenom = new HashMap();
            //                HashMap where = new HashMap();
            //                where.put (CommonConstants.USER_ID, userID);
            //                List lst = ClientUtil.executeQuery("getMaxDateFromCashMovement", where);
            //                HashMap dateMap = new HashMap();
            //                if (lst.size()>0) {
            //                    dateMap = (HashMap) lst.get(0);
            //                    if (dateMap.size()>0 && dateMap.containsKey("MAX_DT") && dateMap.get("MAX_DT")!=null) {
            //                        where.put(CommonConstants.USER_ID, userID);
            //                        where.put("CURR_DATE", dateMap.get("MAX_DT"));
            //                        where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            //                        where.put("INITIATED_BRANCH",TrueTransactMain.BRANCH_ID);
            //                        hashForDenom.put(CommonConstants.MAP_WHERE, where);
            //                        hashForDenom.put(CommonConstants.MAP_NAME, "getAvailableDenominations");
            //                        observable.populateData(hashForDenom);
//            } else {
            //                        resetCashDenominations();
            //                        return;
            //                    }
            //                }
            //                hashForDenom = null;
            //                where = null;
            //                lst = null;
            //                dateMap = null;
            //            }
            if(!chkVaultCash.isSelected()){
                ClientUtil.displayAlert("Please Select Vault Cash");
                        return;
            }else{
                txtCashBoxBalance.setEnabled(true);
                txtCashBoxBalance.setText("");
            }
        } 
        
        //       populateCashDenominations();
      
    }//GEN-LAST:event_rdoTranscationType_PaymentActionPerformed
    
    private void rdoTranscationType_ReceiptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTranscationType_ReceiptActionPerformed
        // TODO add your handling code here:
        transTypeSelected = true;
        lblTransBalance.setText(resourceBundle.getString("lblReceiptBalance"));
        tabDenomination.setTitleAt(0, resourceBundle.getString("lblReceiptDenomination"));
        lblIssueCashierID.setVisible(false);
        panIssueCashierID.setVisible(false);
        lblIssueCashierName.setVisible(false);
        lblDisplayIssueCashierName.setVisible(false);
        lblReceivingCashierId.setVisible(true);
        panCashierID.setVisible(true);
        lblCashierName.setVisible(true);
        lblDisplayCashierName.setVisible(true);
        
        //        lblReceivingCashierId.setText("Receiving Cashier ID");
        //        lblCashierName.setText("Receiving Cashier Name");
        //        lblIssueCashierID.setText("Issue Cashier ID");
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            String userID = txtReceivingCashierID.getText();
            observable.setRdoTranscationType_Payment(rdoTranscationType_Payment.isSelected());
            observable.setRdoVaultCash_Yes(chkVaultCash.isSelected());
            //            observable.setRdoVaultCash_No(rdoVaultCash_No.isSelected());
            //            if (userID.length() != 0) {
            ////                displayAlert("Select the Receiving Cashier ID");
            ////                observable.setRdoTranscationType_Payment(false);
            ////                rdoTranscationType_Payment.setSelected(false);
            ////                return;
            ////            } else {
            //                HashMap hashForDenom = new HashMap();
            //                HashMap where = new HashMap();
            //                where.put (CommonConstants.USER_ID, userID);
            ////                where.put("ISSUE_USER_ID", txtIssueCashierID.getText());
            //                where.put("CURR_DATE", currDt);
            //                where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            //                hashForDenom.put(CommonConstants.MAP_WHERE, where);
            //                hashForDenom.put(CommonConstants.MAP_NAME, "getClosingDenominations");
            //                observable.populateData(hashForDenom);
            //                hashForDenom = null;
            //                where = null;
            //            }
            if(chkVaultCash.isSelected()){
                HashMap hmap=new HashMap();
                hmap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
                
                hmap.put("CURRENCY", "INR");// currency is hardcoded as INR
                List data =  ClientUtil.executeQuery("getDenominations", hmap);
                List list=ClientUtil.executeQuery("getCashBoxValue",hmap);
                List list1=ClientUtil.executeQuery("getPendingAuthList", hmap);
                if(list1!=null && list1.size()>0){
                    ClientUtil.displayAlert("Already a record is pending for authorization.Authorize the same and go for a new record ");
                    return;
                    
                }else
                    if(list!=null && list.size()>0){
                        String tot="";
                        double totalcount=0.0;
                        for(int i=0;i<list.size();i++){
                            hmap=(HashMap)list.get(i);
                            if(data!=null && data.size()>0){
                                for(int j=0;j<data.size();j++){
                                    if(lblCashDenomination[j].getText().equals(hmap.get("DENOMINATION_NAME")) && lblCashDenominationType[j].getText().equals(hmap.get("DENOMINATION_TYPE"))){
                                        double dvalue=CommonUtil.convertObjToDouble(hmap.get("DENOMINATION_VALUE")).doubleValue() ;
                                        
                                        double count =dvalue* CommonUtil.convertObjToDouble(hmap.get("OPENING_COUNT")).doubleValue();
                                        totalcount=totalcount+count;
                                        String total=CommonUtil.convertObjToStr(new Double(count)) ;
                                        txtCashCount[j].setText(CommonUtil.convertObjToStr(hmap.get("OPENING_COUNT")));
                                        txtCashTotal[j].setText(total);
                                    }
                                }
                            }
                        }
                        tot=CommonUtil.convertObjToStr(new Double(totalcount)) ;
                        txtCashBoxBalance.setText(tot);
                        txtCashGrandTotal.setText(tot);
                        
                    }else{
                        String s="0.0";
                        txtCashBoxBalance.setText(s);
                    }
                txtCashBoxBalance.setEnabled(false);
            }else{
                ClientUtil.displayAlert("Please Select Vault Cash");
                rdoTranscationType_Receipt.setSelected(false);
                return;
            }            

        }        
      // populateCashDenominations(); 
        //        if(rdoVaultCash_Yes.isSelected()){
        //        resetCashDenominations();
        //        txtCashBoxBalance.setText("0.0");
        //        tabDenomination.setSelectedIndex(0);
        //        }
    }//GEN-LAST:event_rdoTranscationType_ReceiptActionPerformed
    
    private void panTransTypeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_panTransTypeFocusLost
        // TODO add your handling code here:
        
    }//GEN-LAST:event_panTransTypeFocusLost
    
    private void btnIssueCashierIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIssueCashierIDActionPerformed
        // TODO add your handling code here:
        if(chkVaultCash.isSelected()){
        if (transTypeSelected) {
            viewType = ISSUE_ID;
            popUp();            
            } else{
            displayAlert("Please select Transaction Type");
            }
        }else{
            if(rdoTranscationType_Payment.isSelected()){
                displayAlert("Please select VaultCash");
                return;
            }else{
                viewType = ISSUE_ID;
                popUp();
            }
        }
    }//GEN-LAST:event_btnIssueCashierIDActionPerformed
    
    private void btnReceivingCashierIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReceivingCashierIDActionPerformed
        // TODO add your handling code here:
        if(chkVaultCash.isSelected()){
        if (transTypeSelected) {
            viewType = RECEIVING_ID;
            popUp();            
            } else{
            displayAlert("Please select Transaction Type");
            }
        }else{
            if(rdoTranscationType_Receipt.isSelected()){
                displayAlert("Please select VaultCash");
                return;
            }else{
                viewType = RECEIVING_ID;
                popUp();
            }
        }
    }//GEN-LAST:event_btnReceivingCashierIDActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        try{
            observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION) ;
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
        }catch(Exception e){
            
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        try{
            observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        }catch(Exception e){
            
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        try{
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
            
        }catch(Exception e){
            
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        enableDisablePanCashMovement(false);
        enableDisablePanDenomination(false);
        enableDisableHelpButtons(false);
        disableCashierIDs(false);
        setButtonEnableDisable();
        txtTransBalance.setText("");
        observable.resetForm();
        resetTransDenominations();
        resetCashDenominations();
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        observable.setStatus();
        btnException.setEnabled(true);
        transTypeSelected=false;
        
        lblIssueCashierID.setVisible(true);
        panIssueCashierID.setVisible(true);
        lblIssueCashierName.setVisible(true);
        lblReceivingCashierId.setVisible(true);
        panCashierID.setVisible(true);
        lblCashierName.setVisible(true);
        txtIssueCashierID.setVisible(true);
        txtReceivingCashierID.setVisible(true);
        lblTransBalance.setText("Transaction Balance");
        tabDenomination.setTitleAt(0, "Trans Denomination");
        TRANSACTIONDENOMINATION="";
        isFilled=false;
         viewType = -1 ;
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panDetails);
        
        if (mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
        } else if (checkMandatoryForTransDenomination() && checkMandatoryForCashDenomination()) {
            // Transaction Denominations is empty
            displayAlert((String)resourceBundle.getString("warningDenomination"));
            //        }else if(checkMandatoryForCashDenomination()){
            //CashBoxDenominations is Empty
        }
        
        
        else {
            
            savePerformed();
            
        }
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed
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
    
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    private void enableDisableHelpButtons(boolean flag) {
        btnReceivingCashierID.setEnabled(flag);
        btnIssueCashierID.setEnabled(flag);
    }
    
    /** This method is used to Set the Transaction Type to the varaible TRANSACTIONDENOMINATION according to type of Transaction
     ** selected by the user **/
    private void setTransactionType(){
        if(rdoTranscationType_Receipt.isSelected()){
            TRANSACTIONDENOMINATION = "RECEIPT";
        }else if(rdoTranscationType_Payment.isSelected()){
            TRANSACTIONDENOMINATION = "PAYMENT";
        }
    }
    
    private void savePerformed(){
        updateOBFields();
        setTransactionType();
        observable.saveCashMovementDetails(getTransDenominationValues(),TRANSACTIONDENOMINATION);
        observable.saveCashMovementDetails(getCashDenominationValues(),CASHBOXDENOMINATION);
        //HashMap hash = new HashMap();
        
        observable.doAction();
        HashMap lockMap = new HashMap();
        ArrayList lst = new ArrayList();
        lst.add("CASH_MOVEMENT_ID");
        lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
        if (observable.getProxyReturnMap()!=null) {
            if (observable.getProxyReturnMap().containsKey("CASH_MOVEMENT_ID")) {
                lockMap.put("CASH_MOVEMENT_ID", observable.getProxyReturnMap().get("CASH_MOVEMENT_ID"));
            }
        }
       if ( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ) {
            lockMap.put("CASH_MOVEMENT_ID",observable.getCashMovementID());
        }
        setEditLockMap(lockMap);
        setEditLock();
        btnCancelActionPerformed(null);
        observable.setResultStatus();
        
        
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        viewType = DELETE;
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        popUp();
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        viewType = EDIT;
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        popUp();
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        
        viewType = NEW;
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
       
        //getForexTellerDeno();
        enableDisablePanCashMovement(true);
        enableDisablePanDenomination(true);
        enableDisableHelpButtons(true);
        disableCashierIDs(false);
        setButtonEnableDisable();
        observable.setStatus();
        observable.resetForm();
        resetTransDenominations();
        resetCashDenominations();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        lblDisplayDate.setText(observable.returnDisplayDate());
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnSave.setEnabled(true);
        
    }//GEN-LAST:event_btnNewActionPerformed
    private void enableDisablePanDenomination(boolean flag) {
        ClientUtil.enableDisable(panTransDenomination, flag);
        ClientUtil.enableDisable(panCashDenomination, flag);
    }
    private void enableDisablePanCashMovement(boolean flag) {
        ClientUtil.enableDisable(panDetails, flag);
        
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnIssueCashierID;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReceivingCashierID;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CCheckBox chkVaultCash;
    private com.see.truetransact.uicomponent.CLabel lblCashBoxBalance;
    private com.see.truetransact.uicomponent.CLabel lblCashierName;
    private com.see.truetransact.uicomponent.CLabel lblDate;
    private com.see.truetransact.uicomponent.CLabel lblDisplayCashierName;
    private com.see.truetransact.uicomponent.CLabel lblDisplayDate;
    private com.see.truetransact.uicomponent.CLabel lblDisplayIssueCashierName;
    private com.see.truetransact.uicomponent.CLabel lblIssueCashierID;
    private com.see.truetransact.uicomponent.CLabel lblIssueCashierName;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblReceivingCashierId;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace26;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace28;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace31;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTransBalance;
    private com.see.truetransact.uicomponent.CLabel lblTransactionType;
    private com.see.truetransact.uicomponent.CMenuBar mbrCashManagement;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panCashDenomination;
    private com.see.truetransact.uicomponent.CPanel panCashManagement;
    private com.see.truetransact.uicomponent.CPanel panCashScroll;
    private com.see.truetransact.uicomponent.CPanel panCashierID;
    private com.see.truetransact.uicomponent.CPanel panDetails;
    private com.see.truetransact.uicomponent.CPanel panIssueCashierID;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTransDenomination;
    private com.see.truetransact.uicomponent.CPanel panTransScroll;
    private com.see.truetransact.uicomponent.CPanel panTransType;
    private com.see.truetransact.uicomponent.CButtonGroup rdgTransactionType;
    private com.see.truetransact.uicomponent.CButtonGroup rdgVaultCash;
    private com.see.truetransact.uicomponent.CRadioButton rdoTranscationType_Payment;
    private com.see.truetransact.uicomponent.CRadioButton rdoTranscationType_Receipt;
    private com.see.truetransact.uicomponent.CScrollPane scrCashDenomination;
    private com.see.truetransact.uicomponent.CScrollPane scrTransDenomination;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private com.see.truetransact.uicomponent.CTabbedPane tabDenomination;
    private javax.swing.JToolBar tbrCashManagement;
    private com.see.truetransact.uicomponent.CTextField txtCashBoxBalance;
    private com.see.truetransact.uicomponent.CTextField txtIssueCashierID;
    private com.see.truetransact.uicomponent.CTextField txtReceivingCashierID;
    private com.see.truetransact.uicomponent.CTextField txtTransBalance;
    // End of variables declaration//GEN-END:variables
    
}
