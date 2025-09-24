/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * ChargesUI.java
 *
 * Created on August 6, 2003, 10:51 AM
 */

package com.see.truetransact.ui.operativeaccount;

import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.ui.batchprocess.*;
import com.see.truetransact.uicomponent.COptionPane;

import com.see.truetransact.transferobject.product.loan.LoanProductChargesTO;
import com.see.truetransact.transferobject.product.operativeacct.OperativeAcctChargesParamTO;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 *
 * @author  rahul
 */
public class ChargesUI extends CInternalFrame implements java.util.Observer {
    
    ChargesRB resourceBundle = new ChargesRB();
    
    int CREDIT = 0, DEBIT = 0;
    
    private TaskStatus tskStatus;
    private TaskHeader tskHeader;
    private ThreadPool threadPool;
    private ProcessOB observable;
    private boolean taskRunning;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    Date CurDate = null;
    final int TO=0, FROM=1;
    int viewType=-1;
       private static volatile List loanFacilityDts=null;
    private final String PROD_ID = "PROD_ID";
    private final String AMOUNT = "AMOUNT";
    private final String START_DT = "START";
    String behavesLike="";
    boolean yes=false;
    //    private HashMap taskMap;
    
    /** Creates new form ChargesUI */
    public ChargesUI() {
        initComponents();
        initSetup();
        CurDate = ClientUtil.getCurrentDate();
    }
    
    private void initSetup(){
        setFieldNames();
        internationalize();
        setObservable();
        initComponentData();    // Fill all the combo boxes...
        
        ClientUtil.clearAll(panChargeType);
        ClientUtil.enableDisable(panChargeType, false);
        
        ClientUtil.clearAll(panInterestType);
        ClientUtil.enableDisable(panInterestType, false);
        
        setupInit();
        setInvisible();
        txtFromAccount.setAllowAll(true);
        txtFromAccount.setMaxLength(16);
        txtToAccount.setAllowAll(true);
        txtToAccount.setMaxLength(16);
        //Added By Suresh
//        btnChargesPrint.setEnabled(false);
        chkTrial.setVisible(false);
        btnView.setVisible(false); //Added by kannan
        btnClearTrail.setVisible(false);
    }
    
    // Creates The Instance of ChargesOB
    private void setObservable() {
        observable = new ProcessOB();
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
        cboProdType.setName("cboProdType");
        chkExcessTransactionCharges.setName("chkExcessTransactionCharges");
        chkFolioCharges.setName("chkFolioCharges");
        chkInoperativeCharges.setName("chkInoperativeCharges");
        chkNonMaintenanceOFMinBalCharges.setName("chkNonMaintenanceOFMinBalCharges");
        lblFromAccount.setName("lblFromAccount");
        lblFromDate.setName("lblFromDate");
        lblMsg.setName("lblMsg");
        lblProductId.setName("lblProductId");
        lblSpace1.setName("lblSpace1");
        lblSpace4.setName("lblSpace4");
        lblStatus.setName("lblStatus");
        lblToAccount.setName("lblToAccount");
        lblToDate.setName("lblToDate");
        panChargeType.setName("panChargeType");
        panChargesApplication.setName("panChargesApplication");
        panFolioCharges.setName("panFolioCharges");
        panProductId.setName("panProductId");
        panStatementCharges.setName("panStatementCharges");
        panStatus.setName("panStatus");
        panTable.setName("panTable");
        panToAccount.setName("panToAccount");
        scrOutputMsg.setName("scrOutputMsg");
        tblLog.setName("tblLog");
        tdtFromDate.setName("tdtFromDate");
        tdtToDate.setName("tdtToDate");
        txtFromAccount.setName("txtFromAccount");
        txtToAccount.setName("txtToAccount");
        rdoInterestYes.setName("rdoInterestYes");
        rdoInterestNo.setName("rdoInterestNo");
        
    }
    
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        btnFromAccount.setText(resourceBundle.getString("btnFromAccount"));
        chkExcessTransactionCharges.setText(resourceBundle.getString("chkExcessTransactionCharges"));
        lblFromDate.setText(resourceBundle.getString("lblFromDate"));
        lblProdType.setText(resourceBundle.getString("lblProdType"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblToAccount.setText(resourceBundle.getString("lblToAccount"));
        chkFolioCharges.setText(resourceBundle.getString("chkFolioCharges"));
        chkInoperativeCharges.setText(resourceBundle.getString("chkInoperativeCharges"));
        lblFromAccount.setText(resourceBundle.getString("lblFromAccount"));
        btnToAccount.setText(resourceBundle.getString("btnToAccount"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        btnProcess.setText(resourceBundle.getString("btnProcess"));
        lblToDate.setText(resourceBundle.getString("lblToDate"));
        lblProductId.setText(resourceBundle.getString("lblProductId"));
        
        rdoInterestYes.setText(resourceBundle.getString("rdoInterestYes"));
        rdoInterestNo.setText(resourceBundle.getString("rdoInterestNo"));
        ((javax.swing.border.TitledBorder)panChargeType.getBorder()).setTitle(resourceBundle.getString("panChargeType"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        chkNonMaintenanceOFMinBalCharges.setText(resourceBundle.getString("chkNonMaintenanceOFMinBalCharges"));
    }
    
    
    private void setupInit() {
        HashMap taskMap = new HashMap();
        taskMap.put(chkExcessTransactionCharges.getText(), "ExcessTransChrgesTask");
        taskMap.put(chkFolioCharges.getText(), "FolioChargesTask");
        taskMap.put(chkInoperativeCharges.getText(), "InOperativeChargesTask");
        taskMap.put(chkNonMaintenanceOFMinBalCharges.getText(), "MinBalanceChargesTask");
        taskMap.put(chkCredit.getText(), "InterestTask");
        taskMap.put(chkDebit.getText(), "InterestTask");
        
        observable.setTaskMap(taskMap);
        tblLog.setModel(observable.getDayBeginTableModel());
        tskStatus = new TaskStatus();
        tskHeader = new TaskHeader();
        taskRunning = false;
        taskMap = null;
    }
    // To fill the Data into the Combo Boxes...
    // it invokes the Combo Box model defined in OB class...
    private void initComponentData() {
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
        try{
            // To obtain the Product id from the Table "OP_AC_PRODUCT"...
            // here "getAccProduct" is the mapped Statement name, defined in InwardClearingMap...
            lookup_keys.add("PRODUCTTYPE");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
            key.add("SH");
            value.add("Shares");
            cboProdType.setModel(new ComboBoxModel(key,value));
            
            //            lookUpHash.put(CommonConstants.MAP_NAME,"getAccProducts");
            //            lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
            //            keyValue = ClientUtil.populateLookupData(lookUpHash);
            //            getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
            //            cboProductId.setModel(new ComboBoxModel(key,value));
        }catch(Exception e){
            //System.out.println("Exception in initComponentData()");
            e.printStackTrace();
        }
    }
    
    // Get the value from the Hash Map depending on the Value of Key...
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoTaskType = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoTaskType1 = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrHead = new javax.swing.JToolBar();
        btnProcess = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace28 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
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
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        chkTrial = new com.see.truetransact.uicomponent.CCheckBox();
        btnView = new com.see.truetransact.uicomponent.CButton();
        panChargeType = new com.see.truetransact.uicomponent.CPanel();
        panFolioCharges = new com.see.truetransact.uicomponent.CPanel();
        chkFolioCharges = new com.see.truetransact.uicomponent.CCheckBox();
        chkInoperativeCharges = new com.see.truetransact.uicomponent.CCheckBox();
        panStatementCharges = new com.see.truetransact.uicomponent.CPanel();
        chkExcessTransactionCharges = new com.see.truetransact.uicomponent.CCheckBox();
        chkNonMaintenanceOFMinBalCharges = new com.see.truetransact.uicomponent.CCheckBox();
        panToAccount = new com.see.truetransact.uicomponent.CPanel();
        lblToAccount = new com.see.truetransact.uicomponent.CLabel();
        txtToAccount = new com.see.truetransact.uicomponent.CTextField();
        lblToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtToDate = new com.see.truetransact.uicomponent.CDateField();
        btnToAccount = new com.see.truetransact.uicomponent.CButton();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        scrOutputMsg = new com.see.truetransact.uicomponent.CScrollPane();
        tblLog = new com.see.truetransact.uicomponent.CTable();
        panChoice = new com.see.truetransact.uicomponent.CPanel();
        rdoCharges = new com.see.truetransact.uicomponent.CRadioButton();
        rdoInterest = new com.see.truetransact.uicomponent.CRadioButton();
        panInterestType = new com.see.truetransact.uicomponent.CPanel();
        panCredit = new com.see.truetransact.uicomponent.CPanel();
        chkCredit = new com.see.truetransact.uicomponent.CCheckBox();
        panDebit = new com.see.truetransact.uicomponent.CPanel();
        chkDebit = new com.see.truetransact.uicomponent.CCheckBox();
        panInterestTypeDebit = new com.see.truetransact.uicomponent.CPanel();
        rdoInterestYes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoInterestNo = new com.see.truetransact.uicomponent.CRadioButton();
        btnClearTrail = new com.see.truetransact.uicomponent.CButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Charges Application");
        setPreferredSize(new java.awt.Dimension(515, 620));

        btnProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnProcess.setToolTipText("Start Process");
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });
        tbrHead.add(btnProcess);

        lblSpace4.setText("     ");
        tbrHead.add(lblSpace4);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.setFocusable(false);
        btnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrHead.add(btnCancel);

        lblSpace5.setText("     ");
        tbrHead.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrHead.add(btnPrint);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace28);

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

        panChargesApplication.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panChargesApplication.setPreferredSize(new java.awt.Dimension(480, 470));
        panChargesApplication.setLayout(new java.awt.GridBagLayout());

        panProductId.setName("panProductId"); // NOI18N
        panProductId.setLayout(new java.awt.GridBagLayout());

        lblProductId.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductId.add(lblProductId, gridBagConstraints);

        cboProductId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductId.setPopupWidth(125);
        cboProductId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
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

        lblFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
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
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductId.add(tdtFromDate, gridBagConstraints);

        btnFromAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
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

        lblProdType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductId.add(lblProdType, gridBagConstraints);

        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductId.add(cboProdType, gridBagConstraints);

        chkTrial.setText("Trial");
        chkTrial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkTrialActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        panProductId.add(chkTrial, gridBagConstraints);

        btnView.setText("View");
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductId.add(btnView, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChargesApplication.add(panProductId, gridBagConstraints);

        panChargeType.setBorder(javax.swing.BorderFactory.createTitledBorder("Charges"));
        panChargeType.setMinimumSize(new java.awt.Dimension(360, 70));
        panChargeType.setPreferredSize(new java.awt.Dimension(360, 70));
        panChargeType.setLayout(new java.awt.GridBagLayout());

        panFolioCharges.setLayout(new java.awt.GridBagLayout());

        chkFolioCharges.setText("Folio");
        chkFolioCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkFolioChargesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panFolioCharges.add(chkFolioCharges, gridBagConstraints);

        chkInoperativeCharges.setText("InOperative");
        chkInoperativeCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkInoperativeChargesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panFolioCharges.add(chkInoperativeCharges, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        panChargeType.add(panFolioCharges, gridBagConstraints);

        panStatementCharges.setLayout(new java.awt.GridBagLayout());

        chkExcessTransactionCharges.setText("Excess Transaction");
        chkExcessTransactionCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkExcessTransactionChargesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panStatementCharges.add(chkExcessTransactionCharges, gridBagConstraints);

        chkNonMaintenanceOFMinBalCharges.setText("Non-Maintenance of Minimum Balance");
        chkNonMaintenanceOFMinBalCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkNonMaintenanceOFMinBalChargesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panStatementCharges.add(chkNonMaintenanceOFMinBalCharges, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        panChargeType.add(panStatementCharges, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChargesApplication.add(panChargeType, gridBagConstraints);

        panToAccount.setLayout(new java.awt.GridBagLayout());

        lblToAccount.setText("To Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panToAccount.add(lblToAccount, gridBagConstraints);

        txtToAccount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtToAccount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtToAccountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panToAccount.add(txtToAccount, gridBagConstraints);

        lblToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panToAccount.add(lblToDate, gridBagConstraints);

        tdtToDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtToDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panToAccount.add(tdtToDate, gridBagConstraints);

        btnToAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnToAccount.setToolTipText("To Account");
        btnToAccount.setPreferredSize(new java.awt.Dimension(21, 21));
        btnToAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToAccountActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panToAccount.add(btnToAccount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChargesApplication.add(panToAccount, gridBagConstraints);

        panTable.setLayout(new java.awt.GridBagLayout());

        scrOutputMsg.setMinimumSize(new java.awt.Dimension(150, 150));
        scrOutputMsg.setPreferredSize(new java.awt.Dimension(150, 150));

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
        scrOutputMsg.setViewportView(tblLog);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTable.add(scrOutputMsg, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChargesApplication.add(panTable, gridBagConstraints);

        panChoice.setLayout(new java.awt.GridBagLayout());

        rdoTaskType.add(rdoCharges);
        rdoCharges.setText("Charges Calculation");
        rdoCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoChargesActionPerformed(evt);
            }
        });
        panChoice.add(rdoCharges, new java.awt.GridBagConstraints());

        rdoTaskType.add(rdoInterest);
        rdoInterest.setText("Interest Calculation");
        rdoInterest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoInterestActionPerformed(evt);
            }
        });
        panChoice.add(rdoInterest, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChargesApplication.add(panChoice, gridBagConstraints);

        panInterestType.setBorder(javax.swing.BorderFactory.createTitledBorder("Interest"));
        panInterestType.setMinimumSize(new java.awt.Dimension(404, 75));
        panInterestType.setPreferredSize(new java.awt.Dimension(404, 75));
        panInterestType.setLayout(new java.awt.GridBagLayout());

        panCredit.setLayout(new java.awt.GridBagLayout());

        chkCredit.setText("Credit");
        chkCredit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCreditActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCredit.add(chkCredit, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panInterestType.add(panCredit, gridBagConstraints);

        panDebit.setLayout(new java.awt.GridBagLayout());

        chkDebit.setText("Debit");
        chkDebit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkDebitActionPerformed(evt);
            }
        });
        panDebit.add(chkDebit, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panInterestType.add(panDebit, gridBagConstraints);

        panInterestTypeDebit.setBorder(javax.swing.BorderFactory.createTitledBorder("Interest"));
        panInterestTypeDebit.setMaximumSize(new java.awt.Dimension(250, 50));
        panInterestTypeDebit.setMinimumSize(new java.awt.Dimension(250, 50));
        panInterestTypeDebit.setPreferredSize(new java.awt.Dimension(250, 50));
        panInterestTypeDebit.setLayout(new java.awt.GridBagLayout());

        rdoTaskType1.add(rdoInterestYes);
        rdoInterestYes.setText("Yes");
        rdoInterestYes.setMaximumSize(new java.awt.Dimension(55, 27));
        rdoInterestYes.setMinimumSize(new java.awt.Dimension(55, 27));
        rdoInterestYes.setPreferredSize(new java.awt.Dimension(55, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        panInterestTypeDebit.add(rdoInterestYes, gridBagConstraints);

        rdoTaskType1.add(rdoInterestNo);
        rdoInterestNo.setText("No");
        rdoInterestNo.setMaximumSize(new java.awt.Dimension(55, 27));
        rdoInterestNo.setMinimumSize(new java.awt.Dimension(55, 27));
        rdoInterestNo.setPreferredSize(new java.awt.Dimension(55, 27));
        panInterestTypeDebit.add(rdoInterestNo, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterestType.add(panInterestTypeDebit, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChargesApplication.add(panInterestType, gridBagConstraints);

        btnClearTrail.setText("Clear Trail");
        btnClearTrail.setMaximumSize(new java.awt.Dimension(103, 24));
        btnClearTrail.setMinimumSize(new java.awt.Dimension(103, 24));
        btnClearTrail.setPreferredSize(new java.awt.Dimension(103, 24));
        btnClearTrail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearTrailActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        panChargesApplication.add(btnClearTrail, gridBagConstraints);
        btnClearTrail.getAccessibleContext().setAccessibleName("Re Calculate");

        getContentPane().add(panChargesApplication, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void chkFolioChargesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkFolioChargesActionPerformed
        // TODO add your handling code here:
        ////System.out.println("Folio");
        String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
        if(!prodType.equals("TD")) {
            allChargesCommonDate();
        }
    }//GEN-LAST:event_chkFolioChargesActionPerformed
    private void allChargesCommonDate(){
        HashMap allChargeMap=new HashMap();
        
        final String PRODID = CommonUtil.convertObjToStr(((ComboBoxModel)cboProductId.getModel()).getKeyForSelected());
        final String PRODTYPE = CommonUtil.convertObjToStr(((ComboBoxModel)cboProdType.getModel()).getKeyForSelected());
        allChargeMap.put(PROD_ID,PRODID);
        allChargeMap.put("PROD_TYPE",PRODTYPE);
        allChargeMap.put("BRANCH_ID",TrueTransactMain.BRANCH_ID);
        if(chkFolioCharges.isSelected()) {
            allChargeMap.put("CHARGE_TYPE","FolioChargesTask");
        }
        if(chkInoperativeCharges.isSelected()) {
            allChargeMap.put("CHARGE_TYPE","InoperativeChargesTask");
        }
        List lst= ClientUtil.executeQuery("getSelectCommonFolioCharges", allChargeMap);
        if(lst !=null && lst.size()>0) {
            allChargeMap=(HashMap)lst.get(0);
        }
        String from_date=CommonUtil.convertObjToStr(allChargeMap.get("LAST_CHARG_CALC_DT"));
        String to_date=CommonUtil.convertObjToStr(allChargeMap.get("NEXT_CHARG_CALC_DT"));
        from_date= CommonUtil.convertObjToStr(DateUtil.addDays(DateUtil.getDateMMDDYYYY(from_date),1));
        to_date= CommonUtil.convertObjToStr(DateUtil.addDays(DateUtil.getDateMMDDYYYY(to_date),0));
        tdtToDate.setDateValue(to_date);
        tdtFromDate.setDateValue(from_date);
        dateEnableDisable(false);
        String curr_dt=CommonUtil.convertObjToStr(CurDate);
        ClientUtil.validateFromDate(tdtFromDate,curr_dt);
        //System.out.println("hai############");
//        actNumEnableDisable(false);
        ClientUtil.validateFromDate(tdtToDate,curr_dt);
    }
    private void chkNonMaintenanceOFMinBalChargesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkNonMaintenanceOFMinBalChargesActionPerformed
        //System.out.println("Non-Maintenance of Minimum Balance");
        String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();       // TODO add your handling code here:
        if(prodType.equals("TD")) {
            chkInoperativeCharges.setSelected(false);
            chkNonMaintenanceOFMinBalCharges.setSelected(true);
            chkFolioCharges.setVisible(true);
            chkFolioCharges.setEnabled(true);
            chkFolioCharges.setText("Cash");
            chkExcessTransactionCharges.setVisible(true);
            chkExcessTransactionCharges.setEnabled(true);
            chkExcessTransactionCharges.setSelected(true);
            chkExcessTransactionCharges.setText("Transfer");
        }
    }//GEN-LAST:event_chkNonMaintenanceOFMinBalChargesActionPerformed
    
    private void enableDisabled() {
        panInterestTypeDebit.setVisible(false);
        chkInoperativeCharges.setText("InOperative");
        chkNonMaintenanceOFMinBalCharges.setText("Non-Maintenance of Minimum Balance");
        panChargeType.setEnabled(true);
        chkFolioCharges.setVisible(true);
        rdoInterestYes.setVisible(false);
        rdoInterestNo.setVisible(false);
        chkFolioCharges.setEnabled(true);
        rdoCharges.setVisible(true);
        rdoInterest.setVisible(true);
        chkExcessTransactionCharges.setVisible(true);
        chkExcessTransactionCharges.setEnabled(true);
        chkInoperativeCharges.setEnabled(true);
        chkNonMaintenanceOFMinBalCharges.setEnabled(true);
        chkDebit.setEnabled(true);
        chkDebit.setVisible(true);
        chkCredit.setEnabled(true);
    }
    
    private void enableDisableForTD(){
        chkNonMaintenanceOFMinBalCharges.setVisible(true);
        chkInoperativeCharges.setVisible(true);
        chkInoperativeCharges.setText("Interest Provisining");
        chkNonMaintenanceOFMinBalCharges.setText("Interest Application");
        chkFolioCharges.setVisible(false);
        rdoCharges.setVisible(false);
        rdoInterest.setVisible(false);
        rdoInterestYes.setVisible(false);
        rdoInterestNo.setVisible(false);
        chkExcessTransactionCharges.setVisible(false);
        chkInoperativeCharges.setEnabled(true);
        chkNonMaintenanceOFMinBalCharges.setEnabled(true);
        chkCredit.setVisible(true);
        chkDebit.setVisible(false);
        chkCredit.setEnabled(true);
        panInterestTypeDebit.setVisible(false);
        ///chkNonMaintenanceOFMinBalCharges.setVisible(false);
    }
    
    private void enableDisableForSH(){
        chkNonMaintenanceOFMinBalCharges.setVisible(true);
        chkInoperativeCharges.setVisible(true);
        chkInoperativeCharges.setText("Dividend Calculation");
        chkNonMaintenanceOFMinBalCharges.setText("Dividend Transfer");
        chkFolioCharges.setVisible(false);
        rdoCharges.setVisible(false);
        rdoInterest.setVisible(false);
        rdoInterestYes.setVisible(false);
        rdoInterestNo.setVisible(false);
        chkExcessTransactionCharges.setVisible(false);
        chkInoperativeCharges.setEnabled(true);
        chkNonMaintenanceOFMinBalCharges.setEnabled(true);
        chkCredit.setVisible(true);
        chkDebit.setVisible(false);
        chkCredit.setEnabled(true);
        panInterestTypeDebit.setVisible(false);
        
        
    }
    private void enableDisableForTL(){
        
        chkFolioCharges.setVisible(true);
        //        rdoCharges.setVisible(true);
        rdoInterest.setVisible(true);
        panInterestTypeDebit.setVisible(true);
        chkDebit.setVisible(true);
        rdoInterestYes.setVisible(true);
        rdoInterestYes.setSelected(true);
        rdoInterestNo.setVisible(true);
        rdoInterestYes.setEnabled(true);
        rdoInterestNo.setEnabled(true);
        chkExcessTransactionCharges.setVisible(true);
        chkExcessTransactionCharges.setEnabled(true);
        chkFolioCharges.setEnabled(true);
        chkInoperativeCharges.setVisible(false);
        chkNonMaintenanceOFMinBalCharges.setVisible(false);
        chkCredit.setVisible(false);
        chkCredit.setEnabled(false);
        chkDebit.setEnabled(true);
        
    }
    private void chkInoperativeChargesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkInoperativeChargesActionPerformed
        //System.out.println("InOperative");
        String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();       // TODO add your handling code here:
        if(prodType.equals("TD")) {
            chkInoperativeCharges.setSelected(true);
            chkNonMaintenanceOFMinBalCharges.setSelected(false);
            chkFolioCharges.setVisible(false);
            chkExcessTransactionCharges.setVisible(false);
            chkFolioCharges.setSelected(true);
            chkExcessTransactionCharges.setSelected(false);
        }
        if(!prodType.equals("TD")) {
            allChargesCommonDate();
        }
    }//GEN-LAST:event_chkInoperativeChargesActionPerformed
    public void setAccountHead(String prodType) {
        //rdoCharges.setEnabled(false);
        //chkFolioCharges.setEnabled(false);
        //chkExcessTransactionCharges.setEnabled(false);
        //chkInoperativeCharges.setEnabled(false);
        //chkNonMaintenanceOFMinBalCharges.setEnabled(false);
        if (prodType.equals("TD")) {
            enableDisableForTD();
        }else if(prodType.equals("TL") || prodType.equals("AD")){
            enableDisableForTL();
            if(prodType.equals("AD")){
           // chkCredit.setVisible(true);
           //  chkCredit.setEnabled(true);
            }
        }else if (prodType.equals("SH")){
            enableDisableForSH();
            
        }
        else{
            enableDisabled();
        }
        if (prodType.equals("GL")) {
            key = new ArrayList();
            value = new ArrayList();
            cboProductId.setEnabled(false);
            cboProductId.setSelectedItem("");
        } else {
            cboProductId.setEnabled(true);
            try {
                lookUpHash = new HashMap();
                lookUpHash.put(CommonConstants.MAP_NAME,"Cash.getAccProduct" + prodType);
                lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
                cboProductId.setModel(new ComboBoxModel(key,value));
            } catch(Exception e){
                //System.out.println("Error in cboProductIdActionPerformed()");
            }
        }
        //Added By Suresh
        if (prodType.equals("OA") || prodType.equals("AD")) {
            rdoCharges.setEnabled(true);
            chkFolioCharges.setVisible(true);
            chkExcessTransactionCharges.setVisible(true);
            chkInoperativeCharges.setVisible(true);
            chkNonMaintenanceOFMinBalCharges.setVisible(true);
            rdoInterest.setSelected(true);
            rdoInterestActionPerformed(null);
             if(prodType.equals("AD")){
                 chkNonMaintenanceOFMinBalCharges.setVisible(false);
                 chkNonMaintenanceOFMinBalCharges.setSelected(false);
             }
        }
    }
    
    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        if (cboProdType.getSelectedIndex() > 0) {
            String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();       // TODO add your handling code here:
            setAccountHead(prodType);
             if (prodType.equals("OA") || prodType.equals("TD")|| prodType.equals("AD")) {
                 chkTrial.setVisible(true);  
             }else{
                 chkTrial.setVisible(false); 
                 //setvisibleTrial(false);
             }
        }
    }//GEN-LAST:event_cboProdTypeActionPerformed
    
    private void rdoInterestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoInterestActionPerformed
        // TODO add your handling code here:
        /** Enable the Particular Panel Depending on the Selection Made...*/
        if (rdoInterest.isSelected()) {
            ClientUtil.clearAll(panChargeType);
            ClientUtil.enableDisable(panChargeType, false);
            
            ClientUtil.clearAll(panInterestType);
            ClientUtil.enableDisable(panInterestType, true);
//            btnChargesPrint.setEnabled(false);
//            setvisibleTrial(true); //Added by kannan
        }
        if((((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString()).equals("TL") ||
        (((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString()).equals("AD")){
            chkDebit.setSelected(true);
            chkDebit.setEnabled(true);
            rdoInterest.setSelected(true);
            HashMap map=new HashMap();
            map.put(PROD_ID,((ComboBoxModel)cboProductId.getModel()).getKeyForSelected().toString());
            List lst=ClientUtil.executeQuery("GETLASTCALCDT",map );
            if(lst !=null && lst.size()>0){
                map=(HashMap)lst.get(0);
                tdtFromDate.setDateValue(CommonUtil.convertObjToStr(map.get("LAST_INTCALC_DTDEBIT")));
                //tdtFromDate.setEnabled(false);
            }
            
        }
        
        
    }//GEN-LAST:event_rdoInterestActionPerformed
    
    private void rdoChargesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoChargesActionPerformed
        // TODO add your handling code here:
        if (cboProductId.getSelectedIndex() > 0) {
            if (rdoCharges.isSelected()) {
                ClientUtil.clearAll(panChargeType);
                ClientUtil.enableDisable(panChargeType, true);
                ClientUtil.clearAll(panInterestType);
                ClientUtil.enableDisable(panInterestType, false);
                cboProductIdActionPerformed(null);
                //Added By Suresh
                if (cboProdType.getSelectedIndex() > 0 && rdoCharges.isSelected()) {
                    String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
                    if (prodType.equals("OA") || prodType.equals("AD")) {
                        ClientUtil.enableDisable(panChargeType, true);
                        rdoCharges.setEnabled(true);
                            if (txtFromAccount.getText().length() <= 0 || txtToAccount.getText().length() <= 0
                                || tdtFromDate.getDateValue().length() <= 0 || tdtToDate.getDateValue().length() <= 0
                                || cboProductId.getSelectedIndex() <= 0) {
                            if (cboProductId.getSelectedIndex() <= 0) {
                                ClientUtil.showAlertWindow("Product ID Should not be Empty !!! ");
                                return;
                            }
                            if (txtFromAccount.getText().length() <= 0) {
                                ClientUtil.showAlertWindow("From Account Number Should not be Empty !!! ");
                                return;
                            }
                            if (txtToAccount.getText().length() <= 0) {
                                ClientUtil.showAlertWindow("To Account Number Should not be Empty !!! ");
                                return;
                            }
                            if (tdtFromDate.getDateValue().length() <= 0) {
                                ClientUtil.showAlertWindow("From Date Should not be Empty !!! ");
                                return;
                            }
                            if (tdtToDate.getDateValue().length() <= 0) {
                                ClientUtil.showAlertWindow("To Date Should not be Empty !!! ");
                                return;
                            }
                            rdoInterest.setSelected(true);
                        }
                        java.util.Date toDate = (java.util.Date) DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue());
                        java.util.Date fromDate = (java.util.Date) DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue());
                        GregorianCalendar calendar = new GregorianCalendar();
                        calendar.setTime(toDate);
                        int lastDay = calendar.getActualMaximum(calendar.DAY_OF_MONTH);
                        int firstDay = calendar.getActualMinimum(calendar.DAY_OF_MONTH);
                        System.out.println("########### First_Day Of FromDate Month : " + firstDay);
                        System.out.println("########### Last_Day Of To_Date   Month : " + lastDay);
                        toDate.setDate(lastDay);
                        fromDate.setDate(firstDay);
                        //Should not allow Future Month Date
                        Date currentDate = (Date) CurDate.clone();
                        calendar.setTime(currentDate);
                        lastDay = calendar.getActualMaximum(calendar.DAY_OF_MONTH);
                        currentDate.setDate(lastDay);
                        if (DateUtil.dateDiff(currentDate, toDate) > 0) {
                            ClientUtil.showAlertWindow("To Date Should not be Future Date !!! ");
                            tdtToDate.setDateValue("");
                            return;
                        } else {
                            tdtToDate.setDateValue(DateUtil.getStringDate(toDate));
                        }
                        tdtFromDate.setDateValue(DateUtil.getStringDate(fromDate));
                        
                    } else {
                        ClientUtil.enableDisable(panChargeType, false);
                        rdoCharges.setEnabled(false);
                    }
                    chkFolioCharges.setEnabled(true);
                    chkExcessTransactionCharges.setEnabled(false);
                    chkInoperativeCharges.setEnabled(false);
                    chkNonMaintenanceOFMinBalCharges.setEnabled(false);
                    if (prodType.equals("OA") || prodType.equals("AD")) {
                        if (prodType.equals("OA"))
                            chkNonMaintenanceOFMinBalCharges.setEnabled(true);
                        //Added By Suresh
//                        btnChargesPrint.setEnabled(true);
                    } else {
//                        btnChargesPrint.setEnabled(false);
                    }
                } else {
//                    btnChargesPrint.setEnabled(false);
                } 
                setvisibleTrial(false); //Added by kannan
            } 
        } else {
            ClientUtil.showMessageWindow("Product Id Should not be Empty !!!");
            return;
        }
    }//GEN-LAST:event_rdoChargesActionPerformed
 
    private void setvisibleTrial(boolean flag) {
        //Added by kannan
        chkTrial.setVisible(flag);
        btnView.setVisible(flag);
        btnClearTrail.setVisible(flag);
    }
    
    private void cboProductIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIdActionPerformed
        // TODO add your handling code here:
        if (!rdoCharges.isSelected()) {
            actNumEnableDisable(true);
        }
        //added by abi next 2 line
         final String PRODID = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductId.getModel()).getKeyForSelected());
         final String PRODTYPE = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdType.getModel()).getKeyForSelected());
        
        if (rdoCharges.isSelected()) {
            setInvisible();
            //Added By Suresh
            chkNonMaintenanceOFMinBalCharges.setEnabled(true);
            if (!(CommonUtil.convertObjToStr(cboProductId.getSelectedItem())).equalsIgnoreCase("")) {
//                final String PRODID = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductId.getModel()).getKeyForSelected());
//                final String PRODTYPE = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdType.getModel()).getKeyForSelected());

                try {
                    final HashMap prodDataMap = new HashMap();
                    prodDataMap.put("value",PRODID);
                    /** "getSelectOperativeAcctChargesParamTO" is Defined in OperativeAcctProductMap...*/
                    List resultList=null;
                    if(PRODTYPE.equals("AD")){
                        resultList = (List)ClientUtil.executeQuery("getSelectLoanProductChargesTO", prodDataMap);
                    } else {
                        resultList = (List)ClientUtil.executeQuery("getSelectOperativeAcctChargesParamTO", prodDataMap);
                    }


                    if (resultList != null && resultList.size() > 0) {
                        LoanProductChargesTO loanProductCharge = null;
                        OperativeAcctChargesParamTO obj = null;
                        if (PRODTYPE.equals("AD")) {
                            HashMap folioMap = new HashMap();
                            folioMap.put(PROD_ID, PRODID);
                            folioMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
                            tdtToDate.setDateValue(CommonUtil.convertObjToStr(CurDate));
                            loanProductCharge = (LoanProductChargesTO) resultList.get(0);
                            //                            List lst= ClientUtil.executeQuery("getSelectCommonFolioCharges", folioMap);
                            //                            if(lst !=null && lst.size()>0)
                            //                                folioMap=(HashMap)lst.get(0);
                            //                            String from_date=CommonUtil.convertObjToStr(folioMap.get("LAST_CHARG_CALC_DT"));
                            //                            String to_date=CommonUtil.convertObjToStr(folioMap.get("NEXT_CHARG_CALC_DT"));
                            //                            tdtToDate.setDateValue(to_date);
                            //                            tdtFromDate.setDateValue(from_date);
                            //                            dateEnableDisable(false);
                            //
                            //                            ClientUtil.validateFromDate(tdtFromDate,curr_dt);
                            //                            //System.out.println("hai############");
                            //                            ClientUtil.validateFromDate(tdtToDate,curr_dt);
                            //                                 tdtToDate.setDateValue(to_date);
                        }
                        else{
                            obj = (OperativeAcctChargesParamTO)resultList.get(0);
                            dateEnableDisable(true);
                        }
                        if(PRODTYPE.equals("TD")){
                            HashMap chkMap=new HashMap();
                            chkMap.put("PROD_ID",PRODID);
                            List behavesLikeList=ClientUtil.executeQuery("getAcctHead", chkMap);
                            HashMap behavesMap=(HashMap)(behavesLikeList.get(0));
                            behavesLike=CommonUtil.convertObjToStr(behavesMap.get("BEHAVES_LIKE"));
                        }
                        /**if the particular Chargetype is to applied...*/
                        if(loanProductCharge !=null && loanProductCharge.getFolioChrgAppl().equals("Y") || obj!=null && obj.getFolioChgApplicable().equals("Y")){
                            if(obj !=null && CommonUtil.convertObjToInt(obj.getRatePerFolio()) > 0 || loanProductCharge !=null && CommonUtil.convertObjToInt(loanProductCharge.getRatePerFolio()) > 0 ){
                                chkFolioCharges.setEnabled(true);
                            }
                            if( obj !=null && CommonUtil.convertObjToInt(obj.getInoperativeAcCharges()) > 0){
                                chkInoperativeCharges.setEnabled(true);
                            }
                            if(obj !=null && CommonUtil.convertObjToInt(obj.getChgExcessfreewdPertrans()) > 0){
                                chkExcessTransactionCharges.setEnabled(true);
                            }
                            if( obj !=null && CommonUtil.convertObjToInt(obj.getAmtNonmainMinbal()) > 0){
                                chkNonMaintenanceOFMinBalCharges.setEnabled(true);
                            }
                        }
                    }
                }catch(Exception e){
                    //System.out.println("Error in cboProductIdActionPerformed()");
                }
            }
        }else{
                if (PRODTYPE.equals("AD")) {
                    tdtToDate.setDateValue(CommonUtil.convertObjToStr(CurDate));
                    rdoInterestActionPerformed(null);
                }

        }
        String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
        String prodID = "";
        if (cboProductId.getSelectedIndex() > 0) {
            prodID = ((ComboBoxModel) cboProductId.getModel()).getKeyForSelected().toString();
        }
       if (prodType.equals("TD")||prodType.equals("OA")) {
        if (cboProductId.getSelectedIndex() > 0) {      //Added By Suresh
            setFromDate(prodType, prodID);
        }}
    }//GEN-LAST:event_cboProductIdActionPerformed
    
    private void dateEnableDisable(boolean flag){
        tdtToDate.setEnabled(flag);
        tdtFromDate.setEnabled(flag);
    }
    private void actNumEnableDisable(boolean flag){
        txtFromAccount.setEnabled(flag);
        txtToAccount.setEnabled(flag);
        txtFromAccount.setText("");
        txtToAccount.setText("");
    }
    private void txtToAccountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToAccountFocusLost
        // TODO add your handling code here:
        final String MESSAGE = validateAccNo();
        if(!MESSAGE.equalsIgnoreCase("")){
            displayAlert(MESSAGE);
        }
        //Added By Suresh
        if(rdoCharges.isSelected() && tdtToDate.getDateValue().length()>0 
                && txtFromAccount.getText().length()>0 && txtToAccount.getText().length()>0){
            rdoChargesActionPerformed(null);
        }
    }//GEN-LAST:event_txtToAccountFocusLost
    
    private void txtFromAccountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromAccountFocusLost
        // TODO add your handling code here:
        final String MESSAGE = validateAccNo();
        if(!MESSAGE.equalsIgnoreCase("")){
            displayAlert(MESSAGE);
        }
        //Added By Suresh
        if(rdoCharges.isSelected() && tdtToDate.getDateValue().length()>0 
                && txtFromAccount.getText().length()>0 && txtToAccount.getText().length()>0){
            rdoChargesActionPerformed(null);
        }
    }//GEN-LAST:event_txtFromAccountFocusLost
    private String validateAccNo(){
        String from = txtFromAccount.getText();
        String to = txtToAccount.getText();
        String message = "";
        if(!(from.equalsIgnoreCase("")|| to.equalsIgnoreCase(""))){
            if(from.compareTo(to) > 0){
                message = resourceBundle.getString("ACCOUNTWARNING");
            }
        }
        if(((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString().equals("TL") || ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString().equals("AD"))
        {HashMap hash=new HashMap();
         hash.put(PROD_ID,((ComboBoxModel)cboProductId.getModel()).getKeyForSelected());
         hash.put("ACT_NUM",txtFromAccount.getText());
         if(txtToAccount !=null && (! txtToAccount.getText().equals(""))) {
             hash.put("ACT_NUM",txtToAccount.getText());
         }
         hash.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
         List actlst=ClientUtil.executeQuery("getActNotCLOSEDTL",hash);
         if(actlst != null &&  actlst.size()>0 ){
         } else{
             ClientUtil.displayAlert("Enter the current Number");
             txtFromAccount.setText("");
             txtToAccount.setText("");
             return message;
         }
        }
        return message;
    }
    private void tdtFromDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFromDateFocusLost
        // TODO add your handling code here:
        if(CommonUtil.convertObjToStr(tdtToDate.getDateValue()).equalsIgnoreCase("")){
            tdtToDate.setDateValue(DateUtil.getStringDate(CurDate));
        }else{
            ClientUtil.validateFromDate(tdtFromDate, tdtToDate.getDateValue());
        }
    }//GEN-LAST:event_tdtFromDateFocusLost
    
    private void tdtToDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtToDateFocusLost
        // TODO add your handling code here:
        ClientUtil.validateToDate(tdtToDate, tdtFromDate.getDateValue());
        String prodType =CommonUtil.convertObjToStr(cboProdType.getSelectedItem());
        if(prodType.equals("Advances")){
            String toDate=CommonUtil.convertObjToStr(tdtToDate.getDateValue());
            if(toDate.length()>0){
               if(DateUtil.dateDiff(CurDate, DateUtil.getDateMMDDYYYY(toDate))>0){
                   Date workingDayDt=holiydaychecking(getProperDateFormat(toDate));
                   if(DateUtil.dateDiff(workingDayDt,CurDate)<0){
                       ClientUtil.displayAlert("Please Mark Holidy for future Date !!!!");
                       tdtToDate.setDateValue("");
                       return;
                   }
                    
                }
            }
        }
        if(rdoCharges.isSelected() && tdtToDate.getDateValue().length()>0){
            rdoChargesActionPerformed(null);
        }
    }//GEN-LAST:event_tdtToDateFocusLost
     private Date holiydaychecking(Date lstintCr){
        //System.out.println("inside HolidyChecking");
        try{
            HashMap MonthEnd=new HashMap(); //traansferto holidaychecking method;
            boolean checkHoliday=true;
            //System.out.println("lstintCr   "+lstintCr);
            MonthEnd.put("NEXT_DATE",lstintCr);
            MonthEnd.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            //                try{
            //            sqlMap.startTransaction();
            lstintCr=lstintCr;
            while(checkHoliday){
                boolean tholiday = false;
                //System.out.println("enterytothecheckholiday"+checkHoliday);
                List Holiday=ClientUtil.executeQuery("checkHolidayDateOD",MonthEnd);
                List weeklyOf=ClientUtil.executeQuery("checkWeeklyOffOD",MonthEnd);
                boolean isHoliday = Holiday.size()>0 ? true : false;
                boolean isWeekOff = weeklyOf.size()>0 ? true : false;
                if (isHoliday || isWeekOff) {
                    String processType="";
                   lstintCr=DateUtil.addDays(lstintCr,-1);
                    MonthEnd.put("NEXT_DATE",lstintCr);
//                    MonthEnd = dateAdd(MonthEnd);
                    checkHoliday=true;
                }else{
                    checkHoliday=false;
                    
                    
                }
            }
//            return (Date)MonthEnd.get("NEXT_DATE");
             return lstintCr;
        }catch(Exception e){
            e.printStackTrace();
            return null;
            
        }
        
    }
     
      public Date getProperDateFormat(Object obj) {
        Date currDt = null;
        if (obj!=null && obj.toString().length()>0) {
            Date tempDt= DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDt=(Date)CurDate.clone();
            currDt.setDate(tempDt.getDate());
            currDt.setMonth(tempDt.getMonth());
            currDt.setYear(tempDt.getYear());
        }
        return currDt;
    }
    private void btnToAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToAccountActionPerformed
        // TODO add your handling code here:
        popUp(TO);
        //Added By Suresh
        if(rdoCharges.isSelected() && tdtToDate.getDateValue().length()>0 
                && txtFromAccount.getText().length()>0 && txtToAccount.getText().length()>0){
            rdoChargesActionPerformed(null);
        }
    }//GEN-LAST:event_btnToAccountActionPerformed
    
    private void btnFromAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFromAccountActionPerformed
        // TODO add your handling code here:
        popUp(FROM);
        //Added By Suresh
        if(rdoCharges.isSelected() && tdtToDate.getDateValue().length()>0 
                && txtFromAccount.getText().length()>0 && txtToAccount.getText().length()>0){
            rdoChargesActionPerformed(null);
        }
    }//GEN-LAST:event_btnFromAccountActionPerformed
    private void popUp(int field) {
        final HashMap viewMap = new HashMap();
        viewType = field;
        HashMap hash = new HashMap();
        String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
        if(prodType.equals("AD")){
            viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountListAD");
            hash.put("SELECTED_BRANCH", com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
            hash.put("FILTERED_LIST", "");
        } else if (prodType.equals("TL")) {
            viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountListTL");
            hash.put("SELECTED_BRANCH", com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
            hash.put("FILTERED_LIST", "");
        } else if (prodType.equals("TD")) {
            viewMap.put(CommonConstants.MAP_NAME, "TDCharges.getAcctList");
        } else  if(prodType.equals("SH")){
            if(((ComboBoxModel)cboProductId.getModel()).getKeyForSelected().toString().length()>0)
                hash.put("SHARE_TYPE", ((ComboBoxModel)cboProductId.getModel()).getKeyForSelected().toString());
            hash.put("DIVIDEND_PAY_MODE", "TRANSFER'");
            hash.put("DIVIDEND_PAID_STATUS","DIVIDEND_PAID_STATUS");
            
            viewMap.put(CommonConstants.MAP_NAME, "getSelectDividendUnclaimedTransferList");
        }else {
            viewMap.put(CommonConstants.MAP_NAME, "OACharges.getAcctList");
        }
        
        hash.put(PROD_ID, ((ComboBoxModel)cboProductId.getModel()).getKeyForSelected());
        hash.put(CommonConstants.BRANCH_ID, com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
        //        if(viewType==TO){
        //            hash.put("ACCT_NO", txtFromAccount.getText());
        //        }
        viewMap.put(CommonConstants.MAP_WHERE, hash);
        
        new ViewAll(this, viewMap).show();
    }
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        //System.out.println("Hash: " + hash);
        String alert = "";
        String prodID ="";
        String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
        if (cboProductId.getSelectedIndex() > 0) {
            prodID = ((ComboBoxModel) cboProductId.getModel()).getKeyForSelected().toString();
        }
        if (prodType.equals("TD")) {
            if (viewType == TO) {
                txtToAccount.setText(CommonUtil.convertObjToStr(hash.get("DEPOSIT NUMBER")));
                alert = validateAccNo();
            }else if(viewType==FROM){
                txtFromAccount.setText(CommonUtil.convertObjToStr(hash.get("DEPOSIT NUMBER")));
                alert = validateAccNo();
                if(cboProductId.getSelectedIndex()>0){      //Added By Suresh
                    setFromDate(prodType,prodID);
                }
            }
        } else if(prodType.equals("SH")) {
            if (viewType==TO){
                txtToAccount.setText(CommonUtil.convertObjToStr(hash.get("SHARE_ACCT_NO")));
                alert = validateAccNo();
            }else if(viewType==FROM){
                txtFromAccount.setText(CommonUtil.convertObjToStr(hash.get("SHARE_ACCT_NO")));
                alert = validateAccNo();
            }
        }else {
            if (viewType==TO){
                txtToAccount.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNT NUMBER")));
                if(hash.containsKey("ACCOUNTNO")) {
                    txtToAccount.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
                }
                alert = validateAccNo();
            }else if(viewType==FROM){
                txtFromAccount.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNT NUMBER")));
                if(hash.containsKey("ACCOUNTNO")) {
                    txtFromAccount.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
                }
                alert = validateAccNo();
                if(cboProductId.getSelectedIndex()>0){  //Added By Suresh
                    setFromDate(prodType,prodID);
                }
            }
        }
        if(!alert.equalsIgnoreCase("")){
            displayAlert(alert);
        }
    }
    
    //Added By Suresh           //To Display from Date
    private void setFromDate(String Prod_Type, String prod_ID) {
        if (Prod_Type.length() > 0 && prod_ID.length() > 0) {
            HashMap provMap = new HashMap();
            provMap.put("PROD_ID", prod_ID);
            provMap.put("PROD_TYPE", Prod_Type);
            provMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            List provList = ClientUtil.executeQuery("getLastProvisionDt", provMap);
            if (provList != null && provList.size() > 0) {
                provMap = (HashMap) provList.get(0);
                String from_date = CommonUtil.convertObjToStr(provMap.get("LAST_PROV_DT"));
                from_date = CommonUtil.convertObjToStr(DateUtil.addDays(DateUtil.getDateMMDDYYYY(from_date), 1));
                tdtFromDate.setDateValue(from_date);
            }
        }
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        HashMap reportParamMap = new HashMap();
        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed
    private TaskHeader getTaskFromMap(HashMap dataMap) { //String value, HashMap map, String transType, String prodType, String process) {
        TaskHeader tskHeader = new TaskHeader();
        try {
            tskHeader.setBranchID(ProxyParameters.BRANCH_ID);
            tskHeader.setBankID(ProxyParameters.BANK_ID);
            tskHeader.setUserID(ProxyParameters.USER_ID);
            tskHeader.setIpAddr(java.net.InetAddress.getLocalHost().getHostAddress());
            if((((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString()).equals("TL") || (((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString()).equals("AD")){
                tskHeader.setProductType(((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString());
                tskHeader.setTaskClass("InterestCalculationTask");
                dataMap.put("CHARGESUI","CHARGESUI");
            }else {
                tskHeader.setProductType("OA");
            }
            tskHeader.setTaskParam(dataMap);
        }catch(Exception E){
            //System.out.println("Error in Setting the Task Header...");
            E.printStackTrace();
        }
        return tskHeader;
    }
    
    private TaskHeader getTaskFromDepositMap(HashMap depositMap) { //String value, HashMap map, String transType, String prodType, String process) {
        TaskHeader tskHeader = new TaskHeader();
        try {
            tskHeader.setBranchID(ProxyParameters.BRANCH_ID);
            tskHeader.setBankID(ProxyParameters.BANK_ID);
            tskHeader.setUserID(ProxyParameters.USER_ID);
            tskHeader.setIpAddr(java.net.InetAddress.getLocalHost().getHostAddress());
            tskHeader.setProductType("TD");
            tskHeader.setTaskParam(depositMap);
            //System.out.println("#####getTaskFromDepoistMap : " +tskHeader);
        }catch(Exception E){
            //System.out.println("Error in Setting the Task Header...");
            E.printStackTrace();
        }
        return tskHeader;
    }
    
    private List performValueDate(List valueDateList, HashMap periodMap, HashMap resultMap) throws Exception{
        //System.out.println("#$#$# valueDateList : "+valueDateList+"  resultMap : "+resultMap);
        //        if (resultMap.containsKey(PROD_ID)) {
        //            runningProdId = CommonUtil.convertObjToStr(resultMap.get(PROD_ID));
        //            runningActNum = CommonUtil.convertObjToStr(resultMap.get("ACT_NUM"));
        //        } else {
        //            resultMap.put(InterestTaskRunner.PROD_ID, runningProdId);
        //            resultMap.put("ACT_NUM", runningActNum);
        //        }
        Date lstintCr = (Date) CurDate.clone();
        resultMap.put(AMOUNT, new Double(0));
        Date startDt = (Date) resultMap.get(START_DT);
        Date endDt = (Date) resultMap.get("END");
        Date valueDt = null;
        //        String productId = (String)resultMap.get(PROD_ID);
        //        HashMap periodMap = (HashMap)sqlMap.executeQueryForObject("getInterestCalcPeriod", productId);
        List dailyProductLst = new ArrayList();
        double prevAmount = CommonUtil.convertObjToDouble(resultMap.get("PREV_AMOUNT")).doubleValue();
        periodMap.put(AMOUNT, new Double(prevAmount));
        periodMap.put(PROD_ID, resultMap.get(PROD_ID));
        periodMap.put("ACT_NUM", resultMap.get("ACT_NUM"));
        Date START=DateUtil.addDays((Date)startDt.clone(),1);
        double passAmount = 0;
        int month = -1;
        int year = -1;
        List amtLst = new ArrayList();
        HashMap valueDateMap = (HashMap) valueDateList.get(0);
        valueDt = (Date) valueDateMap.get("VALUE_DT");
        Date transDt = (Date) valueDateMap.get("TRANS_DT");
        double amount = CommonUtil.convertObjToDouble(valueDateMap.get(AMOUNT)).doubleValue();
        passAmount = passAmount+prevAmount;
        String transType = CommonUtil.convertObjToStr(valueDateMap.get("TRANS_TYPE"));
        //System.out.println("#$#$# amount : "+amount);
        int lstSize = 0;
        //System.out.println("#$#$# START : "+START+"  valueDt : "+valueDt);
        int i=1;
        boolean nextValueDt = false;
        HashMap dailyProductHashMap = new HashMap();
        boolean startAsTransDt = false;
        if (DateUtil.dateDiff(START, valueDt)>=0) {    // If Value Date is later than LastIntCalcDt i.e. within current calc period.
            Date END = null;
            while (DateUtil.dateDiff(START, endDt)>0) {
                periodMap.put(START, START);
                if (START.getDate()!=1) {
                    GregorianCalendar calendar = new GregorianCalendar();
                    calendar.setTime(START);
                    END.setDate(calendar.getActualMaximum(calendar.DAY_OF_MONTH));
                } else {
                    END = DateUtil.addDays(START, 30);
                }
                
                if (DateUtil.dateDiff(END, valueDt)>0) {
                    nextValueDt = false;
                } else {
                    END = (Date) valueDt.clone();
                    nextValueDt = true;
                }
                //System.out.println("@!@!@! startAsTransDt... : "+startAsTransDt);
                //                if (startAsTransDt) {
                //                    amount = 0;
                //                    passAmount = 0;
                //                    startAsTransDt = false;
                //                }
                //System.out.println("@!@!@! 1 START : "+START+"@!@!@! 1 transDt : "+transDt+" 1 END : "+END);
                if (DateUtil.dateDiff(START, transDt)>0 && DateUtil.dateDiff(transDt, END)>0) {
                    //System.out.println("@!@!@! Inside transDt condition... ");
                    END = (Date) transDt.clone();
                    startAsTransDt = true;
                    passAmount = 0;
                    amount = 0;
                    nextValueDt = false;
                }
                
                //System.out.println("#$#$# 1 transDt : "+transDt);
                //System.out.println("#$#$# 1 START : "+START+" 1 END : "+END+" 1 valueDt : "+valueDt);
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(END);
                int lastDay = calendar.getActualMaximum(calendar.DAY_OF_MONTH);
                if (DateUtil.dateDiff(endDt, END)==0 || (DateUtil.dateDiff(END, valueDt)!=0 && END.getDate()==lastDay)
                || (DateUtil.dateDiff(START, END)==0 && END.getDate()==lastDay)) {
                    //System.out.println("##$#$ Last Day ... "+END);
                    END = DateUtil.addDays(END, 1);    // DAY_END_DT < END only checking in getDailyBalanceForValueDate map
                }
                
                periodMap.put("TODAY_DT", END);
                //System.out.println("#$#$# END : "+END+"  valueDt : "+valueDt);
                //System.out.println("#$#$# periodMap : "+periodMap);
                
                List lst = ClientUtil.executeQuery("getDailyBalanceForValueDate", periodMap);
                //System.out.println("#$#$# inside for loop  month : "+month+"  START.getMonth() : "+START.getMonth());
                if (lst!=null && lst.size()>0) {
                    if (month != START.getMonth()) {
                        amtLst = new ArrayList();
                    }
                    if (lst!=null && lst.size()>0) {
                        amtLst.addAll(lst);
                    }
                    dailyProductHashMap.put((START.getMonth()+1)+"-"+(START.getYear()+1900), amtLst);
                }
                //System.out.println("#$#$# amtLst  : "+amtLst);
                month = START.getMonth();
                year = START.getYear();
                START = (Date) END.clone();
                //System.out.println("#$#$# dailyProductLst : "+dailyProductHashMap);
                if (nextValueDt) {
                    if (transType.equals("DEBIT")) {
                        amount = -1*amount;
                    }
                    passAmount = passAmount + amount;
                    periodMap.put(AMOUNT, new Double(passAmount));
                    if (i<valueDateList.size()) {
                        //System.out.println("#$#$# Inside  nextValueDt i : "+i);
                        valueDateMap = (HashMap) valueDateList.get(i);
                        valueDt = (Date) valueDateMap.get("VALUE_DT");
                        amount = CommonUtil.convertObjToDouble(valueDateMap.get(AMOUNT)).doubleValue();
                        transType = CommonUtil.convertObjToStr(valueDateMap.get("TRANS_TYPE"));
                        //System.out.println("#$#$# Inside  nextValueDt inside if : "+valueDateMap);
                        i = i+1;
                    } else {
                        valueDt = (Date) endDt.clone();
                        amount = 0;
                        //System.out.println("#$#$# Inside  nextValueDt inside esle : "+valueDateMap);
    }
                    //System.out.println("#$#$# START : "+START+"  END : "+END+"  valueDt : "+valueDt);
                }
                //System.out.println("#$#$# valueDateMap at end of while loop : "+valueDateMap);
                //System.out.println("#$#$# periodMap at end of while loop : "+periodMap);
            }
            resultMap.put("PREV_AMOUNT", periodMap.get(AMOUNT));
            amount = 0;
            String valueDtTransMode = "";
            if (dailyProductHashMap!=null && dailyProductHashMap.size()>0) {
                Object arr[] = dailyProductHashMap.keySet().toArray();
                for (int j=0; j<arr.length; j++) {
                    amtLst = (List)dailyProductHashMap.get(arr[j]);
                    double[] sortList = new double[amtLst.size()];
                    for (int a=0; a<amtLst.size(); a++) {
                        sortList[a] = CommonUtil.convertObjToDouble(amtLst.get(a)).doubleValue();
                    }
                    java.util.Arrays.sort(sortList);
                    //System.out.println("#$#$# final amtLst : "+sortList.toString());
                    dailyProductLst.add(new Double(sortList[0]));
                    amount+=sortList[0];
                }
                //System.out.println("#### valueDt Product Amt = "+amount);
                periodMap.put(START_DT, resultMap.get(START_DT));
                periodMap.put("TODAY_DT", resultMap.get("END"));
                Date currDt = DateUtil.addDays(START, -1);
                //System.out.println("##@@## lstintCr = "+lstintCr+"##@@## currDt = "+currDt);
                if (DateUtil.dateDiff(currDt, lstintCr)!=0) {  // No need to take old balance for the current period
                    //System.out.println("#$#$# inside if Not for Current Period : "+currDt);
                    int add_months = 0;
                    double oldAmount = 0;
                    //                    if (paramMap.containsKey("ADD_MONTHS"))
                    //                        add_months = CommonUtil.convertObjToInt(paramMap.get("ADD_MONTHS"));
                    periodMap.put("ADD_MONTHS", new Integer(add_months));
                    //        //System.out.println("super._branchCode : " + super._branchCode);
                    //System.out.println("#$@## periodMap for old product amount = " + periodMap);
                    amtLst = ClientUtil.executeQuery("getDailyBalance", periodMap);
                    lstSize = amtLst.size();
                    //System.out.println("Amount List:" + amtLst);
                    for (int ai=0; ai < lstSize; ai++ ) {
                        oldAmount += CommonUtil.convertObjToDouble(amtLst.get(ai)).doubleValue();
                    }
                    //System.out.println("#### oldProdAmt = " + oldAmount);
                    resultMap.put("PROD_AMOUNT", new Double(oldAmount-amount));
                    valueDtTransMode = "REVERSAL";
                }
                if (!valueDtTransMode.equals("REVERSAL")) {
                    valueDtTransMode = "NORMAL";
                    resultMap.put("PROD_AMOUNT", new Double(amount));
                }
                resultMap.put("VALUE_DT_MODE", valueDtTransMode);
                //                runBatch(resultMap, paramMap, objLogDAO, objLogTO);
            }
        } else {
            START=(Date)startDt.clone();
            Date END = null;
            int FREQ = CommonUtil.convertObjToInt(resultMap.get("FREQ_A"));
            while (DateUtil.dateDiff(valueDt, START)>0) {
                START =  DateUtil.addDays(START, -1*FREQ);
            }
            END = (Date)START.clone();
            amount = 0;
            while (DateUtil.dateDiff(END, endDt)>0) {
                END = DateUtil.addDays(START, FREQ);
                //System.out.println("#$#$# START : "+START+"   END : "+END);
                resultMap.put(START, START);
                resultMap.put("END", END);
                List valueDateNewList = new ArrayList();
                Date newValueDt = null;
                for (int v=0; v<valueDateList.size(); v++) {
                    valueDateMap = (HashMap) valueDateList.get(v);
                    newValueDt = (Date) valueDateMap.get("VALUE_DT");
                    if ((DateUtil.dateDiff(START, newValueDt)>0) &&
                    (DateUtil.dateDiff(newValueDt, END)>0)) {
                        valueDateNewList.add(valueDateMap);
                    }
                }
                if (valueDateNewList.size()>0) {
                    dailyProductLst.add(performValueDate(valueDateNewList, periodMap, resultMap));
                } else {
                    dailyProductLst.add(performValueDate(resultMap, periodMap));
                }
                if (resultMap.containsKey("PROD_AMOUNT")) {
                    amount += CommonUtil.convertObjToDouble(resultMap.get("PROD_AMOUNT")).doubleValue();
                }
                
                START = (Date)END.clone();
            }
            //System.out.println("#$#$# final PRDUCT DIFFERENCE AMOUNT For Old Period  : "+amount);
            //                resultMap.put(AMOUNT, new Double(amount));
            //                runBatch(resultMap, paramMap, objLogDAO, objLogTO);
            
        }
        //System.out.println("#$#$# final dailyProductLst : "+dailyProductLst);
        periodMap = null;
        return dailyProductLst;
    }
    
    private List performValueDate(HashMap resultMap, HashMap periodMap) throws Exception{
        //System.out.println("#$#$#  resultMap performValueDate2 : "+resultMap);
        //        if (resultMap.containsKey(InterestTaskRunner.PROD_ID)) {
        //            runningProdId = CommonUtil.convertObjToStr(resultMap.get(InterestTaskRunner.PROD_ID));
        //            runningActNum = CommonUtil.convertObjToStr(resultMap.get("ACT_NUM"));
        //        } else {
        //            resultMap.put(InterestTaskRunner.PROD_ID, runningProdId);
        //            resultMap.put("ACT_NUM", runningActNum);
        //        }
        Date lstintCr = (Date) CurDate.clone();
        resultMap.put(AMOUNT, new Double(0));
        Date startDt = (Date) resultMap.get(START_DT);
        Date endDt = (Date) resultMap.get("END");
        HashMap dailyProductHashMap = new HashMap();
        List lst = new ArrayList();
        Date START = DateUtil.addDays((Date)startDt.clone(), 1);
        Date END = null;
        int lstSize = 0;
        while (DateUtil.dateDiff(START, endDt)>0) {
            END =  DateUtil.addDays(START, 30);
            periodMap.put(START, START);
            periodMap.put("TODAY_DT", END);
            
            //System.out.println("#$#$# periodMap in performValueDate 2 : "+periodMap);
            
            lst = ClientUtil.executeQuery("getDailyBalanceForValueDate", periodMap);
            
            dailyProductHashMap.put((START.getMonth()+1)+"-"+(START.getYear()+1900), lst);
            //System.out.println("#$#$# dailyProductLst in performValueDate 2 : "+dailyProductHashMap);
            START = (Date) END.clone();
        }
        List dailyProductLst = new ArrayList();
        String valueDtTransMode = "";
        if (dailyProductHashMap!=null && dailyProductHashMap.size()>0) {
            Object arr[] = dailyProductHashMap.keySet().toArray();
            for (int j=0; j<arr.length; j++) {
                lst = (List)dailyProductHashMap.get(arr[j]);
                double[] sortList = new double[lst.size()];
                for (int a=0; a<lst.size(); a++) {
                    sortList[a] = CommonUtil.convertObjToDouble(lst.get(a)).doubleValue();
                }
                java.util.Arrays.sort(sortList);
                //System.out.println("#$#$# final amtLst : "+sortList.toString());
                dailyProductLst.add(new Double(sortList[0]));
            }
            lstSize = dailyProductLst.size();
            double valueDtProdAmt = 0;
            for (int ai=0; ai < lstSize; ai++ ) {
                valueDtProdAmt += CommonUtil.convertObjToDouble(dailyProductLst.get(ai)).doubleValue();
            }
            //System.out.println("#### valueDtProdAmt = " + valueDtProdAmt);
            Date currDt = DateUtil.addDays(START, -1);
            //System.out.println("##@@## lstintCr = "+lstintCr+"##@@## currDt = "+currDt);
            if (DateUtil.dateDiff(currDt, lstintCr)!=0) {  // No need to take old balance for the current period
                periodMap.put(START, resultMap.get(START_DT));
                periodMap.put("TODAY_DT", resultMap.get("END"));
                int add_months = 0;
                double amount = 0;
                //                if (paramMap.containsKey("ADD_MONTHS"))
                //                    add_months = CommonUtil.convertObjToInt(paramMap.get("ADD_MONTHS"));
                periodMap.put("ADD_MONTHS", new Integer(add_months));
                //        //System.out.println("super._branchCode : " + super._branchCode);
                //System.out.println("#$@## periodMap for old product amount = " + periodMap);
                List amtLst = ClientUtil.executeQuery("getDailyBalance", periodMap);
                lstSize = amtLst.size();
                //System.out.println("Amount List:" + amtLst);
                for (int ai=0; ai < lstSize; ai++ ) {
                    amount += CommonUtil.convertObjToDouble(amtLst.get(ai)).doubleValue();
                }
                //System.out.println("#### oldProdAmt = " + amount);
                resultMap.put("PROD_AMOUNT", new Double(amount-valueDtProdAmt));
                valueDtTransMode = "REVERSAL";
            }
            if (!valueDtTransMode.equals("REVERSAL")) {
                valueDtTransMode = "NORMAL";
                resultMap.put("PROD_AMOUNT", new Double(valueDtProdAmt));
            }
            resultMap.put("VALUE_DT_MODE", valueDtTransMode);
            //            runBatch(resultMap, paramMap, objLogDAO, objLogTO);
        }
        //System.out.println("#$#$# final dailyProductLst in performValueDate 2 : "+dailyProductLst);
        periodMap = null;
        return dailyProductLst;
    }
    
    private void doValueDate(List valueDateList, HashMap periodMap, HashMap resultMap) throws Exception{
        //System.out.println("#$#$# valueDateList : "+valueDateList+"  resultMap : "+resultMap);
        Date lstintCr = (Date) CurDate.clone();
        resultMap.put(AMOUNT, new Double(0));
        Date startDt = (Date) resultMap.get(START_DT);
        Date endDt = (Date) resultMap.get("END");
        periodMap.put(AMOUNT, new Double(0));
        periodMap.put(PROD_ID, resultMap.get(PROD_ID));
        periodMap.put("ACT_NUM", resultMap.get("ACT_NUM"));
        HashMap valueDateMap = (HashMap) valueDateList.get(0);
        Date valueDt = (Date) valueDateMap.get("VALUE_DT");
        Date transDt = null;
        String transType = CommonUtil.convertObjToStr(valueDateMap.get("TRANS_TYPE"));
        HashMap dailyProductHashMap = new HashMap();
        List dailyProductLst = new ArrayList();
        double addAmount = 0;
        
        Date START=(Date)startDt.clone();
        Date END = (Date)START.clone();
        //System.out.println("#$#$# START : "+START+"  valueDt : "+valueDt);
        int FREQ_A = CommonUtil.convertObjToInt(resultMap.get("FREQ_A"));
        int FREQ = CommonUtil.convertObjToInt(resultMap.get("FREQ"));
        while (DateUtil.dateDiff(valueDt, START)>0) {
            START = DateUtil.addDays(START, -1*FREQ_A);
        }
        Date startDate = null;
        while (DateUtil.dateDiff(END, endDt)>0) {
            double valueDtProdAmt = 0;
            END = DateUtil.addDays(START, FREQ_A);
            //System.out.println("#$#$# START : "+START+"   END : "+END);
            List valueDateNewList = new ArrayList();
            Date newValueDt = null;
            for (int v=0; v<valueDateList.size(); v++) {
                valueDateMap = (HashMap) valueDateList.get(v);
                newValueDt = (Date) valueDateMap.get("VALUE_DT");
                if ((DateUtil.dateDiff(START, newValueDt)>0) &&
                (DateUtil.dateDiff(newValueDt, END)>0)) {
                    valueDateNewList.add(valueDateMap);
                }
            }
            //            if (valueDateNewList.size()>0) {
            List lst = null;
            HashMap amtHash = null;
            Date chkDate = null;
            double dayEndAmount = 0;
            double nextAmount = 0;
            startDate = DateUtil.addDays((Date) START.clone(),1);
            Date END_DT = null;
            while (DateUtil.dateDiff(startDate, END)>0) {
                END_DT =  DateUtil.addDays(startDate, FREQ);
                periodMap.put(START, startDate);
                periodMap.put("TODAY_DT", END_DT);
                
                //System.out.println("#$#$# periodMap in performValueDate 2 : "+periodMap);
                
                lst = ClientUtil.executeQuery("getDailyBalanceForValueDateNew", periodMap);
                //System.out.println("#$#$# old dailyProductLst in performValueDate 2 : "+lst);
                if (lst.size()>0) {
                    for (int v=0; v<lst.size(); v++) {
                        amtHash = (HashMap)lst.get(v);
                        chkDate = (Date)amtHash.get("DAY_END_DT");
                        dayEndAmount = CommonUtil.convertObjToDouble(amtHash.get("AMT")).doubleValue();
                        for (int j=0; j<valueDateNewList.size(); j++) {
                            valueDateMap = (HashMap)valueDateNewList.get(j);
                            transDt = (Date) valueDateMap.get("TRANS_DT");
                            valueDt = (Date) valueDateMap.get("VALUE_DT");
                            transType = CommonUtil.convertObjToStr(valueDateMap.get("TRANS_TYPE"));
                            nextAmount = CommonUtil.convertObjToDouble(valueDateMap.get(AMOUNT)).doubleValue();
                            if (transType.equals("DEBIT")) {
                                nextAmount = -1*nextAmount;
                            }
                            if (DateUtil.dateDiff(valueDt, chkDate)==0) {
                                addAmount = addAmount + nextAmount;
                            }
                            if (DateUtil.dateDiff(transDt, chkDate)==0) {
                                nextAmount = -1*nextAmount;
                                addAmount = addAmount + nextAmount;
                            }
                        }
                        dayEndAmount+=addAmount;
                        amtHash.put("AMT", new Double(dayEndAmount));
                    }
                    dailyProductLst = new ArrayList();
                    for (int v=0; v<lst.size(); v++) {
                        amtHash = (HashMap)lst.get(v);
                        dailyProductLst.add(amtHash.get("AMT"));
                    }
                    //System.out.println("#$#$# new dailyProductLst in performValueDate 2 : "+dailyProductLst);
                    
                    dailyProductHashMap.put((startDate.getMonth()+1)+"-"+(startDate.getYear()+1900), dailyProductLst);
                    //System.out.println("#$#$# dailyProductLst in performValueDate 2 : "+dailyProductHashMap);
                    double[] sortList = new double[dailyProductLst.size()];
                    
                    for (int v=0; v<dailyProductLst.size(); v++) {
                        sortList[v] = CommonUtil.convertObjToDouble(dailyProductLst.get(v)).doubleValue();
                    }
                    java.util.Arrays.sort(sortList);
                    valueDtProdAmt += sortList[0];
                    //System.out.println("#$#$# final amtLst : "+sortList[0]);
                }
                //System.out.println("#$#$# final valueDtProdAmt : "+valueDtProdAmt);
                startDate = (Date) END_DT.clone();
            }
            
            Date currDt = DateUtil.addDays(START, -1);
            //System.out.println("##@@## lstintCr = "+lstintCr+"##@@## currDt = "+currDt);
            String valueDtTransMode = "";
            if (DateUtil.dateDiff(currDt, lstintCr)!=0) {  // No need to take old balance for the current period
                int add_months = 0;
                double amount = 0;
                int lstSize = 0;
                periodMap.put(START_DT, START);
                periodMap.put("TODAY_DT", END);
                
                //                if (paramMap.containsKey("ADD_MONTHS"))
                //                    add_months = CommonUtil.convertObjToInt(paramMap.get("ADD_MONTHS"));
                periodMap.put("ADD_MONTHS", new Integer(add_months));
                //        //System.out.println("super._branchCode : " + super._branchCode);
                //System.out.println("#$@## periodMap for old product amount = " + periodMap);
                List amtLst = ClientUtil.executeQuery("getDailyBalance", periodMap);
                lstSize = amtLst.size();
                //System.out.println("Amount List:" + amtLst);
                for (int ai=0; ai < lstSize; ai++ ) {
                    amount += CommonUtil.convertObjToDouble(amtLst.get(ai)).doubleValue();
                }
                //System.out.println("#### oldProdAmt = " + amount);
                resultMap.put("PROD_AMOUNT", new Double(amount-valueDtProdAmt));
                valueDtTransMode = "REVERSAL";
            }
            if (!valueDtTransMode.equals("REVERSAL")) {
                valueDtTransMode = "NORMAL";
                resultMap.put("PROD_AMOUNT", new Double(valueDtProdAmt));
            }
            resultMap.put("VALUE_DT_MODE", valueDtTransMode);
            //            runBatch(resultMap, paramMap, objLogDAO, objLogTO);
            START = (Date) END.clone();
            
        }
        //        //System.out.println("#$#$# final PRDUCT DIFFERENCE AMOUNT For Old Period  : "+amount);
        //        return null;
    }
    
    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
        // TODO add your handling code here:

        try {
            btnView.setVisible(false);
            String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
            if (cboProdType.getSelectedIndex() <= 0) {  //Added By Suresh
                ClientUtil.showMessageWindow("Product Type Should Not be Empty !!!");
                return;
            }
            if (cboProductId.getSelectedIndex() <= 0) {
                ClientUtil.showMessageWindow("Product ID Should Not be Empty !!!");
                return;
            }
            if(!prodType.equals("TD") &&  !prodType.equals("SH")){
                if(rdoCharges.isSelected() == false && rdoInterest.isSelected()==false){
                    ClientUtil.showMessageWindow("Select either Interest calculation or Charges calculation!!!");
                    return;
                }
            }
            if (rdoCharges.isSelected() && !(chkNonMaintenanceOFMinBalCharges.isSelected() || chkFolioCharges.isSelected()||chkInoperativeCharges.isSelected())) { //!chkNonMaintenanceOFMinBalCharges.isSelected() by abi
                ClientUtil.showMessageWindow("Please Select Any One charges !!!");//Please Select Non-Maintenance Of Minimum Balance !!!
                return;
            }
            if (rdoCharges.isSelected() || rdoInterest.isSelected()) {
//                if (txtFromAccount.getText().length() <= 0) {
//                    ClientUtil.showAlertWindow("From Account Number Should not be Empty !!! ");
//                    return;
//                }
//                if (txtToAccount.getText().length() <= 0) {
//                    ClientUtil.showAlertWindow("To Account Number Should not be Empty !!! ");
//                    return;
//                }
                if (tdtFromDate.getDateValue().length() <= 0) {
                    ClientUtil.showAlertWindow("From Date Should not be Empty !!! ");
                    return;
                }
                if (tdtToDate.getDateValue().length() <= 0) {
                    ClientUtil.showAlertWindow("To Date Should not be Empty !!! ");
                    return;
                }
            }   
            
            //Added by kannan refer rajesh sir confirmation for interest running....
            //String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
            if (rdoInterest.isSelected() && (prodType.equals("OA")||prodType.equals("TD")) ) {   
                if (tdtToDate.getDateValue().length() <= 0) {
                    ClientUtil.showAlertWindow("To Date Should not be Empty !!! ");
                    return;
                }
            }
            if (!chkTrial.isSelected() ) { //rdoInterest.isSelected() &&  && (prodType.equals("OA")||prodType.equals("TD"))
                int yesNo = 0;
                String[] options = {"Yes", "No"};
                yesNo = COptionPane.showOptionDialog(null, "Are you sure apply the Transaction for this product?", CommonConstants.WARNINGTITLE,
                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                        null, options, options[0]);
                if (yesNo > 0) {
                    return;
                }
            }
            
            if (!taskRunning){
                observable.resetAll();
                //__ To Set the ProdType in the task Header...
                //            observable.setProdType("OA");
                
                HashMap dataMap = new HashMap();
                dataMap.put("CHARGES_PROCESS","CHARGES_PROCESS");
                dataMap.put(CommonConstants.PRODUCT_ID, CommonUtil.convertObjToStr(((ComboBoxModel) cboProductId.getModel()).getKeyForSelected()));
                dataMap.put(CommonConstants.PRODUCT_TYPE, CommonUtil.convertObjToStr(((ComboBoxModel) cboProdType.getModel()).getKeyForSelected()));
                if(txtFromAccount.getText()!=null && (!txtFromAccount.getText().equals(""))) {
                    dataMap.put("ACT_FROM", CommonUtil.convertObjToStr(txtFromAccount.getText()));
                }
                if(txtToAccount.getText()!=null && (!txtToAccount.getText().equals(""))) {
                    dataMap.put("ACT_TO", CommonUtil.convertObjToStr(txtToAccount.getText()));
                }
                Date tempDt = (Date)CurDate.clone();
                //                Date fromDt = DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue());
                //                Date toDt = DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue());
                Date fromDt = DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue());
                if(fromDt != null){
                    tempDt.setDate(fromDt.getDate());
                    tempDt.setMonth(fromDt.getMonth());
                    tempDt.setYear(fromDt.getYear());
                    dataMap.put("DATE_FROM", tempDt);
                }
                // Commented by Rajesh because if from date not given no need to pass it takes from Deposit_provision table.  
                // for SB interest calculation no need to pass
//                else{
//                    dataMap.put("DATE_FROM", DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue()));
//                }
                tempDt = (Date)CurDate.clone();
                Date toDt = DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue());
                if(toDt != null){
                    tempDt.setDate(toDt.getDate());
                    tempDt.setMonth(toDt.getMonth());
                    tempDt.setYear(toDt.getYear());
                    dataMap.put("DATE_TO", tempDt);
                }
                // Commented by Rajesh because if to date not given no need to pass it takes from Deposit_provision table and adds frequency.  
                // for SB interest calculation no need to pass
//                else{
//                    dataMap.put("DATE_TO", DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue()));
//                }
                dataMap.put(CommonConstants.BRANCH, ProxyParameters.BRANCH_ID);
                dataMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
                //            observable.setDataMap(dataMap);               
                //System.out.println("dataMap: " + dataMap);
                TaskHeader header = null;
                //__ if the Tasks are of Charges Collection type
//                String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
                if(prodType.equals("TD") && (txtFromAccount.getText().length() == 0 || txtToAccount.getText().length() == 0) &&
                chkFolioCharges.isSelected() == true && chkNonMaintenanceOFMinBalCharges.isSelected() == true){
                    boolean processing = false;
                    String[] obj4 = {"Yes","No"};
                    int option3 = COptionPane.showOptionDialog(null,("From Account No & To Account No is empty,\n" +
                    "If you continue interest will be credited to interest payable account head,\n" +
                    "for all FIXED deposit accounts whichever is pending, Do you want to Continue ?"), ("Deposits"),
                    COptionPane.YES_NO_CANCEL_OPTION,COptionPane.QUESTION_MESSAGE,null,obj4,obj4[0]);
                    if(option3 == 1){
                        return;
                    }
                }
                HashMap map=new HashMap();
                if(prodType.equals("TL") || prodType.equals("AD")){
                    String prodId =CommonUtil.convertObjToStr(cboProductId.getSelectedItem());
                    if(prodId.length()==0){
                           ClientUtil.showAlertWindow("Please Select Product Id");
                        return;
                    }
                    if( tdtToDate.getDateValue()  == null || tdtToDate.getDateValue().equals("")){
                        ClientUtil.showAlertWindow("Enter the Todate");
                        return;
                    }
                    if(prodType.equals("AD") && chkCredit.isSelected()){
                        HashMap whereMap=new HashMap();
                        whereMap.put("PROD_ID",((ComboBoxModel)cboProductId.getModel()).getKeyForSelected().toString());
                        List lst=ClientUtil.executeQuery("icm.getProductsAD", whereMap);
                        if(lst !=null && lst.size()>0){
                            HashMap resultMap =(HashMap)lst.get(0);
                            int freq=CommonUtil.convertObjToInt(resultMap.get("CR_INT_APPL_FREQ"));
                            Date lastAppDt= DateUtil.addDays((Date)resultMap.get("LAST_APPL_DT_CR"),freq);
                            if(DateUtil.dateDiff(lastAppDt,DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue()))<0){
                                ClientUtil.showMessageWindow("Interest  Not Calculated "+"\n"+"Next Interest Calculation Date is  :" +CommonUtil.convertObjToStr(DateUtil.addDays((Date)resultMap.get("LAST_APPL_DT_CR"),freq)) 
                              );
                                return;
                            }
                        }
                    }
                    List lst=null;
                    if(rdoInterest.isSelected()) {
                        lst=ClientUtil.executeQuery("getLoanAccountHeads", dataMap);
                    }
                    if(lst !=null && lst.size()>0){
                        map=(HashMap)lst.get(0);
                        if(map.get("AS_CUSTOMER_COMES").equals("Y")){
                            ClientUtil.showMessageWindow("Change the Parameter For Interest" +
                            " Calculation As based on"+"\n"+
                            "CALENDER FREQUENCY at Prduct Level And" + "\n"+ "Do Interest Calculation");
                            return;
                        }
                    }
                }
                updateTable();
              //  List loanFacilityDts=null;
                 prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
                if (prodType.equals("AD") && rdoInterest.isSelected()) {
                    HashMap amap = new HashMap();
                    amap.put("FROM_AC", txtFromAccount.getText());
                    amap.put("TO_AC", txtToAccount.getText());
                    Date today = DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue());
                    amap.put("TODAY", today);
                    loanFacilityDts = ClientUtil.executeQuery("getLoanFacilityforAd", amap);
                    // //System.out.println("loanFacilityDts..."+loanFacilityDts);    
        }
                
                threadPool = new ThreadPool(observable);
               // //System.out.println("observable.getTaskList()"+observable.getTaskList());
                threadPool.setTaskList(observable.getTaskList());
                threadPool.initPooledThread();
                threadPool.start();
                taskRunning = true;
                ClientUtil.enableDisable(panChargesApplication , false);
                if(!prodType.equals("TD") &&  !prodType.equals("SH")){
                    if(rdoCharges.isSelected()){
                        if (chkExcessTransactionCharges.isSelected()){
                            //__ To Set the TransactionType in the task Header...
                            header = getTaskFromMap(dataMap);
                            header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkExcessTransactionCharges.getText())));
                            header.setTransactionType("EXCESSTRANSCHARGE");
                            observable.setEachTask(chkExcessTransactionCharges, header);
                        }
                        if (chkFolioCharges.isSelected()){
                            //__ To Set the TransactionType in the task Header...
                            header = getTaskFromMap(dataMap);
                            header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkFolioCharges.getText())));
                            header.setTransactionType("FOLIOCHG");
                            observable.setEachTask(chkFolioCharges, header);
                        }
                        if (chkInoperativeCharges.isSelected()){
                            //__ To Set the TransactionType in the task Header...
                            header = getTaskFromMap(dataMap);
                            header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkInoperativeCharges.getText())));
                            header.setTransactionType("IN_OPERATIVE");
                            observable.setEachTask(chkInoperativeCharges, header);
                        }
                        if (chkNonMaintenanceOFMinBalCharges.isSelected()){
                            //__ To Set the TransactionType in the task Header...
                            header = getTaskFromMap(dataMap);
                            header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkNonMaintenanceOFMinBalCharges.getText())));
                            header.setTransactionType("MIN_BALANCE");
                            observable.setEachTask(chkNonMaintenanceOFMinBalCharges, header);
                        }
                        
                        //__ if the Tasks are of Interest Calculation type
                    } else if (rdoInterest.isSelected()) {
                        if (chkTrial.isSelected()) {  //Added by kannan refer rajesh sir
                            dataMap.put("TRIAL", "Y");
                            dataMap.put("USER_ID",TrueTransactMain.USER_ID);
                        } else {
                            dataMap.put("TRIAL", "N");
                        }                        
                        //__ if the Transaction Type Is Credit...
                        if (chkCredit.isSelected()){
                            //__ To Set the TransactionType in the task Header...
                            header = getTaskFromMap(dataMap);
                            header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkCredit.getText())));
                            header.setTransactionType(CommonConstants.PAYABLE);
                            header.setDB_DRIVER_NAME(ProxyParameters.dbDriverName); // Added by nithya
                            observable.setEachTask(chkCredit, header);
                            CREDIT = 1;
                        }
                        //__ if the Transaction Type Is Debit...
                        if (chkDebit.isSelected()){
                            //__ To Set the TransactionType in the task Header...
                            header = getTaskFromMap(dataMap);
                            if(! dataMap.containsKey("CHARGESUI")) {
                                header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkDebit.getText())));
                            }
                            header.setTransactionType(CommonConstants.RECEIVABLE);
                            header.setDB_DRIVER_NAME(ProxyParameters.dbDriverName); // Added by nithya
                            observable.setEachTask(chkDebit, header);
                            //System.out.println("header####"+header);
                            DEBIT = 1;
                        }
                    }
                }else {
                    if(prodType.equals("SH")){
                        HashMap taskMap = new HashMap();
                        taskMap.put("TASK", "DividendCalcTask");
                        observable.setTaskMap(taskMap);
                        if(chkInoperativeCharges.isSelected()) {
                            dataMap.put("REMARKS","CALCULATION");
                            dataMap.put("CHARGES_PROCESS","CHARGES_PROCESS");
                            dataMap.put("PROCESS","CALCULATION");
                            //                        dataMap.remove("ACT_FROM");
                            //                        dataMap.remove("ACT_TO");
                            header = getTaskFromDepositMap(dataMap);
                            header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get("TASK")));
                            header.setTransactionType(CommonConstants.PAYABLE);
                            observable.setEachTask(chkInoperativeCharges, header);
                        }
                        if (chkNonMaintenanceOFMinBalCharges.isSelected()){
                            dataMap.put("REMARKS","TRANSFER");
                            dataMap.put("CHARGES_PROCESS","CHARGES_PROCESS");
                            dataMap.put("PROCESS","TRANSFER");
                            header = getTaskFromDepositMap(dataMap);
                            header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get("TASK")));
                            header.setTransactionType(CommonConstants.PAYABLE);
                            observable.setEachTask(chkNonMaintenanceOFMinBalCharges, header);
                        }
                        //System.out.println("#####header : "+header);
                    } else {
                        HashMap taskMap = new HashMap();
                        taskMap.put("TASK", "DepositIntTask");
                        observable.setTaskMap(taskMap);
                        //Added by kannan
                        if (chkTrial.isSelected()) {
                            dataMap.put("TRIAL", "Y");
                            dataMap.put("USER_ID", TrueTransactMain.USER_ID);
                        } else {
                            dataMap.put("TRIAL", "N");
                        }
                        if (tdtToDate.getDateValue() != null) {
                            dataMap.put("PROVISION_DATE", DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue()));
                        }
                        if (tdtFromDate.getDateValue() != null) {
                            dataMap.put("FROM_DT", DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue()));
                        }
                        
                        // 08-05-2020 
                        String specialRDScheme = "N";
                        HashMap roiParamMap = new HashMap();
                        roiParamMap.put("PROD_ID",CommonUtil.convertObjToStr(((ComboBoxModel) cboProductId.getModel()).getKeyForSelected()));
                        List specialRDCompleteLst = ClientUtil.executeQuery("getSpecialRD", roiParamMap);
                        if (specialRDCompleteLst != null && specialRDCompleteLst.size() > 0) {
                            HashMap specialRDMap = (HashMap) specialRDCompleteLst.get(0);
                            if (specialRDMap.containsKey("SPECIAL_RD") && specialRDMap.get("SPECIAL_RD") != null && CommonUtil.convertObjToStr(specialRDMap.get("SPECIAL_RD")).equalsIgnoreCase("Y")) {
                               dataMap.put("SPECIAL_RD_SCHEME","SPECIAL_RD_SCHEME");
                            }
                            if (specialRDMap.containsKey("BEHAVES_LIKE") && specialRDMap.get("BEHAVES_LIKE") != null && CommonUtil.convertObjToStr(specialRDMap.get("BEHAVES_LIKE")).equalsIgnoreCase("RECURRING")) {
                               dataMap.put("RD_INT_APPLICATION","RD_INT_APPLICATION");
                            }
                        }                      
                        // end

                        if (chkInoperativeCharges.isSelected()) {
                            dataMap.put("REMARKS", "PROVISIONING");
                            dataMap.put("CHARGES_PROCESS", "CHARGES_PROCESS");
                            dataMap.put("PROCESS", "DAY_END");
                            //                        dataMap.remove("ACT_FROM");
                            //                        dataMap.remove("ACT_TO");
                            header = getTaskFromDepositMap(dataMap);
                            header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get("TASK")));
                            header.setTransactionType(CommonConstants.PAYABLE);
                            observable.setEachTask(chkInoperativeCharges, header);
                        }
                        if (chkNonMaintenanceOFMinBalCharges.isSelected()) {
                            dataMap.put("REMARKS", "APPLICATION");
                            dataMap.put("CHARGES_PROCESS", "CHARGES_PROCESS");
                            dataMap.put("PROCESS", "DAY_BEGIN");
                            if (chkExcessTransactionCharges.isSelected() || chkFolioCharges.isSelected()) {
                                if (chkFolioCharges.isSelected()) {
                                    dataMap.put("INTPAY_MODE", "CASH");
                                }
                                if (chkExcessTransactionCharges.isSelected()) {
                                    dataMap.put("INTPAY_MODE", "TRANSFER");
                                }

                                if (chkExcessTransactionCharges.isSelected() && chkFolioCharges.isSelected()) {
                                    dataMap.put("INTPAY_MODE", null);
                                }
                            } else {
                                displayAlert("Please Select Transfer Or Cash...");
                                return;
                            }
                            header = getTaskFromDepositMap(dataMap);
                            header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get("TASK")));
                            header.setTransactionType(CommonConstants.PAYABLE);
                            observable.setEachTask(chkNonMaintenanceOFMinBalCharges, header);
                        }
                        //                    if (chkCredit.isSelected()){
                        //                        //__ To Set the TransactionType in the task Header...
                        //                        header = getTaskFromDepositMap(dataMap);
                        //                        header.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get("TASK")));
                        //                        header.setTransactionType(CommonConstants.PAYABLE);
                        //                        observable.setEachTask(chkCredit, header);
                        //                        CREDIT = 1;
                        //                    }
                        //System.out.println("#####header : "+header);
                    }
                }
                //System.out.println("hhhhhaaa");
                updateTable();
                threadPool = new ThreadPool(observable);
                //System.out.println("fhfhgfh observable.getTaskList()"+observable.getTaskList());
                threadPool.setTaskList(observable.getTaskList());
                threadPool.initPooledThread();
                threadPool.start();
                taskRunning = true;
                //System.out.println("bbbbbkk");
//                updateTable();

               ClientUtil.enableDisable(panChargesApplication , false);
              //Added By Suresh
                if (rdoCharges.isSelected()) {
                    btnProcess.setEnabled(false);
                }
                if (rdoInterest.isSelected() && chkTrial.isSelected() && (prodType.equals("OA") || prodType.equals("TD") || prodType.equals("AD"))) {
                    btnView.setVisible(true);
                }
                if (chkTrial.isSelected() && prodType.equals("TD") && chkInoperativeCharges.isSelected()) {
                    btnView.setVisible(true);
                }
            } else {
                final String warnMessage = "Process is running";
                displayAlert(warnMessage);
            }
        } catch (Exception e) {
            //System.out.println("######"+e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnProcessActionPerformed

    private void chkExcessTransactionChargesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkExcessTransactionChargesActionPerformed
        // TODO add your handling code here:
        //System.out.println("Excess Transaction");
    }//GEN-LAST:event_chkExcessTransactionChargesActionPerformed
    //__ To Update the Data regarding the Execution of the Selected Task(s) in the Table
    private void updateTable(){
        //System.out.println("observable.getDayBeginTableModel()"+observable.getDayBeginTableModel());
        tblLog.setModel(observable.getDayBeginTableModel());
        
    }
    
//    private void UpdateAfterOD()
//    { 
//          final ArrayList dayTabTitle = new ArrayList();
//          dayTabTitle.add("Task");
////            dayTabTitle.add("Actual count");
////            dayTabTitle.add("Execution count");
//            dayTabTitle.add("Status");
//       EnhancedTableModel dayTableModel = new EnhancedTableModel(null, dayTabTitle);
//        ArrayList exeTask = new ArrayList();
//        if(rdoInterest.isSelected())
//        exeTask.add("Interest Calculation");  //Task
//        else
//         exeTask.add("Charge Calculation");
//       // String cnt = String.valueOf(random.nextInt(20));
////        exeTask.add(cnt);               //Actual count
////        exeTask.add(cnt);               //Execution count
//        exeTask.add("COMPLETED");   
//        //System.out.println("in Process OBBB exe11"+exeTask);//Status
//        dayTableModel.addRow(exeTask);
//        
//         tblLog.setModel(dayTableModel);
//    }
//            
//            
//    
    public void update(java.util.Observable o, Object arg) {
        //        cboProdType.setSelectedItem(observeChargeOB.getCboProdType());
        this.tskStatus = observable.getTaskStatus();
        this.tskHeader = observable.getTaskHeader();
        //System.out.println("nnnn");
        updateChkBoxTableBasedOnStatus();
    }
    
    private void updateChkBoxTableBasedOnStatus(){
        String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
        boolean enable = false;
        
        //System.out.println("bvbvbvb");
        //__ Charges Calculation Tasks...
        if (tskHeader.getTaskClass().equals("FolioChargesTask")){
            enable = observable.updateTaskStatusInLogTable(chkFolioCharges);
        }
        if (tskHeader.getTaskClass().equals("ExcessTransChrgesTask")){
            enable = observable.updateTaskStatusInLogTable(chkExcessTransactionCharges);
        }
        if (tskHeader.getTaskClass().equals("InOperativeChargesTask")){
            enable = observable.updateTaskStatusInLogTable(chkInoperativeCharges);
        }
        if (tskHeader.getTaskClass().equals("MinBalanceChargesTask")){
            enable = observable.updateTaskStatusInLogTable(chkNonMaintenanceOFMinBalCharges);
        }
        if (tskHeader.getTaskClass().equals("DividendCalcTask")){
            enable = observable.updateTaskStatusInLogTable(chkInoperativeCharges);
        }
        if (tskHeader.getTaskClass().equals("DividendCalcTask")){
            enable = observable.updateTaskStatusInLogTable(chkNonMaintenanceOFMinBalCharges);
        }
        if(prodType.equals("TL")){
            if (tskHeader.getTaskClass().equals("InterestCalculationTask")){
                enable = observable.updateTaskStatusInLogTable(chkDebit);
            }}
          if(prodType.equals("AD")){
            if (tskHeader.getTaskClass().equals("InterestCalculationTask")){
                enable = observable.updateTaskStatusInLogTable(chkDebit);
            }}
        if(prodType.equals("TD")){
            if (tskHeader.getTaskClass().equals("DepositIntTask")){
                enable = observable.updateTaskStatusInLogTable(chkInoperativeCharges);
            }
            if (tskHeader.getTaskClass().equals("DepositIntTask")){
                enable = observable.updateTaskStatusInLogTable(chkNonMaintenanceOFMinBalCharges);
            }
        }
        //__ Interest Calculation Tasks...
        if (tskHeader.getTaskClass().equals("InterestTask")){
            if(CREDIT == 1){
                enable = observable.updateTaskStatusInLogTable(chkCredit);
                //System.out.println("dxzf");
            }
            if(DEBIT == 1){
                enable = observable.updateTaskStatusInLogTable(chkDebit);
                //System.out.println("nnnnnn");
            }
        }
        
        updateTable();
        //System.out.println("enable,,k"+enable);
      
        
      
        
          if (enable){
            taskRunning = false;
            ClientUtil.enableDisable(panChargesApplication , true);
            updateLoan();
            if(yes)
            {
            threadPool.stopAllThreads();
            }
        }
         //System.out.println("nnnnnaSS");
       //  updateLoan();
        CREDIT = 0;
        DEBIT = 0;
    }
    
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
   private void updateLoan()
   {
       try
               {
        String s="";
         //System.out.println("tblLog.getRowCount()"+tblLog.getRowCount()+"sad"+tblLog.getColumnCount());
        if(tblLog.getRowCount()>0 )
        {
         s=tblLog.getValueAt(0,1).toString();
       
        }
        else
        {
         s="";
        }
        //System.out.println("sssss"+s);
        if(!s.equals(""))
        {
        if(s.equals("COMPLETED"))
        {
            
           String  prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
            //System.out.println("prodType.aS."+prodType);
            //System.out.println("loanFacilityDts"+loanFacilityDts);
            //System.out.println("rdoInterest.isSelected()sadsa"+rdoInterest.isSelected());
             //System.out.println("vvvv"+loanFacilityDts.size()); 
            if(prodType.equals("AD") && rdoInterest.isSelected()){
                     if(loanFacilityDts!=null && loanFacilityDts.size()>0)
                     {
                        for(int i=0;i<loanFacilityDts.size();i++)
                        {
                            HashMap loanMap=new HashMap();
                            loanMap=(HashMap)loanFacilityDts.get(i);
                            //System.out.println("loanMapasdas"+loanMap);
                            double peanl= Double.parseDouble(loanMap.get("PENAL").toString());
                            double interet= Double.parseDouble(loanMap.get("INTEREST").toString());
                            
                           
                            
                            
                           double clear= Double.parseDouble(loanMap.get("CLEAR_BALANCE").toString());
                              double available= Double.parseDouble(loanMap.get("AVAILABLE_BALANCE").toString());
                                double total_bal= Double.parseDouble(loanMap.get("TOTAL_BALANCE").toString());
                                  double loaqn_bal= Double.parseDouble(loanMap.get("LOAN_BALANCE_PRINCIPAL").toString());
                            
                                  double total=interet+peanl;
                                  
                                  
                                  
                            loanMap.put("CLEAR_BALANCE",(clear-total));
                             loanMap.put("AVAILABLE_BALANCE",(available-total));
                              loanMap.put("TOTAL_BALANCE",(total_bal-total));
                               loanMap.put("LOAN_BALANCE_PRINCIPAL",(loaqn_bal+total));
                            //System.out.println("loanMap...."+loanMap);
                       //    ClientUtil.execute("updateloanFacilityForOD", loanMap);
                           
//                            ClientUtil.execute("updateadvPenal",loanMap );
//                               ClientUtil.execute("updateadvPbal", loanMap);
//                                ClientUtil.execute("updateAdvParam", loanMap);
                            //System.out.println("over1111"+i);
                        }
                       
                     }
            
//            HashMap amap=new HashMap();
//            amap.put("FROM_AC", txtFromAccount.getText());
//            amap.put("TO_AC", txtToAccount.getText());
//            Date today=DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue());
//            amap.put("TODAY",today);
//             loanFacilityDts=ClientUtil.executeQuery("getLoanFacilityforAd", amap);
//            //System.out.println("loanFacilityDts..."+loanFacilityDts);    
        }
        }
        }
          yes=true;
               }
       catch(Exception e)
       {
           yes=false;
       }
   }
   private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        cboProdType.setSelectedItem("");
        cboProductId.setSelectedItem("");
        txtFromAccount.setText("");
        txtToAccount.setText("");
        tdtFromDate.setDateValue("");
        tdtToDate.setDateValue("");
        chkDebit.setSelected(false);
        chkCredit.setSelected(false);
        rdoInterest.setSelected(false);
        rdoCharges.setSelected(false);
        btnProcess.setEnabled(true);
//        btnChargesPrint.setEnabled(false); 
        resetForm();//Added by kannan
        //The following lines are Added By Suresh 21-Apr-2014
        observable.resetAll();
        updateTable();
        ClientUtil.enableDisable(panChargeType, false);
        chkNonMaintenanceOFMinBalCharges.setSelected(false);
        chkFolioCharges.setSelected(false);
        setvisibleTrial(false);
        rdoInterest.setEnabled(true);
        rdoCharges.setEnabled(true);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void resetForm(){
        cboProdType.setEnabled(true);
        cboProductId.setEnabled(true);
        btnView.setVisible(false);
        chkTrial.setSelected(false);
        chkTrial.setEnabled(true); 
        Invisible(true);
        tdtFromDate.setEnabled(true);
        tdtToDate.setEnabled(true);
    }
    private void setInvisible(){
        chkExcessTransactionCharges.setEnabled(false);
        chkFolioCharges.setEnabled(false);
        chkInoperativeCharges.setEnabled(false);
        chkNonMaintenanceOFMinBalCharges.setEnabled(false);
    }
	
	private void btnChargesPrintActionPerformed(java.awt.event.ActionEvent evt) {                                                
        //Added By Suresh R 24-March-2014   Trial Report Purpose Folio And Non-Maintain Minimum Balance
        if (rdoCharges.isSelected()) {
            if (chkFolioCharges.isSelected() && chkNonMaintenanceOFMinBalCharges.isSelected()) {
                ClientUtil.showMessageWindow("Please Select Any One Charge, Folio or Non-Maintenance of Minimum Balance !!!");
                return;                
            }
            if (cboProdType.getSelectedIndex() > 0) {
                String PRODTYPE = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdType.getModel()).getKeyForSelected());
                if (PRODTYPE.equals("OA") || PRODTYPE.equals("AD")) {
                    if (cboProductId.getSelectedIndex() > 0) {
                        if (txtFromAccount.getText().length() > 0) {
                            if (txtToAccount.getText().length() > 0) {
                                if (tdtFromDate.getDateValue().length() > 0) {
                                    if (tdtToDate.getDateValue().length() > 0) {
                                        if (chkFolioCharges.isSelected() || chkNonMaintenanceOFMinBalCharges.isSelected()) {
                                            String PRODID = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductId.getModel()).getKeyForSelected());
                                            String FROM_ACT_NUM = txtFromAccount.getText();
                                            String TO_ACT_NUM = txtToAccount.getText();
                                            java.util.Date fromDate = (java.util.Date) DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue());
                                            java.util.Date toDate = (java.util.Date) DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue());
                                            TTIntegration ttIntgration = null;
                                            HashMap paramMap = new HashMap();
                                            paramMap.put("prod_type", PRODTYPE);
                                            paramMap.put("prod_id", PRODID);
                                            paramMap.put("fractnum", FROM_ACT_NUM);
                                            paramMap.put("toactnum", TO_ACT_NUM);
                                            paramMap.put("fmdt", fromDate);
                                            paramMap.put("todt", toDate);
                                            paramMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
                                            TTIntegration.setParam(paramMap);
                                            if (chkNonMaintenanceOFMinBalCharges.isSelected()) {
                                                TTIntegration.integrationForPrint("non_maintance_min_bal_Report",true);
                                            } else if (chkFolioCharges.isSelected()) {
                                                TTIntegration.integrationForPrint("folio_charge_trail_Report",true);
                                            }
                                        } else {
                                            ClientUtil.showMessageWindow("Please Select Any One charges !!!");
                                            return;
                                        }
                                    } else {
                                        ClientUtil.showMessageWindow("To Date Should not be empty !!!");
                                        return;
                                    }
                                } else {
                                    ClientUtil.showMessageWindow("From Date Should not be empty !!!");
                                    return;
                                }
                            } else {
                                ClientUtil.showMessageWindow("To Account Number Should not be empty !!!");
                                return;
                            }
                        } else {
                            ClientUtil.showMessageWindow("From Account Number Should not be empty !!!");
                            return;
                        }
                    } else {
                        ClientUtil.showMessageWindow("Product ID Should not be empty !!!");
                        return;
                    }
                }
            } else {
                ClientUtil.showMessageWindow("Product Type Should not be empty !!!");
                return;
            }
        } else {
            ClientUtil.showMessageWindow("Please Select Charges Calculation Button !!!");
            return;
        }
    }
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        //Added by kannan        
        String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
        TTIntegration ttIntgration = null;
        HashMap paramMap = new HashMap();
        paramMap.put("PROD_ID", CommonUtil.convertObjToStr(((ComboBoxModel) cboProductId.getModel()).getKeyForSelected()));
        paramMap.put("USER_ID", ProxyParameters.USER_ID);
        java.util.Date toDate = (java.util.Date) DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue());
        paramMap.put("DAY_END_DT", toDate);
        paramMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
        if(txtFromAccount.getText().length()>0){
          paramMap.put("fractnum", txtFromAccount.getText());  
        }
        if(txtToAccount.getText().length()>0){
          paramMap.put("toactnum", txtToAccount.getText());  
        }
        ttIntgration.setParam(paramMap);
        if (prodType.equals("TD")) {
            ttIntgration.integration("dep_int_trail_Report");
        }
        if (prodType.equals("OA")) {
            ttIntgration.integration("OA_int_trail_Report");
        }   
        if (prodType.equals("AD")) {
            ttIntgration.integration("OD_int_trail_Report");
        }
    }//GEN-LAST:event_btnViewActionPerformed

    private void chkTrialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkTrialActionPerformed
        // TODO add your handling code here:
        String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
        if(prodType.equals("OA") || prodType.equals("TD") ||prodType.equals("AD")){
        if(chkTrial.isSelected()){
            Invisible(true);
//            btnView.setVisible(false);
            btnClearTrail.setVisible(true);
        }else{
            Invisible(true);
//            btnView.setVisible(true);
                        btnClearTrail.setVisible(false);

        }
        }
    }//GEN-LAST:event_chkTrialActionPerformed

    private void btnClearTrailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearTrailActionPerformed
       // TODO add your handling code here:
        HashMap finMap = new HashMap();
        finMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        finMap.put("TO_DATE", CurDate.clone());
        String PRODID = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductId.getModel()).getKeyForSelected());
        if (PRODID != null && PRODID.length() > 0) {
            finMap.put("PRODID", PRODID);
        } else {
            ClientUtil.displayAlert("Please select product type");
            return;
        }
       System.out.println("finalMap Clint util-->"+finMap);
       boolean flag= false;
       List deleteAcLst =ClientUtil.executeQuery("deleteAccountIntTrialAllTO", finMap);//changed by Revathi for Postgres 14/12/2023 reff by Rajesh.
       if(deleteAcLst != null && deleteAcLst.size()>0){
          flag = true;
       }else{
          flag = false; 
       }
       
       if(flag){
           ClientUtil.showAlertWindow("Clear Trail Successfully!!!");
           return;
       }
    }//GEN-LAST:event_btnClearTrailActionPerformed

    private void chkCreditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCreditActionPerformed
        // TODO add your handling code here:
        if(chkCredit.isSelected()){
            chkDebit.setSelected(false);
            
        }
        
    }//GEN-LAST:event_chkCreditActionPerformed

    private void chkDebitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkDebitActionPerformed
        // TODO add your handling code here:
        if(chkDebit.isSelected()){
            chkCredit.setSelected(false);
        }
    }//GEN-LAST:event_chkDebitActionPerformed
    //__ To Update the Data regarding the Execution of the Selected Task(s) in the Table

    private void Invisible(boolean flag){
        lblFromAccount.setVisible(flag);
        txtFromAccount.setVisible(flag);
        btnFromAccount.setVisible(flag);
        tdtFromDate.setVisible(flag);
        lblFromDate.setVisible(flag);
        panToAccount.setVisible(flag);
        txtFromAccount.setText("");
        txtToAccount.setText("");
        tdtFromDate.setDateValue("");
        tdtToDate.setDateValue("");
        btnClearTrail.setVisible(flag);
    }
    
    public static void main(String str[]) throws Exception{
        javax.swing.JFrame frm = new javax.swing.JFrame();
        ChargesUI db = new ChargesUI();
        frm.getContentPane().add(db);
        frm.show();
        db.show();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClearTrail;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnFromAccount;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CButton btnToAccount;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CComboBox cboProductId;
    private com.see.truetransact.uicomponent.CCheckBox chkCredit;
    private com.see.truetransact.uicomponent.CCheckBox chkDebit;
    private com.see.truetransact.uicomponent.CCheckBox chkExcessTransactionCharges;
    private com.see.truetransact.uicomponent.CCheckBox chkFolioCharges;
    private com.see.truetransact.uicomponent.CCheckBox chkInoperativeCharges;
    private com.see.truetransact.uicomponent.CCheckBox chkNonMaintenanceOFMinBalCharges;
    private com.see.truetransact.uicomponent.CCheckBox chkTrial;
    private com.see.truetransact.uicomponent.CLabel lblFromAccount;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblProductId;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace28;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToAccount;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
    private com.see.truetransact.uicomponent.CPanel panChargeType;
    private com.see.truetransact.uicomponent.CPanel panChargesApplication;
    private com.see.truetransact.uicomponent.CPanel panChoice;
    private com.see.truetransact.uicomponent.CPanel panCredit;
    private com.see.truetransact.uicomponent.CPanel panDebit;
    private com.see.truetransact.uicomponent.CPanel panFolioCharges;
    private com.see.truetransact.uicomponent.CPanel panInterestType;
    private com.see.truetransact.uicomponent.CPanel panInterestTypeDebit;
    private com.see.truetransact.uicomponent.CPanel panProductId;
    private com.see.truetransact.uicomponent.CPanel panStatementCharges;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CPanel panToAccount;
    private com.see.truetransact.uicomponent.CRadioButton rdoCharges;
    private com.see.truetransact.uicomponent.CRadioButton rdoInterest;
    private com.see.truetransact.uicomponent.CRadioButton rdoInterestNo;
    private com.see.truetransact.uicomponent.CRadioButton rdoInterestYes;
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

