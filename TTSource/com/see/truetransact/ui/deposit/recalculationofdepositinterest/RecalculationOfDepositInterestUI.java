/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * recalculationofdepositinterestUI.java
 *
 * Created on August 6, 2003, 10:51 AM
 */

package com.see.truetransact.ui.deposit.recalculationofdepositinterest;

import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.commonutil.InterestCalc;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.batchprocess.*;
import java.util.Observable;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
//import java.util.GregorianCalendar;
import com.see.truetransact.uicomponent.COptionPane;
/**
 *
 * @author  Sathiya
 */
public class RecalculationOfDepositInterestUI extends CInternalFrame implements java.util.Observer {
    
//    ChargesRB RecalculationOfDepositInterestRB = new ChargesRB();
    
    int CREDIT = 0, DEBIT = 0;
    
    private TaskStatus tskStatus;
    private TaskHeader tskHeader;
//    private ThreadPool threadPool;
    private RecalculationOfDepositInterestOB observable;
    private boolean taskRunning;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    Date CurDate = null;
    final int TO=0, FROM=1;
    int viewType=-1;
    private String BEHAVES_LIKE = "BEHAVES_LIKE";
    private String FIXED = "FIXED";
    private String CUMMULATIVE = "CUMMULATIVE";
    private String RECURRING = "RECURRING";
    private String DAILY = "DAILY";
    //    private HashMap taskMap;
    
    /** Creates new form ChargesUI */
    public RecalculationOfDepositInterestUI() {
        try{
            initComponents();
            initSetup();
            CurDate = ClientUtil.getCurrentDate();
        } catch (Exception e) {
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }                
    }
    
    private void initSetup() throws Exception{
        setFieldNames();
        internationalize();
        setObservable();
        initComponentData();    // Fill all the combo boxes
        btnProcess.setEnabled(false);
        btnClear.setEnabled(false);        
        setupInit();
        txtFromAccount.setAllowAll(true);
        txtFromAccount.setMaxLength(16);
        txtToAccount.setAllowAll(true);
        txtToAccount.setMaxLength(16);
        tdtToDate.setEnabled(false);
        btnClose.setVisible(false);
        btnPrint.setVisible(false);
        btnNew.setEnabled(true);
        ClientUtil.enableDisable(panChargesApplication,false);
        btnFromAccount.setEnabled(false);
        btnToAccount.setEnabled(false);
        btnView.setEnabled(false);
        btnClear1.setEnabled(false);
    }
    
    // Creates The Instance of ChargesOB
    private void setObservable() throws Exception{
        observable = new RecalculationOfDepositInterestOB();
        observable.addObserver(this);
    }
    
   /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnClose.setName("btnClose");
        btnFromAccount.setName("btnFromAccount");
        btnPrint.setName("btnPrint");
        btnProcess.setName("btnProcess");
        btnToAccount.setName("btnToAccount");
        cboProductId.setName("cboProductId");
        lblFromAccount.setName("lblFromAccount");
        lblFromDate.setName("lblFromDate");
        lblMsg.setName("lblMsg");
        lblProductId.setName("lblProductId");
        lblSpace1.setName("lblSpace1");
        lblSpace4.setName("lblSpace4");
        lblStatus.setName("lblStatus");
        lblToAccount.setName("lblToAccount");
        lblToDate.setName("lblToDate");
        panStatus.setName("panStatus");
        panTable.setName("panTable");
        scrOutputMsg.setName("scrOutputMsg");
        tblLog.setName("tblLog");
        tdtFromDate.setName("tdtFromDate");
        tdtToDate.setName("tdtToDate");
        txtFromAccount.setName("txtFromAccount");
        txtToAccount.setName("txtToAccount");
    }    
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
//        btnFromAccount.setText(resourceBundle.getString("btnFromAccount"));
//        chkExcessTransactionCharges.setText(resourceBundle.getString("chkExcessTransactionCharges"));
//        lblFromDate.setText(resourceBundle.getString("lblFromDate"));
//        lblProdType.setText(resourceBundle.getString("lblProdType"));
//        btnClose.setText(resourceBundle.getString("btnClose"));
//        lblToAccount.setText(resourceBundle.getString("lblToAccount"));
//        chkFolioCharges.setText(resourceBundle.getString("chkFolioCharges"));
//        chkInoperativeCharges.setText(resourceBundle.getString("chkInoperativeCharges"));
//        lblFromAccount.setText(resourceBundle.getString("lblFromAccount"));
//        btnToAccount.setText(resourceBundle.getString("btnToAccount"));
//        lblMsg.setText(resourceBundle.getString("lblMsg"));
//        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
//        btnProcess.setText(resourceBundle.getString("btnProcess"));
//        lblToDate.setText(resourceBundle.getString("lblToDate"));
//        lblProductId.setText(resourceBundle.getString("lblProductId"));
//        
//        rdoInterestYes.setText(resourceBundle.getString("rdoInterestYes"));
//        rdoInterestNo.setText(resourceBundle.getString("rdoInterestNo"));
//        ((javax.swing.border.TitledBorder)panChargeType.getBorder()).setTitle(resourceBundle.getString("panChargeType"));
//        lblStatus.setText(resourceBundle.getString("lblStatus"));
//        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
//        btnPrint.setText(resourceBundle.getString("btnPrint"));
//        chkNonMaintenanceOFMinBalCharges.setText(resourceBundle.getString("chkNonMaintenanceOFMinBalCharges"));
    }
    
    
    private void setupInit() {
        HashMap taskMap = new HashMap();
//        taskMap.put(chkExcessTransactionCharges.getText(), "ExcessTransChrgesTask");
//        taskMap.put(chkFolioCharges.getText(), "FolioChargesTask");
//        taskMap.put(chkInoperativeCharges.getText(), "InOperativeChargesTask");
//        taskMap.put(chkNonMaintenanceOFMinBalCharges.getText(), "MinBalanceChargesTask");
//        taskMap.put(chkCredit.getText(), "InterestTask");
//        taskMap.put(chkDebit.getText(), "InterestTask");
//        
//        observable.setTaskMap(taskMap);
//        tblLog.setModel(observable.getDayBeginTableModel());
        tskStatus = new TaskStatus();
        tskHeader = new TaskHeader();
        taskRunning = false;
        taskMap = null;
    }
    // To fill the Data into the Combo Boxes
    // it invokes the Combo Box model defined in OB class
    private void initComponentData() {
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
        try{
            // To obtain the Product id from the Table "OP_AC_PRODUCT"
            // here "getAccProduct" is the mapped Statement name, defined in InwardClearingMap
//            lookup_keys.add("PRODUCTTYPE");
//            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
//            keyValue = ClientUtil.populateLookupData(lookUpHash);
//            getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
//            
//            cboProdType.setModel(new ComboBoxModel(key,value));
            cboProductId.setModel(observable.getCbmProductId());

            //            lookUpHash.put(CommonConstants.MAP_NAME,"getAccProducts");
            //            lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
            //            keyValue = ClientUtil.populateLookupData(lookUpHash);
            //            getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
            //            cboProductId.setModel(new ComboBoxModel(key,value));
        }catch(Exception e){
            System.out.println("Exception in initComponentData()");
            e.printStackTrace();
        }
    }
    
    // Get the value from the Hash Map depending on the Value of Key
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        rdoTaskType = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoTaskType1 = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrHead = new javax.swing.JToolBar();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panChargesApplication = new com.see.truetransact.uicomponent.CPanel();
        panProductId = new com.see.truetransact.uicomponent.CPanel();
        lblProductId = new com.see.truetransact.uicomponent.CLabel();
        cboProductId = new com.see.truetransact.uicomponent.CComboBox();
        lblFromAccount = new com.see.truetransact.uicomponent.CLabel();
        txtFromAccount = new com.see.truetransact.uicomponent.CTextField();
        lblFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtFromDate = new com.see.truetransact.uicomponent.CDateField();
        btnFromAccount = new com.see.truetransact.uicomponent.CButton();
        lblToAccount = new com.see.truetransact.uicomponent.CLabel();
        txtToAccount = new com.see.truetransact.uicomponent.CTextField();
        lblToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtToDate = new com.see.truetransact.uicomponent.CDateField();
        btnToAccount = new com.see.truetransact.uicomponent.CButton();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        scrOutputMsg = new com.see.truetransact.uicomponent.CScrollPane();
        tblLog = new com.see.truetransact.uicomponent.CTable();
        panProductId1 = new com.see.truetransact.uicomponent.CPanel();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        btnView = new com.see.truetransact.uicomponent.CButton();
        btnProcess = new com.see.truetransact.uicomponent.CButton();
        btnClear1 = new com.see.truetransact.uicomponent.CButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Recalculation of Deposit");
        setMinimumSize(new java.awt.Dimension(725, 620));
        setPreferredSize(new java.awt.Dimension(725, 620));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif")));
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        tbrHead.add(btnNew);

        lblSpace4.setText("     ");
        tbrHead.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif")));
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });

        tbrHead.add(btnPrint);

        lblSpace5.setText("     ");
        tbrHead.add(lblSpace5);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif")));
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        tbrHead.add(btnClose);

        lblSpace6.setText("     ");
        tbrHead.add(lblSpace6);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif")));
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        tbrHead.add(btnCancel);

        getContentPane().add(tbrHead, java.awt.BorderLayout.NORTH);

        panStatus.setLayout(new java.awt.GridBagLayout());

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

        panChargesApplication.setLayout(new java.awt.GridBagLayout());

        panChargesApplication.setBorder(new javax.swing.border.EtchedBorder());
        panChargesApplication.setMinimumSize(new java.awt.Dimension(710, 470));
        panChargesApplication.setPreferredSize(new java.awt.Dimension(710, 470));
        panProductId.setLayout(new java.awt.GridBagLayout());

        panProductId.setName("panProductId");
        panProductId.setPreferredSize(new java.awt.Dimension(700, 87));
        lblProductId.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductId.add(lblProductId, gridBagConstraints);

        cboProductId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductId.setPopupWidth(200);
        cboProductId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductIdActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductId.add(cboProductId, gridBagConstraints);

        lblFromAccount.setText("From Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductId.add(lblFromAccount, gridBagConstraints);

        txtFromAccount.setEditable(false);
        txtFromAccount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFromAccount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFromAccountFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductId.add(txtFromAccount, gridBagConstraints);

        lblFromDate.setText("Account Opened From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductId.add(lblFromDate, gridBagConstraints);

        tdtFromDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFromDateFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductId.add(tdtFromDate, gridBagConstraints);

        btnFromAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnFromAccount.setToolTipText("From Account");
        btnFromAccount.setPreferredSize(new java.awt.Dimension(21, 21));
        btnFromAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFromAccountActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductId.add(btnFromAccount, gridBagConstraints);

        lblToAccount.setText("To Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductId.add(lblToAccount, gridBagConstraints);

        txtToAccount.setEditable(false);
        txtToAccount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtToAccount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtToAccountFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductId.add(txtToAccount, gridBagConstraints);

        lblToDate.setText("Account Opened To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductId.add(lblToDate, gridBagConstraints);

        tdtToDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtToDateFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductId.add(tdtToDate, gridBagConstraints);

        btnToAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnToAccount.setToolTipText("To Account");
        btnToAccount.setPreferredSize(new java.awt.Dimension(21, 21));
        btnToAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToAccountActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductId.add(btnToAccount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChargesApplication.add(panProductId, gridBagConstraints);

        panTable.setLayout(new java.awt.GridBagLayout());

        scrOutputMsg.setMinimumSize(new java.awt.Dimension(650, 300));
        scrOutputMsg.setPreferredSize(new java.awt.Dimension(700, 300));
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
        tblLog.setReorderingAllowed(false);
        scrOutputMsg.setViewportView(tblLog);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTable.add(scrOutputMsg, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChargesApplication.add(panTable, gridBagConstraints);

        panProductId1.setLayout(new java.awt.GridBagLayout());

        panProductId1.setMinimumSize(new java.awt.Dimension(700, 35));
        panProductId1.setName("panProductId");
        panProductId1.setPreferredSize(new java.awt.Dimension(700, 35));
        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductId1.add(btnClear, gridBagConstraints);

        btnView.setText("View");
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductId1.add(btnView, gridBagConstraints);

        btnProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif")));
        btnProcess.setToolTipText("Start Process");
        btnProcess.setMinimumSize(new java.awt.Dimension(63, 27));
        btnProcess.setPreferredSize(new java.awt.Dimension(63, 27));
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductId1.add(btnProcess, gridBagConstraints);

        btnClear1.setText("Recalculated Interest Details");
        btnClear1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClear1ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductId1.add(btnClear1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChargesApplication.add(panProductId1, gridBagConstraints);

        getContentPane().add(panChargesApplication, java.awt.BorderLayout.CENTER);

        pack();
    }//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        btnClearActionPerformed(null);
        setModified(false);
        cboProductId.setEnabled(false);
        tdtFromDate.setEnabled(false);
        btnFromAccount.setEnabled(false);
        btnToAccount.setEnabled(false);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        ClientUtil.enableDisable(panChargesApplication,true);
        btnFromAccount.setEnabled(true);
        btnToAccount.setEnabled(true);
        tdtToDate.setEnabled(false);
        btnView.setEnabled(true);
        btnClear.setEnabled(true);
        txtFromAccount.setEnabled(false);
        txtToAccount.setEnabled(false);
        btnProcess.setEnabled(false);
        btnClear.setEnabled(false);
        btnClear1.setEnabled(true);
        tdtToDate.setDateValue(DateUtil.getStringDate(CurDate));
        setModified(true);
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnClear1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClear1ActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());            
        btnProcess.setEnabled(false);
        Date fromDt = null;
        Date toDt = null;
        if(tdtFromDate.getDateValue().length()>0){
            fromDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtFromDate.getDateValue()));
        }else{
            fromDt = null;
        }
        if(tdtFromDate.getDateValue().length()>0){
            toDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtToDate.getDateValue()));
        }else{
            toDt = null;
        }
        txtFromAccount.setText("");
        txtToAccount.setText("");
        String productId = CommonUtil.convertObjToStr(cboProductId.getSelectedItem().toString());
        observable.recalculatedRecords(fromDt,toDt,((ComboBoxModel)cboProductId.getModel()).getKeyForSelected().toString(),
        CommonUtil.convertObjToStr(txtFromAccount.getText()),CommonUtil.convertObjToStr(txtToAccount.getText()));
        observable.ttNotifyObservers();
        cboProductId.setSelectedItem(productId); 
        if(tblLog.getRowCount() == 0){
            ClientUtil.showAlertWindow("No records");
            return;
        }                    

    }//GEN-LAST:event_btnClear1ActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        observable.resetTable();
        cboProductId.setSelectedItem("");
        tdtFromDate.setDateValue("");
        tdtToDate.setDateValue("");
        txtFromAccount.setText("");
        txtToAccount.setText("");
        btnProcess.setEnabled(false);
        btnClear.setEnabled(false);
        btnClear1.setEnabled(true);
        cboProductId.setEnabled(true);
        tdtFromDate.setEnabled(true);
        btnFromAccount.setEnabled(true);
        btnToAccount.setEnabled(true);
        tdtToDate.setDateValue(DateUtil.getStringDate(CurDate));
        setModified(false);
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        if(tdtFromDate.getDateValue().length()>0){
            observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());            
            Date fromDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtFromDate.getDateValue()));
            Date currDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(CurDate.clone()));
            if(fromDt!=null && DateUtil.dateDiff((Date)fromDt,(Date)currDt)>0){
                observable.resetTable();
                HashMap roiMap = new HashMap();
                roiMap.put("PROD_ID", ((ComboBoxModel) cboProductId.getModel()).getKeyForSelected());
                roiMap.put("FROM_DT",fromDt);
                List lst = ClientUtil.executeQuery("getSelectROIDate", roiMap);
                if(lst!=null && lst.size()>0){
                    roiMap = (HashMap)lst.get(0);
                    Date prodDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(roiMap.get("ROI_DATE")));
                    if(DateUtil.dateDiff(fromDt,prodDt)>0){
                        ClientUtil.showAlertWindow("From date should be greater than or equal to this date : "+DateUtil.getStringDate(prodDt));
                        return;
                    }else{
                        observable.resetTable();
                        fromDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtFromDate.getDateValue()));
                        String productId = CommonUtil.convertObjToStr(cboProductId.getSelectedItem().toString());
                        if(productId.equals("")){
                            btnFromAccount.setEnabled(false);
                            btnToAccount.setEnabled(false);
                        }
                        observable.displayingRecords(fromDt,((ComboBoxModel)cboProductId.getModel()).getKeyForSelected().toString(),
                        CommonUtil.convertObjToStr(txtFromAccount.getText()),CommonUtil.convertObjToStr(txtToAccount.getText()));
                        observable.ttNotifyObservers();
                        cboProductId.setSelectedItem(productId);
                        if(tblLog.getRowCount()>0){
                            btnProcess.setEnabled(true);
                            btnClear.setEnabled(true);
                            btnClear1.setEnabled(false);
                            cboProductId.setEnabled(false);
                            tdtFromDate.setEnabled(false);
                            btnFromAccount.setEnabled(false);
                            btnToAccount.setEnabled(false);
                        }else{
                            ClientUtil.showAlertWindow("No records to do recalculation");
                            return;
                        }
                    }
                }else{
                    tdtToDate.setDateValue(DateUtil.getStringDate(CurDate));
                    observable.resetTable();
                    fromDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtFromDate.getDateValue()));
                    String productId = CommonUtil.convertObjToStr(cboProductId.getSelectedItem().toString());
                    if(productId.equals("")){
                        btnFromAccount.setEnabled(false);
                        btnToAccount.setEnabled(false);
                    }
                    observable.displayingRecords(fromDt,((ComboBoxModel)cboProductId.getModel()).getKeyForSelected().toString(),
                    CommonUtil.convertObjToStr(txtFromAccount.getText()),CommonUtil.convertObjToStr(txtToAccount.getText()));
                    observable.ttNotifyObservers();
                    cboProductId.setSelectedItem(productId);
                    if(tblLog.getRowCount()>0){
                        btnProcess.setEnabled(true);
                        btnClear.setEnabled(true);
                        btnClear1.setEnabled(false);
                        cboProductId.setEnabled(false);
                        tdtFromDate.setEnabled(false);
                        btnFromAccount.setEnabled(false);
                        btnToAccount.setEnabled(false);
                    }else{
                        ClientUtil.showAlertWindow("No records to do recalculation");
                        return;
                    }
                }
            }else{
                ClientUtil.showAlertWindow("From date should be less than or equal to current date");
                return;
            }
        }else{
            ClientUtil.displayAlert("From date should not be empty");
            return;
        }
    }//GEN-LAST:event_btnViewActionPerformed
                       
    private void cboProductIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIdActionPerformed
        // TODO add your handling code here:
        btnProcess.setEnabled(false);
        btnFromAccount.setEnabled(true);
        btnToAccount.setEnabled(true);
    }//GEN-LAST:event_cboProductIdActionPerformed
    
    private void txtToAccountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToAccountFocusLost
        // TODO add your handling code here:
        final String MESSAGE = validateAccNo();
        if(!MESSAGE.equalsIgnoreCase("")){
            displayAlert(MESSAGE);
        }
    }//GEN-LAST:event_txtToAccountFocusLost
    
    private void txtFromAccountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromAccountFocusLost
        // TODO add your handling code here:
        final String MESSAGE = validateAccNo();
        if(!MESSAGE.equalsIgnoreCase("")){
            displayAlert(MESSAGE);
        }
    }//GEN-LAST:event_txtFromAccountFocusLost
    private String validateAccNo(){
        String from = txtFromAccount.getText();
        String to = txtToAccount.getText();
        String message = "";
        if(!(from.equalsIgnoreCase("")|| to.equalsIgnoreCase(""))){
            if(from.compareTo(to) > 0){
//                message = resourceBundle.getString("ACCOUNTWARNING");
            }
        }
//        if(((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString().equals("TL") || ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString().equals("AD"))
//        {
//            HashMap hash=new HashMap();
//         hash.put("PROD_ID",((ComboBoxModel)cboProductId.getModel()).getKeyForSelected());
//         hash.put("ACT_NUM",txtFromAccount.getText());
//         if(txtToAccount !=null && (! txtToAccount.getText().equals("")))
//             hash.put("ACT_NUM",txtToAccount.getText());
//         hash.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
//         List actlst=ClientUtil.executeQuery("getActNotCLOSEDTL",hash);
//         if(actlst != null &&  actlst.size()>0 ){}
//         else{
//             ClientUtil.displayAlert("Enter the current Number");
//             txtFromAccount.setText("");
//             txtToAccount.setText("");
//             return message;
//         }
//        }
        return message;
    }
    private void tdtFromDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFromDateFocusLost
        // TODO add your handling code here:
        if(tdtFromDate.getDateValue().length()>0){
            Date fromDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtFromDate.getDateValue()));
            Date currDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(CurDate.clone()));
            if(fromDt!=null && DateUtil.dateDiff((Date)fromDt,(Date)currDt)>0){
                HashMap roiMap = new HashMap();
                roiMap.put("PROD_ID", ((ComboBoxModel) cboProductId.getModel()).getKeyForSelected());
                roiMap.put("FROM_DT",fromDt);
                List lst = ClientUtil.executeQuery("getSelectROIDate", roiMap);
                if(lst!=null && lst.size()>0){
                    roiMap = (HashMap)lst.get(0);
                    Date prodDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(roiMap.get("ROI_DATE")));
                    if(DateUtil.dateDiff(fromDt,prodDt)>0){
                        ClientUtil.showAlertWindow("From date should be greater than or equal to this date : "+DateUtil.getStringDate(prodDt));
                        return;
                    }else{
                        tdtToDate.setDateValue(DateUtil.getStringDate(CurDate));
                    }
                }
            }else{
                ClientUtil.showAlertWindow("From date should be less than or equal to current date : ");
                return;
            }
        }else{
            ClientUtil.validateFromDate(tdtFromDate, tdtToDate.getDateValue());
        }
    }//GEN-LAST:event_tdtFromDateFocusLost
    
    private void tdtToDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtToDateFocusLost
        // TODO add your handling code here:
        ClientUtil.validateToDate(tdtToDate, tdtFromDate.getDateValue());
    }//GEN-LAST:event_tdtToDateFocusLost
    
    private void btnToAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToAccountActionPerformed
        // TODO add your handling code here:
        popUp(TO);
    }//GEN-LAST:event_btnToAccountActionPerformed
    
    private void btnFromAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFromAccountActionPerformed
        // TODO add your handling code here:
        popUp(FROM);
    }//GEN-LAST:event_btnFromAccountActionPerformed
    private void popUp(int field) {
        Date fromDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtFromDate.getDateValue()));
        if(fromDt != null){
            final HashMap viewMap = new HashMap();
            viewType = field;
            HashMap hash = new HashMap();
            String prodType = "TD";
            hash.put("DEPOSIT_DT", fromDt);
            if(prodType.equals("TD")){
                viewMap.put(CommonConstants.MAP_NAME, "TDRecalcuationInterestList");
            }
            hash.put("PROD_ID", ((ComboBoxModel)cboProductId.getModel()).getKeyForSelected());
            hash.put(CommonConstants.BRANCH_ID, com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
            if(viewType==TO){
                hash.put("ACCT_NO", txtFromAccount.getText());
            }
            viewMap.put(CommonConstants.MAP_WHERE, hash);

            new ViewAll(this, viewMap).show();
        }else{
            ClientUtil.showAlertWindow("Choose from date");
            return;
        }            
    }
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        System.out.println("Hash: " + hash);
        String alert = "";
        String prodType = "TD";
        if(prodType.equals("TD")) {
            if (viewType==TO){
                txtToAccount.setText(CommonUtil.convertObjToStr(hash.get("DEPOSIT NUMBER")));
                alert = validateAccNo();
            }else if(viewType==FROM){
                txtFromAccount.setText(CommonUtil.convertObjToStr(hash.get("DEPOSIT NUMBER")));
                alert = validateAccNo();
            }
        }
        if(!alert.equalsIgnoreCase("")){
            displayAlert(alert);
        }
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        HashMap reportParamMap = new HashMap();
        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed
    
    public long getNearest(long number,long roundingFactor)  {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor%2) != 0)
            roundingFactorOdd +=1;
        long mod = number%roundingFactor;
        if ((mod < (roundingFactor/2)) || (mod < (roundingFactorOdd/2))){
            return lower(number,roundingFactor);
        }else{
            return higher(number,roundingFactor);
        }
    }    
    
    public long lower(long number,long roundingFactor) {
        long mod = number%roundingFactor;
        return number-mod;
    }

    public long higher(long number,long roundingFactor) {
        long mod = number%roundingFactor;
        if ( mod == 0){
            return number;
        }
        return (number-mod) + roundingFactor ;
    }

    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
        // TODO add your handling code here:        
        String[] obj ={"Yes","No"};
        int option =COptionPane.showOptionDialog(null,("Do you want to proceed with recalculation ?"), ("Select The Desired Option"),
        COptionPane.YES_NO_CANCEL_OPTION,COptionPane.QUESTION_MESSAGE,null,obj,obj[0]);
        if(option == 0 && tblLog.getRowCount()>0){
            InterestCalc interestCalc = new InterestCalc();
            try{
                HashMap executeMap = new HashMap();
                HashMap updateMap = new HashMap();
                ArrayList addingList = new ArrayList();
                for(int i=0;i<tblLog.getRowCount();i++){
                    HashMap recalculateMap = new HashMap();
                    btnView.setEnabled(false);
                    btnClear.setEnabled(false);
                    HashMap acctListMap = new HashMap();
                    double oldRoi = CommonUtil.convertObjToDouble(tblLog.getValueAt(i, 4)).doubleValue();
                    double newRoi = CommonUtil.convertObjToDouble(tblLog.getValueAt(i, 5)).doubleValue();
                    double depAmt = CommonUtil.convertObjToDouble(tblLog.getValueAt(i, 4)).doubleValue();
                    double matAmt = CommonUtil.convertObjToDouble(tblLog.getValueAt(i, 5)).doubleValue();
                    Date depDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tblLog.getValueAt(i, 3)));
                    acctListMap.put("DEPOSIT_NO",CommonUtil.convertObjToStr(tblLog.getValueAt(i, 0)));
                    List lst = ClientUtil.executeQuery("getSelectRecalculateDetails", acctListMap);
                    if(lst!=null && lst.size()>0){
                        acctListMap = (HashMap)lst.get(0);
                        recalculateMap.put("CUST_ID", acctListMap.get("CUST_ID"));
                        recalculateMap.put("DEPOSIT_NO",CommonUtil.convertObjToStr(tblLog.getValueAt(i, 0)));
                        recalculateMap.put("CATEGORY_ID",CommonUtil.convertObjToStr(tblLog.getValueAt(i, 2)));
                        recalculateMap.put("OLD_ROI",new Double(oldRoi));
                        recalculateMap.put("NEW_ROI",new Double(newRoi));
                        recalculateMap.put("OLD_TOT_INT_AMT",acctListMap.get("TOT_INT_AMT"));
                        recalculateMap.put("OLD_MATURITY_AMT",acctListMap.get("MATURITY_AMT"));
                        recalculateMap.put("CREATED_BY",ProxyParameters.USER_ID);
                        recalculateMap.put("RECALCULATED_DATE",CurDate.clone());
                        long year = CommonUtil.convertObjToLong(acctListMap.get("DEPOSIT_PERIOD_YY"));
                        long month = CommonUtil.convertObjToLong(acctListMap.get("DEPOSIT_PERIOD_MM"));
                        long day = CommonUtil.convertObjToLong(acctListMap.get("DEPOSIT_PERIOD_DD"));
                        if(acctListMap.get(BEHAVES_LIKE).equals(FIXED)){
                            acctListMap.put("PERIOD_YEARS", new Double(year));
                            acctListMap.put("PERIOD_MONTHS",new Double(month));
                            acctListMap.put("PERIOD_DAYS",new Double(day));
                        }
                        if(year>0){
                            year = year * 360;
                        }
                        if(month>0){
                            month = month * 30;
                        }
                        long period = year + month + day;
                        HashMap depositMap = new HashMap();
                        depositMap.put("DEPOSIT_NO",CommonUtil.convertObjToStr(tblLog.getValueAt(i, 0)));
                        List lstAmt = ClientUtil.executeQuery("getauthorizeByDeposit", depositMap);
                        if(lstAmt!=null && lstAmt.size()>0){
                            depositMap = (HashMap)lstAmt.get(0);
                        }
                        acctListMap.put("PEROID", new Long(period));
                        acctListMap.put("ROI",new Double(newRoi));
                        acctListMap.put("AMOUNT",depositMap.get("DEPOSIT_AMT"));
                        HashMap amtDetHash = interestCalc.calcAmtDetails(acctListMap);
                        double intAmt = CommonUtil.convertObjToDouble(amtDetHash.get("INTEREST")).doubleValue();
                        double completeAmt = CommonUtil.convertObjToDouble(amtDetHash.get("AMOUNT")).doubleValue();
                        intAmt = (double)getNearest((long)(intAmt*100),100)/100;
                        completeAmt = (double)getNearest((long)(completeAmt*100),100)/100;
                        double balance = completeAmt - intAmt;
                        recalculateMap.put("NEW_TOT_INT_AMT",new Double(intAmt));
                        if(acctListMap.get(BEHAVES_LIKE).equals(FIXED)){
                            recalculateMap.put("NEW_MATURITY_AMT",new Double(balance));
                        }else if(acctListMap.get(BEHAVES_LIKE).equals(CUMMULATIVE)){
                            recalculateMap.put("NEW_MATURITY_AMT",new Double(completeAmt));
                        }else if(acctListMap.get(BEHAVES_LIKE).equals(RECURRING)){
                            recalculateMap.put("NEW_MATURITY_AMT",new Double(completeAmt));
                        }
                        recalculateMap.put("PROD_ID",acctListMap.get("PROD_ID"));
                        recalculateMap.put("DEPOSIT_DT",acctListMap.get("DEPOSIT_DT"));
                        recalculateMap.put("BRANCH_CODE",ProxyParameters.BRANCH_ID);
                        addingList.add(i,recalculateMap);
                        acctListMap = null;
                        recalculateMap = null;
                        amtDetHash = null;
                    }
                    btnView.setEnabled(true);
                    btnClear.setEnabled(true);
                    btnClear1.setEnabled(true);
                    btnProcess.setEnabled(false);
                }
                executeMap.put("RECALCULATE_MAP",addingList);
                observable.doAction(executeMap);
            }catch (Exception e){
                System.out.println("######"+e);
                e.printStackTrace();
            }
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
        
    }//GEN-LAST:event_btnProcessActionPerformed
    //__ To Update the Data regarding the Execution of the Selected Task(s) in the Table
    private void updateTable(){
        tblLog.setModel(observable.getTblLog());
        observable.setCboProductId(CommonUtil.convertObjToStr(cboProductId.getSelectedItem()));
    }
    
    public void update(java.util.Observable o, Object arg) {
        cboProductId.setSelectedItem(observable.getCboProductId());
        this.tskStatus = observable.getTaskStatus();
        this.tskHeader = observable.getTaskHeader();
        tblLog.setModel(observable.getTblLog());
    }
    
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
        
    public static void main(String str[]) throws Exception{
//        javax.swing.JFrame frm = new javax.swing.JFrame();
//        ChargesUI db = new ChargesUI();
//        frm.getContentPane().add(db);
//        frm.show();
//        db.show();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnClear1;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnFromAccount;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CButton btnToAccount;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboProductId;
    private com.see.truetransact.uicomponent.CLabel lblFromAccount;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblProductId;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToAccount;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
    private com.see.truetransact.uicomponent.CPanel panChargesApplication;
    private com.see.truetransact.uicomponent.CPanel panProductId;
    private com.see.truetransact.uicomponent.CPanel panProductId1;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CButtonGroup rdoTaskType;
    private com.see.truetransact.uicomponent.CButtonGroup rdoTaskType1;
    private com.see.truetransact.uicomponent.CScrollPane scrOutputMsg;
    private com.see.truetransact.uicomponent.CTable tblLog;
    private javax.swing.JToolBar tbrHead;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    private com.see.truetransact.uicomponent.CDateField tdtToDate;
    private com.see.truetransact.uicomponent.CTextField txtFromAccount;
    private com.see.truetransact.uicomponent.CTextField txtToAccount;
    // End of variables declaration//GEN-END:variables
    
}