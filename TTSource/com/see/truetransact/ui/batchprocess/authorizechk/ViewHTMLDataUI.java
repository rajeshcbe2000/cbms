/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ViewHTMLDataUI.java
 *
 * Created on Feb 27, 2005, 3:55 PM
 */

package com.see.truetransact.ui.batchprocess.authorizechk;

import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.ui.sysadmin.view.ViewLogRB;

import com.see.truetransact.ui.batchprocess.cashtally.CashTallyCheck ;
import com.see.truetransact.ui.batchprocess.authorizechk.UserCheck;
import com.see.truetransact.ui.batchprocess.authorizechk.DepositMaturingCheck;
import com.see.truetransact.ui.batchprocess.gl.BalanceCheck ;
import com.see.truetransact.ui.batchprocess.gl.OABalanceCheck ;
import com.see.truetransact.ui.batchprocess.gl.ContraHeadCheck ;
import com.see.truetransact.ui.batchprocess.gl.CashInHandCheck ;
import com.see.truetransact.ui.batchprocess.gl.UnclearedScheduleCheck ;
import com.see.truetransact.ui.batchprocess.gl.OAInoperativeBalanceCheck ;
import com.see.truetransact.ui.batchprocess.datacenter.AllBranchDayEndCheck;
import com.see.truetransact.ui.batchprocess.datacenter.AllBranchReconcileTask;
import com.see.truetransact.ui.batchprocess.maturedaccounts.MaturedAccountsCheck ;
import com.see.truetransact.ui.batchprocess.unsecuredloanaccounts.UnSecuredLoanAccountsCheck ;
import com.see.truetransact.ui.batchprocess.lockerOperation.LockerOperationCheck ;
import com.see.truetransact.ui.batchprocess.executeLockerRentSi.ExecuteLockerRentSiCheck ;
import java.util.HashMap ;
import java.util.ArrayList ;
import java.awt.Dimension;
import java.awt.Toolkit;
/**
 *
 * @author  152691
 */
public class ViewHTMLDataUI extends com.see.truetransact.uicomponent.CDialog  {
    
    HashMap paramMap = new HashMap();
    CInternalFrame parent = null;
    
    
    
    /** Creates new form ViewHTMLDataUI */
    public ViewHTMLDataUI(String classObject) {
        initComponents();
        setupInit();
        edpData.setText(populateData(classObject));
    }
    public ViewHTMLDataUI(String classObject, String lable) {
        String taskLable = lable;
        initComponents();
        setupInit();
        edpData.setText(populateData(classObject,taskLable));
    }
    public ViewHTMLDataUI(String classObject, String lable, HashMap isPrev) {
        HashMap isPrevDay = isPrev;
        String taskLable = lable;
        initComponents();
        setupInit();
        edpData.setText(populateData(classObject,taskLable,isPrevDay));
    }
    private void setupInit() {
        setFieldNames();
        internationalize();
        setupScreen();
    }
    
    
    /** Auto Generated Method - setFieldNames()
     * This method assigns name for all the components.
     * Other functions are working based on this name. */
    private void setFieldNames() {
        btnOk.setName("btnOk");
        lblData.setName("lblData");
        edpData.setName("edpData");
        panMain.setName("panMain");
        panButton.setName("panMain");
    }
    
    
    /** Auto Generated Method - internationalize()
     * This method used to assign display texts from
     * the Resource Bundle File. */
    private void internationalize() {
        ViewLogRB resourceBundle = new ViewLogRB();
        btnOk.setText(resourceBundle.getString("btnOk"));
        lblData.setText(resourceBundle.getString("lblData"));
    }
    
    private void setupScreen() {
        setModal(true);
        /* Calculate the screen size */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        /* Center frame on the screen */
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) 
            frameSize.height = screenSize.height;
        
        if (frameSize.width > screenSize.width) 
            frameSize.width = screenSize.width;
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }
    private String populateData(String classObject) {
        String returnStmt = "" ;
        try {
            if(classObject.equals("AuthorizationCheckTask"))
                returnStmt =  (new AuthorizationCheck()).executeTask();
            else if(classObject.equals("ZeroBalanceCheckTask"))
                returnStmt =  (new ZeroBalanceCheck()).executeTask();
            else if(classObject.equals("TransferTallyCheckTask"))
                returnStmt =  (new TransferTallyCheck()).executeTask();
            else if(classObject.equals("OutwdClrgPaySlipTallyCheckTask"))
                returnStmt =  (new OutwdClrgPaySlipTallyCheck()).executeTask();
            else if(classObject.equals("InwardTallyOutClrgCheckTask"))
                returnStmt =  (new InwardTallyOutClrgCheck()).executeTask();
            else if(classObject.equals("CashTallyCheckTask"))
                returnStmt =  (new CashTallyCheck()).executeTask();
            else if(classObject.equals("BalanceCheckTask"))
                returnStmt =  (new BalanceCheck()).executeTask();
            else if(classObject.equals("OABalanceCheckTask"))
                returnStmt =  (new OABalanceCheck()).executeTask();
            else if(classObject.equals("ContraHeadCheckTask"))
                returnStmt =  (new ContraHeadCheck()).executeTask();
            else if(classObject.equals("CashInHandCheckTask"))
                returnStmt =  (new CashInHandCheck()).executeTask();
            else if(classObject.equals("OAInoperativeBalanceCheckTask"))
                returnStmt =  (new OAInoperativeBalanceCheck()).executeTask();
            else if(classObject.equals("UnclearedScheduleCheckTask"))
                returnStmt =  (new UnclearedScheduleCheck()).executeTask();
            else if(classObject.equals("UserCheckTask"))
                returnStmt =  (new UserCheck()).executeTask();
            else if(classObject.equals("InterBranchAuthorizationCheck"))
                returnStmt=(new InterBranchAuthorizationCheck()).executeTask();
            else if(classObject.equals("CashAndTransferAuthCheckTask"))
                returnStmt=(new CashAndTransferAuthCheck()).executeTask();
            else if(classObject.equals("AllBranchDayEndTask"))
                returnStmt=(new AllBranchDayEndCheck()).executeTask();
            else if(classObject.equals("AllBranchReconcileTask"))
                returnStmt=(new AllBranchReconcileTask()).executeTask();
            else if(classObject.equals("FlexiTask"))
                returnStmt=(new FlexiCreation()).executeTask();
            else if(classObject.equals("ReverseFlexiTaskonMaturity"))
                returnStmt=(new ReverseFlexiPrematureOnMaturity()).executeTask();
            else if(classObject.equals("ReverseFlexiTask"))
                returnStmt=(new ReverseFlexiPremature()).executeTask();
            else if(classObject.equals("PreviousDayEndTask"))
                returnStmt=(new PreviousDayEndCheck()).executeTask();
            else if(classObject.equals("StartDayBeginTask"))
                returnStmt=(new PreviousDayEndCheck()).executeTask();
            else if(classObject.equals("MaturedAccountsCheckTask"))
                returnStmt=(new MaturedAccountsCheck()).executeTask();
             else if(classObject.equals("LockerOperationCheckTask"))
                returnStmt=(new LockerOperationCheck()).executeTask();
              else if(classObject.equals("ExecuteLockerRentSiCheckTask"))
                returnStmt=(new ExecuteLockerRentSiCheck()).executeTask();
               else if(classObject.equals("ScheduleCheck"))
                returnStmt=(new ScheduleCheck()).executeTask();
               else if(classObject.equals("TransactionDataCheck"))
                returnStmt=(new ScheduleCheck()).executeDataTask();
            else
                returnStmt=(new ErrorShowing()).executeTask();
        } catch (Exception ex) {}
        return returnStmt ;
    }
    
    private String populateData(String classObject, String lable) {
        String returnStmt = "" ;
        try {
            if(classObject.equals("AuthorizationCheckTask"))
                returnStmt =  (new AuthorizationCheck()).executeTask(lable);
            else if(classObject.equals("ZeroBalanceCheckTask"))
                returnStmt =  (new ZeroBalanceCheck()).executeTask(lable);
            else if(classObject.equals("TransferTallyCheckTask"))
                returnStmt =  (new TransferTallyCheck()).executeTask(lable);
            else if(classObject.equals("OutwdClrgPaySlipTallyCheckTask"))
                returnStmt =  (new OutwdClrgPaySlipTallyCheck()).executeTask(lable);
            else if(classObject.equals("InwardTallyOutClrgCheckTask"))
                returnStmt =  (new InwardTallyOutClrgCheck()).executeTask(lable);
            else if(classObject.equals("CashTallyCheckTask"))
                returnStmt =  (new CashTallyCheck()).executeTask(lable);
            else if(classObject.equals("BalanceCheckTask"))
                returnStmt =  (new BalanceCheck()).executeTask(lable);
            else if(classObject.equals("OABalanceCheckTask"))
                returnStmt =  (new OABalanceCheck()).executeTask(lable);
            else if(classObject.equals("ContraHeadCheckTask"))
                returnStmt =  (new ContraHeadCheck()).executeTask(lable);
            else if(classObject.equals("CashInHandCheckTask"))
                returnStmt =  (new CashInHandCheck()).executeTask(lable);
            else if(classObject.equals("OAInoperativeBalanceCheckTask"))
                returnStmt =  (new OAInoperativeBalanceCheck()).executeTask(lable);
            else if(classObject.equals("UnclearedScheduleCheckTask"))
                returnStmt =  (new UnclearedScheduleCheck()).executeTask(lable);
            else if(classObject.equals("UserCheckTask"))
                returnStmt =  (new UserCheck()).executeTask(lable);
            else if(classObject.equals("InterBranchAuthorizationCheck"))
                returnStmt=(new InterBranchAuthorizationCheck()).executeTask(lable);
            else if(classObject.equals("CashAndTransferAuthCheckTask"))
                returnStmt=(new CashAndTransferAuthCheck()).executeTask(lable);
            else if(classObject.equals("AllBranchDayEndTask"))
                returnStmt=(new AllBranchDayEndCheck()).executeTask(lable);
            else if(classObject.equals("AllBranchReconcileTask"))
                returnStmt=(new AllBranchReconcileTask()).executeTask(lable);
            else if(classObject.equals("FlexiTask"))
                returnStmt=(new FlexiCreation()).executeTask(lable);
            else if(classObject.equals("ReverseFlexiTaskonMaturity"))
                returnStmt=(new ReverseFlexiPrematureOnMaturity()).executeTask(lable);
            else if(classObject.equals("ReverseFlexiTask"))
                returnStmt=(new ReverseFlexiPremature()).executeTask(lable);
             else if(classObject.equals("DepositIntTask"))
                returnStmt=(new DepositIntCheck()).executeTask(lable);
            else if(classObject.equals("MinBalanceChargesTask"))
                returnStmt=(new MinBalanceChargesCheck()).executeTask(lable);
            else if(classObject.equals("PreviousDayEndTask"))
                returnStmt=(new PreviousDayEndCheck()).executeTask(lable);
            else if(classObject.equals("DepositMaturingCheckTask"))
                returnStmt=(new DepositMaturingCheck()).executeTask(lable);
            else if(classObject.equals("MaturedAccountsCheckTask"))
                returnStmt=(new MaturedAccountsCheck()).executeTask(lable);
             else if(classObject.equals("UnSecuredLoanAccountsCheckTask"))
                returnStmt=(new UnSecuredLoanAccountsCheck()).executeTask(lable);
              else if(classObject.equals("LockerOperationCheckTask"))
                returnStmt=(new LockerOperationCheck()).executeTask(lable);
              else if(classObject.equals("ExecuteLockerREntSiCheckTask"))
                returnStmt=(new ExecuteLockerRentSiCheck()).executeTask(lable);
                else if(classObject.equals("ScheduleCheck"))
                returnStmt=(new ScheduleCheck()).executeTask();
                else if(classObject.equals("TransactionDataCheck"))
                returnStmt=(new ScheduleCheck()).executeDataTask();
            else
                returnStmt=(new ErrorShowing()).executeTask();
            
        } catch (Exception ex) {}
        return returnStmt ;
    }
    private String populateData(String classObject, String lable, HashMap isPrev) {
        String returnStmt = "" ;
        try {
            if(classObject.equals("AuthorizationCheckTask"))
                returnStmt =  (new AuthorizationCheck()).executeTask(lable,isPrev);
            else if(classObject.equals("ZeroBalanceCheckTask"))
                returnStmt =  (new ZeroBalanceCheck()).executeTask(lable,isPrev);
            else if(classObject.equals("TransferTallyCheckTask"))
                returnStmt =  (new TransferTallyCheck()).executeTask(lable,isPrev);
            else if(classObject.equals("OutwdClrgPaySlipTallyCheckTask"))
                returnStmt =  (new OutwdClrgPaySlipTallyCheck()).executeTask(lable,isPrev);
            else if(classObject.equals("InwardTallyOutClrgCheckTask"))
                returnStmt =  (new InwardTallyOutClrgCheck()).executeTask(lable,isPrev);
            else if(classObject.equals("CashTallyCheckTask"))
                returnStmt =  (new CashTallyCheck()).executeTask(lable,isPrev);
            else if(classObject.equals("BalanceCheckTask"))
                returnStmt =  (new BalanceCheck()).executeTask(lable,isPrev);
            else if(classObject.equals("OABalanceCheckTask"))
                returnStmt =  (new OABalanceCheck()).executeTask(lable,isPrev);
            else if(classObject.equals("ContraHeadCheckTask"))
                returnStmt =  (new ContraHeadCheck()).executeTask(lable,isPrev);
            else if(classObject.equals("CashInHandCheckTask"))
                returnStmt =  (new CashInHandCheck()).executeTask(lable,isPrev);
            else if(classObject.equals("OAInoperativeBalanceCheckTask"))
                returnStmt =  (new OAInoperativeBalanceCheck()).executeTask(lable,isPrev);
            else if(classObject.equals("UnclearedScheduleCheckTask"))
                returnStmt =  (new UnclearedScheduleCheck()).executeTask(lable,isPrev);
            else if(classObject.equals("UserCheckTask"))
                returnStmt =  (new UserCheck()).executeTask(lable,isPrev);
            else if(classObject.equals("InterBranchAuthorizationCheck"))
                returnStmt=(new InterBranchAuthorizationCheck()).executeTask(lable,isPrev);
            else if(classObject.equals("CashAndTransferAuthCheckTask"))
                returnStmt=(new CashAndTransferAuthCheck()).executeTask(lable,isPrev);
            else if(classObject.equals("AllBranchDayEndTask"))
                returnStmt=(new AllBranchDayEndCheck()).executeTask(lable,isPrev);
            else if(classObject.equals("AllBranchReconcileTask"))
                returnStmt=(new AllBranchReconcileTask()).executeTask(lable,isPrev);
            else if(classObject.equals("FlexiTask"))
                returnStmt=(new FlexiCreation()).executeTask(lable,isPrev);
            else if(classObject.equals("ReverseFlexiTaskonMaturity"))
                returnStmt=(new ReverseFlexiPrematureOnMaturity()).executeTask(lable,isPrev);
            else if(classObject.equals("ReverseFlexiTask"))
                returnStmt=(new ReverseFlexiPremature()).executeTask(lable,isPrev);
             else if(classObject.equals("DepositIntTask"))
                returnStmt=(new DepositIntCheck()).executeTask(lable,isPrev);
            else if(classObject.equals("MinBalanceChargesTask"))
                returnStmt=(new MinBalanceChargesCheck()).executeTask(lable,isPrev);
            else if(classObject.equals("PreviousDayEndTask"))
                returnStmt=(new PreviousDayEndCheck()).executeTask(lable,isPrev);
            else if(classObject.equals("MaturedAccountsCheckTask"))
                returnStmt=(new MaturedAccountsCheck()).executeTask(lable,isPrev);
             else if(classObject.equals("UnSecuredLoanAccountsCheckTask"))
                returnStmt=(new UnSecuredLoanAccountsCheck()).executeTask(lable,isPrev);
             else if(classObject.equals("LockerOperationCheckTask"))
                returnStmt=(new LockerOperationCheck()).executeTask(lable,isPrev);
             else if(classObject.equals("ExecuteLockerRentSiCheckTask"))
                returnStmt=(new ExecuteLockerRentSiCheck()).executeTask(lable,isPrev);
            else
                returnStmt=(new ErrorShowing()).executeTask();
            
        } catch (Exception ex) {}
        return returnStmt ;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        panMain = new com.see.truetransact.uicomponent.CPanel();
        panButton = new com.see.truetransact.uicomponent.CPanel();
        btnOk = new com.see.truetransact.uicomponent.CButton();
        lblData = new com.see.truetransact.uicomponent.CLabel();
        sprData = new com.see.truetransact.uicomponent.CScrollPane();
        edpData = new com.see.truetransact.uicomponent.CEditorPane();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        panMain.setLayout(new java.awt.GridBagLayout());

        panMain.setMinimumSize(new java.awt.Dimension(800, 700));
        panMain.setPreferredSize(new java.awt.Dimension(800, 700));
        panButton.setLayout(new java.awt.GridBagLayout());

        btnOk.setText("OK");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(btnOk, gridBagConstraints);

        lblData.setText("Data");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(lblData, gridBagConstraints);

        sprData.setMinimumSize(new java.awt.Dimension(150, 40));
        sprData.setPreferredSize(new java.awt.Dimension(150, 40));
        sprData.setAutoscrolls(true);
        edpData.setEditable(false);
        edpData.setContentType("text/html");
        edpData.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        edpData.setMinimumSize(new java.awt.Dimension(150, 100));
        edpData.setPreferredSize(new java.awt.Dimension(2000, 100));
        edpData.setEnabled(false);
        sprData.setViewportView(edpData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(sprData, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMain.add(panButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(panMain, gridBagConstraints);

        pack();
    }//GEN-END:initComponents
    
    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnOkActionPerformed
    
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        // TODO add your handling code here:
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog
    
    /** Exit the Application */
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws Exception{
        //TransferTallyCheck zbc = new TransferTallyCheck() ;
        //ZeroBalanceCheck zbc = new ZeroBalanceCheck() ;
        //AuthorizationCheck zbc = new AuthorizationCheck();
        //OutwdClrgPaySlipTallyCheck zbc = new OutwdClrgPaySlipTallyCheck() ;
        //InwardTallyOutClrgCheck zbc = new InwardTallyOutClrgCheck();
        
        new ViewHTMLDataUI("InwardTallyOutClrgCheckTask").show();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnOk;
    private com.see.truetransact.uicomponent.CEditorPane edpData;
    private com.see.truetransact.uicomponent.CLabel lblData;
    private com.see.truetransact.uicomponent.CPanel panButton;
    private com.see.truetransact.uicomponent.CPanel panMain;
    private com.see.truetransact.uicomponent.CScrollPane sprData;
    // End of variables declaration//GEN-END:variables
    
}
