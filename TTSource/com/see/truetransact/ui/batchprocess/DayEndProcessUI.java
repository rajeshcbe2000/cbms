/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DayEndProcessUI.java
 *
 * Created on August 17, 2004, 4:49 PM
 */

package com.see.truetransact.ui.batchprocess;

import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants ;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.batchprocess.authorizechk.ViewHTMLDataUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.sysadmin.group.GroupOB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.sysadmin.config.ConfigPasswordTO;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.sql.*;
import javax.swing.JDialog;
/**
 *
 * @author  bala
 */
public class DayEndProcessUI extends CInternalFrame implements java.util.Observer{
    
    
    /** Variables related to the task*/
    private TaskStatus tskStatus;
    private TaskHeader tskHeader;
    private ThreadPool threadPool;
    private ProcessOB observable;
    private boolean taskRunning;
    private boolean dailyBalance=true;
    private String screenName ;
    String chk="";
    String daily="";
    String comp="";
    String dayEndType = "";
    private boolean isAllTskComp = false;
    private Date currDt = null;
    JDialog loading = null;
    private boolean complete = false;
    /** Creates new form BatchProcessUI */
    public DayEndProcessUI() {
        initComponents();
        setObservable();
        setupInit();
        currDt = ClientUtil.getCurrentDate();
        //        panCheckBoxes.setVisible(false);
        chkCCBalChk.setVisible(false);
        chkAcCreditFromDebit.setVisible(true);
        
        chkPendingChkForClearing.setVisible(false);
        chkGL_Abstract.setVisible(false);
        chkSelectAll.setVisible(false);
        selectAllCheckBoxes(panCheckBoxes, false);
        btnProcess.setEnabled(false);
        chkDividendCalculation.setVisible(true);
        panCheckBoxes.setEnabled(false);
        chkDailyBalance.setVisible(false);
        chkDepositAutoRenewal.setVisible(false);
        chkExcessWithdrawlChrg.setVisible(false);
        chkNonMiniBalanceChrg.setVisible(false);
        chkInterestReceivable.setVisible(true);
        chkNonMiniBalanceChrg.setVisible(true);
        chkInterBranchAuthRec.setVisible(false);
        btnDailyActivity.setEnabled(false);
        btnTransCheck.setEnabled(false);
        btnComplete.setEnabled(false);
        HashMap test = new HashMap();
        List lst = ((List)ClientUtil.executeQuery("getSelectConfigPasswordTO", test));
        dayEndType =  lst!=null ? CommonUtil.convertObjToStr(((ConfigPasswordTO)lst.get(0)).getDayEndType()) : "";
        if (dayEndType.equals("")) {
            ClientUtil.showAlertWindow("<html>Cause  : Unable to load Day End.<br>" +
            "Reason : DayEnd type not mentioned in Bank Configuration.</html>");
            panCheckBoxes.setVisible(false);
            panComplete.setVisible(false);
            panTable.setVisible(false);
            btnProcess.setVisible(false);
        } else if (dayEndType.equals("BRANCH_LEVEL")) {
            btnTransCheck.setVisible(true);
            chkDailyBalance.setVisible(true);
            chkGL_Abstract.setVisible(true);
            enableDisableTaskChks(false);
        } else {
            btnDailyActivity.setVisible(false);
            btnTransCheck.setVisible(false);
            chkReverseFlexiPrematureClosing.setVisible(false);
            chkInterestPayable.setVisible(false);
            chkDepositInt.setVisible(false);
            chkIntCalc.setVisible(false);
            chkNPA.setVisible(false);
            chkCABalChk.setVisible(false);
            chkODBalChk.setVisible(false);
            chkDepositAutoRenewal.setVisible(false);
            chkFlexi.setVisible(false);
            chkFolio.setVisible(false);
            chkNonMiniBalanceChrg.setVisible(false);
            chkLapse_Transfer.setVisible(false);
            chkDividendCalculation.setVisible(false);
        }
        test = null;
        lst = null;
    }
    
    public DayEndProcessUI(String selected) {
        this.screenName = selected ;
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        setObservable();
        setupInit();
        chkCCBalChk.setVisible(false);
        //        chkGL_Abstract.setVisible(false);
        chkAcCreditFromDebit.setVisible(true);
        chkPendingChkForClearing.setVisible(false);
    }
    
    private void setupInit() {
        HashMap taskMap = new HashMap();
        taskMap.put(chkGL_Abstract.getText(), "GlAbstractUpdateTask"); //Create GL Abstarct for opening and closing balance
        taskMap.put(chkCAAcctOpenPendingApproval.getText(), "AuthorizationCheckTask"); //A/c opening and modifying for approval
        taskMap.put(chkDepMature.getText(), "DepositMaturingCheckTask"); //Deposit Maturing Check Task
        taskMap.put(chkCAAcctOpenZeroBal.getText(), "ZeroBalanceCheckTask"); //chk for CA A/c opened with 0 bal
        taskMap.put(chkCABalChk.getText(), "OABalanceCheckTask");
        taskMap.put(chkCAInopBalChk.getText(), "OAInoperativeBalanceCheckTask");
        taskMap.put(chkCCBalChk.getText(), "");
        taskMap.put(chkClearingSchkLeft.getText(), "UnclearedScheduleCheckTask");
        taskMap.put(chkClosingCashDebit.getText(), "CashInHandCheckTask");
        taskMap.put(chkContraAcctHd.getText(), "ContraHeadCheckTask");
        taskMap.put(chkIntCalc.getText(), "InterestCalculationTask");
        taskMap.put(chkODBalChk.getText(), "BalanceCheckTask"); // OD balance Chk
        taskMap.put(chkPendingChkForClearing.getText(), "");
        taskMap.put(chkAcCreditFromDebit.getText(), "DebitToCreditCheckTask");
        taskMap.put(chkTDBalChk.getText(), "InwardTallyOutClrgCheckTask"); //Td balance Chk
        taskMap.put(chkTLBalChk.getText(), "OutwdClrgPaySlipTallyCheckTask"); //TL balance chk
        taskMap.put(chkTotCrAndDr.getText(), "TransferTallyCheckTask"); //Checks for Total cr and dr
        taskMap.put(chkUsrCashUntallied.getText(), "CashTallyCheckTask"); //chk for cash untallied for user
        taskMap.put(chkExcessWithdrawlChrg.getText(), "ExcessTransChrgesTask"); //Apply excess Withdrawl charges
        taskMap.put(chkNonMiniBalanceChrg.getText(), "MinBalanceChargesTask"); //Non maintenance of min bal
        taskMap.put(chkFlexi.getText(), "FlexiTask"); //Flexi Task
//        taskMap.put(chkDepositAutoRenewal.getText(), "DepositAutoRenewalTask"); //Deposit Auto Renewal
        taskMap.put(chkInterestPayable.getText(), "InterestTask"); //Interest Application - Payable
        taskMap.put(chkInterestReceivable.getText(), "DebitIntTask"); //Interest Application -  Receivable
        taskMap.put(chkDepositInt.getText(), "DepositIntTask"); //Deposit Int Application
        taskMap.put(chkDailyBalance.getText(), "DailyBalanceUpdateTask"); //Daily Balance Update
        taskMap.put(chkUsersLogout.getText(), "UserCheckTask"); //All users logged out or not checking
        taskMap.put(chkDividendCalculation.getText(), "DividendCalcTask"); //Dividend Calculation and application
        taskMap.put(chkNPA.getText(), "NPATask"); //NPA Status change task
        taskMap.put(chkInterBranchAuthRec.getText(), "InterBranchAuthorizationCheck"); //Inter Branch Authorization
        taskMap.put(chkZeroBalAcHd.getText(), "ZeroBalanceAccountCheckTask"); //Dividend Calculation and application
        taskMap.put(chkFolio.getText(),"FolioChargesTask");
        taskMap.put(chkLapse_Transfer.getText(),"RemittanceLapseTransferTask");
        taskMap.put(chkCashAndTransferAuthChk.getText(), "CashAndTransferAuthCheckTask");
        taskMap.put(chkReverseFlexiPrematureClosing.getText(), "ReverseFlexiTask");
        taskMap.put(chkTransOptInOpt.getText(), "DormantToInOperativeTask");
        taskMap.put(chkMaturedAccounts.getText(), "MaturedAccountsCheckTask");
        taskMap.put(chkUnSecuredAccounts.getText(), "UnSecuredLoanAccountsCheckTask");
        taskMap.put(chkLockerOp.getText(), "LockerOperationCheckTask");
        taskMap.put(chkExecuteLockerRentSi.getText(), "ExecuteLockerRentSiCheckTask");
        taskMap.put("DifferentDateInterBranchCheck", "DifferentDateInterBranchCheck");//Added by abi 16-06-2015
        taskMap.put("ScheduleCheck", "ScheduleCheck");
        taskMap.put("TransactionDataCheck", "TransactionDataCheck");
        observable.setTaskMap(taskMap);
        tblLog.setModel(observable.getDayBeginTableModel());
        tskStatus = new TaskStatus();
        tskHeader = new TaskHeader();
        taskRunning = false;
        taskMap = null;
    }
    private void setObservable(){
        observable = new ProcessOB();
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

        panBatchProcess = new com.see.truetransact.uicomponent.CPanel();
        sptSpace1 = new com.see.truetransact.uicomponent.CSeparator();
        tabDayEndList = new com.see.truetransact.uicomponent.CTabbedPane();
        panCheckBoxes = new com.see.truetransact.uicomponent.CPanel();
        chkUsrCashUntallied = new com.see.truetransact.uicomponent.CCheckBox();
        chkCAAcctOpenPendingApproval = new com.see.truetransact.uicomponent.CCheckBox();
        chkODBalChk = new com.see.truetransact.uicomponent.CCheckBox();
        chkTDBalChk = new com.see.truetransact.uicomponent.CCheckBox();
        chkClearingSchkLeft = new com.see.truetransact.uicomponent.CCheckBox();
        chkClosingCashDebit = new com.see.truetransact.uicomponent.CCheckBox();
        chkCAInopBalChk = new com.see.truetransact.uicomponent.CCheckBox();
        chkContraAcctHd = new com.see.truetransact.uicomponent.CCheckBox();
        chkCAAcctOpenZeroBal = new com.see.truetransact.uicomponent.CCheckBox();
        chkPendingChkForClearing = new com.see.truetransact.uicomponent.CCheckBox();
        chkTotCrAndDr = new com.see.truetransact.uicomponent.CCheckBox();
        chkAcCreditFromDebit = new com.see.truetransact.uicomponent.CCheckBox();
        chkIntCalc = new com.see.truetransact.uicomponent.CCheckBox();
        chkCABalChk = new com.see.truetransact.uicomponent.CCheckBox();
        chkCCBalChk = new com.see.truetransact.uicomponent.CCheckBox();
        chkTLBalChk = new com.see.truetransact.uicomponent.CCheckBox();
        chkNonMiniBalanceChrg = new com.see.truetransact.uicomponent.CCheckBox();
        chkExcessWithdrawlChrg = new com.see.truetransact.uicomponent.CCheckBox();
        chkFlexi = new com.see.truetransact.uicomponent.CCheckBox();
        chkDepositAutoRenewal = new com.see.truetransact.uicomponent.CCheckBox();
        chkDepositInt = new com.see.truetransact.uicomponent.CCheckBox();
        chkInterestPayable = new com.see.truetransact.uicomponent.CCheckBox();
        chkDailyBalance = new com.see.truetransact.uicomponent.CCheckBox();
        chkInterestReceivable = new com.see.truetransact.uicomponent.CCheckBox();
        chkUsersLogout = new com.see.truetransact.uicomponent.CCheckBox();
        chkDividendCalculation = new com.see.truetransact.uicomponent.CCheckBox();
        chkNPA = new com.see.truetransact.uicomponent.CCheckBox();
        chkInterBranchAuthRec = new com.see.truetransact.uicomponent.CCheckBox();
        chkZeroBalAcHd = new com.see.truetransact.uicomponent.CCheckBox();
        chkGL_Abstract = new com.see.truetransact.uicomponent.CCheckBox();
        chkFolio = new com.see.truetransact.uicomponent.CCheckBox();
        chkCashAndTransferAuthChk = new com.see.truetransact.uicomponent.CCheckBox();
        chkReverseFlexiPrematureClosing = new com.see.truetransact.uicomponent.CCheckBox();
        chkLapse_Transfer = new com.see.truetransact.uicomponent.CCheckBox();
        chkTransOptInOpt = new com.see.truetransact.uicomponent.CCheckBox();
        chkDepMature = new com.see.truetransact.uicomponent.CCheckBox();
        chkMaturedAccounts = new com.see.truetransact.uicomponent.CCheckBox();
        chkUnSecuredAccounts = new com.see.truetransact.uicomponent.CCheckBox();
        chkLockerOp = new com.see.truetransact.uicomponent.CCheckBox();
        chkExecuteLockerRentSi = new com.see.truetransact.uicomponent.CCheckBox();
        panComplete = new com.see.truetransact.uicomponent.CPanel();
        btnComplete = new com.see.truetransact.uicomponent.CButton();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        btnComplete1 = new com.see.truetransact.uicomponent.CButton();
        btnDailyActivity = new com.see.truetransact.uicomponent.CButton();
        btnTransCheck = new com.see.truetransact.uicomponent.CButton();
        chkSchedule = new com.see.truetransact.uicomponent.CCheckBox();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        scrOutputMsg = new com.see.truetransact.uicomponent.CScrollPane();
        tblLog = new com.see.truetransact.uicomponent.CTable();
        tbrHead = new javax.swing.JToolBar();
        btnProcess = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace24 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(720, 660));
        setNormalBounds(new java.awt.Rectangle(0, 0, 542, 400));
        setPreferredSize(new java.awt.Dimension(720, 660));
        getContentPane().setLayout(new java.awt.BorderLayout(4, 4));

        panBatchProcess.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panBatchProcess.setMinimumSize(new java.awt.Dimension(680, 600));
        panBatchProcess.setPreferredSize(new java.awt.Dimension(680, 640));
        panBatchProcess.setLayout(new java.awt.GridBagLayout());

        sptSpace1.setPreferredSize(new java.awt.Dimension(720, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panBatchProcess.add(sptSpace1, gridBagConstraints);

        tabDayEndList.setMinimumSize(new java.awt.Dimension(700, 300));
        tabDayEndList.setPreferredSize(new java.awt.Dimension(700, 300));

        panCheckBoxes.setMinimumSize(new java.awt.Dimension(540, 580));
        panCheckBoxes.setPreferredSize(new java.awt.Dimension(536, 430));
        panCheckBoxes.setLayout(new java.awt.GridBagLayout());

        chkUsrCashUntallied.setText("Check for Users Having Untallied Cash");
        chkUsrCashUntallied.setMaximumSize(new java.awt.Dimension(253, 21));
        chkUsrCashUntallied.setMinimumSize(new java.awt.Dimension(253, 18));
        chkUsrCashUntallied.setPreferredSize(new java.awt.Dimension(253, 18));
        chkUsrCashUntallied.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkUsrCashUntalliedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 1, 1);
        panCheckBoxes.add(chkUsrCashUntallied, gridBagConstraints);

        chkCAAcctOpenPendingApproval.setSelected(true);
        chkCAAcctOpenPendingApproval.setText("Account Opening / Modifying pending for Approval");
        chkCAAcctOpenPendingApproval.setMaximumSize(new java.awt.Dimension(313, 18));
        chkCAAcctOpenPendingApproval.setMinimumSize(new java.awt.Dimension(313, 18));
        chkCAAcctOpenPendingApproval.setPreferredSize(new java.awt.Dimension(313, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 1, 1);
        panCheckBoxes.add(chkCAAcctOpenPendingApproval, gridBagConstraints);

        chkODBalChk.setSelected(true);
        chkODBalChk.setText("Transaction Balance Check");
        chkODBalChk.setActionCommand("OD Balance Check");
        chkODBalChk.setMaximumSize(new java.awt.Dimension(185, 21));
        chkODBalChk.setMinimumSize(new java.awt.Dimension(185, 18));
        chkODBalChk.setPreferredSize(new java.awt.Dimension(185, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 10);
        panCheckBoxes.add(chkODBalChk, gridBagConstraints);

        chkTDBalChk.setSelected(true);
        chkTDBalChk.setText("Inward Clearing Tally Check");
        chkTDBalChk.setMaximumSize(new java.awt.Dimension(185, 21));
        chkTDBalChk.setMinimumSize(new java.awt.Dimension(185, 18));
        chkTDBalChk.setPreferredSize(new java.awt.Dimension(185, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 1, 1);
        panCheckBoxes.add(chkTDBalChk, gridBagConstraints);

        chkClearingSchkLeft.setSelected(true);
        chkClearingSchkLeft.setText("Check for Clearing Schedules not closed");
        chkClearingSchkLeft.setMaximumSize(new java.awt.Dimension(305, 21));
        chkClearingSchkLeft.setMinimumSize(new java.awt.Dimension(305, 18));
        chkClearingSchkLeft.setPreferredSize(new java.awt.Dimension(305, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 1, 1);
        panCheckBoxes.add(chkClearingSchkLeft, gridBagConstraints);

        chkClosingCashDebit.setSelected(true);
        chkClosingCashDebit.setText("Check whether Closing Cash is in Debit");
        chkClosingCashDebit.setMaximumSize(new java.awt.Dimension(205, 18));
        chkClosingCashDebit.setMinimumSize(new java.awt.Dimension(215, 18));
        chkClosingCashDebit.setPreferredSize(new java.awt.Dimension(255, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 1, 1);
        panCheckBoxes.add(chkClosingCashDebit, gridBagConstraints);

        chkCAInopBalChk.setSelected(true);
        chkCAInopBalChk.setText("SB / CA Inoperative Balance Check");
        chkCAInopBalChk.setMaximumSize(new java.awt.Dimension(231, 21));
        chkCAInopBalChk.setMinimumSize(new java.awt.Dimension(231, 18));
        chkCAInopBalChk.setPreferredSize(new java.awt.Dimension(231, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 1, 1);
        panCheckBoxes.add(chkCAInopBalChk, gridBagConstraints);

        chkContraAcctHd.setSelected(true);
        chkContraAcctHd.setText("Check for Contra Account Head");
        chkContraAcctHd.setMaximumSize(new java.awt.Dimension(215, 21));
        chkContraAcctHd.setMinimumSize(new java.awt.Dimension(215, 18));
        chkContraAcctHd.setPreferredSize(new java.awt.Dimension(215, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 1, 1);
        panCheckBoxes.add(chkContraAcctHd, gridBagConstraints);

        chkCAAcctOpenZeroBal.setSelected(true);
        chkCAAcctOpenZeroBal.setText("Check for Accounts Opened with Zero Balance");
        chkCAAcctOpenZeroBal.setMaximumSize(new java.awt.Dimension(295, 18));
        chkCAAcctOpenZeroBal.setMinimumSize(new java.awt.Dimension(295, 18));
        chkCAAcctOpenZeroBal.setPreferredSize(new java.awt.Dimension(295, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 1, 1);
        panCheckBoxes.add(chkCAAcctOpenZeroBal, gridBagConstraints);

        chkPendingChkForClearing.setText("Check for Pending Cheques for Clearing");
        chkPendingChkForClearing.setMaximumSize(new java.awt.Dimension(189, 21));
        chkPendingChkForClearing.setMinimumSize(new java.awt.Dimension(189, 18));
        chkPendingChkForClearing.setPreferredSize(new java.awt.Dimension(260, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 1, 1);
        panCheckBoxes.add(chkPendingChkForClearing, gridBagConstraints);

        chkTotCrAndDr.setSelected(true);
        chkTotCrAndDr.setText("Check for Total Credit and Debit");
        chkTotCrAndDr.setMaximumSize(new java.awt.Dimension(217, 21));
        chkTotCrAndDr.setMinimumSize(new java.awt.Dimension(217, 18));
        chkTotCrAndDr.setPreferredSize(new java.awt.Dimension(217, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 1, 1);
        panCheckBoxes.add(chkTotCrAndDr, gridBagConstraints);

        chkAcCreditFromDebit.setText("Checks for Account Coming to Credit From Debit ");
        chkAcCreditFromDebit.setMaximumSize(new java.awt.Dimension(316, 21));
        chkAcCreditFromDebit.setMinimumSize(new java.awt.Dimension(316, 21));
        chkAcCreditFromDebit.setPreferredSize(new java.awt.Dimension(316, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 1, 1);
        panCheckBoxes.add(chkAcCreditFromDebit, gridBagConstraints);

        chkIntCalc.setSelected(true);
        chkIntCalc.setText("Checks for Interest Calculations for OD / CC / TL");
        chkIntCalc.setMaximumSize(new java.awt.Dimension(307, 21));
        chkIntCalc.setMinimumSize(new java.awt.Dimension(307, 18));
        chkIntCalc.setPreferredSize(new java.awt.Dimension(307, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 10);
        panCheckBoxes.add(chkIntCalc, gridBagConstraints);

        chkCABalChk.setText("Operative Account Balance Check");
        chkCABalChk.setMaximumSize(new java.awt.Dimension(223, 21));
        chkCABalChk.setMinimumSize(new java.awt.Dimension(223, 18));
        chkCABalChk.setPreferredSize(new java.awt.Dimension(223, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 10);
        panCheckBoxes.add(chkCABalChk, gridBagConstraints);

        chkCCBalChk.setText("CC Balance Check");
        chkCCBalChk.setMaximumSize(new java.awt.Dimension(135, 21));
        chkCCBalChk.setMinimumSize(new java.awt.Dimension(135, 18));
        chkCCBalChk.setPreferredSize(new java.awt.Dimension(135, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 1, 1);
        panCheckBoxes.add(chkCCBalChk, gridBagConstraints);

        chkTLBalChk.setSelected(true);
        chkTLBalChk.setText("Outward Clearing Tally Check");
        chkTLBalChk.setMaximumSize(new java.awt.Dimension(197, 21));
        chkTLBalChk.setMinimumSize(new java.awt.Dimension(197, 18));
        chkTLBalChk.setPreferredSize(new java.awt.Dimension(197, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 1, 1);
        panCheckBoxes.add(chkTLBalChk, gridBagConstraints);

        chkNonMiniBalanceChrg.setSelected(true);
        chkNonMiniBalanceChrg.setText("Apply Non-Minimum Balance Maintenance Charges");
        chkNonMiniBalanceChrg.setMaximumSize(new java.awt.Dimension(315, 21));
        chkNonMiniBalanceChrg.setMinimumSize(new java.awt.Dimension(315, 18));
        chkNonMiniBalanceChrg.setPreferredSize(new java.awt.Dimension(315, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 10);
        panCheckBoxes.add(chkNonMiniBalanceChrg, gridBagConstraints);

        chkExcessWithdrawlChrg.setSelected(true);
        chkExcessWithdrawlChrg.setText("Apply Excess Withdrawl Charges");
        chkExcessWithdrawlChrg.setMaximumSize(new java.awt.Dimension(219, 21));
        chkExcessWithdrawlChrg.setMinimumSize(new java.awt.Dimension(219, 18));
        chkExcessWithdrawlChrg.setPreferredSize(new java.awt.Dimension(219, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 10);
        panCheckBoxes.add(chkExcessWithdrawlChrg, gridBagConstraints);

        chkFlexi.setText("Flexi Creation");
        chkFlexi.setMaximumSize(new java.awt.Dimension(73, 21));
        chkFlexi.setMinimumSize(new java.awt.Dimension(110, 18));
        chkFlexi.setPreferredSize(new java.awt.Dimension(110, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 10);
        panCheckBoxes.add(chkFlexi, gridBagConstraints);

        chkDepositAutoRenewal.setText("Deposit Auto Renewal");
        chkDepositAutoRenewal.setMaximumSize(new java.awt.Dimension(153, 21));
        chkDepositAutoRenewal.setMinimumSize(new java.awt.Dimension(153, 18));
        chkDepositAutoRenewal.setPreferredSize(new java.awt.Dimension(153, 18));
        chkDepositAutoRenewal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkDepositAutoRenewalActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 10);
        panCheckBoxes.add(chkDepositAutoRenewal, gridBagConstraints);

        chkDepositInt.setText("Deposit Interest Provision");
        chkDepositInt.setMaximumSize(new java.awt.Dimension(175, 21));
        chkDepositInt.setMinimumSize(new java.awt.Dimension(175, 18));
        chkDepositInt.setPreferredSize(new java.awt.Dimension(175, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 10);
        panCheckBoxes.add(chkDepositInt, gridBagConstraints);

        chkInterestPayable.setText("Interest Calculation - Payable for OA,AD");
        chkInterestPayable.setMaximumSize(new java.awt.Dimension(280, 21));
        chkInterestPayable.setMinimumSize(new java.awt.Dimension(280, 18));
        chkInterestPayable.setPreferredSize(new java.awt.Dimension(280, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 10);
        panCheckBoxes.add(chkInterestPayable, gridBagConstraints);

        chkDailyBalance.setSelected(true);
        chkDailyBalance.setText("Update Daily Balance");
        chkDailyBalance.setMaximumSize(new java.awt.Dimension(151, 21));
        chkDailyBalance.setMinimumSize(new java.awt.Dimension(151, 18));
        chkDailyBalance.setPreferredSize(new java.awt.Dimension(151, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 10);
        panCheckBoxes.add(chkDailyBalance, gridBagConstraints);

        chkInterestReceivable.setText("Interest Calculation - Receivable");
        chkInterestReceivable.setMaximumSize(new java.awt.Dimension(211, 21));
        chkInterestReceivable.setMinimumSize(new java.awt.Dimension(211, 18));
        chkInterestReceivable.setPreferredSize(new java.awt.Dimension(211, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 10);
        panCheckBoxes.add(chkInterestReceivable, gridBagConstraints);

        chkUsersLogout.setSelected(true);
        chkUsersLogout.setText("Check for Users not logged out");
        chkUsersLogout.setMaximumSize(new java.awt.Dimension(199, 18));
        chkUsersLogout.setMinimumSize(new java.awt.Dimension(199, 18));
        chkUsersLogout.setPreferredSize(new java.awt.Dimension(210, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 1, 1);
        panCheckBoxes.add(chkUsersLogout, gridBagConstraints);

        chkDividendCalculation.setText("Dividend Calculation");
        chkDividendCalculation.setMaximumSize(new java.awt.Dimension(141, 21));
        chkDividendCalculation.setMinimumSize(new java.awt.Dimension(141, 18));
        chkDividendCalculation.setPreferredSize(new java.awt.Dimension(141, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 10);
        panCheckBoxes.add(chkDividendCalculation, gridBagConstraints);

        chkNPA.setText("Non Performing Assets");
        chkNPA.setMaximumSize(new java.awt.Dimension(159, 21));
        chkNPA.setMinimumSize(new java.awt.Dimension(159, 18));
        chkNPA.setPreferredSize(new java.awt.Dimension(159, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 10);
        panCheckBoxes.add(chkNPA, gridBagConstraints);

        chkInterBranchAuthRec.setSelected(true);
        chkInterBranchAuthRec.setText("Check Inter Branch Records Authorization");
        chkInterBranchAuthRec.setMaximumSize(new java.awt.Dimension(269, 21));
        chkInterBranchAuthRec.setMinimumSize(new java.awt.Dimension(269, 18));
        chkInterBranchAuthRec.setPreferredSize(new java.awt.Dimension(269, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 1, 1);
        panCheckBoxes.add(chkInterBranchAuthRec, gridBagConstraints);

        chkZeroBalAcHd.setText("Check for Zero Balance Account Heads");
        chkZeroBalAcHd.setMaximumSize(new java.awt.Dimension(235, 21));
        chkZeroBalAcHd.setMinimumSize(new java.awt.Dimension(235, 18));
        chkZeroBalAcHd.setPreferredSize(new java.awt.Dimension(255, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 1, 1);
        panCheckBoxes.add(chkZeroBalAcHd, gridBagConstraints);

        chkGL_Abstract.setText("GL Abstract");
        chkGL_Abstract.setMaximumSize(new java.awt.Dimension(117, 21));
        chkGL_Abstract.setMinimumSize(new java.awt.Dimension(117, 18));
        chkGL_Abstract.setPreferredSize(new java.awt.Dimension(117, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 10);
        panCheckBoxes.add(chkGL_Abstract, gridBagConstraints);

        chkFolio.setText("Folio Charges");
        chkFolio.setMaximumSize(new java.awt.Dimension(117, 21));
        chkFolio.setMinimumSize(new java.awt.Dimension(117, 18));
        chkFolio.setPreferredSize(new java.awt.Dimension(117, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 10);
        panCheckBoxes.add(chkFolio, gridBagConstraints);

        chkCashAndTransferAuthChk.setText("Cash And Transfer Authorization Check");
        chkCashAndTransferAuthChk.setMaximumSize(new java.awt.Dimension(255, 18));
        chkCashAndTransferAuthChk.setMinimumSize(new java.awt.Dimension(255, 18));
        chkCashAndTransferAuthChk.setPreferredSize(new java.awt.Dimension(255, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 1, 1);
        panCheckBoxes.add(chkCashAndTransferAuthChk, gridBagConstraints);

        chkReverseFlexiPrematureClosing.setText("Reverse Flexi on Premature Closing");
        chkReverseFlexiPrematureClosing.setMaximumSize(new java.awt.Dimension(73, 21));
        chkReverseFlexiPrematureClosing.setMinimumSize(new java.awt.Dimension(235, 18));
        chkReverseFlexiPrematureClosing.setPreferredSize(new java.awt.Dimension(235, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 10);
        panCheckBoxes.add(chkReverseFlexiPrematureClosing, gridBagConstraints);

        chkLapse_Transfer.setText("Transfer lapsed PO/DD into lapse head");
        chkLapse_Transfer.setMaximumSize(new java.awt.Dimension(265, 18));
        chkLapse_Transfer.setMinimumSize(new java.awt.Dimension(265, 18));
        chkLapse_Transfer.setPreferredSize(new java.awt.Dimension(265, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 10);
        panCheckBoxes.add(chkLapse_Transfer, gridBagConstraints);

        chkTransOptInOpt.setText("Transfer of Operative Account to Inoperative");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 10);
        panCheckBoxes.add(chkTransOptInOpt, gridBagConstraints);

        chkDepMature.setText("Checks for Deposits Loan Maturing today");
        chkDepMature.setMaximumSize(new java.awt.Dimension(316, 21));
        chkDepMature.setMinimumSize(new java.awt.Dimension(316, 21));
        chkDepMature.setPreferredSize(new java.awt.Dimension(316, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 1, 1);
        panCheckBoxes.add(chkDepMature, gridBagConstraints);

        chkMaturedAccounts.setText("Matured Accounts");
        chkMaturedAccounts.setMaximumSize(new java.awt.Dimension(223, 21));
        chkMaturedAccounts.setMinimumSize(new java.awt.Dimension(223, 18));
        chkMaturedAccounts.setPreferredSize(new java.awt.Dimension(223, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 2, 10);
        panCheckBoxes.add(chkMaturedAccounts, gridBagConstraints);

        chkUnSecuredAccounts.setText("UnSecured Loan A/cs");
        chkUnSecuredAccounts.setMaximumSize(new java.awt.Dimension(223, 21));
        chkUnSecuredAccounts.setMinimumSize(new java.awt.Dimension(223, 18));
        chkUnSecuredAccounts.setPreferredSize(new java.awt.Dimension(223, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 10);
        panCheckBoxes.add(chkUnSecuredAccounts, gridBagConstraints);

        chkLockerOp.setText("Check For Locker Operation Completed");
        chkLockerOp.setMaximumSize(new java.awt.Dimension(200, 18));
        chkLockerOp.setMinimumSize(new java.awt.Dimension(200, 18));
        chkLockerOp.setPreferredSize(new java.awt.Dimension(260, 18));
        chkLockerOp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkLockerOpActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 10);
        panCheckBoxes.add(chkLockerOp, gridBagConstraints);

        chkExecuteLockerRentSi.setText("Execute Locker Rent SI");
        chkExecuteLockerRentSi.setMaximumSize(new java.awt.Dimension(190, 18));
        chkExecuteLockerRentSi.setMinimumSize(new java.awt.Dimension(190, 18));
        chkExecuteLockerRentSi.setPreferredSize(new java.awt.Dimension(190, 18));
        chkExecuteLockerRentSi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkExecuteLockerRentSiActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 10);
        panCheckBoxes.add(chkExecuteLockerRentSi, gridBagConstraints);

        panComplete.setMinimumSize(new java.awt.Dimension(470, 27));
        panComplete.setPreferredSize(new java.awt.Dimension(470, 27));
        panComplete.setLayout(new java.awt.GridBagLayout());

        btnComplete.setText("Complete");
        btnComplete.setMinimumSize(new java.awt.Dimension(89, 25));
        btnComplete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCompleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 8, 7, 8);
        panComplete.add(btnComplete, gridBagConstraints);

        chkSelectAll.setSelected(true);
        chkSelectAll.setText("Select All");
        chkSelectAll.setMinimumSize(new java.awt.Dimension(81, 25));
        chkSelectAll.setPreferredSize(new java.awt.Dimension(81, 22));
        chkSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 70, 0, 0);
        panComplete.add(chkSelectAll, gridBagConstraints);

        btnComplete1.setText("Perform Check");
        btnComplete1.setMinimumSize(new java.awt.Dimension(121, 25));
        btnComplete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnComplete1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 8, 7, 8);
        panComplete.add(btnComplete1, gridBagConstraints);

        btnDailyActivity.setText("Daily Activity");
        btnDailyActivity.setActionCommand("Daily Activity");
        btnDailyActivity.setMinimumSize(new java.awt.Dimension(107, 27));
        btnDailyActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDailyActivityActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 8, 7, 8);
        panComplete.add(btnDailyActivity, gridBagConstraints);

        btnTransCheck.setText("TransCheck");
        btnTransCheck.setMinimumSize(new java.awt.Dimension(103, 25));
        btnTransCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransCheckActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 8, 7, 8);
        panComplete.add(btnTransCheck, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        panCheckBoxes.add(panComplete, gridBagConstraints);

        chkSchedule.setText("Check For Schedule");
        chkSchedule.setMaximumSize(new java.awt.Dimension(200, 18));
        chkSchedule.setMinimumSize(new java.awt.Dimension(200, 18));
        chkSchedule.setPreferredSize(new java.awt.Dimension(260, 18));
        chkSchedule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkScheduleActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 10);
        panCheckBoxes.add(chkSchedule, gridBagConstraints);

        tabDayEndList.addTab("DayEnd Process", panCheckBoxes);

        panTable.setMinimumSize(new java.awt.Dimension(536, 150));
        panTable.setPreferredSize(new java.awt.Dimension(536, 150));
        panTable.setLayout(new java.awt.GridBagLayout());

        scrOutputMsg.setMaximumSize(new java.awt.Dimension(150, 100));
        scrOutputMsg.setMinimumSize(new java.awt.Dimension(150, 100));
        scrOutputMsg.setPreferredSize(new java.awt.Dimension(150, 100));

        tblLog.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblLog.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblLogMousePressed(evt);
            }
        });
        scrOutputMsg.setViewportView(tblLog);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTable.add(scrOutputMsg, gridBagConstraints);

        tabDayEndList.addTab("DayEnd Process Result", panTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBatchProcess.add(tabDayEndList, gridBagConstraints);
        tabDayEndList.getAccessibleContext().setAccessibleName("DayEndProcess");

        getContentPane().add(panBatchProcess, java.awt.BorderLayout.CENTER);

        btnProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnProcess.setToolTipText("Process");
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });
        tbrHead.add(btnProcess);

        lblSpace4.setText("     ");
        tbrHead.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrHead.add(btnPrint);

        lblSpace24.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace24.setText("     ");
        lblSpace24.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace24);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrHead.add(btnClose);

        getContentPane().add(tbrHead, java.awt.BorderLayout.NORTH);

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

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void tblLogMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLogMousePressed
        // TODO add your handling code here:
        // authorizechk UI call
        int selectedRow = tblLog.getSelectedRow();
        EnhancedTableModel model = (EnhancedTableModel)tblLog.getModel();
        if (model.getValueAt(selectedRow, 1).toString().equals("ERROR")) {
            String viewName = CommonUtil.convertObjToStr(observable.getTaskMap().get(model.getValueAt(selectedRow, 0)));
            String taskLable =  CommonUtil.convertObjToStr(model.getValueAt(selectedRow, 0).toString());
            ViewHTMLDataUI objViewHTMLDataUI = new ViewHTMLDataUI(viewName,taskLable);
            //            ViewHTMLDataUI objViewHTMLDataUI = new ViewHTMLDataUI(viewName);
            objViewHTMLDataUI.show();
        }
    }//GEN-LAST:event_tblLogMousePressed
    
    private void btnTransCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransCheckActionPerformed
        // TODO add your handling code here:
        complete = false;
        if(daily.equals("daily")){
            boolean value;
            if(comp.equals("comp")) {
                value=false;
            } else {
                value= true;
            }
            btnDailyActivityActionPerformed(null);
            //           btnComplete1ActionPerformed(null);
            //        ClientUtil.enableDisable(panCheckBoxes,false);
            if (!taskRunning){
                observable.resetAll();
                //                TaskHeader header = null ;
                //
                //                 HashMap taskParamMap = new HashMap();
                //                    taskParamMap.put("DAY_END_TYPE", dayEndType);
                //                    taskParamMap.put("GL_ABS_TASK_LABLE", "GL Abstract");
                //                    taskParamMap.put("DAILY_BAL_UPDATE_TASK_LABLE", "Update Daily Balance");
                //                    taskParamMap.put("TOD_UPDATION", "TOD_UPDATION");
                //                header = getTaskFromMap();
                //                header.setProcessType(CommonConstants.DAY_END);
                //
                //
                ////                    header = getTaskFromMap();
                ////                    header.setProcessType(CommonConstants.DAY_END);
                //                    header.setTaskParam(taskParamMap);
                //
                //                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkGL_Abstract.getText())));
                //                observable.setEachTask(chkGL_Abstract, header);
                ////                header = getTaskFromMap();
                //                header = getTaskFromMap();
                //                    header.setProductType("");
                //                    header.setTaskParam(taskParamMap);
                ////                header.setProductType("");
                //                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkDailyBalance.getText())));
                //                observable.setEachTask(chkDailyBalance, header);
                //
                //                updateTable();
                //                threadPool = new ThreadPool(observable);
                //                threadPool.setTaskList(observable.getTaskList());
                //                threadPool.initPooledThread();
                //                threadPool.start();
                //                 String displayDetailsStr = "";
                //            String[] obj ={"Yes","No"};
                //            boolean isError = false;
                
                
                chkGL_Abstract.setSelected(true);
                //                chkGL_Abstract.setEnabled(false);
                chkDailyBalance.setSelected(true);
                //chkSchedule.setSelected(true);
                chkDividendCalculation.setSelected(false);
                chkAcCreditFromDebit.setSelected(false);
                chkReverseFlexiPrematureClosing.setSelected(false);
                chkInterestPayable.setSelected(false);
                chkDepositInt.setSelected(false);
                chkIntCalc.setSelected(false);
                chkNPA.setSelected(false);
//                chkDepositAutoRenewal.setSelected(false);
                chkFlexi.setSelected(false);
                chkFolio.setSelected(false);
                chkInterestReceivable.setSelected(false);
                chkExcessWithdrawlChrg.setSelected(false);
                chkNonMiniBalanceChrg.setSelected(false);
                chkDividendCalculation.setSelected(false);
                chkLapse_Transfer.setSelected(false);
                chkReverseFlexiPrematureClosing.setEnabled(false);
                chkInterestPayable.setEnabled(false);
                chkDepositInt.setEnabled(false);
                chkIntCalc.setEnabled(false);
                chkNPA.setEnabled(false);
//                chkDepositAutoRenewal.setEnabled(false);
                chkFlexi.setEnabled(false);
                chkFolio.setEnabled(false);
                chkInterestReceivable.setEnabled(false);
                chkExcessWithdrawlChrg.setEnabled(false);
                chkNonMiniBalanceChrg.setEnabled(false);
                chkDividendCalculation.setEnabled(false);
                chkLapse_Transfer.setEnabled(false);
                //                chkDailyBalance.setEnabled(false);
                //                taskRunning = true;
                //                            ClientUtil.enableDisable(panBatchProcess , false);
                comp="comp";
                btnDailyActivity.setEnabled(false);
                btnComplete1.setEnabled(false);
                btnComplete.setEnabled(false);
                //                btnTransCheck.setEnabled(false);
                chkAcCreditFromDebit.setEnabled(false);
                if(isAllTskComp){
                    btnComplete.setEnabled(true);
                }
            }
            
            //        chkCABalChk.setSelected(value);
            //        chkODBalChk.setSelected(value);
            //        btnProcess.setEnabled(value);
            //            btnDailyActivity.setEnabled(true);
            //            btnComplete.setEnabled(true);
            //            btnComplete1.setEnabled(true);
            //            panCheckBoxes.setEnabled(true);
        } else{
            ClientUtil.showAlertWindow("Check atleast one Daily Activity");
            btnProcess.setEnabled(false);
        }
        //        comp="comp";
        
        btnProcess.setEnabled(true);
    }//GEN-LAST:event_btnTransCheckActionPerformed
    private List getUnAuthorizedData(HashMap map, boolean header, boolean footer){
        map.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        List unAuthList = ClientUtil.executeQuery("getNonAuthorizedPreviousdaysRecords", map) ;
        System.out.println("unAuthList:" + unAuthList);
        return unAuthList;
    }
	
    private void btnDailyActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDailyActivityActionPerformed
        // TODO add your handling code here:
        complete = false;
        if (dayEndType.equals("BANK_LEVEL")) {
            return;
        }
        //        chkCABalChk.setVisible(true);
        boolean value=false;
        if(chk.equals("chk")){
            //             value;
            btnComplete1ActionPerformed(null);
            if(daily.equals("daily")) {
                value=false;
            } else {
                value =true;
            }
            
            String displayDetailsStr = "";
            //            String[] obj ={"Yes","No"};
            //            boolean isError = false;
            
            HashMap mapData=new HashMap();
            mapData.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            mapData.put("DAYEND_DT", currDt.clone());
            StringBuffer presentTasks = new StringBuffer();
            presentTasks.append("'" + chkUsersLogout.getText() + "'" +
            "," + "'" + chkCashAndTransferAuthChk.getText() + "'" +
            "," + "'" + chkCAAcctOpenPendingApproval.getText() + "'" +
            "," + "'" + chkUsrCashUntallied.getText() + "'");
            mapData.put("TASK_NAME", presentTasks);
            List lstData = ((List)ClientUtil.executeQuery("getSelectTransChkReady", mapData));
            mapData = null;
            if(lstData != null && lstData.size() > 0){
                List cashAndTransList = null;
                for(int j=0;j<lstData.size();j++){
                    mapData = (HashMap)lstData.get(j);
                    if(TrueTransactMain.pendingTxnAllowedDays>0){
//                        if(TrueTransactMain.pendingTxnAllowedDays>=5){
//                            TrueTransactMain.pendingTxnAllowedDays = 5;
//                        }
                        System.out.println("TrueTransactMain.pendingTxnAllowedDays : " + TrueTransactMain.pendingTxnAllowedDays);
                        HashMap map1 = new HashMap();
                        map1.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                        map1.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
                        map1.put("USER_ID", ProxyParameters.USER_ID);
                        List lst1 = ClientUtil.executeQuery("CashAndTransferAuth_view", map1);
                        if(lst1!=null && lst1.size()>0){
                            for(int i = 0; i < lst1.size(); i++){
                                HashMap cashAndTransMap = (HashMap)lst1.get(i);
                                if(cashAndTransMap.get("TABLE_NAME").equals("CASH_TRANS") || cashAndTransMap.get("TABLE_NAME").equals("TRANSFER_TRANS")){
                                    cashAndTransList = getUnAuthorizedData((HashMap)lst1.get(i), false, false) ;
                                    if(cashAndTransList !=null && cashAndTransList.size()>0){
                                        break;
                                    }
//                                    cashAndTransMap = (HashMap)cashAndTransList.get(i);
//                                    Date transDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(cashAndTransMap.get("TRANS_DT")));
////                                    if(DateUtil.dateDiff(transDt,(Date)currDt.clone())>=TrueTransactMain.pendingTxnAllowedDays){
//                                        output.append(DateUtil.getStringDate(CommonUtil.getProperDate(currDt, transDt))+",");
////                                    }
                                }
                            }
                        }
                    }else{
                        if(CommonUtil.convertObjToStr(mapData.get("TASK_STATUS")).equals("ERROR")){
                            //            isError = true;
                            displayDetailsStr = "Error in Task";
                            System.out.println("FFFFF"+displayDetailsStr);

                            //executeBranchList.remove(i--);
                            //System.out.println("FFFFF"+executeBranchList);
                            break;
                        }
                    }
                }
                if(cashAndTransList !=null && cashAndTransList.size()>0){
                    StringBuffer output = new StringBuffer();
                    output.append("");
                    for(int i = 0;i<cashAndTransList.size();i++){
                        HashMap cashAndTransMap = (HashMap)cashAndTransList.get(i);
                        Date transDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(cashAndTransMap.get("TRANS_DT")));
                        if(DateUtil.dateDiff(transDt,(Date)currDt.clone())>=TrueTransactMain.pendingTxnAllowedDays){
                            output.append(DateUtil.getStringDate(CommonUtil.getProperDate((Date)currDt.clone(), transDt))+",");
                        }else if(output.length()>0){
                            output.append(DateUtil.getStringDate(CommonUtil.getProperDate((Date)currDt.clone(), transDt))+",");
                        }
                    }
                    if(output.length()>0){
                        displayDetailsStr = "Cash/Transfer transactions pending authorization for "+output.toString()+" Authorize these transactions and then proceed with Dayend ";
                    }
                }
            }else{
                //                                    isError = true;
                displayDetailsStr += "Run atleast one Perform Check";
                System.out.println("FFFFF"+displayDetailsStr);
                //                                     executeBranchList.remove(i--);
                //                                     System.out.println("FFFFF"+executeBranchList);
            }
            mapData=new HashMap();
            mapData.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            mapData.put("DAYEND_DT", currDt.clone());
            presentTasks = new StringBuffer();
            presentTasks.append("'" + chkGL_Abstract.getText() + "'" +
            "," + "'" + chkDailyBalance.getText() + "'");
            mapData.put("TASK_NAME", presentTasks);
            lstData = ((List)ClientUtil.executeQuery("getSelectTransChkReady", mapData));
            mapData = null;
            if(lstData != null && lstData.size() > 0){
                for(int j=0;j<lstData.size();j++){
                    mapData = (HashMap)lstData.get(j);
                    if(CommonUtil.convertObjToStr(mapData.get("TASK_STATUS")).equals("COMPLETED")){
                        //            isError = true;
                        displayDetailsStr = "TransCheck already complete";
                        //                        btnComplete.setEnabled(true);
                        btnTransCheck.setEnabled(true);
                        if(daily.equals("daily")){
                            isAllTskComp = true;
                        }else{
                            isAllTskComp = false;
                        }
                        daily="daily";
                        System.out.println("FFFFF"+displayDetailsStr);
                        //executeBranchList.remove(i--);
                        //System.out.println("FFFFF"+executeBranchList);
                        break;
                    }
                }
            }
            
            if(displayDetailsStr.length() > 0){
                ClientUtil.showMessageWindow(displayDetailsStr);
            }
            //            if(taskRunning)
            //                return;
            //            if(dailyBalance){
            //                chkDailyBalance.setSelected(value);
            //                btnProcessActionPerformed(null);
            //            }
            //            dailyBalance=false;
            //            if(taskRunning)
            //                return;
            //            chkDailyBalance.setSelected(false);
            //            chkFolio.setSelected(value);
            //            btnProcessActionPerformed(null);
            
            else{
                enableDisableTaskChks(true);
                enableDisableTaskPerChks(false);
                chkInterestPayable.setSelected(false);
                //        chkDepositAutoRenewal.setSelected(value);
                //        chkDailyBalance.setSelected(value);
                chkDepositInt.setSelected(false);
                //        chkExcessWithdrawlChrg.setSelected(value);
                chkIntCalc.setSelected(false);
                chkNPA.setSelected(value);
                //             chkDailyBalance.setVisible(true);//for testing prrpose
                //            chkDailyBalance.setEnabled(true); //for testing prrpose
                //            chkDailyBalance.setSelected(true);//for testing prrpose
                chkDividendCalculation.setVisible(false);
                chkDividendCalculation.setEnabled(false);
                chkDividendCalculation.setSelected(false);
                chkAcCreditFromDebit.setSelected(false);
                chkTransOptInOpt.setSelected(false);
                chkTransOptInOpt.setVisible(false);
                //        chkNonMiniBalanceChrg.setSelected(value);
                chkInterestReceivable.setSelected(false);
                //            btnProcess.setEnabled(true);
                //            btnComplete.setEnabled(true);
                //            btnComplete1.setEnabled(true);
                
                btnComplete1.setEnabled(false);
                btnProcess.setEnabled(true);
                btnTransCheck.setEnabled(false);
                btnComplete.setEnabled(false);
                chkGL_Abstract.setEnabled(false);
                chkDailyBalance.setEnabled(false);
                chkCABalChk.setEnabled(false);
                chkODBalChk.setEnabled(false);
                
                //        ClientUtil.enableDisable(panCheckBoxes,false);
                dailyBalance=true;
                daily="daily";
            }
            
        }else{
            ClientUtil.showAlertWindow("Check atleast one Perform Check");
        }
        
        
    }//GEN-LAST:event_btnDailyActivityActionPerformed
    
    private void btnComplete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnComplete1ActionPerformed
        // TODO add your handling code here:
        if (evt != null) {
            Date Actualdate = new Date();
            Date actDt = (Date)currDt.clone();
            actDt.setDate(Actualdate.getDate());
            actDt.setMonth(Actualdate.getMonth());
            actDt.setYear(Actualdate.getYear());       
            //System.out.println("date diff testing ::"+ DateUtil.dateDiff(actDt, currDt));
            if (DateUtil.dateDiff(actDt, currDt) > 0) {
                ClientUtil.showMessageWindow("You are going to do the Future Date Day end. Please check");
            }
        }      
        
        HashMap eodMap = new HashMap();
        eodMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        eodMap.put("DAYEND_DT", currDt.clone());
        List lst = ClientUtil.executeQuery("getBranchLevelDayendSta", eodMap);
        if(lst != null && lst.size()>0){
            boolean value=false;
            if(chk.equals("chk")) {
                value=false;
            } else {
                value=true;
            }
            //        chkUsersLogout.setSelected(value);
            chkCAAcctOpenPendingApproval.setSelected(value);
            chkDepMature.setSelected(value);
            chkDepMature.setSelected(value);
            //        chkCashAndTransferAuthChk.setSelected(value);
            chkClearingSchkLeft.setSelected(value);
            //        chkCCBalChk.setSelected(value);
            chkContraAcctHd.setSelected(value);
            chkTDBalChk.setSelected(value);
            chkCAInopBalChk.setSelected(value);
            chkZeroBalAcHd.setSelected(value);
            //        chkInterBranchAuthRec.setSelected(value);
            chkTLBalChk.setSelected(value);
            chkCAAcctOpenZeroBal.setSelected(value);
            chkUsrCashUntallied.setSelected(value);
            chkClosingCashDebit.setSelected(value);
            chkTotCrAndDr.setSelected(value);
            btnProcess.setEnabled(value);
            btnDailyActivity.setEnabled(false);
            btnComplete1.setEnabled(false);
            btnTransCheck.setEnabled(false);
            btnComplete.setEnabled(false);
            chkMaturedAccounts.setSelected(value);
            chkUnSecuredAccounts.setSelected(value);
            //chkLockerOp.setSelected(value); // Added by nithya on 21-12-2021 for KD-3164
            chkLockerOp.setSelected(false);
            chkLockerOp.setEnabled(false);
            //chkExecuteLockerRentSi.setSelected(value);
            chkExecuteLockerRentSi.setEnabled(false);
            chkExecuteLockerRentSi.setSelected(false);
            //            enableDisableTaskChks(false);
            //        btnTransCheck.setEnabled(false);
            chk="chk";
            //        btnDailyActivity.setEnabled(false);
            //        btnComplete.setEnabled(false);
            //        ClientUtil.enableDisable(panCheckBoxes ,false);
            //        btnComplete1.setEnabled(false);
            if (dayEndType.equals("BANK_LEVEL")) {
                comp = "comp";
                daily = "daily";
            }
            enableDisableTaskChks(false);
            
        }else{
            ClientUtil.showMessageWindow("Day End already Completed");
            btnProcess.setEnabled(false);
            btnDailyActivity.setEnabled(false);
            btnTransCheck.setEnabled(false);
            btnComplete.setEnabled(false);
        }
    }//GEN-LAST:event_btnComplete1ActionPerformed
    
    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        // TODO add your handling code here:
        if (chkSelectAll.isSelected()) {
            selectAllCheckBoxes(panCheckBoxes, true);
        } else {
            selectAllCheckBoxes(panCheckBoxes, false);
        }
    }//GEN-LAST:event_chkSelectAllActionPerformed
    
    private void selectAllCheckBoxes(java.awt.Container comp, boolean yesno) {
        java.awt.Component[] children = comp.getComponents();
        
        for (int i = 0; i < children.length; i++) {
            if ((children[i] != null)) {
                if (children[i] instanceof javax.swing.JCheckBox){
                    ((javax.swing.JCheckBox)children[i]).setSelected(yesno);
                }
            }
        }
    }
    private void enableDisableTaskChks(boolean value){
        chkAcCreditFromDebit.setEnabled(value);
        chkReverseFlexiPrematureClosing.setEnabled(value);
        chkInterestPayable.setEnabled(value);
        chkDepositInt.setEnabled(value);
        chkIntCalc.setEnabled(value);
        chkNPA.setEnabled(value);
//        chkDepositAutoRenewal.setEnabled(value);
        chkFlexi.setEnabled(value);
        chkFolio.setEnabled(value);
        chkInterestReceivable.setEnabled(value);
        chkExcessWithdrawlChrg.setEnabled(value);
        chkNonMiniBalanceChrg.setEnabled(value);
        chkDividendCalculation.setEnabled(value);
        chkLapse_Transfer.setEnabled(value);
        chkGL_Abstract.setEnabled(value);
        chkDailyBalance.setEnabled(value);
        chkCABalChk.setEnabled(value);
        chkODBalChk.setEnabled(value);
        chkTransOptInOpt.setEnabled(value);
    }
    
    private void enableDisableTaskPerChks(boolean value){
        chkUsersLogout.setEnabled(value);
        chkCashAndTransferAuthChk.setEnabled(value);
        chkCAAcctOpenPendingApproval.setEnabled(value);
        chkDepMature.setEnabled(value);
        chkClosingCashDebit.setEnabled(value);
        chkContraAcctHd.setEnabled(value);
        chkTLBalChk.setEnabled(value);
        chkTDBalChk.setEnabled(value);
        chkCAInopBalChk.setEnabled(value);
        chkUsrCashUntallied.setEnabled(value);
        chkTotCrAndDr.setEnabled(value);
        chkInterBranchAuthRec.setEnabled(value);
        chkZeroBalAcHd.setEnabled(value);
        chkClearingSchkLeft.setEnabled(value);
        chkPendingChkForClearing.setEnabled(value);
        chkCAAcctOpenZeroBal.setEnabled(value);
        chkCCBalChk.setEnabled(value);
    }
    private String getStringDate(java.util.Date date){
        StringBuffer strB = new StringBuffer("");
        
        if (date != null) {
            int year = date.getYear() + 1900;
            int month = date.getMonth() + 1;
            int day = date.getDate();
            
            if (month < 10) {
                strB.append("0");
            }
            strB.append(month);
            strB.append("/");
            if (day < 10) {
                strB.append("0");
            }
            strB.append(day);
            strB.append("/");
            strB.append(year);
        }
        return strB.toString();
    }
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        COptionPane.showMessageDialog(this, "Please wait... Day End Printing Started");
        btnComplete.setText("Day Book");
        HashMap paramMap = new HashMap();
        paramMap.put("N0_OF_ROWS_PER_PAGE", "all");
        paramMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        paramMap.put("DAY_BOOK_DATE", getStringDate((Date) currDt.clone()));
        ClientUtil.printReport("DayBook", paramMap);
        
        btnComplete.setText("Cash Scroll");
        paramMap = new HashMap();
        paramMap.put("N0_OF_ROWS_PER_PAGE", "all");
        paramMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        paramMap.put("TRANSACTION_DATE", getStringDate((Date) currDt.clone()));
        ClientUtil.printReport("CashScroll", paramMap);
        
        btnComplete.setText("Transfer Scroll");
        paramMap = new HashMap();
        paramMap.put("N0_OF_ROWS_PER_PAGE", "all");
        paramMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        paramMap.put("TRANSACTION_DATE", getStringDate((Date) currDt.clone()));
        ClientUtil.printReport("TransferScroll", paramMap);
        
        btnComplete.setText("Inward Clearing Scroll");
        paramMap = new HashMap();
        paramMap.put("N0_OF_ROWS_PER_PAGE", "all");
        paramMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        paramMap.put("TRANSACTION_DATE", getStringDate((Date) currDt.clone()));
        ClientUtil.printReport("InwardClearingScroll", paramMap);
        
        btnComplete.setText("Outward Clearing Scroll");
        paramMap = new HashMap();
        paramMap.put("N0_OF_ROWS_PER_PAGE", "all");
        paramMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        paramMap.put("TRANSACTION_DATE", getStringDate((Date) currDt.clone()));
        ClientUtil.printReport("OutwardClearingScroll", paramMap);
        
        btnComplete.setText("Balance Sheet");
        paramMap = new HashMap();
        paramMap.put("N0_OF_ROWS_PER_PAGE", "all");
        paramMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        ClientUtil.printReport("GL_Abstract", paramMap);
        btnComplete.setText("Complete");
    }//GEN-LAST:event_btnPrintActionPerformed
    
    private void btnCompleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCompleteActionPerformed
        boolean isError = false;
        boolean isIntegrityError=false; //added by rishad 28 jan 2018 for related data intgrity
        // complete = true; //Added by kannan
        complete = false;
        //        if(comp.equals("comp")||chk.equals("chk") ||daily.equals("daily")   ){
        for (int i = 0, j = tblLog.getRowCount(); i < j; i++) {
            if (tblLog.getValueAt(i, 1).toString().equals("ERROR")) {
                isError = true;
                break;
            }
        }

        if (isError) {
            COptionPane.showMessageDialog(this, "Error in Task");
            isError = false;
            return;
        }
        if (comp.equals("comp") && daily.equals("daily")) {
            btnComplete1ActionPerformed(null);
            //            btnDailyActivityActionPerformed(null);
            //            btnTransCheckActionPerformed(null);
            if (dayEndType.equals("BRANCH_LEVEL")) {
                chkCABalChk.setSelected(true);
                chkODBalChk.setSelected(true);
                btnTransCheck.setEnabled(true);
                chkCABalChk.setSelected(true);
                chkGL_Abstract.setSelected(false);
                chkDailyBalance.setSelected(false);
                chkSchedule.setSelected(true);
                chkSchedule.setEnabled(false);
            }
            btnProcessActionPerformed(null);
            for (int i=0, j=tblLog.getRowCount(); i < j; i++) {
                if (tblLog.getValueAt(i, 1).toString().equals("ERROR")) {
                    System.out.println("tblLog.getValueAt(i, 1).toString()"+tblLog.getValueAt(i, 0).toString());
                  if(!(tblLog.getValueAt(i, 0).toString().equalsIgnoreCase("ScheduleCheck")||tblLog.getValueAt(i, 0).toString().equalsIgnoreCase("TransactionDataCheck")))
                  { isError = true;
                    break;}
                  else
                  {
                      isIntegrityError=true;
                  }
                }
            }
            
            if (!isError) {
                
                //            btnPrintActionPerformed(null);
                int yesno=1;
                if (isIntegrityError) {
                    List lstIntegrity = ClientUtil.executeQuery("getMinDataIntegrityDt", null);
                    if (lstIntegrity != null && lstIntegrity.size() > 0) {
                        HashMap intgrityMap = (HashMap) lstIntegrity.get(0);
                        Date transDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(intgrityMap.get("TRANS_DT")));
                        if (DateUtil.dateDiff(transDt, (Date) currDt.clone()) <= TrueTransactMain.pendingTxnAllowedDays) {
                            yesno = COptionPane.showConfirmDialog(this, "Integrity Issue Exist,Do you want to Complete Day End?", "Note", COptionPane.YES_NO_OPTION);
                        } else {
                            COptionPane.showMessageDialog(this, "You cannot Complete the Day End. Recify the Errors");
                            isError = false;
                        }
                    } else {
                        if (TrueTransactMain.pendingTxnAllowedDays < 1) {
                            COptionPane.showMessageDialog(this, "You cannot Complete the Day End. Recify the Errors");
                            isError = false;
                        } else {
                            yesno = COptionPane.showConfirmDialog(this, "Integrity Issue Exist,Do you want to Complete Day End?", "Note", COptionPane.YES_NO_OPTION);
                        }
                    }
                } else {
                    yesno = COptionPane.showConfirmDialog(this, "Do you want to Complete Day End?", "Note", COptionPane.YES_NO_OPTION);
                }

                if (yesno==COptionPane.YES_OPTION) {
                    HashMap eodMap = new HashMap();
                    //                if (!taskRunning){
                    //                        observable.resetAll();
                    //                        TaskHeader header = null ;
                    //
                    //                            header = getTaskFromMap();
                    //                            header.setProcessType(CommonConstants.DAY_END);
                    //                            header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkGL_Abstract.getText())));
                    //                            observable.setEachTask(chkGL_Abstract, header);
                    //
                    //                            updateTable();
                    //                            threadPool = new ThreadPool(observable);
                    //                            threadPool.setTaskList(observable.getTaskList());
                    //                            threadPool.initPooledThread();
                    //                            threadPool.start();
                    //                            taskRunning = true;
                    //                            ClientUtil.enableDisable(panBatchProcess , false);
                    //                        }
                    //
                    
                    eodMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                    ClientUtil.execute("updateDayEnd", eodMap);
                    if ((dayEndType.equals("BANK_LEVEL")) || (dayEndType.equals("BRANCH_LEVEL"))) {
                        eodMap.put("BRANCH_DAY_END_STATUS", "COMPLETED");
                        eodMap.put("DAYEND_DT", currDt.clone());
                        List lst = ClientUtil.executeQuery("getBranchLevelDayendStatus", eodMap);
                        if(lst != null && lst.size()>0){
                            //do nothing
                        }else{
                            eodMap.put("USER_ID",ProxyParameters.USER_ID);//added this list to call businessdeligatebean todo further activity
                            List logoutLst = ClientUtil.executeQuery("getLogoutUserList", eodMap);
                            if(logoutLst!=null && logoutLst.size()>0){
                            	eodMap.put("LOGOUT_LIST",logoutLst);
                            }
                            ClientUtil.execute("insertDailyDayendStatusFinal", eodMap);
                        }
                    }
                    updateTrueTransactMainTree();
                }
                
            } else {
                COptionPane.showMessageDialog(this, "You cannot Complete the Day End. Recify the Errors");
                isError = false;
            }
        }else{
            ClientUtil.showMessageWindow("plz check TransCheck");
        }
        
        
    }//GEN-LAST:event_btnCompleteActionPerformed
            private void chkUsrCashUntalliedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkUsrCashUntalliedActionPerformed
                                }//GEN-LAST:event_chkUsrCashUntalliedActionPerformed
            
            private void updateTrueTransactMainTree() {
                javax.swing.JTree treModules = TrueTransactMain.getTree();
                HashMap map = new HashMap();
                map.put(CommonConstants.MAP_WHERE, TrueTransactMain.GROUP_ID);
                GroupOB treeOB = new GroupOB(false);
                
                HashMap whereMap = new HashMap();
                whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                String strStatus = CommonUtil.convertObjToStr(ClientUtil.executeQuery("chkTransactionAllowed", whereMap).get(0));
                ArrayList grantScreen = new ArrayList();
                if (strStatus.equalsIgnoreCase("COMPLETED")) {
                    whereMap.put("GROUP_ID", TrueTransactMain.GROUP_ID);
                    List lst = (List) ClientUtil.executeQuery("getGroupScreenDataForDayBegin", whereMap);
                    for (int i=0; i<lst.size(); i++) {
                        whereMap = (HashMap) lst.get(i);
                        //                whereMap.put("screenId","SCR01041");
                        //                whereMap.put("screenClass",null);
                        //                whereMap.put("screenName","Day Begin");
                        //                whereMap.put("screenSlNo","1");
                        //                whereMap.put("moduleId","14");
                        //                whereMap.put("moduleSlNo","16");
                        //                whereMap.put("moduleName","Periodic Activity");
                        //                whereMap.put("newAllowed","Y");
                        //                whereMap.put("editAllowed","Y");
                        //                whereMap.put("deleteAllowed","Y");
                        //                whereMap.put("authRejAllowed","Y");
                        //                whereMap.put("exceptionAllowed","Y");
                        //                whereMap.put("printAllowed","Y");
                        //                whereMap.put("interbranchAllowed", null);
                        grantScreen.add(whereMap);
                    }
                    treeOB.setGrantScreen(grantScreen);
                    grantScreen = null;
                } else {
                    treeOB.populateData(map);
                }
                treModules.setModel(treeOB.getGrantScreenModel());
                map = null;
                treeOB = null;
                whereMap = null;
                this.dispose();
        }                    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
            // TODO add your handling code here:
            
            boolean isError = false;
            for (int i = 0, j = tblLog.getRowCount(); i < j; i++) {
                if (tblLog.getValueAt(i, 1).toString().equals("ERROR")) {
                    isError = true;
                    break;
                }
            }

            if (isError) {
                COptionPane.showMessageDialog(this, "Error in Task");
                isError = false;
                return;
            }

            String achdId = "";
            HashMap achdMap = new HashMap();
            List achdList = ClientUtil.executeQuery("getAllUnauthorizedAchdForDayEnd", achdMap);
            for(int i=0;i<achdList.size();i++){
            achdMap = (HashMap)achdList.get(i);
            achdId += achdMap.get("AC_HD_ID").toString()+"("+achdMap.get("AC_HD_DESC").toString()+")\n";
        }
            if(achdId!=null && !achdId.equals("")){
            ClientUtil.showAlertWindow("Please authorize these account head\n"+achdId);
            return;
            }
            //                        btnDailyActivity.setEnabled(false);
            //                        btnTransCheck.setEnabled(false);
            //                        btnComplete1.setEnabled(false);
            //                        btnComplete.setEnabled(false);
            //                        chkDepositInt.setSelected(false);
            //                        chkInterestPayable.setSelected(false);
            HashMap map1 = new HashMap();
            map1.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            map1.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            map1.put("USER_ID", ProxyParameters.USER_ID);
            List lst = ClientUtil.executeQuery("getUserLogoutStatus", map1);
            List lst1 = ClientUtil.executeQuery("CashAndTransferAuth_view", map1);
            List authorizationPendingAccounts = ClientUtil.executeQuery("getViewData", map1);
            if (lst != null && lst.size() > 0) {
                selectAllCheckBoxes(panCheckBoxes, false);
                btnComplete1.setEnabled(true);
                //                         chkUsersLogout.setSelected(true);
                //                         chkCashAndTransferAuthChk.setSelected(true);
                //                         btnComplete1.setEnabled(true);
            }
            if(lst1!=null&& lst1.size()>0){
                selectAllCheckBoxes(panCheckBoxes, false);
                btnComplete1.setEnabled(true);
                chkUsersLogout.setSelected(true);
                chkCashAndTransferAuthChk.setSelected(true);
            }
            if(lst!=null&& lst.size()>0){
                chkUsersLogout.setSelected(true);
            }
            if(lst1!=null&& lst1.size()>0){
                chkCashAndTransferAuthChk.setSelected(true);
            }
            if (authorizationPendingAccounts != null && authorizationPendingAccounts.size() > 0) {
                chkCAAcctOpenPendingApproval.setSelected(true);
            }
            chkUsersLogout.setSelected(true);
            chkCashAndTransferAuthChk.setSelected(true);
            TaskHeader header = null ;
            if (!taskRunning){
                observable.resetAll();
                if (chkGL_Abstract.isSelected()){
                    header = getTaskFromMap();
                    header.setProcessType(CommonConstants.DAY_END);
                    HashMap map = new HashMap();
                    map.put("DAY_END_TYPE", dayEndType);
                    map.put("GL_ABS_TASK_LABLE", "GL Abstract");
                    header.setTaskParam(map);
                    header.setProcessType(CommonConstants.DAY_END);
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkGL_Abstract.getText())));
                    observable.setEachTask(chkGL_Abstract, header);
                }
                if (chkCAAcctOpenPendingApproval.isSelected()){
                    header = getTaskFromMap();
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkCAAcctOpenPendingApproval.getText())));
                    observable.setEachTask(chkCAAcctOpenPendingApproval, header);
                }
                
                if (chkDepMature.isSelected()){
                    header = getTaskFromMap();
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkDepMature.getText())));
                    observable.setEachTask(chkDepMature, header);
                }
                
                if(chkTransOptInOpt.isSelected()){
                    header = getTaskFromMap();
                    HashMap paramMap = new HashMap();
                    paramMap.put("CHK_TRN_OPT_TASK_LABLE", "Transfer of Operative Account to Inoperative");
                    header.setTaskParam(paramMap);
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkTransOptInOpt.getText())));
                    observable.setEachTask(chkTransOptInOpt, header);
                }
//                if (chkCABalChk.isSelected()){ ///commented by rishad 28 jan 2018 checking included in integrity checking
//                    header = getTaskFromMap();
//                    HashMap map = new HashMap();
//                    map.put("OA_BAL_CHK_TASK_LABLE", chkCABalChk.getText());
//                    header.setTaskParam(map);
//                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkCABalChk.getText())));
//                    observable.setEachTask(chkCABalChk, header);
//                }
                if (chkCAAcctOpenZeroBal.isSelected()){
                    header = getTaskFromMap();
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkCAAcctOpenZeroBal.getText())));
                    observable.setEachTask(chkCAAcctOpenZeroBal, header);
                }
                if (chkCAInopBalChk.isSelected()){
                    header = getTaskFromMap();
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkCAInopBalChk.getText())));
                    observable.setEachTask(chkCAInopBalChk, header);
                }
                if (chkCCBalChk.isSelected()){
                    observable.setTabTask(chkCCBalChk);
                }
                if (chkClearingSchkLeft.isSelected()){
                    header = getTaskFromMap();
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkClearingSchkLeft.getText())));
                    observable.setEachTask(chkClearingSchkLeft, header);
                }
                if (chkClosingCashDebit.isSelected()){
                    header = getTaskFromMap();
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkClosingCashDebit.getText())));
                    observable.setEachTask(chkClosingCashDebit, header);
                }
                if(chkIntCalc.isSelected()){
                    header=getTaskFromMap();
                    HashMap map = new HashMap();
                    map.put("INT_CAL_TASK_LABLE", chkIntCalc.getText());
                    header.setTaskParam(map);
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkIntCalc.getText())));
                    observable.setEachTask(chkIntCalc,header);
                    // observable.setTabTask(chkIntCalc);
                }
//                if (chkODBalChk.isSelected()){ ///commented by rishad 28 jan 2018 checking included in integrity checking
//                    header = getTaskFromMap();
//                    HashMap map = new HashMap();
//                    map.put("BAL_CHK_TASK_LABLE", chkODBalChk.getText());
//                    header.setTaskParam(map);
//                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkODBalChk.getText())));
//                    header.setProcessType(CommonConstants.DAY_END);
//                    observable.setEachTask(chkODBalChk, header);
//                }
                if (chkContraAcctHd.isSelected()){
                    header = getTaskFromMap();
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkContraAcctHd.getText())));
                    observable.setEachTask(chkContraAcctHd, header);
                }
                if (chkPendingChkForClearing.isSelected()){
                    observable.setTabTask(chkPendingChkForClearing);
                }
                if (chkAcCreditFromDebit.isSelected()){
                    header = getTaskFromMap();
                    HashMap map = new HashMap();
                    header.setProductType("OA");
                    header.setTaskParam(map);
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkAcCreditFromDebit.getText())));
                    observable.setEachTask(chkAcCreditFromDebit, header);
                }
                if (chkTDBalChk.isSelected()){
                    header = getTaskFromMap();
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkTDBalChk.getText())));
                    observable.setEachTask(chkTDBalChk, header);
                }
                if (chkTLBalChk.isSelected()){
                    header = getTaskFromMap();
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkTLBalChk.getText())));
                    observable.setEachTask(chkTLBalChk, header);
                }
                if (chkTotCrAndDr.isSelected()){
                    header = getTaskFromMap();
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkTotCrAndDr.getText())));
                    observable.setEachTask(chkTotCrAndDr, header);
                }
                if (chkUsrCashUntallied.isSelected()){
                    header = getTaskFromMap();
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkUsrCashUntallied.getText())));
                    header.setProcessType(CommonConstants.DAY_END);
                    observable.setEachTask(chkUsrCashUntallied, header);
                }
                if (chkExcessWithdrawlChrg.isSelected()){
                    header = getTaskFromMap();
                    HashMap map = new HashMap();
                    map.put("TASK_LABLE", chkExcessWithdrawlChrg.getText());
                    header.setTaskParam(map);
                    header.setProductType("OA");
                    header.setTransactionType("EXCESSTRANSCHARGE");
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkExcessWithdrawlChrg.getText())));
                    observable.setEachTask(chkExcessWithdrawlChrg, header);
                }
                if (chkNonMiniBalanceChrg.isSelected()){
                    //                                observable.setTabTask(chkNonMiniBalanceChrg);
                    header = getTaskFromMap();
                    HashMap map = new HashMap();
                    map.put("MIN_BAL_TASK_LABLE", chkNonMiniBalanceChrg.getText());
                    header.setTaskParam(map);
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkNonMiniBalanceChrg.getText())));
                    header.setTransactionType("MIN_BALANCE");
                    observable.setEachTask(chkNonMiniBalanceChrg, header);
                }
                if (chkFlexi.isSelected()){
                    header = getTaskFromMap();
                    HashMap map = new HashMap();
                    map.put("FLEXI_TASK_LABLE", chkFlexi.getText());
                    header.setTaskParam(map);
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkFlexi.getText())));
                    observable.setEachTask(chkFlexi, header);
                }
                if(chkReverseFlexiPrematureClosing.isSelected()){
                    header = getTaskFromMap();
                    HashMap map = new HashMap();
                    map.put("REV_FLEXI_TASK_LABLE", chkReverseFlexiPrematureClosing.getText());
                    header.setTaskParam(map);
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkReverseFlexiPrematureClosing.getText())));
                    observable.setEachTask(chkReverseFlexiPrematureClosing,header);
                }
                if (chkDepositAutoRenewal.isSelected()){
                    header = getTaskFromMap();
                    HashMap map = new HashMap();
                    map.put("DEP_AUTO_REN_TASK_LABLE", chkDepositAutoRenewal.getText());
                    header.setTaskParam(map);
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkDepositAutoRenewal.getText())));
                    observable.setEachTask(chkDepositAutoRenewal, header);
                }
                if (chkDepositInt.isSelected()){
                    header = getTaskFromMap();
                    HashMap map = new HashMap();
                    map.put("PROCESS",CommonConstants.DAY_END);
                    map.put("DEP_INT_TASK_LABLE", chkDepositInt.getText());
                    header.setTaskParam(map);
                    header.setProcessType(CommonConstants.DAY_END);
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkDepositInt.getText())));
                    observable.setEachTask(chkDepositInt, header);
                }
                if (chkInterestPayable.isSelected()){
                    header = getTaskFromMap();
                    HashMap map = new HashMap();
                    map.put("INT_TASK_LABLE", chkInterestPayable.getText());
                    header.setProductType(null);
                    header.setTransactionType(CommonConstants.PAYABLE);
                    header.setTaskParam(map);
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkInterestPayable.getText())));
                    observable.setEachTask(chkInterestPayable, header);
                }
                if (chkInterestReceivable.isSelected()){
                    HashMap map = new HashMap();
                    header = getTaskFromMap();
                    map = new HashMap();
                    map.put("DEBIT_INT_TASK_LABLE", chkInterestReceivable.getText());
                    header.setProductType("OA");
                    header.setTransactionType(CommonConstants.RECEIVABLE);
                    header.setTaskParam(map);
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkInterestReceivable.getText())));
                    observable.setEachTask(chkInterestReceivable, header);
                }
                if (chkDailyBalance.isSelected()){
                    header = getTaskFromMap();
                    header.setProcessType(CommonConstants.DAY_END);
                    HashMap map = new HashMap();
                    map.put("DAILY_BAL_UPDATE_TASK_LABLE", "Update Daily Balance");
                    map.put("DAY_END_TYPE", dayEndType);
                    map.put("TOD_UPDATION", "TOD_UPDATION");
                    header.setTaskParam(map);
                    //                                taskParamMap.put("DAILY_BAL_UPDATE_TASK_LABLE", "Update Daily Balance");
                    header.setProductType("");
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkDailyBalance.getText())));
                    observable.setEachTask(chkDailyBalance, header);
                    //                                header = getTaskFromMap();
                    //                                header.setProductType("AD");
                    //                                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkDailyBalance.getText())));
                    //                                observable.setEachTask(chkDailyBalance, header);
                }
                if (chkUsersLogout.isSelected()){
                    header = getTaskFromMap();
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkUsersLogout.getText())));
                    observable.setEachTask(chkUsersLogout, header);
                }
                if (chkDividendCalculation.isSelected()){
                    header = getTaskFromMap();
                    HashMap map = new HashMap();
                    map.put("TASK_LABLE", chkDividendCalculation.getText());
                    header.setTaskParam(map);
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkDividendCalculation.getText())));
                    observable.setEachTask(chkDividendCalculation, header);
                }
                if (chkNPA.isSelected()){
                    header = getTaskFromMap();
                    HashMap map = new HashMap();
                    map.put("NPA_TASK_LABLE", chkNPA.getText());
                    header.setTaskParam(map);
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkNPA.getText())));
                    observable.setEachTask(chkNPA, header);
                }
                if (chkInterBranchAuthRec.isSelected()){
                    header = getTaskFromMap();
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkInterBranchAuthRec.getText())));
                    observable.setEachTask(chkInterBranchAuthRec, header);
                }
                if (chkZeroBalAcHd.isSelected()){
                    header = getTaskFromMap();
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkZeroBalAcHd.getText())));
                    observable.setEachTask(chkZeroBalAcHd, header);
                }
                if (chkCashAndTransferAuthChk.isSelected()){
                    header = getTaskFromMap();
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkCashAndTransferAuthChk.getText())));
                    observable.setEachTask(chkCashAndTransferAuthChk, header);
                }
                if (chkFolio.isSelected()){
                    header = getTaskFromMap();
                    HashMap map = new HashMap();
                    map.put("FOLIO_TASK_LABLE", chkFolio.getText());
                    header.setTaskParam(map);
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkFolio.getText())));
                    observable.setEachTask(chkFolio, header);
                }
                if(chkLapse_Transfer.isSelected()){
                    header = getTaskFromMap();
                    HashMap map = new HashMap();
                    map.put("REMIT_LAPSE_TASK_LABLE", chkLapse_Transfer.getText());
                    header.setTaskParam(map);
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkLapse_Transfer.getText())));
                    observable.setEachTask(chkLapse_Transfer, header);
                }
                
                if(chkMaturedAccounts.isSelected()){
                    header = getTaskFromMap();                    
                    header.setProcessType(CommonConstants.DAY_END);                    
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkMaturedAccounts.getText())));
                    observable.setEachTask(chkMaturedAccounts, header);
                }
                if(chkUnSecuredAccounts.isSelected()){
                   header = getTaskFromMap();                    
                    header.setProcessType(CommonConstants.DAY_END);                    
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkUnSecuredAccounts.getText())));
                    observable.setEachTask(chkUnSecuredAccounts, header); 
                }
                if(chkLockerOp.isSelected()){
                   header = getTaskFromMap();                    
                    header.setProcessType(CommonConstants.DAY_END);                    
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkLockerOp.getText())));
                    observable.setEachTask(chkLockerOp, header); 
                }
                if(chkExecuteLockerRentSi.isSelected()){
                   header = getTaskFromMap();                    
                    header.setProcessType(CommonConstants.DAY_END);                    
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkExecuteLockerRentSi.getText())));
                    observable.setEachTask(chkExecuteLockerRentSi, header); 
                }

                if (chkGL_Abstract.isSelected()) { //chkGL_Abstract.isSelected() TIMING COMMENTED
                    header = getTaskFromMap();
                    header.setProcessType(CommonConstants.DAY_END);
                    HashMap map = new HashMap();
                    map.put("DAY_END_TYPE", dayEndType);
                    header.setTaskParam(map);
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get("DifferentDateInterBranchCheck")));
                    observable.setEachTask("DifferentDateInterBranchCheck", header);
                }
                if (this.chkSchedule.isSelected()) {
                    header = getTaskFromMap();
                    header.setProcessType("DAY_END");
                    HashMap map = new HashMap();
                    map.put("DAY_END_TYPE", this.dayEndType);
                    header.setTaskParam(map);
                    header.setTaskClass(CommonUtil.convertObjToStr(this.observable.getTaskMap().get("ScheduleCheck")));
                    this.observable.setEachTask("ScheduleCheck", header);
                }

                if (this.chkSchedule.isSelected()) {
                    header = getTaskFromMap();
                    header.setProcessType("DAY_END");
                    HashMap map = new HashMap();
                    map.put("DAY_END_TYPE", this.dayEndType);
                    header.setTaskParam(map);
                    header.setTaskClass(CommonUtil.convertObjToStr(this.observable.getTaskMap().get("TransactionDataCheck")));
                    this.observable.setEachTask("TransactionDataCheck", header);
                }
                updateTable();
                threadPool = new ThreadPool(observable);
                threadPool.setTaskList(observable.getTaskList());
                threadPool.initPooledThread();
                threadPool.start();
                taskRunning = true;
                //                            ClientUtil.enableDisable(panBatchProcess , false);
                //                            this.tskStatus = observable.getTaskStatus();
                //                            this.tskHeader = observable.getTaskHeader();
                //                            updateChkBoxTableBasedOnStatus();
                ClientUtil.enableDisable(panBatchProcess , false, false, true);                
                if(!complete){
                    CommonUtil comm = new CommonUtil();
                    loading = comm.addProgressBar();
                    loading.show();
                }
            }else{
                final String warnMessage = "Process is running";
                displayAlert(warnMessage);
            }
            //                        btnDailyActivity.setEnabled(true);
            //                        btnComplete.setEnabled(true);
            //                        btnComplete1.setEnabled(false);
            //                        btnTransCheck.setEnabled(true);
            btnProcess.setEnabled(false);
    }//GEN-LAST:event_btnProcessActionPerformed
        private void updateTable(){
            tblLog.setModel(observable.getDayBeginTableModel());
			boolean taskCompleted = true;

			if (loading != null && !complete) {
				ArrayList tableList = observable.getDayBeginTableModel().getDataArrayList();
				if (tableList != null && tableList.size() > 0) {
					for (int i = 0; i < tableList.size(); i++) {
						if (!CommonUtil.convertObjToStr(((ArrayList) tableList.get(i)).get(1)).equals("COMPLETED") && !CommonUtil.convertObjToStr(((ArrayList) tableList.get(i)).get(1)).equals("ERROR")) {
							taskCompleted = false;
							break;
						}
					}
				}
				if (taskCompleted) {
					loading.dispose();
				}
			}
        }
        
        private void displayAlert(String message){
            final CMandatoryDialog cmd = new CMandatoryDialog();
            cmd.setMessage(message);
            cmd.setModal(true);
            cmd.show();
        }
        /**To update the Values in the Table...*/
        public void update(java.util.Observable o, Object arg) {
            this.tskStatus = observable.getTaskStatus();
            this.tskHeader = observable.getTaskHeader();
            updateChkBoxTableBasedOnStatus();
        }
        
        private void updateChkBoxTableBasedOnStatus(){
            boolean enable = false;
            if (tskHeader.getTaskClass().equals("GlAbstractUpdateTask")){
                enable = observable.updateTaskStatusInLogTable(chkGL_Abstract);
            }
            else if (tskHeader.getTaskClass().equals("SelectCABalance")){
                enable = observable.updateTaskStatusInLogTable(chkCABalChk);
            }
            else if (tskHeader.getTaskClass().equals("UnclearedScheduleCheckTask")){
                enable = observable.updateTaskStatusInLogTable(chkClearingSchkLeft);
            }
            else if (tskHeader.getTaskClass().equals("SelectCAInoperatvieBalance")){
                enable = observable.updateTaskStatusInLogTable(chkCAInopBalChk);
            }
            else if (tskHeader.getTaskClass().equals("ExcessTransChrgesTask")){
                enable = observable.updateTaskStatusInLogTable(chkExcessWithdrawlChrg);
            }
            else if (tskHeader.getTaskClass().equals("AuthorizationCheckTask")){
                enable = observable.updateTaskStatusInLogTable(chkCAAcctOpenPendingApproval);
            }
            else if (tskHeader.getTaskClass().equals("DepositMaturingCheckTask")){
                enable = observable.updateTaskStatusInLogTable(chkDepMature);
            }
            else if (tskHeader.getTaskClass().equals("DormantToInOperativeTask")){
                enable = observable.updateTaskStatusInLogTable(chkTransOptInOpt);
            }
            else if (tskHeader.getTaskClass().equals("InwardTallyOutClrgCheckTask")){
                enable = observable.updateTaskStatusInLogTable(chkTDBalChk);
            }
            else if (tskHeader.getTaskClass().equals("OutwdClrgPaySlipTallyCheckTask")){
                enable = observable.updateTaskStatusInLogTable(chkTLBalChk);
            }
            else if (tskHeader.getTaskClass().equals("TransferTallyCheckTask")){
                enable = observable.updateTaskStatusInLogTable(chkTotCrAndDr);
            }
            else if (tskHeader.getTaskClass().equals("ZeroBalanceCheckTask")){
                enable = observable.updateTaskStatusInLogTable(chkCAAcctOpenZeroBal);
            }
            else if (tskHeader.getTaskClass().equals("CashTallyCheckTask")){
                enable = observable.updateTaskStatusInLogTable(chkUsrCashUntallied);
            }
            else if (tskHeader.getTaskClass().equals("OABalanceCheckTask")){
                enable = observable.updateTaskStatusInLogTable(chkCABalChk);
            }
            else if (tskHeader.getTaskClass().equals("BalanceCheckTask")){
                enable = observable.updateTaskStatusInLogTable(chkODBalChk);
            }
            else if (tskHeader.getTaskClass().equals("ContraHeadCheckTask")){
                enable = observable.updateTaskStatusInLogTable(chkContraAcctHd);
            }
            else if (tskHeader.getTaskClass().equals("CashInHandCheckTask")){
                enable = observable.updateTaskStatusInLogTable(chkClosingCashDebit);
            }
            else if (tskHeader.getTaskClass().equals("OAInoperativeBalanceCheckTask")){
                enable = observable.updateTaskStatusInLogTable(chkCAInopBalChk);
            }
            else if (tskHeader.getTaskClass().equals("FlexiTask")){
                enable = observable.updateTaskStatusInLogTable(chkFlexi);
            }
            else if (tskHeader.getTaskClass().equals("ReverseFlexiTask")){
                enable = observable.updateTaskStatusInLogTable(chkReverseFlexiPrematureClosing);
            }
            else if (tskHeader.getTaskClass().equals("DepositAutoRenewalTask")){
                enable = observable.updateTaskStatusInLogTable(chkDepositAutoRenewal);
            }
            else if (tskHeader.getTaskClass().equals("DepositIntTask")){
                enable = observable.updateTaskStatusInLogTable(chkDepositInt);
            }
            else if (tskHeader.getTaskClass().equals("DebitIntTask")){
                enable = observable.updateTaskStatusInLogTable(chkInterestReceivable);
            }
            else if (tskHeader.getTaskClass().equals("InterestTask")){
                enable = observable.updateTaskStatusInLogTable(chkInterestPayable);
            }
            else if (tskHeader.getTaskClass().equals("DailyBalanceUpdateTask")){
                enable = observable.updateTaskStatusInLogTable(chkDailyBalance);
            }
            else if (tskHeader.getTaskClass().equals("UserCheckTask")){
                enable = observable.updateTaskStatusInLogTable(chkUsersLogout);
            }
            else if (tskHeader.getTaskClass().equals("DividendCalcTask")){
                enable = observable.updateTaskStatusInLogTable(chkDividendCalculation);
            }
            else if (tskHeader.getTaskClass().equals("NPATask")){
                enable = observable.updateTaskStatusInLogTable(chkNPA);
            }
            else if (tskHeader.getTaskClass().equals("InterBranchAuthorizationCheck")){
                enable = observable.updateTaskStatusInLogTable(chkInterBranchAuthRec);
            }
            else if (tskHeader.getTaskClass().equals("ZeroBalanceAccountCheckTask")){
                enable = observable.updateTaskStatusInLogTable(chkZeroBalAcHd);
            }
            else if (tskHeader.getTaskClass().equals("CashAndTransferAuthCheckTask")){
                enable = observable.updateTaskStatusInLogTable(chkCashAndTransferAuthChk);
            }
            else if (tskHeader.getTaskClass().equals("InterestCalculationTask")){
                enable = observable.updateTaskStatusInLogTable(chkIntCalc);
            }
            
            else if (tskHeader.getTaskClass().equals("FolioChargesTask")){
                enable = observable.updateTaskStatusInLogTable(chkFolio);
            }
            else if (tskHeader.getTaskClass().equals("RemittanceLapseTransferTask")){
                enable = observable.updateTaskStatusInLogTable(chkLapse_Transfer);
            }
            else if (tskHeader.getTaskClass().equals("MinBalanceChargesTask")){
                enable = observable.updateTaskStatusInLogTable(chkNonMiniBalanceChrg);
            }
            else if (tskHeader.getTaskClass().equals("DebitToCreditCheckTask")){
                enable = observable.updateTaskStatusInLogTable(chkAcCreditFromDebit);
            }  else if (tskHeader.getTaskClass().equals("MaturedAccountsCheckTask")){
                enable = observable.updateTaskStatusInLogTable(chkMaturedAccounts);
            } else if (tskHeader.getTaskClass().equals("UnSecuredLoanAccountsCheckTask")){
                enable = observable.updateTaskStatusInLogTable(chkUnSecuredAccounts);
            } else if (tskHeader.getTaskClass().equals("LockerOperationCheckTask")){
                enable = observable.updateTaskStatusInLogTable(chkLockerOp);
            } else if (tskHeader.getTaskClass().equals("ExecuteLockerRentSiCheckTask")){
                enable = observable.updateTaskStatusInLogTable(chkExecuteLockerRentSi);
            } else if (tskHeader.getTaskClass().equals("DifferentDateInterBranchCheck")) {
                enable = observable.updateTaskStatusInLogTable("DifferentDateInterBranchCheck");
            } else if (tskHeader.getTaskClass().equals("ScheduleCheck")) {
                enable = observable.updateTaskStatusInLogTable("ScheduleCheck");
            }
            else if (tskHeader.getTaskClass().equals("TransactionDataCheck")) {
                enable = observable.updateTaskStatusInLogTable("TransactionDataCheck");
            }
            updateTable();
            if (enable){
                taskRunning = false;
                //                            ClientUtil.enableDisable(panBatchProcess , true);
                ClientUtil.enableDisable(panBatchProcess , true, true, true);
                ArrayList tblArrayList = observable.getDayBeginTableModel().getDataArrayList();
                if (tblArrayList!=null && tblArrayList.size()>0) {
                    observable.doExecute(tblArrayList);
                    //                                java.util.Date currDt = currDt.clone();
                    //                                Date sqlCurrDt = new Date(currDt.getTime());
                    //                                java.util.Properties serverProperties = new java.util.Properties();
                    //                                try {
                    //                                    Dummy cons = new Dummy();
                    //                                    serverProperties.load(cons.getClass().getResourceAsStream("/com/see/truetransact/serverutil/SqlMapConfig.properties"));
                    //                                    String dataBaseURL = serverProperties.getProperty("url");
                    //                                    String userName = serverProperties.getProperty("username");
                    //                                    String passWord = serverProperties.getProperty("password");
                    //                                    DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                    //                                    Connection conn = DriverManager.getConnection(dataBaseURL, userName, passWord);
                    //                                    Statement stmt = null;
                    //                                    ArrayList tempList = null;
                    //                                    java.sql.CallableStatement cst = null;
                    //                                    for (int i=0; i<tblArrayList.size(); i++) {
                    //                                        tempList = (ArrayList) tblArrayList.get(i);
                    //                                        stmt = conn.createStatement();
                    //                                        cst = conn.prepareCall("{call EXECUTE_DAYEND_STATUS ( ?, ?, ?, ?, ? )}");
                    //                                        cst.setString(1,TrueTransactMain.BRANCH_ID);
                    //                                        cst.setString(2,CommonUtil.convertObjToStr(tempList.get(0)));
                    //                                        cst.setString(3,CommonUtil.convertObjToStr(tempList.get(1)));
                    //                                        cst.setString(4,TrueTransactMain.USER_ID);
                    //                                        cst.setDate(5,sqlCurrDt);
                    //                                        cst.executeQuery();
                    //                                    }
                    //                                    cst = null;
                    //                                    serverProperties = null;
                    //                                    cons = null;
                    //                                    conn = null;
                    //                                    stmt = null;
                    //                                    tempList = null;
                    //                                } catch (Exception ex) {
                    //                                    System.out.println("#$#$ Exception in SelectAllDAO getReportData() : "+ex);
                    //                                    ex.printStackTrace();
                    //                                }
                }
                tblArrayList=null;
                threadPool.stopAllThreads();
            }
        }
        
        private TaskHeader getTaskFromMap() { //String value, HashMap map, String transType, String prodType, String process) {
            TaskHeader tskHeader = new TaskHeader();
            try{
                //            tskHeader.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(value)));
                
                /** To pass the Parameter Map to the Task to be performed... */
                //            tskHeader.setTaskParam(map);
                //            tskHeader.setTransactionType(transType);
                //            tskHeader.setProductType(prodType);
                //            tskHeader.setProcessType(process);
                tskHeader.setBranchID(ProxyParameters.BRANCH_ID);
                tskHeader.setBankID(ProxyParameters.BANK_ID);
                tskHeader.setUserID(ProxyParameters.USER_ID);
                tskHeader.setIpAddr(java.net.InetAddress.getLocalHost().getHostAddress());
            }catch(Exception E){
                System.out.println("Error in Setting the Task Header...");
                E.printStackTrace();
            }
            return tskHeader;
        }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        observable.resetAll();
        if (threadPool!=null) {
            threadPool.stopAllThreads();
        }
        updateTable();
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void chkLockerOpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkLockerOpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkLockerOpActionPerformed

    private void chkExecuteLockerRentSiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkExecuteLockerRentSiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkExecuteLockerRentSiActionPerformed

    private void chkDepositAutoRenewalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkDepositAutoRenewalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkDepositAutoRenewalActionPerformed

    private void chkScheduleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkScheduleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkScheduleActionPerformed
    public static void main(String str[]) {
        javax.swing.JFrame frm = new javax.swing.JFrame();
        DayEndProcessUI db = new DayEndProcessUI();
        frm.getContentPane().add(db);
        frm.show();
        db.show();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnComplete;
    private com.see.truetransact.uicomponent.CButton btnComplete1;
    private com.see.truetransact.uicomponent.CButton btnDailyActivity;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CButton btnTransCheck;
    private com.see.truetransact.uicomponent.CCheckBox chkAcCreditFromDebit;
    private com.see.truetransact.uicomponent.CCheckBox chkCAAcctOpenPendingApproval;
    private com.see.truetransact.uicomponent.CCheckBox chkCAAcctOpenZeroBal;
    private com.see.truetransact.uicomponent.CCheckBox chkCABalChk;
    private com.see.truetransact.uicomponent.CCheckBox chkCAInopBalChk;
    private com.see.truetransact.uicomponent.CCheckBox chkCCBalChk;
    private com.see.truetransact.uicomponent.CCheckBox chkCashAndTransferAuthChk;
    private com.see.truetransact.uicomponent.CCheckBox chkClearingSchkLeft;
    private com.see.truetransact.uicomponent.CCheckBox chkClosingCashDebit;
    private com.see.truetransact.uicomponent.CCheckBox chkContraAcctHd;
    private com.see.truetransact.uicomponent.CCheckBox chkDailyBalance;
    private com.see.truetransact.uicomponent.CCheckBox chkDepMature;
    private com.see.truetransact.uicomponent.CCheckBox chkDepositAutoRenewal;
    private com.see.truetransact.uicomponent.CCheckBox chkDepositInt;
    private com.see.truetransact.uicomponent.CCheckBox chkDividendCalculation;
    private com.see.truetransact.uicomponent.CCheckBox chkExcessWithdrawlChrg;
    private com.see.truetransact.uicomponent.CCheckBox chkExecuteLockerRentSi;
    private com.see.truetransact.uicomponent.CCheckBox chkFlexi;
    private com.see.truetransact.uicomponent.CCheckBox chkFolio;
    private com.see.truetransact.uicomponent.CCheckBox chkGL_Abstract;
    private com.see.truetransact.uicomponent.CCheckBox chkIntCalc;
    private com.see.truetransact.uicomponent.CCheckBox chkInterBranchAuthRec;
    private com.see.truetransact.uicomponent.CCheckBox chkInterestPayable;
    private com.see.truetransact.uicomponent.CCheckBox chkInterestReceivable;
    private com.see.truetransact.uicomponent.CCheckBox chkLapse_Transfer;
    private com.see.truetransact.uicomponent.CCheckBox chkLockerOp;
    private com.see.truetransact.uicomponent.CCheckBox chkMaturedAccounts;
    private com.see.truetransact.uicomponent.CCheckBox chkNPA;
    private com.see.truetransact.uicomponent.CCheckBox chkNonMiniBalanceChrg;
    private com.see.truetransact.uicomponent.CCheckBox chkODBalChk;
    private com.see.truetransact.uicomponent.CCheckBox chkPendingChkForClearing;
    private com.see.truetransact.uicomponent.CCheckBox chkReverseFlexiPrematureClosing;
    private com.see.truetransact.uicomponent.CCheckBox chkSchedule;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CCheckBox chkTDBalChk;
    private com.see.truetransact.uicomponent.CCheckBox chkTLBalChk;
    private com.see.truetransact.uicomponent.CCheckBox chkTotCrAndDr;
    private com.see.truetransact.uicomponent.CCheckBox chkTransOptInOpt;
    private com.see.truetransact.uicomponent.CCheckBox chkUnSecuredAccounts;
    private com.see.truetransact.uicomponent.CCheckBox chkUsersLogout;
    private com.see.truetransact.uicomponent.CCheckBox chkUsrCashUntallied;
    private com.see.truetransact.uicomponent.CCheckBox chkZeroBalAcHd;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace24;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CPanel panBatchProcess;
    private com.see.truetransact.uicomponent.CPanel panCheckBoxes;
    private com.see.truetransact.uicomponent.CPanel panComplete;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CScrollPane scrOutputMsg;
    private com.see.truetransact.uicomponent.CSeparator sptSpace1;
    private com.see.truetransact.uicomponent.CTabbedPane tabDayEndList;
    private com.see.truetransact.uicomponent.CTable tblLog;
    private javax.swing.JToolBar tbrHead;
    // End of variables declaration//GEN-END:variables
}
