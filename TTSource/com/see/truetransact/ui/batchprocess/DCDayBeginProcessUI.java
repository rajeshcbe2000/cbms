/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DCDayBeginProcessUI.java
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

/**
 *
 * @author bala
 * @modified shanmuga
 * @modified Sunil : Added Matured Deposit Task 05-May-2005
 */
public class DCDayBeginProcessUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer{
    private TaskStatus tskStatus;
    private TaskHeader tskHeader;
    private ThreadPool threadPool;
    private ProcessOB observable = null;
    private boolean taskRunning;
    private java.util.Date currDate;
    private boolean doConti = true;
    private boolean isCheck = false;
    String chk = "";
    String daily = "";
    String comp = "";
    String dayEndType = "";
    String dayBeginPrev = "";
    boolean interBranchOnHoliday = false;
    private HashMap branMap = new HashMap();
    private final String BRANCH_LST = "BRANCH_LST";
    private Date currDt = null;
    /** Creates new form BatchProcessUI */
    public DCDayBeginProcessUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        setObservable();
        setupInit();
        
        selectAllCheckBoxes(panBatchProcess, false);
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
        
        HashMap test = new HashMap();
        List lst = ((List)ClientUtil.executeQuery("getSelectConfigPasswordTO", test));
        if (lst!=null) {
            ConfigPasswordTO objRules = (ConfigPasswordTO)lst.get(0);
            dayEndType = CommonUtil.convertObjToStr(objRules.getDayEndType());
            interBranchOnHoliday = CommonUtil.convertObjToStr(objRules.getInterBranchOnHoliday()).equals("Y") ? true : false;
        }
        if (dayEndType.equals("")) {
            ClientUtil.showAlertWindow("<html>Cause  : Unable to load Day End.<br>" +
            "Reason : DayEnd type not mentioned in Bank Configuration.</html>");
            panBatchProcess.setVisible(false);
            btnProcess.setVisible(false);
        } else if (dayEndType.equals("BRANCH_LEVEL")) {
//            panBatchProcess.setVisible(false);
//            panBatchProcess1.setVisible(false);
            tabDCDayBegin.setVisible(false);
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
        taskMap.put(chkStartDay.getText(), "StartDayBeginTask");
        taskMap.put(chkGL_Abstract.getText(), "GlAbstractUpdateTask");
        taskMap.put(chkExecSI.getText(), "StandingInstructionTask");
        taskMap.put(chkTod.getText(), "TodTask");
        taskMap.put(chkMinor.getText(), "MinorToMajorTask");
        taskMap.put(chkTransOptInOpt.getText(), "DormantToInOperativeTask");
        taskMap.put(chkDepositInterestApplication.getText(),"DepositIntTask");
        taskMap.put(chkTLStart.getText(), "LoanDemandCreationTask");
        taskMap.put(chkGenMaturedIntSlip.getText(), "MaturedDepositTask");
        taskMap.put(chkTDReverseFlexi.getText(), "ReverseFlexiTaskonMaturity");
        taskMap.put(chkChecksumPrinting.getText(), "");
        taskMap.put(chkCurrDay.getText(), "");
        taskMap.put(chkInitSerialNo.getText(), "InitSerialNumTask");
        taskMap.put(chkTempTables.getText(), "");
        taskMap.put(chkMonInt.getText(), "");
        taskMap.put(chkUpdateSacLimit.getText(), "");
        taskMap.put(chkLocker.getText(), "");
        taskMap.put(chkTransAcct.getText(), "");
        taskMap.put(chkAcctCrToDr.getText(), "");
        taskMap.put(chkReversalDebit.getText(), "");
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
    
    private void setVisibleAllCheckBoxes(java.awt.Container comp, boolean yesno) {
        java.awt.Component[] children = comp.getComponents();
        
        for (int i = 0; i < children.length; i++) {
            if ((children[i] != null)) {
                if (children[i] instanceof javax.swing.JCheckBox){
                    ((javax.swing.JCheckBox)children[i]).setVisible(yesno);
                }
            }
        }
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
        tabDCDayBegin = new com.see.truetransact.uicomponent.CTabbedPane();
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
        setTitle("Day Begin Process");
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

        tabDCDayBegin.setMinimumSize(new java.awt.Dimension(694, 524));
        tabDCDayBegin.setPreferredSize(new java.awt.Dimension(694, 524));

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
        gridBagConstraints.gridx = 2;
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

        chkTLStart.setSelected(true);
        chkTLStart.setText("Term Loan Day-Begin Processing ");
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

        btnDailyActivity.setText("Daily Actiity");
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

        tabDCDayBegin.addTab("Data Center Day Begin", panBatchProcess);

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
        scrOutputMsg1.setViewportView(tblData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panBatchProcess1.add(scrOutputMsg1, gridBagConstraints);

        tabDCDayBegin.addTab("Check for DayBegin", panBatchProcess1);

        getContentPane().add(tabDCDayBegin, java.awt.BorderLayout.EAST);

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
        viewMap.put(CommonConstants.MAP_NAME, "getDayBeginCompList");
        whereMap.put("CURR_APPL_DT", tdtFromDate.getDateValue());
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
    private void tblLogMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLogMousePressed
        // TODO add your handling code here:
         int selectedRow = tblLog.getSelectedRow();
        EnhancedTableModel model = (EnhancedTableModel)tblLog.getModel();        
        if (model.getValueAt(selectedRow, 1).toString().equals("ERROR")) {
              if(dayBeginPrev != null && dayBeginPrev.length()>0){
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
    //            ViewHTMLDataUI objViewHTMLDataUI = new ViewHTMLDataUI(viewName);
                String taskLable =  CommonUtil.convertObjToStr(model.getValueAt(selectedRow, 0).toString());
                ViewHTMLDataUI objViewHTMLDataUI = new ViewHTMLDataUI(viewName,taskLable);
                objViewHTMLDataUI.show();
            }
        } 
    }//GEN-LAST:event_tblLogMousePressed

    private void btnCompleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCompleteActionPerformed
        // TODO add your handling code here:
        boolean isError = false;
        if(comp.equals("comp")||chk.equals("chk") ||daily.equals("daily")   ){
            btnComplete1ActionPerformed(null);
//            btnDailyActivityActionPerformed(null);
//            btnTransCheckActionPerformed(null);
            int options = -1;
            Date aplDt=null;
                HashMap mapData = new HashMap();
                boolean isTransError = false;
                String displayDetailsStr = "";
                String[] obj ={"Yes","No"};
                  List lst=null;
            if((branMap != null && branMap.containsKey(BRANCH_LST)) && (dayBeginPrev != null && dayBeginPrev.length()>0)){
                  List brnLst = (List)branMap.get(BRANCH_LST);
                  if(brnLst!=null && brnLst.size()>0){
                      HashMap brnMap = (HashMap)brnLst.get(0);
//                      mapData.put("CURR_APPL_DT", brnMap.get("CUR_BRAN_DT"));
                         HashMap whereMap = new HashMap();
                       whereMap.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(brnMap.get(CommonConstants.BRANCH_ID)));
                       whereMap.put("DAYEND_DT", CommonUtil.convertObjToStr(brnMap.get("DAYEND_DT")));
                       brnLst = (List) ClientUtil.executeQuery("getApplDateHashMap", whereMap);
//                       whereMap = null;
                       if(brnLst!=null && brnLst.size() > 0){  
                            mapData = (HashMap)brnLst.get(0);
                            whereMap.put("DAYEND_DT_CUR", mapData.get("CURR_APPL_DT"));
//                            currDt = (Date) whereMap.get("CURR_APPL_DT");
                       }
                      lst = ((List)ClientUtil.executeQuery("getAllBranchesFromDayEndCompPrevDay", whereMap));
                  }

            }else{
                mapData.put("NEXT_DATE", currDt.clone());
//                mapData.put("BRANCH_DAY_END_STATUS", "COMPLETED");
//                mapData.put("DC_DAY_END_STATUS", "COMPLETED");
                lst = ((List)ClientUtil.executeQuery("getAllBranchesFromDayEndComp", mapData));
            }
                ArrayList executeBranchList = new ArrayList();
                executeBranchList.addAll(lst);
                mapData = null;
                if(executeBranchList != null && executeBranchList.size() > 0){
                    for(int i=0;i<executeBranchList.size();i++){                     
                        mapData=new HashMap();
                        mapData = (HashMap)executeBranchList.get(i);
                        HashMap whereMap = new HashMap();
                        whereMap.put(CommonConstants.BRANCH_ID, mapData.get(CommonConstants.BRANCH_ID));
                        List newLst = ClientUtil.executeQuery("getApplDateHashMap", whereMap);
                        whereMap = (HashMap) newLst.get(0);
                        mapData.put("DAYBEGIN_DT", whereMap.get("CURR_APPL_DT"));
                        aplDt = (java.util.Date)mapData.get("DAYBEGIN_DT");
                        whereMap = null;
                        newLst = null;
                        StringBuffer presentTasks = new StringBuffer();
                        presentTasks.append("'" + chkDepositInterestApplication.getText() + "'" +
                        "," + "'" + chkExecSI.getText() + "'" +
                        "," + "'" + chkTod.getText() + "'" +
                        "," + "'" + chkTransOptInOpt.getText() + "'" +
                        "," + "'" + chkTransAcct.getText() + "'" +
                        "," + "'" + chkTLStart.getText() + "'" +
                        "," + "'" + chkAcctCrToDr.getText() + "'");
                        mapData.put("TASK_NAME", presentTasks);
                        List lstData = ((List)ClientUtil.executeQuery("getSelectTransChkReadyDCDayBegin", mapData));
                        mapData = null;
                        if(lstData != null && lstData.size() > 0){
                            for(int j=0;j<lstData.size();j++){
                                mapData = (HashMap)lstData.get(j);
                                if(CommonUtil.convertObjToStr(mapData.get("TASK_STATUS")).equals("ERROR")){
                                    isTransError = true;
                                    displayDetailsStr += "\n"+"Error Branch ID : "+CommonUtil.convertObjToStr(mapData.get("BRANCH_ID")+"\n");
                                     System.out.println("FFFFF"+displayDetailsStr);
                                     executeBranchList.indexOf(mapData.get("BRANCH_ID"));
                                     executeBranchList.remove(i--);
                                     System.out.println("FFFFF"+executeBranchList);
                                     break;
                                }
                            }
                           
                        }else{
                            isTransError = true;
                                    displayDetailsStr += "Run atleast one Trans Check Task  Branch ID : "+CommonUtil.convertObjToStr(mapData.get("BRANCH_ID")+"\n");
                                     System.out.println("FFFFF"+displayDetailsStr);
                                     executeBranchList.remove(i--);
                                     System.out.println("FFFFF"+executeBranchList);
                        }
                    }  
                }else{
//                    ClientUtil.showAlertWindow("No branch is ready for Completion/Rectify the errors to continue");
                    isError = true;
                    options = -2;
                }
                if(isTransError){
                    options =COptionPane.showOptionDialog(null,(displayDetailsStr+"\n"+" Select Yes to Continue with Completion for other Branches Or No to Cancel"), ("COMPLETE"),
                    COptionPane.YES_NO_CANCEL_OPTION,COptionPane.QUESTION_MESSAGE,null,obj,obj[0]);
                }
                if(options == -1 || options == 0){
                    
                }
            for (int i=0, j=tblLog.getRowCount(); i < j; i++) {
                if (tblLog.getValueAt(i, 1).toString().equals("ERROR")) {
                    isError = true;
                    break;
                }
            }
            if (!isError) {
                int yesno = COptionPane.showConfirmDialog(this, "Do you want to Complete Day Begin?", "Note", COptionPane.YES_NO_OPTION);
                if (yesno==COptionPane.YES_OPTION) {
                    lblStatus.setText("Completed");
                    com.see.truetransact.ui.TrueTransactMain.lblDate.setText("<html> Date : <font color=blue>" + DateUtil.getStringDate((Date) currDt.clone()) + "</font></html>");
                    updateTrueTransactMainTree();
                    if (threadPool!=null) {
                        threadPool.stopAllThreads();
                    }
//                     HashMap mapData = new HashMap();
//                     HashMap newMap = new HashMap();
//                     List prevLst = ClientUtil.executeQuery("getPrevDayEndDate", newMap);
//                    if(prevLst!=null && prevLst.size()>0){
//                        newMap = (HashMap) prevLst.get(0);
//                        mapData.put("DAYEND_DT", newMap.get("PREV_DT"));
//                    }
//                    mapData.put("BRANCH_DAY_END_STATUS", "COMPLETED");
//                    mapData.put("DC_DAY_END_STATUS", "COMPLETED");
//                    List lst = ((List)ClientUtil.executeQuery("getSelectBranCompletedLst", mapData));
//                    ArrayList executeBranchList = new ArrayList();
//                    executeBranchList.addAll(lst);
                    HashMap eodMap = new HashMap();
                    for(int a=0; a<executeBranchList.size();a++){
                        eodMap = new HashMap();
                        eodMap = (HashMap)executeBranchList.get(a);
                        eodMap.put(CommonConstants.BRANCH_ID, eodMap.get(CommonConstants.BRANCH_ID));
                        eodMap.put("DAYBEGIN_DT", aplDt);
                        eodMap.put("DC_DAY_BEGIN_STATUS", "COMPLETED");
                        List lstData = ClientUtil.executeQuery("getDCLevelDayBeginStatus", eodMap);
                        if(lstData != null && lstData.size()>0){
                            //do nothing
                        }else{
                            ClientUtil.execute("InsertDayBeginStatusFinal", eodMap);
                        }
                    }
                }
            } else {
                COptionPane.showMessageDialog(this, "You cannot Complete the Day Begin. Recify the Errors");
                btnProcess.setEnabled(false);
            }  
        }
       
        else{
             ClientUtil.showMessageWindow("plz check TransCheck");
        }
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
             //        chkTransOptInOpt.setSelected(value);
             //        chkExecSI.setSelected(value);
             //        chkAcctCrToDr.setSelected(value);
             //        chkDepositInterestApplication.setSelected(value);
             ////        chkGenMaturedIntSlip.setSelected(value);
             //        chkTLStart.setSelected(value);
             //        chkTLStart.setVisible(value);
             //        chkMonInt.setSelected(false);
             //        chkMonInt.setVisible(false);
             //        chkChecksumPrinting.setSelected(false);
             //        chkReversalDebit.setSelected(false);
             //        chkTempTables.setSelected(false);
             //        chkTempTables.setVisible(false);
             //        chkTransAcct.setSelected(false);
             //        chkLocker.setSelected(false);
             //        chkUpdateSacLimit.setSelected(false);
             //        comp="comp";
             //        chkGL_Abstract.setSelected(false);
             //        chkInitSerialNo.setSelected(false);
             //        chkMinor.setSelected(false);
             //        chkPrevDayEnd.setSelected(false);
             //        chkStartDay.setSelected(false);
             //        chkCurrDay.setSelected(false);
             //        chkGL_Abstract.setEnabled(false);
             //        chkInitSerialNo.setEnabled(false);
             //        chkMinor.setEnabled(false);
             //        chkPrevDayEnd.setEnabled(false);
             //        chkStartDay.setEnabled(false);
             //        chkCurrDay.setEnabled(false);
             //        chkTDReverseFlexi.setSelected(false);
             //            btnDailyActivity.setEnabled(false);
             //            btnComplete1.setEnabled(false);
             //            btnComplete.setEnabled(false);
             //newly added
             
             int options = -1;
             HashMap mapData = new HashMap();
             boolean isError = false;
             String displayDetailsStr = "";
             String[] obj ={"Yes","No"};
             mapData.put("NEXT_DATE", currDt.clone());
             //                mapData.put("BRANCH_DAY_END_STATUS", "COMPLETED");
             //                mapData.put("DC_DAY_END_STATUS", "COMPLETED");
             List lst=null;
             if((branMap != null && branMap.containsKey(BRANCH_LST)) && (dayBeginPrev != null && dayBeginPrev.length()>0)){
                 List brnLst = (List)branMap.get(BRANCH_LST);
                 if(brnLst!=null && brnLst.size()>0){
                     HashMap brnMap = (HashMap)brnLst.get(0);
                     //                      mapData.put("CURR_APPL_DT", brnMap.get("CUR_BRAN_DT"));
                     HashMap whereMap = new HashMap();
                     whereMap.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(brnMap.get(CommonConstants.BRANCH_ID)));
                     whereMap.put("DAYEND_DT", CommonUtil.convertObjToStr(brnMap.get("DAYEND_DT")));
                     brnLst = (List) ClientUtil.executeQuery("getApplDateHashMap", whereMap);
                     //                       whereMap = null;
                     if(brnLst!=null && brnLst.size() > 0){
                         mapData = (HashMap)brnLst.get(0);
                         whereMap.put("DAYEND_DT_CUR", mapData.get("CURR_APPL_DT"));
                         //                            currDt = (Date) whereMap.get("CURR_APPL_DT");
                     }
                     lst = ((List)ClientUtil.executeQuery("getAllBranchesFromDayEndCompPrevDay", whereMap));
                 }
                 
             }else{
                 lst = ((List)ClientUtil.executeQuery("getAllBranchesFromDayEndComp", mapData));
             }
             ArrayList executeBranchList = new ArrayList();
             executeBranchList.addAll(lst);
             mapData = null;
             if(executeBranchList != null && executeBranchList.size() > 0){
                 for(int i=0;i<executeBranchList.size();i++){
                     mapData=new HashMap();
                     mapData = (HashMap)executeBranchList.get(i);
                     HashMap whereMap = new HashMap();
                     whereMap.put(CommonConstants.BRANCH_ID, mapData.get(CommonConstants.BRANCH_ID));
                     List newLst = ClientUtil.executeQuery("getApplDateHashMap", whereMap);
                     whereMap = (HashMap) newLst.get(0);
                     mapData.put("DAYBEGIN_DT", whereMap.get("CURR_APPL_DT"));
                     whereMap = null;
                     newLst = null;
                     StringBuffer presentTasks = new StringBuffer();
                     presentTasks.append("'" + chkGL_Abstract.getText() + "'" +
                     "," + "'" + chkMinor.getText() + "'" +
                     "," + "'" + chkTDReverseFlexi.getText() + "'");
                     mapData.put("TASK_NAME", presentTasks);
                     List lstData = ((List)ClientUtil.executeQuery("getSelectTransChkReadyDCDayBegin", mapData));
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
                 //                    observable.resetAll();
                 //                    TaskHeader header = null ;
                 //                    HashMap taskParamMap = new HashMap();
                 //                    taskParamMap.put("DAY_END_TYPE", dayEndType);
                 //                    if(executeBranchList!=null && executeBranchList.size()>0)
                 //                    taskParamMap.put(BRANCH_LST, executeBranchList);
                 //                    taskParamMap.put("GL_ABS_TASK_LABLE", "GL Abstract");
                 //                    taskParamMap.put("DAILY_BAL_UPDATE_TASK_LABLE", "Update Daily Balance");
                 //                    header = getTaskFromMap();
                 //                    header.setProcessType(CommonConstants.DAY_END);
                 //                    header.setTaskParam(taskParamMap);
                 //                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkGL_Abstract.getText())));
                 //                    observable.setEachTask(chkGL_Abstract, header);
                 //                    header = getTaskFromMap();
                 //                    header.setProductType("");
                 //                    header.setTaskParam(taskParamMap);
                 //                    header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkDailyBalance.getText())));
                 //                    observable.setEachTask(chkDailyBalance, header);
                 //
                 //                    updateTable();
                 //                    threadPool = new ThreadPool(observable);
                 //                    threadPool.setTaskList(observable.getTaskList());
                 //                    threadPool.initPooledThread();
                 //                    threadPool.start();
                 //                    taskRunning = true;
                 //                    comp="comp";
                 //                    taskParamMap = null;
                 value=true;
                 chkTransOptInOpt.setSelected(value);
                 chkExecSI.setSelected(value);
                 chkTod.setSelected(value);
                 chkAcctCrToDr.setSelected(value);
                 chkDepositInterestApplication.setSelected(value);
                 //        chkGenMaturedIntSlip.setSelected(value);
                 chkTLStart.setSelected(value);
                 chkTLStart.setVisible(value);
                 chkMonInt.setSelected(false);
                 chkMonInt.setVisible(false);
                 chkChecksumPrinting.setSelected(false);
                 chkReversalDebit.setSelected(false);
                 chkTempTables.setSelected(false);
                 chkTempTables.setVisible(false);
                 chkTransAcct.setSelected(false);
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
                 chkPrevDayEnd.setEnabled(false);
                 chkStartDay.setEnabled(false);
                 chkCurrDay.setEnabled(false);
                 chkTDReverseFlexi.setSelected(false);
                 btnDailyActivity.setEnabled(false);
                 btnComplete1.setEnabled(false);
                 btnComplete.setEnabled(false);
                 if(executeBranchList!=null && executeBranchList.size()>0) {
                     branMap.put(BRANCH_LST, executeBranchList);
                 }
             }else{
                 ClientUtil.showAlertWindow("No branch is ready for TransCheck/Rectify the errors to continue");
             }
             
         }
         else{
             ClientUtil.showAlertWindow("ggggg");
         }
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
             //        chkGL_Abstract.setSelected(true);
             //        chkInitSerialNo.setSelected(true);
             //        chkMinor.setSelected(true);
             //        btnComplete1.setEnabled(false);
             //        btnProcess.setEnabled(true);
             //        btnTransCheck.setEnabled(false);
             //        btnComplete.setEnabled(false);
             //        chkTDReverseFlexi.setSelected(true);
             //        daily="daily";
             //
             //        chkPrevDayEnd.setEnabled(false);
             //        chkPrevDayEnd.setSelected(false);
             //        chkStartDay.setSelected(false);
             //        chkCurrDay.setSelected(false);
             //        chkStartDay.setEnabled(false);
             //        chkCurrDay.setEnabled(false);
             //         chkMonInt.setSelected(false);
             //        chkTLStart.setSelected(false);
             //        chkGenMaturedIntSlip.setSelected(false);
             //        chkTransOptInOpt.setSelected(false);
             //        chkChecksumPrinting.setSelected(false);
             //        chkReversalDebit.setSelected(false);
             //        chkDepositInterestApplication.setSelected(false);
             //        chkTempTables.setSelected(false);
             //        chkExecSI.setSelected(false);
             //        chkTransAcct.setSelected(false);
             //        chkAcctCrToDr.setSelected(false);
             //        chkLocker.setSelected(false);
             //        chkUpdateSacLimit.setSelected(false);
             //
             //        chkMonInt.setEnabled(false);
             //        chkTLStart.setEnabled(false);
             //        chkGenMaturedIntSlip.setEnabled(false);
             //        chkTransOptInOpt.setEnabled(false);
             //        chkChecksumPrinting.setEnabled(false);
             //        chkReversalDebit.setEnabled(false);
             //        chkDepositInterestApplication.setEnabled(false);
             //        chkTempTables.setEnabled(false);
             //        chkExecSI.setEnabled(false);
             //        chkTransAcct.setEnabled(false);
             //        chkAcctCrToDr.setEnabled(false);
             //        chkLocker.setEnabled(false);
             //        chkUpdateSacLimit.setSelected(false);
             
             //newly added
             HashMap mapData = new HashMap();
             //         HashMap mapData = new HashMap();
             List lst=null;
             if((branMap != null && branMap.containsKey(BRANCH_LST)) && (dayBeginPrev != null && dayBeginPrev.length()>0)){
                 List brnLst = (List)branMap.get(BRANCH_LST);
                 if(brnLst!=null && brnLst.size()>0){
                     HashMap brnMap = (HashMap)brnLst.get(0);
                     //                      mapData.put("CURR_APPL_DT", brnMap.get("CUR_BRAN_DT"));
                     HashMap whereMap = new HashMap();
                     whereMap.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(brnMap.get(CommonConstants.BRANCH_ID)));
                     whereMap.put("DAYEND_DT", CommonUtil.convertObjToStr(brnMap.get("DAYEND_DT")));
                     brnLst = (List) ClientUtil.executeQuery("getApplDateHashMap", whereMap);
                     //                       whereMap = null;
                     if(brnLst!=null && brnLst.size() > 0){
                         mapData = (HashMap)brnLst.get(0);
                         whereMap.put("DAYEND_DT_CUR", mapData.get("CURR_APPL_DT"));
                         //                            currDt = (Date) whereMap.get("CURR_APPL_DT");
                     }
                     lst = ((List)ClientUtil.executeQuery("getAllBranchesFromDayEndCompPrevDay", whereMap));
                 }
                 
             }else{
                 mapData.put("NEXT_DATE", currDt.clone());
                 lst = ((List)ClientUtil.executeQuery("getAllBranchesFromDayEndComp", mapData));
             }
             mapData = null;
             if(lst != null && lst.size() > 0){
                 String displayDetailsStr = "";
                 String[] obj ={"Yes","No"};
                 for(int i=0;i<lst.size();i++){
                     mapData = (HashMap)lst.get(i);
                     displayDetailsStr += " Branch ID : "+CommonUtil.convertObjToStr(mapData.get(CommonConstants.BRANCH_ID))+"\n";
                 }
                 int options =COptionPane.showOptionDialog(null,(displayDetailsStr+"\n"+" Select Yes to Continue with DailyActivity for these Branches Or No to Cancel"), ("Daily Activity"),
                 COptionPane.YES_NO_CANCEL_OPTION,COptionPane.QUESTION_MESSAGE,null,obj,obj[0]);
                 mapData = null;
                 if(options == 0){
                     doConti = true;
                 }else{
                     doConti = false;
                 }
                 if(doConti){
                     btnComplete1ActionPerformed(null);
                     value =doConti;
                     chkGL_Abstract.setSelected(true);
                     chkInitSerialNo.setSelected(true);
                     chkMinor.setSelected(true);
                     btnComplete1.setEnabled(false);
                     btnProcess.setEnabled(true);
                     btnTransCheck.setEnabled(false);
                     btnComplete.setEnabled(false);
                     chkTDReverseFlexi.setSelected(true);
                     daily="daily";
                     
                     chkPrevDayEnd.setEnabled(false);
                     chkPrevDayEnd.setSelected(false);
                     chkStartDay.setSelected(false);
                     chkCurrDay.setSelected(false);
                     chkStartDay.setEnabled(false);
                     chkCurrDay.setEnabled(false);
                     chkMonInt.setSelected(false);
                     chkTLStart.setSelected(false);
                     chkGenMaturedIntSlip.setSelected(false);
                     chkTransOptInOpt.setSelected(false);
                     chkChecksumPrinting.setSelected(false);
                     chkReversalDebit.setSelected(false);
                     chkDepositInterestApplication.setSelected(false);
                     chkTempTables.setSelected(false);
                     chkExecSI.setSelected(false);
                     chkTod.setSelected(false);
                     chkTransAcct.setSelected(false);
                     chkAcctCrToDr.setSelected(false);
                     chkLocker.setSelected(false);
                     chkUpdateSacLimit.setSelected(false);
                     
                     chkMonInt.setEnabled(false);
                     chkTLStart.setEnabled(false);
                     chkGenMaturedIntSlip.setEnabled(false);
                     chkTransOptInOpt.setEnabled(false);
                     chkChecksumPrinting.setEnabled(false);
                     chkReversalDebit.setEnabled(false);
                     chkDepositInterestApplication.setEnabled(false);
                     chkTempTables.setEnabled(false);
                     chkExecSI.setEnabled(false);
                     chkTod.setEnabled(false);
                     chkTransAcct.setEnabled(false);
                     chkAcctCrToDr.setEnabled(false);
                     chkLocker.setEnabled(false);
                     chkUpdateSacLimit.setSelected(false);
                     btnProcess.setEnabled(true);
                     btnComplete.setEnabled(true);
                     btnComplete1.setEnabled(true);
                     daily="daily";
                     ArrayList executeBranchList = new ArrayList();
                     executeBranchList.addAll(lst);
                     if(executeBranchList!=null && executeBranchList.size()>0) {
                         branMap.put(BRANCH_LST, executeBranchList);
                     }
                 }else{
                     value = doConti;
                     btnProcess.setEnabled(false);
                     
                 }
             }else{
                 ClientUtil.showAlertWindow("No branch is ready for DailyActivity/Rectify the errors to continue");
                 btnProcess.setEnabled(false);
             }
         } else{
             ClientUtil.showAlertWindow("fgdfgdfgdfgdfgfg");
         }
    }//GEN-LAST:event_btnDailyActivityActionPerformed

    private void btnComplete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnComplete1ActionPerformed
        // TODO add your handling code here:
        boolean value = false;
        if(chk.equals("chk")) {
            value=false;
        } else {
            value=true;
        }
        chkPrevDayEnd.setSelected(value);
        chkStartDay.setSelected(value);
        chkCurrDay.setSelected(value);
        chk = "chk";
        chkGL_Abstract.setSelected(false);
        chkInitSerialNo.setSelected(false);
        chkMinor.setSelected(false);
        chkGL_Abstract.setEnabled(false);
        chkInitSerialNo.setEnabled(false);
        chkMinor.setEnabled(false);
        chkMonInt.setSelected(false);
        chkTLStart.setSelected(false);//false
        chkGenMaturedIntSlip.setSelected(false);
        chkTransOptInOpt.setSelected(false);
        chkChecksumPrinting.setSelected(false);
        chkReversalDebit.setSelected(false);
        chkDepositInterestApplication.setSelected(false);
        chkTempTables.setSelected(false);
        chkExecSI.setSelected(false);
        chkTod.setSelected(false);
        chkTransAcct.setSelected(false);
        chkAcctCrToDr.setSelected(false);
        chkLocker.setSelected(false);
        chkUpdateSacLimit.setSelected(false);
        //        chkTDReverseFlexi.setSelected(false);
        chkTDReverseFlexi.setEnabled(false);
        chkMonInt.setEnabled(false);
        chkTLStart.setEnabled(false);//false
        chkGenMaturedIntSlip.setEnabled(false);
        chkTransOptInOpt.setEnabled(false);
        chkChecksumPrinting.setEnabled(false);
        chkReversalDebit.setEnabled(false);
        chkDepositInterestApplication.setEnabled(false);
        chkTempTables.setEnabled(false);
        chkExecSI.setEnabled(false);
        chkTod.setEnabled(false);
        chkTransAcct.setEnabled(false);
        chkAcctCrToDr.setEnabled(false);
        chkLocker.setEnabled(false);
        chkUpdateSacLimit.setSelected(false);
        btnDailyActivity.setEnabled(false);
        btnComplete1.setEnabled(false);
        btnTransCheck.setEnabled(false);
        btnComplete.setEnabled(false);
        if(daily.equals("daily")) {
            isCheck=false;
        } else {
            isCheck=true;
        }
    }//GEN-LAST:event_btnComplete1ActionPerformed
                        private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
                            // TODO add your handling code here:
//                            chkDepositInterestApplication.setSelected(false);
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
                                    newBranMap.put("DAYEND_DT", CommonUtil.convertObjToStr(tblData.getValueAt(i,1)));
//                                    newBranMap.put("CUR_BRAN_DT", curBranDt);
                                    branAryLst.add(i, newBranMap);
                                    branMap.put(BRANCH_LST, branAryLst);
                                    dayBeginPrev = "DAY_BEGIN_PREV";
                                    isCheck = false;
                                }
//                                branMap.put(BRANCH_LST, branAryLst);
//                                dayEndPrev = "DAY_END_PREV";
                            }
                            }
                            }
                            btnProcessActionPerformed();
                            btnDailyActivity.setEnabled(true);
                            btnComplete1.setEnabled(true);
                            btnTransCheck.setEnabled(true);
                            btnComplete.setEnabled(true);
    }//GEN-LAST:event_btnProcessActionPerformed
    private void btnProcessActionPerformed() {
        HashMap taskParamMap = new HashMap();
        taskParamMap.put("DAY_END_TYPE", dayEndType);
        if(branMap != null && branMap.containsKey(BRANCH_LST)) {
            taskParamMap.put(BRANCH_LST, branMap.get(BRANCH_LST));
        }
        if(dayBeginPrev != null && dayBeginPrev.length()>0) {
            taskParamMap.put("DAY_BEGIN_PREV", dayBeginPrev);
        }
        TaskHeader header = null ;
        if (!taskRunning){
            observable.resetAll();
            if (chkPrevDayEnd.isSelected()){
                header = getTaskFromMap();
//                taskParamMap.put("DAY_END_TYPE", dayEndType);
                taskParamMap.put("CHK_PREV_DAYEND_TASK_LABLE", "Check for Previous Day's Day-End");
                header.setTaskParam(taskParamMap);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkPrevDayEnd.getText())));
                observable.setEachTask(chkPrevDayEnd, header);
            }
            if (chkStartDay.isSelected()){
                header = getTaskFromMap();
                HashMap paramMap = new HashMap();
                paramMap.put("CURR_DATE", currDate);
                paramMap.put("DAY_END_TYPE", dayEndType);
                paramMap.put("INTER_BRANCH_ON_HOLIDAY", new Boolean(interBranchOnHoliday));
                paramMap.put("CHK_CUR_DAYBEGIN_TASK_LABLE", "Check for Current Day's Day-Begin");
                if(branMap != null && branMap.containsKey(BRANCH_LST)) {
                    paramMap.put(BRANCH_LST, branMap.get(BRANCH_LST));
                }
                if(dayBeginPrev != null && dayBeginPrev.length()>0) {
                    paramMap.put("DAY_BEGIN_PREV", dayBeginPrev);
                }
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
                if(branMap != null && branMap.containsKey(BRANCH_LST)) {
                    map.put(BRANCH_LST, branMap.get(BRANCH_LST));
                }
                map.put("PROCESS","DAY_BEGIN");
                map.put("DAY_END_TYPE", dayEndType);
                map.put("DEP_INT_APPL_TASK_LABLE", "Deposit Interest Application");
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
                taskParamMap.put("CHK_EXEC_STD_TASK_LABLE", "Executing Standing Instructions");
                header.setTaskParam(taskParamMap);
                header.setProcessType(CommonConstants.DAY_BEGIN);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkExecSI.getText())));
                observable.setEachTask(chkExecSI, header);
            }
            if(chkTod.isSelected()){
                header = getTaskFromMap();
                taskParamMap.put("EXEC_TOD_TASK_LABLE", "Execute TOD");
                header.setTaskParam(taskParamMap);
                header.setProcessType(CommonConstants.DAY_BEGIN);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkTod.getText())));
                observable.setEachTask(chkTod, header);
            }
            // Minor To Major Task
            if (chkMinor.isSelected()){
                header = getTaskFromMap();
                taskParamMap.put("CHK_MINOR_TASK_LABLE", "Minor Turning Major Activity");
                header.setTaskParam(taskParamMap);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkMinor.getText())));
                observable.setEachTask(chkMinor, header);
            }
            if (chkUpdateSacLimit.isSelected()){
                observable.setTabTask(chkUpdateSacLimit);
            }
            //loans demand task
            if (chkTLStart.isSelected()){
                header = getTaskFromMap();
//                observable.setTabTask(chkTLStart);
                taskParamMap.put("CHK_TL_START_TASK_LABLE", "Term Loan Day-Begin Processing ");
                header.setTaskParam(taskParamMap);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkTLStart.getText())));
                observable.setEachTask(chkTLStart, header);
            }
            if (chkTDReverseFlexi.isSelected()){
                header = getTaskFromMap();
//                observable.setTabTask(chkTLStart);
                taskParamMap.put("CHK_TD_REV_FLX_TASK_LABLE", "Reverse Flexi on Maturity");
                header.setTaskParam(taskParamMap);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkTDReverseFlexi.getText())));
                observable.setEachTask(chkTDReverseFlexi, header);
            }
            
            if (chkLocker.isSelected()){
                observable.setTabTask(chkLocker);
            }
            //Deposit Account Matured 
            if (chkGenMaturedIntSlip.isSelected()){
                header = getTaskFromMap();
                taskParamMap.put("CHK_MAT_INT_TASK_LABLE", "Generate Interest Slip for Matured Account");
                header.setTaskParam(taskParamMap);
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
                taskParamMap.put("CHK_TRN_OPT_TASK_LABLE", "Transfer of Operative Account to Inoperative");
                header.setTaskParam(taskParamMap);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkTransOptInOpt.getText())));
                observable.setEachTask(chkTransOptInOpt, header);
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
//                HashMap taskParamMap = new HashMap();
                    taskParamMap.put("DAY_END_TYPE", dayEndType);
                    taskParamMap.put("CREATE_GL_ABS_TASK_LABLE", "Create GL Abstract");
                header.setTaskParam(taskParamMap);
                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkGL_Abstract.getText())));
                observable.setEachTask(chkGL_Abstract, header);
            }
            
            updateTable();
            threadPool = new ThreadPool(observable);
            threadPool.setTaskList(observable.getTaskList());
            threadPool.initPooledThread();
            threadPool.start();
            taskRunning = true;
            ClientUtil.enableDisable(panBatchProcess , false);
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
        observable = null;
        if (threadPool!=null) {
            threadPool.stopAllThreads();
        }
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
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
        }else if (tskHeader.getTaskClass().equals("TodTask")){
            enable = observable.updateTaskStatusInLogTable(chkTod);
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
//                    com.see.truetransact.ui.TrueTransactMain.lblDate.setText("<html> Date : <font color=blue>" + DateUtil.getStringDate(currDt.clone()) + "</font></html>");
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
        whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
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
    private com.see.truetransact.uicomponent.CButton btnPrinted;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CButton btnTransCheck;
    private com.see.truetransact.uicomponent.CCheckBox chkAcctCrToDr;
    private com.see.truetransact.uicomponent.CCheckBox chkChecksumPrinting;
    private com.see.truetransact.uicomponent.CCheckBox chkCurrDay;
    private com.see.truetransact.uicomponent.CCheckBox chkDepositInterestApplication;
    private com.see.truetransact.uicomponent.CCheckBox chkExecSI;
    private com.see.truetransact.uicomponent.CCheckBox chkGL_Abstract;
    private com.see.truetransact.uicomponent.CCheckBox chkGenMaturedIntSlip;
    private com.see.truetransact.uicomponent.CCheckBox chkInitSerialNo;
    private com.see.truetransact.uicomponent.CCheckBox chkLocker;
    private com.see.truetransact.uicomponent.CCheckBox chkMinor;
    private com.see.truetransact.uicomponent.CCheckBox chkMonInt;
    private com.see.truetransact.uicomponent.CCheckBox chkPrevDayEnd;
    private com.see.truetransact.uicomponent.CCheckBox chkReversalDebit;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll1;
    private com.see.truetransact.uicomponent.CCheckBox chkStartDay;
    private com.see.truetransact.uicomponent.CCheckBox chkTDReverseFlexi;
    private com.see.truetransact.uicomponent.CCheckBox chkTLStart;
    private com.see.truetransact.uicomponent.CCheckBox chkTempTables;
    private com.see.truetransact.uicomponent.CCheckBox chkTod;
    private com.see.truetransact.uicomponent.CCheckBox chkTransAcct;
    private com.see.truetransact.uicomponent.CCheckBox chkTransOptInOpt;
    private com.see.truetransact.uicomponent.CCheckBox chkUpdateSacLimit;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace24;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CPanel panBatchProcess;
    private com.see.truetransact.uicomponent.CPanel panBatchProcess1;
    private com.see.truetransact.uicomponent.CPanel panCheckBoxes1;
    private com.see.truetransact.uicomponent.CPanel panComplete;
    private com.see.truetransact.uicomponent.CPanel panMultiSearch;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CScrollPane scrOutputMsg;
    private com.see.truetransact.uicomponent.CScrollPane scrOutputMsg1;
    private com.see.truetransact.uicomponent.CSeparator sptSpace1;
    private com.see.truetransact.uicomponent.CSeparator sptSpace2;
    private com.see.truetransact.uicomponent.CTabbedPane tabDCDayBegin;
    private com.see.truetransact.uicomponent.CTable tblData;
    private com.see.truetransact.uicomponent.CTable tblLog;
    private javax.swing.JToolBar tbrHead;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    // End of variables declaration//GEN-END:variables
    
}
