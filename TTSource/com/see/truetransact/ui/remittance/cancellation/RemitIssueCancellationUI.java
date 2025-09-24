/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * RemitIssueCancellationUI.java
 *
 * Created on June 10, 2004, 4:05 PM
 */

package com.see.truetransact.ui.remittance.cancellation;
import java.awt.Dimension;
import java.util.Observable;
import java.util.HashMap;
import com.see.truetransact.ui.remittance.RemittanceIssueOB;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uicomponent.CDialog;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uivalidation.NumericValidation;

/**
 *
 * @author  prasath
 */
public class RemitIssueCancellationUI extends com.see.truetransact.uicomponent.CDialog implements java.util.Observer,UIMandatoryField {
    // Variables declaration - do not modify
    boolean isShow = true;
    private  RemittanceIssueOB  observable;
    private  HashMap mandatoryMap;
    final int CANCELLATION = 0;
    // End of variables declaration
    
    /** Creates new form RemitIssueCancellationUI */
    public RemitIssueCancellationUI() throws Exception{
        initComponents();
        initStartup();
    }
    private void initStartup() throws Exception {
        setFieldNames();
        internationalize();
        setObservable();
        setMaximumLength();
        setMandatoryHashMap();
        setHelpMessage();
        initializeLabels();
    }
    private void setMaximumLength() {
        txtCancellationCharge.setMaxLength(16);
        txtCancellationCharge.setValidation(new NumericValidation());
        txtCancellationRemarks.setMaxLength(1024);
    }
    private void initializeLabels(){
        observable.populateLabelFields();
//        observable.setCancelDate();
        updateLabels();
        observable.ttNotifyObservers();
    }
    private void updateLabels(){
        lblDisplayForDraweeBank.setText(observable.getLblDraweeBank());
        lblDisplayForBranchCode.setText(observable.getLblBranchCode());
        lblDisplayForVariableNo.setText(observable.getLblVariableNo());
//        lblDisplayForCancellationDate.setText(observable.getLblCancellationDate());
    }
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnCancel.setName("btnCancel");
        btnOk.setName("btnOk");
        lblBranchCode.setName("lblBranchCode");
        lblCancellationCharge.setName("lblCancellationCharge");
        lblCancellationDate.setName("lblCancellationDate");
        lblCancellationRemarks.setName("lblCancellationRemarks");
        lblDisplayForBranchCode.setName("lblDisplayForBranchCode");
        lblDisplayForCancellationDate.setName("lblDisplayForCancellationDate");
        lblDisplayForDraweeBank.setName("lblDisplayForDraweeBank");
        lblDisplayForVariableNo.setName("lblDisplayForVariableNo");
        lblDraweeBank.setName("lblDraweeBank");
        lblMsg.setName("lblMsg");
        lblSpace1.setName("lblSpace1");
        lblStatus.setName("lblStatus");
        lblVariableNo.setName("lblVariableNo");
        panButton.setName("panButton");
        panCancellation.setName("panCancellation");
        panFields.setName("panFields");
        panStatus.setName("panStatus");
        txtCancellationCharge.setName("txtCancellationCharge");
        txtCancellationRemarks.setName("txtCancellationRemarks");
    }
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        final RemitIssueCancellationRB resourceBundle = new RemitIssueCancellationRB();
        lblDisplayForCancellationDate.setText(resourceBundle.getString("lblDisplayForCancellationDate"));
        lblCancellationRemarks.setText(resourceBundle.getString("lblCancellationRemarks"));
        lblBranchCode.setText(resourceBundle.getString("lblBranchCode"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblVariableNo.setText(resourceBundle.getString("lblVariableNo"));
        btnOk.setText(resourceBundle.getString("btnOk"));
        lblDraweeBank.setText(resourceBundle.getString("lblDraweeBank"));
        lblCancellationCharge.setText(resourceBundle.getString("lblCancellationCharge"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblCancellationDate.setText(resourceBundle.getString("lblCancellationDate"));
        lblDisplayForDraweeBank.setText(resourceBundle.getString("lblDisplayForDraweeBank"));
        lblDisplayForVariableNo.setText(resourceBundle.getString("lblDisplayForVariableNo"));
        lblDisplayForBranchCode.setText(resourceBundle.getString("lblDisplayForBranchCode"));
    }
    
/* Auto Generated Method - setMandatoryHashMap()
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtCancellationCharge", new Boolean(true));
        mandatoryMap.put("txtCancellationRemarks", new Boolean(true));
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
        RemitIssueCancellationMRB objMandatoryRB = new RemitIssueCancellationMRB();
        txtCancellationCharge.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCancellationCharge"));
        txtCancellationRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCancellationRemarks"));
    }
    
    private void setObservable() throws Exception{
        observable = new RemittanceIssueOB();
        observable.addObserver(this);
    }
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
//        observable.setTxtCancellationCharge(txtCancellationCharge.getText());
//        observable.setTxtCancellationRemarks(txtCancellationRemarks.getText());
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

        panCancellation = new com.see.truetransact.uicomponent.CPanel();
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
        lblCancellationDate = new com.see.truetransact.uicomponent.CLabel();
        lblDisplayForCancellationDate = new com.see.truetransact.uicomponent.CLabel();
        lblCancellationCharge = new com.see.truetransact.uicomponent.CLabel();
        txtCancellationCharge = new com.see.truetransact.uicomponent.CTextField();
        lblCancellationRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtCancellationRemarks = new com.see.truetransact.uicomponent.CTextField();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();

        panCancellation.setLayout(new java.awt.GridBagLayout());

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
        panCancellation.add(panButton, gridBagConstraints);

        panFields.setLayout(new java.awt.GridBagLayout());

        lblDraweeBank.setText("Drawee Bank");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panFields.add(lblDraweeBank, gridBagConstraints);

        lblDisplayForDraweeBank.setText("Test");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panFields.add(lblDisplayForDraweeBank, gridBagConstraints);

        lblBranchCode.setText("Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panFields.add(lblBranchCode, gridBagConstraints);

        lblDisplayForBranchCode.setText("Test");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panFields.add(lblDisplayForBranchCode, gridBagConstraints);

        lblVariableNo.setText("Variable No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panFields.add(lblVariableNo, gridBagConstraints);

        lblDisplayForVariableNo.setText("Test");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panFields.add(lblDisplayForVariableNo, gridBagConstraints);

        lblCancellationDate.setText("Cancellation Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panFields.add(lblCancellationDate, gridBagConstraints);

        lblDisplayForCancellationDate.setText("Test");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panFields.add(lblDisplayForCancellationDate, gridBagConstraints);

        lblCancellationCharge.setText("Cancellation Charge");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panFields.add(lblCancellationCharge, gridBagConstraints);

        txtCancellationCharge.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panFields.add(txtCancellationCharge, gridBagConstraints);

        lblCancellationRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panFields.add(lblCancellationRemarks, gridBagConstraints);

        txtCancellationRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panFields.add(txtCancellationRemarks, gridBagConstraints);

        panCancellation.add(panFields, new java.awt.GridBagConstraints());

        getContentPane().add(panCancellation, java.awt.BorderLayout.CENTER);

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
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panFields);
        if (mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
        }else{
            updateOBFields();
//            observable.setType(CANCELLATION);
            this.dispose();
            
        }
    }//GEN-LAST:event_btnOkActionPerformed
    
    /* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
//        txtCancellationCharge.setText(observable.getTxtCancellationCharge());
//        txtCancellationRemarks.setText(observable.getTxtCancellationRemarks());
        lblStatus.setText(observable.getLblStatus());
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnOk;
    private com.see.truetransact.uicomponent.CLabel lblBranchCode;
    private com.see.truetransact.uicomponent.CLabel lblCancellationCharge;
    private com.see.truetransact.uicomponent.CLabel lblCancellationDate;
    private com.see.truetransact.uicomponent.CLabel lblCancellationRemarks;
    private com.see.truetransact.uicomponent.CLabel lblDisplayForBranchCode;
    private com.see.truetransact.uicomponent.CLabel lblDisplayForCancellationDate;
    private com.see.truetransact.uicomponent.CLabel lblDisplayForDraweeBank;
    private com.see.truetransact.uicomponent.CLabel lblDisplayForVariableNo;
    private com.see.truetransact.uicomponent.CLabel lblDraweeBank;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblVariableNo;
    private com.see.truetransact.uicomponent.CPanel panButton;
    private com.see.truetransact.uicomponent.CPanel panCancellation;
    private com.see.truetransact.uicomponent.CPanel panFields;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CTextField txtCancellationCharge;
    private com.see.truetransact.uicomponent.CTextField txtCancellationRemarks;
    // End of variables declaration//GEN-END:variables
    
}
