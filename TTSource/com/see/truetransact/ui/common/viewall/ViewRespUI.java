/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ViewRespUI.java
 *
 * Created on April 13, 2011, 10:10 PM
 */

package com.see.truetransact.ui.common.viewall;

import java.util.Observer;

import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.ArrayList;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Toolkit;

import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.ui.transaction.cash.CashTransactionUI;
import com.see.truetransact.ui.transaction.transfer.TransferUI;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.DefaultValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.common.viewall.ViewAll;

/**
 *
 * @author  Suresh
 */
public class ViewRespUI extends CInternalFrame implements java.util.Observer {
    
    
    //    final CheckCustomerIdRB resourceBundle = new CheckCustomerIdRB();
    private CTable _tblData;
    private HashMap dataHash;
    private ArrayList data;
    private int dataSize;
    private ArrayList _heading;
    private boolean _isAvailable = true;
    private ArrayList termLoanData = new ArrayList();
    Date currDt = null;
    public String branchID;
    private double displayBalance = 0;
    private double totalBalance = 0;
    private double recieptAmount = 0;
    private double paymentAmount = 0;
    private boolean showDueTableFinal = false;
    private boolean hasPenal = false;
    private String transType = "";
    private double penalAmount = 0.0;
    final int BRANCH=2;
    final int ADVICE_NO=3;
    int viewType=-1;
    CashTransactionUI cashtransactionUI=new CashTransactionUI();
    TransferUI transferUI=new TransferUI();
    public ViewRespUI() {
        initComponents();
        initForm();
    }
    /** Account Number Constructor */
    public ViewRespUI(double totalAmount,String transType,CashTransactionUI cashtransactionUI) {
        initComponents();
        initForm();
        txtAdviceNo.setText(cashtransactionUI.getOrgOrRespAdviceNo());
        dtdAdviceDt.setDateValue(CommonUtil.convertObjToStr(cashtransactionUI.getOrgOrRespAdviceDt()));
        txtAmt.setText(CommonUtil.convertObjToStr(totalAmount));
        txtRespBranch.setText(cashtransactionUI.getOrgBranch());
        lblBranchName.setText(cashtransactionUI.getOrgBranchName());
        cboCategory.setSelectedItem(cashtransactionUI.getOrgOrRespCategory());
        txtAmt.setEnabled(false);
//        txtAdviceNo.setEnabled(false);
//        dtdAdviceDt.setEnabled(false);
        this.paymentAmount=totalAmount;
        this.transType = transType;
        this.cashtransactionUI=cashtransactionUI;
        branchID = TrueTransactMain.BRANCH_ID;
        setupScreen();
        
    }
    
        public ViewRespUI(double totalAmount,String transType,TransferUI transferUI) {
        initComponents();
        initForm();
        txtAdviceNo.setText(transferUI.getOrgOrRespAdviceNo());
        dtdAdviceDt.setDateValue(CommonUtil.convertObjToStr(transferUI.getOrgOrRespAdviceDt()));
        txtAmt.setText(CommonUtil.convertObjToStr(totalAmount));
        txtRespBranch.setText(transferUI.getOrgBranch());
        lblBranchName.setText(transferUI.getOrgBranchName());
        cboCategory.setSelectedItem(transferUI.getOrgOrRespCategory());
        txtAmt.setEnabled(false);
//        txtAdviceNo.setEnabled(false);
//        dtdAdviceDt.setEnabled(false);
        this.paymentAmount=totalAmount;
        this.transType = transType;
        this.transferUI=transferUI;
        branchID = TrueTransactMain.BRANCH_ID;
        setupScreen();
        
    }
    
    /** Method which is used to initialize the form TokenConfig */
    private void initForm(){
        setMaxLengths();        
        internationalize();
        currDt = ClientUtil.getCurrentDate();
    }
    
    private void setupScreen() {
//        setModal(true);
        setTitle("Responding Details"+"["+branchID+"]");
        /* Calculate the screen size */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println("@$#@$#@# screenSize : "+screenSize);
        setSize(570, 280);
        /* Center frame on the screen */
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }
    

    
  
    
  
  
    
    /** Used to set Maximum possible lenghts for TextFields */
    private void setMaxLengths(){
        txtAmt.setValidation(new CurrencyValidation(16,2));
        txtRespBranch.setAllowAll(true);
        txtAdviceNo.setAllowAll(true);
    }
    
    
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
   
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        
    }
    
    public void update(java.util.Observable o, Object arg) {
    }
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return null;
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panMemberShipFacility = new com.see.truetransact.uicomponent.CPanel();
        btnOk = new com.see.truetransact.uicomponent.CButton();
        panTotal1 = new com.see.truetransact.uicomponent.CPanel();
        lblAdviceNo = new com.see.truetransact.uicomponent.CLabel();
        lblAdviceDate = new com.see.truetransact.uicomponent.CLabel();
        lblAmount = new com.see.truetransact.uicomponent.CLabel();
        lblCategory = new com.see.truetransact.uicomponent.CLabel();
        lblRespBranchId = new com.see.truetransact.uicomponent.CLabel();
        txtAmt = new com.see.truetransact.uicomponent.CTextField();
        dtdAdviceDt = new com.see.truetransact.uicomponent.CDateField();
        lblBranchName = new com.see.truetransact.uicomponent.CLabel();
        cboCategory = new com.see.truetransact.uicomponent.CComboBox();
        panAcctNo = new com.see.truetransact.uicomponent.CPanel();
        txtRespBranch = new com.see.truetransact.uicomponent.CTextField();
        btnRespBranch = new com.see.truetransact.uicomponent.CButton();
        panAcctNo1 = new com.see.truetransact.uicomponent.CPanel();
        txtAdviceNo = new com.see.truetransact.uicomponent.CTextField();
        btnAdviceNo = new com.see.truetransact.uicomponent.CButton();

        setPreferredSize(new java.awt.Dimension(500, 316));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panMemberShipFacility.setMaximumSize(new java.awt.Dimension(400, 200));
        panMemberShipFacility.setMinimumSize(new java.awt.Dimension(400, 200));
        panMemberShipFacility.setPreferredSize(new java.awt.Dimension(400, 200));
        panMemberShipFacility.setLayout(new java.awt.GridBagLayout());

        btnOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnOk.setText("OK");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        panMemberShipFacility.add(btnOk, gridBagConstraints);

        panTotal1.setMaximumSize(new java.awt.Dimension(300, 300));
        panTotal1.setMinimumSize(new java.awt.Dimension(300, 300));
        panTotal1.setPreferredSize(new java.awt.Dimension(300, 250));
        panTotal1.setLayout(new java.awt.GridBagLayout());

        lblAdviceNo.setText("Advice No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotal1.add(lblAdviceNo, gridBagConstraints);

        lblAdviceDate.setText("Advice Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotal1.add(lblAdviceDate, gridBagConstraints);

        lblAmount.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotal1.add(lblAmount, gridBagConstraints);

        lblCategory.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCategory.setText("Category");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotal1.add(lblCategory, gridBagConstraints);

        lblRespBranchId.setText("Orginating Branch Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotal1.add(lblRespBranchId, gridBagConstraints);

        txtAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmt.setPreferredSize(new java.awt.Dimension(21, 200));
        txtAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotal1.add(txtAmt, gridBagConstraints);

        dtdAdviceDt.setMinimumSize(new java.awt.Dimension(100, 21));
        dtdAdviceDt.setPreferredSize(new java.awt.Dimension(21, 200));
        dtdAdviceDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                dtdAdviceDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotal1.add(dtdAdviceDt, gridBagConstraints);

        lblBranchName.setForeground(new java.awt.Color(0, 51, 204));
        lblBranchName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblBranchName.setMaximumSize(new java.awt.Dimension(50, 21));
        lblBranchName.setMinimumSize(new java.awt.Dimension(50, 21));
        lblBranchName.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotal1.add(lblBranchName, gridBagConstraints);

        cboCategory.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select", "DD", "MT", "Dividend", "FD Interest", "Misc", "Salary Clearing", "Loan Installment", "Others" }));
        cboCategory.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCategory.setPopupWidth(250);
        cboCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCategoryActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTotal1.add(cboCategory, gridBagConstraints);

        panAcctNo.setMinimumSize(new java.awt.Dimension(121, 21));
        panAcctNo.setPreferredSize(new java.awt.Dimension(21, 200));
        panAcctNo.setLayout(new java.awt.GridBagLayout());

        txtRespBranch.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRespBranch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRespBranchActionPerformed(evt);
            }
        });
        txtRespBranch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRespBranchFocusLost(evt);
            }
        });
        panAcctNo.add(txtRespBranch, new java.awt.GridBagConstraints());

        btnRespBranch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnRespBranch.setToolTipText("Account No.");
        btnRespBranch.setPreferredSize(new java.awt.Dimension(21, 21));
        btnRespBranch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRespBranchActionPerformed(evt);
            }
        });
        panAcctNo.add(btnRespBranch, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotal1.add(panAcctNo, gridBagConstraints);

        panAcctNo1.setMinimumSize(new java.awt.Dimension(121, 21));
        panAcctNo1.setPreferredSize(new java.awt.Dimension(21, 200));
        panAcctNo1.setLayout(new java.awt.GridBagLayout());

        txtAdviceNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAdviceNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAdviceNoActionPerformed(evt);
            }
        });
        txtAdviceNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAdviceNoFocusLost(evt);
            }
        });
        panAcctNo1.add(txtAdviceNo, new java.awt.GridBagConstraints());

        btnAdviceNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAdviceNo.setToolTipText("Account No.");
        btnAdviceNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAdviceNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdviceNoActionPerformed(evt);
            }
        });
        panAcctNo1.add(btnAdviceNo, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotal1.add(panAcctNo1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 38, 3, 41);
        panMemberShipFacility.add(panTotal1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(panMemberShipFacility, gridBagConstraints);
        panMemberShipFacility.getAccessibleContext().setAccessibleName("MembershipFacifility");

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        // TODO add your handling code here:
    String branch=txtRespBranch.getText();
    String adno=txtAdviceNo.getText();
    String dt=dtdAdviceDt.getDateValue();
    String category=CommonUtil.convertObjToStr(cboCategory.getSelectedItem());
    String amt=txtAmt.getText();
    if(branch.equals("") || adno.equals("") || dt.equals("") || category.equals("") || amt.equals("")){
        if(branch.equals("")){
            ClientUtil.displayAlert("Branch Id Should be empty");
            return;
        }else if(adno.equals("")){
            ClientUtil.displayAlert("Advice No Should be empty");
            return;
        }else if(dt.equals("")){
            ClientUtil.displayAlert("Enter Advice Date" );
            return;
        }else if(category.equals("")){
            ClientUtil.displayAlert("Sellect the Category!!!!" );
            return;
        }
        
    }else{
    cashtransactionUI.setOrgOrRespAmout(paymentAmount);
    cashtransactionUI.setOrgBranch(txtRespBranch.getText());
    cashtransactionUI.setOrgBranchName(lblBranchName.getText());
    cashtransactionUI.setOrgOrRespCategory(CommonUtil.convertObjToStr(cboCategory.getSelectedItem()));
    cashtransactionUI.setOrgOrRespAdviceNo(txtAdviceNo.getText());
    cashtransactionUI.setOrgOrRespAdviceDt(DateUtil.getDateMMDDYYYY(dtdAdviceDt.getDateValue()));
    if(transType.equals("DEBIT")){
      cashtransactionUI.setOrgOrRespTransType("R");
    }else if(transType.equals("CREDIT")){
      cashtransactionUI.setOrgOrRespTransType("O");
    }
    cashtransactionUI.setOrgOrRespAdviceDt(currDt);
    transferUI.setOrgOrRespAmout(paymentAmount);
    transferUI.setOrgBranch(txtRespBranch.getText());
    transferUI.setOrgBranchName(lblBranchName.getText());
    transferUI.setOrgOrRespCategory(CommonUtil.convertObjToStr(cboCategory.getSelectedItem()));
    transferUI.setOrgOrRespAdviceNo(txtAdviceNo.getText());
    transferUI.setOrgOrRespAdviceDt(DateUtil.getDateMMDDYYYY(dtdAdviceDt.getDateValue()));
    if(transType.equals("DEBIT")){
      transferUI.setOrgOrRespTransType("R");
    }else if(transType.equals("CREDIT")){
      transferUI.setOrgOrRespTransType("O");
    }
     transferUI.setOrgOrRespAdviceDt(currDt);
        this.dispose();
    }
    }//GEN-LAST:event_btnOkActionPerformed
    
    
                                    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
                                                                                                                                                                                }//GEN-LAST:event_formWindowClosed
                                    
                                    
                                                            private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
                                                                                                                                                                                                                                                                                                                                                            }//GEN-LAST:event_formWindowClosing
                                                            
                                                            /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
                                                            }//GEN-LAST:event_exitForm

    private void txtAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmtFocusLost
        // Add your handling code here:
        //                        String amt = "";
        //                        if (cboInputCurrency.getSelectedIndex() > -1) {
        //                            String type = "";
        //                            if (rdoTransactionType_Credit.isSelected()) {
        //                                type = "CREDIT";
        //                            } else {
        //                                type = "DEBIT";
        //                            }
        //                            if(this.txtInputAmt!=null && this.txtInputAmt.getText().length()>0){
        //                                try {
        //                                    amt = String.valueOf(
        //                                    ClientUtil.convertCurrency(
        //                                    (String) ((ComboBoxModel) cboInputCurrency.getModel()).getKeyForSelected(),
        //                                    lblProductCurrency.getText(),
        //                                    type, new Double(txtInputAmt.getText()).doubleValue()));
        //                                } catch (Exception e) {
        //                                    System.out.println ("currency conversion error");
        //                                    amt = txtInputAmt.getText();
        //                                }
        //                            }
        //                        }
        //                        txtAmount.setText(amt);
    }//GEN-LAST:event_txtAmtFocusLost

    private void dtdAdviceDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dtdAdviceDtFocusLost
        // Add your handling code here:
        //ClientUtil.validateLTDate(tdtInstrumentDate);
    }//GEN-LAST:event_dtdAdviceDtFocusLost

    private void cboCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCategoryActionPerformed
        // Add your handling code here:
        //To Set the Value of Account Head in UI...
        
    }//GEN-LAST:event_cboCategoryActionPerformed

    private void txtRespBranchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRespBranchActionPerformed
        //        // TODO add your handling code here:
        //        HashMap hash = new HashMap();
        //        String ACCOUNTNO = (String) txtAccNo.getText();
        //        if( observable.getProdType().equals("TD")){
        //            if (ACCOUNTNO.lastIndexOf("_")!=-1){
        //                hash.put("ACCOUNTNO", txtAccNo.getText());
        //            }else
        //                hash.put("ACCOUNTNO", txtAccNo.getText()+"_1");
        //        }else{
        //            hash.put("ACCOUNTNO", txtAccNo.getText());
        //        }
        //        hash.put("ACT_NUM", hash.get("ACCOUNTNO"));
        //        hash.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
        //        hash.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
        //        List actlst=null;
        //        List lst=null;
        //        HashMap notClosedMap = new HashMap();
        //        if( observable.getProdType().equals("TD")){
        //            actlst=ClientUtil.executeQuery("getNotClosedDeposits",hash);
        //            if(actlst!=null && actlst.size()>0){
        //                notClosedMap =(HashMap)actlst.get(0);
        //            }
        //        }
        //
        //        if( observable.getProdType().equals("TL"))
        //            actlst=ClientUtil.executeQuery("getActNotCLOSEDTL",hash);
        //
        //        if( observable.getProdType().equals("OA"))
        //            observable.setAccountName(ACCOUNTNO);
        //
        //        if(observable.getProdType().equals("TD") || observable.getProdType().equals("TL")){
        //            if(rdoTransactionType_Debit.isSelected() || rdoTransactionType_Credit.isSelected()){
        //                if(observable.getProdType().equals("TL")){
        //                    if(actlst!=null && actlst.size()>0){
        //                        viewType = ACCNO;
        //                        updateOBFields();
        //                        hash.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
        //                        hash.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
        //                        if( observable.getProdType().equals("TL")) {
        //                            if(rdoTransactionType_Debit.isSelected()) {
        //                                hash.put("PAYMENT","PAYMENT");
        //                                lst=ClientUtil.executeQuery("Cash.getAccountList"
        //                                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
        //                            }else if(rdoTransactionType_Credit.isSelected()){
        //                                if(observable.getProdType().equals("TL"))
        //                                    hash.put("RECEIPT","RECEIPT");
        //                                System.out.println("hash"+hash);
        //                                lst=ClientUtil.executeQuery("Cash.getAccountList"
        //                                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
        //                            }
        //                            fillData(hash);
        //                        }
        //                    }else{
        //                        ClientUtil.showAlertWindow(" Invalid Number Choose correct number");
        //                        txtAccNo.setText("");
        ////                        txtAccNo.requestFocus();
        //                    }
        //                }else if(observable.getProdType().equals("TD")){
        //                    viewType = ACCNO;
        //                    updateOBFields();
        //                    hash.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
        //                    hash.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
        //                    if(actlst!=null && actlst.size()>0){
        //                        if(observable.getProdType().equals("TD")){
        //                            hash.put("RECEIPT","RECEIPT");
        //                            if(rdoTransactionType_Debit.isSelected()) {
        //                                //                                if(observable.getProdType().equals("TD")){
        //                                //                                    hash.put("PAYMENT","PAYMENT");
        //                                //                                    lst=ClientUtil.executeQuery("Cash.getAccountList"
        //                                //                                    + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
        //                                //                                }else{
        //                                lst=ClientUtil.executeQuery("getDepositHoldersInterest",hash);
        //                                transDetails.setIsDebitSelect(true);
        //                            }else if(rdoTransactionType_Credit.isSelected()){
        //                                lst=ClientUtil.executeQuery("Cash.getAccountList"
        //                                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
        //                            }
        //                            hash.put("PRODUCTTYPE",notClosedMap.get("BEHAVES_LIKE"));
        //                            hash.put("TYPE",notClosedMap.get("BEHAVES_LIKE"));
        //                            hash.put("AMOUNT",notClosedMap.get("DEPOSIT_AMT"));
        //                            fillData(hash);
        //                        }
        //                    }else{
        //                        ClientUtil.showAlertWindow(" Invalid Number Choose correct number");
        //                        txtAccNo.setText("");
        //                    }
        //                }
        //            }else{
        //                ClientUtil.showMessageWindow("Select Payment or Receipt ");
        //                txtAccNo.setText("");
        ////                txtAccNo.requestFocus();
        //                return;
        //            }
        //        }else if(observable.getProdType().equals("OA")){
        //            viewType = ACCNO;
        //            HashMap listMap = new HashMap();
        //            if(observable.getLblAccName().length()>0){
        //                updateOBFields();
        //                hash.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
        //                hash.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
        //                lst=ClientUtil.executeQuery("Cash.getAccountList"
        //                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
        //                fillData(hash);
        //                observable.setLblAccName("");
        //            }else{
        //                ClientUtil.showAlertWindow(" Invalid Number Choose correct number");
        //                txtAccNo.setText("");
        //            }
        //        }
    }//GEN-LAST:event_txtRespBranchActionPerformed

    private void txtRespBranchFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRespBranchFocusLost
        // TODO add your handling code here:
      
    }//GEN-LAST:event_txtRespBranchFocusLost

    private void btnRespBranchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRespBranchActionPerformed
        // Add your handling code here:
         popUp(BRANCH);
    }//GEN-LAST:event_btnRespBranchActionPerformed

    private void txtAdviceNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAdviceNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAdviceNoActionPerformed

    private void txtAdviceNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAdviceNoFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAdviceNoFocusLost

    private void btnAdviceNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdviceNoActionPerformed
        // TODO add your handling code here:
        popUp(ADVICE_NO);
    }//GEN-LAST:event_btnAdviceNoActionPerformed
    private void popUp(int field){
        viewType=field;
        HashMap viewMap=new HashMap();
        HashMap whereMap=new HashMap();
        if(viewType==BRANCH){
            viewMap.put("MAPNAME", "getBranchData");
        }else if(viewType==ADVICE_NO){
             viewMap.put("MAPNAME", "getOrginatingAdviceNo");
             whereMap.put("BRANCH_ID", txtRespBranch.getText());
             whereMap.put("BRANCH", branchID);
             viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        }
        new ViewAll(this, viewMap).show(); 
}
     public void fillData(Object obj) {
     HashMap hmap=(HashMap)obj;
     System.out.println("hmap"+hmap);
     if(viewType==BRANCH){
        String branch=CommonUtil.convertObjToStr(hmap.get("BRANCH_CODE"));
        if(branchID.equals(branch)){
            ClientUtil.displayAlert("Own Branch Id Should not be selected");
            txtRespBranch.setText("");
            lblBranchName.setText("");
            return;
        }
        txtRespBranch.setText(CommonUtil.convertObjToStr(hmap.get("BRANCH_CODE")));
        lblBranchName.setText(CommonUtil.convertObjToStr(hmap.get("BRANCH_NAME")));
     }else if(viewType==ADVICE_NO){
         txtAdviceNo.setText(CommonUtil.convertObjToStr(hmap.get("ADVICE_NO")));
         dtdAdviceDt.setDateValue(CommonUtil.convertObjToStr(hmap.get("ADVICE_DT")));
         cboCategory.setSelectedItem(CommonUtil.convertObjToStr(hmap.get("CATEGORY")));
         txtAmt.setText(CommonUtil.convertObjToStr(hmap.get("AMOUNT")));
         
     }
     
     }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        //        new CheckCustomerIdUI().show();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAdviceNo;
    private com.see.truetransact.uicomponent.CButton btnOk;
    private com.see.truetransact.uicomponent.CButton btnRespBranch;
    private com.see.truetransact.uicomponent.CComboBox cboCategory;
    private com.see.truetransact.uicomponent.CDateField dtdAdviceDt;
    private com.see.truetransact.uicomponent.CLabel lblAdviceDate;
    private com.see.truetransact.uicomponent.CLabel lblAdviceNo;
    private com.see.truetransact.uicomponent.CLabel lblAmount;
    private com.see.truetransact.uicomponent.CLabel lblBranchName;
    private com.see.truetransact.uicomponent.CLabel lblCategory;
    private com.see.truetransact.uicomponent.CLabel lblRespBranchId;
    private com.see.truetransact.uicomponent.CPanel panAcctNo;
    private com.see.truetransact.uicomponent.CPanel panAcctNo1;
    private com.see.truetransact.uicomponent.CPanel panMemberShipFacility;
    private com.see.truetransact.uicomponent.CPanel panTotal1;
    private com.see.truetransact.uicomponent.CTextField txtAdviceNo;
    private com.see.truetransact.uicomponent.CTextField txtAmt;
    private com.see.truetransact.uicomponent.CTextField txtRespBranch;
    // End of variables declaration//GEN-END:variables
    
}
