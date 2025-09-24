/*Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemitIssueDuplicateUI.java
 *
 * Created on June 2, 2004, 5:30 PM
 */

package com.see.truetransact.ui.locker.lockeroperation.pwdchange;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Observable;
import com.see.truetransact.ui.locker.lockeroperation.LockerOperationOB;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uicomponent.CDialog;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.commonutil.CommonConstants ;
import com.see.truetransact.commonutil.CommonUtil ;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.common.transaction.TransactionOB;
import java.util.ArrayList;

/**
 *
 * @author  prasath
 *///
public class PwdChangeUI extends com.see.truetransact.uicomponent.CDialog implements java.util.Observer, UIMandatoryField {
    // Variables declaration - do not modify
    boolean isShow = true;
    private  LockerOperationOB  observable;
    private  HashMap mandatoryMap;
    public String rowCnt = "";
    private String drawee = "";
    private String branchCode = "";
    private TransactionUI transactionUI = new TransactionUI();
    private TransactionOB transactionOB = new TransactionOB();
    private String custId = "";
    // End of variables declaration
    
    /** Creates new form RemitIssueDuplicateUI */
    public PwdChangeUI() throws Exception{
        initComponents();
        initStartup();
        //        observable.setTransactionOB(transactionUI.getTransactionOB());
    }
    
    private void initStartup() throws Exception{
        setFieldNames();
        internationalize();
        setObservable();
        initComponentData();
        setMaximumLength();
        setMandatoryHashMap();
        setHelpMessage();
        initializeLabels();
    }
    private void initComponentData() {
//        cboTransactionType.setModel(observable.getCbmTransactionTypeForDuplication());
//        lblTransactionType.setVisible(false);
//        cboTransactionType.setVisible(false);
//        lblDraweeBank.setVisible(false);
//        lblBranchCode.setVisible(false);
//        lblVariableNo.setVisible(false);
//        lblLapsedPeriod.setVisible(false);
//        lblDisplayForDraweeBank.setVisible(false);
//        lblDisplayForBranchCode.setVisible(false);
//        lblDisplayForVariableNo.setVisible(false);
//        lblDisplayLapsedPeriod.setVisible(false);
    }
    private void initializeLabels(){
//        observable.populateLabelFields();
//        observable.setDuplicateDate();
        updateLabels();
        observable.ttNotifyObservers();
    }
    
    private void updateLabels(){
//        lblDisplayForDraweeBank.setText(observable.getLblDraweeBank());
//        lblDisplayForBranchCode.setText(observable.getLblBranchCode());
//        lblDisplayForVariableNo.setText(observable.getLblVariableNo());
//        lblDisplayForDuplicationDate.setText(observable.getLblDuplicationDate());
    }
    
    private void setMaximumLength() {
//        txtDuplicationCharge.setMaxLength(16);
//        txtDuplicationCharge.setValidation(new NumericValidation());
//        txtServiceTax.setMaxLength(16);
//        txtServiceTax.setValidation(new NumericValidation());
//        txtDuplicateRemarks.setMaxLength(1024);
    }
    
  /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnCancel.setName("btnCancel");
        btnOk.setName("btnOk");
//        lblBranchCode.setName("lblBranchCode");
//        lblDisplayForBranchCode.setName("lblDisplayForBranchCode");
//        lblDisplayForDraweeBank.setName("lblDisplayForDraweeBank");
//        lblDisplayForDuplicationDate.setName("lblDisplayForDuplicationDate");
//        lblDisplayForVariableNo.setName("lblDisplayForVariableNo");
//        lblDisplayLapsedPeriod.setName("lblDisplayLapsedPeriod");
//        lblDraweeBank.setName("lblDraweeBank");
//        lblDuplicateRemarks.setName("lblDuplicateRemarks");
//        lblDuplicationDate.setName("lblDuplicationDate");
//        lblLapsedPeriod.setName("lblLapsedPeriod");
        lblMsg.setName("lblMsg");
//        lblTransactionType.setName("lblTransactionType");
        lblNewPwd.setName("lblRevalidationCharge");
        lblConfirmPwd.setName("lblServiceTax");
        lblSpace1.setName("lblSpace1");
        lblStatus.setName("lblStatus");
//        lblVariableNo.setName("lblVariableNo");
        panButton.setName("panButton");
        panDuplicate.setName("panDuplicate");
        panFields.setName("panFields");
        panStatus.setName("panStatus");
//        txtDuplicateRemarks.setName("txtDuplicateRemarks");
        txtNewPwd.setName("txtDuplicationCharge");
        txtConfirmPwd.setName("txtServiceTax");
//        cboTransactionType.setName("cboTransactionType");
    }
    
    
       /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
//        final RemitIssueDuplicateRB resourceBundle = new RemitIssueDuplicateRB();
//        lblLapsedPeriod.setText(resourceBundle.getString("lblLapsedPeriod"));
//        lblBranchCode.setText(resourceBundle.getString("lblBranchCode"));
//        lblMsg.setText(resourceBundle.getString("lblMsg"));
//        lblVariableNo.setText(resourceBundle.getString("lblVariableNo"));
//        btnOk.setText(resourceBundle.getString("btnOk"));
//        lblDraweeBank.setText(resourceBundle.getString("lblDraweeBank"));
//        btnCancel.setText(resourceBundle.getString("btnCancel"));
//        lblDisplayLapsedPeriod.setText(resourceBundle.getString("lblDisplayLapsedPeriod"));
//        lblDisplayForDuplicationDate.setText(resourceBundle.getString("lblDisplayForDuplicationDate"));
//        lblStatus.setText(resourceBundle.getString("lblStatus"));
//        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
//        lblDuplicationDate.setText(resourceBundle.getString("lblDuplicationDate"));
//        lblTransactionType.setText(resourceBundle.getString("lblTransactionType"));
//        lblRevalidationCharge.setText(resourceBundle.getString("lblRevalidationCharge"));
//        lblServiceTax.setText(resourceBundle.getString("lblServiceTax"));
//        lblDuplicateRemarks.setText(resourceBundle.getString("lblDuplicateRemarks"));
//        lblDisplayForVariableNo.setText(resourceBundle.getString("lblDisplayForVariableNo"));
//        lblDisplayForDraweeBank.setText(resourceBundle.getString("lblDisplayForDraweeBank"));
//        lblDisplayForBranchCode.setText(resourceBundle.getString("lblDisplayForBranchCode"));
    }
    
    
    private void setObservable() throws Exception{
        observable = new LockerOperationOB();
        observable.addObserver(this);
    }
    
/* Auto Generated Method - setMandatoryHashMap()
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtDuplicateRemarks", new Boolean(true));
        mandatoryMap.put("txtDuplicationCharge", new Boolean(true));
        mandatoryMap.put("txtServiceTax", new Boolean(true));
        mandatoryMap.put("cboTransactionType", new Boolean(true));
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
//        RemitIssueDuplicateMRB objMandatoryRB = new RemitIssueDuplicateMRB();
//        cboTransactionType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboTransactionType"));
//        txtDuplicateRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDuplicateRemarks"));
//        txtDuplicationCharge.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDuplicationCharge"));
//        txtServiceTax.setHelpMessage(lblMsg, objMandatoryRB.getString("txtServiceTax"));
    }
    
    
    public void show() {
        if (isShow) {
            Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            pack();
            /* Center frame on the screen */
            Dimension frameSize = getSize();
            if (frameSize.height > screenSize.height)
                frameSize.height = screenSize.height;
            if (frameSize.width > screenSize.width)
                frameSize.width = screenSize.width;
            setLocation((screenSize.width - frameSize.width) / 2,
            (screenSize.height - frameSize.height) / 2);
            setModal(true);
            super.show();
        }
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        panDuplicate = new com.see.truetransact.uicomponent.CPanel();
        panButton = new com.see.truetransact.uicomponent.CPanel();
        btnOk = new com.see.truetransact.uicomponent.CButton();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        panFields = new com.see.truetransact.uicomponent.CPanel();
        lblNewPwd = new com.see.truetransact.uicomponent.CLabel();
        txtNewPwd = new com.see.truetransact.uicomponent.CTextField();
        txtConfirmPwd = new com.see.truetransact.uicomponent.CTextField();
        lblConfirmPwd = new com.see.truetransact.uicomponent.CLabel();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();

        panDuplicate.setLayout(new java.awt.GridBagLayout());

        panDuplicate.setMinimumSize(new java.awt.Dimension(300, 250));
        panDuplicate.setPreferredSize(new java.awt.Dimension(300, 250));
        panButton.setLayout(new java.awt.GridBagLayout());

        btnOk.setText("Ok");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(btnOk, gridBagConstraints);

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(btnCancel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panDuplicate.add(panButton, gridBagConstraints);

        panFields.setLayout(new java.awt.GridBagLayout());

        panFields.setMinimumSize(new java.awt.Dimension(280, 200));
        panFields.setPreferredSize(new java.awt.Dimension(280, 270));
        lblNewPwd.setText("New Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFields.add(lblNewPwd, gridBagConstraints);

        txtNewPwd.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNewPwd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNewPwdFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFields.add(txtNewPwd, gridBagConstraints);

        txtConfirmPwd.setMinimumSize(new java.awt.Dimension(100, 21));
        txtConfirmPwd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtConfirmPwdFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFields.add(txtConfirmPwd, gridBagConstraints);

        lblConfirmPwd.setText("Confirm Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFields.add(lblConfirmPwd, gridBagConstraints);

        panDuplicate.add(panFields, new java.awt.GridBagConstraints());

        getContentPane().add(panDuplicate, java.awt.BorderLayout.CENTER);

        panStatus.setLayout(new java.awt.GridBagLayout());

        panStatus.setMinimumSize(new java.awt.Dimension(310, 20));
        panStatus.setPreferredSize(new java.awt.Dimension(310, 20));
        lblSpace1.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace1, gridBagConstraints);

        lblStatus.setBorder(new javax.swing.border.EtchedBorder());
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

    }//GEN-END:initComponents

    private void txtConfirmPwdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtConfirmPwdFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtConfirmPwdFocusLost

    private void txtNewPwdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNewPwdFocusLost
        // TODO add your handling code here:
//        observable.setTxtServiceTax(observable.calServiceTax(txtDuplicationCharge.getText(),lblDisplayForBranchCode.getText(),lblDisplayForVariableNo.getText(),lblDisplayLapsedPeriod.getText(), "DUPLICATE_CHARGE",drawee,branchCode));
//        txtConfirmPwd.setText(observable.getTxtServiceTax());
    }//GEN-LAST:event_txtNewPwdFocusLost
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
//        observable.setDup(11);
//        observable.updateDuplicateDetails();
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed
    /* To display an alert message if any of the mandatory fields is not inputed */
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        // Add your handling code here:
        //        TransactionUI tranUI = new TransactionUI();
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panFields);
        System.out.println("In mandatoryMessage "+mandatoryMessage);
        if (mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
        }else{
            updateOBFields();
            observable.setNewPwdDetails(getCustId());
//            observable.updateTOsWithDuplication(observable.getSelectRow()); 
//            String rowCount =  getRowCnt();
//            ArrayList aryLst = new ArrayList();
//            aryLst.
//            observable.updateTOsWithDuplication(rowCount);
//            observable.setOperationMode(CommonConstants.REMIT_DUPLICATE) ;
//            observable.setDup(-1);
            //             transactionUI.setCallingAmount(observable.getTxtDuplicationCharge());
            //            transactionOB.setTxtTransactionAmt(observable.getTxtDuplicationCharge());
            //            transactionUI.setCallingAmount(observable.getTxtDuplicationCharge());
            //            transactionUI.setCallingParams();
            //            tranUI.cancelAction(false);
            this.dispose();
            
            
        }
    }//GEN-LAST:event_btnOkActionPerformed
    
    /* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
//        cboTransactionType.setSelectedItem(observable.getCboDuplicateTransType());
//        txtDuplicateRemarks.setText(observable.getTxtDuplicateRemarks());
        txtNewPwd.setText(observable.getTxtNewPwd());
        txtConfirmPwd.setText(observable.getTxtConPwd());
//        lblDisplayLapsedPeriod.setText(observable.getLblLapsePeriod());
        lblStatus.setText(observable.getLblStatus());
    }
    
    public void setCharges(String dupCharge, String serviceTax, String prodID, String category, String amount, String payAt,String DraweeBank,String BranchCode){
        txtNewPwd.setText(dupCharge);
        txtConfirmPwd.setText(serviceTax);
//         lblDisplayForBranchCode.setText(prodID);
//        lblDisplayForVariableNo.setText(category);
//        lblDisplayLapsedPeriod.setText(amount);
//        observable.setPayableAt(payAt);
        drawee = DraweeBank;
        branchCode = BranchCode;
        
    }
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
//        observable.setCboDuplicateTransType((String) cboTransactionType.getSelectedItem());
//        observable.setTxtDuplicateRemarks(txtDuplicateRemarks.getText());
        observable.setTxtNewPwd(txtNewPwd.getText());
        observable.setTxtConPwd(txtConfirmPwd.getText());
        observable.setTxtCustId(getCustId());
        
//        observable.setTxtTotalAmt(String.valueOf(
//        CommonUtil.convertObjToDouble(observable.getTxtTotalAmt()).doubleValue()
//        + CommonUtil.convertObjToDouble(txtDuplicationCharge.getText()).doubleValue()));
    }
    
    /**
     * Getter for property rowCnt.
     * @return Value of property rowCnt.
     */
    public String getRowCnt() {
        return rowCnt;
    }
    
    /**
     * Setter for property rowCnt.
     * @param rowCnt New value of property rowCnt.
     */
    public void setRowCnt(String rowCnt) {
        this.rowCnt = rowCnt;
    }
    
    /**
     * Getter for property custId.
     * @return Value of property custId.
     */
    public java.lang.String getCustId() {
        return custId;
    }
    
    /**
     * Setter for property custId.
     * @param custId New value of property custId.
     */
    public void setCustId(java.lang.String custId) {
        this.custId = custId;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnOk;
    private com.see.truetransact.uicomponent.CLabel lblConfirmPwd;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNewPwd;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CPanel panButton;
    private com.see.truetransact.uicomponent.CPanel panDuplicate;
    private com.see.truetransact.uicomponent.CPanel panFields;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CTextField txtConfirmPwd;
    private com.see.truetransact.uicomponent.CTextField txtNewPwd;
    // End of variables declaration//GEN-END:variables
    
}
