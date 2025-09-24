/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DCDayEndProcessUI.java
 *
 * Created on August 17, 2004, 4:49 PM
 */

package com.see.truetransact.ui.batchprocess;

import java.util.HashMap;
import java.util.List;

import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants ;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.transferobject.sysadmin.config.ConfigPasswordTO;
import com.see.truetransact.ui.batchprocess.authorizechk.ViewHTMLDataUI;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.ui.TrueTransactMain;

import java.util.ArrayList;
import java.sql.*;


/**
 *
 * @author bala
 */
public class DCDayEndProcessUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer{
    private TaskStatus tskStatus;
    private TaskHeader tskHeader;
    private ThreadPool threadPool;
    private ProcessOB observable = null;
    private boolean taskRunning;
    private boolean dailyBalance=true;
    private boolean doConti = true;
    private HashMap branMap = new HashMap();
    private java.util.Date currDate;
    private boolean isCheck = false;
    String chk="";
    String daily="";
    String comp="";
    String dayEndType = "";
    String dayEndPrev = "";
    private String CURR_APPL_DT = "CURR_APPL_DT";
    private String BRANCH_LST = "BRANCH_LST";
    
    /** Creates new form BatchProcessUI */
    public DCDayEndProcessUI() {
        initComponents();
        setObservable();
        setupInit();
       
        chkGL_Abstract.setVisible(false);
        chkSelectAll.setVisible(false);
        selectAllCheckBoxes(panCheckBoxes, false);
        btnProcess.setEnabled(false);
        chkDividendCalculation.setVisible(false);
        panCheckBoxes.setEnabled(false);
        chkDailyBalance.setVisible(false);
//        chkDepositAutoRenewal.setVisible(false);
        chkExcessWithdrawlChrg.setVisible(false);
        chkNonMiniBalanceChrg.setVisible(false);
//        chkInterestReceivable.setVisible(false);
        chkNonMiniBalanceChrg.setVisible(true);
        chkReconcile.setVisible(false);
        chkReconcile.setSelected(false);
        HashMap test = new HashMap();
        List lst = ((List)ClientUtil.executeQuery("getSelectConfigPasswordTO", test));
        dayEndType =  lst!=null ? CommonUtil.convertObjToStr(((ConfigPasswordTO)lst.get(0)).getDayEndType()) : "";    
        if (dayEndType.equals("")) {
            ClientUtil.showAlertWindow("<html>Cause  : Unable to load Day End.<br>" +
            "Reason : DayEnd type not mentioned in Bank Configuration.</html>");
            panCheckBoxes.setVisible(false);
            panComplete.setVisible(false);
            scrOutputMsg.setVisible(false);
            btnProcess.setVisible(false);
        } else if (dayEndType.equals("BRANCH_LEVEL")) {
            setVisibleCheckBoxes(false);
            btnDailyActivity.setVisible(false);
        } else {
            setVisibleCheckBoxes(true);
            btnDailyActivity.setVisible(true);
        }
        test = null;
        lst = null;        
        currDate = ClientUtil.getCurrentDate();
    }
    
    private void setVisibleCheckBoxes (boolean value) {
            btnDailyActivity.setVisible(value);
            btnTransCheck.setVisible(value);
            chkReverseFlexiPrematureClosing.setVisible(value);
            chkInterestPayable.setVisible(value);
            chkDepositInt.setVisible(value);
            chkIntCalc.setVisible(value);
            chkNPA.setVisible(value);
            chkCABalChk.setVisible(value);
            chkODBalChk.setVisible(value);
            chkDepositAutoRenewal.setVisible(value);
            chkFlexi.setVisible(value);
            chkFolio.setVisible(value);
            chkNonMiniBalanceChrg.setVisible(value);  
            chkLapse_Transfer.setVisible(value);
            chkAcCreditFromDebit.setVisible(value);
            chkInterestReceivable.setVisible(value);
    }
    
    private void setupInit() {
        observable.resetAll();
        HashMap taskMap = new HashMap();
        taskMap.put(chkBranchDayEnd.getText(), "AllBranchDayEndTask");
        taskMap.put(chkReconcile.getText(), "AllBranchReconcileTask");
        taskMap.put(chkInterBranchAuthRec.getText(), "InterBranchAuthorizationCheck"); //Inter Branch Authorization
        taskMap.put(chkGL_Abstract.getText(), "GlAbstractUpdateTask"); //Create GL Abstarct for opening and closing balance
        taskMap.put(chkCABalChk.getText(), "OABalanceCheckTask");
        taskMap.put(chkIntCalc.getText(), "InterestCalculationTask");
        taskMap.put(chkODBalChk.getText(), "BalanceCheckTask"); // OD balance Chk
        taskMap.put(chkExcessWithdrawlChrg.getText(), "ExcessTransChrgesTask"); //Apply excess Withdrawl charges
        taskMap.put(chkNonMiniBalanceChrg.getText(), "MinBalanceChargesTask"); //Non maintenance of min bal
        taskMap.put(chkFlexi.getText(), "FlexiTask"); //Flexi Task
        taskMap.put(chkLapse_Transfer.getText(), "RemittanceLapseTransferTask");
        taskMap.put(chkDepositAutoRenewal.getText(), "DepositAutoRenewalTask"); //Deposit Auto Renewal
        taskMap.put(chkInterestPayable.getText(), "InterestTask"); //Interest Application - Payable
        taskMap.put(chkInterestReceivable.getText(), "DebitIntTask"); //Interest Application -  Receivable
        taskMap.put(chkAcCreditFromDebit.getText(), "DebitToCreditCheckTask"); //Interest Application -  Receivable
        taskMap.put(chkDepositInt.getText(), "DepositIntTask"); //Deposit Int Application
        taskMap.put(chkDailyBalance.getText(), "DailyBalanceUpdateTask"); //Daily Balance Update
        taskMap.put(chkDividendCalculation.getText(), "DividendCalcTask"); //Dividend Calculation and application
        taskMap.put(chkNPA.getText(), "NPATask"); //NPA Status change task
        taskMap.put(chkFolio.getText(),"FolioChargesTask");
        taskMap.put(chkReverseFlexiPrematureClosing.getText(), "ReverseFlexiTask");        
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
        tabDCDayBegin1 = new com.see.truetransact.uicomponent.CTabbedPane();
        panBatchProcess = new com.see.truetransact.uicomponent.CPanel();
        sptSpace1 = new com.see.truetransact.uicomponent.CSeparator();
        panCheckBoxes = new com.see.truetransact.uicomponent.CPanel();
        chkBranchDayEnd = new com.see.truetransact.uicomponent.CCheckBox();
        chkReconcile = new com.see.truetransact.uicomponent.CCheckBox();
        chkODBalChk = new com.see.truetransact.uicomponent.CCheckBox();
        chkIntCalc = new com.see.truetransact.uicomponent.CCheckBox();
        chkCABalChk = new com.see.truetransact.uicomponent.CCheckBox();
        chkNonMiniBalanceChrg = new com.see.truetransact.uicomponent.CCheckBox();
        chkExcessWithdrawlChrg = new com.see.truetransact.uicomponent.CCheckBox();
        chkFlexi = new com.see.truetransact.uicomponent.CCheckBox();
        chkDepositAutoRenewal = new com.see.truetransact.uicomponent.CCheckBox();
        chkDepositInt = new com.see.truetransact.uicomponent.CCheckBox();
        chkInterestPayable = new com.see.truetransact.uicomponent.CCheckBox();
        chkDailyBalance = new com.see.truetransact.uicomponent.CCheckBox();
        chkInterestReceivable = new com.see.truetransact.uicomponent.CCheckBox();
        chkDividendCalculation = new com.see.truetransact.uicomponent.CCheckBox();
        chkNPA = new com.see.truetransact.uicomponent.CCheckBox();
        chkInterBranchAuthRec = new com.see.truetransact.uicomponent.CCheckBox();
        chkGL_Abstract = new com.see.truetransact.uicomponent.CCheckBox();
        chkFolio = new com.see.truetransact.uicomponent.CCheckBox();
        chkReverseFlexiPrematureClosing = new com.see.truetransact.uicomponent.CCheckBox();
        chkLapse_Transfer = new com.see.truetransact.uicomponent.CCheckBox();
        chkAcCreditFromDebit = new com.see.truetransact.uicomponent.CCheckBox();
        panComplete = new com.see.truetransact.uicomponent.CPanel();
        btnComplete = new com.see.truetransact.uicomponent.CButton();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        btnComplete1 = new com.see.truetransact.uicomponent.CButton();
        btnDailyActivity = new com.see.truetransact.uicomponent.CButton();
        btnTransCheck = new com.see.truetransact.uicomponent.CButton();
        scrOutputMsg = new com.see.truetransact.uicomponent.CScrollPane();
        tblLog = new com.see.truetransact.uicomponent.CTable();
        panBatchProcess1 = new com.see.truetransact.uicomponent.CPanel();
        sptSpace2 = new com.see.truetransact.uicomponent.CSeparator();
        panCheckBoxes1 = new com.see.truetransact.uicomponent.CPanel();
        panMultiSearch = new com.see.truetransact.uicomponent.CPanel();
        tdtFromDate = new com.see.truetransact.uicomponent.CDateField();
        btnPrinted = new com.see.truetransact.uicomponent.CButton();
        chkSelectAll1 = new com.see.truetransact.uicomponent.CCheckBox();
        lblFromDate = new com.see.truetransact.uicomponent.CLabel();
        scrOutputMsg1 = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Data Center Day End Process");
        setPreferredSize(new java.awt.Dimension(750, 660));
        getContentPane().setLayout(new java.awt.BorderLayout(4, 4));

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

        tabDCDayBegin1.setMinimumSize(new java.awt.Dimension(740, 624));
        tabDCDayBegin1.setName("");
        tabDCDayBegin1.setPreferredSize(new java.awt.Dimension(740, 624));

        panBatchProcess.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panBatchProcess.setMinimumSize(new java.awt.Dimension(740, 624));
        panBatchProcess.setPreferredSize(new java.awt.Dimension(740, 624));
        panBatchProcess.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBatchProcess.add(sptSpace1, gridBagConstraints);

        panCheckBoxes.setMinimumSize(new java.awt.Dimension(536, 300));
        panCheckBoxes.setPreferredSize(new java.awt.Dimension(536, 300));
        panCheckBoxes.setLayout(new java.awt.GridBagLayout());

        chkBranchDayEnd.setSelected(true);
        chkBranchDayEnd.setText("Check for all Branches Day-End");
        chkBranchDayEnd.setMaximumSize(new java.awt.Dimension(213, 22));
        chkBranchDayEnd.setMinimumSize(new java.awt.Dimension(213, 22));
        chkBranchDayEnd.setPreferredSize(new java.awt.Dimension(213, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCheckBoxes.add(chkBranchDayEnd, gridBagConstraints);

        chkReconcile.setSelected(true);
        chkReconcile.setText("Inter Branch A/c maintained & reconciled");
        chkReconcile.setPreferredSize(new java.awt.Dimension(263, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCheckBoxes.add(chkReconcile, gridBagConstraints);

        chkODBalChk.setSelected(true);
        chkODBalChk.setText("Transaction Balance Check");
        chkODBalChk.setActionCommand("OD Balance Check");
        chkODBalChk.setMaximumSize(new java.awt.Dimension(185, 21));
        chkODBalChk.setMinimumSize(new java.awt.Dimension(185, 18));
        chkODBalChk.setPreferredSize(new java.awt.Dimension(268, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCheckBoxes.add(chkODBalChk, gridBagConstraints);

        chkIntCalc.setSelected(true);
        chkIntCalc.setText("Checks for Interest Calculations for OD / CC / TL");
        chkIntCalc.setMaximumSize(new java.awt.Dimension(307, 21));
        chkIntCalc.setMinimumSize(new java.awt.Dimension(307, 18));
        chkIntCalc.setPreferredSize(new java.awt.Dimension(315, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCheckBoxes.add(chkIntCalc, gridBagConstraints);

        chkCABalChk.setSelected(true);
        chkCABalChk.setText("Operative Account Balance Check");
        chkCABalChk.setMaximumSize(new java.awt.Dimension(223, 21));
        chkCABalChk.setMinimumSize(new java.awt.Dimension(223, 18));
        chkCABalChk.setPreferredSize(new java.awt.Dimension(268, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCheckBoxes.add(chkCABalChk, gridBagConstraints);

        chkNonMiniBalanceChrg.setSelected(true);
        chkNonMiniBalanceChrg.setText("Apply Non-Minimum Balance Maintenance Charges");
        chkNonMiniBalanceChrg.setMaximumSize(new java.awt.Dimension(315, 21));
        chkNonMiniBalanceChrg.setMinimumSize(new java.awt.Dimension(315, 18));
        chkNonMiniBalanceChrg.setPreferredSize(new java.awt.Dimension(325, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCheckBoxes.add(chkNonMiniBalanceChrg, gridBagConstraints);

        chkExcessWithdrawlChrg.setSelected(true);
        chkExcessWithdrawlChrg.setText("Apply Excess Withdrawl Charges");
        chkExcessWithdrawlChrg.setMaximumSize(new java.awt.Dimension(219, 21));
        chkExcessWithdrawlChrg.setMinimumSize(new java.awt.Dimension(219, 18));
        chkExcessWithdrawlChrg.setPreferredSize(new java.awt.Dimension(315, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCheckBoxes.add(chkExcessWithdrawlChrg, gridBagConstraints);

        chkFlexi.setText("Flexi Creation");
        chkFlexi.setMaximumSize(new java.awt.Dimension(73, 21));
        chkFlexi.setMinimumSize(new java.awt.Dimension(110, 18));
        chkFlexi.setPreferredSize(new java.awt.Dimension(268, 22));
        chkFlexi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkFlexiActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCheckBoxes.add(chkFlexi, gridBagConstraints);

        chkDepositAutoRenewal.setText("Deposit Auto Renewal");
        chkDepositAutoRenewal.setMaximumSize(new java.awt.Dimension(153, 21));
        chkDepositAutoRenewal.setMinimumSize(new java.awt.Dimension(153, 18));
        chkDepositAutoRenewal.setPreferredSize(new java.awt.Dimension(268, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCheckBoxes.add(chkDepositAutoRenewal, gridBagConstraints);

        chkDepositInt.setText("Deposit Interest Provision");
        chkDepositInt.setMaximumSize(new java.awt.Dimension(175, 21));
        chkDepositInt.setMinimumSize(new java.awt.Dimension(175, 18));
        chkDepositInt.setPreferredSize(new java.awt.Dimension(268, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCheckBoxes.add(chkDepositInt, gridBagConstraints);

        chkInterestPayable.setText("Interest Calculation - Payable");
        chkInterestPayable.setMaximumSize(new java.awt.Dimension(195, 21));
        chkInterestPayable.setMinimumSize(new java.awt.Dimension(195, 18));
        chkInterestPayable.setPreferredSize(new java.awt.Dimension(268, 22));
        chkInterestPayable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkInterestPayableActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCheckBoxes.add(chkInterestPayable, gridBagConstraints);

        chkDailyBalance.setSelected(true);
        chkDailyBalance.setText("Update Daily Balance");
        chkDailyBalance.setMaximumSize(new java.awt.Dimension(151, 21));
        chkDailyBalance.setMinimumSize(new java.awt.Dimension(151, 18));
        chkDailyBalance.setPreferredSize(new java.awt.Dimension(315, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCheckBoxes.add(chkDailyBalance, gridBagConstraints);

        chkInterestReceivable.setText("Interest Calculation - Receivable");
        chkInterestReceivable.setMaximumSize(new java.awt.Dimension(211, 21));
        chkInterestReceivable.setMinimumSize(new java.awt.Dimension(211, 18));
        chkInterestReceivable.setPreferredSize(new java.awt.Dimension(315, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCheckBoxes.add(chkInterestReceivable, gridBagConstraints);

        chkDividendCalculation.setText("Dividend Calculation");
        chkDividendCalculation.setMaximumSize(new java.awt.Dimension(141, 21));
        chkDividendCalculation.setMinimumSize(new java.awt.Dimension(141, 18));
        chkDividendCalculation.setPreferredSize(new java.awt.Dimension(315, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCheckBoxes.add(chkDividendCalculation, gridBagConstraints);

        chkNPA.setText("Non Performing Assets");
        chkNPA.setMaximumSize(new java.awt.Dimension(159, 21));
        chkNPA.setMinimumSize(new java.awt.Dimension(159, 18));
        chkNPA.setPreferredSize(new java.awt.Dimension(315, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCheckBoxes.add(chkNPA, gridBagConstraints);

        chkInterBranchAuthRec.setSelected(true);
        chkInterBranchAuthRec.setText("Check Inter Branch Records Authorization");
        chkInterBranchAuthRec.setMaximumSize(new java.awt.Dimension(269, 21));
        chkInterBranchAuthRec.setMinimumSize(new java.awt.Dimension(269, 18));
        chkInterBranchAuthRec.setPreferredSize(new java.awt.Dimension(268, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCheckBoxes.add(chkInterBranchAuthRec, gridBagConstraints);

        chkGL_Abstract.setText("GL Abstract");
        chkGL_Abstract.setMaximumSize(new java.awt.Dimension(117, 21));
        chkGL_Abstract.setMinimumSize(new java.awt.Dimension(117, 18));
        chkGL_Abstract.setPreferredSize(new java.awt.Dimension(315, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCheckBoxes.add(chkGL_Abstract, gridBagConstraints);

        chkFolio.setText("Folio Charges");
        chkFolio.setMaximumSize(new java.awt.Dimension(117, 21));
        chkFolio.setMinimumSize(new java.awt.Dimension(117, 18));
        chkFolio.setPreferredSize(new java.awt.Dimension(315, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCheckBoxes.add(chkFolio, gridBagConstraints);

        chkReverseFlexiPrematureClosing.setText("Reverse Flexi on Premature Closing");
        chkReverseFlexiPrematureClosing.setMaximumSize(new java.awt.Dimension(73, 21));
        chkReverseFlexiPrematureClosing.setMinimumSize(new java.awt.Dimension(235, 18));
        chkReverseFlexiPrematureClosing.setPreferredSize(new java.awt.Dimension(268, 22));
        chkReverseFlexiPrematureClosing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkReverseFlexiPrematureClosingActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCheckBoxes.add(chkReverseFlexiPrematureClosing, gridBagConstraints);

        chkLapse_Transfer.setText("Transfer lapsed PO/DD into lapse head");
        chkLapse_Transfer.setMaximumSize(new java.awt.Dimension(265, 18));
        chkLapse_Transfer.setMinimumSize(new java.awt.Dimension(265, 18));
        chkLapse_Transfer.setPreferredSize(new java.awt.Dimension(265, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCheckBoxes.add(chkLapse_Transfer, gridBagConstraints);

        chkAcCreditFromDebit.setText("Checks for Account Coming to Credit From Debit ");
        chkAcCreditFromDebit.setMaximumSize(new java.awt.Dimension(316, 21));
        chkAcCreditFromDebit.setMinimumSize(new java.awt.Dimension(316, 21));
        chkAcCreditFromDebit.setPreferredSize(new java.awt.Dimension(316, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCheckBoxes.add(chkAcCreditFromDebit, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panBatchProcess.add(panCheckBoxes, gridBagConstraints);

        panComplete.setMinimumSize(new java.awt.Dimension(170, 28));
        panComplete.setPreferredSize(new java.awt.Dimension(170, 24));
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

        btnDailyActivity.setText("Daily Actiity");
        btnDailyActivity.setMinimumSize(new java.awt.Dimension(105, 25));
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
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        panBatchProcess.add(panComplete, gridBagConstraints);

        scrOutputMsg.setMinimumSize(new java.awt.Dimension(200, 200));
        scrOutputMsg.setPreferredSize(new java.awt.Dimension(200, 200));

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
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panBatchProcess.add(scrOutputMsg, gridBagConstraints);

        tabDCDayBegin1.addTab("Data Center DayEnd", panBatchProcess);

        panBatchProcess1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panBatchProcess1.setMinimumSize(new java.awt.Dimension(740, 624));
        panBatchProcess1.setName("");
        panBatchProcess1.setPreferredSize(new java.awt.Dimension(740, 624));
        panBatchProcess1.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBatchProcess1.add(sptSpace2, gridBagConstraints);

        panCheckBoxes1.setMinimumSize(new java.awt.Dimension(536, 120));
        panCheckBoxes1.setPreferredSize(new java.awt.Dimension(536, 300));
        panCheckBoxes1.setLayout(new java.awt.GridBagLayout());

        panMultiSearch.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panMultiSearch.setMinimumSize(new java.awt.Dimension(700, 100));
        panMultiSearch.setPreferredSize(new java.awt.Dimension(700, 160));
        panMultiSearch.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        panMultiSearch.add(tdtFromDate, gridBagConstraints);

        btnPrinted.setText("Search");
        btnPrinted.setMaximumSize(new java.awt.Dimension(75, 23));
        btnPrinted.setMinimumSize(new java.awt.Dimension(75, 23));
        btnPrinted.setPreferredSize(new java.awt.Dimension(75, 23));
        btnPrinted.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panMultiSearch.add(btnPrinted, gridBagConstraints);

        chkSelectAll1.setText("Select All");
        chkSelectAll1.setMaximumSize(new java.awt.Dimension(81, 23));
        chkSelectAll1.setPreferredSize(new java.awt.Dimension(81, 23));
        chkSelectAll1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAll1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panMultiSearch.add(chkSelectAll1, gridBagConstraints);

        lblFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 3, 3);
        panMultiSearch.add(lblFromDate, gridBagConstraints);

        panCheckBoxes1.add(panMultiSearch, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panBatchProcess1.add(panCheckBoxes1, gridBagConstraints);

        scrOutputMsg1.setMinimumSize(new java.awt.Dimension(200, 200));
        scrOutputMsg1.setPreferredSize(new java.awt.Dimension(200, 200));

        tblData.setModel(new javax.swing.table.DefaultTableModel(
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
        tblData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDataMousePressed(evt);
            }
        });
        scrOutputMsg1.setViewportView(tblData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panBatchProcess1.add(scrOutputMsg1, gridBagConstraints);

        tabDCDayBegin1.addTab("Check for DayEnd", panBatchProcess1);

        getContentPane().add(tabDCDayBegin1, java.awt.BorderLayout.EAST);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblDataMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDataMousePressed

    private void chkSelectAll1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAll1ActionPerformed
        // TODO add your handling code here:
        observable.setSelectAll(new Boolean(chkSelectAll1.isSelected()));
        
    }//GEN-LAST:event_chkSelectAll1ActionPerformed

    private void btnPrintedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintedActionPerformed
        // TODO add your handling code here:
//        chkSelectAll1.setVisible(false);
        tblData.setEnabled(true);
//        flag = true;
//        txtAmount.setText("");
//        txtfavouring.setText("");
//        btnPrintReport.setEnabled(false);
        populateData();
    }//GEN-LAST:event_btnPrintedActionPerformed
public void populateData() {
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        viewMap.put(CommonConstants.MAP_NAME, "getDayEndCompList");
        whereMap.put(CURR_APPL_DT, tdtFromDate.getDateValue());
        System.out.println("#### where map : "+whereMap);
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        try {
//            log.info("populateData...");
            ArrayList heading = observable.populateData(viewMap, tblData);
        } catch( Exception e ) {
            System.err.println( "Exception " + e.toString() + "Caught" );
            e.printStackTrace();
        }
//        flag = false;
    }
    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkSelectAllActionPerformed

    private void chkFlexiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkFlexiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkFlexiActionPerformed

    private void chkInterestPayableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkInterestPayableActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkInterestPayableActionPerformed

    private void tblLogMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLogMousePressed
        // TODO add your handling code here:
        // authorizechk UI call
        int selectedRow = tblLog.getSelectedRow();
        EnhancedTableModel model = (EnhancedTableModel)tblLog.getModel();        
        if (model.getValueAt(selectedRow, 1).toString().equals("ERROR")) {
            if(dayEndPrev != null && dayEndPrev.length()>0){
                HashMap newMap = new HashMap();
                if(branMap != null && branMap.containsKey(BRANCH_LST)) {
                    newMap.put(BRANCH_LST, branMap.get(BRANCH_LST));
                }
                String viewName = CommonUtil.convertObjToStr(observable.getTaskMap().get(model.getValueAt(selectedRow, 0)));
                String taskLable =  CommonUtil.convertObjToStr(model.getValueAt(selectedRow, 0).toString());
                ViewHTMLDataUI objViewHTMLDataUI = new ViewHTMLDataUI(viewName,taskLable,newMap);
                objViewHTMLDataUI.show();
                
            }else{
                String viewName = CommonUtil.convertObjToStr(observable.getTaskMap().get(model.getValueAt(selectedRow, 0)));
                String taskLable =  CommonUtil.convertObjToStr(model.getValueAt(selectedRow, 0).toString());
                ViewHTMLDataUI objViewHTMLDataUI = new ViewHTMLDataUI(viewName,taskLable);
                objViewHTMLDataUI.show();
            }
        }    
    }//GEN-LAST:event_tblLogMousePressed

    private void btnCompleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCompleteActionPerformed
        // TODO add your handling code here:
        boolean isError = false;
//        if(comp.equals("comp")||chk.equals("chk") ||daily.equals("daily")   ){
            if(comp.equals("comp") || (chk.equals("chk") && !btnDailyActivity.isVisible())){
            btnComplete1ActionPerformed(null);
            int options = -1;
//            btnDailyActivityActionPerformed(null);
//            btnTransCheckActionPerformed(null);
              HashMap mapData = new HashMap();
                boolean isTransError = false;
                String displayDetailsStr = "";
                String[] obj ={"Yes","No"};
//                   HashMap mapData = new HashMap();
             List lst=null;
            if((branMap != null && branMap.containsKey(BRANCH_LST)) && (dayEndPrev != null && dayEndPrev.length()>0)){
                  List brnLst = (List)branMap.get(BRANCH_LST);
                  if(brnLst!=null && brnLst.size()>0){
                      HashMap brnMap = (HashMap)brnLst.get(0);
//                      mapData.put(CURR_APPL_DT, brnMap.get("CUR_BRAN_DT"));
                         HashMap whereMap = new HashMap();
                       whereMap.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(brnMap.get(CommonConstants.BRANCH_ID)));
                       brnLst = (List) ClientUtil.executeQuery("getApplDateHashMap", whereMap);
                       whereMap = null;
                       if(brnLst!=null && brnLst.size() > 0){  
                            whereMap = (HashMap)brnLst.get(0);
                            mapData.put(CURR_APPL_DT, whereMap.get(CURR_APPL_DT));
//                            currDt = (Date) whereMap.get(CURR_APPL_DT);
                       }
                      lst = ((List)ClientUtil.executeQuery("getSelectBranchCompletedLstPrevDay", mapData));
                  }

            }else{
                mapData.put("DAYEND_DT", currDate.clone());
                mapData.put("BRANCH_DAY_END_STATUS", "COMPLETED");
                mapData.put("DC_DAY_END_STATUS", "COMPLETED");
                lst = ((List)ClientUtil.executeQuery("getSelectBranchCompletedLst", mapData));
            }
//                mapData.put("DAYEND_DT", currDate.clone());
//                mapData.put("BRANCH_DAY_END_STATUS", "COMPLETED");
//                mapData.put("DC_DAY_END_STATUS", "COMPLETED");
//                List lst = ((List)ClientUtil.executeQuery("getSelectBranCompletedLst", mapData));
                ArrayList executeBranchList = new ArrayList();
                executeBranchList.addAll(lst);
                mapData = null;
                if(executeBranchList != null && executeBranchList.size() > 0){
                    for(int i=0;i<executeBranchList.size();i++){
                        mapData = (HashMap)executeBranchList.get(i);
//                        if(!CommonUtil.convertObjToStr(dayEndPrev).equalsIgnoreCase(""))
                            mapData.put("DAYEND_DT", mapData.get(CURR_APPL_DT));
//                        else
//                            mapData.put("DAYEND_DT", currDate.clone());
                        StringBuffer presentTasks = new StringBuffer();
                        presentTasks.append("'" + "GL Abstract" + "'" +
                        "," + "'" + "Update Daily Balance" + "'");
                        mapData.put("TASK_NAME", presentTasks);
                        List lstData = ((List)ClientUtil.executeQuery("getSelectTransChkReady", mapData));
                        mapData = null;
                        if(lstData != null && lstData.size() > 0){
                            for(int j=0;j<lstData.size();j++){
                                mapData = (HashMap)lstData.get(j);
                                if(CommonUtil.convertObjToStr(mapData.get("TASK_STATUS")).equals("ERROR")){
                                    isTransError = true;
                                    displayDetailsStr += "\n"+"Error Branch ID : "+CommonUtil.convertObjToStr(mapData.get("BRANCH_ID")+"\n");
                                     System.out.println("FFFFF"+displayDetailsStr);
                                     executeBranchList.remove(i--);
                                     System.out.println("FFFFF"+executeBranchList);
                                     break;
                                }
                            }
                           
                        }else{
                            isTransError = true;
                            mapData = new HashMap();
                            mapData = (HashMap)executeBranchList.get(i);
                            displayDetailsStr += "Transcheck Tasks not completed for Branch ID : "+CommonUtil.convertObjToStr(mapData.get(CommonConstants.BRANCH_ID)+"\n");
                            System.out.println("FFFFF"+displayDetailsStr);
                            executeBranchList.remove(i--);
                        }
                    }  
                }else{
//                    ClientUtil.showAlertWindow("No branch is ready for Completion/Rectify the errors to continue");
                    isError = true;
                    options = -2;
                }
                
                if(isTransError){
                    options =COptionPane.showOptionDialog(null,(displayDetailsStr+"\n"+" Select Yes to Continue with Completion for other Branches Or No to Cancel"), ("Complete Data Center DayEnd"),
                    COptionPane.YES_NO_CANCEL_OPTION,COptionPane.QUESTION_MESSAGE,null,obj,obj[0]);
                }
            if(options == -1 || options == 0){    
//                chkCABalChk.setSelected(true);
//                chkODBalChk.setSelected(true);
//    //             btnTransCheck.setEnabled(true);
//    //            chkCABalChk.setSelected(true);
//                btnProcessActionPerformed(null);
                observable.resetAll();
                    TaskHeader header = null ;
                    HashMap taskParamMap = new HashMap();
                    taskParamMap.put("DAY_END_TYPE", dayEndType);
                    if(executeBranchList!=null && executeBranchList.size()>0) {
                        taskParamMap.put(BRANCH_LST, executeBranchList);
                    }
                    taskParamMap.put("OA_BAL_CHK_TASK_LABLE", "Operative Account Balance Check");
                    taskParamMap.put("BAL_CHK_TASK_LABLE", "Transaction Balance Check");
                    header = getTaskFromMap();
                    header.setProcessType(CommonConstants.DAY_END);
                    header.setTaskParam(taskParamMap);
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkCABalChk.getText())));
                    observable.setEachTask(chkCABalChk, header);
                    header = getTaskFromMap();
                    header.setProductType("");
                    header.setTaskParam(taskParamMap);
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkODBalChk.getText())));
                    observable.setEachTask(chkODBalChk, header);

                    updateTable();
                    threadPool = new ThreadPool(observable);
                    threadPool.setTaskList(observable.getTaskList());
                    threadPool.initPooledThread();
                    threadPool.start();
                    taskRunning = true;
                    comp="comp";
                    taskParamMap = null;
                    if(executeBranchList!=null && executeBranchList.size()>0) {
                        branMap.put(BRANCH_LST, executeBranchList);
                    }
            }
                for (int i=0, j=tblLog.getRowCount(); i < j; i++) {
                    if (tblLog.getValueAt(i, 1).toString().equals("ERROR")) {
                        isError = true;
                        break;
                    }
                }
                if (!isError) {
                    
                    //            btnPrintActionPerformed(null);
                    
                    int yesno = COptionPane.showConfirmDialog(this, "Do you want to Complete Day End?", "Note", COptionPane.YES_NO_OPTION);
                    if (yesno==COptionPane.YES_OPTION) {
                        //                    HashMap eodMap = new HashMap();
                        //                    eodMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                        //                    ClientUtil.execute("updateDayEnd", eodMap);
                        HashMap eodMap = new HashMap();
                        for(int a=0; a<executeBranchList.size();a++){
                            eodMap = new HashMap();
                            eodMap = (HashMap)executeBranchList.get(a);
                            eodMap.put("BRANCH_ID", eodMap.get(CommonConstants.BRANCH_ID));
                            eodMap.put("DAYEND_DT", eodMap.get(CURR_APPL_DT));
                            ClientUtil.execute("updateDayEndStatusFinal", eodMap);
                        }
                        ClientUtil.execute("DeletePrevDayEndDate",null);
                        ClientUtil.execute("InsertPrevDayEndDate",eodMap);
                        eodMap = null;
                        this.dispose();
                        
                    }
                } else {
                    COptionPane.showMessageDialog(this, "You cannot Complete the Day End. Recify the Errors");
                }
            }else{
                ClientUtil.showMessageWindow("plz check TransCheck");
                btnProcess.setEnabled(false);
            }
    }//GEN-LAST:event_btnCompleteActionPerformed

    private void btnComplete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnComplete1ActionPerformed
        // TODO add your handling code here:
        boolean value=false;
        if(chk.equals("chk")) {
            value=false;
        } else {
            value=true;
        }
        chkBranchDayEnd.setSelected(value);
        chkReconcile.setSelected(false);
        chkInterBranchAuthRec.setSelected(value);
        btnProcess.setEnabled(value);
        chk="chk";
        if(daily.equals("daily")) {
            isCheck=false;
        } else {
            isCheck=true;
        }

    }//GEN-LAST:event_btnComplete1ActionPerformed

    private void btnDailyActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDailyActivityActionPerformed
        // TODO add your handling code here:
        if (dayEndType.equals("BRANCH_LEVEL")) {
            return;
        }
//        chkCABalChk.setVisible(true);
         boolean value=false;
        if(chk.equals("chk")){
//             value;
            //for prev days
             HashMap mapData = new HashMap();
             List lst=null;
            if((branMap != null && branMap.containsKey(BRANCH_LST)) && (dayEndPrev != null && dayEndPrev.length()>0)){
                  List brnLst = (List)branMap.get(BRANCH_LST);
                  if(brnLst!=null && brnLst.size()>0){
                      HashMap brnMap = (HashMap)brnLst.get(0);
//                      mapData.put(CURR_APPL_DT, brnMap.get("CUR_BRAN_DT"));
                         HashMap whereMap = new HashMap();
                       whereMap.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(brnMap.get(CommonConstants.BRANCH_ID)));
                       brnLst = (List) ClientUtil.executeQuery("getApplDateHashMap", whereMap);
                       whereMap = null;
                       if(brnLst!=null && brnLst.size() > 0){  
                            whereMap = (HashMap)brnLst.get(0);
                            mapData.put(CURR_APPL_DT, whereMap.get(CURR_APPL_DT));
//                            currDt = (Date) whereMap.get(CURR_APPL_DT);
                       }
                      lst = ((List)ClientUtil.executeQuery("getSelectBranchCompletedLstPrevDay", mapData));
                  }

            }else{
                mapData.put("DAYEND_DT", currDate.clone());
                mapData.put("BRANCH_DAY_END_STATUS", "COMPLETED");
                mapData.put("DC_DAY_END_STATUS", "COMPLETED");
                lst = ((List)ClientUtil.executeQuery("getSelectBranchCompletedLst", mapData));
            }
            mapData = null;
            if(lst != null && lst.size() > 0){
                String displayDetailsStr = "";
                String[] obj ={"Yes","No"};
                for(int i=0;i<lst.size();i++){
                    mapData = (HashMap)lst.get(i);
                    displayDetailsStr += " Branch ID : "+CommonUtil.convertObjToStr(mapData.get(CommonConstants.BRANCH_ID))+
                    " "+"Branch DayEnd Status : "+CommonUtil.convertObjToStr(mapData.get("BRANCH_DAY_END_STATUS"))+"\n";
                }
                    int options =COptionPane.showOptionDialog(null,(displayDetailsStr+"\n"+" Select Yes to Continue with DailyActivity for these Branches Or No to Cancel"), ("Daily Activity"),
                    COptionPane.YES_NO_CANCEL_OPTION,COptionPane.QUESTION_MESSAGE,null,obj,obj[0]);
//                    mapData = null;
//                    mapData.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(mapData.get("BRANCH_ID")));
//                    mapData.put("DAYEND_DT", currDate.clone());
//                    ClientUtil.execute("updateDailyDayendStatusFinal", mapData);
                    mapData = null;
                    if(options == 0){
                     doConti = true;
//                     observable.clearData();
                     }else{
                         doConti = false;
    //                     observable.clearData();
                     }
                     if(doConti){
                          daily="daily";
                btnComplete1ActionPerformed(null);
//                if(daily.equals("daily"))
//                    value=false;
//                else
                    value =doConti;

                chkInterestPayable.setSelected(value);
                chkDepositInt.setSelected(value);
                chkDepositAutoRenewal.setSelected(value);
                chkIntCalc.setSelected(value);
                chkNPA.setSelected(value);
                chkFlexi.setSelected(value);
                chkLapse_Transfer.setSelected(value);
                chkReverseFlexiPrematureClosing.setSelected(value);
                chkIntCalc.setSelected(value);
                chkNPA.setSelected(value);
                chkInterestReceivable.setSelected(value);
                chkAcCreditFromDebit.setSelected(value);
                chkFolio.setSelected(value);
    //            chkExcessWithdrawlChrg.setSelected(value);
                chkNonMiniBalanceChrg.setSelected(value);
    //            chkDividendCalculation.setSelected(value);
                btnProcess.setEnabled(true);
                btnComplete.setEnabled(true);
                btnComplete1.setEnabled(true);

                dailyBalance=true;
//                daily="daily";
                }else{
                    //do nothing
//                    daily="daily";
                    value = doConti;
                    btnProcess.setEnabled(false);
                    chkInterestPayable.setSelected(value);
                chkDepositInt.setSelected(value);
                chkDepositAutoRenewal.setSelected(value);
                chkIntCalc.setSelected(value);
                chkNPA.setSelected(value);
                chkFlexi.setSelected(value);
                chkLapse_Transfer.setSelected(value);
                chkReverseFlexiPrematureClosing.setSelected(value);
                chkIntCalc.setSelected(value);
                chkNPA.setSelected(value);
                chkInterestReceivable.setSelected(value);
                chkAcCreditFromDebit.setSelected(value);
                chkFolio.setSelected(value);
    //            chkExcessWithdrawlChrg.setSelected(value);
                chkNonMiniBalanceChrg.setSelected(value);
                    
                }
//                        }
                    branMap.put(BRANCH_LST, lst);
            }else{
                ClientUtil.showAlertWindow("No branch is ready for DailyActivity/Rectify the errors to continue");
                btnProcess.setEnabled(false);
            }
           
        }else{
            ClientUtil.showAlertWindow("Please check at least once Perform Check");
        }        
    }//GEN-LAST:event_btnDailyActivityActionPerformed

    private void btnTransCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransCheckActionPerformed
        // TODO add your handling code here:
        if(daily.equals("daily")){
            boolean value;
            if(comp.equals("comp")) {
                value=false;
            } else {
                value= true;
            }
//            btnDailyActivityActionPerformed(null);
            if (!taskRunning){
                int options = -1;
                HashMap mapData = new HashMap();
                HashMap errorMap = new HashMap();
                boolean isError = false;
                String displayDetailsStr = "";
                String[] obj ={"Yes","No"};
//                 HashMap mapData = new HashMap();
             List lst=null;
            if((branMap != null && branMap.containsKey(BRANCH_LST)) && (dayEndPrev != null && dayEndPrev.length()>0)){
                  List brnLst = (List)branMap.get(BRANCH_LST);
                  if(brnLst!=null && brnLst.size()>0){
                      HashMap brnMap = (HashMap)brnLst.get(0);
//                      mapData.put(CURR_APPL_DT, brnMap.get("CUR_BRAN_DT"));
                         HashMap whereMap = new HashMap();
                       whereMap.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(brnMap.get(CommonConstants.BRANCH_ID)));
                       brnLst = (List) ClientUtil.executeQuery("getApplDateHashMap", whereMap);
                       whereMap = null;
                       if(brnLst!=null && brnLst.size() > 0){  
                            whereMap = (HashMap)brnLst.get(0);
                            mapData.put(CURR_APPL_DT, whereMap.get(CURR_APPL_DT));
//                            currDt = (Date) whereMap.get(CURR_APPL_DT);
                       }
                      lst = ((List)ClientUtil.executeQuery("getSelectBranchCompletedLstPrevDay", mapData));
                  }

            }else{
                mapData.put("DAYEND_DT", currDate.clone());
                mapData.put("BRANCH_DAY_END_STATUS", "COMPLETED");
                mapData.put("DC_DAY_END_STATUS", "COMPLETED");
                lst = ((List)ClientUtil.executeQuery("getSelectBranchCompletedLst", mapData));
            }
//                mapData.put("DAYEND_DT", currDate.clone());
//                mapData.put("BRANCH_DAY_END_STATUS", "COMPLETED");
//                mapData.put("DC_DAY_END_STATUS", "COMPLETED");
//                List lst = ((List)ClientUtil.executeQuery("getSelectBranCompletedLst", mapData));
//                List lst1=(List)lst.clone();
                ArrayList executeBranchList = new ArrayList();
                executeBranchList.addAll(lst);
//                executeBranchList=(ArrayList)lst;
                mapData = null;
                if(executeBranchList != null && executeBranchList.size() > 0){
                    for(int i=0;i<executeBranchList.size();i++){
                        
                        mapData=new HashMap();
                        mapData = (HashMap)executeBranchList.get(i);
//                        if(!CommonUtil.convertObjToStr(dayEndPrev).equalsIgnoreCase(""))
                            mapData.put("DAYEND_DT", mapData.get(CURR_APPL_DT));
//                        else
//                            mapData.put("DAYEND_DT", currDate.clone());
                        StringBuffer presentTasks = new StringBuffer();
                        presentTasks.append("'" + chkInterestPayable.getText() + "'" +
                        "," + "'" + chkDepositInt.getText() + "'" +
                        "," + "'" + chkDepositAutoRenewal.getText() + "'" +
                        "," + "'" + chkFlexi.getText() + "'" +
                        "," + "'" + chkReverseFlexiPrematureClosing.getText() + "'" +
                        "," + "'" + chkIntCalc.getText() + "'" +
                        "," + "'" + chkNPA.getText() + "'" +
                        "," + "'" + chkFolio.getText() + "'" +
                        "," + "'" + chkNonMiniBalanceChrg.getText() + "'" +
                        "," + "'" + chkLapse_Transfer.getText() + "'");
                        mapData.put("TASK_NAME", presentTasks);
                        List lstData = ((List)ClientUtil.executeQuery("getSelectTransChkReady", mapData));
                        mapData = null;
                        if(lstData != null && lstData.size() > 0){
                            for(int j=0;j<lstData.size();j++){
                                mapData = (HashMap)lstData.get(j);
                                if(CommonUtil.convertObjToStr(mapData.get("TASK_STATUS")).equals("ERROR")){
                                    isError = true;
                                    displayDetailsStr += "\n"+"Error Branch ID : "+CommonUtil.convertObjToStr(mapData.get("BRANCH_ID")+"\n");
                                     System.out.println("FFFFF"+displayDetailsStr);
                                     executeBranchList.remove(i--);
                                     System.out.println("FFFFF"+executeBranchList);
                                     break;
                                }
                            }
                           
                        }else{
                            isError = true;
                                    displayDetailsStr += "Run atleast one DailyActivity Task  Branch ID : "+CommonUtil.convertObjToStr(mapData.get("BRANCH_ID")+"\n");
                                     System.out.println("FFFFF"+displayDetailsStr);
                                     executeBranchList.remove(i--);
                                     System.out.println("FFFFF"+executeBranchList);
                        }
                    }  
                }else{
//                    ClientUtil.showAlertWindow("No branch is ready for TransCheck/Rectify the errors to continue");
//                    isError = true;
                    options = -2;
                    
                }
                
                if(isError){
                    options =COptionPane.showOptionDialog(null,(displayDetailsStr+"\n"+" Select Yes to Continue with Transcheck for other Branches Or No to Cancel"), ("Trans Check"),
                    COptionPane.YES_NO_CANCEL_OPTION,COptionPane.QUESTION_MESSAGE,null,obj,obj[0]);
                }
                if(options == -1 || options == 0){
                    observable.resetAll();
                    TaskHeader header = null ;
                    HashMap taskParamMap = new HashMap();
                    taskParamMap.put("DAY_END_TYPE", dayEndType);
                    if(executeBranchList!=null && executeBranchList.size()>0) {
                        taskParamMap.put(BRANCH_LST, executeBranchList);
                    }
                    taskParamMap.put("GL_ABS_TASK_LABLE", "GL Abstract");
                    taskParamMap.put("DAILY_BAL_UPDATE_TASK_LABLE", "Update Daily Balance");
                    taskParamMap.put("TOD_UPDATION", "TOD_UPDATION");
                    header = getTaskFromMap();
                    header.setProcessType(CommonConstants.DAY_END);
                    header.setTaskParam(taskParamMap);
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkGL_Abstract.getText())));
                    observable.setEachTask(chkGL_Abstract, header);
                    header = getTaskFromMap();
                    header.setProductType("");
                    header.setTaskParam(taskParamMap);
                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkDailyBalance.getText())));
                    observable.setEachTask(chkDailyBalance, header);

                    updateTable();
                    threadPool = new ThreadPool(observable);
                    threadPool.setTaskList(observable.getTaskList());
                    threadPool.initPooledThread();
                    threadPool.start();
                    taskRunning = true;
                    comp="comp";
                    taskParamMap = null;
                    if(executeBranchList!=null && executeBranchList.size()>0) {
                        branMap.put(BRANCH_LST, executeBranchList);
                    }
                }else{
                    ClientUtil.showAlertWindow("No branch is ready for TransCheck/Rectify the errors to continue");
                }
            }
        } else{
            ClientUtil.showAlertWindow("Please check at least once DailyActivity");
            btnProcess.setEnabled(false);
        }
        //        comp="comp";
        
        btnProcess.setEnabled(true);
    }//GEN-LAST:event_btnTransCheckActionPerformed

    private void chkReverseFlexiPrematureClosingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkReverseFlexiPrematureClosingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkReverseFlexiPrematureClosingActionPerformed
                        private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
                            // TODO add your handling code here:
                            if(isCheck){
                            int rowscnt = tblData.getRowCount();
                            ArrayList branAryLst = new ArrayList();
//                            List branLst = null;
                            HashMap newBranMap = new HashMap();
                            if(rowscnt>0){
                            for (int i=0; i<rowscnt; i++) {
                                String isSelected = CommonUtil.convertObjToStr(tblData.getValueAt(i,0));
                                if(isSelected.equalsIgnoreCase("TRUE")){
                                        newBranMap = new HashMap();
//                                    branAryLst.add(i, CommonUtil.convertObjToStr(tblData.getValueAt(i,2)));
                                    newBranMap.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(tblData.getValueAt(i,2)));
//                                    Date curBranDt = (Date)tblData.getValueAt(i,1);
                                    newBranMap.put("CUR_BRAN_DT", CommonUtil.convertObjToStr(tblData.getValueAt(i,1)));
//                                    newBranMap.put("CUR_BRAN_DT", curBranDt);
                                    branAryLst.add(i, newBranMap);
                                    branMap.put(BRANCH_LST, branAryLst);
                                    dayEndPrev = "DAY_END_PREV";
                                    isCheck = false;
                                }
//                                branMap.put(BRANCH_LST, branAryLst);
//                                dayEndPrev = "DAY_END_PREV";
                            }
                            }
                            }
                            btnProcessActionPerformed();
    }//GEN-LAST:event_btnProcessActionPerformed
    
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
    
    private void btnProcessActionPerformed() {
//        chkDepositInt.setSelected(false);
//        chkInterestPayable.setSelected(false);
        HashMap map1 = new HashMap();
        map1.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        map1.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        map1.put("USER_ID", ProxyParameters.USER_ID);
        HashMap taskParamMap = new HashMap();
        taskParamMap.put("DAY_END_TYPE", dayEndType);
        if(dayEndPrev != null && dayEndPrev.length()>0) {
            taskParamMap.put("DAY_END_PREV", dayEndPrev);
        }
        if(branMap != null && branMap.containsKey(BRANCH_LST)) {
            taskParamMap.put(BRANCH_LST, branMap.get(BRANCH_LST));
        }

        TaskHeader header = null ;
        if (!taskRunning){
            observable.resetAll();
            if(chkBranchDayEnd.isSelected()){
                header = getTaskFromMap();
                taskParamMap.put("TASK_LABLE", chkBranchDayEnd.getText());
                header.setTaskParam(taskParamMap);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkBranchDayEnd.getText())));
                observable.setEachTask(chkBranchDayEnd, header);
            }            
            if (chkReconcile.isSelected()){
                header = getTaskFromMap();
                taskParamMap.put("TASK_LABLE", chkReconcile.getText());
                header.setTaskParam(taskParamMap);  // To execute for all branches.
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkReconcile.getText())));
                observable.setEachTask(chkReconcile, header);
            }
            if (chkInterBranchAuthRec.isSelected()){
                header = getTaskFromMap();
                header.setTaskParam(taskParamMap);  // To execute for all branches.
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkInterBranchAuthRec.getText())));
                observable.setEachTask(chkInterBranchAuthRec, header);
                
            }
            if (chkGL_Abstract.isSelected()){
                header = getTaskFromMap();
                 taskParamMap.put("GL_ABS_TASK_LABLE", chkGL_Abstract.getText());
                header.setProcessType(CommonConstants.DAY_END);
                header.setTaskParam(taskParamMap);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkGL_Abstract.getText())));
                observable.setEachTask(chkGL_Abstract, header);
            }
            if (chkCABalChk.isSelected()){
                header = getTaskFromMap();
                 taskParamMap.put("OA_BAL_CHK_TASK_LABLE", chkCABalChk.getText());
                header.setTaskParam(taskParamMap);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkCABalChk.getText())));
                observable.setEachTask(chkCABalChk, header);
            }
            if (chkODBalChk.isSelected()){
                header = getTaskFromMap();
                 taskParamMap.put("BAL_CHK_TASK_LABLE", chkODBalChk.getText());
                header.setTaskParam(taskParamMap);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkODBalChk.getText())));
                header.setProcessType(CommonConstants.DAY_END);
                observable.setEachTask(chkODBalChk, header);
            }
            if(chkIntCalc.isSelected()){
                header=getTaskFromMap();
                taskParamMap.put("INT_CAL_TASK_LABLE", chkIntCalc.getText());
                header.setTaskParam(taskParamMap);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkIntCalc.getText())));
                observable.setEachTask(chkIntCalc,header);
            }
            if (chkExcessWithdrawlChrg.isSelected()){
                header = getTaskFromMap();
                taskParamMap.put("TASK_LABLE", chkExcessWithdrawlChrg.getText());
                header.setProductType("OA");
                header.setTransactionType("EXCESSTRANSCHARGE");
                header.setTaskParam(taskParamMap);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkExcessWithdrawlChrg.getText())));
                observable.setEachTask(chkExcessWithdrawlChrg, header);
            }
            if (chkNonMiniBalanceChrg.isSelected()){
//                observable.setTabTask(chkNonMiniBalanceChrg);
                header = getTaskFromMap();
                taskParamMap.put("MIN_BAL_TASK_LABLE", chkNonMiniBalanceChrg.getText());
                header.setTaskParam(taskParamMap);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkNonMiniBalanceChrg.getText())));
                header.setTransactionType("MIN_BALANCE");
                observable.setEachTask(chkNonMiniBalanceChrg, header);
            }
            if (chkFlexi.isSelected()){
                header = getTaskFromMap();
                taskParamMap.put("FLEXI_TASK_LABLE", chkFlexi.getText());
                header.setTaskParam(taskParamMap);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkFlexi.getText())));
                observable.setEachTask(chkFlexi, header);
            }
            if(chkLapse_Transfer.isSelected()){
                 header = getTaskFromMap();
                 taskParamMap.put("REMIT_LAPSE_TASK_LABLE", chkLapse_Transfer.getText());
                 header.setTaskParam(taskParamMap);
                 header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkLapse_Transfer.getText())));
                 observable.setEachTask(chkLapse_Transfer, header);
            }
            if(chkReverseFlexiPrematureClosing.isSelected()){
                header = getTaskFromMap();
                taskParamMap.put("REV_FLEXI_TASK_LABLE", chkReverseFlexiPrematureClosing.getText());
                header.setTaskParam(taskParamMap);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkReverseFlexiPrematureClosing.getText())));
                observable.setEachTask(chkReverseFlexiPrematureClosing,header);
            }
            if (chkDepositAutoRenewal.isSelected()){
                header = getTaskFromMap();
                taskParamMap.put("DEP_AUTO_REN_TASK_LABLE", chkDepositAutoRenewal.getText());
                header.setTaskParam(taskParamMap);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkDepositAutoRenewal.getText())));
                observable.setEachTask(chkDepositAutoRenewal, header);
            }
            if (chkDepositInt.isSelected()){
                header = getTaskFromMap();
                
                HashMap map = new HashMap();
                map.put("DEP_INT_TASK_LABLE", chkDepositInt.getText());
//                map.put("PROCESS","CUMTOTAL");  // This is for making provisions for newly migrated a/cs.
                map.put("PROCESS", CommonConstants.DAY_END);
                map.put("DAY_END_TYPE", dayEndType);
                header.setTaskParam(map);
                header.setProcessType(CommonConstants.DAY_END);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkDepositInt.getText())));
                observable.setEachTask(chkDepositInt, header);
            }
            if (chkInterestPayable.isSelected()){
                header = getTaskFromMap();
                taskParamMap.put("INT_TASK_LABLE", chkInterestPayable.getText());
                header.setProductType("OA");
                header.setTransactionType(CommonConstants.PAYABLE);
                header.setTaskParam(taskParamMap);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkInterestPayable.getText())));
                observable.setEachTask(chkInterestPayable, header);
            }
            if (chkInterestReceivable.isSelected()){
                header = getTaskFromMap();
                taskParamMap.put("DEBIT_INT_TASK_LABLE", chkInterestReceivable.getText());
                header.setProductType("AD");
                header.setTransactionType(CommonConstants.RECEIVABLE);
                header.setTaskParam(taskParamMap);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkInterestReceivable.getText())));
                observable.setEachTask(chkInterestReceivable, header);
            }
            if (chkDividendCalculation.isSelected()){
                header = getTaskFromMap();
                taskParamMap.put("TASK_LABLE", chkDividendCalculation.getText());
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkDividendCalculation.getText())));
                observable.setEachTask(chkDividendCalculation, header);
            }
            if (chkNPA.isSelected()){
                header = getTaskFromMap();
                taskParamMap.put("NPA_TASK_LABLE", chkNPA.getText());
                header.setTaskParam(taskParamMap);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkNPA.getText())));
                observable.setEachTask(chkNPA, header);
            }
            if (chkFolio.isSelected()){
                header = getTaskFromMap();
                taskParamMap.put("FOLIO_TASK_LABLE", chkFolio.getText());
                header.setTaskParam(taskParamMap);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkFolio.getText())));
                observable.setEachTask(chkFolio, header);
            }
            updateTable();
            threadPool = new ThreadPool(observable);
            threadPool.setTaskList(observable.getTaskList());
            threadPool.initPooledThread();
            threadPool.start();
            taskRunning = true;
            ClientUtil.enableDisable(panBatchProcess , false, false, true);
        }else{
            final String warnMessage = "Process is running";
            displayAlert(warnMessage);
        }
//        TaskHeader header = null ;
//        if (!taskRunning){
//            observable.resetAll();            
//            if(chkBranchDayEnd.isSelected()){
//                header = getTaskFromMap();
//                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkBranchDayEnd.getText())));
//                observable.setEachTask(chkBranchDayEnd, header);
//            }            
//            if (chkReconcile.isSelected()){
//                header = getTaskFromMap();
//                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkReconcile.getText())));
//                observable.setEachTask(chkReconcile, header);
//            }
//            updateTable();
//            threadPool = new ThreadPool(observable);
//            threadPool.setTaskList(observable.getTaskList());
//            threadPool.initPooledThread();
//            threadPool.start();
//            taskRunning = true;
//            ClientUtil.enableDisable(panBatchProcess , false);
//        }else{
//            final String warnMessage = "Process is running";
//            ClientUtil.displayAlert(warnMessage);
//        }
    }
    
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    private TaskHeader getTaskFromMap() { //String value, HashMap map, String transType, String prodType, String process) {
        TaskHeader tskHeader = new TaskHeader();
        try{
            tskHeader.setProcessType(CommonConstants.DAY_BEGIN);
            tskHeader.setBankID(TrueTransactMain.BANK_ID);
            tskHeader.setBranchID(ProxyParameters.BRANCH_ID);
            tskHeader.setUserID(ProxyParameters.USER_ID);
            tskHeader.setIpAddr(java.net.InetAddress.getLocalHost().getHostAddress());
        }catch(Exception E){
            System.out.println("Error in Setting the Task Header...");
            E.printStackTrace();
        }
        return tskHeader;
    }    
    
    private void updateTable(){
        tblLog.setModel(observable.getDayBeginTableModel());
    }
    public static void main(String str[]) {
        javax.swing.JFrame frm = new javax.swing.JFrame();
        DCDayEndProcessUI db = new DCDayEndProcessUI();
        frm.getContentPane().add(db);
        frm.show();
        db.show();
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        observable.resetAll();
        if (threadPool!=null) {
            threadPool.stopAllThreads();
        }
        updateTable();
        this.dispose();
//        observable = null;
//        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    public void update(java.util.Observable o, Object arg) {
        this.tskStatus = observable.getTaskStatus();
        this.tskHeader = observable.getTaskHeader();
        updateChkBoxTableBasedOnStatus();
    }
    private void updateChkBoxTableBasedOnStatus(){
        boolean enable = false;
        if (tskHeader.getTaskClass().equals("AllBranchDayEndTask")){
            enable = observable.updateTaskStatusInLogTable(chkBranchDayEnd);
        }
        else if (tskHeader.getTaskClass().equals("AllBranchReconcileTask")){
            enable = observable.updateTaskStatusInLogTable(chkReconcile);
        } 
        else if (tskHeader.getTaskClass().equals("GlAbstractUpdateTask")){
            enable = observable.updateTaskStatusInLogTable(chkGL_Abstract);
        }
        else if (tskHeader.getTaskClass().equals("SelectCABalance")){
            enable = observable.updateTaskStatusInLogTable(chkCABalChk);
        }
        else if (tskHeader.getTaskClass().equals("ExcessTransChrgesTask")){
            enable = observable.updateTaskStatusInLogTable(chkExcessWithdrawlChrg);
        }
        else if (tskHeader.getTaskClass().equals("OABalanceCheckTask")){
            enable = observable.updateTaskStatusInLogTable(chkCABalChk);
        }
        else if (tskHeader.getTaskClass().equals("BalanceCheckTask")){
            enable = observable.updateTaskStatusInLogTable(chkODBalChk);
        }
        else if (tskHeader.getTaskClass().equals("FlexiTask")){
            enable = observable.updateTaskStatusInLogTable(chkFlexi);
        }
         else if (tskHeader.getTaskClass().equals("RemittanceLapseTransferTask")){
            enable = observable.updateTaskStatusInLogTable(chkLapse_Transfer);
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
        else if (tskHeader.getTaskClass().equals("DividendCalcTask")){
            enable = observable.updateTaskStatusInLogTable(chkDividendCalculation);
        }
        else if (tskHeader.getTaskClass().equals("NPATask")){
            enable = observable.updateTaskStatusInLogTable(chkNPA);
        }
        else if (tskHeader.getTaskClass().equals("InterBranchAuthorizationCheck")){
            enable = observable.updateTaskStatusInLogTable(chkInterBranchAuthRec);
        }
        else if (tskHeader.getTaskClass().equals("InterestCalculationTask")){
            enable = observable.updateTaskStatusInLogTable(chkIntCalc);
        }
        else if (tskHeader.getTaskClass().equals("FolioChargesTask")){
            enable = observable.updateTaskStatusInLogTable(chkFolio);
        }
        else if (tskHeader.getTaskClass().equals("MinBalanceChargesTask")){
            enable = observable.updateTaskStatusInLogTable(chkNonMiniBalanceChrg);
        }
        
        updateTable();
        if (enable){
            taskRunning = false;
            ClientUtil.enableDisable(panBatchProcess , true, true, true);
            threadPool.stopAllThreads();
        }
//        boolean enable = false;
//        if (tskHeader.getTaskClass().equals("AllBranchDayEndTask")){
//            enable = observable.updateTaskStatusInLogTable(chkBranchDayEnd);
//        }else if (tskHeader.getTaskClass().equals("AllBranchReconcileTask")){
//            enable = observable.updateTaskStatusInLogTable(chkReconcile);
//        }
//        updateTable();
//        if (enable){
//            taskRunning = false;
//            ClientUtil.enableDisable(panBatchProcess , true);
//            com.see.truetransact.ui.TrueTransactMain.lblDate.setText(DateUtil.getStringDate(currDate.clone()));
//            threadPool.stopAllThreads();
//        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnComplete;
    private com.see.truetransact.uicomponent.CButton btnComplete1;
    private com.see.truetransact.uicomponent.CButton btnDailyActivity;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnPrinted;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CButton btnTransCheck;
    private com.see.truetransact.uicomponent.CCheckBox chkAcCreditFromDebit;
    private com.see.truetransact.uicomponent.CCheckBox chkBranchDayEnd;
    private com.see.truetransact.uicomponent.CCheckBox chkCABalChk;
    private com.see.truetransact.uicomponent.CCheckBox chkDailyBalance;
    private com.see.truetransact.uicomponent.CCheckBox chkDepositAutoRenewal;
    private com.see.truetransact.uicomponent.CCheckBox chkDepositInt;
    private com.see.truetransact.uicomponent.CCheckBox chkDividendCalculation;
    private com.see.truetransact.uicomponent.CCheckBox chkExcessWithdrawlChrg;
    private com.see.truetransact.uicomponent.CCheckBox chkFlexi;
    private com.see.truetransact.uicomponent.CCheckBox chkFolio;
    private com.see.truetransact.uicomponent.CCheckBox chkGL_Abstract;
    private com.see.truetransact.uicomponent.CCheckBox chkIntCalc;
    private com.see.truetransact.uicomponent.CCheckBox chkInterBranchAuthRec;
    private com.see.truetransact.uicomponent.CCheckBox chkInterestPayable;
    private com.see.truetransact.uicomponent.CCheckBox chkInterestReceivable;
    private com.see.truetransact.uicomponent.CCheckBox chkLapse_Transfer;
    private com.see.truetransact.uicomponent.CCheckBox chkNPA;
    private com.see.truetransact.uicomponent.CCheckBox chkNonMiniBalanceChrg;
    private com.see.truetransact.uicomponent.CCheckBox chkODBalChk;
    private com.see.truetransact.uicomponent.CCheckBox chkReconcile;
    private com.see.truetransact.uicomponent.CCheckBox chkReverseFlexiPrematureClosing;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll1;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace24;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CPanel panBatchProcess;
    private com.see.truetransact.uicomponent.CPanel panBatchProcess1;
    private com.see.truetransact.uicomponent.CPanel panCheckBoxes;
    private com.see.truetransact.uicomponent.CPanel panCheckBoxes1;
    private com.see.truetransact.uicomponent.CPanel panComplete;
    private com.see.truetransact.uicomponent.CPanel panMultiSearch;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CScrollPane scrOutputMsg;
    private com.see.truetransact.uicomponent.CScrollPane scrOutputMsg1;
    private com.see.truetransact.uicomponent.CSeparator sptSpace1;
    private com.see.truetransact.uicomponent.CSeparator sptSpace2;
    private com.see.truetransact.uicomponent.CTabbedPane tabDCDayBegin1;
    private com.see.truetransact.uicomponent.CTable tblData;
    private com.see.truetransact.uicomponent.CTable tblLog;
    private javax.swing.JToolBar tbrHead;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    // End of variables declaration//GEN-END:variables
}
