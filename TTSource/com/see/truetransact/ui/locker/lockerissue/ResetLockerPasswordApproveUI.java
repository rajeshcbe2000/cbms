/*

 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ResetLockerPasswordApproveUI.java
 *
 * Created on May 13, 2014, 10:10 PM
 */

package com.see.truetransact.ui.locker.lockerissue;

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
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.DefaultValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.transaction.rollback.RollBackUI;
import com.see.truetransact.commonutil.StringEncrypter;
/**
 *
 * @author  Shihad
 */
public class ResetLockerPasswordApproveUI extends com.see.truetransact.uicomponent.CDialog implements Observer {
    
    Date currDt = null;
    public String branchID;
    private StringEncrypter encrypt=null;
    private boolean cancelActionKey=false;
    private boolean loginTrue=false;
    public ResetLockerPasswordApproveUI(LockerIssueUI LockerIssueUI) {
         super(LockerIssueUI,false);
    try{
       
        initComponents();
        initForm();
        encrypt=new StringEncrypter();
        show();
    }catch (Exception e){
        e.printStackTrace();
    }
       
    }
    
    /** Method which is used to initialize the form TokenConfig */
    private void initForm(){
        setMaxLengths();
        setFieldNames();
        internationalize();
        currDt = ClientUtil.getCurrentDate();
    }
    
    private void setupScreen() {
        setModal(true);
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
    
 
    public void show() {     
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


    /** Used to set Maximum possible lenghts for TextFields */
    private void setMaxLengths(){
        
    }
    
    
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        panMemberShipFacility.setName("panMemberShipFacility");
        panRejection.setName("panRejection");
        lblUserId.setName("lblUserId");
        txtUserId.setName("txtUserId");
        btnLogin.setName("btnLogin");
        btnCancel.setName("btnCancel");
        panTotal.setName("panTotal");
        panRejectionUI.setName("panRejectionUI");
    }
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
        panRejectionUI = new com.see.truetransact.uicomponent.CPanel();
        panTotal = new com.see.truetransact.uicomponent.CPanel();
        btnLogin = new com.see.truetransact.uicomponent.CButton();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        panRejection = new com.see.truetransact.uicomponent.CPanel();
        lblUserId = new com.see.truetransact.uicomponent.CLabel();
        lblRecieptAmt = new com.see.truetransact.uicomponent.CLabel();
        txtUserId = new com.see.truetransact.uicomponent.CTextField();
        txtPassword = new com.see.truetransact.uicomponent.CPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panMemberShipFacility.setMaximumSize(new java.awt.Dimension(400, 200));
        panMemberShipFacility.setMinimumSize(new java.awt.Dimension(400, 200));
        panMemberShipFacility.setPreferredSize(new java.awt.Dimension(400, 200));
        panMemberShipFacility.setLayout(new java.awt.GridBagLayout());

        panRejectionUI.setMinimumSize(new java.awt.Dimension(300, 225));
        panRejectionUI.setPreferredSize(new java.awt.Dimension(300, 225));
        panRejectionUI.setLayout(new java.awt.GridBagLayout());

        panTotal.setMaximumSize(new java.awt.Dimension(450, 25));
        panTotal.setMinimumSize(new java.awt.Dimension(450, 25));
        panTotal.setPreferredSize(new java.awt.Dimension(200, 25));
        panTotal.setLayout(new java.awt.GridBagLayout());

        btnLogin.setText("Login");
        btnLogin.setNextFocusableComponent(btnCancel);
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        panTotal.add(btnLogin, gridBagConstraints);

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        panTotal.add(btnCancel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 42, 0, 42);
        panRejectionUI.add(panTotal, gridBagConstraints);

        panRejection.setBorder(javax.swing.BorderFactory.createTitledBorder("Password Reset"));
        panRejection.setMaximumSize(new java.awt.Dimension(300, 120));
        panRejection.setMinimumSize(new java.awt.Dimension(300, 120));
        panRejection.setPreferredSize(new java.awt.Dimension(300, 120));
        panRejection.setLayout(new java.awt.GridBagLayout());

        lblUserId.setText("User Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        panRejection.add(lblUserId, gridBagConstraints);

        lblRecieptAmt.setText("Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        panRejection.add(lblRecieptAmt, gridBagConstraints);

        txtUserId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtUserId.setNextFocusableComponent(txtPassword);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        panRejection.add(txtUserId, gridBagConstraints);

        txtPassword.setNextFocusableComponent(btnLogin);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panRejection.add(txtPassword, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 38, 3, 41);
        panRejectionUI.add(panRejection, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 12, 0, 12);
        panMemberShipFacility.add(panRejectionUI, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(panMemberShipFacility, gridBagConstraints);
        panMemberShipFacility.getAccessibleContext().setAccessibleName("MembershipFacifility");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        ClientUtil.showMessageWindow("Password resting cannot proceed without Approval User Id and Password !!!");
        cancelActionKey=true;
        this.dispose();
         
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        // TODO add your handling code here:
        try{
        if(validUserDetails()){
            return;
        }
        else{
            loginTrue = true;
            cancelActionKey=false;
            this.dispose();
        }
        }catch (Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnLoginActionPerformed
 public boolean validUserDetails()throws Exception{
       HashMap whereMap=new HashMap();
       HashMap userMap = getLoginDetails();
       if(userMap !=null){
           whereMap.put("APPROVE_USER",userMap.get("USER_ID"));
           whereMap.put("CURR_USER",TrueTransactMain.USER_ID);
           whereMap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
           whereMap.put("PWD", encrypt.encrypt(CommonUtil.convertObjToStr(userMap.get("PASSWORD"))));
          
           List lst =ClientUtil.executeQuery("getUserDetails", whereMap);
           if(lst !=null && lst.size()>0){
               return false;
           }else{
               ClientUtil.showMessageWindow("Please Enter Valid User and Password Details");
               txtUserId.setText("");
               txtPassword.setText("");
               txtUserId.requestFocus();
               panRejection.setNextFocusableComponent(txtUserId);
               
           }
       }
       
       return true;
 }
 
                                    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
                                                                                                                                                                                }//GEN-LAST:event_formWindowClosed
                                    
                                    
                                                            private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
                                                                                                                                                                                                                                                                                                                                                            }//GEN-LAST:event_formWindowClosing
                                                            
                                                            /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
                                                            }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        //        new CheckCustomerIdUI().show();
    }
    public HashMap getLoginDetails(){
        HashMap loginMap =new HashMap();
        String user =CommonUtil.convertObjToStr(txtUserId.getText());
        String pass =CommonUtil.convertObjToStr(txtPassword.getText());
        if(user.length()>0 && pass.length()>0 ){
            loginMap.put("USER_ID",user);
            loginMap.put("PASSWORD",pass);
        }else{
            ClientUtil.showMessageWindow("Please Enter User and Password Details");
            return null;
        }
    return loginMap;
    }
    
    /**
     * Getter for property cancelActionKey.
     * @return Value of property cancelActionKey.
     */
    public boolean isCancelActionKey() {
        return cancelActionKey;
    }
    
    /**
     * Setter for property cancelActionKey.
     * @param cancelActionKey New value of property cancelActionKey.
     */
    public void setCancelActionKey(boolean cancelActionKey) {
        this.cancelActionKey = cancelActionKey;
    }

    public boolean isLoginTrue() {
        return loginTrue;
    }

    public void setLoginTrue(boolean loginTrue) {
        this.loginTrue = loginTrue;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnLogin;
    private com.see.truetransact.uicomponent.CLabel lblRecieptAmt;
    private com.see.truetransact.uicomponent.CLabel lblUserId;
    private com.see.truetransact.uicomponent.CPanel panMemberShipFacility;
    private com.see.truetransact.uicomponent.CPanel panRejection;
    private com.see.truetransact.uicomponent.CPanel panRejectionUI;
    private com.see.truetransact.uicomponent.CPanel panTotal;
    private com.see.truetransact.uicomponent.CPasswordField txtPassword;
    private com.see.truetransact.uicomponent.CTextField txtUserId;
    // End of variables declaration//GEN-END:variables
    
}
