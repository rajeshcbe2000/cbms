/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * Authorize.java
 *
 * Created on March 3, 2004, 1:46 PM
 */
package com.see.truetransact.ui.supporting.unlockaccounts;

import com.see.truetransact.ui.termloan.goldstockrelease.*;
import com.see.truetransact.ui.termloan.kcc.multiplerenewal.*;
import com.see.truetransact.ui.termloan.arbitration.*;
import com.see.truetransact.ui.termloan.notices.*;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Observable;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.event.ListSelectionListener;
//import javax.swing.DefaultListModel;

import org.apache.log4j.Logger;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.EnhancedComboBoxModel;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.*;
import com.see.truetransact.ui.common.viewall.ViewAll;

import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.ui.TrueTransactMain;
import java.awt.*;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * @author balachandar
 */
public class UnlockAccountsUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, ListSelectionListener {
//    private final AuthorizeRB resourceBundle = new AuthorizeRB();

    private UnlockAccountsOB observable;
    HashMap paramMap = null;
    CInternalFrame parent = null;
    javax.swing.JList lstSearch;
    java.util.ArrayList arrLst = new java.util.ArrayList();
    Date currDt = null;
    TTIntegration ttIntegration = null;
    int previousRow = -1;
    HashMap accountNumberMap = null;
    HashMap guarantorMemberMap = null;
    HashMap accountChargeMap = null;
    HashMap guarantorChargeMap = null;
    String bankName = "";
    boolean generateNotice = false;
    private String viewType = "";
    int FROMACTNO = 1, TOACTNO = 2;
    private final static Logger log = Logger.getLogger(UnlockAccountsUI.class);
    boolean isEdit = false;
    ArrayList dueColourList = new ArrayList();
    ArrayList suretyColourList = new ArrayList();
    ArrayList riskFundLimitList =  new ArrayList();
    ArrayList deathMarkedCustomerList = new ArrayList();
    int releaseRow;
    ArrayList colorList = new ArrayList();
    ArrayList mdsColorList = new ArrayList();
    /**
     * Creates new form AuthorizeUI
     */
    public UnlockAccountsUI() {
        setupInit();
        setupScreen();
    }

    /**
     * Creates new form AuthorizeUI
     */
    public UnlockAccountsUI(CInternalFrame parent, HashMap paramMap) {
        this.parent = parent;
        this.paramMap = paramMap;
        setupInit();
        setupScreen();
    }

    private void setupInit() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        internationalize();
        setObservable();
        bankName = ((String) TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase();
        txtAcctNo.setEnabled(false);
        txtAcctNo.setEditable(false);
    }

    private void setupScreen() {
//        setModal(true);

        /*
         * Calculate the screen size
         */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        /*
         * Center frame on the screen
         */
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    private void setObservable() {
        try {
            observable = new UnlockAccountsOB();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void populateData(HashMap mapID) {
        try {
            log.info("populateData...");
            ArrayList heading = observable.populateData(mapID, tblData);
            if (heading != null && heading.size() > 0) {
                EnhancedComboBoxModel cboModel = new EnhancedComboBoxModel(heading);
//                cboSearchCol.setModel(cboModel);
            }
        } catch (Exception e) {
            System.err.println("Exception " + e.toString() + "Caught");
            e.printStackTrace();
        }
    }

//    public void show() {
//        if (observable.isAvailable()) {
//            super.show();
//        } else {
//            if (parent != null) parent.setModified(false);
//            ClientUtil.noDataAlert();
//        }
//    }
//    public void setVisible(boolean visible) {
//        if (observable.isAvailable()) {
//            super.setVisible(visible);
//        }
//    }
    /**
     * Bring up and populate the temporary project detail screen.
     */
    private void whenTableRowSelected() {
        int rowIndexSelected = tblData.getSelectedRow();
        if (previousRow != -1) {
            if (((Boolean) tblData.getValueAt(previousRow, 0)).booleanValue()) {
                //    int guarantorRowIndexSelected = tblGuarantorData.getSelectedRow();
                if (accountNumberMap == null) {
                    accountNumberMap = new HashMap();
                }
                if (guarantorMemberMap == null) {
                    guarantorMemberMap = new HashMap();
                }
                if (previousRow != -1 && previousRow != rowIndexSelected) {
                    // isSelectedRowTicked(tblGuarantorData);
                }
            } else {
                // observable.setSelectAll(tblGuarantorData, new Boolean(false));
            }
        }
        //Changed By Suresh
        // String prodType = String.valueOf(cboProdType.getSelectedItem());

        previousRow = rowIndexSelected;
        calTotalAmount();
    }

    private void whenGuarantorTableRowSelected() {
        int rowIndexSelected = tblData.getSelectedRow();
        if (!((Boolean) tblData.getValueAt(rowIndexSelected, 0)).booleanValue()) {
//            if (isSelectedRowTicked(tblGuarantorData)) {
//                ClientUtil.displayAlert("Loanee Record not selected...");
//                observable.setSelectAll(tblGuarantorData, new Boolean(false));
//            }
        }
    }

    private boolean isSelectedRowTicked(com.see.truetransact.uicomponent.CTable table) {
        boolean selected = false;
        for (int i = 0, j = table.getRowCount(); i < j; i++) {
            selected = ((Boolean) table.getValueAt(i, 0)).booleanValue();
            if (selected) {
                //                if (isGuarantor) {
                //                    guarantorMemberMap.put(table.getValueAt(i, 3),null);
                //                } else {
                //                    accountNumberMap.put(table.getValueAt(i, 1),null);
                //                }
                break;
            }
        }
        return selected;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdbArbit = new com.see.truetransact.uicomponent.CButtonGroup();
        cTabbedPane1 = new com.see.truetransact.uicomponent.CTabbedPane();
        panUnlock = new com.see.truetransact.uicomponent.CPanel();
        lblSelectedRecords = new com.see.truetransact.uicomponent.CLabel();
        panSearchCondition = new com.see.truetransact.uicomponent.CPanel();
        panMultiSearch3 = new com.see.truetransact.uicomponent.CPanel();
        panArbit2 = new com.see.truetransact.uicomponent.CPanel();
        lblClosedLoanActNo = new com.see.truetransact.uicomponent.CLabel();
        btnShowClosedLoans = new com.see.truetransact.uicomponent.CButton();
        btnClosedLoanSearch = new com.see.truetransact.uicomponent.CButton();
        txtAcctNo = new com.see.truetransact.uicomponent.CTextField();
        lblProdDescription1 = new com.see.truetransact.uicomponent.CLabel();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        srcTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();
        lblNoOfRecords = new com.see.truetransact.uicomponent.CLabel();
        lblNoOfRecordsVal = new com.see.truetransact.uicomponent.CLabel();
        panSearch = new com.see.truetransact.uicomponent.CPanel();
        btnLoanRelease = new com.see.truetransact.uicomponent.CButton();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnClear1 = new com.see.truetransact.uicomponent.CButton();
        sptLine = new com.see.truetransact.uicomponent.CSeparator();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Gold Stock Release");
        setMinimumSize(new java.awt.Dimension(800, 630));
        setPreferredSize(new java.awt.Dimension(800, 630));

        panUnlock.setMinimumSize(new java.awt.Dimension(800, 35));
        panUnlock.setPreferredSize(new java.awt.Dimension(800, 35));

        panSearchCondition.setMinimumSize(new java.awt.Dimension(574, 150));
        panSearchCondition.setPreferredSize(new java.awt.Dimension(574, 150));

        panMultiSearch3.setBorder(javax.swing.BorderFactory.createTitledBorder("Account Details"));
        panMultiSearch3.setMaximumSize(new java.awt.Dimension(450, 150));
        panMultiSearch3.setMinimumSize(new java.awt.Dimension(450, 150));

        panArbit2.setMinimumSize(new java.awt.Dimension(400, 65));

        lblClosedLoanActNo.setText("Account No");

        btnShowClosedLoans.setText("Show");
        btnShowClosedLoans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowClosedLoansActionPerformed(evt);
            }
        });

        btnClosedLoanSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnClosedLoanSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClosedLoanSearchActionPerformed(evt);
            }
        });

        txtAcctNo.setAllowAll(true);

        javax.swing.GroupLayout panArbit2Layout = new javax.swing.GroupLayout(panArbit2);
        panArbit2.setLayout(panArbit2Layout);
        panArbit2Layout.setHorizontalGroup(
            panArbit2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panArbit2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblClosedLoanActNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtAcctNo, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnClosedLoanSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnShowClosedLoans, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblProdDescription1, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );
        panArbit2Layout.setVerticalGroup(
            panArbit2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panArbit2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panArbit2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnClosedLoanSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(panArbit2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnShowClosedLoans, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtAcctNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblClosedLoanActNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblProdDescription1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(27, 27, 27))
        );

        javax.swing.GroupLayout panMultiSearch3Layout = new javax.swing.GroupLayout(panMultiSearch3);
        panMultiSearch3.setLayout(panMultiSearch3Layout);
        panMultiSearch3Layout.setHorizontalGroup(
            panMultiSearch3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panMultiSearch3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panArbit2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        panMultiSearch3Layout.setVerticalGroup(
            panMultiSearch3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panMultiSearch3Layout.createSequentialGroup()
                .addComponent(panArbit2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panSearchConditionLayout = new javax.swing.GroupLayout(panSearchCondition);
        panSearchCondition.setLayout(panSearchConditionLayout);
        panSearchConditionLayout.setHorizontalGroup(
            panSearchConditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSearchConditionLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(panMultiSearch3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );
        panSearchConditionLayout.setVerticalGroup(
            panSearchConditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSearchConditionLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(panMultiSearch3, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        panTable.setMinimumSize(new java.awt.Dimension(600, 350));
        panTable.setPreferredSize(new java.awt.Dimension(600, 350));
        panTable.setLayout(new java.awt.GridBagLayout());

        srcTable.setViewport(srcTable.getRowHeader());

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
        tblData.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblData.setMinimumSize(new java.awt.Dimension(350, 80));
        tblData.setPreferredScrollableViewportSize(new java.awt.Dimension(804, 296));
        tblData.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tblDataMouseMoved(evt);
            }
        });
        tblData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDataMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDataMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblDataMouseReleased(evt);
            }
        });
        srcTable.setViewportView(tblData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTable.add(srcTable, gridBagConstraints);

        lblNoOfRecords.setText("No. of Records Found : ");
        lblNoOfRecords.setMaximumSize(new java.awt.Dimension(140, 18));
        lblNoOfRecords.setMinimumSize(new java.awt.Dimension(140, 18));
        lblNoOfRecords.setPreferredSize(new java.awt.Dimension(140, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTable.add(lblNoOfRecords, gridBagConstraints);

        lblNoOfRecordsVal.setMaximumSize(new java.awt.Dimension(230, 85));
        lblNoOfRecordsVal.setMinimumSize(new java.awt.Dimension(80, 18));
        lblNoOfRecordsVal.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTable.add(lblNoOfRecordsVal, gridBagConstraints);

        panSearch.setLayout(new java.awt.GridBagLayout());

        btnLoanRelease.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnLoanRelease.setText("Unlock");
        btnLoanRelease.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoanReleaseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        panSearch.add(btnLoanRelease, gridBagConstraints);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        panSearch.add(btnClose, gridBagConstraints);

        btnClear1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnClear1.setText("Clear");
        btnClear1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClear1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(btnClear1, gridBagConstraints);

        sptLine.setMinimumSize(new java.awt.Dimension(2, 2));
        sptLine.setPreferredSize(new java.awt.Dimension(2, 2));

        javax.swing.GroupLayout panUnlockLayout = new javax.swing.GroupLayout(panUnlock);
        panUnlock.setLayout(panUnlockLayout);
        panUnlockLayout.setHorizontalGroup(
            panUnlockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panUnlockLayout.createSequentialGroup()
                .addGap(145, 145, 145)
                .addComponent(lblSelectedRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(562, 562, 562))
            .addGroup(panUnlockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panUnlockLayout.createSequentialGroup()
                    .addGap(30, 30, 30)
                    .addGroup(panUnlockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panSearchCondition, javax.swing.GroupLayout.PREFERRED_SIZE, 739, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panUnlockLayout.createSequentialGroup()
                            .addGap(4, 4, 4)
                            .addGroup(panUnlockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(sptLine, javax.swing.GroupLayout.PREFERRED_SIZE, 731, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(panTable, javax.swing.GroupLayout.PREFERRED_SIZE, 731, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(panSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 731, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addContainerGap(31, Short.MAX_VALUE)))
        );
        panUnlockLayout.setVerticalGroup(
            panUnlockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panUnlockLayout.createSequentialGroup()
                .addContainerGap(493, Short.MAX_VALUE)
                .addComponent(lblSelectedRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58))
            .addGroup(panUnlockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panUnlockLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(panSearchCondition, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(sptLine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(8, 8, 8)
                    .addComponent(panTable, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(43, 43, 43)
                    .addComponent(panSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(10, Short.MAX_VALUE)))
        );

        cTabbedPane1.addTab("Unlock Accounts", panUnlock);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(cTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 774, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        cTabbedPane1.getAccessibleContext().setAccessibleName("Loans");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void setVisibleForAuctionNotice(boolean val) {
//        lblAuctionDate.setVisible(val);
//        tdtAuctionDate.setVisible(val);
    }

    private void setVisibleForDemandNotice(boolean val) {
//        lblOverDueDate.setVisible(val);
//        tdtOverDueDate.setVisible(val);
//        chkFulldue.setVisible(val);
//        lblNoOfInstallments.setVisible(val);
//   
    }

    private void btnClear1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClear1ActionPerformed
        // TODO add your handling code here:
        ClientUtil.clearAll(panUnlock);
        isEdit = false;
        btnLoanRelease.setEnabled(true);
        lblNoOfRecordsVal.setText("");
    }//GEN-LAST:event_btnClear1ActionPerformed

    private void tblDataMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseReleased
        // TODO add your handling code here:
        if (/*
                 * (evt.getClickCount() == 2) &&
                 */(evt.getModifiers() == 16)) {
            whenTableRowSelected();
            setSelectedRecord();
        }
    }//GEN-LAST:event_tblDataMouseReleased
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        System.out.println("Hash: " + hash);
        /*
        Hash: {ACCT_NUM=0001259000057, STOCK= Net Weight : 95   Gross Weight : 101  
        Stock Details :SARIMALA-1,KAICHAIN-1,THADAVALA-1}
        */
        if (viewType.equalsIgnoreCase("LOCKED_ACCTS")) {
          txtAcctNo.setText(CommonUtil.convertObjToStr(hash.get("RECORD_KEY")));
        }
    }

    private void popUp(String field) {
        final HashMap viewMap = new HashMap();
        if (field.equalsIgnoreCase("LOCKED_ACCTS")) {
            HashMap checkMap = new HashMap();
            checkMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            checkMap.put("CURR_DT", currDt);
            viewMap.put(CommonConstants.MAP_WHERE, checkMap);
            viewMap.put(CommonConstants.MAP_NAME, "getAllLockedAccounts");
        } 
        new ViewAll(this, viewMap).show();
    }

    private void tblDataMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseMoved
        Point p = evt.getPoint();
        String tip =
                String.valueOf(
                tblData.getModel().getValueAt(
                tblData.rowAtPoint(p),
                tblData.columnAtPoint(p)));
        tblData.setToolTipText(tip);
    }//GEN-LAST:event_tblDataMouseMoved

    private void tblDataMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMousePressed
//        if ((evt.getClickCount() == 2) && (evt.getModifiers() == 16)) {
//            HashMap whereMap = new HashMap();
//            whereMap.put("ACT_NUM", tblData.getValueAt(tblData.getSelectedRow(), 1));
//
//            TableDialogUI tableData = new TableDialogUI("getNoticeChargeDetails", whereMap);
//            tableData.setTitle("Notice Sent Details for " + tblData.getValueAt(tblData.getSelectedRow(), 1));
//            tableData.setPreferredSize(new Dimension(750, 450));
//            tableData.show();
//        }       
    }//GEN-LAST:event_tblDataMousePressed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        observable.removeRowsFromGuarantorTable(tblData);
        //  observable.removeRowsFromGuarantorTable(tblGuarantorData);
        dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    //Added By Suresh
    private void setSelectedRecord() {
        int count = 0;
        if (tblData.getRowCount() > 0) {
            for (int i = 0, j = tblData.getRowCount(); i < j; i++) {
                if (((Boolean) tblData.getValueAt(i, 0)).booleanValue()) {
                    count += 1;
                }
            }
        }
        //   lblSelectedRecordVal.setText(String.valueOf(count));
        calculateTotalApplCharges();
    }

    private void calculateTotalApplCharges() {
//        double totalChargeAmount = (CommonUtil.convertObjToDouble(txtNoticeCharge.getText())
//                + CommonUtil.convertObjToDouble(txtPostageCharge.getText()));
//        lblTotalChargeAmountVal.setText(String.valueOf(totalChargeAmount * CommonUtil.convertObjToDouble(lblSelectedRecordVal.getText())));
    }

    public void populateEditData() {
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        whereMap.put("CURR_DATE", getProperDate(currDt));
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        viewMap.put(CommonConstants.MAP_NAME, "getAwardViewDetails");
        new ViewAll(this, viewMap).show();

    }

    public void populateData() {
        String behavesLike = "";
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        boolean isOK = true;
        System.out.println("chkng");
        if (isOK) {
          

            whereMap.put("TODAY_DT", getProperDate(currDt));
            whereMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            whereMap.put("CURR_DATE", getProperDate(currDt));
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            try {
                ArrayList heading = observable.populateData(viewMap, tblData);
                setColorLists();
                setColour();
                lblNoOfRecordsVal.setText(String.valueOf(tblData.getRowCount()));
                heading = null;
            } catch (Exception e) {
                System.err.println("Exception " + e.toString() + "Caught");
                e.printStackTrace();
            }
        }
        viewMap = null;
        whereMap = null;
    }
    
    
    
     private void setReleaseColour() {
        /* Set a cellrenderer to this table in order format the date */
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (colorList.contains(String.valueOf(row))){
                    setForeground(Color.RED);
                } else {
                    setForeground(Color.BLACK);
                }
                // Set oquae
                this.setOpaque(true);
                return this;
            }
        };
        tblData.setDefaultRenderer(Object.class, renderer);
    }
     
     
   
     
    
   

    public void populateLockedAccountData(List accountList) {
        String behavesLike = "";
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        boolean isOK = true;
        System.out.println("chkng");
        if (isOK) {
            viewMap.put(CommonConstants.MAP_NAME, "getAllLockedAccounts");
            if (txtAcctNo.getText().length() > 0) {
                whereMap.put("RECORD_KEY", txtAcctNo.getText());
            }
            whereMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            whereMap.put("CURR_DATE", getProperDate(currDt));
            whereMap.put("CURR_DT", getProperDate(currDt));
            whereMap.put("ACCT_LIST", accountList);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            try {
                ArrayList heading = observable.populateLockedAccountData(viewMap, tblData);
                heading = null;
            } catch (Exception e) {
                System.err.println("Exception " + e.toString() + "Caught");
                e.printStackTrace();
            }
        }
        viewMap = null;
        whereMap = null;
    }
    
    

    private void setColour() {
        /*
         * Set a cellrenderer to this table in order format the date
         */
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (deathMarkedCustomerList.contains(String.valueOf(row))) {
                    setForeground(Color.RED);
                } else if (suretyColourList.contains(String.valueOf(row))) {
                    setForeground(Color.BLUE);
                } else {
                    setForeground(Color.BLACK);
                }
                // Set oquae
                this.setOpaque(true);
                return this;
            }
        };
        tblData.setDefaultRenderer(Object.class, renderer);
    }

//     private String validateAccNo(){
//        String from = txtFromAccountno.getText();
//        String to = txtTOAccountno.getText();
//        String message = "";
//        if(!(from.equalsIgnoreCase("")|| to.equalsIgnoreCase(""))){
//            if(from.compareTo(to) > 0){
////                message = resourceBundle.getString("ACCOUNTWARNING");
//            }
//        }
//        if(((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString().equals("TL") || ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString().equals("AD"))
//        {
//            HashMap hash=new HashMap();
//         hash.put(PROD_ID,((ComboBoxModel)cboProdId.getModel()).getKeyForSelected());
//         hash.put("ACT_NUM",txtFromAccount.getText());
//         if(txtTOAccountno !=null && (! txtTOAccountno.getText().equals(""))) {
//             hash.put("ACT_NUM",txtTOAccountno.getText());
//         }
//         hash.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
//         List actlst=ClientUtil.executeQuery("getActNotCLOSEDTL",hash);
//         if(actlst != null &&  actlst.size()>0 ){
//         } else{
//             ClientUtil.displayAlert("Enter the correct Number");
//             txtFromAccountno.setText("");
//             txtTOAccountno.setText("");
//             return message;
//         }
//        }
//        return message;
//    }
    public void generateNotice() {
        //        updateOBFields();
    }

    private void setColorLists() {
        suretyColourList = new ArrayList();
        dueColourList = new ArrayList();
        deathMarkedCustomerList = new ArrayList();
        if (tblData.getRowCount() > 0) {
            for (int i = 0; i < tblData.getRowCount(); i++) {
                double limit = CommonUtil.convertObjToDouble(tblData.getValueAt(i, 5).toString()).doubleValue();
                double suretyAmt = CommonUtil.convertObjToDouble(tblData.getValueAt(i, 7).toString()).doubleValue();
                if (suretyAmt < limit) {
                    suretyColourList.add(String.valueOf(i));
                }
                
                String acctNum = CommonUtil.convertObjToStr(tblData.getValueAt(i, 1).toString());
                HashMap actMap =  new HashMap();
                actMap.put("ACCOUNTNO",acctNum);
                List deathLsit = ClientUtil.executeQuery("getDeathMarkedCustomerAD", actMap);
                if(deathLsit.size() > 0){
                    deathMarkedCustomerList.add(String.valueOf(i));
                }
            }
        }
    }
    
 

    public String guarantorGetSelected() {
//        Boolean bln;
//        ArrayList arrRow;
//        HashMap selectedMap;
        //        ArrayList selectedList = new ArrayList();
        String selected = "";
        Object obj[] = guarantorMemberMap.keySet().toArray();
        for (int i = 0, j = obj.length; i < j; i++) {
            selected += "'" + obj[i];
            selected += "',";
        }
        // If no guarantor selected also records should be selected from other than guarantor.
        selected = selected.equals("") ? "'aa'" : selected.substring(0, selected.length() - 1);
        System.out.println("#$#$ guaranter selected : " + selected);
        return selected;
    }

    private void createCboNoticeType() {
//      
    }

    private Date getProperDate(Date sourceDate) {
        Date targetDate = (Date) currDt.clone();
        targetDate.setDate(sourceDate.getDate());
        targetDate.setMonth(sourceDate.getMonth());
        targetDate.setYear(sourceDate.getYear());
        return targetDate;
    }

    private ComboBoxModel getListModel() {
        ComboBoxModel listData = new ComboBoxModel();
        return listData;
    }

    public void valueChanged(javax.swing.event.ListSelectionEvent e) {
        // Add your handling code here://GEN-FIRST:event_btnCancelActionPerformed
//        if ( < 0) {
//            COptionPane.showMessageDialog(null,
//                resourceBundle.getString("SelectRow"), 
//                resourceBundle.getString("SelectRowHeading"),
//                COptionPane.OK_OPTION + COptionPane.INFORMATION_MESSAGE);
//        } 
//        if (parent != null) parent.setModified(false);
//        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void updateDBStatus(String status) {
        //observable.updateStatus(paramMap, status);
        HashMap screenParamMap = new HashMap();
        screenParamMap.put(CommonConstants.AUTHORIZEDATA, observable.getSelected());
        screenParamMap.put(CommonConstants.AUTHORIZESTATUS, status);

//        observable.insertCharges(paramMap);
        ClientUtil.showMessageWindow(observable.getResult());
        if (observable.getResult().equals(ClientConstants.RESULT_STATUS[4])) {
            observable.removeRowsFromGuarantorTable(tblData);
            lblNoOfRecordsVal.setText("");
            //observable.removeRowsFromGuarantorTable(tblGuarantorData);
        }

    }

    private void btnLoanReleaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoanReleaseActionPerformed
        // Add your handling code here:
//        int row = tblData.getSelectedRow();
//       
//            if (CommonUtil.convertObjToStr(tblData.getValueAt(row, 6)).equals("Released")) {
//                ClientUtil.showMessageWindow("Gold stock already released !!!");
//            } else {
//              try {
//                int n = ClientUtil.confirmationAlert("Are you sure you want to release gold stock of Loan No :" + CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(), 0)), 1);
//                if (n == 0) {
//
//                    HashMap map = new HashMap();
//                    map.put("RELEASE_DT", currDt.clone());
//                    map.put("RELEASE_BY", TrueTransactMain.USER_ID);
//                    map.put("ACCT_NUM", CommonUtil.convertObjToStr(tblData.getValueAt(row, 0)));
//                    ClientUtil.execute("updateGoldStockReleaseStatus", map);
//                    tblData.setValueAt("Released", row, 6);
//                    releaseRow = row;
//                    setReleaseColour();
//                    ClientUtil.showMessageWindow("Gold Stock Released !!!");
//                }
//            }catch(Exception e){
//            
//        }
//        }

        final int row = tblData.getSelectedRow();
        if (CommonUtil.convertObjToStr(tblData.getValueAt(row, 3)).equals("Unlocked")) {
            ClientUtil.showMessageWindow("Account already unlocked !!!");
        } else {
            CommonUtil comm = new CommonUtil();
            final JDialog loading = comm.addProgressBar();
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() throws InterruptedException /**
                 * Execute some operation
                 */
                {
                    try {
                        int n = ClientUtil.confirmationAlert("Are you sure you want to unlock Account No :" + CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(), 0)), 1);
                        if (n == 0) {
                            HashMap map = new HashMap();
                            map.put("CUR_DATE", currDt.clone());
                            map.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
                            map.put("RECORD_KEY", CommonUtil.convertObjToStr(tblData.getValueAt(row, 0)));
                            ClientUtil.execute("deleteLock", map);
                            tblData.setValueAt("Unlocked", row, 3);
                            releaseRow = row;
                            colorList.add(String.valueOf(row));
                            setReleaseColour();
                            ClientUtil.showMessageWindow("Account unlocked !!!");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void done() {
                    loading.dispose();
                }
            };
            worker.execute();
            loading.show();
            try {
                worker.get();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }

    }//GEN-LAST:event_btnLoanReleaseActionPerformed

    

    private void tblDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseClicked
        // TODO add your handling code here:
       
        
    }//GEN-LAST:event_tblDataMouseClicked

    private void btnShowClosedLoansActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowClosedLoansActionPerformed
        // TODO add your handling code here:
        HashMap whereMap = new HashMap();
        if (txtAcctNo.getText().length() > 0) {
            whereMap.put("RECORD_KEY", txtAcctNo.getText());
        }
        whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        whereMap.put("CURR_DT", getProperDate(currDt));
        List accountList = ClientUtil.executeQuery("getAllLockedAccounts", whereMap);
        System.out.println("closedLoanList :: " + accountList);
        if (accountList != null && accountList.size() > 0) {
            populateLockedAccountData(accountList);
            lblNoOfRecordsVal.setText(String.valueOf(tblData.getRowCount()));
            tblData.setRowHeight(30);
            javax.swing.table.TableColumn col = tblData.getColumn(tblData.getColumnName(1));
            col.setMaxWidth(150);
            col.setMinWidth(150);
            col.setWidth(150);
            col.setPreferredWidth(150);
            col = tblData.getColumn(tblData.getColumnName(0));
            col.setMaxWidth(150);
            col.setMinWidth(150);
            col.setWidth(150);
            col.setPreferredWidth(150);
            col = tblData.getColumn(tblData.getColumnName(2));
            col.setMaxWidth(150);
            col.setMinWidth(150);
            col.setWidth(150);
            col.setPreferredWidth(150);
            colorList = new ArrayList();
        } else {
            ClientUtil.showMessageWindow("No Data !!!");
            btnClear1ActionPerformed(null);
        }
    }//GEN-LAST:event_btnShowClosedLoansActionPerformed

    private void btnClosedLoanSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClosedLoanSearchActionPerformed
        // TODO add your handling code here:
         viewType = "LOCKED_ACCTS";
        popUp("LOCKED_ACCTS");
    }//GEN-LAST:event_btnClosedLoanSearchActionPerformed

    private void internationalize() {
//        lblSearch.setText(resourceBundle.getString("lblSearch"));
//        btnSearch.setText(resourceBundle.getString("btnSearch"));
//        chkCase.setText(resourceBundle.getString("chkCase"));
//        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
//        btnCancel.setText(resourceBundle.getString("btnCancel"));
    }

    public void calTotalAmount() {
        if (tblData.getRowCount() > 0) {
            int count = tblData.getRowCount();
            double selectedCount = 0.0;
            for (int i = 0; i < count; i++) {
                if ((Boolean) tblData.getValueAt(i, 0)) {
                    selectedCount++;
                }
            }
            lblSelectedRecords.setText(CommonUtil.convertObjToStr(selectedCount));
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        HashMap mapParam = new HashMap();

        HashMap whereMap = new HashMap();
        whereMap.put("USER_ID", "sysadmin1");
        //getSelectOperativeAcctProductAuthorizeTOList
        whereMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
        whereMap.put("OUTWARD_DT", ClientUtil.getCurrentDate());
        mapParam.put(CommonConstants.MAP_NAME, "getSelectOutwardClearingRealizeTOList");
        mapParam.put(CommonConstants.MAP_WHERE, whereMap);

        mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeOperativeAcctProduct");

//        AuthorizeUI authorizeUI = new AuthorizeUI(mapParam);
//        authorizeUI.setAuthorize(true);
//        authorizeUI.setException(false);
//        authorizeUI.setReject(false);
//        authorizeUI.setRealize(true);
//        authorizeUI.show();
    }

    public void update(Observable o, Object arg) {
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnClear1;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnClosedLoanSearch;
    private com.see.truetransact.uicomponent.CButton btnLoanRelease;
    private com.see.truetransact.uicomponent.CButton btnShowClosedLoans;
    private com.see.truetransact.uicomponent.CTabbedPane cTabbedPane1;
    private com.see.truetransact.uicomponent.CLabel lblClosedLoanActNo;
    private com.see.truetransact.uicomponent.CLabel lblNoOfRecords;
    private com.see.truetransact.uicomponent.CLabel lblNoOfRecordsVal;
    private com.see.truetransact.uicomponent.CLabel lblProdDescription1;
    private com.see.truetransact.uicomponent.CLabel lblSelectedRecords;
    private com.see.truetransact.uicomponent.CPanel panArbit2;
    private com.see.truetransact.uicomponent.CPanel panMultiSearch3;
    private com.see.truetransact.uicomponent.CPanel panSearch;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CPanel panUnlock;
    private com.see.truetransact.uicomponent.CButtonGroup rdbArbit;
    private com.see.truetransact.uicomponent.CSeparator sptLine;
    private com.see.truetransact.uicomponent.CScrollPane srcTable;
    private com.see.truetransact.uicomponent.CTable tblData;
    private com.see.truetransact.uicomponent.CTextField txtAcctNo;
    // End of variables declaration//GEN-END:variables
}
