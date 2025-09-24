/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * BalanceUI.java
 *
 * Created on October 16, 2009, 1:46 PM
 */
package com.see.truetransact.ui.transaction.Balance;

import com.lowagie.text.html.HtmlTagMap;
import com.see.truetransact.ui.transaction.rollback.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Observable;
import java.util.Date;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Toolkit;

import org.apache.log4j.Logger;

import com.see.truetransact.clientutil.EnhancedComboBoxModel;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.CPanel;
import com.see.truetransact.uicomponent.CDialog;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uicomponent.CComboBox;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.viewall.*;
import javax.swing.table.DefaultTableModel;
import java.util.Enumeration;
import java.text.SimpleDateFormat;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import com.see.truetransact.ui.common.viewall.RejectionApproveUI;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.CurrencyValidation;

/**
 * @author Swaroop
 */
public class BalanceUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField {

    private final ViewAllRB resourceBundle = new ViewAllRB();
    private BalanceOB observable;
    HashMap paramMap = null;
    int amtColumnNo = 0;
    double tot = 0;
    private ArrayList categoryTabRow;
    private String viewType = "";
    String amtColName = "";
    String behavesLike = "";
    boolean collDet = false;
    private boolean selectMode = false;
    DefaultTableModel model = null;
    DefaultTreeModel root;
    // private Date currDate = null;
    DefaultTreeModel child;
    Date currDt = null;
    private TableModelListener tableModelListener;
    ArrayList _heading = null;
    ArrayList data = null;
    String node = "";
    RejectionApproveUI rejectionApproveUI = null;
    private final static Logger log = Logger.getLogger(BalanceUI.class);

    /**
     * Creates new form ViewAll
     */
    public BalanceUI() {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        currDt = ClientUtil.getCurrentDate();
        setupInit();
        setupScreen();
        Share_Balance.setVisible(true);
        Deposite_Balance.setVisible(true);
        Investment_Balance.setVisible(true);
        Borrowing_Balance.setVisible(true);
      
    }

    private void setupInit() {
        initComponents();
        internationalize();
        setObservable();
        toFront();
        getUnauthorizedTree();
        observable.fillDropDown();
        cbobranch.setModel(observable.getCbmbranch());
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
            observable = new BalanceOB();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DefaultMutableTreeNode addNode(DefaultMutableTreeNode parent, DefaultMutableTreeNode child, Object obj) {
        if (!chkExistance(parent, obj.toString())) {
            child = new DefaultMutableTreeNode(obj);
        }
        if (!parent.isNodeChild(child)) {
            parent.add(child);
        }
        return child;
    }

    private boolean chkExistance(DefaultMutableTreeNode parent, String chkStr) {
        final Enumeration objEnumeration = parent.children();
        while (objEnumeration.hasMoreElements()) {
            if (objEnumeration.nextElement().toString().equals(chkStr)) {
                return true;
            }
        }
        return false;
    }

    public void show() {
        super.show();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgAndOr = new com.see.truetransact.uicomponent.CButtonGroup();
        try {
            rollBackOB1 = new com.see.truetransact.ui.transaction.rollback.RollBackOB();
        } catch (java.lang.Exception e1) {
            e1.printStackTrace();
        }
        try {
            rollBackOB2 = new com.see.truetransact.ui.transaction.rollback.RollBackOB();
        } catch (java.lang.Exception e1) {
            e1.printStackTrace();
        }
        panSearchCondition = new com.see.truetransact.uicomponent.CPanel();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        cScrollPane1 = new com.see.truetransact.uicomponent.CScrollPane();
        tblproduct = new com.see.truetransact.uicomponent.CTable();
        srcTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();
        panSearchCondition1 = new com.see.truetransact.uicomponent.CPanel();
        Share_Balance = new com.see.truetransact.uicomponent.CButton();
        Deposite_Balance = new com.see.truetransact.uicomponent.CButton();
        Loan_Balance = new com.see.truetransact.uicomponent.CButton();
        Investment_Balance = new com.see.truetransact.uicomponent.CButton();
        Borrowing_Balance = new com.see.truetransact.uicomponent.CButton();
        cbobranch = new com.see.truetransact.uicomponent.CComboBox();
        lblbranch = new com.see.truetransact.uicomponent.CLabel();
        sptLine = new com.see.truetransact.uicomponent.CSeparator();
        panSearch = new com.see.truetransact.uicomponent.CPanel();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        btn_Close = new com.see.truetransact.uicomponent.CButton();
        cButton1 = new com.see.truetransact.uicomponent.CButton();
        cPanel2 = new com.see.truetransact.uicomponent.CPanel();
        From_Date = new com.see.truetransact.uicomponent.CDateField();
        To_Date = new com.see.truetransact.uicomponent.CDateField();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        Btn_Process = new com.see.truetransact.uicomponent.CButton();
        From = new com.see.truetransact.uicomponent.CLabel();
        To = new com.see.truetransact.uicomponent.CLabel();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMaximumSize(new java.awt.Dimension(975, 655));
        setMinimumSize(new java.awt.Dimension(975, 655));
        setPreferredSize(new java.awt.Dimension(975, 655));
        getContentPane().setLayout(new java.awt.GridBagLayout());

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

        tblproduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Select", "Product_Id", "Loan_Products"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblproduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblproductMouseClicked(evt);
            }
        });
        cScrollPane1.setViewportView(tblproduct);

        srcTable.setMaximumSize(new java.awt.Dimension(700, 500));
        srcTable.setMinimumSize(new java.awt.Dimension(700, 500));
        srcTable.setPreferredSize(new java.awt.Dimension(700, 500));
        srcTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                srcTableMouseClicked(evt);
            }
        });

        tblData.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tblData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SLNO", "Description", "Opening_No", "Opening_Amt", "Payment_No", "Payment_Amt", "Receipt_No", "Receipt_Amt", "Balance_No", "Balance_Amt", "Overdue_No", "Overdue_Amt"
            }
        ));
        tblData.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblData.setDragEnabled(true);
        tblData.setMaximumSize(new java.awt.Dimension(0, 500));
        tblData.setMinimumSize(new java.awt.Dimension(0, 500));
        tblData.setPreferredScrollableViewportSize(new java.awt.Dimension(850, 500));
        tblData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDataMousePressed(evt);
            }
        });
        srcTable.setViewportView(tblData);

        javax.swing.GroupLayout panTableLayout = new javax.swing.GroupLayout(panTable);
        panTable.setLayout(panTableLayout);
        panTableLayout.setHorizontalGroup(
            panTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panTableLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(srcTable, javax.swing.GroupLayout.DEFAULT_SIZE, 982, Short.MAX_VALUE)
                .addContainerGap())
        );
        panTableLayout.setVerticalGroup(
            panTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panTableLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE)
                    .addComponent(srcTable, javax.swing.GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE))
                .addContainerGap())
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(panTable, gridBagConstraints);

        Share_Balance.setText("Share Balance");
        Share_Balance.setMaximumSize(new java.awt.Dimension(117, 28));
        Share_Balance.setMinimumSize(new java.awt.Dimension(117, 28));
        Share_Balance.setPreferredSize(new java.awt.Dimension(117, 28));
        Share_Balance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Share_BalanceActionPerformed(evt);
            }
        });

        Deposite_Balance.setText("Deposit Balance");
        Deposite_Balance.setMaximumSize(new java.awt.Dimension(117, 28));
        Deposite_Balance.setMinimumSize(new java.awt.Dimension(117, 28));
        Deposite_Balance.setPreferredSize(new java.awt.Dimension(117, 28));
        Deposite_Balance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Deposite_BalanceActionPerformed(evt);
            }
        });

        Loan_Balance.setText("Loan Balance");
        Loan_Balance.setMaximumSize(new java.awt.Dimension(115, 28));
        Loan_Balance.setMinimumSize(new java.awt.Dimension(115, 28));
        Loan_Balance.setPreferredSize(new java.awt.Dimension(115, 28));
        Loan_Balance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Loan_BalanceActionPerformed(evt);
            }
        });

        Investment_Balance.setText("Investment Balance");
        Investment_Balance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Investment_BalanceActionPerformed(evt);
            }
        });

        Borrowing_Balance.setText("Borrowing Balance");
        Borrowing_Balance.setMaximumSize(new java.awt.Dimension(130, 28));
        Borrowing_Balance.setMinimumSize(new java.awt.Dimension(130, 28));
        Borrowing_Balance.setPreferredSize(new java.awt.Dimension(130, 28));
        Borrowing_Balance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Borrowing_BalanceActionPerformed(evt);
            }
        });

        lblbranch.setText("Branch");

        javax.swing.GroupLayout panSearchCondition1Layout = new javax.swing.GroupLayout(panSearchCondition1);
        panSearchCondition1.setLayout(panSearchCondition1Layout);
        panSearchCondition1Layout.setHorizontalGroup(
            panSearchCondition1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSearchCondition1Layout.createSequentialGroup()
                .addComponent(lblbranch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbobranch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Loan_Balance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(Share_Balance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Deposite_Balance, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Investment_Balance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Borrowing_Balance, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(410, 410, 410))
        );
        panSearchCondition1Layout.setVerticalGroup(
            panSearchCondition1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSearchCondition1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lblbranch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(cbobranch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(Loan_Balance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(Share_Balance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(Deposite_Balance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(Investment_Balance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(Borrowing_Balance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        panSearchCondition.add(panSearchCondition1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(panSearchCondition, gridBagConstraints);

        sptLine.setMinimumSize(new java.awt.Dimension(2, 2));
        sptLine.setPreferredSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(sptLine, gridBagConstraints);

        panSearch.setMinimumSize(new java.awt.Dimension(750, 45));
        panSearch.setPreferredSize(new java.awt.Dimension(750, 45));

        cPanel1.setLayout(new java.awt.GridBagLayout());

        btn_Close.setText("Close");
        btn_Close.setMaximumSize(new java.awt.Dimension(81, 27));
        btn_Close.setMinimumSize(new java.awt.Dimension(81, 27));
        btn_Close.setPreferredSize(new java.awt.Dimension(81, 27));
        btn_Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(12, 61, 12, 1);
        cPanel1.add(btn_Close, gridBagConstraints);

        cButton1.setText("Print");
        cButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        cPanel1.add(cButton1, gridBagConstraints);

        cPanel2.setMinimumSize(new java.awt.Dimension(371, 56));
        cPanel2.setPreferredSize(new java.awt.Dimension(371, 56));
        cPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        cPanel2.add(From_Date, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 10, -1, -1));
        cPanel2.add(To_Date, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 10, -1, -1));

        chkSelectAll.setText("Select All");
        chkSelectAll.setMaximumSize(new java.awt.Dimension(300, 24));
        chkSelectAll.setMinimumSize(new java.awt.Dimension(300, 24));
        chkSelectAll.setPreferredSize(new java.awt.Dimension(300, 24));
        chkSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAllActionPerformed(evt);
            }
        });
        cPanel2.add(chkSelectAll, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 110, 15));

        Btn_Process.setText("Process");
        Btn_Process.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_ProcessActionPerformed(evt);
            }
        });
        cPanel2.add(Btn_Process, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 10, -1, -1));

        From.setText("    From");
        cPanel2.add(From, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 10, 60, 20));

        To.setText("To");
        cPanel2.add(To, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 10, -1, -1));

        javax.swing.GroupLayout panSearchLayout = new javax.swing.GroupLayout(panSearch);
        panSearch.setLayout(panSearchLayout);
        panSearchLayout.setHorizontalGroup(
            panSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSearchLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 515, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 433, Short.MAX_VALUE)
                .addComponent(cPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
        );
        panSearchLayout.setVerticalGroup(
            panSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSearchLayout.createSequentialGroup()
                .addGroup(panSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        getContentPane().add(panSearch, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public DefaultTreeModel getUnauthorizedTree() {
        DefaultMutableTreeNode parent = new DefaultMutableTreeNode("Authorized Records");
        DefaultMutableTreeNode node = new DefaultMutableTreeNode("SB/Current Account Opening");
        parent.add(node);
//        node = new DefaultMutableTreeNode("Customer Master");
//        parent.add(node);
        node = new DefaultMutableTreeNode("SB/Current Account Closing");
        parent.add(node);
        node = new DefaultMutableTreeNode("Deposit Account Opening");
        parent.add(node);
        node = new DefaultMutableTreeNode("Deposit Account Renewal");
        parent.add(node);
        node = new DefaultMutableTreeNode("Deposit Account Closing");
        parent.add(node);
        node = new DefaultMutableTreeNode("Gold Loan Account Opening");
        parent.add(node);
        node = new DefaultMutableTreeNode("Loans/Advances Account Opening");
        parent.add(node);
        node = new DefaultMutableTreeNode("Deposit Loan Account Opening");
        parent.add(node);
        node = new DefaultMutableTreeNode("Loan Account Closing");
        parent.add(node);
        node = new DefaultMutableTreeNode("Cash Transactions");
        parent.add(node);
        node = new DefaultMutableTreeNode("Transfer Transactions");
        parent.add(node);
//        node = new DefaultMutableTreeNode("Share Account");
//        parent.add(node);
        node = new DefaultMutableTreeNode("MDS Receipts");
        parent.add(node);
//        node = new DefaultMutableTreeNode("MDS Payments");
//        parent.add(node);
        final DefaultTreeModel treemodel = new DefaultTreeModel(parent);
//        treData.setModel(treemodel);
        root = null;

        return treemodel;
    }

    public void displayDetails() {
        _heading = null;
        data = null;
        HashMap whereMap = new HashMap();

        List lst = new ArrayList();


        System.out.println("#$#$ whereMap : " + whereMap);
    }
    boolean btnIntDetPressed = false;

    public void fillData(Object obj) {
        HashMap hash = (HashMap) obj;
        System.out.println("#$#$ Hash : " + hash);
    }

    public void populateTable() {
        boolean dataExist;
        if (_heading != null) {
            ArrayList tempList = null;
            for (int i = 0; i < _heading.size(); i++) {
                if (i > 7) {
                    _heading.remove(i);
                    for (int j = 0; j < data.size(); j++) {
                        tempList = (ArrayList) data.get(j);
                        tempList.remove(i);
                    }
                    i--;
                }
            }
            dataExist = true;
            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(tblData);
            TableModel tableModel = new TableModel();
            tableModel.setHeading(_heading);
            tableModel.setData(data);
            tableModel.fireTableDataChanged();
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();
            tblData.setModel(tableSorter);
            tblData.revalidate();
        } else {
            dataExist = false;
            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(tblData);
            TableModel tableModel = new TableModel();
            tableModel.setHeading(new ArrayList());
            tableModel.setData(new ArrayList());
            tableModel.fireTableDataChanged();
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();
            tblData.setModel(tableSorter);
            tblData.revalidate();
            ClientUtil.noDataAlert();
        }
    }

    /**
     * Bring up and populate the temporary project detail screen.
     */
    private void whenTableRowSelected() {
        int rowIndexSelected = tblData.getSelectedRow();
        CInternalFrame frm = null;
        if (rowIndexSelected < 0) {
            COptionPane.showMessageDialog(null,
                    resourceBundle.getString("SelectRow"),
                    resourceBundle.getString("SelectRowHeading"),
                    COptionPane.OK_OPTION + COptionPane.INFORMATION_MESSAGE);
        } else {

            String remarks = COptionPane.showInputDialog(this, "Please enter valid reason.");
            if (remarks != null && remarks.length() == 0) {
                ClientUtil.showMessageWindow("Enter Reason For RollBack");
                return;
            } else if (remarks == null) {
                return;
            }
//            rejectionApproveUI=new RejectionApproveUI(this);
            if (rejectionApproveUI.isCancelActionKey()) {
                return;
            }
            HashMap hash = fillData(rowIndexSelected);
            if (hash == null) {
                return;
            }
            hash.put("ROLL_BACK_SCREEN", node);
            hash.put("ROLL_BACK_REASON", remarks);
            hash.put("AUTHORIZED_BY", ProxyParameters.USER_ID);
            try {
                observable.doActionPerform(hash);
                System.out.println("#$#$ Final Hash : " + hash);
                if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().containsKey("ROLL_BACK_ID")) {
                    ClientUtil.showMessageWindow("RollBack ID : " + CommonUtil.convertObjToStr(observable.getProxyReturnMap().get("ROLL_BACK_ID")));
                    removeSelectedRow();
                } else if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().containsKey("INSUFFICIENT_BALANCE")) {
                    ClientUtil.showMessageWindow("This Account has inSufficient Balance... Can not Rollback Transaction");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void removeSelectedRow() {
        TableSorter tblSorter = (TableSorter) tblData.getModel();
        TableModel tblModel = (TableModel) tblSorter.getModel();
        if (node.equals("Cash Transactions") || node.equals("Transfer Transactions") || node.equals("Loan Account Closing")) {
            ArrayList cashList = tblModel.getDataArrayList();
            ArrayList rowList = (ArrayList) cashList.get(tblData.getSelectedRow());
            String transId = CommonUtil.convertObjToStr(rowList.get(0));
            for (int i = 0; i < cashList.size(); i++) {
                rowList = (ArrayList) cashList.get(i);
                if (transId.equals(CommonUtil.convertObjToStr(rowList.get(0)))) {
                    tblModel.removeRow(i);
                    i--;
                }
            }
        } else {
            tblModel.removeRow(tblData.getSelectedRow());
        }
        tblModel = null;
        tblSorter = null;
    }

    private HashMap fillData(int rowIndexSelected) {
        TableModel _tableModel = (TableModel) tblData.getModel();
        ArrayList rowdata = null;

        if (rowIndexSelected > -1) {
            rowdata = _tableModel.getRow(rowIndexSelected);
            if (node.equals("Cash Transactions") || node.equals("Transfer Transactions")) {
                String prodType = CommonUtil.convertObjToStr(rowdata.get(8));
                String actNum = CommonUtil.convertObjToStr(rowdata.get(2));
                if (rowdata != null && actNum.length() > 0 && (prodType.equals("TL") || prodType.equals("AD")
                        || prodType.equals("GL"))) {
                    HashMap paramMap = new HashMap();
                    paramMap.put("ACT_NUM", actNum);
                    List lst = ClientUtil.executeQuery("getIntDetails", paramMap);
                    if (lst == null || lst.isEmpty()) {
                        lst = ClientUtil.executeQuery("getIntDetailsAD", paramMap);
                    }
                    if (lst != null && lst.size() > 0) {
                        HashMap transMap = (HashMap) lst.get(0);
                        String tabletransId = CommonUtil.convertObjToStr(rowdata.get(0));
                        String transId = CommonUtil.convertObjToStr(transMap.get("TRANS_ID"));
                        if (node.equals("Transfer Transactions")) {
//                      transId=transId.substring(0,transId.endsWith("_"));
                            String retval[] = (String[]) transId.split("_", 2);
                            transId = retval[0];
                            System.out.println("retval##" + retval[0]);
                        }
                        if (!tabletransId.equals(transId)) {
                            ClientUtil.showMessageWindow("Please First Reject Latest Loan Transaction . That  Id is :" + transId);
                            return null;
                        }
                    }
                }
            }
        }

        HashMap hashdata = new HashMap();
        String strColName = null;
        Object obj = null;

        for (int i = 0, j = _tableModel.getColumnCount(); i < j; i++) {
            if (rowdata != null) {
                obj = rowdata.get(i);
            }

            strColName = _tableModel.getColumnName(i).toUpperCase().trim();
            if (obj != null) {
                hashdata.put(strColName, obj);
            } else {
                hashdata.put(strColName, "");
            }
        }
        return hashdata;
    }

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }

    private void Investment_BalanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Investment_BalanceActionPerformed
        // TODO add your handling code here:
        HashMap prodTypeMap = new HashMap();
        chkSelectAll.setSelected(false);
        //prodTypeMap.put("DO_TRANSACTION",new Boolean(true)); 
        prodTypeMap.put(CommonConstants.MAP_NAME, "getInvestmentProductDetails");
        try {
            log.info("populateData...");
            observable.populateData(prodTypeMap, tblproduct);
            tblproduct.setModel(observable.getTblProduct());
            selectMode = true;
            observable.setSelectedButton("INVESTMENT_BALANCE");
            // heading = null;
        } catch (Exception e) {
            System.err.println("Exception " + e.toString() + "Caught");
            e.printStackTrace();
        }
    }//GEN-LAST:event_Investment_BalanceActionPerformed

    private void Share_BalanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Share_BalanceActionPerformed
        // TODO add your handling code here:
        HashMap prodTypeMap = new HashMap();
        chkSelectAll.setSelected(false);
        //prodTypeMap.put("DO_TRANSACTION",new Boolean(true)); 
        prodTypeMap.put(CommonConstants.MAP_NAME, "getShareProductDetails");
        try {
            log.info("populateData...");
            observable.populateData(prodTypeMap, tblproduct);
            tblproduct.setModel(observable.getTblProduct());
            selectMode = true;
            observable.setSelectedButton("SHARE_BALANCE");
            // heading = null;
        } catch (Exception e) {
            System.err.println("Exception " + e.toString() + "Caught");
            e.printStackTrace();
        }
    }//GEN-LAST:event_Share_BalanceActionPerformed

    private void Loan_BalanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Loan_BalanceActionPerformed
        HashMap prodTypeMap = new HashMap();
        chkSelectAll.setSelected(false);
        //prodTypeMap.put("DO_TRANSACTION",new Boolean(true)); 
        prodTypeMap.put(CommonConstants.MAP_NAME, "getLoanProductDetails");
        try {
            log.info("populateData...");
            observable.populateData(prodTypeMap, tblproduct);
            tblproduct.setModel(observable.getTblProduct());
            selectMode = true;
            observable.setSelectedButton("LOAN_BALANCE");
            // heading = null;
        } catch (Exception e) {
            System.err.println("Exception " + e.toString() + "Caught");
            e.printStackTrace();
        }
    }//GEN-LAST:event_Loan_BalanceActionPerformed
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
    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        boolean flag;
        if (chkSelectAll.isSelected() == true) {
            flag = true;
        } else {
            flag = false;
        }
        double totAmount = 0;
        for (int i = 0; i < tblproduct.getRowCount(); i++) {
            tblproduct.setValueAt(new Boolean(flag), i, 0);
            totAmount = totAmount + CommonUtil.convertObjToDouble(tblproduct.getValueAt(i, 2)).doubleValue();
        }
    }//GEN-LAST:event_chkSelectAllActionPerformed

    private void Btn_ProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_ProcessActionPerformed
        Date fromDate = DateUtil.getDateMMDDYYYY(From_Date.getDateValue());
        Date toDate = DateUtil.getDateMMDDYYYY(To_Date.getDateValue());
        if (fromDate != null && toDate != null) {
            HashMap prodTypeMap = new HashMap();
            HashMap whereMap = new HashMap();
            String PROD_ID = "";
            for (int i = 0; i < tblproduct.getRowCount(); i++) {
                if (((Boolean) tblproduct.getValueAt(i, 0)).booleanValue()) {
                    //System.out.println("PROD_ID.length=====" + PROD_ID.length());
                    if (i == 0) {
                        PROD_ID = PROD_ID + "'" + tblproduct.getValueAt(i, 2).toString() + "'";
                    } else {
                        PROD_ID = PROD_ID + ",'" + tblproduct.getValueAt(i, 2).toString() + "'";
                    }
                }
            }
            System.out.println("PROD_ID==============" + PROD_ID);
            System.out.println("bbbb============" + PROD_ID.charAt(0));
            // }
            if (PROD_ID != null && PROD_ID.charAt(0) == ',') {
                PROD_ID = PROD_ID.substring(1, PROD_ID.length());
            }
              String BranchCode = ((ComboBoxModel) cbobranch.getModel()).getKeyForSelected().toString();
            System.out.println("PRODD IDD========" + PROD_ID);
            whereMap.put("PROD_ID", PROD_ID);
            whereMap.put("BRANCH_CODE",BranchCode);
            whereMap.put("From_Date", setProperDtFormat(DateUtil.getDateMMDDYYYY(From_Date.getDateValue())));
            whereMap.put("To_Date", setProperDtFormat(DateUtil.getDateMMDDYYYY(To_Date.getDateValue())));
            System.out.println("whereMap========="+whereMap);
            prodTypeMap.put(CommonConstants.MAP_WHERE, whereMap);
            if (observable.getSelectedButton().equals("LOAN_BALANCE")) {
                prodTypeMap.put(CommonConstants.MAP_NAME, "getLoanAccountDetailsList");
            } else if (observable.getSelectedButton().equals("SHARE_BALANCE")) {
                prodTypeMap.put(CommonConstants.MAP_NAME, "getShareAccountDetailsList");
            } else if (observable.getSelectedButton().equals("DEPOSIT_BALANCE")) {
                prodTypeMap.put(CommonConstants.MAP_NAME, "getDepositAccountDetailsList");
            } else if (observable.getSelectedButton().equals("INVESTMENT_BALANCE")) {
                prodTypeMap.put(CommonConstants.MAP_NAME, "getInvestmentAccountDetailsList");
            } else if (observable.getSelectedButton().equals("BORROWING_BALANCE")) {
                prodTypeMap.put(CommonConstants.MAP_NAME, "getBorrowingAccountDetailsList");
            }
            try {
                log.info("populateData...");
                ArrayList heading = observable.populateData1(prodTypeMap, tblData);
                if (heading != null) {
                    EnhancedComboBoxModel cboModel = new EnhancedComboBoxModel(heading);
                }
                selectMode = true;
            } catch (Exception e) {
                System.err.println("Exception " + e.toString() + "Caught");
                e.printStackTrace();
            }
        } else {
            if (fromDate == null) {
                ClientUtil.showMessageWindow("From date should not be empty");
                return;
            }
            if (toDate == null) {
                ClientUtil.showMessageWindow("To date should not be empty");
                return;
            }
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_Btn_ProcessActionPerformed

    private void btn_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CloseActionPerformed

        {
            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            javax.swing.JMenu mnu = TrueTransactMain.getMenu();
            String tit = "";
            try {
                tit = this.title.substring(0, (this.title.indexOf("[")) - 1);
            } catch (Exception e) {
                tit = this.title.trim();
            }
            if (tit.length() > 0) {
                int avail = 0;
                for (int m = 4; m < mnu.getItemCount(); m++) {
                    if (mnu.getItem(m).getText().equals(tit)) {
                        avail++;
                        mnu.remove(mnu.getItem(m));
                    }
                }
            }
            this.dispose();
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_CloseActionPerformed

    private void tblproductMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblproductMouseClicked
        if (selectMode == true) {
            String st = CommonUtil.convertObjToStr(tblproduct.getValueAt(tblproduct.getSelectedRow(), 0));
            if (st.equals("true")) {
                tblproduct.setValueAt(new Boolean(false), tblproduct.getSelectedRow(), 0);
            } else {
                tblproduct.setValueAt(new Boolean(true), tblproduct.getSelectedRow(), 0);
            }
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_tblproductMouseClicked

private void Deposite_BalanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Deposite_BalanceActionPerformed
// TODO add your handling code here:
    HashMap prodTypeMap = new HashMap();
    chkSelectAll.setSelected(false);
    prodTypeMap.put(CommonConstants.MAP_NAME, "getDepositProductDetails");
    try {
        log.info("populateData...");
        observable.populateData(prodTypeMap, tblproduct);
        tblproduct.setModel(observable.getTblProduct());
        selectMode = true;
        observable.setSelectedButton("DEPOSIT_BALANCE");
        // heading = null;
    } catch (Exception e) {
        System.err.println("Exception " + e.toString() + "Caught");
        e.printStackTrace();
    }
}//GEN-LAST:event_Deposite_BalanceActionPerformed

private void Borrowing_BalanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Borrowing_BalanceActionPerformed
// TODO add your handling code here:
    HashMap prodTypeMap = new HashMap();
    chkSelectAll.setSelected(false);
    prodTypeMap.put(CommonConstants.MAP_NAME, "getBorrowingProductDetails");
    try {
        log.info("populateData...");
        observable.populateData(prodTypeMap, tblproduct);
        tblproduct.setModel(observable.getTblProduct());
        selectMode = true;
        observable.setSelectedButton("BORROWING_BALANCE");
    } catch (Exception e) {
        System.err.println("Exception " + e.toString() + "Caught");
        e.printStackTrace();
    }
}//GEN-LAST:event_Borrowing_BalanceActionPerformed

    private void tblDataMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMousePressed
        // TODO add your handling code here:        
    }//GEN-LAST:event_tblDataMousePressed

    private void srcTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_srcTableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_srcTableMouseClicked

    private void cButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cButton1ActionPerformed
        try
        {   
            tblData.print();
        }
        catch(Exception e)
        {
            System.out.println("Exception"+e);
        }
    }//GEN-LAST:event_cButton1ActionPerformed

    private ComboBoxModel getListModel() {
        ComboBoxModel listData = new ComboBoxModel();
        return listData;
    }

    private void internationalize() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new BalanceUI().show();
    }

    public void update(Observable observed, Object arg) {
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton Borrowing_Balance;
    private com.see.truetransact.uicomponent.CButton Btn_Process;
    private com.see.truetransact.uicomponent.CButton Deposite_Balance;
    private com.see.truetransact.uicomponent.CLabel From;
    private com.see.truetransact.uicomponent.CDateField From_Date;
    private com.see.truetransact.uicomponent.CButton Investment_Balance;
    private com.see.truetransact.uicomponent.CButton Loan_Balance;
    private com.see.truetransact.uicomponent.CButton Share_Balance;
    private com.see.truetransact.uicomponent.CLabel To;
    private com.see.truetransact.uicomponent.CDateField To_Date;
    private com.see.truetransact.uicomponent.CButton btn_Close;
    private com.see.truetransact.uicomponent.CButton cButton1;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CPanel cPanel2;
    private com.see.truetransact.uicomponent.CScrollPane cScrollPane1;
    private com.see.truetransact.uicomponent.CComboBox cbobranch;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblbranch;
    private com.see.truetransact.uicomponent.CPanel panSearch;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition1;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CButtonGroup rdgAndOr;
    private com.see.truetransact.ui.transaction.rollback.RollBackOB rollBackOB1;
    private com.see.truetransact.ui.transaction.rollback.RollBackOB rollBackOB2;
    private com.see.truetransact.uicomponent.CSeparator sptLine;
    private com.see.truetransact.uicomponent.CScrollPane srcTable;
    private com.see.truetransact.uicomponent.CTable tblData;
    private com.see.truetransact.uicomponent.CTable tblproduct;
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
