/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * RollBackUI.java
 *
 * Created on October 16, 2009, 1:46 PM
 */

package com.see.truetransact.ui.transaction.rollback;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Observable;
import java.util.Date;
import java.util.TreeSet;
import javax.swing.plaf.ColorUIResource;

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
/**
 * @author  Swaroop
 */
public class RollBackUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer {
    private final ViewAllRB resourceBundle = new ViewAllRB();
    private RollBackOB observable;
    HashMap paramMap = null;
    int amtColumnNo=0;
    double tot = 0;
    String amtColName = "";
    String behavesLike = "";
    boolean collDet=false;
    
    DefaultTableModel model = null;
    DefaultTreeModel root;
    DefaultTreeModel child;
    Date currDt = null;
    private TableModelListener tableModelListener;
    ArrayList _heading = null;
    ArrayList data = null;
    String node = "";
    RejectionApproveUI rejectionApproveUI=null;
    private final static Logger log = Logger.getLogger(RollBackUI.class);
    
    
    /** Creates new form ViewAll */
    public RollBackUI() {
        try {
      //  javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            
        }
        currDt = ClientUtil.getCurrentDate();
        setupInit();
        setupScreen();
    }

    private void setupInit() {
        initComponents();
        internationalize();
        setObservable();
        toFront();
        getUnauthorizedTree();
        btnRollBack.setEnabled(false);
    }
    
    private void setupScreen() {
//        setModal(true);
        
        /* Calculate the screen size */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        /* Center frame on the screen */
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }
    
    private void setObservable() {
        try {
            observable = new RollBackOB();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private DefaultMutableTreeNode addNode(DefaultMutableTreeNode parent,DefaultMutableTreeNode child,Object obj) {
        if (!chkExistance(parent,obj.toString())){
            child = new DefaultMutableTreeNode(obj);
        }
        if (!parent.isNodeChild(child)){
            parent.add(child);
        }
        return child;
    }
    

    private boolean chkExistance(DefaultMutableTreeNode parent,String chkStr) {
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
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        rdgAndOr = new com.see.truetransact.uicomponent.CButtonGroup();
        panSearchCondition = new com.see.truetransact.uicomponent.CPanel();
        panTree = new com.see.truetransact.uicomponent.CPanel();
        cScrollPane1 = new com.see.truetransact.uicomponent.CScrollPane();
        treData = new javax.swing.JTree();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        srcTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();
        panSearchCondition1 = new com.see.truetransact.uicomponent.CPanel();
        lblSearch = new com.see.truetransact.uicomponent.CLabel();
        cboSearchCol = new com.see.truetransact.uicomponent.CComboBox();
        btnSearch = new com.see.truetransact.uicomponent.CButton();
        cboSearchCriteria = new com.see.truetransact.uicomponent.CComboBox();
        txtSearchData = new javax.swing.JTextField();
        chkCase = new com.see.truetransact.uicomponent.CCheckBox();
        sptLine = new com.see.truetransact.uicomponent.CSeparator();
        panSearch = new com.see.truetransact.uicomponent.CPanel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnRollBack = new com.see.truetransact.uicomponent.CButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(850, 655));
        setPreferredSize(new java.awt.Dimension(850, 655));
        panSearchCondition.setLayout(new java.awt.GridBagLayout());

        panSearchCondition.setMinimumSize(new java.awt.Dimension(700, 600));
        panSearchCondition.setPreferredSize(new java.awt.Dimension(700, 600));
        panTree.setLayout(new java.awt.GridBagLayout());

        panTree.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        panTree.setMinimumSize(new java.awt.Dimension(200, 450));
        panTree.setPreferredSize(new java.awt.Dimension(200, 450));
        cScrollPane1.setMinimumSize(new java.awt.Dimension(700, 680));
        cScrollPane1.setPreferredSize(new java.awt.Dimension(700, 680));
        treData.setExpandsSelectedPaths(false);
        treData.setMaximumSize(new java.awt.Dimension(71, 60));
        treData.setModel(root);
        treData.setPreferredSize(new java.awt.Dimension(71, 60));
        treData.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                treDataValueChanged(evt);
            }
        });

        cScrollPane1.setViewportView(treData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        panTree.add(cScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panSearchCondition.add(panTree, gridBagConstraints);

        panTable.setLayout(new java.awt.GridBagLayout());

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

        srcTable.setViewportView(tblData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTable.add(srcTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(panTable, gridBagConstraints);

        panSearchCondition1.setLayout(new java.awt.GridBagLayout());

        lblSearch.setText("Search");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition1.add(lblSearch, gridBagConstraints);

        cboSearchCol.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition1.add(cboSearchCol, gridBagConstraints);

        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_FIND.gif")));
        btnSearch.setText("Find");
        btnSearch.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        panSearchCondition1.add(btnSearch, gridBagConstraints);

        cboSearchCriteria.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Starts with", "Ends with", "Exact Match", "Pattern Match" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition1.add(cboSearchCriteria, gridBagConstraints);

        txtSearchData.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        panSearchCondition1.add(txtSearchData, gridBagConstraints);

        chkCase.setText("Match Case");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition1.add(chkCase, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
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

        panSearch.setLayout(new java.awt.GridBagLayout());

        panSearch.setMinimumSize(new java.awt.Dimension(750, 40));
        panSearch.setPreferredSize(new java.awt.Dimension(750, 40));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif")));
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panSearch.add(btnClose, gridBagConstraints);

        btnRollBack.setForeground(new java.awt.Color(255, 0, 51));
        btnRollBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif")));
        btnRollBack.setText("Roll Back");
        btnRollBack.setFont(new java.awt.Font("MS Sans Serif", 1, 13));
        btnRollBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRollBackActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panSearch.add(btnRollBack, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        getContentPane().add(panSearch, gridBagConstraints);

        pack();
    }//GEN-END:initComponents

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
            String searchTxt = txtSearchData.getText().trim();
            if (searchTxt.length()<=0) {
                if (observable.getDataSize()>=observable.MAXDATA) {
                    ClientUtil.showAlertWindow("You have not entered the search String");
                    txtSearchData.requestFocus();
                    return;
                } else {
                    observable.populateTable();
                    return;
                }
            } 
            if (!chkCase.isSelected()) searchTxt = searchTxt.toUpperCase();
            int selCol = cboSearchCol.getSelectedIndex();
            int selColCri = cboSearchCriteria.getSelectedIndex();
            observable.searchData(searchTxt, selCol, selColCri, chkCase.isSelected());
    }//GEN-LAST:event_btnSearchActionPerformed

    private void treDataValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_treDataValueChanged
        // TODO add your handling code here:
        TreePath oldPath = evt.getOldLeadSelectionPath();
        TreePath newPath = evt.getNewLeadSelectionPath();
        DefaultMutableTreeNode lastNode = null;
        if(oldPath != null)
            lastNode = (DefaultMutableTreeNode)oldPath.getLastPathComponent();
        DefaultMutableTreeNode newNode = null;
        if(newPath != null)
            newNode = (DefaultMutableTreeNode)newPath.getLastPathComponent();
        if(newNode!=null) {
            node = newNode.toString();
            System.out.println("last selected node = " + lastNode + "\n" +
            "new selected node = " + newNode + "\n\n");
            if (!node.equals("Authorized Records")) {
                displayDetails();
            }
        }
    }//GEN-LAST:event_treDataValueChanged

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
        node = new DefaultMutableTreeNode("Trade Expense");
        parent.add(node);
//        node = new DefaultMutableTreeNode("Share Account");
//        parent.add(node);
        node = new DefaultMutableTreeNode("MDS Receipts");
        parent.add(node);
        node = new DefaultMutableTreeNode("Indend Transactions");
        parent.add(node);
        node = new DefaultMutableTreeNode("Accounts with other bank Master");
        parent.add(node);
        node = new DefaultMutableTreeNode("Investment Master");
        parent.add(node);     
        // Commented by nithya on 29-05-2021 for KD-2433 [ Since investment rollback is not functioning, the click event makes the 
        // system hang- Commenting the lines till the completion of development - On Discussion with Rishad
        //node = new DefaultMutableTreeNode("Investment Transaction");
        //parent.add(node);
        node = new DefaultMutableTreeNode("Multiple Cash Transaction");
        parent.add(node);
//        node = new DefaultMutableTreeNode("MDS Payments");
//        parent.add(node);
        final DefaultTreeModel treemodel = new DefaultTreeModel(parent);
        treData.setModel(treemodel);
        root = null;
        
        return treemodel;
    }

    public void displayDetails() {
        _heading = null;
        data = null;
        HashMap whereMap = new HashMap();
        HashMap where = new HashMap();
        where.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        where.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        where.put(CommonConstants.HIERARCHY_ID, ProxyParameters.HIERARCHY_ID);
        List lst = new ArrayList();
        //        if (node.equals("Customer Master")) {
        //            whereMap.put(CommonConstants.MAP_NAME, "getUnAuthorizedListForCustomer");
        //            ClientUtil.showAlertWindow("Under Construction...");
        //            return;
        //        } else
        if (node.equals("SB/Current Account Opening")) {
            where.put("TRANS_DT", currDt);
            whereMap.put(CommonConstants.MAP_NAME, "getSelectOperativeActOpeningList");
        } else if (node.equals("SB/Current Account Closing")) {
            where.put("TRANS_DT", currDt);
            whereMap.put(CommonConstants.MAP_NAME, "getSelectOperativeActClosingList");
        } else if (node.equals("Deposit Account Opening")) {
            where.put("TRANS_DT", currDt);
            whereMap.put(CommonConstants.MAP_NAME, "getSelectDepositOpeningList");
        } else if (node.equals("Deposit Account Renewal")) {
            where.put("OPENING_MODE", "Renewal");
            where.put("CURR_DATE", currDt);
            where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
            whereMap.put(CommonConstants.MAP_NAME, "getSelectDepositRenewalRollBackList");
        } else if (node.equals("Deposit Account Closing")) {
            where.put("TRANS_DT", currDt);
            whereMap.put(CommonConstants.MAP_NAME, "getSelectDepositClosingList");
        } else if (node.equals("Gold Loan Account Opening")) {
            where.put("AUTHORIZE_REMARK", "= 'OTHER_LOAN'");
            where.put("LOAN_OPENING","LOAN_OPENING");
            where.put("AUTHORIZESTATUS", "AUTHORIZED");
            where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            where.put("STATUS_BY", ProxyParameters.USER_ID);
            where.put("CURR_DATE", currDt);
            whereMap.put(CommonConstants.MAP_NAME, "getSelectTermLoanRollBackList");
        } else if (node.equals("Loans/Advances Account Opening")) {
            where.put("AUTHORIZE_REMARK", "= 'GOLD_LOAN'");
            where.put("AUTHORIZESTATUS", "AUTHORIZED");
            where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            where.put("STATUS_BY", ProxyParameters.USER_ID);
            where.put("CURR_DATE", currDt);
            whereMap.put(CommonConstants.MAP_NAME, "getSelectTermLoanRollBackList");
        } else if (node.equals("Deposit Loan Account Opening")) {
            where.put("AUTHORIZESTATUS", "AUTHORIZED");
            where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            where.put("STATUS_BY", ProxyParameters.USER_ID);
            where.put("CURR_DATE", currDt);
            whereMap.put(CommonConstants.MAP_NAME, "viewTermLoanForLTDForRollBack");
        } else if (node.equals("Loan Account Closing")) {
            where.put(CommonConstants.BRANCH_ID,TrueTransactMain.selBranch);
            where.put("CURR_DATE", currDt);
            whereMap.put(CommonConstants.MAP_NAME, "getSelectLoanAccountCloseRollBackTOList");
            observable.clearTable(tblData);
        } else if (node.equals("Cash Transactions")) {
            where.put("TRANS_DT", currDt);
            whereMap.put(CommonConstants.MAP_NAME, "getSelectCashTransactionRollBackTOList");
        } else if (node.equals("Transfer Transactions")) {
             where.put("TRANS_DT", currDt);
            where.put("AUTHORIZESTATUS", "AUTHORIZED");
            whereMap.put(CommonConstants.MAP_NAME, "getSelectTransferTransactionRollBackTOList");
        } else if (node.equals("MDS Receipts")) {
            where.put("TRANS_DT", currDt);
            whereMap.put(CommonConstants.MAP_NAME, "getSelectMDSReceiptList");
        }
        else if (node.equals("Indend Transactions")) {
            where.put("TRANS_DT", currDt);
            whereMap.put(CommonConstants.MAP_NAME, "getSelectIndendTrans");
        }else if (node.equals("Accounts with other bank Master")) {
            where.put("TRANS_DT", currDt);
            whereMap.put(CommonConstants.MAP_NAME, "getABDetails");
        }else if (node.equals("Investment Master")) {
            where.put("TRANS_DT", currDt);
            whereMap.put(CommonConstants.MAP_NAME, "getInvDetails");
        }
        // Commented by nithya on 29-05-2021 for KD-2433 [ Since investment rollback is not functioning, the click event makes the 
        // system hang- Commenting the lines till the completion of development - On Discussion with Rishad
        /*else if (node.equals("Investment Transaction")) {
            where.put("TRANS_DT", currDt);
            whereMap.put(CommonConstants.MAP_NAME, "getInvTransDetails");
        }*/ 
        else if(node.equals("Multiple Cash Transaction")){
            where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
            where.put("TRANS_DT", currDt);
            whereMap.put(CommonConstants.MAP_NAME, "getMultipleCashTransactionRollBackList");
        } else if (node.equals("Trade Expense")) {
            //where.put("null",null);
            whereMap.put(CommonConstants.MAP_NAME, "PurchaseEntry.getSelectTradeExpenseList");
        }
//        else if (node.equals("Share Account")) {
//            where.put("AUTHORIZESTATUS", "AUTHORIZED");
//            where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
//            whereMap.put(CommonConstants.MAP_NAME, "viewAllShareAcctAuthorizeTOList");
//            ClientUtil.showAlertWindow("Under Construction...");
//            return;
//        } else if (node.equals("MDS Payments")) {
//            whereMap.put(CommonConstants.MAP_NAME, "getPrizedMoneyPaymentAuthorize");
//            ClientUtil.showAlertWindow("Under Construction...");
//            return;
//        }
        
        whereMap.put(CommonConstants.MAP_WHERE, where);
        if (whereMap.containsKey(CommonConstants.MAP_NAME)) {
            ArrayList heading = observable.populateData(whereMap, tblData);
            if (heading!=null) {
                EnhancedComboBoxModel cboModel = new EnhancedComboBoxModel(heading);
                cboSearchCol.setModel(cboModel);
            }
            if(observable.getDataSize()>0){
                btnRollBack.setEnabled(true);
            }else{
                 btnRollBack.setEnabled(false);
            }
//            whereMap = ClientUtil.executeTableQuery(whereMap);
//            _heading = (ArrayList) whereMap.get(CommonConstants.TABLEHEAD);
//            data = (ArrayList) whereMap.get(CommonConstants.TABLEDATA);
        }
//        HashMap hash = (HashMap) obj;
        System.out.println("#$#$ whereMap : "+whereMap);
    }
    
    boolean btnIntDetPressed = false;
    public void fillData(Object obj) {
        HashMap hash = (HashMap) obj;
        System.out.println("#$#$ Hash : "+hash);
    }
    
    public void populateTable() {
        boolean dataExist;
        if (_heading != null){
            ArrayList tempList = null;
            for (int i=0; i<_heading.size(); i++) {
                if (i>7) {
                    _heading.remove(i);
                    for (int j=0; j<data.size(); j++) {
                        tempList = (ArrayList)data.get(j);
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
        }else{
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
    private boolean checkWhetherDataExsistInRenewal() {
        boolean check = false;
        HashMap where = new HashMap();
        where.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        where.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        where.put(CommonConstants.HIERARCHY_ID, ProxyParameters.HIERARCHY_ID);
        where.put("OPENING_MODE", "Renewal");
        where.put("TRANS_DT", currDt);
        where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
        TableSorter tblSorter = (TableSorter) tblData.getModel();
        TableModel tblModel = (TableModel) tblSorter.getModel();
        ArrayList cashList = tblModel.getDataArrayList();
        ArrayList rowList = (ArrayList) cashList.get(tblData.getSelectedRow());
        String actNo = CommonUtil.convertObjToStr(rowList.get(0));
        where.put("ACT_NUM", actNo);
        List rollBackList = ClientUtil.executeQuery("getSelectDepositClosingList", where);
        if (rollBackList != null && rollBackList.size() > 0) {
            ClientUtil.showAlertWindow("RollBack not possible (RollBack Deposit Closing First)");
            check = true;
        } else {
            check = false;
        }
        return check;
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
            
            String remarks = COptionPane.showInputDialog(this,"Please enter valid reason.");
            if(remarks !=null && remarks.length()==0){
                ClientUtil.showMessageWindow("Enter Reason For RollBack");
                return;
            }else if(remarks==null){
                return;
            }
            rejectionApproveUI=new RejectionApproveUI(this);
            if(rejectionApproveUI.isCancelActionKey()){
                return;
            }
            HashMap hash = fillData(rowIndexSelected);
            if(hash ==null){
                return;
            }
            if(node != null && node.equalsIgnoreCase("Deposit Account Renewal")){
                if(checkWhetherDataExsistInRenewal()){
                return;
                }
            }
            hash.put("ROLL_BACK_SCREEN", node);
            hash.put("ROLL_BACK_REASON", remarks);
            hash.put("AUTHORIZED_BY", ProxyParameters.USER_ID);
            if(TrueTransactMain.SERVICE_TAX_REQ.equals("Y")){
                 hash.put("SERVICE_TAX_ROLLBACK", "SERVICE_TAX_ROLLBACK");
            }
            try{
                observable.doActionPerform(hash);
                System.out.println("#$#$ Final Hash : "+hash);
                if (observable.getProxyReturnMap()!=null && observable.getProxyReturnMap().containsKey("ROLL_BACK_ID")) {
                    ClientUtil.showMessageWindow("RollBack ID : " + CommonUtil.convertObjToStr(observable.getProxyReturnMap().get("ROLL_BACK_ID")));
                    removeSelectedRow();
                }else if (observable.getProxyReturnMap()!=null && observable.getProxyReturnMap().containsKey("INSUFFICIENT_BALANCE")) {
                    ClientUtil.showMessageWindow("This Account has Insufficient Balance... Can not Rollback Transaction");
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public void removeSelectedRow() {
        TableSorter tblSorter = (TableSorter)tblData.getModel();
        TableModel tblModel = (TableModel) tblSorter.getModel();
        if (node.equals("Cash Transactions") || node.equals("Transfer Transactions") || node.equals("Loan Account Closing")) {
            ArrayList cashList = tblModel.getDataArrayList();
            ArrayList rowList = (ArrayList)cashList.get(tblData.getSelectedRow());
            String transId = CommonUtil.convertObjToStr(rowList.get(0));
            for (int i=0; i<cashList.size(); i++) {
                rowList = (ArrayList)cashList.get(i);
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
            //added by rishad 21/mar/2018 query running based on trans_id (reason :they click row main leg or subsidery leg)
            String prodType = null;
            String actNum = null;
            if (node.equals("Cash Transactions") || node.equals("Transfer Transactions")) {
                prodType = CommonUtil.convertObjToStr(rowdata.get(8));
                if (rowdata != null && (prodType.equals("OA")||prodType.equals("GL"))) {
                    HashMap paramMap = new HashMap();
                    paramMap.put("TRANS_DT", currDt);
                    paramMap.put("TRANS_ID", CommonUtil.convertObjToStr(rowdata.get(0)));
                    List lst = ClientUtil.executeQuery("getOperativePassBookEntry", paramMap);
                    String transId = null;
                    if (lst != null && lst.size() > 0) {
                        for (int i = 0; i < lst.size(); i++) {
                            HashMap resultMap = (HashMap) lst.get(i);
                            if (i == 0) {
                                transId = CommonUtil.convertObjToStr(resultMap.get("TRANS_ID"));
                            } else {
                                transId += "," + CommonUtil.convertObjToStr(resultMap.get("TRANS_ID"));
                            }
                        }
                        ClientUtil.showMessageWindow("Please First Reject Following  Transaction . That  Id is :" + transId);
                        return null;
                    }
                }
            } else if (node.equals("SB/Current Account Opening")) {
                prodType = CommonUtil.convertObjToStr(rowdata.get(6));
                actNum = CommonUtil.convertObjToStr(rowdata.get(0));
                if (rowdata != null && actNum.length() > 0 && (prodType.equals("OA"))) {
                    HashMap paramMap = new HashMap();
                    paramMap.put("ACT_NUM", actNum);
                    paramMap.put("TRANS_DT", currDt);
                    List lst = ClientUtil.executeQuery("getOperativeTxnPassBookEntry", paramMap);
                    String transId = null;
                    if (lst != null && lst.size() > 0) {
                        for (int i = 0; i < lst.size(); i++) {
                            HashMap resultMap = (HashMap) lst.get(i);
                            if (i == 0) {
                                transId = CommonUtil.convertObjToStr(resultMap.get("TRANS_ID"));
                            } else {
                                transId += "," + CommonUtil.convertObjToStr(resultMap.get("TRANS_ID"));
                            }
                        }
                        ClientUtil.showMessageWindow("Please First Reject Following  Transaction . That  Id is :" + transId);
                        return null;
                    }
                }
            }
        }
        
        HashMap hashdata = new HashMap();
        String strColName = null;
        Object obj = null;
        
        for (int i = 0, j = _tableModel.getColumnCount(); i < j; i++) {
            if (rowdata != null)
                obj = rowdata.get(i);
            
            strColName = _tableModel.getColumnName(i).toUpperCase().trim();
            if (obj != null) {
                hashdata.put(strColName, obj);
            } else {
                hashdata.put(strColName, "");
            }
        }
        return hashdata;
    }
    
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    
    
    private void tblDataMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMousePressed

    }//GEN-LAST:event_tblDataMousePressed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnRollBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRollBackActionPerformed
//        observable.resetForm();
//        update(observable, null);
        int rowIndexSelected = tblData.getSelectedRow();
         if (rowIndexSelected < 0) {
            COptionPane.showMessageDialog(null,
            resourceBundle.getString("SelectRow"),
            resourceBundle.getString("SelectRowHeading"),
            COptionPane.OK_OPTION + COptionPane.INFORMATION_MESSAGE);
            return;
         }
        int confirm = ClientUtil.confirmationAlert("Are you sure to Roll Back ?");
        if (confirm == 0) {
            whenTableRowSelected();
        }
            TrueTransactMain.populateBranches();
            TrueTransactMain.selBranch = ProxyParameters.BRANCH_ID;
            setSelectedBranchID(ProxyParameters.BRANCH_ID);
    }//GEN-LAST:event_btnRollBackActionPerformed
    
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
        new RollBackUI().show();
    }
    
    public void update(Observable observed, Object arg) {
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnRollBack;
    private com.see.truetransact.uicomponent.CButton btnSearch;
    private com.see.truetransact.uicomponent.CScrollPane cScrollPane1;
    private com.see.truetransact.uicomponent.CComboBox cboSearchCol;
    private com.see.truetransact.uicomponent.CComboBox cboSearchCriteria;
    private com.see.truetransact.uicomponent.CCheckBox chkCase;
    private com.see.truetransact.uicomponent.CLabel lblSearch;
    private com.see.truetransact.uicomponent.CPanel panSearch;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition1;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CPanel panTree;
    private com.see.truetransact.uicomponent.CButtonGroup rdgAndOr;
    private com.see.truetransact.uicomponent.CSeparator sptLine;
    private com.see.truetransact.uicomponent.CScrollPane srcTable;
    private com.see.truetransact.uicomponent.CTable tblData;
    private javax.swing.JTree treData;
    private javax.swing.JTextField txtSearchData;
    // End of variables declaration//GEN-END:variables
}

