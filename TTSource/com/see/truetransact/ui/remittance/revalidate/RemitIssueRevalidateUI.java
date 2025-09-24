/*Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemitIssueRevalidateUI.java
 *
 * Created on June 2, 2004, 5:31 PM
 */

package com.see.truetransact.ui.remittance.revalidate;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Observable;
import com.see.truetransact.ui.remittance.RemittanceIssueOB;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uicomponent.CDialog;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.commonutil.CommonConstants ;
import com.see.truetransact.commonutil.CommonUtil ;

/**
 *
 * @author  prasath CInternalFrame CDialog UIMandatoryField
 */

public class RemitIssueRevalidateUI extends com.see.truetransact.uicomponent.CDialog implements java.util.Observer, UIMandatoryField {
    
    // Variables declaration - do not modify
    boolean isShow = true;
    public String rowCnt = "";
    private RemittanceIssueOB observable;
    private HashMap mandatoryMap;
    private String payable = "";
    private String drawee = "";
    private String branchCode = "";
    // End of variables declaration
    
    /** Creates new form RemitIssueRevalidateUI */
    public RemitIssueRevalidateUI() throws Exception{
        initComponents();
        initStartup();
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
        tdtDOExpiring.setVisible(false);
        lblDOExpiring.setVisible(false);
        lblDraweeBank.setVisible(false);
        lblDisplayForDraweeBank.setVisible(false);
//        lblBranchCode.setVisible(false);
//        lblDisplayForBranchCode.setVisible(false);
//        lblVariableNo.setVisible(false);
//        lblDisplayForVariableNo.setVisible(false);
//        lblLapsedPeriod.setVisible(false);
//        lblDisplayLapsedPeriod.setVisible(false);
    }
    
    private void setMaximumLength(){
        txtRevalidateRemarks.setMaxLength(1024);
        txtRevalidationCharge.setMaxLength(16);
        txtRevalidationCharge.setValidation(new NumericValidation());
        txtServiceTax.setMaxLength(16);
        txtServiceTax.setValidation(new NumericValidation());
    }
    
/* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnCancel.setName("btnCancel");
        btnOk.setName("btnOk");
        cboTransactionType.setName("cboTransactionType");
        lblBranchCode.setName("lblBranchCode");
        lblDOExpiring.setName("lblDOExpiring");
        lblDisplayForBranchCode.setName("lblDisplayForBranchCode");
        lblDisplayForDraweeBank.setName("lblDisplayForDraweeBank");
        lblDisplayForRevalidationDate.setName("lblDisplayForRevalidationDate");
        lblDisplayForVariableNo.setName("lblDisplayForVariableNo");
        lblDisplayLapsedPeriod.setName("lblDisplayLapsedPeriod");
        lblDraweeBank.setName("lblDraweeBank");
        lblLapsedPeriod.setName("lblLapsedPeriod");
        lblMsg.setName("lblMsg");
        lblRevalidateRemarks.setName("lblRevalidateRemarks");
        lblTransactionType.setName("lblTransactionType");
        lblRevalidationCharge.setName("lblRevalidationCharge");
        lblRevalidationDate.setName("lblRevalidationDate");
        lblSpace1.setName("lblSpace1");
        lblStatus.setName("lblStatus");
        lblVariableNo.setName("lblVariableNo");
        panButton.setName("panButton");
        panFields.setName("panFields");
        panRevalidate.setName("panRevalidate");
        panStatus.setName("panStatus");
        tdtDOExpiring.setName("tdtDOExpiring");
        txtRevalidateRemarks.setName("txtRevalidateRemarks");
        txtRevalidationCharge.setName("txtRevalidationCharge");
    }
    
    
    
    
        /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        final RemitIssueRevalidateRB resourceBundle = new RemitIssueRevalidateRB();
        lblLapsedPeriod.setText(resourceBundle.getString("lblLapsedPeriod"));
        lblRevalidateRemarks.setText(resourceBundle.getString("lblRevalidateRemarks"));
        lblRevalidationDate.setText(resourceBundle.getString("lblRevalidationDate"));
        lblBranchCode.setText(resourceBundle.getString("lblBranchCode"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblVariableNo.setText(resourceBundle.getString("lblVariableNo"));
        btnOk.setText(resourceBundle.getString("btnOk"));
        lblDraweeBank.setText(resourceBundle.getString("lblDraweeBank"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblDisplayLapsedPeriod.setText(resourceBundle.getString("lblDisplayLapsedPeriod"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblTransactionType.setText(resourceBundle.getString("lblTransactionType"));
        lblRevalidationCharge.setText(resourceBundle.getString("lblRevalidationCharge"));
        lblDisplayForRevalidationDate.setText(resourceBundle.getString("lblDisplayForRevalidationDate"));
        lblDisplayForVariableNo.setText(resourceBundle.getString("lblDisplayForVariableNo"));
        lblDisplayForDraweeBank.setText(resourceBundle.getString("lblDisplayForDraweeBank"));
        lblDOExpiring.setText(resourceBundle.getString("lblDOExpiring"));
        lblDisplayForBranchCode.setText(resourceBundle.getString("lblDisplayForBranchCode"));
    }
    
    private void setObservable() throws Exception{
        observable = new RemittanceIssueOB();
        observable.addObserver(this);
    }
    
    /* Auto Generated Method - setMandatoryHashMap()
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtRevalidationCharge", new Boolean(true));
        mandatoryMap.put("tdtDOExpiring", new Boolean(true));
        mandatoryMap.put("txtRevalidateRemarks", new Boolean(true));
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
        RemitIssueRevalidateMRB objMandatoryRB = new RemitIssueRevalidateMRB();
        cboTransactionType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboTransactionType"));
        txtRevalidationCharge.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRevalidationCharge"));
        tdtDOExpiring.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDOExpiring"));
        txtRevalidateRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRevalidateRemarks"));
    }
     private void initComponentData() {        
        cboTransactionType.setModel(observable.getCbmTransactionTypeForRevalidation());
        lblTransactionType.setVisible(false);
        cboTransactionType.setVisible(false);
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

        panRevalidate = new com.see.truetransact.uicomponent.CPanel();
        panButton = new com.see.truetransact.uicomponent.CPanel();
        btnOk = new com.see.truetransact.uicomponent.CButton();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        panFields = new com.see.truetransact.uicomponent.CPanel();
        lblDraweeBank = new com.see.truetransact.uicomponent.CLabel();
        lblDisplayForDraweeBank = new com.see.truetransact.uicomponent.CLabel();
        lblBranchCode = new com.see.truetransact.uicomponent.CLabel();
        lblDisplayForBranchCode = new com.see.truetransact.uicomponent.CLabel();
        lblVariableNo = new com.see.truetransact.uicomponent.CLabel();
        lblDisplayForVariableNo = new com.see.truetransact.uicomponent.CLabel();
        lblRevalidationDate = new com.see.truetransact.uicomponent.CLabel();
        lblDisplayForRevalidationDate = new com.see.truetransact.uicomponent.CLabel();
        lblRevalidationCharge = new com.see.truetransact.uicomponent.CLabel();
        txtRevalidationCharge = new com.see.truetransact.uicomponent.CTextField();
        lblDOExpiring = new com.see.truetransact.uicomponent.CLabel();
        tdtDOExpiring = new com.see.truetransact.uicomponent.CDateField();
        lblRevalidateRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtRevalidateRemarks = new com.see.truetransact.uicomponent.CTextField();
        lblLapsedPeriod = new com.see.truetransact.uicomponent.CLabel();
        lblDisplayLapsedPeriod = new com.see.truetransact.uicomponent.CLabel();
        lblTransactionType = new com.see.truetransact.uicomponent.CLabel();
        cboTransactionType = new com.see.truetransact.uicomponent.CComboBox();
        txtServiceTax = new com.see.truetransact.uicomponent.CTextField();
        lblServiceTax = new com.see.truetransact.uicomponent.CLabel();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();

        panRevalidate.setLayout(new java.awt.GridBagLayout());

        panRevalidate.setMinimumSize(new java.awt.Dimension(310, 270));
        panRevalidate.setPreferredSize(new java.awt.Dimension(310, 270));
        panButton.setLayout(new java.awt.GridBagLayout());

        panButton.setMinimumSize(new java.awt.Dimension(144, 34));
        panButton.setPreferredSize(new java.awt.Dimension(144, 34));
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
        panRevalidate.add(panButton, gridBagConstraints);

        panFields.setLayout(new java.awt.GridBagLayout());

        panFields.setMinimumSize(new java.awt.Dimension(300, 238));
        panFields.setPreferredSize(new java.awt.Dimension(300, 238));
        lblDraweeBank.setText("Drawee Bank");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFields.add(lblDraweeBank, gridBagConstraints);

        lblDisplayForDraweeBank.setText("Test");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFields.add(lblDisplayForDraweeBank, gridBagConstraints);

        lblBranchCode.setText("Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFields.add(lblBranchCode, gridBagConstraints);

        lblDisplayForBranchCode.setText("Test");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFields.add(lblDisplayForBranchCode, gridBagConstraints);

        lblVariableNo.setText("Variable No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFields.add(lblVariableNo, gridBagConstraints);

        lblDisplayForVariableNo.setText("Test");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFields.add(lblDisplayForVariableNo, gridBagConstraints);

        lblRevalidationDate.setText("Revalidation Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFields.add(lblRevalidationDate, gridBagConstraints);

        lblDisplayForRevalidationDate.setText("Test");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFields.add(lblDisplayForRevalidationDate, gridBagConstraints);

        lblRevalidationCharge.setText("Revalidation Charge");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFields.add(lblRevalidationCharge, gridBagConstraints);

        txtRevalidationCharge.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRevalidationCharge.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRevalidationChargeFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFields.add(txtRevalidationCharge, gridBagConstraints);

        lblDOExpiring.setText("Date Of Expiring");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFields.add(lblDOExpiring, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFields.add(tdtDOExpiring, gridBagConstraints);

        lblRevalidateRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFields.add(lblRevalidateRemarks, gridBagConstraints);

        txtRevalidateRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFields.add(txtRevalidateRemarks, gridBagConstraints);

        lblLapsedPeriod.setText("Lapsed Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFields.add(lblLapsedPeriod, gridBagConstraints);

        lblDisplayLapsedPeriod.setText("Test");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFields.add(lblDisplayLapsedPeriod, gridBagConstraints);

        lblTransactionType.setText("Transaction Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFields.add(lblTransactionType, gridBagConstraints);

        cboTransactionType.setMaximumSize(new java.awt.Dimension(100, 21));
        cboTransactionType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFields.add(cboTransactionType, gridBagConstraints);

        txtServiceTax.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFields.add(txtServiceTax, gridBagConstraints);

        lblServiceTax.setText("Service Tax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFields.add(lblServiceTax, gridBagConstraints);

        panRevalidate.add(panFields, new java.awt.GridBagConstraints());

        getContentPane().add(panRevalidate, java.awt.BorderLayout.CENTER);

        panStatus.setLayout(new java.awt.GridBagLayout());

        panStatus.setMinimumSize(new java.awt.Dimension(320, 20));
        panStatus.setPreferredSize(new java.awt.Dimension(320, 20));
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

    private void txtRevalidationChargeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRevalidationChargeFocusLost
        // TODO add your handling code here:
        observable.setTxtServiceTax(observable.calServiceTax(txtRevalidationCharge.getText(),lblDisplayForBranchCode.getText(),lblDisplayForVariableNo.getText(),lblDisplayLapsedPeriod.getText(), "REVALIDATE_CHARGE",drawee,branchCode));
        txtServiceTax.setText(observable.getTxtServiceTax());
    }//GEN-LAST:event_txtRevalidationChargeFocusLost
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
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
//        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panFields);        
//        if (mandatoryMessage.length() > 0){            
//            displayAlert(mandatoryMessage);
//        }else{
//            updateOBFields(); 
//            observable.updateTOsWithRevalidation(observable.getSelectRow());
//            observable.setOperationMode(CommonConstants.REMIT_REVALIDATE) ;
//            observable.ttNotifyObservers();
//            this.dispose();
//        }
          // Add your handling code here:
        //        TransactionUI tranUI = new TransactionUI();
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panFields);
        System.out.println("In duplicate mandatoryMessage "+mandatoryMessage);
        if (mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
        }else{
            updateOBFields();
//            observable.updateTOsWithDuplication(observable.getSelectRow()); 
            String rowCount =  getRowCnt();
            observable.updateTOsWithRevalidation(rowCount);
//            observable.setOperationMode(CommonConstants.REMIT_DUPLICATE) ;
            observable.setDup(-1);
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
        cboTransactionType.setSelectedItem(observable.getCboRevalidateTransType());
        txtRevalidationCharge.setText(observable.getTxtRevalidationCharge());
        tdtDOExpiring.setDateValue(observable.getTdtDOExpiring());
        txtRevalidateRemarks.setText(observable.getTxtRevalidateRemarks());
        lblStatus.setText(observable.getLblStatus());
    }
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setCboRevalidateTransType((String) cboTransactionType.getSelectedItem());
        observable.setTxtRevalidationCharge(txtRevalidationCharge.getText());
        observable.setTxtTotalAmt(String.valueOf(
            CommonUtil.convertObjToDouble(observable.getTxtTotalAmt()).doubleValue() 
            + CommonUtil.convertObjToDouble(txtRevalidationCharge.getText()).doubleValue()));
        observable.setTdtDOExpiring(tdtDOExpiring.getDateValue());
        observable.setTxtRevalidateRemarks(txtRevalidateRemarks.getText());
        observable.setTxtServiceTax(txtServiceTax.getText());
    }
    
    private void initializeLabels(){
        observable.populateLabelFields();
        observable.setRevalidDate();
        updateLabels();
        observable.ttNotifyObservers();
    }
    
    private void updateLabels(){
        lblDisplayForDraweeBank.setText(observable.getLblDraweeBank());
        lblDisplayForBranchCode.setText(observable.getLblBranchCode());
        lblDisplayForVariableNo.setText(observable.getLblVariableNo());
        lblDisplayForRevalidationDate.setText(observable.getLblRevalidationDate());
        lblDisplayLapsedPeriod.setText(observable.getLblLapsePeriod());
    }
    public void setCharges(String revCharge, String serviceTax, String prodID, String category, String amount, String payAt,String DraweeBank,String BranchCode){
        txtRevalidationCharge.setText(revCharge);
        txtServiceTax.setText(serviceTax);
        lblDisplayForBranchCode.setText(prodID);
        lblDisplayForVariableNo.setText(category);
        lblDisplayLapsedPeriod.setText(amount);
        observable.setPayableAt(payAt);
        drawee = DraweeBank;
        branchCode = BranchCode;
//        lblDisplayForRevalidationDate.setText(amount);
    }
    
    /**
     * Getter for property rowCnt.
     * @return Value of property rowCnt.
     */
    public java.lang.String getRowCnt() {
        return rowCnt;
    }
    
    /**
     * Setter for property rowCnt.
     * @param rowCnt New value of property rowCnt.
     */
    public void setRowCnt(java.lang.String rowCnt) {
        this.rowCnt = rowCnt;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnOk;
    private com.see.truetransact.uicomponent.CComboBox cboTransactionType;
    private com.see.truetransact.uicomponent.CLabel lblBranchCode;
    private com.see.truetransact.uicomponent.CLabel lblDOExpiring;
    private com.see.truetransact.uicomponent.CLabel lblDisplayForBranchCode;
    private com.see.truetransact.uicomponent.CLabel lblDisplayForDraweeBank;
    private com.see.truetransact.uicomponent.CLabel lblDisplayForRevalidationDate;
    private com.see.truetransact.uicomponent.CLabel lblDisplayForVariableNo;
    private com.see.truetransact.uicomponent.CLabel lblDisplayLapsedPeriod;
    private com.see.truetransact.uicomponent.CLabel lblDraweeBank;
    private com.see.truetransact.uicomponent.CLabel lblLapsedPeriod;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblRevalidateRemarks;
    private com.see.truetransact.uicomponent.CLabel lblRevalidationCharge;
    private com.see.truetransact.uicomponent.CLabel lblRevalidationDate;
    private com.see.truetransact.uicomponent.CLabel lblServiceTax;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTransactionType;
    private com.see.truetransact.uicomponent.CLabel lblVariableNo;
    private com.see.truetransact.uicomponent.CPanel panButton;
    private com.see.truetransact.uicomponent.CPanel panFields;
    private com.see.truetransact.uicomponent.CPanel panRevalidate;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CDateField tdtDOExpiring;
    private com.see.truetransact.uicomponent.CTextField txtRevalidateRemarks;
    private com.see.truetransact.uicomponent.CTextField txtRevalidationCharge;
    private com.see.truetransact.uicomponent.CTextField txtServiceTax;
    // End of variables declaration//GEN-END:variables
    
}
