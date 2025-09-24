/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DayBeginProcessUI.java
 *
 * Created on August 17, 2004, 4:49 PM
 */

package com.see.truetransact.ui.batchprocess;

import java.util.HashMap;

import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants ;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.sysadmin.group.GroupOB;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.transferobject.sysadmin.config.ConfigPasswordTO;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.ui.batchprocess.authorizechk.ViewHTMLDataUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import javax.swing.JDialog;
/**
 *
 * @author bala
 * @modified shanmuga
 * @modified Sunil : Added Matured Deposit Task 05-May-2005
 */
public class DayBeginProcessUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer{
    private TaskStatus tskStatus;
    private TaskHeader tskHeader;
    private ThreadPool threadPool;
    private ProcessOB observable = null;
    private boolean taskRunning;
    private java.util.Date currDate;
    String chk = "";
    String daily = "";
    String comp = "";
    String dayEndType = "";
    private Date currDt = null;
	JDialog loading = null;
    /** Creates new form BatchProcessUI */
    public DayBeginProcessUI() {
        initComponents();
        setObservable();
        setupInit();
        currDt = ClientUtil.getCurrentDate();
        //TODO
        chkChecksumPrinting.setVisible(false);
        chkGenMaturedIntSlip.setVisible(false);
        chkLocker.setVisible(false);
        chkReversalDebit.setVisible(false);
        chkUpdateSacLimit.setVisible(false);
        chkDepositInterestApplication.setVisible(true);
//        chkTLStart.setVisible(false);
        chkMonInt.setVisible(false);
        chkTempTables.setVisible(false);
        
        chkGL_Abstract.setSelected(false);
        chkInitSerialNo.setSelected(false);
        chkMonInt.setSelected(false);
        chkMinor.setSelected(false);
//        chkTLStart.setSelected(true);//false
        chkGenMaturedIntSlip.setSelected(false);
        chkTransOptInOpt.setSelected(false);
        chkChecksumPrinting.setSelected(false);
        chkReversalDebit.setSelected(false);
        chkDepositInterestApplication.setSelected(false);
        chkPrevDayEnd.setSelected(false);
        chkExecuteLockerRentSi.setSelected(false);
        chkAgeUpdation.setSelected(false);
        chkStartDay.setSelected(false);
        chkCurrDay.setSelected(false);
        chkTempTables.setSelected(false);
        chkExecSI.setSelected(false);
        chkTransAcct.setSelected(false);
        chkAcctCrToDr.setSelected(false);
        chkLocker.setSelected(false);
        chkUpdateSacLimit.setSelected(false);
        chkTod.setSelected(false);
        btnProcess.setEnabled(false);
        chkDepositAutoRenewal.setEnabled(false);

        //Hidden All checkboxes
//        chkPrevDayEnd.setVisible(false);
//        chkStartDay.setVisible(false);
//        chkGL_Abstract.setVisible(false);
//        chkExecSI.setVisible(false);
//        chkMinor.setVisible(false);
//        chkTransOptInOpt.setVisible(false);
//        chkCurrDay.setVisible(false);
//        chkInitSerialNo.setVisible(false);
//        chkTempTables.setVisible(false);
//        chkMonInt.setVisible(false);
//        chkTLStart.setVisible(false);
//        chkGenMaturedIntSlip.setVisible(false);
//        chkTransAcct.setVisible(false);
//        chkAcctCrToDr.setVisible(false);
        HashMap test = new HashMap();
        List lst = ((List)ClientUtil.executeQuery("getSelectConfigPasswordTO", test));
        dayEndType =  lst!=null ? CommonUtil.convertObjToStr(((ConfigPasswordTO)lst.get(0)).getDayEndType()) : "";    
        if (dayEndType.equals("")) {
            ClientUtil.showAlertWindow("<html>Cause  : Unable to load Day End.<br>" +
            "Reason : DayEnd type not mentioned in Bank Configuration.</html>");
            panBatchProcess.setVisible(false);
            btnProcess.setVisible(false);
        } else if (dayEndType.equals("BRANCH_LEVEL")) {
            // Nothing to do.
        } else {
            panBatchProcess.setVisible(false);
            btnProcess.setVisible(false);
        }
        test = null;
        lst = null;        
        
        currDate = ClientUtil.getCurrentDateProperFormat();
    }
    
    private void setupInit() {
        observable.resetAll();
        HashMap taskMap = new HashMap();
        taskMap.put(chkPrevDayEnd.getText(), "PreviousDayEndTask");
        taskMap.put(chkExecuteLockerRentSi.getText(), "ExecuteLockerRentSiCheckTask");
        taskMap.put(chkAgeUpdation.getText(),"AgeUpdationTask");
        taskMap.put(chkStartDay.getText(), "StartDayBeginTask");
        taskMap.put(chkGL_Abstract.getText(), "GlAbstractUpdateTask");
        taskMap.put(chkExecSI.getText(), "StandingInstructionTask");
        taskMap.put(chkMinor.getText(), "MinorToMajorTask");
        taskMap.put(chkTransOptInOpt.getText(), "DormantToInOperativeTask");
        taskMap.put(chkChecksumPrinting.getText(), "");
        taskMap.put(chkDepositInterestApplication.getText(),"DepositIntTask");
        taskMap.put(chkCurrDay.getText(), "");
        taskMap.put(chkInitSerialNo.getText(), "InitSerialNumTask");
        taskMap.put(chkTempTables.getText(), "");
        taskMap.put(chkMonInt.getText(), "");
        taskMap.put(chkUpdateSacLimit.getText(), "");
        taskMap.put(chkTLStart.getText(), "LoanDemandCreationTask");
        taskMap.put(chkLocker.getText(), "");
        taskMap.put(chkGenMaturedIntSlip.getText(), "MaturedDepositTask");
        taskMap.put(chkTransAcct.getText(), "");
        taskMap.put(chkAcctCrToDr.getText(), "");
        taskMap.put(chkReversalDebit.getText(), "");
        taskMap.put(chkTDReverseFlexi.getText(), "ReverseFlexiTaskonMaturity");
        taskMap.put(chkTod.getText(), "TodTask");
        taskMap.put(chkDepositAutoRenewal.getText(), "DepositAutoRenewalTask"); //Deposit Auto Renewal
        taskMap.put("DifferentDateInterBranchCheck", "DifferentDateInterBranchCheck");
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
        chkPrevDayEnd = new com.see.truetransact.uicomponent.CCheckBox();
        chkStartDay = new com.see.truetransact.uicomponent.CCheckBox();
        chkChecksumPrinting = new com.see.truetransact.uicomponent.CCheckBox();
        chkCurrDay = new com.see.truetransact.uicomponent.CCheckBox();
        chkInitSerialNo = new com.see.truetransact.uicomponent.CCheckBox();
        chkTempTables = new com.see.truetransact.uicomponent.CCheckBox();
        chkMonInt = new com.see.truetransact.uicomponent.CCheckBox();
        chkTDReverseFlexi = new com.see.truetransact.uicomponent.CCheckBox();
        chkExecSI = new com.see.truetransact.uicomponent.CCheckBox();
        chkMinor = new com.see.truetransact.uicomponent.CCheckBox();
        chkUpdateSacLimit = new com.see.truetransact.uicomponent.CCheckBox();
        chkTLStart = new com.see.truetransact.uicomponent.CCheckBox();
        chkLocker = new com.see.truetransact.uicomponent.CCheckBox();
        chkGenMaturedIntSlip = new com.see.truetransact.uicomponent.CCheckBox();
        chkTransAcct = new com.see.truetransact.uicomponent.CCheckBox();
        chkTransOptInOpt = new com.see.truetransact.uicomponent.CCheckBox();
        chkAcctCrToDr = new com.see.truetransact.uicomponent.CCheckBox();
        chkReversalDebit = new com.see.truetransact.uicomponent.CCheckBox();
        sptSpace1 = new com.see.truetransact.uicomponent.CSeparator();
        scrOutputMsg = new com.see.truetransact.uicomponent.CScrollPane();
        tblLog = new com.see.truetransact.uicomponent.CTable();
        chkGL_Abstract = new com.see.truetransact.uicomponent.CCheckBox();
        chkDepositInterestApplication = new com.see.truetransact.uicomponent.CCheckBox();
        panComplete = new com.see.truetransact.uicomponent.CPanel();
        btnComplete1 = new com.see.truetransact.uicomponent.CButton();
        btnDailyActivity = new com.see.truetransact.uicomponent.CButton();
        btnTransCheck = new com.see.truetransact.uicomponent.CButton();
        btnComplete = new com.see.truetransact.uicomponent.CButton();
        chkTod = new com.see.truetransact.uicomponent.CCheckBox();
        chkExecuteLockerRentSi = new com.see.truetransact.uicomponent.CCheckBox();
        chkDepositAutoRenewal = new com.see.truetransact.uicomponent.CCheckBox();
        chkAgeUpdation = new com.see.truetransact.uicomponent.CCheckBox();
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
        setTitle("Day Begin Process");
        getContentPane().setLayout(new java.awt.BorderLayout(4, 4));

        panBatchProcess.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panBatchProcess.setLayout(new java.awt.GridBagLayout());

        chkPrevDayEnd.setSelected(true);
        chkPrevDayEnd.setText("Check for Previous Day's Day-End");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBatchProcess.add(chkPrevDayEnd, gridBagConstraints);

        chkStartDay.setSelected(true);
        chkStartDay.setText("Check for Current Day's Day-Begin ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBatchProcess.add(chkStartDay, gridBagConstraints);

        chkChecksumPrinting.setText("Checksum Printing ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBatchProcess.add(chkChecksumPrinting, gridBagConstraints);

        chkCurrDay.setSelected(true);
        chkCurrDay.setText("Fetch Current Day's Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBatchProcess.add(chkCurrDay, gridBagConstraints);

        chkInitSerialNo.setSelected(true);
        chkInitSerialNo.setText("Initialize Various Serial Number ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBatchProcess.add(chkInitSerialNo, gridBagConstraints);

        chkTempTables.setSelected(true);
        chkTempTables.setText("Initialize of Temp Tables");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBatchProcess.add(chkTempTables, gridBagConstraints);

        chkMonInt.setSelected(true);
        chkMonInt.setText("Calculating Monthly Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBatchProcess.add(chkMonInt, gridBagConstraints);

        chkTDReverseFlexi.setText("Reverse Flexi on Maturity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBatchProcess.add(chkTDReverseFlexi, gridBagConstraints);

        chkExecSI.setSelected(true);
        chkExecSI.setText("Executing Standing Instructions");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBatchProcess.add(chkExecSI, gridBagConstraints);

        chkMinor.setSelected(true);
        chkMinor.setText("Minor Turning Major Activity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBatchProcess.add(chkMinor, gridBagConstraints);

        chkUpdateSacLimit.setText("Updating Sanction Limits");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBatchProcess.add(chkUpdateSacLimit, gridBagConstraints);

        chkTLStart.setText("Term Loan Start Day Processing ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBatchProcess.add(chkTLStart, gridBagConstraints);

        chkLocker.setText("Lockers Processing");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBatchProcess.add(chkLocker, gridBagConstraints);

        chkGenMaturedIntSlip.setText("Generate Interest Slip for Matured Account ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBatchProcess.add(chkGenMaturedIntSlip, gridBagConstraints);

        chkTransAcct.setText("Transfer of Account Operative - Matured");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBatchProcess.add(chkTransAcct, gridBagConstraints);

        chkTransOptInOpt.setText("Transfer of Operative Account to Inoperative");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBatchProcess.add(chkTransOptInOpt, gridBagConstraints);

        chkAcctCrToDr.setSelected(true);
        chkAcctCrToDr.setText("Check for Accounts Coming to Credit from Debit ");
        chkAcctCrToDr.setMaximumSize(new java.awt.Dimension(325, 27));
        chkAcctCrToDr.setMinimumSize(new java.awt.Dimension(325, 27));
        chkAcctCrToDr.setPreferredSize(new java.awt.Dimension(325, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBatchProcess.add(chkAcctCrToDr, gridBagConstraints);

        chkReversalDebit.setText("Reversal of Debit in the SB CA OD CC to Regular Accounts");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBatchProcess.add(chkReversalDebit, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBatchProcess.add(sptSpace1, gridBagConstraints);

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
        gridBagConstraints.gridy = 17;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panBatchProcess.add(scrOutputMsg, gridBagConstraints);

        chkGL_Abstract.setSelected(true);
        chkGL_Abstract.setText("Create GL Abstract");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBatchProcess.add(chkGL_Abstract, gridBagConstraints);

        chkDepositInterestApplication.setText("Deposit Interest Application");
        chkDepositInterestApplication.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkDepositInterestApplicationActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBatchProcess.add(chkDepositInterestApplication, gridBagConstraints);

        panComplete.setMinimumSize(new java.awt.Dimension(170, 34));
        panComplete.setPreferredSize(new java.awt.Dimension(170, 34));
        panComplete.setLayout(new java.awt.GridBagLayout());

        btnComplete1.setText("Perform Check");
        btnComplete1.setMinimumSize(new java.awt.Dimension(125, 27));
        btnComplete1.setPreferredSize(new java.awt.Dimension(125, 27));
        btnComplete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnComplete1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 8);
        panComplete.add(btnComplete1, gridBagConstraints);

        btnDailyActivity.setText("Daily Activity");
        btnDailyActivity.setMaximumSize(new java.awt.Dimension(125, 27));
        btnDailyActivity.setMinimumSize(new java.awt.Dimension(125, 27));
        btnDailyActivity.setPreferredSize(new java.awt.Dimension(125, 27));
        btnDailyActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDailyActivityActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 8);
        panComplete.add(btnDailyActivity, gridBagConstraints);

        btnTransCheck.setText("TransCheck");
        btnTransCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransCheckActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 8);
        panComplete.add(btnTransCheck, gridBagConstraints);

        btnComplete.setText("Complete");
        btnComplete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCompleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 8);
        panComplete.add(btnComplete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        panBatchProcess.add(panComplete, gridBagConstraints);

        chkTod.setSelected(true);
        chkTod.setText("Execute TOD");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBatchProcess.add(chkTod, gridBagConstraints);

        chkExecuteLockerRentSi.setText("Execute Locker Rent SI");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBatchProcess.add(chkExecuteLockerRentSi, gridBagConstraints);

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
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBatchProcess.add(chkDepositAutoRenewal, gridBagConstraints);

        chkAgeUpdation.setText("Age Updation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panBatchProcess.add(chkAgeUpdation, gridBagConstraints);

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

    private void btnCompleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCompleteActionPerformed
        // TODO add your handling code here:
        boolean isError = false;
//        if(comp.equals("comp")||chk.equals("chk") ||daily.equals("daily")   ){
            if(comp.equals("comp") && daily.equals("daily")){
            btnComplete1ActionPerformed(null);
            btnDailyActivityActionPerformed(null);
            btnTransCheckActionPerformed(null);
            for (int i=0, j=tblLog.getRowCount(); i < j; i++) {
                if (tblLog.getValueAt(i, 1).toString().equals("ERROR")) {
                    isError = true;
                    break;
                }
            }
//            chkStartDay.setSelected(true);
//            btnProcessActionPerformed();
            if (!isError) {
    //            btnPrintActionPerformed(null);
                int yesno = COptionPane.showConfirmDialog(this, "Do you want to Complete Day Begin?", "Note", COptionPane.YES_NO_OPTION);
                if (yesno==COptionPane.YES_OPTION) {
                    lblStatus.setText("Completed");
                    com.see.truetransact.ui.TrueTransactMain.lblDate.setText("<html> Date : <font color=blue>" + DateUtil.getStringDate(ClientUtil.getCurrentDate()) + "</font></html>");
                    if (dayEndType.equals("BRANCH_LEVEL")) {
                        updateTrueTransactMainTree();
                    }
                    HashMap eodMap = new HashMap();
                    eodMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
                    eodMap.put("DC_DAY_BEGIN_STATUS", "COMPLETED");
                    eodMap.put("DAYBEGIN_DT", ClientUtil.getCurrentDate());
                    List lst = ClientUtil.executeQuery("getDCLevelDayBeginStatus", eodMap);
                    if(lst != null && lst.size()>0){
                        //do nothing
                    }else{
                        ClientUtil.execute("InsertDayBeginStatusFinal", eodMap);
                    }
//                    if (threadPool!=null)
//                        threadPool.stopAllThreads();
                    this.dispose();
                }
            } else {
                COptionPane.showMessageDialog(this, "You cannot Complete the Day Begin. Recify the Errors");
                btnProcess.setEnabled(false);
            }             
        }else{
            ClientUtil.showMessageWindow("plz check TransCheck");
        }
        expiryOtherUser();
    }//GEN-LAST:event_btnCompleteActionPerformed

    private void btnTransCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransCheckActionPerformed
        // TODO add your handling code here:
         if(daily.equals("daily")){
             boolean value;
             if(comp.equals("comp")) {
                 value=false;
             } else {
                 value= true;
             }
             chkMonInt.setSelected(false);
             chkMonInt.setVisible(false);
             chkTLStart.setSelected(false);
             //        chkTLStart.setVisible(false);
             chkGenMaturedIntSlip.setSelected(false);
             chkTransOptInOpt.setSelected(false);
             chkChecksumPrinting.setSelected(false);
             chkReversalDebit.setSelected(false);
             chkDepositInterestApplication.setSelected(false);
             chkTempTables.setSelected(false);
             chkTempTables.setVisible(false);
             chkExecSI.setSelected(true);
             chkTransAcct.setSelected(false);
             chkAcctCrToDr.setSelected(false);
             chkLocker.setSelected(false);
             chkUpdateSacLimit.setSelected(false);
             comp="comp";
             chkGL_Abstract.setSelected(false);
             chkInitSerialNo.setSelected(false);
             chkMinor.setSelected(false);
             chkPrevDayEnd.setSelected(false);
             chkStartDay.setSelected(false);
             chkCurrDay.setSelected(false);
             chkGL_Abstract.setEnabled(false);
             chkInitSerialNo.setEnabled(false);
             chkMinor.setEnabled(false);
             chkExecuteLockerRentSi.setEnabled(false);
             chkAgeUpdation.setEnabled(false);
             chkStartDay.setEnabled(false);
             chkCurrDay.setEnabled(false);
             chkTDReverseFlexi.setSelected(false);
             btnDailyActivity.setEnabled(false);
             btnComplete1.setEnabled(false);
             btnComplete.setEnabled(false);
             chkTod.setSelected(false);
             chkDepositAutoRenewal.setSelected(false);
         } else{
             ClientUtil.showAlertWindow("Check atleast one Daily Activity");
         }
         expiryOtherUser();
    }//GEN-LAST:event_btnTransCheckActionPerformed

    private void btnDailyActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDailyActivityActionPerformed
        // TODO add your handling code here:
         if(chk.equals("chk")){
             boolean value;
             if(daily.equals("daily")) {
                 value=false;
             } else {
                 value =true;
             }
             if(taskRunning) {
                 return;
             }
             chkGL_Abstract.setSelected(true);
             //KD-3758
             boolean excludeAutoRenewal = false;
             HashMap cbmsMap = new HashMap();
             List list;
             cbmsMap.put("CBMS_KEY", "EXCLUDE_AUTO_RENEWAL_DAYBEGIN");
             list = ClientUtil.executeQuery("getSelectCbmsParameterValues", cbmsMap);
             if (list != null && list.size() > 0) {
                 cbmsMap = (HashMap) list.get(0);
                 String val = CommonUtil.convertObjToStr(cbmsMap.get("CBMS_VALUE"));
                 if (val != null && val.length() > 0 && val.equals("Y")) {
                     excludeAutoRenewal = true;
                 }
             }

             //KD-3758
             if (excludeAutoRenewal) {
                 System.out.println("EXCLUDE_AUTO_RENEWAL_DAYBEGIN :: " + excludeAutoRenewal);
                 chkDepositAutoRenewal.setEnabled(false);
                 chkDepositAutoRenewal.setSelected(false);
             } else {
                 List renewList = ClientUtil.executeQuery("getAutoRenewableProductCount", null);
                 if (renewList != null && renewList.size() > 0) {
                     HashMap renewCntMap = (HashMap) renewList.get(0);
                     if (renewCntMap.containsKey("RENEWAL_CNT") && renewCntMap.get("RENEWAL_CNT") != null && CommonUtil.convertObjToInt(renewCntMap.get("RENEWAL_CNT")) > 0) {
                         chkDepositAutoRenewal.setSelected(true);
                     } else {
                         chkDepositAutoRenewal.setSelected(false);
                     }
                 } else {
                     chkDepositAutoRenewal.setSelected(false);
                 }
                 chkDepositAutoRenewal.setEnabled(true);
             }
             chkInitSerialNo.setSelected(true);
             chkMinor.setSelected(true);
             chkAgeUpdation.setEnabled(true);
             btnComplete1.setEnabled(false);
             btnProcess.setEnabled(true);
             btnTransCheck.setEnabled(false);
             btnComplete.setEnabled(false);
             //        chkTDReverseFlexi.setSelected(true);
             chkTLStart.setSelected(false);
             
             daily="daily";
             
             chkPrevDayEnd.setEnabled(false);
             chkExecuteLockerRentSi.setSelected(false);
             chkAgeUpdation.setSelected(false);
             chkStartDay.setSelected(false);
             chkCurrDay.setSelected(false);
             chkStartDay.setEnabled(false);
             chkCurrDay.setEnabled(false);
             chkMonInt.setSelected(false);
             //        chkTLStart.setSelected(false);
             chkGenMaturedIntSlip.setSelected(false);
             chkTransOptInOpt.setSelected(false);
             chkChecksumPrinting.setSelected(false);
             chkReversalDebit.setSelected(false);
             chkDepositInterestApplication.setSelected(false);
             chkTempTables.setSelected(false);
             chkExecSI.setSelected(false);
             chkTransAcct.setSelected(false);
             chkAcctCrToDr.setSelected(false);
             chkLocker.setSelected(false);
             chkUpdateSacLimit.setSelected(false);
             chkTod.setSelected(false);
             
             chkMonInt.setEnabled(false);
             //        chkTLStart.setEnabled(false);
             chkGenMaturedIntSlip.setEnabled(false);
             chkTransOptInOpt.setEnabled(false);
             chkChecksumPrinting.setEnabled(false);
             chkReversalDebit.setEnabled(false);
             chkDepositInterestApplication.setEnabled(false);
             chkTempTables.setEnabled(false);
             chkExecSI.setEnabled(false);
             chkTransAcct.setEnabled(false);
             chkAcctCrToDr.setEnabled(false);
             chkLocker.setEnabled(false);
             chkUpdateSacLimit.setSelected(false);
             chkTod.setEnabled(false);
            // chkDepositAutoRenewal.setSelected(true);
         } else{
             ClientUtil.showAlertWindow("Check atleast one Perform Check");
//          ClientUtil.showAlertWindow("Check atleast one Perform Check");    
         }
         expiryOtherUser();
    }//GEN-LAST:event_btnDailyActivityActionPerformed

    private void expiryOtherUser(){
        HashMap bodMap = new HashMap();
        bodMap.put("USER_ID", ProxyParameters.USER_ID);
        List logoutLst = ClientUtil.executeQuery("getLogoutUserList", bodMap);
        if (logoutLst != null && logoutLst.size() > 0) {
            bodMap.put("LOGOUT_LIST", logoutLst);
        }
        ClientUtil.executeQuery("daybeginOterUserLogout", bodMap);//for calling to businessdeligatebean todo further activity
    }

    private void btnComplete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnComplete1ActionPerformed
        // TODO add your handling code here:
        HashMap eodMap = new HashMap();
        eodMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
        eodMap.put("DAYBEGIN_DT", ClientUtil.getCurrentDate());
        List lst = ClientUtil.executeQuery("getBranchLevelDayBeginStatus", eodMap);
        if(lst != null && lst.size()>0){
            ClientUtil.showMessageWindow("Day Begin already Completed");
            btnProcess.setEnabled(false);
            btnDailyActivity.setEnabled(false);
            btnTransCheck.setEnabled(false);
            btnComplete.setEnabled(false);
        }else{
            boolean value = false;
            if(chk.equals("chk")) {
                value=false;
            } else {
                value=true;
            }
            chkPrevDayEnd.setSelected(value);
            chkStartDay.setSelected(value);
            chkCurrDay.setSelected(value);
            //chkExecuteLockerRentSi.setSelected(value);
            chk = "chk";
            chkGL_Abstract.setSelected(false);
            chkInitSerialNo.setSelected(false);
            chkMinor.setSelected(false);
            chkGL_Abstract.setEnabled(false);
            chkInitSerialNo.setEnabled(false);
            chkMinor.setEnabled(false);
            chkAgeUpdation.setEnabled(false);
            chkMonInt.setSelected(false);
            //        chkTLStart.setSelected(true);//false
            chkTLStart.setEnabled(false);
            chkTDReverseFlexi.setEnabled(false);
            chkGenMaturedIntSlip.setSelected(false);
            chkTransOptInOpt.setSelected(false);
            chkChecksumPrinting.setSelected(false);
            chkReversalDebit.setSelected(false);
            chkDepositInterestApplication.setSelected(false);
            chkTempTables.setSelected(false);
            chkExecSI.setSelected(false);
            chkTransAcct.setSelected(false);
            chkAcctCrToDr.setSelected(false);
            chkLocker.setSelected(false);
            chkUpdateSacLimit.setSelected(false);
            chkTod.setSelected(false);
            
            chkMonInt.setEnabled(false);
            //        chkTLStart.setEnabled(true);//false
            chkGenMaturedIntSlip.setEnabled(false);
            chkTransOptInOpt.setEnabled(false);
            chkChecksumPrinting.setEnabled(false);
            chkReversalDebit.setEnabled(false);
            chkDepositInterestApplication.setEnabled(false);
            chkTempTables.setEnabled(false);
            chkExecSI.setEnabled(false);
            chkTransAcct.setEnabled(false);
            chkAcctCrToDr.setEnabled(false);
            chkLocker.setEnabled(false);
            chkUpdateSacLimit.setSelected(false);
            btnDailyActivity.setEnabled(false);
            btnComplete1.setEnabled(false);
            btnTransCheck.setEnabled(false);
            btnComplete.setEnabled(false);
            chkTod.setEnabled(false);
            btnProcess.setEnabled(true);
            chkDepositAutoRenewal.setEnabled(false);
        }
    }//GEN-LAST:event_btnComplete1ActionPerformed
                        private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
                            // TODO add your handling code here:
//                            chkDepositInterestApplication.setSelected(false);
                            btnProcessActionPerformed();
                            btnDailyActivity.setEnabled(true);
                            btnComplete1.setEnabled(true);
                            btnTransCheck.setEnabled(true);
                            btnComplete.setEnabled(true);
    }//GEN-LAST:event_btnProcessActionPerformed
    private void btnProcessActionPerformed() {
        
        TaskHeader header = null ;
        if (!taskRunning){
            observable.resetAll();
            if (chkPrevDayEnd.isSelected()){
                header = getTaskFromMap();
                HashMap paramMap = new HashMap();
                paramMap.put("CHK_PREV_DAYEND_TASK_LABLE", "Check for Previous Day's Day-End");
                header.setTaskParam(paramMap);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkPrevDayEnd.getText())));
                observable.setEachTask(chkPrevDayEnd, header);
            }
             if (chkExecuteLockerRentSi.isSelected()){
                header = getTaskFromMap();
                HashMap paramMap = new HashMap();
                paramMap.put("CHK_LCK_SI_TASK_LABLE", "check for Execute Locker Rent SI");
                header.setTaskParam(paramMap);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkExecuteLockerRentSi.getText())));
                observable.setEachTask(chkExecuteLockerRentSi, header);
            }
            // Age Updation code changes
             if (chkAgeUpdation.isSelected()){
                header = getTaskFromMap();
                HashMap paramMap = new HashMap();
                paramMap.put("CHK_AGE_UPDATION", "check for Age Updation");
                header.setTaskParam(paramMap);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkAgeUpdation.getText())));
                observable.setEachTask(chkAgeUpdation, header);
            }
            // End
             
            if (chkStartDay.isSelected()){
                header = getTaskFromMap();
                HashMap paramMap = new HashMap();
                paramMap.put("CURR_DATE", currDate);
                paramMap.put("CHK_CUR_DAYBEGIN_TASK_LABLE", "Check for Current Day's Day-Begin");
                header.setTaskParam(paramMap);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkStartDay.getText())));
                observable.setEachTask(chkStartDay, header);
            }
            if (chkChecksumPrinting.isSelected()){
                observable.setTabTask(chkChecksumPrinting);
            }
            if(chkDepositInterestApplication.isSelected()){
                header = getTaskFromMap();
                HashMap map = new HashMap();
                map.put("DAY_END_TYPE", dayEndType);
                map.put("DEP_INT_APPL_TASK_LABLE", "Deposit Interest Application");
                map.put("PROCESS","DAY_BEGIN");
                header.setTaskParam(map);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkDepositInterestApplication.getText())));
                observable.setEachTask(chkDepositInterestApplication, header);
//                observable.setTabTask(chkDepositInterestApplication);
            }
            if (chkInitSerialNo.isSelected()){
//                observable.setTabTask(chkInitSerialNo);
                      header = getTaskFromMap();
                HashMap paramMap = new HashMap();
//                observable.setTabTask(chkTLStart);
                 paramMap.put("CHK_INIT_SERIAL_NUM", "Initialize Various Serial Number ");
                 header.setTaskParam(paramMap);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkInitSerialNo.getText())));
                observable.setEachTask(chkInitSerialNo, header);
                
            }
            if (chkTempTables.isSelected()){
                observable.setTabTask(chkTempTables);
            }
            if (chkMonInt.isSelected()){
                observable.setTabTask(chkMonInt);
            }
            // Standing Instruction Task
            if(chkExecSI.isSelected()){
                header = getTaskFromMap();
                HashMap paramMap = new HashMap();
                paramMap.put("CHK_EXEC_STD_TASK_LABLE", "Executing Standing Instructions");
                header.setTaskParam(paramMap);
                header.setProcessType(CommonConstants.DAY_BEGIN);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkExecSI.getText())));
                observable.setEachTask(chkExecSI, header);
            }
            // Minor To Major Task
            if (chkMinor.isSelected()){
                header = getTaskFromMap();
                HashMap paramMap = new HashMap();
//                observable.setTabTask(chkTLStart);
                 paramMap.put("CHK_MINOR_TASK_LABLE", "Minor Turning Major Activity");
                 header.setTaskParam(paramMap);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkMinor.getText())));
                observable.setEachTask(chkMinor, header);
            }
            if (chkUpdateSacLimit.isSelected()){
                observable.setTabTask(chkUpdateSacLimit);
            }
            //loans demand task
            if (chkTLStart.isSelected()){
                header = getTaskFromMap();
                HashMap paramMap = new HashMap();
//                observable.setTabTask(chkTLStart);
                 paramMap.put("CHK_TL_START_TASK_LABLE", "Term Loan Day-Begin Processing ");
                 header.setTaskParam(paramMap);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkTLStart.getText())));
                observable.setEachTask(chkTLStart, header);
            }
            if (chkTDReverseFlexi.isSelected()){
                header = getTaskFromMap();
//                observable.setTabTask(chkTLStart);
                 HashMap paramMap = new HashMap();
                paramMap.put("CHK_TD_REV_FLX_TASK_LABLE", "Reverse Flexi on Maturity");
                header.setTaskParam(paramMap);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkTDReverseFlexi.getText())));
                observable.setEachTask(chkTDReverseFlexi, header);
            }
             if (chkTod.isSelected()){
                header = getTaskFromMap();
//                observable.setTabTask(chkTLStart);
//                taskParamMap.put("EXEC_TOD_TASK_LABLE", "Execute TOD");
                HashMap paramMap = new HashMap();
                paramMap.put("EXEC_TOD_TASK_LABLE", "Reverse Flexi on Maturity");
                header.setTaskParam(paramMap);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkTod.getText())));
                observable.setEachTask(chkTod, header);
            }
            if (chkLocker.isSelected()){
                observable.setTabTask(chkLocker);
            }
            //Deposit Account Matured 
            if (chkGenMaturedIntSlip.isSelected()){
                header = getTaskFromMap();
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkGenMaturedIntSlip.getText())));
                observable.setEachTask(chkGenMaturedIntSlip, header);
//                observable.setTabTask(chkGenMaturedIntSlip);
            }
            //Deposit Operative to Matured
            if (chkTransAcct.isSelected()){
                header = getTaskFromMap();
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkTransAcct.getText())));
                observable.setEachTask(chkTransAcct, header);
            }
            // Operative To InOperative Task
            if(chkTransOptInOpt.isSelected()){
                header = getTaskFromMap();
                HashMap paramMap = new HashMap();
                paramMap.put("CHK_TRN_OPT_TASK_LABLE", "Transfer of Operative Account to Inoperative");
                header.setTaskParam(paramMap);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkTransOptInOpt.getText())));
                observable.setEachTask(chkTransOptInOpt, header);
            }
            if (chkDepositAutoRenewal.isSelected()){
                header = getTaskFromMap();
                HashMap map = new HashMap();
                map.put("DEP_AUTO_REN_TASK_LABLE", chkDepositAutoRenewal.getText());
                header.setTaskParam(map);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkDepositAutoRenewal.getText())));
                observable.setEachTask(chkDepositAutoRenewal, header);
            }
            if (chkAcctCrToDr.isSelected()){
                observable.setTabTask(chkAcctCrToDr);
            }
            if (chkReversalDebit.isSelected()){
                observable.setTabTask(chkReversalDebit);
            }
            if (chkCurrDay.isSelected()){
//                boolean isCompleted = false;
//                for (int i=0, j=tblLog.getRowCount(); i < j; i++) {
//                    if (tblLog.getValueAt(i, 0).toString().equals("COMPLETED") && 
//                            tblLog.getValueAt(i, 1).toString().equals(chkStartDay.getText())) {
//                        isCompleted = true;
//                        break;
//                    }
//                }
                String status = "COMPLETED";
//                if (isCompleted) {
//                    status = "COMPLETED";
//                }
                observable.setTabTask(chkCurrDay, status);
            }
            if (chkGL_Abstract.isSelected()){
                header = getTaskFromMap();
                header.setProcessType(CommonConstants.DAY_BEGIN);
                  HashMap paramMap = new HashMap();
//                observable.setTabTask(chkTLStart);
                 paramMap.put("CREATE_GL_ABS_TASK_LABLE", "Create GL Abstract");
                 header.setTaskParam(paramMap);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkGL_Abstract.getText())));
                observable.setEachTask(chkGL_Abstract, header);
            }

            if (chkGL_Abstract.isSelected()) {
                header = getTaskFromMap();
                header.setProcessType(CommonConstants.DAY_BEGIN);
                HashMap paramMap = new HashMap();
                paramMap.put("DifferentDateInterBranchCheck", "DifferentDateInterBranchCheck");
                header.setTaskParam(paramMap);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get("DifferentDateInterBranchCheck")));
                observable.setEachTask("DifferentDateInterBranchCheck", header);
            }

            
            updateTable();
            threadPool = new ThreadPool(observable);
            threadPool.setTaskList(observable.getTaskList());
            threadPool.initPooledThread();
            threadPool.start();
            taskRunning = true;
            ClientUtil.enableDisable(panBatchProcess , false);
			CommonUtil comm = new CommonUtil();
            loading = comm.addProgressBar();
            loading.show();
        }else{
            final String warnMessage = "Process is running";
            ClientUtil.displayAlert(warnMessage);
        }
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
		
		boolean taskCompleted = true;
        if (loading != null) {
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
    public static void main(String str[]) {
        javax.swing.JFrame frm = new javax.swing.JFrame();
        DayBeginProcessUI db = new DayBeginProcessUI();
        frm.getContentPane().add(db);
        frm.show();
        db.show();
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        if (threadPool!=null) {
            threadPool.stopAllThreads();
        }
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void chkDepositAutoRenewalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkDepositAutoRenewalActionPerformed
        // TODO add your handling code here:
        boolean excludeAutoRenewal = false;
        HashMap cbmsMap = new HashMap();
        List list;
        cbmsMap.put("CBMS_KEY", "EXCLUDE_AUTO_RENEWAL_DAYBEGIN");
        list = ClientUtil.executeQuery("getSelectCbmsParameterValues", cbmsMap);
        if (list != null && list.size() > 0) {
            cbmsMap = (HashMap) list.get(0);
            String val = CommonUtil.convertObjToStr(cbmsMap.get("CBMS_VALUE"));
            if (val != null && val.length() > 0 && val.equals("Y")) {
                excludeAutoRenewal = true;
            }
        }
        if (excludeAutoRenewal) {
            System.out.println("EXCLUDE_AUTO_RENEWAL_DAYBEGIN :: " + excludeAutoRenewal);
            chkDepositAutoRenewal.setEnabled(false);
            chkDepositAutoRenewal.setSelected(false);
        }
    }//GEN-LAST:event_chkDepositAutoRenewalActionPerformed

    private void chkDepositInterestApplicationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkDepositInterestApplicationActionPerformed
        // TODO add your handling code here:
        chkDepositInterestApplication.setSelected(false);
    }//GEN-LAST:event_chkDepositInterestApplicationActionPerformed
    public void update(java.util.Observable o, Object arg) {
        this.tskStatus = observable.getTaskStatus();
        this.tskHeader = observable.getTaskHeader();
        updateChkBoxTableBasedOnStatus();
    }
    private void updateChkBoxTableBasedOnStatus(){
        boolean enable = false;
        if (tskHeader.getTaskClass().equals("GlAbstractUpdateTask")){
            enable = observable.updateTaskStatusInLogTable(chkGL_Abstract);
        }else if (tskHeader.getTaskClass().equals("MinorToMajorTask")){
            enable = observable.updateTaskStatusInLogTable(chkMinor);
        }else if (tskHeader.getTaskClass().equals("InitSerialNumTask")){
            enable = observable.updateTaskStatusInLogTable(chkInitSerialNo);
        }else if (tskHeader.getTaskClass().equals("StandingInstructionTask")){
            enable = observable.updateTaskStatusInLogTable(chkExecSI);
        }else if (tskHeader.getTaskClass().equals("DormantToInOperativeTask")){
            enable = observable.updateTaskStatusInLogTable(chkTransOptInOpt);
        }else if (tskHeader.getTaskClass().equals("PreviousDayEndTask")){
            enable = observable.updateTaskStatusInLogTable(chkPrevDayEnd);
        }else if (tskHeader.getTaskClass().equals("StartDayBeginTask")){
            enable = observable.updateTaskStatusInLogTable(chkStartDay);
        }else if (tskHeader.getTaskClass().equals("MaturedDepositTask")){
            enable = observable.updateTaskStatusInLogTable(chkGenMaturedIntSlip);
        }else if (tskHeader.getTaskClass().equals("DepositIntTask")){
            enable = observable.updateTaskStatusInLogTable(chkDepositInterestApplication);
        }
        else if (tskHeader.getTaskClass().equals("LoanDemandCreationTask")){
            enable = observable.updateTaskStatusInLogTable(chkTLStart);
        }else if(tskHeader.getTaskClass().equals("ReverseFlexiTaskonMaturity")){
            enable = observable.updateTaskStatusInLogTable(chkTDReverseFlexi);
        }
        else if(tskHeader.getTaskClass().equals("TodTask")){
            enable = observable.updateTaskStatusInLogTable(chkTod);
        }else if (tskHeader.getTaskClass().equals("ExecuteLockerRentSiCheckTask")){
            enable = observable.updateTaskStatusInLogTable(chkExecuteLockerRentSi);
        }else if (tskHeader.getTaskClass().equals("DepositAutoRenewalTask")){
            enable = observable.updateTaskStatusInLogTable(chkDepositAutoRenewal);
        }else if (tskHeader.getTaskClass().equals("DifferentDateInterBranchCheck")) {
            enable = observable.updateTaskStatusInLogTable("DifferentDateInterBranchCheck");
        }else if (tskHeader.getTaskClass().equals("AgeUpdationTask")){
            enable = observable.updateTaskStatusInLogTable(chkAgeUpdation);
        }
        updateTable();
        if (enable){
            taskRunning = false;
            ClientUtil.enableDisable(panBatchProcess , true);
            boolean isError = false;
            for (int i=0, j=tblLog.getRowCount(); i < j; i++) {
                if (tblLog.getValueAt(i, 1).toString().equals("ERROR")) {
                    isError = true;
                    break;
                }
            }

//            if (!isError) { commented by abi messagebox need end of complete
//    //            btnPrintActionPerformed(null);
//                int yesno = COptionPane.showConfirmDialog(this, "Do you want to Complete Day End?", "Note", COptionPane.YES_NO_OPTION);
//                if (yesno==COptionPane.YES_OPTION) {
//                    lblStatus.setText("Completed");
//                    com.see.truetransact.ui.TrueTransactMain.lblDate.setText("<html> Date : <font color=blue>" + DateUtil.getStringDate(ClientUtil.getCurrentDate()) + "</font></html>");
//                    updateTrueTransactMainTree();
//                } 
//                if (threadPool!=null)
//                    threadPool.stopAllThreads();
//            } else {
////                COptionPane.showMessageDialog(this, "You cannot Complete the Day End. Recify the Errors");
//            }            
        }
    }
    
    private void updateTrueTransactMainTree() {
        javax.swing.JTree treModules = TrueTransactMain.getTree();
        HashMap map = new HashMap();
        map.put(CommonConstants.MAP_WHERE, TrueTransactMain.GROUP_ID);
        GroupOB treeOB = new GroupOB(false);

        HashMap whereMap = new HashMap();
        whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        String strStatus = CommonUtil.convertObjToStr(ClientUtil.executeQuery("chkTransactionAllowed", whereMap).get(0));

        if (strStatus.equalsIgnoreCase("COMPLETED")) {
            whereMap = new HashMap();
            whereMap.put("screenId","SCR01041");
            whereMap.put("screenClass",null);
            whereMap.put("screenName","Day Begin");
            whereMap.put("screenSlNo","1");
            whereMap.put("moduleId","14");
            whereMap.put("moduleSlNo","16");
            whereMap.put("moduleName","Periodic Activity");
            whereMap.put("newAllowed","Y");
            whereMap.put("editAllowed","Y");
            whereMap.put("deleteAllowed","Y");
            whereMap.put("authRejAllowed","Y");
            whereMap.put("exceptionAllowed","Y");
            whereMap.put("printAllowed","Y");
            whereMap.put("interbranchAllowed", null);
            ArrayList grantScreen = new ArrayList();
            grantScreen.add(whereMap);
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
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnComplete;
    private com.see.truetransact.uicomponent.CButton btnComplete1;
    private com.see.truetransact.uicomponent.CButton btnDailyActivity;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CButton btnTransCheck;
    private com.see.truetransact.uicomponent.CCheckBox chkAcctCrToDr;
    private com.see.truetransact.uicomponent.CCheckBox chkAgeUpdation;
    private com.see.truetransact.uicomponent.CCheckBox chkChecksumPrinting;
    private com.see.truetransact.uicomponent.CCheckBox chkCurrDay;
    private com.see.truetransact.uicomponent.CCheckBox chkDepositAutoRenewal;
    private com.see.truetransact.uicomponent.CCheckBox chkDepositInterestApplication;
    private com.see.truetransact.uicomponent.CCheckBox chkExecSI;
    private com.see.truetransact.uicomponent.CCheckBox chkExecuteLockerRentSi;
    private com.see.truetransact.uicomponent.CCheckBox chkGL_Abstract;
    private com.see.truetransact.uicomponent.CCheckBox chkGenMaturedIntSlip;
    private com.see.truetransact.uicomponent.CCheckBox chkInitSerialNo;
    private com.see.truetransact.uicomponent.CCheckBox chkLocker;
    private com.see.truetransact.uicomponent.CCheckBox chkMinor;
    private com.see.truetransact.uicomponent.CCheckBox chkMonInt;
    private com.see.truetransact.uicomponent.CCheckBox chkPrevDayEnd;
    private com.see.truetransact.uicomponent.CCheckBox chkReversalDebit;
    private com.see.truetransact.uicomponent.CCheckBox chkStartDay;
    private com.see.truetransact.uicomponent.CCheckBox chkTDReverseFlexi;
    private com.see.truetransact.uicomponent.CCheckBox chkTLStart;
    private com.see.truetransact.uicomponent.CCheckBox chkTempTables;
    private com.see.truetransact.uicomponent.CCheckBox chkTod;
    private com.see.truetransact.uicomponent.CCheckBox chkTransAcct;
    private com.see.truetransact.uicomponent.CCheckBox chkTransOptInOpt;
    private com.see.truetransact.uicomponent.CCheckBox chkUpdateSacLimit;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace24;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CPanel panBatchProcess;
    private com.see.truetransact.uicomponent.CPanel panComplete;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CScrollPane scrOutputMsg;
    private com.see.truetransact.uicomponent.CSeparator sptSpace1;
    private com.see.truetransact.uicomponent.CTable tblLog;
    private javax.swing.JToolBar tbrHead;
    // End of variables declaration//GEN-END:variables
    
}
