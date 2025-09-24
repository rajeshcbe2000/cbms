/*
 * Non Financial Transacton Enquiry.java
 *
 * Created on October 16, 2009, 1:46 PM
 */
package com.see.truetransact.ui.common.viewall;

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.*;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.ui.generalledger.AccountMaintenanceUI;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.COptionPane;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.*;
import java.util.logging.Level;
import javax.swing.JTable;
import javax.swing.JDialog;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableCellRenderer;
import org.apache.log4j.Logger;

/**
 * @author Swaroop
 */
public class NewAuthorizeListUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer {

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
    private final static Logger log = Logger.getLogger(NewAuthorizeListUI.class);
    ArrayList colorList = new ArrayList();

    /**
     * Creates new form ViewAll
     */
    public NewAuthorizeListUI() {
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
        //getUnauthorizedTree();
        btnClear.setVisible(true);
        setCurBal();
        getUnauthorizedTrees();
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
        String Cashamount="";
        String Cashnamount="";
        double opval = 0;
        double cashval = 0;
        double cashnval = 0;
        // System.out.println(" ooooooooooo ====== "+amtGlobal);
        HashMap singleAuthorizeMapOpBal = new HashMap();
        singleAuthorizeMapOpBal.put("TRANS_DT", currDt);
        singleAuthorizeMapOpBal.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        List aListOp = ClientUtil.executeQuery("getOpBalAmount", singleAuthorizeMapOpBal);
        List cashList = ClientUtil.executeQuery("getCashUnapprovedBal", singleAuthorizeMapOpBal);
        List cashNList = ClientUtil.executeQuery("getCashapprovedBal", singleAuthorizeMapOpBal);
         if (cashList.size() > 0 && cashList.get(0) != null) {
            HashMap mapop = (HashMap) cashList.get(0);
            if(mapop!=null && mapop.containsKey("BALANCE"))
            Cashamount = CommonUtil.convertObjToStr(mapop.get("BALANCE"));
            if (Cashamount != null && !Cashamount.equalsIgnoreCase("")) // lblOpBal.setText(formatCrore(Opamount));
            {
                cashval = CommonUtil.convertObjToDouble(mapop.get("BALANCE").toString());
            }
            // else

        }
         if (cashNList.size() > 0 && cashNList.get(0) != null) {
            HashMap mapop = (HashMap) cashNList.get(0);
             if(mapop!=null && mapop.containsKey("BALANCE"))
            Cashnamount = CommonUtil.convertObjToStr(mapop.get("BALANCE"));
            if (Cashnamount != null && !Cashnamount.equalsIgnoreCase("")) // lblOpBal.setText(formatCrore(Opamount));
            {
                cashnval = CommonUtil.convertObjToDouble(mapop.get("BALANCE").toString());
            }
            // else

        }
        // lblOpBal.setText("0.00");
        //  System.out.println("aListOp ---------------- "+aListOp);
        //  System.out.println("aListOp ------33---------- "+aListOp.size());
        if (aListOp.size() > 0 && aListOp.get(0) != null) {
            HashMap mapop = (HashMap) aListOp.get(0);
            if(mapop!=null && mapop.containsKey("CURAMOUNT"))
            Opamount = CommonUtil.convertObjToStr(mapop.get("CURAMOUNT")).toString();
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
        TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y");
        if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null &&  TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
            txtCurCashBal.setText(formatCrore(String.valueOf(cashnval)));
        } else {
            txtCurCashBal.setText(formatCrore(String.valueOf(cashval)));
        }
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
        /*
         * The following if condition commented by Rajesh Because observable is
         * not making null after closing the UI So, if no data found in
         * previously opened EnquiryUI instance the observable.isAvailable() is
         * false, so EnquiryUI won't open.
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
        lblCurCashBal = new com.see.truetransact.uicomponent.CLabel();
        txtCurCashBal = new com.see.truetransact.uicomponent.CTextField();
        btnRefresh = new com.see.truetransact.uicomponent.CButton();
        btnTransId = new com.see.truetransact.uicomponent.CButton();
        btnRefreshList = new com.see.truetransact.uicomponent.CButton();
        panSearch = new com.see.truetransact.uicomponent.CPanel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnClear = new com.see.truetransact.uicomponent.CButton();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setMinimumSize(new java.awt.Dimension(975, 650));
        setPreferredSize(new java.awt.Dimension(975, 650));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panSearchCondition.setMinimumSize(new java.awt.Dimension(950, 600));
        panSearchCondition.setPreferredSize(new java.awt.Dimension(950, 600));
        panSearchCondition.setLayout(new java.awt.GridBagLayout());

        panTree.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panTree.setMinimumSize(new java.awt.Dimension(200, 575));
        panTree.setPreferredSize(new java.awt.Dimension(200, 575));
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
        gridBagConstraints.ipadx = 11;
        gridBagConstraints.ipady = 25;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        panSearchCondition.add(panTree, gridBagConstraints);

        panTable.setMinimumSize(new java.awt.Dimension(950, 575));
        panTable.setOpaque(false);
        panTable.setPreferredSize(new java.awt.Dimension(750, 575));
        panTable.setLayout(new java.awt.GridBagLayout());

        srcTable.setMinimumSize(new java.awt.Dimension(50, 0));
        srcTable.setOpaque(false);
        srcTable.setPreferredSize(new java.awt.Dimension(240, 0));

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
        tblData.setMaximumSize(new java.awt.Dimension(450, 64));
        tblData.setMinimumSize(new java.awt.Dimension(725, 1000));
        tblData.setOpaque(false);
        tblData.setPreferredScrollableViewportSize(new java.awt.Dimension(450, 64));
        tblData.setPreferredSize(new java.awt.Dimension(725, 1000));
        tblData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDataMousePressed(evt);
            }
        });
        tblData.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblDataKeyPressed(evt);
            }
        });
        srcTable.setViewportView(tblData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.ipadx = 719;
        gridBagConstraints.ipady = 550;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        panTable.add(srcTable, gridBagConstraints);

        lblCurCashBal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCurCashBal.setText("Curr.Cash Bal");
        lblCurCashBal.setMinimumSize(new java.awt.Dimension(110, 20));
        lblCurCashBal.setPreferredSize(new java.awt.Dimension(110, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panTable.add(lblCurCashBal, gridBagConstraints);

        txtCurCashBal.setEditable(false);
        txtCurCashBal.setMaximumSize(new java.awt.Dimension(100, 21));
        txtCurCashBal.setMinimumSize(new java.awt.Dimension(110, 20));
        txtCurCashBal.setPreferredSize(new java.awt.Dimension(110, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 65;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panTable.add(txtCurCashBal, gridBagConstraints);

        btnRefresh.setText("Refresh");
        btnRefresh.setMinimumSize(new java.awt.Dimension(110, 20));
        btnRefresh.setPreferredSize(new java.awt.Dimension(110, 20));
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 11, 0, 0);
        panTable.add(btnRefresh, gridBagConstraints);

        btnTransId.setText("Get TransId");
        btnTransId.setMinimumSize(new java.awt.Dimension(110, 20));
        btnTransId.setPreferredSize(new java.awt.Dimension(110, 20));
        btnTransId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 0, 0);
        panTable.add(btnTransId, gridBagConstraints);

        btnRefreshList.setText("Refresh List");
        btnRefreshList.setMinimumSize(new java.awt.Dimension(110, 20));
        btnRefreshList.setPreferredSize(new java.awt.Dimension(110, 20));
        btnRefreshList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshListActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 3;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panTable.add(btnRefreshList, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 32;
        gridBagConstraints.ipady = 44;
        gridBagConstraints.weightx = 0.4;
        gridBagConstraints.weighty = 0.6;
        panSearchCondition.add(panTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 94;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(panSearchCondition, gridBagConstraints);

        panSearch.setMinimumSize(new java.awt.Dimension(750, 0));
        panSearch.setPreferredSize(new java.awt.Dimension(750, 0));
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
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 163;
        gridBagConstraints.weightx = 1.0;
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
            if (!node.equals("") && !node.equals(null) && !node.equals("Unauthorized Records") && !node.equals("Financial List") && !node.equals("Non Financial List")) {
                displayDetails(node);
            }
//            }
        }
    }//GEN-LAST:event_treDataValueChanged

    private void addinginParentList1(boolean flag, DefaultMutableTreeNode node, DefaultMutableTreeNode parent1) {
        if (flag) {
            parent1.add(node);
        }
    }

    private void addinginParentList2(boolean flag, DefaultMutableTreeNode node, DefaultMutableTreeNode parent2) {
        if (flag) {
            parent2.add(node);
        }
    }
    
    public DefaultTreeModel getUnauthorizedTrees() {
        DefaultMutableTreeNode parent = new DefaultMutableTreeNode("Unauthorized Records");
        DefaultMutableTreeNode parent1 = new DefaultMutableTreeNode("Financial List");
        DefaultMutableTreeNode parent2 = new DefaultMutableTreeNode("Non Financial List");
        DefaultMutableTreeNode node;
        parent.add(parent1);
        parent.add(parent2);
        HashMap treeMap = new HashMap();
        treeMap.put("CASHIER_AUTH_ALLOWED", TrueTransactMain.CASHIER_AUTH_ALLOWED);
        treeMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        treeMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        treeMap.put(CommonConstants.HIERARCHY_ID, ProxyParameters.HIERARCHY_ID);
        treeMap.put("TRANS_DT", currDt.clone());
        List treeList = (List) ClientUtil.executeQuery("getUnAuthorizedTransactionTree", treeMap);
        if (treeList != null && treeList.size() > 0) {
            int size = treeList.size();
            for(int i=0; i<size ;i++){
                treeMap = (HashMap) treeList.get(i);
                node = new DefaultMutableTreeNode(treeMap.get("SCREEN_NAME"));
                addinginParentList1(true, node, parent1);
                
            }        
        }
        
        //added by rishad for deposit accont renewal for without transaction
        node = new DefaultMutableTreeNode("Deposit AccountRenewal");
        boolean flag = displayTransactionDetails(node);
        addinginParentList2(flag, node, parent2);
        
        //Added By Kannan AR
        node = new DefaultMutableTreeNode("View All Transactions");
        parent.add(node);
        
        final DefaultTreeModel treemodel = new DefaultTreeModel(parent);
        treData.setModel(treemodel);
        root = null;
        return treemodel;
    }
    public DefaultTreeModel getUnauthorizedTree() {
//        treData.setVisible(false);
        DefaultMutableTreeNode parent = new DefaultMutableTreeNode("Unauthorized Records");
        DefaultMutableTreeNode parent1 = new DefaultMutableTreeNode("Financial List");
        DefaultMutableTreeNode parent2 = new DefaultMutableTreeNode("Non Financial List");
        parent.add(parent1);
        parent.add(parent2);
        DefaultMutableTreeNode node = new DefaultMutableTreeNode("Customer Master");
        boolean flag = displayTransactionDetails(node);
        addinginParentList2(flag, node, parent2);
        
        node = new DefaultMutableTreeNode("Corporate Customer");
        flag = displayTransactionDetails(node);
        addinginParentList2(flag, node, parent2);
        
        node = new DefaultMutableTreeNode("Locker Master");
        flag = displayTransactionDetails(node);
        addinginParentList2(flag, node, parent2);
        
        node = new DefaultMutableTreeNode("Share");
        flag = displayTransactionDetails(node);
        addinginParentList2(flag, node, parent2);
        
        node = new DefaultMutableTreeNode("Inventory Details");
        flag = displayTransactionDetails(node);
        addinginParentList2(flag, node, parent2);
        //parent2.add(node);
        node = new DefaultMutableTreeNode("SB/Current Account Opening");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);
       
         node = new DefaultMutableTreeNode("SB/Current AccountOpening");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList2(flag, node, parent2);
        
        node = new DefaultMutableTreeNode("Account Closing");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);

        node = new DefaultMutableTreeNode("Deposit Accounts");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);

        node = new DefaultMutableTreeNode("Deposit Account Renewal");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);

        node = new DefaultMutableTreeNode("Deposit Account Closing");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);
        
        node = new DefaultMutableTreeNode("Deposit Multiple Closing");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);
        
        node = new DefaultMutableTreeNode("Multiple Deposit Account Opening");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);  

        node = new DefaultMutableTreeNode("Gold Loan Account Opening");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);

        node = new DefaultMutableTreeNode("Loans/Advances Account Opening");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);
        node = new DefaultMutableTreeNode("Deposit Loan Account Opening");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);

        node = new DefaultMutableTreeNode("Loan Closing");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);

        node = new DefaultMutableTreeNode("Cash Transactions");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);

        node = new DefaultMutableTreeNode("Transfer");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);

        node = new DefaultMutableTreeNode("Share Account");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);

        node = new DefaultMutableTreeNode("MDS Receipts");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);

        node = new DefaultMutableTreeNode("MDS Payments");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);
        
        node = new DefaultMutableTreeNode("MDS Member");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);

        node = new DefaultMutableTreeNode("Lodgement");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);

        node = new DefaultMutableTreeNode("Investment Trans");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);
//        node = new DefaultMutableTreeNode("Cheque Book Issued");
//        parent.add(node);
        node = new DefaultMutableTreeNode("MDS Prized Money Details");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);
//        parent2.add(node);
        addinginParentList2(flag, node, parent2);
        node = new DefaultMutableTreeNode("MDS Master Maintenance");
//        parent1.add(node);
        flag = displayTransactionDetails(node);
//        parent2.add(node);
        addinginParentList2(flag, node, parent2);
        
        node = new DefaultMutableTreeNode("Borrowing Disbursal");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);

        node = new DefaultMutableTreeNode("Borrowing Master");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);
        
        node = new DefaultMutableTreeNode("Borrowing Repayment");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);

        node = new DefaultMutableTreeNode("Other Bank Account Opening");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);

        node = new DefaultMutableTreeNode("Locker Renew/Surrender");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);

        node = new DefaultMutableTreeNode("Locker Issue");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);
        
        node = new DefaultMutableTreeNode("Rent Transaction");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);

        node = new DefaultMutableTreeNode("Indend Transaction");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);

        node = new DefaultMutableTreeNode("Purchase Entry");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);

        node = new DefaultMutableTreeNode("Multiple Cash Tranasction");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);
        
        node = new DefaultMutableTreeNode("Multiple Deposit Creation");
//        parent2.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);

        node = new DefaultMutableTreeNode("Loan Application Register");
//        parent2.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);
        
        node = new DefaultMutableTreeNode("Loan Application With out Transaction");
//        parent2.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList2(flag, node, parent2);

        node = new DefaultMutableTreeNode("Share Dividend Payment");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);

        node = new DefaultMutableTreeNode("DRF Transaction");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);

        node = new DefaultMutableTreeNode("InvestmentMaster");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);

        node = new DefaultMutableTreeNode("Account Head");
//        parent2.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList2(flag, node, parent2);
        node = new DefaultMutableTreeNode("Gahan Customer");
//        parent2.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList2(flag, node, parent2);
        node = new DefaultMutableTreeNode("Standing Instruction");
//        parent2.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList2(flag, node, parent2);
        node = new DefaultMutableTreeNode("Cheque Book Issue");
//        parent2.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList2(flag, node, parent2);
        node = new DefaultMutableTreeNode("Stop Payment Issue");
//        parent2.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList2(flag, node, parent2);
        node = new DefaultMutableTreeNode("Loose Leaf Issue");
//        parent2.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList2(flag, node, parent2);
        node = new DefaultMutableTreeNode("ECS Stop Payment");
//        parent2.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList2(flag, node, parent2);
        node = new DefaultMutableTreeNode("MDS Application New");
//        parent2.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);
        node = new DefaultMutableTreeNode("Daily Loan Collection - Suspense collection");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);

        node = new DefaultMutableTreeNode("Daily Loan Collection - Loan Adjstment");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);
        
        node = new DefaultMutableTreeNode("Group loan Payments");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);
        
        node = new DefaultMutableTreeNode("Group loan Receipts");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);
        
        node = new DefaultMutableTreeNode("Group loan Customer");
        //parent1.add(node);
        flag = displayTransactionDetails(node);
        addinginParentList2(flag, node, parent2);
        
        node = new DefaultMutableTreeNode("Other Bank Accounts Master");
        flag = displayTransactionDetails(node);
        addinginParentList2(flag, node, parent2);
        
        node = new DefaultMutableTreeNode("Pension Scheme");
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);
        
        node = new DefaultMutableTreeNode("Director Board Meeting");
        flag = displayTransactionDetails(node);
        addinginParentList1(flag, node, parent1);
        
        
       String autoAuthorizeAllowed = CommonUtil.convertObjToStr(TrueTransactMain.CBMSPARAMETERS.get("LOAN_NOTICE_AUTO_AUTHORIZE")); 
        if(autoAuthorizeAllowed != null && autoAuthorizeAllowed.equals("N")){
            node = new DefaultMutableTreeNode("LOAN_NOTICE");
            flag = displayTransactionDetails(node);
            addinginParentList1(flag, node, parent1);
        }
        
        
        final DefaultTreeModel treemodel = new DefaultTreeModel(parent);
        treData.setModel(treemodel);
        root = null;

        return treemodel;
    }

    public void displayDetails(String nodeSelected) {
        
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
        currDt = (Date) currDt.clone();
//        ProxyParameters.BRANCH_ID = "0001";
        List lst = new ArrayList();
        if (nodeSelected.equals("Customer Master")) {
            whereMap.put(CommonConstants.MAP_NAME, "getUnAuthorizedListForCustomer");
        }else if (nodeSelected.equals("Corporate Customer")) {
            whereMap.put(CommonConstants.MAP_NAME, "getUnAuthorizedListForCorpCustomer");
        }else if (nodeSelected.equals("Locker Master")) {
            where.put(CommonConstants.AUTHORIZESTATUS, "Authorize"); 
            whereMap.put(CommonConstants.MAP_NAME, "getSelectLockerMasterEditAuthorizeTOList");
        }else if (nodeSelected.equals("Inventory Details")) {
            whereMap.put(CommonConstants.MAP_NAME, "getSelectInventoryDetailsTOList");
        } else if (nodeSelected.equals("Share")) {
            where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
            whereMap.put(CommonConstants.MAP_NAME, "viewAllShareAcctAuthorizeTOListWithoutShareDetails");
        } else if (nodeSelected.equals("SB/Current Account Opening")) {
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectAccountMasterCashierAuthorizeTOListWithTransaction");
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectAccountMasterAuthorizeTOListWithTransaction");
            }
        } else if (CommonUtil.convertObjToStr(node).equals("SB/Current AccountOpening")) {
             whereMap.put(CommonConstants.MAP_NAME, "getSelectAccountMasterAuthorizeTOListWithoutTransaction");
//            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
//                whereMap.put(CommonConstants.MAP_NAME, "getSelectAccountMasterCashierAuthorizeTOList");
//            } else {
//                whereMap.put(CommonConstants.MAP_NAME, "getSelectAccountMasterAuthorizeTOListWithoutTransaction");
//            }
        } else if (nodeSelected.equals("Account Closing")) {
            where.put("AUTHORIZESTATUS", "AUTHORIZED");
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectAccountCloseCashierAuthorizeTOList");
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectAccountCloseAuthorizeTOList");
            }

        } else if (nodeSelected.equals("Deposit Accounts")) {
            where.put("OPENING_MODE", "Normal");
            where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "viewAllDepAccAuthorizeCashierTOList");
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "viewAllDepAccAuthorizeTOList");
            }

        } else if (nodeSelected.equals("Deposit Account Renewal")) {
            where.put("OPENING_MODE", "Renewal");
            where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
//            whereMap.put(CommonConstants.MAP_NAME, "viewAllDepAccAuthorizeTOList");
            whereMap.put(CommonConstants.MAP_NAME, "viewAllDepAccRenewalAuthorizeTOListForAuthScreen");
        } 
       else if (CommonUtil.convertObjToStr(node).equals("Deposit AccountRenewal")) { //Added by Rishad for without Transaction
            where.put("OPENING_MODE", "Renewal");
            where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
            whereMap.put(CommonConstants.MAP_NAME, "viewAllDepAccAuthorizeTOListWithOutTransaction");
        } 
        else if (nodeSelected.equals("Deposit Account Closing")) {
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getDepositAccountCloseCashierAuthorizeTOList");
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getDepositAccountCloseAuthorizeTOList");
            }
        } else if (nodeSelected.equals("Deposit Multiple Closing")) {
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getMultipleDepositAccountCloseCashierAuthorizeTOList");
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getMultipleDepositAccountCloseAuthorizeTOList");
            }
        } else if (nodeSelected.equals("Multiple Deposit Account Opening")) {
            where.put("OPENING_MODE", "Normal");
            where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "viewAllMultipleDepAccAuthorizeCashierTOList");
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "viewAllMultipleDepAccAuthorizeTOList");
            }
        }else if (nodeSelected.equals("Multiple Deposit Creation")) {
            where.put("OPENING_MODE", "Normal");
            where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "viewAllMultipleDepAccAuthorizeCashierTOList");
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "viewAllMultipleDepAccAuthorizeTOList");
            }
        } else if (nodeSelected.equals("Gold Loan Account Opening")) {
            where.put("AUTHORIZE_REMARK", "!= 'GOLD_LOAN'");
            where.put("GOLD_LOAN", "GOLD_LOAN");
            where.put("AUTHORIZESTATUS", "AUTHORIZED");
            where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            where.put("STATUS_BY", ProxyParameters.USER_ID);
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                where.put("AUTH_TRANS_TYPE", "DEBIT");
                whereMap.put(CommonConstants.MAP_NAME, "getSelectTermLoanCashierAuthorizeTOList");
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectTermLoanAuthorizeTOList");
            }

        } else if (nodeSelected.equals("Loans/Advances Account Opening")) {
            where.put("AUTHORIZE_REMARK", "= 'GOLD_LOAN'");
            where.put("AUTHORIZESTATUS", "AUTHORIZED");
            where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            where.put("STATUS_BY", ProxyParameters.USER_ID);
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                where.put("AUTH_TRANS_TYPE", "DEBIT");
                whereMap.put(CommonConstants.MAP_NAME, "getSelectTermLoanCashierAuthorizeTOList");
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectTermLoanAuthorizeTOList");
            }

        } else if (nodeSelected.equals("Deposit Loan Account Opening")) {
            where.put("AUTHORIZESTATUS", "AUTHORIZED");
            where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            where.put("STATUS_BY", ProxyParameters.USER_ID);
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                where.put("AUTH_TRANS_TYPE", "DEBIT");
                whereMap.put(CommonConstants.MAP_NAME, "getSelectTermLoanCashierAuthorizeTOListForLTD");
            } else {
                where.put("AUTH_TRANS_TYPE", "DEBIT");
                whereMap.put(CommonConstants.MAP_NAME, "getSelectTermLoanAuthorizeTOListForLTD");
            }
          
        } else if (nodeSelected.equals("Loan Closing")) {
            where.put("AUTHORIZESTATUS", "AUTHORIZED");
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectLoanAccountCloseCashierAuthorizeTOList");
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectLoanAccountCloseAuthorizeTOList");
            }

        } else if (nodeSelected.equals("Cash Transactions")) {
            where.put("AUTHORIZESTATUS", "AUTHORIZED");
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectCashTransactionCashierAuthorizeTOList");
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectCashTransactionAuthorizeTOList");
            }
        } else if (nodeSelected.equals("Transfer")) {
            where.put("AUTHORIZESTATUS", "AUTHORIZED");
            whereMap.put(CommonConstants.MAP_NAME, "getUnAuthorizeMasterTransferTO");
        } else if (nodeSelected.equals("Share Account")) {
            where.put("AUTHORIZESTATUS", "AUTHORIZED");
            where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
            where.put("TRANS_DT", currDt);
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "viewAllShareAcctCashierAuthorizeTOList");
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "viewAllShareAcctAuthorizeTOList11");
            }

        } else if (nodeSelected.equals("MDS Receipts")) {
            where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectNonAuthRecordForCashierReceipt");
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectNonAuthRecordForReceipt");
            }

        } 
//        else if (nodeSelected.equals("MDS Prized Money Payment")) {
//            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
//                whereMap.put(CommonConstants.MAP_NAME, "getPrizedMoneyPaymentCashierAuthorize");
//            } else {
//                whereMap.put(CommonConstants.MAP_NAME, "getPrizedMoneyPaymentAuthorize");
//            }
//
//        } 
         else if (nodeSelected.equals("MDS Member")) {
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getMemberReceiptCashierAuthorize");
            } else {
               whereMap.put(CommonConstants.MAP_NAME, "getMemberReceiptAuthorize");
            }

        } 
        else if (nodeSelected.equals("Lodgement")) {
            whereMap.put(CommonConstants.MAP_NAME, "getLodgementMasterAuthorizeList");
        } else if (nodeSelected.equals("InvestmentTrans")) {
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getInvestmentTransCashierAuthorizeList");
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getInvestmentTransAuthorizeList");
            }
        } //        else if (node.equals("Cheque Book Issued")) {
        //             if(panChequeBookIssue.isShowing()==true){
        //            whereMap.put(CommonConstants.MAP_NAME, "getPrizedMoneyPaymentAuthorize");
        //        }
        else if (nodeSelected.equals("MDS Prized Money Details")) {
            whereMap.put(CommonConstants.MAP_NAME, "getPrizedMoneyDetailsEntryAuthorize");
        } else if (nodeSelected.equals("MDS Master Maintenance")) {
            whereMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            whereMap.put(CommonConstants.MAP_NAME, "getMDSMasterMaintenanceAuthorize");
        } else if (nodeSelected.equals("Borrowing Disbursal")) {
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getBorrowingDisbursalCashierAuthorizeList");
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getBorrowingDisbursalAuthorizeList");
            }
            //whereMap.put(CommonConstants.MAP_NAME, "getMDSMasterMaintenanceAuthorize");
        } else if (nodeSelected.equals("Borrowing Master")) {
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getBorrowingCashierAuthorizeList");
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getBorrowingAuthorizeList");
            }
            //whereMap.put(CommonConstants.MAP_NAME, "getMDSMasterMaintenanceAuthorize");
        } else if (nodeSelected.equals("Borrowing Repayment")) {
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getBorrowingRepIntClsCashierAuthorizeList");
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getBorrowingRepIntClsAuthorizeList");
            }
        } else if (nodeSelected.equals("Other Bank Account Opening")) {
            //whereMap.put(CommonConstants.MAP_NAME, "getSelectABAuthDetails");
            // whereMap.put(CommonConstants.MAP_NAME, "viewAuthorizeOtherBank");
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectABAuthDetails");
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectABAuthDetailsWithOutCashier");
            }

        } else if (nodeSelected.equals("Locker Renew/Surrender")) {
            //whereMap.put(CommonConstants.MAP_NAME, "getSelectLockerAuthorize");
            // Added by nithya on 24-02-2020 for KD-2711
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {                 
                 whereMap.put(CommonConstants.MAP_NAME, "getSelectLockerCashierAuthorize");
             }else{
            whereMap.put(CommonConstants.MAP_NAME, "getSelectLockerAuthorize");
             } 
        } 
        else if (nodeSelected.equals("Locker Issue")) {
            where.put(CommonConstants.AUTHORIZESTATUS, "Authorize");
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
               whereMap.put(CommonConstants.MAP_NAME, "getSelectLockerMasterCashierAuthorizeTOList"); 
            }else{
            whereMap.put(CommonConstants.MAP_NAME, "getSelectLockerMasterAuthorizeTOList");
            }
        }
        else if (nodeSelected.equals("Rent Transaction")) {
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getRentTransCashierAuthorizeList");
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getRentTransAuthorizeList");
            }
        } else if (nodeSelected.equals("Indend Transaction")) {
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getIndendCashierAuthorizeList");
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getIndendAuthorizeList");
            }
        } else if (nodeSelected.equals("Multiple Cash Tranasction")) {
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getCashierAuthorizationsListForMutipleCash");
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getAuthorizationsListForMutipleCash");
            }
        } else if (nodeSelected.equals("Purchase Entry")) {
            whereMap.put(CommonConstants.MAP_NAME, "getPurchaseEntryAuthorizeList");
        } else if (nodeSelected.equals("Loan Application Register")) {
            where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getLoanApplicationAuthorizeListForCashierAuth");
            } else {
                //whereMap.put(CommonConstants.MAP_NAME, "getLoanApplicationAuthorizeList");
                whereMap.put(CommonConstants.MAP_NAME,"getLoanApplicationAuthorizeListWithOutCahsier");// Added by nithya on 19-05-2018 for 0010443: LOAN APPLICATION LAND INSPECTION RECEIPT AUTHORIZATION ISSUE  
            }

        } else if (nodeSelected.equals("MDS Application New")) {
            where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
           if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectNonAuthRecordForEntered");
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectNonAuthRecordForEnteredWithOutCashier");
            }
        } else if (nodeSelected.equals("Share Dividend Payment")) {
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getDividendPaymentTransferAuthMode");
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getDividendPaymentAuthWithOutCashier");
            }
        } else if (nodeSelected.equals("DRF")) {
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getDrfTransferAuthMode");
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getDrfTransferAuthModeWithOutCashier");
            }
        } else if (nodeSelected.equals("Account Head")) {
           // whereMap.put(CommonConstants.MAP_NAME, "getSelectAccCreationAuthorizeTOList");
            whereMap.put(CommonConstants.MAP_NAME, "getSelectAccMaintainAuthorizeTOList");
        } else if (nodeSelected.equals("Gahan Customer")) {
            whereMap.put(CommonConstants.MAP_NAME, "getSelectGahanaAuthList");
        } else if (nodeSelected.equals("Standing Instruction")) {
            whereMap.put(CommonConstants.MAP_NAME, "getStandingInstructionAuthorizeList");
        } else if (nodeSelected.equals("InvestmentMaster")) {
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectInvestmentCashierAuthDetails");
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectInvestmentAuthDetails");
            }
        } else if (nodeSelected.equals("Cheque Book Issue")) {
            whereMap.put(CommonConstants.MAP_NAME, "getSelectChequeBookIssueTOList");
        } else if (nodeSelected.equals("Stop Payment Issue")) {
            whereMap.put(CommonConstants.MAP_NAME, "getSelectStopPaymentIssueTOList");
        } else if (nodeSelected.equals("Loose Leaf Issue")) {
            whereMap.put(CommonConstants.MAP_NAME, "getSelectLooseLeafIssueTOList");
        } else if (nodeSelected.equals("ECS Stop Payment")) {
            whereMap.put(CommonConstants.MAP_NAME, "getSelectEcsStopPaymentIssueTOList");
        } else if (nodeSelected.equals("Daily Loan Collection - Loan Adjstment")) {
            whereMap.put(CommonConstants.MAP_NAME, "getAdjustmentDailyLoanTransAuthorize");
        } else if (nodeSelected.equals("Daily Loan Collection - Suspense collection")) {
            whereMap.put(CommonConstants.MAP_NAME, "getDailyLoanTransAuthorize");
        }else if (nodeSelected.equals("Loan Application With out Transaction")) {
            whereMap.put(CommonConstants.MAP_NAME, "getLoanApplicationAuthorizeListWithOutTransaction");
        }else if (nodeSelected.equals("Group loan Payments")) {
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getGroupLoanPaymentForAuthCashUI");
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getGroupLoanPaymentForAuthUI");
            }
        }else if (nodeSelected.equals("Group loan Receipts")) {
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getGroupLoanReceiptForAuthCashUI");
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getGroupLoanReceiptForAuthUI");                
            }
        }else if (nodeSelected.equals("Group loan Customer")) {             
            whereMap.put(CommonConstants.MAP_NAME, "getGroupAuthorizeCustomer");              
        } else if (nodeSelected.equals("Other Bank Accounts Master")) {
            whereMap.put(CommonConstants.MAP_NAME, "getSelectABAuthDetailsWithOutTransaction");
        }else if (CommonUtil.convertObjToStr(node).equals("Pension Scheme")) {
            where.put("USER_ID", TrueTransactMain.USER_ID);
            where.put("TRANS_DT", currDt);
            where.put(CommonConstants.AUTHORIZESTATUS, "AUTHORIZED");
            where.put(CommonConstants.INITIATED_BRANCH, ProxyParameters.BRANCH_ID);
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getPensionSchemeAuthorizeCashUI");
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getPensionSchemeAuthorizeUI");                
            }
        }else if (CommonUtil.convertObjToStr(node).equals("Director Board Meeting")) {
            where.put("USER_ID", TrueTransactMain.USER_ID);
            where.put("TRANS_DT", currDt);
            where.put(CommonConstants.AUTHORIZESTATUS, "AUTHORIZED");
            where.put(CommonConstants.INITIATED_BRANCH, ProxyParameters.BRANCH_ID);
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getDirectorBoardAuthorizeList");
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getDirectorBoardAuthorizeList");                
            }
        }else if (CommonUtil.convertObjToStr(node).equals("GDSReceiptEntry")) {  // Started GDS Module changes  
                where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
                whereMap.put(CommonConstants.MAP_NAME, "getGDSSelectNonAuthRecordForReceipt");                       
        } else if (CommonUtil.convertObjToStr(node).equals("GDS Prized Money Payment")) {    
                where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
                whereMap.put(CommonConstants.MAP_NAME, "getGDSPrizedMoneyPaymentAuthorize");                
        }else if (nodeSelected.equals("GDSApplication")) {
            where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);           
            whereMap.put(CommonConstants.MAP_NAME, "getGDSSelectNonAuthRecordForEnteredWithOutCashier");           
        }else if (nodeSelected.equals("GDS Bank Advance")) { // Added by nithya on 20-12-2019 for KD-887
            where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);           
            whereMap.put(CommonConstants.MAP_NAME, "getGDSBankAdvanceAuthorize");           
        }else if (nodeSelected.equals("MDS Receipt Entry")) {
            where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectNonAuthRecordForCashierReceipt");
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectNonAuthRecordForReceipt");
            }

        } else if (nodeSelected.equals("MDS Prized Money Payment")) {
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getPrizedMoneyPaymentCashierAuthorize");
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getPrizedMoneyPaymentAuthorize");
            }

        }else if (nodeSelected.equals("MDS Receipt Entry After Closure")) {
            where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectNonAuthRecordForCashierReceipt");                
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectNonAuthRecordForReceipt");              
            }
        }else if (nodeSelected.equals("MDS Commencement/Closure")) {
            where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);            
            whereMap.put(CommonConstants.MAP_NAME, "getSelectCommencementList");                  
        }else if (nodeSelected.equals("MDS Bank Advance")) { // Added by nithya on 18-08-2020 for KD-2162
            where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);            
            whereMap.put(CommonConstants.MAP_NAME, "getBankAdvanceAuthorize");                  
        }else if (nodeSelected.equals("LOAN_NOTICE")) {
            where.put("USER_ID", TrueTransactMain.USER_ID);
            where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            where.put("CHARGE_DATE", currDt.clone());
            where.put("AUTHORIZE_MODE","AUTHORIZE_MODE");         
            whereMap.put(CommonConstants.MAP_NAME, "getAuthorizeLoanNoticeList");                               
        }else if (CommonUtil.convertObjToStr(node).equals("View All Transactions")) { //Added By Kannan AR
            CInternalFrame viewAllFrm = new com.see.truetransact.ui.common.viewall.ViewAllTransactions();
            TrueTransactMain.showScreen(viewAllFrm);
            HashMap viewAllMap = new HashMap();
            viewAllMap.put("NEW_FROM_AUTHORIZE_LIST_UI", "");
            viewAllMap.put("PARENT", this);
            viewAllFrm.fillData(viewAllMap);
        }
        if (!CommonUtil.convertObjToStr(node).equals("View All Transactions")) {
            whereMap.put(CommonConstants.MAP_WHERE, where);
            if (whereMap.containsKey(CommonConstants.MAP_NAME)) {
                whereMap = ClientUtil.executeTableQuery(whereMap);
                _heading = (ArrayList) whereMap.get(CommonConstants.TABLEHEAD);
                data = (ArrayList) whereMap.get(CommonConstants.TABLEDATA);
            }
            populateTable();
        }
//        HashMap hash = (HashMap) obj;
        System.out.println("#$#$ whereMap : " + whereMap);
    }
    boolean btnIntDetPressed = false;

    public void fillData(Object obj) {
        HashMap hash = (HashMap) obj;
        System.out.println("#$#$ Hash : " + hash);
    }
    
    //Added By Kannan AR
    public void resetByViewAllTrans(){
        getUnauthorizedTrees();
        TableModel tableModel = new TableModel();
        tblData.setModel(tableModel);
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
                if (i > 8) {
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
    
    private class setColour extends DefaultTableCellRenderer {
        /* Set a cellrenderer to this table in order format the date */
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                // if (data.contains(String.valueOf(row))) {
                    setForeground(Color.RED);
                    setBackground(Color.BLACK);
                    setFont(new java.awt.Font("Tahoma", Font.PLAIN, 12));
               // } else {
               //     setForeground(Color.BLACK);
              //  }
                    System.out.println ("hereererre");
                // Set oquae
                this.setOpaque(true);
                return this;
            }
        };
        //tblData.setDefaultRenderer(Object.class, renderer);
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
            if(cashierRejected(CommonUtil.convertObjToStr(tblData.getValueAt(rowIndexSelected, 0)))){
                ClientUtil.showAlertWindow("Transaction is Rejected by Cashier...please reject transaction through  "+node+"  module!!!");
                return;
            }
            
            if(node.equals("Multiple Deposit Creation") && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")){
                if(!isAllMultipleDepositCashEntriesApproved(CommonUtil.convertObjToStr(tblData.getValueAt(rowIndexSelected, 1)))){
                  ClientUtil.showAlertWindow("All multiple deposits are not approved by cashier. Please Check !!");
                  return;  
                }
            }
            
//            this.dispose();
            HashMap hash = fillData(rowIndexSelected);
            if (node.equals("Customer Master")) {
                frm = new com.see.truetransact.ui.customer.IndividualCustUI();
            }else  if (node.equals("Corporate Customer")) {
                frm = new com.see.truetransact.ui.customer.CorporateCustomerUI();
            }else  if (node.equals("Locker Master")) {
                frm = new com.see.truetransact.ui.locker.lockerissue.LockerIssueUI();
            }else  if (node.equals("Inventory Details")) {
                frm = new com.see.truetransact.ui.supporting.inventory.InventoryDetailsUI();
            } else if(node.equals("Share")){
                hash.put("SHARE", "SHARE");
                frm = new com.see.truetransact.ui.share.ShareUI();
            }
            else if (node.equals("SB/Current Account Opening")) {
                frm = new com.see.truetransact.ui.operativeaccount.AccountsUI();
            } 
             else if (node.equals("SB/Current AccountOpening")) {
                frm = new com.see.truetransact.ui.operativeaccount.AccountsUI();
            } 
            else if (node.equals("Account Closing")) {
                frm = new com.see.truetransact.ui.operativeaccount.AccountClosingUI();
            } else if (node.equals("Deposit Accounts")) {
                frm = new com.see.truetransact.ui.deposit.TermDepositUI();
            } else if (node.equals("Deposit Account Renewal")) {
                frm = new com.see.truetransact.ui.deposit.TermDepositUI();
            }
            else if (node.equals("Deposit AccountRenewal")) {
                frm = new com.see.truetransact.ui.deposit.TermDepositUI();
            }
            else if (node.equals("Deposit Account Closing")) {
                frm = new com.see.truetransact.ui.deposit.closing.DepositClosingUI();
            } else if (node.equals("Deposit Multiple Closing")) {
                frm = new com.see.truetransact.ui.deposit.multipleclosing.DepositMultiClosingUI();
            } else if (node.equals("Multiple Deposit Account Opening")) {
                frm = new com.see.truetransact.ui.deposit.multipledeposit.MultipleTermDepositUI();
            }else if (node.equals("Multiple Deposit Creation")) {
                frm = new com.see.truetransact.ui.deposit.multipledeposit.MultipleTermDepositUI();
            } else if (node.equals("Gold Loan Account Opening")) {
//                frm = new com.see.truetransact.ui.termloan.GoldLoanUI("OTHERS", CommonUtil.convertObjToStr(hash.get("PROD_ID")));
                frm = new com.see.truetransact.ui.termloan.GoldLoanUI();
            } else if (node.equals("Loans/Advances Account Opening")) {
                frm = new com.see.truetransact.ui.termloan.TermLoanUI("OTHERS");
            } else if (node.equals("Deposit Loan Account Opening")) {
                frm = new com.see.truetransact.ui.termloan.depositLoan.DepositLoanUI();
            } else if (node.equals("Loan Closing")) {
                frm = new com.see.truetransact.ui.operativeaccount.AccountClosingUI("TermLoan");
            } else if (node.equals("Cash Transactions")) {
                frm = new com.see.truetransact.ui.transaction.cash.CashTransactionUI();
            } else if (node.equals("Transfer")) {
                frm = new com.see.truetransact.ui.transaction.transfer.TransferUI();
            } else if (node.equals("Share Account")) {
                hash.put("SHARE_ACCT", "SHARE_ACCT");
                frm = new com.see.truetransact.ui.share.ShareUI();
            } else if (node.equals("MDS Receipts")) {
                frm = new com.see.truetransact.ui.mdsapplication.mdsreceiptentry.MDSReceiptEntryUI();
            } 
            else if (node.equals("MDS Payments")) {
                frm = new com.see.truetransact.ui.mdsapplication.mdsprizedmoneypayment.MDSPrizedMoneyPaymentUI();
            }
             else if (node.equals("MDS Member")) {
                 frm = new com.see.truetransact.ui.mdsapplication.mdsmemberreceiptentry.MDSMemberReceiptEntryUI();
            }
            else if (node.equals("Lodgement")) {
                frm = new com.see.truetransact.ui.bills.lodgement.LodgementBillsUI();
            } else if (node.equals("Investment Trans")) {
                frm = new com.see.truetransact.ui.investments.InvestmentsTransUI();
            } else if (node.equals("MDS Prized Money Details")) {
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
            } else if (node.equals("Locker Renew/Surrender")) {
                frm = new com.see.truetransact.ui.locker.lockersurrender.LockerSurrenderUI();
            } else if (node.equals("Locker Issue")) {
                frm = new com.see.truetransact.ui.locker.lockerissue.LockerIssueUI();
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
                 hash.put("LOAN_APPLICATION", "LOAN_APPLICATION");
            }else if (node.equals("Loan Application With out Transaction")) {
                frm = new com.see.truetransact.ui.termloan.loanapplicationregister.LoanApplicationUI();
                hash.put("LOAN_APPLICATION_WITHOUT_TRANS", "LOAN_APPLICATION_WITHOUT_TRANS");
            } else if (node.equals("MDS Application New")) {
                frm = new com.see.truetransact.ui.mdsapplication.MDSApplicationUI();
            } else if (node.equals("Share Dividend Payment")) {
                frm = new com.see.truetransact.ui.share.ShareDividendPaymentUI();
            } else if (node.equals("DRF")) {
                frm = new com.see.truetransact.ui.share.DrfTransactionUI();
            } else if (node.equals("InvestmentMaster")) {
                frm = new com.see.truetransact.ui.investments.InvestmentsMasterUI();
            } else if (node.equals("Account Head")) {
                frm = new com.see.truetransact.ui.generalledger.AccountMaintenanceUI();
               // frm = new com.see.truetransact.ui.generalledger.AccountCreationUI();
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
            } else if (node.equals("Daily Loan Collection - Suspense collection")) {
                frm = new com.see.truetransact.ui.termloan.dailyLoanTrans.DailyLoanTransUI();
                hash.put("NEW_FROM_AUTHORIZE_LIST_UI_SUSPENSE_COLLECTION", "");
            } else if (node.equals("Daily Loan Collection - Loan Adjstment")) {
                frm = new com.see.truetransact.ui.termloan.dailyLoanTrans.DailyLoanTransUI();
                hash.put("NEW_FROM_AUTHORIZE_LIST_UI_LOAN_ADJUSTMENT", "");
            }else if (node.equals("Group loan Payments")) {
                frm = new com.see.truetransact.ui.termloan.groupLoan.GroupLoanPaymentUI();
            } else if (node.equals("Group loan Receipts")) {
                frm = new com.see.truetransact.ui.termloan.groupLoan.GroupLoanUI();
            }else if (node.equals("Group loan Customer")) {
                frm = new com.see.truetransact.ui.termloan.groupLoan.GroupLoanCustomerUI();
            }else if (node.equals("Other Bank Accounts Master")) {
                frm = new com.see.truetransact.ui.accountswithotherbank.AccountswithOtherBanksUI();
            }else if (node.equals("Pension Scheme")) {
                frm = new com.see.truetransact.ui.share.pensionScheme.PensionSchemeUI();
            }else if (node.equals("Director Board Meeting")) {
                frm = new com.see.truetransact.ui.directorboardmeeting.DirectorBoardUI();
            }else if (node.equals("GDSReceiptEntry")) {
                frm = new com.see.truetransact.ui.gdsapplication.gdsreceiptentry.GDSReceiptEntryUI();
            }else if (node.equals("GDSApplication")) {
                frm = new com.see.truetransact.ui.gdsapplication.GDSApplicationUI();      
            }else if (node.equals("GDS Bank Advance")) { // Added by nithya on 20-12-2019 for KD-887
                frm = new com.see.truetransact.ui.gdsapplication.gdsbankadvance.GDSBankAdvanceUI();      
            }else if (node.equals("GDS Prized Money Payment")) {
                frm = new com.see.truetransact.ui.gdsapplication.gdsprizedmoneypayment.GDSPrizedMoneyPaymentUI();
            }else if (node.equals("MDS Receipt Entry")) {
                frm = new com.see.truetransact.ui.mdsapplication.mdsreceiptentry.MDSReceiptEntryUI();
            }else if (node.equals("MDS Prized Money Payment")) {
                frm = new com.see.truetransact.ui.mdsapplication.mdsprizedmoneypayment.MDSPrizedMoneyPaymentUI();
            }else if (node.equals("MDS Receipt Entry After Closure")) {
                frm = new com.see.truetransact.ui.mdsapplication.mdsclosedreceipt.MDSClosedReciptUI();
            }else if (node.equals("MDS Commencement/Closure")) {
                frm = new com.see.truetransact.ui.mdsapplication.mdsconmmencement.MDSCommencementUI();
            }else if (node.equals("MDS Bank Advance")) { // Added by nithya on 19-08-2020
                frm = new com.see.truetransact.ui.mdsapplication.mdsbankadvance.MDSBankAdvanceUI();
            }else if (node.equals("LOAN_NOTICE")) {
                frm = new com.see.truetransact.ui.termloan.notices.LoanNoticeUI();		
            }
            hash.put("NEW_FROM_AUTHORIZE_LIST_UI", "");
            frm.setSelectedBranchID(getSelectedBranchID());
            TrueTransactMain.showScreen(frm);
//            frm.setSwitchEnglish(false);
            System.out.println("Haash Before" + hash);
            hash.put("PARENT", this);
            frm.fillData(hash);
            System.out.println("#$#$ Final Hash : after " + hash);
        }
    }

    public boolean cashierRejected(String transID){
        HashMap rejectMap = new HashMap();
        rejectMap.put("TRANS_ID", transID);
        rejectMap.put("TRANS_DT", currDt);
        List rejectList = (List) ClientUtil.executeQuery("getCashierRejectedTransaction", rejectMap);
        if (rejectList != null && rejectList.size() > 0) {
            return true;
        }
        else{
            return false;
        }        
    }
    
    public boolean isAllMultipleDepositCashEntriesApproved(String transID){
        boolean approved = true; 
        HashMap approvalMap = new HashMap();
        approvalMap.put("GL_TRANS_ACT_NUM", transID);
        approvalMap.put("TRANS_DT", currDt);
        List approvalLst = (List) ClientUtil.executeQuery("checkAllMultipleDepositCashEntriesApproved", approvalMap);
        if (approvalLst != null && approvalLst.size() > 0) {
            approvalMap = (HashMap)approvalLst.get(0);
            if(approvalMap.containsKey("CNT") && approvalMap.get("CNT") != null && CommonUtil.convertObjToDouble(approvalMap.get("CNT")) > 0){
                approved = false;
            }else{
                approved = true;
            }
        }
        return approved;
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
        if (evt.getClickCount() == 2) {
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

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
        setCurBal();
    }//GEN-LAST:event_btnRefreshActionPerformed

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

    private void btnTransIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransIdActionPerformed
        // TODO add your handling code here:

        if (node.equals("SB/Current Account Opening")) {
            if (tblData.getSelectedRow() == -1) {
                ClientUtil.showAlertWindow("Please select a row in the table grid");
                return;
            }
            HashMap getTransMap = new HashMap();
            HashMap cashMap = new HashMap();
            HashMap transferMap = new HashMap();
            getTransMap.put("BATCH_ID", tblData.getValueAt(tblData.getSelectedRow(), 0).toString());
//        getTransMap.put("TRANS_DT", currDt);
            getTransMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
//        returnMap = new HashMap();
            List transList = (List) ClientUtil.executeQuery("getTransferDetailsForAuthorizeList", getTransMap);
            if (transList != null && transList.size() > 0) {
                transferMap.put("TRANSFER_TRANS_LIST", transList);
                displayTransDetail(transferMap);
            }
            List cashList = (List) ClientUtil.executeQuery("getCashDetailsForAuthorizeList", getTransMap);
            if (cashList != null && cashList.size() > 0) {
                cashMap.put("CASH_TRANS_LIST", cashList);
                displayTransDetail(cashMap);
            }

        } else if (node.equals("Account Closing")) {
            if (tblData.getSelectedRow() == -1) {
                ClientUtil.showAlertWindow("Please select a row in the table grid");
                return;
            }
            HashMap getTransMap = new HashMap();
            HashMap cashMap = new HashMap();
            HashMap transferMap = new HashMap();
            getTransMap.put("BATCH_ID", tblData.getValueAt(tblData.getSelectedRow(), 0).toString());
//        getTransMap.put("TRANS_DT", currDt);
            getTransMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
//        returnMap = new HashMap();
            List transList = (List) ClientUtil.executeQuery("getTransferDetailsForAuthorizeList", getTransMap);
            if (transList != null && transList.size() > 0) {
                transferMap.put("TRANSFER_TRANS_LIST", transList);
                displayTransDetail(transferMap);
            }
            List cashList = (List) ClientUtil.executeQuery("getCashDetailsForAuthorizeList", getTransMap);
            if (cashList != null && cashList.size() > 0) {
                cashMap.put("CASH_TRANS_LIST", cashList);
                displayTransDetail(cashMap);
            }

        } else if (node.equals("Deposit Accounts")) {
            if (tblData.getSelectedRow() == -1) {
                ClientUtil.showAlertWindow("Please select a row in the table grid");
                return;
            }
            HashMap getTransMap = new HashMap();
            HashMap cashMap = new HashMap();
            HashMap transferMap = new HashMap();
            getTransMap.put("BATCH_ID", tblData.getValueAt(tblData.getSelectedRow(), 0).toString() + "_1");
//        getTransMap.put("TRANS_DT", currDt);
            getTransMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
//        returnMap = new HashMap();
            List transList = (List) ClientUtil.executeQuery("getTransferDetailsForAuthorizeList", getTransMap);
            if (transList != null && transList.size() > 0) {
                transferMap.put("TRANSFER_TRANS_LIST", transList);
                displayTransDetail(transferMap);
            }
            List cashList = (List) ClientUtil.executeQuery("getCashDetailsForAuthorizeList", getTransMap);
            if (cashList != null && cashList.size() > 0) {
                cashMap.put("CASH_TRANS_LIST", cashList);
                displayTransDetail(cashMap);
            }

        } else if (node.equals("Deposit Account Renewal")) {
            if (tblData.getSelectedRow() == -1) {
                ClientUtil.showAlertWindow("Please select a row in the table grid");
                return;
            }
            HashMap getTransMap = new HashMap();
            HashMap cashMap = new HashMap();
            HashMap transferMap = new HashMap();
            getTransMap.put("BATCH_ID", tblData.getValueAt(tblData.getSelectedRow(), 0).toString() + "_1");
//        getTransMap.put("TRANS_DT", currDt);
            getTransMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            System.out.println("########getTransMap:" + getTransMap);
//        returnMap = new HashMap();
            List transList = (List) ClientUtil.executeQuery("getTransferDetailsForAuthorizeList", getTransMap);
            if (transList != null && transList.size() > 0) {
                transferMap.put("TRANSFER_TRANS_LIST", transList);
                displayTransDetail(transferMap);
            }
            List cashList = (List) ClientUtil.executeQuery("getCashDetailsForAuthorizeList", getTransMap);
            if (cashList != null && cashList.size() > 0) {
                cashMap.put("CASH_TRANS_LIST", cashList);
                displayTransDetail(cashMap);
            }
        } else if (node.equals("Deposit Account Closing")) {
            if (tblData.getSelectedRow() == -1) {
                ClientUtil.showAlertWindow("Please select a row in the table grid");
                return;
            }
            HashMap getTransMap = new HashMap();
            HashMap cashMap = new HashMap();
            HashMap transferMap = new HashMap();
            getTransMap.put("BATCH_ID", tblData.getValueAt(tblData.getSelectedRow(), 0).toString() + "_1");
//        getTransMap.put("TRANS_DT", currDt);
            getTransMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
//        returnMap = new HashMap();
            List transList = (List) ClientUtil.executeQuery("getTransferDetailsForAuthorizeList", getTransMap);
            if (transList != null && transList.size() > 0) {
                transferMap.put("TRANSFER_TRANS_LIST", transList);
                displayTransDetail(transferMap);
            }
            List cashList = (List) ClientUtil.executeQuery("getCashDetailsForAuthorizeList", getTransMap);
            if (cashList != null && cashList.size() > 0) {
                cashMap.put("CASH_TRANS_LIST", cashList);
                displayTransDetail(cashMap);
            }

        } else if (node.equals("Deposit Multiple Closing")) {
            if (tblData.getSelectedRow() == -1) {
                ClientUtil.showAlertWindow("Please select a row in the table grid");
                return;
            }
            HashMap getTransMap = new HashMap();
            HashMap cashMap = new HashMap();
            HashMap transferMap = new HashMap();
            getTransMap.put("BATCH_ID", tblData.getValueAt(tblData.getSelectedRow(), 0).toString() + "_1");
            getTransMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            List transList = (List) ClientUtil.executeQuery("getTransferDetailsForAuthorizeList", getTransMap);
            if (transList != null && transList.size() > 0) {
                transferMap.put("TRANSFER_TRANS_LIST", transList);
                displayTransDetail(transferMap);
            }
            List cashList = (List) ClientUtil.executeQuery("getCashDetailsForAuthorizeList", getTransMap);
            if (cashList != null && cashList.size() > 0) {
                cashMap.put("CASH_TRANS_LIST", cashList);
                displayTransDetail(cashMap);
            }
        } else if (node.equals("Gold Loan Account Opening")) {
            if (tblData.getSelectedRow() == -1) {
                ClientUtil.showAlertWindow("Please select a row in the table grid");
                return;
            }
            HashMap getTransMap = new HashMap();
            HashMap cashMap = new HashMap();
            HashMap transferMap = new HashMap();
            getTransMap.put("BATCH_ID", tblData.getValueAt(tblData.getSelectedRow(), 0).toString());
//        getTransMap.put("TRANS_DT", currDt);
            getTransMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
//        returnMap = new HashMap();
            List transList = (List) ClientUtil.executeQuery("getTransferDetailsForAuthorizeList", getTransMap);
            if (transList != null && transList.size() > 0) {
                transferMap.put("TRANSFER_TRANS_LIST", transList);
                displayTransDetail(transferMap);
            }
            List cashList = (List) ClientUtil.executeQuery("getCashDetailsForAuthorizeList", getTransMap);
            if (cashList != null && cashList.size() > 0) {
                cashMap.put("CASH_TRANS_LIST", cashList);
                displayTransDetail(cashMap);
            }
        } else if (node.equals("Loans/Advances Account Opening")) {
            if (tblData.getSelectedRow() == -1) {
                ClientUtil.showAlertWindow("Please select a row in the table grid");
                return;
            }
            HashMap getTransMap = new HashMap();
            HashMap cashMap = new HashMap();
            HashMap transferMap = new HashMap();
            getTransMap.put("BATCH_ID", tblData.getValueAt(tblData.getSelectedRow(), 0).toString());
//        getTransMap.put("TRANS_DT", currDt);
            getTransMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
//        returnMap = new HashMap();
            List transList = (List) ClientUtil.executeQuery("getTransferDetailsForAuthorizeList", getTransMap);
            if (transList != null && transList.size() > 0) {
                transferMap.put("TRANSFER_TRANS_LIST", transList);
                displayTransDetail(transferMap);
            }
            List cashList = (List) ClientUtil.executeQuery("getCashDetailsForAuthorizeList", getTransMap);
            if (cashList != null && cashList.size() > 0) {
                cashMap.put("CASH_TRANS_LIST", cashList);
                displayTransDetail(cashMap);
            }

        } else if (node.equals("Deposit Loan Account Opening")) {
            if (tblData.getSelectedRow() == -1) {
                ClientUtil.showAlertWindow("Please select a row in the table grid");
                return;
            }
            HashMap getTransMap = new HashMap();
            HashMap cashMap = new HashMap();
            HashMap transferMap = new HashMap();
            getTransMap.put("BATCH_ID", tblData.getValueAt(tblData.getSelectedRow(), 2).toString());
//        getTransMap.put("TRANS_DT", currDt);
            getTransMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
//        returnMap = new HashMap();
            List transList = (List) ClientUtil.executeQuery("getTransferDetailsForAuthorizeList", getTransMap);
            if (transList != null && transList.size() > 0) {
                transferMap.put("TRANSFER_TRANS_LIST", transList);
                displayTransDetail(transferMap);
            }
            List cashList = (List) ClientUtil.executeQuery("getCashDetailsForAuthorizeList", getTransMap);
            if (cashList != null && cashList.size() > 0) {
                cashMap.put("CASH_TRANS_LIST", cashList);
                displayTransDetail(cashMap);
            }

        } else if (node.equals("Loan Closing")) {
            if (tblData.getSelectedRow() == -1) {
                ClientUtil.showAlertWindow("Please select a row in the table grid");
                return;
            }
            HashMap getTransMap = new HashMap();
            HashMap cashMap = new HashMap();
            HashMap transferMap = new HashMap();
            getTransMap.put("BATCH_ID", tblData.getValueAt(tblData.getSelectedRow(), 0).toString());
//        getTransMap.put("TRANS_DT", currDt);
            getTransMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
//        returnMap = new HashMap();
            List transList = (List) ClientUtil.executeQuery("getTransferDetailsForAuthorizeList", getTransMap);
            if (transList != null && transList.size() > 0) {
                transferMap.put("TRANSFER_TRANS_LIST", transList);
                displayTransDetail(transferMap);
            }
            List cashList = (List) ClientUtil.executeQuery("getCashDetailsForAuthorizeList", getTransMap);
            if (cashList != null && cashList.size() > 0) {
                cashMap.put("CASH_TRANS_LIST", cashList);
                displayTransDetail(cashMap);
            }

//        } else if (node.equals("Cash Transactions")) {
//            if(tblData.getSelectedRow()==-1){
//           ClientUtil.showAlertWindow("Please select a row in the table grid");
//            return;
//            }
//            HashMap getTransMap = new HashMap();
//            HashMap cashMap = new HashMap();
//            HashMap transferMap = new HashMap();
////            getTransMap.put("BATCH_ID", batchId);
////        getTransMap.put("TRANS_DT", currDt);
//            getTransMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
//            //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
////        returnMap = new HashMap();
//            List transList = (List) ClientUtil.executeQuery("getTransferDetailsForAuthorizeList", getTransMap);
//            if (transList != null && transList.size() > 0) {
//                transferMap.put("TRANSFER_TRANS_LIST", transList);
//                displayTransDetail(transferMap);
//            }
//            List cashList = (List) ClientUtil.executeQuery("getCashDetailsForAuthorizeList", getTransMap);
//            if (cashList != null && cashList.size() > 0) {
//                cashMap.put("CASH_TRANS_LIST", cashList);
//                displayTransDetail(cashMap);
//            }
//        } else if (node.equals("Transfer Transactions")) {
//            HashMap getTransMap = new HashMap();
//            HashMap cashMap = new HashMap();
//            HashMap transferMap = new HashMap();
////            getTransMap.put("BATCH_ID", batchId);
////        getTransMap.put("TRANS_DT", currDt);
//            getTransMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
//            //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
////        returnMap = new HashMap();
//            List transList = (List) ClientUtil.executeQuery("getTransferDetailsForAuthorizeList", getTransMap);
//            if (transList != null && transList.size() > 0) {
//                transferMap.put("TRANSFER_TRANS_LIST", transList);
//                displayTransDetail(transferMap);
//            }
//            List cashList = (List) ClientUtil.executeQuery("getCashDetailsForAuthorizeList", getTransMap);
//            if (cashList != null && cashList.size() > 0) {
//                cashMap.put("CASH_TRANS_LIST", cashList);
//                displayTransDetail(cashMap);
//            }
        } else if (node.equals("Share Account")) {
            if (tblData.getSelectedRow() == -1) {
                ClientUtil.showAlertWindow("Please select a row in the table grid");
                return;
            }
            HashMap getTransMap = new HashMap();
            HashMap cashMap = new HashMap();
            HashMap transferMap = new HashMap();
            if (tblData.getValueAt(tblData.getSelectedRow(), 0) == null
                    || tblData.getValueAt(tblData.getSelectedRow(), 0).equals("")) {
                int cnt = 0;
                HashMap countMap = new HashMap();
                System.out.println("vvvvvvvvvvvvv" + tblData.getValueAt(tblData.getSelectedRow(), 1).toString());
                countMap.put("SHARE_ACCT_NO", tblData.getValueAt(tblData.getSelectedRow(), 1).toString());
                List countList = (List) ClientUtil.executeQuery("getShareCount", countMap);
                if (countList != null && countList.size() > 0) {
                    HashMap count = (HashMap) countList.get(0);
                    cnt = CommonUtil.convertObjToInt(count.get("COUNT"));
                    System.out.println("counttttttttt" + cnt);
                }
                getTransMap.put("BATCH_ID", tblData.getValueAt(tblData.getSelectedRow(), 1).toString() + "_" + cnt);
            } else {
                getTransMap.put("BATCH_ID", tblData.getValueAt(tblData.getSelectedRow(), 0).toString() + "_1");
            }
//        getTransMap.put("TRANS_DT", currDt);
            getTransMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
//        returnMap = new HashMap();
            List transList = (List) ClientUtil.executeQuery("getTransferDetailsForAuthorizeList", getTransMap);
            if (transList != null && transList.size() > 0) {
                transferMap.put("TRANSFER_TRANS_LIST", transList);
                displayTransDetail(transferMap);
            }
            List cashList = (List) ClientUtil.executeQuery("getCashDetailsForAuthorizeList", getTransMap);
            if (cashList != null && cashList.size() > 0) {
                cashMap.put("CASH_TRANS_LIST", cashList);
                displayTransDetail(cashMap);
            }

        } else if (node.equals("MDS Receipts")) {
            if (tblData.getSelectedRow() == -1) {
                ClientUtil.showAlertWindow("Please select a row in the table grid");
                return;
            }
            HashMap getTransMap = new HashMap();
            HashMap cashMap = new HashMap();
            HashMap transferMap = new HashMap();
            getTransMap.put("BATCH_ID", tblData.getValueAt(tblData.getSelectedRow(), 2).toString());
//        getTransMap.put("TRANS_DT", currDt);
            getTransMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
//        returnMap = new HashMap();
            List transList = (List) ClientUtil.executeQuery("getTransferDetailsForAuthorizeList", getTransMap);
            if (transList != null && transList.size() > 0) {
                transferMap.put("TRANSFER_TRANS_LIST", transList);
                displayTransDetail(transferMap);
            }
            List cashList = (List) ClientUtil.executeQuery("getCashDetailsForAuthorizeList", getTransMap);
            if (cashList != null && cashList.size() > 0) {
                cashMap.put("CASH_TRANS_LIST", cashList);
                displayTransDetail(cashMap);
            }

        } else if (node.equals("MDS Payments")) {
            if (tblData.getSelectedRow() == -1) {
                ClientUtil.showAlertWindow("Please select a row in the table grid");
                return;
            }
            HashMap getTransMap = new HashMap();
            HashMap cashMap = new HashMap();
            HashMap transferMap = new HashMap();
            getTransMap.put("BATCH_ID", tblData.getValueAt(tblData.getSelectedRow(), 3).toString());
//        getTransMap.put("TRANS_DT", currDt);
            getTransMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
//        returnMap = new HashMap();
            List transList = (List) ClientUtil.executeQuery("getTransferDetailsForAuthorizeList", getTransMap);
            if (transList != null && transList.size() > 0) {
                transferMap.put("TRANSFER_TRANS_LIST", transList);
                displayTransDetail(transferMap);
            }
            List cashList = (List) ClientUtil.executeQuery("getCashDetailsForAuthorizeList", getTransMap);
            if (cashList != null && cashList.size() > 0) {
                cashMap.put("CASH_TRANS_LIST", cashList);
                displayTransDetail(cashMap);
            }

        } else if (node.equals("Lodgement")) {
            if (tblData.getSelectedRow() == -1) {
                ClientUtil.showAlertWindow("Please select a row in the table grid");
                return;
            }
            HashMap getTransMap = new HashMap();
            HashMap cashMap = new HashMap();
            HashMap transferMap = new HashMap();
            getTransMap.put("BATCH_ID", tblData.getValueAt(tblData.getSelectedRow(), 0).toString());
//        getTransMap.put("TRANS_DT", currDt);
            getTransMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
//        returnMap = new HashMap();
            List transList = (List) ClientUtil.executeQuery("getTransferDetailsForAuthorizeList", getTransMap);
            if (transList != null && transList.size() > 0) {
                transferMap.put("TRANSFER_TRANS_LIST", transList);
                displayTransDetail(transferMap);
            }
            List cashList = (List) ClientUtil.executeQuery("getCashDetailsForAuthorizeList", getTransMap);
            if (cashList != null && cashList.size() > 0) {
                cashMap.put("CASH_TRANS_LIST", cashList);
                displayTransDetail(cashMap);
            }
        } else if (node.equals("Investment Trans")) {
            if (tblData.getSelectedRow() == -1) {
                ClientUtil.showAlertWindow("Please select a row in the table grid");
                return;
            }
            HashMap getTransMap = new HashMap();
            HashMap cashMap = new HashMap();
            HashMap transferMap = new HashMap();
            getTransMap.put("BATCH_ID", tblData.getValueAt(tblData.getSelectedRow(), 5).toString());
//        getTransMap.put("TRANS_DT", currDt);
            getTransMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
//        returnMap = new HashMap();
            List transList = (List) ClientUtil.executeQuery("getTransferDetailsForAuthorizeList", getTransMap);
            if (transList != null && transList.size() > 0) {
                transferMap.put("TRANSFER_TRANS_LIST", transList);
                displayTransDetail(transferMap);
            }
            List cashList = (List) ClientUtil.executeQuery("getCashDetailsForAuthorizeList", getTransMap);
            if (cashList != null && cashList.size() > 0) {
                cashMap.put("CASH_TRANS_LIST", cashList);
                displayTransDetail(cashMap);
            }
        } //        else if (node.equals("Cheque Book Issued")) {
        //             if(panChequeBookIssue.isShowing()==true){
        //            whereMap.put(CommonConstants.MAP_NAME, "getPrizedMoneyPaymentAuthorize");
        //        }
        else if (node.equals("MDS Prized Money Details")) {
            if (tblData.getSelectedRow() == -1) {
                ClientUtil.showAlertWindow("Please select a row in the table grid");
                return;
            }
            HashMap getTransMap = new HashMap();
            HashMap cashMap = new HashMap();
            HashMap transferMap = new HashMap();
            getTransMap.put("BATCH_ID", tblData.getValueAt(tblData.getSelectedRow(), 0).toString());
//        getTransMap.put("TRANS_DT", currDt);
            getTransMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
//        returnMap = new HashMap();
            List transList = (List) ClientUtil.executeQuery("getTransferDetailsForAuthorizeList", getTransMap);
            if (transList != null && transList.size() > 0) {
                transferMap.put("TRANSFER_TRANS_LIST", transList);
                displayTransDetail(transferMap);
            }
            List cashList = (List) ClientUtil.executeQuery("getCashDetailsForAuthorizeList", getTransMap);
            if (cashList != null && cashList.size() > 0) {
                cashMap.put("CASH_TRANS_LIST", cashList);
                displayTransDetail(cashMap);
            }
        } else if (node.equals("MDS Master Maintenance")) {
            if (tblData.getSelectedRow() == -1) {
                ClientUtil.showAlertWindow("Please select a row in the table grid");
                return;
            }
            HashMap getTransMap = new HashMap();
            HashMap cashMap = new HashMap();
            HashMap transferMap = new HashMap();
            getTransMap.put("BATCH_ID", tblData.getValueAt(tblData.getSelectedRow(), 1).toString());
//        getTransMap.put("TRANS_DT", currDt);
            getTransMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
//        returnMap = new HashMap();
            List transList = (List) ClientUtil.executeQuery("getTransferDetailsForAuthorizeList", getTransMap);
            if (transList != null && transList.size() > 0) {
                transferMap.put("TRANSFER_TRANS_LIST", transList);
                displayTransDetail(transferMap);
            }
            List cashList = (List) ClientUtil.executeQuery("getCashDetailsForAuthorizeList", getTransMap);
            if (cashList != null && cashList.size() > 0) {
                cashMap.put("CASH_TRANS_LIST", cashList);
                displayTransDetail(cashMap);
            }
        } else if (node.equals("Borrowing Disbursal")) {
            if (tblData.getSelectedRow() == -1) {
                ClientUtil.showAlertWindow("Please select a row in the table grid");
                return;
            }
            HashMap getTransMap = new HashMap();
            HashMap cashMap = new HashMap();
            HashMap transferMap = new HashMap();
            getTransMap.put("BATCH_ID", tblData.getValueAt(tblData.getSelectedRow(), 2).toString());
//        getTransMap.put("TRANS_DT", currDt);
            getTransMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
//        returnMap = new HashMap();
            List transList = (List) ClientUtil.executeQuery("getTransferDetailsForAuthorizeList", getTransMap);
            if (transList != null && transList.size() > 0) {
                transferMap.put("TRANSFER_TRANS_LIST", transList);
                displayTransDetail(transferMap);
            }
            List cashList = (List) ClientUtil.executeQuery("getCashDetailsForAuthorizeList", getTransMap);
            if (cashList != null && cashList.size() > 0) {
                cashMap.put("CASH_TRANS_LIST", cashList);
                displayTransDetail(cashMap);
            }
        } else if (node.equals("Borrowing Master")) {
            if (tblData.getSelectedRow() == -1) {
                ClientUtil.showAlertWindow("Please select a row in the table grid");
                return;
            }
            HashMap getTransMap = new HashMap();
            HashMap cashMap = new HashMap();
            HashMap transferMap = new HashMap();
            getTransMap.put("BATCH_ID", tblData.getValueAt(tblData.getSelectedRow(), 2).toString());
//        getTransMap.put("TRANS_DT", currDt);
            getTransMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
//        returnMap = new HashMap();
            List transList = (List) ClientUtil.executeQuery("getTransferDetailsForAuthorizeList", getTransMap);
            if (transList != null && transList.size() > 0) {
                transferMap.put("TRANSFER_TRANS_LIST", transList);
                displayTransDetail(transferMap);
            }
            List cashList = (List) ClientUtil.executeQuery("getCashDetailsForAuthorizeList", getTransMap);
            if (cashList != null && cashList.size() > 0) {
                cashMap.put("CASH_TRANS_LIST", cashList);
                displayTransDetail(cashMap);
            }
        }else if (node.equals("Borrowing Repayment")) {
            if (tblData.getSelectedRow() == -1) {
                ClientUtil.showAlertWindow("Please select a row in the table grid");
                return;
            }
            HashMap getTransMap = new HashMap();
            HashMap cashMap = new HashMap();
            HashMap transferMap = new HashMap();
            getTransMap.put("BATCH_ID", tblData.getValueAt(tblData.getSelectedRow(), 2).toString());
//        getTransMap.put("TRANS_DT", currDt);
            getTransMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
//        returnMap = new HashMap();
            List transList = (List) ClientUtil.executeQuery("getTransferDetailsForAuthorizeList", getTransMap);
            if (transList != null && transList.size() > 0) {
                transferMap.put("TRANSFER_TRANS_LIST", transList);
                displayTransDetail(transferMap);
            }
            List cashList = (List) ClientUtil.executeQuery("getCashDetailsForAuthorizeList", getTransMap);
            if (cashList != null && cashList.size() > 0) {
                cashMap.put("CASH_TRANS_LIST", cashList);
                displayTransDetail(cashMap);
            }
        } else if (node.equals("Other Bank Account Opening")) {
            if (tblData.getSelectedRow() == -1) {
                ClientUtil.showAlertWindow("Please select a row in the table grid");
                return;
            }
            HashMap getTransMap = new HashMap();
            HashMap cashMap = new HashMap();
            HashMap transferMap = new HashMap();
            if (tblData.getSelectedRow() != -1) {
                getTransMap.put("BATCH_ID", tblData.getValueAt(tblData.getSelectedRow(), 0).toString());
            }
//        getTransMap.put("TRANS_DT", currDt);
            getTransMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
//        returnMap = new HashMap();
            List transList = (List) ClientUtil.executeQuery("getTransferDetailsForAuthorizeList", getTransMap);
            if (transList != null && transList.size() > 0) {
                transferMap.put("TRANSFER_TRANS_LIST", transList);
                displayTransDetail(transferMap);
            }
            List cashList = (List) ClientUtil.executeQuery("getCashDetailsForAuthorizeList", getTransMap);
            if (cashList != null && cashList.size() > 0) {
                cashMap.put("CASH_TRANS_LIST", cashList);
                displayTransDetail(cashMap);
            }

        } else if (node.equals("Locker Renew/Surrender")) {
            if (tblData.getSelectedRow() == -1) {
                ClientUtil.showAlertWindow("Please select a row in the table grid");
                return;
            }
            HashMap getTransMap = new HashMap();
            HashMap cashMap = new HashMap();
            HashMap transferMap = new HashMap();
            getTransMap.put("BATCH_ID", tblData.getValueAt(tblData.getSelectedRow(), 1).toString());
//        getTransMap.put("TRANS_DT", currDt);
            getTransMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
//        returnMap = new HashMap();
            List transList = (List) ClientUtil.executeQuery("getTransferDetailsForAuthorizeList", getTransMap);
            if (transList != null && transList.size() > 0) {
                transferMap.put("TRANSFER_TRANS_LIST", transList);
                displayTransDetail(transferMap);
            }
            List cashList = (List) ClientUtil.executeQuery("getCashDetailsForAuthorizeList", getTransMap);
            if (cashList != null && cashList.size() > 0) {
                cashMap.put("CASH_TRANS_LIST", cashList);
                displayTransDetail(cashMap);
            }
        } else if (node.equals("Rent Transaction")) {
            if (tblData.getSelectedRow() == -1) {
                ClientUtil.showAlertWindow("Please select a row in the table grid");
                return;
            }
            HashMap getTransMap = new HashMap();
            HashMap cashMap = new HashMap();
            HashMap transferMap = new HashMap();
            getTransMap.put("BATCH_ID", tblData.getValueAt(tblData.getSelectedRow(), 0).toString());
//        getTransMap.put("TRANS_DT", currDt);
            getTransMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
//        returnMap = new HashMap();
            List transList = (List) ClientUtil.executeQuery("getTransferDetailsForAuthorizeList", getTransMap);
            if (transList != null && transList.size() > 0) {
                transferMap.put("TRANSFER_TRANS_LIST", transList);
                displayTransDetail(transferMap);
            }
            List cashList = (List) ClientUtil.executeQuery("getCashDetailsForAuthorizeList", getTransMap);
            if (cashList != null && cashList.size() > 0) {
                cashMap.put("CASH_TRANS_LIST", cashList);
                displayTransDetail(cashMap);
            }
        } else if (node.equals("Indend Transaction")) {
            if (tblData.getSelectedRow() == -1) {
                ClientUtil.showAlertWindow("Please select a row in the table grid");
                return;
            }
            HashMap getTransMap = new HashMap();
            HashMap cashMap = new HashMap();
            HashMap transferMap = new HashMap();
            getTransMap.put("BATCH_ID", tblData.getValueAt(tblData.getSelectedRow(), 0).toString());
//        getTransMap.put("TRANS_DT", currDt);
            getTransMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
//        returnMap = new HashMap();
            List transList = (List) ClientUtil.executeQuery("getTransferDetailsForAuthorizeList", getTransMap);
            if (transList != null && transList.size() > 0) {
                transferMap.put("TRANSFER_TRANS_LIST", transList);
                displayTransDetail(transferMap);
            }
            List cashList = (List) ClientUtil.executeQuery("getCashDetailsForAuthorizeList", getTransMap);
            if (cashList != null && cashList.size() > 0) {
                cashMap.put("CASH_TRANS_LIST", cashList);
                displayTransDetail(cashMap);
            }
//        } else if (node.equals("Multiple Cash Tranasction")) {
//            HashMap getTransMap = new HashMap();
//            HashMap cashMap = new HashMap();
//            HashMap transferMap = new HashMap();
////            getTransMap.put("BATCH_ID", batchId);
////        getTransMap.put("TRANS_DT", currDt);
//            getTransMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
//            //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
////        returnMap = new HashMap();
//            List transList = (List) ClientUtil.executeQuery("getTransferDetailsForAuthorizeList", getTransMap);
//            if (transList != null && transList.size() > 0) {
//                transferMap.put("TRANSFER_TRANS_LIST", transList);
//                displayTransDetail(transferMap);
//            }
//            List cashList = (List) ClientUtil.executeQuery("getCashDetailsForAuthorizeList", getTransMap);
//            if (cashList != null && cashList.size() > 0) {
//                cashMap.put("CASH_TRANS_LIST", cashList);
//                displayTransDetail(cashMap);
//            }
        } else if (node.equals("Loan Application Register")) {
            if (tblData.getSelectedRow() == -1) {
                ClientUtil.showAlertWindow("Please select a row in the table grid");
                return;
            }
            HashMap getTransMap = new HashMap();
            HashMap cashMap = new HashMap();
            HashMap transferMap = new HashMap();
            getTransMap.put("BATCH_ID", tblData.getValueAt(tblData.getSelectedRow(), 0).toString());
//        getTransMap.put("TRANS_DT", currDt);
            getTransMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
//        returnMap = new HashMap();
            List transList = (List) ClientUtil.executeQuery("getTransferDetailsForAuthorizeList", getTransMap);
            if (transList != null && transList.size() > 0) {
                transferMap.put("TRANSFER_TRANS_LIST", transList);
                displayTransDetail(transferMap);
            }
            List cashList = (List) ClientUtil.executeQuery("getCashDetailsForAuthorizeList", getTransMap);
            if (cashList != null && cashList.size() > 0) {
                cashMap.put("CASH_TRANS_LIST", cashList);
                displayTransDetail(cashMap);
            }
        } else if (node.equals("Share Dividend Payment")) {
            if (tblData.getSelectedRow() == -1) {
                ClientUtil.showAlertWindow("Please select a row in the table grid");
                return;
            }
            HashMap getTransMap = new HashMap();
            HashMap cashMap = new HashMap();
            HashMap transferMap = new HashMap();
            getTransMap.put("BATCH_ID", tblData.getValueAt(tblData.getSelectedRow(), 0).toString());
//        getTransMap.put("TRANS_DT", currDt);
            getTransMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
//        returnMap = new HashMap();
            List transList = (List) ClientUtil.executeQuery("getTransferDetailsForAuthorizeList", getTransMap);
            if (transList != null && transList.size() > 0) {
                transferMap.put("TRANSFER_TRANS_LIST", transList);
                displayTransDetail(transferMap);
            }
            List cashList = (List) ClientUtil.executeQuery("getCashDetailsForAuthorizeList", getTransMap);
            if (cashList != null && cashList.size() > 0) {
                cashMap.put("CASH_TRANS_LIST", cashList);
                displayTransDetail(cashMap);
            }
        } else if (node.equals("DRF Transaction")) {
            if (tblData.getSelectedRow() == -1) {
                ClientUtil.showAlertWindow("Please select a row in the table grid");
                return;
            }
            HashMap getTransMap = new HashMap();
            HashMap cashMap = new HashMap();
            HashMap transferMap = new HashMap();
            getTransMap.put("BATCH_ID", tblData.getValueAt(tblData.getSelectedRow(), 0).toString());
//        getTransMap.put("TRANS_DT", currDt);
            getTransMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
//        returnMap = new HashMap();
            List transList = (List) ClientUtil.executeQuery("getTransferDetailsForAuthorizeList", getTransMap);
            if (transList != null && transList.size() > 0) {
                transferMap.put("TRANSFER_TRANS_LIST", transList);
                displayTransDetail(transferMap);
            }
            List cashList = (List) ClientUtil.executeQuery("getCashDetailsForAuthorizeList", getTransMap);
            if (cashList != null && cashList.size() > 0) {
                cashMap.put("CASH_TRANS_LIST", cashList);
                displayTransDetail(cashMap);
            }
        } else if (node.equals("InvestmentMaster")) {
            if (tblData.getSelectedRow() == -1) {
                ClientUtil.showAlertWindow("Please select a row in the table grid");
                return;
            }
            HashMap getTransMap = new HashMap();
            HashMap cashMap = new HashMap();
            HashMap transferMap = new HashMap();
            getTransMap.put("BATCH_ID", tblData.getValueAt(tblData.getSelectedRow(), 0).toString());
//        getTransMap.put("TRANS_DT", currDt);
            getTransMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
//        returnMap = new HashMap();
            List transList = (List) ClientUtil.executeQuery("getTransferDetailsForAuthorizeList", getTransMap);
            if (transList != null && transList.size() > 0) {
                transferMap.put("TRANSFER_TRANS_LIST", transList);
                displayTransDetail(transferMap);
            }
            List cashList = (List) ClientUtil.executeQuery("getCashDetailsForAuthorizeList", getTransMap);
            if (cashList != null && cashList.size() > 0) {
                cashMap.put("CASH_TRANS_LIST", cashList);
                displayTransDetail(cashMap);
            }
        }

    }//GEN-LAST:event_btnTransIdActionPerformed

    private void btnRefreshListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshListActionPerformed
        // TODO add your handling code here:
        CommonUtil comm = new CommonUtil();
        final JDialog loading = comm.addProgressBar();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws InterruptedException /** Execute some operation */
            {
                getUnauthorizedTree();
                if (!node.equals("") && !node.equals(null) && !node.equals("Unauthorized Records") && !node.equals("Financial List") && !node.equals("Non Financial List")) {
                    displayDetails(node);
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

    }//GEN-LAST:event_btnRefreshListActionPerformed

    private void tblDataKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblDataKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10) {
            whenTableRowSelected();
        }
    }//GEN-LAST:event_tblDataKeyPressed

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
        new NewAuthorizeListUI().show();
    }

    public void update(Observable observed, Object arg) {
//            tdtFromDate.setDateValue(DateUtil.getStringDate(observable.getTdtFromDate()));
//            tdtToDate.setDateValue(DateUtil.getStringDate(observable.getTdtToDate()));
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnRefresh;
    private com.see.truetransact.uicomponent.CButton btnRefreshList;
    private com.see.truetransact.uicomponent.CButton btnTransId;
    private com.see.truetransact.uicomponent.CScrollPane cScrollPane1;
    private com.see.truetransact.uicomponent.CLabel lblCurCashBal;
    private com.see.truetransact.uicomponent.CPanel panSearch;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CPanel panTree;
    private com.see.truetransact.uicomponent.CButtonGroup rdgAndOr;
    private com.see.truetransact.uicomponent.CScrollPane srcTable;
    private com.see.truetransact.uicomponent.CTable tblData;
    private javax.swing.JTree treData;
    private com.see.truetransact.uicomponent.CTextField txtCurCashBal;
    // End of variables declaration//GEN-END:variables

    public boolean displayTransactionDetails(DefaultMutableTreeNode node) {
        _heading = null;
        data = null;
        boolean flag = false;
        HashMap whereMap = new HashMap();
        HashMap where = new HashMap();
        where.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        where.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        where.put(CommonConstants.HIERARCHY_ID, ProxyParameters.HIERARCHY_ID);
        where.put("TRANS_DT", currDt.clone());
        where.put("CASHIER_AUTH_ALLOWED", TrueTransactMain.CASHIER_AUTH_ALLOWED);
        currDt = (Date) currDt.clone();
        List lst = new ArrayList();
        if (CommonUtil.convertObjToStr(node).equals("Customer Master")) {
            whereMap.put(CommonConstants.MAP_NAME, "getUnAuthorizedListForCustomer");
            flag = whetherRecordAvailableorNot(whereMap, where);
        }if (CommonUtil.convertObjToStr(node).equals("Corporate Customer")) {
            whereMap.put(CommonConstants.MAP_NAME, "getUnAuthorizedListForCorpCustomer");
            flag = whetherRecordAvailableorNot(whereMap, where);
        }if (CommonUtil.convertObjToStr(node).equals("Locker Master")) {
            where.put(CommonConstants.AUTHORIZESTATUS, "Authorize"); 
            whereMap.put(CommonConstants.MAP_NAME, "getSelectLockerMasterEditAuthorizeTOList");
            flag = whetherRecordAvailableorNot(whereMap, where);
        } if (CommonUtil.convertObjToStr(node).equals("Inventory Details")) {
            whereMap.put(CommonConstants.MAP_NAME, "getSelectInventoryDetailsTOList");
            flag = whetherRecordAvailableorNot(whereMap, where);
        }else if(CommonUtil.convertObjToStr(node).equals("Share")){
           where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
           whereMap.put(CommonConstants.MAP_NAME, "viewAllShareAcctAuthorizeTOListWithoutShareDetails");
           flag = whetherRecordAvailableorNot(whereMap, where);
        } else if (CommonUtil.convertObjToStr(node).equals("SB/Current Account Opening")) {
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectAccountMasterCashierAuthorizeTOListWithTransaction");
                flag = whetherRecordAvailableorNot(whereMap, where);
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectAccountMasterAuthorizeTOListWithTransaction");
                flag = whetherRecordAvailableorNot(whereMap, where);
            }
        } 
        else if (CommonUtil.convertObjToStr(node).equals("SB/Current AccountOpening")) {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectAccountMasterAuthorizeTOListWithoutTransaction");
                flag = whetherRecordAvailableorNot(whereMap, where);
//            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
//                whereMap.put(CommonConstants.MAP_NAME, "getSelectAccountMasterCashierAuthorizeTOList");
//                flag = whetherRecordAvailableorNot(whereMap, where);
//            } else {
//                whereMap.put(CommonConstants.MAP_NAME, "getSelectAccountMasterAuthorizeTOListWithoutTransaction");
//                flag = whetherRecordAvailableorNot(whereMap, where);
//            }
        } 
        else if (CommonUtil.convertObjToStr(node).equals("Account Closing")) {
            where.put("AUTHORIZESTATUS", "AUTHORIZED");
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectAccountCloseCashierAuthorizeTOList");
                flag = whetherRecordAvailableorNot(whereMap, where);
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectAccountCloseAuthorizeTOList");
                flag = whetherRecordAvailableorNot(whereMap, where);
            }
        } else if (CommonUtil.convertObjToStr(node).equals("Deposit Accounts")) {
            where.put("OPENING_MODE", "Normal");
            where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "viewAllDepAccAuthorizeCashierTOList");
                flag = whetherRecordAvailableorNot(whereMap, where);
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "viewAllDepAccAuthorizeTOList");
                flag = whetherRecordAvailableorNot(whereMap, where);
            }
        } else if (CommonUtil.convertObjToStr(node).equals("Deposit Account Renewal")) {
            where.put("OPENING_MODE", "Renewal");
            where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "viewAllDepAccAuthorizeCashierTOList");
                flag = whetherRecordAvailableorNot(whereMap, where);
            } else {
//                whereMap.put(CommonConstants.MAP_NAME, "viewAllDepAccAuthorizeTOList");
                  whereMap.put(CommonConstants.MAP_NAME, "viewAllDepAccRenewalAuthorizeTOListForAuthScreen");
                flag = whetherRecordAvailableorNot(whereMap, where);
            }
        } 
        else if (CommonUtil.convertObjToStr(node).equals("Deposit AccountRenewal")) { //Added by Rishad for without Transaction
            where.put("OPENING_MODE", "Renewal");
            where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
            whereMap.put(CommonConstants.MAP_NAME, "viewAllDepAccAuthorizeTOListWithOutTransaction");
            flag = whetherRecordAvailableorNot(whereMap, where);
        } 
        else if (CommonUtil.convertObjToStr(node).equals("Deposit Account Closing")) {
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getDepositAccountCloseCashierAuthorizeTOList");
                flag = whetherRecordAvailableorNot(whereMap, where);
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getDepositAccountCloseAuthorizeTOList");
                flag = whetherRecordAvailableorNot(whereMap, where);
            }
        } else if (CommonUtil.convertObjToStr(node).equals("Deposit Multiple Closing")) {
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getMultipleDepositAccountCloseCashierAuthorizeTOList");
                flag = whetherRecordAvailableorNot(whereMap, where);
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getMultipleDepositAccountCloseAuthorizeTOList");
                flag = whetherRecordAvailableorNot(whereMap, where);
            }
        } else if (CommonUtil.convertObjToStr(node).equals("Multiple Deposit Account Opening")) {
            where.put("OPENING_MODE", "Normal");
            where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "viewAllMultipleDepAccAuthorizeCashierTOList");
                flag = whetherRecordAvailableorNot(whereMap, where);
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "viewAllMultipleDepAccAuthorizeTOList");
                flag = whetherRecordAvailableorNot(whereMap, where);
            }
        } else if (CommonUtil.convertObjToStr(node).equals("Gold Loan Account Opening")) {
            where.put("AUTHORIZE_REMARK", "!= 'GOLD_LOAN'");
            where.put("GOLD_LOAN", "GOLD_LOAN");
            where.put("AUTHORIZESTATUS", "AUTHORIZED");
            where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            where.put("STATUS_BY", ProxyParameters.USER_ID);
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                where.put("AUTH_TRANS_TYPE", "DEBIT");
                whereMap.put(CommonConstants.MAP_NAME, "getSelectTermLoanCashierAuthorizeTOList");
                flag = whetherRecordAvailableorNot(whereMap, where);
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectTermLoanAuthorizeTOList");
                flag = whetherRecordAvailableorNot(whereMap, where);
            }
        } else if (CommonUtil.convertObjToStr(node).equals("Loans/Advances Account Opening")) {
            where.put("AUTHORIZE_REMARK", "= 'GOLD_LOAN'");
            where.put("AUTHORIZESTATUS", "AUTHORIZED");
            where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            where.put("STATUS_BY", ProxyParameters.USER_ID);
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                where.put("AUTH_TRANS_TYPE", "DEBIT");
                whereMap.put(CommonConstants.MAP_NAME, "getSelectTermLoanCashierAuthorizeTOList");
                flag = whetherRecordAvailableorNot(whereMap, where);
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectTermLoanAuthorizeTOList");
                flag = whetherRecordAvailableorNot(whereMap, where);
            }
        } else if (CommonUtil.convertObjToStr(node).equals("Deposit Loan Account Opening")) {
            where.put("AUTHORIZESTATUS", "AUTHORIZED");
            where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            where.put("STATUS_BY", ProxyParameters.USER_ID);
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                where.put("AUTH_TRANS_TYPE", "DEBIT");
                whereMap.put(CommonConstants.MAP_NAME, "getSelectTermLoanCashierAuthorizeTOListForLTD");
                flag = whetherRecordAvailableorNot(whereMap, where);
            } else {
                where.put("AUTH_TRANS_TYPE", "DEBIT");
                whereMap.put(CommonConstants.MAP_NAME, "getSelectTermLoanAuthorizeTOListForLTD");
                flag = whetherRecordAvailableorNot(whereMap, where);
            }
        } else if (CommonUtil.convertObjToStr(node).equals("Loan Closing")) {
            where.put("AUTHORIZESTATUS", "AUTHORIZED");
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectLoanAccountCloseCashierAuthorizeTOList");
                flag = whetherRecordAvailableorNot(whereMap, where);
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectLoanAccountCloseAuthorizeTOList");
                flag = whetherRecordAvailableorNot(whereMap, where);
            }
        } else if (CommonUtil.convertObjToStr(node).equals("Cash Transactions")) {
            where.put("AUTHORIZESTATUS", "AUTHORIZED");
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectCashTransactionCashierAuthorizeTOList");
                flag = whetherRecordAvailableorNot(whereMap, where);
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectCashTransactionAuthorizeTOList");
                flag = whetherRecordAvailableorNot(whereMap, where);
            }
        } else if (CommonUtil.convertObjToStr(node).equals("Transfer")) {
            where.put("AUTHORIZESTATUS", "AUTHORIZED");
            whereMap.put(CommonConstants.MAP_NAME, "getUnAuthorizeMasterTransferTO");
            flag = whetherRecordAvailableorNot(whereMap, where);
        } else if (CommonUtil.convertObjToStr(node).equals("Share Account")) {
            where.put("AUTHORIZESTATUS", "AUTHORIZED");
            where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
            where.put("TRANS_DT", currDt);
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "viewAllShareAcctCashierAuthorizeTOList");
                flag = whetherRecordAvailableorNot(whereMap, where);
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "viewAllShareAcctAuthorizeTOList11");
                flag = whetherRecordAvailableorNot(whereMap, where);
            }
        } else if (CommonUtil.convertObjToStr(node).equals("MDS Receipts")) {
            where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectNonAuthRecordForCashierReceipt");
                flag = whetherRecordAvailableorNot(whereMap, where);
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectNonAuthRecordForReceipt");
                flag = whetherRecordAvailableorNot(whereMap, where);
            }
        }
        else if (CommonUtil.convertObjToStr(node).equals("MDS Payments")) {
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getPrizedMoneyPaymentCashierAuthorize");
                flag = whetherRecordAvailableorNot(whereMap, where);
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getPrizedMoneyPaymentAuthorize");
                flag = whetherRecordAvailableorNot(whereMap, where);
            }
        } 
         else if (CommonUtil.convertObjToStr(node).equals("MDS Member")) {
             if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getMemberReceiptCashierAuthorize");
                  flag = whetherRecordAvailableorNot(whereMap, where);
             } else {
               whereMap.put(CommonConstants.MAP_NAME, "getMemberReceiptAuthorize");
                flag = whetherRecordAvailableorNot(whereMap, where);
            }
             
        } 
        else if (CommonUtil.convertObjToStr(node).equals("Lodgement")) {
            whereMap.put(CommonConstants.MAP_NAME, "getLodgementMasterAuthorizeList");
            flag = whetherRecordAvailableorNot(whereMap, where);
        } else if (CommonUtil.convertObjToStr(node).equals("Investment Trans")) {
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getInvestmentTransCashierAuthorizeList");
                flag = whetherRecordAvailableorNot(whereMap, where);
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getInvestmentTransAuthorizeList");
                flag = whetherRecordAvailableorNot(whereMap, where);
            }
        } else if (CommonUtil.convertObjToStr(node).equals("MDS Prized Money Details")) {
            whereMap.put(CommonConstants.MAP_NAME, "getPrizedMoneyDetailsEntryAuthorize");
            flag = whetherRecordAvailableorNot(whereMap, where);
        } else if (CommonUtil.convertObjToStr(node).equals("MDS Master Maintenance")) {
            whereMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            whereMap.put(CommonConstants.MAP_NAME, "getMDSMasterMaintenanceAuthorize");
            flag = whetherRecordAvailableorNot(whereMap, where);
        } else if (CommonUtil.convertObjToStr(node).equals("Borrowing Disbursal")) {
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getBorrowingDisbursalCashierAuthorizeList");
                flag = whetherRecordAvailableorNot(whereMap, where);
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getBorrowingDisbursalAuthorizeList");
                flag = whetherRecordAvailableorNot(whereMap, where);
            }
        } else if (CommonUtil.convertObjToStr(node).equals("Borrowing Master")){
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getBorrowingCashierAuthorizeList");
                flag = whetherRecordAvailableorNot(whereMap, where);
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getBorrowingAuthorizeList");
                flag = whetherRecordAvailableorNot(whereMap, where);
            }
        } else if (CommonUtil.convertObjToStr(node).equals("Borrowing Repayment")) {
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getBorrowingRepIntClsCashierAuthorizeList");
                flag = whetherRecordAvailableorNot(whereMap, where);
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getBorrowingRepIntClsAuthorizeList");
                flag = whetherRecordAvailableorNot(whereMap, where);
            }
        } else if (CommonUtil.convertObjToStr(node).equals("Other Bank Account Opening")) {
            //whereMap.put(CommonConstants.MAP_NAME, "getSelectABAuthDetails");
            //flag = whetherRecordAvailableorNot(whereMap, where);
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectABAuthDetails");
                flag = whetherRecordAvailableorNot(whereMap, where);
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectABAuthDetailsWithOutCashier");
                flag = whetherRecordAvailableorNot(whereMap, where);
            }
        } else if (CommonUtil.convertObjToStr(node).equals("Locker Renew/Surrender")) {
            where.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
            //whereMap.put(CommonConstants.MAP_NAME, "getSelectLockerAuthorize");
            //Added by nithya on 24-02-2021 for KD-2711
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
               whereMap.put(CommonConstants.MAP_NAME, "getSelectLockerCashierAuthorize"); 
            }else{
            whereMap.put(CommonConstants.MAP_NAME, "getSelectLockerAuthorize");
            }  
            flag = whetherRecordAvailableorNot(whereMap, where);
        } 
        else if (CommonUtil.convertObjToStr(node).equals("Locker Issue")) {
            where.put(CommonConstants.AUTHORIZESTATUS, "Authorize");
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
               whereMap.put(CommonConstants.MAP_NAME, "getSelectLockerMasterCashierAuthorizeTOList"); 
            }else{
            whereMap.put(CommonConstants.MAP_NAME, "getSelectLockerMasterAuthorizeTOList");
            }
            flag = whetherRecordAvailableorNot(whereMap, where);
        }
        else if (CommonUtil.convertObjToStr(node).equals("Rent Transaction")) {            
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getRentTransCashierAuthorizeList");                
                flag = whetherRecordAvailableorNot(whereMap, where);
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getRentTransAuthorizeList");
                flag = whetherRecordAvailableorNot(whereMap, where);
            }
        } else if (CommonUtil.convertObjToStr(node).equals("Indend Transaction")) {
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getIndendCashierAuthorizeList");
                flag = whetherRecordAvailableorNot(whereMap, where);
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getIndendAuthorizeList");
                flag = whetherRecordAvailableorNot(whereMap, where);
            }
        } else if (CommonUtil.convertObjToStr(node).equals("Multiple Cash Tranasction")) {
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getCashierAuthorizationsListForMutipleCash");
                flag = whetherRecordAvailableorNot(whereMap, where);
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getAuthorizationsListForMutipleCash");
                flag = whetherRecordAvailableorNot(whereMap, where);
            }
        } else if (CommonUtil.convertObjToStr(node).equals("Purchase Entry")) {
            whereMap.put(CommonConstants.MAP_NAME, "getPurchaseEntryAuthorizeList");
            flag = whetherRecordAvailableorNot(whereMap, where);
        } else if (CommonUtil.convertObjToStr(node).equals("Loan Application Register")) {
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getLoanApplicationAuthorizeListForCashierAuth");
            } else {
                //whereMap.put(CommonConstants.MAP_NAME, "getLoanApplicationAuthorizeList");
                whereMap.put(CommonConstants.MAP_NAME,"getLoanApplicationAuthorizeListWithOutCahsier");// Added by nithya on 19-05-2018 for 0010443: LOAN APPLICATION LAND INSPECTION RECEIPT AUTHORIZATION ISSUE  
            }
            flag = whetherRecordAvailableorNot(whereMap, where);
        } else if (CommonUtil.convertObjToStr(node).equals("MDS Application New")) {
            where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);            
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectNonAuthRecordForEntered");
                flag = whetherRecordAvailableorNot(whereMap, where);
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectNonAuthRecordForEnteredWithOutCashier");
                flag = whetherRecordAvailableorNot(whereMap, where);
            }
        } else if (CommonUtil.convertObjToStr(node).equals("Share Dividend Payment")) {
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getDividendPaymentTransferAuthMode");
                flag = whetherRecordAvailableorNot(whereMap, where);
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getDividendPaymentAuthWithOutCashier");
                flag = whetherRecordAvailableorNot(whereMap, where);
            }
        } else if (CommonUtil.convertObjToStr(node).equals("DRF Transaction")) {            
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getDrfTransferAuthMode");
                flag = whetherRecordAvailableorNot(whereMap, where);
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getDrfTransferAuthModeWithOutCashier");
                flag = whetherRecordAvailableorNot(whereMap, where);
            }
        } else if (CommonUtil.convertObjToStr(node).equals("Account Head")) {
          //  whereMap.put(CommonConstants.MAP_NAME, "getSelectAccCreationAuthorizeTOList");
            whereMap.put(CommonConstants.MAP_NAME, "getSelectAccMaintainAuthorizeTOList");
            flag = whetherRecordAvailableorNot(whereMap, where);
        } else if (CommonUtil.convertObjToStr(node).equals("Gahan Customer")) {
            whereMap.put(CommonConstants.MAP_NAME, "getSelectGahanaAuthList");
            flag = whetherRecordAvailableorNot(whereMap, where);
        } else if (CommonUtil.convertObjToStr(node).equals("Standing Instruction")) {
            whereMap.put(CommonConstants.MAP_NAME, "getStandingInstructionAuthorizeList");
            flag = whetherRecordAvailableorNot(whereMap, where);
        } else if (CommonUtil.convertObjToStr(node).equals("InvestmentMaster")) {
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectInvestmentCashierAuthDetails");
                flag = whetherRecordAvailableorNot(whereMap, where);
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectInvestmentAuthDetails");
                flag = whetherRecordAvailableorNot(whereMap, where);
            }
        } else if (CommonUtil.convertObjToStr(node).equals("Cheque Book Issue")) {
            whereMap.put(CommonConstants.MAP_NAME, "getSelectChequeBookIssueTOList");
            flag = whetherRecordAvailableorNot(whereMap, where);
        } else if (CommonUtil.convertObjToStr(node).equals("Stop Payment Issue")) {
            whereMap.put(CommonConstants.MAP_NAME, "getSelectStopPaymentIssueTOList");
            flag = whetherRecordAvailableorNot(whereMap, where);
        } else if (CommonUtil.convertObjToStr(node).equals("Loose Leaf Issue")) {
            whereMap.put(CommonConstants.MAP_NAME, "getSelectLooseLeafIssueTOList");
            flag = whetherRecordAvailableorNot(whereMap, where);
        } else if (CommonUtil.convertObjToStr(node).equals("ECS Stop Payment")) {
            whereMap.put(CommonConstants.MAP_NAME, "getSelectEcsStopPaymentIssueTOList");
            flag = whetherRecordAvailableorNot(whereMap, where);
        } else if (CommonUtil.convertObjToStr(node).equals("Daily Loan Collection - Loan Adjstment")) {
            whereMap.put(CommonConstants.MAP_NAME, "getAdjustmentDailyLoanTransAuthorize");
            flag = whetherRecordAvailableorNot(whereMap, where);
        } else if (CommonUtil.convertObjToStr(node).equals("Daily Loan Collection - Suspense collection")) {
            whereMap.put(CommonConstants.MAP_NAME, "getDailyLoanTransAuthorize");
            flag = whetherRecordAvailableorNot(whereMap, where);
        }else if (CommonUtil.convertObjToStr(node).equals("Multiple Deposit Creation")) {
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "viewAllMultipleDepAccAuthorizeCashierTOList");
                flag = whetherRecordAvailableorNot(whereMap, where);
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "viewAllMultipleDepAccAuthorizeTOList");
                flag = whetherRecordAvailableorNot(whereMap, where);
            }            
        }else if (CommonUtil.convertObjToStr(node).equals("Loan Application With out Transaction")) {
            whereMap.put(CommonConstants.MAP_NAME, "getLoanApplicationAuthorizeListWithOutTransaction");
            flag = whetherRecordAvailableorNot(whereMap, where);
        }else if (CommonUtil.convertObjToStr(node).equals("Group loan Payments")) {
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getGroupLoanPaymentForAuthCashUI");
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getGroupLoanPaymentForAuthUI");
            }
            flag = whetherRecordAvailableorNot(whereMap, where);
        }else if (CommonUtil.convertObjToStr(node).equals("Group loan Receipts")) {
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getGroupLoanReceiptForAuthCashUI");
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getGroupLoanReceiptForAuthUI");                
            }
            flag = whetherRecordAvailableorNot(whereMap, where);
        }else if (CommonUtil.convertObjToStr(node).equals("Group loan Customer")) {             
            whereMap.put(CommonConstants.MAP_NAME, "getGroupAuthorizeCustomer");
            flag = whetherRecordAvailableorNot(whereMap, where);
        }else if (CommonUtil.convertObjToStr(node).equals("Other Bank Accounts Master")) {             
            whereMap.put(CommonConstants.MAP_NAME, "getSelectABAuthDetailsWithOutTransaction");
            flag = whetherRecordAvailableorNot(whereMap, where);
        }else if (CommonUtil.convertObjToStr(node).equals("Pension Scheme")) {
            where.put("USER_ID", TrueTransactMain.USER_ID);
            where.put("TRANS_DT", currDt);
            where.put(CommonConstants.AUTHORIZESTATUS,"AUTHORIZED");
            where.put(CommonConstants.INITIATED_BRANCH, ProxyParameters.BRANCH_ID);
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getPensionSchemeAuthorizeCashUI");
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getPensionSchemeAuthorizeUI");                
            }
            flag = whetherRecordAvailableorNot(whereMap, where);
        }else if (CommonUtil.convertObjToStr(node).equals("Director Board Meeting")) {
            where.put("USER_ID", TrueTransactMain.USER_ID);
            where.put("TRANS_DT", currDt);
            where.put(CommonConstants.AUTHORIZESTATUS,"AUTHORIZED");
            where.put(CommonConstants.INITIATED_BRANCH, ProxyParameters.BRANCH_ID);
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getDirectorBoardAuthorizeList");
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getDirectorBoardAuthorizeList");                
            }
            flag = whetherRecordAvailableorNot(whereMap, where);
        }else if (CommonUtil.convertObjToStr(node).equals("GDSReceiptEntry")) {  // Started GDS Module changes  
                where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
                whereMap.put(CommonConstants.MAP_NAME, "getGDSSelectNonAuthRecordForReceipt");
                flag = whetherRecordAvailableorNot(whereMap, where);          
        }else if (CommonUtil.convertObjToStr(node).equals("GDS Prized Money Payment")) {    
                where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
                whereMap.put(CommonConstants.MAP_NAME, "getGDSPrizedMoneyPaymentAuthorize"); 
                flag = whetherRecordAvailableorNot(whereMap, where);   
        }else if (CommonUtil.convertObjToStr(node).equals("GDSApplication")) {
                where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);           
                whereMap.put(CommonConstants.MAP_NAME, "getGDSSelectNonAuthRecordForEnteredWithOutCashier");
                flag = whetherRecordAvailableorNot(whereMap, where); 
        }else if (CommonUtil.convertObjToStr(node).equals("GDS Bank Advance")) {// Added by nithya on 20-12-2019 FOR kd-887
                where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);           
                whereMap.put(CommonConstants.MAP_NAME, "getGDSBankAdvanceAuthorize");
                flag = whetherRecordAvailableorNot(whereMap, where); 
        }else if (CommonUtil.convertObjToStr(node).equals("MDS Receipt Entry")) {
            where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectNonAuthRecordForCashierReceipt");
                flag = whetherRecordAvailableorNot(whereMap, where);
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectNonAuthRecordForReceipt");
                flag = whetherRecordAvailableorNot(whereMap, where);
            }
        }else if (CommonUtil.convertObjToStr(node).equals("MDS Prized Money Payment")) {
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getPrizedMoneyPaymentCashierAuthorize");
                flag = whetherRecordAvailableorNot(whereMap, where);
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getPrizedMoneyPaymentAuthorize");
                flag = whetherRecordAvailableorNot(whereMap, where);
            }
        }else if (CommonUtil.convertObjToStr(node).equals("MDS Receipt Entry After Closure")) {
            where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectNonAuthRecordForCashierReceipt");
                flag = whetherRecordAvailableorNot(whereMap, where);
            } else {
                whereMap.put(CommonConstants.MAP_NAME, "getSelectNonAuthRecordForReceipt");
                flag = whetherRecordAvailableorNot(whereMap, where);
            }
        }else if (CommonUtil.convertObjToStr(node).equals("MDS Commencement/Closure")) {
            where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
            whereMap.put(CommonConstants.MAP_NAME, "getSelectCommencementList");
            flag = whetherRecordAvailableorNot(whereMap, where);           
        }else if (CommonUtil.convertObjToStr(node).equals("MDS Bank Advance")) { // Added by nithya on 18-08-2020 for KD-2162
            where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
            whereMap.put(CommonConstants.MAP_NAME, "getBankAdvanceAuthorize");
            flag = whetherRecordAvailableorNot(whereMap, where);           
        }else if (CommonUtil.convertObjToStr(node).equals("LOAN_NOTICE")) {
            where.put("USER_ID", TrueTransactMain.USER_ID);
            where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            where.put("CHARGE_DATE", currDt.clone());
            where.put("AUTHORIZE_MODE","AUTHORIZE_MODE");
            whereMap.put(CommonConstants.MAP_NAME, "getAuthorizeLoanNoticeList");
            flag = whetherRecordAvailableorNot(whereMap, where);
        }
        //populateTable();
//        HashMap hash = (HashMap) obj;
        return flag;
        //System.out.println("#$#$ whereMap : " + whereMap);
    }

    private boolean whetherRecordAvailableorNot(HashMap whereMap, HashMap where) {
        boolean flag = false;
        whereMap.put(CommonConstants.MAP_WHERE, where);
        if (whereMap.containsKey(CommonConstants.MAP_NAME)) {
            whereMap = ClientUtil.executeTableQuery(whereMap);
            _heading = (ArrayList) whereMap.get(CommonConstants.TABLEHEAD);
            data = (ArrayList) whereMap.get(CommonConstants.TABLEDATA);
            if (_heading != null && data != null) {
                //setTreeRenderer();
                //parent1.add();
                flag = true;
            }
        }
        return flag;
    }
    /*
     * private void setTreeRenderer() { DefaultTreeCellRenderer renderer = new
     * DefaultTreeCellRenderer() { public java.awt.Component
     * getTreeCellRendererComponent( javax.swing.JTree tree, Object value,
     * boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
     * super.getTreeCellRendererComponent( tree, value, sel, expanded, leaf,
     * row, hasFocus); isReport(value); return this; } protected boolean
     * isReport(Object value) { DefaultMutableTreeNode node =
     * (DefaultMutableTreeNode) value; setBackground(Color.RED); return true; }
     * };
     * javax.swing.ToolTipManager.sharedInstance().registerComponent(treData);
     * treData.setCellRenderer(renderer);        
    }
     */
}
