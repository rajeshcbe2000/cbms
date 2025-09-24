/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * BalanceUpdateUI.java
 *
 * Created on October 16, 2009, 1:46 PM
 */
package com.see.truetransact.ui.supporting.inventory;

import com.see.truetransact.clientproxy.ProxyParameters;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Observable;
import java.util.Date;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import javax.swing.table.DefaultTableModel;
import java.util.Enumeration;
import java.text.SimpleDateFormat;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import com.see.truetransact.ui.common.viewall.RejectionApproveUI;
import com.see.truetransact.uimandatory.UIMandatoryField;
import java.text.DateFormat;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.common.viewall.RejectionApproveUI;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uivalidation.CurrencyValidation;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;



/**
 * @author Swaroop
 */
public class BalanceUpdateUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField {

    private BalanceUpdateOB observable;
    boolean collDet = false;
    DefaultTableModel model = null;
    Date currDt = null;
    ArrayList _heading = null;
    ArrayList data = null;
   // private EnhancedTableModel tmbBalanceUpdate;
    String node = "";
    List lst1;
    List lst2;
    RejectionApproveUI rejectionApproveUI = null;
    private final static Logger log = Logger.getLogger(BalanceUpdateUI.class);
    final int EDIT=0, DELETE=1, AUTHORIZE=2, VIEW =3;
    int viewType=-1;
    boolean isFilled = false;
    private TableModelListener tableModelListener;
    private addBalanceDataUI addDataUI = null;
    private HashMap manualMap = new HashMap();
    ArrayList colorList = new ArrayList();
    
    public BalanceUpdateUI() {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        currDt = ClientUtil.getCurrentDate();
        settingupUI();
        setupInit();
        setupScreen();
        btnNew.setEnabled(true);
    }
    private void settingupUI() {
        // initComponentData();
    }
    private void setupInit() {
        initComponents();
        //internationalize();
        setObservable();
        toFront();
        observable.fillDropDown();
        cboBranch.setModel(observable.getCbmbranch());
        cboBranchFinal.setModel(observable.getCbmbranch());
        txtNetLoss.setValidation(new CurrencyValidation(13, 2));
        txtNetProfit.setValidation(new CurrencyValidation(13, 2));
    }

    private void setupScreen() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        /* Center frame on the screen */
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
            observable = new BalanceUpdateOB();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        if (viewType == AUTHORIZE || viewType == VIEW) {
            isFilled = true;
            hash.put(CommonConstants.MAP_WHERE, hash.get("BAL_SHEET_ID"));
            observable.setBalSheetId(CommonUtil.convertObjToStr(hash.get("BAL_SHEET_ID")));
            // observable.populateData(hash);
            btnProcess.setEnabled(false);
            btnFinalProcess.setEnabled(false);
            btnAddRecord.setVisible(false);
            btnAddRecord.setEnabled(false);
            if (tabDCDayBegin.getSelectedIndex() == 0) {
                initTableAuthData();
            }
            if (tabDCDayBegin.getSelectedIndex() == 1) {
                if(hash.get("FINAL_ACCOUNT_TYPE")!=null && hash.get("FINAL_ACCOUNT_TYPE").equals("BALANCE SHEET"))
                initFinalProcessAuthTableData("Liabilities","Asset",hash.get("FINAL_ACCOUNT_TYPE").toString());
                else
                  initFinalProcessAuthTableData("Expenditure","Income",hash.get("FINAL_ACCOUNT_TYPE").toString()); 
            }
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || viewType == AUTHORIZE || viewType == VIEW) {
                ClientUtil.enableDisable(this, false);     // Disables the panel...
            } else {
                if (hash.get("AUTHORIZE_STATUS").equals("AUTHORIZED")) {
                    ClientUtil.enableDisable(this, false);     // Enables the panel...
                } else {
                    ClientUtil.enableDisable(this, true);
                }
                //                setFieldsEnable(false);
            }

            setButtonEnableDisable();         // Enables or Disables the buttons and menu Items depending on their previous state...
            if (viewType == AUTHORIZE) {
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            }
            //__ To Save the data in the Internal Frame...
            if (hash.containsKey("AUTHORIZE_STATUS")) {
                if (hash.get("AUTHORIZE_STATUS").equals("AUTHORIZED")) {
                    btnSave.setEnabled(false);
                }
            }
            setModified(true);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgAndOr = new com.see.truetransact.uicomponent.CButtonGroup();
        sptLine = new com.see.truetransact.uicomponent.CSeparator();
        tabDCDayBegin = new com.see.truetransact.uicomponent.CTabbedPane();
        panMainSI = new com.see.truetransact.uicomponent.CPanel();
        panSI = new com.see.truetransact.uicomponent.CPanel();
        panSIIDt = new com.see.truetransact.uicomponent.CPanel();
        panSIID = new com.see.truetransact.uicomponent.CPanel();
        panSearchCondition = new com.see.truetransact.uicomponent.CPanel();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        cScrollPane1 = new com.see.truetransact.uicomponent.CScrollPane();
        tblBalanceUpdate = new com.see.truetransact.uicomponent.CTable();
        lblTotal = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        panSearchCondition1 = new com.see.truetransact.uicomponent.CPanel();
        cPanel4 = new com.see.truetransact.uicomponent.CPanel();
        lblDate = new com.see.truetransact.uicomponent.CLabel();
        cboBranch = new com.see.truetransact.uicomponent.CComboBox();
        jPanel1 = new javax.swing.JPanel();
        lblFinalAccountType = new com.see.truetransact.uicomponent.CLabel();
        cboAccountHead = new com.see.truetransact.uicomponent.CComboBox();
        lblBalance = new com.see.truetransact.uicomponent.CLabel();
        lblAccountHead = new com.see.truetransact.uicomponent.CLabel();
        cboFinalActType = new com.see.truetransact.uicomponent.CComboBox();
        btnProcess = new com.see.truetransact.uicomponent.CButton();
        tdtFrmDt = new com.see.truetransact.uicomponent.CDateField();
        panBatchProcess1 = new com.see.truetransact.uicomponent.CPanel();
        cPanel5 = new com.see.truetransact.uicomponent.CPanel();
        lblDate1 = new com.see.truetransact.uicomponent.CLabel();
        cboBranchFinal = new com.see.truetransact.uicomponent.CComboBox();
        jPanel2 = new javax.swing.JPanel();
        lblFinalAccountType1 = new com.see.truetransact.uicomponent.CLabel();
        lblBalance1 = new com.see.truetransact.uicomponent.CLabel();
        cboFinalPrcessType = new com.see.truetransact.uicomponent.CComboBox();
        btnFinalProcess = new com.see.truetransact.uicomponent.CButton();
        tdtFinalDate = new com.see.truetransact.uicomponent.CDateField();
        btnAddRecord = new com.see.truetransact.uicomponent.CButton();
        panSearchCondition2 = new com.see.truetransact.uicomponent.CPanel();
        panTable1 = new com.see.truetransact.uicomponent.CPanel();
        cScrollPane2 = new com.see.truetransact.uicomponent.CScrollPane();
        tblBalanceSheetPreparation = new com.see.truetransact.uicomponent.CTable();
        lblTotExp = new com.see.truetransact.uicomponent.CLabel();
        lblTotalIncome = new com.see.truetransact.uicomponent.CLabel();
        txtTotalExp = new com.see.truetransact.uicomponent.CTextField();
        txtToatalIncome = new com.see.truetransact.uicomponent.CTextField();
        lblNetProfit = new com.see.truetransact.uicomponent.CLabel();
        txtNetProfit = new com.see.truetransact.uicomponent.CTextField();
        lblNetloss = new com.see.truetransact.uicomponent.CLabel();
        txtNetLoss = new com.see.truetransact.uicomponent.CTextField();
        panSearchCondition3 = new com.see.truetransact.uicomponent.CPanel();
        tbrLoantProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace34 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace35 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace36 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace37 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace38 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace39 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        lblSpace40 = new com.see.truetransact.uicomponent.CLabel();
        btnItemLst = new com.see.truetransact.uicomponent.CButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMaximumSize(new java.awt.Dimension(975, 680));
        setMinimumSize(new java.awt.Dimension(975, 680));
        setPreferredSize(new java.awt.Dimension(975, 680));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        sptLine.setMinimumSize(new java.awt.Dimension(2, 2));
        sptLine.setPreferredSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 812;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 72, 0, 0);
        getContentPane().add(sptLine, gridBagConstraints);

        tabDCDayBegin.setMinimumSize(new java.awt.Dimension(890, 524));

        panMainSI.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panMainSI.setMaximumSize(new java.awt.Dimension(750, 500));
        panMainSI.setMinimumSize(new java.awt.Dimension(652, 371));
        panMainSI.setPreferredSize(new java.awt.Dimension(700, 600));
        panMainSI.setLayout(new java.awt.GridBagLayout());

        panSI.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panMainSI.add(panSI, gridBagConstraints);

        panSIIDt.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panMainSI.add(panSIIDt, gridBagConstraints);

        panSIID.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panMainSI.add(panSIID, gridBagConstraints);

        panSearchCondition.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panSearchCondition.setMinimumSize(new java.awt.Dimension(700, 600));
        panSearchCondition.setPreferredSize(new java.awt.Dimension(700, 600));
        panSearchCondition.setLayout(new java.awt.GridBagLayout());

        panTable.setMaximumSize(new java.awt.Dimension(980, 250));
        panTable.setMinimumSize(new java.awt.Dimension(980, 250));
        panTable.setPreferredSize(new java.awt.Dimension(980, 250));

        cScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        cScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        cScrollPane1.setMaximumSize(new java.awt.Dimension(469, 302));
        cScrollPane1.setMinimumSize(new java.awt.Dimension(469, 302));
        cScrollPane1.setPreferredSize(new java.awt.Dimension(469, 302));

        tblBalanceUpdate.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Iteam Head", "Account Head Desc", "Balance"
            }
        ));
        cScrollPane1.setViewportView(tblBalanceUpdate);

        lblTotal.setText("Total");

        txtTotal.setEditable(false);

        javax.swing.GroupLayout panTableLayout = new javax.swing.GroupLayout(panTable);
        panTable.setLayout(panTableLayout);
        panTableLayout.setHorizontalGroup(
            panTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panTableLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panTableLayout.createSequentialGroup()
                        .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(89, 89, 89))
                    .addGroup(panTableLayout.createSequentialGroup()
                        .addComponent(cScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 649, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        panTableLayout.setVerticalGroup(
            panTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panTableLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(cScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotal)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = -311;
        gridBagConstraints.ipady = 108;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(35, 11, 14, 0);
        panSearchCondition.add(panTable, gridBagConstraints);

        panSearchCondition1.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panSearchCondition.add(panSearchCondition1, gridBagConstraints);

        cPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        cPanel4.setMaximumSize(new java.awt.Dimension(260, 260));
        cPanel4.setMinimumSize(new java.awt.Dimension(260, 260));
        cPanel4.setPreferredSize(new java.awt.Dimension(260, 260));
        cPanel4.setLayout(new java.awt.GridBagLayout());

        lblDate.setText(" Date");
        lblDate.setMaximumSize(new java.awt.Dimension(115, 21));
        lblDate.setMinimumSize(new java.awt.Dimension(115, 21));
        lblDate.setPreferredSize(new java.awt.Dimension(115, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        cPanel4.add(lblDate, gridBagConstraints);

        cboBranch.setMaximumSize(new java.awt.Dimension(200, 21));
        cboBranch.setMinimumSize(new java.awt.Dimension(200, 21));
        cboBranch.setOpaque(false);
        cboBranch.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        cPanel4.add(cboBranch, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        lblFinalAccountType.setText("Final Account Type");
        lblFinalAccountType.setMaximumSize(new java.awt.Dimension(115, 21));
        lblFinalAccountType.setMinimumSize(new java.awt.Dimension(115, 21));
        lblFinalAccountType.setPreferredSize(new java.awt.Dimension(115, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        jPanel1.add(lblFinalAccountType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        cPanel4.add(jPanel1, gridBagConstraints);

        cboAccountHead.setMaximumSize(new java.awt.Dimension(100, 21));
        cboAccountHead.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        cPanel4.add(cboAccountHead, gridBagConstraints);

        lblBalance.setText("Branch");
        lblBalance.setMaximumSize(new java.awt.Dimension(115, 21));
        lblBalance.setMinimumSize(new java.awt.Dimension(115, 21));
        lblBalance.setPreferredSize(new java.awt.Dimension(115, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        cPanel4.add(lblBalance, gridBagConstraints);

        lblAccountHead.setText("Final Account Subtype");
        lblAccountHead.setMaximumSize(new java.awt.Dimension(145, 21));
        lblAccountHead.setMinimumSize(new java.awt.Dimension(145, 21));
        lblAccountHead.setPreferredSize(new java.awt.Dimension(145, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        cPanel4.add(lblAccountHead, gridBagConstraints);

        cboFinalActType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select", "TRADING", "PROFIT AND LOSS", "BALANCE SHEET", " " }));
        cboFinalActType.setMaximumSize(new java.awt.Dimension(100, 21));
        cboFinalActType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboFinalActType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboFinalActTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        cPanel4.add(cboFinalActType, gridBagConstraints);

        btnProcess.setText("Process");
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(6, 19, 0, 0);
        cPanel4.add(btnProcess, gridBagConstraints);

        tdtFrmDt.setMaximumSize(new java.awt.Dimension(101, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        cPanel4.add(tdtFrmDt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 436;
        gridBagConstraints.ipady = -86;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panSearchCondition.add(cPanel4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipady = -17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 147, 0, 109);
        panMainSI.add(panSearchCondition, gridBagConstraints);

        tabDCDayBegin.addTab("Balance Update", panMainSI);

        panBatchProcess1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panBatchProcess1.setMinimumSize(new java.awt.Dimension(740, 624));
        panBatchProcess1.setName(""); // NOI18N
        panBatchProcess1.setPreferredSize(new java.awt.Dimension(740, 624));
        panBatchProcess1.setLayout(null);

        cPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        cPanel5.setMaximumSize(new java.awt.Dimension(260, 260));
        cPanel5.setMinimumSize(new java.awt.Dimension(260, 260));
        cPanel5.setPreferredSize(new java.awt.Dimension(260, 260));
        cPanel5.setLayout(new java.awt.GridBagLayout());

        lblDate1.setText(" Date");
        lblDate1.setMaximumSize(new java.awt.Dimension(115, 21));
        lblDate1.setMinimumSize(new java.awt.Dimension(115, 21));
        lblDate1.setPreferredSize(new java.awt.Dimension(115, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        cPanel5.add(lblDate1, gridBagConstraints);

        cboBranchFinal.setMaximumSize(new java.awt.Dimension(200, 21));
        cboBranchFinal.setMinimumSize(new java.awt.Dimension(200, 21));
        cboBranchFinal.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        cPanel5.add(cboBranchFinal, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        lblFinalAccountType1.setText("Final Account Type");
        lblFinalAccountType1.setMaximumSize(new java.awt.Dimension(115, 21));
        lblFinalAccountType1.setMinimumSize(new java.awt.Dimension(115, 21));
        lblFinalAccountType1.setPreferredSize(new java.awt.Dimension(115, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        jPanel2.add(lblFinalAccountType1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        cPanel5.add(jPanel2, gridBagConstraints);

        lblBalance1.setText("Branch");
        lblBalance1.setMaximumSize(new java.awt.Dimension(115, 21));
        lblBalance1.setMinimumSize(new java.awt.Dimension(115, 21));
        lblBalance1.setPreferredSize(new java.awt.Dimension(115, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        cPanel5.add(lblBalance1, gridBagConstraints);

        cboFinalPrcessType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select", "TRADING", "PROFIT AND LOSS", "BALANCE SHEET" }));
        cboFinalPrcessType.setMaximumSize(new java.awt.Dimension(100, 21));
        cboFinalPrcessType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboFinalPrcessType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboFinalPrcessTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        cPanel5.add(cboFinalPrcessType, gridBagConstraints);

        btnFinalProcess.setText("Process");
        btnFinalProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFinalProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(6, 19, 0, 0);
        cPanel5.add(btnFinalProcess, gridBagConstraints);

        tdtFinalDate.setMaximumSize(new java.awt.Dimension(101, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        cPanel5.add(tdtFinalDate, gridBagConstraints);

        btnAddRecord.setText("Add New Record");
        btnAddRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddRecordActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        cPanel5.add(btnAddRecord, gridBagConstraints);

        panBatchProcess1.add(cPanel5);
        cPanel5.setBounds(2, 13, 948, 140);

        panSearchCondition2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panSearchCondition2.setMaximumSize(new java.awt.Dimension(700, 600));
        panSearchCondition2.setMinimumSize(new java.awt.Dimension(700, 600));
        panSearchCondition2.setPreferredSize(new java.awt.Dimension(700, 600));
        panSearchCondition2.setLayout(null);

        panTable1.setMaximumSize(new java.awt.Dimension(500, 250));
        panTable1.setMinimumSize(new java.awt.Dimension(500, 250));
        panTable1.setPreferredSize(new java.awt.Dimension(500, 250));

        cScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        cScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        cScrollPane2.setMaximumSize(new java.awt.Dimension(469, 302));
        cScrollPane2.setMinimumSize(new java.awt.Dimension(469, 302));

        tblBalanceSheetPreparation.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Iteam Head", "Account Head Desc", "Income", "Expendeture", "BalanceHead"
            }
        ));
        cScrollPane2.setViewportView(tblBalanceSheetPreparation);

        lblTotExp.setText("Total Income");

        lblTotalIncome.setText("Toatl Expenditure");

        txtTotalExp.setEditable(false);
        txtTotalExp.setEnabled(false);

        txtToatalIncome.setEnabled(false);

        lblNetProfit.setText("Net Profit");

        txtNetProfit.setEditable(false);
        txtNetProfit.setEnabled(false);

        lblNetloss.setText("Net Loss");

        txtNetLoss.setEditable(false);
        txtNetLoss.setEnabled(false);

        javax.swing.GroupLayout panTable1Layout = new javax.swing.GroupLayout(panTable1);
        panTable1.setLayout(panTable1Layout);
        panTable1Layout.setHorizontalGroup(
            panTable1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panTable1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(cScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 880, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panTable1Layout.createSequentialGroup()
                .addContainerGap(399, Short.MAX_VALUE)
                .addGroup(panTable1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(panTable1Layout.createSequentialGroup()
                        .addComponent(lblNetProfit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtNetProfit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panTable1Layout.createSequentialGroup()
                        .addComponent(lblTotExp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTotalExp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(panTable1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panTable1Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(lblTotalIncome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtToatalIncome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panTable1Layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(lblNetloss, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtNetLoss, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(80, 80, 80))
        );
        panTable1Layout.setVerticalGroup(
            panTable1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panTable1Layout.createSequentialGroup()
                .addComponent(cScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panTable1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panTable1Layout.createSequentialGroup()
                        .addGroup(panTable1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNetProfit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNetProfit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panTable1Layout.createSequentialGroup()
                        .addGroup(panTable1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNetloss, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNetLoss, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)))
                .addGroup(panTable1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotExp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotalIncome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTotalExp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtToatalIncome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        panSearchCondition2.add(panTable1);
        panTable1.setBounds(10, 10, 920, 390);

        panSearchCondition3.setLayout(new java.awt.GridBagLayout());
        panSearchCondition2.add(panSearchCondition3);
        panSearchCondition3.setBounds(0, 0, 0, 0);

        panBatchProcess1.add(panSearchCondition2);
        panSearchCondition2.setBounds(10, 160, 940, 409);

        tabDCDayBegin.addTab("BalanceSheetPreparation", panBatchProcess1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 77;
        gridBagConstraints.ipady = 75;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        getContentPane().add(tabDCDayBegin, gridBagConstraints);

        tbrLoantProduct.setMaximumSize(new java.awt.Dimension(349, 27));
        tbrLoantProduct.setMinimumSize(new java.awt.Dimension(349, 27));
        tbrLoantProduct.setPreferredSize(new java.awt.Dimension(349, 27));

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnView);

        lblSpace5.setText("     ");
        tbrLoantProduct.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnNew);

        lblSpace34.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace34.setText("     ");
        lblSpace34.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace34);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnEdit);

        lblSpace35.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace35.setText("     ");
        lblSpace35.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace35.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace35.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace35);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrLoantProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnSave);

        lblSpace36.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace36.setText("     ");
        lblSpace36.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace36.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace36.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace36);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrLoantProduct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnAuthorize);

        lblSpace37.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace37.setText("     ");
        lblSpace37.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace37.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace37.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace37);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnException);

        lblSpace38.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace38.setText("     ");
        lblSpace38.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace38.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace38.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace38);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnReject);

        lblSpace4.setText("     ");
        tbrLoantProduct.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrLoantProduct.add(btnPrint);

        lblSpace39.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace39.setText("     ");
        lblSpace39.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace39.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace39.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace39);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnClose);

        lblSpace40.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace40.setText("     ");
        lblSpace40.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace40.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace40.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace40);

        btnItemLst.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnItemLst.setToolTipText("Item Balance Listing");
        btnItemLst.setMinimumSize(new java.awt.Dimension(31, 23));
        btnItemLst.setPreferredSize(new java.awt.Dimension(21, 21));
        btnItemLst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemLstActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnItemLst);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 622;
        gridBagConstraints.ipady = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(tbrLoantProduct, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) currDt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }

    private Object[][] setFinalProcessTableData() {
        HashMap whereMap = new HashMap();
        System.out.println("BRR==" + observable.getCbmbranch().getKeyForSelected() + "DATE----" + tdtFinalDate.getDateValue()
                + "FINAL----" + cboFinalPrcessType.getSelectedItem());
        whereMap.put("DT", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtFinalDate.getDateValue())));
        if(observable.getCbmbranch().getKeyForSelected()!=null && observable.getCbmbranch().getKeyForSelected().equals("")){
            whereMap.put("BRANCH_CODE", null);
        }
        else{
             whereMap.put("BRANCH_CODE", observable.getCbmbranch().getKeyForSelected());
        }
        whereMap.put("FINAL_ACCOUNT_TYPE", cboFinalPrcessType.getSelectedItem());
        whereMap.put("FINAL_SUBACCOUNT_TYPE", cboAccountHead.getSelectedItem());
        lst1 = ClientUtil.executeQuery("getFinalBalanceSheet", whereMap);
        HashMap finalProcessMap = new HashMap();
        if (lst1 != null && lst1.size() > 0) {
            Object totalList[][] = new Object[lst1.size()][6];
            for (int j = 0; j < lst1.size(); j++) {
                finalProcessMap = (HashMap) lst1.get(j);
                totalList[j][0] = CommonUtil.convertObjToStr(finalProcessMap.get("MJR_AC_HD_DESC"));
                totalList[j][1] = CommonUtil.convertObjToStr(finalProcessMap.get("AC_HD_DESC"));
                totalList[j][2] = CommonUtil.convertObjToStr(finalProcessMap.get("AC_HD_ID"));
                if((cboFinalPrcessType.getSelectedItem()!=null && cboFinalPrcessType.getSelectedItem().equals("TRADING")) ||
                     (cboFinalPrcessType.getSelectedItem()!=null && cboFinalPrcessType.getSelectedItem().equals("PROFIT AND LOSS"))   )
                {
                    if (finalProcessMap.get("BALANCE_TYPE") != null && finalProcessMap.get("BALANCE_TYPE").equals("DEBIT")) {
                         totalList[j][3] = CommonUtil.convertObjToStr(finalProcessMap.get("CLOSE_BAL"));
                         totalList[j][4] = "0.00";
                        
                    }
                    if (finalProcessMap.get("BALANCE_TYPE") != null && finalProcessMap.get("BALANCE_TYPE").equals("CREDIT")) {
                         totalList[j][3] = "0.00";
                         totalList[j][4] = CommonUtil.convertObjToStr(finalProcessMap.get("CLOSE_BAL"));
                    }
                }
                else{
                    if (finalProcessMap.get("BALANCE_TYPE") != null && finalProcessMap.get("BALANCE_TYPE").equals("DEBIT")) {
                        
                        totalList[j][3] = "0.00";
                        totalList[j][4] = CommonUtil.convertObjToStr(finalProcessMap.get("CLOSE_BAL"));
                    }
                    if (finalProcessMap.get("BALANCE_TYPE") != null && finalProcessMap.get("BALANCE_TYPE").equals("CREDIT")) {
                        
                        totalList[j][3] = CommonUtil.convertObjToStr(finalProcessMap.get("CLOSE_BAL"));
                        totalList[j][4] = "0.00";
                    }
                }
                totalList[j][5] = CommonUtil.convertObjToStr(finalProcessMap.get("BALANCE_TYPE"));
            }
            return totalList;
        } else {
            ClientUtil.displayAlert("No Data!!! ");
        }
        return null;

    }

    private Object[][] setFinalProcessAuthTableData(String finaltype) {
        HashMap whereMap = new HashMap();
        colorList = new ArrayList();
        whereMap.put("BAL_SHEET_ID", observable.getBalSheetId());
        lst1 = ClientUtil.executeQuery("getSelectBalanceFinalSheetTO", whereMap);
        HashMap finalProcessAuthMap = new HashMap();
        if (lst1 != null && lst1.size() > 0) {
            Object totalList[][] = new Object[lst1.size()][6];
            for (int j = 0; j < lst1.size(); j++) {
                finalProcessAuthMap = (HashMap) lst1.get(j);
                String descData = CommonUtil.convertObjToStr(finalProcessAuthMap.get("ACCOUNT_HEAD_DESC"));
                String part1 = "", part2 = "";
                if (descData.contains("-")) {
                    String[] parts = descData.split("-");
                    part1 = parts[0];
                    part2 = parts[1];
                }else{
                    part1 = "MANUAL ENTRY";
                    part2 = descData;
                    colorList.add(String.valueOf(j));
                    System.out.println("colorList&$&$&&"+colorList);
                }
                totalList[j][0] = CommonUtil.convertObjToStr(part1);
                totalList[j][1] = CommonUtil.convertObjToStr(part2);
                totalList[j][2] = CommonUtil.convertObjToStr(finalProcessAuthMap.get("ACCOUNT_HEAD_ID"));
              /*  if (finalProcessAuthMap.get("BALANCE_TYPE") != null && finalProcessAuthMap.get("BALANCE_TYPE").equals("DEBIT")) {
                    totalList[j][3] = CommonUtil.convertObjToStr(finalProcessAuthMap.get("AMOUNT"));
                    totalList[j][4] = "0.00";
                }
                if (finalProcessAuthMap.get("BALANCE_TYPE") != null && finalProcessAuthMap.get("BALANCE_TYPE").equals("CREDIT")) {
                    totalList[j][3] = "0.00";
                    totalList[j][4] = CommonUtil.convertObjToStr(finalProcessAuthMap.get("AMOUNT"));
                }*/
                if((finaltype!=null && finaltype.equals("TRADING")) ||
                     (finaltype!=null && finaltype.equals("PROFIT AND LOSS"))   )
                {
                    if (finalProcessAuthMap.get("BALANCE_TYPE") != null && finalProcessAuthMap.get("BALANCE_TYPE").equals("DEBIT")) {
                         totalList[j][3] = CommonUtil.convertObjToStr(finalProcessAuthMap.get("AMOUNT"));
                         totalList[j][4] = "0.00";
                        
                    }
                    if (finalProcessAuthMap.get("BALANCE_TYPE") != null && finalProcessAuthMap.get("BALANCE_TYPE").equals("CREDIT")) {
                         totalList[j][3] = "0.00";
                         totalList[j][4] = CommonUtil.convertObjToStr(finalProcessAuthMap.get("AMOUNT"));
                    }
                }
                else{
                    if (finalProcessAuthMap.get("BALANCE_TYPE") != null && finalProcessAuthMap.get("BALANCE_TYPE").equals("DEBIT")) {
                        
                        totalList[j][3] = "0.00";
                        totalList[j][4] = CommonUtil.convertObjToStr(finalProcessAuthMap.get("AMOUNT"));
                    }
                    if (finalProcessAuthMap.get("BALANCE_TYPE") != null && finalProcessAuthMap.get("BALANCE_TYPE").equals("CREDIT")) {
                        
                        totalList[j][3] = CommonUtil.convertObjToStr(finalProcessAuthMap.get("AMOUNT"));
                        totalList[j][4] = "0.00";
                    }
                }
                totalList[j][5] = CommonUtil.convertObjToStr(finalProcessAuthMap.get("BALANCE_TYPE"));
                observable.getCbmbranch().setKeyForSelected(CommonUtil.convertObjToStr(finalProcessAuthMap.get("BRANCH_CODE")));
                tdtFinalDate.setDateValue(CommonUtil.convertObjToStr(finalProcessAuthMap.get("TO_DT")));
                cboFinalPrcessType.setSelectedItem(CommonUtil.convertObjToStr(finalProcessAuthMap.get("FINAL_ACCOUNT_TYPE")));
            }
            return totalList;
        } else {
            ClientUtil.displayAlert("No Data!!! ");

        }
        return null;

    }

    private Object[][] setTableData() {
        HashMap whereMap = new HashMap();
        whereMap.put("DT", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtFrmDt.getDateValue())));
        whereMap.put("FINAL_ACCOUNT_TYPE", cboFinalActType.getSelectedItem());
        whereMap.put("FINAL_SUBACCOUNT_TYPE", cboAccountHead.getSelectedItem());
        System.out.println("Keyyy------------"+observable.getCbmbranch().getKeyForSelected());
        if (cboFinalActType.getSelectedItem() != null && cboFinalActType.getSelectedItem().equals("BALANCE SHEET")) {
            if(observable.getCbmbranch().getKeyForSelected()!=null && !observable.getCbmbranch().getKeyForSelected().equals("")){
                whereMap.put("BRANCH_CODE", observable.getCbmbranch().getKeyForSelected());
            }
            else{
                 whereMap.put("BRANCH_CODE", null); 
            }
            if (cboAccountHead.getSelectedItem() != null && cboAccountHead.getSelectedItem().equals("INTEREST RECIEVABLE")) {
                lst1 = ClientUtil.executeQuery("getBalanceUpdateReceivable", whereMap);
            }
            if (cboAccountHead.getSelectedItem() != null && cboAccountHead.getSelectedItem().equals("INTEREST PAYABLE")) {
                lst1 = ClientUtil.executeQuery("getBalanceUpdatePayable", whereMap);
            }
        }
        if (cboFinalActType.getSelectedItem().equals("TRADING")) {
            /// if(cboAccountHead.getSelectedItem()!=null && cboAccountHead.getSelectedItem().equals("CLOSING STOCK")){
             if(observable.getCbmbranch().getKeyForSelected()!=null && !observable.getCbmbranch().getKeyForSelected().equals("")){
                whereMap.put("BRANCH_CODE", observable.getCbmbranch().getKeyForSelected());
            }
            
            lst1 = ClientUtil.executeQuery("getBalanceUpdateData", whereMap);
            // }
        }
         if (cboFinalActType.getSelectedItem() != null && cboFinalActType.getSelectedItem().equals("PROFIT AND LOSS")) {
            if(observable.getCbmbranch().getKeyForSelected()!=null && !observable.getCbmbranch().getKeyForSelected().equals("")){
                whereMap.put("BRANCH_CODE", observable.getCbmbranch().getKeyForSelected());
            }
            else{
                 whereMap.put("BRANCH_CODE", null); 
            }
            if (cboAccountHead.getSelectedItem() != null && cboAccountHead.getSelectedItem().equals("INTEREST RECEIVED")) {
                lst1 = ClientUtil.executeQuery("getBalancePOReceivable", whereMap);
            }
            if (cboAccountHead.getSelectedItem() != null && cboAccountHead.getSelectedItem().equals("INTEREST PAID")) {
                lst1 = ClientUtil.executeQuery("getBalancePOPayable", whereMap);
            }
        }
        HashMap processMap = new HashMap();
        if (lst1 != null && lst1.size() > 0) {
            Object totalList[][] = new Object[lst1.size()][4];
            for (int j = 0; j < lst1.size(); j++) {
                processMap = (HashMap) lst1.get(j);
                if (cboFinalActType.getSelectedItem().equals("TRADING")) {
                    //   if(cboAccountHead.getSelectedItem()!=null && cboAccountHead.getSelectedItem().equals("CLOSING STOCK")){
                    totalList[j][0] = CommonUtil.convertObjToStr(processMap.get("AC_HD_ID"));
                    totalList[j][1] = CommonUtil.convertObjToStr(processMap.get("AC_HD_DESC"));
                    totalList[j][2] = CommonUtil.convertObjToStr(processMap.get("BALANCE"));
                    totalList[j][3] = CommonUtil.convertObjToStr(processMap.get("TYPE"));
                    // }
                }
                if (cboFinalActType.getSelectedItem() != null && cboFinalActType.getSelectedItem().equals("BALANCE SHEET")) {
                    if (cboAccountHead.getSelectedItem() != null && cboAccountHead.getSelectedItem().equals("INTEREST RECIEVABLE")) {
                        totalList[j][0] = CommonUtil.convertObjToStr(processMap.get("ACID"));
                        totalList[j][1] = CommonUtil.convertObjToStr(processMap.get("DESCR"));
                        totalList[j][2] = CommonUtil.convertObjToStr(processMap.get("INTRECEIVABLE"));
                        totalList[j][3] = "DEBIT";
                    }
                    if (cboAccountHead.getSelectedItem() != null && cboAccountHead.getSelectedItem().equals("INTEREST PAYABLE")) {
                        totalList[j][0] = CommonUtil.convertObjToStr(processMap.get("AC_HD_ID"));
                        totalList[j][1] = CommonUtil.convertObjToStr(processMap.get("PROD_DESC"));
                        totalList[j][2] = CommonUtil.convertObjToStr(processMap.get("INTPAYABLE"));
                        totalList[j][3] = "CREDIT";
                    }
                }
                //p & l
                if (cboFinalActType.getSelectedItem() != null && cboFinalActType.getSelectedItem().equals("PROFIT AND LOSS")) {
                    if (cboAccountHead.getSelectedItem() != null && cboAccountHead.getSelectedItem().equals("INTEREST RECEIVED")) {
                        totalList[j][0] = CommonUtil.convertObjToStr(processMap.get("INT_RCBL_HD"));
                        totalList[j][1] = CommonUtil.convertObjToStr(processMap.get("AC_HD_DESC"));
                        totalList[j][2] = CommonUtil.convertObjToStr(processMap.get("BALANCE"));
                        totalList[j][3] = "CREDIT";
                    }
                    if (cboAccountHead.getSelectedItem() != null && cboAccountHead.getSelectedItem().equals("INTEREST PAID")) {
                        totalList[j][0] = CommonUtil.convertObjToStr(processMap.get("INT_PYBL_HD"));
                        totalList[j][1] = CommonUtil.convertObjToStr(processMap.get("AC_HD_DESC"));
                        totalList[j][2] = CommonUtil.convertObjToStr(processMap.get("BALANCE"));
                        totalList[j][3] = "DEBIT";
                    }
                }
            }
            return totalList;
        } else {
            ClientUtil.displayAlert("No Data!!! ");

        }
        return null;

    }

    private Object[][] setTableAuthData() {
        HashMap whereMap = new HashMap();
        whereMap.put("BAL_SHEET_ID", observable.getBalSheetId());
        lst1 = ClientUtil.executeQuery("getSelectBalanceSheetTO", whereMap);
        System.out.println("lst1lst1lst1lst1" + lst1);
        HashMap processAuthMap = new HashMap();
        if (lst1 != null && lst1.size() > 0) {
            Object totalList[][] = new Object[lst1.size()][4];
            for (int j = 0; j < lst1.size(); j++) {
                processAuthMap = (HashMap) lst1.get(j);
                totalList[j][0] = CommonUtil.convertObjToStr(processAuthMap.get("ACCOUNT_HEAD_ID"));
                totalList[j][1] = CommonUtil.convertObjToStr(processAuthMap.get("ACCOUNT_HEAD_DESC"));
                totalList[j][2] = CommonUtil.convertObjToStr(processAuthMap.get("AMOUNT"));
                totalList[j][3] = CommonUtil.convertObjToStr(processAuthMap.get("BALANCE_TYPE"));
                observable.getCbmbranch().setKeyForSelected(CommonUtil.convertObjToStr(processAuthMap.get("BRANCH_CODE")));
                tdtFrmDt.setDateValue(CommonUtil.convertObjToStr(processAuthMap.get("TO_DT")));
                cboFinalActType.setSelectedItem(CommonUtil.convertObjToStr(processAuthMap.get("FINAL_ACCOUNT_TYPE")));
                cboAccountHead.setSelectedItem(CommonUtil.convertObjToStr(processAuthMap.get("SUB_ACCOUNT_TYPE")));
          
            }
            return totalList;
        }
        return null;

    }

    public void initTableData() {
        // model=new javax.swing.table.DefaultTableModel();

        tblBalanceUpdate.setModel(new javax.swing.table.DefaultTableModel(
                setTableData(),
                new String[]{
                    "Iteam Head", "Account Head Desc", "Balance", "Type"}) {

            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (columnIndex == 2 || columnIndex == 3) {
                    return true;
                }
                return canEdit[columnIndex];
            }
        });
        //setWidthColumns();

        tblBalanceUpdate.setCellSelectionEnabled(true);
        tblBalanceUpdate.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                //  tblBalanceUpdatePropertyChange(evt);
            }
        });

        setTableModelListener();
        if (tblBalanceUpdate.getRowCount() > 0) {
            // boolean chk=((Boolean)tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 0)).booleanValue(); 
            System.out.println("calcTotal inuit=====");
            calcTotal();
        }
    }
private void setTableModelListener() {
        try{
        tableModelListener = new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    int selectedRow=tblBalanceUpdate.getSelectedRow(); 
                    double amount =  CommonUtil.convertObjToDouble(tblBalanceUpdate.getValueAt(tblBalanceUpdate.getSelectedRow(),2));
                    System.out.println("column                                  "+column);
                    if (column == 2) {
                        if(amount<0){
                             tblBalanceUpdate.setValueAt("DEBIT", selectedRow,3);
                        }
                        else{
                            tblBalanceUpdate.setValueAt("CREDIT", selectedRow,3);
                        }    
                    }  
            }
            }
        };
        tblBalanceUpdate.getModel().addTableModelListener(tableModelListener);
    }catch(Exception e){
            e.printStackTrace();
            }
    }
    public void initTableAuthData() {
        // model=new javax.swing.table.DefaultTableModel();

        tblBalanceUpdate.setModel(new javax.swing.table.DefaultTableModel(
                setTableAuthData(),
                new String[]{
                    "Iteam Head", "Account Head Desc", "Balance", "Type"}) {

            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
        tblBalanceUpdate.setCellSelectionEnabled(true);
        tblBalanceUpdate.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                //  tblBalanceUpdatePropertyChange(evt);
            }
        });
        if (tblBalanceUpdate.getRowCount() > 0) {
            calcTotal();
        }
    }

    public void initFinalProcessTableData(String head1,String head2) {
        tblBalanceSheetPreparation.setModel(new javax.swing.table.DefaultTableModel(
                setFinalProcessTableData(),
                new String[]{
                    "Group", "Head Desc", "Head", head1, head2, "Type"}) {

            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
        tblBalanceSheetPreparation.getColumnModel().getColumn(5).setMinWidth(0);
        tblBalanceSheetPreparation.getColumnModel().getColumn(5).setMaxWidth(0);
         tblBalanceSheetPreparation.getColumnModel().getColumn(5).setPreferredWidth(0);
        tblBalanceSheetPreparation.setCellSelectionEnabled(true);
        tblBalanceSheetPreparation.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                //  tblBalanceUpdatePropertyChange(evt);
            }
        });
        if (tblBalanceSheetPreparation.getRowCount() > 0) {
            calcFinalProcessTotal();
        }
    }

    public void initFinalProcessAuthTableData(String head1,String head2,String finaltype) {
        tblBalanceSheetPreparation.setModel(new javax.swing.table.DefaultTableModel(
                setFinalProcessAuthTableData(finaltype),
                new String[]{
                    "Group", "Head Desc", "Head",head1, head2, "Type"}) {

            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
        setColour();
         tblBalanceSheetPreparation.getColumnModel().getColumn(5).setMinWidth(0);
        tblBalanceSheetPreparation.getColumnModel().getColumn(5).setMaxWidth(0);
         tblBalanceSheetPreparation.getColumnModel().getColumn(5).setPreferredWidth(0);
        tblBalanceSheetPreparation.setCellSelectionEnabled(true);
        tblBalanceSheetPreparation.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                //  tblBalanceUpdatePropertyChange(evt);
            }
        });

        if (tblBalanceSheetPreparation.getRowCount() > 0) {
            calcFinalProcessTotal();            
        }
    }

    public void calcFinalProcessTotal() {
        double totalExp = 0, totalInc = 0, diff = 0;
        if (tblBalanceSheetPreparation.getRowCount() > 0) {
            for (int i = 0; i < tblBalanceSheetPreparation.getRowCount(); i++) {
                totalExp = totalExp + CommonUtil.convertObjToDouble(tblBalanceSheetPreparation.getValueAt(i, 3)).doubleValue();
                totalInc = totalInc + CommonUtil.convertObjToDouble(tblBalanceSheetPreparation.getValueAt(i, 4)).doubleValue();
            }
        }
        System.out.println("totalExp^$&$&#&#&"+totalExp);
        System.out.println("totalInc^$&$&#&#&"+totalInc);
        if(addDataUI!=null){
            totalExp = totalExp + addDataUI.getNetLoss();
            totalInc = totalInc + addDataUI.getNetProfit();
        }
        System.out.println("totalExp^$&$&#&#&afetree"+totalExp);
        System.out.println("totalInc^$&$&#&#&afetree"+totalInc);
        if (totalInc < totalExp) {
            diff = totalExp - totalInc;
            txtNetProfit.setText("0.0");
            txtNetLoss.setText(CommonUtil.convertObjToStr(diff));
            totalInc = totalInc + diff;
            System.out.println("Diffeence-111-------------"+diff);
        } else {
            diff = totalInc - totalExp;
            txtNetProfit.setText(CommonUtil.convertObjToStr(diff));
            txtNetLoss.setText("0.0");
            totalExp = totalExp + diff;
            System.out.println("Diffeence--222------------"+diff);
        }
         System.out.println("cboFinalPrcessType.getSelectedItem()-------"+cboFinalPrcessType.getSelectedItem());
        if(cboFinalPrcessType.getSelectedItem()!=null && cboFinalPrcessType.getSelectedItem().equals("BALANCE SHEET")){
            System.out.println("Diffeence--------------"+diff);
            txtNetProfit.setText(CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(diff)));
            txtNetLoss.setText("0.0");
        }
        txtTotalExp.setText(CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(totalExp)));
        txtToatalIncome.setText(CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(totalInc)));
    }

    public void calcTotal() {
        double total = 0;
        if (tblBalanceUpdate.getRowCount() > 0) {
            for (int i = 0; i < tblBalanceUpdate.getRowCount(); i++) {
                total = total + CommonUtil.convertObjToDouble(tblBalanceUpdate.getValueAt(i, 2)).doubleValue();
            }
        }
        txtTotal.setText(CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(total)));
    }
    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
        initTableData();
    }//GEN-LAST:event_btnProcessActionPerformed

private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
    observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
}//GEN-LAST:event_btnViewActionPerformed

private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
    observable.resetForm();
    ClientUtil.enableDisable(this, true);
    observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
    setButtonEnableDisable();
    btnAuthorize.setEnabled(false);
    btnReject.setEnabled(false);
    btnException.setEnabled(false);
    setModified(true);
    txtTotal.setEnabled(false);
    txtNetProfit.setEnabled(false);
    txtNetLoss.setEnabled(false);
    txtTotalExp.setEnabled(false);
    txtToatalIncome.setEnabled(false);
    btnProcess.setEnabled(true);
    btnFinalProcess.setEnabled(true);
    disableTabIndex(tabDCDayBegin.getSelectedIndex());
    btnFinalProcess.setEnabled(true);
    btnAddRecord.setVisible(true);
    btnAddRecord.setEnabled(true);
}//GEN-LAST:event_btnNewActionPerformed

private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
    observable.resetForm();
    observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
    btnAuthorize.setEnabled(false);
    btnReject.setEnabled(false);
    btnException.setEnabled(false);
}//GEN-LAST:event_btnEditActionPerformed

private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
    observable.resetForm();
    observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
    btnAuthorize.setEnabled(false);
    btnReject.setEnabled(false);
    btnException.setEnabled(false);
}//GEN-LAST:event_btnDeleteActionPerformed

private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
    savePerformed();
    btnAuthorize.setEnabled(true);
    btnReject.setEnabled(true);
    btnException.setEnabled(true);
}//GEN-LAST:event_btnSaveActionPerformed
    private void savePerformed() {
        String action;
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            action = CommonConstants.TOSTATUS_INSERT;
            saveAction(action);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            action = CommonConstants.TOSTATUS_UPDATE;
            saveAction(action);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
            action = CommonConstants.TOSTATUS_DELETE;
            saveAction(action);
        }
    }

    private void saveAction(String status) {
        updateOBFields();
        HashMap hs = new HashMap();
        if (tabDCDayBegin.getSelectedIndex() == 0) {
              HashMap dataMap=new HashMap();
              String accHead=CommonUtil.convertObjToStr(tblBalanceUpdate.getModel().getValueAt(0, 0));
              System.out.println("accHead IN ---"+accHead);
              dataMap.put("DATE",DateUtil.getDateMMDDYYYY(tdtFrmDt.getDateValue()));
              dataMap.put("FINAL_ACC_TYPE",CommonUtil.convertObjToStr(cboFinalActType.getSelectedItem()));
              dataMap.put("AC_HEAD",accHead);
              dataMap.put("BRANCH_CODE", CommonUtil.convertObjToStr(observable.getCbmbranch().getKeyForSelected()));
             
              List countData=ClientUtil.executeQuery("getCountForBalanceSheet", dataMap);
              System.out.println("rish......."+countData);
              if(countData!=null && countData.size()>0){
                  HashMap countMap=new HashMap();
                  countMap=(HashMap)countData.get(0);
                  if(countMap!=null && countMap.containsKey("COUNT") && CommonUtil.convertObjToInt(countMap.get("COUNT"))>0){
                      int yesNo = 0;
                      String[] options = {"Yes", "No"};
                      yesNo = COptionPane.showOptionDialog(null,"Already processed.Do you want to continue?", CommonConstants.WARNINGTITLE,
                      COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                      null, options, options[0]);
                       observable.setDeleteKey("N");
                      if(yesNo==0){
                          observable.setDeleteKey("Y");
                      }
                      else{
                          return;
                          }
                  }
              }
              observable.setFinalAccType(CommonUtil.convertObjToStr(cboFinalActType.getSelectedItem()));
              observable.setSelBranchId( CommonUtil.convertObjToStr(observable.getCbmbranch().getKeyForSelected()));
            for (int i = 0; i < tblBalanceUpdate.getModel().getRowCount(); i++) {
                HashMap ls = new HashMap();
                String accheaddes = "" + tblBalanceUpdate.getModel().getValueAt(i, 0);
                String des = "" + tblBalanceUpdate.getModel().getValueAt(i, 1);
                String bal = "" + tblBalanceUpdate.getModel().getValueAt(i, 2);
                ls.put("BRANCH_CODE", CommonUtil.convertObjToStr(observable.getCbmbranch().getKeyForSelected()));
                ls.put("DATE", tdtFrmDt.getDateValue());
                ls.put("FINAL_ACC_TYPE", CommonUtil.convertObjToStr(cboFinalActType.getSelectedItem()));
                ls.put("FINAL_ACC_SUB_TYPE", CommonUtil.convertObjToStr(cboAccountHead.getSelectedItem()));
                ls.put("BALANCE", Math.abs(CommonUtil.convertObjToDouble(bal)));
                ls.put("ACC_HEADS", accheaddes);
                ls.put("TYPE", CommonUtil.convertObjToStr("" + tblBalanceUpdate.getModel().getValueAt(i, 3)));
                ls.put("DES", des);
                ls.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                hs.put(i, ls);
            }
        } else { 
             HashMap dataMap=new HashMap();
              String accHead=CommonUtil.convertObjToStr(tblBalanceSheetPreparation.getModel().getValueAt(0, 2));
              System.out.println("accHead IN ---"+accHead);
              dataMap.put("DATE",DateUtil.getDateMMDDYYYY(tdtFinalDate.getDateValue()));
              dataMap.put("FINAL_ACC_TYPE",CommonUtil.convertObjToStr(cboFinalPrcessType.getSelectedItem()));
              dataMap.put("AC_HEAD",accHead);
              dataMap.put("BRANCH_CODE", CommonUtil.convertObjToStr(observable.getCbmbranch().getKeyForSelected()));
              
              List countData=ClientUtil.executeQuery("getCountForBalanceFinalSheet", dataMap);
              if(countData!=null && countData.size()>0){
                  HashMap countMap=new HashMap();
                  countMap=(HashMap)countData.get(0);
                  if(countMap!=null && countMap.containsKey("COUNT") && CommonUtil.convertObjToInt(countMap.get("COUNT"))>0){
                      int yesNo = 0;
                      String[] options = {"Yes", "No"};
                      yesNo = COptionPane.showOptionDialog(null,"Already processed.Do you want to continue?", CommonConstants.WARNINGTITLE,
                      COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                      null, options, options[0]);
                       observable.setDeleteKey("N");
                      if(yesNo==0){
                          observable.setDeleteKey("Y");
                      }
                      else{
                          return;
                          }
                  }
              }
             observable.setFinalAccType(CommonUtil.convertObjToStr(cboFinalPrcessType.getSelectedItem()));
             observable.setSelBranchId(CommonUtil.convertObjToStr(observable.getCbmbranch().getKeyForSelected()));
            for (int i = 0; i < tblBalanceSheetPreparation.getModel().getRowCount(); i++) {
                HashMap ls = new HashMap();
                String accheaddes = "" + tblBalanceSheetPreparation.getModel().getValueAt(i, 2);
                String des = "" + tblBalanceSheetPreparation.getModel().getValueAt(i, 0) + "-" + tblBalanceSheetPreparation.getModel().getValueAt(i, 1);
                String exp = "" + tblBalanceSheetPreparation.getModel().getValueAt(i, 3);
                String income = "" + tblBalanceSheetPreparation.getModel().getValueAt(i, 4);
                String type = "" + tblBalanceSheetPreparation.getModel().getValueAt(i, 5);
                ls.put("BRANCH_CODE", CommonUtil.convertObjToStr(observable.getCbmbranch().getKeyForSelected()));
                ls.put("DATE", tdtFinalDate.getDateValue());
                ls.put("FINAL_ACC_TYPE", CommonUtil.convertObjToStr(cboFinalPrcessType.getSelectedItem()));
                ls.put("FINAL_ACC_SUB_TYPE", CommonUtil.convertObjToStr(cboAccountHead.getSelectedItem()));
                System.out.println("type  ----------------"+type+"income----"+income+" expp----"+exp);
                String finalType=CommonUtil.convertObjToStr(cboFinalPrcessType.getSelectedItem());
                if(finalType!=null && finalType.equals("BALANCE SHEET")){
                    if (type != null && type.equals("CREDIT")) {
                        ls.put("BALANCE",exp );//income);
                    } else {
                        ls.put("BALANCE",income );
                    }
                }
                else{
                    if (type != null && type.equals("CREDIT")) {
                        ls.put("BALANCE",income );//income);
                    } else {
                        ls.put("BALANCE", exp);
                    }
                }
                ls.put("ACC_HEADS", accheaddes);
                ls.put("TYPE", CommonUtil.convertObjToStr(type));
                ls.put("DES", des);
                ls.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                hs.put(i, ls);
            }
             //adde by sreekrishnan for manual entry
            if(manualMap!=null && manualMap.size()>0){
                observable.setAddMap(manualMap);
            }
        }       
        observable.setTabIndex(tabDCDayBegin.getSelectedIndex());
        observable.setNetLoss(CommonUtil.convertObjToDouble(txtNetLoss.getText()));
        observable.setNetProfit(CommonUtil.convertObjToDouble(txtNetProfit.getText()));
        observable.setBalanceList(hs);
        observable.doAction();
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            HashMap lockMap = new HashMap();
            ArrayList lst = new ArrayList();
            lst.add("PURCHASE_ENTRY_ID");
            lockMap.put(ClientConstants.RECORD_KEY_COL, lst);

            settings();
        }


    }

    public void updateOBFields() {

        observable.setCbobranch(CommonUtil.convertObjToStr(cboBranch.getSelectedItem()));
        observable.setCboFinalActType(CommonUtil.convertObjToStr(cboFinalActType.getSelectedItem()));
        observable.setCboAccountHead(CommonUtil.convertObjToStr(cboAccountHead.getSelectedItem()));
        observable.setTdtFromDate(DateUtil.getDateMMDDYYYY(tdtFrmDt.getDateValue()));
        observable.setFrmDate(tdtFrmDt.getDateValue());
        observable.setFinalFrmDate(tdtFinalDate.getDateValue());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setModule(getModule());
        observable.setScreen(getScreen());


    }

    private void settings() {
        observable.resetForm();
        ClientUtil.clearAll(this);
        observable.setResultStatus();
        btnCancelActionPerformed(null);
    }

    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
    }
    public void disableTabIndex(int tabIndex){
        System.out.println("tabIndex  000====="+tabIndex);
        if(tabIndex==0){
             ((DefaultTableModel) tblBalanceSheetPreparation.getModel()).setRowCount(0);
               cboBranchFinal.setSelectedIndex(0);
               cboBranchFinal.setEnabled(false);
                tdtFinalDate.setDateValue("");
                tdtFinalDate.setEnabled(false);
                cboFinalPrcessType.setSelectedIndex(0);
                cboFinalPrcessType.setEnabled(false);
                cPanel5.setEnabled(false);
        }
        if(tabIndex==1){
            ((DefaultTableModel) tblBalanceUpdate.getModel()).setRowCount(0);
            cboBranch.setSelectedIndex(0);
            cboBranch.setEnabled(false);
            tdtFrmDt.setDateValue("");
            tdtFrmDt.setEnabled(false);
            cboFinalActType.setSelectedIndex(0);
            cboFinalActType.setEnabled(false);
            cboAccountHead.setSelectedItem("");
            cboAccountHead.setEnabled(false);
            cPanel4.setEnabled(false);
        }
    }
private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
    // TODO add your handling code here: 
    observable.resetForm();
    ClientUtil.enableDisable(this, false);
    if (!btnSave.isEnabled()) {
        btnSave.setEnabled(true);
    }
    setButtonEnableDisable();
    observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
    setModified(false);
    btnAuthorize.setEnabled(true);
    btnReject.setEnabled(true);
    btnException.setEnabled(true);
    ((DefaultTableModel) tblBalanceUpdate.getModel()).setRowCount(0);
    ((DefaultTableModel) tblBalanceSheetPreparation.getModel()).setRowCount(0);
    txtTotal.setText("0.0");
    txtNetProfit.setText("0.0");
    txtNetLoss.setText("0.0");
    txtTotalExp.setText("0.0");
    txtToatalIncome.setText("0.0");
    cboBranch.setSelectedIndex(0);
    cboBranchFinal.setSelectedIndex(0);
    tdtFrmDt.setDateValue("");
    tdtFinalDate.setDateValue("");
    cboFinalActType.setSelectedIndex(0);
    cboFinalPrcessType.setSelectedIndex(0);
    cboAccountHead.setSelectedItem("");
    txtTotal.setEnabled(false);
    txtNetProfit.setEnabled(false);
    txtNetLoss.setEnabled(false);
    txtTotalExp.setEnabled(false);
    txtToatalIncome.setEnabled(false);
    viewType = -1;
    isFilled = false;
    btnFinalProcess.setEnabled(false);
    btnAddRecord.setEnabled(false);
    btnProcess.setEnabled(true);
    manualMap = new HashMap();
}//GEN-LAST:event_btnCancelActionPerformed

private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
    // TODO add your handling code here:
    observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
    authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
}//GEN-LAST:event_btnAuthorizeActionPerformed

private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
    // TODO add your handling code here:
    observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
    authorizeStatus(CommonConstants.STATUS_REJECTED);
}//GEN-LAST:event_btnRejectActionPerformed

private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
    // TODO add your handling code here:
    observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
    authorizeStatus(CommonConstants.STATUS_EXCEPTION);
}//GEN-LAST:event_btnExceptionActionPerformed
    public void authorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled) {
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, ClientUtil.getCurrentDate());
            singleAuthorizeMap.put("BAL_SHEET_ID", observable.getBalSheetId());
            if (tabDCDayBegin.getSelectedIndex() == 0) {
                ClientUtil.execute("authBalanceSheet", singleAuthorizeMap);
            } else {
                ClientUtil.execute("authBalanceFinalSheet", singleAuthorizeMap);
            }
            btnCancelActionPerformed(null);
            observable.setResult(observable.getActionType());
            observable.setResultStatus();
        } else {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            //__ To Save the data in the Internal Frame...
            setModified(true);
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);

            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            if (tabDCDayBegin.getSelectedIndex() == 0) {
                mapParam.put(CommonConstants.MAP_NAME, "getSelectBalanceUpdateTOList");
            }
            if (tabDCDayBegin.getSelectedIndex() == 1) {
                mapParam.put(CommonConstants.MAP_NAME, "getSelectBalanceFinalTOList");
            }
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authInventoryMaster");
            //System.out
            isFilled = false;
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            whereMap = null;
        }
    }
    
 	private void setColour() {
        /* Set a cellrenderer to this table in order format the date */
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                 if (colorList.contains(String.valueOf(row))) {
                    setForeground(Color.RED);
                    setBackground(Color.BLACK);
                } else {
                    setForeground(Color.BLACK);
                }
                // Set oquae
                this.setOpaque(true);
                return this;
            }
        };
        tblBalanceSheetPreparation.setDefaultRenderer(Object.class, renderer);
    }
    
private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
    cifClosingAlert();
}//GEN-LAST:event_btnCloseActionPerformed

private void btnItemLstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemLstActionPerformed
    btnSave.setEnabled(false);
    btnCancel.setEnabled(true);
}//GEN-LAST:event_btnItemLstActionPerformed

private void cboFinalActTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboFinalActTypeActionPerformed
    if (cboFinalActType.getSelectedItem().equals("TRADING")) {
        cboAccountHead.removeAllItems();
        cboAccountHead.addItem("");
        cboAccountHead.addItem("CLOSING STOCK");
        cboAccountHead.addItem("DEFICIT");
        cboAccountHead.addItem("DAMAGE");
    } else if (cboFinalActType.getSelectedItem().equals("PROFIT AND LOSS")) {
        cboAccountHead.removeAllItems();
        cboAccountHead.addItem("");
        cboAccountHead.addItem("INTEREST RECEIVED");
        cboAccountHead.addItem("INTEREST PAID");
    } else if (cboFinalActType.getSelectedItem().equals("BALANCE SHEET")) {
        cboAccountHead.removeAllItems();
        cboAccountHead.addItem("");
        cboAccountHead.addItem("INTEREST RECIEVABLE");
        cboAccountHead.addItem("INTEREST PAYABLE");
        cboAccountHead.addItem("RESERVES");
    }
}//GEN-LAST:event_cboFinalActTypeActionPerformed

private void btnFinalProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFinalProcessActionPerformed
   if(cboFinalPrcessType.getSelectedItem()!=null && cboFinalPrcessType.getSelectedItem().equals("BALANCE SHEET"))
      initFinalProcessTableData("Liabilities","Asset");
   else
      initFinalProcessTableData("Expenditure","Income");    
}//GEN-LAST:event_btnFinalProcessActionPerformed

private void cboFinalPrcessTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboFinalPrcessTypeActionPerformed
    // TODO add your handling code here:
        lblNetProfit.setText("Net Profit");
        lblNetloss.setText("Net Loss");
        lblNetloss.setVisible(true);
        txtNetLoss.setVisible(true);
    if(cboFinalPrcessType.getSelectedItem()!=null && cboFinalPrcessType.getSelectedItem().equals("BALANCE SHEET"))
    {
        lblTotExp.setText("Total Liablities");
        lblTotalIncome.setText("Total Asset");
        lblNetProfit.setText("Diff.Amount");
        lblNetloss.setVisible(false);
        txtNetLoss.setVisible(false);
    }
    if(cboFinalPrcessType.getSelectedItem()!=null && cboFinalPrcessType.getSelectedItem().equals("TRADING"))
    {
        lblNetProfit.setText("Gross Profit");
        lblNetloss.setText("Gross Loss");
   }
   if(cboFinalPrcessType.getSelectedItem()!=null && cboFinalPrcessType.getSelectedItem().equals("PROFIT AND LOSS"))
   {
        lblNetProfit.setText("Net Profit");
        lblNetloss.setText("Net Loss");
   } 
}//GEN-LAST:event_cboFinalPrcessTypeActionPerformed

	private void btnAddRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddRecordActionPerformed
        try {
            if(tblBalanceSheetPreparation.getRowCount()>0){
                if(manualMap!=null && manualMap.size()>0){
                    addDataUI.setNetLoss(0.0);
                    addDataUI.setNetProfit(0.0);
                    calcFinalProcessTotal();
                    addDataUI = new addBalanceDataUI(manualMap,CommonUtil.convertObjToStr(cboFinalPrcessType.getSelectedItem()));
                }else{
                    addDataUI = new addBalanceDataUI(CommonUtil.convertObjToStr(cboFinalPrcessType.getSelectedItem()));
                }                
                addDataUI.show();   
                System.out.println("addDataUI.getBalanceData()$^&$&"+addDataUI.getBalanceData());                
                manualMap = addDataUI.getBalanceData();
                calcFinalProcessTotal();
            }else{
                ClientUtil.showAlertWindow("Please Process First!!!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }    
	}//GEN-LAST:event_btnAddRecordActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new BalanceUpdateUI().show();
    }

    public void update(Observable observed, Object arg) {
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAddRecord;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnFinalProcess;
    private com.see.truetransact.uicomponent.CButton btnItemLst;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CPanel cPanel4;
    private com.see.truetransact.uicomponent.CPanel cPanel5;
    private com.see.truetransact.uicomponent.CScrollPane cScrollPane1;
    private com.see.truetransact.uicomponent.CScrollPane cScrollPane2;
    private com.see.truetransact.uicomponent.CComboBox cboAccountHead;
    private com.see.truetransact.uicomponent.CComboBox cboBranch;
    private com.see.truetransact.uicomponent.CComboBox cboBranchFinal;
    private com.see.truetransact.uicomponent.CComboBox cboFinalActType;
    private com.see.truetransact.uicomponent.CComboBox cboFinalPrcessType;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private com.see.truetransact.uicomponent.CLabel lblAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblBalance;
    private com.see.truetransact.uicomponent.CLabel lblBalance1;
    private com.see.truetransact.uicomponent.CLabel lblDate;
    private com.see.truetransact.uicomponent.CLabel lblDate1;
    private com.see.truetransact.uicomponent.CLabel lblFinalAccountType;
    private com.see.truetransact.uicomponent.CLabel lblFinalAccountType1;
    private com.see.truetransact.uicomponent.CLabel lblNetProfit;
    private com.see.truetransact.uicomponent.CLabel lblNetloss;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace34;
    private com.see.truetransact.uicomponent.CLabel lblSpace35;
    private com.see.truetransact.uicomponent.CLabel lblSpace36;
    private com.see.truetransact.uicomponent.CLabel lblSpace37;
    private com.see.truetransact.uicomponent.CLabel lblSpace38;
    private com.see.truetransact.uicomponent.CLabel lblSpace39;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace40;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblTotExp;
    private javax.swing.JLabel lblTotal;
    private com.see.truetransact.uicomponent.CLabel lblTotalIncome;
    private com.see.truetransact.uicomponent.CPanel panBatchProcess1;
    private com.see.truetransact.uicomponent.CPanel panMainSI;
    private com.see.truetransact.uicomponent.CPanel panSI;
    private com.see.truetransact.uicomponent.CPanel panSIID;
    private com.see.truetransact.uicomponent.CPanel panSIIDt;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition1;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition2;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition3;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CPanel panTable1;
    private com.see.truetransact.uicomponent.CButtonGroup rdgAndOr;
    private com.see.truetransact.uicomponent.CSeparator sptLine;
    private com.see.truetransact.uicomponent.CTabbedPane tabDCDayBegin;
    private com.see.truetransact.uicomponent.CTable tblBalanceSheetPreparation;
    private com.see.truetransact.uicomponent.CTable tblBalanceUpdate;
    private javax.swing.JToolBar tbrLoantProduct;
    private com.see.truetransact.uicomponent.CDateField tdtFinalDate;
    private com.see.truetransact.uicomponent.CDateField tdtFrmDt;
    private com.see.truetransact.uicomponent.CTextField txtNetLoss;
    private com.see.truetransact.uicomponent.CTextField txtNetProfit;
    private com.see.truetransact.uicomponent.CTextField txtToatalIncome;
    private javax.swing.JTextField txtTotal;
    private com.see.truetransact.uicomponent.CTextField txtTotalExp;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setMandatoryHashMap() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HashMap getMandatoryHashMap() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
