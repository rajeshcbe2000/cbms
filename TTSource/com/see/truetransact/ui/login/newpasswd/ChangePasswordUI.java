/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ChangePasswordUI.java
 *
 * Created on September 30, 2004, 3:57 PM
 */

package com.see.truetransact.ui.login.newpasswd;

import com.see.truetransact.ui.login.newpasswd.ChangePasswordRB;
import com.see.truetransact.ui.login.newpasswd.ChangePasswordMRB;
import com.see.truetransact.ui.login.newpasswd.ChangePasswordOB;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.login.LoginOB;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.ui.common.passwordrules.PasswordRules;

import java.util.Observable;
import java.util.HashMap;
import java.util.Observer;
import java.util.List;

/**
 *
 * @author  Lohith R.
 */
public class ChangePasswordUI extends com.see.truetransact.uicomponent.CDialog implements Observer,UIMandatoryField{
    
    private ChangePasswordOB observable;
    HashMap mandatoryMap;
    /** Creates new form ChangePasswordUI */
    public ChangePasswordUI() {
        initComponents();
        initStartUP();
    }
    
    /** Initialzation of UI */
    private void initStartUP(){
        setObservable();
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        observable.resetPassword();
        observable.setLblUserName(TrueTransactMain.USERINFO.get("USER_ID").toString());
        lblUserName.setText(observable.getLblUserName());
    }
    
    /** Implementing Singleton pattern */
    private void setObservable() {
        observable = ChangePasswordOB.getInstance();
        observable.addObserver(this);
    }
    
    /** Auto Generated Method - setFieldNames()
     * This method assigns name for all the components.
     * Other functions are working based on this name. */
    private void setFieldNames() {
        btnCancel.setName("btnCancel");
        btnOk.setName("btnOk");
        lblConfirmPasswd.setName("lblConfirmPasswd");
        lblNewPasswd.setName("lblNewPasswd");
        lblOldPasswd.setName("lblOldPasswd");
        lblUserName.setName("lblUserName");
        lblUsrNm.setName("lblUsrNm");
        panButton.setName("panButton");
        panMain.setName("panMain");
        pwdConfirmPasswd.setName("pwdConfirmPasswd");
        pwdNewPasswd.setName("pwdNewPasswd");
        pwdOldPasswd.setName("pwdOldPasswd");
        sptPasswdBtn.setName("sptPasswdBtn");
    }
    
    /** Auto Generated Method - internationalize()
     * This method used to assign display texts from
     * the Resource Bundle File. */
    private void internationalize() {
        ChangePasswordRB resourceBundle = new ChangePasswordRB();
        btnOk.setText(resourceBundle.getString("btnOk"));
        lblUserName.setText(resourceBundle.getString("lblUserName"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblUsrNm.setText(resourceBundle.getString("lblUsrNm"));
        lblNewPasswd.setText(resourceBundle.getString("lblNewPasswd"));
        lblConfirmPasswd.setText(resourceBundle.getString("lblConfirmPasswd"));
        lblOldPasswd.setText(resourceBundle.getString("lblOldPasswd"));
        ((javax.swing.border.TitledBorder)panMain.getBorder()).setTitle(resourceBundle.getString("panMain"));
    }
    
    /** Auto Generated Method - setMandatoryHashMap()
     * This method list out all the Input Fields available in the UI.
     * It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("pwdOldPasswd", new Boolean(true));
        mandatoryMap.put("pwdNewPasswd", new Boolean(true));
        mandatoryMap.put("pwdConfirmPasswd", new Boolean(true));
    }
    
    /** Auto Generated Method - getMandatoryHashMap()
     * Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    /** Auto Generated Method - update()
     * This method called by Observable. It updates the UI with
     * Observable's data. If needed add/Remove RadioButtons
     * method need to be added.*/
    public void update(Observable observed, Object arg) {
        pwdOldPasswd.setText(observable.getPwdOldPasswd());
        pwdNewPasswd.setText(observable.getPwdNewPasswd());
        pwdConfirmPasswd.setText(observable.getPwdConfirmPasswd());
    }
    
    /** Auto Generated Method - updateOBFields()
     * This method called by Save option of UI.
     * It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setPwdOldPasswd(new String(pwdOldPasswd.getPassword()));
        observable.setPwdNewPasswd(new String(pwdNewPasswd.getPassword()));
        observable.setPwdConfirmPasswd(new String(pwdConfirmPasswd.getPassword()));
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        panMain = new com.see.truetransact.uicomponent.CPanel();
        lblConfirmPasswd = new com.see.truetransact.uicomponent.CLabel();
        lblNewPasswd = new com.see.truetransact.uicomponent.CLabel();
        lblOldPasswd = new com.see.truetransact.uicomponent.CLabel();
        lblUserName = new com.see.truetransact.uicomponent.CLabel();
        lblUsrNm = new com.see.truetransact.uicomponent.CLabel();
        pwdOldPasswd = new com.see.truetransact.uicomponent.CPasswordField();
        pwdNewPasswd = new com.see.truetransact.uicomponent.CPasswordField();
        pwdConfirmPasswd = new com.see.truetransact.uicomponent.CPasswordField();
        panButton = new com.see.truetransact.uicomponent.CPanel();
        btnOk = new com.see.truetransact.uicomponent.CButton();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        sptPasswdBtn = new com.see.truetransact.uicomponent.CSeparator();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setTitle("Change Password");
        panMain.setLayout(new java.awt.GridBagLayout());

        panMain.setBorder(new javax.swing.border.TitledBorder(""));
        panMain.setMinimumSize(new java.awt.Dimension(390, 116));
        lblConfirmPasswd.setText("Confirmation Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMain.add(lblConfirmPasswd, gridBagConstraints);

        lblNewPasswd.setText("New Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMain.add(lblNewPasswd, gridBagConstraints);

        lblOldPasswd.setText("Old Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMain.add(lblOldPasswd, gridBagConstraints);

        lblUserName.setMinimumSize(new java.awt.Dimension(200, 15));
        lblUserName.setPreferredSize(new java.awt.Dimension(200, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMain.add(lblUserName, gridBagConstraints);

        lblUsrNm.setText("User Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMain.add(lblUsrNm, gridBagConstraints);

        pwdOldPasswd.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMain.add(pwdOldPasswd, gridBagConstraints);

        pwdNewPasswd.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMain.add(pwdNewPasswd, gridBagConstraints);

        pwdConfirmPasswd.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMain.add(pwdConfirmPasswd, gridBagConstraints);

        panButton.setLayout(new java.awt.GridBagLayout());

        btnOk.setLabel("Ok");
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

        btnCancel.setLabel("Cancel");
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
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMain.add(panButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMain.add(sptPasswdBtn, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(panMain, gridBagConstraints);

        pack();
    }//GEN-END:initComponents
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        observable.resetPassword();
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed
    private void updatePwd() {
        pwdOldPasswd.setText(observable.getPwdOldPasswd());
        pwdNewPasswd.setText(observable.getPwdNewPasswd());
        pwdConfirmPasswd.setText(observable.getPwdConfirmPasswd());
    }
    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        // TODO add your handling code here:
        updateOBFields();
        PasswordRules pwdRules = new PasswordRules();
        boolean passwdChanged = false;
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panMain);
        /* mandatoryMessage length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        if (mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
        }else if (pwdRules.validatePassword(pwdNewPasswd.getText())){
            // password rules not satisfied
        } else {
            passwdChanged = observable.doAction();
            if(passwdChanged == true){
                this.dispose();
            }
        }
        pwdRules = null;
        updatePwd();
        
    }//GEN-LAST:event_btnOkActionPerformed
    
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnOk;
    private com.see.truetransact.uicomponent.CLabel lblConfirmPasswd;
    private com.see.truetransact.uicomponent.CLabel lblNewPasswd;
    private com.see.truetransact.uicomponent.CLabel lblOldPasswd;
    private com.see.truetransact.uicomponent.CLabel lblUserName;
    private com.see.truetransact.uicomponent.CLabel lblUsrNm;
    private com.see.truetransact.uicomponent.CPanel panButton;
    private com.see.truetransact.uicomponent.CPanel panMain;
    private com.see.truetransact.uicomponent.CPasswordField pwdConfirmPasswd;
    private com.see.truetransact.uicomponent.CPasswordField pwdNewPasswd;
    private com.see.truetransact.uicomponent.CPasswordField pwdOldPasswd;
    private com.see.truetransact.uicomponent.CSeparator sptPasswdBtn;
    // End of variables declaration//GEN-END:variables
    
}
