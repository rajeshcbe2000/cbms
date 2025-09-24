/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AuthorizeListDebitUI.java
 *
 * Created on October 16, 2009, 1:46 PM
 */
package com.see.truetransact.ui.salaryrecovery;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.*;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.viewall.NonFinTransactionOB;
import com.see.truetransact.ui.common.viewall.ViewAllRB;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.COptionPane;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.*;
import java.util.logging.Level;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;

/**
 * @author Swaroop
 */
public class AuthorizeListDebitUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer {

    private final ViewAllRB resourceBundle = new ViewAllRB();
    private NonFinTransactionOB observable;
    HashMap paramMap = null;
    int amtColumnNo = 0;
    double tot = 0;
    String amtColName = "";
    String behavesLike = "";
    boolean collDet = false;
    DefaultTableModel model = null;
    DefaultTreeModel root;
    DefaultTreeModel child;
    Date currDt = null;
    private TableModelListener tableModelListener;
    ArrayList _heading = null;
    ArrayList data = null;
    String node = "";
    private final static Logger log = Logger.getLogger(AuthorizeListDebitUI.class);

    /**
     * Creates new form ViewAll
     */
    public AuthorizeListDebitUI() {
//        try {
//        javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            
//        }
        setupInit();
        setupScreen();
    }

    private void setupInit() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        internationalize();
//        setObservable();
        toFront();
        getUnauthorizedTree();
        btnClear.setVisible(true);
        setCurBal();
//        observable.resetForm();
//        update(observable, null);
    }

    private void setCurBal() {
//        HashMap currMap = new HashMap();
//        List currList = ClientUtil.executeQuery("getSelectCurrCashBal", currMap);
//        for(int i=0;i<currList.size();i++){
//           currMap = (HashMap)currList.get(0);
//           txtCurCashBal.setText(CommonUtil.convertObjToStr(currMap.get("CUR_BAL")));
//        }
        String amount1 = "";
        String Opamount = "";
        double opval = 0;
        // System.out.println(" ooooooooooo ====== "+amtGlobal);
        HashMap singleAuthorizeMapOpBal = new HashMap();
        singleAuthorizeMapOpBal.put("TRANS_DT", currDt);
        singleAuthorizeMapOpBal.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        List aListOp = ClientUtil.executeQuery("getOpBalAmount", singleAuthorizeMapOpBal);
        // lblOpBal.setText("0.00");
        //  System.out.println("aListOp ---------------- "+aListOp);
        //  System.out.println("aListOp ------33---------- "+aListOp.size());
        if (aListOp.size() > 0 && aListOp.get(0) != null) {
            HashMap mapop = (HashMap) aListOp.get(0);
            Opamount = mapop.get("CURAMOUNT").toString();
            if (Opamount != null && !Opamount.equalsIgnoreCase("")) // lblOpBal.setText(formatCrore(Opamount));
            {
                opval = CommonUtil.convertObjToDouble(mapop.get("CURAMOUNT").toString());
            }
            // else

        }
        // CommonUtil.convertObjToDouble(lblOpBal.getText());
        double receiptAmt = 0;
        double paymentAmt = 0;
        HashMap singleAuthorizeMap1 = new HashMap();
        singleAuthorizeMap1.put("TRANS_DT", currDt);
        singleAuthorizeMap1.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        List aListA = ClientUtil.executeQuery("getSumPayRecAmount", singleAuthorizeMap1);//getSumPayRecAmountUnion
        for (int i = 0; i < aListA.size(); i++) {
            HashMap map = (HashMap) aListA.get(i);
            if (map.get("AMOUNT") != null && map.get("TRANS_TYPE").equals("CREDIT")) {//REC_AMOUNT
                amount1 = map.get("AMOUNT").toString();
                if (amount1 != null && !amount1.equalsIgnoreCase("")) {
                    receiptAmt = CommonUtil.convertObjToDouble(amount1);
                }
            }
            if (map.get("AMOUNT") != null && map.get("TRANS_TYPE").equals("DEBIT")) {//REC_AMOUNT
                amount1 = map.get("AMOUNT").toString();
                if (amount1 != null && !amount1.equalsIgnoreCase("")) {
                    paymentAmt = CommonUtil.convertObjToDouble(amount1);
                }
            }
        }
        System.out.println("opval -- " + opval + "receiptAmt == " + receiptAmt + "paymentAmt==" + paymentAmt);
        double totVal = opval + receiptAmt - paymentAmt;
      //  txtCurCashBal.setText(formatCrore(String.valueOf(totVal)));
    }

    public static String formatCrore(String str) {
        java.text.DecimalFormat numberFormat = new java.text.DecimalFormat();
        numberFormat.applyPattern("########################0.00");

        double currData = Double.parseDouble(str.replaceAll(",", ""));
        str = numberFormat.format(currData);

        String num = str.substring(0, str.lastIndexOf(".")).replaceAll(",", "");
        String dec = str.substring(str.lastIndexOf("."));

        String sign = "";
        if (num.substring(0, 1).equals("-")) {
            sign = num.substring(0, 1);
            num = num.substring(1, num.length());
        }

        char[] chrArr = num.toCharArray();
        StringBuffer fmtStrB = new StringBuffer();

        for (int i = chrArr.length - 1, j = 0, k = 0; i >= 0; i--) {
            if ((j == 3 && k == 3) || (j == 2 && k == 5) || (j == 2 && k == 7)) {
                fmtStrB.insert(0, ",");
                if (k == 7) {
                    k = 0;
                }
                j = 0;
            }
            j++;
            k++;

            fmtStrB.insert(0, chrArr[i]);
        }
        fmtStrB.append(dec);

        str = fmtStrB.toString();

        str = sign + str;

        if (str.equals(".00")) {
            str = "0";
        }

        return str;
    }

    private void setupScreen() {
//        setModal(true);

        /* Calculate the screen size */
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
            observable = NonFinTransactionOB.getInstance();
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
        /* The following if condition commented by Rajesh
         * Because observable is not making null after closing the UI
         * So, if no data found in previously opened EnquiryUI instance
         * the observable.isAvailable() is false, so EnquiryUI won't open.
         */
//        if (observable.isAvailable()) {
        super.show();
//        }
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
        panSearchCondition = new com.see.truetransact.uicomponent.CPanel();
        panTree = new com.see.truetransact.uicomponent.CPanel();
        cScrollPane1 = new com.see.truetransact.uicomponent.CScrollPane();
        treData = new javax.swing.JTree();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        srcTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();
        sptLine = new com.see.truetransact.uicomponent.CSeparator();
        panSearch = new com.see.truetransact.uicomponent.CPanel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnClear = new com.see.truetransact.uicomponent.CButton();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMaximumSize(new java.awt.Dimension(980, 600));
        setMinimumSize(new java.awt.Dimension(980, 600));
        setPreferredSize(new java.awt.Dimension(980, 600));
        setRequestFocusEnabled(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panSearchCondition.setMinimumSize(new java.awt.Dimension(700, 680));
        panSearchCondition.setPreferredSize(new java.awt.Dimension(700, 680));
        panSearchCondition.setLayout(new java.awt.GridBagLayout());

        panTree.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panTree.setMinimumSize(new java.awt.Dimension(200, 450));
        panTree.setPreferredSize(new java.awt.Dimension(200, 450));
        panTree.setLayout(new java.awt.GridBagLayout());

        cScrollPane1.setMinimumSize(new java.awt.Dimension(700, 680));
        cScrollPane1.setPreferredSize(new java.awt.Dimension(700, 680));

        treData.setModel(root);
        treData.setExpandsSelectedPaths(false);
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
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(panTable, gridBagConstraints);

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

        panSearch.setMinimumSize(new java.awt.Dimension(750, 40));
        panSearch.setPreferredSize(new java.awt.Dimension(750, 40));
        panSearch.setLayout(new java.awt.GridBagLayout());

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(btnClose, gridBagConstraints);

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(btnClear, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(panSearch, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void treDataValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_treDataValueChanged
        // TODO add your handling code here:
        TreePath oldPath = evt.getOldLeadSelectionPath();
        TreePath newPath = evt.getNewLeadSelectionPath();
        DefaultMutableTreeNode lastNode = null;
        if (oldPath != null) {
            lastNode = (DefaultMutableTreeNode) oldPath.getLastPathComponent();
        }
        DefaultMutableTreeNode newNode = null;
        if (newPath != null) {
            newNode = (DefaultMutableTreeNode) newPath.getLastPathComponent();
        }
//        clearTransDetails();
        if (newNode != null) {
//            selectedNode = newNode;
            node = newNode.toString();
//            if(node.equals("Payments")){
//                transMode = "Payments";
//            }else if(node.equals("Receipts")){
//                transMode = "Receipts";
//            }
//            if(node != null && node!="Receipts" && node!="Payments" ) {
            System.out.println("last selected node = " + lastNode + "\n"
                    + "new selected node = " + newNode + "\n\n");
//                trans_pid=node;
//                System.out.println("####### transMode : " +transMode);
            if (!node.equals("Unauthorized Records")) {
                displayDetails();
            }
//            }
        }
    }//GEN-LAST:event_treDataValueChanged

    public DefaultTreeModel getUnauthorizedTree() {
//        treData.setVisible(false);
        DefaultMutableTreeNode parent = new DefaultMutableTreeNode("Unauthorized Records");
        DefaultMutableTreeNode parent1 = new DefaultMutableTreeNode("Financial List");
        DefaultMutableTreeNode parent2 = new DefaultMutableTreeNode("Non Financial List");
        parent.add(parent1);
        parent.add(parent2);
        DefaultMutableTreeNode node = new DefaultMutableTreeNode("Customer Master");
        parent2.add(node);
        node = new DefaultMutableTreeNode("SB/Current Account Opening");
        parent2.add(node);
        node = new DefaultMutableTreeNode("Salary Recovery Transaction");
        parent1.add(node);
        node = new DefaultMutableTreeNode("SB/Current Account Closing");
        parent1.add(node);
        node = new DefaultMutableTreeNode("Deposit Account Opening");
        parent1.add(node);
        node = new DefaultMutableTreeNode("Deposit Account Renewal");
        parent1.add(node);
        node = new DefaultMutableTreeNode("Deposit Account Closing");
        parent1.add(node);
        node = new DefaultMutableTreeNode("Gold Loan Account Opening");
        parent1.add(node);
        node = new DefaultMutableTreeNode("Loans/Advances Account Opening");
        parent1.add(node);
        node = new DefaultMutableTreeNode("Deposit Loan Account Opening");
        parent1.add(node);
        node = new DefaultMutableTreeNode("Loan Account Closing");
        parent1.add(node);
        node = new DefaultMutableTreeNode("Cash Transactions");
        parent1.add(node);
        node = new DefaultMutableTreeNode("Transfer Transactions");
        parent1.add(node);
        node = new DefaultMutableTreeNode("Share Account");
        parent1.add(node);
        node = new DefaultMutableTreeNode("MDS Receipts");
        parent1.add(node);
        node = new DefaultMutableTreeNode("MDS Payments");
        parent1.add(node);
        node = new DefaultMutableTreeNode("Bills Lodgement");
        parent1.add(node);
        node = new DefaultMutableTreeNode("Investment Trans");
        parent1.add(node);
//        node = new DefaultMutableTreeNode("Cheque Book Issued");
//        parent.add(node);
        node = new DefaultMutableTreeNode("MDS Prized Money Details");
        parent1.add(node);
        parent2.add(node);
        node = new DefaultMutableTreeNode("MDS Master Maintenance");
        parent1.add(node);
        parent2.add(node);
        node = new DefaultMutableTreeNode("Borrowing Disbursal");
        parent1.add(node);
        node = new DefaultMutableTreeNode("Borrowing Master");
        parent1.add(node);
        node = new DefaultMutableTreeNode("Borrowing Repayment");
        parent1.add(node);
        node = new DefaultMutableTreeNode("Other Bank Account Opening");
        parent1.add(node);
        node = new DefaultMutableTreeNode("Locker");
        parent1.add(node);
        node = new DefaultMutableTreeNode("Rent Transaction");
        parent1.add(node);
        node = new DefaultMutableTreeNode("Indend Transaction");
        parent1.add(node);
        node = new DefaultMutableTreeNode("Purchase Entry");
        parent1.add(node);
        node = new DefaultMutableTreeNode("Multiple Cash Tranasction");
        parent1.add(node);
        node = new DefaultMutableTreeNode("Loan Application Register");
        parent2.add(node);
        node = new DefaultMutableTreeNode("Share Dividend Payment");
        parent1.add(node);
        node = new DefaultMutableTreeNode("DRF Transaction");
        parent1.add(node);
        node = new DefaultMutableTreeNode("InvestmentMaster");
        parent1.add(node);
        node = new DefaultMutableTreeNode("Account Head");
        parent2.add(node);
        node = new DefaultMutableTreeNode("Gahan Customer");
        parent2.add(node);
        node = new DefaultMutableTreeNode("Standing Instruction");
        parent2.add(node);
        node = new DefaultMutableTreeNode("Cheque Book Issue");
        parent2.add(node);
        node = new DefaultMutableTreeNode("Stop Payment Issue");
        parent2.add(node);
        node = new DefaultMutableTreeNode("Loose Leaf Issue");
        parent2.add(node);
        node = new DefaultMutableTreeNode("ECS Stop Payment");
        parent2.add(node);
        node = new DefaultMutableTreeNode("MDS Application New");
        parent2.add(node);
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
        where.put("TRANS_DT", currDt.clone());
        where.put("CASHIER_AUTH_ALLOWED", TrueTransactMain.CASHIER_AUTH_ALLOWED);
//        added by nikhil for share authorization
//        currDt = (Date) currDt.clone();
        where.put("TRANS_DT", currDt.clone());
//        ProxyParameters.BRANCH_ID = "0001";
        List lst = new ArrayList();
        if (node.equals("Customer Master")) {
            whereMap.put(CommonConstants.MAP_NAME, "getUnAuthorizedListForCustomer");
        } else if (node.equals("SB/Current Account Opening")) {
            whereMap.put(CommonConstants.MAP_NAME, "getSelectActMasterAuthDebit");
        }else if (node.equals("SB/Current Account Opening")) {
            whereMap.put(CommonConstants.MAP_NAME, "getSelectActMasterAuthDebit");
        }else if (node.equals("Salary Recovery Transaction")) {
            whereMap.put(CommonConstants.MAP_NAME, "getSelectSalaryRecoveryTransactionAuthDedit");
        } else if (node.equals("Deposit Account Opening")) {
            where.put("OPENING_MODE", "Normal");
            whereMap.put(CommonConstants.MAP_NAME, "getSelectDepositOpeningAuthDebit");
        } else if (node.equals("Deposit Account Renewal")) {
            where.put("OPENING_MODE", "Renewal");
            whereMap.put(CommonConstants.MAP_NAME, "getSelectDepositOpeningAuthDebit");
        } else if (node.equals("Deposit Account Closing")) {
            whereMap.put(CommonConstants.MAP_NAME, "getSelectDepositClosingAuthDebit");
        } else if (node.equals("Gold Loan Account Opening")) {
            where.put("AUTHORIZE_REMARK", "!= 'GOLD_LOAN'");
            where.put("GOLD_LOAN", "GOLD_LOAN");
            where.put("AUTHORIZESTATUS", "AUTHORIZED");
            where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            where.put("STATUS_BY", ProxyParameters.USER_ID);
            whereMap.put(CommonConstants.MAP_NAME, "getSelectLoanGoldLoanOpeningAuthDebit");
        } else if (node.equals("Loans/Advances Account Opening")) {
            where.put("AUTHORIZE_REMARK", "= 'GOLD_LOAN'");
            where.put("AUTHORIZESTATUS", "AUTHORIZED");
            where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            where.put("STATUS_BY", ProxyParameters.USER_ID);
            whereMap.put(CommonConstants.MAP_NAME, "getSelectLoanOpeningAuthDebit");
       } else if (node.equals("Deposit Loan Account Opening")) {
            where.put("AUTHORIZESTATUS", "AUTHORIZED");
            where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            where.put("STATUS_BY", ProxyParameters.USER_ID);
            whereMap.put(CommonConstants.MAP_NAME, "getSelectDepositLoanOpeningAuthDebit");
       } else if (node.equals("SB/Current Account Closing")) {
            where.put("AUTHORIZESTATUS", "AUTHORIZED");
            where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            where.put("STATUS_BY", ProxyParameters.USER_ID);
            whereMap.put(CommonConstants.MAP_NAME, "getSelectSBClosingAuthDebit");
       }else if (node.equals("Loan Account Closing")) {
            where.put("AUTHORIZESTATUS", "AUTHORIZED");
            whereMap.put(CommonConstants.MAP_NAME, "getSelectLoanClosingAuthDebit");
       } else if (node.equals("Cash Transactions")) {
            where.put("AUTHORIZESTATUS", "AUTHORIZED");
            whereMap.put(CommonConstants.MAP_NAME, "getSelectCashAuthDebit");
       } else if (node.equals("Transfer Transactions")) {
            where.put("AUTHORIZESTATUS", "AUTHORIZED");
            whereMap.put(CommonConstants.MAP_NAME, "getUnAuthorizeMasterTransferTO");
        } else if (node.equals("Share Account")) {
            where.put("AUTHORIZESTATUS", "AUTHORIZED");
            where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
            where.put("TRANS_DT", currDt);
            whereMap.put(CommonConstants.MAP_NAME, "getSelectNonAuthRecordForShareDebit");
        } else if (node.equals("MDS Receipts")) {
            where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
            whereMap.put(CommonConstants.MAP_NAME, "getSelectNonAuthRecordForTransferReceipt");
        } else if (node.equals("MDS Payments")) {
            whereMap.put(CommonConstants.MAP_NAME, "getPrizedMoneyPaymentAuthorize");
        } else if (node.equals("Bills Lodgement")) {
            whereMap.put(CommonConstants.MAP_NAME, "getLodgementMasterAuthorizeDebitList");
        } else if (node.equals("Investment Trans")) {
            whereMap.put(CommonConstants.MAP_NAME, "getInvestmentTransAuthorizeDebitList");
        } else if (node.equals("MDS Prized Money Details")) {
            whereMap.put(CommonConstants.MAP_NAME, "getPrizedMoneyDetailsEntryAuthorize");
        } else if (node.equals("MDS Master Maintenance")) {
            whereMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            whereMap.put(CommonConstants.MAP_NAME, "getMDSMasterMaintenanceAuthorize");
        } else if (node.equals("Borrowing Disbursal")) {
            whereMap.put(CommonConstants.MAP_NAME, "getBorrowingDisbursalAuthorizeList");
        } else if (node.equals("Borrowing Master")) {
            whereMap.put(CommonConstants.MAP_NAME, "getBorrowingAuthorizeList");
        } else if (node.equals("Borrowing Repayment")) {
            whereMap.put(CommonConstants.MAP_NAME, "getBorrowingRepIntClsAuthorizeList");
        } else if (node.equals("Other Bank Account Opening")) {
            whereMap.put(CommonConstants.MAP_NAME, "getSelectABAuthDetails");
        } else if (node.equals("Locker")) {
            whereMap.put(CommonConstants.MAP_NAME, "getSelectLockerAuthorize");
        } else if (node.equals("Rent Transaction")) {
            whereMap.put(CommonConstants.MAP_NAME, "getRentTransAuthorizeDebitList");
        } else if (node.equals("Indend Transaction")) {
            whereMap.put(CommonConstants.MAP_NAME, "getIndendAuthorizeList");
        } else if (node.equals("Multiple Cash Tranasction")) {
            whereMap.put(CommonConstants.MAP_NAME, "getAuthorizationsListForMutipleCashDebit");
        } else if (node.equals("Purchase Entry")) {
            whereMap.put(CommonConstants.MAP_NAME, "getPurchaseEntryAuthorizeList");
        } else if (node.equals("Trade Expense")) {
            whereMap.put(CommonConstants.MAP_NAME, "getTradeExpenseEntryAuthorizeList");
        } else if (node.equals("Loan Application Register")) {
            whereMap.put(CommonConstants.MAP_NAME, "getLoanApplicationAuthorizeDebitList");
        } else if (node.equals("MDS Application New")) {
            where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
            whereMap.put(CommonConstants.MAP_NAME, "getMDSappDebitAuth");
        } else if (node.equals("Share Dividend Payment")) {
            whereMap.put(CommonConstants.MAP_NAME, "getDividendPaymentTransferAuthMode");
        } else if (node.equals("DRF Transaction")) {
            whereMap.put(CommonConstants.MAP_NAME, "getDrfTransDebitAuth");
        } else if (node.equals("Account Head")) {
            whereMap.put(CommonConstants.MAP_NAME, "getSelectAccCreationAuthorizeTOList");
        } else if (node.equals("Gahan Customer")) {
            whereMap.put(CommonConstants.MAP_NAME, "getSelectGahanaAuthList");
        } else if (node.equals("Standing Instruction")) {
            whereMap.put(CommonConstants.MAP_NAME, "getStandingInstructionAuthorizeList");
        } else if (node.equals("InvestmentMaster")) {
            whereMap.put(CommonConstants.MAP_NAME, "getSelectInvestmentAuthDetails");
        } else if (node.equals("Cheque Book Issue")) {
            whereMap.put(CommonConstants.MAP_NAME, "getSelectChequeBookIssueTOList");
        } else if (node.equals("Stop Payment Issue")) {
            whereMap.put(CommonConstants.MAP_NAME, "getSelectStopPaymentIssueTOList");
        } else if (node.equals("Loose Leaf Issue")) {
            whereMap.put(CommonConstants.MAP_NAME, "getSelectLooseLeafIssueTOList");
        } else if (node.equals("ECS Stop Payment")) {
            whereMap.put(CommonConstants.MAP_NAME, "getSelectEcsStopPaymentIssueTOList");
        }
        whereMap.put(CommonConstants.MAP_WHERE, where);
        if (whereMap.containsKey(CommonConstants.MAP_NAME)) {
            whereMap = ClientUtil.executeTableQuery(whereMap);
            _heading = (ArrayList) whereMap.get(CommonConstants.TABLEHEAD);
            data = (ArrayList) whereMap.get(CommonConstants.TABLEDATA);
        }
        populateTable();
//        HashMap hash = (HashMap) obj;
        System.out.println("#$#$ whereMap : " + whereMap);
    }
    boolean btnIntDetPressed = false;

    public void fillData(Object obj) {
        HashMap hash = (HashMap) obj;
        System.out.println("#$#$ Hash : " + hash);
    }

    public void populateTable() {
//        ArrayList heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        boolean dataExist;
        if (_heading != null) {
//            _isAvailable = true;
            ArrayList tempList = null;
//            System.out.println("#$#$ heading : "+_heading);
//            System.out.println("#$#$ data : "+data);
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
//            System.out.println("#$#$ heading : "+_heading);
//            System.out.println("#$#$ data : "+data);
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
//            _isAvailable = false;
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
//            this.dispose();
            HashMap hash = fillData(rowIndexSelected);
            if (node.equals("Customer Master")) {
                frm = new com.see.truetransact.ui.customer.IndividualCustUI();
            } else if (node.equals("SB/Current Account Opening")) {
                frm = new com.see.truetransact.ui.operativeaccount.AccountsUI();
            } else if (node.equals("SB/Current Account Closing")) {
                frm = new com.see.truetransact.ui.operativeaccount.AccountClosingUI();
            } else if (node.equals("Salary Recovery Transaction")) {
                frm = new com.see.truetransact.ui.salaryrecovery.TransAllUI();
            }else if (node.equals("Deposit Account Opening")) {
                frm = new com.see.truetransact.ui.deposit.TermDepositUI();
            } else if (node.equals("Deposit Account Renewal")) {
                frm = new com.see.truetransact.ui.deposit.TermDepositUI();
            } else if (node.equals("Deposit Account Closing")) {
                frm = new com.see.truetransact.ui.deposit.closing.DepositClosingUI();
            } else if (node.equals("Gold Loan Account Opening")) {
//                frm = new com.see.truetransact.ui.termloan.GoldLoanUI("OTHERS", CommonUtil.convertObjToStr(hash.get("PROD_ID")));
                frm = new com.see.truetransact.ui.termloan.GoldLoanUI();
            } else if (node.equals("Loans/Advances Account Opening")) {
                frm = new com.see.truetransact.ui.termloan.TermLoanUI("OTHERS");
            } else if (node.equals("Deposit Loan Account Opening")) {
                frm = new com.see.truetransact.ui.termloan.depositLoan.DepositLoanUI();
            } else if (node.equals("Loan Account Closing")) {
                frm = new com.see.truetransact.ui.operativeaccount.AccountClosingUI("TermLoan");
            } else if (node.equals("Cash Transactions")) {
                frm = new com.see.truetransact.ui.transaction.cash.CashTransactionUI();
            } else if (node.equals("Transfer Transactions")) {
                frm = new com.see.truetransact.ui.transaction.transfer.TransferUI();
            } else if (node.equals("Share Account")) {
                frm = new com.see.truetransact.ui.share.ShareUI();
            } else if (node.equals("MDS Receipts")) {
                frm = new com.see.truetransact.ui.mdsapplication.mdsreceiptentry.MDSReceiptEntryUI();
            } else if (node.equals("MDS Payments")) {
                frm = new com.see.truetransact.ui.mdsapplication.mdsprizedmoneypayment.MDSPrizedMoneyPaymentUI();
            } else if (node.equals("Bills Lodgement")) {
                frm = new com.see.truetransact.ui.bills.lodgement.LodgementBillsUI();
            } else if (node.equals("Investment Trans")) {
                frm = new com.see.truetransact.ui.investments.InvestmentsTransUI();
            } //            else if (node.equals("Cheque Book Issued")) {
            //                frm = new com.see.truetransact.ui.supporting.chequebook.ChequeBookUI(getSelectedBranchID());
            //            }Borrowing Disbursal
            else if (node.equals("MDS Prized Money Details")) {
                frm = new com.see.truetransact.ui.mdsapplication.mdsprizedmoneydetailsentry.MDSPrizedMoneyDetailsEntryUI();
            } else if (node.equals("MDS Master Maintenance")) {
                frm = new com.see.truetransact.ui.mdsapplication.mdsmastermaintenance.MDSMasterMaintenanceUI();
            } else if (node.equals("Borrowing Disbursal")) {
                frm = new com.see.truetransact.ui.borrowings.panDisbursal();
            } else if (node.equals("Borrowing Master")) {
                frm = new com.see.truetransact.ui.borrowings.NewBorrowing();
            } else if (node.equals("Borrowing Repayment")) {
                frm = new com.see.truetransact.ui.borrowings.panRepaymentInt();
            } else if (node.equals("Other Bank Account Opening")) {
                frm = new com.see.truetransact.ui.accountswithotherbank.AccountswithOtherBanksUI();
            } else if (node.equals("Locker")) {
                frm = new com.see.truetransact.ui.locker.lockersurrender.LockerSurrenderUI();
            } else if (node.equals("Rent Transaction")) {
                frm = new com.see.truetransact.ui.roomrent.frmRentTrans();
            } else if (node.equals("Indend Transaction")) {
                frm = new com.see.truetransact.ui.indend.frmIndend();
            } else if (node.equals("Purchase Entry")) {
                frm = new com.see.truetransact.ui.indend.PurchaseEntryUI();
            } else if (node.equals("Multiple Cash Tranasction")) {
                frm = new com.see.truetransact.ui.transaction.multipleCash.MultipleCashTransactionUI();
            } else if (node.equals("Loan Application Register")) {
                frm = new com.see.truetransact.ui.termloan.loanapplicationregister.LoanApplicationUI();
            } else if (node.equals("MDS Application New")) {
               frm = new com.see.truetransact.ui.mdsapplication.MDSApplicationUI();
            } else if (node.equals("Share Dividend Payment")) {
                frm = new com.see.truetransact.ui.share.ShareDividendPaymentUI();
            } else if (node.equals("DRF Transaction")) {
                frm = new com.see.truetransact.ui.share.DrfTransactionUI();
            } else if (node.equals("InvestmentMaster")) {
                frm = new com.see.truetransact.ui.investments.InvestmentsMasterUI();
            } else if (node.equals("Account Head")) {
                frm = new com.see.truetransact.ui.generalledger.AccountCreationUI();
            } else if (node.equals("Gahan Customer")) {
                frm = new com.see.truetransact.ui.customer.gahan.GahanCustomerUI();
            } else if (node.equals("Standing Instruction")) {
                frm = new com.see.truetransact.ui.supporting.standinginstruction.StandingInstructionUI();
            } else if (node.equals("Cheque Book Issue")) {
                frm = new com.see.truetransact.ui.supporting.chequebook.ChequeBookUI("0001");
                hash.put("Cheque", "");
            } else if (node.equals("Stop Payment Issue")) {
                frm = new com.see.truetransact.ui.supporting.chequebook.ChequeBookUI(getSelectedBranchID());
                hash.put("Stop", "");
            } else if (node.equals("Loose Leaf Issue")) {
                frm = new com.see.truetransact.ui.supporting.chequebook.ChequeBookUI(getSelectedBranchID());
                hash.put("Leaf", "");
            } else if (node.equals("ECS Stop Payment")) {
                frm = new com.see.truetransact.ui.supporting.chequebook.ChequeBookUI(getSelectedBranchID());
                hash.put("ECS", "");
            }
            hash.put("FROM_MANAGER_AUTHORIZE_LIST_UI", "");
            frm.setSelectedBranchID(getSelectedBranchID());
            TrueTransactMain.showScreen(frm);
//            frm.setSwitchEnglish(false);
            System.out.println("Haash Before"+hash);
            hash.put("PARENT", this);
            hash.put("TRANS_DT", currDt.clone());
            frm.fillData(hash);
            System.out.println("#$#$ Final Hash : after " + hash);
        }
    }

    public void removeSelectedRow() {
        TableSorter tblSorter = (TableSorter) tblData.getModel();
        TableModel tblModel = (TableModel) tblSorter.getModel();
        if (node.equals("Cash Transactions")) {
            ArrayList cashList = tblModel.getDataArrayList();
            ArrayList rowList = (ArrayList) cashList.get(tblData.getSelectedRow());
//            System.out.println("#$#$ rowList:"+rowList);
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
    
    public void setFocusToTable() {
        if (tblData.getRowCount() > 0) {
            try {
                tblData.requestFocus();
                tblData.changeSelection(0, 0, false, false);
                this.setMaximum(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private HashMap fillData(int rowIndexSelected) {
        TableModel _tableModel = (TableModel) tblData.getModel();
        ArrayList rowdata = null;

        if (rowIndexSelected > -1) {
            rowdata = _tableModel.getRow(rowIndexSelected);
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

    private void tblDataMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMousePressed
        if (evt.getClickCount() == 1) {
            whenTableRowSelected();
        }
    }//GEN-LAST:event_tblDataMousePressed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        btnClearActionPerformed(null);
        //cifClosingAlert();
//        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
//        observable.resetForm();
//        update(observable, null);
    }//GEN-LAST:event_btnClearActionPerformed

    private void displayTransDetail(HashMap proxyResultMap) {
        System.out.println("@#$@@$@@@$ proxyResultMap : " + proxyResultMap);
        String cashDisplayStr = "Cash Transaction Details...\n";
        String transferDisplayStr = "Transfer Transaction Details...\n";
        String displayStr = "";
        String transId = "";
        String transType = "";
        Object keys[] = proxyResultMap.keySet().toArray();
        int cashCount = 0;
        int transferCount = 0;
        List tempList = null;
        HashMap transMap = null;
        String actNum = "";
        for (int i = 0; i < keys.length; i++) {
            if (proxyResultMap.get(keys[i]) instanceof String) {
                continue;
            }
            tempList = (List) proxyResultMap.get(keys[i]);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH") != -1) {
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        transId = (String) transMap.get("TRANS_ID");
                    }
                    cashDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {
                        cashDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    } else {
                        cashDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }
                }
//                cashDisplayStr += "Trans Id : " + transMap.get("TRANS_ID");
                cashCount++;
            } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        transId = (String) transMap.get("BATCH_ID");
                    }
//                    transferDisplayStr += "   Batch Id : " + transMap.get("BATCH_ID");//"Trans Id : " + transMap.get("TRANS_ID")
                    break;
                    //  + "   Batch Id : " + transMap.get("BATCH_ID")
                    //  + "   Trans Type : " + transMap.get("TRANS_TYPE");
//                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
//                    if (actNum != null && !actNum.equals("")) {
//                        transferDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
//                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
//                    } else {
//                        transferDisplayStr += "   Account Head : " + transMap.get("AC_HD_ID")
//                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
//                    }
                }
                transferDisplayStr += "   Batch Id : " + transMap.get("BATCH_ID");
                transferCount++;
            }
        }
        if (cashCount > 0) {
            displayStr += cashDisplayStr;
        }
        if (transferCount > 0) {
            displayStr += transferDisplayStr;
        }
        if (!displayStr.equals("")) {
            ClientUtil.showMessageWindow("" + displayStr);
        }
    }

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
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        //CHANGED BY JITHIN TO WORK FOR SAL_REC='Y'
        new AuthorizeListDebitUI().show();
    }

    public void update(Observable observed, Object arg) {
//            tdtFromDate.setDateValue(DateUtil.getStringDate(observable.getTdtFromDate()));
//            tdtToDate.setDateValue(DateUtil.getStringDate(observable.getTdtToDate()));
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CScrollPane cScrollPane1;
    private com.see.truetransact.uicomponent.CPanel panSearch;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CPanel panTree;
    private com.see.truetransact.uicomponent.CButtonGroup rdgAndOr;
    private com.see.truetransact.uicomponent.CSeparator sptLine;
    private com.see.truetransact.uicomponent.CScrollPane srcTable;
    private com.see.truetransact.uicomponent.CTable tblData;
    private javax.swing.JTree treData;
    // End of variables declaration//GEN-END:variables
}
